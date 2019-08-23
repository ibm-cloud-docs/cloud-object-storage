---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Allgemeine Header & Fehlercodes
{: #compatibility-common}

## Allgemeine Anforderungsheader
{: #compatibility-request-headers}

In der folgenden Tabelle werden die unterstützten allgemeinen Anforderungsheader beschrieben. {{site.data.keyword.cos_full}} ignoriert alle allgemeinen Header, die nicht im Folgenden aufgelistet sind, wenn diese Header in einer Anforderung gesendet werden, obwohl einige Anforderungen möglicherweise andere Header unterstützen, die in dieser Dokumentation definiert werden.

| Header                  |Hinweis                                                                                                                             |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Authorization           | **Erforderlich** für alle Anforderungen (OAuth2-Token `bearer`).                                                     |
| ibm-service-instance-id | **Erforderlich** für Anforderungen zum Erstellen oder Auflisten von Buckets.                                                          |
| Content-MD5             | Der Base64-codierte 128-Bit-MD5-Hashwert der Nutzdaten, der für die Integritätsprüfung verwendet wird, um sicherzustellen, dass die Nutzdaten während der Übertragung nicht geändert wurden.  |
| Expect                  | Beim Wert `100-continue` wird auf die Bestätigung des Systems gewartet, dass die Header korrekt sind, bevor die Nutzdaten gesendet werden. |
| host                    | Entweder der Endpunkt oder die Syntax 'virtual host' von `{bucket-name}.{endpoint}`. In der Regel wird dieser Header automatisch hinzugefügt. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).| 
| Cache-Control | Kann verwendet werden, um das Caching-Verhalten für den Verlauf der Anforderungs-/Antwortkette anzugeben. Weitere Informationen finden Sie unter http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9. |

### Angepasste Metadaten
{: #compatibility-headers-metadata}

Ein Vorteil bei der Verwendung des Objektspeichers ist die Möglichkeit, benutzerdefinierte Metadaten hinzuzufügen, indem Schlüssel/Wert-Paare als Header gesendet werden. Diese Header werden im Format `x-amz-meta-{KEY}` angegeben. Beachten Sie hierbei, dass anders als bei AWS S3 in IBM COS mehrere Header mit dem gleichen Metadatenschlüssel in einer Liste mit Werten zusammengeführt werden, die durch Kommas getrennt sind.

## Allgemeine Antwortheader
{: #compatibility-response-headers}

In der folgenden Tabelle werden die allgemeinen Antwortheader beschrieben.

| Header           | Hinweis                                             |
|------------------|-----------------------------------------------------|
| Content-Length   | Die Länge des Anforderungshauptteils in Byte.       |
| Connection       | Gibt an, ob die Verbindung geöffnet oder geschlossen ist. |
| Date             | Die Zeitmarke der Anforderung.                     |
| ETag             | Der MD5-Hashwert der Anforderung.                  |
| Server           | Der Name des antwortenden Servers.                 |
| X-Clv-Request-Id | Eindeutige Kennung, die für jede Anforderung generiert wird. |

### Lebenszyklusantwortheader
{: #compatibility-lifecycle-headers}

In der folgenden Tabelle werden Antwortheader für archivierte Objekte beschrieben.

| Header           | Hinweis                                             |
|------------------|-----------------------------------------------------|
|x-amz-restore|Wird angegeben, wenn das Objekt wiederhergestellt wurde oder wenn momentan eine Wiederherstellung ausgeführt wird. |
|x-amz-storage-class|Gibt `GLACIER` zurück, wenn es sich um ein archiviertes oder vorübergehend wiederhergestelltes Objekt handelt.|
|x-ibm-archive-transition-time|Gibt den Zeitpunkt (Datum und Uhrzeit) zurück, zu dem das Objekt für den Übergang in das Archivierungstier eingeplant ist.|
|x-ibm-transition|Wird angegeben, wenn das Objekt über Übergangsmetadaten verfügt und das Tier und die ursprüngliche Uhrzeit des Übergangs zurückgibt.|
|x-ibm-restored-copy-storage-class|Wird angegeben, wenn ein Objekt sich im Status `RestoreInProgress` oder `Restored` befindet und die Speicherklasse des Buckets zurückgibt.|

## Fehlercodes
{: #compatibility-errors}

| Fehlercode                          | Beschreibung                                                                                           | HTTP-Statuscode                    |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | Zugriff verweigert                                                                                     | 403 Nicht zulässig                 |
| BadDigest                           | Der angegebene Content-MD5-Wert stimmte nicht mit den empfangenen Daten überein.                       | 400 Fehlerhafte Anforderung         |
| BucketAlreadyExists                 | Der angeforderte Bucketname ist nicht verfügbar. Der Namensbereich des Buckets wird von allen Benutzern des Systems gemeinsam genutzt. Wählen Sie einen anderen Namen aus und versuchen Sie es erneut. | 409 Konflikt                        |
| BucketAlreadyOwnedByYou             | Ihre vorherige Anforderung, den angegebenen Bucket zu erstellen, war erfolgreich. Sie sind bereits der Eigner.              | 409 Konflikt                        |
| BucketNotEmpty                      | Das Bucket, das Sie löschen wollten, ist nicht leer.                                                                        | 409 Konflikt                        |
| CredentialsNotSupported             | Diese Anforderung bietet keine Unterstützung für Berechtigungsnachweise.                                                    | 400 Fehlerhafte Anforderung         |
| EntityTooSmall                      | Der von Ihnen vorgeschlagene Upload unterschreitet die zulässige Mindestgröße für Objekte.                                  | 400 Fehlerhafte Anforderung         |
| EntityTooLarge                      | Der von Ihnen vorgeschlagene Upload überschreitet die zulässige Maximalgröße für Objekte.                                   | 400 Fehlerhafte Anforderung         |
| IncompleteBody                      | Sie haben die im HTTP-Header 'Content-Length' angegebene Anzahl von Byte nicht angegeben.                                   | 400 Fehlerhafte Anforderung         |
| IncorrectNumberOfFilesInPostRequest | Für POST ist genau ein Dateiupload pro Anforderung erforderlich.                                                            | 400 Fehlerhafte Anforderung         |
| InlineDataTooLarge                  | Die Inline-Daten überschreiten die maximal zulässige Größe.                                                                 | 400 Fehlerhafte Anforderung         |
| InternalError                       | Es wurde ein interner Fehler festgestellt. Wiederholen Sie den Vorgang.                                                     | 500 Interner Serverfehler |
| InvalidAccessKeyId                  | Die von Ihnen angegebene AWS-Zugriffsschlüssel-ID ist in den Datensätzen nicht vorhanden.                                   | 403 Nicht zulässig                  |
| InvalidArgument                     | Ungültiges Argument.                                                                                                        | 400 Fehlerhafte Anforderung         |
| InvalidBucketName                   | Das angegebene Bucket ist nicht gültig.                                                                                     | 400 Fehlerhafte Anforderung         |
| InvalidBucketState                  | Die Anforderung ist für den aktuellen Status des Buckets nicht gültig.                                                      | 409 Konflikt                        |
| InvalidDigest                       | Der von Ihnen angegebene Content-MD5-Wert ist nicht gültig.                                                                 | 400 Fehlerhafte Anforderung         |
| InvalidLocationConstraint           | Die angegebene Standortbedingung ist nicht gültig. Weitere Informationen zu Regionen finden Sie im Abschnitt zur Auswahl einer Region für Ihre Buckets.            | 400 Fehlerhafte Anforderung         |
| InvalidObjectState                  | Die Operation ist für den aktuellen Status des Objekts nicht gültig.                                                        | 403 Nicht zulässig                  |
| InvalidPart                         | Mindestens eines der angegebenen Teile konnte nicht gefunden werden. Möglicherweise wurde das Teil nicht hochgeladen oder der angegebene Entitätstag stimmte nicht mit dem Entitätstag des Teils überein. | 400 Fehlerhafte Anforderung         |
| InvalidPartOrder                    | Die Liste der Teile ist nicht in aufsteigender Reihenfolge sortiert. Die Teileliste muss in der richtigen Reihenfolge nach Teilenummer angegeben werden.                               | 400 Fehlerhafte Anforderung         |
| InvalidRange                        | Der angeforderte Bereich kann nicht abgerufen werden.                                           | 416 Angeforderter Bereich nicht abrufbar |
| InvalidRequest                      | Bitte verwenden Sie AWS4-HMAC-SHA256.                                                           | 400 Fehlerhafte Anforderung         |
| InvalidSecurity                     | Die angegebenen Sicherheitsberechtigungsnachweise sind nicht gültig.                            | 403 Nicht zulässig                  |
| InvalidURI                          | Der angegebene URI konnte nicht geparst werden.                                                 | 400 Fehlerhafte Anforderung         |
| KeyTooLong                          | Ihr Schlüssel ist zu lang.                                                                      | 400 Fehlerhafte Anforderung         |
| MalformedPOSTRequest                | Im Hauptteil der POST-Anforderung sind keine korrekt formatierten mehrteiligen Formulardaten ('multipart/form-data') angegeben. | 400 Fehlerhafte Anforderung         |
| MalformedXML                        | Der angegebene XML-Code war nicht korrekt formatiert oder konnte anhand des veröffentlichten Schemas nicht validiert werden.    | 400 Fehlerhafte Anforderung         |
| MaxMessageLengthExceeded            | Die Anforderung war zu groß.                                                                                                    | 400 Fehlerhafte Anforderung         |
| MaxPostPreDataLengthExceededError   | Die vor der Uploaddatei angegebenen Felder Ihrer POST-Anforderung waren zu groß.                                                | 400 Fehlerhafte Anforderung         |
| MetadataTooLarge                    | Ihre Metadatenheader überschreiten die maximal zulässige Metadatengröße.                                                        | 400 Fehlerhafte Anforderung         |
| MethodNotAllowed                    | Die angegebene Methode ist für diese Ressource nicht zulässig.                                                                  | 405 Methode nicht zulässig          |
| MissingContentLength                | Sie müssen den HTTP-Header 'Content-Length' angeben.                          | 411 Länge erforderlich              |
| MissingRequestBodyError             | Dieser Fehler tritt auf, wenn der Benutzer ein leeres XML-Dokument als Anforderung sendet. Die Fehlernachricht lautet: "Der Anforderungshauptteil ist leer."   | 400 Fehlerhafte Anforderung         |
| NoSuchBucket                        | Das angegebene Bucket ist nicht vorhanden.                                                                          | 404 Nicht gefunden                  |
| NoSuchKey                           | Der angegebene Schlüssel ist nicht vorhanden.                                                                       | 404 Nicht gefunden                  |
| NoSuchUpload                        | Der angegebene mehrteilige Upload ist nicht vorhanden. Möglicherweise ist die Upload-ID ungültig oder der mehrteilige Upload wurde abgebrochen oder abgeschlossen.     | 404 Nicht gefunden                  |
| NotImplemented                      | Der von Ihnen angegebene Header impliziert Funktionalität, die nicht implementiert wurde.                               | 501 Nicht implementiert             |
| OperationAborted                    | Momentan wird eine bedingte Operation für diese Ressource ausgeführt, die zu einem Konflikt führt. Wiederholen Sie den Vorgang.    | 409 Konflikt                        |
| PreconditionFailed                  | Mindestens eine der Vorbedingungen, die Sie angegeben haben, wurde nicht eingehalten.                                              | 412 Vorbedingung fehlgeschlagen     |
| Redirect                            | Vorübergehende Umleitung.                                                                                                          | 307 Vorübergehend verschoben        |
| RequestIsNotMultiPartContent        | In der POST-Anforderung des Buckets muss für 'enclosure-type' die Einstellung 'multipart/form-data' (mehrteilige Formulardaten) angegeben werden. | 400 Fehlerhafte Anforderung         |
| RequestTimeout                      | Ihre Socketverbindung zum Server wurde innerhalb des Zeitlimitintervalls nicht für Lese- oder Schreiboperationen verwendet.        | 400 Fehlerhafte Anforderung         |
| RequestTimeTooSkewed                | Der Unterschied zwischen der Anforderungszeit und der Serverzeit ist zu groß.                                                      | 403 Nicht zulässig                  |
| ServiceUnavailable                  | Reduzieren Sie die Anforderungsrate.                                                                                               | 503 Service nicht verfügbar             |
| SlowDown                            | Reduzieren Sie die Anforderungsrate.                                                                                               | 503 Verlangsamung               |
| TemporaryRedirect                   | Während der DNS-Aktualisierung werden Sie an das Bucket umgeleitet.                                           | 307 Vorübergehend verschoben        |
| TooManyBuckets                      | Sie haben versucht, eine Anzahl von Buckets zu erstellen, die die maximal zulässige Anzahl überschreitet.     | 400 Fehlerhafte Anforderung         |
| UnexpectedContent                   | Diese Anforderung bietet keine Unterstützung für Inhalt.                                                      | 400 Fehlerhafte Anforderung         |
| UserKeyMustBeSpecified              | Die POST-Anforderung des Buckets muss den angegebenen Feldnamen enthalten. Wurde er angegeben, dann überprüfen Sie die Reihenfolge der Felder.  | 400 Fehlerhafte Anforderung         |
