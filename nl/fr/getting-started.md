---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Tutoriel d'initiation
{: #getting-started}

Ce tutoriel d'initiation vous guide pas à pas lors de la procédure de création de compartiments, d'envoi par téléchargement d'objets et de configuration de règles d'accès afin de permettre aux autres utilisateurs de gérer vos données.
{: shortdesc}

## Avant de commencer
{: #gs-prereqs}

Vous avez besoin des éléments suivants :
  * Un [compte de plateforme {{site.data.keyword.cloud}}](https://cloud.ibm.com) 
  * Une [instance d'{{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) 
  * Des fichiers présents sur votre ordinateur local que vous devrez envoyer par téléchargement
{: #gs-prereqs}

 Lors des premières étapes à l'aide de la console de la plateforme {{site.data.keyword.cloud_notm}}, ce tutoriel prend un nouvel utilisateur. Pour les développeurs qui souhaitent commencer à utiliser l'API, voir le [guide des développeurs](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) ou la [présentation de l'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Création de compartiments dans lesquels stocker vos données
{: #gs-create-buckets}

  1. [Lorsque vous commandez {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision), une _instance de service_ est créée. {{site.data.keyword.cos_full_notm}} est un système à service partagé, et toutes les instances d'{{site.data.keyword.cos_short}} partagent une infrastructure physique. Vous êtes automatiquement redirigé vers l'instance de service où vous pouvez commencer à créer des compartiments. Vos instances {{site.data.keyword.cos_short}} sont répertoriées sous **Stockage** dans la [liste de ressources](https://cloud.ibm.com/resources). 

Les termes 'instance de ressource' et 'instance de service' font référence au même concept, et peuvent être utilisés de manière interchangeable.
{: tip}

  1. Cliquez sur **Créer un compartiment** et choisissez un nom unique. Tous les compartiments de toutes les régions du monde partagent un espace de nom unique. Vérifiez que vous disposez des [droits d'accès appropriés](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) pour créer un compartiment. 

  **Remarque** : lorsque vous créez des compartiments ou ajoutez des objets, prenez soin d'éviter d'utiliser des informations identifiant la personne. Celles-ci permettent d'identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose.
{: tip}

  1. Choisissez d'abord d'un [niveau de _résilience_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints), puis un _emplacement_ où vos données seront stockées physiquement. La résilience fait référence à la portée et à la dimension de la zone géographique dans laquelle vos données sont distribuées. La résilience _inter-régionale_ propage vos données dans plusieurs zones métropolitaines, tandis que la résilience _régionale_ propage les données dans une seule zone métropolitaine. Un _centre de données unique_ distribue les données sur des unités au sein d'un seul site. 
  2. Choisissez la [_classe de stockage_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes) du compartiment, qui reflète la fréquence à laquelle vous prévoyez de lire les données stockées et détermine les détails de facturation. Cliquez le lien **Créer** pour créer un nouveau compartiment et y accéder. 

Les compartiments vous permettent d'organiser vos données, mais ce n'est pas le seul moyen d'y parvenir. Les noms d'objet (souvent appelés _clés d'objet_) peuvent utiliser une ou plusieurs barres obliques pour un système organisationnel de type répertoire. Vous utilisez ensuite la partie du nom de l'objet avant un délimiteur pour former un _préfixe d'objet_, qui est utilisé pour répertorier les objets connexes dans un seul compartiment via l'API. {: tip}


## Ajout de certains objets à vos compartiments
{: #gs-add-objects}

A présent, accédez à l'un de vos compartiments en le sélectionnant dans la liste. Cliquez sur **Ajouter des objets**. Les nouveaux objets écrasent les objets existants portant le même nom au sein d'un même compartiment. Lorsque vous utilisez la console pour envoyer par téléchargement un objet, le nom de l'objet correspond toujours au nom du fichier. Il n'est pas nécessaire d'établir une relation entre le nom de fichier et la clé d'objet si vous utilisez l'API pour écrire des données. A présent, ajoutez une poignée de fichiers à ce compartiment. 

Les objets sont limités à 200 Mo lorsqu'ils sont envoyés par téléchargement via la console, sauf si vous utilisez le plug-in [Transfert à haute vitesse Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload). Les objets plus volumineux (jusqu'à 10 To) peuvent également être [fractionnés en parties et envoyés par téléchargement en parallèle à l'aide de l'API](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects). Les clés d'objet peuvent comporter jusqu'à 1024 caractères et il est préférable de ne pas utiliser des caractères pouvant poser des problèmes dans une adresse Web. Par exemple, `?`, `=`, `<` et d'autres caractères spéciaux peuvent entraîner un comportement inapproprié s'ils ne sont pas en codage URL.
{:tip}

## Invitation d'un utilisateur sur votre compte pour administrer vos compartiments et vos données
{: #gs-invite-user}

A présent, vous allez ajouter un autre utilisateur et lui permettre d'agir en tant qu'administrateur de l'instance et de toutes les données qui y sont stockées. 

  1. Tout d'abord, pour ajouter le nouvel utilisateur, vous devez quitter l'interface {{site.data.keyword.cos_short}} et passer à la console IAM. Accédez au menu **Gérer** et cliquez sur le lien **Accès (IAM)** > **Utilisateurs**. Cliquez sur **Inviter des utilisateurs**.
	<img alt="Invitation d'utilisateurs via IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`Figure 1 : Invitation d'utilisateurs via IAM`
  2. Entrez l'adresse électronique d'un utilisateur que vous souhaitez inviter à rejoindre votre organisation, puis développez la section **Services** et sélectionnez "Ressource" dans le menu **Affecter l'accès à**. A présent, choisissez "Cloud Object Storage" dans le menu **Services**.
	<img alt="Services IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`Figure 2 : Services IAM`
  3. A présent, trois autres zones sont affichées : _Instance de service_, _Type de ressource_ et _ID de ressource_. La première zone définit l'instance d'{{site.data.keyword.cos_short}} à laquelle l'utilisateur peut accéder. Elle peut également être définie pour octroyer le même niveau d'accès à toutes les instances d'{{site.data.keyword.cos_short}}. Les autres zones peuvent rester vides pour l'instant.
	<img alt="Invitation d'utilisateurs via IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`Figure 3 : Invitation d'utilisateurs via IAM`
  4. La case à cocher sous **Sélectionner des rôles** détermine l'ensemble d'actions disponibles pour l'utilisateur. Sélectionnez le rôle d'accès à la plateforme "Administrateur" pour permettre à l'utilisateur d'octroyer des droits d'accès à l'instance à d'autres [utilisateurs et ID de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview). Sélectionnez le rôle d'accès au service "Responsable" pour permettre à l'utilisateur de gérer l'instance {{site.data.keyword.cos_short}} et de créer et supprimer des compartiments et des objets. Ces combinaisons d'_objet_ (utilisateur), de _rôle_ (Responsable) et de _ressource_ (instance de service {{site.data.keyword.cos_short}}) constituent des [règles IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam). Pour plus d'informations sur les rôles et les règles, voir la [documentation IAM](/docs/iam?topic=iam-userroles).
	<img alt="Rôles IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`Figure 4 : Sélection de rôles via IAM`
  5. {{site.data.keyword.cloud_notm}} utilise Cloud Foundry comme plateforme de gestion des comptes sous-jacente, par conséquent, il est nécessaire d'octroyer un niveau minimal d'accès à Cloud Foundry de sorte que l'utilisateur puisse accéder à votre organisation dès le début. Sélectionnez une organisation dans le menu **Organisation**, puis sélectionnez "Auditeur" dans les menus **Rôles organisationnels** et **Rôles d'espace**. La définition des droits d'accès à Cloud Foundry permet à l'utilisateur de visualiser les services disponibles pour votre organisation, mais pas de les modifier. 

## Octroi de droits d'accès à un compartiment pour les développeurs
{: #gs-bucket-policy}

  1. Accédez au menu **Gérer** et cliquez sur le lien **Accès (IAM)** > **ID de service**. A partir de là, vous pouvez créer un _ID de service_, qui sert d'identité abstraite liée au compte. Des clés d'API peuvent être affectées aux ID de service qui sont utilisés lorsque vous ne souhaitez pas lier l'identité d'un développeur en particulier à un processus ou un composant d'une application.
	<img alt="ID de service IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`Figure 5 : ID de service IAM`
  2. Répétez le processus ci-dessus mais à l'étape 3, choisissez une instance de service spécifique et entrez "compartiment" dans la zone _Type de ressource_ et le CRN complet d'un compartiment existant dans la zone _ID de ressource_. 
  3. A présent, l'ID de service peut accéder à ce compartiment spécifique, et uniquement à celui-ci. 

## Etapes suivantes
{: #gs-next-steps}

Maintenant que vous maîtrisez le stockage d'objets via la console Web, vous souhaiterez peut-être réaliser un flux de travaux similaire à partir de la ligne de commande en utilisant l'utilitaire de ligne de commande `ibmcloud cos` pour créer l'instance de service et interagir avec IAM, et `curl` pour accéder directement à COS. Pour commencer, [Consultez la présentation de l'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api). 
