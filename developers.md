---

copyright:
  years: 2019, 2020
lastupdated: "2020-06-19"

keywords: developer, getting started, command line interface, cli

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}


# For developers of {{site.data.keyword.cos_full_notm}}
{: #gs-dev}

The powerful tools of {{site.data.keyword.cos_full}} are available to a developer directly from the command line.
{: shortdesc}

First, ensure that you have the [{{site.data.keyword.cloud}} Platform CLI](https://cloud.ibm.com/docs/cli/index.html) and [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) installed.

## Create an instance of {{site.data.keyword.cos_full_notm}}
{: #gs-dev-provision}

1. First, make sure that you have an API key. Get it from [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys).
2. Log in to {{site.data.keyword.cloud_notm}} Platform by using the CLI. It's also possible to store the API key in a file or set it as an environment variable.

    ```
    ibmcloud login --apikey <value>
    ```
    {: pre}

3. Next, create an instance of {{site.data.keyword.cos_full_notm}} specifying the name for the instance, the ID, and the wanted plan (`lite` or `standard`). Now we have a CRN for the instance. If you have an upgraded account, specify the `standard` plan. Otherwise, specify `lite`.

    ```
    ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
    ```
    {: pre}

Some users may encounter the error `No resource group targeted` when trying to create a new instance. This indicates that the default resource group is not available and that a resource group must be explicitly set. A list of available resource groups can be found using `ibmcloud resource groups` and the target can be set with `ibmcloud target -g <resource-group>`.
{: tip}

The [Getting Started guide](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) walks through the basic steps of creating buckets and objects, as well as inviting users and creating policies. A list of basic 'curl' commands can be found [here](/docs/cloud-object-storage/cli?topic=cloud-object-storage-curl).

Learn more about using the {{site.data.keyword.cloud_notm}} CLI to create applications, manage Kubernetes clusters, and more [in the documentation](/docs/cli?topic=cli-ibmcloud_cli).


## Using the API
{: #gs-dev-api}

For managing data stored in {{site.data.keyword.cos_short}}, you can use S3 API compatible tools like the [AWS CLI](/docs/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)with [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) for compatibility. As IAM tokens are relatively easy to work with, `curl` is a good choice for basic testing and interaction with your storage. More information can be found in [the `curl` reference](/docs/cloud-object-storage/cli?topic=cloud-object-storage-curl), as well as [the API reference documentation](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Using libraries and SDKs
{: #gs-dev-sdk}
IBM COS SDKs are available for [Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go), and [Node.js](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-node). These libraries are forked and modified versions of the AWS S3 SDKs that support [IAM token-based authentication](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview), as well as support for [Key Protect](/docs/cloud-object-storage/basics?topic=cloud-object-storage-encryption). 

## Building applications on IBM Cloud
{: #gs-dev-apps}
{{site.data.keyword.cloud}} provides flexibility to developers in choosing the right architectural and deployment options for a given application. Run your code on [bare metal](https://cloud.ibm.com/catalog/infrastructure/bare-metal), in [virtual machines](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), by using a [serverless framework](https://cloud.ibm.com/openwhisk), in [containers](https://cloud.ibm.com/kubernetes/catalog/cluster), or by using [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs). 

The [Cloud Native Computing Foundation](https://www.cncf.io) fostered [Kubernetes](https://kubernetes.io) container orchestration framework, which forms the foundation for the {{site.data.keyword.cloud}} Kubernetes Service. Developers who want to use Object Storage for persistent storage in their Kubernetes applications can learn more at the following links:

 * [Choosing a storage solution](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [Comparison table for persistent storage options](/docs/containers?topic=containers-storage-plan)
 * [Main COS page](/docs/containers?topic=containers-object_storage)
 * [Installing COS](/docs/containers?topic=containers-object_storage#install_cos)
 * [Creating COS service instance](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [Creating COS secret](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [Decide on the configuration](/docs/containers?topic=containers-object_storage#configure_cos)
 * [Create an instance of COS](/docs/containers?topic=containers-object_storage#add_cos)
 * [Back up and restore information](/docs/containers?topic=containers-object_storage#cos_backup_restore)
 * [Storage Class reference](/docs/containers?topic=containers-object_storage#configure_cos)


