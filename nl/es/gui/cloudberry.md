---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup es un programa de utilidad flexible que permite a los usuarios realizar copias de seguridad de algunos o de todos los sistemas de archivos locales en un sistema de almacenamiento de objetos compatible con la API de S3. Dispone de la versión gratuita y de la versión Profesional para Windows, MacOS y Linux y se da soporte a varios servicios de almacenamiento en la nube, incluido {{site.data.keyword.cos_full}}. Cloudberry Backup se puede descargar desde [cloudberrylab.com](https://www.cloudberrylab.com/).

Cloudberry Backup incluye varias prestaciones muy útiles, que incluyen:

* Planificación
* Copias de seguridad incrementales y a nivel de bloque
* Interfaz de línea de mandatos
* Notificaciones por correo electrónico
* Compresión (*solo versión Pro*)

## Cloudberry Explorer
{: #cloudberry-explorer}

Un nuevo producto de Cloudberry Labs ofrece una interfaz de usuario de gestión de archivos con la que está familiarizado para {{site.data.keyword.cos_short}}. [Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} también se ofrece en las versiones gratuita y Pro, pero actualmente solo está disponible para Windows. Sus principales características incluyen las siguientes:

* Sincronización de carpetas y grupos
* Interfaz de línea de mandatos
* Gestión de ACL
* Informes de capacidad

La versión Pro también incluye:
* Búsqueda 
* Cifrado/Compresión
* Carga que se puede reanudar
* Soporte de FTP/SFTP

## Utilización de Cloudberry con Object Storage
{: #cloudberry-cos}

Principales puntos que debe tener en cuenta cuando configure productos Cloudberry de modo que funcionen con {{site.data.keyword.cos_short}}:

* Seleccione `Compatible con S3` en la lista de opciones
* Actualmente solo se da soporte a las [credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)
* Se necesita una conexión por separado para cada grupo
* Asegúrese de que el `Punto final` especificado en la conexión coincida con la región del grupo seleccionado (*la copia de seguridad fallará si se selecciona un destino inaccesible*). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
