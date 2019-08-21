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

# Construcción de una firma de HMAC
{: #hmac-signature}

Cada solicitud que se realiza sobre IBM COS utilizando [credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) en lugar de una [clave de API o señal de portadora](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) se debe autenticar mediante una implementación de la cabecera `authorization` de AWS Signature Versión 4. El uso de una firma proporciona verificación de identidad e integridad de los datos en tránsito, y, puesto que cada firma está vinculada a la indicación de fecha y hora de la solicitud, no se pueden reutilizar las cabeceras de autorización. La cabecera se compone de cuatro componentes: una declaración de algoritmo, información de credencial, cabeceras firmadas y la signatura calculada:

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

La fecha se proporciona en formato `AAAAMMDD` y la región debe corresponder con la ubicación del grupo especificado, por ejemplo `us`. Las cabeceras `host` y `x-amz-date` son siempre necesarias, y, en función de la solicitud, también pueden ser necesarias otras cabeceras (por ejemplo, `x-amz-content-sha256` en el caso de solicitudes con cargas útiles). Debido a la necesidad de volver a calcular la firma de cada solicitud individual, muchos desarrolladores prefieren utilizar una herramienta o SDK que generará automáticamente la cabecera de autorización.

## Creación de una cabecera `authorization`
{: #hmac-auth-header}

En primer lugar, necesitamos crear una solicitud en un formato estándar.

1. Declare el método HTTP que se va a utilizar (por ejemplo, `PUT`)
2. Defina el recurso al que se va a acceder de forma estandarizada. Esta es la parte de la dirección comprendida entre `http(s)://` y la serie de consulta. Para las solicitudes de nivel de cuenta (por ejemplo, obtención de una lista de grupos), esto es simplemente `/`.
3. Si hay parámetros de solicitud, se deben estandarizar con una codificación de porcentaje (por ejemplo, los espacios se deben representar como `%20`) y alfabetizar.
4. Las cabeceras se deben estandarizar eliminando espacios en blanco, convirtiendo a minúsculas y añadiendo una línea nueva a cada una, y luego se deben clasificar en orden ASCII.
5. Después de que se listen en formato estándar, se deben 'firmar'. Esto se hace tomando solo los nombres de cabecera, no sus valores, y mostrándolas en una lista en orden alfabético, separadas por signos de punto y coma. `Host` y `x-amz-date` son necesarias para todas las solicitudes.
6. Si la solicitud tiene un cuerpo, como por ejemplo cuando se carga un objeto o se crea una nueva ACL, al cuerpo de la solicitud se le debe añadir hash mediante el algoritmo SHA-256 y se debe representar como caracteres en minúsculas codificados en base-16.
7. Combine el método HTTP, el recurso estandarizado, los parámetros estandarizados, las cabeceras estandarizadas, las cabeceras firmadas y el cuerpo de la solicitud con hash, separados por una nueva línea, para formar la solicitud estandarizada.

A continuación tenemos que ensamblar una "serie que firmar", que se combinará con la clave de la firma para formar la firma final. La serie que firmar tiene el formato siguiente:

```
AWS4-HMAC-SHA256
{time}
{date}/{string}/s3/aws4_request
{hashed-standardized-request}
```

1. La indicación de hora debe ser UTC actual y se debe formatear de acuerdo con la especificación ISO 8601 (por ejemplo, `20161128T152924Z`).
2. La fecha está en formato `AAAAMMDD`.
3. La línea final es la solicitud estandarizada creada anteriormente con hash mediante el algoritmo SHA-256.

Ahora tenemos que calcular la firma.

1. En primer lugar, hay que calcular la clave de firma a partir de la clave de acceso secreta de la cuenta, la fecha actual y la región y el tipo de API que se utilizan.
2. Se antepone la serie `AWS4` a la clave de acceso secreta y se utiliza la nueva serie como la clave para crear el hash de la fecha.
3. El hash resultante se utiliza como clave para crear el hash de la región.
4. El proceso continúa, utilizando el nuevo hash como clave para crear el hash del tipo de API.
5. Finalmente, se utiliza el hash más reciente como clave para crear el hash de la serie `aws4_request` que crea la clave de firma.
6. A continuación, se utiliza la clave de firma como clave para crear el hash de la serie que firmar que genera la firma final.

Ahora el único paso que queda es ensamblar la cabecera `authorization` tal como se muestra:

```
AWS4-HMAC-SHA256 Credential={access-key}/{date}/{region}/s3/aws4_request,SignedHeaders=host;x-amz-date;{other-required-headers},Signature={signature}
```

## Generación de una cabecera `authorization`
{: #hmac-auth-header-generate}

### Ejemplo de Python
{: #hmac-auth-header-generate-python}

```python
import os
import datetime
import hashlib
import hmac
import requests

# no guardar credenciales directamente en el código
access_key = os.environ.get('COS_HMAC_ACCESS_KEY_ID')
secret_key = os.environ.get('COS_HMAC_SECRET_ACCESS_KEY')

# elementos de la solicitud
http_method = 'GET'
host = 's3.us.cloud-object-storage.appdomain.cloud'
region = 'us-standard'
endpoint = 'http://s3.us.cloud-object-storage.appdomain.cloud'
bucket = '' # añada '/' antes del nombre del grupo para listar grupos
object_key = ''
request_parameters = ''


# métodos de hash y de firma
def hash(key, msg):
    return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).digest()

# la región es un valor comodín que ocupa el lugar del valor de región de AWS
# ya que COS no utiliza el mismo convenio para regiones; este parámetro acepta cualquier serie
def createSignatureKey(key, datestamp, region, service):

    keyDate = hash(('AWS4' + key).encode('utf-8'), datestamp)
    keyString = hash(keyDate, region)
    keyService = hash(keyString, service)
    keySigning = hash(keyService, 'aws4_request')
    return keySigning


# ensamblar la solicitud estandarizada
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


# ensamblar la serie que firmar
hashing_algorithm = 'AWS4-HMAC-SHA256'
credential_scope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request'
sts = (hashing_algorithm + '\n' +
       timestamp + '\n' +
       credential_scope + '\n' +
       hashlib.sha256(standardized_request).hexdigest())


# generar la firma
signature_key = createSignatureKey(secret_key, datestamp, region, 's3')
signature = hmac.new(signature_key,
                     (sts).encode('utf-8'),
                     hashlib.sha256).hexdigest()


# ensamblar todos los elementos en la cabecera 'authorization'
v4auth_header = (hashing_algorithm + ' ' +
                 'Credential=' + access_key + '/' + credential_scope + ', ' +
                 'SignedHeaders=' + signed_headers + ', ' +
                 'Signature=' + signature)


# crear y enviar la solicitud
headers = {'x-amz-date': timestamp, 'Authorization': v4auth_header}
# el paquete 'requests' añade automáticamente la cabecera 'host' necesaria
request_url = endpoint + standardized_resource + standardized_querystring

print('\nSending `%s` request to IBM COS -----------------------' % http_method)
print('Request URL = ' + request_url)
request = requests.get(request_url, headers=headers)

print('\nResponse from IBM COS ----------------------------------')
print('Response code: %d\n' % request.status_code)
print(request.text)
```

### Ejemplo de Java
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
    // no guardar credenciales directamente en el código
    private static final String accessKey = System.getenv("COS_HMAC_ACCESS_KEY_ID");
    private static final String secretKey = System.getenv("COS_HMAC_SECRET_ACCESS_KEY");
    // constantes
    private static final String httpMethod = "GET";
    private static final String host = "s3.us.cloud-object-storage.appdomain.cloud";
    private static final String region = "us-standard";
    private static final String endpoint = "https://s3.us.cloud-object-storage.appdomain.cloud";
    private static final String bucket = ""; // añada '/' antes del nombre del grupo para listar grupos
    private static final String objectKey = "";
    private static final String requestParameters = "";
    
    public static void main(String[] args) {
        try {
            // ensamblar la solicitud estandarizada
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

            // ensamblar la serie que firmar
            String hashingAlgorithm = "AWS4-HMAC-SHA256";
            String credentialScope = datestamp + "/" + region + "/" + "s3" + "/" + "aws4_request";
            String sts = hashingAlgorithm + "\n" +
                timestamp + "\n" +
                credentialScope + "\n" +
                hashHex(standardizedRequest);

            // generar la firma
            byte[] signatureKey = createSignatureKey(secretKey, datestamp, region, "s3");
            String signature = hmacHex(signatureKey, sts);
            
            // ensamblar todos los elementos en la cabecera "authorization"
            String v4auth_header = hashingAlgorithm + " " +
                "Credential=" + accessKey + "/" + credentialScope + ", " +
                "SignedHeaders=" + signedHeaders + ", " +
                "Signature=" + signature;
            
            // crear y enviar la solicitud
            String requestUrl = endpoint + standardizedResource + standardizedQuerystring;
            URL urlObj = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod(httpMethod);

            //añadir cabeceras de solicitud
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

            //mostrar resultado
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
    
    // la región es un valor comodín que ocupa el lugar del valor de región de AWS
    // ya que COS no utiliza el mismo convenio para regiones; este parámetro acepta cualquier serie
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

### Ejemplo de NodeJS
{: #hmac-auth-header-generate-node}

```javascript
const crypto = require('crypto');
const moment = require('moment');
const https = require('https');
    
// no guardar credenciales directamente en el código
const accessKey = process.env.COS_HMAC_ACCESS_KEY_ID;
const secretKey = process.env.COS_HMAC_SECRET_ACCESS_KEY;

const httpMethod = 'GET';
const host = 's3.us.cloud-object-storage.appdomain.cloud';
const region = 'us-standard';
const endpoint = 'https://s3.us.cloud-object-storage.appdomain.cloud';
const bucket = ''; // add a '/' before the bucket name to list buckets
const objectKey = '';
const requestParameters = '';

// métodos de hash y de firma
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

// la región es un valor comodín que ocupa el lugar del valor de región de AWS
// ya que COS no utiliza el mismo convenio para regiones; este parámetro acepta cualquier serie
function createSignatureKey(key, datestamp, region, service) {
    keyDate = hash(('AWS4' + key), datestamp);
    keyString = hash(keyDate, region);
    keyService = hash(keyString, service);
    keySigning = hash(keyService, 'aws4_request');
    return keySigning;
}

// ensamblar la solicitud estandarizada
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

// ensamblar la serie que firmar
var hashingAlgorithm = 'AWS4-HMAC-SHA256';
var credentialScope = datestamp + '/' + region + '/' + 's3' + '/' + 'aws4_request';
var sts = hashingAlgorithm + '\n' +
    timestamp + '\n' +
    credentialScope + '\n' +
    hashHex(standardizedRequest);

// generar la firma
var signatureKey = createSignatureKey(secretKey, datestamp, region, 's3');
var signature = hmacHex(signatureKey, sts);

// ensamblar todos los elementos en la cabecera 'authorization'
var v4authHeader = hashingAlgorithm + ' ' +
    'Credential=' + accessKey + '/' + credentialScope + ', ' +
    'SignedHeaders=' + signedHeaders + ', ' +
    'Signature=' + signature;

// crear y enviar la solicitud
var authHeaders = {'x-amz-date': timestamp, 'Authorization': v4authHeader}
// el paquete 'requests' añade automáticamente la cabecera 'host' necesaria
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
