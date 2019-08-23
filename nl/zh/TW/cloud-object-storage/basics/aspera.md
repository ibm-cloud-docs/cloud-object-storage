---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# 使用 Aspera 高速傳輸
{: #aspera}

Aspera 高速傳輸克服傳統 FTP 及 HTTP 傳輸的限制，改善大部分情況下的資料傳送效能，尤其是網路遭遇高延遲及封包流失的情況。Aspera 高速傳送會使用 [FASP 通訊協定](https://asperasoft.com/technology/transport/fasp/)來上傳物件，而非標準 HTTP `PUT`。使用 Aspera 高速傳送來上傳及下載，提供了下列好處：

- 較快速的傳送速度
- 在主控台中傳送超過 200MB 的大型物件上傳，以及使用 SDK 或程式庫傳送 1GB
- 上傳任何資料類型的整個資料夾，包括多媒體檔案、磁碟映像檔，以及任何其他結構化或非結構化資料
- 自訂傳送速度及預設喜好設定
- 可以獨立檢視、暫停/繼續或取消傳送

您可以在 {{site.data.keyword.cloud_notm}} [主控台](#aspera-console)中使用 Aspera 高速傳送，也可以使用 [SDK](#aspera-sdk) 以程式設計方式來使用它。 

Aspera 高速傳送僅適用於特定地區。如需詳細資料，請參閱[整合式服務](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)。
{:tip}

## 使用主控台
{: #aspera-console}

當您在[支援的地區](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)中建立儲存區時，可以選擇選取 Aspera 高速傳送來上傳檔案或資料夾。嘗試上傳物件之後，系統會提示您安裝 Aspera Connect 用戶端。

### 安裝 Aspera Connect
{: #aspera-install}

1. 選取**安裝 Aspera Connect** 用戶端。
2. 遵循安裝指示（視作業系統及瀏覽器而定）。
3. 繼續檔案或資料夾上傳。

您也可以直接從 [Aspera 網站](https://downloads.asperasoft.com/connect2/)安裝 Aspera Connect 外掛程式。如需協助疑難排解 Aspera Connect 外掛程式問題，[請參閱文件](https://downloads.asperasoft.com/en/documentation/8)。

安裝外掛程式之後，您可以選擇將 Aspera 高速傳送設為使用相同瀏覽器之目標儲存區的所有上傳的預設值。選取**記住我的瀏覽器喜好設定**。儲存區配置頁面的**傳送選項**下也提供了選項。這些選項容許您在「標準」與「高速」進行選擇，作為上傳及下載的預設傳輸。

一般而言，使用 IBM Cloud Object Storage Web 型主控台，不是使用 {{site.data.keyword.cos_short}} 的最常見方式。「標準傳送」選項會將物件大小限制為 200MB，而且檔名及索引鍵將會相同。Aspera 高速傳送支援較大的物件大小及改良的效能（視網路因素而定）。

### 傳送狀態
{: #aspera-console-transfer-status}

**作用中：**起始傳送之後，傳送狀態即會顯示為作用中。傳送為作用中時，您可以暫停、繼續或取消作用中傳送。 

**已完成：**完成傳送時，此階段作業中這項傳送及所有傳送的相關資訊都會顯示在「已完成」標籤上。您可以清除此資訊。您只會看到現行階段作業中已完成傳送的相關資訊。

**喜好設定：**您可以將上傳及（或）下載的預設值設為「高速」。

使用 Aspera 高速傳送的下載會產生輸出費用。如需相關資訊，請參閱[定價頁面](https://www.ibm.com/cloud/object-storage)。
{:tip}

**進階喜好設定：**您可以設定上傳及下載的頻寬。

----

## 使用程式庫及 SDK
{: #aspera-sdk}

Aspera 高速傳送 SDK 提供在使用 Java 或 Python 時，於自訂應用程式內起始高速傳送的能力。

### 使用 Aspera 高速傳送的時機
{: #aspera-guidance}

Aspera 高速傳送所使用的 FASP 通訊協定並不適用於進出 COS 的所有資料傳送。具體而言，任何使用 Aspera 高速傳送的傳送都應該：

1. 一律使用多個階段作業 - 至少有兩個平行階段作業，最適合使用 Aspera 高速傳送功能。請參閱 [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) 及 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera) 的特定指引。
2. Aspera 高速傳送適用於較大型檔案，任何包含小於 1 GB 總資料量的檔案或目錄都應該改為使用標準「傳送管理員」類別來傳送多個組件中的物件。相較於一般 HTTP 傳送，Aspera 高速傳送需要較長時間來到達第一個位元組。實例化數個「Aspera 傳送管理員」物件來管理個別較小檔案的傳送，可能會導致與基本 HTTP 要求相關的效能低於標準，因此最好實例化單一用戶端，改為上傳較小檔案的目錄。
3. Aspera 高速傳送的設計部分是為了改善具有大量封包流失的網路環境效能，讓通訊協定在遠距離及公用廣域網路上的執行效能良好。Aspera 高速傳送不應該用於在某地區或資料中心內的傳送。

Aspera 高速傳送 SDK 是封閉來源，因此為使用 Apache 授權的 COS SDK 的選用相依關係。
{:tip}

#### COS/Aspera 高速傳送包裝
{: #aspera-packaging}

下圖顯示 COS SDK 如何與 Aspera 高速傳送程式庫互動以提供功能的高階概觀。

<img alt="COS/Aspera 高速傳送 SDK。" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="圖 1：COS/Aspera 高速傳送 SDK。" caption-side="bottom"} 

### 支援的平台
{: #aspera-sdk-platforms}

| OS | 版本   | 架構 | 已測試的 Java 版本 | 已測試的 Python 版本 |
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64 位元      | 6 及更新版本 | 2.7、3.6       |
| Mac OS X               | 10.13     | 64 位元      | 6 及更新版本 | 2.7、3.6       |
| Microsoft&reg; Windows |10 | 64 位元      | 6 及更新版本 | 2.7、3.6       |

每個 Aspera 高速傳送階段作業都會大量產生個別 `ascp` 處理程序，該處理程序在用戶端機器上執行，以執行傳送。請確定您的運算環境可容許執行此處理程序。
{:tip}

**其他限制**

* 不支援 32 位元二進位檔
* Windows 支援需要 Windows 10
* Linux 支援受限於 Ubuntu（已針對 18.04 LTS 進行測試）
* 必須使用 IAM API 金鑰來建立「Aspera 傳送管理員」用戶端，而非 HMAC 認證。

### 使用 Java 取得 SDK
{: #aspera-sdk-java} 
{: java}

使用 {{site.data.keyword.cos_full_notm}} 及 Aspera 高速傳送 Java SDK 的最佳方式是使用 Maven 來管理相依關係。如果您不熟悉 Maven，可以使用 [Maven（5 分鐘）](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window}手冊開始進行。
{: java}

Maven 使用名為 `pom.xml` 的檔案，來指定 Java 專案所需的程式庫（及其版本）。下面的範例 `pom.xml` 檔案說明如何使用 {{site.data.keyword.cos_full_notm}} 及 Aspera 高速傳送 Java SDK
{: java}

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera</artifactId>
            <version>0.1.163682</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}
{: java}

[使用 Aspera 高速傳送](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera)小節中提供使用 Java 起始 Aspera 高速傳送的範例。
{: java}

### 使用 Python 取得 SDK
{: #aspera-sdk-python} 
{: python}

Python Package Index (PyPI) 軟體儲存庫中提供 {{site.data.keyword.cos_full_notm}} 及 Aspera 高速傳送 Python SDK。
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

[使用 Aspera 高速傳送](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera)小節中提供使用 Python 起始 Aspera 傳送的範例。
{: python}
