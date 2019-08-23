---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# 開發人員專用
{: #gs-dev}
首先，請確定您已安裝 [{{site.data.keyword.cloud}} Platform CLI](https://cloud.ibm.com/docs/cli/index.html) 及 [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html)。

## 佈建 {{site.data.keyword.cos_full_notm}} 實例
{: #gs-dev-provision}

  1. 首先，請確定您具有 API 金鑰。請從 [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys) 取得此項目。
  2. 使用 CLI 登入 {{site.data.keyword.cloud_notm}} Platform。也可以將 API 金鑰儲存在檔案中，或將它設為環境變數。

```
ibmcloud login --apikey <value>
  ```
{:codeblock}

  3. 接下來，佈建 {{site.data.keyword.cos_full_notm}} 的實例，並指定實例的名稱、ID 及所需的方案（精簡或標準）。這可讓我們取得 CRN。如果您有已升級的帳戶，請指定`標準`方案。否則，請指定`精簡`。

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

[入門手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)會逐步解說建立儲存區和物件的基本步驟，以及邀請使用者與建立原則。如需基本 'curl' 指令的清單，請參閱[這裡](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)。

[在文件中](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli)，進一步瞭解如何使用 {{site.data.keyword.cloud_notm}} CLI 來建立應用程式、管理 Kubernetes 叢集，以及其他資訊。


## 使用 API
{: #gs-dev-api}

如需管理 {{site.data.keyword.cos_short}} 中所儲存的資料，您可以搭配使用 S3 API 相容工具（如 [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)）與 [HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)，以取得相容性。由於 IAM 記號相當容易使用，所以 `curl` 是基本測試以及與儲存空間互動的良好選擇。如需相關資訊，請參閱 [`curl` 參考資料](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)以及 [API 參考資料文件](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。

## 使用程式庫及 SDK
{: #gs-dev-sdk}
IBM COS SDK 可適用於 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) 及 [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)。這些分出版本的 AWS S3 SDK 已修改為支援 [IAM 記號型鑑別](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)，以及支援 [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption)。 

## 在 IBM Cloud 上建置應用程式
{: #gs-dev-apps}
{{site.data.keyword.cloud}} 可讓開發人員彈性選擇給定應用程式的正確架構及部署選項。在[虛擬機器](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group)中使用[無伺服器架構](https://cloud.ibm.com/openwhisk)，或在[容器](https://cloud.ibm.com/kubernetes/catalog/cluster)中使用 [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs)，以在[裸機](https://cloud.ibm.com/catalog/infrastructure/bare-metal)上執行程式碼。 

[Cloud Native Computing Foundation](https://www.cncf.io) 已包括且最近「已累進」[Kubernetes](https://kubernetes.io) 容器編排架構，而且形成 {{site.data.keyword.cloud}} Kubernetes 服務的基礎。想要在其 Kubernetes 應用程式中使用物件儲存空間作為持續性儲存空間的開發人員，可以在下列鏈結中進一步瞭解：

 * [選擇儲存空間解決方案](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [比較持續性儲存空間選項的表格](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [主要 COS 頁面](/docs/containers?topic=containers-object_storage)
 * [安裝 COS](/docs/containers?topic=containers-object_storage#install_cos)
 * [建立 COS 服務實例](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [建立 COS 密碼](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [決定配置](/docs/containers?topic=containers-object_storage#configure_cos)
 * [佈建 COS](/docs/containers?topic=containers-object_storage#add_cos)
 * [備份及還原資訊](/docs/containers?topic=containers-object_storage#backup_restore)
 * [儲存空間類別參照](/docs/containers?topic=containers-object_storage#storageclass_reference)


