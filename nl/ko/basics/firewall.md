---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-21"

keywords: ip address, firewall, configuration, api

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

# 방화벽 설정
{: #setting-a-firewall}

IAM 정책은 관리자가 개별 버킷에 대한 액세스를 제한하는 방법을 제공합니다. 신뢰할 수 있는 네트워크에서만 특정 데이터에 액세스해야 하는 경우는 어떻게 합니까? 버킷 방화벽은 요청이 허용된 IP 주소 목록에서 시작되지 않는 한 데이터에 대한 모든 액세스를 제한합니다.
{: shortdesc}

방화벽 설정에 대한 몇 가지 규칙이 있습니다.

* 방화벽을 설정하거나 보는 사용자에게 버킷에 대한 `관리자` 역할이 있어야 합니다. 
* 버킷에 대한 `관리자` 역할이 있는 사용자는 우발적인 잠금을 방지하기 위해 IP 주소에서 허용된 IP 주소 목록을 보고 편집할 수 있습니다.
* 사용자의 IP 주소에 권한이 부여된 경우 {{site.data.keyword.cos_short}} 콘솔은 여전히 버킷에 액세스할 수 있습니다.
* 다른 {{site.data.keyword.cloud_notm}} 서비스에는 방화벽을 우회할 수 있는 **권한이 부여되지 않았습니다**. 이 제한사항은 버킷 액세스의 IAM 정책에 의존하는 다른 서비스(예: Aspera, SQL Query, Security Advisor, Watson Studio, Cloud Functions 등)가 이를 수행할 수 없음을 의미합니다.

방화벽이 설정되면 버킷이 나머지 {{site.data.keyword.cloud_notm}}에서 격리됩니다. 방화벽을 사용으로 설정하기 전에 이러한 상황이 버킷에 직접 액세스하는 다른 서비스에 의존하는 애플리케이션 및 워크플로우에 미치는 영향을 고려하십시오.
{: important}

## 콘솔을 사용하여 방화벽 설정
{: #firewall-console}

먼저 버킷이 있는지 확인하십시오. 없는 경우에는 [시작하기 튜토리얼](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)을 수행하여 콘솔 사용법을 숙지하십시오. 

### 권한 부여된 IP 주소 목록 설정
{: #firewall-console-enable}

1. {{site.data.keyword.cloud_notm}} [콘솔 대시보드](https://cloud.ibm.com/)에서 **스토리지**를 선택하여 리소스 목록을 보십시오.
2. 그런 다음 **스토리지** 메뉴 내에서 버킷이 있는 서비스 인스턴스를 선택하십시오. 이렇게 하면 {{site.data.keyword.cos_short}} 콘솔로 이동합니다.
3. 액세스를 권한 부여된 IP 주소로 제한할 버킷을 선택하십시오. 
4. 탐색 메뉴에서 **액세스 정책**을 선택하십시오.
5. **권한 부여된 IP** 탭을 선택하십시오.
6. **IP 주소 추가**를 클릭한 다음 **추가**를 선택하십시오.
7. [CIDR 표기법](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing)의 IP 주소 목록을 추가하십시오(예: `192.168.0.0/16, fe80:021b::0/64`). 주소는 IPv4 또는 IPv6 표준을 따를 수 있습니다. **추가**를 클릭하십시오.
8. 방화벽은 주소가 콘솔에 저장될 때까지 실행되지 않습니다. **모두 저장**을 클릭하여 방화벽을 실행하십시오.
9. 이제 이 버킷에 있는 모든 오브젝트는 해당 IP 주소에서만 액세스할 수 있습니다.

### IP 주소 제한사항 제거
{: #firewalls-console-disable}

1. **권한 부여된 IP** 탭을 통해 권한 부여된 목록에서 제거할 IP 주소 또는 범위 옆에 있는 상자를 선택하십시오.
2. **삭제**를 선택한 다음, **삭제**를 다시 클릭하여 대화 상자를 확인하십시오.
3. 변경사항이 콘솔에 저장될 때까지 업데이트된 목록이 적용되지 않습니다. **모두 저장**을 클릭하여 새 규칙을 시행하십시오.
4. 이제 이 버킷에 있는 모든 오브젝트는 해당 IP 주소에서만 액세스할 수 있습니다.

권한 부여된 IP 주소 목록이 없는 경우 이는 사용자의 IP 주소에 대한 제한 없이 일반 IAM 정책이 버킷에 적용됨을 의미합니다.
{: note}


## API를 통해 방화벽 설정
{: #firewall-api}

방화벽은 [COS 리소스 구성 API](https://cloud.ibm.com/apidocs/cos/cos-configuration)를 사용하여 관리됩니다. 이 새로운 REST API는 버킷 구성에 사용됩니다. 

`관리자` 역할이 있는 사용자는 우발적인 잠금을 방지하기 위해 네트워크에서 허용된 IP 주소 목록을 보고 편집할 수 있습니다.
{: tip}
