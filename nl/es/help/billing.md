---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# Facturación
{: #billing}

Encontrará información sobre precios en [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window}.

## Facturas
{: #billing-invoices}

Busque las facturas de su cuenta en **Gestionar** > **Facturación y uso** en el menú de navegación.

Cada cuenta recibe una sola factura. Si necesita facturar por separado distintos conjuntos de contenedores, es necesario crear varias cuentas.

## Precios de {{site.data.keyword.cos_full_notm}}
{: #billing-pricing}

Los costes de almacenamiento de {{site.data.keyword.cos_full}} se determinan por el volumen total de los datos que se almacenan, la cantidad de ancho de banda de salida público utilizado y el número total de solicitudes operativas procesadas por el sistema.

Las ofertas de la infraestructura están conectadas a una red de tres niveles, que segmentan el tráfico público, privado y de gestión. Los servicios de la infraestructura pueden transferir datos entre sí a través de la red privada sin coste alguno. Las ofertas de la infraestructura (como servidores nativos, servidores virtuales y almacenamiento en la nube) se conectan a otras aplicaciones y servicios en el catálogo de {{site.data.keyword.cloud_notm}} Platform (como servicios de Watson y tiempos de ejecución de Cloud Foundry) a través de la red pública, por lo que la transferencia de datos entre estos dos tipos de ofertas se mide y se carga según la tasa de ancho de banda de red pública estándar.
{: tip}

## Clases de solicitudes
{: #billing-request-classes}

Las solicitudes de 'Clase A' implican modificación o listado. Esta categoría incluye la creación de grupos, la carga o copia de objetos, la creación o modificación de configuraciones, la obtención de listas de grupos y la obtención del contenido de grupos.

Las solicitudes de 'Clase B' están relacionadas con la recuperación de objetos o de sus metadatos asociados o a las configuraciones desde el sistema.

La supresión de grupos o de objetos del sistema no incurre en ningún cargo.

| Clase | Solicitudes | Ejemplos |
|--- |--- |--- |
| Clase A | Solicitudes PUT, COPY y POST, así como solicitudes GET utilizadas para obtener una lista de grupos y objetos | Creación de grupos, carga o copia de o objetos, listado de grupos, listado del contenido de grupos, establecimiento de ACL y establecimiento de configuraciones de CORS |
| Clase B | Solicitudes GET (sin incluir listado), HEAD y OPTIONS | Recuperación de objetos y metadatos |

## Transferencias de Aspera
{: #billing-aspera}

La [transferencia de alta velocidad de Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) incurre en cargos extra de salida. Para obtener más información, consulte la [página de precios](https://www.ibm.com/cloud/object-storage#s3api).

## Clases de almacenamiento
{: #billing-storage-classes}

No es necesario acceder con frecuencia a todos los datos que se almacenan; a algunos datos archivados rara vez se accede, si es que se accede alguna vez. En el caso de cargas de trabajo menos activas, se pueden crear grupos en otra clase de almacenamiento y los objetos almacenados en estos grupos incurren en cargos según una planificación distinta a la del almacenamiento estándar.

Hay cuatro clases:

*  **Estándar (Standard)**: se utiliza para cargas de trabajo activas; no hay ningún cargo para los datos recuperados (aparte del coste de la propia solicitud operativa).
*  **Caja fuerte (Vault)** se utiliza para las cargas de trabajo frías en las que se accede a los datos menos de una vez al mes; se aplica un cargo de recuperación adicional ($/GB) cada vez que se leen datos. El servicio incluye un umbral mínimo de tamaño de objeto y de periodo de almacenamiento coherente con el uso previsto de este servicio de datos en frío menos activos.
*  **Caja fuerte fría (Cold Vault)**: se utiliza para cargas de trabajo frías donde se accede a los datos cada 90 días o menos; se aplica un cargo de recuperación adicional mayor ($/GB) cada vez que se leen datos. El servicio incluye un umbral mínimo mayor de tamaño de objeto y de periodo de almacenamiento coherente con el uso previsto de este servicio de datos en frío menos activos.
*  **Flex**: se utiliza para cargas de trabajo dinámicas, para las que es más difícil predecir patrones de acceso. En función del uso, si los costes y los cargos de recuperación superan un valor límite, se eliminan los cargos por recuperación y se aplica en su lugar un nuevo cargo por capacidad. Si no se accede a los datos con frecuencia, es más rentable que el almacenamiento Estándar, y, si los patrones de uso de acceso se vuelven más activos, es más rentable que el almacenamiento de tipo Caja fuerte o Caja fuerte fría. Flex no requiere un tamaño de objeto mínimo o un período de almacenamiento.

Para obtener más información sobre los precios, consulte [la tabla de precios en ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Para obtener más información sobre cómo crear grupos con diferentes clases de almacenamiento, revise la [Consulta de API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).
