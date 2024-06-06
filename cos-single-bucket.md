---

copyright:
  years: 2019, 2024
lastupdated: "2024-06-06"

keywords: object storage, tutorial, IAM, 

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
- An [instance of IBM Cloud Object Storage](/objectstorage/create)
- A bucket to which a user should be constrained
- To complete the steps to manage access to the service, your user ID needs **administrator platform permissions** to use the IAM service. You may have to contact or work with an account administrator.

## Provide bucket-level access to the individual users
{: #bucket-level-access-individual}

1. Create a custom COS Service role (call it `COS ListBucketsInAccount` as an example) and assign the action `cloud-object-storage.account.get_account_buckets` to this custom role.
1. Create an IAM Access Group, call it `BUCKET_ACCESS_GROUP_1`, for example.
1. In that new access group, create an instance-level access policy for instance `COS-BUCKET-LIMIT-EX`, and assign the platform role Viewer and the custom role you just created, `COS ListBucketsInAccount`.
1. In the same access group, create a bucket-level access policy for the bucket named `group1-users-can-access` and assign the COS service roles, `Content Reader` and `Object Writer`.

What levels of access you want here are going to determine the roles you specify. This example is for a minimal object list, and upload and download in one bucket. For more information see: [Assigning access to an individual bucket](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions)
{: note}

1. Invite a user to the account.
1. Once the user has accepted the invitation to the account, add the user to the access group `BUCKET_ACCESS_GROUP_1`.
1. Now when the user logs in, if they are already members of other {{site.data.keyword.cloud}} accounts, ensure that they select the correct {{site.data.keyword.cloud}} account in the account selector in the console header.
1. Once the user has selected the correct account in the account selector, the user sees only one COS instance in the console resource view, `COS-BUCKET-LIMIT-EX`.
1. Select the instance.

Both buckets are listed, but users will only be able to list objects in the bucket they are given access to (`group1-users-can-access`, in this case) and download objects from that bucket and upload objects to that bucket.
{: note}

The user can appear to select the other bucket but they cannot list objects, cannot download objects, and cannot upload to that other bucket, and so on.
{: note}

<!-- Expanded with the above procedure and eliminated obsolete screen captures. See issue #1068 - 06062024 PW
## Create a custom role
{: #single-bucket-create-role}
{: step}

First, we need to create a role that allows a user to view a list of buckets, but not to access them or be able to create new buckets.

1. Navigate to IAM by following the **Manage** drop-down menu, and selecting **Access (IAM)**.
2. Select **Roles** from the navigation menu.
3. Click the **Create** button to create a new role.

  ![Create a new role](images/tut-iam-1-roles.png){: caption="Figure 1: Creating a custom role."}

4. We can call this role "List Buckets Only".  Give it a name, ID, and brief description, and then select **Cloud Object Storage** from the drop down.

  ![Create a new role](images/tut-iam-2-custom.png)

5. Scroll down until you see the list of actions.  Click **Clear all** to remove all actions from the new role.

   ![Create a new role](images/tut-iam-3-clear.png)

6. Look for the `cloud-object-storage.account.get_account_buckets` action and click **Add**.

  ![Create a new role](images/tut-iam-4-list.png)

7. Click **Create** to finish creating the custom role.

## Create a new user access policy
{: #single-bucket-create-policy}
{: step}

Now that we have our new role, we can apply it to a user.

1. Follow the **Users** link in the navigation menu, and select the user requiring limited access.
2. Click on the **Assign access** button.

  ![Create a new policy](images/tut-iam-5-user.png)

3. Select the **Access policy** tile and select **Cloud Object Storage**.

  ![Create a new policy](images/tut-iam-6-policy.png)

4. Scroll down and assign the new role by checking the box next to **List Buckets Only**.
5. Click **Add**.

  ![Create a new policy](images/tut-iam-7-list-only.png)

6. Repeat step 3, but this time we'll limit the scope.  Select the radio toggle next to **Specific resources**.
7. Select **Resource ID** from the _Attribute type_ drop-down menu.
8. Type in the name of the bucket that the user should be able to access in the _Value_ field.  In this case, it's a bucket called `diagnostics`. 

  ![Create a new policy](images/tut-iam-8-bucket.png)

9.  In the _Roles and access_ section, select the roles **Content Reader** and **Object Writer** roles.  You'll also need the Platform **Viewer** role, if you don't already have it, in order to view the UI.

  ![Create a new policy](images/tut-iam-9-access.png)-->

## Next steps
{: #single-bucket-next-steps}

Congratulations, you've just set up a policy to limit access to a single bucket. 
