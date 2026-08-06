---

copyright:
  years: 2026

lastupdated: "2026-08-06"

keywords: classic infrastructure, instance conversion, softlayer, migrate, cos classic, end of support, EOS

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Converting Classic Infrastructure instances to IBM Cloud Platform instances
{: #cos-instance-conversion}

{{site.data.keyword.cos_full_notm}} supports two types of offerings: modern instances that are part of IBM Cloud Platform, and legacy instances that are part of the original classic SoftLayer platform. IBM is deprecating and retiring the older Cloud Object Storage (Classic Infrastructure) offering.
{: shortdesc}

After the 06 August 2027 End of Support (EOS) date, {{site.data.keyword.cos_full_notm}} (Classic Infrastructure) instances will no longer be supported. You will lose access to unconverted instances, and any remaining data will be deleted. Impacted customers can convert their classic infrastructure instances to IBM Cloud Platform instances in the {{site.data.keyword.cloud_notm}} console before the EOS deadline.

The conversion does not require you to move your data or change your application configuration. It is designed to be as non-disruptive as possible.

## Before you begin
{: #cos-instance-conversion-prereqs}

- Confirm you have access to the {{site.data.keyword.cloud_notm}} console.
- Identify the classic infrastructure instances you want to convert.
- If your application uses legacy S3 ACLs or relies on cross-account access control, review [Potential application impacts](#cos-instance-conversion-app-impacts) before proceeding, and open a support ticket.
- Ensure that you have the following minimum IAM permissions:
   - **Reader** platform role for Resource Groups, to select a destination resource group for the new IBM Cloud Platform instance.
   - **Editor** platform role for {{site.data.keyword.cos_full_notm}}, to create the new IBM Cloud Platform instance and service credentials.
   - **Manager** service role for {{site.data.keyword.cos_full_notm}}, to migrate HMAC keys to IAM with Manager permissions.

## Converting your instances
{: #cos-instance-conversion-steps}

Complete the following steps before 06 August 2027. Do not wait until the EOS date, as early action ensures a smooth transition.

1. Log in to the [{{site.data.keyword.cos_full_notm}} console](https://cloud.ibm.com/){: external}.
1. Navigate to the Conversion page.
1. In the **Instances to convert** table, select the instance or instances to convert.
1. In the side panel, confirm the target account, resource group, and approve the conversion.
1. After approval, IBM performs the conversion process immediately.
1. Track the progress of the conversion on the Conversion page.

## What to expect during conversion
{: #cos-instance-conversion-process}

After you approve the conversion in the console, IBM performs the conversion process for the selected Cloud Object Storage (Classic Infrastructure) instance. Most instance conversions complete in less than a minute, though conversion time can vary. You can track the conversion status from the Conversion page in the console.

After conversion, the converted instance appears on the Instances page. The converted instance is named `<original-name>-converted`. The original Classic Infrastructure instance might remain visible for approximately 15 minutes while cleanup completes, after which it is deleted.

## What happens if you do not convert
{: #cos-instance-conversion-no-action}

If no action is taken before the EOS date, impacted instances will not be converted, access to those instances will be lost, and any remaining data will be deleted. If you do not want to move to IBM Cloud Platform, copy your data to another location before 06 August 2027.

## Capabilities available after conversion
{: #cos-instance-conversion-capabilities}

Converting to IBM Cloud Platform provides long-term platform consistency and access to optional capabilities that are not available to legacy instances.

### Integrated services
{: #cos-instance-conversion-integrations}

After conversion, your instance supports IBM Cloud Identity and Access Management (IAM), which provides access to the following integrations:

- Activity Tracking
- Metrics Monitoring
- Bring Your Own Key (BYOK)
- Key Protect (Multi-Tenant)
- Key Protect (single-tenant)

For more information, see [Integrated services](/docs/cloud-object-storage?topic=cloud-object-storage-integrated-services).

### Bucket and object features
{: #cos-instance-conversion-features}

The following features are available to converted instances and their buckets where the features are supported in the bucket's region:

- Quota enforcement
- Expiration policies
- Retention and Object Lock (WORM)
- Versioning
- Backup policies and bucket backup vaults
- Replication
- Network-based restrictions, including legacy IP firewalls and Context-Based Restrictions
- Aspera

For more information, see [Bucket management](/docs/cloud-object-storage?topic=cloud-object-storage-bucket-management) and [Data management](/docs/cloud-object-storage?topic=cloud-object-storage-data-management).

### Finer-grained access control
{: #cos-instance-conversion-iam}

After conversion, you can use IBM Cloud IAM to provide fine-grained access to your data. You can create multiple credentials with HMAC and IAM keys. The following IAM roles are available:

- Writer
- Reader
- Manager
- Object Reader
- Content Reader

IAM also enables the use of Service IDs, Access Groups, and Access Policies at both the bucket level and the instance level.

The conversion process allows you to continue using the same HMAC credentials used with the legacy instance, with the option of later adopting advanced IAM capabilities. For more information, see [Getting started with IAM](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-with-iam).

### Storage classes
{: #cos-instance-conversion-storage-classes}

After conversion, you cannot provision new buckets with the Flex storage class. Use the Smart Tier storage class as an alternative.

## Required changes after conversion
{: #cos-instance-conversion-required-changes}

Converted instances use IBM Cloud IAM for authorization. Applications can continue to use the same S3 endpoints to access their buckets. Billing transitions to the IBM Cloud Platform system.

### Service credential visibility after conversion
{: #cos-instance-conversion-credential-visibility}

After conversion, users can no longer view existing service credentials unless they are explicitly assigned the **Administrator** IAM role on the instance. This behavior is enforced by the IAM action `resource-controller.credential.retrieve_all`, which controls the ability to retrieve service credentials and is assigned exclusively to the Administrator role.

If your team members require access to service credentials after conversion, assign them the Administrator role on the {{site.data.keyword.cos_full_notm}} instance. This assignment can be made before or after the conversion.

For more information, see [Viewing a service credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials#viewing-a-service-credential), [Resource Controller API reference](/docs/apis/resource-controller/resource-controller#get-resource-key), and [Getting started with IAM](/docs/cloud-object-storage?topic=cloud-object-storage-iam).


## Potential application impacts
{: #cos-instance-conversion-app-impacts}

There are a few uncommonly used legacy access control settings that are not supported in IBM Cloud IAM, and cross-account access control is handled differently.

The following S3 bucket-level ACLs are not supported after conversion:

| Grantee | Permissions not supported |
|---------|--------------------------|
| Public | WRITE and FULL_CONTROL |
| Authenticated users | READ and FULL_CONTROL |
| Log Delivery | READ_ACP, WRITE, and FULL_CONTROL |
{: caption="Unsupported S3 bucket-level ACLs after conversion" caption-side="bottom"}

The following S3 object-level ACL is not supported after conversion:

| Grantee | Permissions not supported |
|---------|--------------------------|
| Public | READ_ACP |
{: caption="Unsupported S3 object-level ACL after conversion" caption-side="bottom"}

If your application uses these legacy S3 ACLs or relies on cross-account access control, open an IBM Cloud Support ticket before converting.

## Potential account impacts
{: #cos-instance-conversion-account-impacts}

Billing transitions to the modern IBM Cloud Platform system after conversion. Pricing and discounts carry over to the converted instance, with the potential for minor differences due to internal system variations.

## Getting support
{: #cos-instance-conversion-support}

If you have questions or concerns about the conversion, open an [IBM Cloud Support ticket](https://cloud.ibm.com/unifiedsupport/supportcenter){: external}.
