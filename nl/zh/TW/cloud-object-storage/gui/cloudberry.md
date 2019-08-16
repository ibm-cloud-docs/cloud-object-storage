---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup 是一項靈活的公用程式，容許使用者將部分或所有本端檔案系統備份至 S3 API 相容物件儲存空間系統。Windows、MacOS 及 Linux 都提供免費及專業版本，而且這些版本支援若干熱門雲端儲存空間服務，包括 {{site.data.keyword.cos_full}}。Cloudberry Backup 可以從 [cloudberrylab.com](https://www.cloudberrylab.com/) 進行下載。

Cloudberry Backup 包括許多有用的特性，包括：

* 排定
* 漸進式及區塊層次備份
* 指令行介面
* 電子郵件通知
* 壓縮（*僅限專業版本*）

## Cloudberry Explorer
{: #cloudberry-explorer}

Cloudberry Labs 的新產品為 {{site.data.keyword.cos_short}} 提供熟悉的檔案管理使用者介面。[Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} 也提供免費及專業版本，但目前僅適用於 Windows。主要特性包括：

* 資料夾/儲存區同步
* 指令行介面
* ACL 管理
* 容量報告

專業版本也包括：
* 搜尋 
* 加密/壓縮
* 可繼續上傳
* FTP/SFTP 支援

## 搭配使用 Cloudberry 與 Object Storage
{: #cloudberry-cos}

配置 Cloudberry 產品使用 {{site.data.keyword.cos_short}} 時需要記住的重點：

* 從選項清單中，選取 `S3 相容`
* 目前僅支援 [HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)
* 每個儲存區都需要個別連線
* 確定連線中所指定的`端點`符合所選取儲存區的地區（*備份會因為無法存取目的地而失敗*）。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
