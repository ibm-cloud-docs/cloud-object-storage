---

copyright:
  years: 2019, 2022
lastupdated: "2022-10-10"

keywords: object storage, tutorial

subcollection: cloud-object-storage

content-type: tutorial
account-plan: lite
completion-time: 15m

---

{{site.data.keyword.attribute-definition-list}}

# Limiting access to a single {{site.data.keyword.cos_short}} bucket
{: #limit-access}
{: toc-content-type="tutorial"} 
{: toc-completion-time="15m"} 

IBM Cloud IAM resource groups and access groups allow administrators to restrict users access to various service instances, but what if a user needs to only access a limited number of buckets within a service instance? This can be accomplished using a custom role and a narrowly tailored IAM policy.
{: shortdesc}

This tutorial provides an introduction to granting access to a single {{site.data.keyword.cos_short}} bucket. 

If you're not familiar with {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage). Also, if you're not familiar with IAM, you may wish to check out how to [get started with IAM](/docs/account?topic=account-iamoverview#iamoverview).

## Before you begin
{: #single-bucket-cos-prereqs}

If you are already managing instances of {{site.data.keyword.cos_short}} or IAM, you do not need to create more. However, as this tutorial will modify and configure the instance we are working with, make sure that any accounts or services are not being used in a production environment. 

This tutorial will create a new access policy and a new custom role in the process. 

For this tutorial, you need:
- An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com){: external}
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)
- A bucket to which a user should be constrained
- To complete the steps to manage access to the service, your user ID needs **administrator platform permissions** to use the IAM service. You may have to contact or work with an account administrator. 

## Create a custom role 
{: #single-bucket-create-role}
{: step}

First, we need to create a role that allows a user to view a list of buckets, but not to access them or be able to create new buckets.

1. Navigate to IAM by following the **Manage** drop-down menu, and selecting **Access (IAM)**.
2. Select **Roles** from the navigation menu.
3. Click the **Create** button to create a new role.

![Create a new role](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-1-roles.png)

4. We can call this role "List Buckets Only".  Give it a name, ID, and brief description, and then select **Cloud Object Storage** from the drop down.

![Create a new role](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-2-custom.png)

5. Scroll down until you see the list of actions.  Click **Clear all** to remove all actions from the new role.
   
![Create a new role](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-3-clear.png)

6. Look for the `cloud-object-storage.account.get_account_buckets` action and click **Add**.

![Create a new role](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-4-list.png)

7. Click **Create** to finish creating the custom role.

## Create a new user access policy 
{: #single-bucket-create-policy}
{: step}

Now that we have our new role, we can apply it to a user.

1. Follow the **Users** link in the navigation menu, and select the user requiring limited access.
2. Click on the **Assign access** button.

![Create a new policy](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-5-user.png)

3. Select the **Access policy** tile and select **Cloud Object Storage**.

![Create a new policy](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-6-policy.png)

4. Scroll down and assign the new role by checking the box next to **List Buckets Only**.
5. Click **Add**.

![Create a new policy](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-7-list-only.png)

6. Repeat step 3, but this time we'll limit the scope.  Select the radio toggle next to **Specific resources**.
7. Select **Resource ID** from the _Attribute type_ drop-down menu.
8. Type in the name of the bucket that the user should be able to access in the _Value_ field.  In this case, it's a bucket called `diagnostics`. 

![Create a new policy](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-8-bucket.png)

9.  In the _Roles and access_ section, select the roles **Content Reader** and **Object Writer** roles.  You'll also need the Platform **Viewer** role, if you don't already have it, in order to view the UI.

![Create a new policy](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/tut-iam-9-access.png)

## Next steps
{: #single-bucket-next-steps}

Congratulations, you've just set up a policy to limit access to a single user. 
