---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# 入门教程
{: #getting-started}

本入门教程将全程引导您完成创建存储区、上传对象和设置访问策略所需的步骤，以允许其他用户使用您的数据。
{: shortdesc}

## 开始之前
{: #gs-prereqs}

您需要：
  * [{{site.data.keyword.cloud}} 平台帐户](https://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}} 的实例](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * 以及本地计算机上要上传的一些文件。
{: #gs-prereqs}

 本教程会引导新用户完成 {{site.data.keyword.cloud_notm}} Platform 控制台的第一步。对于要开始使用 API 的开发者，请参阅[开发者指南](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev)或 [API 概述](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。

## 创建一些存储区以用于存储数据
{: #gs-create-buckets}

  1. [订购 {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) 会创建_服务实例_。{{site.data.keyword.cos_full_notm}} 是一个多租户系统，{{site.data.keyword.cos_short}} 的所有实例共享物理基础架构。系统会将您自动重定向到在其中可以开始创建存储区的服务实例。{{site.data.keyword.cos_short}} 实例会在[资源列表](https://cloud.ibm.com/resources)中的**存储器**下列出。

术语“资源实例”和“服务实例”是指同一概念，可以互换使用。
{: tip}

  1. 执行**创建存储区**并选择唯一名称。全球所有区域中的所有存储区都共享一个名称空间。确保您具有创建存储区的[正确许可权](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions)。

  **注**：创建存储区或添加对象时，请确保避免使用个人可标识信息 (PII)。PII 是可以通过名称、位置或其他任何方式识别到任何用户（自然人）的信息。
  {: tip}

  1. 首先选择所需的[_弹性_级别](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)，然后选择要在其中以物理方式存储数据的_位置_。弹性是指在其中分布数据的地理区域的范围和规模。_跨区域_弹性是在多个大城市区域中分布数据，_区域_弹性是在单个大城市区域中分布数据。_单个数据中心_仅在单个站点内的多个设备中分布数据。
  2. 选择[存储区的_存储类_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes)，这反映了您期望读取所存储数据的频率，并可确定计费详细信息。请遵循**创建**链接来创建和访问新存储区。

存储区是一种用于组织数据的方式，但并不是唯一的方式。对象名称（通常称为_对象键_）可以使用一个或多个正斜杠来表示类似目录的组织系统。然后，在定界符前面使用对象名称的一部分以构成_对象前缀_，此前缀用于通过 API 列出单个存储区中的相关对象。
{: tip}


## 向存储区添加一些对象
{: #gs-add-objects}

现在，通过从列表中选择其中一个存储区来转至该存储区。单击**添加对象**。新对象会覆盖同一存储区中同名的现有对象。使用控制台来上传对象时，对象名称始终与文件名相匹配。如果是使用 API 来写入数据，那么文件名与对象键之间无需有任何关系。请将若干文件添加到此存储区。

通过控制台上传对象时，对象大小限制为 200 MB，除非使用了 [Aspera 高速传输](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload)插件。更大的对象（最大为 10 TB）还可以[使用 API 拆分成分块，然后并行上传](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)。对象键的长度最大可以为 1024 个字符，最好避免使用可能导致 Web 地址发生问题的任何字符。例如，`?`、`=`、`<` 和其他特殊字符如果未经过 URL 编码，可能会导致意外行为。
{:tip}

## 邀请用户加入帐户以管理存储区和数据
{: #gs-invite-user}

现在，您将添加另一个用户，并允许该用户充当实例以及该实例中所存储的任何数据的管理员。

  1. 首先，要添加新用户，您需要离开当前 {{site.data.keyword.cos_short}} 界面并转至 IAM 控制台。转至**管理**菜单，然后访问**访问权 (IAM)** > **用户**中的链接。单击**邀请用户**。
	<img alt="IAM 邀请用户" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`图 1：IAM 邀请用户`
  2. 输入要邀请加入组织的用户的电子邮件地址，然后展开**服务**部分，并从**分配对以下对象的访问权**菜单中选择“资源”。现在，从**服务**菜单中选择“Cloud Object Storage”。
	<img alt="IAM 服务" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`图 2：IAM 服务`
  3. 现在，还将显示另外三个字段：_服务实例_、_资源类型_和_资源标识_。第一个字段定义用户可以访问的 {{site.data.keyword.cos_short}} 实例。此字段还可以设置为授予对 {{site.data.keyword.cos_short}} 的所有实例的相同级别访问权。目前可以将其他字段保留为空白。
	<img alt="IAM 邀请用户" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`图 3：IAM 邀请用户`
  4. **选择角色**下的复选框可确定可供用户使用的操作集。选择“管理员”平台访问角色，可允许用户授予其他[用户和服务标识](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)对实例的访问权。选择“管理者”服务访问角色，可允许用户管理 {{site.data.keyword.cos_short}} 实例，以及创建和删除存储区和对象。_主体_（用户）、_角色_（管理者）和 _资源_（{{site.data.keyword.cos_short}} 服务实例）的这些组合一起构成了 [IAM 策略](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)。有关角色和策略的更详细指导信息，请参阅 [IAM 文档](/docs/iam?topic=iam-userroles)。
	<img alt="IAM 角色" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`图 4：IAM 选择角色`
  5. {{site.data.keyword.cloud_notm}} 使用 Cloud Foundry 作为底层帐户管理平台，因此必须首先向用户授予最低级别的 Cloud Foundry 访问权，用户才能访问组织。从**组织**菜单中选择组织，然后从**组织角色**和**空间角色**菜单中，都选择“审计员”。设置 Cloud Foundry 许可权将允许用户查看可用于组织的服务，但不允许用户对其进行更改。

## 授予开发者对存储区的访问权。
{: #gs-bucket-policy}

  1. 导航至**管理**菜单，然后访问**访问权 (IAM)** > **服务标识**中的链接。在此可以创建_服务标识_，该标识用作与帐户绑定的抽象身份。可以向服务标识分配 API 密钥，并且服务标识在您不希望将特定开发者身份与应用程序的过程或组件关联的情况下使用。
	<img alt="IAM 服务标识" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`图 5：IAM 服务标识`
  2. 重复上述过程，但在步骤 3 中，选择特定服务实例，然后输入“存储区”作为_资源类型_，并输入现有存储区的完整 CRN 作为_资源标识_。
  3. 现在，服务标识可以访问该特定存储区，但不能访问其他存储区。

## 后续步骤
{: #gs-next-steps}

既然您已通过基于 Web 的控制台熟悉对象存储器，您可能有兴趣使用 `ibmcloud cos` 命令行实用程序通过命令行来执行类似的工作流程，以创建服务实例以及与 IAM 交互，还可以通过 `curl` 直接访问 COS。请首先[查看 API 概述](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。
