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

# 面向开发者
{: #gs-dev}
首先，确保已安装 [{{site.data.keyword.cloud}} Platform CLI](https://cloud.ibm.com/docs/cli/index.html) 和 [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html)。

## 供应 {{site.data.keyword.cos_full_notm}} 的实例
{: #gs-dev-provision}

  1. 首先，确保您有 API 密钥。请从 [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys) 中获取 API 密钥。
  2. 使用 CLI 登录到 {{site.data.keyword.cloud_notm}} Platform。还可以将 API 密钥存储在文件中，或将其设置为环境变量。

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. 接下来，供应 {{site.data.keyword.cos_full_notm}} 的实例；请指定实例名称、标识和所需套餐（轻量或标准）。这将生成 CRN。如果您拥有的是升级的帐户，请指定`标准`套餐。 否则，请指定`轻量`。

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

[入门指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)将全程指导您完成创建存储区和对象，以及邀请用户和创建策略的基本步骤。在[此处](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)可以找到基本“curl”命令的列表。

[在此文档中](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli)了解有关使用 {{site.data.keyword.cloud_notm}} CLI 来创建应用程序和管理 Kubernetes 集群等的更多信息。


## 使用 API
{: #gs-dev-api}

要管理存储在 {{site.data.keyword.cos_short}} 中的数据，可以使用兼容 S3 API 的工具（如使用 [HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)的 [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)），以支持兼容性。由于 IAM 令牌使用起来相对容易，因此将 `curl` 用于对存储器进行基本测试和交互是不错的选择。在 [`curl` 参考](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)以及 [API 参考文档](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)中可以找到更多相关信息。

## 使用库和 SDK
{: #gs-dev-sdk}
有可用于 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) 和 [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 的 IBM COS SDK。这些是 AWS S3 SDK 的派生版本，已修改为支持[基于 IAM 令牌的认证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)，并且支持 [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption)。 

## 在 IBM Cloud 上构建应用程序
{: #gs-dev-apps}
通过 {{site.data.keyword.cloud}}，开发者可以灵活地为给定应用程序选择正确的体系结构和部署选项。您可在[裸机](https://cloud.ibm.com/catalog/infrastructure/bare-metal)上、在[虚拟机](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group)中、使用[无服务器框架](https://cloud.ibm.com/openwhisk)、[容器](https://cloud.ibm.com/kubernetes/catalog/cluster)或使用 [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs) 来运行代码。 

[Cloud Native Computing Foundation](https://www.cncf.io) 孵化了 [Kubernetes](https://kubernetes.io) 容器编排框架，并在最近对其进行了“提升”，此框架构成了 {{site.data.keyword.cloud}} Kubernetes Service 的基础。希望在 Kubernetes 应用程序中使用对象存储器进行持久存储的开发者可以在以下链接中了解更多信息：

 * [选择存储解决方案](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [持久性存储器选项的比较表](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [COS 主页](/docs/containers?topic=containers-object_storage)
 * [安装 COS](/docs/containers?topic=containers-object_storage#install_cos)
 * [创建 COS 服务实例](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [创建 COS 私钥](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [决定配置](/docs/containers?topic=containers-object_storage#configure_cos)
 * [供应 COS](/docs/containers?topic=containers-object_storage#add_cos)
 * [备份和复原信息](/docs/containers?topic=containers-object_storage#backup_restore)
 * [存储类参考](/docs/containers?topic=containers-object_storage#storageclass_reference)


