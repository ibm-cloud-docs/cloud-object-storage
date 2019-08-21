---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# Utilización de credenciales de HMAC
{: #hmac}

La API de {{site.data.keyword.cos_full}} es una API basada en REST para leer y escribir objetos. Utiliza {{site.data.keyword.iamlong}} para la autenticación y la autorización, y da soporte a un subconjunto de la API de S3 para facilitar la migración de aplicaciones a {{site.data.keyword.cloud_notm}}.

Además de la autenticación basada en señales de IAM, también es posible [autenticarse mediante una firma](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) creada a partir de un par de claves de acceso y de secreto. Esto es funcionalmente idéntico a la versión 4 de AWS Signature, y las claves de HMAC que proporciona IBM COS deberían funcionar con la mayoría de las bibliotecas y herramientas compatibles con S3.

Los usuarios pueden crear un conjunto de credenciales de HMAC cuando crean una [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) proporcionando el parámetro de configuración `{"HMAC":true}` durante la creación de credenciales. A continuación se muestra un ejemplo de cómo utilizar la CLI de {{site.data.keyword.cos_full}} para crear una clave de servicio con credenciales HMAC utilizando el rol de escritor, **Writer** (es posible que haya otros roles disponibles para su cuenta y que se adapten mejor a sus necesidades). 

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="Ejemplo 1. Utilización de cURL para crear credenciales de HMAC. Observe el uso de comillas simples y de comillas dobles." caption-side="bottom"}

Si desea guardar los resultados de la clave que acaba de generar el mandato del Ejemplo 1, puede añadir ` > file.skey` al final del ejemplo. En este conjunto de instrucciones, solo necesita encontrar la cabecera `cos_hmac_keys` con las claves hijo, `access_key_id` y `secret_access_key`, los dos campos que necesita, tal como se muestra en el Ejemplo 2.

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="Ejemplo 2. Claves de la nota cuando se generan credenciales de HMAC." caption-side="bottom"}

Resulta especialmente interesante la capacidad de establecer variables de entorno (para las que las instrucciones son específicas del sistema operativo utilizado). Por ejemplo, en el Ejemplo 3, un script `.bash_profile` contiene `COS_HMAC_ACCESS_KEY_ID` y `COS_HMAC_SECRET_ACCESS_KEY` que se exportan al iniciar un shell y se utilizan en el desarrollo.

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="Ejemplo 3. Utilización de credenciales de HMAC como variables de entorno." caption-side="bottom"}

Una vez creada la credencial de servicio, la clave HMAC se incluye en el campo `cos_hmac_keys`. A continuación, estas claves HMAC se asocian con un [ID de servicio](/docs/iam?topic=iam-serviceids#serviceids) y se pueden utilizar para acceder a cualquier recurso u operación permitido por el rol del ID de servicio. 

Tenga en cuenta que, cuando se utilizan credenciales de HMAC para crear firmas que se utilizan con llamadas directas a la [API REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api), se necesitan cabeceras adicionales:
1. Todas las solicitudes deben tener una cabecera `x-amz-date` con la fecha en el formato `%Y%m%dT%H%M%SZ`.
2. Cualquier solicitud que tenga una carga útil (cargas de objetos, supresión de varios objetos, etc.) debe proporcionar una cabecera `x-amz-content-sha256` con un hash SHA256 del contenido de la carga útil.
3. Las ACL (que no sean `public-read`) no reciben soporte.

Actualmente no todas las herramientas compatibles con S3 reciben soporte. Algunas herramientas intentan establecer ACL que no son `public-read` durante la creación de grupos. La creación de grupos mediante estas herramientas fallará. Si una solicitud `PUT bucket` falla con un error de ACL no soportada, utilice primero la [consola](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para crear el grupo y, a continuación, configure la herramienta de modo que lea y escriba objetos en dicho grupo. Las herramientas que establecen las ACL al escribir objetos no reciben soporte actualmente.
{:tip}
