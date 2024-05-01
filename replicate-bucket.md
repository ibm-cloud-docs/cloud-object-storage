---

copyright:
   years: 2022, 2024
lastupdated: "2024-04-18"

keywords: tutorials

subcollection: cloud-object-storage

services:
account-plan: lite

---

{{site.data.keyword.attribute-definition-list}}

# Replicating data across buckets
{: #replicating-cos-bucket}
{: toc-content-type="tutorial"}
{: toc-completion-time="10m"}

While all data stored in Cloud Object Storage is highly durable and highly available, it can be necessary for compliance or other reasons to replicate and sync data across multiple buckets.
{: shortdesc}

## Before you begin
{: #replicate-bucket-prereqs}

Before you get started, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](/objectstorage/create)

You will also need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted.

## Create a bucket
{: #replicate-bucket-create}
{: step}

First, create the source bucket:

1. Navigate to an instance of Object Storage.
1. Click **Create bucket**.
![Navigate to COS](images/rep-tut-1.png)
1. Select **Custom bucket**.
![Navigate to COS](images/rep-tut-2.png)
1. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
![Navigate to COS](images/rep-tut-3.png)

You can choose to use Key Protect to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use Key Protect.
{: important}

## Create a destination bucket
{: #replicate-bucket-create-2}
{: step}

## Configure replication on the source bucket
{: #replicate-bucket-select}
{: step}

## Verify that it works
{: #replicate-bucket-verify}
{: step}
