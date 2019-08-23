---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# Transfert de fichiers à l'aide de CrossFTP
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} est un client FTP complet qui prend en charge des solutions de stockage en cloud compatibles S3, dont {{site.data.keyword.cos_full}}. CrossFTP prend en charge Mac OS X, Microsoft Windows et Linux et est disponible dans les versions Free, Pro et Enterprise avec des fonctions telles que celles énumérées ci-dessous : 

* Interface à onglets
* Chiffrement de mot de passe
* Recherche
* Transfert par lots
* Chiffrement (*versions Pro/Enterprise*)
* Synchronisation (*versions Pro/Enterprise*)
* Planificateur (*versions Pro/Enterprise*)
* Interface de ligne de commande (*versions Pro/Enterprise*)

## Connexion à IBM Cloud Object Storage
{: #crossftp-connect}

1. Téléchargez, installez et démarrez CrossFTP. 
2. Dans le panneau de droite, créez un nouveau site en cliquant sur l'icône plus (+) pour ouvrir le gestionnaire de site. 
3. Sous l'onglet *General*, entrez les informations suivantes :
    * Dans la zone **Protocol**, indiquez la valeur `S3/HTTPS`. 
    * Dans la zone **Label**, indiquez un nom descriptif de votre choix. 
    * Dans la zone **Host**, indiquez un noeud final {{site.data.keyword.cos_short}} (par exemple, `s3.us.cloud-object-storage.appdomain.cloud`). 
        * *Assurez-vous que la région du noeud final corresponde au compartiment cible souhaité. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).* 
    * Laissez la valeur `443` dans la zone **Port**. 
    * Dans les zones **Access Key** et **Secret**, indiquez des données d'identification HMAC avec les droits d'accès appropriés à votre compartiment cible. 
4. Sous l'onglet *S3* :
    * Assurez-vous que l'option `Use DevPay` n'est pas cochée. 
    * Cliquez sur **API Set ...** et assurez-vous que les options `Dev Pay` et `CloudFront Distribution` ne sont pas cochées. 
5. ***Pour Mac OS X uniquement*** :
    * Cliquez sur *Security > TLS/SSL Protocols...* dans la barre de menus. 
    * Sélectionnez l'option `Customize the enabled protocols`. 
    * Ajoutez `TLSv1.2` à la zone **Enabled**. 
    * Cliquez sur **OK**. 
6. ***Pour Linux uniquement*** :
    * Cliquez sur *Security > Cipher Settings...* dans la barre de menus. 
    * Sélectionnez l'option `Customize the enabled cipher suites`. 
    * Ajoutez `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` à la zone **Enabled**. 
    * Cliquez sur **OK**. 
7. Cliquez sur **Apply**, puis sur **Close**. 
8. Une nouvelle entrée sous *Sites* doit être disponible avec le *libellé* fourni à l'étape 3. 
9. Cliquez deux fois sur la nouvelle entrée pour vous connecter au noeud final. 

A partir de là, la fenêtre affiche une liste des compartiments disponibles et vous pouvez parcourir les fichiers disponibles et les transférer vers et depuis vos disques locaux. 
