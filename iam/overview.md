---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Getting started with IAM

{{site.data.keyword.cloud}} Identity & Access Management enables you to securely authenticate users and control access to all cloud resources consistently in the {{site.data.keyword.cloud_notm}} Platform.

## Identity management

Identity management includes the interaction of users, services, and resources. Users are identified by their IBMid. Services are identified by their service IDs. And, resources are identified and addressed by using CRNs.

The {{site.data.keyword.cloud_notm}} IAM Token Service allows you to create, update, delete and use API keys for users and services. Those API keys can be created either with API calls or the Identity & Access section of the {{site.data.keyword.cloud}} Platform Console. The same key can be used across multiple services. Each user can have multiple API keys to support key rotation scenarios, as well as scenarios using different keys for different purposes to limit the exposure of a single key.

### Users and API keys

API keys can be created and used by Bluemix users for automation and scripting purposes as well as federated log in when using the CLI. API keys can be created in the Identity and access management UI or using the `bx` CLI.

### Service IDs and API keys

The IAM Token Service enables the ability to create Service IDs and API keys for Service IDs. A Service ID is similar to a "functional id" or an "application id" and is used to authenticate services, and not to represent a user.

Users can create Service IDs and bind them to scopes like a {{site.data.keyword.cloud_notm}} Platform account, a CloudFoundry organization or a CloudFoundry space, although for adopting IAM, it is best to bind Service IDs to a {{site.data.keyword.cloud_notm}} Platform account. This binding is done to give the Service ID a container to live in. This container also defines who can update and delete the Service ID and who can create, update, read and delete API Keys that are associated to that Service ID. It is important to note that a Service ID is NOT related to a user.

### Key rotation

API keys should be regularly rotated to prevent any security breaches caused by leaked keys.

## Access management

IAM Access Control provides a common way to assign user roles for {{site.data.keyword.cloud_notm}} resources and controls the actions the users can perform on those resources. You can view and manage users across the account or organization, depending on the access options that you have been given. For example, account owners are automatically assigned the account Administrator role for Identity and Access Managemement which enables them to assign and manage service policies for all members of their account.

### Users, roles, resources, and policies

IAM Access Control enables the assignment of policies per service or service instance to allow levels of access for managing resources and users within the assigned context. A policy grants a user a role or roles to a set of resources by using a combination of attributes to define the applicable set of resources. When you assign a policy to a user, you first specify the service then a role or roles to assign. Additional configuration options might be available depending on the service you select.

While roles are a collection of actions, the actions that are mapped to these roles are service specific. Each [service determines this role to action mapping](/docs/services/cloud-object-storage/iam/buckets.html) during the onboarding process and this mapping is applicable to all users of the service. Roles and Access Policies are configured through the Policy Administration Point (PAP) and enforced through the Policy Enforcement Point (PEP) and Policy Decision Point (PDP).
