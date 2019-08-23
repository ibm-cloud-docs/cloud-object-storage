---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: administrator, storage, iam, access

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

# Para los administradores
{: #administrators}

Los administradores de almacenamiento y de sistemas que necesitan configurar el almacenamiento de objetos y gestionar el acceso a los datos pueden beneficiarse de IBM Cloud Identity and Access Management (IAM) para gestionar usuarios, crear y rotar claves de API y otorgar roles a usuarios y servicios. Si todavía no lo ha hecho, consulte la [guía de aprendizaje de iniciación](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para familiarizarse con los conceptos principales sobre grupos, objetos y usuarios.

## Configuración del almacenamiento
{: #administrators-setup}

En primer lugar, es necesario tener al menos una instancia del recurso de almacenamiento de objetos y algunos grupos en los que almacenar datos. Cuando planifique estos grupos, piense en cómo desea segmentar el acceso a los datos, dónde desea que residan los datos físicamente y la frecuencia con la que se va a acceder a los datos.

### Segmentación del acceso
{: #administrators-access}

Hay dos niveles en los que puede segmentar el acceso: el nivel de instancia de recurso y el nivel de grupo. 

Tal vez desee asegurarse de que un equipo de desarrollo solo pueda acceder a las instancias de almacenamiento de objetos con las que están trabajando y no a las que utilizan otros equipos. O quizás desee asegurarse de que solo el software que está desarrollando el equipo pueda editar los datos almacenados, por lo que desea que los desarrolladores con acceso a la plataforma de nube solo puedan leer los datos para llevar a cabo la resolución de problemas. Estos son ejemplos de políticas de nivel de servicio.

Si el equipo de desarrollo, o cualquier usuario individual, que tiene acceso de visor a una instancia de almacenamiento debe poder editar directamente datos en uno o más grupos, puede utilizar políticas de nivel de grupo para elevar el nivel de acceso otorgado a los usuarios dentro de su cuenta. Por ejemplo, es posible que un usuario no pueda crear nuevos grupos, pero puede crear y suprimir objetos dentro de grupos existentes.

## Gestión del acceso
{: #administrators-manage-access}

El IAM se basa en un concepto fundamental: se otorga un _rol_ a un _sujeto_ sobre un _recurso_.

Hay dos tipos básicos de sujetos: un _usuario_ y un _ID de servicio_.

Existe otro: _credencial de servicio_. Una credencial de servicio es una colección de información importante necesaria para conectarse a una instancia de {{site.data.keyword.cos_full}}. Esto incluye como mínimo un identificador para la instancia de {{site.data.keyword.cos_full_notm}} (es decir, el ID de instancia de recurso), los puntos finales de servicio/autorización y un medio de asociar el sujeto con una clave de API (es decir, un ID de servicio). Cuando cree la credencial de servicio, tiene la opción de asociarla con un ID de servicio existente o de crear un nuevo ID de servicio.

Por lo tanto, si desea permitir que el equipo de desarrollo utilice la consola para ver instancias de almacenamiento de objetos y clústeres de Kubernetes, necesitarán el rol de `Visor` sobre los recursos de almacenamiento de objetos y el de `Administrador` sobre el servicio de contenedor. Tenga en cuenta que el rol de `Visor` solo permite al usuario ver que existe la instancia y ver las credenciales existentes, **no** ver grupos y objetos. Cuando se crean credenciales de servicio, se han asociado con un ID de servicio. Este ID de servicio debería tener el rol de `Gestor` o `Escritor` sobre la instancia para poder crear y destruir grupos y objetos.

Para obtener más información sobre los roles y permisos de IAM, consulte [Visión general de IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview).
