---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# 使用 HMAC 凭证
{: #hmac}

{{site.data.keyword.cos_full}} API 是基于 REST 的 API，用于读取和写入对象。它使用 {{site.data.keyword.iamlong}} 进行认证/授权，并且支持 S3 API 的子集以用于将应用程序轻松迁移到 {{site.data.keyword.cloud_notm}}。

除了基于 IAM 令牌的认证外，还可以[使用签名进行认证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)，签名通过一对访问密钥和私钥进行创建。这与 AWS Signature V4 的功能完全相同，并且 IBM COS 提供的 HMAC 密钥应该可使用与大多数与 S3 兼容的库和工具。

用户通过在凭证创建期间提供配置参数 `{"HMAC":true}` 来创建[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) 时，可以创建一组 HMAC 凭证。以下示例显示了如何使用 {{site.data.keyword.cos_full}} CLI 通过**写入者**角色（还有其他角色可能可用于您的帐户，并且可能最适合您的需求）使用 HMAC 凭证来创建服务密钥。 

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="示例 1. 使用 cURL 创建 HMAC 凭证。请注意使用了单引号和双引号。" caption-side="bottom"}

如果要存储刚才由示例 1 中的命令生成的密钥的结果，那么可以将 ` > file.skey` 附加到示例末尾。使用此指令集时，您只需要查找包含子键 `access_key_id` 和 `secret_access_key` 的 `cos_hmac_keys` 标题，这些子键是您需要的两个字段，如示例 2 中所示。

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="示例 2. 生成 HMAC 凭证时要注意的键。" caption-side="bottom"}

尤其要关注的是设置环境变量的能力（这些变量的指示信息特定于所涉及的操作系统）。例如，在示例 3 中，`.bash_profile` 脚本包含在启动 shell 时导出并用于开发的 `COS_HMAC_ACCESS_KEY_ID` 和 `COS_HMAC_SECRET_ACCESS_KEY`。

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="示例 3. 将 HMAC 凭证用作环境变量。" caption-side="bottom"}

创建服务凭证后，HMAC 密钥会包含在 `cos_hmac_keys` 字段中。然后，这些 HMAC 密钥会与[服务标识](/docs/iam?topic=iam-serviceids#serviceids)相关联，并且可用于访问服务标识的角色所允许的任何资源或操作。 

请注意，使用 HMAC 凭证来创建签名，以用于需要其他头的直接 [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) 调用时：
1. 所有请求都必须具有 `x-amz-date` 头，其中包含格式为 `%Y%m%dT%H%M%SZ` 的日期。
2. 具有有效内容的任何请求（对象上传、删除多个对象等）必须为 `x-amz-content-sha256` 头提供有效内容的 SHA256 散列。
3. 不支持 ACL（`public-read` 除外）。

目前，并非所有与 S3 兼容的工具都受支持。某些工具会尝试在创建存储区时设置除 `public-read` 以外的 ACL。通过这些工具创建存储区会失败。如果 `PUT bucket` 请求失败并返回“不支持的 ACL”错误，请首先使用[控制台](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)来创建存储区，然后将工具配置为对该存储区执行对象读写操作。目前不支持对对象写操作设置 ACL 的工具。
{:tip}
