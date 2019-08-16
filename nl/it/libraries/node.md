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

# Utilizzo di Node.js
{: #node}

## Installazione dell'SDK
{: #node-install}

Il modo preferito per installare {{site.data.keyword.cos_full}} SDK for Node.js è di utilizzare il gestore del pacchetto
[`npm`](https://www.npmjs.com){:new_window} per Node.js. Immetti il seguente comando
in una riga di comando:

```sh
npm install ibm-cos-sdk
```

Il codice di origine è ospitato su [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window}.

Ulteriori dettagli sui metodi e le classi individuali possono essere trovati in [nella documentazione API per l'SDK](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}.

## Introduzione
{: #node-gs}

### Requisiti minimi 
{: #node-gs-prereqs}

Per eseguire l'SDK, hai bisogno di **Node 4.x+**.

### Creazione di un client e derivazione delle credenziali 
{: #node-gs-credentials}

Per la connessione a COS, viene creato e configurato un client fornendo le informazioni sulle credenziali (chiave API, ID istanza del servizio ed endpoint di autenticazione IBM). Questi valori possono anche essere derivati automaticamente da un file di credenziali o dalle variabili di ambiente. 

Dopo aver generato una [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), il documento JSON risultante può essere salvato in `~/.bluemix/cos_credentials`. L'SDK deriverà automaticamente le credenziali da questo file a meno che non vengano esplicitamente impostate altre credenziali durante la creazione del client. Se il file `cos_credentials` contiene le chiavi HMAC, il client esegue l'autenticazione con una firma, altrimenti utilizza la chiave API fornita con un token di connessione.

L'intestazione della sezione `default` specifica un profilo predefinito e i valori associati alle credenziali. Puoi creare più profili nello stesso file di configurazione, ognuno con le proprie informazioni sulle credenziali. Il seguente esempio mostra un file di configurazione con il profilo predefinito:
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

Se si esegue la migrazione da AWS S3, puoi anche derivare i dati delle credenziali da `~/.aws/credentials` nel formato: 

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

Se esistono `~/.bluemix/cos_credentials` e `~/.aws/credentials`, `cos_credentials` ha la precedenza.

## Esempi di codici
{: #node-examples}

### Inizializzazione della configurazione
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
*Valori chiave*
* `<endpoint>` - endpoint pubblico per la tua archiviazione oggetti cloud (disponibile dal [dashboard IBM Cloud](https://cloud.ibm.com/resources){:new_window}). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). 
* `<api-key>` - chiave API generata quando si creano le credenziali del servizio (l'accesso in scrittura è necessario per gli esempi di creazione ed eliminazione). 
* `<resource-instance-id>` - ID risorsa per la tua archiviazione oggetti cloud (disponibile tramite la [CLI IBM Cloud](/docs/overview?topic=overview-crn) o il [dashboard IBM Cloud](https://cloud.ibm.com/resources){:new_window}) 

### Creazione di un bucket
{: #node-examples-new-bucket}

È possibile che si faccia riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida alle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes). 

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


*Riferimenti SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### Creazione di un oggetto di testo
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

*Riferimenti SDK*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### Elenca i bucket
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

*Riferimenti SDK*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### Elenca gli elementi in un bucket 
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

*Riferimenti SDK*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### Ottieni il contenuto del file di uno specifico elemento 
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

*Riferimenti SDK*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### Elimina un elemento da un bucket 
{: #node-examples-delete-object}

```javascript
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() =>{
        console.log(`Item: ${itemName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*Riferimenti SDK*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### Elimina più elementi da un bucket 
{: #node-examples-multidelete}

La richiesta di eliminazione può contenere un massimo di 1000 chiavi che vuoi eliminare. Se da una parte eliminare gli oggetti in batch è molto utile per ridurre il sovraccarico per ogni richiesta, fai attenzione che quando elimini molte chiavi, la richiesta potrebbe richiedere alcuni minuti per il completamento. Inoltre, prendi in considerazione anche le dimensioni degli oggetti per garantire delle prestazioni adeguate.
{:tip}

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

*Riferimenti SDK*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### Elimina un bucket
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

*Riferimenti SDK*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### Esegui un caricamento in più parti 
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

*Riferimenti SDK*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Utilizzo di Key Protect
{: #node-examples-kp}

Key Protect può essere aggiunto a un bucket di archiviazione per gestire le chiavi di crittografia. Tutti i dati vengono crittografati in IBM COS ma Key Protect fornisce un servizio per generare, ruotare e controllare l'accesso alle chiavi di crittografia utilizzando un servizio centralizzato. 

### Prima di cominciare 
{: #node-examples-kp-prereqs}
I seguenti elementi sono necessari per creare un bucket con Key-Protect abilitato: 

* Un servizio Key Protect [di cui è stato eseguito il provisioning](/docs/services/key-protect?topic=key-protect-provision#provision) 
* Una chiave root disponibile ([generata](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importata](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)) 

### Richiamo del CRN di chiave root 
{: #node-examples-kp-root}

1. Richiama l'[ID istanza](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) per il tuo servizio Key Protect 
2. Utilizza l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) per richiamare tutte le tue [chiavi disponibili](https://cloud.ibm.com/apidocs/key-protect) 
    * Puoi utilizzare i comandi `curl` o un client REST API come [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) per accedere all'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api). 
3. Richiama il CRN della chiave root che utilizzerai per abilitare Key Protect sul tuo bucket. Il CRN sarà simile al seguente: 

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creazione di un bucket con Key Protect abilitato 
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
*Valori chiave*
* `<bucket-location>` - Regione o ubicazione del tuo bucket (Key Protect è disponibile solo in alcune regioni. Assicurati che la tua ubicazione corrisponda al servizio Key Protect). È possibile che si faccia riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida alle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).
* `<algorithm>` - l'algoritmo di crittografia utilizzato per i nuovi oggetti aggiunti al bucket (il valore predefinito è AES256). 
* `<root-key-crn>` - CRN della chiave root ottenuta dal servizio Key Protect. 

*Riferimenti SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## Utilizzo della funzione di archiviazione
{: #node-examples-archive}

Il livello di archiviazione consente agli utenti di archiviare i dati obsoleti e ridurre i loro costi di archiviazione. Le politiche di archiviazione (note anche come *Configurazioni del ciclo di vita*) sono create per i bucket e si applicano a tutti gli oggetti aggiunti al bucket dopo la creazione della politica.

### Visualizza la configurazione del ciclo di vita di un bucket 
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

*Riferimenti SDK*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Crea una configurazione del ciclo di vita  
{: #node-examples-put-lifecycle}

Informazioni dettagliate su come strutturare le regole della configurazione del ciclo di vita sono disponibili nella [Guida di riferimento API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)

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

*Valori chiave*
* `<policy-id>` - Nome della politica del ciclo di vita (deve essere univoco)
* `<number-of-days>` - Numero di giorni di conservazione del file ripristinato 

*Riferimenti SDK*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Elimina la configurazione del ciclo di vita di un bucket 
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

*Riferimenti SDK*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Ripristina temporaneamente un oggetto
{: #node-examples-restore-object}

Informazioni dettagliate sul ripristino dei parametri della richiesta sono disponibili nella [Guida di riferimento API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore)

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

*Valori chiave*
* `<number-of-days>` - Numero di giorni di conservazione del file ripristinato 

*Riferimenti SDK*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Visualizza le informazioni HEAD per un oggetto
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

*Riferimenti SDK*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## Aggiornamento dei metadati 
{: #node-examples-metadata}

Esistono due modi per aggiornare i metadati in un oggetto esistente: 
* Una richiesta `PUT` con i nuovi metadati e il contenuto dell'oggetto originale 
* L'esecuzione di una richiesta `COPY` con i nuovi metadati che specifica l'oggetto originale come origine della copia 

### Utilizzo di PUT per aggiornare i metadati 
{: #node-examples-metadata-put}

**Nota:** la richiesta `PUT` sovrascrive il contenuto esistente dell'oggetto e, pertanto, deve essere prima scaricato e ricaricato con i nuovi metadati 


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

### Utilizzo di COPY per aggiornare i metadati 
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

## Utilizzo di Immutable Object Storage 
{: #node-examples-immutable}

### Aggiunta di una configurazione di protezione ad un bucket esistente 
{: #node-examples-immutable-add}

Gli oggetti scritti in un bucket protetto non possono essere eliminati fino a quando il periodo di protezione non è scaduto e tutte le conservazioni a fini legali sull'oggetto non sono stati rimosse. Il valore di conservazione predefinito del bucket viene dato ad un oggetto a meno che non venga fornito un valore specifico dell'oggetto quando l'oggetto viene creato. Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket.  

I valori supportati minimo e massimo per le impostazioni del periodo di conservazione `MinimumRetention`, `DefaultRetention` e `MaximumRetention` sono, rispettivamente, 0 giorni e 365243 giorni (1000 anni).  


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

### Controlla la protezione su un bucket 
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

### Carica un oggetto protetto 
{: #node-examples-immutable-upload}

Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket. 

|Valore	| Tipo	|Descrizione|
| --- | --- | --- | 
|`Retention-Period` |Numero intero non negativo (secondi) |Il periodo di conservazione da memorizzare sull'oggetto, in secondi. L'oggetto non può essere sovrascritto o eliminato finché l'intervallo di tempo specificato nel periodo di conservazione non è trascorso. Se vengono specificati questi campo e `Retention-Expiration-Date`, viene restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo `DefaultRetention` del bucket. Zero (`0`) è un valore consentito, presumendo che il periodo di conservazione minimo del bucket sia anch'esso `0`. |
| `Retention-expiration-date` |Data (formato ISO 8601) |Data in cui sarà consentito eliminare o modificare l'oggetto. Puoi specificare solo questo valore oppure l'intestazione Retention-Period. Se vengono specificati entrambi, verrà restituito un errore `400`. Se non viene specificato nessuno di questi due valori, verrà utilizzato il periodo DefaultRetention del bucket. |
| `Retention-legal-hold-id` | stringa |Una singola conservazione a fini legali da applicare all'oggetto. Una conservazione a fini legali è una stringa di caratteri di lunghezza Y. L'oggetto non può essere sovrascritto o eliminato finché non sono state rimosse tutte le conservazioni a fini legali a esso associate. |

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

### Aggiungi/rimuovi una conservazione a fini legali a/da un oggetto protetto 
{: #node-examples-immutable-legal-hold}

L'oggetto può supportare 100 conservazioni a fini legali: 

*  Un identificativo di conservazione a fini locali è una stringa di una lunghezza massima di 64 caratteri e una lunghezza minima di 1 carattere. I caratteri validi sono lettere, numeri, `!`, `_`, `.`, `*`, `(`, `)`, `-` e `. 
* Se l'aggiunta di una specifica conservazione a fini legali comporta il superamento di un totale di 100 conservazioni a fini legali sull'oggetto, la nuova conservazione a fini legali non viene aggiunta e viene restituito un errore `400`. 
* Se un identificativo è troppo lungo, non viene aggiunto all'oggetto e viene restituito un errore `400`. 
* Se un identificativo contiene caratteri non validi, non viene aggiunto all'oggetto e viene restituito un errore `400`. 
* Se un identificativo è già in uso su un oggetto, la conservazione a fini legali esistente non viene modificata e la risposta indica che l'identificativo era già in uso con un errore `409`. 
* Se un oggetto non ha metadati del periodo di conservazione, viene restituito un errore `400` e l'aggiunta o la rimozione di una conservazione a fini legali non è consentita. 

L'utente che esegue l'aggiunta o la rimozione di una conservazione a fini legali deve disporre delle autorizzazioni di `Manager` (gestore) per questo bucket. 

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

### Estendi il periodo di conservazione di un oggetto protetto 
{: #node-examples-immutable-extend}

Il periodo di conservazione di un oggetto può solo essere esteso. Non può essere ridotto rispetto al valore attualmente configurato. 

Il valore di espansione della conservazione è impostato in uno di tre possibili modi: 

* ulteriore tempo dal valore corrente (`Additional-Retention-Period` o metodo simile) 
* nuovo periodo di estensione in secondi (`Extend-Retention-From-Current-Time` o metodo simile) 
* nuova data di scadenza della conservazione dell'oggetto (`New-Retention-Expiration-Date` o metodo simile) 

Il periodo di conservazione corrente memorizzato nei metadati dell'oggetto viene aumentato in misura equivalente al tempo aggiuntivo indicato oppure sostituito con il nuovo valore, a seconda del parametro impostato nella richiesta `extendRetention`. In tutti i casi, il parametro di estensione della conservazione viene controllato rispetto al periodo di conservazione corrente e il parametro esteso viene accettato solo se il periodo di conservazione aggiornato è più grande del periodo di conservazione corrente. 

Gli oggetti nei bucket protetti che non sono più oggetto di conservazione (il periodo di conservazione è scaduto e l'oggetto non ha alcuna conservazione a fini legali), quando vengono sovrascritti diventano di nuovo oggetto di conservazione. Il nuovo periodo di conservazione può essere fornito come parte della richiesta di sovrascrittura dell'oggetto, altrimenti all'oggetto verrà assegnato il tempo di conservazione predefinito del bucket. 

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

### Elenca le conservazioni a fini legali su un oggetto protetto 
{: #node-examples-immutable-list-holds}

Questa operazione restituisce: 

* La data di creazione dell'oggetto 
* Il periodo di conservazione dell'oggetto in secondi 
* Data di scadenza della conservazione calcolata sulla base del periodo e della data di creazione 
* Elenco delle conservazioni a fini legali 
* Identificativo della conservazione a fini legali 
* Data/ora di quando è stata applicata la conservazione a fini legali 

Se non ci sono conservazioni a fini legali sull'oggetto, viene restituito un `LegalHoldSet` vuoto.
Se non c'è alcun periodo di conservazione specificato sull'oggetto, viene restituito un errore `404`. 


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
