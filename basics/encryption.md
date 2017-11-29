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

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using multiple randomly generated keys and an all-or-nothing-transform. While this default encryption model is highly secure, some workloads need to be in possession of the encryption keys used.  Keys can either be managed manually by a client, or can be managed using IBM Key Protect.

For more information on Key Protect, [see the documentation](/docs/services/keymgmt/index.html#getting-started-with-key-protect).


## Uploading objects using customer keys.

### Upload an object

A `PUT` given a path to an object uploads the request body as an object. A SHA256 hash of the object is a required header. All objects are limited to 10TB in size. This operation does not make use of operation specific query parameters, or payload elements.

#### Syntax

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

#### Specific headers for SSE-C

The following headers are available for buckets using Server Side Encryption with Customer-Provided Keys (SSE-C). Any request using SSE-C headers must be sent using SSL. Note that `ETag` values in response headers are _not_ the MD5 hash of the object, but a randomly generated 32-byte hexadecimal string.

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`x-amz-server-side-encryption-customer-algorithm` | string | This header is used to specify the algorithm and key size to use with the encryption key stored in `x-amz-server-side-encryption-customer-key` header. This value must be set to the string `AES256`.
`x-amz-server-side-encryption-customer-key`       | string | This header is used to transport the base 64 encoded byte string representation of the AES 256 key used in the server side encryption process.
`x-amz-server-side-encryption-customer-key-MD5`   | string | This header is used to transport the base64-encoded 128-bit MD5 digest of the encryption key according to RFC 1321\. The object store will use this value to validate the key passes in the `x-amz-server-side-encryption-customer-key` has not been corrupted during transport and encoding process. The digest must be calculated on the key BEFORE the key is base 64 encoded.

##### Sample request using SSE-C

```http
PUT /example-bucket/queen-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-date: 20160825T183001Z
x-amz-content-sha256: 309721641329cf441f3fa16ef996cf24a2505f91be3e752ac9411688e3435429
x-amz-server-side-encryption-customer-algorithm: AES256
x-amz-server-side-encryption-customer-key: MjRCRTJCQTNDQjdFOTkyMzY0NjZEN0NBMDhGQTBGRUQwNzFBMjEwMkQyNjU4MjNEOEMyODU5MkQxQ0ZEMkQ1OQ==
x-amz-server-side-encryption-customer-key-MD5: HBbrEt+ZH5iIfDNeBju03w==
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly'. After a short while the 'queen' is
 the mother of nearly every bee in the hive, and the colony will fight
 fiercely to protect her.
```

##### Sample response

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
x-amz-server-side-encryption-customer-algorithm: AES256
x-amz-server-side-encryption-customer-key-MD5: HBbrEt+ZH5iIfDNeBju03w==
Content-Length: 0
```


#### Specific headers for SSE-KP (Key Protect)

The following headers are available for buckets using Server Side Encryption with Key Protect (SSE-KP).

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-sse-kp-encryption-algorithm` | string | This header is used to specify the algorithm and key size to use with the encryption key stored using Key Protect. This value must be set to the string `AES256`.
`ibm-sse-kp-customer-root-key-crn`  | string | This header is used to reference the specific root key used by Key Protect to encrypt this bucket. This value must be the full CRN of the root key.

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Sample request**

This is an example of creating a new bucket called 'secure-files'.

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```
