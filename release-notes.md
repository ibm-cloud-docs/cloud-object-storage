---

copyright:
  years: 2017, 2022
lastupdated: "2022-04-05"

keywords: updates, releases, news, object storage

subcollection: cloud-object-storage

content-type: release-note

---

{{site.data.keyword.attribute-definition-list}}

# Release notes for {{site.data.keyword.cos_short}}
{: #updates}

News on the latest releases from {{site.data.keyword.cos_full}} provide the updates you need on all things related to {{site.data.keyword.cos_full_notm}}.
{: shortdesc}

## 2 May 2022
{: #cloud-object-storage-may0222}
{: release-note}

New feature!
:   It is now possible so manage access [using context-based restrictions](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall). This offers significant improvements over the existing bucket firewall, and allows for the allowlisting of VPCs and other cloud services, in addition to IP address ranges.

## 5 April 2022
{: #cloud-object-storage-apr0522}
{: release-note}

Encryption update
:   Buckets can be created in the `eu-gb` region using [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs-cr) managed encryption.

## 9 March 2022
{: #cloud-object-storage-mar0922}
{: release-note}

Encryption update
:   New buckets created in `eu` Cross Region configuration can now use [Key Protect managed encryption](/docs/cloud-object-storage?topic=cloud-object-storage-kp).

## 20 January 2022
{: #cloud-object-storage-jan2022}
{: release-note}

{{site.data.keyword.satellitelong_notm}}
:   You can now use your own compute infrastructure to create a {{site.data.keyword.satelliteshort}} location. Then, you use the capabilities of {{site.data.keyword.satelliteshort}} to run {{site.data.keyword.cloud_notm}} services on your infrastructure, and consistently deploy, manage, and control your software workloads. For details, see [{{site.data.keyword.satellitelong_notm}}](/docs/satellite)

## 15 December 2022
{: #cloud-object-storage-dec1521}
{: release-note}

Encryption update
:   Buckets can be created in a US cross-region configuration using [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs-cr) managed encryption.

## 11 November 2021
{: #cloud-object-storage-nov1121}
{: release-note}

Versioning update
:   Object expiration is now permitted in [buckets with versioning enabled](/docs/cloud-object-storage?topic=cloud-object-storage-versioning).

Lifecycle update
:   Bucket lifecycle rules can now be created to [automatically remove incomplete multipart uploads](/docs/cloud-object-storage?topic=cloud-object-storage-lifecycle-cleanup-mpu).

## 24 September 2021
{: #cloud-object-storage-sep2421}
{: release-note}

Encryption update
:   Buckets created using [Key Protect managed encryption](/docs/cloud-object-storage?topic=cloud-object-storage-kp) can now also use [Immutable Object Storage](/docs/cloud-object-storage/basics?topic=cloud-object-storage-immutable) to create retention policies that prevent object deletion or modification.

## 30 August 2021
{: #cloud-object-storage-aug3021}
{: release-note}

New location!
:   Buckets can now be created in a Regional configuration in São Paulo, Brazil. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).
## 12 August 2021
{: #cloud-object-storage-aug1221}
{: release-note}

Encryption update
:   New buckets created in `us` or `ap` Cross Region configuration can now use [Key Protect managed encryption](/docs/cloud-object-storage?topic=cloud-object-storage-kp).

## 7 July 2021
{: #cloud-object-storage-jul721}
{: release-note}

New location!
:   Buckets can now be created in a Regional configuration in Toronto, Canada. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

Metrics update
:   In addition to [usage metrics](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration#mm-cos-metrics-usage), IBM Cloud Monitoring can now track [request metrics](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration#mm-cos-metrics-usage) for buckets.

## 31 March 2021
{: #cloud-object-storage-mar3121}
{: release-note}

New feature!
:   Buckets can now be [configured to version objects](/docs/cloud-object-storage?topic=cloud-object-storage-versioning), allowing for non-destructive overwrites and deletes.

Compliance update
:   Rules set in the [Security and Compliance Center](/docs/cloud-object-storage?topic=cloud-object-storage-manage-security-compliance) can now be enforced using the `disallow` action.

New feature!
:   Buckets can now be [configured to have a hard quota](/docs/cloud-object-storage?topic=cloud-object-storage-quota) to control costs by limiting the maximum amount of storage available for that bucket.

## 15 March 2021
{: #cloud-object-storage-mar1521}
{: release-note}

Activity tracking update
:   Updates to a bucket's metadata using the [Resource Configuration API](/apidocs/cos/cos-configuration) (such as [adding or modifying a firewall](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall)) will now show the details of the change in the `requestData` fields shown in [Activity Tracker](/docs/cloud-object-storage?topic=cloud-object-storage-at).

## 15 December 2020
{: #cloud-object-storage-dec1520}
{: release-note}

New feature!
:   Objects can now be efficiently [tagged](/docs/cloud-object-storage?topic=cloud-object-storage-object-tagging) with custom key-value pairs.

## 5 November 2020
{: #cloud-object-storage-nov520}
{: release-note}

New location!
:   Buckets can now be created in a Regional configuration in Osaka, Japan. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 27 October 2020
{: #cloud-object-storage-oct2720}
{: release-note}

New feature!
:   Buckets can now be configured to [serve static websites](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-options).

## 12 October 2020
{: #cloud-object-storage-oct1220}
{: release-note}

Encryption updates
:   Lifecycle actions on [Key Protect](/docs/cloud-object-storage?topic=cloud-object-storage-kp#kp-lifecycle) and [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs-lifecycle) encryption keys can now generate bucket events in Activity Tracker.
:   Buckets can now be encrypted using {{site.data.keyword.hscrypto}} in the US East region.

## 18 August 2020
{: #cloud-object-storage-aug1820}
{: release-note}

New feature!
:   Data can now be [archived](/docs/cloud-object-storage?topic=cloud-object-storage-archive) using an Accelerated class that allows restoration of archived objects in under two hours for an [additional cost](https://www.ibm.com/cloud/object-storage/pricing).

## 30 April 2020
{: #cloud-object-storage-apr3020}
{: release-note}

IAM updates
:   Users and Service IDs can now be granted a new `ObjectWriter` role that allows access to writing objects, but without permissions to download objects or to list the contents of a bucket. 
:   Public Access can now be granted to a new `ObjectReader` role that allows anonymous access to reading objects, but without permissions to list the contents of a bucket. 

## 10 February 2020
{: #cloud-object-storage-feb1020}
{: release-note}

New feature!
:   Buckets can now be created in a new [Smart Tier storage class](/docs/cloud-object-storage?topic=cloud-object-storage-billing#smart-tier-pricing-details) that optimizes costs based on usage patterns. 

## 6 December 2019
{: #cloud-object-storage-dec620}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Singapore. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 15 November 2019
{: #cloud-object-storage-nov1920}
{: release-note}

Lifecycle update
:   Data can now be [archived](/docs/cloud-object-storage?topic=cloud-object-storage-archive) in buckets located in São Paulo, Brazil (`sao1`).

## 24 October 2019
{: #cloud-object-storage-oct2420}
{: release-note}

Compliance update
:   [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable) is now available for buckets in US Cross Region (`us`).

## 13 October 2019
{: #cloud-object-storage-oct1919}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Paris, France. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 11 September 2019
{: #cloud-object-storage-sep1119}
{: release-note}

New feature!
:   Changes made to object storage data can be used as an [event source for Cloud Functions](/docs/cloud-object-storage?topic=cloud-object-storage-functions).

## 28 August 2019
{: #cloud-object-storage-aug2819}
{: release-note}

New feature!
:   Data can be encrypted [using HPCS](/docs/cloud-object-storage?topic=cloud-object-storage-encryption).

## 7 August 2019
{: #cloud-object-storage-aug719}
{: release-note}

Activity tracking update
:   Object-level events can be tracked [using Activity Tracker](/docs/cloud-object-storage?topic=cloud-object-storage-at).

## 11 June 2019
{: #cloud-object-storage-jun1119}
{: release-note}

New feature!
:   Objects can be automatically deleted by [adding expiration rules to a bucket's lifecycle configuration](/docs/cloud-object-storage?topic=cloud-object-storage-expiry).

## 15 May 2019
{: #cloud-object-storage-may1519}
{: release-note}

New CLI plug-in!
:   Users can access and interact with object storage [using the IBM Cloud CLI](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli).

## 26 April 2019
{: #cloud-object-storage-apr2619}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Hong Kong. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 28 March 2019
{: #cloud-object-storage-mar2819}
{: release-note}

New feature!
:   User can use [COS Firewall](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall#setting-a-firewall) to restrict access to the data in COS only if request originates from a list of allowed IP addresses.

IAM update
:   IAM policies can now grant [public access](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access) to entire buckets.

New location!
:   Buckets can now be created in a Single Data Center configuration in Milan, Italy. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 28 February 2019
{: #cloud-object-storage-feb2819}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in San Jose, USA. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 18 January 2019
{: #cloud-object-storage-jan1819}
{: release-note}

New location!
:   Buckets can now be created in AP Australia region. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 14 December 2018
{: #cloud-object-storage-dec1418}
{: release-note}

New feature!
:   Users can use [Immutable Object Storage](/docs/cloud-object-storage/basics?topic=cloud-object-storage-immutable) to create retention policies that prevent object deletion or modification.

New location!
:   Buckets can now be created in a Single Data Center configuration in Mexico City, Mexico. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 12 November 2018
{: #cloud-object-storage-nov1218}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Montréal, Canada. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 12 October 2018
{: #cloud-object-storage-oct1218}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Seoul, South Korea. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 20 September 2018
{: #cloud-object-storage-sep2018}
{: release-note}

New feature!
:   Users can [archive cold data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive) by setting the proper parameters in a bucket lifecycle configuration policy, either using the console, REST API, or a language-specific SDK.

## 18 August 2018
{: #cloud-object-storage-aug1818}
{: release-note}

New locations!
:   Buckets can now be created in a Single Data Center configuration in Sao Paolo, Brazil and Oslo, Norway. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 22 June 2018
{: #cloud-object-storage-jun2218}
{: release-note}

New locations!
:   Buckets can now be created in the EU Germany region. Data stored in these buckets is distributed across three availability zones in the EU Germany region. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) documentation.
:   Buckets can now be created in a Single Data Center configuration in Chennai, India and Amsterdam, Netherlands. This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## 16 March 2018
{: #cloud-object-storage-mar1618}
{: release-note}

New location!
:   Buckets can now be created in an AP Cross Region configuration. Data stored in these buckets is distributed across the Seoul, Tokyo, and Hong Kong data centers. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

New feature!
:   Users can run `SELECT` SQL queries directly against structured data objects using IBM Cloud SQL Query. More information can be found in the [SQL Query documentation](/docs/sql-query?topic=sql-query-overview).
## 7 March 2018
{: #cloud-object-storage-mar718}
{: release-note}

New feature!
:   Users who upload or download files using the web-based console have the option to use Aspera high-speed transfer for these operations via a browser plug-in. This allows for transfers of objects larger than 200MB, and also allows for greater control and visibility of uploads and downloads. Additional information can be found in the [Uploading Data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-aspera) documentation. Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).

## 11 February 2018
{: #cloud-object-storage-feb1118}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Toronto, Canada and Melbourne, Australia. This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) documentation.

## 8 August 2017
{: #cloud-object-storage-aug817}
{: release-note}

Introducing {{site.data.keyword.cos_full_notm}}
:  Object Storage is a highly available, durable, and secure platform for storing unstructured data. Unstructured data (sometimes called binary or "blob" data) refers to data that is not highly structured in the manner of a database. Object storage is the most efficient way to store PDFs, media files, database backups, disk images, or even large structured datasets. 