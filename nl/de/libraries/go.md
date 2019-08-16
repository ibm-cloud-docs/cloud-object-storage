---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: sdks, getting started, go

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

# Go verwenden
{: #go}

Das {{site.data.keyword.cos_full}}-SDK für Go ist umfassend und verfügt über Komponenten und Funktionen, die in diesem Handbuch nicht beschrieben werden. Ausführliche Informationen zu Klassen und Methoden finden Sie in der [Go-Dokumentation](https://ibm.github.io/ibm-cos-sdk-go/). Den Quellcode finden Sie im [GitHub-Repository](https://github.com/IBM/ibm-cos-sdk-go).

## SDK abrufen
{: #go-get-sdk}

Verwenden Sie `go get` zum Abrufen des SDKs, um es zum Arbeitsbereich GOPATH oder den GO-Modulabhängigkeiten des Projekts hinzuzufügen. Für das SDK ist mindestens die GO-Version 1.10 erforderlich, maximal zulässig ist GO 1.12. Zukünftige Versionen von Go werden unterstützt, sobald der Prozess zur Qualitätskontrolle abgeschlossen ist.

```
go get github.com/IBM/ibm-cos-sdk-go
```

Verwenden Sie zum Aktualisieren des SDKs `go get -u`, um die neueste Version des SDKs abzurufen.

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### Pakete importieren
{: #go-import-packages}

Nachdem Sie das SDK installiert haben, müssen Sie die benötigten Pakete wie im folgenden Beispiel dargestellt in Ihre Go-Anwendungen importieren, damit das SDK verwendet werden kann:
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## Client erstellen und Berechtigungsnachweise ableiten
{: #go-client-credentials}

Zum Herstellen einer Verbindung zu {{site.data.keyword.cos_full_notm}} wird ein Client erstellt und durch Bereitstellen der Berechtigungsnachweisinformationen (API-Schlüssel und Serviceinstanz-ID) konfiguriert. Als Quelle für diese Werte können auch automatisch eine Berechtigungsnachweisdatei oder Umgebungsvariablen verwendet werden. 

Die Berechtigungsnachweise können durch Erstellen eines [Serviceberechtigungsnachweises](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) oder über eine Befehlszeilenschnittstelle gesucht werden.

In Abbildung 1 wird ein Beispiel für die Verwendungshinweise zum Definieren von Umgebungsvariablen in einer Anwendungslaufzeit im {{site.data.keyword.cos_full_notm}}-Portal dargestellt. Die erforderlichen Variablen sind `IBM_API_KEY_ID`, in der der Serviceberechtigungsnachweis 'apikey' enthalten ist,  die Variable `IBM_SERVICE_INSTANCE_ID`, in der 'resource_instance_id' ebenfalls aus dem Serviceberechtigungsnachweis enthalten ist, und die Variable `IBM_AUTH_ENDPOINT` mit einem Ihrem Konto entsprechenden Wert, zum Beispiel `https://iam.cloud.ibm.com/identity/token`. Falls Sie Umgebungsvariablen zum Definieren der Anwendungsberechtigungsnachweise verwenden, verwenden Sie `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).` als geeigneten Wert; dadurch wird die ähnliche Methode ersetzt, die im Konfigurationsbeispiel verwendet wird.

![environmentvariables](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="Abbildung 1. Umgebungsvariablen" caption-side="top"}

### Konfiguration initialisieren
{: #go-init-config}

```Go

// Konstanten für IBM COS-Werte
const (
    apiKey            = "<API_KEY>"  // eg "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ"
    serviceInstanceID = "<RESOURCE_INSTANCE_ID>" // "crn:v1:bluemix:public:iam-identity::a/3ag0e9402tyfd5d29761c3e97696b71n::serviceid:ServiceId-540a4a41-7322-4fdd-a9e7-e0cb7ab760f9"
    authEndpoint      = "https://iam.cloud.ibm.com/identity/token"
    serviceEndpoint   = "<SERVICE_ENDPOINT>" // eg "https://s3.us.cloud-object-storage.appdomain.cloud"
    bucketLocation    = "<LOCATION>" // eg "us"
)

// Konfiguration erstellen

conf := aws.NewConfig().
    WithRegion("us-standard").
    WithEndpoint(serviceEndpoint).
    WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
    WithS3ForcePathStyle(true)

```
Weitere Informationen zu Endpunkten finden Sie unter [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Codebeispiele
{: #go-code-examples}

### Neues Bucket erstellen
{: #go-new-bucket}

Auf eine Liste gültiger Bereitstellungscodes für `LocationConstraint` kann im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes) verwiesen werden.

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucketnamen
    newBucket := "<NEW_BUCKET_NAME>"
    newColdBucket := "<NEW_COLD_BUCKET_NAME>"

    input := &s3.CreateBucketInput{
        Bucket: aws.String(newBucket),
    }
    client.CreateBucket(input)

    input2 := &s3.CreateBucketInput{
        Bucket: aws.String(newColdBucket),
        CreateBucketConfiguration: &s3.CreateBucketConfiguration{
            LocationConstraint: aws.String("us-cold"),
        },
    }
    client.CreateBucket(input2)

    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```

### Verfügbare Buckets auflisten
{: #go-list-buckets}

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Funktion aufrufen
    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}

```


### Objekt in Bucket hochladen
{: #go-put-object}

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Variablen
    bucketName := "<NEW_BUCKET_NAME>"
    key := "<OBJECT_KEY>"
    content := bytes.NewReader([]byte("<CONTENT>"))

    input := s3.PutObjectInput{
        Bucket:        aws.String(bucketName),
        Key:           aws.String(key),
        Body:          content,
    }

    // Funktion aufrufen
    result, _ := client.PutObject(&input)
    fmt.Println(result)
}
```



### Elemente in Bucket auflisten
{: #go-list-objects}

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucketname
    bucket := "<BUCKET_NAME>"

    // Funktion aufrufen
    resp, _ := client.ListObjects(&s3.ListObjectsInput{Bucket: aws.String(bucket)})

    for _, item := range resp.Contents {
        fmt.Println("Name:         ", *item.Key)
        fmt.Println("Last modified:", *item.LastModified)
        fmt.Println("Size:         ", *item.Size)
        fmt.Println("Storage class:", *item.StorageClass)
        fmt.Println("")
    }

    fmt.Println("Found", len(resp.Contents), "items in bucket", bucket)
    fmt.Println("")
}

```

### Objektinhalt abrufen
{: #go-get-object}

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Variablen
    bucketName := "<NEW_BUCKET_NAME>"
    key := "<OBJECT_KEY>"

    // Benutzer müssen Bucket und Schlüssel erstellen (unstrukturierter Zeichenfolgename)
    input := s3.GetObjectInput{
        Bucket: aws.String(bucketName),
        Key:    aws.String(key),
    }

    // Funktion aufrufen
    res, _ := client.GetObject(&input)

    body, _ := ioutil.ReadAll(res.Body)
    fmt.Println(body)
}

```

### Objekt in Bucket löschen
{: #go-delete-object}

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucketname
    bucket := "<BUCKET_NAME>"

    input := &s3.DeleteObjectInput{
        Bucket: aws.String(bucket),
        Key:    aws.String("<OBJECT_KEY>"),
    }

    d, _ := client.DeleteObject(input)
    fmt.Println(d)
}
```


### Mehrere Objekte in Bucket löschen
{: #go-multidelete}

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucketname
    bucket := "<BUCKET_NAME>"

    input := &s3.DeleteObjectsInput{
        Bucket: aws.String(bucket),
        Delete: &s3.Delete{
            Objects: []*s3.ObjectIdentifier{
                {
                    Key: aws.String("<OBJECT_KEY1>"),
                },
                {
                    Key: aws.String("<OBJECT_KEY2>"),
                },
                {
                    Key: aws.String("<OBJECT_KEY3>"),
                },
            },
            Quiet: aws.Bool(false),
        },
    }

    d, _ := client.DeleteObjects(input)
    fmt.Println(d)
}
```


### Bucket löschen
{: #go-delete-bucket}

```Go
func main() {

    // Bucketname
    bucket := "<BUCKET_NAME>"

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    input := &s3.DeleteBucketInput{
        Bucket: aws.String(bucket),
    }
    d, _ := client.DeleteBucket(input)
    fmt.Println(d)
}
```



### Manuelles mehrteiliges Hochladen ausführen
{: #go-multipart}

```Go	
func main() {

    // Variablen
    bucket := "<BUCKET_NAME>"
    key := "<OBJECT_KEY>"
    content := bytes.NewReader([]byte("<CONTENT>"))

    input := s3.CreateMultipartUploadInput{
        Bucket: aws.String(bucket),
        Key:    aws.String(key),
    }

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    upload, _ := client.CreateMultipartUpload(&input)

    uploadPartInput := s3.UploadPartInput{
        Bucket:     aws.String(bucket),
        Key:        aws.String(key),
        PartNumber: aws.Int64(int64(1)),
        UploadId:   upload.UploadId,
        Body:          content,
    }

    var completedParts []*s3.CompletedPart
    completedPart, _ := client.UploadPart(&uploadPartInput)

    completedParts = append(completedParts, &s3.CompletedPart{
        ETag:       completedPart.ETag,
        PartNumber: aws.Int64(int64(1)),
    })

    completeMPUInput := s3.CompleteMultipartUploadInput{
        Bucket: aws.String(bucket),
        Key:    aws.String(key),
        MultipartUpload: &s3.CompletedMultipartUpload{
            Parts: completedParts,
        },
        UploadId: upload.UploadId,
    }

    d, _ := client.CompleteMultipartUpload(&completeMPUInput)
    fmt.Println(d)
}
```

## Key Protect verwenden
{: #go-examples-kp}

Key Protect kann zu einem Speicherbucket zum Verwalten von Verschlüsselungsschlüsseln hinzugefügt werden. In IBM COS werden alle Daten verschlüsselt, aber von Key Protect wird ein Service zum Generieren, Rotieren und Steuern des Zugriffs auf Verschlüsselungsschlüssel unter Verwendung eines zentralen Service bereitgestellt.

### Vorbereitende Schritte
{: #go-examples-kp-prereqs}

Damit Sie ein Bucket erstellen können, während Key Protect aktiviert ist, müssen die folgenden Voraussetzungen erfüllt sein:

* Ein Key Protect-Service wird [bereitgestellt](/docs/services/key-protect?topic=key-protect-provision#provision)
* Ein Stammschlüssel ist verfügbar (entweder [generiert](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) oder [importiert](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Cloudressourcenname für Stammschlüssel abrufen
{: #go-examples-kp-root}

1. Rufen Sie die [Instanz-ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) für den Key Protect-Service ab.
2. Verwenden Sie die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api), um alle [verfügbaren Schlüssel](https://cloud.ibm.com/apidocs/key-protect) abzurufen.
    * Sie können entweder `curl`-Befehle oder einen API-REST-Client wie zum Beispiel [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) für den Zugriff auf die [Key Protect-API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) verwenden.
3. Rufen Sie den Cloudressourcennamen des Stammschlüssels ab, mit dem Sie Key Protect für das Bucket aktivieren. Der Cloudressourcenname ähnelt der folgenden Zeichenfolge:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Bucket während Aktivierung von Key Protect erstellen
{: #go-examples-kp-new-bucket}

```Go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucketnamen
    newBucket := "<NEW_BUCKET_NAME>"

    fmt.Println("Creating new encrypted bucket:", newBucket)

	input := &s3.CreateBucketInput{
		Bucket: aws.String(newBucket),
		IBMSSEKPCustomerRootKeyCrn: aws.String("<ROOT-KEY-CRN>"),
		IBMSSEKPEncryptionAlgorithm:aws.String("<ALGORITHM>"),
    }
    client.CreateBucket(input)

    // Buckets auflisten
    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```
*Schlüsselwerte*
* `<NEW_BUCKET_NAME>` - Der Name des neuen Buckets.
* `<ROOT-KEY-CRN>` - Der Cloudressourcenname (CRN) des Stammschlüssels, der vom Key Protect-Service abgerufen wird.
* `<ALGORITHM>` - Der Verschlüsselungsalgorithmus, der für neue Objekte verwendet wird, die zum Bucket hinzugefügt wurden (Standard ist AES256).


### Übertragungsmanager verwenden
{: #go-transfer}

```Go
func main() {

    // Variablen
    bucket := "<BUCKET_NAME>"
    key := "<OBJECT_KEY>"

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Uploader mit S3-Client und angepassten Optionen erstellen
    uploader := s3manager.NewUploaderWithClient(client, func(u *s3manager.Uploader) {
        u.PartSize = 5 * 1024 * 1024 // 64MB per part
    })

    // 5 MB-Puffer erstellen
    buffer := make([]byte, 15*1024*1024, 15*1024*1024)
    random := rand.New(rand.NewSource(time.Now().Unix()))
    random.Read(buffer)

    input := &s3manager.UploadInput{
        Bucket: aws.String(bucket),
        Key:    aws.String(key),
        Body:   io.ReadSeeker(bytes.NewReader(buffer)),
    }

    // Upload ausführen.
    d, _ := uploader.Upload(input)
    fmt.Println(d)

    // Upload mit Optionen ausführen, die sich von denen im Uploader unterscheiden.
    f, _ := uploader.Upload(input, func(u *s3manager.Uploader) {
        u.PartSize = 10 * 1024 * 1024 // Teilgröße 10
        u.LeavePartsOnError = true    // Teile nicht löschen, wenn Upload fehlschlägt.
    })
    fmt.Println(f)
}
```

### Erweiterte Liste abrufen
{: #go-list-buckets-extended}


```Go
func main() {
// Client erstellen
		sess := session.Must(session.NewSession())
		client := s3.New(sess, conf)


		input := new(s3.ListBucketsExtendedInput).SetMaxKeys(<MAX_KEYS>).SetMarker("<MARKER>").SetPrefix("<PREFIX>")
		output, _ := client.ListBucketsExtended(input)

		jsonBytes, _ := json.MarshalIndent(output, " ", " ")
		fmt.Println(string(jsonBytes))
}
```
*Schlüsselwerte*
* `<MAX_KEYS>` - Maximale Anzahl an Buckets, die in der Anforderung abgerufen werden sollen.
* `<MARKER>` - Der Bucketname zum Starten der Liste (bis zu diesem Bucket überspringen).
* `<PREFIX` - Nur Buckets einschließen, deren Name mit diesem Präfix beginnt.

### Erweiterte Liste mit Seitenaufteilung abrufen
{: #go-list-buckets-extended-pagination}

```Go
func main() {

	// Client erstellen
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)

    i := 0
    input := new(s3.ListBucketsExtendedInput).SetMaxKeys(<MAX_KEYS>).SetMarker("<MARKER>").SetPrefix("<PREFIX>")
	output, _ := client.ListBucketsExtended(input)

	for _, bucket := range output.Buckets {
		fmt.Println(i, "\t\t", *bucket.Name, "\t\t", *bucket.LocationConstraint, "\t\t", *bucket.CreationDate)
	}

}
```
*Schlüsselwerte*
* `<MAX_KEYS>` - Maximale Anzahl an Buckets, die in der Anforderung abgerufen werden sollen.
* `<MARKER>` - Der Bucketname zum Starten der Liste (bis zu diesem Bucket überspringen).
* `<PREFIX` - Nur Buckets einschließen, deren Name mit diesem Präfix beginnt.
