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

# 使用 Java
{: #java}

{{site.data.keyword.cos_full}} SDK for Java 综合全面，并且具有本指南中未描述的功能。有关详细的类和方法文档，请参阅 [Javadoc](https://ibm.github.io/ibm-cos-sdk-java/)。在 [GitHub 存储库](https://github.com/ibm/ibm-cos-sdk-java)中可以找到源代码。

## 获取 SDK
{: #java-install}

使用 {{site.data.keyword.cos_full_notm}} Java SDK 的最简单方法是利用 Maven 来管理依赖项。如果您不熟悉 Maven，那么可以使用 [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) 指南来快速入门和熟悉运用。

Maven 使用名为 `pom.xml` 的文件来指定 Java 项目所需的库（及其版本）。下面是示例 `pom.xml` 文件，说明使用 {{site.data.keyword.cos_full_notm}} Java SDK 连接到 {{site.data.keyword.cos_short}}。


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

## 从 1.x.x 迁移
{: #java-migrate}

SDK 2.0 发行版引入了名称空间更改，允许应用程序使用原始 AWS 库来连接到同一应用程序或环境中的 AWS 资源。要从 1.x 迁移到 2.0，有必要进行一些更改：

1. 使用 Maven 通过将 pom.xml 中的所有 `ibm-cos-java-sdk` 依赖项版本标记更改为 `2.0.0` 来进行更新。验证 pom.xml 中是否没有版本低于 `2.0.0` 的 SDK 模块依赖项。
2. 将所有导入声明从 `amazonaws` 更新为 `ibm.cloud.objectstorage`。


## 创建客户机和获取凭证
{: #java-credentials}

在以下示例中，将通过提供凭证信息（API 密钥和服务实例标识）来创建和配置客户机 `cos`。这些值还可以自动从凭证文件或环境变量中获取。

生成[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)后，生成的 JSON 文档可以保存到 `~/.bluemix/cos_credentials`。除非在客户机创建期间显式设置了其他凭证，否则 SDK 会自动从此文件中获取凭证。如果 `cos_credentials` 文件包含 HMAC 密钥，那么客户机会使用签名进行认证；如果不包含 HMAC 密钥，客户机将使用提供的 API 密钥通过不记名令牌进行认证。

如果是从 AWS S3 进行迁移，那么还可以从 `~/.aws/credentials` 中获取以下格式的凭证数据：

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

如果 `~/.bluemix/cos_credentials` 和 `~/.aws/credentials` 同时存在，那么 `cos_credentials` 优先。

 有关客户机构造的更多详细信息，请[参阅 Javadoc](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html)。

## 代码示例
{: #java-examples}

下面首先介绍一个完整的示例类，该类将运行一些基本功能，然后分别讨论这些类。此 `CosExample` 类将列出现有存储区中的对象，创建新存储区，然后列出服务实例中的所有存储区。 

### 收集必需的信息
{: #java-examples-prereqs}

* `bucketName` 和 `newBucketName` 是[唯一的 DNS 安全](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)字符串。因为存储区名称在整个系统中唯一，因此如果多次运行此示例，那么需要更改这些值。请注意，名称在删除后会保留 10 到 15 分钟。
* `api_key` 是在[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `apikey` 的值。
* `service_instance_id` 是在[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `resource_instance_id` 的值。 
* `endpoint_url` 是服务端点 URL，包含 `https://` 协议。这**不是**在[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `endpoints` 值。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `storageClass` 是对应于 `endpoint` 值的[有效供应代码](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)。然后，此项会用作 S3 API 的 `LocationConstraint` 变量。
* `location` 应该设置为 `storageClass` 的 location 部分。对于 `us-south-standard`，这将是 `us-south`。此变量仅用于计算 [HMAC 签名](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac)，但对于任何客户机，此变量都是必需的，包括使用 IAM API 密钥的此示例。

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

### 初始化配置
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

*键值*
* `<endpoint>` - Cloud Object Storage 的公共端点（通过 [IBM Cloud 仪表板](https://cloud.ibm.com/resources){:new_window}提供）。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<api-key>` - 创建服务凭证时生成的 API 密钥（创建和删除示例需要写访问权）
* `<resource-instance-id>` - Cloud Object Storage 的资源标识（通过 [IBM Cloud CLI](/docs//docs/cli?topic=cloud-cli-idt-cli) 或 [IBM Cloud 仪表板](https://cloud.ibm.com/resources){:new_window}提供）
* `<location>` - Cloud Object Storage 的缺省位置（必须与用于 `<endpoint>` 的区域相匹配）

*SDK 参考*
* 类
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### 确定端点
{: #java-examples-endpoint}

下面的方法可用于根据存储区位置、端点类型（公共或专用）和特定区域（可选）来确定服务端点。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

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

### 创建新存储区
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### 创建使用其他存储类的存储区
{: #java-examples-storage-class}

在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中可以参考 `LocationConstraint` 的有效供应代码的列表。

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*SDK 参考*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### 创建新的文本文件
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
请注意，将定制元数据添加到对象时，需要使用 SDK 创建 `ObjectMetadata` 对象，不要手动发送包含 `x-amz-meta-{key}` 的定制头。使用 HMAC 凭证进行认证时，后者可能会导致问题。
{: .tip}

### 通过文件上传对象
{: #java-examples-upload}

此示例假定存储区 `sample` 已存在。

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### 使用流上传对象
{: #java-examples-stream}

此示例假定存储区 `sample` 已存在。

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

### 将对象下载到文件
{: #java-examples-download}

此示例假定存储区 `sample` 已存在。

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


### 使用流下载对象
{: #java-examples-download-stream}

此示例假定存储区 `sample` 已存在。

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### 复制对象
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

*SDK 参考*
* 类
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* 方法
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### putObject 异常
{: #java-examples-put-exception}

即使新对象上传成功，putObject 方法也可能会抛出以下异常：
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

**根本原因：**JAXB API 被视为是 Java EE API，并且 Java SE 9 中的缺省类路径上不再包含 JAXB API。

**解决方法：**将以下条目添加到项目文件夹中的 pom.xml 文件，然后重新打包项目
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### 列出可用存储区
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
*SDK 参考*
* 类
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* 方法
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### 列出存储区中的项 (V2)
{: #java-examples-list-objects-v2}

[AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} 对象包含已更新的用于列出内容的方法 ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window})。此方法允许您限制返回的记录数，并批量检索记录。这对于对应用程序中的结果进行分页非常有用，并可提高性能。

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

*SDK 参考*
* 类
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* 方法
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### 列出存储区中的项 (V1)
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

*SDK 参考*
* 类
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* 方法
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### 获取特定项的文件内容
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

*SDK 参考*
* 类
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* 方法
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### 从存储区中删除一个项
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*SDK 参考*
* 方法
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### 从存储区中删除多个项
{: #java-examples-delete-objects}

删除请求最多可包含 1000 个要删除的键。虽然这对于减少每个请求的开销非常有用，但在删除大量键时应谨慎。此外，请考虑对象的大小，以确保性能合适。
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

*SDK 参考*
* 类
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* 方法
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### 删除存储区
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*SDK 参考*
* 方法
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### 检查对象是否公共可读
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

*SDK 参考*
* 类
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* 方法 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### 执行分块上传
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
*SDK 参考*
* 类
    * [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){:new_window}
    * [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}
    * [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){:new_window}
    * [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){:new_window}

* 方法
    * [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){:new_window}
    * [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){:new_window}
    * [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){:new_window}
    * [uploadPart](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){:new_window}

## 使用传输管理器上传更大的对象
{: #java-examples-transfer-manager}

`TransferManager` 通过在每次有必要设置配置参数时，自动合并分块上传来简化大型文件传输。

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

*SDK 参考*
* 类
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* 方法
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## 使用 Key Protect
{: #java-examples-kp}
可以将 Key Protect 添加到存储区，以对云中的敏感数据进行静态加密。

### 开始之前
{: #java-examples-kp-prereqs}

要创建启用了 Key Protect 的存储区，需要以下各项：

* [已供应](/docs/services/key-protect?topic=key-protect-provision#provision) Key Protect 服务
* 根密钥可用（[已生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已导入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 检索根密钥 CRN
{: #java-examples-kp-root}

1. 检索 Key Protect 服务的[实例标识](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)。
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 来检索所有[可用密钥](https://cloud.ibm.com/apidocs/key-protect)。
    * 可以使用 `curl` 命令或 API REST 客户机（例如，[Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）来访问 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 检索将用于在存储区上启用 Key Protect 的根密钥的 CRN。CRN 类似于以下内容：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 创建启用了 Key Protect 的存储区
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
*键值*
* `<algorithm>` - 用于添加到存储区的新对象的加密算法（缺省值为 AES256）。
* `<root-key-crn>` - 从 Key Protect 服务获取的根密钥的 CRN。

*SDK 参考*
* 类
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* 方法
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### 为 Key Protect 新建头
{: #java-examples-kp-headers}

在 `Headers` 类中定义了其他头：

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

创建存储区实现中已添加 IAM 服务实例头的相同部分将添加 2 个新的加密头：

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

`ObjectListing` 和 `HeadBucketResult` 对象已更新为在 getter 和 setter 方法中包含布尔值 `IBMSSEKPEnabled` 和字符串 `IBMSSEKPCustomerRootKeyCrn` 变量。这两个变量将存储新头的值。

#### 对存储区执行 GET 请求
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

`ObjectListing` 类需要另外 2 种方法：

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

在 `Headers` 类中定义了其他头：

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

S3XmlResponseHandler 负责对所有 XML 响应进行反序列化。添加的检查的结果是 `ObjectListing` 的实例，并且检索到的头将添加到 `ObjectListing` 对象：

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

#### 对存储区执行 HEAD 请求
{: #java-examples-kp-head}
在 Headers 类中定义了其他头：

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

这些变量在 HeadBucketResponseHandler 中进行填充。

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## 使用 Aspera 高速传输
{: #java-examples-aspera}

通过安装 [Aspera 高速传输库](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)，可以在应用程序中利用高速文件传输。Aspera 库是封闭式源代码库，因此具有 COS SDK（使用 Apache 许可证）的可选依赖项。 

每个 Aspera 高速传输会话都会衍生一个单独的 `ascp` 进程，此进程在客户机上运行以执行传输。请确保计算环境允许此进程运行。
{:tip}

您需要 S3 客户机的实例以及 IAM 令牌管理器类来初始化 `AsperaTransferManager`。需要 `s3Client` 来获取 COS 目标存储区的 FASP 连接信息。需要 `tokenManager` 以允许 Aspera 高速传输 SDK 向 COS 目标存储区进行认证。

### 初始化 `AsperaTransferManager`
{: #java-examples-aspera-init}

初始化 `AsperaTransferManager` 之前，请确保在使用 [`s3Client`](#java-examples-config) 和 [`tokenManager`](#java-examples-config) 对象。 

除非您预期网络中会有严重噪声或丢包，否则使用单个 Aspera 高速传输会话并没有多大优点。因此，我们需要指示 `AsperaTransferManager` 通过 `AsperaConfig` 类来使用多个会话。这会将传输分割成多个并行**会话**，这些会话发送的数据块大小由**阈值**定义。

使用多会话的典型配置应该如下：
* 2500 MBps 目标速率
* 100 MB 阈值（*这是针对大多数应用程序的建议值*）

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
在以上示例中，SDK 将衍生足够的会话来尝试达到目标速率 2500 MBps。

或者，可以在 SDK 中显式配置会话管理。对于需要更精确地控制网络利用率的情况，此功能会非常有用。

使用显式多会话的典型配置应该如下：
* 2 个或 10 个会话
* 100 MB 阈值（*这是针对大多数应用程序的建议值*）

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

为了在大多数场景实现最佳性能，请始终使用多个会话，以最大限度地减少与实例化 Aspera 高速传输关联的任何开销。**如果网络容量至少为 1 Gbps，那么应该使用 10 个会话。**带宽低于 1 Gbps 的网络应该使用 2 个会话。
{:tip}

*键值*
* `API_KEY` - 具有写入者或管理者角色的用户或服务标识的 API 密钥

您需要提供 IAM API 密钥来构造 `AsperaTransferManager`。目前**不**支持 [HMAC 凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window}。有关 IAM 的更多信息，请[单击此处](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)。
{:tip}

### 文件上传
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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称。
* `<absolute-path-to-source-data>` - 要上传到 Object Storage 的目录和文件的名称。
* `<item-name>` - 已添加到存储区的新对象的名称。

### 文件下载
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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称。
* `<absolute-path-to-file>` - 要保存从 Object Storage 下载的目录和文件的名称。
* `<item-name>` - 存储区中对象的名称。

### 目录上传
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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称。
* `<absolute-path-to-directory>` - 要上传到 Object Storage 的文件的目录。
* `<virtual-directory-prefix>` - 上传时要添加到每个文件的目录前缀的名称。使用空值或空字符串可将文件上传到存储区根目录。

### 目录下载
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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称。
* `<absolute-path-to-directory>` - 要保存从 Object Storage 下载的文件的目录。
* `<virtual-directory-prefix>` - 要下载的每个文件的目录前缀的名称。使用空值或空字符串可下载存储区中的所有文件。

### 逐个传输分别覆盖会话配置
{: #java-examples-aspera-config}

可以通过将 `AsperaConfig` 的实例传递到 upload 和 download 重载方法，逐个传输分别覆盖多会话配置值。使用 `AsperaConfig` 可以指定会话数和每个会话的最小文件阈值大小。 

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

### 监视传输进度
{: #java-examples-aspera-monitor}

监视文件/目录传输进度的最简单方法是使用 `isDone()` 属性，此属性在传输完成时会返回 `true`。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

还可以通过在 `AsperaTransaction` 上调用 `onQueue` 方法来检查传输是否在排队等待处理。`onQueue` 会返回布尔值，`true` 指示传输已排队。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

要检查是否正在传输，请在 `AsperaTransaction` 中调用 progress 方法。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

缺省情况下，每次传输都会附加有 `TransferProgress`。`TransferProgress` 将报告传输的字节数以及传输的字节数占要传输的总字节数的百分比。要访问传输的 `TransferProgress`，请在 `AsperaTransaction` 中使用 `getProgress` 方法。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

要报告传输的字节数，请在 `TransferProgress` 上调用 `getBytesTransferred` 方法。要报告传输的字节数占总字节数的百分比，请在 `TransferProgress` 上调用 `getPercentTransferred` 方法。

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

### 暂停/恢复/取消
{: #java-examples-aspera-pause}

SDK 提供了通过 `AsperaTransfer` 对象的以下方法来管理文件/目录传输进度的能力：

* `pause()`
* `resume()`
* `cancel()`

调用以上概述的任一方法都不会有任何副作用。合适的清除和整理工作由 SDK 负责处理。
{:tip}

以下示例显示了这些方法的可能用法：

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

### 对 Aspera 问题进行故障诊断
{: #java-examples-aspera-ts}

**问题：**在 Linux 或 Mac OS X 上使用 Oracle JDK 的开发者可能在传输期间会遇到意外和静默崩溃

**原因：**本机代码需要自己的信号处理程序，这些处理程序可能覆盖 JVM 的信号处理程序。可能必须使用 JVM 的信号链接工具。

*IBM&reg; JDK 用户或 Microsoft&reg; Windows 用户不受影响。*

**解决方案：**链接并装入 JVM 的信号链接库。
* 在 Linux 上，找到 `libjsig.so` 共享库并设置以下环境变量：
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* 在 Mac OS X 上，找到共享库 `libjsig.dylib` 并设置以下环境变量：
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

有关信号链接的更多信息，请访问 [Oracle&reg; JDK 文档](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window}。

**问题：**在 Linux 上发生 `UnsatisfiedLinkError`

**原因：**系统无法装入从属库。在应用程序日志中可能会看到以下错误：

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**解决方案：**设置以下环境变量：

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
## 更新元数据
{: #java-examples-metadata}
有两种方法可更新现有对象上的元数据：
* 对新的元数据和原始对象内容执行 `PUT` 请求
* 使用将原始对象指定为复制源的新元数据来执行 `COPY` 请求

### 使用 PUT 更新元数据
{: #java-examples-metadata-put}

**注：**`PUT` 请求会覆盖对象的现有内容，因此必须首先下载对象，然后使用新的元数据重新上传

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

### 使用 COPY 更新元数据
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

## 使用不可变对象存储器
{: #java-examples-immutable}

### 向现有存储区添加保护配置
{: #java-examples-immutable-enable}

此 `PUT` 操作实现使用 `protection` 查询参数来设置现有存储区的保留时间参数。此操作允许您设置或更改最短保留期、缺省保留期和最长保留期。此操作还允许您更改存储区的保护状态。 

对于写入受保护存储区的对象，在保护时间段到期并且除去了对象上的所有合法保留之前，无法删除这些对象。除非在创建对象时提供了特定于对象的值，否则将向对象提供存储区的缺省保留时间值。如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。 

保留期设置 `MinimumRetention`、`DefaultRetention` 和 `MaximumRetention` 的最小和最大支持值分别为 0 天和 365243 天（1000 年）。 

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

### 检查存储区上的保护
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

### 上传受保护对象
{: #java-examples-immutable-upload}

如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。

|值|类型|描述|
| --- | --- | --- | 
|`Retention-Period`|非负整数（秒）|要在对象上存储的保留期（以秒为单位）。在保留期中指定的时间长度到期之前，无法覆盖也无法删除对象。如果同时指定了此字段和 `Retention-Expiration-Date`，将返回 `400` 错误。如果这两个字段均未指定，将使用存储区的 `DefaultRetention` 时间段。假定存储区的最短保留期为 `0`，那么零 (`0`) 是合法值。|
|`Retention-expiration-date`|日期（ISO 8601 格式）|能够合法删除或修改对象的日期。只能指定此项或指定 Retention-Period 头。如果同时指定这两项，将返回 `400` 错误。如果这两项均未指定，将使用存储区的 DefaultRetention 时间段。|
|`Retention-legal-hold-id`|字符串|要应用于对象的单个合法保留。合法保留是长度为 Y 个字符的字符串。在除去与对象关联的所有合法保留之前，无法覆盖或删除对象。|

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

### 向受保护对象添加合法保留或除去受保护对象的合法保留
{: #java-examples-immutable-legal-hold}

一个对象可以支持 100 个合法保留：

*  合法保留标识是一个字符串，最大长度为 64 个字符，最小长度为 1 个字符。有效字符为字母、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 和 `。
* 如果添加给定合法保留将导致对象上超过 100 个合法保留，那么不会添加新的合法保留，并且将返回 `400` 错误。
* 如果标识太长，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识包含无效字符，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识已在对象上使用，那么不会修改现有合法保留，响应会指示该标识已在使用，并返回 `409` 错误。
* 如果对象没有保留期元数据，那么将返回 `400` 错误，并且不允许添加或除去合法保留。

保留期头必须存在，否则会返回 `400` 错误。
{: http}

添加或除去合法保留的用户必须具有对此存储区的`管理者`许可权。

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

### 延长受保护对象的保留期
{: #java-examples-immutable-extend}

对象的保留期只能延长。不能在当前配置值的基础上缩短。

保留时间延长值可通过以下三种方式之一进行设置：

* 在当前值的基础上增加时间（`Additional-Retention-Period` 或类似方法）
* 新的延长时间段（以秒为单位）（`Extend-Retention-From-Current-Time` 或类似方法）
* 对象的新保留到期日期（`New-Retention-Expiration-Date` 或类似方法）

根据 `extendRetention` 请求中设置的参数，对象元数据中存储的当前保留期可通过给定更多时间延长，也可替换为新值。在所有情况下，都会根据当前保留期来检查延长保留时间参数，并且仅当更新的保留期大于当前保留期时，才会接受延长参数。

如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。

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

### 列出受保护对象上的合法保留
{: #java-examples-immutable-list-holds}

此操作会返回以下内容：

* 对象创建日期
* 对象保留期（秒）
* 根据时间段和创建日期计算的保留到期日期
* 合法保留的列表
* 合法保留标识
* 应用合法保留时的时间戳记

如果对象上没有合法保留，那么会返回空的 `LegalHoldSet`。如果在对象上未指定保留期，那么会返回 `404` 错误。

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
