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

# 管理者專用
{: #administrators}

需要配置物件儲存空間以及管理資料存取權的儲存空間和系統管理者，可以充分運用 IBM Cloud Identity and Access Management (IAM) 來管理使用者、建立及替換 API 金鑰，以及將角色授與使用者和服務。如果您尚未完成，請先詳閱[入門指導教學](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)，以充分瞭解儲存區、物件及使用者的核心概念。

## 設定儲存空間
{: #administrators-setup}

首先，您需要至少有一個物件儲存空間資源實例，以及一些要儲存資料的儲存區。請根據要如何進一步分段資料的存取權、希望資料實際所在的位置，以及資料存取的頻率，來考慮這些儲存區。

### 分段存取權
{: #administrators-access}

您可以利用下列兩種層次來分段存取權：資源實例層次及儲存區層次。 

也許您要確定開發團隊只能存取他們所使用的物件儲存空間實例，而不能存取其他團隊所使用的實例。或者，您要確保只有您的團隊所做的軟體才能實際編輯所儲存的資料，讓具有雲端平台存取權的開發人員只能夠基於疑難排解原因來讀取資料。這些是服務水準原則範例。

現在，如果開發團隊或任何個別使用者具有儲存空間實例的檢視者存取權，但應該可以直接編輯一個以上儲存區中的資料，則您可以使用儲存區層次原則，將授與的存取層次提升為您帳戶內的使用者。例如，使用者可能無法建立新的儲存區，但可以在現有儲存區內建立及刪除物件。

## 管理存取權
{: #administrators-manage-access}

IAM 是以基本概念為基礎：_主體_ 會獲授與_資源_ 的_角色_。

主體有兩種基本類型：_使用者_ 及_服務 ID_。

還有另一個概念，即_服務認證_。服務認證是連接至 {{site.data.keyword.cos_full}} 實例所需的重要資訊集合。這至少包括 {{site.data.keyword.cos_full_notm}} 實例的 ID（即「資源實例 ID」）、服務/鑑別端點，以及將主體與 API 金鑰（即服務 ID）相關聯的方法。當您建立服務認證時，可以選擇將它與現有服務 ID 相關聯，或建立新的服務 ID。

因此，如果您要容許開發團隊可以使用主控台來檢視 Object Storage 實例及 Kubernetes 叢集，則他們需要 Object Storage 資源的`檢視者`角色及「容器服務」的`管理者`角色。請注意，`檢視者`角色只容許使用者看到實例已存在，以及檢視現有認證，而**無法**檢視儲存區和物件。服務認證建立時，即已與服務 ID 相關聯。此服務 ID 需要具有實例的`管理員`或`撰寫者`角色，才能建立及破壞儲存區和物件。

如需 IAM 角色及許可權的相關資訊，請參閱 [IAM 概觀](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)。
