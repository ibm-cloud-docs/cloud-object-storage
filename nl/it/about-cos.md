---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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


# Informazioni su {{site.data.keyword.cos_full_notm}}
{: #about-ibm-cloud-object-storage}

Le informazioni archiviate con {{site.data.keyword.cos_full}} vengono crittografate e distribuite in più ubicazioni geografiche ed è possibile accedervi tramite HTTP utilizzando un'API REST. Questo servizio utilizza le tecnologie di archiviazione distribuita fornite dal sistema {{site.data.keyword.cos_full_notm}} (in precedenza Cleversafe).

{{site.data.keyword.cos_full_notm}} è disponibile con tre tipi di resilienza: interregionale, regionale e a singolo data center. Il servizio interregionale offre una maggiore durata e disponibilità rispetto all'utilizzo di una singola regione a scapito di una latenza leggermente più elevata ed è attualmente disponibile negli Stati Uniti, nell'Unione Europea e nell'Asia Pacifico. Il servizio regionale inverte tali compromessi e distribuisce gli oggetti in più zone di disponibilità all'interno di una singola regione ed è disponibile nelle regioni degli Stati Uniti, dell'Unione Europea e dell'Asia Pacifico. Se una determinata regione o zona di disponibilità non è disponibile, l'archivio oggetti continua a funzionare senza impedimenti. Il servizio a singolo data center distribuisce gli oggetti in più macchine all'interno della stessa ubicazione fisica. Controlla [qui](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) per le regioni disponibili.

Gli sviluppatori utilizzano un'API {{site.data.keyword.cos_full_notm}} per interagire con la loro archiviazione oggetti. Questa documentazione fornisce il supporto per [iniziare](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) a eseguire il provisioning degli account, per creare i bucket, per caricare gli oggetti e per utilizzare un riferimento delle interazioni API comuni. 

## Altri servizi di archiviazione oggetti IBM
{: #about-other-cos}
Oltre a {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} fornisce al momento ulteriori offerte di archiviazione oggetti per le diverse esigenze dell'utente, tutte accessibili tramite i portali basati su web e le API REST. [Ulteriori informazioni.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
