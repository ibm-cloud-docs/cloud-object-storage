---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# 关于 {{site.data.keyword.cos_full_notm}} S3 API
{: #compatibility-api}

{{site.data.keyword.cos_full}} API 是基于 REST 的 API，用于读取和写入对象。它使用 {{site.data.keyword.iamlong}} 进行认证和授权，并且支持 S3 API 的子集，能将应用程序轻松迁移到 {{site.data.keyword.cloud_notm}}。

本参考文档仍在不断改进中。如果在应用程序中使用此 API 时遇到技术问题，请在 [Stack Overflow](https://stackoverflow.com/) 上发帖提问。请添加 `ibm-cloud-platform` 和 `object-storage` 标记，帮助我们根据您的反馈改进本文档。

由于 {{site.data.keyword.iamshort}} 令牌使用起来相对容易，因此将 `curl` 用于对存储器进行基本测试以及与存储器进行交互是不错的选择。在 [`curl` 参考](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)中可以找到更多相关信息。

以下各表描述了 {{site.data.keyword.cos_full_notm}} API 的完整操作集。有关更多信息，请参阅有关[存储区](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)或[对象](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)的 API 参考页面。


## 存储区操作
{: #compatibility-api-bucket}

这些操作可创建和删除存储区，获取有关存储区的信息以及控制存储区的行为。

|存储区操作|注释|
|:------------------------|:--------------------------------------------------------------------------------|
|`GET` Buckets|用于检索属于帐户的所有存储区的列表。|
|`DELETE` Bucket|删除空存储区。|
|`DELETE` Bucket CORS|删除存储区上设置的任何 CORS（跨源资源共享）配置。|
|`GET` Bucket|列出存储区中的对象。限制为一次可列出 1,000 个对象。|
|`GET` Bucket CORS|检索存储区上设置的任何 CORS 配置。|
|`HEAD` Bucket|检索存储区的头。|
|`GET` Multipart Uploads|列出未完成或已取消的分块上传。|
|`PUT` Bucket|存储区具有命名限制。帐户限制为只能有 100 个存储区。|
|`PUT` Bucket CORS|为存储区创建 CORS 配置。|


## 对象操作
{: #compatibility-api-object}

这些操作可创建和删除对象，获取有关对象的信息以及控制对象的行为。

|对象操作|注释|
|:--------------------------|:------------------------------------------------------------------------------------|
|`DELETE` Object|从存储区中删除一个对象。|
|`DELETE` Batch|通过一个操作从存储区中删除多个对象。|
|`GET` Object|在存储区中检索对象。|
|`HEAD` Object|检索对象的头。|
|`OPTIONS` Object|检查 CORS 配置以确定是否可以发送特定请求。|
|`PUT` Object|将对象添加到存储区。|
|`PUT` Object (Copy)|创建对象的副本。|
|Begin Multipart Upload|为要上传的一组分块创建一个上传标识。|
|Upload Part|上传与上传标识关联的对象的分块。|
|Upload Part (Copy)|上传与上传标识关联的现有对象的分块。|
|Complete Multipart Upload|将与一个上传标识关联的分块组合成对象。|
|Cancel Multipart Upload|取消上传并删除与上传标识关联的待处理分块。|
|List Parts|返回与上传标识关联的分块的列表。|


另外一些操作（例如，标记和版本控制）在 {{site.data.keyword.cos_short}} 的私有云实现中受支持，但目前在公共云或专用云中不受支持。在 [ibm.com](https://www.ibm.com/cloud/object-storage) 上可以找到有关定制对象存储解决方案的更多信息。
