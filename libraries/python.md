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

Python support is provided through a fork of the Boto 3 library.  It can be installed from the Python Package Index via `pip install ibm-cos-sdk`.

Source code can be found at [GitHub](https://github.com/ibm/ibm-cos-sdk-python/).

The fork of the `boto3` library provides complete access to the {{site.data.keyword.cos_full}} API.  Endpoints, an API key, and the instance ID must be specified when creating a service resource or low-level client as shown in the following basic examples.

Detailed documentation can be found at [here](https://ibm.github.io/ibm-cos-sdk-python/).

## Example script

This script assumes that a service credential has been created and stored in JSON format in the same directory.  The credential must have the following fields at minimum:

### Credential
```json
{
  "apikey": "<api-key>",
  "endpoints": "https://cos-service.bluemix.net/endpoints",
  "resource_instance_id": "<resource-instance-id"
}
```

### Script
```python
import boto3
import json
import requests
import random
from botocore.client import Config
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
cos = boto3.client('s3',
                    ibm_api_key_id=api_key,
                    ibm_service_instance_id=service_instance_id,
                    ibm_auth_endpoint=auth_endpoint,
                    config=Config(signature_version='oauth'),
                    endpoint_url=service_endpoint)


# Call S3 to list current buckets
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

   # Call S3 to list current buckets
   response = cos.list_buckets()

   # Get a list of all bucket names from the response
   buckets = [bucket['Name'] for bucket in response['Buckets']]

   # Print out the bucket list
   print("New Bucket List:")
   print(json.dumps(buckets, indent=2))
   print("---")
else :
   bucket_name = result[0];


# Call S3 to list current objects
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
