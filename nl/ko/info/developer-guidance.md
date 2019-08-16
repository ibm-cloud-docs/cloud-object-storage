---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}

# 개발자 안내
{: #dev-guide}

## 암호 설정 조정
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}}는 전송 중인 데이터를 암호화하기 위해 다양한 암호 설정을 지원합니다. 모든 암호 설정이 동일한 레벨의 성능을 제공하는 것은 아닙니다. 클라이언트와 {{site.data.keyword.cos_full_notm}} 시스템 간에 TLS가 없으므로 `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_128_CBC_SHA` 중 하나를 협상하는 것은 동일한 레벨의 성능을 보였습니다. 

## 다중 파트 업로드 사용
{: #dev-guide-multipart}

대형 오브젝트에 대해 작업하는 경우에는 {{site.data.keyword.cos_full_notm}}에 오브젝트를 기록할 때 다중 파트 업로드 오퍼레이션을 사용하는 것이 좋습니다. 단일 오브젝트의 업로드는 파트의 세트로 수행할 수 있으며 이러한 파트는 순서에 상관없이 독립적으로 병렬로 업로드할 수 있습니다. 업로드가 완료되면 {{site.data.keyword.cos_short}}가 모든 파트를 모아 하나의 오브젝트로 제시합니다. 이는 네트워크 장애가 발생해도 대용량 업로드가 실패하지 않는다는 점, 업로드를 일시정지하고 나중에 다시 시작할 수 있다는 점, 오브젝트가 작성되는 즉시 업로드 가능한 점 등 많은 이점을 제공합니다. 

다중 파트 업로드는 5MB보다 큰 오브젝트에 대해서만 사용할 수 있습니다. 50GB보다 작은 오브젝트의 경우에는 최적의 성능을 위해 파트의 크기를 20MB - 100MB 범위로 설정하는 것이 좋습니다. 더 큰 오브젝트의 경우에는 성능에 큰 영향을 주지 않고 파트 크기를 늘릴 수 있습니다. 

500개가 넘는 파트를 사용하면 {{site.data.keyword.cos_short}}에 비효율성이 나타나므로 가능한 한 피하는 것이 좋습니다. 

작업이 더욱 복잡해질 수 있으므로, 개발자는 다중 파트 업로드 지원을 제공하는 S3 API 라이브러리를 사용하는 것이 좋습니다. 

오브젝트가 삭제되거나 다중 파트 업로드가 중단되며 `AbortIncompleteMultipartUpload`가 나타날 때까지 불완전한 다중 파트 업로드가 지속됩니다. 불완전한 다중 파트 업로드가 중단되지 않으면 파트 업로드가 계속 리소스를 사용합니다. 이 점을 염두에 두고 인터페이스를 설계하고 불완전한 다중 파트 업로드를 정리해야 합니다.


## 소프트웨어 개발 킷 사용
{: #dev-guide-sdks}

공개된 S3 API SDK를 사용하는 것은 필수가 아니며, 사용자 정의 소프트웨어는 API를 활용하여 {{site.data.keyword.cos_short}}에 직접 통합될 수 있습니다. 그러나 공개된 S3 API 라이브러리를 사용하면 인증/서명 생성, `5xx` 오류에 대한 자동 재시도 로직, 사전 서명된 URL 생성과 같은 장점이 있습니다. `503` 오류를 수신하는 경우 지수 백오프를 사용하는 재시도를 제공하는 등과 같이, 임시 오류를 처리하는 데 API를 직접 사용하는 소프트웨어를 작성하는 경우에는 주의해야 합니다. 

## 페이지 매김
{: #dev-guide-pagination}

버킷에 있는 다수의 오브젝트를 처리할 때는 웹 애플리케이션에서 성능 저하가 발생할 수 있습니다. 많은 애플리케이션은 **페이지 매김**(*대형 레코드 세트를 개별 페이지로 나누는 프로세스*)이라는 기술을 사용합니다. 거의 대부분의 개발 플랫폼은 기본 제공 기능 또는 서드파티 라이브러리를 통해 페이지 매김을 수행하기 위한 오브젝트 또는 메소드를 제공합니다. 

{{site.data.keyword.cos_short}} SDK는 지정된 버킷에 있는 오브젝트를 나열하는 메소드를 통해 페이지 매김을 지원합니다. 이 메소드는 대형 결과 세트를 나눌 때 매우 유용한 몇 가지 매개변수를 제공합니다. 

### 기본 사용법
{: #dev-guide-pagination-basics}
오브젝트 나열 메소드의 기본 개념은 응답에 리턴할 최대 키 수(`MaxKeys`)를 설정하는 것을 포함합니다. 이 응답은 더 많은 결과가 있음을 나타내는 `boolean` 값(`IsTruncated`)과 `NextContinuationToken`이라는 `string` 값 또한 포함합니다. 후속 요청에 연속 토큰을 설정하면 추가 결과가 없을 때까지 다음 오브젝트 배치가 리턴됩니다. 

#### 공통 매개변수
{: #dev-guide-pagination-params}

|매개변수|설명|
|---|---|
|`ContinuationToken`|다음 레코드 배치를 지정하는 토큰 설정|
|`MaxKeys`|응답에 포함시킬 최대 키 수 설정|
|`Prefix`|지정된 접두부로 시작하는 키로 응답 제한|
|`StartAfter`|특정 키를 기반으로 오브젝트 나열을 시작할 위치 설정|

### Java 사용
{: #dev-guide-pagination-java}

{{site.data.keyword.cos_full}} SDK for Java는 원하는 크기로 오브젝트 목록을 리턴할 수 있도록 하는 [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} 메소드를 제공합니다. 전체 코드 예는 [여기](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2)에 있습니다. 

### Python 사용
{: #dev-guide-pagination-python}

{{site.data.keyword.cos_full}} SDK for Python은 원하는 크기로 오브젝트 목록을 리턴할 수 있도록 하는 [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} 메소드를 제공합니다. 전체 코드 예는 [여기](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2)에 있습니다. 
