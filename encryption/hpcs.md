---

copyright:
  years: 2018, 2021
lastupdated: "2021-12-15"

keywords: encryption, security, {{site.data.keyword.hscrypto}}

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

# Server-Side Encryption with {{site.data.keyword.hscrypto}} 
{: #hpcs}

You can use [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-overview) to create, add, and manage keys, which you can then associate with your instance of IBM® Cloud Object Storage to encrypt buckets.

This feature is not currently supported in {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}. [Learn more.](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite)
{: note}

## Before you begin
{: #hpcs-begin}
Before you plan on using {{site.data.keyword.hscrypto}} with Cloud Object Storage buckets, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage) with a *standard* pricing plan.

You will need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. This section outlines step-by-step instructions to help you get started. 

It is not possible to use [Immutable Object Storage](/docs/cloud-object-storage/basics?topic=cloud-object-storage-immutable) to create retention policies that prevent object deletion or modification on buckets that use {{site.data.keyword.hscrypto}}.
{:tip}

## Provisioning an instance of {{site.data.keyword.hscrypto}}
{: #hpcs-provision}
Refer to the service-specific product pages for instructions on how to provision and setup appropriate service instances.

- Getting started with [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-get-started)

Once you have an instance of {{site.data.keyword.hscrypto}}, you need to create a root key and note the CRN ([Cloud Resource Name](/docs/account?topic=account-crn)) of that key. The CRN is sent in a header during bucket creation.

Before creating the bucket for use with {{site.data.keyword.hscrypto}}, review the [relevant guidance around availability and disaster recovery](/docs/hs-crypto?hs-crypto-ha-dr).  

## Create or add a key in {{site.data.keyword.hscrypto}}
{: #hpcs-keys}
Navigate to your instance of {{site.data.keyword.hscrypto}} and [initialize the service instance](/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm). Once a [master key](/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm#step1-create-signature-keys) has been created, [generate or enter a root key](/docs/services/hs-crypto?topic=hs-crypto-create-root-keys).

## Grant service authorization
{: #hpcs-auth}
Authorize {{site.data.keyword.hscrypto}} for use with IBM COS:

1. Open your IBM Cloud dashboard.
2. From the menu bar, click **Manage > Access (IAM)**.
3. In the side navigation, click **Authorizations**.
4. Click **Create** to create an authorization.
5. In the **Source service** menu, select **Cloud Object Storage**.
6. In the **Source service instance** menu, select the service instance to authorize.
7. In the **Target service** menu, select **{{site.data.keyword.hscrypto}}**.
8. In the **Target service instance** menu, select the service instance to authorize.
9. Enable the **Reader** role.
10. Click **Authorize**.

## Create a bucket
{: #hpcs-bucket}
When your key exists in {{site.data.keyword.hscrypto}} and you authorized the service for use with IBM COS, you can now associate the key with a new bucket:

1. Navigate to your instance of Object Storage.
2. Click **Create bucket**.
3. Select **Custom bucket**.
3. Enter a bucket name, select the resiliency (only Regional and US Cross Region are currently supported), and choose a location and storage class.
4. In **Service integrations**, toggle **Key management disabled** to enable encryption key management and click on **Use existing instance**.
5. Select the associated service instance and key, and click **Associate key**.
5. Verify the information is correct.
6. Click **Create**.

You can choose to use {{site.data.keyword.hscrypto}} to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use {{site.data.keyword.hscrypto}}.
{:important}

If bucket creation fails with a `400 Bad Request` error with the message `The Key CRN could not be found`, ensure that the CRN is correct and that the service to service authorization policy exists.
{:tip}

In the **Buckets** listing, the bucket now has a _View_ link under **Attributes**, indicating that the bucket has a {{site.data.keyword.hscrypto}} key enabled. To view the key details (along with other object metadata), click _View_.

Note that the `Etag` value returned for objects encrypted using {{site.data.keyword.hscrypto}} **will** be the actual MD5 hash of the original decrypted object.
{:tip}

It is also possible to use [the REST API](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-key-protect) or SDKs ([Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go#go-examples-kp), [Java](/docs/cloud-object-storage?topic=cloud-object-storage-java#java-examples-kp), [Node.js](/docs/cloud-object-storage?topic=cloud-object-storage-node#node-examples-kp), or [Python](/docs/cloud-object-storage?topic=cloud-object-storage-python#python-examples-kp)).

### Creating Cross Region buckets
{: #hpcs-cr}

Creating COS Cross Region bucket with a root key from a {{site.data.keyword.hscrypto}} instance requires that instance to be [configured with failover configuration](https://cloud.ibm.com/docs/hs-crypto?topic=hs-crypto-enable-add-failover). 

You can confirm that failover is properly configured for the selected {{site.data.keyword.hscrypto}} instance correctly using either the IBM Cloud console or CLI.

From IBM Cloud console, navigate to a {{site.data.keyword.hscrypto}} instance and click on **Overview**. A "Failover" section will indicate the status of crypto units in the corresponding failover regions.
 
Ensure the failover section is present, all validation checks are green and there are no warnings for that {{site.data.keyword.hscrypto}} instance.  If you see any errors or warnings, or if the failover section is not present, [refer to the {{site.data.keyword.hscrypto}} documentation for further guidance](https://cloud.ibm.com/docs/hs-crypto?topic=hs-crypto-enable-add-failover).

You can also use the CLI to list all the crypto units for all instances belong to the targeted resource group:

```sh
ibmcloud tke cryptounits
```
{:screen}

To get status of crypto units for the selected instance, create a list of crypto units associated with that instance and compare them:

```sh
ibmcloud tke cryptounit-add
```

After the units have been selected, you can check their verification patterns:

```sh
ibmcloud tke cryptounit-compare
```

Make sure all of them are valid and have same verification pattern.

Once the presence of the failover configuration is verified, you may proceed to create the Cross Region bucket using the key from that {{site.data.keyword.hscrypto}} instance.

If the Cross Region bucket creation in US Cross Region with a {{site.data.keyword.hscrypto}} root key fails with a `500` error, then the user is advised to check if the status of failover configuration for that {{site.data.keyword.hscrypto}} instance (using the methods detailed above) before reattempting the bucket creation.
{:important}


## Key lifecycle management 
{: #hpcs-lifecycle}

{{site.data.keyword.hscrypto}} offers various ways to manage the lifecycle of encryption keys.  For more details, see [the {{site.data.keyword.hscrypto}} documentation](/docs/services/hs-crypto?topic=hs-crypto-overview).

### Rotating Keys
{: #hpcs-rotate}

Key rotation is an important part of mitigating the risk of a data breach. Periodically changing keys reduces the potential data loss if the key is lost or compromised. The frequency of key rotations varies by organization and depends on a number of variables, such as the environment, the amount of encrypted data, classification of the data, and compliance laws. The [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){: external} provides definitions of appropriate key lengths and provides guidelines for how long keys should be used.

For more information, see the documentation for rotating keys in [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-key-rotation#root-key-rotation-intro).

### Disabling and re-enabling keys
{: #hpcs-disable}

As an admin, you might need to [temporarily disable a root key](/docs/hs-crypto?topic=hs-crypto-disable-keys) if you suspect a possible security exposure, compromise, or breach with your data. When you disable a root key, you suspend its encrypt and decrypt operations. After confirming that a security risk is no longer active, you can reestablish access to your data by enabling the disabled root key.

### Deleting keys and cryptographic erasure
{: #hpcs-cryptoerasure}

Cryptographic erasure (or crypto-shredding) is a method of rendering encrypted data  unreadable by [deleting the encryption keys](/docs/hs-crypto?topic=hs-crypto-security-and-compliance#data-deletion) rather than the data itself. When a [root key is deleted in {{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-delete-keys), it will affect all objects in any buckets created using that root key, effectively "shredding" the data and preventing any further reading or writing to the buckets. This process is not instantaneous, but occurs within approximatedly 90 seconds after the key is deleted.

Although objects in a crypto-shredded bucket can not be read, and new object can not be written, existing objects will continue to consume storage until they are deleted by a user.
{: tip}

### Restoring a deleted key 
{: #hpcs-restore}

As an admin, you might need to [restore a root key that you imported](/docs/hs-crypto?topic=hs-crypto-restore-keys) to {{site.data.keyword.hscrypto}} so that you can access data that the key previously protected. When you restore a key, you move the key from the Destroyed to the Active key state, and you restore access to any data that was previously encrypted with the key. This must occur within 30 days of deleting a key.

If a key that was originally uploaded by a user is deleted, and then restored using different key material, it **will result in a loss of data**. It is recommended to keep n-5 keys archived somewhere in order to ensure that the correct key material is available for restoration.
{: important}

## Activity Tracking
{: #hpcs-at}

When {{site.data.keyword.hscrypto}} root keys are deleted, rotated, suspended, enabled, or restored, an [Activity Tracker management event](/docs/cloud-object-storage?topic=cloud-object-storage-at-events#at-actions-global) (`cloud-object-storage.bucket-key-state.update`) is generated in addition to any events logged by {{site.data.keyword.hscrypto}}. 

In the event of a server-side failure in a lifecycle action on a key, that failure is not logged by COS.  If {{site.data.keyword.hscrypto}} does not receive a success from COS for the event handling within four hours of the event being sent, {{site.data.keyword.hscrypto}} will log a failure.
{:note}

The `cloud-object-storage.bucket-key-state.update` actions are triggered by events taking place in {{site.data.keyword.hscrypto}}, and require that the bucket is registered with the {{site.data.keyword.hscrypto}} service.  This registration happens automatically when a bucket is created with a {{site.data.keyword.hscrypto}} root key.

Buckets created prior to February 26th, 2020 are not registered with the {{site.data.keyword.hscrypto}} service and will not receive notifications of encryption key lifecycle events at this time. These buckets can be identified by performing [a bucket listing operation](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets) and looking at the dates for bucket creation. To ensure that these buckets have the latest key state from {{site.data.keyword.hscrypto}}, it is recommended that [some data operation is performed](/docs/cloud-object-storage?topic=cloud-object-storage-object-operations#object-operations-head), such as a `PUT`, `GET`, or `HEAD` on an object in each affected bucket.  It is recommended that an object operation is done twice, at least an hour apart, to ensure that the key state is properly in synchronization with the {{site.data.keyword.hscrypto}} state.
{: important}

For more information on Activity Tracker events for object storage, [see the reference topic](/docs/cloud-object-storage?topic=cloud-object-storage-at-events).
