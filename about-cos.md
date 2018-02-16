---

copyright:
  years: 2017
lastupdated: "2017-10-31"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# About {{site.data.keyword.cos_full_notm}}

Information stored with {{site.data.keyword.cos_full}} is encrypted and dispersed across multiple geographic locations, and accessed over HTTP using a REST API. This service makes use of the distributed storage technologies provided by the {{site.data.keyword.cos_full_notm}} System (formerly Cleversafe).

{{site.data.keyword.cos_full_notm}} is available with three types of resiliency: Cross Region, Regional, and Single Data Center.  Cross Region provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US and EU. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US South and US East regions. If a given region or availability zone is unavailable, the object store continues to function without impediment.  Single Data Center distributes objects across multiple machines within the same physical location.

Developers use an {{site.data.keyword.cos_full_notm}} API to interact with their object storage. This documentation provides support to [get started](/docs/cloud-object-storage/index.html#getting-started-console-) with provisioning accounts, to create buckets, to upload objects, and to use a reference of common API interactions.




## Other IBM object storage services

In addition to {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} currently provides two additional object storage offerings for different user needs, all of which are accessible through web-based portals and REST APIs.

| Offering                                   | Interface | Defining advantage                             |
|--------------------------------------------|-----------|------------------------------------------------|
| {{site.data.keyword.cos_full_notm}}        | COS API   | For cloud-native development.                  |
| IBM Cloud Object Storage (Infrastructure)  | COS API   | For regulatory compliance.                     |
| OpenStack Swift (Infrastructure)           | Swift API | For workloads requiring specific regions.      |
| OpenStack Swift (Cloud Foundry)            | Swift API | Native integration with Cloud Foundry services |

## IBM Cloud Object Storage (Infrastructure)

Data stored with Cloud Object Storage (Infrastructure) is located in one of 20 global data centers. Developers manage access at the account level. This offering is managed through the {{site.data.keyword.cloud_notm}} infrastructure Control portal.

For more information on this object storage service, [view the documentation ![External link icon](../../icons/launch-glyph.svg "External link icon")](https://ibm-public-cos.github.io/crs-docs/index.html){: new_window}.

## OpenStack Swift Object Storage (Infrastructure)

Data stored with OpenStack Swift (Infrastructure) is located in one of 20 global data centers. Developers use the community Swift API to interact with their storage accounts. This offering is managed through the {{site.data.keyword.cloud_notm}} infrastructure Control portal and does not provide encryption at-rest.

For more information on this object storage service, [view the documentation](/docs/infrastructure/objectstorage-swift/index.html).

## Swift Object Storage for Cloud Foundry (PaaS)

Data stored with OpenStack Swift (Cloud Foundry) is located in either Dallas or London data centers, and storage accounts are available for binding to {{site.data.keyword.cloud_notm}} services. Based on the OpenStack Swift platform, developers use the community Swift API to interact with their storage accounts. This offering is managed through the {{site.data.keyword.cloud_notm}} Platform console and does not provide encryption at-rest.

For more information on this object storage service, [view the documentation](/docs/services/ObjectStorage/index.html).
