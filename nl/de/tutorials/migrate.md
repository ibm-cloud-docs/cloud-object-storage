---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# Daten von OpenStack Swift migrieren
{: #migrate}

Bevor {{site.data.keyword.cos_full_notm}} als {{site.data.keyword.cloud_notm}}-Plattformservice verfügbar wurde, wurde für Projekte, für ein Objektspeicher erforderlich war, [OpenStack Swift](https://docs.openstack.org/swift/latest/) oder [OpenStack Swift (Infrastructure)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift) verwendet. Entwicklern wird empfohlen, ihre Anwendungen zu aktualisieren und ihre Daten auf {{site.data.keyword.cloud_notm}} zu migrieren, um von den Vorteilen der von IAM und Key Protect bereitgestellten neuen Zugriffssteuerung und Verschlüsselung sowie weiteren neue Funktionen zu profitieren, sobald diese verfügbar werden.

Das Konzept eines Containers in Swift ist mit dem eines Buckets in COS identisch. In COS ist die Anzahl der Serviceinstanzen auf 100 Buckets begrenzt, für manche Swift-Instanzen ist eine größere Anzahl an Containern zulässig. In COS-Buckets können Milliarden an Objekten enthalten sein und Schrägstriche (`/`) werden in Objektnamen für verzeichnisähnliche 'Präfixe' beim Organisieren der Daten unterstützt. Von COS werden IAM-Richtlinien auf Bucket- und Serviceinstanzebene unterstützt.
{:tip}

Eine Methode zum Migrieren von Daten zwischen Objektspeicherservices besteht darin, ein Tool zum Synchronisieren (sync) oder Klonen (clone) zu verwenden, zum Beispiel das [Open Source-Befehlszeilendienstprogramm `rclone`](https://rclone.org/docs/). Von diesem Dienstprogramm wird eine Dateibaumstruktur zwischen zwei Standorten einschließlich Cloudspeicher synchronisiert. Wenn von `rclone` Daten in COS geschrieben werden, wird die COS/S3-API zum Segmentieren großer Objekte und parallelen Hochladen von Teilen abhängig von den Größen und Schwellenwerten verwendet, die als Konfigurationsparameter festgelegt wurden.

Zwischen COS und Swift bestehen einige Unterschiede, die im Rahmen einer Datenmigration berücksichtigt werden müssen.

  - Von COS werben bisher weder Ablaufrichtlinien noch eine Versionssteuerung unterstützt. Von Workflows, die von solchen Swift-Funktionen abhängen, muss dies bei der Migration in COS stattdessen im Rahmen der Anwendungslogik verarbeitet werden.
  - Von COS werden Metadaten auf Objektebene unterstützt, diese Informationen werden jedoch nicht beibehalten, wenn `rclone` zum Migrieren der Daten verwendet wird. Angepasste Metadaten können in COS für Objekte mithilfe des Headers `x-amz-meta-{key}: {value}` festgelegt werden, es wird jedoch empfohlen, Metadaten auf Objektebene vor der Verwendung von `rclone` in einer Datenbank zu sichern. Angepasste Metadaten können auf vorhandene Objekte durch [das Kopieren des Objekts an die Position desselben Objekts](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object) angewendet werden; vom System wird in diesem Fall erkannt, dass die Objektdaten identisch sind und nur die Metadaten werden aktualisiert. Beachten Sie, dass von `rclone` Zeitmarken beibehalten werden **können**.
  - Von COS werden IAM-Richtlinien für die Zugriffsteuerung auf Serviceinstanz- und Bucketebene verwendet. [Objekte können öffentlich verfügbar gemacht werden](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access), wenn Sie die Zugriffssteuerungsliste `public-read` festlegen, sodass kein Berechtigungsheader erforderlich ist.
  - [Mehrteilige Uploads](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) für große Objekte werden in der COS/S3-API anders als in der Swift-API verarbeitet.
  - In COS sind gängige optionale HTTP-Header wie `Cache-Control`, `Content-Encoding`, `Content-MD5` oder `Content-Type` zulässig.

In diesem Handbuch werden Anweisungen zum Migrieren von Daten aus einem einzelnen Swift-Container in ein einzelnes COS-Bucket bereitgestellt. Dies muss für alle Container wiederholt werden, die Sie migrieren möchten; anschließend muss die Anwendungslogik zur Verwendung der neuen API aktualisiert werden. Nach der Migration der Daten können Sie die Integrität des Transfers mit `rclone check` überprüfen; hierbei werden MD5-Kontrollsummen verglichen und eine Liste aller Objekte erstellt, die nicht identisch sind.


## {{site.data.keyword.cos_full_notm}} konfigurieren
{: #migrate-setup}

  1. Wenn Sie bisher noch keine Instanz erstellt haben, stellen Sie eine Instanz von {{site.data.keyword.cos_full_notm}} aus dem [Katalog](https://cloud.ibm.com/catalog/services/cloud-object-storage) bereit.
  2. Erstellen Sie alle Buckets, die Sie zum Speichern der übertragenen Daten benötigen. Lesen Sie die [Anleitung zur Einführung](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) durch, um sich mit den zentralen Konzepten wie [Endpunkten](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) und [Speicherklassen](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes) vertraut zu machen.
  3. Da die Syntax der Swift-API erheblich von der COS/S3-API abweicht, kann es erforderlich sein, die Anwendung zu refaktorieren, damit in den COS-SDKs äquivalente Methoden verwendet werden. Bibliotheken sind in [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) oder in der [REST-API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) verfügbar.

## Rechenressource zum Ausführen des Migrationstools konfigurieren
{: #migrate-compute}
  1. Wählen Sie eine Linux-, Mac OS- oder BSD-Maschine, einen Bare-Metal-Server der IBM Cloud-Infrastruktur oder einen virtuellen Server mit dem kürzesten Abstand zu Ihren Daten aus. Folgende Serverkonfiguration wird empfohlen: 32 GB RAM, Prozessor mit 2 - 4 Kernen und eine Geschwindigkeit von 1.000 MB/s in einem privaten Netz.  
  2. Wenn Sie die Migration auf einem Bare-Metal-Server der IBM Cloud-Infrastruktur oder einem virtuellen Server durchführen, verwenden  Sie die **privaten** Swift- und COS-Endpunkte.
  3. Verwenden Sie andernfalls die **öffentlichen** Swift- und COS-Endpunkte.
  4. Installieren Sie `rclone` unter Verwendung eines [Paketmanagers oder einer vorkompilierten Binärdatei](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## `rclone` für OpenStack Swift konfigurieren
{: #migrate-rclone}

  1. Erstellen Sie eine `rclone`-Konfigurationsdatei in `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Erstellen Sie die Swift-Quelle; kopieren Sie hierzu den unten folgenden Abschnitt und fügen Sie ihn in `rclone.conf` ein.

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. Rufen Sie den Berechtigungsnachweis für OpenStack Swift ab.
    <br>a. Klicken Sie auf die Swift-Instanz im [Dashboard der IBM Cloud-Konsole](https://cloud.ibm.com/).
    <br>b. Klicken Sie auf die **Serviceberechtigungsnachweise** im Navigationsfenster.
    <br>c. Klicken Sie auf **Neuer Berechtigungsnachweis**, um Berechtigungsinformationen zu generieren. Klicken Sie auf **Hinzufügen**.
    <br>d. Zeigen Sie den erstellten Berechtigungsnachweis an und kopieren Sie den JSON-Inhalt.

  4. Füllen Sie die folgenden Felder aus:

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. Fahren Sie mit dem Abschnitt zum Konfigurieren von `rclone` für COS fort.


## `rclone` für OpenStack Swift (Infrastructure) konfigurieren
{: #migrate-config-swift}

  1. Erstellen Sie eine `rclone`-Konfigurationsdatei in `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Erstellen Sie die Swift-Quelle; kopieren Sie hierzu den unten folgenden Abschnitt und fügen Sie ihn in `rclone.conf` ein.

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. Rufen Sie den Berechtigungsnachweis für OpenStack Swift (Infrastructure) ab.
    <br>a. Klicken Sie auf das Swift-Konto im Kundenportal der IBM Cloud-Infrastruktur.
    <br>b. Klicken Sie auf das Rechenzentrum des Migrationsquellencontainers.
    <br>c. Klicken Sie auf **Berechtigungsnachweise anzeigen**.
    <br>d. Kopieren Sie die folgenden Angaben.
      <br>&nbsp;&nbsp;&nbsp;**Benutzername**
      <br>&nbsp;&nbsp;&nbsp;**API-Schlüssel**
      <br>&nbsp;&nbsp;&nbsp;**Authentifizierungsendpunkt** abhängig von dem Standort, an dem das Migrationstool ausgeführt wird

  4. Füllen Sie unter Verwendung des Berechtigungsnachweises für OpenStack Swift (Infrastructure) die folgenden Felder aus:

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## `rclone` für COS konfigurieren
{: #migrate-config-cos}

### COS-Berechtigungsnachweis abrufen
{: #migrate-config-cos-credential}

  1. Klicken Sie auf die COS-Instanz in der IBM Cloud-Konsole.
  2. Klicken Sie auf die **Serviceberechtigungsnachweise** im Navigationsfenster.
  3. Klicken Sie auf **Neuer Berechtigungsnachweis**, um Berechtigungsinformationen zu generieren.
  4. Fügen Sie unter **Inline-Konfigurationsparameter** die Zeichenfolge `{"HMAC":true}` hinzu. Klicken Sie auf **Hinzufügen**.
  5. Zeigen Sie den erstellten Berechtigungsnachweis an und kopieren Sie den JSON-Inhalt.

### COS-Endpunkt abrufen
{: #migrate-config-cos-endpoint}

  1. Klicken Sie in der Navigationsanzeige auf **Buckets**.
  2. Klicken Sie auf das Bucket für das Migrationsziel.
  3. Klicken Sie in der Navigationsanzeige auf **Konfiguration**.
  4. Blättern Sie nach unten zum Abschnitt **Endpunkte** und wählen Sie die Endpunkte auf der Basis des Standorts aus, an dem das Migrationstool ausgeführt wird.

  5. Erstellen Sie das COS-Ziel; kopieren Sie hierzu den unten folgenden Abschnitt und fügen Sie ihn in `rclone.conf` ein.

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. Füllen Sie unter Verwendung des COS-Berechtigungsnachweises und -Endpunkts die folgenden Felder aus:

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## Ordnungsgemäße Konfiguration von Migrationsquelle und -ziel überprüfen
{: #migrate-verify}

1. Listen Sie den Swift-Container auf, um zu überprüfen, ob `rclone` ordnungsgemäß konfiguriert ist.

    ```
    rclone lsd SWIFT:
    ```

2. Listen Sie den COS-Bucket auf, um zu überprüfen, ob `rclone` ordnungsgemäß konfiguriert ist.

    ```
    rclone lsd COS:
    ```

## `rclone` ausführen
{: #migrate-run}

1. Führen Sie einen Probelauf (ohne Kopieren der Daten) für `rclone` durch, um die Objekte im Swift-Quellcontainer (zum Beispiel `swift-test`) mit dem COS-Zielbucket (zum Beispiel `cos-test`) zu synchronisieren.

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. Stellen Sie sicher, dass die Dateien, die Sie migrieren möchten, in der Befehlsausgabe angezeigt werden. Wenn alles Ihren Erwartungen entspricht, entfernen Sie das Flag `--dry-run` und fügen das Flag `-v` zum Kopieren der Daten hinzu. Wenn Sie das optionale Flag `--checksum` verwenden, wird verhindert, dass die Dateien aktualisiert werden, die an beiden Positionen über denselben MD5-Hashwert und dieselbe Objektgröße verfügen.

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   Sie müssen versuchen, die Werte für CPU, Hauptspeicher und Netz auf der Maschine zu maximieren, auf der 'rclone' ausgeführt wird, um die Übertragungszeit zu verbessern. Zum Optimieren von 'rclone' stehen noch einige weitere Parameter zur Verfügung:

   --checkers int  Number of checkers to run in parallel. (default 8)
   Hierbei handelt es sich um die Anzahl der aktiven Threads für den Vergleich der Kontrollsummen. Es wird empfohlen, diesen Wert auf mindestens 64 zu erhöhen.

   --transfers int Number of file transfers to run in parallel. (default 4)
   Hierbei handelt es sich um die Anzahl der Objekte, die parallel übertragen werden können. Es wird empfohlen, diesen Wert auf 64 bis 128 oder einen höheren Wert zu erhöhen.

   --fast-list Use recursive list if available. Uses more memory but fewer transactions.
   Verwenden Sie diese Option, um die Leistung zu verbessern; die Anzahl der Anforderungen, die zum Kopieren eines Objekts erforderlich sind, wird so reduziert.

Im Verlauf einer Migration mit `rclone` werden die Quellendaten kopiert, aber nicht gelöscht.
{:tip}


3. Wiederholen Sie die Schritte für alle anderen Container, für die eine Migration erforderlich ist.
4. Wenn alle Daten kopiert wurden und Sie sichergestellt haben, dass von der Anwendung auf die Daten in COS zugegriffen werden kann, löschen Sie die Swift-Serviceinstanz.
