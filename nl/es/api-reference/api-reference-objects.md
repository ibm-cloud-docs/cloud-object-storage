---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, objects

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

# Operaciones de objetos
{: #object-operations}

Estas operaciones leen, escriben y configuran los objetos contenidos dentro de un grupo.

Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Carga de un objeto
{: #object-operations-put}

Un mandato `PUT` con una vía de acceso a un objeto carga el cuerpo de la solicitud como un objeto. Todos los objetos que se carguen en una sola hebra deben tener menos de 500 MB (los objetos [cargados en varias partes](/docs/services/cloud-object-storage?topic=cloud-object-storage-large-objects) pueden tener hasta 10 TB).

**Nota**: Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
{:tip}

**Sintaxis**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Solicitud de ejemplo**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Solicitud de ejemplo (HMAC)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: {payload_hash}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
PUT /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

----

## Obtención de las cabeceras de un objeto
{: #object-operations-head}

Un mandato `HEAD` con una vía de acceso a un objeto recupera las cabeceras de ese objeto.

Observe que el valor `Etag` que se devuelve para los objetos cifrados mediante SSE-KP **no** será el hash MD5 del objeto descifrado original.
{:tip}

**Sintaxis**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Solicitud de ejemplo**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:32:44 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: da214d69-1999-4461-a130-81ba33c484a6
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:49:06 GMT
Content-Length: 11
```

----

## Descarga de un objeto
{: #object-operations-get}

Un mandato `GET` con una vía de acceso a un objeto descarga el objeto.

Observe que el valor `Etag` que se devuelve para los objetos cifrados mediante SSE-C/SSE-KP **no** será el hash MD5 del objeto descifrado original.
{:tip}

**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### Cabeceras opcionales
{: #object-operations-get-headers}

Cabecera | Tipo | Descripción
--- | ---- | ------------
`range` | serie | Devuelve los bytes de un objeto dentro del rango especificado.

**Solicitud de ejemplo**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:34:25 GMT
X-Clv-Request-Id: 116dcd6b-215d-4a81-bd30-30291fa38f93
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 116dcd6b-215d-4a81-bd30-30291fa38f93
ETag: "d34d8aada2996fc42e6948b926513907"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:46:53 GMT
Content-Length: 467

 Female bees that are not fortunate enough to be selected to be the 'queen'
 while they were still larvae become known as 'worker' bees. These bees lack
 the ability to reproduce and instead ensure that the hive functions smoothly,
 acting almost as a single organism in fulfilling their purpose.
```

----

## Supresión de un objeto
{: #object-operations-delete}

Un mandato `DELETE` con una vía de acceso a un objeto suprime el objeto.

**Sintaxis**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Solicitud de ejemplo**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 204 No Content
Date: Thu, 25 Aug 2016 17:44:57 GMT
X-Clv-Request-Id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
```

----

## Supresión de varios objetos
{: #object-operations-multidelete}

Un mandato `POST` con una vía de acceso a un grupo y con los parámetros correctos suprimirá un conjunto de objetos especificado. Se requiere una cabecera `Content-MD5` que especifique el hash MD5 codificado en base64 del cuerpo de la solicitud.

La cabecera `Content-MD5` necesaria tiene que ser la representación binaria de un hash MD5 codificado en base64.

**Nota:** si no se encuentra un objeto especificado en la solicitud, en el resultado se devuelve como suprimido. 

### Elementos opcionales
{: #object-operations-multidelete-options}

|Cabecera|Tipo|Descripción|
|---|---|---|
|`Quiet`|Booleano|Habilita la modalidad silenciosa (quiet) para la solicitud.|

La solicitud puede contener un máximo de 1000 claves que desea suprimir. Si bien esto es muy útil para reducir la sobrecarga por solicitud, tenga cuidado cuando vaya a suprimir un gran número de claves. Tenga también en cuenta los tamaños de los objetos para garantizar un rendimiento adecuado.
{:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Sintaxis**

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```

**Solicitud de ejemplo**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
POST /apiary?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&delete=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Delete>
    <Object>
         <Key>surplus-bee</Key>
    </Object>
    <Object>
         <Key>unnecessary-bee</Key>
    </Object>
</Delete>
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 18:54:53 GMT
X-Clv-Request-Id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Content-Type: application/xml
Content-Length: 207
```
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<DeleteResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Deleted>
         <Key>surplus-bee</Key>
    </Deleted>
    <Deleted>
         <Key>unnecessary-bee</Key>
    </Deleted>
</DeleteResult>
```

----

## Copia de un objeto
{: #object-operations-copy}

Un mandato `PUT` con una vía de acceso a un objeto nuevo crea una nueva copia de otro objeto especificado por la cabecera `x-amz-copy-source`. A menos que se alteren de otro modo, los metadatos siguen siendo los mismos.

**Nota**: Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
{:tip}


**Nota**: la copia de un elemento de un grupo habilitado para *Key Protect* en un grupo de destino de otra región está restringida y dará como resultado `500 - Internal Error`.
{:tip}

**Sintaxis**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### Cabeceras opcionales
{: #object-operations-copy-options}

Cabecera | Tipo | Descripción
--- | ---- | ------------
`x-amz-metadata-directive` | serie (`COPY` o `REPLACE`) | `REPLACE` sobrescribirá los metadatos originales con los nuevos metadatos que se proporcionan.
`x-amz-copy-source-if-match` | serie (`ETag`)| Crea una copia si el valor `ETag` especificado coincide con el objeto de origen.
`x-amz-copy-source-if-none-match` | serie (`ETag`)| Crea una copia si el valor `ETag` especificado es distinto del objeto de origen.
`x-amz-copy-source-if-unmodified-since` | serie (indicación de fecha y hora)| Crea una copia si el objeto de origen no se ha modificado desde la fecha especificada. La fecha debe ser una fecha HTTP válida (por ejemplo, `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-copy-source-if-modified-since` | serie (indicación de fecha y hora)| Crea una copia si el objeto de origen se ha modificado desde la fecha especificada. La fecha debe ser una fecha HTTP válida (por ejemplo, `Wed, 30 Nov 2016 20:21:38 GMT`).

**Solicitud de ejemplo**

Este ejemplo básico toma el objeto `bee` del grupo `garden` y crea una copia en el grupo `apiary` con la nueva clave `wild-bee`.

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 19:52:52 GMT
X-Clv-Request-Id: 72992a90-8f86-433f-b1a4-7b1b33714bed
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 72992a90-8f86-433f-b1a4-7b1b33714bed
ETag: "853aab195ce770b0dfb294a4e9467e62"
Content-Type: application/xml
Content-Length: 240
```

```xml
<CopyObjectResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <LastModified>2016-11-30T19:52:53.125Z</LastModified>
  <ETag>"853aab195ce770b0dfb294a4e9467e62"</ETag>
</CopyObjectResult>
```

----

## Comprobación de la configuración de CORS de un objeto
{: #object-operations-options}

Un mandato `OPTIONS` con una vía de acceso a un objeto junto con un origen y un tipo de solicitud comprueba si se puede acceder a dicho objeto desde ese origen mediante este tipo de solicitud. A diferencia de las demás solicitudes, una solicitud OPTIONS no requiere las cabeceras `authorization` ni `x-amx-date`.

**Sintaxis**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Solicitud de ejemplo**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 07 Dec 2016 16:23:14 GMT
X-Clv-Request-Id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: PUT
Access-Control-Allow-Credentials: true
Vary: Origin, Access-Control-Request-Headers, Access-Control-Allow-Methods
Content-Length: 0

```

----

## Carga de objetos en varias partes
{: #object-operations-multipart}

Cuando se trabaja con objetos grandes, se recomienda utilizar operaciones de carga de varias partes para escribir objetos en {{site.data.keyword.cos_full}}. Una carga de un solo objeto se puede realizar como un conjunto de partes, y dichas partes se pueden cargar de forma independiente en cualquier orden o en paralelo. Una vez finalizada la carga, {{site.data.keyword.cos_short}} presenta todas las partes como un solo objeto. Esto ofrece varias ventajas: las interrupciones de red no dan lugar a fallos en cargas grandes, las cargas se pueden poner en pausa y reiniciar a lo largo del tiempo, y los objetos pueden cargarse a medida que se crean.

Las cargas de varias partes solo están disponibles para los objetos de más de 5 MB. Para los objetos de menos de 50 GB, se recomienda un tamaño de parte de entre 20 MB y 100 MB para obtener un rendimiento óptimo. En el caso de objetos más grandes, el tamaño de las partes se puede aumentar sin que ello afecte al rendimiento de forma significativa. Las cargas de varias partes se limitan a 10.000 partes de 5 GB cada una.

El uso de más de 500 partes reduce la eficacia de {{site.data.keyword.cos_short}} y se debe evitar siempre que sea posible.
{:tip}

Debido a la complejidad adicional que esto implica, se recomienda que los desarrolladores utilicen una biblioteca que proporcione soporte de carga de varias partes.

Las cargas de varias partes incompletas persisten hasta que se suprime el objeto o se termina de forma anómala la carga de varias partes con `AbortIncompleteMultipartUpload`. Si no se termina de forma anómala una carga de varias partes incompleta, la carga parcial continúa utilizando recursos. Las interfaces se deben diseñar teniendo esto en cuenta, de modo que limpien las cargas de varias partes incompletas.
{:tip}

El proceso de carga de un objeto en varias partes consta de tres fases:

1. La carga se inicia y se crea un `UploadId`.
2. Las partes individuales se cargan especificando sus números de pieza secuenciales y el `UploadId` correspondiente al objeto.
3. Cuando todas las partes se han terminado de cargar, la carga se completa enviando una solicitud con el `UploadId` y un bloque XML que enumera cada número de pieza y su respectivo valor de `Etag`.

## Inicio de una carga de varias partes
{: #object-operations-multipart-initiate}

Una operación `POST` emitida a un objeto con el parámetro de consulta `upload` crea un nuevo valor de `UploadId`, al que cada parte del objeto que se carga hace referencia.

**Nota**: Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
{:tip}

**Sintaxis**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**Solicitud de ejemplo**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```

----

## Carga de una parte
{: #object-operations-multipart-put-part}

Una solicitud `PUT` que se emite a un objeto con los parámetros de consulta `partNumber` y `uploadId` cargará una parte de un objeto. Las partes se pueden cargar en serie o en paralelo, pero se deben numerar en orden.

**Nota**: Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
{:tip}

**Sintaxis**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**Solicitud de ejemplo**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```

----

## Obtención de una lista de las partes
{: #object-operations-multipart-list}

Un mandato `GET` con una vía de acceso a un objeto de varias partes con un `UploadID` activo especificado como parámetro de consulta devolverá una lista de todas las partes del objeto.


**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```

### Parámetros de consulta
{: #object-operations-multipart-list-params}
Parámetro | ¿Obligatorio?| Tipo | Descripción
--- | ---- | ------------
`uploadId` | Obligatorio | serie | ID de carga que se devuelve cuando se inicializa una carga de varias partes.
`max-parts` | Opcional | serie | El valor predeterminado es 1.000.
`part-number​-marker` | Opcional | serie | Define dónde empezará la lista de partes.

**Solicitud de ejemplo**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Mon, 19 Mar 2018 17:21:08 GMT
X-Clv-Request-Id: 6544044d-4f88-4bb6-9ee5-bfadf5023249
Server: Cleversafe/3.12.4.20
X-Clv-S3-Version: 2.5
Accept-Ranges: bytes
Content-Type: application/xml
Content-Length: 743
```

```xml
<ListPartsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>farm</Bucket>
  <Key>spaceship</Key>
  <UploadId>01000162-3f46-6ab8-4b5f-f7060b310f37</UploadId>
  <Initiator>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Initiator>
  <Owner>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Owner>
  <StorageClass>STANDARD</StorageClass>
  <MaxParts>1000</MaxParts>
  <IsTruncated>false</IsTruncated>
  <Part>
    <PartNumber>1</PartNumber>
    <LastModified>2018-03-19T17:20:35.482Z</LastModified>
    <ETag>"bb03cf4fa8603fe407a65ee1dba55265"</ETag>
    <Size>7128094</Size>
  </Part>
</ListPartsResult>
```

----

## Finalización de una carga de varias partes
{: #object-operations-multipart-complete}

Una solicitud `POST` que se emite a un objeto con el parámetro de consulta `uploadId` y el bloque XML adecuado en el cuerpo completarán una carga de varias partes.

**Sintaxis**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Solicitud de ejemplo**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

## Terminación anómala de cargas de varias partes incompletas
{: #object-operations-multipart-uploads}

Una solicitud `DELETE` emitida a un objeto con el parámetro de consulta `uploadId` suprime todas las partes inacabadas de una carga de varias partes.

**Sintaxis**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**Solicitud de ejemplo**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Restauración temporal de un objeto archivado
{: #object-operations-archive-restore}

Una solicitud `POST` emitida a un objeto con el parámetro de consulta `restore` solicita la restauración temporal de un objeto archivado. Se necesita una cabecera `Content-MD5` como comprobación de la integridad de la carga útil.

Se debe restaurar un objeto archivado para poder descargar o modificar el objeto. Se debe especificar el tiempo de vida del objeto, después del cual la copia temporal del objeto se suprimirá.

Puede haber un retardo de hasta 15 horas antes de que se pueda acceder a la copia restaurada. Una solicitud HEAD puede comprobar si la copia restaurada está disponible.

Para restaurar el objeto de forma permanente, se debe copiar en un grupo que no tenga una configuración de ciclo de vida activa.

**Sintaxis**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```

**Elementos de carga útil**

El cuerpo de la solicitud debe contener un bloque XML con el esquema siguiente:

|Elemento|Tipo|Hijos|Predecesor|Restricción|
|---|---|---|---|---|
|RestoreRequest|Contenedor|Days, GlacierJobParameters|Ninguno|Ninguna|
|Days|Entero|Ninguno|RestoreRequest|Se ha especificado el tiempo de vida del objeto restaurado temporalmente. El número mínimo de días que puede existir una copia restaurada del objeto es 1. Una vez transcurrido el período de restauración, se elimina la copia temporal del objeto.|
|GlacierJobParameters|Serie|Tier|RestoreRequest|Ninguna|
|Tier|Serie|Ninguno|GlacierJobParameters|Se **debe** establecer en `Bulk`.|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Solicitud de ejemplo**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (cabeceras HMAC)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitud de ejemplo (URL prefirmado con HMAC)**

```http
POST /apiary/queenbee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&restore&x-amz-signature={signature} HTTP/1.1
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Respuesta de ejemplo**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Actualización de metadatos
{: #object-operations-metadata}

Hay dos formas de actualizar los metadatos de un objeto existente:
* Una solicitud `PUT` con los nuevos metadatos y el contenido del objeto original
* Ejecución de una solicitud `COPY` con los nuevos metadatos que especifican el objeto original como origen de la copia

Todos los metadatos claves deben tener el prefijo `x-amz-meta-`
{: tip}

### Utilización de PUT para actualizar metadatos
{: #object-operations-metadata-put}

La solicitud `PUT` necesita una copia del objeto existente como contenido que se va a sobrescribir. {: important}

**Sintaxis**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Solicitud de ejemplo**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

### Utilización de COPY para actualizar metadatos
{: #object-operations-metadata-copy}

Para obtener más información sobre cómo ejecutar una solicitud `COPY`, pulse [aquí](#object-operations-copy)

**Sintaxis**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Solicitud de ejemplo**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```
