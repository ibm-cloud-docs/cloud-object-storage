---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# 전이 규칙으로 콜드 데이터 아카이브
{: #archive}

{{site.data.keyword.cos_full}} 아카이브는 거의 액세스되지 않는 데이터에 대한 [저비용](https://www.ibm.com/cloud/object-storage) 옵션입니다. 스토리지 티어(표준, 볼트, 콜드 볼트 및 Flex)에서 장기 오프라인 아카이브로 전이하여 데이터를 저장하거나 온라인 콜드 볼트 옵션을 사용할 수 있습니다.
{: shortdesc}

IBM Cloud Object Storage와 통합된 웹 콘솔, REST API 및 서드파티 도구를 사용하여 오브젝트를 아카이브할 수 있습니다. 

엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.
{:tip}

## 버킷에서 아카이브 정책 추가 또는 관리
{: #archive-add}

버킷에 대한 아카이브 정책을 작성하거나 수정할 때 다음을 고려하십시오.

* 언제든 아카이브 정책을 새 버킷 또는 기존 버킷에 추가할 수 있습니다. 
* 기존 아카이브 정책을 수정하거나 사용 안함으로 설정할 수 있습니다. 
* 새로 추가되거나 수정된 아카이브 정책은 업로드된 새 오브젝트에 적용되며 기존 오브젝트에는 영향을 주지 않습니다.

버킷에 업로드된 새 오브젝트를 즉시 아카이브하려면 아카이브 정책에서 0일을 입력하십시오.
{:tip}

아카이브는 특정 지역에서만 사용 가능합니다. 세부사항은 [통합 서비스](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)를 참조하십시오.
{:tip}

## 아카이브된 오브젝트 복원
{: #archive-restore}

아카이브된 오브젝트에 액세스하려면 원래 스토리지 티어로 복원해야 합니다. 오브젝트를 복원할 때 오브젝트를 사용할 수 있는 기간(일)을 지정할 수 있습니다. 지정된 기간이 끝나면 복원된 사본이 삭제됩니다. 

복원 프로세스에는 최대 12시간이 소요될 수 있습니다.
{:tip}

아카이브된 오브젝트 하위 상태는 다음과 같습니다.

* 아카이브됨: 아카이브된 상태의 오브젝트는 온라인 스토리지 티어(표준, 볼트, 콜드 볼트 및 Flex)에서 버킷의 아카이브 정책을 기반으로 한 오프라인 아카이브 티어로 이동되었습니다.
* 복원 중: 복원 중 상태의 오브젝트는 아카이브된 상태에서 원래 온라인 스토리지 티어로 복사되는 사본을 생성하는 중입니다.
* 복원됨: 복원됨 상태의 오브젝트는 지정된 시간 동안 원래 온라인 스토리지 티어로 복원된 아카이브된 오브젝트의 사본입니다. 기간이 끝나면 아카이브된 오브젝트를 유지보수하는 동안 오브젝트의 사본은 삭제됩니다.

## 제한사항
{: #archive-limitations}

아카이브 정책은 `PUT Bucket Lifecycle Configuration` S3 API 오퍼레이션의 서브세트를 사용하여 구현됩니다. 

지원되는 기능은 다음과 같습니다.
* 오브젝트가 아카이브 상태로 전이될 때 향후 일 수 또는 날짜를 지정합니다.
* 오브젝트에 대한 [만기 규칙](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry)을 설정합니다.

지원되는 기능은 다음과 같습니다.
* 버킷마다 여러 개의 전이 규칙이 있습니다.
* 접두부 또는 오브젝트 키를 사용하여 아카이브할 오브젝트를 필터링합니다.
* 스토리지 클래스 간에 티어를 지정합니다.

## REST API및 SDK 사용
{: #archive-api} 

### 버킷 라이프사이클 구성 작성
{: #archive-api-create} 
{: http}

이 `PUT` 오퍼레이션 구현은 `lifecycle` 조회 매개변수를 사용하여 버킷의 라이프사이클 설정을 지정합니다. 이 오퍼레이션은 지정된 버킷에 대한 단일 라이프사이클 정책 정의를 허용합니다. 정책은 다음 매개변수로 구성된 규칙으로 정의됩니다. `ID`, `Status` 및 `Transition`.
{: http}

전이 조치를 사용하면 정의된 기간이 지난 후에 버킷에 작성되는 향후 오브젝트를 아카이브됨 상태로 사용할 수 있습니다. 버킷에 대한 라이프사이클 정책의 변경사항은 이 버킷에 작성된 **새 오브젝트에만 적용**됩니다.

Cloud IAM 사용자에게 최소한 버킷에 라이프사이클 정책을 추가할 수 있는 `작성자` 역할이 있어야 합니다.

클래식 인프라 사용자는 소유자 권한을 가져야 하며 버킷에 라이프사이클 정책을 추가하기 위해 스토리지 계정에 버킷을 작성할 수 있어야 합니다.

이 오퍼레이션은 추가 오퍼레이션 특정 조회 매개변수를 사용하지 않습니다.
{: http}

헤더                      |유형   |설명
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | 문자열 | **필수**: 페이로드의 base64 인코딩 128비트 MD5 해시이며, 페이로드가 전송 중에 변경되지 않았는지 확인하기 위한 무결성 검사로 사용됩니다.
{: http}

요청 본문에 다음 스키마가 포함된 XML 블록이 있어야 합니다.
{: http}

| 요소                     |유형                 | 하위                                   | 상위                     | 제한조건                                                                                   |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | 컨테이너             | `Rule`                                 |없음                     | 한계 1.                                                                                  |
| `Rule`                   | 컨테이너             | `ID`, `Status`, `Filter`, `Transition` | `LifecycleConfiguration` | 한계 1.                                                                                  |
| `ID`                     | 문자열               |없음                                   | `Rule`                   | (`a-z,`A-Z0-9`) 및 다음 기호로 구성되어야 합니다. `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | 문자열               | `Prefix`                               | `Rule`                   | `Prefix` 요소가 포함되어야 합니다.                                                            |
| `Prefix`                 | 문자열               |없음                                   | `Filter`                 | `<Prefix/>`로 설정**되어야 합니다**.                                                           |
| `Transition`             | `컨테이너`          | `Days`, `StorageClass`                 | `Rule`                   | 한계 1.                                                                                  |
| `Days`                   | 음수가 아닌 정수     |없음                                   | `Transition`             | 0보다 큰 값이어야 합니다.                                                           |
| `Date`                   | 날짜                 |없음                                   | `Transistion`            | ISO 8601 형식이어야 하고 날짜는 미래여야 합니다.                            |
| `StorageClass`           | 문자열               |없음                                   | `Transition`             | `GLACIER`로 설정**해야 합니다**.        |
{: http}

__구문__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="예 1. 이 구문 예의 슬래시와 점 사용을 참고하십시오." caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
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
{: codeblock}
{: http}
{: caption="예 2. 오브젝트 라이프사이클 구성 작성을 위한 XML 샘플입니다." caption-side="bottom"}

__예__
{: http}

_샘플 요청_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="예 3. 오브젝트 라이프사이클 구성 작성을 위한 요청 헤더 샘플입니다." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="예 4. PUT 요청 본문을 위한 XML 샘플입니다." caption-side="bottom"}

_샘플 응답_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="예 5. 응답 헤더입니다." caption-side="bottom"}

---

### 버킷 라이프사이클 구성 검색
{: #archive-api-retrieve} 
{: http}

`GET` 오퍼레이션 구현은 `lifecycle` 조회 매개변수를 사용하여 버킷에 대한 라이프사이클 설정을 검색합니다. 

Cloud IAM 사용자에게 최소한 버킷의 라이프사이클을 검색할 수 있는 `독자` 역할이 있어야 합니다.

클래식 인프라 사용자에게 최소한 버킷의 라이프사이클 정책을 검색할 수 있는 버킷에 대한 `독자` 권한이 있어야 합니다.

이 오퍼레이션은 추가 오퍼레이션 특정 헤더, 조회 매개변수 또는 페이로드를 사용하지 않습니다.


__구문__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="예 6. GET 요청에 대한 구문의 변형입니다." caption-side="bottom"}

__예__
{: http}

_샘플 요청_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="예 7. 구성을 검색하기 위한 샘플 요청 헤더입니다." caption-side="bottom"}

_샘플 응답_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="예 8. GET 요청의 샘플 응답 헤더입니다." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="예 9. 응답 본문을 위한 XML 예입니다." caption-side="bottom"}

---

### 버킷 라이프사이클 구성 삭제
{: #archive-api-delete} {: http}

`DELETE` 오퍼레이션 구현은 `lifecycle` 조회 매개변수를 사용하여 버킷에 대한 라이프사이클 설정을 제거합니다. 규칙으로 정의된 전이는 더 이상 새 오브젝트에 대해 발생하지 않습니다. 

**참고:** 기존 전이 규칙은 규칙이 삭제되기 전에 버킷에 이미 작성된 오브젝트에 대해 유지됩니다.

Cloud IAM 사용자에게 최소한 버킷에서 라이프사이클 정책을 삭제할 수 있는 `작성자` 역할이 있어야 합니다.

클래식 인프라 사용자에게 버킷에서 라이프사이클 정책을 제거할 수 있는 버킷에 대한 `소유자` 권한이 있어야 합니다.

이 오퍼레이션은 추가 오퍼레이션 특정 헤더, 조회 매개변수 또는 페이로드를 사용하지 않습니다.

__구문__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="예 10. 이 구문 예의 슬래시와 점 사용을 참고하십시오." caption-side="bottom"}

__예__
{: http}

_샘플 요청_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="예 11. DELETE HTTP verb에 대한 샘플 요청 헤더입니다." caption-side="bottom"}

_샘플 응답_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="예 12. DELETE 요청의 샘플 응답입니다." caption-side="bottom"}

---

### 아카이브된 오브젝트 임시 복원 
{: #archive-api-restore} {: http}

이 `POST` 오퍼레이션 구현은 `restore` 조회 매개변수를 사용하여 아카이브된 오브젝트의 임시 복원을 요청합니다. 사용자는 오브젝트를 다운로드하거나 수정하기 전에 먼저 아카이브된 오브젝트를 복원해야 합니다. 사용자는 오브젝트를 복원할 때 오브젝트의 임시 사본을 삭제할 기간을 지정해야 합니다. 오브젝트는 버킷의 스토리지 클래스를 유지보수합니다.

복원된 사본에 액세스할 수 있으려면 최대 12시간의 지연이 있을 수 있습니다. `HEAD` 요청은 복원된 사본이 사용 가능한지 확인할 수 있습니다. 

사용자가 오브젝트를 영구적으로 복원하려면 활성 라이프사이클 구성이 없는 버킷에 복원된 오브젝트를 복사해야 합니다.

Cloud IAM 사용자는 오브젝트를 복원하기 위해 최소한 `작성자` 역할을 가져야 합니다.

클래식 인프라 사용자는 복원을 위해 최소한 버킷에 대한 `작성` 권한과 오브젝트에 대한 `읽기` 권한을 가져야 합니다.

이 오퍼레이션은 추가 오퍼레이션 특정 조회 매개변수를 사용하지 않습니다.

헤더                      |유형   |설명
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | 문자열 | **필수**: 페이로드의 base64 인코딩 128비트 MD5 해시이며, 페이로드가 전송 중에 변경되지 않았는지 확인하기 위한 무결성 검사로 사용됩니다.

요청 본문에 다음 스키마가 포함된 XML 블록이 있어야 합니다.

요소                     |유형      | 하위                                   | 상위                     | 제한조건
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | 컨테이너 | `Days`, `GlacierJobParameters`    |없음       |없음
`Days`                   | 정수 |없음 | `RestoreRequest` | 임시로 복원된 오브젝트의 수명을 지정했습니다. 오브젝트의 복원된 사본이 존재할 수 있는 최소 일 수는 1입니다. 복원 기간이 경과되면 오브젝트의 임시 사본이 제거됩니다.
`GlacierJobParameters` | 문자열 | `Tier` | `RestoreRequest` |없음
 `Tier` | 문자열 |없음 | `GlacierJobParameters` | `Bulk`로 설정**해야 합니다**.

성공한 응답은 오브젝트가 아카이브된 상태인 경우 `202`를 리턴하고 오브젝트가 이미 복원됨 상태인 경우 `200`을 리턴합니다. 오브젝트가 이미 복원됨 상태이고 오브젝트 복원에 대한 새 요청이 수신되면 `Days` 요소가 복원된 오브젝트의 만기 시간을 업데이트합니다.

__구문__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="예 13. 이 구문 예의 슬래시와 점 사용을 참고하십시오." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="예 14. 요청 본문을 위한 XML 모델입니다." caption-side="bottom"}

__예__
{: http}

_샘플 요청_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="예 15. 오브젝트 복원에 대한 샘플 요청 헤더입니다." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="예 16. 오브젝트 복원에 대한 샘플 요청 본문입니다." caption-side="bottom"}

_샘플 응답_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="예 17. 오브젝트 복원에 대한 응답입니다(`HTTP 202`)." caption-side="bottom"}

---

### 오브젝트의 헤더 가져오기
{: http}
{: #archive-api-head}

오브젝트에 대한 경로가 지정된 `HEAD`는 이 오브젝트의 헤더를 검색합니다. 이 오퍼레이션은 오퍼레이션 특정 조회 매개변수 또는 페이로드 요소를 사용하지 않습니다.

__구문__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="예 18. 엔드포인트 정의의 변형입니다." caption-side="bottom"}


__아카이브된 오브젝트의 응답 헤더__
{: http}

헤더 |유형 |설명
--- | ---- | ------------
`x-amz-restore` | 문자열 | 오브젝트가 복원되었거나 복원이 진행 중인 경우 포함됩니다.오브젝트가 복원된 경우 임시 사본의 만기 날짜도 리턴됩니다.
`x-amz-storage-class` | 문자열 | 아카이브되었거나 임시로 복원된 경우 `GLACIER`를 리턴합니다.
`x-ibm-archive-transition-time` | 날짜 | 오브젝트가 아카이브 티어로 전이되도록 스케줄된 날짜 및 시간을 리턴합니다.
`x-ibm-transition` | 문자열 | 오브젝트에 전이 메타데이터가 있고 전이의 원래 시간과 티어를 리턴하는 경우 포함됩니다.
`x-ibm-restored-copy-storage-class` | 문자열 | 오브젝트가 `RestoreInProgress` 또는 `Restored` 상태이고 버킷의 스토리지 클래스를 리턴하는 경우에 포함됩니다.


_샘플 요청_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="예 19. 요청 헤더를 표시하는 예입니다." caption-side="bottom"}

_샘플 응답_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="예 20. 응답 헤더를 표시하는 예입니다." caption-side="bottom"}


### 버킷 라이프사이클 구성 작성
{: #archive-node-create} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'STRING_VALUE',
        Filter: '', /* required */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* required if Days not specified */
            Days: 0, /* required if Date not specified */
            StorageClass: 'GLACIER' /* required */
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="예 21. 라이프사이클 구성 작성을 표시하는 예입니다." caption-side="bottom"}

### 버킷 라이프사이클 구성 검색
{: #archive-node-retrieve} {: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="예 22. 라이프사이클 메타데이터 검색을 표시하는 예입니다." caption-side="bottom"}

### 버킷 라이프사이클 구성 삭제
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="예 23. 버킷의 라이프사이클 구성 삭제를 표시하는 예입니다." caption-side="bottom"}

### 아카이브된 오브젝트 임시 복원 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
  ContentMD5: 'STRING_VALUE', /* required */
  RestoreRequest: {
   Days: 1, /* days until copy expires */
   GlacierJobParameters: {
     Tier: Bulk /* required */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="예 24. 아카이브된 오브젝트를 복원하는 데 사용되는 코드입니다." caption-side="bottom"}

### 오브젝트의 헤더 가져오기
{: #archive-node-head} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // an error occurred
  else   
    console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="예 25. 오브젝트 헤더 검색을 표시하는 예입니다." caption-side="bottom"}


### 버킷 라이프사이클 구성 작성
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="예 26. 오브젝트 구성 작성에 사용되는 메소드입니다." caption-side="bottom"}

### 버킷 라이프사이클 구성 검색
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="예 27. 오브젝트 구성 검색에 사용되는 메소드입니다." caption-side="bottom"}

### 버킷 라이프사이클 구성 삭제
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="예 28. 오브젝트 구성 삭제에 사용되는 메소드입니다." caption-side="bottom"}

### 아카이브된 오브젝트 임시 복원 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="예 29. 아카이브된 오브젝트를 임시로 복원합니다." caption-side="bottom"}

### 오브젝트의 헤더 가져오기
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="예 30. 오브젝트 헤더에 대한 응답 처리" caption-side="bottom"}


### 버킷 라이프사이클 구성 작성
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="예 31. 버킷 라이프사이클을 설정하는 데 사용되는 함수입니다." caption-side="bottom"}

**메소드 요약**
{: java}

메소드 |설명
--- | ---
`getBucketName()` | 라이프사이클 구성이 설정되는 버킷의 이름을 가져옵니다.
`getLifecycleConfiguration()` | 지정된 버킷의 새 라이프사이클 구성을 가져옵니다.
`setBucketName(String bucketName)` | 라이프사이클 구성이 설정되는 버킷의 이름을 설정합니다.
`withBucketName(String bucketName)` | 라이프사이클 구성을 설정 중인 버켓의 이름을 설정하고 추가 메소드 호출을 함께 연결할 수 있도록 이 오브젝트를 리턴합니다.
{: java}

### 버킷 라이프사이클 구성 검색
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="예 32. 오브젝트 라이프사이클 구성을 얻기 위한 함수 서명입니다." caption-side="bottom"}

### 버킷 라이프사이클 구성 삭제
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="예 33. 오브젝트 구성 삭제에 사용되는 함수입니다." caption-side="bottom"}

### 아카이브된 오브젝트 임시 복원 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="예 34. 아카이브된 오브젝트 복원을 위한 함수 서명입니다." caption-side="bottom"}

**메소드 요약**
{: java}

메소드 |설명
--- | ---
`clone()` | 핸들러 컨텍스트를 제외한 모든 필드에 대해 이 오브젝트의 간단한 복제본을 작성합니다.
`getBucketName()` | 복원할 오브젝트에 대한 참조가 있는 버킷의 이름을 리턴합니다.
`getExpirationInDays()` | 오브젝트 작성에서 만기까지의 기간(일)을 리턴합니다.
`setExpirationInDays(int expirationInDays)` | 오브젝트가 버킷에 업로드된 시점과 만기 시점 간의 기간(일)을 설정합니다.
{: java}

### 오브젝트의 헤더 가져오기
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="예 35. 오브젝트 헤더를 얻는 데 사용되는 함수입니다." caption-side="bottom"}

**메소드 요약**
{: java}

메소드 |설명
--- | ---
`clone()` | 이 `ObjectMetadata`의 복제본을 리턴합니다.
`getRestoreExpirationTime()` | ARCHIVE에서 임시로 복원된 오브젝트가 만료되는 시간을 리턴하며 액세스하려면 다시 복원해야 합니다.
`getStorageClass() ` | 버킷의 원래 스토리지 클래스를 리턴합니다.
`getIBMTransition()` | 전이 스토리지 클래스 및 전이 시간을 리턴합니다.
{: java}

## 다음 단계
{: #archive-next-steps}

{{site.data.keyword.cos_full_notm}} 외에, {{site.data.keyword.cloud_notm}}는 현재 서로 다른 사용자의 요구에 맞는 여러 추가 오브젝트 스토리지 오퍼링을 제공하며 이러한 오퍼링은 모두 웹 기반 포털 및 REST API를 통해 액세스 가능합니다. [자세히 보기.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
