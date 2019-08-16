---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# 存储区许可权
{: #iam-bucket-permissions}

通过使用 UI 或 CLI 创建策略，为用户和服务标识分配对存储区的访问角色。

|访问角色|示例操作|
|:------------|-------------------------------------------------------------|
|管理者|使对象成为公共对象，创建和销毁存储区和对象|
|写入者|创建和销毁存储区和对象|
|读取者|列出和下载对象|
|内容读取者|下载对象|

## 授予用户访问权
{: #iam-user-access}

如果用户需要能够使用控制台，那么除了服务访问角色（例如，`读取者`）外，**还**需要授予该用户对实例本身的最低平台访问角色`查看者`。这将允许用户查看所有存储区并列出其中的对象。接下来，从左侧导航菜单中选择**存储区许可权**，选择用户，然后选择该用户需要的访问级别（`管理者`或`写入者`）。

如果用户将使用 API 与数据进行交互，而不需要控制台访问权，_并且_该用户是您帐户的成员，那么您可以向其授予对单个存储区的访问权，而不授予对父实例的任何访问权。

## 策略强制实施
{: #iam-policy-enforcement}

IAM 策略是从最高访问级别到最严格访问级别分层强制实施的。解决策略冲突时，将采用更宽松的策略。例如，如果用户同时具有对某个存储区的`写入者`和`读取者`服务访问角色，那么将忽略授予`读取者`角色的策略。

这也适用于服务实例和存储区级别的策略。

- 如果用户有一个策略授予对某个服务实例的`写入者`角色以及对单个存储区的`读取者`角色，那么将忽略存储区级别的策略。
- 如果用户有一个策略授予对某个服务实例的`读取者`角色以及对单个存储区的`写入者`角色，那么将强制实施这两个策略，并且对于此单个存储区，更宽松的`写入者`角色优先。

如果需要限制对单个存储区（或一组存储区）的访问权，请使用控制台或 CLI 来确保用户或服务标识没有任何实例级别的策略。

### 使用 UI
{: #iam-policy-enforcement-console}

要创建新的存储区级别策略，请执行以下操作： 

  1. 从**管理**菜单导航至**访问权 IAM** 控制台。
  2. 从左侧导航菜单中，选择**用户**。
  3. 选择一个用户。
  4. 选择**访问策略**选项卡，以查看该用户的现有策略，然后分配新策略或编辑现有策略。
  5. 单击**分配访问权**以创建新策略。
  6. 选择**分配对资源的访问权**。
  7. 首先从“服务”菜单中选择 **Cloud Object Storage**。
  8. 然后，选择相应的服务实例。在**资源类型**字段中，输入 `bucket`，在**资源标识**字段中，输入存储区名称。
  9. 选择所需的服务访问角色。
  10.  单击**分配**。

请注意，将**资源类型**或**资源**字段保留空白会创建实例级别的策略。
{:tip}

### 使用 CLI
{: #iam-policy-enforcement-cli}

在终端中，运行以下命令：

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

列出现有策略：

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

编辑现有策略：

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## 授予服务标识访问权
{: #iam-service-id}
如果需要为应用程序或其他非人类实体授予对存储区的访问权，请使用服务标识。服务标识可以是专为此目的创建的，也可以是已在使用的现有服务标识。

### 使用 UI
{: #iam-service-id-console}

  1. 从**管理**菜单导航至**访问权 (IAM)** 控制台。
  2. 从左侧导航菜单中，选择**服务标识**。
  3. 选择一个服务标识以查看任何现有策略，然后分配新策略或编辑现有策略。
  3. 选择服务实例、服务标识和所需角色。
  4. 在**资源类型**字段中，输入 `bucket`，在**资源**字段中，输入存储区名称。
  5. 单击**提交**。

  请注意，将**资源类型**或**资源**字段保留空白会创建实例级别的策略。
  {:tip}

### 使用 CLI
{: #iam-service-id-cli}

在终端中，运行以下命令：

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

列出现有策略：

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

编辑现有策略：

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
