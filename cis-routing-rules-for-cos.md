---

copyright:
  years: 2020, 2024
lastupdated: "2024-04-24"

keywords: routing rules for cloud-object-storage, static website domains for cloud-object-storage, domains for cloud-object-storage

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Domain Routing for {{site.data.keyword.cos_full_notm}} static website hosting
{: #routing-rules-cos}

A static website hosted with {{site.data.keyword.cos_full}} can be configured using {{site.data.keyword.cis_full_notm}}. Configuring routing rules for domains hosted in {{site.data.keyword.cos_full_notm}} will be explored in this advanced "how to."
{: shortdesc}

These instructions are subject to change and are provided here for review.
{: important}

## Overview
{: #routing-rules-cos-overview}

When hosting static website content on {{site.data.keyword.cos_full_notm}}, you can configure how the public accesses your site by specifying a custom domain. Using {{site.data.keyword.cis_full_notm}} configures {{site.data.keyword.cos_short}} to modify the HTTP `host` header in response to public requests of your content. For this example, we will use `example.com` as the domain configured in {{site.data.keyword.cis_short}}, and a desired subdomain, `web` that you wish your public visitors to view as `web.example.com`.

## Before you start
{: #routing-rules-cos-prerequisites}

Prerequisites:

- An account for the {{site.data.keyword.cloud_notm}} Platform
- An instance of {{site.data.keyword.cis_short}} with an applicable plan and permissions
- An instance of {{site.data.keyword.cos_short}} with a bucket configured as a hosted static website 
- An Internet domain managed through {{site.data.keyword.cis_full_notm}}

These instructions require an account with the correct [plan](/docs/cis?topic=cis-multi-domain-support) in order to access the services as described.
{: note}

## {{site.data.keyword.cloud_notm}} Internet, Domain, and Delivery Services
{: #routing-rules-cos-cis}

### Create a Page Rule to target your bucket
{: #routing-rules-cos-page-rule}

Creating a "Page Rule" in your instance of {{site.data.keyword.cis_full_notm}} will take several steps, and require your [endpoint](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) information

1. Select Performance from the Navigation
1. Select the Page rules Tab from the options.
1. In the table of rules (empty if this is the first), Select the "Create rule" button.
1. In the URL match field Enter `<sub-domain>.<custom-domain>/*`. For example, `web.example.com/*`.
1. Select from the options for "Rule Behavior Setting" the "Host Header Override." For your custom bucket hosting your static website content as a sub-domain and endpoint: `<bucket-name>.s3-web.<bucket-region>.cloud-object-storage.appdomain.cloud`. For example, using `web-example-com` as the name of the bucket hosting your static website, the example would appear as: `web-example-com.s3-web.us-east.cloud-object-storage.appdomain.cloud`. 

   You can find this information in your bucket configuration, or Quick View in the Console.
   {: note}

1. When you have confirmed your configuration options, select "Create."
1. Next, you will create the DNS CNAME record to forward traffic to your content in {{site.data.keyword.cos_full_notm}}.

### Create a domain alias to proxy your content
{: #routing-rules-cos-page-rule-proxy}

After you have directed your visitors to the right location using a "Page Rule," you will want to create an alias to guide your visitors to the location. For this example, we want to send your visitors to your new subdomain `web` to the existing domain, `example.com` that will point to

1. Select Reliability from the Navigation
1. Select the DNS Tab from the options.
1. Add a new DNS record, substituting your configuration for the exemplified values shown. The desired subdomain should be added in the "name" field. in this example, we used `web` as a new subdomain value. The "alias domain name" is the same as entered earlier, which in this example comprised a bucket name followed by a dot and then the endpoint, for example, `web-example-com.s3-web.us-east.cloud-object-storage.appdomain.cloud`.

   - Type: CNAME
   - Name: `<sub-domain>`
   - TTL: Automatic
   - Alias Domain Name: `<bucket-name>.s3-web.<bucket-region>.cloud-object-storage.appdomain.cloud`

1. Click "Add" to save the DNS entry when you've completed the configuration.
1. In the table of rules where your new entry appears, enable the Proxy option as "on."

To test the rule you just created, allow some time for the configuration to propagate. Then, use a browser to visit the subdomain exemplified by `web.example.com` to validate the settings.

## Next steps
{: #routing-rules-cos-steps}

Learn more about [{{site.data.keyword.cis_full_notm}}](/docs/cis?topic=cis-about-ibm-cloud-internet-services-cis), or jump right in using {{site.data.keyword.cis_short}} to [get started](/docs/cis?topic=cis-getting-started) managing your presence on the Internet.
