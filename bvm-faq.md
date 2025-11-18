---

copyright:
  years: 2025
lastupdated: "2025-08-14"

keywords: bucket, vault, management, provisioning

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - Backing up buckets

{: #faq-backup-vault-mgmt}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## What is the maximum amount of data that you can store in a single `BackupVault`?

{: #faq-backup-vault-mgmt-max-data}
{: faq}

There is no limit on the amount of data or the number of recovery ranges that can be stored in a single backup vault.

## How many backup vaults can I provision?

{: #faq-backup-vault-mgmt-provisions}
{: faq}

Each {{site.data.keyword.cos_short}} instance can contain up to 1000 buckets and backup vaults combined.

## Is there a limit on the number of concurrent restore operations that can occur?

{: #faq-backup-vault-mgmt-operations-concurrent}
{: faq}

Yes. Each backup vault can service a maximum of 3 active restore operations.

## Can I restore partial paths, for example a prefix, or individual objects?

{: #faq-backup-vault-mgmt-operations}
{: faq}

No. Restore operations always sync all the objects that were current at the requested restore point-in-time.

## What operations are supported on a bucket after a Backup Policy has been set?

{: #faq-backup-vault-mgmt-operations-supported}
{: faq}

All operations on a bucket are supported, except for the disabling versioning and deletion of [object-versions](/docs/cloud-object-storage?topic=cloud-object-storage-versioning).

Deletion of an object before it is synced to the `BackupVault` triggers an unrecoverable failure that causes the Backup Policy to fail permanently. For more information, go to [Configure backups](/docs/cloud-object-storage?topic=cloud-object-storage-configuring-backups).

## What are the user's responsibilities with Backup?

{: #faq-backup-vault-mgmt-responsibilities}
{: faq}

It is the user's responsibility to monitor for and remediate `BackupPolicy` sync failures. Users are recommended to periodically check `BackupPolicy` state no less than once per week.

## How do I make my restored bucket identical to the source?

{: #faq-backup-vault-mgmt-identical-source}
{: faq}

Not all actions performed on the source-bucket are synced to the `BackupVault`. If Restore operations need to result in an identical copy of the source bucket, then it is the user's responsibility to not perform actions that are not synced. For more information on what is synced, go to [Configure backups](/docs/cloud-object-storage?topic=cloud-object-storage-configuring-backups).

It is our recommendation that the Restore target bucket be empty in order to avoid conflicts with restored objects. The Restore target bucket should also not have a `BackupPolicy` enabled on it. Restore to the source bucket is possible, but it is recommended that the `BackupPolicy` be deleted before beginning a Restore and then reenabled once Restore is complete.
