---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-19"

keywords: sdks, overview

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}

# About IBM COS SDKs
{: #sdk-about}

IBM COS provides SDKs for Java, Python, NodeJS, and Go. These SDKs are based on the official AWS S3 API SDKs, but have been modified to use IBM Cloud features like IAM, Key Protect, Immutable Object Storage, and others.

| Feature                                             | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |
|-----------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| [IAM API key support](#sdk-about-iam)               | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| [Managed multipart uploads](#sdk-about-tmup)        | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| [Managed multipart downloads](#sdk-about-tmdown)    | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| [Extended bucket listing](#sdk-about-extended-list) |                                                   |                                                   |                                                   |                                                   |                                                   |
| [Version 2 object listing](#sdk-about-v2-list)      | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| [Key Protect](#sdk-about-kp)                        | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| [SSE-C](#sdk-about-sse-c)                           | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| [Archive rules](#sdk-about-archive)                 | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| [Retention policies](#sdk-about-retention)          | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| [Aspera high-speed transfer](#sdk-about-aspera)     | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |
## IAM API key support
{: #sdk-about-iam}
Allows for creating clients with an [API key](/docs/iam?topic=iam-iamoverview#iamoverview) instead of an [pair of Access/Secret]() keys.  
Token management is handled automatically, and tokens are automatically refreshed during long-running operations.

## Managed multipart uploads
{: #sdk-about-tmup}
Using a `TransferManager` class, the SDK will handle all the necessary logic for uploading objects in multiple parts.

## Managed multipart downloads
{: #sdk-about-tmdown}
Using a `TransferManager` class, the SDK will handle all the necessary logic for downloading objects in multiple parts.

## Extended bucket listing
{: #sdk-about-extended-list}
This extension to the S3 API returns a list of buckets with provisioning codes 
(a combination of the bucket's location and storage class, returned as `LocationConstraint`) for buckets when listing.  
This is useful for finding a bucket, as all buckets in a service instance are listed regardless of the specific endpoint used.

## Version 2 object listing
{: #sdk-about-v2-list}
Version 2 listing allows for more powerful scoping of object listings.

## Key Protect
{: #sdk-about-kp}
[{{site.data.keyword.keymanagementservicefull}}](https://test.cloud.ibm.com/docs/services/key-protect?topic=key-protect-about) helps you provision encrypted keys for apps across {site.data.keyword.cloud}} services. As you manage the lifecycle of your keys, you can benefit from knowing that your keys are secured by FIPS 140-2 Level 3 certified cloud-based hardware security modules (HSMs) that protect against the theft of information. [{{site.data.keyword.hscrypto}}](https://test.cloud.ibm.com/docs/services/hs-crypto?topic=hs-crypto-overview) is a single-tenant, dedicated HSM that is controlled by you. The service is built on FIPS 140-2 Level 4-certified hardware, the highest offered by any cloud provider in the industry.

## SSE-C
{: #sdk-about-sse-c}  
{{site.data.keyword.cos_full}} provides several options to encrypt your data. By default, all objects that are stored in {{site.data.keyword.cos_full_notm}} are encrypted by using randomly generated keys and an all-or-nothing-transform (AONT). While this default encryption model provides at-rest security, some workloads need full control over the data encryption keys used. You can manage your keys manually by providing your own encryption keys - referred to as Server-Side Encryption with Customer-Provided Keys (SSE-C).

## Archive rules
{: #sdk-about-archive}  
{{site.data.keyword.cos_full}} Archive is a low-cost option for data that is rarely accessed. You can store data by transitioning from any of the storage tiers (Standard, Vault, Cold Vault and Flex) to a long-term offline archive.

## Retention policies
{: #sdk-about-retention}  
Immutable Object Storage allows client(s) to preserve electronic records and maintain data integrity in a WORM (Write-Once-Read-Many), non-erasable and non-rewritable manner until the end of their retention period and the removal of any legal holds.

## Aspera high-speed transfer 
{: #sdk-about-aspera}  
Aspera high-speed transfer overcomes the limitations of traditional FTP and HTTP transfers to improve data transfer performance under most conditions, especially in networks experiencing high latency and packet loss. Instead of the standard HTTP `PUT`, Aspera high-speed transfer uploads the object using the [FASP protocol](https://asperasoft.com/technology/transport/fasp/).