---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: object storage, provision, console

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

# Provision storage
{: #provision}

Getting data into your instance of {{site.data.keyword.cos_full}} requires just a few steps before you provision your new storage.
{: shortdesc}

## Creating a {{site.data.keyword.cloud_notm}} Platform account
{: #provision-account}

Before you create a new {{site.data.keyword.cos_full}} storage instance, it's necessary to create a customer account first.

1. Go to [cloud.ibm.com](https://cloud.ibm.com/){: external} and click **Create a Free Account**.
2. Complete the form with your email address, name, region, and phone number. Choose a password.
3. Follow the link provided by the confirmation email, and follow the links to log in to the {{site.data.keyword.cloud}} Platform.
4. Next, create a new object storage service instance!

## Creating a service instance
{: #provision-instance}

1. Log in to [the console](https://cloud.ibm.com/){: external}.
2. Navigate to the catalog, by clicking **Catalog** in the navigation bar.
	<img alt="Provision storage Catalog" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_provision_catalog.png" max-height="200px" />
3. Look for the **Object Storage** tile in the storage section and select it.
	<img alt="Provision storage Object Storage" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_provision_os.png" max-height="200px" />
4. Give the service instance a name and choose either the lite or standard plan.
5. Click **Create** and you're automatically redirected to your new instance.

It is also possible to create an instance using the [{{site.data.keyword.cloud}} Platform CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-getting-started):

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{: codeblock}

## Deleting a service instance
{: delete-instance}

When a service instance is deleted, the data is not deleted immediately.  Instead, it is scheduled for reclamation a week after the user requests deletion, after which the data is irreversibly destroyed, and the bucket names will be made available.  

It is possible to check the status of a reclamation, as well as force or cancel a scheduled reclamation using the [the {{site.data.keyword.cloud}} Platform CLI](/docs/cli?topic=cloud-cli-ibmcloud_commands_resource#ibmcloud_resource_reclamations).

It is not possible to delete a Service Instance if there is a bucket with an active Immutable Object Storage policy or legal hold on any objects.  The policy will need to expire before the data can be deleted. It isn't possible to delete a Service Instance if there is a permanent retention policy in place. 
{: important}

It is only possible to manually manage reclamation for instances with a Standard plan.
{: note}