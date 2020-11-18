---
copyright:
  years: 2020
lastupdated: "2020-11-12
"

keywords: security and compliance for cloud-object-storage, security for cloud-object-storage, compliance for cloud-object-storage

subcollection: cloud-object-storage
---

{:external: target="_blank" .external}
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

This service only supports the ability to view the results of your configuration scans in the Security and Compliance Center.  It is not necessary to set up a collector to use configuration rules.
{:note}

## Governing {{site.data.keyword.cos_short}} resource configuration
{: #govern-cloud-object-storage}

As a security or compliance focal, you can use the {{site.data.keyword.compliance_short}} to [define configuration rules](/docs/security-compliance?topic=security-compliance-rules) for the instances of {{site.data.keyword.cos_short}} that you create.

Create rules to ensure that Cloud Object Storage buckets:
* are only created in designated locations
* are only created in a certain storage class
* are created with a designated managed encyption root key CRN
* are prevented from setting public ACLs
* are accessible by using private or direct endpoints only
* are only accessible from designated IP addresses
* can not be accessed from designated IP addresses
* use a designated instance of Activity Tracker
* send object read and/or write events to Activity Tracker

[Configuration rules](x3084914){: term} are used to monitor and enforce configuration standards that you want to implement across your accounts. For more information about configuration rules, see [What makes up a rule](/docs/security-compliance?topic=security-compliance-what-is-rule).

| Resource kind | Property                                               | Operator type | Description                                                                                                                                                                                                      |
|---------------|--------------------------------------------------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| *bucket*      | *location*                                             | String        | Bucket location. This is configured during bucket creation in the request endpoint.                                                                                                                              |
| *bucket*      | *storage_class*                                        | String        | Bucket storage class. This is configured during bucket creation in the `LocationConstraint` field.                                                                                                               |
| *bucket*      | *ibm_sse_kms_customer_root_key_crn*                    | String        | Bucket SSE Key Protect or Hyper Protect Crypto Services Customer Root Key CRN. This maps to the bucket configuration parameter, `ibm-sse-kp-customer-root-key-crn`.                                              |
| *bucket*      | *public_access_block_configuration.block_public_acls*  | String        | Setting to prevent future configuration of ACLs that permit public access on the bucket and its objects. Prior public access configuration for the bucket and its objects is unchanged.                          |
| *bucket*      | *public_access_block_configuration.ignore_public_acls* | String        | Setting to ignore configuration of public ACLs on the bucket and its objects, rendering effective access as private. GET Bucket ACL and GET Object ACL return effective (enforced) permissions for the resource. |
| *bucket*      | *firewall.allowed_network_type*                        | String        | List of network endpoint types that are allowed. Refer to COS Resource Configuration API for the list of valid values.                                                                                           |
| *bucket*      | *firewall.allowed_ip*                                  | String        | List of allowed originating IP addresses/ranges. The list can contain up to 1000 IPv4 or IPv6 addresses/ranges in CIDR notation.                                                                                 |
| *bucket*      | *firewall.denied_ip*                                   | String        | List of originating IP addresses/ranges that are not permitted. The list can contain up to 1000 IPv4 or IPv6 addresses/ranges in CIDR notation.                                                                  |
| *bucket*      | *activity_tracking.activity_tracker_crn*               | String        | CRN of the Activity Tracker instance that receives management events and based on opt-in configuration, read and write data events.                                                                              |
| *bucket*      | *activity_tracking.write_data_events*                  | String        | Opt-in to send the bucket's object write data events (i.e. uploads) to the configured Activity Tracker instance.                                                                                                 |
| *bucket*      | *activity_tracking.read_data_events*                   | String        | Opt-in to send the bucket's object read events (i.e. downloads) to the configured Activity Tracker instance.                                                                                                     |
{: caption="Table 1. Rule properties for {{site.data.keyword.cos_short}}" caption-side="top"}

To learn more about config rules, check out [What is a config rule?](/docs/security-compliance?topic=security-compliance-what-is-rule)

## Evaluating results
{: #results-cloud-object-storage}

After [rules are created and added to scopes](/docs/security-compliance?topic=security-compliance-rules), you can view the evaluation results in the {{site.data.keyword.compliance_short}}. Each rule is shown to be compliant or noncompliant - if a rule shows as being noncompliant then you can view the specific bucket that is in violation of the rule. 

The evaluation results are only available for seven days.  It is recommended that reports are downloaded and organized to maintain a history of compliance for audit purposes. 
{:note}

For more information on reporting results, see [Viewing evaluation results](/docs/security-compliance?topic=security-compliance-results).
