---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Info for existing customers using COS (IaaS version)

Core documentation for the IaaS version of COS (S3 API) [can be found here](https://ibm-public-cos.github.io/crs-docs/index.html).

## What is the new COS?
It's not really a _new_ version of COS, but rather a different provisioning path.  Until now, the only way to provision an instance of COS was as through the Bluemix Infrastructure (SoftLayer) control portal.  Now, it is also available through the IBM Cloud Platform (Bluemix).

### What are the differences between the services?
The key differences are:

Feature        | COS (S3 API)      | COS (Bluemix) |
---------------|-------------------|---------------|
Authentication | AWS V4 Signatures | IAM Oauth2    |
API            | S3 API            | COS API       |
Access control | Coarse            | Granular      |

#### Authentication
The new service uses IBM Cloud Identity and Access Management for access control.  Users present an API key in exchange for a token using the IAM API, and this token is sent in the `Authorization` header instead of a V4 Signature. IaaS users can find [detailed information on access management here](https://ibm-public-cos.github.io/crs-docs/manage-access).

#### Data API
Instances of COS that are provisioned as IaaS are compatible with most libraries and tools that use the S3 API. New instances provisioned through Bluemix do not make use of Access Keys or Secret Keys for authentication, and are _not_ compatible with libraries and tools designed to use the S3 API.

IBM COS offers IAM-enabled SDKs for:
  - [Java](https://github.com/IBM/ibm-cos-sdk-java)
  - [Node.js](https://github.com/IBM/ibm-cos-sdk-js)
  - [Python](https://github.com/IBM/ibm-cos-sdk-python)

These are forked from the original S3 libraries, and are intended to serve as drop-in replacements (with minor tweaks) for data access within an application.

### Should I transistion?  How do I do that?

If what you have now is working for you, or if you need to make use of tools, libraries, or gateways that use the S3 API, then continue to use the IaaS COS service.

If you need granular access control, such as the ability to restrict access of a single bucket or to grant access to an individual user or application, then the new IAM-enabled COS is a good choice.
