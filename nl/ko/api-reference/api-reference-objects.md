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

# 오브젝트 오퍼레이션
{: #object-operations}

이러한 오퍼레이션은 버킷에 포함된 오브젝트를 읽고 쓰고 구성합니다.

엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.
{:tip}

## 오브젝트 업로드
{: #object-operations-put}

오브젝트에 대한 경로가 지정된 `PUT`은 요청 본문을 오브젝트로 업로드합니다. 단일 스레드로 업로드된 모든 오브젝트는 500MB보다 작아야 합니다([다중 파트로 업로드된](/docs/services/cloud-object-storage?topic=cloud-object-storage-large-objects) 오브젝트는 10TB일 수 있음).

**참고**: 개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}

**구문**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**요청 예**

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

**요청 예(HMAC)**

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

**요청 예(HMAC의 미리 서명된 URL)**

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

**응답 예**

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

## 오브젝트의 헤더 가져오기
{: #object-operations-head}

오브젝트에 대한 경로가 지정된 `HEAD`는 이 오브젝트의 헤더를 검색합니다.

SSE-KP를 사용하여 암호화된 오브젝트에 대해 리턴된 `Etag` 값은 원래 암호화되지 않은 오브젝트의 MD5 해시가 **아닙니다**.
{:tip}

**구문**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**요청 예**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**요청 예(HMAC 헤더)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 오브젝트 다운로드
{: #object-operations-get}

오브젝트에 대한 경로가 지정된 `GET`은 오브젝트를 다운로드합니다.

SSE-C/SSE-KP를 사용하여 암호화된 오브젝트에 대해 리턴된 `Etag` 값은 원래 암호화되지 않은 오브젝트의 MD5 해시가 **아닙니다**.
{:tip}

**구문**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### 선택적 헤더
{: #object-operations-get-headers}

헤더 |유형 |설명
--- | ---- | ------------
`range` | 문자열 | 지정된 범위 내의 오브젝트 바이트 수를 리턴합니다.

**요청 예**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC 헤더)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 오브젝트 삭제
{: #object-operations-delete}

오브젝트에 대한 경로가 지정된 `DELETE`는 오브젝트를 삭제합니다.

**구문**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**요청 예**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**요청 예(HMAC 헤더)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 다중 오브젝트 삭제
{: #object-operations-multidelete}

버킷에 대한 경로 및 적절한 매개변수가 지정된 `POST`는 지정된 오브젝트 세트를 삭제합니다. 요청 본문의 base64 인코딩 MD5 해시를 지정하는 `Content-MD5` 헤더가 필요합니다.

필수 `Content-MD5` 헤더는 base64 인코딩 MD5 해시의 2진 표시여야 합니다.

**참고:** 요청에 지정된 오브젝트가 없는 경우 결과가 삭제됨으로 리턴됩니다. 

### 선택적 요소
{: #object-operations-multidelete-options}

|헤더|유형|설명|
|---|---|---|
|`Quiet`|부울|요청에 정음 모드(quiet mode)를 사용합니다.|

요청에는 삭제할 최대 1000개의 키가 포함될 수 있습니다. 이는 요청당 오버헤드를 줄이는 데 매우 유용하지만, 많은 수의 키를 삭제하는 경우에는 주의하십시오. 또한, 적합한 성능을 보장하기 위한 오브젝트 크기도 고려하십시오.
{:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**구문**

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```

**요청 예**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**요청 예(HMAC 헤더)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

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

**응답 예**

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

## 오브젝트 복사
{: #object-operations-copy}

새 오브젝트에 대한 경로가 지정된 `PUT`은 `x-amz-copy-source` 헤더로 지정된 다른 오브젝트의 사본을 새로 작성합니다. 다르게 변경되지 않는 한, 메타데이터는 동일하게 유지됩니다.

**참고**: 개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}


**참고**: *Key Protect* 사용 버킷의 항목을 다른 지역의 대상 버킷에 복사하는 것은 제한되며 그 결과 `500 - Internal Error`가 발생합니다.
{:tip}

**구문**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### 선택적 헤더
{: #object-operations-copy-options}

헤더 |유형 |설명
--- | ---- | ------------
`x-amz-metadata-directive` | 문자열(`COPY` 또는 `REPLACE`) | `REPLACE`는 원래 메타데이터를 제공된 새 메타데이터로 겹쳐씁니다.
`x-amz-copy-source-if-match` | 문자열(`ETag`)| 지정된 `ETag`가 소스 오브젝트와 일치하는 경우 사본을 작성합니다.
`x-amz-copy-source-if-none-match` | 문자열(`ETag`)| 지정된 `ETag`가 소스 오브젝트와 다른 경우 사본을 작성합니다.
`x-amz-copy-source-if-unmodified-since` | 문자열(시간소인)| 지정된 날짜 이후 소스 오브젝트가 수정되지 않은 경우 사본을 작성합니다. 날짜는 올바른 HTTP 날짜여야 합니다(예: `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-copy-source-if-modified-since` | 문자열(시간소인)| 지정된 날짜 이후 소스 오브젝트가 수정된 경우 사본을 작성합니다. 날짜는 올바른 HTTP 날짜여야 합니다(예: `Wed, 30 Nov 2016 20:21:38 GMT`).

**요청 예**

이 기본 예에서는 `garden` 버킷에서 `bee` 오브젝트를 가져와 새 키 `wild-bee`로 `apiary` 버킷에 사본을 작성합니다.

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC 헤더)**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 오브젝트의 CORS 구성 확인
{: #object-operations-options}

오리진 및 요청 유형과 함께 오브젝트에 대한 경로가 지정된 `OPTIONS`는 요청 유형을 사용하여 이 오리진에서 해당 오브젝트에 액세스 가능한지 확인합니다. 다른 모든 요청과 달리 OPTIONS 요청에는 `authorization` 또는 `x-amx-date` 헤더가 필요하지 않습니다.

**구문**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**요청 예**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC 헤더)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 다중 파트의 오브젝트 업로드
{: #object-operations-multipart}

대형 오브젝트에 대해 작업하는 경우에는 {{site.data.keyword.cos_full}}에 오브젝트를 기록할 때 다중 파트 업로드 오퍼레이션을 사용하는 것이 좋습니다. 단일 오브젝트의 업로드는 파트의 세트로 수행할 수 있으며 이러한 파트는 순서에 상관없이 독립적으로 병렬로 업로드할 수 있습니다. 업로드가 완료되면 {{site.data.keyword.cos_short}}가 모든 파트를 모아 하나의 오브젝트로 제시합니다. 이는 네트워크 장애가 발생해도 대용량 업로드가 실패하지 않는다는 점, 업로드를 일시정지하고 나중에 다시 시작할 수 있다는 점, 오브젝트가 작성되는 즉시 업로드 가능한 점 등 많은 이점을 제공합니다. 

다중 파트 업로드는 5MB보다 큰 오브젝트에 대해서만 사용할 수 있습니다. 50GB보다 작은 오브젝트의 경우에는 최적의 성능을 위해 파트의 크기를 20MB - 100MB 범위로 설정하는 것이 좋습니다. 더 큰 오브젝트의 경우에는 성능에 큰 영향을 주지 않고 파트 크기를 늘릴 수 있습니다. 다중 파트 업로드는 각각 5GB인 최대 10,000개의 파트로 제한됩니다.

500개가 넘는 파트를 사용하면 {{site.data.keyword.cos_short}}에 비효율성이 나타나므로 가능한 한 피하는 것이 좋습니다.
{:tip}

관련된 추가 복잡도로 인해 개발자는 다중 파트 업로드 지원을 제공하는 라이브러리를 사용하는 것이 좋습니다.

오브젝트가 삭제되거나 다중 파트 업로드가 중단되며 `AbortIncompleteMultipartUpload`가 나타날 때까지 불완전한 다중 파트 업로드가 지속됩니다. 불완전한 다중 파트 업로드가 중단되지 않으면 파트 업로드가 계속 리소스를 사용합니다. 이 점을 염두에 두고 인터페이스를 설계하고 불완전한 다중 파트 업로드를 정리해야 합니다.
{:tip}

다중 파트로 오브젝트를 업로드하는 데는 세 가지 단계가 있습니다.

1. 업로드가 시작되고 `UploadId`가 작성됩니다.
2. 개별 파트는 순차 파트 번호와 오브젝트의 `UploadId`를 지정하여 업로드됩니다.
3. 모든 파트 업로드가 완료되면 `UploadId`인 요청 및 각 파트 번호와 해당 `Etag` 값을 나열하는 XML 블록을 보내 업로드가 완료됩니다.

## 다중 파트 업로드 시작
{: #object-operations-multipart-initiate}

조회 매개변수 `upload`를 사용하여 오브젝트에 발행된 `POST`는 새 `UploadId` 값을 작성하며, 이 값은 업로드되는 오브젝트의 각 파트에서 참조됩니다.

**참고**: 개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}

**구문**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**요청 예**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC 헤더)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 파트 업로드
{: #object-operations-multipart-put-part}

조회 매개변수 `partNumber` 및 `uploadId`를 사용하여 오브젝트에 발행된 `PUT` 요청이 오브젝트의 한 파트를 업로드합니다. 파트는 연속으로 또는 동시에 업로드될 수 있지만 순서대로 번호를 매겨야 합니다.

**참고**: 개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}

**구문**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**요청 예**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**요청 예(HMAC 헤더)**

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

**요청 예(HMAC의 미리 서명된 URL)**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**응답 예**

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

## 파트 나열
{: #object-operations-multipart-list}

활성 `UploadID`가 조회 매개변수로 지정된 다중 파트 오브젝트에 대한 경로가 지정된 `GET`은 오브젝트의 모든 파트 목록을 리턴합니다.


**구문**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```

### 조회 매개변수
{: #object-operations-multipart-list-params}
매개변수 | 필수 여부 |유형 |설명
--- | ---- | ------------
`uploadId` | 필수 | 문자열 | 다중 파트 업로드를 초기화할 때 리턴된 업로드 ID입니다.
`max-parts` | 선택사항 | 문자열 | 기본값은 1,000입니다.
`part-number​-marker` | 선택사항 | 문자열 | 파트 목록이 시작될 위치를 정의합니다.

**요청 예**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC 헤더)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

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

## 다중 파트 업로드 완료
{: #object-operations-multipart-complete}

조회 매개변수 `uploadId`와 본문의 적절한 XML 블록을 사용하여 오브젝트에 발행된 `POST` 요청이 다중 파트 업로드를 완료합니다.

**구문**

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

**요청 예**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**요청 예(HMAC 헤더)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**요청 예(HMAC의 미리 서명된 URL)**

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

**응답 예**

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

## 불완전한 다중 파트 업로드 중단
{: #object-operations-multipart-uploads}

조회 매개변수 `uploadId`로 오브젝트에 발행된 `DELETE` 요청이 다중 파트 업로드의 완료되지 않은 모든 파트를 삭제합니다.

**구문**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**요청 예**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC 헤더)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**응답 예**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## 아카이브된 오브젝트 임시 복원
{: #object-operations-archive-restore}

아카이브된 오브젝트의 임시 복원을 요청하기 위해 `POST` 요청이 조회 매개변수 `restore`를 사용하여 오브젝트에 발행됩니다. `Content-MD5` 헤더가 페이로드에 대한 무결성 검사로 필요합니다.

오브젝트를 다운로드하거나 수정하기 전에 아카이브된 오브젝트를 복원해야 합니다. 오브젝트의 수명을 지정해야 하며, 이후에는 오브젝트의 임시 사본이 삭제됩니다.

복원된 사본에 액세스할 수 있으려면 최대 15시간의 지연이 있을 수 있습니다. HAED 요청은 복원된 사본이 사용 가능한지 확인할 수 있습니다.

오브젝트를 영구적으로 복원하려면 활성 라이프사이클 구성이 없는 버킷에 이 오브젝트를 복사해야 합니다.

**구문**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```

**페이로드 요소**

요청 본문에 다음 스키마가 포함된 XML 블록이 있어야 합니다.

|요소|유형|하위|상위|제한조건|
|---|---|---|---|---|
|RestoreRequest|컨테이너|Days, GlacierJobParameters|없음|없음|
|Days|정수|없음|RestoreRequest|임시로 복원된 오브젝트의 수명을 지정했습니다. 오브젝트의 복원된 사본이 존재할 수 있는 최소 일 수는 1입니다. 복원 기간이 경과되면 오브젝트의 임시 사본이 제거됩니다.|
|GlacierJobParameters|문자열|Tier|RestoreRequest|없음|
|Tier|문자열|없음|GlacierJobParameters|`Bulk`로 설정**해야 합니다**.|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**요청 예**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC 헤더)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**요청 예(HMAC의 미리 서명된 URL)**

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

**응답 예**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## 메타데이터 업데이트
{: #object-operations-metadata}

기존 오브젝트의 메타데이터를 업데이트하는 데는 두 가지 방법이 있습니다. 
* 새 메타데이터와 원본 오브젝트 컨텐츠를 사용한 `PUT` 요청
* 원본 오브젝트를 복사 소스로 지정하여, 새 메타데이터로 `COPY` 요청 실행

모든 메타데이터 키에 접두부 `x-amz-meta-`를 추가해야 합니다.
{: tip}

### PUT을 사용한 메타데이터 업데이트
{: #object-operations-metadata-put}

컨텐츠를 겹쳐쓸 때 <code>PUT</code> 요청이 기존 오브젝트의 사본을 요구합니다.
{: important}

**구문**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**요청 예**

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

**응답 예**

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

### COPY를 사용한 메타데이터 업데이트
{: #object-operations-metadata-copy}

`COPY` 요청 실행에 대한 추가 세부사항을 보려면 [여기](#object-operations-copy)를 클릭하십시오.

**구문**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**요청 예**

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

**응답 예**

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
