---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: endpoints, legacy, access points, manual failover

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

# Información adicional sobre puntos finales
{: #advanced-endpoints}

La resiliencia de un grupo se define por el punto final que se utiliza para crearlo. La resiliencia de _Varias regiones_ distribuye los datos en varias áreas metropolitanas, mientras que la resiliencia _Regional_ distribuye los datos en una sola área metropolitana. La resiliencia de _un solo centro de datos_ dispersa los datos entre varios dispositivos dentro de un solo centro de datos. Los grupos regionales y de varias regiones pueden mantener la disponibilidad durante una interrupción del sitio.

Las cargas de trabajo de cálculo coubicadas con un punto final de {{site.data.keyword.cos_short}} regional experimentarán una latencia menor y un rendimiento mayor. Para las cargas de trabajo que no están concentradas en una sola área geográfica, un punto final `geo` de varias regiones direcciona las conexiones a los centros de datos regionales más cercanos.

Cuando se utiliza un punto final de varias regiones, se puede dirigir el tráfico de entrada a un punto de acceso específico mientras se siguen distribuyendo datos en las tres regiones. Cuando se envían solicitudes a un punto de acceso individual, no hay una migración tras error automática si dicha región deja de estar disponible. Las aplicaciones que dirigen el tráfico a un punto de acceso en lugar del punto final `geo` **deben** implementar internamente la lógica de migración tras error adecuada para obtener las ventajas de disponibilidad del almacenamiento de varias regiones.
{:tip}

Algunas cargas de trabajo pueden beneficiarse de utilizar un punto final de un solo centro de datos. Los datos almacenados en un solo sitio se siguen distribuyendo entre varios dispositivos de almacenamiento físico, pero están contenidos en un solo centro de datos. Esto puede mejorar el rendimiento de los recursos de cálculo del mismo sitio, pero no mantendrá la disponibilidad en el caso de que se produzca una interrupción en el sitio. Los grupos de un solo centros de datos no proporcionan una duplicación o copia de seguridad automática en el caso de que se destruya el sitio, de modo que cualquier aplicación que utilice un solo sitio debe tener en cuenta la recuperación tras desastre en su diseño.

Todas las solicitudes deben utilizar SSL si se utiliza IAM, y el servicio rechazará cualquier solicitud de texto sin formato.

Tipos de punto final:

Los servicios de {{site.data.keyword.cloud}} están conectados a una red de tres niveles, que segmentan el tráfico público, privado y de gestión.

* Los **puntos finales privados** están disponibles para las solicitudes procedentes de clústeres de Kubernetes, servidores nativos, servidores virtuales y otros servicios de almacenamiento en la nube. Los puntos finales privados proporcionan un mejor rendimiento y no incurren en cargos para el ancho de banda de salida o de entrada, incluso si el tráfico es entre varias regiones o entre centros de datos. **Siempre que sea posible, es mejor utilizar un punto final privado.**
* Los **puntos finales públicos** aceptan solicitudes de cualquier parte y los cargos se evalúan según el ancho de banda de salida. El ancho de banda entrante es gratuito. Los puntos finales públicos se deben utilizar para el acceso que no se origina en un recurso de cálculo de nube de {{site.data.keyword.cloud_notm}}. 

Las solicitudes se deben enviar al punto final asociado con la ubicación de un grupo determinado. Si no está seguro de dónde se encuentra un grupo, hay una [extensión de la API de listado de grupos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) que devuelve la información de ubicación y de clase de almacenamiento de todos los grupos de una instancia de servicio.

A partir de diciembre de 2018, hemos actualizado nuestros puntos finales. Los puntos finales antiguos seguirán funcionando hasta nuevo aviso. Actualice las aplicaciones para que utilicen los [puntos finales nuevos](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints).
{:note}
