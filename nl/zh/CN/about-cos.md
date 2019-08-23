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


# 关于 {{site.data.keyword.cos_full_notm}}
{: #about-ibm-cloud-object-storage}

使用 {{site.data.keyword.cos_full}} 存储的信息经过加密并分散在多个地理位置，可使用 REST API 通过 HTTP 进行访问。此服务使用的是 {{site.data.keyword.cos_full_notm}} 系统（原先称为 Cleversafe）提供的分布式存储技术。

{{site.data.keyword.cos_full_notm}} 提供了三种类型的弹性：跨区域、区域和单个数据中心。“跨区域”提供的耐久性和可用性高于使用单个区域，但代价是等待时间略长，目前在美国、欧盟和亚太地区可用。“区域”服务与“跨区域”相反，它在单个区域内的多个可用性专区中分布对象，耐久性和可用性不如“跨区域”，但等待时间比“跨区域”短，目前在美国、欧盟和亚太地区区域可用。如果给定区域或可用性专区不可用，对象存储会继续正常运行，不受任何影响。“单个数据中心”在同一物理位置中的多个机器上分布对象。请在[此处](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)查看可用的区域。

开发者使用 {{site.data.keyword.cos_full_notm}} API 与其对象存储器进行交互。本文档支持从供应帐户，创建存储区，上传对象以及使用公共 API 交互参考[入门](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)。

## 其他 IBM 对象存储器服务
{: #about-other-cos}
除了 {{site.data.keyword.cos_full_notm}} 之外，{{site.data.keyword.cloud_notm}} 目前还针对不同的用户需求额外提供了多个对象存储器产品，所有这些产品都可通过基于 Web 的门户网站和 REST API 进行访问。[了解更多](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)。
