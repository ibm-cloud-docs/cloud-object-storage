---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: worm, immutable, policy, retention, compliance

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

# Utilizza Immutable Object Storage
{: #immutable}

Immutable Object Storage consente ai clienti di conservare record elettronici e di mantenere l'integrità in un WORM (Write-Once-Read-Many), in modo non eliminabile e non riscrivibile, fino al termine del loro periodo di conservazione e alla rimozione delle conservazioni a fini legali. Questa funzione può essere utilizzata dai clienti che necessitano di conservare i dati a lungo termine nel loro ambiente, incluse ma non limitate alle organizzazioni presenti nei seguenti settori: 

 * Finanziario
 * Assistenza sanitaria
 * Archivi di contenuti multimediali
 * Coloro che stanno cercando di impedire l'eliminazione o la modifica privilegiata di oggetto o documenti

Le capacità della funzione sottostante possono essere utilizzate anche dalle organizzazioni che si occupano della gestione dei record finanziari, come le transazioni dell'intermediatore finanziario, e possono aver bisogno di conservare gli oggetti in un formato non riscrivibile e non eliminabile. 

Immutable Object Storage è disponibile solo in determinate regioni, per i dettagli, vedi [Servizi integrati](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability). Richiede anche un piano dei prezzi standard. Per i dettagli, vedi i [prezzi](https://www.ibm.com/cloud/object-storage).
{:note}

Non è possibile utilizzare il trasferimento ad alta velocità Aspera con i bucket con una politica di conservazione.
{:important}

## Terminologia e uso
{: #immutable-terminology}

### Periodo di conservazione
{: #immutable-terminology-period}

Il periodo di tempo durante il quale l'oggetto deve rimanere archiviato nel bucket COS.

### Politica di conservazione
{: #immutable-terminology-policy}

Una politica di conservazione è abilitata a livello del bucket COS. I periodi di conservazione minimo, massimo e predefinito sono definiti da questa politica e si applicano a tutti gli oggetti nel bucket. 

Il periodo di conservazione minimo è la durata minima del periodo di tempo durante il quale un oggetto deve essere conservato nel bucket.

Il periodo di conservazione massimo è la durata massima del periodo di tempo durante il quale un oggetto può essere conservato nel bucket.

Se un oggetto viene archiviato nel bucket senza specificare un periodo di conservazione personalizzato, viene utilizzato il periodo di conservazione predefinito. Il periodo di conservazione minimo deve essere minore o uguale al periodo di conservazione predefinito, che a sua volta deve essere minore o uguale al periodo di conservazione massimo. 

Nota: per gli oggetti può essere specificato un periodo di conservazione massimo pari a 1000 anni. 

Nota: per creare una politica di conservazione in un bucket, avrai bisogno del ruolo di gestore (Manager). Per ulteriori dettagli, vedi [Autorizzazioni bucket](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions).

### Conservazione a fini legali 
{: #immutable-terminology-hold}

È possibile che determinati record (oggetti) non debbano essere eliminati anche dopo la scadenza del loro periodo di conservazione, ad esempio una revisione legale che è in attesa di completamento può richiedere l'accesso ai record per una durata prolungata che va oltre il periodo di conservazione originariamente configurato per l'oggetto. In uno scenario di questo tipo, può essere applicato un indicatore di conservazione a fini legali a livello dell'oggetto.
 
Le conservazioni a fini legali possono essere applicate agli oggetti durante i caricamenti iniziali nel bucket cos o una volta che è stato aggiunto un oggetto.
 
Nota: possono essere applicate massimo 100 conservazioni a fini legali per oggetto. 

### Conservazione indefinita
{: #immutable-terminology-indefinite}

Consente all'utente di configurare l'oggetto in modo che venga archiviato in modo indefinito fino a quando non viene applicato un nuovo periodo di conservazione. Viene configurata a livello di singolo oggetto. 

### Conservazione basata sull'evento
{: #immutable-terminology-events}

Immutable Object Storage consente agli utenti di configurare una conservazione indefinita per l'oggetto se non sono sicuri della durata finale del periodo di conservazione per il loro caso di utilizzo oppure se desiderano utilizzare la capacità di conservazione basata sull'evento. Una volta impostate su indefinite, le applicazioni utente possono poi cambiare la conservazione dell'oggetto in un valore finito in un secondo tempo. Ad esempio, un'azienda ha una politica di conservazione dei record dei dipendenti di tre anni dopo che il dipendente lascia la società. Quando un dipendente si unisce all'azienda, i record associati a tale dipendente possono essere conservati in modo indefinito. Quando il dipendente lascia l'azienda, la conservazione indefinita viene convertita in un valore finito di tre anni dal tempo corrente, come definito dalla politica aziendale. L'oggetto viene poi protetto per tre anni dopo la modifica del periodo di conservazione. Un'applicazione utente o di terze parti può modificare il periodo di conservazione da indefinito a finito utilizzando un SDK o un'API REST.

### Conservazione permanente
{: #immutable-terminology-permanent}

La conservazione permanente può essere abilitata solo a livello di bucket COS con la politica di conservazione abilitata e gli utenti possono selezionare l'opzione del periodo di conservazione permanente durante i caricamenti degli oggetti. Una volta abilitato, questo processo non può essere annullato e gli oggetti caricati utilizzando il periodo di conservazione permanente **non possono essere eliminati**. È responsabilità degli utenti eseguire la convalida se c'è una necessità legittima di archiviare **in modo permanente** gli oggetti utilizzando i bucket COS con la politica di conservazione. 


Quando utilizzi Immutable Object Storage, devi assicurarti che il tuo account IBM Cloud sia in regola con le politiche e le linee guida di IBM Cloud per tutto il tempo durante il quale i dati saranno soggetti a una politica di conservazione. Per ulteriori informazioni, vedi i termini del servizio di IBM Cloud.
{:important}

## Immutable Object Storage e considerazione per i vari regolamenti
{: #immutable-regulation}

Quando si utilizza Immutable Object Storage, è responsabilità del cliente controllare e assicurarsi che le capacità della funzione trattate possano essere utilizzate per soddisfare ed essere conformi alle regole chiave relative alla conservazione e all'archiviazione dei record elettronici che generalmente sono regolate da:

  * [Securities and Exchange Commission (SEC) Rule 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64),
  * [Financial Industry Regulatory Authority (FINRA) Rule 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957) e
  * [Commodity Futures Trading Commission (CFTC) Rule 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

Per assistere i clienti nel prendere decisioni informate, IBM ha ingaggiato la società Cohasset Associates Inc. per condurre una valutazione indipendente della capacità di Immutable Object Storage di IBM. Esamina il [report](https://www.ibm.com/downloads/cas/JBDNP0KV) della Cohasset Associates Inc. che fornisce dettagli sulla valutazione della capacità di Immutable Object Storage di IBM Cloud Object Storage. 

### Controllo dell'accesso e delle transazioni
{: #immutable-audit}
I dati di log di accesso per Immutable Object Storage per esaminare le modifiche ai parametri di conservazione, al periodo di conservazione dell'oggetto e all'applicazione delle conservazioni a fini legali sono disponibili su base di caso per caso aprendo un ticket del servizio clienti.

## Utilizzo della console
{: #immutable-console}

Le politiche di conservazione possono essere aggiunte a bucket vuoti nuovi o esistenti e non possono essere rimosse. Per un nuovo bucket, assicurati di creare il bucket in una [regione supportata](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability) e poi scegli l'opzione **Add retention policy**. Per un bucket esistente, assicurati che non contenga oggetti e poi passa alle impostazioni di configurazione e fai clic sul pulsante **Create policy** sotto la sezione della politica di conservazione del bucket. In entrambi i casi, configura i periodi di conservazione minimo, massimo e predefinito. 

## Utilizzo dell'API REST, delle librerie e degli SDK
{: #immutable-sdk}

Sono state introdotte diverse nuove API negli SDK IBM COS per fornire supporto per le applicazioni che utilizzano le politiche di conservazione. Seleziona un linguaggio (HTTP, Java, Javascript, Go o Python) nella parte superiore di questa pagina per visualizzare gli esempi che utilizzano l'SDK COS appropriato.  

Tieni presente che tutti gli esempi di codice presuppongono l'esistenza di un oggetto client denominato `cos` che può richiamare i diversi metodi. Per dettagli sulla creazione dei client, vedi le guide SDK specifiche. 

Tutti i valori di data utilizzati per impostare i periodi conservazione hanno formato GMT. Un'intestazione `Content-MD5` è necessaria per assicurare l'integrità dei dati e viene inviata automaticamente quando viene utilizzato un SDK.
{:note}

### Aggiungi una politica di conservazione a un bucket esistente
{: #immutable-sdk-add-policy}
Questa implementazione dell'operazione `PUT` utilizza il parametro di query `protection` per impostare i parametri di conservazione per un bucket esistente. Questa operazione ti consente di impostare o modificare il periodo di conservazione minimo, predefinito e massimo. Questa operazione ti consente anche di modificare lo stato di protezione del bucket. 

Gli oggetti scritti in un bucket protetto non possono essere eliminati fino a quando il periodo di protezione non è scaduto e tutte le conservazioni a fini legali sull'oggetto non sono stati rimossi. Il valore di conservazione predefinito del bucket viene dato ad un oggetto a meno che non venga fornito un valore specifico dell'oggetto quando l'oggetto viene creato. Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket. 

I valori supportati minimo e massimo per le impostazioni del periodo di conservazione `MinimumRetention`, `DefaultRetention` e `MaximumRetention` sono, rispettivamente, 0 giorni e 365243 giorni (1000 anni). 

È necessaria un'intestazione `Content-MD5`. Questa operazione non utilizza parametri di query aggiuntivi. 

Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

{: http}

**Sintassi**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```
PUT /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
x-amz-content-sha256: 2938f51643d63c864fdbea618fe71b13579570a86f39da2837c922bae68d72df
Content-MD5: GQmpTNpruOyK6YrxHnpj7g==
Content-Type: text/plain
Host: 67.228.254.193
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.14.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```
{: codeblock}
{: http}

```py
def add_protection_configuration_to_bucket(bucket_name):
    try:
        new_protection_config = {
            "Status": "Retention",
            "MinimumRetention": {"Days": 10},
            "DefaultRetention": {"Days": 100},
            "MaximumRetention": {"Days": 1000}
        }

        cos.put_bucket_protection_configuration(Bucket=bucket_name, ProtectionConfiguration=new_protection_config)

        print("Protection added to bucket {0}\n".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to set bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addProtectionConfigurationToBucket(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    cos.setBucketProtection(bucketName, newConfig);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}

public static void addProtectionConfigurationToBucketWithRequest(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    SetBucketProtectionConfigurationRequest newRequest = new SetBucketProtectionConfigurationRequest()
        .withBucketName(bucketName)
        .withProtectionConfiguration(newConfig);

    cos.setBucketProtectionConfiguration(newRequest);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}
```
{: codeblock}
{: java}

### Controlla la politica di conservazione su un bucket
{: #immutable-sdk-get}

Questa implementazione di un'operazione GET recupera i parametri di conservazione per un bucket esistente.
{: http}

**Sintassi**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```xml
GET /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
Content-Type: text/plain
Host: 67.228.254.193
Example response
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.13.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

Se non ci sono configurazioni di protezione sul bucket, il server risponde invece con lo stato disabilitato.
{: http}

```xml
<ProtectionConfiguration>
  <Status>Disabled</Status>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

```py
def get_protection_configuration_on_bucket(bucket_name):
    try:
        response = cos.get_bucket_protection_configuration(Bucket=bucket_name)
        protection_config = response.get("ProtectionConfiguration")

        print("Bucket protection config for {0}\n".format(bucket_name))
        print(protection_config)
        print("\n")
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to get bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void getProtectionConfigurationOnBucket(String bucketName) {
    System.out.printf("Retrieving protection configuration from bucket: %s\n", bucketName;

    BucketProtectionConfiguration config = cos.getBucketProtection(bucketName);

    String status = config.getStatus();

    System.out.printf("Status: %s\n", status);

    if (!status.toUpperCase().equals("DISABLED")) {
        System.out.printf("Minimum Retention (Days): %s\n", config.getMinimumRetentionInDays());
        System.out.printf("Default Retention (Days): %s\n", config.getDefaultRetentionInDays());
        System.out.printf("Maximum Retention (Days): %s\n", config.getMaximumRetentionInDays());
    }
}
```
{: codeblock}
{: java}

### Carica un oggetto in un bucket con la politica di conservazione
{: #immutable-sdk-upload}

Questo miglioramento dell'operazione `PUT` aggiunge tre nuove intestazioni della richiesta: due per specificare il periodo di conservazione in modi diversi e uno per aggiungere una singola conservazione a fini legali al nuovo oggetto. Vengono definiti nuovi errori per i valori non validi relativi alle nuove intestazioni e se un oggetto è oggetto di conservazione, le sovrascritture avranno esito negativo.
{: http}

Gli oggetti nei bucket con la politica di conservazione che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket.

È necessaria un'intestazione `Content-MD5`.
{: http}

Queste intestazioni si applicano all'oggetto POST e anche alle richieste di caricamento in più parti. Se un oggetto viene caricato in più parti, ciascuna parte richiede un'intestazione `Content-MD5`.
{: http}

|Valore	| Tipo	| Descrizione |
| --- | --- | --- | 
|`Retention-Period` | Numero intero non negativo (secondi) | Il periodo di conservazione da memorizzare sull'oggetto, in secondi. L'oggetto non può essere sovrascritto o eliminato finché l'intervallo di tempo specificato nel periodo di conservazione non è trascorso. Se vengono specificati questi campi e `Retention-Expiration-Date`, viene restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo `DefaultRetention` del bucket. Zero (`0`) è un valore consentito, presumendo che il periodo di conservazione minimo del bucket sia anch'esso `0`. |
| `Retention-expiration-date` | Data (formato ISO 8601) | Data in cui sarà consentito eliminare o modificare l'oggetto. Puoi specificare solo questo valore oppure l'intestazione Retention-Period. Se vengono specificati entrambi, verrà restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo DefaultRetention del bucket. |
| `Retention-legal-hold-id` | stringa | Una singola conservazione a fini legali da applicare all'oggetto. Una conservazione a fini legali è una stringa di caratteri di lunghezza Y. L'oggetto non può essere sovrascritto o eliminato finché non sono state rimosse tutte le conservazioni a fini legali a esso associate.|

```py
def put_object_add_legal_hold(bucket_name, object_name, file_text, legal_hold_id):
    print("Add legal hold {0} to {1} in bucket {2} with a putObject operation.\n".format(legal_hold_id, object_name, bucket_name))
    
    cos.put_object(
        Bucket=bucket_name,
        Key=object_name,
        Body=file_text, 
        RetentionLegalHoldId=legal_hold_id)

    print("Legal hold {0} added to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))
  
def copy_protected_object(source_bucket_name, source_object_name, destination_bucket_name, new_object_name):
    print("Copy protected object {0} from bucket {1} to {2}/{3}.\n".format(source_object_name, source_bucket_name, destination_bucket_name, new_object_name))

    copy_source = {
        "Bucket": source_bucket_name,
        "Key": source_object_name
    }

    cos.copy_object(
        Bucket=destination_bucket_name, 
        Key=new_object_name, 
        CopySource=copy_source, 
        RetentionDirective="Copy"
    )

    print("Protected object copied from {0}/{1} to {2}/{3}\n".format(source_bucket_name, source_object_name, destination_bucket_name, new_object_name));

def complete_multipart_upload_with_retention(bucket_name, object_name, upload_id, retention_period):
    print("Completing multi-part upload for object {0} in bucket {1}\n".format(object_name, bucket_name))

    cos.complete_multipart_upload(
        Bucket=bucket_name, 
        Key=object_name,
        MultipartUpload={
            "Parts":[{
                "ETag": part["ETag"],
                "PartNumber": 1
            }]
        },
        UploadId=upload_id,
        RetentionPeriod=retention_period
    )

    print("Multi-part upload completed for object {0} in bucket {1}\n".format(object_name, bucket_name))

def upload_file_with_retention(bucket_name, object_name, path_to_file, retention_period):
    print("Uploading file {0} to object {1} in bucket {2}\n".format(path_to_file, object_name, bucket_name))
    
    args = {
        "RetentionPeriod": retention_period
    }

    cos.upload_file(
        Filename=path_to_file,
        Bucket=bucket_name,
        Key=object_name,
        ExtraArgs=args
    )

    print("File upload complete to object {0} in bucket {1}\n".format(object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void putObjectAddLegalHold(String bucketName, String objectName, String fileText, String legalHoldId) {
    System.out.printf("Add legal hold %s to %s in bucket %s with a putObject operation.\n", legalHoldId, objectName, bucketName);

    InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileText.length());

    PutObjectRequest req = new PutObjectRequest(
        bucketName,
        objectName,
        newStream,
        metadata
    );
    req.setRetentionLegalHoldId(legalHoldId);

    cos.putObject(req);

    System.out.printf("Legal hold %s added to object %s in bucket %s\n", legalHoldId, objectName, bucketName);
}

public static void copyProtectedObject(String sourceBucketName, String sourceObjectName, String destinationBucketName, String newObjectName) {
    System.out.printf("Copy protected object %s from bucket %s to %s/%s.\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);

    CopyObjectRequest req = new CopyObjectRequest(
        sourceBucketName, 
        sourceObjectName, 
        destinationBucketName, 
        newObjectName
    );
    req.setRetentionDirective(RetentionDirective.COPY);
    

    cos.copyObject(req);

    System.out.printf("Protected object copied from %s/%s to %s/%s\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);
}
```
{: codeblock}
{: java}

### Aggiungi o rimuovi una conservazione a fini legati a/da un oggetto
{: #immutable-sdk-legal-hold}

Questa implementazione dell'operazione `POST` utilizza il parametro di query `legalHold` e i parametri di query `add` e `remove` per aggiungere o rimuovere una singola conservazione a fini legali da un oggetto protetto in un bucket protetto.
{: http}

L'oggetto può supportare 100 conservazioni a fini legali:

*  Un identificativo di conservazione a fini locali è una stringa di una lunghezza massima di 64 caratteri e una lunghezza minima di 1 carattere. I caratteri validi sono lettere, numeri, `!`, `_`, `.`, `*`, `(`, `)`, `-` e `.
* Se l'aggiunta di una specifica conservazione a fini legali comporta il superamento di un totale di 100 conservazioni a fini legali sull'oggetto, la nuova conservazione a fini legali non viene aggiunta e viene restituito un errore `400`.
* Se un identificativo è troppo lungo, non viene aggiunto all'oggetto e viene restituito un errore `400`.
* Se un identificativo contiene caratteri non validi, non viene aggiunto all'oggetto e viene restituito un errore `400`.
* Se un identificativo è già in uso su un oggetto, la conservazione a fini legali esistente non viene modificata e la risposta indica che l'identificativo era già in uso con un errore `409`.
* Se un oggetto non ha metadati del periodo di conservazione, viene restituito un errore `400` e l'aggiunta o la rimozione di una conservazione a fini legali non è consentita.

La presenza di un'intestazione di periodo di conservazione è obbligatoria, altrimenti viene restituito un errore `400`.
{: http}

L'utente che esegue l'aggiunta o la rimozione di una conservazione a fini legali deve disporre delle autorizzazioni di `Manager` (gestore) per questo bucket.

È necessaria un'intestazione `Content-MD5`. Questa operazione non utilizza elementi payload specifici dell'operazione.
{: http}

**Sintassi**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Richiesta di esempio**
{: http}

```
POST /BucketName/ObjectName?legalHold&add=legalHoldID HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}

```py
def add_legal_hold_to_object(bucket_name, object_name, legal_hold_id):
    print("Adding legal hold {0} to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.add_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} added to object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))

def delete_legal_hold_from_object(bucket_name, object_name, legal_hold_id):
    print("Deleting legal hold {0} from object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.delete_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} deleted from object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addLegalHoldToObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Adding legal hold %s to object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.addLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s added to object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}

public static void deleteLegalHoldFromObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Deleting legal hold %s from object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.deleteLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s deleted from object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}
```
{: codeblock}
{: java}

### Estendi il periodo di conservazione di un oggetto
{: #immutable-sdk-extend}

Questa implementazione dell'operazione `POST` utilizza il parametro di query `extendRetention` per estendere il periodo di conservazione di un oggetto protetto in un bucket protetto.
{: http}

Il periodo di conservazione di un oggetto può solo essere esteso. Non può essere ridotto rispetto al valore attualmente configurato.

Il valore di espansione della conservazione è impostato in uno di tre possibili modi:

* ulteriore tempo dal valore corrente (`Additional-Retention-Period` o metodo simile)
* nuovo periodo di estensione in secondi (`Extend-Retention-From-Current-Time` o metodo simile)
* nuova data di scadenza della conservazione dell'oggetto (`New-Retention-Expiration-Date` o metodo simile)

Il periodo di conservazione corrente memorizzato nei metadati dell'oggetto viene aumentato in misura equivalente al tempo aggiuntivo indicato oppure sostituito con il nuovo valore, a seconda del parametro impostato nella richiesta `extendRetention`. In tutti i casi, il parametro di estensione della conservazione viene controllato rispetto al periodo di conservazione corrente e il parametro esteso viene accettato solo se il periodo di conservazione aggiornato è più grande del periodo di conservazione corrente. 

Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket.

**Sintassi**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```yaml
POST /BucketName/ObjectName?extendRetention HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00GMT
Authorization: authorization string
Content-Type: text/plain
Additional-Retention-Period: 31470552
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:50:00GMT
Connection: close
```
{: codeblock}
{: http}

```py
def extend_retention_period_on_object(bucket_name, object_name, additional_seconds):
    print("Extend the retention period on {0} in bucket {1} by {2} seconds.\n".format(object_name, bucket_name, additional_seconds))

    cos.extend_object_retention(
        Bucket=bucket_ame,
        Key=object_name,
        AdditionalRetentionPeriod=additional_seconds
    )

    print("New retention period on {0} is {1}\n".format(object_name, additional_seconds))
```
{: codeblock}
{: python}

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void extendRetentionPeriodOnObject(String bucketName, String objectName, Long additionalSeconds) {
    System.out.printf("Extend the retention period on %s in bucket %s by %s seconds.\n", objectName, bucketName, additionalSeconds);

    ExtendObjectRetentionRequest req = new ExtendObjectRetentionRequest(
        bucketName, 
        objectName)
        .withAdditionalRetentionPeriod(additionalSeconds);

    cos.extendObjectRetention(req);

    System.out.printf("New retention period on %s is %s\n", objectName, additionalSeconds);
}
```
{: codeblock}
{: java}

### Elenca le conservazioni a fini legali su un oggetto
{: #immutable-sdk-list-holds}

Questa implementazione dell'operazione `GET` utilizza il parametro di query `legalHold` per restituire l'elenco delle conservazioni a fini legali su un oggetto e lo stato di conservazione correlato in un corpo della risposta XML.
{: http}

Questa operazione restituisce:

* La data di creazione dell'oggetto
* Il periodo di conservazione dell'oggetto in secondi
* La data di scadenza della conservazione calcolata sulla base del periodo e della data di creazione
* L'elenco delle conservazioni a fini legali
* L'identificativo della conservazione a fini legali
* La data/ora di quando è stata applicata la conservazione a fini legali

Se non ci sono conservazioni a fini legali sull'oggetto, viene restituito un `LegalHoldSet` vuoto.
Se non c'è alcun periodo di conservazione specificato sull'oggetto, viene restituito un errore `404`.

**Sintassi**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Richiesta di esempio**
{: http}

```
GET /BucketName/ObjectName?legalHold HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: {authorization-string}
Content-Type: text/plain
```
{: codeblock}
{: http}

**Risposta di esempio**
{: http}

```xml
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
<?xml version="1.0" encoding="UTF-8"?>
<RetentionState>
  <CreateTime>Fri, 8 Sep 2018 21:33:08 GMT</CreateTime>
  <RetentionPeriod>220752000</RetentionPeriod>
  <RetentionPeriodExpirationDate>Fri, 1 Sep 2023 21:33:08
GMT</RetentionPeriodExpirationDate>
  <LegalHoldSet>
    <LegalHold>
      <ID>SomeLegalHoldID</ID>
      <Date>Fri, 8 Sep 2018 23:13:18 GMT</Date>
    </LegalHold>
    <LegalHold>
    ...
    </LegalHold>
  </LegalHoldSet>
</RetentionState>
```
{: codeblock}
{: http}

```py 
def list_legal_holds_on_object(bucket_name, object_name):
    print("List all legal holds on object {0} in bucket {1}\n".format(object_name, bucket_name));

    response = cos.list_legal_holds(
        Bucket=bucket_name,
        Key=object_name
    )

    print("Legal holds on bucket {0}: {1}\n".format(bucket_name, response))
```
{: codeblock}
{: python}

```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void listLegalHoldsOnObject(String bucketName, String objectName) {
    System.out.printf("List all legal holds on object %s in bucket %s\n", objectName, bucketName);

    ListLegalHoldsResult result = cos.listLegalHolds(
        bucketName, 
        objectName
    );

    System.out.printf("Legal holds on bucket %s: \n", bucketName);

    List<LegalHold> holds = result.getLegalHolds();
    for (LegalHold hold : holds) {
        System.out.printf("Legal Hold: %s", hold);
    }
}
```
{: codeblock}
{: java}
