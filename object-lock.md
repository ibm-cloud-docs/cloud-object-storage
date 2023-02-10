---

copyright:
  years: 2023

lastupdated: "2023-02-01"

keywords: worm, immutable, policy, retention, compliance

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Using Object Lock to protect data [[DRAFT]]
{: #ol}

Object Lock preserves individual object versions by ensuring that data is stored in a WORM (Write-Once-Read-Many), non-erasable and non-rewritable manner. This protection is enforced until the end of a retention period and the removal of any legal holds. 
{: shortdesc}

This feature is not currently supported in {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}. [Learn more.](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite)
{: note}

This feature can be used by any user that needs long-term data retention in their environment, including but not limited to organizations in the following industries:

 * Financial
 * Healthcare
 * Media content archives
 * Anyone looking to prevent privileged modification or deletion of objects or documents

Object Lock can also be used by organizations that deal with financial records management, such as broker-dealer transactions, and might need to store data in a non-rewritable and non-erasable format. 

Object Lock is available in certain regions only, see [Integrated Services](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability) for details. It also requires a Standard pricing plan. See [pricing](https://www.ibm.com/cloud/object-storage) for details.  [[[Note: Not sure if this is accurate]]]
{:note}

It isn't possible to use Aspera high-speed transfer with buckets with a retention policy.  [[[Note: Not sure if this is accurate]]]
{:important}

## Terminology and usage
{: #ol-terminology}

### Retention period
{: #ol-terminology-period}

The duration of time an object must be stored in the {{site.data.keyword.cos_full_notm}} bucket.

### Legal hold 
{: #ol-terminology-hold}

Certain objects might need to be prevented from modification after a retention period expires. An example is an incomplete legal review, where records might need to be accessible for an extended duration beyond the retention period originally set. A legal hold flag can then be applied at the object level.
 
Legal holds can be applied to objects during initial uploads or after an object is written.
 
## Object Lock and considerations for various regulations
{: #ol-regulation}

When using immutable Object Storage, it is the client's responsibility to check for and ensure whether any of the feature capabilities that are discussed can be used to satisfy and comply with the key rules around electronic records storage and retention that is generally governed by:

* [Securities and Exchange Commission (SEC) Rule 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64){: external},
* [Financial Industry Regulatory Authority (FINRA) Rule 4511(c)](https://www.finra.org/rules-guidance/rulebooks/finra-rules/4511){: external}, and
* [Commodity Futures Trading Commission (CFTC) Rule 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8){: external}

To assist clients in making informed decisions, IBM engaged Cohasset Associates Inc. to conduct an independent assessment of IBM’s implementation of Object Lock. Review [Cohasset Associates Inc.’s report](){: external} that provides details on the assessment of the Object Lock feature of IBM Cloud Object Storage. [[[Note: Waiting on report]]]

## About Object Lock
{: #ol-about
}
Object Lock works only in versioned buckets, and retention periods and legal holds apply to individual object versions. When you lock an object version, {{site.data.keyword.cos_short}} stores the lock information in the metadata for that object version. Placing a retention period or legal hold on an object protects only the version specified in the request. It doesn't prevent new versions of the object from being created.

If you put an object into a bucket that has the same key name as an existing protected object, {{site.data.keyword.cos_short}} creates a new version of that object, stores it in the bucket as requested, and reports the request as completed successfully. The existing protected version of the object remains locked according to its retention configuration.

To use Object Lock, you follow these basic steps:

  1. Create a new bucket with Object Lock enabled.
  2. (Optional) Configure a default retention period for objects placed in the bucket.
  3. Place the objects that you want to lock in the bucket.
  4. Apply a retention period, a legal hold, or both, to the objects that you want to protect.


## Using the console
{: #ol-console}

Object Lock can be enabled on any bucket with versioning enabled.

Navigate to the bucket settings and the **Immutability** section. Set the toggle to **Enabled** and optionally set a default retention period.  Any versions of objects written to this bucket will automatically be assigned this retention period and will be unable to be deleted until the period expires.  

## Using the REST API, Libraries, and SDKs
{: #ol-sdk}

Several new APIs have been introduced to the {{site.data.keyword.cos_full_notm}} SDKs to provide support for applications working with retention policies. Select a language (HTTP, Java, JavaScript, or Python) at the beginning of this page to view examples that use the appropriate {{site.data.keyword.cos_short}} SDK. 

All code examples assume the existence of a client object that is called `cos` that can call the different methods. For details on creating clients, see the specific SDK guides.

All date values used to set retention periods are Greenwich mean time. A `Content-MD5` header is required to ensure data integrity, and is automatically sent when using an SDK.
{:note}

### Configure Object Lock on an existing bucket
{: #ol-sdk-enable}
This implementation of the `PUT` operation uses the `object-lock` query parameter to set a legal hold an existing object. 

<<samples to be inserted>>

### Configure retention for an object
{: #ol-sdk-legal}
This implementation of the `PUT` operation uses the `retention` query parameter to set the retention parameters for an existing object. This operation allows you to override the default retention period.  


<<samples to be inserted>>