---

copyright:
  years: 2023
lastupdated: "2023-06-16"

keywords: object storage, tutorial, secure stored content, store, content, secure

subcollection: cloud-object-storage

content-type: tutorial
account-plan: lite
completion-time: 15m

---

{{site.data.keyword.attribute-definition-list}}

# Store content securely with {{site.data.keyword.cos_short}}
{: #secure-content-store}
{: toc-content-type="tutorial"}
{: toc-completion-time="15m"}

Are you looking to store content securely (locally or globally) at an affordable cost​, for things like **cloud native apps**, **media storage**, **backup storage** and **archive data**? IBM Secure Content Store with {{site.data.keyword.cos_full}} provides agility in supporting fast, highly consistent deployment across locations for business-critical data, immutable storage, immutable backup, and archive data with industry-leading security and controls for regulatory/compliance requirements​.

- Gain security and control over your data with encryption options, governance policies, access permissions, and context-based restrictions​.
- Have immediate consistency across regions or locations for cloud-native apps, disaster recovery, storage backup, video content and delivery. etc. ​
- Leverage your own encryption keys (BYOK) with Key Protect
- Monitor and retain your account & data activity with Activity Tracker and IBM Monitoring
- APIs & SDKs, Static Web Hosting, High Speed Transfer, Tagging, Replication

## Promotion for new customers!
{: #promotion}

IBM Cloud is offering a $500 promotional credit to quickly get started with our Secure Content Store with {{site.data.keyword.cos_short}}. The credit has a duration of 90 days against your metered consumption of {{site.data.keyword.cos_short}}. See instructions below for applying your promo code. To qualify for this offer you must be a new paid user of {{site.data.keyword.cos_short}}.
IBM Cloud IAM resource groups and access groups allow administrators to restrict users access to various service instances, but what if a user needs to only access a limited number of buckets within a service instance? This can be accomplished using a custom role and a narrowly tailored IAM policy.
{: promotion}

## Overview
{: #overview-secure-stored-content}

This tutorial is for customers looking to set up a Secure Content Store using {{site.data.keyword.cos_short}}, Activity Tracker, and Key Protect. In this tutorial, you are guided through the process of quickly getting started with these essential services to ensure the security and integrity of your content. Secure Content Store is comprised of the following services:

- **{{site.data.keyword.cos_short}}**: a scalable and flexible storage solution that allows you to store and manage your data securely.
- **Activity Tracker**: a powerful tool that provides comprehensive visibility into the activities happening within your IBM Cloud environment and allows for ease of audit observability.
- **Monitoring**: to provide insights and information about what is happening with your data in your Secure Content Store.
- **Key Protect**: a Key Management Service that enables you to manage and protect your encryption keys in a secure and centralized manner.

Throughout the tutorial, you are provided with step-by-step instructions, along with helpful tips and best practices, which help you set up a Secure Content Store efficiently. So, let's get started!

### High level steps for the tutorial
{: #high-level-steps}
{: steps}

1. Set up {{site.data.keyword.cos_short}} to store and manage your data securely.
2. Configure Activity Tracker for audit observability of relevant events.
3. Add Monitoring for insights and information about what is happening with your data.
4. Finally, use Key Protect to manage encryption keys to secure your data stored in {{site.data.keyword.cos_short}}.


If you're not familiar with {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage). Also, if you're not familiar with IAM, you may wish to check out how to [get started with IAM](/docs/account?topic=account-iamoverview#iamoverview).

## Before you begin
{: #secure-content-prereqs}

For this tutorial, you need:
- An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com){: external}
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage) (must be a paid Standard Plan instance)

Apply the applicable promotional code that is included below.

IBM Cloud is offering a $500 promotional credit to quickly get started with our Secure Content Store with IBM Cloud Object Storage. The credit has a duration of 90 days against your metered consumption of Cloud Object Storage. To qualify for this offer you must be a new paid user of Cloud Object Storage.
{: promotion}

1. Create or log into a IBM Cloud Paygo, Subscription account.
2. Within IBM Cloud console click **Manage** then **Billing & Usage** from the drop-down list.
3. Select **Promotions and Credits** from the navigation bar on the left.
4. Click **Apply a promo code**.
5. Enter **Promo Code SECUREDCS**, click **verify** and then apply.

## Create a new {{site.data.keyword.cos_short}} bucket
{: #create-cos-bucket}

## Navigate to your [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage).
{: #navigate-cos-instance}
{: step}

## Click Create bucket.
{: #create-cos-bucket}
{: step}

- Select **Customize your bucket** by clicking the right arrow.
    1. Name the new bucket.  It must start and end in alphanumeric characters (from 3 to 63) that is limited to using lowercase, numbers and nonconsecutive dots, and hyphens.
    2. Choose your desired region and [storage class](/docs/cloud-object-storage?topic=cloud-object-storage-classes#classes), based on your activity (hot, cold, variable...etc.)

- Add the following services during the bucket creation by scrolling down to **Service integrations (optional)**.

    Encryption using Key Protect
     1. Switch **Key management disabled** to **Key management enabled** and **click** on **Create new instance**.
     2. Choose a region that corresponds with the bucket, give it a memorable name, and click **Create and continue**.
     3. Give the `root key` a name and click **Create and continue**.

    Activity Tracker
      4. Scroll down to the **Monitoring and activity tracking** section and toggle the radio button to **Activity tracking enabled**. Select an appropriate plan, and give the new instance a memorable name. As you may likely want to create the Activity Tracker instance in the same region as the bucket (e.g. `us-east`) you could name the instance something like `US East AT` so that you can easily find it later.
      5. Click to enable **Track data events** and select both **read & write** from the drop-down list.

    Monitoring for Metrics
      6. Scroll down to the **Monitoring and activity tracking** section and toggle the radio button to **Monitoring enabled**. Select an appropriate plan, and give the new instance a memorable name. For example, if you are creating the instance in the same region as the bucket (e.g. `us-east`) you could name the instance `US East MM` so that you can easily find it later.
      7. Enable monitoring for both **usage and request metrics**.



## Verify the information is correct.
{: #verify-cos-bucket}
{: step}

## Click Create bucket to add the new bucket to your instance of {{site.data.keyword.cos_short}}.
{: #click-cos-bucket}
{: step}

After your bucket is created with Activity Tracker it may take a few minutes for the activity tracker rules to take effect.
{: note}

After your bucket is created with Monitoring it may take a few minutes for IBM Cloud Monitoring rules to take effect.
{: note}

You are now ready to store data in a secure content store with encryption, monitoring, and audit observability!

## Get started by [uploading data](/docs/cloud-object-storage?topic=cloud-object-storage-upload)
{: #upload-data}


## Add additional capabilities to protect from ransomware and accidental deletion such as versioning and immutable retention polices for supporting immutable storage and immutable backup and archive data.
{: #add-capabilities}

- See [Locking objects](/docs/cloud-object-storage?topic=cloud-object-storage-object-lock) for more information.
- See [Versioning objects](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) for more information.

## Check out the IBM Cloud Tutorials library for additional tutorials for deploying solutions with [Cloud Object Storage](https://cloud.ibm.com/docs?tab=tutorials&page=1&pageSize=20&tags=cloud-object-storage).
{: #cos-tutorials}

