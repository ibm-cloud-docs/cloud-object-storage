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

# 其他端點資訊
{: #advanced-endpoints}

儲存區的備援是由用來建立它的端點所定義。_跨地區_ 備援會將資料分散到數個都會區域，而_地區_ 備援會將資料分散到單一都會區域。_單一資料中心_ 備援會將資料分散到單一資料中心內的多個應用裝置。「地區」及「跨地區」儲存區可以在網站中斷期間維護可用性。

與地區 {{site.data.keyword.cos_short}} 端點共置的運算工作負載將具有更低的延遲，以及更好的效能。如果工作負載未集中在單一地理區域，則「跨地區 `geo`」端點會將連線遞送至最接近的地區資料中心。

使用「跨地區」端點時，可以將入埠資料流量導向至特定存取點，同時仍然將資料配送至所有這三個地區。將要求傳送至個別存取點時，如果該地區變成無法使用，則沒有自動失效接手。將資料流量導向至存取點而非 `geo` 端點的應用程式**必須**在內部實作適當的失效接手邏輯，以達到跨地區儲存空間的可用性優點。
{:tip}

部分工作負載可能會受益於使用「單一資料中心」端點。單一站台中所儲存的資料仍然會配送至許多實體儲存空間應用裝置，但包含在單一資料中心內。這可以改善相同站台內運算資源的效能，但不會維護站台中斷時的可用性。「單一資料中心」儲存區不會在站台毀損時提供自動化抄寫或備份，因此任何使用單一站台的應用程式都應該考量其設計中的災難回復。

所有要求都必須在使用 IAM 時使用 SSL，而且服務會拒絕所有純文字要求。

端點類型：

{{site.data.keyword.cloud}} 服務會連接至三層網路，分段公用、專用及管理資料流量。

* **專用端點**可用於源自 Kubernetes 叢集、裸機伺服器、虛擬伺服器及其他雲端儲存空間服務的要求。專用端點提供更好的效能，而且即使資料流量跨地區或跨資料中心，也不會產生任何送出或送入頻寬的費用。**如果可能的話，最好使用專用端點。**
* **公用端點**可以接受來自任何位置的要求，而且根據送出的頻寬來評量費用。送入的頻寬是免費的。公用端點應該用於非源自 {{site.data.keyword.cloud_notm}} 雲端運算資源的存取權。 

要求必須傳送至與給定儲存區位置相關聯的端點。如果您不確定儲存區的位置，則會有[儲存區清單 API 的延伸](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended)，而它會傳回服務實例中所有儲存區的位置及儲存空間類別資訊。

自 2018 年 12 月開始，我們已更新端點。舊式端點會繼續運作，直到另行通知為止。請更新應用程式，以使用[新的端點](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints)。
{:note}
