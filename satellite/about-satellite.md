---

copyright:
  years: 2021
lastupdated: "2021-12-01"

keywords:  object storage, satellite, local

subcollection: cloud-object-storage


---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:table: .aria-labeledby="caption"}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# What is {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}?
{: #about-cos-satellite}

{{site.data.keyword.cos_full_notm}} for {{site.data.keyword.satellitelong_notm}} offers users the flexibility to run a managed {{site.data.keyword.cos_short}} service on client-owned on-premises infrastructure, edge locations or third-party public cloud infrastructure. 
{: shortdesc}

Essentially, provisioning an instance of {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} provides the same familiar interfaces of {{site.data.keyword.cos_full_notm}} outside of {{site.data.keyword.cloud_notm}}. 

## Typical use cases of {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}
{: #about-cos-satellite-benefits}

**Low latency workloads** that need to be run in close proximity to on-premises data and applications including workloads running on factory floors for automated operations in manufacturing, real-time patient diagnosis, and media streaming.

**Data residency requirements** or those in regulated industries that need to securely store and process customer data that needs to remain on-premises or in locations where there is no public Cloud Object Storage service.

**Edge or IOT applications** that collect and process data on the edge of network for new workloads from devices and users such as data collection and processing, location-based media, autonomous vehicle data, analytics, and machine data controls for manufacturing.

**Hybrid workloads** that require management of data between on-premises infrastructure, edge, public cloud or any multi-cloud installation.

## How {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} works
{: #about-cos-satellite-how}

![COS on Satellite Architecture](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/satellite-arch.png){: caption="Figure 1. Object Storage for Satellite Architecture"}

1. A {{site.data.keyword.satelliteshort}} administrator needs to configure a new "Location" using the {{site.data.keyword.satelliteshort}} console and assigns hosts for the {{site.data.keyword.satelliteshort}} Control Plane.  
2. After the new location is created and accessible, an {{site.data.keyword.cos_short}} administrator provisions the {{site.data.keyword.cos_short}} instance in the new location.
3. The {{site.data.keyword.satelliteshort}} administrator assigns the appropriate hosts and storage blocks to the new {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} cluster.
4. The new instance is available for both {{site.data.keyword.cos_short}} bucket configuration and data operations.

At this time, the {{site.data.keyword.satelliteshort}} service must be configured in the Washington, DC (`us-east`) region.
{:note}

## What features are currently supported?
{: #about-cos-satellite-supported}

* [{{site.data.keyword.cloud_notm}} IAM access policies](/docs/cloud-object-storage?topic=cloud-object-storage-iam)
* [Object Expiration](/docs/cloud-object-storage?topic=cloud-object-storage-expiry)
* [Object Versioning](/docs/cloud-object-storage?topic=cloud-object-storage-versioning)
* [Object Tagging](/docs/cloud-object-storage?topic=cloud-object-storage-object-tagging)
* [Static Web hosting](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-options)

## What features are not yet supported?
{: #about-cos-satellite-unsupported}

* Key Protect encryption and key lifecycle management
* HPCS encryption
* Firewall (IP rules, allowed network type rules)
* Activity Tracker
* Metrics Monitoring
* Security and Compliance Center 
* Cloud Functions
* Aspera High-Speed Transfer
* Immutable Object Storage
* Archive lifecycle rules
* Storage classes (billing tiers)




