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
	  <td>An event is generated when a user requests bucket metadata and whether IBM Key Protect is enabled on the bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket.create</td>
	  <td>An event is generated when a user creates a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket.read</td>
	  <td>An event is generated when a user requests the list of objects in a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket.delete</td>
	  <td>An event is generated when a user deletes a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-acl.create</td>
	  <td>An event is generated when a user sets the access control list on a bucket which can be public-read or private.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-acl.read</td>
	  <td>An event is generated when a user reads the access control list on a bucket which can be public-read or private.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-cors.create</td>
	  <td>An event is generated when a user creates a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-cors.read</td>
	  <td>An event is generated when a user requests if cross-origin resource sharing configuration is enabled on a bucket.</td>
  </tr>
  <tr>
    <td>cloud-object-storage.bucket-cors.delete</td>
	  <td>An event is generated when a user deletes a cross-origin resource sharing configuration for a bucket.</td>
  </tr>
</table>



## Where to view the events
{: #ui}

{{site.data.keyword.cloudaccesstrailshort}} events are available in the {{site.data.keyword.cloudaccesstrailshort}} **account domain**.

The {{site.data.keyword.cloudaccesstrailshort}} event will be sent to the Activity Tracker instance closest to the bucket location.

The following table lists where to view the event depending on COS bucket location:

<table>
  <tr>
    <th>COS bucket location</th>
	  <th>Activity Tracker region</th>
  <tr>
  <tr>
    <td>us-geo</td>
	  <td>US South</td>
  </tr>
  <tr>
    <td>us-south</td>
    <td>US South</td>
  </tr>
  <tr>
    <td>us-east</td>
    <td>US South</td>
  </tr>
  <tr>
    <td>Toronto (tor01)</td>
    <td>US South</td>
  </tr>
  <tr>
    <td>eu-geo</td>
    <td>Germany</td>
  </tr>
  <tr>
    <td>eu-de</td>
    <td>Germany</td>
  </tr>
  <tr>
    <td>Amsterdam (ams03)</td>
    <td>Germany</td>
  </tr>
  <tr>
    <td>eu-gb</td>
    <td>United Kingdom</td>
  </tr>
  <tr>
    <td>ap-geo</td>
    <td>Sydney</td>
  </tr>
  <tr>
    <td>Melbourne (mel01)</td>
    <td>Sydney</td>
  </tr>
  <tr>
    <td>Chennai (che01)</td>
    <td>Sydney</td>
  </tr>
</table>
