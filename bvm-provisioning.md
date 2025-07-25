---

copyright:
  years: 2025
lastupdated: "2025-07-02"

keywords: bucket, vault, management, provisioning, metrics monitoring, activity tracker

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Bucket vault provisioning

{: #bvm-provisioning}

Buckets and Backup Vaults are different resources that are both provisioned within a {{site.data.keyword.cos_full}} serviceInstance and must specify a specific region in which to be created.

Backup Vaults are provisioned with the following request:

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
  "backup_vault_name": "mybackupvault",
  "region": "us-south",
  "sse_kp_customer_root_key_crn": "crn:v1:public:kms:us-south:a/9978e07e8a071313c66c89c428028654:5629889a-f2bb-4058-9964-f1838b778315:key:bf5997d5-8767-462f-8d6f-ee058c084f1d"
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
  "backup_vault_name": "mybackupvault",
  "activity_tracking": {
            "management_events": true
        },
  "metrics_monitoring": {
            "usage_metrics_enabled": true
        },
 "sse_kp_customer_root_key_crn": "crn:v1:public:kms:us-south:a/9978e07e8a071313c66c89c428028654:5629889a-f2bb-4058-9964-f1838b778315:key:bf5997d5-8767-462f-8d6f-ee058c084f1d"

}
```


## Bucket vault configuration

{: #bvm-provisioning-config}

`BackupVaults` can be configured with `MetricsMonitoring` and `ActivityTracking`.

### Metrics monitoring

{: #bvm-provisioning-config-metrics-monitoring}

`BackupVaults` can be configured with `MetricsMonitoring` during `BackupVault` creation, and this configuration can be modified afterward that uses a PATCH request. `MetricsMonitoring` currently supports only usage monitoring that reports the bytes usage of the `BackupVault` over time.

For more information, read [Configure Metrics for {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration).

### Activity Tracker

{: #bvm-provisioning-config-at}

`BackupVaults` can be configured with `ActivityTracking` during `BackupVault` creation, and this configuration can be modified afterward that uses a PATCH request. A description of the `ActivityTracker` events is detailed in the Activity Tracking section [Tracking events on your {{site.data.keyword.cos_full_notm}} buckets](/docs/cloud-object-storage?topic=cloud-object-storage-at&interface=ui).
