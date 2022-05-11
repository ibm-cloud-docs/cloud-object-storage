---

copyright:
  years: 2017, 2021
lastupdated: "2021-11-29"

keywords: faq, frequently asked questions, object storage

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ
{: #faq}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## Why can I not create or delete a service instance?
{: #faq-instance-create-delete}
{: faq}

A user is required to have have at a minimum the platform role of `editor` for all IAM enabled services, or at least for Cloud Object Service. For more information, see the [IAM documentation on roles](/docs/account?topic=account-iam-service-roles-actions).

## Which one of my instances uses a Lite plan?
{: #faq-instance-find-lite}
{: faq}

An account is limited to a single instance of {{site.data.keyword.cos_full_notm}} that uses a Lite plan.  You can find this instance three different ways:

1. Navigate to the [catalog](https://cloud.ibm.com/objectstorage/create) and attempt to make a new Lite instance.  An error will pop up prompting you to delete the existing instance, and provides a link to the current Lite instance.
2. Navigate to the storage section of the resource list, and click on any area of blank space to select an instance of {{site.data.keyword.cos_short}}. An informational sidebar will appear and provide the plan name: either Lite or Standard.
3. Use the CLI to search for the resource:

```sh
ibmcloud resource search "service_name:cloud-object-storage AND 2fdf0c08-2d32-4f46-84b5-32e0c92fffd8"
```

## How do I upgrade a service instance from Lite to Standard?
{: #faq-instance-upgrade}
{: faq}

Typically, this is easily done from the {{site.data.keyword.cos_full_notm}} console by clicking on **Plan** in the navigation menu, located after **Connections**.

In cases where the instance has been locked due to exceeding the maximum allowed size of a Lite instance it may be necessary to use the CLI. The plan ID for a standard {{site.data.keyword.cos_short}} instance is `744bfc56-d12c-4866-88d5-dac9139e0e5d` (if curious, this can be found by issuing the CLI command `ic catalog service cloud-object-storage`).  You'll need to know the name of the instance you are trying to upgrade.  For example, to upgrade the instance "My Object Storage", you can issue the command:

```sh
ic resource service-instance-update "My Object Storage" --service-plan-id 744bfc56-d12c-4866-88d5- dac9139e0e5d
```

## Are bucket names case-sensitive?
{: #faq-name-case}
{: faq}

Bucket names are required to be DNS addressable and are not case-sensitive.

## What is the maximum number of characters that can be used in a key, or Object name?
{: #faq-max-key}
{: faq}

Keys have a 1024-character limit.

## What are some tools unable to render object names?
{: #faq-xml-error}
{: faq}

Object names that contain unicode characters that are not allowed by the XML standard will result in "Malformed XML" messages. For more information, see [the XML reference documentation](https://www.w3.org/TR/xml/#charsets).

## How can I find out the total size of my bucket by using the API?
{: #faq-bucket-size}
{: faq}

You can use the [Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration#returns-metadata-for-the-specified-bucket) to get the bytes used for a given bucket.

## How can I view my buckets?
{: #faq-bucket-view}
{: faq}

You can view and navigate your buckets using the console, CLI or the API.

For example, the CLI command `ibmcloud cos buckets` will list all buckets associated with the targeted service instance.

## Can I migrate data from AWS S3 into {{site.data.keyword.cos_full_notm}}?
{: #faq-migrate}
{: faq}

Yes, you can use your existing tools to read and write data into {{site.data.keyword.cos_full_notm}}. You need to configure HMAC credentials allow your tools to authenticate. Not all S3-compatible tools are currently unsupported. For details, see [Using HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main).

## Can I use AWS S3 SDKs with IBM Cloud Object Storage?
{: #faq-aws-sdk}
{: faq}

Yes, IBM COS SDKs are based on the official AWS S3 API SDKs, but are modified to use IBM Cloud features, such as IAM, Key Protect, Immutable Object Storage, and others. When using these SDKs, use HMAC authorization and an explicit endpoint. For details, see [About IBM COS SDKs](/docs/cloud-object-storage/basics?topic=cloud-object-storage-sdk-about).

## Is there a 100-bucket limit to an account? What happens if I need more?
{: #faq-bucket-limit}
{: faq}

Yes, 100 is the current bucket limit. Generally, prefixes are a better way to group objects in a bucket, unless the data needs to be in a different region or storage class. For example, to group patient records, you would use one prefix per patient. If this is not a workable solution and you require additional buckets, contact IBM customer support.

## If I want to store my data by using {{site.data.keyword.cos_full_notm}} Vault or Cold Vault, do I need to create another account?
{: #faq-store-data}
{: faq}

No, storage classes (and regions as well) are defined at the bucket level. Simply create a new bucket that is set to the wanted storage class.

## When I create a bucket by using the API, how do I set the storage class?
{: #faq-bucket-class}
{: faq}

The storage class (for example, `us-smart`) is assigned to the `LocationConstraint` configuration variable for that bucket. This is because of a key difference between the way AWS S3 and {{site.data.keyword.cos_full_notm}} handle storage classes. {{site.data.keyword.cos_short}} sets storage classes at the bucket level, while AWS S3 assigns a storage class to an individual object. For a list of valid provisioning codes for `LocationConstraint`, see [the Storage Classes guide](/docs/cloud-object-storage?topic=cloud-object-storage-classes).

## Can the storage class of a bucket be changed? For example, if you have production data in 'standard', can we easily switch it to 'vault' for billing purposes if we are not using it frequently?
{: #faq-change-class}
{: faq}

You can change the storage class by manually moving or copying the data from one bucket to another bucket with the wanted storage class.

## Can the location of a bucket be changed?
{: #faq-change-loc}
{: faq}
 
To change a location, create a new bucket in the desired location and move existing data to the new bucket.

## What types of authentication can I use to access {{site.data.keyword.cos_full}}?
{: #faq-authenticate}
{: faq}
 
You can use an OAuth 2 token or an HMAC key for authentication. The HMAC key can be used for S3-compatible tools such as rclone, Cyberduck, and others.
* For instructions to obtain an OAuth token, see [Generating an IBM Cloud IAM token by using an API key](/docs/account?topic=account-iamtoken_from_apikey).
* For instructions to obtain the HMAC credentials, see [Using HMAC Credentials](/docs/cloud-object-storage/hmac?topic=cloud-object-storage-uhc-hmac-credentials-main).

Also, see [API Key vs HMAC](/docs/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac).

## How do cross-origin resource sharing (CORS) and a bucket firewall differ in limiting access to data?
{: #faq-cors}
{: faq}
 
CORS allows interactions between resources from different origins that are normally prohibited. A bucket firewall allows access only to requests from a list of allowed IP addresses. For more information on CORS, see [What is CORS?](/docs/CDN?topic=CDN-cors-and-cors-requests-through-your-cdn#what-is-cors).

## How do I allow Aspera High-Speed Transfer through a bucket with context-based restrictions or a firewall?
{: #faq-aspera-ip}
{: faq}
 
The full list (in JSON) of Aspera High-Speed Transfer IP addresses that are used with {{site.data.keyword.cos_full_notm}} can be found [using this API endpoint](https://ats.aspera.io/pub/v1/servers/softlayer).

## Can I create more than one Object Storage service with a Lite account?
{: #faq-lite-storage}
{: faq}
 
If you have a Lite plan, you can provision only one instance of the service.

## What happens if I exceed the maximum usage allowed for a Lite plan?
{: #faq-lite-exceed}
{: faq}
 
Once you exceed the allowed usage, the service instance associated with the Lite plan becomes inaccessible.  You will receive a warning notification email with corrective steps. If you do not take action, the instance is removed.

## Does data consistency in {{site.data.keyword.cos_short}} come with a performance impact?
{: #faq-consistency}
{: faq}
 
Consistency with any distributed system comes with a cost, because the efficiency of the {{site.data.keyword.cos_full_notm}} dispersed storage system is not trivial, but is lower compared to systems with multiple synchronous copies.

## Aren't there performance implications if my application needs to manipulate large objects?
{: #faq-large}
{: faq}
 
For performance optimization, objects can be uploaded and downloaded in multiple parts, in parallel.

## Does {{site.data.keyword.cos_short}} provide encryption at rest and in motion?
{: #faq-encrypt-basics}
{: faq}
 
Yes. Data at rest is encrypted with automatic provider-side Advanced Encryption Standard (AES) 256-bit encryption and the Secure Hash Algorithm (SHA)-256 hash. Data in motion is secured by using the built-in carrier grade Transport Layer Security/Secure Sockets Layer (TLS/SSL) or SNMPv3 with AES encryption.

If you want more control over encryption, you can make use of IBM Key Protect to manage generated or "bring your own" keying. For details, see [Key-protect COS Integration](/docs/key-protect?topic=key-protect-integrate-cos).

## Is there additional encryption processing if a customer wants to encrypt their data?
{: #faq-encrypt-add}
{: faq}

Server-side encryption is always on for customer data. Compared to the hashing required in S3 authentication and the erasure coding, encryption is not a significant part of the processing cost of {{site.data.keyword.cos_short}}.

## Does {{site.data.keyword.cos_short}} encrypt all data?
{: #faq-encrypt-all}
{: faq}

Yes, {{site.data.keyword.cos_short}} encrypts all data.

## Does {{site.data.keyword.cos_short}} have FIPS 140-2 compliance for the encryption algorithms?
{: #faq-encrypt-fips}
{: faq}

Yes, the IBM COS Federal offering is approved for FedRAMP Moderate Security controls, which require a validated FIPS configuration. IBM COS Federal is certified at FIPS 140-2 level 1. For more information on COS Federal offering, [contact us](https://www.ibm.com/cloud/government) via our Federal site.

## Will client-key encryption be supported?
{: #faq-encrypt-client}
{: faq}

Yes, client-key encryption is supported by using SSE-C, Key Protect, or HPCS.

## How does {{site.data.keyword.cos_full}} delete expired data?
{: #faq-expired}
{: faq}

Deletion of an object undergoes various stages to prevent data from being accessible (both before and after deletion). For details, see [Data deletion](/docs/cloud-object-storage?topic=cloud-object-storage-security#security-deletion).

## How many objects can fit in a single bucket?
{: #faq-single}
{: faq}

There is no practical limit to the number of objects in a single bucket.

## Can I nest buckets inside one another?
{: #faq-nest}
{: faq}

No, buckets cannot be nested. If a greater level of organization is required within a bucket, the use of prefixes is supported: `{endpoint}/{bucket-name}/{object-prefix}/{object-name}`. The object's key remains the combination `{object-prefix}/{object-name}`.

## What is the difference between 'Class A' and 'Class B' requests?
{: #faq-ab}
{: faq}

'Class A' requests are operations that involve modification or listing. This includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.'Class B' requests are those related to retrieving objects or their associated metadata/configurations from the system. There is no charge for deleting buckets or objects from the system.

## What is the best way to structure your data by using Object Storage so you can 'look' at it and find what you are looking for? Without a directory structure, having thousands of files at one level is difficult to view.
{: #faq-metadata}
{: faq}

You can use metadata that is associated with each object to find the objects you are looking for. The biggest advantage of Object Storage is the metadata that is associated with each object. Each object can have up to 4 MB of metadata in {{site.data.keyword.cos_short}}. When offloaded to a database, metadata provides excellent search capabilities. Many (key, value) pairs can be stored in 4 MB. You can also use Prefix searching to find what you are looking for. For example, if you use buckets to separate each customer data, you can use prefixes within buckets for organization. For example:  /bucket1/folder/object where 'folder/' is the prefix.

## Can you confirm that {{site.data.keyword.cos_short}} is ‘immediately consistent’, as opposed to ‘eventually consistent’?
{: #faq-immediate}
{: faq}

{{site.data.keyword.cos_short}} is ‘immediately consistent’ for data and ‘eventually consistent’ for usage accounting.

## Can {{site.data.keyword.cos_short}} partition the data automatically using HDFS, so I can read the partitions in parallel, for example, with Spark?
{: #faq-partition}
{: faq}

{{site.data.keyword.cos_short}} supports a ranged GET on the object, so an application can do a distributed striped-read-type operation. Doing the striping is managed by the application.

## Can I restore a bucket from a specific back-up file?
{: #faq-cos-backup}
{: faq}

It is possible to overwrite an existing bucket. Restore options depend on the capabilities provided by the back-up tool you use; check with your back-up provider. As described in [Your responsibilities when using IBM Cloud Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-responsibilities), you are responsible for ensuring data back-ups if necessary. {{site.data.keyword.cos_full}} does not provide a back-up service.

## Can a web browser display the content of files stored in IBM Cloud Object Storage?
{: #faq-cos-web}
{: faq}

Web browsers can display web content in IBM Cloud Object Storage files, using the COS endpoint as the file location. To create a functioning website, however, you need to set up a web environment; for example, elements such as a CNAME record. IBM Cloud Object Storage does not support automatic static website hosting.  For information, see [Static websites](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access#public-access-static-website) and this [tutorial](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).

## If I set an archive policy on an existing bucket, does the policy apply to existing files?
{: #faq-archive}
{: faq}

The policy applies to the new objects uploaded but does not affect existing objects on a bucket. For details, see [Add or manage an archive policy on a bucket](/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive#archive-add).

## Can I unzip a file after I upload it?
{: #faq-unzip}
{: faq}

A feature to unzip or decompress files is not part of the service. For large data transfer, consider using Aspera high-speed transfer, multi-part uploads, or threads to manage multi-part uploads. See [Store large objects](/docs/cloud-object-storage?topic=cloud-object-storage-large-objects).

## Can I create a bucket, in the same or different region, with a deleted bucket name?
{: #faq-reuse-name}
{: faq}

A bucket name can be reused as soon as 15 minutes after the contents of the bucket have been deleted and the bucket has been deleted.  Then, the objects and bucket are irrevocably deleted and **can not** be restored.

If you do not first empty and then delete the bucket, and instead [delete or schedule the {{site.data.keyword.cos_short}} service instance for deletion](/docs/cloud-object-storage?topic=cloud-object-storage-provision#deleting-a-service-instance), the bucket names will be held in reserve for a [default period of seven (7) days until the account reclamation process](/docs/cli?topic=cli-ibmcloud_commands_resource#ibmcloud_resource_reclamations) is completed. Until the reclamation process is complete, it is possible to restore the instance, along with the buckets and objects. After reclamation is complete, all buckets and objects will be irrevocably deleted and **can not** be restored, although the bucket names will be made available for new buckets to reuse.

## Why do ```CredentialRetrievalError``` occur while uploading data to {{site.data.keyword.cos_short}} or while retrieving credentials? 
{: #faq-credret-error}
{: faq}

```CredentialRetrievalError``` can occur due to the following reasons:

* The API key is not valid.
* The IAM endpoint is incorrect. 

However, if the issue persists, contact IBM customer support.

## How can I ensure communication with {{site.data.keyword.cos_short}}?
{: #faq-faq-error}
{: faq}

You can check the communication with {{site.data.keyword.cos_short}} by using one of the following:
* Use a `COS API HEAD` call to a bucket that will return the headers for that bucket. See [api-head-bucket](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-head-bucket).

* Use SDK : See [headbucket property](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#headBucket-property).

## Can I migrate a bucket from one COS instance to another?
{: #faq-create-bucket}
{: faq}

Yes, You can achieve the same by creating a bucket in the target {{site.data.keyword.cos_short}} instance and perform a sync. For details see [cloud-object-storage-region-copy](
 /docs/cloud-object-storage?topic=cloud-object-storage-region-copy).

## My COS service is locked. How do I reactivate the COS service?
{: #faq-locked-account}
{: faq}

Exceeding the data limit for the Lite account is one of the reasons why your account is locked or deactived. The [COS support](https://cloud.ibm.com/unifiedsupport/cases/form) team can help to unlock your account.

## After deleting a {{site.data.keyword.cos_short}} instance, is it possible to reuse the same bucket names that were part of the deleted COS instance?
{: #faq-resue-bucketname}
{: faq}

When an empty bucket is deleted, the name of the bucket is held in reserve by the system for 10 minutes after the delete operation.  After 10 minutes the name is released for re-use.

## How can I archive and restore objects in {{site.data.keyword.cos_short}}?
{: #faq-restore-object}
{: faq}

Archived objects must be restored before you can access them. While restoring, specify the time limit the objects should remain available before being re-archived. For details, see [archive-restore data](/docs/cloud-object-storage?topic=cloud-object-storage-archive).

## Can I enable {{site.data.keyword.cos_short}} replication between two different regions for DR purposes?
{: #faq-cos-replication}
{: faq}

COS by itself does not provide any replication feature. However {{site.data.keyword.cos_short}} offers data resiliency. For details, see [object-storage resiliency](https://www.ibm.com/cloud/object-storage/resiliency).

## How can I track events in {{site.data.keyword.cos_short}}?
{: #faq-event-tracking}
{: faq}

The {{site.data.keyword.cos_short}} Activity Tracker service records user-initiated activities that change the state of a service in {{site.data.keyword.cos_short}}. For details, see [IBM Cloud Activity Tracker](/docs/cloud-object-storage?topic=cloud-object-storage-at-events).

## How can I setup notifications when objects are updated or written to a bucket?
{: #faq-notification-setup}
{: faq}

Use [Cloud Functions for object storage](/docs/cloud-object-storage?topic=cloud-object-storage-functions) to setup an Event Source (notification).

## Does {{site.data.keyword.cos_short}} have rate limits when writing to or reading from buckets?
{: #faq-rate-limit}
{: faq}

Yes, {{site.data.keyword.cos_short}} has rate limiting. For details, see [COS support](https://cloud.ibm.com/unifiedsupport/cases/form).

## Is {{site.data.keyword.cos_short}} HIPAA compliant to host PHI data?
{: #faq-hipaa}
{: faq}

Yes, {{site.data.keyword.cos_short}} is HIPAA compliant.

## Is there any option in {{site.data.keyword.cos_short}} to enable ```accelerate data transfer```?
{: #faq-accel-data}
{: faq}

{{site.data.keyword.cos_short}} offers [**Aspera**](https://www.ibm.com/cloud/object-storage/aspera) service for high speed data transfer.

## How can I compare various attributes of an object in two different buckets?
{: #faq-comp-attributes}
{: faq}

Use [Rclone](https://rclone.org/commands/rclone_check). It enables you to compare various attributes.

## What is the default retention period for buckets?
{: #faq-default-retention}
{: faq}

There is no default retention period applied. You can set it while creating the bucket.

## Can we add a retention policy to an existing bucket?
{: #faq-add-retention}
{: faq}

Yes, [Retention policies](/docs/cloud-object-storage?topic=cloud-object-storage-immutable#immutable-sdk-add-policy) can be added to an existing bucket; however, the retention period can only be extended. It cannot be decreased from the currently configured value.

## Why is there a "legal hold" concept on top of the "retention period"?
{: #faq-legal-hold}
{: faq}

A legal hold prevents an object from being overwritten or deleted. However, a legal hold does not have to be associated with a retention period and remains in effect until the legal hold is removed. For details, see [Legal hold and retention period](/docs/cloud-object-storage?topic=cloud-object-storage-immutable#immutable-terminology-hold).

## How can I access a private COS endpoint in a data center from another date center?
{: #faq-access-pvt-cospoints}
{: faq}

Use {{site.data.keyword.cos_short}} [Direct Link Connection]((/docs/direct-link?topic=direct-link-using-ibm-cloud-direct-link-to-connect-to-ibm-cloud-object-storage)) to create a global direct link.

## How does frequency of data access impact the pricing of {{site.data.keyword.cos_short}}?
{: #faq-access-price}
{: faq}

Storage cost for {{site.data.keyword.cos_short}} is determined by the total volume of data stored, the amount of public outbound bandwidth used, and the total number of operational requests processed by the system. For details, see [cloud-object-storage-billing](/docs/cloud-object-storage/iam?topic=cloud-object-storage-billing).

## What are the considerations for choosing the correct storage class in {{site.data.keyword.cos_short}}?  
{: #faq-choose-storageclass}
{: faq}

You can choose the correct storage class based on your requirement. For details, see [billing-storage-classes](/docs/cloud-object-storage/iam?topic=cloud-object-storage-billing#billing-storage-classes).

## How to invoke IBM Cloud Object Storage bucket operations using cURL?
{: #faq-using-curl}
{: faq}

You have the most power by using the command line in most environments with IBM Cloud Object Storage and cURL. However using cURL assumes a certain amount of familiarity with the command line and Object Storage. For details, see [Using cURL](/docs/cloud-object-storage/cli?topic=cloud-object-storage-curl).

## Is encryption applied to a bucket by default?
{: #faq-default-enc}
{: faq}

Yes, by default, all objects stored in {{site.data.keyword.cos_short}} are encrypted using randomly generated keys and an all-or-nothing-transform (AONT). You can get the encryption details using IBM Cloud UI/CLI. For details, see [Cloud Storage Encryption](/docs/cloud-object-storage?topic=cloud-object-storage-encryption).

## How can I list all permissions of a bucket? 
{: #faq-bucket-permison}
{: faq}

The IAM feature creates a report at the instance level which may extend to their buckets. It does not specifically report at the bucket level. For details, see [Account Access Report](/docs/account?topic=account-access-report).

## How can I monitor bucket changes in the public cloud without using the cloud functions?
{: #faq-Cloud-Function}
{: faq}

You must use cloud functions to get notifications for object changes.

## How can I monitor {{site.data.keyword.cos_short}} resources?
{: #faq-monitor-cos-res}
{: faq}

Use the Activity Tracker service to capture and record {{site.data.keyword.cos_short}} activities and monitor the activity of your IBM Cloud account. Activity Tracker is used to track how users and applications interact with {{site.data.keyword.cos_short}}.
 
## Does an object in a bucket get overwritten if the same object name is used again in the same bucket?
{: #faq-obj-overwrite}
{: faq}

Yes, the object is overwritten.
 
## How do I get bucket information without using the web console? 
{: #faq-bucketinfo-webconsole}
{: faq}

Use the {{site.data.keyword.cos_short}} Resource Configuration API to get bucket information. For details, see [COS configuration](https://cloud.ibm.com/apidocs/cos/cos-configuration#returns-metadata-for-the-specified-bucket) and [COS Integration](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration).

## How can I manage service credentials for {{site.data.keyword.cos_short}} instances?
{: #faq-mnge-cosinst}
{: faq}

When a service credential is created, the underlying Service ID is granted a role on the entire instance of Object Storage. For details, see [Managing Service credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials).

## Why are parts of my credentials hidden or not viewable?
{: #faq-unviewable-credentials}
{: faq}

There may be an issue where the viewer does not have sufficient roles to view the credential information.  For more information, see [the account credentials documentation](/account?topic=account-service_credentials&interface=ui#viewing-credentials-ui).

## Is there a way to enable Key Protect on a {{site.data.keyword.cos_short}} bucket after the bucket is created?
{: #faq-bucket-keypro}
{: faq}

No, it is not possible to add Key Protect after creating a bucket. Key Protect can only be added while creating the bucket.

## How can I move data into archive tier?
{: #faq-archive tier}
{: faq}

You can archive objects using the web console, REST API, and third-party tools that are integrated with IBM Cloud Object Storage. For details, see [COS Archive](/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive).
 
## Can I use the same {{site.data.keyword.cos_short}} instance across multiple regions?
{: #faq-cosinstance-multiplereg}
{: faq}

Yes, the {{site.data.keyword.cos_short}} instance is a global service. Once an instance is created, you can choose the region while creating the bucket.

## Are files scanned for viruses, while being uploaded to COS?
{: #faq-file-scan}
{: faq}

No, the files are not scanned when uploading to {{site.data.keyword.cos_short}}.
When an object is uploaded, you get upload events by using the Activity Tracker or by using the cloud functions. Then, you can download, scan, and re-upload the object.

## Is it possible to form a Hadoop cluster using {{site.data.keyword.cos_short}}?
{: #faq-hadoop-cluster}
{: faq}

No, {{site.data.keyword.cos_short}} is used for the object storage service. For a Hadoop cluster, you need the processing associated with each unit of storage. You may consider the Hadoop-as-a-Service setup.

## Can I generate a "Presigned URL" to download a file and review?
{: #faq-preassign-url}
{: faq}

A PreSigned URL is not generated using the IBM Cloud UI; however, you can use CyberDuck to generate the “pre-signed URL”. It is free. For details, see this [Slack channel](https://ibm-cloudplatform.slack.com/archives/C0VJSU370/p1606859253294800).

## How can I generate a Auth Token using the IAM API Key using REST?
{: #faq-genrt-auth-token}
{: faq}

For more information on working with the API, see [Creating IAM token for API Key](/docs/account?topic=account-iamtoken_from_apikey) and [Congfiguration Authentication](https://cloud.ibm.com/apidocs/cos/cos-configuration#authentication).

## How can I use the {{site.data.keyword.cos_short}} web console to download and upload large objects?
{: #faq-large-object}
{: faq}

You can use IBM Cloud CLI or the API to download large objects. Alternatively, plugins such as  Aspera /rclone can be used.

## What are the libraries that the {{site.data.keyword.cos_short}} SDK supports?
{: #faq-library-support}
{: faq}

{{site.data.keyword.cos_short}} provides SDKs for Java, Python, NodeJS, and Go featuring capabilities to make the most of IBM Cloud Object Storage. For information about the features supported by each SDK, see the [feature list](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-about).

## When a file is uploaded to a cross region bucket using the ‘us-geo’ endpoint, how long is the delay before the file is available at the other US sites?
{: #faq-time-req}
{: faq}

The data are spread immediately without delay and the uploaded files are available once the write is successful.
   
## How do I access the reclaimed resources?
{: #faq-reclaimed-resource}
{: faq}

Create a new set of credentials to access the restored resources.

## Can I host a website using a {{site.data.keyword.cos_short}} bucket?
{: #faq-static-website}
{: faq}

You can use {{site.data.keyword.cos_short}} bucket to host a static website. For details, see [Hosting Website using COS](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial).
 
## Are REST and cURL commands supported for {{site.data.keyword.cos_short}} bucket creation using HMAC credentials?
{: #faq-using-hmac}
{: faq}

Yes, you should setup an authorization header. For details, see [Using HMAC Signature](/docs/cloud-object-storage?topic=cloud-object-storage-hmac-signature).

## What kind of IAM authorization is required to edit a bucket's authorized IPs list? 
{: #faq-author-iplist}
{: faq}

You must have 'Manager' privilege on the bucket to manage the firewall and to set the authorizations.

## Can I convert a single region {{site.data.keyword.cos_short}} bucket to cross region without having to copy objects?
{: #faq-singleregion-to-multiregion}
{: faq}

No, you must copy objects to the target bucket. For details, see [COS Region Copy](/docs/cloud-object-storage?topic=cloud-object-storage-region-copy).

## Is there a way to verify an object’s integrity during an upload to {{site.data.keyword.cos_short}}?
{: #faq-data-integrity}
{: faq}

{{site.data.keyword.cos_short}} supports object integrity and ensures that the payload is not altered during transit. 

## How can I set a notification when usage in a {{site.data.keyword.cos_short}} instance is near a certain billing amount?
{: #faq-quota-billing}
{: faq}

You can use a "soft" bucket quota feature by integrating with Metrics Monitoring and configuring for notifications. For details on establishing a hard quota that prevents usage beyond a set bucket size, see [Using Bucket Quota](/docs/cloud-object-storage?topic=cloud-object-storage-quota).

## Why am I unable to delete a {{site.data.keyword.cos_short}} instance?
{: #faq-delete-instance}
{: faq}

It isn't possible to delete an instance if the API key or Service ID being used is locked. You'll need to navigate in the console to **Manage** > **Access (IAM)** and unlock the API Key or Service ID. The error provided may seem ambiguous but is intended to increase security:

> An error occurred during an attempt to complete the operation. Try fixing the issue or try the operation again later. Description: 400

This is intentionally vague to prevent any useful information from being conveyed to a possible attacker.  For more information on locking API keys or Service IDs, [see the IAM documentation](/docs/account?topic=account-serviceids&interface=ui#lock_serviceid).
