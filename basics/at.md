---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-07-23"

keywords: IBM, activity tracker, LogDNA, event, object storage, COS API calls, monitor COS events

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:table: .aria-labeledby="caption"}


# Activity Tracker events
{: #at-events}

Use the {{site.data.keyword.cloudaccesstrailfull}} service to track how users and applications interact with {{site.data.keyword.cos_full}}.
{: shortdesc}

The {{site.data.keyword.at_full}} service records user-initiated activities that change the state of a service in {{site.data.keyword.Bluemix_notm}}. 
For more information, see [{{site.data.keyword.at_full}}](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started).  

You can use this service to investigate abnormal activity and critical actions and to comply with regulatory audit requirements.  In addition, you can be alerted about actions as they happen. The events that are collected comply with the Cloud Auditing Data Federation (CADF) standard.

## List of events
{: #at-actions}

The following table lists the {{site.data.keyword.cos_short}} actions that generate an event:

### Global Events

| Action                   | Description                 |
| ------------------------ | --------------------------- |
| `cloud-object-storage.bucket.list`     | List the buckets in the service instance |
| `cloud-object-storage.bucket.create`   | Create a bucket in the service instance |
| `cloud-object-storage.bucket.delete`   | Delete a bucket in the service instance |
<!-- {: caption="Table 1. {{site.data.keyword.cos_short}} actions that generate Global Events to Activity Tracker in Frankfurt" caption-side="top"} -->

### Management Events

| Action                   | Description                 |
| ------------------------ | --------------------------- |
| Resource Configuration events | |
| `cloud-object-storage.resource-configuration.read`     | Read the resource configuration for the bucket |
| `cloud-object-storage.resource-configuration.update`   | Update the resource configuration for the bucket |
| Bucket events | |
| `cloud-object-storage.bucket-cors.info`     | Validate the CORS configuration |
| `cloud-object-storage.bucket-cors.read`     | Get the CORS configuration |
| `cloud-object-storage.bucket-cors.create`     | Create the CORS configuration |
| `cloud-object-storage.bucket-cors.delete`   | Delete the CORS configuration |
| `cloud-object-storage.bucket-lifecycle.read`   | Get the bucket lifecycle configuration |
| `cloud-object-storage.bucket-lifecycle.create`   | Create the bucket lifecycle configuration |
| `cloud-object-storage.bucket-lifecycle.delete`   | Delete the bucket lifecycle configuration |
| `cloud-object-storage.bucket-acl.read`   | Get the bucket ACL |
| `cloud-object-storage.bucket-acl.create`   | Create the bucket ACL |
| `cloud-object-storage.bucket-crn.read`   | Get the bucket CRN |
| `cloud-object-storage.bucket-location.read`   | Get the bucket location |
| `cloud-object-storage.bucket-retention.read`  | Get the bucket retention |
| `cloud-object-storage.bucket-retention.create`   | Create the bucket retention |
| Object events | |
| `cloud-object-storage.object-acl.read`   | Get the object ACL |
| `cloud-object-storage.object-acl.create` | Create the object ACL |
| `cloud-object-storage.object-retention-legal-hold.list`  | List the legal holds on the object |
| `cloud-object-storage.object-retention-legal-hold.create`  | Create the object legal hold |
| `cloud-object-storage.object-retention.update`  | Extend the retention time |
| `cloud-object-storage.object-expire.info`  | Get when the object will expire |


<!-- object-acl etc. -->

<!-- {: caption="Table 2. {{site.data.keyword.cos_short}} actions that generate Management Events to the Activity Tracker" caption-side="top"} -->

### Data Events

| Action                   | Description                 |
| ------------------------ | --------------------------- |
| Bucket access events | |
| `cloud-object-storage.object.list`   | List the objects in the bucket |
| `cloud-object-storage.bucket.info`   | Get the metadata for the bucket |
| Object access events | |
| `cloud-object-storage.object.info`   | Get the metadata for the object |
| `cloud-object-storage.object.read`   | Read the object |
| `cloud-object-storage.object.write`  | Write the object |
| `cloud-object-storage.object.delete`  | Delete the object |
| `cloud-object-storage.objects.delete`  | Delete multiple objects |
| `cloud-object-storage.object-batch.delete`  | Delete an object in a batch |
| `cloud-object-storage.object-copy.read`  | Read the source object to copy |
| `cloud-object-storage.object-copy.write`  | Write the target object to copy |
| `cloud-object-storage.object-restore.read`   | Read the source object to restore|
| `cloud-object-storage.object-restore.write`  | Write the target object to restore |
| Multipart events | |
| `cloud-object-storage.bucket-multipart.list` | List multipart uploads of objects in a bucket |
| `cloud-object-storage.object-multipart.list` | List parts of an object |
| `cloud-object-storage.object-multipart.start` | Initiate a multipart upload of an object |
| `cloud-object-storage.object-multipart.create` | Create a part of a multipart upload of an object |
| `cloud-object-storage.object-multipart.complete` | Complete a multipart upload of an object |
| `cloud-object-storage.object-multipart.delete` | Abort an imcomplete multipart upload of an object |


Restore an object from archive
* RequestData.requestId ties together the two events that are generated for restoring an object

Copy an object from one bucket to another
* RequestData.requestId ties together the two events that are generated for copying an object


<!-- {: caption="Table 3. {{site.data.keyword.cos_short}} actions that generate Data Events to the Activity Tracker" caption-side="top"} -->


## Viewing events
{: #at-ui}

You can view the Activity Tracker events that are associated with your {{site.data.keyword.cos_short}} service instance by using [{{site.data.keyword.at_full_notm}}](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started)

### Using {{site.data.keyword.at_full_notm}}
{: #at-ui-logdna}

{{site.data.keyword.cos_short}} Global Events will be forwarded to the {{site.data.keyword.at_full_notm}} service instance in Frankfurt.

{{site.data.keyword.cos_short}} Management and Data Events will be forwarded to the {{site.data.keyword.at_full_notm}} service instance that has been associated with the bucket.

{{site.data.keyword.at_full_notm}} can have only one instance per location. To view events, you must access the web UI of the {{site.data.keyword.at_full_notm}} service instance in the location associated with the bucket. For more information, see [Launching the web UI through the IBM Cloud UI](/docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-launch#launch_step2).

## Analyzing events
{: #at-events-analyze}

The requestData field includes extra information:
* requestData.serviceInstanceId is set to the service instance id to know which service instance the event is generated from
* requestData.bucketLocation is set to the location of the bucket
* requestData.requestId is the unique id of the request
