---

copyright:
  years: 2019
lastupdated: "2019-03-05"

---

# Getting Started with the SDK

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}
{:javascript: .ph data-hd-programlang='javascript'}
{:go: .ph data-hd-programlang='go'}

In this quickstart guide, you'll be provided a code example that will demonstrate the following operations:

* Create a new bucket
* List the available buckets
* Create a new text file
* List the available files
* Retrieve the text file contents
* Upload a large binary file
* Delete a file
* Delete a bucket

## Before you begin

You will need:

* an [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com/registration/?target=%2Fcatalog%2Fservices%2Fcloud-object-storage)
* an [instance of {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics/order-storage.html)
* an [IAM API key](/docs/services/cloud-object-storage/iam/overview.html) with Writer access to your {{site.data.keyword.cos_short}}

## Getting the SDK

Specific instructions for downloading and installing the SDK is available in [Using Python.](/docs/services/cloud-object-storage/libraries/python.html#using-python){: python}[Using Node.js.](/docs/services/cloud-object-storage/libraries/node.html#installing-the-sdk){: javascript}

## Code Example

The code examples below provide introductory examples of executing the basic operations with {{site.data.keyword.cos_short}}.  For simplicity, the code example can be run multiple times as it uses Universally Unique Identifiers (UUIDs) for bucket/item names to prevent potential conflicts.

To complete the code example you will need to replace the following values:

|Value|Description|Example|
|---|---|---|
|`<endpoint>`|Regional endpoint for your COS instance|`s3.us-south.cloud-object-storage.appdomain.cloud`|
|`<api-key>`|IAM API Key with Writer permissions|`xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4`|
|`<resource-instance-id>`|Instance ID for your COS instance|`crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::`|
|`<storage-class>`|Regional storage class (should match your regional endpoint)|`us-south-standard`|

```python
import os
import uuid
import ibm_boto3
from ibm_botocore.client import Config
from ibm_botocore.exceptions import ClientError
import ibm_s3transfer.manager

def log_done():
    print("DONE!\n")

def log_client_error(e):
    print("CLIENT ERROR: {0}\n".format(e))

def log_error(msg):
    print("UNKNOWN ERROR: {0}\n".format(msg))

def get_uuid():
    return str(uuid.uuid4().hex)

def generate_big_random_file(file_name, size):
    with open('%s'%file_name, 'wb') as fout:
        fout.write(os.urandom(size))

# Retrieve the list of available buckets
def get_buckets():
    print("Retrieving list of buckets")
    try:
        bucket_list = cos_cli.list_buckets()
        for bucket in bucket_list["Buckets"]:
            print("Bucket Name: {0}".format(bucket["Name"]))
        
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to retrieve list buckets: {0}".format(e))

# Retrieve the list of contents for a bucket
def get_bucket_contents(bucket_name):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        file_list = cos_cli.list_objects(Bucket=bucket_name)
        if file_list.has_key("Contents"):
            for file in file_list["Contents"]:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))

            log_done()
        else:
            print("Bucket {0} has no items.".format(bucket_name))
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to retrieve bucket contents: {0}".format(e))

# Retrieve a particular item from the bucket
def get_item(bucket_name, item_name):
    print("Retrieving item from bucket: {0}, key: {1}".format(bucket_name, item_name))
    try:
        file = cos_cli.get_object(Bucket=bucket_name, Key=item_name)
        print("File Contents: {0}".format(file["Body"].read()))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to retrieve file contents for {0}:\n{1}".format(item_name, e))

# Create new bucket
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos_cli.create_bucket(
            Bucket=bucket_name, 
            CreateBucketConfiguration={
                "LocationConstraint":COS_STORAGE_CLASS
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to create bucket: {0}".format(e))

# Create new text file
def create_text_file(bucket_name, item_name, file_text):
    print("Creating new item: {0} in bucket: {1}".format(item_name, bucket_name))
    try:
        cos_cli.put_object(
            Bucket=bucket_name,
            Key=item_name,
            Body=file_text
        )
        print("Item: {0} created!".format(item_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to create text file: {0}".format(e))

# Delete item
def delete_item(bucket_name, item_name):
    print("Deleting item: {0} from bucket: {1}".format(item_name, bucket_name))
    try:
        cos_cli.delete_object(
            Bucket=bucket_name, 
            Key=item_name
        )
        print("Item: {0} deleted!".format(item_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to delete item: {0}".format(e))

# Delete bucket
def delete_bucket(bucket_name):
    print("Deleting bucket: {0}".format(bucket_name))
    try:
        cos_cli.delete_bucket(Bucket=bucket_name)
        print("Bucket: {0} deleted!".format(bucket_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to delete bucket: {0}".format(e))

def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # set the chunk size to 5 MB
    part_size = 1024 * 1024 * 5

    # set threadhold to 5 MB
    file_threshold = 1024 * 1024 * 5

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

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # example: https://s3.us-south.cloud-object-storage.appdomain.cloud
COS_API_KEY_ID = "<api-key>" # example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
COS_AUTH_ENDPOINT = "https://iam.ng.bluemix.net/oidc/token"
COS_SERVICE_CRN = "<resource-instance-id>" # example: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
COS_STORAGE_CLASS = "<storage-class>" # example: us-south-standard

# Create client connection
cos_cli = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_SERVICE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

# *** Main Program ***
def main():
    try:
        new_bucket_name = "py.bucket." + get_uuid()
        new_text_file_name = "py_file_" + get_uuid() + ".txt"
        new_text_file_contents = "This is a test file from Python code sample!!!"
        new_large_file_name = "py_large_file_" + get_uuid() + ".bin"
        new_large_file_size = 1024 * 1024 * 20 

        # create a new bucket
        create_bucket(new_bucket_name)

        # get the list of buckets
        get_buckets()

        # create a new text file
        create_text_file(new_bucket_name, new_text_file_name, new_text_file_contents)

        # get the list of files from the new bucket
        get_bucket_contents(new_bucket_name)

        # get the text file contents
        get_item(new_bucket_name, new_text_file_name)

        # create a new local binary file that is 20 MB
        generate_big_random_file(new_large_file_name, new_large_file_size)

        # upload the large file using transfer manager
        upload_large_file(new_bucket_name, new_large_file_name, new_large_file_name)

        # get the list of files from the new bucket
        get_bucket_contents(new_bucket_name)

        # remove the two new files
        delete_item(new_bucket_name, new_large_file_name)
        delete_item(new_bucket_name, new_text_file_name)

        # remove the new bucket
        delete_bucket(new_bucket_name)
    except Exception as e:
        log_error("Main Program Error: {0}".format(e))

if __name__ == "__main__":
    main()
```
{: codeblock}
{: python}

```javascript
'use strict';

// Required libraries
const AWS = require('ibm-cos-sdk');
const fs = require('fs');
const async = require('async');
const uuidv1 = require('uuid/v1');
const crypto = require('crypto');

function logError(e) {
    console.log(`ERROR: ${e.code} - ${e.message}\n`);
}

function logDone() {
    console.log('DONE!\n');
}

function getUUID() {
    return uuidv1().toString().replace(/-/g, "");
}

function generateBigRandomFile(fileName, size) {
    return new Promise(function(resolve, reject) {
        crypto.randomBytes(size, (err, buf) => {
            if (err) reject(err);

            fs.writeFile(fileName, buf, function (err) {
                if (err) {
                    reject(err);
                }
                else {
                    resolve();
                }
            });     
        });      
    });
}

// Retrieve the list of available buckets
function getBuckets() {
    console.log('Retrieving list of buckets');
    return cos.listBuckets()
    .promise()
    .then((data) => {
        if (data.Buckets != null) {
            for (var i = 0; i < data.Buckets.length; i++) {
                console.log(`Bucket Name: ${data.Buckets[i].Name}`);
            }
            logDone();
        }
    })
    .catch((logError));
}

// Retrieve the list of contents for a bucket
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
            logDone();
        }    
    })
    .catch(logError);
}

// Retrieve a particular item from the bucket
function getItem(bucketName, itemName) {
    console.log(`Retrieving item from bucket: ${bucketName}, key: ${itemName}`);
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log('File Contents: ' + Buffer.from(data.Body).toString());
            logDone();
        }    
    })
    .catch(logError);
}

// Create new bucket
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: COS_STORAGE_CLASS
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
        logDone();
    }))
    .catch(logError);
}

// Create new text file
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName, 
        Key: itemName, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
        logDone();
    })
    .catch(logError);
}

// Delete item
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() =>{
        console.log(`Item: ${itemName} deleted!`);
        logDone();
    })
    .catch(logError);
}

// Delete bucket
function deleteBucket(bucketName) {
    console.log(`Deleting bucket: ${bucketName}`);
    return cos.deleteBucket({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucketName} deleted!`);
        logDone();
    })
    .catch(logError);
}

// Multi part upload
function multiPartUpload(bucketName, itemName, filePath) {
    var uploadID = null;

    if (!fs.existsSync(filePath)) {
        logError(new Error(`The file \'${filePath}\' does not exist or is not accessible.`));
        return;
    }

    return new Promise(function(resolve, reject) {
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

                    console.log(`Uploading to ${itemName} (part ${partNum} of ${partCount})`);  

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
                        logError(e);
                        reject(e);
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
                    .then(() => {
                        logDone();
                        resolve();
                    })
                    .catch((e) => {
                        cancelMultiPartUpload(bucketName, itemName, uploadID);
                        logError(e);
                        reject(e);
                    });
                });
            });
        })
        .catch((e) => {
            logError(e);
            reject(e);
        });
    });
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
    .catch(logError);
}

// Constants for IBM COS values
const COS_ENDPOINT = "<endpoint>";  // example: s3.us-south.cloud-object-storage.appdomain.cloud
const COS_API_KEY_ID = "<api-key";  // example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
const COS_AUTH_ENDPOINT = "https://iam.ng.bluemix.net/oidc/token";
const COS_SERVICE_CRN = "<resource-instance-id>"; // example: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
const COS_STORAGE_CLASS = "<storage-class>"; // example: us-south-standard

// Init IBM COS library
var config = {
    endpoint: COS_ENDPOINT,
    apiKeyId: COS_API_KEY_ID,
    ibmAuthEndpoint: COS_AUTH_ENDPOINT,
    serviceInstanceId: COS_SERVICE_CRN,
};

var cos = new AWS.S3(config);

// Main app
function main() {
    try {
        var newBucketName = "js.bucket." + getUUID();
        var newTextFileName = "js_file_" + getUUID() + ".txt";
        var newTextFileContents = "This is a test file from Node.js code sample!!!";
        var newLargeFileName = "js_large_file_" + getUUID() + ".bin";
        var newLargeFileSize = 1024 * 1024 * 20;

        createBucket(newBucketName) // create a new bucket
            .then(() => getBuckets()) // get the list of buckets
            .then(() => createTextFile(newBucketName, newTextFileName, newTextFileContents)) // create a new text file
            .then(() => getBucketContents(newBucketName)) // get the list of files from the new bucket
            .then(() => getItem(newBucketName, newTextFileName)) // get the text file contents
            .then(() => generateBigRandomFile(newLargeFileName, newLargeFileSize)) // create a new local binary file that is 20 MB
            .then(() => multiPartUpload(newBucketName, newLargeFileName, newLargeFileName)) // upload the large file using transfer manager
            .then(() => getBucketContents(newBucketName)) // get the list of files from the new bucket
            .then(() => deleteItem(newBucketName, newLargeFileName)) // remove the large file
            .then(() => deleteItem(newBucketName, newTextFileName)) // remove the text file
            .then(() => deleteBucket(newBucketName)); // remove the new bucket
    }
    catch(ex) {
        logError(ex);
    }
}

main();
```
{: codeblock}
{: javascript}

## Running the Code Example

To run the code sample, either copy the code blocks above or [download the Python example](/docs/services/cloud-object-storage/libraries/examples/python-example.py){: python}[download the Node.js example](/docs/services/cloud-object-storage/libraries/examples/node-example.js){: javascript} and run the following:
```
python python-example.py
```
{: codeblock}
{: python}

```
node node-example.py
```
{: codeblock}
{: javascript}

## Output from the Code Example

The output from the Code Example should resemble the following:

```
Creating new bucket: py.bucket.779177bfe41945edb458294d0b25440a
Bucket: py.bucket.779177bfe41945edb458294d0b25440a created!
DONE!

Retrieving list of buckets
Bucket Name: py.bucket.779177bfe41945edb458294d0b25440a
DONE!

Creating new item: py_file_17b79068b7c845658f2f74249e14e267.txt in bucket: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt created!
DONE!

Retrieving bucket contents from: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt (46 bytes).
DONE!

Retrieving item from bucket: py.bucket.779177bfe41945edb458294d0b25440a, key: py_file_17b79068b7c845658f2f74249e14e267.txt
File Contents: This is a test file from Python code sample!!!
DONE!

Starting large file upload for py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin to bucket: py.bucket.779177bfe41945edb458294d0b25440a
Large file upload complete!
Retrieving bucket contents from: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt (46 bytes).
Item: py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin (20971520 bytes).
DONE!

Deleting item: py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin from bucket: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin deleted!
DONE!

Deleting item: py_file_17b79068b7c845658f2f74249e14e267.txt from bucket: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt deleted!
DONE!

Deleting bucket: py.bucket.779177bfe41945edb458294d0b25440a
Bucket: py.bucket.779177bfe41945edb458294d0b25440a deleted!
DONE!
```
{: codeblock}
{: python}

```
Creating new bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32 created!
DONE!

Retrieving list of buckets
Bucket Name: js.bucket.c697b4403f8211e9b1228597cf8e3a32
DONE!

Creating new item: js_file_c697db503f8211e9b1228597cf8e3a32.txt
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt created!
DONE!

Retrieving bucket contents from: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt (47 bytes).
DONE!

Retrieving item from bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32, key: js_file_c697db503f8211e9b1228597cf8e3a32.txt
File Contents: This is a test file from Node.js code sample!!!
DONE!

Starting multi-part upload for js_large_file_c697db513f8211e9b1228597cf8e3a32.bin to bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 1 of 4)
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 2 of 4)
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 3 of 4)
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 4 of 4)
DONE!

Retrieving bucket contents from: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt (47 bytes).
Item: js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (20971520 bytes).
DONE!

Deleting item: js_large_file_c697db513f8211e9b1228597cf8e3a32.bin
Item: js_large_file_c697db513f8211e9b1228597cf8e3a32.bin deleted!
DONE!

Deleting item: js_file_c697db503f8211e9b1228597cf8e3a32.txt
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt deleted!
DONE!

Deleting bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32 deleted!
DONE!
```
{: codeblock}
{: javascript}

<br/>