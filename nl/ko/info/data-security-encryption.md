---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# 데이터 보안 및 암호화
{: #security}

{{site.data.keyword.cos_full}}는 대량의 구조화되지 않은 데이터를 보안, 가용성 및 신뢰성을 보장하는 동시에 비용 효율적으로 저장하기 위해 혁신적인 접근법을 사용합니다. 이는 데이터를 알아볼 수 없는 여러 "조각"으로 분할하여 여러 데이터 센터의 네트워크에 분산시킴으로써 데이터 전송 및 저장에 기밀성 및 보안을 제공하는 정보 분산 알고리즘(IDA)을 사용하여 이뤄집니다. 어느 스토리지 노드에도 데이터의 완전한 사본은 존재하지 않으며, 노드의 서브세트만 사용 가능하면 네트워크에서 데이터를 완전히 검색할 수 있습니다. 

{{site.data.keyword.cos_full_notm}}의 모든 데이터는 저장 시 암호화됩니다. 이 기술은 오브젝트별로 생성되는 키를 사용하여 각 오브젝트를 개별적으로 암호화합니다. 이러한 키는 개별 노드 또는 하드 드라이브의 데이터가 유출되더라도 키 데이터를 밝혀낼 수 없도록 하는 전부-또는-전무 변환(AONT)을 사용하여, 오브젝트 데이터를 보호하는 데 사용되는 것과 동일한 정보 분산 알고리즘으로 보호되고 안전하게 저장됩니다. 

사용자가 암호화 키를 제어해야 하는 경우에는 [SSE-C를 사용하여 오브젝트별로](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c), 또는 [SSE-KP를 사용하여 버킷별로](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp) 루트 키를 제공할 수 있습니다. 

스토리지는 HTTPS를 통해 액세스할 수 있으며, 스토리지 디바이스는 내부적으로 서로 TLS를 사용하여 인증하고 통신합니다. 


## 데이터 삭제
{: #security-deletion}

데이터 삭제 후 삭제된 오브젝트의 복구 또는 재생성을 방지하는 다양한 메커니즘이 있습니다. 한 오브젝트의 삭제는 오브젝트가 삭제되었음을 나타내는 메타데이터를 표시하는 것부터 시작해 컨텐츠 영역 제거를 거쳐, 마침내 해당 조각 데이터를 나타내는 블록을 겹쳐씀으로써 드라이브 자체에서 삭제를 마무리하기까지 다양한 단계를 거칩니다. 누군가 데이터 센터를 침해했거나 실제 디스크를 확보한 경우, 오브젝트가 복구 불가능 상태가 되는 시간은 삭제 오퍼레이션의 단계에 따라 달라집니다. 메타데이터 오브젝트가 업데이트되는 경우에는 데이터 센터 네트워크 외부의 클라이언트가 더 이상 해당 오브젝트를 읽을 수 없게 됩니다. 컨텐츠 영역을 나타내는 조각 대부분이 스토리지 디바이스에 의해 완성된 경우에는 오브젝트에 액세스하는 것이 불가능합니다. 

## 테넌트 격리
{: #security-isolation}

{{site.data.keyword.cos_full_notm}}는 인프라를 공유하는 멀티테넌트 오브젝트 스토리지 솔루션입니다. 워크로드가 전용 또는 격리된 스토리지를 필요로 하는 경우에는 [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage)에 방문하여 대체 솔루션에 대한 자세한 정보를 얻으십시오. 
