---

copyright:
  years: 2017, 2018
lastupdated: "2018-08-22"

---

# Using Python

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

Python support is provided through a fork of the Boto library.  It can be installed from the Python Package Index via `pip install ibm-cos-sdk`.

Source code can be found at [GitHub](https://github.com/ibm/ibm-cos-sdk-python/).

The `ibm_boto3` library provides complete access to the {{site.data.keyword.cos_full}} API.  Endpoints, an API key, and the instance ID must be specified when creating a service resource or low-level client as shown in the following basic examples.

The service instance ID is also referred to as a _resource instance ID_.  The value can be found by creating a [service credential](/docs/services/cloud-object-storage/iam/service-credentials.html), or through the CLI.
{:tip}

Detailed documentation can be found at [here](https://ibm.github.io/ibm-cos-sdk-python/).

## Migrating from 1.x.x
The 2.0 release of the SDK introduces a namespacing change that allows an application to make use of the original `boto3` library to connect to AWS resources within the same application or environment.  To migrate from 1.x to 2.0 some changes are necessary.

    1. Update the `requirements.txt`, or from PyPI via `pip install -U ibm-cos-sdk`.  Verify no older versions exist with `pip list | grep ibm-cos`.
    2. Update any import declarations from `boto3` to `ibm_boto3`.
    3. If needed, reinstall the original `boto3` by updating the `requirements.txt`, or from PyPI via `pip install boto3`.

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

Code examples were written using **Python 2.7.15**

### Initializing configuration
{: #init-config}

```python
# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>"
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.ng.bluemix.net/oidc/token"
COS_SERVICE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location"

# Create resource
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_SERVICE_CRN,
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

*SDK References*
* [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}

### Creating a new bucket
```python
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create bucket: {0}".format(e))
```

*SDK References*
* Classes
  * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Methods
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

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

#### Manually execute a multi-part upload
If desired, the [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} class can be used to perform a multi-part upload.  This can be useful if more control over the upload process is necessary.

```python
def multi_part_upload_manual(bucket_name, item_name, file_path):
    try:
        # create client object
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT
        )
    
        print("Starting multi-part upload for {0} to bucket: {1}\n".format(item_name, bucket_name))

        # initiate the multi-part upload
        mp = cos_cli.create_multipart_upload(
            Bucket=bucket_name,
            Key=item_name
        )

        upload_id = mp["UploadId"]

        # min 5MB part size
        part_size = 1024 * 1024 * 5
        file_size = os.stat(file_path).st_size
        part_count = int(math.ceil(file_size / float(part_size)))
        data_packs = []
        position = 0
        part_num = 0

        # begin uploading the parts
        with open(file_path, "rb") as file:
            for i in range(part_count):
                part_num = i + 1
                part_size = min(part_size, (file_size - position))

                print("Uploading to {0} (part {1} of {2})".format(item_name, part_num, part_count))

                file_data = file.read(part_size)

                mp_part = cos_cli.upload_part(
                    Bucket=bucket_name,
                    Key=item_name,
                    PartNumber=part_num,
                    Body=file_data,
                    ContentLength=part_size,
                    UploadId=upload_id
                )

                data_packs.append({
                    "ETag":mp_part["ETag"],
                    "PartNumber":part_num
                })

                position += part_size

        # complete upload
        cos_cli.complete_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id,
            MultipartUpload={
                "Parts": data_packs
            }
        )
        print("Upload for {0} Complete!\n".format(item_name))
    except ClientError as be:
        # abort the upload
        cos_cli.abort_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id            
        )
        print("Multi-part upload aborted for {0}\n".format(item_name))
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*SDK References*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Methods
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### Large Object Upload using TransferManager
{: #transfer-manager}

The `TransferManager` provides another way to execute large file transfers by automatically incorporating multi-part uploads whenever necessary setting configuration parameters.

```python
def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # set the chunk size to 5 MB
    part_size = 1024 * 1024 * 5

    # set threadhold to 5 MB
    file_threshold = 1024 * 1024 * 5

    # Create client connection
    cos_cli = ibm_boto3.client("s3",
        ibm_api_key_id=COS_API_KEY_ID,
        ibm_service_instance_id=COS_SERVICE_CRN,
        ibm_auth_endpoint=COS_AUTH_ENDPOINT,
        config=Config(signature_version="oauth"),
        endpoint_url=COS_ENDPOINT
    )

    # set the transfer threshold and chunk size in config settings
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # create transfer manager
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_cli, config=transfer_config)

    try:
        # initiate file upload
        future = transfer_mgr.upload(file_path, bucket_name, item_name)

        # wait for upload to complete
        future.result()

        print ("Large file upload complete!")
    except Exception as e:
        print("Unable to complete large file upload: {0}".format(e))
    finally:
        transfer_mgr.shutdown()
```

## List items in a bucket (v2)
{: #list-objects-v2}

The [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} object contains an updated method to list the contents ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}).  This method allows you to limit the number of records returned and retrieve the records in batches.  This could be useful for paging your results within an application and improve performance.

```python
def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        # create client object
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT

        more_results = True
        next_token = ""

        while (more_results):
            response = cos_cli.list_objects_v2(Bucket=bucket_name, MaxKeys=max_keys, ContinuationToken=next_token)
            files = response["Contents"]
            for file in files:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))
            
            if (response["IsTruncated"]):
                next_token = response["NextContinuationToken"]
                print("...More results in next batch!\n")
            else:
                more_results = False
                next_token = ""

        log_done()
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*SDK References*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Methods
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Using Key Protect

Key Protect can be added to a storage bucket to encrypt sensitive data at rest in the cloud.

### Before You Begin

The following items are necessary in order to create a bucket with Key-Protect enabled:

* A Key Protect service [provisioned](/docs/services/keymgmt/keyprotect_provision.html)
* A Root key available (either [generated](/docs/services/keymgmt/keyprotect_create_root.html#create_root_keys) or [imported](/docs/services/keymgmt/keyprotect_import_root.html#import_root_keys))

### Retrieving the Root Key CRN

1. Retrieve the [instance ID](/docs/services/keymgmt/keyprotect_authentication.html#retrieve_instance_ID) for your Key Protect service
2. Use the [Key Protect API](/docs/services/keymgmt/keyprotect_authentication.html#access-api) to retrieve all your [available keys](/docs/services/keymgmt/keyprotect_authentication.html#form_api_request)
    * You can either use `curl` commands or an API REST Client such as [Postman](../api-reference/postman.html) to access the [Key Protect API](/docs/services/keymgmt/keyprotect_authentication.html#access-api).
3. Retrieve the CRN of the root key you will use to enabled Key Protect on the your bucket.  The CRN will look similar to below:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creating a bucket with key-protect enabled
```python
COS_KP_ALGORITHM = "<algorithm>"
COS_KP_ROOTKEY_CRN = "<root-key-crn>"

# Create a new bucket with key protect (encryption)
def create_bucket_kp(bucket_name):
    print("Creating new encrypted bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            },
            IBMSSEKPEncryptionAlgorithm=COS_KP_ALGORITHM,
            IBMSSEKPCustomerRootKeyCrn=COS_KP_ROOTKEY_CRN
        )
        print("Encrypted Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create encrypted bucket: {0}".format(e))
```

*Key Values*
* `<algorithm>` - The encryption algorithm used for new objects added to the bucket (Default is AES256).
* `<root-key-crn>` - CRN of the Root Key obtained from the Key Protect service.

*SDK References*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Methods
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Using Aspera High-Speed Transfer

By installing the [Aspera SDK](/docs/services/cloud-object-storage/basics/aspera.html#aspera-sdk-python) you can utilize high-speed file transfers within your application.

### Initalizing the AsperaTransferManager

Pass your existing [S3 Client](#init-config) object to create the AsperaTransferManager

```python
transfer_manager = AsperaTransferManager(client)
```

You will need to provide an IAM API Key for Aspera transfers.  HMAC Credentials are **NOT** currently supported.  For more information on IAM, [click here](/docs/services/cloud-object-storage/iam/overview.html#getting-started-with-iam).
{:tip}

You can also allow the `AsperaTransferManager` to use multiple sessions with an additonal configuration option.

The minimum thresholds for using multi-session:
* 2 sessions
* 60 MB threshold (*minimum 100 MB total file size*)

```python
# Configure 5 sessions for transfer, or specify "all" for dynamic number of sessions.
ms_transfer_config = AsperaConfig(multi_session=5)

# Create Transfer Manager
transfer_manager = AsperaTransferManager(client=client, transfer_config=ms_transfer_config)
```


### File Upload

```python
bucket_name = "<bucket-name>"
upload_filename = "<path-to-file>"
object_name = "<item-name>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload(upload_filename, bucket_name, object_name, None, None)

    # Wait for upload to complete
    future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-file>` - directory and file name to the file to be uploaded to Object Storage.
* `<item-name>` - name of the new file added to the bucket.

### File Download

```python
bucket_name = "<bucket-name>"
download_filename = "<path-to-local-file>"
object_name = "<object-to-download>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download(bucket_name, object_name, download_filename, None, None)

    # Wait for download to complete
    future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<path-to-local-file>` - directory and file name where save the file to the local system.
* `<object-to-download>` - name of the file in the bucket to download.

### Directory Upload

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY, and have objects in it.
local_upload_directory = "<path-to-local-directory>"
# THIS SHOULD NOT HAVE A LEADING "/"
remote_directory = "<bucket-directory>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory, None, None)

    # Wait for upload to complete
    future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<path-to-local-directory>` - local directory that contains the files to be uploaded.  Must have leading and trailing `/` (i.e. `/Users/testuser/Documents/Upload/`)
* `<bucket-directory>` - name of the directory in the bucket to store the files. Must not have a leading `/` (i.e. `newuploads/`)

### Directory Download
```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY
local_download_directory = "<path-to-local-directory>"
remote_directory = "<bucket-directory>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

    # Wait for download to complete
    future.result()
```

*Key Values*
* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<path-to-local-directory>` - local directory to save the downloaded files.  Must have leading and trailing `/` (i.e. `/Users/testuser/Downloads/`)
* `<bucket-directory>` - name of the directory in the bucket to store the files. Must not have a leading `/` (i.e. `todownload/`)

### Using Subscribers

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
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### Pause/Resume/Cancel

The SDK provides the ability to manage the progress of file/directory transfers though the following methods of the `AsperaTransferFuture` object:

* `pause()`
* `resume()`
* `cancel()`

```python
# Create Transfer manager
bucket_name = "<bucket-name>"
local_download_directory = "<path-to-local-directory>"
remote_directory = "<bucket-directory>"

with AsperaTransferManager(client) as transfer_manager:

    # download a directory with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

    # pause the transfer
    future.pause()

    # resume the transfer
    future.resume()

    # cancel the transfer
    future.cancel()
```