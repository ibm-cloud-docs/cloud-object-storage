---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# Anleitung für Entwickler
{: #dev-guide}

## Verschlüsselungseinstellungen optimieren
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} unterstützt eine Vielzahl von Verschlüsselungseinstellungen zur Verschlüsselung von Daten bei der Übertragung. Nicht alle Verschlüsselungseinstellungen liefern die gleiche Leistung. Die Vereinbarung von `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA` oder `TLS_RSA_WITH_AES_128_CBC_SHA` hat dieselben Leistungswerte erbracht wie der Verzicht auf TLS zwischen dem Client und dem {{site.data.keyword.cos_full_notm}}-System.

## Mehrteilige Uploads verwenden
{: #dev-guide-multipart}

Bei der Arbeit mit größeren Objekten werden mehrteilige Upload-Operationen empfohlen, um Objekte in {{site.data.keyword.cos_full_notm}} zu schreiben. Der Upload eines einzelnen Objekts kann als Gruppe von Teilen durchgeführt werden und diese Teile können unabhängig in beliebiger Reihenfolge sowie parallel hochgeladen werden. Nach Beendigung des Uploads stellt {{site.data.keyword.cos_short}} dann alle Teile als ein einziges Objekt dar. Dies bietet zahlreiche Vorteile: Netzunterbrechungen bewirken nicht, dass große Uploads fehlschlagen, Uploads können angehalten und mit der Zeit neu gestartet werden und Objekte können während ihrer Erstellung hochgeladen werden.

Mehrteilige Uploads sind nur für Objekte mit einer Größe von über 5 MB verfügbar. Für Objekte mit einer Größe von weniger als 50 GB wird zwecks optimaler Leistung für die einzelnen Teile eine Größe von 20 MB bis 100 MB empfohlen. Bei größeren Objekten kann die Größe der Teile vergrößert werden, ohne dass hierdurch die Leistung wesentlich beeinträchtigt wird.

Die Verwendung von mehr als 500 Teilen führt zu Ineffizienz in {{site.data.keyword.cos_short}} und sollte nach Möglichkeit vermieden werden.

Aufgrund der beteiligten zusätzlichen Komplexität wird empfohlen, dass Entwickler S3-API-Bibliotheken verwenden, die Unterstützung für mehrteilige Uploads bieten.

Unvollständige mehrteilige Uploads bleiben so lange bestehen, bis das Objekt gelöscht oder der mehrteilige Upload mit `AbortIncompleteMultipartUpload` abgebrochen wird. Wenn ein unvollständiger mehrteiliger Upload nicht abgebrochen wird, werden durch den nur teilweise erfolgten Upload weiterhin Ressourcen verbraucht. Schnittstellen sollten unter Berücksichtigung dieses Aspekts entwickelt werden und unvollständige mehrteilige Uploads bereinigen.

## Software-Development-Kits (SDKs) verwenden
{: #dev-guide-sdks}

Es ist nicht zwingend erforderlich, veröffentlichte S3-API-SDKs zu verwenden; angepasste Software ist in der Lage, die API für die direkte Integration mit {{site.data.keyword.cos_short}} zu nutzen. Die Verwendung veröffentlichter S3-API-Bibliotheken bietet jedoch Vorteile wie Authentifizierung/Signaturgenerierung, Logik für die automatische Wiederholung bei Fehlern des Typs `5xx` und die Generierung vorab signierter URLs. Beim Schreiben von Software, die die API direkt zur Behandlung transienter Fehler verwendet, wie etwa durch die Bereitstellung von Wiederholungsversuchen mit Exponential-Backoff-Algorithmus beim Erhalt von Fehlern des Typs `503`, ist jedoch besondere Vorsicht geboten.

## Paginierung
{: #dev-guide-pagination}

Wenn eine große Anzahl von Objekten in einem Bucket verarbeitet werden muss, können sich bei Webanwendungen Leistungseinbußen bemerkbar machen. Zahlreiche Anwendungen verwenden eine Technik namens **Paginierung**, die den *Vorgang der Aufteilung eines umfangreichen Datensatzsets in einzelne Seiten* bezeichnet. Fast alle Entwicklungsplattformen stellen Objekte oder Methoden bereit, um eine Paginierung zu erzielen, sei es durch eingebaute Funktionen oder durch Bibliotheken anderer Anbieter.

Die SDKs von {{site.data.keyword.cos_short}} bieten Unterstützung für die Paginierung durch eine Methode, bei der die Objekte innerhalb eines bestimmten Buckets aufgelistet werden. Diese Methode stellt eine Reihe von Parametern zur Verfügung, die sich als äußerst nützlich erweisen, wenn eine umfangreiche Ergebnismenge in Teile zerlegt werden soll.

### Grundlegende Verwendung
{: #dev-guide-pagination-basics}
Das Grundkonzept, das sich hinter der Methode der Objektauflistung verbirgt, besteht darin, die maximale Anzahl der Schlüssel (`MaxKeys`) festzulegen, die in der Antwort zurückgegeben werden sollen. Die Antwort enthält außerdem einen `booleschen` Wert (`IsTruncated`), der angibt, ob weitere Ergebnisse verfügbar sind, und einen `Zeichenfolgewert` namens `NextContinuationToken`. Durch Festlegen des Fortsetzungstokens in den Folgeanforderungen wird der nächste Batch von Objekten zurückgegeben, bis keine Ergebnisse mehr verfügbar sind.

#### Allgemeine Parameter
{: #dev-guide-pagination-params}

|Parameter|Beschreibung|
|---|---|
|`ContinuationToken`|Legt Token fest, die den nächsten Batch von Datensätzen angeben.|
|`MaxKeys`|Legt die maximale Anzahl von Schlüsseln fest, die die Antwort enthalten soll.|
|`Prefix`|Schränkt die Antwort auf solche Schlüssel ein, die mit dem angegebenen Präfix beginnen.|
|`StartAfter`|Legt fest, ab wo Objektauflistung basierend auf dem Schlüssel starten soll.|

### Java verwenden
{: #dev-guide-pagination-java}

Das {{site.data.keyword.cos_full}}-SDK für Java stellt die [Methode '`listObjectsV2`'](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} bereit, die eine Rückgabe der Objektauflistung in der gewünschten Größe ermöglicht. Ein vollständiges Codebeispiel finden Sie [hier](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2).

### Python verwenden
{: #dev-guide-pagination-python}

Das {{site.data.keyword.cos_full}}-SDK für Python stellt die [Methode '`list_objects_v2`'](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} bereit, die eine Rückgabe der Objektauflistung in der gewünschten Größe ermöglicht. Ein vollständiges Codebeispiel finden Sie [hier](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2).
