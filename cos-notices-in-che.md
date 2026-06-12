---

copyright:
  years: 2026
lastupdated: "2026-06-12"

keywords: IBM Cloud Object Storage notifications, notifications, chennai, che01, data center withdrawal

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# {{site.data.keyword.cos_full}} withdrawal of Single Site location (che01)
{: #cos-notices-single-site-location}

The {{site.data.keyword.cos_full}} team is withdrawing support for the Chennai Single Site {{site.data.keyword.cos_short}} location `che01`. Customers must migrate their data to an alternative region by 10 June 2027.
{: shortdesc}

## What you need to know about this change
{: #cos-notices-in-che-know-about-change}

In line with the IBM Cloud announcement for withdrawal of support for specific data centers, the Chennai Single Site `che01` will be withdrawn on 10 June 2027.

As a result of this change:
- The Chennai Single Site `che01` is withdrawn
- You must migrate your data to a new preferred region, such as Chennai `in-che` or Mumbai `in-mum`, or any other {{site.data.keyword.cos_short}} region of your choice

After 10 June 2027, you will not be able to access your data in the Chennai Single Site `che01`. IBM will not be able to make any exceptions to this date.
{: important}

## Affected region and recommended alternatives
{: #cos-notices-in-che-affected-regions}

The following table shows the affected region and recommended alternative data center regions:

| Region | {{site.data.keyword.cos_short}} bucket location | Recommended alternative regions | Migration deadline |
|--------|------------------------------------------------|--------------------------------|-------------------|
| Chennai | `che01` | Chennai `in-che`, Mumbai `in-mum` | 10 June 2027 |
{: caption="Affected region and alternatives" caption-side="bottom"}

## Action that is required before 10 June 2027
{: #cos-notices-in-che-action-required}

Make appropriate plans to migrate your data out of the Chennai Single Site `che01` to one of the recommended regions (or any other {{site.data.keyword.cos_short}} region of your choice) by 10 June 2027.

### Checking whether your buckets are affected
{: #cos-notices-in-che-check-buckets}

To check whether your buckets are located in the region to be withdrawn:

1. Log in to the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com/){: external}.
2. Navigate to your {{site.data.keyword.cos_short}} service instance.
3. Review the location of each bucket in your instance.
4. Identify any buckets with location `che01`.

You can also use the [`bucket-location-get`](/docs/cloud-object-storage?topic=cloud-object-storage-ic-cos-cli#find-a-bucket) command from the IBM Cloud CLI to check bucket locations.

### Migrating your data
{: #cos-notices-in-che-migrate-data}

For detailed instructions on migrating your data to a different data center, see [Migrating resources to a different data center](/docs/cloud-object-storage?topic=cloud-object-storage-migrate-data-center).

There is one important consideration for customers that currently use the `s3.private.che01.cloud-object-storage.appdomain.cloud` endpoint, and who want to migrate their data to `in-che` or `in-mum`. Connectivity to the `in-che` or `in-mum` private endpoint `s3.private.in-che.cloud-object-storage.appdomain.cloud` or `s3.private.in-mum.cloud-object-storage.appdomain.cloud` requires the source account network configuration to be **VRF-enabled**. A few existing `che01` customers are not using **VRF-enabled** accounts, and would need to convert their accounts before they can use the `in-che` or `in-mum` private endpoint. Accounts created after 30 November 2023 are automatically **VRF-enabled**. Conversion typically requires a brief outage, with potentially special considerations when Direct Link is being used. **VRF-enablement** status can be checked in the Cloud Console under Manage->Account->Account Settings. Additional information about **VRF-enablement** can be found at [Enabling VRF in the console](/docs/account?topic=account-vrf-service-endpoint&interface=ui#vrf).

Alternatively, you can migrate your data to `in-che` or `in-mum` using the public endpoints.

## Getting help
{: #cos-notices-in-che-getting-help}

If you need assistance with your data migration, you can contact IBM support through the following channels:

- Live chat by using the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com/){: external}
- [Open a support case](/docs/get-support?topic=get-support-open-case)

## Summary
{: #cos-notices-in-che-summary}

What
:   {{site.data.keyword.cos_short}} Chennai Single Site `che01` location will be withdrawn.

When
:   Chennai `che01` is withdrawn on 10 June 2027.

Action required
:   Check whether your {{site.data.keyword.cos_short}} buckets are in the `che01` location and plan to migrate your data to a new location or region of your choice. After 10 June 2027, you will no longer be able to access your data stored in `che01`, and there will be no exceptions.
