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

# Java の使用
{: #java}

{{site.data.keyword.cos_full}} SDK for Java の機能は広範囲に渡り、このガイドに記載されていない機能もあります。クラスおよびメソッドの詳細な資料については、[Javadoc](https://ibm.github.io/ibm-cos-sdk-java/) を参照してください。ソース・コードは、[GitHub リポジトリー](https://github.com/ibm/ibm-cos-sdk-java)にあります。

## SDK の取得
{: #java-install}

{{site.data.keyword.cos_full_notm}} Java SDK を使用する最も簡単な方法は、Maven を使用して依存関係を管理することです。Maven に慣れていないユーザーは、[Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) ガイドを使用して操作を開始できます。

Maven は、Java プロジェクトに必要なライブラリー (およびライブラリーのバージョン) を指定するために `pom.xml` というファイルを使用します。{{site.data.keyword.cos_full_notm}} Java SDK を使用して {{site.data.keyword.cos_short}} に接続するための `pom.xml` ファイルの例を以下に示します。


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

## 1.x.x からのマイグレーション
{: #java-migrate}

SDK の 2.0 リリースでは、名前空間の変更が導入されました。この変更により、アプリケーションは元の AWS ライブラリーを使用して、同じアプリケーションまたは環境内の AWS リソースに接続できます。1.x から 2.0 にマイグレーションするには、いくつかの変更が必要です。

1. pom.xml 内ですべての `ibm-cos-java-sdk` 依存関係バージョン・タグを `2.0.0` に変更することで、Maven を使用して更新します。pom.xml 内に `2.0.0` より前のバージョンの SDK モジュール依存関係が存在しないことを確認してください。
2. すべてのインポート宣言を `amazonaws` から `ibm.cloud.objectstorage` に更新します。


## クライアントの作成と資格情報の入手
{: #java-credentials}

以下の例では、資格情報 (API キーとサービス・インスタンス ID) を指定することによりクライアント `cos` が作成および構成されます。これらの値は、資格情報ファイルまたは環境変数から自動的に入手することもできます。

[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)を生成した後は、結果の JSON 文書を `~/.bluemix/cos_credentials` に保存できます。クライアントの作成時に他の資格情報が明示的に設定されない限り、SDK は、このファイルから資格情報を自動的に入手します。`cos_credentials` ファイルに HMAC 鍵が含まれている場合、クライアントは署名を使用して認証を受けます。そうでない場合、クライアントはベアラー・トークンと提供される API キーを使用して認証を受けます。

AWS S3 からマイグレーションする場合は、`~/.aws/credentials` から資格情報データを以下の形式で入手することもできます。

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

`~/.bluemix/cos_credentials` と `~/.aws/credentials` の両方が存在する場合は、`cos_credentials` が優先されます。

 クライアントの構造について詳しくは、[Javadoc](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html) を参照してください。

## コード例
{: #java-examples}

まず、いくつかの基本的な機能を実行するサンプル・クラスの全体像を見てみましょう。その後、各クラスを個別に検討します。この `CosExample` クラスは、既存のバケット内のオブジェクトをリストし、新しいバケットを作成し、その後、サービス・インスタンス内のすべてのバケットをリストします。 

### 必要な情報の収集
{: #java-examples-prereqs}

* `bucketName` と `newBucketName` は、[固有かつ DNS セーフの](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)ストリングです。バケット名はシステム全体で固有であるため、この例を複数回実行する場合は、これらの値を変更する必要があります。名前は、削除後も 10 分から 15 分の間は予約済みになることに注意してください。
* `api_key` は、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)内で `apikey` として検出される値です。
* `service_instance_id` は、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)内で `resource_instance_id` として検出される値です。 
* `endpoint_url` は、`https://` プロトコルを含むサービス・エンドポイント URL です。これは、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)内で検出される `endpoints` の値では**ありません**。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
* `storageClass` は、`endpoint` 値に対応する[有効なプロビジョニング・コード](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)です。これはその後、S3 API `LocationConstraint` 変数として使用されます。
* `location` には、`storageClass` のロケーション部分を設定します。`us-south-standard` の場合、これは `us-south` になります。この変数は、[HMAC 署名](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac)の計算のみに使用されますが、IAM API キーを使用するこのサンプルも含め、あらゆるクライアントに必要です。

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

### 構成の初期化
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

*キー値*
* `<endpoint>` - Cloud Object Storage のパブリック・エンドポイント ([IBM Cloud ダッシュボード](https://cloud.ibm.com/resources){:new_window} から入手可能)。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
* `<api-key>` - サービス資格情報の作成時に生成される API キー (作成および削除の例では、書き込みアクセス権限が必要です)
* `<resource-instance-id>` - Cloud Object Storage のリソース ID ([IBM Cloud CLI](/docs//docs/cli?topic=cloud-cli-idt-cli) または [IBM Cloud ダッシュボード](https://cloud.ibm.com/resources){:new_window} から入手可能)
* `<location>` - Cloud Object Storage のデフォルト・ロケーション (`<endpoint>` に使用した地域と一致しなければなりません)

*SDK リファレンス*
* クラス
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### エンドポイントの決定
{: #java-examples-endpoint}

以下のメソッドを使用して、バケット・ロケーション、エンドポイント・タイプ (パブリックまたはプライベート)、および特定の地域 (オプション) に基づいたサービス・エンドポイントを決定できます。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

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

### 新規バケットの作成
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### 異なるストレージ・クラスを使用するバケットの作成
{: #java-examples-storage-class}

`LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラスのガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)を参照してください。

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*SDK リファレンス*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### 新規テキスト・ファイルの作成
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
カスタム・メタデータをオブジェクトに追加する場合は、SDK を使用して `ObjectMetadata` オブジェクトを作成する必要がある点に注意してください。`x-amz-meta-{key}` を含んでいるカスタム・ヘッダーを手動で送信することはしないでください。後者は、HMAC 資格情報を使用した認証のときに問題を引き起こす可能性があります。
{: .tip}

### オブジェクトをファイルからアップロード
{: #java-examples-upload}

この例では、バケット `sample` が既に存在することを前提としています。

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### ストリームを使用したオブジェクトのアップロード
{: #java-examples-stream}

この例では、バケット `sample` が既に存在することを前提としています。

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

### ファイルへのオブジェクトのダウンロード
{: #java-examples-download}

この例では、バケット `sample` が既に存在することを前提としています。

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


### ストリームを使用したオブジェクトのダウンロード
{: #java-examples-download-stream}

この例では、バケット `sample` が既に存在することを前提としています。

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### オブジェクトのコピー
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

*SDK リファレンス*
* クラス
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* メソッド
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### putObject 例外
{: #java-examples-put-exception}

putObject メソッドは、新しいオブジェクトが正常にアップロードされた場合でも以下の例外をスローすることがあります。
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

**根本原因:** JAXB API は、Java EE API と見なされ、Java SE 9 ではデフォルトのクラスパスに含まれなくなりました。

**修正法:** プロジェクト・フォルダー内の pom.xml ファイルに以下のエントリーを追加し、プロジェクトを再パッケージしてください。
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### 使用可能なバケットのリスト
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
*SDK リファレンス*
* クラス
    * [バケット](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* メソッド
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### バケット内の項目のリスト (v2)
{: #java-examples-list-objects-v2}

[AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} オブジェクトには、コンテンツをリストするための、更新されたメソッドが含まれます ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window})。このメソッドでは、返されるレコードの数を制限できるほか、レコードをバッチで取得できます。これは、アプリケーション内で結果をページングする場合に役立ち、パフォーマンスも改善される可能性があります。

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

*SDK リファレンス*
* クラス
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* メソッド
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### バケット内の項目のリスト (v1)
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

*SDK リファレンス*
* クラス
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* メソッド
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### 特定の項目のファイル内容の取得
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

*SDK リファレンス*
* クラス
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* メソッド
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### バケットから項目を削除
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*SDK リファレンス*
* メソッド
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### バケットから複数項目を削除
{: #java-examples-delete-objects}

削除要求には、削除するキーを最大 1000 個含めることができます。これは要求ごとのオーバーヘッドを削減するのに非常に役立ちますが、多数のキーを削除する場合は注意が必要です。また、適切なパフォーマンスを確保するために、オブジェクトのサイズも考慮に入れてください。
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

*SDK リファレンス*
* クラス
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* メソッド
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### バケットの削除
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*SDK リファレンス*
* メソッド
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### オブジェクトがパブリックに読み取り可能かどうかのチェック
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

*SDK リファレンス*
* クラス
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* メソッド 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### 複数パーツ・アップロードの実行
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
*SDK リファレンス*
* クラス
    * [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){:new_window}
    * [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}
    * [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){:new_window}
    * [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){:new_window}

* メソッド
    * [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){:new_window}
    * [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){:new_window}
    * [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){:new_window}
    * [uploadPart](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){:new_window}

## Transfer Manager を使用したより大きなオブジェクトのアップロード
{: #java-examples-transfer-manager}

`TransferManager` は、必要に応じて構成パラメーターを設定して自動的に複数パーツ・アップロードを取り込むことで、大規模ファイル転送を単純化します。

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

*SDK リファレンス*
* クラス
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [アップロード](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* メソッド
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## Key Protect の使用
{: #java-examples-kp}
クラウド内の重要な Data at Rest (保存されたデータ) を暗号化するために、Key Protect をストレージ・バケットに追加できます。

### 始める前に
{: #java-examples-kp-prereqs}

Key-Protect が有効なバケットを作成するためには、以下の項目が必要です。

* [プロビジョン済み](/docs/services/key-protect?topic=key-protect-provision#provision)の Key Protect サービス
* 使用可能なルート・キー ([生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)されたか、[インポート](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)されたもの)

### ルート・キー CRN の取得
{: #java-examples-kp-root}

1. Key Protect サービスの[インスタンス ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) を取得します。
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) を使用して、すべての[使用可能なキー](https://cloud.ibm.com/apidocs/key-protect)を取得します。
    * `curl` コマンドまたは API REST クライアント ([Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) など) のいずれかを使用して、[Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) にアクセスできます。
3. バケットで Key Protect を有効にするために使用するルート・キーの CRN を取得します。CRN は、下記のようになります。

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect が有効なバケットの作成
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
*キー値*
* `<algorithm>` - バケットに追加される新規オブジェクトに使用される暗号化アルゴリズム (デフォルトは AES256)。
* `<root-key-crn>` - Key Protect サービスから取得したルート・キーの CRN。

*SDK リファレンス*
* クラス
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* メソッド
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Key Protect 用の新規ヘッダー
{: #java-examples-kp-headers}

`Headers` クラス内に追加のヘッダーが定義されています。

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

既に IAM サービス・インスタンス・ヘッダーを追加しているバケット作成実装の同じセクションによって、以下の新しい 2 つの暗号化ヘッダーが追加されます。

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

`ObjectListing` オブジェクトと `HeadBucketResult` オブジェクトは、getter メソッドおよび setterメソッドとともに、ブール変数 `IBMSSEKPEnabled` と String 変数 `IBMSSEKPCustomerRootKeyCrn` を組み込むように更新されました。これらに、新しいヘッダーの値が保管されます。

#### GET バケット
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

`ObjectListing` クラスには、以下の 2 つの追加メソッドが必要です。

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

`Headers` クラス内に追加のヘッダーが定義されています。

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

S3XmlResponseHandler は、すべての xml 応答のアンマーシャルを担当します。以下のように、結果が `ObjectListing` のインスタンスであることを確認するチェックが追加され、取得されたヘッダーが `ObjectListing` オブジェクトに追加されます。

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

#### HEAD バケット
{: #java-examples-kp-head}
Headers クラス内に追加のヘッダーが定義されています。

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

これらの変数は、HeadBucketResponseHandler に取り込まれます。

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Aspera 高速転送の使用
{: #java-examples-aspera}

[Aspera 高速転送ライブラリー](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)をインストールすると、アプリケーション内で高速ファイル転送を使用できます。Aspera ライブラリーはクローズ・ソースであるため、(Apache ライセンスを使用する) COS SDK のオプションの依存関係です。 

各 Aspera 高速転送セッションは、転送を実行するためにクライアント・マシン上で実行される個別の `ascp` プロセスを作成します。ご使用のコンピューティング環境でこのプロセスの実行が許可されるようにしてください。
{:tip}

`AsperaTransferManager` を初期化するために、S3 Client クラスと IAM Token Manager クラスのインスタンスが必要になります。`s3Client` は、COS ターゲット・バケットの FASP 接続情報を取得するために必要です。`tokenManager` は、Aspera 高速転送 SDK が COS ターゲット・バケットで認証されるために必要です。

### `AsperaTransferManager` の初期化
{: #java-examples-aspera-init}

`AsperaTransferManager` を初期化する前に、正常に機能する [`s3Client`](#java-examples-config) オブジェクトと [`tokenManager`](#java-examples-config) オブジェクトが存在することを確認してください。 

ネットワークで大量のノイズやパケット・ロスが発生することが予想される場合を除き、Aspera 高速転送を単一セッションで使用する利点はあまりありません。そのため、`AsperaConfig` クラスを使用して、複数のセッションを使用するように `AsperaTransferManager` に指示する必要があります。そうすると、**しきい値**で定義されたサイズのデータをチャンクで送信する多数の並列**セッション**に転送が分割されます。

マルチセッションを使用する場合の標準的な構成は、以下のとおりです。
* 2500 MBps ターゲット・レート
* 100 MB のしきい値 (*これが、ほとんどのアプリケーションに対する推奨値です*)

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
上記の例の場合、SDK は 2500 MBps のターゲット・レートに到達しようと、それに見合った十分な数のセッションを作成します。

代わりの方法として、セッション管理を SDK で明示的に構成することもできます。これは、ネットワーク使用率をより正確に制御する必要がある場合に役立ちます。

明示的なマルチセッションを使用する場合の標準的な構成は、以下のとおりです。
* 2 個または 10 個のセッション
* 100 MB のしきい値 (*これが、ほとんどのアプリケーションに対する推奨値です*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

最高のパフォーマンスを得るには、ほとんどのシナリオで、Aspera 高速転送のインスタンス化に関連するオーバーヘッドを最小限に抑えるために、常に複数のセッションを使用してください。**ネットワーク容量が 1 Gbps 以上ある場合は、10 個のセッションを使用してください。**それより低い帯域幅のネットワークでは、使用するセッションを 2 つにする必要があります。
{:tip}

*キー値*
* `API_KEY` - ライターまたはマネージャーの役割を持つユーザーまたはサービス ID の API キー

`AsperaTransferManager` を構成するための IAM API キーを指定する必要があります。[HMAC 資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} は、現在、サポートされて**いません**。IAM について詳しくは、[ここをクリック](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)してください。
{:tip}

### ファイルのアップロード
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

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前。
* `<absolute-path-to-source-data>` - Object Storage にアップロードするディレクトリーおよびファイル名。
* `<item-name>` - バケットに追加される新規オブジェクトの名前。

### ファイルのダウンロード
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

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前。
* `<absolute-path-to-file>` - Object Storage から保存する先のディレクトリーおよびファイル名。
* `<item-name>` - バケット内のオブジェクトの名前。

### ディレクトリーのアップロード
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

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前。
* `<absolute-path-to-directory>` - Object Storage にアップロードされるファイルがあるディレクトリー。
* `<virtual-directory-prefix>` - アップロード時に各ファイルに追加されるディレクトリー接頭部の名前。ファイルをバケット・ルートにアップロードする場合は、ヌルまたは空ストリングを使用します。

### ディレクトリーのダウンロード
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

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前。
* `<absolute-path-to-directory>` - Object Storage からダウンロードされたファイルを保存するディレクトリー。
* `<virtual-directory-prefix>` - ダウンロードされる各ファイルのディレクトリー接頭部の名前。バケット内のすべてのファイルをダウンロードする場合は、ヌルまたは空ストリングを使用します。

### 転送単位でのセッション構成のオーバーライド
{: #java-examples-aspera-config}

`AsperaConfig` のインスタンスをアップロードまたはダウンロードの多重定義メソッドに渡すことで、転送ごとにマルチセッション構成の値をオーバーライドできます。`AsperaConfig` を使用して、セッション数およびセッションごとの最小ファイルしきい値サイズを指定できます。 

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

### 転送の進行状況のモニター
{: #java-examples-aspera-monitor}

ファイル/ディレクトリー転送の進行状況をモニターする最も簡単な方法は、転送の完了時に `true` を返す `isDone()` プロパティーを使用することです。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

また、`AsperaTransaction` の `onQueue` メソッドを呼び出すことで、転送が処理のためにキューに入れられているかどうかを確認することもできます。`onQueue` は、転送がキューに入れられたことを示す `true` のブール値を返します。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

転送が進行中かどうかを確認するには、`AsperaTransaction` 内で progress メソッドを呼び出します。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

デフォルトで、すべての転送に `TransferProgress` が付加されます。`TransferProgress` は、転送済みのバイト数および転送される合計バイト数に対する転送済みパーセンテージを報告します。転送の `TransferProgress` にアクセスするには、`AsperaTransaction` の `getProgress` メソッドを使用します。

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

転送されたバイト数を報告するには、`TransferProgress` の `getBytesTransferred` メソッドを呼び出します。転送される合計バイト数に対する転送済みパーセンテージを報告するには、`TransferProgress` の `getPercentTransferred` メソッドを呼び出します。

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

### 一時停止/再開/キャンセル
{: #java-examples-aspera-pause}

SDK には、`AsperaTransfer` オブジェクトの以下のメソッドを使用して、ファイル/ディレクトリー転送の進行を管理できる機能があります。

* `pause()`
* `resume()`
* `cancel()`

上記のいずれのメソッドを呼び出しても副次作用はありません。クリーンアップとハウスキーピングは、SDK によって適切に処理されます。
{:tip}

以下に、これらのメソッドの使用例を示します。

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

### Aspera に関する問題のトラブルシューティング
{: #java-examples-aspera-ts}

**問題:** 開発者が Linux または Mac OS X 上で Oracle JDK を使用している場合、転送中に予期しない異常終了やサイレント・クラッシュが発生することがあります。

**原因:** ネイティブ・コードには、独自のシグナル・ハンドラーが必要ですが、それが JVM のシグナル・ハンドラーをオーバーライドする場合があります。JVM のシグナル・チェーニング機能を使用することが必要な場合があります。

*IBM&reg; JDK ユーザーまたは Microsoft&reg; Windows ユーザーは、影響を受けません。*

**解決策:** JVM のシグナル・チェーニング・ライブラリーをリンクしてロードしてください。
* Linux の場合、`libjsig.so` 共有ライブラリーを見つけ、以下の環境変数を設定します。
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* Mac OS X の場合、共有ライブラリー `libjsig.dylib` を見つけ、以下の環境変数を設定します。
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

シグナル・チェーニングについて詳しくは、[Oracle&reg; JDK 資料](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window} にアクセスしてください。

**問題:** Linux での `UnsatisfiedLinkError`

**原因:** システムが従属ライブラリーをロードできません。アプリケーション・ログに以下のようなエラーが表示される場合があります。

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**解決策:** 以下の環境変数を設定してください。

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
## メタデータの更新
{: #java-examples-metadata}
既存のオブジェクトのメタデータを更新する方法は、2 とおりあります。
* 新しいメタデータと元のオブジェクト・コンテンツが指定された `PUT` 要求
* 元のオブジェクトをコピー・ソースとして指定し、新しいメタデータを使用する `COPY` 要求の実行

### PUT を使用したメタデータの更新
{: #java-examples-metadata-put}

**注:** `PUT` 要求は、オブジェクトの既存のコンテンツを上書きするものです。このため、まずオブジェクトをダウンロードし、新しいメタデータを指定して再度アップロードする必要があります。

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

### COPY を使用したメタデータの更新
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

## Immutable Object Storage の使用
{: #java-examples-immutable}

### 既存のバケットへの保護構成の追加
{: #java-examples-immutable-enable}

`PUT` 操作のこの実装では、`protection` 照会パラメーターを使用して、既存のバケットに保存パラメーターを設定します。この操作により、保存期間の最小、デフォルト、および最大を設定または変更できます。また、この操作により、バケットの保護状態を変更することもできます。 

保護対象のバケットに書き込まれたオブジェクトは、保護期間が満了し、オブジェクトに対するすべての法的保留が解除されるまで削除できません。オブジェクトの作成時にオブジェクト固有の値が指定されない限り、バケットのデフォルトの保存期間値がオブジェクトにも適用されます。保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。 

保存期間設定 `MinimumRetention`、`DefaultRetention`、および `MaximumRetention` でサポートされる最小値と最大値は、それぞれ 0 日と 365243 日 (1000 年) です。 

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

### バケットに対する保護のチェック
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

### 保護オブジェクトのアップロード
{: #java-examples-immutable-upload}

保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。

|値	| タイプ	| 説明 |
| --- | --- | --- | 
|`Retention-Period` | 負でない整数 (秒数) | オブジェクトを保管する保存期間 (秒数)。オブジェクトは、保存期間に指定された時間が経過するまで上書きも削除もできません。このフィールドと `Retention-Expiration-Date` が指定された場合、`400` エラーが返されます。いずれも指定されない場合は、バケットの `DefaultRetention` 期間が使用されます。ゼロ (`0`) は有効な値であり、バケットの最小保存期間も `0` であると想定されます。 |
| `Retention-expiration-date` | 日付 (ISO 8601 フォーマット) | オブジェクトを合法的に削除または変更できるようになる日付。これか Retention-Period ヘッダーのいずれかのみを指定できます。両方が指定された場合は、`400` エラーが返されます。いずれも指定されない場合は、バケットの DefaultRetention 期間が使用されます。 |
| `Retention-legal-hold-id` | ストリング | オブジェクトに適用される単一の法的保留。法的保留とは、Y 文字の長さのストリングです。オブジェクトに関連付けられたすべての法的保留が解除されるまで、オブジェクトは上書きも削除もできません。 |

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

### 保護オブジェクトに対する法的保留の追加または解除
{: #java-examples-immutable-legal-hold}

オブジェクトがサポートできる法的保留は 100 個です。

*  法的保留 ID は、最大長 64 文字かつ最小長 1 文字のストリングです。有効な文字は、英字、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-`、および ` です。
* 指定された法的保留を追加することで、オブジェクトに対する法的保留の合計数が 100 個を超える場合、新しい法的保留は追加されず、`400` エラーが返されます。
* ID が長すぎる場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID に無効文字が含まれている場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID がオブジェクトで既に使用中の場合、既存の法的保留は変更されず、ID が既に使用中であることを示す応答と `409` エラーが返されます。
* オブジェクトに保存期間メタデータがない場合は、`400` エラーが返され、法的保留の追加または削除は許可されません。

保存期間ヘッダーが存在する必要があります。存在しない場合は、`400` エラーが返されます。
{: http}

法的保留の追加または削除を行うユーザーには、このバケットに対する`マネージャー`許可が必要です。

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

### 保護オブジェクトの保存期間の延長
{: #java-examples-immutable-extend}

オブジェクトの保存期間は延長のみが可能です。現在構成されている値から減らすことはできません。

保存延長値は次の 3 つの方法のいずれかで設定されます。

* 現行値からの追加時間 (`Additional-Retention-Period` または同様のメソッド)
* 新しい延長期間 (秒数) (`Extend-Retention-From-Current-Time` または同様のメソッド)
* オブジェクトの新しい保存有効期限日付 (`New-Retention-Expiration-Date` または同様のメソッド)

オブジェクト・メタデータに保管されている現在の保存期間は、`extendRetention` 要求に設定されているパラメーターに応じて、指定された追加時間分増やされるか、新しい値に置き換えられます。いずれの場合も、保存延長パラメーターは現在の保存期間に対して検査され、更新後の保存期間が現在の保存期間より大きい場合にのみ、延長パラメーターが受け入れられます。

保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。

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

### 保護オブジェクトの法的保留のリスト
{: #java-examples-immutable-list-holds}

この操作で返される内容は以下のとおりです。

* オブジェクト作成日
* オブジェクト保存期間 (秒数)
* 期間および作成日に基づいて計算された保存有効期限
* 法的保留のリスト
* 法的保留 ID
* 法的保留が適用されたときのタイム・スタンプ

オブジェクトに法的保留がない場合は、空の `LegalHoldSet` が返されます。
オブジェクトに保存期間が指定されていない場合は、`404` エラーが返されます。

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
