---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, objects

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

# Objektoperationen
{: #object-operations}

Diese Operationen dienen zum Lesen, Schreiben und Konfigurieren der Objekte, die in einem Bucket enthalten sind.

Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
{:tip}

## Objekt hochladen
{: #object-operations-put}

Mit einer `PUT`-Anforderung, in der ein Pfad zu einem Objekt angegeben wurde, wird der Anforderungshauptteil als Objekt hochgeladen. Alle Objekte, die in einem Einzelthread hochgeladen werden, müssen kleiner als 500 MB sein (Objekte, die [in mehreren Teilen hochgeladen werden](/docs/services/cloud-object-storage?topic=cloud-object-storage-large-objects), können bis zu 10 TB groß sein).

**Hinweis**: Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben identifiziert werden kann.
{:tip}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
PUT https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Beispielanforderung (HMAC)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: {payload_hash}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
PUT /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

----

## Header eines Objekts abrufen
{: #object-operations-head}

Mit einer `HEAD`-Anforderung, in der ein Pfad zu einem Objekt angegeben wurde, werden die Header des betreffenden Objekts abgerufen.

Beachten Sie hierbei, dass der Wert für `Etag`, der für Objekte zurückgegeben wird, die mit SSE-KP verschlüsselt wurden, **nicht** mit dem MD5-Hashwert des ursprünglichen unverschlüsselten Objekts übereinstimmt.
{:tip}

**Syntax**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
HEAD https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Beispielanforderung (HMAC-Header)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:32:44 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: da214d69-1999-4461-a130-81ba33c484a6
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:49:06 GMT
Content-Length: 11
```

----

## Objekt herunterladen
{: #object-operations-get}

Mit einer `GET`-Anforderung, in der ein Pfad zu einem Objekt angegeben wurde, wird das betreffende Objekt heruntergeladen.

Beachten Sie hierbei, dass der Wert für `Etag`, der für Objekte zurückgegeben wird, die mit SSE-C/SSE-KP verschlüsselt wurden, **nicht** mit dem MD5-Hashwert des ursprünglichen unverschlüsselten Objekts übereinstimmt.
{:tip}

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
GET https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

### Optionale Header
{: #object-operations-get-headers}

Header | Typ | Beschreibung
--- | ---- | ------------
`range` | Zeichenfolge | Gibt die Byte eines Objekts innerhalb des angegebenen Bereichs zurück.

**Beispielanforderung**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC-Header)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:34:25 GMT
X-Clv-Request-Id: 116dcd6b-215d-4a81-bd30-30291fa38f93
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 116dcd6b-215d-4a81-bd30-30291fa38f93
ETag: "d34d8aada2996fc42e6948b926513907"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:46:53 GMT
Content-Length: 467

 Female bees that are not fortunate enough to be selected to be the 'queen'
 while they were still larvae become known as 'worker' bees. These bees lack
 the ability to reproduce and instead ensure that the hive functions smoothly,
 acting almost as a single organism in fulfilling their purpose.
```

----

## Objekt löschen
{: #object-operations-delete}

Mit einer `DELETE`-Anforderung, in der ein Pfad zu einem Objekt angegeben wurde, wird das betreffende Objekt gelöscht.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
DELETE https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Beispielanforderung (HMAC-Header)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 204 No Content
Date: Thu, 25 Aug 2016 17:44:57 GMT
X-Clv-Request-Id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
```

----

## Mehrere Objekte löschen
{: #object-operations-multidelete}

Mit einer `POST`-Anforderung, in der ein Pfad zu einem Bucket und die korrekten Parameter angegeben sind, wird eine angegebene Objektgruppe gelöscht. Es ist ein Header `Content-MD5` erforderlich, in dem der Base64-codierte MD5-Hashwert des Anforderungshauptteils angegeben wird.

Der erforderliche Header `Content-MD5` muss die binäre Darstellung eines Base64-codierten MD5-Hashwerts sein.

**Hinweis:** Wenn ein in der Anforderung angegebenes Objekt nicht gefunden wird, dann wird es im Ergebnis als gelöscht zurückgegeben. 

### Optionale Elemente
{: #object-operations-multidelete-options}

|Header |Typ|Beschreibung|
|---|---|---|
|`Quiet`|Boolesch|Für die Anforderung wird der Befehlszeilenmodus aktiviert.|

Die Anforderung kann maximal 1000 Schlüssel enthalten, die gelöscht werden sollen. Obwohl dies sehr nützlich ist, um den Aufwand pro Anforderung zu reduzieren, müssen Sie beim Löschen einer großen Anzahl von Schlüsseln mit Bedacht vorgehen. Berücksichtigen Sie auch die Größe der Objekte, um eine angemessene Leistung zu gewährleisten.
{:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}?delete= # Pfadstil
POST https://{bucket-name}.{endpoint}?delete= # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**Beispielanforderung (HMAC-Header)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
POST /apiary?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&delete=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Delete>
    <Object>
         <Key>surplus-bee</Key>
    </Object>
    <Object>
         <Key>unnecessary-bee</Key>
    </Object>
</Delete>
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 18:54:53 GMT
X-Clv-Request-Id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Content-Type: application/xml
Content-Length: 207
```
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<DeleteResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Deleted>
         <Key>surplus-bee</Key>
    </Deleted>
    <Deleted>
         <Key>unnecessary-bee</Key>
    </Deleted>
</DeleteResult>
```

----

## Objekt kopieren
{: #object-operations-copy}

Mit einer `PUT`-Anforderung, in der ein Pfad zu einem neuen Objekt angegeben ist, wird eine neue Kopie eines anderen Objekts erstellt, das im Header `x-amz-copy-source` angegeben ist. Sofern die Metadaten nicht an anderer Stelle geändert wurden, bleiben diese gleich.

**Hinweis**: Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben identifiziert werden kann.
{:tip}


**Hinweis**: Das Kopieren eines Elements aus einem *Key Protect*-fähigen Bucket in ein Zielbucket in einer anderen Region unterliegt gewissen Einschränkungen und führt zur Ausgabe der Nachricht `500 - Interner Fehler`.
{:tip}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
PUT https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

### Optionale Header
{: #object-operations-copy-options}

Header | Typ | Beschreibung
--- | ---- | ------------
`x-amz-metadata-directive` | Zeichenfolge (`COPY` oder `REPLACE`) | `REPLACE` überschreibt die ursprünglichen Metadaten mit den neu angegebenen Metadaten.
`x-amz-copy-source-if-match` | Zeichenfolge (`ETag`)| Erstellt eine Kopie, wenn der angegebene `ETag` mit dem Quellenobjekt übereinstimmt.
`x-amz-copy-source-if-none-match` | Zeichenfolge (`ETag`)| Erstellt eine Kopie, wenn sich der angegebene `ETag` vom Quellenobjekt unterscheidet.
`x-amz-copy-source-if-unmodified-since` | Zeichenfolge (Zeitmarke)| Erstellt eine Kopie, wenn das Quellenobjekt seit dem angegebenen Datum nicht mehr geändert wurde. Als Datum muss ein gültiges HTTP-Datum (z. B. `Wed, 30 Nov 2016 20:21:38 GMT`) angegeben werden.
`x-amz-copy-source-if-modified-since` | Zeichenfolge (Zeitmarke)| Erstellt eine Kopie, wenn das Quellenobjekt seit dem angegebenen Datum geändert wurde. Als Datum muss ein gültiges HTTP-Datum (z. B. `Wed, 30 Nov 2016 20:21:38 GMT`) angegeben werden.

**Beispielanforderung**

Dieses Basisbeispiel verwendet das Objekt `bee` aus dem Bucket `garden` und erstellt im Bucket `apiary` eine Kopie mit dem neuen Schlüssel `wild-bee`.

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC-Header)**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 19:52:52 GMT
X-Clv-Request-Id: 72992a90-8f86-433f-b1a4-7b1b33714bed
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 72992a90-8f86-433f-b1a4-7b1b33714bed
ETag: "853aab195ce770b0dfb294a4e9467e62"
Content-Type: application/xml
Content-Length: 240
```

```xml
<CopyObjectResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <LastModified>2016-11-30T19:52:53.125Z</LastModified>
  <ETag>"853aab195ce770b0dfb294a4e9467e62"</ETag>
</CopyObjectResult>
```

----

## CORS-Konfiguration eines Objekts überprüfen
{: #object-operations-options}

Mit einer `OPTIONS`-Anforderung, in der ein Pfad zu einem Objekt zusammen mit einer Ursprungsangabe und dem Anforderungstyp angegeben ist, wird überprüft, ob auf dieses Objekt mit diesem Anforderungstyp über den betreffenden Ursprung zugegriffen werden kann. Anders als bei allen anderen Anforderungen ist für eine OPTIONS-Anforderung der Header `authorization` oder `x-amx-date` nicht erforderlich.

**Syntax**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC-Header)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 07 Dec 2016 16:23:14 GMT
X-Clv-Request-Id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: PUT
Access-Control-Allow-Credentials: true
Vary: Origin, Access-Control-Request-Headers, Access-Control-Allow-Methods
Content-Length: 0

```

----

## Objekte in mehreren Teilen hochladen
{: #object-operations-multipart}

Beim Arbeiten mit größeren Objekten werden Operationen mit mehrteiligen Uploads empfohlen, um Objekte in {{site.data.keyword.cos_full}} zu schreiben. Der Upload eines einzelnen Objekts kann als Gruppe einzelner Teile ausgeführt werden. Diese Teile können unabhängig voneinander in beliebiger Reihenfolge und auch parallel hochgeladen werden. Nach Abschluss des Uploads stellt {{site.data.keyword.cos_short}} alle Teile als ein einziges Objekt dar. Diese Vorgehensweise hat zahlreiche Vorteile: Es kommt nicht zu Fehlern bei umfangreichen Uploads durch Netzunterbrechungen, die Uploads können angehalten und später neu gestartet werden und die Objekte können zeitnah zur Erstellung hochgeladen werden.

Mehrteilige Uploads sind nur für Objekte mit einer Größe von mehr als 5 MB verfügbar. Bei Objekten, die kleiner als 50 GB sind, wird eine Größe der einzelnen Teile zwischen 20 MB und 100 MB empfohlen, um eine optimale Leistung zu erzielen. Bei größeren Objekten kann die Größe der Teile ohne wesentliche Leistungseinbußen erhöht werden. Mehrteilige Uploads sind auf maximal 10.000 Teile von jeweils 5 GB beschränkt.

Die Verwendung von mehr als 500 Teilen führt zu Ineffizienzen in {{site.data.keyword.cos_short}} und sollte möglichst vermieden werden.
{:tip}

Aufgrund der erhöhten Komplexität sollten Entwickler eine Bibliothek verwenden, die Unterstützung für mehrteilige Uploads bietet.

Unvollständige mehrteilige Uploads bleiben erhalten, bis das Objekt gelöscht wird oder bis der mehrteilige Upload mit `AbortIncompleteMultipartUpload` abgebrochen wird. Wird ein unvollständiger mehrteiliger Upload nicht abgebrochen, belegt der nur teilweise durchgeführte Upload weiterhin Ressourcen. Schnittstellen sollten unter Berücksichtigung dieses Aspekts entworfen werden, sodass unvollständige mehrteilige Uploads bereinigt werden.
{:tip}

Der Upload eines Objekts in mehreren Teilen umfasst drei Phasen:

1. Der Upload wird gestartet und das System erstellt eine `UploadId`.
2. Einzelne Teile werden unter Angabe ihrer sequenziellen Teilenummer und der `UploadId` des Objekts hochgeladen.
3. Nachdem alle Teile hochgeladen wurden, wird der Upload abgeschlossen, indem eine Anforderung mit der `UploadId` und einem XML-Block gesendet wird, der eine Auflistung aller Teilenummern und des entsprechenden Werts für `Etag` enthält.

## Mehrteiligen Upload starten
{: #object-operations-multipart-initiate}

Eine `POST`-Anforderung, die für ein Objekt mit dem Abfrageparameter `upload` abgesetzt wird, erstellt einen neuen Wert für `UploadId`, auf den dann von jedem Teil des hochzuladenden Objekts verwiesen wird.

**Hinweis**: Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben identifiziert werden kann.
{:tip}

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # Pfadstil
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC-Header)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```

----

## Teil hochladen
{: #object-operations-multipart-put-part}

Eine `PUT`-Anforderung, die für ein Objekt mit den Abfrageparametern `partNumber` und `uploadId` abgesetzt wird, dient zum Hochladen eines Teils eines Objekts. Die Teile können nacheinander oder parallel hochgeladen werden, müssen jedoch in der richtigen Reihenfolge nummeriert sein.

**Hinweis**: Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben identifiziert werden kann.
{:tip}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # Pfadstil
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Beispielanforderung (HMAC-Header)**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```

----

## Teile auflisten
{: #object-operations-multipart-list}

Eine `GET`-Anforderung, in der ein Pfad zu einem mehrteiligen Objekt mit einer aktiven `UploadID` angegeben ist, die als Abfrageparameter definiert ist, gibt eine Liste aller Teile des Objekts zurück.


**Syntax**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # Pfadstil
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # Stil des virtuellen Hosts
```

### Abfrageparameter
{: #object-operations-multipart-list-params}
Parameter | Erforderlich? | Typ | Beschreibung
--- | ---- | ------------
`uploadId` | Erforderlich | Zeichenfolge | Die beim Initialisieren eines mehrteiligen Uploads zurückgegebene Upload-ID.
`max-parts` | Optional | Zeichenfolge | Der Standardwert ist 1.000.
`part-number​-marker` | Optional | Zeichenfolge | Definiert, wo die Liste der Teile beginnt.

**Beispielanforderung**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC-Header)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Mon, 19 Mar 2018 17:21:08 GMT
X-Clv-Request-Id: 6544044d-4f88-4bb6-9ee5-bfadf5023249
Server: Cleversafe/3.12.4.20
X-Clv-S3-Version: 2.5
Accept-Ranges: bytes
Content-Type: application/xml
Content-Length: 743
```

```xml
<ListPartsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>farm</Bucket>
  <Key>spaceship</Key>
  <UploadId>01000162-3f46-6ab8-4b5f-f7060b310f37</UploadId>
  <Initiator>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Initiator>
  <Owner>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Owner>
  <StorageClass>STANDARD</StorageClass>
  <MaxParts>1000</MaxParts>
  <IsTruncated>false</IsTruncated>
  <Part>
    <PartNumber>1</PartNumber>
    <LastModified>2018-03-19T17:20:35.482Z</LastModified>
    <ETag>"bb03cf4fa8603fe407a65ee1dba55265"</ETag>
    <Size>7128094</Size>
  </Part>
</ListPartsResult>
```

----

## Mehrteiligen Upload abschließen
{: #object-operations-multipart-complete}

Eine `POST`-Anforderung, die für ein Objekt mit dem Abfrageparameter `uploadId` und dem entsprechenden XML-Block im Hauptteil abgesetzt wird, dient zur Ausführung eines mehrteiligen Uploads.

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # Pfadstil
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # Stil des virtuellen Hosts
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Beispielanforderung**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Beispielanforderung (HMAC-Header)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

## Unvollständige mehrteilige Uploads abbrechen
{: #object-operations-multipart-uploads}

Eine `DELETE`-Anforderung, die für ein Objekt mit dem Abfrageparameter `uploadId` abgesetzt wird, dient zur Löschung aller nicht abgeschlossenen Teile eines mehrteiligen Uploads.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # Pfadstil
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC-Header)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Archiviertes Objekt vorübergehend wiederherstellen
{: #object-operations-archive-restore}

Eine `POST`-Anforderung, die für ein Objekt mit dem Abfrageparameter `restore` abgesetzt wird, dient zur Anforderung einer vorübergehenden Wiederherstellung eines archivierten Objekts. Zur Integritätsprüfung für die Nutzdaten ist ein Header `Content-MD5` erforderlich.

Ein archiviertes Objekt muss wiederhergestellt werden, bevor es heruntergeladen oder geändert wird. Die Lebensdauer des Objekts muss angegeben werden. Nach Ablauf dieser Lebensdauer wird die temporäre Kopie des Objekts gelöscht.

Es kann zu einer Verzögerung von bis zu 15 Stunden kommen, bevor auf die wiederhergestellte Kopie zugegriffen werden kann. Eine HEAD-Anforderung kann verwendet werden, um zu prüfen, ob die wiederhergestellte Kopie verfügbar ist.

Um das Objekt dauerhaft wiederherzustellen, muss es in ein Bucket kopiert werden, das keine aktive Lebenszykluskonfiguration hat.

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # Pfadstil
POST https://{bucket-name}.{endpoint}/{object-name}?restore # Stil des virtuellen Hosts
```

**Nutzdatenelemente**

Der Hauptteil der Anforderung muss einen XML-Block mit dem folgenden Schema enthalten:

|Element|Typ|Untergeordnete Elemente|Vorfahre|Einschränkung|
|---|---|---|---|---|
|RestoreRequest|Container|Days, GlacierJobParameters|Keiner|Keine|
|Days|Ganzzahl|Keine|RestoreRequest|Gibt die Lebensdauer des vorübergehend wiederhergestellten Objekts an. Die Mindestanzahl von Tagen, die eine wiederhergestellte Kopie des Objekts vorhanden sein kann, ist '1'. Nach Ablauf des Wiederherstellungszeitraums wird die temporäre Kopie des Objekts entfernt.|
|GlacierJobParameters|Zeichenfolge|Tier|RestoreRequest|Keine|
|Tier|Zeichenfolge|Keine|GlacierJobParameters|**Muss** auf `Bulk` gesetzt werden.|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Beispielanforderung**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC-Header)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielanforderung (HMAC - vorsignierte URL)**

```http
POST /apiary/queenbee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&restore&x-amz-signature={signature} HTTP/1.1
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Beispielantwort**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Metadaten aktualisieren
{: #object-operations-metadata}

Die Metadaten eines vorhandenen Objekts können auf die folgenden beiden Arten aktualisiert werden:
* Mit einer `PUT`-Anforderung mit den neuen Metadaten und den Inhalten des ursprünglichen Objekts.
* Durch Ausführung einer `COPY`-Anforderung mit den neuen Metadaten, in der das ursprüngliche Objekt als Kopiequelle angegeben ist.

Alle Metadatenschlüssel müssen mit dem Präfix `x-amz-meta-` versehen werden.
{: tip}

### PUT zum Aktualisieren von Metadaten verwenden
{: #object-operations-metadata-put}

Für die `PUT`-Anforderung ist eine Kopie des vorhandenen Objekts erforderlich, da die Inhalte überschrieben werden. {: important}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
PUT https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

### COPY zum Aktualisieren von Metadaten verwenden
{: #object-operations-metadata-copy}

Weiterführende Details zur Ausführung einer `COPY`-Anforderung erhalten Sie, wenn Sie [hier](#object-operations-copy) klicken.

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
PUT https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```
