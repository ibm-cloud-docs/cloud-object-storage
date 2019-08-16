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

# 上传数据
{: #upload}

组织了存储区后，现在该添加一些对象了。根据您希望使用存储器的方式，有多种方法可以获取数据来提供给系统。数据研究员有一些用于分析的大型文件，系统管理员需要使数据库备份与本地文件保持同步，而开发者正在编写需要读写数百万个文件的软件。这其中每个场景都可通过不同的数据摄入方法来进行最佳处理。

## 使用控制台
{: #upload-console}

通常，使用基于 Web 的控制台并不是使用 {{site.data.keyword.cos_short}} 的最常见方法。对象限制为 200 MB，并且文件名和密钥完全相同。可以同时上传多个对象，并且如果浏览器支持多个线程，那么将使用分块方式并行上传各个对象。[Aspera 高速传输](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)支持更大的对象大小，并且性能更高（取决于网络因素）。

## 使用兼容工具
{: #upload-tool}

一些用户希望使用独立实用程序来与其存储器进行交互。由于 Cloud Object Storage API 支持最常用的 S3 API 操作集，因此许多与 S3 兼容的工具也可以使用 [HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)来连接到 {{site.data.keyword.cos_short}}。

一些示例包括文件资源管理器（如 [Cyberduck](https://cyberduck.io/) 或 [Transmit](https://panic.com/transmit/)）、备份实用程序（如 [Cloudberry](https://www.cloudberrylab.com/) 和 [Duplicati](https://www.duplicati.com/)）、命令行实用程序（如 [s3cmd](https://github.com/s3tools/s3cmd) 或 [Minio 客户机](https://github.com/minio/mc)），等等。

## 使用 API
{: #upload-api}

Object Storage 的大多数编程应用程序使用 SDK（例如，[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 或 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)）或 [Cloud Object Storage API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。通常，对象以[分块](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)方式上传，分块大小和分块数量由传输管理器类进行配置。
