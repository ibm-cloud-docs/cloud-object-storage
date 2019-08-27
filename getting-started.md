---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Getting started tutorial
{: #getting-started}

This getting started tutorial walks through the steps that are needed to create buckets, upload objects, and set up access policies to allow other users to work with your data.
{: .shortdesc}

## Before you begin
{: #gs-prereqs}

You need:
  * An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com)
  * An [instance of {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * And some files on your local computer to upload.
{: #gs-prereqs}

 This tutorial takes a new user through the first steps with the {{site.data.keyword.cloud_notm}} Platform console. Developers who want to get started with the API, see the [Developer's Guide](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) or [API overview](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Create some buckets to store your data
{: #gs-create-buckets}

  1. [Ordering {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) creates a _service instance_. {{site.data.keyword.cos_full_notm}} is a multi-tenant system, and all instances of {{site.data.keyword.cos_short}} share physical infrastructure. You're automatically redirected to the service instance where you can start creating buckets. Your {{site.data.keyword.cos_short}} instances are listed under **Storage** in [the resource list](https://cloud.ibm.com/resources).

The terms 'resource instance' and 'service instance' refer to the same concept, and can be used interchangeably.
{: tip}

  1. Follow **Create bucket** and choose a unique name. All buckets in all regions across the globe share a single namespace. Ensure that you have the [correct permissions](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) to create a bucket.

  **Note**: When you create buckets or add objects, be sure to avoid the use of Personally Identifiable Information (PII). PII is information that can identify any user (natural person) by name, location, or any other means.
  {: tip}

  1. Choose a wanted [level of _resiliency_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) first, and then a _location_ where you would like your data to be physically stored. Resiliency refers to the scope and scale of the geographic area across which your data is distributed. _Cross Region_ resiliency spreads your data across several metropolitan areas, while _Regional_ resiliency spreads data across a single metropolitan area. A _Single Data Center_ distributes data across devices within a single site only.
  2. Choose the [bucket's _storage class_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes), which is a reflection of how often you expect to read the stored data and determines billing details. Follow the **Create** link to create and access your new bucket.

Buckets are a way to organize your data, but they're not the sole way. Object names (often referred to as _object keys_) can use one or more forward slashes for a directory-like organizational system. You then use the portion of the object name before a delimiter to form an _object prefix_, which is used to list related objects in a single bucket through the API.
{: tip}


## Add some objects to your buckets
{: #gs-add-objects}

Now go ahead and go to one of your buckets by selecting it from the list. Click **Add Objects**. New objects overwrite existing objects with the same names within the same bucket. When you use the console to upload objects the object name always matches the file name. There doesn't need to be any relationship between the file name and the object key if you're using the API to write data. Go ahead and add a handful of files to this bucket.

Objects are limited to 200 MB when uploaded through the console unless you use the [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload) plug-in. Larger objects (up to 10 TB) can also be [split into parts and uploaded in parallel using the API](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects). Object keys can be up to 1024 characters in length, and it's best to avoid any characters that might be problematic in a web address. For example, `?`, `=`, `<`, and other special characters might cause unwanted behavior if not URL-encoded.
{:tip}

## Invite a user to your account to administer your buckets and data
{: #gs-invite-user}

Now you're going to bring in another user and allow them to act as an administrator for the instance and any data stored in it.

  1. First, to add the new user you need to leave the current {{site.data.keyword.cos_short}} interface and head for the IAM console. Go to the **Manage** menu and follow the link at **Access (IAM)** > **Users**. Click **Invite users**.
	<img alt="IAM invite users" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`Figure 1: IAM invite users`
  2. Enter the email address of a user you want to invite to your organization, then expand the **Services** section and select "Resource" from the **Assign access to** menu. Now choose "Cloud Object Storage" from the **Services** menu.
	<img alt="IAM Services" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`Figure 2: IAM Services`
  3. Now, three more fields appear: _Service instance_, _Resource Type_, and _Resource ID_. The first field defines which instance of {{site.data.keyword.cos_short}} the user can access. It can also be set to grant the same level of access to all instances of {{site.data.keyword.cos_short}}. We can leave the other fields blank for now.
	<img alt="IAM invite users" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`Figure 3: IAM invite users`
  4. The check box under **Select roles** determines the set of actions available to the user. Select the "Administrator" platform access role to allow the user grant other [users and service IDs](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) access to the instance. Select the "Manager" service access role to allow the user to manage the {{site.data.keyword.cos_short}} instance as well as create and delete buckets and objects. These combinations of a _Subject_ (user), _Role_ (Manager), and _Resource_ ({{site.data.keyword.cos_short}} service instance) together form [IAM policies](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam). For more detailed guidance on roles and policies, [see the IAM documentation](/docs/iam?topic=iam-userroles).
	<img alt="IAM roles" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`Figure 4: IAM select roles`
  5. {{site.data.keyword.cloud_notm}} uses Cloud Foundry as the underlying account management platform, so it's necessary to grant a minimal level of Cloud Foundry access in order for the user to access your organization in the first place.  Select an organization from the **Organization** menu, and then select "Auditor" from both the **Organizational roles** and **Space roles** menus.  Setting Cloud Foundry permissions allows the user to view services available to your organization, but not change them.

## Give developers access to a bucket.
{: #gs-bucket-policy}

  1. Navigate to the **Manage** menu and follow the link at **Access(IAM)** > **Service IDs**.  Here you can create a _service ID_, which serves as an abstracted identity bound to the account. Service IDs can be assigned API keys and are used in situations where you don't want to tie a particular Developer's identity to a process or component of an application.
	<img alt="IAM Service Ids" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`Figure 5: IAM Service Ids`
  2. Repeat the above process but in step 3, choose a particular service instance, and enter "bucket" as the _Resource Type_ and the full CRN of an existing bucket as the _Resource ID_.
  3. Now the service ID can access that particular bucket, and no others.

## Next steps
{: #gs-next-steps}

Now that you are familiar with your object storage via the web-based console, you might be interested in doing a similar workflow from the command line using  the `ibmcloud cos` command-line utility for creating the service instance and interacting with IAM, and `curl` for accessing COS directly. [Check out the API overview](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) to get started.
