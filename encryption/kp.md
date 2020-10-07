---

copyright:
  years: 2018, 2020
lastupdated: "2020-10-01"

keywords: encryption, security, sse-c, key protect

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}
{:help: data-hd-content-type='help'}

# Server-Side Encryption with {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #kp}

You can use [IBM Key Protect](/docs/services/key-protect?topic=key-protect-about) to create, add, and manage keys, which you can then associate with your instance of IBM® Cloud Object Storage to encrypt buckets.

## Before you begin
{: #kp-begin}
Before you plan on using Key Protect with Cloud Object Storage buckets, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)

You will also need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. This section outlines step-by-step instructions to help you get started. 

## Provisioning an instance of IBM Key Protect
{: #kp-provision}
Refer to the service-specific product pages for instructions on how to provision and setup appropriate service instances.

- Getting started with [IBM Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-tutorial) 

Once you have an instance of Key Protect in a region that you want to create a new bucket in, you need to create a root key and note the CRN ([Cloud Resource Name](/docs/account?topic=account-crn)) of that key. The CRN is sent in a header during bucket creation.

Note that the location in which the bucket is created must be the same location where the instance of Key Protect is operating.
{:important}

## Create or add a key in Key Protect
{: #kp-create}
Navigate to your instance of Key Protect and [generate or enter a root key](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

## Grant service authorization
{: #kp-sa}
Authorize Key Protect for use with IBM COS:

1. Open your IBM Cloud dashboard.
2. From the menu bar, click **Manage > Access**.
3. In the side navigation, click **Authorizations**.
4. Click **Create authorization**.
5. In the **Source service** menu, select **Cloud Object Storage**.
6. In the **Source service instance** menu, select the service instance to authorize.
7. In the **Target service** menu, select **IBM Key Protect**.
  ![Grant service authorization](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-grant-auth.png){: caption="Figure 1: Grant service authorization for Key Protect."}
8. In the **Target service instance** menu, select the service instance to authorize. The additional fields may be left blank.
9. Enable the **Reader** role.
10. Click **Authorize**.

## Create a bucket
{: #kp-bucket}
When your key exists in Key Protect and you authorized the service for use with IBM COS, associate the key with a new bucket:

1. Navigate to your instance of Object Storage.
2. Click **Create bucket**.
3. Click **Custom bucket**.
3. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
4. In Advanced Configuration, under **Key management services** click on **Add**.
  ![Add KP](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-add.png){: caption="Figure 2: Add Key Protect to a new bucket."}
5. Select the associated service instance and key, and click **Associate key**.
  ![Add KP](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/kp-associate-key.png){: caption="Figure 3: Choose a root key."}
5. Verify the information is correct.
6. Click **Create**.

You can choose to use Key Protect to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use Key Protect.
{:important}

If bucket creation fails with a `400 Bad Request` error with the message `The Key CRN could not be found`, ensure that the CRN is correct and that the service to service authorization policy exists.
{:tip}

In the **Buckets** listing, the bucket has a _View_ link under **Attributes** where you can verify that the bucket has a Key Protect key enabled.

Note that the `Etag` value returned for objects encrypted using SSE-KP **will** be the actual MD5 hash of the original decrypted object.
{:tip}

It is also possible to use [the REST API](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-key-protect) or SDKs ([Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go#go-examples-kp), [Java](/docs/cloud-object-storage?topic=cloud-object-storage-java#java-examples-kp), [Node.js](/docs/cloud-object-storage?topic=cloud-object-storage-node#node-examples-kp), or [Python](/docs/cloud-object-storage?topic=cloud-object-storage-python#python-examples-kp)).


## Key lifecycle management 
{: #kp-lifecycle}

Key Protect offers various ways to manage the lifecycle of encryption keys.  For more details, see [the Key Protect documentation](/docs/key-protect?topic=key-protect-key-states).

### Rotating Keys
{: #kp-rotate}

Key rotation is an important part of mitigating the risk of a data breach. Periodically changing keys reduces the potential data loss if the key is lost or compromised. The frequency of key rotations varies by organization and depends on a number of variables, such as the environment, the amount of encrypted data, classification of the data, and compliance laws. The [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){: external} provides definitions of appropriate key lengths and provides guidelines for how long keys should be used.

For more information, see the documentation for rotating keys in [Key Protect](/docs/key-protect?topic=key-protect-set-rotation-policy).

### Disabling and re-enabling keys
{: #kp-disable}

As an admin, you might need to [temporarily disable a root key](/docs/key-protect?topic=key-protect-disable-keys) if you suspect a possible security exposure, compromise, or breach with your data. When you disable a root key, you suspend its encrypt and decrypt operations. After confirming that a security risk is no longer active, you can reestablish access to your data by enabling the disabled root key.

If a key is disabled, and then re-enabled quickly, requests made to that bucket may be rejected for up to an hour before cached key information is refreshed.  
{:note}

### Deleting keys and cryptographic erasure
{: kp-cryptoerasure}

Cryptographic erasure (or crypto-shredding) is a method of rendering encrypted data  unreadable by [deleting the encryption keys](/docs/key-protect?topic=key-protect-security-and-compliance#data-deletion) rather than the data itself. When a [root key is deleted in Key Protect](/docs/key-protect?topic=key-protect-delete-keys), it will affect all objects in any buckets created using that root key, effectively "shredding" the data and preventing any further reading or writing to the buckets. This process is not instantaneous, but occurs within approximatedly 90 seconds after the key is deleted.

Although objects in a crypto-shredded bucket can not be read, and new object can not be written, existing objects will continue to consume storage until they are deleted by a user.
{: tip}

### Restoring a deleted key 
{: kp-restore}

As an admin, you might need to [restore a root key that you imported](/docs/key-protect?topic=key-protect-restore-keys) to Key Protect so that you can access data that the key previously protected. When you restore a key, you move the key from the Destroyed to the Active key state, and you restore access to any data that was previously encrypted with the key. This must occur within 30 days of deleting a key.

If a key that was originally uploaded by a user is deleted, and then restored using different key material, it **will result in a loss of data**. It is recommended to keep n-5 keys archived somewhere in order to ensure that the correct key material is available for restoration.
{: important}

## Activity Tracking
{: kp-at}

When a Key Protect root keys are deleted, rotated, suspended, enabled, or restored, an [Activity Tracker management event](/docs/cloud-object-storage?topic=cloud-object-storage-at-events#at-actions-global) (`cloud-object-storage.bucket-key-state.update`) is generated in addition to any events logged by Key Protect. 

In the event of a server-side failure in a lifecycle action on a key, that failure is not logged by COS.  If Key Protect does not receive a success from COS for the event handling within four hours of the event being sent, Key Protect will log a failure.
{:note}

The `cloud-object-storage.bucket-key-state.update` actions are triggered by events taking place in Key Protect, and require that the bucket is registered with the Key Protect service.  This registration happens automatically when a bucket is created with a Key Protect root key.

Key lifecycle events will not be generated for buckets created prior to February 26th, 2020 at this time. If a key is altered in some fashion, buckets will not know about this change until some data operation is performed, such as reading or writing an object, at which point the object storage service will check with Key Protect. In other words, if keys have been rotated or updated to a new version, it is important that all buckets are registered with the same version of the key. After keys are rotated, it is critical that all buckets recieve some sort of data operation ([such as a HEAD object](/docs/cloud-object-storage?topic=cloud-object-storage-object-operations#object-operations-head)) to update and refresh the key information.
{: important}

For more information on Activity Tracker events for object storage, [see the reference topic](/docs/cloud-object-storage?topic=cloud-object-storage-at-events).
