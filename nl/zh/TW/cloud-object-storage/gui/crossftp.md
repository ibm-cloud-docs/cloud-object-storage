---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# 使用 CrossFTP 傳送檔案
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} 是支援 S3 相容雲端儲存空間解決方案（包括 {{site.data.keyword.cos_full}}）的全功能 FTP 用戶端。CrossFTP 支援 Mac OS X、Microsoft Windows、Linux，提供免費、專業及企業版本，且具有以下這類特性：

* 標籤式介面
* 密碼加密
* 搜尋
* 批次傳送
* 加密（*專業/企業版本*）
* 同步化（*專業/企業版本*）
* 排程器（*專業/企業版本*）
* 指令行介面（*專業/企業版本*）

## 連接至 IBM Cloud Object Storage
{: #crossftp-connect}

1. 下載、安裝及啟動 CrossFTP。
2. 在右窗格中，按一下加號 (+) 圖示來開啟「網站管理程式」，以建立新的「網站」。
3. 在*一般* 標籤下，輸入下列項目：
    * 將**通訊協定**設為 `S3/HTTPS`
    * 將**標籤**設為您選擇的敘述性名稱
    * 將**主機**設為 {{site.data.keyword.cos_short}} 端點（亦即 `s3.us.cloud-object-storage.appdomain.cloud`）
        * *確定端點地區符合想要的目標儲存區。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。*
    * 將**埠**保留為 `443`
    * 將**存取金鑰**及**密碼**設為具有適當目標儲存區存取權的 HMAC 認證
4. 在 *S3* 標籤下
    * 確定未勾選`使用 DevPay`
    * 按一下 **API 集...**，並確定未勾選 `Dev Pay` 及 `CloudFront Distribution`
5. ***僅適用於 Mac OS X***
    * 按一下功能表列中的*安全 > TLS/SSL 通訊協定...*
    * 選取`自訂已啟用的通訊協定`選項
    * 將 `TLSv1.2` 新增至**已啟用**方框
    * 按一下**確定**
6. ***僅適用於 Linux***
    * 按一下功能表列中的*安全 > 密碼設定...*
    * 選取`自訂已啟用的密碼組合`選項
    * 將 `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` 新增至**已啟用**方框
    * 按一下**確定**
7. 按一下**套用**，然後按一下**關閉**
8. *網站* 下的新項目應該具有步驟 3 中所提供的*標籤*
9. 按兩下新項目，以連接至端點

從這裡，視窗會顯示可用儲存區的清單，而您可以瀏覽可用的檔案，並將它們傳送至本端磁碟，以及從本端磁碟傳送。
