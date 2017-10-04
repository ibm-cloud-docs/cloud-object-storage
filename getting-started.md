---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# IBM Cloud Object Storage Quickstart
In this quickstart guide, you'll create a bucket and upload objects, and set up access policies to allow other users to work with your data.
{: shortdesc}

This documentation refers to IBM Cloud Object Storage provisioned as an IBM Cloud Platform service using the Bluemix console. This service uses IBM Cloud Identity and Access Management and is best suited for cloud-native application development.  Documentation for other object storage offerings, including the IaaS version of IBM COS (S3 API) and OpenStack Swift services, as well as more information on the evolution of object storage in the IBM cloud [is found here](/docs/services/cloud-object-storage/about-cos.html).
{:tip}

## Before you begin
You'll need:
  * a [Bluemix account](https://console.bluemix.net/registration/)
  * an [instance of Cloud Object Storage](/docs/services/cloud-object-storage/basics/order-storage.html)
  * the [Bluemix CLI](https://clis.ng.bluemix.net/ui/home.html)
  * and some files on your local computer to upload.
{: #prereqs}


## Gather key information
  1. First, make sure you have an API key.  Get this from [IBM Cloud Identity and Access Management](https://www.bluemix.net/iam/#/apikeys).
  2. Login to Bluemix using the CLI.

It's also possible to store the API key in a file or set it as an environment variable.
{:tip}

```
bx login --apikey <value>
```

  4. Now you need the ID for your new instance. Use the name you gave the instance when creating it.

```
bx resource instance <instance-name> -r global
```

  5. Next, get a token from IAM.

```
bx iam oauth-tokens
```

## Create a bucket and upload an object

  1. Take your new token, and the ID of the instance, and create a new bucket in the `us-south` region.

```
curl -X "PUT" "https://s3.us-south.objectstorage.softlayer.net/<bucket-name>" \
     -H "Authorization: Bearer <token>" \
     -H "ibm-service-instance-id: <resource-instance-id>"
```

  2. Upload an object.

```
  curl -X "PUT" "https://s3.us-south.objectstorage.softlayer.net/<bucket-name>/<object-key>" \
       -H "Authorization: Bearer <token>" \
       -H "Content-Type: text/plain; charset=utf-8" \
       -d "This is a tiny object made of plain text."
```

## Manage access

  1. Invite someone to your account with minimal permissions.

```
bx account user-invite <email-address> <org-name> auditor <space-name> auditor
```

  2. Then grant them read-only access to your COS instances.

```
bx iam user-policy-create <email-address> --roles AccessViewer --service-name cloud-object-storage
```

  3. Grant them write access to the bucket you created.

```
bx iam user-policy-create nglange@gmail.com --roles AccessEditor --service-name cloud-object-storage --resource-type bucket --resource <bucket-name>
```
