---

copyright:
  years: 2022
lastupdated: "2022-10-18"

keywords: network, vpe, private

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using Virtual Private Endpoints
{: #vpes}


{{site.data.keyword.cloud}} Virtual Private Endpoint (VPE) for {{site.data.keyword.cos_full}} provides connection points to IBM services on the {{site.data.keyword.cloud}} internal network from your VPC network.


## Using Virtual Private Endpoints
{: #using-vpes}

Virtual Private Endpoints (VPEs) are a feature associated with {{site.data.keyword.vpc_full}} environments.
{: .note}

### Before you begin
{: #vpes-before-begin}

- You need to have an [{{site.data.keyword.cloud_notm}} account](https://cloud.ibm.com/registration){: .external}
- You also need an [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)

### Setting up your VPE
{: #vpes-setup}

1. Create an {{site.data.keyword.vpc_full}}. Follow the `Getting started` [instructions here](/docs/vpc?topic=vpc-getting-started).

1. Make sure that your VPC has at least one VSI (virtual server instance), and can connect to the VSI. You can use the UI, CLI, and API to quickly provision {{site.data.keyword.vpc_full}} from the [virtual server instances]((/docs/vpc?topic=vpc-creating-virtual-servers)) page in IBM Cloud console.

1. Find the [location and the corresponding direct endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) where your bucket is located.

1. In the {{site.data.keyword.cloud_notm}} console, click the menu icon and select VPC Infrastructure -> Network -> Virtual private endpoint gateways. Create a VPE for your {{site.data.keyword.cloud_notm}} instances with this [instruction](/docs/vpc?topic=vpc-about-vpe#vpe-getting-started).

1. After you create your VPE, it might take a few minutes for the new VPE and DNS to complete the process and begin working for your VPC. Completion is confirmed when you see an IP address set in the [details view](/docs/vpc?topic=vpc-vpe-viewing-details-of-an-endpoint-gateway) of the VPE.

1. To make sure DNS is functioning for your VPE, `ssh` into your VSI and run `nslookup <instance_hostname>`. The following example shows the output from running `nslookup` on instance hostnames of `host-0.direct.cloud-object-storage.appdomain.cloud`, `host-1.direct.cloud-object-storage.appdomain.cloud`, and `host-2.direct.cloud-object-storage.appdomain.cloud`:
   ```bash
   root@test-vpc-vsi:~# nslookup host-0.direct.cloud-object-storage.appdomain.cloud
   Server:		127.0.0.53
   Address:	127.0.0.53#53
   Non-authoritative answer:
   Name:	host-0.direct.cloud-object-storage.appdomain.cloud
   Address: 10.240.64.6
   ```
   ```bash
   root@test-vpc-vsi:~# nslookup host-1.direct.cloud-object-storage.appdomain.cloud
   Server:		127.0.0.53
   Address:	127.0.0.53#53
   Non-authoritative answer:
   Name:	host-1.direct.cloud-object-storage.appdomain.cloud
   Address: 10.240.64.6
   ```
   ```bash
   root@test-vpc-vsi:~# nslookup host-2.direct.cloud-object-storage.appdomain.cloud
   Server:		127.0.0.53
   Address:	127.0.0.53#53
   Non-authoritative answer:
   Name:	host-2.direct.cloud-object-storage.appdomain.cloud
   Address: 10.240.64.6
   ```

The second ```Address:``` in each example above is the VPE IP address that appears in your COS Activity Tracker logs as the originating IP address.
{: .note}

1. You can now use your instance in the VSI.

### VPE Discoverability
{: #vpes-discoverability}

Following the previous steps results in a COS service instance with private endpoints that is reachable with the Virtual Private Endpoints from your VPC network.

Object storage instances with private endpoints are reachable from any account within the private network and access to each instance requires authentication. To restrict this access to specific IP addresses, or ranges of IP addresses, provide the VPC ID/name when configuring the [context-based restrictions](/docs/cloud-databases?topic=cloud-databases-allowlisting).
{: .important}


### More resources
{: #vpes-resources}

- [Planning for virtual private endpoint gateways](/docs/vpc?topic=vpc-planning-considerations)
- [Creating an endpoint gateway](/docs/vpc?topic=vpc-ordering-endpoint-gateway)
- For further assistance, see the [FAQs for virtual private endpoints here](/docs/vpc?topic=vpc-faqs-vpe), and the `Troubleshooting VPE gateways` documentation that includes [how to fix communications issues here](/docs/vpc?topic=vpc-troubleshoot-cannot-communicate).

