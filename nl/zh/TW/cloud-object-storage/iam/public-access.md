---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# 容許公用存取
{: #iam-public-access}

有時，資料是要共用的。儲存區可能會針對 Web 應用程式及 Content Delivery Network 所使用的學術及私人研究或映像檔儲存庫，保留開放式資料集。讓您可以使用**公用存取**群組來存取這些儲存區。
{: shortdesc}

## 使用主控台來設定公用存取
{: #iam-public-access-console}

首先，請確定您有一個儲存區。否則，請遵循[入門指導教學](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)來熟悉主控台。

### 啟用公用存取
{: #public-access-console-enable}

1. 從 {{site.data.keyword.cloud_notm}} [主控台儀表板](https://cloud.ibm.com/)中，選取**儲存空間**以檢視資源清單。
2. 接下來，從**儲存空間**功能表中選取具有儲存區的服務實例。這會將您帶往「{site.data.keyword.cos_short}} 主控台」。
3. 選擇您要公開存取的儲存區。請記住，此原則讓_儲存區中的所有物件_ 可供具有適當 URL 的所有人員下載。
4. 從導覽功能表中，選取**存取原則**。
5. 選取**公用存取**標籤。
6. 按一下**建立存取原則**。在讀取警告之後，請選擇**啟用**。
7. 現在，此儲存區中的所有物件都可供公開存取！

### 停用公用存取
{: #public-access-console-disable}

1. 從 {{site.data.keyword.cloud_notm}} [主控台](https://cloud.ibm.com/)的任何位置，選取**管理**功能表，以及**存取 (IAM)**。
2. 從導覽功能表中，選取**存取群組**。
3. 選取**公用存取**，以查看目前使用中的所有公用存取原則清單。
4. 尋找對應於您要回到強制存取控制的儲存區的原則。
5. 從原則項目最右邊的動作清單中，選擇**移除**。
6. 確認對話框，現在即會從儲存區中移除原則。

## 容許對個別物件的公用存取
{: #public-access-object}

若要讓物件可透過 REST API 公開存取，可以在要求中併入 `x-amz-acl: public-read` 標頭。設定此標頭會略過所有 [IAM 原則](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)檢查，並容許未經鑑別的 `HEAD` 及 `GET` 要求。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

此外，[HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)讓它能夠容許[使用預先簽署 URL 的暫時公用存取](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url)。

### 上傳公用物件
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### 容許對現有物件的公用存取
{: #public-access-object-existing}

使用沒有有效負載及 `x-amz-acl: public-read` 標頭的查詢參數 `?acl` 時，容許對物件的公用存取，而不需要改寫資料。

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### 再次將公用物件設為專用
{: #public-access-object-private}

使用沒有有效負載及空的 `x-amz-acl:` 標頭的查詢參數 `?acl` 時，會撤銷對物件的公用存取，而不需要改寫資料。

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## 靜態網站
{: #public-access-static-website}

雖然 {{site.data.keyword.cos_full_notm}} 不支援自動靜態網站管理，但可以手動配置 Web 伺服器，並使用它來提供儲存區中所管理的可公開存取內容。如需相關資訊，請參閱[本指導教學](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos)。
