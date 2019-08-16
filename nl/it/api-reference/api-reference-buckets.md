---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, buckets

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

# Operazioni per il bucket
{: #compatibility-api-bucket-operations}


## Elenca i bucket
{: #compatibility-api-list-buckets}

Una richiesta `GET` inviata alla root dell'endpoint restituisce un elenco dei bucket associati all'istanza del servizio specificata. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). 

Intestazione              | Tipo   | Obbligatorio? |  Descrizione
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | Stringa | Sì | Elenca i bucket creati in questa istanza del servizio.

Parametro di query                  | Valore  | Obbligatorio? |  Descrizione
--------------------------|--------|---| -----------------------------------------------------------
`extended` | Nessuno | No | Fornisce i metadati `LocationConstraint` nell'elenco.

L'elenco esteso non è supportato negli SDK o nella CLI.
{:note}

**Sintassi**

```bash
GET https://{endpoint}/
```

**Richiesta di esempio**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Risposta di esempio**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

### Come ottenere un elenco esteso
{: #compatibility-api-list-buckets-extended}

**Sintassi**

```bash
GET https://{endpoint}/?extended
```

**Richiesta di esempio**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Risposta di esempio**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <IsTruncated>false</IsTruncated>
    <MaxKeys>1000</MaxKeys>
    <Prefix/>
    <Marker/>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
            <LocationConstraint>us-south-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
            <LocationConstraint>seo01-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
            <LocationConstraint>eu-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
            <LocationConstraint>us-cold</LocationConstraint>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

## Crea un bucket
{: #compatibility-api-new-bucket}

Una richiesta `PUT` inviata alla root dell'endpoint seguita da una stringa creerà un bucket. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). I nomi bucket devono essere globalmente univoci e conformi a DNS; i nomi lunghi tra i 3 e i 63 caratteri devono essere composti da lettere minuscole, numeri e trattini. I nomi bucket devono iniziare e terminare con una lettera minuscola o un numero. I nomi bucket simili a indirizzi IP non sono consentiti. Questa operazione non utilizza parametri di query specifici dell'operazione. 

I nomi bucket devono essere univoci in quanto tutti i bucket nel cloud pubblico condividono uno spazio dei nomi globale. Ciò consente di accedere a un bucket senza dover fornire le informazioni sull'istanza del servizio o sull'account. Non è inoltre possibile creare un bucket con un nome che inizia con `cosv1-` o `account-` poiché questi prefissi sono riservati dal sistema.
{:important}

Intestazione              | Tipo   |  Descrizione
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | Stringa |  Questa intestazione fa riferimento all'istanza del servizio in cui verrà creato il bucket e in cui verrà fatturato l'utilizzo dei dati. 

**Nota**: Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo nel nome del bucket o dell'oggetto.
{:tip}

**Sintassi**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Richiesta di esempio**

Questo è un esempio di creazione di un nuovo bucket denominato 'images'.

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

----

## Crea un bucket con una classe di archiviazione diversa
{: #compatibility-api-storage-class}

Per creare un bucket con una classe di archiviazione diversa, invia un blocco XML specificando una configurazione bucket con una `LocationConstraint` di `{provisioning code}` nel corpo di una richiesta `PUT` a un endpoint del bucket. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Tieni presente che vengono applicate le [regole di denominazione](#compatibility-api-new-bucket) bucket standard. Questa operazione non utilizza parametri di query specifici dell'operazione. 

Intestazione              | Tipo   |  Descrizione
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | Stringa |  Questa intestazione fa riferimento all'istanza del servizio in cui verrà creato il bucket e in cui verrà fatturato l'utilizzo dei dati. 

**Sintassi**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

Puoi fare riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida delle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

**Richiesta di esempio**

Questo è un esempio di creazione di un nuovo bucket denominato 'vault-images'.

```http
PUT /vault-images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
Content-Length: 110
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Fri, 17 Mar 2017 17:52:17 GMT
X-Clv-Request-Id: b6483b2c-24ae-488a-884c-db1a93b9a9a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
Content-Length: 0
```

----

## Crea un nuovo bucket con le chiavi di crittografia gestite da Key Protect (SSE-KP)
{: #compatibility-api-key-protect}

Per creare un bucket in cui le chiavi di crittografia sono gestite da Key Protect, è necessario avere accesso a un'istanza del servizio Key Protect attiva situata nella stessa ubicazione del nuovo bucket. Questa operazione non utilizza parametri di query specifici dell'operazione. 

Per ulteriori informazioni sull'utilizzo di Key Protect per gestire le tue chiavi di crittografia, [consulta la documentazione](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

Tieni presente che Key Protect **non** è disponibile in una configurazione interregionale e che i bucket SSE-KP devono essere regionali.
{:tip}

Intestazione              | Tipo   |  Descrizione
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | Stringa |  Questa intestazione fa riferimento all'istanza del servizio in cui verrà creato il bucket e in cui verrà fatturato l'utilizzo dei dati. 
`ibm-sse-kp-encryption-algorithm` | Stringa | Questa intestazione viene utilizzata per specificare l'algoritmo e la dimensione chiave da utilizzare con la chiave di crittografia archiviata utilizzando Key Protect. Questo valore deve essere impostato sulla stringa `AES256`. 
`ibm-sse-kp-customer-root-key-crn`  | Stringa | Questa intestazione viene utilizzata per fare riferimento alla chiave root specifica utilizzata da Key Protect per crittografare questo bucket. Questo valore deve essere il CRN completo della chiave root. 

**Sintassi**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Richiesta di esempio**

Questo è un esempio di creazione di un nuovo bucket denominato 'secure-files'.

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

---

## Richiama le intestazioni di un bucket
{: #compatibility-api-head-bucket}

Una richiesta `HEAD` emessa per un bucket restituirà le intestazioni relative a tale bucket.

Le richieste `HEAD` non restituiscono un corpo e quindi non possono restituire messaggi di errore specifici come `NoSuchBucket`, solo `NotFound`.
{:tip}

**Sintassi**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**Richiesta di esempio**

Questo è un esempio di recupero delle intestazioni per il bucket 'images'.

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
```

**Richiesta di esempio**

Le richieste `HEAD` sui bucket con la crittografia Key Protect restituiranno intestazioni supplementari. 

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
ibm-sse-kp-enabled: True
ibm-see-kp-crk-id: {customer-root-key-id}
```

----

## Elenca gli oggetti in un determinato bucket (Versione 2)
{: #compatibility-api-list-objects-v2}

Una richiesta `GET` indirizzata a un bucket restituisce un elenco di oggetti, limitato a 1.000 alla volta e restituito in un ordine non lessicografico. Il valore `StorageClass` restituito nella risposta è un valore predefinito poiché le operazioni della classe di archiviazione non vengono implementate in COS. Questa operazione non utilizza intestazioni o elementi payload specifici dell'operazione. 

**Sintassi**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### Parametri di query facoltativi
{: #compatibility-api-list-objects-v2-params}
Nome | Tipo   |  Descrizione
--- | ---- | ------------
`list-type` | Stringa | Indica la versione 2 dell'API e il valore deve essere 2.
`prefix` | Stringa | Limita la risposta ai nomi oggetto che iniziano con `prefix`.
`delimiter` | Stringa | Raggruppa gli oggetti tra `prefix` e `delimiter`.
`encoding-type` | Stringa | Se in un nome oggetto vengono utilizzati caratteri unicode non supportati da XML, questo parametro può essere impostato su `url` per codificare correttamente la risposta.
`max-keys` | Stringa | Limita il numero di oggetti da visualizzare nella risposta. Il valore predefinito e massimo è 1.000.
`fetch-owner` | Stringa | La versione 2 dell'API non include le informazioni `Owner` per impostazione predefinita. Imposta questo parametro su `true` se desideri avere le informazioni `Owner` nella risposta. 
`continuation-token` | Stringa | Specifica l'insieme di oggetti successivo che deve essere restituito quando viene troncata la risposta (l'elemento `IsTruncated` restituisce `true`).<br/><br/>La tua risposta iniziale includerà l'elemento `NextContinuationToken`. Utilizza questo token nella risposta successiva come il valore per `continuation-token`. 
`start-after` | Stringa | Restituisce i nomi chiave dopo un oggetto chiave specifico. <br/><br/>*Questo parametro è valido solo nella tua richiesta iniziale.*  Se nella tua richiesta viene incluso un parametro `continuation-token`, questo parametro viene ignorato. 

**Richiesta di esempio (semplice con IAM)**

Questa richiesta elenca gli oggetti all'interno del bucket "apiary".

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Risposta di esempio (semplice)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 814
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <KeyCount>3</KeyCount>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Richiesta di esempio (parametro max-keys)**

Questa richiesta elenca gli oggetti all'interno del bucket "apiary" con un numero massimo di chiavi restituito impostato su 1.

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Risposta di esempio (Risposta troncata)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 598
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <NextContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Richiesta di esempio (parametro continuation-token)**

Questa richiesta elenca gli oggetti all'interno del bucket "apiary" con un continuation-token specificato.

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Risposta di esempio (Risposta troncata, parametro continuation-token)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 604
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <ContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</ContinuationToken>
  <NextContinuationToken>1a8j20CqowRrM4epIQ7fTBuyPZWZUeA8Epog16wYu9KhAPNoYkWQYhGURsIQbll1lP7c-OO-V5Vyzu6mogiakC4NSwlK4LyRDdHQgY-yPH4wMB76MfQR61VyxI4TJLxIWTPSZA0nmQQWcuV2mE4jiDA</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

### Elenca gli oggetti in un determinato bucket (obsoleto)
{: #compatibility-api-list-objects}

**Nota:** *questa API è inclusa per la compatibilità con le versioni precedenti.*  Vedi [Versione 2](#compatibility-api-list-objects-v2) per il metodo di richiamo degli oggetti consigliato in un bucket.

Una richiesta `GET` indirizzata a un bucket restituisce un elenco di oggetti, limitato a 1.000 alla volta e restituito in un ordine non lessicografico. Il valore `StorageClass` restituito nella risposta è un valore predefinito poiché le operazioni della classe di archiviazione non vengono implementate in COS. Questa operazione non utilizza intestazioni o elementi payload specifici dell'operazione. 

**Sintassi**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### Parametri di query facoltativi
{: #compatibility-api-list-objects-params}

Nome | Tipo   |  Descrizione
--- | ---- | ------------
`prefix` | Stringa | Limita la risposta ai nomi oggetto che iniziano con `prefix`.
`delimiter` | Stringa | Raggruppa gli oggetti tra `prefix` e `delimiter`.
`encoding-type` | Stringa | Se in un nome oggetto vengono utilizzati caratteri unicode non supportati da XML, questo parametro può essere impostato su `url` per codificare correttamente la risposta.
`max-keys` | Stringa | Limita il numero di oggetti da visualizzare nella risposta. Il valore predefinito e massimo è 1.000.
`marker` | Stringa | Specifica l'oggetto da cui deve iniziare l'elenco, in ordine binario UTF-8.

**Richiesta di esempio**

Questa richiesta elenca gli oggetti all'interno del bucket "apiary".

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 909
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <Marker/>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

## Elimina un bucket

Una richiesta `DELETE` emessa per un bucket vuoto elimina il bucket. Dopo aver eliminato un bucket, il nome verrà tenuto in riserva dal sistema per 10 minuti trascorsi i quali verrà rilasciato per il riutilizzo. *Puoi eliminare solo i bucket vuoti.*

**Sintassi**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### Intestazioni facoltative

Nome | Tipo   |  Descrizione
--- | ---- | ------------
`aspera-ak-max-tries` | Stringa | Specifica per quante volte puoi tentare l'operazione di eliminazione. Il valore predefinito è 2. 


**Richiesta di esempio**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

Il server risponde con `204 No Content`.

Se per l'eliminazione viene richiesto un bucket non vuoto, il server risponde con `409 Conflict`.

**Risposta di esempio**

```xml
<Error>
  <Code>BucketNotEmpty</Code>
  <Message>The bucket you tried to delete is not empty.</Message>
  <Resource>/apiary/</Resource>
  <RequestId>9d2bbc00-2827-4210-b40a-8107863f4386</RequestId>
  <httpStatusCode>409</httpStatusCode>
</Error>
```

----

## Elenca i caricamenti in più parti annullati/incompleti per un bucket

Una richiesta `GET` emessa per un bucket con i parametri appropriati richiama le informazioni sui caricamenti in più parti annullati o incompleti per un bucket.

**Sintassi**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**Parametri**

Nome | Tipo   |  Descrizione
--- | ---- | ------------
`prefix` | Stringa | Limita la risposta ai nomi oggetto che iniziano con `{prefix}`.
`delimiter` | Stringa | Raggruppa gli oggetti tra `prefix` e `delimiter`.
`encoding-type` | Stringa | Se in un nome oggetto vengono utilizzati caratteri unicode non supportati da XML, questo parametro può essere impostato su `url` per codificare correttamente la risposta.
`max-uploads` | numero intero | Limita il numero di oggetti da visualizzare nella risposta. Il valore predefinito e massimo è 1.000.
`key-marker` | Stringa | Specifica da dove deve iniziare l'elenco.
`upload-id-marker` | Stringa | Ignorato se non viene specificato `key-marker`, altrimenti imposta un punto da cui iniziare a elencare le parti prima di `upload-id-marker`.

**Richiesta di esempio**

Questo è un esempio di richiamo di tutti i caricamenti in più parti annullati e incompleti correnti. 

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio** (nessun caricamento in più parti in corso)

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:22:27 GMT
X-Clv-Request-Id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Content-Type: application/xml
Content-Length: 374
```

```xml
<ListMultipartUploadsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>apiary</Bucket>
  <KeyMarker/>
  <UploadIdMarker/>
  <NextKeyMarker>multipart-object-123</NextKeyMarker>
  <NextUploadIdMarker>0000015a-df89-51d0-2790-dee1ac994053</NextUploadIdMarker>
  <MaxUploads>1000</MaxUploads>
  <IsTruncated>false</IsTruncated>
  <Upload>
    <Key>file</Key>
    <UploadId>0000015a-d92a-bc4a-c312-8c1c2a0e89db</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-16T22:09:01.002Z</Initiated>
  </Upload>
  <Upload>
    <Key>multipart-object-123</Key>
    <UploadId>0000015a-df89-51d0-2790-dee1ac994053</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-18T03:50:02.960Z</Initiated>
  </Upload>
</ListMultipartUploadsResult>
```

----

## Elenca qualsiasi configurazione CORS (Cross-Origin Resource Sharing) per un bucket

Una richiesta `GET` emessa per un bucket con i parametri appropriati richiama le informazioni sulla configurazione CORS (Cross-Origin Resource Sharing) per un bucket.

**Sintassi**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Richiesta di esempio**

Questo è un esempio di elenco di una configurazione CORS nel bucket "apiary".

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio** 

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:20:30 GMT
X-Clv-Request-Id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Content-Type: application/xml
Content-Length: 123
```

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <CORSRule>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
  </CORSRule>
</CORSConfiguration>
```

----

## Crea una configurazione CORS (Cross-Origin Resource Sharing) per un bucket

Una richiesta `PUT` emessa per un bucket con i parametri appropriati crea o sostituisce una configurazione CORS (Cross-Origin Resource Sharing) per un bucket.

L'intestazione `Content-MD5` richiesta deve essere la rappresentazione binaria di un hash MD5 con codifica base64.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Sintassi**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Richiesta di esempio**

Questo è un esempio di aggiunta di una configurazione CORS che consente alle richieste provenienti da `www.ibm.com` di emettere le richieste `GET`, `PUT` e `POST` per il bucket.

```http
PUT /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 237
```

```xml
<CORSConfiguration>
  <CORSRule>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
  </CORSRule>
</CORSConfiguration>
```


**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```

----

## Elimina qualsiasi configurazione CORS (Cross-Origin Resource Sharing) per un bucket

Una richiesta `DELETE` emessa per un bucket con i parametri appropriati crea o sostituisce una configurazione CORS (Cross-Origin Resource Sharing) per un bucket. 

**Sintassi**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Richiesta di esempio**

Questo è un esempio di eliminazione di una configurazione CORS per un bucket.

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

Il server risponde con `204 No Content`.

----

## Elenca il vincolo di ubicazione per un bucket

Una richiesta `GET` emessa per un bucket con il parametro appropriato richiama le informazioni sull'ubicazione di un bucket. 

**Sintassi**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**Richiesta di esempio**

Questo è un esempio di richiamo dell'ubicazione del bucket "apiary".

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Risposta di esempio**

```http
HTTP/1.1 200 OK
Date: Tue, 12 Jun 2018 21:10:57 GMT
X-Clv-Request-Id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Content-Type: application/xml
Content-Length: 161
```

```xml
<LocationConstraint xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  us-south-standard
</LocationConstraint>
```

----

## Crea la configurazione del ciclo di vita di un bucket
{: #compatibility-api-create-bucket-lifecycle}

Un'operazione `PUT` utilizza il parametro di query lifecycle per configurare le impostazioni del ciclo di vita per il bucket. Viene richiesta un'intestazione `Content-MD5` come controllo dell'integrità per il payload.

**Sintassi**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Elementi del payload**

Il corpo della richiesta deve contenere un blocco XML con il seguente schema:

|Elemento| Tipo   |Elemento secondario|Predecessore|Vincolo|
|---|---|---|---|---|
|LifecycleConfiguration|Contenitore|Rule| Nessuno |Limite 1|
|Rule|Contenitore|ID, Status, Filter, Transition|LifecycleConfiguration|Limite 1|
|ID| Stringa | Nessuno |Rule|**Deve** essere composto da ``(a-z,A- Z0-9)`` e dai seguenti simboli:`` !`_ .*'()- ``|
|Filter| Stringa |Prefix|Rule|**Deve** contenere un elemento `Prefix`.|
|Prefix| Stringa | Nessuno |Filter|**Deve** essere impostato su `<Prefix/>`.|
|Transition|Contenitore|Days, StorageClass|Rule|Limite 1.|
|Days|Numero interno non negativo| Nessuno |Transition|**Deve** essere un valore maggiore di 0.|
|Date|Date| Nessuno |Transition|**Deve** essere nel formato ISO 8601 e la data deve essere futura. |
|StorageClass| Stringa | Nessuno |Transition|**Deve** essere impostato su GLACIER.|

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>{string}</ID>
        <Status>Enabled</Status>
        <Filter>
            <Prefix/>
        </Filter>
        <Transition>
            <Days>{integer}</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

**Richiesta di esempio**

```http
PUT /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ== 
Content-Length: 305
```

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

Il server risponde con `200 OK`.

----

## Richiama la configurazione del ciclo di vita di un bucket

Un'operazione `GET` utilizza il parametro di query lifecycle per richiamare le impostazioni del ciclo di vita per il bucket. 

**Sintassi**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Richiesta di esempio**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**Risposta di esempio**

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

----

## Elimina la configurazione del ciclo di vita per un bucket

Una richiesta `DELETE` emessa per un bucket con i parametri appropriati rimuove le configurazioni del ciclo di vita per un bucket. 

**Sintassi**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Richiesta di esempio**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

Il server risponde con `204 No Content`.
