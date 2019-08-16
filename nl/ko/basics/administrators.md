---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: administrator, storage, iam, access

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

# 관리자의 경우
{: #administrators}

오브젝트 스토리지를 구성하고 데이터에 대한 액세스를 관리해야 하는 스토리지 및 시스템 관리자는 IBM Cloud Identity and Access Management(IAM)를 이용하여 사용자를 관리하고, API 키를 작성하고 회전시키며, 사용자와 서비스에 역할을 부여할 수 있습니다. 아직 준비되지 않은 경우 [시작하기 튜토리얼](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)을 읽어 버킷, 오브젝트 및 사용자의 핵심 개념을 익히십시오.

## 스토리지 설정
{: #administrators-setup}

먼저 하나 이상의 오브젝트 스토리지 리소스 인스턴스와 데이터를 저장할 몇 가지 버킷이 있어야 합니다. 데이터에 대한 액세스를 세그먼트화하는 방법, 데이터가 실제로 상주할 위치 및 데이터에 액세스할 빈도에 대해 이러한 버킷을 고려하십시오.

### 액세스 세그먼트화
{: #administrators-access}

액세스를 세그먼트화할 수 있는 두 가지 레벨(리소스 인스턴스 레벨과 버킷 레벨)이 있습니다. 

개발 팀이 다른 팀에서 사용하는 인스턴스가 아닌 작업 중인 오브젝트 스토리지의 인스턴스에만 액세스하도록 할 수 있습니다. 또는 팀이 작성하는 소프트웨어만 저장되는 데이터를 실제로 편집하게 할 수 있으므로 클라우드 플랫폼에 대한 액세스 권한을 가진 개발자가 문제점 해결을 이유로 데이터 읽기만 수행하게 할 수 있습니다. 이는 서비스 레벨 정책의 예입니다.

스토리지 인스턴스에 대한 뷰어 액세스 권한이 있는 개발 팀 또는 개별 사용자가 하나 이상의 버킷에서 직접 데이터를 편집할 수 있는 경우 버킷 레벨 정책을 사용하여 계정 내 사용자에게 부여된 액세스 레벨을 높일 수 있습니다. 예를 들어, 사용자는 버킷을 새로 작성할 수 없지만 기존 버킷 내에서 오브젝트를 작성하고 삭제할 수 있습니다.

## 액세스 관리
{: #administrators-manage-access}

IAM은 다음과 같은 기초 개념을 기반으로 합니다. _주체_는 _리소스_에 대한 _역할_을 부여받습니다.

두 가지 기본 주체 유형인 _사용자_와 _서비스 ID_가 있습니다.

또 다른 개념인 _서비스 인증 정보_가 있습니다. 서비스 인증 정보는 {{site.data.keyword.cos_full}}의 인스턴스에 연결하는 데 필요한 중요한 정보 콜렉션입니다. 여기에는 최소한 {{site.data.keyword.cos_full_notm}}의 인스턴스에 대한 ID(예: 리소스 인스턴스 ID), 서비스/인증 엔드포인트 및 API 키와 주체를 연관시키는 수단(예: 서비스 ID)이 포함됩니다. 서비스 인증 정보를 작성하는 경우 이 정보를 기존 서비스 ID와 연관시키거나 새 서비스 ID를 작성하는 옵션이 있습니다.

따라서 개발 팀이 콘솔을 사용하여 오브젝트 스토리지 인스턴스 및 Kubernetes 클러스터를 볼 수 있게 하려면 오브젝트 스토리지 리소스에 대한 `뷰어` 역할과 컨테이너 서비스에 대한 `관리자` 역할이 필요합니다. `뷰어` 역할을 통해 사용자는 인스턴스가 있는지 확인하고 버킷과 오브젝트가 **아닌** 기존 인증 정보를 볼 수 있습니다. 서비스 인증 정보가 작성된 경우 서비스 ID와 연관됩니다. 이 서비스 ID가 버킷 및 오브젝트를 작성하고 영구 삭제할 수 있으려면 인스턴스에 대한 `관리자` 또는 `작성자` 역할을 가져야 합니다.

IAM 역할 및 권한에 대한 자세한 정보는 [IAM 개요](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)를 참조하십시오.
