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

# Iniciación a IAM
{: #iam}

## Roles y acciones de Identity and Access Management
{: #iam-roles}

El acceso a las instancias de servicio de {{site.data.keyword.cos_full}} para los usuarios de su cuenta está controlado por {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM). Todos los usuarios que acceden al servicio de {{site.data.keyword.cos_full}} en su cuenta deben tener asignada una política de acceso con un rol de usuario IAM definido. Esa política determina las acciones que puede realizar el usuario dentro del contexto del servicio o de la instancia que seleccione. Las acciones permitidas se pueden personalizar y definir mediante el servicio {{site.data.keyword.Bluemix_notm}} como operaciones que se permiten sobre el servicio. Luego las acciones se correlacionan con roles de usuario de IAM.

Las políticas permiten que se otorgue acceso a distintos niveles. Algunas de las opciones son las siguientes: 

* Acceso a todas las instancias del servicio en su cuenta
* Acceso a una instancia de servicio individual en su cuenta
* Acceso a un recurso específico dentro de una instancia
* Acceso a todos los servicios habilitados para IAM en su cuenta

Después de definir el ámbito de la política de acceso, asigne un rol. Revise las tablas siguientes, en las que se describen las acciones que permite cada rol dentro del servicio de {{site.data.keyword.cos_short}}.

En la tabla siguiente se detallan las acciones que se correlacionan con los roles de gestión de plataforma. Los roles de gestión de plataforma permiten a los usuarios realizar tareas sobre los recursos de servicio a nivel de plataforma, por ejemplo asignar acceso de usuario para el servicio, crear o suprimir un ID de servicio, crear instancias y enlazar instancias a las aplicaciones.

| Rol de gestión de plataforma | Descripción de acciones | Acciones de ejemplo|
|:-----------------|:-----------------|:-----------------|
| Visor | Ver instancias de servicio, pero no modificarlas | <ul><li>Obtener una lista de las instancias de servicio de COS disponibles</li><li>Ver detalles del plan de servicio de COS</li><li>Ver detalles de uso</li></ul>|
| Editor | Realizar todas las acciones de la plataforma, excepto gestionar las cuentas y asignar políticas de acceso |<ul><li>Crear y suprimir instancias de servicio de COS</li></ul> |
| Operador | COS no lo utiliza | Ninguna |
| Administrador | Realizar todas las acciones de plataforma basadas en el recurso al que se está asignando este rol, incluida la asignación de políticas de acceso a otros usuarios |<ul><li>Actualizar políticas de usuario</li>Actualizar planes de precios</ul>|
{: caption="Tabla 1. Roles de usuario y acciones de IAM" caption-side="top"}


En la tabla siguiente se muestran acciones que se correlacionan con roles de acceso al servicio. Los roles de acceso al servicio permiten a los usuarios acceder a {{site.data.keyword.cos_short}}, así como la posibilidad de llamar a la API de {{site.data.keyword.cos_short}}.

| Rol de acceso al servicio | Descripción de acciones                                                                                                                                       | Acciones de ejemplo                                                                     |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| Lector de contenido      | Descargar objetos, pero no obtener una lista de objetos o de grupos | <ul><li>Descargar objetos</li></ul> |
| Lector              | Además de las acciones del Lector de contenido, los lectores pueden obtener una lista de grupos y/u objetos, pero no modificarlos. | <ul><li>Obtención de una lista de grupos</li><li>Listar y descargar objetos</li></ul>                    |
| Escritor              | Además de las acciones de Lector, los escritores pueden crear grupos y cargar objetos. | <ul><li>Crear nuevos grupos y objetos</li><li>Eliminar grupos y objetos</li></ul> |
| Gestor             | Además de las acciones de Escritor, los gestores pueden realizar acciones con privilegios que afectan al control de accesos. | <ul><li>Añadir una política de retención</li><li>Añadir un cortafuegos de grupo</li></ul>              |
{: caption="Tabla 3. Acciones y roles de acceso al servicio de IAM" caption-side="top"}


Para obtener información sobre la asignación de roles de usuario en la interfaz de usuario, consulte [Gestión del acceso de IAM](/docs/iam?topic=iam-iammanidaccser).
 
