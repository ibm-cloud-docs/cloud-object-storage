---

copyright:
  years: 2017, 2020

lastupdated: "2020-02-10"

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
* [Key Protect](/docs/cloud-object-storage/basics/cloud-object-storage/basics?topic=cloud-object-storage-encryption#sse-kp)
* [{{site.data.keyword.hscrypto}} (SSE-KP)](/docs/cloud-object-storage?topic=cloud-object-storage-encryption)
* [Archive Data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [Immutable Object Storage](/docs/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started)
* [Functions](/docs/cloud-object-storage?topic=cloud-object-storage-functions)
* [Smart Tier](/docs/cloud-object-storage?topic=cloud-object-storage-billing#smart-tier-pricing-details)
* [Monitoring](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration)



Downloads that use Aspera high-speed transfer incur extra egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).
{: tip}

## Cross Region
{: #service-availability-geo}

| Region          | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|-----------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| AP Cross Region | Yes    | No          | No                             | No           | No                       | Tokyo            | No        | No         | Tokyo |
| EU Cross Region | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        | No         | Frankfurt |
| US Cross Region | Yes    | No          | No                             | No           | Yes                      | Dallas           | No        | Yes        | Dallas |


## Regional
{: #service-availability-region}

| Region           | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|------------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|------------|------------|
| AP Australia     | Yes    | Yes         | Yes                            | Yes          | Yes                      | Sydney           | No        | Yes        | Sydney |
| AP Japan         | Yes    | Yes         | No                             | Yes          | Yes                      | Tokyo            | Yes       | Yes        | Tokyo |
| EU Great Britain | Yes    | Yes         | No                             | Yes          | Yes                      | London           | Yes       | Yes        | London |
| EU Germany       | Yes    | Yes         | No                             | Yes          | Yes                      | Frankfurt        | Yes       | Yes        | Frankfurt |
| US South         | Yes    | Yes         | Yes                            | Yes          | Yes                      | Dallas           | Yes       | Yes        | Dallas |
| US East          | Yes    | Yes         | No                             | Yes          | Yes                      | Washington DC           | Yes       | Yes        | Washington DC |

## Single Data Centers
{: #service-availability-zone}

| Region                      | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|-----------------------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|-----------|-----------|
| Amsterdam, Netherlands      | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        | No        | Frankfurt |
| Chennai, India              | Yes    | No          | No                             | No           | No                       | Tokyo            | No        | No        | Tokyo |
| Hong Kong S.A.R. of the PRC | Yes    | No          | No                             | No           | No                       | Tokyo            | No        | No        | Tokyo |
| Melbourne, Australia        | Yes    | No          | No                             | No           | No                       | Sydney           | No        | No        | Sydney |
| Mexico City, Mexico         | Yes    | No          | No                             | No           | No                       | Dallas           | No        | No        | Dallas |
| Milan, Italy                | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        | No        | Frankfurt |
| Montréal, Canada            | Yes    | No          | No                             | No           | No                       | Dallas           | No        | No        | Dallas |
| Oslo, Norway                | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        | No        | Frankfurt |
| Paris, France               | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        | No        | Frankfurt |
| San Jose, US                | Yes    | No          | No                             | No           | No                       | Dallas           | No        | No        | Dallas |
| São Paulo, Brazil           | Yes    | No          | No                             | Yes          | No                       | Dallas           | No        | No        | Dallas |
| Seoul, South Korea          | Yes    | No          | No                             | No           | No                       | Tokyo            | No        | No        | Tokyo |
| Singapore                   | Yes    | No          | No                             | No           | No                       | Tokyo            | No        | No        | Tokyo |
| Toronto, Canada             | Yes    | No          | No                             | Yes          | No                       | Dallas           | No        | No        | Dallas |

## More information
{: #service-availability-more-info}

Learn more about how locations are represented by [endpoints](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints) for users of {{site.data.keyword.cos_full_notm}}.