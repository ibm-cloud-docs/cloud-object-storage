---

copyright:
  years: 2017, 2018
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# For developers

First, ensure you have the [{{site.data.keyword.cloud}} Platform CLI](https://clis.ng.bluemix.net/ui/home.html) and [IBM Developer Tools](https://console.bluemix.net/docs/cloudnative/idt/index.html) installed.

## Provision an instance of {{site.data.keyword.cos_full_notm}}
  1. First, make sure you have an API key.  Get this from [IBM Cloud Identity and Access Management](https://www.bluemix.net/iam/#/apikeys).
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

There isn't an 'official' command line utility for managing data stored in {{site.data.keyword.cos_short}}, although S3 API compatible tools like the [AWS CLI](/docs/services/cloud-object-storage/cli/aws-cli.html) will work using [HMAC credentials](/docs/services/cloud-object-storage/hmac/credentials.html) for compatibility.  As IAM tokens are relatively easy to work with, `curl` is a good choice for basic testing and interaction with your storage.  More information can be found in [the `curl` reference](/docs/services/cloud-object-storage/cli/curl.html), as well as [the API reference documentation](/docs/services/cloud-object-storage/api-reference/about-api.html).

## Using libraries and SDKs

There are IBM COS SDKs available for [Python](/docs/services/cloud-object-storage/libraries/python.html), [Java](/docs/services/cloud-object-storage/libraries/java.html), and [Node.js](/docs/services/cloud-object-storage/libraries/node.html). These are forked versions of the AWS S3 SDKs that have been modified to support [IAM token-based authentication](/docs/services/cloud-object-storage/iam/overview.html), as well as support for [Key Protect](/docs/services/cloud-object-storage/basics/encryption.html). 
