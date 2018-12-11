---

copyright:
  years: 2017
lastupdated: "2018-02-16"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# About {{site.data.keyword.cos_full_notm}}

Information stored with {{site.data.keyword.cos_full}} is encrypted and dispersed across multiple geographic locations, and accessed over HTTP using a REST API. This service makes use of the distributed storage technologies provided by the {{site.data.keyword.cos_full_notm}} System (formerly Cleversafe).

{{site.data.keyword.cos_full_notm}} is available with three types of resiliency: Cross Region, Regional, and Single Data Center.  Cross Region provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US, EU and AP. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US, EU and AP regions. If a given region or availability zone is unavailable, the object store continues to function without impediment.  Single Data Center distributes objects across multiple machines within the same physical location. Check [here](/docs/services/cloud-object-storage/basics/endpoints.html#select-regions-and-endpoints) for available regions.

Developers use an {{site.data.keyword.cos_full_notm}} API to interact with their object storage. This documentation provides support to [get started](/docs/services/cloud-object-storage/getting-started.html) with provisioning accounts, to create buckets, to upload objects, and to use a reference of common API interactions.




## Other IBM object storage services

In addition to {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} currently provides several additional object storage offerings for different user needs, all of which are accessible through web-based portals and REST APIs.  [Learn more.](/docs/services/ibm-cos/index.html)
