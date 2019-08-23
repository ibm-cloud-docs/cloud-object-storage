---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup est un utilitaire flexible qui permet aux utilisateurs de sauvegarder une partie ou la totalité d'un système de fichiers local vers un système de stockage d'objet compatible avec l'API S3. Les versions Free et Pro sont disponibles pour Windows, MacOS et Linux et prennent en charge un certain nombre de services de stockage en cloud, notamment {{site.data.keyword.cos_full}}. Cloudberry Backup peut être reçu par téléchargement à partir de l'adresse [cloudberrylab.com](https://www.cloudberrylab.com/).

Cloudberry Backup comprend un grand nombre de fonctions utiles, notamment les suivantes :

* Planification
* Sauvegardes incrémentielles et de niveau bloc
* Interface de ligne de commande
* Notifications par courrier électronique
* Compression (*version Pro uniquement*)

## Cloudberry Explorer
{: #cloudberry-explorer}

Un nouveau produit issu de Cloudberry Labs offre une interface utilisateur de gestion de fichiers familière pour {{site.data.keyword.cos_short}}. [Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} est également disponible dans les versions Free et Pro, mais n'est actuellement disponible que pour Windows. Les principales fonctions sont les suivantes :

* Synchronisation dossier/compartiment
* Interface de ligne de commande
* Gestion des ACL
* Rapports sur la capacité

La version Pro inclut également les fonctions suivantes :
* Recherche 
* Chiffrement/Compression
* Envoi par téléchargement pouvant être repris
* Prise en charge de FTP/SFTP

## Utilisation de Cloudberry avec Object Storage
{: #cloudberry-cos}

Voici les points clés à retenir lors de la configuration de produits Cloudberry pour qu'ils fonctionnent avec {{site.data.keyword.cos_short}} :

* Sélectionnez `S3 Compatible` dans la liste d'options. 
* Seules les [données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) sont actuellement prises en charge. 
* Une connexion distincte est requise pour chaque compartiment. 
* Assurez-vous que le `noeud final` spécifié dans la connexion correspond à la région du compartiment sélectionné (*la sauvegarde échouera si la destination est inaccessible*). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
