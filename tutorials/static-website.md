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
{:aws: .ph data-hd-programlang='aws cli'}
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

Hosting static websites with {{site.data.keyword.cos_full}} serves static content for public access giving users flexibility, ease of delivery, and high availability.

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

## Create a bucket configured for public access
{: #static-website-create-public-bucket}

Creating a bucket for a static website will require public access. There are, of course, a number of options for configuring public access. Specifically, using the ContentReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) will prevent the listing of the contents of the bucket while still allowing for the static content to be viewed on the internet. 

## Upload content to your bucket
{: #static-website-upload-content}

## Configure the options for your website
{: #static-website-configure-options}

## Next steps
{: #static-website-next-steps}

Learn more at the {{site.data.keyword.cloud_notm}} overview of [web development](https://www.ibm.com/cloud/websites){: external} and choose the path that is right for you.
