---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: heptio, kubernetes, backup

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}

# Intégration Velero
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window} (anciennement appelé Heptio Ark) est un ensemble d'outils qui permet de sauvegarder et restaurer des clusters Kubernetes à l'aide de l'API S3. 

Velero se compose de deux parties :

* Un composant serveur qui s'exécute sur le cluster
* Une interface de ligne de commande qui s'exécute sur un client local

## Prérequis
{: #velero-prereqs}

Avant de commencer, vous devez disposer des éléments suivants :

* L'[`interface CLI IBM Cloud`](/docs/cli?topic=cloud-cli-getting-started){:new_window} doit être installée. 
* L'outil de ligne de commande [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} doit être installé et configuré afin de vous permettre d'établir une connexion avec votre cluster. 
* Une instance {{site.data.keyword.cos_short}}. 
* Un compartiment {{site.data.keyword.cos_short}}. 
* Des données d'identification HMAC avec droits d'accès Editeur sur le compartiment. 

## Installation du client Velero
{: #velero-install}

1. Téléchargez la [version](https://github.com/heptio/velero/releases){:new_window} la plus récente de Velero pour votre système d'exploitation. 
2. Extrayez le fichier .tar dans un dossier sur votre système local. 
3. Vérifiez que vous pouvez exécuter le fichier binaire `velero` :

```
velero --help
```

```
Velero est un outil de gestion de la reprise après incident, en particulier pour les ressources de cluster de Kubernetes. Il fournit une méthode simple, configurable et robuste sur le plan opérationnel pour sauvegarder votre état d'application et les données qui lui sont associées. Si vous connaissez kubectl, Velero prend en charge un modèle similaire, vous permettant d'exécuter des commandes telles que 'velero get backup' et 'velero create schedule'. Ces commandes sont équivalentes à 'velero backup get' et 'velero schedule create'.

Syntaxe :
  velero [commande]

Commandes disponibles :
  backup            Gérer des sauvegardes
  backup-location   Gérer des emplacements de stockage de sauvegarde
  bug               Signaler un bogue Velero
  client            Commandes liées au client Velero
  completion        Afficher le code achèvement pour l'interpréteur de commandes spécifié (bash ou zsh)
  create            Créer des ressources Velero
  delete            Supprimer des ressources Velero
  describe          Décrire des ressources Velero
  get               Obtenir des ressources Velero
  help              Afficher l'aide sur une commande
  plugin            Gérer des plug-in
  restic            Gérer des restrictions
  restore           Gérer des restaurations
  schedule          Gérer des plannings
  server            Exécuter le serveur Velero
  snapshot-location Gérer des emplacements d'instantané
  version           Imprimer la version Velero et l'image qui lui est associée
...
```

*(Facultatif)* Déplacez le fichier binaire ark hors du dossier temporaire vers un emplacement plus permanent, tel que `/usr/local/bin` sur Mac OS ou Linux.
{: tip}

## Installation et configuration du serveur Velero
{: #velero-config}
### Création d'un fichier de données d'identification
{: #velero-config-credentials}

Créez un fichier de données d'identification (`credentials-velero`) à l'aide des touches HMAC dans votre dossier Velero local (*dossier dans lequel le fichier .tar a été extrait*). 

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Configuration de kubectl
{: #velero-config-kubectl}

Configurez [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} pour établir une connexion à votre cluster. 

1. Connectez-vous à la plateforme IBM Cloud à l'aide de l'interface CLI. <br/><br/>*Pour plus de sécurité, il est également possible de stocker la clé d'API dans un fichier ou de la définir en tant que variable d'environnement.*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. Extrayez la configuration de cluster.
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. Copiez et collez la commande **export** pour définir la variable d'environnement KUBECONFIG. 

4. Assurez-vous que `kubectl` est correctement configuré en exécutant ce qui suit :
    ```bash
    kubectl cluster-config
    ```
    {: pre}
  
    ```bash
    Kubernetes master is running at https://c6.hou02.containers.cloud.ibm.com:29244
    Heapster is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/heapster/proxy
    KubeDNS is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
    kubernetes-dashboard is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy
    ```
    {: pre}

### Configuration du serveur Velero et du stockage en cloud
{: #velero-config-storage}

1. Dans le dossier Velero, exécutez la commande suivante pour configurer des espaces de nom, RBAC et d'autres structurations. <br/><br/>*L'espace de nom par défaut est `velero`. Si vous souhaitez créer un espace de nom personnalisé, reportez-vous aux instructions décrites sur la page ['Run in custom namespace'](https://heptio.github.io/velero/master/namespace.html){:new_window}*.
    ```bash
    kubectl apply -f config/common/00-prereqs.yaml
    ```
    {: pre}

    ```bash
    customresourcedefinition.apiextensions.k8s.io/backups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/schedules.velero.io created
    customresourcedefinition.apiextensions.k8s.io/restores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/downloadrequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/deletebackuprequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumebackups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumerestores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/resticrepositories.velero.io created
    customresourcedefinition.apiextensions.k8s.io/backupstoragelocations.velero.io created
    customresourcedefinition.apiextensions.k8s.io/volumesnapshotlocations.velero.io created
    namespace/velero created
    serviceaccount/velero created
    clusterrolebinding.rbac.authorization.k8s.io/velero created
    ```
    {: pre}
2. Créez un secret avec votre fichier de données d'identification.
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. Spécifiez les valeurs suivantes dans `config/ibm/05-ark-backupstoragelocation.yaml` :
   * `<YOUR_BUCKET>` - Nom du compartiment dans lequel stocker les fichiers de sauvegarde. 
   * `<YOUR_REGION>` - [Contrainte d'emplacement](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) de votre compartiment (`us-standard`). 
   * `<YOUR_URL_ACCESS_POINT>` - URL de noeud final régional (`https://s3.us.cloud-object-storage.appdomain.cloud`). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

    *Pour plus d'informations, voir la définition [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window}. *

### Démarrage du serveur Velero
{: #velero-config-server}

1. Dans le dossier Velero, exécutez la commande suivante pour créer l'objet dans votre cluster
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. Exécutez la commande suivante pour créer le déploiement :
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. Assurez-vous que le déploiement a été correctement planifié en utilisant `kubectl get` sur l'espace de nom `velero`. Lorsque `Available` indique `1`, cela signifie qu'Ark est prêt :
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## Test de la sauvegarde et de la restauration
{: #velero-test}

### Sauvegarde
{: #velero-test-backup}

Vous pouvez maintenant créer une sauvegarde simple de votre cluster Kubernetes en exécutant la commande suivante :
```bash
velero backup create <backup-name>
```
{: pre}

Cette commande crée une sauvegarde pour chaque ressource du cluster, y compris les volumes persistants.

Vous pouvez également restreindre la sauvegarde à un espace de nom, un type de ressource ou un libellé en particulier. 

Velero ne permet pas la sélection par nom, mais uniquement par libellé.
{: tip}

Cette commande sauvegarde uniquement les composants libellés `app=<app-label>`.  
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

Pour obtenir une liste complète des options, exécutez la commande suivante :
```bash
velero backup --help
```
{: pre}

### Restauration
{: #velero-test-restore}

Pour restaurer une sauvegarde, exécutez la commande suivante :
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

Pour obtenir une liste complète des options, exécutez la commande suivante :
```bash
velero restore --help
```
{: pre}

