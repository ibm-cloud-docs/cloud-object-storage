---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Utilización de la transferencia de alta velocidad Aspera
{: #aspera}

La transferencia de alta velocidad Aspera omite las limitaciones de las transferencias FTP y HTTP tradicionales a fin de mejorar el rendimiento de la transferencia de datos bajo la mayoría de las condiciones, especialmente en redes con latencia alta y posibilidad de pérdida de paquetes. En lugar de `PUT` de HTTP estándar, la transferencia de alta velocidad de Aspera carga el objeto mediante el [protocolo FASP](https://asperasoft.com/technology/transport/fasp/). El uso de las transferencia de alta velocidad de Aspera para cargas y descargas ofrece las siguiente ventajas:

- Velocidades de transferencia más rápidas
- Transferencia de cargas de objetos grandes de más de 200 MB en la consola y de 1 GB si se utiliza SDK o la biblioteca
- Carga de carpetas enteras de cualquier tipo de datos, incluidos archivos multimedia, imágenes de disco y otros datos estructurados o no estructurados
- Personalización de velocidades de transferencias y de preferencias predeterminadas
- Las transferencias se puede ver, poner en pausa y reanudar o cancelar de forma independiente

La transferencia de alta velocidad de Aspera está disponible en la {{site.data.keyword.cloud_notm}}[consola](#aspera-console) y también se puede utilizar mediante programación utilizando un [SDK](#aspera-sdk). 

La transferencia de alta velocidad de Aspera solo está disponible en determinadas regiones. Consulte [Servicios integrados](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability) para obtener más información.
{:tip}

## Utilización de la consola
{: #aspera-console}

Cuando se crea un grupo en una [región soportada](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability), tiene la opción de seleccionar la transferencia de alta velocidad de Aspera para cargar archivos o carpetas. Una vez que intenta cargar un objeto, se le solicita que instale el cliente de Aspera Connect.

### Instalación de Aspera Connect
{: #aspera-install}

1. Seleccione instalar el cliente de **Aspera Connect**.
2. Siga las instrucciones de instalación en función del sistema operativo y del navegador.
3. Reanude la carga del archivo o de la carpeta.

El plugin Aspera Connect también se puede instalar directamente desde el [sitio web de Aspera](https://downloads.asperasoft.com/connect2/). Para obtener ayuda para solucionar problemas con el plugin de Aspera Connect, [consulte la documentación](https://downloads.asperasoft.com/en/documentation/8).

Una vez instalado el plugin, tiene la opción de establecer la transferencia de alta velocidad de Aspera como valor predeterminado para cualquier carga en el grupo de destino que utiliza el mismo navegador. Seleccione **Recordar mis preferencias de navegador**. Las opciones también están disponibles en la página de configuración del grupo bajo **Opciones de transferencia**. Estas opciones le permiten elegir entre Estándar y Alta Velocidad como transporte predeterminado para cargas y descargas.

Generalmente, la utilización de la consola basada en web de IBM Cloud Object Storage no es la forma más común de utilizar {{site.data.keyword.cos_short}}. La opción de transferencia estándar limita el tamaño de los objetos a 200 MB y el nombre de archivo y la clave serán idénticos. La transferencia de alta velocidad de Aspera proporciona soporte para tamaños de objeto mayores y un rendimiento mejorado (en función de los factores de red).

### Estado de la transferencia
{: #aspera-console-transfer-status}

**Activa:** una vez que se inicia una transferencia, el estado de transferencia se muestra como activo. Mientras la transferencia está activa, puede poner en pausa, reanudar o cancelar una transferencia activa. 

**Completada:** después de completar la transferencia, en el separador Completada se muestra información sobre esta y sobre todas las transferencias de esta sesión. Puede borrar esta información. Solo verá información sobre las transferencias completadas en la sesión actual.

**Preferencias:** puede establecer el valor predeterminado para cargas y/o descargas en Alta velocidad.

Las descargas realizadas mediante la transferencia de alta velocidad de Aspera generan cargos de salida. Para obtener más información, consulte la [página de precios](https://www.ibm.com/cloud/object-storage).
{:tip}

**Preferencias avanzadas:** puede definir el ancho de banda para las cargas y descargas.

----

## Utilización de bibliotecas y SDK
{: #aspera-sdk}

El SDK de transferencia de alta velocidad de Aspera proporciona la posibilidad de iniciar una transferencia de alta velocidad dentro de las aplicaciones personalizadas mediante Java o Python.

### Cuándo utilizar la transferencia de alta velocidad de Aspera
{: #aspera-guidance}

El protocolo FASP que utiliza la transferencia de alta velocidad de Aspera no resulta adecuado para todas las transferencias de datos a y desde COS. En concreto, las transferencias que utilizan la transferencia de alta velocidad de Aspera deberían:

1. Utilizar siempre varias sesiones: al menos dos sesiones paralelas utilizarán mejor las prestaciones de las transferencias de alta velocidad de Aspera. Consulte la guía específica correspondiente a [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) y [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
2. La transferencia de alta velocidad de Aspera resulta ideal para archivos grandes, y cualquier archivo o directorio que contenga una cantidad total de datos inferior a 1 GB debería transferir el objeto en varias partes utilizando las clases estándares de Manager Transfer. Las transferencias de alta velocidad de Aspera requieren un tiempo hasta el primer byte ("time-to-first-byte") más largo que las transferencias HTTP normales. La creación de instancias de muchos objetos de Aspera Transfer Manager para gestionar las transferencias de archivos pequeños individuales puede dar como resultado un rendimiento bajo en comparación con las solicitudes HTTP básicas, por lo que es mejor crear una instancia de un solo cliente para cargar un directorio de archivos más pequeños.
3. La transferencia de alta velocidad de Aspera se ha diseñado en parte para mejorar el rendimiento en entornos de red con gran cantidad de pérdida de paquetes, aumentando el rendimiento del protocolo sobre grandes distancias y en redes públicas de área amplia. La transferencia de alta velocidad de Aspera no se debe utilizar para transferencias dentro de una región o centro de datos.

El SDK de transferencia de alta velocidad de Aspera es de origen cerrado y, por lo tanto, una dependencia opcional para el SDK de COS (que utiliza una licencia de Apache). 
{:tip}

#### Interactuación de la transferencia de alta velocidad de COS/Aspera
{: #aspera-packaging}

La imagen siguiente muestra una visión general a nivel global del modo en que el SDK de COS interactúa con la biblioteca de transferencia de alta velocidad de Aspera para proporcionar funcionalidad.

<img alt="SDK de transferencia de alta velocidad de COS/Aspera" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="Figura 1: SDK de transferencia de alta velocidad de COS/Aspera." caption-side="bottom"} 

### Plataformas soportadas
{: #aspera-sdk-platforms}

| SO                     | Versión   | Arquitectura | Versión Java probada | Versión Python probada |
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64 bits       | 6 y post. | 2.7, 3.6       |
| Mac OS X               | 10.13     | 64 bits       | 6 y post. | 2.7, 3.6       |
| Microsoft&reg; Windows | 10        | 64 bits       | 6 y post. | 2.7, 3.6       |

Cada sesión de transferencia de alta velocidad de Aspera genera un proceso `ascp` individual que se ejecuta en la máquina cliente para realizar la transferencia. Asegúrese de que su entorno permita ejecutar dicho proceso.
{:tip}

**Limitaciones adicionales**

* No se da soporte a los binarios de 32 bits
* El soporte de Windows requiere Windows 10
* El soporte de Linux se limita a Ubuntu (probado sobre 18.04 LTS)
* Los clientes de Aspera Transfer Manager se deben crear mediante claves de API de IAM, no con credenciales de HMAC.

### Obtención del SDK mediante Java
{: #aspera-sdk-java} 
{: java}

La mejor manera de utilizar {{site.data.keyword.cos_full_notm}} y el SDK de Java de transferencia de alta velocidad de Aspera consiste en utilizar Maven para gestionar dependencias. Si no está familiarizado con Maven, consulte la guía [Maven en 5 minutos](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window}.
{: java}

Maven utiliza un archivo llamado `pom.xml` para especificar las bibliotecas (y sus versiones) necesarias para un proyecto Java. A continuación se muestra un archivo `pom.xml` de ejemplo para utilizar {{site.data.keyword.cos_full_notm}} y el SDK de Java de transferencia de alta velocidad de Aspera.
{: java}

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera</artifactId>
            <version>0.1.163682</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}
{: java}

Encontrará ejemplos de cómo iniciar transferencias de alta velocidad de Aspera con Java en la sección sobre [Utilización de la transferencia de alta velocidad de Aspera](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera).
{: java}

### Obtención del SDK mediante Python
{: #aspera-sdk-python} 
{: python}

Los SDK de {{site.data.keyword.cos_full_notm}} y de transferencia de alta velocidad de Aspera están disponibles en el repositorio de software de Python Package Index (PyPI). 
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

Encontrará ejemplos de cómo iniciar transferencias de Aspera con Python en la sección sobre [Utilización de la transferencia de alta velocidad de Aspera](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
{: python}
