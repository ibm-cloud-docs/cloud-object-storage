---

copyright:
  years: 2017, 2026
lastupdated: "2026-06-24"

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
