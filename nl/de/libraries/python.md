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

# Python verwenden
{: #python}

Die Python-Unterstützung wird über eine Verzweigung der Bibliothek `boto3` bereitgestellt. Sie kann vom Python-Paketindex über `pip install ibm-cos-sdk` installiert werden.

Den Quellcode finden Sie unter [GitHub](https://github.com/ibm/ibm-cos-sdk-python/).

Von der Bibliothek `ibm_boto3` wird vollständiger Zugriff auf die {{site.data.keyword.cos_full}}-API bereitgestellt. Endpunkte, ein API-Schlüssel und die Instanz-ID müssen während der Erstellung einer Serviceressource oder eines Low-Level-Clients wie in den folgenden einfachen Beispielen dargestellt angegeben werden.

Die Serviceinstanz-ID wird auch als _Ressourceninstanz-ID_ bezeichnet. Der Wert kann durch Erstellen eines [Serviceberechtigungsnachweises](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) oder über eine Befehlszeilenschnittstelle gesucht werden.
{:tip}

Detaillierte Dokumentation finden Sie [hier](https://ibm.github.io/ibm-cos-sdk-python/).

## Upgrade von 1.x.x durchführen
{: #python-migrate}

Mit Version 2.0 des SDKs wird eine Änderung der Namensbereichsfestlegung eingeführt, die es einer Anwendung ermöglicht, in derselben Anwendung oder Umgebung die ursprüngliche Bibliothek `boto3` für den Verbindungsaufbau zu AWS-Ressourcen zu verwenden. Für die Migration von Version 1.x auf 2.0 sind einige Änderungen erforderlich.

    1. Aktualisieren Sie die Datei `requirements.txt` oder von PyPI über `pip install -U ibm-cos-sdk`. Vergewissern Sie sich mit `pip list | grep ibm-cos`, dass keine älteren Versionen vorhanden sind.
    2. Ändern Sie alle Importdeklarationen von `boto3` in `ibm_boto3`.
    3. Aktualisieren Sie bei Bedarf die ursprüngliche Bibliothek `boto3` durch Aktualisieren der Datei `requirements.txt` oder von PyPI über `pip install boto3`.

## Client erstellen und Berechtigungsnachweise ableiten
{: #python-credentials}

Zum Herstellen einer Verbindung zu COS wird ein Client erstellt und durch Bereitstellen der Berechtigungsnachweisinformationen (API-Schlüssel und Serviceinstanz-ID) konfiguriert. Als Quelle für diese Werte können auch automatisch eine Berechtigungsnachweisdatei oder Umgebungsvariablen verwendet werden.

Nach dem Generieren eines [Serviceberechtigungsnachweises](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) kann das daraus resultierende JSON-Dokument unter `~/.bluemix/cos_credentials` gespeichert werden. Vom SDK wird diese Datei automatisch als Quelle für die Berechtigungsnachweise verwendet, sofern während der Clienterstellung nicht explizit andere Berechtigungsnachweise festgelegt werden. Wenn die Datei `cos_credentials` HMAC-Schlüssel enthält, wird die Authentifizierung vom Client mithilfe einer Signatur durchgeführt; andernfalls wird vom Client für die Authentifizierung der bereitgestellte API-Schlüssel mit einem Bearer-Token verwendet.

Falls Sie eine Migration von AWS S3 durchführen, können Sie die Daten für die Berechtigungsnachweise aus `~/.aws/credentials` im folgenden Format verwenden:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Wenn sowohl `~/.bluemix/cos_credentials` als auch `~/.aws/credentials` vorhanden ist, genießt `cos_credentials` Vorrang.

### Erforderliche Informationen erfassen
{: #python-prereqs}

In den Beispielen werden die folgenden Variablen verwendet:

* `bucket_name` muss eine [eindeutige und DNS-sichere](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket) Zeichenfolge sein. Da Bucketnamen im gesamten System eindeutig sind, müssen diese Werte geändert werden, wenn dieses Beispiel mehrfach ausgeführt wird. Beachten Sie, dass die Namen noch zehn bis fünfzehn Minuten nach ihrer Löschung reserviert bleiben.
* `ibm_api_key_id` ist der Wert, der im [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) als `apikey` gefunden wird.
* `ibm_service_instance_id` ist der Wert, der im [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) als `resource_instance_id` gefunden wird. 
* `endpoint_url` ist eine Serviceendpunkt-URL, einschließlich des Protokolls `https://`. Dieser Wert ist **nicht** der Wert für `endpoints`, der im [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) gefunden wird. Weitere Informationen zu Endpunkten finden Sie unter [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `LocationConstraint` ist ein [gültiger Bereitstellungscode](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint), der dem Wert von `endpoint` entspricht. 


## Codebeispiele
{: #python-examples}

Die Codebeispiele wurden unter Verwendung von **Python 2.7.15** geschrieben.

### Konfiguration initialisieren
{: #python-examples-init}

  
```python
import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Konstanten für IBM COS-Werte
COS_ENDPOINT = "<endpoint>" # Aktuelle Liste verfügbar unter https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # Zum Beispiel "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>" # Zum Beispiel "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Ressource erstellen
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)
```
*Schlüsselwerte*
* `<endpoint>` - Öffentlicher Endpunkt für Cloud Object Storage mit einem Schema mit Präfix ('https://') (verfügbar im [IBM Cloud-Dashboard](https://cloud.ibm.com/resources){:new_window}). Weitere Informationen zu Endpunkten finden Sie unter [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - API-Schlüssel, der beim Erstellen der Serviceberechtigungsnachweise generiert wird (Schreibzugriff für Erstellungs- und Löschbeispiele erforderlich)
* `<resource-instance-id>` - Ressourcen-ID für Cloud Object Storage (verfügbar über [IBM Cloud-Befehlszeilenschnittstelle](/docs/cli?topic=cloud-cli-idt-cli) oder [IBM Cloud-Dashboard](https://cloud.ibm.com/resources){:new_window})
* `<location>` - Standardstandort für Cloud Object Storage (muss mit der Region übereinstimmen, die für `<endpoint>` verwendet wird)

*SDK-Referenzen*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### Neues Bucket erstellen
{: #python-examples-new-bucket}

Auf eine Liste gültiger Bereitstellungscodes für `LocationConstraint` kann im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes) verwiesen werden.

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

*SDK-Referenzen*
* Klassen
  * [`Bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Methoden
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### Neue Textdatei erstellen
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

*SDK-Referenzen*
* Klassen
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Methoden
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### Verfügbare Buckets auflisten
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

*SDK-Referenzen*
* Klassen
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* Sammlungen
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### Elemente in Bucket auflisten
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

*SDK-Referenzen*
* Klassen
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* Sammlungen
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### Dateiinhalt eines bestimmten Elements abrufen
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

*SDK-Referenzen*
* Klassen
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Methoden
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### Element in Bucket löschen
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

*SDK-Referenzen*
* Klassen
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Methoden
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### Mehrere Elemente in Bucket löschen
{: #python-examples-delete-multiple-objects}

Eine Löschanforderung kann maximal 1000 Schlüssel enthalten, die gelöscht werden sollen. Dies ist zwar nützlich, um den Aufwand pro Anforderung zu reduzieren, gehen Sie beim Löschen vieler Schlüsseln jedoch mit Sorgfalt vor. Berücksichtigen Sie auch die Größen der Objekte, um eine geeignete Leistung zu gewährleisten.{:tip}

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

*SDK-Referenzen*
* Klassen
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Methoden
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### Bucket löschen
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

*SDK-Referenzen*
* Klassen
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Methoden
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### Mehrteiliges Hochladen ausführen
{: #python-examples-multipart}

#### Binärdatei hochladen (bevorzugte Methode)
{: #python-examples-multipart-binary}

Bei Verwendung der Methode [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} der Klasse [S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} wird bei Bedarf automatisch ein mehrteiliger Upload ausgeführt. Die Klasse [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} wird verwendet, um den Schwellenwert für die Verwendung des mehrteiligen Uploads zu bestimmen.

```python
def multi_part_upload(bucket_name, item_name, file_path):
    try:
        print("Starting file transfer for {0} to bucket: {1}\n".format(item_name, bucket_name))
        # 5 MB-Datenblöcke festlegen
        part_size = 1024 * 1024 * 5

        # 15 MB-Schwellenwert festlegen
        file_threshold = 1024 * 1024 * 15

        # Übertragungsschwellenwert und Datenblockgröße festlegen
        transfer_config = ibm_boto3.s3.transfer.TransferConfig(
            multipart_threshold=file_threshold,
            multipart_chunksize=part_size
        )

        # Von der Methode upload_fileobj wird automatisch ein mehrteiliger Upload
        # in 5 MB-Datenblöcken für alle Dateien mit einer Größe über 15 MB ausgeführt
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

*SDK-Referenzen*
* Klassen
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* Methoden
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### Mehrteiliges Hochladen manuell ausführen
{: #python-examples-multipart-manual}

Bei Bedarf kann die Klasse [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} zum Ausführen eines mehrteiligen Uploads verwendet werden. Dies kann nützlich sein, wenn mehr Kontrolle über den Uploadprozess erforderlich ist.

```python
def multi_part_upload_manual(bucket_name, item_name, file_path):
    try:
        # Clientobjekt erstellen
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT
        )

        print("Starting multi-part upload for {0} to bucket: {1}\n".format(item_name, bucket_name))

        # Mehrteiligen Upload starten
        mp = cos_cli.create_multipart_upload(
            Bucket=bucket_name,
            Key=item_name
        )

        upload_id = mp["UploadId"]

        # Mindestgröße der Teile ist 5 MB
        part_size = 1024 * 1024 * 5
        file_size = os.stat(file_path).st_size
        part_count = int(math.ceil(file_size / float(part_size)))
        data_packs = []
        position = 0
        part_num = 0

        # Mit Hochladen der Teile beginnen
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

        # Upload abschließen
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

*SDK-Referenzen*
* Klassen
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Methoden
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### Upload großer Objekte mit TransferManager
{: #python-examples-multipart-transfer}

Von `TransferManager` wird eine weitere Möglichkeit zum Ausführen großer Dateiübertragen durch das automatische Integrieren mehrteiliger Uploads bereitgestellt, sofern das Einstellen von Konfigurationsparametern erforderlich ist.

```python
def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # Für Datenblockgröße 5 MB festlegen
    part_size = 1024 * 1024 * 5

    # Für Schwellenwert 5 MB festlegen
    file_threshold = 1024 * 1024 * 5

    # Clientverbindung erstellen
    cos_cli = ibm_boto3.client("s3",
        ibm_api_key_id=COS_API_KEY_ID,
        ibm_service_instance_id=COS_SERVICE_CRN,
        ibm_auth_endpoint=COS_AUTH_ENDPOINT,
        config=Config(signature_version="oauth"),
        endpoint_url=COS_ENDPOINT
    )

    # Übertragungsschwellenwert und Datenblockgröße in Konfigurationseinstellungen festlegen
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # Übertragungsmanager erstellen
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_cli, config=transfer_config)

    try:
        # Dateiupload starten
        future = transfer_mgr.upload(file_path, bucket_name, item_name)

        # Auf Abschluss des Uploads warten
        future.result()

        print ("Large file upload complete!")
    except Exception as e:
        print("Unable to complete large file upload: {0}".format(e))
    finally:
        transfer_mgr.shutdown()
```

### Elemente in Bucket auflisten (v2)
{: #python-examples-list-objects-v2}

Das Objekt [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} enthält eine aktualisierte Methode zum Auflisten des Inhalts ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}). Mit dieser Methode können Sie die Anzahl der zurückgegebenen Datensätze begrenzen und die Datensätze stapelweise abrufen. Dies kann beim Paging der Ergebnisse in einer Anwendung nützlich sein und die Leistung verbessern.

```python
def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        # Clientobjekt erstellen
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

*SDK-Referenzen*
* Klassen
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Methoden
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Key Protect verwenden
{: #python-examples-kp}
Key Protect kann zu einem Speicherbucket hinzugefügt werden, um sensible ruhende Daten in der Cloud zu verschlüsseln.

### Vorbereitende Schritte
{: #python-examples-kp-prereqs}

Damit Sie ein Bucket erstellen können, während Key Protect aktiviert ist, müssen die folgenden Voraussetzungen erfüllt sein:

* Ein Key Protect-Service wird [bereitgestellt](/docs/services/key-protect?topic=key-protect-provision)
* Ein Stammschlüssel ist verfügbar (entweder [generiert](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) oder [importiert](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Cloudressourcenname für Stammschlüssel abrufen
{: #python-examples-kp-root}

1. Rufen Sie die [Instanz-ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) für den Key Protect-Service ab.
2. Verwenden Sie die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api), um alle [verfügbaren Schlüssel](https://cloud.ibm.com/apidocs/key-protect) abzurufen.
    * Sie können entweder `curl`-Befehle oder einen API-REST-Client wie zum Beispiel [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) für den Zugriff auf die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) verwenden.
3. Rufen Sie den Cloudressourcennamen des Stammschlüssels ab, mit dem Sie Key Protect für das Bucket aktivieren. Der Cloudressourcenname ähnelt der folgenden Zeichenfolge:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Bucket während Aktivierung von Key Protect erstellen
{: #python-examples-kp-new-bucket}
```python
COS_KP_ALGORITHM = "<algorithm>"
COS_KP_ROOTKEY_CRN = "<root-key-crn>"

# Neues Bucket mit Key Protect (Verschlüsselung) erstellen
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

*Schlüsselwerte*
* `<algorithm>` - Der Verschlüsselungsalgorithmus, der für neue Objekte verwendet wird, die zum Bucket hinzugefügt wurden (Standard ist AES256).
* `<root-key-crn>` - Der Cloudressourcenname (CRN) des Stammschlüssels, der vom Key Protect-Service abgerufen wird.

*SDK-Referenzen*
* Klassen
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Methoden
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Aspera-Hochgeschwindigkeitsübertragung verwenden
{: #python-examples-aspera}

Durch die Installation der [Bibliothek für die Aspera-Hochgeschwindigkeitsübertragung](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging) können Sie Hochgeschwindigkeitsdateiübertragungen innerhalb der Anwendung verwenden. Die Aspera-Bibliothek ist keine Open Source-Bibliothek und somit eine optionale Abhängigkeit für das COS-SDK (von dem eine Apache-Lizenz verwendet wird).

Während jeder Aspera-Sitzung wird ein einzelner `ascp`-Prozess erstellt, der auf der Clientmaschine zur Durchführung der Übertragung ausgeführt wird. Stellen Sie sicher, dass dieser Prozess in Ihrer Systemumgebung ausgeführt werden kann.
{:tip}


### AsperaTransferManager initialisieren
{: #python-examples-aspera-init}

Stellen Sie vor der Initialisierung von `AsperaTransferManager` sicher, dass Sie mit dem Objekt [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} (nicht `resource` oder `session`) arbeiten.

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_s3transfer.aspera.manager import AsperaTransferManager

COS_ENDPOINT = "<endpoint>" # Aktuelle Liste verfügbar unter https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location>"

# Ressource erstellen
cos = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

transfer_manager = AsperaTransferManager(cos)
```

Für Aspera-Hochgeschwindigkeitsübertragungen müssen Sie einen IAM-API-Schlüssel angeben. [HMAC-Berechtigungsnachweise](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} werden derzeit **NICHT** unterstützt. Weitere Informationen zu IAM finden Sie [hier](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

Wenn Sie den höchsten Durchsatz erreichen möchten, teilen Sie die Übertragung in parallele **Sitzungen** auf, von denen Datenblöcke gesendet werden, deren Größe durch einen **Schwellenwert** definiert werden.

Eine typische Konfiguration für die Verwendung von Mehrfachsitzungen muss folgende Merkmale aufweisen:
* 2.500 MB/s Zielrate
* 100 MB Schwellenwert (*dies ist der empfohlene Wert für die meisten Anwendungen*)

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
Im obigen Beispiel werden vom SDK ausreichend Sitzungen zum Erreichen der Zielrate von 2.500 MB/s generiert.

Alternativ dazu kann das Sitzungsmanagement im SDK explizit konfiguriert werden. Dies ist in den Fällen nützlich, in denen eine genauere Kontrolle über die Netzauslastung gewünscht wird.

Eine typische Konfiguration für die Verwendung expliziter Mehrfachsitzungen muss folgende Merkmale aufweisen:
* 2 oder 10 Sitzungen
* 100 MB Schwellenwert (*dies ist der empfohlene Wert für die meisten Anwendungen*)

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# 2 Sitzungen für Datenübertragung konfigurieren
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Aspera-Übertragungsmanager erstellen
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
Verwenden Sie zur Optimierung der Leistung in den meisten Szenarios immer mehrere Sitzungen, um den Aufwand zu minimieren, der mit der Instanziierung der Aspera-Hochgeschwindigkeitsübertragung verbunden ist. **Falls die Netzkapazität mindestens 1 Gb/s beträgt, sollten Sie 10 Sitzungen verwenden.** In Netzen mit niedrigerer Bandbreite sollten zwei Sitzungen verwendet werden.
{:tip}

### Dateiupload
{: #python-examples-aspera-upload}
```python
bucket_name = "<bucket-name>"
upload_filename = "<absolute-path-to-file>"
object_name = "<item-name>"

# Übertragungsmanager erstellen
with AsperaTransferManager(client) as transfer_manager:

    # Upload ausführen
    future = transfer_manager.upload(upload_filename, bucket_name, object_name)

    # Auf Abschluss des Uploads warten
    future.result()
```

*Schlüsselwerte*
* `<bucket-name>` - Name des Zielbuckets
* `<absolute-path-to-file>` - Verzeichnispfad und Dateiname für die Datei, die hochgeladen werden soll
* `<item-name>` - Name der neuen Datei, die zum Bucket hinzugefügt wird

### Dateidownload
{: #python-examples-aspera-download}

```python
bucket_name = "<bucket-name>"
download_filename = "<absolute-path-to-file>"
object_name = "<object-to-download>"

# Übertragungsmanager erstellen
with AsperaTransferManager(client) as transfer_manager:

    # Objekt mit Aspera abrufen
    future = transfer_manager.download(bucket_name, object_name, download_filename)

    # Auf Abschluss des Downloads warten
    future.result()
```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-file>` - Verzeichnis und Dateiname zum Speichern der Datei auf dem lokalen System
* `<object-to-download>` - Name der Datei im Bucket, die heruntergeladen werden soll

### Verzeichnisupload
{: #python-examples-aspera-directory-upload}

```python
bucket_name = "<bucket-name>"
# DIESES VERZEICHNIS MUSS LOKAL VORHANDEN SEIN und in ihm müssen Objekte enthalten sein.
local_upload_directory = "<absolute-path-to-directory>"
# AM ANFANG DARF HIER KEIN SCHRÄGSTRICH "/" STEHEN
remote_directory = "<object prefix>"

# Übertragungsmanager erstellen
with AsperaTransferManager(client) as transfer_manager:

    # Upload ausführen
    future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory)

    # Auf Abschluss des Uploads warten
    future.result()
```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-directory>` - Lokales Verzeichnis, das die hochzuladenen Dateien enthält. Muss einen führenden und abschließenden Schrägstrich `/` aufweisen (Beispiel: `/Users/testuser/Documents/Upload/`).
* `<object prefix>` - Name des Verzeichnisses im Bucket zum Speichern der Dateien. Darf am Anfang keinen Schrägstrich `/` aufweisen (Beispiel: `newuploads/`).

### Verzeichnisdownload
{: #python-examples-aspera-directory-download}

```python
bucket_name = "<bucket-name>"
# DIESES VERZEICHNIS MUSS LOKAL VORHANDEN SEIN
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Übertragungsmanager erstellen
with AsperaTransferManager(client) as transfer_manager:

    # Objekt mit Aspera abrufen
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory)

    # Auf Abschluss des Downloads warten
    future.result()
```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-directory>` - Lokales Verzeichnis zum Speichern der heruntergeladenen Dateien. Muss einen führenden und abschließenden Schrägstrich `/` aufweisen (Beispiel: `/Users/testuser/Downloads/`).
* `<object prefix>` - Name des Verzeichnisses im Bucket zum Speichern der Dateien. Darf keinen führenden Schrägstrich `/` aufweisen (d. h. `todownload/`)

### Abonnenten verwenden
{: #python-examples-aspera-subscribers}

Abonnenten ermöglichen in Übertragungen Beobachtbarkeit, weil angepasste Callback-Methoden zugeordnet werden können. Alle Übertragungen laufen in den folgenden Phasen ab:

`In die Warteschlange eingereiht - In Bearbeitung - Fertig`

Für jede Phase gibt es drei verfügbare Abonnenten:

* `CallbackOnQueued()` - Wird aufgerufen, wenn zu `AsperaTransferManager` eine neue Übertragung hinzugefügt wurde.
* `CallbackOnProgress()` - Wird aufgerufen, wenn in einer Übertragung der Transfer der Daten begonnen hat (wird wiederholt gestartet, während die Übertragung in Bearbeitung ist).
* `CallbackOnDone()` - Wird aufgerufen, sobald die Übertragung abgeschlossen ist.

```python
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Callbacks für Abonnenten
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

# Übertragungsmanager erstellen
transfer_manager = AsperaTransferManager(client)

# Abonnenten zuordnen
subscribers = [CallbackOnQueued(), CallbackOnProgress(), CallbackOnDone()]

# Objekt mit Aspera abrufen
future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, subscribers)

# Auf Abschluss des Downloads warten
future.result()
```

*Schlüsselwerte*
* `<bucket-name>` - Name des Buckets in der Object Storage-Serviceinstanz, für die Aspera aktiviert ist.
* `<absolute-path-to-directory>` - Lokales Verzeichnis zum Speichern der heruntergeladenen Dateien. Muss einen führenden und abschließenden Schrägstrich `/` aufweisen (Beispiel: `/Users/testuser/Downloads/`).
* `<object prefix>` - Name des Verzeichnisses im Bucket zum Speichern der Dateien. Darf keinen führenden Schrägstrich `/` aufweisen (d. h. `todownload/`)

Vom obigen Beispielcode wird die folgende Ausgabe erstellt:

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### Anhalten/Wiederaufnehmen/Abbrechen
{: #python-examples-aspera-pause}

Das SDK bietet die Möglichkeit, den Fortschritt der Datei- bzw. Verzeichnisübertragungen über die folgenden Methoden des Objekts `AsperaTransferFuture` zu verwalten:

* `pause()`
* `resume()`
* `cancel()`

In Bezug auf das Aufrufen der oben aufgeführten Methoden gibt es keine Nebeneffekte. Vom SDK wird die ordnungsgemäße Bereinigung und Verwaltung gewährleistet.
{:tip}

```python
# Übertragungsmanager erstellen
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

with AsperaTransferManager(client) as transfer_manager:

    # Verzeichnis mit Aspera herunterladen
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

    # Übertragung anhalten
    future.pause()

    # Übertragung wiederaufnehmen
    future.resume()

    # Übertragung abbrechen
    future.cancel()
```

### Fehlerbehebung bei Aspera-Problemen
{: #python-examples-aspera-ts}
**Problem:** Wenn Entwickler Python 2.7.15 unter Windows 10 verwenden, können Fehler beim Installieren des Aspera-SDKs auftreten.

**Ursache:** Wenn in der Umgebung mehrere unterschiedliche Versionen von Python installiert sind, können Installationsfehler auftreten, wenn Sie versuchen, das Aspera-SDK zu installieren. Ursache hierfür kann das Fehlen mindestens einer DLL-Datei oder das Vorhandensein einer falschen DLL-Datei im Pfad sein.

**Lösung:** Der erste Schritt zur Lösung dieses Problems besteht darin, die Aspera-Bibliotheken erneut zu installieren. Dies kann möglicherweise während der Installation fehlgeschlagen sein. Dies kann sich negativ auf die DLL-Dateien ausgewirkt haben. Falls sich die Probleme so nicht beheben lassen, müssen Sie die Version von Python aktualisieren. Wenn Sie dies nicht können, können Sie [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window} installieren. Auf diese Art können Sie das Aspera-SDK ohne Probleme installieren.

## Metadaten aktualisieren
{: #python-examples-metadata}
Zum Aktualisieren der Metadaten für ein vorhandenes Objekt stehen zwei Methoden zur Verfügung:
* Die Anforderung `PUT` mit den neuen Metadaten und dem Inhalt des ursprünglichen Objekts
* Die Ausführung der Anforderung `COPY` mit den neuen Metadaten, von denen das ursprüngliche Objekt als Kopierquelle angegeben wird

### PUT zum Aktualisieren der Metadaten verwenden
{: #python-examples-metadata-put}
**Hinweis:** Von der Anforderung `PUT` wird der vorhandene Inhalt des Objekts überschrieben; deswegen muss es zuerst heruntergeladen und danach mit den neuen Metadaten erneut hochgeladen werden.

```python
def update_metadata_put(bucket_name, item_name, key, value):
    try:
        # Vorhandes Element zum Erneuten Laden des Inhalts abrufen
        response = cos_cli.get_object(Bucket=bucket_name, Key=item_name)
        existing_body = response.get("Body").read()

        # Neue Metadaten festlegen
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

### COPY zum Aktualisieren der Metadaten verwenden
{: #python-examples-metadata-copy}

```python
def update_metadata_copy(bucket_name, item_name, key, value):
    try:
        # Neue Metdaten festlegen
        new_metadata = {
            key: value
        }

        # Eigene Position als Kopierquelle festlegen
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

## Unveränderlichen Objektspeicher verwenden
{: #python-examples-immutable}

### Schutzkonfiguration für vorhandenes Bucket hinzufügen
{: #python-examples-immutable-add}

Objekte, die in ein geschütztes Bucket geschrieben wurden, können erst gelöscht werden, wenn die Schutzdauer abgelaufen ist und alle gesetzlichen Bestimmungen zum Schutz des Objekts entfernt wurden. Einem Objekt wird der Standardaufbewahrungswert des Buckets zugewiesen, sofern für das Objekt bei seiner Erstellung nicht ein objektspezifischer Wert angegeben wurde. Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden. 

Die unterstützten Minimal- und Maximalwerte für die Einstellung der Aufbewahrungsdauer (`MinimumRetention`, `DefaultRetention` und `MaximumRetention`) sind jeweils 0 Tage und 365.243 Tage (1.000 Jahre). 

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

### Schutz für Bucket überprüfen
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


### Geschütztes Objekt hochladen
{: #python-examples-immutable-upload}

Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden.


| Wert | Typ	| Beschreibung |
| --- | --- | --- | 
|`Retention-Period` | Nicht negative Ganzzahl (Sekunden) | Aufbewahrungsdauer zum Speichern für das Objekt in Sekunden. Das Objekt kann so lange weder überschrieben noch gelöscht werden, bis die für die Aufbewahrungsdauer angegebene Zeit abgelaufen ist. Wenn dieses Feld und `Retention-Expiration-Date` angegeben werden, wird der Fehler `400` zurückgegeben. Wenn für beide Felder keine Werte angegeben werden, wird der Wert für den Zeitraum `DefaultRetention` des Buckets verwendet. Null (`0`) ist ein gültiger Wert und bedeutet, dass die Mindestaufbewahrungsdauer des Buckets `0` beträgt. |
| `Retention-expiration-date` | Datum (ISO 8601-Format) | Datum, an dem das Löschen oder Ändern des Objekts zulässig ist. Sie können nur diese Angabe oder den Header 'Retention-Period' angeben. Wenn beide angegeben werden, wird der Fehler `400` zurückgegeben. Wenn für beide Felder keine Werte angegeben werden, wird der Wert für den Zeitraum 'DefaultRetention' des Buckets verwendet. |
| `Retention-legal-hold-id` | Zeichenfolge | Eine einzelne gesetzliche Bestimmung (zum Beispiel eine Aufbewahrungspflicht), die auf das Objekt angewendet wird. Eine gesetzliche Bestimmung ist eine Zeichenfolge, die Y Zeichen lang ist. Das Objekt kann erst überschrieben oder gelöscht werden, wenn alle dem Objekt zugeordneten gesetzlichen Bestimmungen entfernt wurden. |

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

### Gesetzliche Bestimmung zu geschütztem Objekt hinzufügen oder aus geschütztem Objekt entfernen
{: #python-examples-immutable-legal-hold}

Von einem Objekt können 100 gesetzliche Bestimmungen unterstützt werden:

*  Eine Kennung für eine gesetzliche Bestimmung ist eine Zeichenfolge aus maximal 64 Zeichen und muss mindestens aus einem Zeichen bestehen. Gültige Zeichen sind Buchstaben, Ziffern, `!`, `_`, `.`, `*`, `(`, `)`, `-` und `.
* Wenn versucht wird, einem Objekt mehr 100 gesetzliche Bestimmungen hinzuzufügen, wird die neue gesetzliche Bestimmung nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung zu lang ist, wird sie dem Objekt nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung ungültige Zeichen enthält, wird sie dem Objekt nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung bereits für ein Objekt verwendet wird, wird die vorhandene gesetzliche Bestimmung nicht geändert; außerdem wird Fehler `409` mit der Antwort zurückgegeben, dass die Kennung bereits verwendet wird.
* Wenn ein Objekt keine Metadaten für die Aufbewahrungsdauer aufweist, wird der Fehler `400` zurückgegeben und das Hinzufügen oder Entfernen einer gesetzlichen Bestimmung ist nicht zulässig.


Der Benutzer, der gesetzliche Bestimmungen hinzufügt oder entfernt, muss über die Berechtigung `Manager` für dieses Bucket verfügen.


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

### Aufbewahrungsdauer eines geschützten Objekts erweitern
{: #python-examples-immutable-extend}

Die Aufbewahrungsdauer eines Objekts kann nur verlängert werden. Ein derzeit konfigurierter Wert kann nicht verringert werden.

Der Wert zur Verlängerung der Aufbewahrungsdauer kann auf drei Arten festgelegt werden:

* Zusätzliche Zeit auf der Basis des aktuellen Werts (`Additional-Retention-Period` oder eine ähnliche Methode)
* Neuer Erweiterungszeitraum in Sekunden (`Extend-Retention-From-Current-Time` oder eine ähnliche Methode)
* Neues Ablaufdatum für Aufbewahrungszeitraum des Objekts (`New-Retention-Expiration-Date` oder eine ähnliche Methode)

Der aktuelle Aufbewahrungszeitraum, der in den Objektmetadaten gespeichert ist, wird entweder durch die angegebene zusätzliche Zeit verlängert oder durch den neuen Wert ersetzt; dies hängt von dem Parameter ab, der in der Anforderung `extendRetention` festgelegt wird. In allen Fällen wird der Parameter für die Verlängerung des Aufbewahrungszeitraums mit den Angaben für den aktuellen Aufbewahrungszeitraum verglichen; der erweiterte Parameter wird nur akzeptiert, wenn der aktualisierte Aufbewahrungszeitraum größer ist als der aktuelle Aufbewahrungszeitraum.

Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden.



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

### Gesetzliche Bestimmungen für geschütztes Objekt auflisten
{: #python-examples-immutable-list-holds}

Nach dem Ausführen dieser Operation wird Folgendes zurückgegeben:

* Datum der Objekterstellung
* Aufbewahrungsdauer des Objekts in Sekunden
* Berechnetes Ablaufdatum der Aufbewahrungsdauer auf Basis des Zeitraums und des Erstellungsdatums
* Liste der gesetzlichen Bestimmungen
* Kennung der gesetzlichen Bestimmung
* Zeitmarke der Anwendung der gesetzlichen Bestimmung

Wenn auf ein Objekt keine gesetzlichen Bestimmungen angewendet werden, wird `LegalHoldSet` leer zurückgegeben. Wenn für ein Objekt keine Aufbewahrungsdauer angegeben wurde, wird der Fehler `404` zurückgegeben.


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

