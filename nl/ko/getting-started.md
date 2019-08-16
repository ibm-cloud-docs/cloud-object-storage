---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# 시작하기 튜토리얼
{: #getting-started}

이 시작하기 튜토리얼에서는 버킷을 작성하고, 오브젝트를 업로드하고, 다른 사용자가 데이터에 대한 작업을 수행할 수 있도록 액세스 정책을 설정하는 데 필요한 단계를 설명합니다.
{: shortdesc}

## 시작하기 전에
{: #gs-prereqs}

다음이 필요합니다.
  * [{{site.data.keyword.cloud}} 플랫폼 계정](https://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}}의 인스턴스](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * 로컬 컴퓨터의 일부 파일 업로드
{: #gs-prereqs}

 이 튜토리얼에서는 {{site.data.keyword.cloud_notm}} 플랫폼 콘솔에 대한 첫 번째 단계로 새 사용자를 안내합니다. API를 시작할 개발자는 [개발자 안내서](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) 또는 [API 개요](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)를 참조하십시오.

## 데이터를 저장할 몇 가지 버킷 작성
{: #gs-create-buckets}

  1. [{{site.data.keyword.cos_full_notm}} 주문](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) 시 _서비스 인스턴스_가 작성됩니다. {{site.data.keyword.cos_full_notm}}는 멀티 테넌트 시스템이고 {{site.data.keyword.cos_short}}의 모든 인스턴스는 실제 인프라를 공유합니다. 버킷 작성을 시작할 수 있는 서비스 인스턴스로 자동 경로 재지정됩니다. {{site.data.keyword.cos_short}} 인스턴스는 [리소스 목록](https://cloud.ibm.com/resources)의 **스토리지** 아래에 나열됩니다.

'리소스 인스턴스' 및 '서비스 인스턴스'라는 용어는 동일한 개념을 나타내며 서로 교환하여 사용 가능합니다.
{: tip}

  1. **버킷 작성**을 수행하고 고유 이름을 선택하십시오. 전세계 모든 지역의 모든 버킷은 단일 네임스페이스를 공유합니다. 버킷을 작성할 [올바른 권한](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions)이 있는지 확인하십시오.

  **참고**: 버킷을 작성하거나 오브젝트를 추가할 때 PII(Personally Identifiable Information)를 사용하지 마십시오. PII는 이름, 위치 또는 다른 수단에 의해 사용자(자연인)를 식별할 수 있는 정보입니다.
  {: tip}

  1. 먼저 원하는 [_복원성_ 레벨](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)을 선택한 다음 데이터를 실제로 저장할 _위치_를 선택하십시오. 복원성은 데이터가 분산되는 지리적 영역의 범위와 스케일을 나타냅니다. _교차 지역_ 복원성이 여러 대도시 지역에 데이터를 분산시키는 반면 _지역_ 복원성은 하나의 대도시 지역에 데이터를 분산시킵니다. _단일 데이터 센터_는 단일 사이트 내에서만 디바이스에 데이터를 분배합니다.
  2. [버킷의 _스토리지 클래스_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes)를 선택하십시오. 이 클래스는 저장된 데이터를 읽을 빈도를 반영하며 청구 세부사항을 판별합니다. **작성** 링크에 따라 새 버킷을 작성하고 액세스하십시오.

버킷은 데이터를 구성하는 방법이지만 유일한 방법은 아닙니다. 오브젝트 이름(_오브젝트 키_라고도 함)은 디렉토리와 같은 조직 시스템에 하나 이상의 슬래시를 사용할 수 있습니다. 그런 다음 구분 기호 앞에 오브젝트 이름 부분을 사용하여 _오브젝트 접두부_를 구성하며, 이는 API를 통해 단일 버킷의 관련 오브젝트를 나열하는 데 사용됩니다.
{: tip}


## 버킷에 일부 오브젝트 추가
{: #gs-add-objects}

이제 목록에서 버킷 중 하나를 선택하여 이동하십시오. **오브젝트 추가**를 클릭하십시오. 새 오브젝트는 동일한 버킷 내에서 동일한 이름으로 기존 오브젝트를 겹쳐씁니다. 콘솔을 사용하여 오브젝트를 업로드하는 경우 오브젝트 이름이 항상 파일 이름과 일치합니다. API를 사용하여 데이터를 쓰는 경우 파일 이름과 오브젝트 키 간의 관계는 필요하지 않습니다. 이 버킷에 몇 개의 파일을 추가하십시오.

[Aspera 고속 전송](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload) 플러그인을 사용하지 않는 한, 오브젝트는 콘솔을 통해 업로드될 때 200MB로 제한됩니다. 또한 대형 오브젝트(최대 10TB)는 [API를 사용하여 파트로 분할하고 동시에 업로드](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)할 수 있습니다. 오브젝트는 길이가 최대 1024자이며 웹 주소에서 문제가 될 수 있는 문자는 피하는 것이 좋습니다. 예를 들어, URL 인코딩되지 않은 경우 `?`, `=`, `<` 및 기타 특수 문자로 인해 원하지 않는 동작이 발생할 수 있습니다.
{:tip}

## 계정에 사용자를 초대하여 버킷 및 데이터 관리
{: #gs-invite-user}

이제 다른 사용자를 가져와 이 사용자가 인스턴스 및 이 인스턴스에 저장된 모든 데이터의 관리자 역할을 수행하게 할 수 있습니다.

  1. 새 사용자를 추가하려면 IAM 콘솔의 현재 {{site.data.keyword.cos_short}} 인터페이스 및 헤드를 남겨두어야 합니다. **관리자** 메뉴로 이동하고 **액세스(IAM)** > **사용자**의 링크를 따르십시오. **사용자 초대**를 클릭하십시오.
	<img alt="IAM 사용자 초대" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`그림 1: IAM 사용자 초대`
  2. 조직에 초대할 사용자의 이메일 주소를 입력한 다음 **서비스** 섹션을 펼치고 **액세스 지정 대상** 메뉴에서 "리소스"를 선택하십시오. 이제 **서비스** 메뉴에서 "Cloud Object Storage"를 선택하십시오.
	<img alt="IAM 서비스" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`그림 2: IAM 서비스`
  3. 이제 다음과 같은 세 개 이상의 필드가 표시됩니다. _서비스 인스턴스_, _리소스 유형_ 및 _리소스 ID_. 첫 번째 필드는 사용자가 액세스할 수 있는 {{site.data.keyword.cos_short}}의 인스턴스를 정의합니다. 또한 {{site.data.keyword.cos_short}}의 모든 인스턴스에 동일한 레벨의 액세스 권한을 부여하도록 설정될 수 있습니다. 현재는 다른 필드를 공백으로 둘 수 있습니다.
	<img alt="IAM 사용자 초대" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`그림 3: IAM 사용자 초대`
  4. **역할 선택** 아래 선택란은 사용자가 사용할 수 있는 조치 세트를 판별합니다. "관리자" 플랫폼 액세스 역할을 선택하여 사용자가 인스턴스에 대한 다른 [사용자 및 서비스 ID](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) 액세스를 부여할 수 있게 하십시오. "관리자" 서비스 액세스 역할을 선택하여 사용자가 버킷과 오브젝트 작성 및 삭제뿐만 아니라 {{site.data.keyword.cos_short}} 인스턴스를 관리할 수 있게 하십시오. _주체_(사용자), _역할_(관리자) 및 _리소스_({{site.data.keyword.cos_short}} 서비스 인스턴스)의 조합은 [IAM 정책](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)을 구성합니다. 역할 및 정책에 대한 자세한 지침은 [IAM 문서를 참조하십시오](/docs/iam?topic=iam-userroles).
	<img alt="IAM 역할" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`그림 4: IAM 역할 선택`
  5. {{site.data.keyword.cloud_notm}}는 Cloud Foundry를 기본 계정 관리 플랫폼으로 사용하므로 우선 사용자가 조직에 액세스하려면 최소 레벨의 Cloud Foundry 액세스를 부여해야 합니다. **조직** 메뉴에서 조직을 선택한 다음 **조직 역할** 및 **영역 역할** 메뉴에서 "감사자"를 선택하십시오. Cloud Foundry 권한을 설정하면 사용자가 조직에서 사용 가능한 서비스를 볼 수 있지만 변경할 수는 없습니다.

## 개발자에게 버킷에 대한 액세스 권한 부여.
{: #gs-bucket-policy}

  1. **관리자** 메뉴로 이동하고 **액세스(IAM)** > **서비스 ID**의 링크를 따르십시오. 여기서 계정에 바인드된 추상화된 ID의 역할을 하는 _서비스 ID_를 작성할 수 있습니다. 서비스 ID에 API 키가 지정될 수 있으며 이 서비스 ID는 특정 개발자의 ID를 애플리케이션의 컴포넌트 또는 프로세스에 연결하지 않으려는 상황에서 사용됩니다.
	<img alt="IAM 서비스 ID" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`그림 5: IAM 서비스 ID`
  2. 위의 프로세스를 3단계에서 반복하고, 특정 서비스 인스턴스를 선택하고, "버킷"을 _리소스 유형_으로, 기존 버킷의 전체 CRN을 _리소스 ID_로 입력하십시오.
  3. 이제 서비스 ID는 이 특정 버킷에만 액세스할 수 있습니다.

## 다음 단계
{: #gs-next-steps}

웹 기반 콘솔을 통해 오브젝트 스토리지에 익숙해 있으므로, 서비스 인스턴스를 작성하고 IAM과 상호작용하려는 경우 `ibmcloud cos` 명령행 유틸리티를 사용하는 명령행에서, 그리고 COS에 직접 액세스하려는 경우 `curl`을 사용하여 유사한 워크플로우를 수행할 수 있습니다. [API 개요를 확인](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)하여 시작하십시오.
