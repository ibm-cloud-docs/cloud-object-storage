---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: faq, questions

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

# Preguntas más frecuentes
{: #faq}

## Preguntas sobre la API
{: #faq-api}

**¿Distinguen los nombres de grupos de {{site.data.keyword.cos_full}} entre mayúsculas y minúsculas?**

Es necesario que los nombres de grupos sean direccionables a DNS, por lo que no distinguen entre mayúsculas y minúsculas.

**¿Cuál es el número máximo de caracteres que se pueden utilizar en un nombre de objeto?**

1024

**¿Cómo puedo averiguar el tamaño total de mi grupo mediante la API?**

No se puede obtener el tamaño de un grupo con una sola solicitud. Debe obtener una lista del contenido de un grupo y sumar el tamaño de cada objeto.

**¿Puedo migrar datos de AWS S3 a {{site.data.keyword.cos_full_notm}}?**

Sí, puede utilizar las herramientas existentes para leer y escribir datos en {{site.data.keyword.cos_full_notm}}. Tendrá que configurar las credenciales de HMAC para que las herramientas se autentiquen. No todas las herramientas compatibles con S3 no están soportadas actualmente. Para obtener más información, consulte [Utilización de credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).


## Preguntas sobre la oferta
{: #faq-offering}

**¿Hay un límite de 100 grupos para una cuenta?  ¿Qué ocurre si necesito más?**

Sí, 100 es el límite actual de grupo. Por lo general, los prefijos son la mejor manera de agrupar objetos en un grupo, a menos que los datos deban estar en otra región o clase de almacenamiento. Por ejemplo, para agrupar los registros de pacientes, utilizaría un prefijo por paciente. Si esta no es una solución viable, póngase en contacto con el servicio de atención al cliente.

**Si deseo almacenar mis datos mediante {{site.data.keyword.cos_full_notm}} Vault o Cold Vault, ¿necesito crear otra cuenta?**

No, las clases de almacenamiento (así como las regiones) se definen a nivel de grupo. Simplemente cree un nuevo grupo que esté establecido en la clase de almacenamiento deseada.

**Cuando cree un grupo mediante la API, ¿cómo defino la clase de almacenamiento?**

La clase de almacenamiento (por ejemplo, `us-flex`) se asigna a la variable de configuración `LocationConstraint` para dicho grupo. Esto se debe a una diferencia clave entre la forma en que AWS S3 y {{site.data.keyword.cos_full_notm}} manejan las clases de almacenamiento. {{site.data.keyword.cos_short}} define las clases de almacenamiento a nivel de grupo, mientras que AWS S3 asigna una clase de almacenamiento a un objeto individual. Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes).

**¿Se puede cambiar la clase de almacenamiento de un grupo?  Por ejemplo, si tengo datos de producción en 'standard', ¿puedo cambiarlo fácilmente por 'vault' a fines de facturación si no lo utilizo con frecuencia?**

Actualmente, para cambiar la clase de almacenamiento es necesario mover o copiar manualmente los datos de un grupo en otro grupo con la clase de almacenamiento deseada.


## Preguntas sobre rendimiento
{: #faq-performance}

**¿La coherencia de los datos en {{site.data.keyword.cos_short}} implica un impacto en el rendimiento?**

La coherencia con cualquier sistema distribuido tiene un coste, pero la eficiencia del sistema de almacenamiento distribuido de {{site.data.keyword.cos_full_notm}} es mayor y la sobrecarga es menor en comparación con los sistemas con varias copias síncronas.

**¿Hay alguna implicación en el rendimiento si mi aplicación tiene que manipular objetos de gran tamaño?**

Para la optimización del rendimiento, los objetos se pueden cargar y descargar en varias partes, en paralelo.


## Preguntas sobre cifrado
{: #faq-encryption}

**¿{{site.data.keyword.cos_short}} proporciona cifrado en reposo y en movimiento?**

Sí. Los datos en reposo se cifran con el cifrado Advanced Encryption Standard (AES) de 256 bits del lado del proveedor automático y el hash Secure Hash Algorithm (SHA)-256. Los datos en reposo se protegen utilizando Transport Layer Security/Secure Sockets Layer (TLS/SSL) o SNMPv3 con cifrado AES incorporado de portadora.

**¿Cuál es la sobrecarga de cifrado típica si un cliente desea cifrar sus datos?**

El cifrado del lado del servidor siempre está activado para los datos del cliente. En comparación con el hashing que se necesita en la autenticación S3 y la codificación de borrado, el cifrado no es una parte importante del coste de proceso de COS.

**¿{{site.data.keyword.cos_short}} cifra todos los datos?**

Sí, {{site.data.keyword.cos_short}} cifra todos los datos.

**¿Dispone {{site.data.keyword.cos_short}} de conformidad con FIPS 140-2 para los algoritmos de cifrado?**

Sí, la oferta IBM COS Federal tiene la aprobación de los controles FedRAMP Moderate Security, que requieren una configuración de FIPS validada. IBM COS Federal tiene el certificado de FIPS 140-2 de nivel 1. Para obtener más información sobre la oferta COS Federal, [póngase en contacto con nosotros](https://www.ibm.com/cloud/government) a través de nuestro sitio Federal.

**¿Se da soporte al cifrado de clave de cliente?**

Sí, el cifrado de claves de cliente recibe soporte mediante SSE-C o Key Protect.

## Preguntas generales
{: #faq-general}

**¿Cuántos objetos caben en un solo grupo?**

No hay ningún límite práctico en cuanto al número de objetos en un solo grupo.

**¿Puedo anidar grupos dentro de otro?**

No, los grupos no se pueden anidar. Si se necesita un mayor nivel de organización dentro de un grupo, se da soporte al uso de prefijos: `{puntofinal}/{nombre-grupo}/{prefijo-objeto}/{nombre-objeto}`. Tenga en cuenta que la clave del objeto mantiene la combinación `{prefijo-objeto}/{nombre-objeto}`.

**¿Qué diferencia hay entre las solicitudes de 'Clase A' y las de 'Clase B'?**

Las solicitudes de 'Clase A' son operaciones que implican modificación o listado. Esto incluye la creación de grupos, la carga o copia de objetos, la creación o modificación de configuraciones, la obtención de listas de grupos y la obtención del contenido de grupos.Las solicitudes de 'Clase B' son las relacionadas con la recuperación de objetos o de sus metadatos o configuraciones asociados desde el sistema. La supresión de grupos o de objetos del sistema no incurre en ningún cargo.

**¿Cuál es la mejor forma de estructurar los datos utilizando Object Storage de forma que pueda 'mirar' en el mismo y encontrar lo que está buscando?  Sin una estructura de directorios, si se tienen miles de archivos a un nivel resultará difícil verlos.**

Puede utilizar metadatos asociados con cada objeto para localizar los objetos que está buscando. La principal ventaja del almacenamiento de objetos son los metadatos asociados con cada objeto. Cada objeto puede tener un máximo de 4 MB de metadatos en {{site.data.keyword.cos_short}}. Cuando se descargan en una base de datos, los metadatos proporcionan excelentes prestaciones de búsqueda. Se puede guardar un gran número de pares (clave, valor) en 4 MB. También puede utilizar la búsqueda de prefijo para encontrar lo que está buscando. Por ejemplo, si utiliza grupos para separar cada los datos de cada cliente, puede utilizar prefijos dentro de los grupos para la organización. Por ejemplo: /bucket1/folder/object donde 'folder/' es el prefijo.

**¿Puede confirmar que {{site.data.keyword.cos_short}} es 'inmediatamente coherente ', en contraposición a 'posiblemente coherente'?**

{{site.data.keyword.cos_short}} es 'inmediatamente coherente' para los datos y 'posiblemente coherente' para la contabilidad de uso.


**¿Puede {{site.data.keyword.cos_short}} particionar los datos automáticamente como HDFS, de modo que pueda leer las particiones en paralelo, por ejemplo con Spark?**

{{site.data.keyword.cos_short}} da soporte a una operación GET con rango sobre el objeto, de modo que una aplicación puede realizar una operación de tipo de lectura de bandas distribuidas. La distribución se realizaría en la aplicación que se va a gestionar.
