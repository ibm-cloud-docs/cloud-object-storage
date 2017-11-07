---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Developer guidance

## Tuning cipher settings
{{site.data.keyword.cos_full}} supports a variety of cipher settings to encrypt data in transit. Not all cipher settings yield the same level performance. Negotiating one of `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_128_CBC_SHA` has shown to yield the same levels of performance as no TLS between the client and the {{site.data.keyword.cos_full_notm}} System.

## Using multipart uploads
When working with larger objects, multipart upload operations are recommended to write objects into {{site.data.keyword.cos_full_notm}}. An upload of a single object can be performed as a set of parts and these parts can be uploaded independently in any order and in parallel. Upon upload completion, {{site.data.keyword.cos_short}} then presents all parts as a single object. This provides many benefits: network interruptions do not cause large uploads to fail, uploads can be paused and restarted over time, and objects can be uploaded as they are being created.

Multipart uploads are only available for objects larger than 5MB. For objects smaller than 50GB, a part size of 20MB to 100MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact.

Using more than 500 parts leads to inefficiencies in {{site.data.keyword.cos_short}} and should be avoided when possible.

Due to the additional complexity involved, it is recommended that developers make use of S3 API libraries that provide multipart upload support.

Incomplete multipart uploads do persist until the object is deleted or the multipart upload is aborted with `AbortIncompleteMultipartUpload`. If an incomplete multipart upload is not aborted, the partial upload continues to use resources.  Interfaces should be designed with this point in mind, and clean up incomplete multipart uploads.

## Using software development kits

It is not mandatory to use published S3 API SDKs; custom software can leverage the API to integrate directly with {{site.data.keyword.cos_short}}. However, using published S3 API libraries provide advantages such as authentication/signature generation, automatic retry logic on `5xx` errors, and pre-signed url generation. Care must be taken when writing software that uses the API directly to handle transient errors, such as by providing retries with exponential backoff when receiving `503` errors.
