---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# Indicazioni per gli sviluppatori
{: #dev-guide}

## Ottimizzazione delle impostazioni di cifratura
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} supporta una varietà di impostazioni di cifratura per crittografare i dati in transito. Non tutte le impostazioni di cifratura producono lo stesso livello di prestazioni. La negoziazione di uno di `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_128_CBC_SHA` ha dimostrato di produrre gli stessi livelli di prestazioni di nessun TLS tra il client e il sistema {{site.data.keyword.cos_full_notm}}.

## Utilizzo di caricamenti in più parti
{: #dev-guide-multipart}

Quando gestisci oggetti più grandi, consigliamo le operazioni di caricamento in più parti per scrivere gli oggetti in {{site.data.keyword.cos_full_notm}}. Un caricamento di un singolo oggetto può essere eseguito come una serie di parti e tali parti possono essere caricate indipendentemente in qualsiasi ordine e in parallelo. Una volta completato il caricamento, {{site.data.keyword.cos_short}} presenta quindi tutte le parti come un singolo oggetto. Questo offre molti vantaggi: le interruzioni di rete non causano la mancata riuscita di grandi caricamenti, i caricamenti possono essere messi in pausa e riavviati nel corso del tempo e gli oggetti possono essere caricati man mano che vengono creati.

I caricamenti in più parti sono disponibili solo per gli oggetti di dimensioni superiori a 5MB. Per gli oggetti più piccoli di 50GB, per prestazioni ottimali è consigliata una dimensione della parte compresa tra 20MB e 100MB. Per gli oggetti più grandi, la dimensione della parte può essere aumentata senza un significativo impatto sulle prestazioni.

L'utilizzo di più di 500 parti porta a inefficienze in {{site.data.keyword.cos_short}} e dovrebbe essere evitato, quando possibile.

A causa della complessità aggiuntiva coinvolta, consigliamo agli sviluppatori di utilizzare le librerie API S3, che forniscono il supporto di caricamento in più parti.

I caricamenti in più parti incompleti persistono finché l'oggetto non viene eliminato o finché il caricamento in più parti non viene interrotto con `AbortIncompleteMultipartUpload`. Se un caricamento in più parti incompleto non viene interrotto, il caricamento parziale continua a utilizzare risorse. Le interfacce dovrebbero essere progettate tenendo a mente questo punto e una ripulitura dei caricamenti in più parti incompleti.

## Utilizzo dei kit di sviluppo software
{: #dev-guide-sdks}

Non è obbligatorio utilizzare gli SDK API S3 pubblicati; il software personalizzato può avvalersi dell'API per integrarsi direttamente con {{site.data.keyword.cos_short}}. Tuttavia, l'utilizzo delle librerie API S3 pubblicate fornisce dei vantaggi quali l'autenticazione/la generazione di firme, una logica di nuovi tentativi automatici in caso di errori `5xx` e una generazione di url pre-firmato. Presta attenzione quando scrivi software che utilizza la API direttamente per gestire gli errori provvisori, come ad esempio fornendo dei nuovi tentativi con un backoff esponenziale quando si ricevono degli errori `503`.

## Paginazione
{: #dev-guide-pagination}

Quando si gestisce un gran numero di oggetti in un bucket, le applicazioni web possono cominciare a soffrire di una riduzione delle prestazioni. Molte applicazioni utilizzano una tecnica denominata **paginazione** (*il processo di dividere un set di record di grandi dimensioni in pagine discrete*). Quasi tutte le piattaforme di sviluppo forniscono oggetti o metodi per effettuare la paginazione mediante una funzionalità integrata o tramite librerie di terze parti.

Gli SDK {{site.data.keyword.cos_short}} forniscono il supporto per la paginazione tramite un metodo che elenca gli oggetti all'interno di uno specifico bucket. Questo metodo fornisce diversi parametri che lo rendono estremamente utile quando si prova a suddividere un set di risultati di grandi dimensioni.

### Utilizzo di base
{: #dev-guide-pagination-basics}
Il concetto di base dietro il metodo di elenco degli oggetti implica l'impostazione del numero massimo di chiavi (`MaxKeys`) da restituire nella risposta. La risposta include anche un valore `boolean` (`IsTruncated`) che indica se sono disponibili ulteriori risultati e un valore `string` denominato `NextContinuationToken`. L'impostazione del token di continuazione nelle richieste di follow-up restituisce il batch di oggetti successivo finché non ci sono più risultati disponibili.

#### Parametri comuni
{: #dev-guide-pagination-params}

|Parametro|Descrizione|
|---|---|
|`ContinuationToken`|Imposta il token per specificare il successivo batch di record|
|`MaxKeys`|Imposta il numero massimo di chiavi da includere nella risposta|
|`Prefix`|Limita la risposta alle chiavi che iniziano con il prefisso specificato|
|`StartAfter`|Imposta da dove iniziare l'elenco di oggetti in base alla chiave|

### Utilizzo di Java
{: #dev-guide-pagination-java}

L'SDK {{site.data.keyword.cos_full}} per Java fornisce il metodo [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} che consente di restituire l'elenco oggetti nella dimensione desiderata. Un esempio di codice completo è disponibile [qui](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2).

### Utilizzo di Python
{: #dev-guide-pagination-python}

L'SDK {{site.data.keyword.cos_full}} per Python fornisce il metodo [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} che consente di restituire l'elenco oggetti nella dimensione desiderata. Un esempio di codice completo è disponibile [qui](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2).
