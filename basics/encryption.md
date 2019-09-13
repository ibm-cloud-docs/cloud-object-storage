---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-09-12"

keywords: encryption, security, sse-c, key protect, {{site.data.keyword.hscrypto}}

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 

# Manage encryption
{: #encryption}

{{site.data.keyword.cos_full}} provides several options to encrypt your data. By default, all objects that are stored in {{site.data.keyword.cos_full_notm}} are encrypted by using randomly generated keys and an all-or-nothing-transform (AONT). While this default encryption model provides at-rest security, some workloads need full control over the data encryption keys used. You can manage your keys manually by providing your own encryption keys - referred to as Server-Side Encryption with Customer-Provided Keys (SSE-C).

With {{site.data.keyword.cos_short}} you also have a choice to use our integration capabilities with {{site.data.keyword.cloud}} Key Management Services like {{site.data.keyword.keymanagementservicelong}} and {{site.data.keyword.hscrypto}}. Depending on the security requirements, you can decide whether to use IBM Key Protect or IBM {{site.data.keyword.hscrypto}} for your IBM Cloud Object Storage buckets.

[{{site.data.keyword.keymanagementservicefull}}](/docs/services/key-protect?topic=key-protect-about) helps you provision encrypted keys for apps across {site.data.keyword.cloud}} services. As you manage the lifecycle of your keys, you can benefit from knowing that your keys are secured by FIPS 140-2 Level 3 certified cloud-based hardware security modules (HSMs) that protect against the theft of information.

[{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-overview) is a single-tenant, dedicated HSM that is controlled by you. The service is built on FIPS 140-2 Level 4-certified hardware, the highest offered by any cloud provider in the industry.

Refer to product documentation on [{{site.data.keyword.keymanagementservicefull}}](/docs/services/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-overview) for a detailed overview of the two services.


## Server-Side Encryption with Customer-Provided Keys (SSE-C)
{: #encryption-sse-c}

SSE-C is enforced on objects. Requests to read or write objects or their metadata that use customer-managed keys send the required encryption information as headers in the HTTP requests. The syntax is the same as the S3 API, and S3-compatible libraries that support SSE-C work as expected against {{site.data.keyword.cos_full}}.

Any request that uses SSE-C headers must be sent by using SSL. The `ETag` values in response headers are *not* the MD5 hash of the object, but a randomly generated 32-byte hexadecimal string.

Header | Type | Description
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | String | This header is used to specify the algorithm and key size to use with the encryption key stored in `x-amz-server-side-encryption-customer-key` header. This value must be set to the string `AES256`.
`x-amz-server-side-encryption-customer-key` | String | This header is used to transport the base 64 encoded byte string representation of the AES 256 key used in the server-side encryption process.
`x-amz-server-side-encryption-customer-key-MD5` | String | This header is used to transport the base64-encoded 128-bit MD5 digest of the encryption key according to RFC 1321. The object store uses this value to validate the key passes in the `x-amz-server-side-encryption-customer-key` has not been corrupted during transport and encoding process. The digest must be calculated on the key BEFORE the key is base 64 encoded.

## Server-Side Encryption with {{site.data.keyword.keymanagementservicelong_notm}} or {{site.data.keyword.hscrypto}} (SSE-KP)
{: #encryption-kp}

You can use [IBM Key Protect](/docs/services/key-protect?topic=key-protect-about) or [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-overview) to create, add, and manage keys, which you can then associate with your instance of IBM® Cloud Object Storage to encrypt buckets.

### Before you begin
Before you plan on using either Key Protect or {{site.data.keyword.hscrypto}} with Cloud Object Storage buckets, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)


### Getting started with SSE-KP
After you have made the decision to either use IBM Key Protect or {{site.data.keyword.hscrypto}}, you will need to ensure that a service instance is created by using the [IBM Cloud catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. This section outlines step-by-step instructions to help you get started. 


### Provisioning an instance of IBM Key Protect or {{site.data.keyword.hscrypto}}
Refer to the service-specific product pages for instructions on how to provision and setup appropriate service instances.

- Getting started with [IBM Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-tutorial) 
- Getting started with [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-get-started)

Once you have an instance of Key Protect or {{site.data.keyword.hscrypto}} in a region that you want to create a new bucket in, you need to create a root key and note the CRN of that key. The CRN is sent in a header during bucket creation.

Note that the location in which the bucket is created must be the same location where the instance of Key Protect or {{site.data.keyword.hscrypto}}.
{:important}

### Create or add a key in Key Protect
Navigate to your instance of Key Protect and [generate or enter a root key](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).


### Create or add a key in {{site.data.keyword.hscrypto}}
Navigate to your instance of {{site.data.keyword.hscrypto}} and [initialize the service instance](/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm). Once a [master key](/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm#step1-create-signature-keys) has been created, [generate or enter a root key](/docs/services/hs-crypto?topic=hs-crypto-create-root-keys).


### Grant service authorization
Authorize Key Protect or {{site.data.keyword.hscrypto}} for use with IBM COS:

1. Open your IBM Cloud dashboard.
2. From the menu bar, click **Manage > Access**.
3. In the side navigation, click **Authorizations**.
4. Click **Create authorization**.
5. In the **Source service** menu, select **Cloud Object Storage**.
6. In the **Source service instance** menu, select the service instance to authorize.
7. In the **Target service** menu, select **IBM Key Protect** or **{{site.data.keyword.hscrypto}}**.
8. In the **Target service instance** menu, select the service instance to authorize.
9. Enable the **Reader** role.
10. Click **Authorize**.

### Create a bucket
When your key exists in Key Protect or {{site.data.keyword.hscrypto}} and you authorized the service for use with IBM COS, associate the key with a new bucket:

1. Navigate to your instance of Object Storage.
2. Click **Create bucket**.
3. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
4. In Advanced Configuration, enable **Add Key Protect Key** or **Add Hyper Protect Crypto Service key**.
5. Select the associated service instance, key, and Key ID.
6. Click **Create**.

You can choose to use Key Protect or {{site.data.keyword.hscrypto}} to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use Key Protect or {{site.data.keyword.hscrypto}}.
{:important}

If bucket creation fails with a `400 Bad Request` error with the message `The Key CRN could not be found`, ensure that the CRN is correct and that the service to service authorization policy exists.
{:tip}

In the **Buckets and objects** listing, the bucket now has a _View_ link under **Advanced**, indicating that the bucket has a Key Protect or {{site.data.keyword.hscrypto}} key enabled. To view the key details, click _View_.

Note that the `Etag` value returned for objects encrypted using SSE-KP or {{site.data.keyword.hscrypto}} **will** be the actual MD5 hash of the original decrypted object.
{:tip}

It is also possible to use [the REST API](/docs/services/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-key-protect) or SDKs ([Go](/docs/services/cloud-object-storage?topic=cloud-object-storage-go#go-examples-kp), [Java](/docs/services/cloud-object-storage?topic=cloud-object-storage-java#java-examples-kp-bucket), [Node.js](/docs/services/cloud-object-storage?topic=cloud-object-storage-node#node-examples-kp), [Python](/docs/services/cloud-object-storage?topic=cloud-object-storage-python#python-examples-kp))



### Rotating Keys
{: #encryption-rotate}

Key rotation is an important part of mitigating the risk of a data breach. Periodically changing keys reduces the potential data loss if the key is lost or compromised. The frequency of key rotations varies by organization and depends on a number of variables, such as the environment, the amount of encrypted data, classification of the data, and compliance laws. The [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){:new_window} provides definitions of appropriate key lengths and provides guidelines for how long keys should be used.

For more information, see the documentation for rotating keys in [Key Protect](/docs/services/key-protect?topic=key-protect-set-rotation-policy) or [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-rotating-keys).