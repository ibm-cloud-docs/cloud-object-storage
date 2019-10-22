---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-10-15"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:external: target="_blank" .external}
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


# Code patterns for deleting multiple objects, 
{: #deleting-multiple-objects-patterns}

This overview of code patterns focuses on the steps that are needed to access a list of all items in a bucket for the purpose of deleting each one sequentially.
{: .shortdesc}

## Before you begin
{: #dmop-prereqs}

Specific instructions for downloading and installing SDKs are available for [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python){: external}, [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node){: external}, [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java){: external}, and [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go){: external}.

You need:
  * An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com)
  * An [instance of {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * Configured and operational use of {{site.data.keyword.cos_full}} SDKs for your choice of Java, Python, NodeJS, or Go.
{: #dmop-prereqs}

