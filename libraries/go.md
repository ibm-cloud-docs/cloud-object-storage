---

copyright:
  years: 2019
lastupdated: "2019-01-09"

---

# Using Go

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

The {{site.data.keyword.cos_full}} SDK for Go is comprehensive, and has features and capabilities not described in this guide.  For detailed class and method documentation [see the Javadoc](https://ibm.github.io/ibm-cos-sdk-java/). Source code can be found in the [GitHub repository](https://github.com/aws/aws-sdk-go).

## Getting the SDK
Use `go get` to retrieve the SDK to add it to your GOPATH workspace, or project's Go module dependencies.

```
go get github.ibm.com/cs-developer-enablement/ibm-cos-sdk-go
```

To update the SDK use `go get -u` to retrieve the latest version of the SDK..

```
go get -u github.ibm.com/cs-developer-enablement/ibm-cos-sdk-go
```

### Import packages
After you have installed the SDK, you will need to import the packages into your Go applications to use the SDK, as shown in the following example:
```
import "github.com/IBM/ibm-cos-sdk-go/service/s3"
```


## Creating a client and sourcing credentials
{: #client-credentials}

To connect to COS, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.

After generating a [Service Credential](/docs/services/cloud-object-storage/iam/service-credentials.html), the resulting JSON document can be saved to `~/.bluemix/cos_credentials`.  The SDK will automatically source credentials from this file unless other credentials are explicitly set during client creation. If the `cos_credentials` file contains HMAC keys the client will authenticate with a signature, otherwise the client will use the provided API key to authenticate using a bearer token.

If migrating from AWS S3, you can also source credentials data from  `~/.aws/credentials` in the format:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

If both `~/.bluemix/cos_credentials` and `~/.aws/credentials` exist, `cos_credentials` will take preference.


## Code Examples



### Initializing configuration
{: #init-config}

```Go
import (
    "github.ibm.com/cs-developer-enablement/ibm-cos-sdk-go/aws/credentials/ibmiam/providers"
)

const (

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://cos-service.bluemix.net/endpoints
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.ng.bluemix.net/oidc/token"
COS_RESOURCE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location>"

# Create resource
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

```
*Key Values*
* `<endpoint>` - public endpoint for your cloud object storage (available from the [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window})
* `<api-key>` - api key generated when creating the service credentials (write access is required for creation and deletion examples)
* `<resource-instance-id>` - resource ID for your cloud object storage (available through [IBM Cloud CLI](../getting-started-cli.html) or [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window})
* `<location>` - default location for your cloud object storage (must match the region used for `<endpoint>`)











## Code Examples

Note that when adding custom metadata to an object, it is necessary to create an `ObjectMetadata` object using the SDK, and not to manually send a custom header containing `x-amz-meta-{key}`.  The latter can cause issues when authenticating using HMAC credentials.
{: .tip}

### Creating a new bucket
A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage/basics/classes#locationconstraint).

```Go
func (suite *SuiteBucketCreate) TestBadExists() {
	bName := genNameValidChars(11)

	_, e := createBucket(bName, suite.mainSvc)
	suite.Require().Nil(e, "%s", e)
	suite.mainBucket = bName

	_, e = createBucket(bName, suite.mainSvc)
	suite.Require().NotNil(e, "Operation should Not Succeed!")

	if awsErr, ok := e.(awserr.RequestFailure); ok {
		suite.Assert().Equal(bucketAlreadyOwnedByYouCode, awsErr.Code())
		suite.Assert().Equal(httpConflictStatusCode, awsErr.StatusCode())
	} else {
		suite.Fail("Unexpected Error!")
	}
}
```

*SDK References*
* Classes
  * [Bucket](#){:new_window}
* Methods
    * [create](#){:new_window}

### Creating a new text file
```python
def create_text_file(bucket_name, item_name, file_text):
    print("Creating new item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).put(
            Body=file_text
        )
        print("Item: {0} created!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create text file: {0}".format(e))
```

*SDK References*
* Classes
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Methods
    * [put](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### List available buckets
```python
def get_buckets():
    print("Retrieving list of buckets")
    try:
        buckets = cos.buckets.all()
        for bucket in buckets:
            print("Bucket Name: {0}".format(bucket.name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve list buckets: {0}".format(e))
```

*SDK References*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* Collections
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### List items in a bucket
```python
def get_bucket_contents(bucket_name):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        files = cos.Bucket(bucket_name).objects.all()
        for file in files:
            print("Item: {0} ({1} bytes).".format(file.key, file.size))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*SDK References*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* Collections
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### Get file contents of particular item
```python
def get_item(bucket_name, item_name):
    print("Retrieving item from bucket: {0}, key: {1}".format(bucket_name, item_name))
    try:
        file = cos.Object(bucket_name, item_name).get()
        print("File Contents: {0}".format(file["Body"].read()))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve file contents: {0}".format(e))
```

*SDK References*
* Classes
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Methods
    * [get](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### Delete an item from a bucket
```python
def delete_item(bucket_name, item_name):
    print("Deleting item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).delete()
        print("Item: {0} deleted!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete item: {0}".format(e))
```

*SDK References*
* Classes
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Methods
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### Delete multiple items from a bucket

The delete request can contain a maximum of 1000 keys that you want to delete.  While this is very useful in reducing the per-request overhead, be mindful when deleting a large number of keys.  Also take into account the sizes of the objects to ensure suitable performance.
{:tip}

```python
def delete_items(bucket_name):
    try:
        delete_request = {
            "Objects": [
                { "Key": "deletetest/testfile1.txt" },
                { "Key": "deletetest/testfile2.txt" },
                { "Key": "deletetest/testfile3.txt" },
                { "Key": "deletetest/testfile4.txt" },
                { "Key": "deletetest/testfile5.txt" }
            ]
        }

        response = cos_cli.delete_objects(
            Bucket=bucket_name,
            Delete=delete_request
        )

        print("Deleted items for {0}\n".format(bucket_name))
        print(json.dumps(response.get("Deleted"), indent=4))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to copy item: {0}".format(e))
```

*SDK References*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Methods
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### Delete a bucket
```python
def delete_bucket(bucket_name):
    print("Deleting bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).delete()
        print("Bucket: {0} deleted!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete bucket: {0}".format(e))
```

*SDK References*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Methods
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### View a bucket's security
```python
def get_bucket_acl(bucket_name):
    print("Retrieving ACL for bucket: {0}".format(bucket_name))
    try:
        acl_data = cos.BucketAcl(bucket_name)
        print("Owner: {0}".format(acl_data.owner["DisplayName"]))
        for grant in acl_data.grants:
            display_name = grant["Grantee"]["DisplayName"]
            permission = grant["Permission"]
            print("User: {0} ({1})".format(display_name, permission))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket ACL: {0}".format(e))
```

*SDK References*
* Classes
    * [BucketAcl](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucketacl){:new_window}
* Attributes
    * [grants](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.BucketAcl.grants){:new_window}
    * [owner](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ObjectAcl.owner){:new_window}

### View a file's security
```python
def get_item_acl(bucket_name, item_name):
    print("Retrieving ACL for {0} from bucket: {1}".format(item_name, bucket_name))
    try:
        acl_data = cos.ObjectAcl(bucket_name, item_name)
        print("Owner: {0}".format(acl_data.owner["DisplayName"]))
        for grant in acl_data.grants:
            display_name = grant["Grantee"]["DisplayName"]
            permission = grant["Permission"]
            print("User: {0} ({1})".format(display_name, permission))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve item ACL: {0}".format(e))
```

*SDK References*
* Classes
    * [ObjectAcl](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectacl){:new_window}
* Attributes
    * [grants](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ObjectAcl.grants){:new_window}
    * [owner](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ObjectAcl.owner){:new_window}

### Execute a multi-part upload
{: #multipart-upload}

#### Upload binary file (preferred method)
The [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} method of the [S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} class automatically executes a multi-part upload when necessary.  The [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} class is used to determine the threshold for using the mult-part upload.

```python
def multi_part_upload(bucket_name, item_name, file_path):
    try:
        print("Starting file transfer for {0} to bucket: {1}\n".format(item_name, bucket_name))
        # set 5 MB chunks
        part_size = 1024 * 1024 * 5

        # set threadhold to 15 MB
        file_threshold = 1024 * 1024 * 15

        # set the transfer threshold and chunk size
        transfer_config = ibm_boto3.s3.transfer.TransferConfig(
            multipart_threshold=file_threshold,
            multipart_chunksize=part_size
        )

        # the upload_fileobj method will automatically execute a multi-part upload
        # in 5 MB chunks for all files over 15 MB
        with open(file_path, "rb") as file_data:
            cos.Object(bucket_name, item_name).upload_fileobj(
                Fileobj=file_data,
                Config=transfer_config
            )

        print("Transfer for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*SDK References*
* Classes
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* Methods
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}
