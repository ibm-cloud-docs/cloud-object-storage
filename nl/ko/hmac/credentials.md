---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# HMAC 인증 정보 사용
{: #hmac}

{{site.data.keyword.cos_full}} API는 오브젝트를 읽고 쓰기 위한 REST 기반 API입니다. 이는 인증/권한 부여에 {{site.data.keyword.iamlong}}를 사용하며 {{site.data.keyword.cloud_notm}}로의 손쉬운 애플리케이션 마이그레이션을 위해 S3 API 세브세트를 지원합니다. 

IAM 토큰 기반 인증 외에, 액세스 및 비밀 키 쌍으로부터 작성된 [서명을 사용하여 인증](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)할 수도 있습니다. 이는 AWS 서명 버전 4와 기능적으로 동일하며, IBM COS에서 제공된 HMAC 키는 대부분의 S3 호환 라이브러리 및 도구에서 문제없이 작동합니다. 

사용자는 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) 작성 중에 구성 매개변수 `{"HMAC":true}`를 제공하여 HMAC 인증 정보 세트를 작성할 수 있습니다. **Writer** 역할(계정에 대해 다른 역할이 사용 가능하거나 사용자의 필요에 더 적합할 수도 있음)로 {{site.data.keyword.cos_full}} CLI를 사용하여 HMAC 인증 정보를 포함하는 서비스 키를 작성하는 방법에 대한 예는 다음과 같습니다.  

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="예 1. cURL을 사용하여 HMAC 인증 정보를 작성합니다. 작은따옴표와 큰따옴표가 모두 사용되었다는 점을 참고하십시오. " caption-side="bottom"}

예 1의 명령에 의해 생성된 키의 결과를 저장하려는 경우에는 예의 끝에 ` > file.skey`를 추가할 수 있습니다. 이 지시사항 세트의 목적을 위해서는 예 2에 표시되어 있는 바와 같이, 필요한 필드인 하위 키 `access_key_id` 및 `secret_access_key`를 포함하는 `cos_hmac_keys` 헤딩만 찾으면 됩니다. 

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="예 2. HMAC 인증 정보 생성과 관련된 키입니다. " caption-side="bottom"}

특히 중요한 것은 환경 변수를 설정하는 기능(이에 대한 지시사항은 관련된 운영 체제에 대해 고유함)입니다. 예를 들어, 예 3에서 `.bash_profile` 스크립트는 쉘 시작 시에 내보내지며 개발에서 사용되는 `COS_HMAC_ACCESS_KEY_ID` 및 `COS_HMAC_SECRET_ACCESS_KEY`를 포함합니다. 

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="예 3. 환경 변수로 사용되는 HMAC 인증 정보입니다. " caption-side="bottom"}

서비스 인증 정보가 작성되고 나면 HMAC 키가 `cos_hmac_keys` 필드에 포함됩니다. 그 후 이러한 HMAC 키는 [서비스 ID](/docs/iam?topic=iam-serviceids#serviceids)와 연관되며, 서비스 ID의 역할에 의해 허용되는 리소스 또는 오퍼레이션에 액세스하는 데 사용될 수 있습니다.  

추가 헤더가 필요한 직접 [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) 호출과 함께 사용할 서명을 작성하기 위해 HMAC 인증 정보를 사용할 때는 다음 항목을 참고하십시오. 
1. 모든 요청에는 날짜가 `%Y%m%dT%H%M%SZ` 형식인 `x-amz-date` 헤더가 있어야 합니다. 
2. 페이로드(오브젝트 업로드, 여러 오브젝트 삭제 등)가 있는 요청은 페이로드 컨텐츠의 SHA256 해시를 포함하는 `x-amz-content-sha256` 헤더를 제공해야 합니다. 
3. ACL(`public-read` 외)은 지원되지 않습니다. 

S3 호환 도구 중 일부는 현재 지원되지 않습니다. 일부 도구는 버킷 작성 시에 `public-read` 외의 ACL을 설정하려 시도합니다. 이러한 도구를 통한 버킷 작성은 실패합니다. 지원되지 않는 ACL 오류가 발생하며 `PUT bucket` 요청이 실패하는 경우에는 먼저 [콘솔](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)을 사용하여 버킷을 작성한 후 해당 버킷에서 오브젝트를 읽고 쓰도록 도구를 구성하십시오. 오브젝트 쓰기에 대해 ACL을 설정하는 도구는 현재 지원되지 않습니다.
{:tip}
