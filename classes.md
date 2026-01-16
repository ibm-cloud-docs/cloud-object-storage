---

copyright:
  years: 2017, 2026
lastupdated: "2026-01-14"

keywords: storage classes, tiers, cost, buckets, location constraint, provisioning code, locationconstraint

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using storage classes
{: #classes}

Not all data feeds active workloads. Archival data might sit untouched for long periods of time. For less active workloads, you can create buckets with different storage classes. Objects that are stored in these buckets incur charges on a different schedule than standard storage.
{: shortdesc}

This feature is not currently supported in {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}. [Learn more.](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite)
{: note}

## What are the classes?
{: #classes-about}

You can choose from four storage classes:

*  **Smart Tier** can be used for any workload, especially dynamic workloads where access patterns are unknown or difficult to predict.  Smart Tier provides a simplified pricing structure and automatic cost optimization by classifying the data into "hot", "cool", and "cold" tiers based on monthly usage patterns. All data in the bucket is then billed at the lowest applicable rate.  There are no threshold object sizes or storage periods, and there are no retrieval fees. For a detailed explanation of how it works, see the [billing topic](/docs/cloud-object-storage?topic=cloud-object-storage-billing#billing-storage-classes).
*  **Standard** is used for active workloads, with no charge for data retrieved (other than the cost of the operational request itself).
*  **Vault** is used for cool workloads where data is accessed less than once a month - an extra retrieval charge ($/GB) is applied each time data is read. The service includes a minimum threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault** is used for cold workloads where data is accessed every 90 days or less - a larger extra retrieval charge ($/GB) is applied each time data is read. The service includes a longer minimum threshold for object size and storage period consistent with the intended use of this service for cold, inactive data.

**Flex** has been replaced by Smart Tier for dynamic workloads. Flex users can continue to manage their data in existing Flex buckets, although no new Flex buckets may be created.  Existing users can reference pricing information [here](/docs/cloud-object-storage?topic=cloud-object-storage-flex-pricing).
{: note}

For more information, see [the pricing table at ibm.com](/objectstorage/create#pricing){: external}.

The **Active** storage class is only used with [One-Rate plans](/docs/cloud-object-storage?topic=cloud-object-storage-onerate), and cannot be used in a Standard plan instance.
{: important}

For more information about how to create buckets with different storage classes, see the [API reference](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

For each storage class, billing is based on aggregated usage across all buckets at the instance level. For example, for Smart Tier, the billing is based on usage across all Smart Tier buckets in a given instance - not on the individual buckets.
{: important}

## How do I create a bucket with a different storage class?
{: #classes-locationconstraint}

When creating a bucket in the console, there is a menu that allows for storage class selection.

When creating buckets programmatically, it is necessary to specify a `LocationConstraint` that corresponds with the endpoint used. Valid provisioning codes for `LocationConstraint` are <br>
&emsp;&emsp;  **BR São Paulo** `br-sao-standard`              / `br-sao-vault`  / `br-sao-cold`  / `br-sao-smart` <br>
&emsp;&emsp;  **US Geo** `us-standard`                        / `us-vault`      / `us-cold`      / `us-smart` <br>
&emsp;&emsp;  **US East** `us-east-standard`                  / `us-east-vault` / `us-east-cold` / `us-east-smart` <br>
&emsp;&emsp;  **US South** `us-south-standard`                / `us-south-vault`/ `us-south-cold`/ `us-south-smart` <br>
&emsp;&emsp;  **EU Geo** `eu-standard`                        / `eu-vault`      / `eu-cold`      / `eu-smart` <br>
&emsp;&emsp;  **EU Great Britain** `eu-gb-standard`           / `eu-gb-vault`   / `eu-gb-cold`   / `eu-gb-smart` <br>
&emsp;&emsp;  **EU Germany** `eu-de-standard`                 / `eu-de-vault`   / `eu-de-cold`   / `eu-de-smart` <br>
&emsp;&emsp;  **EU Spain** `eu-es-standard`                   / `eu-es-vault`   / `eu-es-cold`   / `eu-es-smart` <br>
&emsp;&emsp;  **AP Geo** `ap-standard`                        / `ap-vault`      / `ap-cold`      / `ap-smart` <br>
&emsp;&emsp;  **AP Tokyo** `jp-tok-standard`                  / `jp-tok-vault`  / `jp-tok-cold`  / `jp-tok-smart`<br>
&emsp;&emsp;  **AP Osaka** `jp-osa-standard`                  / `jp-osa-vault`  / `jp-osa-cold`  / `jp-osa-smart`<br>
&emsp;&emsp;  **AP Australia** `au-syd-standard`              / `au-syd-vault`  / `au-syd-cold`  / `au-syd-smart` <br>
&emsp;&emsp;  **CA Toronto** `ca-tor-standard`                / `ca-tor-vault`  / `ca-tor-cold`  / `ca-tor-smart` <br>
&emsp;&emsp;  **Amsterdam** `ams03-standard`                  / `ams03-vault`   / `ams03-cold`   / `ams03-smart` <br>
&emsp;&emsp;  **Chennai** `che01-standard`                    / `che01-vault`   / `che01-cold`   / `che01-smart` <br>

&emsp;&emsp;  **Montréal** `mon01-standard`                   / `mon01-vault`   / `mon01-cold`   / `mon01-smart` <br>
&emsp;&emsp;  **Paris** `par01-standard`                      / `par01-vault`   / `par01-cold`   / `par01-smart` <br>
&emsp;&emsp;  **San Jose** `sjc04-standard`                   / `sjc04-vault`   / `sjc04-cold`   / `sjc04-smart` <br>
&emsp;&emsp;  **Singapore** `sng01-standard`                  / `sng01-vault`   / `sng01-cold`   / `sng01-smart` <br>


For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Using the REST API, Libraries, and SDKs
{: #classes-sdk}

Several new APIs have been introduced to the IBM COS SDKs to provide support for applications working with retention policies. Select a language (curl, Java, JavaScript, Go, or Python) at the beginning of this page to view examples that use the appropriate COS SDK.

 All code examples assume the existence of a client object that is called `cos` that can call the different methods. For details on creating clients, see the specific SDK guides.


### Create a bucket with a storage class
{: #classes-sdk-create-bucket}

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


```sh
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{: codeblock}
{: curl}

It isn't possible to change the storage class of a bucket once the bucket is created. If objects need to be reclassified, it's necessary to move the data to another bucket with the wanted storage class.
