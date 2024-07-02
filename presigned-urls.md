---

copyright:
  years: 2017, 2024
lastupdated: "2024-07-02"

keywords: authorization, aws, hmac, signature, presign, python, java

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Creating a pre-signed URL
{: #presign-url}

Pre-signed URLs in {{site.data.keyword.cos_full}} create temporary links that can be used to share an object without requiring additional user credentials when accessed.
{: shortdesc}

Of course, one can also [provide a temporary target for sending a PUT request](https://medium.com/codait/keeping-your-secrets-between-cloud-object-storage-and-your-browser-part-1-68f4b83bbd38) also without needing to provide any more information for authentication. The easiest way to create pre-signed URLs is using the [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli). But first, you may need to run `aws configure` in order to set your Access Key ID and Secret Access Key from your own [HMAC-enabled service credential](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main). When you have completed configuring your CLI, use the following example as a template and replace the endpoint and name of your bucket with the appropriate information:

```sh
$ aws --endpoint-url=https://{endpoint} s3 presign s3://{bucket-name}/{new-file-key}
```

If the service credential used to generate the HMAC credentials (used as the Access Key ID and Secret Access Key configuration above) is deleted, the access for the pre-signed URL will fail.
{: note}

It is also possible to set an expiration time for the URL in seconds (default is 3600):

```sh
$ aws --endpoint-url=https://{endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

It is also possible to construct them programmatically. Here are examples for basic `GET` operations written in Python. For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Unlike AWS S3, {{site.data.keyword.cos_full_notm}} does not enforce a maximum expiration time of 7 days (604800 seconds). While it is possible to create a pre-signed URL with a long expiration value, most use cases that require extended public access would be better served by [implementing a public access policy](/docs/cloud-object-storage?topic=cloud-object-storage-iam-public-access) on a bucket instead.
{: tip}

## Create a pre-signed URL to download an object
{: #presign-url-get}

### Python Example
{: #presign-url-get-python}

```python
import ibm_boto3
import os

bucket_name = '<bucekt name>'
key_name = '<object key name>'
http_method = 'get_object'
expiration = 600  # time in seconds, default:600

access_key = os.environ.get('COS_HMAC_ACCESS_KEY_ID')
secret_key = os.environ.get('COS_HMAC_SECRET_ACCESS_KEY')
# Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
cos_service_endpoint = 'https://s3.<region>.cloud-object-storage.appdomain.cloud'

cos = ibm_boto3.client("s3",
                       aws_access_key_id=access_key,
                       aws_secret_access_key=secret_key,
                       endpoint_url=cos_service_endpoint
                       )

signedUrl = cos.generate_presigned_url(http_method, Params={
                                       'Bucket': bucket_name, 'Key': key_name}, ExpiresIn=expiration)
print("presigned download URL =>" + signedUrl)
```
{: codeblock}

### Java Example
{: #presign-url-get-java}

```java
import java.util.Date;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.HttpMethod;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.GeneratePresignedUrlRequest;


String bucketName = "<bucket name>";
String keyName = "<object key name>";
HttpMethod httpMethod = HttpMethod.GET;
Date expiration = new Date();
long expTimeMillis = expiration.getTime();
expTimeMillis += 1000 * 60 * 60;
expiration.setTime(expTimeMillis);

String accessKey = "<COS_HMAC_ACCESS_KEY_ID>";
String secretAccessKey = "<COS_HMAC_SECRET_ACCESS_KEY>";
// Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
String cosServiceEndpoint = "https://s3.<region>.cloud-object-storage.appdomain.cloud";

AmazonS3 cosClient = AmazonS3ClientBuilder.standard()
                     .withEndpointConfiguration(new EndpointConfiguration(cosServiceEndpoint, "us-east-1"))
                     .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretAccessKey))).withPathStyleAccessEnabled(true)
                     .build();

GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, keyName)
                                                          .withMethod(httpMethod)
                                                          .withExpiration(expiration);

URL signedUrl = cosClient.generatePresignedUrl(generatePresignedUrlRequest);
System.out.println(signedUrl);
```

## Create a pre-signed URL to upload an object
{: #presign-url-put-python}

### Python Example
{: #presign-url-put-python}

```python
import ibm_boto3
import os

bucket_name = '<bucket name>'
key_name = '<object key name>'
http_method = 'put_object'
expiration = 600  # time in seconds, default:600

access_key = os.environ.get('COS_HMAC_ACCESS_KEY_ID')
secret_key = os.environ.get('COS_HMAC_SECRET_ACCESS_KEY')
# Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
cos_service_endpoint = 'https://s3.<region>.cloud-object-storage.appdomain.cloud'

cos = ibm_boto3.client("s3",
                       aws_access_key_id=access_key,
                       aws_secret_access_key=secret_key,
                       endpoint_url=cos_service_endpoint
                       )

signedUrl = cos.generate_presigned_url(http_method, Params={
                                       'Bucket': bucket_name, 'Key': key_name}, ExpiresIn=expiration)
print("presigned upload URL =>" + signedUrl)
```
{: codeblock}
