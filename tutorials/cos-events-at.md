---

copyright:
  years: 2019
lastupdated: "2019-12-31"

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

If you're not familiar with {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started). Also, if you're not familiar with {{site.data.keyword.at_full}}, you may wish to check out how to [get started with {{site.data.keyword.at_short}}](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started).

## Prerequisites
{: #tracking-cos-events-prereqs}

For this tutorial, you need:
  * An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com){: external}
  * Some files on your local computer for uploading

If you are already managing instances of {{site.data.keyword.cos_short}} or {{site.data.keyword.at_short}}, you do not need to create more. However, as this tutorial will modify and configure the instances we are working with, make sure that any accounts or services are not being used in a production environment.

When you create buckets or add objects, be sure to avoid the use of Personally Identifiable Information (PII)

PII is information that can identify any user (natural person) by name, location, or any other means.
{: note}


### What to know before using the {{site.data.keyword.cloud_notm}} Console
{: #tracking-cos-events-prereqs-console}

In part, this tutorial will show how to use the UI interface, or [Console](https://cloud.ibm.com/){: external} to create instances of services and manage them. This is the easiest path to take for working with {{site.data.keyword.cloud}} but it is also powerful and reponsive. 

As long as you can [login](https://cloud.ibm.com/login){: external} you should be ready to start! At the context switcher above the main heading, choose "Console" to see the examples you will be working with.

### What to know before using the {{site.data.keyword.cloud_notm}} CLI
{: #tracking-cos-events-prereqs-cli}

In addition to detalining the UI at the Console, this tutorial will also show how to use the Command Line Interface, or CLI. Those who are so interested are encouraged to read the [Developer guidance](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) or study the [API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) for {{site.data.keyword.cos_short}}.

As long as you're familiar with how to [get started](/docs/cli?topic=cloud-cli-getting-started) using the CLI tools, you should be ready for this tutorial. Before you start, you can verify that the CLI and {{site.data.keyword.dev_cli_short}} were installed successfully, run the `help` command in your terminal or other CLI interface.

```
ibmcloud dev help
```
{: codeblock}
{: cli}

If you can't see the example with instructions on how to run the `help` command in your terminal, choose "CLI" in the context switcher above the main heading to see the examples you will be working with.
{: tip}

## Creating your instance of {{site.data.keyword.at_full_notm}}
{: #tracking-cos-events-create-at}

From the [catalog](https://cloud.ibm.com/catalog){: external}, choose the service category "Developer Tools" listed in the categories of Services. The option for selecting {{site.data.keyword.at_full_notm}} should appear in the filtered list, it it isn't already visible. 
{: console}

Selecting the tile for {{site.data.keyword.at_full_notm}} in the catalog will take you to the creation interface in the console. The first choice is to "Select a region" by choosing the appropriate city for your instance. For example, if you are working in the `us-south` region, then `Dallas` is the appropriate choice for your instance.
{: console}

## Creating your instance of {{site.data.keyword.cos_full_notm}}
{: #tracking-cos-events-create-cos}

From the [catalog](https://cloud.ibm.com/catalog){: external}, choose the service category "Storage" listed in the categories of Services. The option for selecting {{site.data.keyword.cos_short}} should appear in the filtered list, it it isn't already visible. 
{: console}

Selecting the tile for {{site.data.keyword.at_full_notm}} in the catalog will take you to the creation interface in the console. The first choice is to "Select a region" by choosing the appropriate city for your instance. For example, if you are working in the `us-south` region, then `Dallas` is the appropriate choice for your instance.
{: console}

## Configuration of services
{: #tracking-cos-events-services-configuration}

In your account [resource list](), you should see your {{site.data.keyword.at_full_notm}} instance listed in the `Services` category. Selecting your newly created service by clicking on the name you chose should take you to a list of your Activity Tracker instances. There, you can choose `Manage access` from the operation drop-down menu on the side of the entry where you will define authorization levels and access at {{site.data.keyword.iamlong}}. 
{: console}

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
