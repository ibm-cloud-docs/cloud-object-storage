---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# Gestion du chiffrement
{: #encryption}

Tous les objets stockés dans {{site.data.keyword.cos_full}} sont chiffrés par défaut à l'aide de [clés générées de façon aléatoire et d'une transformation AONT (All-or-Nothing Transform)](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security). Même si ce modèle de chiffrement par défaut fournit une sécurité au repos, certaines charges de travail doivent posséder les clés de chiffrement utilisées. Vous pouvez gérer vos clés manuellement en fournissant vos propres clés de chiffrement lors du stockage des données (SSE-C), ou vous pouvez créer des compartiments qui utilisent IBM Key Protect (SSE-KP) pour gérer les clés de chiffrement.

## Chiffrement côté serveur avec les clés de chiffrement fournies par le client (SSE-C)
{: #encryption-sse-c}

Le chiffrement SSE-C est appliqué sur les objets. Les demandes de lecture ou d'écriture d'objets ou de leurs métadonnées à l'aide de clés gérées par le client envoient les informations de chiffrement requises en tant qu'en-têtes dans les demandes HTTP. La syntaxe est identique à celle de l'API S3, et les bibliothèques compatibles S3 qui prennent en charge SSE-C doivent fonctionner comme prévu sur {{site.data.keyword.cos_full}}.

Toute demande utilisant des en-têtes SSE-C doit être envoyée à l'aide de SSL. Notez que les valeurs `ETag` dans les en-têtes de réponse ne sont *pas* le hachage MD5 de l'objet, mais une chaîne hexadécimale 32 octets générées de façon aléatoire. 

En-tête | Type | Description
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` |Chaîne| Cet en-tête est utilisé pour spécifier l'algorithme et la taille de clé à utiliser avec la clé de chiffrement stockée dans l'en-tête `x-amz-server-side-encryption-customer-key`. La chaîne `AES256` doit être affectée à cette valeur.
`x-amz-server-side-encryption-customer-key` |Chaîne| Cet en-tête est utilisé pour transporter la représentation de chaîne d'octets codée en base 64 de la clé AES 256 utilisée dans le processus de chiffrement côté serveur.
`x-amz-server-side-encryption-customer-key-MD5` |Chaîne| Cet en-tête est utilisé pour transporter le prétraitement MD5 128 bits codé en base 64 de la clé de chiffrement selon la norme RFC 1321. Le conteneur d'objets utilisera cette valeur pour vérifier que la clé est bien transmise dans `x-amz-server-side-encryption-customer-key` et qu'elle n'est pas endommagée durant le processus de transport et de codage.Le prétraitement doit être calculé sur la clé AVANT que celle-ci ne soit codée en base 64. 


## Chiffrement côté serveur avec {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}} est un système de gestion de clés (KMS) centralisé qui permet de générer, gérer et détruire les clés de chiffrement utilisées par les services {{site.data.keyword.cloud_notm}}. Vous pouvez créer une instance de {{site.data.keyword.keymanagementserviceshort}} à partir du catalogue {{site.data.keyword.cloud_notm}}. 

Une fois que vous avez une instance de {{site.data.keyword.keymanagementserviceshort}} dans une région dans laquelle vous voulez créer un nouveau compartiment, vous devez créer une clé racine et noter le nom de ressource de cloud (CRN) de cette clé.

Vous pouvez choisir d'utiliser {{site.data.keyword.keymanagementserviceshort}} afin de gérer le chiffrement pour un compartiment uniquement lors de la création. Il n'est pas possible de modifier un compartiment existant pour utiliser {{site.data.keyword.keymanagementserviceshort}}.
{:tip}

Lorsque vous créez le compartiment, vous devez fournir des en-têtes supplémentaires. 

Pour plus d'informations sur {{site.data.keyword.keymanagementservicelong_notm}}, [voir la documentation](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect).

### Initiation à SSE-KP
{: #sse-kp-gs}

Tous les objets stockés dans {{site.data.keyword.cos_full}} sont chiffrés par défaut à l'aide de plusieurs clés générées de façon aléatoire et d'une transformation AONT (All-or-Nothing Transform). Même si ce modèle de chiffrement par défaut fournit une sécurité au repos, certaines charges de travail doivent posséder les clés de chiffrement utilisées. Vous pouvez utiliser [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) pour créer, ajouter et gérer des clés, que vous pouvez ensuite associer à votre instance de {{site.data.keyword.cos_full}} afin de chiffrer des compartiments. 

### Avant de commencer
{: #sse-kp-prereqs}

Vous avez besoin des éléments suivants :
  * Un [compte de plateforme {{site.data.keyword.cloud}}](http://cloud.ibm.com) 
  * Une [instance d'{{site.data.keyword.cos_full_notm}}](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window} 
  * Une [instance d'{{site.data.keyword.keymanagementservicelong_notm}}](http://cloud.ibm.com/catalog/services/key-protect){: new_window} 
  * Des fichiers présents sur votre ordinateur local que vous devrez envoyer par téléchargement. 

### Création ou ajout d'une clé dans {{site.data.keyword.keymanagementserviceshort}}
{: #sse-kp-add-key}

Accédez à votre instance de {{site.data.keyword.keymanagementserviceshort}} et [générez ou entrez une clé](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

### Octroi d'une autorisation de service
{: #sse-kp}
Autorisez l'utilisation de {{site.data.keyword.keymanagementserviceshort}} avec IBM COS :

1. Ouvrez votre tableau de bord {{site.data.keyword.cloud_notm}}. 
2. Dans la barre de menus, cliquez sur **Gérer** &gt; **Compte** &gt; **Utilisateurs**. 
3. Dans la barre de navigation, cliquez sur **Identity & Access** &gt; **Autorisations**.
4. Cliquez sur **Créer une autorisation**.
5. Dans le menu **Service source**, sélectionnez **Cloud Object Storage**.
6. Dans le menu **Instance de service source**, sélectionnez l'instance de service que vous souhaitez autoriser. 
7. Dans le menu **Service cible**, sélectionnez **{{site.data.keyword.keymanagementservicelong_notm}}**.
8. Dans le menu **Instance de service cible**, sélectionnez l'instance de service que vous souhaitez autoriser. 
9. Activez le rôle **Lecteur**.
10. Cliquez sur **Autoriser**.

### Création d'un compartiment
{: #encryption-createbucket}

Lorsque votre clé existe dans {{site.data.keyword.keymanagementserviceshort}} et que vous avez autorisé l'utilisation du service Key Protect avec IBM COS, associez la clé à un nouveau compartiment : 

1. Accédez à votre instance de {{site.data.keyword.cos_short}}.
2. Cliquez sur **Créer un compartiment**.
3. Entrez un nom de compartiment, sélectionnez la résilience **Régional** et choisissez un emplacement et une classe de stockage.
4. Dans Configuration avancée, activez **Ajoutez des clés Key Protect**.
5. Sélectionnez l'instance de service, la clé et l'ID de clé Key Protect associés. 
6. Cliquez sur **Créer**.

Dans la liste **Compartiments et objets**, une icône de clé figure désormais sous **Avancé** pour le compartiment, indiquant ainsi qu'une clé Key Protect est activée pour ce dernier. Pour afficher les détails de la clé, cliquez sur le menu à droite du compartiment, puis cliquez sur **Afficher la clé Key Protect**.

Notez que la valeur `Etag` renvoyée pour les objets chiffrés à l'aide de SSE-KP **sera** le véritable hachage MD5 de l'objet non chiffré d'origine.
{:tip}


## Rotation des clés
{: #encryption-rotate}

La rotation des clés est une partie importante du processus d'atténuation du risque d'atteinte à la protection des données. Modifier la clé régulièrement permet de réduire le risque de perte de données si la clé est perdue ou compromise. La fréquence à laquelle la rotation des clés doit être effectuée varie en fonction des organisations et dépend d'un certain nombre de variables, notamment l'environnement, la quantité de données chiffrées, la classification des données et les lois relatives à la conformité. Le site [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){:new_window} fournit des définitions des longueurs de clé appropriées ainsi que des instructions relatives à la durée d'utilisation des clés. 

### Rotation des clés manuelle
{: #encryption-rotate-manual}

Afin d'effectuer la rotation des clés pour {{site.data.keyword.cos_short}}, vous devrez créer un nouveau compartiment avec Key Protect activé à l'aide d'une nouvelle clé racine et copier le contenu du compartiment existant dans le nouveau compartiment. 

**Remarque** : la suppression d'une clé du système provoque le partage de son contenu et des données toujours chiffrées à l'aide de cette clé. Une fois la clé retirée, cette opération est irréversible et les données sont définitivement perdues. 

1. Créez ou ajoutez une nouvelle clé racine dans votre service [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial). 
2. [Créez un nouveau compartiment](#encryption-createbucket) et ajoutez la nouvelle clé racine. 
3. Copiez tous les objets de votre compartiment d'origine dans le nouveau compartiment. 
    1. Cette étape peut être réalisée à l'aide de différentes méthodes :
        1. A partir de la ligne de commande, en utilisant [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) ou l'[interface CLI AWS](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli). 
        2. A l'aide de l'API [/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object]. 
        3. A l'aide du SDK avec [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) ou [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go). 
