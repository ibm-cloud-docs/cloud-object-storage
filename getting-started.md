---

copyright:
  years: 2014, 2017
lastupdated: "2017-02-23"

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Getting started with IBM Cloud Object Storage
In this getting started tutorial, we'll take you through the steps needed to create buckets, upload objects, and set up access policies to allow other users to work with your data.
{: shortdesc}

This documentation refers to IBM Cloud Object Storage provisioned as an IBM Cloud Platform _service_ using the Bluemix console. This service is integrated with IBM Cloud Identity and Access Management and is slightly different from the IBM COS service previously available from Bluemix Infrastructure (SoftLayer).  Documentation for other object storage offerings as well as more information on the evolution of object storage in the IBM cloud can be found [here](/about-cos.html).
{:tip}

## Before you begin
You'll need a [Bluemix account](https://console.bluemix.net/registration/), an instance of the Cloud Object Storage service, a second email address to invite to your account, and some files on your local computer to upload.
{: #prereqs}

## Step 1: Create some buckets to store your data
{: #create-buckets}

When you went to the catalog and ordered Cloud Object Storage, you created a _resource instance_. After creation you will be automatically redirected to that resource instance where you'll find a list of buckets.  Follow the **Create Bucket** link and choose a unique name; all buckets in all regions across the globe share the same namespace.  Choose a desired level of _resiliency_ first, and then a _region_ where you would like your data to be physically stored. Resiliency refers to the scope and scale of the geographic area across which we distribute your data. All data stored in COS buckets is automatically encrypted, sliced into fragments, and dispersed across at least three datacenters.  _Cross Regional_ resiliency will spread you data across hundreds or even thousands of miles, while _Regional_ resiliency will spread data across a metropolitan area.  A bucket's _storage class_ is a reflection of how often you expect to read the stored data and determines billing details. Follow the **Create** link to access your new bucket.

Buckets are a way to organize your data, but they're not the sole way.  Object names (often referred to as _object keys_ can also contain forward slashes, allowing for a directory-like organizational system. These _object prefixes_ provide a mechanism for getting lists of related objects using the API.
{:tip}

Now, it might be tempting to start filling this bucket up with objects, but first create another bucket or two.  Choose different parameters this time and then head back to the list of buckets either by following the **Buckets and Objects** link in the navigation pane, or by clicking on the name you gave the service instance in the breadcrumb trail. Note the different shapes and colors indicating the combinations of locations and resiliencies that make up your object storage portfolio.

## Step 2: Add some objects to your buckets
{: #add-objects}

Now go ahead and navigate to one of your buckets by selecting it from the list.  Follow the **Add Objects** link. Note that new objects overwrite existing objects with identical names within the same bucket, so be careful if you are uploading a file with the same name as an existing object.  When you use the UI like this to upload objects the object's key is identical to the filename, but there doesn't need to be any relationship between the filename and the object key if you are using the API to write data.  Go ahead and add a handful of files to this bucket.

Objects can't exceed 200MB in size when using the portal, but objects uploaded using the API can be as large as 10TB. All object keys need to be no more than 1024 characters in length, and it's best to avoid any characters that might be problematic in a web address (e.g. `?`, `=`, `<`, etc.) There is no practical limit on the amount of storage you can use in a single storage instance, or a single bucket for that matter.  Each bucket can hold billions of objects.
{:tip}

Now add some objects to another bucket, so that you have objects in two different buckets.  One might be a _Standard_ storage class bucket that acts as an active content repository for a cloud application.  Another might be a _Cold Vault_ bucket intended for long term archival storage.

## Step 3: Invite another user to your account
{: #invite-user}

Now we're going to bring in another user and allow them to read data from your buckets, but then permit them to create and delete objects as well in one of your buckets.  First, to add the new user we need to leave the current COS interface and head for the IAM console by navigating to the **Manage** menu and following the link at **Account** > **Users**.  Enter an email address you'd like to invite to your organization, then expand the **Identity and Access enabled services** section and select "Cloud Object Storage" from the **Services** drop-down menu.  Now two more fields will appear: _Service instance_ and _Roles_. The first field defines which instance of COS the user will be able to access, and the second determines what set of actions the user is able to perform. For now, select "Viewer" which will allow the user to read data from your buckets, but not create or delete objects. This combination of a _Subject_ (user), _Role_ (viewer), and _Resource_ (COS service instance) together form an IAM policy. For more detailed guidance on roles and policies, [see the IAM documentation](https://console.stage1.bluemix.net/docs/developing/Access-Management/index.html).

Next it's necessary to grant a level of Cloud Foundry access in order for the user to access your organization in the first place.  Select the desired organization from the **Organization** drop-down menu, and then select "Auditor" from both the **Organizational roles** and **Space roles** drop down menus.  This will allow the user to view services available to your organization, but not change them.

## Step 4: Create a policy for a specific bucket
{: #bucket-policy}

Check that the new user can view the buckets and objects created in steps 1 & 2. Navigate to the bucket that you want to allow the user to add objects to, and select **Bucket permissions** from the navigation menu.  Choose the user from the **Select user** drop-down menu, and choose the "Editor" role from the next menu.  Follow the **Submit user policy** link. Now the new user can add and delete objects from this bucket, but are still limited to downloading objects stored in all other buckets.

## Next steps

Now that you are familiar with managing and using your object storage via the web-based console, you might be interested in doing a similar workflow from the command line using  the `bx` command line utility for creating the service instance and interacting with IAM, and `curl` for accessing COS directly. [Check out the command line tutorial](tutorials/gather-required-information.html) to get started.
