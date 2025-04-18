---

copyright:
  years: 2024, 2025
lastupdated: "2025-03-14"

keywords: IBM Cloud Object Storage notifications, notifications, gslb

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# {{site.data.keyword.cos_full}} deployment of GSLB in all MultiZone Regions
{: #cos-notices-gslb}

The {{site.data.keyword.cos_full}} team is enabling Global Server Load Balancing (GSLB) in {{site.data.keyword.IBM_notm}}'s MZR offerings over the next several months. This change causes the regional endpoints (public, private, and direct) to use new virtual IP addresses. This change affects you when accessing {{site.data.keyword.cos_short}} with an IP address (rather than URL), or if you have IP-based allowlists or firewall-rules running in your environment.
{: shortdesc}

## What you need to know about this change
{: #cos-notices-gslb-know}

During the scheduled maintenance, DNS resolution for the {{site.data.keyword.cos_short}} endpoints listed in the table changes from returning a single IP address to returning one of three possible IP addresses for each region. Previously all connection requests would use a single IP address for each region. With GSLB, your DNS lookup returns the zonal IP address of the {{site.data.keyword.cos_short}} front end to optimally serve your regional traffic. The IP address can change to a different IP address on subsequent DNS lookups. Approximately 14 days before each maintenance window, the new IP addresses are available for customers to verify connectivity. On the DNS update day, customers may experience temporary name resolution issues, connection failures, and increased latency. These impacts should be short lived.

## How you benefit from this change
{: #cos-notices-gslb-benefit}

This change allows your traffic to be routed to the {{site.data.keyword.cos_short}} front-end servers best positioned to handle traffic at any point in time. It also provides increased reliability for {{site.data.keyword.IBM_notm}}'s MZR endpoints.

## Understanding if you are impacted by this change
{: #cos-notices-gslb-affected}

Customers that use hardcoded IP addresses for {{site.data.keyword.IBM_notm}} {{site.data.keyword.cos_short}} endpoints within their workloads, firewalls, or security components may be affected when new IP addresses are used.

**Action may be required.** This change affects you when accessing Object Storage:
- By means of an IP address (rather than URL)
- If you have IP-based allowlists or firewall-rules running in your environment
- If you have IP address specific routing

## What actions you need to take
{: #cos-notices-gslb-actions}

Review the IP address information and schedule tables to understand when the changes are made, and what new IP addresses are used. If you are affected by this change, test connectivity to the provided IP addresses or subnets.  The new IPs are available 14 days before the DNS maintenance cutover date.

## Private and direct networks (for future changes)
{: #cos-notices-gslb-endpoints}

In order to avoid future changes when using the private and direct endpoints, IBM recommends updating firewalls and allow lists to include these networks.
- private network: 10.1.129.0/24
- direct network: 161.26.0.0/16

## Deployment dates
{: #cos-notices-gslb-deployment}
| Region               | New addresses available for testing | DNS updated to new IP addresses   |
|----------------------|-------------------------------------|-----------------------------------|
| Sydney (au-syd)      | August 15, 2024                     | August 29, 2024                   |
| San Paulo (br-sao)   | October 10, 2024                    | October 24, 2024                  |
| Toronto (ca-tor)     | October 14, 2024                    | October 24, 2024 (Public/Direct)  |
| Frankfurt (eu-de)    | October 28, 2024                    | November 11, 2024                 |
| Tokyo (jp-tok)       | October 31, 2024                    | November 14, 2024                 |
| London (eu-gb)       | November 7, 2024                    | November 21, 2024                 |
| Madrid (eu-es)       | November 21, 2024                   | December 5, 2024                  |
| Osaka (jp-osa)       | October 17, 2024                    | December 9, 2024                  |
| Toronto (ca-tor)     | October 14, 2024                    | December 9, 2024 (Private)        |
| Washington (us-east) | October 21, 2024                    | February 15, 2025                 |
| Dallas (us-south)    | October 24, 2024                    | February 22, 2025                 |


## IP address changes
{: #cos-notices-gslb-ip}

|  Region                      |  URL                                                      |  Current IP    |  New IPs                                         |
|:---------------------------- | --------------------------------------------------------- | -------------- | ------------------------------------------------ |
| Sydney (au-syd) public       |  s3.au-syd.cloud-object-storage.appdomain.cloud           | 130.198.118.97 | 130.198.118.97, 130.198.118.105, 130.198.118.106 |
| Sydney (au-syd) private      |  s3.private.au-syd.cloud-object-storage.appdomain.cloud   | 10.1.129.67    | 10.1.129.67, 10.1.129.189, 10.1.129.190          |
| Sydney (au-syd) direct       |  s3.direct.au-syd.cloud-object-storage.appdomain.cloud    | 161.26.0.27    | 161.26.0.27, 161.26.125.27, 161.26.165.27        |
| San Paulo (br-sao) public    |  s3.br-sao.cloud-object-storage.appdomain.cloud           | 13.116.118.49  | 13.116.118.49, 13.116.118.54, 13.116.118.55      |
| San Paulo (br-sao) private   |  s3.private.br-sao.cloud-object-storage.appdomain.cloud   | 10.1.129.165   | 10.1.129.165, 10.1.129.191, 10.1.129.192         |
| San Paulo (br-sao) direct    |  s3.direct.br-sao.cloud-object-storage.appdomain.cloud    | 161.26.0.96    | 161.26.0.96, 161.26.205.96, 161.26.209.96        |
| Toronto (ca-tor) public      |  s3.ca-tor.cloud-object-storage.appdomain.cloud           | 163.66.118.49  | 163.66.118.49, 163.66.118.51, 163.66.118.52      |
| Toronto (ca-tor) private     |  s3.private.ca-tor.cloud-object-storage.appdomain.cloud   | 10.1.129.158   | 10.1.129.158, 10.1.129.193, 10.1.129.194         |
| Toronto (ca-tor) direct      |  s3.direct.ca-tor.cloud-object-storage.appdomain.cloud    | 161.26.0.95    | 161.26.0.95, 161.26.197.95, 161.26.201.95        |
| Osaka (jp-osa) public        |  s3.jp-osa.cloud-object-storage.appdomain.cloud           | 163.68.118.49  | 163.68.118.49, 163.68.118.57, 163.68.118.58      |
| Osaka (jp-osa) private       |  s3.private.jp-osa.cloud-object-storage.appdomain.cloud   | 10.1.129.107   | 10.1.129.107, 10.1.129.195, 10.1.129.196         |
| Osaka (jp-osa) direct        |  s3.direct.jp-osa.cloud-object-storage.appdomain.cloud    | 161.26.0.47    | 161.26.0.47, 161.26.189.47, 161.26.193.47        |
| Washington (us-east) public  |  s3.us-east.cloud-object-storage.appdomain.cloud          | 169.63.118.98  | 169.63.118.98, 169.63.118.96, 169.63.118.100     |
| Washington (us-east) private |  s3.private.us-east.cloud-object-storage.appdomain.cloud  | 10.1.129.94    | 10.1.129.94, 10.1.129.50, 10.1.129.197           |
| Washington (us-east) direct  |  s3.direct.us-east.cloud-object-storage.appdomain.cloud   | 161.26.0.31    | 161.26.0.31, 161.26.0.18, 161.26.0.85            |
| Dallas (us-south) public     |  s3.us-south.cloud-object-storage.appdomain.cloud         | 169.46.118.100 | 169.46.118.100, 169.46.118.97, 169.46.118.98     |
| Dallas (us-south) private    |  s3.private.us-south.cloud-object-storage.appdomain.cloud | 10.1.129.97    | 10.1.129.97, 10.1.129.83, 10.1.129.45            |
| Dallas (us-south) direct     |  s3.direct.us-south.cloud-object-storage.appdomain.cloud  | 161.26.0.34    | 161.26.0.34, 161.26.0.29, 161.26.0.16            |
| Frankfurt (eu-de) public     |  s3.eu-de.cloud-object-storage.appdomain.cloud            | 158.177.118.97 | 158.177.118.97, 158.177.118.101, 158.177.118.102 |
| Frankfurt (eu-de) private    |  s3.private.eu-de.cloud-object-storage.appdomain.cloud    | 10.1.129.58    | 10.1.129.58, 10.1.129.198, 10.1.129.199          |
| Frankfurt (eu-de) direct     |  s3.direct.eu-de.cloud-object-storage.appdomain.cloud     | 161.26.0.24    | 161.26.0.24, 161.26.145.24, 161.26.149.24        |
| Tokyo (jp-tok) public        |  s3.jp-tok.cloud-object-storage.appdomain.cloud           | 162.133.118.49 | 162.133.118.49, 162.133.118.55, 162.133.118.56   |
| Tokyo (jp-tok) private       |  s3.private.jp-tok.cloud-object-storage.appdomain.cloud   | 10.1.129.66    | 10.1.129.66, 10.1.129.200, 10.1.129.201          |
| Tokyo (jp-tok) direct        |  s3.direct.jp-tok.cloud-object-storage.appdomain.cloud    | 161.26.0.22    | 161.26.0.22, 161.26.153.22, 161.26.157.22        |
| London (eu-gb) public        |  s3.eu-gb.cloud-object-storage.appdomain.cloud            | 169.50.118.97  | 169.50.118.97, 169.50.118.100, 169.50.118.101    |
| London (eu-gb) private       |  s3.private.eu-gb.cloud-object-storage.appdomain.cloud    | 10.1.129.53    | 10.1.129.53, 10.1.129.202, 10.1.129.203          |
| London (eu-gb) direct        |  s3.direct.eu-gb.cloud-object-storage.appdomain.cloud     | 161.26.0.26    | 161.26.0.26, 161.26.129.26, 161.26.137.26        |
| Madrid (eu-es) public        |  s3.eu-es.cloud-object-storage.appdomain.cloud            | 13.120.118.49  | 13.120.118.49, 13.120.118.51, 13.120.118.52      |
| Madrid (eu-es) private       |  s3.private.eu-es.cloud-object-storage.appdomain.cloud    | 10.1.129.187   | 10.1.129.187, 10.1.129.204, 10.1.129.205         |
| Madrid (eu-es) direct        |  s3.direct.eu-es.cloud-object-storage.appdomain.cloud     | 161.26.0.99    | 161.26.0.99, 161.26.217.99, 161.26.221.99        |
| Montreal (ca-mon) public     |  s3.ca-mon.cloud-object-storage.appdomain.cloud           |                | 64.5.38.16,   64.5.38.20,    64.5.38.24          |
| Montreal (ca-mon) private    |  s3.private.ca-mon.cloud-object-storage.appdomain.cloud   |                | 10.200.36.32, 10.200.75.184, 10.200.36.36        |
| Montreal (ca-mon) direct     |  s3.direct.ca-mon.cloud-object-storage.appdomain.cloud    |                | 161.26.15.28, 161.26.14.172, 161.26.14.240       |

{: caption="IP address changes" caption-side="top"}
