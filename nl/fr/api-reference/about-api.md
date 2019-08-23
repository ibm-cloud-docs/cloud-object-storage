---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# A propos de l'API S3 {{site.data.keyword.cos_full_notm}}
{: #compatibility-api}

L'API {{site.data.keyword.cos_full}} est une API REST pour la lecture et l'écriture d'objets. Elle utilise {{site.data.keyword.iamlong}} pour l'authentification et l'autorisation et prend en charge un sous-ensemble de l'API S3 afin de faciliter la migration des applications vers {{site.data.keyword.cloud_notm}}. 

Cette documentation de référence est continuellement mise à jour. Si vous avez des questions techniques concernant l'utilisation de l'API dans votre application, posez-les sur [StackOverflow](https://stackoverflow.com/). Ajoutez les balises `ibm-cloud-platform` et `object-storage` et contribuez à améliorer cette documentation grâce à vos commentaires. 

Les jetons {{site.data.keyword.iamshort}} étant plutôt faciles à utiliser, `curl` est idéal pour les tests de base et l'interaction avec votre stockage. Pour plus d'informations, voir [les informations de référence sur `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl). 

Les tableaux ci-après décrivent l'ensemble des opérations de l'API {{site.data.keyword.cos_full_notm}} sur les compartiments. Pour plus d'informations, voir la [page de référence d'API pour les compartiments](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) ou les [objets](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).


## Opérations sur les compartiments
{: #compatibility-api-bucket}

Ces opérations permettent de créer et supprimer des compartiments, d'obtenir des informations sur des compartiments et de contrôler le comportement de compartiments. 

| Opération sur un compartiment    | Important                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` Buckets           | Permet d'extraire une liste de tous les compartiments appartenant à un compte. |
| `DELETE` Bucket         | Permet de supprimer un compartiment vide.                                      |
| `DELETE` Bucket CORS    | Permet de supprimer n'importe quelle configuration de partage de ressources d'origine croisée (CORS) définie sur un compartiment. |
| `GET` Bucket            | Permet de répertorier les objets contenus dans un compartiment. L'opération ne peut répertorier que 1 000 objets à la fois. |
| `GET` Bucket CORS       | Permet d'extraire toute configuration CORS définie sur un compartiment. |
| `HEAD` Bucket           | Permet d'extraire les en-têtes d'un compartiment. |
| `GET` Multipart Uploads | Permet de répertorier les envois par téléchargement en plusieurs parties qui ne sont pas terminés ou qui sont annulés.                     |
| `PUT` Bucket            | Les compartiments sont soumis à des restrictions de dénomination. Les comptes sont limités à 100 compartiments.|
| `PUT` Bucket CORS       | Permet de créer une configuration CORS pour un compartiment.                                     |


## Opérations sur les objets
{: #compatibility-api-object}

Ces opérations permettent de créer et supprimer des objets, d'obtenir des informations sur des objets et de contrôler le comportement d'objets. 

| Opération sur un objet          | Important                                                                                |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` Object           | Permet de supprimer un objet d'un compartiment. |
| `DELETE` Batch            | Permet de supprimer un grand nombre d'objets d'un compartiment en une seule fois.                             |
| `GET` Object              | Permet d'extraire un objet d'un compartiment. |
| `HEAD` Object             | Permet d'extraire les en-têtes d'un objet. |
| `OPTIONS` Object          | Permet de vérifier la configuration CORS pour voir si une demande spécifique peut être envoyée.           |
| `PUT` Object              | Permet d'ajouter un objet à un compartiment.                                                        |
| `PUT` Object (Copy)       | Permet de créer une copie d'un objet.                                                       |
| Begin Multipart Upload    | Permet de créer un ID d'envoi par téléchargement pour un ensemble de parties à envoyer par téléchargement.                            |
| Upload Part               | Permet d'envoyer par téléchargement une partie d'un objet associé à un ID d'envoi par téléchargement.                  |
| Upload Part (Copy)        | Permet d'envoyer par téléchargement une partie d'un objet existant associé à un ID d'envoi par téléchargement.                  |
| Complete Multipart Upload | Permet d'assembler un objet à partir de parties associées à un ID d'envoi par téléchargement.              |
| Cancel Multipart Upload   | Permet d'annuler l'envoi par téléchargement et de supprimer les parties en attente associées à un ID d'envoi par téléchargement. |
| List Parts                | Permet de renvoyer une liste des parties associées à un ID d'envoi par téléchargement.              |


Actuellement, certaines opérations supplémentaires, telles que le balisage et la gestion des versions, sont prises en charge dans les implémentations de cloud privé d'{{site.data.keyword.cos_short}}, mais pas dans les clouds publics ou dédiés. Vous trouverez plus d'informations sur les solutions Object Storage personnalisées à l'adresse [ibm.com](https://www.ibm.com/cloud/object-storage). 
