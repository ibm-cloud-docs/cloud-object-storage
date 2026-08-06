---

copyright:
  years: 2026
lastupdated: "2026-08-06"

keywords: IBM Cloud Object Storage notifications, notifications, classic infrastructure, instance conversion, end of support, EOS, SoftLayer

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# {{site.data.keyword.cos_full}} (Classic Infrastructure) End of Support and instance conversion
{: #cos-notices-classic-conversion}

{{site.data.keyword.cos_full_notm}} is deprecating and retiring the legacy Cloud Object Storage (Classic Infrastructure) offering. After 06 August 2027 End of Support (EOS) date, IBM Cloud Object Storage (Classic Infrastructure) instances will no longer be supported. Clients will lose access to such instances, and any remaining data will be deleted. Before the EOS date, you can convert your classic infrastructure instances to IBM Cloud Platform instances directly from the {{site.data.keyword.cloud_notm}} console, without moving your data or changing your application configuration. After conversion, your instance will be managed and billed through the IBM Cloud platform and will have access to additional Cloud Object Storage and IBM Cloud capabilities.
{: shortdesc}

## What you need to know about this change
{: #cos-notices-classic-conversion-about}

IBM currently supports two types of {{site.data.keyword.cos_short}} offerings: modern instances that are part of IBM Cloud Platform, and legacy instances that are part of the original classic SoftLayer platform. IBM is deprecating and retiring the older Cloud Object Storage (Classic Infrastructure) offering.

After the 06 August 2027 EOS date, {{site.data.keyword.cos_short}} (Classic Infrastructure) instances will no longer be supported and any remaining data will be deleted.
{: important}

The conversion process is designed to be non-disruptive:
- Your data does not need to be moved.
- Your applications can continue to use the same S3 endpoints after conversion.
- Your existing HMAC credentials remain usable after conversion.
- Billing transitions to the IBM Cloud Platform system and pricing and discounts are carried over.

## Understanding if you are affected by this change
{: #cos-notices-classic-conversion-impact}

You are affected by this change if you have one or more {{site.data.keyword.cos_short}} instances that are provisioned on the classic SoftLayer infrastructure.

Impacted instances appear in the **Instances to Convert** table in the conversion page in the {{site.data.keyword.cloud_notm}} console.



## Action required before 06 August 2027
{: #cos-notices-classic-conversion-action-required}

Do not wait until the 06 August 2027 EOS date. Convert your instances as soon as possible to ensure a smooth transition.

### Converting your instances
{: #cos-notices-classic-conversion-how-to}

1. Log in to the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com/){: external}.
2. Navigate to the conversion page for {{site.data.keyword.cos_short}}.
3. In the **Instances to Convert** table, select the instance or instances to convert.
4. Confirm the resource group and approve the conversion.
5. After approval, IBM will immediately perform the conversion process.
6. Track the progress of the conversions on the conversion page.

For more information about converting your classic infrastructure instances to IBM Cloud Platform instances, see [Converting Classic Infrastructure instances to IBM Cloud Platform instances](/docs/cloud-object-storage?topic=cloud-object-storage-cos-instance-conversion).



### Storage class changes
{: #cos-notices-classic-conversion-storage-class}

After conversion, you cannot provision new buckets with the Flex storage class. Use the Smart Tier storage class as an alternative.

### Billing
{: #cos-notices-classic-conversion-billing}

Billing transitions to the IBM Cloud Platform system. Pricing and discounts are carried over. Minor differences of up to 0.5% per pricing component are possible due to internal system variations.



## Getting help
{: #cos-notices-classic-conversion-support}

If you need assistance with your instance conversion, you can contact IBM support through the following channels:

- Live chat by using the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com/){: external}
- [Open an IBM Cloud Support ticket](/docs/get-support?topic=get-support-open-case)

## Summary
{: #cos-notices-classic-conversion-summary}

What
:   {{site.data.keyword.cos_short}} (Classic Infrastructure) legacy offering will reach End of Support.

When
:   End of Support is 06 August 2027.

After this date, you will lose access to unconverted instances and any remaining data will be deleted.
