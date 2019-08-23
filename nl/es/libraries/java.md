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

# Utilización de Java
{: #java}

El SDK de {{site.data.keyword.cos_full}} para Java es muy completo y tiene características que no se describen en esta guía. Para obtener más información sobre clases y métodos, [consulte la documentación de Java](https://ibm.github.io/ibm-cos-sdk-java/). Encontrará el código fuente en el [repositorio GitHub](https://github.com/ibm/ibm-cos-sdk-java).

## Obtención del SDK
{: #java-install}

La forma más sencilla de consumir el SDK Java de {{site.data.keyword.cos_full_notm}} consiste en utilizar Maven para gestionar dependencias. Si no está familiarizado con Maven, consulte la guía [Maven en 5 minutos](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).

Maven utiliza un archivo llamado `pom.xml` para especificar las bibliotecas (y sus versiones) necesarias para un proyecto Java. A continuación se muestra un archivo `pom.xml` de ejemplo para utilizar el SDK Java de {{site.data.keyword.cos_full_notm}} para conectar con {{site.data.keyword.cos_short}}.


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

## Migración desde la versión 1.x.x
{: #java-migrate}

El release 2.0 del SDK incorpora un cambio en los espacios de nombres que permite que una aplicación utilice la biblioteca AWS original para conectar con recursos de AWS dentro de la misma aplicación o el mismo entorno. Para migrar de 1.x a 2.0, es necesario realizar algunos cambios:

1. Actualice mediante Maven cambiando todas las etiquetas de versión de dependencia `ibm-cos-java-sdk` por `2.0.0` en el archivo pom.xml. Compruebe que no haya dependencias de módulos SDK en el archivo pom.xml con una versión anterior a `2.0.0`.
2. Actualice las declaraciones de importación `amazonaws` por `ibm.cloud.objectstorage`.


## Creación de credenciales de cliente y de origen
{: #java-credentials}

En el ejemplo siguiente, se crea un cliente `cos` y se configura proporcionando información de credenciales (clave de API e ID de instancia de servicio). Estos valores también se pueden tomar automáticamente de un archivo de credenciales o de variables de entorno.

Después de generar una [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), el documento JSON resultante se puede guardar en `~/.bluemix/cos_credentials`. El SDK tomará automáticamente las credenciales de este archivo, a menos que se establezcan explícitamente otras credenciales durante la creación del cliente. Si el archivo `cos_credentials` contiene claves de HMAC, el cliente se autenticará con una firma; de lo contrario, el cliente utilizará la clave de API proporcionada para autenticarse utilizando una señal de portadora.

Si se migra desde AWS S3, también puede obtener los datos de credenciales de origen de `~/.aws/credentials` en el formato:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Si existen tanto `~/.bluemix/cos_credentials` como `~/.aws/credentials`, prevalece `cos_credentials`.

 Para obtener más detalles sobre la creación del cliente, [consulte la documentación de Java](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html).

## Ejemplos de código
{: #java-examples}

Empezaremos con una clase de ejemplo completa que se ejecutará con algunas funciones básicas y luego exploraremos las clases individualmente. Esta clase `CosExample` obtiene una lista de los objetos de un grupo existente, crea un grupo nuevo y luego obtiene una lista de todos los grupos de la instancia de servicio. 

### Obtención de la información necesaria
{: #java-examples-prereqs}

* `bucketName` y `newBucketName` son series [exclusivas y DNS seguras](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Puesto que los nombres de grupo son exclusivos en todo el sistema, estos valores se tendrán que modificar si este ejemplo se ejecuta varias veces. Tenga en cuenta que los nombres se reservan durante 10-15 minutos tras su supresión.
* `api_key` es el valor que se encuentra en la [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `apikey`.
* `service_instance_id` es el valor que se encuentra en la [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `resource_instance_id`. 
* `endpoint_url` es un URL de punto final de servicio, incluido el protocolo `https://`. **No** es
el valor de `endpoints` que se encuentra en la [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `storageClass` es un [código de suministro válido](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) que se corresponde con el valor de `endpoint`. Luego se utiliza como la variable `LocationConstraint` de la API S3.
* `location` se debe establecer en la parte de ubicación de `storageClass`. Para `us-south-standard`, sería `us-south`. Esta variable solo se utiliza para el cálculo de [firmas de HMAC](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac), pero es necesaria para cualquier cliente, incluido este ejemplo que utiliza una clave de API de IAM.

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

### Inicialización de la configuración
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

*Valores de clave*
* `<endpoint>`: punto final público para Cloud Object Storage (disponible en el [panel de control de IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>`: clave de api que se genera cuando se crean las credenciales de servicio (se necesita acceso de escritura para los ejemplos de creación y supresión)
* `<resource-instance-id>`: ID de recurso para Cloud Object Storage (disponible en el [CLI de IBM Cloud](/docs//docs/cli?topic=cloud-cli-idt-cli) o en el [panel de control de IBM Cloud](https://cloud.ibm.com/resources){:new_window})
* `<location>`: ubicación predeterminada para Cloud Object Storage (debe coincidir con la región utilizada para `<endpoint>`)

*Referencias del SDK*
* Clases
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### Determinación del punto final
{: #java-examples-endpoint}

Los métodos siguientes se pueden utilizar para determinar el punto final de servicio en función de la ubicación del grupo, el tipo de punto final (público o privado) y la región específica (opcional). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```java
/**
* Devuelve un punto final de servicio que depende de la
* ubicación de la clase de almacenamiento (por ejemplo, us-standard, us-south-standard)
* y del tipo de punto final (public o private)
*/
public static String getEndpoint(String location, String endPointType) {
    return getEndpoint(location, "", endPointType);
}

/**
* Devuelve un punto final de servicio que depende de la
* ubicación de la clase de servicio (por ejemplo, us-standard, us-south-standard),
* y de la región específica si se desea (por ejemplo, sanjose, amsterdam); utilícelo solo si desea un * punto final regional y un tipo de punto final específico (public o private)
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

### Creación de un nuevo grupo
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### Creación de un grupo con una clase de almacenamiento diferente
{: #java-examples-storage-class}

Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*Referencias del SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### Creación de un nuevo archivo de texto
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
Tenga en cuenta que cuando se añaden metadatos personalizados a un objeto, es necesario crear un objeto `ObjectMetadata` mediante el SDK, no enviar manualmente una cabecera personalizada que contenga `x-amz-meta-{key}`. Esto último puede provocar problemas al autenticar con las credenciales de HMAC.
{: .tip}

### Carga de un objeto desde un archivo
{: #java-examples-upload}

En este ejemplo se supone que el grupo `sample` ya existe.

```java
cos.putObject(
    "sample", // el nombre del grupo de destino
    "myfile", // la clave de objeto
    new File("/home/user/test.txt") // el nombre y vía de acceso de archivo del objeto que se va a cargar
);
```

### Carga de un objeto mediante una secuencia
{: #java-examples-stream}

En este ejemplo se supone que el grupo `sample` ya existe.

```java
String obj = "An example"; // el objeto que se va a guardar
ByteArrayOutputStream theBytes = new ByteArrayOutputStream(); // crear una nueva secuencia de salida para guardar los datos del objeto
ObjectOutputStream serializer = new ObjectOutputStream(theBytes); // definir los datos del objeto que se van a serializar
serializer.writeObject(obj); // serializar los datos del objeto
serializer.flush();
serializer.close();
InputStream stream = new ByteArrayInputStream(theBytes.toByteArray()); // convertir los datos serializados en una nueva secuencia de entrada que guardar
ObjectMetadata metadata = new ObjectMetadata(); // define the metadata
metadata.setContentType("application/x-java-serialized-object"); // definir los metadatos
metadata.setContentLength(theBytes.size()); // definir metadatos para la longitud de la secuencia de datos
cos.putObject(
    "sample", // el nombre del grupo en el que se escribe el objeto
    "serialized-object", // el nombre del objeto que se escribe
    stream, // el nombre de la secuencia de datos que escribe el objeto
    metadata // los metadatos del objeto que se escribe
);
```

### Descarga de un objeto en un archivo
{: #java-examples-download}

En este ejemplo se supone que el grupo `sample` ya existe.

```java
GetObjectRequest request = new // crear una nueva solicitud para obtener un objeto
GetObjectRequest( // solicitar el nuevo objeto identificando
    "sample", // el nombre del grupo
    "myFile" // el nombre del objeto
);

s3Client.getObject( // escribir el contenido del objeto
    request, // utilizando la solicitud que se acaba de crear
    new File("retrieved.txt") // para escribir en un nuevo archivo
);
```


### Descarga de un objeto mediante una secuencia
{: #java-examples-download-stream}

En este ejemplo se supone que el grupo `sample` ya existe.

```java
S3Object returned = cos.getObject( // solicitar el objeto identificando
    "sample", // el nombre del grupo
    "serialized-object" // el nombre del objeto serializado
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### Copia de objetos
{: #java-examples-copy}

```java
// copiar un objeto en el mismo grupo
cos.copyObject( // copiar el objeto, pasando…
    "sample",  // el nombre del grupo en el que se almacena el objeto que se va a copiar,
    "myFile.txt",  // el nombre del objeto que se va a copiar del grupo de origen,
    "sample",  // el nombre del grupo en el que se almacena el objeto que se va a copiar,
    "myFile.txt.backup"    // y el nuevo nombre de la copia del objeto que se va a copiar
);
```

```java
// copiar un objeto entre dos grupos
cos.copyObject( // copiar el objeto, pasando…
    "sample", // el nombre del grupo del que se va a copiar el objeto,
    "myFile.txt", // el nombre del objeto que se va a copiar del grupo de origen,
    "backup", // el nombre del grupo en el que se va a copiar el objeto,
    "myFile.txt" // y el nombre del objeto copiado en el grupo de destino
);
```

*Referencias del SDK*
* Clases
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* Métodos
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### Excepción putObject
{: #java-examples-put-exception}

El método putObject puede generar la excepción siguiente, incluso si la carga de objeto nueva se ha realizado correctamente
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

**Causa raíz:** las API de JAXB se consideran API de Java EE y ya no están contenidas en la variable path de clase predeterminada en Java SE 9.

**Solución:** añada la entrada siguiente al archivo pom.xml en la carpeta del proyecto y vuelva a empaquetar el proyecto.
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### Obtención de una lista de grupos disponibles
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
*Referencias del SDK*
* Clases
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* Métodos
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### Obtención de la lista de elementos de un grupo (v2)
{: #java-examples-list-objects-v2}

El objeto [AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} contiene un método actualizado para obtener una lista del contenido ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}). Este método le permite limitar el número de registros devueltos y recuperar los registros por lotes. Esto puede resultar útil para paginar los resultados dentro de una aplicación y mejorar el rendimiento.

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

*Referencias del SDK*
* Clases
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Métodos
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### Obtención de la lista de elementos de un grupo (v1)
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

*Referencias del SDK*
* Clases
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Métodos
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### Obtención del contenido de archivo de un elemento determinado
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

*Referencias del SDK*
* Clases
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* Métodos
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### Supresión de un elemento de un grupo
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*Referencias del SDK*
* Métodos
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### Supresión de varios elementos de un grupo
{: #java-examples-delete-objects}

La solicitud de supresión puede contener un máximo de 1000 claves que desea suprimir. Si bien esto es muy útil para reducir la sobrecarga por solicitud, tenga cuidado cuando vaya a suprimir un gran número de claves. Tenga también en cuenta los tamaños de los objetos para garantizar un rendimiento adecuado.
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

*Referencias del SDK*
* Clases
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* Métodos
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### Supresión de un grupo
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*Referencias del SDK*
* Métodos
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### Comprobación de si un objeto se puede leer públicamente
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

*Referencias del SDK*
* Clases
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* Métodos 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### Ejecución de una carga de varias partes
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

    //empezar la carga de las partes
    //tamaño mínimo de parte 5 MB
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

        //completar la carga
        _cos.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, itemName, uploadID, dataPacks));
        System.out.printf("Upload for %s Complete!\n", itemName);
    } catch (SdkClientException sdke) {
        System.out.printf("Multi-part upload aborted for %s\n", itemName);
        System.out.printf("Upload Error: %s\n", sdke.getMessage());
        _cos.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
    }
}
```
*Referencias del SDK*
* Clases
    * [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){:new_window}
    * [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}
    * [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){:new_window}
    * [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){:new_window}

* Métodos
    * [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){:new_window}
    * [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){:new_window}
    * [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){:new_window}
    * [uploadPart](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){:new_window}

## Carga de objetos grandes mediante un gestor de transferencias
{: #java-examples-transfer-manager}

`TransferManager` simplifica las transferencias de archivos de gran tamaño incorporando automáticamente las cargas de varias partes siempre que sea necesario estableciendo los parámetros de configuración.

```java
public static void largeObjectUpload(String bucketName, String itemName, String filePath) throws IOException, InterruptedException {
    File uploadFile = new File(filePath);

    if (!uploadFile.isFile()) {
        System.out.printf("The file '%s' does not exist or is not accessible.\n", filePath);
        return;
    }

    System.out.println("Starting large file upload with TransferManager");

    //establecer el tamaño de parte en 5 MB
    long partSize = 1024 * 1024 * 5;

    //establecer el tamaño de umbral en 5 MB
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

*Referencias del SDK*
* Clases
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* Métodos
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## Utilización de Key Protect
{: #java-examples-kp}
Key Protect se puede añadir a un grupo de almacenamiento para cifrar los datos confidenciales en reposo en la nube.

### Antes de empezar
{: #java-examples-kp-prereqs}

Se necesitan los elementos siguientes para crear un grupo con Key Protect habilitado:

* Un servicio de Key Protect [suministrado](/docs/services/key-protect?topic=key-protect-provision#provision)
* Una clave raíz disponible (ya sea [generada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperación del CRN de la clave raíz
{: #java-examples-kp-root}

1. Recupere el [ID de instancia](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) del servicio Key Protect
2. Utilice la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas las [claves disponibles](https://cloud.ibm.com/apidocs/key-protect)
    * Puede utilizar mandatos `curl` o un cliente de API REST, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acceder a la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere el CRN de la clave raíz que utilizará para activar Key Protect en el grupo. El CRN se parecerá al siguiente:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creación de un grupo con Key Protect habilitado
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
*Valores de clave*
* `<algorithm>`: el algoritmo de cifrado utilizado para los nuevos objetos que se añaden al grupo (el valor predeterminado es AES256).
* `<root-key-crn>`: el CRN de la clave raíz obtenido del servicio Key Protect.

*Referencias del SDK*
* Clases
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* Métodos
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Nuevas cabeceras para Key Protect
{: #java-examples-kp-headers}

Se han definido cabeceras adicionales dentro de la clase `Headers`:

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

La misma sección de la implementación para crear un grupo que ya añade cabeceras de instancia de servicio de IAM añadirá 2 nuevas cabeceras de cifrado:

```java
//Añadir id de instancia de servicio de IBM y cifrado a las cabeceras
if ((null != this.awsCredentialsProvider ) && (this.awsCredentialsProvider.getCredentials() instanceof IBMOAuthCredentials)) {
    IBMOAuthCredentials oAuthCreds = (IBMOAuthCredentials)this.awsCredentialsProvider.getCredentials();
    if (oAuthCreds.getServiceInstanceId() != null) {
        request.addHeader(Headers.IBM_SERVICE_INSTANCE_ID, oAuthCreds.getServiceInstanceId());
        request.addHeader(Headers.IBM_SSE_KP_ENCRYPTION_ALGORITHM, createBucketRequest.getEncryptionType().getKpEncryptionAlgorithm());
        request.addHeader(Headers.IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN, createBucketRequest.getEncryptionType().getIBMSSEKPCustomerRootKeyCrn());
    }
}
```

Los objetos `ObjectListing` y `HeadBucketResult` se han actualizado para que incluyan las variables de valor booleano `IBMSSEKPEnabled` y de valor de serie `IBMSSEKPCustomerRootKeyCrn` con los métodos getter y setter. Estas variables guardarán los valores de las nuevas cabeceras.

#### GET bucket
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

La clase `ObjectListing` necesitará 2 métodos adicionales:

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

Se han definido cabeceras adicionales dentro de la clase `Headers`:

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

La cabecera S3XmlResponseHandler, responsable de ejecutar unmarshall de todas las respuestas xml. Se ha añadido una comprobación que indica que el resultado es una instancia de `ObjectListing` y las cabeceras recuperadas se añadirán al objeto `ObjectListing`:

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

#### HEAD bucket
{: #java-examples-kp-head}
Se han definido cabeceras adicionales dentro de la clase Headers:

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

Estas variables se llenan en HeadBucketResponseHandler.

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Utilización de la transferencia de alta velocidad de Aspera
{: #java-examples-aspera}

Si instala la [biblioteca de transferencia de alta velocidad de Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), puede utilizar transferencias de archivos de alta velocidad dentro de la aplicación. La biblioteca de Aspera es de origen cerrado y, por lo tanto, una dependencia opcional para el SDK de COS (que utiliza una licencia de Apache). 

Cada sesión de transferencia de alta velocidad de Aspera genera un proceso `ascp` individual que se ejecuta en la máquina cliente para realizar la transferencia. Asegúrese de que su entorno permita ejecutar dicho proceso.
{:tip}

Necesitará instancias de las clases S3 Client e IAM Token Manager para inicializar `AsperaTransferManager`. `s3Client` se necesita para obtener información de conexión de FASP para el grupo de destino de COS. `tokenManager` se necesita para permitir que el SDK de transferencia de alta velocidad de Aspera se autentique con el grupo de destino de COS.

### Inicialización de `AsperaTransferManager`
{: #java-examples-aspera-init}

Antes de inicializar `AsperaTransferManager`, asegúrese de que tiene objetos [`s3Client`](#java-examples-config) y [`tokenManager`](#java-examples-config) en funcionamiento. 

Utilizar una sola sesión de transferencia de alta velocidad de Aspera no supone una gran ventaja, a no ser que espere una reducción significativa del ruido y de la pérdida de paquetes en la red. Por lo tanto, tenemos que indicar a `AsperaTransferManager` que utilice varias sesiones utilizando la clase `AsperaConfig`. Esto dividirá la transferencia entre varias **sesiones** paralelas que enviarán fragmentos de datos cuyo tamaño está definido por el valor **threshold**.

La configuración típica para utilizar la multisesión sería la siguiente:
* Tasa objetivo de 2500 MBps
* Umbral de 100 MB (*este es el valor recomendado para la mayoría de aplicaciones*)

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
En el ejemplo anterior, el sdk abarcará sesiones suficientes para intentar alcanzar la tasa objetivo de 2500 MBps.

Como alternativa, se puede configurar de forma explícita la gestión de sesiones en el sdk. Esto resulta útil en los casos en los que se desea un control más preciso sobre la utilización de la red.

La configuración típica para utilizar la multisesión explícita sería la siguiente:
* 2 o 10 sesiones
* Umbral de 100 MB (*este es el valor recomendado para la mayoría de aplicaciones*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

Para obtener un rendimiento óptimo en la mayoría de los escenarios, utilice siempre varias sesiones para minimizar cualquier sobrecarga asociada con la creación de una instancia de una transferencia de alta velocidad de Aspera. **Si la capacidad de red es como mínimo de 1 Gbps, debe utilizar 10 sesiones.**  Las redes de ancho de banda inferior deben utilizar dos sesiones.
{:tip}

*Valores de clave*
* `API_KEY`: una clave de API para un ID de usuario o de servicio con roles de Escritor o Gestor

Deberá proporcionar una clave de API de IAM para crear un `AsperaTransferManager`. Las [credenciales de HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} **NO** reciben soporte actualmente. Para obtener más información acerca de IAM, [pulse aquí](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

### Carga de archivos
{: #java-examples-aspera-upload}

```java
String filePath = "<absolute-path-to-source-data>";
String bucketName = "<bucket-name>";
String itemName = "<item-name>";

// Cargar archivo
File inputFile = new File(filePath);

// Crear AsperaTransferManager para carga FASP
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client).build();

// Cargar archivo de prueba y mostrar progreso
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.upload(bucketName, itemName, inputFile);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera.
* `<absolute-path-to-source-data>`: directorio y nombre de archivo que se va a cargar en Object Storage.
* `<item-name>`: nombre del nuevo objeto añadido al grupo.

### Descarga de archivos
{: #java-examples-aspera-download}

```java
String bucketName = "<bucket-name>";
String outputPath = "<absolute-path-to-file>";
String itemName = "<item-name>";

// Crear archivo local
File outputFile = new File(outputPath);
outputFile.createNewFile();

// Crear AsperaTransferManager para descarga FASP
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Descargar archivo
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.download(bucketName, itemName, outputFile);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera.
* `<absolute-path-to-file>`: directorio y nombre de archivo que se va a guardar de Object Storage.
* `<item-name>`: nombre del objeto en el grupo.

### Carga de directorios
{: #java-examples-aspera-upload-directory}

```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

// Cargar directorio
File inputDirectory = new File(directoryPath);

// Crear AsperaTransferManager para carga FASP
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Cargar directorio de prueba
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.uploadDirectory(bucketName, directoryPrefix, inputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera.
* `<absolute-path-to-directory>`: directorio de los archivos que se van a cargar en Object Storage.
* `<virtual-directory-prefix>`: nombre del prefijo de directorio que se va a añadir a cada archivo durante la carga. Utilice una serie nula o vacía para cargar los archivos en la raíz del grupo.

### Descarga de directorios
{: #java-examples-aspera-download-directory}
```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

// Cargar directorio
File outputDirectory = new File(directoryPath);

// Crear AsperaTransferManager para descarga FASP
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();

// Descargar directorio de prueba
Future<AsperaTransaction> asperaTransactionFuture   = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera.
* `<absolute-path-to-directory>`: directorio en el que se van a guardar los archivos descargados de Object Storage.
* `<virtual-directory-prefix>`: nombre del prefijo de directorio de cada archivo que se va a descargar. Utilice una serie nula o vacía para descargar todos los archivos del grupo.

### Modificación de la configuración de sesión por transferencia
{: #java-examples-aspera-config}

Puede modificar los valores de configuración multisesión por transferencia pasando una instancia de `AsperaConfig` a los métodos de carga y descarga utilizados. Con `AsperaConfig` puede especificar el número de sesiones y el tamaño mínimo de transferencia de archivos por sesión. 

```java
String bucketName = "<bucket-name>";
String filePath = "<absolute-path-to-file>";
String itemName = "<item-name>";

// Cargar archivo
File inputFile = new File(filePath);

// Crear AsperaTransferManager para carga FASP
AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
.withTokenManager(TOKEN_MANAGER)
.withAsperaConfig(asperaConfig)
.build();

// Crear AsperaConfig para establecer el número de sesiones
// y el umbral de archivos por sesión.
AsperaConfig asperaConfig = new AsperaConfig().
withMultiSession(10).
withMultiSessionThresholdMb(100);

// Cargar archivo de prueba y mostrar progreso
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.upload(bucketName, itemName, inputFile, asperaConfig, null);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

### Supervisión del progreso de la transferencia
{: #java-examples-aspera-monitor}

La forma más sencilla de supervisar el progreso de las transferencias de archivos o directorios consiste en utilizar la propiedad `isDone()`, que devuelve `true` cuando la transferencia se haya completado.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pausa de 3 segundos
    Thread.sleep(1000 * 3);
}
```

También puede comprobar si una transferencia está en cola para procesarse llamando al método `onQueue` en `AsperaTransaction`. `onQueue` devolverá un valor booleano; `true` indica que la transferencia está en cola.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pausa de 3 segundos
    Thread.sleep(1000 * 3);
}
```

Para comprobar si una transferencia está en curso, llame al método de progreso en `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pausa de 3 segundos
    Thread.sleep(1000 * 3);
}
```

De forma predeterminada, cada transferencia tiene un `TransferProgress` conectado. `TransferProgress` mostrará el número total de bytes transferidos y el porcentaje transferido del total de bytes que hay que transferir. Para acceder a `TransferProgress` de una transferencia, utilice el método `getProgress` en `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pausa de 3 segundos
    Thread.sleep(1000 * 3);
}
```

Para ver sobre el número de bytes transferidos, llame al método `getBytesTransferred` en `TransferProgress`. Para ver el porcentaje transferido del número total de bytes a transferir, llame al método `getPercentTransferred` en `TransferProgress`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    System.out.println("Bytes transferred: " + transferProgress.getBytesTransferred());
    System.out.println("Percent transferred: " + transferProgress.getPercentTransferred());


    //pausa de 3 segundos
    Thread.sleep(1000 * 3);
}
```

### Pausa/Reanudación/Cancelación
{: #java-examples-aspera-pause}

El SDK proporciona la capacidad de gestionar el progreso de las transferencias de archivos/directorios mediante los métodos siguientes del objeto `AsperaTransfer`:

* `pause()`
* `resume()`
* `cancel()`

No hay efectos secundarios de llamar a ninguno de los métodos mostrados anteriormente. El SDK se encarga de realizar la limpieza correspondiente.
{:tip}

El ejemplo siguiente muestra un posible uso de estos métodos:

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

//descargar el directorio de cloud storage
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

int pauseCount = 0;

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download in progress...");

    //pausar la transferencia
    asperaTransfer.pause();

    //reanudar la transferencia
    asperaTransfer.resume();

    //cancelar la transferencia
    asperaTransfer.cancel();
}

System.out.println("Directory download complete!");
```

### Resolución de problemas de Aspera
{: #java-examples-aspera-ts}

**Problema:** los desarrolladores que utilizan el JDK de Oracle en Linux o Mac OS X pueden experimentar caídas inesperadas y silenciosas durante las transferencias

**Causa:** el código nativo necesita sus propios manejadores de señales, que pueden haber sobrescrito los manejadores de señales de JVM. Es posible que sea necesario utilizar el recurso de encadenamiento de señales de JVM.

*No se ven afectados los usuarios de IBM&reg; JDK ni los usuarios de Microsoft&reg; Windows.*

**Solución:** enlace y cargue la biblioteca de encadenamiento de señales de JVM.
* En Linux, localice la biblioteca compartida `libjsig.so` y defina la siguiente variable de entorno:
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* En Mac OS X, localice la biblioteca compartida `libjsig.dylib` y defina las siguientes variables de entorno:
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

Consulte la [documentación de Oracle&reg; JDK](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window} para obtener más información sobre el encadenamiento de señales.

**Problema:** `UnsatisfiedLinkError` en Linux

**Causa:** el sistema no puede cargar las bibliotecas dependientes. En los registros de la aplicación se pueden ver errores como los siguientes:

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**Solución:** defina la siguiente variable de entorno:

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
## Actualización de metadatos
{: #java-examples-metadata}
Hay dos formas de actualizar los metadatos de un objeto existente:
* Una solicitud `PUT` con los nuevos metadatos y el contenido del objeto original
* Ejecución de una solicitud `COPY` con los nuevos metadatos que especifican el objeto original como origen de la copia

### Utilización de PUT para actualizar metadatos
{: #java-examples-metadata-put}

**Nota:** la solicitud `PUT` sobrescribe el contenido existente del objeto, por lo que primero se debe descargar y volver a cargar con los nuevos metadatos

```java
public static void updateMetadataPut(String bucketName, String itemName, String key, String value) throws IOException {
    System.out.printf("Updating metadata for item: %s\n", itemName);

    //recuperar el elemento existente para volver a cargar el contenido
    S3Object item = _cos.getObject(new GetObjectRequest(bucketName, itemName));
    S3ObjectInputStream itemContents = item.getObjectContent();

    //leer el contenido del elemento para definir la longitud del contenido y crear una copia
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    int b;
    while ((b = itemContents.read()) != -1) {
        output.write(b);
    }

    int contentLength = output.size();
    InputStream itemCopy = new ByteArrayInputStream(output.toByteArray());

    //definir los nuevos metadatos
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

### Utilización de COPY para actualizar metadatos
{: #java-examples-metadata-copy}

```java
public static void updateMetadataCopy(String bucketName, String itemName, String key, String value) {
    System.out.printf("Updating metadata for item: %s\n", itemName);

    //definir los nuevos metadatos
    HashMap<String, String> userMetadata = new HashMap<String, String>();
    userMetadata.put(key, value);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setUserMetadata(userMetadata);     

    //definir el origen de la copia en sí mismo
    CopyObjectRequest req = new CopyObjectRequest(bucketName, itemName, bucketName, itemName);
    req.setNewObjectMetadata(metadata);

    _cos.copyObject(req);

    System.out.printf("Updated metadata for item %s from bucket %s\n", itemName, bucketName);
}
```

## Utilización de Immutable Object Storage
{: #java-examples-immutable}

### Adición de una configuración de protección a un grupo existente
{: #java-examples-immutable-enable}

Esta implementación de la operación `PUT` utiliza el parámetro de consulta `protection` para definir los parámetros de retención para un grupo existente. Esta operación le permite establecer o cambiar el periodo de retención mínimo, predeterminado y máximo. Esta operación también le permite cambiar el estado de protección del grupo. 

Los objetos que se escriben en un grupo protegido no se pueden suprimir hasta que transcurre el periodo de protección y se eliminan todas las retenciones legales sobre el objeto. Se proporciona el valor de retención predeterminado del grupo a un objeto a menos que se proporcione un valor específico de objeto cuando se crea el objeto. Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto. 

Los valores mínimos y máximos admitidos para los valores de periodo de retención `MinimumRetention`, `DefaultRetention` y `MaximumRetention` son de 0 días y 365243 días (1000 años) respectivamente. 

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

### Comprobación de la protección sobre un grupo
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

### Carga de un objeto protegido
{: #java-examples-immutable-upload}

Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.

|Valor	| Tipo	| Descripción |
| --- | --- | --- | 
|`Retention-Period` | Entero no negativo (segundos) | Periodo de retención para almacenar el objeto en segundos. El objeto no se puede sobrescribir ni suprimir hasta que transcurre el periodo de tiempo especificado en el período de retención. Si se especifica este campo y `Retention-Expiration-Date`, se devuelve el error `400`. Si no se especifica ninguno de los dos, se utiliza el periodo `DefaultRetention` del grupo. Cero (`0`) es un valor válido, siempre y cuando el periodo mínimo de retención del grupo también sea `0`. |
| `Retention-expiration-date` | Fecha (formato ISO 8601) | Fecha en la que se podrá suprimir o modificar el objeto. Solo puede especificar esta cabecera o la cabecera Retention-Period. Si se especifican ambas, se devuelve el error `400`. Si no se especifica ninguna de los dos, se utiliza el periodo DefaultRetention del grupo. |
| `Retention-legal-hold-id` | serie | Una sola retención legal que se aplicará al objeto. Una retención legal es una serie larga de caracteres Y. El objeto no se puede sobrescribir ni suprimir hasta que se eliminen todas las retenciones legales asociadas con el objeto. |

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

### Adición o eliminación de una retención legal de un objeto protegido
{: #java-examples-immutable-legal-hold}

El objeto da soporte a 100 retenciones legales:

*  Un identificador de retención legal es una serie de caracteres con una longitud máxima de 64 caracteres y una longitud mínima de 1 carácter. Los caracteres válidos son letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` y `.
* Si la adición de la retención legal especificada supera las 100 retenciones legales en total del objeto, la nueva retención legal no se añade y se devuelve el error `400`.
* Si un identificador es demasiado largo, no se añade al objeto y se devuelve el error `400`.
* Si un identificador contiene caracteres no válidos, no se añade al objeto y se devuelve el error `400`.
* Si un identificador ya se está utilizando sobre un objeto, la retención legal existente no se modifica y la respuesta indica que el identificador ya se está utilizando con el error `409`.
* Si un objeto no tiene metadatos de periodo de retención, se devuelve el error `400` y no se permite añadir ni eliminar una retención legal.

Es necesario que haya una cabecera de periodo de retención; de lo contrario, se devuelve el error `400`.
{: http}

El usuario que añade o elimina una retención legal debe tener el permiso de `Gestor` sobre el grupo.

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

### Ampliación del periodo de retención de un objeto protegido
{: #java-examples-immutable-extend}

El periodo de retención de un objeto solo se puede ampliar. No se puede reducir con respecto al valor configurado actualmente.

El valor de ampliación de retención se establece de una de las tres maneras siguientes:

* tiempo adicional a partir del valor actual (`Additional-Retention-Period` o un método similar)
* nuevo periodo de ampliación en segundos (`Extend-Retention-From-Current-Time` o un método similar)
* nueva fecha de caducidad de retención del objeto (`New-Retention-Expiration-Date` o método similar)

El periodo de retención actual almacenado en los metadatos de objeto se incrementa en el tiempo adicional especificado o bien se sustituye por el nuevo valor, en función del parámetro establecido en la solicitud `extendRetention`. En cualquiera de los casos, el parámetro de retención de ampliación se comprueba con respecto al periodo de retención actual y el parámetro ampliado solo se acepta si el periodo de retención actualizado es mayor que el periodo de retención actual.

Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.

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

### Obtención de una lista de las retenciones legales sobre un objeto protegido
{: #java-examples-immutable-list-holds}

Esta operación devuelve:

* Fecha de creación del objeto
* Periodo de retención del objeto en segundos
* Fecha de caducidad de retención calculada en función del periodo y de la fecha de creación
* Lista de retenciones legales
* Identificador de retención legal
* Indicación de fecha y hora en que se ha aplicado la retención legal

Si no hay ninguna retención legal sobre el objeto, se devuelve un `LegalHoldSet` vacío.
Si no se ha especificado ningún periodo de retención sobre el objeto, se devuelve el error `404`.

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
