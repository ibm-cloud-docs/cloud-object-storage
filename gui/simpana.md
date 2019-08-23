---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-23"

keywords: gui, archive, simpana

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


# Use CommVault Simpana to archive data
{: #commvault}

CommVault Simpana integrates with the Archive tier of {{site.data.keyword.cos_full_notm}}. For more information about Simpana, see: [CommVault Simpana documentation](https://documentation.commvault.com/commvault/).
{:shortdesc: .shortdesc}

For more information about IBM COS Infrastructure Archive, see [How to: Archive Data](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive).

## Integration steps
{: #commvault-integration}

1.	From the Simpana console, create an Amazon S3 cloud storage library. 

2. Ensure that the Service Host points to the endpoint. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Simpana provisions buckets at this step or it can consume provisioned buckets. 

3.	Create a policy on the bucket. You can use the AWS CLI, SDKs or the web console to create the policy. An example of a policy follows:

```shell
{
  "Rules": [
    {
      "ID": "CommVault",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transitions": [
        {
        "Days": 0,
        "StorageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

### Associate the policy with the bucket
{: #commvault-assign-policy}

1. Execute the following CLI command:

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	Create a storage policy with Simpana and associate the storage policy to the Cloud Storage library that you created in the first step. A storage policy governs the way Simpana interacts with COS for backup transfers. A policy overview can be found [here](https://documentation.commvault.com/commvault/v11/article?p=13804.htm).

3.	Create a backup set and associate the backup set to the storage policy created in the previous step. The backup set overview can be found [here](https://documentation.commvault.com/commvault/v11/article?p=11666.htm)

## Performing Backups
{: #commvault-backup}

You can initiate your backup to the bucket with the policy. and perform backups to {{site.data.keyword.cos_full_notm}}. More information on Simpana backups is available [here](https://documentation.commvault.com/commvault/v11/article?p=11677.htm). Backup contents transition to the Archive tier based on the policy configured on the bucket.

## Performing Restores
{: #commvault-restore}

You can restore backup contents from {{site.data.keyword.cos_full_notm}}. More information on Simpana restore can be found [here](https://documentation.commvault.com/commvault/v11/article?p=12867.htm).

### Configure Simpana to automatically restore objects from the Archive tier
{: #commvault-auto-restore}

1. Create a task that triggers {{site.data.keyword.cos_full_notm}} restore when you restore a backup from COS. See the [CommVault Simpana documentation](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053) to configure.

2. Restore backed up contents from the Archive tier to its original tier through a cloud storage recall task. This task is executed once Simpana receives the return code from {{site.data.keyword.cos_full_notm}}. More information on Archive recall can be found [here](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).

3. Once the restoration (from the Archive tier to its original tier) is complete, Simpana reads the contents and writes to its original or configured location.
