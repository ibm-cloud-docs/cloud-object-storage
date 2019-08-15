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

# バケット操作
{: #compatibility-api-bucket-operations}


## バケットのリスト
{: #compatibility-api-list-buckets}

`GET` 要求がエンドポイント・ルートに送信され、指定されたサービス・インスタンスと関連付けられたバケットのリストが返されます。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

ヘッダー                    | タイプ   | 必須 |  説明
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | ストリング | はい | このサービス・インスタンス内の作成済みバケットをリストします。

照会パラメーター          | 値   | 必須 |  説明
--------------------------|--------|---| -----------------------------------------------------------
`extended` | なし | いいえ | リスト内の `LocationConstraint` メタデータを提供します。

拡張リストは SDK および CLI ではサポートされません。
{:note}

**構文**

```bash
GET https://{endpoint}/
```

**要求例**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**応答例**

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

### 拡張リストの取得
{: #compatibility-api-list-buckets-extended}

**構文**

```bash
GET https://{endpoint}/?extended
```

**要求例**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**応答例**

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

## バケットの作成
{: #compatibility-api-new-bucket}

エンドポイント・ルートの後にストリングが続くものに `PUT` 要求が送信されてバケットが作成されます。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。バケット名は、グローバルに固有でなければならず、DNS に準拠していなければなりません。つまり、名前は 3 文字から 63 文字の長さで、小文字、数字、およびダッシュのみを使用する必要があります。バケット名の先頭と末尾は小文字または数字でなければなりません。IP アドレスに類似したバケット名は許可されません。この操作では操作固有の照会パラメーターは使用されません。

パブリック・クラウド内のすべてのバケットが 1 つのグローバル名前空間を共有するため、バケット名は固有である必要があります。これにより、サービス・インスタンスやアカウント情報の指定を必要とせずにバケットにアクセスすることが可能になります。また、`cosv1-` および `account-` はシステムで予約済みであるため、これらの接頭部で始まる名前のバケットを作成することはできません。
{:important}

ヘッダー                                        | タイプ   | 説明
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | ストリング  | このヘッダーが指すサービス・インスタンスが、バケットが作成される場所となり、データ使用についての請求先になります。

**注:** 個人情報 (PII): パケットの作成またはオブジェクトの追加を行うときには、バケットまたはオブジェクトの名前に、ユーザー (個人) を名前、場所、またはその他の方法で特定できる情報を使用しないように注意してください。
{:tip}

**構文**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**要求例**

以下は、「images」という名前の新規バケットを作成する例です。

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**応答例**

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

## 異なるストレージ・クラスを使用するバケットの作成
{: #compatibility-api-storage-class}

異なるストレージ・クラスのバケットを作成するには、`PUT` 要求の本体内の XML ブロックで `{provisioning code}` の `LocationConstraint` を含むバケット構成を指定してバケット・エンドポイントに送信します。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。標準バケット[命名規則](#compatibility-api-new-bucket)が適用されることに注意してください。この操作では操作固有の照会パラメーターは使用されません。

ヘッダー                                        | タイプ   | 説明
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | ストリング  | このヘッダーが指すサービス・インスタンスが、バケットが作成される場所となり、データ使用についての請求先になります。

**構文**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

`LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラス・ガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)を参照してください。

**要求例**

以下は、「vault-images」という名前の新規バケットを作成する例です。

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

**応答例**

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

## Key Protect によって管理される暗号鍵 (SSE-KP) を使用する新規バケットの作成
{: #compatibility-api-key-protect}

Key Protect によって暗号鍵が管理されるバケットを作成するには、新規バケットと同じ場所にあるアクティブな Key Protect サービス・インスタンスにアクセスできる必要があります。この操作では操作固有の照会パラメーターは使用されません。

Key Protect を使用した暗号鍵の管理について詳しくは、[資料を参照](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)してください。

Key Protect は「クロス地域」構成では使用可能では**ない**ため、SSE-KP バケットは「地域」でなければならないことに注意してください。
{:tip}

ヘッダー                                        | タイプ   | 説明
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | ストリング  | このヘッダーが指すサービス・インスタンスが、バケットが作成される場所となり、データ使用についての請求先になります。
`ibm-sse-kp-encryption-algorithm` | ストリング | このヘッダーは、Key Protect を使用して保管される暗号鍵で使用されるアルゴリズムおよび鍵サイズを指定するために使用されます。この値は、ストリング `AES256` に設定する必要があります。
`ibm-sse-kp-customer-root-key-crn`  | ストリング | このヘッダーは、Key Protect がこのバケットを暗号化するために使用する特定のルート・キーを参照するために使用されます。この値は、ルート・キーの完全な CRN でなければなりません。

**構文**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**要求例**

以下は、「secure-files」という名前の新規バケットを作成する例です。

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**応答例**

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

## バケットのヘッダーの取得
{: #compatibility-api-head-bucket}

`HEAD` がバケットに対して発行され、そのバケットのヘッダーが返されます。

`HEAD` 要求は本体を返さないため、具体的なエラー・メッセージ (`NoSuchBucket` など) が返されることはなく、返されるのは `NotFound` のみです。
{:tip}

**構文**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**要求例**

以下は、「images」バケットのヘッダーを取り出す例です。

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**応答例**

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

**要求例**

Key Protect 暗号化を使用するバケットに対する `HEAD` 要求では、追加のヘッダーが返されます。

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**応答例**

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

## 特定のバケット内のオブジェクトのリスト (バージョン 2)
{: #compatibility-api-list-objects-v2}

バケットへの `GET` 要求は、オブジェクトのリストを返します。返されるのは一度に 1,000 までに制限され、辞書式順序ではありません。COS にはストレージ・クラス操作が実装されていないため、応答で返される `StorageClass` 値はデフォルト値です。この操作では操作固有のヘッダーおよびペイロード要素は使用されません。

**構文**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### オプションの照会パラメーター
{: #compatibility-api-list-objects-v2-params}
名前 | タイプ | 説明
--- | ---- | ------------
`list-type` | ストリング | バージョン 2 の API を示し、値は 2 でなければなりません。
`prefix` | ストリング | 応答を `prefix` で始まるオブジェクト名に制限します。
`delimiter` | ストリング | `prefix` と `delimiter` との間のオブジェクトをグループ化します。
`encoding-type` | ストリング | XML でサポートされない Unicode 文字がオブジェクト名に使用されている場合、このパラメーターを `url` に設定すると応答を適切にエンコードできます。
`max-keys` | ストリング | 応答に表示するオブジェクトの数を制限します。デフォルトおよび最大は 1,000 です。
`fetch-owner` | ストリング | デフォルトでは、バージョン 2 の API では `Owner` 情報は含まれません。応答に `Owner` 情報が必要な場合は、このパラメーターを `true` に設定します。
`continuation-token` | ストリング | 応答が切り捨てられた (`IsTruncated` 要素が `true` を返す) 場合に戻される次のオブジェクト・セットを指定します。<br/><br/>最初の応答には `NextContinuationToken` 要素が含まれます。このトークンを次の要求で `continuation-token` の値として使用します。
`start-after` | ストリング | 特定のキー・オブジェクトの後にあるキー名を返します。<br/><br/>*このパラメーターは最初の要求でのみ有効です。*  要求に `continuation-token` パラメーターが含まれている場合、このパラメーターは無視されます。

**要求の例 (IAM を使用した単純な例)**

次の要求は、「apiary」バケット内のオブジェクトをリストします。

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**応答の例 (単純な例)**

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

**要求の例 (max-keys パラメーター)**

次の要求は、返されるキーの最大数を 1 に設定して、「apiary」バケット内のオブジェクトをリストします。

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**応答の例 (切り捨てられた応答)**

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

**要求の例 (continuation-token パラメーター)**

次の要求は、continuation token を指定して、「apiary」バケット内のオブジェクトをリストします。

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**応答の例 (切り捨てられた応答、continuation-token パラメーター)**

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

### 特定のバケット内のオブジェクトのリスト (非推奨)
{: #compatibility-api-list-objects}

**注:** *この API は後方互換性のために組み込まれています。*  バケット内のオブジェクトを取得するための推奨される方法については、[バージョン 2](#compatibility-api-list-objects-v2) を参照してください。

バケットへの `GET` 要求は、オブジェクトのリストを返します。返されるのは一度に 1,000 までに制限され、辞書式順序ではありません。COS にはストレージ・クラス操作が実装されていないため、応答で返される `StorageClass` 値はデフォルト値です。この操作では操作固有のヘッダーおよびペイロード要素は使用されません。

**構文**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### オプションの照会パラメーター
{: #compatibility-api-list-objects-params}

名前 | タイプ | 説明
--- | ---- | ------------
`prefix` | ストリング | 応答を `prefix` で始まるオブジェクト名に制限します。
`delimiter` | ストリング | `prefix` と `delimiter` との間のオブジェクトをグループ化します。
`encoding-type` | ストリング | XML でサポートされない Unicode 文字がオブジェクト名に使用されている場合、このパラメーターを `url` に設定すると応答を適切にエンコードできます。
`max-keys` | ストリング | 応答に表示するオブジェクトの数を制限します。デフォルトおよび最大は 1,000 です。
`marker` | ストリング | リストが開始されるオブジェクトを UTF-8 バイナリー・オーダーで指定します。

**要求例**

次の要求は、「apiary」バケット内のオブジェクトをリストします。

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**応答例**

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

## バケットの削除

空のバケットに対して発行される `DELETE` は、そのバケットを削除します。バケットの削除後、その名前は 10 分間はシステムによって予約された状態になり、それ以降は再利用のために解放されます。*削除できるのは空のバケットのみです。*

**構文**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### オプションのヘッダー

名前 | タイプ | 説明
--- | ---- | ------------
`aspera-ak-max-tries` | ストリング | 削除操作を再試行する回数を指定します。デフォルト値は 2 です。


**要求例**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

サーバーは `204 No Content` と応答します。

空でないバケットの削除が要求された場合、サーバーは `409 Conflict` と応答します。

**応答例**

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

## バケットのキャンセル済み/未完了のマルチパート・アップロードのリスト

適切なパラメーターを指定してバケットに対して発行される `GET` は、バケットのキャンセル済みまたは未完了のマルチパート・アップロードに関する情報を取得します。

**構文**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**パラメーター**

名前 | タイプ | 説明
--- | ---- | ------------
`prefix` | ストリング | 応答を `{prefix}` で始まるオブジェクト名に制限します。
`delimiter` | ストリング | `prefix` と `delimiter` との間のオブジェクトをグループ化します。
`encoding-type` | ストリング | XML でサポートされない Unicode 文字がオブジェクト名に使用されている場合、このパラメーターを `url` に設定すると応答を適切にエンコードできます。
`max-uploads` | integer | 応答に表示するオブジェクトの数を制限します。デフォルトおよび最大は 1,000 です。
`key-marker` | ストリング | リストがどこから開始されるのかを指定します。
`upload-id-marker` | ストリング | `key-marker` が指定されていない場合は無視されます。それ以外の場合は、パートのリストが開始されるポイントを `upload-id-marker` の上に設定します。

**要求例**

以下は、現在キャンセル済みおよび未完了のすべてのマルチパート・アップロードを取得する例です。

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**応答の例** (進行中のマルチパート・アップロードなし)

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

## バケットのクロス・オリジン・リソース共有構成のリスト

適切なパラメーターを指定してバケットに対して発行される `GET` は、バケットのクロス・オリジン・リソース共有 (CORS) 構成に関する情報を取得します。

**構文**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**要求例**

以下は、「apiary」バケットの CORS 構成をリストする例です。

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**応答例** 

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

## バケットのクロス・オリジン・リソース共有構成の作成

適切なパラメーターを指定してバケットに対して発行される `PUT` は、バケットのクロス・オリジンリソース共有 (CORS) 構成を作成または置換します。

必須の `Content-MD5` ヘッダーは、base64 でエンコードされた MD5 ハッシュのバイナリー表現である必要があります。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**構文**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**要求例**

以下は、`www.ibm.com` からの要求が `GET` 要求、`PUT` 要求、および `POST` 要求をバケットに対して発行することを許可する CORS 構成を追加する例です。

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


**応答例**

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

## バケットのクロス・オリジン・リソース共有構成の削除

適切なパラメーターを指定してバケットに対して発行される `DELETE` は、バケットのクロス・オリジン・リソース共有 (CORS) 構成を作成または置換します。

**構文**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**要求例**

以下は、バケットの CORS 構成を削除する例です。

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

サーバーは `204 No Content` と応答します。

----

## バケットのロケーション制約のリスト

適切なパラメーターを使用してバケットに対して発行される `GET` は、バケットのロケーション情報を取得します。

**構文**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**要求例**

以下は、「apiary」バケットのロケーションを取得する例です。

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**応答例**

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

## バケットのライフサイクル構成の作成
{: #compatibility-api-create-bucket-lifecycle}

`PUT` 操作は、lifecycle 照会パラメーターを使用してバケットのライフサイクル設定を設定します。ペイロードの保全性検査のために `Content-MD5` ヘッダーが必須です。

**構文**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**ペイロード要素**

要求の本体には、以下のスキーマの XML ブロックが含まれている必要があります。

|要素|タイプ|子|上位|制約|
|---|---|---|---|---|
|LifecycleConfiguration|コンテナー|Rule|なし|限度 1|
|Rule|コンテナー|ID、Status、Filter、Transition|LifecycleConfiguration|限度 1|
|ID|ストリング|なし|Rule|``(a-z,A- Z0-9)`` と記号 `` !`_ .*'()- `` で構成される**必要があります**。|
|Filter|ストリング|Prefix|Rule|`Prefix` 要素を含んでいる**必要があります**。|
|Prefix|ストリング|なし|Filter|`<Prefix/>` に設定される**必要があります**。|
|Transition|コンテナー|Days、StorageClass|Rule|限度 1。|
|Days|負でない整数|なし|Transition|0 より大きい値である**必要があります**。|
|Date|Date|なし|Transition|ISO 8601 形式である**必要があり**、将来の日付でなければなりません。|
|StorageClass|ストリング|なし|Transition|GLACIER に設定される**必要があります**。|

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

**要求例**

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

サーバーは `200 OK` と応答します。

----

## バケットのライフサイクル構成の取得

`GET` 操作は、lifecycle 照会パラメーターを使用してバケットのライフサイクル設定を取得します。

**構文**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**要求例**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**応答例**

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

## バケットのライフサイクル構成の削除

適切なパラメーターを使用してバケットに対して発行される `DELETE` は、バケットのライフサイクル構成を削除します。

**構文**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**要求例**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

サーバーは `204 No Content` と応答します。
