---

copyright:
  years: 2021, 2023
lastupdated: "2023-08-08"

keywords: lifecycle, multipart, cleanup

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Cleaning up incomplete multipart uploads
{: #lifecycle-cleanup-mpu}

This lifecycle rule stops any multipart uploads if the uploads are not completed within a defined number of days after initiation.
{: shortdesc}

You can set lifecycle rules for objects by using the web console, REST API, and third-party tools that are integrated with {{site.data.keyword.cos_full_notm}}.

* A new rule can be added to a new or existing bucket at any time.
* An existing rule can be modified or disabled.

These incomplete uploads do not appear in the console, but the uploaded parts continue to accrue usage and billing charges. Setting up lifecycle rules to automatically delete incomplete uploads is the user's responsibility.

## Attributes of expiration rules
{: #expiry-rules-attributes}

Each expiration rule has the following attributes:

### ID
{: #lifecycle-cleanup-mpu-id}

A rule's ID must be unique within the bucket's lifecycle configuration.

### AbortIncompleteMultipartUpload
{: #lifecycle-cleanup-mpu-incomplete}

The `AbortIncompleteMultipartUpload` block contains the details that govern the automatic cancellation of uploads. The block contains a single field: `DaysAfterInitiation`.

### Prefix
{: #lifecycle-cleanup-mpu-prefix}

An optional string that will be matched to the prefix of the object name in the bucket. A rule with a prefix will only apply to the objects that match. You can use multiple rules for different actions for different prefixes within the same bucket.

### Status
{: #lifecycle-cleanup-mpu-status}

A rule can either be enabled or disabled. A rule is active only when enabled.

## Sample lifecycle configurations
{: #lifecycle-cleanup-mpu-configs}

This configuration expires any uploads that haven't completed after 3 days.

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>delete-after-3-days</ID>
        <Filter />
        <Status>Enabled</Status>
        <AbortIncompleteMultipartUpload>
            <DaysAfterInitiation>3</DaysAfterInitiation>
        </AbortIncompleteMultipartUpload>
    </Rule>
</LifecycleConfiguration>
```

You can also combine rules.  This configuration cancels inactive uploads after 5 days, archives any objects 90 days after creation, and deletes any objects with the prefix `foo/` after 180 days .

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>archive-first</ID>
        <Filter />
        <Status>Enabled</Status>
    <Transition>
        <Days>90</Days>
        <StorageClass>GLACIER</StorageClass>
    </Transition>
    </Rule>
    <Rule>
        <ID>then-delete</ID>
    <Filter>
        <Prefix>foo/</Prefix>
    </Filter>
        <Status>Enabled</Status>
        <Expiration>
            <Days>180</Days>
        </Expiration>
    </Rule>
    <Rule>
        <ID>delete-after-3-days</ID>
        <Status>Enabled</Status>
		<AbortIncompleteMultipartUpload>
		    <DaysAfterInitiation>3</DaysAfterInitiation>
        </AbortIncompleteMultipartUpload>
    </Rule>
</LifecycleConfiguration>
```


## Using the API and SDKs
{: #mpu-cleanup-using-api-sdks}

You can programmatically manage lifecycle rules by using the REST API or the IBM COS SDKs.
{: #lifecycle-mpu-api-put}

**REST API reference**

This implementation of the `PUT` operation uses the `lifecycle` query parameter to set lifecycle settings for the bucket. This operation allows for a single lifecycle policy definition for a bucket. The policy is defined as a set of rules consisting of the following parameters: `ID`, `Status`, `Filter`, and `Expiration`.

Cloud IAM users must have the `Writer` role to add a lifecycle policy from a bucket.

Classic Infrastructure Users must have `Owner` permissions on the bucket to add a lifecycle policy from a bucket.

| Header        | Type   | Description                                                                                                                                                 |
| ------------- | ------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Content-MD5` | String | **Required**: The base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit. |
{: caption="Header" caption-side="top"}

The body of the request must contain an XML block with the following schema:

| Element                          | Type                 | Children                                                   | Ancestor                         | Constraint                                                                                 |
| -------------------------------- | -------------------- | ---------------------------------------------------------- | -------------------------------- | ------------------------------------------------------------------------------------------ |
| `LifecycleConfiguration`         | Container            | `Rule`                                                     | None                             | Limit 1.                                                                                   |
| `Rule`                           | Container            | `ID`, `Status`, `Filter`, `AbortIncompleteMultipartUpload` | `LifecycleConfiguration`         | Limit 1000.                                                                                |
| `ID`                             | String               | None                                                       | `Rule`                           | Must consist of (`a-z`,`A-Z0-9`) and the following symbols: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                         | String               | `Prefix`                                                   | `Rule`                           | Must contain a `Prefix` element or be self-closed (`<Filter />`).                                                           |
| `Prefix`                         | String               | None                                                       | `Filter`                         | The rule applies to any objects with keys that match this prefix.                          |
| `AbortIncompleteMultipartUpload` | `Container`          | `DaysAfterInitiation`                                      | `Rule`                           | Limit 1.                                                                                   |
| `DaysAfterInitiation`            | Non-negative integer | None                                                       | `AbortIncompleteMultipartUpload` | Must be a value greater than 0.                                                            |
{: caption="Body of the request schema" caption-side="top"}

The body of the request must contain an XML block with the schema that is addressed in the table (see Example 1).

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-3-days</ID>
        <Filter />
		<Status>Enabled</Status>
		<AbortIncompleteMultipartUpload>
			<DaysAfterInitiation>3</DaysAfterInitiation>
		</AbortIncompleteMultipartUpload>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Example 1. XML sample from the body of the request." caption-side="bottom"}

**Syntax**

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Example 2. Note the use of slashes and dots in this example of syntax." caption-side="bottom"}
{: codeblock}

**Example request**

```yaml
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305

<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-3-days</ID>
        <Filter />
		<Status>Enabled</Status>
		<AbortIncompleteMultipartUpload>
			<DaysAfterInitiation>3</DaysAfterInitiation>
		</AbortIncompleteMultipartUpload>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Example 3. Request header samples for creating an object lifecycle configuration." caption-side="bottom"}

**Code sample for use with NodeJS COS SDK**

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration.

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* required */
        AbortIncompleteMultipartUpload: {
          DaysAfterInitiation: 'NUMBER_VALUE'
        }
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}

**Code sample for use with Python COS SDK**

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration.

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'Status': 'Enabled',
                'Filter': {},
                'AbortIncompleteMultipartUpload': {
                    'DaysAfterInitiation': <NUMBER_VALUE>
        }
            },
        ]
    }
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}

**Code sample for use with Java COS SDK**

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration.

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            String storageClass = "us-south";
            String location = "us";

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            // Define a rule for expiring items in a bucket
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);

                    rule.setFilter(new LifecycleFilter());

            // Add the rule to a new BucketLifecycleConfiguration.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));

            // Use the client to set the LifecycleConfiguration on the bucket.
            _cosClient.setBucketLifecycleConfiguration(bucketName, configuration);
        }

        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
    }
```
{: codeblock}
{: caption="Example 1. Code samples showing creation of lifecycle configuration." caption-side="bottom"}

### Examine a bucket’s lifecycle configuration, including expiration
{: #lifecycle-mpu-api-get}

This implementation of the `GET` operation uses the `lifecycle` query parameter to examine lifecycle settings for the bucket. An HTTP `404` response will be returned if no lifecycle configuration is present.

Cloud IAM users must have the `Reader` role to examine a lifecycle policy from a bucket.

Classic Infrastructure Users must have `Read` permissions on the bucket to examine a lifecycle policy from a bucket.

**Syntax**

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Example 5. Note the use of slashes and dots in this example of syntax." caption-side="bottom"}
{: codeblock}

**Example Header Request**

```yaml
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="Example 6. Request header samples for creating an object lifecycle configuration." caption-side="bottom"}

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration.

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').get_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration.

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";

            String storageClass = "us-south";
            String location = "us";

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            // Use the client to read the configuration
            BucketLifecycleConfiguration config = _cosClient.getBucketLifecycleConfiguration(bucketName);

            System.out.println(config.toString());
        }

        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

    }
```
{: codeblock}
{: java}
{: caption="Example 2. Code samples showing inspection of lifecycle configuration." caption-side="bottom"}

### Delete a bucket’s lifecycle configuration, including expiration
{: #lifecycle-mpu-api-delete}

This implementation of the `DELETE` operation uses the `lifecycle` query parameter to examine lifecycle settings for the bucket. All lifecycle rules associated with the bucket will be deleted.  Transitions defined by the rules will no longer take place for new objects.  However, existing transition rules will be maintained for objects that were already written to the bucket before the rules were deleted.  Expiration Rules will no longer exist. An HTTP `404` response will be returned if no lifecycle configuration is present.

Cloud IAM users must have the `Writer` role to remove a lifecycle policy from a bucket.

Classic Infrastructure Users must have `Owner` permissions on the bucket to remove a lifecycle policy from a bucket.

**Syntax**

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Example 7. Note the use of slashes and dots in this example of syntax." caption-side="bottom"}
{: codeblock}

**Example Header Request**

```yaml
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="Example 8. Request header samples for creating an object lifecycle configuration." caption-side="bottom"}

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration.

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration. 

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').delete_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}

Using the {{site.data.keyword.cos_full}} SDKs only requires calling the appropriate functions with the correct parameters and proper configuration.

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";

            String storageClass = "us-south";
            String location = "us";

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            // Delete the configuration.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);

            // Verify that the configuration has been deleted by attempting to retrieve it.
            config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            String s = (config == null) ? "Configuration has been deleted." : "Configuration still exists.";
            System.out.println(s);
        }

        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

    }

```
{: codeblock}
{: caption="Example 3. Code samples showing deletion of lifecycle configuration." caption-side="bottom"}

## Next Steps
{: #lifecycle-mpu-next-steps}

Expiration is just one of many lifecycle concepts available for {{site.data.keyword.cos_full_notm}}.
Each of the concepts we've covered in this overview can be explored further at the
[{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/).
