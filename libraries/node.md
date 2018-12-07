---

copyright:
  years: 2017, 2018
lastupdated: "2018-12-07"

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
[npm](http://npmjs.org){:new_window} package manager for Node.js. Simply type the following
into a terminal window:

```sh
npm install ibm-cos-sdk
```

Source code is hosted on [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window}.

More detail on individual methods and classes can be found in [the API documentation for the SDK](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}.

## Getting Started

### Minimum requirements ####

To run the SDK you will need **Node 4.x+**.

### Creating a client and sourcing credentials
{: #client-credentials}

To connect to COS, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables (`AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` respectively).

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
* `<endpoint>` - public endpoint for your cloud object storage (available from the [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window})
* `<api-key>` - api key generated when creating the service credentials (write access is required for creation and deletion examples)
* `<resource-instance-id>` - resource ID for your cloud object storage (available through [IBM Cloud CLI](../getting-started-cli.html) or [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window})

### Creating a new bucket
A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage/basics/classes#locationconstraint).

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
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### Creating a new text file
```javascript
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName, 
        Key: itemName, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

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
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### List items in a bucket
```javascript
function getBucketContents(bucketName) {
    console.log(`Retrieving bucket contents from: ${bucketName}`);
    return cos.listObjects(
        {Bucket: bucketName},
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
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### Get file contents of particular item
```javascript
function getItem(bucketName, itemName) {
    console.log(`Retrieving item from bucket: ${bucketName}, key: ${itemName}`);
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
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
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### Delete an item from a bucket
```javascript
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() =>{
        console.log(`Item: ${itemName} deleted!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*SDK References*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### Delete multiple items from a bucket

The delete request can contain a maximum of 1000 keys that you want to delete.  While this is very useful in reducing the per-request overhead, be mindful when deleting a large number of keys.  Also take into account the sizes of the objects to ensure suitable performance.
{:tip}

```javascript
function deleteItems(bucketName) {
    var deleteRequest = {
        "Objects": [
            { "Key": "deletetest/testfile1.txt" },
            { "Key": "deletetest/testfile2.txt" },
            { "Key": "deletetest/testfile3.txt" },
            { "Key": "deletetest/testfile4.txt" },
            { "Key": "deletetest/testfile5.txt" }
        ]        
    }
    return cos.deleteObjects({
        Bucket: bucketName,
        Delete: deleteRequest
    }).promise()
    .then((data) => {
        console.log(`Deleted items for ${bucketName}`);
        console.log(data.Deleted);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });    
}
```

*SDK References*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### Delete a bucket
```javascript
function deleteBucket(bucketName) {
    console.log(`Deleting bucket: ${bucketName}`);
    return cos.deleteBucket({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucketName} deleted!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}

### View a bucket's security
```javascript
function getBucketACL(bucketName) {
    console.log(`Retrieving ACL for bucket: ${bucketName}`);
    return cos.getBucketAcl({
        Bucket: bucketName
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
* [getBucketAcl](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getBucketAcl-property){:new_window}

### View a file's security
```javascript
function getItemACL(bucketName, itemName) {
    console.log(`Retrieving ACL for ${itemName} from bucket: ${bucketName}`);
    return cos.getObjectAcl({
        Bucket: bucketName,
        Key: itemName
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
* [getObjectAcl](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObjectAcl-property){:new_window}

### Execute a multi-part upload
{: #multipart-upload}

```javascript
function multiPartUpload(bucketName, itemName, filePath) {
    var uploadID = null;

    if (!fs.existsSync(filePath)) {
        logError(new Error(`The file \'${filePath}\' does not exist or is not accessible.`));
        return;
    }

    console.log(`Starting multi-part upload for ${itemName} to bucket: ${bucketName}`);
    return cos.createMultipartUpload({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        uploadID = data.UploadId;

        //begin the file upload        
        fs.readFile(filePath, (e, fileData) => {
            //min 5MB part
            var partSize = 1024 * 1024 * 5;
            var partCount = Math.ceil(fileData.length / partSize);
    
            async.timesSeries(partCount, (partNum, next) => {
                var start = partNum * partSize;
                var end = Math.min(start + partSize, fileData.length);
    
                partNum++;

                console.log(`Uploading to ${name} (part ${partNum} of ${partCount})`);  

                cos.uploadPart({
                    Body: fileData.slice(start, end),
                    Bucket: bucketName,
                    Key: itemName,
                    PartNumber: partNum,
                    UploadId: uploadID
                }).promise()
                .then((data) => {
                    next(e, {ETag: data.ETag, PartNumber: partNum});
                })
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.log(`ERROR: ${e.code} - ${e.message}\n`);
                });
            }, (e, dataPacks) => {
                cos.completeMultipartUpload({
                    Bucket: bucketName,
                    Key: itemName,
                    MultipartUpload: {
                        Parts: dataPacks
                    },
                    UploadId: uploadID
                }).promise()
                .then(logDone)
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.log(`ERROR: ${e.code} - ${e.message}\n`);
                });
            });
        });
    })
    .catch(e) {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    };
}

function cancelMultiPartUpload(bucketName, itemName, uploadID) {
    return cos.abortMultipartUpload({
        Bucket: bucketName,
        Key: itemName,
        UploadId: uploadID
    }).promise()
    .then(() => {
        console.log(`Multi-part upload aborted for ${itemName}`);
    })
    .catch((e){
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Using Key Protect

Key Protect can be added to a storage bucket to manage encryption keys.  All data is encrypted in IBM COS, but Key Protect provides a service for generating, rotating, and controlling access to encryption keys using a centralized service.

### Before You Begin

The following items are necessary in order to create a bucket with Key-Protect enabled:

* A Key Protect service [provisioned](/docs/services/keymgmt/keyprotect_provision.html#provision)
* A Root key available (either [generated](/docs/services/keymgmt/keyprotect_create_root.html#create_root_keys) or [imported](/docs/services/keymgmt/keyprotect_import_root.html#import_root_keys))

### Retrieving the Root Key CRN

1. Retrieve the [instance ID](/docs/services/keymgmt/keyprotect_authentication.html#retrieve_instance_ID) for your Key Protect service
2. Use the [Key Protect API](/docs/services/keymgmt/keyprotect_authentication.html#access-api) to retrieve all your [available keys](/docs/services/keymgmt/keyprotect_authentication.html#form_api_request)
    * You can either use `curl` commands or an API REST Client such as [Postman](../api-reference/postman.html) to access the [Key Protect API](/docs/services/keymgmt/keyprotect_authentication.html#access-api).
3. Retrieve the CRN of the root key you will use to enabled Key Protect on the your bucket.  The CRN will look similar to below:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creating a bucket with Key Protect enabled

```javascript
function createBucketKP(bucketName) {
    console.log(`Creating new encrypted bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: '<bucket-location>'
        },
        IBMSSEKPEncryptionAlgorithm: '<algorithm>',
        IBMSSEKPCustomerRootKeyCrn: '<root-key-crn>'
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
        logDone();
    }))
    .catch(logError);
}
```
*Key Values*
* `<bucket-location>` - Region or location for your bucket (Key Protect is only available in certain regions.  Ensure your location matches the Key Protect service) A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage/basics/classes#locationconstraint)..
* `<algorithm>` - The encryption algorithm used for new objects added to the bucket (Default is AES256).
* `<root-key-crn>` - CRN of the Root Key obtained from the Key Protect service.

*SDK References*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## Using Archive Feature

Archive Tier allows users to archive stale data and reduce their storage costs. Archival policies (also known as *Lifecycle Configurations*) are created for buckets and applies to any objects added to the bucket after the policy is created.

### View a bucket's lifecycle configuration
```javascript
function getLifecycleConfiguration(bucketName) {
    return cos.getBucketLifecycleConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log(`Retrieving bucket lifecycle config from: ${bucketName}`);
            console.log(JSON.stringify(data, null, 4));
        }
        else {
            console.log(`No lifecycle configuration for ${bucketName}`);
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Create a lifecycle configuration 

Detailed information about structuring the lifecycle configuration rules are available in the [API Reference](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html#create-bucket-lifecycle)

```javascript
function createLifecycleConfiguration(bucketName) {
    //
    var config = {
        Rules: [{
            Status: 'Enabled', 
            ID: '<policy-id>',
            Filter: {
                Prefix: ''
            },
            Transitions: [{
                Days: <number-of-days>, 
                StorageClass: 'GLACIER'
            }]
        }]
    };
    
    return cos.putBucketLifecycleConfiguration({
        Bucket: bucketName,
        LifecycleConfiguration: config
    }).promise()
    .then(() => {
        console.log(`Created bucket lifecycle config for: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Key Values*
* `<policy-id>` - Name of the lifecycle policy (must be unqiue)
* `<number-of-days>` - Number of days to keep the restored file

*SDK References*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Delete a bucket's lifecycle configuration
```javascript
function deleteLifecycleConfiguration(bucketName) {
    return cos.deleteBucketLifecycle({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Deleted bucket lifecycle config from: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Temporarily restore an object

Detailed information about the restore request parameters are available in the [API Reference](/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#restore-object)

```javascript
function restoreItem(bucketName, itemName) {
    var params = {
        Bucket: bucketName, 
        Key: itemName, 
        RestoreRequest: {
            Days: <number-of-days>, 
            GlacierJobParameters: {
                Tier: 'Bulk' 
            },
        } 
    };
    
    return cos.restoreObject(params).promise()
    .then(() => {
        console.log(`Restoring item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Key Values*
* `<number-of-days>` - Number of days to keep the restored file

*SDK References*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### View HEAD information for an object
```javascript
function getHEADItem(bucketName, itemName) {
    return cos.headObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        console.log(`Retrieving HEAD for item: ${itemName} from bucket: ${bucketName}`);
        console.log(JSON.stringify(data, null, 4));
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK References*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## Updating Metadata

There are two ways to update the metadata on an existing object:
* A `PUT` request with the new metadata and the original object contents
* Executing a `COPY` request with the new metadata specifying the original object as the copy source

### Using PUT to update metadata

**Note:** The `PUT` request overwrites the existing contents of the object so it must first be downloaded and re-uploaded with the new metdata


```javascript
function updateMetadataPut(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //retrieve the existing item to reload the contents
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        //set the new metadata
        var newMetadata = {
            newkey: metaValue
        };

        return cos.putObject({
            Bucket: bucketName,
            Key: itemName,
            Body: data.Body,
            Metadata: newMetadata
        }).promise()
        .then(() => {
            console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
        })
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

### Using COPY to update metadata

```javascript
function updateMetadataCopy(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //set the copy source to itself
    var copySource = bucketName + '/' + itemName;

    //set the new metadata
    var newMetadata = {
        newkey: metaValue
    };

    return cos.copyObject({
        Bucket: bucketName, 
        Key: itemName,
        CopySource: copySource,
        Metadata: newMetadata,
        MetadataDirective: 'REPLACE'
    }).promise()
    .then((data) => {
        console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```