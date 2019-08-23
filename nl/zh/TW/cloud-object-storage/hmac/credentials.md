---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# 使用 HMAC 認證
{: #hmac}

{{site.data.keyword.cos_full}} API 是一個 REST 型 API，用來讀寫物件。它使用 {{site.data.keyword.iamlong}} 進行鑑別/授權，並支援 S3 API 子集，以輕鬆地將應用程式移轉至 {{site.data.keyword.cloud_notm}}。

除了 IAM 記號型鑑別之外，也可以[使用簽章進行鑑別](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)，而簽章是透過一對存取及密碼金鑰所建立。這與 AWS Signature 第 4 版的功能相同，而且 IBM COS 所提供的 HMAC 金鑰應該使用大部分的 S3 相容程式庫及工具。

在認證建立期間提供配置參數 `{"HMAC":true}` 來建立[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)時，使用者可以建立一組 HMAC 認證。以下範例顯示如何使用 {{site.data.keyword.cos_full}} CLI，以使用**撰寫者**角色來建立具有 HMAC 認證的服務金鑰（其他角色可能可用於您的帳戶，而且可能最符合您的需求）。 

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="範例 1. 使用 cURL 建立 HMAC 認證。請注意單引號及雙引號的使用。" caption-side="bottom"}

如果您想要儲存剛剛範例 1 中指令所產生索引鍵的結果，可以將 ` > file.skey` 附加至範例尾端。基於此指令集的目的，您只需要尋找具有下列子索引鍵的 `cos_hmac_keys` 標題：`access_key_id` 及 `secret_access_key`&mdash;您需要的兩個欄位，如範例 2 所示。

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="範例 2. 產生 HMAC 認證時的注意重點。" caption-side="bottom"}

特別感興趣的是設定環境變數的能力（所涉及作業系統特有的指令）。例如，在範例 3 中，`.bash_profile` Script 包含在啟動 Shell 時匯出並用於開發的 `COS_HMAC_ACCESS_KEY_ID` 及 `COS_HMAC_SECRET_ACCESS_KEY`。

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="範例 3. 使用 HMAC 認證作為環境變數。" caption-side="bottom"}

建立「服務認證」之後，`cos_hmac_keys` 欄位中會包括「HMAC 金鑰」。然後，這些 HMAC 金鑰會與[服務 ID](/docs/iam?topic=iam-serviceids#serviceids) 相關聯，而且可以用來存取「服務 ID」角色所容許的任何資源或作業。 

請注意，使用 HMAC 認證來建立要與直接 [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) 呼叫搭配使用的簽章時，需要額外的標頭：
1. 所有要求都必須具有日期格式為 `%Y%m%dT%H%M%SZ` 的 `x-amz-date` 標頭。
2. 任何具有有效負載的要求（物件上傳、刪除多個物件等等）都必須提供具有有效負載內容的 SHA256 雜湊的 `x-amz-content-sha256` 標頭。
3. 不支援 ACL（`public-read` 除外）。

並非所有 S3 相容工具目前都受支援。部分工具嘗試在建立儲存區時設定 `public-read` 以外的 ACL。透過這些工具建立的儲存區將會失敗。如果 `PUT 儲存區`要求失敗，並發生不支援的 ACL 錯誤，請先使用[主控台](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)來建立儲存區，然後配置工具，以將物件讀取及寫入至該儲存區。目前不支援在寫入物件時設定 ACL 的工具。
{:tip}
