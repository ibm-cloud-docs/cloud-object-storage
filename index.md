---

copyright:
  years: 2017
lastupdated: "2017-11-05"

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Getting started (Console)

In this getting started tutorial, you'll walk through the steps needed to create buckets, upload objects, and set up access policies to allow other users to work with your data.
{: shortdesc}

## Before you begin
You'll need:
  * an [{{site.data.keyword.cloud}} Platform account](https://console.bluemix.net/registration/?target=%2Fcatalog%2Finfrastructure%2Fcloud-object-storage)
  * an [instance of {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics/order-storage.html)
  * and some files on your local computer to upload.
{: #prereqs}

This guide takes a new user through the first steps with the {{site.data.keyword.cloud_notm}} Platform console, but for developers looking to get started with the API, see the [Developer's Guide](/docs/services/cloud-object-storage/basics/developers.html) or [API overview](/docs/services/cloud-object-storage/api-reference/about-compatibility-api.html).

## Step 1: Create some buckets to store your data
{: #create-buckets}

  1. When you [order {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics/order-storage.html), you create what is called a _resource instance_. {{site.data.keyword.cos_full_notm}} is a multi-tenant system, and all instances of {{site.data.keyword.cos_short}} share the same physical infrastructure.  After creation, you will be automatically redirected to that resource instance where you may start creating buckets. Your {{site.data.keyword.cos_short}} instances will be listed under **Global Services** in [the console dashboard](/dashboard/apps).

The terms 'resource instance' and 'service instance' refer to the same concept, and may be used interchangeably.
{:tip}

  2. Follow the **Create bucket** button and choose a unique name; all buckets in all regions across the globe share a single namespace. Ensure that you have the [correct permissions](/docs/services/cloud-object-storage/iam/buckets.html) to create a bucket.
  3. Choose a desired [level of _resiliency_](/docs/services/cloud-object-storage/basics/endpoints.html) first, and then a _location_ where you would like your data to be physically stored. Resiliency refers to the scope and scale of the geographic area across which your data is distributed. _Cross Region_ resiliency will spread your data across several metropolitan areas, while _Regional_ resiliency will spread data across a single metropolitan area.
  4. Choose the [bucket's _storage class_](/docs/services/cloud-object-storage/basics/classes.html). This is a reflection of how often you expect to read the stored data and determines billing details. Follow the **Create** link to create and access your new bucket.

  Buckets are a way to organize your data, but they're not the sole way. Object names (often referred to as _object keys_) can also contain one or more forward slashes allowing for a directory-like organizational system. You can use the portion of the object name before a delimiter to form an _object prefix_, which can be used to list related objects in a single bucket through the API.
{:tip}


## Step 2: Add some objects to your buckets
{: #add-objects}

Now go ahead and navigate to one of your buckets by selecting it from the list.  Click the **Add Objects** button. Note that new objects overwrite existing objects with identical names within the same bucket. When using the console to upload objects, the object name always matches the file name, but there doesn't need to be any relationship between the file name and the object key if you are using the API to write data.  Go ahead and add a handful of files to this bucket.

Objects can't exceed 200MB in size when uploaded using the console, but objects [split into multiple parts and uploaded in parallel using the API](/docs/services/cloud-object-storage/basics/multipart.html) can be as large as 10TB.  Object keys can be up to 1024 characters in length, and it's best to avoid any characters that might be problematic in a web address (e.g. `?`, `=`, `<`, etc.).
{:tip}

## Step 3a: Invite a user to your account to administer your buckets and data
{: #invite-user}

Now you're going to bring in another user and allow them to act as an administrator for the instance and any data stored in it.

  1. First, to add the new user you need to leave the current {{site.data.keyword.cos_short}} interface and head for the IAM console by navigating to the **Manage** menu and following the link at **Account** > **Users**.  Click the **Invite users** button.
  2. Enter the email address of a user you'd like to invite to your organization, then expand the **Identity and Access enabled services** section and select "Cloud Object Storage" from the **Services** drop-down menu.
  3. Now two more fields will appear: _Service instance_ and _Roles_. The first field defines which instance of {{site.data.keyword.cos_short}} the user will be able to access and the second determines what set of actions the user is able to perform. Select "Administrator" to allow the user grant other [users and service IDs](/docs/services/cloud-object-storage/iam/users-serviceids.html) access to the instance. Now create another policy to grant the user "Manager".  Now the user can manage the instance as well as create and delete buckets and objects. These combinations of a _Subject_ (user), _Role_ (Manager), and _Resource_ ({{site.data.keyword.cos_short}} service instance) together form [IAM policies](/docs/services/cloud-object-storage/iam/overview.html#getting-started-with-iam). For more detailed guidance on roles and policies, [see the IAM documentation](/docs/iam/users_roles.html).
  4. {{site.data.keyword.cloud_notm}} uses Cloud Foundry as the underlying account management platform, so it's necessary to grant a minimal level of Cloud Foundry access in order for the user to access your organization in the first place.  Select the desired organization from the **Organization** drop-down menu, and then select "Auditor" from both the **Organizational roles** and **Space roles** drop down menus.  This will allow the user to view services available to your organization, but not change them.

## Step 3b: Give developers access to a bucket.
{: #bucket-policy}

  1. Navigate to the **Manage** menu and follow the link at **Account** > **Service IDs**.  Here you can create a _service ID_ which serves as a abstracted identity bound to the account.   Service IDs can be assigned API keys and are used in situations where you don't want to tie a particular developer's identity to a process or component of an application.

## Next steps

Now that you are familiar with managing and using your object storage via the web-based console, you might be interested in doing a similar workflow from the command line using  the `bx` command line utility for creating the service instance and interacting with IAM, and `curl` for accessing COS directly. [Check out the API overview](/docs/services/cloud-object-storage/api-reference/about-compatibility-api.html) to get started.
