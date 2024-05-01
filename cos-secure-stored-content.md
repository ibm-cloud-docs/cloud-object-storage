---

copyright:
  years: 2023, 2024
lastupdated: "2024-04-17"

keywords: object storage, tutorial, secure stored content, store, content, secure, secure content store

subcollection: cloud-object-storage

content-type: tutorial
account-plan: standard
completion-time: 15m

---

{{site.data.keyword.attribute-definition-list}}

# Create a Secure Content Store
{: #secure-content-store}
{: toc-content-type="tutorial"}
{: toc-completion-time="15m"}

Are you looking to store content securely (locally or globally) at an affordable cost​, for things like **cloud native apps**, **media storage**, **backup storage** and **archive data**? IBM Secure Content Store powered by {{site.data.keyword.cos_full}} provides unparalleled agility in supporting fast, highly consistent application deployment around the world to help customers securely expand their business into new regions, from business-critical data to video archive solutions.  It also offers immutable storage, immutable backup, and archive data with industry-leading security and controls for regulatory/compliance requirements​.

- Gain security and control over your data with encryption options, governance policy, access permissions, and context-based restrictions​.
- Have immediate consistency across regions or locations for cloud-native apps, disaster recovery, storage backup, video content and delivery, and so on. ​
- Leverage your own encryption keys (BYOK) with Key Protect.
- Monitor and retain your account & data activity with Activity Tracker and IBM Monitoring.
- APIs & SDKs, Static Web Hosting, High Speed Transfer, Tagging, Replication.

## *Promotion for new customers!*
{: #promotion}

IBM Cloud is offering a $500 promotional credit to quickly get started with our Secure Content Store with {{site.data.keyword.cos_short}}. The credit has a duration of 90 days against your metered consumption of {{site.data.keyword.cos_short}}. See instructions below for how to apply your promo code. To qualify for this offer you must be a new paid user of {{site.data.keyword.cos_short}}. There is a limit of one promotion code per customer account. *The USD 500 credit is for use with this offer only and cannot to be applied to other offers. Offer is subject to availability.*
{: important}

## Overview
{: #overview-secure-stored-content}

This tutorial is for customers looking to set up a Secure Content Store using {{site.data.keyword.cos_short}}, Activity Tracker, and Key Protect. In this tutorial, you are guided through the process of quickly getting started with these essential services to ensure the security and integrity of your content. Secure Content Store is comprised of the following services:

- **{{site.data.keyword.cos_short}}**: a scalable and flexible storage solution that allows you to store and manage your data securely.
- **Activity Tracker**: a powerful tool that provides comprehensive visibility into the activities happening within your IBM Cloud environment and allows for ease of audit observability.
- **Monitoring**: to provide insights and information about what is happening with your data in your Secure Content Store.
- **Key Protect**: a Key Management Service that enables you to manage and protect your encryption keys in a secure and centralized manner.

Throughout the tutorial, you are provided with step-by-step instructions, along with helpful tips and best practices, which can help you set up a Secure Content Store more efficiently.  So, let's get started!

### High level steps for the tutorial
{: #high-level-steps}
{: steps}

1. Set up {{site.data.keyword.cos_short}} to store and manage your data securely.
1. Configure Activity Tracker for audit observability of relevant events.
1. Add Monitoring for insights and information about what is happening with your data.
1. Finally, use Key Protect to manage encryption keys to secure your data stored in {{site.data.keyword.cos_short}}.

## Before you begin
{: #secure-content-prereqs}

For this tutorial, you need:
- An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com){: external}
- An [instance of IBM Cloud Object Storage](/objectstorage/create) (must be a paid service plan instance)

Apply promotional code that is included below.

IBM Cloud is offering a $500 promotional credit to quickly get started with our Secure Content Store with {{site.data.keyword.cos_full}}. The credit has a duration of 90 days against your metered consumption of {{site.data.keyword.cos_short}}. To qualify for this offer you must be a new paid user of Cloud Object Storage.
{: remember}

1. Create or log into an IBM Cloud Paygo, Subscription account.
1. Within IBM Cloud console click **Manage** then **Billing & Usage** from the drop-down list.
1. Select **Promotions and Credits** from the navigation bar on the left.
1. Click **Apply a promo code**.
1. Enter **Promo Code SECURECS**, click **verify** and then **apply**.

Once the credit has been successfully applied, you can review your credit balance at any time by following steps 2-3.
{: note}

## Create a new {{site.data.keyword.cos_short}} bucket
{: #create-cos-bucket}

## Navigate to your instance of {{site.data.keyword.cos_short}}
{: #navigate-cos-instance}
{: step}

- Go to your [instance of IBM Cloud Object Storage](/objectstorage/create).

## Click Create bucket
{: #create-cos-bucket-step}
{: step}

- Select the **Customize your bucket** tile, and click the right arrow.
    1. Name the new bucket.  It must start and end in alphanumeric characters (from 3 to 63) that is limited to using lowercase, numbers and nonconsecutive dots, and hyphens.
    2. Choose your desired region and [storage class](/docs/cloud-object-storage?topic=cloud-object-storage-classes#classes), based on your activity (for example, chose “Standard” storage class for hot data, “Vault” or “Cold Vault” for cold data, or “Smart Tier” for blended or variable data activity.)

- Add the following services during the bucket creation by scrolling down to **Service integrations (optional)**.

    [Key Protect](/docs/cloud-object-storage?topic=cloud-object-storage-tutorial-kp-encrypt-bucket)

    Before you get started, you need:

      - An instance of [IBM Cloud™ Key Protect](/docs/key-protect?topic=key-protect-getting-started-tutorial)
      - [Grant service authorization](/docs/cloud-object-storage?topic=cloud-object-storage-kp#kp-sa) to {{site.data.keyword.cos_short}} in IBM Key Protect.

     1. Toggle **Key management disabled** to enable encryption and **click** on **Create new instance**.
     1. Choose a region that corresponds with the bucket, give it a memorable name, and click **Create and continue**.
     1. Give the `root key` a name and click **Create and continue**.

    [Activity Tracker](/docs/cloud-object-storage?topic=cloud-object-storage-tracking-cos-events)

    Before you get started, you need:

      - An instance of [Activity Tracker](/docs/activity-tracker?topic=activity-tracker-getting-started)
      - A user ID with [administrator platform permissions](/docs/account?topic=account-userroles) and the service access [writer role](/docs/account?topic=account-userroles#service_access_roles).

     1. Scroll down to the **Monitoring and activity tracking** section and toggle the radio button to **Activity tracking enabled**. Select an appropriate plan, and give the new instance a memorable name. As you may likely want to create the Activity Tracker instance in the same region as the bucket (for example, `us-east`) you could name the instance something like `US East AT` so that you can easily find it later.
     1. Click to enable **Track data events** and select both **read & write** from the drop-down list.

    [Monitoring](/docs/cloud-object-storage?topic=cloud-object-storage-monitoring-cos)

    Before you start, you need:

      - An instance of [IBM Cloud™ Monitoring](/docs/monitoring?topic=monitoring-getting-started)
      - A user ID with [administrator platform permissions](/docs/account?topic=account-userroles) and the service access [writer role](/docs/account?topic=account-userroles#service_access_roles).

     1. Scroll down to the **Monitoring and activity tracking** section and toggle the radio button to **Monitoring enabled**. Select an appropriate plan, and give the new instance a memorable name. For example, if you are creating the instance in the same region as the bucket (for example, `us-east`) you could name the instance `US East MM` so that you can easily find it later.
     1. Enable monitoring for both **usage and request metrics**.

## Verify the information is correct
{: #verify-cos-bucket}
{: step}

## Click Create bucket to add the new bucket to your instance of {{site.data.keyword.cos_short}}
{: #click-cos-bucket}
{: step}

After your bucket is created with Activity Tracker and Monitoring, it may take a few minutes for the rules to take effect.
{: attention}

You are now ready to store data in a secure content store with encryption, monitoring, and audit observability!

## Get started by uploading data
{: #upload-data}

- See [uploading data](/docs/cloud-object-storage?topic=cloud-object-storage-upload) for more information.

## Add capabilities
{: #add-capabilities}

Add capabilities to protect objects from ransom-ware and accidental deletion such as [versioning](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) and [immutable retention polices](/docs/cloud-object-storage?topic=cloud-object-storage-ol-overview) for supporting immutable storage, and immutable backup and archive data.

## Library of {{site.data.keyword.cos_short}} tutorials
{: #cos-tutorials}

Check out the IBM Cloud Tutorials library for more tutorials when deploying solutions with [Cloud Object Storage](https://cloud.ibm.com/docs?tab=tutorials&page=1&pageSize=20&tags=cloud-object-storage).

