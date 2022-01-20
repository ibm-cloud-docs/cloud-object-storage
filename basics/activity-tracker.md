---

copyright:
  years: 2019, 2022
lastupdated: "2022-01-19"

keywords: events, activity, logging, api

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Tracking events using {{site.data.keyword.at_full_notm}}
{: #at}

[{{site.data.keyword.at_full_notm}}](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-getting-started) allows you to [audit the requests](/docs/cloud-object-storage?topic=cloud-object-storage-at-events) made against a bucket and the objects it contains.
{: shortdesc}

This feature is not currently supported in {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}. [Learn more.](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite)
{: note}

## Using the console
{: #at-console}

First, make sure that you have a bucket. If not, follow the [getting started tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to become familiar with the console. 

### Enable activity tracking
{: #at-console-enable}

1. From the {{site.data.keyword.cloud_notm}} [console dashboard](https://cloud.ibm.com/), select **Storage** to view your resource list.
2. Next, select the service instance with your bucket from within the **Storage** menu. This takes you to the {{site.data.keyword.cos_short}} Console.
3. Choose the bucket for which you want to enable logging.
4. Select **Configuration** from the navigation menu.
5. Navigate to the **Activity Tracker** tab.
6. Click **Create**.
7. If you already have an instance of {{site.data.keyword.at_full_notm}}, you can select it here.  If not, select the appropriate configuration, and click **Create**.
8. After a few minutes, any activity will be [visible in the web UI](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-launch).

### Archive events to object storage.
{: #at-archive}

It is possible to have all data collected in an instance of {{site.data.keyword.at_full_notm}} be archived and written to a bucket.  For more information, [see the Activity Tracker documentation](/docs/Activity-Tracker-with-LogDNA?topic=Activity-Tracker-with-LogDNA-archiving).


## Using an API
{: #at-api}

Enabling activity tracking is managed with the [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration). This new REST API is used for configuring buckets.
