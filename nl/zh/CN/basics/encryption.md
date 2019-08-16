---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# 管理加密
{: #encryption}

缺省情况下，{{site.data.keyword.cos_full}} 中存储的所有对象都使用[随机生成的密钥以及全有或全无变换](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security)进行加密。虽然此缺省加密模型提供了静态安全性，但某些工作负载需要拥有所使用的加密密钥。您可以通过在存储数据时提供您自己的加密密钥 (SSE-C) 来手动管理密钥，也可以创建使用 IBM Key Protect (SSE-KP) 的存储区来管理加密密钥。

## 使用客户提供的密钥进行服务器端加密 (SSE-C)
{: #encryption-sse-c}

SSE-C 会在对象上强制实施。如果请求是使用客户管理的密钥来对对象或其元数据执行读取或写入操作，那么请求会将必需的加密信息作为 HTTP 请求中的头发送。语法与 S3 API 完全相同，支持 SSE-C 的与 S3 兼容的库应该会按预期对 {{site.data.keyword.cos_full}} 工作。

使用 SSE-C 头的任何请求都必须使用 SSL 进行发送。请注意，响应头中的 `ETag` 值*不是*对象的 MD5 散列，而是随机生成的 32 字节的十六进制字符串。

头|类型|描述
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm`|字符串|此头用于指定要与 `x-amz-server-side-encryption-customer-key` 头中存储的加密密钥配合使用的算法和密钥大小。此值必须设置为字符串 `AES256`。
`x-amz-server-side-encryption-customer-key`|字符串|此头用于传输在服务器端加密过程中所使用 AES 256 位密钥的 Base64 编码的字节字符串表示。
`x-amz-server-side-encryption-customer-key-MD5`|字符串|此头用于传输符合 RFC 1321 的加密密钥的 Base64 编码的 128 位 MD5 摘要。对象存储将使用此值来验证在 `x-amz-server-side-encryption-customer-key` 中传递的密钥在传输和编码过程中是否未损坏。必须在对密钥进行 Base64 编码之前，先对密钥计算摘要。


## 使用 {{site.data.keyword.keymanagementservicelong_notm}} 进行服务器端加密 (SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}} 是一个集中密钥管理系统 (KMS)，用于生成、管理和销毁 {{site.data.keyword.cloud_notm}} 服务使用的加密密钥。您可以通过 {{site.data.keyword.cloud_notm}}“目录”来创建 {{site.data.keyword.keymanagementserviceshort}} 的实例。

在要创建新存储区的区域中具有 {{site.data.keyword.keymanagementserviceshort}} 实例后，需要创建根密钥并记下该密钥的 CRN。

您可以选择仅在创建时使用 {{site.data.keyword.keymanagementserviceshort}} 来管理存储区的加密。无法更改现有存储区来使用 {{site.data.keyword.keymanagementserviceshort}}。
{:tip}

创建存储区时，需要提供其他头。

有关 {{site.data.keyword.keymanagementservicelong_notm}} 的更多信息，请参阅[此文档](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect)。

### SSE-KP 入门
{: #sse-kp-gs}

缺省情况下，{{site.data.keyword.cos_full}} 中存储的所有对象都使用多个随机生成的密钥以及全有或全无变换进行加密。虽然此缺省加密模型提供了静态安全性，但某些工作负载需要拥有所使用的加密密钥。您可以使用 [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) 来创建、添加和管理密钥，然后可以将这些密钥与 {{site.data.keyword.cos_full}} 的实例相关联以加密存储区。

### 开始之前
{: #sse-kp-prereqs}

您将需要：
  * [{{site.data.keyword.cloud}} Platform 帐户](http://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}} 的实例](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * [{{site.data.keyword.keymanagementservicelong_notm}} 的实例](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * 以及本地计算机上要上传的一些文件。

### 在 {{site.data.keyword.keymanagementserviceshort}} 中创建或添加密钥
{: #sse-kp-add-key}

导航至 {{site.data.keyword.keymanagementserviceshort}} 的实例，然后[生成或输入密钥](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)。

### 授予服务授权
{: #sse-kp}
授权 {{site.data.keyword.keymanagementserviceshort}} 以用于 IBM COS：

1. 打开 {{site.data.keyword.cloud_notm}} 仪表板。
2. 在菜单栏中，单击**管理** &gt; **帐户** &gt; **用户**。
3. 在侧边导航中，单击**身份和访问权** &gt; **授权**。
4. 单击**创建授权**。
5. 在**源服务**菜单中，选择 **Cloud Object Storage**。
6. 在**源服务实例**菜单中，选择要授权的服务实例。
7. 在**目标服务**菜单中，选择 **{{site.data.keyword.keymanagementservicelong_notm}}**。
8. 在**目标服务实例**菜单中，选择要授权的服务实例。
9. 启用**读取者**角色。
10. 单击**授权**。

### 创建存储区
{: #encryption-createbucket}

密钥存在于 {{site.data.keyword.keymanagementserviceshort}} 中，并且授权了 Key Protect 服务与 IBM COS 配合使用时，请将该密钥与新存储区相关联：

1. 导航至 {{site.data.keyword.cos_short}} 的实例。
2. 单击**创建存储区**。
3. 输入存储区名称，选择**区域**弹性，然后选择位置和存储类。
4. 在“高级配置”中，启用**添加 Key Protect 密钥**。
5. 选择关联的 Key Protect 服务实例、密钥和密钥标识。
6. 单击**创建**。

在**存储区和对象**列表中，现在存储区在**高级**下具有“密钥”图标，指示存储区启用了 Key Protect 密钥。要查看密钥详细信息，请单击存储区右侧的菜单，然后单击**查看 Key Protect 密钥**。

请注意，针对使用 SSE-KP 加密的对象返回的 `Etag` 值**将是**原始未加密对象的实际 MD5 散列。
{:tip}


## 轮换密钥
{: #encryption-rotate}

密钥轮换是缓解数据违规风险的重要一环。定期更改密钥可减少因密钥丢失或泄露而可能导致的数据丢失。密钥轮换的频率因组织而异，并取决于若干变量，包括环境、加密数据量、数据分类和合规性法律。[美国国家标准技术学会 (NIST)](https://www.nist.gov/topics/cryptography){:new_window} 提供了相应密钥长度的定义，并提供了关于密钥应该使用多长时间的准则。

### 手动密钥轮换
{: #encryption-rotate-manual}

要轮换 {{site.data.keyword.cos_short}} 的密钥，需要创建启用 Key Protect 且使用新根密钥的新存储区，然后将现有存储区中的内容复制到新存储区。

**注**：从系统中删除密钥将粉碎该密钥的内容以及仍使用该密钥加密的所有数据。除去密钥后，即无法撤销该操作，并且会导致永久数据丢失。

1. 在 [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial) 服务中，创建或添加新的根密钥。
2. [创建新存储区](#encryption-createbucket)并添加新的根密钥。
3. 将原始存储区中的所有对象复制到新存储区。
    1. 可以使用多种不同的方法来完成此步骤：
        1. 在命令行中使用 [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) 或 [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)
        2. 使用 (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object]
        3. 使用通过 [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 或 [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) 编写的 SDK
