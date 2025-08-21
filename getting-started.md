---

copyright:
  years: 2017, 2025
lastupdated: "2025-08-19"

keywords: IBM cloud object storage, cloud object storage, object storage, storage, cross origin resource sharing, cors, special characters

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Getting started with {{site.data.keyword.cos_full_notm}}
{: #getting-started-cloud-object-storage}

{{site.data.keyword.cos_full}} stores encrypted and dispersed data across multiple geographic locations. This getting started tutorial walks through the steps that are needed to use {{site.data.keyword.cos_full_notm}} to create buckets, upload objects, and set up access policies to allow other users to work with your data.
{: shortdesc}

## Before you begin
{: #getting-started}

You need the following to get started with {{site.data.keyword.cos_full_notm}}:
- An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com)
- An [instance of {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-provision)
- Some files on your local computer to upload to {{site.data.keyword.cos_short}}.

This tutorial takes a new user through the first steps with the {{site.data.keyword.cloud_notm}} Platform console. Developers who want to get started with the API, see the [Developer's Guide](/docs/cloud-object-storage?topic=cloud-object-storage-gs-dev) or [API overview](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api).

## Create some buckets to store your data
{: #gs-create-buckets}

1. [Ordering {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-provision) creates a _service instance_. {{site.data.keyword.cos_full_notm}} is a multi-tenant system, and all instances of {{site.data.keyword.cos_short}} share physical infrastructure. You will be automatically redirected to the service instance upon its creation. Your {{site.data.keyword.cos_short}} instances are listed under **Storage** in [the resource list](https://cloud.ibm.com/resources).

   The terms 'resource instance' and 'service instance' refer to the same concept, and can be used interchangeably.
   {: tip}


1. You will need a bucket before you can store data in your new _service instance_. To **Create a bucket**, start by choosing a unique name. All buckets in all regions across the globe share a single namespace. Ensure that you have the [correct permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions) to create a bucket.

   When you name buckets or objects, be sure to avoid the use of Personally Identifiable Information (PII). PII is information that can identify any user (natural person) by name, location, or any other means.
   {: tip}

   Bucket names are required to be DNS addressable and are not case-sensitive.
   {: tip}

2. First, choose the [level of _resiliency_](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) you want. Then, choose a _location_ where you would like your data to be physically stored. Resiliency refers to the scope and scale of the geographic area across which your data is distributed. _Cross Region_ resiliency spreads your data across several metropolitan areas, while _Regional_ resiliency spreads data across a single metropolitan area. A _Single Data Center_ distributes data across devices within a single site only.

3. Choose the [bucket's _storage class_](/docs/cloud-object-storage?topic=cloud-object-storage-classes) to accurately reflect how often you expect to read the stored data. This is important as it determines your billing details. Follow the **Create** link to create and access your new bucket.

4. Determine the advanced configurations, if any, suitable to your content. You can store data by transitioning from any of the storage tiers (Standard, Vault, Cold Vault and Flex) to long-term offline archive or use the online Cold Vault option.

Buckets are a way to organize your data, but they're not the sole way. Object names (often referred to as _object keys_) can use one or more forward slashes for a directory-like organizational system. You then use the portion of the object name before a delimiter to form an _object prefix_, which is used to list related objects in a single bucket through the {{site.data.keyword.cos_short}} API.
{: tip}

## Add some objects to your buckets
{: #gs-add-objects}

Now go ahead and go to one of your buckets by selecting it from the list. Click **Add Objects**. New objects overwrite existing objects with the same names within the same bucket. When you use the console to upload objects the object name always matches the file name. There doesn't need to be any relationship between the file name and the object key if you're using the API to write data. Go ahead and add a handful of files to this bucket.

Objects are limited to 200 MB when uploaded through the console unless you use the [Aspera high-speed transfer](/docs/cloud-object-storage?topic=cloud-object-storage-upload) plug-in or use Cross-Origin Resource Sharing (CORS), by setting the CORS headers. Larger objects (up to 10 TB) can also be [split into parts and uploaded in parallel using the API](/docs/cloud-object-storage?topic=cloud-object-storage-large-objects). Object keys can be up to 1024 characters in length, and it's best to avoid any characters that might be problematic in a web address. For example, `?`, `=`, `<`, and other special characters might cause unwanted behavior if not URL-encoded.
{: tip}

If an object with a special character is uploaded to a bucket, it may cause problems with displaying and accessing it in the UI. In these cases, the object should be deleted and re-uploaded with a more standard name. You may delete these objects with Expiration or Lifecycle rules if the UI and CLI deletions are not successful. Avoid special characters to prevent any difficulties with accessing or deleting the object.
{: attention}

## How do I invite a user to administer buckets and data?
{: #gs-invite-user}
{: help}
{: support}

Bringing in another user and allow them to act as an administrator for the instance and any data stored in it is an important way to distribute responsibility for administering your {{site.data.keyword.cos_full_notm}} instance.

1. To add the new user you first need to leave the current {{site.data.keyword.cos_short}} interface and head for the IAM console. Go to the **Manage** menu and follow the link at **Access (IAM)** > **Users**. Click **Invite users**.

1. Enter the email address of a user you want to invite to your organization, then expand the **Services** section and select "Resource" from the **Assign access to** menu. Now choose "Cloud Object Storage" from the **Services** menu.

1. Now, three more fields appear: _Service instance_, _Resource Type_, and _Resource ID_. The first field defines which instance of {{site.data.keyword.cos_short}} the user can access. It can also be set to grant the same level of access to all instances of {{site.data.keyword.cos_short}}. We can leave the other fields blank for now.

1. The check box under **Select roles** determines the set of actions available to the user. Select the "Administrator" platform access role to allow the user grant other [users and service IDs](/docs/cloud-object-storage?topic=cloud-object-storage-iam-overview) access to the instance. Select the "Manager" service access role to allow the user to manage the {{site.data.keyword.cos_short}} instance as well as create and delete buckets and objects. These combinations of a _Subject_ (user), _Role_ (Manager), and _Resource_ ({{site.data.keyword.cos_short}} service instance) together form [IAM policies](/docs/cloud-object-storage?topic=cloud-object-storage-iam-overview). For more detailed guidance on roles and policies, [see the IAM documentation](/docs/account?topic=account-userroles).



## Give developers access to a bucket.
{: #gs-bucket-policy}

1. Navigate to the **Manage** menu and follow the link at **Access(IAM)** > **Service IDs**.  Here you can create a _service ID_, which serves as an abstracted identity bound to the account. Service IDs can be assigned API keys and are used in situations where you don't want to tie a particular Developer's identity to a process or component of an application.


1. Repeat the above process but in step 3, choose a particular service instance, and enter "bucket" as the _Resource Type_ and the [full CRN](/docs/cloud-object-storage?topic=cloud-object-storage-troubleshooting-cos#troubleshooting-cos-details) of an existing bucket as the _Resource ID_.
1. Now the service ID can access that particular bucket, and no others.

## Next steps
{: #gs-next-steps}

Now that you are familiar with your object storage via the web-based console, you might be interested in doing a similar workflow from the command line. Check out using the `ibmcloud cos` command-line utility to create a service instance and interacting with IAM. And you can further use `curl` for accessing COS directly. [Check out the API overview](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api) to get started.
