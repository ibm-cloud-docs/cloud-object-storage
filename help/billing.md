---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# Billing
{: #billing}

Information on pricing can be found at [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window}.

## Invoices
{: #billing-invoices}

Find your account invoices at **Manage** > **Billing and Usage** in the navigation menu.

Each account receives a single bill. If you need separate billing for different sets of containers, then creating multiple accounts is necessary.

## {{site.data.keyword.cos_full_notm}} pricing
{: #billing-pricing}

Storage costs for {{site.data.keyword.cos_full}} are determined by total volume of data that is stored, the amount of public outbound bandwidth used, and the total number of operational requests processed by the system.

Infrastructure offerings are connected to a three-tiered network, segmenting public, private, and management traffic. Infrastructure services can transfer data between one another across the private network at no cost. Infrastructure offerings (such as bare metal servers, virtual servers, and cloud storage) connect to other applications and services in the {{site.data.keyword.cloud_notm}} Platform catalog (such as Watson services and Cloud Foundry runtimes) across the public network, so data transfer between those two types of offerings is metered and charged at standard public network bandwidth rates.
{: tip}

## Request classes
{: #billing-request-classes}

'Class A' requests involve modification or listing. This category includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.

'Class B' requests are related to retrieving objects or their associated metadata or configurations from the system.

Deleting buckets or objects from the system does not incur a charge.

| Class | Requests | Examples |
|--- |--- |--- |
| Class A | PUT, COPY, and POST requests, as well as GET requests used to list buckets and objects | Creating buckets, uploading or copying objects, listing buckets, listing contents of buckets, setting ACLs, and setting CORS configurations |
| Class B | GET (excluding listing), HEAD, and OPTIONS requests | Retrieving objects and metadata |

## Aspera transfers
{: #billing-aspera}

[Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) incurs extra egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage#s3api).

## Storage classes
{: #billing-storage-classes}

Not all data that is stored needs to be accessed frequently, and some archival data might be rarely accessed if at all. For less active workloads, buckets can be created in a different storage class and objects that are stored in these buckets incur charges on a different schedule than standard storage.

There are four classes:

*  **Standard** is used for active workloads, with no charge for data retrieved (other than the cost of the operational request itself).
*  **Vault** is used for cool workloads where data is accessed less than once a month - an extra retrieval charge ($/GB) is applied each time data is read. The service includes a minimum threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault** is used for cold workloads where data is accessed every 90 days or less - a larger extra retrieval charge ($/GB) is applied each time data is read. The service includes a longer minimum threshold for object size and storage period consistent with the intended use of this service for cold, inactive data.
*  **Flex** is used for dynamic workloads where access patterns are more difficult to predict. Depending on usage, if the costs of and retrieval charges exceeds a cap value, then retrieval charges are dropped and a new capacity charge is applied instead. If the data isn't accessed frequently, it is more cost effective than Standard storage, and if access usage patterns unexpectedly become more active it is more cost effective than Vault or Cold Vault storage. Flex doesn't require a minimum object size or storage period.

For more information about pricing, see [the pricing table at ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

For more information about creating buckets with different storage classes, see the [API reference](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).
