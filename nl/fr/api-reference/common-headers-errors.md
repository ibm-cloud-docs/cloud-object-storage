---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# En-têtes communs et codes d'erreur
{: #compatibility-common}

## En-têtes de demande communs
{: #compatibility-request-headers}

Le tableau ci-après décrit les en-têtes de demande communément pris en charge. {{site.data.keyword.cos_full}} ignore les en-têtes communs qui ne sont pas répertoriés ci-après s'ils sont envoyés dans une demande, bien que certaines demandes puissent prendre en charge d'autres en-têtes comme indiqué dans cette documentation.

|En-tête | Important                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Authorization           | **Obligatoire** pour toutes les demandes (jeton `bearer` OAuth2). |
| ibm-service-instance-id | **Obligatoire** pour les demandes de création ou d'affichage de liste de compartiments. |
| Content-MD5             | Hachage MD5 128 bits codé en base 64 du contenu, utilisé comme contrôle d'intégrité pour s'assurer que le contenu n'a pas été modifié alors qu'il était en transit. |
| Expect                  | La valeur `100-continue` attend que le système stipule que les en-têtes sont appropriés avant d'envoyer le contenu. |
| host                    | Syntaxe du noeud final ou de l'hôte virtuel : `{bucket-name}.{endpoint}`. En général, cet en-tête est automatiquement ajouté. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).| 
| Cache-Control | Peut être utilisé pour spécifier le comportement de la mise en cache en même temps que la chaîne demande/réponse. Pour plus d'informations, accédez à http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 |

### Métadonnées personnalisées
{: #compatibility-headers-metadata}

L'un des avantages liés à l'utilisation du stockage d'objets est la possibilité d'ajouter des métadonnées personnalisées en envoyant des paires clé-valeur en tant qu'en-têtes. Ces en-têtes se présentent comme suit : `x-amz-meta-{KEY}`. Notez que contrairement à AWS S3, IBM COS combinera plusieurs en-têtes ayant la même clé de métadonnées dans une liste de valeurs séparées par des virgules. 

## En-têtes de réponse communs
{: #compatibility-response-headers}

Le tableau ci-après décrit les en-têtes de réponse communément pris en charge. 

|En-tête | Important                                                |
|------------------|-----------------------------------------------------|
| Content-Length   | Longueur du corps de demande, exprimée en octets. |
| Connection       | Permet d'indiquer si la connexion est ouverte ou fermée. |
| Date             | Horodatage de la demande. |
| ETag             | Valeur de hachage MD5 de la demande. |
| Server           | Nom du serveur ayant répondu. |
| X-Clv-Request-Id | Identificateur unique généré par chacune des demandes. |

### En-têtes de réponse de cycle de vie
{: #compatibility-lifecycle-headers}

Le tableau ci-après décrit les en-têtes de demande pour les objets archivés. 

|En-tête | Important                                                |
|------------------|-----------------------------------------------------|
|x-amz-restore|Inclus si l'objet a été restauré ou si une restauration est en cours. |
|x-amz-storage-class|Renvoie `GLACIER` si l'objet est archivé ou temporairement restauré. |
|x-ibm-archive-transition-time| Renvoie la date et l'heure planifiées pour la transition de l'objet vers le niveau d'archivage.|
|x-ibm-transition| Inclus si l'objet comporte des métadonnées de transition et renvoie le niveau et l'heure d'origine de la transition.|
|x-ibm-restored-copy-storage-class| Inclus si un objet se trouve à l'état `RestoreInProgress` ou `Restored` et renvoie la classe de stockage du compartiment.|

## Codes d'erreur
{: #compatibility-errors}

|Code d'erreur| Description                                                                                                                                                             | Code de statut HTTP                    |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | Accès refusé |403 Interdit|
| BadDigest                           | L'élément Content-MD5 que vous avez spécifié ne correspondait pas à ce que nous avions reçu |400 Demande incorrecte |
| BucketAlreadyExists                 | Le nom de compartiment demandé n'est pas disponible. L'espace de nom de compartiment est partagé par tous les utilisateurs du système. Sélectionnez un autre nom et renouvelez l'opération. | 409 Conflit                        |
| BucketAlreadyOwnedByYou             | Votre demande précédente visant à créer le compartiment indiqué avait abouti et vous le possédez déjà. | 409 Conflit                        |
| BucketNotEmpty                      | Le compartiment que vous avez essayé de supprimer n'est pas vide.| 409 Conflit                        |
| CredentialsNotSupported             | Cette demande de prend pas en charge de données d'identification. |400 Demande incorrecte |
| EntityTooSmall                      | L'envoi par téléchargement que vous avez proposé est inférieur à la taille d'objet minimale autorisée.  |400 Demande incorrecte |
| EntityTooLarge                      | L'envoi par téléchargement que vous avez proposé dépasse la taille d'objet maximale autorisée. |400 Demande incorrecte |
| IncompleteBody                      | Vous n'avez pas indiqué le nombre d'octets spécifié par l'en-tête HTTP Content-Length. |400 Demande incorrecte |
| IncorrectNumberOfFilesInPostRequest | POST requiert exactement un envoi par téléchargement de fichier par demande. |400 Demande incorrecte |
| InlineDataTooLarge                  | Les données en ligne dépassent la taille maximale autorisée. |400 Demande incorrecte |
| InternalError                       | Une erreur interne s'est produite. Réessayez.|500 Erreur de serveur interne |
| InvalidAccessKeyId                  | L'ID de clé d'accès AWS fourni n'existe pas dans nos enregistrements. |403 Interdit|
| InvalidArgument                     | Argument non valide.|400 Demande incorrecte |
| InvalidBucketName                   | Le compartiment indiqué n'est pas valide. |400 Demande incorrecte |
| InvalidBucketState                  | La demande n'est pas valide avec l'état en cours du compartiment.| 409 Conflit                        |
| InvalidDigest                       | L'élément Content-MD5 que vous avez spécifié n'est pas valide. |400 Demande incorrecte |
| InvalidLocationConstraint           | La contrainte d'emplacement spécifiée n'est pas valide. Pour plus d'informations sur les régions, voir la rubrique expliquant comment sélectionner une région pour les compartiments. |400 Demande incorrecte |
| InvalidObjectState                  | L'opération n'est pas valide avec l'état en cours de l'objet. |403 Interdit|
| InvalidPart                         | Une ou plusieurs des parties indiquées sont introuvables. Il se peut que la partie n'ait pas été envoyée par téléchargement ou que la balise d'entité spécifiée ne corresponde pas à la balise d'entité de la partie. |400 Demande incorrecte |
| InvalidPartOrder                    | La liste des parties n'était pas classée par ordre croissant. La liste des parties doit être spécifiée dans l'ordre par numéro de référence. |400 Demande incorrecte |
| InvalidRange                        | L'intervalle demandé ne peut pas être satisfait. | 416 Intervalle demandé impossible |
| InvalidRequest                      | Utilisez AWS4-HMAC-SHA256. |400 Demande incorrecte |
| InvalidSecurity                     | Les données d'identification de sécurité fournies ne sont pas valides. |403 Interdit|
| InvalidURI                          | Impossible de faire une analyse syntaxique de l'URI spécifié. |400 Demande incorrecte |
| KeyTooLong                          | Votre clé est trop longue. |400 Demande incorrecte |
| MalformedPOSTRequest                | Le corps de votre demande POST n'est pas constitué de données de formulaire/multiple bien formées. |400 Demande incorrecte |
| MalformedXML                        | Le document XML que vous avez fourni n'était pas bien formé ou sa validation par rapport à notre schéma publié a échoué. |400 Demande incorrecte |
| MaxMessageLengthExceeded            | Votre demande est trop volumineuse. |400 Demande incorrecte |
| MaxPostPreDataLengthExceededError   | Les zones de demande POST précédant le fichier d'envoi par téléchargement étaient trop volumineuses. |400 Demande incorrecte |
| MetadataTooLarge                    | Vos en-têtes de métadonnées dépassent la taille maximale autorisée pour les métadonnées. |400 Demande incorrecte |
| MethodNotAllowed                    | La méthode spécifiée n'est pas autorisée pour cette ressource. |405 Méthode non autorisée|
| MissingContentLength                | Vous devez fournir l'en-tête HTTP Content-Length.  |411 Longueur requise|
| MissingRequestBodyError             | Cela se produit lorsque l'utilisateur envoie un document XML vide comme demande. Le message d'erreur est "Le corps de demande est vide." |400 Demande incorrecte |
| NoSuchBucket                        | Le compartiment spécifié n'existe pas. |404 Introuvable|
| NoSuchKey                           | La clé spécifiée n'existe pas.|404 Introuvable|
| NoSuchUpload                        | L'envoi par téléchargement en plusieurs parties n'existe pas. L'ID d'envoi par téléchargement est peut-être incorrect ou l'envoi par téléchargement en plusieurs parties a peut-être été abandonné ou a abouti. |404 Introuvable|
| NotImplemented                      | Un en-tête que vous avez fourni implique une fonctionnalité qui n'est pas implémentée. | 501 Non implémenté |
| OperationAborted                    | Une opération conditionnelle conflictuelle est actuellement en cours sur cette ressource. Essayez à nouveau. | 409 Conflit                        |
| PreconditionFailed                  | Au moins l'une des préconditions que vous avez spécifiées n'a pas été satisfaite. |412 Echec de la précondition|
| Redirect                            | Redirection temporaire. | 307 Déplacé temporairement |
| RequestIsNotMultiPartContent        | L'événement POST de compartiment doit être de type de boîtier Données de formulaire/multiple. |400 Demande incorrecte |
| RequestTimeout                      | Votre connexion socket au serveur n'a pas été lue ou écrite dans le délai imparti. |400 Demande incorrecte |
| RequestTimeTooSkewed                | La différence entre l'heure de la demande et l'heure du serveur est trop élevée. |403 Interdit|
| ServiceUnavailable                  | Réduisez votre taux de demande. | 503 Service non disponible|
| SlowDown                            | Réduisez votre taux de demande. | 503 Ralentir |
| TemporaryRedirect                   | Vous êtes redirigé vers le compartiment pendant la mise à jour de DNS. | 307 Déplacé temporairement |
| TooManyBuckets                      | Vous avez tenté de créer plus de compartiments qu'autorisé. |400 Demande incorrecte |
| UnexpectedContent                   | Cette demande de prend pas en charge le contenu. |400 Demande incorrecte |
| UserKeyMustBeSpecified              | L'événement POST de compartiment doit contenir le nom de zone spécifié. Si cette zone est spécifiée, vérifiez l'ordre des zones. |400 Demande incorrecte |
