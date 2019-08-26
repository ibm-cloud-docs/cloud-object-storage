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

# Utilizzo di Go
{: #go}

L'SDK {{site.data.keyword.cos_full}} per Go è completo e presenta caratteristiche e funzioni non descritte in questa guida. Per una documentazione dettagliata di classi e metodi, [consulta la documentazione di Go](https://ibm.github.io/ibm-cos-sdk-go/). Il codice sorgente è disponibile nel [repository GitHub](https://github.com/IBM/ibm-cos-sdk-go).

## Ottenimento dell'SDK
{: #go-get-sdk}

Utilizza `go get` per richiamare l'SDK per aggiungerlo al tuo spazio di lavoro GOPATH o alle dipendenze di modulo Go del progetto. L'SDK richiede una versione minima di Go 1.10 e una versione massima di Go 1.12. Future versioni di Go saranno supportate dopo che sarà stato completato il processo di controllo della qualità.

```
go get github.com/IBM/ibm-cos-sdk-go
```

Per aggiornare l'SDK, utilizza `go get -u` per richiamare la versione più recente dell'SDK. 

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### Importa pacchetti
{: #go-import-packages}

Dopo che hai installato l'SDK, dovrai importare i pacchetti di cui hai bisogno nelle tue applicazioni Go per utilizzare l'SDK, come mostrato nel seguente esempio:
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## Creazione di un client e derivazione delle credenziali
{: #go-client-credentials}

Per stabilire una connessione a {{site.data.keyword.cos_full_notm}}, un client viene creato e configurato fornendo le informazioni delle credenziali (chiave API e ID istanza del servizio). Questi valori possono anche essere derivati automaticamente da un file di credenziali o dalle variabili di ambiente. 

Le credenziali possono essere trovate creando una [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) o tramite la CLI.

La Figura 1 mostra un esempio di come definire le variabili di ambiente in un runtime dell'applicazione nel portale {{site.data.keyword.cos_full_notm}}. Le variabili obbligatorie sono `IBM_API_KEY_ID`, che contiene la tua credenziale del servizio 'apikey', `IBM_SERVICE_INSTANCE_ID`, che contiene il 'resource_instance_id' anche dalla tua credenziale del servizio, e `IBM_AUTH_ENDPOINT`, con un valore appropriato per il tuo account, come `https://iam.cloud.ibm.com/identity/token`. Se utilizzi le variabili di ambiente per definire le tue credenziali dell'applicazione, utilizza `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).` come appropriato, sostituendo il metodo simile utilizzato nell'esempio di configurazione.

![environmentvariables](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="Figura 1. Variabili di ambiente"}

### Inizializzazione della configurazione
{: #go-init-config}

```Go

// Constants for IBM COS values
const (
    apiKey            = "<API_KEY>"  // eg "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ"
    serviceInstanceID = "<RESOURCE_INSTANCE_ID>" // "crn:v1:bluemix:public:iam-identity::a/3ag0e9402tyfd5d29761c3e97696b71n::serviceid:ServiceId-540a4a41-7322-4fdd-a9e7-e0cb7ab760f9"
    authEndpoint      = "https://iam.cloud.ibm.com/identity/token"
    serviceEndpoint   = "<SERVICE_ENDPOINT>" // eg "https://s3.us.cloud-object-storage.appdomain.cloud"
    bucketLocation    = "<LOCATION>" // eg "us"
)

// Create config

conf := aws.NewConfig().
    WithRegion("us-standard").
    WithEndpoint(serviceEndpoint).
    WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
    WithS3ForcePathStyle(true)

```
Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Esempi di codice
{: #go-code-examples}

### Creazione di un nuovo bucket
{: #go-new-bucket}

È possibile che si faccia riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida alle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Names
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

### Elenca i bucket disponibili
{: #go-list-buckets}

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Call Function
    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}

```


### Carica un oggetto in un bucket
{: #go-put-object}

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Variables
    bucketName := "<NEW_BUCKET_NAME>"
    key := "<OBJECT_KEY>"
    content := bytes.NewReader([]byte("<CONTENT>"))

    input := s3.PutObjectInput{
        Bucket:        aws.String(bucketName),
        Key:           aws.String(key),
        Body:          content,
    }

    // Call Function
    result, _ := client.PutObject(&input)
    fmt.Println(result)
}
```



### Elenca gli elementi in un bucket
{: #go-list-objects}

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Name
    bucket := "<BUCKET_NAME>"

    // Call Function
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

### Ottieni il contenuto di un oggetto
{: #go-get-object}

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Variables
    bucketName := "<NEW_BUCKET_NAME>"
    key := "<OBJECT_KEY>"

    // users will need to create bucket, key (flat string name)
    input := s3.GetObjectInput{
        Bucket: aws.String(bucketName),
        Key:    aws.String(key),
    }

    // Call Function
    res, _ := client.GetObject(&input)

    body, _ := ioutil.ReadAll(res.Body)
    fmt.Println(body)
}

```

### Elimina un oggetto da un bucket
{: #go-delete-object}

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Name
    bucket := "<BUCKET_NAME>"

    input := &s3.DeleteObjectInput{
        Bucket: aws.String(bucket),
        Key:    aws.String("<OBJECT_KEY>"),
    }

    d, _ := client.DeleteObject(input)
    fmt.Println(d)
}
```


### Elimina più oggetti da un bucket
{: #go-multidelete}

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Name
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


### Elimina un bucket
{: #go-delete-bucket}

```Go
func main() {

    // Bucket Name
    bucket := "<BUCKET_NAME>"

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    input := &s3.DeleteBucketInput{
        Bucket: aws.String(bucket),
    }
    d, _ := client.DeleteBucket(input)
    fmt.Println(d)
}
```



### Esegui un caricamento a più parti manuale
{: #go-multipart}

```Go	
func main() {

    // Variables
    bucket := "<BUCKET_NAME>"
    key := "<OBJECT_KEY>"
    content := bytes.NewReader([]byte("<CONTENT>"))

    input := s3.CreateMultipartUploadInput{
        Bucket: aws.String(bucket),
        Key:    aws.String(key),
    }

    // Create client
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

## Utilizzo di Key Protect
{: #go-examples-kp}

Key Protect può essere aggiunto a un bucket di archiviazione per gestire le chiavi di crittografia. Tutti i dati vengono crittografati in IBM COS ma Key Protect fornisce un servizio per generare, ruotare e controllare l'accesso alle chiavi di crittografia utilizzando un servizio centralizzato.

### Prima di cominciare
{: #go-examples-kp-prereqs}

I seguenti elementi sono necessari per creare un bucket con Key-Protect abilitato:

* Un servizio Key Protect [di cui è stato eseguito il provisioning](/docs/services/key-protect?topic=key-protect-provision#provision)
* Una chiave root disponibile ([generata](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importata](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Richiamo del CRN della chiave root
{: #go-examples-kp-root}

1. Richiama l'[ID istanza](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) per il tuo servizio Key Protect
2. Utilizza l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) per richiamare tutte le tue [chiavi disponibili](https://cloud.ibm.com/apidocs/key-protect)
    * Puoi utilizzare i comandi `curl` o un client REST API come [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) per accedere all'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Richiama il CRN della chiave root che utilizzerai per abilitare Key Protect sul tuo bucket. Il CRN sarà simile al seguente:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creazione di un bucket con Key Protect abilitato
{: #go-examples-kp-new-bucket}

```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Names
    newBucket := "<NEW_BUCKET_NAME>"

    fmt.Println("Creating new encrypted bucket:", newBucket)

	input := &s3.CreateBucketInput{
		Bucket: aws.String(newBucket),
		IBMSSEKPCustomerRootKeyCrn: aws.String("<ROOT-KEY-CRN>"),
		IBMSSEKPEncryptionAlgorithm:aws.String("<ALGORITHM>"),
    }
    client.CreateBucket(input)

    // List Buckets
    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```
*Valori chiave*
* `<NEW_BUCKET_NAME>` - il nome del nuovo bucket.
* `<ROOT-KEY-CRN>` - CRN della chiave root ottenuta dal servizio Key Protect.
* `<ALGORITHM>` - l'algoritmo di crittografia utilizzato per i nuovi oggetti aggiunti al bucket (il valore predefinito è AES256).


### Utilizza il gestore trasferimenti
{: #go-transfer}

```Go
func main() {

    // Variables
    bucket := "<BUCKET_NAME>"
    key := "<OBJECT_KEY>"

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Create an uploader with S3 client and custom options
    uploader := s3manager.NewUploaderWithClient(client, func(u *s3manager.Uploader) {
        u.PartSize = 5 * 1024 * 1024 // 64MB per part
    })

    // make a buffer of 5MB
    buffer := make([]byte, 15*1024*1024, 15*1024*1024)
    random := rand.New(rand.NewSource(time.Now().Unix()))
    random.Read(buffer)

    input := &s3manager.UploadInput{
        Bucket: aws.String(bucket),
        Key:    aws.String(key),
        Body:   io.ReadSeeker(bytes.NewReader(buffer)),
    }

    // Perform an upload.
    d, _ := uploader.Upload(input)
    fmt.Println(d)

    // Perform upload with options different than the those in the Uploader.
    f, _ := uploader.Upload(input, func(u *s3manager.Uploader) {
        u.PartSize = 10 * 1024 * 1024 // 10MB part size
        u.LeavePartsOnError = true    // Don't delete the parts if the upload fails.
    })
    fmt.Println(f)
}
```

### Ottenimento di un elenco esteso
{: #go-list-buckets-extended}


```Go
func main() {
// Create client
		sess := session.Must(session.NewSession())
		client := s3.New(sess, conf)


		input := new(s3.ListBucketsExtendedInput).SetMaxKeys(<MAX_KEYS>).SetMarker("<MARKER>").SetPrefix("<PREFIX>")
		output, _ := client.ListBucketsExtended(input)

		jsonBytes, _ := json.MarshalIndent(output, " ", " ")
		fmt.Println(string(jsonBytes))
}
```
*Valori chiave*
* `<MAX_KEYS>` - numero massimo di bucket da richiamare nella richiesta.
* `<MARKER>` - il nome del bucket di inizio dell'elenco (salta fino a questo bucket)
* `<PREFIX` - includi solo i bucket il cui nome inizia con questo prefisso.

### Ottenimento di un elenco esteso con la paginazione
{: #go-list-buckets-extended-pagination}

```Go
func main() {

	// Create client
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
*Valori chiave*
* `<MAX_KEYS>` - numero massimo di bucket da richiamare nella richiesta.
* `<MARKER>` - il nome del bucket di inizio dell'elenco (salta fino a questo bucket)
* `<PREFIX` - includi solo i bucket il cui nome inizia con questo prefisso.
