---
copyright:
  years: 2017
lastupdated: '2017-09-27'
---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Manage encryption

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using [randomly generated keys and an all-or-nothing-transform](/docs/services/cloud-object-storage/info/data-security-encryption.html). While this default encryption model is highly secure, some workloads need to be in possession of the encryption keys used.  Keys can either be managed manually by a client (SSE-C), or can be managed using IBM Key Protect (SSE-KP).

## Server Eide Encryption with Customer-Provided Keys (SSE-C)

SSE-C is enforced on objects.  Requests to read or write objects or their metadata using customer manged keys send the required encryption infomation as headers in the HTTP requests.  The syntax is identical to the S3 API, and S3-compatible libraries that support SSE-C should work as expected against {{site.data.keyword.cos_full}}.

Any request using SSE-C headers must be sent using SSL. Note that `ETag` values in response headers are *not* the MD5 hash of the object, but a randomly generated 32-byte hexadecimal string.

Header | Type | Description
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | string | This header is used to specify the algorithm and key size to use with the encryption key stored in `x-amz-server-side-encryption-customer-key` header. This value must be set to the string `AES256`.
`x-amz-server-side-encryption-customer-key` | string | This header is used to transport the base 64 encoded byte string representation of the AES 256 key used in the server side encryption process.
`x-amz-server-side-encryption-customer-key-MD5` | string | This header is used to transport the base64-encoded 128-bit MD5 digest of the encryption key according to RFC 1321. The object store will use this value to validate the key passes in the `x-amz-server-side-encryption-customer-key` has not been corrupted during transport and encoding process. The digest must be calculated on the key BEFORE the key is base 64 encoded.




For more information on Key Protect, [see the documentation](/docs/services/keymgmt/index.html#getting-started-with-key-protect).
