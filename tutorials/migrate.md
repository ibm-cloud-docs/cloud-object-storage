---

copyright:
  years: 2020
lastupdated: "2020-04-03"

keywords: migrate, amazon, aws

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

## Before you begin
{: migrate-preparation}

Determine your goals and process for your migration before starting your migration. You may also consider training and partnerships to be beneficial. Your planning and assessment stage will consider many possibilities, including security and technical capabilities.

Documentation for any project will help keep you keep track of your resources as well as your goals. After assessing your existing projects, you may benefit by updating them to use {{site.data.keyword.cos_full_notm}} libraries like those for ([Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-node)). If you're interested in programmer interfaces, the [REST API](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) will provide an in-depth look at operations and configurations.

## Provision and configure {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. If you haven't already, create an instance of {{site.data.keyword.cos_full_notm}} from the [Console](https://cloud.ibm.com/catalog/services/cloud-object-storage){: external}.
  2. Create any buckets that you anticipate will be needed to store your transferred data. If you haven't already, read through the [getting started guide](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started) to familiarize yourself with key concepts such as [endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) and [storage classes](/docs/cloud-object-storage/basics?topic=cloud-object-storage-classes).
  3. While {{site.data.keyword.cos_short}} is compatible with the S3 API, it may be necessary to create new credentials, or bring your own keys for your projects. Managing [encryption](/docs/cloud-object-storage?topic=cloud-object-storage-encryption) provides insights into security. Refer to product documentation on [{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) for more information.

## Determine your solution
{: migrate-options}

Sometimes, an [example](https://developer.ibm.com/tutorials/how-to-easily-port-your-app-storage-to-cloud-object-storage/){: external} of a simple migration can illustrate how to convert from AWS concepts to the configuration options available to users of {{site.data.keyword.cos_full_notm}}. But whatever your goals, Once you have provisioned and set your target, it is time to choose a process. 

There are many ways to achieve the goal of migrating your AWS data. Integrated solutions provide comprehensive guides to migration, as shown in the [{{site.data.keyword.icp4i_full_notm}}](https://www.ibm.com/cloud/cloud-pak-for-integration/high-speed-data-transfer1){: external). You may also want to investigate third party migration tools as part of your process. But don't forget that there are many CLI and GUI tools readily available for use as part of your migration.

* [COS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-cli-ic-cos-cli)
* [`rclone`](/docs/cloud-object-storage?topic=cloud-object-storage-rclone)
* [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli)

### Migrate your data
{: migrate-data}

Based on the process and tools you've chosen, you will want to choose a strategy for migrating your data. Will your process incorporate a live migration of data? Will your team need to convert virtual machines?

As you work the migration of your data using the process you've outlines, you will want to validate and verify the results.

### Validating your migration from AWS
{: migrate-testing}

Integrated query-in-place dashboards allows you to see analytics based directly on your data. Using [{{site.data.keyword.mon_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration), you can test and validate your progress in pre-built solutions.

## Next Steps
{: migrate-next-steps}

Get started by visiting the [catalog](https://cloud.ibm.com){: external}, and creating the resources to being your journey from AWS to {{site.data.keyword.cos_full_notm}}. 