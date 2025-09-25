---

copyright:
  years: 2025, 2025
lastupdated: "2025-09-22"

keywords: 403 error, access denied

subcollection: cloud-object-storage

content-type: troubleshoot

---

{{site.data.keyword.attribute-definition-list}}

# Why does an "access denied" error occur during object operations?
{: #troubleshoot-cos-403-error}
{: troubleshoot}

When you perform operations such as listing, reading, or writing objects, the result is an error 403 Access Denied.
{: shortdesc}

You receive the following error message when you perform operations on an {{site.data.keyword.cos_full_notm}} bucket:
{: tsSymptoms}

> Error: AccessDenied: Access Denied status code: 403

Or you might experience symptoms such as the following:

* Failure to access a bucket or objects
* AccessDenied errors during API or CLI operations
* Successful connection to the {{site.data.keyword.cos_short}} endpoint but operation denied

Access Denied errors that occur during bucket operations are often caused by missing or insufficient permissions or by network access restrictions.
{: tsCauses}

Use the following steps to troubleshoot and resolve the error and ensure that your access to {{site.data.keyword.cos_full_notm}} works as expected:
{: tsResolve}

1. Verify permissions

    1. Begin by identifying the service ID or user credentials used for the operation.
    1. Review the IAM policies assigned to the service ID or to the user. You can view assigned IAM policies in the {{site.data.keyword.cloud_notm}} console or by using the CLI.
    1. Confirm that the credentials have appropriate roles for the {{site.data.keyword.cos_short}} instance, such as `Writer` or `Manager`. Ensure that the roles are sufficient for the required operations, such as `Writer` or higher.
    1. If you enabled fine-grained access controls, verify that the credentials allow operations on the specific bucket or objects.
    1. Update or adjust the assigned policies as needed.

    For more information about Identity and Access Management (IAM) roles, see [Getting Started with IAM](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-with-iam).

1. Validate tokens and endpoints

    1. Check that you correctly generated the IAM token. Make sure that the token is valid for accessing {{site.data.keyword.cos_short}} resources.
    1. Confirm that the endpoint being accessed matches the token's scope, either private or public.
    1. If you operate within a private network, verify that you are using the private endpoints. An example of a private endpoint value is `https://s3.private.us-south.cloud-object-storage.appdomain.cloud/`.

1. Check network access controls

    Validate that network access policies, including context-based restrictions, allow the client to reach the necessary endpoints. To learn more, see [Restricting access by network context](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall).

    * If you use context-based restrictions (CBR) with {{site.data.keyword.cos_short}} buckets, ensure that the source IP address, VPC subnet, or other attributes are permitted.
    * Validate that firewall rules or VPC access groups allow outbound access to {{site.data.keyword.cos_short}} endpoints.

1. Perform manual validation to isolate the issue

    Manual validation can help you to isolate authorization issues from application-level misconfigurations. To perform a manual validation, use these steps:

    1. Obtain a new IAM token using a method appropriate for your task (for example, API Key or IAM authentication).
    1. [Using cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl) or another tool, manually send a request to the {{site.data.keyword.cos_short}} endpoint.
    1. Validate that the expected response is returned without access errors.

To minimize the occurrence of 403 Access Denied errors, consider a regular review of the following items:

* Review IAM policies and assigned roles: Perform periodic audits of IAM policies to ensure that users, service IDs, and API keys have only the necessary permissions required for their operations.
* Audit context-based restrictions (CBR): Regularly review CBR rules to verify that valid sources, such as trusted IP ranges or VPC subnets, continue to have access to {{site.data.keyword.cos_short}} endpoints because infrastructure changes over time.
* Monitor access logs and authorization events: Continuously monitor {{site.data.keyword.cos_short}} access logs and IAM authorization logs for denied requests, anomalies, or unexpected access attempts that could indicate configuration issues or security risks.
* Update security practices and service configurations: Stay informed about the latest {{site.data.keyword.cos_full_notm}} security enhancements and IAM best practices. See [Monitoring notifications and status](/docs/account?topic=account-viewing-cloud-status) for options. Apply recommended updates and improvements to ensure compliance and optimal access control.
