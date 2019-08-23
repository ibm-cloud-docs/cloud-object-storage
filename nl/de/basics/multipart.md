---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: big data, multipart, multiple parts, transfer

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
{:S3cmd: .ph data-hd-programlang='S3cmd'}

# Große Objekte speichern
{: #large-objects}

{{site.data.keyword.cos_full}} kann einzelne Objekte mit einer Größe bis zu 10 TB unterstützen, wenn mehrteilige Uploads verwendet werden. Große Objekte können auch über die [Konsole mit aktivierter Aspera-Hochgeschwindigkeitsübertragung](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera) hochgeladen werden. In den meisten Szenarios führt der Einsatz der Aspera-Hochgeschwindigkeitsübertragung zu einer deutlich verbesserten Leistung bei der Datenübertragung. Dies gilt insbesondere bei großen Distanzen oder bei instabilen Netzbedingungen.

## Objekte in mehreren Teilen hochladen
{: #large-objects-multipart}

Operationen für mehrteilige Uploads werden empfohlen, um größere Objekte in {{site.data.keyword.cos_short}} zu schreiben. Der Upload eines einzelnen Objekts wird als Gruppe einzelner Teile ausgeführt. Diese Teile können unabhängig voneinander in beliebiger Reihenfolge und auch parallel hochgeladen werden. Nach Abschluss des Uploads stellt {{site.data.keyword.cos_short}} alle Teile als ein einziges Objekt dar. Diese Vorgehensweise hat zahlreiche Vorteile: Es kommt nicht zu Fehlern bei umfangreichen Uploads durch Netzunterbrechungen, die Uploads können angehalten und später neu gestartet werden und die Objekte können zeitnah zur Erstellung hochgeladen werden.

Mehrteilige Uploads sind nur für Objekte mit einer Größe von mehr als 5 MB verfügbar. Bei Objekten, die kleiner als 50 GB sind, wird eine Größe der einzelnen Teile zwischen 20 MB und 100 MB empfohlen, um eine optimale Leistung zu erzielen. Bei größeren Objekten kann die Größe der Teile ohne wesentliche Leistungseinbußen erhöht werden. Mehrteilige Uploads sind auf maximal 10.000 Teile von jeweils 5 GB beschränkt. Die Objektgröße darf maximal 10 TB betragen.


Aufgrund der komplexen Verwaltung und Optimierung paralleler Uploads verwenden Entwickler häufig Bibliotheken, die Unterstützung für mehrteilige Uploads bieten.

Die meisten Tools wie beispielsweise Befehlszeilenschnittstellen oder IBM Cloud Console sowie die meisten kompatiblen Bibliotheken und SDKs übertragen Objekte automatisch mit mehrteiligen Uploads.

## REST-API oder SDKs verwenden
{: #large-objects-multipart-api} 

Unvollständige mehrteilige Uploads bleiben erhalten, bis das Objekt gelöscht wird oder bis der mehrteilige Upload abgebrochen wird. Wird ein unvollständiger mehrteiliger Upload nicht abgebrochen, belegt der nur teilweise durchgeführte Upload weiterhin Ressourcen. Schnittstellen sollten unter Berücksichtigung dieses Aspekts entworfen werden, sodass unvollständige mehrteilige Uploads bereinigt werden.
{:tip}

Der Upload eines Objekts in mehreren Teilen umfasst drei Phasen:

1. Der Upload wird gestartet und das System erstellt eine `UploadId`.
2. Einzelne Teile werden unter Angabe ihrer sequenziellen Teilenummer und der `UploadId` des Objekts hochgeladen.
3. Nachdem alle Teile hochgeladen wurden, wird der Upload abgeschlossen, indem eine Anforderung mit der `UploadId` und einem XML-Block gesendet wird, der eine Auflistung aller Teilenummern und des entsprechenden Werts für `Etag` enthält.

Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
{:tip}

### Mehrteiligen Upload starten
{: #large-objects-multipart-api-initiate} 
{: http}

Eine `POST`-Anforderung, die für ein Objekt mit dem Abfrageparameter `upload` abgesetzt wird, erstellt einen neuen Wert für `UploadId`, auf den dann von jedem Teil des hochzuladenden Objekts verwiesen wird.
{: http}

**Syntax**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # Pfadstil
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # Stil des virtuellen Hosts
```
{: codeblock}
{: http}

**Beispielanforderung**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Beispielantwort**
{: http}

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
{: codeblock}
{: http}

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```
{: codeblock}
{: http}

----

### Teil hochladen
{: #large-objects-multipart-api-upload-part} 
{: http}

Eine `PUT`-Anforderung, die für ein Objekt mit den Abfrageparametern `partNumber` und `uploadId` abgesetzt wird, dient zum Hochladen eines Teils eines Objekts. Die Teile können nacheinander oder parallel hochgeladen werden, müssen jedoch in der richtigen Reihenfolge nummeriert sein.
{: http}

**Syntax**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # Pfadstil
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # Stil des virtuellen Hosts
```
{: codeblock}
{: http}

**Beispielanforderung**
{: http}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: codeblock}
{: http}

**Beispielantwort**
{: http}

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
{: codeblock}
{: http}

### Mehrteiligen Upload abschließen
{: #large-objects-multipart-api-complete} 
{: http}

Eine `POST`-Anforderung, die für ein Objekt mit dem Abfrageparameter `uploadId` und dem entsprechenden XML-Block im Hauptteil abgesetzt wird, dient zur Ausführung eines mehrteiligen Uploads.
{: http}

**Syntax**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # Pfadstil
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # Stil des virtuellen Hosts
```
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Beispielanforderung**
{: http}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: http}

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
{: codeblock}
{: http}

**Beispielantwort**
{: http}

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
{: codeblock}
{: http}

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```
{: codeblock}
{: http}


### Unvollständige mehrteilige Uploads abbrechen
{: #large-objects-multipart-api-abort} 
{: http}

Eine `DELETE`-Anforderung, die für ein Objekt mit dem Abfrageparameter `uploadId` abgesetzt wird, dient zur Löschung aller nicht abgeschlossenen Teile eines mehrteiligen Uploads.
{: http}
**Syntax**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # Pfadstil
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # Stil des virtuellen Hosts
```
{: codeblock}
{: http}

**Beispielanforderung**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Beispielantwort**
{: http}

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}
{: http}

### S3cmd (CLI) verwenden
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){:new_window} ist ein kostenloses Linux- und Mac-Befehlszeilentool und ein Client zum Hochladen, Abrufen und Verwalten von Daten für Service-Provider von Cloudspeicher, die mit dem S3-Protokoll arbeiten. Das Tool wurde für professionelle Anwender konzipiert, die mit Befehlszeilenprogrammen vertraut sind. Es eignet sich ideal für Stapelscripts und automatisierte Sicherungen. S3cmd ist in Python geschrieben. Es stellt ein Open-Source-Projekt dar, das unter GNU Public License v2 (GPLv2) bereitgestellt wird und sowohl für kommerzielle als auch private Nutzer kostenlos zur Verfügung gestellt wird.
{: S3cmd}

Zur Verwendung von S3cmd ist Python 2.6 oder höher erforderlich. Das Tool ist mit Python 3 kompatibel. Die einfachste Methode zum Installieren von S3cmd besteht in der Verwendung von Python Package Index (PyPi).
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

Nach der Installation des Pakets können Sie die {{site.data.keyword.cos_full}}-Beispielkonfigurationsdatei  [hier](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window} abrufen und sie mit Ihren Berechtigungsnachweisen für Cloud Object Storage (S3) aktualisieren:
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

Die folgenden vier Zeilen müssen aktualisiert werden:
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
Dies trifft unabhängig davon zu, ob Sie die Beispieldatei oder die Datei verwenden, die durch Ausführung des folgenden Befehls generiert werden kann: `s3cmd --configure`.
{: S3cmd}

Sobald diese Zeilen mit den COS-Details über das Kundenportal aktualisiert wurden, können Sie die Verbindung testen, indem Sie den Befehl `s3cmd ls` absetzen, mit dem alle Buckets eines Kontos aufgelistet werden können.
{: S3cmd}

```
$ s3cmd ls 
2017-02-03 14:52  s3://backuptest
2017-02-06 15:04  s3://coldbackups
2017-02-03 21:23  s3://largebackup
2017-02-07 17:44  s3://winbackup
```
{: codeblock}
{: S3cmd}

Die vollständige Liste der Optionen und Befehle sowie grundlegende Informationen zur Verwendung finden Sie auf der Site für [s3tools](https://s3tools.org/usage){:new_window}.
{: S3cmd}

### Mehrteilige Uploads mit S3cmd
{: #large-objects-s3cmd-upload} 
{: S3cmd}

Der Befehl `put` führt automatisch einen mehrteiligen Upload aus, wenn versucht wird, Dateien hochzuladen, deren Größe den angegebenen Schwellenwert übersteigt.
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

Der Schwellenwert wird anhand der Option `--multipart-chunk-size-mb` ermittelt:
{: S3cmd}

```
--multipart-chunk-size-mb=SIZE
    Size of each chunk of a multipart upload. Dateien, die größer
    als SIZE sind, werden automatisch als mehrteiliger Multithread-
    Upload hochgeladen. Kleinere Dateien werden mit der
    konventionellen Methode hochgeladen. SIZE wird in Megabyte angegeben.
    Die standardmäßige Chunkgröße ist 15 MB. Die minimale Chunkgröße ist
    5 MB, die maximale 5 GB.
```
{: codeblock}
{: S3cmd}

Beispiel:
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

Ausgabe:
{: S3cmd}

```
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 1 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  1731.92 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 2 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2001.14 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 3 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2000.28 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 4 of 4, 4MB] [1 of 1]
 4973645 of 4973645   100% in    2s  1823.51 kB/s  done
 ```
{: codeblock}
{: S3cmd}

### Java-SDK verwenden
{: #large-objects-java} 
{: java}

Mit dem Java-SDK stehen Ihnen zwei Möglichkeiten zum Hochladen großer Objekte zur Verfügung:
{: java}

* [Mehrteilige Uploads](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Python-SDK verwenden
{: #large-objects-python} 
{: python}

Mit dem Python-SDK stehen Ihnen zwei Möglichkeiten zum Hochladen großer Objekte zur Verfügung:
{: python}

* [Mehrteilige Uploads](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Node.js-SDK verwenden
{: #large-objects-node} 
{: javascript}

Mit dem Node.js-SDK steht Ihnen eine Möglichkeit zum Hochladen großer Objekte zur Verfügung:
{: javascript}

* [Mehrteilige Uploads](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
