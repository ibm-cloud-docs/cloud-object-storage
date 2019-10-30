---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-10-30"

keywords: empty bucket, delete, multiple

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
{:go: .ph data-hd-programlang='go'}


# Empty a bucket 
{: #deleting-multiple-objects-patterns}

This overview of code patterns using SDKs and other clients for {{site.data.keyword.cos_full}} focuses on the steps that are needed to access a list of all items in a bucket for the purpose of deleting each one sequentially.
{: shortdesc}

The process of emptying a bucket is familiar to anyone who has to delete buckets in their instance of {{site.data.keyword.cos_short}} because a bucket has to be empty to be deleted. There may be other reasons you may wish to delete items, but want to avoid deleting every object individually. This code pattern for the supported SDKs will allow you to define your configuration, create a client, and then connect with that client in order to get a list of all the items in an identified bucket for in order to delete them.

It is a best practice to avoid putting credentials in scripts. This example is for testing and educational purposes, and your specific setup should be informed by best practices and [Developer Guidance](/docs/services/cloud-object-storage?topic=cloud-object-storage-dev-guide).{: tip}

## Before you begin
{: #dmop-prereqs}

Specific instructions for downloading and installing SDKs are available for [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python){: external}, [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node){: external}, [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java){: external}, and [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go){: external}. Also, when working with Command Line Instructions (CLI) and your CLI clients, please check out the pertinent information related to {{site.data.keyword.cos_short}} regarding [AWS](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli){: external} compatibility, [Minio](/docs/cli?topic=cloud-object-storage-minio){: external}, and [rClone](/docs/cli?topic=cloud-object-storage-rclone){: external}.

For this code pattern you will need:
  * An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com)
  * An [instance of {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * Configured and operational use of {{site.data.keyword.cos_full}} SDKs for your choice of Java, Python, NodeJS, or Go; or, a configured and operational CLI client.
{: #dmop-prereqs}

## Code Example
{: #dmop-example}

Deleting an entire directory or removing all the contents of a bucket can be time consuming deleting each object, one at a time. The ability to delete one item at a time can be leveraged to save time and effort by collecting a list of all the items before deletion. 

The topic itself points out the danger of deletion: data **will** be lost. Of course, when that is the goal, caution should be exercised that only the targeted deletions should occur. Check&mdash;and double-check&mdash;instances, bucket names, and any prefixes or paths that need to be specified.{: tip}

### Overview
{: #dmop-example}

The code pattern in this exercise configures a client before creating one for the purpose of gathering a list of items for the purpose of deleting each object.{: javascript}The code pattern in this exercise configures a client before creating one for the purpose of gathering a list of items for the purpose of deleting each object.{: java}The code pattern in this exercise configures a client before creating one for the purpose of gathering a list of items for the purpose of deleting each object.{: python}The code pattern in this exercise configures a client before creating one for the purpose of gathering a list of items for the purpose of deleting each object.{: go}Sample instructions are provided for use once your CLI client has been configured and is operational.{: http}

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

```python
import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # eg "W00YiRnLW4a3fTjMB-odB-2ySfTrFBIQQWanc--P3byk"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>" # eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Create resource
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    returnArray = []
    try:

        more_results = True
        next_token = ""

        while (more_results):
            response = cos.list_objects_v2(Bucket=bucket_name, MaxKeys=max_keys, ContinuationToken=next_token)
            files = response["Contents"]
            for file in files:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))
                returnArray.append(file["Key"])

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
    
    return 

def delete_item(bucket_name, item_name):
    print("Deleting item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).delete()
        print("Item: {0} deleted!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete item: {0}".format(e))

def main():
    bucket = "<bucket_name>"
    deleteListArray = get_bucket_contents_v2(bucket, 1000)
    for item in deleteListArray:
        delete_item(bucket_name, item_name)

main()
```
{: codeblock}
{: python}

```java
package com.cos;
    
    import java.sql.Timestamp;
    import java.util.List;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class CosDeleteMultipleItems
    {

        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {

            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";

            String bucketName = "<BUCKET_NAME>";  // eg my-unique-bucket-name
            String newBucketName = "<NEW_BUCKET_NAME>"; // eg my-other-unique-bucket-name
            String api_key = "<API_KEY>"; // eg "W00YiRnLW4k3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
            String service_instance_id = "<SERVICE_INSTANCE_ID"; // eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud"; // this could be any service endpoint

            String storageClass = "us-south-standard";
            String location = "us"; 

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            List<deletedItem.key> itemsForDeletion = getBucketContentsV2(bucketName, 1000);
            
            for(Int deletedItem: itemsForDeletion) {
                deleteItem(bucketName, deletedItem.getKey());
                System.out.printf("Deleted item: %s\n", deletedItem.getKey());
            }
        }

        /**
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

        public static void getBucketContentsV2(String bucketName, int maxKeys) {
            System.out.printf("Retrieving bucket contents (V2) from: %s\n", bucketName);
        
            boolean moreResults = true;
            String nextToken = "";
        
            while (moreResults) {
                ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(bucketName)
                    .withMaxKeys(maxKeys)
                    .withContinuationToken(nextToken);
        
                ListObjectsV2Result result = _cos.listObjectsV2(request);
                for(S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    System.out.printf("Item: %s (%s bytes)\n", objectSummary.getKey(), objectSummary.getSize());
                }
                
                if (result.isTruncated()) {
                    nextToken = result.getNextContinuationToken();
                    System.out.println("...More results in next batch!\n");
                }
                else {
                    nextToken = "";
                    moreResults = false;
                }
            }
            System.out.println("...No more results!");
        }
    
        public static void deleteItem(String bucketName, String itemName) {
            System.out.printf("Deleting item: %s\n", itemName);
            _cos.deleteObject(bucketName, itemName);
            System.out.printf("Item: %s deleted!\n", itemName);
        }

    }
```
{: codeblock}
{: java}

```go
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)

// Constants for IBM COS values
const (
    apiKey            = "<API_KEY>"  // eg "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ"
    serviceInstanceID = "<RESOURCE_INSTANCE_ID>" // "crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::"
    authEndpoint      = "https://iam.cloud.ibm.com/identity/token"
    serviceEndpoint   = "<SERVICE_ENDPOINT>" // eg "https://s3.us.cloud-object-storage.appdomain.cloud"
    bucketLocation    = "<LOCATION>" // eg "us"
)

// Create config
conf := aws.NewConfig().
    WithRegion("us-standard").
    WithEndpoint(serviceEndpoint).
    WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
    WithS3ForcePathStyle(true)

func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Names
    Bucket := "<BUCKET_NAME>"
    Input := &s3.ListObjectsV2Input{
            Bucket: aws.String(Bucket),
        }
    
    res, _ := client.ListObjectsV2(Input)

    for _, item := range res.Contents {
        input := &s3.DeleteObjectInput{
                Bucket: aws.String(Bucket),
                Key:    aws.String(*item.Key),
            } 
        d, _ := client.DeleteObject(input)
        fmt.Println(d)
    }
}
```
{: codeblock}
{: go}

#### rClone example
{: #dmop-rclone-example}
{: http}

```bash
rclone purge {remote}:{path} [flags]
```
{: codeblock}
{: http}

#### Minio example
{: #dmop-minio-example}
{: http}

```bash
mc rm --recursive --force {instance-alias}/{bucket-name}
```
{: codeblock}
{: http}

#### AWS example
{: #dmop-aws-example}
{: http}

```bash
aws s3 rm s3://{bucket-name} --recursive
```
{: codeblock}
{: http}

### Next Steps
Leveraging new and existing capabilities of the tools covered in this overview can be explored further at the 
[{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/). 


