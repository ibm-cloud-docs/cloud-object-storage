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

Your data can be expressly defined, categorized, and classified in {{site.data.keyword.cos_full}} using associated metadata, called "tags." This document will show you how to take full control in "tagging" the objects representing your data. 
{: shortdesc}

## Objects and metadata
{: #object-tagging-overview}

Organizing your data can be a complex task. Basic methods, such as using key prefixes like organizational "folders" are a great start to hierarchical structures. But for more complex organization, you will need custom "[tags](#x2040924){: term}." Your metadata can describe the relationships inherent to your data, and provide more organization than titles or folders. Unlike mere labels, there are two parts to a tag: a `key` and a `value`, defined individually according to your needs.

### Tagging Objects
{: #object-tagging-overview}

Managing tags describing your objects can be performed through various interfaces and architectures. Using the [Console](https://cloud.ibm.com){: external} provides a graphical user interface. Using the command line requires tools like [`curl`](/docs/cloud-object-storage?topic=cloud-object-storage-curl) and the knowledge of how it interacts with {{site.data.keyword.cos_short}}.

### Before you begin
{: #object-tagging-prereqs}

You need:

* An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com/login)
* An [instance of {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage/basics?topic=cloud-object-storage-provision) and a bucket created for this purpose
* An [IAM API key](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) with Writer access to your {{site.data.keyword.cos_short}} bucket or instance 
* Objects (files) that have been uploaded to your bucket, or can be uploaded so that they may be tagged

### Getting the SDK or CLI
{: #object-tagging-obtain-sdks}

Specific instructions for downloading and installing the SDK is available for [Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.JS](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-node), [Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java), and [Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go){: external}{: go}. You can find out more information about using the {{site.data.keyword.cloud_notm}} [CLI](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli), or use the S3-compatible CLI from [AWS](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli). 

### Reading tags
{: #object-tagging-reading-tags}

Log in to the console, selecting your instance of {{site.data.keyword.cos_full_notm}} and your bucket where your data is represented. After you've uploaded files to your bucket, you can manage your tags right in place. Place the cursor over the ellipses at the end of any row representing your data, and select "Manage your tags" from the options in the menu.
{: console}

![Manage your tags](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/object-manage-tags.jpg){: console}

A properly formed "GET" request is all that is required for accessing the tags for your objects using `curl`. The resulting XML object is also shown.
{: http}

```bash
curl 'https://<endpoint>/<bucketname>/<objectname>?tagging' \
-H 'Authorization: bearer <token>' -H 'ibm-service-instance-id: <resource_instance_id>'
```
{: pre}
{: http}

```shell
curl -X "PUT" "https://s3.test.cloud-object-storage.sample.appdomain.cloud/taggingtest/example-file.csv?tagging" -H "Authorization: bearer ...iOiIyMDIwMTIwNzE0NDkiLCJh..." -H "ibm-service-instance-id: 7nnnnn52-2nn0-nna9-bann-7nnnnn4cc4e7" --data "<Tagging><TagSet><Tag><Key>source</Key><Value>text</Value></Tag></TagSet></Tagging>"
```
{: screen}
{: http}

### Creating tags
{: #object-tagging-create-tags}

The objects in your buckets represent your data in a fixed form, that is, saved to {{site.data.keyword.cos_short}}. 

As noted previously, log in to your instance and navigate to the bucket and object you wish to "tag." In the panel that appears, add tags by typing text into the `key` and `value` fields as desired.
{: console}

![Add tags in place](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/object-add-tags.jpg){: console}

If you do not click on "save" when completing your changes, a dialog box will remind you of the consequences. That is, changes are discarded unless saved.
{: console}

![Unsaved changes are discarded](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/object-discard-changes.jpg){: console}

You will have to authorize in order to tag your data. The examples here use bearer tokens generated using [this example](/docs/cloud-object-storage?topic=cloud-object-storage-curl#curl-token). In addition to the bucket identifier and object key, you will also need the correct [endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) and the resource instance id from the service instance id in your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials). Note the query string for adding tags: **`?tagging`**.
{: http}

```bash
curl -X "PUT" 'https://<endpoint>/<bucketname>/<objectname>?tagging' \
-H 'Authorization: bearer <token>' -H 'ibm-service-instance-id: <resource_instance_id>' -H "content-type: text/plain" --data "<Tagging><TagSet><Tag><Key>source</Key><Value>text</Value></Tag></TagSet></Tagging>"
```
{: pre}
{: http}

The example describes as shown a tag with a `key` of 'source' and a `value` of 'text' in the XML sent as data in the body of the request. The [schema](/docs/cloud-object-storage?topic=cloud-object-storage-object-operations#object-operations-add-tags) of the XML has to validate upon execution. If you want to add multiple tags, duplicate the 'Tag' node and modify the content of each key and value element to your specifications. There is only one 'TagSet' element for each object, and the 'PUT' command will replace any existing metadata with the values you specified.
{: http}

```shell
curl -X "PUT" "https://s3.test.cloud-object-storage.sample.appdomain.cloud/taggingtest/example-file.csv?tagging" -H "Authorization: bearer ...iOiIyMDIwMTIwNzE0NDkiLCJh..." -H "ibm-service-instance-id: 7nnnnn52-2nn0-nna9-bann-7nnnnn4cc4e7" --data "<Tagging><TagSet><Tag><Key>source</Key><Value>text</Value></Tag></TagSet></Tagging>"
```
{: screen}
{: http}

### Editing tags
{: #object-tagging-edit-tags}


Change the contents of the form fields and press "Save" when complete.
{: console}

![Save tags when complete](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/object-save-tags.jpg){: console}

### Removing tags
{: #object-tagging-delete-tags}

## Next Steps
{: #object-tagging-next-steps}

