---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cli, open source, minio

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

# Utilisation du client Minio
{: #minio}

Voulez-vous utiliser des commandes de type UNIX bien connues (`ls`, `cp`, `cat`, etc.) avec {{site.data.keyword.cos_full}} ? Si tel est le cas, le [client Minio](https://min.io/download#/linux){:new_window} open source est ce qu'il vous faut. Vous trouverez des instructions d'installation pour chaque système d'exploitation dans le [guide de démarrage rapide](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} sur le site Web de Minio. 

## Configuration
{: #minio-config}

L'ajout de votre {{site.data.keyword.cos_short}} est effectué en exécutant la commande suivante :

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - Nom abrégé permettant de référencer {{site.data.keyword.cos_short}} dans les commandes. 
* `<COS-ENDPOINT` - Noeud final pour votre instance {{site.data.keyword.cos_short}}. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<ACCESS-KEY>` - Clé d'accès qui est affectée à vos données d'identification de service. 
* `<SECRET-KEY>` - Clé secrète qui est affectée à vos données d'identification de service. 

Les informations de configuration sont stockées dans un fichier JSON, dans `~/.mc/config.json`. 

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## Exemples de commandes
{: #minio-commands}

La liste complète des commandes et des options et paramètres facultatifs est décrite dans le document [Minio Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window}. 

### `mb` - Création d'un compartiment
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - Création de la liste de compartiments
{: #minio-ls}

Bien que tous vos compartiments disponibles soient répertoriés, les objets ne sont pas peut-être pas tous accessibles selon la région de [noeud final](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) spécifiée.
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - Création de la liste d'objets
{: #minio-ls-objects}

```
mc ls cos/testbucket1
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
[2018-08-10 09:49:08 HST]  20MiB newbigfile.pdf
[2018-11-29 09:53:15 HST]    31B testsave.txt
```

### `find` - Recherche d'objets par nom
{: #minio-find}

Une liste complète d'options de recherche est disponible dans le document [Minio Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}.
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - Affichage de quelques lignes d'objet
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - Copie d'objets
{: #minio-cp}

Cette commande permet de copier un objet entre deux emplacements. Ces emplacements peuvent être différents hôtes (tels que des [noeuds finaux](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) ou des services de stockage différents) ou des emplacements de systèmes de fichiers locaux (par exemple, `~/foo/filename.pdf`). 
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - Retrait d'objets
{: #minio-rm}

*D'autres options de retrait sont disponibles dans le document [Minio Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window}*. 

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - Copie d'entrée standard dans un objet
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
