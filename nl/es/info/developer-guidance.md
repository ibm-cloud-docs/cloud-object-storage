---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# Guía del desarrollador
{: #dev-guide}

## Ajuste de los valores de cifrado
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} da soporte a varios valores de cifrado para cifrar los datos en tránsito. No todos los valores de cifrado ofrecen el mismo nivel de rendimiento. Se ha demostrado que la negociación de uno de los valores `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_128_CBC_SHA` ofrece el mismo nivel de rendimiento que ningún TLS entre el cliente y el sistema {{site.data.keyword.cos_full_notm}}.

## Utilización de cargas de varias partes
{: #dev-guide-multipart}

Cuando se trabaja con objetos grandes, se recomienda utilizar operaciones de carga de varias partes para escribir objetos en {{site.data.keyword.cos_full_notm}}. Una carga de un solo objeto se puede realizar como un conjunto de partes, y dichas partes se pueden cargar de forma independiente en cualquier orden o en paralelo. Una vez finalizada la carga, {{site.data.keyword.cos_short}} presenta todas las partes como un solo objeto. Esto ofrece varias ventajas: las interrupciones de red no dan lugar a fallos en cargas grandes, las cargas se pueden poner en pausa y reiniciar a lo largo del tiempo, y los objetos pueden cargarse a medida que se crean.

Las cargas de varias partes solo están disponibles para los objetos de más de 5 MB. Para los objetos de menos de 50 GB, se recomienda un tamaño de parte de entre 20 MB y 100 MB para obtener un rendimiento óptimo. En el caso de objetos más grandes, el tamaño de las partes se puede aumentar sin
que ello afecte al rendimiento de forma significativa.

El uso de más de 500 partes reduce la eficacia de {{site.data.keyword.cos_short}} y se debe evitar siempre que sea posible.

Debido a la complejidad adicional que implica, se recomienda que los desarrolladores utilicen las bibliotecas de API S3 que proporcionan soporte de carga de varias partes.

Las cargas de varias partes incompletas persisten hasta que se suprime el objeto o se termina de forma anómala la carga de varias partes con `AbortIncompleteMultipartUpload`. Si no se termina de forma anómala una carga de varias partes incompleta, la carga parcial continúa utilizando recursos. Las interfaces se deben diseñar teniendo esto en cuenta, de modo que limpien las cargas de varias partes incompletas.

## Utilización de kits de desarrollo de software
{: #dev-guide-sdks}

No es obligatorio utilizar los SDK de API de S3 publicados; un software personalizado puede aprovechar la API para integrarse directamente con {{site.data.keyword.cos_short}}. Sin embargo, el uso de bibliotecas de API de S3 publicadas ofrece ventajas como autenticación y generación de firmas, lógica de reintentos automáticos tras errores `5xx` y generación de url prefirmados. Se debe tener cuidado al escribir software que utilice la API directamente para manejar errores transitorios, por ejemplo proporcionando reintentos con un retroceso exponencial cuando se reciban errores `503`.

## Paginación
{: #dev-guide-pagination}

Cuando se tiene que gestionar un gran número de objetos en un grupo, las aplicaciones web pueden empezar a sufrir una degradación del rendimiento. Muchas aplicaciones emplean una técnica llamada **paginación** (*el proceso de dividir un conjunto de registros de gran tamaño en páginas menores*). Casi todas las plataformas de desarrollo proporcionan objetos o métodos para llevar a cabo la paginación mediante funciones integradas o mediante bibliotecas de terceros.

Los SDK de {{site.data.keyword.cos_short}} proporcionan soporte para la paginación a través de un método que obtiene una lista de los objetos contenidos en un grupo especificado. Este método proporciona varios parámetros que lo convierten en extremadamente útil cuando se intenta dividir un conjunto de resultados grande.

### Uso básico
{: #dev-guide-pagination-basics}
El concepto básico subyacente al método de obtención de una lista de objetos implica definir el número máximo de claves (`MaxKeys`) que se deben devolver en la respuesta. La respuesta también incluye un valor `booleano` (`IsTruncated`) que indica si hay más resultados disponibles y un valor de tipo `serie` denominado `NextContinuationToken`. Si se establece la señal de continuación en las solicitudes de seguimiento, se devuelve el siguiente lote de objetos hasta que no haya más resultados disponibles.

#### Parámetros comunes
{: #dev-guide-pagination-params}

|Parámetro|Descripción|
|---|---|
|`ContinuationToken`|Establece la señal para especificar el siguiente lote de registros|
|`MaxKeys`|Establece el número máximo de claves que se deben incluir en la respuesta|
|`Prefix`|Restringe la respuesta a las claves que empiezan por el prefijo especificado|
|`StartAfter`|Establece dónde debe comenzar la lista de objetos en función de la clave|

### Utilización de Java
{: #dev-guide-pagination-java}

El SDK de {{site.data.keyword.cos_full}} para Java ofrece el método [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} que permite devolver la lista de objetos del tamaño deseado. [Aquí](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2) encontrará un ejemplo de código completo.

### Utilización de Python
{: #dev-guide-pagination-python}

El SDK de {{site.data.keyword.cos_full}} para Python ofrece el método [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} que permite devolver la lista de objetos del tamaño deseado. [Aquí](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2) encontrará un ejemplo de código completo.
