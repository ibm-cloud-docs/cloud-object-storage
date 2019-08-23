---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# Utilisation des données d'identification HMAC
{: #hmac}

L'API {{site.data.keyword.cos_full}} est une API REST pour la lecture et l'écriture d'objets. Elle utilise {{site.data.keyword.iamlong}} pour l'authentification et l'autorisation et prend en charge un sous-ensemble de l'API S3 afin de faciliter la migration des applications vers {{site.data.keyword.cloud_notm}}. 

En plus de l'authentification basée sur un jeton IAM, il est également possible de s'[authentifier à l'aide d'une signature](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) créée à partir d'une paire de clé d'accès et de clé secrète. Cette méthode d'authentification fonctionne comme la signature AWS version 4, et les clés HMAC fournies par IBM COS doivent fonctionner avec la majorité des bibliothèques et des outils compatibles S3. 

Les utilisateurs peuvent créer un ensemble de données d'identification HMAC lorsqu'ils créent des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) en fournissant le paramètre de configuration `{"HMAC":true}` lors de la création des données d'identification. Voici un exemple montrant comment utiliser l'interface de ligne de commande {{site.data.keyword.cos_full}} pour créer une clé de service avec des données d'identification HMAC à l'aide du rôle **Writer** (d'autres rôles peuvent être disponibles pour votre compte et peuvent correspondre davantage à vos besoins).  

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="Exemple 1. Utilisation de cURL pour créer des données d'identification HMAC. Notez l'emploi de guillemets simples et doubles. " caption-side="bottom"}

Si vous souhaitez stocker les résultats de la clé qui vient d'être générée par la commande illustrée dans l'exemple 1, vous pouvez ajouter ` > file.skey` à la fin de l'exemple. Pour les besoins de cet ensemble d'instructions, seul l'en-tête `cos_hmac_keys` avec les clés enfant `access_key_id` et `secret_access_key` sont nécessaires, comme illustré dans l'exemple 2 :

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="Exemple 2. Clés de note lors de la génération des données d'identification HMAC." caption-side="bottom"}

La possibilité de définir des variables d'environnement (dont les instructions sont spécifiques au système d'exploitation concerné) revêt un intérêt particulier. Ainsi, dans l'exemple 3, un script `.bash_profile` contient `COS_HMAC_ACCESS_KEY_ID` et `COS_HMAC_SECRET_ACCESS_KEY` qui sont exportés lors du démarrage d'un interpréteur de commandes et utilisés pour le développement. 

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="Exemple 3. Utilisation des données d'identification HMAC comme variables d'environnement." caption-side="bottom"}

Une fois les données d'identification de service créées, la clé HMAC est incluse dans la zone `cos_hmac_keys`. Ces clés HMAC sont ensuite associées à un [ID de service](/docs/iam?topic=iam-serviceids#serviceids) et peuvent être utilisées pour accéder à toutes les ressources ou opérations autorisées par le rôle de l'ID de service.  

Notez que lorsque vous utilisez les données d'identification HMAC pour créer des signatures à utiliser avec des appels d'[API REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) directs, des en-têtes supplémentaires sont requis :
1. Toutes les demandes doivent comporter un en-tête `x-amz-date` avec la date au format `%Y%m%dT%H%M%SZ`. 
2. Toute demande ayant un contenu (envois par téléchargement d'objet, suppression de plusieurs objets, etc.) doit fournir un en-tête `x-amz-content-sha256` avec un hachage SHA256 du contenu. 
3. Les listes de contrôle d'accès (autres que `public-read`) ne sont pas prises en charge. 

Les outils compatibles S3 ne sont pas tous pris en charge actuellement. Certains outils tentent de définir des listes de contrôle d'accès autres que `public-read` pour la création de compartiment. La création de compartiment via ces outils échoue. Si une demande `PUT bucket` échoue en générant une erreur de liste de contrôle d'accès non prise en charge, utilisez d'abord la [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) pour créer le compartiment, puis configurez l'outil pour lire et écrire des objets sur ce compartiment. Les outils qui définissent des listes de contrôle d'accès sur les écritures d'objet ne sont pas pris en charge actuellement. {:tip}
