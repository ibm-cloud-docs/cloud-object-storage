---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, curl, cli

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

# Utilización de `curl`
{: #curl}

A continuación encontrará una 'hoja de repaso' de los mandatos básicos de `curl` para la API REST de {{site.data.keyword.cos_full}}. Encontrará más información en la consulta de API correspondiente a [grupos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) o a [objetos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations).

Para utilizar `curl` se presupone que el usuario está familiarizado con la línea de mandatos y con el almacenamiento de objetos y que ha obtenido la información necesaria de una [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), la [consulta de puntos finales](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) o la [consola](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Si no está familiarizado con alguno de los términos o de las variables, los puede consultar en el [glosario](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

**Nota**: Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
{:tip}

## Solicitud de una señal de IAM
{: #curl-iam}

Hay dos formas de generar una señal oauth de IAM para autenticar las solicitudes: mediante un mandato `curl` con una clave de API (lo cual se describe a continuación) o desde la línea de mandatos mediante la [CLI de IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli). 

### Solicitud de una señal de IAM mediante una clave de API
{: #curl-token}

En primer lugar, asegúrese de que tiene una clave de API. La puede obtener de [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys).

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## Obtención del ID de instancia de recurso
{: #curl-instance-id}

Algunos de los mandatos siguientes requieren un parámetro `ibm-service-instance-id`. Para encontrar este valor, vaya al separador **Credenciales de servicio** de la instancia de Object Storage en la consola de la nube. Cree una credencial nueva si es necesario y, a continuación, utilice el menú desplegable *Ver credenciales* para ver el formato JSON. Utilice el valor de `resource_instance_id`. 

Para utilizarlo con las API de curl, solo necesita el UUID que empieza después del último signo de dos puntos y termina antes del signo de dos puntos doble final. Por ejemplo, el ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` se puede abreviar como `39d8d161-22c4-4b77-a856-f11db5130d7d`.
{:tip}

## Obtención de una lista de grupos
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Adición de un grupo
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Adición de un grupo (clase de almacenamiento)
{: #curl-add-bucket-class}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}

Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint).

## Creación de un CORS de grupo
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

La cabecera `Content-MD5` tiene que ser la representación binaria de un hash MD5 codificado en base64.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Obtención de un CORS de grupo
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Supresión de un CORS de grupo
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Obtención de una lista de objetos
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Obtención de cabeceras de grupo
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Supresión de un grupo
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Carga de un objeto
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## Obtención de las cabeceras de un objeto
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Copia de un objeto
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## Comprobación de la información de CORS
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## Descarga de un objeto
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Comprobación de la ACL de un objeto
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Cómo permitir el acceso anónimo a un objeto
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Supresión de un objeto
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Supresión de varios objetos
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

La cabecera `Content-MD5` tiene que ser la representación binaria de un hash MD5 codificado en base64.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Inicio de una carga de varias partes
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Carga de una parte
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## Finalización de una carga de varias partes
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## Obtención de cargas de varias partes incompletas
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Terminación anómala de cargas de varias partes incompletas
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
