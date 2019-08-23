---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# Migration des données depuis OpenStack Swift
{: #migrate}

Avant qu'{{site.data.keyword.cos_full_notm}} ne soit disponible en tant que service de la plateforme {{site.data.keyword.cloud_notm}}, les projets qui nécessitaient un conteneur d'objets utilisaient [OpenStack Swift](https://docs.openstack.org/swift/latest/) ou [OpenStack Swift (infrastructure)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift). Nous recommandons aux développeurs de mettre à jour leurs applications et de faire migrer leurs données vers {{site.data.keyword.cloud_notm}} afin de bénéficier des nouveaux avantages en matière de contrôle d'accès et de chiffrement fournis par IAM et Key Protect, et des nouvelles fonctions au fur et à mesure de leur disponibilité. 

Le concept de 'conteneur' Swift est identique au concept de 'compartiment' COS. COS limite les instances de service à 100 compartiments et certaines instances de Swift peuvent comporter un plus grand nombre de conteneurs. Les compartiments COS peuvent contenir des milliards d'objets et prennent en charge les barres obliques (`/`) dans les noms d'objet pour les "préfixes" de type répertoire lors de l'organisation des données. COS prend en charge les règles IAM aux niveaux du compartiment et de l'instance de service. {:tip}

Une approche de migration des données entre les services de stockage d'objets consiste à utiliser un outil 'sync' ou 'clone', tel que [l'utilitaire de ligne de commande `rclone` open source](https://rclone.org/docs/). Cet utilitaire synchronise une arborescence de fichiers entre deux emplacements, y compris le stockage en cloud. Lorsque `rclone` écrit des données sur COS, il utilise l'API COS/S3 pour segmenter des objets volumineux et envoyer par téléchargement les parties en parallèle selon les tailles et les seuils définis comme paramètres de configuration. 

Certaines différences entre COS et Swift doivent être prises en compte dans le cadre de la migration des données. 

  - COS ne prend pas encore en charge les règles d'expiration ou la gestion des versions. A la place, les flux de travaux qui dépendent de ces fonctions Swift doivent les gérer dans le cadre de leur logique d'application lors de la migration dans COS. 
  - COS prend en charge les métadonnées de niveau objet, mais ces informations ne sont pas conservées lors de l'utilisation de `rclone` pour migrer des données. Des métadonnées personnalisées peuvent être définies sur des objets dans COS à l'aide d'un en-tête `x-amz-meta-{key}: {value}`, mais il est conseillé de sauvegarder les métadonnées de niveau objet dans une base de données avant d'utiliser `rclone`. Des métadonnées personnalisées peuvent être appliquées aux objets existants en [copiant l'objet sur lui-même](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object). Le système reconnaîtra que les données d'objet sont identiques et ne mettra à jour que les métadonnées. Notez que `rclone` **peut** conserver les horodatages. 
  - COS utilise des règles IAM pour le contrôle d'accès aux niveaux de l'instance de service et du compartiment. [L'accès aux objets peut devenir public](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access) en définissant une liste de contrôle d'accès `public-read`, rendant ainsi l'utilisation d'un en-tête d'autorisation inutile. 
  - Les [envois par téléchargement en plusieurs parties](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) pour les objets volumineux sont traités différemment dans l'API COS/S3 et dans l'API Swift. 
  - COS autorise l'utilisation d'en-têtes HTTP facultatifs familiers, tels que `Cache-Control`, `Content-Encoding`, `Content-MD5` et `Content-Type`. 

Ce guide fournit des instructions relatives à la migration de données d'un seul conteneur Swift vers un seul compartiment COS. Cette opération devra être répétée pour tous les conteneurs que vous souhaitez migrer, et votre logique d'application devra être mise à jour pour utiliser la nouvelle API. Une fois les données migrées, vous pouvez vérifier l'intégrité du transfert à l'aide de `rclone check`, qui comparera les totaux de contrôle MD5 et générera une liste des objets pour lesquels les totaux ne correspondent pas. 


## Configuration d'{{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. Mettez à disposition une instance d'{{site.data.keyword.cos_full_notm}} à partir du [catalogue](https://cloud.ibm.com/catalog/services/cloud-object-storage) si vous n'en pas encore créé une. 
  2. Créez des compartiments dans lesquels vous stockerez vos données transférées. Lisez le [guide d'initiation](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) afin de vous familiariser avec les principaux concepts, tels que les [noeuds finaux](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) et les [classes de stockage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes). 
  3. Etant donné que la syntaxe de l'API Swift est très différente de l'API COS/S3, il peut être nécessaire de restructurer votre application afin d'utiliser des méthodes équivalentes fournies dans les SDK COS. Les bibliothèques sont disponibles en [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) ou dans l'[API REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api). 

## Configuration d'une ressource de calcul pour l'exécution de l'outil de migration
{: #migrate-compute}
  1. Choisissez une machine Linux/macOS/BSD ou un serveur IBM Cloud Infrastructure Bare Metal ou un serveur virtuel présentant la meilleure proximité avec vos données.
La configuration de serveur suivante est recommandée : RAM 32 Go, processeurs 2 à 4 coeurs et une vitesse de réseau privé de 1000 Mbits/s.   
  2. Si vous exécutez la migration sur un serveur IBM Cloud Infrastructure Bare Metal ou un serveur virtuel, utilisez les noeuds finaux Swift et COS **privés**. 
  3. Sinon, utilisez les noeuds finaux Swift et COS **publics**. 
  4. Installez `rclone` depuis [un gestionnaire de package ou un fichier binaire précompilé](https://rclone.org/install/). 

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## Configuration de `rclone` pour OpenStack Swift
{: #migrate-rclone}

  1. Créez un fichier de configuration `rclone` dans `~/.rclone.conf`. 

        ```
        touch ~/.rclone.conf
        ```

  2. Créez la source Swift en copiant et collant ce qui suit dans `rclone.conf` :

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. Procurez-vous des données d'identification OpenStack Swift.
    <br>a. Cliquez sur votre instance Swift dans le [tableau de bord de la console IBM Cloud](https://cloud.ibm.com/).
    <br>b. Cliquez sur **Données d'identification de service** dans le panneau de navigation.
    <br>c. Cliquez sur **Nouvelles données d'identification** pour générer des données d'identification. Cliquez sur **Ajouter**.
    <br>d. Affichez les données d'identification que vous avez créées et copiez le contenu JSON. 

  4. Renseignez les zones suivantes :

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. Passez directement à la section Configuration de `rclone` pour COS. 


## Configuration de `rclone` pour OpenStack Swift (infrastructure)
{: #migrate-config-swift}

  1. Créez un fichier de configuration `rclone` dans `~/.rclone.conf`. 

        ```
        touch ~/.rclone.conf
        ```

  2. Créez la source Swift en copiant et collant ce qui suit dans `rclone.conf` :

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. Procurez-vous des données d'identification OpenStack Swift (infrastructure).
    <br>a. Cliquez sur votre compte Swift dans le portail client de l'infrastructure IBM Cloud.
    <br>b. Cliquez sur le centre de données du conteneur source de migration.
    <br>c. Cliquez sur **Afficher les données d'identification**.
    <br>d. Copiez ce qui suit :
      <br>&nbsp;&nbsp;&nbsp;**Nom d'utilisateur**
      <br>&nbsp;&nbsp;&nbsp;**Clé d'API**
      <br>&nbsp;&nbsp;&nbsp;**Noeud final d'authentification**, en fonction de l'emplacement où vous exécutez l'outil de migration

  4. A l'aide des données d'identification OpenStack Swift (infrastructure), renseignez les zones suivantes :

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## Configuration de `rclone` pour COS 
{: #migrate-config-cos}

### Obtention des données d'identification COS
{: #migrate-config-cos-credential}

  1. Cliquez sur votre instance COS dans la console IBM Cloud. 
  2. Cliquez sur **Données d'identification de service** dans le panneau de navigation. 
  3. Cliquez sur **Nouvelles données d'identification** pour générer des données d'identification. 
  4. Sous **Paramètres de configuration en ligne**, ajoutez `{"HMAC":true}`. Cliquez sur **Ajouter**. 
  5. Affichez les données d'identification que vous avez créées et copiez le contenu JSON. 

### Obtention du noeud final COS
{: #migrate-config-cos-endpoint}

  1. Cliquez sur **Compartiments** dans le panneau de navigation. 
  2. Cliquez sur le compartiment cible de la migration. 
  3. Cliquez sur **Configuration** dans le panneau de navigation. 
  4. Faites défiler l'écran vers la section **Noeuds finaux** et choisissez le noeud final en fonction de l'emplacement où vous exécutez l'outil de migration. 

  5. Créez la cible COS en copiant et collant ce qui suit dans `rclone.conf` :

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. A l'aide des données d'identification et du noeud final COS, renseignez les zones suivantes :

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## Vérification de la configuration de la source et de la cible de migration
{: #migrate-verify}

1. Créez la liste des éléments du conteneur Swift afin de vérifier que `rclone` est correctement configuré. 

    ```
    rclone lsd SWIFT:
    ```

2. Créez la liste des éléments du compartiment COS afin de vérifier que `rclone` est correctement configuré. 

    ```
    rclone lsd COS:
    ```

## Exécution de `rclone`
{: #migrate-run}

1. Effectuez un essai d'exécution (aucune donnée ne sera copiée) de `rclone` afin de synchroniser les objets de votre conteneur Swift source (par exemple, `swift-test`) avec le compartiment COS cible (par exemple, `cos-test`). 

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. Vérifiez que les fichiers que vous souhaitez faire migrer apparaissent dans le résultat de la commande. Si tout vous semble correct, retirez l'option `--dry-run` et ajoutez l'option `-v` pour copier les données. L'option `--checksum` facultative permet d'éviter la mise à jour des fichiers ayant le même hachage MD5 et la même taille d'objet dans les deux emplacements. 

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   Vous devez essayer d'épuiser l'unité centrale, la mémoire et le réseau sur la machine qui exécute rclone pour obtenir le temps de transfert le plus rapide. Voici d'autres paramètres à prendre en compte pour l'optimisation de rclone :

   --checkers int : Nombre de vérificateurs à exécuter en parallèle. (Valeur par défaut : 8).
Il s'agit du nombre d'unités d'exécution de comparaison de total de contrôle en cours d'exécution. Il est recommandé d'augmenter cette valeur et de la faire passer à 64 ou plus. 

   --transfers int : Nombre de transferts de fichiers à exécuter en parallèle. (Valeur par défaut : 4).
Il s'agit du nombre d'objets à transférer en parallèle. Il est recommandé d'augmenter cette valeur et de la faire passer à 64 ou 128 ou plus. 

   --fast-list : Utilisation d'une liste récursive, le cas échéant. Permet d'utiliser davantage de mémoire, mais moins de transactions.
   Utilisez cette option pour améliorer les performances car elle permet de réduire le nombre de demandes nécessaires pour copier un objet. 

Migration de données à l'aide de copies `rclone` sans supprimer les données source.
{:tip}


3. Répétez l'opération pour tout autre conteneur nécessitant une migration. 
4. Après avoir copié toutes vos données et vérifié que votre application peut accéder aux données dans COS, supprimez votre instance de service Swift. 
