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

# Java 사용
{: #java}

{{site.data.keyword.cos_full}} SDK for Java는 포괄적이며, 이 안내서에 설명되어 있지 않은 기능을 포함하고 있습니다. 자세한 클래스 및 메소드 문서는 [이 Javadoc](https://ibm.github.io/ibm-cos-sdk-java/)을 참조하십시오. 소스 코드는 [GitHub 저장소](https://github.com/ibm/ibm-cos-sdk-java)에서 찾을 수 있습니다. 

## SDK 가져오기
{: #java-install}

{{site.data.keyword.cos_full_notm}} Java SDK를 이용하는 가장 쉬운 방법은 Maven을 사용하여 종속 항목을 관리하는 것입니다. Maven에 대해 잘 모르는 경우에는 [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) 안내서를 사용하여 이에 대해 알아볼 수 있습니다. 

Maven은 `pom.xml`이라는 파일을 사용하여 Java 프로젝트에 필요한 라이브러리(및 해당 버전)를 지정합니다. 아래 내용은 {{site.data.keyword.cos_full_notm}} Java SDK를 사용하여 {{site.data.keyword.cos_short}}에 연결하기 위한 `pom.xml` 파일의 예입니다. 


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

## 1.x.x에서의 마이그레이션
{: #java-migrate}

이 SDK의 2.0 릴리스에서는 애플리케이션이 기존 AWS 라이브러리를 사용하여 동일한 애플리케이션 또는 환경 내의 AWS 리소스에 연결할 수 있도록 하는 네임스페이스 변경사항이 도입되었습니다. 1.x에서 2.0으로 마이그레이션하려면 몇 가지 변경이 필요합니다. 

1. pom.xml에서 모든 `ibm-cos-java-sdk` 종속 항목 버전 태그를 `2.0.0`으로 변경하여 Maven을 업데이트하십시오. pom.xml에 버전이 `2.0.0` 이전인 SDK 모듈 종속 항목이 없는 것을 확인하십시오. 
2. `amazonaws`에서 `ibm.cloud.objectstorage`로 가져오기 선언을 업데이트하십시오. 


## 클라이언트 작성 및 인증 정보 제공
{: #java-credentials}

다음 예에서는 인증 정보(API 키 및 서비스 인스턴스 ID) 제공을 통해 클라이언트 `cos`가 작성되고 구성됩니다. 이러한 값은 인증 정보 파일 또는 환경 변수로부터 자동으로 가져올 수도 있습니다. 

[서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)를 생성하고 나면 결과 JSON 문서를 `~/.bluemix/cos_credentials`에 저장할 수 있습니다. 이 SDK는 클라이언트 작성 중에 다른 인증 정보가 명시적으로 설정되지 않은 한 이 파일에서 자동으로 인증 정보를 가져옵니다. `cos_credentials` 파일이 HMAC 키를 포함하는 경우에는 클라이언트가 서명을 사용하여 인증하며, 그렇지 않은 경우에는 제공된 API 키를 사용하여 Bearer 토큰으로 인증합니다. 

AWS S3에서 마이그레이션하는 경우에는 다음 형식으로 `~/.aws/credentials`에서 인증 정보 데이터를 가져올 수도 있습니다. 

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

`~/.bluemix/cos_credentials`와 `~/.aws/credentials`가 모두 있는 경우에는 `cos_credentials`가 우선권을 갖습니다. 

 클라이언트 생성에 대한 세부사항은 [이 Javadoc](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html)을 참조하십시오. 

## 코드 예
{: #java-examples}

먼저 몇 가지 기본 기능을 실행하는 완전한 클래스 예를 살펴본 후, 각 클래스를 개별적으로 살펴보겠습니다. 이 `CosExample` 클래스는 기존 버킷에 있는 오브젝트를 나열하고, 새 버킷을 작성한 후 서비스 인스턴스에 있는 모든 버킷을 나열합니다.  

### 필수 정보 수집
{: #java-examples-prereqs}

* `bucketName` 및 `newBucketName`은 [고유하며 DNS를 준수하는](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket) 문자열입니다. 버킷 이름은 전체 시스템에서 고유하므로 이 예가 여러 번 실행되는 경우에는 이러한 값을 변경해야 합니다. 이름은 삭제 후에도 10 - 15분 정도 예약되어 있다는 점을 참고하십시오. 
* `api_key`는 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)에서 `apikey`로 찾을 수 있는 값입니다. 
* `service_instance_id`는 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)에서 `resource_instance_id`로 찾을 수 있는 값입니다.  
* `endpoint_url`은 `https://` 프로토콜을 포함하는 서비스 엔드포인트 URL입니다. 이는 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)에서 찾을 수 있는 `endpoints` 값이 **아닙니다**. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 
* `storageClass`는 `endpoint` 값에 대응하는 [유효한 프로비저닝 코드](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)입니다. 이는 S3 API `LocationConstraint` 변수로 사용됩니다. 
* `location`은 `storageClass`의 위치 부분으로 설정되어야 합니다. `us-south-standard`의 경우 이는 `us-south`가 됩니다. 이 변수는 [HMAC 서명](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac#hmac)의 계산에만 사용되지만, IAM API 키를 사용하는 이 예를 비롯하여 모든 클라이언트에서 필수입니다. 

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

### 구성 초기화
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

*키 값*
* `<endpoint>` - Cloud Object Storage의 공용 엔드포인트입니다([IBM Cloud 대시보드](https://cloud.ibm.com/resources){:new_window}에서 사용 가능). 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 
* `<api-key>` - 서비스 인증 정보를 작성할 때 생성된 API 키입니다(작성 및 삭제 예의 경우 쓰기 액세스 권한 필요). 
* `<resource-instance-id>` - Cloud Object Storage의 리소스 ID입니다([IBM Cloud CLI](/docs//docs/cli?topic=cloud-cli-idt-cli) 또는 [IBM Cloud 대시보드](https://cloud.ibm.com/resources){:new_window}를 통해 사용 가능). 
* `<location>` - Cloud Object Storage의 기본 위치입니다(`<endpoint>`에 대해 사용된 지역과 일치해야 함). 

*SDK 참조*
* 클래스
    * [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){:new_window}
    * [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){:new_window}
    * [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){:new_window}
    * [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){:new_window}
    * [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){:new_window}
    * [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){:new_window}
    * [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}

### 엔드포인트 판별
{: #java-examples-endpoint}

아래 메소드는 버킷 위치, 엔드포인트 유형(공용 또는 개인용) 및 특정 지역(선택사항)에 따라 서비스 엔드포인트를 판별하는 데 사용할 수 있습니다. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 

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

### 새 버킷 작성
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

#### 다른 스토리지 클래스로 버킷 작성
{: #java-examples-storage-class}

`LocationConstraint`에 대한 유효한 프로비저닝 코드의 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)에서 참조할 수 있습니다. 

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

*SDK 참조*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){:new_window}



### 새 텍스트 파일 작성
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
오브젝트에 사용자 정의 메타데이터를 추가하는 경우에는 이 SDK를 사용하여 `ObjectMetadata` 오브젝트를 작성해야 하며, `x-amz-meta-{key}`를 포함하는 사용자 정의 헤더를 수동으로 전송하지 않아야 한다는 점을 참고하십시오. 후자는 HMAC 인증 정보를 사용하여 인증할 때 문제를 일으킬 수 있습니다.
{: .tip}

### 파일에서 오브젝트 업로드
{: #java-examples-upload}

이 예에서는 버킷 `sample`이 이미 있다고 가정합니다. 

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### 스트림을 사용한 오브젝트 업로드
{: #java-examples-stream}

이 예에서는 버킷 `sample`이 이미 있다고 가정합니다. 

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

### 오브젝트를 파일로 다운로드
{: #java-examples-download}

이 예에서는 버킷 `sample`이 이미 있다고 가정합니다. 

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


### 스트림을 사용한 오브젝트 다운로드
{: #java-examples-download-stream}

이 예에서는 버킷 `sample`이 이미 있다고 가정합니다. 

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

### 오브젝트 복사
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

*SDK 참조*
* 클래스
    * [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){:new_window}
    * [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){:new_window}
* 메소드
    * [putObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){:new_window}


#### putObject 예외
{: #java-examples-put-exception}

putObject 메소드는 새 오브젝트 업로드가 성공한 경우에도 다음 예외 처리(throw)를 할 수 있습니다. 
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

**근본 원인:** JAXB API는 Java EE API로 간주되며, Java SE 9에서는 더 이상 기본 클래스 경로에 포함되어 있지 않습니다. 

**수정사항:** 프로젝트 폴더에 있는 pom.xml 파일에 다음 항목을 추가하고 프로젝트를 다시 패키지하십시오. 
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
``` 

### 사용 가능한 버킷 나열
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
*SDK 참조*
* 클래스
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){:new_window}
* 메소드
    * [listBuckets](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){:new_window}

### 버킷에 있는 항목 나열(v2)
{: #java-examples-list-objects-v2}

[AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){:new_window} 오브젝트에는 컨텐츠를 나열하는 데 사용할 수 있는 업데이트된 메소드([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window})가 포함되어 있습니다. 이 메소드는 리턴되는 레코드의 수를 제한하고 레코드를 배치로 검색할 수 있도록 합니다. 이는 애플리케이션에서 결과에 대한 페이징을 수행하고 성능을 향상시키는 데 유용합니다. 

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

*SDK 참조*
* 클래스
    * [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){:new_window}
    * [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* 메소드
    * [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){:new_window}
    * [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){:new_window}
    * [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window}
  
### 버킷에 있는 항목 나열(v1)
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

*SDK 참조*
* 클래스
    * [ListObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsRequest.html){:new_window}
    * [ObjectListing](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}
    * [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){:new_window}
* 메소드
    * [listObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectListing.html){:new_window}

### 특정 항목의 파일 컨텐츠 가져오기
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

*SDK 참조*
* 클래스
    * [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){:new_window}
* 메소드
    * [getObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){:new_window}

### 버킷에서 항목 삭제
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```
*SDK 참조*
* 메소드
    * [deleteObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){:new_window}

### 버킷에서 여러 항목 삭제
{: #java-examples-delete-objects}

삭제 요청은 최대 1000개의 삭제할 키를 포함할 수 있습니다. 이는 요청당 오버헤드를 줄이는 데 매우 유용하지만, 많은 수의 키를 삭제하는 경우에는 주의하십시오. 또한, 적합한 성능을 보장하기 위한 오브젝트 크기도 고려하십시오.
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

*SDK 참조*
* 클래스
    * [DeleteObjectsRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){:new_window}
    * [DeleteObjectsResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){:new_window}
    * [DeleteObjectsResult.DeletedObject](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){:new_window}
* 메소드
    * [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){:new_window}
  
### 버킷 삭제
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

*SDK 참조*
* 메소드
    * [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){:new_window}


### 오브젝트를 공용으로 읽을 수 있는지 확인
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

*SDK 참조*
* 클래스
    * [AccessControlList](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){:new_window}
    * [Grant](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){:new_window}
* 메소드 
    * [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){:new_window}

### 다중 파트 업로드 실행
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
*SDK 참조*
* 클래스
    * [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){:new_window}
    * [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){:new_window}
    * [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){:new_window}
    * [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){:new_window}
    * [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){:new_window}
    * [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){:new_window}

* 메소드
    * [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){:new_window}
    * [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){:new_window}
    * [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){:new_window}
    * [uploadPart](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){:new_window}

## 전송 관리자를 사용한 대형 오브젝트 업로드
{: #java-examples-transfer-manager}

`TransferManager`는 필요할 때마다 구성 매개변수를 설정하여 자동으로 다중 파트 업로드를 포함시킴으로써 대형 파일 전송을 간소화합니다. 

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

*SDK 참조*
* 클래스
    * [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){:new_window}
    * [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){:new_window}
    * [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){:new_window}

* 메소드
    * [shutdownNow](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){:new_window}
    * [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){:new_window}
    * [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){:new_window}
    

## Key Protect 사용
{: #java-examples-kp}
클라우드의 민감한 저장 데이터를 암호화하기 위해 Key Protect를 스토리지 버킷에 추가할 수 있습니다. 

### 시작하기 전에
{: #java-examples-kp-prereqs}

Key Protect가 사용으로 설정된 버킷을 작성하려면 다음 항목이 필요합니다. 

* [프로비저닝](/docs/services/key-protect?topic=key-protect-provision#provision)된 Key Protect 서비스
* 사용 가능한 루트 키([생성](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) 또는 [가져오기](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)를 통해 확보)

### 루트 키 CRN 검색
{: #java-examples-kp-root}

1. Key Protect 서비스의 [인스턴스 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)를 검색하십시오. 
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)를 사용하여 [사용 가능한 키](https://cloud.ibm.com/apidocs/key-protect)를 모두 검색하십시오. 
    * `curl` 명령을 사용하거나 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)과 같은 API REST 클라이언트를 사용하여 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)에 액세스할 수 있습니다. 
3. 버킷에서 Key Protect를 사용으로 설정하는 데 사용할 루트 키의 CRN을 검색하십시오. 이 CRN은 아래와 같습니다. 

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect가 사용으로 설정된 버킷 작성
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
*키 값*
* `<algorithm>` - 버킷에 추가된 새 오브젝트에 사용되는 암호화 알고리즘입니다(기본값은 AES256). 
* `<root-key-crn>` - Key Protect 서비스로부터 얻은 루트 키의 CRN입니다. 

*SDK 참조*
* 클래스
    * [CreateBucketRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){:new_window}
    * [EncryptionType](https://ibm.github.io/ibm-cos-sdk-java/){:new_window}
* 메소드
    * [createBucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){:new_window}

### Key Protect용 새 헤더
{: #java-examples-kp-headers}

`Headers` 클래스에 추가 헤더가 정의되었습니다. 

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

이미 IAM 서비스 인스턴스 헤더를 추가하고 있는 버킷 작성 구현의 동일한 섹션이 2개의 새 암호화 헤더를 추가합니다. 

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

`ObjectListing` 및 `HeadBucketResult` 오브젝트는 Getter 및 Setter 메소드로 부울 `IBMSSEKPEnabled` 및 문자열 `IBMSSEKPCustomerRootKeyCrn` 변수를 포함하도록 업데이트되었습니다. 이들 항목이 새 헤더의 값을 저장합니다. 

#### GET 버킷
{: #java-examples-kp-list}
```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

`ObjectListing` 클래스가 2개의 추가 메소드를 요구합니다. 

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

추가 헤더가 `Headers` 클래스에 정의되었습니다. 

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

S3XmlResponseHandler는 모든 XML 응답의 역마샬링을 담당합니다. 결과가 `ObjectListing`의 인스턴스인 확인이 추가되었으며 검색된 헤더는 `ObjectListing` 오브젝트에 추가됩니다. 

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

#### HEAD 버킷
{: #java-examples-kp-head}
Headers 클래스에 추가 헤더가 정의되었습니다. 

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

이러한 변수는 HeadBucketResponseHandler에서 채워집니다. 

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Aspera 고속 전송 사용
{: #java-examples-aspera}

[Aspera 고속 전송 라이브러리](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)를 설치하면 애플리케이션에서 고속 파일 전송을 이용할 수 있습니다. Aspera 라이브러리는 클로즈드 소스이므로 COS SDK(Apache 라이센스를 사용함)의 선택적 종속 항목입니다.  

각 Aspera 고속 전송 세션은 전송을 수행하기 위해 클라이언트 시스템에서 실행되는 개별 `ascp` 프로세스를 생성합니다. 자신의 컴퓨팅 환경이 이 프로세스의 실행을 허용하는지 확인하십시오.
{:tip}

`AsperaTransferManager`를 초기화하려면 S3 클라이언트 및 IAM 토큰 관리자 클래스의 인스턴스가 필요합니다. `s3Client`는 COS 대상 버킷의 FASP 연결 정보를 가져오는 데 필요합니다. `tokenManager`는 Aspera 고속 전송 SDK가 COS 대상 버킷에 인증할 수 있도록 하는 데 필요합니다. 

### `AsperaTransferManager` 초기화
{: #java-examples-aspera-init}

`AsperaTransferManager`를 초기화하기 전에 작동하는 [`s3Client`](#java-examples-config) 및 [`tokenManager`](#java-examples-config) 오브젝트가 있는지 확인하십시오.  

네트워크에서 심각한 잡음 또는 패킷 손실이 발생할 것으로 예상되는 경우가 아니면, Aspera 고속 전송 세션을 하나만 사용하는 것에는 큰 이점이 없습니다. 따라서 여기서는 `AsperaConfig` 클래스를 사용하여 `AsperaTransferManager`에 여러 세션을 사용하도록 지시할 것입니다. 이는 **임계값** 값으로 크기가 정의된 데이터 청크를 전송하는 병렬 **세션**의 수만큼 전송을 분할합니다. 

다중 세션 사용에 대한 일반적인 구성은 다음과 같습니다. 
* 2500MBps의 목표 속도
* 100MB의 임계값(*이는 대부분의 애플리케이션에 대해 권장되는 값임*)

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
위 예에서 이 SDK는 목표 속도인 2500MBps에 도달하기 위해 충분한 세션을 생성합니다. 

또는 SDK에서 세션 관리를 명시적으로 구성할 수도 있습니다. 이는 네트워크 이용을 더 정확하게 제어해야 하는 경우 유용합니다. 

명시적 다중 세션 사용에 대한 일반적인 구성은 다음과 같습니다. 
* 2개 또는 10개의 세션
* 100MB의 임계값(*이는 대부분의 애플리케이션에 대해 권장되는 값임*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withTokenManager(tokenManager)
    .withAsperaConfig(asperaConfig)
    .build();
```

대부분의 시나리오에서 최상의 성능을 얻으려면 항상 여러 세션을 사용하여 Aspera 고속 전송의 인스턴스화와 연관된 오버헤드를 최소화하십시오. **네트워크 용량이 1Gbps 이상인 경우에는 10개의 세션을 사용해야 합니다.** 이보다 느린 대역폭에서는 두 개의 세션을 사용해야 합니다.
{:tip}

*키 값*
* `API_KEY` - Writer 또는 Manager 역할이 있는 사용자 또는 서비스 ID의 API 키

`AsperaTransferManager`를 생성하기 위한 IAM API 키를 제공해야 합니다. [HMAC 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window}는 현재 지원되지 **않습니다**. IAM에 대한 자세한 정보를 보려면 [여기를 클릭](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)하십시오.
{:tip}

### 파일 업로드
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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-source-data>` - Object Storage에 업로드할 디렉토리 및 파일 이름입니다. 
* `<item-name>` - 버킷에 추가되는 새 오브젝트의 이름입니다. 

### 파일 다운로드
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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-file>` - Object Storage에서 저장할 디렉토리 및 파일 이름입니다. 
* `<item-name>` - 버킷에 있는 오브젝트의 이름입니다. 

### 디렉토리 업로드
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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-directory>` - Object Storage에 업로드될 파일의 디렉토리입니다. 
* `<virtual-directory-prefix>` - 업로드 시 각 파일에 추가될 디렉토리 접두부의 이름입니다. 버킷 루트에 파일을 업로드하려면 널 또는 비어 있는 문자열을 사용하십시오. 

### 디렉토리 다운로드
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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-directory>` - Object Storage에서 다운로드한 파일을 저장할 디렉토리입니다. 
* `<virtual-directory-prefix>` - 다운로드할 각 파일의 디렉토리 접두부의 이름입니다. 버킷에 있는 모든 파일을 다운로드하려면 널 또는 비어 있는 문자열을 사용하십시오. 

### 각 전송별로 세션 구성 대체
{: #java-examples-aspera-config}

오버로드된 업로드 및 다운로드 메소드에 `AsperaConfig`의 인스턴스를 전달하여 다중 세션 구성 값을 전송별로 대체할 수 있습니다. `AsperaConfig`를 사용하면 세션 수와 세션당 최소 파일 임계값 크기를 지정할 수 있습니다.  

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

### 전송 진행상태 모니터링
{: #java-examples-aspera-monitor}

파일/디렉토리 전송의 진행상태를 모니터하는 가장 간단한 방법은 전송이 완료되면 `true`를 리턴하는 `isDone()` 특성을 사용하는 것입니다. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

`AsperaTransaction`에 대해 `onQueue` 메소드를 호출하여 전송이 처리를 위해 큐에 삽입되었는지 확인할 수도 있습니다. `onQueue`는 전송이 큐에 삽입되었음을 나타내는, 값이 `true`인 부울을 리턴합니다. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

전송이 진행 중인지 확인하려면 `AsperaTransaction`의 progress 메소드를 호출하십시오. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

모든 전송에는 기본적으로 `TransferProgress`가 첨부됩니다. `TransferProgress`는 전송된 바이트 수와 전송할 총 바이트 수 대비 전송된 바이트 수의 백분율을 보고합니다. 특정 전송의 `TransferProgress`에 액세스하려면 `AsperaTransaction`의 `getProgress` 메소드를 사용하십시오. 

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

전송된 바이트 수를 보고하려면 `TransferProgress`에 대해 `getBytesTransferred` 메소드를 호출하십시오. 총 바이트 수 대비 전송된 바이트 수의 백분율을 보고하려면 `TransferProgress`에 대해 `getPercentTransferred` 메소드를 호출하십시오. 

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

### 일시정지/재개/취소
{: #java-examples-aspera-pause}

이 SDK는 `AsperaTransfer` 오브젝트의 다음 메소드를 통해 파일/디렉토리 전송의 진행상태를 관리하는 기능을 제공합니다. 

* `pause()`
* `resume()`
* `cancel()`

위에 간략히 설명된 메소드를 호출하는 데는 부작용이 없습니다. 적절한 정리 및 하우스키핑은 SDK에 의해 처리됩니다.
{:tip}

다음 예는 이러한 메소드를 사용하는 방법 중 하나를 보여줍니다. 

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

### Aspera 문제점 해결
{: #java-examples-aspera-ts}

**문제:** Linux 또는 Mac OS X에서 Oracle JDK를 사용 중인 개발자의 경우에는 전송 중에 예기치 않은 무기록 충돌이 발생할 수 있습니다. 

**원인:** 기본 코드는 고유 신호 핸들러를 필요로 하며 이것이 JVM의 신호 핸들러를 대체하고 있을 수 있습니다. JVM의 신호 체인 기능을 사용해야 할 수 있습니다. 

*IBM&reg; JDK 사용자 또는 Microsoft&reg; Windows 사용자는 영향을 받지 않습니다. *

**해결책:** JVM의 신호 체인 라이브러리를 링크하여 로드하십시오. 
* Linux에서 `libjsig.so` 공유 라이브러리를 찾고 다음 환경 변수를 설정하십시오. 
    * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* Mac OS X에서 공유 라이브러리 `libjsig.dylib`를 찾고 다음 환경 변수를 설정하십시오. 
    * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib` 
    * `DYLD_FORCE_FLAT_NAMESPACE=0`

신호 체인에 대한 자세한 정보를 얻으려면 [Oracle&reg; JDK 문서](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){:new_window}를 방문하십시오. 

**문제:** Linux의 `UnsatisfiedLinkError`

**원인:** 시스템이 종속 라이브러리를 로드할 수 없습니다. 애플리케이션 로그에 다음과 같은 오류가 표시될 수 있습니다. 

```
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**해결책:** 다음 환경 변수를 설정하십시오. 

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
## 메타데이터 업데이트
{: #java-examples-metadata}
기존 오브젝트의 메타데이터를 업데이트하는 데는 두 가지 방법이 있습니다. 
* 새 메타데이터와 원본 오브젝트 컨텐츠를 사용한 `PUT` 요청
* 원본 오브젝트를 복사 소스로 지정하여, 새 메타데이터로 `COPY` 요청 실행

### PUT을 사용한 메타데이터 업데이트
{: #java-examples-metadata-put}

**참고:** `PUT` 요청은 오브젝트의 기존 컨텐츠를 겹쳐쓰므로 먼저 이를 다운로드한 후 새 메타데이터로 다시 업로드해야 합니다. 

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

### COPY를 사용한 메타데이터 업데이트
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

## 불변 오브젝트 스토리지 사용
{: #java-examples-immutable}

### 기존 버킷에 보호 구성 추가
{: #java-examples-immutable-enable}

이 `PUT` 오퍼레이션 구현은 `protection` 조회 매개변수를 사용하여 기존 버킷의 보존 매개변수를 설정합니다. 이 오퍼레이션을 사용하면 최소, 기본 및 최대 보존 기간을 설정하거나 변경할 수 있습니다. 이 오퍼레이션을 사용하면 버킷의 보호 상태를 변경할 수도 있습니다.  

보호된 버킷에 작성된 오브젝트는 보호 기간이 만료되어 오브젝트에 대한 모든 법적 보존이 제거될 때까지 삭제할 수 없습니다. 오브젝트가 작성될 때 오브젝트 고유 값이 제공되지 않으면 버킷의 기본 보존 값이 오브젝트에 지정됩니다. 보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다.  

보존 기간 설정 `MinimumRetention`, `DefaultRetention` 및 `MaximumRetention`에 대해 지원되는 최소 및 최대값은 각각 0일과 365243일(1000년)입니다.  

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

### 버킷에 대한 보호 확인
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

### 보호된 오브젝트 업로드
{: #java-examples-immutable-upload}

보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다. 

|값 | 유형 |설명 |
| --- | --- | --- | 
|`Retention-Period` | 음수가 아닌 정수(초) | 오브젝트에 저장할 보존 기간(초)입니다. 오브젝트는 보존 기간에 지정된 시간이 경과하기 전까지 겹쳐쓰거나 삭제할 수 없습니다. 이 필드와 `Retention-Expiration-Date`가 모두 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 `DefaultRetention` 기간이 사용됩니다. 영(`0`)은 버킷의 최소 보존 기간도 `0`이라고 가정하는 적법한 값입니다. |
| `Retention-expiration-date` | 날짜(ISO 8601 형식) | 오브젝트를 적법하게 삭제하거나 수정할 수 있는 날짜입니다. 이 헤더 또는 Retention-Period 헤더만 지정할 수 있습니다. 둘 다 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 DefaultRetention 기간이 사용됩니다. |
| `Retention-legal-hold-id` | 문자열 | 오브젝트에 적용할 단일 법적 보존입니다. 법적 보존은 Y자 길이의 문자열입니다. 오브젝트는 해당 오브젝트와 연관된 모든 법적 보존이 제거될 때까지 겹쳐쓰거나 삭제할 수 없습니다. |

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

### 보호된 오브젝트에서 법적 보존을 추가하거나 제거
{: #java-examples-immutable-legal-hold}

오브젝트는 100개의 법적 보존을 지원할 수 있습니다. 

*  법적 보존 ID는 최대 64자이고 최소 1자인 문자열입니다. 유효한 문자는 문자, 숫자, `!`, `_`, `.`, `*`, `(`, `)`, `-` 및 `입니다. 
* 특정 법적 보존을 추가했을 때 오브젝트의 총 100개 법적 보존 한계를 초과하는 경우에는 해당 새 법적 보존이 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID가 너무 긴 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID에 올바르지 않은 문자가 포함되어 있는 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID가 이미 오브젝트에서 사용 중인 경우에는 기존 법적 보존이 수정되지 않으며 해당 ID가 이미 사용 중임을 나타내는 응답이 `409` 오류와 함께 표시됩니다. 
* 오브젝트에 보존 기간 메타데이터가 없는 경우에는 `400` 오류가 리턴되며 법적 보존 추가 또는 제거가 허용되지 않습니다. 

보존 기간 헤더는 반드시 있어야 하며, 그렇지 않은 경우에는 `400` 오류가 리턴됩니다.
{: http}

법적 보존을 추가하거나 제거하는 사용자에게는 해당 버킷에 대한 `Manager` 권한이 있어야 합니다. 

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

### 보호된 오브젝트의 보존 기간 연장
{: #java-examples-immutable-extend}

오브젝트의 보존 기간은 연장만 가능합니다. 현재 구성된 값에서 줄일 수는 없습니다. 

보존 연장 값은 다음 세 가지 방법 중 하나로 설정됩니다. 

* 현재 값에서의 추가 시간(`Additional-Retention-Period` 또는 이와 유사한 메소드)
* 초 단위의 새 연장 기간(`Extend-Retention-From-Current-Time` 또는 이와 유사한 메소드)
* 오브젝트의 새 보존 만료 날짜(`New-Retention-Expiration-Date` 또는 이와 유사한 메소드)

오브젝트 메타데이터에 저장된 현재 보존 기간은 `extendRetention` 요청에 설정된 매개변수에 따라 지정된 추가 시간만큼 증가되거나, 새 값으로 대체될 수 있습니다. 모든 경우에 보존 연장 매개변수는 현재 보존 기간에 대해 확인되며, 연장된 매개변수는 업데이트된 보존 기간이 현재 보존 기간보다 긴 경우에만 허용됩니다. 

보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다. 

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

### 보호된 오브젝트에 대한 법적 보존 나열
{: #java-examples-immutable-list-holds}

이 오퍼레이션은 다음 항목을 리턴합니다. 

* 오브젝트 작성 날짜
* 오브젝트 보존 기간(초)
* 보존 기간 및 작성 날짜를 기반으로 계산된 보존 만료 날짜
* 법적 보존의 목록
* 법적 보존 ID
* 법적 보존이 적용된 시점의 시간소인

오브젝트에 대한 법적 보존이 없는 경우에는 비어 있는 `LegalHoldSet`이 리턴됩니다.
오브젝트에 대해 지정된 보존 기간이 없는 경우에는 `404` 오류가 리턴됩니다. 

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
