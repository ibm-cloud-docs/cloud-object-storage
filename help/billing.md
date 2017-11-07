---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Billing

Information on pricing can be found at [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage#s3api){:new_window}.

## Invoices
Find your account invoices at **Manage** > **Billing and Usage** in the navigation menu.

Each account receives a single bill. If you need separate billing for different sets of containers, then creating multiple accounts is necessary.

## {{site.data.keyword.cos_full_notm}} pricing

Storage costs for {{site.data.keyword.cos_full}} are determined by total volume of data stored, the amount of public outbound bandwidth consumed, and the total number of operational requests processed by the system.

Infrastructure offerings are connected to a three-tiered network, segmenting public, private, and management traffic. Infrastructure services may transfer data between one another across the private network at no cost. Infrastructure offerings (such as bare metal servers, virtual servers, and cloud storage) connect to other applications and services in the {{site.data.keyword.cloud_notm}} Platform catalog (such as Watson services and Cloud Foundry runtimes) across the public network, so data transfer between those two types of offerings is metered and charged at standard public network bandwidth rates.
{: tip}

## Request classes

'Class A' requests are operations that involve modification or listing.  This includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.

'Class B' requests are those related to retrieving objects or their associated metadata/configurations from the system.

There is no charge for deleting buckets or objects from the system.

| Class | Requests | Examples |
|--- |--- |--- |
| Class A | PUT, COPY, and POST requests, as well as GET requests used to list buckets and objects | Creating buckets, uploading or copying objects, listing buckets, listing contents of buckets, setting ACLs, and setting CORS configurations |
| Class B | GET (excluding listing), HEAD, and OPTIONS requests | Retrieving objects and metadata |

## Storage classes

Not all data that is stored needs to be accessed frequently, and some archival data might be rarely accessed if at all.  For less active workloads, buckets can be created in a different storage class and objects stored in these buckets will incur charges on a different schedule than standard storage.

There are four storage classes:

*  **Standard**: Used for active workloads - there is no charge for data retrieved (besides the cost of the operational request itself).
*  **Vault**: Used for cool workloads where data is accessed less than once a month - an additional retrieval charge ($/GB) is applied each time data is read. The service includes a minimum threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault**: Used for cold workloads where data is accessed every 90 days or less - an larger additional retrieval charge ($/GB) is applied each time data is read. The service includes a longer minimum threshold for object size and storage period consistent with the intended use of this service for cold, inactive data.
*  **Flex**: Used for dynamic workloads where access patterns are more difficult to predict. Depending on usage, if the cost of storage combined with retrieval charges exceeds a cap value, then retrieval charges are dropped and a new capacity charge is applied instead. If the data isn't accessed frequently, it is more cost effective than Standard storage, and if access usage patterns unexpectedly become more active it is more cost effective than Vault or Cold Vault storage. There is no minimum object size or storage period.

For pricing details please see [the pricing table at ibm.com](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage#s3api).

For information on how to create buckets with different storage classes, please see the [API reference](/docs/services/api-reference/api-reference-buckets.html#create-a-vault-bucket).
