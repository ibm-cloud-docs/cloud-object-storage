---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# Daten hochladen
{: #upload}

Nachdem Sie Ihre Buckets organisiert haben, sollten Sie ihnen bestimmte Objekte hinzuzufügen. Abhängig davon, wie Sie den Speicher nutzen möchten, gibt es verschiedene Möglichkeiten, um Daten ins System einzulesen. Ein Data-Scientist verfügt über einige große Dateien, die zu Analysezwecken verwendet werden. Ein Systemadministrator muss sicherstellen, dass die Datenbanksicherungen mit den lokalen Dateien synchronisiert bleiben und ein Entwickler schreibt Software, mit der Millionen von Dateien gelesen und geschrieben werden sollen. Für jedes dieser Szenarios gibt es eine optimale Methode zur Datenaufnahme.

## Konsole verwenden
{: #upload-console}

Normalerweise stellt die webbasierte Konsole nicht die am häufigsten genutzte Methode zur Verwendung von {{site.data.keyword.cos_short}} dar. Objekte sind auf eine Größe von 200 MB beschränkt und der Dateiname und der Schlüssel sind identisch. Sie können mehrere Objekte gleichzeitig hochladen. Sofern der verwendete Browser mehrere Threads unterstützt, kann jedes Objekt in mehreren Teilen parallel hochgeladen werden. Die Unterstützung für größere Objekte und die (abhängig von bestimmten Netzfaktoren) verbesserte Leistung wird durch die [Aspera-Hochgeschwindigkeitsübertragung](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) bereitgestellt.

## Kompatibles Tool verwenden
{: #upload-tool}

Bestimmte Benutzer möchten ein eigenständiges Dienstprogramm verwenden, um mit ihrem Speicher zu interagieren. Da die Cloud Object Storage-API die am häufigsten verwendete Gruppe von S3-API-Operationen unterstützt, können viele S3-kompatible Tools eine Verbindung zu {{site.data.keyword.cos_short}} auch mithilfe von [HMAC-Berechtigungsnachweisen](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) herstellen.

Die Beispiele umfassen Dateiexplorer wie [Cyberduck](https://cyberduck.io/) oder [Transmit](https://panic.com/transmit/), Sicherungsdienstprogramme wie [Cloudberry](https://www.cloudberrylab.com/) und [Duplicati](https://www.duplicati.com/), Befehlszeilendienstprogramme wie [s3cmd](https://github.com/s3tools/s3cmd) oder [MinIO Client](https://github.com/minio/mc) und zahlreiche andere Tools.

## API verwenden
{: #upload-api}

Die meisten programmgesteuerten Object Storage-Anwendungen verwenden ein SDK (z. B. für [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) oder [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)) oder die [Cloud Object Storage-API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api). Objekte werden normalerweise in [mehreren Teilen](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) hochgeladen. Dabei werden die Größe der Teile und ihre Anzahl mithilfe einer Klasse des Übertragungsmanagers konfiguriert.
