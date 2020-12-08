---

copyright:
  years: 2020
lastupdated: "2020-12-15"

keywords: tagging, objects, tags, metadata

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
{:term: .term}
{:table: .aria-labeledby="caption"}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}
{:console: .ph data-hd-programlang='Console'}
{:http: .ph data-hd-programlang='curl'}
{:aws: .ph data-hd-programlang='aws'}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}

# Tagging objects in {{site.data.keyword.cos_full_notm}}
{: #object-tagging}

Your data can be expressly defined, categorized, and classified in {{site.data.keyword.cos_full}} using associated metadata, called "tags." This topic will show you how to take full control in "tagging" the objects representing your data. 
{: shortdesc}

## Objects and metadata
{: #object-tagging-overview}

Organizing your collection of data using metadata is much more sophisticated than other methods, like using key prefixes like folders. Being able to manage your metadata for organizing your data is the purpose of [tagging](#x2040924){: term}.

### Tagging Objects
{: #object-tagging-overview}

Managing tags describing your objects can be performed through various interfaces and architectures. Using the [Console](https://cloud.ibm.com){: external} provides a graphical user interface. Using the command line requires tools like [`curl`](/docs/cloud-object-storage?topic=cloud-object-storage-curl) and the knowledge of how it interacts with {{site.data.keyword.cos_short}}.

### Before you begin
{: #object-tagging-prereqs}

You need:

* An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com/login)
* An [instance of {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage/basics?topic=cloud-object-storage-provision) and a bucket created for this purpose
* An [IAM API key](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) with Writer access to your {{site.data.keyword.cos_short}} instance 
* Objects (files) that have been uploaded to your bucket, or can be uploaded so that they may be tagged

### Getting the SDK or CLI
{: #object-tagging-obtain-sdks}

Specific instructions for downloading and installing the SDK is available in [Using Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python){: python}[Using Node.js](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-node){: javascript}[Using Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java){: java}[Using Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go){: external}{: go}. You can find out more information about using the {{site.data.keyword.cloud_notm}} [CLI](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli), or use the S3-compatible CLI from [AWS](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli). 

### Creating tags
{: #object-tagging-create-tags}

### Editing tags
{: #object-tagging-create-tags}

### Removing tags
{: #object-tagging-delete-tags}

## Next Steps
{: #object-tagging-next-steps}

