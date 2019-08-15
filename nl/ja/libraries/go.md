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

# Go の使用
{: #go}

{{site.data.keyword.cos_full}} SDK for Go の機能は広範囲に渡り、このガイドに記載されていない機能もあります。クラスおよびメソッドの詳細な資料については、[Go の資料](https://ibm.github.io/ibm-cos-sdk-go/)を参照してください。ソース・コードは、[GitHub リポジトリー](https://github.com/IBM/ibm-cos-sdk-go)にあります。

## SDK の取得
{: #go-get-sdk}

`go get` を使用して SDK を取得し、GOPATH ワークスペースに追加するか、プロジェクトの Go モジュール依存関係に追加します。SDK に必要な最小バージョンは Go 1.10 で、サポートされる最大バージョンは Go 1.12 です。Go の将来のバージョンは、品質管理プロセスが完了し次第サポートされます。

```
go get github.com/IBM/ibm-cos-sdk-go
```

SDK を更新するには、`go get -u` を使用して、SDK の最新バージョンを取得します。

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### パッケージのインポート
{: #go-import-packages}

SDK のインストールが完了したら、以下の例に示すように、SDK を使用するために必要なパッケージを Go アプリケーションにインポートする必要があります。
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## クライアントの作成と資格情報の入手
{: #go-client-credentials}

{{site.data.keyword.cos_full_notm}} に接続するために、資格情報 (API キーとサービス・インスタンス ID) を指定することによりクライアントが作成および構成されます。これらの値は、資格情報ファイルまたは環境変数から自動的に入手することもできます。 

資格情報は、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)を作成するか、CLI を介して見つけることができます。

図 1 は、{{site.data.keyword.cos_full_notm}} ポータルでアプリケーション・ランタイムに環境変数を定義する方法の例を示しています。必要な変数は、サービス資格情報の「apikey」を含んでいる `IBM_API_KEY_ID`、同じくサービス資格情報の「resource_instance_id」を保持する `IBM_SERVICE_INSTANCE_ID`、およびご使用のアカウントに適した値 (例えば、`https://iam.cloud.ibm.com/identity/token`) が設定された `IBM_AUTH_ENDPOINT` です。環境変数を使用してアプリケーション資格情報を定義する場合は、`WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).` を、構成の例で使用されている類似メソッドを置き換えながら適宜使用してください。

![環境変数](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="図 1. 環境変数" caption-side="top"}

### 構成の初期化
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
エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

## コード例
{: #go-code-examples}

### 新規バケットの作成
{: #go-new-bucket}

`LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラスのガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)を参照してください。

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

### 使用可能なバケットのリスト
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


### バケットへのオブジェクトのアップロード
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



### バケット内の項目のリスト
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

### オブジェクトのコンテンツの取得
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

### バケットからオブジェクトを削除
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


### バケットから複数オブジェクトを削除
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


### バケットの削除
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



### 手動の複数パーツ・アップロードの実行
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

## Key Protect の使用
{: #go-examples-kp}

暗号鍵を管理するために、Key Protect をストレージ・バケットに追加できます。IBM COS ではすべてのデータが暗号化されますが、Key Protect が提供するサービスは、集中型サービスを使用して暗号鍵を生成したり、ローテーションしたり、暗号鍵へのアクセスを制御したりするためのものです。

### 始める前に
{: #go-examples-kp-prereqs}

Key-Protect が有効なバケットを作成するためには、以下の項目が必要です。

* [プロビジョン済み](/docs/services/key-protect?topic=key-protect-provision#provision)の Key Protect サービス
* 使用可能なルート・キー ([生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)されたか、[インポート](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)されたもの)

### ルート・キー CRN の取得
{: #go-examples-kp-root}

1. Key Protect サービスの[インスタンス ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) を取得します。
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) を使用して、すべての[使用可能なキー](https://cloud.ibm.com/apidocs/key-protect)を取得します。
    * `curl` コマンドまたは API REST クライアント ([Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) など) のいずれかを使用して、[Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) にアクセスできます。
3. バケットで Key Protect を有効にするために使用するルート・キーの CRN を取得します。CRN は、下記のようになります。

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect が有効なバケットの作成
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
*キー値*
* `<NEW_BUCKET_NAME>` - 新規バケットの名前。
* `<ROOT-KEY-CRN>` - Key Protect サービスから取得したルート・キーの CRN。
* `<ALGORITHM>` - バケットに追加される新規オブジェクトに使用される暗号化アルゴリズム (デフォルトは AES256)。


### Transfer Manager の使用
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

### 拡張リストの取得
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
*キー値*
* `<MAX_KEYS>` - 要求で取得するバケットの最大数。
* `<MARKER>` - リストを開始するバケット名 (このバケットまでスキップ)。
* `<PREFIX` - この接頭部で始まる名前のバケットのみを含めます。

### ページ編集付きの拡張リストの取得
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
*キー値*
* `<MAX_KEYS>` - 要求で取得するバケットの最大数。
* `<MARKER>` - リストを開始するバケット名 (このバケットまでスキップ)。
* `<PREFIX` - この接頭部で始まる名前のバケットのみを含めます。
