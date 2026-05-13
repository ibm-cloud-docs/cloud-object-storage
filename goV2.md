---

copyright:
  years: 2026, 2026
lastupdated: "2026-05-13"

keywords: object storage, go, v2, sdk, service id, trusted profile credentials

subcollection: cloud-object-storage

---

# Using Go V2
{: #go-v2}

The {{site.data.keyword.cos_full}} SDK for Go v2 provides features to make the most of {{site.data.keyword.cos_full_notm}}.

The {{site.data.keyword.cos_full_notm}} SDK for Go v2 is comprehensive, with many features and capabilities that exceed the scope of this guide. For detailed class and method documentation, see the [Go API reference documentation](https://ibm.github.io/ibm-cos-sdk-go-v2/). Source code can be found in the [GitHub repository](https://github.com/IBM/ibm-cos-sdk-go-v2).

## What's New in v2
{: #go-v2-whatsnew}

The {{site.data.keyword.cos_full_notm}} SDK for Go v2 is a modernized version that is built on the AWS SDK v2 architecture, bringing significant improvements:

* **Modular Architecture**: New namespace `github.com/IBM/ibm-cos-sdk-go-v2` with cleaner organization
* **Enhanced Context Support**: Native `context.Context` support for all API operations, enabling better timeout and cancellation handling
* **Modern Error Handling**: Structured error types that are easier to inspect and handle programmatically
* **Go Modules Support**: First-class support for Go modules with semantic versioning

For developers migrating from v1, see the [Migration Guide](https://github.com/IBM/ibm-cos-sdk-go-v2/blob/main/MIGRATION_GUIDE_V2.md).



## Getting the SDK
{: #go-v2-get}

The easiest way to use the {{site.data.keyword.cos_full_notm}} Go SDK v2 is to use Go modules to manage dependencies. If you aren't familiar with Go modules, you can get up and running by using the [Go Modules in 5-Minutes](https://go.dev/blog/using-go-modules) guide.

### Prerequisites
{: #go-v2-prerequisites}

* **Go 1.23 or later** - The SDK requires a minimum version of Go 1.23 or newer
* An instance of {{site.data.keyword.cos_full_notm}}
* An API key from [IBM Cloud Identity and Access Management](/docs/account?topic=account-userapikey) with at least `Writer` permissions
* The ID of the instance of {{site.data.keyword.cos_full_notm}} that you are working with
* Token acquisition endpoint
* Service endpoint

These values can be found in the IBM Cloud Console by [generating a 'service credential'](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials).

### Installing the SDK
{: #go-v2-install}

Use `go get` to retrieve the SDK to add it to your GOPATH workspace, or project's Go module dependencies. The SDK requires a minimum version of Go 1.23.

```sh
go get github.com/IBM/ibm-cos-sdk-go-v2/config
go get github.com/IBM/ibm-cos-sdk-go-v2/service/s3
go get github.com/IBM/ibm-cos-sdk-go-v2/credentials/ibmiam
```

To update the SDK, use `go get -u` to retrieve the latest version of the SDK:

```sh
go get -u github.com/IBM/ibm-cos-sdk-go-v2/config
go get -u github.com/IBM/ibm-cos-sdk-go-v2/service/s3
go get -u github.com/IBM/ibm-cos-sdk-go-v2/credentials/ibmiam
```

### Import packages
{: #go-v2-import-packages}

After you have installed the SDK, you will need to import the packages that you require into your Go applications to use the SDK, as shown in the following example:

```go
import (
    "context"
    "github.com/IBM/ibm-cos-sdk-go-v2/aws"
    "github.com/IBM/ibm-cos-sdk-go-v2/config"
    "github.com/IBM/ibm-cos-sdk-go-v2/credentials/ibmiam"
    "github.com/IBM/ibm-cos-sdk-go-v2/service/s3"
    "github.com/IBM/ibm-cos-sdk-go-v2/service/s3/types"
)
```

## SDK references
{: #go-v2-reference}

### Core classes
{: #go-v2-reference-core-classes}

* **s3.Client** - Primary client for interacting with {{site.data.keyword.cos_full_notm}}
* **s3.NewFromConfig** - Creates a new S3 client from configuration

### Credentials
{: #go-v2-reference-credentials}

* **ibmiam.NewStaticCredentials** - IBM IAM credentials provider for API key authentication
* **config.WithCredentialsProvider** - Configuration option to set credentials provider

### Configuration
{: #go-v2-reference-configurations}

* **config.LoadDefaultConfig** - Loads SDK configuration with specified options
* **config.WithRegion** - Sets the AWS region for the client
* **config.WithEndpoint** - Sets the service endpoint URL

## Creating a client and sourcing service credentials
{: #go-v2-credentials}

To connect to {{site.data.keyword.cos_full_notm}}, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.

The credentials can be found by creating a [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), or through the CLI.

### Using IBM IAM authentication
{: #go-v2-authentication}

The following example shows how to create a client by using IBM IAM authentication with an API key:

```go
// IBM COS credentials
apiKey := "<API_KEY>"
serviceInstanceID := "<RESOURCE_INSTANCE_ID>"
authEndpoint := "https://iam.cloud.ibm.com/identity/token"
serviceEndpoint := "https://s3.us-south.cloud-object-storage.appdomain.cloud"
region := "us-south"

// Load configuration with IBM IAM credentials
cfg, err := config.LoadDefaultConfig(context.TODO(),
    config.WithCredentialsProvider(
        ibmiam.NewStaticCredentials(authEndpoint, apiKey, serviceInstanceID),
    ),
    config.WithRegion(region),
    config.WithEndpoint(serviceEndpoint),
)
if err != nil {
    log.Fatalf("Failed to load configuration: %v", err)
}

// Create S3 client
client := s3.NewFromConfig(cfg)
```

The required variables are:

* `apiKey` - Your IBM Cloud API key with appropriate permissions
* `serviceInstanceID` - The CRN (Cloud Resource Name) of your {{site.data.keyword.cos_full_notm}} instance
* `authEndpoint` - The IBM IAM token endpoint (typically `https://iam.cloud.ibm.com/identity/token`)
* `serviceEndpoint` - The endpoint URL for your {{site.data.keyword.cos_full_notm}} bucket's region
* `region` - The region where your bucket is located

## Code examples
{: #go-v2-examples}

The following examples assume that you have already created a client as shown in the previous section.

### Creating a bucket
{: #go-v2-examples-create-bucket}

```go
bucketName := "my-new-bucket"

input := &s3.CreateBucketInput{
    Bucket: aws.String(bucketName),
}

_, err = client.CreateBucket(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to create bucket: %v", err)
}

fmt.Printf("Bucket '%s' created successfully\n", bucketName)
```

### Listing available buckets
{: #go-v2-available-bucket}

```go
result, err := client.ListBuckets(context.TODO(), &s3.ListBucketsInput{})
if err != nil {
    log.Fatalf("Failed to list buckets: %v", err)
}

fmt.Println("Buckets:")
for _, bucket := range result.Buckets {
    fmt.Printf("  - %s (created: %v)\n",
        aws.ToString(bucket.Name),
        bucket.CreationDate,
    )
}
```

### Listing buckets with extended information
{: #go-v2-list-bucket}

{{site.data.keyword.cos_full_notm}} provides an extended listing operation that returns additional bucket information:

```go
input := &s3.ListBucketsExtendedInput{
    IBMServiceInstanceId: aws.String(serviceInstanceID),
    Prefix:               aws.String("my-bucket-prefix"),
    MaxKeys:              aws.Int32(100),
}

result, err := client.ListBucketsExtended(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to list buckets: %v", err)
}

fmt.Println("Extended Bucket Information:")
for _, bucket := range result.Buckets {
    fmt.Printf("  Bucket: %s\n", aws.ToString(bucket.Name))
    fmt.Printf("    Location: %s\n", aws.ToString(bucket.LocationConstraint))
    fmt.Printf("    Created: %v\n", bucket.CreationDate)
}
```

### Retrieving a bucket's location
{: #go-v2-location-bucket}

```go
bucketName := "my-bucket"

result, err := client.GetBucketLocation(context.TODO(), &s3.GetBucketLocationInput{
    Bucket: aws.String(bucketName),
})
if err != nil {
    log.Fatalf("Failed to get bucket location: %v", err)
}

fmt.Printf("Bucket '%s' is located in: %s\n",
    bucketName,
    result.LocationConstraint,
)
```

### Deleting a bucket
{: #go-v2-delete-bucket}

```go
bucketName := "my-bucket-to-delete"

_, err = client.DeleteBucket(context.TODO(), &s3.DeleteBucketInput{
    Bucket: aws.String(bucketName),
})
if err != nil {
    log.Fatalf("Failed to delete bucket: %v", err)
}

fmt.Printf("Bucket '%s' deleted successfully\n", bucketName)
```

A bucket must be empty before it can be deleted.
{: note}

### Uploading an object to a bucket
{: #go-v2-upload-object}

```go
bucketName := "my-bucket"
objectKey := "my-object.txt"
content := "Hello, IBM Cloud Object Storage!"

input := &s3.PutObjectInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
    Body:   strings.NewReader(content),
}

result, err := client.PutObject(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to upload object: %v", err)
}

fmt.Printf("Object '%s' uploaded successfully\n", objectKey)
fmt.Printf("ETag: %s\n", aws.ToString(result.ETag))
```

### Downloading an object from a bucket
{: #go-v2-download-bucket}

```go
bucketName := "my-bucket"
objectKey := "my-object.txt"

result, err := client.GetObject(context.TODO(), &s3.GetObjectInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
})
if err != nil {
    log.Fatalf("Failed to download object: %v", err)
}
defer result.Body.Close()

// Read the object content
data, err := io.ReadAll(result.Body)
if err != nil {
    log.Fatalf("Failed to read object data: %v", err)
}

fmt.Printf("Object '%s' downloaded successfully\n", objectKey)
fmt.Printf("Content-Type: %s\n", aws.ToString(result.ContentType))
fmt.Printf("Content-Length: %d bytes\n", result.ContentLength)
fmt.Printf("Content:\n%s\n", string(data))
```

### Listing objects in a bucket
{: #go-v2-list-object}

```go
bucketName := "my-bucket"

input := &s3.ListObjectsV2Input{
    Bucket:  aws.String(bucketName),
    MaxKeys: aws.Int32(1000),
}

result, err := client.ListObjectsV2(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to list objects: %v", err)
}

fmt.Printf("Objects in bucket '%s':\n", bucketName)
for _, object := range result.Contents {
    fmt.Printf("  - %s (size: %d bytes, modified: %v)\n",
        aws.ToString(object.Key),
        object.Size,
        object.LastModified,
    )
}

fmt.Printf("\nTotal objects: %d\n", len(result.Contents))
```

### Copying an object
{: #go-v2-copy-object}

```go
sourceBucket := "source-bucket"
sourceKey := "source-object.txt"
destinationBucket := "destination-bucket"
destinationKey := "destination-object.txt"

// CopySource format: /source-bucket/source-key
copySource := fmt.Sprintf("/%s/%s", sourceBucket, sourceKey)

input := &s3.CopyObjectInput{
    Bucket:     aws.String(destinationBucket),
    Key:        aws.String(destinationKey),
    CopySource: aws.String(copySource),
}

result, err := client.CopyObject(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to copy object: %v", err)
}

fmt.Printf("Object copied successfully\n")
fmt.Printf("Copy ETag: %s\n", aws.ToString(result.CopyObjectResult.ETag))
```

### Deleting an object
{: #go-v2-delete-object}

```go
bucketName := "my-bucket"
objectKey := "object-to-delete.txt"

_, err = client.DeleteObject(context.TODO(), &s3.DeleteObjectInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
})
if err != nil {
    log.Fatalf("Failed to delete object: %v", err)
}

fmt.Printf("Object '%s' deleted successfully\n", objectKey)
```

### Deleting multiple objects
{: #go-v2-delete-multiple-object}

```go
bucketName := "my-bucket"
objectsToDelete := []string{
    "object1.txt",
    "object2.txt",
    "object3.txt",
}

// Build delete request
var objects []types.ObjectIdentifier
for _, key := range objectsToDelete {
    objects = append(objects, types.ObjectIdentifier{
        Key: aws.String(key),
    })
}

input := &s3.DeleteObjectsInput{
    Bucket: aws.String(bucketName),
    Delete: &types.Delete{
        Objects: objects,
        Quiet:   aws.Bool(false),
    },
}

result, err := client.DeleteObjects(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to delete objects: %v", err)
}

fmt.Printf("Successfully deleted %d objects\n", len(result.Deleted))
for _, deleted := range result.Deleted {
    fmt.Printf("  - %s\n", aws.ToString(deleted.Key))
}

if len(result.Errors) > 0 {
    fmt.Printf("\nFailed to delete %d objects:\n", len(result.Errors))
    for _, err := range result.Errors {
        fmt.Printf("  - %s: %s\n",
            aws.ToString(err.Key),
            aws.ToString(err.Message),
        )
    }
}
```

### Getting object metadata (HEAD)
{: #go-v2-get-object}

```go
bucketName := "my-bucket"
objectKey := "my-object.txt"

result, err := client.HeadObject(context.TODO(), &s3.HeadObjectInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
})
if err != nil {
    log.Fatalf("Failed to get object metadata: %v", err)
}

fmt.Printf("Object Metadata for '%s':\n", objectKey)
fmt.Printf("  Content-Type: %s\n", aws.ToString(result.ContentType))
fmt.Printf("  Content-Length: %d bytes\n", result.ContentLength)
fmt.Printf("  ETag: %s\n", aws.ToString(result.ETag))
fmt.Printf("  Last-Modified: %v\n", result.LastModified)

if len(result.Metadata) > 0 {
    fmt.Println("  Custom Metadata:")
    for key, value := range result.Metadata {
        fmt.Printf("    %s: %s\n", key, value)
    }
}
```

### Using multipart uploads
{: #go-v2-upload-multipart-object}

For large objects, multipart upload provides improved throughput and the ability to resume uploads. Each part must be at least 5 MB (except the last part).

```go
bucketName := "my-bucket"
objectKey := "large-object.dat"

// Step 1: Initiate multipart upload
createResp, err := client.CreateMultipartUpload(context.TODO(),
    &s3.CreateMultipartUploadInput{
        Bucket: aws.String(bucketName),
        Key:    aws.String(objectKey),
    },
)
if err != nil {
    log.Fatalf("Failed to initiate multipart upload: %v", err)
}
uploadID := aws.ToString(createResp.UploadId)
fmt.Printf("Multipart upload initiated with ID: %s\n", uploadID)

// Step 2: Upload parts (minimum 5MB per part except last)
var completedParts []types.CompletedPart
minPartSize := 5 * 1024 * 1024 // 5MB

// Create sample parts
parts := []string{
    strings.Repeat("A", minPartSize),
    strings.Repeat("B", minPartSize),
}

for i, partData := range parts {
    partNumber := int32(i + 1)

    uploadResp, err := client.UploadPart(context.TODO(), &s3.UploadPartInput{
        Bucket:     aws.String(bucketName),
        Key:        aws.String(objectKey),
        PartNumber: aws.Int32(partNumber),
        UploadId:   aws.String(uploadID),
        Body:       strings.NewReader(partData),
    })
    if err != nil {
        // Abort multipart upload on error
        client.AbortMultipartUpload(context.TODO(), &s3.AbortMultipartUploadInput{
            Bucket:   aws.String(bucketName),
            Key:      aws.String(objectKey),
            UploadId: aws.String(uploadID),
        })
        log.Fatalf("Failed to upload part %d: %v", partNumber, err)
    }

    completedParts = append(completedParts, types.CompletedPart{
        ETag:       uploadResp.ETag,
        PartNumber: aws.Int32(partNumber),
    })
    fmt.Printf("Part %d uploaded (ETag: %s)\n", partNumber, aws.ToString(uploadResp.ETag))
}

// Step 3: Complete multipart upload
completeResp, err := client.CompleteMultipartUpload(context.TODO(),
    &s3.CompleteMultipartUploadInput{
        Bucket:   aws.String(bucketName),
        Key:      aws.String(objectKey),
        UploadId: aws.String(uploadID),
        MultipartUpload: &types.CompletedMultipartUpload{
            Parts: completedParts,
        },
    },
)
if err != nil {
    log.Fatalf("Failed to complete multipart upload: %v", err)
}

fmt.Printf("Multipart upload completed successfully\n")
fmt.Printf("Location: %s\n", aws.ToString(completeResp.Location))
fmt.Printf("ETag: %s\n", aws.ToString(completeResp.ETag))
```

### Listing multipart uploads
{: #go-v2-list-multipart-upload}

```go
bucketName := "my-bucket"

result, err := client.ListMultipartUploads(context.TODO(),
    &s3.ListMultipartUploadsInput{
        Bucket: aws.String(bucketName),
    },
)
if err != nil {
    log.Fatalf("Failed to list multipart uploads: %v", err)
}

fmt.Printf("In-progress multipart uploads in bucket '%s':\n", bucketName)
for _, upload := range result.Uploads {
    fmt.Printf("  Key: %s\n", aws.ToString(upload.Key))
    fmt.Printf("    Upload ID: %s\n", aws.ToString(upload.UploadId))
    fmt.Printf("    Initiated: %v\n", upload.Initiated)
}
```

### Setting a bucket lifecycle configuration
{: #go-v2-bucket-lifecycle}

Archive policies allow you to automatically transition objects to archive storage classes after a specified time period:

```go
bucketName := "my-bucket"

// Configure lifecycle rule to archive objects after 90 days
input := &s3.PutBucketLifecycleConfigurationInput{
    Bucket: aws.String(bucketName),
    LifecycleConfiguration: &types.BucketLifecycleConfiguration{
        Rules: []types.LifecycleRule{
            {
                ID:     aws.String("archive-rule"),
                Status: types.ExpirationStatusEnabled,
                Filter: &types.LifecycleRuleFilterMemberPrefix{
                    Value: "documents/",
                },
                Transitions: []types.Transition{
                    {
                        Days:         aws.Int32(90),
                        StorageClass: types.TransitionStorageClassGlacier,
                    },
                },
            },
        },
    },
}

_, err = client.PutBucketLifecycleConfiguration(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to set lifecycle configuration: %v", err)
}

fmt.Printf("Lifecycle configuration set for bucket '%s'\n", bucketName)
```

### Getting a bucket lifecycle configuration
{: #go-v2-get-bucket-lifecycle}

```go
bucketName := "my-bucket"

result, err := client.GetBucketLifecycleConfiguration(context.TODO(),
    &s3.GetBucketLifecycleConfigurationInput{
        Bucket: aws.String(bucketName),
    },
)
if err != nil {
    log.Fatalf("Failed to get lifecycle configuration: %v", err)
}

fmt.Printf("Lifecycle rules for bucket '%s':\n", bucketName)
for _, rule := range result.Rules {
    fmt.Printf("  Rule ID: %s\n", aws.ToString(rule.ID))
    fmt.Printf("    Status: %s\n", rule.Status)

    for _, transition := range rule.Transitions {
        fmt.Printf("    Transition to %s after %d days\n",
            transition.StorageClass,
            aws.ToInt32(transition.Days),
        )
    }
}
```

### Enabling bucket versioning
{: #go-v2-enable-versioning}

```go
bucketName := "my-bucket"

// Enable versioning
_, err = client.PutBucketVersioning(context.TODO(), &s3.PutBucketVersioningInput{
    Bucket: aws.String(bucketName),
    VersioningConfiguration: &types.VersioningConfiguration{
        Status: types.BucketVersioningStatusEnabled,
    },
})
if err != nil {
    log.Fatalf("Failed to enable versioning: %v", err)
}

fmt.Printf("Versioning enabled for bucket '%s'\n", bucketName)
```

### Listing object versions
{: #go-v2-list-version}

```go
bucketName := "my-bucket"

result, err := client.ListObjectVersions(context.TODO(),
    &s3.ListObjectVersionsInput{
        Bucket: aws.String(bucketName),
    },
)
if err != nil {
    log.Fatalf("Failed to list object versions: %v", err)
}

fmt.Printf("Object versions in bucket '%s':\n", bucketName)
for _, version := range result.Versions {
    fmt.Printf("  Key: %s\n", aws.ToString(version.Key))
    fmt.Printf("    Version ID: %s\n", aws.ToString(version.VersionId))
    fmt.Printf("    Is Latest: %t\n", aws.ToBool(version.IsLatest))
    fmt.Printf("    Last Modified: %v\n", version.LastModified)
}
```

### Setting CORS configuration
{: #go-v2-set-cors}

```go
bucketName := "my-bucket"

// Set CORS configuration
input := &s3.PutBucketCorsInput{
    Bucket: aws.String(bucketName),
    CORSConfiguration: &types.CORSConfiguration{
        CORSRules: []types.CORSRule{
            {
                AllowedHeaders: []string{"*"},
                AllowedMethods: []string{"GET", "PUT", "POST", "DELETE"},
                AllowedOrigins: []string{"https://example.com"},
                ExposeHeaders:  []string{"ETag"},
                MaxAgeSeconds:  aws.Int32(3000),
            },
        },
    },
}

_, err = client.PutBucketCors(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to set CORS configuration: %v", err)
}

fmt.Printf("CORS configuration set for bucket '%s'\n", bucketName)
```

### Setting object tagging
{: #go-v2-set-tagging}

```go
bucketName := "my-bucket"
objectKey := "my-object.txt"

// Set object tags
input := &s3.PutObjectTaggingInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
    Tagging: &types.Tagging{
        TagSet: []types.Tag{
            {
                Key:   aws.String("Department"),
                Value: aws.String("Finance"),
            },
            {
                Key:   aws.String("Project"),
                Value: aws.String("Q4-2024"),
            },
            {
                Key:   aws.String("Classification"),
                Value: aws.String("Confidential"),
            },
        },
    },
}

_, err = client.PutObjectTagging(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to set object tags: %v", err)
}

fmt.Printf("Tags set for object '%s'\n", objectKey)
```

### Getting object tags
{: #go-v2-get-object-tag}

```go
bucketName := "my-bucket"
objectKey := "my-object.txt"

result, err := client.GetObjectTagging(context.TODO(), &s3.GetObjectTaggingInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
})
if err != nil {
    log.Fatalf("Failed to get object tags: %v", err)
}

fmt.Printf("Tags for object '%s':\n", objectKey)
for _, tag := range result.TagSet {
    fmt.Printf("  %s: %s\n", aws.ToString(tag.Key), aws.ToString(tag.Value))
}
```

### Restoring an archived object
{: #go-v2-restore-object}

Objects in archive storage classes must be restored before they can be accessed:

```go
bucketName := "my-bucket"
objectKey := "archived-object.txt"

// Restore object with accelerated retrieval (2 hours)
input := &s3.RestoreObjectInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
    RestoreRequest: &types.RestoreRequest{
        Days: aws.Int32(7), // Number of days to keep restored copy
        GlacierJobParameters: &types.GlacierJobParameters{
            Tier: types.TierAccelerated, // Accelerated (2 hours) or Standard (12 hours)
        },
    },
}

_, err = client.RestoreObject(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to restore object: %v", err)
}

fmt.Printf("Restore request submitted for object '%s'\n", objectKey)
fmt.Println("The object will be available for download in approximately 2 hours")
```

{{site.data.keyword.cos_full_notm}} supports accelerated archive with restore times of 2 hours or 12 hours, depending on the tier selected.
{: note}

### Setting bucket protection configuration
{: #go-v2-set-bucket-protection-configuration}

{{site.data.keyword.cos_full_notm}} supports Immutable Object Storage to prevent objects from being modified or deleted:

```go
bucketName := "my-protected-bucket"

// Set protection configuration with default retention period
input := &s3.PutBucketProtectionConfigurationInput{
    Bucket: aws.String(bucketName),
    ProtectionConfiguration: &types.ProtectionConfiguration{
        Status:                            types.BucketProtectionStatusRetention,
        MinimumRetention:                  &types.BucketProtectionMinimumRetention{Days: aws.Int32(90)},
        MaximumRetention:                  &types.BucketProtectionMaximumRetention{Days: aws.Int32(365)},
        DefaultRetention:                  &types.BucketProtectionDefaultRetention{Days: aws.Int32(120)},
        EnablePermanentRetention:          aws.Bool(false),
    },
}

_, err = client.PutBucketProtectionConfiguration(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to set protection configuration: %v", err)
}

fmt.Printf("Protection configuration set for bucket '%s'\n", bucketName)
fmt.Println("Objects will be protected with a default retention of 120 days")
```

### Getting bucket protection configuration
{: #go-v2-get-bucket-protection-configuration}

```go
bucketName := "my-protected-bucket"

result, err := client.GetBucketProtectionConfiguration(context.TODO(),
    &s3.GetBucketProtectionConfigurationInput{
        Bucket: aws.String(bucketName),
    },
)
if err != nil {
    log.Fatalf("Failed to get protection configuration: %v", err)
}

fmt.Printf("Protection configuration for bucket '%s':\n", bucketName)
fmt.Printf("  Status: %s\n", result.ProtectionConfiguration.Status)
fmt.Printf("  Minimum Retention: %d days\n",
    aws.ToInt32(result.ProtectionConfiguration.MinimumRetention.Days))
fmt.Printf("  Maximum Retention: %d days\n",
    aws.ToInt32(result.ProtectionConfiguration.MaximumRetention.Days))
fmt.Printf("  Default Retention: %d days\n",
    aws.ToInt32(result.ProtectionConfiguration.DefaultRetention.Days))
```

### Adding a legal hold to an object
{: #go-v2-add-legal-hold}

Legal holds prevent an object from being deleted or modified, regardless of retention periods:

```go
bucketName := "my-protected-bucket"
objectKey := "important-document.pdf"
legalHoldID := "legal-case-2024-001"

// Add legal hold to the object
_, err = client.AddLegalHold(context.TODO(), &s3.AddLegalHoldInput{
    Bucket:               aws.String(bucketName),
    Key:                  aws.String(objectKey),
    RetentionLegalHoldId: aws.String(legalHoldID),
})
if err != nil {
    log.Fatalf("Failed to add legal hold: %v", err)
}

fmt.Printf("Legal hold '%s' added to object '%s'\n", legalHoldID, objectKey)
fmt.Println("The object cannot be deleted or modified until the legal hold is removed")
```

The bucket must have an Object Lock enabled to use legal holds.
{: note}

### Listing legal holds on an object
{: #go-v2-list-legal-hold}

```go
bucketName := "my-protected-bucket"
objectKey := "important-document.pdf"

// List legal holds
result, err := client.ListLegalHolds(context.TODO(), &s3.ListLegalHoldsInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
})
if err != nil {
    log.Fatalf("Failed to list legal holds: %v", err)
}

fmt.Printf("Legal holds on object '%s':\n", objectKey)
if len(result.LegalHolds) == 0 {
    fmt.Println("  No legal holds")
} else {
    for _, hold := range result.LegalHolds {
        fmt.Printf("  - ID: %s\n", aws.ToString(hold.ID))
        fmt.Printf("    Date: %v\n", hold.Date)
    }
}
```

### Deleting a legal hold from an object
{: #go-v2-set-legal-hold}

```go
bucketName := "my-protected-bucket"
objectKey := "important-document.pdf"
legalHoldID := "legal-case-2024-001"

// Delete legal hold
_, err = client.DeleteLegalHold(context.TODO(), &s3.DeleteLegalHoldInput{
    Bucket:               aws.String(bucketName),
    Key:                  aws.String(objectKey),
    RetentionLegalHoldId: aws.String(legalHoldID),
})
if err != nil {
    log.Fatalf("Failed to delete legal hold: %v", err)
}

fmt.Printf("Legal hold '%s' removed from object '%s'\n", legalHoldID, objectKey)
```

### Setting object retention
{: #go-v2-set-object-retention}

```go
bucketName := "my-protected-bucket"
objectKey := "important-document.pdf"

// Set retention until a specific date
retainUntilDate := time.Now().AddDate(0, 6, 0) // 6 months from now

input := &s3.PutObjectRetentionInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
    Retention: &types.ObjectLockRetention{
        Mode:            types.ObjectLockRetentionModeCompliance,
        RetainUntilDate: aws.Time(retainUntilDate),
    },
}

_, err = client.PutObjectRetention(context.TODO(), input)
if err != nil {
    log.Fatalf("Failed to set object retention: %v", err)
}

fmt.Printf("Retention set for object '%s' until %v\n", objectKey, retainUntilDate)
```

### Getting object retention
{: #go-v2-get-object-retention}

```go
bucketName := "my-protected-bucket"
objectKey := "important-document.pdf"

result, err := client.GetObjectRetention(context.TODO(), &s3.GetObjectRetentionInput{
    Bucket: aws.String(bucketName),
    Key:    aws.String(objectKey),
})
if err != nil {
    log.Fatalf("Failed to get object retention: %v", err)
}

fmt.Printf("Retention for object '%s':\n", objectKey)
fmt.Printf("  Mode: %s\n", result.Retention.Mode)
fmt.Printf("  Retain Until: %v\n", result.Retention.RetainUntilDate)
```

## Next steps
{: #go-v2-next-steps}

* Review the [Go API reference documentation](https://ibm.github.io/ibm-cos-sdk-go-v2/) for detailed information on all available methods and types
* Explore the [GitHub repository](https://github.com/IBM/ibm-cos-sdk-go-v2) for more examples and source code
* Read the [Migration Guide](https://github.com/IBM/ibm-cos-sdk-go-v2/blob/main/MIGRATION_GUIDE_V2.md) if you're upgrading from v1
* Check out the [IBM Cloud Object Storage documentation](https://cloud.ibm.com/docs/cloud-object-storage) for service-specific features and best practices
* For help and support:
   * Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/object-storage+ibm) with tags `ibm` and `object-storage`
   * Open an issue on [GitHub](https://github.com/IBM/ibm-cos-sdk-go-v2/issues)
   * Contact [IBM Cloud Support](https://cloud.ibm.com/unifiedsupport/supportcenter/)
