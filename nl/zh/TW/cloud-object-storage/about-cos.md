---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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


# 關於 {{site.data.keyword.cos_full_notm}}
{: #about-ibm-cloud-object-storage}

使用 {{site.data.keyword.cos_full}} 儲存的資訊會加密並分散在多個地理位置，且使用 REST API 透過 HTTP 進行存取。此服務會使用 {{site.data.keyword.cos_full_notm}} System（先前稱為 Cleversafe）所提供的分散式儲存空間技術。

{{site.data.keyword.cos_full_notm}} 具有三種類型的備援：「跨地區」、「地區」及「單一資料中心」。「跨地區」提供的延續性及可用性高於使用單一地區，但延遲成本略高，而且現在可以在美國、歐盟及亞太地區使用。「地區」服務會反轉這些交易，並將物件配送至單一地區內的多個可用性區域，而且可以在美國、歐盟及亞太地區使用。如果給定地區或可用性區域無法使用，則物件儲存庫會在沒有阻礙的情況下繼續運作。「單一資料中心」會將物件配送至相同實體位置內的多部機器。請檢查[這裡](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)，以取得可用地區。

開發人員使用 {{site.data.keyword.cos_full_notm}} API 與其物件儲存空間互動。本文件支援[開始使用](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)佈建帳戶、建立儲存區、上傳物件，以及使用一般 API 互動的參照。

## 其他 IBM 物件儲存空間服務
{: #about-other-cos}
除了 {{site.data.keyword.cos_full_notm}} 之外，{{site.data.keyword.cloud_notm}} 目前還會針對不同的使用者需求提供數個其他物件儲存空間供應項目，而且所有這些項目都可以透過 Web 型入口網站及 REST API 進行存取。[進一步瞭解。](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
