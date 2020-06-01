---

copyright:
  years: 2017, 2020
lastupdated: "2020-05-11"

keywords: provision, create, service

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

# Provisioning storage
{: #provision}

Getting data into your instance of {{site.data.keyword.cos_full}} requires just a few steps before you provision your new storage.
{: shortdesc}

## Creating a {{site.data.keyword.cloud_notm}} Platform account
{: #provision-account}

Before you create a new {{site.data.keyword.cos_full_notm}} storage instance, it's necessary to create a customer account first.

1. Go to [cloud.ibm.com](https://cloud.ibm.com/){: external} and click **Create a Free Account**.
2. Complete the form with your email address, name, region, and phone number. Choose a password.
3. Follow the link provided by the confirmation email, and follow the links to log in to the {{site.data.keyword.cloud}} Platform.
4. Next, create a new object storage service instance!

## Creating a service instance
{: #provision-instance}

1. Log in to [the console](https://cloud.ibm.com/){: external}.
1. Navigate to the catalog, by clicking **Catalog** in the navigation bar.

   ![COS Catalog](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/catalog.jpg){: caption="Figure 1. Create an {{site.data.keyword.cloud_notm}} instance"}
   
1. Look for the **Object Storage** tile in the storage section and select it.

   ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/object-storage-card.jpg){: caption="Figure 2. Select {{site.data.keyword.cloud_notm}}"}

1. Give the service instance a name and choose either the lite or standard plan.
1. Click **Create** and you're automatically redirected to your new instance.

It is also possible to manage resources using the [{{site.data.keyword.cloud}} Platform CLI](/docs/resources?topic=resources-manage_resource):

```bash
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{: codeblock}

## Deleting a service instance
{: delete-instance}

When a service instance is deleted, the data is not deleted immediately.  Instead, it is scheduled for reclamation (by default this is set to take 7 days), after which the data is irreversibly destroyed, and the bucket names will be made available for reuse. It is also possible to [restore a deleted resource](/docs/resources?topic=resources-manage_resource#restore-resource) that has not yet been reclaimed.

It is possible to check the status of a reclamation, as well as force or cancel a scheduled reclamation using the [the {{site.data.keyword.cloud}} Platform CLI](/docs/cli?topic=cloud-cli-ibmcloud_commands_resource#ibmcloud_resource_reclamations).

It is not possible to delete a Service Instance if there is a bucket with an active Immutable Object Storage policy or legal hold on any objects.  The policy will need to expire before the data can be deleted. It isn't possible to delete a Service Instance if there is a permanent retention policy in place. 
{: important}

Currently, the reclamation can be scheduled for {{site.data.keyword.cos_full_notm}} standard plan instances only.
{: note}
