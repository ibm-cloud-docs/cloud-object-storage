---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# Utilización de la CLI de AWS
{: #aws-cli}

La interfaz de línea de mandatos oficial para AWS es compatible con la API de IBM COS S3. Escrita en Python, se puede instalar desde Python Package Index con `pip install awscli`. De forma predeterminada, las claves de acceso se obtienen de `~/.aws/credentials`, pero también se pueden establecer como variables de entorno.

Estos ejemplos se han generado con la versión 1.14.2 de la CLI. Para comprobar la versión instalada, ejecute `aws --version`.

## Configuración de la CLI para que se conecte a {{site.data.keyword.cos_short}}
{: #aws-cli-config}

Para configurar la CLI de AWS, escriba `aws configure` y especifique sus [credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) y un nombre de región predeterminado. El "nombre de región" que utiliza AWS S3 corresponde al código de suministro (`LocationConstraint`) que utiliza {{site.data.keyword.cos_short}} para definir la clase de almacenamiento de los grupos nuevos.

Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

Esto crea dos archivos:

 `~/.aws/credentials`:

```
[default]
aws_access_key_id = {Access Key ID}
aws_secret_access_key = {Secret Access Key}
```
{:codeblock}

`~/.aws/config`:

```
[default]
region = {Provisioning Code}
output = json
```
{:codeblock}


También puede utilizar variables de entorno para establecer las credenciales de HMAC:

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


El punto final de IBM COS se debe obtener con la opción `--endpoint-url` y no se puede establecer en el archivo de credenciales.


## Mandatos de sintaxis de alto nivel
{: #aws-cli-high-level}

`aws --endpoint-url {endpoint} s3 <command>` se puede aplicar a casos de uso sencillos. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Los objetos se gestionan mediante mandatos de shell conocidos, como por ejemplo `ls`, `mv`, `cp` y `rm`. Los grupos se pueden crear con `mb` y se pueden suprimir con `rb`.

### Obtención de una lista de todos los grupos dentro de una instancia de servicio
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### Obtención de una lista de los objetos contenidos en un grupo
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### Creación de un nuevo grupo
{: #aws-cli-high-level-new-bucket}

**Nota**: Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
{:tip}

Si la región predeterminada en el archivo `~/.aws/config` corresponde a la misma ubicación que el punto final elegido, la creación de grupos es sencilla.

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### Adición de un objeto a un grupo
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

De forma alternativa, puede definir una nueva clave de objeto distinta del nombre de archivo:

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### Copia de un objeto de un grupo en otro dentro de la misma región:
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### Supresión de un objeto de un grupo
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### Eliminación de un grupo
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### Creación de URL prefirmados
{: #aws-cli-high-level-presign}

La CLI también puede crear URL prefirmados. Esto permite el acceso público temporal a los objetos sin tener que cambiar ninguno de los controles de acceso existentes. El URL que se genera contiene una firma HMAC que enmascara el URI, lo reduce la probabilidad de que un usuario que no sepa el URL completo pueda acceder a un archivo que de otro modo es de acceso público.

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

También se puede establecer un tiempo de caducidad para el URL en segundos (el valor predeterminado es 3600):

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## Mandatos de sintaxis de bajo nivel
{: #aws-cli-low-level}

La CLI de AWS también permite llamadas de API directas que proporcionan las mismas respuestas que las solicitudes HTTP directas mediante el mandato `s3api`.

### Obtención de una lista de grupos:
{: #aws-cli-low-level-list-buckets}

```bash
$ aws --endpoint-url {endpoint} s3api list-buckets
{
    "Owner": {
        "DisplayName": "{storage-account-uuid}",
        "ID": "{storage-account-uuid}"
    },
    "Buckets": [
        {
            "CreationDate": "2016-09-09T12:48:52.442Z",
            "Name": "bucket-1"
        },
        {
            "CreationDate": "2016-09-16T21:29:00.912Z",
            "Name": "bucket-2"
        }
    ]
}
```

### Obtención de una lista de los objetos contenidos en un grupo
{: #aws-cli-low-level-list-objects}

```sh
$ aws --endpoint-url {endpoint} s3api list-objects --bucket bucket-1
```

```json
{
    "Contents": [
        {
            "LastModified": "2016-09-28T15:36:56.807Z",
            "ETag": "\"13d567d518c650414c50a81805fff7f2\"",
            "StorageClass": "STANDARD",
            "Key": "c1ca2-filename-00001",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 837
        },
        {
            "LastModified": "2016-09-09T12:49:58.018Z",
            "ETag": "\"3ca744fa96cb95e92081708887f63de5\"",
            "StorageClass": "STANDARD",
            "Key": "c9872-filename-00002",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 533
        },
        {
            "LastModified": "2016-09-28T15:36:17.573Z",
            "ETag": "\"a54ed08bcb07c28f89f4b14ff54ce5b7\"",
            "StorageClass": "STANDARD",
            "Key": "98837-filename-00003",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 14476
        },
        {
            "LastModified": "2016-10-06T14:46:26.923Z",
            "ETag": "\"2bcc8ee6bc1e4b8cd2f9a1d61d817ed2\"",
            "StorageClass": "STANDARD",
            "Key": "abfc4-filename-00004",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 20950
        }
    ]
}
```
