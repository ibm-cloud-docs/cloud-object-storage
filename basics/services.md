---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-07"

keywords: cloud services, integration, aspera, key protect, archive, worm

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

The table below describes the regions where the following services are supported
* [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)
* [Key Protect](/docs/services/cloud-object-storage/basics/cloud-object-storage/basics?topic=cloud-object-storage-encryption#sse-kp)
* [{{site.data.keyword.hscrypto}} ({{site.data.keyword.hscrypto}})](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption)
* [Archive Data](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [Immutable Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started)


Downloads using Aspera high-speed will incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).
{:tip}

## Cross Region
{: #service-availability-geo}

| Region          | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker |
|-----------------|--------|-------------|------|--------------|--------------------------|------------------|
| AP Cross Region | Yes    | No          | No   | No           | No                       | Tokyo            |
| EU Cross Region | Yes    | No          | No   | No           | No                       | Frankfurt        |
| US Cross Region | Yes    | No          | No   | No           | No                       | Dallas           |




## Regional
{: #service-availability-region}

| Region           | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker |
|------------------|--------|-------------|------|--------------|--------------------------|------------------|
| AP Australia     | Yes    | Yes         | Yes  | Yes          | Yes                      | with COS API     |
| AP Japan         | Yes    | Yes         | No   | Yes          | Yes                      | Tokyo            |
| EU Great Britain | Yes    | Yes         | No   | Yes          | Yes                      | London           |
| EU Germany       | Yes    | Yes         | Yes   | Yes          | Yes                      | Frankfurt        |
| US South         | Yes    | Yes         | Yes  | Yes          | Yes                      | Dallas           |
| US East          | Yes    | Yes         | No   | Yes          | Yes                      | Dallas           |
## Single Data Centers
{: #service-availability-zone}

| Region                 | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Immutable Object Storage | Activity Tracker |
|------------------------|--------|-------------|------|--------------|--------------------------|------------------|
| Amsterdam, Netherlands | Yes    | No          | No   | No           | No                       | Frankfurt        |
| Chennai, India         | Yes    | No          | No   | No           | No                       | Tokyo            |
| Hong Kong              | Yes    | No          | No   | No           | No                       | Tokyo            |
| Melbourne, Australia   | Yes    | No          | No   | No           | No                       | with COS API     |
| Mexico City, Mexico    | Yes    | No          | No   | No           | No                       | Dallas           |
| Milan, Italy           | Yes    | No          | No   | No           | No                       | Frankfurt        |
| Montréal, Canada       | Yes    | No          | No   | No           | No                       | Dallas           |
| Oslo, Norway           | Yes    | No          | No   | No           | No                       | Frankfurt        |
| San Jose, USA          | Yes    | No          | No   | No           | No                       | Dallas           |
| São Paulo, Brazil      | Yes    | No          | No   | No           | No                       | Dallas           |
| Seoul, South Korea     | Yes    | No          | No   | No           | No                       | Japan            |
| Toronto, Canada        | Yes    | No          | No   | Yes          | No                       | Dallas           |
