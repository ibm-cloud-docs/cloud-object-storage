---

copyright:
  years: 2020, 2023
lastupdated: "2023-05-26"

keywords: decommission, migrate

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Migrating resources to a different data center
{: #migrate-data-center}

IBM Cloud invests significantly in data center infrastructure. These investments include rolling out newer data centers and multizone regions (MZRs) designed to deliver a more resilient architecture with higher levels of network throughput and redundancy.

Part of this modernization strategy is to close older data centers that are unsuitable for upgrading. As this transition approaches, help is available to assist you in your migration to modern data centers. For a list of the available data centers, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints).

For additional information about data center closings, see [Withdrawal of support for some data centers](/docs/get-support?topic=get-support-dc-closure).

{:shortdesc}

To identify your impacted resources, take advantage of special offers, or learn about recommended configurations, use one of the following options to contact the {{site.data.keyword.IBM_notm}} 24x7 Client Success team:
   * [Live chat](https://www.ibm.com/cloud/data-centers/?focusArea=WCP%20-%20Pooled%20CSM&contactmodule){: external} (Click 'Lets Talk' in the lower right corner)
   * Phone: (US) 866-597-9687; (EMEA) +31 20 308 0540; (APAC) +65 6622 2231


## Migrating your resources
{: #migrating-your-resources}

To avoid any disruption to your service, please complete the following steps **before any announced deadlines**:

1. Identify your buckets in the data centers that are set to close through viewing your COS UI buckets page. Also, [Extended listing](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) can be used for this purpose, as it will return 'LocationConstraint' values that indicate the location in addition to the storage class of a bucket. For more information, contact the Client Success team [Live chat](https://www.ibm.com/cloud/data-centers/?focusArea=WCP%20-%20Pooled%20CSM&contactmodule){: external}.
2. Migrate your data to the new destination bucket [using Rclone](https://cloud.ibm.com/docs/services/cloud-object-storage?topic=cloud-object-storage-region-copy).
3. To avoid being double billed for data in your old and new buckets, [empty your old buckets](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-deleting-multiple-objects-patterns) and delete them.

### Identifying buckets that require migration
{: #migrating-your-resources-identify}

You can use the IBM Cloud CLI to identify buckets located in a given location.

1. First, ensure you have both the [IBM Cloud CLI](/docs/cli) and [COS plug-in](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli) installed.
2. After you are logged into the CLI, you can use the `ibmcloud cos buckets-extended` command to list all of the buckets in a given instance.
3. You can filter the results using `grep`. For example, `ibmcloud cos buckets-extended | grep mon01` will return all buckets that are located in the `mon01` single data center.




