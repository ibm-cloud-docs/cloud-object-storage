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

# Integrazione di Velero
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window} (in precedenza Heptio Ark) è un set di strumenti per il backup e il ripristino dei cluster Kubernetes utilizzando l'API S3.

Velero è costituito da due parti:

* Un componente server che viene eseguito sul cluster
* Un'interfaccia riga di comando che viene eseguita su un client locale

## Prerequisiti
{: #velero-prereqs}

Prima di iniziare, hai bisogno della seguente impostazione:

* La [`CLI IBM Cloud`](/docs/cli?topic=cloud-cli-getting-started){:new_window} installata
* Lo strumento di riga di comando [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} installato e configurato per stabilire una connessione al tuo cluster
* Un'istanza {{site.data.keyword.cos_short}} 
* Un bucket {{site.data.keyword.cos_short}}
* Credenziali HMAC con accesso di Writer (Scrittore) al bucket

## Installa il client Velero
{: #velero-install}

1. Scaricare la [versione](https://github.com/heptio/velero/releases){:new_window} più recente di Velero per il tuo sistema operativo
2. Estrai il file .tar in una cartella sul tuo sistema locale
3. Verifica che puoi eseguire il file binario `velero`:

```
velero --help
```

```
Velero is a tool for managing disaster recovery, specifically for Kubernetes
cluster resources. It provides a simple, configurable, and operationally robust
way to back up your application state and associated data.

If you're familiar with kubectl, Velero supports a similar model, allowing you to
execute commands such as 'velero get backup' and 'velero create schedule'. The same
operations can also be performed as 'velero backup get' and 'velero schedule create'.

Usage:
  velero [command]

Available Commands:
  backup            Work with backups
  backup-location   Work with backup storage locations
  bug               Report an velero bug
  client            Velero client related commands
  completion        Output shell completion code for the specified shell (bash or zsh)
  create            Create velero resources
  delete            Delete velero resources
  describe          Describe velero resources
  get               Get velero resources
  help              Help about any command
  plugin            Work with plugins
  restic            Work with restic
  restore           Work with restores
  schedule          Work with schedules
  server            Run the velero server
  snapshot-location Work with snapshot locations
  version           Print the velero version and associated image
...
```

*(FACOLTATIVO)* Sposta il file binario ark dalla cartella temporanea a un'ubicazione più permanente come `/usr/local/bin` su Mac OS o Linux.
{: tip}

## Installa e configura il server Velero
{: #velero-config}
### Crea il file di credenziali
{: #velero-config-credentials}

Crea un file di credenziali (`credentials-velero`) con le chiavi HMAC nella tua cartella Velero locale (*cartella in cui era stato estratto il file .tar)*)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Configura kubectl
{: #velero-config-kubectl}

Configura [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} per stabilire una connessione al tuo cluster.

1. Accedi alla piattaforma IBM Cloud utilizzando la CLI.<br/><br/>*Per una maggiore sicurezza, è anche possibile archiviare la chiave API in un file o impostarla come una variabile di ambiente*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. Richiama la configurazione cluster
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. Copia e incolla il comando **export** per impostare la variabile di ambiente KUBECONFIG

4. Assicurati che `kubectl` sia configurato correttamente eseguendo:
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

### Configura il server Velero e l'archivio cloud
{: #velero-config-storage}

1. Nella cartella Velero, esegui quanto segue per impostare spazi dei nomi, HBAC e altra struttura<br/><br/>*Lo spazio dei nomi predefinito è `velero`. Se vuoi creare uno spazio dei nomi personalizzato, vedi le istruzioni in ['Run in custom namespace'](https://heptio.github.io/velero/master/namespace.html){:new_window}*
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
2. Crea un segreto con il tuo file delle credenziali
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. Specifica i seguenti valori in `config/ibm/05-ark-backupstoragelocation.yaml`:
   * `<YOUR_BUCKET>` - nome del bucket per l'archiviazione dei file di backup
   * `<YOUR_REGION>` - il [vincolo di ubicazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) del tuo bucket (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - l'URL dell'endpoint regionale (`https://s3.us.cloud-object-storage.appdomain.cloud`). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

    *Per ulteriori informazioni, vedi la definizione di [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window}.*

### Avvia il server Velero
{: #velero-config-server}

1. Nella cartella Velero, esegui questo comando per creare l'oggetto nel tuo cluster:
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. Esegui questo comando per creare la distribuzione:
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. Assicurati che la distribuzione sia pianificata correttamente utilizzando `kubectl get` sullo spazio dei nomi `velero` . Quando `Available` indica `1`, Ark è pronto per l'uso:
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## Esecuzione del test di backup e ripristino
{: #velero-test}

### Backup
{: #velero-test-backup}

Puoi ora creare un semplice backup del tuo cluster Kubernetes eseguendo questo comando:
```bash
velero backup create <backup-name>
```
{: pre}

Questo comando crea un backup per ogni risorsa nel cluster, compresi i volumi persistenti.

Puoi anche limitare il backup a uno spazio dei nomi, un tipo di risorsa o un'etichetta specifici.

Velero non consente la selezione in base al nome, solo in base alle etichette.
{: tip}

Questo comando esegue il backup solo dei componenti etichettati con `app=<app-label>`. 
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

Un elenco completo delle opzioni è disponibile eseguendo:
```bash
velero backup --help
```
{: pre}

### Ripristino
{: #velero-test-restore}

Per ripristinare un backup, esegui questo comando:
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

Un elenco completo delle opzioni è disponibile eseguendo:
```bash
velero restore --help
```
{: pre}

