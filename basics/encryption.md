---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using [randomly generated keys and an all-or-nothing-transform](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security). While this default encryption model provides at-rest security, some workloads need to be in possession of the encryption keys used. You can manage your keys manually by providing your own encryption keys when storing data (SSE-C), or you can create buckets that use IBM Key Protect (SSE-KP) to manage encryption keys.

## Server Side Encryption with Customer-Provided Keys (SSE-C)
{: #encryption-sse-c}

SSE-C is enforced on objects. Requests to read or write objects or their metadata using customer manged keys send the required encryption infomation as headers in the HTTP requests. The syntax is identical to the S3 API, and S3-compatible libraries that support SSE-C should work as expected against {{site.data.keyword.cos_full}}.

Any request using SSE-C headers must be sent using SSL. Note that `ETag` values in response headers are *not* the MD5 hash of the object, but a randomly generated 32-byte hexadecimal string.

Header | Type | Description
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | string | This header is used to specify the algorithm and key size to use with the encryption key stored in `x-amz-server-side-encryption-customer-key` header. This value must be set to the string `AES256`.
`x-amz-server-side-encryption-customer-key` | string | This header is used to transport the base 64 encoded byte string representation of the AES 256 key used in the server side encryption process.
`x-amz-server-side-encryption-customer-key-MD5` | string | This header is used to transport the base64-encoded 128-bit MD5 digest of the encryption key according to RFC 1321. The object store will use this value to validate the key passes in the `x-amz-server-side-encryption-customer-key` has not been corrupted during transport and encoding process. The digest must be calculated on the key BEFORE the key is base 64 encoded.


## Server Side Encryption with {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}} is a centralized key management system (KMS) for generating, managing, and destroying encryption keys used by {{site.data.keyword.cloud_notm}} services. You can create an instance of {{site.data.keyword.keymanagementserviceshort}} from the {{site.data.keyword.cloud_notm}} catalog.

Once you have an instance of {{site.data.keyword.keymanagementserviceshort}} in a region that you want to create a new bucket in, you need to create a root key and note the CRN of that key.

You can choose to use {{site.data.keyword.keymanagementserviceshort}} to manage encryption for a bucket only at the time of creation. It isn't possible to change an existing bucket to use {{site.data.keyword.keymanagementserviceshort}}.
{:tip}

When creating the bucket, you need to provide additional headers.

For more information on {{site.data.keyword.keymanagementservicelong_notm}}, [see the documentation](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect).

### Getting started with SSE-KP
{: #sse-kp-gs}

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using multiple randomly generated keys and an all-or-nothing-transform. While this default encryption model provides at-rest security, some workloads need to be in possession of the encryption keys used. You can use [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) to create, add, and manage keys, which you can then associate with your instance of {{site.data.keyword.cos_full}} to encrypt buckets.

### Before you begin
{: #sse-kp-prereqs}

You'll need:
  * an [{{site.data.keyword.cloud}} Platform account](http://cloud.ibm.com)
  * an [instance of {{site.data.keyword.cos_full_notm}}](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * an [instance of {{site.data.keyword.keymanagementservicelong_notm}}](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * and some files on your local computer to upload.

### Create or add a key in {{site.data.keyword.keymanagementserviceshort}}
{: #sse-kp-add-key}

Navigate to your instance of {{site.data.keyword.keymanagementserviceshort}} and [generate or enter a key](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

### Grant service authorization
{: #sse-kp}
Authorize {{site.data.keyword.keymanagementserviceshort}} for use with IBM COS:

1. Open your {{site.data.keyword.cloud_notm}} dashboard.
2. From the menu bar, click **Manage** &gt; **Access**.
3. In the side navigation, click **Authorizations**.
4. Click **Create authorization**.
5. In the **Source service** menu, select **Cloud Object Storage**.
6. In the **Source service instance** menu, select the service instance to authorize.
7. In the **Target service** menu, select **{{site.data.keyword.keymanagementservicelong_notm}}**.
8. In the **Target service instance** menu, select the service instance to authorize.
9. Enable the **Reader** role.
10. Click **Authorize**.

### Create a bucket
{: #encryption-createbucket}

When your key exists in {{site.data.keyword.keymanagementserviceshort}} and you authorized the Key Protect service for use with IBM COS, associate the key with a new bucket:

1. Navigate to your instance of {{site.data.keyword.cos_short}}.
2. Click **Create bucket**.
3. Enter a bucket name, select the **Regional** resiliency, and choose a location and storage class.
4. In Advanced Configuration, enable **Add Key Protect Keys**.
5. Select the associated Key Protect service instance, key, and Key ID.
6. Click **Create**.

In the **Buckets and objects** listing, the bucket now has a key icon under **Advanced**, indicating that the bucket has a Key Protect key enabled. To view the key details, click the menu at the right of the bucket and then click **View Key Protect key**.

Note that the `Etag` value returned for objects encrypted using SSE-KP **will** be the actual MD5 hash of the original unencrypted object.
{:tip}


## Rotating Keys
{: #encryption-rotate}

Key rotation is an important part of mitigating the risk of a data breach.  Periodically changing keys reduces the potential data loss if the key is lost or compromised. The frequency of key rotations varies by organization and depends on a number of variables including the environment, the amount of encrypted data, classification of the data, and compliance laws. The [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){:new_window} provides definitions of appropriate key lengths and provides guidelines for how long keys should be used.

### Manual Key Rotation
{: #encryption-rotate-manual}

To rotate the keys for your {{site.data.keyword.cos_short}} you will need to create a new bucket with Key Protect enabled using a new Root Key and copy the contents from your existing bucket to the new one.

**NOTE**: Deleting a key from the system will shred its contents and any data still encrypted with that key. Once removed, it cannot be undone or reversed and will result in permanent data loss.

1. Create or add a new Root Key in your [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial) service.
2. [Create a new bucket](#encryption-createbucket) and add the new Root Key
3. Copy all the objects from your original bucket into the new bucket.
    1. This step can be accomplished using a number of different methods:
        1. From the command-line using [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) or [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)
        2. Using the (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object]
        3. Using the SDK with [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) or [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go)