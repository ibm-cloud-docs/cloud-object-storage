---

copyright:
  years: 2025
lastupdated: "2025-03-14"

keywords: IBM Cloud Object Storage notifications, notifications, ca-mon

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# {{site.data.keyword.cos_full}} conversion of Montreal 01 Single Site COS location (mon01) into a Single Campus Multi-Zone Region (ca-mon)
{: #cos-notices-ca-mon}

The {{site.data.keyword.cos_full}} team has begun the process of converting the Montreal 01 Single Site {{site.data.keyword.cos_short}} location `mon01` into a Single Campus Multi-Zone Region `ca-mon`.  The process is similar to past conversions of Single Site offerings to Multi-Zone Region offerings in Toronto and Sao Paolo, with one new endpoint consideration.
{: shortdesc}

## What you need to know about this change
{: #cos-notices-ca-mon-know-about-change}

Conversions like this involve live, transparent data migration into more resilient fault tolerance zones.  See [Single Campus Multi-zone regions](/docs/overview?topic=overview-locations#single-campus-mzr) for more information on how the resiliency differs from other Multi-zone regions.

The region name displayed on your invoice may change, but there are no pricing changes.

The existing {{site.data.keyword.cos_short}} service integrations enabled for `mon01` are supported in ca-mon, see [Integrated service availability](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).  Conversion to the regional offering model will enable additional integrations in the future.

There are two main areas where customer action may be required, concerning the s3 endpoints and the s3 location-constraints.

### S3 ENDPOINTS

There is a new set of endpoints associated with `ca-mon`:

- Public endpoint:  `s3.ca-mon.cloud-object-storage.appdomain.cloud`
- Private endpoint:  `s3.private.ca-mon.cloud-object-storage.appdomain.cloud`
- Direct endpoint:  `s3.direct.ca-mon.cloud-object-storage.appdomain.cloud`

{{site.data.keyword.cos_short}} endpoints are documented at [Regional Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints-region) and discoverable using an [API](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints).

The Global Server Load Balancing ( GSLB ) virtual IP addresses associated with the `ca-mon` endpoints can be found at [IP address changes](/docs/cloud-object-storage?topic=cloud-object-storage-cos-notices-gslb#cos-notices-gslb-ip).

All of the `mon01` endpoints will continue to remain available and be fully supported even after the completion of the conversion, but the best practice recommendation is to switch over to the new `ca-mon` endpoints as soon as convenient.
{: important}

There is one important consideration for `mon01` customers that are currently using the `s3.private.mon01.cloud-object-storage.appdomain.cloud` endpoint.  Connectivity to the `ca-mon` private endpoint (`s3.private.ca-mon.cloud-object-storage.appdomain.cloud`) requires the source account network configuration to be "VRF-enabled".  A small number of existing `mon01` customers are not using VRF-enabled accounts, and will need to convert their accounts before they can begin using the `ca-mon` private endpoint.  Accounts created after November 30, 2023 are automatically VRF-enabled.  Conversion typically requires a brief outage, with potentially special considerations when Direct Link is being used.  VRF-enablement status can be checked in the Cloud Console under `Manage->Account->Account Settings`.  Additional information about VRF enablement can be found at [Enabling VRF in the console](/docs/account?topic=account-vrf-service-endpoint&interface=ui#vrf).

### S3 LOCATION CONSTRAINT

The location constraint in {{site.data.keyword.cos_short}} is a piece of bucket metadata that describes the bucket's location and storage class.  When the conversion process to `ca-mon` completes, the location constraint associated with buckets in montreal will change to reflect the new `ca-mon` location.

The location constraint mapping is as follows:

| Region from     | Conversion -->  | Region to         |
|-----------------|---------------- |-------------------|
| Montreal `mon01`| standard        | Montreal `ca-mon` |
| Montreal `mon01`| vault           | Montreal `ca-mon` |
| Montreal `mon01`| cold            | Montreal `ca-mon` |
| Montreal `mon01`| smart           | Montreal `ca-mon` |
| Montreal `mon01`| onerate_active  | Montreal `ca-mon` |
{: caption="location constraint mapping" caption-side="top"}

The first place that location constraints are used is in bucket creation.  Either the `ca-mon` or the `mon01` location constraint associated with your desired storage class can be used.   See [Using storage classes](/docs/cloud-object-storage?topic=cloud-object-storage-classes) for information on the usage of location constraints during bucket creation.

The complication comes on the reverse flow, when {{site.data.keyword.cos_short}} is reporting the location-constraint of a bucket.  Until the conversion process is completed, {{site.data.keyword.cos_short}} will return the `mon01` format, regardless of which format was used to create the bucket.  After the conversion process is completed, starting May 1, 2025, {{site.data.keyword.cos_short}} will return the `ca-mon` format, regardless of which format was used to create the bucket.

The location constraint associated with a bucket can be reported in the following ways:

- By means of the {{site.data.keyword.cos_short}} Console
- Using the [`?extended`](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) parameter of the list buckets operation
- Using the [`bucket-location-get`](/docs/cloud-object-storage?topic=cloud-object-storage-ic-cos-cli#find-a-bucket) command from the IBM Cloud CLI

The potential discrepancy will not typically matter, but it depends on the application.  There will be a behavior change starting May 1, at which point it will be recommended to always use the `ca-mon` format.
