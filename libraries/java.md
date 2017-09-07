---

copyright:
  years: 2017
lastupdated: "2017-02-23"

---

# Java

The COS SDK for Java is comprehensive, and has features and capabilities not described in this guide.  For detailed class and method documentation, as well as the source code, see the [GitHub repository](https://github.com/).

## Getting the SDK
The easiest way to consume the IBM COS Java SDK is to use Maven to manage dependencies. If you aren't familiar with Maven, you get can get up and running using the [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) guide.

Maven uses a file called `pom.xml` to specify the libraries (and their versions) needed for a Java project.  Here is an example `pom.xml` file for using the IBM COS Java SDK to connect to IBM COS.


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
            <groupId>com.amazonaws</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>1.11.5</version>
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


## Creating a client and setting the endpoint

### Create an COS Java SDK connection using the default credentials

Use default credentials using the first credentials found in this order of precedence:

1. Environment variables
2. System properties
3. Default profile in `~/.aws/credentials`

The AWS Java SDK automatically reads the Access Key ID and Secret Access Key from one of these locations. They do not need to be provided explicitly.

The AWS Java SDK sends all requests to `s3.amazonaws.com` by default. To send requests to IBM COS, the new `AmazonS3Client` instance needs the correct `setEndpoint` parameter. The SDK interprets the http(s) from the endpoint and infers encrypted or plain text from the URL.
{:tip}

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");
```

#### Example: Setting the endpoint to point to COS US Cross Region

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndPoint("https://s3-api.us-geo.objectstorage.softlayer.net");
```

The COS implementation of the S3 API supports both resource path and virtual host addressing.

### Use virtual host addressing
This S3 implementation supports virtual host addressing of storage buckets. The AWS Java SDK uses virtual host addressing by default.

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndPoint("http://s3-api.us-geo.objectstorage.softlayer.net");
```

### Use resource path addressing
To configure the AWS Java SDK to use resource path addressing instead of virtual host addressing, either:

Set the `disableDNSBuckets` system property to True:

```java
System.setProperty("com.amazonaws.sdk.disableDNSBuckets", "True");
```

Set the `withPathStyleAccess` property of the `AmazonS3Client.S3ClientOptions` to `True`:

```java
AmazonS3 cos = new AmazonS3Client(credentials);

S3ClientOptions opts = new S3ClientOptions().withPathStyleAccess(true);
cos.setS3ClientOptions(opts);
```

## Configure optional parameters

### Set the connection timeout duration
To increase the connection timeout for longer running operations, change the `withSocketTimeout` property of the `ClientConfiguration` object when the connection is created.

```java
ClientConfiguration config = new ClientConfiguration().withSocketTimeout(15 * 60 * 1000);

AmazonS3 cos = new AmazonS3Client(credentials, config);
```

### Set the maximum retry limit

If AWS Java SDK receives an error from the system, it retries the request. The default number of times it retries the request is five. The default retry limit can be changed in the client application.

{% include important.html content="The client reports the final error and not the initial failure, which can mask the details of the underlying issue." %}

{% include note.html content="Set the `maxErrorRetry` property of the `ClientConfiguration` object to `0` to disable the default retry policy." %}

#### Example: Changing the Maximum Retry Value

```java
ClientConfiguration config = new ClientConfiguration().withMaxErrorRetry(0);

AmazonS3 cos = new AmazonS3Client(credentials, config);
```

## Managing credentials

The order of precedence using for access credentials is:

1. Credentials passed as `BasicAWSCredentials` instance parameters
2. Credentials set as environment variables
3. Credentials set as JVM system properties
4. Credentials set in `AwsCredentials.properties` file
5. Credentials set in the shared credentials file

### Credentials passed as `BasicAWSCredentials` instance parameters

Credentials can be supplied as parameters of the `BasicAWSCredentials` object. This object is then passed to the `AmazonS3Client` as a constructor parameter.

#### Example: Create an AWS Java SDK connection specifying credentials provided as strings in code

```java
final String accessKey = "lDrDjH0D45hQivu6FNlwQ";
final String secretKey = "bHp5DOjg0HHJrGK7h3ejEqRDnVmWZK03T4lstel6";

BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey); // declare a new set of basic credentials that includes the Access Key ID and the Secret Access Key
AmazonS3 cos = new AmazonS3Client(credentials); // create a constructor for the client using the declared credentials.
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net"); // set the desired endpoint
```

### Credentials set as environment variables

Set the environment variables `AWS_ACCESS_KEY_ID` to define the access key and `AWS_SECRET_ACCESS_KEY` to define the secret key.

If these variables are defined when the `AmazonS3Client` is created, the default constructor can be used.

{% include note.html content="Setting environment variables varies by operating system. Refer to this [Knowledge Base Article](http://www.schrodinger.com/kb/1842 ) for more information." %}

| Variable     |            Purpose
|-------------------------------------
| `AWS_ACCESS_KEY`      |      AWS access key.
| `AWS_SECRET_ACCESS_KEY`  |   AWS secret key. Access and secret key variables override credentials that are stored in both credential and config files.
| `AWS_PROFILE`          |    Name of the profile to use, such as the name of a profile that is stored in a credential or a config file. The default is to use the default profile.
{:.opstable}


#### Example: Setting and loading credentials using system Environment Variables

The AWS Java SDK automatically reads the Access Key ID and Secret Access Key from the Environment Variables. They do not need to be provided explicitly.

```java
AmazonS3 cos = new AmazonS3Client();
```

### Credentials set as JVM system properties

Set the JVM system properties `aws.accessKeyId` to define the access key and `aws.secretKey` to define the secret key.

These properties may be set on start up or programmatically and will be used by the `AmazonS3Client` when the default constructor is used.

#### Example: Set system properties then open an S3 connection using those properties

```java
System.setProperty("aws.accessKeyId", "lDrDjH0D45hQivu6FNlwQ");
System.setProperty("aws.secretKey", "bHp5DOjg0HHJrGK7h3ejEqRDnVmWZK03T4lstel6");

AmazonS3 cos = new AmazonS3Client();
```

### Credentials set in `AwsCredentials.properties` file

An `AmazonS3Client` constructor can use a credentials file called `AwsCredentials.properties`, which is found on the Java classpath.

To create an S3 client that uses `AwsCredentials.properties`, the `AmazonS3Client` object is created by passing a `ClasspathPropertiesFileCredentialsProvider` as a constructor parameter.

#### Example: `AwsCredentials.properties` File Format

```
accessKey=lDrDjH0D45hQivu6FNlwQ
secretKey=bHp5DOjg0HHJrGK7h3ejEqRDnVmWZK03T4lstel6
```

#### Example: Use an `AWSCredentials.properties` file on the classpath

```java
ClasspathPropertiesFileCredentialsProvider provider = new ClasspathPropertiesFileCredentialsProvider(); // declare a new set of basic credentials that use the AWSCredentials.properties file

AmazonS3 cos = new AmazonS3Client(provider); // create a constructor for an S3 compatible client using the credentials from the AWSCredentials.properties file
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net"); // set the endpoint for the new S3 compatible client
```



Credentials can be put in a different file and location. The credentials can be accessed passing the `PropertiesFileCredentialsProvider` constructor parameter when creating an `AmazonS3Client` instance.

#### Example: Creating a client using a `AwsCredentials.properties` in another location

```java
AWSCredentialsProvider provider = new PropertiesFileCredentialsProvider("/path/to/alternative/credentials/file.properties");

AmazonS3 cos = new AmazonS3Client(provider);
```


### Credentials set in the shared credentials file

#### Create AWS shared credentials

Use `aws configure` to create an access credentials file with the default profile. For more information about using the AWS CLI, see the documentation on [using command line interfaces].

#### Example: Command output of `aws configure`

```
$ aws configure
AWS Access Key ID [None]: #AKIAIOSFODNN7EXAMPLE# // stored in ~/.aws/credentials
AWS Secret Access Key [None]: #wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY# // stored in ~/.aws/credentials
Default region name [None]: // the region does not need to be set
Default output format [None]: #json# // the output format will be stored in ~/.aws/config
```

To support multiple identities, AWS credential files have named profiles.

Use `aws configure — profile {profileName}` to create an access credentials file with a named profile. Additional named profiles are appended to the `~/.aws/credentials` file.

#### Example: Command output of `aws configure --profile pool2`

```
$ aws configure --profile pool2
AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE
AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Default region name [None]:
Default output format [None]: text
```

#### Example: Contents of the AWS credentials file (`~/.aws/credentials`)

```
[default]
aws_access_key_id = AKIAIOSFODNN7EXAMPLE
aws_secret_access_key = wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY

[profile pool2]
aws_access_key_id = N67W90RKLCWOLPSKN8W8
aws_secret_access_key = RlfTDyqPg0WnY/PWdxMEe/gjuG7QRckynofRMwwR

```

#### Example: Contents of the AWS configuration file (`~/.aws/config`)

```
[default]
output=json

[profile pool2]
output=text
```

### Use a named profile in a AWS shared credentials file

To use the name profile credentials in this file:

1. Provide the profile name as a parameter of the `ProfileCredentialsProvider` Object.
2. Provide the resulting object to the `AmazonS3Client` as a constructor parameter.

#### Example: Using an AWS Credential Named Profile in an AWS Java SDK connection method

```java
AWSCredentialsProvider provider = new ProfileCredentialsProvider("ibm"); // specify the Named Profile to use
AmazonS3 cos = new AmazonS3Client(provider); // specify which set of credentials to use
```

## Code examples

These are examples and assume a strong grasp of Java programming fundamentals. They should be used to assist developers in programming their own solutions and not be copied and pasted directly into their applications. Import declarations and try/catch blocks have been omitted for readability. IBM cannot be held accountable for developers using this code verbatim.

{% include note.html content="The following examples assume the use of default credentials" %}

## Create a standard bucket

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

cos.createBucket("sample", "us-standard"); // the name of the bucket, and the storage class (LocationConstraint)
```

## Create a Vault bucket

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

cos.createBucket("sample", "us-vault"); // the name of the bucket, and the storage class (LocationConstraint)
```

## Create a Cold Vault bucket

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

cos.createBucket("sample", "us-cold"); // the name of the bucket, and the storage class (LocationConstraint)
```

## Upload object from a file

{% include note.html content="This example assumes that the bucket 'sample' already exists." %}

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

cos.putObject(
"sample", // the name of the destination bucket
"myfile", // the object key
new File("/home/user/test.txt") // the file name and path of the object to be uploaded
);
```

## Upload object using a stream

{% include note.html content="This example assumes that the bucket 'sample' already exists." %}

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");
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

{% include note.html content="This example assumes that the bucket 'sample' already exists." %}

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

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

{% include note.html content="This example assumes that the bucket 'sample' already exists." %}

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");
S3Object returned = CLIENT.getObject( // request the object by identifying
"sample", // the name of the bucket
"serialized-object" // the name of the serialized object
);
S3ObjectInputStream s3Input = s3Response.getObjectContent(); // set the object stream
```

## Copy objects

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

// copy an object within the same Bucket
cos.copyObject( // copy the Object, passing…
"sample",  // the name of the Bucket in which the Object to be copied is stored,
"myFile.txt",  // the name of the Object being copied from the source Bucket,
"sample",  // the name of the Bucket in which the Object to be copied is stored,
"myFile.txt.backup"    // and the new name of the copy of the Object to be copied
);
```

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

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
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint(`https://s3-api.us-geo.objectstorage.softlayer.net`);

List<Bucket> Buckets = cos.listBuckets(); // get a list of buckets

for (Bucket b : Buckets) { // for each bucket...
  System.out.println(`Found: `+b.getName()); // display 'Found: ' and then the name of the bucket
}
```

## List objects

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint(`https://s3-api.us-geo.objectstorage.softlayer.net`);

ObjectListing listing = cos.listObjects(`sample`); // get the list of objects in the 'sample' bucket
List<S3ObjectSummary> summaries = listing.getObjectSummaries(); // create a list of object summaries

for (S3ObjectSummary obj : summaries){ // for each object...
  System.out.println(`found: `+obj.getKey()); // display 'found: ' and then the name of the object
}
```

## Delete object

```java
AmazonS3 cos = new AmazonS3Client();
cos.setEndpoint("https://s3-api.us-geo.objectstorage.softlayer.net");

cos.deleteObject( // delete the Object, passing…
"sample", // the name of the Bucket that stores the Object,
"myFile.txt" // and he name of the Object to be deleted
);
```

## API reference

This list summarizes the AWS Java SDK methods that are supported by IBM COS. More detailed documentation on individual classes and methods can be found in the [GitHub repository](https://github.com/aws/aws-sdk-java).

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
