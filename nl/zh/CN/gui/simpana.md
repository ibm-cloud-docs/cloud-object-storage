---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, simpana

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


# 使用 CommVault Simpana 归档数据
{: #commvault}

CommVault Simpana 与 {{site.data.keyword.cos_full_notm}} 的归档层相集成。有关 Simpana 的更多信息，请参阅：[CommVault Simpana 文档](https://documentation.commvault.com/commvault/)

有关 IBM COS 基础架构归档的更多信息，请参阅[操作指南：归档数据](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive)。

## 集成步骤
{: #commvault-integration}

1.	在 Simpana 控制台中，创建 Amazon S3 Cloud Storage 库。 

2. 确保服务主机指向端点。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。Simpana 可在此步骤中供应存储区，也可以使用已供应的存储区。 

3.	在存储区上创建策略。可以使用 AWS CLI、SDK 或 Web 控制台来创建策略。下面是策略的示例：

```shell
{
  "Rules": [
    {
      "ID": "CommVault",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transitions": [
        {
        "Days": 0,
        "StorageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

### 将策略与存储区相关联
{: #commvault-assign-policy}

1. 执行以下 CLI 命令：

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	使用 Simpana 创建存储策略，并将该存储策略关联到在第一步中创建的 Cloud Storage 库。存储策略用于管理 Simpana 与 COS 之间交互以进行备份传输的方式。在[此处](https://documentation.commvault.com/commvault/v11/article?p=13804.htm)可以找到策略概述。

3.	创建备份集，并将该备份集关联到上一步中创建的存储策略。在[此处](https://documentation.commvault.com/commvault/v11/article?p=11666.htm)可以找到备份集概述。

## 执行备份
{: #commvault-backup}

您可以使用策略启动到存储区的备份，然后执行到 {{site.data.keyword.cos_full_notm}} 的备份。[此处](https://documentation.commvault.com/commvault/v11/article?p=11677.htm)提供了有关 Simpana 备份的更多信息。根据存储区上配置的策略，备份内容会转换到归档层。

## 执行复原
{: #commvault-restore}

您可以从 {{site.data.keyword.cos_full_notm}} 复原备份内容。在[此处](https://documentation.commvault.com/commvault/v11/article?p=12867.htm)可以找到有关 Simpana 复原的更多信息。

### 配置 Simpana 以自动从归档层复原对象
{: #commvault-auto-restore}

1. 创建任务，用于从 COS 复原备份时触发 {{site.data.keyword.cos_full_notm}} 复原。请参阅 [CommVault Simpana 文档](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)以进行配置。

2. 通过 Cloud Storage 重新调用任务，将备份内容从归档层复原到其原始层。Simpana 收到来自 {{site.data.keyword.cos_full_notm}} 的返回码后，将执行此任务。在[此处](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)可以找到有关归档重新调用的更多信息。

3. 复原（从归档层到其原始层）完成后，Simpana 会读取内容并写入其原始位置或配置的位置。
