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

# 計費
{: #billing}

您可以在 [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window} 找到定價資訊。

## 發票
{: #billing-invoices}

在導覽功能表的**管理** > **計費及用量**中，尋找您的帳戶發票。

每個帳戶都會收到單一帳單。如果您需要對不同的容器集進行個別計費，則需要建立多個帳戶。

## {{site.data.keyword.cos_full_notm}} 定價
{: #billing-pricing}

{{site.data.keyword.cos_full}} 的儲存空間成本取決於儲存的資料總量、所使用的公用出埠頻寬量，以及系統所處理的作業要求總數。

基礎架構供應項目會連接至三層網路，分段公用、專用及管理資料流量。基礎架構服務可以跨專用網路相互傳送資料，而不需要任何成本。基礎架構供應項目（例如裸機伺服器、虛擬伺服器及雲端儲存空間）會連接至跨公用網路的 {{site.data.keyword.cloud_notm}} Platform 型錄（例如 Watson 服務及 Cloud Foundry 運行環境）中的其他應用程式及服務，因此會計量這兩種類型的供應項目之間的資料傳送，並以標準公用網路頻寬費率計費。
{: tip}

## 要求類別
{: #billing-request-classes}

「類別 A」要求涉及修改或列出。此種類包括建立儲存區、上傳或複製物件、建立或變更配置、列出儲存區，以及列出儲存區的內容。

「類別 B」要求與從系統擷取物件或其關聯 meta 資料或配置相關。

從系統中刪除儲存區或物件不會產生費用。

| 類別 | 要求 | 範例 |
|--- |--- |--- |
| 類別 A | PUT、COPY 及 POST 要求，以及用來列出儲存區和物件的 GET 要求 | 建立儲存區、上傳或複製物件、列出儲存區、列出儲存區的內容、設定 ACL，以及設定 CORS 配置 |
| 類別 B | GET（排除清單）、HEAD 及 OPTIONS 要求 | 擷取物件及 meta 資料 |

## Aspera 傳送
{: #billing-aspera}

[Aspera 高速傳送](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)會產生額外輸出費用。如需相關資訊，請參閱[定價頁面](https://www.ibm.com/cloud/object-storage#s3api)。

## 儲存空間類別
{: #billing-storage-classes}

並不需要頻繁存取所有儲存的資料，有些保存資料根本很少被存取。若為較少作用中的工作負載，您可以使用不同的儲存空間類別來建立儲存區，而這些儲存區中所儲存的物件會產生與標準儲存空間不同排程的費用。

有四個類別：

*  **標準**用於作用中工作負載，擷取資料是免費的（除了作業要求本身的成本之外）。
*  **儲存庫**用於資料存取少於一個月一次的冷工作負載 - 每次讀取資料時，都會套用額外的擷取費用 ($/GB)。服務包括物件大小和儲存期限臨界值下限，其與此服務的預期使用一致：儲存較冷且活動量不大的資料。
*  **冷儲存庫**用於每 90 天或更少天數存取一次資料的冷工作負載 - 每次讀取資料時，都會套用較大的額外擷取費用 ($/GB)。服務包括較大的物件大小和儲存期限臨界值下限，其與此服務的預期使用一致：儲存冷且非作用中的資料。
*  **Flex** 用於更難預測存取型樣的動態工作負載。取決於使用情形，如果成本及擷取費用超出上限值，則會捨棄擷取費用，並改為套用新的容量費用。如果未頻繁存取資料，則這會比「標準」儲存空間更具成本效益，而且，如果存取使用型樣非預期地變成更為活躍，則它會比「儲存庫」或「冷儲存庫」儲存空間更具成本效益。Flex 不需要最小的物件大小或儲存期限。

如需定價的相關資訊，請參閱 [ibm.com 上的定價表](https://www.ibm.com/cloud/object-storage#s3api)。

如需建立具有不同儲存空間類別的儲存區的相關資訊，請參閱 [API 參考資料](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)。
