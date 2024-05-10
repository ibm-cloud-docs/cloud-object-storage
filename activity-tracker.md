---

copyright:
  years: 2022, 2024
lastupdated: "2024-05-10"

keywords: events, activity, logging, api, buckets, tracking

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Tracking events on your {{site.data.keyword.cos_full_notm}} buckets
{: #at}

{{site.data.keyword.cloud_notm}} offers centralized logging services to track events performed on your resources. You can use these services to investigate abnormal activity and critical actions and comply with regulatory audit requirements.

Use these services to track events on your {{site.data.keyword.cos_full}} buckets to provide a record of what is happening with your data. Enable these services on your bucket to receive detailed logs about data access and bucket configuration events.

When event tracking is enabled on your bucket, the default target service that captures these events is [{{site.data.keyword.at_full}}](#at-title). Ensure that you have an instance of Activity Tracker at the receiving location corresponding to your bucket location as specified in [{{site.data.keyword.cos_short}} Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).

Alternatively, use IBM Cloud Activity Tracker Event Routing (<- Make this link to section below on Event Routing) to send events to other target services or to send events to Activity Tracker instances in locations other than the bucket location.

## {{site.data.keyword.cloud_notm}} Activity Tracker
{: #at-at}

{{../log-analysis/_include-segments/deprecation_notice.md}}

Tracking {{site.data.keyword.cos_short}} events with {{site.data.keyword.at_full}} provides a record of what is happening with your data. The {{site.data.keyword.at_full_notm}} service provides the framework and functionality to monitor API calls to services on the IBM Cloud and produces the evidence to comply with corporate policies and market industry-specific regulations.{{#at-title}}

See Getting started with {{site.data.keyword.at_full_notm}} to learn more.

Migrate from {{site.data.keyword.at_full_notm}} to IBM Cloud Logs to avoid any disruption in event tracking.

## IBM Cloud Logs (Coming Soon)
{: #at-logs}

IBM Cloud Logs will replace IBM Cloud Activity Tracker hosted event search. See the IBM Announcement to learn more.

IBM Cloud Logs gives you flexibility in how your data is processed for insights and trends, and where data is stored for high-speed search and long-term trend analysis. It provides the tools for you to maximize the value obtained while maintaining control on the total cost at all times

## Route Logs with IBM Cloud Activity Tracker Event Routing
{: #at-route-logs}

Get started with IBM Cloud Activity Tracker Event Routing to configure routing for your IBM Cloud Object Storage auditing events. You can use Activity Tracker Event Routing, a platform service, to manage auditing events at the account-level by configuring targets and routes that define where auditing data is routed.

Activity Tracker Event Routing supports routing IBM COS bucket logs to the following targets
-	Another COS Bucket
-	Activity Tracker Instance (Deprecated)
-	Event Streams
-	IBM Cloud Logs (Coming Soon)


## Enabling Activity Tracking Events on your IBM Cloud Object Storage Bucket (Recommended)
{: #at-enable}

Event tracking can be enabled on your IBM Cloud Object Storage bucket at the time of bucket provisioning or by updating the bucket configuration after bucket creation. Event tracking will only apply to COS requests made after enablement.

Note: This feature is not currently supported in Object Storage for Satellite. Learn more.

Note: This feature supports SCC monitoring

By default, COS events that report on global actions, such as bucket creation, are collected automatically. You can monitor global actions through the Activity Tracker instance located in the Frankfurt location.

IBM COS also optionally supports enabling tracking on three categories of events:
- Management Events – Requests related to managing bucket and object configuration
- Read Data Events – Requests related to object list and read requests
- Write Data Events – These are all events related to writing and deleting objects

Refer to the COS API events to see the full list of Management, Read Data, and Write Data actions that produce events.

Use the COS Resource Configuration API to configure tracking of these events on your bucket

When event tracking is enabled, all events are sent to the default receiving location for IBM Cloud Activity Tracker Event Router based on the location of the bucket. Refer to IBM COS Service Integration to see this default mapping. Use Activity Tracker Event Router rules to route events to an alternative location or target service. See Managing Rules to learn more.


## Enabling Activity Tracking  Events on your IBM Cloud Object Storage Bucket (Legacy)
{: #at-enable-legacy}

Enable IBM Activity Tracking on your COS bucket by specifying the target CRN of the Activity Tracker instance in the COS Resource Configuration API. Specify the CRN to define the route for COS events.

Management events are always enabled when a CRN is set on the Activity Tracking configuration

The legacy model also supports optionally enabling tracking on the following event types:
- Read Data Events – Requests related to object list and read requests
- Write Data Events – These are all events related to writing and deleting objects

IBM Cloud observability routing services are the standardized way for customers to manage routing of platform observability data.  Service-specific routing configurations like COS are being deprecated.
{: note}

It is recommended that customers remove these legacy routing configurations (make this a link to upgrade section below) that use CRNs and instead use the IBM Activity Tracker Event Routing service to route events to other locations.

IBM COS will continue to support legacy configurations where a CRN was specified that differs from the default location.


## Upgrading from Legacy to the Recommended Event Tracking on your COS bucket
{: #at-legacy-upgrade}

To upgrade from the legacy configuration using the Resource Configuration API, remove the target Activity Tracker instance CRN. Events will now route to the default Activity Tracker Event Router receiving location as described in [COS Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability). Provision an instance of Activity Tracker hosted event search at this location or define a routing rule prior to upgrading to ensure there’s no interruption in event logging.


[{{site.data.keyword.at_full_notm}}](/docs/activity-tracker?topic=activity-tracker-getting-started) allows you to [audit the requests](/docs/cloud-object-storage?topic=cloud-object-storage-at-events) made against a bucket and the objects it contains.
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
8. After a few minutes, any activity will be [visible in the web UI](/docs/activity-tracker?topic=activity-tracker-observe).

### Archive events to object storage.
{: #at-archive}

It is possible to have all data collected in an instance of {{site.data.keyword.at_full_notm}} be archived and written to a bucket.  For more information, [see the Activity Tracker documentation](/docs/activity-tracker?topic=activity-tracker-archiving-ov).


## Using an API
{: #at-api}

Enabling activity tracking is managed with the [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration). This new REST API is used for configuring buckets.
