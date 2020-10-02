---

copyright:
  years: 2018, 2020
lastupdated: "2020-06-19"

keywords: encryption, security, sse-c, key protect, {{site.data.keyword.hscrypto}}

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

# Encrypting your data
{: #encryption}

{{site.data.keyword.cos_full}} provides several options to encrypt your data. 
{: shortdesc}

By default, all objects that are stored in {{site.data.keyword.cos_full_notm}} are encrypted by using randomly generated keys and an all-or-nothing-transform (AONT). While this default encryption model provides at-rest security, some workloads need full control over the data encryption keys used. You can manage your keys manually on a per-object basis by providing your own encryption keys - referred to as [Server-Side Encryption with Customer-Provided Keys (SSE-C)](docs/cloud-object-storage?topic=sse-c).

With {{site.data.keyword.cos_short}} you also have a choice to use our integration capabilities with {{site.data.keyword.cloud}} Key Management Services like {{site.data.keyword.keymanagementservicelong}} and {{site.data.keyword.hscrypto}}. Depending on the security requirements, you can decide whether to use IBM Key Protect or IBM {{site.data.keyword.hscrypto}} for your IBM Cloud Object Storage buckets.

[{{site.data.keyword.keymanagementservicefull}}](/docs/services/key-protect?topic=key-protect-about) helps you provision encrypted keys for apps across {{site.data.keyword.cloud}} services. As you manage the lifecycle of your keys, you can benefit from knowing that your keys are secured by FIPS 140-2 Level 3 certified cloud-based hardware security modules (HSMs) that protect against the theft of information.

[{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-overview) is a single-tenant, dedicated HSM that is controlled by you. The service is built on FIPS 140-2 Level 4-certified hardware, the highest offered by any cloud provider in the industry.

Refer to product documentation on [{{site.data.keyword.keymanagementservicefull}}](/docs/services/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/services/hs-crypto?topic=hs-crypto-overview) for a detailed overview of the two services.


## Rotating Keys
{: #encryption-rotate}

Key rotation is an important part of mitigating the risk of a data breach. Periodically changing keys reduces the potential data loss if the key is lost or compromised. The frequency of key rotations varies by organization and depends on a number of variables, such as the environment, the amount of encrypted data, classification of the data, and compliance laws. The [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){: external} provides definitions of appropriate key lengths and provides guidelines for how long keys should be used.

For more information, see the documentation for rotating keys in [Key Protect](/docs/key-protect?topic=key-protect-set-rotation-policy) or [{{site.data.keyword.hscrypto}}](/docs/key-protect?topic=key-protect-rotate-keys).

## Cryptographic erasure
{: encryption-cryptoerasure}

Cryptographic erasure (or crypto-shredding) is a method of rendering encrypted data  unreadable by [deleting the encryption keys](/docs/key-protect?topic=key-protect-security-and-compliance#data-deletion) rather than the data itself. When a [root key is deleted in Key Protect](/docs/key-protect?topic=key-protect-delete-keys), it will affect all objects in any buckets created using that root key, effectively "shredding" the data and preventing any further reading or writing to the buckets. This process is not instantaneous, but occurs within a few minutes after the key is deleted.

Although objects in a crypto-shredded bucket can not be read, and new object can not be written, existing objects will continue to consume storage until they are deleted by a user.
{: tip}

When a Key Protect root key is deleted and an associated object storage bucket is crypto-shredded, an [Activity Tracker management event](/docs/cloud-object-storage?topic=cloud-object-storage-at-events#at-actions-global) (`cloud-object-storage.bucket-key-state.update`) is generated in addition to the `kms.secrets.delete` event logged by Key Protect. In the event of a server-side failure to delete the key, that failure is not logged unless it does not succeed within four hours.

Rotating, suspending, or resuming (enabling) keys does not generate a bucket management event at this time.
{:note}

This event will not be generated for buckets created prior to February 26th, 2020 at this time.
{: important}
