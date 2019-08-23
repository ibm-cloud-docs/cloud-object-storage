---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# 資料安全及加密
{: #security}

{{site.data.keyword.cos_full}} 使用一種創新的方式，以符合成本效益的方法儲存大量的非結構化資料，同時確保安全、可用性和可靠性。此作業的完成方式是使用 Information Dispersal Algorithms (IDAs) 來將資料區隔為配送到資料中心網路的無法辨識「截塊」，讓資料的傳輸及儲存具有固有的專用和安全。任何單一儲存空間節點中都沒有完整的資料副本，而且只需要一部分的節點可供使用，才能完全擷取網路上的資料。

{{site.data.keyword.cos_full_notm}} 中的所有資料都是靜態加密。這種技術會使用依物件所產生的金鑰，個別地加密每個物件。這些金鑰是使用相同的 Information Dispersal Algorithms 來保護及可靠地儲存，而此演算法使用「全部或根本不轉換 (AONT)」來保護物件資料，這可防止在個別節點或硬碟受損時揭露重要資料。

如果使用者需要控制加密金鑰，則可以[使用 SSE-C 依物件](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c)或[使用 SSE-KP 依儲存區](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp)來提供根金鑰。

儲存空間可以透過 HTTPS 存取，而儲存裝置會在內部進行認證，並使用 TLS 彼此通訊。


## 資料刪除
{: #security-deletion}

刪除資料之後，有各種機制可以防止回復或重新建構已刪除的物件。刪除物件會經歷各種階段，從標示將物件指出為已刪除的 meta 資料、到移除內容地區，再到最後消除磁碟機本身，直到最後改寫代表該截塊資料的區塊為止。根據是否有人洩漏資料中心或已佔有實體磁碟，物件變成無法復原的時間取決於刪除作業的階段。更新 meta 資料物件時，資料中心網路外部的用戶端就無法再讀取物件。儲存裝置已完成代表內容地區的大部分截塊時，就無法存取該物件。

## 承租戶隔離
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} 是共用基礎架構的多方承租戶物件儲存空間解決方案。如果您的工作負載需要專用或隔離的儲存空間，請造訪 [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) 以取得相關資訊。
