---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# Sécurité et chiffrement des données
{: #security}

{{site.data.keyword.cos_full}} utilise une approche innovante pour stocker à moindre coût d'importants volumes de données non structurées tout en assurant la sécurité, la disponibilité et la fiabilité. Pour ce faire, des algorithmes de dispersion de l'information (IDA) sont utilisés. Ils ont pour but de séparer les données en "tranches" non reconnaissables qui sont distribuées dans un réseau de centres de données, ce qui rend la transmission et le stockage de données intrinsèquement privées et sécurisées. Aucune copie complète des données ne réside dans un noeud de stockage unique, et seul un sous-ensemble de noeuds doit être disponible pour pouvoir extraire entièrement les données du réseau.

Toutes les données d'{{site.data.keyword.cos_full_notm}} sont chiffrées au repos. Cette technologie chiffre individuellement chaque objet à l'aide
de clés générées par objet. Ces clés sont sécurisées et stockées de manière fiable à l'aide des algorithmes IDA qui protègent les données d'objet à l'aide d'une transformation AONT (All-or-Nothing Transform), ce qui empêche la divulgation de données clés si des noeuds individuels ou des disques durs sont compromis.

Si l'utilisateur doit contrôler les clés de chiffrement, des clés racine peuvent être fournies [par objet à l'aide de SSE-C](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c) ou [par compartiment à l'aide de SSE-KP](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp).

Le stockage est accessible via HTTPS, et les unités de stockage internes sont certifiées et communiquent entre elles à l'aide de TLS.


## Suppression des données
{: #security-deletion}

Une fois les données supprimées, il existe plusieurs mécanismes qui empêchent la récupération ou la reconstruction des objets supprimés. La suppression d'un objet passe par différentes étapes qui vont du marquage des métadonnées pour indiquer l'objet comme supprimé au retrait des régions de contenu, à la finalisation de l'effacement sur les unités elles-mêmes et à l'écrasement éventuel des blocs représentant les données de cette tranche. Selon qu'une personne a compromis le centre de données ou a pris possession des disques physiques, le moment auquel un objet devient irrécupérable dépend de la phase de suppression. Lorsque l'objet métadonnées est mis à jour, les clients externes au réseau du centre de données ne peuvent plus le lire. Lorsqu'une majorité de tranches représentant les régions de contenu a été supprimée par les unités de stockage, il n'est pas possible d'accéder à l'objet. 

## Isolement de locataire
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} est une solution de stockage d'objets à service partagé dans une infrastructure partagée. Si votre charge de travail nécessite un stockage dédié ou isolé, reportez-vous au site [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) pour plus d'informations.
