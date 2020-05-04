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

{{site.data.keyword.IBM_notm}} continually updates and modernizes data centers to give you higher levels of performance. Some data centers aren't able to be upgraded, so they must be closed and you must migrate your resources to a different data center.
{:shortdesc}

The [single data center storage location](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints-zone) in **Melbourne, Australia (`mel01`)** will be closed November 30, 2020. The following endpoints will no longer be valid:

- `s3.mel01.cloud-object-storage.appdomain.cloud`
- `s3.private.mel01.cloud-object-storage.appdomain.cloud`
- `s3.direct.mel01.cloud-object-storage.appdomain.cloud` 

Throughout the data center migration process, help is available. To identify your impacted resources, take advantage of special offers, or learn about recommended configurations, use one of the following options to contact the {{site.data.keyword.IBM_notm}} 24x7 Client Success team: 
  * [Live chat](https://www.ibm.com/cloud/data-centers/?focusArea=WCP%20-%20Pooled%20CSM&contactmodule){: external}
  * Phone: (US) 866-597-9687; (EMEA) +31 20 308 0540; (APAC) +65 6622 2231

For more information about which data centers are closing, see [Withdrawal of support for some data centers](/docs/get-support?topic=get-support-dc-migrate). For a list of the data centers that are available, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints).

## Migrating your resources
{: #migrating-your-resources}
 
To avoid any disruption to your service, please complete the following steps before 30 November 2020: 

1. Identify any buckets in the data centers that are set to close. [Extended listing](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) can be used for this purpose, as it will return 'LocationConstraint' values that indicate the location in addition to the storage class of a bucket. For more information, contact the Client Success team [Live chat](https://www.ibm.com/cloud/data-centers/?focusArea=WCP%20-%20Pooled%20CSM&contactmodule){: external}. 
2. [Create your new buckets](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started#gs-create-buckets) in a different data center. 
3. Need something here to advise clients on reconfiguring their bucket policies for features (IAM bucket policies, Activity Tracker, SysDig, Obj Expiry, COS firewall, CDN) before they migrate their data
4. Migrate your data to the new bucket [using Rclone](https://cloud.ibm.com/docs/services/cloud-object-storage?topic=cloud-object-storage-region-copy).
5. To avoid being double billed for the data in your old and new buckets, [empty your old buckets](/docs/basics?topic=cloud-object-storage-deleting-multiple-objects-patterns) and delete them. 





