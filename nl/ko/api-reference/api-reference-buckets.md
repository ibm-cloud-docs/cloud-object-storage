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

# 버킷 오퍼레이션
{: #compatibility-api-bucket-operations}


## 버킷 나열
{: #compatibility-api-list-buckets}

엔드포인트 루트로 보낸 `GET` 요청은 지정된 서비스 인스턴스와 연관된 버킷 목록을 리턴합니다. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.

헤더                      |유형   | 필수 여부 |설명
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | 문자열 |예 | 이 서비스 인스턴스에서 작성된 버킷을 나열합니다.

조회 매개변수             |값   | 필수 여부 |설명
--------------------------|--------|---| -----------------------------------------------------------
`extended` |없음 |아니오 | 목록에 `LocationConstraint` 메타데이터를 제공합니다.

SDK 또는 CLI에서는 확장된 목록이 지원되지 않습니다.
{:note}

**구문**

```bash
GET https://{endpoint}/
```

**요청 예**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**응답 예**

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

### 확장된 목록 가져오기
{: #compatibility-api-list-buckets-extended}

**구문**

```bash
GET https://{endpoint}/?extended
```

**요청 예**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**응답 예**

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

## 버킷 작성
{: #compatibility-api-new-bucket}

문자열이 뒤에 오는 엔드포인트 루트로 보낸 `PUT` 요청은 버킷을 작성합니다. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 버킷 이름은 전체적으로 고유하고 DNS를 준수해야 합니다. 3 - 63자의 이름이 소문자, 숫자 및 대시로 구성되어야 합니다. 버킷 이름은 소문자 또는 숫자로 시작하고 끝나야 합니다. IP 주소와 유사한 버킷 이름은 허용되지 않습니다. 이 오퍼레이션은 오퍼레이션 특정 조회 매개변수를 사용하지 않습니다.

퍼블릭 클라우드의 모든 버킷이 글로벌 네임스페이스를 공유하므로 버킷 이름이 고유해야 합니다. 이렇게 되면 서비스 인스턴스 또는 계정 정보를 제공하지 않아도 버킷에 액세스할 수 있습니다. 또한 `cosv1-` 또는 `account-` 접두부가 시스템에 예약되어 있으므로 이러한 접두부로 시작되는 이름의 버킷은 작성할 수 없습니다.
{:important}

헤더                                           |유형   |설명
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | 문자열 |  이 헤더는 버킷이 작성되고 데이터 사용량이 청구될 서비스 인스턴스를 참조합니다.

**참고**: 개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 버킷이나 오브젝트의 이름에 있는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}

**구문**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**요청 예**

이는 'images'라는 새 버킷을 작성하는 예입니다.

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**응답 예**

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

## 다른 스토리지 클래스로 버킷 작성
{: #compatibility-api-storage-class}

다른 스토리지 클래스로 버킷을 작성하려면 `PUT` 요청의 본문에 `{provisioning code}`의 `LocationConstraint`가 있는 버킷 구성을 지정하는 XML 블록을 버킷 엔드포인트로 보내십시오. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 표준 버킷 [이름 지정 규칙](#compatibility-api-new-bucket)이 적용됩니다. 이 오퍼레이션은 오퍼레이션 특정 조회 매개변수를 사용하지 않습니다.

헤더                                          |유형   |설명
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | 문자열 |  이 헤더는 버킷이 작성되고 데이터 사용량이 청구될 서비스 인스턴스를 참조합니다.

**구문**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

`LocationConstraint`에 대한 올바른 프로비저닝 코드 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)에서 참조할 수 있습니다.

**요청 예**

이는 'vault-images'라는 새 버킷을 작성하는 예입니다.

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

**응답 예**

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

## Key Protect 관리 암호화 키(SSE-KP)로 새 버킷 작성
{: #compatibility-api-key-protect}

Key Protect로 암호화 키를 관리하는 버킷을 작성하려면 새 버킷과 동일한 위치에 있는 활성 Key Protect 서비스 인스턴스에 대한 액세스 권한이 있어야 합니다. 이 오퍼레이션은 오퍼레이션 특정 조회 매개변수를 사용하지 않습니다.

Key Protect를 사용하여 암호화 키를 관리하는 방법에 대한 자세한 정보는 [문서를 참조](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)하십시오.

Key Protect를 교차 지역 구성에서 사용할 수 **없으며** SSE-KP 버킷이 지역이어야 합니다.
{:tip}

헤더                                          |유형   |설명
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | 문자열 |  이 헤더는 버킷이 작성되고 데이터 사용량이 청구될 서비스 인스턴스를 참조합니다.
`ibm-sse-kp-encryption-algorithm` | 문자열 | 이 헤더는 Key Protect를 사용하여 저장된 암호화 키와 함께 사용할 알고리즘 및 키 크기를 지정하는 데 사용됩니다. 이 값은 문자열 `AES256`으로 설정해야 합니다.
`ibm-sse-kp-customer-root-key-crn`  | 문자열 | 이 헤더는 이 버킷을 암호화하기 위해 Key Protect에서 사용되는 특정 루트 키를 참조하는 데 사용됩니다. 이 값은 루트 키의 전체 CRN이어야 합니다.

**구문**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**요청 예**

이는 'secure-files'라는 새 버킷을 작성하는 예입니다.

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**응답 예**

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

## 버킷의 헤더 검색
{: #compatibility-api-head-bucket}

버킷에 발행된 `HEAD`는 이 버킷의 헤더를 리턴합니다.

`HEAD` 요청은 본문을 리턴하지 않으므로 `NoSuchBucket`과 같은 특정 오류 메시지를 리턴할 수 없으며 `NotFound`만 리턴할 수 있습니다.
{:tip}

**구문**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**요청 예**

이는 'images' 버킷의 헤더를 페치하는 예입니다.

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**응답 예**

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

**요청 예**

Key Protect 암호화를 사용하는 버킷의 `HEAD` 요청은 추가 헤더를 리턴합니다.

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**응답 예**

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

## 지정된 버킷의 오브젝트 나열(버전 2)
{: #compatibility-api-list-objects-v2}

버킷으로 보낸 `GET` 요청이 오브젝트 목록(한 번에 1,000개로 제한됨)을 리턴하며 이는 사전식이 아닌 순서로 리턴됩니다. 스토리지 클래스 오퍼레이션이 COS에 구현되지 않았으므로 응답에 리턴된 `StorageClass` 값은 기본값입니다. 이 오퍼레이션은 오퍼레이션 특정 헤더 또는 페이로드 요소를 사용하지 않습니다.

**구문**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### 선택적 조회 매개변수
{: #compatibility-api-list-objects-v2-params}
이름 |유형 |설명
--- | ---- | ------------
`list-type` | 문자열 | API의 버전 2를 표시하며 값은 2여야 합니다.
`prefix` | 문자열 | `prefix`로 시작하는 오브젝트 이름에 대한 응답을 제한합니다.
`delimiter` | 문자열 | `prefix`와 `delimiter` 간의 오브젝트를 그룹화합니다.
`encoding-type` | 문자열 | XML에서 지원되지 않는 유니코드 문자가 오브젝트 이름에 사용되는 경우, 응답을 올바르게 인코딩하기 위해 이 매개변수를 `url`로 설정할 수 있습니다.
`max-keys` | 문자열 | 응답에 표시할 오브젝트 수를 제한합니다. 기본값 및 최대값은 1,000입니다.
`fetch-owner` | 문자열 | API의 버전 2는 기본적으로 `소유자` 정보를 포함하지 않습니다. 응답에 `소유자` 정보가 필요한 경우 이 매개변수를 `true`로 설정하십시오.
`continuation-token` | 문자열 | 응답이 잘릴 때 리턴할 다음 오브젝트 세트를 지정합니다(`IsTruncated` 요소가 `true`를 리턴).<br/><br/>초기 응답에 `NextContinuationToken` 요소가 포함됩니다. 다음 요청에서 이 토큰을 `continuation-token`의 값으로 사용하십시오.
`start-after` | 문자열 | 특정 키 오브젝트 다음에 키 이름을 리턴합니다.<br/><br/>*이 매개변수는 초기 요청에서만 유효합니다.*  `continuation-token` 매개변수가 요청에 포함되어 있는 경우 이 매개변수는 무시됩니다.

**요청 예(IAM의 경우 단순함)**

이 요청은 "apiary" 버킷 내 오브젝트를 나열합니다.

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**응답 예(단순)**

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

**요청 예(max-keys 매개변수)**

이 요청은 리턴되는 최대 키가 1로 설정된 "apiary" 버킷 내 오브젝트를 나열합니다.

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**응답 예(잘린 응답)**

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

**요청 예(continuation-token 매개변수)**

이 요청은 연속 토큰이 지정된 "apiary" 버킷 내 오브젝트를 나열합니다.

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**응답 예(잘린 응답, continuation-token 매개변수)**

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

### 지정된 버킷의 오브젝트 나열(더 이상 사용되지 않음)
{: #compatibility-api-list-objects}

**참고:** *이 API는 역호환성을 위해 포함됩니다.* 버킷에서 오브젝트를 검색하는 권장 방법은 [버전 2](#compatibility-api-list-objects-v2)를 참조하십시오.

버킷으로 보낸 `GET` 요청이 오브젝트 목록(한 번에 1,000개로 제한됨)을 리턴하며 이는 사전식이 아닌 순서로 리턴됩니다. 스토리지 클래스 오퍼레이션이 COS에 구현되지 않았으므로 응답에 리턴된 `StorageClass` 값은 기본값입니다. 이 오퍼레이션은 오퍼레이션 특정 헤더 또는 페이로드 요소를 사용하지 않습니다.

**구문**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### 선택적 조회 매개변수
{: #compatibility-api-list-objects-params}

이름 |유형 |설명
--- | ---- | ------------
`prefix` | 문자열 | `prefix`로 시작하는 오브젝트 이름에 대한 응답을 제한합니다.
`delimiter` | 문자열 | `prefix`와 `delimiter` 간의 오브젝트를 그룹화합니다.
`encoding-type` | 문자열 | XML에서 지원되지 않는 유니코드 문자가 오브젝트 이름에 사용되는 경우, 응답을 올바르게 인코딩하기 위해 이 매개변수를 `url`로 설정할 수 있습니다.
`max-keys` | 문자열 | 응답에 표시할 오브젝트 수를 제한합니다. 기본값 및 최대값은 1,000입니다.
`marker` | 문자열 | UTF-8 2진 순서로 목록이 시작되는 오브젝트를 지정합니다.

**요청 예**

이 요청은 "apiary" 버킷 내 오브젝트를 나열합니다.

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**응답 예**

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

## 버킷 삭제

빈 버킷에 발행된 `DELETE`는 버킷을 삭제합니다. 버킷을 삭제하면 이름이 10분 동안 시스템에 보관되며 이후 재사용을 위해 해제됩니다. *빈 버킷만 삭제할 수 있습니다.*

**구문**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### 선택적 헤더

이름 |유형 |설명
--- | ---- | ------------
`aspera-ak-max-tries` | 문자열 | 삭제 오퍼레이션을 시도하는 횟수를 지정합니다. 기본값은 2입니다.


**요청 예**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

서버는 `204 No Content`로 응답합니다.

비어 있지 않은 버킷이 삭제 요청되면 서버가 `409 Conflict`로 응답합니다.

**응답 예**

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

## 버킷에 대한 취소되거나 불완전한 다중 파트 업로드 나열

적절한 매개변수가 있는 버킷에 발행된 `GET`은 버킷에 대한 취소되거나 불완전한 다중 파트 업로드에 대한 정보를 검색합니다.

**구문**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**매개변수**

이름 |유형 |설명
--- | ---- | ------------
`prefix` | 문자열 | `{prefix}`로 시작하는 오브젝트 이름에 대한 응답을 제한합니다.
`delimiter` | 문자열 | `prefix`와 `delimiter` 간의 오브젝트를 그룹화합니다.
`encoding-type` | 문자열 | XML에서 지원되지 않는 유니코드 문자가 오브젝트 이름에 사용되는 경우, 응답을 올바르게 인코딩하기 위해 이 매개변수를 `url`로 설정할 수 있습니다.
`max-uploads` | 정수 | 응답에 표시할 오브젝트 수를 제한합니다. 기본값 및 최대값은 1,000입니다.
`key-marker` | 문자열 | 목록이 시작되는 위치를 지정합니다.
`upload-id-marker` | 문자열 | `key-marker`를 지정하지 않으면 무시되며, 지정한 경우 `upload-id-marker` 위 파트 나열을 시작할 지점이 설정됩니다.

**요청 예**

이는 현재 취소되고 불완전한 다중 파트 업로드를 모두 검색하는 에제입니다.

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 에제**(진행 중인 다중 파트 업로드가 없음)

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

## 버킷의 CORS(Cross-Origin Resource Sharing) 구성 나열

적절한 매개변수가 있는 버킷에 발행된 `GET`은 버킷의 CORS(Cross-Origin Resource Sharing) 구성에 대한 정보를 검색합니다.

**구문**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**요청 예**

이는 "apiary" 버킷의 CORS 구성을 나열하는 예입니다.

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예** 

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

## 버킷의 CORS(Cross-Origin Resource Sharing) 구성 작성

적절한 매개변수가 있는 버킷에 발행된 `PUT`은 버킷의 CORS(Cross-Origin Resource Sharing) 구성을 작성하거나 바꿉니다.

필수 `Content-MD5` 헤더는 base64 인코딩 MD5 해시의 2진 표시여야 합니다.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**구문**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**요청 예**

이는 `www.ibm.com`의 요청이 버킷에 `GET`, `PUT` 및 `POST` 요청을 발행할 수 있는 CORS 구성을 추가하는 예입니다.

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


**응답 예**

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

## 버킷의 CORS(Cross-Origin Resource Sharing) 구성 삭제

적절한 매개변수가 있는 버킷에 발행된 `DELETE`는 버킷의 CORS(Cross-Origin Resource Sharing) 구성을 작성하거나 바꿉니다.

**구문**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**요청 예**

이는 버킷에 대한 CORS 구성을 삭제하는 예입니다.

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

서버는 `204 No Content`로 응답합니다.

----

## 버킷에 대한 위치 제한조건 나열

적절한 매개변수가 있는 버킷에 발행된 `GET`은 버킷에 대한 위치 정보를 검색합니다.

**구문**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**요청 예**

이는 "apiary" 버킷의 위치를 검색하는 예입니다.

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 버킷 라이프사이클 구성 작성
{: #compatibility-api-create-bucket-lifecycle}

`PUT` 오퍼레이션은 라이프사이클 조회 매개변수를 사용하여 버킷에 대한 라이프사이클 설정을 지정합니다. `Content-MD5` 헤더가 페이로드에 대한 무결성 검사로 필요합니다.

**구문**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**페이로드 요소**

요청 본문에 다음 스키마가 포함된 XML 블록이 있어야 합니다.

|요소|유형|하위|상위|제한조건|
|---|---|---|---|---|
|LifecycleConfiguration|컨테이너|Rule|없음|한계 1|
|Rule|컨테이너|ID, Status, Filter, Transition|LifecycleConfiguration|한계 1|
|ID|문자열|없음|Rule|``(a-z,A- Z0-9)`` 및 `` !`_ .*'()- `` 기호로 구성**되어야 합니다**.|
|Filter|문자열|Prefix|Rule|`Prefix` 요소가 포함**되어야 합니다**.|
|Prefix|문자열|없음|Filter|`<Prefix/>`로 설정**되어야 합니다**.|
|Transition|컨테이너|Days, StorageClass|Rule|한계 1.|
|Days|음수가 아닌 정수|없음|Transition|0보다 큰 값**이어야 합니다**.|
|Date|날짜|없음|Transition|ISO 8601 형식**이어야 하고** 날짜는 미래여야 합니다.|
|StorageClass|문자열|없음|Transition|GLACIER로 설정**되어야 합니다**.|

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

**요청 예**

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

서버는 `200 OK`로 응답합니다.

----

## 버킷 라이프사이클 구성 검색

`GET` 오퍼레이션은 라이프사이클 조회 매개변수를 사용하여 버킷에 대한 라이프사이클 설정을 검색합니다.

**구문**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**요청 예**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**응답 예**

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

## 버켓에 대한 라이프사이클 구성 삭제

적절한 매개변수가 있는 버킷에 발행된 `DELETE`는 버킷에 대한 라이프사이클 구성을 제거합니다.

**구문**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**요청 예**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

서버는 `204 No Content`로 응답합니다.
