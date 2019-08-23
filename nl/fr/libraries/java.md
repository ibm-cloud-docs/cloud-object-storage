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

# Utilisation de Java
{: #java}

Le SDK {{site.data.keyword.cos_full}} pour Java est complet et comprend des fonctions qui ne sont pas décrites dans le présent guide. Pour obtenir une documentation détaillée sur les classes et les méthodes, [voir la Javadoc](https://ibm.github.io/ibm-cos-sdk-java/). Le code source se trouve dans le [référentiel GitHub](https://github.com/ibm/ibm-cos-sdk-java).

## Obtention du SDK
{: #java-install}

Le moyen le plus simple de consommer le SDK {{site.data.keyword.cos_full_notm}} pour Java est d'utiliser Maven pour gérer les dépendances. Si vous ne connaissez pas bien Maven, vous pouvez devenir opérationnel en vous reportant au guide intitulé [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html). 

Maven utilise un fichier nommé `pom.xml` pour spécifier les bibliothèques (et leurs versions) nécessaires pour un projet Java. Voici un exemple de fichier `pom.xml` permettant d'utiliser le SDK {{site.data.keyword.cos_full_notm}} pour Java afin de se connecter à {{site.data.keyword.cos_short}}. 


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

## Migration à partir de 1.x.x
{: #java-migrate}

L'édition 2.0 du SDK introduit un changement d'espace de nom qui permet à une application d'utiliser la bibliothèque AWS d'origine pour se connecter aux ressources AWS dans la même application ou le même environnement. La migration depuis la version 1.x vers l'édition 2.0 n'est possible que si les modifications suivantes sont effectuées :

1. Effectuez une mise à jour à l'aide de Maven en remplaçant toutes les balises de version de dépendance `ibm-cos-java-sdk` par `2.0.0` dans le fichier pom.xml. vérifiez qu'il n'existe aucune dépendance de module SDK dans le fichier pom.xml dont la version est antérieure à `2.0.0`. 
2. Effectuez une mise à jour des déclarations d'importation `amazonaws` en `ibm.cloud.objectstorage`. 


## Création d'un client et sourçage de données d'identification
{: #java-credentials}

Dans l'exemple ci-après, un client `cos` est créé et configuré en fournissant des données d'identification (clé d'API et ID d'instance de service). Ces valeurs peuvent aussi être automatiquement sourcées à partir d'un fichier de données d'identification ou à partir de variables d'environnement. 

Après que vous avez généré des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), le document JSON résultant peut être sauvegardé dans `~/.bluemix/cos_credentials`. Le SDK source automatiquement les données d'identification de ce fichier, sauf si d'autres données d'identification sont explicitement définies lors de la création du client. Si le fichier `cos_credentials` contient des clés HMAC, le client s'authentifie à l'aide d'une signature, sinon, il utilise la clé d'API fournie pour s'authentifier à l'aide d'un jeton bearer. 

Si vous effectuez une migration à partir de AWS S3, vous pouvez également sourcer des données d'identification à partir de `~/.aws/credentials` au format suivant :

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Si `~/.bluemix/cos_credentials` et `~/.aws/credentials` existent tous les deux, `cos_credentials` est prioritaire. 

 Pour plus d'informations sur la construction client, [voir la Javadoc](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html). 

## Exemples de code
{: #java-examples}

Commençons par un exemple de classe complet qui s'exécutera à travers certaines fonctionnalités de base. Ensuite nous explorerons les classes individuellement. Cette classe `CosExample` répertoriera les objets contenus dans un compartiment existant, créera un nouveau compartiment, puis répertoriera tous les compartiments contenus dans l'instance de service.  

### Collecte des informations requises
{: #java-examples-prereqs}

* `bucketName` et `newBucketName` sont des chaînes [uniques et sécurisées DNS](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Etant donné que les noms de compartiment sont uniques sur l'ensemble du système, ces valeurs devront être modifiées si cet exemple est exécuté plusieurs fois. Notez que les noms sont réservés pendant 10 à 15 minutes après leur suppression. 
* `api_key` est la valeur trouvée dans les [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) sous la forme `apikey`. 
* `service_instance_id` est la valeur trouvée dans les [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) sous la forme `resource_instance_id`.  
* `endpoint_url` est une URL de noeud final de service, incluse dans le protocole `https://`. Il ne s'agit **pas** de la valeur `endpoints` qui est trouvée dans les [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `storageClass` est un [code de mise à disposition valide](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) qui correspond à la valeur `endpoint`. Il est ensuite utilisé comme variable `LocationConstraint` de l'API S3. 
* `location` doit avoir pour valeur la partie emplacement de `storageClass`. Pour `us-south-standard`, il s'agira de `us-south`. Cette variable est utilisée uniquement pour le calcul des [signatures HMAC](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac), mais elle est requise pour tous les clients, dont cet exemple qui utilise une clé d'API IAM. 

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

### Initialisation de la configuration
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

*Valeurs de clé*
* `<endpoint>` - Noeud final public pour votre solution Cloud Object Storage (disponible à partir du [tableau de bord IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - Clé d'API générée lors de la création des données d'identification de service (un accès en écriture est requis pour les exemples de création et de suppression). 
* `<resource-instance-id>` - ID de ressource pour votre solution Cloud Object Storage (disponible via l'[interface CLI IBM Cloud](/docs//docs/cli?topic=cloud-cli-idt-cli) ou le [tableau de bord IBM Cloud](https://cloud.ibm.com/resources){:new_window}). 
* `<location>` - Emplacement par défaut pour votre solution Cloud Object Storage (doit correspondre à la région utilisée pour `<endpoint>`). 

*Références SDK*
* Classes
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### Détermination de noeud final
{: #java-examples-endpoint}

Les méthodes ci-après peuvent être utilisées pour déterminer le noeud final de service en fonction de l'emplacement du compartiment, du type de noeud final (public ou privé) et de la région spécifique (facultatif). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

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

### Création d'un nouveau compartiment
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### Création d'un compartiment avec une classe de stockage différente
{: #java-examples-storage-class}

Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*Références SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### Création d'un nouveau fichier texte
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
Notez que lors de l'ajout de métadonnées personnalisées à un objet, un objet `ObjectMetadata` doit être créé à l'aide du SDK et un en-tête personnalisé contenant `x-amz-meta-{key}` ne doit pas être envoyé manuellement. Ce dernier peut provoquer des problèmes lors de l'authentification à l'aide des données d'identification HMAC.
{: .tip}

### Envoi par téléchargement d'un objet à partir d'un fichier
{: #java-examples-upload}

Cet exemple part du principe que le compartiment `sample` existe déjà. 

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### Envoi par téléchargement d'un objet à l'aide d'un flux
{: #java-examples-stream}

Cet exemple part du principe que le compartiment `sample` existe déjà. 

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

### Envoi par téléchargement d'un objet à un fichier
{: #java-examples-download}

Cet exemple part du principe que le compartiment `sample` existe déjà. 

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


### Réception par téléchargement d'un objet à l'aide d'un flux
{: #java-examples-download-stream}

Cet exemple part du principe que le compartiment `sample` existe déjà. 

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### Copie d'objets
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

*Références SDK*
* Classes
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* Méthodes
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### Exception putObject
{: #java-examples-put-exception}

La méthode putObject peut émettre l'exception suivante même si le nouvel envoi par téléchargement d'objet a abouti :
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

**Cause première :** les API JAXB sont considérées comme des API Java EE et ne sont plus contenues sur le chemin de classe par défaut dans Java SE 9.

**Correctif :** ajoutez l'entrée suivante au fichier pom.xml dans votre dossier de projet et reconditionnez votre projet. 
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### Création de la liste des compartiments disponibles
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
*Références SDK*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* Méthodes
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### Création de la liste des éléments contenus dans un compartiment (v2)
{: #java-examples-list-objects-v2}

L'objet [AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} contient une méthode mise à jour permettant de répertorier le contenu ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}). Cette méthode vous permet de limiter le nombre d'enregistrements renvoyés et d'extraire les enregistrements par lots. Cela peut s'avérer utile pour paginer vos résultats dans une application et améliorer les performances. 

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

*Références SDK*
* Classes
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Méthodes
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### Création de la liste des éléments contenus dans un compartiment (v1)
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

*Références SDK*
* Classes
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Méthodes
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### Obtention du contenu de fichier d'un élément particulier
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

*Références SDK*
* Classes
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* Méthodes
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### Suppression d'un élément d'un compartiment
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*Références SDK*
* Méthodes
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### Suppression de plusieurs éléments d'un compartiment
{: #java-examples-delete-objects}

La demande de suppression peut contenir un maximum de 1000 clés. Bien que cela soit très utile pour réduire le temps système pour chaque demande, n'oubliez pas que la durée d'exécution d'une suppression d'un grand nombre de clés peut prendre un certain temps. Tenez compte également de la taille des objets de manière à obtenir de bonnes performances. {:tip}

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

*Références SDK*
* Classes
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* Méthodes
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### Suppression d'un compartiment
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*Références SDK*
* Méthodes
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### Procédure permettant de vérifier si un objet est accessible en lecture par le public
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

*Références SDK*
* Classes
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* Méthodes 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### Exécution d'un envoi par téléchargement en plusieurs parties
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
*Références SDK*
* Classes
    * [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){:new_window}
    * [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}
    * [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){:new_window}
    * [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){:new_window}

* Méthodes
    * [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){:new_window}
    * [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){:new_window}
    * [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){:new_window}
    * [uploadPart](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){:new_window}

## Envoi par téléchargement d'objets plus volumineux à l'aide d'une classe TransferManager
{: #java-examples-transfer-manager}

`TransferManager` simplifie les transferts de fichiers volumineux en incorporant automatiquement des envois par téléchargement en plusieurs parties chaque fois que cela est nécessaire lors de la définition des paramètres de configuration. 

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

*Références SDK*
* Classes
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* Méthodes
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## Utilisation de Key Protect
{: #java-examples-kp}
Key Protect peut être ajouté à un compartiment de stockage pour chiffrer les données sensibles qui sont au repos dans le cloud. 

### Avant de commencer
{: #java-examples-kp-prereqs}

Les éléments suivants sont nécessaires pour créer un compartiment avec Key Protect activé :

* Un service Key Protect doit être [mis à disposition](/docs/services/key-protect?topic=key-protect-provision#provision). 
* Une clé racine doit être disponible ([générée](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importée](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)). 

### Extraction du CRN de clé racine
{: #java-examples-kp-root}

1. Extrayez l'[ID d'instance](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) pour votre service Key Protect. 
2. Utilisez l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) pour extraire toutes vos [clés disponibles](https://cloud.ibm.com/apidocs/key-protect). 
    * Vous pouvez utiliser des commandes `curl` ou un client REST API, tel que [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), pour accéder à l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api). 
3. Extrayez le CRN de la clé racine que vous utiliserez pour activer Key Protect sur votre compartiment. Le CRN se présentera comme suit :

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Création d'un compartiment avec Key Protect activé
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
*Valeurs de clé*
* `<algorithm>` - Algorithme de chiffrement pour les nouveaux objets ajoutés au compartiment (la valeur par défaut est AES256). 
* `<root-key-crn>` - CRN de la clé racine qui est obtenue auprès du service Key Protect. 

*Références SDK*
* Classes
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* Méthodes
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Nouveaux en-têtes pour Key Protect
{: #java-examples-kp-headers}

Les en-têtes supplémentaires ont été définis dans la classe `Headers` :

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

La même section de l'implémentation de création de compartiment qui ajoute déjà les en-têtes d'instance de service IAM ajoutera les 2 nouveaux en-têtes de chiffrement :

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

Les objets `ObjectListing` et `HeadBucketResult` ont été mis à jour pour inclure des variables `IBMSSEKPEnabled` & String `IBMSSEKPCustomerRootKeyCrn` booléennes avec des méthodes d'accès get et set. Cela permettra de stocker les valeurs des nouveaux en-têtes. 

#### Opération GET sur un compartiment
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

La classe `ObjectListing` requiert deux méthodes supplémentaires :

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

Les en-têtes supplémentaires ont été définis dans la classe `Headers` :

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

Le gestionnaire S3XmlResponseHandler est chargé de la déconversion de paramètres dans toutes les réponses XML. Une vérification a été ajoutée afin de s'assurer que le résultat est une instance de `ObjectListing` et les en-têtes extraits seront ajoutés à l'objet `ObjectListing` :

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

#### Opération HEAD sur un compartiment
{: #java-examples-kp-head}
Les en-têtes supplémentaires ont été définis dans la classe Headers : 

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

Ces variables sont remplies dans le gestionnaire HeadBucketResponseHandler.

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Utilisation de l'option Transfert haut débit Aspera
{: #java-examples-aspera}

En installant la [bibliothèque de transfert haut débit Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), vous pouvez utiliser des transferts de fichiers à haut débit dans votre application. La bibliothèque Aspera est close source, il s'agit donc d'une dépendance facultative pour le SDK COS (qui utilise une licence Apache).  

Chaque session de transfert haut débit Aspera génère un processus `ascp` individuel qui s'exécute sur la machine client pour effectuer le transfert. Assurez-vous que votre environnement de calcul puisse permettre l'exécution de ce processus. {:tip}

Vous aurez besoin d'instances des classes du client S3 et du gestionnaire de jetons IAM pour initialiser `AsperaTransferManager`. L'objet `s3Client` est requis afin d'obtenir des informations de connexion FASP pour le compartiment cible COS. L'objet `tokenManager` est requis pour permettre au SDK de l'option Transfert haut débit Aspera de s'authentifier auprès du compartiment cible COS. 

### Initialisation de `AsperaTransferManager`
{: #java-examples-aspera-init}

Avant d'initialiser `AsperaTransferManager`, assurez-vous que les objets [`s3Client`](#java-examples-config) et [`tokenManager`](#java-examples-config) fonctionnent.  

L'utilisation d'une seule session de transfert haut débit Aspera ne présente pas beaucoup d'avantages sauf si vous vous attendez à constater une mauvaise connexion ou une perte de paquet significative dans le réseau. Par conséquent, nous devons indiquer à `AsperaTransferManager` qu'il doit utiliser plusieurs sessions à l'aide de la classe `AsperaConfig`. Cela permettra de fractionner le transfert en un certain nombre de **sessions** parallèles qui envoient des blocs de données dont la taille est définie par la valeur **threshold**. 

La configuration standard pour l'utilisation de plusieurs sessions doit être la suivante :
* Débit cible de 2500 Mbit/s
* Seuil de 100 Mo (*il s'agit de la valeur recommandée pour la plupart des applications*)

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
Dans l'exemple ci-dessus, le SDK générera suffisamment de sessions pour tenter d'atteindre le taux cible de 2500 Mbit/s. 

Sinon, la gestion de session peut aussi être configurée explicitement dans le SDK. Cela s'avère utile si vous souhaitez un contrôle plus précis de l'utilisation du réseau. 

La configuration standard pour l'utilisation de plusieurs sessions explicites doit être la suivante :
* 2 ou 10 sessions
* Seuil de 100 Mo (*il s'agit de la valeur recommandée pour la plupart des applications*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

Pour optimiser les performances dans la plupart des scénarios, utilisez toujours plusieurs sessions afin de minimiser les temps système liés à l'instanciation d'un transfert haut débit Aspera. **Si votre capacité réseau est d'au moins 1 Gbps, vous devez utiliser 10 sessions. **  Des réseaux avec une bande passante inférieure doivent utiliser 2 sessions.
{:tip}

*Valeurs de clé*
* `API_KEY` - Clé d'API pour un utilisateur ou un ID de service doté des rôles Auteur ou Responsable. 

Vous devrez fournir une clé d'API IAM pour la construction d'un élément `AsperaTransferManager`. Les [données d'identification HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} ne sont **PAS** prises en charge actuellement. Pour plus d'informations sur IAM, [cliquez ici](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

### Envoi par téléchargement d'un fichier
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

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-source-data>` - Nom de répertoire et de fichier utilisé pour l'envoi par téléchargement vers Object Storage.
* `<item-name>` - Nom du nouvel objet ajouté au compartiment. 

### Réception par téléchargement d'un fichier
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

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-file>` - Nom de répertoire et de fichier utilisé pour la sauvegarde à partir d'Object Storage.
* `<item-name>` - Nom de l'objet dans le compartiment. 

### Envoi par téléchargement d'un répertoire
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

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-directory>` - Répertoire des fichiers à envoyer par téléchargement vers Object Storage. 
* `<virtual-directory-prefix>` - Nom du préfixe de répertoire à ajouter à chaque fichier lors de l'envoi par téléchargement. Utilisez une chaîne vide ou nulle pour envoyer par téléchargement les fichiers vers la racine du compartiment. 

### Réception par téléchargement d'un répertoire
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

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-directory>` - Répertoire utilisé pour sauvegarder les fichiers reçus par téléchargement à partir d'Object Storage. 
* `<virtual-directory-prefix>` - Nom du préfixe de répertoire à ajouter à chaque fichier à recevoir par téléchargement. Utilisez une chaîne vide ou nulle pour recevoir par téléchargement tous les fichiers du compartiment. 

### Remplacement de la configuration de session pour chaque transfert
{: #java-examples-aspera-config}

Vous pouvez remplacer les valeurs de configuration de plusieurs sessions pour chaque transfert en transmettant une instance de `AsperaConfig` aux méthodes surchargées d'envoi par téléchargement et de réception par téléchargement. A l'aide de `AsperaConfig`, vous pouvez spécifier le nombre de sessions et la taille minimale de fichier par session.  

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

### Surveillance de la progression du transfert
{: #java-examples-aspera-monitor}

Le moyen le plus simple de surveiller la progression de vos transferts de fichiers/répertoires consiste à utiliser la propriété `isDone()` qui renvoie la valeur `true` lorsque votre transfert est terminé. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Vous pouvez également vérifier si un transfert est mis en file d'attente pour traitement en appelant la méthode `onQueue` sur `AsperaTransaction`. `onQueue` renvoie une valeur booléenne avec `true` pour indiquer que le transfert a été mis en file d'attente. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Pour vérifier si un transfert est en cours, appelez la méthode progress dans `AsperaTransaction`. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Par défaut, un élément `TransferProgress` est associé à chaque transfert. L'élément `TransferProgress` signale le nombre d'octets transférés et le pourcentage d'octets transférés par rapport au nombre total d'octets à transférer. Pour accéder à l'élément `TransferProgress` d'un transfert, utilisez la méthode `getProgress` dans `AsperaTransaction`. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Pour signaler le nombre d'octets transférés, appelez la méthode `getBytesTransferred` sur `TransferProgress`. Pour signaler le pourcentage d'octets transférés par rapport au nombre total d'octets à transférer, appelez la méthode `getPercentTransferred` sur `TransferProgress`. 

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

### Pause/Reprise/Annulation
{: #java-examples-aspera-pause}

Le SDK permet de gérer la progression des transferts de fichiers/répertoires via les méthodes suivantes de l'objet `AsperaTransfer` :

* `pause()`
* `resume()`
* `cancel()`

Il n'y a pas d'effets secondaires liés à l'appel de l'une ou l'autre des méthodes ci-dessus. Le nettoyage adéquat est géré par le SDK. {:tip}

L'exemple suivant illustre une utilisation possible de ces méthodes :

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

### Traitement des incidents liés à Aspera
{: #java-examples-aspera-ts}

**Problème :** les développeurs qui utilisent le SDK Oracle sur Linux ou Mac OS X peuvent rencontrer des pannes inattendues et silencieuses lors des transferts. 

**Cause :** le code natif requiert ses propres gestionnaires de signaux, lesquels sont susceptibles d'écraser les gestionnaires de signaux de la machine virtuelle Java. Il peut être nécessaire d'utiliser la fonction de chaînage de signaux de la machine virtuelle Java. 

*Les utilisateurs d'IBM&reg; JDK ou de Microsoft&reg; Windows ne sont pas concernés.*

**Solution :** liez et chargez la bibliothèque de chaînage de signaux de la machine virtuelle Java. 
* Sous Linux, localisez la bibliothèque partagée `libjsig.so` et définissez la variable d'environnement suivante : 
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* Sous Mac OS X, localisez la bibliothèque partagée `libjsig.dylib` et définissez les variables d'environnement suivantes :
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

Pour plus d'informations sur le chaînage de signaux, voir la [documentation d'Oracle&reg; JDK](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window}. 

**Problème :** erreur `UnsatisfiedLinkError` sous Linux. 

**Cause :** le système ne peut pas charger les bibliothèques dépendantes. Des erreurs telles que la suivante peuvent être consignées dans les journaux d'application : 

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**Solution :** définissez la variable d'environnement suivante :

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
## Mise à jour de métadonnées
{: #java-examples-metadata}
Il existe deux manières de mettre à jour les métadonnées sur un objet existant :
* En exécutant une demande `PUT` avec les nouvelles métadonnées et le contenu de l'objet d'origine 
* En exécutant une demande `COPY` avec les nouvelles métadonnées qui spécifient l'objet d'origine comme source de la copie 

### Utilisation de PUT pour mettre à jour les métadonnées
{: #java-examples-metadata-put}

**Remarque :** la demande `PUT` remplace le contenu existant de l'objet, par conséquent, le contenu doit d'abord être reçu par téléchargement, puis à nouveau envoyé par téléchargement avec les nouvelles métadonnées. 

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

### Utilisation de COPY pour mettre à jour les métadonnées
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

## Utilisation de la fonction Immutable Object Storage
{: #java-examples-immutable}

### Ajout d'une configuration de protection à un compartiment existant
{: #java-examples-immutable-enable}

Cette implémentation de l'opération `PUT` utilise le paramètre de requête `protection` pour définir les paramètres de conservation d'un compartiment existant. Cette opération vous permet de définir ou modifier la période de conservation minimale, par défaut et maximale. Cette opération vous permet également de modifier l'état de protection du compartiment.  

Les objets écrits dans un compartiment protégé ne peuvent pas être supprimés tant que la période de protection n'est pas arrivée à expiration et que les conservations légales associées à l'objet n'ont pas toutes été retirées. La valeur de conservation par défaut du compartiment est attribuée à un objet, sauf si une valeur spécifique à un objet est fournie lors de la création de l'objet. Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet.  

Les valeurs minimales et maximales prises en charge pour les paramètres de durée de conservation `MinimumRetention`, `DefaultRetention` et `MaximumRetention` sont respectivement de 0 jours et 365243 jours (1000 ans).  

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

### Vérification de la protection d'un compartiment
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

### Envoi par téléchargement d'un objet protégé
{: #java-examples-immutable-upload}

Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 

|Valeur	| Type	| Description |
| --- | --- | --- | 
|`Retention-Period` | Entier non négatif (secondes) | Durée de conservation, exprimée en secondes, pendant laquelle stocker l'objet. L'objet ne peut être ni écrasé, ni supprimé tant que la durée de conservation n'est pas écoulée. Si cette zone et la zone `Retention-Expiration-Date` sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la période `DefaultRetention` du compartiment est utilisée. Zéro (`0`) est une valeur légale qui part du principe que la durée de conservation minimale du compartiment est également fixée à `0`. |
| `Retention-expiration-date` | Date (format ISO 8601) | Date à laquelle la suppression ou la modification de l'objet devient légale. Vous pouvez uniquement spécifier cette zone ou bien la zone d'en-tête Retention-Period. Si ces deux zones sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la durée de conservation par défaut du compartiment est utilisée. |
| `Retention-legal-hold-id` |Chaîne| Conservation légale unique à appliquer à l'objet. Une conservation légale est une chaîne comportant Y caractères. L'objet ne peut être ni écrasé ni supprimé tant que les conservations légales associées à l'objet n'ont pas toutes été retirées. |

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

### Ajout ou retrait d'une conservation légale dans un objet protégé
{: #java-examples-immutable-legal-hold}

L'objet peut prendre en charge 100 conservations légales :

*  Un identificateur de conservation légale est une chaîne comprise entre 1 et 64 caractères. Les caractères valides sont des lettres, des chiffres, `!`, `_`, `.`, `*`, `(`, `)`, `-` et `.
* Si la tentative d'ajout de la conservation légale spécifiée dépasse le seuil de 100 conservations légales affectées à l'objet, l'opération d'ajout échoue et un code d'erreur `400` est renvoyé. 
* Si un identificateur est trop long, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur contient des caractères non valides, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur est déjà en cours d'utilisation sur un objet, la conservation légale existante n'est pas modifiée et la réponse indique que l'identificateur était déjà utilisé et renvoie un code d'erreur `409`. 
* Si un objet ne comporte pas de métadonnées de durée de conservation, un code d'erreur `400` est renvoyé et l'ajout ou le retrait d'une conservation légale ne sont pas autorisés. 

La présence d'un en-tête de durée de conservation est obligatoire, sinon, une erreur `400` est renvoyée.
{: http}

L'utilisateur qui effectue l'ajout ou la suppression d'une conservation légale doit disposer des droits d'accès `Manager` pour ce compartiment. 

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

### Prolongement de la durée de conservation d'un objet protégé
{: #java-examples-immutable-extend}

La durée de conservation d'un objet ne peut être que prolongée. La valeur actuellement configurée ne peut pas être diminuée. 

La valeur de prolongement de la conservation est définie de l'une des trois façons suivantes :

* En ajoutant une durée supplémentaire par rapport à la valeur en cours (`Additional-Retention-Period` ou méthode similaire) 
* En définissant une nouvelle période de prolongement, exprimée en secondes (`Extend-Retention-From-Current-Time` ou méthode similaire) 
* En définissant une nouvelle date d'expiration de la conservation de l'objet (`New-Retention-Expiration-Date` ou méthode similaire) 

La durée de conservation en cours stockée dans les métadonnées de l'objet est soit augmentée de la durée supplémentaire indiquée, soit remplacée par la nouvelle valeur, en fonction du paramètre défini dans la demande `extendRetention`. Dans tous les cas, le paramètre de prolongement de la conservation est vérifié par rapport à la durée de conservation en cours et le paramètre de prolongement n'est accepté que si la durée de conservation mise à jour est supérieure à la durée de conservation en cours. 

Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 

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

### Création de la liste des conservations légales associées à un objet protégé
{: #java-examples-immutable-list-holds}

Cette opération renvoie les éléments suivants :

* La date de création de l'objet
* La durée de conservation, exprimée en secondes
* La date d'expiration de la conservation, calculée sur la base de la durée de conservation et de la date de création
* La liste des conservations légales
* L'identificateur de conservation légale
* L'horodatage correspondant à l'application de la conservation légale

Si aucune conservation légale n'est associée à l'objet, un élément `LegalHoldSet` vide est renvoyé.
Si aucune durée de conservation n'est spécifiée pour l'objet, un code d'erreur `404` est renvoyé. 

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
