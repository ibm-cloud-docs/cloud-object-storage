---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Kalte Daten mit Übergangsregeln archivieren
{: #archive}

{{site.data.keyword.cos_full}} Archive ist eine [kostengünstige](https://www.ibm.com/cloud/object-storage) Option zur Aufbewahrung von Daten, auf die nur selten zugegriffen wird. Sie können Daten speichern, indem Sie sie aus allen Speichertiers ('Standard', 'Vault', 'Kalte Vault' und 'Flex') in ein Offlinearchiv für die Langzeitspeicherung überführen oder die Onlineoption 'Kalte Vault' verwenden.
{: shortdesc}

Zum Archivieren von Objekten können Sie die Webkonsole, eine REST-API sowie Drittanbietertools verwenden, die in IBM Cloud Object Storage integriert sind. 

Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
{:tip}

## Archivierungsrichtlinie für Bucket hinzufügen oder verwalten
{: #archive-add}

Wenn Sie eine Archivierungsrichtlinie für ein Bucket erstellen oder ändern, sollten Sie Folgendes beachten:

* Eine Archivierungsrichtlinie kann zu jedem beliebigen Zeitpunkt zu einem neuen oder vorhandenen Bucket hinzugefügt werden. 
* Eine vorhandene Archivierungsrichtlinie kann geändert oder inaktiviert werden. 
* Eine neu hinzugefügte oder geänderte Archivierungsrichtlinie gilt für neu hochgeladene Objekte, wirkt sich jedoch auf bereits vorhandene Objekte nicht aus.

Wenn Sie neue Objekte, die in ein Bucket hochgeladen wurden, sofort archivieren möchten, geben Sie für die Archivierungsrichtlinie 0 Tage ein.
{:tip}

Die Archivierung steht nur in bestimmten Regionen zur Verfügung. Weitere Einzelheiten zu diesem Thema finden Sie im Abschnitt [Integrierte Services](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability).
{:tip}

## Archiviertes Objekt wiederherstellen
{: #archive-restore}

Um auf ein archiviertes Objekt zugreifen zu können, müssen Sie es im ursprünglichen Speichertier wiederherstellen. Beim Wiederherstellen eines Objekts können Sie die Anzahl der Tage angeben, die das Objekt verfügbar sein soll. Am Ende des angegebenen Zeitraums wird die wiederhergestellte Kopie gelöscht. 

Der Wiederherstellungsprozess kann bis zu 12 Stunden dauern.
{:tip}

Das archivierte Objekt verfügt über die folgenden Unterstatus:

* Archiviert: Ein Objekt im Status 'Archiviert' wurde auf Basis der geltenden Archivierungsrichtlinie des Buckets von seinem Onlinespeichertier ('Standard', 'Vault', 'Kalte Vault'  oder 'Flex') in das Offlinespeichertier verschoben.
* Wiederherstellen: Für ein Objekt im Status 'Wiederherstellen' wird momentan eine Kopie erstellt, die vom Status 'Archiviert' in das ursprüngliche Onlinespeichertier überführt wird.
* Wiederhergestellt: Ein Objekt im Status 'Wiederhergestellt' ist eine Kopie des archivierten Objekts, das für einen angegebenen Zeitraum in seinem ursprünglichen Onlinespeichertier wiederhergestellt wurde. Nach Ablauf dieses Zeitraums wird die Kopie des Objekts gelöscht, wobei das archivierte Objekt erhalten bleibt.

## Einschränkungen
{: #archive-limitations}

Archivierungsrichtlinien werden mithilfe einer Untergruppe der S3-API-Operation `PUT Bucket Lifecycle Configuration` implementiert. 

Die folgende Funktionalität wird unterstützt:
* Angabe eines Datums oder der Anzahl der Tage in der Zukunft, nach deren Ablauf Objekte in den Status 'Archiviert' überführt werden.
* Angabe der [Ablaufregeln](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry) für Objekte.

Die folgende Funktionalität wird nicht unterstützt:
* Mehrere Übergangsregeln pro Bucket.
* Filterung von zu archivierenden Objekten mit einem Präfix oder Objektschlüssel.
* Tiering zwischen Speicherklassen.

## REST-API und SDKs verwenden
{: #archive-api} 

### Konfiguration für Bucketlebenszyklus erstellen
{: #archive-api-create} 
{: http}

Bei dieser Implementierung der `PUT`-Operation wird der Abfrageparameter `lifecycle` verwendet, um die Lebenszykluseinstellungen für das Bucket festzulegen. Diese Operation ermöglicht die Verwendung einer einzigen Lebenszyklusrichtliniendefinition für ein bestimmtes Bucket. Die Richtlinie wird als Regel definiert, die die folgenden Parameter umfasst: `ID`, `Status` und `Transition` (Übergang).
{: http}

Die Übergangsaktion ermöglicht die Überführung von Objekten, die zukünftig in das Bucket geschrieben werden, in den Status 'Archiviert', nachdem ein definierter Zeitraum verstrichen ist. Änderungen an der Lebenszyklusrichtlinie für ein Bucket werden **nur auf neue Objekte angewendet**, die in dieses Bucket geschrieben werden.

Cloud IAM-Benutzer müssen mindestens über die Rolle `Schreibberechtigter` verfügen, um eine Lebenszyklusrichtlinie zum Bucket hinzuzufügen.

Benutzer der klassischen Infrastruktur müssen über Eignerberechtigungen verfügen und zum Erstellen von Buckets im Speicherkonto berechtigt sein, um eine Lebenszyklusrichtlinie zum Bucket hinzufügen zu können.

Bei dieser Operation werden keine zusätzlichen operationsspezifischen Abfrageparameter verwendet.
{: http}

Header                    | Typ    | Beschreibung
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Zeichenfolge | **Erforderlich**: Der Base64-codierte 128-Bit-MD5-Hashwert der Nutzdaten, der für die Integritätsprüfung verwendet wird, um sicherzustellen, dass die Nutzdaten während der Übertragung nicht geändert wurden.  
{: http}

Der Hauptteil der Anforderung muss einen XML-Block mit dem folgenden Schema enthalten:
{: http}

| Element                  | Typ                  | Untergeordnete Elemente                 | Vorfahre                 | Einschränkung                                                                              |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | Keiner                   | Grenzwert 1.                                                                                  |
| `Rule`                   | Container            | `ID`, `Status`, `Filter`, `Transition` | `LifecycleConfiguration` | Grenzwert 1.                                                                              |
| `ID`                     | Zeichenfolge         | Keine                                  | `Rule`                   | Muss aus den Zeichen (`a-z,`A-Z,0-9`) und den folgenden Symbolen bestehen: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | Zeichenfolge         | `Prefix`                               | `Rule`                   | Muss ein Element `Prefix` enthalten                                                          |
| `Prefix`                 | Zeichenfolge         | Keine                                  | `Filter`                 | **Muss** auf `<Prefix/>` gesetzt werden.                                            |
| `Transition`             | `Container`          | `Days`, `StorageClass`                 | `Rule`                   | Grenzwert 1.                                                                              |
| `Days`                   | Nicht negative Ganzzahl | Keine                                  | `Transition`             |Muss ein Wert größer als '0' sein.|
| `Date`                   | Datum                | Keine                                  | `Transistion`            |Muss im ISO 8601-Format angegeben werden. Das Datum muss in der Zukunft liegen.|
| `StorageClass`           | Zeichenfolge         | Keine                                  | `Transition`             | **Muss** auf `GLACIER` gesetzt werden.                                                             |
{: http}

__Syntax__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # Pfadstil
PUT https://{bucket}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```
{: codeblock}
{: http}
{: caption="Beispiel 1. Beachten Sie die Verwendung von Schrägstrichen und Punkten in diesem Syntaxbeispiel." caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
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
{: codeblock}
{: http}
{: caption="Beispiel 2. XML-Beispiel für die Erstellung einer Objektlebenszykluskonfiguration." caption-side="bottom"}

__Beispiele__
{: http}

_Beispielanforderung_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Beispiel 3. Anforderungsheaderbeispiele für die Erstellung einer Objektlebenszykluskonfiguration." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Beispiel 4. XML-Beispiel für PUT-Anforderungshauptteil." caption-side="bottom"}

_Beispielantwort_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Beispiel 5. Antwortheader." caption-side="bottom"}

---

### Konfiguration für Bucketlebenszyklus abrufen
{: #archive-api-retrieve} 
{: http}

Bei dieser Implementierung der `GET`-Operation wird der Abfrageparameter `lifecycle` verwendet, um die Lebenszykluseinstellungen für das Bucket abzurufen. 

Cloud IAM-Benutzer müssen mindestens über die Rolle `Leseberechtigter` verfügen, um einen Lebenszyklus für ein Bucket abzurufen.

Benutzer der klassischen Infrastruktur müssen mindestens über die Berechtigungen zum `Lesen` für das Bucket verfügen, um eine Lebenszyklusrichtlinie für ein Bucket abzurufen.

Diese Operation verwendet keine zusätzlichen operationsspezifischen Header, Abfrageparameter oder Nutzdaten.

__Syntax__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # Pfadstil
GET https://{bucket}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```
{: codeblock}
{: http}
{: caption="Beispiel 6. Syntaxvarianten für GET-Anforderungen." caption-side="bottom"}

__Beispiele__
{: http}

_Beispielanforderung_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Beispiel 7. Beispielanforderungsheader zum Abrufen der Konfiguration." caption-side="bottom"}

_Beispielantwort_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Beispiel 8. Beispielantwortheader von GET-Anforderung." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Beispiel 9. XML-Beispiel für Antworthauptteil." caption-side="bottom"}

---

### Konfiguration für Bucketlebenszyklus löschen
{: #archive-api-delete} {: http}

Bei dieser Implementierung der `DELETE`-Operation wird der Abfrageparameter `lifecycle` verwendet, um die Lebenszykluseinstellungen für das Bucket zu entfernen. Übergänge, die anhand der Regeln definiert wurden, werden für neue Objekte nicht mehr angewendet. 

**Hinweis:** Vorhandene Übergangsregeln werden für Objekte beibehalten, die bereits in das Bucket geschrieben wurden, bevor die Regeln gelöscht wurden.

Cloud IAM-Benutzer müssen mindestens über die Rolle `Schreibberechtigter` verfügen, um eine Lebenszyklusrichtlinie aus dem Bucket zu entfernen.

Benutzer der klassischen Infrastruktur müssen mindestens über die Berechtigungen für `Eigner` für das Bucket verfügen, um eine Lebenszyklusrichtlinie aus einem Bucket zu entfernen.

Diese Operation verwendet keine zusätzlichen operationsspezifischen Header, Abfrageparameter oder Nutzdaten.

__Syntax__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # Pfadstil
DELETE https://{bucket}.{endpoint}?lifecycle # Stil des virtuellen Hosts
```
{: codeblock}
{: http}
{: caption="Beispiel 10. Beachten Sie die Verwendung von Schrägstrichen und Punkten in diesem Syntaxbeispiel." caption-side="bottom"}

__Beispiele__
{: http}

_Beispielanforderung_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Beispiel 11. Beispielanforderungsheader für das HTTP-Verb DELETE." caption-side="bottom"}

_Beispielantwort_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Beispiel 12. Beispielantwort für DELETE-Anforderung." caption-side="bottom"}

---

### Archiviertes Objekt vorübergehend wiederherstellen 
{: #archive-api-restore} {: http}

Bei dieser Implementierung der `POST`-Operation wird der Abfrageparameter `restore` verwendet, um die vorübergehende Wiederherstellung eines archivierten Objekts anzufordern. Der Benutzer muss ein archiviertes Objekt zuerst wiederherstellen, bevor es heruntergeladen oder geändert wird. Beim Wiederherstellen eines Objekts muss der Benutzer einen Zeitraum angeben, nach dessen Ablauf die temporäre Kopie des Objekts gelöscht wird. Das Objekt behält die Speicherklasse des Buckets bei.

Es kann zu einer Verzögerung von bis zu 12 Stunden kommen, bevor auf die wiederhergestellte Kopie zugegriffen werden kann. Eine `HEAD`-Anforderung kann verwendet werden, um zu prüfen, ob die wiederhergestellte Kopie verfügbar ist. 

Um das Objekt dauerhaft wiederherzustellen, muss der Benutzer das wiederhergestellte Objekt in ein Bucket kopieren, das keine aktive Lebenszykluskonfiguration hat.

Cloud IAM-Benutzer müssen mindestens über die Rolle `Schreibberechtigter` verfügen, um ein Objekt wiederherzustellen.

Benutzer der klassischen Infrastruktur müssen mindestens über die Berechtigungen `Schreiben` für das Bucket und über die Berechtigung `Lesen` für das Objekt verfügen, um es wiederherzustellen.

Bei dieser Operation werden keine zusätzlichen operationsspezifischen Abfrageparameter verwendet.

Header                    | Typ    | Beschreibung
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Zeichenfolge | **Erforderlich**: Der Base64-codierte 128-Bit-MD5-Hashwert der Nutzdaten, der für die Integritätsprüfung verwendet wird, um sicherzustellen, dass die Nutzdaten während der Übertragung nicht geändert wurden.  

Der Hauptteil der Anforderung muss einen XML-Block mit dem folgenden Schema enthalten:

Element                  | Typ    | Untergeordnete Elemente                 | Vorfahre                 |Einschränkung
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | Container | `Days`, `GlacierJobParameters`    | Keiner     |Keine
`Days`                   | Ganzzahl | Keine | `RestoreRequest` | Gibt die Lebensdauer des vorübergehend wiederhergestellten Objekts an. Die Mindestanzahl von Tagen, die eine wiederhergestellte Kopie des Objekts vorhanden sein kann, ist '1'. Nach Ablauf des Wiederherstellungszeitraums wird die temporäre Kopie des Objekts entfernt.
`GlacierJobParameters` | Zeichenfolge | `Tier` | `RestoreRequest` |Keine
`Tier` | Zeichenfolge | Keine | `GlacierJobParameters` | **Muss** auf `Bulk` gesetzt werden.

Eine erfolgreiche Antwort gibt eine Nachricht `202` zurück, wenn das Objekt sich im Status 'Archiviert' befindet, und eine Nachricht `200`, wenn sich das Objekt bereits im Status 'Wiederhergestellt' befindet. Wenn sich das Objekt bereits im Status 'Wiederhergestellt' befindet und eine neue Anforderung zum Wiederherstellen des Objekts empfangen wird, dann wird mit dem Element `Days` die Ablaufzeit des wiederhergestellten Objekts aktualisiert.

__Syntax__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # Pfadstil
POST https://{bucket}.{endpoint}/{object}?restore # Stil des virtuellen Hosts
```
{: codeblock}
{: http}
{: caption="Beispiel 13. Beachten Sie die Verwendung von Schrägstrichen und Punkten in diesem Syntaxbeispiel." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Beispiel 14. Modell des XML-Codes für Anforderungshauptteil." caption-side="bottom"}

__Beispiele__
{: http}

_Beispielanforderung_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Beispiel 15. Beispielanforderungsheader für die Objektwiederherstellung." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Beispiel 16. Beispielanforderungshauptteil für Objektwiederherstellung." caption-side="bottom"}

_Beispielantwort_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Beispiel 17. Antwort auf Objektwiederherstellung ('HTTP 202')." caption-side="bottom"}

---

### Header eines Objekts abrufen
{: http}
{: #archive-api-head}

Mit einer `HEAD`-Anforderung, in der ein Pfad zu einem Objekt angegeben wurde, werden die Header des betreffenden Objekts abgerufen. Diese Operation verwendet keine operationsspezifischen Abfrageparameter oder Nutzdatenelemente.

__Syntax__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # Pfadstil
HEAD https://{bucket-name}.{endpoint}/{object-name} # Stil des virtuellen Hosts
```
{: codeblock}
{: http}
{: caption="Beispiel 18. Varianten bei der Endpunktdefinition." caption-side="bottom"}


__Antwortheader für archivierte Objekte__
{: http}

Header | Typ  | Beschreibung
--- | ---- | ------------
`x-amz-restore` | Zeichenfolge |Wird angegeben, wenn das Objekt wiederhergestellt wurde oder wenn momentan eine Wiederherstellung ausgeführt wird. Wenn das Objekt wiederhergestellt wurde, wird auch das Ablaufdatum für die temporäre Kopie zurückgegeben.
`x-amz-storage-class` | Zeichenfolge |Gibt `GLACIER` zurück, wenn es sich um ein archiviertes oder vorübergehend wiederhergestelltes Objekt handelt.
`x-ibm-archive-transition-time` | Datum |Gibt den Zeitpunkt (Datum und Uhrzeit) zurück, zu dem das Objekt für den Übergang in das Archivierungstier eingeplant ist.
`x-ibm-transition` | Zeichenfolge |Wird angegeben, wenn das Objekt über Übergangsmetadaten verfügt und das Tier und die ursprüngliche Uhrzeit des Übergangs zurückgibt.
`x-ibm-restored-copy-storage-class` | Zeichenfolge | Wird angegeben, wenn ein Objekt sich im Status `RestoreInProgress` oder `Restored` befindet und die Speicherklasse des Buckets zurückgibt.


_Beispielanforderung_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="Beispiel 19. Beispiel für Anforderungsheader." caption-side="bottom"}

_Beispielantwort_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="Beispiel 20. Beispiel für Antwortheader." caption-side="bottom"}


### Konfiguration für Bucketlebenszyklus erstellen
{: #archive-node-create} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* erforderlich */
  LifecycleConfiguration: {
    Rules: [ /* erforderlich */
      {
        Status: 'Enabled', /* erforderlich */
        ID: 'STRING_VALUE',
        Filter: '', /* erforderlich */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* erforderlich, wenn Days nicht angegeben wurde */
            Days: 0, /* erforderlich, wenn Date nicht angegeben wurde */
            StorageClass: 'GLACIER' /* erforderlich */
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}
{: caption="Beispiel 21. Beispiel für Erstellung einer Lebenszykluskonfiguration." caption-side="bottom"}

### Konfiguration für Bucketlebenszyklus abrufen
{: #archive-node-retrieve} {: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* erforderlich */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}
{: caption="Beispiel 22. Beispiel für Abruf der Lebenszyklusmetadaten." caption-side="bottom"}

### Konfiguration für Bucketlebenszyklus löschen
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* erforderlich */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}
{: caption="Beispiel 23. Beispiel für das Löschen der Lebenszykluskonfiguration eines Buckets." caption-side="bottom"}

### Archiviertes Objekt vorübergehend wiederherstellen 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* erforderlich */
  Key: 'STRING_VALUE', /* erforderlich */
  ContentMD5: 'STRING_VALUE', /* erforderlich */
  RestoreRequest: {
   Days: 1, /* Tage bis Ablauf der Kopie */
   GlacierJobParameters: {
     Tier: Bulk /* erforderlich */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}
{: caption="Beispiel 24. Zum Wiederherstellen eines archivierten Objekts verwendeter Code." caption-side="bottom"}

### Header eines Objekts abrufen
{: #archive-node-head} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* erforderlich */
  Key: 'STRING_VALUE', /* erforderlich */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // es ist ein Fehler aufgetreten
  else     console.log(data);           // erfolgreiche Antwort
});
```
{: codeblock}
{: javascript}
{: caption="Beispiel 25. Beispiel für Abruf von Objektheadern." caption-side="bottom"}


### Konfiguration für Bucketlebenszyklus erstellen
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="Beispiel 26. Methode zur Erstellung einer Objektkonfiguration." caption-side="bottom"}

### Konfiguration für Bucketlebenszyklus abrufen
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Beispiel 27. Methode zum Abrufen einer Objektkonfiguration." caption-side="bottom"}

### Konfiguration für Bucketlebenszyklus löschen
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Beispiel 28. Methode zum Löschen einer Objektkonfiguration." caption-side="bottom"}

### Archiviertes Objekt vorübergehend wiederherstellen 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="Beispiel 29. Vorübergehende Wiederherstellung eines archivierten Objekts." caption-side="bottom"}

### Header eines Objekts abrufen
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="Beispiel 30. Behandlung der Antwort für Objektheader." caption-side="bottom"}


### Konfiguration für Bucketlebenszyklus erstellen
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="Beispiel 31. Funktion zum Festlegen eines Bucketlebenszyklus." caption-side="bottom"}

**Zusammenfassung der Methode**
{: java}

Methode |  Beschreibung
--- | ---
`getBucketName()` | Ruft den Namen des Buckets ab, dessen Lebenszykluskonfiguration festgelegt wird.
`getLifecycleConfiguration()` | Ruft die neue Lebenszykluskonfiguration für das angegebene Bucket ab.
`setBucketName(String bucketName)` | Ruft den Namen des Buckets ab, dessen Lebenszykluskonfiguration festgelegt wird.
`withBucketName(String bucketName)` | Legt den Namen des Buckets fest, dessen Lebenszykluskonfiguration festgelegt wird, und gibt dieses Objekt zurück, sodass zusätzliche Methodenaufrufe verkettet werden können.
{: java}

### Konfiguration für Bucketlebenszyklus abrufen
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Beispiel 32. Funktionssignatur zum Abrufen der Lebenszykluskonfiguration eines Objekts." caption-side="bottom"}

### Konfiguration für Bucketlebenszyklus löschen
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Beispiel 33. Funktion zum Löschen einer Objektkonfiguration." caption-side="bottom"}

### Archiviertes Objekt vorübergehend wiederherstellen 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="Beispiel 34. Funktionssignatur zur Wiederherstellung eines archivierten Objekts." caption-side="bottom"}

**Zusammenfassung der Methode**
{: java}

Methode |  Beschreibung
--- | ---
`clone()` | Erstellt einen flachen Klon dieses Objekts für alle Felder mit Ausnahme des Handlerkontextes.
`getBucketName()` | Gibt den Namen des Buckets zurück, das den Verweis auf das wiederherzustellende Objekt enthält.
`getExpirationInDays()` | Gibt die Zeitdauer in Tagen von der Erstellung eines Objekts bis zu seinem Ablauf zurück.
`setExpirationInDays(int expirationInDays)` | Legt die Zeitdauer (in Tagen) fest, die zwischen dem Zeitpunkt, zu dem ein Objekt in das Bucket hochgeladen wird, und dem Zeitpunkt, zu dem es abläuft, liegt.
{: java}

### Objektheader abrufen
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="Beispiel 35. Funktion zum Abrufen von Objektheadern." caption-side="bottom"}

**Zusammenfassung der Methode**
{: java}

Methode |  Beschreibung
--- | ---
`clone()` | Gibt einen Klon für dieses Element `ObjectMetadata` zurück.
`getRestoreExpirationTime()` | Gibt den Zeitpunkt zurück, zu dem ein Objekt, das vorübergehend aus dem Archiv wiederhergestellt wurde, abläuft und erneut wiederhergestellt werden muss, damit auf das Objekt zugegriffen werden kann.
`getStorageClass() ` | Gibt die ursprüngliche Speicherklasse des Buckets zurück.
`getIBMTransition()` | Gibt die Übergangsspeicherklasse und den Übergangszeitpunkt zurück.
{: java}

## Nächste Schritte
{: #archive-next-steps}

Zusätzlich zu {{site.data.keyword.cos_full_notm}} stellt {{site.data.keyword.cloud_notm}} momentan verschiedene zusätzliche Object Storage-Angebote für unterschiedliche Benutzeranforderungen zur Verfügung, auf die über webbasierte Portale und REST-APIs zugegriffen werden kann. [Weitere Informationen](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud).
