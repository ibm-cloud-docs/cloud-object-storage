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

# Node.js verwenden
{: #node}

## SDK installieren
{: #node-install}

Die bevorzugte Methode zum Installieren des {{site.data.keyword.cos_full}}-SDK für Node.js ist die Verwendung des Paketmanagers [`npm`](https://www.npmjs.com){:new_window} für Node.js. Geben Sie den folgenden Befehl in eine Befehlszeile ein:

```sh
npm install ibm-cos-sdk
```

Der Quellcode wird bei [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window} gehostet.

Weitere Details zu einzelnen Methoden und Klassen finden Sie in der [API-Dokumentation für das SDK](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}.

## Einführung
{: #node-gs}

### Mindestvoraussetzungen
{: #node-gs-prereqs}

Zum Ausführen des SDKs benötigen Sie **Node 4.x+**.

### Client erstellen und Berechtigungsnachweise ableiten
{: #node-gs-credentials}

Zum Herstellen einer Verbindung zu COS wird ein Client erstellt und durch Bereitstellen der Berechtigungsnachweisinformationen (API-Schlüssel, Serviceinstanz-ID und IBM Authentifizierungsendpunkt) konfiguriert. Als Quelle für diese Werte können auch automatisch eine Berechtigungsnachweisdatei oder Umgebungsvariablen verwendet werden.

Nach dem Generieren eines [Serviceberechtigungsnachweises](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) kann das daraus resultierende JSON-Dokument unter `~/.bluemix/cos_credentials` gespeichert werden. Vom SDK wird diese Datei automatisch als Quelle für die Berechtigungsnachweise verwendet, sofern während der Clienterstellung nicht explizit andere Berechtigungsnachweise festgelegt werden. Wenn die Datei `cos_credentials` HMAC-Schlüssel enthält, wird die Authentifizierung vom Client mithilfe einer Signatur durchgeführt; andernfalls wird vom Client für die Authentifizierung der bereitgestellte API-Schlüssel mit einem Bearer-Token verwendet.

In der Abschnittsüberschrift `default` werden ein Standardprofil und zugeordnete Werte für Berechtigungsnachweise angegeben. Sie können in derselben gemeinsam genutzten Konfigurationsdatei weitere Profile erstellen und für jedes eigene Berechtigungsnachweisdaten angeben. Im folgenden Beispiel wird eine Konfigurationsdatei mit dem Standardprofil dargestellt.
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

Falls Sie eine Migration von AWS S3 durchführen, können Sie die Daten für die Berechtigungsnachweise aus `~/.aws/credentials` im folgenden Format verwenden:

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

Wenn sowohl `~/.bluemix/cos_credentials` als auch `~/.aws/credentials` vorhanden ist, genießt `cos_credentials` Vorrang.

## Codebeispiele
{: #node-examples}

### Konfiguration initialisieren
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
*Schlüsselwerte*
* `<endpoint>` - Öffentlicher Endpunkt für Cloud Object Storage (verfügbar über das [IBM Cloud-Dashboard](https://cloud.ibm.com/resources){:new_window}). Weitere Informationen zu Endpunkten finden Sie unter [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - API-Schlüssel, der beim Erstellen der Serviceberechtigungsnachweise generiert wird (Schreibzugriff für Erstellungs- und Löschbeispiele erforderlich)
* `<resource-instance-id>` - Ressourcen-ID für Cloud Object Storage (verfügbar über [IBM Cloud-Befehlszeilenschnittstelle](/docs/overview?topic=overview-crn) oder [IBM Cloud-Dashboard](https://cloud.ibm.com/resources){:new_window})

### Bucket erstellen
{: #node-examples-new-bucket}

Auf eine Liste gültiger Bereitstellungscodes für `LocationConstraint` kann im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes) verwiesen werden.

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


*SDK-Referenzen*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### Textobjekt erstellen
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

*SDK-Referenzen*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### Buckets auflisten
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

*SDK-Referenzen*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### Elemente in Bucket auflisten
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

*SDK-Referenzen*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### Dateiinhalt eines bestimmten Elements abrufen
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

*SDK-Referenzen*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### Element in Bucket löschen
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
*SDK-Referenzen*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### Mehrere Elemente in Bucket löschen
{: #node-examples-multidelete}

Eine Löschanforderung kann maximal 1000 Schlüssel enthalten, die gelöscht werden sollen. Das Löschen von Objekten in Stapeln ist zwar sehr nützlich, um den Aufwand pro Anforderung zu reduzieren, gehen Sie beim Löschen einer großen Anzahl an Schlüsseln jedoch mit Sorgfalt vor, weil die Ausführung der Anforderung einige Zeit in Anspruch nehmen kann. Berücksichtigen Sie auch die Größen der Objekte, um eine geeignete Leistung zu gewährleisten.{:tip}

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

*SDK-Referenzen*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### Bucket löschen
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

*SDK-Referenzen*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### Mehrteiliges Hochladen ausführen
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

        // Dateiupload starten
        fs.readFile(filePath, (e, fileData) => {
            // Minimum 5 MB pro Teil
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

*SDK-Referenzen*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Key Protect verwenden
{: #node-examples-kp}

Key Protect kann zu einem Speicherbucket zum Verwalten von Verschlüsselungsschlüsseln hinzugefügt werden. In IBM COS werden alle Daten verschlüsselt, aber von Key Protect wird ein Service zum Generieren, Rotieren und Steuern des Zugriffs auf Verschlüsselungsschlüssel unter Verwendung eines zentralen Service bereitgestellt.

### Vorbereitende Schritte
{: #node-examples-kp-prereqs}
Damit Sie ein Bucket erstellen können, während Key Protect aktiviert ist, müssen die folgenden Voraussetzungen erfüllt sein:

* Ein Key Protect-Service wird [bereitgestellt](/docs/services/key-protect?topic=key-protect-provision#provision)
* Ein Stammschlüssel ist verfügbar (entweder [generiert](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) oder [importiert](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Cloudressourcenname für Stammschlüssel abrufen
{: #node-examples-kp-root}

1. Rufen Sie die [Instanz-ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) für den Key Protect-Service ab.
2. Verwenden Sie die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api), um alle [verfügbaren Schlüssel](https://cloud.ibm.com/apidocs/key-protect) abzurufen.
    * Sie können entweder `curl`-Befehle oder einen API-REST-Client wie zum Beispiel [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) für den Zugriff auf die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) verwenden.
3. Rufen Sie den Cloudressourcennamen des Stammschlüssels ab, mit dem Sie Key Protect für das Bucket aktivieren. Der Cloudressourcenname ähnelt der folgenden Zeichenfolge:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Bucket während Aktivierung von Key Protect erstellen
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
*Schlüsselwerte*
* `<bucket-location>` - Region oder Standort für das Bucket (Key Protect ist nur in bestimmten Regionen verfügbar. Stellen Sie sicher, dass Ihr Standort mit dem Key Protect-Service übereinstimmt). Auf eine Liste gültiger Bereitstellungscodes für `LocationConstraint` kann im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes) verwiesen werden.
* `<algorithm>` - Der Verschlüsselungsalgorithmus, der für neue Objekte verwendet wird, die zum Bucket hinzugefügt wurden (Standard ist AES256).
* `<root-key-crn>` - Der Cloudressourcenname (CRN) des Stammschlüssels, der vom Key Protect-Service abgerufen wird.

*SDK-Referenzen*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## Archivkomponente verwenden
{: #node-examples-archive}

Die Archivierungsschicht ermöglicht es Benutzern, veraltete Daten zu archivieren und so ihre Speicherkosten zu reduzieren. Die Archivrichtlinien (auch als *Lebenszykluskonfigurationen* bezeichnet) werden für Buckets erstellt und gelten für alle Objekte, die dem Bucket hinzugefügt werden, nachdem die Richtlinie erstellt wurde.

### Lebenszykluskonfiguration eines Buckets anzeigen
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

*SDK-Referenzen*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Lebenszykluskonfiguration erstellen 
{: #node-examples-put-lifecycle}

Ausführliche Informationen zur Strukturierung der Regeln für Lebenszykluskonfigurationen finden Sie in der [API-Referenz](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket).

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

*Schlüsselwerte*
* `<policy-id>` - Name der Lebenszyklusrichtlinie (muss eindeutig sein)
* `<number-of-days>` - Anzahl der Tage zum Aufbewahren der wiederhergestellten Datei

*SDK-Referenzen*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Lebenszykluskonfiguration eines Buckets löschen
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

*SDK-Referenzen*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Objekt vorübergehend wiederherstellen
{: #node-examples-restore-object}

Ausführliche Informationen zum Wiederherstellen von Anforderungsparametern finden Sie in der [API-Referenz](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore).

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

*Schlüsselwerte*
* `<number-of-days>` - Anzahl der Tage zum Aufbewahren der wiederhergestellten Datei

*SDK-Referenzen*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### HEAD-Informationen für Objekt anzeigen
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

*SDK-Referenzen*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## Metadaten aktualisieren
{: #node-examples-metadata}

Zum Aktualisieren der Metadaten für ein vorhandenes Objekt stehen zwei Methoden zur Verfügung:
* Die Anforderung `PUT` mit den neuen Metadaten und dem Inhalt des ursprünglichen Objekts
* Die Ausführung der Anforderung `COPY` mit den neuen Metadaten, von denen das ursprüngliche Objekt als Kopierquelle angegeben wird

### PUT zum Aktualisieren der Metadaten verwenden
{: #node-examples-metadata-put}

**Hinweis:** Von der Anforderung `PUT` wird der vorhandene Inhalt des Objekts überschrieben; deswegen muss es zuerst heruntergeladen und danach mit den neuen Metadaten erneut hochgeladen werden.


```javascript
function updateMetadataPut(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    // Vorhandenes Element zum erneuten Laden des Inhalts abrufen
    return cos.getObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        // Neue Metadaten festlegen
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

### COPY zum Aktualisieren der Metadaten verwenden
{: #node-examples-metadata-copy}

```javascript
function updateMetadataCopy(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    // Eigene Position als Kopierquelle festlegen
    var copySource = bucketName + '/' + itemName;

    // Neue Metadaten festlegen
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

## Unveränderlichen Objektspeicher verwenden
{: #node-examples-immutable}

### Schutzkonfiguration für vorhandenes Bucket hinzufügen
{: #node-examples-immutable-add}

Objekte, die in ein geschütztes Bucket geschrieben wurden, können erst gelöscht werden, wenn die Schutzdauer abgelaufen ist und alle gesetzlichen Bestimmungen zum Schutz des Objekts entfernt wurden. Einem Objekt wird der Standardaufbewahrungswert des Buckets zugewiesen, sofern für das Objekt bei seiner Erstellung nicht ein objektspezifischer Wert angegeben wurde. Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden. 

Die unterstützten Minimal- und Maximalwerte für die Einstellung der Aufbewahrungsdauer (`MinimumRetention`, `DefaultRetention` und `MaximumRetention`) sind jeweils 0 Tage und 365.243 Tage (1.000 Jahre). 


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

### Schutz für Bucket überprüfen
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

### Geschütztes Objekt hochladen
{: #node-examples-immutable-upload}

Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden.

| Wert | Typ	| Beschreibung |
| --- | --- | --- | 
|`Retention-Period` | Nicht negative Ganzzahl (Sekunden) | Aufbewahrungsdauer zum Speichern für das Objekt in Sekunden. Das Objekt kann so lange weder überschrieben noch gelöscht werden, bis die für die Aufbewahrungsdauer angegebene Zeit abgelaufen ist. Wenn dieses Feld und `Retention-Expiration-Date` angegeben werden, wird der Fehler `400` zurückgegeben. Wenn für beide Felder keine Werte angegeben werden, wird der Wert für den Zeitraum `DefaultRetention` des Buckets verwendet. Null (`0`) ist ein gültiger Wert und bedeutet, dass die Mindestaufbewahrungsdauer des Buckets `0` beträgt. |
| `Retention-expiration-date` | Datum (ISO 8601-Format) | Datum, an dem das Löschen oder Ändern des Objekts zulässig ist. Sie können nur diese Angabe oder den Header 'Retention-Period' angeben. Wenn beide angegeben werden, wird der Fehler `400` zurückgegeben. Wenn für beide Felder keine Werte angegeben werden, wird der Wert für den Zeitraum 'DefaultRetention' des Buckets verwendet. |
| `Retention-legal-hold-id` | Zeichenfolge | Eine einzelne gesetzliche Bestimmung (zum Beispiel eine Aufbewahrungspflicht), die auf das Objekt angewendet wird. Eine gesetzliche Bestimmung ist eine Zeichenfolge, die Y Zeichen lang ist. Das Objekt kann erst überschrieben oder gelöscht werden, wenn alle dem Objekt zugeordneten gesetzlichen Bestimmungen entfernt wurden. |

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

### Gesetzliche Bestimmung zu geschütztem Objekt hinzufügen oder aus geschütztem Objekt entfernen
{: #node-examples-immutable-legal-hold}

Von einem Objekt können 100 gesetzliche Bestimmungen unterstützt werden:

*  Eine Kennung für eine gesetzliche Bestimmung ist eine Zeichenfolge aus maximal 64 Zeichen und muss mindestens aus einem Zeichen bestehen. Gültige Zeichen sind Buchstaben, Ziffern, `!`, `_`, `.`, `*`, `(`, `)`, `-` und '.
* Wenn versucht wird, einem Objekt mehr 100 gesetzliche Bestimmungen hinzuzufügen, wird die neue gesetzliche Bestimmung nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung zu lang ist, wird sie dem Objekt nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung ungültige Zeichen enthält, wird sie dem Objekt nicht hinzugefügt und der Fehler `400` wird zurückgegeben.
* Wenn eine Kennung bereits für ein Objekt verwendet wird, wird die vorhandene gesetzliche Bestimmung nicht geändert; außerdem wird Fehler `409` mit der Antwort zurückgegeben, dass die Kennung bereits verwendet wird.
* Wenn ein Objekt keine Metadaten für die Aufbewahrungsdauer aufweist, wird der Fehler `400` zurückgegeben und das Hinzufügen oder Entfernen einer gesetzlichen Bestimmung ist nicht zulässig.

Der Benutzer, der gesetzliche Bestimmungen hinzufügt oder entfernt, muss über die Berechtigung `Manager` für dieses Bucket verfügen.

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

### Aufbewahrungsdauer eines geschützten Objekts erweitern
{: #node-examples-immutable-extend}

Die Aufbewahrungsdauer eines Objekts kann nur verlängert werden. Ein derzeit konfigurierter Wert kann nicht verringert werden.

Der Wert zur Verlängerung der Aufbewahrungsdauer kann auf drei Arten festgelegt werden:

* Zusätzliche Zeit auf der Basis des aktuellen Werts (`Additional-Retention-Period` oder eine ähnliche Methode)
* Neuer Erweiterungszeitraum in Sekunden (`Extend-Retention-From-Current-Time` oder eine ähnliche Methode)
* Neues Ablaufdatum für Aufbewahrungszeitraum des Objekts (`New-Retention-Expiration-Date` oder eine ähnliche Methode)

Der aktuelle Aufbewahrungszeitraum, der in den Objektmetadaten gespeichert ist, wird entweder durch die angegebene zusätzliche Zeit verlängert oder durch den neuen Wert ersetzt; dies hängt von dem Parameter ab, der in der Anforderung `extendRetention` festgelegt wird. In allen Fällen wird der Parameter für die Verlängerung des Aufbewahrungszeitraums mit den Angaben für den aktuellen Aufbewahrungszeitraum verglichen; der erweiterte Parameter wird nur akzeptiert, wenn der aktualisierte Aufbewahrungszeitraum größer ist als der aktuelle Aufbewahrungszeitraum.

Wenn Objekte in geschützten Buckets, auf die die Aufbewahrungsdauer nicht mehr angewendet wird (der Aufbewahrungszeitraum ist abgelaufen und das Objekt ist nicht durch gesetzliche Bestimmungen geschützt), überschrieben werden, wird auf sie wieder die Aufbewahrungsdauer angewendet. Der neue Aufbewahrungszeitraum kann als Teil der Objektüberschreibungsanforderung bereitgestellt werden; alternativ kann der Standardaufbewahrungszeitraum des Buckets auf das Objekt angewendet werden.

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

### Gesetzliche Bestimmungen für geschütztes Objekt auflisten
{: #node-examples-immutable-list-holds}

Nach dem Ausführen dieser Operation wird Folgendes zurückgegeben:

* Datum der Objekterstellung
* Aufbewahrungsdauer des Objekts in Sekunden
* Berechnetes Ablaufdatum der Aufbewahrungsdauer auf Basis des Zeitraums und des Erstellungsdatums
* Liste der gesetzlichen Bestimmungen
* Kennung der gesetzlichen Bestimmung
* Zeitmarke der Anwendung der gesetzlichen Bestimmung

Wenn auf ein Objekt keine gesetzlichen Bestimmungen angewendet werden, wird `LegalHoldSet` leer zurückgegeben. Wenn für ein Objekt keine Aufbewahrungsdauer angegeben wurde, wird der Fehler `404` zurückgegeben.


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
