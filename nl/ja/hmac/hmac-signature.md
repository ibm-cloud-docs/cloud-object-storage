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

# HMAC 署名の構成
{: #hmac-signature}

IBM COS に対し、[API キーまたはベアラー・トークン](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)の代わりに [HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)を使用して行われる各要求は、認証を受けなければならず、その認証には AWS 署名バージョン 4 `authorization` ヘッダーの実装が使用されます。署名を使用することで、ID 検査と転送中データ保全性が提供され、また各署名は要求のタイム・スタンプに結び付けられるため、許可ヘッダーの再使用が不可能になります。ヘッダーは、アルゴリズム宣言、資格情報、署名付きヘッダー、および計算された署名の 4 つのコンポーネントで構成されます。

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

日付 (date) は、`YYYYMMDD` 形式で指定します。地域 (region) は、指定するバケットのロケーションに対応する必要があります (例えば、`us`)。`host` ヘッダーと `x-amz-date` ヘッダーは常に必要です。要求によっては、その他のヘッダーも必要な場合があります (例えば、ペイロードがある要求の場合は `x-amz-content-sha256` が必要など)。個々の要求ごとに署名を再計算する必要があることから、多くの開発者は、許可ヘッダーを自動的に生成するツールまたは SDK を使用します。

## `authorization` ヘッダーの作成
{: #hmac-auth-header}

まずは、標準化されたフォーマットで要求を作成する必要があります。

1. 使用する HTTP メソッドを宣言します (例えば、`PUT`)。
2. アクセスするリソースを標準化された方法で定義します。これは、アドレスの `http(s)://` と照会ストリングの間の部分です。アカウント・レベルの要求 (すなわちバケットのリスト) の場合、この部分は単純に `/` です。
3. 要求のパラメーターがある場合は、パーセントでエンコードし (例えば、スペースであれば `%20` と表現)、アルファベットで表すことで標準化する必要があります。
4. ヘッダーは、空白文字を削除し、小文字に変換し、それぞれに改行を追加することで標準化する必要があります。その後、ヘッダーは ASCII 順にソートされる必要があります。
5. 標準形式でリストされた後は、「署名」される必要があります。これには、値ではなくヘッダー名のみが必要です。ヘッダー名をセミコロンで区切り、アルファベット順にリストします。`host` と `x-amz-date` は、すべての要求に必要です。
6. オブジェクトをアップロードするときや、新しい ACL を作成するときのように、要求に本体が含まれる場合、要求本体は、SHA-256 アルゴリズムを使用してハッシュされ、base-16 エンコードされた小文字として表される必要があります。
7. HTTP メソッド、標準化されたリソース、標準化されたパラメーター、標準化されたヘッダー、署名済みヘッダー、およびハッシュされた要求本体のそれぞれを改行で区切って組み合わせることで、標準化された要求を形成します。

次は、最終的な署名を形成するために署名キーと結合されることになる「string-to-sign」をアセンブルする必要があります。string-to-sign の形式は以下のようになります。

```
AWS4-HMAC-SHA256
{time}
{date}/{string}/s3/aws4_request
{hashed-standardized-request}
```

1. 時間 (time) は、現在の UTC でなければならず、ISO 8601 仕様に従ってフォーマット設定される必要があります (例えば、`20161128T152924Z`)。
2. 日付 (date) は、`YYYYMMDD` 形式にします。
3. 最後の行は、SHA-256 アルゴリズムを使用してハッシュされた、前に作成した標準化された要求です。

ここで、署名を実際に計算する必要があります。

1. まず、アカウントの秘密アクセス・キー、現在日付、および使用する地域と API タイプに基づいて署名キーを計算する必要があります。
2. ストリング `AWS4` が秘密アクセス・キーの前に付加され、その新しいストリングが日付をハッシュするためのキーとして使用されます。
3. 次に、結果のハッシュが、地域をハッシュするためのキーとして使用されます。
4. このプロセスが続行され、新しいハッシュが、API タイプをハッシュするためのキーとして使用されます。
5. 最後に、署名キーを作成するストリング `aws4_request` をハッシュするためのキーとして、最新のハッシュが使用されます。
6. これで、署名キーが、最終的な署名を生成する string-to-sign をハッシュするためのキーとして使用されます。

これで残りは、以下に示すように `authorization` ヘッダーを実際にアセンブルするステップのみになりました。

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

## `authorization` ヘッダーの生成
{: #hmac-auth-header-generate}

### Python の例
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

### Java の例
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

### NodeJS の例
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
