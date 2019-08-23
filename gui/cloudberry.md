---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry backup
{: #cloudberry-backup}

Cloudberry Backup is a flexible utility that allows users to back up a local file system to an object store. Both free and professional editions can be downloaded from [cloudberrylab.com](https://www.cloudberrylab.com/).

Cloudberry Backup includes many useful features:

* Scheduling
* Incremental & Block level backups
* Command-line interface
* Email notifications
* Compression (*Pro version only*)

## Cloudberry Explorer
{: #cloudberry-explorer}

[Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} is available for Windows. This simple file browser provides a set of basic features:

* Folder (bucket) sync
* Command-line interface
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
* Only [HMAC credentials](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) are currently supported
* A separate connection is required for each bucket
* Ensure the `Endpoint` specified in the connection matches the region of the selected bucket (*backup fails because of an inaccessible destination*). For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
