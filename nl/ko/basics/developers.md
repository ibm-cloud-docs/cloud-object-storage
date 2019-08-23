---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# 개발자용
{: #gs-dev}
먼저 [{{site.data.keyword.cloud}} 플랫폼 CLI](https://cloud.ibm.com/docs/cli/index.html) 및 [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html)가 설치되어 있는지 확인하십시오.

## {{site.data.keyword.cos_full_notm}}의 인스턴스 프로비저닝
{: #gs-dev-provision}

  1. 먼저 API 키가 있는지 확인하십시오. [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys)에서 이 키를 확보하십시오.
  2. CLI를 사용하여 {{site.data.keyword.cloud_notm}} 플랫폼에 로그인하십시오. API 키를 파일에 저장하거나 환경 변수로 설정할 수도 있습니다.

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. 그 다음 인스턴스, ID 및 원하는 플랜(Lite 또는 표준)의 이름을 지정하는 {{site.data.keyword.cos_full_notm}}의 인스턴스를 프로비저닝하십시오. 이렇게 하면 CRN을 확보할 수 있습니다. 업그레이드된 계정이 있는 경우 `표준` 플랜을 지정하십시오. 그렇지 않으면 `Lite`를 지정하십시오.

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

[시작하기 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)는 사용자 초대 및 정책 작성뿐만 아니라 버킷과 오브젝트를 작성하는 기본 단계를 안내합니다. 기본 'curl' 명령 목록은 [여기](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)에 있습니다.

[문서](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli)에서 {{site.data.keyword.cloud_notm}} CLI를 사용하여 애플리케이션을 작성하고, Kubernetes 클러스터를 관리하는 방법 등에 대해 자세히 알아보십시오.
.


## API 사용
{: #gs-dev-api}

{{site.data.keyword.cos_short}}에 저장된 데이터를 관리하려면 호환성을 위해 [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)와 함께 [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)와 같은 S3 API 호환 가능 도구를 사용할 수 있습니다. IAM 토큰은 상대적으로 쉽게 작업할 수 있으므로 `curl`은 기본 테스트 및 스토리지와의 상호작용을 위한 좋은 선택이 됩니다. 자세한 정보는 [`curl` 참조](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) 및 [API 참조 문서](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)에 있습니다.

## 라이브러리 및 SDK 사용
{: #gs-dev-sdk}
[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) 및 [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)에 사용 가능한 IBM COS SDK가 있습니다. 이는 [IAM 토큰 기반 인증](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) 및 [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption)를 지원하도록 수정된 AWS S3 SDK의 분기된 버전입니다. 

## IBM Cloud에 애플리케이션 빌드
{: #gs-dev-apps}
{{site.data.keyword.cloud}}를 통해 개발자는 지정된 애플리케이션에 대한 올바른 아키텍처 및 배치 옵션을 유연하게 선택할 수 있습니다. [베어 메탈](https://cloud.ibm.com/catalog/infrastructure/bare-metal)에서, [가상 머신](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group)에서, [서버리스 프레임워크](https://cloud.ibm.com/openwhisk)를 사용하여, [컨테이너](https://cloud.ibm.com/kubernetes/catalog/cluster)에서, 또는 [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs)를 사용하여 코드를 실행하십시오. 

[Cloud Native Computing Foundation](https://www.cncf.io)은 [Kubernetes](https://kubernetes.io) 컨테이너 오케스트레이션 프레임워크를 개발하기 시작하여 최근에 "완성"했으며, {{site.data.keyword.cloud}} Kubernetes Service의 기초가 됩니다. Kubernetes 애플리케이션의 지속적 스토리지를 위해 오브젝트 스토리지를 사용하려는 개발자들은 다음 링크에서 자세히 볼 수 있습니다.

 * [스토리지 솔루션 선택](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [지속적 스토리지 옵션을 위한 비교 테이블](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [기본 COS 페이지](/docs/containers?topic=containers-object_storage)
 * [COS 설치](/docs/containers?topic=containers-object_storage#install_cos)
 * [COS 서비스 인스턴스 작성](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [COS 시크릿 작성](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [구성 결정](/docs/containers?topic=containers-object_storage#configure_cos)
 * [COS 프로비저닝](/docs/containers?topic=containers-object_storage#add_cos)
 * [백업 및 복원 정보](/docs/containers?topic=containers-object_storage#backup_restore)
 * [스토리지 클래스 참조](/docs/containers?topic=containers-object_storage#storageclass_reference)


