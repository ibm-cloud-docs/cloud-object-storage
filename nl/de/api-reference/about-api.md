---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# Informationen zur S3-API von {{site.data.keyword.cos_full_notm}}
{: #compatibility-api}

Bei der {{site.data.keyword.cos_full}}-API handelt es sich um eine REST-basierte API zum Durchführen von Lese- und Schreiboperationen für Objekte. Sie verwendet {{site.data.keyword.iamlong}} für die Authentifizierung und Berechtigung und unterstützt eine Untergruppe der S3-API für die einfache Migration von Anwendungen auf {{site.data.keyword.cloud_notm}}.

Diese Referenzdokumentation wird laufend verbessert. Wenn Sie technische Fragen zur Verwendung der API in Ihrer Anwendung haben, posten Sie sie unter [StackOverflow](https://stackoverflow.com/). Fügen Sie die Tags `ibm-cloud-platform` und `object-storage` hinzu und verbessern Sie diese Dokumentation mit Ihrem Feedback.

Da das Arbeiten mit {{site.data.keyword.iamshort}}-Tokens relativ einfach ist, stellt `curl` eine gute Wahl zum Durchführen grundlegender Tests und zur Interaktion mit Ihrem Speicher dar. Weitere Informationen finden Sie in der [`curl`-Referenz](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl).

In den folgenden Tabellen wird die vollständige Gruppe der Operationen in der {{site.data.keyword.cos_full_notm}}-API beschrieben. Weitere Informationen finden Sie auf der [API-Referenzseite für Buckets](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) oder [Objekte](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).


## Bucketoperationen
{: #compatibility-api-bucket}

Diese Operationen dienen zum Erstellen und Löschen von Buckets, zum Abrufen von Informationen zu Buckets und zum Steuern des Verhaltens von Buckets.

| Bucketoperation        | Hinweis                                                                         |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` Buckets           | Wird verwendet, um eine Liste aller Buckets abzurufen, die zu einem Konto gehören. |
| `DELETE` Bucket         | Löscht ein leeres Bucket.                                                        |
| `DELETE` Bucket-CORS    | Löscht die gesamte CORS-Konfiguration (CORS = Cross-Origin Resource Sharing), die für ein Bucket festgelegt wurde. |
| `GET` Bucket            | Listet die Objekte in einem Bucket auf. Es können maximal 1.000 Objekte gleichzeitig aufgelistet werden. |
| `GET` Bucket-CORS       | Ruft die gesamte CORS-Konfiguration ab, die für ein Bucket festgelegt wurde.      |
| `HEAD` Bucket           | Ruft die Header eines Buckets ab.                                              |
| `GET` Mehrteilige Uploads | Listet mehrteilige Uploads auf, die nicht abgeschlossen oder abgebrochen wurden.    |
| `PUT` Bucket            | Die Buckets unterliegen Einschränkungen bei der Benennung. Konten sind auf 100 Buckets begrenzt.     |
| `PUT` Bucket-CORS       | Erstellt eine CORS-Konfiguration für einen Bucket.                                     |


## Objektoperationen
{: #compatibility-api-object}

Diese Operationen dienen zum Erstellen und Löschen von Buckets, zum Abrufen von Informationen zu Buckets und zum Steuern des Verhaltens von Buckets.

| Objektoperation           | Hinweis                                                                             |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` Objekt           | Löscht ein Objekt aus einem Bucket.                                                |
| `DELETE` Batch            | Löscht mehrere Objekte mit einer Operation aus einem Bucket.                     |
| `GET` Objekt              | Ruft ein Objekt aus einem Bucket ab.                                               |
| `HEAD` Objekt             | Ruft die Header eines Objekts ab.                                                  |
| `OPTIONS` Objekt          | Überprüft die CORS-Konfiguration, um festzustellen, ob eine bestimmte Anforderung gesendet werden kann. |
| `PUT` Objekt              | Fügt ein Objekt zu einem Bucket hinzu.                                             |
| `PUT` Objekt (Kopieren)   | Erstellt eine Kopie eines Objekts.                                                 |
| Mehrteiligen Upload beginnen | Erstellt eine Upload-ID für eine Gruppe von Teilen, die hochgeladen werden sollen. |
| Teil hochladen            | Lädt ein Teil eines Objekts hoch, das einer Upload-ID zugeordnet ist.                    |
| Teil hochladen (Kopieren) | Lädt ein Teil eines vorhandenen Objekts hoch, das einer Upload-ID zugeordnet ist.              |
| Mehrteiligen Upload ausführen | Setzt ein Objekt aus den Teilen zusammen, die einer Upload-ID zugeordnet sind. |
| Mehrteiligen Upload abbrechen | Bricht den Upload ab und löscht noch anstehende Teile, die einer Upload-ID zugeordnet sind. |
| Teile auflisten           | Gibt eine Liste der Teile zurück, die einer Upload-ID zugeordnet sind.                   |


In {{site.data.keyword.cos_short}}-Implementierungen mit einer privaten Cloud von werden einige zusätzliche Operationen wie beispielsweise das Tagging und die Versionierung unterstützt. Diese Unterstützung ist jedoch in öffentlichen oder dedizierten Clouds momentan nicht verfügbar. Weitere Informationen zu angepassten Object Storage-Lösungen finden Sie unter [ibm.com](https://www.ibm.com/cloud/object-storage).
