---

copyright:
  years: 2018
lastupdated: "2018-07-19"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Aspera High-Speed Transfer SDK

## Using Java

### Getting the SDK

The best way to use {{site.data.keyword.cos_full_notm}} and Aspera Connect Java SDK is to use Maven to manage dependencies. If you aren't familiar with Maven, you get can get up and running using the [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) guide.

Maven uses a file named `pom.xml` to specify the libraries (and their versions) needed for a Java project.  Below is an example `pom.xml` file for using the {{site.data.keyword.cos_full_notm}} and Aspera Java SDK

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
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera</artifactId>
            <version>0.1.161418</version>
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
### Creating a client and sourcing credentials

To connect to COS, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.  

More information for using a credentials file is available in the [Java SDK](/docs/services/cloud-object-storage/libraries/java.html#client-credentials).

An example for storing and using VCAP variables is available in the [Cloud Foundry Applications](/docs/services/cloud-object-storage/info/cof.html#storing-credentials-as-vcap-variables) guide.

### Code Examples

The following operations are **supported**:
* File upload/download
* Directory upload/download
* Pause/Resume/Cancel operations

The following items are **not supported**:
* Multi-threading within the Aspera Transfer Manager
* Sub-directory exclusion
* Configuration settings
    * Minimal configuration settings can be overrided by are subject to change
* Windows OS
* HMAC credentials

#### Initializing configuration

```java
private static final String BLUEMIX_URL = "<endpoint>";
private static final String API_KEY = "<api-key>";
private static final String SERVICE_INSTANCE = "<service-instance-id>";
private static final String IAM_URL = "https://iam.ng.bluemix.net/oidc/token";

// Create S3 Client
BasicIBMOAuthCredentials credentials = new BasicIBMOAuthCredentials(API_KEY, SERVICE_INSTANCE);
EndpointConfiguration endpoint = new EndpointConfiguration(BLUEMIX_URL, "Us-South");

AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withEndpointConfiguration(endpoint)
        .withCredentials(new AWSStaticCredentialsProvider(credentials)).withPathStyleAccessEnabled(true)
        .withIAMEndpoint(IAM_URL).build();
```
*Key Values*
* `<endpoint>` - public endpoint for your cloud object storage (available from the [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window}).
* `<api-key>` - api key generated when creating the service credentials (write access is required for creation and deletion examples).
* `<service-instance-id>` - service ID for your cloud object storage (available through [IBM Cloud CLI](../getting-started-cli.html) or [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window}). (i.e. `ServiceId-cb9fc456-3d8d-493a-afd5-d08ec674c18d`)

#### File Upload

```java
String bucketName = "<bucket-name>";
String filePath = "<path-to-local-file>";
String itemName = "<item-name>";

//Load file
File inputFile = new File(filePath);

// Create AsperaTransferManager for FASP upload
AsperaTransferManager asperaTransfer = new AsperaTransferManagerBuilder(API_KEY, s3Client).build();

// Upload test file and report progress
AsperaTransfer AsperaTransfer = asperaTransfer.upload(bucketName, itemName, inputFile);
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-local-file>` - directory and file name to the file to be uploaded to Object Storage.
* `<item-name>` - name of the new file added to the bucket.

#### File Download

```java
String bucketName = "<bucket-name>";
String outputFile = "<path-to-local-file>";
String itemName = "<item-name>";

// Create AsperaTransferManager for FASP download
AsperaTransferManager asperaTransfer = new AsperaTransferManagerBuilder(API_KEY, s3Client).build();

// Download file
AsperaTransfer AsperaTransfer = asperaTransfer.download(bucketName, itemName, outputFile);
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-local-file>` - directory and file name to save from Object Storage.
* `<item-name>` - name of the file in the bucket.

#### Directory Upload

```java
String bucketName = "<bucket-name>";
String directoryPath = "<path-to-local-directory>";
String directoryPrefix = "<virtual-directory-prefix>";

//Load Directory
File inputDirectory = new File(directoryPath);

// Create AsperaTransferManager for FASP upload
AsperaTransferManager asperaTransfer = new AsperaTransferManagerBuilder(API_KEY, s3Client).build();

// Upload test directory
AsperaTransfer AsperaTransfer = asperaTransfer.uploadDirectory(bucketName, directoryPrefix, inputDirectory, true);
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-local-directory>` - directory of the files to be uploaded to Object Storage.
* `<virtual-directory-prefix>` - name of the directory prefix to be added to each file upon upload.  Use null or empty string to upload the files to the bucket root.

#### Directory Download

```java
String bucketName = "<bucket-name>";
String directoryPath = "<path-to-local-directory>";
String directoryPrefix = "<virtual-directory-prefix>";

//Load Directory
File outputDirectory = new File(directoryPath);

// Create AsperaTransferManager for FASP download
AsperaTransferManager asperaTransfer = new AsperaTransferManagerBuilder(API_KEY, s3Client).build();

// Download test directory
AsperaTransfer AsperaTransfer = asperaTransfer.downloadDirectory(bucketName, directoryPrefix, outputDirectory);
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-local-directory>` - directory to save downloaded files from Object Storage.
* `<virtual-directory-prefix>` - name of the directory prefix of each file to download.  Use null or empty string to download all files in the bucket.


## Using Python

### Getting the SDK

The {{site.data.keyword.cos_full_notm}} and Aspera Connect Python SDK is available from the Python Package Index (PyPI) software repository.  The Aspera SDK is an optional dependency that can be included in the requirements.txt or setup.py.

```json
extras_requires = {
    "aspera": ["ibm-aspera-sdk==1.0.0"]
}
```

Both can be installed using the following commands:

For Mac OS X
```
pip install ibm-cos-sdk["aspera"]
pip install cos-aspera-mac-10-7-64-py-27
```

For Linux
```
pip install ibm-cos-sdk["aspera"]
pip install cos-aspera-linux-64-py-36
```

An additional dependency is also required for **Python 2.7**
```
pip install backports.functools_lru_cache
```

Final step is to add COS Aspera install path to the `PYTHONPATH` environment variable (typically located in your `site-packages` folder, i.e. `~/Library/Python/2.7/lib/python/site-packages`)

```
export PYTHONPATH=$PYTHONPATH:~/Library/Python/2.7/lib/python/site-packages/cos-aspera-mac-10-7-64-py-27
```

To test your installation run the following command and ensure you do not receive any errors:

```
python -c  "import faspmanager2"
```

### Creating a client and sourcing credentials

To connect to COS, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.  

More information for using a credentials file is available in the [Python SDK](/docs/services/cloud-object-storage/libraries/python.html#client-credentials).

An example for storing and using VCAP variables is available in the [Cloud Foundry Applications](/docs/services/cloud-object-storage/info/cof.html#storing-credentials-as-vcap-variables) guide.

### Code Examples

The Aspera SDK currently supports **Python 2.7** only.  The following operations are **supported**:
* File upload/download
* Directory upload/download
* Pause/Resume/Cancel operations

The following items are **not supported**:
* Multi-threading within the Aspera Transfer Manager
* Sub-directory exclusion
* Configuration settings
    * Minimal configuration settings can be overrided by are subject to change
* Windows OS
* HMAC credentials

#### Initializing configuration

```python
# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>"
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.ng.bluemix.net/oidc/token"
COS_SERVICE_CRN = "<resource-instance-id>"

# Create client
session = boto3.session.Session()
client = session.client("s3",
                        endpoint_url=COS_ENDPOINT,
                        ibm_api_key_id=COS_API_KEY_ID,
                        ibm_service_instance_id=COS_SERVICE_CRN,
                        ibm_auth_endpoint=COS_AUTH_ENDPOINT,
                        verify=False,
                        config=Config(signature_version='oauth'))
)
```

*Key Values*
* `<endpoint>` - public endpoint for your cloud object storage (available from the [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window}).
* `<api-key>` - api key generated when creating the service credentials (write access is required for creation and deletion examples).
* `<resource-instance-id>` - resource ID for your cloud object storage (available through [IBM Cloud CLI](../getting-started-cli.html) or [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window}).

#### File Upload

```python
bucket_name = "<bucket-name>"
upload_filename = "<path-to-file>"
object_name = "<item-name>"

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Perform upload
future = transfer_manager.upload(upload_filename, bucket_name, object_name, None, None)

# Wait for upload to complete
future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-file>` - directory and file name to the file to be uploaded to Object Storage.
* `<item-name>` - name of the new file added to the bucket.

#### File Download

```python
bucket_name = "<bucket-name>"
download_filename = "<path-to-local-file>"
object_name = "<object-to-download>"

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Get object with Aspera
future = transfer_manager.download(bucket_name, object_name, download_filename, None, None)

# Wait for download to complete
future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-local-file>` - directory and file name where save the file to the local system.
* `<object-to-download>` - name of the file in the bucket to download.

#### Directory Upload

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY, and have objects in it.
local_upload_directory = "<path-to-local-directory>"
# THIS SHOULD NOT HAVE A LEADING "/"
remote_directory = "<bucket-directory>"

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Perform upload
future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory, None, None)

# Wait for upload to complete
future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<path-to-local-directory>` - local directory that contains the files to be uploaded.  Must have leading and trailing `/` (i.e. `/Users/testuser/Documents/Upload/`)
* `<bucket-directory>` - name of the directory in the bucket to store the files. Must not have a leading `/` (i.e. `newuploads/`)

#### Directory Download
```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY
local_download_directory = "<path-to-local-directory>"
remote_directory = "<bucket-directory>"

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Get object with Aspera
future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

# Wait for download to complete
future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<path-to-local-directory>` - local directory to save the downloaded files.  Must have leading and trailing `/` (i.e. `/Users/testuser/Downloads/`)
* `<bucket-directory>` - name of the directory in the bucket to store the files. Must not have a leading `/` (i.e. `todownload/`)

#### Using Subscribers

Subscribers allow you monitor the progress of your operations by attach custom callback methods.  There are three subscribers currently available:

* Queued
* Progress
* Done

```python
bucket_name = "<bucket-name>"
local_download_directory = "<path-to-local-directory>"
remote_directory = "<bucket-directory>"

# Subscriber callbacks
class CallbackOnQueued(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_queued(self, future, **kwargs):
        print("Directory download queued.")

class CallbackOnProgress(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_progress(self, future, bytes_transferred, **kwargs):
        print("Directory download in progress: %s bytes transferred" % bytes_transferred)

class CallbackOnDone(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_done(self, future, **kwargs):
        print("Downloads complete!")

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Attach subscribers
subscribers = [CallbackOnQueued(), CallbackOnProgress(), CallbackOnDone()]

# Get object with Aspera
future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, subscribers)

# Wait for download to complete
future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<path-to-local-directory>` - local directory to save the downloaded files.  Must have leading and trailing `/` (i.e. `/Users/testuser/Downloads/`)
* `<bucket-directory>` - name of the directory in the bucket to store the files. Must not have a leading `/` (i.e. `todownload/`)

The sample code above produces the following output:

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
Directory download in progress: 2095104 bytes transferred
Directory download in progress: 4190208 bytes transferred
Directory download in progress: 5237760 bytes transferred
Directory download in progress: 7332864 bytes transferred
Directory download in progress: 8380416 bytes transferred
Directory download in progress: 10475520 bytes transferred
Directory download in progress: 12570624 bytes transferred
Directory download in progress: 13618176 bytes transferred
Directory download in progress: 15713280 bytes transferred
Directory download in progress: 16760832 bytes transferred
Directory download in progress: 18855936 bytes transferred
Directory download in progress: 20706509 bytes transferred
Directory download in progress: 28920781 bytes transferred
Directory download in progress: 32225357 bytes transferred
Directory download in progress: 33957197 bytes transferred
Directory download in progress: 35368013 bytes transferred
Directory download in progress: 36415565 bytes transferred
Directory download in progress: 37463117 bytes transferred
Directory download in progress: 38510669 bytes transferred
Directory download in progress: 40605773 bytes transferred
Directory download in progress: 41418650 bytes transferred
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

## Service Key API

### List Aspera service api keys for a bucket

A `GET` issued to a bucket with the proper parameters retrieves Aspera service api keys for a bucket.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?faspConnectionInfo= # path style
GET https://{bucket-name}.{endpoint}?faspConnectionInfo= # virtual host style
```

**Sample request (IAM)**

This is an example of listing the Aspera service api keys on the "apiary" bucket.

```http
GET /apiary?faspConnectionInfo= HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
GET /apiary?faspConnectionInfo= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
GET /faspConnectionInfo?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&cors=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Thu, 5 Jul 2018 15:20:30 GMT
X-Clv-Request-Id: dbfc054e-702e-4d72-9456-0393efa63159
Accept-Ranges: bytes
Server: Cleversafe/3.13.4.52
X-Clv-S3-Version: 2.5
x-amz-request-id: dbfc054e-702e-4d72-9456-0393efa63159
Content-Type: application/xml
Content-Length: 326
```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<FASPConnectionInfo xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <AccessKey>
    <Id>5CX9km-xFFFjAPab0aJ7HaFa</Id>
    <Secret>g0sXx2X1VbLXXb9vjPABwjHU1CYPKhZT9-X1x0E6xXXX</Secret>
  </AccessKey>
  <ATSEndpoint>https://ats-sl-dal.aspera.io:443</ATSEndpoint>
</FASPConnectionInfo>
```

----

### Delete retry AK

added to delete bucket in api-reference-buckets

#### Optional headers

Name | Type | Description
--- | ---- | ------------
`aspera-ak-max-tries` | string | Specifies the number of times to attempt the delete operation.  Default value is 2.
