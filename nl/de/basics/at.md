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


# {{site.data.keyword.cloudaccesstrailshort}}-Ereignisse
{: #at-events}

Verwenden Sie den Service {{site.data.keyword.cloudaccesstrailfull}}, um zu überwachen, wie Benutzer und Anwendungen mit {{site.data.keyword.cos_full}} interagieren.
{: shortdesc}

Der Service {{site.data.keyword.cloudaccesstrailfull_notm}} zeichnet die vom Benutzer eingeleiteten Aktivitäten auf, durch die der Status eines Service in {{site.data.keyword.Bluemix_notm}} geändert wird. Weitere Informationen hierzu finden Sie in [Einführung in {{site.data.keyword.cloudaccesstrailshort}}](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started).



## Liste der Ereignisse
{: #at-events-list}

In der folgenden Tabelle werden die Aktionen aufgelistet, die ein Ereignis generieren:

<table>
  <caption>Aktionen, die Ereignisse generieren</caption>
  <tr>
    <th>Aktionen</th>
	  <th>Beschreibung</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer Bucketmetadaten anfordert. Ob dieser Vorgang ausgeführt wird, hängt davon ab, ob IBM Key Protect für das Bucket aktiviert wurde.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer ein Bucket erstellt.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer die Liste mit Objekten in einem Bucket anfordert.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer ein Bucket aktualisiert und es dabei z. B. umbenennt.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer ein Bucket löscht.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer die Zugriffssteuerungsliste für ein Bucket auf `public-read` oder `private` setzt.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer die Zugriffssteuerungsliste für ein Bucket liest.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer eine CORS-Konfiguration für ein Bucket erstellt.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer anfordert, dass eine CORS-Konfiguration für ein Bucket aktiviert wird.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer eine CORS-Konfiguration für ein Bucket ändert.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>Ein Ereignis wird generiert, wenn ein Benutzer eine CORS-Konfiguration für ein Bucket löscht.</td>
  </tr>
</table>



## Wo werden Ereignisse angezeigt?
{: #at-ui}

{{site.data.keyword.cloudaccesstrailshort}}-Ereignisse stehen in der **Kontodomäne** von {{site.data.keyword.cloudaccesstrailshort}} zur Verfügung.

Ereignisse werden an die {{site.data.keyword.cloudaccesstrailshort}}-Region gesendet, die dem {{site.data.keyword.cos_full_notm}}-Bucketstandort, der auf der [Seite für die unterstützten Services](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability) angezeigt wird, am nächsten ist.
