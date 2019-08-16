---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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


# A propos d'{{site.data.keyword.cos_full_notm}}
{: #about-ibm-cloud-object-storage}

Les informations stockées avec {{site.data.keyword.cos_full}} sont chiffrées et réparties sur plusieurs emplacements géographiques, et accessibles via HTTP à l'aide d'une API REST. Ce service utilise les technologies de stockage distribuées fournies par le système {{site.data.keyword.cos_full_notm}} (anciennement Cleversafe).

{{site.data.keyword.cos_full_notm}} est disponible avec trois types de résilience : Inter-régional, Régional et Centre de données unique. Le type Inter-régional fournit une durabilité et une disponibilité plus élevées que le type Régional, mais offre des temps d'attente légèrement supérieurs. Il est disponible actuellement aux Etats-Unis, dans l'Union européenne et en Asie-pacifique. Le type Régional inverse ces échafaudages et distribue des objets sur plusieurs zones de disponibilité dans une région unique. Il est disponible aux Etats-Unis, dans l'Union européenne et en Asie-Pacifique. Si une région ou une zone de disponibilité donnée n'est pas disponible, le conteneur d'objets continue de fonctionner sans entrave. Le type Centre de données unique distribue des objets sur plusieurs machines au sein du même emplacement physique. Pour connaître les régions disponibles, cliquez [ici](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints). 

Les développeurs utilisent une API {{site.data.keyword.cos_full_notm}} pour interagir avec leur stockage d'objets. Cette documentation fournit de l'aide pour [commencer](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) à mettre des comptes à disposition, pour créer des compartiments, pour envoyer par téléchargement des objets et pour utiliser une référence d'interactions d'API communes. 

## Autres services IBM Object Storage
{: #about-other-cos}
En plus d'{{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} fournit actuellement plusieurs autres offres de stockage d'objets répondant à différents besoins des utilisateurs, toutes accessibles via des portails Web et des API REST. [En savoir plus.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
