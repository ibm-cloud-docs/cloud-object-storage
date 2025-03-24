---

copyright:
  years: 2022, 2024, 2025
lastupdated: "2025-03-24"

keywords: events, activity, logging, buckets, tracking, logs, legacy

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Tracking events on your {{site.data.keyword.cos_full_notm}} buckets
{: #at}

{{site.data.keyword.cloud_notm}} offers centralized logging services to track events performed on your resources. You can use these services to investigate abnormal activity and critical actions and comply with regulatory audit requirements.

Use these services to track events on your {{site.data.keyword.cos_full}} buckets to provide a record of what is happening with your data. Enable these services on your bucket to receive detailed logs about data access and bucket configuration events.

Use [IBM Cloud Activity Tracker Event Routing](/docs/cloud-object-storage?topic=cloud-object-storage-at#at-route-logs) to manage auditing events at the account-level by configuring targets and routes that define where auditing data is routed.

This feature is not currently supported in [Object Storage for Satellite](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite).
{: note}

This feature supports [SCC monitoring](/docs/security-compliance).
{: note}

## Using {{site.data.keyword.cloud_notm}} Activity Tracker to track bucket events
{: #at-at}

As of 28 March 2024 the IBM Log Analysis and IBM Cloud Activity Tracker services are deprecated and will no longer be supported as of 30 March 2025. Customers will need to migrate to IBM Cloud Logs, which replaces these two services, prior to 30 March 2025.
{: deprecated}

Tracking {{site.data.keyword.cos_short}} events with {{site.data.keyword.at_full}} provides a record of what is happening with your data. The {{site.data.keyword.at_full_notm}} service provides the framework and functionality to monitor API calls to services on the IBM Cloud and produces the evidence to comply with corporate policies and market industry-specific regulations.

See Getting started with [{{site.data.keyword.at_full_notm}}](/docs/activity-tracker?topic=activity-tracker-getting-started) to learn more.  [Migrate from {{site.data.keyword.at_full_notm}} to IBM Cloud Logs](/docs/activity-tracker?topic=activity-tracker-deprecation_migration) to avoid any disruption in event tracking.


## Using {{site.data.keyword.logs_full_notm}} to track bucket events
{: #at-logs}

{{site.data.keyword.logs_full_notm}} gives you flexibility in how your data is processed for insights and trends, and where data is stored for high-speed search and long-term trend analysis. It provides the tools for you to maximize the value obtained while maintaining control on the total cost.

To track bucket events by using {{site.data.keyword.logs_full_notm}} as a target for {{site.data.keyword.atracker_short}}, follow the steps in [Configuring an IBM Cloud Logs instance as a target](/docs/atracker?topic=atracker-getting-started-target-cloud-logs) to provision an {{site.data.keyword.logs_full_notm}} instance, configure the service-to-service authorization, create a target, and create a route. When creating a route, select the location that corresponds to the region where your buckets are located, for example, `us-south`. After you complete the configuration, all {{site.data.keyword.cos_short}} request logs from the selected region are forwarded to the {{site.data.keyword.logs_full_notm}} instance. To view the logs, [launch the IBM Cloud Logs UI](/docs/cloud-logs?topic=cloud-logs-instance-launch#instance-launch-cloud-ui).

Optionally, you can use the [TCO Optimizer](/docs/cloud-logs?topic=cloud-logs-tco-optimizer) to filter the logs to display information for only a specific {{site.data.keyword.cos_short}} instance. By default, {{site.data.keyword.atracker_short}} captures data for all services in the selected region. To limit the logs to a particular {{site.data.keyword.cos_short}} instance, follow the steps in [Creating a policy](/docs/cloud-logs?topic=cloud-logs-tco-optimizer#tco-optimizer-create-policy) to add and apply a new TCO Optimizer policy with these settings:
- Application = `ibm-audit-event`
- Subsystem = CRN for the specific {{site.data.keyword.cos_short}} instance, `CRNserviceName:instanceID`

## Route Logs with {{site.data.keyword.at_full_notm}} Event Routing
{: #at-route-logs}

[Get started with {{site.data.keyword.at_full_notm}} Event Routing](/docs/atracker?topic=atracker-getting-started) to configure routing for your IBM Cloud Object Storage auditing events. You can use Activity Tracker Event Routing, a platform service, to manage auditing events at the account-level by configuring targets and routes that define where auditing data is routed.

Activity Tracker Event Routing supports routing IBM COS bucket logs to the following targets
-	[Another COS Bucket](/docs/atracker?topic=atracker-getting-started-target-cos)
-	[Event Streams](/docs/atracker?topic=atracker-getting-started-target-event-streams)
-	[IBM Cloud Logs](/docs/cloud-logs?topic=cloud-logs-getting-started)


## Configure Activity Tracking Events on your IBM Cloud Object Storage Bucket (Recommended)
{: #at-configure}

Event tracking can be enabled on your IBM Cloud Object Storage bucket at the time of bucket provisioning, or by updating the bucket configuration after bucket creation. Event tracking will only apply to COS requests made after enablement.

By default, COS events that report on global actions, such as bucket creation, are collected automatically. You can monitor global actions through the Activity Tracker instance located in the Frankfurt location.

IBM COS also optionally supports tracking on these event types:
- Management Events – Requests related to managing bucket and object configuration
- Read Data Events – Requests related to object list and read requests
- Write Data Events – These are all events related to writing and deleting objects

Refer to the COS API events to see the full list of Management, Read Data, and Write Data actions that produce events.

Use the [COS Resource Configuration API](/apidocs/cos/cos-configuration) to configure tracking of these events on your bucket

When event tracking is enabled, all events are sent to the default receiving location for IBM Cloud Activity Tracker Event Router that are based on the location of the bucket. Refer to [IBM COS Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability) to see this default mapping. Use Activity Tracker Event Router rules to route events to an alternative location or target service. See [Managing Rules](/docs/atracker?topic=atracker-route_v2) to learn more.

### How to configure {{site.data.keyword.at_full_notm}} on your bucket (Recommended)
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

### Examples
{: #at-api-example-recommended}
{: api}

JAVA SDK

   ```sh
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.ResourceConfiguration;
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.model.BucketPatch;
   import com.ibm.cloud.sdk.core.security.IamAuthenticator;

   public class ActivityTrackerExample {
      private static final String BUCKET_NAME = <BUCKET_NAME>;
      private static final String API_KEY = <API_KEY>;

      public static void main(String[] args) {
          IamAuthenticator authenticator = new IamAuthenticator.Builder()
                  .apiKey(API_KEY)
                  .build();
          ResourceConfiguration RC_CLIENT = new ResourceConfiguration("resource-configuration", authenticator);
          ActivityTracking activityTrackingConfig = new ActivityTracking().Builder()
                  .readDataEvents(true)
                  .writeDataEvents(true)
                  .managementEvents(true)
                  .build();
          BucketPatch bucketPatch = new BucketPatch.Builder().activityTracking(activityTrackingConfig).build();
          UpdateBucketConfigOptions update = new UpdateBucketConfigOptions
                  .Builder(BUCKET_NAME)
                  .bucketPatch(bucketPatch.asPatch())
                  .build();

          RC_CLIENT.updateBucketConfig(update).execute();
          GetBucketConfigOptions bucketOptions = new GetBucketConfigOptions.Builder(BUCKET_NAME).build();
          Bucket bucket = RC_CLIENT.getBucketConfig(bucketOptions).execute().getResult();

          ActivityTracking activityTrackingResponse = bucket.getActivityTracking();
          System.out.println("Read Data Events : " + activityTrackingResponse.readDataEvents());
          System.out.println("Write Data Events : " + activityTrackingResponse.writeDataEvents());
          System.out.println("Management Events : " + activityTrackingResponse.managementEvents());
      }
   }
   ```
   {: codeblock}

NodeJS SDK

   ```sh
    const ResourceConfigurationV1 = require('ibm-cos-sdk-config/resource-configuration/v1');
    IamAuthenticator      = require('ibm-cos-sdk-config/auth');

    var apiKey = "<API_KEY>"
    var bucketName = "<BUCKET_NAME>"

    authenticator = new IamAuthenticator({apikey: apiKey})
    rcConfig = {authenticator: authenticator}
    const client = new ResourceConfigurationV1(rcConfig);

    function addAT() {
        console.log('Updating bucket metadata...');

        var params = {
            bucket: bucketName,
            activityTracking: {
              "read_data_events": true,
              "write_data_events": true,
              "management_events": true
              }
        };

        client.updateBucketConfig(params, function (err, response) {
            if (err) {
                console.log("ERROR: " + err);
            } else {
                console.log(response.result);
            }
        });
    }

    addAT()
   ```
    {: codeblock}

Python SDK

   ```sh
    from ibm_cos_sdk_config.resource_configuration_v1 import ResourceConfigurationV1
    from ibm_cloud_sdk_core.authenticators import IAMAuthenticator

    api_key = "<API_KEY>"
    bucket_name = "<BUCKET_NAME>"

    authenticator = IAMAuthenticator(apikey=api_key)
    client = ResourceConfigurationV1(authenticator=authenticator)
    activity_tracking_config = {
                                'activity_tracking':
                                  {
                                    'read_data_events':True,
                                    'write_data_events':True,
                                    'management_events':True
                                  }
                                }
    client.update_bucket_config(bucket_name, bucket_patch=activity_tracking_config)
   ```
    {: codeblock}

GO SDK example

   ```sh
    import (
    "github.com/IBM/go-sdk-core/core"
    rc "github.com/IBM/ibm-cos-sdk-go-config/v2/resourceconfigurationv1"
    )

    apiKey := "<ApiKey>"
    bucketName := "<BucketName>"

    authenticator := new(core.IamAuthenticator)
    authenticator.ApiKey = apiKey
    optionsRC := new(rc.ResourceConfigurationV1Options)
    optionsRC.Authenticator = authenticator
    rcClient, _ := rc.NewResourceConfigurationV1(optionsRC)

    patchNameMap := make(map[string]interface{})
    patchNameMap["activity_tracking"] = &rc.ActivityTracking{
      ReadDataEvents:     core.BoolPtr(true),
      WriteDataEvents:    core.BoolPtr(true),
      ManagementEvents:    core.BoolPtr(true)
    }
    updateBucketConfigOptions := &rc.UpdateBucketConfigOptions{
      Bucket:      core.StringPtr(bucketName),
      BucketPatch: patchNameMap,
    }
    rcClient.UpdateBucketConfig(updateBucketConfigOptions)
   ```
    {: codeblock}

### Example
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
      bucket_name          = “Name-of-the-bucket”
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
    {: codeblock}


## Configure Activity Tracking Events on your IBM Cloud Object Storage Bucket (Legacy)
{: #at-configure-legacy}

Enable IBM Activity Tracking on your COS bucket by specifying the target CRN of the Activity Tracker instance in the [COS Resource Configuration API](/apidocs/cos/cos-configuration). Specify the CRN to define the route for COS events.

Management events are always enabled when a CRN is set on the Activity Tracking configuration

The legacy model also supports optionally enabling tracking on the following event types:
- Read Data Events – Requests related to object list and read requests
- Write Data Events – These are all events related to writing and deleting objects

IBM Cloud observability routing services are the standardized way for customers to manage routing of platform observability data.  Service-specific routing configurations like COS are being deprecated.
{: note}

It is recommended that customers [remove these legacy routing configurations](/docs/cloud-object-storage?topic=cloud-object-storage-at&interface=ui#at-legacy-upgrade) that use CRNs and instead use the IBM Activity Tracker Event Routing service to route events to other locations.

IBM COS will continue to support legacy configurations where a CRN was specified that differs from the default location.

### Upgrading from Legacy to the Recommended Event Tracking on your COS bucket
{: #at-legacy-upgrade}

To upgrade from the legacy configuration using the Resource Configuration API, remove the target Activity Tracker instance CRN. Events will now route to the default Activity Tracker Event Router receiving location as described in [COS Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability). Provision an instance of Activity Tracker hosted event search at this location or define a routing rule prior to upgrading to ensure there’s no interruption in event logging.

### Example patch to transition from the Legacy to Recommended event tracking configuration on your COS bucket
{: #at-legacy-upgrade-examples}

Select the UI, API, or Terraform tab at the top of this topic to see examples of patchs.

### UI example patch to transition from the Legacy to Recommended event tracking configuration on your COS bucket
{: #at-ui-example-legacy}
{: ui}

1.	From the IBM Cloud console [resource list](https://cloud.ibm.com/resources), select the service instance that contains the bucket you wish to upgrade to the recommended event tracking configuration. This takes you to the Object Storage Console.
2.	Choose the bucket for which you want to upgrade.
3.	Navigate to the configuration tab.
4.	Scroll down to the advanced configuration section and locate the configuration panel for Activity Tracker.
5.	Click on the top right corner of the panel and select upgrade.
6.	Confirm you would like to upgrade event tracking for this bucket.


### Examples
{: #at-api-example-legacy}
{: api}

JAVA SDK

   ```sh
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.ResourceConfiguration;
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.model.BucketPatch;
   import com.ibm.cloud.sdk.core.security.IamAuthenticator;

   public class ActivityTrackerExample {
      private static final String BUCKET_NAME = <BUCKET_NAME>;
      private static final String API_KEY = <API_KEY>;

      public static void main(String[] args) {
          IamAuthenticator authenticator = new IamAuthenticator.Builder()
                  .apiKey(API_KEY)
                  .build();
          ResourceConfiguration RC_CLIENT = new ResourceConfiguration("resource-configuration", authenticator);
          ActivityTracking activityTrackingConfig = new ActivityTracking().Builder()
                .activityTrackerCrn(AT_CRN)
                .readDataEvents(true)
                .writeDataEvents(true)
                .build();
          BucketPatch bucketPatch = new BucketPatch.Builder().activityTracking(activityTrackingConfig).build();
          UpdateBucketConfigOptions update = new UpdateBucketConfigOptions
                  .Builder(BUCKET_NAME)
                  .bucketPatch(bucketPatch.asPatch())
                  .build();

          RC_CLIENT.updateBucketConfig(update).execute();
          GetBucketConfigOptions bucketOptions = new GetBucketConfigOptions.Builder(BUCKET_NAME).build();
          Bucket bucket = RC_CLIENT.getBucketConfig(bucketOptions).execute().getResult();

          ActivityTracking activityTrackingResponse = bucket.getActivityTracking();
          System.out.println("Read Data Events : " + activityTrackingResponse.readDataEvents());
          System.out.println("Write Data Events : " + activityTrackingResponse.writeDataEvents());
          System.out.println("Management Events : " + activityTrackingResponse.managementEvents());
      }
   }
   ```
   {: codeblock}

NodeJS SDK

   ```sh
    const ResourceConfigurationV1 = require('ibm-cos-sdk-config/resource-configuration/v1');
    IamAuthenticator      = require('ibm-cos-sdk-config/auth');

    var apiKey = "<API_KEY>"
    var bucketName = "<BUCKET_NAME>"

    authenticator = new IamAuthenticator({apikey: apiKey})
    rcConfig = {authenticator: authenticator}
    const client = new ResourceConfigurationV1(rcConfig);

    function addAT() {
        console.log('Updating bucket metadata...');

        };
        var params = {
            bucket: bucketName,
            activityTracking: {
              "activity_tracker_crn": at_crn,
              "read_data_events": true,
              "write_data_events": true
              }
        };

        client.updateBucketConfig(params, function (err, response) {
            if (err) {
                console.log("ERROR: " + err);
            } else {
                console.log(response.result);
            }
        });
    }

    addAT()
   ```
    {: codeblock}

Python SDK

   ```sh
    from ibm_cos_sdk_config.resource_configuration_v1 import ResourceConfigurationV1
    from ibm_cloud_sdk_core.authenticators import IAMAuthenticator

    api_key = "<API_KEY>"
    bucket_name = "<BUCKET_NAME>"

    authenticator = IAMAuthenticator(apikey=api_key)
    client = ResourceConfigurationV1(authenticator=authenticator)
    activity_tracking_config = {
                                'activity_tracking':
                                  {
                                    'activity_tracker_crn':at_crn,
                                    'read_data_events':True,
                                    'write_data_events':True,
                                  }
                                }

    client.update_bucket_config(bucket_name, bucket_patch=activity_tracking_config)
   ```
    {: codeblock}

GO SDK

   ```sh
    import (
    "github.com/IBM/go-sdk-core/core"
    rc "github.com/IBM/ibm-cos-sdk-go-config/v2/resourceconfigurationv1"
    )

    apiKey := "<ApiKey>"
    bucketName := "<BucketName>"

    authenticator := new(core.IamAuthenticator)
    authenticator.ApiKey = apiKey
    optionsRC := new(rc.ResourceConfigurationV1Options)
    optionsRC.Authenticator = authenticator
    rcClient, _ := rc.NewResourceConfigurationV1(optionsRC)

    patchNameMap := make(map[string]interface{})
    patchNameMap["activity_tracking"] = &rc.ActivityTracking{
      ActivityTrackerCrn: core.StringPtr(activityTrackerCrn),
      ReadDataEvents:     core.BoolPtr(true),
      WriteDataEvents:    core.BoolPtr(true),
    }
    updateBucketConfigOptions := &rc.UpdateBucketConfigOptions{
      Bucket:      core.StringPtr(bucketName),
      BucketPatch: patchNameMap,
    }
    rcClient.UpdateBucketConfig(updateBucketConfigOptions)
   ```
    {: codeblock}

### Example
{: #at-terraform-example-legacy}
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
        activity_tracker_crn = “crn:v1:bluemix:public:logdnaat:us-south:a/2xxxxxxxxxxxxxxxxxxxxxxxxf:3xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxec::”
          }
    }
   ```
    {: codeblock}
