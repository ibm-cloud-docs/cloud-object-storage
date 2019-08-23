---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: sdks, getting started, java

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
{:go: .ph data-hd-programlang='go'}

# Java verwenden
{: #java}

Das {{site.data.keyword.cos_full}}-SDK for Java ist umfassend und verfügt über Funktionen, die in diesem Handbuch nicht beschrieben sind. Die detaillierte Dokumentation zu Klassen und Methoden finden Sie in der [Java-Dokumentation](https://ibm.github.io/ibm-cos-sdk-java/). Den Quellcode finden Sie im [GitHub-Repository](https://github.com/ibm/ibm-cos-sdk-java).

## SDK abrufen
{: #java-install}

Die einfachste Methode zum Abrufen des Java-SDKs für {{site.data.keyword.cos_full_notm}} ist die Verwendung von Maven zum Verwalten von Abhängigkeiten. Wenn Sie mit Maven nicht vertraut sind, können Sie sich mithilfe des Handbuchs [Maven in 5 Minuten](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) einen ersten Überblick verschaffen.

Von Maven wird die Datei `pom.xml` zum Angeben der Bibliotheken (und der Versionen) verwendet, die für ein Java-Projekt erforderlich sind. Nachfolgend wird ein Beispiel für die `pom.xml` aufgeführt, mit der eine Verbindung vom Java-SDK von {{site.data.keyword.cos_full_notm}} zu {{site.data.keyword.cos_short}} hergestellt wird.


```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.0</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.8.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.3</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```
{:codeblock}

## Von Version 1.x.x migrieren
{: #java-migrate}

Mit Release 2.0 des SDKs wird eine Änderung der Namensbereichsfestlegung eingeführt, die es einer Anwendung ermöglicht, in derselben Anwendung oder Umgebung die ursprüngliche AWS-Bibliothek für den Verbindungsaufbau zu AWS-Ressourcen zu verwenden. Für die Migration von Version 1.x auf 2.0 sind einige Änderungen erforderlich:

1. Ändern Sie unter Verwendung von Maven alle Abhängigkeitsversionstags des Typs `ibm-cos-java-sdk` in der Datei 'pom.xml' in `2.0.0`. Stellen Sie sicher, dass in der Datei 'pom.xml' keine SDK-Modulabhängigkeiten vor Version `2.0.0` vorhanden sind.
2. Ändern Sie alle Importdeklaration von `amazonaws` in `ibm.cloud.objectstorage`.


## Client erstellen und Berechtigungsnachweise ableiten
{: #java-credentials}

Im folgenden Beispiel wird der Client `cos` erstellt und durch Bereitstellen der Berechtigungsnachweisinformationen (API-Schlüssel und Serviceinstanz-ID) konfiguriert. Diese Werte können auch automatisch aus einer Berechtigungsnachweisdatei oder aus Umgebungsvariablen abgeleitet werden.

Nach dem Generieren eines [Serviceberechtigungsnachweises](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) kann das daraus resultierende JSON-Dokument unter `~/.bluemix/cos_credentials` gespeichert werden. Vom SDK wird diese Datei automatisch als Quelle für die Berechtigungsnachweise verwendet, sofern während der Clienterstellung nicht explizit andere Berechtigungsnachweise festgelegt werden. Wenn die Datei `cos_credentials` HMAC-Schlüssel enthält, wird die Authentifizierung vom Client mithilfe einer Signatur durchgeführt; andernfalls wird vom Client für die Authentifizierung der bereitgestellte API-Schlüssel mit einem Bearer-Token verwendet.

Falls Sie eine Migration von AWS S3 durchführen, können Sie die Daten für die Berechtigungsnachweise aus `~/.aws/credentials` im folgenden Format verwenden:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Wenn sowohl `~/.bluemix/cos_credentials` als auch `~/.aws/credentials` vorhanden ist, genießt `cos_credentials` Vorrang.

 Weitere Details zur Clienterstellung finden Sie in der [Java-Dokumentation](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html).

## Codebeispiele
{: #java-examples}

Zunächst wird eine vollständige Beispielklasse aufgeführt, von der einige grundlegende Funktionen ausgeführt werden, danach folgen die einzelnen Klassen. Von der folgenden Klasse `CosExample` werden Objekte in einem vorhandenen Bucket aufgelistet, ein neues Bucket erstellt und anschließend alle Buckets in der Serviceinstanz aufgelistet. 

### Erforderliche Informationen erfassen
{: #java-examples-prereqs}

* `bucketName` und `newBucketName` sind [eindeutige und DNS-sichere](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket) Zeichenfolgen. Da Bucketnamen im gesamten System eindeutig sind, müssen diese Werte geändert werden, wenn dieses Beispiel mehrfach ausgeführt wird. Beachten Sie, dass die Namen noch zehn bis fünfzehn Minuten nach ihrer Löschung reserviert bleiben.
* `api_key` ist der Wert, der im [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) als `apikey` gefunden wurde.
* `service_instance_id` ist der Wert, der im [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) als `resource_instance_id` gefunden wurde. 
* `endpoint_url` ist eine Serviceendpunkt-URL, einschließlich des Protokolls `https://`. Dies ist **nicht** der Wert für `endpoints`, der im [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) gefunden wurde. Weitere Informationen zu Endpunkten finden Sie unter [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `storageClass` ist ein [gültiger Bereitstellungscode](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint), der dem Wert von `endpoint` entspricht. Dieser Wert wird anschließend für die S3-API-Variable `LocationConstraint` verwendet.
* Für `location` muss der Standortteil von `storageClass` festgelegt werden. Für `us-south-standard` ist dies `us-south`. Diese Variable wird nur für die Berechnung von [HMAC-Signaturen](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac)verwendet, ist aber für jeden Client erforderlich (auch in diesem Beispiel), von dem ein IAM-API-Schlüssel verwendet wird.

```java
    package com.cos;

    import java.sql.Timestamp;
    import java.util.List;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class CosExample
    {

        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {

            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";

            String bucketName = "<BUCKET_NAME>";  // eg my-unique-bucket-name
            String newBucketName = "<NEW_BUCKET_NAME>"; // eg my-other-unique-bucket-name
            String api_key = "<API_KEY>"; // eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
            String service_instance_id = "<SERVICE_INSTANCE_ID"; // eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud"; // this could be any service endpoint

            String storageClass = "us-south-standard";
            String location = "us";

            System.out.println("Current time: " + new Timestamp(System.currentTimeMillis()).toString());
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            listObjects(bucketName, _cosClient);
            createBucket(newBucketName, _cosClient, storageClass);
            listBuckets(_cosClient);
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

        /**
         * @param bucketName
         * @param cosClient
         */
        public static void listObjects(String bucketName, AmazonS3 cosClient)
        {
            System.out.println("Listing objects in bucket " + bucketName);
            ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println();
        }

        /**
         * @param bucketName
         * @param cosClient
         * @param storageClass
         */
        public static void createBucket(String bucketName, AmazonS3 cosClient, String storageClass)
        {
            cosClient.createBucket(bucketName, storageClass);
        }

        /**
         * @param cosClient
         */
        public static void listBuckets(AmazonS3 cosClient)
        {
            System.out.println("Listing buckets");
            final List<Bucket> bucketList = _cosClient.listBuckets();
            for (final Bucket bucket : bucketList) {
                System.out.println(bucket.getName());
            }
            System.out.println();
        }

    }

```
{:codeblock}

### Konfiguration initialisieren
{: #java-examples-config}

```java
private static String COS_ENDPOINT = "<endpoint>"; // eg "https://s3.us.cloud-object-storage.appdomain.cloud"
private static String COS_API_KEY_ID = "<api-key>"; // eg "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ"
private static String COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
private static String COS_SERVICE_CRN = "<resource-instance-id>"; // "crn:v1:bluemix:public:iam-identity::a/3ag0e9402tyfd5d29761c3e97696b71n::serviceid:ServiceId-540a4a41-7322-4fdd-a9e7-e0cb7ab760f9"
private static String COS_BUCKET_LOCATION = "<location>"; // eg "us"

public static void main(String[] args)
{
    SDKGlobalConfiguration.IAM_ENDPOINT = COS_AUTH_ENDPOINT;

    try {
        _cos = createClient(COS_API_KEY_ID, COS_SERVICE_CRN, COS_ENDPOINT, COS_BUCKET_LOCATION);
    } catch (SdkClientException sdke) {
        System.out.printf("SDK Error: %s\n", sdke.getMessage());
    } catch (Exception e) {
        System.out.printf("Error: %s\n", e.getMessage());
    }
}

public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
{
    AWSCredentials credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);
    ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
    clientConfig.setUseTcpKeepAlive(true);

    AmazonS3 cos = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
            .withClientConfiguration(clientConfig).build();

    return cos;
}
```

*Schlüsselwerte*
* `<endpoint>` - Öffentlicher Endpunkt für Cloud Object Storage (verfügbar über das [IBM Cloud-Dashboard](https://cloud.ibm.com/resources){:new_window}). Weitere Informationen zu Endpunkten finden Sie unter [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - API-Schlüssel, der beim Erstellen der Serviceberechtigungsnachweise generiert wird (Schreibzugriff für Erstellungs- und Löschbeispiele erforderlich)
* `<resource-instance-id>` - Ressourcen-ID für Cloud Object Storage (verfügbar über [IBM Cloud-Befehlszeilenschnittstelle](/docs//docs/cli?topic=cloud-cli-idt-cli) oder [IBM Cloud-Dashboard](https://cloud.ibm.com/resources){:new_window})
* `<location>` - Standardstandort für Cloud Object Storage (muss mit der Region übereinstimmen, die für `<endpoint>` verwendet wird)

*SDK-Referenzen*
* Klassen
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### Endpunkt bestimmen
{: #java-examples-endpoint}

Die folgenden Methoden können verwendet werden, um den Serviceendpunkt basierend auf Bucketstandort, Endpunkttyp (öffentlich oder privat) und einer bestimmten Region (optional) zu bestimmen. Weitere Informationen zu Endpunkten finden Sie unter [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```java
/**
* Gibt Serviceendpunkt auf der Basis des Standorts der
* Speicherklasse (z. B. us-standard, us-south-standard)
* und des Endpunkttyps (öffentlich oder privat) zurück
*/
public static String getEndpoint(String location, String endPointType) {
    return getEndpoint(location, "", endPointType);
}

/**
* Gibt den Serviceendpunkt auf der Basis der Speicherklassenposition
* (z. B. us-standard, us-south-standard), bei Bedarf einer bestimmten Region
* (z. B. sanjose, amsterdam) - nur verwenden, wenn ein bestimmter regionaler
* Endpunkt gewünscht wird - und des Enpunttyps (öffentlich oder privat) zurück
*/
public static String getEndpoint(String location, String region, String endpointType) {
    HashMap locationMap = new HashMap<String, String>();
    locationMap.put("us", "s3-api.us-geo");
    locationMap.put("us-dallas", "s3-api.dal-us-geo");
    locationMap.put("us-sanjose", "s3-api.sjc-us-geo");
    locationMap.put("us-washington", "s3-api.wdc-us-geo");
    locationMap.put("us-south", "s3.us-south");
    locationMap.put("us-east", "s3.us-east");
    locationMap.put("eu", "s3.eu-geo");
    locationMap.put("eu-amsterdam", "s3.ams-eu-geo");
    locationMap.put("eu-frankfurt", "s3.fra-eu-geo");
    locationMap.put("eu-milan", "s3.mil-eu-geo");
    locationMap.put("eu-gb", "s3.eu-gb");
    locationMap.put("eu-germany", "s3.eu-de");
    locationMap.put("ap", "s3.ap-geo");
    locationMap.put("ap-tokyo", "s3.tok-ap-geo");
    locationMap.put("ap-seoul", "s3.seo-ap-geo");
    locationMap.put("ap-hongkong", "s3.hkg-ap-geo");
    locationMap.put("che01", "s3.che01");
    locationMap.put("mel01", "s3.mel01");
    locationMap.put("tor01", "s3.tor01");

    String key = location.substring(0, location.lastIndexOf("-")) + (region != null && !region.isEmpty() ? "-" + region : "");
    String endpoint = locationMap.getOrDefault(key, null).toString();

    if (endpoint != null) {
        if (endpointType.toLowerCase() == "private")
            endpoint += ".objectstorage.service.networklayer.com";
        else
            endpoint += ".objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net";
    }

    return endpoint;
}
```

### Neues Bucket erstellen
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### Bucket mit abweichender Speicherklasse erstellen
{: #java-examples-storage-class}

Auf eine Liste gültiger Bereitstellungscodes für `LocationConstraint` kann im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes) verwiesen werden.

```java
cos.createBucket("sample", "us-vault"); // der Name des Buckets und die Speicherklasse (LocationConstraint)
```

*SDK-Referenzen*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### Neue Textdatei erstellen
{: #java-examples-text-file}

```java
public static void createTextFile(String bucketName, String itemName, String fileText) {
    System.out.printf("Creating new item: %s\n", itemName);

    InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileText.length());

    PutObjectRequest req = new PutObjectRequest(bucketName, itemName, newStream, metadata);
    _cos.putObject(req);

    System.out.printf("Item: %s created!\n", itemName);
}
```
Beachten Sie, dass beim Hinzufügen von angepassten Metadaten zu einem Objekt mit dem SDK das Objekt `ObjectMetadata` erstellt werden muss und nicht manuell ein angepasster Header mit `x-amz-meta-{` gesendet werden darf. Die letztere Aktion kann Probleme beim Authentifizieren mithilfe von HMAC-Berechtigungsnachweisen verursachen.
{: .tip}

### Objekt aus Datei hochladen
{: #java-examples-upload}

Im folgenden Beispiel wird davon ausgegangen, dass das Bucket `sample` bereits vorhanden ist.

```java
cos.putObject(
    "sample", // der Name des Zielbuckets
    "myfile", // der Objektschlüssel
    new File("/home/user/test.txt") // der Dateiname und Pfad des Objekts, das hochgeladen werden soll
);
```

### Objekt per Streaming hochladen
{: #java-examples-stream}

Im folgenden Beispiel wird davon ausgegangen, dass das Bucket `sample` bereits vorhanden ist.

```java
String obj = "An example"; // das Objekt, das gespeichert werden soll
ByteArrayOutputStream theBytes = new ByteArrayOutputStream(); // neuen Ausgabedatenstrom zum Speicher der Objektdaten erstellen
ObjectOutputStream serializer = new ObjectOutputStream(theBytes); // Objektdaten festlegen, die serialisiert werden sollen
serializer.writeObject(obj); // Objektdaten serialisieren
serializer.flush();
serializer.close();
InputStream stream = new ByteArrayInputStream(theBytes.toByteArray()); // serialisierte Daten in neuen Eingabedatenstrom zum Speichern konvertieren
ObjectMetadata metadata = new ObjectMetadata(); // Metadaten definieren
metadata.setContentType("application/x-java-serialized-object"); // Metadaten festlegen
metadata.setContentLength(theBytes.size()); // Metadaten für Länge des Datenstroms festlegen
cos.putObject(
    "sample", // der Name des Buckets, in das das Objekt geschrieben wird
    "serialized-object", // der Name das Objekts, das geschrieben wird
    stream, // der Name des Datenstroms, von dem das Objekt geschrieben wird
    metadata // die Metadaten für das Objekt, das geschrieben wird
);
```

### Objekt in Datei herunterladen
{: #java-examples-download}

Im folgenden Beispiel wird davon ausgegangen, dass das Bucket `sample` bereits vorhanden ist.

```java
GetObjectRequest request = new // neue Anforderung zum Abrufen eines Objekts erstellen
GetObjectRequest( // neues Objekt anfordern durch Ermitteln
    "sample", // des Namens des Buckets
    "myFile" // des Namens des Objekts
);

s3Client.getObject( // Inhalt des Objekts schreiben
    request, // Anforderung verwenden, die soeben erstellt wurde
    new File("retrieved.txt") // in eine neue Datei schreiben
);
```


### Objekt per Streaming herunterladen
{: #java-examples-download-stream}

Im folgenden Beispiel wird davon ausgegangen, dass das Bucket `sample` bereits vorhanden ist.

```java
S3Object returned = cos.getObject( // Objekt anfordern durch Ermitteln
    "sample", // des Namens des Buckets
    "serialized-object" // des Namens des serialisierten Objekts
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // Objektdatenstrom festlegen
```

### Objekte kopieren
{: #java-examples-copy}

```java
// Objekt mit demselben Bucket kopieren
cos.copyObject( // Objekt kopieren, weitergegeben wird...
    "sample",  // der Name des Buckets, in dem das Objekt gespeichert ist, das kopiert werden soll,
    "myFile.txt",  // der Name des Objekts, das aus dem Quellenbucket kopiert wird,
    "sample",  // der Name des Buckets, in dem das Objekt gespeichert ist, das kopiert werden soll
    "myFile.txt.backup"    // und der neue Name der Kopie des Objekts, das kopiert werden soll
);
```

```java
// Objekt zwischen zwei Buckets kopieren
cos.copyObject( // Objekt kopieren, weitergegeben wird...
    "sample", // der Name des Buckets, aus dem das Objekt kopiert wird,
    "myFile.txt", // der Name des Objekts, das aus dem Quellenbucket kopiert wird,
    "backup", // der Name des Buckets, in das das Objekt kopiert wird
    "myFile.txt" // und der Name des kopierten Objekts im Zielbucket
);
```

*SDK-Referenzen*
* Klassen
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* Methoden
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### Ausnahmebedingung bei 'putObject'
{: #java-examples-put-exception}

Von der Methode 'putObject' kann die folgende Ausnahmebedingung auch dann ausgelöst werden, wenn das Hochladen des neuen Objekts erfolgreich war.
```
Exception in thread "main" java.lang.NoClassDefFoundError: javax/xml/bind/JAXBException
	at com.ibm.cloud.objectstorage.services.s3.AmazonS3Client.putObject(AmazonS3Client.java:1597)
	at ibmcos.CoSExample.createTextFile(CoSExample.java:174)
	at ibmcos.CoSExample.main(CoSExample.java:65)
Caused by: java.lang.ClassNotFoundException: javax.xml.bind.JAXBException
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:582)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:190)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:499)
	... 3 more
```

**Ursache:** Die JAXB-APIs werden als Java EE-APIs betrachtet und sind nicht mehr im Standardklassenpfad in Java SE 9 enthalten.

**Korrektur:** Fügen Sie den folgenden Eintrag zur Datei 'pom.xml' im Projektordner hinzu und packen Sie das Projekt erneut.
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### Verfügbare Buckets auflisten
{: #java-examples-list-buckets}

```java
public static void getBuckets() {
    System.out.println("Retrieving list of buckets");

    final List<Bucket> bucketList = _cos.listBuckets();
    for (final Bucket bucket : bucketList) {
        System.out.printf("Bucket Name: %s\n", bucket.getName());
    }
}
```
*SDK-Referenzen*
* Klassen
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* Methoden
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### Elemente in Bucket auflisten (v2)
{: #java-examples-list-objects-v2}

Das Objekt [AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} enthält eine aktualisierte Methode zum Auflisten des Inhalts ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}). Mit dieser Methode können Sie die Anzahl der zurückgegebenen Datensätze begrenzen und die Datensätze stapelweise abrufen. Dies kann beim Paging der Ergebnisse in einer Anwendung nützlich sein und die Leistung verbessern.

```java
public static void getBucketContentsV2(String bucketName, int maxKeys) {
    System.out.printf("Retrieving bucket contents (V2) from: %s\n", bucketName);

    boolean moreResults = true;
    String nextToken = "";

    while (moreResults) {
        ListObjectsV2Request request = new ListObjectsV2Request()
            .withBucketName(bucketName)
            .withMaxKeys(maxKeys)
            .withContinuationToken(nextToken);

        ListObjectsV2Result result = _cos.listObjectsV2(request);
        for(S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            System.out.printf("Item: %s (%s bytes)\n", objectSummary.getKey(), objectSummary.getSize());
        }

        if (result.isTruncated()) {
            nextToken = result.getNextContinuationToken();
            System.out.println("...More results in next batch!\n");
        }
        else {
            nextToken = "";
            moreResults = false;
        }
    }
    System.out.println("...No more results!");
}
```

*SDK-Referenzen*
* Klassen
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Methoden
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### Elemente in Bucket auflisten (v1)
{: #java-examples-list-objects}

```java
public static void getBucketContents(String bucketName) {
    System.out.printf("Retrieving bucket contents from: %s\n", bucketName);

    ObjectListing objectListing = _cos.listObjects(new ListObjectsRequest().withBucketName(bucketName));
    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        System.out.printf("Item: %s (%s bytes)\n", objectSummary.getKey(), objectSummary.getSize());
    }
}
```

*SDK-Referenzen*
* Klassen
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Methoden
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### Dateiinhalt eines bestimmten Elements abrufen
{: #java-examples-get-contents}

```java
public static void getItem(String bucketName, String itemName) {
    System.out.printf("Retrieving item from bucket: %s, key: %s\n", bucketName, itemName);

    S3Object item = _cos.getObject(new GetObjectRequest(bucketName, itemName));

    try {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        InputStreamReader in = new InputStreamReader(item.getObjectContent());

        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }

        System.out.println(out.toString());
    } catch (IOException ioe){
        System.out.printf("Error reading file %s: %s\n", name, ioe.getMessage());
    }
}
```

*SDK-Referenzen*
* Klassen
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* Methoden
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### Element in Bucket löschen
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*SDK-Referenzen*
* Methoden
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### Mehrere Elemente in Bucket löschen
{: #java-examples-delete-objects}

Eine Löschanforderung kann maximal 1000 Schlüssel enthalten, die gelöscht werden sollen. Dies ist zwar sehr nützlich, um den Aufwand pro Anforderung zu reduzieren, gehen Sie beim Löschen einer großen Anzahl an Schlüsseln jedoch mit Sorgfalt vor. Berücksichtigen Sie auch die Größen der Objekte, um eine geeignete Leistung zu gewährleisten.{:tip}

```java
public static void deleteItems(String bucketName) {
    DeleteObjectsRequest req = new DeleteObjectsRequest(bucketName);
    req.withKeys(
        "deletetest/testfile1.txt",
        "deletetest/testfile2.txt",
        "deletetest/testfile3.txt",
        "deletetest/testfile4.txt",
        "deletetest/testfile5.txt"
    );

    DeleteObjectsResult res = _cos.deleteObjects(req);

    System.out.printf("Deleted items for %s\n", bucketName);

    List<DeleteObjectsResult.DeletedObject> deletedItems = res.getDeletedObjects();
    for(DeleteObjectsResult.DeletedObject deletedItem : deletedItems) {
        System.out.printf("Deleted item: %s\n", deletedItem.getKey());
    }
}
```

*SDK-Referenzen*
* Klassen
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* Methoden
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### Bucket löschen
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*SDK-Referenzen*
* Methoden
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### Öffentliche Lesbarkeit eines Objekts überprüfen
{: #java-examples-public-check}

```java
public static void getItemACL(String bucketName, String itemName) {
    System.out.printf("Retrieving ACL for %s from bucket: %s\n", itemName, bucketName);

    AccessControlList acl = _cos.getObjectAcl(bucketName, itemName);

    List<Grant> grants = acl.getGrantsAsList();

    for (Grant grant : grants) {
        System.out.printf("User: %s (%s)\n", grant.getGrantee().getIdentifier(), grant.getPermission().toString());
    }
}
```

*SDK-Referenzen*
* Klassen
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* Methoden 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### Mehrteiliges Hochladen ausführen
{: #java-examples-multipart-object}

```java
public static void multiPartUpload(String bucketName, String itemName, String filePath) {
    File file = new File(filePath);
    if (!file.isFile()) {
        System.out.printf("The file '%s' does not exist or is not accessible.\n", filePath);
        return;
    }

    System.out.printf("Starting multi-part upload for %s to bucket: %s\n", itemName, bucketName);

    InitiateMultipartUploadResult mpResult = _cos.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, itemName));
    String uploadID = mpResult.getUploadId();

    // Mit Hochladen der Teile beginnen
    // Mindestgröße der Teile ist 5 MB
    long partSize = 1024 * 1024 * 5;
    long fileSize = file.length();
    long partCount = ((long)Math.ceil(fileSize / partSize)) + 1;
    List<PartETag> dataPacks = new ArrayList<PartETag>();

    try {
        long position = 0;
        for (int partNum = 1; position < fileSize; partNum++) {
            partSize = Math.min(partSize, (fileSize - position));

            System.out.printf("Uploading to %s (part %s of %s)\n", name, partNum, partCount);

            UploadPartRequest upRequest = new UploadPartRequest()
                    .withBucketName(bucketName)
                    .withKey(itemName)
                    .withUploadId(uploadID)
                    .withPartNumber(partNum)
                    .withFileOffset(position)
                    .withFile(file)
                    .withPartSize(partSize);

            UploadPartResult upResult = _cos.uploadPart(upRequest);
            dataPacks.add(upResult.getPartETag());

            position += partSize;
        }

        // Upload abgeschlossen
        _cos.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, itemName, uploadID, dataPacks));
        System.out.printf("Upload for %s Complete!\n", itemName);
    } catch (SdkClientException sdke) {
        System.out.printf("Multi-part upload aborted for %s\n", itemName);
        System.out.printf("Upload Error: %s\n", sdke.getMessage());
        _cos.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
    }
}
```
*SDK-Referenzen*
* Klassen
    * [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){:new_window}
    * [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}
    * [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){:new_window}
    * [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){:new_window}

* Methoden
    * [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){:new_window}
    * [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){:new_window}
    * [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){:new_window}
    * [uploadPart](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){:new_window}

## Größere Objekte mit einem Übertragungsmanager hochladen
{: #java-examples-transfer-manager}

`TransferManager` erleichtert das Übertragen großer Dateien durch das automatische Integrieren mehrteiliger Uploads, wenn das Einstellen von Konfigurationsparametern erforderlich ist.

```java
public static void largeObjectUpload(String bucketName, String itemName, String filePath) throws IOException, InterruptedException {
    File uploadFile = new File(filePath);

    if (!uploadFile.isFile()) {
        System.out.printf("The file '%s' does not exist or is not accessible.\n", filePath);
        return;
    }

    System.out.println("Starting large file upload with TransferManager");

    // für Teilgröße 5 MB festlegen
    long partSize = 1024 * 1024 * 5;

    // für Schwellenwert 5 MB festlegen
    long thresholdSize = 1024 * 1024 * 5;

    String endPoint = getEndpoint(COS_BUCKET_LOCATION, "public");
    AmazonS3 s3client = createClient(COS_API_KEY_ID, COS_SERVICE_CRN, endPoint, COS_BUCKET_LOCATION);

    TransferManager transferManager = TransferManagerBuilder.standard()
        .withS3Client(s3client)
        .withMinimumUploadPartSize(partSize)
        .withMultipartCopyThreshold(thresholdSize)
        .build();

    try {
        Upload lrgUpload = transferManager.upload(bucketName, itemName, uploadFile);

        lrgUpload.waitForCompletion();

        System.out.println("Large file upload complete!");
    }
    catch (SdkClientException e) {
        System.out.printf("Upload error: %s\n", e.getMessage());
    }
    finally {
        transferManager.shutdownNow();
    }
```

*SDK-Referenzen*
* Klassen
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* Methoden
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## Key Protect verwenden
{: #java-examples-kp}
Key Protect kann zu einem Speicherbucket hinzugefügt werden, um sensible ruhende Daten in der Cloud zu verschlüsseln.

### Vorbereitende Schritte
{: #java-examples-kp-prereqs}

Damit Sie ein Bucket erstellen können, während Key Protect aktiviert ist, müssen die folgenden Voraussetzungen erfüllt sein:

* Ein Key Protect-Service wird [bereitgestellt](/docs/services/key-protect?topic=key-protect-provision#provision)
* Ein Stammschlüssel ist verfügbar (entweder [generiert](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) oder [importiert](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Cloudressourcenname für Stammschlüssel abrufen
{: #java-examples-kp-root}

1. Rufen Sie die [Instanz-ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) für den Key Protect-Service ab.
2. Verwenden Sie die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api), um alle [verfügbaren Schlüssel](https://cloud.ibm.com/apidocs/key-protect) abzurufen.
    * Sie können entweder `curl`-Befehle oder einen API-REST-Client wie zum Beispiel [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) für den Zugriff auf die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) verwenden.
3. Rufen Sie den Cloudressourcennamen des Stammschlüssels ab, mit dem Sie Key Protect für das Bucket aktivieren. Der Cloudressourcenname ähnelt der folgenden Zeichenfolge:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Bucket während Aktivierung von Key Protect erstellen
{: #java-examples-kp-bucket}

```java
private static String COS_KP_ALGORITHM = "<algorithm>";
private static String COS_KP_ROOTKEY_CRN = "<root-key-crn>";

public static void createBucketKP(String bucketName) {
    System.out.printf("Creating new encrypted bucket: %s\n", bucketName);

    EncryptionType encType = new EncryptionType();
    encType.setKmsEncryptionAlgorithm(COS_KP_ALGORITHM);
    encType.setIBMSSEKMSCustomerRootKeyCrn(COS_KP_ROOTKEY_CRN);

    CreateBucketRequest req = new CreateBucketRequest(bucketName).withEncryptionType(encType);

    _cos.createBucket(req);

    System.out.printf("Bucket: %s created!", bucketName);
}
```
*Schlüsselwerte*
* `<algorithm>` - Der Verschlüsselungsalgorithmus, der für neue Objekte verwendet wird, die zum Bucket hinzugefügt wurden (Standard ist AES256).
* `<root-key-crn>` - Der Cloudressourcenname des Stammschlüssels, der vom Key Protect-Service abgerufen wird.

*SDK-Referenzen*
* Klassen
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* Methoden
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Neue Header für Key Protect
{: #java-examples-kp-headers}

Die zusätzlichen Header wurden in der Klasse `Headers` definiert:

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

In demselben Abschnitt der Implementierung z um Erstellen von Buckets, in der bereits IAM-Serviceinstanzheader hinzugefügt werden, werden zwei neue Verschlüsselungsheader hinzugefügt:

```java
// IBM Serviceinstanz-ID & Verschlüsslung zu Header hinzufügen
if ((null != this.awsCredentialsProvider ) && (this.awsCredentialsProvider.getCredentials() instanceof IBMOAuthCredentials)) {
    IBMOAuthCredentials oAuthCreds = (IBMOAuthCredentials)this.awsCredentialsProvider.getCredentials();
    if (oAuthCreds.getServiceInstanceId() != null) {
        request.addHeader(Headers.IBM_SERVICE_INSTANCE_ID, oAuthCreds.getServiceInstanceId());
        request.addHeader(Headers.IBM_SSE_KP_ENCRYPTION_ALGORITHM, createBucketRequest.getEncryptionType().getKpEncryptionAlgorithm());
        request.addHeader(Headers.IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN, createBucketRequest.getEncryptionType().getIBMSSEKPCustomerRootKeyCrn());
    }
}
```

Die Objekte `ObjectListing` und `HeadBucketResult` wurden so aktualisiert, dass die boolesche Variablen `IBMSSEKPEnabled` & die Zeichenfolgenvariable `IBMSSEKPCustomerRootKeyCrn` mit Getter- & Setter-Methoden eingeschlossen sind. Auf diese Art werden die Werte der neuen Header gespeichert.

#### GET-Bucket
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

Für die Klasse `ObjectListing` sind zwei weitere Methoden erforderlich:

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

Die zusätzlichen Header wurden in der Klasse `Headers` definiert:

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

'S3XmlResponseHandler' ist für das Unmarshalling aller XML-Antworten zuständig. In einer neu hinzugefügten Prüfung wird festgestellt, ob das Ergebnis eine Instanz `ObjectListing` ist und die abgerufenen Header zum Objekt `ObjectListing` hinzugefügt werden:

```java
if (result instanceof ObjectListing) {
    if (!StringUtils.isNullOrEmpty(responseHeaders.get(Headers.IBM_SSE_KP_ENABLED)){
            ((ObjectListing) result).setIBMSSEKPEnabled(Boolean.parseBoolean(responseHeaders.get(Headers.IBM_SSE_KP_ENABLED)));
        }
    if (!StringUtils.isNullOrEmpty(responseHeaders.get(Headers.IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN))) {
            ((ObjectListing) result).setIBMSSEKPCrk(responseHeaders.get(Headers.IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));
        }
}
```

#### HEAD-Bucket
{: #java-examples-kp-head}
Die zusätzlichen Header wurden in der Klasse 'Headers' definiert:

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

Diese Variablen werden im 'HeadBucketResponseHandler' gefüllt.

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Aspera-Hochgeschwindigkeitsübertragung verwenden
{: #java-examples-aspera}

Durch die Installation der [Bibliothek für die Aspera-Hochgeschwindigkeitsübertragung](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging) können Sie Hochgeschwindigkeitsdateiübertragungen innerhalb der Anwendung verwenden. Die Aspera-Bibliothek ist keine Open Source-Bibliothek und somit eine optionale Abhängigkeit für das COS-SDK (von dem eine Apache-Lizenz verwendet wird). 

Während jeder Sitzung einer Aspera-Hochgeschwindigkeitsübertragung wird ein einzelner `ascp`-Prozess generiert, der auf der Clientmaschine zur Durchführung der Übertragung ausgeführt wird. Stellen Sie sicher, dass dieser Prozess in Ihrer Systemumgebung ausgeführt werden kann.
{:tip}

Zum Initialisieren von `AsperaTransferManager` benötigen Sie Instanzen des S3-Clients und Klassen des IAM-Token-Managers. `s3Client` ist erforderlich, um FASP-Verbindungsinformationen für das COS-Zielbucket abzurufen. `tokenManager` ist erforderlich, damit das SDK für die Aspera-Hochgeschwindigkeitsübertragung am COS-Zielbucket authentifiziert werden kann.

### `AsperaTransferManager` initialisieren
{: #java-examples-aspera-init}

Stellen Sie vor der Initialisierung von `AsperaTransferManager` sicher, dass Sie mit den Objekten [`s3Client`](#java-examples-config) und [`tokenManager`](#java-examples-config) arbeiten. 

Der Vorteil ist im Vergleich zu einer einzelnen Sitzung der Aspera-Hochgeschwindigkeitsübertragung ist nicht groß, wenn Sie nicht erwarten, dass erhebliche Störgrößen und Paketverluste im Netz auftreten. Daher muss `AsperaTransferManager` für die Verwendung mehrerer Sitzungen mithilfe der Klasse `AsperaConfig` eingerichtet werden. Dadurch wird die Übertragung in eine Anzahl paralleler **Sitzungen** aufgeteilt, in denen Datenblöcke gesendet werden, deren Größe durch den Wert für **Schwellenwert** definiert ist.

Die typische Konfiguration für die Verwendung von Mehrfachsitzungen muss folgende Merkmale aufweisen:
* 2.500 MB/s Zielrate
* 100 MB Schwellenwert (*dies ist der empfohlene Wert für die meisten Anwendungen*)

```java
AsperaTransferManagerConfig transferConfig = new AsperaTransferManagerConfig()
    .withMultiSession(true);

AsperaConfig asperaConfig = new AsperaConfig()
    .withTargetRateMbps(2500L)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaTransferManagerConfig(transferConfig)
    .withAsperaConfig(asperaConfig)
    .build();
```
Im obigen Beispiel werden vom SDK ausreichend Sitzungen zum Erreichen der Zielrate von 2.500 MB/s generiert.

Alternativ dazu kann das Sitzungsmanagement im SDK explizit konfiguriert werden. Dies ist in den Fällen nützlich, in denen eine genauere Kontrolle über die Netzauslastung gewünscht wird.

Die typische Konfiguration für die Verwendung expliziter Mehrfachsitzungen muss folgende Merkmale aufweisen:
* 2 oder 10 Sitzungen
* 100 MB Schwellenwert (*dies ist der empfohlene Wert für die meisten Anwendungen*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

Verwenden Sie zur Optimierung der Leistung in den meisten Szenarios immer mehrere Sitzungen, um den Aufwand zu minimieren, der mit der Instanziierung der Aspera-Hochgeschwindigkeitsübertragung verbunden ist. **Falls die Netzkapazität mindestens 1 Gb/s beträgt, sollten Sie 10 Sitzungen verwenden.** In Netzen mit niedrigerer Bandbreite sollten zwei Sitzungen verwendet werden.
{:tip}

*Schlüsselwerte*
* `API_KEY` - Ein API-Schlüssel für eine Benutzer- oder Service-ID mit der Rolle 'Schreibberechtigter' oder 'Manager'

Für die Erstellung von `AsperaTransferManager` müssen Sie einen IAM-API-Schlüssel angeben. [HMAC-Berechtigungsnachweise](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} werden derzeit **NICHT** unterstützt. Weitere Informationen zu IAM finden Sie [hier](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

### Dateiupload
{: #java-examples-aspera-upload}

```java
String filePath = "<absolute-path-to-source-data>";
String bucketName = "<bucket-name>";
String itemName = "<item-name>";

// Datei laden
File inputFile = new File(filePath);

// AsperaTransferManager für FASP-Upload erstellen
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client).build();

// Testdatei hochladen und Fortschritt melden
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.upload(bucketName, itemName, inputFile);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-source-data>` - Verzeichnis und Dateiname zum Hochladen zu Object Storage.
* `<item-name>` - Name des neuen Objekts, das zum Bucket hinzugefügt wird.

### Dateidownload
{: #java-examples-aspera-download}

```java
String bucketName = "<bucket-name>";
String outputPath = "<absolute-path-to-file>";
String itemName = "<item-name>";

// Lokale Datei erstellen
File outputFile = new File(outputPath);
outputFile.createNewFile();

// AsperaTransferManager für FASP-Download erstellen
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Datei herunterladen
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.download(bucketName, itemName, outputFile);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-file>` - Verzeichnis und Dateiname zum Speichern von Object Storage.
* `<item-name>` - Name des Objekts im Bucket.

### Verzeichnisupload
{: #java-examples-aspera-upload-directory}

```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

// Verzeichnis laden
File inputDirectory = new File(directoryPath);

// AsperaTransferManager für FASP-Upload erstellen
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Testverzeichnis hochladen
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.uploadDirectory(bucketName, directoryPrefix, inputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-directory>` - Verzeichnis der Dateien, die zu Object Storage hochgeladen werden sollen.
* `<virtual-directory-prefix>` - Name des Verzeichnispräfixes, das beim Upload zu jeder Datei hinzugefügt werden soll. Verwenden Sie eine Nullzeichenfolge oder eine leere Zeichenfolge, um die Dateien in das Bucketstammverzeichnis hochzuladen.

### Verzeichnisdownload
{: #java-examples-aspera-download-directory}
```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

// Verzeichnis laden
File outputDirectory = new File(directoryPath);

// AsperaTransferManager für FASP-Download erstellen
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Testverzeichnis herunterladen
Future<AsperaTransaction> asperaTransactionFuture   = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-directory>` - Verzeichnis zum Speichern der von Object Storage heruntergeladenen Dateien.
* `<virtual-directory-prefix>` - Name des Verzeichnispräfixes jeder Datei, die heruntergeladen werden soll. Verwenden Sie eine Nullzeichenfolge oder eine leere Zeichenfolge, um alle Dateien in das Bucket herunterzuladen.

### Sitzungskonfiguration abhängig von Übertragung überschreiben
{: #java-examples-aspera-config}

Sie können die Werte für die Mehrfachsitzungskonfiguration je nach Übertragung überschreiben; übergeben Sie hierzu eine Instanz von `AsperaConfig` für die überladenen Methoden für Upload und Download. Mit `AsperaConfig` können Sie die Anzahl der Sitzungen und den Schwellenwert für die Mindestdateigröße pro Sitzung angeben. 

```java
String bucketName = "<bucket-name>";
String filePath = "<absolute-path-to-file>";
String itemName = "<item-name>";

// Datei laden
File inputFile = new File(filePath);

// AsperaTransferManager für FASP-Upload erstellen
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
.withTokenManager(TOKEN_MANAGER)
.withAsperaConfig(asperaConfig)
.build();

// AsperaConfig zum Festlegen der Anzahl der Sitzungen und
// des Dateischwellenwerts pro Sitzung festlegen.
AsperaConfig asperaConfig = new AsperaConfig().
withMultiSession(10).
withMultiSessionThresholdMb(100);

// Testdatei hochladen und Fortschritt melden
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.upload(bucketName, itemName, inputFile, asperaConfig, null);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

### Übertragungsfortschritt überwachen
{: #java-examples-aspera-monitor}

Die einfachste Möglichkeit, den Fortschritt der Datei- bzw. Verzeichnisübertragungen zu überwachen ist die Verwendung der Eigenschaft `isDone()`, von der `true` zurückgegeben wird, wenn die Übertragung abgeschlossen ist.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    // 3 Sekunden anhalten
    Thread.sleep(1000 * 3);
}
```

Sie können auch überprüfen, ob eine Übertragung zur Verarbeitung in die Warteschlange gestellt wird; rufen Sie hierzu die Methode `onQueue` für `AsperaTransaction` ab. Von `onQueue` wird der boolesche Wert `true` zurückgegeben, wenn die Übertragung in die Warteschlange eingereiht wurde.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    // 3 Sekunden anhalten
    Thread.sleep(1000 * 3);
}
```

Wenn Sie überprüfen möchten, ob eine Übertragung in Bearbeitung ist, rufen Sie die Fortschrittsmethode in `AsperaTransaction`auf.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    // 3 Sekunden anhalten
    Thread.sleep(1000 * 3);
}
```

Jeder Übertragung ist standardmäßig ein Wert für `TransferProgress` zugeordnet. Von `TransferProgress` wird die Anzahl der übertragenen Byte und der Prozentsatz der übertragenen Byte im Vergleich zur Gesamtsumme der zu übertragenden Byte angegeben. Wenn Sie auf die Angaben von `TransferProgress` einer Übertragung zugreifen möchten, verwenden Sie die Methode `getProgress` in `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    // 3 Sekunden anhalten
    Thread.sleep(1000 * 3);
}
```

Wenn Sie die Anzahl der übertragenen Byte auflisten möchten, rufen Sie die Methode `getBytesTransferred` für `TransferProgress` auf. Wenn Sie den Prozentsatz der übertragenen Byte im Vergleich zur Gesamtsumme der zu übertragenen Byte auflisten möchten, rufen Sie die Methode `getPercentTransferred` für `TransferProgress` auf.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    System.out.println("Bytes transferred: " + transferProgress.getBytesTransferred());
    System.out.println("Percent transferred: " + transferProgress.getPercentTransferred());


    // 3 Sekunden anhalten
    Thread.sleep(1000 * 3);
}
```

### Anhalten/Wiederaufnehmen/Abbrechen
{: #java-examples-aspera-pause}

Das SDK bietet die Möglichkeit, den Fortschritt der Datei- bzw. Verzeichnisübertragungen über die folgenden Methoden des Objekts `AsperaTransfer` zu verwalten:

* `pause()`
* `resume()`
* `cancel()`

In Bezug auf das Aufrufen der oben aufgeführten Methoden gibt es keine Nebeneffekte. Vom SDK wird die ordnungsgemäße Bereinigung und Verwaltung gewährleistet.
{:tip}

Am folgenden Beispiel wird eine mögliche Verwendung dieser Methoden veranschaulicht:

```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, _cos)
    .withTokenManager(TOKEN_MANAGER)
    .build();

File outputDirectory = new File(directoryName);

System.out.println("Starting directory download...");

// Verzeichnis vom Cloudspeicher herunterladen
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

int pauseCount = 0;

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download in progress...");

    // Übertragung anhalten
    asperaTransfer.pause();

    // Übertragung wiederaufnehmen
    asperaTransfer.resume();

    // Übertragung abbrechen
    asperaTransfer.cancel();
}

System.out.println("Directory download complete!");
```

### Fehlerbehebung bei Aspera-Problemen
{: #java-examples-aspera-ts}

**Problem:** Wenn Entwickler das Oracle-JDK unter Linux oder Mac OS X verwenden, können bei Übertragungen unerwartete Abstürze und Abstürze im Hintergrund auftreten.

**Ursache:** Für den nativen Code sind die eigenen Signalhandler erforderlich, was dazu führen kann, dass die Signalhandler der JVM überschrieben werden. Es kann erforderlich sein, die Funktion für die Signalverkettung der JVM zu verwenden.

Benutzer des *IBM&reg; JDK oder Benutzer von Microsoft&reg; Windows sind nicht betroffen.*

**Lösung:** Stellen Sie eine Verbindung zur Bibliothek für die JVM-Signalverkettung her und laden Sie sie.
* Suchen Sie unter Linux die gemeinsam genutzte Bibliothek `libjsig.so` und legen Sie die folgenden Umgebungsvariable fest:
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* Suchen Sie unter Mac OS die gemeinsam genutzte Bibliothek `libjsig.dylib` und legen Sie die folgenden Umgebungsvariablen fest:
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

Weitere Informationen zur Signalverkettung finden Sie in der [Dokumentation zum Oracle&reg;-JDK](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window}.

**Problem:**`UnsatisfiedLinkError` unter Linux

**Ursache:** Vom System können abhängige Bibliotheken nicht geladen werden. In den Anwendungsprotokollen werden Fehler wie die folgenden aufgeführt:

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**Lösung:** Legen Sie die folgende Umgebungsvariable fest:

`LD_LIBRARY_PATH=<JAVA_HOME>/jre/lib/amd64/server:<JAVA_HOME>/jre/lib/amd64`

<!--
## S3 API compatibility reference

This list summarizes the AWS Java SDK methods that are supported by {{site.data.keyword.cos_full_notm}}. More detailed documentation on individual classes and methods can be found in the [the Javadoc](https://ibm.github.io/ibm-cos-sdk-java/)

```java
abortMultipartUpload(AbortMultipartUploadRequest request)
completeMultipartUpload(CompleteMultipartUploadRequest request)
copyObject(CopyObjectRequest copyObjectRequest)
copyObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey)
copyPart(CopyPartRequest copyPartRequest)
createBucket(CreateBucketRequest createBucketRequest)
createBucket(String bucketName)
createBucket(String bucketName, String LocationConstraint)
deleteBucket(DeleteBucketRequest deleteBucketRequest)
deleteBucket(String bucketName)
deleteBucketCrossOriginConfiguration(DeleteBucketCrossOriginConfigurationRequest deleteBucketCrossOriginConfigurationRequest)
deleteBucketCrossOriginConfiguration(String bucketName)
deleteObject(DeleteObjectRequest deleteObjectRequest)
deleteObject(String bucketName, String key)
deleteObjects(DeleteObjectsRequest deleteObjectsRequest)
doesBucketExist(String bucketName)
generatePresignedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest)
generatePresignedUrl(String bucketName, String key, Date expiration)
generatePresignedUrl(String bucketName, String key, Date expiration, HttpMethod method)
getBucketAcl(GetBucketAclRequest getBucketAclRequest)
getBucketAcl(String bucketName)
getBucketCrossOriginConfiguration(String bucketName)
getBucketLocation(GetBucketLocationRequest getBucketLocationRequest)
getBucketLocation(String bucketName)
getCachedResponseMetadata(AmazonWebServiceRequest request)
getObject(GetObjectRequest getObjectRequest)
getObject(GetObjectRequest getObjectRequest, File destinationFile)
getObject(String bucketName, String key)
getObjectAcl(String bucketName, String key)
getObjectAcl(String bucketName, String key, String versionId)
getObjectMetadata(GetObjectMetadataRequest getObjectMetadataRequest)
getObjectMetadata(String bucketName, String key)
initiateMultipartUpload(InitiateMultipartUploadRequest request)
listBuckets()
listBuckets(ListBucketsRequest listBucketsRequest)
listMultipartUploads(ListMultipartUploadsRequest request)
listNextBatchOfObjects(ObjectListing previousObjectListing)
listNextBatchOfVersions(VersionListing previousVersionListing)
listObjects(String bucketName)
listObjects(String bucketName, String prefix)
listObjects(ListObjectsRequest listObjectsRequest)
listParts(ListPartsRequest request)
putObject(String bucketName, String key, File file)
putObject(String bucketName, String key, InputStream input, ObjectMetadata metadata)
putObject(PutObjectRequest putObjectRequest)
setBucketAcl(String bucketName, AccessControlList acl)
setBucketAcl(String bucketName, CannedAccessControlList acl)
setBucketAcl(SetBucketAclRequest setBucketAclRequest)
setBucketCrossOriginConfiguration(String bucketName, BucketCrossOriginConfiguration bucketCrossOriginConfiguration)
setBucketCrossOriginConfiguration(SetBucketCrossOriginConfigurationRequest setBucketCrossOriginConfigurationRequest)
setEndpoint(String endpoint)
setObjectAcl(String bucketName, String key, AccessControlList acl)
setObjectAcl(String bucketName, String key, CannedAccessControlList acl)
setObjectAcl(String bucketName, String key, String versionId, AccessControlList acl)
setObjectAcl(String bucketName, String key, String versionId, CannedAccessControlList acl)
setObjectAcl(SetObjectAclRequest setObjectAclRequest)
setS3ClientOptions(S3ClientOptions clientOptions)
uploadPart(UploadPartRequest request)
```
-->
## Metadaten aktualisieren
{: #java-examples-metadata}
Zum Aktualisieren der Metadaten für ein vorhandenes Objekt stehen zwei Methoden zur Verfügung:
* Die Anforderung `PUT` mit den neuen Metadaten und dem Inhalt des ursprünglichen Objekts
* Die Ausführung der Anforderung `COPY` mit den neuen Metadaten, von denen das ursprüngliche Objekt als Kopierquelle angegeben wird

### PUT zum Aktualisieren der Metadaten verwenden
{: #java-examples-metadata-put}

**Hinweis:** Von der Anforderung `PUT` wird der vorhandene Inhalt des Objekts überschrieben; deswegen muss es zuerst heruntergeladen und danach mit den neuen Metadaten erneut hochgeladen werden.

```java
public static void updateMetadataPut(String bucketName, String itemName, String key, String value) throws IOException {
    System.out.printf("Updating metadata for item: %s\n", itemName);

    // Vorhandenes Element zum Erneuten Laden des Inhalts abrufen
    S3Object item = _cos.getObject(new GetObjectRequest(bucketName, itemName));
    S3ObjectInputStream itemContents = item.getObjectContent();

    // Inhalt des Elements zum Festlegen der Inhaltslänge und Erstellen einer Kopie lesen
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    int b;
    while ((b = itemContents.read()) != -1) {
        output.write(b);
    }

    int contentLength = output.size();
    InputStream itemCopy = new ByteArrayInputStream(output.toByteArray());

    // neue Metadaten festlegen
    HashMap<String, String> userMetadata = new HashMap<String, String>();
    userMetadata.put(key, value);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(contentLength);
    metadata.setUserMetadata(userMetadata);

    PutObjectRequest req = new PutObjectRequest(bucketName, itemName, itemCopy, metadata);

    _cos.putObject(req);

    System.out.printf("Updated metadata for item %s from bucket %s\n", itemName, bucketName);
}
```

### COPY zum Aktualisieren der Metadaten verwenden
{: #java-examples-metadata-copy}

```java
public static void updateMetadataCopy(String bucketName, String itemName, String key, String value) {
    System.out.printf("Updating metadata for item: %s\n", itemName);

    // neue Metadaten festlegen
    HashMap<String, String> userMetadata = new HashMap<String, String>();
    userMetadata.put(key, value);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setUserMetadata(userMetadata);

    // Eigene Position als Kopierquelle festlegen
    CopyObjectRequest req = new CopyObjectRequest(bucketName, itemName, bucketName, itemName);
    req.setNewObjectMetadata(metadata);

    _cos.copyObject(req);

    System.out.printf("Updated metadata for item %s from bucket %s\n", itemName, bucketName);
}
```

## Unveränderlichen Objektspeicher verwenden
{: #java-examples-immutable}

### Schutzkonfiguration für vorhandenes Bucket hinzufügen
{: #java-examples-immutable-enable}

Von dieser Implementierung der Operation `PUT` wird der Abfrageparameter `protection` verwendet, um die Aufbewahrungsparameter für ein vorhandenes Bucket festzulegen. Mit dieser Operation können Sie Mindestwerte, Standardwerte und Maximalwerte für die Aufbewahrungsdauer festlegen. Mit dieser Operation können Sie auch den Schutzstatus des Buckets ändern. 

Objekte, die in ein geschütztes Bucket geschrieben wurden, können erst gelöscht werden, wenn die Schutzdauer abgelaufen ist und alle gesetzlichen Bestimmungen zum Schutz des Objekts entfernt wurden. Einem Objekt wird der Standardaufbewahrungswert des Buckets zugewiesen, sofern für das Objekt bei seiner Erstellung nicht ein objektspezifischer Wert angegeben wurde. Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden. 

Die unterstützten Minimal- und Maximalwerte für die Einstellung der Aufbewahrungsdauer (`MinimumRetention`, `DefaultRetention` und `MaximumRetention`) sind jeweils 0 Tage und 365.243 Tage (1.000 Jahre). 

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

### Schutz für Bucket überprüfen
{: #java-examples-immutable-check}

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

### Geschütztes Objekt hochladen
{: #java-examples-immutable-upload}

Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden.

| Wert | Typ	| Beschreibung |
| --- | --- | --- | 
|`Retention-Period` | Nicht negative Ganzzahl (Sekunden) | Aufbewahrungsdauer zum Speichern für das Objekt in Sekunden. Das Objekt kann so lange weder überschrieben noch gelöscht werden, bis die für die Aufbewahrungsdauer angegebene Zeit abgelaufen ist. Wenn dieses Feld und `Retention-Expiration-Date` angegeben werden, wird der Fehler `400` zurückgegeben. Wenn für beide Felder keine Werte angegeben werden, wird der Wert für den Zeitraum `DefaultRetention` des Buckets verwendet. Null (`0`) ist ein gültiger Wert und bedeutet, dass die Mindestaufbewahrungsdauer des Buckets `0` beträgt. |
| `Retention-expiration-date` | Datum (ISO-8601-Format) | Datum, an dem das Löschen oder Ändern des Objekts zulässig ist. Sie können nur diese Angabe oder den Header 'Retention-Period' angeben. Wenn beide angegeben werden, wird der Fehler `400` zurückgegeben. Wenn für beide Felder keine Werte angegeben werden, wird der Wert für den Zeitraum 'DefaultRetention' des Buckets verwendet. |
| `Retention-legal-hold-id` | Zeichenfolge | Eine einzelne gesetzliche Bestimmung (zum Beispiel eine Aufbewahrungspflicht), die auf das Objekt angewendet wird. Eine gesetzliche Bestimmung ist eine Zeichenfolge, die Y Zeichen lang ist. Das Objekt kann erst überschrieben oder gelöscht werden, wenn alle dem Objekt zugeordneten gesetzlichen Bestimmungen entfernt wurden. |

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

### Gesetzliche Bestimmung zu geschütztem Objekt hinzufügen oder aus geschütztem Objekt entfernen
{: #java-examples-immutable-legal-hold}

Von einem Objekt können 100 gesetzliche Bestimmungen unterstützt werden:

*  Eine Kennung für eine gesetzliche Bestimmung ist eine Zeichenfolge aus maximal 64 Zeichen und muss mindestens aus einem Zeichen bestehen. Gültige Zeichen sind Buchstaben, Ziffern, `!`, `_`, `.`, `*`, `(`, `)`, `-` und `.
* Wenn versucht wird, einem Objekt mehr 100 gesetzliche Bestimmungen hinzuzufügen, wird die neue gesetzliche Bestimmung nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung zu lang ist, wird sie dem Objekt nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung ungültige Zeichen enthält, wird sie dem Objekt nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung bereits für ein Objekt verwendet wird, wird die vorhandene gesetzliche Bestimmung nicht geändert; außerdem wird Fehler `409` mit der Antwort zurückgegeben, dass die Kennung bereits verwendet wird.
* Wenn ein Objekt keine Metadaten für die Aufbewahrungsdauer aufweist, wird der Fehler `400` zurückgegeben und das Hinzufügen oder Entfernen einer gesetzlichen Bestimmung ist nicht zulässig.

Das Vorhandensein eines Headers für die Aufbewahrungsdauer ist erforderlich; andernfalls wird der Fehler `400` zurückgegeben.
{: http}

Der Benutzer, der gesetzliche Bestimmungen hinzufügt oder entfernt, muss über die Berechtigung `Manager` für dieses Bucket verfügen.

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

### Aufbewahrungsdauer eines geschützten Objekts erweitern
{: #java-examples-immutable-extend}

Die Aufbewahrungsdauer eines Objekts kann nur verlängert werden. Ein derzeit konfigurierter Wert kann nicht verringert werden.

Der Wert zur Verlängerung der Aufbewahrungsdauer kann auf drei Arten festgelegt werden:

* Zusätzliche Zeit auf der Basis des aktuellen Werts (`Additional-Retention-Period` oder eine ähnliche Methode)
* Neuer Erweiterungszeitraum in Sekunden (`Extend-Retention-From-Current-Time` oder eine ähnliche Methode)
* Neues Ablaufdatum für Aufbewahrungszeitraum des Objekts (`New-Retention-Expiration-Date` oder eine ähnliche Methode)

Der aktuelle Aufbewahrungszeitraum, der in den Objektmetadaten gespeichert ist, wird entweder durch die angegebene zusätzliche Zeit verlängert oder durch den neuen Wert ersetzt; dies hängt von dem Parameter ab, der in der Anforderung `extendRetention` festgelegt wird. In allen Fällen wird der Parameter für die Verlängerung des Aufbewahrungszeitraums mit den Angaben für den aktuellen Aufbewahrungszeitraum verglichen; der erweiterte Parameter wird nur akzeptiert, wenn der aktualisierte Aufbewahrungszeitraum größer ist als der aktuelle Aufbewahrungszeitraum.

Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden.

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

### Gesetzliche Bestimmungen für geschütztes Objekt auflisten
{: #java-examples-immutable-list-holds}

Nach dem  Ausführen dieser Operation wird Folgendes zurückgegeben:

* Datum der Objekterstellung
* Aufbewahrungsdauer des Objekts in Sekunden
* Berechnetes Ablaufdatum der Aufbewahrungsdauer auf Basis des Zeitraums und des Erstellungsdatums
* Liste der gesetzlichen Bestimmungen
* Kennung der gesetzlichen Bestimmung
* Zeitmarke der Anwendung der gesetzlichen Bestimmung

Wenn auf ein Objekt keine gesetzlichen Bestimmungen angewendet werden, wird `LegalHoldSet` leer zurückgegeben. Wenn für ein Objekt keine Aufbewahrungsdauer angegeben wurde, wird der Fehler `404` zurückgegeben.

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
