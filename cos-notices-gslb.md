---

copyright:
  years: 2024
lastupdated: "2024-07-30"

keywords: IBM Cloud Object Storage notifications, notifications, gslb

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# {{site.data.keyword.cos_full}} deployment of GSLB in all MultiZone Regions
{: #cos-notices-gslb}

The {{site.data.keyword.cos_full}} team is enabling Global Server Load Balancing (GSLB) in {{site.data.keyword.IBM_notm}}'s MZR offerings over the next several months. This change causes the regional endpoints (public, private, and direct) to use new virtual IP addresses. This change affects you when accessing {{site.data.keyword.cos_short}} by means of an IP address (rather than URL), or if you have IP-based allowlists or firewall-rules running in your environment.
{: shortdesc}

## What you need to know about this change
{: #cos-notices-gslb-know}

During the scheduled maintenance, DNS resolution for the {{site.data.keyword.cos_short}} endpoints listed in the table changes from returning a single IP address to returning one of three possible IP addresses for each region. Previously all connection requests would use a single IP address for each region. With GSLB, your DNS lookup returns the zonal IP address of the {{site.data.keyword.cos_short}} front end to optimally serve your regional traffic. The IP address can change to a different IP address on subsequent DNS lookups. Approximately 14 days before each maintenance window, the new IP addresses will be available for customers to verify connectivity.

## How you benefit from this change
{: #cos-notices-gslb-benefit}

This change allows your traffic to be routed to the {{site.data.keyword.cos_short}} front-end servers best positioned to handle traffic at any point in time. It also provides increased reliability for {{site.data.keyword.IBM_notm}}'s MZR endpoints.

## Understanding if you are impacted by this change
{: #cos-notices-gslb-affected}

Customers that use hardcoded IP addresses for {{site.data.keyword.IBM_notm}} {{site.data.keyword.cos_short}} endpoints within their workloads, firewalls, or security components may be affected when new IP addresses are used.

## What actions you need to take
{: #cos-notices-gslb-actions}

Review the IP address information and schedule tables to understand when the changes are made, and what new IP addresses will be used. If you are affected by this change, test connectivity to the provided IP addresses or subnets.  The new IPs will be available 14 days before the DNS maintenance cutover date.

## Private and direct networks (for future changes)
{: #cos-notices-gslb-endpoints}

In order to avoid future changes when using the private and direct endpoints, we recommend updating firewalls and allow lists to include these networks.
- private network: 10.1.129.0/24
- direct network: 161.26.0.0/16

## Deployment dates
{: #cos-notices-gslb-deployment}

| Region          | New addresses available for testing  | DNS updated to new IP addresses |
|:----------------|--------------------------------------|---------------------------------|
| Sydney (au-syd) | August 15, 2023                      | August 29, 2024                 |
{: caption="Table 1. Deployment dates" caption-side="top"}

Note: This table will be updated as new regions are planned for deployment.

## IP address changes
{: #cos-notices-gslb-ip}

| Region                 | URL                                                    | Current IP                      | New IPs                                           |
|:-----------------------|-----------------------------------------------------   |---------------------------------|-------------------------------------------------- |
| Sydney (au-syd) public | s3.au-syd.cloud-object-storage.appdomain.cloud         | 130.198.118.97                  | 130.198.118.97, 130.198.118.105, 130.198.118.106  |
| Sydney (au-syd) private| s3.private.au-syd.cloud-object-storage.appdomain.cloud | 10.1.129.67                     | 10.1.129.67, 10.1.129.189, 10.1.129.190           |
| Sydney (au-syd) direct | s3.direct.au-syd.cloud-object-storage.appdomain.cloud  | 161.26.0.27                     | 161.26.0.27, 161.26.125.27, 161.26.165.27         |
{: caption="Table 2. IP address changes" caption-side="top"}

Note: This table will be updated as new regions are planned for deployment.