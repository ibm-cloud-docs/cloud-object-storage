---

copyright:
  years: 2018, 2020
lastupdated: "2020-06-19"

keywords: encryption, security, sse-c, key protect, {{site.data.keyword.hscrypto}}

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

{:help: data-hd-content-type='help'}

# Encrypting your data
{: #encryption}

{{site.data.keyword.cos_full}} provides several options to encrypt your data. 
{: shortdesc}

By default, all objects that are stored in {{site.data.keyword.cos_full_notm}} are encrypted by using randomly generated keys and an all-or-nothing-transform (AONT). While this default encryption model provides at-rest security, some workloads need full control over the data encryption keys used. You can manage your keys manually on a per-object basis by providing your own encryption keys - referred to as [Server-Side Encryption with Customer-Provided Keys (SSE-C)](/docs/cloud-object-storage?topic=cloud-object-storage-sse-c).

With {{site.data.keyword.cos_short}} you also have a choice to use our integration capabilities with {{site.data.keyword.cloud}} Key Management Services like {{site.data.keyword.keymanagementservicelong}} and {{site.data.keyword.hscrypto}}. Depending on the security requirements, you can decide whether to use IBM Key Protect or IBM {{site.data.keyword.hscrypto}} for your IBM Cloud Object Storage buckets.

[{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) helps you provision encrypted keys for apps across {{site.data.keyword.cloud}} services. As you manage the lifecycle of your keys, you can benefit from knowing that your keys are secured by FIPS 140-2 Level 3 certified cloud-based hardware security modules (HSMs) that protect against the theft of information.

[{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) is a single-tenant, dedicated HSM that is controlled by you. The service is built on FIPS 140-2 Level 4-certified hardware, the highest offered by any cloud provider in the industry.

Refer to product documentation on [{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) for a detailed overview of the two services.
