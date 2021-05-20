---
copyright:
  years: 2020, 2021
lastupdated: "2021-03-29"

keywords: security and compliance for cloud-object-storage, security for cloud-object-storage, compliance for cloud-object-storage

subcollection: cloud-object-storage
---

{:external: target="_blank" .external}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:term: .term}
{:shortdesc: .shortdesc}
{:table: .aria-labeledby="caption"}


# Managing security and compliance with {{site.data.keyword.cos_full_notm}}
{: #manage-security-compliance}

{{site.data.keyword.cos_full_notm}} is integrated with the [{{site.data.keyword.compliance_short}}](/docs/security-compliance) to help you manage security and compliance for your organization.
{: shortdesc}

With the {{site.data.keyword.compliance_short}}, you can:

* Monitor for controls and goals that pertain to {{site.data.keyword.cos_short}}.
* Define rules for {{site.data.keyword.cos_short}} that can help to standardize resource configuration.
* Enforce those rules by failing requests that would violate defined rules.

This service supports the ability to view and the results of your configuration scans in the Security and Compliance Center, as well allowing for the enforcement of defined rules. The enforcement of some rules may require the use of **templates** to automatically assign default values to new bucket resources.  It is not necessary to set up a collector to use configuration rules.
{:note}

## Monitoring security and compliance posture with {{site.data.keyword.cos_short}}
{: #monitor-cloud-object-storage}

As a security or compliance focal, you can use the {{site.data.keyword.cos_short}} [goals](#x2117978){: term} to help ensure that your organization is adhering to the external and internal standards for your industry. By using the {{site.data.keyword.compliance_short}} to validate the resource configurations in your account against a [profile](#x2034950){: term}, you can identify potential issues as they arise or even prevent actions that would create a violation of your security policy.

All of the goals for {{site.data.keyword.cos_short}} are added to the {{site.data.keyword.cloud_notm}} Best Practices Controls 1.0 profile but can also be mapped to other profiles.
{: note}

To start monitoring your resources, check out [Getting started](/docs/security-compliance) for {{site.data.keyword.compliance_short}}.

### Available goals for {{site.data.keyword.cos_short}}
{: #cloud-object-storage-available-goals}

* Check whether Cloud Object Storage public access is disabled in IAM settings (not applicable to ACLs managed using S3 APIs)
* Check whether Cloud Object Storage is enabled with encryption
* Check whether Cloud Object Storage is enabled with customer-managed encryption and Bring Your Own Key (BYOK)
* Check whether Cloud Object Storage is accessible only by using private endpoints
* Check whether Cloud Object Storage bucket access is restricted by using IAM and S3 access control
* Check whether Cloud Object Storage network access is restricted to a specific IP range
* Check whether Cloud Object Storage is enabled with customer-managed encryption and Keep Your Own Key (KYOK)
* Check whether Cloud Object Storage buckets are enabled with IBM Cloud Monitoring
* Check whether Cloud Object Storage bucket resiliency is set to cross region


## Governing {{site.data.keyword.cos_short}} resource configuration
{: #govern-cloud-object-storage}

As a security or compliance focal, you can use the {{site.data.keyword.compliance_short}} to [define configuration rules](/docs/security-compliance?topic=security-compliance-rules) for the instances of {{site.data.keyword.cos_short}} that you create.

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

[Configuration rules](x3084914){: term} are used to monitor and enforce configuration standards that you want to implement across your accounts. For more information about configuration rules, see [What makes up a rule](/docs/security-compliance?topic=security-compliance-what-is-rule).

| Resource kind | Property                                               | Operator type | Description                                                                                                                                                                                                      |
|---------------|--------------------------------------------------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| *bucket*      | *location*                                             | string        | Cloud Object Storage bucket location.                                                                                                                              |
| *bucket*      | *storage_class*                                        | string       | Cloud Object Storage bucket storage class. This field is case-sensitive. Valid values are standard                                                                                                          |
| *bucket*      | *ibm_sse_kms_customer_root_key_crn*                    | string      | SSE Key Protect or Hyper Protect Crypto Services Customer Root Key CRN associated with a Cloud Object Storage bucket. This maps to the bucket configuration parameter, `ibm-sse-kp-customer-root-key-crn`.                                              |
| *bucket*      | *public_access_block_configuration.block_public_acls*  | boolean       | Setting to prevent future configuration of ACLs that permit public access on the Cloud Object Storage bucket and its objects. Prior public access configuration for the bucket and its objects is unchanged.                          |
| *bucket*      | *public_access_block_configuration.ignore_public_acls* | boolean      | Setting to ignore configuration of public ACLs on the Cloud Object Storage bucket and its objects, rendering effective access as private. `GET Bucket ACL` and `GET Object ACL` return effective (enforced) permissions for the resource. |
| *bucket*      | *firewall.allowed_network_type*                        | string list  | List of network endpoint types (`public`, `private`, or `direct`) that are allowed.                                                                                           |
| *bucket*      | *firewall.allowed_ip*                                  | IP list  | List of allowed originating (source) IP addresses/ranges. The list can contain up to 1000 IPv4 or IPv6 addresses/ranges in CIDR notation.                                                                                 |
| *bucket*      | *firewall.denied_ip*                                   | IP list  | List of originating (source) IP addresses/ranges that are not permitted. The list can contain up to 1000 IPv4 or IPv6 addresses/ranges in CIDR notation.   |
| *bucket*      | *activity_tracking.activity_tracker_crn*               | string       | CRN of the Activity Tracker instance that receives both management (bucket-level) and data (object-level) events. Management events are sent automatically, but data events are opt-in.                                                                              |
| *bucket*      | *activity_tracking.write_data_events*                  | boolean     | If set to true, the Cloud Object Storage bucket's object write data events (i.e. uploads) will be sent to the activity tracking service.                                                                                                 |
| *bucket*      | *activity_tracking.read_data_events*                   | boolean     | If set to true, the Cloud Object Storage bucket's object read events (i.e. downloads) will be sent to the activity tracking service.                                                                                                     |
| *bucket*      | *metrics_monitoring.metrics_monitoring_crn*               | string        | CRN of the IBM Cloud Monitoring instance that receives the bucket metrics.                                                                              |
| *bucket*      | *metrics_monitoring.usage_metrics_enabled*                  | boolean       | If set to true, the Cloud Object Storage bucket's usage metrics will be sent to the monitoring service.                                                                                                 |
| *bucket*      | *hard_quota*                                           | numeric        | Maximum bytes allotted to the Cloud Object Storage bucket.                                                                                                     |
{: caption="Table 1. Rule properties for {{site.data.keyword.cos_short}}" caption-side="top"}

Although the operator types may indicate the values are numeric or boolean, all of the values in the rule are entered as strings or lists of strings, such as `'true'` or `'324342'`.  Additionally, even though an operator type might indicate a string, it is still possible to set an array of possible strings that comply with the rule.  For more information, see [What are the supported operators?](https://cloud.ibm.com/docs/security-compliance?topic=security-compliance-what-is-rule#rule-operators)
{:note}

To learn more about configuration rules and how they are evaluated and enforced, check out [What is a configuration rule?](/docs/security-compliance?topic=security-compliance-what-is-rule).

After [rules are created and added to scopes](/docs/security-compliance?topic=security-compliance-rules), you can view the evaluation results in the {{site.data.keyword.compliance_short}}. Each rule is shown to be compliant or noncompliant - if a rule shows as being noncompliant then you can view the specific bucket that is in violation of the rule. 

The evaluation results are only available for a limited period.  It is recommended that reports are downloaded and organized to maintain a history of compliance for audit purposes. For more information on reporting results, see [Viewing evaluation results](/docs/security-compliance?topic=security-compliance-results).
{:note}

For example, let's assume you want to enforce a set of requirements on new buckets:

1. Only buckets in the `us-south` region are subject to the rule; other locations aren't affected.
2. Buckets must use the Smart Tier storage class.
3. A firewall must be in place to only allow requests from inside the IBM Cloud.
4. Only the IP addresses in the range `fe80:021b::0/64` will be allowed to make requests.
5. Activity tracking must be enabled for both read and write requests.
6. The bucket cannot be allowed to grow past 10 TiB (10995116277760 bytes).

For step-by-step instructions using the UI and API, see [Working with config rules](/docs/security-compliance?topic=security-compliance-rules).
{:tip}

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
       "property": "hard_quota",
       "operator": "num_equals",
       "value": "10995116277760"
     }
   ]
 }
}
```
{: codeblock}

While you can set the enforcement action for this rule to log any violations without trouble, you will need to use a **template** to enforce this rule using `disallow` - as only the `location`, `storage_class`, and `ibm_sse_kms_customer_root_key_crn` can be set when you create a bucket, and the other parameters can only be applied to an existing bucket.

### Using templates to automatically assign default values

When creating a bucket, you can assign the location, storage class, and encryption key CRN.  All other aspects of that bucket's configuration, such as firewall details, activity tracking, metrics monitoring, or a hard quota on a bucket's size must be applied to an existing bucket after creation.  Enforcing these rules would then be paradoxical - as it would not be possible to create a bucket that is in compliance with the security requirements established for new buckets. Templates make it possible to automatically assign default values to ensure that new buckets are in compliance with defined rules.

For step-by-step instructions using the UI and API, see [Managing templates](/docs/security-compliance?topic=security-compliance-templates).

The template used to allow enforcement this would look like the following:

```json
{
 "target": {
   "service_name": "cloud-object-storage",
   "resource_kind": "bucket",
   "additional_target_attributes": [
     {
       "name": "location",
       "value": "us-south"
     }
   ]
 },
 "customized_defaults": [
     {
       "property": "storage_class",
       "value": "us-south-smart"    
     },
     {
       "property": "firewall.allowed_network_type",
       "value": [
         "private"
       ]
     },
     {
       "property": "firewall.allowed_ip",
       "value": [
         "fe80:021b::0/64"
       ]
     },
      {
        "property": "activity_tracking.activity_tracker_crn",
        "value": "crn:v1:bluemix:public:logdnaat:us-south:a/91631433ee674cd9ab0ef150b8e7030f:xxxxxxx-830b-43f1-b517-0be1bc50108f::"
      },
      {
        "property": "activity_tracking.write_data_events",
        "value": "true"
      },
      {
        "property": "activity_tracking.read_data_events",
        "value": "true"
      },
     {
       "property": "hard_quota",
       "value": "10995116277760"
     }
   ]
 }
```
{: codeblock}
