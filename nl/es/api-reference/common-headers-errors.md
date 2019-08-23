---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Cabeceras y códigos de error comunes
{: #compatibility-common}

## Cabeceras de solicitud comunes
{: #compatibility-request-headers}

En la tabla siguiente se describen las cabeceras de solicitud comunes admitidas. {{site.data.keyword.cos_full}} pasa por alto las cabeceras comunes que no se muestran a continuación si se envían en una solicitud, aunque algunas de las solicitudes pueden dar soporte a otras cabeceras, tal como se describe en esta documentación.

| Cabecera                  | Nota                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Authorization           | **Obligatoria** para todas las solicitudes (señal OAuth2 `bearer`).                                                                            |
| ibm-service-instance-id | **Obligatoria** para las solicitudes para crear o para obtener una lista de grupos.                                                                              |
| Content-MD5             | El hash MD5 de 128 bits codificado en base 64 de la carga útil, que se utiliza como comprobación de integridad para garantizar que la carga útil no se ha modificado durante el tránsito.  |
| Expect                  | El valor `100-continue` espera a que el sistema reconozca que las cabeceras son apropiadas antes de enviar la carga útil. |
| host                    | El punto final o la sintaxis de 'host virtual' de `{nombre-grupo}.{puntofinal}`. Generalmente esta cabecera se añade automáticamente. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)    | 
| Cache-Control | Se puede utilizar para especificar el comportamiento de la memoria caché en la cadena de solicitud/respuesta. Para obtener más información, visite http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 |

### Metadatos personalizados
{: #compatibility-headers-metadata}

Una ventaja de utilizar el almacenamiento de objetos es la posibilidad de añadir metadatos personalizados mediante el envío de pares de clave-valor como cabeceras. Estas cabeceras adoptan la forma de `x-amz-meta-{KEY}`. Observe que, a diferencia de AWS S3, IBM COS combina varias cabeceras con la misma clave de metadatos en una lista de valores separados por comas.

## Cabeceras de respuesta comunes
{: #compatibility-response-headers}

En la tabla siguiente se describen las cabeceras de respuesta comunes.

| Cabecera           | Nota                                                |
|------------------|-----------------------------------------------------|
| Content-Length   | La longitud del cuerpo de la solicitud en bytes.           |
| Connection       | Indica si la conexión está abierta o cerrada. |
| Date             | Indicación de fecha y hora de la solicitud.                          |
| ETag             | Valor de hash MD5 de la solicitud.                     |
| Server           | Nombre del servidor que responde.                     |
| X-Clv-Request-Id | Identificador exclusivo que se genera por solicitud.           |

### Cabeceras de respuesta de ciclo de vida
{: #compatibility-lifecycle-headers}

En la tabla siguiente se describen las cabeceras de respuesta para objetos archivados

| Cabecera           | Nota                                                |
|------------------|-----------------------------------------------------|
|x-amz-restore|Se incluye si el objeto se ha restaurado o si hay una restauración en curso.|
|x-amz-storage-class|Devuelve `GLACIER` si se archiva o se restaura temporalmente.|
|x-ibm-archive-transition-time|Devuelve la fecha y la hora en que se ha planificado la transición del objeto al nivel de archivado.|
|x-ibm-transition|Se incluye si el objeto tiene metadatos de transición y devuelve el nivel y el tiempo original de la transición.|
|x-ibm-restored-copy-storage-class|Se incluye si un objeto se encuentra en los estados `RestoreInProgress` o `Restored` y devuelve la clase de almacenamiento del grupo.|

## Códigos de error
{: #compatibility-errors}

| Código de error                          | Descripción                                                                                                                                                             | Código de estado HTTP                    |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | Acceso denegado                                                                                                                                                           | 403 Prohibido                       |
| BadDigest                           | El valor Content-MD5 que ha especificado no coincide con lo que se ha recibido.                                                                                                          | 400 Solicitud errónea                     |
| BucketAlreadyExists                 | El nombre de grupo solicitado no está disponible. El espacio de nombres del grupo se comparte entre todos los usuarios del sistema. Seleccione otro nombre e inténtelo de nuevo.                   | 409 Conflicto                        |
| BucketAlreadyOwnedByYou             | Su solicitud anterior de crear el grupo nombrado se ha ejecutado correctamente y ya lo tiene.                                                                                     | 409 Conflicto                        |
| BucketNotEmpty                      | El grupo que ha intentado suprimir no está vacío.                                                                                                                           | 409 Conflicto                        |
| CredentialsNotSupported             | Esta solicitud no da soporte a credenciales.                                                                                                                             | 400 Solicitud errónea                     |
| EntityTooSmall                      | La carga propuesta es menor que el tamaño mínimo de objeto permitido.                                                                                                  | 400 Solicitud errónea                     |
| EntityTooLarge                      | La carga propuesta supera el tamaño máximo de objeto permitido.                                                                                                          | 400 Solicitud errónea                     |
| IncompleteBody                      | No ha proporcionado el número de bytes especificado por la cabecera HTTP Content-Length.                                                                                   | 400 Solicitud errónea                     |
| IncorrectNumberOfFilesInPostRequest | POST necesita exactamente una carga de archivo por solicitud.                                                                                                                     | 400 Solicitud errónea                     |
| InlineDataTooLarge                  | Los datos en línea superan el tamaño máximo permitido.                                                                                                                          | 400 Solicitud errónea                     |
| InternalError                       | Se ha producido un error interno. Vuélvalo a intentar.                                                                                                                    | 500 Error de servidor interno           |
| InvalidAccessKeyId                  | El ID de clave de acceso de AWS que ha proporcionado no existe en nuestros registros.                                                                                                      | 403 Prohibido                       |
| InvalidArgument                     | Argumento no válido                                                                                                                                                        | 400 Solicitud errónea                     |
| InvalidBucketName                   | El grupo especificado no es válido.                                                                                                                                     | 400 Solicitud errónea                     |
| InvalidBucketState                  | La solicitud no es válida con el estado actual del grupo.                                                                                                         | 409 Conflicto                        |
| InvalidDigest                       | El valor Content-MD5 que ha especificado no es válido.                                                                                                                            | 400 Solicitud errónea                     |
| InvalidLocationConstraint           | La restricción de ubicación especificada no es válida. Para obtener más información acerca de las regiones, consulte Cómo seleccionar una región para sus grupos.                                       | 400 Solicitud errónea                     |
| InvalidObjectState                  | La operación no es válida para el estado actual del objeto.                                                                                                        | 403 Prohibido                       |
| InvalidPart                         | No se ha encontrado una o varias de las partes especificadas. Es posible que la parte no se haya cargado o que el código de entidad especificado no coincida con el código de entidad de la parte. | 400 Solicitud errónea                     |
| InvalidPartOrder                    | La lista de partes no está en orden ascendente. La lista de partes se debe especificar en orden por número de parte.                                                                       | 400 Solicitud errónea                     |
| InvalidRange                        | No se puede satisfacer el rango solicitado.                                                                                                                               | 416 No se puede satisfacer el rango solicitado |
| InvalidRequest                      | Utilice AWS4-HMAC-SHA256.                                                                                                                                           | 400 Solicitud errónea                     |
| InvalidSecurity                     | Las credenciales de seguridad especificadas no son válidas.                                                                                                                       | 403 Prohibido                       |
| InvalidURI                          | No se ha podido analizar el URI especificado.                                                                                                                                      | 400 Solicitud errónea                     |
| KeyTooLong                          | La clave es demasiado larga.                                                                                                                                                  | 400 Solicitud errónea                     |
| MalformedPOSTRequest                | El cuerpo de la solicitud POST no consta de datos de formato o de varias partes bien formados.                                                                                                  | 400 Solicitud errónea                     |
| MalformedXML                        | El XML que ha proporcionado no está bien formado o no se ha validado con nuestro esquema publicado.                                                                             | 400 Solicitud errónea                     |
| MaxMessageLengthExceeded            | La solicitud es demasiado grande.                                                                                                                                              | 400 Solicitud errónea                     |
| MaxPostPreDataLengthExceededError   | Los campos de la solicitud POST que preceden al archivo de carga son demasiado grandes.                                                                                                     | 400 Solicitud errónea                     |
| MetadataTooLarge                    | Las cabeceras de metadatos superan el tamaño máximo permitido de metadatos.                                                                                                        | 400 Solicitud errónea                     |
| MethodNotAllowed                    | El método especificado no está permitido en este recurso.                                                                                                             | 405 Método no permitido              |
| MissingContentLength                | Debe especificar la cabecera HTTP Content-Length.                                                                                                                       | 411 Longitud necesaria.                 |
| MissingRequestBodyError             | Esto sucede cuando el usuario envía un documento xml vacío como solicitud. El mensaje de error es "El cuerpo de la solicitud está vacío."                                                     | 400 Solicitud errónea                     |
| NoSuchBucket                        | El grupo especificado no existe.                                                                                                                                   | 404 No encontrado                       |
| NoSuchKey                           | La clave especificada no existe.                                                                                                                                      | 404 No encontrado                       |
| NoSuchUpload                        | La carga de varias partes especificada no existe. Es posible que el ID de carga no sea válido o que la carga de varias partes haya terminado anormalmente o haya finalizado.                           | 404 No encontrado                       |
| NotImplemented                      | Una cabecera que ha especificado implica una funcionalidad que no se ha implementado.                                                                                                   | 501 No implementado                 |
| OperationAborted                    | Actualmente está en curso una operación condicional en conflicto sobre este recurso. Inténtelo de nuevo.                                                                         | 409 Conflicto                        |
| PreconditionFailed                  | Al menos una de las condiciones previas que ha especificado no se ha retenido.                                                                                                          | 412 Error de condición previa             |
| Redirect                            | Redirección temporal.                                                                                                                                                    | 307 Movido temporalmente               |
| RequestIsNotMultiPartContent        | La operación POST de grupo debe ser de datos de formato o de varias partes del tipo enclosure-type.                                                                                                         | 400 Solicitud errónea                     |
| RequestTimeout                      | La conexión de socket con el servidor no se ha leído ni se ha escrito en el periodo de tiempo de espera.                                                                        | 400 Solicitud errónea                     |
| RequestTimeTooSkewed                | La diferencia entre el tiempo de solicitud y el tiempo del servidor es demasiado grande.                                                                                            | 403 Prohibido                       |
| ServiceUnavailable                  | Reduzca la tasa de la solicitud.                                                                                                                                              | 503 Servicio no disponible             |
| SlowDown                            | Reduzca la tasa de la solicitud.                                                                                                                                              | 503 Más despacio                       |
| TemporaryRedirect                   | Se le está redirigiendo al grupo mientras se actualiza el DNS.                                                                                                              | 307 Movido temporalmente               |
| TooManyBuckets                      | Ha intentado crear más grupos de los que se permiten.                                                                                                                | 400 Solicitud errónea                     |
| UnexpectedContent                   | Esta solicitud no da soporte al contenido.                                                                                                                                 | 400 Solicitud errónea                     |
| UserKeyMustBeSpecified              | La operación POST del grupo debe contener el nombre de campo especificado. Si se especifica, compruebe el orden de los campos.                                                              | 400 Solicitud errónea                     |
