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
In this getting started tutorial, we'll take you the steps needed to create buckets, upload objects, and set up access policies to allow other users to work with your data."
{: shortdesc}

## Before you begin
{: #prereqs}
You'll need a [Bluemix account](https://console.ng.bluemix.net/registration/), an instance of the Cloud Object Storage service, a second email address to invite to your account, and some files on your local computer to upload."


## Step 1: Create some buckets to store your data
{: #create-buckets}

When you went to the catalog and ordered Cloud Object Storage, you created what is called a _service instance_. If you haven't already, navigate to that service instance where you'll find an list of buckets.  Follow the **Create Bucket** link and choose a unique name; all buckets in all regions across the globe share the same namespace.  Choose a desired level of _resiliency_ first, and then a _region_ where you would like your data to physically reside. Resiliency refers to the scope of the geographic area across which your data will be distributed. All data stored in COS buckets is automatically encrypted, sliced into tiny fragments, and dispersed across multiple datacenters.  _Cross Regional_ resiliency will spread you data across hundreds or even thousands of miles, while _Regional_ resiliency will spread data across a metropolitan area.  A bucket's _storage class_ is a reflection of how often you expect to read the stored data and determines how usage is billed. Follow the **Create** link to access your new bucket.

Buckets are a way to organize your data, but they're not the only way.  Object names (often referred to as _object keys_ can also contain forward slashes, allowing for a directory-like organizational system. These are called _object prefixes_ and provide a mechanism for getting lists of related objects using the API.
{:tip}

Now, it might be tempting to just start filling this bucket up with objects, but first create another bucket or two.  Choose different parameters this time and then head back to the list of buckets either by following the **Buckets and Objects** link in the navigation pane, or by clicking on the name you gave the service instance in the breadcrumb trail. Note that the shapes and colors are different in the list of buckets to clearly indicate the various combinations of locations and resiliencies that make up your object storage portfolio.

## Step 2: Add some objects to your buckets
{: #add-objects}

Now go ahead and navigate to one of your buckets by selecting it from the list.  Follow the **Add Objects** link. Note that new objects overwrite existing objects with identical names within the same bucket, so be careful if you are uploading a file with the same name as an existing object.  When you use the UI like this to upload objects the object's key is set to the filename, but there doesn't need to be any relationship between the filename and the object key if you are using the API to write data.  Go ahead and add a few files to this bucket.

Each object is limited to 200MB in size when being uploaded using the portal.  Objects uploaded using the API are limited to 10TB.  There is no practical limit on the amount of storage you can use in a single storage instance, or a single bucket for that matter.  Each bucket can hold billions of objects.
{:tip}

Go ahead and add some objects to another bucket, so that now you have objects in two different buckets.  Maybe one is set up with a _Standard_ storage class, like a content repository for a cloud application.  Another might be a _Cold Vault_ bucket, intended for long term archival storage.

Next up, we're going to bring in another user and allow them to read data from all of your buckets, but they'll only be able to create and delete objects within one specified bucket.

## Step 3: Invite another user to your account
{: #invite-user}


## Step 4: Create a policy for a specific bucket
{: #bucket-policy}


## Next steps
