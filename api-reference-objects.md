---

copyright:
  years: 2017, 2025
lastupdated: "2025-12-05"

keywords: rest, s3, compatibility, api, objects

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Object operations
{: #object-operations}

The modern capabilities of {{site.data.keyword.cos_full}} are conveniently available via a RESTful API. Operations and methods for reading, writing, and configuring objects (stored within a bucket), are documented here.
{: shortdesc}

For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
{: tip}

## A note regarding Access/Secret Key (HMAC) authentication
{: #object-operations-hmac}

When authenticating to your instance of {{site.data.keyword.cos_full_notm}} by [using HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main), you need the information that is represented in Table 1 when [constructing an HMAC signature](/docs/cloud-object-storage?topic=cloud-object-storage-hmac-signature).

|Key|Value|Example|
|---|---|---|
|{access_key}|Access key assigned to your Service Credential|cf4965cebe074720a4929759f57e1214|
|{date}|The formatted date of your request (`yyyymmdd`)|20180613|
|{region}|The location code for your endpoint|us-standard|
|{signature}|The hash created using the secret key, location, and date|ffe2b6e18f9dcc41f593f4dbb39882a6bb4d26a73a04326e62a8d344e07c1a3e|
|{timestamp}|The formatted date and time of your request|20180614T001804Z|
{: caption="HMAC signature components"}

## Upload an object
{: #object-operations-put}

A `PUT` given a path to an object uploads the request body as an object. All objects uploaded in a single thread should be smaller than 500 MB to minimize the risk of network disruptions. (objects that are [uploaded in multiple parts](/docs/cloud-object-storage?topic=cloud-object-storage-large-objects) can be as large as 10 TB).

Personally Identifiable Information (PII): When _naming_ buckets or objects, do not use any information that can identify any user (natural person) by name, location, or any other means.
{: note}



It is possible to stream objects as large as 5 GB using a single `PUT` request. Multipart uploads are more reliable and can upload more efficiently by using multiple threads to upload parts in parallel. Uploading larger objects in a single `PUT` request results in the performance limitations of a single thread, and in the event of any failures single-threaded uploads will need to be retried in their entirety (whereas with MPU only the specific part(s) that failed need to be retried). The precise throughput that can be achieved by a single thread varies depending on the network bandwidth from the client to the IBM Cloud endpoint, the rate of packet loss (if any) on that connection, the use of HTTP vs HTTPS, the specific ciphers used in the connection and specific TCP connection parameters (such as window size), as well as other factors. While these factors can be optimized for a single-threaded upload, the optimizations would apply equally to any multi-threaded (multipart) uploads as well.
{: tip}

Personally Identifiable Information (PII): When creating buckets or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location, or any other means.
{: note}



**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

### Optional headers
{: #object-operations-put-options}

Header | Type | Description
--- | ---- | ------------
|`x-amz-tagging` | string | A set of tags to apply to the object, formatted as query parameters (`"SomeKey=SomeValue"`).|
|`x-amz-object-lock-mode` | string | Valid value is `COMPLIANCE` or `GOVERNANCE` - required if `x-amz-object-lock-retain-until-date` is present.|
|`x-amz-object-lock-retain-until-date` | ISO8601 Date and Time | Required if `x-amz-object-lock-mode` is present.|
|`x-amz-object-lock-legal-hold` | string | Valid values are `ON` or `OFF`.|
|`Content-MD5` | String | The Base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.|
| `x-amz-checksum-crc32` | String | This header is the Base64 encoded, 32-bit CRC32 checksum of the object. |
| `x-amz-checksum-crc32c` | String | This header is the Base64 encoded, 32-bit CRC32C checksum of the object.|
| `x-amz-checksum-crc64nvme` | String | This header is the Base64 encoded, 64-bit CRC64NVME checksum of the object. The CRC64NVME checksum is always a full object checksum. |
| `x-amz-checksum-sha1` | String | This header is the Base64 encoded, 160-bit SHA1 digest of the object. |
| `x-amz-checksum-sha256` | String | This header is the Base64 encoded, 256-bit SHA256 digest of the object. |
| `x-amz-sdk-checksum-algorithm` | String | Indicates the algorithm used to create the checksum for the object when using the SDK. |
| `x-amz-trailer` | String | Indicates which checksum value header will be found in the trailer of the payload in order to verify object upload integrity.|
{: caption="Optional Headers" caption-side="bottom"}

**Example request**
{: token}

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```
{: token}

**Example request**
{: hmac}

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: {payload_hash}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```
{: hmac}

**Example response**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
x-amz-checksum-crc64nvme: T1r5SUWc07k=
x-amz-checksum-type: FULL_OBJECT
Content-Length: 0
```
{: codeblock}

----

## Get an object's headers
{: #object-operations-head}

A `HEAD` given a path to an object retrieves that object's headers.

The `Etag` value returned for objects encrypted using SSE-KP **is** the MD5 hash of the original decrypted object.
{: note}



**Syntax**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

### Optional headers
{: #object-operations-get-headers}

Header | Type | Description
--- | ---- | ------------
|`x-amz-checksum-mode` | string | This indicates whether or not to include checksum metadata on the response.|
{: caption="Optional Headers" caption-side="bottom"}

**Example request**
{: token}

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```
{: token}

**Example request**
{: hmac}

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Example response**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:32:44 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: da214d69-1999-4461-a130-81ba33c484a6
ETag: "37d4c94839ee181a2224d6242176c4b5"
x-amz-checksum-crc64nvme: T1r5SUWc07k=
x-amz-checksum-type: FULL_OBJECT
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:49:06 GMT
Content-Length: 11
```
{: codeblock}

----

## Download an object
{: #object-operations-get}

A `GET` given a path to an object downloads the object.

The `Etag` value that is returned for objects encrypted using SSE-C/SSE-KP will **not** be the MD5 hash of the original decrypted object.
{: note}



**Syntax**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

### Optional headers
{: #object-operations-get-headers}

Header | Type | Description
--- | ---- | ------------
`range` | String | Returns the bytes of an object within the specified range.
`x-amz-checksum-mode` | String | This indicates whether or not to include checksum metadata on the response.

**Example request**
{: token}

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: token}

**Example request**
{: hmac}

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

**Example response**

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
{: codeblock}

----

## Delete an object
{: #object-operations-delete}

A `DELETE` given a path to an object deletes an object. 



**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

**Example request**
{: token}

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```
{: token}

**Example request**
{: hmac}

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

**Example response**

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

## Delete multiple objects
{: #object-operations-multidelete}

A `POST` given a path to a bucket and proper parameters deletes a specified set of objects. A `Content-MD5` header or a `checksum` header (including `x-amz-checksum-crc32`, `x-amz-checksum-crc32c`, `x-amz-checksum-crc64nvme`, `x-amz-checksum-sha1`, or `x-amz-checksum-sha256`) is required as an integrity check for the payload.

**Optional headers**

| Header        | Type   | Description                                                                                                                                                 |
| ------------- | ------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Content-MD5` | String | The base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit. |
| `x-amz-checksum-crc32` | String | This header is the Base64 encoded, 32-bit CRC32 checksum of the object. |
| `x-amz-checksum-crc32c` | String | This header is the Base64 encoded, 32-bit CRC32C checksum of the object.|
| `x-amz-checksum-crc64nvme` | String | This header is the Base64 encoded, 64-bit CRC64NVME checksum of the object. The CRC64NVME checksum is always a full object checksum. |
| `x-amz-checksum-sha1` | String | This header is the Base64 encoded, 160-bit SHA1 digest of the object. |
| `x-amz-checksum-sha256` | String | This header is the Base64 encoded, 256-bit SHA256 digest of the object. |
{: caption="Optional Headers" caption-side="top"}

When an object that is specified in the request is not found the result returns as deleted.
{: note}



Multiple object deletes involve a `POST operation` that is charged as Class A. The cost of the `POST` request for multiple deletes varies depending on the storage class of the objects, and the amount of data that is deleted. For more information about pricing, see the [IBM Cloud Object Storage pricing page](/objectstorage/create#pricing).
{: note}

### Optional Elements
{: #object-operations-multidelete-options}

|Header|Type|Description|
|---|---|---|
|`Quiet`|Boolean|Enable quiet mode for the request.|
{: caption="Header" caption-side="top"}

The request can contain a maximum of 1000 keys that you want to delete. While this is useful in reducing the number of requests, be mindful when deleting many keys. Also, take into account the sizes of the objects to ensure suitable performance.
{: tip}

The following code shows one example of how to create the necessary representation of the header content:

```sh
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{: codeblock}

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```
{: codeblock}

The body of the request must contain an XML block with the following schema:

|Element|Type|Children|Ancestor|Constraint|
|---|---|---|---|---|
|Delete | Container | Object | - | - |
|Object| Container | Key | Delete | - |
|Key| String | - | Object | Valid key string |
{: caption="Body of the request schema" caption-side="top"}

**Example request**
{: token}

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```
{: token}

**Example request**
{: hmac}

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

**Example response**

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

## Add or update retention on an object
{: #object-operations-add-retention}

A `PUT` issued to an object with the proper parameters adds or extends the retention period.
In `COMPLIANCE` mode, the retention period can only be extended and cannot be shortened or removed.
In `GOVERNANCE` mode, authorized users can extend, shorten, or remove the retention period by including the `x-amz-bypass-governance-retention` header.



**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?retention # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?retention # virtual host style
```
{: codeblock}

**Payload Elements**

The body of the request must contain an XML block with the following schema:

| Element | Type      | Children   | Ancestor | Notes    |
|---------|-----------|------------|----------|----------|
| Retention | Container | Mode, RetainUntilDate     | -        | Required |
| Mode  | String | -        | Retention  | Required - valid value is `COMPLIANCE` or `GOVERNANCE` |
| RetainUntilDate     | Timestamp    | - | Retention   | Required |
{: caption="Body of the request schema" caption-side="top"}

### Optional headers
{: #object-operations-add-retention-headers}

Header | Type | Description
--- | ---- | ------------
`x-amz-bypass-governance-retention` | String | This header allows authorized users to override GOVERNANCE mode retention settings to delete or modify an object before its retain-until date.

The following code shows one example of how to create the necessary representation of the header content:

```sh
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{: codeblock}

**Example request**

This is an example of adding or extending retention on an object.

```http
PUT /apiary/myObject?retention HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Content-MD5: cDeRJIdLuEXWmLpA79K2kg==
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 119
```

```xml
<Retention>
    <Mode>COMPLIANCE</Mode>
    <RetainUntilDate>2023-04-12T23:01:00.000Z</RetainUntilDate>
</Retention>
```


**Example response**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2020 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Content-Length: 0
```

----

----

## Add tags to an object
{: #object-operations-add-tags}

A `PUT` issued to an object with the proper parameters creates or replaces a set of key-value tags associated with the object.



**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?tagging # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?tagging # virtual host style
```
{: codeblock}

**Payload Elements**

The body of the request must contain an XML block with the following schema:

| Element | Type      | Children   | Ancestor | Notes    |
|---------|-----------|------------|----------|----------|
| Tagging | Container | TagSet     | -        | Required |
| TagSet  | Container | Tag        | Tagging  | Required |
| Tag     | String    | Key, Value | TagSet   | Required |
| Key     | Container | -          | Tag      | Required |
| Value   | String    | -          | Tag      | Required |
{: caption="Body of the request schema" caption-side="top"}

Tags must comply with the following restrictions:
* An object can have a maximum of 10 tags
* For each object, each tag key must be unique, and each tag key can have only one value.
* Minimum key length - 1 Unicode characters in UTF-8
* Maximum key length - 128 Unicode characters in UTF-8
* Maximum key byte size - 256 bytes
* Minimum value length - 0 Unicode characters in UTF-8 (Tag Value can be empty)
* Maximum value length - 256 Unicode characters in UTF-8
* Maximum value byte size - 512 bytes
* A Tag key and value may consist of US Alpha Numeric Characters (`a-z`,`A-Z`,`0-9`), and spaces representable in UTF-8, and the following symbols: `+`, `-`, `=`, `.`, `_`, `:`, `/`, `@`
* Tag keys and values are case-sensitive
* `ibm:` cannot be used as a key prefix for tags

|Header                    | Type   | Description |
|--------------------------|--------|-------------------------------------------------------------|
|`Content-MD5` | String | The Base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.|
| `x-amz-checksum-crc32` | String | This header is the Base64 encoded, 32-bit CRC32 checksum of the object. |
| `x-amz-checksum-crc32c` | String | This header is the Base64 encoded, 32-bit CRC32C checksum of the object.|
| `x-amz-checksum-crc64nvme` | String | This header is the Base64 encoded, 64-bit CRC64NVME checksum of the object. The CRC64NVME checksum is always a full object checksum. |
| `x-amz-checksum-sha1` | String | This header is the Base64 encoded, 160-bit SHA1 digest of the object. |
| `x-amz-checksum-sha256` | String | This header is the Base64 encoded, 256-bit SHA256 digest of the object. |
| `x-amz-sdk-checksum-algorithm` | String | Indicates the algorithm used to create the checksum for the object when using the SDK. |
{: caption="Optional Headers" caption-side="bottom"}

**Example request**

This is an example of adding a set of tags to an object.

```http
PUT /apiary/myObject?tagging HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 119
```
{: token}

```http
PUT /apiary/myObject?tagging HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 128
```
{: hmac}

```xml
<Tagging>
   <TagSet>
      <Tag>
         <Key>string</Key>
         <Value>string</Value>
      </Tag>
   </TagSet>
</Tagging>
```


**Example response**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2020 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Content-Length: 0
```

----

## Read an object's tags
{: #object-operations-get-tags}

A `GET` issued to an object with the proper parameters returns the set of key-value tags associated with the object.



**Syntax**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?tagging # path style
GET https://{bucket-name}.{endpoint}/{object-name}?tagging # virtual host style
```
{: codeblock}


**Example request**

This is an example of reading a set of object tags.

```http
GET /apiary/myObject?tagging HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 0
```
{: token}

```http
GET /apiarymyObject?tagging HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 0
```
{: hmac}


**Example response**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2020 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Content-Length: 128
```

```xml
<Tagging>
   <TagSet>
      <Tag>
         <Key>string</Key>
         <Value>string</Value>
      </Tag>
   </TagSet>
</Tagging>
```
----

## Delete an object's tags
{: #object-operations-delete-tags}

A `DELETE` issued to a bucket with the proper parameters removes an object's tags.



**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}{object-name}?tagging # path style
DELETE https://{bucket-name}.{endpoint}{object-name}?tagging # virtual host style
```
{: codeblock}

**Example request**
{: token}

This is an example of deleting an object's tags.
{: token}

```http
DELETE /apiary/myObject?tagging HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: token}

**Example request**
{: hmac}

```http
DELETE /apiary/myObject?tagging HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

The server responds with `204 No Content`.

----

## Copy an object
{: #object-operations-copy}

A `PUT` given a path to a new object creates a new copy of another object that is specified by the `x-amz-copy-source` header. Unless otherwise altered the metadata remains the same.

Personally Identifiable Information (PII): When _naming_ buckets or objects, do not use any information that can identify any user (natural person) by name, location, or any other means.
{: note}

Copying objects (even across locations) does not incur the public outbound bandwidth charges. All data remains inside the COS internal network.
{: note}



**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

### Optional headers
{: #object-operations-copy-options}

Header | Type | Description
--- | ---- | ------------
`x-amz-metadata-directive` | string (`COPY` or `REPLACE`) | A `REPLACE` overwrites original metadata with new metadata that is provided.
`x-amz-tagging` | string | A set of tags to apply to the object, formatted as query parameters (`"SomeKey=SomeValue"`).
`x-amz-tagging-directive` | string (`COPY` or `REPLACE`) | A `REPLACE` overwrites original tags with new tags that is provided.
`x-amz-copy-source-if-match` | String (`ETag`)| Creates a copy if the specified `ETag` matches the source object.
`x-amz-copy-source-if-none-match` | String (`ETag`)| Creates a copy if the specified `ETag` is different from the source object.
`x-amz-copy-source-if-unmodified-since` | String (time stamp)| Creates a copy if the source object has not been modified since the specified date. Date must be a valid HTTP date (for example, `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-copy-source-if-modified-since` | String (time stamp)| Creates a copy if the source object has been modified since the specified date. Date must be a valid HTTP date (for example, `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-checksum-algorithm` | String | Indicates which checksum algorithm will be used to create the checksum for the destination object.

**Example request**
{: token}

This basic example takes the `bee` object from the `garden` bucket, and creates a copy in the `apiary` bucket with the new key `wild-bee`.

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: token}

**Example request**
{: hmac}

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

**Example response**

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
{: codeblock}

```xml
<CopyObjectResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <LastModified>2016-11-30T19:52:53.125Z</LastModified>
  <ETag>"853aab195ce770b0dfb294a4e9467e62"</ETag>
</CopyObjectResult>
```
{: codeblock}

----

## Check an object's CORS configuration
{: #object-operations-options}

An `OPTIONS` given a path to an object along with an origin and request type checks to see whether that object is accessible from that origin by using that request type. Unlike all other requests, an OPTIONS request does not require the `authorization` or `x-amx-date` headers.



**Syntax**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

**Example request**
{: token}

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: token}

**Example request**
{: hmac}

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

**Example response**

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
{: #object-operations-multipart}

When working with larger objects, multipart upload operations are recommended to write objects into {{site.data.keyword.cos_full}}. An upload of a single object can be performed as a set of parts and these parts can be uploaded independently in any order and in parallel. Upon upload completion, {{site.data.keyword.cos_short}} then presents all parts as a single object. This provides many benefits: network interruptions do not cause large uploads to fail, uploads can be paused and restarted over time, and objects can be uploaded as they are being created.

Multipart uploads are only available for objects larger than 5 MB. For objects smaller than 50 GB, a part size of 20 MB to 100 MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact.

Due to the additional complexity involved, it is recommended that developers make use of a library that provides multipart upload support.

Incomplete multipart uploads do persist until the object is deleted or the multipart upload is aborted with `AbortIncompleteMultipartUpload`. If an incomplete multipart upload is not aborted, the partial upload continues to use resources. Interfaces should be designed with this point in mind, and clean up incomplete multipart uploads.
{: tip}

There are three phases to uploading an object in multiple parts:

1. The upload is initiated and an `UploadId` is created.
2. Individual parts are uploaded specifying their sequential part numbers and the `UploadId` for the object.
3. When all parts are finished uploading, the upload is completed by sending a request with the `UploadId` and an XML block that lists each part number and its respective `Etag` value.

## Initiate a multipart upload
{: #object-operations-multipart-initiate}

A `POST` issued to an object with the query parameter `upload` creates a new `UploadId` value, which is then be referenced by each part of the object being uploaded.

Personally Identifiable Information (PII): When _naming_ buckets or objects, do not use any information that can identify any user (natural person) by name, location, or any other means.
{: note}



**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}

### Optional headers
{: #object-operations-post-multipart}

Header | Type | Description
--- | ---- | ------------
|`x-amz-checksum-algorithm` | String |  Indicates which checksum algorithm will be used to create the checksum for the whole multiplart object.|
|`x-amz-checksum-type` | String | Indicates which checksum type will be used to create the checksum for the whole multiplart object. |
{: caption="Optional Headers" caption-side="bottom"}

**Example request**
{: token}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: token}

**Example request**
{: hmac}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

**Example response**

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
{: #object-operations-multipart-put-part}

A `PUT` request that is issued to an object with query parameters `partNumber` and `uploadId` will upload one part of an object. The parts can be uploaded serially or in parallel, but must be numbered in order.

Personally Identifiable Information (PII): When _naming_ buckets or objects, do not use any information that can identify any user (natural person) by name, location, or any other means.
{: note}



**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}

### Optional headers
{: #object-operations-put-part}

Header | Type | Description
--- | ---- | ------------
|`Content-MD5` | String | The Base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.|
| `x-amz-checksum-crc32` | String | This header is the Base64 encoded, 32-bit CRC32 checksum of the object. |
| `x-amz-checksum-crc32c` | String | This header is the Base64 encoded, 32-bit CRC32C checksum of the object.|
| `x-amz-checksum-crc64nvme` | String | This header is the Base64 encoded, 64-bit CRC64NVME checksum of the object. The CRC64NVME checksum is always a full object checksum. |
| `x-amz-checksum-sha1` | String | This header is the Base64 encoded, 160-bit SHA1 digest of the object. |
| `x-amz-checksum-sha256` | String | This header is the Base64 encoded, 256-bit SHA256 digest of the object. |
| `x-amz-sdk-checksum-algorithm` | String | Indicates the algorithm used to create the checksum for the object when using the SDK. |
| `x-amz-trailer` | String | Indicates which checksum value header will be found in the trailer of the payload in order to verify object upload integrity.|
{: caption="Optional Headers" caption-side="bottom"}

**Example request**
{: token}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: token}

**Example request**
{: hmac}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: hmac}

**Example response**

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
{: codeblock}

----

## List parts
{: #object-operations-multipart-list}

A `GET` given a path to a multipart object with an active `UploadID` specified as a query parameter returns a list of all of the object's parts.



**Syntax**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```
{: codeblock}

### Query parameters
{: #object-operations-multipart-list-params}
| Parameter           | Required? | Type   | Description                                              |
|---------------------|-----------|--------|----------------------------------------------------------|
| `uploadId`          | Required  | string | Upload ID returned when initializing a multipart upload. |
| `max-parts`         | Optional  | string | Defaults to 1,000.                                       |
| `part-number​-marker` | Optional  | string | Defines where the list of parts begins.                  |
{: caption="Parameters" caption-side="top"}

**Example request**
{: token}

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: token}

**Example request**
{: hmac}

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: hmac}

**Example response**

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
{: codeblock}

----

## Complete a multipart upload
{: #object-operations-multipart-complete}

A `POST` request that is issued to an object with query parameter `uploadId` and the appropriate XML block in the body will complete a multipart upload.



**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}

### Optional headers
{: #object-operations-post-multipart}

Header | Type | Description
--- | ---- | ------------
| `x-amz-checksum-crc32` | String | This header is the Base64 encoded, 32-bit CRC32 checksum of the object. |
| `x-amz-checksum-crc32c` | String | This header is the Base64 encoded, 32-bit CRC32C checksum of the object.|
| `x-amz-checksum-crc64nvme` | String | This header is the Base64 encoded, 64-bit CRC64NVME checksum of the object. The CRC64NVME checksum is always a full object checksum. |
| `x-amz-checksum-sha1` | String | This header is the Base64 encoded, 160-bit SHA1 digest of the object. |
| `x-amz-checksum-sha256` | String | This header is the Base64 encoded, 256-bit SHA256 digest of the object. |
| `x-amz-checksum-type` | String | Indicates which checksum type will be used to create the checksum for the whole multiplart object. |
{: caption="Optional Header" caption-side="top"}

The body of the request must contain an XML block with the following schema:

|Element|Type|Children|Ancestor|Constraint|
|---|---|---|---|---|
|CompleteMultipartUpload | Container | Part | - | - |
|Part| Container | PartNumber, `ETag` | Delete | - |
|PartNumber| String | - | Object | Valid part number |
|`ETag`| String | - | Object | Valid `ETag` value string |
{: caption="Body of the request schema" caption-side="top"}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}

**Example request**
{: token}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: token}

**Example request**
{: hmac}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: hmac}

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
{: codeblock}

**Example response**

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
{: codeblock}

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```
{: codeblock}

----

## Abort incomplete multipart uploads
{: #object-operations-multipart-uploads}

A `DELETE` request issued to an object with query parameter `uploadId` deletes all unfinished parts of a multipart upload.



**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}

**Example request**
{: token}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: token}

**Example request**
{: hmac}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: hmac}

**Example response**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}

## Temporarily restore an archived object
{: #object-operations-archive-restore}

A `POST` request that is issued to an object with query parameter `restore` to request temporary restoration of an archived object. A `Content-MD5` header or a `checksum` header (including `x-amz-checksum-crc32`, `x-amz-checksum-crc32c`, `x-amz-checksum-crc64nvme`, `x-amz-checksum-sha1`, or `x-amz-checksum-sha256`) is required as an integrity check for the payload.

An archived object must be restored before downloading or modifying the object. The lifetime of the object must be specified after which the temporary copy of the object will be deleted.

For buckets with a lifecycle policy transition storage class of `GLACIER`, there can be a delay of up to 12 hours before the restored copy is available for access. If the transition storage class was set to `ACCELERATED`, there can be a delay of up to two (2) hours before the restored object is available.  A HEAD request can check whether the restored copy is available.

To permanently restore the object, it must be copied to a bucket that doesn't have an active lifecycle configuration.



**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```
{: codeblock}

**Payload Elements**

The body of the request must contain an XML block with the following schema:

Element              | Type      | Children                   | Ancestor             | Constraint
---------------------|-----------|----------------------------|----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
RestoreRequest       | Container | Days, GlacierJobParameters | None                 | None
Days                 | Integer   | None                       | RestoreRequest       | Specified the lifetime of the temporarily restored object. The minimum number of days that a restored copy of the object can exist is 1. After the restore period has elapsed, temporary copy of the object will be removed.
GlacierJobParameters | String    | Tier                       | RestoreRequest       | None
Tier                 | String    | None                       | GlacierJobParameters | Optional, and if left blank will default to the value associated with the storage tier of the policy that was in place when the object was written. If this value is not left blank, it **must** be set to `Bulk` if the transition storage class for the bucket's lifecycle policy was set to `GLACIER`, and **must** be set to `Accelerated` if the transition storage class was set to `ACCELERATED`.

| Header        | Type   | Description                                                                                                                                                 |
| ------------- | ------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Content-MD5` | String | The base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit. |
| `x-amz-checksum-crc32` | String | This header is the Base64 encoded, 32-bit CRC32 checksum of the object. |
| `x-amz-checksum-crc32c` | String | This header is the Base64 encoded, 32-bit CRC32C checksum of the object.|
| `x-amz-checksum-crc64nvme` | String | This header is the Base64 encoded, 64-bit CRC64NVME checksum of the object. The CRC64NVME checksum is always a full object checksum. |
| `x-amz-checksum-sha1` | String | This header is the Base64 encoded, 160-bit SHA1 digest of the object. |
| `x-amz-checksum-sha256` | String | This header is the Base64 encoded, 256-bit SHA256 digest of the object. |
{: caption="Optional Headers" caption-side="top"}

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}

**Example request**
{: token}

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: token}

**Example request**
{: hmac}

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: hmac}

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}

**Example response**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}

## Updating metadata
{: #object-operations-metadata}

There are two ways to update the metadata on an existing object:

* A `PUT` request with the new metadata and the original object contents
* Running a `COPY` request with the new metadata specifying the original object as the copy source

All metadata key must be prefixed with `x-amz-meta-`
{: tip}



### Using PUT to update metadata
{: #object-operations-metadata-put}

The `PUT` request requires a copy of existing object as the contents are overwritten. {: important}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

**Example request**
{: token}

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```
{: codeblock}
{: token}

**Example request**
{: hmac}

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```
{: codeblock}
{: hmac}

**Example response**

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
{: codeblock}

### Using COPY to update metadata
{: #object-operations-metadata-copy}

The complete details about the `COPY` request are [here](#object-operations-copy).

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}

**Example request**
{: token}

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```
{: codeblock}
{: token}

**Example request**
{: hmac}

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;,Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```
{: codeblock}
{: hmac}

**Example response**

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
{: codeblock}

## Next Steps
{: #api-ref-objects-next-steps}

Learn more about bucket operations at the [documentation](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations).
