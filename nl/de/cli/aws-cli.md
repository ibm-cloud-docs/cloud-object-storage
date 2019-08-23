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

# AWS-Befehlszeilenschnittstelle verwenden
{: #aws-cli}

Die offizielle Befehlszeilenschnittstelle (CLI) für AWS ist mit der IBM COS-S3-API kompatibel. Sie wurde in Python geschrieben und kann über Python Package Index mithilfe von `pip install awscli` installiert werden. Standardmäßig stammen die Zugriffsschlüssel aus `~/.aws/credentials`, sie können jedoch auch als Umgebungsvariablen festgelegt werden.

Diese Beispiele wurden mit Version 1.14.2 der Befehlszeilenschnittstelle generiert. Führen Sie `aws -- version` aus, um zu überprüfen, welche Version auf Ihrem System installiert ist.

## Befehlszeilenschnittstelle zur Verbindung mit {{site.data.keyword.cos_short}} konfigurieren
{: #aws-cli-config}

Um die AWS-Befehlszeilenschnittstelle zu konfigurieren, müssen Sie `aws configure` eingeben und dann Ihre [HMAC-Berechtigungsnachweise](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) und einen standardmäßigen Regionsnamen angeben. Der von AWS S3 verwendete 'Regionsname' entspricht dem Bereitstellungscode (`LocationConstraint`), der von {{site.data.keyword.cos_short}} zum Definieren der Speicherklasse neuer Buckets verwendet wird.

Eine Liste gültiger Bereitstellungscodes für `LocationConstraint` finden Sie im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

Mit dieser Angabe werden zwei Dateien erstellt:

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


Zum Festlegen der HMAC-Berechtigungsnachweise können Sie auch Umgebungsvariablen verwenden:

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


Der IBM COS-Endpunkt muss anhand der Option `--endpoint-url` abgeleitet werden und kann nicht in der Berechtigungsnachweisdatei festgelegt werden.


## Befehle mit allgemeiner Syntax
{: #aws-cli-high-level}

Mit dem Befehl `aws --endpoint-url {endpoint} s3 <command>` können Sie einfache Anwendungsfälle erstellen. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Objekte werden anhand gängiger Shellbefehle wie beispielsweise `ls`, `mv`, `cp` und `rm` verwaltet. Buckets können mit dem Befehl `mb` erstellt und mit `rb` gelöscht werden.

### Alle Buckets einer Serviceinstanz auflisten
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### Objekte in einem Bucket auflisten
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### Neues Bucket erstellen
{: #aws-cli-high-level-new-bucket}

**Hinweis**: Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben identifiziert werden kann.
{:tip}

Wenn für die Standardregion in der Datei `~/.aws/config` derselbe Standort wie für den ausgewählten Endpunkt angegeben ist, dann ist die Bucketerstellung ein einfacher Vorgang.

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### Objekt zu Bucket hinzufügen
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

Alternativ hierzu können Sie einen neuen Objektschlüssel festlegen, der sich vom Dateinamen unterscheidet:

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### Objekt aus Bucket in anderes Bucket in derselben Region kopieren:
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### Objekt aus Bucket löschen
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### Bucket entfernen
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### Vorsignierte URLs erstellen
{: #aws-cli-high-level-presign}

Über die Befehlszeilenschnittstelle können auch vorsignierte URLs erstellt werden. Diese ermöglichen den temporären öffentlichen Zugriff auf Objekte, ohne dass hierzu vorhandene Zugriffssteuerungsmechanismen geändert werden müssen. Die generierte URL enthält eine HMAC-Signatur, mit der der URI verschlüsselt wird, sodass das Risiko reduziert werden kann, dass Benutzer, die nicht über die vollständige URL verfügen, auf eine ansonsten öffentlich zugängliche Datei zugreifen können.

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

Sie können auch eine Ablaufzeit für die URL in Sekunden angeben (Standardwert ist 3600):

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## Befehle mit maschinennaher Syntax
{: #aws-cli-low-level}

Die AWS-Befehlszeilenschnittstelle ermöglicht auch direkte API-Aufrufe, die die gleichen Antworten wie direkte HTTP-Anforderungen zurückgeben. Hierzu wird der Befehl `s3api` verwendet.

### Buckets auflisten:
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

### Objekte in einem Bucket auflisten
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
