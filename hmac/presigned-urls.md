---

copyright:
  years: 2017, 2018
lastupdated: "2018-03-16"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Create a presigned URL

Presigned URLs create a temporary link that can be used to share an object publicly, or to [provide a temporary target for sending a PUT request](https://medium.com/ibm-watson-data-lab/keeping-your-secrets-between-cloud-object-storage-and-your-browser-part-1-68f4b83bbd38) without needing to provide authentication information.



The easiest way to create presigned URLs is using the [AWS CLI](/docs/services/cloud-object-storage/cli/aws-cli.html):

```bash
$ aws --endpoint-url=https://{endpoint} s3 presign s3://bucket-1/new-file
```

It is also possible to set an expiration time for the URL in seconds (default is 3600):

```bash
$ aws --endpoint-url=https://{endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

It is also possible to construct them programatically.  Here are examples for basic `PUT` and `GET` operations written in Python.

## Create a presigned URL to download an object

```python
import datetime
import hashlib
import hmac
import requests
from requests.utils import quote

access_key = '{COS_HMAC_ACCESS_KEY_ID}'
secret_key = '{COS_HMAC_SECRET_ACCESS_KEY}'

# request elements
http_method = 'GET'
region = ''
bucket = 'example-bucket'
cos_endpoint = '{endpoint}'
host = cos_endpoint
endpoint = 'https://' + host
object_key = 'example-object'
expiration = 86400  # time in seconds


# hashing methods
def hash(key, msg):
    return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).digest()


# region is a wildcard value that takes the place of the AWS region value
# as COS doen't use regions like AWS, this parameter can accept any string
def createSignatureKey(key, datestamp, region, service):
    keyDate = hash(('AWS4' + key).encode('utf-8'), datestamp)
    keyRegion = hash(keyDate, region)
    keyService = hash(keyRegion, service)
    keySigning = hash(keyService, 'aws4_request')
    return keySigning


# assemble the standardized request
time = datetime.datetime.utcnow()
timestamp = time.strftime('%Y%m%dT%H%M%SZ')
datestamp = time.strftime('%Y%m%d')

standardized_querystring = ( 'X-Amz-Algorithm=AWS4-HMAC-SHA256' +
                             '&X-Amz-Credential=' + access_key + '/' + datestamp + '/' + region + '/s3/aws4_request' +
                             '&X-Amz-Date=' + timestamp +
                             '&X-Amz-Expires=' + str(expiration) +
                             '&X-Amz-SignedHeaders=host' )
standardized_querystring_url_encoded = quote(standardized_querystring, safe='&=')

standardized_resource = '/' + bucket + '/' + object_key
standardized_resource_url_encoded = quote(standardized_resource, safe='&')

payload_hash = 'UNSIGNED-PAYLOAD'
standardized_headers = 'host:' + host
signed_headers = 'host'

standardized_request = (http_method + '\n' +
                        standardized_resource + '\n' +
                        standardized_querystring_url_encoded + '\n' +
                        standardized_headers + '\n' +
                        '\n' +
                        signed_headers + '\n' +
                        payload_hash)

# assemble string-to-sign
hashing_algorithm = 'AWS4-HMAC-SHA256'
credential_scope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request'
sts = ( hashing_algorithm + '\n' +
        timestamp + '\n' +
        credential_scope + '\n' +
        hashlib.sha256(standardized_request).hexdigest() )

# generate the signature
signature_key = createSignatureKey(secret_key, datestamp, region, 's3')
signature = hmac.new(signature_key,
                     (sts).encode('utf-8'),
                     hashlib.sha256).hexdigest()

# create and send the request
# the 'requests' package autmatically adds the required 'host' header
request_url = ( endpoint + '/' +
                bucket + '/' +
                object_key + '?' +
                standardized_querystring_url_encoded +
                '&X-Amz-Signature=' +
                signature )

print 'request_url: %s' % request_url

print '\nSending `%s` request to IBM COS -----------------------' % http_method
print 'Request URL = ' + request_url
request = requests.get(request_url)

print '\nResponse from IBM COS ---------------------------------'
print 'Response code: %d\n' % request.status_code
print request.text


# this information can be helpful in troubleshooting, or to better
# understand what goes into signature creation
print 'These are the values used to construct this request.'
print 'Request details -----------------------------------------'
print 'http_method: %s' % http_method
print 'host: %s' % host
print 'region: %s' % region
print 'endpoint: %s' % endpoint
print 'bucket: %s' % bucket
print 'object_key: %s' % object_key
print 'timestamp: %s' % timestamp
print 'datestamp: %s' % datestamp

print 'Standardized request details ----------------------------'
print 'standardized_resource: %s' % standardized_resource_url_encoded
print 'standardized_querystring: %s' % standardized_querystring_url_encoded
print 'standardized_headers: %s' % standardized_headers
print 'signed_headers: %s' % signed_headers
print 'payload_hash: %s' % payload_hash
print 'standardized_request: %s' % standardized_request

print 'String-to-sign details ----------------------------------'
print 'credential_scope: %s' % credential_scope
print 'string-to-sign: %s' % sts
print 'signature_key: %s' % signature_key
print 'signature: %s' % signature

print 'Because the signature key has non-ASCII characters, it is'
print 'necessary to create a hexadecimal digest for the purposes'
print 'of checking against this example.'

def hex_hash(key, msg):
    return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).hexdigest()
def createHexSignatureKey(key, datestamp, region, service):
    keyDate = hex_hash(('AWS4' + key).encode('utf-8'), datestamp)
    keyRegion = hex_hash(keyDate, region)
    keyService = hex_hash(keyRegion, service)
    keySigning = hex_hash(keyService, 'aws4_request')
    return keySigning

signature_key_hex = createHexSignatureKey(secret_key, datestamp, region, 's3')

print 'signature_key (hexidecimal): %s' % signature_key_hex

print 'Header details ------------------------------------------'
print 'pre-signed url: %s' % request_url
```

## Create a presigned URL to upload an object

```python
import datetime
import hashlib
import hmac
import requests
from requests.utils import quote

access_key = '{COS_HMAC_ACCESS_KEY_ID}'
secret_key = '{COS_HMAC_SECRET_ACCESS_KEY}'

# request elements
http_method = 'PUT'
region = ''
bucket = 'example-bucket'
cos_endpoint = '{endpoint}'
host = cos_endpoint
endpoint = 'https://' + host
object_key = 'example-object'
expiration = 3600  # time in seconds


# hashing methods
def hash(key, msg):
    return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).digest()


# region is a wildcard value that takes the place of the AWS region value
# as COS doen't use regions like AWS, this parameter can accept any string
def createSignatureKey(key, datestamp, region, service):
    keyDate = hash(('AWS4' + key).encode('utf-8'), datestamp)
    keyRegion = hash(keyDate, region)
    keyService = hash(keyRegion, service)
    keySigning = hash(keyService, 'aws4_request')
    return keySigning


# assemble the standardized request
time = datetime.datetime.utcnow()
timestamp = time.strftime('%Y%m%dT%H%M%SZ')
datestamp = time.strftime('%Y%m%d')

standardized_querystring = ('X-Amz-Algorithm=AWS4-HMAC-SHA256' +
                            '&X-Amz-Credential=' + access_key + '/' + datestamp + '/' + region + '/s3/aws4_request' +
                            '&X-Amz-Date=' + timestamp +
                            '&X-Amz-Expires=' + str(expiration) +
                            '&X-Amz-SignedHeaders=host')
standardized_querystring_url_encoded = quote(standardized_querystring, safe='&=')

standardized_resource = '/' + bucket + '/' + object_key
standardized_resource_url_encoded = quote(standardized_resource, safe='&')

payload_hash = 'UNSIGNED-PAYLOAD'
standardized_headers = 'host:' + host + '\n'
signed_headers = 'host'

standardized_request = (http_method + '\n' +
                        standardized_resource + '\n' +
                        standardized_querystring_url_encoded + '\n' +
                        standardized_headers + '\n' +
                        signed_headers + '\n' +
                        payload_hash)

# assemble string-to-sign
hashing_algorithm = 'AWS4-HMAC-SHA256'
credential_scope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request'
sts = (hashing_algorithm + '\n' +
       timestamp + '\n' +
       credential_scope + '\n' +
       hashlib.sha256(standardized_request).hexdigest())

# generate the signature
signature_key = createSignatureKey(secret_key, datestamp, region, 's3')
signature = hmac.new(signature_key,
                     (sts).encode('utf-8'),
                     hashlib.sha256).hexdigest()

# create and send the request
# the 'requests' package autmatically adds the required 'host' header
request_url = (endpoint + '/' +
               bucket + '/' +
               object_key + '?' +
               standardized_querystring_url_encoded +
               '&X-Amz-Signature=' +
               signature)

print 'request_url: %s' % request_url

print '\nSending `%s` request to IBM COS -----------------------' % http_method
print 'Request URL = ' + request_url
request = requests.PUT(request_url)

print '\nResponse from IBM COS ---------------------------------'
print 'Response code: %d\n' % request.status_code
print request.text


# this information can be helpful in troubleshooting, or to better
# understand what goes into signature creation
print 'These are the values used to construct this request.'
print 'Request details -----------------------------------------'
print 'http_method: %s' % http_method
print 'host: %s' % host
print 'region: %s' % region
print 'endpoint: %s' % endpoint
print 'bucket: %s' % bucket
print 'object_key: %s' % object_key
print 'timestamp: %s' % timestamp
print 'datestamp: %s' % datestamp

print 'Standardized request details ----------------------------'
print 'standardized_resource: %s' % standardized_resource_url_encoded
print 'standardized_querystring: %s' % standardized_querystring_url_encoded
print 'standardized_headers: %s' % standardized_headers
print 'signed_headers: %s' % signed_headers
print 'payload_hash: %s' % payload_hash
print 'standardized_request: %s' % standardized_request

print 'String-to-sign details ----------------------------------'
print 'credential_scope: %s' % credential_scope
print 'string-to-sign: %s' % sts
print 'signature_key: %s' % signature_key
print 'signature: %s' % signature

 print 'Because the signature key has non-ASCII characters, it is'
 print 'necessary to create a hexadecimal digest for the purposes'
 print 'of checking against this example.'

 def hex_hash(key, msg):
     return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).hexdigest()
 def createHexSignatureKey(key, datestamp, region, service):
     keyDate = hex_hash(('AWS4' + key).encode('utf-8'), datestamp)
     keyRegion = hex_hash(keyDate, region)
     keyService = hex_hash(keyRegion, service)
     keySigning = hex_hash(keyService, 'aws4_request')
     return keySigning

 signature_key_hex = createHexSignatureKey(secret_key, datestamp, region, 's3')

 print 'signature_key (hexidecimal): %s' % signature_key_hex

 print 'Header details ------------------------------------------'
 print 'pre-signed url: %s' % request_url
```
