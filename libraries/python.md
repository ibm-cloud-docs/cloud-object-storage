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

The fork of the `boto3` library provides complete access to the COS API.  Endpoints, an API key, and the instance ID must be specified when creating a service resource or low-level client as shown in the following basic examples.

Detailed documentation can be found at [here](https://ibm.github.io/ibm-cos-sdk-python/).

## Example script

Creating a service resource provides greater abstraction for higher level tasks.  This is a basic script that fetches the list of buckets owned by an account, and lists the objects in each bucket.

```python
import boto3
import json
import requests
import random
from botocore.client import Config

# the following values are found by generating a service credential in the console
cos_instance_connection_info = {
  "apikey": "<api-key>",
  "endpoints": "https://cos-service.bluemix.net/endpoints",
  "resource_instance_id": "<resource-instance-id>"
}



# Rquest detailed enpoint list
endpoints = requests.get(cos_instance_connection_info.get('endpoints')).json()
#import pdb; pdb.set_trace()

# Obtain iam and cos host from the the detailed endpoints
iam_host = (endpoints['identity-endpoints']['iam-token'])
cos_host = (endpoints['service-endpoints']['cross-region']['us']['public']['us-geo'])

api_key = cos_instance_connection_info.get('apikey')
service_instance_id = cos_instance_connection_info.get('resource_instance_id')

# Constrict auth and cos endpoint
auth_endpoint = "https://" + iam_host + "/oidc/token"
service_endpoint = "https://" + cos_host


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
print("Current Bucket List: %s" % buckets)

result = [bucket for bucket in buckets if 'cos-bucket-sample-' in bucket]

if len(result) == 0 :
   bucket_name = 'cos-bucket-sample-' + str(random.randint(100,99999999));
   # Create a bucket
   cos.create_bucket(Bucket=bucket_name)
   # Upload a file
   cos.upload_file('./cos-sample.py', bucket_name, 'cos-sampple.py')

   # Call S3 to list current buckets
   response = cos.list_buckets()

   # Get a list of all bucket names from the response
   buckets = [bucket['Name'] for bucket in response['Buckets']]

   # Print out the bucket list
   print("New Bucket List: %s" % buckets)
else :
   bucket_name = result[0];


# Call S3 to list current objects
response = cos.list_objects(Bucket=bucket_name)

# Get a list of all object names from the response
objects = [object['Key'] for object in response['Contents']]

# Print out the object list
print("Bucket Name: %s" % bucket_name, "Object List: %s" % objects)
```
