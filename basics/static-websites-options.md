---

copyright:
  years: 2020
lastupdated: "2020-10-22"

keywords: static website, object storage, options 

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Serving static content with {{site.data.keyword.cos_full_notm}}
{: #static-website-options}

Hosting a static website on {{site.data.keyword.cos_full}} starts with [configuring a bucket](/docs/cloud-object-storage?topic=static-website-tutorial#static-website-create-public-bucket) for public access. Then, [upload](/docs/cloud-object-storage?topic=static-website-tutorial#static-website-upload-content) your website content to your bucket. And finally, at a minimum [configure the website](/docs/cloud-object-storage?topic=static-website-tutorial#static-website-configure-options) to use your documents as an index for the site and to potentially display errors. The ability to serve static content over the web demonstrates one capability of using {{site.data.keyword.cos_short}} solutions as part of your overall web strategy.
{: shortdesc}

Note that this is unreleased information and represents work in progress for the purpose of review.
{: note}

## Overview
{: #static-websites-options-overview}

Modern web development requires modern tools and secure infrastructure. Static websites represent the latest developments in high-availability, SEO improvement, and increased security. While covering every available option is beyond the scope of this hands-on overview, the ease of serving static content on {{site.data.keyword.cos_full_notm}} allows for many possible strategies.

Hosted static websites focus on the content your users desire: information and media. By removing the administration of webservers like Apache or Nginx, management of your website focuses directly on content, from generation to deployment. An excellent overview of open source generators can be found on the web at [StaticGen](https://www.staticgen.com){: external}.

Static content differs substantially from dynamic web content. Learn more at the {{site.data.keyword.cloud_notm}} overview of [dynamic websites](https://www.ibm.com/cloud/websites){: external} to see if that is right for you. But if you don't need to generate dynamic content on the web or if your workflow results in content saved to a fixed form, then the hosted static solution presented here presents the best choice.

## Compatibility
{: #static-websites-options-compatibility}

Creating static website hosting in {{site.data.keyword.cos_full_notm}} can be accomplished with [cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl), as well as libraries for [Java](/docs/cloud-object-storage?topic=cloud-object-storage-java), [Python](/docs/cloud-object-storage?topic=cloud-object-storage-python), and [NodeJS](/docs/cloud-object-storage?topic=cloud-object-storage-node). In addition, S3 compatibility means that the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli) can also be used to define static website functionality from the command line.

## {{site.data.keyword.cloud_notm}} Content Delivery Networks
{: #static-websites-options-cdn}

Static websites are meant to be fast and secure. Serving up static content is easy with the right tools that deliver the content to your customers. Many deployment tools have built-in support for CDN support.

The process for delivering static content through dedicated networks starts with this [overview of CDN options](https://www.ibm.com/cloud/cdn). Using the above solution as a template, others have also contributed their [expertise](https://jamesthom.as/2019/07/hosting-static-websites-on-ibm-cloud/){: external}, and [enthusiasm](https://bakingclouds.com/hosting-a-static-website-on-ibm-cos/){: external}.

## Next steps
{: #static-websites-options-next-steps}

The detailed description of configuration options for {{site.data.keyword.cos_full_notm}} hosted static websites can be found in the [API Documentation](https://cloud.ibm.com/apidocs/cos/cos-configuration){: external}.
