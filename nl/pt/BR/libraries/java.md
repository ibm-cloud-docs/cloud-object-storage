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

# Usando Java
{: #java}

O {{site.data.keyword.cos_full}} SDK for Java é abrangente e tem recursos não descritos neste guia. Para obter a documentação detalhada de classe o método, [consulte o Javadoc](https://ibm.github.io/ibm-cos-sdk-java/). O código-fonte pode ser localizado no [Repositório GitHub](https://github.com/ibm/ibm-cos-sdk-java).

## Obtendo o SDK
{: #java-install}

A maneira mais fácil de consumir o {{site.data.keyword.cos_full_notm}} Java SDK é usar o Maven para gerenciar dependências. Se você não estiver familiarizado com o Maven, poderá colocá-lo em funcionamento usando o guia [Maven em 5 minutos](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).


O Maven usa um arquivo chamado `pom.xml` para especificar as bibliotecas (e suas versões) necessárias para um projeto Java. Aqui está um exemplo de arquivo `pom.xml` para usar o {{site.data.keyword.cos_full_notm}} Java SDK para conexão com o {{site.data.keyword.cos_short}}.


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

## Migrando do 1.x.x
{: #java-migrate}

A liberação 2.0 do SDK introduz uma mudança de namespace que permite que um aplicativo faça uso da biblioteca AWS original para se conectar aos recursos do AWS no mesmo aplicativo ou ambiente. Para migrar de 1.x para 2.0, algumas mudanças são necessárias:

1. Atualize usando o Maven, mudando todas as identificações de versão de dependência `ibm-cos-java-sdk` para `2.0.0` no pom.xml. Verifique se não há dependências do módulo do SDK no pom.xml com uma versão anterior a `2.0.0`.
2. Atualize quaisquer declarações de importação de `amazonaws` para `ibm.cloud.objectstorage`.


## Criando um cliente e credenciais de fornecimento
{: #java-credentials}

No exemplo a seguir, um cliente `cos` é criado e configurado fornecendo informações de credenciais (Chave de API e ID da instância de serviço). Esses valores também podem ser originados automaticamente de um arquivo de credenciais ou de variáveis de ambiente.

Depois de gerar uma [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), o documento JSON resultante pode ser salvo em `~/.bluemix/cos_credentials`. O SDK originará automaticamente as credenciais desse arquivo, a menos que outras credenciais sejam explicitamente configuradas durante a criação do cliente. Se o arquivo `cos_credentials` contiver chaves HMAC, o cliente será autenticado com uma assinatura, caso contrário, o cliente usará a chave de API fornecida para autenticar usando um token de acesso.

Se estiver migrando do AWS S3, também será possível originar os dados de credenciais de `~/.aws/credentials` no formato:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Se ambos, `~/.bluemix/cos_credentials` e `~/.aws/credentials`, existirem, `cos_credentials` terá a preferência.

 Para obter mais detalhes sobre a construção do cliente, [consulte o Javadoc](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html).

## Exemplos de código
{: #java-examples}

Vamos iniciar com uma classe de exemplo completa que será executada por meio de alguma funcionalidade básica, em seguida, explorar as classes individualmente. Essa classe `CosExample` listará objetos em um depósito existente, criará um novo depósito e, em seguida, listará todos os depósitos na instância de serviço. 

### Reunir informações necessárias
{: #java-examples-prereqs}

* `bucketName` e `newBucketName` são sequências [exclusivas e protegidas por DNS](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Como os nomes dos depósitos são exclusivos em todo o sistema, esses valores precisarão ser mudados se este exemplo for executado múltiplas vezes. Observe que os nomes são reservados por 10 a 15 minutos após a exclusão.
* `api_key` é o valor localizado na [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `apikey`.
* `service_instance_id` é o valor localizado na [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `resource_instance_id`. 
* `endpoint_url` é uma URL de terminal em serviço, inclusive do protocolo `https://`. Esse **não** é o valor `endpoints` localizado na [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `storageClass` é um [código de fornecimento válido](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) que corresponde ao valor `endpoint`. Isso é, então, usado como a variável `LocationConstraint` da API S3.
* `location` deve ser configurado como a parte de local do `storageClass`. Para `us-south-standard`, esse seria `us-south`. Essa variável é usada somente para o cálculo de [assinaturas HMAC](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac), mas é necessária para qualquer cliente, incluindo este exemplo que usa uma chave de API do IAM.

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
                    .withEndpointConfiguration (new EndpointConfiguration (endpoint_url, location)) .withPathStyleAccessEnabled (true)
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

### Inicializando a configuração
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

    AmazonS3 cos = AmazonS3ClientBuilder.standard( ).withCredentials(new AWSStaticCredentialsProvider (credentials))
            .withEndpointConfiguration (new EndpointConfiguration (endpoint_url, location)) .withPathStyleAccessEnabled (true)
            .withClientConfiguration(clientConfig).build();

    return cos;
}
```

*Valores da chave*
* `<endpoint>` - o terminal público para seu armazenamento de objeto de nuvem (disponível por meio do [IBM Cloud Dashboard](https://cloud.ibm.com/resources){:new_window}). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - a chave de api gerada ao criar as credenciais de serviço (o acesso de gravação é necessário para exemplos de criação e exclusão)
* `<resource-instance-id>` - o ID do recurso para seu armazenamento de objeto de nuvem (disponível por meio da [CLI do IBM Cloud](/docs//docs/cli?topic=cloud-cli-idt-cli) ou do [IBM Cloud Dashboard](https://cloud.ibm.com/resources){:new_window})
* `<location>` - o local padrão para seu armazenamento de objeto de nuvem (deve corresponder à região usada para `<endpoint>`)

*Referências do SDK*
* Classes
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### Determinando o terminal
{: #java-examples-endpoint}

Os métodos a seguir podem ser usados para determinar o terminal em serviço com base no local do depósito, tipo de terminal (público ou privado) e região específica (opcional). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

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

### Criando um novo depósito
{: #java-examples-new-bucket}

```java
public static void createBucket (String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### Criar um depósito com uma classe de armazenamento diferente
{: #java-examples-storage-class}

Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*Referências do SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### Criando um novo arquivo de texto
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
Observe que, ao incluir metadados customizados em um objeto, é necessário criar um objeto `ObjectMetadata` usando o SDK e não enviar manualmente um cabeçalho customizado contendo `x-amz-meta-{key}`. O último pode causar problemas ao autenticar usando credenciais HMAC.
{: .tip}

### Fazer upload do objeto de um arquivo
{: #java-examples-upload}

Este exemplo supõe que o depósito `sample` já existe.

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### Fazer upload do objeto usando um fluxo
{: #java-examples-stream}

Este exemplo supõe que o depósito `sample` já existe.

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

### Fazer download do objeto para um arquivo
{: #java-examples-download}

Este exemplo supõe que o depósito `sample` já existe.

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


### Fazer download do objeto usando um fluxo
{: #java-examples-download-stream}

Este exemplo supõe que o depósito `sample` já existe.

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### Copiar objetos
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

*Referências do SDK*
* Classes
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* Métodos
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### Exceção de putObject
{: #java-examples-put-exception}

O método putObject pode lançar a exceção a seguir, mesmo se o novo upload do objeto foi bem-sucedido
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

**Causa raiz:** as APIs JAXB são consideradas como APIs do Java EE e não estão mais contidas no caminho de classe padrão no Java SE 9.

**Correção:** inclua a entrada a seguir no arquivo pom.xml em sua pasta de projeto e reempacote seu projeto
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### Listar depósitos disponíveis
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
*Referências do SDK*
* Classes
    * [ Depósito ](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* Métodos
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### Listar itens em um depósito (v2)
{: #java-examples-list-objects-v2}

O objeto [AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} contém um método atualizado para listar o conteúdo ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}). Esse método permite limitar o número de registros retornados e recuperar os registros em lotes. Isso pode ser útil para paginação de seus resultados em um aplicativo e melhorar o desempenho.

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

*Referências do SDK*
* Classes
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Métodos
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### Listar itens em um depósito (v1)
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

*Referências do SDK*
* Classes
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* Métodos
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### Obter conteúdo do arquivo de um item específico
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

*Referências do SDK*
* Classes
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* Métodos
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### Excluir um item de um depósito
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*Referências do SDK*
* Métodos
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### Excluir múltiplos itens de um depósito
{: #java-examples-delete-objects}

A solicitação de exclusão pode conter um máximo de 1000 chaves que você deseja excluir. Embora isso seja muito útil na redução da sobrecarga por solicitação, fique atento ao excluir um grande número de chaves. Além disso, leve em conta os tamanhos dos objetos para assegurar o desempenho adequado.
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

*Referências do SDK*
* Classes
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* Métodos
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### Excluir um depósito
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*Referências do SDK*
* Métodos
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### Verificar se um objeto é legível publicamente
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

*Referências do SDK*
* Classes
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Concessão](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* Métodos 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### Executar um upload de múltiplas partes
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
*Referências do SDK*
* Classes
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

## Fazer upload de objetos maiores usando um Transfer Manager
{: #java-examples-transfer-manager}

O `TransferManager` simplifica as grandes transferências de arquivos, incorporando automaticamente uploads de múltiplas partes sempre que necessário ao configurar parâmetros de configuração.

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
        .build ();

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

*Referências do SDK*
* Classes
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Fazer upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* Métodos
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [Fazer upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## Usando o Key Protect
{: #java-examples-kp}
O Key Protect pode ser incluído em um depósito de armazenamento para criptografar dados sensíveis em repouso na nuvem.

### Antes de iniciar
{: #java-examples-kp-prereqs}

Os itens a seguir são necessários para criar um depósito com o Key-Protect ativado:

* Um serviço Key Protect [provisionado](/docs/services/key-protect?topic=key-protect-provision#provision)
* Uma chave Raiz disponível ([gerada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperando o CRN da chave raiz
{: #java-examples-kp-root}

1. Recupere o [ID da instância](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) para seu serviço Key Protect
2. Use a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas as suas [chaves disponíveis](https://cloud.ibm.com/apidocs/key-protect)
    * É possível usar comandos `curl` ou um Cliente REST de API, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acessar a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere o CRN da chave raiz que você usará para ativar o Key Protect no seu depósito. O CRN será semelhante ao abaixo:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Criando um depósito com o Key-Protect ativado
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
*Valores da chave*
* `<algorithm>` - o algoritmo de criptografia usado para novos objetos incluídos no depósito (o padrão é AES256).
* `<root-key-crn>` - o CRN da Chave raiz obtida do serviço Key Protect.

*Referências do SDK*
* Classes
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* Métodos
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Novos cabeçalhos para o Key Protect
{: #java-examples-kp-headers}

Os cabeçalhos adicionais foram definidos na classe `Headers`:

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

A mesma seção da implementação do depósito de criação que já inclui os cabeçalhos da instância de serviço do IAM incluirá os 2 novos cabeçalhos de criptografia:

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

Os objetos `ObjectListing` e `HeadBucketResult` foram atualizados para incluir as variáveis booleanas `IBMSSEKPEnabled` e de sequência `IBMSSEKPCustomerRootKeyCrn` com métodos getter e setter. Eles armazenarão os valores dos novos cabeçalhos.

#### GET depósito
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

A classe `ObjectListing` requererá 2 métodos adicionais:

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

Os cabeçalhos adicionais foram definidos na classe `Headers`:

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

O S3XmlResponseHandler que é responsável por desserializar todas as respostas xml. Uma verificação foi incluída de que o resultado é uma instância de `ObjectListing` e os cabeçalhos recuperados serão incluídos no objeto `ObjectListing`:

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

#### HEAD depósito
{: #java-examples-kp-head}
Os cabeçalhos adicionais foram definidos na classe Headers:

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

Estas variáveis são preenchidas no HeadBucketResponseHandler.

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Usando o Aspera High-Speed Transfer
{: #java-examples-aspera}

Instalando a [biblioteca do Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), é possível utilizar as transferências de arquivos de alta velocidade em seu aplicativo. A biblioteca Aspera é de origem fechada e, portanto, uma dependência opcional para o SDK do COS (que usa uma licença do Apache). 

Cada sessão do Aspera high-speed transfer gera um processo `ascp` individual que é executado na máquina do cliente para executar a transferência. Assegure-se de que seu ambiente de computação possa permitir que esse processo seja executado.
{:tip}

Você precisará de instâncias das classes do S3 Client e do IAM Token Manager para inicializar o `AsperaTransferManager`. O `s3Client` é necessário para obter informações de conexão do FASP para o depósito de destino do COS. O `tokenManager` é necessário para permitir que o SDK do Aspera high-speed transfer seja autenticado com o depósito de destino do COS.

### Inicializando o `AsperaTransferManager`
{: #java-examples-aspera-init}

Antes de inicializar o `AsperaTransferManager`, certifique-se de que você tenha trabalhado com objetos [`s3Client`](#java-examples-config) e [`tokenManager`](#java-examples-config). 

Não há muito benefício em usar uma única sessão do Aspera high-speed transfer, a menos que você espere ver um ruído significativo ou perda de pacote na rede. Portanto, é necessário informar ao `AsperaTransferManager` para usar múltiplas sessões usando a classe `AsperaConfig`. Isso dividirá a transferência em um número de **sessões** paralelas que enviam chunks de dados cujo tamanho é definido pelo valor do **limite**.

A configuração típica para usar múltiplas sessões deve ser:
* Taxa de destino de 2500 MBps
* Limite de 100 MB (*esse é o valor recomendado para a maioria dos aplicativos*)

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
    .build ();
```
No exemplo acima, o sdk gerará sessões suficientes para tentar atingir a taxa de destino de 2500 MBps.

Como alternativa, o gerenciamento de sessões pode ser configurado explicitamente no sdk. Isso é útil em casos em que o controle mais preciso sobre a utilização de rede é desejado.

A configuração típica para usar múltiplas sessões explícitas deve ser:
* 2 ou 10 sessões
* Limite de 100 MB (*esse é o valor recomendado para a maioria dos aplicativos*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build ();
```

Para obter melhor desempenho na maioria dos cenários, sempre use múltiplas sessões para minimizar qualquer sobrecarga associada à instanciação de um Aspera high-speed transfer. **Se sua capacidade de rede for pelo menos 1 Gbps, será necessário usar 10 sessões.** As redes de largura da banda inferior devem usar duas sessões.
{:tip}

*Valores da chave*
* `API_KEY` - uma chave de API para um ID de usuário ou de serviço com funções de Gravador ou Gerenciador

Será necessário fornecer uma Chave de API do IAM para construir um `AsperaTransferManager`. As [Credenciais HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} **NÃO** são suportadas atualmente. Para obter mais informações sobre o IAM, [clique aqui](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

### Upload de arquivo
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

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado.
* `<absolute-path-to-source-data>` - o diretório e nome do arquivo para fazer upload para o Object Storage.
* `<item-name>` - o nome do novo objeto incluído no depósito.

### Download de arquivo
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
    .build ();

// Download file
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.download(bucketName, itemName, outputFile);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado.
* `<absolute-path-to-file>` - o diretório e nome do arquivo para salvar do Object Storage.
* `<item-name>` - o nome do objeto no depósito.

### Upload de diretório
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
    .build ();

// Upload test directory
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.uploadDirectory(bucketName, directoryPrefix, inputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado.
* `<absolute-path-to-directory>` - o diretório dos arquivos a serem transferidos por upload para o Object Storage.
* `<virtual-directory-prefix>` - o nome do prefixo de diretório a ser incluído em cada arquivo após o upload. Use a sequência nula ou vazia para fazer upload dos arquivos para a raiz do depósito.

### Download de diretório
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
    .build ();

// Download test directory
Future<AsperaTransaction> asperaTransactionFuture   = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado.
* `<absolute-path-to-directory>` - o diretório para salvar arquivos transferidos por download do Object Storage.
* `<virtual-directory-prefix>` - o nome do prefixo de diretório de cada arquivo a ser transferido por download. Use uma sequência nula ou vazia para fazer download de todos os arquivos no depósito.

### Substituindo a configuração de sessão em uma Base por transferência
{: #java-examples-aspera-config}

É possível substituir os valores de configuração de múltiplas sessões em uma base por transferência passando uma instância de `AsperaConfig` para os métodos de upload e download sobrecarregados. Usando `AsperaConfig`, é possível especificar o número de sessões e o tamanho mínimo do limite de arquivo por sessão. 

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
.build ();

// Create AsperaConfig to set number of sessions
// and file threshold per session.
AsperaConfig asperaConfig = new AsperaConfig().
withMultiSession(10).
withMultiSessionThresholdMb(100);

// Upload test file and report progress
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.upload(bucketName, itemName, inputFile, asperaConfig, null);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();
```

### Monitorando o progresso de transferência
{: #java-examples-aspera-monitor}

A maneira mais simples de monitorar o progresso de suas transferências de arquivo/diretório é usar a propriedade `isDone()` que retorna `true` quando sua transferência é concluída.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Também é possível verificar se uma transferência está enfileirada para processamento chamando o método `onQueue` no `AsperaTransaction`. O `onQueue` retornará um Booleano com `true` indicando que a transferência está enfileirada.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Para verificar se uma transferência está em andamento, chame o método de progresso em `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Cada transferência por padrão terá um `TransferProgress` anexado a ela. O `TransferProgress` relatará o número de bytes transferidos e a porcentagem transferida do total de bytes para transferência. Para acessar o `TransferProgress` de uma transferência, use o método `getProgress` em `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Para relatar o número de bytes transferidos, chame o método `getBytesTransferred` em `TransferProgress`. Para relatar a porcentagem transferida do total de bytes a serem transferidos, chame o método `getPercentTransferred` em `TransferProgress`.

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

### Pausar/Continuar/Cancelar
{: #java-examples-aspera-pause}

O SDK fornece a capacidade de gerenciar o progresso de transferências de arquivos/diretórios por meio dos métodos a seguir do objeto `AsperaTransfer`:

* `pause()`
* `resume()`
* `cancel()`

Não há efeitos colaterais de chamar qualquer um dos métodos descritos acima. A limpeza e a manutenção adequadas são manipuladas pelo SDK.
{:tip}

O exemplo a seguir mostra um possível uso para esses métodos:

```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory>";
String directoryPrefix = "<virtual-directory-prefix>";
boolean includeSubDirectories = true;

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(COS_API_KEY_ID, _cos)
    .withTokenManager(TOKEN_MANAGER)
    .build ();

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

### Resolução de problemas do Aspera
{: #java-examples-aspera-ts}

**Problema:** os desenvolvedores que usam o Oracle JDK no Linux ou Mac OS X podem sofrer travamentos inesperados e silenciosos durante as transferências

**Causa:** o código nativo requer seus próprios manipuladores de sinal que podem estar substituindo os manipuladores de sinal da JVM. Pode ser necessário usar o recurso de encadeamento de sinal da JVM.

*Os usuários do IBM&reg; JDK ou os usuários do Microsoft&reg; Windows não são afetados.*

**Solução:** vincule e carregue a biblioteca de encadeamento de sinal da JVM.
* No Linux, localize a biblioteca compartilhada `libjsig.so` e configure a variável de ambiente a seguir:
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* No Mac OS X, localize a biblioteca compartilhada `libjsig.dylib` e configure as variáveis de ambiente a seguir:
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

Visite a [documentação do Oracle&reg; JDK](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window} para obter mais informações sobre o encadeamento de sinais.

**Problema:** `UnsatisfiedLinkError` no Linux

**Causa:** o sistema não é capaz de carregar bibliotecas dependentes. Erros como os a seguir podem ser vistos nos logs do aplicativo:

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**Solução:** configure a variável de ambiente a seguir:

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
## Atualizando os metadados
{: #java-examples-metadata}
Há duas maneiras de atualizar os metadados em um objeto existente:
* Uma solicitação `PUT` com os novos metadados e o conteúdo do objeto original
* Executando uma solicitação de `COPY` com os novos metadados especificando o objeto original como a origem de cópia

### Usando PUT para atualizar metadados
{: #java-examples-metadata-put}

**Nota:** a solicitação de `PUT` sobrescreve o conteúdo existente do objeto, portanto, ele deve primeiro ser transferido por download e transferido por upload novamente com os novos metadados

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

### Usando COPY para atualizar metadados
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

## Usando o Immutable Object Storage
{: #java-examples-immutable}

### Incluir uma configuração de proteção em um depósito existente
{: #java-examples-immutable-enable}

Essa implementação da operação `PUT` usa o parâmetro de consulta `protection` para configurar os parâmetros de retenção para um depósito existente. Essa operação permite configurar ou mudar os períodos mínimo, padrão e máximo de retenção. Essa operação também permite mudar o estado de proteção do depósito. 

Os objetos gravados em um depósito protegido não podem ser excluídos até que o período de proteção tenha expirado e todas as retenções legais no objeto sejam removidas. O valor de retenção padrão do depósito é fornecido para um objeto, a menos que um valor específico do objeto seja fornecido quando o objeto for criado. Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto. 

Os valores mínimo e máximo suportados para as configurações de período de retenção `MinimumRetention`, `DefaultRetention` e `MaximumRetention` são 0 dias e 365243 dias (1000 anos) respectivamente. 

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

### Verificar a proteção em um depósito
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

### Fazer upload de um objeto protegido
{: #java-examples-immutable-upload}

Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.

|Valor	| Tipo	| Descrição |
| --- | --- | --- | 
|`Retention-Period` | Número inteiro não negativo (segundos) | O período de retenção para armazenar o objeto em segundos. O objeto não pode ser sobrescrito nem excluído até que a quantia de tempo especificada no período de retenção tenha decorrido. Se esse campo e `Retention-Expiration-Date` forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período `DefaultRetention` do depósito será usado. Zero (`0`) é um valor legal que supõe que o período mínimo de retenção do depósito também é `0`. |
| `Retention-expiration-date` | Data (formato ISO 8601) | A data na qual será legal excluir ou modificar o objeto. É possível especificar somente isso ou o cabeçalho Retention-Period. Se ambos forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período DefaultRetention do depósito será usado. |
| `Retention-legal-hold-id` | sequência | Uma única retenção legal para aplicar ao objeto. Uma retenção legal é uma sequência longa de caracteres Y. O objeto não pode ser sobrescrito nem excluído até que todas as retenções legais associadas ao objeto sejam removidas. |

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

### Incluir ou remover uma retenção legal para ou de um objeto protegido
{: #java-examples-immutable-legal-hold}

O objeto pode suportar 100 retenções legais:

*  Um identificador de retenção legal é uma sequência de comprimento máximo de 64 caracteres e um comprimento mínimo de 1 caractere. Os caracteres válidos são letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` e `.
* Se a adição de uma determinada retenção legal exceder 100 retenções legais totais no objeto, a nova retenção legal não será incluída e um erro `400` será retornado.
* Se um identificador for muito longo, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador contiver caracteres inválidos, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador já estiver em uso em um objeto, a retenção legal existente não será modificada e a resposta indicará que o identificador já estava em uso com um erro `409`.
* Se um objeto não tiver metadados de período de retenção, um erro `400` será retornado e a inclusão ou remoção de uma retenção legal não será permitida.

A presença de um cabeçalho de período de retenção é necessária, caso contrário, um erro `400` é retornado.
{: http}

O usuário que faz a inclusão ou remoção de uma retenção legal deve ter as permissões `Manager` para esse depósito.

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

### Ampliar o período de retenção de um objeto protegido
{: #java-examples-immutable-extend}

O período de retenção de um objeto pode somente ser ampliado. Ele não pode ser diminuído do valor configurado atualmente.

O valor de expansão de retenção é configurado de uma de três maneiras:

* tempo adicional do valor atual (`Additional-Retention-Period` ou método semelhante)
* novo período de extensão em segundos (`Extend-Retention-From-Current-Time` ou método semelhante)
* nova data de validade de retenção do objeto (`New-Retention-Expiration-Date` ou método semelhante)

O período de retenção atual armazenado nos metadados do objeto é aumentado pelo tempo adicional fornecido ou substituído pelo novo valor, dependendo do parâmetro que está configurado na solicitação `extendRetention`. Em todos os casos, o parâmetro de ampliação de retenção é verificado com relação ao período de retenção atual e o parâmetro ampliado será aceito somente se o período de retenção atualizado for maior que o período de retenção atual.

Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.

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

### Listar retenções legais em um objeto protegido
{: #java-examples-immutable-list-holds}

Essa operação retorna:

* Data de criação do objeto
* Período de retenção do objeto em segundos
* Data de expiração de retenção calculada com base no período e data de criação
* Lista de retenções legais
* Identificador de retenção legal
* Registro de data e hora em que a retenção legal foi aplicada

Se não houver retenções legais no objeto, um `LegalHoldSet` vazio será retornado.
Se não houver nenhum período de retenção especificado no objeto, um erro `404` será retornado.

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
