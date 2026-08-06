---

copyright:
  years: 2026
lastupdated: "2026-08-06"

keywords: IBM Cloud Object Storage notifications, notifications, classic infrastructure, instance conversion, deprecation, end of support, EOS, SoftLayer

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Deprecation of {{site.data.keyword.cos_full}} (Classic Infrastructure)
{: #deprecation-cos-classic-infrastructure}

{{site.data.keyword.cloud_notm}} continues to evaluate its service offerings periodically, keeping in perspective client requirements and market direction. As a result, the legacy {{site.data.keyword.cos_full_notm}} (Classic Infrastructure) offering is being deprecated and retired. After 06 August 2027, classic infrastructure instances will no longer be supported, access to those instances will be lost, and any remaining data will be deleted. Before the End of Support date, you can convert your classic infrastructure instances to IBM Cloud Platform instances directly from the {{site.data.keyword.cloud_notm}} console, without moving your data or changing your application configuration.
{: deprecated}

## Important dates
{: #deprecation-cos-classic-infrastructure-dates}

| Stage | Date | Description |
|---------------------------|----------------------|----------------------------------------------------|
| Deprecation announcement | 06 August 2026 | Announcement of the deprecation of {{site.data.keyword.cos_short}} (Classic Infrastructure). Existing instances continue to operate and can be converted to IBM Cloud Platform instances. |
| End of support | 06 August 2027 | Support for {{site.data.keyword.cos_short}} (Classic Infrastructure) ends. Any remaining unconverted instances become permanently inaccessible and any remaining data is deleted. |
{: caption="Important dates" caption-side="top"}

## Deprecation details
{: #deprecation-cos-classic-infrastructure-details}

-	The legacy {{site.data.keyword.cos_short}} (Classic Infrastructure) offering is being retired.
-	If you have one or more {{site.data.keyword.cos_short}} instances that are provisioned on the classic SoftLayer infrastructure, you are affected by this change.
-	Impacted instances appear in the **Instances to Convert** table on the conversion page in the {{site.data.keyword.cloud_notm}} console.
-	The conversion process is designed to be non-disruptive: your data does not need to be moved, your applications can continue to use the same S3 endpoints after conversion, your existing HMAC credentials remain usable after conversion, and billing transitions to the IBM Cloud Platform system with pricing and discounts carried over.
-	After conversion, you cannot provision new buckets with the Flex storage class. Use Smart Tier as an alternative.
-	Minor billing differences of up to 0.5% per pricing component are possible because of internal system variations.
-	If you do not convert before 06 August 2027, access to unconverted instances will be lost and any remaining data will be deleted.

## Next steps for current users
{: #deprecation-cos-classic-infrastructure-next-steps}

Do not wait until the 06 August 2027 End of Support date. Convert your instances as soon as possible to ensure a smooth transition. For more information, see [Converting Classic Infrastructure instances to IBM Cloud Platform instances](/docs/cloud-object-storage?topic=cloud-object-storage-cos-instance-conversion).

## Help
{: #deprecation-cos-classic-infrastructure-help}

If you have questions or concerns about the conversion, [open an IBM Cloud Support ticket](/docs/get-support?topic=get-support-open-case).
