---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: python, sdk

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

# Utilización de Python
{: #python}

El soporte de Python se proporciona a través de una bifurcación de la biblioteca `boto3`. Se puede instalar desde el índice de paquetes de Python con `pip install ibm-cos-sdk`.

Encontrará el código fuente en [GitHub](https://github.com/ibm/ibm-cos-sdk-python/).

La biblioteca `ibm_boto3` ofrece acceso completo a la API de {{site.data.keyword.cos_full}}. Los puntos finales, una clave de API y el ID de instancia se deben especificar durante la creación de un recurso de servicio o un cliente de bajo nivel, tal como se muestra en los siguientes ejemplos básicos.

El ID de instancia de servicio también se conoce como _ID de instancia de recurso_. Puede encontrar el valor creando una [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) o a través de la CLI.
{:tip}

[Aquí](https://ibm.github.io/ibm-cos-sdk-python/) encontrará documentación detallada.

## Actualización desde 1.x.x
{: #python-migrate}

La versión 2.0 del SDK incorpora un cambio en los espacios de nombres que permite que una aplicación utilice la biblioteca `boto3` original para conectar con recursos de AWS dentro de la misma aplicación o el mismo entorno. Para migrar de 1.x a 2.0, es necesario realizar algunos cambios.

    1. Actualice `requirements.txt`, o desde PyPI mediante `pip install -U ibm-cos-sdk`. Verifique que no existe ninguna versión anterior con `pip list | grep ibm-cos`.
    2. Actualice las declaraciones de importación `boto3` por `ibm_boto3`.
    3. Si es necesario, vuelva a instalar el `boto3` original actualizando el archivo `requirements.txt` o desde PyPI con `pip install boto3`.

## Creación de credenciales de cliente y de origen
{: #python-credentials}

Para conectarse a COS, se crea y se configura un cliente proporcionando información de credenciales (clave de API e ID de instancia de servicio). Estos valores también se pueden tomar automáticamente de un archivo de credenciales o de variables de entorno.

Después de generar una [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), el documento JSON resultante se puede guardar en `~/.bluemix/cos_credentials`. El SDK tomará automáticamente las credenciales de este archivo, a menos que se establezcan explícitamente otras credenciales durante la creación del cliente. Si el archivo `cos_credentials` contiene claves de HMAC, el cliente se autentica con una firma; de lo contrario, el cliente utiliza la clave de API proporcionada para autenticarse mediante una señal de portadora.

Si se migra desde AWS S3, también puede obtener los datos de credenciales de origen de `~/.aws/credentials` en el formato:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Si existen tanto `~/.bluemix/cos_credentials` como `~/.aws/credentials`, prevalece `cos_credentials`.

### Obtención de la información necesaria
{: #python-prereqs}

En los ejemplos aparecen las variables siguientes:

* `bucket_name` debe ser una serie [exclusiva y DNS segura](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Puesto que los nombres de grupo son exclusivos en todo el sistema, estos valores se tienen que modificar si este ejemplo se ejecuta varias veces. Tenga en cuenta que los nombres se reservan durante 10-15 minutos tras su supresión.
* `ibm_api_key_id` es el valor que se encuentra en la [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `apikey`.
* `ibm_service_instance_id` es el valor que se encuentra en la [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `resource_instance_id`. 
* `endpoint_url` es un URL de punto final de servicio, incluido el protocolo `https://`. **No** es
el valor de `endpoints` que se encuentra en la [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `LocationConstraint` es un [código de suministro válido](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) que se corresponde con el valor de `endpoint`. 


## Ejemplos de código
{: #python-examples}

Los ejemplos de código se han escrito con **Python 2.7.15**

### Inicialización de la configuración
{: #python-examples-init}

  
```python
import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Constantes correspondientes a valores de IBM COS
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>" # eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Crear recurso
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)
```
*Valores de clave*
* `<endpoint>`: punto final público de Cloud Object Storage con el prefijo de esquema ('https://') (disponible en el [panel de control de IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>`: clave de api que se genera cuando se crean las credenciales de servicio (se necesita acceso de escritura para los ejemplos de creación y supresión)
* `<resource-instance-id>`: ID de recurso para Cloud Object Storage (disponible en el [CLI de IBM Cloud](/docs/cli?topic=cloud-cli-idt-cli) o en el [panel de control de IBM Cloud](https://cloud.ibm.com/resources){:new_window})
* `<location>`: ubicación predeterminada para Cloud Object Storage (debe coincidir con la región utilizada para `<endpoint>`)

*Referencias del SDK*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### Creación de un nuevo grupo
{: #python-examples-new-bucket}

Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```python
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create bucket: {0}".format(e))
```

*Referencias del SDK*
* Clases
  * [`Bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Métodos
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### Creación de un nuevo archivo de texto
{: #python-examples-new-file}

```python
def create_text_file(bucket_name, item_name, file_text):
    print("Creating new item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).put(
            Body=file_text
        )
        print("Item: {0} created!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create text file: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Métodos
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### Obtención de una lista de grupos disponibles
{: #python-examples-list-buckets}

```python
def get_buckets():
    print("Retrieving list of buckets")
    try:
        buckets = cos.buckets.all()
        for bucket in buckets:
            print("Bucket Name: {0}".format(bucket.name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve list buckets: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* Colecciones
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### Obtención de la lista de elementos de un grupo
{: #python-examples-list-objects}

```python
def get_bucket_contents(bucket_name):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        files = cos.Bucket(bucket_name).objects.all()
        for file in files:
            print("Item: {0} ({1} bytes).".format(file.key, file.size))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* Colecciones
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### Obtención del contenido de archivo de un elemento determinado
{: #python-examples-get-file-contents}

```python
def get_item(bucket_name, item_name):
    print("Retrieving item from bucket: {0}, key: {1}".format(bucket_name, item_name))
    try:
        file = cos.Object(bucket_name, item_name).get()
        print("File Contents: {0}".format(file["Body"].read()))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve file contents: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Métodos
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### Supresión de un elemento de un grupo
{: #python-examples-delete-object}

```python
def delete_item(bucket_name, item_name):
    print("Deleting item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).delete()
        print("Item: {0} deleted!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete item: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Métodos
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### Supresión de varios elementos de un grupo
{: #python-examples-delete-multiple-objects}

La solicitud de supresión puede contener un máximo de 1000 claves que desea suprimir. Si bien esto resulta útil para reducir la sobrecarga por solicitud, tenga cuidado cuando vaya a suprimir un gran número de claves. Tenga también en cuenta los tamaños de los objetos para garantizar un rendimiento adecuado.
{:tip}

```python
def delete_items(bucket_name):
    try:
        delete_request = {
            "Objects": [
                { "Key": "deletetest/testfile1.txt" },
                { "Key": "deletetest/testfile2.txt" },
                { "Key": "deletetest/testfile3.txt" },
                { "Key": "deletetest/testfile4.txt" },
                { "Key": "deletetest/testfile5.txt" }
            ]
        }

        response = cos_cli.delete_objects(
            Bucket=bucket_name,
            Delete=delete_request
        )

        print("Deleted items for {0}\n".format(bucket_name))
        print(json.dumps(response.get("Deleted"), indent=4))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to copy item: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Métodos
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### Supresión de un grupo
{: #python-examples-delete-bucket}

```python
def delete_bucket(bucket_name):
    print("Deleting bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).delete()
        print("Bucket: {0} deleted!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete bucket: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Métodos
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### Ejecución de una carga de varias partes
{: #python-examples-multipart}

#### Carga del archivo binario (método recomendado)
{: #python-examples-multipart-binary}

El método [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} de la clase [S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} ejecuta automáticamente una carga de varias partes cuando es necesario. Se utiliza la clase [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} para determinar el umbral para utilizar la carga de varias partes.

```python
def multi_part_upload(bucket_name, item_name, file_path):
    try:
        print("Starting file transfer for {0} to bucket: {1}\n".format(item_name, bucket_name))
        # definir fragmentos de 5 MB
        part_size = 1024 * 1024 * 5

        # establecer umbral de 15 MB
        file_threshold = 1024 * 1024 * 15

        # establecer umbral de transferencia y tamaño de fragmento
        transfer_config = ibm_boto3.s3.transfer.TransferConfig(
            multipart_threshold=file_threshold,
            multipart_chunksize=part_size
        )

        # El método upload_fileobj ejecutará automáticamente una carga de varias partes
        # en fragmentos de 5 MB para todos los archivos de más de 15 MB
        with open(file_path, "rb") as file_data:
            cos.Object(bucket_name, item_name).upload_fileobj(
                Fileobj=file_data,
                Config=transfer_config
            )

        print("Transfer for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* Métodos
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### Ejecución manual de una carga de varias partes
{: #python-examples-multipart-manual}

Si se desea, se puede utilizar la clase [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} para realizar una carga de varias partes. Esto puede ser útil si se necesita más control sobre el proceso de carga.

```python
def multi_part_upload_manual(bucket_name, item_name, file_path):
    try:
        # crear objeto cliente
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT
        )

        print("Starting multi-part upload for {0} to bucket: {1}\n".format(item_name, bucket_name))

        # iniciar la carga de varias partes
        mp = cos_cli.create_multipart_upload(
            Bucket=bucket_name,
            Key=item_name
        )

        upload_id = mp["UploadId"]

        # tamaño mínimo de parte de 5 MB
        part_size = 1024 * 1024 * 5
        file_size = os.stat(file_path).st_size
        part_count = int(math.ceil(file_size / float(part_size)))
        data_packs = []
        position = 0
        part_num = 0

        # comenzar a cargar las partes
        with open(file_path, "rb") as file:
            for i in range(part_count):
                part_num = i + 1
                part_size = min(part_size, (file_size - position))

                print("Uploading to {0} (part {1} of {2})".format(item_name, part_num, part_count))

                file_data = file.read(part_size)

                mp_part = cos_cli.upload_part(
                    Bucket=bucket_name,
                    Key=item_name,
                    PartNumber=part_num,
                    Body=file_data,
                    ContentLength=part_size,
                    UploadId=upload_id
                )

                data_packs.append({
                    "ETag":mp_part["ETag"],
                    "PartNumber":part_num
                })

                position += part_size

        # finalizar carga
        cos_cli.complete_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id,
            MultipartUpload={
                "Parts": data_packs
            }
        )
        print("Upload for {0} Complete!\n".format(item_name))
    except ClientError as be:
        # terminar anormalmente la carga
        cos_cli.abort_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id
        )
        print("Multi-part upload aborted for {0}\n".format(item_name))
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Métodos
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### Carga de objetos grandes mediante TransferManager
{: #python-examples-multipart-transfer}

`TransferManager` proporciona otra forma de ejecutar transferencias de archivos de gran tamaño incorporando automáticamente las cargas de varias partes siempre que sea necesario mediante el establecimiento de parámetros de configuración.

```python
def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # definir el tamaño de cada parte en 5 MB
    part_size = 1024 * 1024 * 5

    # definir un umbral de 5 MB
    file_threshold = 1024 * 1024 * 5

    # Crear conexión cliente
    cos_cli = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_SERVICE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

    # definir el umbral de transferencia y el tamaño de cada parte en los valores de configuración
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # crear gestor de transferencias
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_cli, config=transfer_config)

    try:
        # iniciar carga de archivos
        future = transfer_mgr.upload(file_path, bucket_name, item_name)

        # esperar a que finalice la carga
        future.result()

        print ("Large file upload complete!")
    except Exception as e:
        print("Unable to complete large file upload: {0}".format(e))
    finally:
        transfer_mgr.shutdown()
```

### Obtención de la lista de elementos de un grupo (v2)
{: #python-examples-list-objects-v2}

El objeto [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} contiene un método actualizado para obtener una lista del contenido ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}). Este método le permite limitar el número de registros que se devuelven y recuperar los registros por lotes. Esto puede resultar útil para paginar los resultados dentro de una aplicación y mejorar el rendimiento.

```python
def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        # crear objeto cliente
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT)

        more_results = True
        next_token = ""

        while (more_results):
            response = cos_cli.list_objects_v2(Bucket=bucket_name, MaxKeys=max_keys, ContinuationToken=next_token)
            files = response["Contents"]
            for file in files:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))

            if (response["IsTruncated"]):
                next_token = response["NextContinuationToken"]
                print("...More results in next batch!\n")
            else:
                more_results = False
                next_token = ""

        log_done()
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*Referencias del SDK*
* Clases
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Métodos
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Utilización de Key Protect
{: #python-examples-kp}
Key Protect se puede añadir a un grupo de almacenamiento para cifrar los datos confidenciales en reposo en la nube.

### Antes de empezar
{: #python-examples-kp-prereqs}

Se necesitan los elementos siguientes para crear un grupo con Key Protect habilitado:

* Un servicio de Key Protect [suministrado](/docs/services/key-protect?topic=key-protect-provision)
* Una clave raíz disponible (ya sea [generada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperación del CRN de la clave raíz
{: #python-examples-kp-root}

1. Recupere el [ID de instancia](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) del servicio Key Protect
2. Utilice la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas las [claves disponibles](https://cloud.ibm.com/apidocs/key-protect)
    * Puede utilizar mandatos `curl` o un cliente de API REST, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acceder a la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere el CRN de la clave raíz que utilizará para activar Key Protect en el grupo. El CRN tiene un aspecto similar al siguiente:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creación de un grupo con Key Protect habilitado
{: #python-examples-kp-new-bucket}
```python
COS_KP_ALGORITHM = "<algorithm>"
COS_KP_ROOTKEY_CRN = "<root-key-crn>"

# Crear un nuevo grupo con key protect (cifrado)
def create_bucket_kp(bucket_name):
    print("Creating new encrypted bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            },
            IBMSSEKPEncryptionAlgorithm=COS_KP_ALGORITHM,
            IBMSSEKPCustomerRootKeyCrn=COS_KP_ROOTKEY_CRN
        )
        print("Encrypted Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create encrypted bucket: {0}".format(e))
```

*Valores de clave*
* `<algorithm>`: el algoritmo de cifrado que se utiliza para los nuevos objetos que se añaden al grupo (el valor predeterminado es AES256).
* `<root-key-crn>`: el CRN de la clave raíz que se ha obtenido del servicio Key Protect.

*Referencias del SDK*
* Clases
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Métodos
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Utilización de la transferencia de alta velocidad de Aspera
{: #python-examples-aspera}

Si instala la [biblioteca de transferencia de alta velocidad de Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), puede aprovechar las transferencias de archivos de alta velocidad dentro de la aplicación. La biblioteca de Aspera es de origen cerrado y, por lo tanto, una dependencia opcional para el SDK de COS (que utiliza una licencia de Apache).

Cada sesión de Aspera crea un proceso `ascp` individual que se ejecuta en la máquina cliente para realizar la transferencia. Asegúrese de que su entorno permita ejecutar dicho proceso.
{:tip}


### Inicialización de AsperaTransferManager
{: #python-examples-aspera-init}

Antes de inicializar `AsperaTransferManager`, asegúrese de que tiene un objeto [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} en funcionamiento (no un objeto `resource` o `session`).

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_s3transfer.aspera.manager import AsperaTransferManager

COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location>"

# Crear recurso
cos = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

transfer_manager = AsperaTransferManager(cos)
```

Tiene que proporcionar una clave de API de IAM para las transferencias de alta velocidad de Aspera. Las [credenciales de HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} **NO** reciben soporte actualmente. Para obtener más información acerca de IAM, [pulse aquí](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

Para optimizar el rendimiento, divida la transferencia en un número especificado de **sesiones** paralelas que enviarán fragmentos de datos cuyo tamaño está definido por el valor **threshold**.

La configuración típica para utilizar la multisesión sería la siguiente:
* Tasa objetivo de 2500 MBps
* Umbral de 100 MB (*este es el valor recomendado para la mayoría de aplicaciones*)

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
En el ejemplo anterior, el sdk abarcará sesiones suficientes para intentar alcanzar la tasa objetivo de 2500 MBps.

Como alternativa, se puede configurar de forma explícita la gestión de sesiones en el sdk. Esto resulta útil en los casos en los que se desea un control más preciso sobre la utilización de la red.

La configuración típica para utilizar la multisesión explícita sería la siguiente:
* 2 o 10 sesiones
* Umbral de 100 MB (*este es el valor recomendado para la mayoría de aplicaciones*)

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configurar 2 sesiones por transferencia
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Crear el gestor de transferencias de Aspera
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
Para obtener un rendimiento óptimo en la mayoría de los escenarios, utilice siempre varias sesiones para minimizar cualquier sobrecarga asociada con la creación de una instancia de una transferencia de alta velocidad de Aspera. **Si la capacidad de red es como mínimo de 1 Gbps, debe utilizar 10 sesiones.**  Las redes de ancho de banda inferior deben utilizar dos sesiones.
{:tip}

### Carga de archivos
{: #python-examples-aspera-upload}
```python
bucket_name = "<bucket-name>"
upload_filename = "<absolute-path-to-file>"
object_name = "<item-name>"

# Crear gestor de transferencias
with AsperaTransferManager(client) as transfer_manager:

    # Ejecutar carga
    future = transfer_manager.upload(upload_filename, bucket_name, object_name)

    # Esperar a que finalice la carga
    future.result()
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo de destino
* `<absolute-path-to-file>`: nombre de archivo y vía de acceso al directorio del archivo que se va a cargar
* `<item-name>`: nombre del nuevo archivo añadido al grupo

### Descarga de archivos
{: #python-examples-aspera-download}

```python
bucket_name = "<bucket-name>"
download_filename = "<absolute-path-to-file>"
object_name = "<object-to-download>"

# Crear gestor de transferencias
with AsperaTransferManager(client) as transfer_manager:

    # Obtener objeto con Aspera
    future = transfer_manager.download(bucket_name, object_name, download_filename)

    # Esperar a que finalice la descarga
    future.result()
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera.
* `<absolute-path-to-file>`: directorio y nombre de archivo donde guardar el archivo en el sistema local.
* `<object-to-download>`: nombre del archivo en el grupo que se va a descargar.

### Carga de directorios
{: #python-examples-aspera-directory-upload}

```python
bucket_name = "<bucket-name>"
# ESTE DIRECTORIO DEBE EXISTIR LOCALMENTE y debe contener objetos.
local_upload_directory = "<absolute-path-to-directory>"
# NO DEBE TENER UN SIGNO "/" INICIAL
remote_directory = "<object prefix>"

# Crear gestor de transferencias
with AsperaTransferManager(client) as transfer_manager:

    # Ejecutar carga
    future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory)

    # Esperar a que finalice la carga
    future.result()
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera
* `<absolute-path-to-directory>`: directorio local que contiene los archivos que se van a cargar. Debe tener un signo `/` inicial y final (por ejemplo, `/Users/testuser/Documents/Upload/`)
* `<object prefix>`: nombre del directorio del grupo en el que se van a almacenar los archivos. No debe tener una barra inclinada `/` inicial (por ejemplo, `newuploads/`)

### Descarga de directorios
{: #python-examples-aspera-directory-download}

```python
bucket_name = "<bucket-name>"
# ESTE DIRECTORIO DEBE EXISTIR LOCALMENTE
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Crear gestor de transferencias
with AsperaTransferManager(client) as transfer_manager:

    # Obtener objeto con Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory)

    # Esperar a que finalice la descarga
    future.result()
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera
* `<absolute-path-to-directory>`: directorio local en el que se van a guardar los archivos descargados. Debe tener una barra inclinada `/` inicial y final (por ejemplo, `/Users/testuser/Downloads/`)
* `<object prefix>`: nombre del directorio del grupo en el que se van a almacenar los archivos. No debe tener una barra inclinada `/` inicial (por ejemplo, `todownload/`)

### Utilización de suscriptores
{: #python-examples-aspera-subscribers}

Los suscriptores facilitan la observación de las transferencias, mediante la conexión de métodos personalizados de devolución de llamada. Todas las transferencias pasan por las fases siguientes:

`En cola - En curso - Finalizada`

Hay tres suscriptores disponibles para cada fase:

* `CallbackOnQueued()`: se le llama cuando se añade una nueva transferencia a `AsperaTransferManager`
* `CallbackOnProgress()`: se le llama cuando una transferencia a comenzado a transmitir datos (se activa de forma repetida mientras la transferencia está en curso).
* `CallbackOnDone()`: se le llama cuando finaliza la transferencia

```python
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Devoluciones de llamada del suscriptor
class CallbackOnQueued(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_queued(self, future, **kwargs):
        print("Directory download queued.")

class CallbackOnProgress(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_progress(self, future, bytes_transferred, **kwargs):
        print("Directory download in progress: %s bytes transferred" % bytes_transferred)

class CallbackOnDone(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_done(self, future, **kwargs):
        print("Downloads complete!")

# Crear gestor de transferencias
transfer_manager = AsperaTransferManager(client)

# Adjuntar suscriptores
subscribers = [CallbackOnQueued(), CallbackOnProgress(), CallbackOnDone()]

# Obtener objeto con Aspera
future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, subscribers)

# Esperar a que finalice la descarga
future.result()
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo en la instancia del servicio Object Storage que tiene habilitado Aspera
* `<absolute-path-to-directory>`: directorio local en el que se van a guardar los archivos descargados. Debe tener una barra inclinada `/` inicial y final (por ejemplo, `/Users/testuser/Downloads/`)
* `<object prefix>`: nombre del directorio del grupo en el que se van a almacenar los archivos. No debe tener una barra inclinada `/` inicial (por ejemplo, `todownload/`)

El código de ejemplo anterior genera la salida siguiente:

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### Pausa/Reanudación/Cancelación
{: #python-examples-aspera-pause}

El SDK proporciona la capacidad de gestionar el progreso de las transferencias de archivos/directorios mediante los métodos siguientes del objeto `AsperaTransferFuture`:

* `pause()`
* `resume()`
* `cancel()`

No hay efectos secundarios de llamar a ninguno de los métodos mostrados anteriormente. El SDK se encarga de realizar la limpieza correspondiente.
{:tip}

```python
# Crear gestor de transferencias
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

with AsperaTransferManager(client) as transfer_manager:

    # descargar un objeto con Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

    # pausar la transferencia
    future.pause()

    # reanudar la transferencia
    future.resume()

    # cancelar la transferencia
    future.cancel()
```

### Resolución de problemas de Aspera
{: #python-examples-aspera-ts}
**Problema:** los desarrolladores que utilizan Python 2.7.15 en Windows 10 pueden observar errores al instalar el SDK de Aspera.

**Causa:** si hay distintas versiones de Python instaladas en el entorno, es posible que detecte errores de instalación al intentar instalar el SDK de Aspera. Pueden deberse a que falten archivos DLL o a que haya una DLL errónea en la variable path.

**Solución:** el primer paso para solucionar este problema sería volver a instalar las bibliotecas de Aspera. Puede que se haya producido un error durante la instalación. Como resultado, esto podría haber afectado a los archivos DLL. Si eso no soluciona los problemas, tendrá que actualizar la versión de Python. Si no puede hacerlo, puede instalar [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window}. Esto le permitirá instalar el SDK de Aspera sin ningún problema.

## Actualización de metadatos
{: #python-examples-metadata}
Hay dos formas de actualizar los metadatos de un objeto existente:
* Una solicitud `PUT` con los nuevos metadatos y el contenido del objeto original
* Ejecución de una solicitud `COPY` con los nuevos metadatos que especifican el objeto original como origen de la copia

### Utilización de PUT para actualizar metadatos
{: #python-examples-metadata-put}
**Nota:** la solicitud `PUT` sobrescribe el contenido existente del objeto, por lo que primero se debe descargar y volver a cargar con los nuevos metadatos

```python
def update_metadata_put(bucket_name, item_name, key, value):
    try:
        # recuperar el elemento existente para volver a cargar el contenido
        response = cos_cli.get_object(Bucket=bucket_name, Key=item_name)
        existing_body = response.get("Body").read()

        # establecer lo nuevos metadatos
        new_metadata = {
            key: value
        }

        cos_cli.put_object(Bucket=bucket_name, Key=item_name, Body=existing_body, Metadata=new_metadata)

        print("Metadata update (PUT) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```

### Utilización de COPY para actualizar metadatos
{: #python-examples-metadata-copy}

```python
def update_metadata_copy(bucket_name, item_name, key, value):
    try:
        # establecer los nuevos metadatos
        new_metadata = {
            key: value
        }

        # establecer el origen de la copia en sí mismo
        copy_source = {
            "Bucket": bucket_name,
            "Key": item_name
        }

        cos_cli.copy_object(Bucket=bucket_name, Key=item_name, CopySource=copy_source, Metadata=new_metadata, MetadataDirective="REPLACE")

        print("Metadata update (COPY) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```

## Utilización de Immutable Object Storage
{: #python-examples-immutable}

### Adición de una configuración de protección a un grupo existente
{: #python-examples-immutable-add}

Los objetos que se escriben en un grupo protegido no se pueden suprimir hasta que transcurre el periodo de protección y se eliminan todas las retenciones legales sobre el objeto. Se proporciona el valor de retención predeterminado del grupo a un objeto a menos que se proporcione un valor específico de objeto cuando se crea el objeto. Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto. 

Los valores mínimos y máximos admitidos para los valores de periodo de retención `MinimumRetention`, `DefaultRetention` y `MaximumRetention` son de 0 días y 365243 días (1000 años) respectivamente. 

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

### Comprobación de la protección sobre un grupo
{: #python-examples-immutable-check}
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


### Carga de un objeto protegido
{: #python-examples-immutable-upload}

Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.


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

### Adición o eliminación de una retención legal de un objeto protegido
{: #python-examples-immutable-legal-hold}

El objeto da soporte a 100 retenciones legales:

*  Un identificador de retención legal es una serie de caracteres con una longitud máxima de 64 caracteres y una longitud mínima de 1 carácter. Los caracteres válidos son letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` y `.
* Si la adición de la retención legal especificada supera las 100 retenciones legales en total del objeto, la nueva retención legal no se añade y se devuelve el error `400`.
* Si un identificador es demasiado largo, no se añade al objeto y se devuelve el error `400`.
* Si un identificador contiene caracteres no válidos, no se añade al objeto y se devuelve el error `400`.
* Si un identificador ya se está utilizando sobre un objeto, la retención legal existente no se modifica y la respuesta indica que el identificador ya se está utilizando con el error `409`.
* Si un objeto no tiene metadatos de periodo de retención, se devuelve el error `400` y no se permite añadir ni eliminar una retención legal.


El usuario que añade o elimina una retención legal debe tener el permiso de `Gestor` sobre el grupo.


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

### Ampliación del periodo de retención de un objeto protegido
{: #python-examples-immutable-extend}

El periodo de retención de un objeto solo se puede ampliar. No se puede reducir con respecto al valor configurado actualmente.

El valor de ampliación de retención se establece de una de las tres maneras siguientes:

* tiempo adicional a partir del valor actual (`Additional-Retention-Period` o un método similar)
* nuevo periodo de ampliación en segundos (`Extend-Retention-From-Current-Time` o un método similar)
* nueva fecha de caducidad de retención del objeto (`New-Retention-Expiration-Date` o método similar)

El periodo de retención actual almacenado en los metadatos de objeto se incrementa en el tiempo adicional especificado o bien se sustituye por el nuevo valor, en función del parámetro establecido en la solicitud `extendRetention`. En cualquiera de los casos, el parámetro de retención de ampliación se comprueba con respecto al periodo de retención actual y el parámetro ampliado solo se acepta si el periodo de retención actualizado es mayor que el periodo de retención actual.

Los objetos de grupos protegidos que ya no están bajo retención (el periodo de retención ha caducado y el objeto no tiene retenciones legales) vuelven a estar bajo retención cuando se sobrescriben. El nuevo periodo de retención se puede proporcionar como parte de la solicitud de sobrescritura del objeto o se asigna el tiempo de retención predeterminado del grupo al objeto.



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

### Obtención de una lista de las retenciones legales sobre un objeto protegido
{: #python-examples-immutable-list-holds}

Esta operación devuelve:

* Fecha de creación del objeto
* Periodo de retención del objeto en segundos
* Fecha de caducidad de retención calculada en función del periodo y de la fecha de creación
* Lista de retenciones legales
* Identificador de retención legal
* Indicación de fecha y hora en que se ha aplicado la retención legal

Si no hay ninguna retención legal sobre el objeto, se devuelve un `LegalHoldSet` vacío.
Si no se ha especificado ningún periodo de retención sobre el objeto, se devuelve el error `404`.


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

