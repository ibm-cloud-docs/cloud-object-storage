---

copyright:
  years: 2017, 2020

lastupdated: "2020-03-25"

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

| Region          | Aspera | Key Protect         | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|-----------------|--------|---------------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| AP Cross Region | Yes    | Yes (in `jp-tok`)   | No                             | No           | No                       | Tokyo            | No        | Yes        | Tokyo      |
| EU Cross Region | Yes    | No                  | No                             | No           | No                       | Frankfurt        | No        | Yes        | Frankfurt  |
| US Cross Region | Yes    | Yes (in `us-south`) | No                             | No           | Yes                      | Dallas           | No        | Yes        | Dallas     |


## Regional
{: #service-availability-region}

| Region           | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring    |
|------------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|---------------|
| AP Australia     | Yes    | Yes         | Yes                            | Yes          | Yes                      | Sydney           | No        | Yes        | Sydney        |
| AP Tokyo         | Yes    | Yes         | No                             | Yes          | Yes                      | Tokyo            | Yes       | Yes        | Tokyo         |
| AP Osaka         | No     | Yes         | No                             | Yes          | Yes                      | Osaka            | Yes       | Yes        | Osaka         |
| EU Great Britain | Yes    | Yes         | No                             | Yes          | Yes                      | London           | Yes       | Yes        | London        |
| EU Germany       | Yes    | Yes         | Yes                            | Yes          | Yes                      | Frankfurt        | Yes       | Yes        | Frankfurt     |
| US South         | Yes    | Yes         | Yes                            | Yes          | Yes                      | Dallas           | Yes       | Yes        | Dallas        |
| US East          | Yes    | Yes         | Yes                            | Yes          | Yes                      | Washington DC    | Yes       | Yes        | Washington DC |
| CA Toronto       | No     | Yes         | No                             | Yes          | Yes                      | Toronto          | No        | Yes        | Toronto       |
| BR São Paulo     | No     | Yes         | No                             | Yes          | Yes                      | Dallas           | No        | Yes        | Dallas        |

## Single Data Centers
{: #service-availability-zone}

| Region                      | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|-----------------------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| Amsterdam, Netherlands      | No     | No          | No                             | No           | No                       | Frankfurt        | No        | Yes        | Frankfurt  |
| Chennai, India              | No     | No          | No                             | No           | No                       | Tokyo            | No        | Yes        | Tokyo      |
| Hong Kong S.A.R. of the PRC | No     | No          | No                             | No           | No                       | Tokyo            | No        | Yes        | Tokyo      |
| Mexico City, Mexico         | No     | No          | No                             | No           | No                       | Dallas           | No        | Yes        | Dallas     |
| Milan, Italy                | No     | No          | No                             | No           | No                       | Frankfurt        | No        | Yes        | Frankfurt  |
| Montréal, Canada            | No     | No          | No                             | No           | No                       | Dallas           | No        | Yes        | Dallas     |
| Oslo, Norway                | No     | No          | No                             | No           | No                       | Frankfurt        | No        | Yes        | Frankfurt  |
| Paris, France               | No     | No          | No                             | No           | No                       | Frankfurt        | No        | Yes        | Frankfurt  |
| San Jose, US                | No     | No          | No                             | No           | No                       | Dallas           | No        | Yes        | Dallas     |
| São Paulo, Brazil           | Yes    | No          | No                             | Yes          | No                       | Dallas           | No        | Yes        | Dallas     |
| Seoul, South Korea          | No     | No          | No                             | No           | No                       | Tokyo            | No        | Yes        | Tokyo      |
| Singapore                   | No     | No          | No                             | No           | No                       | Tokyo            | No        | Yes        | Tokyo      |

## More information
{: #service-availability-more-info}

Learn more about how locations are represented by [endpoints](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints) for users of {{site.data.keyword.cos_full_notm}}.