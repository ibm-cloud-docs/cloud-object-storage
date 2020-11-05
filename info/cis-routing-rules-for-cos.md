---

copyright:
  years: 2020
lastupdated: "2020-11-07"

keywords: routing rules for cloud-object-storage, static website domains for cloud-object-storage, domains for cloud-object-storage 

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

# Domain Routing for {{site.data.keyword.cos_full_notm}}
{: #routing-rules-cos}

A static website hosted with {{site.data.keyword.cos_full}} can be configured using {{site.data.keyword.cis_full_notm}}. Configuring routing rules for domains hosted in {{site.data.keyword.cos_full_notm}} will be explored in this "how to."
{: shortdesc}

These instructions are subject to change and are provided here for review. 
{: important}

## Overview
{: #routing-rules-cos-overview}

When hosting static website content on {{site.data.keyword.cos_full_notm}}, you can configure how the public accesses your site by specifying a custom domain. Using {{site.data.keyword.cis_full_notm}} configures {{site.data.keyword.cos_short}} to modify the HTTP `host` header in response to public requests of your content. 

## Before you start
{: #routing-rules-cos-prerequisites}

Prerequisites:

- An account for the {{site.data.keyword.cloud_notm}} Platform
- An instance of {{}}
- A bucket in your instance configured as a hosted static website 
- An Internet domain managed through {{site.data.keyword.cis_full_notm}}

These instructions require an account with the correct [plan](/docs/cis?topic=cis-multi-domain-support) in order to access the services as described. 
{: note}

### {{site.data.keyword.cloud_notm}} Internet, Domain, and Delivery Services
{: #routing-rules-cos-cis}

Create a "Page Rule" in your instance of {{site.data.keyword.cis_full_notm}}

1. Select Performance from the Navigation
1. Select the Page rules Tab from the options.
1. In the table of rules (empty if this is the first), Select the "Create rule" button.
1. In the URL match field Enter `<sub-domain>.<custom-domain>`. For example, `web.example.com`.
1. Select from the options for "Rule Behavior Setting" the "Host Header Override." For your custom bucket hosting your static website content as a sub-domain and endpoint: `<bucket-name>.s3-web.<bucket-region>.cloud-object-storage.appdomain.cloud`. For example, `web-example-com.s3-web.us-east.cloud-object-storage.appdomain.cloud`. 

   You can find this link in your bucket configuration, or Quick View in the Console.
   {: note}

1. When you have confirmed your configuration options, select "Create."
1. Next, you will create the DNS CNAME record to forward traffic to your content in {{site.data.keyword.cos_full_notm}}.
1. Select Reliability from the Navigation
1. Select the DNS Tab from the options.
1. Add a new DNS record, substituting your configuration for the examplified values shown:

   - Type: CNAME
   - Name: <sub-domain>
   - TTL: Automatic
   - Alias Domain Name: web-example-com.s3-web.us-east.cloud-object-storage.appdomain.cloud
   
1. "Save" when you've completed the configuration.
1. In the table of rules, enable the Proxy option as "on."

To test the rule you just created, allow for the configuration to propagate and visit the subdomain exemplified by `web.example.com`.

## Next steps
{: #routing-rules-cos-steps}

Reviewing this content should be essential.
