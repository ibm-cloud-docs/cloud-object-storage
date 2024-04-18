---

copyright:
  years: 2017, 2024
lastupdated: "2024-04-18"

keywords:

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}
{:http: .ph data-hd-programlang='http'}
{:console: .ph data-hd-programlang='Console'}
{:cli: .ph data-hd-programlang='CLI'}

# Monitoring metrics for {{site.data.keyword.cos_full_notm}}
{: #mm-cos-monitoring}

Use the {{site.data.keyword.mon_full}} service to monitor your {{site.data.keyword.cos_full}} (COS) data in the {{site.data.keyword.cloud_notm}}. The results of the activity can be measured for compliance and other analysis through the web dashboard UI.
{: shortdesc}

{{site.data.keyword.mon_full_notm}} is a cloud-native management system. Documentation from [{{site.data.keyword.mon_short}}](/docs/Monitoring-with-Sysdig?topic=Monitoring-with-Sysdig-monitoring#monitoring_dashboards) can guide you in how to use the comprehensive dashboards. In this guide we will focus on how to measure activity on individual buckets in your instance of {{site.data.keyword.cos_full_notm}}.

This feature is not currently supported in {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}. [Learn more.](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite)
{: note}

## Configuring a bucket to collect platform metrics
{: #mm-cos-monitoring-config}

A metric is a quantitative measure that has one or more labels to define its characteristics.

To collect metrics for a bucket, you must configure your bucket and indicate the {{site.data.keyword.mon_short}} instance where data is automatically collected and available for analysis through the monitoring web UI. This {{site.data.keyword.mon_short}} instance must be the one in the region that has been enabled to collect platform metrics. For more information, see [Enabling platform metrics](/docs/monitoring?topic=monitoring-platform_metrics_enabling).

You can configure request metrics and usage metrics.

- Usage metrics are only sent to {{site.data.keyword.mon_short}} every 24 hours. 

    Any alert thresholds exceeded on usage metrics will not trigger until the next time the metrics are sent to the {{site.data.keyword.mon_short}} service.{: important}

- Request metrics are sent continuously.

You can configure a bucket by using the UI or the API.

To configure a regional bucket, consider the following information:

- When you use the UI, you must set the {{site.data.keyword.mon_short}} instance that is located in the same region as the bucket.
- When you use the [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration){: external}, you can set a {{site.data.keyword.mon_short}} instance that is available in your {{site.data.keyword.cloud_notm}} account.

To configure a cross-regional bucket, consider the following information:
- When you use the UI, you must set the {{site.data.keyword.mon_short}} instance that is defined per geography. see [Location of metrics](#mm-cos-monitoring-locations).
- When you use the [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration){: external}, you can set a {{site.data.keyword.mon_short}} instance that is available in your {{site.data.keyword.cloud_notm}} account.

### Configuring a bucket to collect platform metrics by using the API
{: #mm-cos-monitoring-config-api}

For example, you can run the following command to configure monitoring for a bucket:

```bash
curl -X PATCH -k  \
  -H "authorization: Bearer $TOKEN" \
  https://config.cloud-object-storage.cloud.ibm.com/v1/b/$1 \
  -H 'cache-control: no-cache' \
  -d '{"metrics_monitoring": {
    "usage_metrics_enabled": true,
    "request_metrics_enabled": true,
    "metrics_monitoring_crn": "crn:v1:bluemix:public:sysdig-monitor:us-east:a/9xxxxxxxxxb1xxxc7fdxxxxxxxxxx5:7xxxxxxxx0-xx7x-xdx8-9fxx-123456789012::"
    }
   }'

{: codeblock}

## Locations of metrics
{: #mm-cos-monitoring-locations}

The following table list the locations where platform metrics are collected when you configure a regional bucket to collect metrics:

| Geography             | Location                   | Location where metrics are available | On October 30th, 2023 |
|-----------------------|--------------------------|-------------------------------------|-------------------------|
| `Asia Pacific`        | `Sydney (au-syd)`        | `Sydney (au-syd)` | |
| `Asia Pacific`        | `Osaka (jp-osa)`         | `Osaka (jp-osa)` | |
| `Asia Pacific`        | `Tokyo (jp-tok)`         | `Tokyo (jp-tok)` | |
| `Asia Pacific`        | `Chennai (che01)`        | `Tokyo (jp-tok)` | |
| `Asia Pacific`        | `Singapore (sng01)`      | `Tokyo (jp-tok)` | |
| `Europe`              | `Frankfurt (eu-de)`      | `Frankfurt (eu-de)` | |
| `Europe`              | `London (eu-gb)`         | `London (eu-gb)` | |
| `Europe`              | `Madrid (eu-es)`         | `Frankfurt (eu-de)` | `Madrid (eu-es)` `(*)` |
| `Europe`              | `Amsterdam (ams03)`      | `Frankfurt (eu-de)` | |
| `Europe`              | `Milan (mil01)`          | `Frankfurt (eu-de)` | |
| `Europe`              | `Paris (par01)`          | `Frankfurt (eu-de)` | |
| `North America`       | `Dallas (us-south)`      | `Dallas (us-south)` | |
| `North America`       | `Toronto (ca-tor)`       | `Toronto (ca-tor)` | |
| `North America`       | `Washington (us-east)`   | `Washington (us-east)` | |
| `North America`       | `Montreal (mon01)`       | `Dallas (us-south)` | |
| `North America`       | `San Jose (sjc04)`       | `Dallas (us-south)` | |
| `South America`       | `Sao Paulo (br-sao)`     | `Sao Paulo (br-sao)` | |
{: caption="Table 9. Location where platform metrics are available for regional buckets" caption-side="bottom"}

`(*)` On October 30th, platform metrics that you opt-in for a bucket that is provisioned in Madrid will be sent to Madrid. Currently, these platform metrics are sent to Frankfurt. to collect these metrics in Madrid on October 30th, you must use the API to reconfigure the {{site.data.keyword.mon_short}} instance to the one located in Madrid. Otherwise, these metrics will continue to go to Frankfurt.

The following table list the locations where platform metrics are collected when you configure a cross-regional bucket to collect metrics:

| Geography             | Location where metrics are available |
|-----------------------|--------------------------|
| `Asia Pacific`        | `Tokyo (jp-tok)` |
| `United States (US)`  | `Dallas (us-south)` |
| `Europe (EU)`         | `Frankfurt (eu-de)` |
{: caption="Table 10. Location where platform metrics are available for cross-regional bucket" caption-side="bottom"}

When you configure a bucket to collect platform metrics by using the API, you can choose a {{site.data.keyword.mon_short}} instance that is located in a different region from the one where the bucket is located. The location of these events is defined by the location of the {{site.data.keyword.mon_short}} instance that you configure. Notice that when you configure via the UI, the instance must be located in the same region as the one where the bucket is located for regional buckets, or to the location listed for cross-regional buckets in the table above.

## Viewing metrics
{: #mm-cos-monitoring-view-metrics}

If you have configured your bucket to collect platform metrics, you can monitor your bucket by using [{{site.data.keyword.mon_full_notm}}](/docs/monitoring?topic=monitoring-getting-started).

- You must identify the location where metrics are collected and available for monitoring for a bucket.

  To identify the location, check the bucket UI configuration details for monitoring. {: tip}

- You must access the web UI of the {{site.data.keyword.mon_short}} instance in that location. For more information, see [Launching the web UI through the IBM Cloud UI](/docs/monitoring?topic=monitoring-launch).

You can only provision 1 instance of the {{site.data.keyword.mon_short}} service per location to collect platform metrics. Therefore, you must launch the UI of the {{site.data.keyword.mon_short}} instance that is configured to collect platform metrics in the location that you need. 

Complete the following steps to monitor metrics collected for a bucket:

1. [Start the Monitoring UI](/docs/monitoring?topic=monitoring-launch).
2. Select **DASHBOARDS**.
3. In the **Default Dashboards** section, expand **IBM**.
4. Choose a dashboard from the list. Available dashboards are **IBM COS Account Summary** and **IBM COS Buckets**. For more information about predefined dashboards, see [Predefined dashboards](#mm-cos-monitoring-dashboards-dictionary).

Next, change the scope or make a copy of the Default dashboard so that you can monitor your account in {{site.data.keyword.registryshort_notm}}. For more information, see [Working with dashboards](/docs/monitoring?topic=monitoring-dashboards).

## Usage metrics
{: #mm-cos-monitoring-metrics-usage}

All metrics are reported as `float64` numeric values.
{: note}

Usage metrics track the bucket's data usage:

- `ibm_cos_bucket_used_bytes`
- `ibm_cos_bucket_object_count`
- `ibm_cos_bucket_hard_quota_bytes`

## Request metrics
{: #mm-cos-monitoring-metrics-request}

All metrics are reported as `float64` numeric values.
{: note}

### HTTP metrics
{: #mm-cos-monitoring-metrics-http}

The following metrics report the aggregation for different classes of HTTP requests:

- `ibm_cos_bucket_all_requests`
- `ibm_cos_bucket_get_requests`
- `ibm_cos_bucket_put_requests`
- `ibm_cos_bucket_delete_requests`
- `ibm_cos_bucket_post_requests`
- `ibm_cos_bucket_list_requests`
- `ibm_cos_bucket_head_requests`

### Error related metrics
{: #mm-cos-monitoring-metrics-error}

Errors are also collected, with server-side (5xx) errors broken out:

- `ibm_cos_bucket_4xx_errors`
- `ibm_cos_bucket_5xx_errors`

### Upload metrics
{: #mm-cos-monitoring-metrics-upload}

The minimum, maximum, and average bytes transferred by network type are reported:

- `ibm_cos_bucket_bytes_upload_public_min`
- `ibm_cos_bucket_bytes_upload_public_max`
- `ibm_cos_bucket_bytes_upload_public_avg`
- `ibm_cos_bucket_bytes_upload_private_min`
- `ibm_cos_bucket_bytes_upload_private_max`
- `ibm_cos_bucket_bytes_upload_private_avg`
- `ibm_cos_bucket_bytes_upload_direct_min`
- `ibm_cos_bucket_bytes_upload_direct_max`
- `ibm_cos_bucket_bytes_upload_direct_avg`

### Download metrics
{: #mm-cos-monitoring-metrics-download}

The minimum, maximum, and average bytes transferred by network type are reported:

- `ibm_cos_bucket_bytes_download_public_min`
- `ibm_cos_bucket_bytes_download_public_max`
- `ibm_cos_bucket_bytes_download_public_avg`
- `ibm_cos_bucket_bytes_download_private_min`
- `ibm_cos_bucket_bytes_download_private_max`
- `ibm_cos_bucket_bytes_download_private_avg`
- `ibm_cos_bucket_bytes_download_direct_min`
- `ibm_cos_bucket_bytes_download_direct_max`
- `ibm_cos_bucket_bytes_download_direct_avg`

## Latency metrics
{: #mm-cos-monitoring-metrics-latency}

Latency metrics (first byte and general) for requests are broken down by request type:

- `ibm_cos_bucket_first_byte_latency_read_min`
- `ibm_cos_bucket_first_byte_latency_read_max`
- `ibm_cos_bucket_first_byte_latency_read_avg`
- `ibm_cos_bucket_first_byte_latency_write_min`
- `ibm_cos_bucket_first_byte_latency_write_max`
- `ibm_cos_bucket_first_byte_latency_write_avg`
- `ibm_cos_bucket_first_byte_latency_misc_min`
- `ibm_cos_bucket_first_byte_latency_misc_max`
- `ibm_cos_bucket_first_byte_latency_misc_avg`
- `ibm_cos_bucket_request_latency_read_min`
- `ibm_cos_bucket_request_latency_read_max`
- `ibm_cos_bucket_request_latency_read_avg`
- `ibm_cos_bucket_request_latency_write_min`
- `ibm_cos_bucket_request_latency_write_max`
- `ibm_cos_bucket_request_latency_write_avg`
- `ibm_cos_bucket_request_latency_misc_min`
- `ibm_cos_bucket_request_latency_misc_max`
- `ibm_cos_bucket_request_latency_misc_avg`

## Attributes for Segmentation
{: ç-attributes}

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
{: mm-cos-monitoring-additional-attributes}

The following attributes are available for segmenting one or more attributes as described in the reference above.  Please see the individual metrics for segmentation options.

| Attribute | Attribute Name | Attribute Description |
|-----------|----------------|-----------------------|
| `IBM COS Bucket storage class` | `ibm_cos_bucket_storage_class` | Storage class of the bucket |
| `Service instance` | `ibm_service_instance` | The service instance segment identifies the guide of the instance the metric is associated with. |
{: caption="Table 5: COS specific attributes" caption-side="top"}

## Predefined dashboards
{: #mm-cos-monitoring-dashboards-dictionary}

The following table outlines the predefined monitoring dashboards that you can use to monitor metrics that are generated by the {{site.data.keyword.cos_full}} (COS) service.

| Dashboard name | Description |
|----------------|-------------|
| `IBM COS Account Summary` | Displays usage metrics for your {{site.data.keyword.cos_short}} account aggregated by location and storage class. |
| `IBM COS Bucket`          | Displays request metrics for bucket(s). |
{: caption="Table 7. Predefined dashboards" caption-side="bottom"}

The predefined dashboards can't be changed. You can copy any predefined dashboard so that you can change it to suit your requirements. For more information, see [Working with dashboards](/docs/monitoring?topic=monitoring-dashboards).
{: important}

When you start your dashboard, some metrics might display a `Data Load Error` warning icon. This warning is because more time is required to create the data. When data is available, the warning sign goes away, and the metric is populated. You might also need to change the resolution period.
{: note}

## Next Steps
{: #mm-cos-monitoring-next-steps}

You will want to manage the data the {{site.data.keyword.mon_short}} instance is collecting for you. From management to setting alerts, you can [get started](/docs/Monitoring-with-Sysdig?topic=Monitoring-with-Sysdig-getting-started) monitoring your data quickly and efficiently.
