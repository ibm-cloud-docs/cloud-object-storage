---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: activity tracker, event logging, observability

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


# {{site.data.keyword.cloudaccesstrailshort}} events
{: #at-events}

Use the {{site.data.keyword.cloudaccesstrailfull}} service to track how users and applications interact with {{site.data.keyword.cos_full}}.
{: shortdesc}

The {{site.data.keyword.cloudaccesstrailfull_notm}} service records user-initiated activities that change the state of a service in {{site.data.keyword.Bluemix_notm}}. For more information, see [Getting started with {{site.data.keyword.cloudaccesstrailshort}}](/docs/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started).



## List of events
{: #at-events-list}

The following table lists the actions that generate an event:

<table>
  <caption>Actions that generate events</caption>
  <tr>
    <th>Actions</th>
	  <th>Description</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>An event is generated when a user requests bucket metadata and whether IBM Key Protect is enabled on the bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>An event is generated when a user creates a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>An event is generated when a user requests the list of objects in a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>An event is generated when a user updates a bucket, for example, when a user renames a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>An event is generated when a user deletes a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>An event is generated when a user sets the access control list on a bucket to `public-read` or `private`.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>An event is generated when a user reads the access control list on a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>An event is generated when a user creates a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>An event is generated when a user requests if cross-origin resource sharing configuration is enabled on a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>An event is generated when a user modifies a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>An event is generated when a user deletes a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
</table>



## Where to view the events
{: #at-ui}

{{site.data.keyword.cloudaccesstrailshort}} events are available in the {{site.data.keyword.cloudaccesstrailshort}} **account domain**.

Events are sent to the {{site.data.keyword.cloudaccesstrailshort}} region closest to the {{site.data.keyword.cos_full_notm}} bucket location that is shown on the [services supported page](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability).
