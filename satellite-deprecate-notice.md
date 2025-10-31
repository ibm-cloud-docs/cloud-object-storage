---

copyright:
  years: 2024, 2025
lastupdated: "2025-10-31"


keywords:  object storage, satellite, deprecation

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Deprecation {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}
{: #deprecation-cos-satellite}

{{site.data.keyword.cloud_notm}} continues to evaluate its service offerings periodically, keeping in perspective our client requirements and market direction. As a result, as of December 16, 2024, the {{site.data.keyword.cos_full}} for {{site.data.keyword.satellitelong}} offering is being deprecated.
{: shortdesc}

## Important dates
{: #deprecation-cos-satellite-dates}

| Stage | Date | Description            |
|---------------------------|----------------------|----------------------------------------------------|
| Deprecation announcement  | 16 December 2024   | Announcement of {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} deprecation. Existing instances are serviced as per terms of offering. |
| End of Marketing         | 13 January 2025     | No new requests for {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} can be created or submitted. Existing instances are serviced as per terms of offering.  |
| End of Support          | 16 December 2025               | Support for this service ends on this date, after which all instances will be permanently disabled and inaccessible, and no new support cases can be opened.  |
{: caption="Important dates" caption-side="top"}

## Deprecation details
{: #deprecation-cos-satellite-details}

-	The service is removed from the {{site.data.keyword.cloud_notm}} console on 13 January 2025, and no new instances can be created after that date. Your existing instances that were created before this date will continue to run as planned.
-	This deprecation means that support, including updates and technical support for the product, is no longer available, effective 16 December 2025.
-	Any remaining instances will be permanently disabled and inaccessible as of 16 December 2025, including any user data.
-	No support cases can be opened after 16 December 2025.

## Next steps for current users
{: #deprecation-cos-satellite-next-steps}

Please complete your in-progress activities promptly and ensure the transition to another object storage service on IBM Satellite (as offered through alternatives). If you need help, please open a service request with {{site.data.keyword.cloud_notm}} support.

Migrating to an alternative service
{: #deprecation-cos-satellite-alternative-service}

Clients seeking an {{site.data.keyword.cos_short}} solution on {{site.data.keyword.satelliteshort}} must transition to alternative options.
1. Connecting to S3-compatible {{site.data.keyword.cos_short}} [using the CSI driver](https://github.com/IBM/ibm-object-csi-driver).
2. Using {{site.data.keyword.cos_short}} native to ROKS (for locality, disconnected mode) [using ODF](/docs/openshift?topic=openshift-ocs-storage-prep).
3. For larger deployments, utilize our [on-premises Cloud Object Storage solution](https://www.ibm.com/products/cloud-object-storage/systems).

## Help
{: #deprecation-cos-satellite-alternative-help}

If you have questions, comments, or concerns, you can contact the team through [IBM Cloud Support](https://cloud.ibm.com/unifiedsupport/supportcenter).

