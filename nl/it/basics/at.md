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


# Eventi di {{site.data.keyword.cloudaccesstrailshort}}
{: #at-events}

Utilizza il servizio {{site.data.keyword.cloudaccesstrailfull}} per tenere traccia di come gli utenti e le applicazioni interagiscono con {{site.data.keyword.cos_full}}.
{: shortdesc}

Il servizio {{site.data.keyword.cloudaccesstrailfull_notm}} registra le attività avviate dall'utente che modificano lo stato di un servizio in {{site.data.keyword.Bluemix_notm}}. Per ulteriori informazioni, consulta [Introduzione a {{site.data.keyword.cloudaccesstrailshort}}](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started).



## Elenco di eventi
{: #at-events-list}

La seguente tabella elenca le azioni che generano un evento:

<table>
  <caption>Azioni che generano gli eventi</caption>
  <tr>
    <th>Azioni</th>
	  <th>Descrizione</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>Un evento viene generato quando un utente richiede i metadati del bucket e se IBM Key Protect è abilitato nel bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>Un evento viene generato quando un utente crea un bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>Un evento viene generato quando un utente richiede l'elenco degli oggetti in un bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>Un evento viene generato quando un utente aggiorna un bucket, ad esempio quando un utente ridenomina un bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>Un evento viene generato quando un utente elimina un bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>Un evento viene generato quando un utente imposta l'ACL (access control list) in un bucket su `public-read` o `private`.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>Un evento viene generato quando un utente legge l'ACL (access control list) in un bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>Un evento viene generato quando un utente crea una configurazione CORS (Cross-Origin Resource Sharing) per un bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>Un evento viene generato quando un utente richiede se la configurazione CORS (Cross-Origin Resource Sharing) è abilitata in un bucket.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>Un evento viene generato quando un utente modifica una configurazione CORS (Cross-Origin Resource Sharing) per un bucket. </td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>Un evento viene generato quando un utente elimina una configurazione CORS (Cross-Origin Resource Sharing) per un bucket. </td>
  </tr>
</table>



## Dove visualizzare gli eventi
{: #at-ui}

Gli eventi {{site.data.keyword.cloudaccesstrailshort}} sono disponibili nel **dominio dell'account** {{site.data.keyword.cloudaccesstrailshort}}.

Gli eventi vengono inviati alla regione {{site.data.keyword.cloudaccesstrailshort}} più vicina all'ubicazione del bucket {{site.data.keyword.cos_full_notm}} mostrata nella [pagina supportata dei servizi](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability).
