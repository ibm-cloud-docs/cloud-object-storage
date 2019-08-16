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

# Utilizzo di Python
{: #python}

Il supporto Python viene fornito tramite un duplicato della libreria `boto3`. Può essere installato dal Python Package Index tramite `pip install ibm-cos-sdk`.

Il codice di origine può essere trovato in [GitHub](https://github.com/ibm/ibm-cos-sdk-python/).

La libreria `ibm_boto3` fornisce l'accesso completo all'API {{site.data.keyword.cos_full}}. Devono essere specificati gli endpoint, una chiave API e l'ID istanza durante la creazione di una risorsa del servizio o del client di basso livello come mostrato nei seguenti esempi di base.

All'ID istanza del servizio viene fatto riferimento anche come a _ID istanza risorsa_. Il valore può essere trovato creando una [credenziale di servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) o tramite la CLI.
{:tip}

La documentazione dettagliata può essere trovata [qui](https://ibm.github.io/ibm-cos-sdk-python/).

## Upgrade da 1.x.x
{: #python-migrate}

La versione 2.0 dell'SDK introduce una modifica della spaziatura dei nomi che consente a un'applicazione di utilizzare la libreria `boto3` originale per stabilire una connessione alle risorse AWS all'interno della stessa applicazione o dello stesso ambiente. Per la migrazione da 1.x a 2.0, sono necessarie alcune modifiche.

    1. Aggiorna `requirements.txt` o da PyPI tramite `pip install -U ibm-cos-sdk`. Verifica che non siano presenti delle versioni precedenti con `pip list | grep ibm-cos`.
    2. Aggiorna qualsiasi dichiarazione di importazione da `boto3` a `ibm_boto3`.
    3. Se necessario, reinstalla il `boto3` originale aggiornando `requirements.txt` o da PyPI tramite `pip install boto3`. 

## Creazione di un client e derivazione delle credenziali
{: #python-credentials}

Per la connessione a COS, viene creato e configurato un client fornendo le informazioni sulle credenziali (chiave API e ID istanza del servizio). Questi valori possono anche essere derivati automaticamente da un file di credenziali o dalle variabili di ambiente.

Dopo aver generato una [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), il documento JSON risultante può essere salvato in `~/.bluemix/cos_credentials`. L'SDK deriverà automaticamente le credenziali da questo file a meno che non vengano esplicitamente impostate altre credenziali durante la creazione del client. Se il file `cos_credentials` contiene le chiavi HMAC, il client esegue l'autenticazione con una firma, altrimenti utilizza la chiave API fornita tramite un token di connessione.

Se si esegue la migrazione da AWS S3, puoi anche derivare i dati delle credenziali da `~/.aws/credentials` nel formato:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Se esistono `~/.bluemix/cos_credentials` e `~/.aws/credentials`, `cos_credentials` ha la precedenza.

### Raccogli le informazioni richieste 
{: #python-prereqs}

Le seguenti variabili vengono visualizzate negli esempi:

* `bucket_name` deve essere una stringa [univoca e indipendente da DNS](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Poiché i nomi dei bucket sono univoci nell'intero sistema, questi valori devono essere modificati se questo esempio viene eseguito più volte. Tieni presente che i nomi sono riservati per 10-15 minuti dopo l'eliminazione.
* `ibm_api_key_id` è il valore trovato nella [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) come `apikey`.
* `ibm_service_instance_id` è il valore trovato nella [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) come `resource_instance_id`. 
* `endpoint_url` è un URL di endpoint del servizio, inclusivo del protocollo `https://`. Questo valore **non** è il valore `endpoints` disponibile nella [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `LocationConstraint` è un [codice di provisioning valido](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) che corrisponde al valore `endpoint`. 


## Esempi di codici
{: #python-examples}

Esempi di codice scritti utilizzando **Python 2.7.15**

### Inizializzazione della configurazione
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
*Valori chiave*
* `<endpoint>` - endpoint pubblico per il tuo Object Storage cloud con schema con prefisso ('https://') (disponibile dal [Dashboard IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - chiave API generata quando si creano le credenziali del servizio (l'accesso in scrittura è necessario per gli esempi di creazione ed eliminazione). 
* `<resource-instance-id>` - ID risorsa per il tuo Object Storage cloud (disponibile tramite la [CLI IBM Cloud](/docs/cli?topic=cloud-cli-idt-cli) o il [Dashboard IBM Cloud](https://cloud.ibm.com/resources){:new_window})
* `<location>` - l'ubicazione predefinita per il tuo Object Storage cloud (deve corrispondere alla regione utilizzata per `<endpoint>`)

*Riferimenti SDK*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### Creazione di un nuovo bucket 
{: #python-examples-new-bucket}

È possibile che si faccia riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida alle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

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

*Riferimenti SDK*
* Classi
  * [`Bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Metodi
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### Creazione di un nuovo file di testo 
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

*Riferimenti SDK*
* Classi
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Metodi
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### Elenca i bucket disponibili 
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

*Riferimenti SDK*
* Classi
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* Raccolte 
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### Elenca gli elementi in un bucket
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

*Riferimenti SDK*
* Classi
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* Raccolte 
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### Ottieni il contenuto del file di uno specifico elemento
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

*Riferimenti SDK*
* Classi
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Metodi
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### Elimina un elemento da un bucket
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

*Riferimenti SDK*
* Classi
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Metodi
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### Elimina più elementi da un bucket
{: #python-examples-delete-multiple-objects}

La richiesta di eliminazione può contenere un massimo di 1000 chiavi che vuoi eliminare. Se da una parte questo è utile per ridurre il sovraccarico per ogni richiesta, fai attenzione quando elimini molte chiavi. Inoltre, prendi in considerazione anche le dimensioni degli oggetti per garantire delle prestazioni adeguate.
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

*Riferimenti SDK*
* Classi
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Metodi
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### Elimina un bucket
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

*Riferimenti SDK*
* Classi
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Metodi
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### Esegui un caricamento in più parti 
{: #python-examples-multipart}

#### Carica un file binario (metodo preferito)
{: #python-examples-multipart-binary}

Il metodo [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} della classe [S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} esegue automaticamente un caricamento in più parti quando necessario. La classe [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} viene utilizzata per determinare la soglia di utilizzo del caricamento in più parti.

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

*Riferimenti SDK*
* Classi
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* Metodi
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### Esegui manualmente un caricamento in più parti 
{: #python-examples-multipart-manual}

Se desiderato, la classe [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} può essere utilizzata per eseguire un caricamento in più parti. Questo può essere utile se è necessario maggiore controllo sul processo di caricamento.

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

*Riferimenti SDK*
* Classi
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Metodi
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### Caricamento di grandi oggetti tramite TransferManager
{: #python-examples-multipart-transfer}

`TransferManager` fornisce un altro modo di eseguire trasferimenti di grandi file incorporando automaticamente i caricamenti in più parti quando necessario impostando dei parametri di configurazione.

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

### Elenca gli elementi in un bucket (v2) 
{: #python-examples-list-objects-v2}

L'oggetto [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} contiene un metodo aggiornato per elencare i contenuti ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}). Questo metodo ti consente di limitare il numero di record restituiti e di richiamare i record in batch. Questo potrebbe essere utile per la paginazione dei tuoi risultati all'interno di un'applicazione e per migliorare le prestazioni.

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

*Riferimenti SDK*
* Classi
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Metodi
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Utilizzo di Key Protect
{: #python-examples-kp}
Key Protect può essere aggiunto a un bucket di archiviazione per crittografare dati sensibili inattivi nel cloud. 

### Prima di cominciare
{: #python-examples-kp-prereqs}

I seguenti elementi sono necessari per creare un bucket con Key-Protect abilitato:

* Un servizio Key Protect [di cui è stato eseguito il provisioning](/docs/services/key-protect?topic=key-protect-provision)
* Una chiave root disponibile ([generata](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importata](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Richiamo del CRN di chiave root
{: #python-examples-kp-root}

1. Richiama l'[ID istanza](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) per il tuo servizio Key Protect
2. Utilizza l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) per richiamare tutte le tue [chiavi disponibili](https://cloud.ibm.com/apidocs/key-protect)
    * Puoi utilizzare i comandi `curl` o un client REST API come [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) per accedere all'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Richiama il CRN della chiave root che utilizzi per abilitare Key Protect sul tuo bucket. Il CRN è simile al seguente: 

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creazione di un bucket con Key Protect abilitato 
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

*Valori chiave*
* `<algorithm>` - l'algoritmo di crittografia utilizzato per i nuovi oggetti aggiunti al bucket (il valore predefinito è AES256). 
* `<root-key-crn>` - CRN della chiave root ottenuta dal servizio Key Protect.

*Riferimenti SDK*
* Classi
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Metodi
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Utilizzo del trasferimento ad alta velocità Aspera 
{: #python-examples-aspera}

Installando la [Libreria del trasferimento ad alta velocità Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), puoi utilizzare trasferimenti file ad alta velocità all'interno della tua applicazione. La libreria Aspera è closed-source e, pertanto, una dipendenza facoltativa per l'SDK COS (che utilizza una licenza Apache). 

Ogni sessione Aspera crea un singolo processo `ascp` eseguito sulla macchina client per eseguire il trasferimento. Assicurati che il tuo ambiente di calcolo possa consentire l'esecuzione di questo processo.
{:tip}


### Inizializzazione di AsperaTransferManager
{: #python-examples-aspera-init}

Prima di inizializzare `AsperaTransferManager`, assicurati di avere un oggetto [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} funzionante (non una risorsa (`resource`) o una sessione (`session`)).

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

Devi fornire una chiave API IAM per i trasferimenti ad alta velocità Aspera. [Le credenziali HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} **NON** sono al momento supportate. Per ulteriori informazioni su IAM, [fai clic qui](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

Per ottenere una velocità effettiva più elevata, suddividi il trasferimento in un numero specificato di **sessioni** parallele che inviano blocchi di dati la cui dimensione è definita dal valore **threshold**.

La tipica configurazione per l'utilizzo di più sessioni dovrebbe essere: 
* Velocità di destinazione di 2500 MBps 
* Soglia di 100 MB (*questo è il valore consigliato per la maggior parte delle applicazioni*) 

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
Nell'esempio sopra riportato, l'SDK genererà un numero sufficiente di sessioni per provare a raggiungere la velocità di destinazione di 2500 MBps. 

In alternativa, la gestione delle sessioni può essere configurata esplicitamente nell'SDK. Ciò è utile nei casi in cui si voglia un controllo più preciso sull'utilizzo della rete. 

La tipica configurazione per l'utilizzo di più sessioni esplicito dovrebbe essere: 
* 2 o 10 sessioni
* Soglia di 100 MB (*questo è il valore consigliato per la maggior parte delle applicazioni*) 

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
Per le migliori prestazioni nella maggior parte degli scenari, utilizza sempre sessioni multiple per ridurre al minimo il sovraccarico associato all'istanziazione di un trasferimento ad alta velocità Aspera. **Se la tua capacità di rete è di almeno 1 Gbps devi utilizzare 10 sessioni.** Reti con una larghezza di banda inferiore devono utilizzare due sessioni.
{:tip}

### Caricamento file 
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

*Valori chiave*
* `<bucket-name>` - nome del bucket di destinazione
* `<absolute-path-to-file>` - percorso di directory e nome del file che deve essere caricato
* `<item-name>` - nome del nuovo file aggiunto al bucket

### Scarica file
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

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato. 
* `<absolute-path-to-file>` - directory e nome del file in cui salvare il file sul sistema locale.
* `<object-to-download>` - nome del file nel bucket da scaricare

### Caricamento directory
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

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato. 
* `<absolute-path-to-directory>` - directory locale che contiene i file che devono essere caricati. Deve avere una `/` di inizio e di fine (ad es. `/Users/testuser/Documents/Upload/`)
* `<object prefix>` - nome della directory nel bucket per archiviare i file. Non deve avere una barra `/` iniziale (ad es. `newuploads/`)

### Scaricamento directory
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

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato. 
* `<absolute-path-to-directory>` - directory locale per salvare i file scaricati. Deve avere una barra `/` di inizio e di fine (ad es. `/Users/testuser/Downloads/`)
* `<object prefix>` - nome della directory nel bucket per archiviare i file. Non deve avere una barra `/` iniziale (ad es. `todownload/`)

### Utilizzo dei sottoscrittori
{: #python-examples-aspera-subscribers}

I sottoscrittori forniscono l'osservabilità nei trasferimenti collegando dei metodi di callback personalizzati. Tutte le transizioni dei trasferimenti tra le seguenti fasi:

`Queued - In Progress - Done`

Esistono tre sottoscrittori disponibili per ogni fase:

* `CallbackOnQueued()` - richiamato quando è stato aggiunto un nuovo trasferimento a `AsperaTransferManager`
* `CallbackOnProgress()` - richiamato quando un trasferimento ha iniziato a trasmettere dati (attivato ripetutamente mentre il trasferimento è in corso).
* `CallbackOnDone()` - richiamato quando il trasferimento è stato completato

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

*Valori chiave*
* `<bucket-name>` - il nome del bucket nella tua istanza del servizio Object Storage che ha Aspera abilitato. 
* `<absolute-path-to-directory>` - directory locale per salvare i file scaricati. Deve avere una barra `/` di inizio e di fine (ad es. `/Users/testuser/Downloads/`)
* `<object prefix>` - nome della directory nel bucket per archiviare i file. Non deve avere una barra `/` iniziale (ad es. `todownload/`)

Il codice di esempio produce il seguente output: 

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### Metti in pausa/Riprendi/Annulla 
{: #python-examples-aspera-pause}

L'SDK consente di gestire l'avanzamento dei trasferimenti di file/directory mediante i seguenti metodi dell'oggetto `AsperaTransfer`: 

* `pause()`
* `resume()`
* `cancel()`

Il richiamo dei metodi sopra indicati non ha alcun effetto collaterale. Adeguate attività di ripulitura e manutenzione sono gestite dall'SDK.
{:tip}

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

### Risoluzione dei problemi di Aspera 
{: #python-examples-aspera-ts}
**Problema:** gli sviluppatori che utilizzano Python 2.7.15 su Windows 10 potrebbero riscontrare degli errori durante l'installazione dell'SDK Aspera.

**Causa:** se ci sono diverse versioni di Python installate sul tuo ambiente puoi riscontrare degli errori di installazione quando tenti di installare l'SDK Aspera. Questo può essere causato dalla mancanza di file DLL o di un DLL non corretto nel percorso.

**Soluzione:** il primo passo nella risoluzione di questo problema dovrebbe essere di reinstallare le librerie Aspera. Potrebbe essersi verificato un errore durante l'installazione. Di conseguenza questo errore potrebbe avere coinvolto i file DLL. Se questo non risolve i problemi, ti verrà richiesto di aggiornare la tua versione di Python. Se non puoi farlo, puoi utilizzare l'installazione di [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window}. Questo ti consentirà di installare l'SDK Aspera senza alcun problema.

## Aggiornamento dei metadati
{: #python-examples-metadata}
Esistono due modi per aggiornare i metadati in un oggetto esistente:
* Una richiesta `PUT` con i nuovi metadati e il contenuto dell'oggetto originale
* L'esecuzione di una richiesta `COPY` con i nuovi metadati che specifica l'oggetto originale come origine della copia

### Utilizzo di PUT per aggiornare i metadati
{: #python-examples-metadata-put}
**Nota:** la richiesta `PUT` sovrascrive il contenuto esistente dell'oggetto e, pertanto, deve essere prima scaricato e ricaricato con i nuovi metadati

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

### Utilizzo di COPY per aggiornare i metadati
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

## Utilizzo di Immutable Object Storage
{: #python-examples-immutable}

### Aggiunta di una configurazione di protezione ad un bucket esistente
{: #python-examples-immutable-add}

Gli oggetti scritti in un bucket protetto non possono essere eliminati fino a quando il periodo di protezione non è scaduto e tutte le conservazioni a fini legali sull'oggetto non sono stati rimosse. Il valore di conservazione predefinito del bucket viene dato ad un oggetto a meno che non venga fornito un valore specifico dell'oggetto quando l'oggetto viene creato. Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket. 

I valori supportati minimo e massimo per le impostazioni del periodo di conservazione `MinimumRetention`, `DefaultRetention` e `MaximumRetention` sono, rispettivamente, 0 giorni e 365243 giorni (1000 anni). 

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

### Controlla la protezione su un bucket
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


### Carica un oggetto protetto
{: #python-examples-immutable-upload}

Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket.


|Valore	| Tipo	| Descrizione |
| --- | --- | --- | 
|`Retention-Period` | Numero intero non negativo (secondi) | Il periodo di conservazione da memorizzare sull'oggetto, in secondi. L'oggetto non può essere sovrascritto o eliminato finché l'intervallo di tempo specificato nel periodo di conservazione non è trascorso. Se vengono specificati questi campo e `Retention-Expiration-Date`, viene restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo `DefaultRetention` del bucket. Zero (`0`) è un valore consentito, presumendo che il periodo di conservazione minimo del bucket sia anch'esso `0`. |
| `Retention-expiration-date` | Data (formato ISO 8601) | Data in cui sarà consentito eliminare o modificare l'oggetto. Puoi specificare solo questo valore oppure l'intestazione Retention-Period. Se vengono specificati entrambi, verrà restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo DefaultRetention del bucket. |
| `Retention-legal-hold-id` | stringa | Una singola conservazione a fini legali da applicare all'oggetto. Una conservazione a fini legali è una stringa di caratteri di lunghezza Y. L'oggetto non può essere sovrascritto o eliminato finché non sono state rimosse tutte le conservazioni a fini legali a esso associate. |

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

### Aggiungi/rimuovi una conservazione a fini legali a/da un oggetto protetto
{: #python-examples-immutable-legal-hold}

L'oggetto può supportare 100 conservazioni a fini legali:

*  Un identificativo di conservazione a fini locali è una stringa di una lunghezza massima di 64 caratteri e una lunghezza minima di 1 carattere. I caratteri validi sono lettere, numeri, `!`, `_`, `.`, `*`, `(`, `)`, `-` e `.
* Se l'aggiunta di una specifica conservazione a fini legali comporta il superamento di un totale di 100 conservazioni a fini legali sull'oggetto, la nuova conservazione a fini legali non viene aggiunta e viene restituito un errore `400`.
* Se un identificativo è troppo lungo, non viene aggiunto all'oggetto e viene restituito un errore `400`.
* Se un identificativo contiene caratteri non validi, non viene aggiunto all'oggetto e viene restituito un errore `400`.
* Se un identificativo è già in uso su un oggetto, la conservazione a fini legali esistente non viene modificata e la risposta indica che l'identificativo era già in uso con un errore `409`.
* Se un oggetto non ha metadati del periodo di conservazione, viene restituito un errore `400` e l'aggiunta o la rimozione di una conservazione a fini legali non è consentita.


L'utente che esegue l'aggiunta o la rimozione di una conservazione a fini legali deve disporre delle autorizzazioni di `Manager` (gestore) per questo bucket.


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

### Estendi il periodo di conservazione di un oggetto protetto
{: #python-examples-immutable-extend}

Il periodo di conservazione di un oggetto può solo essere esteso. Non può essere ridotto rispetto al valore attualmente configurato.

Il valore di espansione della conservazione è impostato in uno di tre possibili modi:

* ulteriore tempo dal valore corrente (`Additional-Retention-Period` o metodo simile)
* nuovo periodo di estensione in secondi (`Extend-Retention-From-Current-Time` o metodo simile)
* nuova data di scadenza della conservazione dell'oggetto (`New-Retention-Expiration-Date` o metodo simile)

Il periodo di conservazione corrente memorizzato nei metadati dell'oggetto viene aumentato in misura equivalente al tempo aggiuntivo indicato oppure sostituito con il nuovo valore, a seconda del parametro impostato nella richiesta `extendRetention`. In tutti i casi, il parametro di estensione della conservazione viene controllato rispetto al periodo di conservazione corrente e il parametro esteso viene accettato solo se il periodo di conservazione aggiornato è più grande del periodo di conservazione corrente. 

Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket.



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

### Elenca le conservazioni a fini legali su un oggetto protetto
{: #python-examples-immutable-list-holds}

Questa operazione restituisce:

* La data di creazione dell'oggetto
* Il periodo di conservazione dell'oggetto in secondi
* Data di scadenza della conservazione calcolata sulla base del periodo e della data di creazione
* Elenco delle conservazioni a fini legali
* Identificativo della conservazione a fini legali
* Data/ora di quando è stata applicata la conservazione a fini legali 

Se non ci sono conservazioni a fini legali sull'oggetto, viene restituito un `LegalHoldSet` vuoto.
Se non c'è alcun periodo di conservazione specificato sull'oggetto, viene restituito un errore `404`.


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

