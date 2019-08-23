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

# Utilisation de Python
{: #python}

La prise en charge de Python est fournie via une déviation de la bibliothèque `boto3`. Elle peut être installée à partir de Python Package Index via la commande `pip install ibm-cos-sdk`. 

Le code source se trouve dans [GitHub](https://github.com/ibm/ibm-cos-sdk-python/). 

La bibliothèque `ibm_boto3` fournit un accès complet à l'API {{site.data.keyword.cos_full}}. Des noeuds finaux, une clé d'API et l'ID instance doivent être spécifiés lors de la création d'une ressource de service ou d'un client de bas niveau, comme indiqué dans les exemples de base suivants. 

L'ID d'instance de service est également appelé _ID d'instance de ressource_. Vous pouvez trouver la valeur en créant des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) ou via l'interface CLI. {:tip}

Vous trouverez une documentation détaillée en cliquant [ici](https://ibm.github.io/ibm-cos-sdk-python/). 

## Mise à niveau à partir de 1.x.x
{: #python-migrate}

La version 2.0 du SDK introduit un changement d'espace de nom qui permet à une application d'utiliser la bibliothèque `boto3` d'origine pour se connecter aux ressources AWS dans la même application ou le même environnement. La mise à niveau depuis la version 1.x vers la version 2.0 n'est possible que si les modifications suivantes sont effectuées :

    1. Mettez à jour le fichier `requirements.txt` ou à partir de PyPI via `pip install -U ibm-cos-sdk`. Vérifiez qu'aucune version antérieure n'existe à l'aide de la commande `pip list | grep ibm-cos`. 
    2. Effectuez une mise à jour des déclarations d'importation `boto3` en `ibm_boto3`. 
    3. Si besoin, réinstallez la déclaration d'importation `boto3` d'origine en mettant à jour le fichier `requirements.txt`, ou à partir de PyPI via la commande `pip install boto3`. 

## Création d'un client et sourçage de données d'identification
{: #python-credentials}

Pour permettre l'établissement d'une connexion à COS, un client est créé et configuré en fournissant des données d'identification (clé d'API et ID d'instance de service). Ces valeurs peuvent aussi être automatiquement sourcées à partir d'un fichier de données d'identification ou à partir de variables d'environnement. 

Après que vous avez généré des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), le document JSON résultant peut être sauvegardé dans `~/.bluemix/cos_credentials`. Le SDK source automatiquement les données d'identification de ce fichier, sauf si d'autres données d'identification sont explicitement définies lors de la création du client. Si le fichier `cos_credentials` contient des clés HMAC, le client s'authentifie à l'aide d'une signature, sinon, il utilise la clé d'API fournie pour s'authentifier à l'aide d'un jeton bearer. 

Si vous effectuez une migration à partir de AWS S3, vous pouvez également sourcer des données d'identification à partir de `~/.aws/credentials` au format suivant :

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Si `~/.bluemix/cos_credentials` et `~/.aws/credentials` existent tous les deux, `cos_credentials` est prioritaire. 

### Collecte des informations requises
{: #python-prereqs}

Les variables suivantes apparaissent dans les exemples :

* `bucket_name` doit être une chaîne [unique et sécurisée DNS](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Etant donné que les noms de compartiment sont uniques sur l'ensemble du système, ces valeurs devront être modifiées si cet exemple est exécuté plusieurs fois. Notez que les noms sont réservés pendant 10 à 15 minutes après leur suppression. 
* `ibm_api_key_id` est la valeur trouvée dans les [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) sous la forme `apikey`. 
* `ibm_service_instance_id` est la valeur trouvée dans les [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) sous la forme `resource_instance_id`.  
* `endpoint_url` est une URL de noeud final de service qui englobe le protocole `https://`. Cette valeur n'est **pas** la valeur `endpoints` qui se trouve dans les [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `LocationConstraint` est un [code de mise à disposition valide](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) qui correspond à la valeur `endpoint`.  


## Exemples de code
{: #python-examples}

Les exemples de code ont été écrits à l'aide de **Python 2.7.15**. 

### Initialisation de la configuration
{: #python-examples-init}

  
```python
import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>" # eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Create resource
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)
```
*Valeurs de clé*
* `<endpoint>` - Noeud final public pour votre solution Cloud Object Storage avec un préfixe de schéma ('https://') (disponible à partir du [tableau de bord IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - Clé d'API générée lors de la création des données d'identification de service (un accès en écriture est requis pour les exemples de création et de suppression). 
* `<resource-instance-id>` - ID de ressource pour votre solution Cloud Object Storage (disponible à via l'[interface CLI IBM Cloud](/docs/cli?topic=cloud-cli-idt-cli) ou du [tableau de bord IBM Cloud](https://cloud.ibm.com/resources){:new_window}). 
* `<location>` - Emplacement par défaut pour votre solution Cloud Object Storage (doit correspondre à la région utilisée pour `<endpoint>`). 

*Références SDK*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### Création d'un nouveau compartiment
{: #python-examples-new-bucket}

Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

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

*Références SDK*
* Classes
  * [`Bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Méthodes
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### Création d'un nouveau fichier texte
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

*Références SDK*
* Classes
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Méthodes
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### Création de la liste des compartiments disponibles
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

*Références SDK*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* Collections
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### Création de la liste des éléments contenus dans un compartiment
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

*Références SDK*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* Collections
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### Obtention du contenu de fichier d'un élément particulier
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

*Références SDK*
* Classes
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Méthodes
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### Suppression d'un élément d'un compartiment
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

*Références SDK*
* Classes
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Méthodes
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### Suppression de plusieurs éléments d'un compartiment
{: #python-examples-delete-multiple-objects}

La demande de suppression peut contenir un maximum de 1000 clés. Bien que cela soit très utile pour réduire le temps système pour chaque demande, n'oubliez pas que la durée d'exécution d'une suppression d'un grand nombre de clés peut prendre un certain temps. Tenez compte également de la taille des objets de manière à obtenir de bonnes performances. {:tip}

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

*Références SDK*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Méthodes
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### Suppression d'un compartiment
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

*Références SDK*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Méthodes
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### Exécution d'un envoi par téléchargement en plusieurs parties
{: #python-examples-multipart}

#### Envoi par téléchargement de fichier binaire (méthode préférée)
{: #python-examples-multipart-binary}

La méthode [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} de la classe [S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} exécute automatiquement un envoi par téléchargement en plusieurs parties si nécessaire. La classe [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} est utilisée afin de déterminer le seuil pour l'utilisation de l'envoi par téléchargement en plusieurs parties. 

```python
def multi_part_upload(bucket_name, item_name, file_path):
    try:
        print("Starting file transfer for {0} to bucket: {1}\n".format(item_name, bucket_name))
        # set 5 MB chunks
        part_size = 1024 * 1024 * 5

        # set threadhold to 15 MB
        file_threshold = 1024 * 1024 * 15

        # set the transfer threshold and chunk size
        transfer_config = ibm_boto3.s3.transfer.TransferConfig(
            multipart_threshold=file_threshold,
            multipart_chunksize=part_size
        )

        # the upload_fileobj method will automatically execute a multi-part upload
        # in 5 MB chunks for all files over 15 MB
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

*Références SDK*
* Classes
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* Méthodes
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### Exécution manuelle d'un envoi par téléchargement en plusieurs parties
{: #python-examples-multipart-manual}

Vous pouvez, si vous le souhaitez, utiliser la classe [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} pour effectuer un envoi par téléchargement en plusieurs parties. Cela peut être utile si un contrôle plus important sur le processus d'envoi par téléchargement est nécessaire. 

```python
def multi_part_upload_manual(bucket_name, item_name, file_path):
    try:
        # create client object
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT
        )

        print("Starting multi-part upload for {0} to bucket: {1}\n".format(item_name, bucket_name))

        # initiate the multi-part upload
        mp = cos_cli.create_multipart_upload(
            Bucket=bucket_name,
            Key=item_name
        )

        upload_id = mp["UploadId"]

        # min 5MB part size
        part_size = 1024 * 1024 * 5
        file_size = os.stat(file_path).st_size
        part_count = int(math.ceil(file_size / float(part_size)))
        data_packs = []
        position = 0
        part_num = 0

        # begin uploading the parts
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

        # complete upload
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
        # abort the upload
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

*Références SDK*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Méthodes
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### Envoi par téléchargement d'objet volumineux à l'aide de TransferManager
{: #python-examples-multipart-transfer}

`TransferManager` représente un autre moyen d'exécuter des transferts de fichiers volumineux en incorporant automatiquement des envois par téléchargement en plusieurs parties chaque fois que cela est nécessaire lors de la définition des paramètres de configuration. 

```python
def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # set the chunk size to 5 MB
    part_size = 1024 * 1024 * 5

    # set threadhold to 5 MB
    file_threshold = 1024 * 1024 * 5

    # Create client connection
    cos_cli = ibm_boto3.client("s3",
        ibm_api_key_id=COS_API_KEY_ID,
        ibm_service_instance_id=COS_SERVICE_CRN,
        ibm_auth_endpoint=COS_AUTH_ENDPOINT,
        config=Config(signature_version="oauth"),
        endpoint_url=COS_ENDPOINT
    )

    # set the transfer threshold and chunk size in config settings
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # create transfer manager
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_cli, config=transfer_config)

    try:
        # initiate file upload
        future = transfer_mgr.upload(file_path, bucket_name, item_name)

        # wait for upload to complete
        future.result()

        print ("Large file upload complete!")
    except Exception as e:
        print("Unable to complete large file upload: {0}".format(e))
    finally:
        transfer_mgr.shutdown()

```

### Création de la liste des éléments contenus dans un compartiment (v2)
{: #python-examples-list-objects-v2}

L'objet [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} contient une méthode mise à jour permettant de répertorier le contenu ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}). Cette méthode vous permet de limiter le nombre d'enregistrements renvoyés et d'extraire les enregistrements par lots. Cela peut s'avérer utile pour paginer vos résultats dans une application et améliorer les performances. 

```python
def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        # create client object
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

*Références SDK*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Méthodes
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Utilisation de Key Protect
{: #python-examples-kp}
Key Protect peut être ajouté à un compartiment de stockage pour chiffrer les données sensibles qui sont au repos dans le cloud. 

### Avant de commencer
{: #python-examples-kp-prereqs}

Les éléments suivants sont nécessaires pour créer un compartiment avec Key Protect activé :

* Un service Key Protect doit être [mis à disposition](/docs/services/key-protect?topic=key-protect-provision). 
* Une clé racine doit être disponible ([générée](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importée](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)). 

### Extraction du CRN de clé racine
{: #python-examples-kp-root}

1. Extrayez l'[ID d'instance](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) pour votre service Key Protect. 
2. Utilisez l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) pour extraire toutes vos [clés disponibles](https://cloud.ibm.com/apidocs/key-protect). 
    * Vous pouvez utiliser des commandes `curl` ou un client REST API, tel que [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), pour accéder à l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api). 
3. Extrayez le CRN de la clé racine que vous utiliserez pour activer Key Protect sur votre compartiment. Le CRN se présente comme suit :

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Création d'un compartiment avec Key Protect activé
{: #python-examples-kp-new-bucket}
```python
COS_KP_ALGORITHM = "<algorithm>"
COS_KP_ROOTKEY_CRN = "<root-key-crn>"

# Create a new bucket with key protect (encryption)
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

*Valeurs de clé*
* `<algorithm>` - Algorithme de chiffrement utilisé pour les nouveaux objets ajoutés au compartiment (la valeur par défaut est AES256). 
* `<root-key-crn>` - CRN de la clé racine qui est obtenue auprès du service Key Protect. 

*Références SDK*
* Classes
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Méthodes
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Utilisation de l'option Transfert haut débit Aspera
{: #python-examples-aspera}

En installant la [bibliothèque Aspera High-Speed Transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), vous pouvez utiliser des transferts de fichiers à haut débit dans votre application. La bibliothèque Aspera est close source, il s'agit donc d'une dépendance facultative pour le SDK COS (qui utilise une licence Apache). 

Chaque session Aspera génère un processus `ascp` individuel qui s'exécute sur la machine client pour effectuer le transfert. Assurez-vous que votre environnement de calcul puisse permettre l'exécution de ce processus. {:tip}


### Initialisation de AsperaTransferManager
{: #python-examples-aspera-init}

Avant d'initialiser `AsperaTransferManager`, assurez-vous que l'objet [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} (et non pas `resource` ou `session`) fonctionne. 

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_s3transfer.aspera.manager import AsperaTransferManager

COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location>"

# Create resource
cos = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

transfer_manager = AsperaTransferManager(cos)
```

Vous devez fournir une clé d'API IAM pour Aspera High-Speed Transfer. Les [données d'identification HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} ne sont **PAS** prises en charge actuellement. Pour plus d'informations sur IAM, [cliquez ici](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

Pour obtenir une capacité de traitement la plus élevée possible, fractionnez le transfert en un certain nombre de **sessions** parallèles qui envoient des blocs de données dont la taille est définie par la valeur **threshold**. 

La configuration standard pour l'utilisation de plusieurs sessions doit être la suivante :
* Débit cible de 2500 Mbit/s
* Seuil de 100 Mo (*il s'agit de la valeur recommandée pour la plupart des applications*)

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
Dans l'exemple ci-dessus, le SDK générera suffisamment de sessions pour tenter d'atteindre le taux cible de 2500 Mbit/s. 

Sinon, la gestion de session peut aussi être configurée explicitement dans le SDK. Cela s'avère utile si vous souhaitez un contrôle plus précis de l'utilisation du réseau. 

La configuration standard pour l'utilisation de plusieurs sessions explicites doit être la suivante :
* 2 ou 10 sessions
* Seuil de 100 Mo (*il s'agit de la valeur recommandée pour la plupart des applications*)

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
Pour optimiser les performances dans la plupart des scénarios, utilisez toujours plusieurs sessions afin de minimiser les temps système liés à l'instanciation d'un transfert haut débit Aspera. **Si votre capacité réseau est d'au moins 1 Gbps, vous devez utiliser 10 sessions. **  Des réseaux avec une bande passante inférieure doivent utiliser 2 sessions.
{:tip}

### Envoi par téléchargement d'un fichier
{: #python-examples-aspera-upload}
```python
bucket_name = "<bucket-name>"
upload_filename = "<absolute-path-to-file>"
object_name = "<item-name>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload(upload_filename, bucket_name, object_name)

    # Wait for upload to complete
    future.result()
```

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment cible. 
* `<absolute-path-to-file>` - Chemin de répertoire et nom du fichier à envoyer par téléchargement. 
* `<item-name>` - Nom du nouveau fichier ajouté au compartiment. 

### Réception par téléchargement d'un fichier
{: #python-examples-aspera-download}

```python
bucket_name = "<bucket-name>"
download_filename = "<absolute-path-to-file>"
object_name = "<object-to-download>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download(bucket_name, object_name, download_filename)

    # Wait for download to complete
    future.result()
```

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-file>` - Répertoire et nom de fichier où sauvegarder le fichier sur le système local. 
* `<object-to-download>` - Nom du fichier à recevoir par téléchargement à partir du compartiment. 

### Envoi par téléchargement d'un répertoire
{: #python-examples-aspera-directory-upload}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY, and have objects in it.
local_upload_directory = "<absolute-path-to-directory>"
# THIS SHOULD NOT HAVE A LEADING "/"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory)

    # Wait for upload to complete
    future.result()
```

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-directory>` - Répertoire local contenant les fichiers à envoyer par téléchargement. Doit comporter une barre oblique (`/`) de début et de fin (par exemple, `/Users/testuser/Documents/Upload/`). 
* `<object prefix>` - Nom du répertoire dans lequel stocker les fichiers dans le compartiment. Ne doit pas comporter de barre oblique (`/`) de début (par exemple, `newuploads/`). 

### Réception par téléchargement d'un répertoire
{: #python-examples-aspera-directory-download}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory)

    # Wait for download to complete
    future.result()
```

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-directory>` - Répertoire local dans lequel sauvegarder les fichiers reçus par téléchargement. Doit comporter une barre oblique (`/`) de début et de fin (par exemple, `/Users/testuser/Downloads/`). 
* `<object prefix>` - Nom du répertoire dans lequel stocker les fichiers dans le compartiment. Ne doit pas comporter de barre oblique (`/`) de début (par exemple, `todownload/`). 

### Utilisation des abonnés
{: #python-examples-aspera-subscribers}

Les abonnés offrent une certaine observabilité des transferts en associant des méthodes de rappel personnalisées. Tous les transferts passent par les phases suivantes :

`Queued - In Progress - Done`

Trois abonnés sont disponibles pour chaque phase :

* `CallbackOnQueued()` - Appelé lorsqu'un nouveau transfert est ajouté à `AsperaTransferManager`. 
* `CallbackOnProgress()` - Appelé lorsqu'un transfert commence à transmettre des données (déclenché de manière répétée pendant que le transfert est en cours). 
* `CallbackOnDone()` - Appelé lorsque le transfert est terminé. 

```python
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Subscriber callbacks
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

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Attach subscribers
subscribers = [CallbackOnQueued(), CallbackOnProgress(), CallbackOnDone()]

# Get object with Aspera
future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, subscribers)

# Wait for download to complete
future.result()
```

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment dans votre instance de service Object Storage avec Aspera activé. 
* `<absolute-path-to-directory>` - Répertoire local dans lequel sauvegarder les fichiers reçus par téléchargement. Doit comporter une barre oblique (`/`) de début et de fin (par exemple, `/Users/testuser/Downloads/`). 
* `<object prefix>` - Nom du répertoire dans lequel stocker les fichiers dans le compartiment. Ne doit pas comporter de barre oblique (`/`) de début (par exemple, `todownload/`). 

L'exemple de code ci-dessus produit le résultat suivant :

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### Pause/Reprise/Annulation
{: #python-examples-aspera-pause}

Le SDK permet de gérer la progression des transferts de fichiers/répertoires via les méthodes suivantes de l'objet `AsperaTransferFuture` :

* `pause()`
* `resume()`
* `cancel()`

Il n'y a pas d'effets secondaires liés à l'appel de l'une ou l'autre des méthodes ci-dessus. Le nettoyage adéquat est géré par le SDK. {:tip}

```python
# Create Transfer manager
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

with AsperaTransferManager(client) as transfer_manager:

    # download a directory with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

    # pause the transfer
    future.pause()

    # resume the transfer
    future.resume()

    # cancel the transfer
    future.cancel()
```

### Traitement des incidents liés à Aspera
{: #python-examples-aspera-ts}
**Problème :** les développeurs qui utilisent Python 2.7.15 sous Windows 10 peuvent rencontrer des problèmes lorsqu'ils installent le SDK Aspera. 

**Cause :** si différentes versions de Python sont installées dans votre environnement, des incidents peuvent se produire lors de la tentative d'installation du SDK Aspera. Cela peut être dû à des fichiers DLL manquants ou à une DLL incorrecte dans le chemin. 

**Solution :** en premier lieu, réinstallez les bibliothèques Aspera. Il se peut que leur installation ait échoué. Cela peut avoir affecté les fichiers DLL. Si cela ne permet pas de résoudre les problèmes, vous devez mettre à jour votre version de Python. Si vous ne pouvez pas effectuer cette mise à jour, vous pouvez installer [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window}. Cet outil vous permettra d'installer le SDK Aspera. 

## Mise à jour de métadonnées
{: #python-examples-metadata}
Il existe deux manières de mettre à jour les métadonnées sur un objet existant :
* En exécutant une demande `PUT` avec les nouvelles métadonnées et le contenu de l'objet d'origine 
* En exécutant une demande `COPY` avec les nouvelles métadonnées en spécifiant l'objet d'origine comme source de la copie 

### Utilisation de PUT pour mettre à jour les métadonnées
{: #python-examples-metadata-put}
**Remarque :** la demande `PUT` remplace le contenu existant de l'objet, par conséquent, le contenu doit d'abord être reçu par téléchargement, puis à nouveau envoyé par téléchargement avec les nouvelles métadonnées. 

```python
def update_metadata_put(bucket_name, item_name, key, value):
    try:
        # retrieve the existing item to reload the contents
        response = cos_cli.get_object(Bucket=bucket_name, Key=item_name)
        existing_body = response.get("Body").read()

        # set the new metadata
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

### Utilisation de COPY pour mettre à jour les métadonnées
{: #python-examples-metadata-copy}

```python
def update_metadata_copy(bucket_name, item_name, key, value):
    try:
        # set the new metadata
        new_metadata = {
            key: value
        }

        # set the copy source to itself
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

## Utilisation de la fonction Immutable Object Storage
{: #python-examples-immutable}

### Ajout d'une configuration de protection à un compartiment existant
{: #python-examples-immutable-add}

Les objets écrits dans un compartiment protégé ne peuvent pas être supprimés tant que la période de protection n'est pas arrivée à expiration et que les conservations légales associées à l'objet n'ont pas toutes été retirées. La valeur de conservation par défaut du compartiment est attribuée à un objet, sauf si une valeur spécifique à un objet est fournie lors de la création de l'objet. Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet.  

Les valeurs minimales et maximales prises en charge pour les paramètres de durée de conservation `MinimumRetention`, `DefaultRetention` et `MaximumRetention` sont respectivement de 0 jours et 365243 jours (1000 ans).  

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

### Vérification de la protection d'un compartiment
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


### Envoi par téléchargement d'un objet protégé
{: #python-examples-immutable-upload}

Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 


|Valeur	| Type	| Description |
| --- | --- | --- | 
|`Retention-Period` | Entier non négatif (secondes) | Durée de conservation, exprimée en secondes, pendant laquelle stocker l'objet. L'objet ne peut être ni écrasé, ni supprimé tant que la durée de conservation n'est pas écoulée. Si cette zone et la zone `Retention-Expiration-Date` sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la période `DefaultRetention` du compartiment est utilisée. Zéro (`0`) est une valeur légale qui part du principe que la durée de conservation minimale du compartiment est également fixée à `0`. |
| `Retention-expiration-date` | Date (format ISO 8601) | Date à laquelle la suppression ou la modification de l'objet devient légale. Vous pouvez uniquement spécifier cette zone ou bien la zone d'en-tête Retention-Period. Si ces deux zones sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la durée de conservation par défaut du compartiment est utilisée. |
| `Retention-legal-hold-id` |Chaîne| Conservation légale unique à appliquer à l'objet. Une conservation légale est une chaîne comportant Y caractères. L'objet ne peut être ni écrasé ni supprimé tant que les conservations légales associées à l'objet n'ont pas toutes été retirées. |

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

### Ajout ou retrait d'une conservation légale dans un objet protégé
{: #python-examples-immutable-legal-hold}

L'objet peut prendre en charge 100 conservations légales :

*  Un identificateur de conservation légale est une chaîne comprise entre 1 et 64 caractères. Les caractères valides sont des lettres, des chiffres, `!`, `_`, `.`, `*`, `(`, `)`, `-` et `.
* Si la tentative d'ajout de la conservation légale spécifiée dépasse le seuil de 100 conservations légales affectées à l'objet, l'opération d'ajout échoue et un code d'erreur `400` est renvoyé. 
* Si un identificateur est trop long, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur contient des caractères non valides, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur est déjà en cours d'utilisation sur un objet, la conservation légale existante n'est pas modifiée et la réponse indique que l'identificateur était déjà utilisé et renvoie un code d'erreur `409`. 
* Si un objet ne comporte pas de métadonnées de durée de conservation, un code d'erreur `400` est renvoyé et l'ajout ou le retrait d'une conservation légale ne sont pas autorisés. 


L'utilisateur qui effectue l'ajout ou la suppression d'une conservation légale doit disposer des droits d'accès `Manager` pour ce compartiment. 


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

### Prolongement de la durée de conservation d'un objet protégé
{: #python-examples-immutable-extend}

La durée de conservation d'un objet ne peut être que prolongée. La valeur actuellement configurée ne peut pas être diminuée. 

La valeur de prolongement de la conservation est définie de l'une des trois façons suivantes :

* En ajoutant une durée supplémentaire par rapport à la valeur en cours (`Additional-Retention-Period` ou méthode similaire) 
* En définissant une nouvelle période de prolongement, exprimée en secondes (`Extend-Retention-From-Current-Time` ou méthode similaire) 
* En définissant une nouvelle date d'expiration de la conservation de l'objet (`New-Retention-Expiration-Date` ou méthode similaire) 

La durée de conservation en cours stockée dans les métadonnées de l'objet est soit augmentée de la durée supplémentaire indiquée, soit remplacée par la nouvelle valeur, en fonction du paramètre défini dans la demande `extendRetention`. Dans tous les cas, le paramètre de prolongement de la conservation est vérifié par rapport à la durée de conservation en cours et le paramètre de prolongement n'est accepté que si la durée de conservation mise à jour est supérieure à la durée de conservation en cours. 

Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 



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

### Création de la liste des conservations légales associées à un objet protégé
{: #python-examples-immutable-list-holds}

Cette opération renvoie les éléments suivants :

* La date de création de l'objet
* La durée de conservation, exprimée en secondes
* La date d'expiration de la conservation, calculée sur la base de la durée de conservation et de la date de création
* La liste des conservations légales
* L'identificateur de conservation légale
* L'horodatage correspondant à l'application de la conservation légale

Si aucune conservation légale n'est associée à l'objet, un élément `LegalHoldSet` vide est renvoyé.
Si aucune durée de conservation n'est spécifiée pour l'objet, un code d'erreur `404` est renvoyé. 


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

