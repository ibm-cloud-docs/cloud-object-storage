---

copyright:
  years: 2022, 2024
lastupdated: "2024-01-11"

keywords: network, vpe, private, vpc, dns, buckets

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using Virtual Private Endpoints
{: #vpes}


{{site.data.keyword.cloud}} Virtual Private Endpoint (VPE) for {{site.data.keyword.cos_full}} provides connection points to IBM services on the {{site.data.keyword.cloud}} internal network from your VPC network.


## Using Virtual Private Endpoints
{: #using-vpes}


Virtual Private Endpoints (VPEs) are generally available in all regions.
{: .note}

### Before you begin
{: #vpes-before-begin}

- You need to have an [{{site.data.keyword.cloud_notm}} account](https://cloud.ibm.com/registration){: .external}
- You also need an [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)

### Setting up your VPE
{: #vpes-setup}

1. Create a {{site.data.keyword.vpc_full}} to host the applications that need to access your {{site.data.keyword.cos_full_notm}} buckets. See [Getting started with VPC](/docs/vpc?topic=vpc-getting-started).

1. Find the [location and the corresponding direct endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) where your bucket is located.

1. In the {{site.data.keyword.cloud_notm}} console, click the menu icon and select VPC Infrastructure -> Network -> Virtual private endpoint gateways. Create a VPE for your {{site.data.keyword.cloud_notm}} instances with the [following instructions](/docs/vpc?topic=vpc-about-vpe).

1. After you create your VPE, it may take a few minutes for the new VPE and DNS to complete the process and begin working for your VPC. Completion is confirmed when you see an IP address set in the [details view](/docs/vpc?topic=vpc-vpe-viewing-details-of-an-endpoint-gateway) of the VPE.

### VPE Discoverability
{: #vpes-discoverability}

Following the previous steps results in a VPE that provides access over the internal {{site.data.keyword.cloud}} network from your VPC network to all of your buckets in a particular location.

Each access to your buckets from your {{site.data.keyword.vpc_short}} will require authorization at the S3 API level. To further restrict this access to specific IP addresses, or ranges of IP addresses, provide the {{site.data.keyword.vpc_short}} ID or name when configuring the context-based restrictions.
{: .important}

The [VPE details](https://cloud.ibm.com/docs/vpc?topic=vpc-vpe-viewing-details-of-an-endpoint-gateway&interface=ui) page will provide you with more information, including IP address, after creation.
{: .tip}

### More resources
{: #vpes-resources}

- [Planning for virtual private endpoint gateways](/docs/vpc?topic=vpc-planning-considerations)
- [Creating an endpoint gateway](/docs/vpc?topic=vpc-ordering-endpoint-gateway)
- For further assistance, see the [FAQs for virtual private endpoints here](/docs/vpc?topic=vpc-faqs-vpe), and the `Troubleshooting VPE gateways` documentation that includes [how to fix communications issues here](/docs/vpc?topic=vpc-troubleshoot-cannot-communicate).

