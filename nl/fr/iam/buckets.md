---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# Droits des compartiments
{: #iam-bucket-permissions}

Attribuez des rôles d'accès aux utilisateurs et aux ID de service sur des compartiments, à l'aide de l'interface utilisateur ou de l'interface de ligne de commande, pour créer des règles.

| Rôle d'accès |Exemples d'action|
|:------------|-------------------------------------------------------------|
|Gestionnaire | Rendre les objets publics, créer et détruire des compartiments et des objets |
| Auteur      | Créer et détruire des compartiments et des objets |
| Lecteur      |Répertorier et recevoir par téléchargement des objets |
| Lecteur de contenu |Recevoir par téléchargement des objets|

## Octroi d'un accès à un utilisateur
{: #iam-user-access}

Si l'utilisateur doit pouvoir utiliser la console, il est nécessaire de lui accorder **également** un rôle d'accès minimal à la plateforme (`Afficheur`) sur l'instance proprement dite, en plus du rôle d'accès au service (par exemple, `Lecteur`). Cela lui permettra de visualiser tous les compartiments et de répertorier les objets qui s'y trouvent. Sélectionnez ensuite l'option de **droits d'accès au compartiment** dans le menu de navigation de gauche, puis choisissez l'utilisateur et le niveau d'accès requis (`Gestionnaire` ou `Editeur`) pour ce dernier.

Si l'utilisateur a l'intention d'interagir avec des données à l'aide de l'API et qu'il n'a pas besoin d'accéder à la console, _et_ s'il est membre de votre compte, vous pouvez lui accorder des droits d'accès à un seul compartiment sans qu'il ait accès à l'instance parent.

## Application des règles
{: #iam-policy-enforcement}

Les règles IAM sont appliquées hiérarchiquement du niveau d'accès le plus élevé vers le niveau d'accès le plus restreint. Les conflits sont résolus en utilisant la règle plus permissive. Par exemple, si un utilisateur possède les rôles d'accès au service `Auteur` et `Lecteur` sur un compartiment, la règle accordant le rôle `Lecteur` sera ignorée.

Cela s'applique également aux règles d'instance de service et de niveau de compartiment.

- Par exemple, si la règle affectée à un utilisateur lui octroie le rôle d'accès `Auteur` sur une instance de service et le rôle `Lecteur` sur un compartiment unique, la règle de niveau compartiment sera ignorée. 
- Si la règle affectée à un utilisateur lui octroie le rôle d'accès `Lecteur` sur une instance de service et le rôle `Auteur` sur un compartiment unique, les deux règles seront appliquées et le rôle `Auteur` plus permissif sera prioritaire pour le compartiment individuel. 

S'il est nécessaire de restreindre l'accès à un seul compartiment (ou à un ensemble de compartiments), assurez-vous qu'aucune règle de niveau instance n'est affectée à l'utilisateur ou à l'ID de service en utilisant la console ou l'interface de ligne de commande.

### Utilisation de l'interface utilisateur
{: #iam-policy-enforcement-console}

Pour créer une règle de niveau compartiment : 

  1. Accédez à la console **Accès (IAM)** à partir du menu **Gérer**. 
  2. Sélectionnez **Utilisateurs** dans le menu de navigation de gauche. 
  3. Sélectionnez un utilisateur. 
  4. Sélectionnez l'onglet **Règles d'accès** pour visualiser les règles existantes de l'utilisateur, affecter une nouvelle règle ou éditer une règle existante. 
  5. Cliquez sur **Affecter un accès** pour créer une nouvelle règle. 
  6. Choisissez **Affecter l'accès aux ressources**. 
  7. Sélectionnez d'abord **Cloud Object Storage** dans le menu des services. 
  8. Sélectionnez ensuite l'instance de service appropriée. Entrez `bucket` dans la zone **Type de ressource** et le nom du compartiment dans la zone **Ressource**. 
  9. Sélectionnez le rôle d'accès au service souhaité.
  10.  Cliquez sur **Affecter**. 

Notez que si la zone **Type de ressource** ou **Ressource** est vide, une règle de niveau instance sera créée. {:tip}

### Utilisation de l'interface de ligne de commande
{: #iam-policy-enforcement-cli}

A partir d'un terminal, exécutez la commande suivante :

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Pour répertorier des règles existantes :

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

Pour éditer une règle existante :

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## Octroi d'un accès à un utilisateur
{: #iam-service-id}
Si vous devez accorder l'accès à un compartiment pour une application ou une autre entité non humaine, utilisez un ID de service. L'ID de service peut être créé spécifiquement pour cette opération ou il peut s'agir d'un ID service existant déjà utilisé. 

### Utilisation de l'interface utilisateur
{: #iam-service-id-console}

  1. Accédez à la console **Accès (IAM)** à partir du menu **Gérer**. 
  2. Sélectionnez **ID de service** dans le menu de navigation de gauche. 
  3. Sélectionnez un ID de service pour afficher des règles existantes et affectez une nouvelle règle ou éditez une règle existante.
  3. Sélectionnez l'instance de service, l'ID de service et le rôle souhaité.
  4. Entrez `bucket` dans la zone **Type de ressource** et le nom de compartiment dans la zone **Ressource**. 
  5. Cliquez sur **Soumettre**.

  Notez que si la zone **Type de ressource** ou **Ressource** est vide, une règle de niveau instance sera créée. {:tip}

### Utilisation de l'interface de ligne de commande
{: #iam-service-id-cli}

A partir d'un terminal, exécutez la commande suivante :

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Pour répertorier des règles existantes :

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

Pour éditer une règle existante :

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
