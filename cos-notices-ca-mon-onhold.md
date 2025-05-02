---

copyright:
  years: 2025
lastupdated: "2025-05-02"

keywords: IBM Cloud Object Storage notifications, notifications, ca-mon, on hold, postponed

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# {{site.data.keyword.cos_full}} conversion of Montreal 01 Single Site COS location (mon01) into a Single Campus Multi-Zone Region (ca-mon): **ON HOLD**
{: #cos-notices-ca-mon-onhold}

The {{site.data.keyword.cos_full}} team has **postponed** the process of migrating the Montreal 01 Single Site {{site.data.keyword.cos_short}} location `mon01` to the Single Campus Multi-Zone Region `ca-mon`, until the `ca-mon` offering obtains equivalent compliance certifications.   The `mon01` offering continues to be fully supported, while the `ca-mon` offering is also available for provisioning new buckets.
{: shortdesc}

## What you need to know about this change
{: #cos-notices-ca-mon-know-about-change-onhold}

[Single Campus Multi-zone regions](/docs/overview?topic=overview-locations#single-campus-mzr) like `ca-mon` offer greater data resiliency and availability.  However, `ca-mon` does not yet have full compliance certification equivalence with `mon01`.  SOC-2 compliance is not available yet.  IBM cannot at this time automatically migrate customer data into the more resilient deployment and maintain SOC2 compliance on the migrated data.

Continue to use the `mon01` endpoints to access your existing `mon01` data.  Customers are able to self-migrate data from `mon01` to `ca-mon` if desired, see [Moving data between buckets](/docs/cloud-object-storage?topic=cloud-object-storage-region-copy) for guidance.

There is one important consideration for customers that are currently using the `s3.private.mon01.cloud-object-storage.appdomain.cloud` endpoint and who wish to migrate their data to `ca-mon`. Connectivity to the `ca-mon` private endpoint `s3.private.ca-mon.cloud-object-storage.appdomain.cloud` requires the source account network configuration to be **VRF-enabled**. A small number of existing `mon01` customers are not using **VRF-enabled** accounts, and would need to convert their accounts before they can begin using the `ca-mon` private endpoint. Accounts created after November 30, 2023 are automatically **VRF-enabled**. Conversion typically requires a brief outage, with potentially special considerations when Direct Link is being used. **VRF-enablement** status can be checked in the Cloud Console under Manage->Account->Account Settings. Additional information about **VRF-enablement** can be found at [Enabling VRF in the console](/docs/account?topic=account-vrf-service-endpoint&interface=ui#vrf).
