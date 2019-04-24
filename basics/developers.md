---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# For developers
{: #gs-dev}
First, ensure you have the [{{site.data.keyword.cloud}} Platform CLI](https://cloud.ibm.com/docs/cli/index.html) and [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) installed.

## Provision an instance of {{site.data.keyword.cos_full_notm}}
{: #gs-dev-provision}

  1. First, make sure you have an API key.  Get this from [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam#/apikeys).
  2. Login to {{site.data.keyword.cloud_notm}} Platform using the CLI.  It's also possible to store the API key in a file or set it as an environment variable.

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. Next, provision an instance of {{site.data.keyword.cos_full_notm}} specifying the name for the instance, the ID and the desired plan (lite or standard).  This will get us the CRN.  If you have an upgraded account, specify the `Standard` plan.  Otherwise specify `Lite`.

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

The [Getting Started guide](/docs/services/cloud-object-storage/getting-started.html) walks through the basic steps of creating buckets and objects, as well as inviting users and creating policies.  A list of basic 'curl' commands can be found [here](/docs/services/cloud-object-storage/cli/curl.html).

Learn more about using the the {{site.data.keyword.cloud_notm}} CLI to create applications, manage Kubernetes clusters, and more [in the documentation](/docs/cli/reference/ibmcloud/bx_cli.html).


## Using the API
{: #gs-dev-api}

For managing data stored in {{site.data.keyword.cos_short}}, you can use S3 API compatible tools like the [AWS CLI](/docs/services/cloud-object-storage/cli/aws-cli.html)with [HMAC credentials](/docs/services/cloud-object-storage/hmac/credentials.html) for compatibility.  As IAM tokens are relatively easy to work with, `curl` is a good choice for basic testing and interaction with your storage.  More information can be found in [the `curl` reference](/docs/services/cloud-object-storage/cli/curl.html), as well as [the API reference documentation](/docs/services/cloud-object-storage/api-reference/about-api.html).

## Using libraries and SDKs
{: #gs-dev-sdk}
There are IBM COS SDKs available for [Python](/docs/services/cloud-object-storage/libraries/python.html), [Java](/docs/services/cloud-object-storage/libraries/java.html), and [Node.js](/docs/services/cloud-object-storage/libraries/node.html). These are forked versions of the AWS S3 SDKs that have been modified to support [IAM token-based authentication](/docs/services/cloud-object-storage/iam/overview.html), as well as support for [Key Protect](/docs/services/cloud-object-storage/basics/encryption.html). 

## Building applications on IBM Cloud
{: #gs-dev-apps}
{{site.data.keyword.cloud}} provides flexibility to developers in choosing the right architectural and deployment options for a given application.  Run your code on [bare metal](https://cloud.ibm.com/catalog/infrastructure/bare-metal), in [virtual machines](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), using a [serverless framework](https://cloud.ibm.com/openwhisk), in [containers](https://cloud.ibm.com/kubernetes/catalog/cluster), or using [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs).  

The [Cloud Native Computing Foundation](https://www.cncf.io) incubated and recently "graduated" the [Kubernetes](https://kubernetes.io) container orchestration framework, and it forms the foundation for the {{site.data.keyword.cloud}} Kubernetes Service.  Developers who wish to use object storage for persistent storage in their Kubernetes applications can learn more at the following links:

 * [Choosing a storage solution](/docs/containers/cs_storage_planning.html#choose_storage_solution)
 * [Comparison table for persistent storage options](/docs/containers/cs_storage_planning.html#persistent_storage_overview)
 * [Main COS page](/docs/containers/cs_storage_cos.html)
 * [Installing COS](/docs/containers/cs_storage_cos.html#install_cos)
 * [Creating COS service instance](/docs/containers/cs_storage_cos.html#create_cos_service)
 * [Creating COS secret](/docs/containers/cs_storage_cos.html#create_cos_secret)
 * [Decide on the configuration](/docs/containers/cs_storage_cos.html#configure_cos)
 * [Provision COS](/docs/containers/cs_storage_cos.html#add_cos)
 * [Backup and restore info](/docs/containers/cs_storage_cos.html#backup_restore)
 * [Storage Class reference](/docs/containers/cs_storage_cos.html#storageclass_reference)


