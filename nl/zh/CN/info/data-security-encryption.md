---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# 数据安全性和加密
{: #security}

{{site.data.keyword.cos_full}} 使用一种创新的方法，在确保安全性、可用性和可靠性的同时，以高性价比存储大量非结构化数据。这是使用信息分散算法 (IDA) 将数据分成不可识别的“切片”来实现的，这些“切片”分布在数据中心的网络中，使数据传输和存储具备固有的私密性和安全性。没有完整的数据副本存在于任何一个存储节点中，只需要节点的子集可用，便可完全检索网络上的数据。

{{site.data.keyword.cos_full_notm}} 中的所有数据都是静态加密的。此技术使用为每个对象生成的密钥分别对每个对象进行加密。这些密钥使用相同的信息分散算法（使用全有或全无变换 (AONT) 来保护对象数据）来实现安全、可靠的存储，这将防止因个别节点或硬盘驱动器泄露而导致密钥数据泄露。

如果用户需要控制加密密钥，那么可以[使用 SSE-C 对每个对象](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c)提供根密钥，也可以[使用 SSE-KP 对每个存储区](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp)提供根密钥。

存储器可以通过 HTTPS 进行访问，并且在内部，存储设备通过 TLS 进行认证并相互通信。


## 数据删除
{: #security-deletion}

删除数据后，存在各种阻止恢复或重构已删除对象的机制。删除对象会经历多个阶段，从将指示对象的元数据标记为已删除、除去内容区域、最终擦除驱动器本身，一直到最终覆盖表示该切片数据的块。根据是有人泄露了数据中心还是获得了物理磁盘，对象变为不可恢复的时间取决于删除操作的阶段。更新元数据对象时，来自数据中心网络外部的客户机无法再读取该对象。当表示内容区域的大部分切片已由存储设备最终完成时，即无法访问该对象。

## 租户隔离
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} 是一个共享基础架构的多租户对象存储解决方案。如果工作负载需要专用或隔离的存储器，请访问 [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) 以获取更多信息。
