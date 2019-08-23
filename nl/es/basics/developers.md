---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# Para los desarrolladores
{: #gs-dev}
En primer lugar, asegúrese de que ha instalado la [CLI de {{site.data.keyword.cloud}} Platform](https://cloud.ibm.com/docs/cli/index.html) e [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) .

## Suministro de una instancia de {{site.data.keyword.cos_full_notm}}
{: #gs-dev-provision}

  1. En primer lugar, asegúrese de que tiene una clave de API. Puede obtenerla de [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys).
  2. Inicie una sesión en {{site.data.keyword.cloud_notm}} Platform mediante la CLI. También puede guardar la clave de API en un archivo o establecerla como variable de entorno.

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. A continuación, suministre una instancia de {{site.data.keyword.cos_full_notm}} especificando el nombre de la instancia, el ID y el plan que desee (lite o estándar). Esto nos llevará a CRN. Si tiene una cuenta actualizada, especifique el plan `Estándar`. De lo contrario, especifique `Lite`.

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

La [Guía de iniciación](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) le muestra los pasos básicos para crear grupos y objetos, así como para invitar a usuarios y crear políticas. Encontrará una lista de los mandatos 'curl' básicos [aquí](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl).

Encontrará más información sobre el uso de la CLI de {{site.data.keyword.cloud_notm}} para crear aplicaciones, gestionar clústeres de Kubernetes y más [en la documentación](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli).


## Utilización de la API
{: #gs-dev-api}

Para gestionar los datos almacenados en {{site.data.keyword.cos_short}}, puede utilizar herramientas compatibles con la API S3, como la [CLI de AWS](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli), con [credenciales HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) para la compatibilidad. Puesto que resulta relativamente fácil trabajar con señales de IAM, `curl` es una buena opción para las pruebas básicas y la interacción con el almacenamiento. Encontrará más información en la [consulta de `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) y en la [documentación de consulta de API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Utilización de bibliotecas y SDK
{: #gs-dev-sdk}
Hay SDK de IBM COS disponibles para [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) y [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node). Se trata de versiones derivadas de los SDK de AWS S3 que se han modificado para dar soporte a la [autenticación basada en señales de IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) y a [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption). 

## Creación de aplicaciones en IBM Cloud
{: #gs-dev-apps}
{{site.data.keyword.cloud}} ofrece a los desarrolladores flexibilidad para elegir las opciones de arquitectura y de despliegue adecuadas para una determinada aplicación. Puede ejecutar su código en [entornos nativos](https://cloud.ibm.com/catalog/infrastructure/bare-metal), en [máquinas virtuales](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), mediante una [infraestructura sin servidor](https://cloud.ibm.com/openwhisk), en [contenedores](https://cloud.ibm.com/kubernetes/catalog/cluster) o mediante [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs). 

[Cloud Native Computing Foundation](https://www.cncf.io) ha desarrollado y "graduado" recientemente la infraestructura de orquestación de contenedores [Kubernetes](https://kubernetes.io), y constituye la base del servicio Kubernetes de {{site.data.keyword.cloud}}. Los desarrolladores que deseen utilizar almacenamiento de objetos para almacenamiento persistente en sus aplicaciones Kubernetes pueden obtener más información en los enlaces siguientes:

 * [Elección de una solución de almacenamiento](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [Tabla de comparación entre las opciones de almacenamiento persistente](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [Página principal de COS](/docs/containers?topic=containers-object_storage)
 * [Instalación de COS](/docs/containers?topic=containers-object_storage#install_cos)
 * [Creación de instancia de servicio COS](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [Creación de un secreto de COS](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [Cómo decidir la configuración](/docs/containers?topic=containers-object_storage#configure_cos)
 * [Suministro de COS](/docs/containers?topic=containers-object_storage#add_cos)
 * [Información sobre copia de seguridad y restauración](/docs/containers?topic=containers-object_storage#backup_restore)
 * [Consulta de clase de almacenamiento](/docs/containers?topic=containers-object_storage#storageclass_reference)


