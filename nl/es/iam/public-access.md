---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Habilitación del acceso público
{: #iam-public-access}

A veces los datos están diseñados para ser compartidos. Los grupos pueden contener conjuntos de datos abiertos para investigaciones académicas y privadas o repositorios de imágenes que utilizan las aplicaciones web o las redes de distribución de contenido. Para conseguir que estos grupos sean accesibles, utilice el grupo **Acceso público**.
{: shortdesc}

## Utilización de la consola para establecer el acceso público
{: #iam-public-access-console}

En primer lugar, asegúrese de que tiene un grupo. Si no es así, siga la [guía de aprendizaje de iniciación](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para familiarizarse con la consola.

### Habilitación del acceso público
{: #public-access-console-enable}

1. En el {{site.data.keyword.cloud_notm}}[panel de control de la consola](https://cloud.ibm.com/), seleccione **Almacenamiento** para ver la lista de recursos.
2. A continuación, seleccione la instancia de servicio con el grupo desde el menú **Almacenamiento**. Esto le lleva a la consola de {site.data.keyword.cos_short}}.
3. Elija el grupo al que desea asignar acceso público. Tenga en cuenta que esta política hace que cualquiera que tenga el URL adecuado pueda descargar _todos los objetos del grupo_.
4. Seleccione **Políticas de acceso** en el menú de navegación.
5. Seleccione el separador **Acceso público**.
6. Pulse **Crear política de acceso**. Después de leer el aviso, seleccione **Habilitar**.
7. Ahora se puede acceder de forma pública a todos los objetos de este grupo.

### Inhabilitación del acceso público
{: #public-access-console-disable}

1. Desde cualquier logar de la [consola](https://cloud.ibm.com/) de {{site.data.keyword.cloud_notm}}, seleccione el menú **Gestionar** y **Acceso (IAM)**.
2. Seleccione **Grupos de acceso** en el menú de navegación.
3. Seleccione **Acceso público** para ver una lista de todas las políticas de acceso público que se están utilizando actualmente.
4. Localice la política correspondiente al grupo al que desea volver a aplicar al control de acceso.
5. En la lista de acciones de la parte derecha de la entrada de política, seleccione **Eliminar**.
6. Confirme el recuadro de diálogo y la política se eliminará del grupo.

## Habilitación del acceso público sobre objetos individuales
{: #public-access-object}

Para que se pueda acceder de forma pública a un objeto a través de la API REST, se puede incluir una cabecera `x-amz-acl: public-read` en la solicitud. Si se define esta cabecera, se omite cualquier comprobación de [política de IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) y se permiten solicitudes `HEAD` y `GET` no autenticadas. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Además, las [credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) hacen posible que se pueda permitir el [acceso público temporal que utiliza URL prefirmados](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url).

### Carga de un objeto público
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### Habilitación del acceso público a un objeto existente
{: #public-access-object-existing}

Si se utiliza el parámetro de consulta `? acl` sin una carga útil y la cabecera `x-amz-acl: public-read`, se permite el acceso público al objeto sin necesidad de sobrescribir datos.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### Cómo volver a convertir un objeto público en privado
{: #public-access-object-private}

Si se utiliza el parámetro de consulta `? acl` sin una carga útil y una cabecera `x-amz-acl:` vacía, se revoca el acceso público al objeto sin necesidad de sobrescribir datos.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## Sitios web estáticos
{: #public-access-static-website}

Aunque {{site.data.keyword.cos_full_notm}} no da soporte al alojamiento automático de sitios web estáticos, se puede configurar manualmente un servidor web y utilizarlo para ofrecer contenido de acceso público alojado en un grupo. Para obtener más información, consulte [esta guía de aprendizaje](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).
