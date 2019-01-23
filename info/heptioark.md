---

copyright:
  years: 2019
lastupdated: "2019-01-22"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Heptio Ark Integration

[Ark](https://github.com/heptio/ark){:new_window} is a toolset provided by [Heptio](https://heptio.com/){:new_window} to backup and restore your Kubernetes cluster resources.  Ark supports the use of S3-compatible storage providers including {{site.data.keyword.cos_full}} for different backup/snapshot operations.

Ark consists of two parts:

* Server component that runs on the cluster
* Command-line tool that runs on a local client

## Prerequisites

Before you begin you'll need to ensure you have the following:

* The [`IBM Cloud CLI`](https://cloud.ibm.com/docs/cli/index.html#overview){:new_window} installed
* The [`kubectl`](https://kubernetes.io/docs/user-guide/kubectl-overview/){:new_window} command-line tool installed and configured to connect to your cluster
* An {{site.data.keyword.cos_short}} instance 
* A {{site.data.keyword.cos_short}} bucket
* HMAC credentials with Writer access to bucket

## Install Ark Client

1. Download the latest [release](https://github.com/heptio/ark/releases){:new_window} of Ark for your OS
2. Extract the tarball to a folder on your local system
3. Verify you can run the `ark` binary such as the following:

```
./ark --help
```

You should see the following output:

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

4. *(OPTIONAL)* Move the ark binary out of the temporary folder to somewhere more permanent such as `/usr/local/bin` on Mac OS or Linux.

## Install and Configure Ark Server

### Create credentials file

Create a credentials file (`credentials-ark`) using the HMAC pair of access and secret keys in your local Ark folder (*folder that the tarball was extracted*)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Configure kubectl

Configure [`kubectl`](https://kubernetes.io/docs/user-guide/kubectl-overview/){:new_window} to connect to your cluster.

1. Login to the IBM Cloud Platform using the CLI.<br/><br/>*For increased security, it's also possible to store the API key in a file or set it as an environment variable.*

```
bx login --apikey <value>
```

2. Retrieve the cluster configuration 

```
bx cs cluster-config <cluster-name>
```

3. Copy and paste the **export** command to set the KUBECONFIG environment variable

4. Ensure `kubectl` is configure correctly by running:

```
kubectl cluster-config
```
```
Kubernetes master is running at https://c6.hou02.containers.cloud.ibm.com:29244
Heapster is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/heapster/proxy
KubeDNS is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
kubernetes-dashboard is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy
```

### Configure Ark Server and Cloud Storage

1. In the Ark folder run the following to setup namespaces, RBAC, and other scaffolding<br/><br/>*The default namespace is `heptio-ark`.  If you wish to create a custom namespace, see the instructions at [Run in custom namespace](https://heptio.github.io/ark/v0.10.0/namespace.html){:new_window}*

```
kubectl apply -f config/common/00-prereqs.yaml
```
```
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

2. Create a Secret with your credentials file

```
kubectl create secret generic cloud-credentials --namespace heptio-ark --from-file cloud=credentials-ark
```

```
secret/cloud-credentials created
```

3. Specify the following values in `config/ibm/05-ark-backupstoragelocation.yaml`:
* `<YOUR_BUCKET>` - Name of the bucket for storing backup files
* `<YOUR_REGION>` - The [location constraint](/docs/services/cloud-object-storage/basics/classes.html#locationconstraint) of your bucket (i.e. `us-standard`)
* `<YOUR_URL_ACCESS_POINT>` - The regional endpoint URL (i.e. `https://s3-api.us-geo.objectstorage.softlayer.net`)

See the [BackupStorageLocation](https://heptio.github.io/ark/v0.10.0/api-types/backupstoragelocation.html#aws){:new_window} definition for additional information.

### Start the Ark Server

In the Ark folder run the following commands:

```
kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
```

```
backupstoragelocation.ark.heptio.com/default created
```

```
kubectl apply -f config/ibm/10-deployment.yaml
```

```
deployment.apps/ark created
```

Ensure deployment has been successfully scheduled using `kubectl get` on the `heptio-ark` namespace:

```
kubectl get deployments --namespace=heptio-ark
```

```
NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
ark    1         1         1            0           48s
```

## Testing Backup/Restore

### Backup

You can now perform a simple backup of your Kubernetes cluster by running the following command:

```
ark backup create <backup-name>
```

This will create a backup for every resource on the cluster, including persistent volumes.

You can also restrict the backup to a particular namespace, resource type or label. 

Ark doesn’t allow to select by name, just by labels
{:tip}

```
ark backup create <backup-name> --selector app=<app-label>
```

This will backup only the components labeled with `app=<app-label>`. 

A full list of options is available by running:

```
ark backup --help
```

### Restore

Ro restore a backup run the following command:

```
ark restore create  --from-backup <backup-name>
```

A full list of options is available by running:

```
ark restore --help
```
