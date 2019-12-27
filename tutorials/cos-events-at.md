---

copyright:
  years: 2019
lastupdated: "2019-12-27"

keywords: activity, tracking, object storage, event, tutorial

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
{:table: .aria-labeledby="caption"}
{:cli: .ph data-hd-programlang='CLI'}
{:console: .ph data-hd-programlang='Console'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Tracking {{site.data.keyword.cos_short}} events in {{site.data.keyword.at_short}}
{: #tracking-cos-events}

There are many great options for tracking the activity involving your {{site.data.keyword.cos_full}} instance, especially when utilizing the benefits of {{site.data.keyword.at_full}}.

## What this tutorial will cover
{: #tracking-cos-events-overview}

This tutorial will provide an introduction to capturing information regarding the events of your {{site.data.keyword.cos_short}} instance using {{site.data.keyword.at_short}}. Of course both offer so many comprehensive feature sets that this introduction will cover only a fraction of what is available.

Also, to find out what works for you beyond this tutorial, check out the offerings listed for each service. With so many options, you are sure to find the specific configuration to work for your needs, beyond this brief introduction. 
{: tip} 

If you're not famiiiar with {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started). Also, if you're not familiar with {{site.data.keyword.at_full}}, you may wish to check out how to [get started with {{site.data.keyword.at_short}}](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started).

## Prerequisites
{: #tracking-cos-events-prereqs}


### What to know before using the {{site.data.keyword.cos_short}} Console
{: #tracking-cos-events-prereqs-console}


### What to know before using the {{site.data.keyword.cloud_notm}} CLI
{: #tracking-cos-events-prereqs-cli}


## Deployment of assets
{: #tracking-cos-events-deploy}


## Configuration of services
{: #tracking-cos-events-services-configuration}


## Observability
{: #tracking-cos-events-observability}


### Testing
{: #tracking-cos-events-testing}


### Further Operations
{: #tracking-cos-events-operations}


## Next Steps
{: #tracking-cos-events-next-steps}

For more about {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started). Also, you can find out more regarding {{site.data.keyword.at_full}} at the [getting started with {{site.data.keyword.at_short}}](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started).

With so many options, there is literally too much to cover beyond the scope of this tutorial. Here are only a few links to explore more of the ideas presented in this document to get you started on your own journey.

### Viewing Events
{: #tracking-cos-events-viewing}

With multiple options for viewing events in {{site.data.keyword.at_short}}, it may be helpful to review the [documentation](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-view_events).

### Configuring Alerts
{: #tracking-cos-events-configuring-alerts}

In {{site.data.keyword.at_short}} there are two kinds of alerts but many different ways to use them, as shown in the documentation for [configuring alerts](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-alerts).

### Exporting Events
{: #tracking-cos-events-exporting}

After generating information about each event, {{site.data.keyword.at_short}} also has the ability to [export the events](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-export) per your configuration.

### Archiving Events
{: #tracking-cos-events-archiving}

In this tutorial, events from {{site.data.keyword.cos_short}} were viewed in {{site.data.keyword.at_short}} as log entries. Reversing the relationship between them, you can also [archive {{site.data.keyword.at_short}} events in {{site.data.keyword.cos_full_notm}}](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-archiving).
