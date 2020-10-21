---

copyright:
  years: 2020
lastupdated: "2020-10-22"

keywords: static website, hosting, tutorial 

subcollection: cloud-object-storage

content-type: tutorial

services: cloud-object-storage

account-plan: lite

completion-time: 15m

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
{:console: .ph data-hd-programlang='Console'}
{:http: .ph data-hd-programlang='curl'}
{:aws: .ph data-hd-programlang='aws'}
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
{: toc-completion-time="15m"}

This tutorial shows how to host a static website on {{site.data.keyword.cos_full}}, including configuring a bucket, uploading content, and configuring your new website.
{: shortdesc}

Note that this is unreleased information and represents work in progress for the purpose of review.
{: important}

Hosting static websites with {{site.data.keyword.cos_full_notm}} serves static content for public access giving users flexibility, ease of delivery, and high availability.

## The Scenario
{: #static-website-scenario}

The scenario for this tutorial simplifies web development to its essentials in order to highlight the steps involved. While not every configuration option will be covered in this tutorial, correctly completing this tutorial results in web-accessible content.

## Before you start
{: #static-website-before-you-start}

Ensure that you have what you need to start:

- {: hide-dashboard} An account for the {{site.data.keyword.cloud_notm}} Platform 
- An instance of {{site.data.keyword.cos_full_notm}}
- Content in fixed form, like text (HTML would be perfect) and image files.

Check that you have the access as appropriate to either the instance of {{site.data.keyword.cos_full_notm}} you will be using or the proper [permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions) for the buckets you will be using for this tutorial. 
{: console}

Once you have your [credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), keep them handy as appropriate for your task. If this is your first time working with {{site.data.keyword.cos_full_notm}}, please review how to use [cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl). Preparing to authorize your API calls typically starts with getting an [IAM bearer token](/docs/cloud-object-storage?topic=cloud-object-storage-curl#curl-iam). 
{: http}

For use of the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli) with this tutorial, you will need to have the appropriate [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) for your use. Then, start your AWS CLI session at the command prompt with `aws configure` where you paste the `access_key_id` and `secret_access_key` from your credentials at the appropriate prompts. Or, use the appropriate format for the files `~/.aws/config` and `~/.aws/credentials`. 
{: aws}

## Create a bucket configured for public access
{: #static-website-create-public-bucket}

Creating a bucket for a static website will require public access. There are a number of options for configuring public access. Specifically, using the ObjectReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) will prevent the listing of the contents of the bucket while still allowing for the static content to be viewed on the internet. If you want to allow the viewing of the listing of the contents, use the ContentReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) for your bucket.

### Create a bucket
{: #static-website-create-bucket}

In working with `cURL` you will need to start by choosing a [region and endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) as well as the [name of your bucket](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage#gs-create-buckets). In addition to the bearer token or other authorization headers, keep those choices handy for this tutorial. You will need them to replace the placeholder content as shown in the example command to create a bucket:
{: http}

```bash
curl --location --request PUT 'https://<endpoint>/<bucketname>' \
--header 'Authorization: bearer <token>' --header 'ibm-service-instance-id: <resource_instance_id>
```
{: pre}
{: http}

When the command succeeds, you will get an HTTP response of `200 OK` from the endpoint handling the request.
{: http}

The compatibility layer of {{site.data.keyword.cos_full_notm}} allows for S3 operations, like using the command to create a bucket: `aws s3 mb` once you have configured your AWS CLI instance. In this tutorial, we'll use the configuration service represented by `aws s3api create-bucket`. Once you've chosen your [region and endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) as well as the [name of your bucket](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage#gs-create-buckets) replace the placeholder content as shown in the example command to create a bucket:
{: aws}

```
aws --endpoint-url=https://<endpoint> s3api create-bucket --bucket <bucketname>
```
{: pre}
{: aws}

Once you login to the Console and after you create an instance of {{site.data.keyword.cos_full_notm}}, you can create a bucket. Click on the button labeled "Create bucket" and choose from the options as shown in Figure 1. Select the card that reads "Host a Static Website."
{: console}

![Select Static Website option](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-bucket-cards.jpg){: caption="Figure 1. Choose Host a Static Website"}
{: console}

The container for the static files in your website will reside in a bucket that you can name. The name you create must be unique, should not contain personal or identifying information, can't have two periods, dots, or hyphens in a row, and must start and end with alphanumeric characters (ASCII character set items 3&ndash;63). See Figure 2 for an example.
{: console}

![Name bucket for Static Website](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-bucketname.jpg){: caption="Figure 2. Type a unique name for your bucket"}
{: console}

### Setting public access
{: #static-website-public-access}

In all scenarios for this tutorial, you will want to use the [UI at the Console](https://cloud.ibm.com/login){: external} to allow [public access](/docs/cloud-object-storage?topic=cloud-object-storage-iam-public-access) to your new website.

When creating a bucket for hosting Static Website content, there is an option to enable public access as part of the bucket creation process. See Figure 3 for the option to enable public access to your bucket. For the explanation of the options for the "index document" and "error document" as shown, find more below in the section [Configure the options for your website](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial#static-website-configure-options). You may complete the basic configuration with this step, before uploading content to your bucket as shown in the next step.
{: console}

![Enable public access](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-basic-config.jpg){: caption="Figure 3. Enable public access"}
{: console}

## Upload content to your bucket
{: #static-website-upload-content}

The content of your hosted static website files focuses naturally on information and media. A popular approach to creating content for static websites are open source generators listed at [StaticGen](https://www.staticgen.com){: external}. For the purpose of this tutorial, we only need two files:

- An index page, typically written in HTML and named `index.html`, that loads by default for visitors to your site
- An error page, also in HTML and here named `error.html`, and typically loaded when a visitor tries to access a file that isn't present

Other files, like images, PDFs, or videos, can also be uploaded to your bucket (but this tutorial will focus only on what is required).

For the `index.html` file, we can use `curl` to upload a simple file with a single command. Please note you may have to refresh your token if it has expired.
{: http}

```
curl --location --request PUT 'https://<endpoint>/<bucketname>/index.html' \
--header 'Authorization: bearer <token>' --header 'ibm-service-instance-id: <resource_instance_id> --header 'Content-Type: text/html' \
--data-raw '<html><head><title>Index</title></head><body><h1>Index</h1></body></html>'
```
{: pre}
{: http}

For the `error.html` file, we can also upload the file with a single command. Please note that all the HTTPS calls use the endpoint you chose earlier.
{: http}

```
curl --location --request PUT 'https://<endpoint>/<bucketname>/index.html' \
--header 'Authorization: bearer <token>' --header 'ibm-service-instance-id: <resource_instance_id> --header 'Content-Type: text/html' \
--data-raw '<html><head><title>Error</title></head><body><h1>Error</h1></body></html>'
```
{: pre}
{: http}

When each upload completes, you will get an HTTP response of `200 OK` from the endpoint handling the request.
{: http}

For the purpose of this tutorial, place the HTML pages for the index and error handling in a local directory. The compatibility layer of {{site.data.keyword.cos_full_notm}} will provide the means to upload your content to your bucket. Replace the placeholder content as shown in the example command to upload your html files:
{: aws}

```
aws --endpoint-url=https://<endpoint> s3 cp /<local-path-to-directory-containing-files>/ s3://<bucketname>/ --recursive --include "*.html
```
{: pre}
{: aws}

You may have already completed the basic configuration for hosting your static website. Files can be uploaded directly in the Console once you've named and configured your bucket. Note the step is optional as shown in Figure 4, and can occur at any point before the testing of your new hosted website.
{: console}

![Upload files](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-upload-files.jpg){: caption="Figure 4. Upload files"}
{: console}

For the rest of the tutorial, we will assume that the object key for the index page is `index.html` and the key for the error document is `error.html` although any appropriate filename can be used for the suffix or key.

## Configure the options for your website
{: #static-website-configure-options}

There are more options than this tutorial can describe, and for the purpose of this tutorial we only need to set the configuration to start using the static website feature.

Configuring the bucket to be a static website using `cURL` starts with the parameter `?website` as shown later. To configure the website, you will need to create specific XML for the setting and then generate an MD5 hash of the XML you created. Use the excerpt from the sample as a start.
{: http}

```xml
<WebsiteConfiguration>
    <IndexDocument>
        <Suffix>index.html</Suffix>
    </IndexDocument>
    <ErrorDocument>
        <Key>error.html</Key>
    </ErrorDocument>
</WebsiteConfiguration>
```
{: pre}
{: http}

The `Content-MD5` header needs to be the binary representation of a base64-encoded MD5 hash. Note that the quotes encapsulate multi-line input (as in this XML example).
{: http}

```
echo -n "XML block" | openssl dgst -md5 -binary | openssl enc -base64
``` 
{: pre}
{: http}
 
Use the content you've generated to replace the placeholder content as shown in the example command to create a bucket:
{: http}

```bash
curl --location --request PUT 'https://<endpoint>/<bucketname>?website' \
--header 'Authorization: bearer <token>' --header 'ibm-service-instance-id: <resource_instance_id> \
--header 'Content-MD5: <hashed-output>' --header 'Content-Type: text/plain' \
--data-raw '<WebsiteConfiguration>
    <IndexDocument>
        <Suffix>index.html</Suffix>
    </IndexDocument>
    <ErrorDocument>
        <Key>error.html</Key>
    </ErrorDocument>
</WebsiteConfiguration>'

```
{: pre}
{: http}

The compatibility layer of {{site.data.keyword.cos_full_notm}} will provide the means to configure your new hosted static website. Replace the placeholder content as shown in the example command to configure the website:
{: aws}

```
aws --endpoint-url=https://<endpoint> s3 website s3://<bucketname>/ --index-document index.html --error-document error.html
```
{: pre}
{: aws}

You may have completed this step during the creation of your bucket, as the basic configuration for your hosted static website determines when and how content is shown. For visitors to your website who fail to provide a key, or webpage, the default file will be shown instead. When your users encounter an error, the key for the error page determines what content visitors will receive. The basic configuration options for the default and error pages are shown in Figure 5.  
{: console}

![Configure basic options](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-basic-config.jpg){: caption="Figure 5. Configure basic options"}
{: console}

### Testing and visiting your new website
{: #static-website-testing}

Once you have configured your bucket to provide HTTP headers using the example command, all you have to do to test your new site is visit the URL for the site. Please note the protocol shown (http), after replacing the placeholders with your own choices made previously in this tutorial:

```
http://<bucketname>.s3-web.<endpoint>/
```
{: screen}

With the successful testing of your new site, you can now explore more options and add more content.

## Next steps
{: #static-website-next-steps}

The detailed description of configuration options for {{site.data.keyword.cos_full_notm}} hosted static websites can be found in the [API Documentation](https://cloud.ibm.com/apidocs/cos/cos-compatibility){: external}.
