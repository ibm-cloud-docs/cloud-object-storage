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

# HMAC-Signatur erstellen
{: #hmac-signature}

Jede Anforderung, die nicht mit einem [API-Schlüssel oder Trägertoken](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview), sondern stattdessen mit [HMAC-Berechtigungsnachweisen](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) an IBM COS gestellt wird, muss unter Verwendung einer Implementierung des `authorization`-Headers der AWS-Signatur Version 4 authentifiziert werden. Durch die Verwendung einer Signatur wird die Prüfung der Identität und Integrität der Daten während der Übertragung sichergestellt und da jede Signatur an den Zeitstempel der Anforderung gebunden ist, ist es nicht möglich, Berechtigungsheader wiederzuverwenden. Der Header setzt sich aus vier Komponenten zusammen: einer Algorithmusdeklaration, den Anmeldeinformationen, signierten Headern und der berechneten Signatur:

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

Das Datum wird in Format `JJJJMMTT` angegeben und die Region sollte dem Standort des angegebenen Buckets entsprechen, zum Beispiel `us`. Die Header `host` und `x-amz-date` sind stets erforderlich und je nach Anforderung sind gegebenenfalls auch weitere Header erforderlich (wie z. B. `x-amz-content-sha256` bei Anforderungen mit Nutzdaten). Da die Signatur für jede einzelne Anforderung neu berechnet werden muss, bevorzugen viele Entwickler ein Tool oder SDK, das den Berechtigungsheader (Header des Typs `authorization`) automatisch erzeugt.

## Header vom Typ `authorization` erstellen
{: #hmac-auth-header}

Zuerst muss eine Anfrage in einem standardisierten Format erstellt werden.

1. Deklarieren Sie, welche HTTP-Methode verwendet wird (z. B. `PUT`).
2. Definieren Sie die Ressource, auf die in standardisierter Weise zugegriffen wird. Dies ist der Teil der Adresse zwischen `http(s)://` und der Abfragezeichenfolge. Für Anforderungen auf Kontoebene (wie zum Beispiel die Auflistung von Buckets) ist dies einfach das Zeichen `/`.
3. Falls Anforderungsparameter verwendet werden, müssen diese Angaben als durch als Prozentwert codierte Werte (Leerzeichen müssen beispielsweise durch `%20` dargestellt sein) und in alphabetischer Sortierung standardisiert sein.
4. Header müssen standardisiert werden, indem enthaltene Leerzeichen entfernt, ihre Schreibung in Kleinbuchstaben umgewandelt und jeweils ein Zeilenumbruch hinzugefügt wird und anschließend eine Sortierung in ASCII-Reihenfolge durchgeführt wird.
5. Nachdem sie in Standardformat aufgelistet worden sind, müssen die Header 'signiert' werden. Das bedeutet, dass nur die Headernamen (nicht aber ihre Werte) in alphabetischer Reihenfolge und jeweils getrennt durch ein Semikolon aufgelistet werden. `Host` und `x-amz-date` sind für alle Anforderungen erforderlich.
6. Wenn die Anforderung über einen Hauptteil verfügt, wenn beispielsweise ein Objekt hochgeladen oder eine neue ACL erstellt wird, muss der Anforderungshauptteil unter Verwendung des Algorithmus SHA-256 hashverschlüsselt und mit Base16 in Kleinbuchstaben dargestellt werden.
7. Formulieren Sie durch Verbinden der HTTP-Methode, der standardisierten Ressource, der standardisierten Parameter, der standardisierten Header und des hashverschlüsselten Anforderungshauptteils jeweils unter Angabe eines Zeilenumbruch zur Trennung eine standardisierte Anforderung.

Als Nächstes müssen Sie eine zu signierende Zeichenfolge (String-to-sign) assemblieren, die in Verbindung mit dem Signaturschlüssel die endgültige Signatur bildet. Die zu signierende Zeichenfolge (String-to-sign) verwendet das folgende Format:

```
AWS4-HMAC-SHA256
{time}
{date}/{string}/s3/aws4_request
{hashed-standardized-request}
```

1. Die Zeit muss in der aktuellen koordinierten Weltzeit (Coordinated Universal Time, UTC) angegeben werden und gemäß Spezifikation ISO 8601 formatiert sein (z. B. `20161128T152924Z`).
2. Das Datum ist im Format `JJJJMMTT` anzugeben.
3. Die letzte Zeile besteht aus der zuvor erstellten standardisierten Anfrage, die mit dem SHA-256-Algorithmus hashverschlüsselt wurde.

Jetzt müssen Sie die Signatur tatsächlich berechnen.

1. Zuerst muss der Signaturschlüssel aus dem geheimen Zugriffsschlüssel des Kontos, dem aktuellen Datum, der Region sowie dem verwendeten API-Typ berechnet werden.
2. dem geheimen Zugriffsschlüssel wird die Zeichenfolge `AWS4` als Präfix vorangestellt und die daraus entstandene neue Zeichenfolge wird als Schüssel zum Umwandeln des Datums in einen Hashwert verwendet.
3. Der Hashwert, die sich daraus ergeben hat, wird als Schlüssel für die Umwandlung der Region in einen Hashwert verwendet.
4. Der Prozess wird unter Verwendung des neuen Hashwerts als Schlüssel zur Umwandlung des API-Typs in einen Hashwert fortgesetzt.
5. Zum Abschluss wird der neueste Hashwert als Schlüssel zur Umwandlung der Zeichenfolge `aws4_request` in einen Hashwert und damit zur Erstellung des Signaturschlüssels verwendet.
6. Der Signaturschlüssel wird dann als Schlüssel zur Umwandlung der zu signierenden Zeichenfolge (String-to-sign) verwendet, wodurch die endgültige Signatur generiert wird.

Als letzter noch auszuführender Schritt muss nur noch der Header vom Typ `authorization` wie hier gezeigt assembliert werden:

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

## Header vom Typ `authorization` generieren
{: #hmac-auth-header-generate}

### Python-Beispiel
{: #hmac-auth-header-generate-python}

```python
import os
import datetime
import hashlib
import hmac
import requests

# Berechtigungsnachweise bitte nicht direkt im Code speichern
access_key = os.environ.get('COS_HMAC_ACCESS_KEY_ID')
secret_key = os.environ.get('COS_HMAC_SECRET_ACCESS_KEY')

# Elemente anfordern
http_method = 'GET'
host = 's3.us.cloud-object-storage.appdomain.cloud'
region = 'us-standard'
endpoint = 'http://s3.us.cloud-object-storage.appdomain.cloud'
bucket = '' # Zum Auflisten von Buckets dem Bucketnamen einen Schrägstrich ('/') voranstellen
object_key = ''
request_parameters = ''


# Hashing- und Signiermethoden
def hash(key, msg):
    return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).digest()

# 'Region' ist ein Platzhalterwert, der an die Stelle des AWS-Regionswerts tritt
# Da COS nicht dieselben Konventionen für Regionen verwendet, akzeptiert dieser Parameter jede beliebige Zeichenkette
def createSignatureKey(key, datestamp, region, service):

    keyDate = hash(('AWS4' + key).encode('utf-8'), datestamp)
    keyString = hash(keyDate, region)
    keyService = hash(keyString, service)
    keySigning = hash(keyService, 'aws4_request')
    return keySigning


# Standardisierte Anforderung assemblieren
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


# Zu signierende Zeichenfolge (String-to-sign) assemblieren
hashing_algorithm = 'AWS4-HMAC-SHA256'
credential_scope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request'
sts = (hashing_algorithm + '\n' +
       timestamp + '\n' +
       credential_scope + '\n' +
       hashlib.sha256(standardized_request).hexdigest())


# Signatur generieren
signature_key = createSignatureKey(secret_key, datestamp, region, 's3')
signature = hmac.new(signature_key,
                     (sts).encode('utf-8'),
                     hashlib.sha256).hexdigest()


# Alle Elemente im 'authorization'-Header assemblieren
v4auth_header = (hashing_algorithm + ' ' +
                 'Credential=' + access_key + '/' + credential_scope + ', ' +
                 'SignedHeaders=' + signed_headers + ', ' +
                 'Signature=' + signature)


# Anforderung erstellen und senden
headers = {'x-amz-date': timestamp, 'Authorization': v4auth_header}
# Das Paket 'requests' fügt automatisch den erforderlichen 'host'-Header hinzu
request_url = endpoint + standardized_resource + standardized_querystring

print('\nSending `%s` request to IBM COS -----------------------' % http_method)
print('Request URL = ' + request_url)
request = requests.get(request_url, headers=headers)

print('\nResponse from IBM COS ----------------------------------')
print('Response code: %d\n' % request.status_code)
print(request.text)
```

### Java-Beispiel
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
    // Berechtigungsnachweise bitte nicht direkt im Code speichern
    private static final String accessKey = System.getenv("COS_HMAC_ACCESS_KEY_ID");
    private static final String secretKey = System.getenv("COS_HMAC_SECRET_ACCESS_KEY");
    // Konstanten
    private static final String httpMethod = "GET";
    private static final String host = "s3.us.cloud-object-storage.appdomain.cloud";
    private static final String region = "us-standard";
    private static final String endpoint = "https://s3.us.cloud-object-storage.appdomain.cloud";
    private static final String bucket = ""; // Zum Auflisten von Buckets dem Bucketnamen einen Schrägstrich ('/') voranstellen
    private static final String objectKey = "";
    private static final String requestParameters = "";

    public static void main(String[] args) {
        try {
            // Standardisierte Anforderung assemblieren
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

            // Zu signierende Zeichenfolge (String-to-sign) assemblieren
            String hashingAlgorithm = "AWS4-HMAC-SHA256";
            String credentialScope = datestamp + "/" + region + "/" + "s3" + "/" + "aws4_request";
            String sts = hashingAlgorithm + "\n" +
                timestamp + "\n" +
                credentialScope + "\n" +
                hashHex(standardizedRequest);

            // Signatur generieren
            byte[] signatureKey = createSignatureKey(secretKey, datestamp, region, "s3");
            String signature = hmacHex(signatureKey, sts);

            // Alle Elemente im "authorization"-Header assemblieren
            String v4auth_header = hashingAlgorithm + " " +
                "Credential=" + accessKey + "/" + credentialScope + ", " +
                "SignedHeaders=" + signedHeaders + ", " +
                "Signature=" + signature;
            
            // Anforderung erstellen und senden
            String requestUrl = endpoint + standardizedResource + standardizedQuerystring;
            URL urlObj = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod(httpMethod);

            // Anforderungsheader hinzufügen
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
    
    // "Region" ist ein Platzhalterwert, der an die Stelle des AWS-Regionswerts tritt
    // Da COS nicht dieselben Konventionen für Regionen verwendet, akzeptiert dieser Parameter jede beliebige Zeichenkette
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

### NodeJS-Beispiel
{: #hmac-auth-header-generate-node}

```javascript
const crypto = require('crypto');
const moment = require('moment');
const https = require('https');
    
// Berechtigungsnachweise bitte nicht direkt im Code speichern
const accessKey = process.env.COS_HMAC_ACCESS_KEY_ID;
const secretKey = process.env.COS_HMAC_SECRET_ACCESS_KEY;

const httpMethod = 'GET';
const host = 's3.us.cloud-object-storage.appdomain.cloud';
const region = 'us-standard';
const endpoint = 'https://s3.us.cloud-object-storage.appdomain.cloud';
const bucket = ''; // Zum Auflisten von Buckets dem Bucketnamen einen Schrägstrich ('/') voranstellen
const objectKey = '';
const requestParameters = '';

// Hashing- und Signiermethoden
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

// 'Region' ist ein Platzhalterwert, der an die Stelle des AWS-Regionswerts tritt
// Da COS nicht dieselben Konventionen für Regionen verwendet, akzeptiert dieser Parameter jede beliebige Zeichenkette
function createSignatureKey(key, datestamp, region, service) {
    keyDate = hash(('AWS4' + key), datestamp);
    keyString = hash(keyDate, region);
    keyService = hash(keyString, service);
    keySigning = hash(keyService, 'aws4_request');
    return keySigning;
}

// Standardisierte Anforderung assemblieren
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

// Zu signierende Zeichenfolge (String-to-sign) assemblieren
var hashingAlgorithm = 'AWS4-HMAC-SHA256';
var credentialScope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request';
var sts = hashingAlgorithm + '\n' +
    timestamp + '\n' +
    credentialScope + '\n' +
    hashHex(standardizedRequest);

// Signatur generieren
var signatureKey = createSignatureKey(secretKey, datestamp, region, 's3');
var signature = hmacHex(signatureKey, sts);

// Alle Elemente im 'authorization'-Header assemblieren
var v4authHeader = hashingAlgorithm + ' ' +
    'Credential=' + accessKey + '/' + credentialScope + ', ' +
    'SignedHeaders=' + signedHeaders + ', ' +
    'Signature=' + signature;

// Anforderung erstellen und senden
var authHeaders = {'x-amz-date': timestamp, 'Authorization': v4authHeader}
// Das Paket 'requests' fügt automatisch den erforderlichen 'host'-Header hinzu
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
