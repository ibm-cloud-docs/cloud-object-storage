---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, buckets

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 

# 存储区操作
{: #compatibility-api-bucket-operations}


## 列出存储区
{: #compatibility-api-list-buckets}

向端点根目录发送 `GET` 请求将返回与指定服务实例关联的存储区的列表。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

头|类型|必需？|描述
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id`|字符串|是|列出在此服务实例中创建的存储区。

查询参数|值|必需？|描述
--------------------------|--------|---| -----------------------------------------------------------
`extended`|无|否|在列表中提供 `LocationConstraint` 元数据。

SDK 或 CLI 中不支持扩展列表。
{:note}

**语法**

```bash
GET https://{endpoint}/
```

**示例请求**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**示例响应**

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

### 获取扩展列表
{: #compatibility-api-list-buckets-extended}

**语法**

```bash
GET https://{endpoint}/?extended
```

**示例请求**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**示例响应**

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

## 创建存储区
{: #compatibility-api-new-bucket}

向端点根目录发送 `PUT` 请求且后跟一个字符串将创建存储区。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。存储区名称必须全局唯一且符合 DNS 要求；名称长度在 3 到 63 个字符之间，并且必须由小写字母、数字和短划线组成。存储区名称必须以小写字母或数字开头和结尾。不允许使用类似 IP 地址的存储区名称。此操作不会使用特定于操作的查询参数。

存储区名称必须唯一，因为公共云中的所有存储区都共享一个全局名称空间。这样一来，无需提供任何服务实例或帐户信息就能访问存储区。此外，也不能创建名称以 `cosv1-` 或 `account-` 开头的存储区，因为这些是系统保留的前缀。
{:important}

头|类型|描述
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`|字符串|此头引用将在其中创建存储区的服务实例，以及将计费的数据使用量。

**注**：个人可标识信息 (PII)：创建存储区和/或添加对象时，请确保在存储区或对象的名称中，未使用可以通过名称、位置或其他任何方式识别到任何用户（自然人）的任何信息。
{:tip}

**语法**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**示例请求**

此示例创建名为“images”的新存储区。

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**示例响应**

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

## 创建使用其他存储类的存储区
{: #compatibility-api-storage-class}

要创建使用其他存储类的存储区，请发送 XML 块，其中在对存储区端点发出的 `PUT` 请求的主体中，使用值为 `{provisioning code}` 的 `LocationConstraint` 来指定存储区配置。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。请注意，标准存储区[命名规则](#compatibility-api-new-bucket)适用。此操作不会使用特定于操作的查询参数。

头|类型|描述
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`|字符串|此头引用将在其中创建存储区的服务实例，以及将计费的数据使用量。

**语法**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)中可以参考 `LocationConstraint` 的有效供应代码的列表。

**示例请求**

此示例创建名为“vault-images”的新存储区。

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

**示例响应**

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

## 使用 Key Protect 管理的加密密钥 (SSE-KP) 创建新存储区
{: #compatibility-api-key-protect}

要创建其中加密密钥由 Key Protect 管理的存储区，您需要有权访问新存储区所在位置的活动 Key Protect 服务实例。此操作不会使用特定于操作的查询参数。

有关使用 Key Protect 来管理加密密钥的更多信息，请参阅此[文档](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)。

请注意，Key Protect 在“跨区域”配置中**不**可用，并且任何 SSE-KP 存储区必须为“区域”。
{:tip}

头|类型|描述
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`|字符串|此头引用将在其中创建存储区的服务实例，以及将计费的数据使用量。
`ibm-sse-kp-encryption-algorithm`|字符串|此头用于指定要与使用 Key Protect 存储的加密密钥配合使用的算法和密钥大小。此值必须设置为字符串 `AES256`。
`ibm-sse-kp-customer-root-key-crn`|字符串|此头用于引用 Key Protect 用来加密此存储区的特定根密钥。此值必须是根密钥的完整 CRN。

**语法**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**示例请求**

此示例创建名为“secure-files”的新存储区。

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**示例响应**

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

## 检索存储区的头
{: #compatibility-api-head-bucket}

对存储区发出 `HEAD` 请求将返回该存储区的头。

`HEAD` 请求不会返回主体，因此无法返回 `NoSuchBucket` 之类的具体错误消息，而只会返回 `NotFound`。
{:tip}

**语法**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**示例请求**

此示例访存“images”存储区的头。

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**示例响应**

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

**示例请求**

对使用 Key Protect 加密的存储区发出 `HEAD` 请求将返回额外的头。

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**示例响应**

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

## 列出给定存储区中的对象 (V2)
{: #compatibility-api-list-objects-v2}

对存储区发出 `GET` 请求将返回对象列表，限制为一次只能返回 1,000 个对象，并且按非字典顺序列出。响应中返回的 `StorageClass` 值是缺省值，因为在 COS 中未实现存储类操作。此操作不会使用特定于操作的头或有效内容元素。

**语法**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### 可选查询参数
{: #compatibility-api-list-objects-v2-params}
名称|类型|描述
--- | ---- | ------------
`list-type`|字符串|指示 API V2，因此值必须为 2。
`prefix`|字符串|将响应限制为以 `prefix` 开头的对象名称。
`delimiter`|字符串|对 `prefix` 和 `delimiter` 之间的对象分组。
`encoding-type`|字符串|如果在对象名称中使用了 XML 不支持的 Unicode 字符，那么可以将此参数设置为 `url` 以正确对响应进行编码。
`max-keys`|字符串|限制要在响应中显示的对象数。缺省值和最大值均为 1,000。
`fetch-owner`|字符串|缺省情况下，API V2 不包含 `Owner` 信息。如果需要响应中包含 `Owner` 信息，请将此参数设置为 `true`。
`continuation-token`|字符串|指定截断响应时（`IsTruncated` 元素会返回 `true`）要返回的下一组对象。<br/><br/>初始响应将包含 `NextContinuationToken` 元素。在下一个请求中将此令牌用作 `continuation-token` 的值。
`start-after`|字符串|在特定键对象后返回键名。<br/><br/>*此参数仅在初始请求中有效。*如果请求中包含 `continuation-token` 参数，那么会忽略此参数。

**示例请求（使用 IAM 的简单示例）**

此请求列出“apiary”存储区中的对象。

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**示例响应（简单）**

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

**示例请求（max-keys 参数）**

此请求会列出“apiary”存储区中的对象，其中返回的最大键数设置为 1。

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**示例响应（已截断响应）**

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

**示例请求（continuation-token 参数）**

此请求会列出“apiary”存储区中的对象，其中指定了延续令牌。

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**示例响应（已截断响应，continuation-token 参数）**

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

### 列出给定存储区中的对象（不推荐）
{: #compatibility-api-list-objects}

**注：***包含此 API 是为了支持向后兼容性。*有关在存储区中检索对象的建议方法，请参阅 [V2](#compatibility-api-list-objects-v2)。

对存储区发出 `GET` 请求将返回对象列表，限制为一次只能返回 1,000 个对象，并且按非字典顺序列出。响应中返回的 `StorageClass` 值是缺省值，因为在 COS 中未实现存储类操作。此操作不会使用特定于操作的头或有效内容元素。

**语法**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### 可选查询参数
{: #compatibility-api-list-objects-params}

名称|类型|描述
--- | ---- | ------------
`prefix`|字符串|将响应限制为以 `prefix` 开头的对象名称。
`delimiter`|字符串|对 `prefix` 和 `delimiter` 之间的对象分组。
`encoding-type`|字符串|如果在对象名称中使用了 XML 不支持的 Unicode 字符，那么可以将此参数设置为 `url` 以正确对响应进行编码。
`max-keys`|字符串|限制要在响应中显示的对象数。缺省值和最大值均为 1,000。
`marker`|字符串|指定以 UTF-8 二进制顺序列出的列表应从哪个对象开始。

**示例请求**

此请求列出“apiary”存储区中的对象。

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**示例响应**

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

## 删除存储区

对空存储区发出 `DELETE` 请求将删除该存储区。删除存储区后，系统会将其名称锁定保留 10 分钟，10 分钟后将释放该名称以供复用。*只能删除空存储区。*

**语法**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### 可选头

名称|类型|描述
--- | ---- | ------------
`aspera-ak-max-tries`|字符串|指定尝试执行删除操作的次数。缺省值为 2。


**示例请求**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

服务器使用 `204 无内容`进行响应。

如果请求删除的是非空存储区，服务器将使用 `409 冲突`进行响应。

**示例响应**

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

## 列出存储区已取消/未完成的分块上传

使用相应参数对存储区发出 `GET` 请求将检索有关存储区任何已取消或未完成的分块上传的信息。

**语法**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**参数**

名称|类型|描述
--- | ---- | ------------
`prefix`|字符串|将响应限制为以 `{prefix}` 开头的对象名称。
`delimiter`|字符串|对 `prefix` 和 `delimiter` 之间的对象分组。
`encoding-type`|字符串|如果在对象名称中使用了 XML 不支持的 Unicode 字符，那么可以将此参数设置为 `url` 以正确对响应进行编码。
`max-uploads`|整数|限制要在响应中显示的对象数。缺省值和最大值均为 1,000。
`key-marker`|字符串|指定列表应该开始的位置。
`upload-id-marker`|字符串|如果未指定 `key-marker`，那么将忽略此值，否则会设置大于 `upload-id-marker` 的点，从该点开始列出分块。

**示例请求**

此示例检索所有当前已取消和未完成的分块上传。

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**示例响应**（没有进行中的分块上传）

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

## 列出存储区的任何跨源资源共享配置

使用相应参数对存储区发出 `GET` 请求将检索有关存储区的跨源资源共享 (CORS) 的信息。

**语法**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**示例请求**

此示例列出“apiary”存储区上的 CORS 配置。

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**示例响应** 

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

## 为存储区创建跨源资源共享配置

使用相应参数对存储区发出 `PUT` 请求将创建或替换存储区的跨源资源共享 (CORS) 配置。

必需的 `Content-MD5` 头需要是 Base64 编码的 MD5 散列的二进制表示。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**语法**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**示例请求**

此示例添加 CORS 配置，允许来自 `www.ibm.com` 的请求对存储区发出 `GET`、`PUT` 和 `POST` 请求。

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


**示例响应**

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

## 删除存储区的任何跨源资源共享配置

使用相应参数对存储区发出 `DELETE` 请求将删除存储区的跨源资源共享 (CORS) 配置。

**语法**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**示例请求**

此示例删除存储区的 CORS 配置。

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

服务器使用 `204 无内容`进行响应。

----

## 列出存储区的位置约束

使用相应参数对存储区发出 `GET` 请求将检索存储区的位置信息。

**语法**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**示例请求**

此示例检索“apiary”存储区的位置。

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**示例响应**

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

## 创建存储区生命周期配置
{: #compatibility-api-create-bucket-lifecycle}

`PUT` 操作使用生命周期查询参数来配置存储区的生命周期设置。需要 `Content-MD5` 头作为有效内容的完整性检查。

**语法**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**有效内容元素**

请求主体必须包含具有以下模式的 XML 块：

|元素|类型|子代|祖代|约束|
|---|---|---|---|---|
|LifecycleConfiguration|容器|Rule|无|限制 1|
|Rule|容器|ID、Status、Filter、Transition|LifecycleConfiguration|限制 1|
|ID|字符串|无|Rule|**必须**由 ``a-z、A-Z 和 0-9`` 以及以下符号组成：``!`_ .*'()-``|
|Filter|字符串|Prefix|Rule|**必须**包含 `Prefix` 元素。|
|Prefix|字符串|无|Filter|**必须**设置为 `<Prefix/>`。|
|Transition|容器|Days、StorageClass|Rule|限制为 1。|
|Days|非负整数|无|Transition|**必须**为大于 0 的值。|
|Date|日期|无|Transition|**必须**为 ISO 8601 格式，并且日期必须是未来的日期。|
|StorageClass|字符串|无|Transition|**必须**设置为 GLACIER。|

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

**示例请求**

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

服务器使用 `200 正常`进行响应。

----

## 检索存储区生命周期配置

`GET` 操作使用生命周期查询参数来检索存储区的生命周期设置。

**语法**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**示例请求**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**示例响应**

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

## 删除存储区的生命周期配置

使用相应参数对存储区发出 `DELETE` 请求将除去存储区的任何生命周期配置。

**语法**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**示例请求**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

服务器使用 `204 无内容`进行响应。
