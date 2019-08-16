---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: faq, questions

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

# 常见问题
{: #faq}

## API 问题
{: #faq-api}

**{{site.data.keyword.cos_full}} 存储区名称区分大小写吗？**

存储区名称必须是 DNS 可寻址的名称，因此不区分大小写。

**对象名称中最多可以使用多少个字符？**

1024

**如何使用 API 来了解存储区的总大小？**

无法使用单个请求来访存存储区的大小。您需要列出存储区的内容，然后对每个对象的大小求和。

**可以将数据从 AWS S3 迁移到 {{site.data.keyword.cos_full_notm}} 吗？**

可以，您可以使用现有工具对 {{site.data.keyword.cos_full_notm}} 中的数据执行读写操作。您需要配置 HMAC 凭证，以允许工具进行认证。目前，并非所有与 S3 兼容的工具都受支持。有关更多详细信息，请参阅[使用 HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)。


## 产品问题
{: #faq-offering}

**帐户的存储区数是否限制为 100 个？如果需要更多存储区，该怎么做？**

是的，目前存储区限制为 100 个。 通常，最好是利用前缀将存储区中的对象分组在一起，除非数据需要位于其他区域或存储类中。例如，要对患者记录进行分组，您将为每个患者使用一个前缀。如果这种解决方案不可行，请联系客户支持人员。

**如果要使用 {{site.data.keyword.cos_full_notm}} 保险库或冷保险库来存储数据，是否需要创建其他帐户？**

不需要，存储类（以及区域）是在存储区级别定义的。只需创建一个设置为所需存储类的新存储区即可。

**使用 API 创建存储区时，如何设置存储类？**

为该存储区的 `LocationConstraint` 配置变量分配了存储类（例如，`us-flex`）。这是因为 AWS S3 与 {{site.data.keyword.cos_full_notm}} 处理存储类的方式有重大差异。{{site.data.keyword.cos_short}} 在存储区级别设置存储类，而 AWS S3 会将存储类分配给单个对象。在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes)中可以参考 `LocationConstraint` 的有效供应代码的列表。

**可以更改存储区的存储类吗？例如，生产数据为“标准”时，如果数据使用不频繁，可以轻松将其切换到“保险库”以用于计费吗？**

目前更改存储类需要将数据从一个存储区手动移动或复制到使用所需存储类的另一个存储区。


## 性能问题
{: #faq-performance}

**{{site.data.keyword.cos_short}} 中的数据一致性会影响性能吗？**

任何分布式系统要实现一致性都要付出一定代价，但与具有多个同步副本的系统相比，{{site.data.keyword.cos_full_notm}} 分散存储系统的效率更高，开销更低。

**如果应用程序需要处理大对象，这不会影响性能吗？**

要优化性能，可以通过分块方式并行上传和下载对象。


## 加密问题
{: #faq-encryption}

**{{site.data.keyword.cos_short}} 提供静态和动态加密吗？**

是的。静态数据使用自动提供者端高级加密标准 (AES) 256 位加密和安全散列算法 (SHA)-256 散列进行加密。动态数据使用内置运营商级传输层安全性/安全套接字层 (TLS/SSL) 或采用 AES 加密的 SNMPv3 进行保护。

**如果客户要对自己的数据进行加密，那么典型的加密开销是多少？**

对于客户数据，服务器端加密永远可用。与 S3 认证中所需的散列以及擦除编码相比，加密在 COS 处理成本中所占的比重并不大。

**{{site.data.keyword.cos_short}} 会对所有数据加密吗？**

是的，{{site.data.keyword.cos_short}} 会加密所有数据。

**{{site.data.keyword.cos_short}} 的加密算法是否符合 FIPS 140-2？**

符合，IBM COS 联邦产品已批准用于 FedRAMP 中等安全控制（需要经过验证的 FIPS 配置）。IBM COS 联邦产品通过了 FIPS 140-2 一级认证。有关 COS 联邦产品的更多信息，请通过我们的联邦站点[联系我们](https://www.ibm.com/cloud/government)。

**是否支持客户机密钥加密？**

是的，客户机密钥加密使用 SSE-C 或 Key Protect 进行支持。

## 一般问题
{: #faq-general}

**单个存储区中可以容纳多少个对象？**

单个存储区中的对象数没有实际限制。

**存储区可以相互嵌套吗？**

不能，存储区不能嵌套。如果存储区中需要进行更高级别的组织，那么支持使用前缀：`{endpoint}/{bucket-name}/{object-prefix}/{object-name}`。请注意，对象的键仍然是组合 `{object-prefix}/{object-name}`。

**“A 类”和“B 类”请求有什么区别？**

“A 类”请求是涉及修改或列示的操作。这包括创建存储区，上传或复制对象，创建或更改配置，列出存储区以及列出存储区的内容。“B 类”请求是与在系统中检索对象或其关联的元数据/配置相关的请求。从系统中删除存储区或对象是免费的。

**使用 Object Storage 构造数据，以便可以“查看”数据并查找目标内容的最佳方式是什么？如果没有目录结构，在一个级别就有上千个文件，似乎很难进行查看。**

您可以使用与每个对象关联的元数据来查找目标对象。Object Storage 的最大优点是与每个对象关联的元数据。在 {{site.data.keyword.cos_short}} 中，每个对象最多可以有 4 MB 的元数据。元数据卸载到数据库后，提供了卓越的搜索能力。在 4 MB 容量内，可以存储大量（键/值）对。此外，还可以使用“前缀”搜索来查找目标内容。例如，如果使用存储区来分隔各个客户数据，那么可以在存储区中使用前缀以进行组织。例如：/bucket1/folder/object，其中“folder/”是前缀。

**可以确认 {{site.data.keyword.cos_short}} 是“立即一致”，而不是“最终一致”吗？**

{{site.data.keyword.cos_short}} 对于数据是“立即一致”，对于使用量计费是“最终一致”。


**{{site.data.keyword.cos_short}} 可以自动对数据分区（例如，HDFS），以便我可以使用 Spark 等工具并行读取分区吗？**

{{site.data.keyword.cos_short}} 支持对对象执行限定范围的 GET 操作，因此应用程序可以执行分布式条带分割读取类型操作。条带分割操作将在要管理的应用程序上执行。
