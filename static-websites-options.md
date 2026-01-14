---

copyright:
  years: 2020, 2026
lastupdated: "2026-01-14"

keywords: object storage, static website, options

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Serving static websites
{: #static-website-options}

A new hosted static website can be created with {{site.data.keyword.cos_full}} in minutes using [this simple tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial). This topic contains the details and some advanced configuration options for hosting static websites.
{: shortdesc}

## Overview
{: #static-websites-options-overview}

Modern web development requires modern tools and secure infrastructure. Static websites represent the latest developments in high-availability, SEO improvement, and increased security. While covering every available option is beyond the scope of this hands-on overview, the ease of serving static content on {{site.data.keyword.cos_full_notm}} allows for many possible strategies.

Hosted static websites focus on the content your users desire: information and media. By removing the administration of web servers like Apache or Nginx, management of your website focuses directly on content, from generation to deployment.

Static content differs substantially from dynamic web content. However, if you don't need to generate dynamic content on the web or if your workflow results in content saved to a fixed form, then the hosted static solution featured here presents the best choice.

## Capabilities
{: #static-websites-options-compatibility}

Creating static website hosting in {{site.data.keyword.cos_full_notm}} can be accomplished with [cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl#curl-configure-static-web), as well as libraries for [Java](/docs/cloud-object-storage?topic=cloud-object-storage-java#java-examples-hosted-static-website-create), [Go](/docs/cloud-object-storage?topic=cloud-object-storage-using-go#go-guide-hosted-static-website-create), [Python](/docs/cloud-object-storage?topic=cloud-object-storage-python#python-examples-hosted-static-website-create), and [NodeJS](/docs/cloud-object-storage?topic=cloud-object-storage-node#node-examples-hosted-static-website-create). In addition, S3 compatibility means that the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli#aws-cli-configure-static-web) can also be used to define static website functionality from the command line. Also, creating and configuring a new hosted static website solution can be created using a GUI in the [Console](https://cloud.ibm.com/login){: external} just by adding the option for Static Website when creating a bucket.

## Basic Configuration
{: #static-websites-options-basic-conf}

Hosting a static website on {{site.data.keyword.cos_full}} starts with [creating a bucket](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial#static-website-create-public-bucket) and configuring it for public access. Then, [upload](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial#static-website-upload-content) your website content to your bucket. Finally, [configure the website](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial#static-website-configure-options) to use your documents as an index for the site and to potentially display errors.

At minimum, your configuration should consist of a required index document for visitors to view by default, usually written in HTML and named `index.html`. An optional error document can help your visitors stay on track when they stray. Of course, you can always try for yourself using [this tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial).

## Advanced Configuration
{: #static-websites-options-adv-conf}

When you create and configure a new hosted static website, you may also wish to use {{site.data.keyword.cis_full_notm}} to configure more advanced options including [routing rules for your domain](/docs/cloud-object-storage?topic=cloud-object-storage-routing-rules-cos). But you don't even have to go further than configuring your bucket during creation to start customizing your new site.

![Configure options](images/cos-sw-ui-complete-config.jpg){: caption="Initial configuration options"}

### Routing
{: #static-websites-options-adv-conf-routing}

Routing gives you control over the requests coming from your visitors. For example, you could globally redirect all your traffic from using one protocol to another, like replacing HTTP with the secure HTTPS. Or, you can create individual rules that process incoming requests for specific files and provide responses to your visitors based on the rules you define.

![Global routing rule](images/cos-sw-ui-global-routing.jpg){: caption="Global routing rule"}

If you already have a hosted static website that you wish to migrate, you can bring a set of the routing rules that you have already set and import the set as code. The input shown in Figure 3 requires a JSON array formatted for the website configuration rules.

![Import configuration code](images/cos-sw-ui-code-config.jpg){: caption="Import configuration as code"}

An example of JSON code exemplifies the possibilities. The following shows a rule that redirects visitors from missing pages or possible malformed request resulting in a `404` error code and redirecting the visitor to a specific error page. The JSON can contain multiple objects representing the definition of the rules as needed.

```json
[
  {
    "Condition": {
      "HttpErrorCodeReturnedEquals": "404"
    },
    "Redirect": {
      "HostName": "<bucketname>.<endpoint>",
      "HttpRedirectCode": "302",
      "Protocol": "https",
      "ReplaceKeyWith": "error404.html"
    }
  }
]
```

The same rule codified previously can be added as an individual rule using the Console, and shown in Figure 3.

![Add individual rules](images/cos-sw-ui-rule-creation.jpg){: caption="Add individual rules"}

### {{site.data.keyword.cloud_notm}} Internet, Domain, and Delivery Services
{: #static-websites-options-cdn}

One of the benefits of using {{site.data.keyword.cis_full_notm}} pertains to [setting up your own domains](/docs/cis?topic=cis-set-up-your-dns-for-cis). A "domain" is part of the overall web address, consisting of a Top Level Domain (TLD) and one or more unique words separated by dots, like `example.com` where the TLD is `com`. You can choose to skip this step, but if your DNS records are not configured properly using {{site.data.keyword.cis_short_notm}} (or other service providing domain name resolution), it might leave all or part of your website inaccessible.

Static websites are meant to be fast and secure. Serving up static content is easy with the right tools that deliver the content to your customers. Many deployment tools have built-in support for CDN support. Getting started configuring your domains using [{{site.data.keyword.cis_full}}](/docs/cis?topic=cis-resolve-override-cos). When creating redirect rules, you will be adding a `CNAME`, a "canonical (domain) name", or alias. Just like files on an operating system can have an alias for convenience, your hosted static website can be just as convenient.

The process for delivering static content through dedicated networks starts with this [overview of CDN options](https://www.ibm.com/cloud/cdn). Content Delivery moves your static content closer to your customer's own location, extending your reach without having to manage copies of your content.

## Endpoints for hosting static website content
{: #static-websites-options-more-endpoints}

The following tables match most of the regions, locations, and type of connections used in {{site.data.keyword.cos_full_notm}} to the new specific endpoints used for sourcing and testing hosted static websites. For tethered endpoints not listed here, find more information on [using tethered endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-advanced-endpoints).

### Regional endpoints
{: #static-websites-options-regional-endpoints}

| Region            | Hosted Static Website Endpoint                         |
|-------------------|--------------------------------------------------------|
| US South          | `s3-web.us-south.cloud-object-storage.appdomain.cloud` |
| US East           | `s3-web.us-east.cloud-object-storage.appdomain.cloud`  |
| EU United Kingdom | `s3-web.eu-gb.cloud-object-storage.appdomain.cloud`    |
| EU Germany        | `s3-web.eu-de.cloud-object-storage.appdomain.cloud`    |
| AP Australia      | `s3-web.au-syd.cloud-object-storage.appdomain.cloud`   |
| AP Tokyo          | `s3-web.jp-tok.cloud-object-storage.appdomain.cloud`   |
| AP Osaka          | `s3-web.jp-osa.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Regional Endpoints" caption-side="top"}
{: #swregionalendpointtable1}
{: tab-title="Public"}
{: tab-group="Regional-endpoints"}

| Region            | Hosted Static Website Endpoint                                 |
|-------------------|----------------------------------------------------------------|
| US South          | `s3-web.private.us-south.cloud-object-storage.appdomain.cloud` |
| US East           | `s3-web.private.us-east.cloud-object-storage.appdomain.cloud`  |
| EU United Kingdom | `s3-web.private.eu-gb.cloud-object-storage.appdomain.cloud`    |
| EU Germany        | `s3-web.private.eu-de.cloud-object-storage.appdomain.cloud`    |
| AP Australia      | `s3-web.private.au-syd.cloud-object-storage.appdomain.cloud`   |
| AP Tokyo          | `s3-web.private.jp-tok.cloud-object-storage.appdomain.cloud`   |
| AP Osaka          | `s3-web.private.jp-osa.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Regional Endpoints" caption-side="top"}
{: #swregionalendpointtable2}
{: tab-title="Private"}
{: tab-group="Regional-endpoints"}

| Region            | Hosted Static Website Endpoint                                |
|-------------------|---------------------------------------------------------------|
| US South          | `s3-web.direct.us-south.cloud-object-storage.appdomain.cloud` |
| US East           | `s3-web.direct.us-east.cloud-object-storage.appdomain.cloud`  |
| EU United Kingdom | `s3-web.direct.eu-gb.cloud-object-storage.appdomain.cloud`    |
| EU Germany        | `s3-web.direct.eu-de.cloud-object-storage.appdomain.cloud`    |
| AP Australia      | `s3-web.direct.au-syd.cloud-object-storage.appdomain.cloud`   |
| AP Tokyo          | `s3-web.direct.jp-tok.cloud-object-storage.appdomain.cloud`   |
| AP Osaka          | `s3-web.direct.jp-osa.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Regional Endpoints" caption-side="top"}
{: #swregionalendpointtable3}
{: tab-title="Direct"}
{: tab-group="Regional-endpoints"}


### Cross Region endpoints
{: #static-websites-options-cross-region-endpoints}

| Region          | Hosted Static Website Endpoint                   |
|-----------------|--------------------------------------------------|
| US Cross Region | `s3-web.us.cloud-object-storage.appdomain.cloud` |
| EU Cross Region | `s3-web.eu.cloud-object-storage.appdomain.cloud` |
| AP Cross Region | `s3-web.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Endpoints" caption-side="top"}
{: #swcrossregionalendpointtable1}
{: tab-title="Public"}
{: tab-group="Cross-regional-endpoints"}

| Region          | Hosted Static Website Endpoint                           |
|-----------------|----------------------------------------------------------|
| US Cross Region | `s3-web.private.us.cloud-object-storage.appdomain.cloud` |
| EU Cross Region | `s3-web.private.eu.cloud-object-storage.appdomain.cloud` |
| AP Cross Region | `s3-web.private.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Endpoints" caption-side="top"}
{: #swcrossregionalendpointtable2}
{: tab-title="Private"}
{: tab-group="Cross-regional-endpoints"}

| Region          | Hosted Static Website Endpoint                          |
|-----------------|---------------------------------------------------------|
| US Cross Region | `s3-web.direct.us.cloud-object-storage.appdomain.cloud` |
| EU Cross Region | `s3-web.direct.eu.cloud-object-storage.appdomain.cloud` |
| AP Cross Region | `s3-web.direct.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Endpoints" caption-side="top"}
{: #swcrossregionalendpointtable3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints"}

### Single site endpoints
{: #static-websites-options-single-site-endpoints}

| Location                    | Hosted Static Website Endpoint                      |
|-----------------------------|-----------------------------------------------------|
| Amsterdam, Netherlands      | `s3-web.ams03.cloud-object-storage.appdomain.cloud` |
| Chennai, India              | `s3-web.che01.cloud-object-storage.appdomain.cloud` |
| Mexico City, Mexico         | `s3-web.mex01.cloud-object-storage.appdomain.cloud` |

| Montr&egrave;al, Canada     | `s3-web.mon01.cloud-object-storage.appdomain.cloud` |
| Paris, France               | `s3-web.par01.cloud-object-storage.appdomain.cloud` |
| San Jose, US                | `s3-web.sjc04.cloud-object-storage.appdomain.cloud` |
| S&atilde;o Paulo, Brazil    | `s3-web.sao01.cloud-object-storage.appdomain.cloud` |
| Singapore                   | `s3-web.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Single Data Center Endpoints" caption-side="top"}
{: #swsdcendpointtable1}
{: tab-title="Public"}
{: tab-group="single-datacenter-endpoints"}

| Location                    | Hosted Static Website Endpoint                              |
|-----------------------------|-------------------------------------------------------------|
| Amsterdam, Netherlands      | `s3-web.private.ams03.cloud-object-storage.appdomain.cloud` |
| Chennai, India              | `s3-web.private.che01.cloud-object-storage.appdomain.cloud` |
| Mexico City, Mexico         | `s3-web.private.mex01.cloud-object-storage.appdomain.cloud` |

| Montr&egrave;al, Canada     | `s3-web.private.mon01.cloud-object-storage.appdomain.cloud` |
| Paris, France               | `s3-web.private.par01.cloud-object-storage.appdomain.cloud` |
| San Jose, US                | `s3-web.private.sjc04.cloud-object-storage.appdomain.cloud` |
| S&atilde;o Paulo, Brazil    | `s3-web.private.sao01.cloud-object-storage.appdomain.cloud` |
| Singapore                   | `s3-web.private.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Single Data Center Endpoints" caption-side="top"}
{: #swsdcendpointtable2}
{: tab-title="Private"}
{: tab-group="single-datacenter-endpoints"}

| Location                    | Hosted Static Website Endpoint                             |
|-----------------------------|------------------------------------------------------------|
| Amsterdam, Netherlands      | `s3-web.direct.ams03.cloud-object-storage.appdomain.cloud` |
| Chennai, India              | `s3-web.direct.che01.cloud-object-storage.appdomain.cloud` |
| Mexico City, Mexico         | `s3-web.direct.mex01.cloud-object-storage.appdomain.cloud` |

| Montr&egrave;al, Canada     | `s3-web.direct.mon01.cloud-object-storage.appdomain.cloud` |
| Paris, France               | `s3-web.direct.par01.cloud-object-storage.appdomain.cloud` |
| San Jose, US                | `s3-web.direct.sjc04.cloud-object-storage.appdomain.cloud` |
| S&atilde;o Paulo, Brazil    | `s3-web.direct.sao01.cloud-object-storage.appdomain.cloud` |
| Singapore                   | `s3-web.direct.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Single Data Center Endpoints" caption-side="top"}
{: #swsdcendpointtable3}
{: tab-title="Direct"}
{: tab-group="single-datacenter-endpoints"}

## Next steps
{: #static-websites-options-next-steps}

Making the most of modern web development requires modern tools and secure infrastructure, but shouldn't be a barrier to the success of your projects. If you haven't already tried the tutorial, check out for yourself [how hosting a static website](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial) can work for you.
