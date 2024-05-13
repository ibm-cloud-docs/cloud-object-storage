---

copyright:
  years: 2022, 2024
lastupdated: "2024-05-13"

keywords: events, activity, logging, api, buckets, tracking

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Tracking events on your {{site.data.keyword.cos_full_notm}} buckets
{: #at}

{{site.data.keyword.cloud_notm}} offers centralized logging services to track events performed on your resources. You can use these services to investigate abnormal activity and critical actions and comply with regulatory audit requirements.

Use these services to track events on your {{site.data.keyword.cos_full}} buckets to provide a record of what is happening with your data. Enable these services on your bucket to receive detailed logs about data access and bucket configuration events.

When event tracking is enabled on your bucket, the default target service that captures these events is [{{site.data.keyword.at_full}}](/docs/cloud-object-storage?topic=cloud-object-storage-at#at-at). Ensure that you have an instance of Activity Tracker at the receiving location corresponding to your bucket location as specified in [{{site.data.keyword.cos_short}} Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).

Alternatively, use [IBM Cloud Activity Tracker Event Routing](/docs/cloud-object-storage?topic=cloud-object-storage-at#at-route-logs) to send events to other target services or to send events to Activity Tracker instances in locations other than the bucket location.

## {{site.data.keyword.cloud_notm}} Activity Tracker
{: #at-at}

As of 28 March 2024 the IBM Log Analysis and IBM Cloud Activity Tracker services are deprecated and will no longer be supported as of 30 March 2025. Customers will need to migrate to IBM Cloud Logs, which replaces these two services, prior to 30 March 2025.
{: deprecated}

Tracking {{site.data.keyword.cos_short}} events with {{site.data.keyword.at_full}} provides a record of what is happening with your data. The {{site.data.keyword.at_full_notm}} service provides the framework and functionality to monitor API calls to services on the IBM Cloud and produces the evidence to comply with corporate policies and market industry-specific regulations.

See Getting started with [{{site.data.keyword.at_full_notm}}](/docs/activity-tracker?topic=activity-tracker-getting-started) to learn more.


## IBM Cloud Logs (Coming Soon)
{: #at-logs}

IBM Cloud Logs will replace IBM Cloud Activity Tracker hosted event search. See the [IBM Announcement](/blog/announcement/ibm-cloud-logs-observability/) to learn more.

IBM Cloud Logs gives you flexibility in how your data is processed for insights and trends, and where data is stored for high-speed search and long-term trend analysis. It provides the tools for you to maximize the value obtained while maintaining control on the total cost.

[Migrate from IBM Cloud Activity Tracker to IBM Cloud Logs](/docs/activity-tracker?topic=activity-tracker-deprecation_migration) once available to avoid any disruption in event tracking.

## Route Logs with IBM Cloud Activity Tracker Event Routing
{: #at-route-logs}

[Get started with IBM Cloud Activity Tracker Event Routing](/docs/atracker?topic=atracker-getting-started) to configure routing for your IBM Cloud Object Storage auditing events. You can use Activity Tracker Event Routing, a platform service, to manage auditing events at the account-level by configuring targets and routes that define where auditing data is routed.

Activity Tracker Event Routing supports routing IBM COS bucket logs to the following targets
-	[Another COS Bucket](/docs/atracker?topic=atracker-getting-started-target-cos)
-	[Activity Tracker Instance](/docs/atracker?topic=atracker-getting-started-target-logdna) (Deprecated)
-	[Event Streams](/docs/atracker?topic=atracker-getting-started-target-event-streams)
-	[IBM Cloud Logs](/docs/activity-tracker?topic=activity-tracker-deprecation#cloud-logs-intro) (Coming Soon)


## Configure Activity Tracking Events on your IBM Cloud Object Storage Bucket (Recommended)
{: #at-configure}

Enable event tracking can be enabled on your IBM Cloud Object Storage bucket at the time of bucket provisioning, or by updating the bucket configuration after bucket creation. Event tracking will only apply to COS requests made after enablement.

This feature is not currently supported in [Object Storage for Satellite](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite).
{: note}

This feature supports [SCC monitoring](/docs/security-compliance).
{: note}

By default, COS events that report on global actions, such as bucket creation, are collected automatically. You can monitor global actions through the Activity Tracker instance located in the Frankfurt location.

IBM COS also optionally supports tracking on these event types:
- Management Events – Requests related to managing bucket and object configuration
- Read Data Events – Requests related to object list and read requests
- Write Data Events – These are all events related to writing and deleting objects

Refer to the COS API events to see the full list of Management, Read Data, and Write Data actions that produce events.

Use the [COS Resource Configuration API](/apidocs/cos/cos-configuration) to configure tracking of these events on your bucket

When event tracking is enabled, all events are sent to the default receiving location for IBM Cloud Activity Tracker Event Router that are based on the location of the bucket. Refer to [IBM COS Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability) to see this default mapping. Use Activity Tracker Event Router rules to route events to an alternative location or target service. See [Managing Rules](/docs/atracker?topic=atracker-route_v2) to learn more.

### Recommended examples
{: #at-examples-recommended}

Select the UI, API or Terraform tab at the top of this topic to display the examples that show how to enable tracking of management, data read, and data write events in your bucket.

### UI example for how to enable tracking of events in your bucket
{: #at-ui-example-recommended}
{: ui}

1.	From the IBM Cloud console [resource list](https://cloud.ibm.com/resources), select the service instance that contains the bucket you are interested in adding event tracking. This takes you to the Object Storage Console
2.	Choose the bucket for which you want to enable event tracking.
3.	Navigate to the configuration tab.
4.	Scroll down to the advanced configuration section and toggle on the events you want to track for this bucket.
5.	After a few minutes, any activity will be visible in the Activity Tracker web UI.

### API example for how to enable tracking of events in your bucket
{: #at-api-example-recommended}
{: api}



### Terraform example for how to create a {{site.data.keyword.cos_full_notm}} instance and then creating a COS bucket with Activity Tracker
{: #at-terraform-example-recommended}
{: terraform}

    ```sh
    resource "ibm_resource_instance" "cos_instance" {
      name              = "cos-instance"
      resource_group_id = data.ibm_resource_group.cos_group.id
      service           = "cloud-object-storage"
      plan              = "standard"
      location          = "global"
    }

    resource "ibm_cos_bucket" "activity_tracker_bucket" {
      bucket_name          = “bucket_name”
      resource_instance_id = ibm_resource_instance.cos_instance.id
      region_location      = “us-south”
      storage_class        = “standard”
      activity_tracking {
      read_data_events     = true
        write_data_events    = true
        management_events    = true
          }
    }
    ```

### SDK example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #at-sdk-example-recommended}
{: sdk}

## Configure Activity Tracking Events on your IBM Cloud Object Storage Bucket (Legacy)
{: #at-configure-legacy}

Enable IBM Activity Tracking on your COS bucket by specifying the target CRN of the Activity Tracker instance in the [COS Resource Configuration API](/apidocs/cos/cos-configuration). Specify the CRN to define the route for COS events.

Management events are always enabled when a CRN is set on the Activity Tracking configuration

The legacy model also supports optionally enabling tracking on the following event types:
- Read Data Events – Requests related to object list and read requests
- Write Data Events – These are all events related to writing and deleting objects

IBM Cloud observability routing services are the standardized way for customers to manage routing of platform observability data.  Service-specific routing configurations like COS are being deprecated.
{: note}

It is recommended that customers [remove these legacy routing configurations]({#a-atlegacy}) that use CRNs and instead use the IBM Activity Tracker Event Routing service to route events to other locations.

IBM COS will continue to support legacy configurations where a CRN was specified that differs from the default location.

## Upgrading from Legacy to the Recommended Event Tracking on your COS bucket
{: #at-legacy-upgrade}

{#a-atlegacy}
To upgrade from the legacy configuration using the Resource Configuration API, remove the target Activity Tracker instance CRN. Events will now route to the default Activity Tracker Event Router receiving location as described in [COS Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability). Provision an instance of Activity Tracker hosted event search at this location or define a routing rule prior to upgrading to ensure there’s no interruption in event logging.

### Example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #at-legacy-upgrade-examples}

Select the SDK, API, UI, Terraform tab at the top of this topic to see examples of patchs.

### UI example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #at-ui-example-legacy}
{: ui}

1.	From the IBM Cloud console [resource list](https://cloud.ibm.com/resources), select the service instance that contains the bucket you wish to upgrade to the recommended event tracking configuration. This takes you to the Object Storage Console.
2.	Choose the bucket for which you want to upgrade.
3.	Navigate to the configuration tab.
4.	Scroll down to the advanced configuration section and locate the configuration panel for Activity Tracker.
5.	Click on the top right corner of the panel and select upgrade.
6.	Confirm you would like to upgrade event tracking for this bucket.


### API example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #at-api-example-legacy}
{: api}

### example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #at-terraform-example-legacy}
{: terraform}

### SDK example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #at-sdk-example-legacy}
{: sdk}


