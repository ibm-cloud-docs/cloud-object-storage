---

copyright:
   years: 2022, 2025
lastupdated: "2025-02-18"

keywords: tutorials, key protect, bucket, encryption

subcollection: cloud-object-storage

content-type: tutorial
services:
account-plan: lite
completion-time: 10m

---

{{site.data.keyword.attribute-definition-list}}

# Encrypting a bucket with Key Protect
{: #tutorial-kp-encrypt-bucket}
{: toc-content-type="tutorial"}
{: toc-completion-time="10m"}

While all data stored in Cloud Object Storage is automatically encrypted using randomly generated keys, some workloads require that the keys can be rotated, deleted, or otherwise controlled by a key management system (KMS) like Key Protect.
{: shortdesc}

## Before you begin
{: #kp-encrypt-bucket-prereqs}

Before you plan on using Key Protect with Cloud Object Storage buckets, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](/objectstorage/create)
- An [instance of Key Protect](key-protect/key-protect-about)

As of 1 January 2025, five key versions per account are no longer free. You are charged for each key version, starting with the first created key.
{: important}

You will also need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. This tutorial does not outline the step-by-step instructions to help you get started.  This information is found in section  [Server-Side Encryption with IBM Key Protect (SSE-KP)](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-kp)

## Create a new encryption key
{: #kp-create-encryption-key}
{: step}

1. Using the **Navigation Menu**, go to **Resource List** and expand **Security**.
1. Click a **Key Protect** instance.
1. Click the **Add** button.
1. Click the **Root key** tab.
1. Enter a Key name.
1. Click **Advanced Option** and enter a Key description.
1. Click the **Add key** button. Your new encryption key is listed in the **Keys** table.

## Create a new bucket and associate the key with it
{: #kp-encrypt-bucket-create}
{: step}

1. Using the **Navigation Menu**, go to **Resource List** and expand **Storage**.
1. Click your **Storage** instance.
1. Click **Create bucket**.
1. Click **Create** in the **Create a Custom Bucket** pane.
1. Enter a unique bucket name.
1. Select **Resiliency>Regional**.
1. Select a **Location**.
1. Select a **Storage Class**.
1. Enable **Service integrations>Encryption>Key management**.
1. Click **Key Protect>Use existing instance**.
1. Select the **Search by instance** tab in the **Key Protect integration** side panel.
1. Select a Key Protect instance from the menu.
1. Select the **Key name** that you just created.
1. Click the **Associate key** button.
1. Click the **Create bucket** button. A popup message displays that a bucket was created successfully.
1. Confirm by clicking the **Configuration** tab.
1. Click **Jump to>Key management** (or scroll down the page).
1. In the **Associated key management services** box see **Service instance** and the **Key** that was associated with the bucket.
