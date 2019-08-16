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

# Usando Go
{: #go}

O {{site.data.keyword.cos_full}} SDK for Go é abrangente e tem recursos e capacidades não descritos neste guia. Para obter a documentação detalhada de classe o método, [consulte os Docs do Go](https://ibm.github.io/ibm-cos-sdk-go/). O código-fonte pode ser localizado no [Repositório GitHub](https://github.com/IBM/ibm-cos-sdk-go).

## Obtendo o SDK
{: #go-get-sdk}

Use `go get` para recuperar o SDK para incluí-lo em sua área de trabalho GOPATH ou nas dependências do módulo Go do projeto. O SDK requer uma versão mínima do Go 1.10 e a versão máxima do Go 1.12. As futuras versões do Go serão suportadas quando o nosso processo de controle de qualidade tiver sido concluído.

```
go get github.com/IBM/ibm-cos-sdk-go
```

Para atualizar o SDK, use `go get -u` para recuperar a versão mais recente do SDK.

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### Importar pacotes
{: #go-import-packages}

Depois de ter instalado o SDK, será necessário importar os pacotes que você requer em seus aplicativos Go para usar o SDK, conforme mostrado no exemplo a seguir:
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## Criando um cliente e credenciais de fornecimento
{: #go-client-credentials}

Para se conectar ao {{site.data.keyword.cos_full_notm}}, um cliente é criado e configurado fornecendo informações de credenciais (Chave de API e ID da instância de serviço). Esses valores também podem ser originados automaticamente de um arquivo de credenciais ou de variáveis de ambiente. 

As credenciais podem ser localizadas criando uma [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) ou por meio da CLI.

A Figura 1 mostra um exemplo de como definir variáveis de ambiente em um tempo de execução do aplicativo no portal do {{site.data.keyword.cos_full_notm}}. As variáveis necessárias são `IBM_API_KEY_ID` contendo sua Credencial de serviço 'apikey', `IBM_SERVICE_INSTANCE_ID` contendo o 'resource_instance_id' também de sua Credencial de serviço e um `IBM_AUTH_ENDPOINT` com um valor apropriado para sua conta, como `https://iam.cloud.ibm.com/identity/token`. Se estiver usando variáveis de ambiente para definir suas credenciais do aplicativo, use `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).` conforme apropriado, substituindo o método semelhante usado no exemplo de configuração.

![environmentvariables](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="Figura 1. Variáveis de ambiente" caption-side="top"}

### Inicializando a configuração
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
Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Exemplos de código
{: #go-code-examples}

### Criando um novo depósito
{: #go-new-bucket}

Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

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

### Listar depósitos disponíveis
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


### Fazer upload de um objeto em um depósito
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



### Listar itens em um depósito
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

### Obter conteúdo de um objeto
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

### Excluir um objeto de um depósito
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


### Excluir múltiplos objetos de um depósito
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


### Excluir um depósito
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



### Executar um upload manual de múltiplas partes
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

## Usando o Key Protect
{: #go-examples-kp}

O Key Protect pode ser incluído em um depósito de armazenamento para gerenciar chaves de criptografia. Todos os dados são criptografados no IBM COS, mas o Key Protect fornece um serviço para gerar, girar e controlar o acesso às chaves de criptografia usando um serviço centralizado.

### Antes de iniciar
{: #go-examples-kp-prereqs}

Os itens a seguir são necessários para criar um depósito com o Key-Protect ativado:

* Um serviço Key Protect [provisionado](/docs/services/key-protect?topic=key-protect-provision#provision)
* Uma chave Raiz disponível ([gerada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperando o CRN da chave raiz
{: #go-examples-kp-root}

1. Recupere o [ID da instância](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) para seu serviço Key Protect
2. Use a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas as suas [chaves disponíveis](https://cloud.ibm.com/apidocs/key-protect)
    * É possível usar comandos `curl` ou um Cliente REST de API, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acessar a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere o CRN da chave raiz que você usará para ativar o Key Protect no seu depósito. O CRN será semelhante ao abaixo:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Criando um depósito com o Key Protect ativado
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
*Valores da chave*
* `<NEW_BUCKET_NAME>` - o nome do novo depósito.
* `<ROOT-KEY-CRN>` - o CRN da Chave raiz que é obtida do serviço Key Protect.
* `<ALGORITHM>` - o algoritmo de criptografia usado para novos objetos incluídos no depósito (o padrão é AES256).


### Usar o gerenciador de transferência
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

### Obtendo uma listagem ampliada
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
*Valores da chave*
* `<MAX_KEYS>` - o número máximo de depósitos a serem recuperados na solicitação.
* `<MARKER>` - o nome do depósito para iniciar a listagem (ignorar até esse depósito).
* `<PREFIX` - inclua somente depósitos cujo nome inicie com esse prefixo.

### Obtendo uma listagem ampliada com paginação
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
*Valores da chave*
* `<MAX_KEYS>` - o número máximo de depósitos a serem recuperados na solicitação.
* `<MARKER>` - o nome do depósito para iniciar a listagem (ignorar até esse depósito).
* `<PREFIX` - inclua somente depósitos cujo nome inicie com esse prefixo.
