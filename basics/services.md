---

copyright:
  years: 2017, 2022

lastupdated: "2022-03-09"

keywords: aspera, key protect, archive, worm

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Integrated service availability
{: #service-availability}

The document describes the regions where services and the different kinds of availability that are supported.
{: shortdesc}

For more information about the following services, be sure to check out the respective links:

* [Aspera high-speed transfer](/docs/cloud-object-storage/basics?topic=cloud-object-storage-aspera)
* [Key Protect (SSE-KP)](/docs/cloud-object-storage?topic=cloud-object-storage-kp)
* [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs)
* [Archive Data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [Immutable Object Storage](/docs/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-getting-started)
* [Functions](/docs/cloud-object-storage?topic=cloud-object-storage-functions)
* [Smart Tier](/docs/cloud-object-storage?topic=cloud-object-storage-billing#smart-tier-pricing-details)
* [Monitoring](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration)



Downloads that use Aspera high-speed transfer incur extra egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).
{: tip}

## Cross Region
{: #service-availability-geo}

| Region | Aspera | Key Protect         | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|--------|--------|---------------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| `ap`   | Yes    | Yes (in `jp-tok`)   | No                             | No           | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   |
| `eu`   | Yes    | Yes (in `eu-de`)    | No                             | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    |
| `us`   | Yes    | Yes (in `us-south`) | Yes (failover in `us-east`)    | No           | Yes                      | `us-south`       | No        | Yes        | `us-south` |


## Regional
{: #service-availability-region}

| Region     | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| `au-syd`   | Yes    | Yes         | Yes (see note)                 | Yes          | Yes                      | `au-syd`         | Yes       | Yes        | `au-syd`   |
| `jp-tok`   | Yes    | Yes         | No                             | Yes          | Yes                      | `ap-tok`         | Yes       | Yes        | `ap-tok`   |
| `jp-osa`   | No     | Yes         | No                             | Yes          | Yes                      | `ap-osa`         | Yes       | Yes        | `ap-osa`   |
| `eu-gb`    | Yes    | Yes         | No                             | Yes          | Yes                      | `eu-gb`          | Yes       | Yes        | `eu-gb`    |
| `eu-de`    | Yes    | Yes         | Yes (see note)                 | Yes          | Yes                      | `eu-de`          | Yes       | Yes        | `eu-de`    |
| `us-south` | Yes    | Yes         | Yes (see note)                 | Yes          | Yes                      | `us-south`       | Yes       | Yes        | `us-south` |
| `us-east`  | Yes    | Yes         | Yes (see note)                 | Yes          | Yes                      | `us-east`        | Yes       | Yes        | `us-east`  |
| `ca-tor`   | No     | Yes         | No                             | Yes          | Yes                      | `ca-tor`         | Yes       | Yes        | `ca-tor`   |
| `br-sao`   | No     | Yes         | No                             | Yes          | Yes                      | `br-sao`         | No        | Yes        | `br-sao`   |

It is possible to create a bucket and associate any available Key Protect or Hyper Protect Crypto Services instance with any of the listed Cloud Object Storage locations. Hyper Protect Crypto Services is only available in selected locations and it is your responsibility to ensure the location/region you select meets any pertinent requirements. Please refer to [Hyper Protect Crypto Services documentation](/docs/hs-crypto?topic=hs-crypto-regions) for a list of regions/locations currently available. 
{:note}

## Single Data Centers
{: #service-availability-zone}

| Region  | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|---------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| `ams03` | No     | No          | No                             | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    |
| `che01` | No     | No          | No                             | No           | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   |
| `hkg02` | No     | No          | No                             | No           | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   |
| `mex01` | No     | No          | No                             | No           | No                       | `us-south`       | No        | Yes        | `us-south` |
| `mil01` | No     | No          | No                             | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    |
| `mon01` | No     | No          | No                             | No           | No                       | `us-south`       | No        | Yes        | `us-south` |
| `par01` | No     | No          | No                             | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    |
| `sjc01` | No     | No          | No                             | No           | No                       | `us-south`       | No        | Yes        | `us-south` |
| `seo01` | No     | No          | No                             | No           | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   |
| `sng01` | No     | No          | No                             | No           | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   |

## Satellite 
{: #service-availability-sat}
| Location  | Aspera | Key Protect (IBM Cloud) | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|-----------|--------|-------------------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| `us-east` | No     | Yes                     | No                             | No           | No                       | No               | No        | No         | No         |
| `eu-de`   | No     | Yes                     | No                             | No           | No                       | No               | No        | No         | No         |
| `eu-gb`   | No     | Yes                     | No                             | No           | No                       | No               | No        | No         | No         |
| `jp-tok`  | No     | Yes                     | No                             | No           | No                       | No               | No        | No         | No         |

## More information
{: #service-availability-more-info}

Learn more about how locations are represented by [endpoints](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints) for users of {{site.data.keyword.cos_full_notm}}.