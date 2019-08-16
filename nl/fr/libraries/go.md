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

# Utilisation de Go
{: #go}

Le SDK {{site.data.keyword.cos_full}} pour Go est complet et comprend des fonctions qui ne sont pas décrites dans le présent guide. Pour obtenir une documentation détaillée sur les classes et les méthodes, [voir la documentation Go](https://ibm.github.io/ibm-cos-sdk-go/). Le code source se trouve dans le [référentiel GitHub](https://github.com/IBM/ibm-cos-sdk-go).

## Obtention du SDK
{: #go-get-sdk}

Utilisez la commande `go get` pour extraire le SDK afin de l'ajouter à votre espace de travail GOPATH, ou les dépendances de module Go du projet. Le SDK requiert la version minimum Go 1.10 et la version maximum Go 1.12. Les versions ultérieures de Go seront prises en charge une fois que notre processus de contrôle de la qualité aura été achevé. 

```
go get github.com/IBM/ibm-cos-sdk-go
```

Pour mettre à jour le SDK, utilisez la commande `go get -u` pour extraire la version la plus récente du SDK. 

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### Importation de packages
{: #go-import-packages}

Après avoir installé le SDK, vous devrez importer les packages nécessaires dans vos applications Go pour l'utiliser, comme illustré dans l'exemple suivant : 
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## Création d'un client et sourçage de données d'identification
{: #go-client-credentials}

Pour permettre l'établissement d'une connexion à {{site.data.keyword.cos_full_notm}}, un client est créé et configuré en fournissant des données d'identification (clé d'API et ID d'instance de service). Ces valeurs peuvent aussi être automatiquement sourcées à partir d'un fichier de données d'identification ou à partir de variables d'environnement.  

Vous pouvez trouver les données d'identification en créant des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) ou via l'interface CLI. 

La figure 1 illustre un exemple de définition de variables d'environnement dans un contexte d'exécution d'application sur le portail {{site.data.keyword.cos_full_notm}}. Les variables requises sont `IBM_API_KEY_ID`, qui contient votre clé d'API ('apikey') de données d'identification de service, `IBM_SERVICE_INSTANCE_ID`, qui contient l'ID d'instance de ressource ('resource_instance_id') provenant également de vos données d'identification de service et `IBM_AUTH_ENDPOINT`, qui est dotée d'une valeur appropriée pour votre compte, par exemple, `https://iam.cloud.ibm.com/identity/token`. Si vous avez recours à des variables d'environnement pour définir vos données d'identification d'application, utilisez `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).` si besoin, en remplaçant la méthode similaire utilisée dans l'exemple de configuration. 

![environmentvariables](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="Figure 1. Variables d'environnement" caption-side="top"}

### Initialisation de la configuration
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
Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Exemples de code
{: #go-code-examples}

### Création d'un nouveau compartiment
{: #go-new-bucket}

Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

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

### Création de la liste des compartiments disponibles
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


### Envoi par téléchargement d'un objet à un compartiment
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



### Création de la liste des éléments contenus dans un compartiment
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

### Obtention du contenu d'un objet
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

### Suppression d'un objet d'un compartiment
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


### Suppression de plusieurs objets d'un compartiment
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


### Suppression d'un compartiment
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



### Exécution manuelle d'un envoi par téléchargement en plusieurs parties
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

## Utilisation de Key Protect
{: #go-examples-kp}

Key Protect peut être ajouté à un compartiment de stockage pour gérer les clés de chiffrement. Toutes les données sont chiffrées dans IBM COS, mais Key Protect fournit des fonctions de génération, de rotation et de contrôle de l'accès aux clés de chiffrement à l'aide d'un service centralisé. 

### Avant de commencer
{: #go-examples-kp-prereqs}

Les éléments suivants sont nécessaires pour créer un compartiment avec Key Protect activé :

* Un service Key Protect doit être [mis à disposition](/docs/services/key-protect?topic=key-protect-provision#provision). 
* Une clé racine doit être disponible ([générée](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importée](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)). 

### Extraction du CRN de clé racine
{: #go-examples-kp-root}

1. Extrayez l'[ID d'instance](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) pour votre service Key Protect. 
2. Utilisez l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) pour extraire toutes vos [clés disponibles](https://cloud.ibm.com/apidocs/key-protect). 
    * Vous pouvez utiliser des commandes `curl` ou un client REST API, tel que [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), pour accéder à l'[API Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api). 
3. Extrayez le CRN de la clé racine que vous utiliserez pour activer Key Protect sur votre compartiment. Le CRN se présentera comme suit :

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Création d'un compartiment avec Key Protect activé
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
*Valeurs de clé*
* `<NEW_BUCKET_NAME>` - Nom du nouveau compartiment. 
* `<ROOT-KEY-CRN>` - CRN de la clé racine qui est obtenue auprès du service Key Protect. 
* `<ALGORITHM>` - Algorithme de chiffrement pour les nouveaux objets ajoutés au compartiment (la valeur par défaut est AES256). 


### Utilisation de la classe TransferManager
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

### Obtention d'une liste étendue
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
*Valeurs de clé*
* `<MAX_KEYS>` - Nombre maximum de compartiments à extraire dans la demande. 
* `<MARKER>` - Nom de compartiment à partir duquel démarrer la création de la liste (ignorer jusqu'à ce compartiment). 
* `<PREFIX` - Inclure uniquement les compartiments dont le nom débute avec ce préfixe. 

### Obtention d'une liste étendue avec pagination
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
*Valeurs de clé*
* `<MAX_KEYS>` - Nombre maximum de compartiments à extraire dans la demande. 
* `<MARKER>` - Nom de compartiment à partir duquel démarrer la création de la liste (ignorer jusqu'à ce compartiment). 
* `<PREFIX` - Inclure uniquement les compartiments dont le nom débute avec ce préfixe. 
