---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, simpana

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


# 使用 CommVault Simpana 來保存資料
{: #commvault}

CommVault Simpana 整合 {{site.data.keyword.cos_full_notm}} 的「保存」層級。如需 Simpana 的相關資訊，請參閱：[CommVault Simpana 文件](https://documentation.commvault.com/commvault/)

如需 IBM COS Infrastructure Archive 的相關資訊，請參閱[如何：保存資料](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive)。

## 整合步驟
{: #commvault-integration}

1.	從 Simpana 主控台中，建立 Amazon S3 雲端儲存空間程式庫。 

2. 確定「服務主機」指向端點。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。Simpana 會在此步驟佈建儲存區，或者可以使用佈建的儲存區。 

3.	在儲存區上建立原則。您可以使用 AWS CLI、SDK 或 Web 主控台來建立原則。原則的範例如下：

```shell
{
  "Rules": [
    {
      "ID": "CommVault",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transitions": [
        {
        "Days": 0,
        "StorageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

### 建立原則與儲存區的關聯
{: #commvault-assign-policy}

1. 執行下列 CLI 指令：

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	使用 Simpana 建立儲存空間原則，並建立儲存空間原則與您在第一個步驟中建立的「雲端儲存空間」程式庫的關聯。儲存空間原則控管 Simpana 與 COS 進行備份傳送的互動方式。如需原則概觀，請參閱[這裡](https://documentation.commvault.com/commvault/v11/article?p=13804.htm)。

3.	建立備份集，並建立備份集與前一個步驟中建立的儲存空間原則的關聯。如需備份集概觀，請參閱[這裡](https://documentation.commvault.com/commvault/v11/article?p=11666.htm)

## 執行備份
{: #commvault-backup}

您可以使用原則來起始備份至儲存區，並執行備份至 {{site.data.keyword.cos_full_notm}}。如需 Simpana 備份的相關資訊，請參閱[這裡](https://documentation.commvault.com/commvault/v11/article?p=11677.htm)。備份內容會根據儲存區上所配置的原則轉移至「保存」層級。

## 執行還原
{: #commvault-restore}

您可以透過 {{site.data.keyword.cos_full_notm}} 還原備份內容。如需 Simpana 還原的相關資訊，請參閱[這裡](https://documentation.commvault.com/commvault/v11/article?p=12867.htm)。

### 配置 Simpana，以自動從「保存」層級還原物件
{: #commvault-auto-restore}

1. 建立作業，以在您從 COS 還原備份時觸發 {{site.data.keyword.cos_full_notm}} 還原。請參閱 [CommVault Simpana 文件](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)來進行配置。

2. 透過雲端儲存空間取消作業，將已備份的內容從「保存」層級還原至其原始層級。此作業會在 Simpana 接收來自 {{site.data.keyword.cos_full_notm}} 的回覆碼時執行。如需「保存」取消的相關資訊，請參閱[這裡](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)。

3. 在（從「保存」層級到其原始層級）還原完成之後，Simpana 即會讀取內容，並將其寫入至其原始或配置的位置。
