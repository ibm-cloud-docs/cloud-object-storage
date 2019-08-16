---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# Pour les développeurs
{: #gs-dev}
Tout d'abord, assurez-vous que l'[interface CLI de la plateforme {{site.data.keyword.cloud}}](https://cloud.ibm.com/docs/cli/index.html) et [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) sont installés.

## Mise à disposition d'une instance d'{{site.data.keyword.cos_full_notm}}
{: #gs-dev-provision}

  1. Tout d'abord, procurez-vous une clé d'API. Pour l'obtenir, accédez à [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys).
  2. Connectez-vous à la plateforme {{site.data.keyword.cloud_notm}} à l'aide de l'interface CLI. Il est également possible de stocker la clé d'API dans un fichier ou de la définir en tant que variable d'environnement.

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. Ensuite, mettez à disposition une instance d'{{site.data.keyword.cos_full_notm}} en spécifiant le nom de cette instance, l'ID et le plan souhaité (Lite ou Standard). Cela vous permettra d'obtenir le nom de ressource de cloud (CRN). Si vous disposez d'un compte mis à niveau, spécifiez le plan `Standard`. Sinon, spécifiez `Lite`.

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

Le [guide d'initiation](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) décrit les étapes de base relatives à la création de compartiments et d'objets, à l'invitation d'utilisateurs et à la création de règles. Vous trouverez une liste de commandes 'curl' de base en cliquant [ici](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl). 

Pour en savoir plus sur l'utilisation de l'interface CLI {{site.data.keyword.cloud_notm}} afin notamment de créer des applications et gérer des clusters Kubernetes, voir la [documentation](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli).


## Utilisation de l'API
{: #gs-dev-api}

Pour la gestion des données stockées dans {{site.data.keyword.cos_short}}, vous pouvez utiliser des outils compatibles avec l'API S3, tels que l'[interface CLI AWS](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli), avec des [données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) pour la compatibilité. Les jetons IAM étant plutôt faciles à utiliser, `curl` est idéal pour les tests de base et l'interaction avec votre stockage. Pour plus d'informations, voir [les informations de référence sur `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl), ainsi que la [documentation de référence d'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Utilisation des bibliothèques et des SDK
{: #gs-dev-sdk}
Des SDK IBM COS sont disponibles pour [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) et[Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node). Il s'agit des versions déviées des SDK AWS S3 qui ont été modifiés pour prendre en charge l'[authentification basée sur un jeton IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) et [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption). 

## Génération d'applications sur IBM Cloud
{: #gs-dev-apps}
{{site.data.keyword.cloud}} permet aux développeurs de choisir les options d'architecture et de déploiement appropriées pour une application donnée. Exécutez votre code sur un [serveur bare metal](https://cloud.ibm.com/catalog/infrastructure/bare-metal), dans des [machines virtuelles](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), à l'aide d'une [infrastructure sans serveur](https://cloud.ibm.com/openwhisk), dans des [conteneurs](https://cloud.ibm.com/kubernetes/catalog/cluster) ou à l'aide de [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs). 

[Cloud Native Computing Foundation](https://www.cncf.io) a incubé et récemment promu l'infrastructure d'orchestration de conteneur  [Kubernetes](https://kubernetes.io) et constitue la base d'{{site.data.keyword.cloud}} Kubernetes Service. Les développeurs qui souhaitent utiliser le stockage d'objets pour le stockage persistant dans leurs applications Kubernetes peuvent en apprendre davantage en cliquant sur les liens suivants :

 * [Choix d'une solution de stockage](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [Table de comparaison des options de stockage persistant](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [Page principale de la fonction COS](/docs/containers?topic=containers-object_storage)
 * [Installation de COS](/docs/containers?topic=containers-object_storage#install_cos)
 * [Création d'une instance de service COS](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [Création d'un secret COS](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [Choix de la configuration](/docs/containers?topic=containers-object_storage#configure_cos)
 * [Mise à disposition de COS](/docs/containers?topic=containers-object_storage#add_cos)
 * [Sauvegarde et restauration d'informations](/docs/containers?topic=containers-object_storage#backup_restore)
 * [Référence de classe de stockage](/docs/containers?topic=containers-object_storage#storageclass_reference)


