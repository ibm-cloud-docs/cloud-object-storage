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

## Features
{: #mm-cos-features}

 {{site.data.keyword.mon_full_notm}} is a third-party and cloud-native management system. Documentation from [{{site.data.keyword.mon_short}}](/docs/Monitoring-with-Sysdig?topic=Sysdig-monitoring#monitoring_dashboards) can guide you in how to use the comprehensive dashboards. In this guide we will focus on how to measure activity on individual buckets in your instance of {{site.data.keyword.cos_full_notm}}.

### Working with Metrics
{: #mm-cos-metrics}

According to the [{{site.data.keyword.mon_short}} documentation](/docs/Monitoring-with-Sysdig?topic=Sysdig-metrics), "A metric is a quantitative measure that has one or more labels to define its characteristics." When you configure your buckets to forward data to a {{site.data.keyword.mon_short}} instance, that data is automatically collected and available for analysis through the web UI.

## Before you begin
{: #mm-cos-before-starting}

Before you provision an instance of {{site.data.keyword.mon_full_notm}}, consider the following guidance:

   * The account owner can create, view, and delete an instance of a service in the {{site.data.keyword.cloud_notm}}. This user can also grant permissions to other users to work with the {{site.data.keyword.mon_full_notm}} service.
   * Other {{site.data.keyword.cloud_notm}} users with `administrator` or `editor` permissions can manage the {{site.data.keyword.mon_full_notm}} service in the {{site.data.keyword.cloud_notm}}. These users must also have platform permissions to create resources within the context of the resource group where they plan to provision the instance.

In this guide, we will examine using both the {{site.data.keyword.cloud_notm}} Console as well as the IBM Cloud Developer Tools (CLI) to integrate {{site.data.keyword.mon_short}} in your {{site.data.keyword.cos_short}} instance. For more information about IBM Cloud Developer Tools, check out the [documentation](/docs/cli?topic=cloud-cli-getting-started).

Using either set of instructions, Console or CLI, you will be able to get started using this guide. If the instructions that you seek do not appear in this guide, click on the appropriate item using the context switcher. 

### Instantiating and Provisioning {{site.data.keyword.cos_full_notm}}
{: #mm-cos-instantiation}

If you have not already done so, [set up and provision](/docs/cloud-object-storage?topic=cloud-object-storage-provision) your instance of {{site.data.keyword.cos_short}}. If you already have an instance of {{site.data.keyword.mon_short}} to work with, verify you have either the `manager` or `writer` [role](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions) to work with this guide.

## Manage User Access
{: #mm-cos-manage-users}

There are many ways to manage access to your {{site.data.keyword.cos_short}} instance, but for this guide we'll focus on how to access the dashboard you are going to create. 

**Users in an account [must be assigned a platform role](/docs/Monitoring-with-Sysdig?topic=Sysdig-iam) in order to manage instances as well as launching the Sysdig UI from the {{site.data.keyword.cloud_notm}} console. In addition, users must have a service role that defines the permissions to work with {{site.data.keyword.mon_full_notm}}.** 
{: important}

## Provisioning an instance of {{site.data.keyword.mon_short}}
{: #mm-cos-provisioning}

From the [catalog](https://cloud.ibm.com/login){: external} choose {{site.data.keyword.mon_full_notm}} from the available services. When selected, you will be taken to the configuration shown in Figure 1.  At the bottom of the screen under "IBM platform metrics:" select Enable before creating the instance.
{: console}

![Instance creation](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-instance-creation.png){: console}

After you [login](/docs/cli?topic=cloud-cli-ibmcloud_cli#ibmcloud_login) using IBM Cloud Developer Tools and target both the region and resource group for your account, create a new resource using the command as shown.
{: cli}

```bash
ibmcloud resource service-instance-create <INSTANCE_NAME> <SERVICE_NAME> <SERVICE_PLAN_NAME> <LOCATION>
```
{: codeblock}
{: cli}

In the code sample, replace the placeholders with the appropriate values.
{: cli}

| Value	| Description | Sample |
| --- | --- | --- |
| &lt;INSTANCE_NAME&gt; | The name of your new instance | `MySysDig` |
| &lt;SERVICE_NAME&gt; | The name of the service | `sysdig-monitor` |
| &lt;SERVICE_PLAN_NAME&gt; | The name of the plan | `Lite` |
| &lt;LOCATION&gt; | The region of your buckets | `us-east` |
{: cli}

### Configure Platform Metrics on {{site.data.keyword.mon_short}}
{: cli}

```bash
ibmcloud resource service-instance-update <INSTANCE_NAME> -p '{"default_receiver": true}'
```
{: codeblock}
{: cli}

## Connect {{site.data.keyword.cos_short}} to {{site.data.keyword.mon_short}}
{: #mm-cos-connection}

Most of the work you will do with {{site.data.keyword.mon_short}} will be in specific dashboards to be discussed later. But first, there are a couple of ways in which you can get started.

Complete the following steps to get an IAM token:
{: cli}

1. From a terminal, log in to the {{site.data.keyword.cloud_notm}}.
{: cli}
2. Run the following command to get a token:
{: cli}

    ```
    ibmcloud iam oauth-tokens
    ```
    {: codeblock}
    {: cli}

Keep the token handy for later if you are using the Dev Tools CLI.
{: cli}

### Configure a bucket for metrics
{: #mm-cos-connection-console}

In this guide, we want to measure the number and size of objects in our buckets.

When creating a bucket, you can configure your {{site.data.keyword.mon_short}} instance at the same time.
{: console}

![Bucket creation](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-COS-UI-bucket-creation.png){: console}

You can also configure an existing bucket to use the {{site.data.keyword.mon_short}} instance by selecting `configuration` in the console.
{: console}

![Bucket modification](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-COS-UI-bucket-modification.png){: console}

Note that in each case the `region` for your new instance of {{site.data.keyword.mon_short}} is automatically tied to the region of the bucket. 
{: important}
{: console}

Note that if the {{site.data.keyword.mon_short}} instance is created by the COS, Platform metrics must be set on the {{site.data.keyword.mon_short}} instance.
{: important}
{: console}

To set Platform metrics on the instance:
   1. Go to https://cloud.ibm.com/observe/monitoring
   2. Click on "Configure platform metrics"
   3. Select the region where the {{site.data.keyword.mon_short}} instance was created
   4. Click Configure
   5. Now the {{site.data.keyword.mon_short}} instance is set for Platform metrics
{: console}

We will use cURL to connect to the Resource Configuration API and modify our bucket's configuration.
{: cli}

```bash
curl -X PATCH -k  \
  -H "authorization: Bearer $TOKEN" \
  https://config.cloud-object-storage.cloud.ibm.com/v1/b/$1 \
  -H 'cache-control: no-cache' \
  -d '{"metrics_monitoring": {
    "usage_metrics_enabled": true,
    "metrics_monitoring_crn": "crn:v1:bluemix:public:sysdig-monitor:us-east:a/9xxxxxxxxxb1xxxc7fdxxxxxxxxxx5:7xxxxxxxx0-xx7x-xdx8-9fxx-123456789012::"
    }
   }'
```
{: codeblock}
{: cli}

In the code sample, replace the placeholders with the appropriate values.
{: cli}

| Value	| Description | 
| --- | --- | 
| $TOKEN | The token for authentication created previously | 
| $1 | The name of your bucket | 
{: cli}

### Launch your instance of {{site.data.keyword.mon_short}}
{: #mm-cos-connection-launch}

When you have created your instance of {{site.data.keyword.mon_short}}, click on the "View Sysdig" button in the "View dashboard" column, as shown in Figure 4.

![Launching instances](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-instance-management.png){: caption="Figure 1. Launching {{site.data.keyword.mon_short}} instances"}

### Choose a pre-built dashboard for {{site.data.keyword.mon_short}}
{: #mm-cos-connection-dashboard}

This guide has skipped right past the installation of agents that is typical of most monitoring configurations. In addition, you can also skip the manual creation of building a dashboard by using a pre-built solution. As shown in Figure 2, you can choose how to present your data using one of the options given.

![Dashboard configuration](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-pre-built-reports.png){: caption="Figure 2. Choose a bre-built report"}

### View your data in {{site.data.keyword.mon_short}}
{: #mm-cos-connection-view-data}

Once you've configured your dashboard, you can view your data. Figures 3-5 show different views of your usage. 

![View bucket metrics](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-COS-metrics.jpg){: caption="Figure 3. View sample data showing space used and number of objects"}

![View metrics by location](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-COS-metrics-2.jpg){: caption="Figure 4. View space used by location"}

![View metrics by storage class](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/SysDig-COS-metrics-3.jpg){: caption="Figure 5. View used space by storage class"}

## Cloud Object Storage metrics details
{: mm-cos-metrics}

You can measure the number of objects in your bucket as well as its size, by using the following metrics.

### IBM COS Bucket object count
{: #mm-cos-ibm_cos_bucket_object_count}

Number of objects in the bucket enumerates your contents.

| Metadata | Description |
|----------|-------------|
| `Metric Name` | `ibm_cos_bucket_object_count`|
| `Metric Type` | `gauge` |
| `Value Type`  | `none` |
| `Segment By` | `Service instance, IBM COS Bucket storage class` |
{: caption="Table 2: IBM COS Bucket object count metric metadata" caption-side="top"}

### IBM COS Bucket size
{: #mm-cos-ibm_cos_bucket_used_bytes}

Bucket Size in bytes shows the usage of the bucket.

| Metadata | Description |
|----------|-------------|
| `Metric Name` | `ibm_cos_bucket_used_bytes`|
| `Metric Type` | `gauge` |
| `Value Type`  | `byte` |
| `Segment By` | `Service instance, IBM COS Bucket storage class` |
{: caption="Table 3: IBM COS Bucket size metric metadata" caption-side="top"}

## Attributes for Segmentation
{: mm-cos-attributes}

You can filter your results by attributes.

### Global Attributes
{: mm-cos-global-attributes}

The following attributes are available for segmenting all of the metrics listed above

| Attribute | Attribute Name | Attribute Description |
|-----------|----------------|-----------------------|
| `Cloud Type` | `ibm_ctype` | public, dedicated or local |
| `Location` | `ibm_location` | The location of the monitored resource - this may be a Cross Region, Regional, or Single Site bucket |
| `Resource` | `ibm_resource` | COS bucket name |
| `Resource Type` | `ibm_resource_type` | COS bucket |
| `Scope` | `ibm_scope` | The scope is the account associated with this metric |
| `Service name` | `ibm_service_name` | cloud-object-storage |

### Additional Attributes
{: mm-cos-additional-attributes}

The following attributes are available for segmenting one or more attributes as described in the reference above.  Please see the individual metrics for segmentation options.

| Attribute | Attribute Name | Attribute Description |
|-----------|----------------|-----------------------|
| `IBM COS Bucket storage class` | `ibm_cos_bucket_storage_class` | Storage class of the bucket |
| `Service instance` | `ibm_service_instance` | The service instance segment identifies the guid of the instance the metric is associated with |

## Next Steps
{: #mm-cos-next-steps}

You will want to manage the data the {{site.data.keyword.mon_short}} instance is collecting for you. From management to setting alerts, you can [get started](/docs/Monitoring-with-Sysdig?topic=Sysdig-getting-started) monitoring your data quickly and efficiently.
