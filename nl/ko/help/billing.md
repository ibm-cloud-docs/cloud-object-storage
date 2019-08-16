---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# 청구
{: #billing}

가격에 대한 정보는 [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window}에 있습니다.

## 송장
{: #billing-invoices}

탐색 메뉴의 **관리** > **청구 및 사용량**에서 계정 송장을 찾으십시오.

각 계정은 하나의 청구서를 받습니다. 여러 컨테이너 세트에 대해 개별 청구가 필요한 경우 다중 계정을 작성해야 합니다.

## {{site.data.keyword.cos_full_notm}} 가격
{: #billing-pricing}

{{site.data.keyword.cos_full}}에 대한 스토리지 비용은 저장된 총 데이터 볼륨, 사용되는 공용 아웃바운드 대역폭의 크기 및 시스템에서 처리되는 총 운영 요청 수로 결정됩니다.

인프라 오퍼링은 3개의 티어가 있는 네트워크에 연결되어 공용, 개인용 및 관리 트래픽을 세그먼트화합니다. 인프라 서비스는 비용 없이 사설 네트워크에서 서로 데이터를 전송할 수 있습니다. 인프라 오퍼링(예: 베어 메탈 서버, 가상 서버 및 클라우드 스토리지)은 공용 네트워크에서 {{site.data.keyword.cloud_notm}} 플랫폼 카탈로그의 다른 애플리케이션과 서비스(예: Watson 서비스 및 Cloud Foundry 런타임)에 연결되므로, 이러한 두 유형의 오퍼링 간 데이터 전송이 표준 공용 네트워크 대역폭 비율로 측정되어 비용이 청구됩니다.
{: tip}

## 요청 클래스
{: #billing-request-classes}

'클래스 A' 요청에는 수정 또는 나열이 포함됩니다. 이 카테고리에는 버킷 작성, 오브젝트 업로드 또는 복사, 구성 작성 또는 변경, 버킷 나열 및 버킷의 컨텐츠 나열이 포함됩니다.

'클래스 B' 요청은 시스템에서 오브젝트 또는 연관된 메타데이터나 구성 검색과 관련되어 있습니다.

시스템에서 버킷 또는 오브젝트 삭제 시 비용이 발생하지 않습니다.

| 클래스 | 요청 | 예 |
|--- |--- |--- |
| 클래스 A | 버킷과 오브젝트 나열에 사용되는 GET 요청 및 PUT, COPY 및 POST 요청 | 버킷 작성, 오브젝트 업로드 또는 복사, 버킷 나열, 버킷의 컨텐츠 나열, ACL 설정 및 CORS 구성 설정 |
| 클래스 B | GET(나열 제외), HEAD 및 OPTIONS 요청 | 오브젝트 및 메타데이터 검색 |

## Aspera 전송
{: #billing-aspera}

[Aspera 고속 전송](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) 시 추가 Egress 비용이 발생합니다. 자세한 정보는 [가격 페이지](https://www.ibm.com/cloud/object-storage#s3api)를 참조하십시오.

## 스토리지 클래스
{: #billing-storage-classes}

저장된 일부 데이터는 자주 액세스되지 않으며, 일부 아카이브 데이터는 거의 액세스되지 않을 수 있습니다. 덜 활성인 워크로드의 경우 다른 스토리지 클래스에서 버킷을 작성할 수 있으며 이러한 버킷에 저장된 오브젝트에는 표준 스토리지와 다른 스케줄에 대한 비용이 발생합니다.

다음과 같은 네 가지 클래스가 있습니다.

*  **표준**은 검색되는 데이터에 대한 비용 없이(운영 요청 비용 제외) 활성 워크로드에 사용됩니다.
*  **볼트**는 데이터가 한 달에 한 번 미만으로 액세스되는 쿨 워크로드에 사용됩니다. 데이터를 읽을 때마다 추가 검색 비용($/GB)이 적용됩니다. 서비스에는 이 서비스의 사용 목적, 즉, 덜 활성화된 쿨러 데이터에 적합한 오브젝트 크기 및 스토리지 기간에 대한 최소 임계값이 포함됩니다.
*  **콜드 볼트**는 데이터가 90일 이하마다 액세스되는 콜드 워크로드에 사용되며 데이터를 읽을 때마다 많은 추가 검색 비용($/GB)이 적용됩니다. 서비스에는 이 서비스의 사용 목적, 즉, 비활성화된 콜드 데이터에 적합한 오브젝트 크기 및 스토리지 기간에 대한 더 긴 최소 임계값이 포함됩니다.
*  **Flex**는 액세스 패턴 예측이 어려운 동적 워크로드에 사용됩니다. 사용량에 따라 비용 및 검색 비용이 한계 값을 초과하는 경우 검색 비용이 빠지고 대신 새 용량 비용이 적용됩니다. 데이터가 자주 액세스되지 않는 경우 이 Flex 스토리지가 표준 스토리지보다 비용면에서 효율적일 수 있으며, 액세스 사용 패턴이 예상치 못하게 더 활성화되는 경우 볼트 또는 콜드 볼트 스토리지보다 비용면에서 효율적입니다. Flex에는 최소 오브젝트 크기 또는 스토리지 기간이 필요하지 않습니다.

가격에 대한 자세한 정보는 [ibm.com의 가격 테이블](https://www.ibm.com/cloud/object-storage#s3api)을 참조하십시오.

여러 스토리지 클래스로 버킷을 작성하는 방법에 대한 자세한 정보는 [API 참조](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)를 참조하십시오.
