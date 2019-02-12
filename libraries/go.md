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

The credentials can be found by creating a [Service Credential](/docs/services/cloud-object-storage/iam/service-credentials.html), or through the CLI.

If migrating from AWS S3, you can also source credentials data from  `~/.aws/credentials` in the format:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```


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
	bucketLocation	  = "<LOCATION>"
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
* `<LOCATION>` - default location for your cloud object storage (must match the region used for `<endpoint>`)

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
	newBucket := "<NEW_BUCKET_NAME>"

	// Call Function
	input := &s3.CreateBucketInput{
		Bucket: aws.String(newBucket),
	}
	client.CreateBucket(input)

	d, _ := client.ListBuckets(&s3.ListBucketsInput{})
	fmt.Println(d)
}
```

*SDK References* * Pending Doc gen *
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

	// Call Function
	d, _ := client.ListBuckets(&s3.ListBucketsInput{})
	fmt.Println(d)
}

```
*SDK References* * Pending Doc gen *


### List items in a bucket
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

*SDK References* * Pending Doc gen *



### Get file contents of particular object
```Go
func main() {
	
	// Create client
	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)

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

*SDK References* * Pending Doc gen *



### Delete an object from a bucket
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

*SDK References* * Pending Doc gen *



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

	// Call Function
	result, _ := deleteBucket("<BUCKET_NAME>", client)

	fmt.Println(result)
}
```

*SDK References* * Pending Doc gen *



### Upload an object to a bucket
```Go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)
    
    // users will need to create bucket, key (flat string name), body of an object

    input := s3.PutObjectInput{
        Bucket:        aws.String(bucketName),
        Key:           aws.String(key),
        Body:          io.ReadSeeker(object),
        ContentLength: aws.Int64(objectStat.Size()),
        ContentType:   aws.String("application/octet-stream"),
    }

    // Call Function
    result, _ := client.PutObject(&input)
    fmt.Println(result)
}
```

*SDK References*


