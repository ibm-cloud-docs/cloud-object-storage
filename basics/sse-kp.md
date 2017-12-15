---
copyright:
  years: 2017
lastupdated: '2017-11-08'
---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Configuring Server Side Encryption with {{site.data.keyword.keymanagementservicelong_notm}}

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using multiple randomly generated keys and an all-or-nothing-transform. While this default encryption model is remarkably secure, some workloads need to be in possession of the encryption keys used. You can use [{{site.data.keyword.ketmanagementservice_long}}](/docs/services/keymgmt/keyprotect_about.html) to create, add, and manage keys, which you can then associate with your instance of {{site.data.keyword.cos_full}} to encrypt buckets.

{{site.data.keyword.keymanagementserviceshort}} is currently on available in the US-South region.
{: note}

## Before you begin
You'll need:
  * an [{{site.data.keyword.cloud}} Platform account](https://console.bluemix.net/registration/?target=%2Fcatalog%2Finfrastructure%2Fcloud-object-storage)
  * an [instance of {{site.data.keyword.cos_full_notm}}](https://console.bluemix.net/catalog/infrastructure/object-storage-group?env_id=ibm:yp:us-south){: new_window}
  * an [instance of {{site.data.keyword.keymanagementservicelong_notm}}](https://console.ng.bluemix.net/catalog/services/key-protect/?taxonomyNavigation=apps){: new_window}
  * and some files on your local computer to upload.
{: #prereqs}

## Create or add a key in {{site.data.keyword.keymanagementserviceshort}}

Navigate to your instance of {{site.data.keyword.keymanagementserviceshort}} and [generate or enter a key](/docs/services/keymgmt/index.html#getting-started-with-key-protect).

## Associate the key with your {{site.data.keyword.cos_full_notm}} instance

Once your keys exist in {{site.data.keyword.keymanagementserviceshort}}, associate the key with your {{site.data.keyword.cos_full_notm}} instance:

1. Open your {{site.data.keyword.cloud_notm}} dashboard.
2. From the menu bar, click **Manage** &gt; **Account** &gt; **Users**.
3. 

## Grant service authorization

## Create a bucket

