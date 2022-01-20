---

copyright:
  years: 2017, 2021
lastupdated: "2021-10-27"

keywords: authorization, iam, basics

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Getting Started with IAM
{: #iam}

Access to {{site.data.keyword.cos_full}} service instances for users in your account is controlled by {{site.data.keyword.cloud_notm}} Identity and Access Management (IAM).
{: shortdesc}

## Identity and Access Management roles
{: #iam-roles}

Every user that accesses the {{site.data.keyword.cos_full}} service in your account must be assigned an access policy with an IAM user role defined. That policy determines what actions the user can perform within the context of the service or instance you select. The allowable actions are customized and defined by the {{site.data.keyword.cloud_notm}} service as operations that are allowed to be performed on the service. The actions are then mapped to IAM user roles.

Policies enable access to be granted at different levels. Some of the options include the following: 

* Access across all instances of the service in your account
* Access to an individual service instance in your account
* Access to a specific bucket within an instance (see [Bucket permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions))
* Access to all IAM-enabled services in your account

After you define the scope of the access policy, you assign a role. Review the following tables which outline what actions each role allows within the {{site.data.keyword.cos_short}} service.

The following table details actions that are mapped to platform management roles. Platform management roles enable users to perform tasks on service resources at the platform level, for example assign user access for the service, create or delete service IDs, create instances, and bind instances to applications.

| Platform management role | Description of actions                                                                                                             | Example actions                                                                                                         |
| :----------------------- | :--------------------------------------------------------------------------------------------------------------------------------- | :---------------------------------------------------------------------------------------------------------------------- |
| Viewer                   | View service instances but not modify them                                                                                         | <ul><li>List available COS service instances</li><li>View COS service plan details</li><li>View usage details</li></ul> |
| Editor                   | Perform all platform actions except for managing the accounts and assigning access policies                                        | <ul><li>Create and delete COS service instances</li></ul>                                                               |
| Operator                 | Not used by COS                                                                                                                    | None                                                                                                                    |
| Administrator            | Perform all platform actions based on the resource this role is being assigned, including assigning access policies to other users | <ul><li>Update user policies</li>Update pricing plans</ul>                                                              |
{: caption="Table 1. IAM user roles and actions"}


The following table details actions that are mapped to service access roles. Service access roles enable users access to {{site.data.keyword.cos_short}} as well as the ability to call the {{site.data.keyword.cos_short}} API.

| Service access role | Description of actions                                                                                                | Example actions                                                                                             |
| :------------------ | :-------------------------------------------------------------------------------------------------------------------- | :---------------------------------------------------------------------------------------------------------- |
| Object Writer       | Upload and overwrite objects (including uploading objects in multiple parts).                                         | <ul><li>Upload objects</li></ul>                                                                            |
| Object Reader       | Download objects, read object metadata (headers), but not list objects or buckets.                                    | <ul><li>Download objects</li></ul>                                                                          |
| Content Reader      | Download and list objects, read object metadata (headers), but not list buckets.                                      | <ul><li>Download and list objects</li></ul>                                                                 |
| Reader              | In addition to Content Reader actions, Readers can list buckets and read bucket metadata, but not make modifications. | <ul><li>List buckets</li></ul>                                                                              |
| Writer              | In addition to Reader actions, Writers can create buckets and upload objects.                                         | <ul><li>Create new buckets and objects</li><li>Remove buckets and objects</li></ul>                         |
| Manager             | In addition to Writer actions, Managers can complete privileged actions that affect access control.                   | <ul><li>Configure retention policies</li><li>Configure bucket firewalls</li><li>Block public ACLs</li></ul> |
{: caption="Table 3. IAM service access roles and actions"}


For information about assigning user roles in the UI, see [Managing IAM access](/docs/account?topic=account-assign-access-resources).
 
## Identity and Access Management actions
{: #iam-actions}

| Action                                                           | Description                                                                         |
| ---------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| `cloud-object-storage.account.get_account_buckets`               | List all buckets in a service instance.                                             |
| `cloud-object-storage.bucket.put_bucket`                         | Create a bucket.                                                                    |
| `cloud-object-storage.bucket.post_bucket`                        | Internal use only - unsupported for users.                                          |
| `cloud-object-storage.bucket.delete_bucket`                      | Delete a bucket.                                                                    |
| `cloud-object-storage.bucket.get`                                | List all the objects in a bucket.                                                   |
| `cloud-object-storage.bucket.list_crk_id`                        | List the IDs of encryption root keys associated with a bucket.                      |
| `cloud-object-storage.bucket.head`                               | View bucket metadata.                                                               |
| `cloud-object-storage.bucket.get_versions`                       | List object versions.                                                               |
| `cloud-object-storage.bucket.get_uploads`                        | List all active multipart uploads for a bucket.                                     |
| `cloud-object-storage.bucket.put_quota`                          | Unsupported operation - used for S3 API compatibility only.                         |
| `cloud-object-storage.bucket.get_acl`                            | Read a bucket ACL [deprecated].                                                     |
| `cloud-object-storage.bucket.put_acl`                            | Create a bucket ACL [deprecated].                                                   |
| `cloud-object-storage.bucket.get_cors`                           | Read CORS rules.                                                                    |
| `cloud-object-storage.bucket.put_cors`                           | Add CORS rules to a bucket.                                                         |
| `cloud-object-storage.bucket.delete_cors`                        | Delete CORS rules.                                                                  |
| `cloud-object-storage.bucket.get_versioning`                     | Check versioning status of a bucket.                                                |
| `cloud-object-storage.bucket.put_versioning`                     | Enable versioning on a bucket.                                                      |
| `cloud-object-storage.bucket.get_fasp_connection_info`           | View Aspera FASP connection information.                                            |
| `cloud-object-storage.account.delete_fasp_connection_info`       | Delete Aspera FASP connection information.                                          |
| `cloud-object-storage.bucket.get_location`                       | View the location and storage class of a bucket.                                    |
| `cloud-object-storage.bucket.get_lifecycle`                      | Read a bucket lifecycle policy.                                                     |
| `cloud-object-storage.bucket.put_lifecycle`                      | Create a bucket lifecycle policy.                                                   |
| `cloud-object-storage.bucket.get_basic`                          | Read bucket metadata (number of objects, etc) using the Resource Configuration API. |
| `cloud-object-storage.bucket.get_activity_tracking`              | Read activity tracking configuration.                                               |
| `cloud-object-storage.bucket.put_activity_tracking`              | Add activity tracking configuration.                                                |
| `cloud-object-storage.bucket.get_metrics_monitoring`             | Read metrics monitoring configuration.                                              |
| `cloud-object-storage.bucket.put_metrics_monitoring`             | Add metrics monitoring configuration.                                               |
| `cloud-object-storage.bucket.put_protection`                     | Add Immutable Object Storage policy.                                                |
| `cloud-object-storage.bucket.get_protection`                     | Read Immutable Object Storage policy.                                               |
| `cloud-object-storage.bucket.put_firewall`                       | Add a firewall configuration.                                                       |
| `cloud-object-storage.bucket.get_firewall`                       | Read a firewall configuration.                                                      |
| `cloud-object-storage.bucket.list_bucket_crn`                    | View a bucket CRN.                                                                  |
| `cloud-object-storage.bucket.get_notifications`                  | Internal use only - unsupported for users.                                          |
| `cloud-object-storage.bucket.put_notifications`                  | Internal use only - unsupported for users.                                          |
| `cloud-object-storage.object.get`                                | View and download objects.                                                          |
| `cloud-object-storage.object.head`                               | Read an object's metadata.                                                          |
| `cloud-object-storage.object.get_version`                        | Read a specified version of an object.                                              |
| `cloud-object-storage.object.head_version`                       | Get headers for a specific version of an object.                                    |
| `cloud-object-storage.object.put`                                | Write and upload objects.                                                           |
| `cloud-object-storage.object.post`                               | Upload an object using HTML forms [deprecated].                                     |
| `cloud-object-storage.object.post_md`                            | Update object metadata using HTML forms [deprecated].                               |
| `cloud-object-storage.object.post_initiate_upload`               | Initiate multipart uploads.                                                         |
| `cloud-object-storage.object.put_part`                           | Upload an object part.                                                              |
| `cloud-object-storage.object.copy_part`                          | Copy (write) an object part.                                                        |
| `cloud-object-storage.object.copy_part_get`                      | Copy (read) an object part.                                                         |
| `cloud-object-storage.object.post_complete_upload`               | Complete a multipart upload.                                                        |
| `cloud-object-storage.object.copy`                               | Copy (write) an object from one bucket to another.                                  |
| `cloud-object-storage.object.copy_get`                           | Copy (read) an object from one bucket to another.                                   |
| `cloud-object-storage.object.get_acl`                            | Read object ACL [deprecated].                                                       |
| `cloud-object-storage.object.put_acl`                            | Write object ACL [deprecated].                                                      |
| `cloud-object-storage.object.put_acl_version`                    | Unsupported operation - used for S3 API compatibility only.                         |
| `cloud-object-storage.object.delete`                             | Delete an object.                                                                   |
| `cloud-object-storage.object.delete_version`                     | Delete a specific version of an object.                                             |
| `cloud-object-storage.object.get_uploads`                        | List parts of an object.                                                            |
| `cloud-object-storage.object.delete_upload`                      | Abort a multipart upload.                                                           |
| `cloud-object-storage.object.restore`                            | Temporarily restore an archived object.                                             |
| `cloud-object-storage.object.post_multi_delete`                  | Delete multiple objects.                                                            |
| `cloud-object-storage.object.post_legal_hold`                    | Add a legal hold to an object.                                                      |
| `cloud-object-storage.object.get_legal_hold`                     | View any legal holds on an object.                                                  |
| `cloud-object-storage.object.post_extend_retention`              | Extend a retention policy.                                                          |
| `cloud-object-storage.provide_ibm_client_originating_ip_address` | Internal use only - unsupported for users.                                          |
| `cloud-object-storage.bucket.put_public_access_block`            | Add a public ACL block configuration                                                |
| `cloud-object-storage.bucket.get_public_access_block`            | Read a public ACL block configuration                                               |
| `cloud-object-storage.bucket.delete_public_access_block`         | Delete a public ACL block configuration                                             |
{: caption="Table 4. Granular IAM action descriptions"}
