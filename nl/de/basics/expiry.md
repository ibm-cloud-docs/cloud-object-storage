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

# Veraltete Daten mit Ablaufregeln löschen
{: #expiry}

Eine Ablaufregel dient zum Löschen von Objekten nach einem definierten Zeitraum, der mit dem Erstellungsdatum des Objekts beginnt. 

Sie können den Lebenszyklus für Objekte festlegen, indem Sie die Webkonsole, die REST-API und die Tools anderer Anbieter verwenden, die in {{site.data.keyword.cos_full_notm}} integriert sind. 

* Eine Ablaufregel kann zu einem neuen oder vorhandenen Bucket hinzugefügt werden.
* Eine vorhandene Ablaufregel kann geändert oder inaktiviert werden.
* Eine neu hinzugefügte oder geänderte Ablaufregel gilt für alle neuen und vorhandenen Objekte in dem Bucket.
* Zum Hinzufügen oder Ändern von Lebenszyklusrichtlinien ist die Rolle `Schreibberechtigter` erforderlich. 
* Pro Bucket können bis zu 1000 Lebenszyklusregeln (Archivierung + Ablauf) definiert werden.
* Es können bis zu 24 Stunden vergehen, bis Änderungen an den Ablaufregeln wirksam werden.
* Der Geltungsbereich jeder Ablaufregel kann eingeschränkt werden, indem ein optionaler Präfixfilter definiert wird, der nur auf eine Untergruppe von Objekten mit Namen angewendet werden soll, die mit dem Präfix übereinstimmen.
* Eine Ablaufregel ohne Präfixfilter gilt für alle Objekte in dem Bucket.
* Der Ablaufzeitraum eines Objekts, der als Anzahl von Tagen angegeben wird, wird ausgehend von dem Zeitpunkt berechnet, zu dem das Objekt erstellt wurde, und wird auf Mitternacht des nächsten Tages in UTC (Coordinated Universal Time; koordinierte Weltzeit) abgerundet. Beispiel: Wenn Sie über eine Ablaufregel für ein Bucket verfügen, mit der das Ablaufen der Gültigkeit einer Gruppe von Objekten auf ein Datum zehn Tage nach deren Erstellungsdatum festgelegt wird, dann läuft die Gültigkeit eines Objekts, das am 15. April 2019 um 05:10 UTC erstellt wurde, am 26. April 2019 00:00 UTC ab. 
* Die Ablaufregeln für alle Buckets werden einmal alle 24 Stunden bewertet. Jedes Objekt, dessen Gültigkeit (auf Basis des Ablaufdatums des betreffenden Objekts) abgelaufen ist, wird zum Löschen in die Warteschlange eingestellt. Die Löschung abgelaufener Objekte beginnt am folgenden Tag und dauert normalerweise weniger als 24 Stunden. Der Speicher, der diesen Objekten zugeordnet wurde, wird Ihnen nach der Löschung der Objekte nicht mehr berechnet.

Für Objekte, die der IOS-Aufbewahrungsrichtlinie (IOS = Immutable Object Storage; Speicher für unveränderliche Objekte) unterliegen, werden Ablaufaktionen verzögert, bis die Aufbewahrungsrichtlinie nicht mehr durchgesetzt wird.
{: important}

## Attribute für Ablaufregeln
{: #expiry-rules-attributes}

Jede Ablaufregel verfügt über die folgenden Attribute:

### ID
Die ID einer Regel muss innerhalb der Lebenszykluskonfiguration des Buckets eindeutig sein.

### Ablauf
Der Ablaufblock enthält die Details, die das automatische Löschen von Objekten steuern. Hierbei kann es sich um ein bestimmtes Datum in der Zukunft oder um einen Zeitraum handeln, der beginnt, nachdem neue Objekte geschrieben wurden.

### Präfix
Eine optionale Zeichenfolge, die mit dem Präfix des Objektnamens im Bucket abgeglichen wird. Eine Regel mit einem Präfix gilt nur für die Objekte, für die eine Übereinstimmung festgestellt wurde. Sie können mehrere Regeln für unterschiedliche Ablaufaktionen für unterschiedliche Präfixe innerhalb desselben Buckets verwenden. Innerhalb derselben Lebenszykluskonfiguration kann eine Regel beispielsweise zum Löschen aller Objekte, die mit der Zeichenfolge `logs/` beginnen, nach 30 Tagen dienen. Eine zweite Regel kann zum Löschen von Objekten, die mit `video/` beginnen, nach 365 Tagen dienen.  

### Status
Eine Regel kann entweder aktiviert oder inaktiviert werden. Eine Regel ist nur aktiv, wenn sie aktiviert wurde.

## Beispiele für Lebenszykluskonfigurationen

In dieser Konfiguration läuft die Gültigkeit neuer Objekte nach 30 Tagen ab.

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

In dieser Konfiguration werden alle Objekte mit dem Präfix `foo/` am 1. Juni 2020 gelöscht.

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

Sie können Übergangs- und Ablaufregeln auch kombinieren. In dieser Konfiguration werden alle Objekte 90 Tage nach ihrer Erstellung archiviert und es werden alle Objekte mit dem Präfix `foo/` nach 180 Tagen gelöscht.

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

## Konsole verwenden
{: #expiry-using-console}

Wenn Sie ein neues Bucket erstellen, dann aktivieren Sie das Kontrollkästchen **Ablaufregel hinzufügen**. Klicken Sie als Nächstes auf **Regel hinzufügen**, um die neue Ablaufregel zu erstellen. Sie können während der Erstellung eines Buckets bis zu fünf Regeln hinzufügen. Weitere Regeln können später hinzugefügt werden.

Wählen Sie für ein vorhandenes Bucket im Navigationsmenü die Option **Konfiguration** aus und klicken Sie dann im Abschnitt _Ablaufregel_ auf **Regel hinzufügen**.

## API und SDKs verwenden
{: #expiry-using-api-sdks}

Sie können Ablaufregeln über die REST-API oder die IBM COS-SDKs programmgesteuert verwalten. Wählen Sie das Format für die Beispiele aus, indem Sie eine Kategorie in der Kontextumschaltung auswählen.

### Ablaufregel zur Lebenszykluskonfiguration eines Buckets hinzufügen
{: #expiry-api-create}

**REST-API-Referenz**
{: http}

Bei dieser Implementierung der `PUT`-Operation wird der Abfrageparameter `lifecycle` verwendet, um die Lebenszykluseinstellungen für das Bucket festzulegen. Diese Operation ermöglicht die Verwendung einer einzigen Lebenszyklusrichtliniendefinition für ein Bucket. Die Richtlinie wird als Regelgruppe definiert, die die folgenden Parameter umfasst: `ID`, `Status`, `Filter` und `Expiration` (Ablauf).
{: http}
 
Cloud IAM-Benutzer müssen über die Rolle `Schreibberechtigter` verfügen, um eine Lebenszyklusrichtlinie aus einem Bucket zu entfernen.

Benutzer der klassischen Infrastruktur müssen mindestens über die Berechtigungen für `Eigner` für das Bucket verfügen, um eine Lebenszyklusrichtlinie aus einem Bucket zu entfernen.

Header                    | Typ    | Beschreibung
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Zeichenfolge | **Erforderlich**: Der Base64-codierte 128-Bit-MD5-Hashwert der Nutzdaten, der für die Integritätsprüfung verwendet wird, um sicherzustellen, dass die Nutzdaten während der Übertragung nicht geändert wurden. 
{: http}

Der Hauptteil der Anforderung muss einen XML-Block mit dem folgenden Schema enthalten:
{: http}

| Element                  | Typ                  | Untergeordnete Elemente                 | Vorfahre                 | Einschränkung                                                                              |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | Keiner                   | Grenzwert 1.                                                                              |
| `Rule`                   | Container            | `ID`, `Status`, `Filter`, `Expiration` | `LifecycleConfiguration` | Grenzwert 1000.                                                                              |
| `ID`                     | Zeichenfolge         | Keine                                  | `Rule`                   | Muss aus den Zeichen (`a-z,`A-Z,0-9`) und den folgenden Symbolen bestehen: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | Zeichenfolge         | `Prefix`                               | `Rule`                   | Muss ein Element `Prefix` enthalten                                                          |
| `Prefix`                 | Zeichenfolge         | Keine                                  | `Filter`                 | Die Regel gilt für alle Objekte mit Schlüsseln, die mit diesem Präfix übereinstimmen.                                                            |
| `Expiration`             | `Container`          | `Days` oder `Date`                       | `Rule`                   | Grenzwert 1.                                                                              |
| `Days`                   | Nicht negative Ganzzahl | Keine                                  | `Expiration`             |Muss ein Wert größer als '0' sein.|
| `Date`                   | Datum                | Keine                                  | `Expiration`             |Muss im ISO 8601-Format angegeben werden. |
{: http}

Der Hauptteil der Anforderung muss einen XML-Block mit dem Schema enthalten, auf das in der Tabelle (siehe Beispiel 1) verwiesen wird:
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
{: caption="Beispiel 1. XML-Beispiel aus Hauptteil der Anforderung." caption-side="bottom"}
{: http}

**Syntax**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # Pfadstil
PUT https://{bucket}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```
{: caption="Beispiel 2. Beachten Sie die Verwendung von Schrägstrichen und Punkten in diesem Syntaxbeispiel." caption-side="bottom"}
{: codeblock}
{: http}

**Beispielanforderung**
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
{: caption="Beispiel 3. Anforderungsheaderbeispiele für die Erstellung einer Objektlebenszykluskonfiguration." caption-side="bottom"}
{: http}

**Codebeispiel zur Verwendung des Node.js-COS-SDK**
{: javascript}

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
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
  Bucket: 'STRING_VALUE', /* erforderlich */
  LifecycleConfiguration: {
    Rules: [ /* erforderlich */
      {
        Status: 'Enabled', /* erforderlich */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* erforderlich */
        Expiration:
        {
          Date: date
        }
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}

**Codebeispiel zur Verwendung des Python-COS-SDK**
{: python}

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
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

**Codebeispiel zur Verwendung des Java-COS-SDK**
{: java}

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
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
            
            // Regel für Ablauf von Elementen in einem Bucket definieren
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);
            
            // Regel zu neuem Element BucketLifecycleConfiguration hinzufügen.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));
            
            // Client zum Festlegen von LifecycleConfiguration für das Bucket verwenden.
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
{: caption="Beispiel 1. Codebeispiele für Erstellung einer Lebenszykluskonfiguration." caption-side="bottom"}

### Lebenszykluskonfiguration eines Buckets (einschließlich Ablauf) überprüfen
{: #expiry-api-view}

Bei dieser Implementierung der `GET`-Operation wird der Abfrageparameter `lifecycle` verwendet, um die Lebenszykluseinstellungen für das Bucket zu überprüfen. Wenn keine Lebenszykluskonfiguration vorhanden ist, wird die HTTP-Antwort `404` zurückgegeben.
{: http}

Cloud IAM-Benutzer müssen über die Rolle `Leseberechtigter` verfügen, um eine Lebenszyklusrichtlinie aus einem Bucket zu entfernen.

Benutzer der klassischen Infrastruktur müssen über die Berechtigungen zum `Lesen` für das Bucket verfügen, um eine Lebenszyklusrichtlinie aus einem Bucket zu entfernen.

Header                    | Typ    | Beschreibung
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Zeichenfolge | **Erforderlich**: Der Base64-codierte 128-Bit-MD5-Hashwert der Nutzdaten, der für die Integritätsprüfung verwendet wird, um sicherzustellen, dass die Nutzdaten während der Übertragung nicht geändert wurden.
{: http}

**Syntax**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # Pfadstil
GET https://{bucket}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```
{: caption="Beispiel 5. Beachten Sie die Verwendung von Schrägstrichen und Punkten in diesem Syntaxbeispiel." caption-side="bottom"}
{: codeblock}
{: http}

**Beispielheaderanforderung**
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
{: caption="Beispiel 6. Anforderungsheaderbeispiele für die Erstellung einer Objektlebenszykluskonfiguration." caption-side="bottom"}
{: http}

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
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
  Bucket: 'STRING_VALUE' /* erforderlich */
};

s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.


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

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
 

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
            
            // Client zum Lesen der Konfiguration verwenden.
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
{: caption="Beispiel 2. Codebeispiele für Überprüfung einer Lebenszykluskonfiguration." caption-side="bottom"}

### Lebenszykluskonfiguration eines Buckets (einschließlich Ablauf) löschen
{: #expiry-api-delete}

Bei dieser Implementierung der `DELETE`-Operation wird der Abfrageparameter `lifecycle` verwendet, um die Lebenszykluseinstellungen für das Bucket zu überprüfen. Alle Lebenszyklusregeln, die dem Bucket zugeordnet sind, werden gelöscht. Übergänge, die anhand der Regeln definiert wurden, werden für neue Objekte nicht mehr angewendet. Vorhandene Übergangsregeln werden für Objekte beibehalten, die bereits in das Bucket geschrieben wurden, bevor die Regeln gelöscht wurden. Die Ablaufregeln sind nicht mehr vorhanden. Wenn keine Lebenszykluskonfiguration vorhanden ist, wird die HTTP-Antwort `404` zurückgegeben.
{: http}

Cloud IAM-Benutzer müssen über die Rolle `Schreibberechtigter` verfügen, um eine Lebenszyklusrichtlinie aus einem Bucket zu entfernen.

Benutzer der klassischen Infrastruktur müssen mindestens über die Berechtigungen für `Eigner` für das Bucket verfügen, um eine Lebenszyklusrichtlinie aus einem Bucket zu entfernen.

**Syntax**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # Pfadstil
DELETE https://{bucket}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```
{: caption="Beispiel 7. Beachten Sie die Verwendung von Schrägstrichen und Punkten in diesem Syntaxbeispiel." caption-side="bottom"}
{: codeblock}
{: http}

**Beispielheaderanforderung**
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
{: caption="Beispiel 8. Anforderungsheaderbeispiele für die Erstellung einer Objektlebenszykluskonfiguration." caption-side="bottom"}
{: http}

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
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
  Bucket: 'STRING_VALUE' /* erforderlich */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
 

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

Zur Nutzung der {{site.data.keyword.cos_full}}-SDKs ist lediglich ein Aufruf der entsprechenden Funktionen mit den korrekten Parametern und der richtigen Konfiguration erforderlich.
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
            
            // Konfiguration löschen.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);
            
            // Löschung der Konfiguration überprüfen, indem versucht wird, sie abzurufen.
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
{: caption="Beispiel 3. Codebeispiele für Löschung einer Lebenszykluskonfiguration." caption-side="bottom"}

## Nächste Schritte
{: #expiry-next-steps}

Der Ablauf der Gültigkeit ist nur eines der zahlreichen Lebenszykluskonzepte, die für {{site.data.keyword.cos_full_notm}} zur Verfügung stehen.
Mit jedem der in dieser Übersicht behandelten Konzepte können Sie sich in [{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/) weiter vertraut machen.

