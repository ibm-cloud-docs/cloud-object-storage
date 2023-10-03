---

copyright:
  years: 2017, 2023

lastupdated: "2023-10-03"

keywords: aspera, key protect, archive, worm

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Integrated service availability
{: #service-availability}

The document describes the regions where services and the different kinds of availability that are supported.
{: shortdesc}

For more information about the following services, be sure to check out the respective links:

* [Aspera high-speed transfer](/docs/cloud-object-storage/basics?topic=cloud-object-storage-aspera)
* [Key Protect (SSE-KP)](/docs/cloud-object-storage?topic=cloud-object-storage-kp)
* [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs)
* [Archive Data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [Object Lock](/docs/cloud-object-storage/basics?topic=cloud-object-storage-ol-overview)
* [Immutable Object Storage](/docs/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/activity-tracker?topic=activity-tracker-getting-started)
* [Functions](/docs/cloud-object-storage?topic=cloud-object-storage-functions)
* [Smart Tier](/docs/cloud-object-storage?topic=cloud-object-storage-billing#smart-tier-pricing-details)
* [Monitoring](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration)

Downloads that use Aspera high-speed transfer incur extra egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).
{: tip}

## Cross Region
{: #service-availability-geo}

| Region | Aspera | Key Protect         | {{site.data.keyword.hscrypto}} | Archive Data | Object Lock  | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring | Replication |  One Rate |
|--------|--------|---------------------|--------------------------------|--------------|--------------|--------------------------|------------------|-----------|------------|------------|-------------| -----------|
| `ap`   | Yes    | Yes (in `jp-tok`)   | No                             | No           | No           | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   | Yes         |  No        |
| `eu`   | Yes    | Yes (in `eu-de`)    | No                             | No           | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    | Yes         |  No        |
| `us`   | Yes    | Yes (in `us-south`) | Yes (failover in `us-east`)    | No           | Yes          | Yes                      | `us-south`       | No        | Yes        | `us-south` | Yes         |  No        |


## Regional
{: #service-availability-region}

| Region     | Aspera | Key Protect   | {{site.data.keyword.hscrypto}} | Archive Data | Object Lock | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring | Replication | One Rate |
|------------|--------|---------------|--------------------------------|--------------|-------------|--------------------------|------------------|-----------|------------|------------|-------------|----------|
| `au-syd`   | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `au-syd`         | Yes       | Yes        | `au-syd`   | Yes         | Yes      |
| `jp-tok`   | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `ap-tok`         | Yes       | Yes        | `ap-tok`   | Yes         | Yes      |
| `jp-osa`   | No     | Yes           | No                             | Yes          | Yes         | Yes                      | `ap-osa`         | Yes       | Yes        | `ap-osa`   | Yes         | Yes      |
| `eu-gb`    | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `eu-gb`          | Yes       | Yes        | `eu-gb`    | Yes         | Yes      |
| `eu-de`    | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `eu-de`          | Yes       | Yes        | `eu-de`    | Yes         | Yes      |
| `us-south` | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `us-south`       | Yes       | Yes        | `us-south` | Yes         | Yes      |
| `us-east`  | Yes    | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `us-east`        | Yes       | Yes        | `us-east`  | Yes         | Yes      |
| `ca-tor`   | No     | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `ca-tor`         | Yes       | Yes        | `ca-tor`   | Yes         | Yes      |
| `br-sao`   | No     | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `br-sao`         | Yes       | Yes        | `br-sao`   | Yes         | Yes      |
| `eu-es`    | No     | Yes           | Yes (see note)                 | Yes          | Yes         | Yes                      | `eu-de`          | No        | Yes        | `eu-de`    | Yes         | Yes      |

It is possible to create a bucket and associate any available Key Protect or Hyper Protect Crypto Services instance with any of the listed Cloud Object Storage locations. Hyper Protect Crypto Services is only available in selected locations and it is your responsibility to ensure the location/region you select meets any pertinent requirements. Please refer to [Hyper Protect Crypto Services documentation](/docs/hs-crypto?topic=hs-crypto-regions) and [IBM Key Protect](/docs/key-protect?topic=key-protect-regions) for a list of regions/locations currently available.
{: note}

## Single Data Centers
{: #service-availability-zone}

| Region  | Aspera | Key Protect | {{site.data.keyword.hscrypto}} | Archive Data | Object Lock  | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring | Replication | One Rate |
|---------|--------|-------------|--------------------------------|--------------|--------------|--------------------------|------------------|-----------|------------|------------|-------------| -----------|
| `ams03` | No     | No          | No                             | No           | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    | Yes         | Yes        |
| `che01` | No     | No          | No                             | Yes           | Yes          | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   | Yes         | Yes        |
| `mil01` | No     | No          | No                             | No           | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    | Yes         | Yes        |
| `mon01` | No     | No          | No                             | No           | No           | No                       | `us-south`       | No        | Yes        | `us-south` | Yes         | Yes        |
| `par01` | No     | No          | No                             | No           | No           | No                       | `eu-de`          | No        | Yes        | `eu-de`    | Yes         | Yes        |
| `sjc01` | No     | No          | No                             | No           | No           | No                       | `us-south`       | No        | Yes        | `us-south` | Yes         | Yes        |
| `sng01` | No     | No          | No                             | No           | No           | No                       | `ap-tok`         | No        | Yes        | `ap-tok`   | Yes         | Yes        |

## Satellite
{: #service-availability-sat}
| Location  | Aspera | Key Protect (IBM Cloud) | {{site.data.keyword.hscrypto}} | Archive Data | Object Lock  | Immutable Object Storage | Activity Tracker | Functions | Smart Tier | Monitoring |
|-----------|--------|-------------------------|--------------------------------|--------------|--------------|--------------------------|------------------|-----------|------------|------------|
| `us-east` | No     | Yes                     | No                             | No           | No           | No                       | No               | No        | No         | No         |
| `eu-de`   | No     | Yes                     | No                             | No           | No           | No                       | No               | No        | No         | No         |
| `eu-gb`   | No     | Yes                     | No                             | No           | No           | No                       | No               | No        | No         | No         |
| `jp-tok`  | No     | Yes                     | No                             | No           | No           | No                       | No               | No        | No         | No         |

## More information
{: #service-availability-more-info}

Learn more about how locations are represented by [endpoints](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints) for users of {{site.data.keyword.cos_full_notm}}.