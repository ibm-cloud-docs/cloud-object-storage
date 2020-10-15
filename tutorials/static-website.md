---

copyright:
  years: 2020
lastupdated: "2020-10-22"

keywords: static website, hosting, tutorial 

subcollection: cloud-object-storage

content-type: tutorial

services: cloud-object-storage

account-plan: lite

completion-time: 10m

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
{:aws: .ph data-hd-programlang='aws'}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}
{:console: .ph data-hd-programlang='Console'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}
{:step: data-tutorial-type='step'}
{:hide-dashboard: .hide-dashboard}
{:apikey: `data-credential-placeholder`='apikey'}
{:url: data-credential-placeholder='url'}
{:username: data-credential-placeholder='username'}
{:password: data-credential-placeholder='password'}

# Building a Static Website
{: #static-website-tutorial}
{: toc-content-type="tutorial"}
{: toc-services="cloud-object-storage"}
{: toc-completion-time="10m"}

This tutorial shows how to host a static website on {{site.data.keyword.cos_full}}, including configuring a bucket, uploading content, and configuring your new website.
{: shortdesc}

Note that this is unreleased information and represents work in progress for the purpose of review.
{: note}

Hosting static websites with {{site.data.keyword.cos_full_notm}} serves static content for public access giving users flexibility, ease of delivery, and high availability.

This material represents work in progress and should not be considered final.
{: important}

## The Scenario
{: #wa-scenario}

The scenario for this tutorial simplifies web development to its essentials in order to highlight the steps involved. While not every configuration option will be covered in this tutorial, you should have web-accessible content when it's complete.

## Before you start
{: #static-website-before-you-start}

Ensure that you have what you need to start:

- {: hide-dashboard} An account for the {{site.data.keyword.cloud_notm}} Platform 
- Content in fixed form, like text (HTML would be perfect) and image files.

Check that you have the access as appropriate to either the instance of {{site.data.keyword.cos_full_notm}} you will be using or the proper [permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions) for the buckets you will be using for this tutorial. 
{: console}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to use [cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl).
{: http}

For use of the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli) with this tutorial, you will need to have the appropriate [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) for your use. Then, start your AWS CLI session at the command prompt with `aws config` where you paste the `access_key_id` and `secret_access_key` from your credentials at the appropriate prompts.
{: aws}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to [get started with NodeJS](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-gs&programming_language=javascript).
{: javascript}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to [get started with Java](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-gs&programming_language=java).
{: java}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials) keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to [get started with Python](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-gs&programming_language=python).
{: python}

## Create a bucket configured for public access
{: #static-website-create-public-bucket}

Creating a bucket for a static website will require public access. There are, of course, a number of options for configuring public access. Specifically, using the ObjectReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) will prevent the listing of the contents of the bucket while still allowing for the static content to be viewed on the internet. If you want to allow the viewing of the directory listing of your bucket, use the ContentReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam).  

## Upload content to your bucket
{: #static-website-upload-content}


## Configure the options for your website
{: #static-website-configure-options}


## Next steps
{: #static-website-next-steps}

The detailed description of configuration options for {{site.data.keyword.cos_full_notm}} hosted static websites can be found in the [API Documentation](https://cloud.ibm.com/apidocs/cos/cos-configuration){: external}.