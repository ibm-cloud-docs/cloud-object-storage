---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, buckets

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

# Operaciones de grupo
{: #compatibility-api-bucket-operations}


## Obtención de una lista de grupos
{: #compatibility-api-list-buckets}

Una solicitud `GET` enviada a la raíz del punto final devuelve una lista de los grupos que están asociados a la instancia de servicio especificada. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Cabecera                    | Tipo   | ¿Obligatorio? |  Descripción
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | Serie | Sí | Obtiene una lista de los grupos que se han creado en esta instancia de servicio.

Parámetro de consulta                    | Valor   | ¿Obligatorio? |  Descripción
--------------------------|--------|---| -----------------------------------------------------------
`extended` | Ninguno | No | Proporciona los metadatos de `LocationConstraint` en la lista.

En los SDK o en la CLI no se da soporte a la lista ampliada.
{:note}

**Sintaxis**

```bash
GET https://{endpoint}/
```

**Solicitud de ejemplo**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Respuesta de ejemplo**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

### Obtención de una lista ampliada
{: #compatibility-api-list-buckets-extended}

**Sintaxis**

```bash
GET https://{endpoint}/?extended
```

**Solicitud de ejemplo**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Respuesta de ejemplo**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <IsTruncated>false</IsTruncated>
    <MaxKeys>1000</MaxKeys>
    <Prefix/>
    <Marker/>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
            <LocationConstraint>us-south-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
            <LocationConstraint>seo01-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
            <LocationConstraint>eu-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
            <LocationConstraint>us-cold</LocationConstraint>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

## Creación de un grupo
{: #compatibility-api-new-bucket}

Una solicitud `PUT` enviada a la raíz del punto final seguida de una serie crea un grupo. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Los nombres de los grupos deben ser globalmente exclusivos y deben cumplir con DNS; los nombres de entre 3 y 63 caracteres de longitud deben constar de letras minúsculas, números y guiones. Los nombres de grupo deben empezar y terminar por una letra en minúscula o por un número. No se permiten nombres de grupo que se parezcan a direcciones IP. Esta operación no utiliza parámetros de consulta específicos de la operación.

Los nombres de grupo deben ser exclusivos porque todos los grupos de la nube pública comparten un espacio de nombres global. Esto permite el acceso a un grupo sin necesidad de proporcionar ninguna instancia de servicio o información de cuenta. Tampoco se puede crear un grupo con un nombre que empiece por `cosv1-` o `account-`, ya que estos prefijos están reservados por el sistema.
{:important}

Cabecera                                        | Tipo   | Descripción
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | Serie  |  Esta cabecera hace referencia a la instancia de servicio en la que se va a crear el grupo y a la que se facturará el uso de los datos.

**Nota**: Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio en el nombre del grupo o del objeto.
{:tip}

**Sintaxis**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Solicitud de ejemplo**

Este es un ejemplo de creación de un nuevo grupo llamado 'images'.

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

----

## Creación de un grupo con una clase de almacenamiento diferente
{: #compatibility-api-storage-class}

Para crear un grupo con una clase de almacenamiento diferente, envíe un bloque XML que especifique una configuración de grupo con una `LocationConstraint` de `{provisioning code}` en el cuerpo de una solicitud `PUT` a un punto final de grupo. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Tenga en cuenta que se aplican las [reglas de denominación](#compatibility-api-new-bucket) estándares de grupo. Esta operación no utiliza parámetros de consulta específicos de la operación.

Cabecera                                        | Tipo   | Descripción
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | Serie  |  Esta cabecera hace referencia a la instancia de servicio en la que se va a crear el grupo y a la que se facturará el uso de los datos.

**Sintaxis**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

**Solicitud de ejemplo**

Este es un ejemplo de creación de un nuevo grupo llamado 'vault-images'.

```http
PUT /vault-images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
Content-Length: 110
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Fri, 17 Mar 2017 17:52:17 GMT
X-Clv-Request-Id: b6483b2c-24ae-488a-884c-db1a93b9a9a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
Content-Length: 0
```

----

## Creación de un nuevo grupo con claves de cifrado gestionadas por Key Protect (SSE-KP)
{: #compatibility-api-key-protect}

Para crear un grupo donde las claves de cifrado están gestionadas por Key Protect, es necesario tener acceso a una instancia de servicio de Key Protect situada en la misma ubicación que el grupo nuevo. Esta operación no utiliza parámetros de consulta específicos de la operación.

Para obtener más información sobre el uso de Key Protect para gestionar las claves de cifrado, [consulte la documentación](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

Tenga en cuenta que Key Protect **no** está disponible en una configuración de varias regiones y que cualquier grupo de SSE-KP debe ser regional.
{:tip}

Cabecera                                        | Tipo   | Descripción
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | Serie  |  Esta cabecera hace referencia a la instancia de servicio en la que se va a crear el grupo y a la que se facturará el uso de los datos.
`ibm-sse-kp-encryption-algorithm` | Serie | Esta cabecera se utiliza para especificar el algoritmo y el tamaño de clave que se deben utilizar con la clave de cifrado almacenada mediante Key Protect. Este valor se debe establecer en la serie `AES256`.
`ibm-sse-kp-customer-root-key-crn`  | Serie | Esta cabecera se utiliza para hacer referencia a la clave raíz específica que utiliza Key Protect para cifrar este grupo. Este valor debe ser el CRN completo de la clave raíz.

**Sintaxis**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Solicitud de ejemplo**

Este es un ejemplo de creación de un nuevo grupo llamado 'secure-files'.

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

---

## Recuperación de las cabeceras de un grupo
{: #compatibility-api-head-bucket}

Un mandato `HEAD` emitido a un grupo devolverá las cabeceras de dicho grupo.

Las solicitudes `HEAD` no devuelven un cuerpo y por lo tanto no pueden devolver mensajes de error específicos como, por ejemplo, `NoSuchBucket`, sino simplemente `NotFound`.
{:tip}

**Sintaxis**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**Solicitud de ejemplo**

Este es un ejemplo de captación de las cabeceras correspondientes al grupo 'images'.

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
```

**Solicitud de ejemplo**

Las solicitudes `HEAD` sobre grupos con cifrado Key Protect devolverán cabeceras adicionales.

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
ibm-sse-kp-enabled: True
ibm-see-kp-crk-id: {customer-root-key-id}
```

----

## Obtención de una lista de objetos de un grupo determinado (Versión 2)
{: #compatibility-api-list-objects-v2}

Una solicitud `GET` dirigida a un grupo devuelve una lista de objetos, con un límite 1.000 cada vez, devueltos en orden no lexográfico. El valor `StorageClass` que se devuelve en la respuesta es un valor predeterminado, ya que las operaciones de clase de almacenamiento no se implementan en COS. Esta operación no utiliza cabeceras ni elementos de carga útil específicos de la operación.

**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### Parámetros de consulta opcionales
{: #compatibility-api-list-objects-v2-params}
Nombre | Tipo | Descripción
--- | ---- | ------------
`list-type` | Serie | Indica la versión 2 de la API y el valor debe ser 2.
`prefix` | Serie | Limita la respuesta a los nombres de objeto que empiezan por `prefix`.
`delimiter` | Serie | Agrupa los objetos entre `prefix` y `delimiter`.
`encoding-type` | Serie | Si se utilizan caracteres unicode que no reciben soporte de XML en el nombre de un objeto, este parámetro se puede establecer en `url` para codificar correctamente la respuesta.
`max-keys` | Serie | Restringe el número de objetos que se muestran en la respuesta. El valor predeterminado y máximo es 1.000.
`fetch-owner` | Serie | De forma predeterminada, la versión 2 de la API no incluye la información de `Owner`. Establezca este parámetro en `true` si se desea la información de `Owner` en la respuesta.
`continuation-token` | Serie | Especifica el siguiente conjunto de objetos que se debe devolver cuando se trunca la respuesta (el elemento `IsTruncated` devuelve `true`).<br/><br/>La respuesta inicial incluirá el elemento `NextContinuationToken`. Utilice esta señal en la siguiente solicitud como valor de `continuación-token`.
`start-after` | Serie | Devuelve los nombres de clave después de un objeto de clave específico.<br/><br/>*Este parámetro solo es válido en la solicitud inicial.*  Si se incluye un parámetro `continuation-token` en la solicitud, este parámetro se pasa por alto.

**Solicitud de ejemplo (simple con IAM)**

Esta solicitud muestra una lista de los objetos contenidos en el grupo "apiary".

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Respuesta de ejemplo (simple)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 814
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <KeyCount>3</KeyCount>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Solicitud de ejemplo (parámetro max-keys)**

Esta solicitud muestra una lista de los objetos contenidos en el grupo "apiary" con una clave máxima devuelta establecida en 1.

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Respuesta de ejemplo (respuesta truncada)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 598
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <NextContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Solicitud de ejemplo (parámetro continuation-token)**

Esta solicitud muestra una lista de los objetos contenidos en el grupo "apiary" con una señal de continuación especificada.

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Respuesta de ejemplo (respuesta truncada, parámetro continuation-token)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 604
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <ContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</ContinuationToken>
  <NextContinuationToken>1a8j20CqowRrM4epIQ7fTBuyPZWZUeA8Epog16wYu9KhAPNoYkWQYhGURsIQbll1lP7c-OO-V5Vyzu6mogiakC4NSwlK4LyRDdHQgY-yPH4wMB76MfQR61VyxI4TJLxIWTPSZA0nmQQWcuV2mE4jiDA</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

### Obtención de una lista de objetos de un grupo determinado (en desuso)
{: #compatibility-api-list-objects}

**Nota:** *esta API se incluye por motivos de compatibilidad con versiones anteriores.*  Consulte [Versión 2](#compatibility-api-list-objects-v2) para ver el método recomendado para recuperar los objetos de un grupo.

Una solicitud `GET` dirigida a un grupo devuelve una lista de objetos, con un límite 1.000 cada vez, devueltos en orden no lexográfico. El valor `StorageClass` que se devuelve en la respuesta es un valor predeterminado, ya que las operaciones de clase de almacenamiento no se implementan en COS. Esta operación no utiliza cabeceras ni elementos de carga útil específicos de la operación.

**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### Parámetros de consulta opcionales
{: #compatibility-api-list-objects-params}

Nombre | Tipo | Descripción
--- | ---- | ------------
`prefix` | Serie | Limita la respuesta a los nombres de objeto que empiezan por `prefix`.
`delimiter` | Serie | Agrupa los objetos entre `prefix` y `delimiter`.
`encoding-type` | Serie | Si se utilizan caracteres unicode que no reciben soporte de XML en el nombre de un objeto, este parámetro se puede establecer en `url` para codificar correctamente la respuesta.
`max-keys` | Serie | Restringe el número de objetos que se muestran en la respuesta. El valor predeterminado y máximo es 1.000.
`marker` | Serie | Especifica el objeto a partir del cual debe comenzar la lista, en orden binario UTF-8.

**Solicitud de ejemplo**

Esta solicitud muestra una lista de los objetos contenidos en el grupo "apiary".

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 909
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <Marker/>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

## Supresión de un grupo

Un mandato `DELETE` emitido a un grupo vacío suprime el grupo. Después de suprimir un grupo, el sistema retendrá el nombre en reserva durante 10 minutos, después de lo cual se liberará para su reutilización. *Solo se pueden suprimir los grupos vacíos.*

**Sintaxis**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### Cabeceras opcionales

Nombre | Tipo | Descripción
--- | ---- | ------------
`aspera-ak-max-tries` | Serie | Especifica el número de veces que se intenta ejecutar la operación de supresión. El valor predeterminado es 2.


**Solicitud de ejemplo**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

El servidor responde con `204 No Content`.

Si se solicita la supresión de un grupo que no está vacío, el servidor responde con `409 Conflict`.

**Respuesta de ejemplo**

```xml
<Error>
  <Code>BucketNotEmpty</Code>
  <Message>The bucket you tried to delete is not empty.</Message>
  <Resource>/apiary/</Resource>
  <RequestId>9d2bbc00-2827-4210-b40a-8107863f4386</RequestId>
  <httpStatusCode>409</httpStatusCode>
</Error>
```

----

## Obtención de una lista de las cargas de varias partes canceladas o incompletas para un grupo

Un mandato `GET` emitido a un grupo con los parámetros adecuados recupera información sobre las cargas de varias partes canceladas o incompletas para el grupo.

**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**Parámetros **

Nombre | Tipo | Descripción
--- | ---- | ------------
`prefix` | Serie | Limita la respuesta a los nombres de objeto que empiezan por `{prefix}`.
`delimiter` | Serie | Agrupa los objetos entre `prefix` y `delimiter`.
`encoding-type` | Serie | Si se utilizan caracteres unicode que no reciben soporte de XML en el nombre de un objeto, este parámetro se puede establecer en `url` para codificar correctamente la respuesta.
`max-uploads` | entero | Restringe el número de objetos que se muestran en la respuesta. El valor predeterminado y máximo es 1.000.
`key-marker` | Serie | Especifica dónde debe comenzar la lista.
`upload-id-marker` | Serie | Se pasa por alto si no se especifica `key-marker`; de lo contrario, establece un punto en el que empezar la lista de partes por encima de `upload-id-marker`.

**Solicitud de ejemplo**

Este es un ejemplo de recuperación de todas las cargas de varias partes canceladas e incompletas.

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo ** (no hay ninguna carga de varias partes en curso)

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:22:27 GMT
X-Clv-Request-Id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Content-Type: application/xml
Content-Length: 374
```

```xml
<ListMultipartUploadsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>apiary</Bucket>
  <KeyMarker/>
  <UploadIdMarker/>
  <NextKeyMarker>multipart-object-123</NextKeyMarker>
  <NextUploadIdMarker>0000015a-df89-51d0-2790-dee1ac994053</NextUploadIdMarker>
  <MaxUploads>1000</MaxUploads>
  <IsTruncated>false</IsTruncated>
  <Upload>
    <Key>file</Key>
    <UploadId>0000015a-d92a-bc4a-c312-8c1c2a0e89db</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-16T22:09:01.002Z</Initiated>
  </Upload>
  <Upload>
    <Key>multipart-object-123</Key>
    <UploadId>0000015a-df89-51d0-2790-dee1ac994053</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-18T03:50:02.960Z</Initiated>
  </Upload>
</ListMultipartUploadsResult>
```

----

## Obtención de la configuración de compartición de recursos entre orígenes para un grupo

Un mandato `GET` emitido a un grupo con los parámetros adecuados recupera información sobre la configuración de compartición de recursos entre orígenes (CORS) para el grupo.

**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Solicitud de ejemplo**

Este es un ejemplo de obtención de la configuración de CORS en el grupo "apiary".

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo** 

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:20:30 GMT
X-Clv-Request-Id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Content-Type: application/xml
Content-Length: 123
```

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <CORSRule>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
  </CORSRule>
</CORSConfiguration>
```

----

## Creación de una configuración de compartición de recursos entre orígenes para un grupo

Un mandato `PUT` emitido a un grupo con los parámetros adecuados crea o sustituye una configuración de compartición de recursos entre orígenes (CORS) para el grupo.

La cabecera `Content-MD5` necesaria tiene que ser la representación binaria de un hash MD5 codificado en base64.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Sintaxis**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Solicitud de ejemplo**

Este es un ejemplo de adición de una configuración de CORS que permite que las solicitudes procedentes de `www.ibm.com` emitan solicitudes `GET`, `PUT` y `POST` al grupo.

```http
PUT /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 237
```

```xml
<CORSConfiguration>
  <CORSRule>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
  </CORSRule>
</CORSConfiguration>
```


**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```

----

## Supresión de una configuración de compartición de recursos entre orígenes para un grupo

Un mandato `DELETE` emitido a un grupo con los parámetros adecuados crea o sustituye una configuración de compartición de recursos entre orígenes (CORS) para el grupo.

**Sintaxis**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Solicitud de ejemplo**

Este es un ejemplo de supresión de una configuración de CORS para un grupo.

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

El servidor responde con `204 No Content`.

----

## Obtención de la restricción de ubicación para un grupo

Un mandato `GET` emitido a un grupo con el parámetro adecuado recupera la información de ubicación de un grupo.

**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**Solicitud de ejemplo**

Este es un ejemplo de recuperación de la ubicación del grupo "apiary".

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Respuesta de ejemplo**

```http
HTTP/1.1 200 OK
Date: Tue, 12 Jun 2018 21:10:57 GMT
X-Clv-Request-Id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Content-Type: application/xml
Content-Length: 161
```

```xml
<LocationConstraint xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  us-south-standard
</LocationConstraint>
```

----

## Creación de una configuración de ciclo de vida de un grupo
{: #compatibility-api-create-bucket-lifecycle}

Una operación `PUT` utiliza el parámetro de consulta lifecycle para establecer los valores de ciclo de vida para el grupo. Se necesita una cabecera `Content-MD5` como comprobación de la integridad de la carga útil.

**Sintaxis**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Elementos de carga útil**

El cuerpo de la solicitud debe contener un bloque XML con el esquema siguiente:

|Elemento|Tipo|Hijos|Predecesor|Restricción|
|---|---|---|---|---|
|LifecycleConfiguration|Contenedor|Rule|Ninguno|Límite 1|
|Rule|Contenedor|ID, Status, Filter, Transition|LifecycleConfiguration|Límite 1|
|ID|Serie|Ninguno|Rule|**Debe** consistir en ``(a-z,A- Z0-9)`` y los siguientes símbolos:`` !`_ .*'()- ``|
|Filter|Serie|Prefix|Rule|**Debe** contener un elemento `Prefix`.|
|Prefix|Serie|Ninguno|Filter|Se **debe** establecer en `<Prefix/>`.|
|Transition|Contenedor|Days, StorageClass|Rule|Límite 1.|
|Days|Entero no negativo|Ninguno|Transition|**Debe** ser un valor mayor que 0.|
|Date|Fecha|Ninguno|Transition|**Debe** estar en formato ISO 8601 y la fecha debe ser futura.|
|StorageClass|Serie|Ninguno|Transition|Se **debe** establecer en GLACIER.|

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>{string}</ID>
        <Status>Enabled</Status>
        <Filter>
            <Prefix/>
        </Filter>
        <Transition>
            <Days>{integer}</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

**Solicitud de ejemplo**

```http
PUT /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ== 
Content-Length: 305
```

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

El servidor responde con `200 OK`.

----

## Recuperación de una configuración de ciclo de vida de un grupo

Una operación `GET` utiliza el parámetro de consulta lifecycle para recuperar los valores de ciclo de vida para el grupo.

**Sintaxis**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Solicitud de ejemplo**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**Respuesta de ejemplo**

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

----

## Supresión de la configuración de ciclo de vida de un grupo

Un mandato `DELETE` emitido a un grupo con los parámetros adecuados elimina cualquier configuración de ciclo de vida para un grupo.

**Sintaxis**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Solicitud de ejemplo**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

El servidor responde con `204 No Content`.
