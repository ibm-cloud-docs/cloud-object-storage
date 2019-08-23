---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# 从 OpenStack Swift 迁移数据
{: #migrate}

在 {{site.data.keyword.cos_full_notm}} 作为 {{site.data.keyword.cloud_notm}} Platform 服务可用之前，需要对象存储的项目使用的是 [OpenStack Swift](https://docs.openstack.org/swift/latest/) 或 [OpenStack Swift（基础架构）](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift)。我们建议开发者更新自己的应用程序，并将其数据迁移到 {{site.data.keyword.cloud_notm}}，以利用 IAM 和 Key Protect 提供的新访问控制和加密优点，以及在新功能变得可用时利用新功能。

Swift 的“容器”概念与 COS 的“存储区”概念完全相同。COS 将服务实例限制为只能有 100 个存储区，但某些 Swift 实例的容器数可能超过此数量。COS 存储区在组织数据时，可以容纳数十亿个对象，并支持对象名称中使用正斜杠 (`/`) 来表示类似目录的“前缀”。COS 在存储区和服务实例级别支持 IAM 策略。
{:tip}

要在 Object Storage 服务之间迁移数据，一种方法是使用“同步”或“克隆”工具，例如[开放式源代码 `rclone` 命令行实用程序](https://rclone.org/docs/)。此实用程序将在两个位置之间同步文件树，包括 Cloud Storage。`rclone` 将数据写入 COS 时，将使用 COS/S3 API 根据设置为配置参数的大小和阈值来对大对象分块，然后并行上传这些分块。

COS 和 Swift 之间存在一些差异，在数据迁移过程中必须考虑到这些差异。

  - COS 尚不支持到期策略或版本控制。依赖于这些 Swift 功能的工作流程在迁移到 COS 时，必须改为将其作为应用程序逻辑的一部分进行处理。
  - COS 支持对象级别的元数据，但在使用 `rclone` 迁移数据时，不会保留这些信息。可以使用 `x-amz-meta-{key}: {value}` 头在 COS 中的对象上设置定制元数据，但建议在使用 `rclone` 之前，先将对象级别的元数据备份到数据库。通过[将对象复制到自身](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object)，可以将定制元数据应用于现有对象 - 系统将识别到对象数据完全相同，因此仅更新元数据。请注意，`rclone` **可以**保留时间戳记。
  - COS 将 IAM 策略用于服务实例和存储区级别的访问控制。通过设置 `public-read` ACL（从而无需 authorization 头），[可以使对象公开可用](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access)。
  - COS/S3 API 中处理大对象[分块上传](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)的方法不同于 Swift API。
  - COS 支持熟悉的可选 HTTP 头，例如 `Cache-Control`、`Content-Encoding`、`Content-MD5` 和 `Content-Type`。

本指南提供了有关将数据从单个 Swift 容器迁移到单个 COS 存储区的指示信息。此操作需要针对要迁移的所有容器重复执行，然后需要更新应用程序逻辑以使用新 API。迁移数据后，可以使用 `rclone check` 来验证传输的完整性，这将比较 MD5 校验和，并生成这些校验和不匹配的任何对象的列表。


## 设置 {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. 如果尚未创建 {{site.data.keyword.cos_full_notm}} 实例，请通过[目录](https://cloud.ibm.com/catalog/services/cloud-object-storage)供应一个实例。
  2. 创建将需要存储传输的数据的任何存储区。请通读[入门指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)以熟悉关键概念，例如[端点](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)和[存储类](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes)。
  3. 由于 Swift API 的语法与 COS/S3 API 有明显差异，因此可能需要重构应用程序，才能使用 COS SDK 中提供的等效方法。提供有多种语言（[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python) 和 [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)）的库或 [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。

## 设置计算资源以运行迁移工具
{: #migrate-compute}
  1. 选择距离您的数据最近的 Linux/macOS/BSD 机器或 IBM Cloud 基础架构裸机服务器或虚拟服务器。建议使用以下服务器配置：32 GB RAM，2 到 4 个核心处理器，专用网络速度 1000 Mbps。  
  2. 如果要在 IBM Cloud 基础架构裸机或虚拟服务器上运行迁移，请使用**专用** Swift 和 COS 端点。
  3. 否则，请使用**公共** Swift 和 COS 端点。
  4. 通过[软件包管理器或预编译的二进制文件](https://rclone.org/install/)安装 `rclone`。

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## 针对 OpenStack Swift 配置 `rclone`
{: #migrate-rclone}

  1. 在 `~/.rclone.conf` 中创建 `rclone` 配置文件。

        ```
        touch ~/.rclone.conf
        ```

  2. 通过将以下内容复制并粘贴到 `rclone.conf` 来创建 Swift 源。

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. 获取 OpenStack Swift 凭证
    <br>a. 在 [IBM Cloud 控制台仪表板](https://cloud.ibm.com/)中，单击 Swift 实例。
    <br>b. 单击导航面板中的**服务凭证**。
    <br>c. 单击**新建凭证**以生成凭证信息。单击**添加**。
    <br>d. 查看已创建的凭证，然后复制 JSON 内容。

  4. 填写以下字段：

        ```
        user_id = <userId>
        key = <password>
        region = dallas 或 london            取决于容器位置
        endpoint_type = public 或 internal   internal 表示专用端点
        ```

  5. 跳至“针对 COS 配置 `rclone`”部分


## 针对 OpenStack Swift（基础架构）配置 `rclone`
{: #migrate-config-swift}

  1. 在 `~/.rclone.conf` 中创建 `rclone` 配置文件。

        ```
        touch ~/.rclone.conf
        ```

  2. 通过将以下内容复制并粘贴到 `rclone.conf` 来创建 Swift 源。

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. 获取 OpenStack Swift（基础架构）凭证
    <br>a. 在 IBM Cloud 基础架构客户门户网站中，单击 Swift 帐户。
    <br>b. 单击迁移源容器的数据中心。
    <br>c. 单击**查看凭证**。
    <br>d. 复制以下内容。
      <br>&nbsp;&nbsp;&nbsp;**用户名**
      <br>&nbsp;&nbsp;&nbsp;**API 密钥**
      <br>&nbsp;&nbsp;&nbsp;**认证端点**（基于运行迁移工具的位置）

  4. 使用 OpenStack Swift（基础架构）凭证来填写以下字段：

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## 针对 COS 配置 `rclone`
{: #migrate-config-cos}

### 获取 COS 凭证
{: #migrate-config-cos-credential}

  1. 在 IBM Cloud 控制台中，单击 COS 实例。
  2. 单击导航面板中的**服务凭证**。
  3. 单击**新建凭证**以生成凭证信息。
  4. 在**内联配置参数**下，添加 `{"HMAC":true}`。单击**添加**。
  5. 查看已创建的凭证，然后复制 JSON 内容。

### 获取 COS 端点
{: #migrate-config-cos-endpoint}

  1. 单击导航面板中的**存储区**。
  2. 单击迁移目标存储区。
  3. 单击导航面板中的**配置**。
  4. 向下滚动到**端点**部分，然后根据运行迁移工具的位置来选择端点。

  5. 通过将以下内容复制并粘贴到 `rclone.conf` 来创建 COS 目标。

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. 使用 COS 凭证和端点来填写以下字段：

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## 验证迁移源和目标是否已正确配置
{: #migrate-verify}

1. 列出 Swift 容器以验证 `rclone` 是否已正确配置。

    ```
    rclone lsd SWIFT:
    ```

2. 列出 COS 存储区以验证 `rclone` 是否已正确配置。

    ```
    rclone lsd COS:
    ```

## 运行 `rclone`
{: #migrate-run}

1. 执行 `rclone` 预演（不复制任何数据），以将源 Swift 容器中的对象（例如，`swift-test`）同步到目标 COS 存储区（例如，`cos-test`）。

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. 检查要迁移的文件是否显示在命令输出中。如果一切看起来正常，请除去 `--dry-run` 标志并添加 `-v` 标志以复制数据。使用可选的 `--checksum` 标志可避免更新在两个位置中具有相同 MD5 散列和对象大小的任何文件。

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   您应该尝试在运行 rclone 的机器上最大限度地利用 CPU、内存和网络，以实现最快的传输时间。要调整 rclone，可考虑使用其他几个参数：

   --checkers int 要并行运行的校验器数量。（缺省值为 8）
   这是运行的校验和比较线程数量。我们建议将此值增大到 64 或更多。

   --transfers int 要并行运行的文件传输数量。（缺省值为 4）
   这是要并行传输的对象数量。我们建议将此值增大到 64 或 128 或更高。

   --fast-list 使用递归列表（如果可用）。使用更多内存，但使用更少的事务。
   使用此选项可提高性能 - 减少复制对象所需的请求数。

使用 `rclone` 迁移数据会复制源数据，但不会删除源数据。
{:tip}


3. 对需要迁移的其他任何容器重复上述步骤。
4. 复制所有数据，并且已验证应用程序可以访问 COS 中的数据后，请删除 Swift 服务实例。
