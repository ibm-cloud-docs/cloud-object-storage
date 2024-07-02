---

copyright:
  years: 2024
lastupdated: "2024-07-02"

keywords: IBM Cloud Object Storage notifications, notifications, gslb

subcollection: Rcloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# {{site.data.keyword.cos_full}} deployment of GSLB in all MultiZone Regions
{: #cos-notices-gslb}

The {{site.data.keyword.cos_full}} team is enabling Global Server Load Balancing (GSLB) in our MZR offerings over the next several months. This change causes the regional endpoints (public, private, and direct) to use new virtual IP addresses. This change affects you if you access {{site.data.keyword.cos_short}} from an IP address (rather than URL), or if you have allowlists, or a firewall running in your environment.
{: shortdesc}

## What you need to know about this change
{: #cos-notices-gslb-know}

During the scheduled maintenance, DNS resolution for the {{site.data.keyword.cos_short}} endpoints listed below change from returning a single IP address to returning one of three possible IP addresses for each region. Previously all connection requests would use a single IP address for each region. With GSLB, your DNS lookup returns the IP address of the {{site.data.keyword.cos_short}} frontend to optimally serve your regional traffic. The IP address may change to a different IP address on subsequent DNS lookups. Approximately 14 days before each maintenance window, the new IP addresses are available for customers to verify connectivity.

## How you benefit from this change
{: #cos-notices-gslb-benefit}

This change allows your traffic to be routed to the {{site.data.keyword.cos_short}} front-end servers best positioned to handle traffic at any point in time. This alsos provides increased reliability for our MZR endpoints.

## Understanding if you are impacted by this change
{: #cos-notices-gslb-affected}

Customers utilizing hard-coded IP addresses for IBM {{site.data.keyword.cos_short}} endpoints within their workloads, firewalls, or security components are encouraged to test connectivity using the provided IP addresses or subnets 14 days before the DNS maintenance.

### What actions you need to take
{: #cos-notices-gslb-actions}

Review the IP address information and schedule below to understand when the changes are made, and what new IP addresses are in use. If you are impacted by this change, update and test the new endpoints before the cutover date.

## Deployment dates
{: #cos-notices-gslb-deployment}

| Region          | New addresses available for testing  | DNS updated to new IP Addresses |
|:----------------|--------------------------------------|---------------------------------|
| Sydney (au-syd) | Launch date – 14 days                | August 29, 2024                 |
| Madrid (eu-es)  | TBD                                  | TBD                             |
| Toronto (ca-tor)| TBD                                  |TBD                              |
{: caption="Table 1. Deployment dates" caption-side="top"}

## IP address changes
{: #cos-notices-gslb-ip}

| Region                 | URL                                                    | Current IP                      | New IPs                                           |
|:-----------------------|-----------------------------------------------------   |---------------------------------|-------------------------------------------------- |
| Sydney (au-syd) public | s3.au-syd.cloud-object-storage.appdomain.cloud         | 130.198.118.97                  | 130.198.118.97, 130.198.118.105, 130.198.118.106  |
| Sydney (au-syd) private| s3.private.au-syd.cloud-object-storage.appdomain.cloud | 10.1.129.67                     | 10.1.129.67, 10.1.129.189, 10.1.129.190           |
| Sydney (au-syd) direct | s3.direct.au-syd.cloud-object-storage.appdomain.cloud  | 161.26.0.27                     | 161.26.0.27, 161.26.125.27, 161.26.165.27         |
{: caption="Table 2. IP address changes" caption-side="top"}
