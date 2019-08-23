---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# Dateien mit Cyberduck übertragen
{: #cyberduck}

Cyberduck ist ein gängiger, quelloffener und benutzerfreundlicher Browser für Cloudobjektspeicher für Mac und Windows. Cyberduck ist in der Lage, die richtigen Autorisierungssignaturen zu berechnen, die für die Herstellung einer Verbindung mit IBM COS erforderlich sind. Cyberduck kann von der Website [cyberduck.io/](https://cyberduck.io/){: new_window} heruntergeladen werden.

Wenn Sie mit Cyberduck eine Verbindung zu IBM COS herstellen und einen Ordner mit lokalen Dateien mit einem Bucket synchronisieren möchten, führen Sie die folgenden Schritte aus:

 1. Laden Sie Cyberduck herunter, installieren Sie den Browser und starten Sie ihn.
 2. Es wird das Hauptfenster der Anwendung geöffnet, in dem Sie eine Verbindung zu IBM COS herstellen können. Klicken Sie auf **Verbindung öffnen**, um eine Verbindung zu IBM COS zu konfigurieren.
 3. Ein Popup-Fenster wird geöffnet. Wählen Sie im Dropdown-Menü oben die Option `S3 (HTTPS)` aus. Machen Sie für die nachfolgend genannten Felder die entsprechenden Angaben und klicken Sie dann auf **Verbinden**:

    * `Server`: Geben Sie den Endpunkt für IBM COS ein.
        * Stellen Sie sicher, dass die Region des Endpunkts mit dem beabsichtigten Zielbucket übereinstimmt. Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Zugriffsschlüssel-ID`
    * `Geheimer Zugriffsschlüssel`
    * `Zur Schlüsselkette hinzufügen`: Speichern Sie die Verbindung in der Schlüsselkette, um ihre anwendungsübergreifende Verwendung zu ermöglichen *(optional)*.

 4. Cyberduck führt Sie direkt zum Stammverzeichnis des Kontos, wo Sie Buckets erstellen können.
    * Klicken Sie mit der rechten Maustaste auf eine beliebige Stelle im Hauptfenster und wählen Sie **Neuer Ordner** aus. (*Die Anwendung arbeitet mit zahlreichen Übertragungsprotokollen, bei denen 'Ordner' das gängigere Containerkonstrukt ist.*)
    * Geben Sie den Bucketnamen ein und klicken Sie auf 'Erstellen'.
 5. Doppelklicken Sie nach der Erstellung auf das neu erstellte Bucket, um es anzuzeigen. Im Bucket können Sie unterschiedliche Funktionen ausführen, wie zum Beispiel die folgenden:
    * Dateien in das Bucket hochladen
    * Inhalt des Buckets auflisten
    * Objekte vom Bucket herunterladen
    * Lokale Dateien mit einem Bucket synchronisieren
    * Objekte mit einem anderen Bucket synchronisieren
    * Archiv eines Buckets erstellen
 6. Klicken Sie mit der rechten Maustaste auf eine beliebige Stelle im Bucket und wählen Sie **Synchronisieren** aus. Es wird ein Popup-Fenster geöffnet, in dem Sie zu dem Ordner blättern können, der mit dem Bucket synchronisiert werden soll. Wählen Sie den gewünschten Ordner aus und klicken Sie auf 'Auswählen'.
 7. Nachdem Sie den Ordner ausgewählt haben, wird ein neues Popup-Fenster geöffnet. Dieses Fenster enthält ein Dropdown-Menü, in dem Sie die Synchronisationsoperation mit dem Bucket auswählen. Für die Synchronisierung sind im Menü die folgenden drei möglichen Optionen verfügbar:

    * `Herunterladen:` Diese Option bewirkt, dass geänderte und fehlende Objekte aus dem Bucket heruntergeladen werden.
    * `Hochladen:` Diese Option bewirkt, dass geänderte und fehlende Objekte in das Bucket hochgeladen werden.
    * `Spiegeln:` Diese Option bewirkt, dass sowohl die Download- als auch die Uploadoperation durchgeführt wird, wodurch sichergestellt wird, dass alle neuen und aktualisierten Dateien und Objekte zwischen dem lokalen Ordner und dem Bucket synchronisiert werden.

 8. Es wird ein weiteres Fenster geöffnet, in dem aktive und archivierte Übertragungsanforderungen angezeigt werden. Nachdem die Synchronisationsanforderung abgeschlossen worden ist, wird im Hauptfenster für das Bucket eine Auflistungsoperation ausgeführt, die den aktualisierten Inhalt im Bucket widerspiegelt.

## Mountain Duck
{: #mountain-duck}

Mountain Duck baut auf Cyberduck auf, um Ihnen im Finder (unter Mac) bzw. im Explorer (unter Windows) das Anhängen von Cloudobjektspeicher als Datenträger zu ermöglichen. Es sind zwar Testversionen verfügbar, doch zur fortgesetzten Verwendung ist die Verwendung eines Registrierungsschlüssels erforderlich.

Das Erstellen eines Lesezeichens in Mountain Duck hat starke Ähnlichkeit mit dem Erstellen von Verbindungen in Cyberduck:

1. Laden Sie Mountain Duck herunter, installieren Sie den Browser und starten Sie ihn.
2. Erstellen Sie ein neues Lesezeichen.
3. Wählen Sie im Dropdown-Menü die Option `S3 (HTTPS)` aus und geben Sie die folgenden Informationen ein:
    * `Server:` Geben Sie den Endpunkt für IBM COS ein. 
        * *Stellen Sie sicher, dass die Region des Endpunkts mit dem beabsichtigten Bucket übereinstimmt. Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Benutzername:` Geben Sie den Zugriffsschlüssel ein.
    * Klicken Sie auf **Verbinden**.
    * Sie werden zur Eingabe Ihres geheimen Schlüssels aufgefordert, der dann in der Schlüsselkette gespeichert wird.

Ihre Buckets sind nun im Finder oder im Explorer verfügbar. Sie können mit {{site.data.keyword.cos_short}} wie mit jedem beliebigen anderen angehängten Dateisystem interagieren.

## Befehlszeilenschnittstelle (CLI)
{: #cyberduck-cli}

Cyberduck stellt außerdem die Befehlszeilenschnittstelle (CLI) `duck` bereit, die in der Shell unter Linux, Mac OS X und Windows ausgeführt wird. Anweisungen zur Installation finden Sie auf der [Wikiseite](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window} für `duck`.

Um `duck` in Verbindung mit {{site.data.keyword.cos_full}} nutzen zu können, muss ein angepasstes Profil zum [Anwendungsunterstützungsverzeichnis](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window} hinzugefügt werden. Detaillierte Informationen zu Verbindungsprofilen für `duck` (einschließlich Beispielprofilen sowie vorkonfigurierten Profilen) enthält die [Cyberduck-Hilfe/Vorgehensweise](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window}.

Bei dem nachfolgenden Beispiel handelt es sich um ein Beispielprofil für einen regionalen COS-Endpunkt:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Wenn Sie dieses Profil zu `duck` hinzufügen, können Sie mit einem Befehl ähnlich dem unten aufgeführten auf {{site.data.keyword.cos_short}} zugreifen:

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*Schlüsselwerte*
* `<bucket-name>` - Name des COS-Buckets (*Bucket und Endpunktregionen müssen konsistent sein*)
* `<access-key>` - HMAC-Zugriffsschlüssel
* `<secret-access-key>` - geheimer HMAC-Schlüssel

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

Eine vollständige Liste der Befehlszeilenoptionen erhalten Sie, wenn Sie `duck --help` in die Shell eingeben oder aber [diese Wiki-Site](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window} besuchen.
