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

# 常見問題 (FAQ)
{: #faq}

## API 問題
{: #faq-api}

儲存區名稱**是否{{site.data.keyword.cos_full}}區分大小寫？**

儲存區名稱必須是 DNS 可定址的，因此不區分大小寫。

**物件名稱中可用的字元數上限是多少？**

1024

**如何使用 API 找出儲存區的大小總計？**

無法使用單一要求來提取儲存區的大小。您需要列出儲存區的內容，並加總每個物件的大小。

**是否可以將資料從 AWS S3 移轉至 {{site.data.keyword.cos_full_notm}}？**

是，您可以使用現有工具在 {{site.data.keyword.cos_full_notm}} 中讀取及寫入資料。您需要配置 HMAC 認證，以容許工具進行鑑別。並非所有 S3 相容工具目前都不受支援。如需詳細資料，請參閱[使用 HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)。


## 供應項目問題
{: #faq-offering}

**帳戶是否有 100 個儲存區的限制？如果需要更多，會發生什麼情況？**

是，100 是現行儲存區限制。一般而言，除非資料需要位於不同的地區或儲存空間類別中，否則字首是將物件一起群組到儲存區的最佳方式。例如，若要將病患記錄分組，請一個病患使用一個字首。如果這不是可行的解決方案，請與客戶支援中心聯絡。

**如果我要使用 {{site.data.keyword.cos_full_notm}} Vault 或 Cold Vault 來儲存資料，是否需要建立另一個帳戶？**

否，儲存空間類別（及地區）定義於儲存區層次。只需建立一個設為所需儲存空間類別的新儲存區即可。

**使用 API 建立儲存區時，如何設定儲存空間類別？**

將儲存空間類別（例如 `us-flex`）指派給該儲存區的 `LocationConstraint` 配置變數。這是因為 AWS S3 與 {{site.data.keyword.cos_full_notm}} 處理儲存空間類別的方式有極大的差異。{{site.data.keyword.cos_short}} 會在儲存區層次設定儲存空間類別，而 AWS S3 會將儲存空間類別指派給個別物件。您可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes)中參照 `LocationConstraint` 的有效佈建碼清單。

**是否可以變更儲存區的儲存空間類別？例如，如果您有處於 'standard' 的正式作業資料，若不常使用，是否可以基於計費目的而將它輕鬆地切換至 'vault'？**

現今，儲存空間類別的變更需要手動移動資料，或將資料從某個儲存區複製到另一個具有所需儲存空間類別的儲存區。


## 效能問題
{: #faq-performance}

**{{site.data.keyword.cos_short}} 中的資料一致性是否會影響效能？**

與任何分散式系統的一致性都會產生成本，但相較於具有多個同步複製的系統，{{site.data.keyword.cos_full_notm}} 分散儲存系統的效率較高，額外負擔也較低。

**如果應用程式需要操作大型物件，是否會影響效能？**

基於效能最佳化，可以在多個組件中平行上傳及下載物件。


## 加密問題
{: #faq-encryption}

**{{site.data.keyword.cos_short}} 是否提供靜態及動態加密？**

是。靜態資料會使用自動提供者端的「進階加密標準 (AES)」256 位元加密及「安全雜湊演算法 (SHA)」256 雜湊來進行加密。動態資料會使用內建載波等級的「傳輸層安全/Secure Sockets Layer (TLS/SSL)」或具有 AES 加密的 SNMPv3 來進行加密。

**如果客戶想要加密其資料，一般加密額外負擔為何？**

伺服器端加密一律適用於客戶資料。相較於 S3 鑑別所需的雜湊及消除編碼，加密並不是 COS 處理成本的絕大部分。

**{{site.data.keyword.cos_short}} 是否加密所有資料？**

是，{{site.data.keyword.cos_short}} 會加密所有資料。

**{{site.data.keyword.cos_short}} 是否具有加密演算法的 FIPS 140-2 規範？**

是，已核准 IBM COS Federal 供應項目，其適用於需要已驗證 FIPS 配置的「FedRAMP 控管安全」控制項。IBM COS Federal 已通過 FIPS 140-2 1 級認證。如需 COS Federal Offering 的相關資訊，請透過 Federal 網站[與我們聯絡](https://www.ibm.com/cloud/government)。

**是否支援用戶端金鑰加密？**

是，使用 SSE-C 或 Key Protect 支援用戶端金鑰加密。

## 一般問題
{: #faq-general}

**單一儲存區中可以放入多少個物件？**

單一儲存區中的物件數目沒有實際限制。

**儲存區中是否可以有巢狀儲存區？**

否，儲存區不可以有巢狀儲存區。如果儲存區內需要較高層次的組織，支援使用字首：`{endpoint}/{bucket-name}/{object-prefix}/{object-name}`。請注意，物件的金鑰會保留組合 `{object-prefix}/{object-name}`。

**「類別 A」與「類別 B」要求有何差異？**

「類別 A」要求是涉及修改或列出的作業。這包括建立儲存區、上傳或複製物件、建立或變更配置、列出儲存區，以及列出儲存區的內容。「類別 B」要求是與從系統擷取物件或其關聯 meta 資料/配置相關的作業。從系統中刪除儲存區或物件是免費的。

**使用 Object Storage 來建構資料結構而讓您能夠「看到」它並找到所要尋找之內容的最佳方式為何？如果沒有目錄結構，似乎很難檢視一個具有 1000 個檔案的層次。**

您可以使用與每個物件相關聯的 meta 資料來尋找所要尋找的物件。Object Storage 的最大優點是與每個物件相關聯的 meta 資料。在 {{site.data.keyword.cos_short}} 中，每個物件最多可以有 4 MB 的 meta 資料。卸載至資料庫時，meta 資料提供絕佳的搜尋功能。在 4 MB 裡可以儲存大量的 (key, value) 配對。您也可以使用「字首」搜尋來尋找您要尋找的項目。例如，如果您使用儲存區來區隔每個客戶資料，則可以在儲存區內使用字首進行分組。例如：/bucket1/folder/object，其中 'folder/' 是字首。

**是否可以確認 {{site.data.keyword.cos_short}} 為「立即一致」，而非「最終一致」？**

{{site.data.keyword.cos_short}} 對於資料而言是「立即一致」，而對於使用情形統計作業而言是「最終一致」。


**{{site.data.keyword.cos_short}} 是否會像 HDFS 一樣自動分割資料，因此我可以像使用 Spark 一樣平行讀取分割區？**

{{site.data.keyword.cos_short}} 支援在物件上的範圍 GET，因此應用程式可以執行分散式分段讀取類型作業。執行分段將會在應用程式上進行管理。
