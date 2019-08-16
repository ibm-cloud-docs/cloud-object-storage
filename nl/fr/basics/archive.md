---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Archivage des données les moins sollicitées à l'aide de règles de transition
{: #archive}

La fonction d'archivage d'{{site.data.keyword.cos_full}} est une option [économique](https://www.ibm.com/cloud/object-storage) pour les données rarement sollicitées. Vous pouvez stocker des données en passant de n'importe quel niveau de stockage (Standard, Coffre, Coffre froid et Flex) à des archives hors ligne à long terme ou vous pouvez utiliser l'option de stockage en ligne Coffre froid.
{: shortdesc}

Vous pouvez archiver des objets à l'aide de la console Web, de l'API REST et des outils tiers qui sont intégrés à IBM Cloud Object Storage. 

Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).{:tip}

## Ajout ou gestion d'une règle d'archivage sur un compartiment
{: #archive-add}

Lorsque vous créez ou modifiez une règle d'archivage pour un compartiment, tenez compte des points suivants : 

* Une règle d'archivage peut être ajoutée à tout moment à un compartiment nouveau ou existant.  
* Une règle d'archivage existante peut être modifiée ou désactivée.  
* Une règle d'archivage récemment ajoutée ou modifiée s'applique aux nouveaux objets envoyés par téléchargement et n'affecte pas les objets existants. 

Pour archiver immédiatement les nouveaux objets envoyés par téléchargement dans un compartiment, entrez 0 jours dans la règle d'archivage. {:tip}

La fonction d'archivage n'est disponible que dans certaines régions. Pour plus d'informations, voir [Services intégrés](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability).
{:tip}

## Restauration d'un objet archivé
{: #archive-restore}

Pour pouvoir accéder à un objet archivé, vous devez le restaurer vers le niveau de stockage d'origine. Lorsque vous restaurez un objet, vous pouvez indiquer le nombre de jours pendant lequel l'objet doit être disponible. A la fin de la période ainsi spécifiée, la copie restaurée est supprimée.  

Le processus de restauration peut prendre jusqu'à 12 hours.{:tip}

Les sous-états d'objet archivé sont les suivants :

* Archivé : Un objet à l'état Archivé est passé de son niveau de stockage en ligne (Standard, Coffre, Coffre froid et Flex) au niveau d'archivage hors ligne sur la base de la règle d'archivage définie sur le compartiment. 
* En cours de restauration : Un objet à l'état En cours de restauration est en train de générer une copie à partir de l'état Archivé vers son niveau de stockage en ligne d'origine. 
* Restauré : Un objet de l'état Restauré est une copie de l'objet archivé qui a été restauré vers son niveau de stockage en ligne d'origine pour une durée déterminée. A la fin de la période ainsi spécifiée, la copie de l'objet est supprimée et l'objet archivé est conservé. 

## Limitations
{: #archive-limitations}

Les règles d'archivage sont implémentée à l'aide d'un sous-ensemble de l'opération d'API S3 `PUT Bucket Lifecycle Configuration`.  

Les fonctionnalités prises en charge sont notamment les suivantes :
* Spécification d'une date ou d'un nombre de jours situés dans le futur indiquant le moment où les objets passeront à l'état Archivé 
* Définition de [règles d'expiration](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry) pour les objets 

Les fonctionnalités non prises en charge sont notamment les suivantes :
* Plusieurs règles de transition par compartiment 
* Filtrage des objets à archiver à l'aide d'un préfixe ou d'une clé d'objet 
* Hiérarchisation entre les classes de stockage 

## Utilisation de l'API REST et de SDK
{: #archive-api} 

### Création d'une configuration de cycle de vie de compartiment
{: #archive-api-create} 
{: http}

Cette implémentation de l'opération `PUT` utilise le paramètre de requête `lifecycle` pour définir les paramètres de cycle de vie du compartiment. Cette opération permet de créer une définition de règle de cycle de vie unique pour un compartiment donné. La règle est définie comme une règle composée des paramètres suivants : `ID`, `Statuts` et `Transition`.
{: http}

L'action de transition permet de faire passer les objets qui seront écrits dans le compartiment à un état Archivé après une période définie. Les modifications apportées à la règle de cycle de vie d'un compartiment sont **appliquées uniquement aux nouveaux objets** qui sont écrits dans ce compartiment. 

Les utilisateurs Cloud IAM doivent disposer au minimum du rôle `Auteur` afin de pouvoir ajouter une règle de cycle de vie au compartiment. 

Les utilisateurs d'infrastructure classique doivent disposer des droits d'accès Propriétaire et être en mesure de créer des compartiments dans le compte de stockage afin de pouvoir ajouter une règle de cycle de vie au compartiment.

Cette opération n'utilise pas d'autres paramètres de requête propres aux opérations.
{: http}

En-tête | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` |Chaîne| **Obligatoire** : Hachage MD5 128 bits codé en base 64 du contenu, utilisé comme contrôle d'intégrité pour s'assurer que le contenu n'a pas été modifié alors qu'il était en transit.  
{: http}

Le corps de la demande doit contenir un bloc XML avec le schéma suivant :
{: http}

|Elément| Type   |Enfants|Ancêtre|Contrainte|
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` |Conteneur| `Rule` |Néant |Limite 1.|
| `Rule` |Conteneur| `ID`, `Status`, `Filter`, `Transition` | `LifecycleConfiguration` |Limite 1.|
| `ID` | Chaîne |Néant | `Rule` | Doit être composé de (`a-z,`A-Z0-9`) et des symboles suivants : `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter` | Chaîne | `Prefix` | `Rule` | Doit contenir un élément `Prefix`. |
| `Prefix` | Chaîne |Néant | `Filter` | **Doit** avoir pour valeur `<Prefix/>`. |
| `Transition` | `Conteneur` | `Days`, `StorageClass` | `Rule` |Limite 1.|
| `Days` |Entier non négatif|Néant | `Transition` | Doit être une valeur supérieure à 0. |
| `Date` | Date |Néant | `Transition` |Doit être au format ISO 8601 et la date doit être située dans le futur. |
| `StorageClass` | Chaîne |Néant | `Transition` | **Doit** avoir pour valeur `GLACIER`. |
{: http}

__Syntaxe__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemple 1. Notez l'emploi de barres obliques et de points dans cet exemple de syntaxe. " caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
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
{: codeblock}
{: http}
{: caption="Exemple 2. Exemple XML illustrant la création d'une configuration de cycle de vie d'objet." caption-side="bottom"}

__Exemples__
{: http}

_Exemple de demande_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Exemple 3. Exemples d'en-tête de demande permettant de créer une configuration de cycle de vie d'objet." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Exemple 4. Exemple XML illustrant un corps de demande PUT. " caption-side="bottom"}

_Exemple de réponse_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemple 5. En-têtes de réponse. " caption-side="bottom"}

---

### Extraction d'une configuration de cycle de vie de compartiment
{: #archive-api-retrieve} 
{: http}

Cette implémentation de l'opération `GET` utilise le paramètre de requête `lifecycle` pour extraire les paramètres de cycle de vie du compartiment.  

Les utilisateurs Cloud IAM doivent disposer au minimum du rôle `Reader` afin de pouvoir extraire une règle de cycle de vie pour un compartiment. 

Les utilisateurs d'infrastructure classique doivent disposer au minimum des droits d'accès `Read` sur le compartiment afin de pouvoir extraire une règle de cycle de vie pour un compartiment.

Cette opération n'utilise pas d'autres en-têtes, paramètres de requête ou contenu propres aux opérations. 

__Syntaxe__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemple 6. Variantes de syntaxe pour les demandes GET." caption-side="bottom"}

__Exemples__
{: http}

_Exemple de demande_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Exemple 7. Exemple d'en-tête de demande pour l'extraction de la configuration." caption-side="bottom"}

_Exemple de réponse_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemple 8. Exemples d'en-tête de réponse d'une demande GET." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Exemple 9. Exemple XML illustrant un corps de demande. " caption-side="bottom"}

---

### Suppression d'une configuration de cycle de vie de compartiment
{: #archive-api-delete} {: http}

Cette implémentation de l'opération `DELETE` utilise le paramètre de requête `lifecycle` pour retirer les paramètres de cycle de vie du compartiment. Les transitions définies par les règles ne seront plus effectuées pour les nouveaux objets.  

**Remarque :** les règles de transition existantes seront conservées pour les objets qui avaient déjà été écrits dans le compartiment avant la suppression des règles.

Les utilisateurs Cloud IAM doivent disposer au minimum du rôle `Writer` afin de pouvoir retirer une règle de cycle de vie d'un compartiment. 

Les utilisateurs d'infrastructure classique doivent disposer des droits d'accès `Owner` sur le compartiment afin de pouvoir retirer une règle de cycle de vie d'un compartiment.

Cette opération n'utilise pas d'autres en-têtes, paramètres de requête ou contenu propres aux opérations. 

__Syntaxe__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemple 10. Notez l'emploi de barres obliques et de points dans l'exemple de syntaxe. " caption-side="bottom"}

__Exemples__
{: http}

_Exemple de demande_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Exemple 11. Exemple d'en-têtes de demande pour l'instruction DELETE HTTP." caption-side="bottom"}

_Exemple de réponse_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemple 12. Exemple de réponse d'une demande DELETE." caption-side="bottom"}

---

### Restauration temporaire d'un objet archivé 
{: #archive-api-restore} {: http}

Cette implémentation de l'opération `POST` utilise le paramètre de requête `restore` pour demander la restauration temporaire d'un objet archivé. L'utilisateur doit commencer par restaurer un objet archivé avant de le recevoir par téléchargement ou de le modifier. Lorsqu'il restaure un objet, l'utilisateur doit spécifier une période au terme de laquelle la copie temporaire de cet objet sera supprimée. L'objet conserve la classe de stockage du compartiment. 

Un délai maximal de 12 heures peut s'écouler avant que la copie restaurée ne soit accessible. Une demande `HEAD` peut être utilisée pour vérifier si la copie restaurée est disponible.  

Pour pouvoir restaurer définitivement l'objet, l'utilisateur doit copier l'objet restauré dans un compartiment qui ne possède pas de configuration de cycle de vie active.

Les utilisateurs Cloud IAM doivent disposer au minimum du rôle `Writer` afin de pouvoir restaurer un objet. 

Les utilisateurs d'infrastructure classique doivent disposer au minimum des droits d'accès `Write` sur le compartiment et des droits d'accès `Read` sur l'objet afin de pouvoir restaurer celui-ci. 

Cette opération n'utilise pas d'autres paramètres de requête propres aux opérations.


En-tête | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` |Chaîne| **Obligatoire** : Hachage MD5 128 bits codé en base 64 du contenu, utilisé comme contrôle d'intégrité pour s'assurer que le contenu n'a pas été modifié alors qu'il était en transit.  

Le corps de la demande doit contenir un bloc XML avec le schéma suivant :

Elément| Type   |Enfants|Ancêtre|Contrainte
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` |Conteneur| `Days`, `GlacierJobParameters`    |Néant |Néant 
`Days` |Entier|Néant |`RestoreRequest` |Spécifie la durée de vie de l'objet temporairement restauré. Le nombre minimal de jours pendant lequel une copie restaurée de l'objet peut exister est 1. Une fois la période de restauration écoulée, la copie temporaire de l'objet est supprimée. 
`GlacierJobParameters` | Chaîne | `Tier` | `RestoreRequest` |Néant 
`Tier` | Chaîne |Néant |`GlacierJobParameters` |**Doit** avoir pour valeur `Bulk`.

Une réponse réussie renvoie une valeur `202` si l'objet est à l'état Archivé et une valeur `200` si l'objet est déjà à l'état Restauré. Si l'objet est déjà à l'état Restauré et qu'une nouvelle demande de restauration de l'objet est reçue, l'élément `Days` met à jour le délai d'expiration de l'objet restauré.

__Syntaxe__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemple 13. Notez l'emploi de barres obliques et de points dans l'exemple de syntaxe. " caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Exemple 14. Modèle XML d'un corps de demande. " caption-side="bottom"}

__Exemples__
{: http}

_Exemple de demande_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Exemple 15. Exemple d'en-têtes de demande pour une restauration d'objet. " caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Exemple 16. Exemple de corps de demande pour une restauration d'objet. " caption-side="bottom"}

_Exemple de réponse_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemple 17. Réponse à une restauration d'objet (`HTTP 202`)." caption-side="bottom"}

---

### Obtention des en-têtes d'un objet
{: http}
{: #archive-api-head}

Une demande `HEAD` avec un chemin d'accès à un objet permet d'extraire les en-têtes de cet objet. Cette opération n'utilise pas de paramètres de requête ou d'éléments de contenu propres aux opérations.

__Syntaxe__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemple 18. Variantes de définition de noeuds finaux. " caption-side="bottom"}


__En-têtes de réponse pour les objets archivés__
{: http}

En-tête | Type | Description
--- | ---- | ------------
`x-amz-restore` |Chaîne|Inclus si l'objet a été restauré ou si une restauration est en cours. Si l'objet a été restauré, la date d'expiration de la copie temporaire est également renvoyée. 
`x-amz-storage-class` |Chaîne|Renvoie `GLACIER` si l'objet est archivé ou temporairement restauré. 
`x-ibm-archive-transition-time` | Date | Renvoie la date et l'heure planifiées pour la transition de l'objet vers le niveau d'archivage.
`x-ibm-transition` |Chaîne| Inclus si l'objet comporte des métadonnées de transition et renvoie le niveau et l'heure d'origine de la transition.
`x-ibm-restored-copy-storage-class` |Chaîne| Inclus si un objet se trouve à l'état `RestoreInProgress` ou `Restored` et renvoie la classe de stockage du compartiment.


_Exemple de demande_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="Exemple 19. Exemple illustrant des en-têtes de demande. " caption-side="bottom"}

_Exemple de réponse_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="Exemple 20. Exemple illustrant des en-têtes de réponse. " caption-side="bottom"}


### Création d'une configuration de cycle de vie de compartiment
{: #archive-node-create} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'STRING_VALUE',
        Filter: '', /* required */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* required if Days not specified */
            Days: 0, /* required if Date not specified */
            StorageClass: 'GLACIER' /* required */
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemple 21. Exemple illustrant la création d'une configuration de cycle de vie." caption-side="bottom"}

### Extraction d'une configuration de cycle de vie de compartiment
{: #archive-node-retrieve} {: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemple 22. Exemple illustrant l'extraction de métadonnées de cycle de vie." caption-side="bottom"}

### Suppression d'une configuration de cycle de vie de compartiment
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemple 23. Exemple illustrant la suppression d'une configuration de cycle de vie d'un compartiment. " caption-side="bottom"}

### Restauration temporaire d'un objet archivé 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
  ContentMD5: 'STRING_VALUE', /* required */
  RestoreRequest: {
   Days: 1, /* days until copy expires */
   GlacierJobParameters: {
     Tier: Bulk /* required */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemple 24. Code utilisé pour la restauration d'un objet archivé. " caption-side="bottom"}

### Obtention des en-têtes d'un objet
{: #archive-node-head} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemple 25. Exemple illustrant l'extraction d'en-têtes d'objet. " caption-side="bottom"}


### Création d'une configuration de cycle de vie de compartiment
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="Exemple 26. Méthode utilisée pour créer une configuration d'objet. " caption-side="bottom"}

### Extraction d'une configuration de cycle de vie de compartiment
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Exemple 27. Méthode utilisée pour extraire une configuration d'objet. " caption-side="bottom"}

### Suppression d'une configuration de cycle de vie de compartiment
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Exemple 28. Méthode utilisée pour supprimer une configuration d'objet. " caption-side="bottom"}

### Restauration temporaire d'un objet archivé 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="Exemple 29. Restauration temporaire d'un objet archivé. " caption-side="bottom"}

### Obtention des en-têtes d'un objet
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="Exemple 30. Traitement de la réponse d'en-têtes d'objet. " caption-side="bottom"}


### Création d'une configuration de cycle de vie de compartiment
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="Exemple 31. Fonction utilisée pour définir un cycle de vie de compartiment. " caption-side="bottom"}

**Récapitulatif des méthodes**
{: java}

Méthode|  Description
--- | ---
`getBucketName()` | Extrait le nom du compartiment dont la configuration de cycle de vie est en cours de définition.
`getLifecycleConfiguration()` | Extrait la nouvelle configuration de cycle de vie pour le compartiment spécifié. 
`setBucketName(String bucketName)` | Définit le nom du compartiment dont la configuration de cycle de vie est en cours de définition. 
`withBucketName(String bucketName)` | Définit le nom du compartiment dont la configuration de cycle de vie est en cours de définition et renvoie cet objet de sorte que d'autres appels de méthode puissent être enchaînés.  
{: java}

### Extraction d'une configuration de cycle de vie de compartiment
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Exemple 32. Signature de fonction pour obtenir une configuration du cycle de vie d'objet. " caption-side="bottom"}

### Suppression d'une configuration de cycle de vie de compartiment
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Exemple 33. Fonction utilisée pour supprimer une configuration d'objet. " caption-side="bottom"}

### Restauration temporaire d'un objet archivé 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="Exemple 34. Signature de fonction pour restaurer un objet archivé. " caption-side="bottom"}

**Récapitulatif des méthodes**
{: java}

Méthode|  Description
--- | ---
`clone()` | Crée un clone superficiel de cet objet pour toutes les zones à l'exception du contexte de gestionnaire.  
`getBucketName()` | Renvoie le nom du compartiment contenant la référence à l'objet qui doit être restauré. 
`getExpirationInDays()` | Renvoie la durée, exprimée en nombre de jours, entre la création d'un objet et son expiration.
`setExpirationInDays(int expirationInDays)` | Définit la durée, exprimée en nombre de jours, entre l'envoi par téléchargement d'un objet dans le compartiment et son expiration. 
{: java}

### Obtention des en-têtes d'un objet
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="Exemple 35. Fonction utilisée pour obtenir des en-têtes d'objet. " caption-side="bottom"}

**Récapitulatif des méthodes**
{: java}

Méthode|  Description
--- | ---
`clone()` | Renvoie un clone de cet élément `ObjectMetadata` .
`getRestoreExpirationTime()` | Renvoie l'heure à laquelle un objet qui a été temporairement restauré à partir d'une archive expirera et devra être restauré à nouveau afin d'être accessible .
`getStorageClass() ` | Renvoie la classe de stockage d'origine du compartiment. 
`getIBMTransition()` | Renvoie la classe de stockage de transition et l'heure de la transition.
{: java}

## Etapes suivantes
{: #archive-next-steps}

En plus d'{{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} fournit actuellement plusieurs autres offres de stockage d'objets répondant à différents besoins des utilisateurs, toutes accessibles via des portails Web et des API REST. [En savoir plus.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
