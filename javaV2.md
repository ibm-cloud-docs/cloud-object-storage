---

copyright:
  years: 2017, 2026
lastupdated: "2026-04-09"

keywords: cloud object storage, javaV2, sdk

subcollection: cloud-object-storage

---

# Using Java V2
{: #java-v2}

The {{site.data.keyword.cos_full}} SDK for Java v2 provides features to make the most of {{site.data.keyword.cos_full_notm}}.

The {{site.data.keyword.cos_full_notm}} SDK for Java v2 is comprehensive, with many features and capabilities that exceed the scope and space of this guide. For detailed class and method documentation, see the [Java API reference documentation](https://ibm.github.io/ibm-cos-sdk-java-v2/). Source code can be found in the [GitHub repository](https://github.com/IBM/ibm-cos-sdk-java-v2).

## What's New in v2
{: #java-v2-whatsnew}

The {{site.data.keyword.cos_full_notm}} SDK for Java v2 is a modernized version that is built on the AWS SDK v2 architecture, bringing significant improvements:

- **Immutable Builders**: All request and response objects use immutable builder patterns for better thread safety
- **Modern Package Structure**: New namespace `com.ibm.cos.v2.*` with cleaner organization
- **Enhanced Async Support**: Introduction of `S3AsyncClient` for nonblocking operations
- **Improved Streaming**: Better streaming APIs with `RequestBody` and `ResponseTransformer`
- **Automatic IAM Token Management**: SDK handles IAM token refresh automatically
- **Type Safety**: Enhanced compile-time type checking with builders
- **Modern HTTP Stack**: Support for Apache HTTP Client and Netty for async operations

For developers migrating from v1, see the [Migration Guide](https://github.com/IBM/ibm-cos-sdk-java-v2/blob/main/MIGRATION_GUIDE_V2.md).

## Getting the SDK
{: #java-v2-install}

The easiest way to use the {{site.data.keyword.cos_full_notm}} Java SDK v2 is to use Maven to manage dependencies. If you aren't familiar with Maven, you can get up and running by using the [Maven in 5-Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) guide.

Maven uses a file that is called `pom.xml` to specify the libraries (and their versions) needed for a Java project. Here is an example `pom.xml` file for using the {{site.data.keyword.cos_full_notm}} Java SDK v2 to connect to Object Storage.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos.v2</groupId>
            <artifactId>cos-java-sdk</artifactId>
            <version>1.0.1</version>
        </dependency>
    </dependencies>
</project>
```

## SDK References
{: #java-v2-reference}

### Core Classes
{: #java-v2-reference-core-classes}

- [S3Client](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html) - Synchronous S3 client
- [S3AsyncClient](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3AsyncClient.html) - Asynchronous S3 client
- [S3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3ClientBuilder.html) - Builder for S3 client

### Credentials
{: #java-v2-reference-credentials}

- [AwsCredentials](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/auth/credentials/AwsCredentials.html) - Base credentials interface
- [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/auth/credentials/ibmOAuth/BasicIBMOAuthCredentials.html) - IBM IAM credentials
- [AwsBasicCredentials](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/auth/credentials/AwsBasicCredentials.html) - HMAC credentials
- [StaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/auth/credentials/StaticCredentialsProvider.html) - Static credentials provider
- [AwsCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/auth/credentials/AwsCredentialsProvider.html) - Credentials provider interface

### Configuration
{: #java-v2-reference-configurations}

- [Region](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/regions/Region.html) - AWS region representation
- [ClientOverrideConfiguration](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/client/config/ClientOverrideConfiguration.html) - Client configuration overrides
- [S3Configuration](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Configuration.html) - S3-specific configuration

### Streaming
{: #java-v2-reference-streaming}

- [RequestBody](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/sync/RequestBody.html) - Request body for uploads
- [ResponseTransformer](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/sync/ResponseTransformer.html) - Response transformer for downloads
- [ResponseInputStream](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/ResponseInputStream.html) - Input stream response

### Exceptions
{: #java-v2-reference-exceptions}

- [S3Exception](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/S3Exception.html) - Base S3 exception
- [NoSuchBucketException](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/NoSuchBucketException.html) - Bucket not found
- [NoSuchKeyException](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/NoSuchKeyException.html) - Object not found
- [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/exception/SdkClientException.html) - Client-side exception

## Creating a client and sourcing credentials
{: #java-v2-credentials}

In the following example, a client `cos` is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.

After generating a [Service Credential](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), the resulting JSON document can be saved to `~/.bluemix/cos_credentials`. The SDK will automatically source credentials from this file unless other credentials are explicitly set during client creation. If the `cos_credentials` file contains HMAC keys the client authenticates with a signature, otherwise the client uses the provided API key to authenticate by using a bearer token.

If migrating from AWS S3, you can also source credentials data from `~/.aws/credentials` in the format:

```sh
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

If both `~/.bluemix/cos_credentials` and `~/.aws/credentials` exist, `cos_credentials` takes preference.

For more details on client construction, see the [Java API reference documentation](https://ibm.github.io/ibm-cos-sdk-java-v2/).

## Code Examples
{: #java-v2-examples}

Let's start with a complete example class that runs through some basic functionality. This `CosExample` class lists objects in an existing bucket, create a new bucket, and then list all buckets in the service instance.

### Gather required information
{: #java-v2-examples-prereqs}

- `bucketName` and `newBucketName` are [unique and DNS-safe](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket) strings. Because bucket names are unique across the entire system, these values need to be changed if this example is run multiple times. The names are reserved for 10 - 15 minutes after deletion.
- `apiKey` is the value found in the [Service Credential](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) as `apikey`.
- `serviceInstanceId` is the value found in the [Service Credential](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) as `resource_instance_id`.
- `endpointUrl` is a service endpoint URL, inclusive of the `https://` protocol. This is **not** the `endpoints` value found in the [Service Credential](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials). For more information about endpoints, see [Endpoints and storage locations](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).
- `location` must be set to the location portion of the `storageClass`. For `us-south-standard`, this would be `us-south`. This variable is used only for the calculation of [HMAC signatures](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-hmac), but is required for any client, including this example that uses an IAM API key.

```java
package com.cos;

import java.net.URI;
import java.util.List;

import com.ibm.cos.v2.auth.credentials.AwsCredentials;
import com.ibm.cos.v2.auth.credentials.StaticCredentialsProvider;
import com.ibm.cos.v2.auth.credentials.ibmOAuth.BasicIBMOAuthCredentials;
import com.ibm.cos.v2.regions.Region;
import com.ibm.cos.v2.services.s3.S3Client;
import com.ibm.cos.v2.services.s3.model.*;

public class CosExample {

    private static String COS_ENDPOINT = "<endpoint>";
    private static String COS_API_KEY_ID = "<api-key>";
    private static String COS_SERVICE_INSTANCE_ID = "<service-instance-id>";
    private static String COS_LOCATION = "<location>";

    public static void main(String[] args) {
        System.out.println("Current time: " + new java.util.Date());

        // Create IBM COS credentials using API key
        AwsCredentials credentials = new BasicIBMOAuthCredentials(COS_API_KEY_ID, COS_SERVICE_INSTANCE_ID);

        // Create the S3 client
        S3Client cosClient = S3Client.builder()
                .endpointOverride(URI.create(COS_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(COS_LOCATION))
                .build();

        listObjects("my-bucket", cosClient);
        createBucket("my-new-bucket", cosClient);
        listBuckets(cosClient);

        cosClient.close();
    }

    public static void listObjects(String bucketName, S3Client cosClient) {
        System.out.println("Listing objects in bucket: " + bucketName);

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = cosClient.listObjectsV2(request);
        List<S3Object> objects = response.contents();

        for (S3Object object : objects) {
            System.out.println("Item: " + object.key() + " (" + object.size() + " bytes)");
        }
    }

    public static void createBucket(String bucketName, S3Client cosClient) {
        System.out.println("Creating bucket: " + bucketName);

        CreateBucketRequest request = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        cosClient.createBucket(request);
        System.out.println("Bucket created: " + bucketName);
    }

    public static void listBuckets(S3Client cosClient) {
        System.out.println("Listing buckets:");

        ListBucketsResponse response = cosClient.listBuckets();
        List<Bucket> buckets = response.buckets();

        for (Bucket bucket : buckets) {
            System.out.println("Bucket Name: " + bucket.name());
        }
    }
}
```

### Key References
{: #java-v2-key-reference}

- [S3Client](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html)
- [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/auth/credentials/ibmOAuth/BasicIBMOAuthCredentials.html)
- [StaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/auth/credentials/StaticCredentialsProvider.html)

### Determining Endpoint
{: #java-v2-endpoint}

For more information about endpoints, see [Endpoints and storage locations](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

Endpoints follow this pattern: `https://s3.{region}.cloud-object-storage.appdomain.cloud`

Examples:
- US South: `https://s3.us-south.cloud-object-storage.appdomain.cloud`
- US East: `https://s3.us-east.cloud-object-storage.appdomain.cloud`
- EU Great Britain: `https://s3.eu-gb.cloud-object-storage.appdomain.cloud`

### Creating a new bucket
{: #java-v2-examples-new-bucket}

```java
public static void createBucket(String bucketName, S3Client cosClient) {
    System.out.println("Creating new bucket: " + bucketName);

    CreateBucketRequest request = CreateBucketRequest.builder()
            .bucket(bucketName)
            .build();

    cosClient.createBucket(request);

    System.out.println("Bucket: " + bucketName + " created!");
}
```

### Key Reference
{: #java-v2-create-bucket-key-reference}

- [CreateBucketRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CreateBucketRequest.Builder.html)
- [createBucket](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#createBucket-com.ibm.cos.v2.services.s3.model.CreateBucketRequest-)

## Create a bucket with a different storage class
{: #java-v2-examples-storage-class}

```java
public static void createBucketWithStorageClass(String bucketName, String storageClass, String location, S3Client cosClient) {
    System.out.println("Creating bucket: " + bucketName + " with storage class: " + storageClass);

    CreateBucketRequest request = CreateBucketRequest.builder()
            .bucket(bucketName)
            .ibmServiceInstanceId(COS_SERVICE_INSTANCE_ID)
            .build();

    cosClient.createBucket(request);

    System.out.println("Bucket: " + bucketName + " created with " + storageClass + " class storage!");
}
```

Storage class options include:
- `us-south-standard` / `us-east-standard` / `eu-gb-standard` - Standard storage
- `us-south-vault` / `us-east-vault` / `eu-gb-vault` - Vault storage
- `us-south-cold` / `us-east-cold` / `eu-gb-cold` - Cold Vault storage
- `us-south-flex` / `us-east-flex` / `eu-gb-flex` - Flex storage

### Key Reference
{: #java-v2-create-bucket-kr}

- [CreateBucketRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CreateBucketRequest.Builder.html)

## Creating a new text file
{: #java-v2-examples-text-file}

```java
public static void createTextFile(String bucketName, String itemName, String fileText, S3Client cosClient) {
    System.out.println("Creating new item: " + itemName);

    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .build();

    cosClient.putObject(request, RequestBody.fromString(fileText));

    System.out.println("Item: " + itemName + " created!");
}
```

### Key References
{: #java-v2-create-text-file-kr}

- [PutObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutObjectRequest.Builder.html)
- [RequestBody](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/sync/RequestBody.html)
- [putObject](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#putObject-com.ibm.cos.v2.services.s3.model.PutObjectRequest-com.ibm.cos.v2.core.sync.RequestBody-)

## Upload object from a file
{: #java-v2-examples-upload}

```java
public static void putObject(String bucketName, String itemName, String filePath, S3Client cosClient) {
    System.out.println("Creating new item: " + itemName);

    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .build();

    cosClient.putObject(request, RequestBody.fromFile(new File(filePath)));

    System.out.println("Item: " + itemName + " created!");
}
```

### Key References
{: #java-v2-upload-object-kr}

- [PutObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutObjectRequest.Builder.html)
- [RequestBody.fromFile](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/sync/RequestBody.html#fromFile-java.nio.file.Path-)

## Upload object by using a stream
{: #java-v2-examples-stream}

```java
public static void putObjectStream(String bucketName, String itemName, InputStream inputStream, long contentLength, S3Client cosClient) {
    System.out.println("Creating new item: " + itemName + " from input stream");

    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .contentLength(contentLength)
            .build();

    cosClient.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));

    System.out.println("Item: " + itemName + " created!");
}
```

### Key References
{: #java-v2-upload-object-kr}

- [RequestBody.fromInputStream](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/sync/RequestBody.html#fromInputStream-java.io.InputStream-long-)

## Download object to a file
{: #java-v2-examples-download}

```java
public static void getObject(String bucketName, String itemName, String filePath, S3Client cosClient) {
    System.out.println("Retrieving item: " + itemName);

    GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .build();

    cosClient.getObject(request, ResponseTransformer.toFile(new File(filePath)));

    System.out.println("Item: " + itemName + " downloaded to: " + filePath);
}
```

### Key References
{: #java-v2-download-object-kr}


- [GetObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/GetObjectRequest.Builder.html)
- [ResponseTransformer.toFile](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/sync/ResponseTransformer.html#toFile-java.nio.file.Path-)
- [getObject](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#getObject-com.ibm.cos.v2.services.s3.model.GetObjectRequest-com.ibm.cos.v2.core.sync.ResponseTransformer-)

## Download object by using a stream
{: #java-v2-examples-download-stream}

```java
public static void getObjectStream(String bucketName, String itemName, S3Client cosClient) {
    System.out.println("Retrieving item: " + itemName + " as stream");

    GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .build();

    ResponseInputStream<GetObjectResponse> response = cosClient.getObject(request);

    try (InputStream inputStream = response) {
        // Process the input stream
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            // Process bytes
            System.out.write(buffer, 0, bytesRead);
        }
    } catch (IOException e) {
        System.err.println("Error reading object: " + e.getMessage());
    }

    System.out.println("\nItem: " + itemName + " retrieved!");
}
```

### Key References
{: #java-v2-download-stream-kr}

- [ResponseInputStream](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/ResponseInputStream.html)
- [GetObjectResponse](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/GetObjectResponse.html)

## Copy objects
{: #java-v2-copy-object}

```java
public static void copyObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey, S3Client cosClient) {
    System.out.println("Copying item: " + sourceKey + " from bucket: " + sourceBucketName +
                       " to: " + destinationKey + " in bucket: " + destinationBucketName);

    CopyObjectRequest request = CopyObjectRequest.builder()
            .sourceBucket(sourceBucketName)
            .sourceKey(sourceKey)
            .destinationBucket(destinationBucketName)
            .destinationKey(destinationKey)
            .build();

    cosClient.copyObject(request);

    System.out.println("Item: " + sourceKey + " copied!");
}
```

### Key References
{: #java-v2-copy-object-kr}

- [CopyObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CopyObjectRequest.Builder.html)
- [copyObject](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#copyObject-com.ibm.cos.v2.services.s3.model.CopyObjectRequest-)

## List available buckets
{: #java-v2-available-bucket}

```java
public static void listBuckets(S3Client cosClient) {
    System.out.println("Listing buckets:");

    ListBucketsResponse response = cosClient.listBuckets();
    List<Bucket> buckets = response.buckets();

    for (Bucket bucket : buckets) {
        System.out.println("Bucket Name: " + bucket.name());
    }
}
```

### Key References
{: #java-v2-available-bucket-kr}


- [ListBucketsResponse](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ListBucketsResponse.html)
- [Bucket](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/Bucket.html)
- [listBuckets](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#listBuckets--)

## Get file contents of particular item
{: #java-v2-content}

```java
public static void getItem(String bucketName, String itemName, S3Client cosClient) {
    System.out.println("Retrieving item: " + itemName);

    GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .build();

    ResponseInputStream<GetObjectResponse> response = cosClient.getObject(request);

    try {
        String content = new String(response.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("File Contents:\n" + content);
    } catch (IOException e) {
        System.err.println("Error reading object: " + e.getMessage());
    }
}
```

### Key References
{: #java-v2-content-kr}

- [GetObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/GetObjectRequest.Builder.html)
- [ResponseInputStream](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/ResponseInputStream.html)

## Delete an item from a bucket
{: #java-v2-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName, S3Client cosClient) {
    System.out.println("Deleting item: " + itemName);

    DeleteObjectRequest request = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .build();

    cosClient.deleteObject(request);

    System.out.println("Item: " + itemName + " deleted!");
}
```

### Key References
{: #java-v2-delete-object-kr}

- [DeleteObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/DeleteObjectRequest.Builder.html)
- [deleteObject](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#deleteObject-com.ibm.cos.v2.services.s3.model.DeleteObjectRequest-)

## Delete multiple items from a bucket
{: #java-v2-examples-delete-objects}

```java
public static void deleteMultipleItems(String bucketName, List<String> itemNames, S3Client cosClient) {
    System.out.println("Deleting multiple items from bucket: " + bucketName);

    List<ObjectIdentifier> objectsToDelete = itemNames.stream()
            .map(key -> ObjectIdentifier.builder().key(key).build())
            .collect(Collectors.toList());

    Delete delete = Delete.builder()
            .objects(objectsToDelete)
            .build();

    DeleteObjectsRequest request = DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete(delete)
            .build();

    DeleteObjectsResponse response = cosClient.deleteObjects(request);

    System.out.println("Deleted " + response.deleted().size() + " items!");
}
```

### Key References
{: #java-v2-delete-objects-kr}

- [DeleteObjectsRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/DeleteObjectsRequest.Builder.html)
- [ObjectIdentifier.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ObjectIdentifier.Builder.html)
- [deleteObjects](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#deleteObjects-com.ibm.cos.v2.services.s3.model.DeleteObjectsRequest-)

## Delete a bucket
{: #java-v2-examples-delete-bucket}

The bucket must be empty before it can be deleted.
{: note}

```java
public static void deleteBucket(String bucketName, S3Client cosClient) {
    System.out.println("Deleting bucket: " + bucketName);

    DeleteBucketRequest request = DeleteBucketRequest.builder()
            .bucket(bucketName)
            .build();

    cosClient.deleteBucket(request);

    System.out.println("Bucket: " + bucketName + " deleted!");
}
```

### Key References
{: #java-v2-delete-bucket-kr}

- [DeleteBucketRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/DeleteBucketRequest.Builder.html)
- [deleteBucket](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#deleteBucket-com.ibm.cos.v2.services.s3.model.DeleteBucketRequest-)

## Check if an object is publicly readable
{: #java-v2-examples-public-check}

```java
public static boolean isObjectPubliclyReadable(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Checking if object is publicly readable: " + objectKey);

    try {
        GetObjectAclRequest request = GetObjectAclRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectAclResponse response = cosClient.getObjectAcl(request);

        for (Grant grant : response.grants()) {
            if (grant.grantee().type() == Type.GROUP &&
                grant.grantee().uri() != null &&
                grant.grantee().uri().contains("AllUsers") &&
                grant.permission() == Permission.READ) {
                System.out.println("Object is publicly readable");
                return true;
            }
        }

        System.out.println("Object is not publicly readable");
        return false;
    } catch (S3Exception e) {
        System.err.println("Error checking object ACL: " + e.getMessage());
        return false;
    }
}
```

### Key References
{: #java-v2-public-check-kr}

- [GetObjectAclRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/GetObjectAclRequest.Builder.html)
- [Grant](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/Grant.html)
- [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/S3Client.html#getObjectAcl-com.ibm.cos.v2.services.s3.model.GetObjectAclRequest-)

## Execute a multi-part upload
{: #java-v2-examples-multipart-object}

When working with larger objects, multipart upload operations are recommended to write objects into {{site.data.keyword.cos_full_notm}}. An upload of a single object can be performed as a set of parts and these parts can be uploaded independently in any order and in parallel. Upon upload completion, {{site.data.keyword.cos_full_notm}} then presents all parts as a single object.

Multipart uploads are only available for objects larger than 5 MB. For objects smaller than 50 GB, a part size of 20 MB to 100 MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact.

```java
public static void multipartUpload(String bucketName, String itemName, String filePath, S3Client cosClient) {
    System.out.println("Starting multipart upload for: " + itemName);

    // Step 1: Initiate multipart upload
    CreateMultipartUploadRequest initiateRequest = CreateMultipartUploadRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .build();

    CreateMultipartUploadResponse initiateResponse = cosClient.createMultipartUpload(initiateRequest);
    String uploadId = initiateResponse.uploadId();
    System.out.println("Upload ID: " + uploadId);

    // Step 2: Upload parts
    int partNumber = 1;
    List<CompletedPart> completedParts = new ArrayList<>();
    ByteBuffer buffer = ByteBuffer.allocate(5 * 1024 * 1024); // 5 MB parts

    try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
        long fileSize = file.length();
        long position = 0;

        while (position < fileSize) {
            file.seek(position);
            long bytesRead = file.getChannel().read(buffer);

            buffer.flip();

            UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                    .bucket(bucketName)
                    .key(itemName)
                    .uploadId(uploadId)
                    .partNumber(partNumber)
                    .build();

            UploadPartResponse partResponse = cosClient.uploadPart(
                    uploadPartRequest,
                    RequestBody.fromByteBuffer(buffer)
            );

            CompletedPart part = CompletedPart.builder()
                    .partNumber(partNumber)
                    .eTag(partResponse.eTag())
                    .build();
            completedParts.add(part);

            System.out.println("Uploaded part " + partNumber);

            buffer.clear();
            position += bytesRead;
            partNumber++;
        }
    } catch (IOException e) {
        System.err.println("Error during multipart upload: " + e.getMessage());

        // Abort the multipart upload on error
        AbortMultipartUploadRequest abortRequest = AbortMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(itemName)
                .uploadId(uploadId)
                .build();
        cosClient.abortMultipartUpload(abortRequest);
        return;
    }

    // Step 3: Complete multipart upload
    CompletedMultipartUpload completedUpload = CompletedMultipartUpload.builder()
            .parts(completedParts)
            .build();

    CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
            .bucket(bucketName)
            .key(itemName)
            .uploadId(uploadId)
            .multipartUpload(completedUpload)
            .build();

    cosClient.completeMultipartUpload(completeRequest);

    System.out.println("Multipart upload completed for: " + itemName);
}
```

### Key References
{: #java-v2-multipart-object-kr}

- [CreateMultipartUploadRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CreateMultipartUploadRequest.Builder.html)
- [UploadPartRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/UploadPartRequest.Builder.html)
- [CompleteMultipartUploadRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CompleteMultipartUploadRequest.Builder.html)
- [CompletedPart.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CompletedPart.Builder.html)
- [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/AbortMultipartUploadRequest.html)

## Using Immutable Object Storage
{: #java-v2-immutable-object-storage}

Users can configure buckets with an Immutable Object Storage policy to prevent objects from being modified or deleted for a defined period. The retention period can be specified on a per-object basis, or objects can inherit a default retention period set on the bucket.

## Adding a protection configuration to a bucket
{: #java-v2-examples-protection-configuration}

This implementation of the `PUT` operation uses the `protection` query parameter to set the retention parameters for an existing bucket. This operation allows you to set or change the minimum, default, and maximum retention period. This operation also allows you to change the protection state of the bucket.

Objects written to a protected bucket cannot be deleted until the protection period has expired and all legal holds on the object are removed. The bucket's default retention value is given to an object unless an object-specific value is provided when the object is created. Objects in protected buckets that are no longer under retention (the retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

The minimum and maximum supported values for the retention period settings `MinimumRetention`, `DefaultRetention`, and `MaximumRetention` are a minimum of 0 days and a maximum of 365243 days (1000 years).

```java
public static void addProtectionConfigurationToBucket(String bucketName, S3Client cosClient) {
    System.out.println("Adding protection configuration to bucket: " + bucketName);

    BucketProtectionConfiguration protectionConfig = BucketProtectionConfiguration.builder()
            .status(BucketProtectionStatus.RETENTION)
            .minimumRetention(BucketProtectionMinimumRetention.builder()
                    .days(10)
                    .build())
            .defaultRetention(BucketProtectionDefaultRetention.builder()
                    .days(100)
                    .build())
            .maximumRetention(BucketProtectionMaximumRetention.builder()
                    .days(1000)
                    .build())
            .build();

    PutBucketProtectionConfigurationRequest request = PutBucketProtectionConfigurationRequest.builder()
            .bucket(bucketName)
            .protectionConfiguration(protectionConfig)
            .build();

    cosClient.putBucketProtectionConfiguration(request);

    System.out.println("Protection configuration added!");
}
```

## Getting a bucket's protection configuration
{: #java-v2-examples-get-protection-configuration}

```java
public static void getProtectionConfigurationOnBucket(String bucketName, S3Client cosClient) {
    System.out.println("Retrieving protection configuration for bucket: " + bucketName);

    GetBucketProtectionConfigurationRequest request = GetBucketProtectionConfigurationRequest.builder()
            .bucket(bucketName)
            .build();

    GetBucketProtectionConfigurationResponse response = cosClient.getBucketProtectionConfiguration(request);

    System.out.println("Status: " + response.status());
    System.out.println("Minimum Retention (days): " + response.minimumRetention().days());
    System.out.println("Default Retention (days): " + response.defaultRetention().days());
    System.out.println("Maximum Retention (days): " + response.maximumRetention().days());
}
```

## Uploading a protected object with retention
{: #java-v2-examples-upload-protected-object}

Objects in protected buckets that are no longer under retention (the retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

You can specify retention parameters when uploading objects to protected buckets by using custom headers:

| Header | Type | Description |
|--------|------|-------------|
| `Retention-Period` | Nonnegative integer (seconds) | Retention period to store on the object in seconds. The object can be neither overwritten nor deleted until the amount of time that is specified in the retention period has elapsed. If this field and `Retention-Expiration-Date` are specified a 400 error is returned. If neither is specified the bucket's DefaultRetention period will be used. Zero (0) is a legal value assuming the bucket's minimum retention period is also 0. |
| `Retention-Expiration-Date` | Date (ISO 8601 Format) | The date on which it is legal to delete or modify the object. You can only specify this or the `Retention-Period` header. If both are specified a 400 error will be returned. If neither is specified the bucket's DefaultRetention period will be used. |
| `Retention-Legal-Hold-Id` | string | A single legal hold to apply to the object. A legal hold is a Y-character long string. The object cannot be overwritten or deleted until all legal holds associated with the object are removed. |

```java
public static void uploadProtectedObject(String bucketName, String objectKey, String filePath, S3Client cosClient) {
    System.out.println("Uploading protected object with retention: " + objectKey);

    // Option 1: Upload with retention period (in seconds)
    PutObjectRequest requestWithPeriod = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .retentionPeriod(2592000L)  // 30 days in seconds
            .build();

    cosClient.putObject(requestWithPeriod, RequestBody.fromFile(new File(filePath)));
    System.out.println("Object uploaded with 30-day retention period");
}

public static void uploadProtectedObjectWithExpirationDate(String bucketName, String objectKey, String filePath, S3Client cosClient) {
    System.out.println("Uploading protected object with expiration date: " + objectKey);

    // Option 2: Upload with retention expiration date
    Instant expirationDate = Instant.now().plus(90, ChronoUnit.DAYS);

    PutObjectRequest requestWithDate = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .retentionExpirationDate(expirationDate)
            .build();

    cosClient.putObject(requestWithDate, RequestBody.fromFile(new File(filePath)));
    System.out.println("Object uploaded with expiration date: " + expirationDate);
}

public static void uploadProtectedObjectWithLegalHold(String bucketName, String objectKey, String filePath, String legalHoldId, S3Client cosClient) {
    System.out.println("Uploading protected object with legal hold: " + objectKey);

    // Option 3: Upload with legal hold
    PutObjectRequest requestWithLegalHold = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .retentionLegalHoldId(legalHoldId)
            .build();

    cosClient.putObject(requestWithLegalHold, RequestBody.fromFile(new File(filePath)));
    System.out.println("Object uploaded with legal hold: " + legalHoldId);
}

public static void uploadProtectedObjectWithMultipleRetentionOptions(String bucketName, String objectKey, String filePath, String legalHoldId, S3Client cosClient) {
    System.out.println("Uploading protected object with retention period and legal hold: " + objectKey);

    // Option 4: Combine retention period with legal hold
    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .retentionPeriod(2592000L)  // 30 days
            .retentionLegalHoldId(legalHoldId)
            .build();

    cosClient.putObject(request, RequestBody.fromFile(new File(filePath)));
    System.out.println("Object uploaded with retention and legal hold");
}
```

**Important Notes:**
- You cannot specify both `Retention-Period` and `Retention-Expiration-Date` in the same request.
- If neither retention parameter is specified, the bucket's default retention period is applied.
- Legal holds can be combined with retention periods.
- Objects under retention or with legal holds cannot be deleted or overwritten until the retention expires and all legal holds are removed.

### Key References
{: #java-v2-upload-protected-object-kr}

- [PutObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutObjectRequest.Builder.html)

## Adding a legal hold to a protected object
{: #java-v2-example-add-legal-hold}

The object can support 100 legal holds:

A legal hold identifier is a string of a maximum length of 64 characters and a minimum length of 1 character. Valid characters are letters, numbers, `!`, `_`, `.`, `*`, `(`, `)`, `-`, and `'`.
If the addition of the given legal hold exceeds 100 total legal holds on the object, the new legal hold will not be added, a `400` error will be returned.
If an identifier is too long it will not be added to the object, and a `400` error is returned.
If an identifier contains invalid characters, it will not be added to the object and a `400` error is returned.
If an identifier is already in use on an object, the existing legal hold is not modified and the response indicates that the identifier was already in use with a `409` error.
If an object does not have retention period metadata, a `400` error is returned and adding or removing a legal hold is not allowed.
The presence of a retention period header is required, otherwise a `400` error is returned.

The user making adding or removing a legal hold must have `Manager` permissions for this bucket.

```java
public static void addLegalHoldToObject(String bucketName, String objectKey, String legalHoldId, S3Client cosClient) {
    System.out.println("Adding legal hold to object: " + objectKey);

    AddLegalHoldRequest request = AddLegalHoldRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .legalHoldId(legalHoldId)
            .build();

    cosClient.addLegalHold(request);

    System.out.println("Legal hold added!");
}
```

## Extending the retention period of a protected object
{: #java-v2-example-extend-retention}

The retention period of an object can only be extended. It cannot be decreased from the currently configured value.

The retention expansion value is set in one of three ways:

- **Additional time from the current value** (`additionalRetentionPeriod` or similar method)
- **New extension period in seconds** (`extendRetentionFromCurrentTime` or similar method)
- **New retention expiry date of the object** (`newRetentionExpirationDate` or similar method)

The current retention period that is stored in the object metadata is either increased by the given extra time or replaced with the new value, depending on the parameter that is set in the `extendRetention` request. In all cases, the extended retention parameter is checked against the current retention period and the extended parameter is only accepted if the updated retention period is greater than the current retention period.

Objects in protected buckets that are no longer under retention (the retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

```java
public static void extendRetentionPeriodOnObject(String bucketName, String objectName, Long additionalSeconds, S3Client cosClient) {
    System.out.printf("Extending the retention period on %s in bucket %s by %s seconds.%n", objectName, bucketName, additionalSeconds);

    ExtendObjectRetentionRequest request = ExtendObjectRetentionRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .additionalRetentionPeriod(additionalSeconds)
            .build();

    cosClient.extendObjectRetention(request);

    System.out.printf("Retention period extended on %s by %s seconds%n", objectName, additionalSeconds);
}
```

### Key References
{: #java-v2-extend-retention-kr}

- [ExtendObjectRetentionRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ExtendObjectRetentionRequest.Builder.html)

## Listing legal holds on a protected object
{: #java-v2-example-get-legal-hold}

This operation returns:

- **Object creation date**
- **Object retention period in seconds**
- **Calculated retention expiration date** based on the period and creation date
- **List of legal holds**
  - **Legal hold identifier**
  - **Timestamp when legal hold was applied**

**Important Notes:**
- If there are no legal holds on the object, an empty `LegalHoldSet` is returned
- If there is no retention period that is specified on the object, a `404` error is returned


```java
public static void listLegalHoldsOnObject(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Listing legal holds for object: " + objectKey);

    ListLegalHoldsRequest request = ListLegalHoldsRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

    ListLegalHoldsResponse response = cosClient.listLegalHolds(request);

    for (LegalHold hold : response.legalHolds()) {
        System.out.println("Legal Hold ID: " + hold.id());
        System.out.println("Date: " + hold.date());
    }
}
```

## Deleting a legal hold from a protected object
{: #java-v2-example-delete-legal-hold}

```java
public static void deleteLegalHoldFromObject(String bucketName, String objectKey, String legalHoldId, S3Client cosClient) {
    System.out.println("Deleting legal hold from object: " + objectKey);

    DeleteLegalHoldRequest request = DeleteLegalHoldRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .legalHoldId(legalHoldId)
            .build();

    cosClient.deleteLegalHold(request);

    System.out.println("Legal hold deleted!");
}
```

### Key References
{: #java-v2-delete-legal-hold-kr}

- [BucketProtectionConfiguration.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/BucketProtectionConfiguration.Builder.html)
- [PutBucketProtectionConfigurationRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutBucketProtectionConfigurationRequest.Builder.html)


## Using Key Protect
{: #java-v2-examples-kp}

Key Protect can be added to a storage bucket to encrypt sensitive data at rest in the cloud.

### Creating a bucket with Key Protect
{: #java-v2-create-bucket-kp}

```java
public static void createBucketWithKeyProtect(String bucketName, String kpRootKeyCrn, S3Client cosClient) {
    System.out.println("Creating bucket with Key Protect: " + bucketName);

    CreateBucketRequest request = CreateBucketRequest.builder()
            .bucket(bucketName)
            .ibmSSEKPEncryptionAlgorithm("AES256")
            .ibmSSEKPCustomerRootKeyCrn(kpRootKeyCrn)
            .build();

    cosClient.createBucket(request);

    System.out.println("Bucket created with Key Protect encryption!");
}
```

### Uploading an object to a Key Protect enabled bucket
{: #java-examples-create-kp}

```java
public static void putObjectToKPBucket(String bucketName, String objectKey, String filePath, S3Client cosClient) {
    System.out.println("Uploading object to Key Protect enabled bucket");

    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

    cosClient.putObject(request, RequestBody.fromFile(new File(filePath)));

    System.out.println("Object uploaded with Key Protect encryption!");
}
```

## Upload larger objects using a Transfer Manager
{: #java-v2-examples-create-larger-object}

The Transfer Manager provides a simple API for uploading and downloading objects to and from {{site.data.keyword.cos_full_notm}}.

```java
import com.ibm.cos.v2.transfer.s3.S3TransferManager;
import com.ibm.cos.v2.transfer.s3.model.*;

public static void uploadWithTransferManager(String bucketName, String objectKey, String filePath, S3Client cosClient) {
    System.out.println("Uploading with Transfer Manager: " + objectKey);

    // Create Transfer Manager
    S3TransferManager transferManager = S3TransferManager.builder()
            .s3Client(cosClient)
            .build();

    try {
        // Upload file
        UploadFileRequest uploadRequest = UploadFileRequest.builder()
                .putObjectRequest(req -> req.bucket(bucketName).key(objectKey))
                .source(Paths.get(filePath))
                .build();

        FileUpload upload = transferManager.uploadFile(uploadRequest);

        // Wait for upload to complete
        CompletedFileUpload completedUpload = upload.completionFuture().join();

        System.out.println("Upload completed: " + completedUpload.response().eTag());
    } finally {
        transferManager.close();
    }
}
```

### Key References
{: #java-v2-create-larger-object-kr}

- [S3TransferManager](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/transfer/s3/S3TransferManager.html)
- [UploadFileRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/transfer/s3/model/UploadFileRequest.Builder.html)

## Updating metadata
{: #java-v2-examples-metadata}

### Updating metadata on an existing object
{: #java-v2-update-metadata}

```java
public static void updateObjectMetadata(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Updating metadata for object: " + objectKey);

    // Create new metadata
    Map<String, String> newMetadata = new HashMap<>();
    newMetadata.put("updated-by", "admin");
    newMetadata.put("update-date", LocalDate.now().toString());

    // Copy object to itself with new metadata
    CopyObjectRequest request = CopyObjectRequest.builder()
            .sourceBucket(bucketName)
            .sourceKey(objectKey)
            .destinationBucket(bucketName)
            .destinationKey(objectKey)
            .metadata(newMetadata)
            .metadataDirective(MetadataDirective.REPLACE)
            .build();

    cosClient.copyObject(request);

    System.out.println("Metadata updated!");
}
```

### Key References
{: #java-v2-metadata-kr}

- [CopyObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CopyObjectRequest.Builder.html)
- [MetadataDirective](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/MetadataDirective.html)


## Using Aspera High-Speed Transfer
{: #java-v2-examples-aspera}

Aspera high-speed transfer integration is available through the IBM Aspera SDK. Refer to the [Aspera documentation](https://www.ibm.com/products/aspera) for integration details with {{site.data.keyword.cos_full_notm}}.
{: note}

For Java applications, you can use the Aspera Transfer SDK alongside the {{site.data.keyword.cos_full_notm}} SDK v2 for high-speed transfers of large files.

## Using Object Lock
{: #java-v2-examples-ol}

Object Lock allows you to store objects by using a write-once-read-many (WORM) model. Object Lock can help prevent objects from being deleted or overwritten for a fixed amount of time or indefinitely.

### Creating a bucket with Object Lock enabled
{: #java-v2-examples-create-ol}

Object Lock must be enabled at bucket creation time and cannot be added to an existing bucket.

```java
public static void createBucketWithObjectLock(String bucketName, S3Client cosClient) {
    System.out.println("Creating bucket with Object Lock: " + bucketName);

    CreateBucketRequest request = CreateBucketRequest.builder()
            .bucket(bucketName)
            .objectLockEnabledForBucket(true)
            .build();

    cosClient.createBucket(request);

    System.out.println("Bucket created with Object Lock enabled!");
}
```

### Setting Object Lock retention on an object
{: #java-v2-examples-set-ol}

You can set retention on an object to prevent it from being deleted or overwritten. Two retention modes are available:
- **COMPLIANCE**: Object cannot be overwritten or deleted by any user, including the root user.
- **GOVERNANCE**: Users with special permissions can alter retention settings or delete the object.

```java
public static void putObjectRetention(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Setting object retention for: " + objectKey);

    // Create retention period of 90 days
    Instant retainUntil = Instant.now().plus(90, ChronoUnit.DAYS);

    // Create object lock retention with COMPLIANCE mode
    ObjectLockRetention retention = ObjectLockRetention.builder()
            .mode(ObjectLockRetentionMode.COMPLIANCE)
            .retainUntilDate(retainUntil)
            .build();

    // Apply retention to object
    PutObjectRetentionRequest request = PutObjectRetentionRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .retention(retention)
            .build();

    cosClient.putObjectRetention(request);

    System.out.println("Object retention set until: " + retainUntil);
}
```

### Getting Object Lock retention information
{: #java-v2-examples-get-ol}

```java
public static void getObjectRetention(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Getting object retention for: " + objectKey);

    GetObjectRetentionRequest request = GetObjectRetentionRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

    GetObjectRetentionResponse response = cosClient.getObjectRetention(request);

    System.out.println("Retention Mode: " + response.retention().mode());
    System.out.println("Retain Until: " + response.retention().retainUntilDate());
}
```

### Setting Object Lock legal hold
{: #java-v2-examples-setting-ol}

A legal hold provides the same protection as a retention period, but has no expiration date. Legal holds remain in effect until explicitly removed.

```java
public static void putObjectLegalHold(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Setting legal hold on: " + objectKey);

    ObjectLockLegalHold legalHold = ObjectLockLegalHold.builder()
            .status(ObjectLockLegalHoldStatus.ON)
            .build();

    PutObjectLegalHoldRequest request = PutObjectLegalHoldRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .legalHold(legalHold)
            .build();

    cosClient.putObjectLegalHold(request);

    System.out.println("Legal hold applied!");
}
```

### Getting Object Lock legal hold status
{: #java-v2-examples-get-ol-status}

```java
public static void getObjectLegalHold(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Getting legal hold status for: " + objectKey);

    GetObjectLegalHoldRequest request = GetObjectLegalHoldRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

    GetObjectLegalHoldResponse response = cosClient.getObjectLegalHold(request);

    System.out.println("Legal Hold Status: " + response.legalHold().status());
}
```

### Key References
{: #java-v2-ol-key-reference}

- [ObjectLockRetention.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ObjectLockRetention.Builder.html)
- [PutObjectRetentionRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutObjectRetentionRequest.Builder.html)
- [ObjectLockLegalHold.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ObjectLockLegalHold.Builder.html)


## Using Bucket Lifecycle Configuration
{: #java-v2-examples-ol}

Lifecycle configuration enables you to define actions that {{site.data.keyword.cos_full_notm}} applies to a group of objects. You can use lifecycle policies to transition objects to different storage classes or expire objects after a specified time period.

### Setting a lifecycle configuration on a bucket
{: #java-v2-examples-set-lifecycle-configuration}

```java
public static void putBucketLifecycle(String bucketName, S3Client cosClient) {
    System.out.println("Setting lifecycle configuration on bucket: " + bucketName);

    // Create a transition to move objects to GLACIER after 30 days
    Transition transition = Transition.builder()
            .days(30)
            .storageClass(StorageClass.GLACIER)
            .build();

    // Create lifecycle rule filter
    LifecycleRuleFilter ruleFilter = LifecycleRuleFilter.builder()
            .prefix("archive/")  // Apply to objects with this prefix
            .build();

    // Create lifecycle rule
    LifecycleRule rule = LifecycleRule.builder()
            .id("archive-old-objects")
            .filter(ruleFilter)
            .transitions(transition)
            .status(ExpirationStatus.ENABLED)
            .build();

    // Create lifecycle configuration
    BucketLifecycleConfiguration lifecycleConfig = BucketLifecycleConfiguration.builder()
            .rules(rule)
            .build();

    // Apply lifecycle configuration to bucket
    PutBucketLifecycleConfigurationRequest request = PutBucketLifecycleConfigurationRequest.builder()
            .bucket(bucketName)
            .lifecycleConfiguration(lifecycleConfig)
            .build();

    cosClient.putBucketLifecycleConfiguration(request);

    System.out.println("Lifecycle configuration applied!");
}
```

### Getting a bucket's lifecycle configuration
{: #java-v2-examples-get-lifecycle-configuration}

```java
public static void getBucketLifecycle(String bucketName, S3Client cosClient) {
    System.out.println("Getting lifecycle configuration for bucket: " + bucketName);

    GetBucketLifecycleConfigurationRequest request = GetBucketLifecycleConfigurationRequest.builder()
            .bucket(bucketName)
            .build();

    GetBucketLifecycleConfigurationResponse response = cosClient.getBucketLifecycleConfiguration(request);

    System.out.println("Found " + response.rules().size() + " lifecycle rules:");

    for (LifecycleRule rule : response.rules()) {
        System.out.println("  Rule ID: " + rule.id());
        System.out.println("  Status: " + rule.status());
        if (rule.hasTransitions()) {
            for (Transition t : rule.transitions()) {
                System.out.println("  Transition after " + t.days() + " days to " + t.storageClass());
            }
        }
    }
}
```

### Deleting a bucket's lifecycle configuration
{: #java-v2-examples-delete-lifecycle-configuration}

```java
public static void deleteBucketLifecycle(String bucketName, S3Client cosClient) {
    System.out.println("Deleting lifecycle configuration from bucket: " + bucketName);

    DeleteBucketLifecycleRequest request = DeleteBucketLifecycleRequest.builder()
            .bucket(bucketName)
            .build();

    cosClient.deleteBucketLifecycle(request);

    System.out.println("Lifecycle configuration deleted!");
}
```

### Key References
{: #java-v2-delete-lifecycle-configuration-kr}

- [BucketLifecycleConfiguration.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/BucketLifecycleConfiguration.Builder.html)
- [LifecycleRule.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/LifecycleRule.Builder.html)
- [PutBucketLifecycleConfigurationRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutBucketLifecycleConfigurationRequest.Builder.html)

## Using Bucket Versioning
{: #java-v2-bucket-versioning}

Versioning enables you to keep multiple versions of an object in the same bucket. This can help you recover from unintended user actions and application failures.

### Enabling versioning on a bucket
{: #java-v2-examples-bucket-versioning}

```java
public static void enableBucketVersioning(String bucketName, S3Client cosClient) {
    System.out.println("Enabling versioning on bucket: " + bucketName);

    VersioningConfiguration versioningConfig = VersioningConfiguration.builder()
            .status(BucketVersioningStatus.ENABLED)
            .build();

    PutBucketVersioningRequest request = PutBucketVersioningRequest.builder()
            .bucket(bucketName)
            .versioningConfiguration(versioningConfig)
            .build();

    cosClient.putBucketVersioning(request);

    System.out.println("Versioning enabled!");
}
```

### Getting bucket versioning status
{: #java-v2-examples-get-bucket-versioning-status}

```java
public static void getBucketVersioning(String bucketName, S3Client cosClient) {
    System.out.println("Getting versioning status for bucket: " + bucketName);

    GetBucketVersioningRequest request = GetBucketVersioningRequest.builder()
            .bucket(bucketName)
            .build();

    GetBucketVersioningResponse response = cosClient.getBucketVersioning(request);

    System.out.println("Versioning Status: " + response.status());
}
```

### Listing object versions
{: #java-v2-examples-get-object-versioning}

```java
public static void listObjectVersions(String bucketName, S3Client cosClient) {
    System.out.println("Listing object versions in bucket: " + bucketName);

    ListObjectVersionsRequest request = ListObjectVersionsRequest.builder()
            .bucket(bucketName)
            .build();

    ListObjectVersionsResponse response = cosClient.listObjectVersions(request);

    System.out.println("Versions:");
    for (ObjectVersion version : response.versions()) {
        System.out.println("  Key: " + version.key());
        System.out.println("  Version ID: " + version.versionId());
        System.out.println("  Is Latest: " + version.isLatest());
        System.out.println("  Last Modified: " + version.lastModified());
        System.out.println();
    }
}
```

### Deleting a specific object version
{: #java-v2-examples-delete-object-versioning}

```java
public static void deleteObjectVersion(String bucketName, String objectKey, String versionId, S3Client cosClient) {
    System.out.println("Deleting version " + versionId + " of object: " + objectKey);

    DeleteObjectRequest request = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .versionId(versionId)
            .build();

    cosClient.deleteObject(request);

    System.out.println("Object version deleted!");
}
```

### Key References
{: #java-v2-delete-object-versioning-kr}

- [VersioningConfiguration.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/VersioningConfiguration.Builder.html)
- [PutBucketVersioningRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutBucketVersioningRequest.Builder.html)
- [ListObjectVersionsRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ListObjectVersionsRequest.Builder.html)

---

## Using Extended Bucket Listing
{: #java-v2-examples-bucket-listing}

{{site.data.keyword.cos_full_notm}} provides an extended listing API that returns additional metadata about buckets, including location and storage class information.

```java
public static void listBucketsExtended(S3Client cosClient) {
    System.out.println("Listing buckets with extended information:");

    ListBucketsExtendedRequest request = ListBucketsExtendedRequest.builder().build();

    ListBucketsExtendedResponse response = cosClient.listBucketsExtended(request);

    for (Bucket bucket : response.buckets()) {
        System.out.println("Bucket Name: " + bucket.name());
        System.out.println("  Creation Date: " + bucket.creationDate());

        // Extended metadata (IBM-specific)
        if (bucket.locationConstraint() != null) {
            System.out.println("  Location: " + bucket.locationConstraint());
        }
    }
}
```

### Key References
{: #java-v2-bucket-listing-kr}

- [ListBucketsExtendedRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ListBucketsExtendedRequest.Builder.html)
- [ListBucketsExtendedResponse](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ListBucketsExtendedResponse.html)


## Using Archive Tier and Object Restoration
{: #java-v2-object-restore}

{{site.data.keyword.cos_full_notm}} provides archive storage classes (Glacier) for long-term data retention at a lower cost. Objects in archive storage must be restored before they can be accessed.

### Transitioning objects to archive storage
{: #java-v2-examples-object-restore}

You can use lifecycle policies to automatically transition objects to archive storage after a specified time period.

```java
public static void setArchiveRule(String bucketName, S3Client cosClient) {
    System.out.println("Setting archive rule on bucket: " + bucketName);

    // Create transition to Glacier after 30 days
    Transition transition = Transition.builder()
            .days(30)
            .storageClass(StorageClass.GLACIER)
            .build();

    // Create lifecycle rule
    LifecycleRuleFilter filter = LifecycleRuleFilter.builder()
            .prefix("archive/")
            .build();

    LifecycleRule rule = LifecycleRule.builder()
            .id("archive-rule")
            .filter(filter)
            .transitions(transition)
            .status(ExpirationStatus.ENABLED)
            .build();

    // Apply lifecycle configuration
    BucketLifecycleConfiguration config = BucketLifecycleConfiguration.builder()
            .rules(rule)
            .build();

    PutBucketLifecycleConfigurationRequest request = PutBucketLifecycleConfigurationRequest.builder()
            .bucket(bucketName)
            .lifecycleConfiguration(config)
            .build();

    cosClient.putBucketLifecycleConfiguration(request);

    System.out.println("Archive rule configured!");
}
```

### Restoring an archived object
{: #java-v2-examples-archived-restore}

Objects in archive storage must be restored before they can be downloaded. You can specify the number of days the restored copy must be available.

```java
public static void restoreArchivedObject(String bucketName, String objectKey, int days, S3Client cosClient) {
    System.out.println("Restoring archived object: " + objectKey);

    // Create restore request with duration
    RestoreRequest restoreRequest = RestoreRequest.builder()
            .days(days)
            .glacierJobParameters(GlacierJobParameters.builder()
                    .tier(Tier.STANDARD)  // Options: STANDARD (12 hours), EXPEDITED (2 hours), BULK (5-12 hours)
                    .build())
            .build();

    RestoreObjectRequest request = RestoreObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .restoreRequest(restoreRequest)
            .build();

    cosClient.restoreObject(request);

    System.out.println("Restore initiated. Object will be available for " + days + " days once restored.");
}
```

### Checking restoration status
{: #java-v2-examples-object-restore-status}

```java
public static void checkRestoreStatus(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Checking restore status for: " + objectKey);

    HeadObjectRequest request = HeadObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

    HeadObjectResponse response = cosClient.headObject(request);

    if (response.restore() != null) {
        System.out.println("Restore Status: " + response.restore());

        // Parse restore status
        String restoreStatus = response.restore();
        if (restoreStatus.contains("ongoing-request=\"true\"")) {
            System.out.println("Restoration in progress...");
        } else if (restoreStatus.contains("ongoing-request=\"false\"")) {
            System.out.println("Object has been restored and is available for download");
        }
    } else {
        System.out.println("Object is not archived or no restore in progress");
    }
}
```

### Accelerated archive retrieval
{: #java-v2-examples-retrive-archive}

{{site.data.keyword.cos_full_notm}} supports accelerated archive retrieval for faster access to archived data:
- **Expedited**: 2 hours retrieval time
- **Standard**: 3-5 hours retrieval time (default)
- **Bulk**: 5-12 hours retrieval time (lowest cost)

```java
public static void restoreWithExpeditedRetrieval(String bucketName, String objectKey, S3Client cosClient) {
    System.out.println("Restoring with expedited retrieval: " + objectKey);

    RestoreRequest restoreRequest = RestoreRequest.builder()
            .days(1)
            .glacierJobParameters(GlacierJobParameters.builder()
                    .tier(Tier.EXPEDITED)  // 2-hour retrieval
                    .build())
            .build();

    RestoreObjectRequest request = RestoreObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .restoreRequest(restoreRequest)
            .build();

    cosClient.restoreObject(request);

    System.out.println("Expedited restore initiated (2-hour retrieval)");
}
```

### Key References
{: #java-v2-retrive-archive-kr}

- [RestoreObjectRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/RestoreObjectRequest.Builder.html)
- [RestoreRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/RestoreRequest.Builder.html)
- [GlacierJobParameters.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/GlacierJobParameters.Builder.html)


## Using CORS (Cross-Origin Resource Sharing)
{: #java-v2-cors}

CORS configuration allows web applications running in one domain to access resources in {{site.data.keyword.cos_full_notm}} from another domain.

### Setting CORS configuration on a bucket
{: #java-v2-examples-cors}

```java
public static void setCorsConfiguration(String bucketName, S3Client cosClient) {
    System.out.println("Setting CORS configuration on bucket: " + bucketName);

    // Create CORS rule
    CORSRule corsRule = CORSRule.builder()
            .allowedMethods("GET", "PUT", "POST", "DELETE")
            .allowedOrigins("https://example.com")
            .allowedHeaders("*")
            .maxAgeSeconds(3000)
            .exposeHeaders("ETag", "x-amz-request-id")
            .build();

    // Create CORS configuration
    CORSConfiguration corsConfig = CORSConfiguration.builder()
            .corsRules(corsRule)
            .build();

    PutBucketCorsRequest request = PutBucketCorsRequest.builder()
            .bucket(bucketName)
            .corsConfiguration(corsConfig)
            .build();

    cosClient.putBucketCors(request);

    System.out.println("CORS configuration applied!");
}
```

### Getting CORS configuration
{: #java-v2-examples-get-cors}

```java
public static void getCorsConfiguration(String bucketName, S3Client cosClient) {
    System.out.println("Getting CORS configuration for bucket: " + bucketName);

    GetBucketCorsRequest request = GetBucketCorsRequest.builder()
            .bucket(bucketName)
            .build();

    GetBucketCorsResponse response = cosClient.getBucketCors(request);

    System.out.println("CORS Rules:");
    for (CORSRule rule : response.corsRules()) {
        System.out.println("  Allowed Methods: " + rule.allowedMethods());
        System.out.println("  Allowed Origins: " + rule.allowedOrigins());
        System.out.println("  Allowed Headers: " + rule.allowedHeaders());
        System.out.println("  Max Age: " + rule.maxAgeSeconds());
    }
}
```

### Deleting CORS configuration
{: #java-v2-examples-delete-cors}


```java
public static void deleteCorsConfiguration(String bucketName, S3Client cosClient) {
    System.out.println("Deleting CORS configuration from bucket: " + bucketName);

    DeleteBucketCorsRequest request = DeleteBucketCorsRequest.builder()
            .bucket(bucketName)
            .build();

    cosClient.deleteBucketCors(request);

    System.out.println("CORS configuration deleted!");
}
```

### Key References
{: #java-v2-delete-cors-kr}

- [CORSConfiguration.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CORSConfiguration.Builder.html)
- [CORSRule.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/CORSRule.Builder.html)
- [PutBucketCorsRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutBucketCorsRequest.Builder.html)

---

## Using bucket policies
{: #java-v2-bucket-policy}

Bucket policies provide access control management for buckets and objects. They are written in JSON and can grant or deny permissions to users and services.

### Setting a bucket policy
{: #java-v2-examples-set-bucket-policy}

```java
public static void setBucketPolicy(String bucketName, S3Client cosClient) {
    System.out.println("Setting bucket policy on: " + bucketName);

    // Create policy document (JSON format)
    String policyText = "{"
            + "\"Version\": \"2012-10-17\","
            + "\"Statement\": [{"
            + "  \"Effect\": \"Allow\","
            + "  \"Principal\": {\"AWS\": \"*\"},"
            + "  \"Action\": \"s3:GetObject\","
            + "  \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\""
            + "}]"
            + "}";

    PutBucketPolicyRequest request = PutBucketPolicyRequest.builder()
            .bucket(bucketName)
            .policy(policyText)
            .build();

    cosClient.putBucketPolicy(request);

    System.out.println("Bucket policy applied!");
}
```

### Getting a bucket policy
{: #java-v2-examples-get-bucket-policy}

```java
public static void getBucketPolicy(String bucketName, S3Client cosClient) {
    System.out.println("Getting bucket policy for: " + bucketName);

    GetBucketPolicyRequest request = GetBucketPolicyRequest.builder()
            .bucket(bucketName)
            .build();

    GetBucketPolicyResponse response = cosClient.getBucketPolicy(request);

    System.out.println("Policy: " + response.policy());
}
```

### Deleting a bucket policy
{: #java-v2-examples-delete-bucket-policy}

```java
public static void deleteBucketPolicy(String bucketName, S3Client cosClient) {
    System.out.println("Deleting bucket policy from: " + bucketName);

    DeleteBucketPolicyRequest request = DeleteBucketPolicyRequest.builder()
            .bucket(bucketName)
            .build();

    cosClient.deleteBucketPolicy(request);

    System.out.println("Bucket policy deleted!");
}
```

### Key References
{: #java-v2-delete-bucket-policy-kr}

- [PutBucketPolicyRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutBucketPolicyRequest.Builder.html)
- [GetBucketPolicyRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/GetBucketPolicyRequest.Builder.html)

---

## Create a hosted static website
{: #java-v2-examples-get-bucket-policy}

{{site.data.keyword.cos_full_notm}} supports hosting static websites directly from buckets. You can configure a bucket to serve static content by specifying an index document and an error document.

This operation requires the following import statement:

```java
import com.ibm.cos.v2.services.s3.model.BucketWebsiteConfiguration;
```

### Setting bucket website configuration
{: #java-v2-examples-bucket-website}

This operation provides the following upon configuration and requires a correctly configured client:

- **Bucket configuration for suffix** (index document)
- **Bucket configuration for key** (error document)

```java
public static void setBucketWebsiteConfiguration(String bucketName, S3Client cosClient) {
    System.out.println("Setting website configuration for bucket: " + bucketName);

    // Create website configuration with index and error documents
    IndexDocument indexDocument = IndexDocument.builder()
            .suffix("index.html")
            .build();

    ErrorDocument errorDocument = ErrorDocument.builder()
            .key("error.html")
            .build();

    WebsiteConfiguration websiteConfig = WebsiteConfiguration.builder()
            .indexDocument(indexDocument)
            .errorDocument(errorDocument)
            .build();

    PutBucketWebsiteRequest request = PutBucketWebsiteRequest.builder()
            .bucket(bucketName)
            .websiteConfiguration(websiteConfig)
            .build();

    cosClient.putBucketWebsite(request);

    System.out.println("Website configuration set successfully!");
}
```

### Getting bucket website configuration
{: #java-v2-examples-get-bucket-website}

```java
public static void getBucketWebsiteConfiguration(String bucketName, S3Client cosClient) {
    System.out.println("Getting website configuration for bucket: " + bucketName);

    GetBucketWebsiteRequest request = GetBucketWebsiteRequest.builder()
            .bucket(bucketName)
            .build();

    GetBucketWebsiteResponse response = cosClient.getBucketWebsite(request);

    System.out.println("Index Document: " + response.indexDocument().suffix());
    System.out.println("Error Document: " + response.errorDocument().key());
}
```

### Deleting bucket website configuration
{: #java-v2-examples-delete-bucket-website}

```java
public static void deleteBucketWebsiteConfiguration(String bucketName, S3Client cosClient) {
    System.out.println("Deleting website configuration from bucket: " + bucketName);

    DeleteBucketWebsiteRequest request = DeleteBucketWebsiteRequest.builder()
            .bucket(bucketName)
            .build();

    cosClient.deleteBucketWebsite(request);

    System.out.println("Website configuration deleted!");
}
```

### Key References
{: #java-v2-delete-bucket-website-kr}

- [BucketWebsiteConfiguration](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/WebsiteConfiguration.html)
- [PutBucketWebsiteRequest.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/PutBucketWebsiteRequest.Builder.html)
- [IndexDocument.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/IndexDocument.Builder.html)
- [ErrorDocument.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/ErrorDocument.Builder.html)

## Error Handling and Best Practices
{: #java-v2-error-handling}

### Handling SDK exceptions
{: #java-v2-error-handling-exception}

The v2 SDK uses specific exception types for different error scenarios.

```java
import com.ibm.cos.v2.services.s3.model.S3Exception;
import com.ibm.cos.v2.services.s3.model.NoSuchBucketException;
import com.ibm.cos.v2.services.s3.model.NoSuchKeyException;

public static void handleExceptions(String bucketName, String objectKey, S3Client cosClient) {
    try {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        cosClient.getObject(request, ResponseTransformer.toBytes());

    } catch (NoSuchBucketException e) {
        System.err.println("Bucket does not exist: " + bucketName);
        System.err.println("Error Code: " + e.awsErrorDetails().errorCode());

    } catch (NoSuchKeyException e) {
        System.err.println("Object does not exist: " + objectKey);
        System.err.println("Error Code: " + e.awsErrorDetails().errorCode());

    } catch (S3Exception e) {
        System.err.println("S3 Error: " + e.awsErrorDetails().errorMessage());
        System.err.println("Error Code: " + e.awsErrorDetails().errorCode());
        System.err.println("Status Code: " + e.statusCode());

    } catch (Exception e) {
        System.err.println("Unexpected error: " + e.getMessage());
    }
}
```

### Best practices for v2 SDK
{: #java-v2-best-practice}

- **Reuse S3Client instances**: Creating clients is expensive. Reuse them across requests.

```java
// Good: Create once, reuse
S3Client client = S3Client.builder()
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.of(region))
        .build();

// Use client for multiple operations
client.listBuckets();
client.putObject(...);
client.getObject(...);

// Close when done
client.close();
```

- **Use try-with-resources**: Ensure proper resource cleanup.

```java
try (S3Client client = S3Client.builder()
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.of(region))
        .build()) {

    // Perform operations
    client.listBuckets();

} // Client automatically closed
```

- **Handle large files with multipart uploads**: For files over 5 MB, use multipart uploads.

- **Use Transfer Manager for large transfers**: Provides automatic multipart upload, retry logic, and progress tracking.

- **Set appropriate timeouts**: Configure client timeouts for your use case.

```java
S3Client client = S3Client.builder()
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.of(region))
        .overrideConfiguration(ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMinutes(5))
                .apiCallAttemptTimeout(Duration.ofMinutes(2))
                .build())
        .build();
```

### Key References
{: #java-v2-key-reference}

- [S3Exception](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/services/s3/model/S3Exception.html)
- [ClientOverrideConfiguration.Builder](https://ibm.github.io/ibm-cos-sdk-java-v2/com/ibm/cos/v2/core/client/config/ClientOverrideConfiguration.Builder.html)

## Additional Resources
{: #java-v2-additional-resource}

- [Java API Reference (v2)](https://ibm.github.io/ibm-cos-sdk-java-v2/)
- [GitHub Repository](https://github.com/IBM/ibm-cos-sdk-java-v2)
- [Endpoints and Storage Locations](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-endpoints)



## Getting Help
{: #java-v2-help}

- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/object-storage+ibm) with tags `ibm` and `object-storage`
- Open a support ticket with [IBM Cloud Support](https://cloud.ibm.com/unifiedsupport/supportcenter/)
- Report bugs or request features on [GitHub Issues](https://github.com/IBM/ibm-cos-sdk-java-v2/issues)

This documentation provides the structure and SDK references for {{site.data.keyword.cos_full_notm}} Java SDK v2. For complete, working code examples of all operations, refer to the [examples directory](https://github.com/IBM/ibm-cos-sdk-java-v2/tree/main/examples) and [Migration Guide](https://github.com/IBM/ibm-cos-sdk-java-v2/blob/main/MIGRATION_GUIDE_V2.md) in the GitHub repository.
