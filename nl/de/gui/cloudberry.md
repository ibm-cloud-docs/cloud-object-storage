---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup ist ein flexibles Dienstprogramm, das Benutzern ermöglicht, die Gesamtheit oder einen Teil eines lokalen Dateisystems auf einem mit der S3-API kompatiblen Objektspeichersystem zu sichern. Für Windows, MacOS und Linux sind kostenlose Testversionen sowie kostenpflichtige Versionen für professionelle Anwender verfügbar, die eine Reihe beliebter Cloudspeicherservices einschließlich {{site.data.keyword.cos_full}} unterstützen. Cloudberry Backup kann von der Website [cloudberrylab.com](https://www.cloudberrylab.com/) heruntergeladen werden.

Cloudberry Backup enthält zahlreiche nützliche Funktionen, darunter auch die folgenden:

* Planung
* Inkrementelle Backups & Backups auf Blockebene
* Befehlszeilenschnittstelle
* E-Mail-Benachrichtigungen
* Komprimierung (*nur bei der zahlungspflichtigen Version für professionelle Anwender*)

## Cloudberry Explorer
{: #cloudberry-explorer}

Dieses neue Produkt von Cloudberry Labs bietet eine vertraute Benutzerschnittstelle zur Dateiverwaltung für {{site.data.keyword.cos_short}}. [Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} gibt es ebenfalls als kostenlose Testversion sowie als kostenpflichtige Version für professionelle Anwender, ist gegenwärtig aber nur für Windows verfügbar. Zu den wichtigsten Funktionen von Cloudberry Explorer zählen die folgenden:

* Synchronisation von Ordnern/Buckets
* Befehlszeilenschnittstelle
* ACL-Management
* Kapazitätsberichte

Die kostenpflichtige Version für professionelle Anwender schließt darüber hinaus die folgenden Funktionen ein:
* Suche 
* Verschlüsselung/Komprimierung
* Wiederaufnahmefähiger Upload
* Unterstützung von FTP/SFTP

## Cloudberry in Verbindung mit Object Storage verwenden
{: #cloudberry-cos}

Bei der Konfiguration von Cloudberry-Produkten für die Zusammenarbeit mit {{site.data.keyword.cos_short}} sollten Sie die folgenden wichtigen Punkte berücksichtigen:

* Wählen Sie in der Optionsliste die Option `S3-kompatibel` aus
* Gegenwärtig werden nur [HMAC-Berechtigungsnachweise](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) unterstützt
* Für jedes Bucket ist eine eigene (getrennte) Verbindung erforderlich
* Stellen Sie sicher, dass der in der Verbindung angegebene `Endpunkt` mit der Region des ausgewählten Buckets übereinstimmt (denn *andernfalls schlägt das Backup wegen fehlendem Zugriff auf das Ziel fehl*). Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
