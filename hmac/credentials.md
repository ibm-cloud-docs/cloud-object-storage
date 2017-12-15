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

# Using HMAC credentials

The {{site.data.keyword.cos_full}} API is a REST-based API for reading and writing objects. It uses {{site.data.keyword.iamlong}} for authentication/authorization, and supports a subset of the S3 API for easy migration of applications to {{site.data.keyword.cloud_notm}}.

In addition to IAM token-based authentication, it is also possible to [authenticate using a signature](/docs/services/cloud-object-storage/hmac/hmac-signature.html) created from a pair of access and secret keys. This is functionally identical to the AWS Signature Version 4, and HMAC keys provided by IBM COS should work with the majority of S3-compatible libraries and tools.

Users can create a set of HMAC credentials when creating a [Service Credential](/docs/services/cloud-object-storage/iam/service-credentials.html) by providing a configuration parameter during credential creation. After the Service Credential is created, the HMAC Key is included in the `cos_hmac_keys` field. These HMAC keys are then associated with a [Service ID](/docs/services/cloud-object-storage/iam/users-serviceids.html) and can be used to access any resources or operations allowed by the Service ID's role. 

Note that when using HMAC credentials to create signatures to use with direct [REST API](/docs/services/cloud-object-storage/api-reference/about-compatibility-api.html) calls that additional headers are required:
1. All requests must have an `x-amz-date` header with the date in `%Y%m%dT%H%M%SZ` format.
2. Any request that has a payload (object uploads, deleting multiple objects, etc.) must provide a `x-amz-content-sha256` header with a SHA256 hash of the payload contents.
3. ACLs (other than `public-read`) are unsupported.

Not all S3-compatible tools are currently unsupported. Some tools attempt to set ACLs other than `public-read` on bucket creation. Bucket creation through these tools will fail. If a `PUT bucket` request fails with an unsupported ACL error, first use the [Console](/docs/services/cloud-object-storage/getting-started.html#getting-started-console-) to create the bucket, then configure the tool to read and write objects to that bucket. Tools that set ACLs on object writes are not currently supported.
{:tip}
