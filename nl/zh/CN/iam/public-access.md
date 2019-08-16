---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# 允许公共访问
{: #iam-public-access}

有时提供数据的目的是为了共享。存储区可能会保存开放数据集，供学术和专用研究使用，或者保存图像存储库，供 Web 应用程序和内容交付网络使用。使用**公共访问**组可使这些存储区可供访问。
{: shortdesc}

## 使用控制台来设置公共访问
{: #iam-public-access-console}

首先，确保您有存储区。如果没有，请遵循[入门教程](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)以熟悉控制台。

### 启用公共访问
{: #public-access-console-enable}

1. 在 {{site.data.keyword.cloud_notm}} [控制台仪表板](https://cloud.ibm.com/)中，选择**存储**以查看资源列表。
2. 接下来，从**存储**菜单中选择具有您的存储区的服务实例。这将转至 {site.data.keyword.cos_short}} 控制台。
3. 选择您希望可供公共访问的存储区。请记住，此策略会使_存储区中的所有对象_可供任何人通过相应的 URL 进行下载。
4. 从导航菜单中，选择**访问策略**。
5. 选择**公共访问**选项卡。
6. 单击**创建访问策略**。阅读警告后，选择**启用**。
7. 现在，此存储区中的所有对象都可供公共访问！

### 禁用公共访问
{: #public-access-console-disable}

1. 在 {{site.data.keyword.cloud_notm}} [控制台](https://cloud.ibm.com/)中的任何位置，选择**管理**菜单，然后选择**访问权 (IAM)**。
2. 从导航菜单中，选择**访问组**。
3. 选择**公共访问**以查看当前正在使用的所有公共访问策略的列表。
4. 针对要返回到强制实施访问控制的存储区，找到对应的策略。
5. 从策略条目最右侧的操作列表中，选择**除去**。
6. 确认对话框，现在已将该策略从存储区中除去。

## 允许对单个对象设置公共访问
{: #public-access-object}

要通过 REST API 使某个对象可供公共访问，可以在请求中包含 `x-amz-acl: public-read` 头。设置此头会绕过所有 [IAM 策略](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)检查，并支持未经认证的 `HEAD` 和 `GET` 请求。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

此外，[HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)允许[使用预签名 URL 进行临时公共访问](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url)。

### 上传公共对象
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### 允许对现有对象进行公共访问
{: #public-access-object-existing}

使用不带有效内容和 `x-amz-acl: public-read` 头的查询参数 `?acl` 允许对对象进行公共访问，而无需覆盖数据。

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### 使公共对象再次成为专用对象
{: #public-access-object-private}

使用不带有效内容和空的 `x-amz-acl:` 头的查询参数 `?acl` 将撤销对对象的公共访问权，而无需覆盖数据。

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## 静态 Web 站点
{: #public-access-static-website}

虽然 {{site.data.keyword.cos_full_notm}} 不支持自动静态 Web 站点托管，但可以手动配置 Web 服务器，并将其用于提供在存储区中托管的可供公共访问的内容。有关更多信息，请参阅[本教程](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos)。
