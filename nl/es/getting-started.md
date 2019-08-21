---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Guía de aprendizaje de iniciación
{: #getting-started}

Esta guía de aprendizaje de inicio le guía por los pasos a seguir para crear grupos, cargar objetos y configurar políticas de acceso para permitir que otros usuarios trabajen con los datos.
{: shortdesc}

## Antes de empezar
{: #gs-prereqs}

Necesita:
  * Una cuenta de [{{site.data.keyword.cloud}} Platform](https://cloud.ibm.com)
  * Una [instancia de {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * Y algunos archivos en el sistema local que cargar.
{: #gs-prereqs}

 Esta guía de aprendizaje muestra a los nuevos usuarios los primeros pasos a seguir en la consola de {{site.data.keyword.cloud_notm}} Platform. Los desarrolladores que deseen comenzar a utilizar la API, deben consultar la [Guía del desarrollador](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) o la [Visión general de la API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Creación de algunos grupos para almacenar los datos
{: #gs-create-buckets}

  1. [Cuando se solicita {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) se crea una _instancia de servicio_. {{site.data.keyword.cos_full_notm}} es un sistema de varios arrendatarios, y todas las instancias de {{site.data.keyword.cos_short}} comparten una infraestructura física. Se redirige automáticamente al usuario a la instancia de servicio en la que puede empezar a crear grupos. Las instancias de {{site.data.keyword.cos_short}} se muestran bajo **Almacenamiento** en [la lista de recursos](https://cloud.ibm.com/resources).

Los términos 'instancia de recurso' e 'instancia de servicio' hacen referencia al mismo concepto, y se pueden utilizar indistintamente.
{: tip}

  1. Siga el enlace **Crear grupo** y elija un nombre exclusivo. Todos los grupos de todas las regiones de todo el mundo comparten un solo espacio de nombres. Asegúrese de que tiene los [permisos correctos](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) para crear un grupo.

  **Nota**: cuando cree grupos y añada objetos, asegúrese de no utilizar información de identificación personal (PII). PII es información que puede identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
  {: tip}

  1. Elija en primer lugar el [nivel de _resiliencia_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) que desee y luego una _ubicación_ en la que desea guardar físicamente los datos. La resiliencia se refiere al ámbito y la escala del área geográfica en la que se distribuyen los datos. La resiliencia de _Varias regiones_ distribuye los datos en varias áreas metropolitanas, mientras que la resiliencia _Regional_ distribuye los datos en una sola área metropolitana. Un _Centro de datos único_ distribuye los datos entre los dispositivos de un solo sitio.
  2. Elija la [_clase de almacenamiento_ del grupo](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes), que es un reflejo de la frecuencia con la que espera leer los datos almacenados y determina los detalles de facturación. Siga el enlace **Crear** para crear y acceder al nuevo grupo.

Los grupos constituyen una manera de organizar sus datos, pero no son la única manera. Los nombres de objeto (a menudo denominados _claves de objeto_) pueden utilizar una o más barras inclinadas para un sistema de organización de tipo de directorio. Luego utilice la parte del nombre de objeto anterior al delimitador para formar un _prefijo de objeto_, que se utiliza para mostrar una lista de los objetos relacionados en un solo grupo a través de la API.
{: tip}


## Adición de algunos objetos a los grupos
{: #gs-add-objects}

Para continuar, vaya a uno de los grupos seleccionándolo en la lista. Pulse **Añadir objetos**. Los objetos nuevos sobrescriben los objetos existentes con los mismos nombres dentro del mismo grupo. Si utiliza la consola para cargar objetos, el nombre de objeto siempre coincide con el nombre de archivo. No es necesario que haya ninguna relación entre el nombre del archivo y el nombre del objeto si utiliza la API para escribir datos. Para continuar, añada unos cuantos archivos a este grupo.

Los objetos se limitan a 200 MB si se cargan a través de la consola, a no ser que utilice el plugin de [transferencia de alta velocidad de Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload). Los objetos mayores (hasta 10 TB) también se pueden [dividir en partes y cargar en paralelo mediante la API](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects). Las claves de objeto pueden tener un máximo de 1024 caracteres de longitud, y es mejor evitar cualquier carácter que pueda ser problemático en una dirección web. Por ejemplo, `?`, `=`, `<` y otros caracteres especiales pueden ocasionar un comportamiento no deseado si no están codificados en URL.
{:tip}

## Invitación a un usuario a su cuenta para administrar sus grupos y datos
{: #gs-invite-user}

Ahora va a incorporar otro usuario y va a permitir que actúe como administrador de la instancia y de los datos almacenados en el mismo.

  1. En primer lugar, para añadir el nuevo usuario, debe abandonar la interfaz de {{site.data.keyword.cos_short}} actual e ir a la consola de IAM. Vaya al menú **Gestionar** y siga el enlace **Acceso (IAM)** > **Usuarios**. Pulse **Invitar a usuarios**.
<img alt="Invitar a usuarios de IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`Figura 1: Invitar a usuarios de IAM`
  2. Especifique la dirección de correo electrónico de un usuario que desee invitar a su organización y, a continuación, expanda la sección **Servicios** y seleccione "Recurso" en el menú **Asignar acceso a**. Ahora seleccione "Cloud Object Storage" en el menú **Servicios**.
	<img alt="Servicios de IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`Figura 2: Servicios de IAM`
  3. Ahora aparecen tres campos más: _Instancia de servicio_, _Tipo de recurso_ e _ID de recurso_. El primer campo define la instancia de {{site.data.keyword.cos_short}} a la que puede acceder el usuario. Se puede establecer de modo que otorgue el mismo nivel de acceso a todas las instancias de {{site.data.keyword.cos_short}}. Por ahora podemos dejar los demás campos en blanco. <img alt="Invitar a usuarios de IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`Figura 3: Invitar a usuarios de IAM`
  4. El recuadro de selección que hay bajo **Seleccionar roles** determina el conjunto de acciones disponibles para el usuario. Seleccione el rol de acceso de plataforma de "Administrador" para permitir que el usuario otorgue a otros [usuarios e ID de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) acceso a la instancia. Seleccione el rol de acceso al servicio "Gestor" para permitir que el usuario gestione la instancia de {{site.data.keyword.cos_short}} y cree y suprima grupos y objetos. Estas combinaciones de un _Sujeto_ (usuario), _Rol_ (Gestor) y _Recurso_ (instancia de servicio de {{site.data.keyword.cos_short}}) forman conjuntamente las [Políticas de IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam). Para obtener instrucciones más detalladas sobre los roles y las políticas, [consulte la documentación de IAM](/docs/iam?topic=iam-userroles).
<img alt="Roles de IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`Figura 4: Selección de roles de IAM`
  5. {{site.data.keyword.cloud_notm}} utiliza Cloud Foundry como plataforma de gestión de cuentas subyacente, de modo que es necesario otorgar un nivel mínimo de acceso de Cloud Foundry para que un usuario pueda acceder a la organización por primera vez.  Seleccione una organización en el menú **Organización** y, a continuación, seleccione "Auditor" de los menús **Roles organizativos** y **Roles de espacio**.  El establecimiento de permisos de Cloud Foundry permite al usuario ver los servicios disponibles en la organización, pero no modificarlos.

## Concesión de acceso a un grupo a los desarrolladores.
{: #gs-bucket-policy}

  1. Vaya al menú **Gestionar** y siga el enlace en **Acceso (IAM)** > **ID de servicio**.  Aquí puede crear un _ID de servicio_, que sirve como identidad abstracta enlazada a la cuenta. A los ID de servicio se les puede asignar claves de API y se utilizan en situaciones en las que no desea vincular la identidad de un desarrollador concreto con un proceso o componente de una aplicación. <img alt="ID de servicio de IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`Figura 5: ID de servicio de IAM`
  2. Repita el proceso anterior pero, en el paso 3, elija una instancia de servicio particular y escriba "bucket" como _Tipo de recurso_ y el CRN completo de un grupo existente como _ID de recurso_.
  3. Ahora el ID de servicio puede acceder a esa grupo en particular, y no a otros.

## Siguientes pasos
{: #gs-next-steps}

Ahora que está familiarizado con el almacenamiento de objetos a través de la consola basada en la web, quizás le interese seguir un flujo de trabajo similar desde la línea de mandatos utilizando el programa de utilidad de línea de mandatos `ibmcloud cos` para crear la instancia de servicio e interactuar con IAM, y `curl` para acceder directamente a COS. [Consulte la visión general de API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) para empezar.
