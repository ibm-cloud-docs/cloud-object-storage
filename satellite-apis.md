---

copyright:
  years: 2021, 2024
lastupdated: "2024-12-20"

keywords:  object storage, satellite, local

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Supported APIs
{: #apis-cos-satellite}


{{site.data.keyword.cloud_notm}} continues to evaluate its service offerings periodically, keeping in perspective our client requirements and market direction. As a result, as of December 16, 2024, the {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} offering is being deprecated. For more information, see [Deprecation overview](/docs/cloud-object-storage?topic=cloud-object-storage-deprecation-cos-satellite).
{: deprecated}

{{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} supports most S3 APIs.
{: shortdesc}

## Supported S3 APIs
{: #apis-satellite-supported}

* `AbortMultipartUpload`
* `CompleteMultipartUpload`
* `CopyObject`
* `CreateBucket`
* `CreateMultipartUpload`
* `DeleteBucket`
* `DeleteBucketCors`
* `DeleteBucketLifecycle`
* `DeleteBucketWebsite`
* `DeleteObject`
* `DeleteObjects`
* `DeleteObjectTagging`
* `DeletePublicAccessBlock`
* `GetBucketAcl`
* `GetBucketCors`
* `GetBucketLifecycle`
* `GetBucketLocation`
* `GetBucketVersioning`
* `GetBucketWebsite`
* `GetObject`
* `GetObjectAcl`
* `GetObjectTagging`
* `GetPublicAccessBlock`
* `HeadBucket`
* `HeadObject`
* `ListBuckets`
* `ListMultipartUploads`
* `ListObjects`
* `ListObjectsV2`
* `ListObjectVersions`
* `ListParts`
* `PutBucketAcl`
* `PutBucketCors`
* `PutBucketLifecycle` (expiration rules only)
* `PutBucketVersioning`
* `PutBucketWebsite`
* `PutObject`
* `PutObjectAcl`
* `PutObjectTagging`
* `PutPublicAccessBlock`
* `UploadPart`
* `UploadPartCopy`

## Unsupported S3 APIs
{: #apis-satellite-unsupported}

* `PutBucketLifecycle` (archive rules only)
* `RestoreObject`
