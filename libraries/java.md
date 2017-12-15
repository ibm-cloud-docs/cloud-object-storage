---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Java

The {{site.data.keyword.cos_full}} SDK for Java is comprehensive, and has features and capabilities not described in this guide.  For detailed class and method documentation [see the Javadoc](https://ibm.github.io/ibm-cos-sdk-java/). Source code can be found in the [GitHub repository](https://github.com/ibm/ibm-cos-sdk-java).

## Getting the SDK
The easiest way to consume the {{site.data.keyword.cos_full_notm}} Java SDK is to use Maven to manage dependencies. If you aren't familiar with Maven, you get can get up and running using the [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) guide.

Maven uses a file called `pom.xml` to specify the libraries (and their versions) needed for a Java project.  Here is an example `pom.xml` file for using the {{site.data.keyword.cos_full_notm}} Java SDK to connect to {{site.data.keyword.cos_short}}.


```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>1.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>1.0.1</version>
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

## Example class

```java
package com.cos;

import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ibm.oauth.BasicIBMOAuthCredentials;

public class CosExample
{

    private static AmazonS3 _s3Client;

    /**
     * @param args
     */
    public static void main(String[] args)
    {

        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.bluemix.net/oidc/token";

        String bucketName = "<bucketName>";
        String api_key = "<apiKey>";
        String service_instance_id = "<resourceInstanceId>";
        String endpoint_url = "https://s3-api.us-geo.objectstorage.softlayer.net";
        String location = "us";

        System.out.println("Current time: " + new Timestamp(System.currentTimeMillis()).toString());
        _s3Client = createClient(api_key, service_instance_id, endpoint_url, location);
        listObjects(bucketName, _s3Client);
        listBuckets(_s3Client);
    }

    /**
     * @param bucketName
     * @param clientNum
     * @param api_key
     *            (or access key)
     * @param service_instance_id
     *            (or secret key)
     * @param endpoint_url
     * @param location
     * @return AmazonS3
     */
    public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
    {
        AWSCredentials credentials;
        if (endpoint_url.contains("objectstorage.softlayer.net")) {
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);
        } else {
            String access_key = api_key;
            String secret_key = service_instance_id;
            credentials = new BasicAWSCredentials(access_key, secret_key);
        }
        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig).build();
        return s3Client;
    }

    /**
     * @param bucketName
     * @param s3Client
     */
    public static void listObjects(String bucketName, AmazonS3 s3Client)
    {
        System.out.println("Listing objects in bucket " + bucketName);
        ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    }

    /**
     * @param s3Client
     */
    public static void listBuckets(AmazonS3 s3Client)
    {
        System.out.println("Listing buckets");
        final List<Bucket> bucketList = _s3Client.listBuckets();
        for (final Bucket bucket : bucketList) {
            System.out.println(bucket.getName());
        }
        System.out.println();
    }

}
```

The following examples assume a client `cos` has been configured.

## Create a standard bucket

```java
cos.createBucket("sample", "us-standard"); // the name of the bucket, and the storage class (LocationConstraint)
```

## Create a Vault bucket

```java
cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

## Create a Cold Vault bucket

```java
cos.createBucket("sample", "us-cold"); // the name of the bucket, and the storage class (LocationConstraint)
```

## Upload object from a file

This example assumes that the bucket `sample` already exists.

```java
cos.putObject(
"sample", // the name of the destination bucket
"myfile", // the object key
new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

## Upload object using a stream

This example assumes that the bucket `sample` already exists.

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

## Download object to a file

This example assumes that the bucket `sample` already exists.

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


## Download object using a stream

This example assumes that the bucket `sample` already exists.

```java
S3Object returned = cos.getObject( // request the object by identifying
"sample", // the name of the bucket
"serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

## Copy objects

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

## List buckets

```java
List<Bucket> Buckets = cos.listBuckets(); // get a list of buckets

for (Bucket b : Buckets) { // for each bucket...
  System.out.println(`Found: `+b.getName()); // display 'Found: ' and then the name of the bucket
}
```

## List objects

```java
ObjectListing listing = cos.listObjects(`sample`); // get the list of objects in the 'sample' bucket
List<S3ObjectSummary> summaries = listing.getObjectSummaries(); // create a list of object summaries

for (S3ObjectSummary obj : summaries){ // for each object...
  System.out.println(`found: `+obj.getKey()); // display 'found: ' and then the name of the object
}
```

## Delete object

```java
cos.deleteObject( // delete the Object, passing…
"sample", // the name of the Bucket that stores the Object,
"myFile.txt" // and he name of the Object to be deleted
);
```

## Using Key Protect

A new object `EncryptionType` will contain the default value for the encryption algorithm & a `IBMSSEKPCustomerRootKeyCrn` variable.

```java
private String kpEncryptionAlgorithm = "AES256";
private String IBMSSEKPCustomerRootKeyCrn;

`kpEncryptionAlgorithm` will default to `AES256`, but can be overwritten with a setter method
The `CreateBucketRequest` object has been modified to accept the `EncryptionType` object when creating a bucket.
```

```
s3client.createBucket(new CreateBucketRequest(bucketName).withEncryptionType(encryptionType));
```

The additional headers have been defined within `Headers` class:

```java
Headers.java

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

### GET bucket

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

The additonal headers have been defined within the `Headers` class:

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

### HEAD bucket

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

## API reference

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
