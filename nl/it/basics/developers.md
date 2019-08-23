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

# Per gli sviluppatori
{: #gs-dev}
Innanzitutto, assicurati di avere la [CLI della piattaforma {{site.data.keyword.cloud}}](https://cloud.ibm.com/docs/cli/index.html) e [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) installati. 

## Esegui il provisioning di un'istanza di {{site.data.keyword.cos_full_notm}}
{: #gs-dev-provision}

  1. Innanzitutto, assicurati di avere una chiave API. Ottienila da [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys).
  2. Accedi alla piattaforma {{site.data.keyword.cloud_notm}} utilizzando la CLI. Puoi anche archiviare la chiave API in un file o configurarla come una variabile di ambiente. 

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. Poi, esegui il provisioning di un'istanza di {{site.data.keyword.cos_full_notm}} specificando il nome per l'istanza, l'ID e il piano desiderato (lite o standard). Ciò ci procurerà il CRN. Se hai un account di cui è stato eseguito l'upgrade, specifica il piano `Standard`. Altrimenti, specifica `Lite`.

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

La [guida introduttiva](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) illustra i passi di base per creare i bucket e gli oggetti, per invitare gli utenti e per creare le politiche. Puoi trovare [qui](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) un elenco dei comandi 'curl' di base.

Ulteriori informazioni sull'utilizzo della CLI di {{site.data.keyword.cloud_notm}} per creare le applicazioni, gestire i cluster Kubernetes e altro sono disponibili [nella documentazione](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli).


## Utilizzo dell'API
{: #gs-dev-api}

Per gestire i dati archiviati in {{site.data.keyword.cos_short}}, puoi utilizzare strumenti compatibili con l'API S3 come la [CLI AWS](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)con le [credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) per compatibilità. Poiché i token IAM sono relativamente semplici da utilizzare, `curl` è una buona scelta per la verifica di base e l'interazione con la tua archiviazione. Puoi trovare ulteriori informazioni nel [riferimento `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl), come pure nella [documentazione di riferimento API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Utilizzo delle librerie e degli SDK
{: #gs-dev-sdk}
Ci sono SDK di IBM COS disponibili per [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) e [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node). Queste versioni duplicate degli SDK di AWS S3 sono state modificate per supportare l'[autenticazione basata sul token IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview), come pure per supportare [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption). 

## Creazione delle applicazioni su IBM Cloud
{: #gs-dev-apps}
{{site.data.keyword.cloud}} fornisce agli sviluppatori flessibilità nella scelta delle opzioni di architettura e di sviluppo corrette per una determinata applicazione. Esegui il tuo codice nel [bare metal](https://cloud.ibm.com/catalog/infrastructure/bare-metal), nelle [macchine virtuali](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), utilizzando un [framework senza server](https://cloud.ibm.com/openwhisk), in [contenitori](https://cloud.ibm.com/kubernetes/catalog/cluster) oppure utilizzando [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs). 

[Cloud Native Computing Foundation](https://www.cncf.io) ha incubato e recentemente "promosso" il framework di orchestrazione dei contenitori [Kubernetes](https://kubernetes.io) ed esso costituisce la base per {{site.data.keyword.cloud}} Kubernetes Service. Gli sviluppatori che desiderano utilizzare l'archiviazione oggetti per l'archiviazione persistente nelle loro applicazioni Kubernetes possono acquisire ulteriori informazioni ai seguenti link: 

 * [Scelta di una soluzione di archiviazione](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [Tabella di confronto per le opzioni di archiviazione persistente](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [Pagina COS principale](/docs/containers?topic=containers-object_storage)
 * [Installazione di COS](/docs/containers?topic=containers-object_storage#install_cos)
 * [Creazione di un'istanza del servizio COS](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [Creazione di un segreto COS](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [Decidi in merito alla configurazione](/docs/containers?topic=containers-object_storage#configure_cos)
 * [Provisioning di COS](/docs/containers?topic=containers-object_storage#add_cos)
 * [Informazioni di backup e ripristino](/docs/containers?topic=containers-object_storage#backup_restore)
 * [Riferimento delle classi di archiviazione](/docs/containers?topic=containers-object_storage#storageclass_reference)


