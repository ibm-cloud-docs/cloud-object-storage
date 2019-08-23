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

# Utilización de Go
{: #go}

El SDK de {{site.data.keyword.cos_full}} para Go es muy completo y ofrece características y prestaciones que no se describen en esta guía. Para obtener más información sobre clases y métodos, [consulte la documentación de Go](https://ibm.github.io/ibm-cos-sdk-go/). Encontrará el código fuente en el [repositorio GitHub](https://github.com/IBM/ibm-cos-sdk-go).

## Obtención del SDK
{: #go-get-sdk}

Utilice `go get` para recuperar el SDK para añadirlo al espacio de trabajo GOPATH o a las dependencias de módulo Go del proyecto. El SDK requiere una versión mínima de Go 1.10 y la versión máxima de Go 1.12. Se dará soporte a futuras versiones de Go cuando se complete el proceso de control de calidad.

```
go get github.com/IBM/ibm-cos-sdk-go
```

Para actualizar el SDK, utilice `go get -u` para recuperar la última versión del SDK.

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### Importación de paquetes
{: #go-import-packages}

Después de instalar el SDK, tendrá que importar los paquetes que necesite en las aplicaciones Go para utilizar el SDK, tal como se muestra en el ejemplo siguiente:
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## Creación de credenciales de cliente y de origen
{: #go-client-credentials}

Para conectarse a {{site.data.keyword.cos_full_notm}}, se crea y se configura un cliente proporcionando información de credenciales (clave de API e ID de instancia de servicio). Estos valores también se pueden tomar automáticamente de un archivo de credenciales o de variables de entorno. 

Puede encontrar las credenciales creando una [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) o a través de la CLI.

En la Figura 1 se muestra un ejemplo de cómo definir las variables de entorno en un tiempo de ejecución de aplicaciones en el portal de {{site.data.keyword.cos_full_notm}}. Las variables obligatorias son `IBM_API_KEY_ID`, que contiene la 'apikey' de la credencial de servicio, `IBM_SERVICE_INSTANCE_ID`, que contiene 'resource_instance_id' también de la credencial de servicio, y `IBM_AUTH_ENDPOINT` con un valor adecuado para la cuenta, como por ejemplo `https://iam.cloud.ibm.com/identity/token`. Si utiliza variables de entorno para definir las credenciales de la aplicación, utilice `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).` según proceda y sustituya el método similar utilizado en el ejemplo de configuración.

![environmentvariables](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="Figura 1. Variables de entorno"}

### Inicialización de la configuración
{: #go-init-config}

```Go

// Constantes para valores de IBM COS
const (
    apiKey            = "<API_KEY>"  // por ejemplo, "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ"
    serviceInstanceID = "<RESOURCE_INSTANCE_ID>" // "crn:v1:bluemix:public:iam-identity::a/3ag0e9402tyfd5d29761c3e97696b71n::serviceid:ServiceId-540a4a41-7322-4fdd-a9e7-e0cb7ab760f9"
    authEndpoint      = "https://iam.cloud.ibm.com/identity/token"
    serviceEndpoint   = "<SERVICE_ENDPOINT>" // por ejemplo, "https://s3.us.cloud-object-storage.appdomain.cloud"
    bucketLocation    = "<LOCATION>" // por ejemplo, "us"
)

// Crear config

conf := aws.NewConfig().
    WithRegion("us-standard").
    WithEndpoint(serviceEndpoint).
    WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
    WithS3ForcePathStyle(true)

```
Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Ejemplos de código
{: #go-code-examples}

### Creación de un nuevo grupo
{: #go-new-bucket}

Puede consultar la lista de códigos de suministro válidos para `LocationConstraint` en la [guía de Storage Classes](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```Go
func main() {

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Nombres de grupos
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

### Obtención de una lista de grupos disponibles
{: #go-list-buckets}

```Go
func main() {

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Llamar a función
    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}

```


### Carga de un objeto en un grupo
{: #go-put-object}

```Go
func main() {

    // Crear cliente
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

    // Llamar a función
    result, _ := client.PutObject(&input)
    fmt.Println(result)
}
```



### Obtención de la lista de elementos de un grupo
{: #go-list-objects}

```Go
func main() {

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Nombre de grupo
    bucket := "<BUCKET_NAME>"

    // Llamar a función
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

### Obtención del contenido de un objeto
{: #go-get-object}

```Go
func main() {
    
    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Variables
    bucketName := "<NEW_BUCKET_NAME>"
    key := "<OBJECT_KEY>"

    // los usuarios tendrán que crear grupo, clave (nombre de serie en texto plano)
    input := s3.GetObjectInput{
        Bucket: aws.String(bucketName),
        Key:    aws.String(key),
    }

    // Llamar a función
    res, _ := client.GetObject(&input)

    body, _ := ioutil.ReadAll(res.Body)
    fmt.Println(body)
}

```

### Supresión de un objeto de un grupo
{: #go-delete-object}

```Go
func main() {

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)
    
    // Nombre de grupo
    bucket := "<BUCKET_NAME>"
    
    input := &s3.DeleteObjectInput{
        Bucket: aws.String(bucket),
        Key:    aws.String("<OBJECT_KEY>"),
    }
    
    d, _ := client.DeleteObject(input)
    fmt.Println(d)
}
```


### Supresión de varios objetos de un grupo
{: #go-multidelete}

```Go
func main() {

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Nombre de grupo
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


### Supresión de un grupo
{: #go-delete-bucket}

```Go
func main() {

    // Nombre de grupo
    bucket := "<BUCKET_NAME>"

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    input := &s3.DeleteBucketInput{
        Bucket: aws.String(bucket),
    }
    d, _ := client.DeleteBucket(input)
    fmt.Println(d)
}
```



### Ejecución manual de una carga de varias partes
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

    // Crear cliente
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

## Utilización de Key Protect
{: #go-examples-kp}

Key Protect se puede añadir a un grupo de almacenamiento para gestionar claves de cifrado. Todos los datos están cifrados en IBM COS, pero Key Protect proporciona un servicio para generar, rotar y controlar el acceso a las claves de cifrado utilizando un servicio centralizado.

### Antes de empezar
{: #go-examples-kp-prereqs}

Se necesitan los elementos siguientes para crear un grupo con Key Protect habilitado:

* Un servicio de Key Protect [suministrado](/docs/services/key-protect?topic=key-protect-provision#provision)
* Una clave raíz disponible (ya sea [generada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) o [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperación del CRN de la clave raíz
{: #go-examples-kp-root}

1. Recupere el [ID de instancia](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) del servicio Key Protect
2. Utilice la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas las [claves disponibles](https://cloud.ibm.com/apidocs/key-protect)
    * Puede utilizar mandatos `curl` o un cliente de API REST, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acceder a la [API de Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere el CRN de la clave raíz que utilizará para activar Key Protect en el grupo. El CRN se parecerá al siguiente:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creación de un grupo con Key Protect habilitado
{: #go-examples-kp-new-bucket}

```Go
func main() {

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Nombres de grupo
    newBucket := "<NEW_BUCKET_NAME>"
        
    fmt.Println("Creating new encrypted bucket:", newBucket)

	input := &s3.CreateBucketInput{
		Bucket: aws.String(newBucket),
		IBMSSEKPCustomerRootKeyCrn: aws.String("<ROOT-KEY-CRN>"),
		IBMSSEKPEncryptionAlgorithm:aws.String("<ALGORITHM>"),
    }
    client.CreateBucket(input)

    // Obtener lista de grupos
    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```
*Valores de clave*
* `<NEW_BUCKET_NAME>`: el nombre del nuevo grupo.
* `<ROOT-KEY-CRN>`: el CRN de la clave raíz que se ha obtenido del servicio Key Protect.
* `<ALGORITHM>`: el algoritmo de cifrado utilizado para los nuevos objetos que se añaden al grupo (el valor predeterminado es AES256).


### Utilización del gestor de transferencias
{: #go-transfer}

```Go
func main() {

    // Variables
    bucket := "<BUCKET_NAME>"
    key := "<OBJECT_KEY>"

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // crear un cargador con cliente S3 y opciones personalizadas
    uploader := s3manager.NewUploaderWithClient(client, func(u *s3manager.Uploader) {
        u.PartSize = 5 * 1024 * 1024 // 64MB per part
    })

    // crear un almacenamiento intermedio de 5 MB
    buffer := make([]byte, 15*1024*1024, 15*1024*1024)
    random := rand.New(rand.NewSource(time.Now().Unix()))
    random.Read(buffer)

    input := &s3manager.UploadInput{
        Bucket: aws.String(bucket),
        Key:    aws.String(key),
        Body:   io.ReadSeeker(bytes.NewReader(buffer)),
    }

    // Ejecutar una carga.
    d, _ := uploader.Upload(input)
    fmt.Println(d)
    
    // Ejecutar una carga con opciones distintas de las del programa Uploader.
    f, _ := uploader.Upload(input, func(u *s3manager.Uploader) {
        u.PartSize = 10 * 1024 * 1024 // Tamaño de parte de 10 MB
        u.LeavePartsOnError = true    // No suprimir las partes si la carga falla.
    })
    fmt.Println(f)
}
```

### Obtención de una lista ampliada
{: #go-list-buckets-extended}


```Go
func main() {
// Crear cliente
		sess := session.Must(session.NewSession())
		client := s3.New(sess, conf)


		input := new(s3.ListBucketsExtendedInput).SetMaxKeys(<MAX_KEYS>).SetMarker("<MARKER>").SetPrefix("<PREFIX>")
		output, _ := client.ListBucketsExtended(input)

		jsonBytes, _ := json.MarshalIndent(output, " ", " ")
		fmt.Println(string(jsonBytes))
}
```
*Valores de clave*
* `<MAX_KEYS>`: número máximo de grupos que se van a recuperar en la solicitud.
* `<MARKER>`: el nombre del grupo a partir del cual comenzar la lista (saltar hasta este grupo).
* `<PREFIX`: incluir solo los grupos cuyo nombre empiece por este prefijo.

### Obtención de una lista ampliada con paginación
{: #go-list-buckets-extended-pagination}

```Go
func main() {

	// Crear cliente
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
*Valores de clave*
* `<MAX_KEYS>`: número máximo de grupos que se van a recuperar en la solicitud.
* `<MARKER>`: el nombre del grupo a partir del cual comenzar la lista (saltar hasta este grupo).
* `<PREFIX`: incluir solo los grupos cuyo nombre empiece por este prefijo.
