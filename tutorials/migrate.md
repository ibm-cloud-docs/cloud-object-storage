---

copyright:
  years: 2020
lastupdated: "2020-04-03"

keywords: migrate, aws, object storage

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

# Migrating from AWS
{: #migrate}

Migrating your data is a complex and daunting task, but don't let it stop you from making the right decision. Using the right tools will make moving from Amazon&trade; to {{site.data.keyword.cos_full}} more secure, globally accessible, and completed with confidence.
{: shortdesc}

The high-speed data transfer option from the integrated [IBM Aspera&reg; service](/docs/cloud-object-storage?topic=cloud-object-storage-aspera) makes it easy to transfer data to {{site.data.keyword.cos_full_notm}}.
{: tip}

## Before you begin
{: migrate-preparation}

Determine your goals and process for your migration before starting your migration. You may also consider training and partnerships to be beneficial. Your planning and assessment stage will consider many possibilities, including security and technical capabilities.

Documentation for any project will help keep you keep track of your resources as well as your goals. You may benefit by updating your existing projects to use {{site.data.keyword.cos_full_notm}} libraries like those for ([Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-node)). If you're interested in programmer interfaces, the [REST API](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) will provide an in-depth look at operations and configurations.

## Provision and configure {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. Create an instance of {{site.data.keyword.cos_full_notm}} from the [catalog](https://cloud.ibm.com/catalog/services/cloud-object-storage).
  2. Create any buckets that you anticipate will be needed to store your transferred data. If you haven't already, read through the [getting started guide](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started) to familiarize yourself with key concepts such as [endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) and [storage classes](/docs/cloud-object-storage/basics?topic=cloud-object-storage-classes).
  3. While {{site.data.keyword.cos_short}} is compatible with the S3 API, it may be necessary to create new credentials, or bring your own keys for your projects. Managing [encryption](/docs/cloud-object-storage?topic=cloud-object-storage-encryption) provides insights into security. Refer to product documentation on [{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) for more information.

## Determine your solution
{: migrate-options}

Integrated solutions provide comprehensive guides to migration, as shown in the [{{site.data.keyword.icp4i_full_notm}}](https://www.ibm.com/cloud/cloud-pak-for-integration/high-speed-data-transfer1){: external). You may also want to investigate third party migration tools as part of your process. Both CLI and gui tools are readily available for use in migration.

* `rclone`

### Migrate your data
{: migrate-data}

Based on the process and tools you've chosen, you will want to choose a strategy for migrating your data. Will your process incorporate a live migration of data? Will your team need to convert virtual machines?

As you work the migration of your data using the process you've outlines, you will want to validate and verify the results.

## Validating your migration from AWS
{: migrate-testing}

Integrated query-in-place dashboards allows you to see analytics based directly on your data. Using [{{site.data.keyword.mon_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration), you can test and validate your progress in pre-built solutions.