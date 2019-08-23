---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, buckets

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

# Bucketoperationen
{: #compatibility-api-bucket-operations}


## Buckets auflisten
{: #compatibility-api-list-buckets}

Eine `GET`-Anforderung, die an den Endpunktroot gesendet wird, gibt eine Liste mit Buckets zurück, die der angegebenen Serviceinstanz zugeordnet sind. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Header                    | Typ    | Erforderlich? |  Beschreibung
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | Zeichenfolge | Ja | Listet Buckets auf, die in dieser Serviceinstanz erstellt wurden.

Abfrageparameter                   | Wert    | Erforderlich? |  Beschreibung
--------------------------|--------|---| -----------------------------------------------------------
`extended` | Keiner | Nein | Stellt Metadaten für `LocationConstraint` in der Liste bereit.

In den SDKs oder der Befehlszeilenschnittstelle wird die erweiterte Liste nicht unterstützt.
{:note}

**Syntax**

```bash
GET https://{endpoint}/
```

**Beispielanforderung**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Beispielantwort**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

### Erweiterte Liste abrufen
{: #compatibility-api-list-buckets-extended}

**Syntax**

```bash
GET https://{endpoint}/?extended
```

**Beispielanforderung**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Beispielantwort**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <IsTruncated>false</IsTruncated>
    <MaxKeys>1000</MaxKeys>
    <Prefix/>
    <Marker/>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
            <LocationConstraint>us-south-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
            <LocationConstraint>seo01-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
            <LocationConstraint>eu-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
            <LocationConstraint>us-cold</LocationConstraint>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

## Bucket erstellen
{: #compatibility-api-new-bucket}

Eine `PUT`-Anforderung, die mit einer nachfolgenden Zeichenfolge an den Endpunktroot gesendet wird und mit der ein Bucket erstellt wird. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Bucketnamen müssen global eindeutig und DNS-konform sein. Die Namen dürfen eine Länge zwischen 3 und 63 Zeichen haben und dürfen Kleinbuchstaben, Zahlen und Gedankenstriche enthalten. Bucketnamen müssen mit einem Kleinbuchstaben oder einer Zahl beginnen und enden. Bucketnamen, die Ähnlichkeiten mit IP-Adressen aufweisen, sind nicht zulässig. Bei dieser Operation werden keine operationsspezifischen Abfrageparameter verwendet.

Bucketnamen müssen eindeutig sein, weil alle Buckets in der öffentlichen Cloud einen gemeinsamen Namensbereich verwenden. Auf diese Weise kann auf ein Bucket zugegriffen werden, ohne dass Sie hierzu eine Serviceinstanz oder die Kontoinformationen angeben müssen. Des Weiteren ist es nicht möglich, ein Bucket mit einem Namen zu erstellen, der mit der Zeichenfolge `cosv1-` oder `account-` beginnt, da diese Präfixe für das System reserviert sind.
{:important}

Header                    | Typ    | Beschreibung
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | Zeichenfolge |  Dieser Header verweist auf die Serviceinstanz, in der das Bucket erstellt wird und für die die Datennutzung in Rechnung gestellt wird.

**Hinweis**: Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben im Namen des Buckets oder Objekts identifiziert werden kann.
{:tip}

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # Pfadstil
PUT https://{bucket-name}.{endpoint} # Stil des virtuellen Hosts
```

**Beispielanforderung**

Dies ist ein Beispiel für die Erstellung eines neuen Buckets mit dem Namen 'images'.

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

----

## Bucket mit anderer Speicherklasse erstellen
{: #compatibility-api-storage-class}

Um ein Bucket mit einer anderen Speicherklasse zu erstellen, müssen Sie einen XML-Block senden, in dem eine Bucketkonfiguration mit der Angabe `{provisioning code}` für `LocationConstraint` im Hauptteil einer `PUT`-Anforderung an einen Bucketendpunkt angegeben ist. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Beachten Sie hierbei, dass die standardmäßigen [Namenskonventionen](#compatibility-api-new-bucket) für Buckets gelten. Bei dieser Operation werden keine operationsspezifischen Abfrageparameter verwendet.

Header                    | Typ    | Beschreibung
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | Zeichenfolge |  Dieser Header verweist auf die Serviceinstanz, in der das Bucket erstellt wird und für die die Datennutzung in Rechnung gestellt wird.

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # Pfadstil
PUT https://{bucket-name}.{endpoint} # Stil des virtuellen Hosts
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

Eine Liste gültiger Bereitstellungscodes für `LocationConstraint` finden Sie im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

**Beispielanforderung**

Dies ist ein Beispiel für die Erstellung eines neuen Buckets mit dem Namen 'vault-images'.

```http
PUT /vault-images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
Content-Length: 110
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Fri, 17 Mar 2017 17:52:17 GMT
X-Clv-Request-Id: b6483b2c-24ae-488a-884c-db1a93b9a9a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
Content-Length: 0
```

----

## Neues Bucket mit von Key Protect verwalteten Verschlüsselungsschlüsseln erstellen (SSE-KP)
{: #compatibility-api-key-protect}

Um ein Bucket zu erstellen, dessen Verschlüsselungsschlüssel mit Key Protect verwaltet werden, benötigen Sie Zugriff auf eine aktive Key Protect-Serviceinstanz, die sich an demselben Standort befindet wie das neue Bucket. Bei dieser Operation werden keine operationsspezifischen Abfrageparameter verwendet.

Weitere Informationen zur Verwendung von Key Protect für die Verwaltung Ihrer Verschlüsselungsschlüssel finden Sie in der zugehörigen [Dokumentation](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

Beachten Sie hierbei, dass Key Protect in einer Konfiguration vom Typ 'Regionsübergreifend' **nicht** verfügbar ist und dass für alle SSE-KP-Buckets der Typ 'Regional' verwendet werden muss.
{:tip}

Header                    | Typ    | Beschreibung
------------------------------------------------- | ------ | ----
`ibm-service-instance-id` | Zeichenfolge |  Dieser Header verweist auf die Serviceinstanz, in der das Bucket erstellt wird und für die die Datennutzung in Rechnung gestellt wird.
`ibm-sse-kp-encryption-algorithm` | Zeichenfolge | Dieser Header wird verwendet, um den Algorithmus und die Schlüsselgröße anzugeben, die mit dem Verschlüsselungsschlüssel verwendet werden sollen, der unter Verwendung von Key Protect gespeichert wird. Dieser Wert muss auf die Zeichenfolge `AES256` gesetzt werden.
`ibm-sse-kp-customer-root-key-crn`  | Zeichenfolge | Dieser Header wird verwendet, um auf den Rootschlüssel zu verweisen, der von Key Protect verwendet wird, um dieses Bucket zu verschlüsseln. Dieser Wert muss den vollständigen CRN des Rootschlüssels enthalten. 

**Syntax**

```shell
PUT https://{endpoint}/{bucket-name} # Pfadstil
PUT https://{bucket-name}.{endpoint} # Stil des virtuellen Hosts
```

**Beispielanforderung**

Dies ist ein Beispiel für die Erstellung eines neuen Buckets mit dem Namen 'secure-files'.

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

---

## Header eines Buckets abrufen
{: #compatibility-api-head-bucket}

Eine `HEAD`-Anforderung, die für ein Bucket abgesetzt wird, gibt die Header für dieses Bucket zurück.

`HEAD`-Anforderungen geben keinen Hauptteil zurück und können deshalb auch keine spezifischen Fehlernachrichten wie beispielsweise `NoSuchBucket` zurückgeben, sondern lediglich die Nachricht `NotFound`.
{:tip}

**Syntax**

```bash
HEAD https://{endpoint}/{bucket-name} # Pfadstil
HEAD https://{bucket-name}.{endpoint} # Stil des virtuellen Hosts
```

**Beispielanforderung**

Dies ist ein Beispiel für das Abrufen der Header für das Bucket 'images'.

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
```

**Beispielanforderung**

`HEAD`-Anforderungen für Buckets mit Key Protect-Verschlüsselung geben zusätzliche Header zurück.

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
ibm-sse-kp-enabled: True
ibm-see-kp-crk-id: {customer-root-key-id}
```

----

## Objekte in einem bestimmten Bucket auflisten (Version 2)
{: #compatibility-api-list-objects-v2}

Eine `GET`-Anforderung an ein Bucket gibt eine Liste mit Objekten zurück. Die Anzahl ist auf 1.000 gleichzeitig zurückgegebene Objekte beschränkt, wobei die Objekte in nicht lexikalischer Reihenfolge zurückgegeben werden. Der Wert für `StorageClass`, der in der Antwort zurückgegeben wird, ist ein Standardwert, da Speicherklassenoperationen nicht in COS implementiert sind. Diese Operation verwendet keine operationsspezifischen Header oder Nutzdatenelemente.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # Pfadstil
GET https://{bucket-name}.{endpoint}?list-type=2 # Stil des virtuellen Hosts
```

### Optionale Abfrageparameter
{: #compatibility-api-list-objects-v2-params}
Name | Typ | Beschreibung
--- | ---- | ------------
`list-type` | Zeichenfolge | Gibt Version 2 der API an. Der Wert muss '2' lauten.
`prefix` | Zeichenfolge | Beschränkt die Antwort auf Objektnamen, die mit `prefix` beginnen.
`delimiter` | Zeichenfolge | Gruppiert Objekte zwischen `prefix` und `delimiter`.
`encoding-type` | Zeichenfolge | Wenn Unicode-Zeichen, die von XML nicht unterstützt werden, in einem Objektnamen verwendet werden, kann für diesen Parameter `url` angegeben werden, um die Antwort ordnungsgemäß zu codieren.
`max-keys` | Zeichenfolge | Beschränkt die Anzahl der Objekte, die in der Antwort angezeigt werden sollen. Der Standardwert und das Maximum beträgt 1.000.
`fetch-owner` | Zeichenfolge | Version 2 der API enthält standardmäßig keine Informationen zu `Owner`. Legen Sie für diesen Parameter die Einstellung `true` fest, wenn in der Antwort Informationen zu `Owner` enthalten sein sollen.
`continuation-token` | Zeichenfolge | Gibt die nächste Gruppe von Objekten an, die zurückgegeben werden sollen, wenn Ihre Antwort abgeschnitten wird (Element `IsTruncated` gibt `true` zurück).<br/><br/>Ihre erste Antwort enthält das Element `NextContinuationToken`. Verwenden Sie dieses Token in der nächsten Anforderung als Wert für `continuation-token`.
`start-after` | Zeichenfolge | Gibt Schlüsselnamen nach einem bestimmten Schlüsselobjekt zurück.<br/><br/>*Dieser Parameter ist nur in der ersten Anforderung gültig.* Wenn Ihre Anforderung den Parameter `continuation-token` enthält, dann wird dieser Parameter ignoriert.

**Beispielanforderung (einfach mit IAM)**

Diese Anforderung listet die Objekte im Bucket 'apiary' auf.

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Beispielantwort (einfach)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 814
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <KeyCount>3</KeyCount>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Beispielanforderung (Parameter 'max-keys')**

Diese Anforderung listet die Objekte im Bucket 'apiary' auf, wobei die maximale Anzahl zurückgegebener Schlüssel auf '1' gesetzt ist.

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Beispielantwort (abgeschnittene Antwort)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 598
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <NextContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Beispielanforderung (Parameter 'continuation-token')**

Diese Anforderung listet die Objekte im Bucket 'apiary' auf, wobei ein Fortsetzungstoken angegeben ist.

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Beispielantwort (abgeschnittene Antwort, Parameter 'continuation-token')**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 604
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <ContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</ContinuationToken>
  <NextContinuationToken>1a8j20CqowRrM4epIQ7fTBuyPZWZUeA8Epog16wYu9KhAPNoYkWQYhGURsIQbll1lP7c-OO-V5Vyzu6mogiakC4NSwlK4LyRDdHQgY-yPH4wMB76MfQR61VyxI4TJLxIWTPSZA0nmQQWcuV2mE4jiDA</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

### Objekte in einem bestimmten Bucket auflisten (veraltet)
{: #compatibility-api-list-objects}

**Hinweis:** *Diese API dient zur Sicherstellung der Abwärtskompatibilität.* Informationen zur empfohlenen Methode beim Abrufen von Objekten in einem Bucket finden Sie unter [Version 2](#compatibility-api-list-objects-v2).

Eine `GET`-Anforderung an ein Bucket gibt eine Liste mit Objekten zurück. Die Anzahl ist auf 1.000 gleichzeitig zurückgegebene Objekte beschränkt, wobei die Objekte in nicht lexikalischer Reihenfolge zurückgegeben werden. Der Wert für `StorageClass`, der in der Antwort zurückgegeben wird, ist ein Standardwert, da Speicherklassenoperationen nicht in COS implementiert sind. Diese Operation verwendet keine operationsspezifischen Header oder Nutzdatenelemente.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name} # Pfadstil
GET https://{bucket-name}.{endpoint} # Stil des virtuellen Hosts
```

### Optionale Abfrageparameter
{: #compatibility-api-list-objects-params}

Name | Typ | Beschreibung
--- | ---- | ------------
`prefix` | Zeichenfolge | Beschränkt die Antwort auf Objektnamen, die mit `prefix` beginnen.
`delimiter` | Zeichenfolge | Gruppiert Objekte zwischen `prefix` und `delimiter`.
`encoding-type` | Zeichenfolge | Wenn Unicode-Zeichen, die von XML nicht unterstützt werden, in einem Objektnamen verwendet werden, kann für diesen Parameter `url` angegeben werden, um die Antwort ordnungsgemäß zu codieren.
`max-keys` | Zeichenfolge | Beschränkt die Anzahl der Objekte, die in der Antwort angezeigt werden sollen. Der Standardwert und das Maximum beträgt 1.000.
`marker` | Zeichenfolge | Gibt das Objekt, bei dem die Auflistung beginnen soll, in UTF-8-Binärreihenfolge an.

**Beispielanforderung**

Diese Anforderung listet die Objekte im Bucket 'apiary' auf.

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 909
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <Marker/>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

## Bucket löschen

Mit einer `DELETE`-Anforderung, die an ein leeres Bucket abgesetzt wird, kann das Bucket gelöscht werden. Nach dem Löschen eines Buckets wird der Name vom System für zehn Minuten reserviert und dann freigegeben, damit er wiederverwendet werden kann. *Nur leere Buckets können gelöscht werden.*

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name} # Pfadstil
DELETE https://{bucket-name}.{endpoint} # Stil des virtuellen Hosts
```

### Optionale Header

Name | Typ | Beschreibung
--- | ---- | ------------
`aspera-ak-max-tries` | Zeichenfolge | Gibt an, wie oft versucht wird, die Löschoperation auszuführen. Der Standardwert lautet '2'.


**Beispielanforderung**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

Der Server antwortet mit `204 Kein Inhalt`.

Wenn für ein Bucket, das nicht leer ist, eine Löschung angefordert wird, dann antwortet der Server mit `409 Konflikt`.

**Beispielantwort**

```xml
<Error>
  <Code>BucketNotEmpty</Code>
  <Message>The bucket you tried to delete is not empty.</Message>
  <Resource>/apiary/</Resource>
  <RequestId>9d2bbc00-2827-4210-b40a-8107863f4386</RequestId>
  <httpStatusCode>409</httpStatusCode>
</Error>
```

----

## Abgebrochene und unvollständige mehrteilige Uploads für ein Bucket auflisten

Eine `GET`-Anforderung, die für ein Bucket mit den korrekten Parametern abgesetzt wird, ruft Informationen zu allen abgebrochenen oder unvollständigen mehrteiligen Uploads für ein Bucket ab.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # Pfadstil
GET https://{bucket-name}.{endpoint}?uploads= # Stil des virtuellen Hosts
```

**Parameter**

Name | Typ | Beschreibung
--- | ---- | ------------
`prefix` | Zeichenfolge | Beschränkt die Antwort auf Objektnamen, die mit `{prefix}` beginnen.
`delimiter` | Zeichenfolge | Gruppiert Objekte zwischen `prefix` und `delimiter`.
`encoding-type` | Zeichenfolge | Wenn Unicode-Zeichen, die von XML nicht unterstützt werden, in einem Objektnamen verwendet werden, kann für diesen Parameter `url` angegeben werden, um die Antwort ordnungsgemäß zu codieren.
`max-uploads` | Ganzzahl | Beschränkt die Anzahl der Objekte, die in der Antwort angezeigt werden sollen. Der Standardwert und das Maximum beträgt 1.000.
`key-marker` | Zeichenfolge | Gibt an, wo die Liste beginnen soll.
`upload-id-marker` | Zeichenfolge | Wird ignoriert, wenn `key-marker` nicht angegeben wird. Andernfalls wird ein Punkt festgelegt, bei dem die Auflistung von Teilen über `upload-id-marker` beginnen soll.

**Beispielanforderung**

Dies ist ein Beispiel für das Abrufen aller aktuellen abgebrochenen und unvollständigen mehrteiligen Uploads.

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort** (keine mehrteiligen Uploads in Bearbeitung)

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:22:27 GMT
X-Clv-Request-Id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Content-Type: application/xml
Content-Length: 374
```

```xml
<ListMultipartUploadsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>apiary</Bucket>
  <KeyMarker/>
  <UploadIdMarker/>
  <NextKeyMarker>multipart-object-123</NextKeyMarker>
  <NextUploadIdMarker>0000015a-df89-51d0-2790-dee1ac994053</NextUploadIdMarker>
  <MaxUploads>1000</MaxUploads>
  <IsTruncated>false</IsTruncated>
  <Upload>
    <Key>file</Key>
    <UploadId>0000015a-d92a-bc4a-c312-8c1c2a0e89db</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-16T22:09:01.002Z</Initiated>
  </Upload>
  <Upload>
    <Key>multipart-object-123</Key>
    <UploadId>0000015a-df89-51d0-2790-dee1ac994053</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-18T03:50:02.960Z</Initiated>
  </Upload>
</ListMultipartUploadsResult>
```

----

## CORS-Konfiguration für Bucket auflisten

Eine `GET`-Anforderung, die für ein Bucket mit den korrekten Parametern abgesetzt wird, ruft Informationen zur CORS-Konfiguration (CORS = Cross-Origin Resource Sharing) für ein Bucket ab.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?cors= # Pfadstil
GET https://{bucket-name}.{endpoint}?cors= # Stil des virtuellen Hosts
```

**Beispielanforderung**

Dies ist ein Beispiel für die Auflistung einer CORS-Konfiguration für das Bucket 'apiary'.

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort** 

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:20:30 GMT
X-Clv-Request-Id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Content-Type: application/xml
Content-Length: 123
```

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <CORSRule>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
  </CORSRule>
</CORSConfiguration>
```

----

## CORS-Konfiguration für Bucket erstellen

Eine `PUT`-Anforderung, die für ein Bucket mit den korrekten Parametern abgesetzt wird, erstellt oder ersetzt eine CORS-Konfiguration (CORS = Cross-Origin Resource Sharing) für ein Bucket.

Der erforderliche Header `Content-MD5` muss die binäre Darstellung eines Base64-codierten MD5-Hashwerts sein.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # Pfadstil
PUT https://{bucket-name}.{endpoint}?cors= # Stil des virtuellen Hosts
```

**Beispielanforderung**

Dies ist ein Beispiel für das Hinzufügen einer CORS-Konfiguration, die es Anforderungen von `www.ibm.com` erlaubt, `GET`-, `PUT`- und `POST`Anforderungen an das Bucket abzusetzen.

```http
PUT /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 237
```

```xml
<CORSConfiguration>
  <CORSRule>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
  </CORSRule>
</CORSConfiguration>
```


**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```

----

## CORS-Konfiguration für Bucket löschen

Eine `DELETE`-Anforderung, die für ein Bucket mit den korrekten Parametern abgesetzt wird, erstellt oder ersetzt eine CORS-Konfiguration (CORS = Cross-Origin Resource Sharing) für ein Bucket.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # Pfadstil
DELETE https://{bucket-name}.{endpoint}?cors= # Stil des virtuellen Hosts
```

**Beispielanforderung**

Dies ist ein Beispiel für das Löschen einer CORS-Konfiguration für einen Bucket.

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

Der Server antwortet mit `204 Kein Inhalt`.

----

## Standortbedingung für ein Bucket auflisten

Eine `GET`-Anforderung, die für ein Bucket mit dem korrekten Parameter abgesetzt wird, ruft Informationen zum Standort für ein Bucket ab.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?location # Pfadstil
GET https://{bucket-name}.{endpoint}?location # Stil des virtuellen Hosts
```

**Beispielanforderung**

Dies ist ein Beispiel für das Abrufen des Standorts des Buckets 'apiary'.

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Beispielantwort**

```http
HTTP/1.1 200 OK
Date: Tue, 12 Jun 2018 21:10:57 GMT
X-Clv-Request-Id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Content-Type: application/xml
Content-Length: 161
```

```xml
<LocationConstraint xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  us-south-standard
</LocationConstraint>
```

----

## Konfiguration für Bucketlebenszyklus erstellen
{: #compatibility-api-create-bucket-lifecycle}

Bei einer `PUT`-Operation wird der Parameter für die Lebenszyklusabfrage verwendet, um Lebenszykluseinstellungen für das Bucket festzulegen. Zur Integritätsprüfung für die Nutzdaten ist ein Header `Content-MD5` erforderlich.

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # Pfadstil
PUT https://{bucket-name}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```

**Nutzdatenelemente**

Der Hauptteil der Anforderung muss einen XML-Block mit dem folgenden Schema enthalten:

|Element|Typ|Untergeordnete Elemente|Vorfahre|Einschränkung|
|---|---|---|---|---|
|LifecycleConfiguration|Container|Rule|Keiner|Grenzwert 1|
|Rule|Container|ID, Status, Filter, Transition|LifecycleConfiguration|Grenzwert 1|
|ID|Zeichenfolge|Keine|Rule|**Muss** aus den Zeichen ``(a-z, A-Z, 0-9)`` und den folgenden Symbolen bestehen: ``!`_ .*'()- ``.|
|Filter|Zeichenfolge|Prefix|Rule|**Muss** ein Element `Prefix` enthalten.|
|Prefix|Zeichenfolge|Keine|Filter|**Muss** auf `<Prefix/>` gesetzt werden.|
|Transition|Container|Days, StorageClass|Rule|Grenzwert 1.|
|Days|Nicht negative Ganzzahl|Keine|Transition|**Muss** ein Wert größer als '0' sein.|
|Date|Datum|Keine|Transition|**Muss** im ISO 8601-Format angegeben werden. Das Datum muss in der Zukunft liegen.|
|StorageClass|Zeichenfolge|Keine|Transition|**Muss** auf GLACIER gesetzt sein.|

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>{string}</ID>
        <Status>Enabled</Status>
        <Filter>
            <Prefix/>
        </Filter>
        <Transition>
            <Days>{integer}</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

**Beispielanforderung**

```http
PUT /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ== 
Content-Length: 305
```

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

Der Server antwortet mit `200 OK`.

----

## Konfiguration für Bucketlebenszyklus abrufen

Bei einer `GET`-Operation wird der Abfrageparameter für den Lebenszyklus verwendet, um Lebenszykluseinstellungen für das Bucket abzurufen.

**Syntax**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # Pfadstil
GET https://{bucket-name}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**Beispielantwort**

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

----

## Lebenszykluskonfiguration für Bucket löschen

Eine `DELETE`-Anforderung, die für ein Bucket mit den korrekten Parametern abgesetzt wird, entfernt alle Lebenszykluskonfigurationen für ein Bucket.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # Pfadstil
DELETE https://{bucket-name}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```

**Beispielanforderung**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

Der Server antwortet mit `204 Kein Inhalt`.
