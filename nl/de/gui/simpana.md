---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, simpana

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


# CommVault Simpana zum Archivieren von Daten verwenden
{: #commvault}

CommVault Simpana kann mit dem Archivtier von {{site.data.keyword.cos_full_notm}} integriert werden. Weitere Informationen zu Simpana enthält die [Dokumentation zu CommVault Simpana](https://documentation.commvault.com/commvault/).

Weitere Informationen zu IBM COS Infrastructure Archive finden Sie in [Vorgehensweise zur Archivieren von Daten](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive).

## Schritte zur Integration
{: #commvault-integration}

1.	Erstellen Sie über die Simpana-Konsole eine Amazon S3-Cloudspeicherbibliothek. 

2. Stellen Sie sicher, dass der Service-Host auf den Endpunkt verweist. Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Simpana stellt in diesem Schritt Buckets bereit, kann aber auch bereitgestellte Buckets verarbeiten. 

3.	Erstellen Sie eine Richtlinie für das Bucket. Für die Erstellung der Richtlinie können Sie die AWS-CLI, SDKs oder die Webkonsole verwenden. Nachfolgend sehen Sie ein Beispiel einer Richtlinie:

```shell
{
  "Rules": [
    {
      "ID": "CommVault",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transitions": [
        {
        "Days": 0,
        "StorageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

### Richtlinie dem Bucket zuordnen
{: #commvault-assign-policy}

1. Führen Sie folgenden CLI-Befehl aus:

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucketname> --lifecycle-configuration file://<gespeicherte_richtliniendatei> --endpoint <endpunkt>
```

2.	Erstellen Sie eine Speicherrichtlinie mit Simpana und ordnen Sie diese Speicherrichtlinie der Cloud Storage-Bibliothek zu, die Sie im ersten Schritt erstellt haben. Eine Speicherrichtlinie regelt die Art und Weise, in der Simpana bei Backupübertragungen mit COS interagiert. Eine Übersicht über Richtlinien finden Sie [hier](https://documentation.commvault.com/commvault/v11/article?p=13804.htm).

3.	Erstellen Sie ein Backup-Set und ordnen Sie dieses Backup-Set der Speicherrichtlinie zu, die Sie im vorherigen Schritt erstellt haben. Eine Übersicht über Backup-Sets finden Sie [hier](https://documentation.commvault.com/commvault/v11/article?p=11666.htm).

## Backups durchführen
{: #commvault-backup}

Sie können das Backup in das Bucket mit der Richtlinie initialisieren. Außerdem können Sie Backups in {{site.data.keyword.cos_full_notm}} durchführen. Weitere Informationen zu Simpana-Backups finden Sie [hier](https://documentation.commvault.com/commvault/v11/article?p=11677.htm). Die Überführung des Backupinhalts erfolgt auf der Grundlage der für das Bucket konfigurierten Richtlinie.

## Restores durchführen
{: #commvault-restore}

Sie können die Inhalte eines Backups von {{site.data.keyword.cos_full_notm}} mit Restore wiederherstellen. Weitere Informationen zu Simpana-Restores finden Sie [hier](https://documentation.commvault.com/commvault/v11/article?p=12867.htm).

### Simpana zum automatischen Durchführen eines Restore für Objekte aus dem Archive-Tier konfigurieren
{: #commvault-auto-restore}

1. Erstellen Sie eine Task, die einen {{site.data.keyword.cos_full_notm}}-Restore auslöst, wenn Sie ein Backup von COS wiederherstellen. Informationen zur Konfiguration finden Sie in der [Dokumentation zu CommVault Simpana](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).

2. Stellen Sie per Backup im Archivtier gesicherte Inhalte anhand einer Recall-Task des Cloudspeichers auf ihrem ursprünglichen Tier mit Restore wieder her. Diese Task wird ausgeführt, nachdem Simpana den Rückgabecode von {{site.data.keyword.cos_full_notm}} erhalten hat. Weitere Informationen zum Recall aus dem Archive-Tier finden Sie [hier](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).

3. Sobald die Wiederherstellung (vom Archivtier in das ursprüngliche Tier) abgeschlossen ist, liest Simpana die Inhalte und schreibt an die jeweilige ursprüngliche oder die konfigurierte Position.

