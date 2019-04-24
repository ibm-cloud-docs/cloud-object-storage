---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# Upload data
{: #upload}

After getting your buckets organized, it's time to add some objects. Depending on how you want to use your storage, there are different ways to get data into the system. A data scientist has a few large files that are used for analytics, a systems administrator needs to keep database backups synchronized with local files, and a developer is writing software that needs to read and write millions of files. Each of these scenarios is best served by different methods of data ingest.

## Using the console
{: #upload-console}

Typically, using the web-based console is not the most common way to use {{site.data.keyword.cos_short}}. Objects are limited to 200 MB and the file name and key are identical. Multiple objects can be uploaded at the same time, and if the browser allows for multiple threads each object will be uploaded by using multiple parts in parallel. Support for larger object sizes and improved performance (depending on network factors) is provided by [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics/aspera.html).

## Using a compatible tool
{: #upload-tool}

Some users want to use a stand-alone utility to interact with their storage. As the Cloud Object Storage API supports the most common set of S3 API operations, many S3-compatible tools can also connect to {{site.data.keyword.cos_short}} by using [HMAC credentials](/docs/services/cloud-object-storage/hmac/credentials.html).

Some examples include file explorers like [Cyberduck](https://cyberduck.io/) or [Transmit](https://panic.com/transmit/), backup utilities like [Cloudberry](https://www.cloudberrylab.com/) and [Duplicati](https://www.duplicati.com/), command-line utilities like [s3cmd](https://github.com/s3tools/s3cmd) or [Minio Client](https://github.com/minio/mc), and many others.

## Using the API
{: #upload-api}

Most programmatic applications of Object Storage use an SDK (such as [Java](/docs/services/cloud-object-storage/libraries/java.html), [node.js](/docs/services/cloud-object-storage/libraries/node.html), or [Python](/docs/services/cloud-object-storage/libraries/python.html)) or the [Cloud Object Storage API](/docs/services/cloud-object-storage/api-reference/about-api.html). Typically objects are uploaded in [multiple parts](/docs/services/cloud-object-storage/basics/multipart.html), with part size and number of parts configured by a Transfer Manager class.
