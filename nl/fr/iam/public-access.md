---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Activation de l'accès public
{: #iam-public-access}

Parfois, les données sont faites pour être partagées. Les compartiments peuvent contenir des ensembles de données ouverts pour des recherches académiques et privées ou des référentiels d'images qui sont utilisés par des applications Web et des réseaux de distribution de contenu. Rendez ces compartiments accessibles en utilisant le groupe **Accès public**.
{: shortdesc}

## Utilisation de la console pour définir l'accès public
{: #iam-public-access-console}

Tout d'abord, vérifiez que vous disposez d'un compartiment. Si tel n'est pas le cas, suivez le [tutoriel d'initiation](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) pour vous familiariser avec la console.

### Activation de l'accès public
{: #public-access-console-enable}

1. Dans le {{site.data.keyword.cloud_notm}}[tableau de bord de la console](https://cloud.ibm.com/), sélectionnez **Stockage** pour afficher votre liste de ressources. 
2. Sélectionnez ensuite l'instance de service contenant votre compartiment à partir du menu **Stockage**. Vous accédez ainsi à la console {site.data.keyword.cos_short}}. 
3. Choisissez le compartiment qui doit être accessible publiquement. Gardez à l'esprit que cette règle rend _tous les objets d'un compartiment_ disponibles pour téléchargement pour tout le monde avec l'URL appropriée. 
4. Sélectionnez **Règles d'accès** dans le menu de navigation. 
5. Sélectionnez l'onglet **Accès public**. 
6. Cliquez sur **Créer une règle d'accès**. Après avoir lu l'avertissement, choisissez **Activer**.
7. A présent, tous les objets de ce compartiment sont accessibles publiquement. 

### Désactivation de l'accès public
{: #public-access-console-disable}

1. A partir de n'importe où dans la {{site.data.keyword.cloud_notm}}console [](https://cloud.ibm.com/), sélectionnez le menu **Gérer**, puis **Accès (IAM)**.
2. Sélectionnez **Groupes d'accès** dans le menu de navigation. 
3. Sélectionnez **Accès public** pour afficher la liste de toutes les règles d'accès public en cours d'utilisation.
4. Recherchez la règle correspondant au compartiment dont vous souhaitez renforcer le contrôle d'accès. 
5. Dans la liste d'actions située à l'extrême droite de l'entrée de règle, sélectionnez **Supprimer**.
6. Confirmez l'opération de suppression. La règle est maintenant retirée du compartiment.

## Activation de l'accès public sur des objets individuels
{: #public-access-object}

Pour rendre un objet accessible au public via l'API REST, vous pouvez inclure un en-tête `x-amz-acl: public-read` dans la demande. Le fait de définir cet en-tête permet d'ignorer les contrôles de [règle IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) et d'autoriser les demandes `HEAD` et `GET` non authentifiées. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

De plus, les [données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) permettent d'autoriser un [accès public temporaire qui utilise des URL présignées](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url).

### Envoi par téléchargement d'un objet public
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### Activation de l'accès public à un objet existant
{: #public-access-object-existing}

Le fait d'utiliser le paramètre de requête `?acl` sans un contenu et l'en-tête `x-amz-acl: public-read` permet d'autoriser l'accès public à l'objet sans avoir à écraser les données. 

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### Reprivatisation d'un objet public
{: #public-access-object-private}

Le fait d'utiliser le paramètre de requête `?acl` sans un contenu et l'en-tête `x-amz-acl:` permet de révoquer l'accès public à l'objet sans avoir à écraser les données. 

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## Sites Web statiques
{: #public-access-static-website}

Bien qu'{{site.data.keyword.cos_full_notm}} ne prenne pas en charge l'hébergement automatique de sites Web statiques, il est possible de configurer manuellement un serveur Web et de l'utiliser pour servir un contenu accessible publiquement hébergé dans un compartiment. Pour plus d'informations, voir [ce tutoriel](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).
