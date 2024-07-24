---

copyright:
  years: 2019, 2024
lastupdated: "2024-07-24"

keywords: developer, getting started, command line interface, cli

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}


# For developers
{: #gs-dev}

The powerful features of {{site.data.keyword.cos_full}} are available to a developer directly from the command line.
{: shortdesc}

First, ensure that you have the [{{site.data.keyword.cloud}} Platform CLI](https://cloud.ibm.com/docs/cli/index.html) and [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) installed.

## Create an instance of {{site.data.keyword.cos_full_notm}}
{: #gs-dev-provision}

1. First, make sure that you have an API key. Get it from [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys).
2. Log in to {{site.data.keyword.cloud_notm}} Platform by using the CLI. It's also possible to store the API key in a file or set it as an environment variable.

    ```sh
    ibmcloud login --apikey <value>
    ```
    {: pre}

3. Next, create an instance of {{site.data.keyword.cos_full_notm}} specifying the name for the instance and the Standard plan (see [Choosing a plan and creating an instance](/docs/cloud-object-storage?topic=cloud-object-storage-provision)). Now you have a CRN for the instance.

    ```sh
    ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
    ```
    {: pre}

When trying to create a new instance, if you encounter the error `No resource group targeted`, it indicates that the default resource group is not available and that a resource group must be explicitly set. A list of available resource groups can be found using `ibmcloud resource groups` and the target can be set with `ibmcloud target -g <resource-group>`.
{: tip}

The [Getting Started guide](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) walks through the basic steps of creating buckets and objects, as well as inviting users and creating policies. A list of basic 'curl' commands can be found [here](/docs/cloud-object-storage?topic=cloud-object-storage-curl).

Learn more about using the {{site.data.keyword.cloud_notm}} CLI to create applications, manage Kubernetes clusters, and more [in the documentation](/docs/cli?topic=cli-ibmcloud_cli).

## Using the API
{: #gs-dev-api}

For managing data stored in {{site.data.keyword.cos_short}}, you can use S3 API compatible tools like the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli) with [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) for compatibility. As IAM tokens are relatively easy to work with, `curl` is a good choice for basic testing and interaction with your storage. More information can be found in the [`curl`](/docs/cloud-object-storage?topic=cloud-object-storage-curl) and the [API](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api) documentation.

## Using libraries and SDKs
{: #gs-dev-sdk}

IBM COS SDKs are available for [Python](/docs/cloud-object-storage?topic=cloud-object-storage-python), [Java](/docs/cloud-object-storage?topic=cloud-object-storage-java), [Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go), and [Node.js](/docs/cloud-object-storage?topic=cloud-object-storage-node). These libraries are forked and modified versions of the AWS S3 SDKs that support [IAM token-based authentication](/docs/cloud-object-storage?topic=cloud-object-storage-iam-overview), as well as support for [Key Protect](/docs/cloud-object-storage?topic=cloud-object-storage-encryption).

## Building applications on IBM Cloud
{: #gs-dev-apps}

{{site.data.keyword.cloud}} provides flexibility to developers in choosing the right architectural and deployment options for a given application. Run your code on [bare metal](https://cloud.ibm.com/catalog/infrastructure/bare-metal), in [virtual machines](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), in [containers](https://cloud.ibm.com/kubernetes/catalog/cluster), or by using a [serverless framework](/docs/solution-tutorials?topic=solution-tutorials-serverless-webapp).

The [Cloud Native Computing Foundation](https://www.cncf.io) fostered [Kubernetes](https://kubernetes.io) container orchestration framework, which forms the foundation for the {{site.data.keyword.cloud}} Kubernetes Service. Developers who want to use Object Storage for persistent storage in their Kubernetes applications can learn more at the following links:

 * [Choosing a storage solution](/docs/containers?topic=containers-storage-plan)
 * [Comparison table for persistent storage options](/docs/containers?topic=containers-storage-plan)
 * [Main COS page](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage)
 * [Installing COS](/docs/containers?topic=containers-storage_cos_install)
 * [Creating COS service instance](/docs/containers?topic=containers-storage-cos-understand#create_cos_service)
 * [Decide on the configuration](/docs/containers?topic=containers-storage_cos_install#configure_cos)
 * [Creating a COS Kubernetes secret](/docs/containers?topic=containers-storage-cos-understand#create_cos_secret)
 * [Kubernetes back up and restore information](/docs/containers?topic=containers-storage_br)
 * [Kubernetes Storage Class reference](/docs/containers?topic=containers-storage_cos_reference)
