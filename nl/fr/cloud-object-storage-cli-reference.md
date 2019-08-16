---

copyright:
  years: 2017, 2018, 2019
lastupdated: "26-06-2019"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:note: .note}

# Utilisation de l'interface CLI IBM Cloud
{: #ic-use-the-ibm-cli}

Le plug-in Cloud Object Storage étend l'interface de ligne de commande (CLI) IBM Cloud avec un encapsuleur d'API afin de gérer les ressources Object Storage.

## Prérequis
{: #ic-prerequisites}
* Un compte [IBM Cloud](https://cloud.ibm.com/) 
* Une instance d'[IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision) 
* L'[interface CLI IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli) 


## Installation et configuration
{: #ic-installation}

Le plug-in est compatible avec les systèmes d'exploitation Windows, Linux et macOS qui s'exécutent sur des processeurs 64 bits. 

Installez le plug-in à l'aide de la commande `plugin install`. 

```
ibmcloud plugin install cloud-object-storage
```

Une fois le plug-in installé, vous pouvez le configurer à l'aide de la commande [`ibmcloud cos config`](#configure-the-program). Vous pouvez vous servir de cette configuration pour remplir le plug-in avec vos données d'identification, l'emplacement d'envoi par téléchargement par défaut, choisir votre authentification, etc.

Le programme vous permet également de définir le répertoire local par défaut pour les fichiers reçus par téléchargement, ainsi qu'une région par défaut. Pour définir l'emplacement de réception par téléchargement par défaut, tapez `ibmcloud cos config ddl` et entrez un chemin de fichier valide dans le programme. Pour définir une région par défaut, tapez `ibmcloud cos config region` et entrez un code de région dans le programme, par exemple, `us-south`. Par défaut, cette valeur est `us-geo`.


Si vous utilisez l'authentification IAM, vous devez fournir un nom de ressource de cloud (CRN) pour utiliser certaines des commandes. Pour définir le CRN, vous pouvez taper `ibmcloud cos config crn` et fournir votre CRN. Pour trouver le CRN, vous pouvez exécuter la commande `ibmcloud resource service-instance INSTANCE_NAME`. Vous pouvez aussi ouvrir la console Web, sélectionner **Données d'identification de service** dans la barre de navigation et créer un nouvel ensemble de données d'identification (ou afficher un fichier de données d'identification existant que vous avez déjà créé). 

Vous pouvez afficher vos données d'identification Cloud Object Storage en cours à l'aide de la commande `ibmcloud cos config list`. Etant donné que le fichier de configuration est généré par le plug-in, il est préférable de ne pas l'éditer manuellement. 

### Données d'identification HMAC
{: #ic-hmac-credentials}

Si vous préférez, vous pouvez utiliser les [données d'identification HMAC d'un ID de service](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac) à la place de votre clé d'API. Exécutez la commande `ibmcloud cos config hmac` pour entrer les données d'identification HMAC, puis changez de méthodes d'autorisation à l'aide de la commande `ibmcloud cos config auth`.

Si vous choisissez d'utiliser l'authentification par jeton avec votre propre clé d'API, vous n'avez pas besoin de fournir des données d'identification car le programme vous authentifie automatiquement.
{: note}

A tout moment, pour passer de l'authentification HMAC à l'authentification IAM, vous pouvez taper `ibmcloud cos config auth`. Pour plus d'informations sur l'authentification et l'autorisation dans IBM Cloud, voir la rubrique sur [Identity and Access Management](/docs/iam?topic=iam-iamoverview). 

## Index de commande
{: #ic-command-index}

| Commandes |  |  |
| --- | --- | --- |
| [`abort-multipart-upload`](#abort-a-multipart-upload) | [`complete-multipart-upload`](#complete-a-multipart-upload) | [`config`](#configure-the-program) |
| [`copy-object`](#copy-object-from-bucket) | [`create-bucket`](#create-a-new-bucket) | [`create-multipart-upload`](#create-a-new-multipart-upload) |
| [`delete-bucket`](#delete-an-existing-bucket) | [`delete-bucket-cors`](#delete-bucket-cors) | [`delete-object`](#delete-an-object) |
| [`delete-objects`](#delete-multiple-objects) | [`download`](#download-objects-using-s3manager) | [`get-bucket-class`](#get-a-buckets-class) | 
| [`get-bucket-cors`](#get-bucket-cors) | [`get-bucket-location`](#find-a-bucket) | [`get-object`](#download-an-object) |
| [`head-bucket`](#get-a-buckets-headers) | [`head-object`](#get-an-objects-headers) | [`list-buckets`](#list-all-buckets) | 
| [`list-buckets-extended`](#extended-bucket-listing) | [`list-multipart-uploads`](#list-in-progress-multipart-uploads) | [`list-objects`](#list-objects) |
| [`list-parts`](#list-parts) | [`put-bucket-cors`](#set-bucket-cors) | [`put-object`](#upload-an-object) |
| [`upload`](#upload-objects-using-s3manager) | [`upload-part`](#upload-a-part) | [`upload-part-copy`](#upload-a-part-copy) |
| [`wait`](#wait) |  |  |

Chacune des opérations mentionnées ci-après est répertoriée avec une description des actions qu'elle permet d'effectuer, de sa syntaxe et des paramètres facultatifs ou obligatoires qui lui sont associés. Tous les paramètres répertoriés sont obligatoires, sauf ceux mentionnés comme étant facultatifs. 

Le plug-in CLI ne prend pas en charge la suite complète des fonctions disponibles dans Object Storage, telles que l'option Transfert haut débit Aspera, Immutable Object Storage, la création de compartiments Key Protect, ou les pare-feux de compartiment.
{: note}

### Abandon d'un envoi par téléchargement en plusieurs parties
{: #ic-abort-multipart-upload}
* **Action** : permet d'abandonner une instance d'envoi par téléchargement en plusieurs parties en mettant fin à l'envoi par téléchargement vers le compartiment dans le compte IBM Cloud Object Storage de l'utilisateur. 
* **Syntaxe :** `ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* ID d'envoi par téléchargement identifiant l'envoi par téléchargement en plusieurs parties. 
		* Option : `--upload-id ID`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Achèvement d'un envoi par téléchargement en plusieurs parties
{: #ic-complete-multipart-upload}
* **Action** : permet de terminer une instance d'envoi par téléchargement en plusieurs parties en assemblant les parties envoyées par téléchargement et en envoyant par téléchargement le fichier vers le compartiment dans le compte IBM Cloud Object Storage de l'utilisateur. 
* **Syntaxe :** `ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* ID d'envoi par téléchargement identifiant l'envoi par téléchargement en plusieurs parties. 
		* Option : `--upload-id ID`
	* Structure de l'envoi par téléchargement en plusieurs parties à définir. 
		* Option : `--multipart-upload STRUCTURE`
		* Syntaxe abrégée :  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* Syntaxe JSON :  
	`--multipart-upload file://<filename.json>`  
	La commande `--multipart-upload` prend une structure JSON qui décrit les parties de l'envoi par téléchargement en plusieurs parties qui doivent être réassemblées dans le fichier complet. Dans cet exemple, le préfixe `file://` est utilisé pour charger la structure JSON à partir du fichier spécifié.
```
			{
  			"Parts": [
    			{
     			 "ETag": "string",
     			 "PartNumber": integer
    			}
    			...
  				]
			}
		```
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


## Contrôle manuel des envois par téléchargement en plusieurs parties
{: #ic-manual-multipart-uploads}

L'interface CLI IBM Cloud Object Storage permet aux utilisateurs d'envoyer par téléchargement des fichiers volumineux en plusieurs parties à l'aide des fonctions d'envoi par téléchargement en plusieurs parties. Pour lancer un nouvel envoi par téléchargement en plusieurs parties, exécutez la commande `create-multipart-upload`, qui renvoie l'ID d'envoi par téléchargement de la nouvelle instance d'envoi par téléchargement. Afin de poursuivre le processus d'envoi par téléchargement, vous devez sauvegarder l'ID d'envoi par téléchargement pour chaque commande suivante. 

Une fois que vous avez exécuté la commande `complete-multipart-upload`, exécutez la commande `upload-part` pour chaque partie de fichier à envoyer par téléchargement. **Pour les envois par téléchargement en plusieurs parties, chaque partie de fichier (à l'exception de la dernière partie) doit avoir une taille d'au moins 5 Mo. ** Pour fractionner un fichier en plusieurs parties distinctes, vous pouvez exécuter `split` dans une fenêtre de terminal. Par exemple, si vous disposez d'un fichier de 13 Mo nommé `TESTFILE` sur votre bureau et que vous souhaitez le fractionner en plusieurs parties de 5 Mo chacune, vous pouvez exécuter la commande `split -b 3m ~/Desktop/TESTFILE partie-file-`. Cette commande génère trois parties de fichier dans deux parties de fichier de 5 Mo chacune et une partie de fichier de 3 Mo, qu'elle nomme `part-file-aa`, `part-file-ab` et `part-file-ac`.  

A mesure que chaque partie de fichier est envoyée par téléchargement, l'interface CLI affiche la valeur ETag correspondante. Vous devez sauvegarder cette valeur ETag dans un fichier JSON, avec le numéro de référence. Utilisez le modèle suivant pour créer votre propre fichier de données JSON ETag :

```
{
  "Parts": [
    {
      "PartNumber": 1,
      "ETag": "The ETag of the first file part goes here."
    },
    {
      "PartNumber": 2,
      "ETag": "The ETag of the second file part goes here."
    }
  ]
}
```

Ajoutez d'autres entrées à ce modèle JSON si nécessaire. 

Pour afficher le statut de votre instance d'envoi par téléchargement en plusieurs parties, vous pouvez toujours exécuter la commande `upload-part`, en indiquant le nom du compartiment, la clé et l'ID d'envoi par téléchargement. Cette commande permet d'afficher des informations brutes sur votre instance d'envoi par téléchargement en plusieurs parties. Une fois l'envoi par téléchargement de chaque partie du fichier terminé, exécutez la commande `complete-multipart-upload` avec les paramètres nécessaires. Si tout se passe bien, vous recevez un message vous confirmant que le fichier a bien été envoyé par téléchargement dans le compartiment souhaité. 

### Configuration du programme
{: #ic-config}
* **Action :** permet de configurer les préférences du programme. 
* **Syntaxe :** `ibmcloud cos config [COMMAND]`
* **Commandes :**
	* Passer de l'authentification HMAC à l'authentification IAM 
		* Commande : `auth`
	* Stocker le CRN dans la configuration 
		* Commande : `crn`
	* Stocker l'emplacement de réception par téléchargement par défaut dans la configuration 
		* Commande : `ddl`
	* Stocker les identifiants HMAC dans la configuration 
		* Commande : `hmac`
	* Créer la liste des éléments de configuration 
		* Commande : `list`
	* Stocker la région par défaut dans la configuration 
		* Commande : `region`
	* Passer du style d'URL VHost à Chemin 
		* Commande : `url-style`


### Copie d'un objet à partir d'un compartiment
{: #ic-copy-object}
* **Action :** permet de copier un objet d'un compartiment source vers un compartiment de destination. 
* **Syntaxe :** `ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* (SOURCE) Nom du compartiment source et nom de clé de l'objet source, séparés par une barre oblique (/). Il doivent être en codage URL.
		* Option : `--copy-source SOURCE`
	* _Facultatif _ : permet de spécifier `CACHING_DIRECTIVES` pour la chaîne de demande et de réponse. 
		* Option : `--cache-control CACHING_DIRECTIVES`
	* _Facultatif_ : permet de spécifier les informations de présentation (`DIRECTIVES`). 
		* Option : `--content-disposition DIRECTIVES`
	* _Facultatif_ : permet de spécifier les codages de contenu (CONTENT_ENCODING) qui sont appliqués à l'objet, et par conséquent, les mécanismes de décodage qui doivent être appliqués pour obtenir le type de support référencé par la zone d'en-tête Content-Type. 
		* Option : `--content-encoding CONTENT_ENCODING`
	* _Facultatif_ : langue dans laquelle le contenu est écrit. 
		* Option : `--content-language LANGUAGE`
	* _Facultatif_ : type MIME standard qui décrit le format des données d'objet. 
		* Option : `--content-type MIME`
	* _Facultatif_ : permet de copier l'objet si sa balise d'entité (Etag) correspond à la balise spécifiée (ETAG). 
		* Action : `--copy-source-if-match ETAG`
	* _Facultatif_ : permet de copier l'objet s'il a été modifié depuis l'heure indiquée (TIMESTAMP). 
		* Action : `--copy-source-if-modified-since TIMESTAMP`
	* _Facultatif_ : permet de copier l'objet si sa balise d'entité (ETag) est différente de la balise spécifiée (ETAG). 
		* Action : `--copy-source-if-none-match ETAG`
	* _Facultatif_ : permet de copier l'objet s'il n'a pas été modifié depuis l'heure indiquée (TIMESTAMP). 
		* Action : `--copy-source-if-unmodified-since TIMESTAMP`
	* _Facultatif_ : mappe des métadonnées à stocker. Syntaxe : KeyName1=string,KeyName2=string
		* Option : `--metadata MAP`
	* _Facultatif_ : permet de spécifier si les métadonnées sont copiées à partir de l'objet source ou remplacées par des métadonnées fournies dans la demande. Valeurs de DIRECTIVE : COPY,REPLACE.
		* Option : ` --metadata-directive DIRECTIVE`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Création d'un nouveau compartiment
{: #ic-create-bucket}

* **Action :** permet de créer un compartiment dans une instance IBM Cloud Object Storage. 
* **Syntaxe :** `ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* Notez que vous devez fournir un CRN si vous utilisez l'authentification IAM. Pour cela, vous pouvez utiliser la commande [`ibmcloud cos config crn`](#configure-the-program). 
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : nom de la classe. 
		* Option : `--class CLASS_NAME`
	* _Facultatif_ : permet de définir l'ID de l'instance de service IBM dans la demande. 
		* Option : `--ibm-service-instance-id ID`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`



### Création d'un nouveau téléchargement en plusieurs parties
{: #ic-create-multipart-upload}
* **Action :** permet de commencer le processus d'envoi par téléchargement de fichiers en plusieurs parties en créant une nouvelle instance d'envoi par téléchargement en plusieurs parties.
* **Syntaxe :** `ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* _Facultatif _ : permet de spécifier `CACHING_DIRECTIVES` pour la chaîne de demande et de réponse. 
		* Option : `--cache-control CACHING_DIRECTIVES`
	* _Facultatif_ : permet de spécifier les informations de présentation (`DIRECTIVES`). 
		* Option : `--content-disposition DIRECTIVES`
	* _Facultatif_ : permet de spécifier le codage du contenu (`CONTENT_ENCODING`) de l'objet. 
		* Option : `--content-encoding CONTENT_ENCODING`
	* _Facultatif_ : langue dans laquelle le contenu est écrit. 
		* Option : `--content-language LANGUAGE`
	* _Facultatif_ : type MIME standard qui décrit le format des données d'objet. 
		* Option : `--content-type MIME`
	* _Facultatif_ : mappe des métadonnées à stocker. Syntaxe : KeyName1=string,KeyName2=string
		* Option : `--metadata MAP`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Suppression d'un compartiment existant
{: #ic-delete-bucket}

* **Action :** permet de supprimer un compartiment existant dans une instance IBM Cloud Object Storage. 
* **Syntaxe :** `ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
    * _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
       * Option : `--region REGION`
    * _Facultatif_ : aucune conformation ne sera demandée. 
       * Option : `--force`
    * _Facultatif_ : sortie renvoyée au format JSON brut. 
       * Option : `--json`


### Suppression d'une configuration de partage de ressources d'origine croisée pour un compartiment
{: #ic-delete-bucket-cors}
* **Action :** permet de supprimer une configuration de partage de ressources d'origine croisée sur un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe : ** `ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Suppression d'un objet
{: #ic-delete-object}
* **Action :** permet de supprimer un objet d'un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe :** `ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
  * _Facultatif_ : aucune conformation ne sera demandée. 
  	* Option : `--force`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Suppression de plusieurs objets
{: #ic-delete-objects}
* **Action :** permet de supprimer plusieurs objets d'un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe :** `ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment.   
		* Option : `--bucket BUCKET_NAME`  
	* Structure qui utilise une syntaxe abrégée ou JSON.   
		* Option : `--delete STRUCTURE`  
		* Syntaxe abrégée :  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* Syntaxe JSON :  
	`--delete file://<filename.json>`  
	La commande `--delete` prend une structure JSON qui décrit les parties de l'envoi par téléchargement en plusieurs parties qui doivent être réassemblées dans le fichier complet. Dans cet exemple, le préfixe `file://` est utilisé pour charger la structure JSON à partir du fichier spécifié.
```
	{
  	"Objects": [
    	{
    	"Key": "string",
    	"VersionId": "string"
    	}
    ...
  	],
  	"Quiet": true|false
	}
	```
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Réception par téléchargement d'objets à l'aide de S3Manager
{: #ic-download-s3manager}
* **Action :** permet de recevoir par téléchargement de façon simultanée des objets à partir de s3. 
* **Syntaxe :** `ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Paramètres à fournir :**
	* Nom (BUCKET_NAME) du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* _Facultatif_ : nombre de goroutines qui doivent être lancées parallèlement lors de chaque appel pour réception par téléchargement au moment de l'envoi de parties. La valeur par défaut est 5.
		* Option : `--concurrency value`
	* _Facultatif_ : taille de mémoire tampon (exprimée en octets) à utiliser lors de la mise en mémoire tampon de données dans des blocs et de leur envoi sous forme de parties à S3. La plus petite taille de partie autorisée est de 5 Mo.
		* Option : `--part-size SIZE`
	* _Facultatif_ : permet de renvoyer l'objet uniquement si sa balise d'entité (ETag) est identique à la valeur ETAG spécifiée, sinon, un code d'erreur 412 (échec de la précondition) est renvoyé. 
		* Option : `--if-match ETAG`
	* _Facultatif_ : permet de renvoyer l'objet uniquement s'il a été modifié depuis l'horodatage indiqué, sinon, un code d'erreur 304 (non modifié) est renvoyé. 
		* Flag: `--if-modified-since TIMESTAMP`
	* _Facultatif_ : permet de renvoyer l'objet uniquement si sa balise d'entité (ETag) est différente de la valeur ETAG spécifiée, sinon, un code d'erreur 304 (non modifié) est renvoyé. 
		* Option : `--if-none-match ETAG`
	* _Facultatif_ : permet de renvoyer l'objet uniquement s'il n'a pas été modifié depuis l'horodatage indiqué, sinon, un code d'erreur 412 (échec de la précondition) est renvoyé. 
		* Option : `--if-unmodified-since TIMESTAMP`
	* _Facultatif_ : permet de recevoir par téléchargement la plage d'octets spécifiée pour un objet. Pour plus d'informations sur l'en-tête de la plage HTTP, [cliquez ici](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35). 
		* Option : `--range RANGE`
	* _Facultatif_ : permet de définir l'en-tête Cache-Control de la réponse. 
		* Option : `--response-cache-control HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Disposition de la réponse. 
		* Option : `--response-content-disposition HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Encoding de la réponse. 
		* Option : `--response-content-encoding HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Language de la réponse. 
		* Option : `--response-content-language HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Type de la réponse. 
		* Option : `--response-content-type HEADER`
	* _Facultatif_ : permet de définir l'en-tête Expires de la réponse. 
		* Option : `--response-expires HEADER`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilisera l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`
	* _Facultatif_ : emplacement où le contenu de l'objet a été sauvegardé. Si ce paramètre n'est pas fourni, le programme utilise l'emplacement par défaut. 
		* Paramètre : `OUTFILE`


### Obtention d'une classe de compartiment
{: #ic-bucket-class}
* **Action :** permet de déterminer la classe d'un compartiment dans une instance IBM Cloud Object Storage. 
* **Syntaxe :** `ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Obtention d'une configuration de partage de ressources d'origine croisée pour un compartiment
{: #ic-get-bucket-cors}
* **Action :** permet de renvoyer la configuration de partage de ressources d'origine croisée pour un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe :** `ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Paramètres à fournir :**
  * Nom du compartiment.   
    * Option : `--bucket BUCKET_NAME`
  * _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
    * Option : `--region REGION`
  * _Facultatif_ : sortie renvoyée au format JSON brut. 
    * Option : `--json`


### Recherche d'un compartiment
{: #ic-find-bucket}
* **Action :** permet de déterminer la région et la classe d'un compartiment dans une instance IBM Cloud Object Storage.  
* **Syntaxe :** `ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`
	


### Réception par téléchargement d'un objet
{: #ic-download-object}
* **Action :** permet de recevoir par téléchargement un objet d'un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe :** `ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* _Facultatif_ : permet de renvoyer l'objet uniquement si sa balise d'entité (ETag) est identique à la valeur ETAG spécifiée, sinon, un code d'erreur 412 (échec de la précondition) est renvoyé. 
		* Option : `--if-match ETAG`
	* _Facultatif_ : permet de renvoyer l'objet uniquement s'il a été modifié depuis l'horodatage indiqué, sinon, un code d'erreur 304 (non modifié) est renvoyé. 
		* Flag: `--if-modified-since TIMESTAMP`
	* _Facultatif_ : permet de renvoyer l'objet uniquement si sa balise d'entité (ETag) est différente de la valeur ETAG spécifiée, sinon, un code d'erreur 304 (non modifié) est renvoyé. 
		* Option : `--if-none-match ETAG`
	* _Facultatif_ : permet de renvoyer l'objet uniquement s'il n'a pas été modifié depuis l'horodatage indiqué, sinon, un code d'erreur 412 (échec de la précondition) est renvoyé. 
		* Option : `--if-unmodified-since TIMESTAMP`
	* _Facultatif_ : permet de recevoir par téléchargement la plage d'octets spécifiée pour un objet. 
		* Option : `--range RANGE`
	* _Facultatif_ : permet de définir l'en-tête Cache-Control de la réponse. 
		* Option : `--response-cache-control HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Disposition de la réponse. 
		* Option : `--response-content-disposition HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Encoding de la réponse. 
		* Option : `--response-content-encoding HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Language de la réponse. 
		* Option : `--response-content-language HEADER`
	* _Facultatif_ : permet de définir l'en-tête Content-Type de la réponse. 
		* Option : `--response-content-type HEADER`
	* _Facultatif_ : permet de définir l'en-tête Expires de la réponse. 
		* Option : `--response-expires HEADER`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`
	* _Facultatif_ : emplacement où le contenu de l'objet a été sauvegardé. Si ce paramètre n'est pas fourni, le programme utilise l'emplacement par défaut. 
		* Paramètre : `OUTFILE`


### Obtention en-têtes d'un compartiment
{: #ic-bucket-header}
* **Action :** permet de déterminer si un compartiment existe dans une instance IBM Cloud Object Storage. 
* **Syntaxe :** `ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Obtention des en-têtes d'un objet
{: #ic-object-header}
* **Action :** permet de déterminer si un fichier existe dans un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe :** `ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* _Facultatif_ : permet de renvoyer l'objet uniquement si sa balise d'entité (ETag) est identique à la valeur ETAG spécifiée, sinon, un code d'erreur 412 (échec de la précondition) est renvoyé. 
		* Option : `--if-match ETAG`
	* _Facultatif_ : permet de renvoyer l'objet uniquement s'il a été modifié depuis l'horodatage indiqué, sinon, un code d'erreur 304 (non modifié) est renvoyé. 
		* Flag: `--if-modified-since TIMESTAMP`
	* _Facultatif_ : permet de renvoyer l'objet uniquement si sa balise d'entité (ETag) est différente de la valeur ETAG spécifiée, sinon, un code d'erreur 304 (non modifié) est renvoyé. 
		* Option : `--if-none-match ETAG`
	* _Facultatif_ : permet de renvoyer l'objet uniquement s'il n'a pas été modifié depuis l'horodatage indiqué, sinon, un code d'erreur 412 (échec de la précondition) est renvoyé. 
		* Option : `--if-unmodified-since TIMESTAMP`
	* Permet de recevoir par téléchargement la plage d'octets spécifiée pour un objet. 
		* Option : `--range RANGE`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Création de la liste de tous les compartiments
{: #ic-list-buckets}
* **Action : ** permet d'afficher une liste de tous les compartiments présents dans le compte IBM Cloud Object Storage d'un utilisateur. Les compartiments peuvent se trouver dans différentes régions. 
* **Syntaxe :** `ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* Notez que vous devez fournir un CRN si vous utilisez l'authentification IAM. Pour cela, vous pouvez utiliser la commande [`ibmcloud cos config crn`](#configure-the-program). 
* **Paramètres à fournir :**
  * Aucun paramètre à fournir. 
	* _Facultatif_ : permet de définir l'ID de l'instance de service IBM dans la demande. 
		* Option : `--ibm-service-instance-id`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Création d'une liste de compartiments étendue
{: #ic-extended-bucket-listing}
* **Action : ** permet d'afficher une liste de tous les compartiments présents dans le compte IBM Cloud Object Storage d'un utilisateur. Les compartiments peuvent se trouver dans différentes régions. 
* **Syntaxe :** `ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* Notez que vous devez fournir un CRN si vous utilisez l'authentification IAM. Pour cela, vous pouvez utiliser la commande [`ibmcloud cos config crn`](#configure-the-program). 
* **Paramètres à fournir :**
  * Aucun paramètre à fournir. 
	* _Facultatif_ : permet de définir l'ID de l'instance de service IBM dans la demande. 
		* Option : `--ibm-service-instance-id`
	* _Facultatif_ : permet de spécifier la clé à partir de laquelle commencer lors de la création de la liste des objets présents dans un compartiment. 
		* Option : `--marker KEY`
	* _Facultatif_ : permet de limiter la réponse aux clés débutant par le préfixe spécifié. 
		* Option : `--prefix PREFIX`
	* _Facultatif_ : taille de chaque page à obtenir dans l'appel de service. Cette valeur n'affecte pas le nombre d'éléments renvoyés dans le résultat de la commande. Si vous définissez une taille de page plus petite, un plus grand nombre d'appels est émis vers le service, avec moins d'éléments extraits dans chaque appel. Cela peut permettre d'éviter que les appels de service ne dépassent le délai d'expiration. 
		* Option : `--page-size SIZE`
	* _Facultatif_ : nombre total d'éléments qui doivent être renvoyés dans le résultat de la commande. 
		* Option : `--max-items NUMBER`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Création de la liste des envois par téléchargement en plusieurs parties qui sont en cours
{: #ic-list-multipart-uploads}
* **Action :** permet de répertorier les envois par téléchargement en plusieurs parties qui sont en cours. 
* **Syntaxe :** `ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : un délimiteur est un caractère que vous utilisez pour regrouper des clés. 
		* Option : `--delimiter DELIMITER`
	* _Facultatif_ : permet de demander le codage des clés d'objet dans la réponse et de spécifier la méthode de codage à utiliser. 
		* Option : `--encoding-type METHOD`
	* _Facultatif_ : permet de limiter la réponse aux clés débutant par le préfixe spécifié. 
		* Option : `--prefix PREFIX`
	* _Facultatif_ : utilisé conjointement avec upload-id-marker, ce paramètre permet de spécifier l'envoi par téléchargement en plusieurs parties après lequel la création de liste doit débuter. 
		* Option : `--key-marker value`
	* _Facultatif_ : utilisé conjointement avec key-marker, ce paramètre permet de spécifier l'envoi par téléchargement en plusieurs parties après lequel la création de liste doit débuter. Si key-marker n'est pas spécifié, le paramètre upload-id-marker est ignoré.
		* Option : `--upload-id-marker value`
	* _Facultatif_ : taille de chaque page à obtenir dans l'appel de service. Cette valeur n'affecte pas le nombre d'éléments renvoyés dans le résultat de la commande. Si vous définissez une taille de page plus petite, un plus grand nombre d'appels est émis vers le service, avec moins d'éléments extraits dans chaque appel. Cela peut permettre d'éviter que les appels de service ne dépassent le délai d'expiration. (Valeur par défaut : 1000). 
		* Option : `--page-size SIZE`
	* _Facultatif_ : nombre total d'éléments qui doivent être renvoyés dans le résultat de la commande. Si le nombre total d'éléments disponibles est supérieur à la valeur indiquée, un jeton NextToken est fourni dans le résultat de la commande. Pour reprendre la pagination, indiquez la valeur de NextToken dans l'argument starting-token d'une autre commande. (Valeur par défaut : 0). 
		* Option : `--max-items NUMBER`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Création d'une liste d'objets
{: #ic-list-objects}
* **Action :** permet de répertorier les fichiers présents dans un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. Cette opération est actuellement limitée aux 1 000 objets créés le plus récemment et ne peut pas être filtrée. 
* **Syntaxe :** `ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : un délimiteur est un caractère que vous utilisez pour regrouper des clés. 
		* Option : `--delimiter DELIMITER`
	* _Facultatif_ : permet de demander le codage des clés d'objet dans la réponse et de spécifier la méthode de codage à utiliser. 
		* Option : `--encoding-type METHOD`
	* _Facultatif_ : permet de limiter la réponse aux clés débutant par le préfixe spécifié. 
		* Option : `--prefix PREFIX`
	* _Facultatif_ : jeton permettant de spécifier où commencer la pagination. Il s'agit du jeton NextToken issu d'une réponse précédemment tronquée. 
		* Option : `--starting-token TOKEN`
	* _Facultatif_ : taille de chaque page à obtenir dans l'appel de service. Cette valeur n'affecte pas le nombre d'éléments renvoyés dans le résultat de la commande. Si vous définissez une taille de page plus petite, un plus grand nombre d'appels est émis vers le service, avec moins d'éléments extraits dans chaque appel. Cela peut permettre d'éviter que les appels de service ne dépassent le délai d'expiration. (Valeur par défaut : 1000). 
		* Option : `--page-size SIZE`
	* _Facultatif_ : nombre total d'éléments qui doivent être renvoyés dans le résultat de la commande. Si le nombre total d'éléments disponibles est supérieur à la valeur indiquée, un jeton NextToken est fourni dans le résultat de la commande. Pour reprendre la pagination, indiquez la valeur de NextToken dans l'argument starting-token d'une autre commande. (Valeur par défaut : 0). 
		* Option : `--max-items NUMBER`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Création d'une liste de parties
{: #ic-list-parts}
* **Action :** permet d'afficher des informations sur une instance d'envoi par téléchargement en plusieurs parties qui est en cours. 
* **Syntaxe :** `ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* ID d'envoi par téléchargement identifiant l'envoi par téléchargement en plusieurs parties. 
		* Option : `--upload-id ID`
	* Valeur de référence après laquelle la création de la liste commence (valeur par défaut : 1). 
		* Option : `--part-number-marker VALUE`
	* _Facultatif_ : taille de chaque page à obtenir dans l'appel de service. Cette valeur n'affecte pas le nombre d'éléments renvoyés dans le résultat de la commande. Si vous définissez une taille de page plus petite, un plus grand nombre d'appels est émis vers le service, avec moins d'éléments extraits dans chaque appel. Cela peut permettre d'éviter que les appels de service ne dépassent le délai d'expiration. (Valeur par défaut : 1000). 
		* Option : `--page-size SIZE`
	* _Facultatif_ : nombre total d'éléments qui doivent être renvoyés dans le résultat de la commande. Si le nombre total d'éléments disponibles est supérieur à la valeur indiquée, un jeton NextToken est fourni dans le résultat de la commande. Pour reprendre la pagination, indiquez la valeur de NextToken dans l'argument starting-token d'une autre commande. (Valeur par défaut : 0). 
		* Option : `--max-items NUMBER`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Définition d'une configuration de partage de ressources d'origine croisée pour un compartiment
{: #ic-set-bucket-cors}
* **Action :** permet de définir la configuration de partage de ressources d'origine croisée pour un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe :** `ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* _Facultatif_ : structure qui utilise une syntaxe JSON dans un fichier. 
		* Option : `--cors-configuration STRUCTURE`
		* Syntaxe JSON :  
	`--cors-configuration file://<filename.json>`  
	La commande `--cors-configuration` prend une structure JSON qui décrit les parties de l'envoi par téléchargement en plusieurs parties qui doivent être réassemblées dans le fichier complet. Dans cet exemple, le préfixe `file://` est utilisé pour charger la structure JSON à partir du fichier spécifié.
```
	{
  	"CORSRules": [
    	{
      	"AllowedHeaders": ["string", ...],
      	"AllowedMethods": ["string", ...],
      	"AllowedOrigins": ["string", ...],
      	"ExposeHeaders": ["string", ...],
      	"MaxAgeSeconds": integer
    	}
    	...
  	]
	}
	```
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`



### Insertion d'un objet
{: #ic-upload-object}
* **Action :** permet d'envoyer par téléchargement un objet sur un compartiment dans le compte IBM Cloud Object Storage d'un utilisateur. 
* **Syntaxe :** `ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Paramètres à fournir :**
    * Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* _Facultatif_ : Emplacement des données d'objet (`FILE_PATH`). 
		* Option : `--body FILE_PATH`
	* _Facultatif _ : permet de spécifier `CACHING_DIRECTIVES` pour la chaîne de demande et de réponse. 
		* Option : `--cache-control CACHING_DIRECTIVES`
	* _Facultatif_ : permet de spécifier les informations de présentation (`DIRECTIVES`). 
		* Option : `--content-disposition DIRECTIVES`
	* _Facultatif_ : permet de spécifier le codage du contenu (`CONTENT_ENCODING`) de l'objet. 
		* Option : `--content-encoding CONTENT_ENCODING`
	* _Facultatif_ : langue dans laquelle le contenu est écrit. 
		* Option : `--content-language LANGUAGE`
	* _Facultatif_ : taille du corps, exprimée en octets. Ce paramètre est utile lorsque la taille du corps ne peut pas être déterminée automatiquement. (Valeur par défaut : 0). 
		* Option : `--content-length SIZE`
	* _Facultatif_ : prétraitement MD5 128 bits codé en base 64 pour les données. 
		* Option : `--content-md5 MD5`
	* _Facultatif_ : type MIME standard qui décrit le format des données d'objet. 
		* Option : `--content-type MIME`
	* _Facultatif_ : mappe des métadonnées à stocker. Syntaxe : KeyName1=string,KeyName2=string
		* Option : `--metadata MAP`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Envoi par téléchargement d'objets à l'aide de S3Manager
{: #ic-upload-s3manager}
* **Action :** permet d'envoyer par téléchargement par téléchargement de façon simultanée des objets à partir de s3. 
* **Syntaxe :** `ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Paramètres à fournir :**
	* Nom (BUCKET_NAME) du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* Chemin d'accès au fichier à envoyer par téléchargement. 
		* Option : `--file PATH`
	* _Facultatif_ : nombre de goroutines qui doivent être lancées parallèlement lors de chaque appel pour envoi par téléchargement au moment de l'envoi de parties. La valeur par défaut est 5.
		* Option : `--concurrency value`
	* _Facultatif_ : nombre maximal de parties qui seront envoyées par téléchargement vers S3 qui calcule la taille de partie de l'objet à envoyer par téléchargement. La limite est fixée à 10 000 parties.
		* Option : `--max-upload-parts PARTS`
	* _Facultatif_ : taille de mémoire tampon (exprimée en octets) à utiliser lors de la mise en mémoire tampon de données dans des blocs et de leur envoi sous forme de parties à S3. La plus petite taille de partie autorisée est de 5 Mo.
		* Option : `--part-size SIZE`
	* _Facultatif_ : si ce paramètre a pour valeur true, le SDK évite d'appeler AbortMultipartUpload en cas d'échec, et toutes les parties ayant été envoyées par téléchargement vers S3 peuvent faire l'objet d'une récupération manuelle. 
		* Option : `--leave-parts-on-errors`
	* _Facultatif _ : permet de spécifier CACHING_DIRECTIVES pour la chaîne de demande et de réponse. 
		* Option : `--cache-control CACHING_DIRECTIVES`
	* _Facultatif_ : permet de spécifier les informations de présentation (DIRECTIVES). 
		* Option : `--content-disposition DIRECTIVES`
	* _Facultatif_ : permet de spécifier les codages de contenu (CONTENT_ENCODING) qui sont appliqués à l'objet, et par conséquent, les mécanismes de décodage qui doivent être appliqués pour obtenir le type de support référencé par la zone d'en-tête Content-Type. 
		* Option : `--content-encoding CONTENT_ENCODING`
	* _Facultatif_ : langue dans laquelle le contenu est écrit. 
		* Option : `--content-language LANGUAGE`
	* _Facultatif_ : taille du corps, exprimée en octets. Ce paramètre est utile lorsque la taille du corps ne peut pas être déterminée automatiquement. 
		* Option : `--content-length SIZE`
	* _Facultatif_ : prétraitement MD5 128 bits codé en base 64 pour les données. 
		* Option : `--content-md5 MD5`
	* _Facultatif_ : type MIME standard qui décrit le format des données d'objet. 
		* Option : `--content-type MIME`
	* _Facultatif_ : mappe des métadonnées à stocker. Syntaxe : KeyName1=string,KeyName2=string
		* Option : `--metadata MAP`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilisera l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Envoi par téléchargement d'une partie
{: #ic-upload-part}
* **Action :** permet d'envoyer par téléchargement une partie d'un fichier dans une instance d'envoi par téléchargement en plusieurs parties existante. 
* **Syntaxe :** `ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* Notez que vous devez sauvegarder le numéro et la balise ETag de chaque partie de fichier envoyée par téléchargement (que l'interface CLI imprimera pour vous) pour chaque partie dans un fichier JSON. Pour plus d'informations, voir la rubrique sur l'envoi par téléchargement en plusieurs parties. 
* **Paramètres à fournir :**
	* Nom du compartiment dans lequel s'effectue l'envoi par téléchargement en plusieurs parties. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* ID d'envoi par téléchargement identifiant l'envoi par téléchargement en plusieurs parties. 
		* Option : `--upload-id ID`
	* Référence de la partie en cours d'envoi par téléchargement. Il s'agit d'un entier positif compris entre 1 et 10 000. (Valeur par défaut : 1). 
		* Option : `--part-number NUMBER`
	* _Facultatif_ : Emplacement des données d'objet (`FILE_PATH`). 
		* Option : `--body FILE_PATH`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Envoi par téléchargement d'une copie de partie
{: #ic-upload-a-part-copy}
* **Action :** permet d'envoyer par téléchargement une partie en copiant les données d'un objet existant. 
* **Syntaxe :** `ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* Notez que vous devez sauvegarder le numéro et la balise ETag de chaque partie de fichier envoyée par téléchargement (que l'interface CLI imprimera pour vous) pour chaque partie dans un fichier JSON. Pour plus d'informations, voir la rubrique sur l'envoi par téléchargement en plusieurs parties. 
* **Paramètres à fournir :**
	* Nom du compartiment. 
		* Option : `--bucket BUCKET_NAME`
	* Clé de l'objet. 
		* Option : `--key KEY`
	* ID d'envoi par téléchargement identifiant l'envoi par téléchargement en plusieurs parties. 
		* Option : `--upload-id ID`
	* Référence de la partie en cours d'envoi par téléchargement. Il s'agit d'un entier positif compris entre 1 et 10 000. 
		* Option : `--part-number PART_NUMBER`
	* (SOURCE) Nom du compartiment source et nom de clé de l'objet source, séparés par une barre oblique (/). Il doivent être en codage URL.
		* Option : `--copy-source SOURCE`
	* _Facultatif_ : permet de copier l'objet si sa balise d'entité (Etag) correspond à la balise spécifiée (ETAG). 
		* Action : `--copy-source-if-match ETAG`
	* _Facultatif_ : permet de copier l'objet s'il a été modifié depuis l'heure indiquée (TIMESTAMP). 
		* Action : `--copy-source-if-modified-since TIMESTAMP`
	* _Facultatif_ : permet de copier l'objet si sa balise d'entité (ETag) est différente de la balise spécifiée (ETAG). 
		* Action : `--copy-source-if-none-match ETAG`
	* _Facultatif_ : permet de copier l'objet s'il n'a pas été modifié depuis l'heure indiquée (TIMESTAMP). 
		* Action : `--copy-source-if-unmodified-since TIMESTAMP`
	* _Facultatif_ : plage d'octets à copier depuis l'objet source. La valeur de la plage doit être au format bytes=fist-last, first et last étant les décalages comptés à partir de zéro. Par exemple, bytes=0-9 indique que vous souhaitez copier les dix premiers octets de la source. Vous ne pouvez copier une plage que si l'objet source fait plus de 5 Mo.
		* Option : `--copy-source-range value`
	* _Facultatif_ : région où réside le compartiment. Si cette option n'est pas fournie, le programme utilise l'option par défaut qui est spécifiée dans la configuration. 
		* Option : `--region REGION`
	* _Facultatif_ : sortie renvoyée au format JSON brut. 
		* Option : `--json`


### Commande Wait
{: #ic-wait}
* **Action :** permet d'attendre qu'une condition spécifique soit réunie. Chaque sous-commande sonde une API jusqu'à ce que l'exigence indiquée soit remplie. 
* **Syntaxe :** `ibmcloud cos wait command [arguments...] [command options]`
* **Commandes :**
    * `bucket-exists`
  		* Permet d'attendre une réponse 200 lors d'un sondage avec head-bucket. Un sondage a lieu toutes les 5 secondes jusqu'à l'obtention d'un état de réussite. La commande se termine avec un code retour 255 après 20 échecs de vérification.
	* `bucket-not-exists`
		* Permet d'attendre une réponse 404 lors d'un sondage avec head-bucket. Un sondage a lieu toutes les 5 secondes jusqu'à l'obtention d'un état de réussite. La commande se termine avec un code retour 255 après 20 échecs de vérification.
	* `object-exists`
		* Permet d'attendre une réponse 200 lors d'un sondage avec head-object. Un sondage a lieu toutes les 5 secondes jusqu'à l'obtention d'un état de réussite. La commande se termine avec un code retour 255 après 20 échecs de vérification.
	* `object-not-exists`
		* Permet d'attendre une réponse 404 lors d'un sondage avec head-object. Un sondage a lieu toutes les 5 secondes jusqu'à l'obtention d'un état de réussite. La commande se termine avec un code retour 255 après 20 échecs de vérification.

