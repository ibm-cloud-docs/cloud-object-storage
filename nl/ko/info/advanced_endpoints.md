---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: endpoints, legacy, access points, manual failover

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

# 추가 엔드포인트 정보
{: #advanced-endpoints}

버킷의 복원성은 이를 작성하는 데 사용된 엔드포인트에 의해 정의됩니다. _교차 지역_ 복원성이 여러 대도시 지역에 데이터를 분산시키는 반면 _지역_ 복원성은 하나의 대도시 지역에 데이터를 분산시킵니다. _단일 데이터 센터_ 복원성은 데이터를 하나의 데이터 센터에 있는 여러 어플라이언스에 분산시킵니다. 지역 및 교차 지역 버킷은 한 사이트의 가동 중단 중에도 가용성을 유지할 수 있습니다. 

하나의 지역 {{site.data.keyword.cos_short}} 엔드포인트에 함께 배치된 컴퓨팅 워크로드는 낮은 대기 시간과 더 나은 성능을 보여줍니다. 하나의 지역에 집중되지 않은 워크로드의 경우 교차 지역 `geo` 엔드포인트는 가장 가까운 지역 데이터 센터로 연결을 라우팅합니다. 

교차 지역 엔드포인트를 사용할 때는 세 지역 전부에 데이터를 분산시키면서 특정 액세스 지점으로 인바운드 트래픽을 경로 지정할 수 있습니다. 개별 액세스 지점에 요청을 전송하는 경우에는 해당 지역이 사용 불가능해지는 경우 수행되는 자동화된 장애 복구가 없습니다. `geo` 엔드포인트가 아닌 액세스 지점으로 트래픽을 경로 지정하는 애플리케이션은 교차 지역 스토리지의 가용성 이점을 확보하기 위해 **반드시** 내부적으로 적절한 장애 복구 로직을 구현해야 합니다.
{:tip}

일부 워크로드는 단일 데이터 센터 엔드포인트를 사용하는 것이 이득인 경우가 있습니다. 단일 사이트에 저장된 데이터는 여전히 여러 실제 스토리지 어플라이언스에 분산되지만 하나의 데이터 센터에 포함되어 있습니다. 이는 동일한 사이트에 있는 컴퓨팅 리소스의 성능을 향상시킬 수 있지만, 해당 사이트가 가동 중단되는 경우에는 가용성을 유지하지 못합니다. 단일 데이터 센터 버킷은 사이트가 파괴되는 경우 자동화된 복제 또는 백업을 제공하지 않으므로 단일 사이트를 사용하는 애플리케이션은 해당 디자인에서 재해 복구를 고려해야 합니다. 

IAM을 사용하는 경우에는 모든 요청이 SSL을 사용해야 하며, 이 서비스는 모든 평문 요청을 거부합니다. 

엔드포인트 유형:

{{site.data.keyword.cloud}} 서비스는 공용, 개인용 및 관리 트래픽으로 구분된 3계층 네트워크에 연결됩니다. 

* **개인용 엔드포인트**는 Kubernetes 클러스터, Bare Metal Server, Virtual Server 및 기타 클라우드 스토리지 서비스로부터의 요청에 사용 가능합니다. 개인용 엔드포인트는 더 나은 성능을 제공하며, 트래픽이 지역 간 또는 데이터 센터 간을 이동하더라도 발신 또는 수신 대역폭에 대해 비용이 발생하지 않습니다. **가능한 경우에는 항상 개인용 엔드포인트를 사용하는 것이 가장 좋습니다. **
* **공용 엔드포인트**는 모든 위치로부터의 요청을 수락할 수 있으며 발신 대역폭에 따라 비용이 결정됩니다. 수신 대역폭은 무료입니다. 공용 엔드포인트는 {{site.data.keyword.cloud_notm}} 클라우드 컴퓨팅 리소스로부터의 액세스가 아닌 액세스에 사용해야 합니다.  

요청은 해당 버킷의 위치와 연관된 엔드포인트에 전송되어야 합니다. 버킷의 위치를 모르는 경우에는 서비스 인스턴스에 있는 모든 버킷의 위치 및 스토리지 클래스 정보를 리턴하는, [버킷 나열 API에 대한 확장](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended)을 사용할 수 있습니다. 

2018년 12월부로 엔드포인트가 업데이트되었습니다. 레거시 엔드포인트는 추가 알림이 있을 때까지 계속 작동합니다. [새 엔드포인트](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints)를 사용하도록 애플리케이션을 업데이트하십시오.
{:note}
