---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}


# Acerca de {{site.data.keyword.cos_full_notm}}
{: #about-ibm-cloud-object-storage}

La información que se almacena con {{site.data.keyword.cos_full}} se cifra y se distribuye entre varias ubicaciones geográficas, y se accede a la misma a través de HTTP mediante una API REST. Este servicio utiliza las tecnologías de almacenamiento distribuido que proporciona el sistema {{site.data.keyword.cos_full_notm}} (antes llamado Cleversafe).

{{site.data.keyword.cos_full_notm}} está disponible con tres tipos de resiliencia: varias regiones, regional y un solo centro de datos. El tipo de varias regiones ofrece una mayor durabilidad y disponibilidad que si se utiliza una sola región, aunque con una latencia ligeramente superior, y actualmente está disponible en EE. UU., UE y AP. El servicio regional invierte estas prestaciones y distribuye los objetos entre varias zonas de disponibilidad dentro de una sola región; está disponible en EE. UU., UE y AP. Si una determinada región o zona de disponibilidad no está disponible, el almacén de objetos continúa funcionando sin impedimento. El servicio de un solo centro de datos distribuye los objetos entre varias máquinas dentro de la misma ubicación física. Consulte [aquí](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) para ver las regiones disponibles.

Los desarrolladores utilizan una API de {{site.data.keyword.cos_full_notm}} para interactuar con el almacén de objetos. Esta documentación proporciona soporte para [empezar](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) a trabajar en el suministro de cuentas, crear grupos, cargar objetos y utilizar una referencia de interacciones comunes de API.

## Otros servicios de almacenamiento de objetos de IBM
{: #about-other-cos}
Además de {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} actualmente proporciona varias ofertas de almacenamiento de objetos adicionales para distintas necesidades de los usuarios, todas ellas accesibles a través de portales web y API REST. [Más información.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
