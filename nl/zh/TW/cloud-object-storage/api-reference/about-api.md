---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# 關於 {{site.data.keyword.cos_full_notm}} S3 API
{: #compatibility-api}

{{site.data.keyword.cos_full}} API 是一個 REST 型 API，用來讀寫物件。它使用 {{site.data.keyword.iamlong}} 進行鑑別及授權，並支援 S3 API 子集，以輕鬆地將應用程式移轉至 {{site.data.keyword.cloud_notm}}。

此參考文件正在持續改進中。如果您有在應用程式中使用 API 的相關技術問題，請將它們張貼在 [StackOverflow](https://stackoverflow.com/)。同時新增 `ibm-cloud-platform` 及 `object-storage` 標籤，並藉由您的意見來協助改善本文件。

由於 {{site.data.keyword.iamshort}} 記號相當容易使用，所以 `curl` 是基本測試以及與儲存空間互動的良好選擇。如需相關資訊，請參閱 [`curl` 參考資料](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)。

下列各表說明 {{site.data.keyword.cos_full_notm}} API 的完整作業集。如需相關資訊，請參閱[儲存區的 API 參考資料頁面](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)或[物件的 API 參考資料頁面](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)。


## 儲存區作業
{: #compatibility-api-bucket}

這些作業會建立儲存區、刪除儲存區、取得儲存區的相關資訊，以及控制儲存區的行為。

| 儲存區作業              | 附註                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` 儲存區            | 用來擷取屬於帳戶的所有儲存區清單。                                             |
| `DELETE` 儲存區         | 刪除空的儲存區。                                                               |
| `DELETE` 儲存區 CORS    | 刪除儲存區上設定的任何 CORS（跨原點資源共用）配置。                            |
| `GET` 儲存區            | 列出儲存區中的物件。限制為一次列出 1,000 個物件。                              |
| `GET` 儲存區 CORS       | 擷取儲存區上設定的任何 CORS 配置。                                             |
| `HEAD` 儲存區           | 擷取儲存區的標頭。                                                             |
| `GET` 多部分上傳        | 列出未完成或已取消的多部分上傳。                                               |
| `PUT` 儲存區            | 儲存區具有命名限制。帳戶限制為 100 個儲存區。                                  |
| `PUT` 儲存區 CORS       | 建立儲存區的 CORS 配置。                                                       |


## 物件作業
{: #compatibility-api-object}

這些作業會建立物件、刪除物件、取得物件的相關資訊，以及控制物件的行為。

| 物件作業                  | 附註                                                                                |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` 物件             | 刪除儲存區中的物件。                                                               |
| `DELETE` 批次             | 使用一個作業刪除儲存區中的數個物件。                                               |
| `GET` 物件                | 擷取儲存區中的物件。                                                               |
| `HEAD` 物件               | 擷取物件的標頭。                                                                   |
| `OPTIONS` 物件            | 檢查 CORS 配置，以查看是否可以傳送特定要求。                                       |
| `PUT` 物件                | 將物件新增至儲存區。                                                               |
| `PUT` 物件（副本）        | 建立物件的副本。                                                                   |
| 開始多部分上傳            | 建立要上傳的一組組件的上傳 ID。                                                    |
| 上傳組件                  | 上傳與上傳 ID 相關聯之物件的組件。                                                 |
| 上傳組件（副本）          | 上傳與上傳 ID 相關聯之現有物件的組件。                                             |
| 完成多部分上傳            | 組合來自與上傳 ID 相關聯之組件的物件。                                             |
| 取消多部分上傳            | 取消上傳及刪除與上傳 ID 相關聯的未完成組件。                                       |
| 列出組件                  | 傳回與上傳 ID 相關聯的組件清單                                                     |


在 {{site.data.keyword.cos_short}} 的專用雲端實作中，支援部分其他作業（例如標記及版本化），但目前在公用或專用雲端中則不予支援。如需自訂 Object Storage 解決方案的相關資訊，請參閱 [ibm.com](https://www.ibm.com/cloud/object-storage)。
