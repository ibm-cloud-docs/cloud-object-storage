---

copyright:
  years: 2017, 2018
lastupdated: "2018-12-07"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Object operations

## Authentication Options

### IAM
IAM bearer tokens generated using the [IBM Cloud CLI](/docs/services/cloud-object-storage/getting-started-cli.html#gather-key-information)

### HMAC (Headers or Pre-signed URL)
Adding headers to your request using the following values subtituted:

|Key|Value|Example|
|---|---|---|
|{access_key}|Access key assigned to your Service Credential|cf4965cebe074720a4929759f57e1214|
|{datestamp}|The formatted date of your request (yyyymmdd)|20180613|
|{location}|The location code for your endpoint|us-standard|
|{signature}|The hash created using the secret key, location, and date|ffe2b6e18f9dcc41f593f4dbb39882a6bb4d26a73a04326e62a8d344e07c1a3e|
|{timestamp}|The formatted date and time of your request|20180614T001804Z|
|{payload_hash}|The hexadecimal value of the SHA256 hash of the request body (required for uploading objects)|2cd5697803d5409ed17e4c9a09debad05afa9af830c2d56a966b531ddda5cac8|

## Upload an object
{: #upload-object}

A `PUT` given a path to an object uploads the request body as an object. All objects uploaded in a single thread should be smaller than 500MB (objects [uploaded in multiple parts](/docs/services/cloud-object-storage/basics/multipart.html#uploading-objects-in-multiple-parts) can be as large as 10TB).

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Sample request (IAM)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Sample request (HMAC Headers)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: {payload_hash}
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Sample request (HMAC Pre-signed URL)**

```http
PUT /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

----

## Get an object's headers

A `HEAD` given a path to an object retrieves that object's headers.

Note that the `Etag` value returned for objects encrypted using SSE-KP will **not** be the MD5 hash of the original unencrypted object.
{:tip}

**Syntax**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Sample request (IAM)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:32:44 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: da214d69-1999-4461-a130-81ba33c484a6
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:49:06 GMT
Content-Length: 11
```

----

## Download an object

A `GET` given a path to an object downloads the object.

Note that the `Etag` value returned for objects encrypted using SSE-C/SSE-KP will **not** be the MD5 hash of the original unencrypted object.
{:tip}

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

## Optional headers

Header | Type | Description
--- | ---- | ------------
`range` | string | Returns the bytes of an object within the specified range.

**Sample request (IAM)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:34:25 GMT
X-Clv-Request-Id: 116dcd6b-215d-4a81-bd30-30291fa38f93
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 116dcd6b-215d-4a81-bd30-30291fa38f93
ETag: "d34d8aada2996fc42e6948b926513907"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:46:53 GMT
Content-Length: 467

 Female bees that are not fortunate enough to be selected to be the 'queen'
 while they were still larvae become known as 'worker' bees. These bees lack
 the ability to reproduce and instead ensure that the hive functions smoothly,
 acting almost as a single organism in fulfilling their purpose.
```

----

## Delete an object

A `DELETE` given a path to an object deletes an object.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Sample request (IAM)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 204 No Content
Date: Thu, 25 Aug 2016 17:44:57 GMT
X-Clv-Request-Id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
```

----

## Deleting multiple objects

A `POST` given a path to an bucket and proper parameters will delete a specified set of objects. A `Content-MD5` header specifying the base64 encoded MD5 hash of the request body is required.

The required `Content-MD5` header needs to be the binary representation of a base64 encoded MD5 hash.

**Note:** If an object specified in the request is not found the result returns as deleted. 

### Optional Elements
|Header|Type|Description|
|---|---|---|
|`Quiet`|Boolean|Enable quiet mode for the request.|

The request can contain a maximum of 1000 keys that you want to delete.  While this is very useful in reducing the per-request overhead, be mindful when deleting a large number of keys.  Also take into account the sizes of the objects to ensure suitable performance.
{:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```

**Sample request (IAM)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**Sample request (HMAC Headers)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
POST /apiary?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&delete=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3-api.us-geo.objectstorage.softlayer.net
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Delete>
    <Object>
         <Key>surplus-bee</Key>
    </Object>
    <Object>
         <Key>unnecessary-bee</Key>
    </Object>
</Delete>
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 18:54:53 GMT
X-Clv-Request-Id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Content-Type: application/xml
Content-Length: 207
```
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<DeleteResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Deleted>
         <Key>surplus-bee</Key>
    </Deleted>
    <Deleted>
         <Key>unnecessary-bee</Key>
    </Deleted>
</DeleteResult>
```

----

## Copy an object
{: #copy-object}

A `PUT` given a path to a new object creates a new copy of another object specified by the `x-amz-copy-source` header. Unless otherwise altered the metadata remains the same.

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}


**Note**: Copying an item from a *Key Protect*-enabled bucket to a destination bucket in another region is restricted and will result in a `500 - Internal Error`.
{:tip}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### Optional headers

Header | Type | Description
--- | ---- | ------------
`x-amz-metadata-directive` | string (`COPY` or `REPLACE`) | `REPLACE` will overwrite original metadata with new metadata that is provided.
`x-amz-copy-source-if-match` | string (`ETag`)| Creates a copy if the specified `ETag` matches the source object.
`x-amz-copy-source-if-none-match` | string (`ETag`)| Creates a copy if the specified `ETag` is different from the source object.
`x-amz-copy-source-if-unmodified-since` | string (timestamp)| Creates a copy if the the source object has not been modified since the specified date.  Date must be a valid HTTP date (e.g. `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-copy-source-if-modified-since` | string (timestamp)| Creates a copy if the source object has been modified since the specified date.  Date must be a valid HTTP date (e.g. `Wed, 30 Nov 2016 20:21:38 GMT`).

**Sample request (IAM)**

This basic example takes the `bee` object from the `garden` bucket, and creates a copy in the `apiary` bucket with the new key `wild-bee`.

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 19:52:52 GMT
X-Clv-Request-Id: 72992a90-8f86-433f-b1a4-7b1b33714bed
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 72992a90-8f86-433f-b1a4-7b1b33714bed
ETag: "853aab195ce770b0dfb294a4e9467e62"
Content-Type: application/xml
Content-Length: 240
```

```xml
<CopyObjectResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <LastModified>2016-11-30T19:52:53.125Z</LastModified>
  <ETag>"853aab195ce770b0dfb294a4e9467e62"</ETag>
</CopyObjectResult>
```

----

## Check an object's CORS configuration
{: #options}

An `OPTIONS` given a path to an object along with an origin and request type checks to see if that object is accessible from that origin using that request type.  Unlike all other requests, an OPTIONS request does not require the `authorization` or `x-amx-date` headers.

**Syntax**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Sample request (IAM)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Wed, 07 Dec 2016 16:23:14 GMT
X-Clv-Request-Id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: PUT
Access-Control-Allow-Credentials: true
Vary: Origin, Access-Control-Request-Headers, Access-Control-Allow-Methods
Content-Length: 0

```

----

## Uploading objects in multiple parts
{: multipart}

When working with larger objects, multipart upload operations are recommended to write objects into {{site.data.keyword.cos_full}}. An upload of a single object can be performed as a set of parts and these parts can be uploaded independently in any order and in parallel. Upon upload completion, {{site.data.keyword.cos_short}} then presents all parts as a single object. This provides many benefits: network interruptions do not cause large uploads to fail, uploads can be paused and restarted over time, and objects can be uploaded as they are being created.

Multipart uploads are only available for objects larger than 5MB. For objects smaller than 50GB, a part size of 20MB to 100MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact.  Multipart uploads are limited to no more than 10,000 parts of 5GB each.

Using more than 500 parts leads to inefficiencies in {{site.data.keyword.cos_short}} and should be avoided when possible.
{:tip}

Due to the additional complexity involved, it is recommended that developers make use of a library that provide multipart upload support.

Incomplete multipart uploads do persist until the object is deleted or the multipart upload is aborted with `AbortIncompleteMultipartUpload`. If an incomplete multipart upload is not aborted, the partial upload continues to use resources.  Interfaces should be designed with this point in mind, and clean up incomplete multipart uploads.
{:tip}

There are three phases to uploading an object in multiple parts:

1. The upload is initiated and an `UploadId` is created.
2. Individual parts are uploaded specifying their sequential part numbers and the `UploadId` for the object.
3. When all parts are finished uploading, the upload is completed by sending a request with the `UploadId` and an XML block that lists each part number and it's respective `Etag` value.

## Initiate a multipart upload

A `POST` issued to an object with the query parameter `upload` creates a new `UploadId` value, which is then be referenced by each part of the object being uploaded.

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**Sample request (IAM)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```

----

## Upload a part

A `PUT` request issued to an object with query parameters `partNumber` and `uploadId` will upload one part of an object.  The parts may be uploaded serially or in parallel, but must be numbered in order.

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**Sample request (IAM)**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 13374550
```

**Sample request (HMAC Headers)**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 13374550
```

**Sample request (HMAC Pre-signed URL)**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 13374550
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```

----

## List parts
{: #list-parts}

A `GET` given a path to a multipart object with an active `UploadID` specified as a query parameter will return a list of all of the object's parts.


**Syntax**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```

### Required query parameters

Header | Type | Description
--- | ---- | ------------
`uploadId` | string | Upload ID returned when initializing a multipart upload.

### Optional query parameters

Header | Type | Description
--- | ---- | ------------
`max-parts` | string | Defaults to 1,000.
`part-number​-marker` | string | Defines where the list of parts will begin.

**Sample request (IAM)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Mon, 19 Mar 2018 17:21:08 GMT
X-Clv-Request-Id: 6544044d-4f88-4bb6-9ee5-bfadf5023249
Server: Cleversafe/3.12.4.20
X-Clv-S3-Version: 2.5
Accept-Ranges: bytes
Content-Type: application/xml
Content-Length: 743
```

```xml
<ListPartsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>farm</Bucket>
  <Key>spaceship</Key>
  <UploadId>01000162-3f46-6ab8-4b5f-f7060b310f37</UploadId>
  <Initiator>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Initiator>
  <Owner>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Owner>
  <StorageClass>STANDARD</StorageClass>
  <MaxParts>1000</MaxParts>
  <IsTruncated>false</IsTruncated>
  <Part>
    <PartNumber>1</PartNumber>
    <LastModified>2018-03-19T17:20:35.482Z</LastModified>
    <ETag>"bb03cf4fa8603fe407a65ee1dba55265"</ETag>
    <Size>7128094</Size>
  </Part>
</ListPartsResult>
```

----

## Complete a multipart upload

A `POST` request issued to an object with query parameter `uploadId` and the appropriate XML block in the body will complete a multipart upload.

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Sample request (IAM)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 257
```

**Sample request (HMAC Headers)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 257
```

**Sample request (HMAC Pre-signed URL)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 257
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3-api.us-geo.objectstorage.softlayer.net/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

## Abort incomplete multipart uploads

A `DELETE` request issued to an object with query parameter `uploadId` will delete all unfinished parts of a multipart upload.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**Sample request (IAM)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Temporarily restore an archived object
{: #restore-object}

A `POST` request issued to an object with query parameter `restore` to request temporary restoration of an archived object.  A `Content-MD5` header is required as an integrity check for the payload.

An archived object must be restored before downloading or modifying the object.  The lifetime of the object must be specifed, after which the temporary copy of the object will be deleted.

There can be a delay of up to 15 hours before the restored copy is available for access. A HEAD request can check if the restored copy is available.

To permanently restore the object, it must be copied to a bucket that does not have an active lifecycle configuration.

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```

**Payload Elements**

The body of the request must contain an XML block with the following schema:

|Element|Type|Children|Ancestor|Constraint|
|---|---|---|---|---|
|RestoreRequest|Container|Days, GlacierJobParameters|None|None|
|Days|Integer|None|RestoreRequest|Specified the lifetime of the temporarily restored object. The minimum number of days that a restored copy of the object can exist is 1. After the restore period has elapsed, temporary copy of the object will be removed.|
|GlacierJobParameters|String|Tier|RestoreRequest|None|
|Tier|String|None|GlacierJobParameters|**Must** be set to `Bulk`.|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Sample Request (IAM)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Headers)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample request (HMAC Pre-signed URL)**

```http
POST /apiary/queenbee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&restore&x-amz-signature={signature} HTTP/1.1
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3-api.us-geo.objectstorage.softlayer.net
```

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Sample Response**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Updating Metadata

There are two ways to update the metadata on an existing object:
* A `PUT` request with the new metadata and the original object contents
* Executing a `COPY` request with the new metadata specifying the original object as the copy source

All metadata key must be prefixed with `x-amz-meta-`
{: tip}

### Using PUT to update metadata

**Note:** The `PUT` request requires a copy of existing object as the contents will be overwritten.

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Sample request**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

### Using COPY to update metadata

For additional details about executing a `COPY` request, click [here](#copy-object)

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Sample request**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```

**Sample response**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```