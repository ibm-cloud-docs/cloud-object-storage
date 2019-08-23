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


# Sucesos de {{site.data.keyword.cloudaccesstrailshort}}
{: #at-events}

Utilice el servicio {{site.data.keyword.cloudaccesstrailfull}} para realizar el seguimiento de cómo interactúan los usuarios y las aplicaciones con {{site.data.keyword.cos_full}}.
{: shortdesc}

El servicio {{site.data.keyword.cloudaccesstrailfull_notm}} registra las actividades iniciadas por el usuario que cambian el estado de un servicio en {{site.data.keyword.Bluemix_notm}}. Para obtener más información, consulte [Guía de iniciación a {{site.data.keyword.cloudaccesstrailshort}}](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started).



## Lista de sucesos
{: #at-events-list}

La tabla siguiente contiene las acciones que generan un suceso:

<table>
  <caption>Acciones que generan sucesos</caption>
  <tr>
    <th>Acciones</th>
	  <th>Descripción</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>Se genera un suceso cuando un usuario solicita metadatos de grupo y si IBM Key Protect está habilitado en el grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>Se genera un suceso cuando un usuario crea un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>Se genera un suceso cuando un usuario solicita la lista de objetos de un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>Se genera un suceso cuando un usuario actualiza un grupo, por ejemplo cuando un usuario cambia el nombre de un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>Se genera un suceso cuando un usuario suprime un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>Se genera un suceso cuando un usuario establece la lista de control de accesos de un grupo en `public-read` o `private`.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>Se genera un suceso cuando un usuario lee la lista de control de accesos de un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>Se genera un suceso cuando un usuario crea una configuración de compartición de recursos entre orígenes para un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>Se genera un suceso cuando un usuario solicita si la configuración de compartición de recursos entre orígenes está habilitada en un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>Se genera un suceso cuando un usuario modifica una configuración de compartición de recursos entre orígenes para un grupo.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>Se genera un suceso cuando un usuario suprime una configuración de compartición de recursos entre orígenes para un grupo.</td>
  </tr>
</table>



## Dónde ver los sucesos
{: #at-ui}

Los sucesos de {{site.data.keyword.cloudaccesstrailshort}} están disponibles en el {{site.data.keyword.cloudaccesstrailshort}}**dominio de cuenta**.

Los sucesos se envían a la región de {{site.data.keyword.cloudaccesstrailshort}} más cercana a la ubicación del grupo de {{site.data.keyword.cos_full_notm}} que se muestra en la [página de servicios admitidos](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability).
