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

# Heptio Ark Integration
{: #ark}
[Ark](https://github.com/heptio/ark){:new_window} is a toolset provided by [Heptio](https://heptio.com/){:new_window} to backup and restore your Kubernetes cluster resources.  Ark supports the use of S3-compatible storage providers including {{site.data.keyword.cos_full}} for different backup/snapshot operations.

Ark consists of two parts:

* Server component that runs on the cluster
* Command-line tool that runs on a local client

## Prerequisites
{: #ark-prereqs}

Before you begin you'll need to ensure you have the following:

* The [`IBM Cloud CLI`](https://cloud.ibm.com/docs/cli/index.html#overview){:new_window} installed
* The [`kubectl`](https://kubernetes.io/docs/user-guide/kubectl-overview/){:new_window} command-line tool installed and configured to connect to your cluster
* An {{site.data.keyword.cos_short}} instance 
* A {{site.data.keyword.cos_short}} bucket
* HMAC credentials with Writer access to bucket

## Install Ark Client
{: #ark-install}

1. Download the latest [release](https://github.com/heptio/ark/releases){:new_window} of Ark for your OS
2. Extract the tarball to a folder on your local system
3. Verify you can run the `ark` binary such as the following:

```
./ark --help
```

Output:

```
Heptio Ark is a tool for managing disaster recovery, specifically for Kubernetes
cluster resources. It provides a simple, configurable, and operationally robust
way to back up your application state and associated data.

If you're familiar with kubectl, Ark supports a similar model, allowing you to
execute commands such as 'ark get backup' and 'ark create schedule'. The same
operations can also be performed as 'ark backup get' and 'ark schedule create'.

Usage:
  ark [command]

Available Commands:
  backup            Work with backups
  backup-location   Work with backup storage locations
  bug               Report an Ark bug
  client            Ark client related commands
  completion        Output shell completion code for the specified shell (bash or zsh)
  create            Create ark resources
  delete            Delete ark resources
  describe          Describe ark resources
  get               Get ark resources
  help              Help about any command
  plugin            Work with plugins
  restic            Work with restic
  restore           Work with restores
  schedule          Work with schedules
  server            Run the ark server
  snapshot-location Work with snapshot locations
  version           Print the ark version and associated image
...
```

*(OPTIONAL)* Move the ark binary out of the temporary folder to somewhere more permanent such as `/usr/local/bin` on Mac OS or Linux.
{: tip}

## Install and Configure Ark Server
{: #ark-config}

### Create credentials file
{: #ark-config-credentials}

Create a credentials file (`credentials-ark`) using the HMAC pair of access and secret keys in your local Ark folder (*folder that the tarball was extracted*)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Configure kubectl
{: #ark-config-kubectl}

Configure [`kubectl`](https://kubernetes.io/docs/user-guide/kubectl-overview/){:new_window} to connect to your cluster.

1. Login to the IBM Cloud Platform using the CLI.<br/><br/>*For increased security, it's also possible to store the API key in a file or set it as an environment variable.*
    ```bash
    bx login --apikey <value>
    ```
    {: pre}
2. Retrieve the cluster configuration 
    ```bash
    bx cs cluster-config <cluster-name>
    ```
    {: pre}
3. Copy and paste the **export** command to set the KUBECONFIG environment variable

4. Ensure `kubectl` is configure correctly by running:
    ```bash
    kubectl cluster-config
    ```
    {: pre}
    Output:
    ```bash
    Kubernetes master is running at https://c6.hou02.containers.cloud.ibm.com:29244
    Heapster is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/heapster/proxy
    KubeDNS is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
    kubernetes-dashboard is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy
    ```
    {: pre}

### Configure Ark Server and Cloud Storage
{: #ark-config-storage}

1. In the Ark folder run the following to setup namespaces, RBAC, and other scaffolding<br/><br/>*The default namespace is `heptio-ark`.  If you wish to create a custom namespace, see the instructions at [Run in custom namespace](https://heptio.github.io/ark/v0.10.0/namespace.html){:new_window}*
    ```bash
    kubectl apply -f config/common/00-prereqs.yaml
    ```
    {: pre}
    Output:
    ```bash
    customresourcedefinition.apiextensions.k8s.io/backups.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/schedules.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/restores.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/downloadrequests.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/deletebackuprequests.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/podvolumebackups.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/podvolumerestores.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/resticrepositories.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/backupstoragelocations.ark.heptio.com created
    customresourcedefinition.apiextensions.k8s.io/volumesnapshotlocations.ark.heptio.com created
    namespace/heptio-ark created
    serviceaccount/ark created
    clusterrolebinding.rbac.authorization.k8s.io/ark created
    ```
    {: pre}
2. Create a Secret with your credentials file
    ```bash
    kubectl create secret generic cloud-credentials --namespace heptio-ark --from-file cloud=credentials-ark
    ```
    {: pre}
    Output:
    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. Specify the following values in `config/ibm/05-ark-backupstoragelocation.yaml`:
   * `<YOUR_BUCKET>` - Name of the bucket for storing backup files
   * `<YOUR_REGION>` - The [location constraint](/docs/services/cloud-object-storage/basics/classes.html#locationconstraint) of your bucket (i.e. `us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - The regional endpoint URL (i.e. `https://s3-api.us-geo.objectstorage.softlayer.net`)

    *See the [BackupStorageLocation](https://heptio.github.io/ark/v0.10.0/api-types/backupstoragelocation.html#aws){:new_window} definition for additional information.*

### Start the Ark Server
{: #ark-config-server}

1. In the Ark folder run the following command to create the object in your cluster:
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
    Output:
    ```bash
    backupstoragelocation.ark.heptio.com/default created
    ```
    {: pre}

2. Run the following command to create the deployment:
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}
    Output:
    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. Ensure deployment has been successfully scheduled using `kubectl get` on the `heptio-ark` namespace.  Once `Available` reads `1` Ark is ready to go:
    ```bash
    kubectl get deployments --namespace=heptio-ark
    ```
    {: pre}
    Output:
    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## Testing Backup/Restore
{: #ark-test}

### Backup
{: #ark-test-backup}

You can now perform a simple backup of your Kubernetes cluster by running the following command:
```bash
ark backup create <backup-name>
```
{: pre}

This will create a backup for every resource on the cluster, including persistent volumes.v$$

You can also restrict the backup to a particular namespace, resource type or label. 

Ark doesn’t allow to select by name, just by labels
{: tip}

This will backup only the components labeled with `app=<app-label>`. 
```bash
ark backup create <backup-name> --selector app=<app-label>
```
{: pre}

A full list of options is available by running:
```bash
ark backup --help
```
{: pre}

### Restore
{: #ark-test-restore}

To restore a backup run the following command:
```bash
ark restore create  --from-backup <backup-name>
```
{: pre}

A full list of options is available by running:
```bash
ark restore --help
```
{: pre}

