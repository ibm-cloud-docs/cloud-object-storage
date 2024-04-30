---
copyright:
  years: 2020, 2024
lastupdated: "2024-04-19"

keywords: security and compliance for cloud-object-storage, security for cloud-object-storage, compliance for cloud-object-storage

subcollection: cloud-object-storage
---

{{site.data.keyword.attribute-definition-list}}

# Managing security and compliance with {{site.data.keyword.cos_full_notm}}
{: #manage-security-compliance}

{{site.data.keyword.cos_full_notm}} is integrated with the [{{site.data.keyword.compliance_short}}](/docs/security-compliance) to help you manage security and compliance for your organization.
{: shortdesc}

With the {{site.data.keyword.compliance_short}}, you can:

* Monitor for controls and goals that pertain to {{site.data.keyword.cos_short}}.
* Define [rules](#x2037526){: term} for {{site.data.keyword.cos_short}} that can help to standardize resource configuration.

This service supports the ability to view  the results of your configuration scans in the Security and Compliance Center. It is not necessary to set up a collector to use configuration rules.
{: note}

## Monitoring security and compliance posture with {{site.data.keyword.cos_short}}
{: #monitor-cloud-object-storage}

As a security or compliance focal, you can use the {{site.data.keyword.cos_short}} goals to help ensure that your organization is adhering to the external and internal standards for your industry. By using the {{site.data.keyword.compliance_short}} to validate the resource configurations in your account against a [profile](#x2034950){: term}, you can identify potential issues as they arise or even prevent actions that would create a violation of your security policy.

All of the goals for {{site.data.keyword.cos_short}} are added to the {{site.data.keyword.cloud_notm}} Control Library but can also be mapped to other profiles.
{: note}

To start monitoring your resources, check out [Getting started](/docs/security-compliance) for {{site.data.keyword.compliance_short}}.

### Available goals for {{site.data.keyword.cos_short}}
{: #cloud-object-storage-available-goals}

* Check whether Cloud Object Storage bucket resiliency is set to cross region
    _This ensures that buckets are created in designated locations, resiliency levels, and storage classes._
* Check whether Cloud Object Storage buckets are enabled with IBM Cloud Monitoring
    _This ensures buckets are associated with specific instances of IBM Cloud Monitoring, as well as whether usage and/or request metrics are enabled._
* Check whether Cloud Object Storage buckets are enabled with IBM Activity Tracker
    _This ensures buckets are associated with specific instances of IBM Activity Tracker, as well as whether read and/or write events are enabled._
* Check whether Cloud Object Storage public access is disabled in IAM settings (not applicable to ACLs managed using S3 APIs)
    _This ensures IAM policies to allow unauthenticated access to a bucket are disabled._
* Check whether Cloud Object Storage is enabled with customer-managed encryption and Keep Your Own Key (KYOK)
    _This ensures buckets are associated with specific encryption root keys in Key Protect or HPCS._
* Check whether Cloud Object Storage network access is restricted to a specific IP range
    _This ensures buckets are only accessible from designated IP addresses, or that designated IP addresses are blocked._
* Check whether Cloud Object Storage bucket access is restricted by using IAM and S3 access control
    _This ensures buckets are prevented from using legacy S3 Access Control Lists (ACLs) to allow unauthenticated access._
* Check whether Cloud Object Storage is accessible only by using private endpoints
    _This ensures requests can be restricted to any combination of public, private, or direct networks._
* Check whether Cloud Object Storage is accessible only through HTTPS
    _All IAM token-based requests are required to use HTTPS._
* Check whether Cloud Object Storage is enabled with encryption
    _All data stored in Cloud Object Storage is encrypted by default._


## Governing {{site.data.keyword.cos_short}} resource configuration
{: #govern-cloud-object-storage}

As a security or compliance focal, you can use the {{site.data.keyword.compliance_short}} to [define configuration rules](/docs/security-compliance?topic=security-compliance-rules-define) for the instances of {{site.data.keyword.cos_short}} that you create.

Create rules to ensure that Cloud Object Storage buckets:
* are only created in designated locations
* are only created in a certain storage class
* are created with a designated managed encryption root key CRN
* are prevented from setting public ACLs
* are accessible by using private or direct endpoints only
* are only accessible from designated IP addresses
* can not be accessed from designated IP addresses
* use a designated instance of Activity Tracker
* send object read and/or write events to Activity Tracker
* don't grow past a defined maximum size

[Configuration rules](x3084914){: term} are used to monitor configuration standards that you want to implement across your accounts.

| Resource kind | Property                                               | Operator type | Description                                                                                                                                                                                                      |
|---------------|--------------------------------------------------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| *bucket*      | *location*                                             | string        | Cloud Object Storage bucket location.                                                                                                                              |
| *bucket*      | `storage_class`                                        | string        | Cloud Object Storage bucket storage class. This field is case-sensitive. The valid values are `standard`, `smart`, `vault`, or `cold`.  |
| *bucket*      | `ibm_sse_kms_customer_root_key_crn`                    | string        | SSE Key Protect or Hyper Protect Crypto Services Customer Root Key CRN associated with a Cloud Object Storage bucket. This maps to the bucket configuration parameter, `ibm-sse-kp-customer-root-key-crn`.                                              |
| *bucket*      | `public_access_block_configuration.block_public_acls`  | boolean       | Setting to prevent future configuration of ACLs that permit public access on the Cloud Object Storage bucket and its objects. Prior public access configuration for the bucket and its objects is unchanged.                          |
| *bucket*      | `public_access_block_configuration.ignore_public_acls` | boolean       | Setting to ignore configuration of public ACLs on the Cloud Object Storage bucket and its objects, rendering effective access as private. `GET Bucket ACL` and `GET Object ACL` return effective (enforced) permissions for the resource. |
| *bucket*      | `firewall.allowed_network_type`                        | string list   | List of network endpoint types (`public`, `private`, or `direct`) that are allowed.                                                                                           |
| *bucket*      | `firewall.allowed_ip`                                  | IP list       | List of allowed originating (source) IP addresses/ranges. The list can contain up to 1000 IPv4 or IPv6 addresses/ranges in CIDR notation.                                                                                 |
| *bucket*      | `firewall.denied_ip`                                   | IP list       | List of originating (source) IP addresses/ranges that are not permitted. The list can contain up to 1000 IPv4 or IPv6 addresses/ranges in CIDR notation.   |
| *bucket*      | `activity_tracking.activity_tracker_crn`               | string        | CRN of the Activity Tracker instance that receives both management (bucket-level) and data (object-level) events. Management events are sent automatically, but data events are opt-in.                                                                              |
| *bucket*      | `activity_tracking.write_data_events`                  | boolean       | If set to true, the Cloud Object Storage bucket's object write data events (that is, uploads) will be sent to the activity tracking service.                                                                                                 |
| *bucket*      | `activity_tracking.read_data_events`                   | boolean       | If set to true, the Cloud Object Storage bucket's object read events (that is. downloads) will be sent to the activity tracking service.                                                                                                     |
| *bucket*      | `metrics_monitoring.metrics_monitoring_crn`            | string        | CRN of the IBM Cloud Monitoring instance that receives the bucket metrics.                                                                              |
| *bucket*      | `metrics_monitoring.usage_metrics_enabled`             | boolean       | If set to true, the Cloud Object Storage bucket's usage metrics will be sent to the monitoring service.                                                                                                 |
| *bucket*      | `metrics_monitoring.request_metrics_enabled`           | boolean       | If set to true, the Cloud Object Storage bucket's request metrics will be sent to the monitoring service.                                                                                                 |
| *bucket*      | `hard_quota`                                           | numeric       | Maximum bytes allotted to the Cloud Object Storage bucket.                                                                                                     |
{: caption="Table 1. Rule properties for {{site.data.keyword.cos_short}}" caption-side="top"}

Although the operator types may indicate the values are numeric or boolean, all of the values in the rule are entered as strings or lists of strings, such as `'true'` or `'324342'`.  Additionally, even though an operator type might indicate a string, it is still possible to set an array of possible strings that comply with the rule.  For more information, see [What are the supported operators?](/docs/security-compliance?topic=security-compliance-rules-define&interface=ui#operators)
{: note}

After the rules are created and added to scopes, you can view the evaluation results in the {{site.data.keyword.compliance_short}}. Each rule is shown to be compliant or non-compliant - if a rule shows as being non-compliant then you can view the specific bucket that is in violation of the rule.

The evaluation results are only available for a limited period.  It is recommended that reports are downloaded and organized to maintain a history of compliance for audit purposes. For more information on reporting results, see [Viewing evaluation results](/docs/security-compliance?topic=security-compliance-results).
{: note}

For example, let's assume you want to evaluate a set of requirements on new buckets:

1. Only buckets in the `us-south` region are subject to the rule (other locations aren't subject to the rule).
2. Buckets must use the Smart Tier storage class.
3. Buckets must use Key Protect for managing encryption.
4. Public ACLs will be ignored (not blocked).
5. A firewall must be in place to only allow requests from inside the IBM Cloud.
6. Only the IP addresses in the range `fe80:021b::0/64` will be allowed to make requests.
7. Activity tracking must be enabled for both read and write requests.
8. IBM Cloud Monitoring must be enabled for both usage and requests.
9. The bucket cannot be allowed to grow past 10 TiB (10995116277760 bytes).

For step-by-step instructions using the UI and API, see [Defining custom rules](/docs/security-compliance?topic=security-compliance-rules-define).
{: tip}

The rule would look like the following:

```json
{
 "target": {
   "service_name": "cloud-object-storage",
   "resource_kind": "bucket",
   "additional_target_attributes": [
     {
       "name": "location",
       "operator": "string_equals",
       "value": "us-south"
     }
   ]
 },
 "required_config": {
   "description": "us south restrictions",
   "and": [
     {
       "property": "storage_class",
       "operator": "string_equals",
       "value": [
         "smart"
       ]
     },
     {
       "property": "ibm_sse_kms_customer_root_key_crn",
       "operator": "string_equals",
       "value": [
         "crn:v1:bluemix:public:kms:us-south:a/3bf0d9003abfb5d29761c4e97696b71c:xxxxxxx-07ba-4eb4-877b-e5b0c4966051:key:7f5f825a-0463-4cf4-9042-44366a7298f6"    
       ]
     },
     {
       "property": "firewall.allowed_network_type",
       "operator": "strings_in_list",
       "value": [
         "private"
       ]
     },
     {
       "property": "firewall.allowed_ip",
       "operator": "ips_in_range",
       "value": [
         "fe80:021b::0/64"
       ]
     },
      {
        "property": "activity_tracking.activity_tracker_crn",
        "operator": "string_equals",
        "value": "crn:v1:bluemix:public:logdnaat:us-south:a/91631433ee674cd9ab0ef150b8e7030f:xxxxxxx-830b-43f1-b517-0be1bc50108f::"
      },
      {
        "property": "activity_tracking.write_data_events",
        "operator": "is_true"
      },
      {
        "property": "activity_tracking.read_data_events",
        "operator": "is_true"
      },
      {
        "property": "metrics_monitoring.metrics_monitoring_crn",
        "operator": "string_equals",
        "value": "crn:v1:bluemix:public:sysdig-monitor:us-south:a/9xxxxxxxxxb1xxxc7fdxxxxxxxxxx5:7xxxxxxxx0-xx7x-xdx8-9fxx-123456789012::"
      },
      {
        "property": "metrics_monitoring.usage_metrics_enabled",
        "operator": "is_true"
      },
      {
        "property": "metrics_monitoring.request_metrics_enabled",
        "operator": "is_true"
      },
     {
       "property": "hard_quota",
       "operator": "num_equals",
       "value": "10995116277760"
     }
   ]
 }
}
```
{: codeblock}

