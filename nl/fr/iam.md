---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# Initiation à IAM
{: #iam}

## Rôles et actions Identity and Access Management
{: #iam-roles}

L'accès aux instances de service {{site.data.keyword.cos_full}} pour les utilisateurs de votre compte est contrôlé par {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM). Une règle d'accès avec un rôle utilisateur IAM doit être affectée à chaque utilisateur disposant d'un accès au service {{site.data.keyword.cos_full}} dans votre compte. Cette règle détermine les actions que l'utilisateur peut effectuer dans le contexte du service ou de l'instance que vous sélectionnez.
Les actions autorisées sont personnalisées et définies par le service {{site.data.keyword.Bluemix_notm}} en tant qu'opérations pouvant être réalisées sur le service. Les actions sont ensuite mappées à des rôles utilisateur IAM.

Les règles permettent d'activer différents niveaux d'accès. Voici quelques-unes des options : 

* Accès à toutes les instances du service dans votre compte
* Accès à une instance de service particulière de votre compte
* Accès à une ressource spécifique d'une instance
* Accès à tous les services activés par IAM dans votre compte

Après avoir défini la portée de la règle d'accès, vous lui affectez un rôle. Consultez les tableaux suivants qui décrivent les actions que chaque rôle autorise dans le service {{site.data.keyword.cos_short}}.

Le tableau suivant détaille les actions mappées sur les rôles de gestion de plateforme. Les rôles de gestion de plateforme permettent aux utilisateurs d'effectuer des tâches sur les ressources de service au niveau de la plateforme. Par exemple, attribuer un accès utilisateur au service, créer ou supprimer des ID de service, créer des instances et lier des instances aux applications.

| Rôle de gestion de plateforme | Description des actions |Exemples d'action|
|:-----------------|:-----------------|:-----------------|
| Afficheur | Afficher les instances de service mais ne pas les modifier | <ul><li>Création de la liste des instances de service COS disponibles</li><li>Affichage des détails de plan de service COS</li><li>Affichage des détails d'utilisation</li></ul>|
| Editeur | Exécuter toutes les actions de plateforme, à l'exception de la gestion des comptes et de l'affectation des règles d'accès |<ul><li>Création et suppression d'instances de service COS</li></ul> |
| Opérateur | Non utilisé par COS |Néant |
| Administrateur | Effectuer toutes les actions de plateforme en fonction de la ressource pour laquelle ce rôle est affecté, y compris l'affectation de règles d'accès à d'autres utilisateurs. |<ul><li>Mise à jour des règles utilisateur </li>Mise à jour des plans de tarification </ul>|
{: caption="Tableau 1. Actions et rôles utilisateur IAM" caption-side="top"}


Le tableau suivant détaille les actions qui sont mappées aux rôles d'accès au service. Les rôles d'accès aux services permettent aux utilisateurs d'accéder à {{site.data.keyword.cos_short}} ainsi que d'appeler l'API {{site.data.keyword.cos_short}}. 

| Rôle d'accès au service | Description des actions |Exemples d'action|
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| Lecteur de contenu | Recevoir par téléchargement des objets, mais pas répertorier les objets ou les compartiments | <ul><li>Réception par téléchargement d'objets</li></ul> |
| Lecteur              | En plus des actions du rôle Lecteur de contenu, les lecteurs peuvent répertorier des compartiments et/ou des objets, mais pas les modifier. | <ul><li>Création d'une liste de compartiments</li><li>Création d'une liste d'objets et téléchargement d'objets</li></ul>                    |
| Auteur              | En plus des actions du rôle Lecteur, les auteurs peuvent créer des compartiments et envoyer par téléchargement des objets. | <ul><li>Création de compartiments et d'objets</li><li>Retrait de compartiments et d'objets</li></ul> |
|Gestionnaire | En plus des actions du rôle Auteur, les gestionnaires peuvent effectuer des actions privilégiées qui affectent le contrôle d'accès. | <ul><li>Ajout d'une règle de conservation</li><li>Ajout d'un pare-feu de compartiment</li></ul>              |
{: caption="Tableau 3. Rôles et actions d'accès au service IAM" caption-side="top"}


Pour plus d'informations sur l'affectation de rôles utilisateur dans l'interface utilisateur, voir [Gestion de l'accès IAM](/docs/iam?topic=iam-iammanidaccser).
 
