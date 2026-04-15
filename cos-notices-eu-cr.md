---

copyright:
  years: 2026
lastupdated: "2026-04-15"

keywords: IBM Cloud Object Storage notifications, notifications, eu-cr

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Important: IP address updates for {{site.data.keyword.cos_full}} EU cross region
{: #cos-notices-eu-cr}

## Introduction
{: #cos-notices-eu-cr-intro}

As part of our commitment to improving reliability and performance, we are updating IP addresses for the {{site.data.keyword.cos_full}} **EU Cross Region** endpoints. Additionally, the **Milan** datacenter has been retired and replaced by **Madrid**.


## Action required
{: #cos-notices-eu-cr-action-req}

Update any firewall rules or network configurations referencing old IP addresses before **15 May 2026**.

## What you need to know about this change
{: #cos-notices-eu-cr-about}

During the scheduled maintenance, the DNS resolution for the Object Storage endpoints that are listed after this will change. The new IP addresses are available now for customers to verify connectivity. On the DNS cutover day (17 May 2026), customers may experience temporary name resolution issues, connection failures, and increased latency. These impacts should be short-lived.

## Understanding if you are impacted by this change
{: #cos-notices-eu-cr-impact}

Customers that use hardcoded IP addresses for {{site.data.keyword.cos_full}} endpoints within their workloads, firewalls, or security components may be affected when new IP addresses are used.
Action may be required. This change affects you when accessing Object Storage:

- By means of an IP address (rather than URL)
- If you have IP-based allowlists or firewall rules running in your environment
- If you have an IP address-specific routing

## What actions you need to take
{: #cos-notices-eu-cr-actions}

Review the IP address information and schedule tables to understand when the changes are made, and what new IP addresses are used. If you are affected by this change, test connectivity to the provided IP addresses or subnets.

## Recommended changes (Future-Proofing)
{: #cos-notices-eu-cr-recommended-changes}

Recommendation: Use **GEO (global) endpoints** for better resilience and to avoid future IP address changes. Global endpoints automatically route traffic to healthy datacenters, providing better resilience than datacenter-specific tethered endpoints.


To minimize impact from future IP changes, configure your firewalls to allow these entire network ranges:

```sh
private network: 10.1.129.0/24
direct network: 161.26.0.0/16
```

This is optional but recommended for operational flexibility when using private and direct endpoints.
{: note}

## GEO endpoints
{: #cos-notices-eu-cr-geo-endpoints}

| Hostname | New/current IPs | Retired IPs |
| --- | --- | --- |
| s3.eu.cloud-object-storage.appdomain.cloud | 13.122.118.50, 159.8.199.246, 159.122.119.244 | 159.122.138.99 |
| s3.direct.eu.cloud-object-storage.appdomain.cloud | 161.26.29.12, 161.26.37.12, 161.26.221.99 | 161.26.53.14 |
| s3.private.eu.cloud-object-storage.appdomain.cloud | 10.1.129.162, 10.1.129.163, 10.1.129.208 | 10.1.129.164 |
{: caption="GEO endpoints" caption-side="top"}

Retired IPs are no longer available and can be removed from client configurations.
{: important}

## Tethered endpoints (Datacenter-specific)
{: #cos-notices-eu-cr-tethered-endpoints}

### Madrid (replacing Milan)
{: #cos-notices-eu-cr-tethered-endpoints-milan}

| New Hostname | New IPs | Retired IPs | Old Hostname |
|---|---|---|---|
| s3.mad.eu.cloud-object-storage.appdomain.cloud | 13.122.118.49 | 159.122.138.97 | s3.mil.eu.cloud-object-storage.appdomain.cloud |
| s3.direct.mad.eu.cloud-object-storage.appdomain.cloud | 161.26.221.100 | 161.26.53.13 | s3.direct.mil.eu.cloud-object-storage.appdomain.cloud |
| s3.private.mad.eu.cloud-object-storage.appdomain.cloud | 10.1.129.208 | 10.1.129.93 | s3.private.mil.eu.cloud-object-storage.appdomain.cloud |
{: caption="Madrid" caption-side="top"}

### Amsterdam (unchanged)
{: #cos-notices-eu-cr-tethered-endpoints-amsterdam}

These IP addresses are not changing and are provided for reference.
{: note}

| Hostname | IPs |
|---|---|
| s3.ams.eu.cloud-object-storage.appdomain.cloud | 159.8.199.243 |
| s3.direct.ams.eu.cloud-object-storage.appdomain.cloud | 161.26.29.11 |
| s3.private.ams.eu.cloud-object-storage.appdomain.cloud |  10.1.129.91 |
{: caption="Amsterdam" caption-side="top"}

### Frankfurt (unchanged)
{: #cos-notices-eu-cr-tethered-endpoints-frankfurt}

These IP addresses are not changing and are provided for reference.
{: note}

| Hostname | IPs |
|---|---|
| s3.fra.eu.cloud-object-storage.appdomain.cloud | 159.122.119.243 |
| s3.direct.fra.eu.cloud-object-storage.appdomain.cloud | 161.26.37.11 |
| s3.private.fra.eu.cloud-object-storage.appdomain.cloud | 10.1.129.92 |
{: caption="Frankfort" caption-side="top"}

## Static Web Endpoints
{: #cos-notices-eu-cr-static-web-endpoints}

### Anycast Static Web Endpoints
{: #cos-notices-eu-cr-anycast-static-web-endpoints}

These IP addresses are not changing and are provided for reference.
{: note}

| Hostname | IPs |
|---|---|
| s3-web.eu.cloud-object-storage.appdomain.cloud | 67.228.255.198 |
| s3-web.direct.eu.cloud-object-storage.appdomain.cloud | 161.26.0.58 |
| s3-web.private.eu.cloud-object-storage.appdomain.cloud | 10.1.129.118 |
{: caption="Anycast Static Web Endpoint" caption-side="top"}

## Static Web Tethered Endpoints (Datacenter-specific)
{: #cos-notices-eu-cr-static-web-tethered-endpoints}

### Madrid Static Web (replacing Milan)
{: #cos-notices-eu-cr-static-web-endpoints-madrid}

| New Hostname | New IPs | Retired IPs | Old Hostname |
|---|---|---|---|
| s3-web.mad.eu.cloud-object-storage.appdomain.cloud | 13.122.118.50 | 159.122.138.98 | s3-web.mil.eu.cloud-object-storage.appdomain.cloud |
| s3-web.direct.mad.eu.cloud-object-storage.appdomain.cloud | 161.26.221.101 | 161.26.0.61 | s3-web.direct.mil.eu.cloud-object-storage.appdomain.cloud |
| s3-web.private.mad.eu.cloud-object-storage.appdomain.cloud | 10.1.129.209 | 10.1.129.121 | s3-web.private.mil.eu.cloud-object-storage.appdomain.cloud |
{: caption="Madrid Static Web" caption-side="top"}

### Amsterdam Static Web (unchanged)
{: #cos-notices-eu-cr-static-web-endpoints-amsterdam}

These IP addresses are not changing and are provided for reference.
{: note}

| Hostname | IPs |
|---|---|
| s3-web.ams.eu.cloud-object-storage.appdomain.cloud | 159.8.199.244 |
| s3-web.direct.ams.eu.cloud-object-storage.appdomain.cloud | 161.26.0.59 |
| s3-web.private.ams.eu.cloud-object-storage.appdomain.cloud | 10.1.129.119 |
{: caption="Amsterdam Static Web" caption-side="top"}

### Frankfurt Static Web (unchanged)
{: #cos-notices-eu-cr-static-web-endpoints-frankfurt}

These IP addresses are not changing and are provided for reference.
{: note}

| Hostname | IPs |
|---|---|
| s3-web.fra.eu.cloud-object-storage.appdomain.cloud | 159.122.119.241 |
| s3-web.direct.fra.eu.cloud-object-storage.appdomain.cloud | 161.26.0.60 |
| s3-web.private.fra.eu.cloud-object-storage.appdomain.cloud | 10.1.129.120 |
{: caption="Frankfurt Static Web" caption-side="top"}
