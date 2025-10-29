---

copyright:
  years: 2017, 2025

lastupdated: "2025-10-29"

keywords: aspera, key protect, archive, worm

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Integrated service availability
{: #service-availability}

The document describes the regions where services and the different kinds of availability that are supported.
{: shortdesc}

For more information about the following services, be sure to check out the respective links:

* [Aspera high-speed transfer](/docs/cloud-object-storage?topic=cloud-object-storage-aspera)
* [Key Protect (SSE-KP)](/docs/cloud-object-storage?topic=cloud-object-storage-kp)
* [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs)
* [Archive Data](/docs/cloud-object-storage?topic=cloud-object-storage-archive)
* [Object Lock](/docs/cloud-object-storage?topic=cloud-object-storage-ol-overview)
* [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/activity-tracker?topic=activity-tracker-getting-started)
* [Functions](/docs/cloud-object-storage?topic=cloud-object-storage-functions)
* [Code Engine](/docs/cloud-object-storage?topic=cloud-object-storage-code-engine)
* [Smart Tier](/docs/cloud-object-storage?topic=cloud-object-storage-billing#smart-tier-pricing)
* [Metrics Routing](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration)
* [Bucket Backup](/docs/cloud-object-storage?group=backing-up-buckets)

Downloads that use Aspera high-speed transfer incur extra egress charges. For more information, see the [pricing page](https://www.ibm.com/products/cloud-object-storage).
{: tip}

## Cross Region
{: #service-availability-geo}

| Region | Aspera | Key Protect         | {{site.data.keyword.hscrypto}} | Archive Data | Object Lock                             | Immutable Object Storage | Activity Tracker Routing| Code Engine | Smart Tier | Metrics Routing | Replication |  One-Rate  |  Bucket Backup  |
|--------|--------|---------------------|--------------------------------|--------------|-----------------------------------------|--------------------------|-------------------------|-------------|------------|-----------------|-------------| -----------| ----------------|
| `ap`   | Yes    | Yes (in `jp-tok`)   | No                             | No           | Yes          | No                       | `ap-tok`                | No          | Yes        | `ap-tok`        | Yes         |  No        |  Yes            |
| `eu`   | Yes    | Yes (in `eu-de`)    | No                             | No           | Yes          | No                       | `eu-de`                 | No          | Yes        | `eu-de`         | Yes         |  No        |  Yes            |
| `us`   | Yes    | Yes (in `us-south`) | Yes (failover in `us-east`)    | No           | Yes                                     | Yes                      | `us-south`              | No          | Yes        | `us-south`      | Yes         |  No        |  Yes            |

Refer to [Pricing for Key Protect on IBM Cloud](/docs/key-protect?topic=key-protect-pricing-plan) for appropriate planning.
{: note}

## Regional
{: #service-availability-region}

| Region     | Aspera | Key Protect   | {{site.data.keyword.hscrypto}} | Archive Data | Object Lock | Immutable Object Storage | Activity Tracker Routing | Code Engine | Smart Tier | Metrics Routing | Replication | One-Rate | Bucket Backup |
|------------|--------|---------------|--------------------------------|--------------|-------------|--------------------------|--------------------------|-------------|------------|-----------------|-------------|----------|---------------|
| `au-syd`   | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `au-syd`                 | Yes         | Yes        | `au-syd`        | Yes         | Yes      | Yes           |
| `jp-tok`   | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `ap-tok`                 | Yes         | Yes        | `ap-tok`        | Yes         | Yes      | Yes           |
| `jp-osa`   | No     | Yes           | No                             | Yes          | Yes         | Yes                      | `ap-osa`                 | Yes         | Yes        | `ap-osa`        | Yes         | Yes      | Yes           |
| `eu-gb`    | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `eu-gb`                  | Yes         | Yes        | `eu-gb`         | Yes         | Yes      | Yes           |
| `eu-de`    | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `eu-de`                  | Yes         | Yes        | `eu-de`         | Yes         | Yes      | Yes           |
| `us-south` | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `us-south`               | Yes         | Yes        | `us-south`      | Yes         | Yes      | Yes           |
| `us-east`  | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `us-east`                | Yes         | Yes        | `us-east`       | Yes         | Yes      | Yes           |
| `ca-tor`   | No     | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `ca-tor`                 | Yes         | Yes        | `ca-tor`        | Yes         | Yes      | Yes           |
| `br-sao`   | No     | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `br-sao`                 | Yes         | Yes        | `br-sao`        | Yes         | Yes      | Yes           |
| `eu-es`    | No     | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `eu-es`                  | No          | Yes        | `eu-es`         | Yes         | Yes      | Yes           |
| `ca-mon`   | No     | No            | No                             | No           | Yes         | No                       | `ca-mon`                 | No          | Yes        | `ca-mon`        | Yes         | Yes      | Yes           |

It is possible to create a bucket in any of the listed Cloud Object Storage regional locations and associate it with any available Key Protect or Hyper Protect Crypto Service instance in any IBM Cloud region. Hyper Protect Crypto Services is only available in selected locations and it is your responsibility to ensure the location/region you select meets any pertinent requirements. Please refer to [Hyper Protect Crypto Services documentation](/docs/hs-crypto?topic=hs-crypto-regions) and [IBM Key Protect](/docs/key-protect?topic=key-protect-regions) for a list of regions/locations currently available.
{: note}

## Single Data Centers
{: #service-availability-zone}

| Region  | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Object Lock                             | Immutable Object Storage | Activity Tracker Routing | Code Engine | Smart Tier | Metrics Routing | Replication | One-Rate   | Bucket Backup   |
|---------|--------|-------------|--------------------------------|--------------|-----------------------------------------|--------------------------|--------------------------|-------------|------------|-----------------|-------------| -----------| ----------------|
| `ams03` | No     | No          | No                             | No           | Yes          | No                       | `eu-de`                  | No          | Yes        | `eu-de`         | Yes         | Yes        | Yes             |
| `che01` | Yes    | No          | No                             | Yes          | Yes                                     | No                       | `che01`                  | No          | Yes        | `jp-tok`        | Yes         | Yes        | Yes             |
| `mil01` | No     | No          | No                             | No           | Yes          | No                       | `eu-de`                  | No          | Yes        | `eu-de`         | Yes         | Yes        | Yes             |
| `mon01` | No     | No          | No                             | No           | Yes          | No                       | `ca-tor`                 | No          | Yes        | `ca-tor`        | Yes         | Yes        | Yes             |
| `par01` | No     | No          | No                             | No           | Yes          | No                       | `eu-de`                  | No          | Yes        | `eu-de`         | Yes         | Yes        | Yes             |
| `sjc04` | No     | No          | No                             | No           | Yes          | No                       | `us-south`               | No          | Yes        | `us-south`      | Yes         | Yes        | Yes             |
| `sng01` | No     | No          | No                             | No           | Yes          | No                       | `ap-tok`                 | No          | Yes        | `ap-tok`        | Yes         | Yes        | Yes             |

## More information
{: #service-availability-more-info}

Learn more about how locations are represented by [endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) for users of {{site.data.keyword.cos_full_notm}}.
