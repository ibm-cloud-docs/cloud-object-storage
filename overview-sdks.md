---

copyright:
  years: 2017, 2024
lastupdated: "2024-02-20"

keywords: object storage, sdk, overview

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
{:http: .ph data-hd-programlang='http'}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# About IBM COS SDKs
{: #sdk-about}

{{site.data.keyword.cos_full}} provides SDKs for Java, Python, NodeJS, and Go featuring capabilities to make the most of {{site.data.keyword.cos_full_notm}}.
{: shortdesc}

These SDKs are based on the official AWS S3 API SDKs, but are modified to use IBM Cloud features like IAM, Key Protect, Immutable Object Storage, and others.

| Feature                                             | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |  Terraform                                              |
|-----------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| [IAM API key support](#sdk-about-iam)               | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |![Checkmark icon](../../icons/checkmark-icon.svg)
| [Managed multipart uploads](#sdk-about-tmup)        | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |  |
| [Managed multipart downloads](#sdk-about-tmdown)    | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| [Extended bucket listing](#sdk-about-extended-list) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| [Version 2 object listing](#sdk-about-v2-list)      | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                  |
| [Key Protect](#sdk-about-kp)                        | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |  | ![Checkmark icon](../../icons/checkmark-icon.svg)
| [SSE-C](#sdk-about-sse-c)                           | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| [Archive rules](#sdk-about-archive)                 | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |     | ![Checkmark icon](../../icons/checkmark-icon.svg)
| [Retention policies](#sdk-about-retention)          | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |        | ![Checkmark icon](../../icons/checkmark-icon.svg)
| [Aspera high-speed transfer](#sdk-about-aspera)     | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                      &nbsp;                        |                             &nbsp;                      |                     &nbsp;                        |
{: caption="Features supported per SDK"}

## IAM API key support
{: #sdk-about-iam}

Allows for creating clients with an [API key](/docs/account?topic=account-iamoverview#iamoverview) instead of a [pair of Access and Secret](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) keys.
Token management is handled automatically, and tokens are automatically refreshed during long-running operations.

## Managed multipart uploads
{: #sdk-about-tmup}

Using a `TransferManager` class, the SDK handles all the necessary logic for uploading objects in parallel parts.

## Managed multipart downloads
{: #sdk-about-tmdown}

Using a `TransferManager` class, the SDK handles all the necessary logic for downloading objects in parallel parts.

## Extended bucket listing
{: #sdk-about-extended-list}

This extension to the S3 API returns a list of buckets with their `LocationConstraint`.
All buckets in a service instance are always returned on a list request, not just the subset that is located in the region of the target endpoint. This API is useful for finding where a bucket is located.

## Version 2 object listing
{: #sdk-about-v2-list}

Version 2 listing allows for more powerful scoping of object listings.

## Key Protect
{: #sdk-about-kp}

[{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) helps you create encrypted keys for apps across {{site.data.keyword.cloud}} services. Keys are secured by FIPS 140-2 Level 3 cloud-based hardware security modules (HSMs) that protect against the theft of information. [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) is a single-tenant, dedicated HSM that is controlled by you. The service is built on FIPS 140-2 Level 4 hardware, the highest offered by any cloud provider in the industry.

## SSE-C
{: #sdk-about-sse-c}

{{site.data.keyword.cos_full}} provides several options to encrypt your data. By default, all objects that are stored in {{site.data.keyword.cos_full_notm}} are encrypted by using randomly generated keys and an all-or-nothing-transform (AONT). While this default encryption model provides at-rest security, some workloads need full control over the data encryption keys used. You can manage your keys manually by supplying your own encryption keys - referred to as Server-Side Encryption with Customer-Provided Keys (SSE-C).

## Archive rules
{: #sdk-about-archive}

{{site.data.keyword.cos_full}} Archive is a low-cost option for data that is rarely accessed. You can migrate data from any of the storage tiers (Standard, Vault, Cold Vault, and Flex) to a long-term offline archive.

## Retention policies
{: #sdk-about-retention}

Immutable Object Storage maintains data integrity in a WORM (Write-Once-Read-Many) manner. Objects can't be modified until the end of their retention period and the removal of any legal holds.

## Aspera high-speed transfer
{: #sdk-about-aspera}

Aspera high-speed transfer improves data transfer performance under most conditions, especially in networks with high latency or packet loss. Instead of the standard HTTP `PUT`, Aspera high-speed transfer uploads the object by using the [FASP protocol](https://www.ibm.com/products/aspera){: external}.
