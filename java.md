---

copyright:
  years: 2017, 2025
lastupdated: "2025-05-28"

keywords: object storage, java, sdk

subcollection: cloud-object-storage

---

{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'}
{:java: .ph data-hd-programlang='java'}
{:go: .ph data-hd-programlang='go'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Using Java
{: #java}

The {{site.data.keyword.cos_full}} SDK for Java provides features to make the most of {{site.data.keyword.cos_full_notm}}.
{: shortdesc}

The {{site.data.keyword.cos_full_notm}} SDK for Java is comprehensive, with many features and capabilities that exceed the scope and space of this guide. For detailed class and method documentation [see the Javadoc](https://ibm.github.io/ibm-cos-sdk-java/). Source code can be found in the [GitHub repository](https://github.com/ibm/ibm-cos-sdk-java).

## Getting the SDK
{: #java-install}

The easiest way to use the {{site.data.keyword.cos_full_notm}} Java SDK is to use Maven to manage dependencies. If you aren't familiar with Maven, you can get up and running by using the [Maven in 5-Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) guide.

Maven uses a file that is called `pom.xml` to specify the libraries (and their versions) needed for a Java project. Here is an example `pom.xml` file for using the {{site.data.keyword.cos_full_notm}} Java SDK to connect to {{site.data.keyword.cos_short}}.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.8.0</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}

## Creating a client and sourcing credentials
{: #java-credentials}

In the following example, a client `cos` is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.

After generating a [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), the resulting JSON document can be saved to `~/.bluemix/cos_credentials`. The SDK will automatically source credentials from this file unless other credentials are explicitly set during client creation. If the `cos_credentials` file contains HMAC keys the client authenticates with a signature, otherwise the client uses the provided API key to authenticate by using a bearer token.

If migrating from AWS S3, you can also source credentials data from  `~/.aws/credentials` in the format:

``` sh
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

If both `~/.bluemix/cos_credentials` and `~/.aws/credentials` exist, `cos_credentials` takes preference.

 For more details on client construction, [see the Javadoc](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.html).

## Code Examples
{: #java-examples}

In your code, you must remove the angled brackets or any other excess characters that are provided here as illustration.
{: note}

Let's start with an complete example class that will run through some basic functionality, then explore the classes individually. This `CosExample` class will list objects in an existing bucket, create a new bucket, and then list all buckets in the service instance.

### Gather required information
{: #java-examples-prereqs}

* `bucketName` and `newBucketName` are [unique and DNS-safe](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket) strings. Because bucket names are unique across the entire system, these values need to be changed if this example is run multiple times. Note that names are reserved for 10 - 15 minutes after deletion.
* `apiKey` is the value found in the [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) as `apikey`.
* `serviceInstanceId` is the value found in the [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) as `resource_instance_id`.
* `endpointUrl` is a service endpoint URL, inclusive of the `https://` protocol. This is **not** the `endpoints` value found in the [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials). For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `storageClass` is a [valid provisioning code](/docs/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) that corresponds to the `endpoint` value. This is then used as the S3 API `LocationConstraint` variable.
* `location` should be set to the location portion of the `storageClass`. For `us-south-standard`, this would be `us-south`. This variable is used only for the calculation of [HMAC signatures](/docs/cloud-object-storage?topic=cloud-object-storage-hmac-signature), but is required for any client, including this example that uses an IAM API key.

```java
    package com.cos;

    import java.time.LocalDateTime;
    import java.util.List;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
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
        public static void main(String[] args)
        {
            String bucketName = "<BUCKET_NAME>";  // eg my-unique-bucket-name
            String newBucketName = "<NEW_BUCKET_NAME>"; // eg my-other-unique-bucket-name
            String apiKey = "<API_KEY>"; // eg "W00YiRnLW4k3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
            String serviceInstanceId = "<SERVICE_INSTANCE_ID"; // eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"
            String endpointUrl = "https://s3.us-south.cloud-object-storage.appdomain.cloud"; // this could be any service endpoint

            String storageClass = "us-south-standard";
            String location = "us"; // not an endpoint, but used in a custom function below to obtain the correct URL

            System.out.println("Current time: " + LocalDateTime.now());
            AmazonS3 cosClient = createClient(apiKey, serviceInstanceId, endpointUrl, location);
            listObjects(cosClient, bucketName);
            createBucket(cosClient, newBucketName, storageClass);
            listBuckets(cosClient);
        }

        public static AmazonS3 createClient(String apiKey, String serviceInstanceId, String endpointUrl, String location)
        {
            AWSCredentials credentials = new BasicIBMOAuthCredentials(apiKey, serviceInstanceId);
            ClientConfiguration clientConfig = new ClientConfiguration()
                    .withRequestTimeout(5000)
                    .withTcpKeepAlive(true);

            return AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpointUrl, location))
                    .withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig)
                    .build();
        }

        public static void listObjects(AmazonS3 cosClient, String bucketName)
        {
            System.out.println("Listing objects in bucket " + bucketName);
            ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println();
        }

        public static void createBucket(AmazonS3 cosClient, String bucketName, String storageClass)
        {
            cosClient.createBucket(bucketName, storageClass);
        }

        public static void listBuckets(AmazonS3 cosClient)
        {
            System.out.println("Listing buckets");
            final List<Bucket> bucketList = cosClient.listBuckets();
            for (final Bucket bucket : bucketList) {
                System.out.println(bucket.getName());
            }
            System.out.println();
        }
    }

```
{: codeblock}

### Initializing configuration
{: #java-examples-config}

```java
private static String COS_ENDPOINT = "<endpoint>"; // eg "https://s3.us.cloud-object-storage.appdomain.cloud"
private static String COS_API_KEY_ID = "<api-key>"; // eg "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ"
private static String COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
private static String COS_SERVICE_CRN = "<resource-instance-id>"; // "crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::"
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

#### Key Values
{: #java-init-config-key-values}

* `<endpoint>` - public endpoint for your cloud Object Storage (available from the [IBM Cloud Dashboard](https://cloud.ibm.com/resources){: external}). For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - api key generated when creating the service credentials (write access is required for creation and deletion examples)
* `<resource-instance-id>` - resource ID for your cloud Object Storage (available through [IBM Cloud CLI](/docs/cli?topic=cli-idt-cli) or [IBM Cloud Dashboard](https://cloud.ibm.com/resources){: external})
* `<location>` - default location for your cloud Object Storage (must match the region that is used for `<endpoint>`)

#### SDK References
{: #java-init-config-sdk-refs}

Classes

* [AmazonS3ClientBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3ClientBuilder.html){: external}
* [AWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSCredentials.html){: external}
* [AWSStaticCredentialsProvider](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/AWSStaticCredentialsProvider.html){: external}
* [BasicAWSCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/auth/BasicAWSCredentials.html){: external}
* [BasicIBMOAuthCredentials](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/oauth/BasicIBMOAuthCredentials.html){: external}
* [ClientConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/ClientConfiguration.html){: external}
* [EndpointConfiguration](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/client/builder/AwsClientBuilder.EndpointConfiguration.html){: external}
* [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){: external}

### Determining Endpoint
{: #java-examples-endpoint}

The methods below can be used to determine the service endpoint based on the bucket location, endpoint type (public or private), and specific region (optional). For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

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

### Creating a new bucket
{: #java-examples-new-bucket}

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName);
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```

### Create a bucket with a different storage class
{: #java-examples-storage-class}

A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

#### SDK References
{: #java-create-bucket-diff-class-sdk-refs}

* [`createBucket`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-java.lang.String-){: external}

### Creating a new text file
{: #java-examples-text-file}

```java
public static void createTextFile(String bucketName, String itemName, String fileText) {
    System.out.printf("Creating new item: %s\n", itemName);

    byte[] arr = fileText.getBytes(StandardCharsets.UTF_8);
    InputStream newStream = new ByteArrayInputStream(arr);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(arr.length);

    PutObjectRequest req = new PutObjectRequest(bucketName, itemName, newStream, metadata);
    _cos.putObject(req);

    System.out.printf("Item: %s created!\n", itemName);
}
```

Note that when adding custom metadata to an object, it is necessary to create an `ObjectMetadata` object by using the SDK, and not to manually send a custom header containing `x-amz-meta-{key}`. The latter can cause issues when authenticating by using HMAC credentials.
{: .tip}

### Upload object from a file
{: #java-examples-upload}

This example assumes that the bucket `sample` exists.

```java
cos.putObject(
    "sample", // the name of the destination bucket
    "myfile", // the object key
    new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

### Upload object by using a stream
{: #java-examples-stream}

This example assumes that the bucket `sample` exists.

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

Alternatively, you can use a `CipherInputStream` to more easily encrypt the data stream without needing to overload the existing `InputStream` object.

```java
public CipherInputStream encryptStream(InputStream inputStream) {
       // Generate key
       KeyGenerator kgen = KeyGenerator.getInstance("AES");
       kgen.init(128);
       SecretKey aesKey = kgen.generateKey();
       // Encrypt cipher
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       cipher.init(Cipher.ENCRYPT_MODE, aesKey);
       CipherInputStream cis = new CipherInputStream(inputStream, cipher);
       return cis;
}
```

### Download object to a file
{: #java-examples-download}

This example assumes that the bucket `sample` exists.

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

### Download object by using a stream
{: #java-examples-download-stream}

This example assumes that the bucket `sample` exists.

```java
S3Object returned = cos.getObject( // request the object by identifying
    "sample", // the name of the bucket
    "serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = returned.getObjectContent(); // set the object stream
```

### Copy objects
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

#### SDK References
{: #java-copy-obj-sdk-refs}

Classes

* [ObjectMetadata](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ObjectMetadata.html){: external}
* [PutObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/PutObjectRequest.html){: external}

*Methods

* [`putObject`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#putObject-com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest-){: external}

#### `putObject` Exception
{: #java-examples-put-exception}

The `putObject` method might throw the following exception even if the new object upload was successful:

```java
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

**Root Cause:** The JAXB APIs are considered to be Java EE APIs, and are no longer contained on the default class path in Java SE 9.

**Fix:** Add the following entry to the pom.xml file in your project folder and repackage your project

```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
```

### List available buckets
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

#### SDK References
{: #list-example-buckets-sdk-refs}

Classes

* [Bucket](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Bucket.html){: external}

Methods

* [`listBuckets`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listBuckets--){: external}

### List items in a bucket (v2)
{: #java-examples-list-objects-v2}

The [AmazonS3](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html){: external} object contains an updated method to list the contents ([listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){: external}). This method allows you to limit the number of records that are returned and retrieve the records in batches. This might be useful for paging your results within an application and improve performance.

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

#### SDK References
{: #list-items-v2-sdk-refs}

Classes

* [ListObjectsV2Request](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Request.html){: external}
* [ListObjectsV2Result](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html){: external}
* [S3ObjectSummary](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/S3ObjectSummary.html){: external}

Methods

* [getObjectSummaries](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getObjectSummaries--){: external}
* [getNextContinuationToken](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/ListObjectsV2Result.html#getNextContinuationToken--){: external}
* [listObjectsV2](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){: external}

### Get file contents of particular item
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

#### SDK References
{: #java-examples-get-contents-sdk-refs}

Classes

* [GetObjectRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/GetObjectRequest.html){: external}

Methods

* [`getObject`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObject-com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest-java.io.File-){: external}

### Delete an item from a bucket
{: #java-examples-delete-object}

```java
public static void deleteItem(String bucketName, String itemName) {
    System.out.printf("Deleting item: %s\n", itemName);
    _cos.deleteObject(bucketName, itemName);
    System.out.printf("Item: %s deleted!\n", itemName);
}
```

#### SDK References
{: #java-examples-delete-object-sdk-refs}

Methods

* [`deleteObject`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteObject-java.lang.String-java.lang.String-){: external}

### Delete multiple items from a bucket
{: #java-examples-delete-objects}

The delete request can contain a maximum of 1000 keys that you want to delete. While this is very useful in reducing the per-request performance hit, be mindful when deleting a large number of keys. Also take into account the sizes of the objects to ensure suitable performance.
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

#### SDK References
{: #java-examples-delete-objects-sdk-refs}

Classes

* [`DeleteObjectsRequest`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsRequest.html){: external}
* [`DeleteObjectsResult`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.html){: external}
* [`DeleteObjectsResult.DeletedObject`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/DeleteObjectsResult.DeletedObject.html){: external}

Methods

* [`deleteObjects`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3Client.html#deleteObjects-com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectsRequest-){: external}

### Delete a bucket
{: #java-examples-delete-bucket}

```java
public static void deleteBucket(String bucketName) {
    System.out.printf("Deleting bucket: %s\n", bucketName);
    _cos.deleteBucket(bucketName);
    System.out.printf("Bucket: %s deleted!\n", bucketName);
}
```

#### SDK References
{: #java-examples-delete-bucket-sdk-refs}

Methods

* [`deleteBucket`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#deleteBucket-java.lang.String-){: external}

### Check if an object is publicly readable
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

#### SDK References
{: #java-examples-public-check-sdk-refs}

Classes

* [`AccessControlList`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AccessControlList.html){: external}
* [`Grant`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/Grant.html){: external}

Methods

* [`getObjectAcl`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#getObjectAcl-java.lang.String-java.lang.String-){: external}

### Execute a multi-part upload
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

#### SDK References
{: #java-examples-multipart-object-sdk-refs}

Classes

* [AbortMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/AbortMultipartUploadRequest.html){: external}
* [CompleteMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CompleteMultipartUploadRequest.html){: external}
* [InitiateMultipartUploadRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadRequest.html){: external}
* [InitiateMultipartUploadResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/InitiateMultipartUploadResult.html){: external}
* [SdkClientException](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/SdkClientException.html){: external}
* [UploadPartRequest](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartRequest.html){: external}
* [UploadPartResult](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/UploadPartResult.html){: external}

Methods

* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#abortMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest-){: external}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#completeMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest-){: external}
* [initiateMultipartUpload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#initiateMultipartUpload-com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest-){: external}
* [`uploadPart`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#uploadPart-com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest-){: external}

### Creating a Backup Policy
{: #java-create-backup-policy}

```java
public static void main(String[] args) {
        try {
            // Initialize authenticator
            IamAuthenticator authenticator = new IamAuthenticator.Builder()
                    .apikey(API_KEY)
                    .build();

            // Initialize ResourceConfiguration client
            ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);

            // Generate unique backup vault name
            String backupVaultName = "vault-" + UUID.randomUUID().toString();

            // Create backup policy
            CreateBackupPolicyOptions backupPolicyOptions = new CreateBackupPolicyOptions.Builder()
                    .bucket(SOURCE_BUCKET_NAME)
                    .initialRetention(new DeleteAfterDays.Builder().deleteAfterDays(1).build())
                    .policyName(BACKUP_POLICY_NAME)
                    .targetBackupVaultCrn(BACKUP_VAULT_CRN)
                    .backupType("continuous").build();
            Response<BackupPolicy> createResult = rcClient.createBackupPolicy(backupPolicyOptions).execute();


            System.out.println("Policy Name:");
            System.out.println(createResult.getResult().getPolicyName());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
```
{: codeblock}




### Listing a Backup Policy
{: #java-list-backup-policy}

```java
    public static void main(String[] args) {
            try {
                // Initialize IAM authenticator and Resource Configuration client
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);

                // List all backup policies
                ListBackupPoliciesOptions listOptions = new ListBackupPoliciesOptions.Builder()
                        .bucket(SOURCE_BUCKET_NAME)
                        .build();

                Response<BackupPolicyCollection> listResponse = rcClient.listBackupPolicies(listOptions).execute();

                System.out.println("\nList of backup policies:");
                List<?> policies = listResponse.getResult().getBackupPolicies();
                for (Object policy : policies) {
                    System.out.println(policy);
                }

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Get a Backup Policy
{: #java-get-backup-policy}

```java
    public static void main(String[] args) {
            try {
                // Setup IAM Authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);

                // Generate unique policy name
                String policyName = "policy_name_" + UUID.randomUUID().toString();


                // Fetch backup policy using policy ID
                GetBackupPolicyOptions getOptions = new GetBackupPolicyOptions.Builder()
                        .bucket(SOURCE_BUCKET_NAME)
                        .policyId(POLICY_ID)
                        .build();

                Response<BackupPolicy> getResponse = rcClient.getBackupPolicy(getOptions).execute();
                BackupPolicy fetchedPolicy = getResponse.getResult();

                System.out.println("\nFetched Backup Policy Details:");
                System.out.println(fetchedPolicy);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}




### Delete a Backup Policy
{: #java-delete-backup-policy}

```java
    public static void main(String[] args) {
            try {
                // Setup IAM Authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);



                // Delete the created backup policy
                DeleteBackupPolicyOptions deleteOptions = new DeleteBackupPolicyOptions.Builder()
                        .bucket(SOURCE_BUCKET_NAME)
                        .policyId(POLICY_ID)
                        .build();

                rcClient.deleteBackupPolicy(deleteOptions).execute();

                System.out.printf("Backup policy '%s' deleted successfully.%n", POLICY_ID);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}




### Creating a Backup Vault
{: #java-create-backup-vault}

```java
    public static void main(String[] args) {
            try {
                // Setup IAM Authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);

                // Generate unique backup vault name
                String backupVaultName = "backup-vault-" + UUID.randomUUID();

                // Create backup vault
                CreateBackupVaultOptions createOptions = new CreateBackupVaultOptions.Builder()
                        .serviceInstanceId(SERVICE_INSTANCE_ID)
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .region(REGION)
                        .build();

                Response<BackupVault> response = rcClient.createBackupVault(createOptions).execute();
                BackupVault vault = response.getResult();

                System.out.println("Backup vault created:");
                System.out.println(vault);

            } catch (Exception e) {
                System.err.println("Error creating backup vault: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Listing Backup Vaults
{: #java-list-backup-vaults}

```java
    public static void main(String[] args) {
            try {
                // Setup IAM Authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);



                // List backup vaults

                ListBackupVaultsOptions listBackupVaultsOptions = new ListBackupVaultsOptions.Builder().
                        serviceInstanceId(SERVICE_INSTANCE_ID).build();
                Response<BackupVaultCollection> backupVaults = rcClient.listBackupVaults(listBackupVaultsOptions).execute();



                System.out.println("\nList of backup vaults:");
                if ( backupVaults.getResult().getBackupVaults() != null) {
                    for (String vault :  backupVaults.getResult().getBackupVaults()) {
                        System.out.println(vault);
                    }
                } else {
                    System.out.println("No backup vaults found.");
                }

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}




### Get Backup Vaults
{: #java-get-backup-vaults}

```java
    public static void main(String[] args) {
            try {
                // Initialize IAM Authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Create Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);

                // Generate unique backup vault name
                String backupVaultName = "backup-vault-" + UUID.randomUUID();


                // Get backup vault details
                GetBackupVaultOptions getOptions = new GetBackupVaultOptions.Builder()
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .build();

                Response<BackupVault> getResponse = rcClient.getBackupVault(getOptions).execute();
                BackupVault vaultDetails = getResponse.getResult();

                System.out.println("Backup vault details:");
                System.out.println(vaultDetails);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Update Backup Vaults
{: #java-update-backup-vaults}

```java
    public static void main(String[] args) {
            try {
                // Setup IAM Authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);


                // Update vault: disable activity tracking and metrics monitoring

                BackupVaultPatch backupVaultPatch = new BackupVaultPatch.Builder()
                        .activityTracking(new BackupVaultActivityTracking.Builder().managementEvents(Boolean.FALSE).build())
                        .metricsMonitoring(new BackupVaultMetricsMonitoring.Builder().usageMetricsEnabled(Boolean.FALSE).build())
                        .build();
                UpdateBackupVaultOptions updateBackupVaultOptions = new UpdateBackupVaultOptions.Builder()
                        .backupVaultName(backupVaultName)
                        .backupVaultPatch(backupVaultPatch.asPatch()).build();
                Response<BackupVault> backupVaultResponse2 = rcClient.updateBackupVault(updateBackupVaultOptions).execute();


            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}




### Delete a Backup Vault
{: #java-delete-backup-vault}

```java
    public static void main(String[] args) {
            try {
                // Set up the authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);


                // Delete backup vault
                DeleteBackupVaultOptions deleteOptions = new DeleteBackupVaultOptions.Builder()
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .build();

                Response<Void> deleteResponse = rcClient.deleteBackupVault(deleteOptions).execute();
                System.out.println("Failed to delete backup vault '" + BACKUP_VAULT_NAME + "'.");

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Listing Recovery Ranges
{: #java-list-recovery-ranges}

```java
    public static void main(String[] args) {
            try {
                // Setup authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);



                // List recovery ranges
                ListRecoveryRangesOptions recoveryRangesOptions = new ListRecoveryRangesOptions.Builder()
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .build();

                Response<RecoveryRangeCollection> recoveryRangesResponse = rcClient.listRecoveryRanges(recoveryRangesOptions).execute();
                System.out.println("Recovery Ranges:");
                System.out.println(recoveryRangesResponse.getResult());

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Get Recovery Range
{: #java-get-recovery-range}

```java
    public static void main(String[] args) {
            try {
                // Setup authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);


                // Fetch details of the recovery range
                GetSourceResourceRecoveryRangeOptions recoveryRangeOptions = new GetSourceResourceRecoveryRangeOptions.Builder()
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .recoveryRangeId(RECOVERY_RANGE_ID)
                        .build();

                Response<RecoveryRange> getRecoveryRangeResponse = rcClient.getSourceResourceRecoveryRange(recoveryRangeOptions).execute();
                System.out.println("Recovery Range Details:");
                System.out.println(getRecoveryRangeResponse.getResult());

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Update Recovery Range
{: #java-update-recovery-range}

```java
    public static void main(String[] args) {
            try {
                // Setup authenticator
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                // Initialize Resource Configuration client
                ResourceConfiguration rcClient = new ResourceConfiguration("resource-configuration", authenticator);


                // Patch the recovery range (update retention to 99 days)
                PatchSourceResourceRecoveryRangeOptions patchOptions = new PatchSourceResourceRecoveryRangeOptions.Builder()
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .recoveryRangeId(RECOVERY_RANGE_ID)
                        .recoveryRangePatch(
                                new RecoveryRangePatch.Builder()
                                        .retention(new DeleteAfterDays.Builder().deleteAfterDays(99).build())
                                        .build()
                        )
                        .build();

                Response<RecoveryRange> patchResponse = rcClient.patchSourceResourceRecoveryRange(patchOptions).execute();
                System.out.println("Recovery Range successfully patched:");
                System.out.println(patchResponse.getResult());

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Initiating a Restore
{: #java-initiate-restore}

```java
    public static void main(String[] args) {

            try {
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                ResourceConfiguration RC_CLIENT = new ResourceConfiguration("resource-configuration", authenticator);



                 CreateRestoreOptions  createRestoreOptions = new CreateRestoreOptions.Builder()
                         .backupVaultName(BACKUP_VAULT_NAME)
                         .recoveryRangeId(recoveryRangeId)
                         .restoreType("in_place")
                         .targetResourceCrn(TARGET_BUCKET_CRN)
                         .restorePointInTime(RESTORE_TIME)
                         .build();
                 Response<Restore> createRestoreCall = RC_CLIENT.createRestore(createRestoreOptions).execute();


                 String restoreId = createRestoreCall.getResult().getRestoreId();
                 GetRestoreOptions getRestoreOptions = new GetRestoreOptions.Builder().restoreId(restoreId).backupVaultName(backupVaultName).build();
                 Response<Restore> restoreResult = RC_CLIENT.getRestore(getRestoreOptions).execute();

                 System.out.println("Restore successfully:");
                 System.out.println(restoreResult);


            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
```
{: codeblock}



### Listing Restore
{: #java-list-restore}

```java
    public static void main(String[] args) {

            try {
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                ResourceConfiguration RC_CLIENT = new ResourceConfiguration("resource-configuration", authenticator);



                ListRestoresOptions listRestoreOptions = new ListRestoresOptions.Builder()
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .build();

                Response<RestoreList> listRestoreResponse = rcClient.listRestores(listRestoreOptions).execute();
                System.out.println("Restore operations: " + listRestoreResponse.getResult());


           } catch (Exception e) {
               System.err.println("Error: " + e.getMessage());
               e.printStackTrace();
           }

        }
```
{: codeblock}



### Get Restore Details
{: #java-get-restore}

```java
    public static void main(String[] args) {

            try {
                IamAuthenticator authenticator = new IamAuthenticator.Builder()
                        .apikey(API_KEY)
                        .build();

                ResourceConfiguration RC_CLIENT = new ResourceConfiguration("resource-configuration", authenticator);


                Response<Restore> getRestoreOptions =
                        GetRestoreOptions.Builder()
                        .restoreId(restoreId)
                        .backupVaultName(BACKUP_VAULT_NAME)
                        .build();
                Response<Restore> restoreResult = RC_CLIENT.getRestore(getRestoreOptions).execute();

                System.out.println("Get Restore: " + restoreResult);

           } catch (Exception e) {
               System.err.println("Error: " + e.getMessage());
               e.printStackTrace();
           }

        }
```
{: codeblock}




## Upload larger objects using a Transfer Manager
{: #java-examples-transfer-manager}

The `TransferManager` simplifies large file transfers by automatically incorporating multi-part uploads whenever necessary setting configuration parameters.

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

#### SDK References
{: #java-examples-transfer-manager-sdk-refs}

Classes

* [TransferManager](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html){: external}
* [TransferManagerBuilder](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManagerBuilder.html){: external}
* [Upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/Upload.html){: external}

Methods

* [`shutdownNow`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#shutdownNow--){: external}
* [upload](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/TransferManager.html#upload-java.lang.String-java.lang.String-java.io.File-){: external}
* [waitForCompletion](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/transfer/internal/AbstractTransfer.html#waitForCompletion--){: external}

## Using Key Protect
{: #java-examples-kp}

Key Protect can be added to a storage bucket to encrypt sensitive data at rest in the cloud.

### Before You Begin
{: #java-examples-kp-prereqs}

The following items are necessary in order to create a bucket with Key-Protect enabled:

* A Key Protect service [provisioned](/docs/key-protect?topic=key-protect-provision)
* A Root key available (either [generated](/docs/key-protect?topic=key-protect-create-root-keys) or [imported](/docs/key-protect?topic=key-protect-import-root-keys))

### Retrieving the Root Key CRN
{: #java-examples-kp-root}

1. Retrieve the [instance ID](/docs/key-protect?topic=key-protect-retrieve-instance-ID) for your Key Protect service
1. Use the [Key Protect API](/docs/key-protect?topic=key-protect-set-up-api) to retrieve all your [available keys](https://cloud.ibm.com/apidocs/key-protect)
    * You can either use `curl` commands or an API REST Client such as [Postman](/docs/cloud-object-storage?topic=cloud-object-storage-postman) to access the [Key Protect API](/docs/key-protect?topic=key-protect-set-up-api).
1. Retrieve the CRN of the root key you will use to enabled Key Protect on the your bucket. The CRN will look similar to below:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creating a bucket with key-protect enabled
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

#### Key Values
{: #java-examples-kp-bucket-key-values}

* `<algorithm>` - The encryption algorithm used for new objects added to the bucket (Default is AES256).
* `<root-key-crn>` - CRN of the Root Key obtained from the Key Protect service.

#### SDK References
{: #java-examples-kp-bucket-sdk-refs}

Classes

* [`CreateBucketRequest`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/model/CreateBucketRequest.html){: external}
* [`EncryptionType`](https://ibm.github.io/ibm-cos-sdk-java/){: external}

Methods

* [`createBucket`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#createBucket-com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest-){: external}

### New Headers for Key Protect
{: #java-examples-kp-headers}

The additional headers have been defined within `Headers` class:

```java
public static final String IBM_SSE_KP_ENCRYPTION_ALGORITHM = "ibm-sse-kp-encryption-algorithm";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

The same section of the create bucket implementation which already adds IAM service instance headers will add the 2 new encryption headers:

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

The `ObjectListing` and `HeadBucketResult` objects have been updated to include boolean `IBMSSEKPEnabled` & String `IBMSSEKPCustomerRootKeyCrn` variables with getter & setter methods. These will store the values of the new headers.

#### GET bucket
{: #java-examples-kp-list}

```java
public ObjectListing listObjects(String bucketName)
public ObjectListing listObjects(String bucketName, String prefix)
public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
```

The `ObjectListing` class will require 2 additional methods:

```java
ObjectListing listing = s3client.listObjects(listObjectsRequest)
String KPEnabled = listing.getIBMSSEKPEnabled();
String crkId = listing.getIBMSSEKPCrkId();
```

The additional headers have been defined within the `Headers` class:

```java
Headers.java
public static final string IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

The S3XmlResponseHandler which is responsible for unmarshalling all xml responses. A check has been added that the result is an instance of `ObjectListing` and the retrieved headers will be added to the `ObjectListing` object:

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

The additional headers have been defined within Headers class:

```java
Headers.java
public static final String IBM_SSE_KP_ENABLED = "ibm-sse-kp-enabled";
public static final String IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN = "ibm-sse-kp-customer-root-key-crn";
```

These variables are populated in the HeadBucketResponseHandler.

```java
HeadBucketResultHandler
result.setIBMSSEKPEnabled(response.getHeaders().get(Headers.IBM_SSE_KP_ENABLED));
result.setIBMSSEKPCrk(response.getHeaders().get(Headers. IBM_SSE_KP_CUSTOMER_ROOT_KEY_CRN));

Head Bucket Example
HeadBucketResult result = s3client.headBucket(headBucketRequest)
boolean KPEnabled = result.getIBMSSEKPEnabled();
String crn = result.getIBMSSEKPCUSTOMERROOTKEYCRN();
```

## Using Aspera High-Speed Transfer
{: #java-examples-aspera}

By installing the [Aspera high-speed transfer library](/docs/cloud-object-storage?topic=cloud-object-storage-aspera#aspera-packaging) you can utilize high-speed file transfers within your application. The Aspera library is closed-source, and thus an optional dependency for the COS SDK (which uses an Apache license).

Each Aspera high-speed transfer session spawns an individual `ascp` process that runs on the client machine to perform the transfer. Ensure that your computing environment can allow this process to run.
{: tip}

You will need instances of the S3 Client and IAM Token Manager classes to initialize the `AsperaTransferManager`. The `s3Client` is required to get FASP connection information for the COS target bucket. The `tokenManager` is required to allow the Aspera high-speed transfer SDK to authenticate with the COS target bucket.

### Initializing the `AsperaTransferManager`
{: #java-examples-aspera-init}

Before initializing the `AsperaTransferManager`, make sure you've got working [`s3Client`](#java-examples-config) and [`tokenManager`](#java-examples-config) objects.

It is advised to use `TokenManager tokenManager = new DefaultTokenManager(new DelegateTokenProvider(apiKey));` and avoid `.withTokenManager(tokenManager)` when building `AsperaTransferManager` with `AsperaTransferManagerBuilder`.
{: note}

There isn't a lot of benefit to using a single session of Aspera high-speed transfer unless you expect to see significant noise or packet loss in the network. So we need to tell the `AsperaTransferManager` to use multiple sessions using the `AsperaConfig` class. This will split the transfer into a number of parallel **sessions** that send chunks of data whose size is defined by the **threshold** value.

The typical configuration for using multi-session should be:

* 2500 Mbps target rate
* 100 MB threshold (*this is the recommended value for most applications*)

```java
AsperaTransferManagerConfig transferConfig = new AsperaTransferManagerConfig()
    .withMultiSession(true);

AsperaConfig asperaConfig = new AsperaConfig()
    .withTargetRateMbps(2500L)
    .withMultiSessionThresholdMb(100);

TokenManager tokenManager = new DefaultTokenManager(new DelegateTokenProvider(API_KEY));

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withAsperaTransferManagerConfig(transferConfig)
    .withAsperaConfig(asperaConfig)
    .build();
```

In the above example, the sdk will spawn enough sessions to attempt to reach the target rate of 2500 Mbps.

Alternatively, session management can be explicitly configured in the sdk. This is useful in cases where more precise control over network utilization is desired.

The typical configuration for using explicit multi-session should be:

* 2 or 10 sessions
* 100 MB threshold (*this is the recommended value for most applications*)

```java
AsperaConfig asperaConfig = new AsperaConfig()
    .withMultiSession(2)
    .withMultiSessionThresholdMb(100);

TokenManager tokenManager = new DefaultTokenManager(new DelegateTokenProvider(API_KEY));

AsperaTransferManager asperaTransferMgr = new AsperaTransferManagerBuilder(API_KEY, s3Client)
    .withAsperaConfig(asperaConfig)
    .build();
```

For best performance in most scenarios, always make use of multiple sessions to minimize any processing associated with instantiating an Aspera high-speed transfer. **If your network capacity is at least 1 Gbps you should use 10 sessions.**  Lower bandwidth networks should use two sessions.
{: tip}

#### Key Values
{: #java-examples-aspera-init-key-values}

* `API_KEY` - An API key for a user or service ID with Writer or Manager roles

You need to provide an IAM API Key for constructing an `AsperaTransferManager`. [HMAC Credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){: external} are **NOT** currently supported. For more information on IAM, [click here](/docs/cloud-object-storage?topic=cloud-object-storage-iam-overview).
{: tip}

### File Upload
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

#### Key Values
{: #java-examples-aspera-upload-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<absolute-path-to-source-data>` - directory and file name to upload to Object Storage.
* `<item-name>` - name of the new object added to the bucket.

### File Download
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
Future<AsperaTransaction> asperaTransactionFuture = asperaTransferMgr.download(bucketName, itemName, outputPath);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

```

#### Key Values
{: #java-examples-aspera-download-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<absolute-path-to-file>` - directory and file name to save from Object Storage.
* `<item-name>` - name of the object in the bucket.

### Directory Upload
{: #java-examples-aspera-upload-directory}

```java
String bucketName = "<bucket-name>";
String directoryPath = "<absolute-path-to-directory-for-new-file>";
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

#### Key Values
{: #java-examples-aspera-upload-directory-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<absolute-path-to-directory>` - directory of the files to be uploaded to Object Storage.
* `<virtual-directory-prefix>` - name of the directory prefix to be added to each file upon upload. Use null or empty string to upload the files to the bucket root.

### Directory Download
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

#### Key Values
{: #java-examples-aspera-download-directory-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<absolute-path-to-directory>` - directory to save downloaded files from Object Storage.
* `<virtual-directory-prefix>` - name of the directory prefix of each file to download. Use null or empty string to download all files in the bucket.

### Overriding Session Configuration on a Per Transfer Basis
{: #java-examples-aspera-config}

You can override the multi-session configuration values on a per transfer basis by passing an instance of `AsperaConfig` to the upload and download overloaded methods. Using `AsperaConfig` you can specify the number of sessions and minimum file threshold size per session.

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

### Monitoring Transfer Progress
{: #java-examples-aspera-monitor}

The simplest way to monitor the progress of your file/directory transfers is to use the `isDone()` property that returns `true` when your transfer is complete.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress");

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

You can also check if a transfer is queued for processing by calling the `onQueue` method on the `AsperaTransaction`. `onQueue` will return a Boolean with `true` indicating that the transfer is queued.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in queueing: " + asperaTransaction.onQueue());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

To check if a transfer is in progress call the progress method in `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    System.out.println("Directory download is in progress: " + asperaTransaction.progress());

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

Every transfer by default will have a `TransferProgress` attached to it. The `TransferProgress` will report the number of bytes transferred and the percentage transferred of the total bytes to transfer. To access a transfer’s `TransferProgress` use the `getProgress` method in `AsperaTransaction`.

```java
Future<AsperaTransaction> asperaTransactionFuture  = asperaTransferMgr.downloadDirectory(bucketName, directoryPrefix, outputDirectory, includeSubDirectories);
AsperaTransaction asperaTransaction = asperaTransactionFuture.get();

while (!asperaTransaction.isDone()) {
    TransferProgress transferProgress = asperaTransaction.getProgress();

    //pause for 3 seconds
    Thread.sleep(1000 * 3);
}
```

To report the number of bytes transferred call the `getBytesTransferred` method on `TransferProgress`. To report the percentage transferred of the total bytes to transfer call the `getPercentTransferred` method on `TransferProgress`.

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

### Pause/Resume/Cancel
{: #java-examples-aspera-pause}

The SDK provides the ability to manage the progress of file/directory transfers through the following methods of the `AsperaTransfer` object:

* `pause()`
* `resume()`
* `cancel()`

There are no side-effects from calling either of the methods outlined above. Proper clean up and housekeeping is handled by the SDK.
{: tip}

The following example shows a possible use for these methods:

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

### Troubleshooting Aspera Issues
{: #java-examples-aspera-ts}

**Issue:** developers using the Oracle JDK on Linux or Mac OS X may experience unexpected and silent crashes during transfers

**Cause:** The native code requires its own signal handlers which could be overriding the JVM's signal handlers. It might be necessary to use the JVM's signal chaining facility.

*IBM&reg; JDK users or Microsoft&reg; Windows users are not affected.*

**Solution:** Link and load the JVM's signal chaining library.

* On Linux locate the `libjsig.so` shared library and set the following environment variable:

  * `LD_PRELOAD=<PATH_TO_SHARED_LIB>/libjsig.so`

* On Mac OS X locate the shared library `libjsig.dylib` and set the following environment variables:

  * `DYLD_INSERT_LIBRARIES=<PATH_TO_SHARED_LIB>/libjsig.dylib`
  * `DYLD_FORCE_FLAT_NAMESPACE=0`

Visit the [Oracle&reg; JDK documentation](https://docs.oracle.com/javase/10/vm/signal-chaining.htm){: external} for more information about signal chaining.

**Issue:** `UnsatisfiedLinkError` on Linux

**Cause:** System unable to load dependent libraries. Errors such as the following may be seen in the application logs:

```sh
libfaspmanager2.so: libawt.so: cannot open shared object file: No such file or directory
```

**Solution:** Set the following environment variable:

`LD_LIBRARY_PATH=<JAVA_HOME>/jre/lib/amd64/server:<JAVA_HOME>/jre/lib/amd64`


## Updating Metadata
{: #java-examples-metadata}

There are two ways to update the metadata on an existing object:

* A `PUT` request with the new metadata and the original object contents
* Executing a `COPY` request with the new metadata specifying the original object as the copy source

### Using PUT to update metadata
{: #java-examples-metadata-put}

The `PUT` request overwrites the existing contents of the object so it must first be downloaded and re-uploaded with the new metadata.
{: note}

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

### Using COPY to update metadata
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

## Using Immutable Object Storage
{: #java-examples-immutable}

### Add a protection configuration to an existing bucket
{: #java-examples-immutable-enable}

This implementation of the `PUT` operation uses the `protection` query parameter to set the retention parameters for an existing bucket. This operation allows you to set or change the minimum, default, and maximum retention period. This operation also allows you to change the protection state of the bucket.

Objects written to a protected bucket cannot be deleted until the protection period has expired and all legal holds on the object are removed. The bucket's default retention value is given to an object unless an object specific value is provided when the object is created. Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

The minimum and maximum supported values for the retention period settings `MinimumRetention`, `DefaultRetention`, and `MaximumRetention` are a minimum of 0 days and a maximum of 365243 days (1000 years).

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

### Check protection on a bucket
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

### Upload a protected object
{: #java-examples-immutable-upload}

Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

|Value	| Type	| Description |
| --- | --- | --- |
|`Retention-Period` | Non-negative integer (seconds) | Retention period to store on the object in seconds. The object can be neither overwritten nor deleted until the amount of time specified in the retention period has elapsed. If this field and `Retention-Expiration-Date` are specified a `400`  error is returned. If neither is specified the bucket's `DefaultRetention` period will be used. Zero (`0`) is a legal value assuming the bucket's minimum retention period is also `0`. |
| `Retention-expiration-date` | Date (ISO 8601 Format) | Date on which it will be legal to delete or modify the object. You can only specify this or the Retention-Period header. If both are specified a `400`  error will be returned. If neither is specified the bucket's DefaultRetention period will be used. |
| `Retention-legal-hold-id` | string | A single legal hold to apply to the object. A legal hold is a Y character long string. The object cannot be overwritten or deleted until all legal holds associated with the object are removed. |

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

### Add or remove a legal hold to or from a protected object
{: #java-examples-immutable-legal-hold}

The object can support 100 legal holds:

* A legal hold identifier is a string of maximum length 64 characters and a minimum length of 1 character. Valid characters are letters, numbers, `!`, `_`, `.`, `*`, `(`, `)`, `-` and `'`.
* If the addition of the given legal hold exceeds 100 total legal holds on the object, the new legal hold will not be added, a `400` error will be returned.
* If an identifier is too long it will not be added to the object and a `400` error is returned.
* If an identifier contains invalid characters, it will not be added to the object and a `400` error is returned.
* If an identifier is already in use on an object, the existing legal hold is not modified and the response indicates the identifier was already in use with a `409` error.
* If an object does not have retention period metadata, a `400` error is returned and adding or removing a legal hold is not allowed.

The presence of a retention period header is required, otherwise a `400` error is returned.

The user making adding or removing a legal hold must have `Manager` permissions for this bucket.

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

### Extend the retention period of a protected object
{: #java-examples-immutable-extend}

The retention period of an object can only be extended. It cannot be decreased from the currently configured value.

The retention expansion value is set in one of three ways:

* additional time from the current value (`Additional-Retention-Period` or similar method)
* new extension period in seconds (`Extend-Retention-From-Current-Time` or similar method)
* new retention expiry date of the object (`New-Retention-Expiration-Date` or similar method)

The current retention period stored in the object metadata is either increased by the given additional time or replaced with the new value, depending on the parameter that is set in the `extendRetention` request. In all cases, the extend retention parameter is checked against the current retention period and the extended parameter is only accepted if the updated retention period is greater than the current retention period.

Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

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

### List legal holds on a protected object
{: #java-examples-immutable-list-holds}

This operation returns:

* Object creation date
* Object retention period in seconds
* Calculated retention expiration date based on the period and creation date
* List of legal holds
* Legal hold identifier
* Timestamp when legal hold was applied

If there are no legal holds on the object, an empty `LegalHoldSet` is returned.
If there is no retention period specified on the object, a `404` error is returned.

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

### Create a hosted static website
{: #java-examples-hosted-static-website-create}

This operation requires an import statement to be added:

```java
import com.ibm.cloud.objectstorage.services.s3.model.model.BucketWebsiteConfiguration;
```
{: codeblock}

This operation provides the following upon configuration and requires a correctly configured client:

* Bucket configuration for suffix (index document)
* Bucket configuration for key (error document)

```java
cosClient.setBucketWebsiteConfiguration("<bucket_name>", new BucketWebsiteConfiguration("index.html", "error.html"));
```
{: codeblock}

## Next Steps
{: #java-guide-next-steps}

 For more information, [see the Javadoc](https://ibm.github.io/ibm-cos-sdk-java/){: external}. The source code for the project can be found in the [GitHub repository](https://github.com/ibm/ibm-cos-sdk-java){: external}.
