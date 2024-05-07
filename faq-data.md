---

copyright:
  years: 2024
lastupdated: "2024-05-06"


keywords: faq, frequently asked questions, object storage, S3, HMAC

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - Data management
{: #faq}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## Can I migrate data from AWS S3 into {{site.data.keyword.cos_full_notm}}?
{: #faq-migrate}
{: faq}

Yes, you can use your existing tools to read and write data into {{site.data.keyword.cos_full_notm}}. You need to configure HMAC credentials allow your tools to authenticate. Not all S3-compatible tools are currently unsupported. For details, see [Using HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main).

## How does {{site.data.keyword.cos_full}} delete expired data?
{: #faq-expired}
{: faq}

Deletion of an object undergoes various stages to prevent data from being accessible (both before and after deletion). For details, see [Data deletion](/docs/cloud-object-storage?topic=cloud-object-storage-security#security-deletion).

## What is the best way to structure your data by using Object Storage so you can 'look' at it and find what you are looking for?
{: #faq-metadata}
{: faq}

You can use metadata that is associated with each object to find the objects you are looking for. The biggest advantage of Object Storage is the metadata that is associated with each object. Each object can have up to 4 MB of metadata in {{site.data.keyword.cos_short}}. When offloaded to a database, metadata provides excellent search capabilities. Many (key, value) pairs can be stored in 4 MB. You can also use Prefix searching to find what you are looking for. For example, if you use buckets to separate each customer data, you can use prefixes within buckets for organization. For example:  /bucket1/folder/object where 'folder/' is the prefix.

## Can {{site.data.keyword.cos_short}} partition the data automatically using HDFS, so I can read the partitions in parallel, for example, with Spark?
{: #faq-partition}
{: faq}

{{site.data.keyword.cos_short}} supports a ranged GET on the object, so an application can do a distributed striped-read-type operation. Doing the striping is managed by the application.

## Can I unzip a file after I upload it?
{: #faq-unzip}
{: faq}

A feature to unzip or decompress files is not part of the service. For large data transfer, consider using Aspera high-speed transfer, multi-part uploads, or threads to manage multi-part uploads. See [Store large objects](/docs/cloud-object-storage?topic=cloud-object-storage-large-objects).

## How can I archive and restore objects in Object Storage?
{: #faq-restore-object}
{: faq}

Archived objects must be restored before you can access them. While restoring, specify the time limit the objects should remain available before being re-archived. For details, see [archive-restore data](/docs/cloud-object-storage?topic=cloud-object-storage-archive).

## Does an object in a bucket get overwritten if the same object name is used again in the same bucket?
{: #faq-obj-overwrite}
{: faq}

Yes, the object is overwritten.
 
## Are files scanned for viruses, while being uploaded to COS?
{: #faq-file-scan}
{: faq}

While there is no built in antivirus scanning in {{site.data.keyword.cos_short}}, customers could enable a scanning workflow employing their own anti-virus technology that is deployed on {{site.data.keyword.codeengineshort}}(/docs/codeengine?topic=codeengine-getting-started).

## How can I use the {{site.data.keyword.cos_short}} web console to download and upload large objects?
{: #faq-large-object}
{: faq}

You can use IBM Cloud CLI or the API to download large objects. Alternatively, plugins such as  Aspera /rclone can be used.

## How do I access the reclaimed resources?
{: #faq-reclaimed-resource}
{: faq}

Create a new set of credentials to access the restored resources.

## Is there a way to verify an object’s integrity during an upload to {{site.data.keyword.cos_short}}?
{: #faq-data-integrity}
{: faq}

{{site.data.keyword.cos_short}} supports object integrity and ensures that the payload is not altered during transit. 
