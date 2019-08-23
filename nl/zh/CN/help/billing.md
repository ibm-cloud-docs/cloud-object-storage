---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# 帐单
{: #billing}

在 [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window} 上可以找到有关定价的信息。

## 发票
{: #billing-invoices}

在导航菜单的**管理** > **计费和使用情况**中查找您的帐户发票。

每个帐户会收到一张帐单。如果需要对不同组的容器单独计费，那么必须创建多个帐户。

## {{site.data.keyword.cos_full_notm}} 定价
{: #billing-pricing}

{{site.data.keyword.cos_full}} 的存储成本由存储的数据总量、使用的公共出站带宽量以及系统处理的操作请求总数确定。

基础架构产品连接到一个三层网络，分段处理公共流量、专用流量和管理流量。基础架构服务可以在专用网络上相互免费传输数据。基础架构产品（例如，裸机服务器、虚拟服务器和云存储器）可连接到公用网络上的 {{site.data.keyword.cloud_notm}} Platform 目录（例如，Watson 服务和 Cloud Foundry 运行时）中的其他应用程序和服务，因此这两种类型的产品之间进行的数据传输将按标准公用网络带宽费率进行计量和收费。
{: tip}

## 请求类
{: #billing-request-classes}

“A 类”请求涉及修改或列示操作。此类别包括创建存储区，上传或复制对象，创建或更改配置，列出存储区以及列出存储区的内容。

“B 类”请求与在系统中检索对象或者其关联的元数据或配置相关。

从系统中删除存储区或对象不会产生费用。

|类|请求|示例|
|--- |--- |--- |
|A 类|PUT、COPY 和 POST 请求，以及用于列出存储区和对象的 GET 请求|创建存储区，上传或复制对象，列出存储区，列出存储区的内容，设置 ACL 以及设置 CORS 配置|
|B 类|GET（不包括列示）、HEAD 和 OPTIONS 请求|检索对象和元数据 |

## Aspera 传输
{: #billing-aspera}

[Aspera 高速传输](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)将产生额外的输出费用。有关更多信息，请参阅[定价页面](https://www.ibm.com/cloud/object-storage#s3api)。

## 存储类
{: #billing-storage-classes}

并非存储的所有数据都需要频繁访问，某些归档数据可能很少被访问，甚至根本不会被访问。对于活动性较低的工作负载，可以创建使用不同存储类的存储区，对于这些存储区中存储的对象，收费安排与标准存储器不同。

有四种存储类：

*  **标准**用于活动工作负载，检索数据免费（除了操作请求本身的成本外）。
*  **保险库**用于凉工作负载，其中访问数据的频率少于每月一次 - 每次读取数据时，会产生额外的检索费用（美元/GB）。该服务包括符合服务预期用途的对象大小和存储时间段的最小阈值：存储不太活跃且活动性较低的数据。
*  **冷保险库**用于冷工作负载，其中访问数据的频率为每 90 天一次或更低 - 每次读取数据时，会产生更多额外检索费用（美元/GB）。该服务包括符合服务预期用途的对象大小和存储时间段的最小阈值：存储不活跃的非活动数据。
*  **Flex** 用于访问模式更难以预测的动态工作负载。根据使用情况，如果存储器成本加上检索费用超过上限值，那么会去掉检索费用，而改为收取新容量费用。如果对数据的访问不频繁，那么 Flex 存储器的性价比会高于“标准”存储器，并且如果访问使用模式意外变得更活跃，那么 Flex 存储器的性价比会高于“保险库”或“冷保险库”存储器。Flex 无需最小对象大小或存储时间段。

有关定价的更多信息，请参阅 [ibm.com 上的定价表](https://www.ibm.com/cloud/object-storage#s3api)。

有关如何创建使用不同存储类的存储区的更多信息，请参阅 [API 参考](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)。
