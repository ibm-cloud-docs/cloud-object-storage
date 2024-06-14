---

copyright:
  years: 2020, 2024
lastupdated: "2024-06-12"

keywords: Object Storage, SysDig, monitoring, integration, metrics, legacy, recommended, routing

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}
{:http: .ph data-hd-programlang='http'}
{:console: .ph data-hd-programlang='Console'}
{:cli: .ph data-hd-programlang='CLI'}

# Configure Metrics for {{site.data.keyword.cos_full}}
{: #mm-cos-integration}

Use the [IBM Cloud® Monitoring](/docs/monitoring?topic=monitoring-getting-started) service to monitor your {{site.data.keyword.cos_full}} data. IBM Cloud Monitoring is a cloud-native management system. The metrics produced by your COS buckets can be displayed in dashboards built in IBM Monitoring. Documentation from [Monitoring](/docs/monitoring?topic=monitoring-dashboards) can guide you in how to use the comprehensive dashboards. Additionally, [specify the conditions when a metrics alert is trigged](/docs/monitoring?topic=monitoring-alert-metric#alert_metrics_trigger) to set notifications when custom thresholds are exceeded.

When metrics monitoring is enabled on your bucket, the default target service that captures these metrics is [IBM Cloud Monitoring](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration#mm-configure). Ensure that you have a platform instance of IBM Cloud Monitoring at the receiving location corresponding to your bucket location as specified in COS Service Integration.

Alternatively, use [IBM Cloud Metrics Routing](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration#mm-route-metrics) rules to send metrics to other target services or to IBM Cloud Monitoring instances in locations other than the bucket location.

Enable metrics monitoring on your bucket via the [{{site.data.keyword.cos_full}} Resource Configuration API](/apidocs/cos/cos-configuration) or through the UI directly. This is done during bucket provisioning or afterwards by updating the bucket configuration.

IBM COS supports enabling metrics tracking on the following metric types:
- Usage Metrics – These are metrics related to the overall usage of your COS bucket such as total storage consumed in bytes.
- Request Metrics – The metrics report the counts for certain types of API requests made to your bucket

See the {{site.data.keyword.cos_full}} metrics details section below for the full list of metrics sent to IBM Monitoring.

This feature is not currently supported in [Object Storage for Satellite](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite).
{: note}

This feature supports [SCC monitoring](/docs/security-compliance).
{: note}

{{site.data.keyword.cos_full}} metrics can only be consumed by IBM Monitoring platform instances. If a platform instance does not exist at the final receiving location, ensure one is created.
{: note}

## Route Metrics with IBM Cloud Metrics Routing
{: #mm-route-metrics}

Use IBM Cloud® Metrics Routing to route metrics for your {{site.data.keyword.cos_full}} buckets to locations different from the bucket location or to other target services. You can use Metrics Routing, a platform service, to manage targets and routes that define where metrics data is routed.

When enabling monitoring on your {{site.data.keyword.cos_full}} buckets, metrics are sent to a default receiving location as defined in [COS Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability). You must have an instance of IBM Monitoring at this location or configure a routing rule to another location with a Monitoring instance, to ensure metrics are received.

See [Getting started with IBM Cloud Metrics Routing](/docs/metrics-router?topic=metrics-router-getting-started) for more information.

## Configure Metrics on your {{site.data.keyword.cos_full}} Bucket (Recommended)
{: #mm-configure}

Enable metrics tracking on your {{site.data.keyword.cos_full}} bucket at the time of bucket provisioning or by updating the bucket configuration after bucket creation. Metrics monitoring will only apply to {{site.data.keyword.cos_full}} metrics produced after enablement.

Refer to the [{{site.data.keyword.cos_full}} Metrics Details](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration#mm-cos-metrics-details) to see the full list of Usage and Request metrics available for tracking.

Use the [{{site.data.keyword.cos_full}} Resource Configuration API](/apidocs/cos/cos-configuration) to configure tracking of these metrics for your bucket.

When metrics tracking is enabled, all metrics are sent to the default receiving location for IBM Cloud Metrics Router based on the location of the bucket. Refer to [{{site.data.keyword.cos_full}} Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability) to see this default mapping. Use Metrics Router rules to route metrics to a location other than the bucket location or to another target service. See [Managing Routes](/docs/metrics-router?topic=metrics-router-route-manage) for more information.

### How to configure Metrics for {{site.data.keyword.cos_full}} (Recommended)
{: #mm-examples-recommended}

Select the UI, API or Terraform tab at the top of this topic to display the examples that show how to configure metrics monitoring to track both usage and request metrics on your bucket.

### UI example for how to configure Metrics Monitoring on your bucket
{: #mm-ui-example-recommended}
{: ui}

1.	From the IBM Cloud console [resource list](https://cloud.ibm.com/resources), select the service instance that contains the bucket you are interested in adding metrics monitoring. This takes you to the Object Storage Console
2.	Choose the bucket for which you want to enable monitoring.
3.	Navigate to the configuration tab.
4.	Scroll down to the advanced configuration section and toggle on the metrics you want to monitor for this bucket.
5.	After a few minutes, any activity will be visible in the IBM Cloud Monitoring web UI.

### Examples
{: #mm-api-example-recommended}
{: api}

JAVA SDK

   ```sh
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.ResourceConfiguration;
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.model.BucketPatch;
   import com.ibm.cloud.sdk.core.security.IamAuthenticator;

   public class MetricsMonitoringExample {

      private static final String BUCKET_NAME = <BUCKET_NAME>;
      private static final String API_KEY = <API_KEY>;

      public static void main(String[] args) {
         IamAuthenticator authenticator = new IamAuthenticator.Builder()
                  .apiKey(API_KEY)
                  .build();
         ResourceConfiguration RC_CLIENT = new ResourceConfiguration("resource-configuration", authenticator);
         MetricsMonitoring metricsMonitoringConfig = new MetricsMonitoring().Builder()
               .requestMetricsEnabled(true)
               .usageMetricsEnabled(true)
               .build();
         BucketPatch bucketPatch = new BucketPatch.Builder().metricsMonitoring(metricsMonitoringConfig).build();
         UpdateBucketConfigOptions update = new UpdateBucketConfigOptions
                  .Builder(BUCKET_NAME)
                  .bucketPatch(bucketPatch.asPatch())
                  .build();
         RC_CLIENT.updateBucketConfig(update).execute();

         GetBucketConfigOptions bucketOptions = new GetBucketConfigOptions.Builder(BUCKET_NAME).build();
         Bucket bucket = RC_CLIENT.getBucketConfig(bucketOptions).execute().getResult();
         MetricsMonitoring metricsMonitoringResponse = bucket.getMetricsMonitoring();
         System.out.println("Usage Metrics Enabled  : " + metricsMonitoringResponse.usageMetricsEnabled());
         System.out.println("Request Metrics Enabled : " + metricsMonitoringResponse.requestMetricsEnabled());
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

   function addMM() {
      console.log('Updating bucket metadata...');

      var params = {
         bucket: bucketName,
         metricsMonitoring: {
            "request_metrics_enabled": true,
            "usage_metrics_enabled": true
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

   addMM()
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
   metrics_monitoring_config = {'metrics_monitoring': 
                              {
                                 'request_metrics_enabled':True, 
                                 'usage_metrics_enabled':True
                                 }
                              }
   client.update_bucket_config(bucket_name, bucket_patch=metrics_monitoring_config
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
   patchNameMap["metrics_monitoring"] = &rc.MetricsMonitoring{
   RequestMetricsEnabled:     core.BoolPtr(true),
   UsageMetricsEnabled:    core.BoolPtr(true)
   }
   updateBucketConfigOptions := &rc.UpdateBucketConfigOptions{
   Bucket:      core.StringPtr(bucketName),
   BucketPatch: patchNameMap,
   }
   rcClient.UpdateBucketConfig(updateBucketConfigOptions)
   ```
   {: codeblock}

### Example
{: #mm-terraform-example-recommended}
{: terraform}

   ```sh
   resource "ibm_resource_instance" "cos_instance" {
      name              = "cos-instance"
      resource_group_id = data.ibm_resource_group.cos_group.id
      service           = "cloud-object-storage"
      plan              = "standard"
      location          = "global"
   }

   resource "ibm_cos_bucket" "metric_monitoring_bucket" {
      bucket_name          = “bucket_name”
      resource_instance_id = ibm_resource_instance.cos_instance.id
      region_location      = “us-south”
      storage_class        = “standard”
      metrics_monitoring {
         usage_metrics_enabled   = true
         request_metrics_enabled = true
   }
   }
   ```
   {: codeblock}

## Configure Metrics on your {{site.data.keyword.cos_full}} Bucket (Legacy)
{: #mm-configure-legacy}

Enable IBM Metrics Monitoring on your {{site.data.keyword.cos_full}} bucket by specifying the target CRN of the Monitoring instance in the {{site.data.keyword.cos_full}} Resource Configuration API. Specify the CRN to define the route for COS metrics.

IBM Cloud Metrics Routing is the standardized way for customers to manage routing of platform observability data.  Service-specific routing configurations like {{site.data.keyword.cos_full}} are being deprecated.
{: note}

It is recommended that customers remove these [legacy routing configurations](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration#mm-legacy-upgrade) (make this a link to upgrade section below) that use CRNs and instead use the IBM Metrics Router service to route metrics to other locations.

{{site.data.keyword.cos_full}} will continue to support legacy configurations where a CRN was specified that differs from the default location.

### Upgrading from Legacy to the Recommended Metrics Monitoring on your COS bucket:
{: #mm-legacy-upgrade}

To upgrade from the legacy configuration using the Resource Configuration API, remove the target Metrics Monitoring instance CRN. Metrics will now route to the default Metrics Router receiving location as described in [COS Service Integration](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability). Provision an instance of Monitoring at this location or define a routing rule prior to upgrading to ensure there’s no interruption in metrics monitoring.

### Example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #mm-legacy-upgrade-examples}

Select the UI, API or Terraform tab at the top of this topic to see examples of patchs.

### UI example patch to transition from the Legacy to Recommend event tracking configuration on your COS bucket
{: #mm-ui-example-legacy}
{: ui}

Example patch to transition from the Legacy to Recommend metrics monitoring configuration on your {{site.data.keyword.cos_full}} bucket (SDK, RC API, UI, Terraform)

1.	From the IBM Cloud console resource list, select the service instance that contains the bucket you wish to upgrade to the recommended metrics monitoring configuration. This takes you to the Object Storage Console.
2.	Choose the bucket for which you want to upgrade.
3.	Navigate to the configuration tab.
4.	Scroll down to the advanced configuration section and locate the configuration panel for metrics monitoring.
5.	Click on the top right corner of the panel and select upgrade.
6.	Confirm you would like to upgrade metrics monitoring for this bucket.

### Examples
{: #mm-api-example-legacy}
{: api}

JAVA SDK

   ```sh
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.ResourceConfiguration;
   import com.ibm.cloud.objectstorage.config.resource_configuration.v1.model.BucketPatch;
   import com.ibm.cloud.sdk.core.security.IamAuthenticator;

   public class MetricsMonitoringExample {

      private static final String BUCKET_NAME = <BUCKET_NAME>;
      private static final String API_KEY = <API_KEY>;

      public static void main(String[] args) {
         IamAuthenticator authenticator = new IamAuthenticator.Builder()
                  .apiKey(API_KEY)
                  .build();
         ResourceConfiguration RC_CLIENT = new ResourceConfiguration("resource-configuration", authenticator);
         MetricsMonitoring metricsMonitoringConfig = new MetricsMonitoring().Builder()
               .metricsMonitoringCrn(MM_CRN)
               .requestMetricsEnabled(true)
               .usageMetricsEnabled(true)
               .build();
         BucketPatch bucketPatch = new BucketPatch.Builder().metricsMonitoring(metricsMonitoringConfig).build();
         UpdateBucketConfigOptions update = new UpdateBucketConfigOptions
                  .Builder(BUCKET_NAME)
                  .bucketPatch(bucketPatch.asPatch())
                  .build();
         RC_CLIENT.updateBucketConfig(update).execute();

         GetBucketConfigOptions bucketOptions = new GetBucketConfigOptions.Builder(BUCKET_NAME).build();
         Bucket bucket = RC_CLIENT.getBucketConfig(bucketOptions).execute().getResult();
         MetricsMonitoring metricsMonitoringResponse = bucket.getMetricsMonitoring();
         System.out.println("Usage Metrics Enabled  : " + metricsMonitoringResponse.usageMetricsEnabled());
         System.out.println("Request Metrics Enabled : " + metricsMonitoringResponse.requestMetricsEnabled());
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

   function addMM() {
      console.log('Updating bucket metadata...');

      var params = {
         bucket: bucketName,
         metricsMonitoring: {
                  "metrics_monitoring_crn": metricsCrn,
            "request_metrics_enabled": true,
            "usage_metrics_enabled": true
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

   addMM()
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
   metrics_monitoring_config = {'metrics_monitoring':
                              {
                           'metrics_monitoring_crn': mm_crn,
                                 'request_metrics_enabled':True,
                                 'usage_metrics_enabled':True
                                 }
                              }

   client.update_bucket_config(bucket_name, bucket_patch=metrics_monitoring_config
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

   patchNameMap["metrics_monitoring"] = &rc.MetricsMonitoring{
   MetricsMonitoringCrn: core.StringPtr(MMCrn),
   RequestMetricsEnabled:     core.BoolPtr(true),
   UsageMetricsEnabled:    core.BoolPtr(true)
   }

   updateBucketConfigOptions := &rc.UpdateBucketConfigOptions{
   Bucket:      core.StringPtr(bucketName),
   BucketPatch: patchNameMap,
   }
   rcClient.UpdateBucketConfig(updateBucketConfigOptions)
   ```
   {: codeblock}

### Example
{: #mm-terraform-example-legacy}
{: terraform}

   ```sh
   resource "ibm_resource_instance" "cos_instance" {
      name              = "cos-instance"
      resource_group_id = data.ibm_resource_group.cos_group.id
      service           = "cloud-object-storage"
      plan              = "standard"
      location          = "global"
   }

   resource "ibm_cos_bucket" "metric_monitoring_bucket" {
      bucket_name          = “bucket_name”
      resource_instance_id = ibm_resource_instance.cos_instance.id
      region_location      = “us-south”
      storage_class        = “standard”
      metrics_monitoring {
         usage_metrics_enabled   = true
         request_metrics_enabled = true
         metrics_monitoring_crn = "crn:v1:bluemix:public:sysdig-monitor:us-east:a/xxxxxxxxxxxxxxxxxxxxxxxxx:4xxxxxxxx-xxxx-xxxx-xxxx-fxxxxxxxxx4c::"
   }
   }
   ```
   {: codeblock}

## Cloud Object Storage metrics details
{: #mm-cos-metrics-details}

### Usage metrics
{: #mm-cos-metrics-usage}

There are a set of basic metrics that track usage:

* `ibm_cos_bucket_used_bytes`
* `ibm_cos_bucket_object_count`
* `ibm_cos_bucket_hard_quota_bytes`

### Request metrics
{: #mm-cos-metrics-request}

There are metrics that report the aggregates for different classes of HTTP requests:

* `ibm_cos_bucket_all_requests`
* `ibm_cos_bucket_get_requests`
* `ibm_cos_bucket_put_requests`
* `ibm_cos_bucket_delete_requests`
* `ibm_cos_bucket_post_requests`
* `ibm_cos_bucket_list_requests`
* `ibm_cos_bucket_head_requests`

Errors are also collected, with server-side (5xx) errors broken out:

* `ibm_cos_bucket_4xx_errors`
* `ibm_cos_bucket_5xx_errors`

The minimum, maximum, and average bytes transferred by network type are reported:

* `ibm_cos_bucket_bytes_download_public_min`
* `ibm_cos_bucket_bytes_download_public_max`
* `ibm_cos_bucket_bytes_download_public_avg`
* `ibm_cos_bucket_bytes_download_private_min`
* `ibm_cos_bucket_bytes_download_private_max`
* `ibm_cos_bucket_bytes_download_private_avg`
* `ibm_cos_bucket_bytes_download_direct_min`
* `ibm_cos_bucket_bytes_download_direct_max`
* `ibm_cos_bucket_bytes_download_direct_avg`
* `ibm_cos_bucket_bytes_upload_public_min`
* `ibm_cos_bucket_bytes_upload_public_max`
* `ibm_cos_bucket_bytes_upload_public_avg`
* `ibm_cos_bucket_bytes_upload_private_min`
* `ibm_cos_bucket_bytes_upload_private_max`
* `ibm_cos_bucket_bytes_upload_private_avg`
* `ibm_cos_bucket_bytes_upload_direct_min`
* `ibm_cos_bucket_bytes_upload_direct_max`
* `ibm_cos_bucket_bytes_upload_direct_avg`

Latency metrics (first byte and general) for requests are broken down by request type:

* `ibm_cos_bucket_first_byte_latency_read_min`
* `ibm_cos_bucket_first_byte_latency_read_max`
* `ibm_cos_bucket_first_byte_latency_read_avg`
* `ibm_cos_bucket_first_byte_latency_write_min`
* `ibm_cos_bucket_first_byte_latency_write_max`
* `ibm_cos_bucket_first_byte_latency_write_avg`
* `ibm_cos_bucket_first_byte_latency_misc_min`
* `ibm_cos_bucket_first_byte_latency_misc_max`
* `ibm_cos_bucket_first_byte_latency_misc_avg`
* `ibm_cos_bucket_request_latency_read_min`
* `ibm_cos_bucket_request_latency_read_max`
* `ibm_cos_bucket_request_latency_read_avg`
* `ibm_cos_bucket_request_latency_write_min`
* `ibm_cos_bucket_request_latency_write_max`
* `ibm_cos_bucket_request_latency_write_avg`
* `ibm_cos_bucket_request_latency_misc_min`
* `ibm_cos_bucket_request_latency_misc_max`
* `ibm_cos_bucket_request_latency_misc_avg`

All metrics are reported as `float64` numeric values:

## Attributes for Segmentation
{: #mm-cos-attributes}

You can filter your results by attributes. In this guide, we'll look at some general examples as well as those specific to {{site.data.keyword.cos_full_notm}}.

### Global Attributes
{: #mm-cos-global-attributes}

The following attributes are available for segmenting all the metrics listed above

| Attribute | Attribute Name | Attribute Description |
|-----------|----------------|-----------------------|
| `Cloud Type` | `ibm_ctype` | public, dedicated or local |
| `Location` | `ibm_location` | The location of the monitored resource. This may be a Cross Region, Regional, or Single Site bucket. |
| `Resource` | `ibm_resource` | COS bucket name |
| `Resource Type` | `ibm_resource_type` | COS bucket |
| `Scope` | `ibm_scope` | The scope is the account associated with this metric. |
| `Service name` | `ibm_service_name` | cloud-object-storage |
{: caption="Table 4: IBM global attributes" caption-side="top"}

### Additional Attributes
{: #mm-cos-additional-attributes}

The following attributes are available for segmenting one or more attributes as described in the reference above.  Please see the individual metrics for segmentation options.

| Attribute | Attribute Name | Attribute Description |
|-----------|----------------|-----------------------|
| `IBM COS Bucket storage class` | `ibm_cos_bucket_storage_class` | Storage class of the bucket |
| `Service instance` | `ibm_service_instance` | The service instance segment identifies the guide of the instance the metric is associated with. |
{: caption="Table 5: COS specific attributes" caption-side="top"}


