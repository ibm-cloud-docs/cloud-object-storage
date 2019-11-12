---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-11-11"

keywords: velero, kubernetes, integration, object storage

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
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

# Velero integration
{: #velero}
[Velero](https://github.com/heptio/velero){: external}(formerly Heptio Ark) is a toolset for backing up and restoring Kubernetes clusters by using the {{site.data.keyword.cos_full}} API.
{: .shortdesc}

Velero consists of two parts:

* Server component that runs on the cluster
* Command-line interface that runs on a local client

## Prerequisites
{: #velero-prereqs}

Before you begin, you need the following setup:

* The [`IBM Cloud CLI`](/docs/cli?topic=cloud-cli-getting-started){: external} installed
* The [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){: external} command-line tool that is installed and configured to connect to your cluster
* An {{site.data.keyword.cos_short}} instance
* A {{site.data.keyword.cos_short}} bucket
* HMAC credentials with Writer access to bucket

## Install Velero Client
{: #velero-install}

1. Download the most recent [version](https://github.com/heptio/velero/releases){: external} of Velero for your OS
2. Extract the .tar file to a folder on your local system
3. Verify you can run the `velero` binary:

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

*(OPTIONAL)* Move the ark binary out of the temporary folder to somewhere more permanent such as `/usr/local/bin` on Mac OS or Linux.
{: tip}

## Install and Configure Velero Server
{: #velero-config}
### Create credentials file
{: #velero-config-credentials}

Create a credentials file (`credentials-velero`) with the HMAC keys in your local Velero folder (*folder that the .tar file was extracted*)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Configure kubectl
{: #velero-config-kubectl}

Configure [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){: external} to connect to your cluster.

1. Log in to the IBM Cloud Platform by using the CLI.<br/><br/>*For increased security, it's also possible to store the API key in a file or set it as an environment variable.*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. Retrieve the cluster configuration 
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. Copy and paste the **export** command to set the KUBECONFIG environment variable

4. Ensure `kubectl` is configure correctly by running:
    ```bash
    kubectl cluster-info
    ```
    {: pre}
  
    ```bash
    Kubernetes master is running at https://c6.hou02.containers.cloud.ibm.com:29244
    Heapster is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/heapster/proxy
    KubeDNS is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
    kubernetes-dashboard is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy
    ```
    {: pre}

### Configure Velero Server and Cloud Storage
{: #velero-config-storage}

1. In the Velero folder, run the following to set up namespaces, RBAC, and other scaffolding<br/><br/>*The default namespace is `velero`. If you want to create a custom namespace, see the instructions at ['run in custom namespace'](https://heptio.github.io/velero/master/namespace.html){: external}*
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
2. Create a Secret with your credentials file
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. Specify the following values in `config/ibm/05-ark-backupstoragelocation.yaml`:
   * `<YOUR_BUCKET>` - Name of the bucket for storing backup files
   * `<YOUR_REGION>` - The [location constraint](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) of your bucket (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - The regional endpoint URL (`https://s3.us.cloud-object-storage.appdomain.cloud`). For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

    *For more information, see [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){: external} definition for additional information.*

### Start the Velero server
{: #velero-config-server}

1. In the Velero folder run the following command to create the object in your cluster:
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. Run the following command to create the deployment:
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. Ensure that deployment is successfully scheduled by using `kubectl get` on the `velero` namespace. When `Available` reads `1`, Ark is ready to go:
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## Testing back up and restore
{: #velero-test}

### Backup
{: #velero-test-backup}

You can now create a simple backup of your Kubernetes cluster by running the following command:
```bash
velero backup create <backup-name>
```
{: pre}

This command creates a backup for every resource on the cluster, including persistent volumes.

You can also restrict the backup to a particular namespace, resource type, or label.

Velero doesn’t allow selection by name, just by labels.
{: tip}

This command backs up only the components that are labeled with `app=<app-label>`. 
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

A full list of options is available by running:
```bash
velero backup --help
```
{: pre}

### Restore
{: #velero-test-restore}

To restore a backup, run the following command:
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

A full list of options is available by running:
```bash
velero restore --help
```
{: pre}

