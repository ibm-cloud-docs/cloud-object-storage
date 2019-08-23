---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# 上傳資料
{: #upload}

組織儲存區後，是時候新增一些物件了。取決於想要的儲存空間使用方式，有不同的方法可以讓資料進入系統。資料科學家有幾個用於分析的大型檔案、系統管理者需要讓資料庫備份與本端檔案保持同步，開發人員則是撰寫需要讀取及寫入數百萬個檔案的軟體。所有這些情境都由不同的資料汲取方法提供最佳服務。

## 使用主控台
{: #upload-console}

一般而言，使用 Web 型主控台不是使用 {{site.data.keyword.cos_short}} 的最常見方式。物件限制為 200 MB，而且檔名及索引鍵相同。可以同時上傳多個物件，如果瀏覽器容許多個執行緒，則會平行使用多個組件來上傳每個物件。[Aspera 高速傳送](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)支援較大的物件大小及改良的效能（視網路因素而定）。

## 使用相容工具
{: #upload-tool}

部分使用者想要使用獨立式公用程式來與其儲存空間互動。由於 Cloud Object Storage API 支援最常用的 S3 API 作業集，所以許多 S3 相容工具也可以使用 [HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)連接至 {{site.data.keyword.cos_short}}。

部分範例包括 [Cyberduck](https://cyberduck.io/) 或 [Transmit](https://panic.com/transmit/) 這類檔案瀏覽器、[Cloudberry](https://www.cloudberrylab.com/) 及 [Duplicati](https://www.duplicati.com/) 這類備份公用程式、[s3cmd](https://github.com/s3tools/s3cmd) 或 [Minio Client](https://github.com/minio/mc) 這類指令行公用程式等等。

## 使用 API
{: #upload-api}

Object Storage 的大部分程式化應用程式都使用 SDK（例如 [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 或 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)）或 [Cloud Object Storage API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。一般而言，會透過[多個組件](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)上傳物件，而且組件大小及組件數目是透過「傳送管理員」類別所配置。
