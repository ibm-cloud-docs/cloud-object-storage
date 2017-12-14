---
copyright:
  years: 2017
lastupdated: '2017-09-27'
---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Manage encryption

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using [randomly generated keys and an all-or-nothing-transform](/docs/services/cloud-object-storage/info/data-security-encryption.html). While this default encryption model is highly secure, some workloads need to be in possession of the encryption keys used.  You can manage your keys manually by providing your own encryption keys when storing data (SSE-C), or you can create buckets that use IBM Key Protect (SSE-KP) to manage encryption keys.

## Server Eide Encryption with Customer-Provided Keys (SSE-C)
{: #sse-c}

SSE-C is enforced on objects.  Requests to read or write objects or their metadata using customer manged keys send the required encryption infomation as headers in the HTTP requests.  The syntax is identical to the S3 API, and S3-compatible libraries that support SSE-C should work as expected against {{site.data.keyword.cos_full}}.

Any request using SSE-C headers must be sent using SSL. Note that `ETag` values in response headers are *not* the MD5 hash of the object, but a randomly generated 32-byte hexadecimal string.

Header | Type | Description
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | string | This header is used to specify the algorithm and key size to use with the encryption key stored in `x-amz-server-side-encryption-customer-key` header. This value must be set to the string `AES256`.
`x-amz-server-side-encryption-customer-key` | string | This header is used to transport the base 64 encoded byte string representation of the AES 256 key used in the server side encryption process.
`x-amz-server-side-encryption-customer-key-MD5` | string | This header is used to transport the base64-encoded 128-bit MD5 digest of the encryption key according to RFC 1321. The object store will use this value to validate the key passes in the `x-amz-server-side-encryption-customer-key` has not been corrupted during transport and encoding process. The digest must be calculated on the key BEFORE the key is base 64 encoded.


## Server Eide Encryption with {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #sse-kp}

{{site.data.keyword.keymanagementservicefull}} is a centralized key management system (KMS) for generating, managing, and destroying encryption keys used by {{site.data.keyword.cloud_notm}} services.  You can create an instance of {{site.data.keyword.keymanagementserviceshort}} from the {{site.data.keyword.cloud_notm}} catalog.

Once you have an instance of {{site.data.keyword.keymanagementserviceshort}} in a region that you want to create a new bucket in, you need to create a root key and note the CRN of that key.

You can choose to use {{site.data.keyword.keymanagementserviceshort}} to manage encryption for a bucket only at the time of creation.  It isn't possible to change an existing bucket to use {{site.data.keyword.keymanagementserviceshort}}. 
{:tip}

When creating the bucket, you need to provide additional headers.

For more information on {{site.data.keyword.keymanagementservicelong_notm}}, [see the documentation](/docs/services/keymgmt/index.html#getting-started-with-key-protect).

### Getting started with SSE-KP

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using multiple randomly generated keys and an all-or-nothing-transform. While this default encryption model is remarkably secure, some workloads need to be in possession of the encryption keys used. You can use [{{site.data.keyword.keymanagementservice_long}}](/docs/services/keymgmt/keyprotect_about.html) to create, add, and manage keys, which you can then associate with your instance of {{site.data.keyword.cos_full}} to encrypt buckets.

{{site.data.keyword.keymanagementserviceshort}} is currently only available in the US-South region.
{: note}

### Before you begin
You'll need:
  * an [{{site.data.keyword.cloud}} Platform account](https://console.bluemix.net/registration/?target=%2Fcatalog%2Finfrastructure%2Fcloud-object-storage)
  * an [instance of {{site.data.keyword.cos_full_notm}}](https://console.bluemix.net/catalog/infrastructure/object-storage-group?env_id=ibm:yp:us-south){: new_window}
  * an [instance of {{site.data.keyword.keymanagementservicelong_notm}}](https://console.ng.bluemix.net/catalog/services/key-protect/?taxonomyNavigation=apps){: new_window}
  * and some files on your local computer to upload.
{: #prereqs}

### Create or add a key in {{site.data.keyword.keymanagementserviceshort}}

Navigate to your instance of {{site.data.keyword.keymanagementserviceshort}} and [generate or enter a key](/docs/services/keymgmt/index.html#getting-started-with-key-protect).

### Grant service authorization

Authorize {{site.data.keyword.keymanagementserviceshort}} to work with IBM COS:

1. Open your {{site.data.keyword.cloud_notm}} dashboard.
2. From the menu bar, click **Manage** &gt; **Account** &gt; **Users**.
3. In the side navigation, click **Identity & Access** &gt; **Authorizations**.
4. Click **Create authorization**.
5. In the **Source service** menu, select **Cloud Object Storage**.
6. In the **Source service instance** menu, select the service instance to authorize.
7. In the **Target service** menu, select **{{site.data.keyword.keymanagementservicelong_notm }}**.
8. In the **Target service instance** menu, select the service instance to authorize.
9. Enable the **Reader** role.
10. Click **Authorize**.

### Create a bucket

Once your key exists in {{site.data.keyword.keymanagementserviceshort}} and you authorized the Key Protect service to work with IBM COS, associate the key with a new bucket:

1. Navigate to your instance of {{site.data.keyword.cos_short}}.
2. Click **Create bucket**.
3. Add a bucket name.hy
4. Select the **Regional** resiliency.
5. Select the **us-south** location.
6. Select a storage class.
7. In Advanced Configuration, enable **Add Key Protect Keys**.
  1. Select the associated Key Protect service instance.
  2. Select a key.
  3. Select a Key ID.
4. Click **Create**.

In the **Buckets and objects** listing, the bucket now has a key icon under **Advanced**, indicating that the bucket has a Key Protect key enabled. To view the key details, click the menu at the right of the bucket and then click **View Key Protect key**.
