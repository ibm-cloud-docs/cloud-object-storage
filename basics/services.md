---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-07-25"

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
* [Hyper Protect Crypto Services (HPCS)](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption)
* [Archive Data](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [Immutable Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started)


Downloads using Aspera high-speed will incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).
{:tip}

## Cross Region
{: #service-availability-geo}

| Region          | Aspera | Key Protect | HPCS | Archive Data | Immutable Object Storage | Activity Tracker |
|-----------------|--------|-------------|------|--------------|--------------------------|------------------|
| AP Cross Region | Yes    | No          | No   | No           | No                       | jp-tok           |
| EU Cross Region | Yes    | No          | No   | No           | No                       | eu-de            |
| US Cross Region | Yes    | No          | No   | No           | No                       | us-south         |




## Regional
{: #service-availability-region}

| Region           | Aspera | Key Protect | HPCS | Archive Data | Immutable Object Storage | Activity Tracker |
|------------------|--------|-------------|------|--------------|--------------------------|------------------|
| AP Australia     | Yes    | Yes         | Yes  | Yes          | Yes                      | No               |
| AP Japan         | Yes    | Yes         | No   | Yes          | Yes                      | jp-tok           |
| EU Great Britain | Yes    | Yes         | No   | Yes          | Yes                      | eu-gb            |
| EU Germany       | Yes    | Yes         | No   | Yes          | Yes                      | eu-de            |
| US South         | Yes    | Yes         | Yes  | Yes          | Yes                      | us-south         |
| US East          | Yes    | Yes         | No   | Yes          | Yes                      | us-south         |
## Single Data Centers
{: #service-availability-zone}

| Region                 | Aspera | Key Protect | HPCS | Archive Data | Immutable Object Storage | Activity Tracker |
|------------------------|--------|-------------|------|--------------|--------------------------|------------------|
| Amsterdam, Netherlands | Yes    | No          | No   | No           | No                       | eu-de            |
| Chennai, India         | Yes    | No          | No   | No           | No                       | jp-tok           |
| Hong Kong              | Yes    | No          | No   | No           | No                       | jp-tok           |
| Melbourne, Australia   | Yes    | No          | No   | No           | No                       | No               |
| Mexico City, Mexico    | Yes    | No          | No   | No           | No                       | us-south         |
| Milan, Italy           | Yes    | No          | No   | No           | No                       | eu-de            |
| Montréal, Canada       | Yes    | No          | No   | No           | No                       | us-south         |
| Oslo, Norway           | Yes    | No          | No   | No           | No                       | eu-de            |
| San Jose, USA          | Yes    | No          | No   | No           | No                       | us-south         |
| São Paulo, Brazil      | Yes    | No          | No   | No           | No                       | us-south         |
| Seoul, South Korea     | Yes    | No          | No   | No           | No                       | jp-tok           |
| Toronto, Canada        | Yes    | No          | No   | Yes          | No                       | us-south         |