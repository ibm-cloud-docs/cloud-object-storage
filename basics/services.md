---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-26"

keywords: aspera, key protect, archive, worm

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

# Integrated service availability
{: #service-availability}

The document describes the regions where services and the different kinds of availability that are supported.
{: .shortdesc}

For more information about the following services, be sure to check out the respective links:

* [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)
* [Key Protect](/docs/services/cloud-object-storage/basics/cloud-object-storage/basics?topic=cloud-object-storage-encryption#sse-kp)
* [{{site.data.keyword.hscrypto}} ({{site.data.keyword.hscrypto}})](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption)
* [Archive Data](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [Immutable Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started)


Downloads that use Aspera high-speed transfer incur extra egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).
{:tip}

## Cross Region
{: #service-availability-geo}

| Region          | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions |
|-----------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|
| AP Cross Region | Yes    | No          | No                             | No           | No                       | Tokyo            | No        |
| EU Cross Region | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        |
| US Cross Region | Yes    | No          | No                             | No           | No                       | Dallas           | No        |




## Regional
{: #service-availability-region}

| Region           | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions |
|------------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|
| AP Australia     | Yes    | Yes         | Yes                            | Yes          | Yes                      | Sydney           | No        |
| AP Japan         | Yes    | Yes         | No                             | Yes          | Yes                      | Tokyo            | Yes       |
| EU Great Britain | Yes    | Yes         | No                             | Yes          | Yes                      | London           | Yes       |
| EU Germany       | Yes    | Yes         | No                             | Yes          | Yes                      | Frankfurt        | Yes       |
| US South         | Yes    | Yes         | Yes                            | Yes          | Yes                      | Dallas           | Yes       |
| US East          | Yes    | Yes         | No                             | Yes          | Yes                      | Dallas           | Yes       |

## Single Data Centers
{: #service-availability-zone}

| Region                      | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker | Functions |
|-----------------------------|--------|-------------|--------------------------------|--------------|--------------------------|------------------|-----------|
| Amsterdam, Netherlands      | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        |
| Chennai, India              | Yes    | No          | No                             | No           | No                       | Tokyo            | No        |
| Hong Kong S.A.R. of the PRC | Yes    | No          | No                             | No           | No                       | Tokyo            | No        |
| Melbourne, Australia        | Yes    | No          | No                             | No           | No                       | Sydney           | No        |
| Mexico City, Mexico         | Yes    | No          | No                             | No           | No                       | Dallas           | No        |
| Milan, Italy                | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        |
| Montréal, Canada            | Yes    | No          | No                             | No           | No                       | Dallas           | No        |
| Oslo, Norway                | Yes    | No          | No                             | No           | No                       | Frankfurt        | No        |
| Paris, France               | Yes     | No          | No                             | No           | No                       | Frankfurt        | No        |
| San Jose, US                | Yes    | No          | No                             | No           | No                       | Dallas           | No        |
| São Paulo, Brazil           | Yes    | No          | No                             | No           | No                       | Dallas           | No        |
| Seoul, South Korea          | Yes    | No          | No                             | No           | No                       | Tokyo            | No        |
| Toronto, Canada             | Yes    | No          | No                             | Yes          | No                       | Dallas           | No        |
