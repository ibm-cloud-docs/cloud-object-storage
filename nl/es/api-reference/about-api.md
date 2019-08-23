---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# Acerca de la API de {{site.data.keyword.cos_full_notm}} S3
{: #compatibility-api}

La API de {{site.data.keyword.cos_full}} es una API basada en REST para leer y escribir objetos. Utiliza {{site.data.keyword.iamlong}} para la autenticación y la autorización, y da soporte a un subconjunto de la API de S3 para facilitar la migración de aplicaciones a {{site.data.keyword.cloud_notm}}.

Esta documentación de consulta se mejora continuamente. Si tiene preguntas técnicas sobre el uso de la API en la aplicación, publíquelas en [StackOverflow](https://stackoverflow.com/). Añada las etiquetas `ibm-cloud-platform` y `object-storage` para ayudar a mejorar esta documentación con sus comentarios.

Puesto que resulta relativamente fácil trabajar con señales de {{site.data.keyword.iamshort}}, `curl` es una buena opción para las pruebas básicas y la interacción con el almacenamiento. Encontrará más información en [la consulta de `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl).

En las tablas siguientes se describe el conjunto completo de operaciones de la API de {{site.data.keyword.cos_full_notm}}. Para obtener más información, consulte [la página de consulta de API para grupos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) y [objetos](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).


## Operaciones de grupo
{: #compatibility-api-bucket}

Estas operaciones crean grupos, los suprimen, obtienen información sobre los mismos y controlan su comportamiento.

| Operación de grupo        | Nota                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` Buckets           | Se utiliza para recuperar una lista de todos los grupos pertenecientes a una cuenta.              |
| `DELETE` Bucket         | Suprime un grupo vacío.                                                       |
| `DELETE` Bucket CORS    | Suprime cualquier configuración de CORS (compartición de recursos entre orígenes) definida en un grupo. |
| `GET` Bucket            | Muestra una lista de los objetos de un grupo. Limitado a mostrar 1.000 objetos a la vez.         |
| `GET` Bucket CORS       | Recupera cualquier configuración de CORS definida en un grupo.                              |
| `HEAD` Bucket           | Recupera las cabeceras de un grupo.                                                  |
| `GET` Multipart Uploads | Muestra las cargas de varias partes que no se han completado o cancelado.                     |
| `PUT` Bucket            | Los grupos tienen restricciones de denominación. Las cuentas están limitadas a 100 grupos.         |
| `PUT` Bucket CORS       | Crea una configuración de CORS para un grupo.                                     |


## Operaciones de objetos
{: #compatibility-api-object}

Estas operaciones crean objetos, los suprimen, obtienen información sobre los mismos y controlan su comportamiento.

| Operación de objeto          | Nota                                                                                |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` Object           | Suprime un objeto de un grupo.                                                   |
| `DELETE` Batch            | Suprime muchos objetos de un grupo con una operación.                             |
| `GET` Object              | Recupera un objeto de un grupo.                                                 |
| `HEAD` Object             | Recupera las cabeceras de un objeto.                                                     |
| `OPTIONS` Object          | Comprueba la configuración de CORS para ver si se puede enviar una solicitud específica.           |
| `PUT` Object              | Añade un objeto a un grupo.                                                        |
| `PUT` Object (Copy)       | Crea una copia de un objeto.                                                       |
| Begin Multipart Upload    | Crea un ID de carga para un conjunto de partes que se van a cargar.                            |
| Upload Part               | Carga una parte de un objeto que está asociado con un ID de carga.                  |
| Upload Part (Copy)        | Carga una parte de un objeto existente que está asociado con un ID de carga.         |
| Complete Multipart Upload | Ensambla un objeto a partir de partes que están asociadas con un ID de carga.              |
| Cancel Multipart Upload   | Cancela la carga y suprime las partes pendientes que están asociadas con un ID de carga. |
| List Parts                | Devuelve una lista de partes que están asociadas a un ID de carga                       |


Algunas operaciones adicionales, como etiquetado y mantenimiento de versiones, reciben soporte en implementaciones de nube privada de {{site.data.keyword.cos_short}}, pero actualmente no reciben soporte en nubes públicas o dedicadas. Encontrará más información sobre las soluciones de almacenamiento de objetos personalizados en [ibm.com](https://www.ibm.com/cloud/object-storage).
