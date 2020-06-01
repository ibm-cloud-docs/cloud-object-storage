---

copyright:
  years: 2017, 2020

lastupdated: "2020-02-10"

keywords: storage classes, tiers, cost, buckets, location constraint, provisioning code, locationconstraint

subcollection: cloud-object-storage


---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Use storage classes
{: #classes}

Not all data feeds active workloads. Archival data might sit untouched for long periods of time. For less active workloads, you can create buckets with different storage classes. Objects that are stored in these buckets incur charges on a different schedule than standard storage.
{: shortdesc}

## What are the classes?
{: #classes-about}

You can choose from five storage classes:

*  **Smart Tier** can be used for any workload, especially dynamic workloads where access patterns are unknown or difficult to predict.  Smart Tier provides a simplified pricing structure and automatic cost optimization by classifying the data into "hot", "cool", and "cold" tiers based on monthly usage patterns. All data in the bucket is then billed at the lowest applicable rate.  There are no threshold object sizes or storage periods, and there are no retrieval fees. For a detailed explanation of how it works, see the [billing topic](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-billing#billing-storage-classes).
*  **Standard** is used for active workloads, with no charge for data retrieved (other than the cost of the operational request itself).
*  **Vault** is used for cool workloads where data is accessed less than once a month - an extra retrieval charge ($/GB) is applied each time data is read. The service includes a minimum threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault** is used for cold workloads where data is accessed every 90 days or less - a larger extra retrieval charge ($/GB) is applied each time data is read. The service includes a longer minimum threshold for object size and storage period consistent with the intended use of this service for cold, inactive data.
*  **Flex** is being replaced by Smart Tier for dynamic workloads. Flex users can continue to manage their data in existing Flex buckets, although no new Flex buckets may be created.  Existing users can reference pricing information [here](/docs/cloud-object-storage?topic=cloud-object-storage-flex-pricing).

For more information, see [the pricing table at ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

For more information about how to create buckets with different storage classes, see the [API reference](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

## How do I create a bucket with a different storage class?
{: #classes-locationconstraint}

When creating a bucket in the console, there is a menu that allows for storage class selection. 

When creating buckets programmatically, it is necessary to specify a `LocationConstraint` that corresponds with the endpoint used. Valid provisioning codes for `LocationConstraint` are <br>
&emsp;&emsp;  **US Geo** `us-standard`                        / `us-vault`      / `us-cold`      / `us-flex`         / `us-smart` <br>
&emsp;&emsp;  **US East** `us-east-standard`                  / `us-east-vault` / `us-east-cold` / `us-east-flex`    / `us-east-smart` <br>
&emsp;&emsp;  **US South** `us-south-standard`                / `us-south-vault`/ `us-south-cold`/ `us-south-flex`   / `us-south-smart` <br>
&emsp;&emsp;  **EU Geo** `eu-standard`                        / `eu-vault`      / `eu-cold`      / `eu-flex`         / `eu-smart` <br>
&emsp;&emsp;  **EU Great Britain** `eu-gb-standard`           / `eu-gb-vault`   / `eu-gb-cold`   / `eu-gb-flex`      / `eu-gb-smart` <br>
&emsp;&emsp;  **EU Germany** `eu-de-standard`                 / `eu-de-vault`   / `eu-de-cold`   / `eu-de-flex`      / `eu-de-smart` <br>
&emsp;&emsp;  **AP Geo** `ap-standard`                        / `ap-vault`      / `ap-cold`      / `ap-flex`         / `ap-smart` <br>
&emsp;&emsp;  **AP Japan** `jp-tok-standard`                  / `jp-tok-vault`  / `jp-tok-cold`  / `jp-tok-flex`     / `jp-tok-smart`<br>
&emsp;&emsp;  **AP Australia** `au-syd-standard`              / `au-syd-vault`  / `au-syd-cold`  / `au-syd-flex`     / `au-syd-smart` <br>
&emsp;&emsp;  **Amsterdam** `ams03-standard`                  / `ams03-vault`   / `ams03-cold`   / `ams03-flex`      / `ams03-smart` <br>
&emsp;&emsp;  **Chennai** `che01-standard`                    / `che01-vault`   / `che01-cold`   / `che01-flex`      / `che01-smart` <br>
&emsp;&emsp;  **Hong Kong S.A.R. of the PRC** `hkg02-standard`/ `hkg02-vault`   / `hkg02-cold`   / `hkg02-flex`      / `hkg02-smart` <br>
&emsp;&emsp;  **Melbourne** `mel01-standard`                  / `mel01-vault`   / `mel01-cold`   / `mel01-flex`      / `mel01-smart` <br>
&emsp;&emsp;  **Mexico** `mex01-standard`                     / `mex01-vault`   / `mex01-cold`   / `mex01-flex`      / `mex01-smart` <br>
&emsp;&emsp;  **Milan** `mil01-standard`                      / `mil01-vault`   / `mil01-cold`   / `mil01-flex`      / `mil01-smart` <br>
&emsp;&emsp;  **Montréal** `mon01-standard`                   / `mon01-vault`   / `mon01-cold`   / `mon01-flex`      / `mon01-smart` <br>
&emsp;&emsp;  **Paris** `par01-standard`                      / `par01-vault`   / `par01-cold`   / `par01-flex`      / `par01-smart` <br>
&emsp;&emsp;  **Oslo** `osl01-standard`                       / `osl01-vault`   / `osl01-cold`   / `osl01-flex`      / `osl01-smart` <br>
&emsp;&emsp;  **San Jose** `sjc04-standard`                   / `sjc04-vault`   / `sjc04-cold`   / `sjc04-flex`      / `sjc04-smart` <br>
&emsp;&emsp;  **São Paulo** `sao01-standard`                  / `sao01-vault`   / `sao01-cold`   / `sao01-flex`      / `sao01-smart` <br>
&emsp;&emsp;  **Seoul** `seo01-standard`                      / `seo01-vault`   / `seo01-cold`   / `seo01-flex`      / `seo01-smart` <br>
&emsp;&emsp;  **Singapore** `sng01-standard`                  / `sng01-vault`   / `sng01-cold`   / `sng01-flex`      / `sng01-smart` <br>
&emsp;&emsp;  **Toronto** `tor01-standard`                    / `tor01-vault`   / `tor01-cold`   / `tor01-flex`      / `tor01-smart` <br>


For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Using the REST API, Libraries, and SDKs
{: #classes-sdk}

Several new APIs have been introduced to the IBM COS SDKs to provide support for applications working with retention policies. Select a language (curl, Java, JavaScript, Go, or Python) at the beginning of this page to view examples that use the appropriate COS SDK. 

 All code examples assume the existence of a client object that is called `cos` that can call the different methods. For details on creating clients, see the specific SDK guides.


### Create a bucket with a storage class

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

It isn't possible to change the storage class of a bucket once the bucket is created. If objects need to be reclassified, it's necessary to move the data to another bucket with the wanted storage class. 
