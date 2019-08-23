---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-22"

keywords: faq, frequently asked questions, object storage

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}

# FAQ
{: #faq}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{:shortdesc: .shortdesc}

## API Questions
{: #faq-api}

**Are {{site.data.keyword.cos_notm}} bucket names case sensitive?**

Bucket names are required to be DNS addressable, and thus not case sensitive.

**What is the maximum number of characters that can be used in an Object name?**

1024

**How can I find out the total size of my bucket using the API?**

It's not possible to fetch the size of a bucket with a single request. You'll need to list the contents of a bucket and sum the size of each object.

**Can I migrate data from AWS S3 into {{site.data.keyword.cos_full_notm}}?**

Yes, you can use your existing tools to read and write data into {{site.data.keyword.cos_full_notm}}. You will need to configure HMAC credentials allow your tools to authenticate. Not all S3-compatible tools are currently unsupported. For more details, see [Using HMAC credentials](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).


## Offering Questions
{: #faq-offering}

**Is there a 100-bucket limit to an account?  What happens if we need more?**

Yes, 100 is the current bucket limit. Generally, prefixes are a better way to group together objects in a bucket, unless the data needs to be in a different region or storage class. For example, to group patient records, you would use one prefix per patient. If this is not a workable solution, contact customer support.

**If I want to store my data using {{site.data.keyword.cos_full_notm}} Vault or Cold Vault, do I need to create another account?**

No, storage classes (and regions as well) are defined at the bucket level. Simply create a new bucket that is set to the desired storage class.

**When I create a bucket using the API, how do I set the storage class?**

The storage class (eg `us-flex`) is assigned to the `LocationConstraint` configuration variable for that bucket. This is because of a key difference between the way AWS S3 and {{site.data.keyword.cos_full_notm}} handle storage classes. {{site.data.keyword.cos_short}} sets storage classes at the bucket level, while AWS S3 assigns a storage class to an individual object. A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes).

**Can the storage class of a bucket be changed?  For example, if you have production data in 'standard', can we easily switch it to 'vault' for billing purposes if we are not using it frequently?**

Today changing of storage class requires manually moving or copying the data from one bucket to another bucket with the desired storage class.


## Performance Questions
{: #faq-performance}

**Does data consistency in {{site.data.keyword.cos_short}} come with a performance impact?**

Consistency with any distributed system comes with a cost, but the efficiency of the {{site.data.keyword.cos_full_notm}} dispersed storage system is higher, and overhead is lower compared to systems with multiple synchronous copies.

**Aren't there performance implications if my application needs to manipulate large objects?**

For performance optimization, objects can be uploaded and downloaded in multiple parts, in parallel.


## Encryption Questions
{: #faq-encryption}

**Does {{site.data.keyword.cos_short}} provide encryption at rest and in motion?**

Yes. Data at rest is encrypted with automatic provider side Advanced Encryption Standard (AES) 256-bit encryption and Secure Hash Algorithm (SHA)-256 hash. Data in motion is secured by using built-in carrier grade Transport Layer Security/Secure Sockets Layer (TLS/SSL) or SNMPv3 with AES encryption.

**What is the typical encryption overhead if a customer wants to encrypt their data?**

Server side encryption is always on for customer data. Compared to the hashing required in S3 authentication and the erasure coding, encryption is not a big part of the processing cost of COS.

**Does {{site.data.keyword.cos_short}} encrypt all data?**

Yes, {{site.data.keyword.cos_short}} encrypts all data.

**Does {{site.data.keyword.cos_short}} have FIPS 140-2 compliance for the encryption algorithms?**

Yes, IBM COS Federal offering is approved for FedRAMP Moderate Security controls which require a validated FIPS configuration. IBM COS Federal is certified at FIPS 140-2 level 1. For more information on COS Federal Offering, please [contact us](https://www.ibm.com/cloud/government) via our Federal site.

**Will client-key encryption be supported?**

Yes, client-key encryption is supported using SSE-C or Key Protect.

## General questions
{: #faq-general}

**How many objects can fit in a single bucket?**

There is no practical limit to the number of objects in a single bucket.

**Can I nest buckets inside one another?**

No, buckets can not be nested. If a greater level of organization is required within a bucket, the use of prefixes is supported: `{endpoint}/{bucket-name}/{object-prefix}/{object-name}`. Note that the object's key remains the combination `{object-prefix}/{object-name}`.

**What is the difference between 'Class A' and 'Class B' requests?**

'Class A' requests are operations that involve modification or listing. This includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.'Class B' requests are those related to retrieving objects or their associated metadata/configurations from the system. There is no charge for deleting buckets or objects from the system.

**What is the best way to structure your data using Object storage such that you can 'look' at it and find what you are looking for?  Without a directory structure, having 1000s of files at one level seems hard to view.**

You can use metadata associated with each object to find the objects you are looking for. The biggest advantage of object storage is the metadata associated with each object. Each object can have up to 4 MB of metadata in {{site.data.keyword.cos_short}}. When offloaded to a database, metadata provides excellent search capabilities. A large number of (key, value) pairs can be stored in 4 MB. You can also use Prefix searching to find what you are looking for. For example, if you use buckets to separate each customer data, you can use prefixes within buckets for organization. For example:  /bucket1/folder/object where 'folder/' is the prefix.

**Can you confirm that {{site.data.keyword.cos_short}} is ‘immediately consistent’, as opposed to ‘eventually consistent’?**

{{site.data.keyword.cos_short}} is ‘immediately consistent’ for data and ‘eventually consistent’ for usage accounting.


**Can {{site.data.keyword.cos_short}} partition the data automatically for me like HDFS, so I can read the partitions in parallel, e.g. with Spark?**

{{site.data.keyword.cos_short}} supports a ranged GET on the object, so an application can do a distributed striped read type operation. Doing the striping would be on the application to manage.
