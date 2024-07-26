---

copyright:
  years: 2019, 2024
lastupdated: "2024-07-26"

keywords: object storage, tutorial, IAM, bucket, service role, policy

subcollection: cloud-object-storage

content-type: tutorial
account-plan: lite
completion-time: 15m

---

{{site.data.keyword.attribute-definition-list}}

# Limiting access to a single {{site.data.keyword.cos_short}} bucket using the UI
{: #limit-access}
{: toc-content-type="tutorial"}
{: toc-completion-time="15m"}

IBM Cloud IAM resource groups and access policies allow administrators to restrict user access to various service instances. But what if you are using the IBM Cloud user interface and only need to access a limited number of buckets within a service instance? This can be accomplished using a custom role and a narrowly-tailored IAM policy.
{: shortdesc}


This tutorial provides an introduction to granting access to a single {{site.data.keyword.cos_short}} bucket for a user who needs to use the IBM Cloud UI to access the bucket.

If you are not familiar with {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage). Also, if you are not familiar with IAM, you may wish to check out how to [get started with IAM](/docs/account?topic=account-iamoverview#iamoverview).

## Before you begin
{: #single-bucket-cos-prereqs}

If you are already managing instances of {{site.data.keyword.cos_short}} or IAM, you do not need to create more. However, as this tutorial will modify and configure the instance you are working with, make sure that any accounts or services are not being used in a production environment.

This tutorial will create a new access policy and a new custom role in the process.

For this tutorial, you need:
- An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com){: external}
- An [instance of IBM Cloud Object Storage](/objectstorage/create). If you do not have a COS instance, you can create one. For purposes of this tutorial, name the instance `COS-BUCKET-LIMIT-EX`.
- A bucket to which a user should be constrained. If you have created a new instance for the tutorial, create several buckets after you have created the COS instance.  The tutorial will describe how to provide access to only one of the buckets in the COS service instance.
- To complete the steps to manage access to the service, your user ID needs **administrator platform permissions** to configure the IAM policy. You may have to contact or work with an account administrator.

## Provide bucket-level access to the individual users
{: #bucket-level-access-individual}

1. [Create a custom COS Service role](https://cloud.ibm.com/docs/account?topic=account-custom-roles&interface=ui) (call it `COS ListBucketsInAccount`, for example) and assign the action `cloud-object-storage.account.get_account_buckets` to this custom role.
1. [Create an IAM Access Group](/docs/account?topic=account-groups&interface=ui) and call it `BUCKET_ACCESS_GROUP_1`, for example.
1. In that new access group, create an instance-level access policy for the instance `COS-BUCKET-LIMIT-EX`, and assign the platform role `Viewer` and the custom role you just created, `COS ListBucketsInAccount`.
1. In the same access group, create a bucket-level access policy for one of the buckets in the instance and assign the COS service roles `Content Reader` and `Object Writer`.

   What levels of access you want here are going to determine the roles you specify. This example is for a minimal object list, and upload and download in one bucket. For more information see: [Assigning access to an individual bucket](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions)
{: note}

1. [Invite a user to the account](/docs/account?topic=account-iamuserinv&interface=ui).
1. Once the user has accepted the invitation to the account, add the user to the access group `BUCKET_ACCESS_GROUP_1`.
1. Now when the user logs in, if they are already members of other {{site.data.keyword.cloud}} accounts, ensure that they select the correct {{site.data.keyword.cloud}} account in the account selector in the console header.
1. Once the user has selected the correct account in the account selector, the user sees only one COS instance in the console resource view, `COS-BUCKET-LIMIT-EX`.
1. Select the instance.

Both buckets are listed, but users will only be able to list objects in the bucket they are given access to and, depending on the accesses you provided, download objects from that bucket and upload objects to that bucket.
{: note}

The user can appear to select the other bucket but they cannot list objects, cannot download objects, and cannot upload to that other bucket, and so on.
{: note}



## Next steps
{: #single-bucket-next-steps}

Congratulations, you've just set up a policy to limit user access to a single bucket when they must use the IBM Cloud user interface for their access.
