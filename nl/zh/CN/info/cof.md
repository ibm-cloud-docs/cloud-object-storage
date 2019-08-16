---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# 将 Cloud Object Storage 与 Cloud Foundry 应用程序配合使用
{: #cloud-foundry}

{{site.data.keyword.cos_full}} 可以与 {{site.data.keyword.cfee_full}} 应用程序搭配使用，以通过区域和端点来提供高可用性内容。

## Cloud Foundry Enterprise Environment
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} 是用于在云中托管应用程序和服务的平台。您可以根据需要对在您自己的帐户中运行的多个隔离的企业级平台进行实例化，并且可以将这些平台部署在共享或专用硬件上。通过平台，可随着使用量的增长而轻松扩展应用程序，从而简化运行时和基础架构，让您可以专注于开发。

成功实施 Cloud Foundry 平台需要[适当的规划和设计](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation)，以满足必需的资源和企业需求。请在 Cloud Foundry Enterprise Environment [入门](/docs/cloud-foundry?topic=cloud-foundry-about#creating)以及入门[教程](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started)中了解更多相关信息。

### 区域
{: #cloud-foundry-regions}
[区域端点](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)是 IBM Cloud 环境的重要组成部分。您可以在具有相同 IBM Cloud 基础架构的不同区域中创建应用程序和服务实例以进行应用程序管理，并且创建相同的使用情况详细信息视图以用于计费。通过选择地理位置上靠近您或您客户的 IBM Cloud 区域，可以减少应用程序中的数据等待时间，并最大限度地降低成本。此外，还可以通过选择区域，解决任何安全问题或满足法规需求。 

使用 {{site.data.keyword.cos_full}} 时，可以通过[选择端点](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)以供应用程序在其中发送 API 请求，在单个数据中心、整个区域甚至区域组合中分布数据。

### 资源连接和别名
{: #cloud-foundry-aliases}

别名是资源组中受管服务与组织或空间中的应用程序之间的连接。别名类似于用于保存对远程资源的引用的符号链接。别名支持实例在整个平台上的互操作性和复用。在 {{site.data.keyword.cloud_notm}} 控制台中，连接（别名）表示为服务实例。您可以在资源组中创建服务实例，然后通过在这些区域的组织或空间中创建别名，从任何可用区域复用此实例。

## 将凭证存储为 VCAP 变量 
{: #cloud-foundry-vcap}

{{site.data.keyword.cos_short}} 凭证可以存储在 VCAP_SERVICES 环境变量中，在访问 {{site.data.keyword.cos_short}} 服务时可对该变量进行解析以使用凭证。凭证包含的信息如以下示例所示：

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

然后，可以在应用程序中解析 VCAP_SERVICES 环境变量，以便访问 {{site.data.keyword.cos_short}} 内容。下面是使用 Node.js 将环境变量与 COS SDK 相集成的示例。

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

有关如何通过代码示例使用 SDK 访问 {{site.data.keyword.cos_short}} 的更多信息，请访问：

* [使用 Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [使用 Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [使用 Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## 创建服务绑定 
{: #cloud-foundry-bindings}

### 仪表板
{: #cloud-foundry-bindings-console}

创建服务绑定的最简单方法是使用 [{{site.data.keyword.cloud}} 仪表板](https://cloud.ibm.com/resources)。 

1. 登录到[仪表板](https://cloud.ibm.com/resources)。
2. 单击 Cloud Foundry 应用程序。
3. 单击左侧菜单中的“连接”。
4. 单击右侧的**创建连接**。
5. 在*连接现有兼容服务*页面上，将鼠标悬停在 {{site.data.keyword.cos_short}} 服务上，然后单击**连接**。
6. 在*连接启用 IAM 的服务*弹出屏幕中，选择“访问角色”，对于“服务标识”，保留“自动生成”，然后单击**连接**。
7. 需要对 Cloud Foundry 应用程序重新编译打包，才能使用新的服务绑定。单击**重新编译打包**以启动该过程。
8. 重新编译打包完成后，Cloud Object Storage 服务即可供应用程序使用。

应用程序 VCAP_SERVICES 环境变量会自动使用服务信息进行更新。要查看新变量，请执行以下操作：

1. 单击右侧菜单中的*运行时*
2. 单击*环境变量*
3. 验证 COS 服务现在是否已列出

### IBM 客户机工具 (CLI)
{: #cloud-foundry-bindings-cli}

1. 使用 IBM Cloud CLI 进行登录
```
 ibmcloud login --apikey <your api key>
```

2. 将 Cloud Foundry 环境设定为目标
```
 ibmcloud target --cf
```

3. 为 {{site.data.keyword.cos_short}} 创建服务别名
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. 在 {{site.data.keyword.cos_short}} 别名和 Cloud Foundry 应用程序之间创建服务绑定，并为绑定提供角色。有效的角色为：<br/><ul><li>写入者</li><li>读取者</li><li>管理者</li><li>管理员</li><li>操作员</li><li>查看者</li><li>编辑者</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### 使用 HMAC 凭证的 IBM 客户机工具 (CLI)
{: #cloud-foundry-hmac}

基于散列的消息认证代码 (HMAC) 是一种机制，用于计算创建的使用访问密钥和私钥对的消息认证代码。此方法可用于验证消息的完整性和真实性。{{site.data.keyword.cos_short}} 文档中提供了有关使用 [HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)的更多信息。

1. 使用 IBM Cloud CLI 进行登录
```
 ibmcloud login --apikey <your api key>
```

2. 将 Cloud Foundry 环境设定为目标
```
 ibmcloud target --cf
```

3. 为 {{site.data.keyword.cos_short}} 创建服务别名
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. 在 {{site.data.keyword.cos_short}} 别名和 Cloud Foundry 应用程序之间创建服务绑定，并为绑定提供角色。<br/><br/>***注：**额外参数* (`{"HMAC":true}`)* 是创建启用 HMAC 的服务凭证所必需的。*<br/><br/>有效的角色为：<br/><ul><li>写入者</li><li>读取者</li><li>管理者</li><li>管理员</li><li>操作员</li><li>查看者</li><li>编辑者</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### 绑定到 {{site.data.keyword.containershort_notm}}
{: #cloud-foundry-k8s}

创建与 {{site.data.keyword.containershort}} 的服务绑定需要的过程略有不同。 

*对于此部分，您还需要安装 [jq - 轻量级命令行 JSON 处理器](https://stedolan.github.io/jq/){:new_window}。*

您需要以下信息并替换以下命令中的键值：

* `<service alias>` - COS 服务的新别名
* `<cos instance name>` - 现有 COS 实例的名称
* `<service credential name>` - 服务密钥/凭证的新名称
* `<role>` - 要连接到服务密钥的角色（请参阅上文以获取有效角色，最常指定的角色是 `Writer`）
* `<cluster name>` - 现有 Kubernetes 集群服务的名称
* `<secret binding name>` - COS 绑定到集群服务时，会生成此值


1. 为 COS 实例创建服务别名<br/><br/>***注：**COS 实例只能有一个服务别名*
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. 创建具有 COS 服务别名许可权的新服务密钥
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. 将集群服务绑定到 COS
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. 验证 COS 服务别名是否已绑定到集群
```
ibmcloud cs cluster-services --cluster <cluster name>
```
输出将类似于以下内容：
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. 检索集群中的私钥列表，并查找 COS 服务的私钥。通常，私钥为 `binding-`，后跟步骤 1 中指定的 `<service alias>`（即，`binding-sv-cos`）。在步骤 6 中将此值用作 `<secret binding name>`。
```
kubectl get secrets
```
输出应该类似于以下内容：
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. 验证 COS HMAC 凭证在集群私钥中是否可用
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
输出应该类似于以下内容：
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
