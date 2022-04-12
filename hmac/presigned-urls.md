---

copyright:
  years: 2017, 2022
lastupdated: "2022-12-04"

keywords: authorization, aws, hmac, signature, presign

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Creating a presigned URL
{: #presign-url}

Presigned URLs in {{site.data.keyword.cos_full}} create temporary links that can be used to share an object without requiring additional user credentials when accessed.
{: shortdesc}

Of course, one can also [provide a temporary target for sending a PUT request](https://medium.com/codait/keeping-your-secrets-between-cloud-object-storage-and-your-browser-part-1-68f4b83bbd38) also without needing to provide any more information for authentication. The easiest way to create presigned URLs is using the [AWS CLI](/docs/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli). But first, you may need to run `aws configure` in order to set your Access Key ID and Secret Access Key from your own [HMAC-enabled service credential](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main). When you have completed configuring your CLI, use the following example as a template and replace the endpoint and name of your bucket with the appropriate information:

```bash
$ aws --endpoint-url=https://{endpoint} s3 presign s3://{bucket-name}/{new-file-key}
```

If the service credential used to generate the HMAC credentials (used as the Access Key ID and Secret Access Key configuration above) is deleted, the access for the pre-signed URL will fail.
{: note}

It is also possible to set an expiration time for the URL in seconds (default is 3600):

```bash
$ aws --endpoint-url=https://{endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

It is also possible to construct them programmatically. Here are examples for basic `GET` operations written in Python. For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Unlike AWS S3, {{site.data.keyword.cos_full_notm}} does not enforce a maximum expiration time of 7 days (604800 seconds). While it is possible to create a presigned URL with a long expiration value, most use cases that require extended public access would be better served by [implementing a public access policy](/docs/cloud-object-storage/info?topic=cloud-object-storage-iam-public-access) on a bucket instead.
{: tip}

## Create a presigned URL to download an object
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

### Java Example
{: #presign-url-get-java}

```java
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CoSHMAC {
    // constants
    private static final String accessKey = "{COS_HMAC_ACCESS_KEY_ID}";
    private static final String secretKey = "{COS_HMAC_SECRET_ACCESS_KEY}";
    private static final String httpMethod = "GET";
    private static final String host = "{endpoint}";
    private static final String region = "";
    private static final String endpoint = "https://" + host;
    private static final String bucket = "example-bucket";
    private static final String objectKey = "example-object";
    private static final String requestParameters = "";
    private static final int expiration = 86400;  // time in seconds
    
    public static void main(String[] args) {
        try {
            // assemble the standardized request
            ZonedDateTime time = ZonedDateTime.now(ZoneOffset.UTC);
            String datestamp = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String timestamp = datestamp + "T" + time.format(DateTimeFormatter.ofPattern("HHmmss")) + "Z";

            String standardizedQuerystring = "X-Amz-Algorithm=AWS4-HMAC-SHA256" +
                "&X-Amz-Credential=" + URLEncoder.encode(accessKey + "/" + datestamp + "/" + region + "/s3/aws4_request", StandardCharsets.UTF_8.toString()) +
                "&X-Amz-Date=" + timestamp +
                "&X-Amz-Expires=" + String.valueOf(expiration) +
                "&X-Amz-SignedHeaders=host";

            String standardizedResource = "/" + bucket + "/" + objectKey;

            String payloadHash = "UNSIGNED-PAYLOAD";
            String standardizedHeaders = "host:" + host;
            String signedHeaders = "host";

            String standardizedRequest = httpMethod + "\n" +
                standardizedResource + "\n" +
                standardizedQuerystring + "\n" +
                standardizedHeaders + "\n" +
                "\n" +
                signedHeaders + "\n" +
                payloadHash;

            // assemble string-to-sign
            String hashingAlgorithm = "AWS4-HMAC-SHA256";
            String credentialScope = datestamp + "/" + region + "/" + "s3" + "/" + "aws4_request";
            String sts = hashingAlgorithm + "\n" +
                timestamp + "\n" +
                credentialScope + "\n" +
                hashHex(standardizedRequest);

            // generate the signature
            byte[] signatureKey = createSignatureKey(secretKey, datestamp, region, "s3");
            String signature = hmacHex(signatureKey, sts);

            // create and send the request
            String requestUrl = endpoint + "/" +
                bucket + "/" +
                objectKey + "?" +
                standardizedQuerystring +
                "&X-Amz-Signature=" +
                signature;

            URL urlObj = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod(httpMethod);

            System.out.printf("\nSending %s request to IBM COS -----------------------", httpMethod);
            System.out.println("Request URL = " + requestUrl);

            int responseCode = con.getResponseCode();
            System.out.println("\nResponse from IBM COS ----------------------------------");
            System.out.printf("Response code: %d\n\n", responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

            con.disconnect();

            // this information can be helpful in troubleshooting, or to better
            // understand what goes into signature creation
            System.out.println("These are the values used to construct this request.");
            System.out.println("Request details -----------------------------------------");
            System.out.printf("http_method: %s\n", httpMethod);
            System.out.printf("host: %s\n", host);
            System.out.printf("region: %s\n", region);
            System.out.printf("endpoint: %s\n", endpoint);
            System.out.printf("bucket: %s\n", bucket);
            System.out.printf("object_key: %s\n", objectKey);
            System.out.printf("timestamp: %s\n", timestamp);
            System.out.printf("datestamp: %s\n", datestamp);
            
            System.out.println("Standardized request details ----------------------------");
            System.out.printf("standardized_resource: %s\n", standardizedResource);
            System.out.printf("standardized_querystring: %s\n", standardizedQuerystring);
            System.out.printf("standardized_headers: %s\n", standardizedHeaders);
            System.out.printf("signed_headers: %s\n", signedHeaders);
            System.out.printf("payload_hash: %s\n", payloadHash);
            System.out.printf("standardized_request: %s\n", standardizedRequest);
            
            System.out.println("String-to-sign details ----------------------------------");
            System.out.printf("credential_scope: %s\n", credentialScope);
            System.out.printf("string-to-sign: %s\n", sts);
            System.out.printf("signature_key: %s\n", signatureKey);
            System.out.printf("signature: %s\n", signature);
            
            System.out.println("Because the signature key has non-ASCII characters, it is");
            System.out.println("necessary to create a hexadecimal digest for the purposes");
            System.out.println("of checking against this example.");

            String signatureKeyHex = createHexSignatureKey(secretKey, datestamp, region, "s3");
            System.out.printf("signature_key (hexidecimal): %s\n", signatureKeyHex);

            System.out.println("Header details ------------------------------------------");
            System.out.printf("pre-signed url: %s\n", requestUrl);
        }
        catch (Exception ex) {
            System.out.printf("Error: %s\n", ex.getMessage());
        }
    }

    private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		return formatter.toString();
	}

    public static byte[] hash(byte[] key, String msg) {
        byte[] returnVal = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            returnVal = mac.doFinal(msg.getBytes("UTF8"));
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            return returnVal;
        }
    }

	public static String hmacHex(byte[] key, String msg) {
        String returnVal = null;
        try {
            returnVal = toHexString(hash(key, msg));
        }
        catch (Exception ex) {

        }
        finally {
            return returnVal;
        }
    }
    
    public static String hashHex(String msg) {
        String returnVal = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(msg.getBytes(StandardCharsets.UTF_8));
            returnVal = toHexString(encodedhash);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            return returnVal;
        }
    }
    
    // region is a wildcard value that takes the place of the AWS region value
    // as COS doesn"t use the same conventions for regions, this parameter can accept any string
    public static byte[] createSignatureKey(String key, String datestamp, String region, String service) {
        byte[] returnVal = null;
        try {
            byte[] keyDate = hash(("AWS4" + key).getBytes("UTF8"), datestamp);
            byte[] keyString = hash(keyDate, region);
            byte[] keyService = hash(keyString, service);
            byte[] keySigning = hash(keyService, "aws4_request");
            returnVal = keySigning;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            return returnVal;
        }
    }

    public static String createHexSignatureKey(String key, String datestamp, String region, String service) {
        String returnVal = null;
        try {
            returnVal = toHexString(createSignatureKey(key, datestamp, region, service));
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            return returnVal;
        }
    }
}
```

## Create a presigned URL to upload an object
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

### NodeJS Example
{: #presign-url-put-node}

```javascript
const crypto = require('crypto');
const moment = require('moment');
const https = require('https');

const accessKey = "{COS_HMAC_ACCESS_KEY_ID}";
const secretKey = "{COS_HMAC_SECRET_ACCESS_KEY}";
const httpMethod = 'GET';
const host = '{endpoint}';
const region = '';
const endpoint = 'https://' + host;
const bucket = 'example-bucket';
const objectKey = 'example-object'
const expiration = 86400  // time in seconds

// hashing and signing methods
function hash(key, msg) {
    var hmac = crypto.createHmac('sha256', key);
    hmac.update(msg, 'utf8');
    return hmac.digest();
}

function hmacHex(key, msg) {
    var hmac = crypto.createHmac('sha256', key);
    hmac.update(msg, 'utf8');
    return hmac.digest('hex');
}

function hashHex(msg) {
    var hash = crypto.createHash('sha256');
    hash.update(msg);
    return hash.digest('hex');
}

// region is a wildcard value that takes the place of the AWS region value
// as COS doesn't use the same conventions for regions, this parameter can accept any string
function createSignatureKey(key, datestamp, region, service) {
    keyDate = hash(('AWS4' + key), datestamp);
    keyString = hash(keyDate, region);
    keyService = hash(keyString, service);
    keySigning = hash(keyService, 'aws4_request');
    return keySigning;
}

function createHexSignatureKey(key, datestamp, region, service) {
    keyDate = hashHex(('AWS4' + key), datestamp);
    keyString = hashHex(keyDate, region);
    keyService = hashHex(keyString, service);
    keySigning = hashHex(keyService, 'aws4_request');
    return keySigning;
}

function printDebug() {
    // this information can be helpful in troubleshooting, or to better
    // understand what goes into signature creation
    console.log('These are the values used to construct this request.');
    console.log('Request details -----------------------------------------');
    console.log(`httpMethod: ${httpMethod}`);
    console.log(`host: ${host}`);
    console.log(`region: ${region}`);
    console.log(`endpoint: ${endpoint}`);
    console.log(`bucket: ${bucket}`);
    console.log(`objectKey: ${objectKey}`);
    console.log(`timestamp: ${timestamp}`);
    console.log(`datestamp: ${datestamp}`);

    console.log('Standardized request details ----------------------------');
    console.log(`standardizedResource: ${standardizedResource}`);
    console.log(`standardizedQuerystring: ${standardizedQuerystring}`);
    console.log(`standardizedHeaders: ${standardizedHeaders}`);
    console.log(`signedHeaders: ${signedHeaders}`);
    console.log(`payloadHash: ${payloadHash}`);
    console.log(`standardizedRequest: ${standardizedRequest}`);

    console.log('String-to-sign details ----------------------------------');
    console.log(`credentialScope: ${credentialScope}`);
    console.log(`string-to-sign: ${sts}`);
    console.log(`signatureKey: ${signatureKey}`);
    console.log(`signature: ${signature}`);

    console.log('Because the signature key has non-ASCII characters, it is');
    console.log('necessary to create a hexadecimal digest for the purposes');
    console.log('of checking against this example.');

    signatureKeyHex = createHexSignatureKey(secretKey, datestamp, region, 's3')

    console.log(`signatureKey (hexidecimal): ${signatureKeyHex}`);

    console.log('Header details ------------------------------------------');
    console.log(`pre-signed url: ${requestUrl}`);    
}

// assemble the standardized request
var time = moment().utc();
var timestamp = time.format('YYYYMMDDTHHmmss') + 'Z';
var datestamp = time.format('YYYYMMDD');

var standardizedQuerystring = 'X-Amz-Algorithm=AWS4-HMAC-SHA256' +
    '&X-Amz-Credential=' + encodeURIComponent(accessKey + '/' + datestamp + '/' + region + '/s3/aws4_request') +
    '&X-Amz-Date=' + timestamp +
    '&X-Amz-Expires=' + expiration.toString() +
    '&X-Amz-SignedHeaders=host';

var standardizedResource = '/' + bucket + '/' + objectKey;

var payloadHash = 'UNSIGNED-PAYLOAD';
var standardizedHeaders = 'host:' + host;
var signedHeaders = 'host';

var standardizedRequest = httpMethod + '\n' +
    standardizedResource + '\n' +
    standardizedQuerystring + '\n' +
    standardizedHeaders + '\n' +
    '\n' +
    signedHeaders + '\n' +
    payloadHash;

// assemble string-to-sign
var hashingAlgorithm = 'AWS4-HMAC-SHA256';
var credentialScope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request';
var sts = hashingAlgorithm + '\n' +
    timestamp + '\n' +
    credentialScope + '\n' +
    hashHex(standardizedRequest);

// generate the signature
signatureKey = createSignatureKey(secretKey, datestamp, region, 's3');
signature = hmacHex(signatureKey, sts);

// create and send the request
// the 'requests' package autmatically adds the required 'host' header
var requestUrl = endpoint + '/' +
    bucket + '/' +
    objectKey + '?' +
    standardizedQuerystring +
    '&X-Amz-Signature=' +
    signature;

console.log(`requestUrl: ${requestUrl}`);

console.log(`\nSending ${httpMethod} request to IBM COS -----------------------`);
console.log('Request URL = ' + requestUrl);

// create and send the request
console.log(`\nSending ${httpMethod} request to IBM COS -----------------------`);
console.log('Request URL = ' + requestUrl);

var request = https.get(requestUrl, function (response) {
    console.log('\nResponse from IBM COS ----------------------------------');
    console.log(`Response code: ${response.statusCode}\n`);

    response.on('data', function (chunk) {
        console.log('Response: ' + chunk);
        printDebug();
    });
});

request.end();
```