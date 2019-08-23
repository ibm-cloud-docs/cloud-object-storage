---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: node, javascript, sdk

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

# Utilisation de Node.js
{: #node}

## Installation du SDK
{: #node-install}

La méthode recommandée pour installer le SDK {{site.data.keyword.cos_full}} pour Node.js consiste à utiliser le gestionnaire de package [`npm`](https://www.npmjs.com){:new_window} pour Node.js. Entrez la commande suivante sur une ligne de commande :

```sh
npm install ibm-cos-sdk
```

Le code source est hébergé sur [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window}. 

Pour plus d'informations sur les méthodes et les classes individuelles, voir la [documentation d'API pour le SDK](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}. 

## Initiation
{: #node-gs}

### Minimum requis
{: #node-gs-prereqs}

Pour exécuter le SDK, vous avez besoin de **Node 4.x+**.

### Création d'un client et sourçage de données d'identification
{: #node-gs-credentials}

Pour permettre l'établissement d'une connexion à COS, un client est créé et configuré en fournissant des données d'identification (clé d'API, ID d'instance de service et noeud final d'authentification IBM). Ces valeurs peuvent aussi être automatiquement sourcées à partir d'un fichier de données d'identification ou à partir de variables d'environnement. 

Après que vous avez généré des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), le document JSON résultant peut être sauvegardé dans `~/.bluemix/cos_credentials`. Le SDK source automatiquement les données d'identification de ce fichier, sauf si d'autres données d'identification sont explicitement définies lors de la création du client. Si le fichier `cos_credentials` contient des clés HMAC, le client s'authentifie à l'aide d'une signature, sinon, il utilise la clé d'API fournie pour s'authentifier à l'aide d'un jeton bearer. 

L'en-tête de section `default` spécifie un profil par défaut et les valeurs associées aux données d'identification. Vous pouvez créer davantage de profils dans le même fichier de configuration partagé, chacun avec ses propres données d'identification. L'exemple suivant présente un fichier de configuration avec le profil par défaut :
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

Si vous effectuez une migration à partir de AWS S3, vous pouvez également sourcer des données d'identification à partir de `~/.aws/credentials` au format suivant :

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

Si `~/.bluemix/cos_credentials` et `~/.aws/credentials` existent tous les deux, `cos_credentials` est prioritaire. 

## Exemples de code
{: #node-examples}

### Initialisation de la configuration
{: #node-examples-init}

```javascript
const AWS = require('ibm-cos-sdk');

var config = {
    endpoint: '<endpoint>',
    apiKeyId: '<api-key>',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: '<resource-instance-id>',
};

var cos = new AWS.S3(config);
```
*Valeurs de clé*
* `<endpoint>` - Noeud final public pour votre solution Cloud Object Storage (disponible à partir du [tableau de bord IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - Clé d'API générée lors de la création des données d'identification de service (un accès en écriture est requis pour les exemples de création et de suppression). 
* `<resource-instance-id>` - ID de ressource pour votre solution Cloud Object Storage (disponible via l'[interface CLI IBM Cloud](/docs/overview?topic=overview-crn) ou le [tableau de bord IBM Cloud](https://cloud.ibm.com/resources){:new_window}). 

### Création d'un compartiment
{: #node-examples-new-bucket}

Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```javascript
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: 'us-standard'
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```


*Références SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### Création d'un objet de texte
{: #node-examples-new-file}

```javascript
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName, 
        Key: itemName, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### Création d'une liste de compartiments
{: #node-examples-list-buckets}

```javascript
function getBuckets() {
    console.log('Retrieving list of buckets');
    return cos.listBuckets()
    .promise()
    .then((data) => {
        if (data.Buckets != null) {
            for (var i = 0; i < data.Buckets.length; i++) {
                console.log(`Bucket Name: ${data.Buckets[i].Name}`);
            }
        }
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### Création de la liste des éléments contenus dans un compartiment
{: #node-examples-list-objects}

```javascript
function getBucketContents(bucketName) {
    console.log(`Retrieving bucket contents from: ${bucketName}`);
    return cos.listObjects(
        {Bucket: bucketName},
    ).promise()
    .then((data) => {
        if (data != null && data.Contents != null) {
            for (var i = 0; i < data.Contents.length; i++) {
                var itemKey = data.Contents[i].Key;
                var itemSize = data.Contents[i].Size;
                console.log(`Item: ${itemKey} (${itemSize} bytes).`)
            }
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### Obtention du contenu de fichier d'un élément particulier
{: #node-examples-get-contents}

```javascript
function getItem(bucketName, itemName) {
    console.log(`Retrieving item from bucket: ${bucketName}, key: ${itemName}`);
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log('File Contents: ' + Buffer.from(data.Body).toString());
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### Suppression d'un élément d'un compartiment
{: #node-examples-delete-object}

```javascript
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*Références SDK*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### Suppression de plusieurs éléments d'un compartiment
{: #node-examples-multidelete}

La demande de suppression peut contenir un maximum de 1000 clés. Bien que la suppression d'objets par lots soit très utile pour réduire le temps système pour chaque demande, n'oubliez pas que lorsque vous supprimez un grand nombre de clés, l'exécution de la demande peut durer un certain temps. Tenez compte également de la taille des objets de manière à obtenir de bonnes performances. {:tip}

```javascript
function deleteItems(bucketName) {
    var deleteRequest = {
        "Objects": [
            { "Key": "deletetest/testfile1.txt" },
            { "Key": "deletetest/testfile2.txt" },
            { "Key": "deletetest/testfile3.txt" },
            { "Key": "deletetest/testfile4.txt" },
            { "Key": "deletetest/testfile5.txt" }
        ]        
    }
    return cos.deleteObjects({
        Bucket: bucketName,
        Delete: deleteRequest
    }).promise()
    .then((data) => {
        console.log(`Deleted items for ${bucketName}`);
        console.log(data.Deleted);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });    
}
```

*Références SDK*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### Suppression d'un compartiment
{: #node-examples-delete-bucket}

```javascript
function deleteBucket(bucketName) {
    console.log(`Deleting bucket: ${bucketName}`);
    return cos.deleteBucket({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucketName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### Exécution d'un envoi par téléchargement en plusieurs parties
{: #node-examples-multipart}

```javascript
function multiPartUpload(bucketName, itemName, filePath) {
    var uploadID = null;

    if (!fs.existsSync(filePath)) {
        log.error(new Error(`The file \'${filePath}\' does not exist or is not accessible.`));
        return;
    }

    console.log(`Starting multi-part upload for ${itemName} to bucket: ${bucketName}`);
    return cos.createMultipartUpload({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        uploadID = data.UploadId;

        //begin the file upload        
        fs.readFile(filePath, (e, fileData) => {
            //min 5MB part
            var partSize = 1024 * 1024 * 5;
            var partCount = Math.ceil(fileData.length / partSize);
    
            async.timesSeries(partCount, (partNum, next) => {
                var start = partNum * partSize;
                var end = Math.min(start + partSize, fileData.length);
    
                partNum++;

                console.log(`Uploading to ${itemName} (part ${partNum} of ${partCount})`);  

                cos.uploadPart({
                    Body: fileData.slice(start, end),
                    Bucket: bucketName,
                    Key: itemName,
                    PartNumber: partNum,
                    UploadId: uploadID
                }).promise()
                .then((data) => {
                    next(e, {ETag: data.ETag, PartNumber: partNum});
                })
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            }, (e, dataPacks) => {
                cos.completeMultipartUpload({
                    Bucket: bucketName,
                    Key: itemName,
                    MultipartUpload: {
                        Parts: dataPacks
                    },
                    UploadId: uploadID
                }).promise()
                .then(console.log(`Upload of all ${partCount} parts of ${itemName} successful.`))
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            });
        });
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function cancelMultiPartUpload(bucketName, itemName, uploadID) {
    return cos.abortMultipartUpload({
        Bucket: bucketName,
        Key: itemName,
        UploadId: uploadID
    }).promise()
    .then(() => {
        console.log(`Multi-part upload aborted for ${itemName}`);
    })
    .catch((e)=>{
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Utilisation de Key Protect
{: #node-examples-kp}

Key Protect peut être ajouté à un compartiment de stockage pour gérer les clés de chiffrement. Toutes les données sont chiffrées dans IBM COS, mais Key Protect fournit des fonctions de génération, de rotation et de contrôle de l'accès aux clés de chiffrement à l'aide d'un service centralisé. 

### Avant de commencer
{: #node-examples-kp-prereqs}
Les éléments suivants sont nécessaires pour créer un compartiment avec Key Protect activé :

* Un service Key Protect doit être [mis à disposition](/docs/services/key-protect?topic=key-protect-provision#provision). 
* Une clé racine doit être disponible ([générée](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importée](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)). 

### Extraction du CRN de clé racine
{: #node-examples-kp-root}

1. Extrayez l'[ID d'instance](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) pour votre service Key Protect. 
2. Utilisez l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) pour extraire toutes vos [clés disponibles](https://cloud.ibm.com/apidocs/key-protect). 
    * Vous pouvez utiliser des commandes `curl` ou un client REST API, tel que [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), pour accéder à l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api). 
3. Extrayez le CRN de la clé racine que vous utiliserez pour activer Key Protect sur votre compartiment. Le CRN se présentera comme suit :

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Création d'un compartiment avec Key Protect activé
{: #node-examples-kp-new-bucket}

```javascript
function createBucketKP(bucketName) {
    console.log(`Creating new encrypted bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: '<bucket-location>'
        },
        IBMSSEKPEncryptionAlgorithm: '<algorithm>',
        IBMSSEKPCustomerRootKeyCrn: '<root-key-crn>'
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
        logDone();
    }))
    .catch(logError);
}
```
*Valeurs de clé*
* `<bucket-location>` - Région ou emplacement de votre compartiment (Key Protect n'est disponible que dans certaines régions. Assurez-vous que votre emplacement correspond au service Key Protect). Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes). 
* `<algorithm>` - Algorithme de chiffrement pour les nouveaux objets ajoutés au compartiment (la valeur par défaut est AES256). 
* `<root-key-crn>` - CRN de la clé racine qui est obtenue auprès du service Key Protect. 

*Références SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## Utilisation de la fonction d'archivage
{: #node-examples-archive}

Le niveau d'archivage permet aux utilisateurs d'archiver des données périmées et de réduire leurs coûts de stockage. Des règles d'archivage (également appelées *configurations de cycle de vie*) sont créées pour les compartiments et s'appliquent à n'importe quel objet ajouté au compartiment après la création de la règle. 

### Affichage d'une configuration de cycle de vie de compartiment
{: #node-examples-get-lifecycle}

```javascript
function getLifecycleConfiguration(bucketName) {
    return cos.getBucketLifecycleConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log(`Retrieving bucket lifecycle config from: ${bucketName}`);
            console.log(JSON.stringify(data, null, 4));
        }
        else {
            console.log(`No lifecycle configuration for ${bucketName}`);
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Création d'une configuration de cycle de vie 
{: #node-examples-put-lifecycle}

Pour plus d'informations sur la structuration des règles de configuration de cycle de vie, voir la rubrique [Référence d'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). 

```javascript
function createLifecycleConfiguration(bucketName) {
    //
    var config = {
        Rules: [{
            Status: 'Enabled', 
            ID: '<policy-id>',
            Filter: {
                Prefix: ''
            },
            Transitions: [{
                Days: <number-of-days>, 
                StorageClass: 'GLACIER'
            }]
        }]
    };
    
    return cos.putBucketLifecycleConfiguration({
        Bucket: bucketName,
        LifecycleConfiguration: config
    }).promise()
    .then(() => {
        console.log(`Created bucket lifecycle config for: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Valeurs de clé*
* `<policy-id>` - Nom de la règle de cycle de vie (doit être unique). 
* `<number-of-days>` - Nombre de jours pendant lesquels conserver le fichier restauré. 

*Références SDK*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Suppression d'une configuration de cycle de vie de compartiment
{: #node-examples-delete-lifecycle}

```javascript
function deleteLifecycleConfiguration(bucketName) {
    return cos.deleteBucketLifecycle({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Deleted bucket lifecycle config from: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Restauration temporaire d'un objet
{: #node-examples-restore-object}

Pour plus d'informations sur les paramètres de demande de restauration, voir la rubrique [Référence d'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore). 

```javascript
function restoreItem(bucketName, itemName) {
    var params = {
        Bucket: bucketName, 
        Key: itemName, 
        RestoreRequest: {
            Days: <number-of-days>, 
            GlacierJobParameters: {
                Tier: 'Bulk' 
            },
        } 
    };
    
    return cos.restoreObject(params).promise()
    .then(() => {
        console.log(`Restoring item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Valeurs de clé*
* `<number-of-days>` - Nombre de jours pendant lesquels conserver le fichier restauré. 

*Références SDK*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Affichage des informations HEAD pour un objet
{: #node-examples-lifecycle-head-object}
```javascript
function getHEADItem(bucketName, itemName) {
    return cos.headObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        console.log(`Retrieving HEAD for item: ${itemName} from bucket: ${bucketName}`);
        console.log(JSON.stringify(data, null, 4));
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Références SDK*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## Mise à jour de métadonnées
{: #node-examples-metadata}

Il existe deux manières de mettre à jour les métadonnées sur un objet existant :
* En exécutant une demande `PUT` avec les nouvelles métadonnées et le contenu de l'objet d'origine 
* En exécutant une demande `COPY` avec les nouvelles métadonnées en spécifiant l'objet d'origine comme source de la copie 

### Utilisation de PUT pour mettre à jour les métadonnées
{: #node-examples-metadata-put}

**Remarque :** la demande `PUT` remplace le contenu existant de l'objet, par conséquent, le contenu doit d'abord être reçu par téléchargement, puis à nouveau envoyé par téléchargement avec les nouvelles métadonnées. 


```javascript
function updateMetadataPut(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //retrieve the existing item to reload the contents
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        //set the new metadata
        var newMetadata = {
            newkey: metaValue
        };

        return cos.putObject({
            Bucket: bucketName,
            Key: itemName,
            Body: data.Body,
            Metadata: newMetadata
        }).promise()
        .then(() => {
            console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
        })
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

### Utilisation de COPY pour mettre à jour les métadonnées
{: #node-examples-metadata-copy}

```javascript
function updateMetadataCopy(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //set the copy source to itself
    var copySource = bucketName + '/' + itemName;

    //set the new metadata
    var newMetadata = {
        newkey: metaValue
    };

    return cos.copyObject({
        Bucket: bucketName, 
        Key: itemName,
        CopySource: copySource,
        Metadata: newMetadata,
        MetadataDirective: 'REPLACE'
    }).promise()
    .then((data) => {
        console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
        })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

## Utilisation de la fonction Immutable Object Storage
{: #node-examples-immutable}

### Ajout d'une configuration de protection à un compartiment existant
{: #node-examples-immutable-add}

Les objets écrits dans un compartiment protégé ne peuvent pas être supprimés tant que la période de protection n'est pas arrivée à expiration et que les conservations légales associées à l'objet n'ont pas toutes été retirées. La valeur de conservation par défaut du compartiment est attribuée à un objet, sauf si une valeur spécifique à un objet est fournie lors de la création de l'objet. Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet.  

Les valeurs minimales et maximales prises en charge pour les paramètres de durée de conservation `MinimumRetention`, `DefaultRetention` et `MaximumRetention` sont respectivement de 0 jours et 365243 jours (1000 ans).  


```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Vérification de la protection d'un compartiment
{: #node-examples-immutable-check}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Envoi par téléchargement d'un objet protégé
{: #node-examples-immutable-upload}

Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 

|Valeur	| Type	| Description |
| --- | --- | --- | 
|`Retention-Period` | Entier non négatif (secondes) | Durée de conservation, exprimée en secondes, pendant laquelle stocker l'objet. L'objet ne peut être ni écrasé, ni supprimé tant que la durée de conservation n'est pas écoulée. Si cette zone et la zone `Retention-Expiration-Date` sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la période `DefaultRetention` du compartiment est utilisée. Zéro (`0`) est une valeur légale qui part du principe que la durée de conservation minimale du compartiment est également fixée à `0`. |
| `Retention-expiration-date` | Date (format ISO 8601) | Date à laquelle la suppression ou la modification de l'objet devient légale. Vous pouvez uniquement spécifier cette zone ou bien la zone d'en-tête Retention-Period. Si ces deux zones sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la durée de conservation par défaut du compartiment est utilisée. |
| `Retention-legal-hold-id` |Chaîne| Conservation légale unique à appliquer à l'objet. Une conservation légale est une chaîne comportant Y caractères. L'objet ne peut être ni écrasé ni supprimé tant que les conservations légales associées à l'objet n'ont pas toutes été retirées. |

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Ajout ou retrait d'une conservation légale dans un objet protégé
{: #node-examples-immutable-legal-hold}

L'objet peut prendre en charge 100 conservations légales :

*  Un identificateur de conservation légale est une chaîne comprise entre 1 et 64 caractères. Les caractères valides sont des lettres, des chiffres, `!`, `_`, `.`, `*`, `(`, `)`, `-` et `.
* Si la tentative d'ajout de la conservation légale spécifiée dépasse le seuil de 100 conservations légales affectées à l'objet, l'opération d'ajout échoue et un code d'erreur `400` est renvoyé. 
* Si un identificateur est trop long, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur contient des caractères non valides, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur est déjà en cours d'utilisation sur un objet, la conservation légale existante n'est pas modifiée et la réponse indique que l'identificateur était déjà utilisé et renvoie un code d'erreur `409`. 
* Si un objet ne comporte pas de métadonnées de durée de conservation, un code d'erreur `400` est renvoyé et l'ajout ou le retrait d'une conservation légale ne sont pas autorisés. 

L'utilisateur qui effectue l'ajout ou la suppression d'une conservation légale doit disposer des droits d'accès `Manager` pour ce compartiment. 

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Prolongement de la durée de conservation d'un objet protégé
{: #node-examples-immutable-extend}

La durée de conservation d'un objet ne peut être que prolongée. La valeur actuellement configurée ne peut pas être diminuée. 

La valeur de prolongement de la conservation est définie de l'une des trois façons suivantes :

* En ajoutant une durée supplémentaire par rapport à la valeur en cours (`Additional-Retention-Period` ou méthode similaire) 
* En définissant une nouvelle période de prolongement, exprimée en secondes (`Extend-Retention-From-Current-Time` ou méthode similaire) 
* En définissant une nouvelle date d'expiration de la conservation de l'objet (`New-Retention-Expiration-Date` ou méthode similaire) 

La durée de conservation en cours stockée dans les métadonnées de l'objet est soit augmentée de la durée supplémentaire indiquée, soit remplacée par la nouvelle valeur, en fonction du paramètre défini dans la demande `extendRetention`. Dans tous les cas, le paramètre de prolongement de la conservation est vérifié par rapport à la durée de conservation en cours et le paramètre de prolongement n'est accepté que si la durée de conservation mise à jour est supérieure à la durée de conservation en cours. 

Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Création de la liste des conservations légales associées à un objet protégé
{: #node-examples-immutable-list-holds}

Cette opération renvoie les éléments suivants :

* La date de création de l'objet
* La durée de conservation, exprimée en secondes
* La date d'expiration de la conservation, calculée sur la base de la durée de conservation et de la date de création
* La liste des conservations légales
* L'identificateur de conservation légale
* L'horodatage correspondant à l'application de la conservation légale

Si aucune conservation légale n'est associée à l'objet, un élément `LegalHoldSet` vide est renvoyé.
Si aucune durée de conservation n'est spécifiée pour l'objet, un code d'erreur `404` est renvoyé. 


```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}
