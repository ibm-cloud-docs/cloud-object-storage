---

copyright:
  years: 2017, 2020
lastupdated: "2021-11-15"

keywords: faq, frequently asked questions, object storage

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# FAQ
{: #faq}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## API Questions
{: #faq-api}

### Are bucket names case-sensitive?
{: #faq-name-case}

Bucket names are required to be DNS addressable, and not case-sensitive.

### What is the maximum number of characters that can be used in a key, or Object name?
{: #faq-max-key}

Keys have a 1024-character limit.

### How can I find out the total size of my bucket by using the API?
{: #faq-bucket-size}

You can use the [Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration#returns-metadata-for-the-specified-bucket) to get the bytes used for a given bucket.

### Can I migrate data from AWS S3 into {{site.data.keyword.cos_full_notm}}?
{: #faq-migrate}

Yes, you can use your existing tools to read and write data into {{site.data.keyword.cos_full_notm}}. You need to configure HMAC credentials allow your tools to authenticate. Not all S3-compatible tools are currently unsupported. For more details, see [Using HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main).

### Can I use AWS S3 SDKs with IBM Cloud Object Storage?
{: #faq-aws-sdk}

Yes, IBM COS SDKs are based on the official AWS S3 API SDKs, but are modified to use IBM Cloud features like IAM, Key Protect, Immutable Object Storage, and others. When using these SDKs, you use HMAC authorization and an explicit endpoint. For details, refer to [About IBM COS SDKs](/docs/cloud-object-storage/basics?topic=cloud-object-storage-sdk-about).


## Offering Questions
{: #faq-offering}

### Is there a 100-bucket limit to an account? What happens if we need more?
{: #faq-bucket-limit}

Yes, 100 is the current bucket limit. Generally, prefixes are a better way to group objects in a bucket, unless the data needs to be in a different region or storage class. For example, to group patient records, you would use one prefix per patient. If this is not a workable solution and you require additional buckets, contact customer support.

### If I want to store my data by using {{site.data.keyword.cos_full_notm}} Vault or Cold Vault, do I need to create another account?
{: #faq-store-data}

No, storage classes (and regions as well) are defined at the bucket level. Simply create a new bucket that is set to the wanted storage class.

### When I create a bucket by using the API, how do I set the storage class?
{: #faq-bucket-class}

The storage class (for example, `us-smart`) is assigned to the `LocationConstraint` configuration variable for that bucket. This is because of a key difference between the way AWS S3 and {{site.data.keyword.cos_full_notm}} handle storage classes. {{site.data.keyword.cos_short}} sets storage classes at the bucket level, while AWS S3 assigns a storage class to an individual object. A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/cloud-object-storage?topic=cloud-object-storage-classes).

### Can the storage class of a bucket be changed? For example, if you have production data in 'standard', can we easily switch it to 'vault' for billing purposes if we are not using it frequently?
{: #faq-change-class}

Today changing of storage class requires manually moving or copying the data from one bucket to another bucket with the wanted storage class.

### Can the location of a bucket be changed?
{: #faq-change-loc}

Changing the location requires you to create a new bucket in the desired location then move existing data to that new bucket.

### What types of authentication can I use to access IBM Cloud Object Storage?
{: #faq-authenticate}

You can use an OAuth 2 token or an HMAC key for authentication. The HMAC key can be used for S3-compatible tools such as rclone, Cyberduck, and so on.
* Instructions to obtain an OAuth token are available in [Generating an IBM Cloud IAM token by using an API key](/docs/account?topic=account-iamtoken_from_apikey).
* Instructions to obtain the HMAC credentials are in Using [HMAC Credentials](/docs/cloud-object-storage/hmac?topic=cloud-object-storage-uhc-hmac-credentials-main).

See also [API Key vs HMAC](/docs/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac).

### How do cross-origin resource sharing (CORS) and a bucket firewall differ in limiting access to data?
{: #faq-cors}

CORS allows interactions between resources from different origins that is normally prohibited.  A bucket firewall allows access only to requests from a list of allowed IP addresses. For more information on CORS, review [What is CORS?](/docs/CDN?topic=CDN-cors-and-cors-requests-through-your-cdn#what-is-cors)

### Can I create more than one Object Storage service with a Lite account?
{: #faq-lite-storage}

If you have a Lite plan, you can provision only one instance of the service.

### What happens if I exceed the maximum usage allowed for a Lite plan?
{: #faq-lite-exceed}

Once you exceed the allowed usage, the service instance associated to the Lite plan becomes inaccessible.  You will receive a warning notification email with corrective steps. If you do not take action, the instance will be removed.



## Performance Questions
{: #faq-performance}

### Does data consistency in {{site.data.keyword.cos_short}} come with a performance impact?
{: #faq-consistency}

Consistency with any distributed system comes with a cost, as the efficiency of the {{site.data.keyword.cos_full_notm}} dispersed storage system is not trivial, but lower compared to systems with multiple synchronous copies.

### Aren't there performance implications if my application needs to manipulate large objects?
{: #faq-large}

For performance optimization, objects can be uploaded and downloaded in multiple parts, in parallel.


## Encryption Questions
{: #faq-encryption}

### Does {{site.data.keyword.cos_short}} provide encryption at rest and in motion?
{: #faq-encrypt-basics}

Yes. Data at rest is encrypted with automatic provider side Advanced Encryption Standard (AES) 256-bit encryption and Secure Hash Algorithm (SHA)-256 hash. Data in motion is secured by using built-in carrier grade Transport Layer Security/Secure Sockets Layer (TLS/SSL) or SNMPv3 with AES encryption.

If you want more control over encryption, you can make use of IBM Key Protect to manage generated or "bring your own" keying. For details, see [Key-protech COS Integration](https://cloud.ibm.com/docs/key-protect?topic=key-protect-integrate-cos)

### Is there additional encryption processing if a customer wants to encrypt their data?
{: #faq-encrypt-add}

Server-side encryption is always on for customer data. Compared to the hashing required in S3 authentication and the erasure coding, encryption is not a significant part of the processing cost of COS.

### Does {{site.data.keyword.cos_short}} encrypt all data?
{: #faq-encrypt-all}

Yes, {{site.data.keyword.cos_short}} encrypts all data.

### Does {{site.data.keyword.cos_short}} have FIPS 140-2 compliance for the encryption algorithms?
{: #faq-encrypt-fips}

Yes, IBM COS Federal offering is approved for FedRAMP Moderate Security controls, which require a validated FIPS configuration. IBM COS Federal is certified at FIPS 140-2 level 1. For more information on COS Federal Offering, [contact us](https://www.ibm.com/cloud/government) via our Federal site.

### Will client-key encryption be supported?
{: #faq-encrypt-client}

Yes, client-key encryption is supported by using SSE-C, Key Protect, or HPCS.

### How does IBM Cloud Object Storage destruct expired data?
{: #faq-expired}

Deletion of an object undergoes various stages to prevent data from being accessible (both before and after deletion).  Review [Data deletion](/docs/cloud-object-storage?topic=cloud-object-storage-security#security-deletion) for details.

## General questions
{: #faq-general}

### How many objects can fit in a single bucket?
{: #faq-single}

There is no practical limit to the number of objects in a single bucket.

### Can I nest buckets inside one another?
{: #faq-nest}

No, buckets cannot be nested. If a greater level of organization is required within a bucket, the use of prefixes is supported: `{endpoint}/{bucket-name}/{object-prefix}/{object-name}`. The object's key remains the combination `{object-prefix}/{object-name}`.

### What is the difference between 'Class A' and 'Class B' requests?
{: #faq-ab}

'Class A' requests are operations that involve modification or listing. This includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.'Class B' requests are those related to retrieving objects or their associated metadata/configurations from the system. There is no charge for deleting buckets or objects from the system.

### What is the best way to structure your data by using Object Storage such that you can 'look' at it and find what you are looking for? Without a directory structure, having 1000s of files at one level seems hard to view.
{: #faq-metadata}

You can use metadata that is associated with each object to find the objects you are looking for. The biggest advantage of Object Storage is the metadata that is associated with each object. Each object can have up to 4 MB of metadata in {{site.data.keyword.cos_short}}. When offloaded to a database, metadata provides excellent search capabilities. Many (key, value) pairs can be stored in 4 MB. You can also use Prefix searching to find what you are looking for. For example, if you use buckets to separate each customer data, you can use prefixes within buckets for organization. For example:  /bucket1/folder/object where 'folder/' is the prefix.

### Can you confirm that {{site.data.keyword.cos_short}} is ‘immediately consistent’, as opposed to ‘eventually consistent’?
{: #faq-immediate}

{{site.data.keyword.cos_short}} is ‘immediately consistent’ for data and ‘eventually consistent’ for usage accounting.


### Can {{site.data.keyword.cos_short}} partition the data automatically for me like HDFS, so I can read the partitions in parallel, for example, with Spark?
{: #faq-partition}

{{site.data.keyword.cos_short}} supports a ranged GET on the object, so an application can do a distributed striped read type operation. Doing the striping would be on the application to manage.

### Can I restore a bucket from a specific back-up file?

{: #faq-cos-backup}

It is possible to overwrite an existing bucket. Restore options depend on the capabilities provided by the back-up tool you use; check with your back-up provider.  As described in [Your responsibilities when using IBM Cloud Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-responsibilities), you are responsible for ensuring data back-ups if necessary. IBM Cloud Object Storage does not provide a back-up service.

### Can a web browser display the content of files stored in Cloud Object Storage?

{: #faq-cos-web}

Web browsers can display web content in Cloud Object Storage files, using the COS endpoint as the file location. To create a functioning website, however, you need to set up a web environment, for example elements such as a CNAME record. IBM Cloud Object Storage doesn't support automatic static website hosting.  For information, see [Static websites](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access#public-access-static-website) and this [tutorial](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).

### If I set an archive policy on an existing bucket, does the policy apply to existing files?

{: #faq-archive}

The policy applies to the new objects uploaded but does not affect existing objects on a bucket.  For details, refer to [Add or manage an archive policy on a bucket](https://cloud.ibm.com/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive#archive-add).

### Can I unzip a file after I upload it?

{: #faq-unzip}

A feature to unzip or decompress files is not part of the service.  For large data transfer, consider using Aspera high-speed transfer, multi-part uploads, or threads to manage multi-part uploads.  See [Store large objects](/docs/cloud-object-storage?topic=cloud-object-storage-large-objects).

### Can I create a bucket, in the same or different region, with a deleted bucket name?

{: #faq-reuse-name}

A bucket name can be reused as soon as 15 minutes after the contents of the bucket have been deleted and then the bucket has been deleted.  At this point, the objects and bucket are irrevocably deleted and **can not** be restored.

If the user does not first empty and then delete the bucket, and instead [deletes or schedules the {{site.data.keyword.cos_short}} service instance for deletion](/docs/cloud-object-storage?topic=cloud-object-storage-provision#deleting-a-service-instance), the bucket names will be held in reserve for a [default period of seven (7) days until the account reclamation process](/docs/cli?topic=cli-ibmcloud_commands_resource#ibmcloud_resource_reclamations) is completed. Until the reclamation process is complete it remains possible to restore the instance, along with the buckets and objects.  After reclamation is complete, all buckets and objects will be irrevocably deleted and **can not** be restored, although the bucket names will be made available for new buckets to reuse.

### Why do  ```CredentialRetrievalError``` occurs while uploading data to COS  or while retrieving credentials? 

{: #faq-credret-error}

```CredentialRetrievalError``` can occur due to the following reasons:
   * If the API key is not valid.
   * If the IAM endpoint is incorrect. 

      However, If the issue persists, reach out to IBM support.


### How to ensure communication with COS?
{: #faq-faq-error}

You can check  communication with  COS by one of the follwing:
* By using `COS API HEAD` call to a bucket that will return the headers for that bucket. See [api-head-bucket](https://cloud.ibm.com/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-head-bucket)

* By using SDK : See [ headbucket property](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#headBucket-property)


### Can we migrate bucket from one COS instance to another?
{: #faq-create-bucket}

Yes, You can achieve the same by creating a bucket in the target COS instance and perform a sync . For details see [cloud-object-storage-region-copy](
 https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-region-copy)

### My COS service is locked. How do I reactivate the COS service?

{: #faq-locked-account}

Exceeding the data limit for the Lite account is one of the reasons why your account is locked/deactived.\
[COS support](https://cloud.ibm.com/unifiedsupport/cases/form) team can help unlocking the account.


### After deleting a COS instance, is it possible to reuse the same bucket names that were part of the deleted COS instance?

{: #faq-resue-bucketname}

When we delete an empty bucket, the name of the bucket is held in reserve by the system for 10 minutes after the delete operation.  After 10 minutes the name is released for re-use.  

### How to archive/restore objects in COS?

{: #faq-restore-object}

Archived objects needs to be restored first before you can access them. While restoring, specify the time limit the objects should remain available before being re-archived. For details, see [archive-restore data ](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-archive)

### Can we enable COS replication between two different regions for DR purposes?

{: #faq-cos-replication}

COS by itself does not provide any replication feature. However IBM COS offers data resiliency.
For details, see [object-storage resiliency](https://www.ibm.com/cloud/object-storage/resiliency)


### How to track events in COS?

{: #faq-event-tracking}

The IBM Cloud Activity Tracker service records user-initiated activities that change the state of a service in IBM Cloud. For details see [IBM Cloud Activity Tracker](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-at-events)


### How to setup notifications when objects gets updated/written to a bucket?

{: #faq-notification-setup}


Use [Cloud Functions for object storage](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-functions) to setup an Event Source (notification.)


### Does COS have rate limits when writing/reading to/from buckets?

{: #faq-rate-limit}

Yes, COS has rate limiting. To know more, please reach out to [COS support](https://cloud.ibm.com/unifiedsupport/cases/form)


### Is COS HIPAA compliant to host PHI data?

{: #faq-hipaa}

Yes, COS is HIPAA  compliant.

### Is there any option in IBM COS to enable ```accelerate data transfer```?

{: #faq-accel-data}

IBM COS offers [**Aspera**]( https://www.ibm.com/cloud/object-storage/aspera) service for high speed data transfer.

### How do we compare various attributes of object in two different buckets?

{: #faq-comp-attributes}

Use [Rclone](https://rclone.org/commands/rclone_check), It enables to compare various attributes.

### What is the default retention period for buckets?

{: #faq-default-retention}

There is no default retention period applied. You can set it  while creating the bucket.


### Can we add a retention policy to an existing bucket?

{: #faq-add-retention}

Yes, [Retention policies](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-immutable#immutable-sdk-add-policy) can be added to an existing bucket, however, the retention period can only be extended. It cannot be decreased from the currently configured value.

### Why is there a "legal hold" concept on top of the "retention period"?

{: #faq-legal-hold}

A legal hold prevents an object from being overwritten or deleted. However, a legal hold doesn't have to be  associated with a retention period and remains in effect until legal hold is removed.  For more details, see [Legal hold and retention period](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-immutable#immutable-terminology-hold)


### How do we access private COS endpoint in a data center from an another date center?

{: #faq-access-pvt-cospoints}

Use IBM Cloud [Direct Link Connection]((https://cloud.ibm.com/docs/direct-link?topic=direct-link-using-ibm-cloud-direct-link-to-connect-to-ibm-cloud-object-storage)) to create a global direct link.


### How does frequency of data access impacts the pricing of COS?

{: #faq-access-price}

Storage cost for COS is determined by the total volume of data stored, the amount of public outbound bandwidth used, and the total number of operational requests processed by the system. For more details, see [cloud-object-storage-billing](https://cloud.ibm.com/docs/cloud-object-storage/iam?topic=cloud-object-storage-billing)


### What are the considerations for choosing the right storage class in COS?  

{: #faq-choose-storageclass}

You can choose the right storage class based on your requirement. For details information, see [billing-storage-classes](https://cloud.ibm.com/docs/cloud-object-storage/iam?topic=cloud-object-storage-billing#billing-storage-classes)

### How to invoke IBM Cloud Object Storage Bucket operations using cURL?

{: #faq-using-curl}

You can get the most out working with the command line in most environments with IBM® Cloud Object Storage and cURL, however using curl assumes a certain amount of familiarity with the command line and Object Storage, For details, refer [ Using cURL](https://cloud.ibm.com/docs/cloud-object-storage/cli?topic=cloud-object-storage-curl)%


### Is encryption applied to a bucket by default?
{: #faq-default-enc}

Yes, by default, all objects stored in COS are encrypted by using randomly generated keys and an all-or-nothing-transform (AONT). We can get the encryption details using IBM Cloud UI/CLI.
For details see [Cloud Storage Encryption](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-encryption)


### How to list all permissions of a bucket? 
{: #faq-bucket-permison}

There is an IAM feature that can create a report at the instance level which may extend to their buckets. It doesn’t specifically report at the bucket level. For details, see [Account Access Report](https://cloud.ibm.com/docs/account?topic=account-access-report)

 
### How to monitor bucket changes in public cloud without using cloud function?
{: #faq-Cloud-Function}

The only way to get notifications for object changes is using Cloud Functions.


### How to monitor COS resource?
{: #faq-monitor-cos-res}

Use the Activity Tracker service to capture a record IBM Cloud activities and monitor the activity of your IBM Cloud account. Activity  Tracker is used to track how users and applications interact with IBM Cloud Object Storage (COS).
 
 
### Does the object in a bucket  gets overwritten, if the same objet name is used again in the same bucket?
{: #faq-obj-overwrite}
Yes, it overwrites.
 
 
### How to get bucket information without using web console? 
{: #faq-bucketinfo-webconsole}

Use the COS Resource Configuration API to get bucket information.
For details, see [COS configuration](https://cloud.ibm.com/apidocs/cos/cos-configuration#returns-metadata-for-the-specified-bucket) and [COS Integration](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration)

 
### How to  manage service credentials for a COS instances?
{: #faq-mnge-cosinst}

When a service credential is created, the underlying Service ID is granted a role on the entire instance of Object Storage. To know more, refer [Managing Service credentials](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials)


 
### Is there a way to enable Key Protect to a COS bucket after the bucket is created?
{: #faq-vucket-keypro}

No, It's not possible to add Key Protect after creating a bucket. It can only be  added while creating the bucket.


### How do we move the data into archive tier?
{: #faq-archive tier}

You can archive objects using the web console, REST API, and 3rd party tools that are integrated with IBM Cloud Object Storage. See [COS Archive](https://cloud.ibm.com/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive)
 

### Can we use the  same COS instance across multiple regions?
{: #faq-cosinstance-multiplereg}

Yes, Cos instance is a global service. Once an instance is created,  you can choose the region while creating the bucket.

 
### Are the file scanned for viruses, while being uploaded to COS?
{: #faq-file-scan}

No, the files are not scanned when uploading to COS.
When an  object is uploaded, You get upload events  via activity tracker or a Cloud Functions. thereby you can download, scan, and re upload the object.

### Is it possible to form a hadoop cluster using COS?
{: #faq-hadoop-cluster}

No, COS is for object storage service and for a Hadoop cluster, you need processing associated with each unit of storage. You may consider Hadoop-as-a-Service setup.


### Is there a way to generate a "Presigned URL" to download a file? review?
{: #faq-preassign-url}

We cannot generate PreSigned RUL using  IBM Cloud UI, however you can use CyberDuck to generate the “pre-signed URL”. it is free. 
Slack reference:  https://ibm-cloudplatform.slack.com/archives/C0VJSU370/p1606859253294800


### How to generate Auth Token using the IAM API Key using REST?
{: #faq-genrt-auth-token}

For more information on working with API, see [Creating IAM token for API Key](https://cloud.ibm.com/docs/account?topic=account-iamtoken_from_apikey) and [Congfiguration Authentication](https://cloud.ibm.com/apidocs/cos/cos-configuration#authentication)


### How to use COS web console to download/upload large objects?
{: #faq-large-object}

You have to either use IBM Cloud CLI/API to download large objects . Alternatively, plugins  like Aspera /rclone can also be used.


### What are the libraries that IBM COS  SDK supports?
{: #faq-library-support}

IBM COS provides SDKs for Java, Python, NodeJS, and Go featuring capabilities to make the most of IBM Cloud Object Storage. To know freatures supported by each SDK, see [Feature List](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-sdk-about)


### When a file is uploaded to a cross region bucket using the ‘us-geo’ endpoint, how long is the delay before the file is available at the other US sites?
{: #faq-time-req}

The data is spread immediately without delay and the uploaded files are available  once the write is successful.
   
### How do we access the reclaimed resources?
{: #faq-reclaimed-resource}

You have to create a new set of credentials for accessing the restored resources.


### Can we host a website using COS bucket?
{: #faq-static-website}

Yes, you can use COS bucket to host static website. For details, see [Hosting Website using COS](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial)
 
 
### Are REST, cURL commands supported for COS bucket creation using HMAC credentials?
{: #faq-using-hmac}

Yes, you need to set an authorization header. For details, see [Using HMAC Signature](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-hmac-signature)


### What kind of IAM authorization is required to edit a bucket's authorized IPs list? 
{: #faq-author-iplist}
A user  must have 'Manager' privilege on the bucket to manage the firewall and to set authorization.

 
### Is there a way to convert single region COS bucket to cross region without having to copy objects over again?
{: #faq-singleregion-to-multiregion}
No,  it is not possible. You have to copy objects to the target bucket. For details, see [COS Region Copy](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-region-copy)

 
### Is there a way to verify an object’s integrity during upload to COS?
{: #faq-data-integrity}

COS support object integrity and ensure that the payload is not altered during transit. 



### How to set a notification when usage in a COS instance gets near a certain billing amount?
{: #faq-quota-billing}

You can use bucket quote feature by integrating with sysdig and configure for notifications. For details, see [Using Bucket Quota](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-quota)
 
