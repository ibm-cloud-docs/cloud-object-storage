---

copyright:
  years: 2017, 2026

lastupdated: "2026-02-10"


keywords: updates, releases, news, object storage, buckets, fine grained access control, iam, policy, region, cli, monitoring, object lock, encryption, key protect, hyper protect crypto services, immutable object storage, satellite, versioning, metrics router, activity tracker routing, routing, monitoring

subcollection: cloud-object-storage

content-type: release-note

---

{{site.data.keyword.attribute-definition-list}}

# Release notes for {{site.data.keyword.cos_short}}
{: #updates}

News on the latest releases from {{site.data.keyword.cos_full}} provide the updates you need on all things related to {{site.data.keyword.cos_full_notm}}.
{: shortdesc}


## 08 January 2026
{: #cloud-object-storage-jan0826}
{: release-note}

Local-access VPE support for {{site.data.keyword.cos_full_notm}}
:   {{site.data.keyword.cos_short}} now supports local-access Virtual Private Endpoints (VPEs) within DNS-shared VPCs. This feature lets customers route traffic to specific buckets directly from each shared VPC, improving performance and enabling finer access control with Context-Based Restriction (CBR) rules, security groups, and resource-level bindings. The hub VPC VPE remains available for all other {{site.data.keyword.cos_short}} access, allowing flexible and secure topologies. For more information, see [Creating a local-access endpoint gateway](/docs/vpc?topic=vpc-create-local-access-vpe&interface=ui).

## 06 January 2026
{: #cloud-object-storage-jan0626}
{: release-note}

Feature update for {{site.data.keyword.cos_full_notm}}
:   {{site.data.keyword.cos_short}} has enhanced the Object Lock feature with support for the GOVERNANCE retention mode. Compared to prior release, only COMPLIANCE mode and Legal Hold were supported, which provided strict Write Once Read Many (WORM) protections. With this enhancement, GOVERNANCE mode offers more flexible data protection that still enforces immutability, but with the ability for privileged users to override restrictions under certain conditions.  For more information, see [Object operations](/docs/cloud-object-storage?topic=cloud-object-storage-object-operations) and the SDK languages for [Using Java](/docs/cloud-object-storage?topic=cloud-object-storage-java&interface=ui#java-create-bucket), [Using Python](/docs/cloud-object-storage?topic=cloud-object-storage-python&interface=ui#python-examples-create-bucket-ol), [Using Node.js](/docs/cloud-object-storage?topic=cloud-object-storage-node&interface=ui#node-examples-create-bucket) and [Using Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go&interface=ui#go-create-bucket-ol-enabled).

## 31 October 2025
{: #cloud-object-storage-oct312025}
{: release-note}

New location!
:   Buckets can now be created in a single-campus Multi-Zone Region (SC-MZR) location available as a regional COS offering in Chennai, India (in-che). More information can be found in the [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) and [Integrated service availability](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).



## 21 August 2025
{: #cloud-object-storage-aug212025}
{: release-note}

Checksum update!
:   Additional Checksum Algorithms for AWS S3
API methods have been updated to accept new checksums for content verification with parity to AWS S3. There is added support for additional checksum algorithms: SHA-1, SHA-256, CRC-32, CRC-32C, and CRC-64/NVME. The checksum value may be provided as a header, or as a trailer to the payload when using chunked encoding. Such algorithms and trailer format are now used by the latest AWS SDK by default. These checksums may be used to verify not only object upload requests, but also requests involving XML content bodies. There will be a default checksum calculation performed for every object upload, if one of the new checksums is not specifically requested to be used. In addition, there is now an option to make use of a full object type checksum for multipart uploads, as well as composite. The additional checksums may also be used to validate request types that require content validation. Some of these requests, which enforce content validation, are related to features such as Object Lock or WORM, Lifecycle, or batch delete (multi-delete).

## 28 May 2025
{: #cloud-object-storage-may282025}
{: release-note}

New feature!
:   You can now back up data stored in your buckets with {{site.data.keyword.cos_full}} Backup. Use this feature to create continuous backups of your data, tracking all changes to your objects. Store backups in new Backup Vaults which are hardened resources to protect backups from deletion. Customize the retention periods of your backups. Restore data from any point in time within your retention window.


## 14 March 2025
{: #cloud-object-storage-mar142025}
{: release-note}

New location!
:   Buckets can now be created in a single-campus MZR (SC-MZR) location available as a regional COS offering in Montreal (ca-mon). More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) and [Integrated service availability](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).

## 12 December 2024
{: #cloud-object-storage-dec2024}
{: release-note}

{{site.data.keyword.cos_full}} for {{site.data.keyword.satellitelong}} is deprecated
:   {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} is a managed object storage service that can be deployed on IBM Satellite for clients to store data closer to their applications and data sources, whether on-premises, at the edge, or in a multi-cloud environment. This service is being deprecated due to changes in market expectations, client fit, and lack of adoption. After December 16, 2025, this service is no longer supported. For more information, see [Deprecation overview](/docs/cloud-object-storage?topic=cloud-object-storage-deprecation-cos-satellite).

## 20 August 2024
{: #cloud-object-storage-aug2024}
{: release-note}

Object Lock available in the EU and AP Cross Regions and all Single Sites
:   It is now possible to lock objects to ensure individual object versions are stored in a WORM (Write-Once-Read-Many), non-erasable and non-rewritable manner in the EU and AP Cross Region sites along with all Single Sites.

## 01 July 2024
{: #cloud-object-storage-jul0124}
{: release-note}

Free tier update
:   IBM Cloud Object Storage currently offers a free evaluation to new clients using the Lite Plan. Effective July 1st, 2024, IBM Cloud will replace the Lite Plan with a new Free Tier available within the Standard (paid) plan. See [Cloud Object Storage Lite Plan will be replaced by Free Tier announcement](https://cloud.ibm.com/status/announcement?query=Cloud+Object+Storage+Lite+Plan+will+be+replaced+by+Free+Tier).

## 14 June 2024
{: #cloud-object-storage-jun1424}
{: release-note}

New feature!
:   You can now do full integration with [{{site.data.keyword.cloud_notm}} Metrics Routing](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration) and [{{site.data.keyword.cloud_notm}} Activity Tracker Event Routing](/docs/cloud-object-storage?topic=cloud-object-storage-at) for {{site.data.keyword.cos_full}} buckets.

## 04 June 2024
{: #cloud-object-storage-june0424}
{: release-note}

Aspera available in the Chennai Single Data Center
:   It is now possible to use [Aspera high-speed transfer](/docs/cloud-object-storage?topic=cloud-object-storage-aspera).

## 19 April 2024
{: #cloud-object-storage-apr1924}
{: release-note}

Cloud Functions update
:   {{site.data.keyword.openwhisk}} is deprecated. Existing Functions entities such as actions, triggers, or sequences will continue to run, but as of 28 December 2023, you can’t create new Functions entities. Existing Functions entities are supported until October 2024. Any Functions entities that still exist on that date will be deleted. For more information, see [Deprecation overview](/docs/openwhisk?topic=openwhisk-dep-overview).

## 05 March 2024
{: #cloud-object-storage-mar0524}
{: release-note}

Support available for Code Engine in Madrid
:   Code Engine is supported in the `eu-es` region for Madrid regional using [{{site.data.keyword.cos_full}}](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).

## 29 January 2024
{: #cloud-object-storage-jan2925}
{: release-note}

New feature!
:   You can configure a bucket into a state of Protection Management using the [Resource Configuration API](/apidocs/cos/cos-configuration) if you have approval from IBM Cloud support and Offering Management.

## 30 November 2023
{: #cloud-object-storage-nov3023}
{: release-note}

New feature!
:   You can create IAM policies that control access to individual objects within your bucket using [fine-grained access control](/docs/cloud-object-storage?topic=cloud-object-storage-fgac-iam-access-conditions).

## 30 October 2023
{: #cloud-object-storage-oct3023}
{: release-note}

Activity Tracker and Monitoring update
:   Buckets can be created in the `eu-es` region for Madrid regional using [{{site.data.keyword.at_full_notm}}](/docs/activity-tracker?topic=activity-tracker-getting-started) and [{{site.data.keyword.mon_full}}](/docs/monitoring?topic=monitoring-getting-started).

## 16 October 2023
{: #cloud-object-storage-oct1623}
{: release-note}

Archive available in the Chennai Single Site
:   It is now possible to [archive data](/docs/cloud-object-storage?topic=cloud-object-storage-archive).

## 3 October 2023
{: #cloud-object-storage-oct0323}
{: release-note}

Object Lock available in the Chennai Single Site
:   It is now possible to [lock objects](/docs/cloud-object-storage?topic=cloud-object-storage-ol-overview) to ensure individual object versions are stored in a WORM (Write-Once-Read-Many), non-erasable and non-rewritable manner in the CHE01 single site.

Encryption update
:   Buckets can be created in the `eu-es` region using [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs) managed encryption.

## 22 September 2023
{: #cloud-object-storage-sept2223}
{: release-note}

Encryption update
:   Buckets created using Key Protect managed encryption can now use Key Protect in Madrid regional. Check out [KP Regions and Endpoints](/docs/key-protect?topic=key-protect-regions).

## 14 June 2023
{: #cloud-object-storage-jun1423}
{: release-note}

New location!
:   Buckets can now be created in a Regional (MZR) configuration in Madrid, Spain. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 16 March 2023
{: #cloud-object-storage-mar1623}
{: release-note}

New feature!
:   It is now possible to [lock objects](/docs/cloud-object-storage?topic=cloud-object-storage-ol-overview) to ensure individual object versions are stored in a WORM (Write-Once-Read-Many), non-erasable and non-rewritable manner.

## 26 September 2022
{: #cloud-object-storage-sep2622}
{: release-note}

New feature!
:   It is now possible to create instances [using a One-Rate plan](/docs/cloud-object-storage?topic=cloud-object-storage-onerate) to lower costs and simplify billing for workloads with high levels of egress.

## 21 June 2022
{: #cloud-object-storage-jun2122}
{: release-note}

New feature!
:   It is now possible to configure buckets for automated [replication of objects to a destination bucket](/docs/cloud-object-storage?topic=cloud-object-storage-replication-overview).

## 7 June 2022
{: #cloud-object-storage-jun0722}
{: release-note}

Encryption update
:   Buckets created with [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs) managed encryption can now use [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable) to create retention policies that prevent object deletion or modification.

## 23 May 2022
{: #cloud-object-storage-may2322}
{: release-note}

Encryption update
:   Buckets created using [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs) managed encryption can now use .

## 2 May 2022
{: #cloud-object-storage-may0222}
{: release-note}

New feature!
:   It is now possible so manage access [using context-based restrictions](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall). This offers significant improvements over the existing bucket firewall, and allows for the allowlisting of VPCs and other cloud services, in addition to IP address ranges.

## 5 April 2022
{: #cloud-object-storage-apr0522}
{: release-note}

Encryption update
:   Buckets can be created in the `eu-gb` region using [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs) managed encryption.

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
:   Buckets can be created in a US cross-region configuration using [{{site.data.keyword.hscrypto}}](/docs/cloud-object-storage?topic=cloud-object-storage-hpcs) managed encryption.

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
:   Buckets created using [Key Protect managed encryption](/docs/cloud-object-storage?topic=cloud-object-storage-kp) can now also use [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable) to create retention policies that prevent object deletion or modification.

## 30 August 2021
{: #cloud-object-storage-aug3021}
{: release-note}

New location!
:   Buckets can now be created in a Regional configuration in São Paulo, Brazil. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 12 August 2021
{: #cloud-object-storage-aug1221}
{: release-note}

Encryption update
:   New buckets created in `us` or `ap` Cross Region configuration can now use [Key Protect managed encryption](/docs/cloud-object-storage?topic=cloud-object-storage-kp).

## 7 July 2021
{: #cloud-object-storage-jul721}
{: release-note}

New location!
:   Buckets can now be created in a Regional configuration in Toronto, Canada. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

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
:   Lifecycle actions on [Key Protect](/docs/cloud-object-storage?topic=cloud-object-storage-kp#kp-lifecycle) and [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-key-states) encryption keys can now generate bucket events in Activity Tracker.
:   Buckets can now be encrypted using {{site.data.keyword.hscrypto}} in the US East region.

## 18 August 2020
{: #cloud-object-storage-aug1820}
{: release-note}

New feature!
:   Data can now be [archived](/docs/cloud-object-storage?topic=cloud-object-storage-archive) using an Accelerated class that allows restoration of archived objects in under two hours for an [additional cost](https://www.ibm.com/products/cloud-object-storage/pricing).

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
:   Buckets can now be created in a new [Smart Tier storage class](/docs/cloud-object-storage?topic=cloud-object-storage-billing#smart-tier-pricing) that optimizes costs based on usage patterns.

## 6 December 2019
{: #cloud-object-storage-dec620}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Singapore. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

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
:   Buckets can now be created in a Single Data Center configuration in Paris, France. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

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
:   Users can access and interact with object storage [using the IBM Cloud CLI](/docs/cloud-object-storage?topic=cloud-object-storage-ic-cos-cli).

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
:   IAM policies can now grant [public access](/docs/cloud-object-storage?topic=cloud-object-storage-iam-public-access) to entire buckets.

New location!
:   Buckets can now be created in a Single Data Center configuration in Milan, Italy. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 28 February 2019
{: #cloud-object-storage-feb2819}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in San Jose, USA. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 18 January 2019
{: #cloud-object-storage-jan1819}
{: release-note}

New location!
:   Buckets can now be created in AP Australia region. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 14 December 2018
{: #cloud-object-storage-dec1418}
{: release-note}

New feature!
:   Users can use [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable) to create retention policies that prevent object deletion or modification.

New location!
:   Buckets can now be created in a Single Data Center configuration in Mexico City, Mexico. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 12 November 2018
{: #cloud-object-storage-nov1218}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Montréal, Canada. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 12 October 2018
{: #cloud-object-storage-oct1218}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Seoul, South Korea. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 20 September 2018
{: #cloud-object-storage-sep2018}
{: release-note}

New feature!
:   Users can [archive cold data](/docs/cloud-object-storage?topic=cloud-object-storage-archive) by setting the proper parameters in a bucket lifecycle configuration policy, either using the console, REST API, or a language-specific SDK.

## 18 August 2018
{: #cloud-object-storage-aug1818}
{: release-note}

New locations!
:   Buckets can now be created in a Single Data Center configuration in Sao Paolo, Brazil and Oslo, Norway. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 22 June 2018
{: #cloud-object-storage-jun2218}
{: release-note}

New locations!
:   Buckets can now be created in the EU Germany region. Data stored in these buckets is distributed across three availability zones in the EU Germany region. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) documentation.
:   Buckets can now be created in a Single Data Center configuration in Chennai, India and Amsterdam, Netherlands. This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## 16 March 2018
{: #cloud-object-storage-mar1618}
{: release-note}

New location!
:   Buckets can now be created in an AP Cross Region configuration. Data stored in these buckets is distributed across the Seoul, Tokyo, and Hong Kong data centers. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

New feature!
:   Users can run `SELECT` SQL queries directly against structured data objects using {{site.data.keyword.sqlquery_full}}. More information can be found in the [{{site.data.keyword.sqlquery_short}} documentation](/docs/sql-query?topic=sql-query-overview).

## 7 March 2018
{: #cloud-object-storage-mar718}
{: release-note}

New feature!
:   Users who upload or download files using the web-based console have the option to use Aspera high-speed transfer for these operations via a browser plug-in. This allows for transfers of objects larger than 200MB, and also allows for greater control and visibility of uploads and downloads. Additional information can be found in the [Uploading Data](/docs/cloud-object-storage?topic=cloud-object-storage-aspera) documentation. Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/products/cloud-object-storage).

## 11 February 2018
{: #cloud-object-storage-feb1118}
{: release-note}

New location!
:   Buckets can now be created in a Single Data Center configuration in Toronto, Canada and Melbourne, Australia. This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) documentation.

## 8 August 2017
{: #cloud-object-storage-aug817}
{: release-note}

Introducing {{site.data.keyword.cos_full_notm}}
:  Object Storage is a highly available, durable, and secure platform for storing unstructured data. Unstructured data (sometimes called binary or "blob" data) refers to data that is not highly structured in the manner of a database. Object storage is the most efficient way to store PDFs, media files, database backups, disk images, or even large structured datasets.
