---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: administrator, storage, iam, access

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

# Pour les administrateurs
{: #administrators}

Les administrateurs système et les administrateurs de stockage qui doivent configurer le stockage des objets et gérer l'accès aux données peuvent bénéficier d'IBM Cloud Identity and Access Management (IAM) pour gérer les utilisateurs, créer et faire pivoter des clés d'API et accorder des rôles aux utilisateurs et aux services. Si vous ne l'avez pas déjà fait, suivez le [tutoriel d'initiation](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) pour vous familiariser avec les concepts de base des compartiments, des objets et des utilisateurs. 

## Configuration de votre stockage
{: #administrators-setup}

Tout d'abord, vous devez disposer d'au moins une instance de ressource Object Storage et de compartiments dans lesquels stocker les données. Imaginez ces compartiments en pensant à la manière vous souhaitez segmenter davantage l'accès à vos données, à l'emplacement où vous souhaitez que vos données résident physiquement et à quelle fréquence vos données seront consultées. 

### Segmentation de l'accès
{: #administrators-access}

Vous pouvez segmenter l'accès au niveau de l'instance de ressource et au niveau du compartiment.  

Vous souhaitez peut-être faire en sorte qu'une équipe de développement puisse uniquement accéder aux instances de stockage d'objets qu'elle gère et non à celles utilisées par d'autres équipes. Ou vous souhaitez faire en sorte que seul le logiciel que votre équipe est en train de créer puisse effectivement éditer les données stockées, par conséquent, vous voulez que vos développeurs qui ont accès à la plateforme cloud puissent lire les données uniquement à des fins de traitement des incidents. Voici des exemples de règles de niveau de service.

A présent, si l'équipe de développement, ou un utilisateur individuel, dispose de l'accès Afficheur à une instance de stockage mais doit pouvoir éditer des données directement dans un ou plusieurs compartiments, vous pouvez utiliser des règles de niveau compartiment pour augmenter le niveau d'accès accordé aux utilisateurs dans votre compte. Par exemple, un utilisateur peut ne pas être en mesure de créer de nouveaux compartiments, mais peut créer et supprimer des objets dans des compartiments existants. 

## Gestion des accès
{: #administrators-manage-access}

IAM est basé sur un concept fondamental : un _sujet_ se voit accorder un _rôle_ sur une _ressource_. 

Il existe deux types de base de sujet : un _utilisateur_ et un _ID service_. 

Il existe un autre concept, les _données d'identification de service_. Les données d'identification de service sont un ensemble d'informations importantes nécessaires pour se connecter à une instance d'{{site.data.keyword.cos_full}}. Elles incluent au minimum un identificateur pour l'instance d'{{site.data.keyword.cos_full_notm}} (c'est-à-dire l'ID d'instance de ressource), des noeuds finaux de service/d'authentification et un moyen d'associer le sujet à une clé d'API (c'est-à-dire l'ID de service). Lorsque vous créez les données d'identification de service, vous avez la possibilité de les associer à un ID de service existant ou de créer un nouvel ID de service. 

Par conséquent, si vous souhaitez permettre aux membres de votre équipe de développement d'utiliser la console pour visualiser des instances Object Storage et des clusters Kubernetes, ils doivent disposer du rôle `Viewer` sur les ressources Object Storage et du rôle `Administrator` sur le service de conteneur. Notez que le rôle `Viewer` permet uniquement à l'utilisateur de vérifier que l'instance existe et de visualiser les données d'identification existantes, mais **pas** de visualiser des compartiments et des objets. Lorsque les données d'identification de service ont été créées, elles ont été associées à un ID de service. Cet ID de service doit posséder le rôle `Manager` ou `Writer` sur l'instance pour pouvoir créer et détruire des compartiments et des objets. 

Pour plus d'informations sur les rôles et les droits IAM, voir [la présentation d'IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview). 
