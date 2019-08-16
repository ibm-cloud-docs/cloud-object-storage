---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: administrator, storage, iam, access

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

# 面向管理员
{: #administrators}

对于需要配置对象存储器和管理数据访问权的存储器和系统管理员，可以利用 IBM Cloud Identity and Access Management (IAM) 来管理用户，创建和循环 API 密钥，以及向用户和服务授予角色。如果尚未学习[入门教程](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)，请转至入门教程并进行通读，以熟悉存储区、对象和用户的核心概念。

## 设置存储器
{: #administrators-setup}

首先，您需要至少有一个对象存储资源实例和用于存储数据的一些存储区。请从希望如何进一步对数据访问权分段、希望数据实际驻留在哪里以及数据访问频率方面来考虑这些存储区。

### 对访问权分段
{: #administrators-access}

可以在两个级别对访问权分段：资源实例级别和存储区级别。 

您可能希望确保开发团队只能访问其使用的对象存储器的实例，而不能访问其他团队使用的对象存储器实例。或者，您希望确保只有团队创建的软件才能实际编辑存储的数据，因此您希望开发者对云平台具有数据只读访问权，以进行故障诊断。下面是服务级别策略的示例。

现在，如果开发团队或任何单个用户具有对存储实例的查看者访问权，但应该能够直接编辑一个或多个存储区中的数据，那么可以使用存储区级别策略来提升授予帐户中用户的访问级别。例如，用户可能无法创建新存储区，但可以在现有存储区中创建和删除对象。

## 管理访问权
{: #administrators-manage-access}

IAM 基于的基本概念是：授予_主体_对_资源_的_角色_。

有两种基本类型的主体：_用户_和_服务标识_。

还有一个概念：_服务凭证_。服务凭证是连接到 {{site.data.keyword.cos_full}} 实例所需的重要信息集合。这至少包含 {{site.data.keyword.cos_full_notm}} 实例的标识（即资源实例标识）、服务/认证端点，以及将主体与 API 密钥相关联的方法（即服务标识）。创建服务凭证时，可以选择将其与现有服务标识相关联，也可以选择创建新的服务标识。

因此，如果要允许开发团队使用控制台来查看对象存储实例和 Kubernetes 集群，开发团队需要对对象存储资源的`查看者`角色以及对容器服务的`管理员`角色。请注意，`查看者`角色仅支持用户查看实例是否存在、查看现有凭证，但**不能**查看存储区和对象。创建服务凭证时，这些凭证会与服务标识相关联。此服务标识需要具有对实例的`管理者`或`写入者`角色，才能创建和销毁存储区和对象。

有关 IAM 角色和许可权的更多信息，请参阅 [IAM 概述](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)。
