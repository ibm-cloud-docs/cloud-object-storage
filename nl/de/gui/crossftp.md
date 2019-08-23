---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# Dateien mit CrossFTP übertragen
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} ist ein mit vielen Funktionen ausgestatteter FTP-Client, der S3-kompatible Cloudspeicherlösungen einschließlich {{site.data.keyword.cos_full}} unterstützt. CrossFTP unterstützt Mac OS X, Microsoft Windows und Linux, ist als kostenlose Testversion sowie als kostenpflichtige Pro- und Enterprise-Version und bietet unter anderem die folgenden Funktionen:

* Schnittstelle mit Registerkarten
* Kennwortverschlüsselung
* Suche
* Batchübertragung
* Verschlüsselung (*Pro-/Enterprise-Version*)
* Synchronisation (*Pro-/Enterprise-Version*)
* Planungsfunktion (*Pro-/Enterprise-Version*)
* Befehlszeilenschnittstelle (*Pro-/Enterprise-Version*)

## Verbindung zu IBM Cloud Object Storage herstellen
{: #crossftp-connect}

1. Laden Sie CrossFTP herunter, installieren Sie den Client und starten Sie ihn.
2. Erstellen Sie im rechten Fensterbereich eine neue Site, indem Sie auf das Pluszeichen (+) klicken, um den Site-Manager zu öffnen.
3. Geben Sie auf der Registerkarte *Allgemein* Folgendes ein:
    * Legen Sie für **Protokoll** das Protokoll `S3/HTTPS` fest.
    * Legen Sie für **Bezeichnung** einen beschreibenden Namen Ihrer Wahl fest.
    * Legen Sie für **Host** einen {{site.data.keyword.cos_short}}-Endpunkt fest (wie zum Beispiel `s3.us.cloud-object-storage.appdomain.cloud`).
        * *Stellen Sie sicher, dass die Region des Endpunkts mit dem beabsichtigten Zielbucket übereinstimmt. Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * Belassen Sie den Wert für **Port** bei `443`.
    * Legen Sie für **Zugriffsschlüssel** und **Geheimer Schlüssel** HMAC-Berechtigungsnachweise mit den korrekten Zugriffsberechtigungen für Ihr Zielbucket fest.
4. Registerkarte *S3*:
    * Stellen Sie sicher, dass `DevPay verwenden` nicht ausgewählt ist.
    * Klicken Sie auf **API-Gruppe ...** und stellen Sie sicher, dass `DevPay` und `CloudFront-Distribution` nicht ausgewählt sind.
5. ***Nur für Mac OS X:***
    * Klicken Sie in der Menüleiste auf *Sicherheit > TLS-/SSL-Protokolle...*.
    * Wählen Sie die Option `Aktivierte Protokolle anpassen` aus.
    * Fügen Sie zum Feld **Aktiviert** das Protokoll `TLSv1.2` hinzu.
    * Klicken Sie auf **OK**.
6. ***Nur für Linux:***
    * Klicken Sie in der Menüleiste auf *Sicherheit > Verschlüsselungseinstellungen...*.
    * Wählen Sie die Option `Aktivierte Cipher-Suites anpassen` aus.
    * Fügen Sie `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` zum Feld **Aktiviert** hinzu.
    * Klicken Sie auf **OK**.
7. Klicken Sie auf **Anwenden** und dann auf **Schließen**.
8. Unter *Standorte* müsste jetzt ein neuer Eintrag mit der in Schritt 3 angegebenen *Bezeichnung* verfügbar sein.
9. Doppelklicken Sie auf den neuen Eintrag, um eine Verbindung zum Endpunkt herzustellen.

Ab diesem Punkt wird im Fenster eine Liste der verfügbaren Buckets angezeigt und Sie können die verfügbaren Dateien durchsuchen sowie auf und von Ihren lokalen Festplatten übertragen.
