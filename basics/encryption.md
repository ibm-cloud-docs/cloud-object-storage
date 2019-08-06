---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect, hpcs

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

{{site.data.keyword.cos_full}} provides several options to encrypt your data. By default, all objects stored in {{site.data.keyword.cos_full_notm}} are encrypted by using randomly generated keys and an all-or-nothing-transform (AONT). While this default encryption model provides at-rest security, some workloads need to be in possession of the data encryption keys used. You can manage your keys manually by providing your own encryption keys when storing data - referred to as Server Side Encryption with Customer-Provided Keys (SSE-C).

With {site.data.keyword.cos_short}} you also have a choice to leverage our integration capabilities with {{site.data.keyword.cloud}} Key Management Services like {{site.data.keyword.keymanagementservicelong}} and {{site.data.keyword.hscrypto}}. Depending on the security requirements, you can decide whether to use IBM Key Protect or IBM Hyper Protect Crypto Services when creating IBM Cloud Object Storage buckets.

[{{site.data.keyword.keymanagementservicefull}}]() helps you provision encrypted keys for apps across {site.data.keyword.cloud}} services. As you manage the lifecycle of your keys, you can benefit from knowing that your keys are secured by FIPS 140-2 Level 3 certified cloud-based hardware security modules (HSMs) that protect against the theft of information.

[{{site.data.keyword.hscrypto}}]() is a single-tenant, dedicated HSM that is controlled by you. The service is built on FIPS 140-2 Level 4-certified hardware, the highest offered by any cloud provider in the industry.

Please refer to product documentation on [{{site.data.keyword.keymanagementservicefull}}]() and [{{site.data.keyword.hscrypto}}]() for a detailed overview of the two services.


## Server Side Encryption with Customer-Provided Keys (SSE-C)
{: #encryption-sse-c}

SSE-C is enforced on objects. Requests to read or write objects or their metadata using customer manged keys send the required encryption infomation as headers in the HTTP requests. The syntax is identical to the S3 API, and S3-compatible libraries that support SSE-C should work as expected against {{site.data.keyword.cos_full}}.

Any request using SSE-C headers must be sent using SSL. Note that `ETag` values in response headers are *not* the MD5 hash of the object, but a randomly generated 32-byte hexadecimal string.

Header | Type | Description
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | string | This header is used to specify the algorithm and key size to use with the encryption key stored in `x-amz-server-side-encryption-customer-key` header. This value must be set to the string `AES256`.
`x-amz-server-side-encryption-customer-key` | string | This header is used to transport the base 64 encoded byte string representation of the AES 256 key used in the server side encryption process.
`x-amz-server-side-encryption-customer-key-MD5` | string | This header is used to transport the base64-encoded 128-bit MD5 digest of the encryption key according to RFC 1321. The object store will use this value to validate the key passes in the `x-amz-server-side-encryption-customer-key` has not been corrupted during transport and encoding process. The digest must be calculated on the key BEFORE the key is base 64 encoded.

## Server Side Encryption with {{site.data.keyword.keymanagementservicelong_notm}} or {{site.data.keyword.hscrypto}} (SSE-KP)
{: #encryption-kp}

You can use [IBM Key Protect](https://test.cloud.ibm.com/docs/services/key-protect?topic=key-protect-about) or [Hyper Protect Crypto Services](https://test.cloud.ibm.com/docs/services/hs-crypto?topic=hs-crypto-overview) to create, add, and manage keys, which you can then associate with your instance of IBM® Cloud Object Storage to encrypt buckets.

### Before you begin
Before you plan on using either Key Protect or Hyper Protect Crypto Services with Cloud Object Storage buckets, you will need:
•	an [IBM Cloud™ Platform account](http://cloud.ibm.com/)
•	an [instance of IBM Cloud Object Storage](http://cloud.ibm.com/catalog/services/cloud-object-storage)


### Getting started with SSE-KP
After you have made the decision to either use IBM Key Protect or Hyper Protect Crypto Services, you will need to ensure that a service instance is created by using the [IBM Cloud Catalog](https://cloud.ibm.com/catalog) and appropriate permissions are granted. This section outlines step by step instructions to help you get started. 


### Provisioning an instance of IBM Key Protect or Hyper Protect Crypto Services
Please refer to the service specific product pages for instructions on how to provision and setup appropriate service instances. 
•	Getting started with [IBM Key Protect](https://cloud.ibm.com/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-tutorial) 
•	Getting started with [Hyper Protect Crypto Services](https://cloud.ibm.com/docs/services/hs-crypto?topic=hs-crypto-get-started)

Once you have an instance of Key Protect or Hyper Protect Crypto Services in a region that you want to create a new bucket in, you need to create a root key and note the CRN of that key.
When creating the bucket, you need to provide additional headers.

### Create or add a key in Key Protect
Navigate to your instance of Key Protect and [generate or enter a root key](https://test.cloud.ibm.com/docs/services/key-protect?topic=key-protect-getting-started-tutorial).


### Create or add a key in Hyper Protect Crypto Services**
Navigate to your instance of Hyper Protect Crypto Services and [initialize the service instance](https://test.cloud.ibm.com/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm). Once a [master key](https://test.cloud.ibm.com/docs/services/hs-crypto?topic=hs-crypto-initialize-hsm#step1-create-signature-keys) has been created, [generate or enter a root key](https://test.cloud.ibm.com/docs/services/hs-crypto?topic=hs-crypto-create-root-keys).


### Grant service authorization
Authorize Key Protect or Hyper Protect Crypto Services for use with IBM COS:

1.	Open your IBM Cloud dashboard.
2.	From the menu bar, click **Manage > Access**.
3.	In the side navigation, click **Authorizations**.
4.	Click **Create authorization**.
5.	In the **Source service** menu, select **Cloud Object Storage**.
6.	In the **Source service instance** menu, select the service instance to authorize.
7.	In the **Target service** menu, select **IBM Key Protect** or **Hyper Protect Crypto Services**.
8.	In the **Target service instance** menu, select the service instance to authorize.
9.	Enable the **Reader** role.
10.	Click **Authorize**.

### Create a bucket
When your key exists in Key Protect or Hyper Protect Crypto Services and you authorized the service for use with IBM COS, associate the key with a new bucket:

1.	Navigate to your instance of Object Storage.
2.	Click **Create bucket**.
3.	Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
4.	In Advanced Configuration, enable **Add Key Protect Key** or **Add Hyper Protect Crypto Service key**.
5.	Select the associated service instance, key, and Key ID.
6.	Click **Create**.

You can choose to use Key Protect or Hyper Protect Crypto Services to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use Key Protect or Hyper Protect Crypto Services.
{:important}

In the **Buckets and objects** listing, the bucket now has a key icon under **Advanced**, indicating that the bucket has a Key Protect or Hyper Protect Crypto Services key enabled. To view the key details, click the menu at the right of the bucket and then click **View key**.

Note that the `Etag` value returned for objects encrypted using SSE-KP or HPCS **will** be the actual MD5 hash of the original unencrypted object.
{:tip}

### Rotating Keys
{: #encryption-rotate}

Key rotation is an important part of mitigating the risk of a data breach.  Periodically changing keys reduces the potential data loss if the key is lost or compromised. The frequency of key rotations varies by organization and depends on a number of variables including the environment, the amount of encrypted data, classification of the data, and compliance laws. The [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){:new_window} provides definitions of appropriate key lengths and provides guidelines for how long keys should be used.

For additional details, see the documentation for rotating keys in [Key Protect](/docs/services/key-protect?topic=key-protect-set-rotation-policy) or [HPCS](/docs/services/hs-crypto?topic=hs-crypto-rotating-keys).