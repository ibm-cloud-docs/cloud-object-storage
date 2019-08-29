---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-23"

keywords: object storage, go, sdk

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

# Using Go
{: #go}

The {{site.data.keyword.cos_full}} SDK for Go provides features to make the most of {{site.data.keyword.cos_full_notm}}.
{: .shortdesc}

The {{site.data.keyword.cos_full_notm}} SDK for Go is comprehensive, with many features and capabilities that exceed the scope and space of this guide. For detailed class and method documentation [see the Go API documentation](https://ibm.github.io/ibm-cos-sdk-go/). Source code can be found in the [GitHub repository](https://github.com/IBM/ibm-cos-sdk-go).

## Getting the SDK
{: #go-get-sdk}

Use `go get` to retrieve the SDK to add it to your GOPATH workspace, or project's Go module dependencies. The SDK requires a minimum version of Go 1.10 and maximum version of Go 1.12. Future versions of Go will be supported once our quality control process has been completed.

```
go get github.com/IBM/ibm-cos-sdk-go
```

To update the SDK use `go get -u` to retrieve the latest version of the SDK.

```
go get -u github.com/IBM/ibm-cos-sdk-go
```

### Import packages
{: #go-import-packages}

After you have installed the SDK, you will need to import the packages that you require into your Go applications to use the SDK, as shown in the following example:
```
import (
    "github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go/aws"
    "github.com/IBM/ibm-cos-sdk-go/aws/session"
    "github.com/IBM/ibm-cos-sdk-go/service/s3"
)
```

## Creating a client and sourcing credentials
{: #go-client-credentials}

To connect to {{site.data.keyword.cos_full_notm}}, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables. 

The credentials can be found by creating a [Service Credential](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), or through the CLI.

Figure 1 shows an example of how to define environment variables in an application runtime at the {{site.data.keyword.cos_full_notm}} portal. The required variables are `IBM_API_KEY_ID` containing your Service Credential 'apikey', `IBM_SERVICE_INSTANCE_ID` holding the 'resource_instance_id' also from your Service Credential, and an `IBM_AUTH_ENDPOINT` with a value appropriate to your account, like `https://iam.cloud.ibm.com/identity/token`. If using environment variables to define your application credentials, use `WithCredentials(ibmiam.NewEnvCredentials(aws.NewConfig())).`, replacing the similar method used in the configuration example.

![environment variables](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/go-library-fig-1-env-vars.png)
{: caption="Figure 1. Environment Variables"}

### Initializing configuration
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
For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Code Examples
{: #go-code-examples}

### Creating a new bucket
{: #go-new-bucket}

A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

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

### List available buckets
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


### Upload an object to a bucket
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



### List items in a bucket
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

### Get an object's contents
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

### Delete an object from a bucket
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


### Delete multiple objects from a bucket
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


### Delete a bucket
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



### Run a manual multi-part upload
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

## Using Key Protect
{: #go-examples-kp}

Key Protect can be added to a storage bucket to manage encryption keys. All data is encrypted in IBM COS, but Key Protect provides a service for generating, rotating, and controlling access to encryption keys by using a centralized service.

### Before You Begin
{: #go-examples-kp-prereqs}

The following items are necessary in order to create a bucket with Key-Protect enabled:

* A Key Protect service [provisioned](/docs/services/key-protect?topic=key-protect-provision#provision)
* A Root key available (either [generated](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) or [imported](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Retrieving the Root Key CRN
{: #go-examples-kp-root}

1. Retrieve the [instance ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) for your Key Protect service
2. Use the [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) to retrieve all your [available keys](https://cloud.ibm.com/apidocs/key-protect)
    * You can either use `curl` commands or an API REST Client such as [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) to access the [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Retrieve the CRN of the root key you use to enabled Key Protect on your bucket. The CRN looks similar to below:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Creating a bucket with Key Protect enabled
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
*Key Values*
* `<NEW_BUCKET_NAME>` - The name of the new bucket.
* `<ROOT-KEY-CRN>` - CRN of the Root Key that is obtained from the Key Protect service.
* `<ALGORITHM>` - The encryption algorithm that is used for new objects added to the bucket (Default is AES256).


### Use the transfer manager
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

### Getting an extended listing
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
*Key Values*
* `<MAX_KEYS>` - Maximum number of buckets to retrieve in the request.
* `<MARKER>` - The bucket name to start the listing (Skip until this bucket).
* `<PREFIX` - Only include buckets whose name start with this prefix.

### Getting an extended listing with pagination
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
*Key Values*
* `<MAX_KEYS>` - Maximum number of buckets to retrieve in the request.
* `<MARKER>` - The bucket name to start the listing (Skip until this bucket).
* `<PREFIX` - Only include buckets whose name start with this prefix.

### Archive Tier Support
{: #go-archive-tier-support}

You can automatically archive objects after a specified length of time or after a specified date. Once archived, a temporary copy of an object can be restored for access as needed. Please note the time required to restore the temporary copy of the object(s) may take up to 15 hours. 

To use the example provided, provide your own configuration&mdash;including replacing `<apikey>` and other bracketed `<...>` information&mdash;keeping in mind that using environment variables are more secure, and one should not put credentials in code that will be versioned.

An archive policy is set at the bucket level by calling the `PutBucketLifecycleConfiguration` method on a client instance. A newly added or modified archive policy applies to new objects uploaded and does not affect existing objects. For more detail, see the [documentation](https://cloud.ibm.com/docs/services/cloud-object-storage?topic=cloud-object-storage-go).

```Go
package main

import (
	"bytes"
	"fmt"
	"io"
	"github.com/IBM/ibm-cos-sdk-go/aws"
	"github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
	"github.com/IBM/ibm-cos-sdk-go/aws/session"
	"github.com/IBM/ibm-cos-sdk-go/service/s3"
)

const (
	apiKey            = "<apikey>"
	serviceInstanceID = "<instance-id>"
	authEndpoint      = "https://iam.cloud.ibm.com/identity/token"
	serviceEndpoint   = "<instance-endpoint>"
)

func main() {

	conf := aws.NewConfig().
		WithEndpoint(serviceEndpoint).
		WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(),
			authEndpoint, apiKey, serviceInstanceID)).
		WithS3ForcePathStyle(true)

	// Create Client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)
	
	// PUT BUCKET LIFECYCLE CONFIGURATION
	// Replace <bucketname> with the name of the bucket
	lInput := &s3.PutBucketLifecycleConfigurationInput{
		Bucket: aws.String("<bucketname>"),
		LifecycleConfiguration: &s3.LifecycleConfiguration{
			Rules: []*s3.LifecycleRule{
				{
					Status: aws.String("Enabled"),
					Filter: &s3.LifecycleRuleFilter{},
					ID:     aws.String("id3"),
					Transitions: []*s3.Transition{
						{
							Days:         aws.Int64(5),
							StorageClass: aws.String("Glacier"),
						},
					},
				},
			},
		},
	}
	l, e := client.PutBucketLifecycleConfiguration(lInput)
	fmt.Println(l) // should print an empty bracket
	fmt.Println(e) // should print <nil>
	
	// GET BUCKET LIFECYCLE CONFIGURATION
	gInput := &s3.GetBucketLifecycleConfigurationInput{
		Bucket: aws.String("<bucketname>"),
	}
	g, e := client.GetBucketLifecycleConfiguration(gInput)
	fmt.Println(g)
	fmt.Println(e) // see response for results

    // RESTORE OBJECT
    // Replace <archived-object> with the appropriate key
    rInput := &s3.RestoreObjectInput{
        Bucket: aws.String("<bucketname>"),
        Key:    aws.String("<archived-object>"),
        RestoreRequest: &s3.RestoreRequest{
            Days: aws.Int64(100),
            GlacierJobParameters: &s3.GlacierJobParameters{
                Tier: aws.String("Bulk"),
            },
        },
    }
    r, e := client.RestoreObject(rInput)
    fmt.Println(r)
    fmt.Println(e)

}

```
{: .codeblock}

The typical response is exemplified here.

```
 {
   Rules: [{
       Filter: {
 
       },
       ID: "id3",
       Status: "Enabled",
       Transitions: [{
           Days: 5,
           StorageClass: "GLACIER"
         }]
     }]
 }
```
{: .codeblock}

### Immutable Object Storage
{: #go-immutable-object-storage}

Users can configure buckets with an Immutable Object Storage policy to prevent objects from being modified or deleted for a defined period of time. The retention period can be specified on a per-object basis, or objects can inherit a default retention period set on the bucket. It is also possible to set open-ended and permanent retention periods. Immutable Object Storage meets the rules set forth by the SEC governing record retention, and IBM Cloud administrators are unable to bypass these restrictions. 

Note: Immutable Object Storage does not support Aspera transfers via the SDK to upload objects or directories at this stage.

```Go
package main

import (
	"bytes"
	"fmt"
	"io"
	"github.com/IBM/ibm-cos-sdk-go/aws"
	"github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
	"github.com/IBM/ibm-cos-sdk-go/aws/session"
	"github.com/IBM/ibm-cos-sdk-go/service/s3"
)

const (
	apiKey            = "<apikey>"
	serviceInstanceID = "<instance-id>"
	authEndpoint      = "https://iam.cloud.ibm.com/identity/token"
	serviceEndpoint   = "<instance-endpoint>"
)

func main() {

conf := aws.NewConfig().
		WithEndpoint(serviceEndpoint).
		WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(),
			authEndpoint, apiKey, serviceInstanceID)).
		WithS3ForcePathStyle(true)

	// Create Client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)
	
	// Create a bucket
	input := &s3.CreateBucketInput{
		Bucket: aws.String("<bucketname>"),
	}
	d, e := client.CreateBucket(input)
	fmt.Println(d) // should print an empty bracket
	fmt.Println(e) // should print <nil>

	// PUT BUCKET PROTECTION CONFIGURATION
	pInput := &s3.PutBucketProtectionConfigurationInput{
		Bucket: aws.String("<bucketname>"),
		ProtectionConfiguration: &s3.ProtectionConfiguration{
			DefaultRetention: &s3.BucketProtectionDefaultRetention{
				Days: aws.Int64(100),
			},
			MaximumRetention: &s3.BucketProtectionMaximumRetention{
				Days: aws.Int64(1000),
			},
			MinimumRetention: &s3.BucketProtectionMinimumRetention{
				Days: aws.Int64(10),
			},
			Status: aws.String("Retention"),
		},
	}
	p, e := client.PutBucketProtectionConfiguration(pInput)
	fmt.Println(p)
	fmt.Println(e) // see response for results

	// GET BUCKET PROTECTION CONFIGURATION
	gInput := &s3.GetBucketProtectionConfigurationInput{
		Bucket: aws.String("<bucketname>"),
	}
	g, e := client.GetBucketProtectionConfiguration(gInput)
	fmt.Println(g)
	fmt.Println(e)
}


```

The typical response is exemplified here.

```
 {
   ProtectionConfiguration: {
     DefaultRetention: {
       Days: 100
     },
     MaximumRetention: {
       Days: 1000
     },
     MinimumRetention: {
       Days: 10
     },
     Status: "COMPLIANCE"
   }
 }
```
{: .codeblock}

## Next Steps

For even more detail, check out the [source code](https://github.com/IBM/ibm-cos-sdk-go).
