---

copyright:
   years: 2022
lastupdated: "2022-04-07"

keywords: tutorials

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
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)

You will also need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. This tutorial does not outline the step-by-step instructions to help you get started.  This information is found in section  [Server-Side Encryption with IBM Key Protect (SSE-KP)](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-kp)


## Create a bucket
{: #kp-encrypt-bucket-create}
{: step}

When your key exists in Key Protect and you authorize a service for use with IBM COS, associate the key with a new bucket:

1. Navigate to your instance of Object Storage.
2. Click **Create bucket**.
![Navigate to COS](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-tut-1.png)
2. Select **Custom bucket**.
![Navigate to COS](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-tut-2.png)
3. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
![Navigate to COS](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-tut-3.png)

You can choose to use Key Protect to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use Key Protect.
{: important}

## Choose KP encryption
{: #kp-encrypt-bucket-select}
{: step}

1. Scroll down to **Service integrations (optional)**, toggle **Key management disabled** to enable encryption key management and click on **Create new instance**.
![Navigate to COS](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-tut-4.png)
2. Choose a region that corresponds with the bucket, give it a memorable name, and click **Create and continue**.
![Navigate to COS](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-tut-5.png)
3. Give the root key a name and click **Create and continue**.
![Navigate to COS](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-tut-6.png)
4. Verify the information is correct.
5. Click **Create bucket**.

If bucket creation fails with a `400 Bad Request` error with the message `The Key CRN could not be found`, ensure that the CRN is correct and that the service to service authorization policy exists.
{: tip}

In the **Buckets** listing, the bucket has a _View_ link under **Attributes** where you can verify that the bucket has a Key Protect key enabled.

## Verify that it works
{: #kp-encrypt-bucket-verify}
{: step}

1. When viewing the bucket, click the **Configuration** tab to verify that the bucket has Key Protect enabled.
2. Scroll down to **Advanced configurations** and locate **Associated key management service** .
3. Verify the information is correct.ß

