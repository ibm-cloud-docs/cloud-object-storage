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

# 使用 Go
{: #go}

{{site.data.keyword.cos_full}} SDK for Go 包羅萬象，且具有本手冊中未說明的特性和功能。如需詳細的類別及方法文件，[請參閱 Go 文件](https://ibm.github.io/ibm-cos-sdk-go/)。原始碼可以在 [GitHub 儲存庫](https://github.com/IBM/ibm-cos-sdk-go)中找到。

## 取得 SDK
{: #go-get-sdk}

請使用 `go get` 來擷取 SDK，以將它新增至 GOPATH 工作區，或專案的 Go 模組相依關係。SDK 需要最低 Go 1.10 版，以及最高 Go 1.12 版。當我們的品質控制處理程序完成後，未來的 Go 版本將受到支援。

```
go get github.com/IBM/ibm-cos-sdk-go
```

若要更新 SDK，請使用 `go get -u` 來擷取最新版本的 SDK。

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### 匯入套件
{: #go-import-packages}

安裝 SDK 之後，您需要將您需要的套件匯入到 Go 應用程式以便使用 SDK，如下列範例所示：
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## 建立用戶端及讀取認證
{: #go-client-credentials}

為了連接至 {{site.data.keyword.cos_full_notm}}，會藉由提供認證資訊（API 金鑰及服務實例 ID），來建立及配置用戶端。這些值也可以自動從 credentials 檔案或環境變數中取得。 

可以藉由建立[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)，或透過 CLI 找到認證。

圖 1 顯示如何在 {{site.data.keyword.cos_full_notm}} 入口網站中，在應用程式運行環境裡定義環境變數的範例。必要變數為 `IBM_API_KEY_ID`（包含您的服務認證 'apikey'）、`IBM_SERVICE_INSTANCE_ID`（保存同樣來之您服務認證的 'resource_instance_id'），以及 `IBM_AUTH_ENDPOINT` 和適合您帳戶的值，例如 `https://iam.cloud.ibm.com/identity/token`。如果使用環境變數來定義您的應用程式認證，請適當地使用 `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).`，並取代配置範例中使用的類似方法。

![環境變數](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="圖 1. 環境變數" caption-side="top"}

### 起始設定配置
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
如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

## 程式碼範例
{: #go-code-examples}

### 建立新的儲存區
{: #go-new-bucket}

可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中參閱 `LocationConstraint` 的有效佈建碼清單。

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

### 列出可用的儲存區
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


### 將物件上傳至儲存區
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



### 列出儲存區中的項目
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

### 取得物件的內容
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

### 從儲存區刪除物件
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


### 從儲存區刪除多個項目
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


### 刪除儲存區
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



### 執行手動多部分上傳
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

## 使用 Key Protect
{: #go-examples-kp}

Key Protect 可新增至儲存空間儲存區，以管理加密金鑰。在 IBM COS 中所有資料都已加密，但 Key Protect 提供了一項服務，來使用集中化服務產生、替換及控制對加密金鑰的存取權。

### 開始之前
{: #go-examples-kp-prereqs}

為了建立儲存區並啟用 Key Protect，需要下列項目：

* [已佈建](/docs/services/key-protect?topic=key-protect-provision#provision) Key Protect 服務
* 根金鑰可用（[已產生](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已匯入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 擷取根金鑰 CRN
{: #go-examples-kp-root}

1. 擷取 Key Protect 服務的[實例 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 來擷取所有[可用金鑰](https://cloud.ibm.com/apidocs/key-protect)
    * 您可以使用 `curl` 指令或 API REST 用戶端（例如 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）來存取 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 擷取您將使用來在儲存區上啟用 Key Protect 的根金鑰 CRN。CRN 看起來如下：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 建立儲存區並啟用 Key Protect
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
*金鑰值*
* `<NEW_BUCKET_NAME>` - 新儲存區的名稱。
* `<ROOT-KEY-CRN>` - 從 Key Protect 服務取得的「根金鑰」的 CRN。
* `<ALGORITHM>` - 用於新增至儲存區的新物件加密演算法（預設為 AES256）。


### 使用傳送管理程式
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

### 取得延伸清單
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
*金鑰值*
* `<MAX_KEYS>` - 在要求中要擷取的儲存區數目上限。
* `<MARKER>` - 開始清單的儲存區名稱（跳過直到到達此儲存區為止）。
* `<PREFIX` - 僅包含名稱開頭為這個字首的儲存區。

### 取得具有分頁的延伸清單
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
*金鑰值*
* `<MAX_KEYS>` - 在要求中要擷取的儲存區數目上限。
* `<MARKER>` - 開始清單的儲存區名稱（跳過直到到達此儲存區為止）。
* `<PREFIX` - 僅包含名稱開頭為這個字首的儲存區。
