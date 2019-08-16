---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# 공통 헤더 및 오류 코드
{: #compatibility-common}

## 공통 요청 헤더
{: #compatibility-request-headers}

다음 표에서는 지원되는 공통 요청 헤더를 설명합니다. 일부 요청이 이 문서에 정의된 다른 헤더를 지원할 수 있지만 {{site.data.keyword.cos_full}}는 요청에 보낸 경우 아래에 나열되지 않은 공통 헤더를 무시합니다.

| 헤더                    |참고                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
|허가           | 모든 요청에 대해 **필수**(OAuth2 `bearer` 토큰).                                                                           |
| ibm-service-instance-id | 버킷 작성 또는 나열 요청에 대해 **필수**.                                                                                         |
| Content-MD5             | 페이로드의 base64 인코딩 128비트 MD5 해시이며, 페이로드가 전송 중에 변경되지 않았는지 확인하기 위한 무결성 검사로 사용됩니다.                        |
| Expect                  | `100-continue` 값은 페이로드를 전송하기 전에 헤더가 적합하다는 시스템의 수신확인을 기다립니다.                                          |
| host                    | `{bucket-name}.{endpoint}`의 '가상 호스트' 구문 또는 엔드포인트입니다. 일반적으로 이 헤더는 자동으로 추가됩니다. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. | 
| Cache-Control | 요청/응답 체인에 따라 캐싱 동작을 지정하는 데 사용할 수 있습니다. 자세한 정보는 http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 페이지를 참조하십시오. |

### 사용자 정의 메타데이터
{: #compatibility-headers-metadata}

오브젝트 스토리지 사용의 이점은 키-값 쌍을 헤더로서 보내 사용자 정의 메타데이터를 추가하는 기능입니다. 이러한 헤더는 `x-amz-meta-{KEY}` 양식을 사용합니다. AWS S3와 달리 IBM COS는 쉼표로 구분된 값 목록에 동일한 메타데이터 키가 있는 다중 헤더를 결합합니다.

## 공통 응답 헤더
{: #compatibility-response-headers}

다음 표에서는 공통 응답 헤더를 설명합니다.

| 헤더             |참고                                                |
|------------------|-----------------------------------------------------|
| Content-Length   | 요청 본문의 길이(바이트)입니다.                    |
| Connection       | 연결이 열려 있는지 또는 닫혀 있는지를 표시합니다.  |
| Date             | 요청의 시간소인입니다.                             |
| ETag             | 요청의 MD5 해시 값입니다.                          |
| Server           | 응답 서버의 이름입니다.                            |
| X-Clv-Request-Id | 요청마다 생성된 고유 ID입니다.                     |

### 라이프사이클 응답 헤더
{: #compatibility-lifecycle-headers}

다음 표에서는 아카이브된 오브젝트의 응답 헤더를 설명합니다.

| 헤더             |참고                                                |
|------------------|-----------------------------------------------------|
|x-amz-restore|오브젝트가 복원되었거나 복원이 진행 중인 경우 포함됩니다.|
|x-amz-storage-class|아카이브되었거나 임시로 복원된 경우 `GLACIER`를 리턴합니다.|
|x-ibm-archive-transition-time|오브젝트가 아카이브 티어로 전이되도록 스케줄된 날짜 및 시간을 리턴합니다.|
|x-ibm-transition|오브젝트에 전이 메타데이터가 있고 전이의 원래 시간과 티어를 리턴하는 경우 포함됩니다.|
|x-ibm-restored-copy-storage-class|오브젝트가 `RestoreInProgress` 또는 `Restored` 상태이고 버킷의 스토리지 클래스를 리턴하는 경우에 포함됩니다.|

## 오류 코드
{: #compatibility-errors}

| 오류 코드                           |  설명                                                                                                                                                                   | HTTP 상태 코드                       |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | 액세스가 거부됨                                                                                                                                                         | 403 금지됨                          |
| BadDigest                           | 지정된 Content-MD5가 수신된 내용과 일치하지 않았습니다.                                                                                                                 | 400 잘못된 요청                     |
| BucketAlreadyExists                 | 요청된 버킷 이름을 사용할 수 없습니다. 시스템의 모든 사용자가 버킷 네임스페이스를 공유합니다. 다른 이름을 선택하고 다시 시도하십시오.                                   | 409 충돌                            |
| BucketAlreadyOwnedByYou             | 이름 지정된 버킷을 작성하는 이전 요청이 성공했으며 이미 이를 소유하고 있습니다.                                                                                         | 409 충돌                            |
| BucketNotEmpty                      | 삭제하려는 버킷이 비어 있지 않습니다.                                                                                                                                   | 409 충돌                            |
| CredentialsNotSupported             | 이 요청은 인증 정보를 지원하지 않습니다.                                                                                                                                | 400 잘못된 요청                     |
| EntityTooSmall                      | 제안된 업로드가 허용된 최소 오브젝트 크기보다 작습니다.                                                                                                                 | 400 잘못된 요청                     |
| EntityTooLarge                      | 제안된 업로드가 허용된 최대 오브젝트 크기를 초과합니다.                                                                                                                 | 400 잘못된 요청                     |
| IncompleteBody                      | Content-Length HTTP 헤더에서 지정한 바이트 수를 제공하지 않았습니다.                                                                                                    | 400 잘못된 요청                     |
| IncorrectNumberOfFilesInPostRequest | POST는 요청당 정확히 하나의 파일 업로드가 필요합니다.                                                                                                                   | 400 잘못된 요청                     |
| InlineDataTooLarge                  | 인라인 데이터가 허용된 최대 크기를 초과합니다.                                                                                                                          | 400 잘못된 요청                     |
| InternalError                       | 내부 오류가 발생했습니다. 다시 시도하십시오.                                                                                                                            | 500 내부 서버 오류                  |
| InvalidAccessKeyId                  | 제공된 AWS 액세스 키 ID가 레코드에 없습니다.                                                                                                                            | 403 금지됨                          |
| InvalidArgument                     | 올바르지 않은 인수입니다.                                                                                                                                               | 400 잘못된 요청                     |
| InvalidBucketName                   | 지정된 버킷이 올바르지 않습니다.                                                                                                                                        | 400 잘못된 요청                     |
| InvalidBucketState                  | 현재 상태의 버킷에서 요청이 올바르지 않습니다.                                                                                                                          | 409 충돌                            |
| InvalidDigest                       | 지정된 Content-MD5가 올바르지 않습니다.                                                                                                                                 | 400 잘못된 요청                     |
| InvalidLocationConstraint           | 지정된 위치 제한조건이 올바르지 않습니다. 지역에 대한 자세한 정보는 버킷의 지역 선택 방법을 참조하십시오.                                                               | 400 잘못된 요청                     |
| InvalidObjectState                  | 오퍼레이션이 현재 오브젝트 상태에 대해 올바르지 않습니다.                                                                                                               | 403 금지됨                          |
| InvalidPart                         | 하나 이상의 지정된 파트를 찾을 수 없습니다. 파트를 업로드하지 않았거나 지정된 엔티티 태그가 파트의 엔티티 태그와 일치하지 않았을 수 있습니다.                           | 400 잘못된 요청                     |
| InvalidPartOrder                    | 파트 목록이 오름차순으로 표시되지 않았습니다. 파트 목록은 파트 번호별로 지정해야 합니다.                                                                                | 400 잘못된 요청                     |
| InvalidRange                        | 요청된 범위를 충족할 수 없습니다.                                                                                                                                       | 416 요청된 범위를 만족시킬 수 없음  |
| InvalidRequest                      | AWS4-HMAC-SHA256을 사용하십시오.                                                                                                                                        | 400 잘못된 요청                     |
| InvalidSecurity                     | 제공된 보안 인증 정보가 올바르지 않습니다.                                                                                                                              | 403 금지됨                          |
| InvalidURI                          | 지정된 URI를 구문 분석할 수 없습니다.                                                                                                                                   | 400 잘못된 요청                     |
| KeyTooLong                          | 키가 너무 깁니다.                                                                                                                                                       | 400 잘못된 요청                     |
| MalformedPOSTRequest                | POST 요청의 본문이 올바르게 구성된 다중 파트/양식 데이터가 아닙니다.                                                                                                    | 400 잘못된 요청                     |
| MalformedXML                        | 제공한 XML이 올바로 구성되지 않았거나 공개된 스키마에 대해 유효성을 검증하지 않았습니다.                                                                                | 400 잘못된 요청                     |
| MaxMessageLengthExceeded            | 요청이 너무 큽니다.                                                                                                                                                     | 400 잘못된 요청                     |
| MaxPostPreDataLengthExceededError   | 업로드 파일 앞의 POST 요청 필드가 너무 깁니다.                                                                                                                          | 400 잘못된 요청                     |
| MetadataTooLarge                    | 메타데이터 헤더가 허용된 최대 메타데이터 크기를 초과합니다.                                                                                                             | 400 잘못된 요청                     |
| MethodNotAllowed                    | 지정된 메소드가 이 리소스에 대해 허용되지 않습니다.                                                                                                                     | 405 허용되지 않은 메소드            |
| MissingContentLength                | Content-Length HTTP 헤더를 제공해야 합니다.                                                                                                                             |411 길이 필수                        |
| MissingRequestBodyError             | 사용자가 빈 XML 문서를 요청으로서 전송할 때 발생합니다. 오류 메시지는 "Request body is empty."입니다.                                                                   | 400 잘못된 요청                     |
| NoSuchBucket                        | 지정된 버킷이 없습니다.                                                                                                                                                 |404 찾을 수 없음                     |
| NoSuchKey                           | 지정된 키가 없습니다.                                                                                                                                                   |404 찾을 수 없음                     |
| NoSuchUpload                        | 지정된 다중 파트 업로드가 없습니다. 업로드 ID가 올바르지 않거나 다중 파트 업로드가 중단되었거나 완료되었을 수 있습니다.                                                 |404 찾을 수 없음                     |
| NotImplemented                      | 제공된 헤더는 구현되지 않은 기능을 의미합니다.                                                                                                                          |501 구현되지 않음                    |
| OperationAborted                    | 충돌 조건 오퍼레이션이 현재 이 리소스에 대해 진행 중입니다. 다시 시도하십시오.                                                                                          | 409 충돌                            |
| PreconditionFailed                  | 지정한 전제조건 중 하나 이상을 보유하지 않았습니다.                                                                                                                     | 412 전제조건 실패                   |
| Redirect                            | 임시적으로 경로 재지정.                                                                                                                                                 | 307 임시 이동                       |
| RequestIsNotMultiPartContent        | 버킷 POST가 격납장치 유형 다중 파트/양식 데이터여야 합니다.                                                                                                             | 400 잘못된 요청                     |
| RequestTimeout                      | 서버에 대한 소켓 연결을 제한시간 내에 읽거나 기록하지 못했습니다.                                                                                                       | 400 잘못된 요청                     |
| RequestTimeTooSkewed                | 요청 시간과 서버의 시간 사이의 차이가 너무 큽니다.                                                                                                                      | 403 금지됨                          |
| ServiceUnavailable                  | 요청 속도를 줄입니다.                                                                                                                                                   | 503 사용 불가능한 서비스            |
| SlowDown                            | 요청 속도를 줄입니다.                                                                                                                                                   | 503 속도 늦추기                     |
| TemporaryRedirect                   | DNS 업데이트 중에 버킷으로 경로 재지정합니다.                                                                                                                           | 307 임시 이동                       |
| TooManyBuckets                      | 허용되는 것보다 많은 버킷을 작성하려고 시도했습니다.                                                                                                                    | 400 잘못된 요청                     |
| UnexpectedContent                   | 이 요청은 컨텐츠를 지원하지 않습니다.                                                                                                                                   | 400 잘못된 요청                     |
| UserKeyMustBeSpecified              | 버킷 POST는 지정된 필드 이름을 포함해야 합니다. 지정된 경우 필드의 순서를 확인하십시오.                                                                                 | 400 잘못된 요청                     |
