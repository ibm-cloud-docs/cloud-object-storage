---

copyright:
  years: 2020
lastupdated: "2020-11-07"

keywords: static website, hosting, tutorial 

subcollection: cloud-object-storage

content-type: tutorial

services: cloud-object-storage

account-plan: lite

completion-time: 15m

---

{{site.data.keyword.attribute-definition-list}}

# Building a Static Website
{: #static-website-tutorial}
{: toc-content-type="tutorial"}
{: toc-services="cloud-object-storage"}
{: toc-completion-time="15m"}

This tutorial shows how to [host a static website](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-options) on {{site.data.keyword.cos_full}}, including creating a bucket, uploading content, and configuring your new website.
{: shortdesc}

Hosting static websites with {{site.data.keyword.cos_full_notm}} serves static content for public access giving users flexibility, ease of delivery, and high availability. This tutorial contains instructions for using [cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl), the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli), as well as the [Console](https://cloud.ibm.com/login){: external}. Choose your path for this tutorial by using the links for switching between the instructions above the title of this topic.

## The Scenario
{: #static-website-scenario}

The scenario for this tutorial simplifies web hosting to its essentials in order to highlight the steps involved. While not every configuration option will be covered in this tutorial, correctly completing this tutorial results in web-accessible content.

## Before you start
{: #static-website-before-you-start}

Ensure that you have what you need to start:

- {: hide-dashboard} An account for the {{site.data.keyword.cloud_notm}} Platform 
- An instance of {{site.data.keyword.cos_full_notm}}
- Content in fixed form, like text (HTML would be perfect), and image files

Check that you have the access as appropriate to either the instance of {{site.data.keyword.cos_full_notm}} you will be using or the proper [permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions) for the buckets you will be using for this tutorial. 
{: ui}

For use of the [IBM Cloud CLI](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli) with this tutorial, you will need to [configure the Object Storage plug-in](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli#ic-config) to specify the service instance you want to use and the default region where you want your new bucket to be created. 
{: cli}

## Create a bucket configured for public access
{: #static-website-create-public-bucket}

Creating a bucket for a static website will require public access. There are a number of options for configuring public access. Specifically, using the ObjectReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) will prevent the listing of the contents of the bucket while still allowing for the static content to be viewed on the internet. If you want to allow the viewing of the listing of the contents, use the ContentReader [IAM role](/docs/cloud-object-storage?topic=cloud-object-storage-iam) for your bucket.

### Create a bucket
{: #static-website-create-bucket}

After configuring the CLI plug-in, replace the placeholder content as shown in the example command to create a bucket:
{: cli}

```
ibmcloud cos bucket-create --bucket <bucketname>
```
{: pre}
{: cli}

Once you login to the Console and after you create an instance of {{site.data.keyword.cos_full_notm}}, you can create a bucket. Click on the button labeled "Create bucket" and choose from the options as shown in Figure 1. Select the card that reads "Customize your bucket."
{: ui}

![Customize your bucket](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-bucket-cards.jpg){: ui}

The container for the static files in your website will reside in a bucket that you can name. The name you create must be unique, should not contain personal or identifying information, can't have two periods, dots, or hyphens in a row, and must start and end with alphanumeric characters (ASCII character set items 3&ndash;63). See Figure 2 for an example.
{: ui}

![Name bucket for Static Website](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-bucketname.jpg){: ui}

### Setting public access
{: #static-website-public-access}

In all scenarios for this tutorial, you will want to use the [UI at the Console](https://cloud.ibm.com/login){: external} to allow [public access](/docs/cloud-object-storage?topic=cloud-object-storage-iam-public-access) to your new website.

When creating a bucket for hosting Static Website content, there is an option to enable public access as part of the bucket creation process. See Figure 3 for the option to enable public access to your bucket. For the explanation of the options for the "index document" and "error document" as shown, find more below in the section [Configure the options for your website](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial#static-website-configure-options). You may complete the basic configuration with this step, before uploading content to your bucket as shown in the next step.
{: ui}

![Enable public access](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-complete-config.jpg){: ui}

## Upload content to your bucket
{: #static-website-upload-content}

The content of your hosted static website files focuses naturally on information and media. A popular approach to creating content for static websites are open source generators listed at [StaticGen](https://www.staticgen.com){: external}. For the purpose of this tutorial, we only need two files:

- An index page, typically written in HTML and named `index.html`, that loads by default for visitors to your site
- An error page, also in HTML and here named `error.html`; typically the error page is loaded when a visitor tries to access an object that isn't present or doesn't have public access

Other files, like images, PDFs, or videos, can also be uploaded to your bucket (but this tutorial will focus only on a minimum set of requirements).

For the purpose of this tutorial, place the HTML pages for the index and error handling in a local directory. Replace the placeholder content as shown in the example command to upload your html files:
{: cli}

```
ibmcloud cos object-put --bucket BUCKET_NAME --key KEY [--body FILE_PATH]
```
{: pre}
{: cli}

You may have already completed the basic configuration for hosting your static website. Files can be uploaded directly in the Console once you've named and configured your bucket. Note the step is optional as shown in Figure 4, and can occur at any point before the testing of your new hosted website.
{: ui}

![Upload files](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-upload-files.jpg){: ui}

For the rest of the tutorial, we will assume that the object key for the index page is `index.html` and the key for the error document is `error.html` although any appropriate filename can be used for the suffix or key.

## Configure the options for your website
{: #static-website-configure-options}

There are more options than this tutorial can describe, and for the purpose of this tutorial we only need to set the configuration to start using the static website feature.

Create a JSON file with the appropriate configuration information:
{: cli}

```json
{
  "ErrorDocument": {
    "Key": "error.html"
  },
  "IndexDocument": {
    "Suffix": "index.html"
  }
}
```

Replace the placeholder content as shown in the example command to configure the website:
{: cli}

```
ibmcloud cos bucket-website-put --bucket BUCKET_NAME --website-configuration file://<filename.json>
```
{: pre}
{: cli}

You may have completed this step during the creation of your bucket, as the basic configuration for your hosted static website determines when and how content is shown. For visitors to your website who fail to provide a key, or webpage, the default file will be shown instead. When your users encounter an error, the key for the error page determines what content visitors will receive. The configuration options for the default and error pages are repeated for reference.  
{: ui}

![Configure options](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cos-sw-ui-complete-config.jpg){: ui}

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
