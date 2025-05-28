---

copyright:
  years: 2025
lastupdated: "2025-05-28"

keywords: bucket, vault, management, configure

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Configuring Backups

{: #bvm-configure}


## Prerequisites for a backup

{: #bvm-prerequisites}


### Versioning enablement

{: #bvm-versioning-enablement}

Buckets must have versioning enabled for them to be configured with a `BackupPolicy`. See [Versioning Objects](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) to read more about how to configure versioning.

### Authorizing a backup

{: #bvm-configure-add-roles-backuppolicy}

You need to create the Service2Service authorization policy between source bucket and target.
{: note}

Buckets require permission to perform the `cloud-object-storage.backup-vault.sync` operation to a `BackupVault`. It is granted by using service-to-service policy, and must be configured even if the bucket and backup vault are in the same account or serviceInstance.

See [Getting Started with IAM](https://test.cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-iam) and [Identity and Access Management actions](https://test.cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) on more details for configuring service-to-service permissions.

## Adding a backup policy to a bucket

{: #bvm-configure-add-backuppolicy}

Backup is configured by adding a `BackupPolicy` to a bucket. Buckets can have a maximum of three `BackupPolicies` at any point in time, with each `BackupPolicy` targeting a unique `BackupVault`. Once a bucket has a `BackupPolicy` set on it, that policy must be deleted before the bucket can be deleted.

### Creating a backup policy

{: #bvm-configure-create-backuppolicy}

Creation of a `BackupPolicy` is done by using a `POST /v1/buckets/{bucket_name}/backup_policies` request. The request requires that the following be specified:

Element                    | Description
---------------------------|----------------------------------------------------------------------------------------------------------------------
`policy_name`                | The name given to the `BackupPolicy` and is used as a means of tracking the `BackupPolicy` and the resulting RecoveryRange.
`target_backup_vault_crn`    | The CRN of the `BackupVault` into which backup data should be written. `BackupVaults` can be located within the same or different accounts or {{site.data.keyword.cos_full_notm}} regions.
`backup_type `               |  The kind of `BackupPolicy` to create. Currently, only **continuous** backups are supported
`initial_retention `               | The initial retention configuration set on the newly created RecoveryRange. Once created the retention configuration is controlled on the `RecoveryRange` directly, independent of what was set initially that uses the `initial_retention` configuration.



When a `BackupPolicy` is configured on a bucket, {{site.data.keyword.cos_full_notm}} starts the one-time process of syncing all current object-versions from the bucket to the `BackupVault`. The `RecoveryRange` is not visible on a `BackupVault` until this process has been completed. Because the number of objects to be synced might be large, this initialization operation might require several hours to complete.

When initialization is completed, any new data written to the bucket is synced to the `RecoveryRange`, which extends the range forward in time. It occurs when the `BackupPolicy` exists and sync operations are successful. Sync operations are designed to be highly resilient and are retried for several days before consistently failing sync operations that trigger the failure of the `BackupPolicy`, after which no sync operations will be attempted. It is important for users to monitor sync operations for failures to help ensure the `BackupPolicy` does not enter a failed state.

You need to monitor backup policies for sync success
{: note}

## Updating a backup policy

{: #bvm-configure-updating-backuppolicy}

Backup policies cannot be modified once they are created. Any changes to the policy must be affected by deleting and re-creating it. This trigger the creation of a new recovery range.

## Deleting or disabling a backup policy

{: #bvm-configure-delete-disable-backuppolicy}

Deleting a Backup Policy does not cause the deletion of any backup data created through that Backup Policy.

## Monitoring

{: #bvm-configure-monitoring}


{{site.data.keyword.cos_full_notm}} provides two mechanisms by which users can monitor BackupPolicies.

## Monitoring backup policies

{: #bvm-configure-monitoring-policies}

The preferred method is by introspecting on the status of the `BackupPolicy`. The BackupPolicy provides more detailed information relative to what is contained on the bucket.



**Example request**

```http
GET /buckets/myBucket/backup_policies HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
```
{: codeblock}

**Example response**

```json
{
  "policy_name": "myBackupPolicy",
  "initial_retention": {
    "delete_after_days": 10
  },
  "backup_type": "continuous",
  "policy_id": "44d3dd41-d616-4d25-911a-9ef7fbf28aef",
  "policy_status": "pending",
  "target_backup_vault_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:1a0ec336-f391-4091-a6fb-5e084a4c56f4:backup-vault:backup-vault-name"
}
```

Status indicator          | Description
--------------------------|----------------------------------------------------------------------------------------------------------------------
pending                   | The policy has been received and preprocessing of the policy has begun.
initializing              | Initial sync of preexisting object-versions is in progress. When in this state, the response contains a `initial_sync_progress` entry that reports the % completeness of initial sync.
active                    | The policy has completed initialization and is operating normally.
degraded                  | Sync operations are failing due to internal server errors that cannot be remediated by any user action. If this state persists, contact support.
action_needed             | Sync operations are failing due to errors that must be remediated by the user. When in this state, the response contains a `error_cause` entry that reports the cause of the error.
failed                    | The sync operations have been observed to fail consistently, and that no further reattempts are made by the {{site.data.keyword.cos_full_notm}} system. This is a terminal state that indicates that the `BackupPolicy` will no longer sync new data to the `BackupVault`. To recover from this state, the user must delete the `BackupPolicy` and re-create it.



## What is Synced

{: #bvm-configure-what-is-synced}

### The following are synced to a `BackupVault`:

{: #bvm-configure-what-is-synced}

- All current object-versions on a bucket, at the time of setting a BackupPolicy
- New object-versions written to the bucket after setting a BackupPolicy
- Object deletes that occurs on the bucket after setting a BackupPolicy
- Object-tags set on object-versions

### The following is not synced to a `BackupVault`:

{: #bvm-configure-what-is-not-synced}

- Bucket configuration (such as encryption, activity-tracking, permissions, and so on)
- Object-Lock, Archive and Object-ACL state
- Delete operations triggered by Lifecycle Policies
- Version-Delete operations (that is, deletion of a specific version)
- Objects written when using S3 Replication
- Objects written when using a Backup Restore operation
- Objects written directly to Archive or Archived at the time of sync
- Objects encrypted when using SSE-C

## Version delete interactions

{: #bvm-configure-version-delete-interactions}

When versioning is enabled on a bucket, S3 object DELETE operations are nondestructive operations that create a delete marker that becomes the new current version of the object. The previously current version becomes a retained, noncurrent version preserved in version history. Delete marker creation is supported by Backup.

Version-Delete operations are an S3 object DELETE operation where a versionId is specified. It is a destructive operation that removes the object version from versioning history. Backup provides limited support for these operations.

If a version-delete occurs for a version that has not yet been synced to the BackupVault, then the `BackupPolicy` cannot provide backup coverage for any point-in-time after the object was created. It causes the `BackupPolicy` to fail. In order to recover from this condition, the user must delete the BackupPolicy and re-create it.

Version-delete of a current-version (that is, the most recent version that exists for a given object) will not be synced to the `BackupVault` and Restore operations for any point-in-time after this operation has been performed will not reflect the result of the `version-delete` operation.

Version-delete operations for noncurrent versions (that is, versions retained in versioning history) may be performed on versions that have already been synced to the `BackupVault`. Such operations can be performed after allowing a sufficient amount of time to pass after the version has been written. It is typically less than 1 hour but might be longer if the `BackupPolicy` is undergoing sync delays (such as those due to an IAM misconfiguration). These operations can be performed directly, or when using an S3 Lifecycle Expiration policy that deletes noncurrent versions once they are older than some number of days.

{{site.data.keyword.cos_short}} recommends that users should grant access to source-buckets through a custom IAM role that does not include the`cloud-object-storage.object.delete_version` or `cloud-object-storage.bucketput_lifecycle` actions that allow for potentially harmful version-delete operations.
