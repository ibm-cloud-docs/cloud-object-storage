---

copyright:
  years: 2017, 2025
lastupdated: "2025-09-12"

keywords: object storage, python, sdk, aspera, apache, asperatransfermanager

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using Python
{: #python}

Python support is provided through a fork of the `boto3` library with features to make the most of {{site.data.keyword.cos_full}}.
{: shortdesc}

It can be installed from the Python Package Index through `pip install ibm-cos-sdk`.

Source code can be found at [GitHub](https://github.com/ibm/ibm-cos-sdk-python/){: external}.

The `ibm_boto3` library provides complete access to the {{site.data.keyword.cos_full}} API. Endpoints, an API key, and the instance ID must be specified during creation of a service resource or low-level client as shown in the following basic examples.

The service instance ID is also referred to as a _resource instance ID_. The value can be found by creating a [service credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), or through the CLI.
{: tip}

Detailed documentation can be found at [here](https://ibm.github.io/ibm-cos-sdk-python/){: external}.

## Creating a client and sourcing credentials
{: #python-credentials}

To connect to COS, a client is created and configured using credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.

After generating a [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), the resulting JSON document can be saved to `~/.bluemix/cos_credentials`. The SDK will automatically source credentials from this file unless other credentials are explicitly set during client creation. If the `cos_credentials` file contains HMAC keys the client authenticates with a signature, otherwise the client uses the provided API key to authenticate by using a bearer token (using an API key still requires the `config=Config(signature_version="oauth")` to be included during client creation).

If migrating from AWS S3, you can also source credentials data from `~/.aws/credentials` in the format:

``` sh
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```
{: codeblock}

**Note**: If both `~/.bluemix/cos_credentials` and `~/.aws/credentials` exist, `cos_credentials` takes preference.

### Gather required information
{: #python-prereqs}

The following variables appear in the examples:

* `bucket_name` must be a [unique and DNS-safe](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket) string. Because bucket names are unique across the entire system, these values need to be changed if this example is run multiple times. Note that names are reserved for 10 - 15 minutes after deletion.
* `ibm_api_key_id` is the value found in the [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) as `apikey`.
* `ibm_service_instance_id` is the value found in the [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) as `resource_instance_id`.
* `endpoint_url` is a service endpoint URL, inclusive of the `https://` protocol. This value is **not** the `endpoints` value that is found in the [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials). For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `LocationConstraint` is a [valid provisioning code](/docs/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) that corresponds to the `endpoint` value.


## Code Examples
{: #python-examples}

Code examples are tested on supported release versions of Python.

In your code, you must remove the angled brackets or any other excess characters that are provided here as illustration.
{: note}

### Initializing configuration
{: #python-examples-init}

This example creates a `resource` object. A resource provides an object-oriented interface to COS. This allows for a higher level of abstraction than the low-level calls provided by a client object.

Note that some operations (such as Aspera high-speed transfer) require a `client` object. Aspera itself requires Python version 3.6.
{: important}

**Legacy Notice**: Support for Aspera is considered legacy. Instead, use the [Aspera Transfer SDK](https://developer.ibm.com/apis/catalog/aspera--aspera-transfer-sdk/API%20Reference).
{: important}

```python
import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # eg "W00YixxxxxxxxxxMB-odB-2ySfTrFBIQQWanc--P3byk"
COS_INSTANCE_CRN = "<service-instance-id>" # eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003xxxxxxxxxx1c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Create resource
cos_resource = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_INSTANCE_CRN,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)
```
{: codeblock}
{: python}

A client provides a low-level interface to the COS S3 API. This allows for processing HTTP responses directly, rather than making use of abstracted methods and attributes provided by a resource to access the information contained in headers or XML response payloads.

```python

import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # eg "W00YixxxxxxxxxxMB-odB-2ySfTrFBIQQWanc--P3byk"
COS_INSTANCE_CRN = "<service-instance-id>" # eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003xxxxxxxxxx1c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Create client
cos_client = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_INSTANCE_CRN,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)
```

#### Key Values
{: #init-cfg-key-values}

* `<endpoint>` - public endpoint for your cloud Object Storage with schema prefixed ('https://') (available from the [IBM Cloud Dashboard](https://cloud.ibm.com/resources){: external}). For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - api key generated when creating the service credentials (write access is required for creation and deletion examples)
* `<service-instance-id>` - resource ID for your cloud Object Storage (available through [IBM Cloud CLI](/docs/cli?topic=cli-idt-cli) or [IBM Cloud Dashboard](https://cloud.ibm.com/resources){: external})
* `<location>` - default location for your cloud Object Storage (must match the region that is used for `<endpoint>`)

#### SDK References
{: #init-cfg-sdk-refs}

* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){: external}


### Creating a new bucket
{: #python-examples-new-bucket}

The examples below uses client which is a low level interface.

A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```python
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos_client.create_bucket(
            Bucket=bucket_name,
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
{: codeblock}
{: python}

#### SDK References
{: #create-bucket-sdk-refs}

Methods

* [`create_bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_bucket){: external}

### Creating a new text file
{: #python-examples-new-file}

```python
def create_text_file(bucket_name, item_name, file_text):
    print("Creating new item: {0}".format(item_name))
    try:
        cos_client.put_object(
            Bucket=bucket_name,
            Key=item_name,
            Body=file_text
        )
        print("Item: {0} created!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create text file: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #create-text-file-sdk-refs}

Methods

* [`put_object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.put_object){: external}

### List available buckets
{: #python-examples-list-buckets}

```python
def get_buckets():
    print("Retrieving list of buckets")
    try:
        buckets = cos_client.list_buckets()
        for bucket in buckets["Buckets"]:
            print("Bucket Name: {0}".format(bucket["Name"]))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve list buckets: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #list-buckets-sdk-refs}

Methods

* [`list_buckets`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_buckets){: external}

### List items in a bucket
{: #python-examples-list-objects}

```python
def get_bucket_contents(bucket_name):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        files = cos_client.list_objects(Bucket=bucket_name)
        for file in files.get("Contents", []):
            print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #list-items-sdk-refs}

Methods

* [`list_objects`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects){: external}

### Get file contents of particular item
{: #python-examples-get-file-contents}

```python
def get_item(bucket_name, item_name):
    print("Retrieving item from bucket: {0}, key: {1}".format(bucket_name, item_name))
    try:
        file = cos_client.get_object(Bucket=bucket_name, Key=item_name)
        print("File Contents: {0}".format(file["Body"].read()))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve file contents: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #get-contents-sdk-refs}

Methods

* [`get_object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.get_object){: external}

### Delete an item from a bucket
{: #python-examples-delete-object}

```python
def delete_item(bucket_name, object_name):
    try:
        cos_client.delete_object(Bucket=bucket_name, Key=object_name)
        print("Item: {0} deleted!\n".format(object_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete object: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #delete-item-sdk-refs}

Methods

* [`delete_object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_object){: external}

### Delete multiple items from a bucket
{: #python-examples-delete-multiple-objects}

The delete request can contain a maximum of 1000 keys that you want to delete. While this is useful in reducing the per-request performance hit, be mindful when deleting many keys. Also, take into account the sizes of the objects to ensure suitable performance.
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

        response = cos_client.delete_objects(
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
{: codeblock}
{: python}

#### SDK References
{: #delete-mult-items-sdk-refs}

Methods

* [`delete_objects`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){: external}

### Delete a bucket
{: #python-examples-delete-bucket}

```python
def delete_bucket(bucket_name):
    print("Deleting bucket: {0}".format(bucket_name))
    try:
        cos_client.delete_bucket(Bucket=bucket_name)
        print("Bucket: {0} deleted!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete bucket: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #delete-bucket-sdk-refs}

Methods

* [`delete_bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_bucket){: external}

The bucket names are reserved for 10 - 15 minutes after deletion.
{: note}

### Run a multi-part upload
{: #python-examples-multipart}

#### Upload binary file (preferred method)
{: #python-examples-multipart-binary}

The [`upload_fileobj`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_fileobj){: external} method of the S3 Object automatically runs a multi-part upload when necessary. The [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){: external} class is used to determine the threshold for using the multi-part upload.

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
            cos_client.upload_fileobj(
                Bucket=bucket_name,
                Key=item_name,
                Fileobj=file_data,
                Config=transfer_config
            )

        print("Transfer for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #multipart-upload-sdk-refs}

Methods

* [`upload_fileobj`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_fileobj){: external}

#### Manually run a multi-part upload
{: #python-examples-multipart-manual}

If wanted, the [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){: external} class can be used to perform a multi-part upload. This can be useful if more control over the upload process is necessary.

```python
def multi_part_upload_manual(bucket_name, item_name, file_path):
    try:
        # create client object
        cos_client = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT
        )

        print("Starting multi-part upload for {0} to bucket: {1}\n".format(item_name, bucket_name))

        # initiate the multi-part upload
        mp = cos_client.create_multipart_upload(
            Bucket=bucket_name,
            Key=item_name
        )

        upload_id = mp["UploadId"]

        # min 20MB part size
        part_size = 1024 * 1024 * 20
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

                mp_part = cos_client.upload_part(
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
        cos_client.complete_multipart_upload(
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
        cos_client.abort_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id
        )
        print("Multi-part upload aborted for {0}\n".format(item_name))
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References continued
{: #manual-multipart-upload-sdk-refs}

Classes

* [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){: external}

Methods

* [`abort_multipart_upload`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){: external}
* [`complete_multipart_upload`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){: external}
* [`create_multipart_upload`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){: external}
* [`upload_part`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){: external}

### Large Object Upload by using TransferManager
{: #python-examples-multipart-transfer}

The `TransferManager` provides another way to run large file transfers by automatically incorporating multi-part uploads whenever necessary setting configuration parameters.

```python
def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # set the chunk size to 5 MB
    part_size = 1024 * 1024 * 5

    # set threadhold to 5 MB
    file_threshold = 1024 * 1024 * 5

    # Create client connection
    cos_client = ibm_boto3.client("s3",
        ibm_api_key_id=COS_API_KEY_ID,
        ibm_service_instance_id=COS_SERVICE_CRN,
        config=Config(signature_version="oauth"),
        endpoint_url=COS_ENDPOINT
    )

    # set the transfer threshold and chunk size in config settings
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # create transfer manager
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_client, config=transfer_config)

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
{: codeblock}
{: python}

### List items in a bucket (v2)
{: #python-examples-list-objects-v2}

The [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){: external} object has an updated method to list the contents ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){: external}). This method allows you to limit the number of records that are returned and retrieve the records in batches. This might be useful for paging your results within an application and improve performance.

```python
def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        # create client object
        cos_client = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT)

        more_results = True
        next_token = ""

        while (more_results):
            response = cos_client.list_objects_v2(Bucket=bucket_name, MaxKeys=max_keys, ContinuationToken=next_token)
            files = response["Contents"]
            for file in files:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))

            if (response["IsTruncated"]):
                next_token = response["NextContinuationToken"]
                print("...More results in next batch!\n")
            else:
                more_results = False
                next_token = ""

    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```
{: codeblock}
{: python}

#### SDK References
{: #list-items-v2-sdk-refs}

Methods

* [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){: external}

### Creating a Backup Policy
{: #python-examples-create-backup-policy}

```python
# Config values
api_key = "<API_KEY>"
vault_crn = "<SERVICE_INSTANCE_ID>"
source_bucket_name = "<BACKUP_VAULT_NAME>"
policy_name = "<POLICY_NAME>"

# Authenticator and client setup
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Create policy
create_backup_policy = rc_client.create_backup_policy(
        bucket=source_bucket_name,
        policy_name=policy_name,
        target_backup_vault_crn=vault_crn,
        backup_type="continuous",
        initial_retention={"delete_after_days": 1}
    )

# Print response
print(f" Policy created: { create_backup_policy }")

```
{: codeblock}
{: python}



### Listing a Backup Policy
{: #python-examples-list-backup-policy}

```python
# Config values
api_key = "<API_KEY>"
source_bucket_name = "<BACKUP_VAULT_NAME>"

# Authenticator and client setup
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# List all backup policies
list_response = rc_client.list_backup_policies(bucket=source_bucket_name)

print("\nList of backup policies:")
for policy in list_response.result.get("backup_policies", []):
    print(policy)

```
{: codeblock}
{: python}



### Get a Backup Policy
{: #python-examples-get-backup-policy}

```python
# Config
api_key = "<API_KEY>"
source_bucket_name = "<SOURCE_BUCKET_NAME>"
backup_vault_crn = "<BACKUP_VAULT_CRN>"
policy_name = "<POLICY_NAME>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Create backup policy
create_backup_policy_response = rc_client.create_backup_policy(
    bucket=source_bucket_name,
    policy_name=policy_name,
    target_backup_vault_crn=backup_vault_crn,
    backup_type="continuous",
    initial_retention={"delete_after_days": 1}
)

# Extract policy ID
policy_id = create_backup_policy_response.result.get("policy_id")

get_backup_policy_response = rc_client.get_backup_policy(
    bucket=source_bucket_name,
    policy_id=policy_id
)

print("\nFetched Backup Policy Details:")
print(get_backup_policy_response.result)

```
{: codeblock}
{: python}



### Delete a Backup Policy
{: #python-examples-delete-backup-policy}

```python
# Config
api_key = "<API_KEY>"
source_bucket_name = "<SOURCE_BUCKET_NAME>"
policy_id = "<POLICY_ID>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Delete the backup policy
delete_backup_policy_response = rc_client.delete_backup_policy(
    bucket=source_bucket_name,
    policy_id=policy_id
)

print(f"Backup policy '{policy_id}' deleted successfully.")

```
{: codeblock}
{: python}



### Creating a Backup Vault
{: #python-examples-create-backup-vault}

```python
# Config
api_key = "<API_KEY>"
service_instance_id = "<SERVICE_INSTANCE_ID>"
backup_vault_name = "<BACKUP_VAULT_NAME>"
region = "<REGION>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Create a backup vault
create_backup_vault_response = rc_client.create_backup_vault(
    service_instance_id=service_instance_id,
    backup_vault_name=backup_vault_name,
    region=region
)

# Output result
print("Backup vault created:")
print(create_backup_vault_response.result)

```
{: codeblock}
{: python}



### Listing Backup Vaults
{: #python-examples-list-backup-vault}

```python
# Config
api_key = "<API_KEY>"
service_instance_id = "<SERVICE_INSTANCE_ID>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# List backup vaults
list_backup_vaults_response = rc_client.list_backup_vaults(
    service_instance_id=service_instance_id
)

print("List of backup vaults:")
print(list_backup_vaults_response.result)

```
{: codeblock}
{: python}



### Get Backup Vaults
{: #python-examples-get-backup-vault}

```python
# Config
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Get backup vault
get_backup_vault = rc_client.get_backup_vault(
    backup_vault_name=backup_vault_name
)

# Output result
print("Backup vault details:")
print(get_backup_vault.result)

```
{: codeblock}
{: python}



### Update Backup Vaults
{: #python-examples-patch-backup-vault}

```python
# Config
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Update backup vault settings (disable activity tracking and metrics monitoring)
backup_vault_patch = {
    "activity_tracking": {"management_events": True},
    "metrics_monitoring": {"usage_metrics_enabled": True},
}

update_backup_vault_response = rc_client.update_backup_vault(
    backup_vault_name=backup_vault_name,
    backup_vault_patch=backup_vault_patch
)

# Output result
print("Backup vault updated successfully.")
print(update_backup_vault_response)

```
{: codeblock}
{: python}



### Delete a Backup Vault
{: #python-examples-delete-backup-vault}

```python
# Config
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Delete the backup vault
delete_vault_response = rc_client.delete_backup_vault(
    backup_vault_name=backup_vault_name
)

# Output result
print(f"Successfully deleted backup vault '{delete_vault_response}'.")

```
{: codeblock}
{: python}



### Listing Recovery Ranges
{: #python-examples-list-recovery-range}

```python
# Config
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# List recovery ranges
recovery_ranges_response = rc_client.list_recovery_ranges(
    backup_vault_name=backup_vault_name
)

# Output recovery range results
print("Recovery Ranges:")
print(recovery_ranges_response.result)

```
{: codeblock}
{: python}



### Get Recovery Range
{: #python-examples-get-recovery-range}

```python
# Config
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"
recovery_range_id = "<RECOVERY_RANGE_ID>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

get_recovery_range_response = rc_client.get_source_resource_recovery_range(
    backup_vault_name=backup_vault_name,
    recovery_range_id=recovery_range_id
)
print("Recovery Range Details:")
print(get_recovery_range_response.result)

```
{: codeblock}
{: python}



### Update Recovery Range
{: #python-examples-update-recovery-range}

```python
# Config
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"
recovery_range_id = "<RECOVERY_RANGE_ID>"

# Setup authenticator and client
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

recovery_range_patch_model = {}
recovery_range_patch_model['retention'] = {"delete_after_days": 99}

patch_response = rc_client.patch_source_resource_recovery_range(
    backup_vault_name=backup_vault_name,
    recovery_range_id=recovery_range_id,
    recovery_range_patch=recovery_range_patch_model
)
print("Patch Response Details:")
print(patch_response)

```
{: codeblock}
{: python}



### Initiating a Restore
{: #python-examples-initiate-restore}

```python
# Configuration
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"
target_bucket_crn = "<TARGET_BUCKET_CRN>"
recovery_range_id = "<RECOVERY_RANGE_ID>"
restore_point_in_time = "<RESTORE_POINT_IN_TIME>"

# Setup authenticator and clients
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Initiate restore
create_restore = rc_client.create_restore(
    backup_vault_name=backup_vault_name,
    recovery_range_id=recovery_range_id,
    restore_type="in_place",
    restore_point_in_time=restore_point_in_time,
    target_resource_crn=target_bucket_crn
)
print(f"Restore initiated : {create_restore}")

```
{: codeblock}
{: python}



### Listing Restore
{: #python-examples-list-restore}

```python
# Config
api_key = "<API_KEY>"
backup_vault_name = "<BACKUP_VAULT_NAME>"

# Setup authenticator and clients
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# List restore operations
get_store = rc_client.get_restore(
    backup_vault_name=backup_vault_name)

print("Restore response:")
print(get_store.result)

```
{: codeblock}
{: python}



### Get Restore Details
{: #python-examples-get-restore}

```python
# Config
api_key = "<API_KEY>"
source_bucket_name = "<SOURCE_BUCKET_NAME>"
backup_vault_crn = "<BACKUP_VAULT_CRN>"
backup_vault_name = "<BACKUP_VAULT_NAME>"
target_bucket_crn = "<TARGET_BUCKET_CRN>"
recovery_range_id = "<RECOVERY_RANGE_ID>"
restore_point_in_time = "<RESTORE_POINT_IN_TIME>"

# Setup authenticator and clients
authenticator = IAMAuthenticator(apikey=api_key)
rc_client = ResourceConfigurationV1(authenticator=authenticator)

# Create restore
create_restore = rc_client.create_restore(
    backup_vault_name=backup_vault_name,
    recovery_range_id=recovery_range_id,
    restore_type="in_place",
    restore_point_in_time=restore_point_in_time,
    target_resource_crn=target_bucket_crn
)

restore_id = create_restore.result["restore_id"]

# List restore operations
get_store = rc_client.get_restore(
    backup_vault_name=backup_vault_name, restore_id=restore_id)

print("Restore response:")
print(get_store.result)

```
{: codeblock}
{: python}



## Using Key Protect
{: #python-examples-kp}

Key Protect can be added to a storage bucket to encrypt sensitive data at rest in the cloud.

### Before You Begin
{: #python-examples-kp-prereqs}

The following items are necessary in order to create a bucket with Key-Protect enabled:

* A Key Protect service [provisioned](/docs/key-protect?topic=key-protect-provision)
* A Root key available (either [generated](/docs/key-protect?topic=key-protect-create-root-keys) or [imported](/docs/key-protect?topic=key-protect-import-root-keys))

### Retrieving the Root Key CRN
{: #python-examples-kp-root}

1. Retrieve the [instance ID](/docs/key-protect?topic=key-protect-retrieve-instance-ID) for your Key Protect service
1. Use the [Key Protect API](/docs/key-protect?topic=key-protect-set-up-api) to retrieve all your [available keys](https://cloud.ibm.com/apidocs/key-protect)
    * You can either use `curl` commands or an API REST Client such as [Postman](/docs/cloud-object-storage?topic=cloud-object-storage-postman) to access the [Key Protect API](/docs/key-protect?topic=key-protect-set-up-api).
1. Retrieve the CRN of the root key you use to enabled Key Protect on your bucket. The CRN looks similar to below:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creating a bucket with key-protect enabled
{: #python-examples-kp-new-bucket}

```python
COS_KP_ALGORITHM = "<algorithm>"
COS_KP_ROOTKEY_CRN = "<root-key-crn>"

# Create a new bucket with key protect (encryption)
def create_bucket_kp(bucket_name):
    print("Creating new encrypted bucket: {0}".format(bucket_name))
    try:
        cos_client.create_bucket(
            Bucket=bucket_name,
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
{: codeblock}
{: python}

#### Key Values
{: #create-bucket-kp-key-values}

* `<algorithm>` - The encryption algorithm that is used for new objects added to the bucket (Default is AES256).
* `<root-key-crn>` - CRN of the Root Key that is obtained from the Key Protect service.

#### SDK References
{: #create-bucket-kp-sdk-refs}

Methods

* [`create_bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_bucket){: external}

## Using Aspera High-Speed Transfer
**Legacy Notice**: Support for Aspera is considered legacy. Users are recommended to use Aspera Transfer SDK[https://developer.ibm.com/apis/catalog/aspera--aspera-transfer-sdk/API%20Reference].

{: #python-examples-aspera}

**Legacy Notice**: Support for Aspera is considered legacy. Users are recommended to use [Aspera Transfer SDK](https://developer.ibm.com/apis/catalog/aspera--aspera-transfer-sdk){: external}.

By installing the [Aspera high-speed transfer library](/docs/cloud-object-storage?topic=cloud-object-storage-aspera#aspera-packaging), you can use high-speed file transfers within your application. The Aspera library is closed-source, and thus an optional dependency for the COS SDK (which uses an Apache license).

Each Aspera session creates an individual `ascp` process that runs on the client machine to perform the transfer. Ensure that your computing environment can allow this process to run.
{: tip}

### Initializing the AsperaTransferManager
{: #python-examples-aspera-init}

Before initializing the `AsperaTransferManager`, make sure that you have a working [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){: external} (not a `resource` or `session`) object.
{: important}

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_s3transfer.aspera.manager import AsperaTransferManager

COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>"
COS_RESOURCE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location>"

# Create resource
cos_client = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

transfer_manager = AsperaTransferManager(cos)
```
{: codeblock}
{: python}

You need to provide an IAM API Key for Aspera high-speed transfers. [HMAC Credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){: external} are **NOT** currently supported. For more information on IAM, [click here](/docs/cloud-object-storage?topic=cloud-object-storage-iam-overview).
{: tip}

To get the highest throughput, split the transfer into a specified number of parallel **sessions** that send chunks of data whose size is defined by a **threshold** value.

The typical configuration for using multi-session should be:

* 2500 Mbps target rate
* 100 MB threshold (*this is the recommended value for most applications*)

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
{: codeblock}
{: python}

In the above example, the sdk spawns enough sessions to attempt to reach the target rate of 2500 Mbps.

Session management can also be explicitly configured in the SDK. This is useful in cases where more precise control over network utilization is wanted.

The typical configuration for using explicit multi-session should be:

* 2 or 10 sessions
* 100 MB threshold (*this is the recommended value for most applications*)

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
{: codeblock}
{: python}

For best performance in most scenarios, always make use of multiple sessions to minimize any processing that is associated with instantiating an Aspera high-speed transfer. **If your network capacity is at least 1 Gbps, you should use 10 sessions.**  Lower bandwidth networks should use two sessions.
{:tip}

### File Upload
{: #python-examples-aspera-upload}

```python
bucket_name = "<bucket-name>"
upload_filename = "<absolute-path-to-file>"
object_name = "<item-name>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload(upload_filename, bucket_name, object_name)

    # Wait for upload to complete
    future.result()
```
{: codeblock}
{: python}

#### Key Values
{: #aspera-transfer-key-values}

* `<bucket-name>` - name of the target bucket
* `<absolute-path-to-file>` - directory path and file name to the file to be uploaded
* `<item-name>` - name of the new file added to the bucket

### File Download
{: #python-examples-aspera-download}

```python
bucket_name = "<bucket-name>"
download_filename = "<absolute-path-to-file>"
object_name = "<object-to-download>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download(bucket_name, object_name, download_filename)

    # Wait for download to complete
    future.result()
```
{: codeblock}
{: python}

#### Key Values
{: #file-download-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled.
* `<absolute-path-to-file>` - directory and file name where save the file to the local system.
* `<object-to-download>` - name of the file in the bucket to download.

### Directory Upload
{: #python-examples-aspera-directory-upload}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY, and have objects in it.
local_upload_directory = "<absolute-path-to-directory>"
# THIS SHOULD NOT HAVE A LEADING "/"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory)

    # Wait for upload to complete
    future.result()
```
{: codeblock}
{: python}

#### Key Values
{: #dir-upload-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<absolute-path-to-directory>` - local directory that contains the files to be uploaded. Must have leading and trailing `/` (that is, `/Users/testuser/Documents/Upload/`)
* `<object prefix>` - name of the directory in the bucket to store the files. Must not have a leading slash `/` (that is, `newuploads/`)

### Directory Download
{: #python-examples-aspera-directory-download}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory)

    # Wait for download to complete
    future.result()
```
{: codeblock}
{: python}

#### Key Values
{: #dir-download-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<absolute-path-to-directory>` - local directory to save the downloaded files. Must have leading and trailing slash `/` (that is `/Users/testuser/Downloads/`)
* `<object prefix>` - name of the directory in the bucket to store the files. Must not have a leading slash `/` (that is, `todownload/`)

### Using Subscribers
{: #python-examples-aspera-subscribers}

Subscribers provide observability into transfers by attaching custom callback methods. All transfers transition between the following phases:

`Queued - In Progress - Done`

There are three available subscribers for each phase:

* `CallbackOnQueued()` - called when a new transfer has been added to the `AsperaTransferManager`
* `CallbackOnProgress()` - called when a transfer has transmitted data (fired repeatedly while the transfer is in progress).
* `CallbackOnDone()` - called once the transfer is completed

```python
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

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
{: codeblock}
{: python}

#### Key Values
{: #using-subscribers-key-values}

* `<bucket-name>` - name of the bucket in your Object Storage service instance that has Aspera enabled
* `<absolute-path-to-directory>` - local directory to save the downloaded files. Must have leading and trailing slash `/` (that is, `/Users/testuser/Downloads/`)
* `<object prefix>` - name of the directory in the bucket to store the files. Must not have a leading slash `/` (that is, `todownload/`)

The sample code above produces the following output:

``` sh
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### Pause/Resume/Cancel
{: #python-examples-aspera-pause}

The SDK provides the ability to manage the progress of file/directory transfers through the following methods of the `AsperaTransferFuture` object:

* `pause()`
* `resume()`
* `cancel()`

There are no side-effects from calling either of the methods outlined above. Proper clean up and housekeeping is handled by the SDK.
{: tip}

```python
# Create Transfer manager
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

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
{: codeblock}
{: python}

### Troubleshooting Aspera Issues
{: #python-examples-aspera-ts}

**Issue:** Developers using any version of Python besides 3.6 may experience failures when installing or using Aspera SDK.

**Cause:** If there are different versions of Python installed on your environment, then you might encounter installation failures when you try to install the Aspera SDK. This can be caused by a missing DLL files or wrong DLL in path.

**Solution:** The first step to resolving this issue would be to reinstall the Aspera libraries. There might have been a failure during the installation. As a result this might have affected the DLL files. If that does not resolve the issues, then you will be required to update your version of Python. If you are unable to do this, then you can use installation [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){: external}. This should allow you to install the Aspera SDK on Python 3.6.x without any issues.

## Updating metadata
{: #python-examples-metadata}

There are two ways to update the metadata on an existing object:

* A `PUT` request with the new metadata and the original object contents
* Running a `COPY` request with the new metadata specifying the original object as the copy source

### Using PUT to update metadata
{: #python-examples-metadata-put}

**Note:** The `PUT` request overwrites the existing contents of the object so it must first be downloaded and re-uploaded with the new metadata.

```python
def update_metadata_put(bucket_name, item_name, key, value):
    try:
        # retrieve the existing item to reload the contents
        response = cos_client.get_object(Bucket=bucket_name, Key=item_name)
        existing_body = response.get("Body").read()

        # set the new metadata
        new_metadata = {
            key: value
        }

        cos_client.put_object(Bucket=bucket_name, Key=item_name, Body=existing_body, Metadata=new_metadata)

        print("Metadata update (PUT) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```
{: codeblock}
{: python}

### Using COPY to update metadata
{: #python-examples-metadata-copy}

```python
def update_metadata_copy(bucket_name, item_name, key, value):
    try:
        # set the new metadata
        new_metadata = {
            key: value
        }

        # set the copy source to itself
        copy_source = {
            "Bucket": bucket_name,
            "Key": item_name
        }

        cos_client.copy_object(Bucket=bucket_name, Key=item_name, CopySource=copy_source, Metadata=new_metadata, MetadataDirective="REPLACE")

        print("Metadata update (COPY) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```
{: codeblock}
{: python}

## Using Immutable Object Storage
{: #python-examples-immutable}

### Add a protection configuration to an existing bucket
{: #python-examples-immutable-add}

Objects written to a protected bucket cannot be deleted until the protection period has expired and all legal holds on the object are removed. The bucket's default retention value is given to an object unless an object-specific value is provided when the object is created. Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

The minimum and maximum supported values for the retention period settings `MinimumRetention`, `DefaultRetention`, and `MaximumRetention` are a minimum of 0 days and a maximum of 365243 days (1000 years).

```py
def add_protection_configuration_to_bucket(bucket_name):
    try:
        new_protection_config = {
            "Status": "Retention",
            "MinimumRetention": {"Days": 10},
            "DefaultRetention": {"Days": 100},
            "MaximumRetention": {"Days": 1000}
        }

        cos_client.put_bucket_protection_configuration(Bucket=bucket_name, ProtectionConfiguration=new_protection_config)

        print("Protection added to bucket {0}\n".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to set bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

### Check protection on a bucket
{: #python-examples-immutable-check}

```py
def get_protection_configuration_on_bucket(bucket_name):
    try:
        response = cos_client.get_bucket_protection_configuration(Bucket=bucket_name)
        protection_config = response.get("ProtectionConfiguration")

        print("Bucket protection config for {0}\n".format(bucket_name))
        print(protection_config)
        print("\n")
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to get bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

### Upload a protected object
{: #python-examples-immutable-upload}

Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.


|Value	| Type	| Description |
| --- | --- | --- |
|`Retention-Period` | Non-negative integer (seconds) | Retention period to store on the object in seconds. The object can be neither overwritten nor deleted until the amount of time that is specified in the retention period has elapsed. If this field and `Retention-Expiration-Date` are specified a `400`  error is returned. If neither is specified the bucket's `DefaultRetention` period will be used. Zero (`0`) is a legal value assuming the bucket's minimum retention period is also `0`. |
| `Retention-expiration-date` | Date (ISO 8601 Format) | Date on which it will be legal to delete or modify the object. You can only specify this or the Retention-Period header. If both are specified a `400`  error will be returned. If neither is specified the bucket's DefaultRetention period will be used. |
| `Retention-legal-hold-id` | string | A single legal hold to apply to the object. A legal hold is a Y character long string. The object cannot be overwritten or deleted until all legal holds associated with the object are removed. |

```py
def put_object_add_legal_hold(bucket_name, object_name, file_text, legal_hold_id):
    print("Add legal hold {0} to {1} in bucket {2} with a putObject operation.\n".format(legal_hold_id, object_name, bucket_name))
    cos_client.put_object(
        Bucket=bucket_name,
        Key=object_name,
        Body=file_text,
        RetentionLegalHoldId=legal_hold_id)
    print("Legal hold {0} added to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

def copy_protected_object(source_bucket_name, source_object_name, destination_bucket_name, new_object_name):
    print("Copy protected object {0} from bucket {1} to {2}/{3}.\n".format(source_object_name, source_bucket_name, destination_bucket_name, new_object_name))

    copy_source = {
        "Bucket": source_bucket_name,
        "Key": source_object_name
    }

    cos_client.copy_object(
        Bucket=destination_bucket_name,
        Key=new_object_name,
        CopySource=copy_source,
        RetentionDirective="Copy"
    )

    print("Protected object copied from {0}/{1} to {2}/{3}\n".format(source_bucket_name, source_object_name, destination_bucket_name, new_object_name));

def complete_multipart_upload_with_retention(bucket_name, object_name, upload_id, retention_period):
    print("Completing multi-part upload for object {0} in bucket {1}\n".format(object_name, bucket_name))
    cos_client.complete_multipart_upload(
        Bucket=bucket_name,
        Key=object_name,
        MultipartUpload={
            "Parts":[{
                "ETag": part["ETag"],
                "PartNumber": 1
            }]
        },
        UploadId=upload_id,
        RetentionPeriod=retention_period
    )

    print("Multi-part upload completed for object {0} in bucket {1}\n".format(object_name, bucket_name))

def upload_file_with_retention(bucket_name, object_name, path_to_file, retention_period):
    print("Uploading file {0} to object {1} in bucket {2}\n".format(path_to_file, object_name, bucket_name))

    args = {
        "RetentionPeriod": retention_period
    }

    cos_client.upload_file(
        Filename=path_to_file,
        Bucket=bucket_name,
        Key=object_name,
        ExtraArgs=args
    )

    print("File upload complete to object {0} in bucket {1}\n".format(object_name, bucket_name))
```
{: codeblock}
{: python}

### Add or remove a legal hold to or from a protected object
{: #python-examples-immutable-legal-hold}

The object can support 100 legal holds:

* A legal hold identifier is a string of maximum length 64 characters and a minimum length of 1 character. Valid characters are letters, numbers, and the symbols `!`, `_`, `.`, `*`, `(`, `)`, and `-`.
* If the addition of the given legal hold exceeds 100 total legal holds on the object, the new legal hold will not be added, a `400` error is returned.
* If an identifier is too long, it will not be added to the object and a `400` error is returned.
* If an identifier contains invalid characters, it will not be added to the object and a `400` error is returned.
* If an identifier is already in use on an object, the existing legal hold is not modified and the response indicates that the identifier was already in use with a `409` error.
* If an object does not have retention period metadata, a `400` error is returned and adding or removing a legal hold is not allowed.

To add or remove a legal hold, you must have `Manager` permissions for this bucket.

```py
def add_legal_hold_to_object(bucket_name, object_name, legal_hold_id):
    print("Adding legal hold {0} to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos_client.add_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} added to object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))

def delete_legal_hold_from_object(bucket_name, object_name, legal_hold_id):
    print("Deleting legal hold {0} from object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos_client.delete_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} deleted from object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))
```
{: codeblock}
{: python}

### Extend the retention period of a protected object
{: #python-examples-immutable-extend}

The retention period of an object can only be extended. It cannot be decreased from the currently configured value.

The retention expansion value is set in one of three ways:

* additional time from the current value (`Additional-Retention-Period` or similar method)
* new extension period in seconds (`Extend-Retention-From-Current-Time` or similar method)
* new retention expiry date of the object (`New-Retention-Expiration-Date` or similar method)

The current retention period that is stored in the object metadata is either increased by the given more time or replaced with the new value, depending on the parameter that is set in the `extendRetention` request. In all cases, the extend retention parameter is checked against the current retention period and the extended parameter is only accepted if the updated retention period is greater than the current retention period.

Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

```py
def extend_retention_period_on_object(bucket_name, object_name, additional_seconds):
    print("Extend the retention period on {0} in bucket {1} by {2} seconds.\n".format(object_name, bucket_name, additional_seconds))

    cos_client.extend_object_retention(
        Bucket=bucket_ame,
        Key=object_name,
        AdditionalRetentionPeriod=additional_seconds
    )

    print("New retention period on {0} is {1}\n".format(object_name, additional_seconds))
```
{: codeblock}
{: python}

### List legal holds on a protected object
{: #python-examples-immutable-list-holds}

This operation returns:

* Object creation date
* Object retention period in seconds
* Calculated retention expiration date based on the period and creation date
* List of legal holds
* Legal hold identifier
* Timestamp when legal hold was applied

If there are no legal holds on the object, an empty `LegalHoldSet` is returned.
If there is no retention period that is specified on the object, a `404` error is returned.


```py
def list_legal_holds_on_object(bucket_name, object_name):
    print("List all legal holds on object {0} in bucket {1}\n".format(object_name, bucket_name));

    response = cos_client.list_legal_holds(
        Bucket=bucket_name,
        Key=object_name
    )

    print("Legal holds on bucket {0}: {1}\n".format(bucket_name, response))
```
{: codeblock}
{: python}

### Create a hosted static website
{: #python-examples-hosted-static-website-create}

This operation requires permissions, as only the bucket owner is typically permitted to configure a bucket to host a static website. The parameters determine the default suffix for visitors to the site as well as an optional error document.

```py
def putBucketWebsiteConfiguration(bucket_name):
    website_defaults = {
        'ErrorDocument': {'Key': 'error.html'},
        'IndexDocument': {'Suffix': 'index.html'},
    }
    cos_client.put_bucket_website(Bucket=bucket_name, WebsiteConfiguration=website_defaults)
    print("Website configuration set on bucket {0}\n".format(bucket_name))
```
{: codeblock}
{: python}

## Next Steps
{: #python-guide-next-steps}

For more information, the source code can be found at [GitHub](https://github.com/ibm/ibm-cos-sdk-python/){: external}.
