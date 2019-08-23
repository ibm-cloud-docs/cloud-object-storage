---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# 管理加密
{: #encryption}

依預設，會使用[隨機產生的金鑰及全部轉換或根本不轉換](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security)來加密 {{site.data.keyword.cos_full}} 中所儲存的所有物件。雖然這個預設加密模型提供靜態安全，但是有些工作負載需要擁有已使用的加密金鑰。您可以在儲存資料 (SSE-C) 時提供自己的加密金鑰來手動管理金鑰，也可以建立使用 IBM Key Protect (SSE-KP) 來管理加密金鑰的儲存區。

## 具有客戶提供金鑰的伺服器端加密 (SSE-C)
{: #encryption-sse-c}

SSE-C 在物件上強制執行。使用客戶受管理金鑰來讀取或寫入物件或其 meta 資料的要求，會將必要的加密資訊傳送為 HTTP 要求中的標頭。此語法與 S3 API 相同，而且支援 SSE-C 的 S3 相容程式庫應該針對 {{site.data.keyword.cos_full}} 如預期運作。

必須使用 SSL 傳送任何使用 SSE-C 標頭的要求。請注意，回應標頭中的 `ETag` 值*不* 是物件的 MD5 雜湊，而是隨機產生的 32 個位元組的十六進位字串。

標頭 | 類型 | 說明
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` |字串   | 此標頭用來指定要與 `x-amz-server-side-encryption-customer-key` 標頭中所儲存的加密金鑰搭配使用的演算法及金鑰大小。此值必須設為字串 `AES256`。
`x-amz-server-side-encryption-customer-key` |字串   | 此標頭用來傳輸伺服器端加密處理程序中所使用 AES 256 金鑰的 base64 編碼位元組字串表示法。
`x-amz-server-side-encryption-customer-key-MD5` |字串   | 此標頭用來根據 RFC 1321 傳輸加密金鑰的 base64 編碼 128 位元 MD5 摘要。此物件儲存庫將會使用此值來驗證 `x-amz-server-side-encryption-customer-key` 中的金鑰通行證在傳輸及編碼處理程序期間並未毀損。在金鑰是 base64 編碼「之前」，必須計算金鑰的摘要。


## 具有 {{site.data.keyword.keymanagementservicelong_notm}} 的伺服器端加密 (SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}} 是一種集中式金鑰管理系統 (KMS)，用於產生、管理及破壞 {{site.data.keyword.cloud_notm}} 服務所使用的加密金鑰。您可以從 {{site.data.keyword.cloud_notm}} 型錄中建立 {{site.data.keyword.keymanagementserviceshort}} 的實例。

在您要建立新儲存區的地區中具有 {{site.data.keyword.keymanagementserviceshort}} 的實例之後，您需要建立根金鑰，並記下該金鑰的 CRN。

您可以選擇使用 {{site.data.keyword.keymanagementserviceshort}}，只在建立時管理儲存區的加密。無法變更現有儲存區來使用 {{site.data.keyword.keymanagementserviceshort}}。
{:tip}

建立儲存區時，您需要提供其他標頭。

如需 {{site.data.keyword.keymanagementservicelong_notm}} 的相關資訊，[請參閱文件](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect)。

### 開始使用 SSE-KP
{: #sse-kp-gs}

依預設，會使用多個隨機產生的金鑰及全部轉換或根本不轉換來加密 {{site.data.keyword.cos_full}} 中所儲存的所有物件。雖然這個預設加密模型提供靜態安全，但是有些工作負載需要擁有已使用的加密金鑰。您可以使用 [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) 來建立、新增及管理金鑰，然後，您可以建立它與 {{site.data.keyword.cos_full}} 實例的關聯，以加密儲存區。

### 開始之前
{: #sse-kp-prereqs}

您將需要：
  * [{{site.data.keyword.cloud}} Platform 帳戶](http://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}} 實例](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * [{{site.data.keyword.keymanagementservicelong_notm}} 實例](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * 以及本端電腦上一些要上傳的檔案。

### 在 {{site.data.keyword.keymanagementserviceshort}} 中建立或新增金鑰
{: #sse-kp-add-key}

導覽至 {{site.data.keyword.keymanagementserviceshort}} 實例，並[產生或輸入金鑰](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)。

### 授與服務授權
{: #sse-kp}
授權 {{site.data.keyword.keymanagementserviceshort}} 以與 IBM COS 搭配使用：

1. 開啟 {{site.data.keyword.cloud_notm}} 儀表板。
2. 從功能表列中，按一下**管理** &gt; **帳戶** &gt; **使用者**。
3. 在側邊導覽中，按一下 **Identity & Access** &gt; **授權**。
4. 按一下**建立授權**。
5. 在**來源服務**功能表中，選取 **Cloud Object Storage**。
6. 在**來源服務實例**功能表中，選取要授權的服務實例。
7. 在**目標服務**功能表中，選取 **{{site.data.keyword.keymanagementservicelong_notm}}**。
8. 在**目標服務實例**功能表中，選取要授權的服務實例。
9. 啟用**讀者**角色。
10. 按一下**授權**。

### 建立儲存區
{: #encryption-createbucket}

如果您的金鑰存在於 {{site.data.keyword.keymanagementserviceshort}}，而且您已授權 Key Protect 服務與 IBM COS 搭配使用，則請建立金鑰與新儲存區的關聯：

1. 導覽至您的 {{site.data.keyword.cos_short}} 實例。
2. 按一下**建立儲存區**。
3. 輸入儲存區名稱，選取**地區**備援，然後選擇位置及儲存空間類別。
4. 在「進階配置」中，啟用**新增 Key Protect 金鑰**。
5. 選取關聯的 Key Protect 服務實例、金鑰及金鑰 ID。
6. 按一下**建立**。

在**儲存區及物件**清單中，儲存區現在會在**進階**下方有一個鑰匙圖示，指出儲存區已啟用 Key Protect 金鑰。若要檢視金鑰詳細資料，請按一下儲存區右側的功能表，然後按一下**檢視 Key Protect 金鑰**。

請注意，針對使用 SSE-KP 加密的物件所傳回的 `Etag` 值，**將**會是原始未加密物件的實際 MD5 雜湊。
{:tip}


## 替換金鑰
{: #encryption-rotate}

金鑰替換是降低資料外洩風險的重要部分。如果金鑰遺失或遭洩漏，則定期變更金鑰可降低潛在的資料流失。金鑰替換的頻率會依組織而不同，取決於若干變數（包括環境、已加密資料量、資料分類及規範法律）。[國家標準與技術機構 (NIST)](https://www.nist.gov/topics/cryptography){:new_window} 提供適當金鑰長度的定義，並提供金鑰應該使用多久時間的準則。

### 手動金鑰替換
{: #encryption-rotate-manual}

若要替換 {{site.data.keyword.cos_short}} 的金鑰，您需要使用新的「根金鑰」來建立已啟用 Key Protect 的新儲存區，並將現有儲存區中的內容複製到新的儲存區。

**附註**：從系統中刪除金鑰將會清除其內容，以及任何仍使用該金鑰加密的資料。移除之後，即無法復原或回復，並會導致永久資料流失。

1. 在 [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial) 服務中建立或新增「根金鑰」。
2. [建立新的儲存區](#encryption-createbucket)，並新增「根金鑰」。
3. 將原始儲存區中的所有物件都複製到新儲存區。
    1. 此步驟可以使用一些不同的方法來達成：
        1. 從指令行中，使用 [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) 或 [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)
        2. 使用 (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object]
        3. 搭配使用 SDK 與 [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 或 [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go)
