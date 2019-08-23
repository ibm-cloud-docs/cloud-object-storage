---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# 开发者指南
{: #dev-guide}

## 调整密码设置
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} 支持各种密码设置用于加密传输中的数据。并非所有密码设置都能实现相同级别的性能。通过协调 `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`、`TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`、`TLS_RSA_WITH_AES_256_CBC_SHA` 和 `TLS_RSA_WITH_AES_128_CBC_SHA` 中的一个设置，能够生成相同级别的性能，因为客户机与 {{site.data.keyword.cos_full_notm}} 系统之间没有 TLS。

## 使用分块上传
{: #dev-guide-multipart}

使用较大的对象时，建议使用分块上传操作将对象写入 {{site.data.keyword.cos_full_notm}}。可以将单个对象的上传作为一组分块来执行，这些分块可以按任意顺序单独上传，也可以并行上传。上传完成后，{{site.data.keyword.cos_short}} 会将所有分块显示为单个对象。此方法有诸多优点：网络中断不会导致大型上传操作失败，在一段时间内上传可以暂停并重新启动，并且可以在对象创建期间上传对象。

分块上传仅可用于大于 5 MB 的对象。对于小于 50 GB 的对象，建议的分块大小为 20 MB 到 100 MB，以实现最佳性能。对于更大的对象，可以增大分块大小，而不会对性能产生重大影响。

使用 500 个以上的分块会导致 {{site.data.keyword.cos_short}} 中的效率低下，因此应该尽可能予以避免。


由于涉及到额外的复杂性，因此建议开发者使用提供分块上传支持的 S3 API 库。

未完成的分块上传会一直持久存储，直到删除对象或使用 `AbortIncompleteMultipartUpload` 中止分块上传。如果未中止未完成的分块上传，那么分块上传会继续使用资源。设计界面时应该谨记这一点，应清除未完成的分块上传。


## 使用软件开发包
{: #dev-guide-sdks}

不必使用发布的 S3 API SDK；定制软件可以利用 API 直接与 {{site.data.keyword.cos_short}} 相集成。但是，使用发布的 S3 API 库具有多种优点，例如认证/生成签名、在发生 `5xx` 错误时自动执行重试逻辑，以及生成预签名 URL。在编写直接使用 API 来处理瞬态错误的软件时，必须十分谨慎；例如，在收到 `503` 错误时，提供使用指数退避的重试。

## 分页
{: #dev-guide-pagination}

处理存储区中的大量对象时，Web 应用程序可能会开始出现性能下降问题。为此，许多应用程序采用一种名为**分页**的方法，这是*将大型记录集划分成独立页面的过程*。几乎所有开发平台都提供有可通过内置功能或第三方库实现分页的对象或方法。

{{site.data.keyword.cos_short}} SDK 用于支持分页的方法是列出指定存储区中的对象。此方法提供了若干参数，在尝试划分大型结果集时非常有用。

### 基本用法
{: #dev-guide-pagination-basics}
对象列示方法背后的基本概念涉及设置要在响应中返回的最大键数 (`MaxKeys`)。响应还包含用于指示是否有更多结果可用的 `boolean` 值 (`IsTruncated`) 和名为 `NextContinuationToken` 的 `string` 值。在后续请求中设置延续令牌会返回下一批对象，直到没有更多结果可用为止。

#### 公共参数
{: #dev-guide-pagination-params}

|参数|描述|
|---|---|
|`ContinuationToken`|设置令牌以指定下一批记录|
|`MaxKeys`|设置要包含在响应中的最大键数|
|`Prefix`|将响应限制为只显示以指定前缀开头的键|
|`StartAfter`|基于键设置对象列表的开始位置|

### 使用 Java
{: #dev-guide-pagination-java}

{{site.data.keyword.cos_full}} SDK for Java 提供了 [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} 方法，支持返回所需大小的对象列表。[此处](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2)提供了完整的代码示例。

### 使用 Python
{: #dev-guide-pagination-python}

{{site.data.keyword.cos_full}} SDK for Python 提供了 [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} 方法，支持返回所需大小的对象列表。[此处](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2)提供了完整的代码示例。
