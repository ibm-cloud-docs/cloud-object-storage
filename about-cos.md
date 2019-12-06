---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: object storage, information, about

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# About {{site.data.keyword.cos_full_notm}}

Information stored with {{site.data.keyword.cos_full}} is encrypted and dispersed across multiple geographic locations, and accessed over popular protocols like HTTP using a modern RESTful API. 
{: shortdesc}

This service makes use of the distributed storage technologies provided by the {{site.data.keyword.cos_full_notm}} System (formerly Cleversafe).

{{site.data.keyword.cos_full_notm}} is available with three types of resiliency: Cross Region, Regional, and Single Data Center.  Cross Region provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US, EU and AP. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US, EU and AP regions. If a given region or availability zone is unavailable, the object store continues to function without impediment.  Single Data Center distributes objects across multiple machines within the same physical location. Check [here](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) for available regions.

Developers use an {{site.data.keyword.cos_full_notm}} API to interact with their object storage. This documentation provides support to [get started](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started) with provisioning accounts, to create buckets, to upload objects, and to use a reference of common API interactions.


