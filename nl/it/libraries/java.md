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

# Utilizzo di Java
{: #java}

L'SDK {{site.data.keyword.cos_full}} per Java è completo e presenta funzioni non descritte in questa guida. Per una documentazione dettagliata di classi e metodi, [consulta la documentazione di Java](https://ibm.github.io/ibm-cos-sdk-java/). Il codice sorgente è disponibile nel [repository GitHub](https://github.com/ibm/ibm-cos-sdk-java).

## Ottenimento dell'SDK
{: #java-install}

Il modo più facile per utilizzare l'SDK {{site.data.keyword.cos_full_notm}} Java consiste nell'utilizzare Maven per gestire le dipendenze. Se non hai dimestichezza con Maven, puoi diventare operativo utilizzando la guida [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).

Maven utilizza un file denominato `pom.xml` per specificare le librerie (e le relative versioni) necessarie per un progetto Java. Ecco un file `pom.xml` di esempio per utilizzare l'SDK {{site.data.keyword.cos_full_notm}} Java per stabilire una connessione a {{site.data.keyword.cos_short}}.


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

## Migrazione dalla 1.x.x
{: #java-migrate}

La release 2.0 dell'SDK introduce una modifica della spaziatura dei nomi che consente a un'applicazione di utilizzare la libreria AWS originale per stabilire una connessione alle risorse AWS all'interno della stessa applicazione o dello stesso ambiente. Per eseguire la migrazione dalla 1.x alla 2.0, sono necessarie alcune modifiche.

1. Aggiorna utilizzando Maven modificando tutte le tag di versione della dipendenza `ibm-cos-java-sdk` in `2.0.0` nel pom.xml. Verifica che non ci sia alcuna dipendenza di modulo SDK nel pom.xml con una versione antecedente alla `2.0.0`.
2. Aggiorna qualsiasi dichiarazione di importazione da `amazonaws` a `ibm.cloud.objectstorage`.


## Creazione di un client e derivazione delle credenziali
{: #java-credentials}

Nel seguente esempio,un client `cos` viene creato e configurato fornendo informazioni di credenziali )chiave API e ID istanza del servizio). Questi valori possono anche essere derivati automaticamente da un file di credenziali o dalle variabili di ambiente.

Dopo aver generato una [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), il documento JSON risultante può essere salvato in `~/.bluemix/cos_credentials`. L'SDK deriverà automaticamente le credenziali da questo file, a meno che non vengano esplicitamente impostate altre credenziali durante la creazione del client. Se il file `cos_credentials` contiene chiavi HMAC, il client eseguirà l'autenticazione con una firma; altrimenti, il client utilizzerà la chiave API fornita per eseguire l'autenticazione utilizzando un token di connessione.

Se si esegue la migrazione da AWS S3, puoi anche derivare i dati delle credenziali da `~/.aws/credentials` nel formato:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Se esistono sia `~/.bluemix/cos_credentials` che `~/.aws/credentials`, `cos_credentials` avrà la preferenza.

 Per ulteriori dettagli sulla creazione del client, [vedi la documentazione Java](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html).

## Esempi di codice
{: #java-examples}

Iniziamo con una classe di esempio completa che verrà eseguita mediante della funzionalità di base ed esploriamo quindi le classi singolarmente. Questa classe `CosExample` elencherà gli oggetti in un bucket esistente, creerà un nuovo bucket ed elencherà quindi tutti i bucket nell'istanza del servizio. 

### Raccogli le informazioni richieste
{: #java-examples-prereqs}

* `bucketName` e `newBucketName` sono stringhe [univoche e indipendenti da DNS](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Poiché i nomi bucket sono univoci in tutto il sistema, questi valori dovranno essere modificati se questo esempio viene eseguito più volte. Nota che i nomi sono riservati per 10-15 minuti dopo l'eliminazione.
* `api_key` è il valore disponibile nella [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) come `apikey`.
* `service_instance_id` è il valore disponibile nella [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) come `resource_instance_id`. 
* `endpoint_url` è un URL di endpoint del servizio, inclusivo del protocollo `https://`. Questo **non** è il valore `endpoints` disponibile nella [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `storageClass` è un [codice di provisioning valido](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) che corrisponde al valore `endpoint`. Questo viene quindi utilizzato come la variabile API S3 `LocationConstraint`.
* `location` deve essere impostata sulla parte di ubicazione della `storageClass`. Per `us-south-standard`, sarà `us-south`. Questa variabile viene utilizzata solo per il calcolo delle [firme HMAC](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac), ma è obbligatoria per qualsiasi client, compreso questo esempio che utilizza una chiave API IAM.

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

### Inizializzazione della configurazione
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

*Valori chiave*
* `<endpoint>` - endpoint pubblico per la tua archiviazione oggetti cloud (disponibile dal [dashboard IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - chiave api generata quando si creano le credenziali del servizio (l'accesso in scrittura è necessario per gli esempi di creazione ed eliminazione).
* `<resource-instance-id>` - ID risorsa per la tua archiviazione oggetti cloud (disponibile tramite la [API IBM Cloud](/docs//docs/cli?topic=cloud-cli-idt-cli) o il [dashboard IBM Cloud](https://cloud.ibm.com/resources){:new_window})
* `<location>` - ubicazione predefinita per la tua archiviazione oggetti cloud (deve corrispondere alla regione utilizzata per `<endpoint>`)

*Riferimenti SDK*
* Classi
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### Determinazione dell'endpoint
{: #java-examples-endpoint}

I metodi di seguito possono essere utilizzati per determinare l'endpoint del servizio in base all'ubicazione del bucket, al tipo di endpoint (pubblico o privato) e alla specifica regione (facoltativo). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```java
/**
* Returns a service endpoint based on the
* storage class location (i.e. us-standard, us-south-standard),
* endpoint type (public or private)
*/
public static String getEndpoint(String location, String endPointType) {
    return getEndpoint(location, "", endPointType);
}

/**
* Returns a service endpoint based on the
* storage class location (i.e. us-standard, us-south-standard),
* specific region if desired (i.e. sanjose, amsterdam) - only use if you want a specific regional endpoint,
* endpoint type (public or private)
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

### Creazione di un nuovo bucket
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### Crea un bucket con una classe di archiviazione differente
{: #java-examples-storage-class}

È possibile che si faccia riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida alle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*Riferimenti SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### Creazione di un nuovo file di testo
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
Nota: quando aggiungi metadati personalizzati a un oggetto, è necessario creare un oggetto `ObjectMetadata` utilizzando l'SDK e non inviare manualmente un'intestazione personalizzata contenente `x-amz-meta-{key}`. Quest'ultima operazione può causare dei problemi quando si esegue l'autenticazione utilizzando credenziali HMAC.
{: .tip}

### Carica oggetto da un file
{: #java-examples-upload}

Questo esempio presume che il bucket `sample` esista già.

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### Carica oggetto utilizzando un flusso
{: #java-examples-stream}

Questo esempio presume che il bucket `sample` esista già.

```java
String obj = "An example"; // the object to be stored
ByteArrayOutputStream theBytes = new ByteArrayOutputStream(); // create a new output stream to store the object data
ObjectOutputStream serializer = new ObjectOutputStream(theBytes); // set the object data to be serialized
serializer.writeObject(obj); // serialize the object data
serializer.flush();
serializer.close();
InputStream stream = new ByteArrayInputStream(theBytes.toByteArray()); // convert the serialized data to a new input stream to store
ObjectMetadata metadata = new ObjectMetadata(); // define the metadata
metadata.setContentType("application/x-java-serialized-object"); // set the metadata
metadata.setContentLength(theBytes.size()); // set metadata for the length of the data stream
cos.putObject(
    "sample", // the name of the bucket to which the object is being written
    "serialized-object", // the name of the object being written
    stream, // the name of the data stream writing the object
    metadata // the metadata for the object being written
);
```

### Scarica oggetto in un file
{: #java-examples-download}

Questo esempio presume che il bucket `sample` esista già.

```java
GetObjectRequest request = new // create a new request to get an object
GetObjectRequest( // request the new object by identifying
    "sample", // the name of the bucket
    "myFile" // the name of the object
);

s3Client.getObject( // write the contents of the object
    request, // using the request that was just created
    new File("retrieved.txt") // to write to a new file
);
```


### Scarica oggetto utilizzando un flusso
{: #java-examples-download-stream}

Questo esempio presume che il bucket `sample` esista già.

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### Copia oggetti
{: #java-examples-copy}

```java
// copy an object within the same Bucket
cos.copyObject( // copy the Object, passing…
    "sample",  // the name of the Bucket in which the Object to be copied is stored,
    "myFile.txt",  // the name of the Object being copied from the source Bucket,
    "sample",  // the name of the Bucket in which the Object to be copied is stored,
    "myFile.txt.backup"    // and the new name of the copy of the Object to be copied
);
```

```java
// copy an object between two Buckets
cos.copyObject( // copy the Object, passing…
    "sample", // the name of the Bucket from which the Object will be copied,
    "myFile.txt", // the name of the Object being copied from the source Bucket,
    "backup", // the name of the Bucket to which the Object will be copied,
    "myFile.txt" // and the name of the copied Object in the destination Bucket
);
```

*Riferimenti SDK*
* Classi
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* Metodi
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### Eccezione putObject
{: #java-examples-put-exception}

Il metodo putObject può generare la seguente eccezione anche se il nuovo caricamento oggetto è stato eseguito correttamente.
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

**Causa radice:** le API JAXB sono considerate API Java EE e non sono più contenute nel percorso classe predefinito in Java SE 9.

**Correzione:** aggiungi la seguente voce al file pom.xml nella tua cartella del progetto e riassembla il tuo progetto
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### Elenca i bucket disponibili
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
*Riferimenti SDK*
* Classi
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* Metodi
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### Elenca gli elementi in un bucket (v2)
{: #java-examples-list-objects-v2}

L'oggetto [AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} contiene un metodo aggiornato per elencare il contenuto ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}). Questo metodo ti consente di limitare il numero di record restituito e di richiamare i record in batch. Ciò potrebbe essere utile per la paginazione dei tuoi risultati all'interno di un'applicazione e per migliorare le prestazioni.

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

*Riferimenti SDK*
* Classi
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Metodi
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### Elenca gli elementi in un bucket (v1)
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

*Riferimenti SDK*
* Classi
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Metodi
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### Ottieni il contenuto del file di uno specifico elemento
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

*Riferimenti SDK*
* Classi
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* Metodi
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### Elimina un elemento da un bucket
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*Riferimenti SDK*
* Metodi
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### Elimina più elementi da un bucket
{: #java-examples-delete-objects}

La richiesta di eliminazione può contenere un massimo di 1000 chiavi che vuoi eliminare. Se da una parte ciò è molto utile per ridurre il sovraccarico per ogni richiesta, fai attenzione quando elimini un ampio numero di chiavi. Prendi in considerazione anche le dimensioni degli oggetti per garantire delle prestazioni adeguate.
{:tip}

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

*Riferimenti SDK*
* Classi
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* Metodi
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### Elimina un bucket
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*Riferimenti SDK*
* Metodi
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### Controlla se un oggetto è leggibile pubblicamente
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

*Riferimenti SDK*
* Classi
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* Metodi 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### Esegui un caricamento a più parti
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

    //begin uploading the parts
    //min 5MB part size
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

        //complete upload
        _cos.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, itemName, uploadID, dataPacks));
        System.out.printf("Upload for %s Complete!\n", itemName);
    } catch (SdkClientException sdke) {
        System.out.printf("Multi-part upload aborted for %s\n", itemName);
        System.out.printf("Upload Error: %s\n", sdke.getMessage());
        _cos.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
    }
}
```
*Riferimenti SDK*
* Classi
    * [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){:new_window}
    * [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}
    * [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){:new_window}
    * [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){:new_window}

* Metodi
    * [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){:new_window}
    * [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){:new_window}
    * [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){:new_window}
    * [uploadPart](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){:new_window}

## Carica oggetti più grandi utilizzando un gestore trasferimenti
{: #java-examples-transfer-manager}

`TransferManager` semplifica i trasferimenti di file di grandi dimensioni incorporando automaticamente dei caricamenti in più parti ogni qual volta è necessario impostando i parametri di configurazione.

```java
public static void largeObjectUpload(String bucketName, String itemName, String filePath) throws IOException, InterruptedException {
    File uploadFile = new File(filePath);

    if (!uploadFile.isFile()) {
        System.out.printf("The file '%s' does not exist or is not accessible.\n", filePath);
        return;
    }

    System.out.println("Starting large file upload with TransferManager");

    //set the part size to 5 MB
    long partSize = 1024 * 1024 * 5;

    //set the threshold size to 5 MB
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

*Riferimenti SDK*
* Classi
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* Metodi
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## Utilizzo di Key Protect
{: #java-examples-kp}
Key Protect può essere aggiunto a un bucket di archiviazione per crittografare dati sensibili inattivi nel cloud.

### Prima di cominciare
{: #java-examples-kp-prereqs}

I seguenti elementi sono necessari per creare un bucket con Key-Protect abilitato:

* Un servizio Key Protect [di cui è stato eseguito il provisioning](/docs/services/key-protect?topic=key-protect-provision#provision)
* Una chiave root disponibile ([generata](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importata](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Richiamo del CRN della chiave root
{: #java-examples-kp-root}

1. Richiama l'[ID istanza](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) per il tuo servizio Key Protect
2. Utilizza l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) per richiamare tutte le tue [chiavi disponibili](https://cloud.ibm.com/apidocs/key-protect)
    * Puoi utilizzare i comandi `curl` o un client REST API come [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) per accedere all'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Richiama il CRN della chiave root che utilizzerai per abilitare Key Protect sul tuo bucket. Il CRN sarà simile al seguente:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creazione di un bucket con key-protect abilitato
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
*Valori chiave*
* `<algorithm>` - l'algoritmo di crittografia utilizzato per i nuovi oggetti aggiunti al bucket (il valore predefinito è AES256).
* `<root-key-crn>` - CRN della chiave root ottenuta dal servizio Key Protect.

*Riferimenti SDK*
* Classi
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* Metodi
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Nuove intestazioni per Key Protect
{: #java-examples-kp-headers}

Nella classe `Headers` sono state definite delle intestazioni aggiuntive:

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

La stessa sezione dell'implementazione di creazione del bucket che già aggiunge le intestazioni dell'istanza del servizio IAM aggiungerà le 2 nuove intestazioni di crittografia:

```java
//Add IBM Service Instance Id & Encryption to headers
if ((null != this.awsCredentialsProvider ) && (this.awsCredentialsProvider.getCredentials() instanceof IBMOAuthCredentials)) {
    IBMOAuthCredentials oAuthCreds = (IBMOAuthCredentials)this.awsCredentialsProvider.getCredentials();
    if (oAuthCreds.getServiceInstanceId() != null) {
        request.addHeader(Headers.IBM_SERVICE_INSTANCE_ID, oAuthCreds.getServiceInstanceId());
        request.addHeader(Headers.IBM_SSE_KP_ENCRYPTION_ALGORITHM, createBucketRequest.getEncryptionType().getKpEncryptionAlgorithm());
        request.addHeader(Headers.IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN, createBucketRequest.getEncryptionType().getIBMSSEKPCustomerRootKeyCrn());
    }
}
```

Gli oggetti `ObjectListing` e `HeadBucketResult` sono stati aggiornati per include le variabili `IBMSSEKPEnabled` & String `IBMSSEKPCustomerRootKeyCrn` booleane con i metodi getter e setter. Memorizzeranno i valori delle nuove intestazioni.

#### Bucket GET
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

La classe `ObjectListing` richiederà 2 metodi aggiuntivi:

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

Le intestazioni aggiuntive sono state definite all'interno della classe `Headers`:

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

L'S3XmlResponseHandler che è responsabile dell'annullamento del marshalling di tutte le risposte xml. È stato aggiunto un controllo che il risultato sia un'istanza di `ObjectListing` e che le intestazioni richiamate verranno aggiunte all'oggetto `ObjectListing`:

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

#### Bucket HEAD
{: #java-examples-kp-head}
Le intestazioni aggiuntive sono state definite all'interno della classe Headers:

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

Queste variabili sono compilate nell'HeadBucketResponseHandler.

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Utilizzo del trasferimento ad alta velocità Aspera
{: #java-examples-aspera}

Installando la [libreria di trasferimento ad alta velocità Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), puoi utilizzare i trasferimenti di file ad alta velocità all'interno della tua applicazione. La libreria Aspera è closed-source e, pertanto, una dipendenza facoltativa per l'SDK COS (che utilizza una licenza Apache). 

Ogni sessione di trasferimento ad alta velocità Aspera genera un singolo processo `ascp` che viene eseguito sulla macchina client per eseguire il trasferimento. Assicurati che il tuo ambiente di calcolo possa consentire l'esecuzione di questo processo.
{:tip}

Ti serviranno delle istanze delle classi del client S3 e del gestore dei token IAM per inizializzare l'`AsperaTransferManager`. `s3Client` è necessario per ottenere le informazioni sulla connessione FASP per il bucket di destinazione COS. `tokenManager` è necessario per consentire all'SDK di trasferimento ad alta velocità Aspera di eseguire l'autenticazione presso il bucket di destinazione COS.

### Inizializzazione di `AsperaTransferManager`
{: #java-examples-aspera-init}

Prima di inizializzare `AsperaTransferManager`, assicurati di disporre di oggetti [`s3Client`](#java-examples-config) e [`tokenManager`](#java-examples-config) funzionanti. 

L'utilizzo di una singola sessione del trasferimento ad alta velocità Aspera non offre molti vantaggi, a meno che tu non preveda un rumore o una perdita di pacchetti notevoli nella rete. Dobbiamo quindi indicare a `AsperaTransferManager` di utilizzare più sessioni utilizzando la classe `AsperaConfig`. Questo suddividerà il trasferimento in diverse **sessioni** parallele che inviano blocchi di dati la cui dimensione è definita dal valore **threshold**.

La tipica configurazione per l'utilizzo di più sessioni dovrebbe essere:
* Velocità di destinazione di 2500 MBps
* Soglia di 100 MB (*questo è il valore consigliato per la maggior parte delle applicazioni*)

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
Nell'esempio sopra riportato, l'SDK genererà un numero sufficiente di sessioni per provare a raggiungere la velocità di destinazione di 2500 MBps.

In alternativa, la gestione delle sessioni può essere configurata esplicitamente nell'SDK. Ciò è utile nei casi in cui si voglia un controllo più preciso sull'utilizzo della rete.

La tipica configurazione per l'utilizzo di più sessioni esplicito dovrebbe essere:
* 2 o 10 sessioni
* Soglia di 100 MB (*questo è il valore consigliato per la maggior parte delle applicazioni*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

Per le migliori prestazioni nella maggior parte degli scenari, utilizza sempre sessioni multiple per ridurre al minimo il sovraccarico associato all'istanziazione di un trasferimento ad alta velocità Aspera.**Se la tua capacità di rete è di almeno 1 Gbps devi utilizzare 10 sessioni.**  Reti con una larghezza di banda inferiore devono utilizzare due sessioni.
{:tip}

*Valori chiave*
* `API_KEY` - una chiave API per un ID servizio o utente con ruoli di Writer (Scrittore) o Manager (Gestore)

Dovrai fornire una chiave API IAM per creare un `AsperaTransferManager`. [Le credenziali HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} attualmente **NON** sono supportate. Per ulteriori informazioni su IAM, [fai clic qui](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

### Caricamento file
{: #java-examples-aspera-upload}

```java
String filePath = "<absolute-path-to-source-data>";
String bucketName = "<bucket-name>";
String itemName = "<item-name>";

// Load file
File inputFile = new File(filePath);

// Create AsperaTransferManager for FASP upload
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client).build();

// Upload test file and report progress
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.upload(bucketName, itemName, inputFile);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato.
* `<absolute-path-to-source-data>` - nome di directory e file per il caricamento su Object Storage.
* `<item-name>` - nome del nuovo oggetto aggiunto al bucket.

### Download di file
{: #java-examples-aspera-download}

```java
String bucketName = "<bucket-name>";
String outputPath = "<absolute-path-to-file>";
String itemName = "<item-name>";

// Create local file
File outputFile = new File(outputPath);
outputFile.createNewFile();

// Create AsperaTransferManager for FASP download
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Download file
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.download(bucketName, itemName, outputFile);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato.
* `<absolute-path-to-file>` - nome di directory e file per il salvataggio da Object Storage.
* `<item-name>` - nome dell'oggetto nel bucket.

### Caricamento di directory
{: #java-examples-aspera-upload-directory}

```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

// Load Directory
File inputDirectory = new File(directoryPath);

// Create AsperaTransferManager for FASP upload
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Upload test directory
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.uploadDirectory(bucketName, directoryPrefix, inputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato.
* `<absolute-path-to-directory>` - directory dei file da caricare in Object Storage.
* `<virtual-directory-prefix>` - nome del prefisso di directory da aggiungere a ogni file al caricamento. Utilizza una stringa null o vuota per caricare i file nella root del bucket.

### Download di directory
{: #java-examples-aspera-download-directory}
```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

// Load Directory
File outputDirectory = new File(directoryPath);

// Create AsperaTransferManager for FASP download
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Download test directory
Future<AsperaTransaction> asperaTransactionFuture   = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato.
* `<absolute-path-to-directory>` - directory per il salvataggio dei file scaricati da Object Storage.
* `<virtual-directory-prefix>` - nome del prefisso di directory di ogni file da scaricare. Utilizza una stringa null o vuota per scaricare tutti i file nel bucket.

### Sovrascrittura della configurazione di sessione per ogni singolo trasferimento
{: #java-examples-aspera-config}

Puoi sovrascrivere i valori della configurazione a più sessioni per ogni singolo trasferimento passando un'istanza di `AsperaConfig` ai metodi sovraccaricati di caricamento e download. Utilizzando `AsperaConfig`, puoi specificare il numero di sessioni e la dimensione di soglia di file minima per ogni sessione. 

```java
String bucketName = "<bucket-name>";
String filePath = "<absolute-path-to-file>";
String itemName = "<item-name>";

// Load file
File inputFile = new File(filePath);

// Create AsperaTransferManager for FASP upload
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
.withTokenManager(TOKEN_MANAGER)
.withAsperaConfig(asperaConfig)
.build();

// Create AsperaConfig to set number of sessions
// and file threshold per session.
AsperaConfig asperaConfig = new AsperaConfig().
withMultiSession(10).
withMultiSessionThresholdMb(100);

// Upload test file and report progress
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.upload(bucketName, itemName, inputFile, asperaConfig, null);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

### Monitoraggio dell'avanzamento del trasferimento
{: #java-examples-aspera-monitor}

Il modo più semplice per monitorare l'avanzamento dei tuoi trasferimenti di file/directory consiste nell'utilizzare la proprietà `isDone()` che restituisce `true` quando il tuo trasferimento è completo.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Puoi anche controllare se un trasferimento è accodato per l'elaborazione richiamando il metodo `onQueue` sulla `AsperaTransaction`. `onQueue` restituirà un booleano con `true` che indica che il trasferimento è accodato.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Per controllare se un trasferimento è in corso, richiama il metodo progress in `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Ogni trasferimento avrà, per impostazione predefinita, un `TransferProgress` collegato a esso. Il `TransferProgress` notificherà il numero di byte trasferito e la percentuale trasferita dei byte totali da trasferire. Per accedere al `TransferProgress` di un trasferimento, utilizza il metodo `getProgress` in `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Per notificare il numero di byte trasferito, richiama il metodo `getBytesTransferred` su `TransferProgress`. Per notificare la percentuale trasferita dei byte totali da trasferire, richiama il metodo `getPercentTransferred` su `TransferProgress`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    System.out.println("Bytes transferred: " + transferProgress.getBytesTransferred());
    System.out.println("Percent transferred: " + transferProgress.getPercentTransferred());


    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

### Metti in pausa/Riprendi/Annulla
{: #java-examples-aspera-pause}

L'SDK consente di gestire l'avanzamento dei trasferimenti di file/directory mediante i seguenti metodi dell'oggetto `AsperaTransfer`:

* `pause()`
* `resume()`
* `cancel()`

Il richiamo dei metodi sopra indicati non ha alcun effetto collaterale. Adeguate attività di ripulitura e manutenzione sono gestite dall'SDK.
{:tip}

Il seguente esempio mostra un uso possibile per questi metodi:

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

//download the directory from cloud storage
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

int pauseCount = 0;

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download in progress...");

    //pause the transfer
    asperaTransfer.pause();

    //resume the transfer
    asperaTransfer.resume();

    //cancel the transfer
    asperaTransfer.cancel();
}

System.out.println("Directory download complete!");
```

### Risoluzione dei problemi di Aspera
{: #java-examples-aspera-ts}

**Problema:** gli sviluppatori che utilizzano Oracle JDK su Linux o Mac OS X potrebbero riscontrare degli arresti anomali imprevisti e non notificati durante i trasferimenti

**Causa:** il codice nativo richiede dei propri gestori di segnale che potrebbero sovrascrivere i gestori di segnale di JVM. Potrebbe essere necessario utilizzare la funzione di concatenamento dei segnali di JVM.

*Gli utenti di IBM&reg; JDK o Microsoft&reg; Windows non sono interessati da questo problema.*

**Soluzione:** collega e carica la libreria di concatenamento dei segnali di JVM.
* Su Linux, individua la libreria condivisa `libjsig.so` e imposta la seguente variabile di ambiente:
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* Su Mac OS X, individua la libreria condivisa `libjsig.dylib` e imposta le seguenti variabili di ambiente:
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

Per ulteriori informazioni sul concatenamento dei segnali, visita la [documentazione di Oracle&reg; JDK](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window}.

**Problema:** `UnsatisfiedLinkError` su Linux

**Causa:** il sistema non è in grado di caricare le librerie dipendenti. Errori come il seguente potrebbero essere riscontrati nei log dell'applicazione:

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**Soluzione:** imposta la seguente variabile di ambiente:

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
## Aggiornamento di metadati
{: #java-examples-metadata}
Ci sono due modi per aggiornare i metadati su un oggetto esistente:
* Una richiesta `PUT` con i nuovi metadati e il contenuto dell'oggetto originale
* L'esecuzione di una richiesta `COPY` con i nuovi metadati che specifica l'oggetto originale come origine della copia

### Utilizzo di PUT per aggiornare i metadati
{: #java-examples-metadata-put}

**Nota:** la richiesta `PUT` sovrascrive il contenuto esistente dell'oggetto e, pertanto, ne deve prima essere eseguito il download e quindi il ricaricamento con i nuovi metdati

```java
public static void updateMetadataPut(String bucketName, String itemName, String key, String value) throws IOException {
    System.out.printf("Updating metadata for item: %s\n", itemName);

    //retrieve the existing item to reload the contents
    S3Object item = _cos.getObject(new GetObjectRequest(bucketName, itemName));
    S3ObjectInputStream itemContents = item.getObjectContent();

    //read the contents of the item in order to set the content length and create a copy
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    int b;
    while ((b = itemContents.read()) != -1) {
        output.write(b);
    }

    int contentLength = output.size();
    InputStream itemCopy = new ByteArrayInputStream(output.toByteArray());

    //set the new metadata
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

### Utilizzo di COPY per aggiornare i metadati
{: #java-examples-metadata-copy}

```java
public static void updateMetadataCopy(String bucketName, String itemName, String key, String value) {
    System.out.printf("Updating metadata for item: %s\n", itemName);

    //set the new metadata
    HashMap<String, String> userMetadata = new HashMap<String, String>();
    userMetadata.put(key, value);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setUserMetadata(userMetadata);

    //set the copy source to itself
    CopyObjectRequest req = new CopyObjectRequest(bucketName, itemName, bucketName, itemName);
    req.setNewObjectMetadata(metadata);

    _cos.copyObject(req);

    System.out.printf("Updated metadata for item %s from bucket %s\n", itemName, bucketName);
}
```

## Utilizzo di Immutable Object Storage
{: #java-examples-immutable}

### Aggiungi una configurazione di protezione ad un bucket esistente
{: #java-examples-immutable-enable}

Questa implementazione dell'operazione `PUT` utilizza il parametro di query `protection` per impostare i parametri di conservazione per un bucket esistente. Questa operazione ti consente di impostare o modificare il periodo di conservazione minimo, predefinito e massimo. Questa operazione ti consente anche di modificare lo stato di protezione del bucket. 

Gli oggetti scritti in un bucket protetto non possono essere eliminati fino a quando il periodo di protezione non è scaduto e tutte le conservazioni a fini legali sull'oggetto non sono state rimosse. A un oggetto viene dato il valore di conservazione predefinito del bucket, a meno che non venga fornito un valore specifico per l'oggetto quando l'oggetto viene creato. Gli oggetti nei bucket protetti che non sono più sottoposti a conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti sono di nuovo sottoposti a conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket. 

I valori supportati minimo e massimo per le impostazioni del periodo di conservazione `MinimumRetention`, `DefaultRetention` e `MaximumRetention` sono, rispettivamente, 0 giorni e 365243 giorni (1000 anni). 

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

### Controlla la protezione su un bucket
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

### Carica un oggetto protetto
{: #java-examples-immutable-upload}

Gli oggetti nei bucket protetti che non sono più sottoposti a conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti sono di nuovo sottoposti a conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket.

|Valore	| Tipo	| Descrizione |
| --- | --- | --- | 
|`Retention-Period` | Numero intero non negativo (secondi) | Il periodo di conservazione da memorizzare sull'oggetto, in secondi. L'oggetto non può essere sovrascritto o eliminato finché l'intervallo di tempo specificato nel periodo di conservazione non è trascorso. Se vengono specificati questo campo e `Retention-Expiration-Date`, viene restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo `DefaultRetention` del bucket. Zero (`0`) è un valore consentito, presumendo che il periodo di conservazione minimo del bucket sia anch'esso `0`. |
| `Retention-expiration-date` | Data (formato ISO 8601) | Data in cui sarà consentito eliminare o modificare l'oggetto. Puoi specificare solo questo valore oppure l'intestazione Retention-Period. Se vengono specificati entrambi, verrà restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo DefaultRetention del bucket. |
| `Retention-legal-hold-id` | stringa | Una singola conservazione a fini legali da applicare all'oggetto. Una conservazione a fini legali è una stringa di caratteri di lunghezza Y. L'oggetto non può essere sovrascritto o eliminato finché non sono state rimosse tutte le conservazioni a fini legali a esso associate.|

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

### Aggiungi/rimuovi una conservazione a fini legai a/da un oggetto protetto
{: #java-examples-immutable-legal-hold}

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

### Estendi il periodo di conservazione di un oggetto protetto
{: #java-examples-immutable-extend}

Il periodo di conservazione di un oggetto può solo essere esteso. Non può essere ridotto rispetto al valore attualmente configurato.

Il valore di espansione della conservazione è impostato in uno di tre possibili modi:

* ulteriore tempo dal valore attuale (`Additional-Retention-Period` o metodo simile)
* nuovo periodo di estensione in secondi (`Extend-Retention-From-Current-Time` o metodo simile)
* nuova data di scadenza della conservazione dell'oggetto (`New-Retention-Expiration-Date` o metodo simile)

Il periodo di conservazione attuale memorizzato nei metadati dell'oggetto viene aumentato in misura equivalente al tempo aggiuntivo indicato oppure sostituito con il nuovo valore, a seconda del parametro impostato nella richiesta `extendRetention`. In tutti i casi, il parametro di estensione della conservazione viene controllato rispetto al periodo di conservazione attuale e il parametro esteso viene accettato solo se il periodo di conservazione aggiornato è più grande del periodo di conservazione attuale.

Gli oggetti nei bucket protetti che non sono più sottoposti a conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti sono di nuovo sottoposti a conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket.

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

### Elenca le conservazioni a fini legali su un oggetto protetto
{: #java-examples-immutable-list-holds}

Questa operazione restituisce:

* La data di creazione dell'oggetto
* Il periodo di conservazione dell'oggetto in secondi
* Data di scadenza della conservazione calcolata sulla base del periodo e della data di creazione
* Elenco delle conservazioni a fini legali
* Identificativo della conservazione a fini legali
* Data/ora di quando è stata applicata la conservazione a fini legali

Se non ci sono conservazioni a fini legali sull'oggetto, viene restituito un `LegalHoldSet` vuoto.
Se non c'è alcun periodo di conservazione specificato sull'oggetto, viene restituito un errore `404`.

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
