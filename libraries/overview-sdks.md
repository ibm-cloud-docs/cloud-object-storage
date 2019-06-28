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

| Feature                     | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| IAM API key support         | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| Managed multipart uploads   | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| Managed multipart downloads | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Extended bucket listing     |                                                   |                                                   |                                                   |                                                   |                                                   |
| Version 2 object listing    | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Key Protect                 | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |
| SSE-C                       | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Archive rules               | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Retention policies          | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Aspera high-speed transfer  | ![Checkmark icon](../../icons/checkmark-icon.svg) | ![Checkmark icon](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## IAM API key support
{: #sdk-about-iam}
Allows for creating clients with an API key instead of an pair of Access/Secret keys.  Token management is handled automatically, and tokens are automatically refreshed during long-running operations.
## Managed multipart uploads
Using a `TransferManager` class, the SDK will handle all the necessary logic for uploading objects in multiple parts.
## Managed multipart downloads
Using a `TransferManager` class, the SDK will handle all the necessary logic for downloading objects in multiple parts.
## Extended bucket listing
This is an extension to the S3 API that Returns a list of buckets with provisioning codes (a combination of the bucket's location and storage class, returned as `LocationConstraint`) for buckets when listing.  This is useful for finding a bucket, as the buckets in a service instance are all listed regardless of the endpoint used.
## Version 2 object listing
Version 2 listing allows for more powerful scoping of object listings.
## Key Protect
Key Protect is an IBM Cloud service that manages encryption keys, and is an optional parameter during bucket creation.
## SSE-C                      
## Archive rules              
## Retention policies         
## Aspera high-speed transfer 