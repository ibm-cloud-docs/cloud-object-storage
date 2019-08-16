---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# 開發人員指引
{: #dev-guide}

## 調整密碼設定
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} 支援各種密碼設定，以加密傳輸中資料。並非所有密碼設定都會產生相同的層次效能。協議 `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`、`TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`、`TLS_RSA_WITH_AES_256_CBC_SHA`、`TLS_RSA_WITH_AES_128_CBC_SHA` 中的其中一個時顯示產生相同的效能層次，因為用戶端與 {{site.data.keyword.cos_full_notm}} System 之間沒有 TLS。

## 使用多部分上傳
{: #dev-guide-multipart}

使用較大型的物件時，建議使用多部分上傳作業，將物件寫入至 {{site.data.keyword.cos_full_notm}}。單一物件上傳可以作為一組組件來執行，而且可以依任何順序平行獨立上傳這些組件。上傳完成時，{{site.data.keyword.cos_short}} 接著會將所有組件呈現為單一物件。這提供許多優點：網路岔斷不會導致大型上傳失敗、可以在一段時間後暫停並重新啟動上傳，而且物件可以在建立時上傳。

多部分上傳僅適用於大於 5MB 的物件。對於小於 50GB 的物件，建議使用 20MB 到 100MB 的組件大小，以達到最佳效能。對於較大的物件，可以增加組件大小，而不會造成重大效能影響。

使用超過 500 個組件會導致 {{site.data.keyword.cos_short}} 效率不佳，因此應該盡可能避免使用。

因為涉及額外的複雜性，所以建議開發人員使用提供多部分上傳支援的 S3 API 程式庫。

除非刪除物件，或使用 `AbortIncompleteMultipartUpload` 中斷多部分上傳，否則會持續保留不完整的多部分上傳。如果未中斷不完整的多部分上傳，則局部上傳會繼續使用資源。設計介面時，應該記住這一點，並清除不完整的多部分上傳。


## 使用軟體開發套件
{: #dev-guide-sdks}

不需要使用已發佈的 S3 API SDK；自訂軟體可以運用 API 直接與 {{site.data.keyword.cos_short}} 整合。不過，使用已發佈的 S3 API 程式庫提供下列這類優點：產生鑑別/簽章、發生 `5xx` 錯誤時自動重試邏輯，以及產生預先簽署 URL。撰寫軟體直接使用 API 來處理暫時性錯誤時，必須謹慎小心，例如，在接收到 `503` 錯誤時提供具有指數後退的重試。

## 分頁
{: #dev-guide-pagination}

處理儲存區中的大量物件時，Web 應用程式的效能可能會開始降低。許多應用程式都會採用稱為**分頁**的技術（*將大型記錄集分割成離散頁面的處理程序*）。幾乎所有開發平台都提供物件或方法，以透過內建功能或透過協力廠商程式庫來完成分頁。

{{site.data.keyword.cos_short}} SDK 透過列出指定儲存區內物件的方法來支援分頁。此方法提供一些參數，讓它在嘗試拆解大型結果集時非常有用。

### 基本用法
{: #dev-guide-pagination-basics}
物件清單方法背後的基本概念涉及設定要在回應中傳回的金鑰數目上限 (`MaxKeys`)。回應也包括 `boolean` 值 (`IsTruncated`) 以指出是否有其他結果可用，以及稱為 `NextContinuationToken` 的 `string` 值。除非沒有其他結果可用，否則在後續要求中設定接續記號會傳回下一批的物件。

#### 一般參數
{: #dev-guide-pagination-params}

|參數|說明|
|---|---|
|`ContinuationToken`|設定記號，以指定下一批的記錄|
|`MaxKeys`|設定要包含在回應中的索引鍵數目上限|
|`Prefix`|將回應限制為開頭為指定字首的索引鍵|
|`StartAfter`|根據索引鍵，設定開始物件清單的位置|

### 使用 Java
{: #dev-guide-pagination-java}

{{site.data.keyword.cos_full}} SDK for Java 提供 [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} 方法，以容許傳回所需大小的物件清單。[這裡](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2)提供完整程式碼範例。

### 使用 Python
{: #dev-guide-pagination-python}

{{site.data.keyword.cos_full}} SDK for Python 提供 [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} 方法，以容許傳回所需大小的物件清單。[這裡](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2)提供完整程式碼範例。
