---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# For developers

First, ensure you have the [{{site.data.keyword.cloud}} Platform CLI](https://clis.ng.bluemix.net/ui/home.html) and [IBM Developer Tools](https://console.bluemix.net/docs/cloudnative/dev_cli.html) installed.

## Provision an instance of {{site.data.keyword.cos_full_notm}}
  1. First, make sure you have an API key.  Get this from [IBM Cloud Identity and Access Management](https://www.bluemix.net/iam/#/apikeys).
  2. Login to {{site.data.keyword.cloud_notm}} Platform using the CLI.  It's also possible to store the API key in a file or set it as an environment variable.

```
bx login --apikey <value>
```
{:codeblock}

  3. Next, provision an instance of {{site.data.keyword.cos_full_notm}} specifying the name for the instance, the ID and the desired plan (lite or standard).  This will get us the CRN.  If you have an upgraded account, specify the `Standard` plan.  Otherwise specify `Lite`.

```
bx resource service-instance-create <instance-name> cloud-object-storage <plan> -r global
```
{:codeblock}

The [Getting Started guide](/docs/services/cloud-object-storage/getting-started.html) walks through the basic steps of creating buckets and objects, as well as inviting users and creating policies.  A list of basic 'curl' commands can be found [here](/docs/services/cloud-object-storage/cli/curl.html).

Learn more about using the the {{site.data.keyword.cloud_notm}} CLI to create applications, manage Kubernetes clusters, and more [in the documentation](/docs/cli/reference/bluemix_cli/bx_cli.html).


## Using the API

There isn't a focused command line utility for managing data stored in {{site.data.keyword.cos_short}}.  As IAM tokens are relatively easy to work with, `curl` is a good choice for basic testing and interaction with your storage.  More information can be found in [the `curl` reference](/docs/services/cloud-object-storage/cli/curl.html), as well as [the API reference documentation](/docs/services/cloud-object-storage/api-reference/about-compatibility-api.html).
