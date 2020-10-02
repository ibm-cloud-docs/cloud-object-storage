---

copyright:
  years: 2018, 2020
lastupdated: "2020-10-01"

keywords: encryption, security, sse-c

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}
{:help: data-hd-content-type='help'}

# Server-Side Encryption with Customer-Provided Keys (SSE-C)
{: #sse-c}

SSE-C is enforced on objects. Requests to read or write objects or their metadata that use customer-managed keys send the required encryption information as headers in the HTTP requests. The syntax is the same as the S3 API, and S3-compatible libraries that support SSE-C work as expected against {{site.data.keyword.cos_full}}.

Any request that uses SSE-C headers must be sent by using SSL. The `ETag` values in response headers are *not* the MD5 hash of the object, but a randomly generated 32-byte hexadecimal string.

Header | Type | Description
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | String | This header is used to specify the algorithm and key size to use with the encryption key stored in `x-amz-server-side-encryption-customer-key` header. This value must be set to the string `AES256`.
`x-amz-server-side-encryption-customer-key` | String | This header is used to transport the base 64 encoded byte string representation of the AES 256 key used in the server-side encryption process.
`x-amz-server-side-encryption-customer-key-MD5` | String | This header is used to transport the base64-encoded 128-bit MD5 digest of the encryption key according to RFC 1321. The object store uses this value to validate the key passes in the `x-amz-server-side-encryption-customer-key` has not been corrupted during transport and encoding process. The digest must be calculated on the key BEFORE the key is base 64 encoded.
