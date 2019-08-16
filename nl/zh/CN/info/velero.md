---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: heptio, kubernetes, backup

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

# Velero 集成
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window}（原名为 Heptio Ark）是一个工具集，用于使用 S3 API 来备份和复原 Kubernetes 集群。

Velero 由两部分组成：

* 在集群上运行的服务器组件
* 在本地客户机上运行的命令行界面

## 先决条件
{: #velero-prereqs}

开始之前，需要以下设置：

* 已安装 [`IBM Cloud CLI`](/docs/cli?topic=cloud-cli-getting-started){:new_window}
* 已安装 [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} 命令行工具，并将其配置为连接到集群
* {{site.data.keyword.cos_short}} 实例
* {{site.data.keyword.cos_short}} 存储区
* 具有对存储区的“写入者”访问权的 HMAC 凭证

## 安装 Velero 客户机
{: #velero-install}

1. 下载适用于您操作系统的最新 Velero [版本](https://github.com/heptio/velero/releases){:new_window}
2. 将 .tar 文件解压缩到本地系统上的文件夹
3. 验证是否可以运行 `velero` 二进制文件：

```
velero --help
```

```
Velero is a tool for managing disaster recovery, specifically for Kubernetes
cluster resources. It provides a simple, configurable, and operationally robust
way to back up your application state and associated data.

If you're familiar with kubectl, Velero supports a similar model, allowing you to
execute commands such as 'velero get backup' and 'velero create schedule'. The same
operations can also be performed as 'velero backup get' and 'velero schedule create'.

Usage:
  velero [command]

Available Commands:
  backup            Work with backups
  backup-location   Work with backup storage locations
  bug               Report an velero bug
  client            Velero client related commands
  completion        Output shell completion code for the specified shell (bash or zsh)
  create            Create velero resources
  delete            Delete velero resources
  describe          Describe velero resources
  get               Get velero resources
  help              Help about any command
  plugin            Work with plugins
  restic            Work with restic
  restore           Work with restores
  schedule          Work with schedules
  server            Run the velero server
  snapshot-location Work with snapshot locations
  version           Print the velero version and associated image
...
```

*（可选）*将 ark 二进制文件从临时文件夹移至更永久的位置，例如 `/usr/local/bin`（在 Mac OS 或 Linux 上）。
{: tip}

## 安装和配置 Velero 服务器
{: #velero-config}
### 创建凭证文件
{: #velero-config-credentials}

在本地 Velero 文件夹（*.tar 文件解压缩到的文件夹*）中创建具有 HMAC 密钥的凭证文件 (`credentials-velero`)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### 配置 kubectl
{: #velero-config-kubectl}

配置 [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} 以连接到集群。

1. 使用 CLI 登录到 IBM Cloud Platform。<br/><br/>*为了提高安全性，还可以将 API 密钥存储在文件中，或将其设置为环境变量。*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. 检索集群配置
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. 复制并粘贴 **export** 命令以设置 KUBECONFIG 环境变量

4. 通过运行以下命令，确保 `kubectl` 已正确配置：
    ```bash
    kubectl cluster-config
    ```
    {: pre}
  
    ```bash
    Kubernetes master is running at https://c6.hou02.containers.cloud.ibm.com:29244
    Heapster is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/heapster/proxy
    KubeDNS is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
    kubernetes-dashboard is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy
    ```
    {: pre}

### 配置 Velero 服务器和 Cloud Storage
{: #velero-config-storage}

1. 在 Velero 文件夹中，运行以下命令以设置名称空间、RBAC 和其他脚手架<br/><br/>*缺省名称空间为 `velero`。如果要创建定制名称空间，请参阅[“run in custom namespace”](https://heptio.github.io/velero/master/namespace.html){:new_window}*中的指示信息
    ```bash
    kubectl apply -f config/common/00-prereqs.yaml
    ```
    {: pre}

    ```bash
    customresourcedefinition.apiextensions.k8s.io/backups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/schedules.velero.io created
    customresourcedefinition.apiextensions.k8s.io/restores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/downloadrequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/deletebackuprequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumebackups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumerestores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/resticrepositories.velero.io created
    customresourcedefinition.apiextensions.k8s.io/backupstoragelocations.velero.io created
    customresourcedefinition.apiextensions.k8s.io/volumesnapshotlocations.velero.io created
    namespace/velero created
    serviceaccount/velero created
    clusterrolebinding.rbac.authorization.k8s.io/velero created
    ```
    {: pre}
2. 使用凭证文件创建私钥
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. 在 `config/ibm/05-ark-backupstoragelocation.yaml` 中指定以下值：
   * `<YOUR_BUCKET>` - 用于存储备份文件的存储区的名称
   * `<YOUR_REGION>` - 存储区的[位置约束](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - 区域端点 URL (`https://s3.us.cloud-object-storage.appdomain.cloud`)。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

    *有关更多信息，请参阅 [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window} 定义以获取其他信息。*

### 启动 Velero 服务器
{: #velero-config-server}

1. 在 Velero 文件夹中，运行以下命令以在集群中创建对象：
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. 运行以下命令以创建部署：
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. 对 `velero` 名称空间使用 `kubectl get` 以确保部署已成功安排。`Available` 显示为 `1` 时，即说明 Ark 已准备就绪：
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## 测试备份和复原
{: #velero-test}

### 备份
{: #velero-test-backup}

现在，可以通过运行以下命令来创建 Kubernetes 集群的简单备份：
```bash
velero backup create <backup-name>
```
{: pre}

此命令为集群上的每个资源（包括持久卷）创建备份。

您还可以将备份的对象限制为特定名称空间、资源类型或标签。

Velero 不允许按名称进行选择，仅支持按标签进行选择。
{: tip}

此命令仅备份标注有 `app=<app-label>` 的组件。 
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

通过运行以下命令，可提供选项的完整列表：
```bash
velero backup --help
```
{: pre}

### 复原
{: #velero-test-restore}

要复原备份，请运行以下命令：
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

通过运行以下命令，可提供选项的完整列表：
```bash
velero restore --help
```
{: pre}

