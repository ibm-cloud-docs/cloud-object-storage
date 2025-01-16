---

copyright:
  years: 2017, [(CURRENT_YEAR)]
lastupdated: "2024-08-23"


keywords: faq, frequently asked questions, object storage, S3, HMAC, general, rate limit

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - General
{: #faq}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## Can I use AWS S3 SDKs with {{site.data.keyword.cos_full_notm}}?
{: #faq-aws-sdk}
{: faq}

{{site.data.keyword.cos_full_notm}} supports the most commonly used subset of Amazon S3 API operations. IBM makes a sustained best effort to ensure that the {{site.data.keyword.cos_full_notm}} APIs stay compatible with the industry standard S3 API. {{site.data.keyword.cos_full_notm}} also produces several native core COS SDKs that are derivatives of publicly available AWS SDKs. These core COS SDKs are explicitly tested on each new {{site.data.keyword.cos_full_notm}} upgrade. When using AWS SDKs, use HMAC authorization and an explicit endpoint. For details, see [About IBM COS SDKs](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-about).

## Does data consistency in {{site.data.keyword.cos_short}} come with a performance impact?
{: #faq-consistency}
{: faq}

Consistency with any distributed system comes with a cost, because the efficiency of the {{site.data.keyword.cos_full_notm}} dispersed storage system is not trivial, but is lower compared to systems with multiple synchronous copies.

## Aren't there performance implications if my application needs to manipulate large objects?
{: #faq-large}
{: faq}

For performance optimization, objects can be uploaded and downloaded in multiple parts, in parallel.

## What is the difference between 'Class A' and 'Class B' requests?
{: #faq-ab}
{: faq}

'Class A' requests are operations that involve modification or listing. This includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.'Class B' requests are those related to retrieving objects or their associated metadata/configurations from the system. There is no charge for deleting buckets or objects from the system.

## Can you confirm that {{site.data.keyword.cos_short}} is ‘immediately consistent’, as opposed to ‘eventually consistent’?
{: #faq-immediate}
{: faq}

{{site.data.keyword.cos_short}} is ‘immediately consistent’ for data and ‘eventually consistent’ for usage accounting.

## Can a web browser display the content of files stored in IBM Cloud Object Storage?
{: #faq-cos-web}
{: faq}

Web browsers can display web content in IBM Cloud Object Storage files, using the COS endpoint as the file location. To create a functioning website, however, you need to set up a web environment; for example, elements such as a CNAME record. IBM Cloud Object Storage does not support automatic static website hosting.  For information, see [Static websites](/docs/cloud-object-storage?topic=cloud-object-storage-iam-public-access#public-access-static-website) and this [tutorial](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).

## Why do `CredentialRetrievalError` occur while uploading data to {{site.data.keyword.cos_short}} or while retrieving credentials? 
{: #faq-credret-error}
{: faq}

`CredentialRetrievalError` can occur due to the following reasons:

* The API key is not valid.
* The IAM endpoint is incorrect.

However, if the issue persists, contact IBM customer support.

## How do I ensure communication with Object Storage?
{: #faq-faq-error}
{: faq}

You can check the communication with Object Storage by using one of the following:

* Use a `COS API HEAD` call to a bucket that will return the headers for that bucket. See [api-head-bucket](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-head-bucket).

* Use SDK : See [`headbucket` property](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#headBucket-property){: external}.

## Why can I not create or delete a service instance?
{: #faq-instance-create-delete}
{: faq}

A user is required to have have at a minimum the platform role of `editor` for all IAM enabled services, or at least for Cloud Object Service. For more information, see the [IAM documentation on roles](/docs/account?topic=account-iam-service-roles-actions).

## What is the maximum number of characters that can be used in a key, or Object name?
{: #faq-max-key}
{: faq}

Keys have a 1024-character limit.

## How can I track events in Object Storage?
{: #faq-event-tracking}
{: faq}

The Object Storage Activity Tracker service records user-initiated activities that change the state of a service in Object Storage. For details, see [IBM Cloud Activity Tracker](/docs/cloud-object-storage?topic=cloud-object-storage-at-events).

## What are some tools unable to render object names?
{: #faq-xml-error}
{: faq}

Object names that contain unicode characters that are not allowed by the XML standard will result in "Malformed XML" messages. For more information, see [the XML reference documentation](https://www.w3.org/TR/xml/#charsets).

## Is Object Storage HIPAA compliant to host PHI data?
{: #faq-hipaa}
{: faq}

Yes, Object Storage is HIPAA compliant.

## Is there any option in Object Storage to enable `accelerate data transfer`?
{: #faq-accel-data}
{: faq}

{{site.data.keyword.cos_short}} offers [**Aspera**](https://www.ibm.com/products/cloud-object-storage/aspera) service for high speed data transfer.

## How can I access a private COS endpoint in a data center from another date center?
{: #faq-access-pvt-cospoints}
{: faq}

Use {{site.data.keyword.cos_short}} [Direct Link Connection](/docs/direct-link?topic=direct-link-using-ibm-cloud-direct-link-to-connect-to-ibm-cloud-object-storage) to create a global direct link.

## How can I monitor {{site.data.keyword.cos_short}} resources?
{: #faq-monitor-cos-res}
{: faq}

Use the Activity Tracker service to capture and record {{site.data.keyword.cos_short}} activities and monitor the activity of your IBM Cloud account. Activity Tracker is used to track how users and applications interact with {{site.data.keyword.cos_short}}.
 
## How can I move data into the archive tier?
{: #faq-archive tier}
{: faq}

You can archive objects using the web console, REST API, and third-party tools that are integrated with IBM Cloud Object Storage. For details, see [COS Archive](/docs/cloud-object-storage?topic=cloud-object-storage-archive).
 
## Can I use the same {{site.data.keyword.cos_short}} instance across multiple regions?
{: #faq-cosinstance-multiplereg}
{: faq}

Yes, the {{site.data.keyword.cos_short}} instance is a global service. Once an instance is created, you can choose the region while creating the bucket.

## Is it possible to form a Hadoop cluster using {{site.data.keyword.cos_short}}?
{: #faq-hadoop-cluster}
{: faq}

No, {{site.data.keyword.cos_short}} is used for the object storage service. For a Hadoop cluster, you need the processing associated with each unit of storage. You may consider the Hadoop-as-a-Service setup.

## Can I generate a "Pre-signed URL" to download a file and review?
{: #faq-preassign-url}
{: faq}

A Pre-signed URL is not generated using the IBM Cloud UI; however, you can use CyberDuck to generate the “pre-signed URL”. It is free. 

## How can I generate a Auth Token using the IAM API Key using REST?
{: #faq-genrt-auth-token}
{: faq}

For more information on working with the API, see [Creating IAM token for API Key](/docs/account?topic=account-iamtoken_from_apikey) and [Configuration Authentication](https://cloud.ibm.com/apidocs/cos/cos-configuration#authentication).

## What are the libraries that the {{site.data.keyword.cos_short}} SDK supports?
{: #faq-library-support}
{: faq}

{{site.data.keyword.cos_short}} provides SDKs for Java, Python, NodeJS, and Go featuring capabilities to make the most of IBM Cloud Object Storage. For information about the features supported by each SDK, see the [feature list](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-about).

## When a file is uploaded to a cross region bucket using the ‘us-geo’ endpoint, how long is the delay before the file is available at the other US sites?
{: #faq-time-req}
{: faq}

The data are spread immediately without delay and the uploaded files are available once the write is successful.

## Why am I unable to delete a {{site.data.keyword.cos_short}} instance?
{: #faq-delete-instance}
{: faq}

It isn't possible to delete an instance if the API key or Service ID being used is locked. You'll need to navigate in the console to **Manage** > **Access (IAM)** and unlock the API Key or Service ID. The error provided may seem ambiguous but is intended to increase security:

> An error occurred during an attempt to complete the operation. Try fixing the issue or try the operation again later. Description: 400

This is intentionally vague to prevent any useful information from being conveyed to a possible attacker.  For more information on locking API keys or Service IDs, [see the IAM documentation](/docs/account?topic=account-serviceids&interface=ui#lock_serviceid).

## How do I download the Root CA certificate for {{site.data.keyword.cos_short}}?
{: #faq-download-root-ca-cert}
{: faq}

{{site.data.keyword.cos_short}} root CA certificates can be downloaded from https://www.digicert.com/kb/digicert-root-certificates.htm. Please download PEM or DER/CRT format from "DigiCert TLS RSA SHA256 2020 CA1" that is located  under "Other intermediate certificates."

## How to I find my current active {{site.data.keyword.cos_short}} instance/resources?
{: #faq-shell-cloud-instance}
{: faq}

Login to the IBM Cloud shell: https://cloud.ibm.com/shell and enter at the prompt `ibmcloud resource search "service_name:cloud-object-storage AND type:resource-instance"`.

The response you receive includes information for the name of your instance, location, family, resource type, resource group ID, CRN, tags, service tags, and access tags.

## Does {{site.data.keyword.cos_full_notm}} rate limit?
{: #faq-cos-rate-limit}
{: faq}

{{site.data.keyword.cos_full_notm}} may rate-limit your workload based on its specific characteristics and current system capacity. Rate-limiting will be seen as a 429 or 503 response, in which case retries with exponential back-off are suggested.
