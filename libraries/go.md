---

copyright:
  years: 2019
lastupdated: "2019-01-09"

---

# Using Go

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

The {{site.data.keyword.cos_full}} SDK for Go is comprehensive, and has features and capabilities not described in this guide.  For detailed class and method documentation [see the Go Docs](https://ibm.github.io/ibm-cos-sdk-go/). Source code can be found in the [GitHub repository](https://github.com/IBM/ibm-cos-sdk-go).

## Getting the SDK
Use `go get` to retrieve the SDK to add it to your GOPATH workspace, or project's Go module dependencies.

```
go get github.ibm.com/cs-developer-enablement/ibm-cos-sdk-go
```

To update the SDK use `go get -u` to retrieve the latest version of the SDK..

```
go get -u github.ibm.com/cs-developer-enablement/ibm-cos-sdk-go
```

### Import packages
After you have installed the SDK, you will need to import the packages into your Go applications to use the SDK, as shown in the following example:
```
import "github.com/IBM/ibm-cos-sdk-go/service/s3"
```


## Creating a client and sourcing credentials
{: #client-credentials}

To connect to COS, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.

After generating a [Service Credential](/docs/services/cloud-object-storage/iam/service-credentials.html), the resulting JSON document can be saved to `~/.bluemix/cos_credentials`.  The SDK will automatically source credentials from this file unless other credentials are explicitly set during client creation. If the `cos_credentials` file contains HMAC keys the client will authenticate with a signature, otherwise the client will use the provided API key to authenticate using a bearer token.

If migrating from AWS S3, you can also source credentials data from  `~/.aws/credentials` in the format:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

If both `~/.bluemix/cos_credentials` and `~/.aws/credentials` exist, `cos_credentials` will take preference.


## Code Examples



### Initializing configuration
{: #init-config}

```Go

# Constants for IBM COS values
const (
    apiKey            = "<API_KEY>"
    serviceInstanceID = "<RESOURCE_INSTANCE_ID>"
    authEndpoint      = "https://iam.bluemix.net/oidc/token"
    serviceEndpoint   = "https://s3-api.us-geo.objectstorage.softlayer.net"
)

# Create resource
conf := aws.NewConfig().
		WithRegion("us-standard").
		WithEndpoint(serviceEndpoint).
		WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
	WithS3ForcePathStyle(true)
)
```

## Code Examples

Note that when adding custom metadata to an object, it is necessary to create an `ObjectMetadata` object using the SDK, and not to manually send a custom header containing `x-amz-meta-{key}`.  The latter can cause issues when authenticating using HMAC credentials.
{: .tip}

### Creating a new bucket
A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage/basics/classes#locationconstraint).

```Go
// Usage:
//    go run s3_create_bucket BUCKET_NAME
func main() {
    if len(os.Args) != 2 {
        exitErrorf("Bucket name missing!\nUsage: %s bucket_name", os.Args[0])
    }

    bucket := os.Args[1]

    // Initialize a session in us-west-2 that the SDK will use to load
    // credentials from the shared credentials file ~/.aws/credentials.
    sess, err := session.NewSession(&aws.Config{
        Region: aws.String("us-west-2")},
    )

    // Create client
    svc := s3.New(sess)

    // Create the S3 Bucket
    _, err = svc.CreateBucket(&s3.CreateBucketInput{
        Bucket: aws.String(bucket),
    })
    if err != nil {
        exitErrorf("Unable to create bucket %q, %v", bucket, err)
    }

    // Wait until bucket is created before finishing
    fmt.Printf("Waiting for bucket %q to be created...\n", bucket)

    err = svc.WaitUntilBucketExists(&s3.HeadBucketInput{
        Bucket: aws.String(bucket),
    })
    if err != nil {
        exitErrorf("Error occurred while waiting for bucket to be created, %v", bucket)
    }

    fmt.Printf("Bucket %q successfully created\n", bucket)
}

func exitErrorf(msg string, args ...interface{}) {
    fmt.Fprintf(os.Stderr, msg+"\n", args...)
    os.Exit(1)
}
```

*SDK References*


### Creating a new bucket (short)

```Go
func createBucketACL(bucketName string, acl string, client s3iface.S3API) (*s3.CreateBucketOutput, error) {
	input := new(s3.CreateBucketInput).SetIBMServiceInstanceId( v: "")
	conf := new(s3.CreateBucketConfiguration)
	// conf.SetLocationConstraint("us-standard")
	input.CreateBucketConfiguration = conf
	input.Bucket = aws.String(bucketName)
	input.ACL = aws.String(acl)
	return client.CreateBucket(input)
}
```

### List available buckets
```Go
func main() {
    // Initialize a session in us-west-2 that the SDK will use to load
    // credentials from the shared credentials file ~/.aws/credentials.
    sess, err := session.NewSession(&aws.Config{
        Region: aws.String("us-west-2")},
    )

    // Create  client
    svc := s3.New(sess)

    result, err := svc.ListBuckets(nil)
    if err != nil {
        exitErrorf("Unable to list buckets, %v", err)
    }

    fmt.Println("Buckets:")

    for _, b := range result.Buckets {
        fmt.Printf("* %s created on %s\n",
            aws.StringValue(b.Name), aws.TimeValue(b.CreationDate))
    }
}

func exitErrorf(msg string, args ...interface{}) {
    fmt.Fprintf(os.Stderr, msg+"\n", args...)
    os.Exit(1)
}
```


### List items in a bucket
```Go
// Usage:
//    go run s3_list_objects.go BUCKET_NAME
func main() {
    if len(os.Args) != 2 {
        exitErrorf("Bucket name required\nUsage: %s bucket_name",
            os.Args[0])
    }

    bucket := os.Args[1]

    // Initialize a session in us-west-2 that the SDK will use to load
    // credentials from the shared credentials file ~/.aws/credentials.
    sess, err := session.NewSession(&aws.Config{
        Region: aws.String("us-west-2")},
    )

    // Create client
    svc := s3.New(sess)

    // Get the list of items
    resp, err := svc.ListObjects(&s3.ListObjectsInput{Bucket: aws.String(bucket)})
    if err != nil {
        exitErrorf("Unable to list items in bucket %q, %v", bucket, err)
    }

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

func exitErrorf(msg string, args ...interface{}) {
    fmt.Fprintf(os.Stderr, msg+"\n", args...)
    os.Exit(1)
}
```

*SDK References*


### Upload an object to a bucket
```Go
// Usage:
//    go run s3_upload_object.go BUCKET_NAME FILENAME
func main() {
    if len(os.Args) != 3 {
        exitErrorf("bucket and file name required\nUsage: %s bucket_name filename",
            os.Args[0])
    }

    bucket := os.Args[1]
    filename := os.Args[2]

    file, err := os.Open(filename)
    if err != nil {
        exitErrorf("Unable to open file %q, %v", err)
    }

    defer file.Close()

    // Initialize a session in us-west-2 that the SDK will use to load
    // credentials from the shared credentials file ~/.aws/credentials.
    sess, err := session.NewSession(&aws.Config{
        Region: aws.String("us-west-2")},
    )

    // Setup the S3 Upload Manager.
    uploader := s3manager.NewUploader(sess)

    // Upload the file's body to S3 bucket as an object with the key being the
    // same as the filename.
    _, err = uploader.Upload(&s3manager.UploadInput{
        Bucket: aws.String(bucket),

        // Can also use the `filepath` standard library package to modify the
        // filename as need for an S3 object key. Such as turning absolute path
        // to a relative path.
        Key: aws.String(filename),

        // The file to be uploaded. io.ReadSeeker is preferred as the Uploader
        // will be able to optimize memory when uploading large content. io.Reader
        // is supported, but will require buffering of the reader's bytes for
        // each part.
        Body: file,
    })
    if err != nil {
        // Print the error and exit.
        exitErrorf("Unable to upload %q to %q, %v", filename, bucket, err)
    }

    fmt.Printf("Successfully uploaded %q to %q\n", filename, bucket)
}

func exitErrorf(msg string, args ...interface{}) {
    fmt.Fprintf(os.Stderr, msg+"\n", args...)
    os.Exit(1)
}
```

*SDK References*




### Delete an item from a bucket
```Go
// Usage:
//    go run s3_delete_object BUCKET_NAME OBJECT_NAME
func main() {
    if len(os.Args) != 3 {
        exitErrorf("Bucket and object name required\nUsage: %s bucket_name object_name",
            os.Args[0])
    }

    bucket := os.Args[1]
    obj := os.Args[2]

    // Initialize a session in us-west-2 that the SDK will use to load
    // credentials from the shared credentials file ~/.aws/credentials.
    sess, err := session.NewSession(&aws.Config{
        Region: aws.String("us-west-2")},
    )

    // Create client
    svc := s3.New(sess)

    // Delete the item
    _, err = svc.DeleteObject(&s3.DeleteObjectInput{Bucket: aws.String(bucket), Key: aws.String(obj)})
    if err != nil {
        exitErrorf("Unable to delete object %q from bucket %q, %v", obj, bucket, err)
    }

    err = svc.WaitUntilObjectNotExists(&s3.HeadObjectInput{
        Bucket: aws.String(bucket),
        Key:    aws.String(obj),
    })
    if err != nil {
        exitErrorf("Error occurred while waiting for object %q to be deleted, %v", obj)
    }

    fmt.Printf("Object %q successfully deleted\n", obj)
}

func exitErrorf(msg string, args ...interface{}) {
    fmt.Fprintf(os.Stderr, msg+"\n", args...)
    os.Exit(1)
}
```

*SDK References*




### Delete all the items from a bucket
```Go
// Usage:
//    go run s3_delete_objects BUCKET
func main() {
    if len(os.Args) != 2 {
        exitErrorf("Bucket name required\nUsage: %s BUCKET", os.Args[0])
    }

    bucket := os.Args[1]

    // Initialize a session in us-west-2 that the SDK will use to load
    // credentials from the shared credentials file ~/.aws/credentials.
    sess, _ := session.NewSession(&aws.Config{
        Region: aws.String("us-west-2")},
    )

    // Create client
    svc := s3.New(sess)

    // Setup BatchDeleteIterator to iterate through a list of objects.
    iter := s3manager.NewDeleteListIterator(svc, &s3.ListObjectsInput{
        Bucket: aws.String(bucket),
    })

    // Traverse iterator deleting each object
    if err := s3manager.NewBatchDeleteWithClient(svc).Delete(aws.BackgroundContext(), iter); err != nil {
        exitErrorf("Unable to delete objects from bucket %q, %v", bucket, err)
    }

    fmt.Printf("Deleted object(s) from bucket: %s", bucket)
}

func exitErrorf(msg string, args ...interface{}) {
    fmt.Fprintf(os.Stderr, msg+"\n", args...)
    os.Exit(1)
}
```

*SDK References*



### Delete a bucket
```Go
// Usage:
//    go run s3_delete_bucket BUCKET_NAME
func main() {
    if len(os.Args) != 2 {
        exitErrorf("bucket name required\nUsage: %s bucket_name", os.Args[0])
    }

    bucket := os.Args[1]

    // Initialize a session in us-west-2 that the SDK will use to load
    // credentials from the shared credentials file ~/.aws/credentials.
    sess, err := session.NewSession(&aws.Config{
        Region: aws.String("us-west-2")},
    )

    // Create client
    svc := s3.New(sess)

    // Delete the S3 Bucket
    // It must be empty or else the call fails
    _, err = svc.DeleteBucket(&s3.DeleteBucketInput{
        Bucket: aws.String(bucket),
    })
    if err != nil {
        exitErrorf("Unable to delete bucket %q, %v", bucket, err)
    }

    // Wait until bucket is deleted before finishing
    fmt.Printf("Waiting for bucket %q to be deleted...\n", bucket)

    err = svc.WaitUntilBucketNotExists(&s3.HeadBucketInput{
        Bucket: aws.String(bucket),
    })
    if err != nil {
        exitErrorf("Error occurred while waiting for bucket to be deleted, %v", bucket)
    }

    fmt.Printf("Bucket %q successfully deleted\n", bucket)
}

func exitErrorf(msg string, args ...interface{}) {
    fmt.Fprintf(os.Stderr, msg+"\n", args...)
    os.Exit(1)
}
```

*SDK References*


