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

# Utilizza la CLI AWS
{: #aws-cli}

La CLI (command line interface) ufficiale per AWS è compatibile con l'API S3 di IBM COS. Scritta in Python, può essere installata da PyPI (Python Package Index) tramite `pip install awscli`. Per impostazione predefinita, le chiavi di accesso provengono da `~/.aws/credentials`, ma possono anche essere impostate come variabili di ambiente. 

Questi esempi sono stati generati utilizzando la versione 1.14.2 della CLI. Per controllare la versione installata, esegui `aws --version`.

## Configura la CLI per connetterti a {{site.data.keyword.cos_short}}
{: #aws-cli-config}

Per configurare la CLI AWS, immetti `aws configure` e fornisci le tue [credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) e un nome regione predefinito. Il "nome regione" utilizzato da AWS S3 corrisponde al codice di provisioning (`LocationConstraint`) utilizzato da {{site.data.keyword.cos_short}} per definire la classe di archiviazione dei nuovi bucket.

Puoi fare riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida delle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

Ciò crea due file:

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


Puoi anche utilizzare le variabili di ambiente per impostare le credenziali HMAC:

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


L'endpoint IBM COS può essere originato utilizzando l'opzione `--endpoint-url` e non può essere impostato nel file delle credenziali. 


## Comandi con sintassi di alto livello
{: #aws-cli-high-level}

I casi di utilizzo semplici possono essere eseguiti utilizzando `aws --endpoint-url {endpoint} s3 <command>`. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Gli oggetti vengono gestiti utilizzando comandi shell familiari, ad esempio `ls`, `mv`, `cp` e `rm`. I bucket possono essere creati utilizzando `mb` ed eliminati utilizzando `rb`.

### Elenca tutti i bucket all'interno di un'istanza del servizio
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### Elenca gli oggetti all'interno di un bucket
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### Crea un nuovo bucket
{: #aws-cli-high-level-new-bucket}

**Nota**: Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
{:tip}

Se la regione predefinita nel file `~/.aws/config` corrisponde alla stessa ubicazione dell'endpoint scelto, la creazione del bucket è semplice. 

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### Aggiungi un oggetto a un bucket
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

In alternativa, puoi impostare una nuova chiave oggetto che sia diversa dal nome file: 

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### Copia di un oggetto da un bucket a un altro all'interno della stessa regione:
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### Elimina un oggetto da un bucket
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### Rimuovi un bucket
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### Crea gli URL prefirmati
{: #aws-cli-high-level-presign}

La CLI è anche in grado di creare gli URL prefirmati. Questi consentono un accesso pubblico temporaneo agli oggetti senza modificare i controlli di accesso esistenti. L'URL che viene generato contiene una firma HMAC che offusca l'URI, riducendo la probabilità che gli utenti senza l'URL completo possano accedere a un file altrimenti accessibile pubblicamente. 

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

È anche possibile impostare una scadenza per l'URL espressa in secondi (il valore predefinito è 3600):

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## Comandi con sintassi di basso livello
{: #aws-cli-low-level}

La CLI AWS consente anche le chiamate API dirette che forniscono le stesse risposte delle richieste HTTP dirette utilizzando il comando `s3api`.

### Elenco dei bucket:
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

### Elenco degli oggetti all'interno di un bucket
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
