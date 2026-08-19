---

copyright:
  years: 2017, 2026
lastupdated: "2026-08-17"

keywords: object storage, node, javascript, sdk

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Using Node.js V2
{: #node-v2}

The {{site.data.keyword.cos_full}} SDK for Node.js v2 provides features to make the most of {{site.data.keyword.cos_full_notm}}.

The {{site.data.keyword.cos_full_notm}} SDK for Node.js v2 is comprehensive, with many features and capabilities that exceed the scope and space of this guide. For detailed class and method documentation, see the [Node.js API reference documentation](https://ibm.github.io/ibm-cos-sdk-js-v2/). Source code can be found in the [GitHub repository](https://github.com/IBM/ibm-cos-sdk-js-v2).

## What's New in v2
{: #node-v2-whatsnew}

The {{site.data.keyword.cos_full_notm}} SDK for Node.js v2 is a modernized version that is built on the AWS SDK v2 architecture, bringing significant improvements:

- **Modular architecture** - Import only the commands and clients you need
- **Promise-first design** - Native async/await support with cleaner error handling
- **Smaller bundle sizes** - Tree-shakeable modules reduce application size
- **Modern JavaScript** - Leverages ES6+ features and TypeScript support
- **Middleware stack** - Extensible request/response pipeline
- **Better error handling** - Structured error types with detailed information

For developers migrating from v1, see the [Migration Guide](https://github.com/IBM/ibm-cos-sdk-js-v2/blob/main/MIGRATION_GUIDE_V2.md).


## Getting the SDK
{: #node-v2-get}

The preferred way to install the IBM COS SDK for Node.js is to use the [npm](https://www.npmjs.com) package manager for Node.js. Simply type the following into a terminal window:
```sh
npm install ibm-cos-sdk-v2
```

### Prerequisites
{: #node-v2-prerequisites}

* **Node.js 18 or later** - The SDK requires a minimum version of Node.js 18 or newer.
* An instance of {{site.data.keyword.cos_full_notm}}
* An API key from [IBM Cloud Identity and Access Management](/docs/account?topic=account-userapikey) with at least `Writer` permissions
* The ID of the instance of COS that you are working with
* Token acquisition endpoint
* Service endpoint

These values can be found in the IBM Cloud Console by [generating a 'service credential'](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials).

### Import packages
{: #node-v2-import-packages}

After you have installed the SDK, you will need to import the packages that you require into your Node.js applications to use the SDK, as shown in the following example:

**CommonJS:**

```javascript
const { S3Client } = require('ibm-cos-sdk-v2');
const {
  CreateBucketCommand,
  ListBucketsCommand,
  PutObjectCommand,
  GetObjectCommand
} = require('ibm-cos-sdk-v2');
```

**ES Modules / TypeScript:**

```typescript
import { S3Client } from 'ibm-cos-sdk-v2';
import {
  CreateBucketCommand,
  ListBucketsCommand,
  PutObjectCommand,
  GetObjectCommand
} from 'ibm-cos-sdk-v2';
```

## SDK References
{: #node-v2-reference}

### Core Classes
{: #node-v2-reference-core-classes}

* **S3Client** - Primary client for interacting with {{site.data.keyword.cos_full_notm}}
* **Command classes** - Each operation has a corresponding command class (e.g., `PutObjectCommand`, `GetObjectCommand`)

### Configuration
{: #node-v2-reference-configurations}

* **S3Client constructor** - Creates a new S3 client with configuration options
* **region** - Sets the region for the client
* **endpoint** - Sets the service endpoint URL
* **credentials** - Sets authentication credentials


## Creating a Client and Sourcing Service Credentials
{: #node-v2-credentials}

To connect to {{site.data.keyword.cos_full_notm}}, a client is created and configured by providing credential information (API key and service instance ID). These values can also be automatically sourced from a credentials file or from environment variables.

The credentials can be found by creating a [Service Credential](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), or through the CLI.

### Using IBM IAM Authentication
{: #node-v2-authentication}

The following example shows how to create a client using IBM IAM authentication with an API key:

**CommonJS:**

```javascript
const { S3Client } = require('ibm-cos-sdk-v2');

// Initialize client
const client = new S3Client({
  endpoint: 'https://s3.us-south.cloud-object-storage.appdomain.cloud',
  region: 'us-south',
  credentials: {
    apiKey: '<API_KEY>',
    serviceInstanceId: '<SERVICE_INSTANCE_ID>'
  }
});
```

**TypeScript:**

```typescript
import { S3Client } from 'ibm-cos-sdk-v2';

const client = new S3Client({
  endpoint: 'https://s3.us-south.cloud-object-storage.appdomain.cloud',
  region: 'us-south',
  credentials: {
    apiKey: '<API_KEY>',
    serviceInstanceId: '<SERVICE_INSTANCE_ID>'
  }
});
```

The required configuration options are:

* `endpoint` - The endpoint URL for your COS bucket's region
* `region` - The region where your bucket is located
* `credentials.apiKey` - Your IBM Cloud API key with appropriate permissions
* `credentials.serviceInstanceId` - The CRN (Cloud Resource Name) of your COS instance

## Code Examples
{: #node-v2-examples}

The following examples assume you have already created a client as shown in the previous section.

### Creating a bucket
{: #node-v2-examples-create-bucket}

```javascript
const { CreateBucketCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-new-bucket';

const command = new CreateBucketCommand({
  Bucket: bucketName
});

try {
  await client.send(command);
  console.log(`Bucket '${bucketName}' created successfully`);
} catch (err) {
  console.error('Failed to create bucket:', err);
}
```

### Listing available buckets
{: #node-v2-available-bucket}

```javascript
const { ListBucketsCommand } = require('ibm-cos-sdk-v2');

try {
  const command = new ListBucketsCommand({});
  const response = await client.send(command);

  console.log('Buckets:');
  if (response.Buckets && response.Buckets.length > 0) {
    response.Buckets.forEach(bucket => {
      console.log(` - ${bucket.Name} (created: ${bucket.CreationDate})`);
    });
  } else {
    console.log(' No buckets found');
  }
} catch (err) {
  console.error('Failed to list buckets:', err);
}
```

### Listing buckets with extended information (IBM Extension)
{: #node-v2-list-bucket}

{{site.data.keyword.cos_full_notm}} provides an extended listing operation that returns additional bucket information:

```javascript
const { ListBucketsExtendedCommand } = require('ibm-cos-sdk-v2');

const command = new ListBucketsExtendedCommand({
  IBMServiceInstanceId: '<SERVICE_INSTANCE_ID>',
  Prefix: 'my-bucket-prefix',
  MaxKeys: 100
});

try {
  const response = await client.send(command);

  console.log('Extended Bucket Information:');
  if (response.Buckets && response.Buckets.length > 0) {
    response.Buckets.forEach(bucket => {
      console.log(` Bucket: ${bucket.Name}`);
      console.log(` Location: ${bucket.LocationConstraint}`);
      console.log(` Created: ${bucket.CreationDate}`);
    });
  } else {
    console.log(' No buckets found');
  }
} catch (err) {
  console.error('Failed to list buckets:', err);
}
```

### Retrieving a bucket's location
{: #node-v2-location-bucket}

```javascript
const { GetBucketLocationCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

const command = new GetBucketLocationCommand({
  Bucket: bucketName
});

try {
  const response = await client.send(command);
  console.log(`Bucket '${bucketName}' is located in: ${response.LocationConstraint}`);
} catch (err) {
  console.error('Failed to get bucket location:', err);
}
```

### Deleting a bucket
{: #node-v2-delete-bucket}

```javascript
const { DeleteBucketCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket-to-delete';

const command = new DeleteBucketCommand({
  Bucket: bucketName
});

try {
  await client.send(command);
  console.log(`Bucket '${bucketName}' deleted successfully`);
} catch (err) {
  console.error('Failed to delete bucket:', err);
}
```

**Note**: A bucket must be empty before it can be deleted.

### Uploading an object to a bucket
{: #node-v2-upload-object}

```javascript
const { PutObjectCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'my-object.txt';
const content = 'Hello, IBM Cloud Object Storage!';

const command = new PutObjectCommand({
  Bucket: bucketName,
  Key: objectKey,
  Body: content
});

try {
  const response = await client.send(command);
  console.log(`Object '${objectKey}' uploaded successfully`);
  console.log(`ETag: ${response.ETag}`);
} catch (err) {
  console.error('Failed to upload object:', err);
}
```

### Downloading an object from a bucket
{: #node-v2-download-bucket}

```javascript
const { GetObjectCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'my-object.txt';

const command = new GetObjectCommand({
  Bucket: bucketName,
  Key: objectKey
});

try {
  const response = await client.send(command);

  // Convert stream to buffer
  const chunks = [];
  for await (const chunk of response.Body) {
    chunks.push(chunk);
  }
  const buffer = Buffer.concat(chunks);

  console.log(`Object '${objectKey}' downloaded successfully`);
  console.log(`Content-Type: ${response.ContentType}`);
  console.log(`Content-Length: ${response.ContentLength} bytes`);
  console.log(`Size: ${buffer.length} bytes`);
} catch (err) {
  console.error('Failed to download object:', err);
}
```

### Listing objects in a bucket
{: #node-v2-list-object}

```javascript
const { ListObjectsV2Command } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

const command = new ListObjectsV2Command({
  Bucket: bucketName,
  MaxKeys: 1000
});

try {
  const response = await client.send(command);

  console.log(`Objects in bucket '${bucketName}':`);
  if (response.Contents && response.Contents.length > 0) {
    response.Contents.forEach(object => {
      console.log(` - ${object.Key} (size: ${object.Size} bytes, modified: ${object.LastModified})`);
    });
    console.log(`\nTotal objects: ${response.Contents.length}`);
  } else {
    console.log(' No objects found');
  }
} catch (err) {
  console.error('Failed to list objects:', err);
}
```

### Copying an object
{: #node-v2-copy-object}

```javascript
const { CopyObjectCommand } = require('ibm-cos-sdk-v2');

const sourceBucket = 'source-bucket';
const sourceKey = 'source-object.txt';
const destinationBucket = 'destination-bucket';
const destinationKey = 'destination-object.txt';

// CopySource format: source-bucket/source-key
const copySource = `${sourceBucket}/${sourceKey}`;

const command = new CopyObjectCommand({
  Bucket: destinationBucket,
  Key: destinationKey,
  CopySource: copySource
});

try {
  const response = await client.send(command);
  console.log('Object copied successfully');
  if (response.CopyObjectResult) {
    console.log(`ETag: ${response.CopyObjectResult.ETag}`);
  }
} catch (err) {
  console.error('Failed to copy object:', err);
}
```

### Deleting an object
{: #node-v2-delete-object}

```javascript
const { DeleteObjectCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'object-to-delete.txt';

const command = new DeleteObjectCommand({
  Bucket: bucketName,
  Key: objectKey
});

try {
  await client.send(command);
  console.log(`Object '${objectKey}' deleted successfully`);
} catch (err) {
  console.error('Failed to delete object:', err);
}
```

### Deleting multiple objects
{: #node-v2-delete-multiple-object}

```javascript
const { DeleteObjectsCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectsToDelete = [
  'object1.txt',
  'object2.txt',
  'object3.txt'
];

// Build delete request
const objects = objectsToDelete.map(key => ({ Key: key }));

const command = new DeleteObjectsCommand({
  Bucket: bucketName,
  Delete: {
    Objects: objects,
    Quiet: false
  }
});

try {
  const response = await client.send(command);

  console.log('Delete operation completed');

  if (response.Deleted && response.Deleted.length > 0) {
    console.log(`Successfully deleted ${response.Deleted.length} object(s):`);
    response.Deleted.forEach(deleted => {
      console.log(` - ${deleted.Key}`);
    });
  }

  if (response.Errors && response.Errors.length > 0) {
    console.log(`\nFailed to delete ${response.Errors.length} object(s):`);
    response.Errors.forEach(error => {
      console.log(` - ${error.Key}: ${error.Message}`);
    });
  }
} catch (err) {
  console.error('Failed to delete objects:', err);
}
```

### Getting object metadata (HEAD)
{: #node-v2-get-object}

```javascript
const { HeadObjectCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'my-object.txt';

const command = new HeadObjectCommand({
  Bucket: bucketName,
  Key: objectKey
});

try {
  const response = await client.send(command);

  console.log(`Object Metadata for '${objectKey}':`);
  console.log(` Content-Type: ${response.ContentType}`);
  console.log(` Content-Length: ${response.ContentLength} bytes`);
  console.log(` ETag: ${response.ETag}`);
  console.log(` Last-Modified: ${response.LastModified}`);

  if (response.Metadata && Object.keys(response.Metadata).length > 0) {
    console.log(' Custom Metadata:');
    for (const [key, value] of Object.entries(response.Metadata)) {
      console.log(`  ${key}: ${value}`);
    }
  }
} catch (err) {
  console.error('Failed to get object metadata:', err);
}
```



### Using multipart uploads
{: #node-v2-upload-multipart-object}

For large objects, multipart upload provides improved throughput and the ability to resume uploads. Each part must be at least 5 MB (except the last part).

**Manual Multipart Upload:**

```javascript
const {
  CreateMultipartUploadCommand,
  UploadPartCommand,
  CompleteMultipartUploadCommand,
  AbortMultipartUploadCommand
} = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'large-object.dat';

try {
  // Step 1: Initiate multipart upload
  const createCommand = new CreateMultipartUploadCommand({
    Bucket: bucketName,
    Key: objectKey
  });

  const createResponse = await client.send(createCommand);
  const uploadId = createResponse.UploadId;
  console.log(`Multipart upload initiated with ID: ${uploadId}`);

  // Step 2: Upload parts (minimum 5MB per part except last)
  const completedParts = [];
  const minPartSize = 5 * 1024 * 1024; // 5MB

  // Create sample parts
  const parts = [
    'A'.repeat(minPartSize),
    'B'.repeat(minPartSize)
  ];

  for (let i = 0; i < parts.length; i++) {
    const partNumber = i + 1;
    const uploadPartCommand = new UploadPartCommand({
      Bucket: bucketName,
      Key: objectKey,
      PartNumber: partNumber,
      UploadId: uploadId,
      Body: parts[i]
    });

    try {
      const uploadResponse = await client.send(uploadPartCommand);
      completedParts.push({
        ETag: uploadResponse.ETag,
        PartNumber: partNumber
      });
      console.log(`Part ${partNumber} uploaded (ETag: ${uploadResponse.ETag})`);
    } catch (err) {
      // Abort multipart upload on error
      const abortCommand = new AbortMultipartUploadCommand({
        Bucket: bucketName,
        Key: objectKey,
        UploadId: uploadId
      });
      await client.send(abortCommand);
      throw err;
    }
  }

  // Step 3: Complete multipart upload
  const completeCommand = new CompleteMultipartUploadCommand({
    Bucket: bucketName,
    Key: objectKey,
    UploadId: uploadId,
    MultipartUpload: {
      Parts: completedParts
    }
  });

  const completeResponse = await client.send(completeCommand);
  console.log('Multipart upload completed successfully');
  console.log(`Location: ${completeResponse.Location}`);
  console.log(`ETag: ${completeResponse.ETag}`);
} catch (err) {
  console.error('Failed to complete multipart upload:', err);
}
```

**Using Upload Manager (Recommended):**

For easier multipart uploads, use the `@ibm-cos/lib-storage` package:

```javascript
const { Upload } = require('@ibm-cos/lib-storage');
const { S3Client } = require('ibm-cos-sdk-v2');
const fs = require('fs');

const fileStream = fs.createReadStream('large-file.bin');

const upload = new Upload({
  client: client,
  params: {
    Bucket: 'my-bucket',
    Key: 'large-file.bin',
    Body: fileStream
  },
  queueSize: 4, // Concurrent parts
  partSize: 5 * 1024 * 1024, // 5MB parts
  leavePartsOnError: false
});

// Track progress
upload.on('httpUploadProgress', (progress) => {
  console.log('Upload progress:', progress);
});

try {
  const result = await upload.done();
  console.log('Upload completed:', result);
} catch (err) {
  console.error('Upload failed:', err);
}
```

### Listing multipart uploads
{: #node-v2-list-multipart-upload}

```javascript
const { ListMultipartUploadsCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

const command = new ListMultipartUploadsCommand({
  Bucket: bucketName
});

try {
  const response = await client.send(command);

  console.log(`In-progress multipart uploads in bucket '${bucketName}':`);
  if (response.Uploads && response.Uploads.length > 0) {
    response.Uploads.forEach(upload => {
      console.log(` Key: ${upload.Key}`);
      console.log(` Upload ID: ${upload.UploadId}`);
      console.log(` Initiated: ${upload.Initiated}`);
    });
  } else {
    console.log(' No in-progress uploads found');
  }
} catch (err) {
  console.error('Failed to list multipart uploads:', err);
}
```

### Setting a bucket lifecycle configuration
{: #node-v2-bucket-lifecycle}

Archive policies allow you to automatically transition objects to archive storage classes after a specified time period:

```javascript
const { PutBucketLifecycleConfigurationCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

// Configure lifecycle rule to archive objects after 90 days
const command = new PutBucketLifecycleConfigurationCommand({
  Bucket: bucketName,
  LifecycleConfiguration: {
    Rules: [
      {
        ID: 'delete-old-logs',
        Status: 'Enabled',
        Filter: {
          Prefix: 'logs/',
        },
        Expiration: {
          Days: 30,
        },
      },
      {
        ID: 'cleanup-multipart-uploads',
        Status: 'Enabled',
        Filter: {
          Prefix: '',
        },
        AbortIncompleteMultipartUpload: {
          DaysAfterInitiation: 7,
        },
      },
    ]
  }
});

try {
  await client.send(command);
  console.log(`Lifecycle configuration set for bucket '${bucketName}'`);
} catch (err) {
  console.error('Failed to set lifecycle configuration:', err);
}
```

### Getting a bucket lifecycle configuration
{: #node-v2-get-bucket-lifecycle}

```javascript
const { GetBucketLifecycleConfigurationCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

const command = new GetBucketLifecycleConfigurationCommand({
  Bucket: bucketName
});

try {
  const response = await client.send(command);

  console.log(`Lifecycle rules for bucket '${bucketName}':`);
  if (response.Rules && response.Rules.length > 0) {
    response.Rules.forEach(rule => {
      console.log(` Rule ID: ${rule.ID}`);
      console.log(` Status: ${rule.Status}`);
      if (rule.Transitions && rule.Transitions.length > 0) {
        rule.Transitions.forEach(transition => {
          console.log(` Transition to ${transition.StorageClass} after ${transition.Days} days`);
        });
      }
    });
  } else {
    console.log(' No lifecycle rules found');
  }
} catch (err) {
  console.error('Failed to get lifecycle configuration:', err);
}
```



### Enabling bucket versioning
{: #node-v2-enable-versioning}

```javascript
const { PutBucketVersioningCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

// Enable versioning
const command = new PutBucketVersioningCommand({
  Bucket: bucketName,
  VersioningConfiguration: {
    Status: 'Enabled'
  }
});

try {
  await client.send(command);
  console.log(`Versioning enabled for bucket '${bucketName}'`);
} catch (err) {
  console.error('Failed to enable versioning:', err);
}
```

### Listing object versions
{: #node-v2-list-version}

```javascript
const { ListObjectVersionsCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

const command = new ListObjectVersionsCommand({
  Bucket: bucketName
});

try {
  const response = await client.send(command);

  console.log(`Object versions in bucket '${bucketName}':`);
  if (response.Versions && response.Versions.length > 0) {
    response.Versions.forEach(version => {
      console.log(` Key: ${version.Key}`);
      console.log(` Version ID: ${version.VersionId}`);
      console.log(` Is Latest: ${version.IsLatest}`);
      console.log(` Last Modified: ${version.LastModified}`);
    });
  } else {
    console.log(' No versions found');
  }
} catch (err) {
  console.error('Failed to list object versions:', err);
}
```

### Setting CORS configuration
{: #node-v2-set-cors}

```javascript
const { PutBucketCorsCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';

// Set CORS configuration
const command = new PutBucketCorsCommand({
  Bucket: bucketName,
  CORSConfiguration: {
    CORSRules: [
      {
        AllowedHeaders: ['*'],
        AllowedMethods: ['GET', 'PUT', 'POST', 'DELETE'],
        AllowedOrigins: ['https://example.com'],
        ExposeHeaders: ['ETag'],
        MaxAgeSeconds: 3000
      }
    ]
  }
});

try {
  await client.send(command);
  console.log(`CORS configuration set for bucket '${bucketName}'`);
} catch (err) {
  console.error('Failed to set CORS configuration:', err);
}
```

### Setting object tagging
{: #node-v2-set-tagging}

```javascript
const { PutObjectTaggingCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'my-object.txt';

// Set object tags
const command = new PutObjectTaggingCommand({
  Bucket: bucketName,
  Key: objectKey,
  Tagging: {
    TagSet: [
      {
        Key: 'Department',
        Value: 'Finance'
      },
      {
        Key: 'Project',
        Value: 'Q4-2024'
      },
      {
        Key: 'Classification',
        Value: 'Confidential'
      }
    ]
  }
});

try {
  await client.send(command);
  console.log(`Tags set for object '${objectKey}'`);
} catch (err) {
  console.error('Failed to set object tags:', err);
}

```



### Getting object tags
{: #node-v2-get-object-tag}

```javascript
const { GetObjectTaggingCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'my-object.txt';

const command = new GetObjectTaggingCommand({
  Bucket: bucketName,
  Key: objectKey
});

try {
  const response = await client.send(command);

  console.log(`Tags for object '${objectKey}':`);
  if (response.TagSet && response.TagSet.length > 0) {
    response.TagSet.forEach(tag => {
      console.log(` ${tag.Key}: ${tag.Value}`);
    });
  } else {
    console.log(' No tags found');
  }
} catch (err) {
  console.error('Failed to get object tags:', err);
}
```

### Restoring an archived object
{: #node-v2-restore-object}

Objects in archive storage classes must be restored before they can be accessed:

```javascript
const { RestoreObjectCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-bucket';
const objectKey = 'archived-object.txt';

// Restore object with accelerated retrieval (2 hours)
const command = new RestoreObjectCommand({
  Bucket: bucketName,
  Key: objectKey,
  RestoreRequest: {
    Days: 7, // Number of days to keep restored copy
    GlacierJobParameters: {
      Tier: 'Accelerated' // Accelerated (2 hours) or Standard (12 hours)
    }
  }
});

try {
  await client.send(command);
  console.log(`Restore request submitted for object '${objectKey}'`);
  console.log('The object will be available for download in approximately 2 hours');
} catch (err) {
  console.error('Failed to restore object:', err);
}
```

**Note**: {{site.data.keyword.cos_full_notm}} supports accelerated archive with restore times of 2 hours or 12 hours, depending on the tier selected.

### Setting object retention
{: #node-v2-set-object-retention}

```javascript
const { PutObjectRetentionCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-protected-bucket';
const objectKey = 'important-document.pdf';

// Set retention until a specific date
const retainUntilDate = new Date();
retainUntilDate.setMonth(retainUntilDate.getMonth() + 6); // 6 months from now

const command = new PutObjectRetentionCommand({
  Bucket: bucketName,
  Key: objectKey,
  Retention: {
    Mode: 'COMPLIANCE',
    RetainUntilDate: retainUntilDate
  }
});

try {
  await client.send(command);
  console.log(`Retention set for object '${objectKey}' until ${retainUntilDate}`);
} catch (err) {
  console.error('Failed to set object retention:', err);
}
```

### Getting object retention
{: #node-v2-get-object-retention}

```javascript
const { GetObjectRetentionCommand } = require('ibm-cos-sdk-v2');

const bucketName = 'my-protected-bucket';
const objectKey = 'important-document.pdf';

const command = new GetObjectRetentionCommand({
  Bucket: bucketName,
  Key: objectKey
});

try {
  const response = await client.send(command);

  console.log(`Retention for object '${objectKey}':`);
  console.log(` Mode: ${response.Retention.Mode}`);
  console.log(` Retain Until: ${response.Retention.RetainUntilDate}`);
} catch (err) {
  console.error('Failed to get object retention:', err);
}
```

### Checking if a bucket exists (HEAD)
{: #node-v2-head-bucket}

```javascript
const { HeadBucketCommand } = require('ibm-cos-sdk-v2');

const command = new HeadBucketCommand({
  Bucket: 'my-bucket'
});

try {
  await client.send(command);
  console.log('Bucket exists');
} catch (err) {
  if (err.name === 'NotFound') {
    console.log('Bucket does not exist');
  } else {
    console.error('Error checking bucket:', err);
  }
}
```

### Streaming upload from a file
{: #node-v2-stream-upload}

```javascript
const fs = require('fs');
const { PutObjectCommand } = require('ibm-cos-sdk-v2');

const fileStream = fs.createReadStream('large-file.bin');

const command = new PutObjectCommand({
  Bucket: 'my-bucket',
  Key: 'large-file.bin',
  Body: fileStream
});

try {
  const response = await client.send(command);
  console.log('File uploaded successfully');
} catch (err) {
  console.error('Error uploading file:', err);
}
```

### Streaming download to a file
{: #node-v2-stream-download}

```javascript
const fs = require('fs');
const { GetObjectCommand } = require('ibm-cos-sdk-v2');

const command = new GetObjectCommand({
  Bucket: 'my-bucket',
  Key: 'large-file.bin'
});

try {
  const response = await client.send(command);
  const fileStream = fs.createWriteStream('downloaded-file.bin');

  response.Body.pipe(fileStream);

  await new Promise((resolve, reject) => {
    fileStream.on('finish', resolve);
    fileStream.on('error', reject);
    response.Body.on('error', reject);
  });

  console.log('File downloaded successfully');
} catch (err) {
  console.error('Error downloading file:', err);
}
```

### Listing all objects with pagination
{: #node-v2-paginate-objects}

The v2 SDK provides built-in paginators for operations that return truncated results.

**Using a paginator (recommended):**

```javascript
const { paginateListObjectsV2 } = require('ibm-cos-sdk-v2');

async function listAllObjects(bucket, prefix) {
  const allObjects = [];

  const paginator = paginateListObjectsV2(
    { client: client, pageSize: 1000 },
    { Bucket: bucket, Prefix: prefix }
  );

  try {
    for await (const page of paginator) {
      if (page.Contents) {
        allObjects.push(...page.Contents);
      }
    }

    console.log('Total objects:', allObjects.length);
    return allObjects;
  } catch (err) {
    console.error('Error listing objects:', err);
    throw err;
  }
}
```

**Manual pagination:**

```javascript
const { ListObjectsV2Command } = require('ibm-cos-sdk-v2');

async function listAllObjects(bucket, prefix) {
  const allObjects = [];
  let continuationToken = undefined;

  do {
    const command = new ListObjectsV2Command({
      Bucket: bucket,
      Prefix: prefix,
      ContinuationToken: continuationToken
    });

    const response = await client.send(command);

    if (response.Contents) {
      allObjects.push(...response.Contents);
    }

    continuationToken = response.NextContinuationToken;
  } while (continuationToken);

  console.log('Total objects:', allObjects.length);
  return allObjects;
}
```

### Generating presigned URLs
{: #node-v2-presigned-urls}

Presigned URLs allow temporary, unauthenticated access to objects. Install the presigner package first:

```sh
npm install @ibm-cos/s3-request-presigner
```

**Presigned GET URL (download):**

```javascript
const { GetObjectCommand } = require('ibm-cos-sdk-v2');
const { getSignedUrl } = require('@ibm-cos/s3-request-presigner');

const command = new GetObjectCommand({
  Bucket: 'my-bucket',
  Key: 'my-object.txt'
});

try {
  const url = await getSignedUrl(client, command, { expiresIn: 3600 });
  console.log('Presigned URL:', url);
} catch (err) {
  console.error('Error generating presigned URL:', err);
}
```

**Presigned PUT URL (upload):**

```javascript
const { PutObjectCommand } = require('ibm-cos-sdk-v2');
const { getSignedUrl } = require('@ibm-cos/s3-request-presigner');

const command = new PutObjectCommand({
  Bucket: 'my-bucket',
  Key: 'upload-object.txt',
  ContentType: 'text/plain'
});

try {
  const url = await getSignedUrl(client, command, { expiresIn: 3600 });
  console.log('Presigned PUT URL:', url);
} catch (err) {
  console.error('Error generating presigned URL:', err);
}
```

### Getting CORS configuration
{: #node-v2-get-cors}

```javascript
const { GetBucketCorsCommand } = require('ibm-cos-sdk-v2');

const command = new GetBucketCorsCommand({
  Bucket: 'my-bucket'
});

try {
  const response = await client.send(command);
  console.log('CORS rules:', response.CORSRules);
} catch (err) {
  console.error('Error getting CORS:', err);
}
```

### Deleting object tags
{: #node-v2-delete-object-tags}

```javascript
const { DeleteObjectTaggingCommand } = require('ibm-cos-sdk-v2');

const command = new DeleteObjectTaggingCommand({
  Bucket: 'my-bucket',
  Key: 'my-object.txt'
});

try {
  await client.send(command);
  console.log('Object tagging deleted successfully');
} catch (err) {
  console.error('Error deleting object tags:', err);
}
```

### Uploading an object with custom metadata
{: #node-v2-upload-object-metadata}

```javascript
const { PutObjectCommand } = require('ibm-cos-sdk-v2');

const command = new PutObjectCommand({
  Bucket: 'my-bucket',
  Key: 'my-object.txt',
  Body: 'content',
  Metadata: {
    'author': 'John Doe',
    'department': 'Engineering'
  }
});

try {
  await client.send(command);
  console.log('Object uploaded with metadata');
} catch (err) {
  console.error('Error uploading object:', err);
}
```

### Setting bucket tagging
{: #node-v2-set-bucket-tagging}

```javascript
const { PutBucketTaggingCommand } = require('ibm-cos-sdk-v2');

const command = new PutBucketTaggingCommand({
  Bucket: 'my-bucket',
  Tagging: {
    TagSet: [
      { Key: 'Environment', Value: 'Production' },
      { Key: 'Project', Value: 'WebApp' }
    ]
  }
});

try {
  await client.send(command);
  console.log('Bucket tags updated');
} catch (err) {
  console.error('Error updating bucket tags:', err);
}
```

### Getting versioning status
{: #node-v2-get-versioning}

```javascript
const { GetBucketVersioningCommand } = require('ibm-cos-sdk-v2');

const command = new GetBucketVersioningCommand({ Bucket: 'my-bucket' });

try {
  const response = await client.send(command);
  console.log('Bucket versioning configuration retrieved successfully');
  console.log('Status:', response.Status);
} catch (err) {
  console.error('Error:', err.message);
}
```

### Getting a specific object version
{: #node-v2-get-object-version}

```javascript
const { GetObjectCommand } = require('ibm-cos-sdk-v2');

const command = new GetObjectCommand({
  Bucket: 'my-bucket',
  Key: 'my-file.txt',
  VersionId: '<VERSION_ID>'
});

try {
  const response = await client.send(command);
  console.log('Version ID:', response.VersionId);
  // Stream response.Body to a file or buffer as needed.
} catch (err) {
  console.error('Error:', err.message);
}
```

### Setting bucket protection (WORM) (IBM Extension)
{: #node-v2-bucket-protection}

{{site.data.keyword.cos_full_notm}} supports Write-Once-Read-Many (WORM) bucket protection for compliance and data retention.

**Setting bucket protection:**

```javascript
const { PutBucketProtectionConfigurationCommand } = require('ibm-cos-sdk-v2');

const command = new PutBucketProtectionConfigurationCommand({
  Bucket: 'my-protected-bucket',
  ProtectionConfiguration: {
    Status: 'Retention',
    MinimumRetention: { Days: 1 },
    DefaultRetention: { Days: 30 },
    MaximumRetention: { Days: 365 }
  }
});

try {
  await client.send(command);
  console.log('Bucket protection configuration set successfully');
} catch (err) {
  console.error('Error configuring protection:', err);
}
```

**Getting bucket protection:**

```javascript
const { GetBucketProtectionConfigurationCommand } = require('ibm-cos-sdk-v2');

const command = new GetBucketProtectionConfigurationCommand({
  Bucket: 'my-protected-bucket'
});

try {
  const response = await client.send(command);
  console.log('Bucket protection configuration retrieved successfully');
  console.log('Response:', JSON.stringify(response, null, 2));
} catch (err) {
  console.error('Error getting protection config:', err);
}
```

### Managing legal holds (IBM Extension)
{: #node-v2-legal-hold}

Legal holds prevent object deletion regardless of retention period expiry. Each hold is identified by a unique ID and all holds must be explicitly removed before an object can be deleted.

**Note**: Legal holds require the bucket to have an IBM COS protection configuration set.
{: note}

**Adding a legal hold:**

```javascript
const { AddLegalHoldCommand } = require('ibm-cos-sdk-v2');

const command = new AddLegalHoldCommand({
  Bucket: 'my-protected-bucket',
  Key: 'important-document.pdf',
  RetentionLegalHoldId: 'legal-case-12345'
});

try {
  await client.send(command);
  console.log('Legal hold added');
} catch (err) {
  console.error('Error adding legal hold:', err);
}
```

**Listing legal holds:**

```javascript
const { ListLegalHoldsCommand } = require('ibm-cos-sdk-v2');

const command = new ListLegalHoldsCommand({
  Bucket: 'my-protected-bucket',
  Key: 'important-document.pdf'
});

try {
  const response = await client.send(command);
  console.log('Legal holds listed successfully');
  console.log('Legal holds:', JSON.stringify(response.LegalHolds, null, 2));
} catch (err) {
  console.error('Error listing legal holds:', err);
}
```

**Deleting a legal hold:**

```javascript
const { DeleteLegalHoldCommand } = require('ibm-cos-sdk-v2');

const command = new DeleteLegalHoldCommand({
  Bucket: 'my-protected-bucket',
  Key: 'important-document.pdf',
  RetentionLegalHoldId: 'legal-case-12345'
});

try {
  await client.send(command);
  console.log('Legal hold removed');
} catch (err) {
  console.error('Error removing legal hold:', err);
}
```

### Creating a Key Protect encrypted bucket (IBM Extension)
{: #node-v2-key-protect}

IBM Key Protect provides encryption key management for bucket-level encryption:

```javascript
const { CreateBucketCommand } = require('ibm-cos-sdk-v2');

const command = new CreateBucketCommand({
  Bucket: 'my-encrypted-bucket',
  CreateBucketConfiguration: {
    LocationConstraint: 'us-south-standard'
  },
  IBMSSEKPEncryptionAlgorithm: 'AES256',
  IBMSSEKPCustomerRootKeyCrn: 'crn:v1:bluemix:public:kms:us-south:...'
});

try {
  await client.send(command);
  console.log('Encrypted bucket created');
} catch (err) {
  console.error('Error creating encrypted bucket:', err);
}
```

### Listing directory buckets (IBM Extension)
{: #node-v2-list-directory-buckets}

Directory buckets are a distinct {{site.data.keyword.cos_full_notm}} bucket type optimized for high-throughput workloads:

```javascript
const { ListDirectoryBucketsCommand } = require('ibm-cos-sdk-v2');

const command = new ListDirectoryBucketsCommand({
  // Optional: MaxDirectoryBuckets, ContinuationToken for pagination
});

try {
  const response = await client.send(command);
  console.log('Directory buckets listed successfully');
  if (response.Buckets?.length) {
    response.Buckets.forEach(b => console.log(' -', b.Name));
  } else {
    console.log('No directory buckets found');
  }
} catch (err) {
  console.error('Error:', err.message);
}
```

### Renaming an object (IBM Extension)
{: #node-v2-rename-object}

`RenameObject` is an IBM COS-specific atomic server-side operation. No data is transferred — only the key changes:

```javascript
const { RenameObjectCommand } = require('ibm-cos-sdk-v2');

const command = new RenameObjectCommand({
  Bucket: 'my-bucket',
  Key: 'new-object-key',                    // destination key
  RenameSource: 'my-bucket/old-object-key'  // source: bucket/key
});

try {
  await client.send(command);
  console.log('Object renamed successfully');
} catch (err) {
  console.error('Error:', err.message);
}
```

### Updating object encryption (IBM Extension)
{: #node-v2-update-object-encryption}

`UpdateObjectEncryption` updates the encryption key reference on an existing object. Requires the bucket to have IBM Key Protect or HPCS configured:

```javascript
const { UpdateObjectEncryptionCommand } = require('ibm-cos-sdk-v2');

const command = new UpdateObjectEncryptionCommand({
  Bucket: 'my-bucket',
  Key: 'my-object.txt'
});

try {
  await client.send(command);
  console.log('Object encryption updated successfully');
} catch (err) {
  console.error('Error:', err.message);
}
```

### Managing bucket replication (IBM Extension)
{: #node-v2-bucket-replication}

**Getting replication configuration:**

```javascript
const { GetBucketReplicationCommand } = require('ibm-cos-sdk-v2');

const command = new GetBucketReplicationCommand({ Bucket: 'my-bucket' });

try {
  const response = await client.send(command);
  console.log('Bucket replication configuration retrieved successfully');
  console.log('Rules:', JSON.stringify(response.ReplicationConfiguration?.Rules, null, 2));
} catch (err) {
  console.error('Error:', err.message);
}
```

**Deleting replication configuration:**

```javascript
const { DeleteBucketReplicationCommand } = require('ibm-cos-sdk-v2');

const command = new DeleteBucketReplicationCommand({ Bucket: 'my-bucket' });

try {
  await client.send(command);
  console.log('Bucket replication configuration deleted successfully');
} catch (err) {
  console.error('Error:', err.message);
}
```

**Listing replication failures:**

```javascript
const { ListBucketReplicationFailuresCommand } = require('ibm-cos-sdk-v2');

const command = new ListBucketReplicationFailuresCommand({ Bucket: 'my-bucket' });

try {
  const response = await client.send(command);
  console.log('Bucket replication failures listed successfully');
  console.log('Response:', JSON.stringify(response, null, 2));
} catch (err) {
  console.error('Error:', err.message);
}
```

**Reattempting failed replications:**

```javascript
const { PutBucketReplicationReattemptCommand } = require('ibm-cos-sdk-v2');

const command = new PutBucketReplicationReattemptCommand({ Bucket: 'my-bucket' });

try {
  await client.send(command);
  console.log('Bucket replication reattempt triggered successfully');
} catch (err) {
  console.error('Error:', err.message);
}
```

### Creating a session (IBM Extension)
{: #node-v2-create-session}

`CreateSession` creates a temporary session token for an {{site.data.keyword.cos_full_notm}} bucket:

```javascript
const { CreateSessionCommand } = require('ibm-cos-sdk-v2');

const command = new CreateSessionCommand({ Bucket: 'my-bucket' });

try {
  const response = await client.send(command);
  console.log('Session created successfully');
  console.log('Response:', JSON.stringify(response, null, 2));
} catch (err) {
  console.error('Error:', err.message);
}
```

### Waiting for a resource to be ready
{: #node-v2-waiters}

Waiters poll a resource until it reaches a desired state, removing the need for manual polling loops.

**Waiting for a bucket to exist:**

```javascript
const { waitUntilBucketExists } = require('ibm-cos-sdk-v2');

try {
  await waitUntilBucketExists(
    {
      client: client,
      maxWaitTime: 120, // Maximum wait time in seconds
      minDelay: 2,      // Minimum delay between checks in seconds
      maxDelay: 10      // Maximum delay between checks in seconds
    },
    { Bucket: 'my-bucket' }
  );
  console.log('Bucket exists and is ready');
} catch (err) {
  console.error('Bucket did not become available:', err);
}
```

**Waiting for an object to exist:**

```javascript
const { waitUntilObjectExists } = require('ibm-cos-sdk-v2');

try {
  await waitUntilObjectExists(
    {
      client: client,
      maxWaitTime: 60,
      minDelay: 1,
      maxDelay: 5
    },
    {
      Bucket: 'my-bucket',
      Key: 'my-object.txt'
    }
  );
  console.log('Object exists and is ready');
} catch (err) {
  console.error('Object did not become available:', err);
}
```

## Next Steps
{: #node-v2-next-steps}

* Review the [Node.js API reference documentation](https://ibm.github.io/ibm-cos-sdk-js-v2/) for detailed information on all available methods and types
* Explore the [GitHub repository](https://github.com/IBM/ibm-cos-sdk-js-v2) for additional examples and source code
* Read the [Migration Guide](https://github.com/IBM/ibm-cos-sdk-js-v2/blob/main/MIGRATION_GUIDE_V2.md) if you're upgrading from v1
* Check out the [IBM Cloud Object Storage documentation](https://cloud.ibm.com/docs/cloud-object-storage) for service-specific features and best practices
* For help and support:
  * Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/object-storage+ibm) with tags `ibm` and `object-storage`
  * Open an issue on [GitHub](https://github.com/IBM/ibm-cos-sdk-js-v2/issues)
  * Contact [IBM Cloud Support](https://cloud.ibm.com/unifiedsupport/supportcenter/)
