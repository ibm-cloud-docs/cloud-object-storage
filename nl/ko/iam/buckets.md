---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# 버킷 권한
{: #iam-bucket-permissions}

UI 또는 CLI를 사용해 정책을 작성하여 버킷에 대한 사용자 및 서비스 ID의 액세스 역할을 지정하십시오. 

| 액세스 역할 | 조치 예                                                     |
|:------------|-------------------------------------------------------------|
| Manager     | 오브젝트를 공용으로 설정, 버킷 및 오브젝트 작성 및 영구 삭제|
| Writer      | 버킷 및 오브젝트 작성 및 영구 삭제                          |
| Reader      | 오브젝트 나열 및 다운로드                                   |
| ContentReader | 오브젝트 다운로드                                         |

## 사용자에게 액세스 부여
{: #iam-user-access}

사용자가 콘솔을 사용할 수 있어야 하는 경우에는 서비스 액세스 역할(`Reader` 등) 외에 인스턴스 자체에 대한 최소한의 플랫폼 액세스 역할인 `Viewer` 역할 **또한** 부여해야 합니다. 이렇게 하면 사용자가 모든 버킷을 보고 버킷에 포함된 오브젝트를 나열할 수 있게 됩니다. 그 후에는 왼쪽 탐색 메뉴에서 **버킷 권한**을 선택하고, 사용자를 선택한 후 이들이 필요로 하는 액세스 권한 레벨(`Manager` 또는 `Writer`)을 선택하십시오. 

사용자가 API를 사용하여 데이터와 상호작용하고, 콘솔 액세스를 필요로 하지 않으며, _동시에_ 계정의 구성원인 경우에는 상위 인스턴스에 대한 액세스 권한 없이 단일 버킷에 대한 액세스만 부여할 수 있습니다. 

## 정책 적용
{: #iam-policy-enforcement}

IAM 정책은 가장 큰 액세스 레벨에서 가장 제한적인 액세스 레벨로, 계층식으로 적용됩니다. 충돌은 더 관대한 정책으로 해결됩니다. 예를 들어, 특정 사용자에게 버킷에 대한 `Writer` 및 `Reader` 서비스 액세스 역할이 있는 경우 `Reader` 역할을 부여하는 정책은 무시됩니다. 

이는 서비스 인스턴스 및 버킷 레벨 정책에도 해당됩니다. 

- 사용자에게 서비스 인스턴스에 대한 `Writer` 역할을 부여하는 정책이 있고 단일 버킷에 대한 `Reader` 역할을 부여하는 정책이 있으면 버킷 레벨 정책이 무시됩니다. 
- 사용자에게 서비스 인스턴스에 대한 `Reader` 역할을 부여하는 정책이 있고 단일 버킷에 대한 `Writer` 역할을 부여하는 정책이 있으면 두 정책이 모두 적용되며 개별 버킷에 대해서는 더 관대한 `Writer` 역할이 우선순위를 갖습니다. 

액세스를 단일 버킷(또는 버킷 세트)으로 제한해야 하는 경우에는 콘솔 또는 CLI를 사용하여 사용자 또는 서비스 ID가 인스턴스 레벨 정책을 갖지 않도록 하십시오. 

### UI 사용
{: #iam-policy-enforcement-console}

새 버킷 레벨 정책을 작성하려면 다음 작업을 수행하십시오.  

  1. **관리** 메뉴에서 **액세스(IAM)** 콘솔로 이동하십시오. 
  2. 왼쪽 탐색 메뉴에서 **사용자**를 선택하십시오. 
  3. 사용자를 선택하십시오. 
  4. **액세스 정책** 탭을 선택하여 사용자의 기존 정책을 보거나, 새 정책을 지정하거나, 기존 정책을 편집하십시오. 
  5. **액세스 권한 지정**을 클릭하여 새 정책을 작성하십시오. 
  6. **리소스에 대한 액세스 권한 지정**을 선택하십시오. 
  7. 먼저 서비스 메뉴에서 **Cloud Object Storage**를 선택하십시오. 
  8. 그 후 적절한 서비스 인스턴스를 선택하십시오. **리소스 유형** 필드에 `bucket`을 입력하고 **리소스 ID** 필드에 버킷 이름을 입력하십시오. 
  9. 원하는 서비스 액세스 역할을 선택하십시오. 
  10.  **지정**을 클릭하십시오. 

**리소스 유형** 또는 **리소스** 필드를 비워 두면 인스턴스 레벨 정책이 작성된다는 점을 참고하십시오.
{:tip}

### CLI 사용
{: #iam-policy-enforcement-cli}

터미널에서 다음 명령을 실행하십시오. 

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

기존 정책을 나열하려면 다음 명령을 실행하십시오. 

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

기존 정책을 편집하려면 다음 명령을 실행하십시오. 

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## 서비스 ID에 액세스 부여
{: #iam-service-id}
애플리케이션, 또는 사람이 아닌 기타 엔티티에 액세스를 부여해야 하는 경우에는 서비스 ID를 사용하십시오. 이러한 서비스 ID는 이 목적을 위해 특별히 작성된 것이거나, 이미 사용 중인 기존 서비스 ID일 수 있습니다. 

### UI 사용
{: #iam-service-id-console}

  1. **관리** 메뉴에서 **액세스(IAM)** 콘솔로 이동하십시오. 
  2. 왼쪽 탐색 메뉴에서 **서비스 ID**를 선택하십시오. 
  3. 서비스 ID를 선택하여 기존 정책을 보거나, 새 정책을 지정하거나, 기존 정책을 편집하십시오. 
  3. 서비스 인스턴스, 서비스 ID 및 원하는 역할을 선택하십시오. 
  4. **리소스 유형** 필드에 `bucket`을 입력하고 **리소스** 필드에 버킷 이름을 입력하십시오. 
  5. **제출**을 클릭하십시오. 

  **리소스 유형** 또는 **리소스** 필드를 비워 두면 인스턴스 레벨 정책이 작성된다는 점을 참고하십시오.
{:tip}

### CLI 사용
{: #iam-service-id-cli}

터미널에서 다음 명령을 실행하십시오. 

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

기존 정책을 나열하려면 다음 명령을 실행하십시오. 

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

기존 정책을 편집하려면 다음 명령을 실행하십시오. 

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
