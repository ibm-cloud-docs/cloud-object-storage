---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: faq, questions

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

# FAQ
{: #faq}

## API 질문
{: #faq-api}

**{{site.data.keyword.cos_full}} 버킷 이름은 대소문자를 구분합니까?**

버킷 이름은 DNS 주소 지정 가능해야 하므로 대소문자를 구분하지 않습니다. 

**오브젝트 이름에 사용할 수 있는 최대 문자 수는 얼마입니까?**

1024자입니다. 

**API를 사용하여 내 버킷의 총 크기를 알아보는 방법은 무엇입니까?**

하나의 요청으로 버킷의 크기를 페치하는 것은 불가능합니다. 사용자는 버킷의 컨텐츠를 나열하고 각 오브젝트의 크기를 합산해야 합니다. 

**AWS S3에서 {{site.data.keyword.cos_full_notm}}로 데이터를 마이그레이션할 수 있습니까?**

예, 기존 도구를 사용하여 {{site.data.keyword.cos_full_notm}}에서 데이터를 읽고 쓸 수 있습니다. 사용자는 도구가 인증할 수 있도록 HMAC 인증 정보를 구성해야 합니다. S3 호환 도구 중 일부는 현재 지원됩니다. 세부사항은 [HMAC 인증 정보 사용](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)을 참조하십시오. 


## 오퍼링 질문
{: #faq-offering}

**하나의 계정에 대해 100개의 버킷 수 한계가 있습니까? 더 많이 필요한 경우에는 어떻게 해야 합니까?**

예, 현재 버킷 수 한계는 100개입니다. 데이터를 서로 다른 지역 또는 스토리지 클래스에 두어야 하는 경우가 아니면 일반적으로는 접두부가 버킷의 오브젝트를 그룹화하는 더 좋은 방법입니다. 예를 들어, 환자 레코드를 그룹화하려는 경우에는 환자당 하나의 접두어를 사용합니다. 이 솔루션을 사용할 수 없는 경우에는 고객 지원에 문의하십시오. 

**{{site.data.keyword.cos_full_notm}} Vault 또는 Cold Vault를 사용하여 데이터를 저장하려면 다른 계정을 작성해야 합니까?**

아니오, 스토리지 클래스(지역도 마찬가지)는 버킷 레벨에서 정의됩니다. 단순히 원하는 스토리지 클래스로 설정된 새 버킷을 작성하기만 하면 됩니다. 

**API를 사용하여 버킷을 작성할 때 스토리지 클래스는 어떻게 설정합니까?**

스토리지 클래스(예: `us-flex`)는 해당 버킷에 대한 `LocationConstraint` 구성 변수에 지정됩니다. 이는 AWS S3와 {{site.data.keyword.cos_full_notm}} 간에 스토리지 클래스를 처리하는 방법이 서로 다르기 때문입니다. {{site.data.keyword.cos_short}}는 스토리지 클래스를 버킷 레벨에서 설정하지만 AWS S3는 스토리지 클래스를 개별 오브젝트에 지정합니다. `LocationConstraint`에 대한 유효한 프로비저닝 코드의 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes)에서 참조할 수 있습니다. 

**버킷의 스토리지 클래스를 변경할 수 있습니까? 예를 들어 'standard'에 프로덕션 데이터가 있으며, 이를 빈번히 사용하지 않는 경우에는 청구 관련 이유로 이를 쉽게 'vault'로 전환할 수 있습니까?**

현재는 스토리지 클래스를 변경하려면 한 버킷에서 원하는 스토리지 클래스의 다른 버킷으로 데이터를 수동으로 이동하거나 복사해야 합니다. 


## 성능 질문
{: #faq-performance}

**{{site.data.keyword.cos_short}}의 데이터 일관성이 성능에 영향을 줍니까?**

모든 분산 시스템을 통한 일관성 확보에는 대가가 따르지만, 다중 동기 사본을 사용하는 다른 시스템과 비교했을 때 {{site.data.keyword.cos_full_notm}} 분산 스토리지 시스템의 효율은 더 높고 오버헤드는 더 낮습니다. 

**내 애플리케이션이 대형 오브젝트를 조작해야 하는 경우 성능에 영향이 있습니까?**

성능 최적화를 위해 오브젝트를 다중 파트로 분할하여 병렬로 업로드 및 다운로드할 수 있습니다. 


## 암호화 질문
{: #faq-encryption}

**{{site.data.keyword.cos_short}}에서 저장 시 및 전송 중 암호화를 제공합니까?**

예. 저장 데이터는 제공자 측 고급 암호화 표준(AES) 256비트 자동 암호화 및 Secure Hash Algorithm(SHA)-256 해시를 사용하여 암호화됩니다. 전송 중 데이터는 통신 사업자 급 Transport Layer Security/Secure Sockets Layer(TLS/SSL) 또는 AES 암호화를 사용한 SNMPv3를 사용하여 보호됩니다. 

**고객이 자신의 데이터를 암호화하려는 경우의 일반적인 암호화 오버헤드는 무엇입니까?**

고객 데이터에 대해서는 항상 서버 측 암호화가 설정되어 있습니다. S3 인증 및 이레이저 코딩에서 필요한 해싱과 비교했을 때, 암호화가 COS의 처리 작업에서 큰 부분을 차지하지는 않습니다. 

**{{site.data.keyword.cos_short}}가 모든 데이터를 암호화합니까?**

예, {{site.data.keyword.cos_short}}는 모든 데이터를 암호화합니다. 

**{{site.data.keyword.cos_short}}는 암호화 알고리즘에 대해 FIPS 140-2를 준수합니까?**

예, IBM COS Federal 오퍼링은 유효성 검증된 FIPS 구성을 필요로 하는 FedRAMP Moderate 보안 제어에 대해 승인되었습니다. IBM COS Federal은 FIPS 140-2 레벨 1로 인증되었습니다. COS Federal 오퍼링에 대한 자세한 정보를 얻으려면 Federal 사이트를 통해 [문의하십시오](https://www.ibm.com/cloud/government). 

**클라이언트 키 암호화가 지원됩니까?**

예, SSE-C 또는 Key Protect를 사용하여 클라이언트 키 암호화가 지원됩니다. 

## 일반적인 질문
{: #faq-general}

**하나의 버킷에는 몇 개의 오브젝트가 있을 수 있습니까?**

하나의 버킷에 대한 실질적인 오브젝트 수 한계는 없습니다. 

**한 버킷에 다른 버킷을 중첩시킬 수 있습니까?**

아니오, 버킷은 중첩시킬 수 없습니다. 버킷 내에서 큰 레벨의 조직이 필요한 경우에는 접두부를 사용할 수 있습니다(`{endpoint}/{bucket-name}/{object-prefix}/{object-name}`). 오브젝트의 키는 변함없이 `{object-prefix}/{object-name}` 결합이라는 점을 참고하십시오. 

**'클래스 A' 요청과 '클래스 B' 요청의 차이는 무엇입니까?**

'클래스 A' 요청은 수정 또는 나열과 관련된 오퍼레이션입니다. 이는 버킷 작성, 오브젝트 업로드 또는 복사, 구성 작성 또는 변경, 버킷 컨텐츠 나열을 포함합니다. '클래스 B' 요청은 오브젝트, 또는 오브젝트의 연관된 메타데이터/구성을 시스템에서 검색하는 것과 관련되어 있습니다. 시스템에서 버킷 또는 오브젝트를 삭제하는 데 대한 비용은 없습니다. 

**데이터를 '보고' 원하는 것을 찾을 수 있도록 Object Storage를 사용하여 데이터를 구조화하는 가장 좋은 방법은 무엇입니까? 디렉토리 구조 없이 하나의 레벨에 1000개 이상의 파일을 두면 보기가 어렵습니다.**

각 오브젝트와 연관된 메타데이터를 사용하여 원하는 오브젝트를 찾을 수 있습니다. Object Storage의 가장 큰 장점은 각 오브젝트와 연관된 메타데이터입니다. {{site.data.keyword.cos_short}}에서 각 오브젝트는 최대 4MB의 메타데이터를 가질 수 있습니다. 메타데이터는 데이터베이스에 오프로드되면 탁월한 검색 기능을 제공합니다. 4MB에는 많은 수의 (키, 값) 쌍을 저장할 수 있습니다. 접두부 검색을 사용하여 원하는 항목을 찾을 수도 있습니다. 예를 들어, 각 고객 데이터를 구분하기 위해 버킷을 사용하는 경우에는 버킷 내에서 조직에 대해 접두부를 사용할 수 있습니다. 예를 들면 /bucket1/folder/object에서는 'folder/'가 접두부입니다. 

**{{site.data.keyword.cos_short}}가 '궁극적으로 일관적'이 아니라 '즉시 일관적'임을 확인해 줄 수 있습니까?**

{{site.data.keyword.cos_short}}는 데이터에 대해 '즉시 일관적'이며 사용 집계에 대해서는 '궁극적으로 일관적'입니다. 


**Spark 등으로 파티션을 병렬로 읽을 수 있도록, {{site.data.keyword.cos_short}}가 HDFS와 같이 데이터를 자동으로 파티셔닝할 수 있습니까?**

{{site.data.keyword.cos_short}}는 애플리케이션이 스트라이프로 분산된 읽기 유형의 오퍼레이션을 수행할 수 있도록 오브젝트에 대해 범위 지정된 GET을 지원합니다. 스트라이핑 수행은 애플리케이션에서 관리해야 합니다. 
