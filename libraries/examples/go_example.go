package main

import (
	"bytes"
	"fmt"
	"github.com/IBM/ibm-cos-sdk-go/aws"
	"github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
	"github.com/IBM/ibm-cos-sdk-go/aws/session"
	"github.com/IBM/ibm-cos-sdk-go/service/s3"
	"io"
	"math/rand"
	"os"
	"time"
)

// Constants for IBM COS values
const (
	apiKey            = "<api-key>" // apikey within Service credentials - example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
	serviceInstanceID = "<resource-instance-id>" // resource_instance_id within Service credentials - example: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
	authEndpoint      = "https://iam.bluemix.net/oidc/token"
	serviceEndpoint   = "<endpoint>" // e.g. us-geo = "https://s3-api.us-geo.objectstorage.softlayer.net"
)

// UUID
func random(min int, max int) int {
	return rand.Intn(max-min) + min
}

func main() {

	// UUID
	rand.Seed(time.Now().UnixNano())
	UUID := random(10, 2000)

	// Variables
	newBucket := fmt.Sprintf("%s%d", "go.bucket", UUID) // New bucket name
	objectKey := fmt.Sprintf("%s%d%s", "go_file_", UUID, ".txt") // Object Key
	content := bytes.NewReader([]byte("This is a test file from Go code sample!!!")) // Content for the new object
	downloadObjectKey := fmt.Sprintf("%s%d%s", "downloaded_go_file_", UUID, ".txt") // Downloaded Object Key



	// Setting up a new configuration
	conf := aws.NewConfig().
		WithRegion("us-standard"). // Enter your storage class (LocationConstraint) - example: us-standard
		WithEndpoint(serviceEndpoint).
		WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(), authEndpoint, apiKey, serviceInstanceID)).
		WithS3ForcePathStyle(true)

	// Create client connection
	sess := session.Must(session.NewSession()) // Creating a new session
	client := s3.New(sess, conf)               // Creating a new client



	// Create new bucket
	_, err := client.CreateBucket(&s3.CreateBucketInput{
		Bucket: aws.String(newBucket), // New Bucket Name
	})
	if err != nil {
		exitErrorf("Unable to create bucket %q, %v", newBucket, err)
	}

	// Wait until bucket is created before finishing
	fmt.Printf("Waiting for bucket %q to be created...\n", newBucket)

	err = client.WaitUntilBucketExists(&s3.HeadBucketInput{
		Bucket: aws.String(newBucket),
	})
	if err != nil {
		exitErrorf("Error occurred while waiting for bucket to be created, %v", newBucket)
	}

	fmt.Printf("Bucket %q successfully created\n", newBucket)

	// Retrieve the list of available buckets
	bklist, err := client.ListBuckets(nil)
	if err != nil {
		exitErrorf("Unable to list buckets, %v", err)
	}

	fmt.Println("Listing buckets:")

	for _, b := range bklist.Buckets {
		fmt.Printf("* %s created on %s\n",
			aws.StringValue(b.Name), aws.TimeValue(b.CreationDate))
	}

	// Uploading an object
	input3 := s3.CreateMultipartUploadInput{
		Bucket: aws.String(newBucket), // Bucket Name
		Key:    aws.String(objectKey), // Object Key
	}

	upload, _ := client.CreateMultipartUpload(&input3)

	uploadPartInput := s3.UploadPartInput{
		Bucket:     aws.String(newBucket), // Bucket Name
		Key:        aws.String(objectKey), // Object Key
		PartNumber: aws.Int64(int64(1)),
		UploadId:   upload.UploadId,
		Body:       content,
	}

	var completedParts []*s3.CompletedPart
	completedPart, _ := client.UploadPart(&uploadPartInput)

	completedParts = append(completedParts, &s3.CompletedPart{
		ETag:       completedPart.ETag,
		PartNumber: aws.Int64(int64(1)),
	})

	completeMPUInput := s3.CompleteMultipartUploadInput{
		Bucket: aws.String(newBucket), // Bucket Name
		Key:    aws.String(objectKey), // Object Key
		MultipartUpload: &s3.CompletedMultipartUpload{
			Parts: completedParts,
		},
		UploadId: upload.UploadId,
	}

	d, _ := client.CompleteMultipartUpload(&completeMPUInput)
	fmt.Println(d)

	// List objects within a bucket
	resp, err := client.ListObjects(&s3.ListObjectsInput{Bucket: aws.String(newBucket)})
	if err != nil {
		exitErrorf("Unable to list items in bucket %q, %v", newBucket, err)
	}
	for _, item := range resp.Contents {
		fmt.Println("Name:         ", *item.Key)          // Print the object's name
		fmt.Println("Last modified:", *item.LastModified) // Print the last modified date of the object
		fmt.Println("Size:         ", *item.Size)         // Print the size of the object
		fmt.Println("")
	}

	fmt.Println("Found", len(resp.Contents), "items in bucket", newBucket)


	// Download an object
	input4 := s3.GetObjectInput{
		Bucket: aws.String(newBucket), // The bucket where the object is located
		Key:    aws.String(objectKey), // Object you want to download
	}

	res, err := client.GetObject(&input4)
	if err != nil {
		exitErrorf("Unable to download object %q from bucket %q, %v", objectKey, newBucket, err)
	}

	f, _ := os.Create(downloadObjectKey)
	defer f.Close()
	io.Copy(f, res.Body)

	fmt.Println("Downloaded", f.Name())


	// Delete object within the new bucket
	_, err = client.DeleteObject(&s3.DeleteObjectInput{Bucket: aws.String(newBucket), Key: aws.String(objectKey)})
	if err != nil {
		exitErrorf("Unable to delete object %q from bucket %q, %v", objectKey, newBucket, err)
	}

	err = client.WaitUntilObjectNotExists(&s3.HeadObjectInput{
		Bucket: aws.String(newBucket),
		Key:    aws.String(objectKey),
	})
	if err != nil {
		exitErrorf("Error occurred while waiting for object %q to be deleted, %v", objectKey)
	}

	fmt.Printf("Object %q successfully deleted\n", objectKey)

	// Delete the new bucket
	// It must be empty or else the call fails
	_, err = client.DeleteBucket(&s3.DeleteBucketInput{
		Bucket: aws.String(newBucket),
	})
	if err != nil {
		exitErrorf("Unable to delete bucket %q, %v", newBucket, err)
	}

	// Wait until bucket is deleted before finishing
	fmt.Printf("Waiting for bucket %q to be deleted...\n", newBucket)

	err = client.WaitUntilBucketNotExists(&s3.HeadBucketInput{
		Bucket: aws.String(newBucket),
	})
	if err != nil {
		exitErrorf("Error occurred while waiting for bucket to be deleted, %v", newBucket)
	}

	fmt.Printf("Bucket %q successfully deleted\n", newBucket)
}


func exitErrorf(msg string, args ...interface{}) {
	fmt.Fprintf(os.Stderr, msg+"\n", args...)
	os.Exit(1)
}