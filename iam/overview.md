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

# IAM overview
{: #iam-overview}

{{site.data.keyword.cloud_notm}} Identity and Access Management service securely authenticates users and controls access to all resources consistently in the {{site.data.keyword.cloud_notm}} Platform. 
{: shortdesc}

For more information, see the [Getting started tutorial](/docs/iam?topic=iam-getstarted#getstarted).

## Identity  Management
{: #iam-overview-identity}

Identity  Management includes the interaction of users, services, and resources. Users are identified by their IBMid. Services are identified by their service IDs. And, resources are identified and addressed by using CRNs.

The {{site.data.keyword.cloud_notm}} IAM Token Service is used to create, update, delete, and use API keys for users and services. Those API keys are created either with API calls or the Identity & Access section of the {{site.data.keyword.cloud}} Platform Console. The same key can be used across services. Each user has any number of API keys to support key rotation scenarios, as well as scenarios by using different keys for different purposes to limit the exposure of a single key.

For more information, see [the IAM documentation](/docs/iam?topic=iam-iamoverview#iamoverview).

### Users and API keys
{: #iam-overview-user-api-keys}

API keys can be created and used by {{site.data.keyword.cloud_notm}} users for automation and scripting purposes. API keys can be created in the Identity and Access Management UI or by using the `ibmcloud` CLI.

### Service IDs and API keys
{: #iam-overview-service-id-api-key}

Users also can create Service IDs and API keys for Service IDs. A Service ID is similar to a "functional ID" or an "application ID" and is used to authenticate services, and not to represent a user.

Users create Service IDs and bind them to scopes, like a {{site.data.keyword.cloud_notm}} Platform account, a CloudFoundry organization, or a CloudFoundry space. It is best to bind Service IDs to a {{site.data.keyword.cloud_notm}} Platform account. This binding is done to give the Service ID a container to live in. This container also defines who can update and delete the Service ID and who can create, update, read, and delete API Keys that are associated to that Service ID. It is important to note that a Service ID is NOT related to a user.

### Key rotation
{: #iam-overview-key-rotation}

API keys can be regularly rotated to prevent any security breaches caused by leaked keys.

## Access Management
{: #iam-overview-access-management}

IAM Access Control provides a common way to assign user roles for {{site.data.keyword.cloud_notm}} resources and controls the actions that the users can take on those resources. You can view and manage users across the account or organization, depending on the access options that you have been given. For example, account owners are automatically assigned the account Administrator role for Identity and Access Managemement, which enables them to assign and manage service policies for all members of their account.

### Users, roles, resources, and policies
{: #iam-overview-access-policies}

IAM Access Control enables the assignment of policies per service or service instance to allow levels of access for managing resources and users within the assigned context. A policy grants a user a role or roles to a set of resources by using a combination of attributes to define the applicable set of resources. When you assign a policy to a user, you first specify the service then a role or roles to assign. Extra configuration options might be available depending on the service you select.

While roles are a collection of actions, the actions that are mapped to these roles are service specific. Each service determines this role to action mapping during the onboarding process and this mapping effects all users of the service. Roles and access policies are configured through the Policy Administration Point (PAP) and enforced through the Policy Enforcement Point (PEP) and Policy Decision Point (PDP).

See [Best practices for organizing users, teams, applications](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications) to learn more.
