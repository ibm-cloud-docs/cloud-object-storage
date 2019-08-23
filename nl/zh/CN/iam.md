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

# IAM 入门
{: #iam}

## Identity and Access Management 角色和操作
{: #iam-roles}

您帐户中用户对 {{site.data.keyword.cos_full}} 服务实例的访问权由 {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM) 控制。对于访问您帐户中 {{site.data.keyword.cos_full}} 服务的每个用户，必须向其分配定义了 IAM 用户角色的访问策略。该策略确定用户可在您所选服务或实例的上下文中执行的操作。允许的操作由 {{site.data.keyword.Bluemix_notm}} 服务进行定制，并定义为允许在服务上执行的操作。然后，这些操作将映射到 IAM 用户角色。

策略支持在不同级别授予访问权。一些选项包括以下各项： 

* 对帐户中所有服务实例的访问权
* 对帐户中单个服务实例的访问权
* 对实例中特定资源的访问权
* 对帐户中所有启用 IAM 的服务的访问权

在定义访问策略的作用域后，可以分配角色。请查看以下各表，其中概述每个角色在 {{site.data.keyword.cos_short}} 服务中允许执行的操作。

下表详细描述了映射到平台管理角色的操作。平台管理角色支持用户在平台级别对服务资源执行任务，例如，分配用户对服务的访问权、创建或删除服务标识、创建实例以及将实例绑定到应用程序。

|平台管理角色|操作描述|示例操作|
|:-----------------|:-----------------|:-----------------|
|查看者|查看服务实例，但不能对其进行修改| <ul><li>列出可用 COS 服务实例</li><li>查看 COS 服务套餐详细信息</li><li>查看使用情况详细信息</li></ul>|
|编辑者|执行除管理帐户和分配访问策略之外的其他所有平台操作|<ul><li>创建和删除 COS 服务实例</li></ul> |
|操作员|COS 未使用|无|
|管理员|基于分配给此角色的资源执行所有平台操作，包括向其他用户分配访问策略|<ul><li>更新用户策略</li>更新价格套餐</ul>|
{: caption="表 1. IAM 用户角色和操作"}


下表详细描述了映射到服务访问角色的操作。通过服务访问角色，用户可以访问 {{site.data.keyword.cos_short}}，并且有能力调用 {{site.data.keyword.cos_short}} API。

|服务访问角色|操作描述|示例操作|
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
|内容读取者|下载对象，但不能列出对象或存储区| <ul><li>下载对象</li></ul> |
|读取者|除了可执行“内容读取者”操作外，“读取者”还可以列出存储区和/或对象，但不能对其进行修改。| <ul><li>列出存储区</li><li>列出和下载对象</li></ul>                    |
|写入者|除了可执行“读取者”操作外，“写入者”还可以创建存储区和上传对象。| <ul><li>创建新的存储区和对象</li><li>除去存储区和对象</li></ul> |
|管理者|除了可执行“写入者”操作外，“管理者”还可以完成影响访问控制的特权操作。| <ul><li>添加保留时间策略</li><li>添加存储区防火墙</li></ul>              |
{: caption="表 3. IAM 服务访问角色和操作"}


有关在 UI 中分配用户角色的信息，请参阅[管理 IAM 访问权](/docs/iam?topic=iam-iammanidaccser)。
 
