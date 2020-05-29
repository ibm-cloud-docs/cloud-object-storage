---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: gui, desktop, backup, cloudberry

subcollection: cloud-object-storage


---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Cloudberry Labs
{: #cloudberry}

Leverage {{site.data.keyword.cos_full}} to make the most of keeping your customer data protected.
{: shortdesc}

## Cloudberry backup
{: #cloudberry-backup}

Cloudberry backup is a flexible utility that allows users to back up a local file system to an object store. Both free and professional editions can be downloaded from [cloudberrylab.com](https://www.cloudberrylab.com/).

Cloudberry backup includes many useful features:

* Scheduling
* Incremental & Block level backups
* Email notifications
* Compression (*Pro version only*)

## Cloudberry Explorer
{: #cloudberry-explorer}

[Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){: external} is a simple file browser that provides a set of basic features:

* Folder (bucket) sync
* ACL management
* Capacity reports

The professional edition has a few more features:
* Search 
* Encryption and compression
* Resumable Upload

## Using Cloudberry with Object Storage
{: #cloudberry-cos}

Key points to remember when you configure a Cloudberry product to work with {{site.data.keyword.cos_short}}:

* Select `S3 Compatible` from the list of options
* Only [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) are currently supported
* A separate connection is required for each bucket
* Ensure the `Endpoint` specified in the connection matches the region of the selected bucket (*backup fails because of an inaccessible destination*). For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

