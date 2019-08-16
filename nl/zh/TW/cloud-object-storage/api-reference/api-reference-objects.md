---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, objects

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

# 物件作業
{: #object-operations}

這些作業會讀取、寫入及配置儲存區內所含的物件。

如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
{:tip}

## 上傳物件
{: #object-operations-put}

給定物件路徑的 `PUT` 會將要求內文上傳為物件。單一執行緒中上傳的所有物件都應該小於 500MB（[多個組件中上傳的](/docs/services/cloud-object-storage?topic=cloud-object-storage-large-objects)物件最大可以為 10TB）。

**附註**：個人識別資訊 (PII)：建立儲存區及（或）新增物件時，請確保不要使用任何可依下列方式來識別任何使用者（自然人）的資訊：依名稱、位置或任何其他方法。
{:tip}

** 語法 **

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**範例要求**

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

**要求範例 (HMAC)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
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

**要求範例（HMAC 預先簽署的 URL）**

```http
PUT /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**範例回應**

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

## 取得物件的標頭
{: #object-operations-head}

給定物件路徑的 `HEAD` 會擷取該物件的標頭。

請注意，針對使用 SSE-KP 加密的物件所傳回的 `Etag` 值，**不**會是原始未加密物件的 MD5 雜湊。
{:tip}

** 語法 **

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**範例要求**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**要求範例（HMAC 標頭）**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 下載物件
{: #object-operations-get}

給定物件路徑的 `GET` 會下載物件。

請注意，針對使用 SSE-C/SSE-KP 加密的物件所傳回的 `Etag` 值，**不**會是原始未加密物件的 MD5 雜湊。
{:tip}

** 語法 **

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### 選用標頭
{: #object-operations-get-headers}

標頭 | 類型 | 說明
--- | ---- | ------------
`range` |字串   | 傳回指定範圍內物件的位元組。

**範例要求**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 標頭）**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 刪除物件
{: #object-operations-delete}

給定物件路徑的 `DELETE` 會刪除物件。

** 語法 **

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**範例要求**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**要求範例（HMAC 標頭）**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 刪除多個物件
{: #object-operations-multidelete}

給定儲存區路徑及適當參數的 `POST`，會刪除指定的物件集。需要一個指定要求內文 base64 編碼 MD5 雜湊的 `Content-MD5` 標頭。

必要的 `Content-MD5` 標頭必須是 base64 編碼 MD5 雜湊的二進位表示法。

**附註：**如果找不到要求中指定的物件，結果會傳回為已刪除。 

### 選用元素
{: #object-operations-multidelete-options}

|標頭|類型|說明|
|---|---|---|
|`Quiet`|布林|啟用要求的無聲模式。|

要求最多可以包含您要刪除的 1000 個金鑰。雖然這對於減少每個要求的額外負擔非常有用，但在刪除大量金鑰時請小心。同時請將物件大小列入考量，以確保適當的效能。
{:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

** 語法 **

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```

**範例要求**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**要求範例（HMAC 標頭）**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
POST /apiary?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&delete=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
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

**範例回應**

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

## 複製物件
{: #object-operations-copy}

給定新物件路徑的 `PUT` 會建立由 `x-amz-copy-source` 標頭所指定的另一個物件的新副本。除非另有變更，否則 meta 資料會保持相同。

**附註**：個人識別資訊 (PII)：建立儲存區及（或）新增物件時，請確保不要使用任何可依下列方式來識別任何使用者（自然人）的資訊：依名稱、位置或任何其他方法。
{:tip}


**附註**：將項目從啟用 *Key Protect* 的儲存區複製到另一個地區中的目的地儲存區受到限制，而且會導致 `500 - Internal Error`。
{:tip}

** 語法 **

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### 選用標頭
{: #object-operations-copy-options}

標頭 | 類型 | 說明
--- | ---- | ------------
`x-amz-metadata-directive` | 字串（`COPY` 或 `REPLACE`）| `REPLACE` 會將原始 meta 資料改寫為提供的新 meta 資料。
`x-amz-copy-source-if-match` | 字串 (`ETag`)| 如果指定的 `ETag` 符合來源物件，則會建立副本。
`x-amz-copy-source-if-none-match` | 字串 (`ETag`)| 如果指定的 `ETag` 與來源物件不同，則會建立副本。
`x-amz-copy-source-if-unmodified-since` | 字串 (timestamp)| 如果來源物件自指定的日期後未被修改，則會建立副本。 日期必須是有效的 HTTP 日期（例如 `Wed, 30 Nov 2016 20:21:38 GMT`）。
`x-amz-copy-source-if-modified-since` | 字串 (timestamp)| 如果來源物件自指定的日期後已被修改，則會建立副本。 日期必須是有效的 HTTP 日期（例如 `Wed, 30 Nov 2016 20:21:38 GMT`）。

**範例要求**

此基本範例會從 `garden` 儲存區中取得 `bee` 物件，並在新索引鍵為 `wild-bee` 的 `apiary` 儲存區中建立副本。

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 標頭）**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 檢查物件的 CORS 配置
{: #object-operations-options}

給定物件路徑與原點及要求類型的 `OPTIONS` 會查看是否可以使用該要求類型從該原點存取該物件。與所有其他要求不同，OPTIONS 要求不需要 `authorization` 或 `x-amx-date` 標頭。

** 語法 **

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**範例要求**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 標頭）**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 透過多個組件上傳物件
{: #object-operations-multipart}

使用較大型的物件時，建議使用多部分上傳作業，將物件寫入至 {{site.data.keyword.cos_full}}。單一物件上傳可以作為一組組件來執行，而且可以依任何順序平行獨立上傳這些組件。上傳完成時，{{site.data.keyword.cos_short}} 接著會將所有組件呈現為單一物件。這提供許多優點：網路岔斷不會導致大型上傳失敗、可以在一段時間後暫停並重新啟動上傳，而且物件可以在建立時上傳。

多部分上傳僅適用於大於 5MB 的物件。對於小於 50GB 的物件，建議使用 20MB 到 100MB 的組件大小，以達到最佳效能。對於較大的物件，可以增加組件大小，而不會造成重大效能影響。多部分上傳限制為不超過 10,000 個組件，且各組件 5GB。

使用超過 500 個組件會導致 {{site.data.keyword.cos_short}} 效率不佳，因此應該盡可能避免使用。
{:tip}

由於涉及額外的複雜性，所以建議開發人員使用提供多部分上傳支援的程式庫。

除非刪除物件，或使用 `AbortIncompleteMultipartUpload` 中斷多部分上傳，否則會持續保留不完整的多部分上傳。如果未中斷不完整的多部分上傳，則局部上傳會繼續使用資源。設計介面時，應該記住這一點，並清除不完整的多部分上傳。
{:tip}

在多個組件中上傳物件分為三個階段：

1. 起始上傳，並建立 `UploadId`。
2. 上傳個別組件，指定物件的循序組件號碼及 `UploadId`。
3. 完成上傳所有組件時，會藉由傳送具有 `UploadId` 的要求以及列出每個組件號碼及其個別 `Etag` 值的 XML 區塊來完成上傳。

## 起始多部分上傳
{: #object-operations-multipart-initiate}

針對具有查詢參數 `upload` 的物件發出 `POST`，會建立新的 `UploadId` 值，之後，所上傳物件的每個組件都會參照該值。

**附註**：個人識別資訊 (PII)：建立儲存區及（或）新增物件時，請確保不要使用任何可依下列方式來識別任何使用者（自然人）的資訊：依名稱、位置或任何其他方法。
{:tip}

** 語法 **

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**範例要求**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 標頭）**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 上傳組件
{: #object-operations-multipart-put-part}

針對具有查詢參數 `partNumber` 及 `uploadId` 的物件發出 `PUT` 要求，將會上傳物件的某個組件。組件可以依序或平行上傳，但必須依序編號。

**附註**：個人識別資訊 (PII)：建立儲存區及（或）新增物件時，請確保不要使用任何可依下列方式來識別任何使用者（自然人）的資訊：依名稱、位置或任何其他方法。
{:tip}

** 語法 **

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**範例要求**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**要求範例（HMAC 標頭）**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**要求範例（HMAC 預先簽署的 URL）**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**範例回應**

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

## 列出組件
{: #object-operations-multipart-list}

給定多部分物件（將作用中 `UploadID` 指定為查詢參數）路徑的 `GET`，會傳回所有物件組件的清單。


** 語法 **

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```

### 查詢參數
{: #object-operations-multipart-list-params}
參數 | 必要？ | 類型 | 說明
--- | ---- | ------------
`uploadId` | 必要 |字串   | 起始設定多部分上傳時傳回的上傳 ID。
`max-parts` |選用|字串   | 預設為 1,000。
`part-number​-marker` |選用|字串   | 定義組件清單的開始位置。

**範例要求**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 標頭）**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

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

## 完成多部分上傳
{: #object-operations-multipart-complete}

針對具有查詢參數 `uploadId` 且內文中具有適當 XML 區塊的物件發出 `POST` 要求，將會完成多部分上傳。

** 語法 **

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

**範例要求**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**要求範例（HMAC 標頭）**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**要求範例（HMAC 預先簽署的 URL）**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
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

**範例回應**

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
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

## 中斷不完整的多部分上傳
{: #object-operations-multipart-uploads}

針對具有查詢參數 `uploadId` 的物件發出 `DELETE` 要求，將會刪除多部分上傳的所有未完成組件。

** 語法 **

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**範例要求**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 標頭）**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**範例回應**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## 暫時還原保存物件
{: #object-operations-archive-restore}

針對具有查詢參數 `restore` 的物件發出 `POST` 要求，會要求暫時還原保存物件。需要有 `Content-MD5` 標頭作為有效負載的完整性檢查。

下載或修改物件之前，必須先還原保存物件。 必須指定物件的生命期限，在此之後，將會刪除物件的暫存副本。

在還原的副本可供存取之前，可能會有長達 15 小時的延遲。HEAD 要求可查看還原副本是否可供使用。

若要永久地還原物件，必須將它複製到沒有作用中生命週期配置的儲存區。

** 語法 **

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```

**有效負載元素**

要求的內文必須包含具有下列綱目的 XML 區塊：

|元素|類型|子項|上代|限制|
|---|---|---|---|---|
|RestoreRequest|Container|Days、GlacierJobParameters|無|無|
|Days|整數|無|RestoreRequest|指定暫時還原物件的生命期限。還原物件副本可能存在的天數下限是 1。在經歷還原期間之後，將會移除物件的暫存副本。|
|GlacierJobParameters|字串|Tier|RestoreRequest|無|
|Tier|字串|無|GlacierJobParameters|**必須**設為 `Bulk`。|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**範例要求**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 標頭）**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**要求範例（HMAC 預先簽署的 URL）**

```http
POST /apiary/queenbee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&restore&x-amz-signature={signature} HTTP/1.1
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**範例回應**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## 更新 meta 資料
{: #object-operations-metadata}

您可以利用下列兩種方式來更新現有物件的 meta 資料：
* 具有新 meta 資料及原始物件內容的 `PUT` 要求
* 使用指定原始物件作為副本來源的新 meta 資料來執行 `COPY` 要求

所有 meta 資料索引鍵的字首都必須為 `x-amz-meta-`
{: tip}

### 使用 PUT 來更新 meta 資料
{: #object-operations-metadata-put}

因為將會改寫內容，所以 `PUT` 要求需要現有物件的副本。
{: important}

** 語法 **

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**範例要求**

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

**範例回應**

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

### 使用 COPY 來更新 meta 資料
{: #object-operations-metadata-copy}

如需執行 `COPY` 要求的其他詳細資料，請按一下[這裡](#object-operations-copy)

** 語法 **

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**範例要求**

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

**範例回應**

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
