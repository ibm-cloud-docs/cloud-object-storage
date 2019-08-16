---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature, create

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
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

# HMAC 서명 생성
{: #hmac-signature}

[API 키 또는 Bearer 토큰](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)이 아니라 [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)를 사용하여 IBM COS에 전달되는 각 요청은 AWS 서명 버전 4 `authorization` 헤더의 구현을 사용하여 인증되어야 합니다. 서명 사용은 ID 확인 및 전송 중 데이터 무결성을 제공하며, 각 서명은 요청의 시간소인과 연결되어 있으므로 권한 부여 헤더를 재사용하는 것은 불가능합니다. 이 헤더는 알고리즘 선언, 인증 정보, 서명된 헤더, 계산된 서명의 네 가지 구성요소로 구성됩니다. 

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

날짜는 `YYYYMMDD` 형식으로 제공되며, 지역은 `us`와 같이 지정된 버킷의 위치에 대응해야 합니다. `host` 및 `x-amz-date` 헤더는 항상 필수이며, 요청에 따라 다른 헤더 또한 필요할 수 있습니다(예: 페이로드를 포함하는 요청의 경우 `x-amz-content-sha256`). 각 개별 요청마다 서명을 다시 계산해야 하므로, 많은 개발자는 권한 부여 헤더를 자동으로 생성하는 도구 또는 SDK를 사용합니다. 

## `authorization` 헤더 작성
{: #hmac-auth-header}

먼저 표준화된 형식으로 요청을 작성해야 합니다. 

1. 사용할 HTTP 메소드를 선언하십시오(예: `PUT`). 
2. 액세스하는 리소스를 표준화된 방식으로 정의하십시오. 이는 `http(s)://`와 조회 문자열 사이의 주소 부분입니다. 계정 레벨에서의 요청(예: 버킷 나열)의 경우 이는 단순히 `/`입니다. 
3. 요청 매개변수가 있는 경우에는 이를 퍼센트로 인코딩(예: 간격은 `%20`으로 표시해야 함)하여 표준화하고 알파벳으로 표기해야 합니다. 
4. 헤더는 공백을 제거하고, 소문자로 변환하고, 각각에 새 행을 추가하여 표준화한 후 ASCII 순서대로 정렬해야 합니다. 
5. 표준 형식으로 나열하고 나면 여기에 '서명'해야 합니다. 이는 헤더의 값이 아니라 이름만 가져와, 세미콜론으로 구분하여 알파벳순으로 나열하는 것입니다. `Host` 및 `x-amz-date`는 모든 요청에서 필수입니다. 
6. 오브젝트를 업로드하거나 새 ACL을 작성하는 경우와 같이 요청에 본문이 있는 경우 요청 본문은 SHA-256 알고리즘을 사용하여 해시되어야 하며 Base-16 인코딩된 소문자로 표현되어야 합니다. 
7. 각각 줄 바꾸기로 구분된 HTTP 메소드, 표준화된 리소스, 표준화된 매개변수, 표준화된 헤더, 서명된 헤더, 해시된 요청 본문을 결합하여 표준화된 요청을 구성하십시오. 

그 다음에는 최종 서명을 생성하기 위해 서명 키와 결합될 '서명할 문자열'을 구성해야 합니다. 서명할 문자열은 다음 양식을 사용합니다. 

```
AWS4-HMAC-SHA256
{time}
{date}/{string}/s3/aws4_request
{hashed-standardized-request}
```

1. 시간은 현재 UTC여야 하며 ISO 8601 스펙에 따라 형식화되어야 합니다(예: `20161128T152924Z`). 
2. 날짜는 `YYYYMMDD` 형식입니다. 
3. 마지막 행은 SHA-256 알고리즘을 사용하여 해시된, 이전에 작성된 표준화된 요청입니다. 

이제 실제로 서명을 계산해야 합니다. 

1. 먼저 사용되고 있는 계정의 비밀 액세스 키, 현재 날짜, 지역 및 API 유형으로부터 서명 키를 계산해야 합니다. 
2. 비밀 액세스 키 앞에 문자열 `AWS4`가 추가되며, 해당 새 문자열이 날짜를 해시하기 위한 키로 사용됩니다. 
3. 그 후 결과 해시가 지역을 해시하기 위한 키로 사용됩니다. 
4. 새 해시가 API 유형을 해시하기 위한 키로 사용되며 프로세스가 계속됩니다. 
5. 최종적으로 마지막 해시가 문자열 `aws4_request`를 해시하기 위한 키로 사용되며 서명 키가 작성됩니다. 
6. 그 후에는 이 서명 키가 서명할 문자열을 해시하기 위한 키로 사용되며 최종 서명이 생성됩니다. 

이제 남은 단계는 다음에 표시되어 있는 바와 같이 실제로 `authorization` 헤더를 구성하는 것입니다. 

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

## `authorization` 헤더 생성
{: #hmac-auth-header-generate}

### Python 예
{: #hmac-auth-header-generate-python}

```python
import os
import datetime
import hashlib
import hmac
import requests

# please don't store credentials directly in code
access_key = os.environ.get('COS_HMAC_ACCESS_KEY_ID')
secret_key = os.environ.get('COS_HMAC_SECRET_ACCESS_KEY')

# request elements
http_method = 'GET'
host = 's3.us.cloud-object-storage.appdomain.cloud'
region = 'us-standard'
endpoint = 'http://s3.us.cloud-object-storage.appdomain.cloud'
bucket = '' # add a '/' before the bucket name to list buckets
object_key = ''
request_parameters = ''


# hashing and signing methods
def hash(key, msg):
    return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).digest()

# region is a wildcard value that takes the place of the AWS region value
# as COS doen't use the same conventions for regions, this parameter can accept any string
def createSignatureKey(key, datestamp, region, service):

    keyDate = hash(('AWS4' + key).encode('utf-8'), datestamp)
    keyString = hash(keyDate, region)
    keyService = hash(keyString, service)
    keySigning = hash(keyService, 'aws4_request')
    return keySigning


# assemble the standardized request
time = datetime.datetime.utcnow()
timestamp = time.strftime('%Y%m%dT%H%M%SZ')
datestamp = time.strftime('%Y%m%d')

standardized_resource = bucket + '/' + object_key
standardized_querystring = request_parameters
standardized_headers = 'host:' + host + '\n' + 'x-amz-date:' + timestamp + '\n'
signed_headers = 'host;x-amz-date'
payload_hash = hashlib.sha256(''.encode('utf-8')).hexdigest()

standardized_request = (http_method + '\n' +
                        standardized_resource + '\n' +
                        standardized_querystring + '\n' +
                        standardized_headers + '\n' +
                        signed_headers + '\n' +
                        payload_hash).encode('utf-8')


# assemble string-to-sign
hashing_algorithm = 'AWS4-HMAC-SHA256'
credential_scope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request'
sts = (hashing_algorithm + '\n' +
       timestamp + '\n' +
       credential_scope + '\n' +
       hashlib.sha256(standardized_request).hexdigest())


# generate the signature
signature_key = createSignatureKey(secret_key, datestamp, region, 's3')
signature = hmac.new(signature_key,
                     (sts).encode('utf-8'),
                     hashlib.sha256).hexdigest()


# assemble all elements into the 'authorization' header
v4auth_header = (hashing_algorithm + ' ' +
                 'Credential=' + access_key + '/' + credential_scope + ', ' +
                 'SignedHeaders=' + signed_headers + ', ' +
                 'Signature=' + signature)


# create and send the request
headers = {'x-amz-date': timestamp, 'Authorization': v4auth_header}
# the 'requests' package autmatically adds the required 'host' header
request_url = endpoint + standardized_resource + standardized_querystring

print('\nSending `%s` request to IBM COS -----------------------' % http_method)
print('Request URL = ' + request_url)
request = requests.get(request_url, headers=headers)

print('\nResponse from IBM COS ----------------------------------')
print('Response code: %d\n' % request.status_code)
print(request.text)
```

### Java 예
{: #hmac-auth-header-generate-java}

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

public class CosHMAC {
    // please don't store credentials directly in code
    private static final String accessKey = System.getenv("COS_HMAC_ACCESS_KEY_ID");
    private static final String secretKey = System.getenv("COS_HMAC_SECRET_ACCESS_KEY");
    // constants
    private static final String httpMethod = "GET";
    private static final String host = "s3.us.cloud-object-storage.appdomain.cloud";
    private static final String region = "us-standard";
    private static final String endpoint = "https://s3.us.cloud-object-storage.appdomain.cloud";
    private static final String bucket = ""; // add a '/' before the bucket name to list buckets
    private static final String objectKey = "";
    private static final String requestParameters = "";
    
    public static void main(String[] args) {
        try {
            // assemble the standardized request
            ZonedDateTime time = ZonedDateTime.now(ZoneOffset.UTC);
            String datestamp = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String timestamp = datestamp + "T" + time.format(DateTimeFormatter.ofPattern("HHmmss")) + "Z";

            String standardizedResource = bucket + "/" + objectKey;
            String standardizedQuerystring = requestParameters;
            String standardizedHeaders = "host:" + host + "\n" + "x-amz-date:" + timestamp + "\n";
            String signedHeaders = "host;x-amz-date";
            String payloadHash = hashHex("");

            String standardizedRequest = httpMethod + "\n" +
                standardizedResource + "\n" +
                standardizedQuerystring + "\n" +
                standardizedHeaders + "\n" +
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
            
            // assemble all elements into the "authorization" header
            String v4auth_header = hashingAlgorithm + " " +
                "Credential=" + accessKey + "/" + credentialScope + ", " +
                "SignedHeaders=" + signedHeaders + ", " +
                "Signature=" + signature;
            
            // create and send the request
            String requestUrl = endpoint + standardizedResource + standardizedQuerystring;
            URL urlObj = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod(httpMethod);

            //add request headers
            con.setRequestProperty("x-amz-date", timestamp);
            con.setRequestProperty("Authorization", v4auth_header);

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

    private static byte[] hash(byte[] key, String msg) {
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

    private static String hmacHex(byte[] key, String msg) {
        String returnVal = null;
        try {
            returnVal = toHexString(hash(key, msg));
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            return returnVal;
        }
    }
    
    private static String hashHex(String msg) {
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
    private static byte[] createSignatureKey(String key, String datestamp, String region, String service) {
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
}
```

### NodeJS 예
{: #hmac-auth-header-generate-node}

```javascript
const crypto = require('crypto');
const moment = require('moment');
const https = require('https');
    
// please don't store credentials directly in code
const accessKey = process.env.COS_HMAC_ACCESS_KEY_ID;
const secretKey = process.env.COS_HMAC_SECRET_ACCESS_KEY;

const httpMethod = 'GET';
const host = 's3.us.cloud-object-storage.appdomain.cloud';
const region = 'us-standard';
const endpoint = 'https://s3.us.cloud-object-storage.appdomain.cloud';
const bucket = ''; // add a '/' before the bucket name to list buckets
const objectKey = '';
const requestParameters = '';

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

// assemble the standardized request
var time = moment().utc();
var timestamp = time.format('YYYYMMDDTHHmmss') + 'Z';
var datestamp = time.format('YYYYMMDD');

var standardizedResource = bucket + '/' + objectKey;
var standardizedQuerystring = requestParameters;
var standardizedHeaders = 'host:' + host + '\n' + 'x-amz-date:' + timestamp + '\n';
var signedHeaders = 'host;x-amz-date';
var payloadHash = hashHex('');

var standardizedRequest = httpMethod + '\n' +
    standardizedResource + '\n' +
    standardizedQuerystring + '\n' +
    standardizedHeaders + '\n' +
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
var signatureKey = createSignatureKey(secretKey, datestamp, region, 's3');
var signature = hmacHex(signatureKey, sts);

// assemble all elements into the 'authorization' header
var v4authHeader = hashingAlgorithm + ' ' +
    'Credential=' + accessKey + '/' + credentialScope + ', ' +
    'SignedHeaders=' + signedHeaders + ', ' +
    'Signature=' + signature;

// create and send the request
var authHeaders = {'x-amz-date': timestamp, 'Authorization': v4authHeader}
// the 'requests' package autmatically adds the required 'host' header
console.log(authHeaders);
var requestUrl = endpoint + standardizedResource + standardizedQuerystring

console.log(`\nSending ${httpMethod} request to IBM COS -----------------------`);
console.log('Request URL = ' + requestUrl);

var options = {
    host: host,
    port: 443,
    path: standardizedResource + standardizedQuerystring,
    method: httpMethod,
    headers: authHeaders
}

var request = https.request(options, function (response) {
    console.log('\nResponse from IBM COS ----------------------------------');
    console.log(`Response code: ${response.statusCode}\n`);
    
    response.on('data', function (chunk) {
        console.log(chunk.toString());
    });
});

request.end();
```
