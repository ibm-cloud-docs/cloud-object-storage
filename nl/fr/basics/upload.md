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

# Envoi par téléchargement de données
{: #upload}

Après avoir organisé vos compartiments, vous pouvez y ajouter des objets. Selon la manière dont vous souhaitez utiliser votre stockage, il existe différentes façons d'insérer des données dans le système. Un spécialiste des données possède quelques gros fichiers qui sont utilisés à des fins d'analyse, un administrateur système doit assurer la synchronisation des sauvegardes de base de données et des fichiers locaux, et un développeur crée un logiciel qui doit lire et écrire des millions de fichiers. Chacun de ces scénarios est servi au mieux par différentes méthodes d'ingestion de données. 

## Utilisation de la console
{: #upload-console}

La console basée sur le Web n'est pas couramment employée pour utiliser {{site.data.keyword.cos_short}}. Les objets sont limités à 200 Mo et le nom de fichier et la clé sont identiques. Plusieurs objets peuvent être envoyés par téléchargement en même temps, et si le navigateur autorise plusieurs unités d'exécution, chaque objet est envoyé par téléchargement en utilisant plusieurs parties en parallèle. La prise en charge des objets plus volumineux et des performances améliorées (en fonction des facteurs de réseau) est fournie par l'option [Transfert haut débit Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera). 

## Utilisation d'un outil compatible
{: #upload-tool}

Certains utilisateurs souhaitent utiliser un utilitaire autonome pour interagir avec leur stockage. Etant donné que l'API Cloud Object Storage prend en charge l'ensemble le plus commun d'opérations API S3, de nombreux outils compatibles S3 peuvent également se connecter à {{site.data.keyword.cos_short}} à l'aide des [données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac). 

Certains exemples incluent notamment des explorateurs de fichiers, tels que [Cyberduck](https://cyberduck.io/) ou [Transmit](https://panic.com/transmit/), des utilitaires de sauvegarde, tels que [Cloudberry](https://www.cloudberrylab.com/) et [Duplicati](https://www.duplicati.com/), des utilitaires de ligne de commande, tels que [s3cmd](https://github.com/s3tools/s3cmd) ou [Minio Client](https://github.com/minio/mc), etc. 

## Utilisation de l'API
{: #upload-api}

La plupart des applications de programmation d'Object Storage utilisent un SDK (par exemple, [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) ou  [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)) ou l'[API Cloud Object Storage](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api). Les objets sont généralement envoyés par téléchargement en [plusieurs parties](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects), les tailles de partie et le nombre de parties étant configurés par une classe TransferManager. 
