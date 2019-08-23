---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: sdks, getting started

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
{:go: .ph data-hd-programlang='go'}

# Einführung in die SDKs
{: #sdk-gs}

Dieser Leitfaden für den Schnelleinstig enthält ein Codebeispiel, das die folgenden Operationen veranschaulicht:

* Neues Bucket erstellen
* Verfügbare Buckets auflisten
* Neue Textdatei erstellen
* Verfügbare Dateien auflisten
* Inhalt der Textdatei abrufen
* Umfangreiche Binärdatei hochladen
* Datei löschen
* Bucket löschen

## Vorbemerkungen
{: #sdk-gs-prereqs}

Sie benötigen Folgendes:

* Ein Konto bei der [{{site.data.keyword.cloud}}-Plattform](https://cloud.ibm.com/login)
* Eine [Instanz von {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
* Einen [IAM-API-Schlüssel](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) mit Schreibzugriff auf Ihre Instanz von {{site.data.keyword.cos_short}}

## SDK abrufen
{: #sdk-gs-install}

Spezifische Anweisungen zum Herunterladen und Installieren des SDKs enthält der Abschnitt [Python verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python){:new_window}.{: python}[Node.js. verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node){:new_window}.{: javascript}[Java verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java){:new_window}.{: java}[Go verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go){:new_window}.{: go}

## Codebeispiel
{: #sdk-gs-example}

Die nachfolgenden Codebeispiele liefern einführende Beispiele für die Ausführung der grundlegenden Operationen mit {{site.data.keyword.cos_short}}. Der Einfachheit halber kann das Codebeispiel mehrmals ausgeführt werden, denn es verwendet Universally Unique Identifiers (UUIDs) für Bucket-/Positionsnamen, um potenzielle Konflikte zu vermeiden.

Zum Durchführen des Codebeispiels müssen Sie die folgenden Werte ersetzen:

|Wert|Beschreibung|Beispiel|
|---|---|---|
|`<endpoint>`|Regionaler Endpunkt für Ihre COS-Instanz|`s3.us-south.cloud-object-storage.appdomain.cloud`|
|`<api-key>`|IAM-API-Schlüssel mit mindestens `Schreibberechtigung`|`xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4`|
|`<resource-instance-id>`|Eindeutige ID für die Serviceinstanz|`crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::`|
|`<storage-class>`|Speicherklasse für ein neues Bucket|`us-south-standard`|

Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```python
import os
import uuid
import ibm_boto3
from ibm_botocore.client import Config
from ibm_botocore.exceptions import ClientError
import ibm_s3transfer.manager

def log_done():
    print("DONE!\n")

def log_client_error(e):
    print("CLIENT ERROR: {0}\n".format(e))

def log_error(msg):
    print("UNKNOWN ERROR: {0}\n".format(msg))

def get_uuid():
    return str(uuid.uuid4().hex)

def generate_big_random_file(file_name, size):
    with open('%s'%file_name, 'wb') as fout:
        fout.write(os.urandom(size))

# Liste der verfügbaren Buckets abrufen
def get_buckets():
    print("Retrieving list of buckets")
    try:
        bucket_list = cos_cli.list_buckets()
        for bucket in bucket_list["Buckets"]:
            print("Bucket Name: {0}".format(bucket["Name"]))

        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to retrieve list buckets: {0}".format(e))

# Liste des Inhalts für ein Bucket abrufen
def get_bucket_contents(bucket_name):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        file_list = cos_cli.list_objects(Bucket=bucket_name)
        if file_list.has_key("Contents"):
            for file in file_list["Contents"]:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))

            log_done()
        else:
            print("Bucket {0} has no items.".format(bucket_name))
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to retrieve bucket contents: {0}".format(e))

# Bestimmtes Element aus dem Bucket abrufen
def get_item(bucket_name, item_name):
    print("Retrieving item from bucket: {0}, key: {1}".format(bucket_name, item_name))
    try:
        file = cos_cli.get_object(Bucket=bucket_name, Key=item_name)
        print("File Contents: {0}".format(file["Body"].read()))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to retrieve file contents for {0}:\n{1}".format(item_name, e))

# Neues Bucket erstellen
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos_cli.create_bucket(
            Bucket=bucket_name,
            CreateBucketConfiguration={
                "LocationConstraint":COS_STORAGE_CLASS
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to create bucket: {0}".format(e))

# Neue Textdatei erstellen
def create_text_file(bucket_name, item_name, file_text):
    print("Creating new item: {0} in bucket: {1}".format(item_name, bucket_name))
    try:
        cos_cli.put_object(
            Bucket=bucket_name,
            Key=item_name,
            Body=file_text
        )
        print("Item: {0} created!".format(item_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to create text file: {0}".format(e))

# Element löschen
def delete_item(bucket_name, item_name):
    print("Deleting item: {0} from bucket: {1}".format(item_name, bucket_name))
    try:
        cos_cli.delete_object(
            Bucket=bucket_name,
            Key=item_name
        )
        print("Item: {0} deleted!".format(item_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to delete item: {0}".format(e))

# Bucket löschen
def delete_bucket(bucket_name):
    print("Deleting bucket: {0}".format(bucket_name))
    try:
        cos_cli.delete_bucket(Bucket=bucket_name)
        print("Bucket: {0} deleted!".format(bucket_name))
        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to delete bucket: {0}".format(e))

def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # Datenblockgröße auf 5 MB festlegen
    part_size = 1024 * 1024 * 5

    # Schwellenwert auf 5 MB festlegen
    file_threshold = 1024 * 1024 * 5

    # Schwellenwert für Übertragungen und Datenblockgröße in den Konfigurationseinstellungen festlegen
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # Übertragungsmanager erstellen
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_cli, config=transfer_config)

    try:
        # Dateiupload initiieren
        future = transfer_mgr.upload(file_path, bucket_name, item_name)

        # Auf Beendigung des Uploads warten
        future.result()

        print ("Large file upload complete!")
    except Exception as e:
        print("Unable to complete large file upload: {0}".format(e))
    finally:
        transfer_mgr.shutdown()

# Konstanten für IBM COS-Werte
COS_ENDPOINT = "<endpunkt>" # Beispiel: https://s3.us-south.cloud-object-storage.appdomain.cloud
COS_API_KEY_ID = "<API-schlüssel>" # Beispiel: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_SERVICE_CRN = "<ressourceninstanz-ID>" # Beispiel: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
COS_STORAGE_CLASS = "<speicherklasse>" # Beispiel: us-south-standard

# Clientverbindung erstellen
cos_cli = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_SERVICE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

# *** Hauptprogramm ***
def main():
    try:
        new_bucket_name = "py.bucket." + get_uuid()
        new_text_file_name = "py_file_" + get_uuid() + ".txt"
        new_text_file_contents = "This is a test file from Python code sample!!!"
        new_large_file_name = "py_large_file_" + get_uuid() + ".bin"
        new_large_file_size = 1024 * 1024 * 20 

        # Neues Bucket erstellen
        create_bucket(new_bucket_name)

        # Liste von Buckets abrufen
        get_buckets()

        # Neue Textdatei erstellen
        create_text_file(new_bucket_name, new_text_file_name, new_text_file_contents)

        # Liste der Dateien aus dem neuen Bucket abrufen
        get_bucket_contents(new_bucket_name)

        # Inhalt der Textdatei abrufen
        get_item(new_bucket_name, new_text_file_name)

        # Neue lokale Binärdatei von 20 MB erstellen
        generate_big_random_file(new_large_file_name, new_large_file_size)

        # Umfangreiche Datei mit dem Übertragungsmanager hochladen
        upload_large_file(new_bucket_name, new_large_file_name, new_large_file_name)

        # Liste der Dateien aus dem neuen Bucket abrufen
        get_bucket_contents(new_bucket_name)

        # Die zwei neuen Dateien entfernen
        delete_item(new_bucket_name, new_large_file_name)
        delete_item(new_bucket_name, new_text_file_name)

        # Neues Bucket entfernen
        delete_bucket(new_bucket_name)
    except Exception as e:
        log_error("Main Program Error: {0}".format(e))

if __name__ == "__main__":
    main()
```
{: codeblock}
{: python}

```javascript
'use strict';

// Erforderliche Bibliotheken
const ibm = require('ibm-cos-sdk');
const fs = require('fs');
const async = require('async');
const uuidv1 = require('uuid/v1');
const crypto = require('crypto');

function logError(e) {
    console.log(`ERROR: ${e.code} - ${e.message}\n`);
}

function logDone() {
    console.log('DONE!\n');
}

function getUUID() {
    return uuidv1().toString().replace(/-/g, "");
}

function generateBigRandomFile(fileName, size) {
    return new Promise(function(resolve, reject) {
        crypto.randomBytes(size, (err, buf) => {
            if (err) reject(err);

            fs.writeFile(fileName, buf, function (err) {
                if (err) {
                    reject(err);
                }
                else {
                    resolve();
                }
            });     
        });      
    });
}

// Liste der verfügbaren Buckets abrufen
function getBuckets() {
    console.log('Retrieving list of buckets');
    return cos.listBuckets()
    .promise()
    .then((data) => {
        if (data.Buckets != null) {
            for (var i = 0; i < data.Buckets.length; i++) {
                console.log(`Bucket Name: ${data.Buckets[i].Name}`);
            }
            logDone();
        }
    })
    .catch((logError));
}

// Liste des Inhalts für ein Bucket abrufen
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
            logDone();
        }    
    })
    .catch(logError);
}

// Bestimmtes Element aus dem Bucket abrufen
function getItem(bucketName, itemName) {
    console.log(`Retrieving item from bucket: ${bucketName}, key: ${itemName}`);
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log('File Contents: ' + Buffer.from(data.Body).toString());
            logDone();
        }    
    })
    .catch(logError);
}

// Neues Bucket erstellen
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: COS_STORAGE_CLASS
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
        logDone();
    }))
    .catch(logError);
}

// Neue Textdatei erstellen
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName, 
        Key: itemName, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
        logDone();
    })
    .catch(logError);
}

// Element löschen
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() =>{
        console.log(`Item: ${itemName} deleted!`);
        logDone();
    })
    .catch(logError);
}

// Bucket löschen
function deleteBucket(bucketName) {
    console.log(`Deleting bucket: ${bucketName}`);
    return cos.deleteBucket({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucketName} deleted!`);
        logDone();
    })
    .catch(logError);
}

// Mehrteiliger Upload
function multiPartUpload(bucketName, itemName, filePath) {
    var uploadID = null;

    if (!fs.existsSync(filePath)) {
        logError(new Error(`The file \'${filePath}\' does not exist or is not accessible.`));
        return;
    }

    return new Promise(function(resolve, reject) {
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
                        logError(e);
                        reject(e);
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
                    .then(() => {
                        logDone();
                        resolve();
                    })
                    .catch((e) => {
                        cancelMultiPartUpload(bucketName, itemName, uploadID);
                        logError(e);
                        reject(e);
                    });
                });
            });
        })
        .catch((e) => {
            logError(e);
            reject(e);
        });
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
    .catch(logError);
}

// Konstanten für IBM COS-Werte
const COS_ENDPOINT = "<endpunkt>";  // Beispiel: s3.us-south.cloud-object-storage.appdomain.cloud
const COS_API_KEY_ID = "<API-Schlüssel";  // Beispiel: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
const COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
const COS_SERVICE_CRN = "<ressourceninstanz-ID>"; // Beispiel: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
const COS_STORAGE_CLASS = "<speicherklasse>"; // Beispiele: us-south-standard

// IBM COS-Bibliothek initialisieren
var config = {
    endpoint: COS_ENDPOINT,
    apiKeyId: COS_API_KEY_ID,
    ibmAuthEndpoint: COS_AUTH_ENDPOINT,
    serviceInstanceId: COS_SERVICE_CRN,
};

var cos = new ibm.S3(config);

// Haupt-App
function main() {
    try {
        var newBucketName = "js.bucket." + getUUID();
        var newTextFileName = "js_file_" + getUUID() + ".txt";
        var newTextFileContents = "This is a test file from Node.js code sample!!!";
        var newLargeFileName = "js_large_file_" + getUUID() + ".bin";
        var newLargeFileSize = 1024 * 1024 * 20;

        createBucket(newBucketName) // Neues Bucket erstellen
            .then(() => getBuckets()) // Liste von Buckets abrufen
            .then(() => createTextFile(newBucketName, newTextFileName, newTextFileContents)) // Neue Textdatei erstellen
            .then(() => getBucketContents(newBucketName)) // Liste der Dateien aus dem neuen Bucket abrufen
            .then(() => getItem(newBucketName, newTextFileName)) // Inhalt der Textdatei abrufen
            .then(() => generateBigRandomFile(newLargeFileName, newLargeFileSize)) // Neue lokale Binärdatei von 20 MB erstellen
            .then(() => multiPartUpload(newBucketName, newLargeFileName, newLargeFileName)) // Umfangreiche Datei mit dem Übertragungsmanager hochladen
            .then(() => getBucketContents(newBucketName)) // Liste der Dateien aus dem neuen Bucket abrufen
            .then(() => deleteItem(newBucketName, newLargeFileName)) // Umfangreiche Datei entfernen
            .then(() => deleteItem(newBucketName, newTextFileName)) // Textdatei entfernen
            .then(() => deleteBucket(newBucketName)); // Neues Bucket entfernen
    }
    catch(ex) {
        logError(ex);
    }
}

main();
```
{: codeblock}
{: javascript}

```java
// Erforderliche Bibliotheken
import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.SdkClientException;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import com.ibm.cloud.objectstorage.services.s3.transfer.TransferManager;
import com.ibm.cloud.objectstorage.services.s3.transfer.TransferManagerBuilder;
import com.ibm.cloud.objectstorage.services.s3.transfer.Upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class JavaExampleCode {
    private static AmazonS3 _cosClient;
    private static String api_key;
    private static String service_instance_id;
    private static String endpoint_url;
    private static String location;

    public static void main(String[] args) throws IOException
    {
        // Zufälligen Universal Unique Identifier (UUID) erstellen
        UUID uuid = UUID.randomUUID();

        // Konstanten für IBM COS-Werte
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
        api_key = "<API-schlüssel>"; // Beispiel: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
        service_instance_id = "<ressourceninstanz-ID>"; // Beispiel: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
        endpoint_url = "<endpunkt>"; // Beispiel: https://s3.us-south.cloud-object-storage.appdomain.cloud
        location = "<speicherklasse>"; // Beispiel: us-south-standard

        // Clientverbindungsdetails erstellen
        _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

        // Zeichenfolgewerte festlegen
        String bucketName = "java.bucket" + UUID.randomUUID().toString().replace("-","");
        String itemName = UUID.randomUUID().toString().replace("-","") + "_java_file.txt";
        String fileText = "This is a test file from the Java code sample!!!";

        // Neues Bucket erstellen
        createBucket(bucketName, _cosClient);

        // Liste von Buckets abrufen
        listBuckets(_cosClient);

        // Neue Textdatei erstellen & hochladen
        createTextFile(bucketName, itemName, fileText);

        // Liste der Dateien aus dem neuen Bucket abrufen
        listObjects(bucketName, _cosClient);

        // Neue Datei entfernen
        deleteItem(bucketName, itemName);

        // Umfangreiche Datei erstellen & mit dem Übertragungsmanager hochladen & umfangreiche Datei entfernen
        createLargeFile(bucketName);
        // Neues Bucket entfernen
        deleteBucket(bucketName);
    }

    private static void createLargeFile(String bucketName)  throws IOException {
        String fileName = "Sample"; //Setting the File Name

        try {
            File uploadFile = File.createTempFile(fileName,".tmp");
            uploadFile.deleteOnExit();
            fileName = uploadFile.getName();

            largeObjectUpload(bucketName, uploadFile);
        } catch (InterruptedException e) {
            System.out.println("object upload timed out");
        }

        deleteItem(bucketName, fileName); // Neue umfangreiche Datei entfernen
    }

    // Clientverbindung erstellen
    public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
    {
        AWSCredentials credentials;
        credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig).build();
        return cosClient;
    }

    // Neues Bucket erstellen
    public static void createBucket(String bucketName, AmazonS3 cosClient)
    {
        cosClient.createBucket(bucketName);
        System.out.printf("Bucket: %s created!\n", bucketName);
    }

    // Liste der verfügbaren Buckets abrufen
    public static void listBuckets(AmazonS3 cosClient)
    {
        System.out.println("Listing buckets:");
        final List<Bucket> bucketList = _cosClient.listBuckets();
        for (final Bucket bucket : bucketList) {
            System.out.println(bucket.getName());
        }
        System.out.println();
    }

    // Liste des Inhalts für ein Bucket abrufen
    public static void listObjects(String bucketName, AmazonS3 cosClient)
    {
        System.out.println("Listing objects in bucket " + bucketName);
        ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    }

    // Datei erstellen und in neues Bucket hochladen
    public static void createTextFile(String bucketName, String itemName, String fileText) {
        System.out.printf("Creating new item: %s\n", itemName);

        InputStream newStream = new ByteArrayInputStream(fileText.getBytes(Charset.forName("UTF-8")));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileText.length());

        PutObjectRequest req = new PutObjectRequest(bucketName, itemName, newStream, metadata);
        _cosClient.putObject(req);

        System.out.printf("Item: %s created!\n", itemName);
    }

    // Element löschen
    public static void deleteItem(String bucketName, String itemName) {
        System.out.printf("Deleting item: %s\n", itemName);
        _cosClient.deleteObject(bucketName, itemName);
        System.out.printf("Item: %s deleted!\n", itemName);
    }

    // Bucket löschen
    public static void deleteBucket(String bucketName) {
        System.out.printf("Deleting bucket: %s\n", bucketName);
        _cosClient.deleteBucket(bucketName);
        System.out.printf("Bucket: %s deleted!\n", bucketName);
    }

    //  Umfangreiche Datei in neues Bucket hochladen
    public static void largeObjectUpload(String bucketName, File uploadFile) throws IOException, InterruptedException {

        if (!uploadFile.isFile()) {
            System.out.printf("The file does not exist or is not accessible.\n");
            return;
        }

        System.out.println("Starting large file upload with TransferManager");

        //Größe der Dateiteile auf 5 MB festlegen
        long partSize = 1024 * 1024 * 20;

        //Schwellenwert auf 5 MB festlegen
        long thresholdSize = 1024 * 1024 * 20;

        AmazonS3 s3client = createClient( api_key, service_instance_id, endpoint_url, location);

        TransferManager transferManager = TransferManagerBuilder.standard()
                .withS3Client(s3client)
                .withMinimumUploadPartSize(partSize)
                .withMultipartCopyThreshold(thresholdSize)
                .build();

        try {
            Upload lrgUpload = transferManager.upload(bucketName, uploadFile.getName(), uploadFile);
            lrgUpload.waitForCompletion();
            System.out.println("Large file upload complete!");
        } catch (SdkClientException e) {
            System.out.printf("Upload error: %s\n", e.getMessage());
        } finally {
            transferManager.shutdownNow();
        }
    }
}

```
{: codeblock}
{: java}

``` Go
package main
import (
	"bytes"
	"fmt"
	"github.com/IBM/ibm-cos-sdk-go/aws"
	"github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
	"github.com/IBM/ibm-cos-sdk-go/aws/session"
	"github.com/IBM/ibm-cos-sdk-go/service/s3"
	"io"
	"math/rand"
	"os"
	"time"
)

// Konstanten für IBM COS-Werte
const (
	apiKey            = "<API-Schlüssel>" // Beispiel: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
	serviceInstanceID = "<ressourceninstanz-ID>" // Beispiel: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
	authEndpoint      = "https://iam.bluemix.net/oidc/token"
	serviceEndpoint   = "<endpunkt>" // Beispiel: https://s3.us-south.cloud-object-storage.appdomain.cloud )

// UUID
func random(min int, max int) int {
	return rand.Intn(max-min) + min
}

func main() {

	// UUID
	rand.Seed(time.Now().UnixNano())
	UUID := random(10, 2000)

	// Variablen
	newBucket := fmt.Sprintf("%s%d", "go.bucket", UUID) // Name des neuen Buckets
	objectKey := fmt.Sprintf("%s%d%s", "go_file_", UUID, ".txt") // Objektschlüssel
	content := bytes.NewReader([]byte("This is a test file from Go code sample!!!"))
	downloadObjectKey := fmt.Sprintf("%s%d%s", "downloaded_go_file_", UUID, ".txt") // Heruntergeladener Objektschlüssel
	//Neue Konfiguration einrichten
	conf := aws.NewConfig().
		WithRegion("<storage-class>"). // Speicherklasse eingeben (LocationConstraint) - Beispiel: us-standard 		WithEndpoint(serviceEndpoint).
		WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
		WithS3ForcePathStyle(true)

	// Clientverbindung erstellen
	sess := session.Must(session.NewSession()) // Neue Sitzung erstellen
	client := s3.New(sess, conf)               // Neuen Client erstellen

	// Neues Bucket erstellen
	_, err := client.CreateBucket(&s3.CreateBucketInput{
		Bucket: aws.String(newBucket), // Name des neuen Buckets
	})
	if err != nil {
		exitErrorf("Unable to create bucket %q, %v", newBucket, err)
	}

	// Vor Beenden warten, bis Bucket erstellt ist
	fmt.Printf("Waiting for bucket %q to be created...\n", newBucket)

	err = client.WaitUntilBucketExists(&s3.HeadBucketInput{
		Bucket: aws.String(newBucket),
	})
	if err != nil {
		exitErrorf("Error occurred while waiting for bucket to be created, %v", newBucket)
	}

	fmt.Printf("Bucket %q successfully created\n", newBucket)

	// Liste der verfügbaren Buckets abrufen
	bklist, err := client.ListBuckets(nil)
	if err != nil {
		exitErrorf("Unable to list buckets, %v", err)
	}

	fmt.Println("Buckets:")

	for _, b := range bklist.Buckets {
		fmt.Printf("* %s created on %s\n",
			aws.StringValue(b.Name), aws.TimeValue(b.CreationDate))
	}

	// Objekt hochladen
	input3 := s3.CreateMultipartUploadInput{
		Bucket: aws.String(newBucket), // Bucket Name
		Key:    aws.String(objectKey), // Object Key
	}

	upload, _ := client.CreateMultipartUpload(&input3)

	uploadPartInput := s3.UploadPartInput{
		Bucket:     aws.String(newBucket), // Bucket Name
		Key:        aws.String(objectKey), // Object Key
		PartNumber: aws.Int64(int64(1)),
		UploadId:   upload.UploadId,
		Body:       content,
	}

	var completedParts []*s3.CompletedPart
	completedPart, _ := client.UploadPart(&uploadPartInput)

	completedParts = append(completedParts, &s3.CompletedPart{
		ETag:       completedPart.ETag,
		PartNumber: aws.Int64(int64(1)),
	})

	completeMPUInput := s3.CompleteMultipartUploadInput{
		Bucket: aws.String(newBucket), // Name des Buckets
		Key:    aws.String(objectKey), // Objektschlüssel
		MultipartUpload: &s3.CompletedMultipartUpload{
			Parts: completedParts,
		},
		UploadId: upload.UploadId,
	}

	d, _ := client.CompleteMultipartUpload(&completeMPUInput)
	fmt.Println(d)

	// Objekte innerhalb eines Buckets auflisten
	resp, err := client.ListObjects(&s3.ListObjectsInput{Bucket: aws.String(newBucket)})
	if err != nil {
		exitErrorf("Unable to list items in bucket %q, %v", newBucket, err)
	}
	for _, item := range resp.Contents {
		fmt.Println("Name:         ", *item.Key)          // Name des Objekts ausgeben
		fmt.Println("Last modified:", *item.LastModified) // Datum der letzten Änderung an dem Objekt ausgeben
		fmt.Println("Size:         ", *item.Size)         // Größe des Objekts ausgeben
		fmt.Println("")
	}

	fmt.Println("Found", len(resp.Contents), "items in bucket", newBucket)


	// Objekt herunterladen
	input4 := s3.GetObjectInput{
		Bucket: aws.String(newBucket), // Das Bucket, in dem sich das Objekt befindet
		Key:    aws.String(objectKey), // Das Objekt, das heruntergeladen werden soll
	}

	res, err := client.GetObject(&input4)
	if err != nil {
		exitErrorf("Unable to download object %q from bucket %q, %v", objectKey, newBucket, err)
	}

	f, _ := os.Create(downloadObjectKey)
	defer f.Close()
	io.Copy(f, res.Body)

	fmt.Println("Downloaded", f.Name())


	// Objekt innerhalb des neuen Buckets löschen
	_, err = client.DeleteObject(&s3.DeleteObjectInput{Bucket: aws.String(newBucket), Key: aws.String(objectKey)})
	if err != nil {
		exitErrorf("Unable to delete object %q from bucket %q, %v", objectKey, newBucket, err)
	}

	err = client.WaitUntilObjectNotExists(&s3.HeadObjectInput{
		Bucket: aws.String(newBucket),
		Key:    aws.String(objectKey),
	})
	if err != nil {
		exitErrorf("Error occurred while waiting for object %q to be deleted, %v", objectKey)
	}

	fmt.Printf("Object %q successfully deleted\n", objectKey)

	// Neues Bucket löschen
	// Bucket muss leer sein; andernfalls schlägt der Aufruf fehl
	_, err = client.DeleteBucket(&s3.DeleteBucketInput{
		Bucket: aws.String(newBucket),
	})
	if err != nil {
		exitErrorf("Unable to delete bucket %q, %v", newBucket, err)
	}

	// Vor Beenden warten, bis Bucket gelöscht ist
	fmt.Printf("Waiting for bucket %q to be deleted...\n", newBucket)

	err = client.WaitUntilBucketNotExists(&s3.HeadBucketInput{
		Bucket: aws.String(newBucket),
	})
	if err != nil {
		exitErrorf("Error occurred while waiting for bucket to be deleted, %v", newBucket)
	}

	fmt.Printf("Bucket %q successfully deleted\n", newBucket)
}


func exitErrorf(msg string, args ...interface{}) {
	fmt.Fprintf(os.Stderr, msg+"\n", args...)
	os.Exit(1)
```
{: codeblock}
{: Go}

## Codebeispiel ausführen
{: #sdk-gs-run}

Wenn Sie das Codebeispiel ausführen möchten, kopieren Sie die Codeblöcke oben und führen Sie Folgendes aus:
```
python python-example.py
```
{: codeblock}
{: python}

```
node node-example.js
```
{: codeblock}
{: javascript}

```
java javaexamplecode
```
{: codeblock}
{: java}

```
go run go_example.go
```
{: codeblock}
{: Go}

## Ausgabe vom Codebeispiel
{: #sdk-gs-output}

Die Ausgabe des Codebeispiels sollte ungefähr wie folgt aussehen:

```
Creating new bucket: py.bucket.779177bfe41945edb458294d0b25440a
Bucket: py.bucket.779177bfe41945edb458294d0b25440a created!
DONE!

Retrieving list of buckets
Bucket Name: py.bucket.779177bfe41945edb458294d0b25440a
DONE!

Creating new item: py_file_17b79068b7c845658f2f74249e14e267.txt in bucket: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt created!
DONE!

Retrieving bucket contents from: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt (46 bytes).
DONE!

Retrieving item from bucket: py.bucket.779177bfe41945edb458294d0b25440a, key: py_file_17b79068b7c845658f2f74249e14e267.txt
File Contents: This is a test file from Python code sample!!!
DONE!

Starting large file upload for py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin to bucket: py.bucket.779177bfe41945edb458294d0b25440a
Large file upload complete!
Retrieving bucket contents from: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt (46 bytes).
Item: py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin (20971520 bytes).
DONE!

Deleting item: py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin from bucket: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_large_file_722319147bba4fc4a6c111cc21eb11b5.bin deleted!
DONE!

Deleting item: py_file_17b79068b7c845658f2f74249e14e267.txt from bucket: py.bucket.779177bfe41945edb458294d0b25440a
Item: py_file_17b79068b7c845658f2f74249e14e267.txt deleted!
DONE!

Deleting bucket: py.bucket.779177bfe41945edb458294d0b25440a
Bucket: py.bucket.779177bfe41945edb458294d0b25440a deleted!
DONE!
```
{: codeblock}
{: python}

```
Creating new bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32 created!
DONE!

Retrieving list of buckets
Bucket Name: js.bucket.c697b4403f8211e9b1228597cf8e3a32
DONE!

Creating new item: js_file_c697db503f8211e9b1228597cf8e3a32.txt
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt created!
DONE!

Retrieving bucket contents from: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt (47 bytes).
DONE!

Retrieving item from bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32, key: js_file_c697db503f8211e9b1228597cf8e3a32.txt
File Contents: This is a test file from Node.js code sample!!!
DONE!

Starting multi-part upload for js_large_file_c697db513f8211e9b1228597cf8e3a32.bin to bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 1 of 4)
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 2 of 4)
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 3 of 4)
Uploading to js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (part 4 of 4)
DONE!

Retrieving bucket contents from: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt (47 bytes).
Item: js_large_file_c697db513f8211e9b1228597cf8e3a32.bin (20971520 bytes).
DONE!

Deleting item: js_large_file_c697db513f8211e9b1228597cf8e3a32.bin
Item: js_large_file_c697db513f8211e9b1228597cf8e3a32.bin deleted!
DONE!

Deleting item: js_file_c697db503f8211e9b1228597cf8e3a32.txt
Item: js_file_c697db503f8211e9b1228597cf8e3a32.txt deleted!
DONE!

Deleting bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32
Bucket: js.bucket.c697b4403f8211e9b1228597cf8e3a32 deleted!
DONE!
```
{: codeblock}
{: javascript}

```
Bucket: java.bucket71bd68d087b948f5a1f1cbdd86e4fda2 created!
DONE!

Listing buckets:
java.bucket71bd68d087b948f5a1f1cbdd86e4fda2

Creating new item: 4e69e627be7e4e10bf8d39e3fa10058f_java_file.txt
Item: 4e69e627be7e4e10bf8d39e3fa10058f_java_file.txt created!

Listing objects in bucket java.bucket71bd68d087b948f5a1f1cbdd86e4fda2
 - 4e69e627be7e4e10bf8d39e3fa10058f_java_file.txt  (size = 48)

Deleting item: 4e69e627be7e4e10bf8d39e3fa10058f_java_file.txt
Item: 4e69e627be7e4e10bf8d39e3fa10058f_java_file.txt deleted!

Starting large file upload with TransferManager
Large file upload complete!
Deleting item: Sample5438677733541671254.tmp
Item: Sample5438677733541671254.tmp deleted!

Deleting bucket: java.bucket71bd68d087b948f5a1f1cbdd86e4fda2
Bucket: java.bucket71bd68d087b948f5a1f1cbdd86e4fda2 deleted!
```
{: codeblock}
{: java}

```
Waiting for bucket "go.bucket645" to be created...
Bucket "go.bucket645" successfully created

Listing buckets:
* go.bucket645 created on 2019-03-10 13:25:12.072 +0000 UTC

{
  Bucket: "go.bucket645",
  ETag: "\"686d1d07d6de02e920532342fcbd6d2a-1\"",
  Key: "go_file_645.txt",
  Location: "http://s3.us.cloud-object-storage.appdomain.cloud/go.bucket645/go_file_645.txt"
}

Name:          go_file_645.txt
Last modified: 2019-03-10 13:25:14 +0000 UTC
Size:          42

Found 1 items in bucket go.bucket645

Downloaded downloaded_go_file_645.txt

Object "go_file_645.txt" successfully deleted

Waiting for bucket "go.bucket645" to be deleted...
Bucket "go.bucket645" successfully deleted
```
{: codeblock}
{: go}

<br/>
