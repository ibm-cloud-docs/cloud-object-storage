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

{{site.data.keyword.cos_full_notm}} is available with two types of resiliency: Cross Region and Regional.  Cross Region provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US and EU. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US South and US East regions. If a given region or availability zone is unavailable, the object store continues to function without impediment.

Developers use an {{site.data.keyword.cos_full_notm}} API to interact with their object storage. This documentation provides support to [get started](docs/cloud-object-storage/index.html#getting-started-console-) with provisioning accounts, to create buckets, to upload objects, and to use a reference of common API interactions.

Users of the original {{site.data.keyword.cos_full_notm}} IaaS service provisioned through SoftLayer, [please visit this link for updates on the transition to {{site.data.keyword.cloud_notm}} Platform ![External link icon](../../icons/launch-glyph.svg "External link icon")](https://ibm-public-cos.github.io/crs-docs/ordering-storage){: new_window}.
<!--MM 11/6: this was the original link, but it doesn't work and I don't see it showing up in the nav to point to, so I picked the external link from the old doc. Nick should investigate this later (docs/services/cloud-object-storage/classic/iaas.html) -->



## Other IBM object storage services

In addition to {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} currently provides two additional object storage offerings for different user needs, all of which are accessible through web-based portals and REST APIs.

| Offering                            | Interface | Defining advantage                             |
|-------------------------------------|-----------|------------------------------------------------|
| {{site.data.keyword.cos_full_notm}} | COS API   | For cloud-native development.                  |
| IBM Cloud Object Storage (IaaS)     | S3 API    | For use with S3-compatible tools.              |
| OpenStack Swift (IaaS)              | Swift API | For workloads requiring specific regions.      |
| OpenStack Swift (Cloud Foundry)     | Swift API | Native integration with Cloud Foundry services |


## OpenStack Swift Object Storage (IaaS)

Data stored with OpenStack Swift (IaaS) is located in one of 20 global data centers. Developers use the community Swift API to interact with their storage accounts. This offering is managed through the {{site.data.keyword.cloud_notm}} infrastructure Control portal and does not provide encryption at-rest.

For more information on this object storage service, [view the documentation](/docs/infrastructure/objectstorage-swift/index.html).

## Swift Object Storage for Cloud Foundry (PaaS)

Data stored with OpenStack Swift (Cloud Foundry) is located in either Dallas or London data centers, and storage accounts are available for binding to {{site.data.keyword.cloud_notm}} services. Based on the OpenStack Swift platform, developers use the community Swift API to interact with their storage accounts. This offering is managed through the {{site.data.keyword.cloud_notm}} Platform console and does not provide encryption at-rest.

For more information on this object storage service, [view the documentation](/docs/services/ObjectStorage/index.html).
