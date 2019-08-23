---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: big data, multipart, multiple parts, transfer

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
{:S3cmd: .ph data-hd-programlang='S3cmd'}

# Archivia oggetti di grandi dimensioni
{: #large-objects}

{{site.data.keyword.cos_full}} può supportare singoli oggetti con una dimensione di 10 TB quando si utilizzano i caricamenti in più parti. Gli oggetti di grandi dimensioni possono anche essere caricati [utilizzando la console con il trasferimento ad alta velocità Aspera abilitato](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera). Nella maggior parte degli scenari, il trasferimento ad alta velocità Aspera produce un significativo miglioramento delle prestazioni per il trasferimento dei dati, specialmente nelle lunghe distanze o in condizioni di rete instabile. 

## Caricamento degli oggetti in più parti
{: #large-objects-multipart}

Le operazioni di caricamento in più parti sono consigliate per scrivere oggetti di grandi dimensioni in {{site.data.keyword.cos_short}}. Un caricamento di un singolo oggetto viene eseguito come un insieme di parti e queste parti possono essere caricate indipendentemente, in qualsiasi ordine e in parallelo. Al completamento del caricamento, {{site.data.keyword.cos_short}} presenta poi tutte le parti come un singolo oggetto. Ciò offre molti vantaggi: le interruzioni di rete non causano la mancata riuscita di grandi caricamenti, i caricamenti possono essere sospesi e riavviati nel tempo e gli oggetti possono essere caricati mentre vengono creati. 

I caricamenti in più parti sono disponibili solo per gli oggetti più grandi di 5 MB. Per gli oggetti più piccoli di 50 GB, ti consigliamo una dimensione della parte che vada da 20 MB a 100 MB per prestazioni ottimali. Per gli oggetti più grandi, la dimensione della parte può essere incrementata senza un impatto significativo sulle prestazioni. I caricamenti in più parti sono limitati a un massimo di 10.000 parti di 5 GB ciascuna fino a una dimensione oggetto massima di 10 TB.


A causa della complessità nella gestione e nell'ottimizzazione dei caricamenti in parallelo, molti sviluppatori utilizzano librerie che forniscono il supporto di caricamento in più parti. 

La maggior parte degli strumenti, ad esempio le CLI o la console IBM Cloud, come pure la maggior parte degli SDK e delle librerie compatibili, trasferirà automaticamente gli oggetti in caricamenti in più parti. 

## Utilizzo dell'API REST o degli SDK
{: #large-objects-multipart-api} 

I caricamenti in più parti incompleti persistono fino a quando l'oggetto non viene eliminato oppure il caricamento in più parti non viene interrotto. Se un caricamento in più parti incompleto non viene interrotto, il caricamento parziale continua a utilizzare le risorse. Le interfacce devono essere progettate tenendo presente questo comportamento ed eliminare i caricamenti in più parti incompleti.
{:tip}

Ci sono tre fasi per caricare un oggetto in più parti: 

1. Il caricamento viene avviato e viene creato un `UploadId`. 
2. Vengono caricate singole parti specificando i loro numeri sequenziali e l'`UploadId` per l'oggetto. 
3. Una volta terminato il caricamento di tutte le parti, il caricamento viene completato inviando una richiesta con l'`UploadId` e un blocco XML che elenca ogni numero parte e il rispettivo valore `Etag`.

Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

### Avvia un caricamento in più parti
{: #large-objects-multipart-api-initiate} 
{: http}

Una richiesta `POST` emessa per un oggetto con il parametro di query `upload` crea un nuovo valore `UploadId` a cui fa poi riferimento ogni parte dell'oggetto che viene caricato.
{: http}

**Sintassi**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```
{: codeblock}
{: http}

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```
{: codeblock}
{: http}

----

### Carica una parte
{: #large-objects-multipart-api-upload-part} 
{: http}

Una richiesta `PUT` emessa per un oggetto con i parametri di query `partNumber` e `uploadId` caricherà una parte di un oggetto. Le parti possono essere caricate in modo seriale o in parallelo, ma devono essere numerate in ordine.
{: http}

**Sintassi**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```
{: codeblock}
{: http}

### Completa un caricamento in più parti
{: #large-objects-multipart-api-complete} 
{: http}

Una richiesta `POST` emessa per un oggetto con il parametro di query `uploadId` e il blocco XML appropriato nel corpo, completerà un caricamento in più parti.
{: http}

**Sintassi**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```
{: codeblock}
{: http}


### Interrompi i caricamenti in più parti incompleti
{: #large-objects-multipart-api-abort} 
{: http}

Una richiesta `DELETE` emessa per un oggetto con il parametro di query `uploadId` elimina tutte le parti incomplete di un caricamento in più parti.
{: http}
**Sintassi**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}
{: http}

### Utilizzo di S3cmd (CLI)
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){:new_window} è un client e uno strumento di riga di comando Linux e Mac gratuito per caricare, richiamare e gestire i dati nei provider del servizio di archiviazione cloud che utilizzano il protocollo S3. È progettato per utenti più esperti che hanno familiarità con i programmi della riga di comando ed è ideale per gli script batch e il backup automatizzato. S3cmd è scritto in Python. È un progetto open source disponibile in GNU Public License v2 (GPLv2) ed è gratuito sia per l'uso commerciale che per quello privato.
{: S3cmd}

S3cmd richiede Python 2.6 o successivo ed è compatibile con Python 3. Il modo più semplice per installare S3cmd è tramite PyPI (Python Package Index).
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

Una volta installato il pacchetto, seleziona il file di configurazione di esempio di {{site.data.keyword.cos_full}} [qui](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window} e aggiornalo con le tue credenziali di Cloud Object Storage (S3):
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

Le quattro righe che devono essere aggiornate sono
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
È la stessa cosa sia se utilizzi il file di esempio oppure quello generato eseguendo: `s3cmd --configure`.
{: S3cmd}

Una volta che queste righe sono state aggiornate con i dettagli COS dal portale del cliente, puoi verificare la connessione emettendo il comando `s3cmd ls`, che elencherà tutti i bucket nell'account.
{: S3cmd}

```
$ s3cmd ls 
2017-02-03 14:52  s3://backuptest
2017-02-06 15:04  s3://coldbackups
2017-02-03 21:23  s3://largebackup
2017-02-07 17:44  s3://winbackup
```
{: codeblock}
{: S3cmd}

L'elenco completo delle opzioni e dei comandi insieme alle informazioni di base è disponibile sul sito [s3tools](https://s3tools.org/usage){:new_window}.
{: S3cmd}

### Caricamenti in più parti con S3cmd
{: #large-objects-s3cmd-upload} 
{: S3cmd}

Un comando `put` eseguirà automaticamente un caricamento in più parti quando tenti di caricare un file più grande della soglia specificata.
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

La soglia viene determinata dall'opzione `--multipart-chunk-size-mb`:
{: S3cmd}

```
--multipart-chunk-size-mb=SIZE
    Size of each chunk of a multipart upload. Files bigger
    than SIZE are automatically uploaded as multithreaded-
    multipart, smaller files are uploaded using the
    traditional method. SIZE is in megabytes, default
    chunk size is 15MB, minimum allowed chunk size is 5MB,
    maximum is 5GB.
```
{: codeblock}
{: S3cmd}

Esempio:
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

Output:
{: S3cmd}

```
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 1 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  1731.92 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 2 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2001.14 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 3 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2000.28 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 4 of 4, 4MB] [1 of 1]
 4973645 of 4973645   100% in    2s  1823.51 kB/s  done
 ```
{: codeblock}
{: S3cmd}

### Utilizzo dell'SDK Java
{: #large-objects-java} 
{: java}

L'SDK Java fornisce due modi per eseguire caricamenti di oggetti di grandi dimensioni:
{: java}

* [Caricamenti in più parti](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Utilizzo dell'SDK Python
{: #large-objects-python} 
{: python}

L'SDK Python fornisce due modi per eseguire caricamenti di oggetti di grandi dimensioni:
{: python}

* [Caricamenti in più parti](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Utilizzo dell'SDK Node.js
{: #large-objects-node} 
{: javascript}

L'SDK Node.js fornisce un solo modo per eseguire caricamenti di oggetti di grandi dimensioni:
{: javascript}

* [Caricamenti in più parti](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
