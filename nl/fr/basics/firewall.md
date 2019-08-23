---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-21"

keywords: ip address, firewall, configuration, api

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

# Définition d'un pare-feu
{: #setting-a-firewall}

Les règles IAM permettent aux administrateurs de limiter l'accès aux compartiments individuels. Que faire si certaines données ne doivent être accessibles qu'à partir de réseaux de confiance ? Un pare-feu de compartiment restreint tous les accès aux données, sauf si la demande provient d'une liste d'adresses IP autorisées.
{: shortdesc}

Certaines règles s'appliquent lors de la définition d'un pare-feu :

* Un utilisateur qui définit ou visualise un pare-feu doit posséder le rôle `Manager` sur le compartiment.  
* Un utilisateur disposant du rôle `Manager` sur le compartiment peut afficher et éditer la liste des adresses IP autorisées à partir de n'importe quelle adresse IP pour empêcher les verrouillages accidentels. 
* La console {{site.data.keyword.cos_short}} peut toujours accéder au compartiment, à condition que l'adresse IP de l'utilisateur soit autorisée. 
* Les autres services {{site.data.keyword.cloud_notm}} **ne sont pas autorisés** à contourner le pare-feu. Cette limitation signifie que les autres services dont l'accès au compartiment est régi par des règles IAM (tels que Aspera, SQL Query, Security Advisor, Watson Studio, Cloud Functions, etc.) ne seront pas en mesure d'accéder au compartiment. 

Lorsqu'un pare-feu est défini, le compartiment est isolé du reste d'{{site.data.keyword.cloud_notm}}. Tenez compte de l'impact de ceci sur les applications et les flux de travaux qui dépendent des autres services qui accèdent directement à un compartiment, avant d'activer le pare-feu.
{: important}

## Utilisation de la console pour définir un pare-feu
{: #firewall-console}

Tout d'abord, vérifiez que vous disposez d'un compartiment. Si tel n'est pas le cas, suivez le [tutoriel d'initiation](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) pour vous familiariser avec la console.

### Définition d'une liste d'adresses IP autorisées
{: #firewall-console-enable}

1. Dans le {{site.data.keyword.cloud_notm}}[tableau de bord de la console](https://cloud.ibm.com/), sélectionnez **Stockage** pour afficher votre liste de ressources. 
2. Sélectionnez ensuite l'instance de service contenant votre compartiment à partir du menu **Stockage**. Vous accédez ainsi à la console {{site.data.keyword.cos_short}}. 
3. Choisissez le compartiment pour lequel vous souhaitez limiter l'accès à des adresses IP autorisées.  
4. Sélectionnez **Règles d'accès** dans le menu de navigation. 
5. Sélectionnez l'onglet **Adresses IP autorisées**. 
6. Cliquez sur **Ajouter des adresses IP**, puis choisissez **Ajouter**.
7. Ajoutez une liste d'adresses IP en [notation CIDR](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing), par exemple, `192.168.0.0/16, fe80:021b::0/64`. Les adresses peuvent suivre les normes IPv4 ou IPv6. Cliquez sur **Ajouter**. 
8. Le pare-feu ne sera pas appliqué tant que l'adresse ne sera pas sauvegardée dans la console. Cliquez sur **Sauvegarder tout** pour appliquer le pare-feu. 
9. Désormais, tous les objets de ce compartiment ne sont accessibles qu'à partir de ces adresses IP. 

### Retrait des restrictions d'adresse IP
{: #firewalls-console-disable}

1. Dans l'onglet **Adresses IP autorisées**, cochez les cases en regard des adresses IP ou des plages à retirer de la liste d'adresses autorisées. 
2. Sélectionnez **Supprimer**, puis confirmez la suppression en cliquant à nouveau sur **Supprimer** dans la boîte de dialogue qui s'affiche. 
3. La liste d'adresses mise à jour ne sera pas appliquée tant que les modifications ne seront pas sauvegardées dans la console. Cliquez sur **Sauvegarder tout** pour appliquer les nouvelles règles. 
4. Désormais, tous les objets de ce compartiment ne sont accessibles qu'à partir de ces adresses IP. 

Si aucune adresse IP autorisée n'est répertoriée, cela signifie que les règles IAM standard s'appliqueront au compartiment, sans aucune restriction concernant l'adresse IP de l'utilisateur.
{: note}


## Définition d'un pare-feu via une API
{: #firewall-api}

Les pare-feu sont gérés à l'aide de l'[API de configuration des ressources COS](https://cloud.ibm.com/apidocs/cos/cos-configuration). Cette nouvelle API REST est utilisée pour la configuration des compartiments.  

Les utilisateurs disposant du rôle `Manager` peuvent afficher et éditer la liste des adresses IP autorisées à partir de n'importe quel réseau pour empêcher les verrouillages accidentels.
{: tip}
