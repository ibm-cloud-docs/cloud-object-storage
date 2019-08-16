---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: big data, multipart, multiple parts, transfer

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
{:S3cmd: .ph data-hd-programlang='S3cmd'}

# Stockage d'objets volumineux
{: #large-objects}

{{site.data.keyword.cos_full}} peut prendre en charge des objets uniques pouvant atteindre 10 To lors de l'utilisation d'envois par téléchargement en plusieurs parties. Les objets volumineux peuvent également être envoyés par téléchargement [à l'aide de la console avec l'option Transfert haut débit Aspera activée](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera). Dans la plupart des cas, l'utilisation de l'option Transfert haut débit Aspera entraîne une augmentation significative des performances de transfert de données, en particulier sur de grandes distances ou lorsque le réseau est instable. 

## Envoi par téléchargement d'objets en plusieurs parties
{: #large-objects-multipart}

Il est recommandé d'utiliser des opérations d'envoi par téléchargement en plusieurs parties pour écrire des objets volumineux dans {{site.data.keyword.cos_short}}. L'envoi par téléchargement d'un objet est effectué sous la forme d'un ensemble de parties et ces parties peuvent être envoyées par téléchargement indépendamment dans n'importe quel ordre et en parallèle. Une fois l'exécution de l'envoi par téléchargement terminée, {{site.data.keyword.cos_short}} présente toutes les parties en tant qu'objet unique. Cela procure de nombreux avantages. Les interruptions réseau ne provoquent pas l'échec des envois par téléchargement volumineux, les envois par téléchargement peuvent être mis en pause et redémarrés plus tard, et les objets peuvent être envoyés par téléchargement à mesure qu'ils sont créés.

Les envois par téléchargement en plusieurs parties ne sont disponibles que pour les objets de plus de 5 Mo. Pour les objets de moins de 50 Go, il est recommandé d'utiliser une taille de partie comprise entre 20 Mo et 100 Mo afin d'optimiser les performances. Pour les objets plus volumineux, la taille des parties peut être augmentée sans que cela ait un impact significatif sur les performances. Les envois par téléchargement en plusieurs parties sont limités à 10 000 parties de 5 Go chacune pour une taille d'objet maximale de 10 To. 


En raison de la complexité de la gestion et de l'optimisation des envois par téléchargement parallélisés, de nombreux développeurs utilisent des bibliothèques qui fournissent la prise en charge de l'envoi par téléchargement en plusieurs parties.

La plupart des outils, tels que les interfaces CLI ou la console IBM Cloud, ainsi que la plupart des bibliothèques et des SDK les plus compatibles, transfèrent automatiquement des objets dans des envois par téléchargement en plusieurs parties. 

## Utilisation de l'API REST ou de SDK
{: #large-objects-multipart-api} 

Les envois par téléchargement en plusieurs parties qui sont incomplets subsistent jusqu'à ce qu'ils soient abandonnés ou que l'objet soit supprimé. Si un envoi par téléchargement en plusieurs parties qui est incomplet n'est pas abandonné, l'envoi par téléchargement partiel continue d'utiliser les ressources. La conception des interfaces doit tenir compte de cela et prévoir le nettoyage des envois par téléchargement en plusieurs parties qui sont incomplets. {:tip}

L'envoi par téléchargement d'un objet en plusieurs parties s'effectue en trois phases :

1. L'envoi par téléchargement est initié et une valeur `UploadId` est créée. 
2. Des parties individuelles sont envoyées par téléchargement en spécifiant leurs numéros séquentiels et la valeur `UploadId` pour l'objet. 
3. Une fois toutes les parties envoyées par téléchargement, l'envoi par téléchargement est achevé en envoyant une demande avec la valeur `UploadId` et un bloc XML qui recense chaque numéro de partie et sa valeur `Etag` respective. 

Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).{:tip}

### Lancement d'un envoi par téléchargement en plusieurs parties
{: #large-objects-multipart-api-initiate} 
{: http}

Une demande `POST` émise sur un objet avec le paramètre de requête `upload` crée une nouvelle valeur `UploadId`, laquelle est alors référencée par chaque partie de l'objet en cours d'envoi par téléchargement.
{: http}

**Syntaxe**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```
{: codeblock}
{: http}

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```
{: codeblock}
{: http}

----

### Envoi par téléchargement d'une partie
{: #large-objects-multipart-api-upload-part} 
{: http}

Une demande `PUT` qui est émise sur un objet avec les paramètres de requête `partNumber` et `uploadId` télécharge une partie d'un objet. Les parties peuvent être envoyées par téléchargement en série ou en parallèle, mais doivent être numérotées dans l'ordre.
{: http}

**Syntaxe**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```
{: codeblock}
{: http}

### Achèvement d'un envoi par téléchargement en plusieurs parties
{: #large-objects-multipart-api-complete} 
{: http}

Une demande `POST` qui est émise sur un objet avec le paramètre de requête `uploadId` et le bloc XML approprié dans le corps permet d'achever un envoi par téléchargement en plusieurs parties.
{: http}

**Syntaxe**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```
{: codeblock}
{: http}


### Abandon d'envois par téléchargement en plusieurs parties qui sont incomplets
{: #large-objects-multipart-api-abort} 
{: http}

Une demande `DELETE` émise sur un objet avec le paramètre de requête `uploadId` permet de supprimer toutes les parties non terminées d'un envoi par téléchargement en plusieurs parties.
{: http}
**Syntaxe**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}
{: http}

### Utilisation de S3cmd (interface CLI)
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){:new_window} est un client et un outil de ligne de commande Linux et Mac gratuit qui permet d'envoyer par téléchargement, d'extraire et de gérer des données dans des fournisseurs de service de stockage en cloud qui utilisent le protocole S3. Conçu pour les utilisateurs chevronnés qui connaissent bien les programmes de ligne de commande, il est idéal pour les scripts de traitement par lots et la sauvegarde automatisée. S3cmd est écrit en Python. Il s'agit d'un projet open source disponible sous GNU Public License v2 (GPLv2) et gratuit pour une utilisation à la fois commerciale et privée.
{: S3cmd}

S3cmd requiert au minimum Python 2.6 et est compatible avec Python 3. L'installation la plus simple de S3cmd s'effectue à partir de Python Package Index (PyPi).
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

Une fois le package installé, récupérez l'exemple de fichier de configuration d'{{site.data.keyword.cos_full}} en cliquant [ici](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window} et mettez-le à jour avec vos données d'identification Cloud Object Storage (S3) :
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

Les quatre lignes qui doivent être mises à jour sont les suivantes :
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
Il en va de même que vous utilisiez l'exemple de fichier ou le fichier généré en exécutant la commande `s3cmd -- configure`.
{: S3cmd}

Une fois ces lignes mises à jour avec les détails COS à partir du portail client, vous pouvez tester la connexion en exécutant la commande `s3cmd ls`, qui permettra de répertorier tous les compartiments contenus dans le compte.
{: S3cmd}

```
$ s3cmd ls 
2017-02-03 14:52  s3://backuptest
2017-02-06 15:04  s3://coldbackups
2017-02-03 21:23  s3://largebackup
2017-02-07 17:44  s3://winbackup
```
{: codeblock}
{: S3cmd}

La liste complète des options et des commandes, ainsi que les informations d'utilisation de base, sont disponibles sur le site [s3tools](https://s3tools.org/usage){:new_window}.
{: S3cmd}

### Envois par téléchargement en plusieurs parties avec S3cmd
{: #large-objects-s3cmd-upload} 
{: S3cmd}

Une commande `put` exécute automatiquement un envoi par téléchargement en plusieurs parties lors de la tentative d'envoi par téléchargement d'un fichier dont la taille est supérieure au seuil spécifié.
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

Le seuil est déterminé par l'option `--multipart-chunk-size-mb` :
{: S3cmd}

```
--multipart-chunk-size-mb=SIZE
    Size of each chunk of a multipart upload. Files bigger
    than SIZE are automatically uploaded as multithreaded-
    multipart, smaller files are uploaded using the
    traditional method. SIZE is in megabytes, default
    chunk size is 15MB, minimum allowed chunk size is 5MB,
    maximum is 5GB.
```
{: codeblock}
{: S3cmd}

Exemple :
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

Sortie :
{: S3cmd}

```
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 1 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  1731.92 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 2 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2001.14 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 3 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2000.28 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 4 of 4, 4MB] [1 of 1]
 4973645 of 4973645   100% in    2s  1823.51 kB/s  done
 ```
{: codeblock}
{: S3cmd}

### Utilisation du SDK Java
{: #large-objects-java} 
{: java}

Le SDK Java fournit deux méthodes pour exécuter des envois par téléchargement d'objet volumineux :
{: java}

* [Envois par téléchargement en plusieurs parties](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Utilisation du SDK Python
{: #large-objects-python} 
{: python}

Le SDK Python fournit deux méthodes pour exécuter des envois par téléchargement d'objet volumineux :
{: python}

* [Envois par téléchargement en plusieurs parties](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Utilisation du SDK Node.js
{: #large-objects-node} 
{: javascript}

Le SDK Node.js fournit une méthode pour exécuter des envois par téléchargement d'objet volumineux :
{: javascript}

* [Envois par téléchargement en plusieurs parties](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
