---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# Transfert de fichiers à l'aide de Cyberduck
{: #cyberduck}

Cyberduck est un navigateur Cloud Object Storage populaire, open source et facile à utiliser pour les plateformes Mac et Windows. Cyberduck est capable de calculer les signatures d'autorisation appropriées qui sont nécessaires pour se connecter à IBM COS. Il peut être reçu par téléchargement à partir de [cyberduck.io/](https://cyberduck.io/){: new_window}.

Pour utiliser Cyberduck afin de créer une connexion à IBM COS et synchroniser un dossier de fichiers locaux avec un compartiment, procédez comme suit :

 1. Téléchargez, installez et démarrez Cyberduck. 
 2. La fenêtre principale de l'application s'ouvre, à partir de laquelle vous pouvez créer une connexion à IBM COS. Cliquez sur **Ouvrir une connexion** pour configurer une  connexion à IBM COS. 
 3. Une fenêtre en incrustation s'affiche. Dans le menu déroulant situé en haut, sélectionnez `S3 (HTTPS)`. Renseignez les zones suivantes, puis cliquez sur Connexion : 

    * `Serveur` : entrez le noeud final d'IBM COS. 
        * *Assurez-vous que la région du noeud final corresponde au compartiment souhaité. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).* 
    * `ID de clé d'accès`
    * `Clé d'accès secrète`
    * `Ajouter à la chaîne de certificats` : sauvegardez la connexion à la chaîne de certificats pour autoriser l'utilisation sur d'autres applications *(facultatif)*. 

 4. Cyberduck vous conduit à la racine du compte où des compartiments peuvent être créés. 
    * Cliquez avec le bouton droit de la souris dans le panneau principal et sélectionnez **Nouveau dossier** (*l'application doit gérer un grand nombre de protocoles où le dossier est la construction de conteneur la plus courante*). 
    * Entrez un nom pour le compartiment et cliquez sur Créer. 
 5. Une fois le compartiment créé, cliquez deux fois dessus pour le visualiser. Dans le compartiment, vous pouvez effectuer diverses actions, telles que les suivantes :
    * Envoyer par téléchargement des fichiers dans le compartiment
    * Répertorier le contenu du compartiment
    * Recevoir par téléchargement des objets du compartiment
    * Synchroniser des fichiers locaux avec un compartiment
    * Synchroniser des objets avec un autre compartiment
    * Créer une archive d'un compartiment
 6. Cliquez avec le bouton droit de la souris dans le compartiment et sélectionnez **Synchroniser**. Une fenêtre en incrustation s'ouvre pour vous permettre de naviguer jusqu'au dossier que vous souhaitez synchroniser avec le compartiment. Sélectionnez le dossier et cliquez sur Choisir. 
 7. Une fois le dossier sélectionné, une nouvelle fenêtre en incrustation s'ouvre. Elle contient un menu déroulant dans lequel vous pouvez sélectionner l'opération de synchronisation avec le compartiment. Ce menu déroulant propose trois options de synchronisation :

    * `Réception par téléchargement` : permet de recevoir par téléchargement à partir du compartiment les objets modifiés et manquants. 
    * `Envoi par téléchargement` : permet d'envoyer par téléchargement au compartiment les fichiers modifiés et manquants. 
    * `Reproduction` : permet d'effectuer les opérations de réception par téléchargement et d'envoi par téléchargement en s'assurant que tous les fichiers et objets nouveaux et mis à jour sont synchronisés entre le dossier local et le compartiment. 

 8. Une autre fenêtre s'ouvre pour afficher les demandes de transfert actives et historiques. Une fois la demande de synchronisation terminée, la fenêtre principale exécute une opération de création de liste sur le compartiment afin de refléter le contenu mis à jour dans le compartiment. 

## Mountain Duck
{: #mountain-duck}

Mountain Duck s'appuie sur Cyberduck pour vous permettre de monter Cloud Object Storage en tant que disque dans le Finder sous Mac ou l'explorateur sous Windows. Des versions d'évaluation sont disponibles, mais une clé d'enregistrement est requise afin de pouvoir utiliser ce logiciel une fois la période d'évaluation écoulée. 

La procédure de création d'un signet dans Mountain Duck est très similaire à la procédure de création de connexions dans Cyberduck :

1. Téléchargez, installez et démarrez Mountain Duck. 
2. Créez un nouveau signet. 
3. Dans le menu déroulant, sélectionnez `S3 (HTTPS)` et entrez les informations suivantes :
    * `Server` : entrez le noeud final d'IBM COS.  
        * *Assurez-vous que la région du noeud final corresponde au compartiment souhaité. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).* 
    * `Username` : entrez la clé d'accès. 
    * Cliquez sur **Connect**
    * Vous êtes invité à entrer votre clé secrète, qui sera ensuite sauvegardée dans la chaîne de certificats. 

Vos compartiments seront maintenant disponibles dans le Finder ou l'explorateur. Vous pouvez interagir avec {{site.data.keyword.cos_short}} comme avec tout autre système de fichiers monté. 

## Interface CLI
{: #cyberduck-cli}

Cyberduck fournit également `duck`, une interface de ligne de commande (CLI) qui s'exécute dans un interpréteur de commandes sous Linux, Mac OS X et Windows. Les instructions d'installation sont disponibles sur la [page wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window} de `duck`. 

Pour utiliser `duck` avec {{site.data.keyword.cos_full}}, vous devez ajouter un profil personnalisé au [répertoire de support d'application](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}. Vous trouverez des informations détaillées sur les profils de connexion `duck`, y compris des exemples de profil et des profils préconfigurés, sur la page [CLI help/how-to](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window}.

Voici un exemple de profil pour un noeud final COS régional :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

L'ajout de ce profil `duck` vous permet d'accéder à {{site.data.keyword.cos_short}} à l'aide d'une commande semblable à celle qui suit : 

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*Valeurs de clé*
* `<bucket-name>` - Nom du compartiment COS (*assurez-vous que le compartiment et les régions de noeud final sont cohérents*)
* `<access-key>` - Clé d'accès HMAC
* `<secret-access-key>` - Clé secrète HMAC

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

Pour obtenir une liste complète des options de ligne de commande, entrez `duck --help` dans l'interpréteur de commandes ou reportez-vous à la [page wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}. 
