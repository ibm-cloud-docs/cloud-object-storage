---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-11-02"

keywords: rest, s3, compatibility, api, buckets

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

# Bucket operations
{: #compatibility-api-bucket-operations}

The modern capabilities of {{site.data.keyword.cos_full}} are conveniently available via a RESTful API. Operations and methods concerning buckets (where objects are stored) are documented here.
{: shortdesc}


## List buckets
{: #compatibility-api-list-buckets}

A `GET` request sent to the endpoint root returns a list of buckets that are associated with the specified service instance. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Header                    | Type   | Required? |  Description
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | String | Yes | List buckets that were created in this service instance.

Query Parameter                    | Value   | Required? |  Description
--------------------------|--------|---| -----------------------------------------------------------
`extended` | None | No | Provides `LocationConstraint` metadata in the listing.

Extended listing isn't supported in the SDKs or CLI.
{:note}

**Syntax**

```bash
GET https://{endpoint}/
```

**Example request**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Example response**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

### Getting an extended listing
{: #compatibility-api-list-buckets-extended}

**Syntax**

```bash
GET https://{endpoint}/?extended
```

**Example request**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Example response**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <IsTruncated>false</IsTruncated>
    <MaxKeys>1000</MaxKeys>
    <Prefix/>
    <Marker/>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
            <LocationConstraint>us-south-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
            <LocationConstraint>seo01-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
            <LocationConstraint>eu-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
            <LocationConstraint>us-cold</LocationConstraint>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

## Create a bucket
{: #compatibility-api-new-bucket}

A `PUT` request sent to the endpoint root followed by a string will create a bucket. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Bucket names must be globally unique and DNS-compliant; names between 3 and 63 characters long must be made of lowercase letters, numbers, and dashes. Bucket names must begin and end with a lowercase letter or number. Bucket names resembling IP addresses are not allowed. This operation doesn't make use of operation specific query parameters.

Bucket names must be unique because all buckets in the public cloud share a global namespace. This allows for access to a bucket without needing to provide any service instance or account information. It is also not possible to create a bucket with a name beginning with `cosv1-` or `account-` as these prefixes are reserved by the system.
{:important}

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | String  |  This header references the service instance where the bucket will be created and to which data usage will be billed.

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means in the name of the bucket or object.
{:tip}

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Example request**

This is an example of creating a new bucket called 'images'.

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Example response**

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

----

## Create a bucket with a different storage class
{: #compatibility-api-storage-class}

To create a bucket with a different storage class, send an XML block specifying a bucket configuration with a `LocationConstraint` of `{provisioning code}` in the body of a `PUT` request to a bucket endpoint. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Note that standard bucket [naming rules](#compatibility-api-new-bucket) apply. This operation does not make use of operation specific query parameters.

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | String  |  This header references the service instance where the bucket will be created and to which data usage will be billed.

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

The body of the request must contain an XML block with the following schema:

|Element|Type|Children|Ancestor|Constraint|
|---|---|---|---|---|
|CreateBucketConfiguration | Container | LocationConstraint | - | - |
|LocationConstraint| String | - | CreateBucketConfiguration | Valid location code |

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

**Example request**

This is an example of creating a new bucket called 'vault-images'.

```http
PUT /vault-images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
Content-Length: 110
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

**Example response**

```http
HTTP/1.1 200 OK
Date: Fri, 17 Mar 2017 17:52:17 GMT
X-Clv-Request-Id: b6483b2c-24ae-488a-884c-db1a93b9a9a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
Content-Length: 0
```

----

## Create a new bucket with Key Protect or {{site.data.keyword.hscrypto}} managed encryption keys (SSE-KP)
{: #compatibility-api-key-protect}

To create a bucket where the encryption keys are managed by Key Protect or {{site.data.keyword.hscrypto}}, it is necessary to have access to an active Key Protect or {{site.data.keyword.hscrypto}} service instance located in the same location as the new bucket. This operation does not make use of operation specific query parameters.

For more information on using Key Protect to manage your encryption keys, [see the documentation for Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

For more information on {{site.data.keyword.hscrypto}}, [see the documentation](/docs/services/hs-crypto?topic=hs-crypto-get-started).

Note that managed encryption is **not** available in a Cross Region configuration and any SSE-KP buckets must be Regional.
{:tip}

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | String  |  This header references the service instance where the bucket will be created and to which data usage will be billed.
`ibm-sse-kp-encryption-algorithm` | String | This header is used to specify the algorithm and key size to use with the encryption key stored by using Key Protect. This value must be set to the string `AES256`.
`ibm-sse-kp-customer-root-key-crn`  | String | This header is used to reference the specific root key used by Key Protect or {{site.data.keyword.hscrypto}} to encrypt this bucket. This value must be the full CRN of the root key.

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Example request**

This is an example of creating a new bucket called 'secure-files'.

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**Example response**

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

---

## Retrieve a bucket's headers
{: #compatibility-api-head-bucket}

A `HEAD` issued to a bucket will return the headers for that bucket.

`HEAD` requests don't return a body and thus can't return specific error messages such as `NoSuchBucket`, only `NotFound`.
{:tip}

**Syntax**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**Example request**

This is an example of fetching the headers for the 'images' bucket.

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Example response**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
```

**Example request**

`HEAD` requests on buckets with Key Protect encryption will return extra headers.

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Example response**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
ibm-sse-kp-enabled: True
ibm-sse-kp-crk-id: {customer-root-key-id}
```

----

## List objects in a given bucket (Version 2)
{: #compatibility-api-list-objects-v2}

A `GET` request addressed to a bucket returns a list of objects, limited to 1,000 at a time and returned in non-lexographical order. The `StorageClass` value that is returned in the response is a default value as storage class operations are not implemented in COS. This operation does not make use of operation specific headers or payload elements.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### Optional query parameters
{: #compatibility-api-list-objects-v2-params}
Name | Type | Description
--- | ---- | ------------
`list-type` | String | Indicates version 2 of the API and the value must be 2.
`prefix` | String | Constrains response to object names beginning with `prefix`.
`delimiter` | String | Groups objects between the `prefix` and the `delimiter`.
`encoding-type` | String | If unicode characters that are not supported by XML are used in an object name, this parameter can be set to `url` to properly encode the response.
`max-keys` | String | Restricts the number of objects to display in the response. Default and maximum is 1,000.
`fetch-owner` | String | Version 2 of the API does not include the `Owner` information by default. Set this parameter to `true` if `Owner` information is desired in the response.
`continuation-token` | String | Specifies the next set of objects to be returned when your response is truncated (`IsTruncated` element returns `true`).<br/><br/>Your initial response will include the `NextContinuationToken` element. Use this token in the next request as the value for `continuation-token`.
`start-after` | String | Returns key names after a specific key object.<br/><br/>*This parameter is only valid in your initial request.*  If a `continuation-token` parameter is included in your request, this parameter is ignored.

**Example request (simple with IAM)**

This request lists the objects inside the "apiary" bucket.

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Example response (simple)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 814
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <KeyCount>3</KeyCount>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Example request (max-keys parameter)**

This request lists the objects inside the "apiary" bucket with a max key returned set to 1.

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Example response (Truncated Response)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 598
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <NextContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Example request (continuation-token parameter)**

This request lists the objects inside the "apiary" bucket with a continuation token specified.

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Example response (Truncated Response, continuation-token parameter)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 604
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <ContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</ContinuationToken>
  <NextContinuationToken>1a8j20CqowRrM4epIQ7fTBuyPZWZUeA8Epog16wYu9KhAPNoYkWQYhGURsIQbll1lP7c-OO-V5Vyzu6mogiakC4NSwlK4LyRDdHQgY-yPH4wMB76MfQR61VyxI4TJLxIWTPSZA0nmQQWcuV2mE4jiDA</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

### List objects in a given bucket (deprecated)
{: #compatibility-api-list-objects}

**Note:** *This API is included for backwards compatibility.*  See [Version 2](#compatibility-api-list-objects-v2) for the recommended method of retrieving objects in a bucket.

A `GET` request addressed to a bucket returns a list of objects, limited to 1,000 at a time and returned in non-lexographical order. The `StorageClass` value that is returned in the response is a default value as storage class operations are not implemented in COS. This operation does not make use of operation specific headers or payload elements.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### Optional query parameters
{: #compatibility-api-list-objects-params}

Name | Type | Description
--- | ---- | ------------
`prefix` | String | Constrains response to object names beginning with `prefix`.
`delimiter` | String | Groups objects between the `prefix` and the `delimiter`.
`encoding-type` | String | If unicode characters that are not supported by XML are used in an object name, this parameter can be set to `url` to properly encode the response.
`max-keys` | String | Restricts the number of objects to display in the response. Default and maximum is 1,000.
`marker` | String | Specifies the object from where the listing should begin, in UTF-8 binary order.

**Example request**

This request lists the objects inside the "apiary" bucket.

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Example response**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 909
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <Marker/>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

## Delete a bucket

A `DELETE` issued to an empty bucket deletes the bucket. After deleting a bucket the name will be held in reserve by the system for 10 minutes, after which it will be released for re-use. *Only empty buckets can be deleted.*

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### Optional headers

Name | Type | Description
--- | ---- | ------------
`aspera-ak-max-tries` | String | Specifies the number of times to attempt the delete operation. Default value is 2.


**Example request**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

The server responds with `204 No Content`.

If a non-empty bucket is requested for deletion, the server responds with `409 Conflict`.

**Example response**

```xml
<Error>
  <Code>BucketNotEmpty</Code>
  <Message>The bucket you tried to delete is not empty.</Message>
  <Resource>/apiary/</Resource>
  <RequestId>9d2bbc00-2827-4210-b40a-8107863f4386</RequestId>
  <httpStatusCode>409</httpStatusCode>
</Error>
```

----

## List canceled/incomplete multipart uploads for a bucket

A `GET` issued to a bucket with the proper parameters retrieves information about any canceled or incomplete multipart uploads for a bucket.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**Parameters**

Name | Type | Description
--- | ---- | ------------
`prefix` | String | Constrains response to object names beginning with `{prefix}`.
`delimiter` | String | Groups objects between the `prefix` and the `delimiter`.
`encoding-type` | String | If unicode characters that are not supported by XML are used in an object name, this parameter can be set to `url` to properly encode the response.
`max-uploads` | integer | Restricts the number of objects to display in the response. Default and maximum is 1,000.
`key-marker` | String | Specifies from where the listing should begin.
`upload-id-marker` | String | Ignored if `key-marker` is not specified, otherwise sets a point at which to begin listing parts above `upload-id-marker`.

**Example request**

This is an example of retrieving all current canceled and incomplete multipart uploads.

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Example response** (no multipart uploads in progress)

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:22:27 GMT
X-Clv-Request-Id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Content-Type: application/xml
Content-Length: 374
```

```xml
<ListMultipartUploadsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>apiary</Bucket>
  <KeyMarker/>
  <UploadIdMarker/>
  <NextKeyMarker>multipart-object-123</NextKeyMarker>
  <NextUploadIdMarker>0000015a-df89-51d0-2790-dee1ac994053</NextUploadIdMarker>
  <MaxUploads>1000</MaxUploads>
  <IsTruncated>false</IsTruncated>
  <Upload>
    <Key>file</Key>
    <UploadId>0000015a-d92a-bc4a-c312-8c1c2a0e89db</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-16T22:09:01.002Z</Initiated>
  </Upload>
  <Upload>
    <Key>multipart-object-123</Key>
    <UploadId>0000015a-df89-51d0-2790-dee1ac994053</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-18T03:50:02.960Z</Initiated>
  </Upload>
</ListMultipartUploadsResult>
```

----

## List any cross-origin resource sharing configuration for a bucket

A `GET` issued to a bucket with the proper parameters retrieves information about cross-origin resource sharing (CORS) configuration for a bucket.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Example request**

This is an example of listing a CORS configuration on the "apiary" bucket.

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Example response** 

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:20:30 GMT
X-Clv-Request-Id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Content-Type: application/xml
Content-Length: 123
```

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <CORSRule>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
  </CORSRule>
</CORSConfiguration>
```

----

## Create a cross-origin resource sharing configuration for a bucket

A `PUT` issued to a bucket with the proper parameters creates or replaces a cross-origin resource sharing (CORS) configuration for a bucket.

The required `Content-MD5` header needs to be the binary representation of a base64-encoded MD5 hash.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Example request**

This is an example of adding a CORS configuration that allows requests from `www.ibm.com` to issue `GET`, `PUT`, and `POST` requests to the bucket.

```http
PUT /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 237
```

```xml
<CORSConfiguration>
  <CORSRule>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
  </CORSRule>
</CORSConfiguration>
```


**Example response**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```

----

## Delete any cross-origin resource sharing configuration for a bucket

A `DELETE` issued to a bucket with the proper parameters creates or replaces a cross-origin resource sharing (CORS) configuration for a bucket.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Example request**

This is an example of deleting a CORS configuration for a bucket.

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

The server responds with `204 No Content`.

----

## List the location constraint for a bucket

A `GET` issued to a bucket with the proper parameter retrieves the location information for a bucket.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**Example request**

This is an example of retrieving the location of the "apiary" bucket.

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Example response**

```http
HTTP/1.1 200 OK
Date: Tue, 12 Jun 2018 21:10:57 GMT
X-Clv-Request-Id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Content-Type: application/xml
Content-Length: 161
```

```xml
<LocationConstraint xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  us-south-standard
</LocationConstraint>
```

----

## Create a bucket lifecycle configuration
{: #compatibility-api-create-bucket-lifecycle}

A `PUT` operation uses the lifecycle query parameter to set lifecycle settings for the bucket. A `Content-MD5` header is required as an integrity check for the payload.

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Payload Elements**

The body of the request must contain an XML block with the following schema:

|Element|Type|Children|Ancestor|Constraint|
|---|---|---|---|---|
|LifecycleConfiguration|Container|Rule|None|Limit 1|
|Rule|Container|ID, Status, Filter, Transition|LifecycleConfiguration|Limit 1|
|ID|String|None|Rule|**Must** consist of `(a-z,A- Z0-9)` and the following symbols:`` !`_ .*'()- ``|
|Filter|String|Prefix|Rule|**Must** contain a `Prefix` element.|
|Prefix|String|None|Filter|**Must** be set to `<Prefix/>`.|
|Transition|Container|Days, StorageClass|Rule|Limit 1.|
|Days|Non-negative integer|None|Transition|**Must** be a value greater than 0.|
|Date|Date|None|Transition|**Must** be in ISO 8601 Format and the date must be in the future.|
|StorageClass|String|None|Transition|**Must** be set to GLACIER.|

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>{string}</ID>
        <Status>Enabled</Status>
        <Filter>
            <Prefix/>
        </Filter>
        <Transition>
            <Days>{integer}</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

**Example request**

```http
PUT /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ== 
Content-Length: 305
```

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

The server responds with `200 OK`.

----

## Retrieve a bucket lifecycle configuration

A `GET` operation uses the lifecycle query parameter to retrieve lifecycle settings for the bucket.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Example request**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**Example response**

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

----

## Delete the lifecycle configuration for a bucket

A `DELETE` issued to a bucket with the proper parameters removes any lifecycle configurations for a bucket.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Example request**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

The server responds with `204 No Content`.
