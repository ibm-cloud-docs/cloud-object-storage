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

# 儲存區作業
{: #compatibility-api-bucket-operations}


## 列出儲存區
{: #compatibility-api-list-buckets}

傳送至端點根目錄的 `GET` 要求會傳回與指定服務實例相關聯的儲存區清單。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

標頭                    | 類型   |必要？    |  說明
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | 字串   | 是 | 列出在此服務實例中建立的儲存區。

查詢參數                    | 值   |必要？    |  說明
--------------------------|--------|---| -----------------------------------------------------------
`extended` | 無 | 否 | 以清單形式提供 `LocationConstraint` meta 資料。

SDK 或 CLI 中不支援延伸清單。
{:note}

** 語法 **

```bash
GET https://{endpoint}/
```

**範例要求**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**範例回應**

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

### 取得延伸清單
{: #compatibility-api-list-buckets-extended}

** 語法 **

```bash
GET https://{endpoint}/?extended
```

**範例要求**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**範例回應**

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

## 建立儲存區
{: #compatibility-api-new-bucket}

傳送至端點根目錄且後面接著字串的 `PUT` 要求，將會建立儲存區。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。儲存區名稱必須是廣域唯一的且符合 DNS 標準；長度介於 3 到 63 個字元之間的名稱必須是小寫字母、數字及橫線。儲存區名稱的開頭和結尾必須是小寫字母或數字。不容許與 IP 位址類似的儲存區名稱。此作業不會使用作業特定查詢參數。

儲存區名稱必須是唯一的，因為公用雲端中的所有儲存區都會共用廣域名稱空間。這可讓您存取儲存區，而不需要提供任何服務實例或帳戶資訊。同時無法建立名稱開頭為 `cosv1-` 或 `account-` 的儲存區，因為系統保留這些字首。
{:important}

標頭                                        | 類型   | 說明
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | 字串     |  此標頭會參照將在其中建立儲存區的服務實例，以及對其收取資料使用費用的服務實例。

**附註**：個人識別資訊 (PII)：建立儲存區及（或）新增物件時，請確保不要使用任何可依下列方式來識別任何使用者（自然人）的資訊：依名稱、位置，或是儲存區或物件名稱中的任何其他方法。
{:tip}

** 語法 **

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**範例要求**

此範例說明如何建立稱為 'images' 的新儲存區。

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**範例回應**

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

## 建立具有不同儲存空間類別的儲存區
{: #compatibility-api-storage-class}

若要建立具有不同儲存空間類別的儲存區，請傳送 XML 區塊，並在對儲存區端點的 `PUT` 要求內文中使用 `{provisioning code}` 的 `LocationConstraint` 來指定儲存區配置。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。請注意，會套用標準儲存區[命名規則](#compatibility-api-new-bucket)。此作業不會使用作業特定查詢參數。

標頭                                        | 類型   | 說明
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | 字串     |  此標頭會參照將在其中建立儲存區的服務實例，以及對其收取資料使用費用的服務實例。

** 語法 **

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

您可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)中參照 `LocationConstraint` 的有效佈建碼清單。

**範例要求**

此範例說明如何建立稱為 'vault-images' 的新儲存區。

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

**範例回應**

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

## 使用 Key Protect 受管理加密金鑰 (SSE-KP) 建立新的儲存區
{: #compatibility-api-key-protect}

若要建立 Key Protect 在其中管理加密金鑰的儲存區，需要可以存取位於與新儲存區相同位置中的作用中 Key Protect 服務實例。此作業不會使用作業特定查詢參數。

如需使用 Key Protect 來管理加密金鑰的相關資訊，請參閱[文件](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)。

請注意，「跨區域」配置中**無法**使用 Key Protect，而且任何 SSE-KP 儲存區都必須是「地區」。
{:tip}

標頭                                        | 類型   | 說明
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | 字串     |  此標頭會參照將在其中建立儲存區的服務實例，以及對其收取資料使用費用的服務實例。
`ibm-sse-kp-encryption-algorithm` | 字串   | 此標頭用來指定要與使用 Key Protect 所儲存的加密金鑰搭配使用的演算法及金鑰大小。此值必須設為字串 `AES256`。
`ibm-sse-kp-customer-root-key-crn` | 字串   | 此標頭用來參照 Key Protect 用來加密此儲存區的特定根金鑰。此值必須是根金鑰的完整 CRN。

** 語法 **

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**範例要求**

此範例說明如何建立稱為 'secure-files' 的新儲存區。

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**範例回應**

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

## 擷取儲存區的標頭
{: #compatibility-api-head-bucket}

對儲存區發出的 `HEAD` 將會傳回該儲存區的標頭。

`HEAD` 要求不會傳回內文，因此無法傳回 `NoSuchBucket` 這類特定錯誤訊息，只會傳回 `NotFound`。
{:tip}

** 語法 **

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**範例要求**

此範例說明如何提取 'images' 儲存區的標頭。

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**範例回應**

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

**範例要求**

具有 Key Protect 加密的儲存區上的 `HEAD` 要求會傳回額外標頭。

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**範例回應**

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

## 列出給定儲存區中的物件（第 2 版）
{: #compatibility-api-list-objects-v2}

定址到儲存區的 `GET` 要求會傳回物件清單、一次限制為 1,000 個，並依非辭典編纂順序傳回。回應中所傳回的 `StorageClass` 值是預設值，因為在 COS 中未實作儲存空間類別作業。此作業不會使用作業特定標頭或有效負載元素。

** 語法 **

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### 選用的查詢參數
{: #compatibility-api-list-objects-v2-params}
名稱 | 類型 | 說明
--- | ---- | ------------
`list-type` | 字串   | 指出第 2 版的 API，而且值必須為 2。
`prefix` | 字串   | 將回應限制為開頭為 `prefix` 的物件名稱。
`delimiter` | 字串   | 對 `prefix` 與 `delimiter` 之間的物件進行分組。
`encoding-type` | 字串   | 如果物件名稱中使用 XML 不支援的 Unicode 字元，則此參數可以設為 `url`，以適當地編碼回應。
`max-keys` | 字串   | 限制要在回應中顯示的物件數。預設值及最大值為 1,000。
`fetch-owner` | 字串   | 依預設，第 2 版的 API 不包括 `Owner` 資訊。如果回應中想要有 `Owner` 資訊，請將此參數設為 `true`。
`continuation-token` | 字串   | 指定在截斷回應時要傳回的下一組物件（`IsTruncated` 元素會傳回 `true`）。<br/><br/>您的起始回應將包括 `NextContinuationToken` 元素。請在下一個要求中使用此記號作為 `continuation-token` 的值。
`start-after` | 字串   | 傳回特定金鑰物件之後的金鑰名稱。<br/><br/>*此參數僅適用於您的起始要求。*如果要求中包括 `continuation-token` 參數，則會忽略此參數。

**要求範例（簡單，使用 IAM）**

此要求會列出 "apiary" 儲存區內的物件。

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**回應範例（簡單）**

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

**要求範例（max-keys 參數）**

此要求會列出傳回的索引鍵上限設為 1 的 "apiary" 儲存區內的物件。

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**回應範例（截斷的回應）**

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

**要求範例（continuation-token 參數）**

此要求會列出指定接續記號的 "apiary" 儲存區內的物件。

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**回應範例（截斷的回應、continuation-token 參數）**

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

### 列出給定儲存區中的物件（已淘汰）
{: #compatibility-api-list-objects}

**附註：***基於舊版相容性，會包括此 API。*如需擷取儲存區中物件的建議方法，請參閱[第 2 版](#compatibility-api-list-objects-v2)。

定址到儲存區的 `GET` 要求會傳回物件清單、一次限制為 1,000 個，並依非辭典編纂順序傳回。回應中所傳回的 `StorageClass` 值是預設值，因為在 COS 中未實作儲存空間類別作業。此作業不會使用作業特定標頭或有效負載元素。

** 語法 **

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### 選用的查詢參數
{: #compatibility-api-list-objects-params}

名稱 | 類型 | 說明
--- | ---- | ------------
`prefix` | 字串   | 將回應限制為開頭為 `prefix` 的物件名稱。
`delimiter` | 字串   | 對 `prefix` 與 `delimiter` 之間的物件進行分組。
`encoding-type` | 字串   | 如果物件名稱中使用 XML 不支援的 Unicode 字元，則此參數可以設為 `url`，以適當地編碼回應。
`max-keys` | 字串   | 限制要在回應中顯示的物件數。預設值及最大值為 1,000。
`marker` | 字串   | 以 UTF-8 二進位順序指定清單應從其開始的物件。

**範例要求**

此要求會列出 "apiary" 儲存區內的物件。

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**範例回應**

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

## 刪除儲存區

對空儲存區發出的 `DELETE` 會刪除儲存區。刪除儲存區之後，系統會保留名稱 10 分鐘，在此之後，將會釋放它以供重複使用。*只能刪除空的儲存區。*

** 語法 **

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### 選用標頭

名稱 | 類型 | 說明
--- | ---- | ------------
`aspera-ak-max-tries` | 字串   | 指定嘗試刪除作業的次數。預設值為 2。


**範例要求**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

伺服器會回應 `204 No Content`。

如果要求刪除非空儲存區，則伺服器會回應 `409 Conflict`。

**範例回應**

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

## 列出儲存區的已取消/不完整的多部分上傳

使用適當的參數對儲存區發出的 `GET`，會擷取儲存區的任何已取消或不完整的多部分上傳的相關資訊。

** 語法 **

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**參數**

名稱 | 類型 | 說明
--- | ---- | ------------
`prefix` | 字串   | 將回應限制為開頭為 `{prefix}` 的物件名稱。
`delimiter` | 字串   | 對 `prefix` 與 `delimiter` 之間的物件進行分組。
`encoding-type` | 字串   | 如果物件名稱中使用 XML 不支援的 Unicode 字元，則此參數可以設為 `url`，以適當地編碼回應。
`max-uploads` | 整數 | 限制要在回應中顯示的物件數。預設值及最大值為 1,000。
`key-marker` | 字串   | 指定清單應從其開始的位置。
`upload-id-marker` | 字串   | 如果未指定 `key-marker`，則會予以忽略，否則會設定開始列出 `upload-id-marker` 上方組件的點。

**範例要求**

此範例說明如何擷取所有現行已取消及不完整的多部分上傳。

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**回應範例**（沒有進行中的多部分上傳）

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

## 列出儲存區的任何跨原點資源共用配置

使用適當的參數對儲存區發出的 `GET`，會擷取儲存區的跨原點資源共用 (CORS) 配置的相關資訊。

** 語法 **

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**範例要求**

此範例說明如何列出 "apiary" 儲存區上的 CORS 配置。

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應** 

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

## 建立儲存區的跨原點資源共用配置

使用適當的參數對儲存區發出的 `PUT`，會建立或取代儲存區的跨原點資源共用 (CORS) 配置。

必要的 `Content-MD5` 標頭必須是 base64 編碼 MD5 雜湊的二進位表示法。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

** 語法 **

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**範例要求**

此範例說明如何新增 CORS 配置，容許來自 `www.ibm.com` 的要求對儲存區發出 `GET`、`PUT` 及 `POST` 要求。

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


**範例回應**

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

## 刪除儲存區的任何跨原點資源共用配置

使用適當的參數對儲存區發出的 `DELETE`，會建立或取代儲存區的跨原點資源共用 (CORS) 配置。

** 語法 **

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**範例要求**

此範例說明如何刪除儲存區的 CORS 配置。

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

伺服器會回應 `204 No Content`。

----

## 列出儲存區的位置限制

使用適當的參數對儲存區發出的 `GET`，會擷取儲存區的位置資訊。

** 語法 **

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**範例要求**

此範例說明如何擷取 "apiary" 儲存區的位置。

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 建立儲存區生命週期配置
{: #compatibility-api-create-bucket-lifecycle}

`PUT` 作業會使用生命週期查詢參數來設定儲存區的生命週期設定。需要有 `Content-MD5` 標頭作為有效負載的完整性檢查。

** 語法 **

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**有效負載元素**

要求的內文必須包含具有下列綱目的 XML 區塊：

|元素|類型|子項|上代|限制|
|---|---|---|---|---|
|LifecycleConfiguration|Container|Rule|無|限制 1|
|Rule|Container|ID、Status、Filter、Transition|LifecycleConfiguration|限制 1|
|ID|字串|無|Rule|**必須**包含（``a-z、A- Z0-9``）及下列符號：`` !`_ .*'()- ``|
|Filter|字串|Prefix|Rule|**必須**包含 `Prefix` 元素。|
|Prefix|字串|無|Filter|**必須**設為 `<Prefix/>`。|
|Transition|Container|Days、StorageClass|Rule|限制 1。|
|Days|非負整數|無|Transition|**必須**是大於 0 的值。|
|Date|Date|無|Transition|**必須**為「ISO 8601 格式」，而且日期必須是未來日期。|
|StorageClass|字串|無|Transition|**必須**設為 GLACIER。|

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

**範例要求**

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

伺服器會回應 `200 OK`。

----

## 擷取儲存區生命週期配置

`GET` 作業會使用生命週期查詢參數來擷取儲存區的生命週期設定。

** 語法 **

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**範例要求**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**範例回應**

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

## 刪除儲存區的生命週期配置

使用適當的參數對儲存區發出的 `DELETE`，會移除儲存區的任何生命週期配置。

** 語法 **

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**範例要求**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

伺服器會回應 `204 No Content`。
