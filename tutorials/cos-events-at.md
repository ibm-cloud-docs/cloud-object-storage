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

Warning! This topic contains draft content about unreleased functionality and is being reviewed for accuracy.
{: important}

# Tracking {{site.data.keyword.cos_short}} events in {{site.data.keyword.at_short}}
{: #tracking-cos-events}

There are many great options for tracking the activity involving your {{site.data.keyword.cos_full}} instance, especially when utilizing the benefits of {{site.data.keyword.at_full}}.

## What this tutorial will cover
{: #tracking-cos-events-overview}

This tutorial will provide an introduction to capturing information regarding the events of your {{site.data.keyword.cos_short}} instance using {{site.data.keyword.at_short}}. Of course both offer so many comprehensive feature sets that this introduction will cover only a fraction of what is available.

Also, to find out what works for you beyond this tutorial, check out the offerings listed for each service. With so many options, you are sure to find the specific configuration to work for your needs, beyond this brief introduction. 
{: tip} 

If you're not familiar with {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage). Also, if you're not familiar with {{site.data.keyword.at_full}}, you may wish to check out how to [get started with {{site.data.keyword.at_short}}](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-getting-started).

## Prerequisites
{: #tracking-cos-events-prereqs}

If you are already managing instances of {{site.data.keyword.cos_short}} or {{site.data.keyword.at_short}}, you do not need to create more. However, as this tutorial will modify and configure the instances we are working with, make sure that any accounts or services are not being used in a production environment.

For this tutorial, you need:
* An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com){: external}
* To complete the steps to manage access to the service, your user ID needs **administrator platform permissions** to manage the {{site.data.keyword.at_full_notm}} service. You may have to contact a account administrator. The account owner can grant another user access to the account for the purposes of managing user access, and managing account resources. [Learn more](/docs/iam?topic=iam-userroles).
* Your user ID needs to be configured with the **platform editor role** (at the very least) to create the {{site.data.keyword.cos_short}} instance and the **service access writer role** to create the manipulate buckets.
* Installation of both the [IBM Cloud CLI](/docs/cli?topic=cli-install-ibmcloud-cli) and [COS plugin](/docs/cloud-object-storage-cli-plugin?topic=cloud-object-storage-cli-plugin-ic-cos-cli).
{: cli}

When you create buckets or add objects, be sure to avoid the use of Personally Identifiable Information (PII). PII is information that can identify any user (natural person) by name, location, or any other means.
{: note}

### What to know before using the {{site.data.keyword.cloud_notm}} Console
{: #tracking-cos-events-prereqs-console}

In part, this tutorial will show how to use the UI interface, or [Console](https://cloud.ibm.com/){: external} to create instances of services and manage them. This is the easiest path to take for working with {{site.data.keyword.cloud}} but it is also powerful and responsive. 

As long as you can [login](https://cloud.ibm.com/login){: external} you should be ready to begin. At the context switcher above the main heading, choose "Console" to see the examples you will be working with.

### What to know before using the {{site.data.keyword.cloud_notm}} CLI
{: #tracking-cos-events-prereqs-cli}

In addition to detailing the UI at the Console, this tutorial will also show how to use the Command Line Interface, or CLI. Those who are so interested are encouraged to read the [Developer guidance](/docs/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) or study the [API](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) for {{site.data.keyword.cos_short}}.

Once you've comfortable with how to [get started](/docs/cli?topic=cli-getting-started) using the CLI tools, you should be ready for the next step. Before you start this tutorial, install the [{{site.data.keyword.cos_short}} plugin](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli#ic-installation). Next, you can verify that the CLI and {{site.data.keyword.dev_cli_short}} were installed successfully, run the `help` command in your terminal or other CLI interface.

```
ibmcloud cos help
```
{: codeblock}
{: cli}

If you can't see the example with instructions on how to run the `help` command in your terminal, choose "CLI" in the context switcher near the main heading to see the examples you will be working with.
{: tip}

## Creating your instance of {{site.data.keyword.at_full_notm}}
{: #tracking-cos-events-create-at}

From the [catalog](https://cloud.ibm.com/catalog){: external}, choose the service category "Developer Tools" listed in the categories of Services. The option for selecting {{site.data.keyword.at_full_notm}} should appear in the filtered list, it it isn't already visible. 
{: console}

Selecting the tile for {{site.data.keyword.at_full_notm}} in the catalog will take you to the creation interface in the console. The first choice is to "Select a region" by choosing the appropriate city for your instance. For example, if you are working in the `us-south` region, then `Dallas` is the appropriate choice for your instance.
{: console}

Log in to the region in the IBM Cloud where you want to provision the instance. Run the following command: `ibmcloud login`.
{: cli}

To get started with an instance of {{site.data.keyword.at_full_notm}} through the command line, you must provision an instance, and then create credentials for that instance.
{: cli}

Set the resource group where you want to provision the instance. Run the following command: [`ibmcloud target`](/docs/cli?topic=cli-ibmcloud_cli#ibmcloud_target). By default, the `default` resource group is set.
{: cli}

Run the [`ibmcloud resource service-instance-create`](/docs/cli?topic=cli-ibmcloud_commands_resource#ibmcloud_resource_service_instance_create) command:
{: cli}

```
ibmcloud resource service-instance-create <instance-name> logdna <service-plan-name> <location>
```
{: codeblock}
{: cli}

Replace the values as appropriate according to the information in Table 1.
{: cli}  

| Attribute | Value |
| --- | --- |
| <instance-name> | Replace with a name of your choice for the instance. |
| *logdna* | The literal reference of the {{site.data.keyword.la_full_notm}} service. |
| <service-plan-name> | Type of plan; valid values are *lite*, *7-days*, *14-days*, *30-days*. |
| <location> | The region where the LogDNA instance is created. To get the latest list of available locations, check out the [locations](/docs/Log-Analysis-with-LogDNA?topic=Log-Analysis-with-LogDNA-regions). |
{: caption="Table 1. CLI attributes and values relevant to {{site.data.keyword.at_short}}" caption-side="top"}
{: cli}

For example, to provision an instance with the lite plan, run the following command:
{: cli}

```
ibmcloud resource service-instance-create TestLogDNA logdna lite us-south
```
{: codeblock}
{: cli}

The results will display upon completion, appearing like shown.
{: cli}

```bash
Creating service instance TestLogDNA in resource group Default of account IBM as xxxx.xxxx@ibm.com...
OK
Service instance TestLogDNA was created.
                 
Name:         TestLogDNA   
ID:           crn:v1:bluemix:public:logdna:us-south:a/xxxxxxxxxxxxxxxxxxxxxxxx:3exxx14-7xxx-xxx3-9xxc-fxxxxc9b4xx::   
GUID:         3exxx14-7xxx-xxx3-9xxc-fxxxxc9b4xx   
Location:     us-south   
State:        active   
Type:         service_instance   
Sub Type:        
Created at:   2020-01-24T17:34:48Z   
Updated at:   2020-01-24T17:34:48Z
```
{: cli}

## Creating your instance of {{site.data.keyword.cos_full_notm}}
{: #tracking-cos-events-create-cos}

From the [catalog](https://cloud.ibm.com/catalog){: external}, choose the service category "Storage" listed in the categories of Services. The option for selecting {{site.data.keyword.cos_short}} should appear in the filtered list, it it isn't already visible. 
{: console}

Select the tile for {{site.data.keyword.cos_full_notm}} in the catalog. At the creation interface in the console, fill in the form with your details. The first choice is to "Select a region" by choosing the appropriate city for your instance. For example, if you are working in the `us-south` region, then `Dallas` is the appropriate choice for your instance.
{: console}

To create a new service instance using the CLI, type the following command: `ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global`.
{: cli}

Replace the terms according to the information in Table 2.

| Attribute | Value |
| --- | --- |
| <instance-name> | Replace with a name of your choice for the instance. |
| *cloud-object-storage* | The literal reference of the {{site.data.keyword.cos_short}} service. |
| <plan> | Type of plan; valid values are *lite*, *standard*. |
| *global* | The literal reference to the one location for public instances where the {{site.data.keyword.cos_short}} instance is created. When you provision your buckets, check out the [endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints). |
{: caption="Table 2. CLI attributes and values relevant to {{site.data.keyword.cos_short}}" caption-side="top"}
{: cli}

```
ibmcloud resource service-instance-create TestCOS cloud-object-storage Lite global
```
{: codeblock}
{: cli}

The results will display upon completion, appearing like shown.
{: cli}

```bash
Creating service instance TestCOS in resource group Default of account IBM as Default of account IBM as xxxx.xxxx@ibm.com...
OK
Service instance TestCOS was created.
                 
Name:         TestCOS   
ID:           crn:v1:staging:public:cloud-object-storage:global:a/xxxxxxxxxxxxxxxxxxxxxxxx:a2xxx26c-xx1c-4xxa-adxx-f9xxxxxxx14c::   
GUID:         a2xxx26c-xx1c-4xxa-adxx-f9xxxxxxx14c   
Location:     global   
State:        active   
Type:         service_instance   
Sub Type:        
Created at:   2020-01-24T20:34:35Z   
Updated at:   2020-01-24T20:34:35Z
```
{: cli}

## Configuration and connection of services
{: #tracking-cos-events-services-configuration}

In your account [resource list](https://cloud.ibm.com/resources), you should see your {{site.data.keyword.at_full_notm}} instance listed in the `Services` category. Selecting your newly created service by clicking on the name you chose should take you to a list of your Activity Tracker instances. There, you can choose `Manage access` from the operation drop-down menu on the side of the entry where you will define authorization levels and access at {{site.data.keyword.iamlong}}. 
{: console}

To begin the configuration of the new instances you created, we need to retrieve an identifier called a Cloud Resource Name ([CRN](/docs/resources?topic=resources-crn)). Start by determining the identifier for the service instance in {{site.data.keyword.cos_short}}.
{: cli}

```bash
ibmcloud resource service-instance TestCOS --id
```
{: codeblock}
{: cli}

The resulting CRN is shown in your command-line. Copy and paste it somewhere secure, or keep track of it as you will for later.
{: cli}

```bash
crn:v1:staging:public:cloud-object-storage:global:a/943494a618ed4e978e68b918d1aeec4c:a274c26c-841c-47fa-ad07-f925ff07d14c:: a274c26c-841c-47fa-ad07-f925ff07d14c
```
{: cli}

Next, determine the identifier for the service instance in {{site.data.keyword.at_short}}.

```bash
ibmcloud resource service-instance TestLogDNA --id
```
{: codeblock}
{: cli}

The resulting CRN is shown in your command-line. Copy and paste it somewhere secure, or keep track of it as you will for later.
{: cli}

```bash
crn:v1:staging:public:logdna:us-south:a/943494a618ed4e978e68b918d1aeec4c:c1ee8d0b-7591-461b-afbc-60e2cf47cf39:: c1ee8d0b-7591-461b-afbc-60e2cf47cf39
```
{: cli}

## Next Steps
{: #tracking-cos-events-next-steps}

For more about {{site.data.keyword.cos_full}}, you can quickly get an overview by [getting started with {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage). Also, you can find out more regarding {{site.data.keyword.at_full}} at the [getting started with {{site.data.keyword.at_short}}](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-getting-started).

With so many options, there is literally too much to cover beyond the scope of this tutorial. Here are only a few links to explore more of the ideas presented in this document to get you started on your own journey.

### Viewing Events
{: #tracking-cos-events-viewing}

With multiple options for viewing events in {{site.data.keyword.at_short}}, it may be helpful to review the [documentation](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-view_events).

### Configuring Alerts
{: #tracking-cos-events-configuring-alerts}

In {{site.data.keyword.at_short}} there are two kinds of alerts but many different ways to use them, as shown in the documentation for [configuring alerts](/docs/Activity-Tracker-with-LogDNA?topic=logdnaat-alerts).

### Exporting Events
{: #tracking-cos-events-exporting}

After generating information about each event, {{site.data.keyword.at_short}} also has the ability to [export the events](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-export) per your configuration.

### Archiving Events
{: #tracking-cos-events-archiving}

In this tutorial, events from {{site.data.keyword.cos_short}} were viewed in {{site.data.keyword.at_short}} as log entries. Reversing the relationship between them, you can also [archive {{site.data.keyword.at_short}} events in {{site.data.keyword.cos_full_notm}}](https://cloud.ibm.com/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-archiving).
