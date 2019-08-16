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

# IAM 시작하기
{: #iam}

## Identity and Access Management 역할 및 조치
{: #iam-roles}

계정 내 사용자의 {{site.data.keyword.cos_full}} 서비스 인스턴스에 대한 액세스는 {{site.data.keyword.Bluemix_notm}} Identity and Access Management(IAM)에 의해 제어됩니다. 계정에서 {{site.data.keyword.cos_full}} 서비스에 액세스하는 모든 사용자에게는 IAM 사용자 역할이 정의된 액세스 정책이 지정되어야 합니다. 이 정책은 선택된 서비스 또는 인스턴스의 컨텍스트에서 사용자가 수행할 수 있는 조치를 결정합니다. {{site.data.keyword.Bluemix_notm}} 서비스는 허용 가능한 조치를 서비스에서 수행할 수 있도록 허용된 오퍼레이션으로 사용자 정의하고 정의합니다. 이러한 조치는 그 후 IAM 사용자 역할에 맵핑됩니다. 

정책은 다양한 레벨에서 액세스 권한을 부여할 수 있도록 합니다. 몇 가지 옵션에는 다음 항목이 포함됩니다.  

* 계정의 모든 서비스 인스턴스에 액세스
* 계정의 개별 서비스 인스턴스에 액세스
* 인스턴스의 특정 리소스에 액세스
* 계정의 모든 IAM 사용 서비스에 액세스

액세스 정책의 범위를 정의한 후에는 역할을 지정합니다. {{site.data.keyword.cos_short}} 서비스에서 각 역할에 허용되는 조치에 대한 간략한 설명은 다음 표를 검토하십시오. 

다음 표에는 플랫폼 관리 역할에 맵핑된 조치가 자세히 설명되어 있습니다. 플랫폼 관리 역할은 서비스에 대한 사용자 액세스를 지정하고, 서비스 ID를 작성하거나 삭제하고, 인스턴스를 작성하고, 인스턴스를 애플리케이션에 바인드하는 등과 같이 사용자가 플랫폼 레벨에서 서비스 리소스에 대해 태스크를 수행할 수 있도록 합니다. 

| 플랫폼 관리 역할 | 조치에 대한 설명 | 조치 예          |
|:-----------------|:-----------------|:-----------------|
| Viewer | 서비스 인스턴스를 볼 수 있으나 수정할 수는 없음 | <ul><li>사용 가능한 COS 서비스 인스턴스 나열</li><li>COS 서비스 플랜 세부사항 보기</li><li>사용량 세부사항 보기</li></ul>|
| Editor | 계정 관리 및 액세스 정책 지정을 제외한 모든 플랫폼 조치 수행 |<ul><li>COS 서비스 인스턴스 작성 및 삭제</li></ul> |
| Operator | COS에서 사용되지 않음 |없음 |
| Administrator | 액세스 정책을 다른 사용자에게 지정하는 것을 비롯하여, 이 역할이 지정된 리소스를 기반으로 모든 플랫폼 조치 수행 |<ul><li>사용자 정책 업데이트</li>가격 플랜 업데이트</ul>|
{: caption="표 1. IAM 사용자 역할 및 조치" caption-side="top"}


다음 표에는 서비스 액세스 역할에 맵핑된 조치가 자세히 설명되어 있습니다. 서비스 액세스 역할은 사용자가 {{site.data.keyword.cos_short}}에 액세스하고 {{site.data.keyword.cos_short}} API를 호출할 수 있게 해 줍니다. 

| 서비스 액세스 역할  | 조치에 대한 설명                                                                                                                                             | 조치 예                                                                                            |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| Content Reader      | 오브젝트를 다운로드할 수 있으나 오브젝트 또는 버킷을 나열할 수는 없음 | <ul><li> 오브젝트 다운로드</li></ul> |
| Reader              | Reader는 Content Reader 조치와 더불어 버킷 및/또는 오브젝트를 나열할 수도 있으나 이를 수정할 수는 없음 | <ul><li>버킷 나열</li><li>오브젝트 나열 및 다운로드</li></ul>                    |
| Writer              | Writer는 Reader 조치와 더불어 버킷을 작성하고 오브젝트를 업로드할 수 있음 | <ul><li>새 버킷 및 오브젝트 작성</li><li>버킷 및 오브젝트 제거</li></ul> |
| Manager             | Manager는 Writer 조치와 더불어 액세스 제어에 영향을 주는 권한 부여된 조치를 완료할 수 있음 | <ul><li>보존 정책 추가</li><li>버킷 방화벽 추가</li></ul>              |
{: caption="표 3. IAM 서비스 액세스 역할 및 조치" caption-side="top"}


UI에서 사용자 역할을 지정하는 데 대한 정보는 [IAM 액세스 관리](/docs/iam?topic=iam-iammanidaccser)를 참조하십시오. 
 
