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

{{site.data.keyword.cos_full}} SDK for Java 包羅萬象，且具有本手冊中未說明的特性。如需詳細的類別及方法文件，[請參閱 Javadoc](https://ibm.github.io/ibm-cos-sdk-java/)。原始碼可以在 [GitHub 儲存庫](https://github.com/ibm/ibm-cos-sdk-java)中找到。

## 取得 SDK
{: #java-install}

使用 {{site.data.keyword.cos_full_notm}} Java SDK 最簡單的方法是使用 Maven 來管理相依關係。如果不熟悉 Maven，您可以使用 [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) 手冊開始進行。

Maven 會使用稱為 `pom.xml` 的檔案，來指定 Java 專案所需的程式庫（及其版本）。以下是使用 {{site.data.keyword.cos_full_notm}} Java SDK 連接至 {{site.data.keyword.cos_short}} 的範例 `pom.xml` 檔。


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

## 從 1.x.x 移轉
{: #java-migrate}

2.0 版的 SDK 引進名稱空間作業的變更，可讓應用程式利用原始 AWS 程式庫來連接至相同應用程式或環境內的 AWS 資源。若要從 1.x 移轉至 2.0，需要進行一些變更：

1. 使用 Maven 進行更新，方法是將 pom.xml 中的所有 `ibm-cos-java-sdk` 相依關係版本標籤變更為 `2.0.0`。驗證 pom.xml 中沒有版本早於 `2.0.0` 的 SDK 模組相依關係。
2. 將任何匯入宣告從 `amazonaws` 更新為 `ibm.cloud.objectstorage`。


## 建立用戶端及讀取認證
{: #java-credentials}

在下列範例中，是透過提供認證資訊（API 金鑰及服務實例 ID），來建立及配置用戶端 `cos`。這些值也可以自動從 credentials 檔案或環境變數中取得。

產生[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)後，會將產生的 JSON 文件儲存為 `~/.bluemix/cos_credentials`。除非在建立用戶端期間，明確地設定其他認證，否則 SDK 將自動從此檔案讀取認證。如果 `cos_credentials` 檔案包含 HMAC 金鑰，用戶端將以簽章進行鑑別，否則用戶端會使用提供的 API 金鑰以持有人記號進行鑑別。

如果從 AWS S3 移轉，您也可以使用下列格式從 `~/.aws/credentials` 讀取認證資料：

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

如果 `~/.bluemix/cos_credentials` 和 `~/.aws/credentials` 同時存在，則 `cos_credentials` 會優先。

 如需用戶端建構的詳細資料，[請參閱 Javadoc](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html)。

## 程式碼範例
{: #java-examples}

讓我們從一個完整的範例類別開始，執行一些基本功能，然後再個別探索類別。此 `CosExample` 類別會列出現有儲存區中的物件、建立新的儲存區，然後列出服務實例中的所有儲存區。 

### 收集必要資訊
{: #java-examples-prereqs}

* `bucketName` 及 `newBucketName` 是[唯一且 DNS 安全](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)的字串。因為儲存區名稱在整個系統中是唯一的，因此如果此範例執行多次，將需要變更這些值。請注意，名稱在刪除之後會保留 10-15 分鐘。
* `api_key` 是[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中作為 `apikey` 找到的值。
* `service_instance_id` 是[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中作為 `resource_instance_id` 找到的值。 
* `endpoint_url` 是服務端點 URL，包含 `https://` 通訊協定。這**不是**在[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `endpoints` 值。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `storageClass` 是與 `endpoint` 值對應的[有效佈建碼](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)。然後，這會用來作為 S3 API `LocationConstraint` 變數。
* `location` 應該設定為 `storageClass` 的位置部分。若為 `us-south-standard`，這將是 `us-south`。此變數僅用於計算 [HMAC 簽章](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac)，但任何用戶端（包括使用 IAM API 金鑰的這個範例）都需要此變數。

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

### 起始設定配置
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

*金鑰值*
* `<endpoint>` - 雲端物件儲存空間的公用端點（可從 [IBM Cloud 儀表格](https://cloud.ibm.com/resources){:new_window}取得）。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<api-key>` - 建立服務認證時產生的 API 金鑰（建立及刪除範例需要寫入權）。
* `<resource-instance-id>` - 雲端物件儲存空間的資源 ID（可透過 [IBM Cloud CLI](/docs//docs/cli?topic=cloud-cli-idt-cli) 或 [IBM Cloud 儀表板](https://cloud.ibm.com/resources){:new_window}取得）。
* `<location>` - 雲端物件儲存空間的預設位置（必須符合用於 `<endpoint>` 的地區）。

*SDK 參照*
* 類別
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### 決定端點
{: #java-examples-endpoint}

下面的方法可用來根據儲存區位置、端點類型（公用或專用）及特定地區（選用）來決定服務端點。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

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

### 建立新的儲存區
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### 建立具有不同儲存空間類別的儲存區
{: #java-examples-storage-class}

可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中參閱 `LocationConstraint` 的有效佈建碼清單。

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*SDK 參照*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### 建立新的文字檔
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
請注意，在將自訂 meta 資料新增至物件時，必須使用 SDK 來建立 `ObjectMetadata` 物件，而不要手動傳送包含 `x-amz-meta-{key}` 的自訂標頭。後者可能會在使用 HMAC 認證進行鑑別時造成問題。
{: .tip}

### 從檔案上傳物件
{: #java-examples-upload}

此範例假設儲存區 `sample` 已存在。

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### 使用串流上傳物件
{: #java-examples-stream}

此範例假設儲存區 `sample` 已存在。

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

### 將物件下載至檔案
{: #java-examples-download}

此範例假設儲存區 `sample` 已存在。

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


### 使用串流下載物件
{: #java-examples-download-stream}

此範例假設儲存區 `sample` 已存在。

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### 複製物件
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

*SDK 參照*
* 類別
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* 方法
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### putObject 異常狀況
{: #java-examples-put-exception}

即使新物件上傳成功，putObject 方法也可能會擲出下列異常狀況。
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

**主要原因：**會將 JAXB API 視為 Java EE API，且不再包含在 Java SE 9 的預設類別路徑上。

**修正：**將下列項目新增至您專案資料夾中的 pom.xml 檔案，並重新包裝專案。
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### 列出可用的儲存區
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
*SDK 參照*
* 類別
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* 方法
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### 列出儲存區中的項目（第 2 版）
{: #java-examples-list-objects-v2}

[AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} 物件包含更新的方法，以列出內容 ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window})。此方法可讓您限制傳回的記錄數，並分批次擷取記錄。這可能有助於在應用程式內將您的結果進行分頁，並改善效能。

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

*SDK 參照*
* 類別
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* 方法
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### 列出儲存區中的項目（第 1 版）
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

*SDK 參照*
* 類別
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* 方法
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### 取得特定項目的檔案內容
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

*SDK 參照*
* 類別
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* 方法
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### 從儲存區刪除項目
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*SDK 參照*
* 方法
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### 從儲存區刪除多個項目
{: #java-examples-delete-objects}

刪除要求最多可以包含您要刪除的 1000 個金鑰。雖然這對於減少每個要求的額外負擔非常有用，但在刪除大量金鑰時，請注意。此外，也請考量物件的大小，以確保適當的效能。
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

*SDK 參照*
* 類別
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* 方法
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### 刪除儲存區
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*SDK 參照*
* 方法
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### 檢查物件是否可公開讀取
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

*SDK 參照*
* 類別
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* 方法 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### 執行多部分上傳
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
*SDK 參照*
* 類別
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

## 使用 Transfer Manager 上傳較大的物件
{: #java-examples-transfer-manager}

`TransferManager` 可簡化大型檔案傳送，方法是在必須設定配置參數時自動併入多部分上傳。

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

*SDK 參照*
* 類別
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* 方法
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## 使用 Key Protect
{: #java-examples-kp}
Key Protect 可新增至儲存空間儲存區，以加密雲端中靜止的機密資料。

### 開始之前
{: #java-examples-kp-prereqs}

為了建立儲存區並啟用 Key Protect，需要下列項目：

* [已佈建](/docs/services/key-protect?topic=key-protect-provision#provision) Key Protect 服務
* 根金鑰可用（[已產生](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已匯入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 擷取根金鑰 CRN
{: #java-examples-kp-root}

1. 擷取 Key Protect 服務的[實例 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 來擷取所有[可用金鑰](https://cloud.ibm.com/apidocs/key-protect)
    * 您可以使用 `curl` 指令或 API REST 用戶端（例如 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）來存取 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 擷取您將使用來在儲存區上啟用 Key Protect 的根金鑰 CRN。CRN 看起來如下：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 建立儲存區並啟用 key-protect
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
*金鑰值*
* `<algorithm>` - 用於新增至儲存區的新物件加密演算法（預設為 AES256）。
* `<root-key-crn>` - 從 Key Protect 服務取得的根金鑰 CRN。

*SDK 參照*
* 類別
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* 方法
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Key Protect 的新標頭
{: #java-examples-kp-headers}

其他標頭已定義在 `Headers` 類別內：

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

已新增 IAM 服務實例標頭之建立儲存區實作的相同區段中，將會新增 2 個新的加密標頭：

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

已更新 `ObjectListing` 及 `HeadBucketResult` 物件，以包含布林 `IBMSSEKPEnabled` & 字串 `IBMSSEKPCustomerRootKeyCrn` 變數，以及 getter & setter 方法。這些將會儲存新標頭的值。

#### GET 儲存區
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

`ObjectListing` 類別需要 2 個額外的方法：

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

額外的標頭已在 `Headers` 類別內定義：

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

S3XmlResponseHandler 負責取消配置所有 XML 回應。已新增檢查，其結果是 `ObjectListing` 的實例，而擷取的標頭將新增至 `ObjectListing` 物件：

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

#### HEAD 儲存區
{: #java-examples-kp-head}
其他標頭已定義在 Headers 類別內：

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

這些變數會移入 HeadBucketResponseHandler 中。

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## 使用 Aspera 高速傳輸
{: #java-examples-aspera}

透過安裝 [Aspera 高速傳送程式庫](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)，您可以在應用程式中使用高速檔案傳送。Aspera 程式庫是封閉的來源，因此對於 COS SDK（其使用 Apache 授權）具有選用的相依關係。 

每一個 Aspera 高速傳送階段作業都會產生在用戶端機器上執行的個別 `ascp` 處理程序，以執行傳送。請確定您的運算環境可以容許執行此處理程序。
{:tip}

您將需要 S3 Client 及 IAM Token Manager 類別的實例，才能起始設定 `AsperaTransferManager`。需要 `s3Client`，才能取得 COS 目標儲存區的 FASP 連線資訊。需要 `tokenManager`，才能容許 Aspera 高速傳送 SDK，以使用 COS 目標儲存區進行鑑別。

### 起始設定 `AsperaTransferManager`
{: #java-examples-aspera-init}

在起始設定 `AsperaTransferManager` 之前，請確定您已使用 [`s3Client`](#java-examples-config) 及 [`tokenManager`](#java-examples-config) 物件。 

除非您預期在網路中看到大量的雜訊或封包流失，否則使用 Aspera 高速傳送的單一階段作業不會有多大的好處。因此，我們需要告知 `AsperaTransferManager` 使用 `AsperaConfig` 類別，以使用多個階段作業。這會將傳送分割成數個平行的**階段作業** ，以傳送大小由 **threshold** 值定義的資料區塊。

使用多個階段作業的一般配置應該是：
* 2500 MBps 目標率
* 100 MB 臨界值（*此為大部分應用程式的建議值*）

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
在上述範例中，SDK 會大量產生足夠的階段作業，以嘗試達到目標率 2500 MBps。

或者，可以在 SDK 中明確配置階段作業管理。在需要更精確控制網路使用率的情況下，這十分有用。

使用明確的多個階段作業的一般配置應該是：
* 2 或 10 個階段作業
* 100 MB 臨界值（*此為大部分應用程式的建議值*）

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

若要在大部分情境下具有最佳效能，請一律使用多個階段作業，讓與實例化 Aspera 高速傳送相關聯的任何額外負擔減到最少。**如果您的網路容量至少為 1 Gbps，您應該使用 10 個階段作業。**較低頻寬的網路應該使用兩個階段作業。
{:tip}

*金鑰值*
* `API_KEY` - 具有「撰寫者」或「管理者」角色之使用者或服務 ID 的 API 金鑰

您將需要提供 IAM API 金鑰，以建構 `AsperaTransferManager`。[HMAC 認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} 目前**不**支援。如需 IAM 的相關資訊，[請按一下這裡](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)。
{:tip}

### 檔案上傳
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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-source-data>` - 要上傳至 Object Storage 的目錄及檔案名稱。
* `<item-name>` - 新增至儲存區之新物件的名稱。

### 檔案下載
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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-file>` - 要從 Object Storage 儲存的目錄及檔案名稱。
* `<item-name>` - 儲存區中的物件名稱。

### 目錄上傳
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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-directory>` - 要上傳至 Object Storage 的檔案目錄。
* `<virtual-directory-prefix>` - 上傳時要新增至每個檔案的目錄字首名稱。請使用空值或空字串，將檔案上傳至儲存區根目錄。

### 目錄下載
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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-directory>` - 要從 Object Storage 儲存已下載檔案的目錄。
* `<virtual-directory-prefix>` - 要下載之每個檔案的目錄字首名稱。請使用空值或空字串，以下載儲存區中的所有檔案。

### 在每個傳送基礎上置換階段作業配置
{: #java-examples-aspera-config}

在每個傳送基礎上，您可以透過將 `AsperaConfig` 的實例傳遞至上傳及下載已置換方法，來置換多階段作業配置值。使用 `AsperaConfig`，您可以指定階段作業數及每個階段作業的檔案臨界值大小下限。 

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

### 監視傳送進度
{: #java-examples-aspera-monitor}

監視檔案/目錄傳送進度的最簡單方式是使用 `isDone()` 內容，當您的傳送完成時，會傳回 `true`。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

您也可以在 `AsperaTransaction` 上呼叫 `onQueue` 方法，檢查傳送是否置入佇列進行處理。`onQueue` 將傳回 `true` 的布林值，指出傳送已置入佇列。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

若要檢查傳送是否正在進行，請在 `AsperaTransaction` 中呼叫進度方法。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

依預設，每個傳送都會有 `TransferProgress` 附加至其中。`TransferProgress` 會報告傳送的位元組數，以及要傳送的位元組總數的傳送百分比。若要存取傳送的 `TransferProgress`，請在 `AsperaTransaction` 中使用 `getProgress` 方法。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

若要報告傳送的位元組數，請在 `TransferProgress`上呼叫 `getBytesTransferred` 方法。若要報告傳送的位元組總數傳送百分比，請在 `TransferProgress` 上呼叫 `getPercentTransferred` 方法。

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

### 暫停/繼續/取消
{: #java-examples-aspera-pause}

SDK 可讓您透過 `AsperaTransfer` 物件的下列方法來管理檔案/目錄傳送的進度：

* `pause()`
* `resume()`
* `cancel()`

呼叫上述任一種方法時，不會產生任何副作用。SDK 會處理適當的清除及整理。
{:tip}

下列範例顯示這些方法的可能用法：

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

### 疑難排解 Aspera 問題
{: #java-examples-aspera-ts}

**問題：**在 Linux 或 Mac OS X 上使用 Oracle JDK 的開發人員在傳送期間可能會遇到非預期且無聲自動的損毀

**原因：**原生程式碼需要它自己的信號處理程式，這可能會置換 JVM 的信號處理程式。可能需要使用 JVM 的信號鏈結機能。

*IBM&reg; JDK 使用者或 Microsoft&reg; Windows 使用者不受到影響。*

**解決方案：**鏈結並載入 JVM 的信號鏈結程式庫。
* 在 Linux 上尋找 `libjsig.so` 共用程式庫，並設定下列環境變數：
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* 在 Mac OS X 上，尋找共用程式庫 `libjsig.dylib`，並設定下列環境變數：
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

造訪 [Oracle&reg; JDK 文件](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window}，以取得信號鏈結的相關資訊。

**問題：**Linux 上的 `UnsatisfiedLinkError`

**原因：**系統無法載入相依程式庫。在應用程式日誌中，可能會看到下列錯誤：

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**解決方案：**設定下列環境變數：

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
## 更新 meta 資料
{: #java-examples-metadata}
有兩種方式可更新現有物件上的 meta 資料：
* 具有新 meta 資料及原始物件內容的 `PUT` 要求
* 使用新 meta 資料來執行 `COPY` 要求，並指定原始物件作為副本來源

### 使用 PUT 更新 meta 資料
{: #java-examples-metadata-put}

**附註：**`PUT` 要求會改寫物件的現有內容，因此必須先下載並重新上傳具有新 meta 資料的內容

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

### 使用 COPY 更新 meta 資料
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

## 使用 Immutable Object Storage
{: #java-examples-immutable}

### 將保護配置新增至現有儲存區
{: #java-examples-immutable-enable}

此 `PUT` 作業的實作使用 `protection` 查詢參數來設定現有儲存區的保留參數。此作業可讓您設定或變更最短保留期間、預設值及最長保留期間。此作業也可讓您變更儲存區的保護狀態。 

在保護期間過期，並移除物件上的所有合法保留之前，無法刪除寫入受保護儲存區的物件。除非在物件建立時提供物件特定值，否則會將儲存區的預設保留值提供給物件。不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。 

保留期間設定值 `MinimumRetention`、`DefaultRetention` 及 `MaximumRetention` 的最小和最大支援值分別是 0 天到 365243 天（1000 年）。 

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

### 檢查儲存區的保護
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

### 上傳受保護物件
{: #java-examples-immutable-upload}

不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。

|值	|類型|說明|
| --- | --- | --- | 
|`Retention-Period` | 非負整數（秒）| 儲存在物件上的保留期間（以秒為單位）。除非已過保留期間中指定的時間，否則無法改寫或刪除物件。如果指定此欄位及 `Retention-Expiration-Date`，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 `DefaultRetention` 期間。零 (`0`) 是合法值，假設儲存區最小保留期間也為 `0`。|
| `Retention-expiration-date` | 日期（ISO 8601 格式）|在此日期將可以合法刪除或修改物件。您只能指定此項或 Retention-Period 標頭。如果兩者都指定，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 DefaultRetention 期間。|
| `Retention-legal-hold-id` |字串   | 要套用至物件的單一合法保留。合法保留是 Y 字元長字串。除非已移除與物件相關聯的所有合法保留，否則無法改寫或刪除物件。|

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

### 新增或移除受保護物件的合法保留
{: #java-examples-immutable-legal-hold}

物件可支援 100 個合法保留：

*  合法保留 ID 是長度上限為 64 個字元的字串，且長度下限為 1 個字元。有效字元為字母、數字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 及 `。
* 如果給定合法保留新增數超過物件上的 100 個合法保留總數，則不會新增合法保留，將會傳回 `400` 錯誤。
* 如果 ID 太長，將不會新增到物件中，且會傳回 `400` 錯誤。
* 如果 ID 包含無效的字元，則不會將它新增至物件，且會傳回 `400` 錯誤。
* 如果某個 ID 已在物件上使用，則不會修改現有合法保留，且回應會指出 ID 已在使用中，並且有 `409` 錯誤。
* 如果物件沒有保留期間 meta 資料，則會傳回 `400` 錯誤，且不容許新增或移除合法保留。

需要有保留期間標頭，否則會傳回 `400` 錯誤。
{: http}

正在新增或移除合法保留的使用者必須具有此儲存區的 `Manager` 許可權。

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

### 延長受保護物件的保留期間
{: #java-examples-immutable-extend}

只能延長物件的保留期間。不能從目前配置的值縮短。

保留擴充值以三種方式之一來設定：

* 現行值再加上時間（`Additional-Retention-Period` 或類似方法）
* 新擴充期間（以秒為單位）（`Extend-Retention-From-Current-Time` 或類似方法）
* 物件的新保留到期日（`New-Retention-Expiration-Date` 或類似方法）

儲存在物件 meta 資料中的現行保留期間，會增加給定的額外時間，或以新值取代，視 `extendRetention` 要求中設定的參數而定。在所有情況下，會針對現行保留期間檢查延長保留參數，而且只有在更新的保留期間大於現行保留期間時，才會接受延長的參數。

不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。

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

### 列出受保護物件的合法保留
{: #java-examples-immutable-list-holds}

此作業傳回：

* 物件建立日期
* 物件保留期間（秒）
* 根據期間和建立日期計算的保留到期日
* 合法保留的清單
* 合法保留 ID
* 套用合法保留時的時間戳記

如果物件沒有任何合法保留，則會傳回空的 `LegalHoldSet`。如果在物件上未指定保留期間，則會傳回 `404` 錯誤。

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
