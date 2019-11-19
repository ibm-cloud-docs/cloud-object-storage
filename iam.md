---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

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
{: .shortdesc}

## Identity and Access Management roles and actions
{: #iam-roles}

Every user that accesses the {{site.data.keyword.cos_full}} service in your account must be assigned an access policy with an IAM user role defined. That policy determines what actions the user can perform within the context of the service or instance you select. The allowable actions are customized and defined by the {{site.data.keyword.cloud_notm}} service as operations that are allowed to be performed on the service. The actions are then mapped to IAM user roles.

Policies enable access to be granted at different levels. Some of the options include the following: 

* Access across all instances of the service in your account
* Access to an individual service instance in your account
* Access to a specific resource within an instance
* Access to all IAM-enabled services in your account

After you define the scope of the access policy, you assign a role. Review the following tables which outline what actions each role allows within the {{site.data.keyword.cos_short}} service.

The following table details actions that are mapped to platform management roles. Platform management roles enable users to perform tasks on service resources at the platform level, for example assign user access for the service, create or delete service IDs, create instances, and bind instances to applications.

| Platform management role | Description of actions | Example actions|
|:-----------------|:-----------------|:-----------------|
| Viewer | View service instances but not modify them | <ul><li>List available COS service instances</li><li>View COS service plan details</li><li>View usage details</li></ul>|
| Editor | Perform all platform actions except for managing the accounts and assigning access policies |<ul><li>Create and delete COS service instances</li></ul> |
| Operator | Not used by COS | None |
| Administrator | Perform all platform actions based on the resource this role is being assigned, including assigning access policies to other users |<ul><li>Update user policies</li>Update pricing plans</ul>|
{: caption="Table 1. IAM user roles and actions"}


The following table details actions that are mapped to service access roles. Service access roles enable users access to {{site.data.keyword.cos_short}} as well as the ability to call the {{site.data.keyword.cos_short}} API.

| Service access role | Description of actions                                                                                                                                       | Example actions                                                                     |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| Content Reader      | Download objects, but not list objects or buckets | <ul><li>Download objects</li></ul> |
| Reader              | In addition to Content Reader actions, Readers can list buckets and/or objects but not modify them. | <ul><li>List buckets</li><li>List and download objects</li></ul>                    |
| Writer              | In addition to Reader actions, Writers can create buckets and upload objects. | <ul><li>Create new buckets and objects</li><li>Remove buckets and objects</li></ul> |
| Manager             | In addition to Writer actions, Managers can complete privileged actions that affect access control. | <ul><li>Add a retention policy</li><li>Add a bucket firewall</li></ul>              |
{: caption="Table 3. IAM service access roles and actions"}


For information about assigning user roles in the UI, see [Managing IAM access](/docs/iam?topic=iam-iammanidaccser).
 
