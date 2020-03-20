---

copyright:
  years: 2020
lastupdated: "2020-03-26"

keywords: Object Storage, SysDig, monitoring, integration

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}
{:http: .ph data-hd-programlang='http'} 
{:console: .ph data-hd-programlang='Console'} 
{:cli: .ph data-hd-programlang='CLI'} 

# Using {{site.data.keyword.mon_full_notm}} with {{site.data.keyword.cos_full_notm}}
{: #mm-cos-integration}

Use the {{site.data.keyword.mon_full}} service to monitor your {{site.data.keyword.cos_full}} (COS) data in the {{site.data.keyword.cloud_notm}}. The results of the activity can be measured for compliance and other analysis through the web dashboard UI. 
{: shortdesc}

This is pre-release documentation reflecting validation in progress. Please evaluate accordingly.
{: important}

## Features
{: #mm-cos-features}

 {{site.data.keyword.mon_full_notm}} is a third-party, cloud-native, and container-aware management system. Documentation from [{{site.data.keyword.mon_short}}](/docs/Monitoring-with-Sysdig?topic=Sysdig-monitoring#monitoring_dashboards) can guide you in how to use the comprehensive dashboards. Full [platform integration](/docs/Monitoring-with-Sysdig?topic=Sysdig-platform_metrics_enabling) is also available, but in this guide we will focus on how to measure activity on individual buckets in your instance of {{site.data.keyword.cos_full_notm}}.

### Working with Metrics
{: #mm-cos-metrics}

According to the [{{site.data.keyword.mon_short}} documentation](/docs/Monitoring-with-Sysdig?topic=Sysdig-metrics), "A metric is a quantitative measure that has one or more labels to define its characteristics." When you configure your buckets to forward data to a {{site.data.keyword.mon_short}} instance, that data is automatically collected and available for analysis through the web UI.

## Before you begin
{: #mm-cos-before-starting}

Before you provision an instance of {{site.data.keyword.mon_full_notm}}, consider the following guidance:

   * The account owner can create, view, and delete an instance of a service in the {{site.data.keyword.cloud_notm}}. This user can also grant permissions to other users to work with the {{site.data.keyword.mon_full_notm}} service.
   * Other {{site.data.keyword.cloud_notm}} users with `administrator` or `editor` permissions can manage the {{site.data.keyword.mon_full_notm}} service in the {{site.data.keyword.cloud_notm}}. These users must also have platform permissions to create resources within the context of the resource group where they plan to provision the instance.

In this guide, we will examine using both the {{site.data.keyword.cloud_notm}} Console as well as the {{dev_cli_notm}} (CLI) to integrate {{site.data.keyword.mon_short}} in your {{site.data.keyword.cos_short}} instance. For more information about {{dev_cli_notm}}, check out the [documentation](/docs/cli?topic=cloud-cli-getting-started).

Using either set of instructions, Console or CLI, you will be able to get started using this guide. If the instructions that you seek do not appear in this guide, click on the appropriate context to view either 

### Instantiating and Provisioning {{site.data.keyword.cos_full_notm}}
{: #mm-cos-instantiation}

If you have not already done so, [set up and provision](/docs/cloud-object-storage?topic=cloud-object-storage-provision) your instance of {{site.data.keyword.cos_short}}. If you already have an instance of {{}} to work with, verify you have either the `manager` or `writer` [role](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions) to work with this guide.

## Manage User Access
{: #mm-cos-manage-users}

There are many ways to manage access to your {{site.data.keyword.cos_short}} instance, including creating [service credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials). 

**Users in an account [must be assigned a platform role](/docs/Monitoring-with-Sysdig?topic=Sysdig-iam) in order to manage instances as well as launching the Sysdig UI from the {{site.data.keyword.cloud_notm}} console. In addition, users must have a service role that defines the permissions to work with {{site.data.keyword.mon_full_notm}}.** 
{: important}

## Provisioning an instance of {{site.data.keyword.mon_short}}
{: #mm-cos-provisioning}



### Use the Console
{: #mm-cos-provisioning-console}
{: console}


### Use the API
{: #mm-cos-provisioning-api}
{: cli}


## Connect {{site.data.keyword.cos_short}} to {{site.data.keyword.mon_short}}
{: #mm-cos-connection}


### Configure a bucket using the console
{: #mm-cos-connection-console}
{: console}


### Configure a bucket using Resource Config API
{: #mm-cos-connection-api}
{: cli}


## Next Steps
{: #mm-cos-next-steps}


