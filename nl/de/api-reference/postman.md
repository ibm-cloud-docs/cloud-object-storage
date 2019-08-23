---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# `Postman` verwenden
{: #postman}

Im Folgenden finden Sie eine grundlegende `Postman`-Konfiguration für die {{site.data.keyword.cos_full}}-REST-API. Weiterführende Details finden Sie in der API-Referenz für [Buckets](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) oder [Objekte](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).

Die Verwendung von `Postman` erfordert Kenntnisse zum Objektspeicher. Des Weiteren benötigen Sie die erforderlichen Informationen aus einem [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) oder für die [Konsole](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Unbekannte Begriffe und Variablen finden Sie im [Glossar](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben identifiziert werden kann.
{:tip}

## REST-API-Clientübersicht
{: #postman-rest}

REST (REpresentational State Transfer) ist ein Architekturstil, der einen Standard für Computersysteme bei der Interaktion mit anderen Systemen über das Netz bereitstellt. Dabei werden normalerweise standardmäßige HTTP-URLs und Verben (GET, PUT, POST etc.) verwendet, die von allen wichtigen Entwicklungssprachen und -plattformen unterstützt werden. Die Interaktion mit einer REST-API ist allerdings nicht so einfach, wie dies die Benutzung eines standardmäßigen Internet-Browsers ist. In einfachen Browsern können URL-Anforderungen nicht bearbeitet werden. Hierzu kann ein REST-API-Client eingesetzt werden.

Ein REST-API-Client stellt eine einfache grafisch orientierte Anwendung bereit, die als Schnittstelle zu einer bereits vorhandenen REST-API-Bibliothek dient. Ein guter Client vereinfacht das Testen, Entwickeln und das Dokumentieren von APIs, indem er Benutzern die schnelle Zusammenstellung einfacher und auch komplexer HTTP-Anforderungen ermöglicht. Postman ist ein hervorragender REST-API-Client, der eine vollständige API-Entwicklungsumgebung bereitstellt, die integrierte Tools zum Entwerfen und Simulieren, Debuggen, Testen, Dokumentieren, Überwachen und Veröffentlichen von APIs enthält. Des Weiteren stellt das Produkt hilfreiche Funktionen zur Erfassung von Daten und für Arbeitsbereiche zur Verfügung, die die Zusammenarbeit entscheidend vereinfachen. 

## Voraussetzungen
{: #postman-prereqs}
* IBM Cloud-Konto
* [Erstellte Cloudspeicherressource](https://cloud.ibm.com/catalog/) (Lite-Plan/kostenloser Plan ausreichend)
* [IBM Cloud-Befehlszeilenschnittstelle installiert und konfiguriert](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [Serviceinstanz-ID für Ihren Cloudspeicher](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [IAM-Token (IAM = Identity and Access Management)](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [Endpunkt für COS-Bucket](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### Bucket erstellen
{: #postman-create-bucket}
1.	Postman starten
2.	Wählen Sie auf der Registerkarte 'Neu' in der Dropdown-Liste den Eintrag `PUT` aus.
3.	Geben Sie den Endpunkt in der Adressleiste ein und fügen Sie den Namen Ihres neuen Buckets hinzu.
a.	Bucketnamen müssen für alle Buckets eindeutig sein. Wählen Sie also einen kennzeichnenden Namen aus.
4.	Wählen Sie in der Dropdown-Liste 'Typ' den Eintrag für das Trägertoken aus.
5.	Fügen Sie das IAM-Token im Feld 'Token' hinzu.
6.	Klicken Sie auf die Option zum Voranzeigen der Anforderung.
a.	Daraufhin wird eine Bestätigungsnachricht angezeigt, in der Sie darüber informiert werden, dass die Header hinzugefügt wurden.
7.	Klicken Sie auf die Registerkarte 'Header', auf der ein bereits bestehender Eintrag für die Berechtigung (Authorization) angezeigt wird.
8.	Fügen Sie einen neuen Schlüssel hinzu.
a.	Schlüssel: `ibm-service-instance-id`
b.	Wert: Ressourceninstanz-ID für Ihren Cloudspeicherservice.
9.	Klicken Sie auf 'Senden'.
10.	Sie erhalten die Statusnachricht `200 OK`.

### Neue Textdatei erstellen
{: #postman-create-text-file}

1.	Erstellen Sie eine neue Registerkarte, indem Sie auf das Plussymbol (+) klicken.
2.	Wählen Sie in der Liste `PUT` aus.
3.	Geben Sie in der Adressleiste die Endpunktadresse mit dem Bucketnamen aus dem vorherigen Abschnitt und einen Dateinamen ein.
4.	Wählen Sie in der Liste 'Typ' den Eintrag für das Trägertoken aus.
5.	Fügen Sie das IAM-Token im Feld 'Token' hinzu.
6.	Wählen Sie die Registerkarte 'Hauptteil' aus.
7.	Wählen Sie die Option 'raw' aus und vergewissern Sie sich, dass 'Text' ausgewählt wurde.
8.	Geben Sie im vorgesehenen Bereich den gewünschten Text ein.
9.	Klicken Sie auf 'Senden'.
10.	Sie erhalten die Statusnachricht `200 OK`.

### Inhalte eines Buckets auflisten
{: #postman-list-objects}

1.	Erstellen Sie eine neue Registerkarte, indem Sie das Plussymbol (+) auswählen.
2.	Vergewissern Sie sich, dass in der Liste `GET` ausgewählt ist.
3.	Geben Sie in der Adressleiste die Endpunktadresse mit dem Bucketnamen aus dem vorherigen Abschnitt ein.
4.	Wählen Sie in der Liste 'Typ' die Option für das Trägertoken aus.
5.	Fügen Sie das IAM-Token im Feld 'Token' hinzu.
6.	Klicken Sie auf 'Senden'.
7.	Sie erhalten die Statusnachricht `200 OK`.
8.	Im Hauptteil des Abschnitts 'Antwort' wird eine XML-Nachricht mit der Liste der Dateien in Ihrem Bucket angezeigt.

## Beispiel für Datensammlung verwenden
{: #postman-collection}

Sie können eine Postman-Datensammlung [herunterladen ![Symbol für externen Link](../icons/launch-glyph.svg "Symbol für externen Link")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window}, in der konfigurierbare Beispiele für {{site.data.keyword.cos_full}}-API-Anforderungen enthalten sind.

### Datensammlung in Postman importieren
{: #postman-import-collection}

1. Klicken Sie in Postman auf die Schaltfläche zum Importieren, die sich rechts oben in der Anzeige befindet.
2. Importieren Sie die Datenerfassungsdatei mit einer der folgenden Methoden:
    * Ziehen Sie die Datenerfassungsdatei vom Fenster für den Import in das Fenster **Dateien hier ablegen** und legen Sie sie dort ab.
    * Klicken Sie auf die Schaltfläche zum Auswählen von Dateien auswählen und navigieren Sie zu dem Ordner und wählen Sie die Datenerfassungsdatei aus.
3. Daraufhin wird *IBM COS* im Datenerfassungsfenster angezeigt.
4. Erweitern Sie die Ansicht der Datenerfassung, sodass die zwanzig (20) Beispielanforderungen angezeigt werden.
5. Die Datenerfassung umfasst sechs (6) Variablen, die gesetzt werden müssen, damit die API-Anforderungen erfolgreich ausgeführt werden können.
    * Klicken Sie auf die drei Punkte rechts neben der Datenerfassung, um das Menü zu erweitern, und klicken Sie dann auf 'Bearbeiten'.
6. Bearbeiten Sie die Variablen, um sie auf Ihre Cloudspeicherumgebung abzustimmen.
    * **bucket** - Geben Sie den Namen des neuen Buckets ein, den Sie erstellen wollen (Bucketnamen müssen innerhalb des Cloudspeichers eindeutig sein).
    * **serviceid** - Geben Sie den CRN Ihres Cloudspeicherservice ein. Anweisungen zum Abrufen des CRN stehen [hier](/docs/overview?topic=overview-crn) zur Verfügung.
    * **iamtoken** - Geben Sie das OAUTH-Token für Ihren Cloudspeicherservice ein. Anweisungen zum Abrufen des OAUTH-Tokens stehen [hier](/docs/services/key-protect?topic=key-protect-retrieve-access-token) zur Verfügung.
    * **endpoint** - Geben Sie den regionalen Endpunkt für Ihren Cloudspeicherservice ein. Rufen Sie die verfügbaren Endpunkte aus dem [IBM Cloud-Dashboard](https://cloud.ibm.com/resources/){:new_window} ab.
        * *Vergewissern Sie sich, dass der ausgewählte Endpunkt mit dem Key Protect-Service übereinstimmt, um sicherzustellen, dass die Beispiele korrekt ausgeführt werden können.*
    * **rootkeycrn** - Der CRN des Rootschlüssels, der im primären Key Protect-Service erstellt wurde.
        * Der CRN hat folgendes Format:<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *Vergewissern Sie sich, dass der ausgewählte Key Protect-Service mit der Region des Endpunkts übereinstimmt.*
    * **bucketlocationvault** - Geben Sie den Standortbedingungswert für die Bucketerstellung der API-Anforderung *Neues Bucket erstellen (andere Speicherklasse)* an.
        * Folgende Werte sind zulässig:
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. Klicken Sie auf die Option zum Aktualisieren.

### Beispiele ausführen
{: #postman-samples}
Die API-Beispielanforderungen sind relativ einfach und benutzerfreundlich. Sie sind so konzipiert, dass sie in einer bestimmten Reihenfolge ausgeführt werden, und zeigen, wie Sie mit Cloud Storage interagieren können. Sie können auch verwendet werden, um einen Funktionstest für Ihren Cloudspeicherservice auszuführen, mit dem der ordnungsgemäße Betrieb sichergestellt werden kann.

<table>
    <tr>
        <th>Anforderung</th>
        <th>Erwartetes Ergebnis</th>
        <th>Testergebnisse</th>
    </tr>
    <tr>
        <td>Liste der Buckets abrufen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil müssen Sie eine XML-Liste mit den Buckets in Ihrem Cloudspeicher angeben.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Inhalt</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Neues Bucket erstellen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Neue Textdatei erstellen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Neue Binärdatei erstellen</td>
        <td>
            <ul>
                <li>
                    Klicken Sie auf den Hauptteil und dann auf 'Datei auswählen', um ein Image auszuwählen, das hochgeladen werden soll.
                </li>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Liste der Dateien aus Bucket abrufen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil der Antwort werden die beiden Dateien angezeigt, die Sie in den vorherigen Anforderungen erstellt haben.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Liste der Dateien aus Bucket abrufen (Filterung nach Präfix)</td>
        <td>
            <ul>
                <li>Ändern Sie den Wert für 'querystring' in 'prefix=&lt;gewünschter Text&gt;'.</li>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil der Antwort werden die Dateien mit Namen angezeigt, die mit dem angegebenen Präfix beginnen.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Textdatei abrufen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil der Antwort wird der Text angezeigt, den Sie in der vorherigen Anforderung eingegeben haben.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Hauptteilinhalt</li>
                <li>Antwort enthält erwarteten Header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Binärdatei abrufen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil der Antwort wird das Image angezeigt, das Sie in der vorherigen Anforderung ausgewählt haben.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Header</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Liste der fehlgeschlagenen mehrteiligen Uploads abrufen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil der Antwort werden alle fehlgeschlagenen mehrteiligen Uploads für das Bucket angezeigt.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Inhalt</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Liste der fehlgeschlagenen mehrteiligen Uploads abrufen (Filterung nach Name)</td>
        <td>
            <ul>
                <li>Ändern Sie den Wert für 'querystring' in 'prefix=&lt;gewünschter Text&gt;'.</li>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil der Antwort werden alle fehlgeschlagenen mehrteiligen Uploads für das Bucket angezeigt, deren Namen mit dem angegebenen Präfix beginnen.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Inhalt</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>CORS-fähiges Bucket festlegen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>CORS-Konfiguration für Bucket abrufen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
                <li>
                    Im Hauptteil der Antwort wird die CORS-Konfiguration angezeigt, die für das Bucket festgelegt wurde.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
                <li>Antwort enthält erwarteten Inhalt</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>CORS-Konfiguration für Bucket löschen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Textdatei löschen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Binärdatei löschen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Bucket löschen</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Neues Bucket erstellen (andere Speicherklasse)</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Bucket löschen (andere Speicherklasse)</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Neues Bucket erstellen (Key Protect)</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Bucket löschen (Key Protect)</td>
        <td>
            <ul>
                <li>Statuscode 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Anforderung war erfolgreich</li>
            </ul>
        </td>                
    </tr>
</table>

## Postman Collection Runner verwenden
{: #postman-runner}

Der Postman Collection Runner stellt eine Benutzerschnittstelle zum Testen einer Datenerfassung bereit und ermöglicht Ihnen die gleichzeitige Ausführung aller Anforderungen in einer Datenerfassung. 

1. Klicken Sie auf die Schaltfläche für den Runner oben rechts im Postman-Hauptfenster.
2. Wählen Sie im Runner-Fenster die IBM COS-Datenerfassung aus und klicken Sie dann auf die große blaue Schaltfläche zum **Ausführen von IBM COS** unten in der Anzeige.
3. Im Collection Runner-Fenster werden die Iterationen während der Ausführung der Anforderungen angezeigt. Die Testergebnisse werden jeweils unterhalb der jeweiligen Anforderungen angezeigt.
    * In der **Ausführungszusammenfassung** wird eine Rasteransicht der Anforderungen angezeigt. Dort können Sie die Ergebnisse filtern.
    * Sie können auch auf die Option zum **Exportieren der Ergebnisse** klicken, um die Ergebnisse in einer JSON-Datei zu speichern.
