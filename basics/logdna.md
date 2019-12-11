---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

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

# Track events using Activity Tracker with LogDNA
{: #at}

[Activity Tracker with LogDNA](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started) allows you to [audit the requests](/docs/services/cloud-object-storage?topic=cloud-object-storage-at-events) made against a bucket and the objects it contains.
{: shortdesc}

## Using the console
{: #at-console}

First, make sure that you have a bucket. If not, follow the [getting started tutorial](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) to become familiar with the console. 

### Enable activity tracking
{: #at-console-enable}

1. From the {{site.data.keyword.cloud_notm}} [console dashboard](https://cloud.ibm.com/), select **Storage** to view your resource list.
2. Next, select the service instance with your bucket from within the **Storage** menu. This takes you to the {{site.data.keyword.cos_short}} Console.
3. Choose the bucket for which you want to enable logging.
4. Select **Configuration** from the navigation menu.
5. Navigate to the **Activity Tracker** tab.
6. Click **Create**.
7. If you already have an instance of Activity Tracker with LogDNA, you can select it here.  If not, select the appropriate configuration, and click **Create**.
8. After a few minutes, any activity will be [visible in the web UI](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-launch).

### Archive events to object storage.
{: #at-archive}

It is possible to have all data collected in an instance of Activity Tracker with LogDNA be archived and written to a bucket.  For more information, [see the Activity Tracker documentation](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-archiving).


## Using an API
{: #at-api}

Enabling activity tracking is managed with the [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration). This new REST API is used for configuring buckets.
