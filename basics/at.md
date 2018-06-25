---

copyright:
  years: 2016, 2018
lastupdated: "2018-06-26"

---

{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:download: .download}
{:table: .aria-labeledby="caption"}


# {{site.data.keyword.cloudaccesstrailshort}} events
{: #at_events}

Use the {{site.data.keyword.cloudaccesstrailfull}} service to track how users and applications interact with {{site.data.keyword.cos_full}}.
{: shortdesc}

The {{site.data.keyword.cloudaccesstrailfull_notm}} service records user-initiated activities that change the state of a service in {{site.data.keyword.Bluemix_notm}}. For more information, see [Getting started with {{site.data.keyword.cloudaccesstrailshort}}](/docs/services/cloud-activity-tracker/index.html#getting-started-with-cla).



## List of events
{: #events}

The following table lists the actions that generate an event:

<table>
  <caption>Actions that generate events</caption>
  <tr>
    <th>Actions</th>
	  <th>Description</th>
  <tr>
  <tr>
    <td>cloud-object-storage.bucket.info</td>
	  <td>An event is generated when a user gets details about a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket.create</td>
	  <td>An event is generated when a user creates a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket.update</td>
	  <td>An event is generated when a user updates a bucket, for example, when a user renames a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket.delete</td>
	  <td>An event is generated when a user deletes a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-acl.create</td>
	  <td>An event is generated when a user assigns one or more access roles to a user or to a serviceID to work with a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-acl.update</td>
	  <td>An event is generated when a user modifies one or more access roles that a user or a serviceID has to work with a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-cors.create</td>
	  <td>An event is generated when a user creates a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-cors.update</td>
	  <td>An event is generated when a user modifies a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-cors.delete</td>
	  <td>An event is generated when a user deletes a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
</table>



## Where to view the events
{: #ui}

{{site.data.keyword.cloudaccesstrailshort}} events are available in the {{site.data.keyword.cloudaccesstrailshort}} **account domain** that is available in the {{site.data.keyword.Bluemix_notm}} region where the events are generated.





