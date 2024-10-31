---

copyright:
  years: 2017, 2024
lastupdated: "2024-06-11"

keywords: activity tracker, LogDNA, event, object storage, COS API calls, monitor COS events

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Activity Tracker events
{: #at-events}

Use the {{site.data.keyword.at_full}} service to track how users and applications interact with {{site.data.keyword.cos_full_notm}} (COS).
{: shortdesc}

This feature is not currently supported in {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}. [Learn more.](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite)
{: note}

The {{site.data.keyword.at_full_notm}} service records user-initiated activities that change the state of a service in {{site.data.keyword.cloud_notm}}.
For more information, see [{{site.data.keyword.at_full_notm}}](/docs/activity-tracker?topic=activity-tracker-getting-started).

By default, COS events that report on global actions such as creation of a bucket are collected automatically. You can monitor global actions through the Activity Tracker instance that is located in the Frankfurt location.

In {{site.data.keyword.cos_full_notm}}, you can also monitor management events and COS data events.

* Collection of these events in your account is optional.
* You must configure each bucket to enable management events, or data events, or both.
* Each action that a user performs on a COS resource has a unique ID that is included in the event in the `responseData.requestId` field.

You can use this service to investigate abnormal activity and critical actions, and to comply with regulatory audit requirements. In addition, you can be alerted about actions as they happen. The events that are collected comply with the Cloud Auditing Data Federation (CADF) standard.

For guidance on how to use {{site.data.keyword.at_full}} with {{site.data.keyword.cos_short}} see [Tracking events on your {{site.data.keyword.cos_full_notm}} buckets](/docs/cloud-object-storage?topic=cloud-object-storage-at) .  Below we list all of the events that are available.

## Management events
{: #at-actions-mngt}

Management events are classified in the following categories:

* Global events
* Resource configuration events
* Bucket events
* Object events

### Global events
{: #at-actions-global}

The following table lists the COS actions that generate a global event. You can monitor this events through the Activity Tracker instance that is available in the Frankfurt location.

| Action                               | Description                              |
| ------------------------------------ | ---------------------------------------- |
| `cloud-object-storage.instance.list` | List the buckets in the service instance |
| `cloud-object-storage.bucket.create` | Create a bucket in the service instance  |
| `cloud-object-storage.bucket.delete` | Delete a bucket in the service instance  |
{: caption="{{site.data.keyword.cos_short}} actions that generate global events"}

### Resource configuration events
{: #at-actions-mngt-1}

The following table lists the COS resource configuration events:

| Action                                               | Description                                      |
| ---------------------------------------------------- | ------------------------------------------------ |
| `cloud-object-storage.resource-configuration.read`   | Read the resource configuration for the bucket   |
| `cloud-object-storage.resource-configuration.update` | Update the resource configuration for the bucket |
{: caption="Resource Configuration events"}

### Bucket events
{: #at-actions-mngt-2}

The following table lists the COS bucket events:

| Action                                                   | Description                                |
| -------------------------------------------------------- | ------------------------------------------ |
| `cloud-object-storage.bucket-cors.read`                  | Get the CORS configuration                 |
| `cloud-object-storage.bucket-cors.create`                | Create the CORS configuration              |
| `cloud-object-storage.bucket-cors.delete`                | Delete the CORS configuration              |
| `cloud-object-storage.bucket-lifecycle.read`             | Get the bucket lifecycle configuration     |
| `cloud-object-storage.bucket-lifecycle.create`           | Create the bucket lifecycle configuration  |
| `cloud-object-storage.bucket-lifecycle.delete`           | Delete the bucket lifecycle configuration  |
| `cloud-object-storage.bucket-acl.read`                   | Get the bucket [ACL](#x2012793){: term}    |
| `cloud-object-storage.bucket-acl.create`                 | Create the bucket [ACL](#x2012793){: term} |
| `cloud-object-storage.bucket-crn.read`                   | Get the bucket CRN                         |
| `cloud-object-storage.bucket-location.read`              | Get the bucket location                    |
| `cloud-object-storage.bucket-retention.read`             | Get the bucket retention                   |
| `cloud-object-storage.bucket-retention.create`           | Create the bucket retention                |
| `cloud-object-storage.bucket-key-state.update`           | Updating a Key Protect root encryption key |
| `cloud-object-storage.bucket-public-access-block.create` | Add a public ACL block configuration       |
| `cloud-object-storage.bucket-public-access-block.read`   | Read a public ACL block configuration      |
| `cloud-object-storage.bucket-public-access-block.delete` | Delete a public ACL block configuration    |
{: caption="Bucket events"}

For `cloud-object-storage.bucket-key-state.update` events, the following fields include extra information:

| Field                            | Description                                                               |
| -------------------------------- | ------------------------------------------------------------------------- |
| `requestData.eventType`          | The type of lifecycle event that occurred, such as deletion, rotation, and so on |
| `requestData.requestedKeyState`  | The the requested state of the key (enabled or disabled).                 |
| `requestData.requestKeyVersion`  | The requested version of the key.                                         |
| `requestData.bucketLocation`     | The location of the bucket that uses the key.                             |
| `responseData.eventID`           | The unique identifier associated with the key lifecycle event.            |
| `responseData.adopterKeyState`   | The current state the key (enabled or disabled).                          |
| `responseData.adopterKeyVersion` | The current version of the key.                                           |
{: caption="Table 3a. Additional fields for bucket-key-state.update events"}

### Object events
{: #at-actions-mngt-3}

The following table lists the COS object events:

| Action                                                    | Description                        |
| --------------------------------------------------------- | ---------------------------------- |
| `cloud-object-storage.object-cors.read`                   | Get the CORS configuration         |
| `cloud-object-storage.object-acl.read`                    | Get the object ACL                 |
| `cloud-object-storage.object-acl.create`                  | Create the object ACL              |
| `cloud-object-storage.object-retention-legal-hold.list`   | List the legal holds on the object |
| `cloud-object-storage.object-retention-legal-hold.update` | Add or remove object legal hold    |
| `cloud-object-storage.object-retention.update`            | Extend the retention time          |
| `cloud-object-storage.object-expire.read`                 | Get when the object will expire    |
{: caption="Object events"}

## Data Events
{: #at-actions-data}

Data events are classified in the following categories:
* Bucket access events
* Object access events
* Multipart events
* Bucket versioning events

### Bucket access events
{: #at-actions-data-1}

The following table lists the COS bucket access events:

| Action                                      | Description                     |
| ------------------------------------------- | ------------------------------- |
| `cloud-object-storage.bucket.list`          | List the objects in the bucket  |
| `cloud-object-storage.bucket-metadata.read` | Get the metadata for the bucket |
{: caption="Bucket access events"}

### Object access events
{: #at-actions-data-2}

The following table lists the COS object access events:

| Action                                       | Description                               |
| -------------------------------------------- | ----------------------------------------- |
| `cloud-object-storage.object-metadata.read`  | Get the metadata for the object           |
| `cloud-object-storage.object.read`           | Read the object                           |
| `cloud-object-storage.object.create`         | Create the object                         |
| `cloud-object-storage.object.delete`         | Delete the object                         |
| `cloud-object-storage.objects.delete`        | Delete multiple objects                   |
| `cloud-object-storage.object-batch.delete`   | Delete an object in a batch               |
| `cloud-object-storage.object-copy.read`      | Read the source object to copy            |
| `cloud-object-storage.object-copy.create`    | Create the target object from the copy    |
| `cloud-object-storage.object-restore.read`   | Read the source object to restore         |
| `cloud-object-storage.object-restore.create` | Create the target object from the restore |
{: caption="Object access events"}

If versioning is enabled for a bucket, then `target.versionId` will be present for operations that make use of object versions.

For `cloud-object-storage.object.delete` and `cloud-object-storage.object-batch.delete` events, the following fields include extra information:

| Field                               | Description                                                      |
| ----------------------------------- | ---------------------------------------------------------------- |
| `responseData.deleteMarker.created` | The object has been versioned and replaced with a delete marker. |
{: caption="Table 6a. Additional fields for deletion events"}

### Multipart events
{: #at-actions-data-3}

The following table lists the COS multipart events:

| Action                                           | Description                                       |
| ------------------------------------------------ | ------------------------------------------------- |
| `cloud-object-storage.bucket-multipart.list`     | List multipart uploads of objects in a bucket     |
| `cloud-object-storage.object-multipart.list`     | List parts of an object                           |
| `cloud-object-storage.object-multipart.start`    | Initiate a multipart upload of an object          |
| `cloud-object-storage.object-multipart.create`   | Create a part of a multipart upload of an object  |
| `cloud-object-storage.object-multipart.complete` | Complete a multipart upload of an object          |
| `cloud-object-storage.object-multipart.delete`   | Abort an incomplete multipart upload of an object |
{: caption="Multipart events"}

### Bucket versioning events
{: #at-actions-data-4}

The following table lists the COS versioning events:

| Action                                          | Description                          |
| ----------------------------------------------- | ------------------------------------ |
| `cloud-object-storage.bucket-versioning.create` | Enable versioning on a bucket        |
| `cloud-object-storage.bucket-versioning.read`   | Check versioning status of a bucket  |
| `cloud-object-storage.bucket-versioning.list`   | List versions of objects in a bucket |
{: caption="Versioning events"}

For `cloud-object-storage.bucket-versioning.create` events, the following fields include extra information:

| Field                                   | Description                                                |
| --------------------------------------- | ---------------------------------------------------------- |
| `requestData.newValue.versioning.state` | The versioning state of the bucket (enabled or suspended). |
{: caption="Table 8a. Additional fields for bucket-versioning.create events"}

## Viewing events
{: #at-ui}

You can view the Activity Tracker events that are associated with your {{site.data.keyword.cos_short}} instance by using [{{site.data.keyword.at_full_notm}}](/docs/activity-tracker?topic=activity-tracker-getting-started).

You can only provision 1 instance of the {{site.data.keyword.at_full_notm}} service per location.

To view events, you must identify the location where events are collected and available for monitoring. Then, you must access the web UI of the {{site.data.keyword.at_full_notm}} instance in that location. For more information, see [Launching the web UI through the IBM Cloud UI](/docs/activity-tracker?topic=activity-tracker-observe).

### Management events
{: #at-ui-mngt}

{{site.data.keyword.cos_short}} global events are forwarded to the {{site.data.keyword.at_full_notm}} service instance that is located in Frankfurt.

All other {{site.data.keyword.cos_short}} management events are forwarded to the {{site.data.keyword.at_full_notm}} instance that is associated with the bucket.

To view events, you must access the web UI of the {{site.data.keyword.at_full_notm}} instance in the location that is associated with the bucket.

### Data events
{: #at-ui-data}

{{site.data.keyword.cos_short}} data events are forwarded to the {{site.data.keyword.at_full_notm}} instance that is associated with the bucket.

To view events, you must access the web UI of the {{site.data.keyword.at_full_notm}} instance in the location that is associated with the bucket.

## Analyzing events
{: #at-events-analyze}

### Identifying the COS instance ID that generates the event
{: #at-events-analyze-1}

In the {{site.data.keyword.cloud_notm}}, you can have 1 or more COS instances.

To quickly identify the COS instance ID in your account that has generated an event, check the field `responseData.serviceInstanceId` that is set in the `responseData` field.

### Identifying the bucket location
{: #at-events-analyze-2}

To quickly identify the bucket location, check the field `responseData.bucketLocation` that is set in the `responseData` field.

### Getting the unique ID of a request
{: #at-events-analyze-3}

Each action that a user performs on a COS resource has a unique ID.

To get the unique ID of a request to a COS resource, check the field `responseData.requestId` that is set in the `responseData` field.

### Getting all events for a multipart upload operations
{: #at-events-analyze-4}

When you upload a large object by using *multipart upload operations*, each operation generates an event. In each event, the field `responseData.uploadId` is set to the same value.

To search for all events that are part of a multipart upload operation, you can search for a specific `responseData.uploadId` value.

### Getting all events that are generated for a restore request
{: #at-events-analyze-5}

A request to restore an object from an archive generates multiple events in COS:

1. A read action of the source object. This action generates an event with action **cloud-object-storage.object-restore.read**.
2. A create action of the object into a bucket. This action generates an event with action **cloud-object-storage.object-restore.create**.

You can use the `responseData.requestId` field to identify the events that are generated when you restore an object.

### Getting all events that are generated for copying an object from one bucket to another
{: #at-events-analyze-6}

A request to copy an object from one bucket to a different one generates multiple events in COS:

1. A read action of the source object. This action generates an event with action **cloud-object-storage.object-copy.read**.
2. A create action of the object into the new bucket. This action generates an event with action **cloud-object-storage.object-copy.create**.

To collect and monitor all events that report on a copy action across buckets, consider configuring each bucket to collect and forward events to the same Activity Tracker instance in your account.

* If one bucket is not enabled to collect management and data events, you will not receive the event that reports any copy action on that bucket.
* If you configure different Activity Tracker instances for each bucket, you will have one event in 1 instance and the other event in a different instance.

You can use the `responseData.requestId` field to identify the events that are generated when you copy an object from one bucket to another.

### Getting the details of a firewall update
{: #at-events-analyze-7}

Updating a bucket's firewall will generate a `cloud-object-storage.resource-configuration.update` event.

To get the details of what was changed, check for fields `requestData.allowedIp`, `requestData.deniedIp`, and `requestData.allowedNetworkTypes` that appear in the **`requestData`** field.
