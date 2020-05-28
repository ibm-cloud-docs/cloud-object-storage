---

copyright:
  years: 2017, 2020
lastupdated: "2020-05-28"

keywords: about, object storage, overview

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
{: #about-cloud-object-storage}

Information stored with {{site.data.keyword.cos_full}} is encrypted and dispersed across multiple geographic locations, and accessed over popular protocols like HTTPS using a modern RESTful API. 
{: shortdesc}

This service makes use of the distributed storage technologies provided by the {{site.data.keyword.cos_full_notm}} System (formerly Cleversafe).

{{site.data.keyword.cos_full_notm}} is available with three types of resiliency: Cross Region, Regional, and Single Data Center.  Cross Region provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US, EU and AP. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US, EU and AP regions. If a given region or availability zone is unavailable, the object store continues to function without impediment.  Single Data Center distributes objects across multiple machines within the same physical location. Check [here](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) for available regions.

Developers use an {{site.data.keyword.cos_full_notm}} API to interact with their object storage. Of course, the [console](https://cloud.ibm.com/){: external} provides a user interface for many operations as well. 

## Next Steps
{: #about-cloud-object-storage-next-steps}

Documentation on the best way to [get started](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started) provides support to provision accounts, to create buckets, to upload objects, and to use a reference of common operations through API interactions.


