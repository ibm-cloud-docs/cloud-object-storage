---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# Carica i dati
{: #upload}

Dopo aver organizzato i tuoi bucket, è ora di aggiungere alcuni oggetti. A seconda di come vuoi utilizzare la tua archiviazione, ci sono diversi modi per raggiungere i dati nel sistema. Un data scientist dispone di pochi file di grandi dimensioni che vengono utilizzati per l'analisi, un amministratore di sistema deve tenere sincronizzati i backup del database con i file locali e uno sviluppatore sta scrivendo il software che deve leggere e scrivere milioni di file. Ognuno di questi scenari viene servito al meglio da diversi metodi di inserimento dei dati. 

## Utilizzo della console
{: #upload-console}

Di norma, l'utilizzo della console basata su web non è il modo più comune per utilizzare {{site.data.keyword.cos_short}}. Gli oggetti sono limitati a 200 MB e il nome file e la chiave sono identici. Più oggetti possono essere caricati contemporaneamente e se il browser consente più thread, ogni oggetto verrà caricato utilizzando più parti in parallelo. Il supporto per le dimensioni degli oggetti più grandi e per le prestazioni migliorate (a seconda dei fattori di rete) viene fornito dal [trasferimento ad alta velocità Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera).

## Utilizzo di uno strumento compatibile
{: #upload-tool}

Alcuni utenti vogliono utilizzare un programma di utilità autonomo per interagire con la loro archiviazione. Poiché l'API Cloud Object Storage supporta l'insieme più comune di operazioni API S3, molti strumenti compatibili con S3 possono anche connettersi a {{site.data.keyword.cos_short}} utilizzando le [credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).

Alcuni esempi includono alcuni file explorer come [Cyberduck](https://cyberduck.io/) o [Transmit](https://panic.com/transmit/), programmi di utilità di backup come [Cloudberry](https://www.cloudberrylab.com/) e [Duplicati](https://www.duplicati.com/), programmi di utilità di riga di comando come [s3cmd](https://github.com/s3tools/s3cmd) o [Minio Client](https://github.com/minio/mc) e molti altri.

## Utilizzo dell'API
{: #upload-api}

La maggior parte delle applicazioni programmatiche di Object Storage utilizzano un SDK (ad esempio [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) o [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)) o l'[API Cloud Object Storage](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api). Di norma, gli oggetti vengono caricati in [più parti](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects), con dimensione parte e numero di parti configurati da una classe Transfer Manager.
