---
copyright:
  years: 2020
lastupdated: "2020-11-12"

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

{{site.data.keyword.cos_full_notm}} is integrated with the {{site.data.keyword.compliance_short}} to help you manage security and compliance for your organization.
{: shortdesc}

With the {{site.data.keyword.compliance_short}}, you can:

* Monitor for controls and goals that pertain to {{site.data.keyword.cos_short}}.
* Define rules for {{site.data.keyword.cos_short}} that can help to standardize resource configuration.

This service only supports the ability to view the results of your configuration scans in the Security and Compliance Center.
{:note}

## Monitoring security and compliance posture with {{site.data.keyword.cos_short}}
{: #monitor-cloud-object-storage}

As a security or compliance focal, you can use the {{site.data.keyword.cos_short}} [goals](x2117978){: term} to help ensure that your organization is adhering to the external and internal standards for your industry. By using the {{site.data.keyword.compliance_short}} to validate the resource configurations in your account against a [profile](x2034950){: term}, you can identity potential issues as they arise.

All of the goals for {{site.data.keyword.cos_short}} are added to the {{site.data.keyword.cloud_notm}} Best Practices Controls 1.0 profile but can also be mapped to other profiles.
{: note}

To start monitoring your resources, check out [Getting started with {{site.data.keyword.compliance_short}}](/docs/security-compliance?topic-security-compliance-getting-started)

### Available goals for {{site.data.keyword.cos_short}}
{: #cloud-object-storage-available-goals}

* Ensure that Cloud Object Storage buckets are accessible by using private endpoints only

## Governing {{site.data.keyword.cos_short}} resource configuration
{: #govern-cloud-object-storage}

As a security or compliance focal, you can use the {{site.data.keyword.compliance_short}} to define configuration rules for the instances of {{site.data.keyword.cos_short}} that you create.

[Config rules](x3084914){: term} are used to enforce the configuration standards that you want to implement across your accounts. To learn more about the about the data that you can use to create a rule for {{site.data.keyword.cos_short}}, review the following table.

| Resource kind   | Property               | Operator type | Value   | Description                                                                                                        |
|-----------------|------------------------|---------------|---------|--------------------------------------------------------------------------------------------------------------------|
| *instance*      | *private_network_only* | Boolean       | -       | *Indicates whether access to a {{site.data.keyword.cos_short}} instance is allowed only through a private network. |
| <resource_kind> | <property_name>        | <operator>    | <value> | <description>                                                                                                      |
{: caption="Table 1. Rule properties for {{site.data.keyword.cos_short}}" caption-side="top"}

To learn more about config rules, check out [What is a config rule?](/docs/security-compliance?topic=security-compliance-what-is-rule).

