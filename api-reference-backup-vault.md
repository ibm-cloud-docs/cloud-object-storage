---

copyright:
  years: 2025
lastupdated: "2025-05-28"

keywords: rest, api, backup, vault

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Backup vault operations
{: #compatibility-api-backup-vault-operations}

The modern capabilities of {{site.data.keyword.cos_full}} are conveniently available through a RESTful API. Operations and methods that are used to interact with buckets (where objects are stored) are documented here.
{: shortdesc}

For more information about the permissions and access, see [Bucket permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions).
{: tip}

## A note about Access/Secret Key (HMAC) authentication
{: #backup-vault-operations-hmac}

When authenticating to your instance of {{site.data.keyword.cos_full}} by [using HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main), you need the information that is represented in Table 1 when [constructing an HMAC signature](/docs/cloud-object-storage?topic=cloud-object-storage-hmac-signature).

| Key          | Value                                                     | Example                            |
|--------------|-----------------------------------------------------------|------------------------------------|
| {access_key} | Access key that is assigned to your Service Credential    | `cf4965cebe074720a4929759f57e1214` |
| {date}       | The formatted date of your request (`yyyymmdd`)           | `20180613`                         |
| {region}     | The location code for your endpoint                       | `us-standard`                      |
| {signature}  | The hash that is created by using the secret key, location, and date |`ffe2b6e18f9dcc41f593f4dbb39882a6bb4d26a73a04326e62a8d344e07c1a3e`|
| {timestamp}  | The formatted date and time of your request               | `20180614T001804Z`                 |
{: caption="HMAC signature components"}




## Backup Vault Management Operations
{: #backup-vault-management-operations}

The {{site.data.keyword.cos_full}} Backup Vault API provides operations for creating, managing, and interacting with backup vaults. Backup vaults are used to store backups of buckets and their objects.
{: shortdesc}

For more information about permissions and access, see [Backup Vault permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-backup-vault-permissions).
{: tip}

### List Backup Vaults
{: #list-backup-vaults}

A `GET` request to the `/backup_vaults` endpoint returns a list of backup vaults associated with the specified service instance.

| Header                    | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `service_instance_id`     | String | Yes       | The ID of the service instance to list backup vaults for.
{: caption="Headers" caption-side="bottom"}

**Syntax**

```sh
GET https://{endpoint}/backup_vaults
```
{: codeblock}

**Example request**

```http
GET /backup_vaults HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
service_instance_id: {service_instance_id}
```
{: codeblock}

**Example response**

```json
{
  "next": {
    "token": "SWYwNTZMflFmZUgheGNcNHMuIm9OMTFz",
    "href": "config.cloud-object-storage.cloud.ibm.com/v1/backup_vaults?token=1znNRhmyheG-pm6mlMKFsoxcKXFnW1hgBb2_6ZReqHduL3rivfeBWHeyL-OaJvtfVZjKrgRVmODtOdVeQEXkKCxDWpA5hRWuv9WgTPR2eLIN9fbEaTxqdNO4dp0sQeHh4HO-13nTgRV_1QGiR1QFzREHX3wzquLn9o_1DZZAEP-vAY2V8d6BCA1z2krhpMfswKJ8r6UynUc0W5JHSH4auwJ6jC-h8sc8NEcQpHUx2Ep4oxebmtJMaycQSjLA3J2fdcYuysKq3YIcszDnPte0zwr25x5_GmBrxlgd2oesjfyTaLHfIg1g4TD0uH9m9aEAv"
  },
  "backup_vaults": [
    "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:backup-vault:otherbackupvault",
    "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:backup-vault:somebackupvault"
  ]
}
```

### Create a Backup Vault
{: #create-backup-vault}

A `POST` request to the `/backup_vaults` endpoint creates a new backup vault.

| Header                    | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `Authorization`           | String | Yes       | Bearer token for authentication.
| `service_instance_id`     | String | Yes       | The ID of the service instance to list backup vaults for.
{: caption="Headers" caption-side="bottom"}

**Syntax**

```sh
POST https://{endpoint}/backup_vaults
```
{: codeblock}

**Request body**

```json
{
  "backup_vault_name": "myBackupVault",
  "region": "us-south",
  "sse_kp_customer_root_key_crn": "crn:v1:bluemix:public:kms:us-south:a/123456:key/abcd1234"
}
```

**Example request**

```http
POST /backup_vaults HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com

{
  "activity_tracking": {
    "management_events": true
  },
  "metrics_monitoring": {
    "usage_metrics_enabled": true
  },
  "backup_vault_name": "lzuuahcf7ocayou4jrr4",
  "region": "-v5W5jEUSwxnItfY583Iymb63CFvAf",
  "sse_kp_customer_root_key_crn": "u1_0eoiRRzyW.XOKVn16oKQGFtfuSPi8KzWosGFW6sIMYduswlbXuZOQtHn7D"
}
```
{: codeblock}

**Example response**

```json
{
  "crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4-e6a7-40e3-8660-20847e525436:backup-vault:mybackupvault",
  "service_instance_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436::",
  "time_created": "2024-06-02T12:00:00.000Z",
  "time_updated": "2024-06-02T12:00:00.000Z",
  "bytes_used": 0,
  "region": "us-south",
  "backup_vault_name": "mybackupvault"
}
```

### Get Backup Vault Details
{: #get-backup-vault}

A `GET` request to the `/backup_vaults/{backup_vault_name}` endpoint retrieves the config for a specific backup vault.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
GET https://{endpoint}/backup_vaults/{backup_vault_name}
```
{: codeblock}

**Example request**

```http
GET /backup_vaults/myBackupVault HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
```
{: codeblock}

**Example response**

```json
{
  "crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4-e6a7-40e3-8660-20847e525436:backup-vault:mybackupvault",
  "service_instance_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436::",
  "time_created": "2024-06-02T12:00:00.000Z",
  "time_updated": "2024-06-02T12:00:00.000Z",
  "bytes_used": 0,
  "region": "us-south",
  "backup_vault_name": "mybackupvault"
}
```

### Update a Backup Vault
{: #update-backup-vault}

A `PATCH` request to the `/backup_vaults/{backup_vault_name}` endpoint updates the config on a specific backup vault.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
PATCH https://{endpoint}/backup_vaults/{backup_vault_name}
```
{: codeblock}

**Example request**

```http
GET /backup_vaults/myBackupVault HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com

{
  "activity_tracking": {
    "management_events": true
  },
  "metrics_monitoring": {
    "usage_metrics_enabled": true
  }
}
```
{: codeblock}

**Example response**

```json
{
  "crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4-e6a7-40e3-8660-20847e525436:backup-vault:mybackupvault",
  "service_instance_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436::",
  "time_created": "2024-06-02T12:00:00.000Z",
  "time_updated": "2024-06-02T12:00:00.000Z",
  "bytes_used": 0,
  "region": "us-south",
  "backup_vault_name": "mybackupvault"
}
```

### Delete a Backup Vault
{: #delete-backup-vault}

A `DELETE` request to the `/backup_vaults/{backup_vault_name}` endpoint deletes a backup vault. The vault must be empty before deletion.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
DELETE https://{endpoint}/backup_vaults/{backup_vault_name}
```
{: codeblock}

**Example request**

```http
DELETE /backup_vaults/myBackupVault HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
```
{: codeblock}

**Example response**

```http
HTTP/1.1 204 No Content
```

---

For more information about backup vault operations, see [Backup vault management](/docs/cloud-object-storage?topic=cloud-object-storage-bvm-overview).


## Recovery Range operations
{: #recovery-range-operations}

The {{site.data.keyword.cos_full}} Recovery Range API provides operations for listing and retrieving recovery ranges. Recovery ranges represent the time intervals during which data in a backup vault can be restored.
{: shortdesc}

For more information about permissions and access, see [Recovery Range permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-recovery-range-permissions).
{: tip}

---

### List Recovery Ranges
{: #list-recovery-ranges}

A `GET` request to the `/backup_vaults/{backup_vault_name}/recovery_ranges` endpoint retrieves all recovery ranges for a specific backup vault.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
{: caption="Path parameters" caption-side="bottom"}

| Query Parameter           | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `source_resource_crn`     | String | No        | Filters recovery ranges by the source resource CRN.
| `token`                   | String | No        | Continuation token for paginated results.
{: caption="Query parameters" caption-side="bottom"}

**Syntax**

```sh
GET https://{endpoint}/backup_vaults/{backup_vault_name}/recovery_ranges
```
{: codeblock}

**Example request**

```http
GET /backup_vaults/myBackupVault/recovery_ranges HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
```
{: codeblock}

**Example response**

```json
{
  "next": {
    "token": "SWYwNTZMflFmZUgheGNcNHMuIm9OMTFz",
    "href": "config.cloud-object-storage.cloud.ibm.com/v1/backup_vaults/mybackupvault/recovery_ranges?token=1znNRhmyheG-pm6mlMKFsoxcKXFnW1hgBb2_6ZReqHduL3rivfeBWHeyL-OaJvtfVZjKrgRVmODtOdVeQEXkKCxDWpA5hRWuv9WgTPR2eLIN9fbEaTxqdNO4dp0sQeHh4HO-13nTgRV_1QGiR1QFzREHX3wzquLn9o_1DZZAEP-vAY2V8d6BCA1z2krhpMfswKJ8r6UynUc0W5JHSH4auwJ6jC-h8sc8NEcQpHUx2Ep4oxebmtJMaycQSjLA3J2fdcYuysKq3YIcszDnPte0zwr25x5_GmBrxlgd2oesjfyTaLHfIg1g4TD0uH9m9aEAv"
  },
  "recovery_ranges": [
    {
      "recovery_range_id": "44d3dd41-d616-4d25-911a-9ef7fbf28aef",
      "source_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:sourcebucket",
      "backup_policy_name": "myBackupPolicy",
      "range_start_time": "2024-06-02T12:00:00.000Z",
      "range_end_time": "2024-06-03T12:00:00.000Z",
      "range_create_time": "2024-06-02T12:00:00.000Z",
      "retention": {
        "delete_after_days": 100
      }
    },
    {
      "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
      "source_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:sourcebucket",
      "backup_policy_name": "myBackupPolicy",
      "range_start_time": "2024-06-04T12:00:00.000Z",
      "range_end_time": "2024-06-05T12:00:00.000Z",
      "range_create_time": "2024-06-04T12:00:00.000Z",
      "retention": {
        "delete_after_days": 100
      }
    }
  ]
}
```

---

### Get Recovery Range Details
{: #get-recovery-range}

A `GET` request to the `/backup_vaults/{backup_vault_name}/recovery_ranges/{recovery_range_id}` endpoint retrieves details about a specific recovery range.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
| `recovery_range_id`       | String | Yes       | The ID of the recovery range.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
GET https://{endpoint}/backup_vaults/{backup_vault_name}/recovery_ranges/{recovery_range_id}
```
{: codeblock}

**Example request**

```http
GET /backup_vaults/myBackupVault/recovery_ranges/44d3dd41-d616-4d25-911a-9ef7fbf28aef HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
```
{: codeblock}

**Example response**

```json
{
  "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
  "source_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:sourcebucket",
  "backup_policy_name": "myBackupPolicy",
  "range_start_time": "2024-06-04T12:00:00.000Z",
  "range_end_time": "2024-06-05T12:00:00.000Z",
  "range_create_time": "2024-06-04T12:00:00.000Z",
  "retention": {
    "delete_after_days": 100
  }
}
```

---

### Update Recovery Range Detail
{: #update-recovery-range}

A `PATCH` request to the `/backup_vaults/{backup_vault_name}/recovery_ranges/{recovery_range_id}` endpoint updates details for a specific recovery range.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
| `recovery_range_id`       | String | Yes       | The ID of the recovery range.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
PATCH https://{endpoint}/backup_vaults/{backup_vault_name}/recovery_ranges/{recovery_range_id}
```
{: codeblock}

**Example request**

```http
GET /backup_vaults/myBackupVault/recovery_ranges/44d3dd41-d616-4d25-911a-9ef7fbf28aef HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com

{
  "retention": {
    "delete_after_days": 10
  }
}
```
{: codeblock}

**Example response**

```json
{
  "retention": {
    "delete_after_days": 200
  }
}
```

---

For more information about recovery range operations, see [Backup vault management](/docs/cloud-object-storage?topic=cloud-object-storage-bvm-overview).


## Restore operations
{: #restore-operations}

The {{site.data.keyword.cos_full}} Restore API provides operations for initiating and managing restore operations. Restore operations allow you to recover data from a backup vault to a target bucket.
{: shortdesc}

For more information about permissions and access, see [Restore permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-restore-permissions).
{: tip}

---

### Initiate a Restore
{: #initiate-restore}

A `POST` request to the `/backup_vaults/{backup_vault_name}/restores` endpoint initiates a restore operation.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault to restore from.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
POST https://{endpoint}/backup_vaults/{backup_vault_name}/restores
```
{: codeblock}

**Request body**

```json
{
  "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
  "restore_point_in_time": "2024-06-04T12:12:00.000Z",
  "restore_type": "in_place",
  "target_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:targetbucket"
}
```

**Example request**

```http
POST /backup_vaults/myBackupVault/restores HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com

{
  "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
  "restore_point_in_time": "2024-06-04T12:12:00.000Z",
  "restore_type": "in_place",
  "target_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:targetbucket"
}
```
{: codeblock}

**Example response**

```json
{
  "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
  "source_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:sourcebucket",
  "restore_point_in_time": "2024-06-04T12:12:00.000Z",
  "restore_type": "in_place",
  "target_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:targetbucket",
  "restore_id": "b0bcb32b-0fa9-423c-b10b-9d4328e5fc63",
  "restore_status": "initializing",
  "init_time": "2024-06-10T12:12:00.000Z"
}
```

---

### List Restores
{: #list-restores}

A `GET` request to the `/backup_vaults/{backup_vault_name}/restores` endpoint retrieves all restore operations for a specific backup vault.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
GET https://{endpoint}/backup_vaults/{backup_vault_name}/restores
```
{: codeblock}

**Example request**

```http
GET /backup_vaults/myBackupVault/restores HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
```
{: codeblock}

 **Example response**

```json
{
  "next": {
    "token": "SWYwNTZMflFmZUgheGNcNHMuIm9OMTFz",
    "href": "config.cloud-object-storage.cloud.ibm.com/v1/backup_vaults/mybackupvault/restores?Token=SWYwNTZMflFmZUgheGNcNHMuIm9OMTFz"
  },
  "restores": [
    {
      "restore_id": "b0bcb32b-0fa9-423c-b10b-9d4328e5fc63",
      "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
      "source_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:sourcebucket",
      "restore_point_in_time": "2024-06-04T12:12:00.000Z",
      "restore_type": "in_place",
      "target_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:targetbucket",
      "restore_status": "running",
      "restore_percent_progress": 50,
      "init_time": "2024-06-10T12:12:00.000Z"
    },
    {
      "restore_id": "3deefef0-3ed7-492c-8828-00fb33e64f88",
      "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
      "source_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:sourcebucket",
      "restore_point_in_time": "2024-06-04T12:12:00.000Z",
      "restore_type": "in_place",
      "target_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:othertargetbucket",
      "restore_status": "initializing",
      "init_time": "2024-06-10T12:24:00.000Z"
    }
  ]
}
```

---

### Get Restore Details
{: #get-restore}

A `GET` request to the `/backup_vaults/{backup_vault_name}/restores/{restore_id}` endpoint retrieves details about a specific restore operation.

| Path Parameter            | Type   | Required? | Description
|---------------------------|--------|-----------|---------------------------------------------------------
| `backup_vault_name`       | String | Yes       | The name of the backup vault.
| `restore_id`              | String | Yes       | The ID of the restore operation.
{: caption="Path parameters" caption-side="bottom"}

**Syntax**

```sh
GET https://{endpoint}/backup_vaults/{backup_vault_name}/restores/{restore_id}
```
{: codeblock}

**Example request**

```http
GET /backup_vaults/myBackupVault/restores/b0bcb32b-0fa9-423c-b10b-9d4328e5fc63 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
Host: config.cloud-object-storage.cloud.ibm.com
```
{: codeblock}

**Example response**

```json
{
  "restore_id": "b0bcb32b-0fa9-423c-b10b-9d4328e5fc63",
  "recovery_range_id": "6ff0d31c-7583-4463-8ae5-208752f5769c",
  "source_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:sourcebucket",
  "restore_point_in_time": "2024-06-04T12:12:00.000Z",
  "restore_type": "in_place",
  "target_resource_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a1229395:8dfbcba4e6a740e3866020847e525436:bucket:targetbucket",
  "restore_status": "running",
  "restore_percent_progress": 50,
  "init_time": "2024-06-10T12:12:00.000Z"
}
```
---

For more information about restore operations, see [Backup vault management](/docs/cloud-object-storage?topic=cloud-object-storage-bvm-overview).
```
