---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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


# {{site.data.keyword.cos_full_notm}} 정보
{: #about-ibm-cloud-object-storage}

{{site.data.keyword.cos_full}}에 저장된 정보는 여러 지리적 위치에서 암호화되어 분산되고 REST API를 사용하여 HTTP를 통해 액세스됩니다. 이 서비스는 {{site.data.keyword.cos_full_notm}} 시스템(이전의 Cleversafe)에서 제공된 분산 스토리지 기술을 사용합니다.

{{site.data.keyword.cos_full_notm}}는 세 가지 유형의 복원성(교차 지역, 지역 및 단일 데이터 센터)으로 사용 가능합니다. 교차 지역은 대기 시간이 약간 더 길지만 단일 지역을 사용하는 경우보다 내구성과 가용성이 높으며 미국, 유럽 연합 및 아시아 태평양에서 사용 가능합니다. 지역 서비스는 이러한 트레이드오프를 뒤바꿔 단일 지역 내 여러 가용성 구역에 오브젝트를 분배하며 미국, 유럽 연합 및 아시아 태평양 지역에서 사용 가능합니다. 지정된 지역 또는 가용성 구역이 사용 불가능한 경우 오브젝트 저장소는 방해 없이 계속 작동합니다. 단일 데이터 센터는 동일한 실제 위치 내 여러 시스템에 오브젝트를 분배합니다. 사용 가능한 지역은 [여기](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)를 확인하십시오.

개발자들은 {{site.data.keyword.cos_full_notm}} API를 사용하여 오브젝트 저장소와 상호작용합니다. 이 문서에서는 버킷을 작성하고 오브젝트를 업로드하며 일반 API 상호작용의 참조를 사용할 수 있도록 프로비저닝 계정으로 [시작하기](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)에 대한 지원을 제공합니다.

## 기타 IBM Object Storage 서비스
{: #about-other-cos}
{{site.data.keyword.cos_full_notm}} 외에, {{site.data.keyword.cloud_notm}}는 현재 서로 다른 사용자의 요구에 맞는 여러 추가 오브젝트 스토리지 오퍼링을 제공하며 이러한 오퍼링은 모두 웹 기반 포털 및 REST API를 통해 액세스 가능합니다. [자세히 보기.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
