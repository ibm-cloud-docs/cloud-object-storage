---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, objects

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

# Opérations sur les objets
{: #object-operations}

Il s'agit des opérations de lecture, d'écriture et de configuration des objets contenues dans un compartiment. 

Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).{:tip}

## Envoi par téléchargement d'un objet
{: #object-operations-put}

Une demande `PUT` avec un chemin d'accès à un objet permet d'envoyer par téléchargement le corps de demande en tant qu'objet. Tous les objets envoyés par téléchargement dans une unité d'exécution doivent être inférieurs à 500 Mo (les objets [envoyés par téléchargement en plusieurs parties](/docs/services/cloud-object-storage?topic=cloud-object-storage-large-objects) peuvent faire 10 To).

**Remarque** : Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose. {:tip}

**Syntaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemple de demande**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Exemple de demande (HMAC)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: {payload_hash}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Exemple de demande (URL présignée HMAC)**

```http
PUT /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

----

## Obtention des en-têtes d'un objet
{: #object-operations-head}

Une demande `HEAD` avec un chemin d'accès à un objet permet d'extraire les en-têtes de cet objet. 

Notez que la valeur `Etag` renvoyée pour les objets chiffrés à l'aide de SSE-KP ne **sera** pas le hachage MD5 de l'objet non chiffré d'origine.
{:tip}

**Syntaxe**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemple de demande**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Exemple de demande (en-têtes HMAC)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:32:44 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: da214d69-1999-4461-a130-81ba33c484a6
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:49:06 GMT
Content-Length: 11
```

----

## Réception par téléchargement d'un objet
{: #object-operations-get}

Une demande `GET` avec un chemin d'accès à un objet permet de recevoir par téléchargement cet objet. 

Notez que la valeur `Etag` renvoyée pour les objets chiffrés à l'aide de SSE-C/SSE-KP ne **sera** pas le hachage MD5 de l'objet non chiffré d'origine.
{:tip}

**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### En-têtes facultatifs
{: #object-operations-get-headers}

En-tête | Type | Description
--- | ---- | ------------
`range` |Chaîne| Renvoie les octets d'un objet dans la plage spécifiée.

**Exemple de demande**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (en-têtes HMAC)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:34:25 GMT
X-Clv-Request-Id: 116dcd6b-215d-4a81-bd30-30291fa38f93
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 116dcd6b-215d-4a81-bd30-30291fa38f93
ETag: "d34d8aada2996fc42e6948b926513907"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:46:53 GMT
Content-Length: 467

 Female bees that are not fortunate enough to be selected to be the 'queen'
 while they were still larvae become known as 'worker' bees. These bees lack
 the ability to reproduce and instead ensure that the hive functions smoothly,
 acting almost as a single organism in fulfilling their purpose.
```

----

## Suppression d'un objet
{: #object-operations-delete}

Une demande `DELETE` avec un chemin d'accès à un objet permet de supprimer cet objet. 

**Syntaxe**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemple de demande**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Exemple de demande (en-têtes HMAC)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 204 No Content
Date: Thu, 25 Aug 2016 17:44:57 GMT
X-Clv-Request-Id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
```

----

## Suppression de plusieurs objets
{: #object-operations-multidelete}

Une demande `POST` avec un chemin d'accès à un compartiment et les paramètres appropriés permet de supprimer un ensemble spécifié d'objets Un en-tête `Content-MD5` spécifiant le hachage MD5 codé en base 64 du corps de la demande est requis.

L'en-tête `Content-MD5` obligatoire doit être la représentation binaire d'un hachage MD5 codé en base 64. 

**Remarque :** si un objet spécifié dans la demande est introuvable, l'objet est considéré comme supprimé.  

### Eléments facultatifs
{: #object-operations-multidelete-options}

|En-tête |Type|Description|
|---|---|---|
|`Quiet`|Booléen|Active le mode silencieux pour la demande.|

La demande peut contenir un maximum de 1000 clés à supprimer. Bien que cela soit très utile pour réduire le temps système pour chaque demande, n'oubliez pas que la durée d'exécution d'une suppression d'un grand nombre de clés peut prendre un certain temps. Tenez compte également de la taille des objets de manière à obtenir de bonnes performances. {:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Syntaxe**

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```

**Exemple de demande**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**Exemple de demande (en-têtes HMAC)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
POST /apiary?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&delete=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Delete>
    <Object>
         <Key>surplus-bee</Key>
    </Object>
    <Object>
         <Key>unnecessary-bee</Key>
    </Object>
</Delete>
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 18:54:53 GMT
X-Clv-Request-Id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Content-Type: application/xml
Content-Length: 207
```
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<DeleteResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Deleted>
         <Key>surplus-bee</Key>
    </Deleted>
    <Deleted>
         <Key>unnecessary-bee</Key>
    </Deleted>
</DeleteResult>
```

----

## Copie d'un objet
{: #object-operations-copy}

Une demande `PUT` avec un chemin d'accès à un nouvel objet permet de créer une nouvelle copie d'un autre objet spécifiée par l'en-tête `x-amz-copy-source`. En l'absence d'autres modifications, les métadonnées restent inchangées. 

**Remarque** : Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose. {:tip}


**Remarque** : la copie d'un élément depuis un compartiment protégé par *Key Protect* vers un compartiment de destination situé dans une autre région est restreinte car cela entraîne l'erreur `500 - Erreur interne`.
{:tip}

**Syntaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### En-têtes facultatifs
{: #object-operations-copy-options}

En-tête | Type | Description
--- | ---- | ------------
`x-amz-metadata-directive` | Chaîne (`COPY` ou `REPLACE`) | `REPLACE` remplace les métadonnées d'origine par de nouvelles métadonnées fournies.
`x-amz-copy-source-if-match` | Chaîne (`ETag`)| Crée une copie si la valeur `ETag` spécifiée correspond à l'objet source. 
`x-amz-copy-source-if-none-match` | Chaîne (`ETag`)| Crée une copie si la valeur `ETag` spécifiée est différente de l'objet source. 
`x-amz-copy-source-if-unmodified-since` | Chaîne (horodatage)| Crée une copie si l'objet source n'a pas été modifié depuis la date spécifiée. La date doit être une date HTTP valide (par exemple, `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-copy-source-if-modified-since` | Chaîne (horodatage)| Crée une copie si l'objet source a été modifié depuis la date spécifiée. La date doit être une date HTTP valide (par exemple, `Wed, 30 Nov 2016 20:21:38 GMT`).

**Exemple de demande**

L'exemple de base suivant prend l'objet `bee` du compartiment `garden` et en crée une copie dans le compartiment `apiary` avec la nouvelle clé `wild-bee` :

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (en-têtes HMAC)**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 19:52:52 GMT
X-Clv-Request-Id: 72992a90-8f86-433f-b1a4-7b1b33714bed
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 72992a90-8f86-433f-b1a4-7b1b33714bed
ETag: "853aab195ce770b0dfb294a4e9467e62"
Content-Type: application/xml
Content-Length: 240
```

```xml
<CopyObjectResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <LastModified>2016-11-30T19:52:53.125Z</LastModified>
  <ETag>"853aab195ce770b0dfb294a4e9467e62"</ETag>
</CopyObjectResult>
```

----

## Vérification de la configuration de partage de ressources d'origine croisée d'un objet
{: #object-operations-options}

Une demande `OPTIONS` avec un chemin d'accès à un objet et une origine et un type de demande permet de vérifier si cet objet est accessible depuis cette origine à l'aide de ce type de demande. Contrairement aux autres demandes, une demande OPTIONS ne requiert pas les en-têtes `authorization` ou `x-amx-date`. 

**Syntaxe**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemple de demande**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (en-têtes HMAC)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 07 Dec 2016 16:23:14 GMT
X-Clv-Request-Id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: PUT
Access-Control-Allow-Credentials: true
Vary: Origin, Access-Control-Request-Headers, Access-Control-Allow-Methods
Content-Length: 0

```

----

## Envoi par téléchargement d'objets en plusieurs parties
{: #object-operations-multipart}

Lorsque vous gérez des objets volumineux, il est recommandé d'utiliser des opérations d'envoi par téléchargement en plusieurs parties pour écrire ces objets dans {{site.data.keyword.cos_full}}. L'envoi par téléchargement d'un objet peut être effectué sous la forme d'un ensemble de parties et ces parties peuvent être envoyées par téléchargement indépendamment dans n'importe quel ordre et en parallèle. Une fois l'exécution de l'envoi par téléchargement terminée, {{site.data.keyword.cos_short}} présente toutes les parties en tant qu'objet unique. Cela procure de nombreux avantages. Les interruptions réseau ne provoquent pas l'échec des envois par téléchargement volumineux, les envois par téléchargement peuvent être mis en pause et redémarrés plus tard, et les objets peuvent être envoyés par téléchargement à mesure qu'ils sont créés.

Les envois par téléchargement en plusieurs parties ne sont disponibles que pour les objets de plus de 5 Mo. Pour les objets de moins de 50 Go, il est recommandé d'utiliser une taille de partie comprise entre 20 Mo et 100 Mo afin d'optimiser les performances. Pour les objets plus volumineux, la taille des parties peut être augmentée sans que cela ait un impact significatif sur les performances. Les envois par téléchargement en plusieurs parties sont limités à 10 000 parties de 5 Go chacune.

L'utilisation de plus de 500 parties entraîne des pertes d'efficacité dans {{site.data.keyword.cos_short}} et cela doit être évité dans la mesure du possible.{:tip}

En raison de la complexité supplémentaire, il est conseillé aux développeurs d'utiliser une bibliothèque qui fournit la prise en charge de l'envoi par téléchargement en plusieurs parties.

Les envois par téléchargement en plusieurs parties qui sont incomplets subsistent jusqu'à ce qu'ils soient abandonnés à l'aide de `AbortIncompleteMultipartUpload` ou que l'objet soit supprimé. Si un envoi par téléchargement en plusieurs parties qui est incomplet n'est pas abandonné, l'envoi par téléchargement partiel continue d'utiliser les ressources. La conception des interfaces doit tenir compte de cela et prévoir le nettoyage des envois par téléchargement en plusieurs parties qui sont incomplets. {:tip}

L'envoi par téléchargement d'un objet en plusieurs parties s'effectue en trois phases :

1. L'envoi par téléchargement est initié et une valeur `UploadId` est créée. 
2. Des parties individuelles sont envoyées par téléchargement en spécifiant leurs numéros séquentiels et la valeur `UploadId` pour l'objet. 
3. Une fois toutes les parties envoyées par téléchargement, l'envoi par téléchargement est achevé en envoyant une demande avec la valeur `UploadId` et un bloc XML qui recense chaque numéro de partie et sa valeur `Etag` respective. 

## Lancement d'un envoi par téléchargement en plusieurs parties
{: #object-operations-multipart-initiate}

Une demande `POST` émise sur un objet avec le paramètre de requête `upload` crée une nouvelle valeur `UploadId`, laquelle est alors référencée par chaque partie de l'objet en cours d'envoi par téléchargement. 

**Remarque** : Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose. {:tip}

**Syntaxe**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**Exemple de demande**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (en-têtes HMAC)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

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

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```

----

## Envoi par téléchargement d'une partie
{: #object-operations-multipart-put-part}

Une demande `PUT` émise sur un objet avec les paramètres de requête `partNumber` et `uploadId` télécharge une partie d'un objet. Les parties peuvent être envoyées par téléchargement en série ou en parallèle, mais doivent être numérotées dans l'ordre. 

**Remarque** : Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose. {:tip}

**Syntaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**Exemple de demande**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Exemple de demande (en-têtes HMAC)**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Exemple de demande (URL présignée HMAC)**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Exemple de réponse**

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

----

## Création d'une liste de parties
{: #object-operations-multipart-list}

Une demande `GET` avec un chemin d'accès à un objet à plusieurs parties et une valeur `UploadID` active spécifiée en tant que paramètre de requête renvoie une liste contenant toutes les parties de l'objet. 


**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```

### Paramètres de requête
{: #object-operations-multipart-list-params}
Paramètre| Obligatoire ? | Type | Description
--- | ---- | ------------
`uploadId` | Obligatoire |Chaîne| ID d'envoi par téléchargement renvoyé lors du lancement d'un envoi par téléchargement en plusieurs parties. 
`max-parts` |Facultatif  |Chaîne| La valeur par défaut est 1 000.
`part-number​-marker` |Facultatif  |Chaîne| Indique l'emplacement à partir duquel la création de la liste de parties doit commencer. 

**Exemple de demande**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (en-têtes HMAC)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Mon, 19 Mar 2018 17:21:08 GMT
X-Clv-Request-Id: 6544044d-4f88-4bb6-9ee5-bfadf5023249
Server: Cleversafe/3.12.4.20
X-Clv-S3-Version: 2.5
Accept-Ranges: bytes
Content-Type: application/xml
Content-Length: 743
```

```xml
<ListPartsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>farm</Bucket>
  <Key>spaceship</Key>
  <UploadId>01000162-3f46-6ab8-4b5f-f7060b310f37</UploadId>
  <Initiator>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Initiator>
  <Owner>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Owner>
  <StorageClass>STANDARD</StorageClass>
  <MaxParts>1000</MaxParts>
  <IsTruncated>false</IsTruncated>
  <Part>
    <PartNumber>1</PartNumber>
    <LastModified>2018-03-19T17:20:35.482Z</LastModified>
    <ETag>"bb03cf4fa8603fe407a65ee1dba55265"</ETag>
    <Size>7128094</Size>
  </Part>
</ListPartsResult>
```

----

## Achèvement d'un envoi par téléchargement en plusieurs parties
{: #object-operations-multipart-complete}

Une demande `POST` émise sur un objet avec le paramètre de requête `uploadId` et le bloc XML approprié dans le corps permet d'achever un envoi par téléchargement en plusieurs parties. 

**Syntaxe**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Exemple de demande**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Exemple de demande (en-têtes HMAC)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Exemple de demande (URL présignée HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

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

**Exemple de réponse**

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

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

## Abandon d'envois par téléchargement en plusieurs parties qui sont incomplets
{: #object-operations-multipart-uploads}

Une demande `DELETE` émise sur un objet avec le paramètre de requête `uploadId` permet de supprimer toutes les parties non terminées d'un envoi par téléchargement en plusieurs parties. 

**Syntaxe**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**Exemple de demande**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (en-têtes HMAC)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Restauration temporaire d'un objet archivé
{: #object-operations-archive-restore}

Une demande `POST` émise sur un objet avec le paramètre de requête `restore` permet de demander la restauration temporaire d'un objet archivé. Un en-tête `Content-MD5` est requis en tant que contrôle d'intégrité du contenu. 

Un objet archivé doit être restauré avant d'être reçu par téléchargement ou modifié. La durée de vie de l'objet doit être spécifiée, après quoi la copie temporaire de l'objet est supprimée. 

Un délai maximal de 15 heures peut s'écouler avant que la copie restaurée ne soit accessible. Une demande HEAD peut être utilisée pour vérifier si la copie restaurée est disponible. 

Pour pouvoir être restauré définitivement, l'objet doit être copié dans un compartiment qui ne possède pas de configuration de cycle de vie active.

**Syntaxe**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```

**Eléments de contenu**

Le corps de la demande doit contenir un bloc XML avec le schéma suivant :

|Elément|Type|Enfants|Ancêtre|Contrainte|
|---|---|---|---|---|
|RestoreRequest|Conteneur|Days, GlacierJobParameters|Néant |Néant |
|Days|Entier|Néant |RestoreRequest|Spécifie la durée de vie de l'objet temporairement restauré. Le nombre minimal de jours pendant lequel une copie restaurée de l'objet peut exister est 1. Une fois la période de restauration écoulée, la copie temporaire de l'objet est supprimée. |
|GlacierJobParameters| Chaîne |Tier|RestoreRequest|Néant |
|Tier| Chaîne |Néant |GlacierJobParameters|**Doit** avoir pour valeur `Bulk`.|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Exemple de demande**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (en-têtes HMAC)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de demande (URL présignée HMAC)**

```http
POST /apiary/queenbee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&restore&x-amz-signature={signature} HTTP/1.1
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Exemple de réponse**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Mise à jour de métadonnées
{: #object-operations-metadata}

Il existe deux manières de mettre à jour les métadonnées sur un objet existant :
* En exécutant une demande `PUT` avec les nouvelles métadonnées et le contenu de l'objet d'origine 
* En exécutant une demande `COPY` avec les nouvelles métadonnées en spécifiant l'objet d'origine comme source de la copie 

Toutes les clés de métadonnées doivent comporter le préfixe `x-amz-meta-`.
{: tip}

### Utilisation de PUT pour mettre à jour les métadonnées
{: #object-operations-metadata-put}

La demande `PUT` requiert une copie de l'objet existant car le contenu sera écrasé. {: important}

**Syntaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemple de demande**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

### Utilisation de COPY pour mettre à jour les métadonnées
{: #object-operations-metadata-copy}

Pour plus d'informations sur l'exécution d'une demande `COPY`, cliquez [ici](#object-operations-copy). 

**Syntaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemple de demande**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```
