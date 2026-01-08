---

copyright:
  years: 2025
lastupdated: "2026-01-08"

keywords: bucket, vault, management, restore

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Restore

{: #bvm-restore}

Backup data is stored in a Backup Vault in the form of Recovery Ranges. Each Recovery Range represents a stretch of time for which backup coverage exists for a specific {{site.data.keyword.cos_full_notm}} bucket. For more information on Recovery Ranges and how they are created, see [recovery ranges](docs-draft/cloud-object-storage?topic=cloud-object-storage-bvm-recovery-ranges). When using the backup data contained within a Recovery Range, it must be written to some {{site.data.keyword.cos_full_notm}} bucket by using a  `Restore` operation.


## Prerequisites to Restore

### Target bucket configuration

- The target bucket must have [versioning](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) enabled. It can be validated by using the `GET Bucket?versioning` S3 API request.



- The target bucket must not have [legacy firewall](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall#firewall-legacy-about) rules configured. It can be validated by using the `GET bucket request` in the RC API request.



### Source backup vault configuration

Backup vaults require permission to perform the "cloud-object-storage.bucket.restore_sync" operation to a target bucket. It is granted by using service-to-service policy, and must be configured even if the backup vault and bucket are in the same account or service instance.

See [Getting Started with IAM](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-iam) and [Identity and Access Management actions](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) on more details for configuring service-to-service permissions.


### Concurrent restore limit

A Backup Vault can service at most 3 restore operations at a time.

## Triggering restores

Restore requests require that the following be specified:

Element                    | Description
---------------------------|----------------------------------------------------------------------------------------------------------------------
`recovery_range_id`        | This is the UUID of the `RecoveryRange` to restore from.
`restore_type`             | This is the kind of Restore to perform. Currently, only `in_place` restores are supported.
`target_resource_crn`      | This is the CRN of the target bucket to write restored objects into.
`restore_point_in_time`    | This is the point-in-time to restore to. This value must be between the start and end times published for the `RecoveryRange` being restored from.



A restore operation will write all the current-versions of objects, including Delete Markers, at the requested restore point-in-time to the target bucket. Restored objects will have the same versionId, LastModifiedTime and Etag as on the source bucket. The most recent object-tags for a version will also be restored. Note that Backup only stores the most recent set of object-tags, meaning that the object-tags on restored objects may not correspond with the tag-set on the object at the requested point-in-time. Rather, they will correspond to the latest object-tags present on the source bucket.

An "in_place" restore is a non-destructive restore operation that syncs object versions to the target bucket without deleting any object versions that may already exist on the target bucket. If an object is being restored to a bucket where an object with the same name already exists, then the restored object is written into versioning history based on the LastModifiedTime of the restored object. This means that restored object versions may not be written as current-versions.

S3 object GET and HEAD requests for objects written to the target bucket via restore shall respond with `x-ibm-backup-restore-time=<timestamp>` and `x-ibm-backup-restore-id=<restore_id>` headers that report the time at which the object was restored to the bucket, and the UUID of the restore operation that it was a part of. Objects written to the target bucket will inherit whatever default encryption or object-lock state that is set on the target bucket.

## Monitoring restore

{: #bvm-restore-monitor}

Once a restore is initiated, {{site.data.keyword.cos_full_notm}} will begin the process of syncing the required objects to the target bucket. Because the number of objects to be synced may be large, restore operations may require several hours to complete. You can can monitor the progress of a restore operation using the `GET Restore request` on the `BackupVault`. This request responds with a `restore_status` field that can take the following values:


Value           | Description
----------------|----------------------------------------------------------------------------------------------------------------------
Initializing    | This status indicates that the Restore request has been received and pre-processing of the Restore has begun. This is a short-lived state that only occurs immediately after requesting a Restore.
Running         | This status indicates that the Restore has completed initialization and is currently syncing objects to the target bucket. While in this state, a `restore_percent_progress` response element is included that reports the percent progress of the Restore.
Complete        | This status indicates that the Restore operation has completed successfully.
Failed          | This status indicates that the Restore operation has encountered a terminal failure. While in this state, an `error_cause` response element is included which indicates the cause of the failure.
