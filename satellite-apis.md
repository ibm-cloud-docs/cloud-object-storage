---

copyright:
  years: 2021
lastupdated: "2021-12-01"

keywords:  object storage, satellite, local

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Supported APIs
{: #apis-cos-satellite}


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
