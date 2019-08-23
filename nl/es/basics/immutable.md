---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: worm, immutable, policy, retention, compliance

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

# Utilización de Immutable Object Storage
{: #immutable}

La característica Immutable Object Storage permite a los clientes conservar registros electrónicos y mantener la integridad de los datos de forma que no se puedan eliminar ni rescribir y con el método WORM (escribir una vez, leer muchas) hasta que finalice el periodo de retención y se elimine cualquier restricción legal. Puede utilizar esta característica cualquier cliente que tenga que conservar datos a largo plazo en su entorno, incluidas, aunque sin limitarse a las mismas, organizaciones de los siguientes sectores:

 * Financiero
 * Sanidad
 * Archivos de contenido multimedia
 * Aquellos que desean evitar la modificación o supresión con privilegios de objetos o documentos

También pueden utilizar las funciones subyacentes de la característica las organizaciones que se ocupan de la gestión de registros financieros, como transacciones de agentes de bolsa, que pueden tener necesidad de conservar objetos en un formato que no se pueda rescribir ni eliminar.

Immutable Object Storage solo está disponible en determinadas regiones. Consulte [Servicios integrados](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability) para obtener más información. También requiere un plan de precios Estándar. Consulte el tema sobre [precios](https://www.ibm.com/cloud/object-storage) para obtener detalles.
{:note}

No se puede utilizar la transferencia de alta velocidad de Aspera con grupos con una política de retención.
{:important}

## Terminología y uso
{: #immutable-terminology}

### Periodo de retención
{: #immutable-terminology-period}

El periodo de tiempo que un objeto debe permanecer almacenado en el grupo de COS.

### Política de retención
{: #immutable-terminology-policy}

Una política de retención se habilita a nivel de grupo de COS. Esta política define el periodo de retención mínimo, máximo y predeterminado y se aplica a todos los objetos del grupo.

El periodo de retención mínimo es la duración mínima del tiempo que un objeto debe conservarse en el grupo.

El periodo de retención máximo es la duración máxima del tiempo que un objeto se puede conservar en el grupo.

Si un objeto se almacena en el grupo sin especificar un periodo de retención personalizado, se utiliza el periodo de retención predeterminado. El periodo de retención mínimo debe ser menor o igual que el periodo de retención predeterminado, que a su vez debe ser menor o igual que el periodo de retención máximo.

Nota: se puede especificar un periodo de retención máximo de 1000 años para los objetos.

Nota: para crear una política de retención sobre un grupo, necesitará el rol de Gestor. Consulte el tema sobre [Permisos de grupo](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions) para obtener más detalles.

### Retención legal 
{: #immutable-terminology-hold}

Es posible que se deba evitar la supresión de algunos registros (objetos), incluso después de que finalice el periodo de retención; por ejemplo, un examen legal que está pendiente de finalización puede requerir acceso a registros durante un periodo que vaya más allá del periodo de retención que se ha establecido originalmente para el objeto. En este caso, se puede aplicar un distintivo de retención legal a nivel de objeto.
Se puede aplicar una retención legal a los objetos durante la carga en el grupo de cos o después de que se añada un objeto.
Nota: se puede aplicar un máximo de 100 retenciones legales por objeto.

### Retención indefinida
{: #immutable-terminology-indefinite}

Permite al usuario definir que el objeto que almacene de forma indefinida hasta que se aplique un nuevo periodo de retención. Esta característica se define objeto a objeto.

### Retención basada en suceso
{: #immutable-terminology-events}

Immutable Object Storage permite al usuario definir una retención indefinida sobre el objeto si no está seguro de la duración final del periodo de retención o si desea utilizar la función de retención basada en suceso. Una vez definida la retención indefinida, las aplicaciones de usuario pueden cambiar la retención del objeto por un valor finito más adelante. Por ejemplo, supongamos que una empresa tiene una política de retención de registros de empleados de tres años después de que el empleado abandone la empresa. Cuando un empleado comienza a trabajar en la empresa, los registros asociados a dicho empleado se pueden retener de forma indefinida. Cuando el empleado abandona la empresa, la retención indefinida se convierte en un valor finito de tres años a partir de la fecha actual, tal como define la política de la empresa. Luego el objeto queda protegido durante tres años a partir del momento del cambio de periodo de retención. Una aplicación de usuario o de un tercero puede cambiar el periodo de retención indefinido por un valor finito mediante un SDK o una API REST.

### Retención permanente
{: #immutable-terminology-permanent}

La retención permanente solio se puede habilitar a un nivel de grupo de COS con la política de retención habilitada y los usuarios pueden seleccionar la opción de periodo de retención permanente durante las cargas de objetos. Una vez habilitado, este proceso no se puede revertir y los objetos que se cargan con un periodo de retención permanente **no se pueden suprimir**. Es responsabilidad de los usuarios validar al final si hay una necesidad legítima de almacenar **permanentemente** los objetos que utilizan grupos de COS con política de retención. 


Cuando se utiliza Immutable Object Storage, el usuario es el responsable de garantizar que la cuenta de IBM Cloud resulta adecuada para las políticas y directrices de IBM Cloud durante el periodo en que los datos están sujetos a una política de retención. Consulte los términos del servicio IBM Cloud para obtener más información.
{:important}

## Immutable Object Storage y consideraciones sobre diversos reglamentos
{: #immutable-regulation}

Cuando se utiliza Immutable Object Storage, es responsabilidad del cliente comprobar y garantizar si alguna de las funciones de la característica se puede aprovechar para cumplir con las reglas de claves relacionadas con el almacenamiento de registros electrónico y la retención, que se suelen regir por:

  * [Regulación de Securities and Exchange Commission (SEC) 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64),
  * [Regulación de Financial Industry Regulatory Authority (FINRA) 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957) y
  * [Regulación de Commodity Futures Trading Commission (CFTC) 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

Para ayudar a los clientes a tomar decisiones informadas, IBM se ha asociado a Cohasset Associates Inc. para realizar una evaluación independiente de la capacidad de Immutable Object Storage de IBM. Consulte el [informe](https://www.ibm.com/downloads/cas/JBDNP0KV) de Cohasset Associates Inc., que contiene detalles de la evaluación de la capacidad de Immutable Object Storage de IBM Cloud Object Storage. 

### Auditoría del acceso y de las transacciones
{: #immutable-audit}
Acceda a los datos de registro correspondientes a Immutable Object Storage para revisar si los cambios en los parámetros de retención, el periodo de retención de objetos y la aplicación de retenciones legales está disponible, caso por caso, abriendo una incidencia de servicio al cliente.

## Utilización de la consola
{: #immutable-console}

Las políticas de retención se pueden añadir a grupos vacíos nuevos o existentes, y no se pueden eliminar. En el caso de un grupo nuevo, asegúrese de que crea el grupo en una [región admitida](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability) y luego seleccione la opción **Añadir política de retención**. En el caso de un grupo existente, asegúrese de que no tiene ningún objeto y luego vaya a los valores de configuración y pulse el botón **Crear política** que hay bajo la sección de política de retención de grupo. En cualquiera de los casos, establezca periodos de retención mínimo, máximo y predeterminado.

## Utilización de API REST, bibliotecas y SDK
{: #immutable-sdk}

Se han incorporado varias API nuevas en los SDK de IBM COS para proporcionar soporte para las aplicaciones que trabajan con políticas de retención. Seleccione un lenguaje (HTTP, Java, Javascript o Python) en la parte superior de esta página para ver ejemplos de utilización del SDK de COS adecuado. 

Tenga en cuenta que en todos los ejemplos de código se asume la existencia de un objeto de cliente llamado `cos` que puede llamar a los distintos métodos. Para ver información sobre cómo crear clientes, consulte las guías de SDK específicas.

Todos los valores de fecha utilizados para establecer periodos de retención están en GMT. Se necesita la cabecera `Content-MD5` para garantizar la integridad de los datos, y se envía automáticamente cuando se utiliza un SDK.
{:note}

### Adición de una política de retención en un grupo existente
{: #immutable-sdk-add-policy}
Esta implementación de la operación `PUT` utiliza el parámetro de consulta `protection` para definir los parámetros de retención para un grupo existente. Esta operación le permite establecer o cambiar el periodo de retención mínimo, predeterminado y máximo. Esta operación también le permite cambiar el estado de protección del grupo. 

Los objetos que se escriben en un grupo protegido no se pueden suprimir hasta que transcurre el periodo de protección y se eliminan todas las retenciones legales sobre el objeto. Se proporciona el valor de retención predeterminado del grupo a un objeto a menos que se proporcione un valor específico de objeto cuando se crea el objeto. Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto. 

Los valores mínimos y máximos admitidos para los valores de periodo de retención `MinimumRetention`, `DefaultRetention` y `MaximumRetention` son de 0 días y 365243 días (1000 años) respectivamente. 

Se necesita una cabecera `Content-MD5`. Esta operación no utiliza ningún parámetro de consulta adicional.

Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

{: http}

**Sintaxis**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```
PUT /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
x-amz-content-sha256: 2938f51643d63c864fdbea618fe71b13579570a86f39da2837c922bae68d72df
Content-MD5: GQmpTNpruOyK6YrxHnpj7g==
Content-Type: text/plain
Host: 67.228.254.193
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

```
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.14.1
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```
{: codeblock}
{: http}

```py
def add_protection_configuration_to_bucket(bucket_name):
    try:
        new_protection_config = {
            "Status": "Retention",
            "MinimumRetention": {"Days": 10},
            "DefaultRetention": {"Days": 100},
            "MaximumRetention": {"Days": 1000}
        }

        cos.put_bucket_protection_configuration(Bucket=bucket_name, ProtectionConfiguration=new_protection_config)

        print("Protection added to bucket {0}\n".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to set bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addProtectionConfigurationToBucket(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    cos.setBucketProtection(bucketName, newConfig);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}

public static void addProtectionConfigurationToBucketWithRequest(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    SetBucketProtectionConfigurationRequest newRequest = new SetBucketProtectionConfigurationRequest()
        .withBucketName(bucketName)
        .withProtectionConfiguration(newConfig);

    cos.setBucketProtectionConfiguration(newRequest);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}
```
{: codeblock}
{: java}

### Comprobación de la política de retención en un grupo
{: #immutable-sdk-get}

Esta implementación de una operación GET capta los parámetros de retención de un grupo existente. 
{: http}

**Sintaxis**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```xml
GET /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
Content-Type: text/plain
Host: 67.228.254.193
Example response
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.13.1
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

Si no hay ninguna configuración de protección en el grupo, el servidor responde en su lugar con el estado inhabilitado.
{: http}

```xml
<ProtectionConfiguration>
  <Status>Disabled</Status>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

```py
def get_protection_configuration_on_bucket(bucket_name):
    try:
        response = cos.get_bucket_protection_configuration(Bucket=bucket_name)
        protection_config = response.get("ProtectionConfiguration")

        print("Bucket protection config for {0}\n".format(bucket_name))
        print(protection_config)
        print("\n")
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to get bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void getProtectionConfigurationOnBucket(String bucketName) {
    System.out.printf("Retrieving protection configuration from bucket: %s\n", bucketName;

    BucketProtectionConfiguration config = cos.getBucketProtection(bucketName);

    String status = config.getStatus();

    System.out.printf("Status: %s\n", status);

    if (!status.toUpperCase().equals("DISABLED")) {
        System.out.printf("Minimum Retention (Days): %s\n", config.getMinimumRetentionInDays());
        System.out.printf("Default Retention (Days): %s\n", config.getDefaultRetentionInDays());
        System.out.printf("Maximum Retention (Days): %s\n", config.getMaximumRetentionInDays());
    }
}
```
{: codeblock}
{: java}

### Carga de un objeto a un grupo con una política de retención
{: #immutable-sdk-upload}

Esta mejora de la operación `PUT` añade tres nuevas cabeceras de solicitud: dos para especificar el periodo de retención de diferentes maneras, y una para añadir una única retención legal al nuevo objeto. Se definen nuevos errores para los valores no permitidos para las nuevas cabeceras y, si un objeto está bajo retención, cualquier operación de sobrescritura fallará.
{: http}

Los objetos de grupos con una política de retención que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.

Se necesita una cabecera `Content-MD5`.
{: http}

Estas cabeceras también se aplican también al objeto POST y a las solicitudes de carga de varias partes. Si un objeto se carga en varias partes, cada parte necesita una cabecera `Content-MD5`.
{: http}

|Valor	| Tipo	| Descripción |
| --- | --- | --- | 
|`Retention-Period` | Entero no negativo (segundos) | Periodo de retención para almacenar el objeto en segundos. El objeto no se puede sobrescribir ni suprimir hasta que transcurre el periodo de tiempo especificado en el período de retención. Si se especifica este campo y `Retention-Expiration-Date`, se devuelve el error `400`. Si no se especifica ninguno de los dos, se utiliza el periodo `DefaultRetention` del grupo. Cero (`0`) es un valor válido, siempre y cuando el periodo mínimo de retención del grupo también sea `0`. |
| `Retention-expiration-date` | Fecha (formato ISO 8601) | Fecha en la que se podrá suprimir o modificar el objeto. Solo puede especificar esta cabecera o la cabecera Retention-Period. Si se especifican ambas, se devuelve el error `400`. Si no se especifica ninguna de los dos, se utiliza el periodo DefaultRetention del grupo. |
| `Retention-legal-hold-id` | serie | Una sola retención legal que se aplicará al objeto. Una retención legal es una serie larga de caracteres Y. El objeto no se puede sobrescribir ni suprimir hasta que se eliminen todas las retenciones legales asociadas con el objeto. |

```py
def put_object_add_legal_hold(bucket_name, object_name, file_text, legal_hold_id):
    print("Add legal hold {0} to {1} in bucket {2} with a putObject operation.\n".format(legal_hold_id, object_name, bucket_name))
    
    cos.put_object(
        Bucket=bucket_name,
        Key=object_name,
        Body=file_text,
        RetentionLegalHoldId=legal_hold_id)

    print("Legal hold {0} added to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))
  
def copy_protected_object(source_bucket_name, source_object_name, destination_bucket_name, new_object_name):
    print("Copy protected object {0} from bucket {1} to {2}/{3}.\n".format(source_object_name, source_bucket_name, destination_bucket_name, new_object_name))

    copy_source = {
        "Bucket": source_bucket_name,
        "Key": source_object_name
    }

    cos.copy_object(
        Bucket=destination_bucket_name,
        Key=new_object_name,
        CopySource=copy_source,
        RetentionDirective="Copy"
    )

    print("Protected object copied from {0}/{1} to {2}/{3}\n".format(source_bucket_name, source_object_name, destination_bucket_name, new_object_name));

def complete_multipart_upload_with_retention(bucket_name, object_name, upload_id, retention_period):
    print("Completing multi-part upload for object {0} in bucket {1}\n".format(object_name, bucket_name))

    cos.complete_multipart_upload(
        Bucket=bucket_name,
        Key=object_name,
        MultipartUpload={
            "Parts":[{
                "ETag": part["ETag"],
                "PartNumber": 1
            }]
        },
        UploadId=upload_id,
        RetentionPeriod=retention_period
    )

    print("Multi-part upload completed for object {0} in bucket {1}\n".format(object_name, bucket_name))

def upload_file_with_retention(bucket_name, object_name, path_to_file, retention_period):
    print("Uploading file {0} to object {1} in bucket {2}\n".format(path_to_file, object_name, bucket_name))
    
    args = {
        "RetentionPeriod": retention_period
    }

    cos.upload_file(
        Filename=path_to_file,
        Bucket=bucket_name,
        Key=object_name,
        ExtraArgs=args
    )

    print("File upload complete to object {0} in bucket {1}\n".format(object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void putObjectAddLegalHold(String bucketName, String objectName, String fileText, String legalHoldId) {
    System.out.printf("Add legal hold %s to %s in bucket %s with a putObject operation.\n", legalHoldId, objectName, bucketName);

    InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileText.length());

    PutObjectRequest req = new PutObjectRequest(
        bucketName,
        objectName,
        newStream,
        metadata
    );
    req.setRetentionLegalHoldId(legalHoldId);

    cos.putObject(req);

    System.out.printf("Legal hold %s added to object %s in bucket %s\n", legalHoldId, objectName, bucketName);
}

public static void copyProtectedObject(String sourceBucketName, String sourceObjectName, String destinationBucketName, String newObjectName) {
    System.out.printf("Copy protected object %s from bucket %s to %s/%s.\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);

    CopyObjectRequest req = new CopyObjectRequest(
        sourceBucketName,
        sourceObjectName,
        destinationBucketName,
        newObjectName
    );
    req.setRetentionDirective(RetentionDirective.COPY);
    

    cos.copyObject(req);

    System.out.printf("Protected object copied from %s/%s to %s/%s\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);
}
```
{: codeblock}
{: java}

### Adición de una retención legal a un objeto o eliminación de una retención legal del mismo
{: #immutable-sdk-legal-hold}

Esta implementación de la operación `POST` utiliza el parámetro de consulta `legalHold` y los parámetros de consulta `add` y `remove` para añadir o eliminar una única retención legal de un objeto protegido de un grupo protegido.
{: http}

El objeto da soporte a 100 retenciones legales:

*  Un identificador de retención legal es una serie de caracteres con una longitud máxima de 64 caracteres y una longitud mínima de 1 carácter. Los caracteres válidos son letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` y `.
* Si la adición de la retención legal especificada supera las 100 retenciones legales en total del objeto, la nueva retención legal no se añade y se devuelve el error `400`.
* Si un identificador es demasiado largo, no se añade al objeto y se devuelve el error `400`.
* Si un identificador contiene caracteres no válidos, no se añade al objeto y se devuelve el error `400`.
* Si un identificador ya se está utilizando sobre un objeto, la retención legal existente no se modifica y la respuesta indica que el identificador ya se está utilizando con el error `409`.
* Si un objeto no tiene metadatos de periodo de retención, se devuelve el error `400` y no se permite añadir ni eliminar una retención legal.

Es necesario que haya una cabecera de periodo de retención; de lo contrario, se devuelve el error `400`.
{: http}

El usuario que añade o elimina una retención legal debe tener el permiso de `Gestor` sobre el grupo.

Se necesita una cabecera `Content-MD5`. Esta operación no utiliza elementos de carga útil específicos de la operación.
{: http}

**Sintaxis**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Solicitud de ejemplo**
{: http}

```
POST /BucketName/ObjectName?legalHold&add=legalHoldID HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
```
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}

```py
def add_legal_hold_to_object(bucket_name, object_name, legal_hold_id):
    print("Adding legal hold {0} to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.add_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} added to object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))

def delete_legal_hold_from_object(bucket_name, object_name, legal_hold_id):
    print("Deleting legal hold {0} from object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.delete_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} deleted from object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addLegalHoldToObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Adding legal hold %s to object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.addLegalHold(
        bucketName,
        objectName,
        legalHoldId
    );

    System.out.printf("Legal hold %s added to object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}

public static void deleteLegalHoldFromObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Deleting legal hold %s from object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.deleteLegalHold(
        bucketName,
        objectName,
        legalHoldId
    );

    System.out.printf("Legal hold %s deleted from object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}
```
{: codeblock}
{: java}

### Ampliación del periodo de retención de un objeto
{: #immutable-sdk-extend}

Esta implementación de la operación `POST` utiliza el parámetro de consulta `extendRetention` para ampliar el periodo de retención de un objeto protegido en un grupo protegido.
{: http}

El periodo de retención de un objeto solo se puede ampliar. No se puede reducir con respecto al valor configurado actualmente.

El valor de ampliación de retención se establece de una de las tres maneras siguientes:

* tiempo adicional a partir del valor actual (`Additional-Retention-Period` o un método similar)
* nuevo periodo de ampliación en segundos (`Extend-Retention-From-Current-Time` o un método similar)
* nueva fecha de caducidad de retención del objeto (`New-Retention-Expiration-Date` o método similar)

El periodo de retención actual almacenado en los metadatos de objeto se incrementa en el tiempo adicional especificado o bien se sustituye por el nuevo valor, en función del parámetro establecido en la solicitud `extendRetention`. En cualquiera de los casos, el parámetro de retención de ampliación se comprueba con respecto al periodo de retención actual y el parámetro ampliado solo se acepta si el periodo de retención actualizado es mayor que el periodo de retención actual.

Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.

**Sintaxis**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**Solicitud de ejemplo**
{: http}

```yaml
POST /BucketName/ObjectName?extendRetention HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00GMT
Authorization: authorization string
Content-Type: text/plain
Additional-Retention-Period: 31470552
```
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:50:00GMT
Connection: close
```
{: codeblock}
{: http}

```py
def extend_retention_period_on_object(bucket_name, object_name, additional_seconds):
    print("Extend the retention period on {0} in bucket {1} by {2} seconds.\n".format(object_name, bucket_name, additional_seconds))

    cos.extend_object_retention(
        Bucket=bucket_ame,
        Key=object_name,
        AdditionalRetentionPeriod=additional_seconds
    )

    print("New retention period on {0} is {1}\n".format(object_name, additional_seconds))
```
{: codeblock}
{: python}

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void extendRetentionPeriodOnObject(String bucketName, String objectName, Long additionalSeconds) {
    System.out.printf("Extend the retention period on %s in bucket %s by %s seconds.\n", objectName, bucketName, additionalSeconds);

    ExtendObjectRetentionRequest req = new ExtendObjectRetentionRequest(
        bucketName,
        objectName)
        .withAdditionalRetentionPeriod(additionalSeconds);

    cos.extendObjectRetention(req);

    System.out.printf("New retention period on %s is %s\n", objectName, additionalSeconds);
}
```
{: codeblock}
{: java}

### Obtención de una lista de las retenciones legales sobre un objeto
{: #immutable-sdk-list-holds}

Esta implementación de la operación `GET` utiliza el parámetro de consulta `legalHold` para devolver la lista de retenciones legales sobre un objeto y el estado de retención relacionado en el cuerpo de una respuesta XML.
{: http}

Esta operación devuelve:

* Fecha de creación del objeto
* Periodo de retención del objeto en segundos
* Fecha de caducidad de retención calculada en función del periodo y de la fecha de creación
* Lista de retenciones legales
* Identificador de retención legal
* Indicación de fecha y hora en que se ha aplicado la retención legal

Si no hay ninguna retención legal sobre el objeto, se devuelve un `LegalHoldSet` vacío.
Si no se ha especificado ningún periodo de retención sobre el objeto, se devuelve el error `404`.

**Sintaxis**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Solicitud de ejemplo**
{: http}

```
GET /BucketName/ObjectName?legalHold HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: {authorization-string}
Content-Type: text/plain
```
{: codeblock}
{: http}

**Respuesta de ejemplo**
{: http}

```xml
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
<?xml version="1.0" encoding="UTF-8"?>
<RetentionState>
  <CreateTime>Fri, 8 Sep 2018 21:33:08 GMT</CreateTime>
  <RetentionPeriod>220752000</RetentionPeriod>
  <RetentionPeriodExpirationDate>Fri, 1 Sep 2023 21:33:08
GMT</RetentionPeriodExpirationDate>
  <LegalHoldSet>
    <LegalHold>
      <ID>SomeLegalHoldID</ID>
      <Date>Fri, 8 Sep 2018 23:13:18 GMT</Date>
    </LegalHold>
    <LegalHold>
    ...
    </LegalHold>
  </LegalHoldSet>
</RetentionState>
```
{: codeblock}
{: http}

```py 
def list_legal_holds_on_object(bucket_name, object_name):
    print("List all legal holds on object {0} in bucket {1}\n".format(object_name, bucket_name));

    response = cos.list_legal_holds(
        Bucket=bucket_name,
        Key=object_name
    )

    print("Legal holds on bucket {0}: {1}\n".format(bucket_name, response))
```
{: codeblock}
{: python}

```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void listLegalHoldsOnObject(String bucketName, String objectName) {
    System.out.printf("List all legal holds on object %s in bucket %s\n", objectName, bucketName);

    ListLegalHoldsResult result = cos.listLegalHolds(
        bucketName,
        objectName
    );

    System.out.printf("Legal holds on bucket %s: \n", bucketName);

    List<LegalHold> holds = result.getLegalHolds();
    for (LegalHold hold : holds) {
        System.out.printf("Legal Hold: %s", hold);
    }
}
```
{: codeblock}
{: java}
