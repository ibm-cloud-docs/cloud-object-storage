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

# Utilisation de l'interface CLI AWS
{: #aws-cli}

L'interface de ligne de commande officielle pour AWS est compatible avec l'API S3 IBM COS. Ecrite en Python, elle peut être installée à partir de Python Package Index via la commande `pip install awscli`. Par défaut, les clés d'accès sont sourcées à partir de `~/.aws/credentials`, mais peuvent aussi être définies en tant que variables d'environnement. 

Ces exemples ont été générés à l'aide de la version 1.14.2 de l'interface de ligne de commande. Pour vérifier la version installée, exécutez la commande `aws -- version`. 

## Configuration de l'interface CLI pour établir une connexion à {{site.data.keyword.cos_short}}
{: #aws-cli-config}

Pour configurer l'interface CLI AWS, tapez `aws configure` et indiquez vos [données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) et un nom de région par défaut. Le "nom de région" utilisé par AWS S3 correspond au code de mise à disposition (`LocationConstraint`) dont se sert {{site.data.keyword.cos_short}} pour définir la classe de stockage des nouveaux compartiments. 

Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

Cela permet de créer deux fichiers : 

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


Vous pouvez également utiliser des variables d'environnement pour définir des données d'identification HMAC :

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


Le noeud final IBM COS doit être sourcé à l'aide de l'option `--endpoint-url` et ne peut pas être défini dans le fichier de données d'identification. 


## Commandes de syntaxe de haut niveau
{: #aws-cli-high-level}

Des cas d'utilisation simples peuvent être réalisés à l'aide de `aws --endpoint-url {endpoint} s3 <command>`. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Les objets sont gérés à l'aide de commandes shell bien connues, telles que `ls`, `mv`, `cp` et `rm`. Les compartiments peuvent être créés à l'aide de `mb` et supprimés à l'aide de `rb`. 

### Création de la liste de tous les compartiments contenus dans une instance de service
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### Création de la liste des objets contenus dans un compartiment
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### Création d'un nouveau compartiment
{: #aws-cli-high-level-new-bucket}

**Remarque** : Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose. {:tip}

Si la région par défaut dans le fichier `~/.aws/config` correspond au même emplacement que le noeud final choisi, la création de compartiment est simple. 

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### Ajout d'un objet à un compartiment
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

Vous pouvez aussi définir une nouvelle clé d'objet qui est différente du nom de fichier :

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### Copie d'un objet à partir d'un compartiment vers un autre compartiment situé dans la même région
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### Suppression d'un objet d'un compartiment
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### Retrait d'un compartiment
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### Création d'URL présignées
{: #aws-cli-high-level-presign}

L'interface CLI peut également créer des URL présignées. Ces dernières permettent au public d'accéder temporairement aux objets sans avoir à modifier les contrôles d'accès existants. L'URL qui est générée contient une signature HMAC qui brouille l'URI, réduisant ainsi la possibilité pour les utilisateurs ne disposant pas de l'URL complète d'accéder à un fichier par ailleurs accessible au public. 

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

Il est également possible de définir un délai d'expiration pour l'URL (la valeur par défaut est 3600 secondes) :

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## Commandes de syntaxe de bas niveau
{: #aws-cli-low-level}

L'interface CLI AWS permet également d'émettre des appels d'API directs qui fournissent les mêmes réponses que les demandes HTTP directes à l'aide de la commande `s3api`. 

### Création de la liste de compartiments :
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

### Création de la liste des objets contenus dans un compartiment
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
