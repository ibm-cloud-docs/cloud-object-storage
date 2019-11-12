---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-11-11"

keywords: developer, best practices, object storage

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

# Developer guidance
{: #dev-guide}

If you're seeking solutions for your development with {{site.data.keyword.cos_full}}, this collection of best practices is for you.
{: .shortdesc}

## Tuning cipher settings
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} supports various cipher settings to encrypt data in transit. Not all cipher settings yield the same level performance. Negotiating one of `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_128_CBC_SHA` yields the same levels of performance as no TLS between the client and the {{site.data.keyword.cos_full_notm}} System.

## Using multipart uploads
{: #dev-guide-multipart}

When you work with larger objects, multipart upload operations are recommended to write objects into {{site.data.keyword.cos_full_notm}}. An upload of a single object is performed as a set of parts and these parts are uploaded independently in any order and in parallel. Upon upload completion, {{site.data.keyword.cos_short}} then presents all parts as a single object. This means network interruptions don't cause large uploads to fail, uploads are able to be paused and restarted over time, and objects can be uploaded as they're being created.

Multipart uploads are only available for objects larger than 5 MB. For objects smaller than 50 GB, a part size of 20 MB to 100 MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact.

Due to the additional complexity involved, it is recommended that developers make use of S3 API libraries that provide multipart upload support.

Incomplete multipart uploads do persist until the object is deleted or the multipart upload is aborted with `AbortIncompleteMultipartUpload`. If an incomplete multipart upload is not aborted, the partial upload continues to use resources. Interfaces should be designed with this point in mind, and clean up incomplete multipart uploads.

## Using software development kits
{: #dev-guide-sdks}

It is not mandatory to use published S3 API SDKs; custom software can use the API to integrate directly with {{site.data.keyword.cos_short}}. However, using published S3 API libraries provide advantages such as authentication signature generation, automatic retry logic on `5xx` errors, and pre-signed url generation. Care must be taken when writing software that uses the API directly to handle transient errors, such as by providing retries with exponential backoff when receiving `503` errors.

## Pagination
{: #dev-guide-pagination}

When dealing with many objects in a bucket, web applications can suffer performance degradation. Many applications employ a technique that is called **pagination** (*the process of dividing a large recordset into discrete pages*). Almost all development platforms provide objects or methods to accomplish pagination either by built-in functions or through 3rd party libraries.

The {{site.data.keyword.cos_short}} SDKs provide support for pagination through a method that lists the objects within a specified bucket. This method provides a number of parameters that make it useful when breaking apart a large result set.

### Basic Usage
{: #dev-guide-pagination-basics}
The basic concept behind the object listing method involves setting the maximum number of keys (`MaxKeys`) to return in the response. The response also includes a `boolean` value (`IsTruncated`) that indicates whether more results are available and a `string` value called `NextContinuationToken`. Setting the continuation token in the follow-up requests returns the next batch of objects until no more results are available.

#### Common Parameters
{: #dev-guide-pagination-params}

|Parameter|Description|
|---|---|
|`ContinuationToken`|Sets token to specify the next batch of records|
|`MaxKeys`|Sets the maximum number of keys to include in the response|
|`Prefix`|Restricts the response to keys that begin with the specified prefix|
|`StartAfter`|Sets where to start the object listing from based on the key|

### Using Java
{: #dev-guide-pagination-java}

The {{site.data.keyword.cos_full}} SDK for Java provides the [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){: external} method that allows for returning the object listing in the wanted size. There is a complete code example available [here](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2).

### Using Python
{: #dev-guide-pagination-python}

The {{site.data.keyword.cos_full}} SDK for Python provides the [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){: external} method that allows for returning the object listing in the wanted size. There is a complete code example available [here](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2).
