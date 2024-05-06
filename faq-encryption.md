---

copyright:
  years: 2017, 2024
lastupdated: "2024-05-06"

keywords: faq, frequently asked questions, object storage, rclone, encryption, Cyberduck, key protect, FedRAMP, AES, SHA

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - Encryption
{: #faq-encryption}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## What types of authentication can I use to access {{site.data.keyword.cos_full}}?
{: #faq-authenticate}
{: faq}

You can use an OAuth 2 token or an HMAC key for authentication. The HMAC key can be used for S3-compatible tools such as `rclone`, `Cyberduck`, and others.

* For instructions to obtain an OAuth token, see [Generating an IBM Cloud IAM token by using an API key](/docs/account?topic=account-iamtoken_from_apikey).
* For instructions to obtain the HMAC credentials, see [Using HMAC Credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main).

Also, see [API Key vs HMAC](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac).

## Does {{site.data.keyword.cos_short}} provide encryption at rest and in motion?
{: #faq-encrypt-basics}
{: faq}

Yes. Data at rest is encrypted with automatic provider-side Advanced Encryption Standard (AES) 256-bit encryption and the Secure Hash Algorithm (SHA)-256 hash. Data in motion is secured by using the built-in carrier grade Transport Layer Security/Secure Sockets Layer (TLS/SSL) or SNMPv3 with AES encryption.

If you want more control over encryption, you can make use of IBM Key Protect to manage generated or "bring your own" keying. For details, see [Key-protect COS Integration](/docs/key-protect?topic=key-protect-integrate-cos).

## Is there additional encryption processing if a customer wants to encrypt their data?
{: #faq-encrypt-add}
{: faq}

Server-side encryption is always on for customer data. Compared to the hashing required in S3 authentication and the erasure coding, encryption is not a significant part of the processing cost of {{site.data.keyword.cos_short}}.

## Does {{site.data.keyword.cos_short}} encrypt all data?
{: #faq-encrypt-all}
{: faq}

Yes, {{site.data.keyword.cos_short}} encrypts all data.

## How do I encrypt my data?
{: #troubleshooting-cos-encryption}
{: faq}

1. Go to the {{site.data.keyword.cos_full_notm}} documentation for [managing encryption](/docs/cloud-object-storage?topic=cloud-object-storage-encryption) to research the encryption topic.
1. Choose between [{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) for your encryption needs.
1. Remember that customer-provided keys are enforced on objects.
1. Use [IBM Key Protect](/docs/key-protect?topic=key-protect-about) or [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) to create, add, and manage keys, which you can then associate with your instance of {{site.data.keyword.cos_full_notm}}.
1. Grant service authorization
     1. Open your IBM Cloud dashboard.
     1. From the menu bar, click **Manage > Access**.
     1. In the side navigation, click **Authorizations**.
     1. Click **Create authorization**.
     1. In the **Source service** menu, select **Cloud Object Storage**.
     1. In the **Source service instance** menu, select the service instance to authorize.
     1. In the **Target service** menu, select **IBM Key Protect** or **{{site.data.keyword.hscrypto}}**.
     1. In the **Target service instance** menu, select the service instance to authorize.
     1. Enable the **Reader** role.
     1. Click **Authorize**

## Does {{site.data.keyword.cos_short}} have FIPS 140-2 compliance for the encryption algorithms?
{: #faq-encrypt-fips}
{: faq}

Yes, the IBM COS Federal offering is approved for FedRAMP Moderate Security controls, which require a validated FIPS configuration. IBM COS Federal is certified at FIPS 140-2 level 1. For more information on COS Federal offering, [contact us](https://www.ibm.com/cloud/government) via our Federal site.

## Is client-key encryption supported?
{: #faq-encrypt-client}
{: faq}

Yes, client-key encryption is supported by using SSE-C, Key Protect, or HPCS.

## Is encryption applied to a bucket by default?
{: #faq-default-enc}
{: faq}
