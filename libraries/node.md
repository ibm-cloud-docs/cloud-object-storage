---

copyright:
  years: 2017
lastupdated: "2018-05-24"

---

# Using Node.js

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

## Installing the SDK

The preferred way to install the {{site.data.keyword.cos_full}} SDK for Node.js is to use the
[npm](http://npmjs.org) package manager for Node.js. Simply type the following
into a terminal window:

```sh
npm install ibm-cos-sdk
```

Source code is hosted on [GitHub](https://github.com/IBM/ibm-cos-sdk-js).

More detail on individual methods and classes can be found in [the API documentation for the SDK](https://ibm.github.io/ibm-cos-sdk-js/).

## Getting Started

### Minimum requirements ####

To run the SDK you will need **Node 4.x+**.

### Creating a client and sourcing credentials
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
```javascript
const AWS = require('ibm-cos-sdk');

var config = {
    endpoint: '<endpoint>',
    apiKeyId: '<api-key>',
    ibmAuthEndpoint: 'https://iam.ng.bluemix.net/oidc/token',
    serviceInstanceId: '<resource-instance-id>',
};

var cos = new AWS.S3(config);
```
*Key Values*
* `<endpoint>` - public endpoint for your cloud object storage (available from the [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps))
* `<api-key>` - api key generated when creating the service credentials (write access is required for creation and deletion examples)
* `<resource-instance-id>` - resource ID for your cloud object storage (available through [IBM Cloud CLI](../getting-started-cli.html) or [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps))

### Creating a new bucket
```javascript
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: 'us-standard'
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*SDK References*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property)

### Creating a new text file
```javascript
function createTextFile(bucket, name, fileText) {
    console.log(`Creating new item: ${name}`);
    return cos.putObject({
        Bucket: bucket, 
        Key: name, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${name} created!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property)

### List available buckets
```javascript
function getBuckets() {
    console.log('Retrieving list of buckets');
    return cos.listBuckets()
    .promise()
    .then((data) => {
        if (data.Buckets != null) {
            for (var i = 0; i < data.Buckets.length; i++) {
                console.log(`Bucket Name: ${data.Buckets[i].Name}`);
            }
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property)

### List items in a bucket
```javascript
function getBucketContents(bucket) {
    console.log(`Retrieving bucket contents from: ${bucket}`);
    return cos.listObjects(
        {Bucket: bucket},
    ).promise()
    .then((data) => {
        if (data != null && data.Contents != null) {
            for (var i = 0; i < data.Contents.length; i++) {
                var itemKey = data.Contents[i].Key;
                var itemSize = data.Contents[i].Size;
                console.log(`Item: ${itemKey} (${itemSize} bytes).`)
            }
        }    
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property)

### Get file contents of particular item
```javascript
function getItem(bucket, name) {
    console.log(`Retrieving item from bucket: ${bucket}, key: ${name}`);
    return cos.getObject({
        Bucket: bucket, 
        Key: name
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log('File Contents: ' + Buffer.from(data.Body).toString());
        }    
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property)

### Delete an item from a bucket
```javascript
function deleteItem(bucket, name) {
    console.log(`Deleting item: ${name}`);
    return cos.deleteObject({
        Bucket: bucket,
        Key: name
    }).promise()
    .then(() =>{
        console.log(`Item: ${name} deleted!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*SDK References*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property)

### Delete a bucket
```javascript
function deleteBucket(bucket) {
    console.log(`Deleting bucket: ${bucket}`);
    return cos.deleteBucket({
        Bucket: bucket
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucket} deleted!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property)

### View a bucket's security
```javascript
function getBucketACL(bucket) {
    console.log(`Retrieving ACL for bucket: ${bucket}`);
    return cos.getBucketAcl({
        Bucket: bucket
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log(`Owner: ${data.Owner.DisplayName}`);
            if (data.Grants != null) {
                data.Grants.forEach((grantee) => {
                    console.log(`User: ${grantee.DisplayName} (${grantee.Permission})`)
                })
            }
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [getBucketAcl](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getBucketAcl-property)

### View a file's security
```javascript
function getItemACL(bucket, name) {
    console.log(`Retrieving ACL for ${name} from bucket: ${bucket}`);
    return cos.getObjectAcl({
        Bucket: bucket,
        Key: name
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log(`Owner: ${data.Owner.DisplayName}`);
            if (data.Grants != null) {
                data.Grants.forEach((grantee) => {
                    console.log(`User: ${grantee.DisplayName} (${grantee.Permission})`)
                })
            }
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObjectAcl-property)

## Using Key protect

```javascript
var params = {
  Bucket: 'STRING_VALUE', /* required */
  ACL: private | public-read | public-read-write | authenticated-read,
  CreateBucketConfiguration: {
    LocationConstraint: EU | eu-west-1 | us-west-1 | us-west-2 | ap-south-1 | ap-southeast-1 | ap-southeast-2 | ap-northeast-1 | sa-east-1 | cn-north-1 | eu-central-1
  },
  GrantFullControl: 'STRING_VALUE',
  GrantRead: 'STRING_VALUE',
  GrantReadACP: 'STRING_VALUE',
  GrantWrite: 'STRING_VALUE',
  GrantWriteACP: 'STRING_VALUE',
  IBMServiceInstanceId: 'STRING_VALUE',
  IBMSSEKPEncryptionAlgorithm: 'STRING_VALUE',
  IBMSSEKPCustomerRootKeyCrn: 'STRING_VALUE'
};
s3.createBucket(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```

Parameters (new fields only):
`IBMSSEKPEncryptionAlgorithm` (string) – The encryption algorithm that will be used for objects stored in the newly created bucket.  Default: AES256
`IBMSSEKPCustomerRootKeyCrn` (string) – Container for describing the KMS-KP Key CRN.  The crn includes version, cname, ctype, servicename, location, scope, serviceinstance, resourcetype, resource. We will pass the CRN as-is.

Callback (callback):
function(err, data) { ... }
Called when a response from the service is returned. If a callback is not supplied, you must call AWS.Request.send() on the returned request object to initiate the request.
Parameters:err (Error) —
the error object returned from the request. Set to null if the request is successful.
data (Object) — the de-serialized data returned from the request. Set to null if a request error occurs. The data object has the following properties:
Location — (String)
