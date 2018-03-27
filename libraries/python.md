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

# Python

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

## Example script

This script assumes that a service credential is stored in JSON format in the same directory.  The credential must have the following fields:

### Credential
```json
{
  "apikey": "<api-key>",
  "endpoints": "https://cos-service.bluemix.net/endpoints",
  "resource_instance_id": "<resource-instance-id>"
}
```

### Script
```python
import ibm_boto3
import json
import requests
import random
from ibm_botocore.client import Config
from pprint import pprint

with open('./credentials.json') as data_file:
    credentials = json.load(data_file)

print("Service credential:")
print(json.dumps(credentials, indent=2))
print("")
print("Connecting to COS...")

# Rquest detailed enpoint list
endpoints = requests.get(credentials.get('endpoints')).json()
#import pdb; pdb.set_trace()

# Obtain iam and cos host from the the detailed endpoints
iam_host = (endpoints['identity-endpoints']['iam-token'])
cos_host = (endpoints['service-endpoints']['cross-region']['us']['public']['us-geo'])

api_key = credentials.get('apikey')
service_instance_id = credentials.get('resource_instance_id')

# Constrict auth and cos endpoint
auth_endpoint = "https://" + iam_host + "/oidc/token"
service_endpoint = "https://" + cos_host

print("Creating client...")
# Get bucket list
cos = ibm_boto3.client('s3',
                    ibm_api_key_id=api_key,
                    ibm_service_instance_id=service_instance_id,
                    ibm_auth_endpoint=auth_endpoint,
                    config=Config(signature_version='oauth'),
                    endpoint_url=service_endpoint)


# Call COS to list current buckets
response = cos.list_buckets()

# Get a list of all bucket names from the response
buckets = [bucket['Name'] for bucket in response['Buckets']]

# Print out the bucket list
print("Current Bucket List:")
print(json.dumps(buckets, indent=2))
print("---")
result = [bucket for bucket in buckets if 'cos-bucket-sample-' in bucket]

print("Creating a new bucket and uploading an object...")
if len(result) == 0 :
   bucket_name = 'cos-bucket-sample-' + str(random.randint(100,99999999));
   # Create a bucket
   cos.create_bucket(Bucket=bucket_name)
   # Upload a file
   cos.upload_file('./example.py', bucket_name, 'example-object')

   # Call COS to list current buckets
   response = cos.list_buckets()

   # Get a list of all bucket names from the response
   buckets = [bucket['Name'] for bucket in response['Buckets']]

   # Print out the bucket list
   print("New Bucket List:")
   print(json.dumps(buckets, indent=2))
   print("---")
else :
   bucket_name = result[0];


# Call COS to list current objects
response = cos.list_objects(Bucket=bucket_name)

# Get a list of all object names from the response
objects = [object['Key'] for object in response['Contents']]

# Print out the object list
print("Objects in %s:" % bucket_name)
print(json.dumps(objects, indent=2))
```

## Using Key Protect

```python
response = client.create_bucket(
    ACL= 'public-read',
    Bucket='string',
    CreateBucketConfiguration={
        'LocationConstraint': 'us-standard'
    },
    GrantRead='string',
    GrantReadACP='string',
    IBMServiceInstanceId='string',
    IBMSSEKPEncryptionAlgorithm='string',
    IBMSSEKPCustomerRootKeyCrn='string'
)
```
