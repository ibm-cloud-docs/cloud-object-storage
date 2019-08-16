---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, objects

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

# Operazioni per l'oggetto
{: #object-operations}

Queste operazioni leggono, scrivono e configurano gli oggetti contenuti all'interno di un bucket.

Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Carica un oggetto
{: #object-operations-put}

Una richiesta `PUT`, fornito un percorso a un oggetto, carica il corpo della richiesta come un oggetto. Tutti gli oggetti caricati in un singolo thread devono essere più piccoli di 500MB (gli oggetti [caricati in più parti](/docs/services/cloud-object-storage?topic=cloud-object-storage-large-objects) posso arrivare a una dimensione di 10TB).

**Nota**: Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
{:tip}

**Sintassi**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Richiesta di esempio**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Richiesta di esempio (HMAC)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: {payload_hash}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
PUT /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

----

## Ottieni le intestazioni di un oggetto
{: #object-operations-head}

Una richiesta `HEAD`, fornito un percorso a un oggetto, richiama le intestazioni di tale oggetto. 

Tieni presente che il valore `Etag` restituito per gli oggetti crittografati utilizzando SSE-KP **non** sarà l'hash MD5 dell'oggetto non crittografato originale.
{:tip}

**Sintassi**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Richiesta di esempio**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:32:44 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: da214d69-1999-4461-a130-81ba33c484a6
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:49:06 GMT
Content-Length: 11
```

----

## Scarica un oggetto
{: #object-operations-get}

Una richiesta `GET`, fornito un percorso a un oggetto, scarica l'oggetto. 

Tieni presente che il valore `Etag` restituito per gli oggetti crittografati utilizzando SSE-C/SSE-KP **non** sarà l'hash MD5 dell'oggetto non crittografato originale.
{:tip}

**Sintassi**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### Intestazioni facoltative
{: #object-operations-get-headers}

Intestazione              | Tipo   |  Descrizione
--- | ---- | ------------
`range` | stringa | Restituisce i byte di un oggetto all'interno dell'intervallo specificato. 

**Richiesta di esempio**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:34:25 GMT
X-Clv-Request-Id: 116dcd6b-215d-4a81-bd30-30291fa38f93
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 116dcd6b-215d-4a81-bd30-30291fa38f93
ETag: "d34d8aada2996fc42e6948b926513907"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:46:53 GMT
Content-Length: 467

 Female bees that are not fortunate enough to be selected to be the 'queen'
 while they were still larvae become known as 'worker' bees. These bees lack
 the ability to reproduce and instead ensure that the hive functions smoothly,
 acting almost as a single organism in fulfilling their purpose.
```

----

## Elimina un oggetto
{: #object-operations-delete}

Una richiesta `DELETE`, fornito un percorso a un oggetto, elimina un oggetto. 

**Sintassi**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Richiesta di esempio**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 204 No Content
Date: Thu, 25 Aug 2016 17:44:57 GMT
X-Clv-Request-Id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
```

----

## Eliminazione di più oggetti
{: #object-operations-multidelete}

Una richiesta `POST`, forniti un percorso a un bucket e parametri appropriati, eliminerà un insieme specificato di oggetti. È richiesta un'intestazione `Content-MD5` che specifica l'hash MD5 con codifica base64 del corpo della richiesta. 

L'intestazione `Content-MD5` richiesta deve essere la rappresentazione binaria di un hash MD5 con codifica base64. 

**Nota:** se un oggetto specificato nella richiesta non viene trovato, il risultato viene restituito come eliminato.  

### Elementi facoltativi
{: #object-operations-multidelete-options}

|Intestazione              | Tipo   |  Descrizione|
|---|---|---|
|`Quiet`|Booleano|Abilita la modalità quiet per la richiesta.|

La richiesta può contenere massimo 1000 chiavi che vuoi eliminare. Sebbene ciò si riveli utile nel ridurre il sovraccarico per richiesta, presta attenzione quando elimini un ampio numero di chiavi. Tieni inoltre presente le dimensioni degli oggetti per assicurarti prestazioni adeguate.
{:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Sintassi**

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```

**Richiesta di esempio**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
POST /apiary?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&delete=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Delete>
    <Object>
         <Key>surplus-bee</Key>
    </Object>
    <Object>
         <Key>unnecessary-bee</Key>
    </Object>
</Delete>
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 18:54:53 GMT
X-Clv-Request-Id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Content-Type: application/xml
Content-Length: 207
```
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<DeleteResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Deleted>
         <Key>surplus-bee</Key>
    </Deleted>
    <Deleted>
         <Key>unnecessary-bee</Key>
    </Deleted>
</DeleteResult>
```

----

## Copia un oggetto
{: #object-operations-copy}

Una richiesta `PUT`, fornito un percorso a un nuovo oggetto, crea una nuova copia di un altro oggetto specificato dall'intestazione `x-amz-copy-source`. A meno che non vengano modificati, i metadati rimangono gli stessi. 

**Nota**: Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
{:tip}


**Nota**: la copia di un elemento da un bucket abilitato a *Key Protect* a un bucket di destinazione in un'altra regione è limitata e genererà un errore `500 - Internal Error`.
{:tip}

**Sintassi**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### Intestazioni facoltative
{: #object-operations-copy-options}

Intestazione              | Tipo   |  Descrizione
--- | ---- | ------------
`x-amz-metadata-directive` | stringa (`COPY` o `REPLACE`) | `REPLACE` sovrascriverà i metadati originali con i nuovi metadati forniti. 
`x-amz-copy-source-if-match` | stringa (`ETag`)| Crea una copia se l'`ETag` specificato corrisponde all'oggetto di origine. 
`x-amz-copy-source-if-none-match` | stringa (`ETag`)| Crea una copia se l'`ETag` specificato è diverso dall'oggetto di origine. 
`x-amz-copy-source-if-unmodified-since` | stringa (timestamp)| Crea una copia se l'oggetto di origine non è stato modificato dopo la data specificata. La data deve essere una data HTTP valida (ad esempio, `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-copy-source-if-modified-since` | stringa (timestamp)| Crea una copia se l'oggetto di origine è stato modificato dopo la data specificata. La data deve essere una data HTTP valida (ad esempio, `Wed, 30 Nov 2016 20:21:38 GMT`).

**Richiesta di esempio**

Questo esempio di base prende l'oggetto `bee` dal bucket `garden` e crea una copia nel bucket `apiary` con la nuova chiave `wild-bee`.

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 19:52:52 GMT
X-Clv-Request-Id: 72992a90-8f86-433f-b1a4-7b1b33714bed
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 72992a90-8f86-433f-b1a4-7b1b33714bed
ETag: "853aab195ce770b0dfb294a4e9467e62"
Content-Type: application/xml
Content-Length: 240
```

```xml
<CopyObjectResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <LastModified>2016-11-30T19:52:53.125Z</LastModified>
  <ETag>"853aab195ce770b0dfb294a4e9467e62"</ETag>
</CopyObjectResult>
```

----

## Controlla la configurazione CORS di un oggetto
{: #object-operations-options}

Una richiesta `OPTIONS`, fornito un percorso a un oggetto insieme a un'origine e a un tipo di richiesta, esegue un controllo per vedere se tale oggetto è accessibile da tale origine utilizzando tale tipo di richiesta. Diversamente da tutte le altre richieste, una richiesta OPTIONS non richiede le intestazioni `authorization` o `x-amx-date`.

**Sintassi**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Richiesta di esempio**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 07 Dec 2016 16:23:14 GMT
X-Clv-Request-Id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: PUT
Access-Control-Allow-Credentials: true
Vary: Origin, Access-Control-Request-Headers, Access-Control-Allow-Methods
Content-Length: 0

```

----

## Caricamento degli oggetti in più parti
{: #object-operations-multipart}

Quando utilizzi oggetti più grandi, ti consigliamo di utilizzare le operazioni di caricamento in più parti per scrivere gli oggetti in {{site.data.keyword.cos_full}}. Un caricamento di un singolo oggetto può essere eseguito come un insieme di parti e queste parti possono essere caricate indipendentemente, in qualsiasi ordine e in parallelo. Al completamento del caricamento, {{site.data.keyword.cos_short}} presenta poi tutte le parti come un singolo oggetto. Ciò offre molti vantaggi: le interruzioni di rete non causano la mancata riuscita di grandi caricamenti, i caricamenti possono essere sospesi e riavviati nel tempo e gli oggetti possono essere caricati mentre vengono creati. 

I caricamenti in più parti sono disponibili solo per gli oggetti più grandi di 5MB. Per gli oggetti più piccoli di 50GB, ti consigliamo una dimensione della parte che vada da 20MB a 100MB per prestazioni ottimali. Per gli oggetti più grandi, la dimensione della parte può essere incrementata senza un impatto significativo sulle prestazioni. I caricamenti in più parti sono limitati a un massimo di 10.000 parti di 5GB ciascuna.

L'utilizzo di più di 500 parti porta a inefficienze in {{site.data.keyword.cos_short}} e deve essere evitato quando possibile.
{:tip}

A causa della complessità aggiuntiva coinvolta, ti consigliamo di far utilizzare agli sviluppatori una libreria che fornisca il supporto per il caricamento in più parti. 

I caricamenti in più parti incompleti persistono fino a quando l'oggetto non viene eliminato oppure il caricamento in più parti non viene interrotto con `AbortIncompleteMultipartUpload`. Se un caricamento in più parti incompleto non viene interrotto, il caricamento parziale continua a utilizzare le risorse. Le interfacce devono essere progettate tenendo presente questo comportamento ed eliminare i caricamenti in più parti incompleti.
{:tip}

Ci sono tre fasi per caricare un oggetto in più parti: 

1. Il caricamento viene avviato e viene creato un `UploadId`. 
2. Vengono caricate singole parti specificando i loro numeri sequenziali e l'`UploadId` per l'oggetto. 
3. Una volta terminato il caricamento di tutte le parti, il caricamento viene completato inviando una richiesta con l'`UploadId` e un blocco XML che elenca ogni numero parte e il rispettivo valore `Etag`.

## Avvia un caricamento in più parti
{: #object-operations-multipart-initiate}

Una richiesta `POST` emessa per un oggetto con il parametro di query `upload` crea un nuovo valore `UploadId` a cui fa poi riferimento ogni parte dell'oggetto che viene caricato. 

**Nota**: Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
{:tip}

**Sintassi**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**Richiesta di esempio**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

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

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```

----

## Carica una parte
{: #object-operations-multipart-put-part}

Una richiesta `PUT` emessa per un oggetto con i parametri di query `partNumber` e `uploadId` caricherà una parte di un oggetto. Le parti possono essere caricate in modo seriale o in parallelo, ma devono essere numerate in ordine. 

**Nota**: Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
{:tip}

**Sintassi**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**Richiesta di esempio**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Risposta di esempio**

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

----

## Elenca le parti
{: #object-operations-multipart-list}

Una richiesta `GET`, fornito un percorso a un oggetto in più parti con un `UploadID` attivo specificato come parametro di query, restituirà un elenco di tutte le parti dell'oggetto. 


**Sintassi**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```

### Parametri di query
{: #object-operations-multipart-list-params}
Parametro | Obbligatorio? | Tipo   |  Descrizione
--- | ---- | ------------
`uploadId` | Obbligatorio | stringa | ID di caricamento restituito quando viene inizializzato un caricamento in più parti. 
`max-parts` | Facoltativo | stringa | Il valore predefinito è 1.000.
`part-number​-marker` | Facoltativo | stringa | Definisce da dove inizierà l'elenco delle parti. 

**Richiesta di esempio**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Mon, 19 Mar 2018 17:21:08 GMT
X-Clv-Request-Id: 6544044d-4f88-4bb6-9ee5-bfadf5023249
Server: Cleversafe/3.12.4.20
X-Clv-S3-Version: 2.5
Accept-Ranges: bytes
Content-Type: application/xml
Content-Length: 743
```

```xml
<ListPartsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>farm</Bucket>
  <Key>spaceship</Key>
  <UploadId>01000162-3f46-6ab8-4b5f-f7060b310f37</UploadId>
  <Initiator>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Initiator>
  <Owner>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Owner>
  <StorageClass>STANDARD</StorageClass>
  <MaxParts>1000</MaxParts>
  <IsTruncated>false</IsTruncated>
  <Part>
    <PartNumber>1</PartNumber>
    <LastModified>2018-03-19T17:20:35.482Z</LastModified>
    <ETag>"bb03cf4fa8603fe407a65ee1dba55265"</ETag>
    <Size>7128094</Size>
  </Part>
</ListPartsResult>
```

----

## Completa un caricamento in più parti
{: #object-operations-multipart-complete}

Una richiesta `POST` emessa per un oggetto con il parametro di query `uploadId` e il blocco XML appropriato nel corpo, completerà un caricamento in più parti.

**Sintassi**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Richiesta di esempio**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

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

**Risposta di esempio**

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

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

## Interrompi i caricamenti in più parti incompleti
{: #object-operations-multipart-uploads}

Una richiesta `DELETE` emessa per un oggetto con il parametro di query `uploadId` eliminerà tutte le parti incomplete di un caricamento in più parti. 

**Sintassi**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**Richiesta di esempio**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Ripristina temporaneamente un oggetto archiviato
{: #object-operations-archive-restore}

Una richiesta `POST` emessa per un oggetto con il parametro di query `restore` per richiedere il ripristino temporaneo di un oggetto archiviato. Viene richiesta un'intestazione `Content-MD5` come controllo dell'integrità per il payload.

Un oggetto archiviato deve essere ripristinato prima di scaricare o di modificare l'oggetto. Devi specificare il ciclo di vita dell'oggetto trascorso il quale la copia temporanea dell'oggetto verrà eliminata. 

Può verificarsi un ritardo fino a 15 ore prima che la copia di ripristino sia disponibile per l'accesso. Una richiesta HEAD può controllare se la copia ripristinata è disponibile. 

Per ripristinare in modo permanente l'oggetto, deve essere copiato in un bucket che non ha una configurazione attiva del ciclo di vita. 

**Sintassi**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```

**Elementi del payload**

Il corpo della richiesta deve contenere un blocco XML con il seguente schema:

|Elemento| Tipo   |Elemento secondario|Predecessore|Vincolo|
|---|---|---|---|---|
|RestoreRequest|Contenitore|Days, GlacierJobParameters| Nessuno | Nessuno |
|Days|Numero intero| Nessuno |RestoreRequest|Specificato il ciclo di vita dell'oggetto ripristinato temporaneamente. Il numero minimo di giorni per cui può esistere una copia ripristinata dell'oggetto è 1. Trascorso il periodo di ripristino, la copia temporanea dell'oggetto verrà rimossa.|
|GlacierJobParameters| Stringa |Tier|RestoreRequest| Nessuno |
|Tier| Stringa | Nessuno |GlacierJobParameters|**Deve** essere impostato su `Bulk`.|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Richiesta di esempio**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (Intestazioni HMAC)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Richiesta di esempio (URL pre-firmato HMAC)**

```http
POST /apiary/queenbee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&restore&x-amz-signature={signature} HTTP/1.1
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Risposta di esempio**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Aggiornamento dei metadati
{: #object-operations-metadata}

Esistono due modi per aggiornare i metadati in un oggetto esistente:
* Una richiesta `PUT` con i nuovi metadati e il contenuto dell'oggetto originale
* Eseguendo una richiesta `COPY` con i nuovi metadati specificando l'oggetto originale come origine della copia

Tutti i metadati devono avere `x-amz-meta-` come prefisso
{: tip}

### Utilizzo di PUT per aggiornare i metadati
{: #object-operations-metadata-put}

La richiesta `PUT` richiede una copia dell'oggetto esistente poiché il contenuto verrà sovrascritto. {: important}

**Sintassi**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Richiesta di esempio**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

### Utilizzo di COPY per aggiornare i metadati
{: #object-operations-metadata-copy}

Per ulteriori dettagli sull'esecuzione di una richiesta `COPY`, fai clic [qui](#object-operations-copy)

**Sintassi**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Richiesta di esempio**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```
