---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: endpoints, legacy, access points, manual failover

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

# 其他端点信息
{: #advanced-endpoints}

存储区的弹性由用于创建该存储区的端点进行定义。_跨区域_弹性是在多个大城市区域中分布数据，_区域_弹性是在单个大城市区域中分布数据。_单个数据中心_弹性是在单个数据中心内的多个设备中分布数据。区域和跨区域存储区可以在站点中断期间保持可用性。

对于与区域 {{site.data.keyword.cos_short}} 端点并置的计算工作负载，可实现更短的等待时间和更高的性能。对于不集中在单个地理区域中的工作负载，跨区域 `geo` 端点会将连接路由到距离最近的区域数据中心。

使用跨区域端点时，可以将入站流量定向到特定访问点，同时仍在所有三个区域中分布数据。向单个访问点发送请求时，如果该区域变为不可用，那么不会自动执行故障转移。将流量定向到访问点（而不是 `geo` 端点）的应用程序**必须**在内部实施相应的故障转移逻辑，才能实现跨区域存储器的可用性优势。
{:tip}

某些工作负载可能在使用单数据中心端点时更具优势。存储在单个站点中的数据仍会在多个物理存储设备中分布，但限制于单个数据中心。这可以提高同一站点中计算资源的性能，但在站点中断时无法保持可用性。单个数据中心存储区在发生站点破坏时不会提供自动复制或备份，因此使用单个站点的任何应用程序都应该在其设计中考虑灾难恢复。

使用 IAM 时，所有请求都必须使用 SSL，并且服务将拒绝任何明文请求。

端点类型：

{{site.data.keyword.cloud}} 服务连接到一个三层网络，分段处理公共流量、专用流量和管理流量。

* **专用端点**可用于源自 Kubernetes 集群、裸机服务器、虚拟服务器和其他云存储服务的请求。专用端点能提供更好的性能，即使是跨区域或跨数据中心的流量，也不会产生任何出局或入局带宽费用。**最好尽可能使用专用端点。**
* **公共端点**可以接受来自任何位置的请求，并根据出局带宽评估费用。入局带宽免费。公共端点应该用于不是源自 {{site.data.keyword.cloud_notm}} 云计算资源的访问。 

请求必须发送到与给定存储区位置关联的端点。如果您不确定存储区的位置，可使用[存储区列示 API 的扩展](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended)，此扩展用于返回服务实例中所有存储区的位置和存储类信息。

从 2018 年 12 月开始，我们已更新了端点。旧端点将继续运行，直到发布进一步通知为止。请更新应用程序以使用[新端点](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints)。
{:note}
