---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: administrator, object storage, access, iam

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# For administrators
{: #administrators}

Storage and system administrators familiar with {{site.data.keyword.cos_full}} can easily and conveniently manage users, create and rotate API keys, and grant roles to users and services. 
{: shortdesc}

If you haven't already, go ahead and read through the [getting started tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to familiarize yourself with the core concepts of buckets, objects, and users.

## Set up your storage
{: #administrators-setup}

First, you need to have at least one Object Storage resource instance, and some buckets to store data in. How do you want to segment access to your data? Where do you want your data to physically reside? How often will the data is accessed?

### Segmenting access
{: #administrators-access}

You can segment access at two levels: at the resource instance level and at the bucket level. 

Perhaps you want to make sure that only a development team can access particular instances of Object Storage. Or, you want to ensure that only the application your team is making can edit the data that is stored. You might want your developers with access to the cloud platform to only be able to read data for troubleshooting reasons, but not change anything. These are examples of service level policies.

Now what if the development team, or any individual user, who has viewer access to a storage instance, but should be able to directly edit data in one or more buckets? You can use bucket level policies to elevate the level of access that is granted to users within your account. For instance, a user might not be able to create new buckets, but can create and delete objects within existing buckets.

## Manage access
{: #administrators-manage-access}

IAM is based on a fundamental concept: A _subject_ is granted a _role_ on a _resource_.

There are two basic types of subjects: a _user_ and a _service ID_.

There is another concept, a _service credential_. A service credential is a collection of important information that is needed to connect to an instance of {{site.data.keyword.cos_full}}. It gives a user an identifier for the instance of {{site.data.keyword.cos_full_notm}} (that is, the Resource Instance ID), service and authorization endpoints, and a means of associating the subject with an API key (that is, Service ID). When you create the service credential you have the option of either associating it with an existing service ID, or creating a new service ID.

You might want your development team to be able to use the console to view Object Storage instances and Kubernetes clusters. They would need `Viewer` roles on the Object Storage resources and `Administrator` roles on the Container Service. The `Viewer` role allows for the user to only see that the instance exists, and to view existing credentials, but **not** to view buckets and objects. When the service credentials were created, they were associated with a service ID. This service ID would need to have the `Manager` or `Writer` role on the instance to be able to create and delete buckets and objects.

For more information on IAM roles and permissions, see [the IAM overview](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview).
