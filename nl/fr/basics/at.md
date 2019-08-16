---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: activity tracker, event logging, observability

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
{:table: .aria-labeledby="caption"}


# Evénements {{site.data.keyword.cloudaccesstrailshort}}
{: #at-events}

Utilisez le service {{site.data.keyword.cloudaccesstrailfull}} pour savoir comment les utilisateurs et les applications interagissent avec {{site.data.keyword.cos_full}}.
{: shortdesc}

Le service {{site.data.keyword.cloudaccesstrailfull_notm}} enregistre les activités initiées par l'utilisateur qui changent l'état d'un service dans {{site.data.keyword.Bluemix_notm}}. Pour plus d'informations, voir
[Initiation à {{site.data.keyword.cloudaccesstrailshort}}](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started).



## Liste des événements
{: #at-events-list}

Le tableau suivant répertorie les actions qui génèrent un événement :

<table>
  <caption>Actions générant des événements</caption>
  <tr>
    <th>Actions</th>
	  <th>Description</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>Un événement est généré lorsqu'un utilisateur demande des métadonnées de compartiment et qu'IBM Key Protect est activé ou non sur le compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>Un événement est généré lorsqu'un utilisateur crée un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>Un événement est généré lorsqu'un utilisateur demande la liste des objets contenus dans un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>Un événement est généré lorsqu'un utilisateur met à jour un compartiment, par exemple, lorsqu'un utilisateur renomme un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>Un événement est généré lorsqu'un utilisateur supprime un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>Un événement est généré lorsqu'un utilisateur affecte à la liste de contrôle d'accès sur un compartiment les valeurs `public-read` ou `private`. </td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>Un événement est généré lorsqu'un utilisateur lit la liste de contrôle d'accès sur un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>Un événement est généré lorsqu'un utilisateur crée une configuration de partage de ressources d'origine croisée pour un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>Un événement est généré lorsqu'un utilisateur demande si une configuration de partage de ressources d'origine croisée est activée sur un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>Un événement est généré lorsqu'un utilisateur modifie une configuration de partage de ressources d'origine croisée pour un compartiment.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>Un événement est généré lorsqu'un utilisateur supprime une configuration de partage de ressources d'origine croisée pour un compartiment.</td>
  </tr>
</table>



## Emplacement des événements
{: #at-ui}

Les événements {{site.data.keyword.cloudaccesstrailshort}} sont disponibles dans le **domaine de compte** {{site.data.keyword.cloudaccesstrailshort}}. 

Les événements sont envoyés à la région {{site.data.keyword.cloudaccesstrailshort}} la plus proche de l'emplacement de compartiment {{site.data.keyword.cos_full_notm}} qui est affiché sur la [page des services pris en charge](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability).
