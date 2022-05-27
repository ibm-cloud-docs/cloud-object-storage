---

copyright:
   years: 2022
lastupdated: "2022-05-22"

keywords: tutorials

subcollection: cloud-object-storage

content-type: tutorial
services: 
account-plan: lite 
completion-time: 10m 

---

{{site.data.keyword.attribute-definition-list}}

# Encrypting a bucket with Key Protect 
{: #tutorial-replicate-bucket}
{: toc-content-type="tutorial"}
{: toc-completion-time="10m"} 

While all data stored in Cloud Object Storage is highly durable and highly available, it can be necessary for compliance or other reasons to replicate and sync data across multiple buckets. 
{: shortdesc}

## Before you begin
{: #replicate-bucket-prereqs}

Before you get started, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)

You will also need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. 

## Create a bucket
{: #replicate-bucket-create}
{: step}
{: ui}

First we'll create the source bucket:

1. Navigate to your instance of Object Storage.
1. Click **Create bucket**.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-1.png)
2. Select **Custom bucket**.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-2.png)
3. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-3.png)

## Create a destination bucket
{: #replicate-bucket-create-2}
{: step}
{: ui}

First we'll create the source bucket:

1. Navigate to your instance of Object Storage.
1. Click **Create bucket**.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-1.png)
2. Select **Custom bucket**.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-2.png)
3. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-3.png)

## Configure replication on the source bucket
{: #replicate-bucket-select}
{: step}
{: ui}

1. Scroll down to **Service integrations (optional)**, toggle **Key management disabled** to enable encryption key management and click on **Create new instance**.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-4.png)
2. Choose a region that corresponds with the bucket, give it a memorable name, and click **Create and continue**.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-5.png)
3. Give the root key a name and click **Create and continue**.
![Navigate to COS](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/kp-tut-6.png)
4. Verify the information is correct.
5. Click **Create bucket**.

If bucket creation fails with a `400 Bad Request` error with the message `The Key CRN could not be found`, ensure that the CRN is correct and that the service to service authorization policy exists.
{: tip}

In the **Buckets** listing, the bucket has a _View_ link under **Attributes** where you can verify that the bucket has a Key Protect key enabled.

## Verify that it works
{: #replicate-bucket-verify}
{: step}
{: ui}

1. When viewing the bucket, click the **Configuration** tab to verify that the bucket has Key Protect enabled.
2. Scroll down to **Advanced configurations** and locate **Associated key management service** .
3. Verify the information is correct.

