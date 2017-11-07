---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Info for existing customers using {{site.data.keyword.cos_full_notm}} (IaaS version)

Core documentation for the IaaS version of {{site.data.keyword.cos_full_notm}} (S3 API) [can be found here](https://ibm-public-cos.github.io/crs-docs/index.html).

## What is the new {{site.data.keyword.cos_full_notm}}?
It's not really a _new_ version of {{site.data.keyword.cos_short}}, but rather a different provisioning path.  Until now, the only way to provision an instance of {{site.data.keyword.cos_short}} was as through the {{site.data.keyword.cloud}} infrastructure (SoftLayer) control portal.  Now, it is also available through the {{site.data.keyword.cloud_notm}} Platform.

### What are the differences between the services?
The key differences are:

Feature        | {{site.data.keyword.cos_short}} (S3 API) | {{site.data.keyword.cos_short}} ({{site.data.keyword.cloud_notm}} Platform) |
---------------|-------------------|---------------|
Authentication | AWS V4 Signatures | IAM Oauth2    |
API            | S3 API            | COS API       |
Access control | Coarse            | Granular      |

#### Authentication
The new service uses {{site.data.keyword.cloud_notm}} Identity and Access Management for access control.  Users present an API key in exchange for a token using the IAM API, and this token is sent in the `Authorization` header instead of a V4 Signature. IaaS users can find [detailed information on access management here](https://ibm-public-cos.github.io/crs-docs/manage-access).

#### Data API
Instances of {{site.data.keyword.cos_short}} that are provisioned as IaaS are compatible with most libraries and tools that use the S3 API. New instances provisioned through the {{site.data.keyword.cloud_notm}} Platform do not make use of Access Keys or Secret Keys for authentication, and are _not_ compatible with libraries and tools designed to use the S3 API.

{{site.data.keyword.cos_short}} offers IAM-enabled SDKs for:
  - [Java](https://github.com/IBM/ibm-cos-sdk-java)
  - [Node.js](https://github.com/IBM/ibm-cos-sdk-js)
  - [Python](https://github.com/IBM/ibm-cos-sdk-python)

These are forked from the original S3 libraries, and are intended to serve as drop-in replacements (with minor tweaks) for data access within an application.

### Should I transistion?  How do I do that?

If what you have now is working for you, or if you need to make use of tools, libraries, or gateways that use the S3 API, then continue to use the IaaS {{site.data.keyword.cos_short}} service.

If you need granular access control, such as the ability to restrict access of a single bucket or to grant access to an individual user or application, then the new IAM-enabled {{site.data.keyword.cos_short}} is a good choice.
