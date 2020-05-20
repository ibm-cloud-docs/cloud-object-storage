---

copyright:
  years: 2020
lastupdated: "2020-04-30"

keywords: decommission, migrate

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

# Migrating resources to a different data center
{: #migrate-data-center}

{{site.data.keyword.IBM_notm}} continually updates and modernizes data centers to give you higher levels of performance. Some data centers aren't able to be upgraded, so they must be closed.  To avoid disruption to your service, your action is required to migrate your resources to a different data center.  For a list of the available data centers, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints).

For additional information about data center closings, see [Withdrawal of support for some data centers](/docs/get-support?topic=get-support-dc-migrate). 

{:shortdesc}

The [single data center storage location](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints-zone) in **Melbourne, Australia (`mel01`)** will be closed November 30, 2020. The following endpoints will no longer be valid:

- `s3.mel01.cloud-object-storage.appdomain.cloud`
- `s3.private.mel01.cloud-object-storage.appdomain.cloud`
- `s3.direct.mel01.cloud-object-storage.appdomain.cloud` 

Throughout the data center migration process, help is available. To identify your impacted resources, take advantage of special offers, or learn about recommended configurations, use one of the following options to contact the {{site.data.keyword.IBM_notm}} 24x7 Client Success team: 
  * [Live chat](https://www.ibm.com/cloud/data-centers/?focusArea=WCP%20-%20Pooled%20CSM&contactmodule){: external}
  * Phone: (US) 866-597-9687; (EMEA) +31 20 308 0540; (APAC) +65 6622 2231


## Migrating your resources
{: #migrating-your-resources}
 
To avoid any disruption to your service, please complete the following steps before 30 November 2020: 

1. Identify your buckets in the data centers that are set to close. [Extended listing](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) can be used for this purpose, as it will return 'LocationConstraint' values that indicate the location in addition to the storage class of a bucket. For more information, contact the Client Success team [Live chat](https://www.ibm.com/cloud/data-centers/?focusArea=WCP%20-%20Pooled%20CSM&contactmodule){: external}. 
2. [Create your new buckets](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started#gs-create-buckets) in a different data center. Available data centers can be found here: [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints).
3. Once you've created your destination bucket you will need to configure your feature access policies prior to migrating your data.  Feature configuration and access policies documentation can be viewed at the IBM Cloud portal pages listed below:
 * IBM Cloud Identity and Access Management - IAM (https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-iam)
 * Activity Tracker (https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-at)
 * SysDig Monitoring (https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration)
 * Object Expiry (https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-expiry)
 * Cloud Object Storage Firewall (https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall)
 * Content Delivery Network - CDN (https://cloud.ibm.com/docs/cis?topic=cis-resolve-override-cos)
4. Migrate your data to the new bucket [using Rclone](https://cloud.ibm.com/docs/services/cloud-object-storage?topic=cloud-object-storage-region-copy).
5. To avoid being double billed for data in your old and new buckets, [empty your old buckets](/docs/basics?topic=cloud-object-storage-deleting-multiple-objects-patterns) and delete them. 


We're here to help! Please email us at talk-to-cos@wwpdl.vnet.ibm.com, if you have any questions.


