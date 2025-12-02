---

copyright:
  years: 2022, 2025
lastupdated: "2025-12-02"

keywords: network, vpe, private, vpc, dns, buckets, gateway

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using Virtual Private Endpoints
{: #vpes}

{{site.data.keyword.cloud}} Virtual Private Endpoint (VPE) for {{site.data.keyword.cos_full}} provides connection points to IBM services on the {{site.data.keyword.cloud_notm}}internal network from your VPC network.

Virtual Private Endpoints (VPEs) are generally available in all regions.
{: .note}

## Before you begin
{: #vpes-before-begin}

- You need to have an [{{site.data.keyword.cloud_notm}} account](https://cloud.ibm.com/registration){: .external}.
- You also need an [instance of IBM Cloud Object Storage](/objectstorage/create).

## Setting up your VPE
{: #vpes-setup}

1. Create an {{site.data.keyword.vpc_full}} to host the applications that need to access your {{site.data.keyword.cos_full_notm}} buckets. See [Getting started with VPC](/docs/vpc?topic=vpc-getting-started).

1. Find the [location and the corresponding direct endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) where your bucket is located.

1. From the [{{site.data.keyword.cloud_notm}} console](/login){: external}, select the **Navigation menu** ![Navigation menu icon](../icons/icon_hamburger.svg), then click **Infrastructure** ![VPC icon](../../icons/vpc.svg) > **Network** > **Virtual private endpoint gateways**. Create a VPE for your {{site.data.keyword.cloud_notm}} instances with the [following instructions](/docs/vpc?topic=vpc-ordering-endpoint-gateway&interface=ui).

1. After you create your VPE, it may take a few minutes for the new VPE and DNS to complete the process and begin working for your VPC. Completion is confirmed when you see an IP address set in the [details view](/docs/vpc?topic=vpc-vpe-viewing-details-of-an-endpoint-gateway) of the VPE.

## VPE discoverability
{: #vpes-discoverability}

Following the previous steps results in a VPE that provides access over the internal {{site.data.keyword.cloud}} network from your VPC network to all of your buckets in a particular location.

Each access to your buckets from your {{site.data.keyword.vpc_short}} will require authorization at the S3 API level. To further restrict this access to specific IP addresses, or ranges of IP addresses, provide the {{site.data.keyword.vpc_short}} ID or name when configuring the context-based restrictions.
{: .important}

The [VPE details](/docs/vpc?topic=vpc-vpe-viewing-details-of-an-endpoint-gateway&interface=ui) page provides more information, including IP address, after creation.
{: .tip}

## More resources
{: #vpes-resources}

- [About virtual private endpoint gateways](/docs/vpc?topic=vpc-about-vpe)
- [Planning for virtual private endpoint gateways](/docs/vpc?topic=vpc-planning-considerations)
- [Creating an endpoint gateway](/docs/vpc?topic=vpc-ordering-endpoint-gateway)
- For further assistance, see the [FAQ for virtual private endpoints](/docs/vpc?topic=vpc-faqs-vpe), and the `Troubleshooting VPE gateways` documentation that includes [how to fix communications issues](/docs/vpc?topic=vpc-troubleshoot-cannot-communicate).
