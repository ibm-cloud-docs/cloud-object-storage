---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# 암호화 관리
{: #encryption}

{{site.data.keyword.cos_full}}에 저장된 모든 오브젝트는 기본적으로 [랜덤으로 생성된 키 및 AONT(All-Or-Nothing-Transform)](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security)를 사용하여 암호화됩니다. 이 기본 암호화 모델은 안정된 보안을 제공하지만 일부 워크로드는 사용되는 암호화 키를 소유해야 합니다. 데이터(SSE-C) 저장 시 고유 암호화 키를 제공하여 직접 키를 관리하거나 IBM Key Protect(SSE-KP)를 사용하여 암호화 키를 관리하는 버킷을 작성할 수 있습니다.

## 고객 제공 키를 사용한 서버 측 암호화(SSE-C)
{: #encryption-sse-c}

SSE-C는 오브젝트에 적용됩니다. 고객 관리 키를 사용하여 오브젝트 또는 해당 메타데이터를 읽거나 쓰는 요청이 HTTP 요청의 헤더로서 필수 암호화 정보를 전송합니다. 구문은 S3 API와 동일하며, SSE-C를 지원하는 S3 호환 가능 라이브러리가 {{site.data.keyword.cos_full}}에 대해 예상대로 작동해야 합니다.

SSE-C 헤더를 사용하는 요청은 SSL을 사용하여 전송되어야 합니다. 응답 헤더의 `ETag` 값은 오브젝트의 MD5 해시가 *아니*지만 랜덤으로 생성된 32바이트 16진 문자열입니다.

헤더 |유형 |설명
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | 문자열 | 이 헤더는 `x-amz-server-side-encryption-customer-key` 헤더에 저장된 암호화 키로 사용할 알고리즘 및 키 크기를 지정하는 데 사용됩니다. 이 값은 문자열 `AES256`으로 설정해야 합니다.
`x-amz-server-side-encryption-customer-key` | 문자열 | 이 헤더는 서버 측 암호화 프로세스에 사용되는 AES 256 키의 Base 64 인코딩 바이트 문자열 표시를 전송하는 데 사용됩니다.
`x-amz-server-side-encryption-customer-key-MD5` | 문자열 | 이 헤더는 RFC 1321에 따라 암호화 키의 base64 인코딩 128비트 MD5 요약을 전송하는 데 사용됩니다. 오브젝트 저장소는 이 값을 사용하여 `x-amz-server-side-encryption-customer-key`로 전달되는 키가 전송 및 인코딩 프로세스 중에 손상되지 않았음을 검증합니다. 키를 Base 64로 인코딩하려면 먼저 키에서 요약을 추정해야 합니다.


## {{site.data.keyword.keymanagementservicelong_notm}}를 사용한 서버 측 암호화(SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}}는 {{site.data.keyword.cloud_notm}} 서비스에서 사용되는 암호화 키를 생성하고 관리하고 영구 삭제하기 위한 중앙화된 키 관리 시스템(KMS)입니다. {{site.data.keyword.cloud_notm}} 카탈로그에서 {{site.data.keyword.keymanagementserviceshort}}의 인스턴스를 작성할 수 있습니다.

새 버킷을 작성할 지역에서 {{site.data.keyword.keymanagementserviceshort}}의 인스턴스를 갖게 되면 루트 키를 작성하고 이 키의 CRN을 기록해야 합니다.

{{site.data.keyword.keymanagementserviceshort}}를 사용하여 작성 시에만 버킷의 암호화를 관리하도록 선택할 수 있습니다. {{site.data.keyword.keymanagementserviceshort}}를 사용하기 위해 기존 버킷을 변경할 수 없습니다.
{:tip}

버킷을 작성할 때 추가 헤더를 제공해야 합니다.

{{site.data.keyword.keymanagementservicelong_notm}}에 대한 자세한 정보는 [문서를 참조](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect)하십시오.

### SSE-KP 시작하기
{: #sse-kp-gs}

{{site.data.keyword.cos_full}}에 저장된 모든 오브젝트는 기본적으로 랜덤으로 생성된 키 및 AONT(All-Or-Nothing-Transform)를 사용하여 암호화됩니다. 이 기본 암호화 모델은 안정된 보안을 제공하지만 일부 워크로드는 사용되는 암호화 키를 소유해야 합니다. [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about)를 사용하여 키를 작성하고 추가하고 관리할 수 있으며, 그런 다음 {{site.data.keyword.cos_full}}의 인스턴스와 연관시켜 버킷을 암호화할 수 있습니다.

### 시작하기 전에
{: #sse-kp-prereqs}

다음이 필요합니다.
  * [{{site.data.keyword.cloud}} 플랫폼 계정](http://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}}의 인스턴스](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * [{{site.data.keyword.keymanagementservicelong_notm}}의 인스턴스](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * 로컬 컴퓨터의 일부 파일 업로드

### {{site.data.keyword.keymanagementserviceshort}}에서 키 작성 또는 추가
{: #sse-kp-add-key}

{{site.data.keyword.keymanagementserviceshort}}의 인스턴스를 탐색하고 [키를 생성하거나 입력](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)하십시오.

### 서비스 권한 부여
{: #sse-kp}
IBM COS에서 사용하기 위해 {{site.data.keyword.keymanagementserviceshort}}에 권한을 부여하십시오.

1. {{site.data.keyword.cloud_notm}} 대시보드를 여십시오.
2. 메뉴 표시줄에서 **관리** &gt; **계정** &gt; **사용자**를 클릭하십시오.
3. 사이드 탐색에서 **ID 및 액세스** &gt; **권한**을 클릭하십시오.
4. **권한 작성**을 클릭하십시오.
5. **소스 서비스** 메뉴에서 **Cloud Object Storage**를 선택하십시오.
6. **소스 서비스 인스턴스** 메뉴에서 권한 부여할 서비스 인스턴스를 선택하십시오.
7. **대상 서비스** 메뉴에서 **{{site.data.keyword.keymanagementservicelong_notm}}**를 선택하십시오.
8. **대상 서비스 인스턴스** 메뉴에서 권한 부여할 서비스 인스턴스를 선택하십시오.
9. **독자** 역할을 사용으로 설정하십시오.
10. **권한 부여**를 클릭하십시오.

### 버킷 작성
{: #encryption-createbucket}

키가 {{site.data.keyword.keymanagementserviceshort}}에 있고 IBM COS에서 사용하기 위해 Key Protect 서비스에 권한을 부여한 경우 키를 새 버킷과 연관시키십시오.

1. {{site.data.keyword.cos_short}}의 인스턴스를 탐색하십시오.
2. **버킷 작성**을 클릭하십시오.
3. 버킷 이름을 입력하고 **지역** 복원성을 선택하고 위치 및 스토리지 클래스를 선택하십시오.
4. 고급 구성에서 **Key Protect 키 추가**를 사용으로 설정하십시오.
5. 연관된 Key Protect 서비스 인스턴스, 키 및 키 ID를 선택하십시오.
6. **작성**을 클릭하십시오.

**버킷 및 오브젝트** 목록에서 버킷의 **고급** 아래에 키 아이콘이 있으며, 이는 버킷에서 Key Protect 키가 사용 가능함을 표시합니다. 키 세부사항을 보려면 버킷의 오른쪽에 있는 메뉴를 클릭한 다음 **Key Protect 키 보기**를 클릭하십시오.

SSE-KP를 사용하여 암호화된 오브젝트에 대해 리턴된 `Etag` 값은 원래 암호화되지 않은 오브젝트의 실제 MD5 해시가 **됩니다**.
{:tip}


## 키 순환
{: #encryption-rotate}

키 순환은 데이터 위반 위험을 완화하는 데 중요한 부분입니다. 키를 주기적으로 변경하면 키가 유실되거나 손상될 경우 잠재적인 데이터 유실이 줄어듭니다. 키 순환 빈도는 조직에 따라 달라지며 환경, 암호화된 데이터 양, 데이터의 분류 및 규제 준수 법률을 포함하는 여러 변수에 따라 달라집니다. [NIST(National Institute of Standards and Technology)](https://www.nist.gov/topics/cryptography){:new_window}는 적절한 키 길이에 대한 정의를 제공하고 키를 사용할 기간에 대한 가이드라인을 제공합니다.

### 수동 키 순환
{: #encryption-rotate-manual}

{{site.data.keyword.cos_short}}의 키를 순환하려면 새 루트 키를 사용하여 Key Protect가 사용 가능한 버킷을 새로 작성하고 기존 버킷의 컨텐츠를 새 버킷에 복사해야 합니다.

**참고**: 시스템에서 키를 삭제하면 해당 컨텐츠와 이 키로 암호화된 모든 데이터가 삭제됩니다. 일단 제거되면 실행 취소하거나 되돌릴 수 없으며 영구적인 데이터 손실이 발생합니다.

1. [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial) 서비스에서 새 루트 키를 작성하거나 추가하십시오.
2. [버킷을 새로 작성](#encryption-createbucket)하고 새 루트 키를 추가하십시오.
3. 원래 버킷의 모든 오브젝트를 새 버킷에 복사하십시오.
    1. 이 단계는 여러 방법을 사용하여 수행할 수 있습니다.
        1. 명령행에서 [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) 또는 [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli) 사용
        2. (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object] 사용
        3. [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 또는 [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go)에서 SDK 사용
