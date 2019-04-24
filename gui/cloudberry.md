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

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup is a flexible utility that allows users to back up some or all of a local filesystem to an S3 API compatible object storage system. Free and Professional versions are available for Windows, MacOS, and Linux and support a number of popular cloud storage services including {{site.data.keyword.cos_full}}.  Cloudberry Backup can be downloaded from [cloudberrylab.com](https://www.cloudberrylab.com/).

Cloudberry Backup includes many useful features including:

* Scheduling
* Incremental & Block level backups
* Command-line interface
* Email notifications
* Compression (*Pro version only*)

## Cloudberry Explorer
{: #cloudberry-explorer}

A new product from Cloudberry Labs offers a familiar, file management user interface for {{site.data.keyword.cos_short}}.  [Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} also comes in Free and Pro versions but is currently only available for Windows.  Key features include:

* Folder/Bucket sync
* Command-line interface
* ACL management
* Capacity reports

Pro Version also includes:
* Search 
* Encryption/Compression
* Resumable Upload
* FTP/SFTP support

## Using Cloudberry with Object Storage
{: #cloudberry-cos}

Key points to remember when configuring Cloudberry products to work with {{site.data.keyword.cos_short}}:

* Select `S3 Compatible` from the list of options
* Only [HMAC credentials](/docs/services/cloud-object-storage/hmac/credentials.html#using-hmac-credentials) are currently supported
* A separate connection is required for each bucket
* Ensure the `Endpoint` specifed in the connection matches the region of the selected bucket (*backup will fail due to inaccessible destination*). For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
