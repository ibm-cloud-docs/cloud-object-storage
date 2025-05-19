---

copyright:
  years: 2017, 2020
lastupdated: "2020-11-02"

keywords: activity tracker, actions, api

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# IAM and Activity Tracker actions by API
{: #at-iam}

Find detailed information on {{site.data.keyword.cloud_notm}} events from IAM and {{site.data.keyword.at_full}} actions, listed here by API method.
{: shortdesc}

## Resource Configuration API
{: #at-iam-config}

The first table details the API for configuring {{site.data.keyword.cos_full}} buckets: [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration#introduction)

*  Note the endpoint URL: `https://config.cloud-object-storage.cloud.ibm.com/v1`

| Action                                    | Method                                                          | IAM Action                                                                                                                                                                                                             | Activity Tracker action                              |
|-------------------------------------------|-----------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| Returns metadata for the specified bucket | `GET {endpoint}/b/{bucket}`                                     | `cloud-object-storage.bucket.list_bucket_crn`, </br> `cloud-object-storage.bucket.get_firewall`, </br> `cloud-object-storage.bucket.get_activity_tracking`, </br> `cloud-object-storage.bucket.get_metrics_monitoring` | `cloud-object-storage.resource-configuration.read`   |
| Make changes to a bucket's configuration  | `PATCH {endpoint}/b/{bucket}`                                   | `cloud-object-storage.bucket.put_firewall`, </br> `cloud-object-storage.bucket.put_activity_tracking`, </br> `cloud-object-storage.bucket.put_metrics_monitoring`                                                      | `cloud-object-storage.resource-configuration.update` |
| Create a backup policy on a bucket        | `POST /v1/buckets/{bucket_name}/backup_policies`                | `cloud-object-storage.bucket.post_backup_policy`                                                                                                                                                                       | `cloud-object-storage.bucket-backup-policy.create`   |  
| Read a backup policy on a bucket          | `GET /v1/buckets/{bucket_name}/backup_policies/{policy_id}`     | `cloud-object-storage.bucket.get_backup_policy`                                                                                                                                                                        | `cloud-object-storage.bucket-backup-policy.read`     |   
| List all the backup policies on a bucket  | `GET /v1/buckets/{bucket_name}/backup_policies`                 | `cloud-object-storage.bucket.list_backup_policies`                                                                                                                                                                     | `cloud-object-storage.bucket-backup-policy.list`     |    
| Delete a backup policy on a bucket        | `DELETE /v1/buckets/{bucket_name}/backup_policies/{policy_id}`  | `cloud-object-storage.bucket.delete_backup_policy`                                                                                                                                                                     | `cloud-object-storage.bucket-backup-policy.delete`   |   
{: caption="RC API actions" caption-side="top"}
   


| Action                                    | Method                                                          | IAM Action                                                                                                                                                                                                             | Activity Tracker action                              |
|-------------------------------------------|-----------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| Get all backup vaults in a service instance    |  `GET /v1/backup_vaults`                                                           | `cloud-object-storage.account.list_account_backup_vaults`                                                                                                                                                                              |  `cloud-object-storage.backup-vault.list`                 |
| Create a backup vault                          |  `POST /v1/backup_vaults`                                                          | `cloud-object-storage.backup-vault.post_backup_vault`, </br> `cloud-object-storage.backup-vault.put_activity_tracking`, </br>`cloud-object-storage.backup-vault.put_metrics_monitoring`                                                |  `cloud-object-storage.backup-vault.create`               |
| View configuration on a backup vault           |  `GET /v1/backup_vaults/{backup_vault_name}`                                       | `cloud-object-storage.backup-vault.get_basic`, </br> `cloud-object-storage.backup-vault.get_activity_tracking`, </br> `cloud-object-storage.backup-vault.get_metrics_monitoring`, </br> `cloud-object-storage.backup-vault.get_crk_id` |  `cloud-object-storage.backup-vault-configuration.read`   |
| Update the configuration on a backup vault     |  `PATCH /v1/backup_vaults/{backup_vault_name}`                                     | `cloud-object-storage.backup-vault.put_activity_tracking`, </br>`cloud-object-storage.backup-vault.put_metrics_monitoring`                                                                                                             |  `cloud-object-storage.backup-vault-configuration.update` |
| Delete a backup vault                          |  `DELETE /v1/backup_vaults/{backup_vault_name}`                                    | `cloud-object-storage.backup-vault.delete_backup_vault`                                                                                                                                                                                |  `cloud-object-storage.backup-vault.delete`               |
| List recovery ranges in a backup vault         |  `GET /v1/backup_vaults/{backup_vault_name}/recovery_ranges`                       | `cloud-object-storage.backup-vault.list_recovery_ranges`                                                                                                                                                                               |  `cloud-object-storage.backup-recovery-range.list`        |
| View a recovery range                          |  `GET /v1/backup_vaults/{backup_vault_name}/recovery_ranges/{recovery_range_id}`   | `cloud-object-storage.backup-vault.get_recovery_range`                                                                                                                                                                                 |  `cloud-object-storage.backup-recovery-range.read`        |
| Update a recovery range configuration          |  `PATCH /v1/backup_vaults/{backup_vault_name}/recovery_ranges/{recovery_range_id}` | `cloud-object-storage.backup-vault.put_retention`                                                                                                                                                                                      |  `cloud-object-storage.backup-recovery-range.update`      |
| Create a new restore operation                 |  `POST /v1/backup_vaults/{backup_vault_name}/restores`                             | `cloud-object-storage.backup-vault.post_restore`                                                                                                                                                                                       |  `cloud-object-storage.backup-restore.create`             |
| View all restore operations on a backup vault  |  `GET /v1/backup_vaults/{backup_vault_name}/restores`                              | `cloud-object-storage.backup-vault.list_restores`                                                                                                                                                                                      |  `cloud-object-storage.backup-restore.list`               |
| View a restore opration                        |  `GET /v1/backup_vaults/{backup_vault_name}/restores/{restore_id}`                 | `cloud-object-storage.backup-vault.get_restore`                                                                                                                                                                                        |  `cloud-object-storage.backup-restore.read`               |




## S3 API
{: #at-iam-s3}

The next table describes the API for reading and writing objects as defined in the [COS Compatibility S3 API](https://cloud.ibm.com/apidocs/cos/cos-compatibility#introduction)
* Note that the endpoint URL for S3 operations differs by region: [Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints)

| Action                                              | Method                                                                      | IAM Action                                                                          | Activity Tracker action                                  |
|-----------------------------------------------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------------------------|----------------------------------------------------------|
| List buckets                                        | `GET {endpoint}/`                                                           | `cloud-object-storage.account.get_account_buckets`                                  | `cloud-object-storage.instance.list`                     |
| Create a bucket                                     | `PUT {endpoint}/{bucket}`                                                   | `cloud-object-storage.bucket.put_bucket`                                            | `cloud-object-storage.bucket.create`                     |
| List objects                                        | `GET {endpoint}/{bucket}`                                                   | `cloud-object-storage.bucket.get`                                                   | `cloud-object-storage.bucket.list`                       |
| Check a bucket's headers                            | `HEAD {endpoint}/{bucket}`                                                  | `cloud-object-storage.bucket.head`, </br> `cloud-object-storage.bucket.list_crk_id` | `cloud-object-storage.bucket-metadata.read`              |
| Delete a bucket                                     | `DELETE {endpoint}/{bucket}`                                                | `cloud-object-storage.bucket.delete_bucket`                                         | `cloud-object-storage.bucket.delete`                     |
| Upload an object                                    | `PUT {endpoint}/{bucket}/{key}`                                             | `cloud-object-storage.object.put`                                                   | `cloud-object-storage.object.create`                     |
| Download an object                                  | `GET {endpoint}/{bucket}/{key}`                                             | `cloud-object-storage.object.get`                                                   | `cloud-object-storage.object.read`                       |
| Check an object's headers                           | `HEAD {endpoint}/{Bucket}/{key}`                                            | `cloud-object-storage.object.head`                                                  | `cloud-object-storage.object-metadata.read`              |
| Delete an object                                    | `DELETE {endpoint}/{bucket}/{key}`                                          | `cloud-object-storage.object.delete`                                                | `cloud-object-storage.object.delete`                     |
| Add a CORS configuration                            | `PUT {endpoint}/{bucket}?cors`                                              | `cloud-object-storage.bucket.put_cors`                                              | `cloud-object-storage.bucket-cors.create`                |
| Read a CORS configuration                           | `GET {endpoint}/{bucket}?cors`                                              | `cloud-object-storage.bucket.get_cors`                                              | `cloud-object-storage.bucket-cors.read`                  |
| Delete a CORS configuration                         | `DELETE {endpoint}/{bucket}?cors`                                           | `cloud-object-storage.bucket.delete_cors`                                           | `cloud-object-storage.bucket-cors.delete`                |
| Add/edit a bucket's lifecycle configuration         | `PUT {endpoint}/{bucket}?lifecycle`                                         | `cloud-object-storage.bucket.put_lifecycle`                                         | `cloud-object-storage.bucket-lifecycle.create`           |
| Read a bucket's lifecycle configuration             | `GET {endpoint}/{bucket}?lifecycle`                                         | `cloud-object-storage.bucket.get_lifecycle`                                         | `cloud-object-storage.bucket-lifecycle.read`             |
| Delete a bucket's lifecycle configuration           | `DELETE {endpoint}/{bucket}?lifecycle`                                      | `cloud-object-storage.bucket.put_lifecycle`                                         | `cloud-object-storage.bucket-lifecycle.delete`           |
| Add/edit/remove a bucket's Immutable Storage policy | `PUT {endpoint}/{bucket}?protection`                                        | `cloud-object-storage.bucket.put_protection`                                        | `cloud-object-storage.bucket-retention.create`           |
| Read a bucket's Immutable Storage policy            | `GET {endpoint}/{bucket}?protection`                                        | `cloud-object-storage.bucket.get_protection`                                        | `cloud-object-storage.bucket-retention.read`             |
| Initiate a multipart upload                         | `POST {endpoint}/{bucket}/{key}?uploads`                                    | `cloud-object-storage.object.post_initiate_upload`                                  | `cloud-object-storage.object-multipart.start`            |
| Upload a part                                       | `PUT {endpoint}/{bucket}/{key}?uploadId={uploadId}&partNumber={partNumber}` | `cloud-object-storage.object.put_part`                                              | `cloud-object-storage.object-multipart.create`           |
| Complete a multipart upload                         | `POST {endpoint}/{bucket}/{key}?uploadID={uploadId}`                        | `cloud-object-storage.object.post_initiate_upload`                                  | `cloud-object-storage.object-multipart.complete`         |
| Add a public ACL block configuration                | `PUT {endpoint}/{bucket}?publicAccessBlock`                                 | `cloud-object-storage.bucket.put_public_access_block`                               | `cloud-object-storage.bucket-public-access-block.create` |
| Read a public ACL block configuration               | `GET {endpoint}/{bucket}?publicAccessBlock`                                 | `cloud-object-storage.bucket.get_public_access_block`                               | `cloud-object-storage.bucket-public-access-block.read`   |
| Delete a public ACL block configuration             | `DELETE {endpoint}/{bucket}?publicAccessBlock`                              | `cloud-object-storage.bucket.delete_public_access_block`                            | `cloud-object-storage.bucket-public-access-block.delete` |
{: caption="S3 API actions" caption-side="top"}
