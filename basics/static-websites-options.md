---

copyright:
  years: 2020
lastupdated: "2020-10-08"

keywords: static website, object storage, cdn 

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

# Options for Static Websites with {{site.data.keyword.cos_full_notm}}
{: #static-website-options}

There are many possible paths to take when building static websites with {{site.data.keyword.cos_full}}. The ability to serve static content over the web demonstrates one capability of using {{site.data.keyword.cos_short}}.
{: shortdesc}

This material represents work in progress and should not be considered final.
{: important}

Modern web development requires modern tools and secure infrastructure. Static websites represent the latest developments in high-availability, SEO improvement, and increased security. While covering every available option is beyond the scope of this hands-on overview, the ease of serving static content on {{site.data.keyword.cos_full_notm}} allows for many possible strategies.

## Before you start
{: #static-websites-options-before-you-start}

Static websites focus on the content your users desire: information and media. By removing the administration of webservers like Apache or Nginx, management of your website focuses directly on content, from generation to deployment. An excellent overview of open source generators can be found on the web at [StaticGen](https://www.staticgen.com){: external}.

## {{site.data.keyword.cloud_notm}} Content Delivery Networks
{: #static-websites-options-cdn}

Static websites are meant to be fast and secure. Serving up static content is easy with the right tools.

The process for serving static content can be covered from start to finish with [this tutorial](/docs/solution-tutorials?topic=solution-tutorials-static-files-cdn). Using the above solution as a template, others have contributed their [expertise](https://jamesthom.as/2019/07/hosting-static-websites-on-ibm-cloud/){: external}, and [enthusiasm](https://bakingclouds.com/hosting-a-static-website-on-ibm-cos/){: external}.

## Next steps
{: #static-websites-options-next-steps}

Learn more at the {{site.data.keyword.cloud_notm}} overview of [web development](https://www.ibm.com/cloud/websites){: external} and choose the path that is right for you.
