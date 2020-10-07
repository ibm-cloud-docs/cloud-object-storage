---

copyright:
  years: 2018, 2020
lastupdated: "2020-10-01"

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

## Before you begin
{: #hpcs-begin}
Before you plan on using {{site.data.keyword.hscrypto}} with Cloud Object Storage buckets, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)

You will need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. This section outlines step-by-step instructions to help you get started. 

## Provisioning an instance of {{site.data.keyword.hscrypto}}
{: #hpcs-provision}
Refer to the service-specific product pages for instructions on how to provision and setup appropriate service instances.

- Getting started with [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-get-started)

Once you have an instance of {{site.data.keyword.hscrypto}} in a region that you want to create a new bucket in, you need to create a root key and note the CRN of that key. The CRN is sent in a header during bucket creation.

Note that the location in which the bucket is created must be the same location where the instance of {{site.data.keyword.hscrypto}} is operating.
{:important}

## Create or add a key in {{site.data.keyword.hscrypto}}
{: #hpcs-keys}
Navigate to your instance of {{site.data.keyword.hscrypto}} and [initialize the service instance](/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm). Once a [master key](/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm#step1-create-signature-keys) has been created, [generate or enter a root key](/docs/services/hs-crypto?topic=hs-crypto-create-root-keys).

## Grant service authorization
{: #hpcs-auth}
Authorize {{site.data.keyword.hscrypto}} for use with IBM COS:

1. Open your IBM Cloud dashboard.
2. From the menu bar, click **Manage > Access**.
3. In the side navigation, click **Authorizations**.
4. Click **Create authorization**.
5. In the **Source service** menu, select **Cloud Object Storage**.
6. In the **Source service instance** menu, select the service instance to authorize.
7. In the **Target service** menu, select **{{site.data.keyword.hscrypto}}**.
8. In the **Target service instance** menu, select the service instance to authorize.
9. Enable the **Reader** role.
10. Click **Authorize**.

## Create a bucket
{: #hpcs-bucket}
When your key exists in {{site.data.keyword.hscrypto}} and you authorized the service for use with IBM COS, associate the key with a new bucket:

1. Navigate to your instance of Object Storage.
2. Click **Create bucket**.
3. Click **Custom bucket**.
3. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
4. In Advanced Configuration, under **Key management services** click on **Add** in the **{{site.data.keyword.hscrypto}}** section.
5. Select the associated service instance and key, and click **Associate key**.
5. Verify the information is correct.
6. Click **Create**.

You can choose to use {{site.data.keyword.hscrypto}} to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use {{site.data.keyword.hscrypto}}.
{:important}

If bucket creation fails with a `400 Bad Request` error with the message `The Key CRN could not be found`, ensure that the CRN is correct and that the service to service authorization policy exists.
{:tip}

In the **Buckets and objects** listing, the bucket now has a _View_ link under **Advanced**, indicating that the bucket has a {{site.data.keyword.hscrypto}} key enabled. To view the key details, click _View_.

Note that the `Etag` value returned for objects encrypted using {{site.data.keyword.hscrypto}} **will** be the actual MD5 hash of the original decrypted object.
{:tip}

It is also possible to use [the REST API](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-key-protect) or SDKs ([Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go#go-examples-kp), [Java](/docs/cloud-object-storage?topic=cloud-object-storage-java#java-examples-kp), [Node.js](/docs/cloud-object-storage?topic=cloud-object-storage-node#node-examples-kp), or [Python](/docs/cloud-object-storage?topic=cloud-object-storage-python#python-examples-kp)).

## Rotating Keys
{: #hpcs-rotate}

Key rotation is an important part of mitigating the risk of a data breach. Periodically changing keys reduces the potential data loss if the key is lost or compromised. The frequency of key rotations varies by organization and depends on a number of variables, such as the environment, the amount of encrypted data, classification of the data, and compliance laws. The [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){: external} provides definitions of appropriate key lengths and provides guidelines for how long keys should be used.

For more information, see the documentation for rotating keys in [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-key-rotation).

## Cryptographic erasure
{: #hpcs-erase}

Cryptographic erasure (or crypto-shredding) is a method of rendering encrypted data  unreadable by [deleting the encryption keys](/docs/hs-crypto?topic=hs-crypto-security-and-compliance#data-deletion) rather than the data itself. When a [root key is deleted](/docs/hs-crypto?topic=hs-crypto-delete-keys), it will affect all objects in any buckets created using that root key, effectively "shredding" the data and preventing any further reading or writing to the buckets. This process is not instantaneous, but occurs within a few minutes after the key is deleted.

Although objects in a crypto-shredded bucket can not be read, and new object can not be written, existing objects will continue to consume storage until they are deleted by a user.
{: tip}

## Activity Tracking
Changes made to {{site.data.keyword.hscrypto}} keys do not generate specific Activity Tracker events at this time.
