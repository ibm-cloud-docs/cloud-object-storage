---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, buckets

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

# Opérations sur les compartiments
{: #compatibility-api-bucket-operations}


## Création d'une liste de compartiments
{: #compatibility-api-list-buckets}

Une demande `GET` envoyée à la racine du noeud final renvoie une liste de compartiments qui sont associés à l'instance de service spécifiée. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

En-tête | Type   | Obligatoire ? |  Description
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | Chaîne |Oui| Permet de répertorier les compartiments qui ont été créés dans cette instance de service. 

Paramètre de requête | Valeur  | Obligatoire ? |  Description
--------------------------|--------|---| -----------------------------------------------------------
`extended` |Néant |Non| Fournit des métadonnées `LocationConstraint` dans la liste. 

La liste étendue n'est pas prise en charge dans les SDK ou l'interface CLI.
{:note}

**Syntaxe**

```bash
GET https://{endpoint}/
```

**Exemple de demande**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Exemple de réponse**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

### Obtention d'une liste étendue
{: #compatibility-api-list-buckets-extended}

**Syntaxe**

```bash
GET https://{endpoint}/?extended
```

**Exemple de demande**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Exemple de réponse**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <IsTruncated>false</IsTruncated>
    <MaxKeys>1000</MaxKeys>
    <Prefix/>
    <Marker/>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
            <LocationConstraint>us-south-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
            <LocationConstraint>seo01-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
            <LocationConstraint>eu-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
            <LocationConstraint>us-cold</LocationConstraint>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

## Création d'un compartiment
{: #compatibility-api-new-bucket}

Une demande `PUT` envoyée au noeud final suivie d'une chaîne crée un compartiment. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).Les noms de compartiment doivent être globalement unique et conformes à DNS ; les noms comprenant 3 à 63 caractères doivent être composés de lettres minuscules, de chiffres et de tirets. Les noms de compartiment doivent commencer et se terminer par une lettre ou un chiffre minuscule. Les noms de compartiment ressemblant à des adresses IP ne sont pas autorisés. Cette opération n'utilise pas de paramètres de requête propres aux opérations.

Les noms de compartiment doivent être uniques car tous les compartiments du cloud public partagent un espace de nom global. Cela permet d'accéder à un compartiment sans avoir besoin de fournir une instance de service ou des informations de compte. En outre, il n'est pas possible de créer un compartiment dont le nom commence par `cosv1-` ou `account-` car ces préfixes sont réservés par le système.
{:important}

En-tête | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | Chaîne |  Cet en-tête fait référence à l'instance de service dans laquelle le compartiment sera créé et pour laquelle l'utilisation des données sera facturée.

**Remarque** : Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou autre chose dans le nom du compartiment ou de l'objet. {:tip}

**Syntaxe**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Exemple de demande**

Cet exemple illustre la création d'un nouveau compartiment nommé 'images' :

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

----

## Création d'un compartiment avec une classe de stockage différente
{: #compatibility-api-storage-class}

Pour créer un compartiment avec une classe de stockage différente, envoyez un bloc XML spécifiant une configuration de compartiment avec un paramètre `LocationConstraint` ayant pour valeur `{provisioning code}` dans le corps d'une demande `PUT` sur un noeud final de compartiment. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Notez que les [règles de nommage](#compatibility-api-new-bucket) de compartiment standard s'appliquent. Cette opération n'utilise pas de paramètres de requête propres aux opérations.

En-tête | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | Chaîne |  Cet en-tête fait référence à l'instance de service dans laquelle le compartiment sera créé et pour laquelle l'utilisation des données sera facturée.

**Syntaxe**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

**Exemple de demande**

Cet exemple illustre la création d'un nouveau compartiment nommé 'vault-images' :

```http
PUT /vault-images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
Content-Length: 110
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Fri, 17 Mar 2017 17:52:17 GMT
X-Clv-Request-Id: b6483b2c-24ae-488a-884c-db1a93b9a9a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
Content-Length: 0
```

----

## Création d'un nouveau compartiment avec des clés de chiffrement gérées par Key Protect (SSE-KP)
{: #compatibility-api-key-protect}

La création d'un compartiment où les clés de chiffrement sont gérées par Key Protect nécessite d'avoir accès à une instance de service Key Protect active située dans le même emplacement que le nouveau compartiment. Cette opération n'utilise pas de paramètres de requête propres aux opérations.

Pour plus d'informations sur l'utilisation de Key Protect pour gérer vos clés de chiffrement, [voir la documentation](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

Notez que Key Protect n'est **pas** disponible dans une configuration inter-régionale et que les compartiments SSE-KP doivent être régionaux.
{:tip}

En-tête | Type   | Description
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | Chaîne |  Cet en-tête fait référence à l'instance de service dans laquelle le compartiment sera créé et pour laquelle l'utilisation des données sera facturée.
`ibm-sse-kp-encryption-algorithm` | Chaîne | Cet en-tête est utilisé pour spécifier l'algorithme et la taille de clé à utiliser avec la clé de chiffrement stockée à l'aide de Key Protect. La chaîne `AES256` doit être affectée à cette valeur.
`ibm-sse-kp-customer-root-key-crn`  | Chaîne | Cet en-tête est utilisé pour faire référence à la clé racine spécifique utilisée par Key Protect pour chiffrer ce compartiment. Cette valeur doit correspondre au CRN complet de la clé racine. 

**Syntaxe**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Exemple de demande**

Cet exemple illustre la création d'un nouveau compartiment nommé 'secure-files' :

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

---

## Extraction des en-têtes d'un compartiment
{: #compatibility-api-head-bucket}

Une demande `HEAD` émise sur un compartiment renvoie les en-têtes de ce compartiment. 

Les demandes `HEAD` ne renvoient pas de corps, par conséquent, elles ne peuvent pas renvoyer de messages d'erreur spécifiques, tels que `NoSuchBucket`, mais uniquement `NotFound`. {:tip}

**Syntaxe**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**Exemple de demande**

Cet exemple illustre l'extraction des en-têtes du compartiment 'images' :

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
```

**Exemple de demande**

Les demandes `HEAD` émises sur des compartiments avec le chiffrement Key Protect renvoient des en-têtes supplémentaires.

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
ibm-sse-kp-enabled: True
ibm-see-kp-crk-id: {customer-root-key-id}
```

----

## Création de la liste des objets d'un compartiment spécifique (version 2)
{: #compatibility-api-list-objects-v2}

Une demande `GET` émise sur un compartiment renvoie une liste d'objets, limitée à 1 000 entrées et non triée dans un ordre lexicographique. La valeur `StorageClass` renvoyée dans la réponse est une valeur par défaut car les opérations de classe de stockage ne sont pas implémentées dans COS. Cette opération n'utilise pas d'en-têtes ou d'éléments de contenu propres aux opérations.

**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### Paramètres de requête facultatifs
{: #compatibility-api-list-objects-v2-params}
Nom  | Type | Description
--- | ---- | ------------
`list-type` | Chaîne | Indique la version 2 de l'API et la valeur doit être 2.
`prefix` | Chaîne | Limite la réponse aux noms d'objet débutant par `prefix`.
`delimiter` | Chaîne | Regroupe les objets entre `prefix` et `delimiter`.
`encoding-type` | Chaîne | Si des caractères Unicode qui ne sont pas pris en charge par XML sont utilisés dans un nom d'objet, ce paramètre peut avoir pour valeur `url` afin de coder correctement la réponse.
`max-keys` | Chaîne | Limite le nombre d'objets à afficher dans la réponse. La valeur par défaut et maximale est 1 000.
`fetch-owner` | Chaîne | La version 2 de l'API n'inclut pas les informations `Owner` par défaut. Affectez à ce paramètre la valeur `true` si les informations `Owner` sont souhaitées dans la réponse. 
`continuation-token` | Chaîne | Indique l'ensemble suivant d'objets à renvoyer lorsque votre réponse est tronquée (l'élément`IsTruncated` renvoie `true`). <br/><br/>Votre réponse initiale inclura l'élément `NextContinuationToken`. Utilisez ce jeton dans la demande suivante comme valeur pour `continuation-token`.
`start-after` | Chaîne | Renvoie les noms de clé après un objet de clé spécifique. <br/><br/>*Ce paramètre n'est valide que dans votre demande initiale.* Si un paramètre `continuation-token` est inclus dans votre demande, ce paramètre est ignoré. 

**Exemple de demande (simple avec IAM)**

Cette demande répertorie les objets contenus dans le compartiment "apiary" :

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Exemple de réponse (simple)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 814
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <KeyCount>3</KeyCount>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Exemple de demande (paramètre max-keys)**

Cette demande répertorie les objets contenus dans le compartiment "apiary" avec un nombre maximal de clés renvoyées fixé à 1 :

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Exemple de réponse (réponse tronquée)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 598
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <NextContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Exemple de demande (paramètre continuation-token)**

Cette demande répertorie les objets contenus dans le compartiment "apiary" avec un jeton de continuation spécifié :

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Exemple de réponse (réponse tronquée, paramètre continuation-token)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 604
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <ContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</ContinuationToken>
  <NextContinuationToken>1a8j20CqowRrM4epIQ7fTBuyPZWZUeA8Epog16wYu9KhAPNoYkWQYhGURsIQbll1lP7c-OO-V5Vyzu6mogiakC4NSwlK4LyRDdHQgY-yPH4wMB76MfQR61VyxI4TJLxIWTPSZA0nmQQWcuV2mE4jiDA</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

### Création de la liste des objets d'un compartiment spécifique (obsolète)
{: #compatibility-api-list-objects}

**Remarque :*** cette API est incluse pour la compatibilité avec les versions antérieures.* Pour connaître la méthode recommandée pour l'extraction des objets contenus dans un compartiment, voir [Version 2](#compatibility-api-list-objects-v2). 

Une demande `GET` émise sur un compartiment renvoie une liste d'objets, limitée à 1 000 entrées et non triée dans un ordre lexicographique. La valeur `StorageClass` renvoyée dans la réponse est une valeur par défaut car les opérations de classe de stockage ne sont pas implémentées dans COS. Cette opération n'utilise pas d'en-têtes ou d'éléments de contenu propres aux opérations.

**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### Paramètres de requête facultatifs
{: #compatibility-api-list-objects-params}

Nom  | Type | Description
--- | ---- | ------------
`prefix` | Chaîne | Limite la réponse aux noms d'objet débutant par `prefix`.
`delimiter` | Chaîne | Regroupe les objets entre `prefix` et `delimiter`.
`encoding-type` | Chaîne | Si des caractères Unicode qui ne sont pas pris en charge par XML sont utilisés dans un nom d'objet, ce paramètre peut avoir pour valeur `url` afin de coder correctement la réponse.
`max-keys` | Chaîne | Limite le nombre d'objets à afficher dans la réponse. La valeur par défaut et maximale est 1 000.
`marker` | Chaîne | Indique l'objet à partir duquel la création de liste doit commencer, dans l'ordre binaire UTF-8.

**Exemple de demande**

Cette demande répertorie les objets contenus dans le compartiment "apiary" :

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 909
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <Marker/>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

## Suppression d'un compartiment

Une demande `DELETE` émise sur un compartiment vide supprime celui-ci. Une fois un compartiment supprimé, son nom est gardé en réserve par le système pendant 10 minutes, après quoi il est libéré pour être réutilisé. *Seuls les compartiments vides peuvent être supprimés.*

**Syntaxe**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### En-têtes facultatifs

Nom  | Type | Description
--- | ---- | ------------
`aspera-ak-max-tries` | Chaîne | Indique le nombre de tentatives de suppression. La valeur par défaut est 2.


**Exemple de demande**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

Le serveur émet la réponse `204 Aucun contenu`. 

Si la demande de suppression porte sur un compartiment non vide, le serveur émet la réponse `409 Conflit`. 

**Exemple de réponse**

```xml
<Error>
  <Code>BucketNotEmpty</Code>
  <Message>The bucket you tried to delete is not empty.</Message>
  <Resource>/apiary/</Resource>
  <RequestId>9d2bbc00-2827-4210-b40a-8107863f4386</RequestId>
  <httpStatusCode>409</httpStatusCode>
</Error>
```

----

## Création d'une liste d'envois par téléchargement en plusieurs parties annulés/incomplets pour un compartiment

Une demande `GET` émise sur un compartiment avec les paramètres appropriés permet d'extraire des informations sur les envois par téléchargement en plusieurs parties qui sont annulés ou incomplets pour un compartiment.

**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**Paramètres**

Nom  | Type | Description
--- | ---- | ------------
`prefix` | Chaîne | Limite la réponse aux noms d'objet débutant par `{prefix}` .
`delimiter` | Chaîne | Regroupe les objets entre `prefix` et `delimiter`.
`encoding-type` | Chaîne | Si des caractères Unicode qui ne sont pas pris en charge par XML sont utilisés dans un nom d'objet, ce paramètre peut avoir pour valeur `url` afin de coder correctement la réponse.
`max-uploads` | Entier  | Limite le nombre d'objets à afficher dans la réponse. La valeur par défaut et maximale est 1 000.
`key-marker` | Chaîne | Indique l'emplacement à partir duquel la création de liste doit commencer. 
`upload-id-marker` | Chaîne | Ignoré si le paramètre `key-marker` n'est pas spécifié, sinon, il définit un point à partir duquel commencer la création d'une liste de parties au-dessus de `upload-id-marker`.

**Exemple de demande**

Cet exemple illustre l'extraction de tous les envois par téléchargement en plusieurs parties en cours qui sont annulés et incomplets :

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse** (aucun envoi par téléchargement en plusieurs parties n'est en cours)

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:22:27 GMT
X-Clv-Request-Id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Content-Type: application/xml
Content-Length: 374
```

```xml
<ListMultipartUploadsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>apiary</Bucket>
  <KeyMarker/>
  <UploadIdMarker/>
  <NextKeyMarker>multipart-object-123</NextKeyMarker>
  <NextUploadIdMarker>0000015a-df89-51d0-2790-dee1ac994053</NextUploadIdMarker>
  <MaxUploads>1000</MaxUploads>
  <IsTruncated>false</IsTruncated>
  <Upload>
    <Key>file</Key>
    <UploadId>0000015a-d92a-bc4a-c312-8c1c2a0e89db</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-16T22:09:01.002Z</Initiated>
  </Upload>
  <Upload>
    <Key>multipart-object-123</Key>
    <UploadId>0000015a-df89-51d0-2790-dee1ac994053</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-18T03:50:02.960Z</Initiated>
  </Upload>
</ListMultipartUploadsResult>
```

----

## Création d'une liste d'éléments de configuration de partage de ressources d'origine croisée pour un compartiment

Une demande `GET` émise sur un compartiment avec les paramètres appropriés permet d'extraire des informations sur la configuration de partage de ressources d'origine croisée pour un compartiment.

**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Exemple de demande**

Cet exemple illustre la création d'une liste d'éléments de configuration de partage de ressources d'origine croisée (CORS) sur le compartiment "apiary" :

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse** 

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:20:30 GMT
X-Clv-Request-Id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Content-Type: application/xml
Content-Length: 123
```

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <CORSRule>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
  </CORSRule>
</CORSConfiguration>
```

----

## Création d'une configuration de partage de ressources d'origine croisée pour un compartiment

Une demande `PUT` émise sur un compartiment avec les paramètres appropriés permet de créer ou de remplacer une configuration de partage de ressources d'origine croisée pour un compartiment.

L'en-tête `Content-MD5` obligatoire doit être la représentation binaire d'un hachage MD5 codé en base 64. 

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Syntaxe**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Exemple de demande**

Cet exemple illustre l'ajout d'une configuration CORS autorisant les demandes émises à partir de `www.ibm.com` à émettre des demandes `GET`, `PUT` et `POST` sur le compartiment :

```http
PUT /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 237
```

```xml
<CORSConfiguration>
  <CORSRule>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
  </CORSRule>
</CORSConfiguration>
```


**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```

----

## Suppression d'une configuration de partage de ressources d'origine croisée pour un compartiment

Une demande `DELETE` émise sur un compartiment avec les paramètres appropriés permet de supprimer une configuration de partage de ressources d'origine croisée pour un compartiment.

**Syntaxe**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Exemple de demande**

Cet exemple illustre la suppression d'une configuration de partage de ressources d'origine croisée (CORS) pour un compartiment : 

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

Le serveur émet la réponse `204 Aucun contenu`. 

----

## Création d'une liste d'éléments de contrainte d'emplacement pour un compartiment

Une demande `GET` émise sur un compartiment avec le paramètre approprié permet d'extraire des informations sur l'emplacement pour un compartiment. 

**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**Exemple de demande**

Cet exemple illustre l'extraction de l'emplacement du compartiment "apiary" :

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Exemple de réponse**

```http
HTTP/1.1 200 OK
Date: Tue, 12 Jun 2018 21:10:57 GMT
X-Clv-Request-Id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Content-Type: application/xml
Content-Length: 161
```

```xml
<LocationConstraint xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  us-south-standard
</LocationConstraint>
```

----

## Création d'une configuration de cycle de vie de compartiment
{: #compatibility-api-create-bucket-lifecycle}

Une opération `PUT` utilise le paramètre de requête lifecycle pour définir les paramètres de cycle de vie du compartiment. Un en-tête `Content-MD5` est requis en tant que contrôle d'intégrité du contenu. 

**Syntaxe**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Eléments de contenu**

Le corps de la demande doit contenir un bloc XML avec le schéma suivant :

|Elément|Type|Enfants|Ancêtre|Contrainte|
|---|---|---|---|---|
|LifecycleConfiguration|Conteneur|Rule|Néant |Limite 1|
|Rule|Conteneur|ID, Status, Filter, Transition|LifecycleConfiguration|Limite 1|
|ID| Chaîne |Néant |Rule|**Doit** être composé de ``(a-z,A- Z0-9)`` et des symboles suivants : `` !`_ .*'()- ``|
|Filter| Chaîne |Prefix|Rule|**Doit** contenir un élément `Prefix`. |
|Prefix| Chaîne |Néant |Filter|**Doit ** avoir pour valeur `<Prefix/>`.|
|Transition|Conteneur|Days, StorageClass|Rule|Limite 1.|
|Days|Entier non négatif|Néant |Transition|**Doit** être une valeur supérieure à 0.|
|Date|Date|Néant |Transition|**Doit** être au format ISO 8601 et la date doit être située dans le futur. |
|StorageClass| Chaîne |Néant |Transition|**Doit** avoir pour valeur GLACIER.|

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>{string}</ID>
        <Status>Enabled</Status>
        <Filter>
            <Prefix/>
        </Filter>
        <Transition>
            <Days>{integer}</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

**Exemple de demande**

```http
PUT /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ== 
Content-Length: 305
```

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

Le serveur émet la réponse `200 OK`. 

----

## Extraction d'une configuration de cycle de vie de compartiment

Une opération `GET` utilise le paramètre de requête lifecycle pour extraire les paramètres de cycle de vie du compartiment. 

**Syntaxe**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Exemple de demande**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**Exemple de réponse**

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

----

## Suppression de la configuration de cycle de vie d'un compartiment

Une demande `DELETE` émise sur un compartiment avec les paramètres appropriés permet de retirer des configurations de cycle de vie pour un compartiment. 

**Syntaxe**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Exemple de demande**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

Le serveur émet la réponse `204 Aucun contenu`. 
