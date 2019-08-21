---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-05"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:external: target="blank" .external}
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 
{:http: .ph data-hd-programlang='http'} 

# Supresión de datos obsoletos con reglas de caducidad
{: #expiry}

Una regla de caducidad suprime los objetos después de un periodo de tiempo definido (desde la fecha de creación del objeto). 

Puede establecer el ciclo de vida de los objetos mediante la consola web, la API REST y herramientas de terceros que están integradas con {{site.data.keyword.cos_full_notm}}. 

* Se puede añadir una regla de caducidad a un grupo nuevo o existente.
* Una regla de caducidad existente se puede modificar o inhabilitar.
* Se aplica una regla de caducidad nueva o modificada a todos los objetos nuevos y existentes del grupo.
* Para añadir o modificar políticas de ciclo de vida se necesita el rol de `Escritor`. 
* Se pueden definir un máximo de 1000 reglas de ciclo de vida (archivado + caducidad) por grupo.
* Los cambios realizados en las reglas de caducidad pueden tardar hasta 24 horas en entrar en vigor.
* El ámbito de cada regla de caducidad se puede limitar mediante la definición de un filtro de prefijo opcional que se aplica dolo a un subconjunto de objetos con nombres que coincidan con el prefijo.
* Se aplicará una regla de caducidad sin un filtro de prefijo a todos los objetos del grupo.
* El periodo de caducidad para un objeto, especificado en número de días, se calcula a partir del momento en que se creó el objeto y se redondea a la hora UTC de la medianoche del día siguiente. Por ejemplo, si tiene una regla de caducidad para que un conjunto de objetos de un grupo caduque diez días después de la fecha de creación, un objeto creado el 15 de abril de 2019 a las 05:10 UTC caducará el 26 de abril de 2019 a las 00:00 UTC. 
* Las reglas de caducidad de cada grupo se evalúan cada 24 horas. Cualquier objeto apto para que caduque (en función de la fecha de caducidad de los objetos) se colocará en la cola para su supresión. La supresión de los objetos caducados empieza al día siguiente y suele tardar menos de 24 horas. No se le facturará por el almacenamiento asociado correspondiente a los objetos una vez que se hayan suprimido.

Las acciones de caducidad de los objetos sujetos a una política de retención de Immutable Object Storage de un grupo se diferirán hasta que se deje de imponer la política de retención. 
{: important}

## Atributos de las reglas de caducidad
{: #expiry-rules-attributes}

Cada regla de caducidad tiene los atributos siguientes:

### ID
El ID de una regla debe ser exclusivo dentro de la configuración del ciclo de vida del grupo.

### Caducidad
El bloque de caducidad contiene los detalles que controlan la supresión automática de objetos. Puede ser una fecha específica en el futuro, o un periodo de tiempo después del cual se escriben los objetos nuevos.

### Prefijo
Una serie opcional que se comparará con el prefijo del nombre de objeto en el grupo. Una regla con un prefijo solo se aplicará a los objetos que coinciden. Puede utilizar varias reglas para distintas acciones de caducidad para distintos prefijos dentro del mismo grupo. Por ejemplo, dentro de la misma configuración de ciclo de vida, una regla podría suprimir todos los objetos que empiecen por `logs/` transcurridos 30 días, y una segunda regla podría suprimir los objetos que empiecen por `video/` transcurridos 365 días.  

### Estatus
Una regla se puede habilitar o inhabilitar. Una regla solo está activa cuando está habilitada.

## Configuración de ciclo de vida de ejemplo

Con esta configuración, los objetos nuevos caducan transcurridos 30 días.

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-30-days</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>30</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

Esta configuración suprime cualquier objeto con el prefijo `foo/` el 1 de junio de 2020.

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-on-a-date</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Date>2020-06-01T00:00:00.000Z</Date>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

También puede combinar las reglas de transición y de caducidad.  Esta configuración archiva los objetos 90 días después de su creación y suprime los objetos con el prefijo `foo/` transcurridos 180 días.

```xml
<LifecycleConfiguration>
  <Rule>
		<ID>archive-first</ID>
		<Filter />
		<Status>Enabled</Status>
    <Transition>
      <Days>90</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
	</Rule>
	<Rule>
		<ID>then-delete</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Days>180</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

## Utilización de la consola
{: #expiry-using-console}

Cuando cree un grupo nuevo, marque el recuadro **Añadir regla de caducidad**. A continuación, pulse **Añadir regla** para crear la nueva regla de caducidad. Puede añadir un máximo de cinco reglas durante la creación de un grupo y se pueden añadir reglas adicionales posteriormente.

Para un grupo existente, seleccione **Configuración** en el menú de navegación y pulse **Añadir regla** bajo la sección _Regla de caducidad_.

## Utilización de la API y los SDK
{: #expiry-using-api-sdks}

Puede gestionar mediante programación las reglas de caducidad mediante la API REST o los SDK de IBM COS. Seleccione el formato de los ejemplos seleccionando una categoría en el conmutador de contexto.

### Adición de una regla de caducidad a la configuración del ciclo de vida de un grupo
{: #expiry-api-create}

**Consulta de API REST**
{: http}

Esta implementación de la operación `PUT` utiliza el parámetro de consulta `lifecycle` para definir valores de ciclo de vida para el grupo. Esta operación permite una sola definición de política de ciclo de vida para un grupo. La política se define como un conjunto de reglas que constan de los parámetros siguientes: `ID`, `Status`, `Filter` y `Expiration`.
{: http}
 
Los usuarios de Cloud IAM deben tener el rol de `Escritor` para eliminar una política de ciclo de vida de un grupo.

Los usuarios de la infraestructura clásica deben tener permisos de `Propietario` sobre el grupo para eliminar una política de ciclo de vida de un grupo.

Cabecera                    | Tipo   | Descripción
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Serie | **Obligatorio**: el hash MD5 de 128 bits codificado en base 64 de la carga útil, que se utiliza como comprobación de integridad para garantizar que la carga útil no se ha modificado durante el tránsito.
{: http}

El cuerpo de la solicitud debe contener un bloque XML con el esquema siguiente:
{: http}

| Elemento                  | Tipo                 | Hijos                               | Predecesor                 | Restricción                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Contenedor            | `Rule`                                 | Ninguno                     | Límite 1.                                                                                  |
| `Rule`                   | Contenedor            | `ID`, `Status`, `Filter`, `Expiration` | `LifecycleConfiguration` | Límite 1000.                                                                                  |
| `ID`                     | Serie               | Ninguno                                   | `Rule`                   | Debe consistir en (`a-z,`A-Z0-9`) y los siguientes símbolos: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | Serie               | `Prefix`                               | `Rule`                   | Debe contener un elemento `Prefix`                                                            |
| `Prefix`                 | Serie               | Ninguno                                   | `Filter`                 | La regla se aplica a cualquier objeto con claves que coincidan con este prefijo.                                                           |
| `Expiration`             | `Contenedor`          | `Days` o `Date`                       | `Rule`                   | Límite 1.                                                                                  |
| `Days`                   | Entero no negativo | Ninguno                                   | `Expiration`             | Debe ser un valor mayor que 0.                                                           |
| `Date`                   | Fecha                 | Ninguno                                   | `Expiration`             | Debe estar en formato ISO 8601.                            |
{: http}

El cuerpo de la solicitud debe contener un bloque XML con el esquema que se aborda en la tabla (consulte el Ejemplo 1).
{: http}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Ejemplo 1. Ejemplo de XML del cuerpo de la solicitud." caption-side="bottom"}
{: http}

**Sintaxis**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Ejemplo 2. Observe el uso de barras inclinadas y puntos en este ejemplo de sintaxis." caption-side="bottom"}
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```yaml
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305

<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Ejemplo 3. Ejemplos de cabecera de solicitud para crear una configuración de ciclo de vida de objeto." caption-side="bottom"}
{: http}

**Ejemplo de código que se utiliza con el SDK de NodeJS COS**
{: javascript}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada. 
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);
var date = new Date('June 16, 2019 00:00:00');

var params = {
  Bucket: 'STRING_VALUE', /* obligatorio */
  LifecycleConfiguration: {
    Rules: [ /* obligatorio */
      {
        Status: 'Enabled', /* obligatorio */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* obligatorio */
        Expiration:
        {
          Date: date
        }
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}

**Ejemplo de código que se utiliza con el SDK de Python COS**
{: python}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada. 
{: python}

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'Status': 'Enabled',
                'Filter': {},
                'Expiration':
                {
                    'Days': 123
                },
            },
        ]
    }
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

**Ejemplo de código que se utiliza con el SDK de Java COS**
{: java}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada. 
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Definir una regla para los elementos que caducan de un grupo
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);
            
            // Añadir la regla a una nueva BucketLifecycleConfiguration.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));
            
            // Utilizar el cliente para definir LifecycleConfiguration en el grupo.
            _cosClient.setBucketLifecycleConfiguration(bucketName, configuration);
        }

        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
    }
```
{: codeblock}
{: java}
{: caption="Ejemplo 1. Ejemplos de código que muestran cómo crear la configuración del ciclo de vida" caption-side="bottom"}

### Examen de la configuración del ciclo de vida de un grupo, incluida la caducidad
{: #expiry-api-view}

Esta implementación de la operación `GET` utiliza el parámetro de consulta `lifecycle` para definir valores de ciclo de vida para el grupo. Se devolverá la respuesta HTTP `404` si no hay ninguna configuración de ciclo de vida.
{: http}

Los usuarios de Cloud IAM deben tener el rol de `Lector` para eliminar una política de ciclo de vida de un grupo.

Los usuarios de la infraestructura clásica deben tener permisos de `Lectura` sobre el grupo para eliminar una política de ciclo de vida de un grupo.

Cabecera                    | Tipo   | Descripción
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Serie | **Obligatorio**: el hash MD5 de 128 bits codificado en base 64 de la carga útil, que se utiliza como comprobación de integridad para garantizar que la carga útil no se ha modificado durante el tránsito.
{: http}

**Sintaxis**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Ejemplo 5. Observe el uso de barras inclinadas y puntos en este ejemplo de sintaxis." caption-side="bottom"}
{: codeblock}
{: http}

**Ejemplo de cabecera de solicitud**
{: http}

```yaml
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: caption="Ejemplo 6. Ejemplos de cabecera de solicitud para crear una configuración de ciclo de vida de objeto." caption-side="bottom"}
{: http}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* obligatorio */
};

s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada.

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').get_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada. 

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Utilizar el cliente para leer la configuración
            BucketLifecycleConfiguration config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            
            System.out.println(config.toString());
        }

        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

    }
```
{: codeblock}
{: java}
{: caption="Ejemplo 2. Ejemplos de código que muestran cómo inspeccionar la configuración del ciclo de vida" caption-side="bottom"}

### Supresión de la configuración del ciclo de vida de un grupo, incluida la caducidad
{: #expiry-api-delete}

Esta implementación de la operación `DELETE` utiliza el parámetro de consulta `lifecycle` para definir valores de ciclo de vida para el grupo. Se suprimirán todas las reglas de ciclo de vida asociadas al grupo.  Las transiciones definidas por las reglas dejarán de ejecutarse para los objetos nuevos.  Sin embargo, se mantendrán las reglas de transición existentes para los objetos que ya se habían escrito en el grupo antes de que se suprimieran las reglas.  Las reglas de caducidad dejarán de existir. Se devolverá la respuesta HTTP `404` si no hay ninguna configuración de ciclo de vida.
{: http}

Los usuarios de Cloud IAM deben tener el rol de `Escritor` para eliminar una política de ciclo de vida de un grupo.

Los usuarios de la infraestructura clásica deben tener permisos de `Propietario` sobre el grupo para eliminar una política de ciclo de vida de un grupo.

**Sintaxis**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Ejemplo 7. Observe el uso de barras inclinadas y puntos en este ejemplo de sintaxis." caption-side="bottom"}
{: codeblock}
{: http}

**Ejemplo de cabecera de solicitud**
{: http}

```yaml
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="Ejemplo 8. Ejemplos de cabecera de solicitud para crear una configuración de ciclo de vida de objeto." caption-side="bottom"}
{: http}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada. 
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* obligatorio */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // se ha producido un error
  else     console.log(data);           // respuesta correcta
});
```
{: codeblock}
{: javascript}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada. 

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').delete_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

Para utilizar los SDK de {{site.data.keyword.cos_full}} solo es necesario llamar a las funciones adecuadas con los parámetros correctos y la configuración adecuada. 
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Suprimir la configuración.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);
            
            // Verificar que la configuración se ha suprimido intentando recuperarla.
            config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            String s = (config == null) ? "Configuration has been deleted." : "Configuration still exists.";
            System.out.println(s);
        }

        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

    }

```
{: codeblock}
{: java}
{: caption="Ejemplo 3. Ejemplos de código que muestran cómo suprimir la configuración del ciclo de vida" caption-side="bottom"}

## Siguientes pasos
{: #expiry-next-steps}

La caducidad es solo uno de los muchos conceptos de ciclo de vida disponibles para {{site.data.keyword.cos_full_notm}}.
Cada uno de los conceptos que hemos cubierto en esta visión general se puede seguir explorando en [{{site.data.keyword.cloud_notm}} Plataform](https://cloud.ibm.com/).

