---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# Carga de datos
{: #upload}

Después de organizar los grupos, es hora de añadir algunos objetos. En función de cómo desee utilizar el almacenamiento, existen diferentes formas de obtener datos en el sistema. Un científico de datos tiene unos pocos archivos grandes que se utilizan para realizar análisis, un administrador de sistemas necesita mantener las copias de seguridad de la base de datos sincronizadas con los archivos locales y un desarrollador está escribiendo software que necesita leer y escribir millones de archivos. Cada uno de estos casos de ejemplo requiere un método diferente de ingestión de datos.

## Utilización de la consola
{: #upload-console}

Generalmente, la utilización de la consola basada en web no es la forma más común de utilizar {{site.data.keyword.cos_short}}. Los objetos se limitan a 200 MB y el nombre de archivo y la clave son idénticos. Se pueden cargar varios objetos al mismo tiempo, y, si el navegador permite varias hebras, cada objeto se cargará utilizando varias partes en paralelo. La [transferencia de alta velocidad de Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) proporciona soporte para tamaños de objeto mayores y un rendimiento mejorado (en función de los factores de red).

## Utilización de una herramienta compatible
{: #upload-tool}

Algunos usuarios desean utilizar un programa de utilidad autónomo para interactuar con su almacenamiento. Puesto que la API de Cloud Object Storage da soporte a las operaciones de la API S3 más utilizadas, muchas herramientas compatibles con S3 también se pueden conectar a {{site.data.keyword.cos_short}} utilizando las [credenciales HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).

Algunos ejemplos incluyen exploradores de archivos como [Cyberduck](https://cyberduck.io/) o [Transmit](https://panic.com/transmit/), programas de utilidad de copia de seguridad como [Cloudberry](https://www.cloudberrylab.com/) y [Duplicati](https://www.duplicati.com/), programas de utilidad de línea de mandatos como [s3cmd](https://github.com/s3tools/s3cmd) o [Minio Client](https://github.com/minio/mc) y muchos otros.

## Utilización de la API
{: #upload-api}

La mayoría de aplicaciones programáticas de Object Storage utilizan un SDK (por ejemplo, [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)o [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)) o la [API de Cloud Object Storage](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api). Normalmente los objetos se cargan en [varias partes](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects); la clase Transfer Manager configura el tamaño y el número de las partes.
