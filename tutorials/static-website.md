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

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to use [cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl).
{: http}

For use of the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli) with this tutorial, you will need to have the appropriate [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) for your use. Then, start your AWS CLI session at the command prompt with `aws configure` where you paste the `access_key_id` and `secret_access_key` from your credentials at the appropriate prompts. Or, use the appropriate format for the files `~/.aws/config` and `~/.aws/credentials`. 
{: aws}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to [get started with NodeJS](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-gs&programming_language=javascript).
{: javascript}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to [get started with Java](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-gs&programming_language=java).
{: java}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to [get started with Python](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-gs&programming_language=python).
{: python}

## Create a bucket configured for public access
{: #static-website-create-public-bucket}

Creating a bucket for a static website will require public access. There are a number of options for configuring public access. Specifically, using the ObjectReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) will prevent the listing of the contents of the bucket while still allowing for the static content to be viewed on the internet. If you want to allow the viewing of the listing of the contents, use the ContentReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) for your bucket.

The compatibility layer of {{site.data.keyword.cos_full_notm}} allows for S3 operations, like using the command to create a bucket: `aws s3 mb` once you have configured your AWS CLI instance. In this tutorial, we'll use the configuration service represented by `aws s3api create-bucket`. Once you've chosen your [region and endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) as well as the [name of your bucket](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage#gs-create-buckets) replace the placeholder content as shown in the example command to create a bucket:
{: aws}

```
aws --endpoint-url=https://<endpoint> s3api create-bucket --bucket <bucketname>
```
{: pre}
{: aws}


## Upload content to your bucket
{: #static-website-upload-content}

The content of your hosted static website files focuses naturally on information and media. A popular approact to creating content for static websites are open source generators listed at [StaticGen](https://www.staticgen.com){: external}. For the purpose of this tutorial, we only need two files:

- An index page, typically written in HTML and named `index.html`, that loads by default for visitors to your site
- An error page, also in HTML and here named `error.html`, and typically loaded when a visitor tries to access a file that isn't present

Any other files, like images, PDFs, or videos can also be uploaded (but this tutorial will focus only on what is required).

The compatibility layer of {{site.data.keyword.cos_full_notm}} will provide the means to upload your content to your bucket. Replace the placeholder content as shown in the example command to upload your html files:
{: aws}

```
aws --endpoint-url=https://<endpoint> s3 cp /<local-path-to-directory-containing-files>/ s3://<bucketname>/ --recursive --include "*.html
```
{: pre}
{: aws}

For the rest of the tutorial, we will assume that the object key for the index page is `index.html` and the key for the error document is `error.html` although any appropriate key can be used.

## Configure the options for your website
{: #static-website-configure-options}

There are more options than this tutorial can describe, and for the purpose of this tutorial we only need to set the configuration to start using the static website feature.

The compatibility layer of {{site.data.keyword.cos_full_notm}} will provide the means to configure your new hosted static website. Replace the placeholder content as shown in the example command to configure the website:
{: aws}

```
aws --endpoint-url=https://<endpoint> s3 website s3://<bucketname>/ --index-document index.html --error-document error.html
```
{: pre}
{: aws}

Once you have configured your bucket to provide HTTP headers using the example command, all you have to do to test your new site is visit the URL as shown after replacing the placeholders with your own choices made previously in this tutorial:

```
http://<bucketname>.s3-website.<endpoint>/
```
{: screen}

With the successful testing of your new site, you can now explore more options and add more content.

## Next steps
{: #static-website-next-steps}

The detailed description of configuration options for {{site.data.keyword.cos_full_notm}} hosted static websites can be found in the [API Documentation](https://cloud.ibm.com/apidocs/cos/cos-configuration){: external}.