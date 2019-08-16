---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# IAM 개요
{: #iam-overview}

{{site.data.keyword.cloud}} Identity & Access Management는 {{site.data.keyword.cloud_notm}} 플랫폼에서 사용자를 안전하게 인증하고 모든 클라우드 리소스에 대한 액세스를 일관되게 제어할 수 있게 해 줍니다. 자세한 정보는 [시작하기 튜토리얼](/docs/iam?topic=iam-getstarted#getstarted)을 참조하십시오. 

## ID 관리
{: #iam-overview-identity}

ID 관리는 사용자, 서비스 및 리소스의 상호작용을 포함합니다. 사용자는 IBM ID로 식별됩니다. 서비스는 서비스 ID로 식별됩니다. 그리고 리소스는 CRN을 사용하여 식별되고 주소 지정됩니다. 

{{site.data.keyword.cloud_notm}} IAM 토큰 서비스는 사용자 및 서비스에 대해 API 키를 작성하고, 업데이트하고, 삭제하고, 사용할 수 있게 해 줍니다. 이러한 API 키는 API 호출 또는 {{site.data.keyword.cloud}} 플랫폼 콘솔의 ID 및 액세스 섹션을 사용하여 작성할 수 있습니다. 동일한 키를 여러 서비스에서 사용할 수 있습니다. 각 사용자는 여러 API 키를 보유함으로써 키 순환 시나리오를 지원할 수 있으며, 하나의 키의 노출을 제한하기 위해 서로 다른 목적에 서로 다른 키를 사용하는 시나리오 또한 지원할 수 있습니다. 

자세한 정보는 [Cloud IAM 개념](/docs/iam?topic=iam-iamoverview#iamoverview)을 참조하십시오. 

### 사용자와 API 키
{: #iam-overview-user-api-keys}

{{site.data.keyword.cloud_notm}} 사용자는 CLI를 사용하는 경우 자동화 및 스크립팅 목적과 연합 로그인을 위해 API 키를 작성하고 사용할 수 있습니다. API 키는 Identity and Access Management UI에서 작성하거나 `ibmcloud` CLI를 사용하여 작성할 수 있습니다. 

### 서비스 ID와 API 키
{: #iam-overview-service-id-api-key}

IAM 토큰 서비스는 서비스 ID, 그리고 서비스 ID에 대한 API 키를 작성할 수 있도록 합니다. 서비스 ID는 "기능 ID" 또는 "애플리케이션 ID"와 유사하며 사용자를 나타내기 위해서가 아니라 서비스를 인증하는 데 사용됩니다. 

사용자는 서비스 ID를 작성하고 이를 {{site.data.keyword.cloud_notm}} 플랫폼 계정, Cloud Foundry 조직 또는 Cloud Foundry 영역과 같은 범위에 바인드할 수 있지만, IAM 채택을 위해서는 서비스 ID를 {{site.data.keyword.cloud_notm}} 플랫폼 계정에 바인드하는 것이 가장 좋습니다. 이 바인딩은 서비스 ID가 상주할 컨테이너를 제공하기 위해 수행됩니다. 이 컨테이너는 서비스 ID를 업데이트하고 삭제할 수 있는 사용자, 그리고 해당 서비스 ID와 연관된 API 키를 작성하고, 업데이트하고, 읽고, 삭제할 수 있는 사용자 또한 정의합니다. 서비스 ID는 사용자와 관련이 없다는 점을 아는 것이 중요합니다. 

### 키 순환
{: #iam-overview-key-rotation}

유출된 키로 인해 발생하는 보안 위반을 방지하기 위해 API 키는 정기적으로 순환되어야 합니다. 

## 액세스 관리
{: #iam-overview-access-management}

IAM 액세스 제어는 {{site.data.keyword.cloud_notm}} 리소스에 대한 사용자 역할을 지정하는 일반적인 방법을 제공하며 이러한 리소스에 대해 사용자가 수행할 수 있는 조치를 제어합니다. 사용자는 자신에게 부여된 액세스 옵션에 따라 계정 또는 조직에서 사용자를 보고 관리할 수 있습니다. 예를 들면, 계정 소유자에게는 Identity and Access Managemement의 계정 관리자 역할이 자동으로 지정되며, 이는 계정의 모든 구성원에 대한 서비스 정책을 지정하고 관리할 수 있도록 합니다. 

### 사용자, 역할, 리소스 및 정책
{: #iam-overview-access-policies}

IAM 액세스 제어는 지정된 컨텍스트 내에서 리소스 및 사용자를 관리할 수 있도록 하기 위한 액세스 레벨을 허용하기 위해 각 서비스 또는 서비스 인스턴스에 대해 정책을 지정할 수 있도록 합니다. 정책은 해당되는 리소스 세트를 정의하는 속성의 조합을 사용하여 사용자에게 리소스 세트에 대한 역할을 부여합니다. 사용자에게 정책을 지정할 때는 먼저 서비스를 지정한 후 지정할 역할을 지정합니다. 선택하는 서비스에 따라 추가 구성 옵션이 사용 가능할 수도 있습니다. 

역할은 조치의 콜렉션이지만, 이러한 역할에 맵핑되는 조치는 서비스별로 다릅니다. 각 서비스는 온보딩 프로세스 중에 이 역할 대 조치 맵핑을 결정하며 이 맵핑은 서비스의 모든 사용자에게 적용됩니다. 역할 및 액세스 정책은 정책 관리 지점(PAP)을 통해 구성되며 정책 적용 지점(PEP) 및 정책 결정 지점(PDP)을 통해 적용됩니다. 

더 자세히 알아보려면 [사용자, 팀, 애플리케이션 구성을 위한 우수 사례](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications)를 참조하십시오. 
