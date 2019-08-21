---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# Seguridad de datos y cifrado
{: #security}

{{site.data.keyword.cos_full}} utiliza un enfoque innovador para almacenar de forma rentable grandes volúmenes de datos no estructurados, al tiempo que garantiza la seguridad, la disponibilidad y la fiabilidad. Esto se logra mediante el uso de algoritmos de dispersión de información (IDA) para separar los datos en "porciones" irreconocibles que se distribuyen a través de una red de centros de datos, haciendo que la transmisión y almacenamiento de datos sea inherentemente privada y segura. Ninguna copia completa de los datos reside en un solo nodo de almacenamiento y solo es necesario que haya un subconjunto de nodos para disponibles poder recuperar por completo los datos de la red.

Todos los datos de {{site.data.keyword.cos_full_notm}} se cifran en reposo. Esta tecnología cifra individualmente cada objeto utilizando claves generadas por objeto. Estas claves se almacenan de forma segura y fiable utilizando los mismos algoritmos de dispersión de información que protegen los datos del objeto utilizando una transformación de tipo todo o nada (AONT), que evita que se distribuyan datos clave si los nodos individuales o las unidades de disco duro se ven comprometidos.

Si es necesario que el usuario controle las claves de cifrado, se pueden proporcionar claves raíz [por objeto mediante SSE-C](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c) o [por grupo mediante SSE-KP](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp).

Se puede acceder al almacenamiento a través de HTTPS y los dispositivos de almacenamiento interno están certificados y se comunican entre sí mediante TLS.


## Supresión de datos
{: #security-deletion}

Después de que se supriman los datos, existen varios mecanismos que impiden la recuperación o la reconstrucción de los objetos suprimidos. La supresión de un objeto pasa por varias etapas, desde la marcación de los metadatos que indican que el objeto se ha suprimido, hasta la eliminación de las regiones de contenido y la finalización del borrado en las propias unidades hasta la sobrescritura final de los bloques que representan esas porciones de datos. En función de si el centro de datos se ha visto comprometido o si se tiene posesión de los discos físicos, el tiempo que tarda un objeto en convertirse en irrecuperable depende de la fase de la operación de supresión. Cuando se actualiza el objeto de metadatos, los clientes externos a la red del centro de datos ya no pueden leer el objeto. Cuando los dispositivos de almacenamiento han finalizado la mayoría de las porciones que representan las regiones de contenido, no se puede acceder al objeto.

## Aislamiento de arrendatarios
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} es una infraestructura compartida, una solución de almacenamiento de objetos de varios arrendatarios. Si la carga de trabajo requiere almacenamiento dedicado o aislado, consulte [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) para obtener más información.
