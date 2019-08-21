---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# Visión general de IAM
{: #iam-overview}

{{site.data.keyword.cloud}} Identity & Access Management le permite autenticar de forma segura los usuarios y controlar el acceso a todos los recursos de la nube de forma coherente en {{site.data.keyword.cloud_notm}} Platform. Consulte la [Guía de aprendizaje de iniciación](/docs/iam?topic=iam-getstarted#getstarted) para obtener más información.

## Gestión de identidades
{: #iam-overview-identity}

La gestión de identidades incluye la interacción de usuarios, servicios y recursos. Los usuarios se identifican mediante su IBMid. Los servicios se identifican mediante sus ID de servicio. Y los recursos se identifican y se gestionan mediante los CRN.

El servicio de señal de {{site.data.keyword.cloud_notm}} IAM le permite crear, actualizar, suprimir y utilizar claves de API para usuarios y servicios. Estas claves de API se pueden crear con llamadas de API o con la sección Identity & Access de la consola de {{site.data.keyword.cloud}} Platform. Se puede utilizar la misma clave en varios servicios. Cada usuario puede tener varias claves de API para dar soporte a escenarios de rotación de claves, así como a escenarios en los que se utilizan diferentes claves para distintos fines para limitar la exposición de una única clave.

Consulte [¿Qué es Cloud IAM?](/docs/iam?topic=iam-iamoverview#iamoverview) para obtener más información.

### Usuarios y claves de API
{: #iam-overview-user-api-keys}

Los usuarios de {{site.data.keyword.cloud_notm}} pueden crear y utilizar claves de API para la automatización y la creación de scripts, así como para el inicio de sesión federado cuando se utiliza la CLI. Las claves de API se pueden crear en la interfaz de usuario de Identity and Access Management o mediante la CLI `ibmcloud`.

### ID de servicio y claves de API
{: #iam-overview-service-id-api-key}

El servicio de señal de IAM permite crear ID de servicio y claves de API para los ID de servicio. Un ID de servicio es parecido a un "ID funcional" o a un "ID de aplicación" y se utiliza para autenticar servicios, no para representar a un usuario.

Los usuarios pueden crear ID de servicio y enlazarlos a ámbitos, como una cuenta de {{site.data.keyword.cloud_notm}} Platform, una organización de CloudFoundry o un espacio de CloudFoundry, aunque, para adoptar IAM, es mejor enlazar los ID de servicio a una cuenta de {{site.data.keyword.cloud_notm}} Platform. Este enlace se lleva a cabo para dar al ID de servicio un contenedor en el que residir. Este contenedor también define quién puede actualizar y suprimir el ID de servicio y quién puede crear, actualizar, leer y suprimir las claves de API que asociadas a dicho ID de servicio. Es importante tener en cuenta que un ID de servicio NO está relacionado con un usuario.

### Rotación de claves
{: #iam-overview-key-rotation}

Las claves de API deben rotarse regularmente para evitar cualquier violación de seguridad causada por una filtración de claves.

## Gestión de acceso
{: #iam-overview-access-management}

El control de accesos de IAM proporciona una forma común de asignar roles de usuario para recursos de {{site.data.keyword.cloud_notm}} y controla las acciones que pueden llevar a cabo los usuarios sobre dichos recursos. Puede ver y gestionar usuarios de la cuenta o de la organización, en función de las opciones de acceso que se le hayan asignado. Por ejemplo, a los propietarios de cuenta se les asigna automáticamente el rol de administrador de la cuenta para la gestión de identidades y de acceso, lo que les permite asignar y gestionar políticas de servicio para todos los miembros de su cuenta.

### Usuarios, roles, recursos y políticas
{: #iam-overview-access-policies}

El control de acceso de IAM permite la asignación de políticas por servicio o instancia de servicio para permitir niveles de acceso para gestionar recursos y usuarios dentro del contexto asignado. Una política otorga a un usuario uno o varios roles sobre un conjunto de recursos mediante una combinación de atributos para definir el conjunto aplicable de recursos. Cuando asigna una política a un usuario, primero debe especificar el servicio y luego el rol o roles que se deben asignar. Es posible que haya opciones de configuración adicionales, en función del servicio que seleccione.

Mientras que los roles son una colección de acciones, las acciones que se correlacionan con estos roles son específicas del servicio. Cada servicio determina este rol para la correlación de acciones durante el proceso de incorporación y esta correlación se aplica a todos los usuarios del servicio. Los roles y las políticas de acceso se configuran mediante el punto de administración de políticas (PAP) y se aplican mediante el punto de aplicación de política (PEP) y el punto de decisión de política (PDP).

Consulte [Prácticas recomendadas para organizar usuarios, equipos y aplicaciones](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications) para obtener más información.
