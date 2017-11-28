---

copyright:
  years: 2017
lastupdated: "2017-11-05"

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Getting started (CLI)
In this quickstart guide, you'll create a bucket and upload objects, and set up access policies to allow other users to work with your data.
{: shortdesc}

## Before you begin
You'll need:
  * an [{{site.data.keyword.cloud}} Platform account](https://console.bluemix.net/registration/)
  * an [instance of {{site.data.keyword.cos_full}}](/docs/services/cloud-object-storage/basics/order-storage.html)
  * the [{{site.data.keyword.cloud_notm}} CLI](https://clis.ng.bluemix.net/ui/home.html)
  * and some files on your local computer to upload.
{: #prereqs}

Looking for a walkthrough that uses the console instead of the CLI? [Click here](/docs/services/cloud-object-storage/index.html).
{:tip}

## Gather key information
  1. First, make sure you have an API key.  Get this from [IBM Cloud Identity and Access Management](https://www.bluemix.net/iam/#/apikeys).
  2. Login to the {{site.data.keyword.cloud_notm}} Platform using the CLI.

For increased security, it's also possible to store the API key in a file or set it as an environment variable.
{:tip}

```
bx login --apikey <value>
```
{:codeblock}

```
Authenticating...
OK

Targeted account <account-name> (<account-id>)

Targeted resource group default


API endpoint:     https://api.ng.bluemix.net (API version: 2.75.0)
Region:           us-south
User:             <email-address>
Account:          <account-name> (<account-id>)
Resource group:   default
```

  4. Now you need the name and ID for your new instance. Use the name you gave the instance when creating it.

  ```
  bx resource service-instances -r global
  ```
  {:codeblock}

```
Retrieving resource instances in all regions under account <account-name> as <email-address>...
OK
Name                                               Region     State    Type
<resource-instance-name>                           global     active   service_instance
```

```
bx resource service-instance <instance-name> -r global
```
{:codeblock}

```
Retrieving resource instance docs-testing under account IBM as nicholas.lange@ibm.com...
OK

Name:                  <resource-instance-name>
ID:                    <resource-instance-id>
Region:                global
Resource Name:         cloud-object-storage
Resource Plan Name:    Standard
Resource Group Name:   default
State:                 active
Type:                  service_instance
```

  5. Next, get a token from IAM.

```
bx iam oauth-tokens
```
{:codeblock}

```
IAM token:  Bearer <token>
UAA token:  Bearer <refresh-token>
```

## Create a bucket and upload an object

  1. Take your new token, and the ID of the instance, and create a new bucket in the `us-south` region.

```sh
  curl -X "PUT" "https://s3.us-south.objectstorage.softlayer.net/<bucket-name>" \
       -H "Authorization: Bearer <token>" \
       -H "ibm-service-instance-id: <resource-instance-id>"
```
{:codeblock}

  2. Upload an object.

```sh
  curl -X "PUT" "https://s3.us-south.objectstorage.softlayer.net/<bucket-name>/<object-key>" \
       -H "Authorization: Bearer <token>" \
       -H "Content-Type: text/plain; charset=utf-8" \
       -d "This is a tiny object made of plain text."
```
{:codeblock}

## Manage access

  1. Invite someone to your account with minimal permissions.

```
bx account user-invite <email-address> <org-name> auditor <space-name> auditor
```
{:codeblock}

  2. Then grant them read-only access to your {{site.data.keyword.cos_short}} instances.

```
bx iam user-policy-create <email-address> --roles Reader --service-name cloud-object-storage
```
{:codeblock}

  3. Grant them write access to the bucket you created.

```
bx iam user-policy-create <email-address> --roles Writer --service-name cloud-object-storage --resource-type bucket --resource <bucket-name>
```
{:codeblock}

Want to learn more?  [Read more of the documentation](https://console.bluemix.net/docs/services/cloud-object-storage/about-cos.html).
