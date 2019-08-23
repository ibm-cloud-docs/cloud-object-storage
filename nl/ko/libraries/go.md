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

# Go 사용
{: #go}

{{site.data.keyword.cos_full}} SDK for Go는 포괄적이며, 이 안내서에 설명되어 있지 않은 기능을 포함하고 있습니다. 자세한 클래스 및 메소드 문서는 [Go 문서](https://ibm.github.io/ibm-cos-sdk-go/)를 참조하십시오. 소스 코드는 [GitHub 저장소](https://github.com/IBM/ibm-cos-sdk-go)에서 찾을 수 있습니다. 

## SDK 가져오기
{: #go-get-sdk}

`go get`을 사용하여 SDK를 검색하고 이를 GOPATH 작업공간, 또는 프로젝트의 Go 모듈 종속 항목에 추가하십시오. 이 SDK는 최소 1.10에서 최대 1.12의 Go 버전을 필요로 합니다. 이보다 더 높은 Go 버전은 품질 제어 프로세스가 완료되고 나면 지원됩니다. 

```
go get github.com/IBM/ibm-cos-sdk-go
```

SDK를 업데이트하려면 `go get -u`를 사용하여 SDK의 최신 버전을 검색하십시오. 

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### 패키지 가져오기
{: #go-import-packages}

SDK를 설치한 후에는 다음 예에 표시되어 있는 바와 같이, SDK를 사용하는 데 필요한 패키지를 Go 애플리케이션으로 가져와야 합니다. 
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## 클라이언트 작성 및 인증 정보 제공
{: #go-client-credentials}

{{site.data.keyword.cos_full_notm}}에 연결하기 위해, 인증 정보(API 키 및 서비스 인스턴스 ID) 제공을 통해 클라이언트가 작성되고 구성됩니다. 이러한 값은 인증 정보 파일 또는 환경 변수로부터 자동으로 가져올 수도 있습니다.  

인증 정보는 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)를 작성하여 찾거나 CLI를 통해 찾을 수 있습니다. 

그림 1은 {{site.data.keyword.cos_full_notm}} 포털에서 애플리케이션 런타임에 환경 변수를 정의하는 방법에 대한 예를 보여줍니다. 필수 변수는 서비스 인증 정보 'apikey'를 포함하는 `IBM_API_KEY_ID`, 마찬가지로 서비스 인증 정보의 'resource_instance_id'를 포함하는 `IBM_SERVICE_INSTANCE_ID`, 그리고 계정에 대해 적절한 값을 포함하는 `IBM_AUTH_ENDPOINT`(예: `https://iam.cloud.ibm.com/identity/token`)입니다. 환경 변수를 사용하여 애플리케이션 인증 정보를 정의하는 경우에는 `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).`를 적절히 사용하여 구성 예에서 사용된 유사한 메소드를 대체하십시오. 

![환경 변수](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="그림 1. 환경 변수"}

### 구성 초기화
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
엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 

## 코드 예
{: #go-code-examples}

### 새 버킷 작성
{: #go-new-bucket}

`LocationConstraint`에 대한 유효한 프로비저닝 코드의 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)에서 참조할 수 있습니다. 

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

### 사용 가능한 버킷 나열
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


### 버킷에 오브젝트 업로드
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



### 버킷에 있는 항목 나열
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

### 오브젝트의 컨텐츠 가져오기
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

### 버킷에서 오브젝트 삭제
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


### 버킷에서 여러 오브젝트 삭제
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


### 버킷 삭제
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



### 수동 다중 파트 업로드 실행
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

## Key Protect 사용
{: #go-examples-kp}

암호화 키를 관리하기 위해 Key Protect를 스토리지 버킷에 추가할 수 있습니다. IBM COS에서는 모든 데이터가 암호화되지만, Key Protect는 중앙 집중식 서비스를 사용하여 암호화 키를 생성하고, 순환하고, 암호화 키에 대한 액세스를 제어하는 서비스를 제공합니다. 

### 시작하기 전에
{: #go-examples-kp-prereqs}

Key Protect가 사용으로 설정된 버킷을 작성하려면 다음 항목이 필요합니다. 

* [프로비저닝](/docs/services/key-protect?topic=key-protect-provision#provision)된 Key Protect 서비스
* 사용 가능한 루트 키([생성](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) 또는 [가져오기](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)를 통해 확보)

### 루트 키 CRN 검색
{: #go-examples-kp-root}

1. Key Protect 서비스의 [인스턴스 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)를 검색하십시오. 
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)를 사용하여 [사용 가능한 키](https://cloud.ibm.com/apidocs/key-protect)를 모두 검색하십시오. 
    * `curl` 명령을 사용하거나 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)과 같은 API REST 클라이언트를 사용하여 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)에 액세스할 수 있습니다. 
3. 버킷에서 Key Protect를 사용으로 설정하는 데 사용할 루트 키의 CRN을 검색하십시오. 이 CRN은 아래와 같습니다. 

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect가 사용으로 설정된 버킷 작성
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
*키 값*
* `<NEW_BUCKET_NAME>` - 새 버킷의 이름입니다. 
* `<ROOT-KEY-CRN>` - Key Protect 서비스로부터 얻은 루트 키의 CRN입니다. 
* `<ALGORITHM>` - 버킷에 추가된 새 오브젝트에 사용되는 암호화 알고리즘입니다(기본값은 AES256). 


### 전송 관리자 사용
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

### 확장된 목록 가져오기
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
*키 값*
* `<MAX_KEYS>` - 요청에서 검색할 최대 버킷 수입니다. 
* `<MARKER>` - 목록을 시작할 버킷 이름입니다(이 버킷까지 건너뜀). 
* `<PREFIX` - 이 접두부로 이름이 시작하는 버킷만 포함합니다. 

### 페이지 매김을 사용하여 확장된 목록 가져오기
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
*키 값*
* `<MAX_KEYS>` - 요청에서 검색할 최대 버킷 수입니다. 
* `<MARKER>` - 목록을 시작할 버킷 이름입니다(이 버킷까지 건너뜀). 
* `<PREFIX` - 이 접두부로 이름이 시작하는 버킷만 포함합니다. 
