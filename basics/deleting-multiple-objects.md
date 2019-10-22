---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-10-15"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:external: target="_blank" .external}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}


# Code patterns for deleting multiple objects, 
{: #deleting-multiple-objects-patterns}

This overview of code patterns focuses on the steps that are needed to access a list of all items in a bucket for the purpose of deleting each one sequentially.
{: .shortdesc}

## Before you begin
{: #dmop-prereqs}

Specific instructions for downloading and installing SDKs are available for [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python){: external}, [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node){: external}, [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java){: external}, and [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go){: external}.

You need:
  * An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com)
  * An [instance of {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * Configured and operational use of {{site.data.keyword.cos_full}} SDKs for your choice of Java, Python, NodeJS, or Go.
{: #dmop-prereqs}

## Code Example
{: #dmop-example}

```javascript
const myCOS = require('ibm-cos-sdk');

var config = {
    endpoint: 's3.us-geo.cloud-object-storage.appdomain.cloud',
    apiKeyId: 'd-2-DO-NOT-USEevplExampleo_t3ZTCJO',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/SAMPLE253abf6e65dca920c9d58126b:3f656f43-5c2a-941e-b128-DO-NOT-USE::',
};

var cosClient = new myCOS.S3(config);

function logDone() {
    console.log('COMPLETE!\n');
}

function logError(e) {
    console.log(`ERROR: ${e.code} - ${e.message}\n`);
}

// Retrieve the list of contents from a bucket
function getBucketContents(bucketName) {
		var returnArr = new Array();
		
    console.log(`Retrieving bucket contents from: ${bucketName}\n`);
    return cosClient.listObjects(
        {Bucket: bucketName},
    ).promise()
    .then((data) => {
        if (data != null && data.Contents != null) {
            for (var i = 0; i < data.Contents.length; i++) {
            		returnArr.push(data.Contents[i].Key);
                var itemKey = data.Contents[i].Key;
                var itemSize = data.Contents[i].Size;
                console.log(`Item: ${itemKey} (${itemSize} bytes).\n`)
            }
            logDone();
        }    
    })
    .catch(logError);
    
    return returnArr;
}

// Delete item
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cosClient.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() =>{
        console.log(`Item: ${itemName} deleted!`);
        
    })
    .catch(logError);
}

function main() {
	try {
        var BucketName = "<BUCKET_NAME>";
        
        var deleteArr = getBucketContents(BucketName);
        var self = this;
        if (deleteArr.length != 0) {
            for (var i = 0; i < deleteArr.length; i++) {
                self.deleteItem(BucketName, deleteArr[i]);
            }
        }
    }
    catch(ex) {
        logError(ex);
    }
}

main();
```
{: codeblock}
{: javascript}
