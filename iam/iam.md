---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
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

# Getting Started with IAM
{: #iam}

## Identity and Access Management roles and actions
{: #iam-roles}

Access to {{site.data.keyword.cos_full}} service instances for users in your account is controlled by {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM). Every user that accesses the {{site.data.keyword.cos_full}} service in your account must be assigned an access policy with an IAM user role defined. That policy determines what actions the user can perform within the context of the service or instance you select. The allowable actions are customized and defined by the {{site.data.keyword.Bluemix_notm}} service as operations that are allowed to be performed on the service. The actions are then mapped to IAM user roles.

Policies enable access to be granted at different levels. Some of the options include the following: 

* Access across all instances of the service in your account
* Access to an individual service instance in your account
* Access to a specific resource within an instance
* Access to all IAM-enabled services in your account

After you define the scope of the access policy, you assign a role. Review the following tables which outline what actions each role allows within the {{site.data.keyword.cos_short}} service.

The following table details actions that are mapped to platform management roles. Platform management roles enable users to perform tasks on service resources at the platform level, for example assign user access for the service, create or delete service IDs, create instances, and bind instances to applications.

| Platform management role | Description of actions | Example actions|
|:-----------------|:-----------------|:-----------------|
| Viewer | View service instances but not modify them | <ul><li>List available COS services</li><li>View COS service plan details</li><li>View usage details</li><li>List buckets and objects</li></ul>|
| Editor | Perform all platform actions except for managing the accounts and assigning access policies |<ul><li>Create new buckets</li><li>Create new objects</li></ul> |
| Operator | Perform platform actions required to configure and operate service instances, such as viewing a service's dashboard | <ul><li>Bind Cloud Foundry services</li><li>Create credentials</li></ul> |
| Administrator | Perform all platform actions based on the resource this role is being assigned, including assigning access policies to other users |<ul><li>Create serivce instances</li><li>Remove service instances</li><li>Update user policies</li>Update pricing plans</ul>|
{: caption="Table 1. IAM user roles and actions" caption-side="top"}

 For {{site.data.keyword.cos_full}}, the following actions exist:

| Role | Action | Operation on service | 
|:-----------------|:-----------------|:--------------|
| Viewer role | View instances, credentials, buckets, and objects | View service instance details | 
| Editor role | Create, delete, edit instances, manage credentials, create, delete, edit buckets and objects | Create, delete, and edit service instances |
| Operator role | Edit service instances, view service dashboard | Manage service instances |
| Administrator role | All actions including managing user access | All management actions |
{: caption="Table 2. Service actions and operations" caption-side="top"}

The following table details actions that are mapped to service access roles. Service access roles enable users access to {{site.data.keyword.cos_short}} as well as the ability to call the {{site.data.keyword.cos_short}} API.

| Service access role | Description of actions | Example actions|
|:-----------------|:-----------------|:-----------------|
| Reader | Perform read-only actions within a service such as viewing service-specific resources | <ul><li>List buckets</li><li>List and download objects</li></ul>|
| Writer | Permissions beyond the reader role, including creating and editing service-specific resources |<ul><li>Create new buckets and objects</li><li>Remove buckets and objects</li></ul> |
| Manager | Permissions beyond the writer role to complete privileged actions as defined by the service. In addition, you can create and edit service-specific resources | <ul><li>Update user access roles</li><li>Key-protect buckets</li></ul> |
{: caption="Table 3. IAM service access roles and actions" caption-side="top"}

 For {{site.data.keyword.cos_short}}, the following actions exist:

| Role | Action | Operation on service | 
|:-----------------|:-----------------|:--------------|
| Reader role | View service instance resources | View buckets and objects within service instance | 
| Writer role | Create, delete, and edit service instance resources | Create, delete and edit buckets and objects within service instance |
| Manager role | All actions on service instance resources and update user access | Update user access roles | 
{: caption="Table 4. Service actions and operations" caption-side="top"}

For information about assigning user roles in the UI, see [Managing IAM access](/docs/iam/mngiam.html#iammanidaccser).
 
