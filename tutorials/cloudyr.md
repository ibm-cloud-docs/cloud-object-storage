---

copyright:
  years: 2019
lastupdated: "2019-11-14"

keywords: r, tutorial, cloudyr, data science

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

# Using cloudyr for data science
{: #cloudyr-data-science}

When using the [R programming language](https://www.r-project.org/about.html){: external} for your projects, get the most out of the features for supporting data science from {{site.data.keyword.cos_full}} by using [cloudyr](https://cloudyr.github.io){: external}.
{: shortdesc}

This tutorial will show you how to integrate data from the {{site.data.keyword.cloud}} Platform within your R project. Your project will use {{site.data.keyword.cos_full_notm}} for storage with S3-compatible connectivity in your project.

## Before you begin
{: #cloudyr-prereqs}

We need to make sure that we have the prerequisites we will need:

  - {{site.data.keyword.cloud_notm}} Platform account
  - An instance of {{site.data.keyword.cos_full}}
  - R installed and configured 
  - Git (both desktop and command line)
  - S3-compatible authentication configuration

## Create HMAC credentials
{: #cloudyr-hmac}

Users can create a set of HMAC credentials as part of a [Service Credential](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) with the configuration parameter `{"HMAC":true}` during credential creation. You can also use the {{site.data.keyword.cos_full_notm}} CLI as shown here. 
  
```bash
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{: pre}
  
If you want to store the results of the generated key, you can append ` > file.skey` to the end of the example. For the purposes of this instruction set, you need only find the `cos_hmac_keys` heading with child keys, `access_key_id`, and `secret_access_key`.
  
```
      cos_hmac_keys:
          access_key_id:      7exampledonotusea6440da12685eee02
          secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```
{: screen}

While it is best practices to set credentials in environment variables, you can also set your credentials inside your local copy of your R script itself.

You will need to set the actual values for the `access_key_id` and `secret_access_key` in your code along with the {{site.data.keyword.cos_full_notm}} endpoint for your instance.
{: note}

```r
Sys.setenv("AWS_ACCESS_KEY_ID" = "access_key_id",
           "AWS_SECRET_ACCESS_KEY" = "secret_access_key",
           "AWS_S3_ENDPOINT" = "myendpoint",
           "AWS_DEFAULT_REGION" = "us-south")
```
{: codeblock}
