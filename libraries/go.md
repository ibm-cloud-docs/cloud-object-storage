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
Use `go get` to retrieve the SDK to add it to your GOPATH workspace, or project's Go module dependencies. The SDK requires a minimum version of Go 1.9.

```
go get github.com/IBM/ibm-cos-sdk-go
```

To update the SDK use `go get -u` to retrieve the latest version of the SDK..

```
go get -u github.com/IBM/ibm-cos-sdk-go
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
    serviceEndpoint   = "<SERVICE_ENDPOINT>"
	Bucket_Location	  = "<LOCATION>"
)

# Create resource
conf := aws.NewConfig().
		WithRegion("us-standard").
		WithEndpoint(serviceEndpoint).
		WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
	WithS3ForcePathStyle(true)

```
*Key Values*
* `<API_KEY>` - api key generated when creating the service credentials (write access is required for creation and deletion examples)
* `<RESOURCE_INSTANCE_ID>` - resource ID for your cloud object storage (available through [IBM Cloud CLI](../getting-started-cli.html) or [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window})
* `<SERVICE_ENDPOINT>` - public endpoint for your cloud object storage (available from the [IBM Cloud Dashboard](https://console.bluemix.net/dashboard/apps){:new_window})
* `<location>` - default location for your cloud object storage (must match the region used for `<endpoint>`)

*SDK References*
* [ServiceResource](#){:new_window} - * Pending Doc gen *

## Code Examples

### Creating a new bucket
A list of valid provisioning codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage/basics/classes#locationconstraint).

```Go
func main() {

	// Create client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)

	// Bucket Name
	newBucket := "new-bucket"

	input := &s3.CreateBucketInput{
		Bucket: aws.String(newBucket),
	}
	client.CreateBucket(input)

	d, _ := client.ListBuckets(&s3.ListBucketsInput{})
	fmt.Println(d)
}
```

*SDK References*
* Classes
  * [Bucket](#){:new_window}
* Methods
    * [Create](#){:new_window}


### List available buckets
```Go
func main() {

	// Create client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)


	fmt.Println("Buckets:")

	d, _ := client.ListBuckets(&s3.ListBucketsInput{})
	fmt.Println(d)
}

```
*SDK References*


### List items in a bucket
```Go
func main() {

	// Create client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)

	// Bucket Name
	bucket := "<Bucket_Name>"

	// Get the list of items
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

*SDK References*



### Get object contents
```Go
func getObject(objectKey string, bucketName string, client s3iface.S3API) (*s3.GetObjectOutput, error) {
	input := new(s3.GetObjectInput)
	input.Bucket = aws.String(bucketName)
	input.Key = aws.String(objectKey)
	return client.GetObject(input)
}

func main() {
	
	// Create client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)

	// Call Function
	res, _ := getObject("<FILE_NAME>", "<BUCKET_NAME>", client)
	fmt.Println(res.Body)

	bytecont, _ := ioutil.ReadAll(res.Body)

	fmt.Println(bytecont)
}

```

*SDK References*



### Delete an item from a bucket
```Go
func deleteObject(objectKey string, bucketName string, client s3iface.S3API) (*s3.DeleteObjectOutput, error) {
	input := new(s3.DeleteObjectInput)
	input.Bucket = aws.String(bucketName)
	input.Key = aws.String(objectKey)
	return client.DeleteObject(input)
}

func main() {

	// Create client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)

	// Call Function
	result, _ :=  deleteObject("<FILE_NAME>", "<BUCKET_NAME>", client)
	fmt.Println(result)
}
```

*SDK References*



### Delete a bucket
```Go
func deleteBucket(bucketName string, client s3iface.S3API) (*s3.DeleteBucketOutput, error) {
	input := new(s3.DeleteBucketInput)
	input.Bucket = aws.String(bucketName)
	return client.DeleteBucket(input)
}

func main() {

	// Create client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)

	result, _ := deleteBucket("<BUCKET_NAME>", client)

	fmt.Println(result)
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
