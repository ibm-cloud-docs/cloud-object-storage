---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-05"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:external: target="blank" .external}
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 
{:http: .ph data-hd-programlang='http'} 

# Elimina i dati obsoleti con le regole di scadenza
{: #expiry}

Una regola di scadenza elimina gli oggetti dopo un periodo definito (dalla data di creazione dell'oggetto). 

Puoi configurare il ciclo di vita degli oggetti utilizzando la console web, l'API REST e gli strumenti di terze parti integrati con {{site.data.keyword.cos_full_notm}}. 

* Una regola di scadenza può essere aggiunta a un nuovo bucket o a uno esistente.
* Una regola di scadenza esistente può essere modificata o disabilitata. 
* Una regola di scadenza appena aggiunta o modificata si applica a tutti gli oggetti nuovi ed esistenti nel bucket.
* L'aggiunta o la modifica delle politiche del ciclo di vita richiedono il ruolo di scrittore (`Writer`). 
* Puoi definire massimo 1000 regole del ciclo di vita (archiviazione + scadenza) per bucket.
* Sono consentite fino a 24 ore affinché le modifiche nelle regole di scadenza diventino effettive. 
* L'ambito di ciascuna regola di scadenza può essere limitato definendo un filtro di prefisso facoltativo da applicare a un solo sottoinsieme di oggetti con nomi che corrispondono al prefisso. 
* Una regola di scadenza senza un filtro di prefisso si applicherà a tutti gli oggetti nel bucket.
* Il periodo di scadenza per un oggetto, specificato in numero di giorni, viene calcolato dal momento della creazione dell'oggetto e viene arrotondato alla mezzanotte UTC del giorno successivo. Ad esempio, se hai una regola di scadenza per un bucket che stabilisce che un insieme di oggetti scade dieci giorni dopo la data di creazione, un oggetto che è stato creato il 15 aprile 2019 05:10 UTC scadrà il 26 aprile 2019 00:00 UTC. 
* Le regole di scadenza per ciascun bucket vengono valutate una volta ogni 24 ore. Qualsiasi oggetto che si qualifica per la scadenza (in base alla data di scadenza degli oggetti) verrà accodato per l'eliminazione. L'eliminazione degli oggetti scaduti inizia il giorno successivo e, di norma, impiegherà meno di 24 ore. Non ti verrà fatturata alcuna archiviazione associata per gli oggetti una volta eliminati. 

Per gli oggetti che sono soggetti alla politica di conservazione di Immutable Object Storage di un bucket, le azioni di scadenza verranno rimandate fino a quando la politica di conservazione non verrà più applicata.
{: important}

## Attributi delle regole di scadenza
{: #expiry-rules-attributes}

Ogni regola di scadenza ha i seguenti attributi:

### ID
Un ID della regola deve essere univoco all'interno della configurazione del ciclo di vita del bucket. 

### Expiration
Un blocco di scadenza contiene i dettagli che regolano l'eliminazione automatica degli oggetti. Potrebbe essere una data specifica nel futuro oppure un periodo di tempo successivo a quando sono stati scritti i nuovi oggetti. 

### Prefix
Una stringa facoltativa verrà messa in corrispondenza con il prefisso del nome oggetto nel bucket. Una regola con un prefisso verrà applicata solo agli oggetti che corrispondono. Puoi utilizzare più regole per azioni di scadenza diverse per prefissi differenti all'interno dello stesso bucket. Ad esempio, all'interno della stessa configurazione del ciclo di vita, una regola potrebbe eliminare tutti gli oggetti che iniziano con `logs/` dopo 30 giorni e una seconda regola potrebbe eliminare gli oggetti che iniziano con `video/` dopo 365 giorni.  

### Status
Una regola può essere abilitata e disabilitata. Una regola è attiva solo quando è abilitata. 

## Configurazioni del ciclo di vita di esempio

Questa configurazione fa scadere tutti i nuovi oggetti dopo 30 giorni. 

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-30-days</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>30</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

Questa configurazione elimina tutti gli oggetti con il prefisso `foo/` il 1° giugno 2020.

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-on-a-date</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Date>2020-06-01T00:00:00.000Z</Date>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

Puoi anche combinare le regole di transizione e di scadenza. Questa configurazione archivia tutti gli oggetti 90 giorni dopo la creazione ed elimina tutti gli oggetti con il prefisso `foo/` dopo 180 giorni.

```xml
<LifecycleConfiguration>
  <Rule>
		<ID>archive-first</ID>
		<Filter />
		<Status>Enabled</Status>
    <Transition>
      <Days>90</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
	</Rule>
	<Rule>
		<ID>then-delete</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Days>180</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

## Utilizzo della console
{: #expiry-using-console}

Quando crei un nuovo bucket, seleziona la casella **Add expiration rule**. Poi, fai clic su **Add rule** per creare la nuova regola di scadenza. Puoi aggiungere fino a cinque regole durante la creazione del bucket e puoi aggiungere regole supplementari in un secondo momento. 

Per un bucket esistente, seleziona **Configuration** dal menu di navigazione e fai clic su **Add rule** nella sezione _Expiration rule_.

## Utilizzo dell'API e degli SDK
{: #expiry-using-api-sdks}

Puoi gestire in modo programmatico le regole di scadenza utilizzando l'API REST o gli SDK di IBM COS. Seleziona il formato per gli esempi selezionando una categoria nel commutatore di contesto. 

### Aggiungi una regola di scadenza alla configurazione del ciclo di vita di un bucket
{: #expiry-api-create}

**Riferimento API REST**
{: http}

Questa implementazione dell'operazione `PUT` utilizza il parametro di query `lifecycle` per configurare le impostazioni di ciclo di vita per il bucket. Questa operazione consente una singola definizione della politica del ciclo di vita per un bucket. La politica viene definita come un insieme di regole composto dai seguenti parametri: `ID`, `Status`, `Filter` ed `Expiration`.
{: http}
 
Gli utenti Cloud IAM devono disporre del ruolo di scrittore (`Writer`) per rimuovere una politica del ciclo di vita da un bucket.

Gli utenti dell'infrastruttura classica devono disporre almeno delle autorizzazioni di proprietario (`Owner`) per il bucket per rimuovere una politica del ciclo di vita da un bucket.

Intestazione              | Tipo   |Descrizione
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Stringa | **Obbligatorio**: l'hash MD5 a 128 bit con codifica base64 del payload, utilizzato come un controllo dell'integrità per garantire che il payload non è stato modificato in transito. 
{: http}

Il corpo della richiesta deve contenere un blocco XML con il seguente schema:
{: http}

|Elemento| Tipo   |Elemento secondario|Predecessore|Vincolo|
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | Nessuno                  | Limite 1.                                                                                  |
| `Rule`                   | Container            | `ID`, `Status`, `Filter`, `Expiration` | `LifecycleConfiguration` | Limite 1000.                                                                                  |
| `ID`                     | Stringa              | Nessuno                                | `Rule`                   | Deve essere composto da (`a-z,`A-Z0-9`) e dai seguenti simboli: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | Stringa              | `Prefix`                               | `Rule`                   | Deve contenere un elemento `Prefix`                                                            |
| `Prefix`                 | Stringa              | Nessuno                                | `Filter`                 | La regola si applica a tutti gli oggetti con le chiavi che corrispondono a questo prefisso.                                                           |
| `Expiration`             | `Container`          | `Days` o `Date`                       | `Rule`                   | Limite 1.                                                                                  |
| `Days`                   |Numero interno non negativo| Nessuno                                | `Expiration`             | Deve essere un valore maggiore di 0.                                                           |
| `Date`                   | Date                 | Nessuno                                | `Expiration`             | Deve essere nel formato ISO 8601.                            |
{: http}

Il corpo della richiesta deve contenere un blocco XML con lo schema trattato nella tabella (vedi Esempio 1).
{: http}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Esempio 1. Esempio di XML dal corpo della richiesta." caption-side="bottom"}
{: http}

**Sintassi**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Esempio 2. Osserva l'uso delle barre e dei punti in questo esempio di sintassi." caption-side="bottom"}
{: codeblock}
{: http}

**Richiesta di esempio**
{: http}

```yaml
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305

<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Esempio 3. Esempi di intestazione della richiesta per la creazione della configurazione del ciclo di vita di un oggetto." caption-side="bottom"}
{: http}

**Esempio di codice da utilizzare con l'SDK COS NodeJS**
{: javascript}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);
var date = new Date('June 16, 2019 00:00:00');

var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* required */
        Expiration:
        {
          Date: date
        }
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

**Esempio di codice da utilizzare con l'SDK COS Python**
{: python}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
{: python}

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'Status': 'Enabled',
                'Filter': {},
                'Expiration':
                {
                    'Days': 123
                },
            },
        ]
    }
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

**Esempio di codice da utilizzare con l'SDK COS Java**
{: java}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Define a rule for exiring items in a bucket
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);
            
            // Add the rule to a new BucketLifecycleConfiguration.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));
            
            // Use the client to set the LifecycleConfiguration on the bucket.
            _cosClient.setBucketLifecycleConfiguration(bucketName, configuration);   
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
    }
```
{: codeblock}
{: java}
{: caption="Esempio 1. Esempi di codice che mostrano la creazione della configurazione del ciclo di vita." caption-side="bottom"}

### Esamina la configurazione del ciclo di vita di un bucket, inclusa la scadenza
{: #expiry-api-view}

Questa implementazione dell'operazione `GET` utilizza il parametro di query `lifecycle` per esaminare le impostazioni di ciclo di vita per il bucket. Verrà restituita una risposta `404` HTTP se non sono presenti configurazioni del ciclo di vita.
{: http}

Gli utenti Cloud IAM devono disporre del ruolo di lettore (`Reader`) per rimuovere una politica del ciclo di vita da un bucket.

Gli utenti dell'infrastruttura classica devono disporre almeno delle autorizzazioni di lettura (`Read`) per il bucket per rimuovere una politica del ciclo di vita da un bucket.

Intestazione              | Tipo   |Descrizione
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Stringa | **Obbligatorio**: l'hash MD5 a 128 bit con codifica base64 del payload, utilizzato come un controllo dell'integrità per garantire che il payload non è stato modificato in transito. 
{: http}

**Sintassi**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Esempio 5. Osserva l'uso delle barre e dei punti in questo esempio di sintassi." caption-side="bottom"}
{: codeblock}
{: http}

**Richiesta dell'intestazione di esempio**
{: http}

```yaml
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: caption="Esempio 6. Esempi di intestazione della richiesta per la creazione della configurazione del ciclo di vita di un oggetto." caption-side="bottom"}
{: http}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.


```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').get_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
 

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";

            String storageClass = "us-south";
            String location = "us";

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            // Use the client to read the configuration
            BucketLifecycleConfiguration config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            
            System.out.println(config.toString());
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }
```
{: codeblock}
{: java}
{: caption="Esempio 2. Esempi di codice che mostrano il controllo della configurazione del ciclo di vita." caption-side="bottom"}

### Elimina la configurazione del ciclo di vita di un bucket, inclusa la scadenza
{: #expiry-api-delete}

Questa implementazione dell'operazione `DELETE` utilizza il parametro di query `lifecycle` per esaminare le impostazioni di ciclo di vita per il bucket. Tutte le regole del ciclo di vita associate al bucket verranno eliminate. Le transizioni definite dalle regole non verranno più eseguite per i nuovi oggetti. Tuttavia, le regole di transizione esistenti verranno conservate per gli oggetti che sono stati già scritti nel bucket prima dell'eliminazione delle regole. Le regole di scadenza non esisteranno più. Verrà restituita una risposta `404` HTTP se non sono presenti configurazioni del ciclo di vita.
{: http}

Gli utenti Cloud IAM devono disporre del ruolo di scrittore (`Writer`) per rimuovere una politica del ciclo di vita da un bucket.

Gli utenti dell'infrastruttura classica devono disporre almeno delle autorizzazioni di proprietario (`Owner`) per il bucket per rimuovere una politica del ciclo di vita da un bucket.

**Sintassi**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Esempio 7. Osserva l'uso delle barre e dei punti in questo esempio di sintassi." caption-side="bottom"}
{: codeblock}
{: http}

**Richiesta dell'intestazione di esempio**
{: http}

```yaml
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="Esempio 8. Esempi di intestazione della richiesta per la creazione della configurazione del ciclo di vita di un oggetto." caption-side="bottom"}
{: http}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
 

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').delete_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

L'utilizzo degli SDK {{site.data.keyword.cos_full}} richiede solo il richiamo delle funzioni appropriate con i parametri corretti e la configurazione appropriata.
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";

            String storageClass = "us-south";
            String location = "us";

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            // Delete the configuration.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);
            
            // Verify that the configuration has been deleted by attempting to retrieve it.
            config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            String s = (config == null) ? "Configuration has been deleted." : "Configuration still exists.";
            System.out.println(s);
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

    }
```
{: codeblock}
{: java}
{: caption="Esempio 3. Esempi di codice che mostrano l'eliminazione della configurazione del ciclo di vita." caption-side="bottom"}

## Passi successivi
{: #expiry-next-steps}

La scadenza è solo uno dei molti concetti del ciclo di vita disponibili per {{site.data.keyword.cos_full_notm}}.
Puoi esplorare in modo approfondito ognuno dei concetti che abbiamo trattato in questa panoramica nella
[Piattaforma {{site.data.keyword.cloud_notm}}](https://cloud.ibm.com/).

