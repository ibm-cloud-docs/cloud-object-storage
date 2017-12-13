---

copyright:
  years: 2017
lastupdated: "2017-11-05"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Use storage classes

Not all data needs feeds active workloads.  Archival data may sit untouched for long periods of time.  For less active workloads, you can create buckets with different storage classes.  Objects stored in these buckets will incur charges on a different schedule than standard storage.

## What are the classes?

There are four storage classes:

*  **Standard**: Used for active workloads - there is no charge for data retrieved (besides the cost of the operational request itself and public outbound bandwidth).
*  **Vault**: Used for cool workloads where data is not accessed frequently - a retrieval charge applies for reading data. The service includes a threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault**: Used for cold workloads where data is primarily archived (accessed every 90 days or less) - a larger  retrieval charge applies for reading data. The service includes a threshold for object size and storage period consistent with the intended use of this service: storing cold, inactive data.
*  **Flex**: Used for dynamic workloads where access patterns are more difficult to predict. Depending on usage, if the lower costs of cooler storage combined with retrieval charges exceeds a cap value, then the storage charge increases and no any retrieval charges apply. If the data isn't accessed frequently, Flex storage can be more cost effective than Standard storage, and if cooler usage patterns become more active Flex storage is more cost effective than Vault or Cold Vault storage. No threshold object size or storage period applies to Flex buckets.

For pricing details please see [the pricing table at ibm.com](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage#s3api).

For information on how to create buckets with different storage classes, please see the [API reference](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html#create-a-bucket-with-a-different-storage-class).

## How do I create a bucket with a different storage class?

When creating a bucket in the console, there is a drop-down menu that allows for storage class selection.

When creating buckets programatically, it is necessary to specify a `LocationConstraint`. Valid provisioning codes for `LocationCostraint` are: <br>
&emsp;&emsp;  `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>

A request to create a Cold Vault bucket in the US Cross Region would look like:

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration><LocationConstraint>us-cold</LocationConstraint></CreateBucketConfiguration>"
```
{:codeblock}

It is not possible to change the storage class of a bucket once the bucket is created.  If objects need to be reclassified, it is necessary to move the data to another bucket with the desired storage class.
