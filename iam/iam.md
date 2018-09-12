---

copyright:
  years:  2017
lastupdated: "2017-10-09"

---

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:screen: .screen}
{:pre: .pre}
{:table: .aria-labeledby="caption"}
{:codeblock: .codeblock}
{:tip: .tip}
{:download: .download}

# Service provider IAM docs template 

## Managing user access with Identity and Access Management

Access to {{site.data.keyword.cos_short}} service instances for users in your account is controlled by {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM). Every user that accesses the {{site.data.keyword.cos_short}} service in your account must be assigned an access policy with an IAM user role defined. That policy determines what actions the user can perform within the context of the service or instance you select. The allowable actions are customized and defined by the {{site.data.keyword.Bluemix_notm}} service as operations that are allowed to be performed on the service. The actions are then mapped to IAM user roles.

Policies enable access to be granted at different levels. Some of the options include the following: 

* Access across all instances of the service in your account
* Access to an individual service instance in your account
* Access to a specific resource within an instance
* Access to all IAM-enabled services in your account

After you define the scope of the access policy, you assign a role. Review the following tables which outline what actions each role allows within the {{site.data.keyword.cos_short}} service.

The following table details actions that are mapped to platform management roles. Platform management roles enable users to perform tasks on service resources at the platform level, for example assign user access for the service, create or delete service IDs, create instances, and bind instances to applications.

| Platform management role | Description of actions | Example actions|
|:-----------------|:-----------------|:-----------------|
| Viewer | Description | <ul><li>Example 1</li><li>Example 2</li></ul>|
| Editor | Description |<ul><li>Example 1</li><li>Example 2</li></ul> |
| Operator | Description | <ul><li>Example 1</li><li>Example 2</li><li>Example 3</li></ul> |
| Administrator | Description |<ul><li>Example 1</li><li>Example 2</li><li>Example 3</li></ul>|
{: caption="Table 1. IAM user roles and actions" caption-side="top"}

 For {{site.data.keyword.cos_short}}, the following actions exist:

| Action | Operation on service | Role
|:-----------------|:-----------------|:--------------|
| Action name from the IAM service registration | Operation allowed on the service for this action | IAM role it maps to |
| Action name from the IAM service registration | Operation allowed on the service for this action | IAM role it maps to |
{: caption="Table 2. Service actions and operations" caption-side="top"}

The following table details actions that are mapped to service access roles. Service access roles enable users access to {{site.data.keyword.cos_short}} as well as the ability to call the `service name's` API.

| Service access role | Description of actions | Example actions|
|:-----------------|:-----------------|:-----------------|
| Reader | Description | <ul><li>Example 1</li><li>Example 2</li></ul>|
| Writer | Description |<ul><li>Example 1</li><li>Example 2</li></ul> |
| Manager | Description | <ul><li>Example 1</li><li>Example 2</li><li>Example 3</li></ul> |
{: caption="Table 3. IAM service access roles and actions" caption-side="top"}

 For {{site.data.keyword.cos_short}}, the following actions exist:

| Action | Operation on service | Role
|:-----------------|:-----------------|:--------------|
| Action name from the IAM service registration | Operation allowed on the service for this action | IAM role it maps to |
| Action name from the IAM service registration | Operation allowed on the service for this action | IAM role it maps to |
{: caption="Table 4. Service actions and operations" caption-side="top"}

For information about assigning user roles in the UI, see [Managing IAM access](/docs/iam/mngiam.html#iammanidaccser) **This link is not live in production yet. Use https://console.bluemix.net/docs/iam/iamusermanage.html#iamusermanage until the link above is available in production**.
 