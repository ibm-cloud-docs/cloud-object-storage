---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# Conseils aux développeurs
{: #dev-guide}

## Réglage des paramètres de chiffrement
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} prend en charge une grande variété de paramètres de chiffrement pour chiffrer les données en transit. Les paramètres de chiffrement n'ont pas tous les même niveau de performance. La négociation de `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA` ou `TLS_RSA_WITH_AES_128_CBC_SHA` a généré les mêmes niveaux de performance qu'en l'absence de protocole TLS entre le client et le système {{site.data.keyword.cos_full_notm}}. 

## Utilisation des envois par téléchargement en plusieurs parties
{: #dev-guide-multipart}

Lorsque vous gérez des objets volumineux, il est recommandé d'utiliser des opérations d'envoi par téléchargement en plusieurs parties pour écrire ces objets dans {{site.data.keyword.cos_full_notm}}. L'envoi par téléchargement d'un objet peut être effectué sous la forme d'un ensemble de parties et ces parties peuvent être envoyées par téléchargement indépendamment dans n'importe quel ordre et en parallèle. Une fois l'exécution de l'envoi par téléchargement terminée, {{site.data.keyword.cos_short}} présente toutes les parties en tant qu'objet unique. Cela procure de nombreux avantages. Les interruptions réseau ne provoquent pas l'échec des envois par téléchargement volumineux, les envois par téléchargement peuvent être mis en pause et redémarrés plus tard, et les objets peuvent être envoyés par téléchargement à mesure qu'ils sont créés.

Les envois par téléchargement en plusieurs parties ne sont disponibles que pour les objets de plus de 5 Mo. Pour les objets de moins de 50 Go, il est recommandé d'utiliser une taille de partie comprise entre 20 Mo et 100 Mo afin d'optimiser les performances. Pour les objets plus volumineux, la taille des parties peut être augmentée sans que cela ait un impact significatif sur les performances. 

L'utilisation de plus de 500 parties entraîne des pertes d'efficacité dans {{site.data.keyword.cos_short}} et cela doit être évité dans la mesure du possible.

En raison de la complexité supplémentaire, il est conseillé aux développeurs d'utiliser des bibliothèques d'API S3 qui fournissent la prise en charge de l'envoi par téléchargement en plusieurs parties. 

Les envois par téléchargement en plusieurs parties qui sont incomplets subsistent jusqu'à ce qu'ils soient abandonnés à l'aide de `AbortIncompleteMultipartUpload` ou que l'objet soit supprimé. Si un envoi par téléchargement en plusieurs parties qui est incomplet n'est pas abandonné, l'envoi par téléchargement partiel continue d'utiliser les ressources. La conception des interfaces doit tenir compte de cela et prévoir le nettoyage des envois par téléchargement en plusieurs parties qui sont incomplets. 

## Utilisation de kits de développement de logiciels
{: #dev-guide-sdks}

L'utilisation des SDK d'API S3 publiés n'est pas obligatoire ; le logiciel personnalisé peut tirer parti de l'API pour s'intégrer directement à {{site.data.keyword.cos_short}}. Toutefois, l'utilisation des bibliothèques d'API S3 publiées offre des avantages, tels que la génération de signature pour authentification, la logique de relance automatique après des erreurs `5xx` et la génération d'URL présignée. Des précautions doivent être prises lors de l'écriture d'un logiciel qui utilise l'API directement pour traiter des erreurs transitoires, par exemple, en fournissant des relances avec un incrément exponentiel lorsque des erreurs `503` sont reçues. 

## Pagination
{: #dev-guide-pagination}

Lorsqu'un compartiment contient un grand nombre d'objets, il se peut que les applications Web commencent à subir une dégradation de leurs performances. De nombreuses applications utilisent une technique appelée **pagination** (*processus de fractionnement d'un ensemble d'enregistrements en pages distinctes*). Presque toutes les plateformes de développement fournissent des objets ou des méthodes pour réaliser la pagination, soit par fonctionnalité intégrée, soit par l'intermédiaire de bibliothèques tierces. 

Les kits DSK {{site.data.keyword.cos_short}} fournissent la prise en charge de la pagination via une méthode qui répertorie les objets contenus dans un compartiment spécifié. Cette méthode fournit un certain nombre de paramètres qui la rendent extrêmement utile lors de la tentative de fractionnement d'un ensemble de résultats volumineux. 

### Utilisation de base
{: #dev-guide-pagination-basics}
Le concept de base derrière la méthode de création d'une liste d'objets implique de définir le nombre maximal de clés (`MaxKeys`) à renvoyer dans la réponse. La réponse inclut également une valeur `booléenne` (`IsTruncated`) qui indique si d'autres résultats sont disponibles et une valeur `chaîne` nommée `NextContinuationToken`. Définir un jeton de continuation dans les demandes de suivi permet de renvoyer le lot d'objets suivant jusqu'à ce qu'il n'y ait plus aucun résultat disponible. 

#### Paramètres communs
{: #dev-guide-pagination-params}

|Paramètre|Description|
|---|---|
|`ContinuationToken`|Définit le jeton permettant de spécifier le lot d'enregistrements suivant. |
|`MaxKeys`|Définit le nombre maximal de clés à inclure dans la réponse. |
|`Prefix`|Limite la réponse aux clés débutant par le préfixe spécifié. |
|`StartAfter`|Définit l'emplacement à partir duquel la création de la liste d'objets doit démarrer en fonction de la clé. |

### Utilisation de Java
{: #dev-guide-pagination-java}

Le kit SDK {{site.data.keyword.cos_full}} pour Java fournit la méthode [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} qui permet de renvoyer la liste d'objets dans la taille souhaitée. Vous trouverez un exemple de code complet en cliquant [ici](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2). 

### Utilisation de Python
{: #dev-guide-pagination-python}

Le kit SDK {{site.data.keyword.cos_full}} pour Python fournit la méthode [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} qui permet de renvoyer la liste d'objets dans la taille souhaitée. Vous trouverez un exemple de code complet en cliquant [ici](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2). 
