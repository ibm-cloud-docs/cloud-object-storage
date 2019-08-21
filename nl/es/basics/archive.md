---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Archivado de datos en frío con reglas de transición
{: #archive}

El archivado de {{site.data.keyword.cos_full}} es una opción de [bajo coste](https://www.ibm.com/cloud/object-storage) para datos a los que se accede con poca frecuencia. Puede almacenar datos transfiriéndolos de los niveles de almacenamiento (Estándar, Caja fuerte, Caja fuerte fría y Flex) a un archivador a largo plazo fuera de línea o puede utilizar la opción Caja fuerte fría.
{: shortdesc}

Puede archivar objetos mediante la consola web, la API REST y herramientas de terceros integradas en IBM Cloud Object Storage. 

Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Adición o gestión de una política de archivado en un grupo
{: #archive-add}

Al crear o modificar una política de archivado para un grupo, tenga en cuenta lo siguiente:

* Una política de archivado se puede añadir a un grupo nuevo o existente en cualquier momento. 
* Una política de archivado existente se puede modificar o inhabilitar. 
* Una política de archivado recién añadida o modificada se aplica a los nuevos objetos cargados y no afecta a los objetos existentes.

Para archivar de forma inmediata los nuevos objetos cargados en un grupo, especifique 0 días en la política de archivado.
{:tip}

El archivado solo está disponible en determinadas regiones. Consulte [Servicios integrados](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability) para obtener más información.
{:tip}

## Restauración de un objeto archivado
{: #archive-restore}

Para acceder a un objeto archivado, debe restaurarlo en el nivel de almacenamiento original. Cuando restaure un objeto, puede especificar el número de días que desea que el objeto esté disponible. Al final del periodo especificado, la copia restaurada se suprime. 

El proceso de restauración puede llevar hasta 12 horas.
{:tip}

Los subestados de un objeto archivados son los siguientes:

* Archivado: un objeto en estado archivado se ha movido de su nivel de almacenamiento en línea (Estándar, Caja fuerte, Caja fuerte fría y Flex) al nivel de archivado fuera de línea en función de la política de archivado del grupo.
* Restaurando: un objeto en estado restaurando están en proceso de generar una copia a partir del estado archivado a su nivel de almacenamiento en línea original.
* Restaurado: un objeto en el estado restaurado es una copia del objeto archivado que se ha restaurado en su nivel de almacenamiento en línea original durante un periodo de tiempo especificado. Al final del periodo, la copia del objeto se suprime, mientras se mantiene el objeto archivado.

## Limitaciones
{: #archive-limitations}

Las políticas de archivado se implementan mediante un subconjunto de la operación de la API S3 `PUT de configuración de ciclo de vida de un grupo`. 

Las funciones admitidas incluyen:
* Especificación de una fecha o del número de días en el futuro en que los objetos se transferirán a un estado archivado.
* Establecimiento de [reglas de caducidad](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry) para los objetos.

Las funciones no admitidas incluyen:
* Varias reglas de transición por grupo.
* Filtrado de objetos que archivar mediante un prefijo o una clave de objeto.
* Definición de niveles entre clases de almacenamiento.

## Utilización de la API REST y de los SDK
{: #archive-api} 

### Creación de una configuración de ciclo de vida de un grupo
{: #archive-api-create} 
{: http}

Esta implementación de la operación `PUT` utiliza el parámetro de consulta `lifecycle` para definir valores de ciclo de vida para el grupo. Esta operación permite una sola definición de política de ciclo de vida para un determinado grupo. La política se define como una regla que consta de los parámetros siguientes: `ID`, `Status` y `Transition`.
{: http}

La acción de transición permite pasar objetos futuros escritos en el grupo a un estado archivado después de un periodo de tiempo definido. Los cambios en la política de ciclo de vida para un grupo **solo se aplican a los objetos nuevos** escritos en ese grupo.

Los usuarios de Cloud IAM deben tener como mínimo el rol de `Escritor` para poder añadir una política de ciclo de vida al grupo.

Los usuarios de la infraestructura clásica deben tener permisos de Propietario y poder crear grupos en la cuenta de almacenamiento para añadir una política de ciclo de vida al grupo.

Esta operación no utiliza parámetros de consulta adicionales específicos de la operación.
{: http}

Cabecera                    | Tipo   | Descripción
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | serie | **Obligatorio**: el hash MD5 de 128 bits codificado en base 64 de la carga útil, que se utiliza como comprobación de integridad para garantizar que la carga útil no se ha modificado durante el tránsito.
{: http}

El cuerpo de la solicitud debe contener un bloque XML con el esquema siguiente:
{: http}

| Elemento                  | Tipo                 | Hijos                               | Predecesor                 | Restricción                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Contenedor            | `Rule`                                 | Ninguno                     | Límite 1.                                                                                  |
| `Rule`                   | Contenedor            | `ID`, `Status`, `Filter`, `Transition` | `LifecycleConfiguration` | Límite 1.                                                                                  |
| `ID`                     | Serie               | Ninguno                                   | `Rule`                   | Debe consistir en (`a-z,`A-Z0-9`) y los siguientes símbolos: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | Serie               | `Prefix`                               | `Rule`                   | Debe contener un elemento `Prefix`                                                            |
| `Prefix`                 | Serie               | Ninguno                                   | `Filter`                 | Se **debe** establecer en `<Prefix/>`.                                                           |
| `Transition`             | `Contenedor`          | `Days`, `StorageClass`                 | `Rule`                   | Límite 1.                                                                                  |
| `Days`                   | Entero no negativo | Ninguno                                   | `Transition`             | Debe ser un valor mayor que 0.                                                           |
| `Date`                   | Fecha                 | Ninguno                                   | `Transistion`            | Debe estar en formato ISO 8601 y la fecha debe ser futura.                            |
| `StorageClass`           | Serie               | Ninguno                                   | `Transition`             | Se **debe** establecer en `GLACIER`.                                                             |
{: http}

__Sintaxis__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Ejemplo 1. Observe el uso de barras inclinadas y puntos en este ejemplo de sintaxis." caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
		<Filter>
			<Prefix/>
		</Filter>
		<Transition>
			<Days>{integer}</Days>
			<StorageClass>GLACIER</StorageClass>
		</Transition>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Ejemplo 2. Ejemplo de XML para crear una configuración de ciclo de vida de objeto." caption-side="bottom"}

__Ejemplos__
{: http}

_Solicitud de ejemplo_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Ejemplo 3. Ejemplos de cabecera de solicitud para crear una configuración de ciclo de vida de objeto." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Ejemplo 4. Ejemplo de XML para el cuerpo de la solicitud PUT." caption-side="bottom"}

_Respuesta de ejemplo_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Ejemplo 5. Cabeceras de la respuesta." caption-side="bottom"}

---

### Recuperación de una configuración de ciclo de vida de un grupo
{: #archive-api-retrieve} 
{: http}

Esta implementación de la operación `GET` utiliza el parámetro de consulta `lifecycle` para recuperar valores de ciclo de vida para el grupo. 

Los usuarios de Cloud IAM deben tener como mínimo el rol de `Lector` para poder recuperar un ciclo de vida para un grupo.

Los usuarios de la infraestructura clásica deben tener como mínimo permisos de `Lectura` sobre el grupo para recuperar una política de ciclo de vida para un grupo.

Esta operación no utiliza cabeceras adicionales específicas de la operación, parámetros de consulta ni carga útil.

__Sintaxis__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Ejemplo 6. Variaciones de la sintaxis de las solicitudes GET." caption-side="bottom"}

__Ejemplos__ 
{: http}

_Solicitud de ejemplo_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Ejemplo 7. Cabeceras de solicitud de ejemplo para recuperar la configuración." caption-side="bottom"}

_Respuesta de ejemplo_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Ejemplo 8. Cabeceras de solicitud de ejemplo de la solicitud GET." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Ejemplo 9. Ejemplo de XML para el cuerpo de la respuesta." caption-side="bottom"}

---

### Supresión de una configuración de ciclo de vida de un grupo
{: #archive-api-delete} {: http}

Esta implementación de la operación `DELETE` utiliza el parámetro de consulta `lifecycle` para eliminar valores de ciclo de vida para el grupo. Las transiciones definidas por las reglas dejarán de ejecutarse para los objetos nuevos. 

**Nota:** se mantendrán las reglas de transición existentes para los objetos que ya se habían escrito en el grupo antes de que se suprimieran las reglas.

Los usuarios de Cloud IAM deben tener como mínimo el rol de `Escritor` para poder eliminar una política de ciclo de vida de un grupo.

Los usuarios de la infraestructura clásica deben tener permisos de `Propietario` sobre el grupo para eliminar una política de ciclo de vida de un grupo.

Esta operación no utiliza cabeceras adicionales específicas de la operación, parámetros de consulta ni carga útil.

__Sintaxis__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Ejemplo 10. Observe el uso de barras inclinadas y puntos en el ejemplo de sintaxis." caption-side="bottom"}

__Ejemplos__
{: http}

_Solicitud de ejemplo_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Ejemplo 11. Cabeceras de solicitud de ejemplo para el verbo DELETE HTTP." caption-side="bottom"}

_Respuesta de ejemplo_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Ejemplo 12. Respuesta de ejemplo de la solicitud DELETE." caption-side="bottom"}

---

### Restauración temporal de un objeto archivado 
{: #archive-api-restore} {: http}

Esta implementación de la operación `POST` utiliza el parámetro de consulta `restore` para solicitar la restauración temporal de un objeto archivado. Primero el usuario debe restaurar un objeto archivado para poder descargar o modificar el objeto. Cuando se restaura un objeto, el usuario debe especificar un periodo después del cual se suprimirá la copia temporal del objeto. El objeto mantiene la clase de almacenamiento del grupo.

Puede haber un retardo de hasta 12 horas antes de que se pueda acceder a la copia restaurada. Una solicitud `HEAD` puede comprobar si la copia restaurada está disponible. 

Para restaurar el objeto de forma permanente, el usuario debe copiar el objeto restaurado en un grupo que no tenga una configuración de ciclo de vida activa.

Los usuarios de Cloud IAM deben tener como mínimo el rol de `Escritor` para poder restaurar un objeto.

Los usuarios de la infraestructura clásica deben tener como mínimo permisos de `escritura` sobre el grupo y el permiso de `Lectura` sobre el objeto para restaurarlo.

Esta operación no utiliza parámetros de consulta adicionales específicos de la operación.

Cabecera                    | Tipo   | Descripción
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | serie | **Obligatorio**: el hash MD5 de 128 bits codificado en base 64 de la carga útil, que se utiliza como comprobación de integridad para garantizar que la carga útil no se ha modificado durante el tránsito.

El cuerpo de la solicitud debe contener un bloque XML con el esquema siguiente:

Elemento                  | Tipo      | Hijos                               | Predecesor                 | Restricción
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | Contenedor | `Days`, `GlacierJobParameters`    | Ninguno       | Ninguna
`Days`                   | Entero | Ninguno | `RestoreRequest` | Se ha especificado el tiempo de vida del objeto restaurado temporalmente. El número mínimo de días que puede existir una copia restaurada del objeto es 1. Una vez transcurrido el período de restauración, se elimina la copia temporal del objeto.
`GlacierJobParameters` | Serie | `Tier` | `RestoreRequest` | Ninguna
`Tier` | Serie | Ninguno | `GlacierJobParameters` | Se **debe** establecer en `Bulk`.

Una respuesta correcta devuelve `202` si el objeto se encuentra en el estado archivado y `200` si el objeto ya está en el estado restaurado.  Si el objeto ya está en el estado restaurado y se recibe una nueva solicitud para restaurar el objeto, el elemento `Days` actualizará el tiempo de caducidad del objeto restaurado.

__Sintaxis__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="Ejemplo 13. Observe el uso de barras inclinadas y puntos en el ejemplo de sintaxis." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days>
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Ejemplo 14. Modelo de XML para el cuerpo de la solicitud." caption-side="bottom"}

__Ejemplos__
{: http}

_Solicitud de ejemplo_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Ejemplo 15. Cabeceras de solicitud de ejemplo para la restauración de objetos." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days>
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Ejemplo 16. Cuerpo de solicitud de ejemplo para la restauración de objetos." caption-side="bottom"}

_Respuesta de ejemplo_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Ejemplo 17. Respuesta para la restauración de objetos (`HTTP 202`)." caption-side="bottom"}

---

### Obtención de las cabeceras de un objeto
{: http}
{: #archive-api-head}

Un mandato `HEAD` con una vía de acceso a un objeto recupera las cabeceras de ese objeto. Esta operación no utiliza parámetros de consulta ni elementos de carga útil específicos de la operación.

__Sintaxis__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="Ejemplo 18. Variaciones en la definición de puntos finales." caption-side="bottom"}


__Cabeceras de respuesta para objetos archivados__
{: http}

Cabecera | Tipo | Descripción
--- | ---- | ------------
`x-amz-restore` | serie | Se incluye si el objeto se ha restaurado o si hay una restauración en curso. Si el objeto se ha restaurado, también se devuelve la fecha de caducidad de la copia temporal.
`x-amz-storage-class` | serie | Devuelve `GLACIER` si se archiva o se restaura temporalmente.
`x-ibm-archive-transition-time` | fecha | Devuelve la fecha y la hora en que se ha planificado la transición del objeto al nivel de archivado.
`x-ibm-transition` | serie | Se incluye si el objeto tiene metadatos de transición y devuelve el nivel y el tiempo original de la transición.
`x-ibm-restored-copy-storage-class` | serie | Se incluye si un objeto se encuentra en los estados `RestoreInProgress` o `Restored` y devuelve la clase de almacenamiento del grupo.


_Solicitud de ejemplo_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="Ejemplo 19. Ejemplo que muestra cabeceras de solicitud." caption-side="bottom"}

_Respuesta de ejemplo_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="Ejemplo 20. Ejemplo que muestra cabeceras de respuesta." caption-side="bottom"}


### Creación de una configuración de ciclo de vida de un grupo
{: #archive-node-create} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* obligatorio */
  LifecycleConfiguration: {
    Rules: [ /* obligatorio */
      {
        Status: 'Enabled', /* obligatorio */
        ID: 'STRING_VALUE',
        Filter: '', /* obligatorio */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* obligatorio si no se especifica Days */
            Days: 0, /* obligatorio si no se especifica Date */
            StorageClass: 'GLACIER' /* obligatorio */
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 21. Ejemplo que muestra cómo crear la configuración del ciclo de vida." caption-side="bottom"}

### Recuperación de una configuración de ciclo de vida de un grupo
{: #archive-node-retrieve} {: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* obligatorio */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 22. Ejemplo que muestra la recuperación de los metadatos del ciclo de vida." caption-side="bottom"}

### Supresión de una configuración de ciclo de vida de un grupo
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* obligatorio */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 23. Ejemplo que muestra cómo suprimir la configuración del ciclo de vida de un grupo." caption-side="bottom"}

### Restauración temporal de un objeto archivado 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* obligatorio */
  Key: 'STRING_VALUE', /* obligatorio */
  ContentMD5: 'STRING_VALUE', /* obligatorio */
  RestoreRequest: {
   Days: 1, /* días hasta que caduque la copia */
   GlacierJobParameters: {
     Tier: Bulk /* obligatorio */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 24. Código utilizado en la restauración de un objeto archivado." caption-side="bottom"}

### Obtención de las cabeceras de un objeto
{: #archive-node-head} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* obligatorio */
  Key: 'STRING_VALUE', /* obligatorio */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 25. Ejemplo que muestra la recuperación de las cabeceras de un objeto." caption-side="bottom"}


### Creación de una configuración de ciclo de vida de un grupo
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="Ejemplo 26. Método utilizado para crear la configuración de un objeto." caption-side="bottom"}

### Recuperación de una configuración de ciclo de vida de un grupo
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Ejemplo 27. Método utilizado para recuperar la configuración de un objeto." caption-side="bottom"}

### Supresión de una configuración de ciclo de vida de un grupo
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Ejemplo 28. Método utilizado para suprimir la configuración de un objeto." caption-side="bottom"}

### Restauración temporal de un objeto archivado 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="Ejemplo 29. Restauración temporal de un objeto archivado." caption-side="bottom"}

### Obtención de las cabeceras de un objeto
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="Ejemplo 30. Manejo de la respuesta de cabeceras de objeto." caption-side="bottom"}


### Creación de una configuración de ciclo de vida de un grupo
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="Ejemplo 31. Función utilizada para definir el ciclo de vida de un grupo." caption-side="bottom"}

**Resumen del método**
{: java}

Método |  Descripción
--- | ---
`getBucketName()` | Obtiene el nombre del grupo cuya configuración de ciclo de vida se va a definir.
`getLifecycleConfiguration()` | Obtiene la nueva configuración del ciclo de vida para el grupo especificado.
`setBucketName(String bucketName)` | Define el nombre del grupo cuya configuración de ciclo de vida se va a definir.
`withBucketName(String bucketName)` | Define el nombre del grupo cuya configuración de ciclo de vida se va a definir y devuelve este objeto para que las llamadas adicionales al método se puedan encadenar.
{: java}

### Recuperación de una configuración de ciclo de vida de un grupo
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Ejemplo 32. Firma de función para obtener la configuración del ciclo de vida." caption-side="bottom"}

### Supresión de una configuración de ciclo de vida de un grupo
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Ejemplo 33. Función utilizada para suprimir la configuración de un objeto." caption-side="bottom"}

### Restauración temporal de un objeto archivado 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="Ejemplo 34. Firma de función para restaurar un objeto archivado." caption-side="bottom"}

**Resumen del método**
{: java}

Método |  Descripción
--- | ---
`clone()` | Crea un clon de este objeto para todos los campos excepto el contexto de manejador.
`getBucketName()` | Devuelve el nombre del grupo que contiene la referencia al objeto que se va a restaurar.
`getExpirationInDays()` | Devuelve el tiempo en días entre la creación de un objeto y su caducidad.
`setExpirationInDays(int expirationInDays)` | Establece el tiempo, en días, entre el momento en que se carga un objeto en el grupo y el momento en que caduca.
{: java}

### Obtención de las cabeceras de un objeto
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="Ejemplo 35. Función utilizada para obtener las cabeceras de un objeto." caption-side="bottom"}

**Resumen del método**
{: java}

Método |  Descripción
--- | ---
`clone()` | Devuelve un clon de este `ObjectMetadata`.
`getRestoreExpirationTime()` | Devuelve el momento en que caducará un objeto que se ha restaurado temporalmente desde ARCHIVE, y será necesario volverlo a restaurar para poder acceder al mismo.
`getStorageClass() ` | Devuelve la clase de almacenamiento original del grupo.
`getIBMTransition()` | Devuelva la clase de almacenamiento de transición y la hora de la transición.
{: java}

## Siguientes pasos
{: #archive-next-steps}

Además de {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} actualmente proporciona varias ofertas de almacenamiento de objetos adicionales para distintas necesidades de los usuarios, todas ellas accesibles a través de portales web y API REST. [Más información.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
