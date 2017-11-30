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

# Bucket operations

## List buckets belonging to an account (`GET` service)
{: #list-buckets}

A `GET` issued to the endpoint root returns a list of buckets associated with the specified service instance. This operation does not make use of operation specific query parameters or payload elements.

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | string  |  This header references the service instance where the bucket will be created and to which data usage will be billed.

**Syntax**

```bash
GET https://{endpoint}/
```

**Sample request**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
ibm-service-instance-id: {ibm-service-instance-id}
```

**Sample response**

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

## Create a new bucket
{: #new-bucket}

A `PUT` issued to the endpoint root will create a bucket when a string is provided. Bucket names must be unique, and accounts are limited to 100 buckets each. Bucket names must be DNS-compliant; names between 3 and 63 characters long must be made of lowercase letters, numbers, and dashes. Bucket names must begin and end with a lowercase letter or number. Bucket names resembling IP addresses are not allowed. This operation does not make use of operation specific query parameters.

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | string  |  This header references the service instance where the bucket will be created and to which data usage will be billed.

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Sample request**

This is an example of creating a new bucket called 'images'.

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
ibm-service-instance-id: {ibm-service-instance-id}
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

----

## Create a bucket with a different storage class
{: #storage-class}

To create a bucket with a different storage class, send an XML block specifying a bucket configuration with a `LocationConstraint` of `{provisioning code}` in the body of a `PUT` request to a bucket endpoint.  Note that standard bucket [naming rules](#create-a-new-bucket) apply. This operation does not make use of operation specific query parameters.

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | string  |  This header references the service instance where the bucket will be created and to which data usage will be billed.

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

Valid provisioning codes for `LocationCostraint` are: <br>
&emsp;&emsp;  `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>

**Sample request**

This is an example of creating a new bucket called 'vault-images'.

```http
PUT /vault-images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
ibm-service-instance-id: {ibm-service-instance-id}
Content-Length: 110
```
```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

**Sample response**

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

## Create a new bucket with Key Protect managed encryption keys (SSE-KP)
{: #key-protect}

To create a bucket where the encryption keys are managed by Key Protect, it is necessary to have access to an active Key Protect service instance located in the same location as the new bucket. This operation does not make use of operation specific query parameters.

For more information on using Key Protect to manage your encryption keys, [see the documentation](/docs/services/keymgmt/index.html).

Note that Key Protect is not available in a Cross Region configuration and any SSE-KP buckets must be Regional.
{:tip}

Header                                        | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | string  |  This header references the service instance where the bucket will be created and to which data usage will be billed.
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
Host: s3.us-south.objectstorage.softlayer.net
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

---

## Retrieve a bucket's headers

A `HEAD` issued to a bucket will return the headers for that bucket.

**Syntax**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**Sample request**

This is an example of fetching the headers for the 'images' bucket.

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
Authorization:Bearer {token}
```

**Sample response**

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

**Sample request**

`HEAD` requests on buckets with Key Protect encrytions will return extra headers.

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
Authorization:Bearer {token}
```

**Sample response**

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
ibm-see-kp-crk-id: {customer-root-key-id}
```

----

## List objects in a given bucket

A `GET` request addressed to a bucket returns a list of objects, limited to 1,000 at a time and returned in non-lexographical order. The `StorageClass` value that is returned in the response is a default value as storage class operations are not implemented in COS. This operation does not make use of operation specific headers or payload elements.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

## Optional query parameters

Name | Type | Description
--- | ---- | ------------
`prefix` | string | Constrains response to object names beginning with `prefix`.
`delimiter` | string | Groups objects between the `prefix` and the `delimiter`.
`encoding-type` | string | If unicode characters that are not supported by XML are used in an object name, this parameter can be set to `url` to properly encode the response.
`max-keys` | string | Restricts the number of objects to display in the response.  Default and maximum is 1,000.
`marker` | string | Specifies the object from where the listing should begin, in UTF-8 binary order.

**Sample request**

This request lists the objects inside the "apiary" bucket.

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
Authorization: Bearer {token}
```

**Sample response**

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

A `DELETE` issued to an empty bucket deletes the bucket. After deleting a bucket the name will be held in reserve by the system for 10 minutes, after which it will be released for re-use.  *Only empty buckets can be deleted.*

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

**Sample request**

```http
DELETE /images HTTP/1.1
Host: s3-api.us-geo.objectstorage.softlayer.net
Authorization: Bearer {token}
```

The server responds with `204 No Content`.

If a non-empty bucket is requested for deletion, the server responds with `409 Conflict`.

**Sample request**

```http
DELETE /apiary HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

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
`prefix` | string | Constrains response to object names beginning with `{prefix}`.
`delimiter` | string | Groups objects between the `prefix` and the `delimiter`.
`encoding-type` | string | If unicode characters that are not supported by XML are used in an object name, this parameter can be set to `url` to properly encode the response.
`max-uploads` | integer | Restricts the number of objects to display in the response.  Default and maximum is 1,000.
`key-marker` | string | Specifies from where the listing should begin.
`upload-id-marker` | string | Ignored if `key-marker` is not specified, otherwise sets a point at which to begin listing parts above `upload-id-marker`.

**Sample request**

This is an example of retrieving all current canceled and incomplete multipart uploads.

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response** (no multipart uploads in progress)

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

**Sample request**

This is an example of listing a CORS configuration on the "apiary" bucket.

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response** No CORS configuration set

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
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/"/>
```

----

## Create a cross-origin resource sharing configuration for a bucket

A `PUT` issued to a bucket with the proper parameters creates or replaces a cross-origin resource sharing (CORS) configuration for a bucket.

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Sample request**

This is an example of adding a CORS configuration that allows requests from `www.ibm.com` to issue `GET`, `PUT`, and `POST` requests to the bucket.

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3-api.us-geo.objectstorage.softlayer.net
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

**Sample response**

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

**Sample request**

This is an example of deleting a CORS configuration for a bucket.

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

The server responds with `204 No Content`.
