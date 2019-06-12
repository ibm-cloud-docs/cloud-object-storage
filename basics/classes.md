---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: storage classes, tiers, cost, buckets, location constraint, provisioning code, locationconstraint

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:table: .aria-labeledby="caption"}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}
{:curl: .ph data-hd-programlang='curl'}

# Use storage classes
{: #classes}

Not all data feeds active workloads. Archival data may sit untouched for long periods of time. For less active workloads, you can create buckets with different storage classes. Objects stored in these buckets will incur charges on a different schedule than standard storage.

## What are the classes?
{: #classes-about}

There are four storage classes:

*  **Standard**: Used for active workloads - there is no charge for data retrieved (besides the cost of the operational request itself and public outbound bandwidth).
*  **Vault**: Used for cool workloads where data is not accessed frequently - a retrieval charge applies for reading data. The service includes a threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault**: Used for cold workloads where data is primarily archived (accessed every 90 days or less) - a larger  retrieval charge applies for reading data. The service includes a threshold for object size and storage period consistent with the intended use of this service: storing cold, inactive data.
*  **Flex**: Used for dynamic workloads where access patterns are more difficult to predict. Depending on usage, if the lower costs of cooler storage combined with retrieval charges exceeds a cap value, then the storage charge increases and no any retrieval charges apply. If the data isn't accessed frequently, Flex storage can be more cost effective than Standard storage, and if cooler usage patterns become more active Flex storage is more cost effective than Vault or Cold Vault storage. No threshold object size or storage period applies to Flex buckets.

For pricing details please see [the pricing table at ibm.com](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage#s3api).

For information on how to create buckets with different storage classes, please see the [API reference](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html#storage-class).

## How do I create a bucket with a different storage class?
{: #classes-locationconstraint}

When creating a bucket in the console, there is a drop-down menu that allows for storage class selection. 

When creating buckets programmatically, it is necessary to specify a `LocationConstraint` that corresponds with the endpoint used. Valid provisioning codes for `LocationConstraint` are: <br>
&emsp;&emsp;  **US Geo:** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **US East:** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **US South:** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **EU Geo:** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **EU Great Britain:** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **EU Germany:** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **AP Geo:** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **AP Japan:** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **AP Australia:** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **Amsterdam:** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **Chennai:** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **Hong Kong:** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **Melbourne:** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **Mexico:** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **Milan:** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **Montréal:** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **Oslo:** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **San Jose:** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **São Paulo:** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **Seoul:** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **Toronto:** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Using the REST API, Libraries, and SDKs
{: #classes-sdk}

Several new APIs have been introduced to the IBM COS SDKs to provide support for applications working with retention policies. Select a language (HTTP, Java, Javascript, Go or Python) at the top of this page to view examples using the appropriate COS SDK. 

Note that all code examples assume the existence of a client object called `cos` that can call the different methods. For details on creating clients, see the specific SDK guides.

All date values used to set retention periods are GMT.
{:note}


### Create a bucket with with a storage class

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName, "us-vault");
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```
{: codeblock}
{: java}


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
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}


```py
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
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

```go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Names
    newBucket := "<NEW_BUCKET_NAME>"

    input := &s3.CreateBucketInput{
        Bucket: aws.String(newBucket),
        CreateBucketConfiguration: &s3.CreateBucketConfiguration{
            LocationConstraint: aws.String("us-cold"),
        },
    }
    client.CreateBucket(input)

    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```
{: codeblock}
{: go}


```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}
{: curl}

It is not possible to change the storage class of a bucket once the bucket is created. If objects need to be reclassified, it is necessary to move the data to another bucket with the desired storage class. 
