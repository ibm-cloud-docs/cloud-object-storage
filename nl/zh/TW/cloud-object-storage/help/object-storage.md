---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, basics

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

# 關於 Object Storage
{: #about-cos}

Object Storage 是一種現代儲存空間技術概念，以及區塊和檔案儲存空間中的邏輯進度。Object Storage 大約自 1990 年晚期開始使用，但在過去 10 年獲得市場接受並成功。

Object Storage 的創造克服了許多問題：

*  很難使用傳統區塊及檔案系統來管理極大量的資料，因為這些技術會由於資料管理硬體及軟體堆疊的各種層次限制而導致 Data Island。

*  管理大規模的名稱空間，會導致維護大型且複雜的階層，而存取資料時需要這些階層。傳統區塊及檔案儲存空間陣列上的巢狀結構限制進一步促成 Data Island 的形成。

*  提供存取安全時需要結合參與管理這些區域的技術、複雜的安全保護方法及重要人員。

Object Storage（也稱為物件型儲存空間 (OBS)）使用不同的方法來儲存及參照資料。物件資料儲存空間概念包括下列三個建構：

*  資料：這是需要持續性儲存空間的使用者及應用程式資料。它可以是文字、二進位格式、多媒體，或是任何其他人員或機器產生的內容。

*  meta 資料：這是資料的相關資料。它包括一些預先定義的屬性，例如上傳時間和大小。Object Storage 容許使用者包括內含索引鍵及值配對中任何資訊的自訂 meta 資料。此資訊一般會包含與儲存資料之使用者或應用程式相關的資訊，而且隨時可以進行修正。在 Object Storage 系統中 meta 資料處理的唯一層面，是與物件一起儲存的 meta 資料。

*  索引鍵：會將唯一資源 ID 指派給 OBS 系統中的每個物件。此索引鍵容許 Object Storage 系統區分不同的物件，並用來尋找資料，而不需要知道資料所在的確切實體磁碟機、陣列或網站。

此方法容許 Object Storage 將資料儲存在簡單的平面階層中，這可緩和對大型效能繼承 meta 資料儲存庫的需求。

資料存取是透過 HTTP 通訊協定使用 REST 介面來達成，只需要參照物件索引鍵即可隨時隨地進行存取。
