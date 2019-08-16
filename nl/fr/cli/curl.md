---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, curl, cli

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

# Utilisation de `curl`
{: #curl}

Cette rubrique est un aide-mémoire sur les commandes `curl` de base pour l'API REST {{site.data.keyword.cos_full}}. D'autres informations figurent dans la référence d'API pour des [compartiments](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) ou des [objets](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations). 

L'utilisation de `curl` suppose de posséder des connaissances en matière d'utilisation de la ligne de commande et du stockage d'objets et d'avoir récupéré les informations nécessaires à partir des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), de la[référence de noeuds finaux](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) ou de la [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Si des termes ou des variables ne vous sont pas familiers, vous pouvez consulter leur définition dans le [glossaire](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology). 

**Remarque** : Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose. {:tip}

## Demande d'un jeton IAM
{: #curl-iam}

Il existe deux manières de générer un jeton oauth IAM pour l'authentification des demandes : à l'aide d'une commande `curl` avec une clé d'API (décrite ci-dessous) ou à partir de la ligne de commande à l'aide de l'[interface de ligne de commande IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli).  

### Demande d'un jeton IAM à l'aide d'une clé d'API
{: #curl-token}

Tout d'abord, procurez-vous une clé d'API. Pour l'obtenir, accédez à [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys). 

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## Obtention de votre ID d'instance de ressource
{: #curl-instance-id}

Certaines des commandes ci-après requièrent un paramètre `ibm-service-instance-id`. Pour trouver cette valeur, accédez à l'onglet **Données d'identification de service** de votre instance Object Storage dans la console IBM Cloud. Créez de nouvelles données d'identification, si besoin, puis utilisez le menu déroulant *Afficher les données d'identification* pour voir le format JSON. Utilisez la valeur de `resource_instance_id`.  

Pour utiliser des API curl, vous n'avez besoin que de l'identificateur unique universel (UUID) qui débute après le dernier signe deux-points et se termine avant le double signe deux-points final. Par exemple, l'ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` peut être abrégé en `39d8d161-22c4-4b77-a856-f11db5130d7d`.
{:tip}

## Création d'une liste de compartiments
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Ajout d'un compartiment
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Ajout d'un compartiment (classe de stockage)
{: #curl-add-bucket-class}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}

Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint).

## Création d'un partage de ressources d'origine croisée pour un compartiment
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

L'en-tête `Content-MD5` doit être la représentation binaire d'un hachage MD5 codé en base 64. 

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Obtention d'un partage de ressources d'origine croisée pour un compartiment
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Suppression d'un partage de ressources d'origine croisée pour un compartiment
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Création d'une liste d'objets
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Obtention d'en-têtes de compartiment
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Suppression d'un compartiment
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Envoi par téléchargement d'un objet
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## Obtention des en-têtes d'un objet
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Copie d'un objet
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## Vérification des informations relatives à un partage de ressources d'origine croisée
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## Réception par téléchargement d'un objet
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Vérification de la liste de contrôle d'accès d'un objet
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Activation de l'accès anonyme à un objet
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Suppression d'un objet
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Suppression de plusieurs objets
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

L'en-tête `Content-MD5` doit être la représentation binaire d'un hachage MD5 codé en base 64. 

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Lancement d'un envoi par téléchargement en plusieurs parties
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Envoi par téléchargement d'une partie
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## Achèvement d'un envoi par téléchargement en plusieurs parties
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## Obtention d'envois par téléchargement en plusieurs parties qui sont incomplets
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Abandon d'envois par téléchargement en plusieurs parties qui sont incomplets
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
