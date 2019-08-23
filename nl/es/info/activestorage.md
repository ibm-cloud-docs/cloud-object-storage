---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: ruby, activestorage, rails, aws

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

# Ruby on Rails/Active Storage
{: #ror-activestorage}

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} es una infraestructura de desarrollo de aplicaciones web de código abierto que combina el lenguaje de programación Ruby con HTML, CSS y JavaScript. Incluye todo lo necesario para crear aplicaciones web del lado del servidor utilizando el patrón MVC (Model-View-Controller). 

En Rails, el modelo (datos de negocio y capa de lógica) del patrón MVC se maneja mediante [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window}. Contiene la infraestructura [Object-Relational Mapping (ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} que conecta los objetos de negocio con almacenamiento persistente en el sistema de gestión de bases de datos relacionales.

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} es una infraestructura integrada para conectar archivos de servicios de almacenamiento en la nube, como {{site.data.keyword.cos_full}}, con objetos de Active Record. Amazon S3, Google Cloud Storage y Microsoft Azure también reciben soporte, así como los servicios locales basados en disco.

Para comenzar a utilizar Active Storage, ejecute los siguientes mandatos del directorio inicial de la aplicación: 

```
bin/rails active_storage:install
bin/rails db:migrate
```

Esto creará las dos tablas necesarias (`active_storage_blobs` y `active_storage_attachments`) en la base de datos de la aplicación para gestionar los archivos adjuntos de Active Record. 

## Configuración del almacenamiento
{: #ror-activestorage-config}

Declare el servicio {{site.data.keyword.cos_short}} en `config/storage.yml`:

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

Añada [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} a su archivo Gemfile:

```
gem "aws-sdk-s3", require: false
```

Puede declarar varias instancias de servicio con distintos puntos finales y grupos para que se utilicen en diferentes entornos.
{:tip}

*Valores de clave*
* `<bucket-region>`: región que coincide con el grupo (por ejemplo, `us-south-standard`). [Aquí](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint) encontrará la lista completa de regiones disponibles.
* `<bucket-name>`: nombre del grupo.
* `<regional-endpoint>`: punto final para acceder al grupo (por ejemplo, `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`). [Aquí](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) encontrará la lista completa de puntos finales disponibles.

La sentencia `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` indica a Rails que obtenga la clave de acceso y la clave secreta de los datos de credenciales guardados en `~/.aws/credentials` en el formato:

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Entorno Rails
{: #ror-activestorage-rails}

Configure los entornos de modo que utilicen el servicio {{site.data.keyword.cos_short}} actualizando el siguiente valor:

```
config.active_storage.service = :ibmcos
```

Actualice el archivo de configuración correspondiente para cada uno de los entornos:

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## Configuración de CORS
{: #ror-activestorage-cors}

Para permitir que Rails acceda al grupo, debe crear una configuración de CORS (Cross-Origin Resource Sharing) parecida a la siguiente:

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <CORSRule>
        <AllowedMethod>GET</AllowedMethod>
        <AllowedMethod>PUT</AllowedMethod>
        <AllowedMethod>POST</AllowedMethod>
        <AllowedOrigin>www.ibm.com</AllowedOrigin>
        <AllowedHeader>x-amz-*</AllowedHeader>
        <AllowedHeader>content-*</AllowedHeader>
    </CORSRule>
</CORSConfiguration>
```

Esta configuración permitirá que las solicitudes procedentes de `www.ibm.com` ejecuten solicitudes `GET`, `PUT` y `POST` en el grupo. Ajuste la entrada `<AllowedOrigin>` para que se ajuste a las necesidades de la aplicación. 

También en necesario permitir cabeceras `x-amz-*` y `content-*` para que Rails interactúe correctamente con el grupo. Encontrará más información sobre CORS en la [Consulta de API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket).
