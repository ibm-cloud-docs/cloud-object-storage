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

# IAM 概述
{: #iam-overview}

通过 {{site.data.keyword.cloud}} Identity & Access Management，您可以在 {{site.data.keyword.cloud_notm}} Platform 中安全地认证用户并以一致的方式控制对所有云资源的访问权。有关更多信息，请参阅[入门教程](/docs/iam?topic=iam-getstarted#getstarted)。

## 身份管理
{: #iam-overview-identity}

身份管理包含用户、服务和资源的交互。用户通过其 IBM 标识进行标识。服务通过其服务标识进行标识。资源使用 CRN 进行标识和查找。

通过 {{site.data.keyword.cloud_notm}} IAM 令牌服务，可以为用户和服务创建、更新、删除和使用 API 密钥。这些 API 密钥可以使用 API 调用或 {{site.data.keyword.cloud}} Platform 控制台的 Identity & Access 部分进行创建。相同的密钥可以在多个服务中使用。每个用户都可以有多个 API 密钥，以支持密钥轮换场景，以及支持将不同密钥用于不同目的，以限制单个密钥暴露风险的场景。

有关更多信息，请参阅[什么是 Cloud IAM？](/docs/iam?topic=iam-iamoverview#iamoverview)。

### 用户和 API 密钥
{: #iam-overview-user-api-keys}

使用 CLI 时，{{site.data.keyword.cloud_notm}} 用户可以创建并使用 API 密钥，以实现自动化和脚本编制目的以及联合登录。可以在 Identity and Access Management UI 中或使用 `ibmcloud` CLI 创建 API 密钥。

### 服务标识和 API 密钥
{: #iam-overview-service-id-api-key}

IAM 令牌服务支持创建服务标识和用于服务标识的 API 密钥。服务标识类似于“功能标识”或“应用程序标识”，用于对服务进行认证，而不是用于表示用户。

用户可以创建服务标识并将其绑定到作用域（如 {{site.data.keyword.cloud_notm}} Platform 帐户、CloudFoundry 组织或 CloudFoundry 空间），但采用 IAM 时，最好将服务标识绑定到 {{site.data.keyword.cloud_notm}} Platform 帐户。执行此绑定是为了向服务标识提供可容纳该标识的容器。此容器还定义谁可以更新和删除服务标识，以及谁可以创建、更新、读取和删除与该服务标识关联的 API 密钥。务必注意的是，服务标识与用户无关。

### 密钥轮换
{: #iam-overview-key-rotation}

API 密钥应定期轮换，以避免因泄漏的密钥引起的任何安全违规。

## 访问管理
{: #iam-overview-access-management}

IAM 访问控制提供了为 {{site.data.keyword.cloud_notm}} 资源分配用户角色的常见方法，并可控制用户可以对这些资源执行的操作。您可以查看和管理帐户或组织中的用户，具体取决于授予您的访问权选项。例如，帐户所有者自动分配有 Identity and Access Managemment 的帐户管理员角色，因此帐户所有者能够为其帐户的所有成员分配和管理服务策略。

### 用户、角色、资源和策略
{: #iam-overview-access-policies}

IAM 访问控制支持按服务或服务实例分配策略，以允许在分配的上下文中管理资源和用户的访问级别。策略使用属性组合向用户授予对一组资源的角色，以定义一组适用的资源。将策略分配给用户时，请首先指定服务，然后指定要分配的角色。根据您选择的服务，可能有其他配置选项可用。

虽然角色是操作的集合，但映射到这些角色的操作是特定于服务的。每个服务在上线过程中会确定这种角色到操作的映射，此映射适用于服务的所有用户。角色和访问策略通过策略管理点 (PAP) 进行配置，并通过策略实施点 (PEP) 和策略决策点 (PDP) 强制实施。

要了解更多信息，请参阅[组织用户、团队和应用程序的最佳实践](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications)。
