---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# {{site.data.keyword.cos_full_notm}} S3 API 정보
{: #compatibility-api}

{{site.data.keyword.cos_full}} API는 오브젝트를 읽고 쓰기 위한 REST 기반 API입니다. 또한 인증 및 권한 부여를 위해 {{site.data.keyword.iamlong}}를 사용하고 {{site.data.keyword.cloud_notm}}로 애플리케이션을 쉽게 마이그레이션하기 위해 S3 API의 서브세트를 지원합니다.

이 참조 문서는 지속적으로 개선되고 있습니다. 애플리케이션에서 API를 사용하는 방법에 대한 기술적인 질문이 있는 경우 [StackOverflow](https://stackoverflow.com/)에 게시하십시오. `ibm-cloud-platform` 및 `object-storage` 태그를 둘 다 추가하십시오. 피드백을 주시면 이 문서 개선에 도움이 됩니다.

{{site.data.keyword.iamshort}} 토큰은 상대적으로 쉽게 작업할 수 있으므로 `curl`은 기본 테스트 및 스토리지와의 상호작용을 위한 좋은 선택이 됩니다. 자세한 정보는 [`curl` 참조](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)에 있습니다.

다음 표에서는 {{site.data.keyword.cos_full_notm}} API의 전체 오퍼레이션 세트를 설명합니다. 자세한 정보는 [버킷을 위한 API 참조 페이지](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) 또는 [오브젝트](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)를 참조하십시오.


## 버킷 오퍼레이션
{: #compatibility-api-bucket}

이러한 오퍼레이션은 정보를 작성하고 삭제하고 가져오고 버킷의 동작을 제어합니다.

| 버킷 오퍼레이션         |참고                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` 버킷              | 계정에 속한 모든 버킷 목록을 검색하는 데 사용됩니다.                           |
| `DELETE` 버킷           | 빈 버킷을 삭제합니다.                                                          |
| `DELETE` 버킷 CORS      | 버킷에서 CORS(Cross-Origin Resource Sharing) 구성 세트를 삭제합니다.           |
| `GET` 버킷              | 버킷의 오브젝트를 나열합니다. 한 번에 1,000개의 오브젝트 나열로 제한됩니다.    |
| `GET` 버킷 CORS         | 버킷에서 CORS 구성 세트를 검색합니다.                                          |
| `HEAD` 버킷             | 버킷의 헤더를 검색합니다.                                                      |
| `GET` 다중 파트 업로드  | 완료되지 않았거나 취소된 다중 파트 업로드를 나열합니다.                        |
| `PUT` 버킷              | 버킷에는 이름 지정 제한이 있습니다. 계정은 100개의 버킷으로 제한됩니다.        |
| `PUT` 버킷 CORS         | 버킷의 CORS 구성을 작성합니다.                                                 |


## 오브젝트 오퍼레이션
{: #compatibility-api-object}

이러한 오퍼레이션은 정보를 작성하고 삭제하고 가져오고 오브젝트의 동작을 제어합니다.

| 오브젝트 오퍼레이션       |참고                                                                                |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` 오브젝트         | 버킷에서 오브젝트를 삭제합니다.                                                    |
| `DELETE` 일괄처리         | 하나의 오퍼레이션으로 버킷에서 많은 오브젝트를 삭제합니다.                         |
| `GET` 오브젝트            | 버킷에서 오브젝트를 검색합니다.                                                    |
| `HEAD` 오브젝트           | 오브젝트의 헤더를 검색합니다.                                                      |
| `OPTIONS` 오브젝트        | 특정 요청을 보낼 수 있는지 여부를 확인하기 위해 CORS 구성을 확인합니다.            |
| `PUT` 오브젝트            | 버킷에 오브젝트를 추가합니다.                                                      |
| `PUT` 오브젝트(복사)      | 오브젝트의 사본을 작성합니다.                                                      |
| 다중 파트 업로드 시작     | 업로드할 파트 세트의 ID를 작성합니다.                                              |
| 파트 업로드               | 업로드 ID와 연관된 오브젝트의 파트를 업로드합니다.                                 |
| 파트 업로드(복사)         | 업로드 ID와 연관된 기존 오브젝트의 파트를 업로드합니다.                            |
| 다중 파트 업로드 완료     | 업로드 ID와 연관된 파트에서 오브젝트를 어셈블합니다.                               |
| 다중 파트 업로드 취소     | 업로드를 취소하고 업로드 ID와 연관된 미해결 파트를 삭제합니다.                     |
| 파트 나열                 | 업로드 ID와 연관된 파트 목록을 리턴합니다.                                         |


태그 지정 및 버전화와 같은 일부 추가 오퍼레이션은 {{site.data.keyword.cos_short}}의 퍼블릭 또는 데디케이티드 클라우드가 아닌 프라이빗 클라우드 구현에서 지원됩니다. 사용자 정의 오브젝트 스토리지 솔루션의 자세한 정보는 [ibm.com](https://www.ibm.com/cloud/object-storage)에 있습니다.
