---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: node, javascript, sdk

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

# Utilización de Node.js
{: #node}

## Instalación del SDK
{: #node-install}

La forma recomendada de instalar el SDK de {{site.data.keyword.cos_full}} para Node.js consiste en utilizar el gestor de paquetes [`npm`](https://www.npmjs.com){:new_window} para Node.js. Escriba el mandato siguiente en una línea de mandatos:

```sh
npm install ibm-cos-sdk
```

El código fuente se encuentra en [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window}.

Encontrará más detalles sobre los métodos y clases individuales en [la documentación de la API para el SDK](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}.

## Iniciación
{: #node-gs}

### Requisitos mínimos
{: #node-gs-prereqs}

Para ejecutar el SDK, necesita **Node 4.x+**.

### Creación de credenciales de cliente y de origen
{: #node-gs-credentials}

Para conectarse a COS, se crea y se configura un cliente proporcionando información de credenciales (clave de API, ID de instancia de servicio y punto final de autenticación de IBM). Estos valores también se pueden tomar automáticamente de un archivo de credenciales o de variables de entorno.

Después de generar una [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), el documento JSON resultante se puede guardar en `~/.bluemix/cos_credentials`. El SDK tomará automáticamente las credenciales de este archivo, a menos que se establezcan explícitamente otras credenciales durante la creación del cliente. Si el archivo `cos_credentials` contiene claves de HMAC, el cliente se autentica con una firma; de lo contrario, el cliente utiliza la clave de API proporcionada para autenticarse con una señal de portadora.

La cabecera de sección `default` especifica un perfil predeterminado y los valores asociados para credenciales. Puede crear más perfiles en el mismo archivo de configuración compartido, cada uno con su propia información de credenciales. En el ejemplo siguiente se muestra un archivo de configuración con el perfil predeterminado:
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

Si se migra desde AWS S3, también puede obtener los datos de credenciales de origen de `~/.aws/credentials` en el formato:

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

Si existen tanto `~/.bluemix/cos_credentials` como `~/.aws/credentials`, prevalece `cos_credentials`.

## Ejemplos de código
{: #node-examples}

### Inicialización de la configuración
{: #node-examples-init}

```javascript
const AWS = require('ibm-cos-sdk');

var config = {
    endpoint: '<endpoint>',
    apiKeyId: '<api-key>',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: '<resource-instance-id>',
};

var cos = new AWS.S3(config);
```
*Valores de clave*
* `<endpoint>`: punto final público para Cloud Object Storage (disponible en el [panel de control de IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>`: clave de API que se genera cuando se crean las credenciales de servicio (se necesita acceso de escritura para los ejemplos de creación y supresión)
* `<resource-instance-id>`: ID de recurso para Cloud Object Storage (disponible en el [CLI de IBM Cloud](/docs/overview?topic=overview-crn) o en el [panel de control de IBM Cloud](https://cloud.ibm.com/resources){:new_window})

### Creación de un grupo
{: #node-examples-new-bucket}

Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```javascript
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: 'us-standard'
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```


*Referencias del SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### Creación de un objeto de texto
{: #node-examples-new-file}

```javascript
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName,
        Key: itemName,
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### Obtención de una lista de grupos
{: #node-examples-list-buckets}

```javascript
function getBuckets() {
    console.log('Retrieving list of buckets');
    return cos.listBuckets()
    .promise()
    .then((data) => {
        if (data.Buckets != null) {
            for (var i = 0; i < data.Buckets.length; i++) {
                console.log(`Bucket Name: ${data.Buckets[i].Name}`);
            }
        }
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### Obtención de la lista de elementos de un grupo
{: #node-examples-list-objects}

```javascript
function getBucketContents(bucketName) {
    console.log(`Retrieving bucket contents from: ${bucketName}`);
    return cos.listObjects(
        {Bucket: bucketName},
    ).promise()
    .then((data) => {
        if (data != null && data.Contents != null) {
            for (var i = 0; i < data.Contents.length; i++) {
                var itemKey = data.Contents[i].Key;
                var itemSize = data.Contents[i].Size;
                console.log(`Item: ${itemKey} (${itemSize} bytes).`)
            }
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### Obtención del contenido de archivo de un elemento determinado
{: #node-examples-get-contents}

```javascript
function getItem(bucketName, itemName) {
    console.log(`Retrieving item from bucket: ${bucketName}, key: ${itemName}`);
    return cos.getObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log('File Contents: ' + Buffer.from(data.Body).toString());
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### Supresión de un elemento de un grupo
{: #node-examples-delete-object}

```javascript
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() =>{
        console.log(`Item: ${itemName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*Referencias del SDK*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### Supresión de varios elementos de un grupo
{: #node-examples-multidelete}

La solicitud de supresión puede contener un máximo de 1000 claves que desea suprimir. Si bien la supresión de objetos por lotes es muy útil para reducir la sobrecarga por solicitud, cuando suprima varias claves tenga en cuenta que la solicitud puede tardar en completarse. Tenga también en cuenta los tamaños de los objetos para garantizar un rendimiento adecuado.
{:tip}

```javascript
function deleteItems(bucketName) {
    var deleteRequest = {
        "Objects": [
            { "Key": "deletetest/testfile1.txt" },
            { "Key": "deletetest/testfile2.txt" },
            { "Key": "deletetest/testfile3.txt" },
            { "Key": "deletetest/testfile4.txt" },
            { "Key": "deletetest/testfile5.txt" }
        ]
    }
    return cos.deleteObjects({
        Bucket: bucketName,
        Delete: deleteRequest
    }).promise()
    .then((data) => {
        console.log(`Deleted items for ${bucketName}`);
        console.log(data.Deleted);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### Supresión de un grupo
{: #node-examples-delete-bucket}

```javascript
function deleteBucket(bucketName) {
    console.log(`Deleting bucket: ${bucketName}`);
    return cos.deleteBucket({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucketName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### Ejecución de una carga de varias partes
{: #node-examples-multipart}

```javascript
function multiPartUpload(bucketName, itemName, filePath) {
    var uploadID = null;

    if (!fs.existsSync(filePath)) {
        log.error(new Error(`The file \'${filePath}\' does not exist or is not accessible.`));
        return;
    }

    console.log(`Starting multi-part upload for ${itemName} to bucket: ${bucketName}`);
    return cos.createMultipartUpload({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        uploadID = data.UploadId;

        //iniciar la carga del archivo
            fs.readFile(filePath, (e, fileData) => {
            //tamaño mínimo de parte de 5 MB
                var partSize = 1024 * 1024 * 5;
                var partCount = Math.ceil(fileData.length / partSize);
    
            async.timesSeries(partCount, (partNum, next) => {
                var start = partNum * partSize;
                var end = Math.min(start + partSize, fileData.length);
    
                partNum++;

                console.log(`Uploading to ${itemName} (part ${partNum} of ${partCount})`);  

                cos.uploadPart({
                    Body: fileData.slice(start, end),
                    Bucket: bucketName,
                    Key: itemName,
                    PartNumber: partNum,
                    UploadId: uploadID
                }).promise()
                .then((data) => {
                    next(e, {ETag: data.ETag, PartNumber: partNum});
                })
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            }, (e, dataPacks) => {
                cos.completeMultipartUpload({
                    Bucket: bucketName,
                    Key: itemName,
                    MultipartUpload: {
                        Parts: dataPacks
                    },
                    UploadId: uploadID
                }).promise()
                .then(console.log(`Upload of all ${partCount} parts of ${itemName} successful.`))
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            });
        });
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function cancelMultiPartUpload(bucketName, itemName, uploadID) {
    return cos.abortMultipartUpload({
        Bucket: bucketName,
        Key: itemName,
        UploadId: uploadID
    }).promise()
    .then(() => {
        console.log(`Multi-part upload aborted for ${itemName}`);
    })
    .catch((e)=>{
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Utilización de Key Protect
{: #node-examples-kp}

Key Protect se puede añadir a un grupo de almacenamiento para gestionar claves de cifrado. Todos los datos están cifrados en IBM COS, pero Key Protect proporciona un servicio para generar, rotar y controlar el acceso a las claves de cifrado utilizando un servicio centralizado.

### Antes de empezar
{: #node-examples-kp-prereqs}
Se necesitan los elementos siguientes para crear un grupo con Key Protect habilitado:

* Un servicio de Key Protect [suministrado](/docs/services/key-protect?topic=key-protect-provision#provision)
* Una clave raíz disponible (ya sea [generada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperación del CRN de la clave raíz
{: #node-examples-kp-root}

1. Recupere el [ID de instancia](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) del servicio Key Protect
2. Utilice la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas las [claves disponibles](https://cloud.ibm.com/apidocs/key-protect)
    * Puede utilizar mandatos `curl` o un cliente de API REST, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acceder a la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere el CRN de la clave raíz que utilizará para activar Key Protect en el grupo. El CRN se parecerá al siguiente:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creación de un grupo con Key Protect habilitado
{: #node-examples-kp-new-bucket}

```javascript
function createBucketKP(bucketName) {
    console.log(`Creating new encrypted bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: '<bucket-location>'
        },
        IBMSSEKPEncryptionAlgorithm: '<algorithm>',
        IBMSSEKPCustomerRootKeyCrn: '<root-key-crn>'
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
        logDone();
    }))
    .catch(logError);
}
```
*Valores de clave*
* `<bucket-location>`: región o ubicación del grupo (Key Protect solo está disponible en determinadas regiones. Asegúrese de que su ubicación coincide con el servicio de Key Protect). Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).
* `<algorithm>`: el algoritmo de cifrado utilizado para los nuevos objetos que se añaden al grupo (el valor predeterminado es AES256).
* `<root-key-crn>`: el CRN de la clave raíz que se ha obtenido del servicio Key Protect.

*Referencias del SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## Utilización de la característica de archivado
{: #node-examples-archive}

El nivel de archivado permite a los usuarios archivar datos obsoletos y reducir los costes de almacenamiento. Se crean políticas de archivado (también denominadas *configuraciones de ciclo de vida*) para los grupos y se aplican a los objetos que se añaden al grupo después de que se cree la política.

### Visualización de la configuración del ciclo de vida de un grupo
{: #node-examples-get-lifecycle}

```javascript
function getLifecycleConfiguration(bucketName) {
    return cos.getBucketLifecycleConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log(`Retrieving bucket lifecycle config from: ${bucketName}`);
            console.log(JSON.stringify(data, null, 4));
        }
        else {
            console.log(`No lifecycle configuration for ${bucketName}`);
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Creación de una configuración de ciclo de vida 
{: #node-examples-put-lifecycle}

Encontrará información detallada sobre la estructura de las reglas de configuración del ciclo de vida en la publicación [Consulta de API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)

```javascript
function createLifecycleConfiguration(bucketName) {
    //
    var config = {
        Rules: [{
            Status: 'Enabled',
            ID: '<policy-id>',
            Filter: {
                Prefix: ''
            },
            Transitions: [{
                Days: <number-of-days>,
                StorageClass: 'GLACIER'
            }]
        }]
    };
    
    return cos.putBucketLifecycleConfiguration({
        Bucket: bucketName,
        LifecycleConfiguration: config
    }).promise()
    .then(() => {
        console.log(`Created bucket lifecycle config for: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Valores de clave*
* `<policy-id>`: nombre de la política de ciclo de vida (debe ser exclusivo)
* `<number-of-days>`: número de días que se va a conservar el archivo restaurado

*Referencias del SDK*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Supresión de la configuración del ciclo de vida de un grupo
{: #node-examples-delete-lifecycle}

```javascript
function deleteLifecycleConfiguration(bucketName) {
    return cos.deleteBucketLifecycle({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Deleted bucket lifecycle config from: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Restauración temporal de un objeto
{: #node-examples-restore-object}

Encontrará información detallada sobre los parámetros de la solicitud de restauración en la publicación [Consulta de API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore)

```javascript
function restoreItem(bucketName, itemName) {
    var params = {
        Bucket: bucketName, 
        Key: itemName, 
        RestoreRequest: {
            Days: <number-of-days>, 
            GlacierJobParameters: {
                Tier: 'Bulk' 
            },
        } 
    };
    
    return cos.restoreObject(params).promise()
    .then(() => {
        console.log(`Restoring item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Valores de clave*
* `<number-of-days>`: número de días que se va a conservar el archivo restaurado

*Referencias del SDK*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Visualización de información de HEAD para un objeto
{: #node-examples-lifecycle-head-object}
```javascript
function getHEADItem(bucketName, itemName) {
    return cos.headObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        console.log(`Retrieving HEAD for item: ${itemName} from bucket: ${bucketName}`);
        console.log(JSON.stringify(data, null, 4));
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referencias del SDK*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## Actualización de metadatos
{: #node-examples-metadata}

Hay dos formas de actualizar los metadatos de un objeto existente:
* Una solicitud `PUT` con los nuevos metadatos y el contenido del objeto original
* Ejecución de una solicitud `COPY` con los nuevos metadatos que especifican el objeto original como origen de la copia

### Utilización de PUT para actualizar metadatos
{: #node-examples-metadata-put}

**Nota:** la solicitud `PUT` sobrescribe el contenido existente del objeto, por lo que primero se debe descargar y volver a cargar con los nuevos metadatos


```javascript
function updateMetadataPut(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //recuperar el elemento existente para volver a cargar el contenido
    return cos.getObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        //definir los nuevos metadatos
        var newMetadata = {
            newkey: metaValue
        };

        return cos.putObject({
            Bucket: bucketName,
            Key: itemName,
            Body: data.Body,
            Metadata: newMetadata
        }).promise()
        .then(() => {
            console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
        })
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

### Utilización de COPY para actualizar metadatos
{: #node-examples-metadata-copy}

```javascript
function updateMetadataCopy(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //definir el origen de la copia en sí mismo
    var copySource = bucketName + '/' + itemName;

    //definir los nuevos metadatos
        var newMetadata = {
        newkey: metaValue
    };

    return cos.copyObject({
        Bucket: bucketName,
        Key: itemName,
        CopySource: copySource,
        Metadata: newMetadata,
        MetadataDirective: 'REPLACE'
    }).promise()
    .then((data) => {
        console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

## Utilización de Immutable Object Storage
{: #node-examples-immutable}

### Adición de una configuración de protección a un grupo existente
{: #node-examples-immutable-add}

Los objetos que se escriben en un grupo protegido no se pueden suprimir hasta que transcurre el periodo de protección y se eliminan todas las retenciones legales sobre el objeto. Se proporciona el valor de retención predeterminado del grupo a un objeto a menos que se proporcione un valor específico de objeto cuando se crea el objeto. Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto. 

Los valores mínimos y máximos admitidos para los valores de periodo de retención `MinimumRetention`, `DefaultRetention` y `MaximumRetention` son de 0 días y 365243 días (1000 años) respectivamente. 


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

### Comprobación de la protección sobre un grupo
{: #node-examples-immutable-check}

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

### Carga de un objeto protegido
{: #node-examples-immutable-upload}

Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.

|Valor	| Tipo	| Descripción |
| --- | --- | --- | 
|`Retention-Period` | Entero no negativo (segundos) | Periodo de retención para almacenar el objeto en segundos. El objeto no se puede sobrescribir ni suprimir hasta que transcurre el periodo de tiempo especificado en el período de retención. Si se especifica este campo y `Retention-Expiration-Date`, se devuelve el error `400`. Si no se especifica ninguno de los dos, se utiliza el periodo `DefaultRetention` del grupo. Cero (`0`) es un valor válido, siempre y cuando el periodo mínimo de retención del grupo también sea `0`. |
| `Retention-expiration-date` | Fecha (formato ISO 8601) | Fecha en la que se podrá suprimir o modificar el objeto. Solo puede especificar esta cabecera o la cabecera Retention-Period. Si se especifican ambas, se devuelve el error `400`. Si no se especifica ninguna de los dos, se utiliza el periodo DefaultRetention del grupo. |
| `Retention-legal-hold-id` | serie | Una sola retención legal que se aplicará al objeto. Una retención legal es una serie larga de caracteres Y. El objeto no se puede sobrescribir ni suprimir hasta que se eliminen todas las retenciones legales asociadas con el objeto. |

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

### Adición o eliminación de una retención legal de un objeto protegido
{: #node-examples-immutable-legal-hold}

El objeto da soporte a 100 retenciones legales:

*  Un identificador de retención legal es una serie de caracteres con una longitud máxima de 64 caracteres y una longitud mínima de 1 carácter. Los caracteres válidos son letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` y `.
* Si la adición de la retención legal especificada supera las 100 retenciones legales en total del objeto, la nueva retención legal no se añade y se devuelve el error `400`.
* Si un identificador es demasiado largo, no se añade al objeto y se devuelve el error `400`.
* Si un identificador contiene caracteres no válidos, no se añade al objeto y se devuelve el error `400`.
* Si un identificador ya se está utilizando sobre un objeto, la retención legal existente no se modifica y la respuesta indica que el identificador ya se está utilizando con el error `409`.
* Si un objeto no tiene metadatos de periodo de retención, se devuelve el error `400` y no se permite añadir ni eliminar una retención legal.

El usuario que añade o elimina una retención legal debe tener el permiso de `Gestor` sobre el grupo.

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

### Ampliación del periodo de retención de un objeto protegido
{: #node-examples-immutable-extend}

El periodo de retención de un objeto solo se puede ampliar. No se puede reducir con respecto al valor configurado actualmente.

El valor de ampliación de retención se establece de una de las tres maneras siguientes:

* tiempo adicional a partir del valor actual (`Additional-Retention-Period` o un método similar)
* nuevo periodo de ampliación en segundos (`Extend-Retention-From-Current-Time` o un método similar)
* nueva fecha de caducidad de retención del objeto (`New-Retention-Expiration-Date` o método similar)

El periodo de retención actual almacenado en los metadatos de objeto se incrementa en el tiempo adicional especificado o bien se sustituye por el nuevo valor, en función del parámetro establecido en la solicitud `extendRetention`. En cualquiera de los casos, el parámetro de retención de ampliación se comprueba con respecto al periodo de retención actual y el parámetro ampliado solo se acepta si el periodo de retención actualizado es mayor que el periodo de retención actual.

Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.

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

### Obtención de una lista de las retenciones legales sobre un objeto protegido
{: #node-examples-immutable-list-holds}

Esta operación devuelve:

* Fecha de creación del objeto
* Periodo de retención del objeto en segundos
* Fecha de caducidad de retención calculada en función del periodo y de la fecha de creación
* Lista de retenciones legales
* Identificador de retención legal
* Indicación de fecha y hora en que se ha aplicado la retención legal

Si no hay ninguna retención legal sobre el objeto, se devuelve un `LegalHoldSet` vacío.
Si no se ha especificado ningún periodo de retención sobre el objeto, se devuelve el error `404`.


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
