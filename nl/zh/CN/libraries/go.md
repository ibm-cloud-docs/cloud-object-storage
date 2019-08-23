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

{{site.data.keyword.cos_full}} SDK for Go 综合全面，并且具有本指南中未描述的功能。有关详细的类和方法文档，请[参阅 Go 文档](https://ibm.github.io/ibm-cos-sdk-go/)。在 [GitHub 存储库](https://github.com/IBM/ibm-cos-sdk-go)中可以找到源代码。

## 获取 SDK
{: #go-get-sdk}

使用 `go get` 可检索 SDK 以将其添加到 GOPATH 工作空间，或检索项目的 Go 模块依赖项。SDK 需要的最低版本为 Go 1.10，最高版本为 Go 1.12。我们在质量控制过程完成后，将支持未来版本的 Go。

```
go get github.com/IBM/ibm-cos-sdk-go
```

要更新 SDK，请使用 `go get -u` 来检索最新版本的 SDK。

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### 导入包
{: #go-import-packages}

安装 SDK 后，需要将所需的包导入到 Go 应用程序才可使用 SDK，如以下示例所示：
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## 创建客户机和获取凭证
{: #go-client-credentials}

为了连接到 {{site.data.keyword.cos_full_notm}}，将通过提供凭证信息（API 密钥和服务实例标识）来创建和配置客户机。这些值还可以自动从凭证文件或环境变量中获取。 

通过创建[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)或通过 CLI 可以找到凭证。

图 1 显示了如何在 {{site.data.keyword.cos_full_notm}} 门户网站中定义应用程序运行时中的环境变量的示例。必需的变量为 `IBM_API_KEY_ID`（包含服务凭证的“apikey”）、`IBM_SERVICE_INSTANCE_ID`（用于保存同样来自服务凭证的“resource_instance_id”）和 `IBM_AUTH_ENDPOINT`（值与帐户相对应，如 `https://iam.cloud.ibm.com/identity/token`）。如果使用环境变量来定义应用程序凭证，请视情形使用 `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).`，并替换配置示例中使用的类似方法。

![环境变量](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="图 1. 环境变量"}

### 初始化配置
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
有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

## 代码示例
{: #go-code-examples}

### 创建新存储区
{: #go-new-bucket}

在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中可以参考 `LocationConstraint` 的有效供应代码的列表。

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

### 列出可用存储区
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


### 将对象上传到存储区
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



### 列出存储区中的项
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

### 获取对象的内容
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

### 从存储区中删除一个对象
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


### 从存储区中删除多个对象
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


### 删除存储区
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



### 执行手动分块上传
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

可以将 Key Protect 添加到存储区，以管理加密密钥。所有数据都在 IBM COS 中进行加密，但 Key Protect 提供的是使用集中服务来生成和循环加密密钥以及控制加密密钥访问权的服务。

### 开始之前
{: #go-examples-kp-prereqs}

要创建启用了 Key Protect 的存储区，需要以下各项：

* [已供应](/docs/services/key-protect?topic=key-protect-provision#provision) Key Protect 服务
* 根密钥可用（[已生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已导入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 检索根密钥 CRN
{: #go-examples-kp-root}

1. 检索 Key Protect 服务的[实例标识](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)。
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 来检索所有[可用密钥](https://cloud.ibm.com/apidocs/key-protect)。
    * 可以使用 `curl` 命令或 API REST 客户机（例如，[Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）来访问 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 检索将用于在存储区上启用 Key Protect 的根密钥的 CRN。CRN 类似于以下内容：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 创建启用了 Key Protect 的存储区
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
*键值*
* `<NEW_BUCKET_NAME>` - 新存储区的名称。
* `<ROOT-KEY-CRN>` - 从 Key Protect 服务获取的根密钥的 CRN。
* `<ALGORITHM>` - 用于添加到存储区的新对象的加密算法（缺省值为 AES256）。


### 使用传输管理器
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

### 获取扩展列表
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
*键值*
* `<MAX_KEYS>` - 要在请求中检索的最大存储区数。
* `<MARKER>` - 列表中起始位置的存储区名称（跳过此存储区之前的存储区）。
* `<PREFIX` - 仅包含名称以此前缀开头的存储区。

### 使用分页获取扩展列表
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
*键值*
* `<MAX_KEYS>` - 要在请求中检索的最大存储区数。
* `<MARKER>` - 列表中起始位置的存储区名称（跳过此存储区之前的存储区）。
* `<PREFIX` - 仅包含名称以此前缀开头的存储区。
