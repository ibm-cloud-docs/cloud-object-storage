---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-19"

keywords: sdks, overview

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
{:go: .ph data-hd-programlang='go'}

# Acerca de los SDK de IBM COS
{: #sdk-about}

IBM COS proporciona SDK para Java, Python, NodeJS e Go. Estos SDK se basan en los SDK oficiales de la API AWS S3, pero se han modificado para que utilicen características de IBM Cloud, como IAM, Key Protect, Immutable Object Storage y otros.

| Característica                     | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| Soporte de claves de API de IAM         | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |
| Cargas de varias partes gestionadas   | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |
| Descargas de varias partes gestionadas | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Lista ampliada de grupos     |                                                   |                                                   |                                                   |                                                   |                                                   |
| Lista de objetos de la versión 2    | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Key Protect                 | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |
| SSE-C                       | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Reglas de archivado               | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Políticas de retención          | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Transferencia de alta velocidad de Aspera  | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) | ![Icono de marca de comprobación](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## Soporte de claves de API de IAM
{: #sdk-about-iam}
Permite crear clientes con una clave de API en lugar de un par de clave de acceso y clave secreta.  La gestión de señales se gestiona automáticamente, y las señales se renuevan automáticamente durante las operaciones de larga ejecución.
## Cargas de varias partes gestionadas
Mediante la clase `TransferManager`, el SDK gestiona toda la lógica necesaria para cargar objetos en varias partes.
## Descargas de varias partes gestionadas
Mediante la clase `TransferManager`, el SDK gestiona toda la lógica necesaria para descargar objetos en varias partes.
## Lista ampliada de grupos
Es una extensión de la API S3 que devuelve una lista de grupos con códigos de suministro (una combinación de la ubicación del grupo y la clase de almacenamiento, que se devuelve como `LocationConstraint`) para los grupos cuando se obtiene una lista.  Resulta útil para localizar un grupo, ya que todos los grupos de una instancia de servicio se muestran en la lista, independientemente del punto final utilizado.
## Lista de objetos de la versión 2
La lista de la versión 2 permite acotar de forma más precisa el listado de objetos.
## Key Protect
Key Protect es un servicio de IBM Cloud que gestiona las claves de cifrado, y es un parámetro opcional durante la creación de un grupo.
## SSE-C                      
## Reglas de archivado              
## Políticas de retención         
## Transferencia de alta velocidad de Aspera 
