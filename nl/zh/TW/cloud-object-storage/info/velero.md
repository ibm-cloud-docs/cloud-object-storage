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

# Velero 整合
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window}（早期為 Heptio Ark）是一個工具集，用於使用 S3 API 來備份及還原 Kubernetes 叢集。

Velero 由兩個部分組成：

* 在叢集上執行的伺服器元件
* 在本端用戶端上執行的指令行介面

## 必要條件
{: #velero-prereqs}

開始之前，您需要下列設定：

* 已安裝 [`IBM Cloud CLI`](/docs/cli?topic=cloud-cli-getting-started){:new_window}
* 已安裝並配置為連接至叢集的 [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} 指令行工具
* {{site.data.keyword.cos_short}} 實例
* {{site.data.keyword.cos_short}} 儲存區
* 具有儲存區「撰寫者」存取權的 HMAC 認證

## 安裝 Velero 用戶端
{: #velero-install}

1. 下載 OS 適用的 Velero 最新[版本](https://github.com/heptio/velero/releases){:new_window}
2. 將 .tar 檔案解壓縮至本端系統上的資料夾
3. 驗證您可以執行 `velero` 二進位檔：

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

*（選用）*將暫存資料夾中的 ark 二進位檔移出至更永久的位置，例如 Mac OS 或 Linux 上的 `/usr/local/bin`。
{: tip}

## 安裝與配置 Velero 伺服器
{: #velero-config}
### 建立認證檔
{: #velero-config-credentials}

在本端 Velero 資料夾（*解壓縮 .tar 檔案的資料夾*）中，使用 HMAC 金鑰建立認證檔 (`credentials-velero`)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### 配置 kubectl
{: #velero-config-kubectl}

配置 [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} 以連接至叢集。

1. 使用 CLI，登入 IBM Cloud Platform。<br/><br/>*為了增加安全性，也可以將 API 金鑰儲存在檔案中，或將它設為環境變數。*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. 擷取叢集配置
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. 複製並貼上 **export** 指令，以設定 KUBECONFIG 環境變數

4. 執行下列指令，確定已正確配置 `kubectl`：
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

### 配置 Velero 伺服器及 Cloud Storage
{: #velero-config-storage}

1. 在 Velero 資料夾中，執行下列指令以設定名稱空間、RBAC 及其他支撐<br/><br/>*預設名稱空間是 `velero`。如果您要建立自訂名稱空間，請參閱[在自訂名稱空間中執行](https://heptio.github.io/velero/master/namespace.html){:new_window}中的指示*
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
2. 使用認證檔來建立「密碼」
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. 在 `config/ibm/05-ark-backupstoragelocation.yaml` 中指定下列值：
   * `<YOUR_BUCKET>` - 用於儲存備份檔的儲存區名稱
   * `<YOUR_REGION>` - 儲存區的[位置限制](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - 地區端點 URL (`https://s3.us.cloud-object-storage.appdomain.cloud`)。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

    *如需相關資訊，請參閱 [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window} 定義以取得其他資訊。*

### 啟動 Velero 伺服器
{: #velero-config-server}

1. 在 Velero 資料夾中，執行下列指令，以在叢集中建立物件：
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. 執行下列指令，以建立部署：
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. 確定已在 `velero` 名稱空間上使用 `kubectl get` 順利排定部署。`Available` 顯示為 `1` 時，就可以執行 Ark：
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## 測試備份及還原
{: #velero-test}

### 備份
{: #velero-test-backup}

您現在可以執行下列指令來建立 Kubernetes 叢集的簡單備份：
```bash
velero backup create <backup-name>
```
{: pre}

這個指令會為叢集上的每個資源（包括持續性磁區）建立備份。

您也可以將備份限制為特定名稱空間、資源類型或標籤。

Velero 不容許依名稱進行選取，只會依標籤進行選取。
{: tip}

這個指令只會備份標示為 `app=<app-label>` 的元件。 
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

執行下列指令，以取得完整的選項清單：
```bash
velero backup --help
```
{: pre}

### 還原
{: #velero-test-restore}

若要還原備份，請執行下列指令：
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

執行下列指令，以取得完整的選項清單：
```bash
velero restore --help
```
{: pre}

