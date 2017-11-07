---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# For administrators

Storage and system administrators who need to configure object storage and manage access to data can take advantage of IBM Cloud Identity and Access Management (IAM) to manage users, create and rotate API keys, and grant roles to users and services. If you haven't already, go ahead and read through the [getting started tutorial](/docs/services/cloud-object-storage/index.html) to familiarize yourself with the core concepts of buckets, objects, and users.

## Set up your storage

First things first, you need to have at least one object storage resource instance, and some buckets to store data in.  Think of these buckets in terms of how you want to further segment access to your data, where you want your data to physically reside, and how often the data will be accessed.

### Segmenting access

There are two levels where you can segment access: at the resource instance level and at the bucket level.

Perhaps you want to make sure that a development team can only access the instances of object storage that they are working with and not those used by other teams.  Or you want to ensure that only the software your team is making can actually edit the data being stored, so you want your developers with access to the cloud platform to only be able to read data for troubleshooting reasons.  These are examples of service level policies.

Now if the development team, or any individual user, who has viewer access to a storage instance, but should be able to directly edit data in one or more buckets, then you can use bucket level policies to elevate the level of access granted to users within your account. For instance, a user might not be able to create new buckets, but can create and delete objects within existing buckets.

## Manage access

IAM is based on a fundamental concept: A _subject_ is granted a _role_ on a _resource_.

There are two basic types of subjects: a _user_ and a _service ID_.

There is another concept, a _service credential_.  A service credential is a collection of important information needed to connect to an instance of {{site.data.keyword.cos_full}}.  This includes at a minimum an identifier for the instance of {{site.data.keyword.cos_full_notm}} (ie the Resource Instance ID), service/auth endpoints, and a means of associating the subject with an API key (ie Service ID).  When you create the service credential you have the option of either associating it with an existing service ID, or creating a new service ID.

So if you want to allow your development team to be able to use the console to view object storage instances and Kubernetes clusters, they would need `Viewer` roles on the object storage resources and `Administrator` roles on the Container Service.  Note that the `Viewer` role only allows for the user to see that the instance exists, and to view existing credentials, **not** to view buckets and objects.  When the service credentials were created, they were associated with a service ID.  This service ID would need to have the  `Manager` or `Writer` role on the instance to be able to create and destroy buckets and objects.

For more information on IAM roles and permissions, see [the IAM overview](/docs/services/cloud-object-storage/iam/overview.html).
