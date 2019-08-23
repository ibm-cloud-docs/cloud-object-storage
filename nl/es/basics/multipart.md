---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: big data, multipart, multiple parts, transfer

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
{:S3cmd: .ph data-hd-programlang='S3cmd'}

# Almacenamiento de objetos grandes
{: #large-objects}

{{site.data.keyword.cos_full}} admite objetos individuales de hasta 10 TB cuando se utiliza la carga de varias partes. Los objetos grandes también se pueden cargar [mediante la consola con la transferencia de alta velocidad de Aspera habilitada](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera). En la mayoría de los casos, la transferencia de alta velocidad de Aspera aumenta significativamente el rendimiento de la transferencia de datos, especialmente entre distancias largas o bajo condiciones de red inestables.

## Carga de objetos en varias partes
{: #large-objects-multipart}

Se recomienda utilizar operaciones de carga de varias partes para escribir objetos grandes en {{site.data.keyword.cos_short}}. Una carga de un solo objeto se realiza como un conjunto de partes, y dichas partes se pueden cargar de forma independiente en cualquier orden o en paralelo. Una vez finalizada la carga, {{site.data.keyword.cos_short}} presenta todas las partes como un solo objeto. Esto ofrece varias ventajas: las interrupciones de red no dan lugar a fallos en cargas grandes, las cargas se pueden poner en pausa y reiniciar a lo largo del tiempo, y los objetos pueden cargarse a medida que se crean.

Las cargas de varias partes solo están disponibles para los objetos de más de 5 MB. Para los objetos de menos de 50 GB, se recomienda un tamaño de parte de entre 20 MB y 100 MB para obtener un rendimiento óptimo. En el caso de objetos más grandes, el tamaño de las partes se puede aumentar sin que ello afecte al rendimiento de forma significativa. Las cargas de varias partes se limitan a 10.000 partes de 5 GB cada una hasta un tamaño máximo de objeto de 10 TB.


Debido a la complejidad que implica la gestión y la optimización de las cargas en paralelo, muchos desarrolladores utilizan bibliotecas que proporcionan soporte de carga de varias partes.

La mayoría de las herramientas, como las CLI o la consola de IBM Cloud, así como la mayoría de bibliotecas y SDK compatibles, transfieren automáticamente objetos en cargas de varias partes.

## Utilización de API REST o SDK
{: #large-objects-multipart-api} 

Las cargas de varias partes incompletas persisten hasta que se suprime el objeto o se termina de forma anómala la carga de varias partes. Si no se termina de forma anómala una carga de varias partes incompleta, la carga parcial continúa utilizando recursos. Las interfaces se deben diseñar teniendo esto en cuenta, de modo que limpien las cargas de varias partes incompletas.
{:tip}

El proceso de carga de un objeto en varias partes consta de tres fases:

1. La carga se inicia y se crea un `UploadId`.
2. Las partes individuales se cargan especificando sus números de pieza secuenciales y el `UploadId` correspondiente al objeto.
3. Cuando todas las partes se han terminado de cargar, la carga se completa enviando una solicitud con el `UploadId` y un bloque XML que enumera cada número de pieza y su respectivo valor de `Etag`.

Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

### Inicio de una carga de varias partes
{: #large-objects-multipart-api-initiate} 
{: http}

Una operación `POST` emitida a un objeto con el parámetro de consulta `upload` crea un nuevo valor de `UploadId`, al que cada parte del objeto que se carga hace referencia.
{: http}

**Sintaxis**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

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
{: codeblock}
{: http}

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```
{: codeblock}
{: http}

----

### Carga de una parte
{: #large-objects-multipart-api-upload-part} 
{: http}

Una solicitud `PUT` que se emite a un objeto con los parámetros de consulta `partNumber` y `uploadId` cargará una parte de un objeto. Las partes se pueden cargar en serie o en paralelo, pero se deben numerar en orden.
{: http}

**Sintaxis**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

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
{: codeblock}
{: http}

### Finalización de una carga de varias partes
{: #large-objects-multipart-api-complete} 
{: http}

Una solicitud `POST` que se emite a un objeto con el parámetro de consulta `uploadId` y el bloque XML adecuado en el cuerpo completarán una carga de varias partes.
{: http}

**Sintaxis**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: http}

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
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

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
{: codeblock}
{: http}

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```
{: codeblock}
{: http}


### Terminación anómala de cargas de varias partes incompletas
{: #large-objects-multipart-api-abort} 
{: http}

Una solicitud `DELETE` emitida a un objeto con el parámetro de consulta `uploadId` suprime todas las partes inacabadas de una carga de varias partes.
{: http}
**Sintaxis**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}
{: http}

### Utilización de S3cmd (CLI)
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){:new_window} es una herramienta de línea de mandatos y cliente de Linux y Mac gratuitos para cargar, recuperar y gestionar datos en los proveedores de servicios de almacenamiento en la nube que utilizan el protocolo S3. Está diseñada para ayudar a los usuarios que están familiarizados con programas de línea de mandatos y resulta ideal para scripts por lotes y para copias de seguridad automatizadas. S3cmd está escrito en Python. Es un proyecto de código abierto disponible bajo GNU Public License v2 (GPLv2) y es gratuito tanto para uso comercial como para uso privado.
{: S3cmd}

S3cmd requiere Python 2.6 o más reciente y es compatible con Python 3. La forma más fácil de instalar S3cmd es con el Python Package Index (PyPi).
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

Una vez instalado el paquete, tome el archivo de configuración de ejemplo de {{site.data.keyword.cos_full}} [aquí](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window} y actualícelo con las credenciales de Cloud Object Storage (S3):
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

Las cuatro líneas que se deben actualizar son
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
Esto es igual tanto si utiliza el archivo de ejemplo como si utiliza el que se genera ejecutando: `s3cmd --configure`.
{: S3cmd}

Una vez que se han actualizado estas líneas con los detalles de COS del Portal de clientes, puede probar la conexión emitiendo el mandato `s3cmd ls`, que mostrará todos los grupos de la cuenta.
{: S3cmd}

```
$ s3cmd ls
2017-02-03 14:52  s3://backuptest
2017-02-06 15:04  s3://coldbackups
2017-02-03 21:23  s3://largebackup
2017-02-07 17:44  s3://winbackup
```
{: codeblock}
{: S3cmd}

Encontrará la lista completa de opciones y mandatos, junto con información básica sobre uso, en el sitio [s3tools](https://s3tools.org/usage){:new_window}.
{: S3cmd}

### Carga de varias partes con S3cmd
{: #large-objects-s3cmd-upload} 
{: S3cmd}

Un mandato `put` ejecutará automáticamente una carga de varias partes cuando se intente cargar un archivo mayor que el umbral especificado.
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

La opción `--multipart-chunk-size-mb` determina el umbral:
{: S3cmd}

```
--multipart-chunk-size-mb=SIZE
    Tamaño de cada porción de una carga de varias partes. Los archivos mayores que
    SIZE se cargan automáticamente como archivos de varias partes; los archivos
    menores se cargan con el método
    tradicional. SIZE está en megabytes; el tamaño predeterminado
    de porción es 15 MB, el tamaño mínimo permitido de porción es 5 MB,
    el tamaño máximo es 5 GB.
```
{: codeblock}
{: S3cmd}

Ejemplo:
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

Salida:
{: S3cmd}

```
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 1 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  1731.92 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 2 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2001.14 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 3 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2000.28 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 4 of 4, 4MB] [1 of 1]
 4973645 of 4973645   100% in    2s  1823.51 kB/s  done
 ```
{: codeblock}
{: S3cmd}

### Utilización del SDK de Java
{: #large-objects-java} 
{: java}

El SDK de Java proporciona dos formas de ejecutar cargas de objetos grandes:
{: java}

* [Cargas de varias partes](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Utilización del SDK de Python
{: #large-objects-python} 
{: python}

El SDK de Python proporciona dos formas de ejecutar cargas de objetos grandes:
{: python}

* [Cargas de varias partes](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Utilización del SDK de Node.js
{: #large-objects-node} 
{: javascript}

El SDK de Node.js proporciona una sola forma de ejecutar cargas de objetos grandes:
{: javascript}

* [Cargas de varias partes](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
