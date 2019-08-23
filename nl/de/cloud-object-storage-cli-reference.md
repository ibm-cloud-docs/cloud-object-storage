---

copyright:
  years: 2017, 2018, 2019
lastupdated: "26-06-2019"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:note: .note}

# IBM Cloud-Befehlszeilenschnittstelle verwenden
{: #ic-use-the-ibm-cli}

Das Cloud Object Storage-Plug-in erweitert die IBM Cloud-Befehlszeilenschnittstelle (IBM Cloud CLI) mit einem API-Wrapper für die Arbeit mit Object Storage-Ressourcen.

## Voraussetzungen
{: #ic-prerequisites}
* Ein [IBM Cloud](https://cloud.ibm.com/)-Konto
* Eine Instanz von [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision)
* Die [IBM Cloud-Befehlszeilenschnittstelle](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## Installation und Konfiguration
{: #ic-installation}

Das Plug-in ist mit Betriebssystemversionen von Windows, Linux und Mac OS kompatibel, die auf 64-Bit-Prozessoren ausgeführt werden.

Führen Sie zum Installieren des Plug-ins den Befehl `plugin install` aus.

```
ibmcloud plugin install cloud-object-storage
```

Nachdem das Plug-in installiert ist, können Sie es mit dem Befehl [`ibmcloud cos config`](#configure-the-program) konfigurieren. Diesen Befehl können Sie verwenden, um unter anderem das Plug-in mit Ihren Berechtigungsnachweisen zu belegen, die Standarddownloadposition festzulegen, Ihre Authentifizierung auszuwählen usw.

Das Programm bietet außerdem die Möglichkeit, das lokale Standardverzeichnis für heruntergeladene Dateien festzulegen sowie eine Standardregion festzulegen. Sie legen die Standarddownloadposition fest, indem Sie `ibmcloud cos config ddl` gefolgt von einem gültigen Dateipfad in das Programm eingeben. Wenn Sie eine Standardregion festlegen möchten, geben Sie `ibmcloud cos config region` ein und geben Sie dann einen Regionscode als Eingabe für das Programm an, wie zum Beispiel `us-south`. Standardmäßig ist für diese Angabe der Wert `us-geo` festgelegt.


Falls Sie die IAM-Authentifizierung verwenden, ist es für manche Befehle erforderlich, einen Cloudressourcennamen (CRN) anzugeben. Geben Sie zum Festlegen des CRNs den Befehl `ibmcloud cos config crn` ein und geben Sie Ihren CRN an. Bei Bedarf können Sie den CRN mit dem Befehl `ibmcloud resource service-instance INSTANZNAME` ermitteln. Alternativ können Sie auch die webbasierte Konsole öffnen, in der Seitenleiste **Serviceberechtigungsnachweise** auswählen und eine neue Gruppe von Berechtigungsnachweisen erstellen (oder eine vorhandene Berechtigungsnachweisdatei anzeigen, die Sie bereits erstellt haben).

Sie können Ihre aktuellen Berechtigungsnachweise für Cloud Object Storage durch Eingabe von `ibmcloud cos config list` abrufen. Da die Konfigurationsdatei vom Plug-in generiert wird, ist es ratsam, die Datei nicht manuell zu bearbeiten.

### HMAC-Berechtigungsnachweise
{: #ic-hmac-credentials}

Auf Wunsch können Sie an Stelle des API-Schlüssels die [HMAC-Berechtigungsnachweise einer Service-ID](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac) verwenden. Führen Sie in diesem Fall `ibmcloud cos config hmac` aus, um die HMAC-Berechtigungsnachweise einzugeben, und wechseln Sie dann durch Eingabe von `ibmcloud cos config auth` die Berechtigungsmethode.

Wenn Sie die Tokenauthentifizierung in Verbindung mit Ihrem eigenen API-Schlüssel verwenden möchten, ist keine Eingabe von Berechtigungsnachweisen erforderlich, denn das Programm authentifiziert Sie automatisch.
{: note}

Sie können jederzeit zwischen HMAC- und IAM-Authentifizierung wechseln, indem Sie `ibmcloud cos config auth` eingeben. Weitere Informationen zur Authentifizierung und zu Berechtigungen in IBM Cloud enthält die [Dokumentation zu Identity and Access Management (IAM)](/docs/iam?topic=iam-iamoverview).

## Befehlsindex
{: #ic-command-index}

| Befehle |  |  |
| --- | --- | --- |
| [`abort-multipart-upload`](#abort-a-multipart-upload) | [`complete-multipart-upload`](#complete-a-multipart-upload) | [`config`](#configure-the-program) |
| [`copy-object`](#copy-object-from-bucket) | [`create-bucket`](#create-a-new-bucket) | [`create-multipart-upload`](#create-a-new-multipart-upload) |
| [`delete-bucket`](#delete-an-existing-bucket) | [`delete-bucket-cors`](#delete-bucket-cors) | [`delete-object`](#delete-an-object) |
| [`delete-objects`](#delete-multiple-objects) | [`download`](#download-objects-using-s3manager) | [`get-bucket-class`](#get-a-buckets-class) | 
| [`get-bucket-cors`](#get-bucket-cors) | [`get-bucket-location`](#find-a-bucket) | [`get-object`](#download-an-object) |
| [`head-bucket`](#get-a-buckets-headers) | [`head-object`](#get-an-objects-headers) | [`list-buckets`](#list-all-buckets) | 
| [`list-buckets-extended`](#extended-bucket-listing) | [`list-multipart-uploads`](#list-in-progress-multipart-uploads) | [`list-objects`](#list-objects) |
| [`list-parts`](#list-parts) | [`put-bucket-cors`](#set-bucket-cors) | [`put-object`](#upload-an-object) |
| [`upload`](#upload-objects-using-s3manager) | [`upload-part`](#upload-a-part) | [`upload-part-copy`](#upload-a-part-copy) |
| [`wait`](#wait) |  |  |

Jede der unten aufgeführten Operationen ist mit einer Erläuterung ihrer Funktion und Verwendungsweise versehen und mit einer Auflistung aller optionaler oder erforderlicher Parameter. Sofern nicht ausdrücklich als optional vermerkt, handelt es sich bei allen aufgelisteten Parametern um obligatorische Parameter, deren Angabe verbindlich ist.

Das Plug-in für die Befehlszeilenschnittstelle unterstützt nicht die vollständige Suite der in Object Storage verfügbaren Funktionen, wie Aspera-Hochgeschwindigkeitsübertragung, Immutable Object Storage, die Erstellung von Key Protect-Buckets oder Bucket-Firewalls.
{: note}

### Mehrteiligen Upload abbrechen
{: #ic-abort-multipart-upload}
* **Aktion:** Abbrechen der Instanz eines mehrteiligen Uploads durch Beenden des Uploads in das Bucket im IBM Cloud Object Storage-Konto des Benutzers.
* **Syntax:** `ibmcloud cos abort-multipart-upload --bucket BUCKETNAME --key SCHLÜSSEL --upload-id ID [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* Die Upload-ID, die den mehrteiligen Upload angibt.
		* Flag: `--upload-id ID`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Mehrteiligen Upload ausführen
{: #ic-complete-multipart-upload}
* **Aktion:** Ausführen der Instanz eines mehrteiligen Uploads durch Assemblieren der gegenwärtig hochgeladenen Teile und Hochladen der Datei in das Bucket im IBM Cloud Object Storage-Konto des Benutzers.
* **Syntax:** `ibmcloud cos complete-multipart-upload --bucket BUCKETNAME --key SCHLÜSSEL --upload-id ID --multipart-upload STRUKTUR [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* Die Upload-ID, die den mehrteiligen Upload angibt.
		* Flag: `--upload-id ID`
	* Die STRUKTUR für MultipartUpload, die festgelegt werden soll.
		* Flag: `--multipart-upload STRUKTUR`
		* Syntax der Kurzform:  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* JSON-Syntax:  
	`--multipart-upload file://<filename.json>`  
	Der Befehl `--multipart-upload` erfordert eine JSON-Struktur, die diejenigen Teile des mehrteiligen Uploads beschreibt, aus denen die vollständige Datei neu assembliert werden soll. In diesem Beispiel wird das Präfix `file://` zum Laden der JSON-Struktur aus der angegebenen Datei verwendet.
		```
			{
  			"Parts": [
    			{
     			 "ETag": "string",
     			 "PartNumber": integer
    			}
    			...
  				]
			}
		```
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


## Mehrteilige Uploads manuell steuern
{: #ic-manual-multipart-uploads}

Die IBM Cloud Object Storage-Befehlszeilenschnittstelle bietet Nutzern bei Verwendung der Funktionen für mehrteilige Uploads die Funktionalität zum Hochladen umfangreicher Dateien in mehreren Teilen. Führen Sie zum Initiieren eines neuen mehrteiligen Uploads den Befehl `create-multipart-upload` aus, der die Rückgabe der Upload-ID der neuen Upload-Instanz bewirkt. Um mit dem Uploadprozess fortfahren zu können, müssen Sie die Upload-ID für jeden nachfolgenden Befehl speichern.

Nachdem Sie den Befehl `complete-multipart-upload` ausgeführt haben, führen Sie für jeden Dateiteil, den Sie hochladen möchten, den Befehl `upload-part` aus. **Bei mehrteiligen Uploads muss jeder einzelne Dateiteil (mit Ausnahme des letzten Teils) eine Größe von mindestens 5 MB haben.** Wenn Sie eine Datei in einzelne Teile aufteilen möchten, führen Sie in einem Terminalfenster den Befehl `split` aus. Wenn Sie zum Beispiel über eine Datei namens `TESTFILE` verfügen, die eine Größe von 13 MB hat und sich auf Ihrem Desktop befindet, und Sie möchten diese Datei in einzelne Dateiteile von je 5 MB aufteilen, führen Sie den Befehl `split -b 3m ~/Desktop/TESTFILE part-file-` aus. Durch diesen Befehl werden insgesamt drei Dateiteile namens `part-file-aa`, `part-file-ab` und `part-file-ac` generiert: zwei Dateiteile von je 5 MB und ein Dateiteil von 3 MB.

Nach dem Hochladen der einzelnen Dateiteile gibt die Befehlszeilenschnittstelle jeweils den zugehörigen ETag aus. Diesen ETag müssen Sie zusammen mit der Teilnummer in einer formatierten JSON-Datei speichern. Richten Sie sich nach dieser Vorlage, um eine eigene JSON-Datendatei mit den ETags zu erstellen.

```
{
  "Parts": [
    {
      "PartNumber": 1,
      "ETag": "Hierhin gehört der ETag für den ersten Dateiteil."
    },
    {
      "PartNumber": 2,
      "ETag": "Hierhin gehört der ETag für den zweiten Dateiteil."
    }
  ]
}
```

Fügen Sie nach Bedarf weitere Einträge zu dieser JSON-Vorlage hinzu.

UM den Status ihrer Instanz eines mehrteiligen Uploads anzuzeigen, können Sie jederzeit den Befehl `upload-part` unter Angabe des Bucketnamens, des Schlüssels und der Upload-ID ausführen. Dadurch werden unaufbereitete Informationen zu Ihrer Instanz eines mehrteiligen Uploads ausgegeben. Führen Sie nach dem erfolgreichen Upload der einzelnen Teile der Datei den Befehl `complete-multipart-upload` mit den nötigen Parametern aus. Sofern alles wie vorgesehen funktioniert, erhalten Sie eine Bestätigung, dass die Datei erfolgreich in das gewünschte Bucket hochgeladen wurde.

### Programm konfigurieren
{: #ic-config}
* **Aktion:** Konfigurieren der Vorgaben für das Programm.
* **Syntax:** `ibmcloud cos config [BEFEHL]`
* **Befehle:**
	* Zwischen HMAC- und IAM-Authentifizierung wechseln.
		* Befehl: `auth`
	* CRN in der Konfiguration speichern.
		* Befehl: `crn`
	* Standarddownloadposition in der Konfiguration speichern.
		* Befehl: `ddl`
	* HMAC-Berechtigungsnachweise in der Konfiguration speichern.
		* Befehl: `hmac`
	* Konfiguration auflisten.
		* Befehl: `list`
	* Standardregion in der Konfiguration speichern.
		* Befehl: `region`
	* Zwischen VHost- und Pfaddarstellung der URL wechseln.
		* Befehl: `url-style`


### Objekt aus Bucket kopieren
{: #ic-copy-object}
* **Aktion:** Kopieren eines Objekts aus dem Quellenbucket in das Zielbucket.
* **Syntax: ** `ibmcloud cos copy-object --bucket BUCKETNAME --key SCHLÜSSEL --copy-source QUELLE [--cache-control CACHINGANWEISUNGEN] [--content-disposition ANWEISUNGEN] [--content-encoding INHALTSCODIERUNG] [--content-language SPRACHE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since ZEITMARKE] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since ZEITMARKE] [--metadata ZUORDNUNG] [--metadata-directive ANWEISUNG] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* (QUELLE) Der Name des Quellenbuckets und der Schlüsselname des Quellenobjekts, welcher durch einen Schrägstrich (/) abgetrennt wird. Muss URL-codiert sein.
		* Flag: `--copy-source QUELLE`
	* _Optional_: Gibt `CACHINGANWEISUNGEN` für die Anforderungs- und Antwortkette an.
		* Flag: `--cache-control CACHINGANWEISUNGEN`
	* _Optional_: Gibt Informationen für die Darstellung an (`ANWEISUNGEN`).
		* Flag: `--content-disposition ANWEISUNGEN`
	* _Optional_: Gibt, welche Inhaltscodierungen (INHALTSCODIERUNG) auf das Objekt angewendet werden und daher welche Decodierungsmechanismen angewendet werden müssen, um den Medientyp zu erhalten, auf den im Headerfeld für den Inhaltstyp verwiesen wird.
		* Flag: `--content-encoding INHALTSCODIERUNG`
	* _Optional_: Die SPRACHE, in der der Inhalt vorliegt.
		* Flag: `--content-language SPRACHE`
	* _Optional_: Ein standardmäßiger MIME-Typ, der das Format der Objektdaten beschreibt.
		* Flag: `--content-type MIME`
	* _Optional_: Kopiert das Objekt, sofern dessen Entitätstag (Etag) mit dem angegebenen Tag (ETAG) übereinstimmt.
		* Flag: `--copy-source-if-match ETAG`
	* _Optional_: Kopiert das Objekt, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) geändert wurde.
		* Flag: `--copy-source-if-modified-since ZEITMARKE`
	* _Optional_: Kopiert das Objekt, sofern dessen Entitätstag (ETag) vom angegebenen Tag (ETAG) abweicht.
		* Flag: `--copy-source-if-none-match ETAG`
	* _Optional_: Kopiert das Objekt, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) nicht geändert wurde.
		* Flag: `--copy-source-if-unmodified-since ZEITMARKE`
	* _Optional_: eine ZUORDNUNG der Metadaten, die gespeichert werden sollen. Syntax: KeyName1=string,KeyName2=string
		* Flag: `--metadata ZUORDNUNG`
	* _Optional_: Gibt an, ob die Metadaten aus dem Quellenobjekt kopiert oder durch die in der Anforderung angegebenen Metadaten ersetzt werden. Zulässige Werte für ANWEISUNG: COPY, REPLACE.
		* Flag: ` --metadata-directive ANWEISUNG`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Neues Bucket erstellen
{: #ic-create-bucket}

* **Aktion:** Erstellen eines Buckets in einer Instanz von IBM Cloud Object Storage.
* **Syntax:** `ibmcloud cos create-bucket --bucket BUCKETNAME [--class KLASSENNAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* Beachten Sie, das Sie einen Cloudressourcennamen (CRN) angeben müssen, wenn Sie die IAM-Authentifizierung verwenden. Dieser kann mit dem Befehl [`ibmcloud cos config crn`](#configure-the-program) festgelegt werden.
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Der Name der Klasse.
		* Flag: `--class KLASSENNAME`
	* _Optional_: Legt die ID für die IBM Service-Instanz in der Anforderung fest.
		* Flag: `--ibm-service-instance-id ID`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`



### Neuen mehrteiligen Upload erstellen
{: #ic-create-multipart-upload}
* **Aktion:** Starten des mehrteiligen Dateiuploads durch Erstellen einer neuen Instanz eines mehrteiligen Uploads.
* **Syntax:** `ibmcloud cos create-multipart-upload --bucket BUCKETNAME --key SCHLÜSSEL [--cache-control CACHINGANWEISUNGEN] [--content-disposition ANWEISUNGEN] [--content-encoding INHALTSCODIERUNG] [--content-language SPRACHE] [--content-type MIME] [--metadata ZUORDNUNG] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* _Optional_: Gibt `CACHINGANWEISUNGEN` für die Anforderungs- und Antwortkette an.
		* Flag: `--cache-control CACHINGANWEISUNGEN`
	* _Optional_: Gibt Informationen für die Darstellung an (`ANWEISUNGEN`).
		* Flag: `--content-disposition ANWEISUNGEN`
	* _Optional_: Gibt die Inhaltscodierung (`INHALTSCODIERUNG`) des Objekts an.
		* Flag: `--content-encoding INHALTSCODIERUNG`
	* _Optional_: Die SPRACHE, in der der Inhalt vorliegt.
		* Flag: `--content-language SPRACHE`
	* _Optional_: Ein standardmäßiger MIME-Typ, der das Format der Objektdaten beschreibt.
		* Flag: `--content-type MIME`
	* _Optional_: eine ZUORDNUNG der Metadaten, die gespeichert werden sollen. Syntax: KeyName1=string,KeyName2=string
		* Flag: `--metadata ZUORDNUNG`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Vorhandenes Bucket löschen
{: #ic-delete-bucket}

* **Aktion:** Löschen eines in einer Instanz von IBM Cloud Object Storage vorhandenen Buckets.
* **Syntax:** `ibmcloud cos delete-bucket --bucket BUCKETNAME [--region REGION] [--force] [--json]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
    * _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
       * Flag: `--region REGION`
    * _Optional_: Bei diesem Vorgang wird keine Bestätigung verlangt.
       * Flag: `--force`
    * _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
       * Flag: `--json`


### CORS für Bucket löschen
{: #ic-delete-bucket-cors}
* **Aktion:** Löschen der CORS-Konfiguration für ein Bucket im IBM Cloud Object Storage-Konto eines Benutzers.
* **Syntax:** `ibmcloud cos delete-bucket-cors --bucket BUCKETNAME [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Objekt löschen
{: #ic-delete-object}
* **Aktion:** Löschen eines Objekts aus einem Bucket im IBM Cloud Object Storage-Konto eines Benutzers.
* **Syntax:** `ibmcloud cos delete-object --bucket BUCKETNAME --key SCHLÜSSEL [--region REGION] [--force] [--json]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
  * _Optional_: Bei diesem Vorgang wird keine Bestätigung verlangt.
  	* Flag: `--force`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Mehrere Objekte löschen
{: #ic-delete-objects}
* **Aktion:** Löschen mehrerer Objekte aus einem Bucket im IBM Cloud Object Storage-Konto eines Benutzers.
* **Syntax:** `ibmcloud cos delete-objects --bucket BUCKETNAME --delete STRUKTUR [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.  
		* Flag: `--bucket BUCKETNAME`  
	* Eine STRUKTUR, bei der entweder die Kurzform oder JSON-Syntax verwendet wird.  
		* Flag: `--delete STRUKTUR`  
		* Syntax der Kurzform:  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* JSON-Syntax:  
	`--delete file://<filename.json>`  
	Der Befehl `--delete` erfordert eine JSON-Struktur, die diejenigen Teile des mehrteiligen Uploads beschreibt, aus denen die vollständige Datei neu assembliert werden soll. In diesem Beispiel wird das Präfix `file://` zum Laden der JSON-Struktur aus der angegebenen Datei verwendet.
	```
	{
  	"Objects": [
    	{
    	"Key": "string",
    	"VersionId": "string"
    	}
    ...
  	],
  	"Quiet": true|false
	}
	```
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Objekte mit S3Manager herunterladen
{: #ic-download-s3manager}
* **Aktion:** Gleichzeitiges Herunterladen von Objekten von S3.
* **Syntax:** `ibmcloud cos download --bucket BUCKETNAME --key SCHLÜSSEL [--concurrency wert] [--part-size GRÖSSE] [--if-match ETAG] [--if-modified-since ZEITMARKE] [--if-none-match ETAG] [--if-unmodified-since ZEITMARKE] [--range BEREICH] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [AUSGABEDATEI]`
* **Parameter, die angegeben werden müssen:**
	* Der Name (BUCKETNAME) des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* _Optional_: Die Anzahl von Goroutinen, die pro Aufruf an Upload parallel den Betrieb aufnehmen sollen. Der Standardwert ist 5.
		* Flag: `--concurrency wert`
	* _Optional_: Die GRÖSSE des Puffers (in Byte), der zum Puffern von Daten in Datenblöcken und ihrem Beenden als Teile für S3 verwendet werden soll. Die zulässige Mindestgröße für Teile beträgt 5 MB.
		* Flag: `--part-size GRÖSSE`
	* _Optional_: Gibt das Objekt nur dann zurück, wenn sein Entitätstag (ETag) mit dem angegebenen ETAG übereinstimmt; andernfalls erfolgt die Rückgabe des Antwortcodes 412 (Vorbedingung fehlgeschlagen).
		* Flag: `--if-match ETAG`
	* _Optional_: Gibt das Objekt nur dann zurück, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) geändert wurde; andernfalls erfolgt die Rückgabe des Antwortcodes 304 (Nicht geändert).
		* Flag: `--if-modified-since ZEITMARKE`
	* _Optional_: Gibt das Objekt nur dann zurück, wenn sein Entitätstag (ETag) nicht mit dem angegebenen ETAG übereinstimmt; andernfalls erfolgt die Rückgabe des Antwortcodes 304 (Nicht geändert).
		* Flag: `--if-none-match ETAG`
	* _Optional_: Gibt das Objekt nur dann zurück, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) nicht geändert wurde; andernfalls erfolgt die Rückgabe des Antwortcodes 412 (Vorbedingung fehlgeschlagen).
		* Flag: `--if-unmodified-since ZEITMARKE`
	* _Optional_: Lädt die Byte für den angegebenen BEREICH eines Objekts herunter. Um weitere Informationen zum HTTP-Header für Bereiche zu erhalten, klicken Sie [hier](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35).
		* Flag: `--range BEREICH`
	* _Optional_: Legt den HEADER 'Cache-Control' der Antwort fest.
		* Flag: `--response-cache-control HEADER`
	* _Optional_: Legt den HEADER 'Content-Disposition' der Antwort fest.
		* Flag: `--response-content-disposition HEADER`
	* _Optional_: Legt den HEADER 'Content-Encoding' der Antwort fest.
		* Flag: `--response-content-encoding HEADER`
	* _Optional_: Legt den HEADER 'Content-Language' der Antwort fest.
		* Flag: `--response-content-language HEADER`
	* _Optional_: Legt den HEADER 'Content-Type' der Antwort fest.
		* Flag: `--response-content-type HEADER`
	* _Optional_: Legt den HEADER 'Expires' der Antwort fest.
		* Flag: `--response-expires HEADER`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`
	* _Optional_: Die Position, an der der Inhalt des Objekts gespeichert werden soll. Wenn dieser Parameter nicht angegeben ist, verwendet das Programm die Standardposition.
		* Parameter: `AUSGABEDATEI`


### Klasse eines Buckets abrufen
{: #ic-bucket-class}
* **Aktion:** Ermitteln der Klasse eines Buckets in einer Instanz von IBM Cloud Object Storage.
* **Syntax:** `ibmcloud cos get-bucket-class --bucket BUCKETNAME [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### CORS für Bucket abrufen
{: #ic-get-bucket-cors}
* **Aktion:** Zurückgeben der CORS-Konfiguration für das Bucket im IBM Cloud Object Storage-Konto eines Benutzers.
* **Syntax:** `ibmcloud cos get-bucket-cors --bucket BUCKETNAME [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
  * Der Name des Buckets.  
    * Flag: `--bucket BUCKETNAME`
  * _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
    * Flag: `--region REGION`
  * _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
    * Flag: `--json`


### Bucket suchen
{: #ic-find-bucket}
* **Aktion:** Ermitteln der Region und Klasse eines Buckets in einer Instanz von IBM Cloud Object Storage. 
* **Syntax:** `ibmcloud cos get-bucket-location --bucket BUCKETNAME [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`
	


### Objekt herunterladen
{: #ic-download-object}
* **Aktion:** Herunterladen eines Objekts aus einem Bucket im IBM Cloud Object Storage-Konto eines Benutzers.
* **Syntax:** `ibmcloud cos get-object --bucket BUCKETNAME --key SCHLÜSSEL [--if-match ETAG] [--if-modified-since ZEITMARKE] [--if-none-match ETAG] [--if-unmodified-since ZEITMARKE] [--range BEREICH] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [AUSGABEDATEI]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* _Optional_: Gibt das Objekt nur dann zurück, wenn sein Entitätstag (ETag) mit dem angegebenen ETAG übereinstimmt; andernfalls erfolgt die Rückgabe des Antwortcodes 412 (Vorbedingung fehlgeschlagen).
		* Flag: `--if-match ETAG`
	* _Optional_: Gibt das Objekt nur dann zurück, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) geändert wurde; andernfalls erfolgt die Rückgabe des Antwortcodes 304 (Nicht geändert).
		* Flag: `--if-modified-since ZEITMARKE`
	* _Optional_: Gibt das Objekt nur dann zurück, wenn sein Entitätstag (ETag) nicht mit dem angegebenen ETAG übereinstimmt; andernfalls erfolgt die Rückgabe des Antwortcodes 304 (Nicht geändert).
		* Flag: `--if-none-match ETAG`
	* _Optional_: Gibt das Objekt nur dann zurück, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) nicht geändert wurde; andernfalls erfolgt die Rückgabe des Antwortcodes 412 (Vorbedingung fehlgeschlagen).
		* Flag: `--if-unmodified-since ZEITMARKE`
	* _Optional_: Lädt die Byte für den angegebenen BEREICH eines Objekts herunter.
		* Flag: `--range BEREICH`
	* _Optional_: Legt den HEADER 'Cache-Control' der Antwort fest.
		* Flag: `--response-cache-control HEADER`
	* _Optional_: Legt den HEADER 'Content-Disposition' der Antwort fest.
		* Flag: `--response-content-disposition HEADER`
	* _Optional_: Legt den HEADER 'Content-Encoding' der Antwort fest.
		* Flag: `--response-content-encoding HEADER`
	* _Optional_: Legt den HEADER 'Content-Language' der Antwort fest.
		* Flag: `--response-content-language HEADER`
	* _Optional_: Legt den HEADER 'Content-Type' der Antwort fest.
		* Flag: `--response-content-type HEADER`
	* _Optional_: Legt den HEADER 'Expires' der Antwort fest.
		* Flag: `--response-expires HEADER`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`
	* _Optional_: Die Position, an der der Inhalt des Objekts gespeichert werden soll. Wenn dieser Parameter nicht angegeben ist, verwendet das Programm die Standardposition.
		* Parameter: `AUSGABEDATEI`


### Header eines Buckets abrufen
{: #ic-bucket-header}
* **Aktion:** Ermitteln, ob ein Bucket in einer Instanz von IBM Cloud Object Storage vorhanden ist.
* **Syntax:** `ibmcloud cos head-bucket --bucket BUCKETNAME [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Header eines Objekts abrufen
{: #ic-object-header}
* **Aktion:** Ermitteln, ob eine Datei in einem Bucket im IBM Cloud Object Storage-Konto eines Benutzers vorhanden ist.
* **Syntax:** `ibmcloud cos head-object --bucket BUCKETNAME --key SCHLÜSSEL [--if-match ETAG] [--if-modified-since ZEITMARKE] [--if-none-match ETAG] [--if-unmodified-since ZEITMARKE] [--range BEREICH] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* _Optional_: Gibt das Objekt nur dann zurück, wenn sein Entitätstag (ETag) mit dem angegebenen ETAG übereinstimmt; andernfalls erfolgt die Rückgabe des Antwortcodes 412 (Vorbedingung fehlgeschlagen).
		* Flag: `--if-match ETAG`
	* _Optional_: Gibt das Objekt nur dann zurück, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) geändert wurde; andernfalls erfolgt die Rückgabe des Antwortcodes 304 (Nicht geändert).
		* Flag: `--if-modified-since ZEITMARKE`
	* _Optional_: Gibt das Objekt nur dann zurück, wenn sein Entitätstag (ETag) nicht mit dem angegebenen ETAG übereinstimmt; andernfalls erfolgt die Rückgabe des Antwortcodes 304 (Nicht geändert).
		* Flag: `--if-none-match ETAG`
	* _Optional_: Gibt das Objekt nur dann zurück, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) nicht geändert wurde; andernfalls erfolgt die Rückgabe des Antwortcodes 412 (Vorbedingung fehlgeschlagen).
		* Flag: `--if-unmodified-since ZEITMARKE`
	* Lädt die Byte für den angegebenen BEREICH eines Objekts herunter.
		* Flag: `--range BEREICH`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Alle Buckets auflisten
{: #ic-list-buckets}
* **Aktion:** Ausgeben einer Liste aller Buckets im IBM Cloud Object Storage-Konto eines Benutzers. Buckets können sich in unterschiedlichen Regionen befinden.
* **Syntax:** `ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* Beachten Sie, das Sie einen Cloudressourcennamen (CRN) angeben müssen, wenn Sie die IAM-Authentifizierung verwenden. Dieser kann mit dem Befehl [`ibmcloud cos config crn`](#configure-the-program) festgelegt werden.
* **Parameter, die angegeben werden müssen:**
  * Es müssen keine Parameter angegeben werden.
	* _Optional_: Legt die ID für die IBM Service-Instanz in der Anforderung fest.
		* Flag: `--ibm-service-instance-id`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Erweiterte Auflistung von Buckets
{: #ic-extended-bucket-listing}
* **ListeAktion:** Ausgeben einer Liste aller Buckets im IBM Cloud Object Storage-Konto eines Benutzers. Buckets können sich in unterschiedlichen Regionen befinden.
* **Syntax:** `ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker SCHLÜSSEL] [--prefix PRÄFIX] [--page-size GRÖSSE] [--max-items ANZAHL] [--json] `
	* Beachten Sie, das Sie einen Cloudressourcennamen (CRN) angeben müssen, wenn Sie die IAM-Authentifizierung verwenden. Dieser kann mit dem Befehl [`ibmcloud cos config crn`](#configure-the-program) festgelegt werden.
* **Parameter, die angegeben werden müssen:**
  * Es müssen keine Parameter angegeben werden.
	* _Optional_: Legt die ID für die IBM Service-Instanz in der Anforderung fest.
		* Flag: `--ibm-service-instance-id`
	* _Optional_: Gibt den SCHLÜSSEL an, mit dem bei der Auflistung von Objekten in einem Bucket begonnen werden soll.
		* Flag: `--marker SCHLÜSSEL`
	* _Optional_: Begrenzt die Antwort auf Schlüssel, die mit dem angegebenen PRÄFIX beginnen.
		* Flag: `--prefix PRÄFIX`
	* _Optional_: Die GRÖSSE jeder Seite, die im Serviceaufruf abgerufen werden soll. Diese Angabe hat keine Auswirkungen auf die Anzahl von Elementen, die in der Ausgabe des Befehls zurückgegeben werden. Die Festlegung einer kleineren Seitengröße führt zu mehr Aufrufen an den Service, wobei in jedem Aufruf weniger Elemente zurückgegeben werden. Dies kann dazu beitragen, dass keine Zeitlimitüberschreitungen für Serviceaufrufe auftreten.
		* Flag: `--page-size GRÖSSE`
	* _Optional_: Die ANZAHL von Elementen insgesamt, die in der Ausgabe des Befehls zurückgegeben werden sollen.
		* Flag: `--max-items ANZAHL`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### In Bearbeitung befindliche mehrteilige Uploads auflisten
{: #ic-list-multipart-uploads}
* **Aktion:** Auflisten der in Bearbeitung befindlichen mehrteiligen Uploads.
* **Syntax:** `ibmcloud cos list-multipart-uploads --bucket BUCKETNAME [--delimiter BEGRENZER] [--encoding-type METHODE] [--prefix PRÄFIX] [--key-marker value] [--upload-id-marker value] [--page-size GRÖSSE] [--max-items ANZAHL] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Ein BEGRENZER ist ein Zeichen, mit dem Sie Schlüssel in Gruppen zusammenfassen.
		* Flag: `--delimiter BEGRENZER`
	* _Optional_: Fordert die Codierung der Objektschlüssel in der Antwort an und gibt die METHODE an, die für die Codierung verwendet werden soll.
		* Flag: `--encoding-type METHODE`
	* _Optional_: Begrenzt die Antwort auf Schlüssel, die mit dem angegebenen PRÄFIX beginnen.
		* Flag: `--prefix PRÄFIX`
	* _Optional_: In Verbindung mit 'upload-id-marker' gibt dieser Parameter den mehrteiligen Upload an, nach dem die Auflistung beginnen soll.
		* Flag: `--key-marker value`
	* _Optional_: In Verbindung mit 'key-marker' gibt dieser Parameter den mehrteiligen Upload an, nach dem die Auflistung beginnen soll. Wenn 'key-marker' nicht angegeben ist, wird der Parameter 'upload-id-marker' ignoriert.
		* Flag: `--upload-id-marker value`
	* _Optional_: Die GRÖSSE jeder Seite, die im Serviceaufruf abgerufen werden soll. Diese Angabe hat keine Auswirkungen auf die Anzahl von Elementen, die in der Ausgabe des Befehls zurückgegeben werden. Die Festlegung einer kleineren Seitengröße führt zu mehr Aufrufen an den Service, wobei in jedem Aufruf weniger Elemente zurückgegeben werden. Dies kann dazu beitragen, dass keine Zeitlimitüberschreitungen für Serviceaufrufe auftreten. (Standardwert: 1000).
		* Flag: `--page-size GRÖSSE`
	* _Optional_: Die ANZAHL von Elementen insgesamt, die in der Ausgabe des Befehls zurückgegeben werden sollen. Wenn die Gesamtzahl der verfügbaren Elemente den angegebenen Wert überschreitet, wird in der Ausgabe des Befehls ein 'NextToken'-Wert angegeben. Geben Sie zum Fortsetzen der Paginierung den 'NextToken'-Wert im Argument 'starting-token' eines nachfolgenden Befehls an. (Standardwert: 0).
		* Flag: `--max-items ANZAHL`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Objekte auflisten
{: #ic-list-objects}
* **Aktion:** Auflisten der in einem Bucket im IBM Cloud Object Storage-Konto eines Benutzers vorhandenen Dateien. Diese Operation ist gegenwärtig auf die 1000 zuletzt erstellten Objekte beschränkt und lässt kein Filtern zu.
* **Syntax:** `ibmcloud cos list-objects --bucket BUCKETNAME [--delimiter BEGRENZER] [--encoding-type METHODE] [--prefix PRÄFIX] [--starting-token TOKEN] [--page-size GRÖSSE] [--max-items ANZAHL] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Ein BEGRENZER ist ein Zeichen, mit dem Sie Schlüssel in Gruppen zusammenfassen.
		* Flag: `--delimiter BEGRENZER`
	* _Optional_: Fordert die Codierung der Objektschlüssel in der Antwort an und gibt die METHODE an, die für die Codierung verwendet werden soll.
		* Flag: `--encoding-type METHODE`
	* _Optional_: Begrenzt die Antwort auf Schlüssel, die mit dem angegebenen PRÄFIX beginnen.
		* Flag: `--prefix PRÄFIX`
	* _Optional_: Ein TOKEN, das angibt, wo die Paginierung beginnen soll. Dabei handelt es sich um den 'NextToken'-Wert einer zuvor abgeschnittenen Antwort.
		* Flag: `--starting-token TOKEN`
	* _Optional_: Die GRÖSSE jeder Seite, die im Serviceaufruf abgerufen werden soll. Diese Angabe hat keine Auswirkungen auf die Anzahl von Elementen, die in der Ausgabe des Befehls zurückgegeben werden. Die Festlegung einer kleineren Seitengröße führt zu mehr Aufrufen an den Service, wobei in jedem Aufruf weniger Elemente zurückgegeben werden. Dies kann dazu beitragen, dass keine Zeitlimitüberschreitungen für Serviceaufrufe auftreten. (Standardwert: 1000)
		* Flag: `--page-size GRÖSSE`
	* _Optional_: Die ANZAHL von Elementen insgesamt, die in der Ausgabe des Befehls zurückgegeben werden sollen. Wenn die Gesamtzahl der verfügbaren Elemente den angegebenen Wert überschreitet, wird in der Ausgabe des Befehls ein 'NextToken'-Wert angegeben. Geben Sie zum Fortsetzen der Paginierung den 'NextToken'-Wert im Argument 'starting-token' eines nachfolgenden Befehls an. (Standardwert: 0)
		* Flag: `--max-items ANZAHL`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Teile auflisten
{: #ic-list-parts}
* **Aktion:** Ausgeben von Informationen zu einer in Bearbeitung befindlichen Instanz eines mehrteiligen Uploads.
* **Syntax:** `ibmcloud cos list-parts --bucket BUCKETNAME --key SCHLÜSSEL --upload-id ID --part-number-marker WERT [--page-size GRÖSSE] [--max-items ANZAHL] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* Die Upload-ID, die den mehrteiligen Upload angibt.
		* Flag: `--upload-id ID`
	* Gibt den WERT der Teilnummer an, nach dem die Auflistung beginnt. (Standardwert: 1)
		* Flag: `--part-number-marker WERT`
	* _Optional_: Die GRÖSSE jeder Seite, die im Serviceaufruf abgerufen werden soll. Diese Angabe hat keine Auswirkungen auf die Anzahl von Elementen, die in der Ausgabe des Befehls zurückgegeben werden. Die Festlegung einer kleineren Seitengröße führt zu mehr Aufrufen an den Service, wobei in jedem Aufruf weniger Elemente zurückgegeben werden. Dies kann dazu beitragen, dass keine Zeitlimitüberschreitungen für Serviceaufrufe auftreten. (Standardwert: 1000)
		* Flag: `--page-size GRÖSSE`
	* _Optional_: Die ANZAHL von Elementen insgesamt, die in der Ausgabe des Befehls zurückgegeben werden sollen. Wenn die Gesamtzahl der verfügbaren Elemente den angegebenen Wert überschreitet, wird in der Ausgabe des Befehls ein 'NextToken'-Wert angegeben. Geben Sie zum Fortsetzen der Paginierung den 'NextToken'-Wert im Argument 'starting-token' eines nachfolgenden Befehls an. (Standardwert: 0)
		* Flag: `--max-items ANZAHL`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### CORS für Bucket festlegen
{: #ic-set-bucket-cors}
* **Aktion:** Festlegen der CORS-Konfiguration für ein Bucket im IBM Cloud Object Storage-Konto des Benutzers.
* **Syntax:** `ibmcloud cos put-bucket-cors --bucket BUCKETNAME [--cors-configuration STRUKTUR] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* _Optional_: Eine STRUKTUR mit JSON-Syntax in einer Datei.
		* Flag: `--cors-configuration STRUKTUR`
		* JSON-Syntax:  
	`--cors-configuration file://<filename.json>`  
	Der Befehl `--cors-configuration` erfordert eine JSON-Struktur, die diejenigen Teile des mehrteiligen Uploads beschreibt, aus denen die vollständige Datei neu assembliert werden soll. In diesem Beispiel wird das Präfix `file://` zum Laden der JSON-Struktur aus der angegebenen Datei verwendet.
		```
	{
  	"CORSRules": [
    	{
      	"AllowedHeaders": ["string", ...],
      	"AllowedMethods": ["string", ...],
      	"AllowedOrigins": ["string", ...],
      	"ExposeHeaders": ["string", ...],
      	"MaxAgeSeconds": integer
    	}
    	...
  	]
	}
	```
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`



### Objekt mit 'Put' hochladen
{: #ic-upload-object}
* **Aktion:** Hochladen eines Objekts in ein Bucket im IBM Cloud Object Storage-Konto eines Benutzers.
* **SYNTAX:** `ibmcloud cos put-object --bucket BUCKETNAME --key SCHLÜSSEL [--body DATEIPFAD] [--cache-control CACHINGANWEISUNGEN] [--content-disposition ANWEISUNGEN] [--content-encoding INHALTSCODIERUNG] [--content-language SPRACHE] [--content-length GRÖSSE] [--content-md5 MD5] [--content-type MIME] [--metadata ZUORDNUNG] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
    * Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* _Optional_: Speicherposition der Objektdaten (`DATEIPFAD`).
		* Flag: `--body DATEIPFAD`
	* _Optional_: Gibt `CACHINGANWEISUNGEN` für die Anforderungs- und Antwortkette an.
		* Flag: `--cache-control CACHINGANWEISUNGEN`
	* _Optional_: Gibt Informationen für die Darstellung an (`ANWEISUNGEN`).
		* Flag: `--content-disposition ANWEISUNGEN`
	* _Optional_: Gibt die Inhaltscodierung (`INHALTSCODIERUNG`) des Objekts an.
		* Flag: `--content-encoding INHALTSCODIERUNG`
	* _Optional_: Die SPRACHE, in der der Inhalt vorliegt.
		* Flag: `--content-language SPRACHE`
	* _Optional_: GRÖSSE des Hauptteils in Byte. Dieser Parameter ist nützlich, wenn die Größe des Hauptteils nicht automatisch bestimmt werden kann. ris useful when the size of the body cannot be determined automatically. (Standardwert: 0)
		* Flag: `--content-length GRÖSSE`
	* _Optional_: Der in Base64 codierte 128-Bit-MD5-Digest der Daten.
		* Flag: `--content-md5 MD5`
	* _Optional_: Ein standardmäßiger MIME-Typ, der das Format der Objektdaten beschreibt.
		* Flag: `--content-type MIME`
	* _Optional_: eine ZUORDNUNG der Metadaten, die gespeichert werden sollen. Syntax: KeyName1=string,KeyName2=string
		* Flag: `--metadata ZUORDNUNG`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Objekte mit S3Manager hochladen
{: #ic-upload-s3manager}
* **Aktion:** Gleichzeitiges Hochladen von Objekten in S3.
* **Syntax:** `ibmcloud cos upload --bucket BUCKETNAME --key KEY --file PFAD [--concurrency wert] [--max-upload-parts TEILE] [--part-size GRÖSSE] [--leave-parts-on-errors] [--cache-control CACHINGANWEISUNGEN] [--content-disposition ANWEISUNGEN] [--content-encoding INHALTSCODIERUNG] [--content-language SPRACHE] [--content-length GRÖSSE] [--content-md5 MD5] [--content-type MIME] [--metadata ZUORDNUNG] [--region REGION] [--json]`
* **Parameter, die angegeben werden müssen:**
	* Der Name (BUCKETNAME) des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* Der PFAD zu der Datei, die hochgeladen werden soll.
		* Flag: `--file PFAD`
	* _Optional_: Die Anzahl von Goroutinen, die pro Aufruf an Upload parallel den Betrieb aufnehmen sollen. Der Standardwert ist 5.
		* Flag: `--concurrency wert`
	* _Optional_: Maximale Anzahl der TEILE, die in S3 hochgeladen werden, wobei die Teilgröße des hochzuladenden Objekts berechnet wird. Die Obergrenze liegt bei 10.000 Teilen.
		* Flag: `--max-upload-parts TEILE`
	* _Optional_: Die GRÖSSE des Puffers (in Byte), der zum Puffern von Daten in Datenblöcken und ihrem Beenden als Teile für S3 verwendet werden soll. Die zulässige Mindestgröße für Teile beträgt 5 MB.
		* Flag: `--part-size GRÖSSE`
	* _Optional_: Wenn Sie für diesen Wert 'True' festlegen, wird vermieden, dass das SDK beim Auftreten eines Fehlers 'AbortMultipartUpload' aufruft, wodurch alle erfolgreich hochgeladenen Teile auf S3 zur manuellen Wiederherstellung erhalten bleiben.
		* Flag: `--leave-parts-on-errors`
	* _Optional_: Gibt CACHINGANWEISUNGEN für die Anforderungs- und Antwortkette an.
		* Flag: `--cache-control CACHINGANWEISUNGEN`
	* _Optional_: Gibt Informationen für die Darstellung an (ANWEISUNGEN).
		* Flag: `--content-disposition ANWEISUNGEN`
	* _Optional_: Gibt, welche Inhaltscodierungen (INHALTSCODIERUNG) auf das Objekt angewendet wurden und daher welche Decodierungsmechanismen angewendet werden müssen, um den Medientyp zu erhalten, auf den im Headerfeld für den Inhaltstyp verwiesen wird.
		* Flag: `--content-encoding INHALTSCODIERUNG`
	* _Optional_: Die SPRACHE, in der der Inhalt vorliegt.
		* Flag: `--content-language SPRACHE`
	* _Optional_: GRÖSSE des Hauptteils in Byte. Dieser Parameter ist nützlich, wenn die Größe des Hauptteils nicht automatisch bestimmt werden kann.
		* Flag: `--content-length GRÖSSE`
	* _Optional_: Der in Base64 codierte 128-Bit-MD5-Digest der Daten.
		* Flag: `--content-md5 MD5`
	* _Optional_: Ein standardmäßiger MIME-Typ, der das Format der Objektdaten beschreibt.
		* Flag: `--content-type MIME`
	* _Optional_: eine ZUORDNUNG der Metadaten, die gespeichert werden sollen. Syntax: KeyName1=string,KeyName2=string
		* Flag: `--metadata ZUORDNUNG`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Teil hochladen
{: #ic-upload-part}
* **Aktion:** Hochladen eines Teils einer Datei bei einer vorhandenen Instanz eines mehrteiligen Uploads.
* **Syntax:** `ibmcloud cos upload-part --bucket BUCKETNAME --key SCHLÜSSEL --upload-id ID --part-number NUMMER [--body DATEIPFAD] [--region REGION] [--json]`
	* Beachten Sie, dass Sie für jeden hochgeladenen Dateiteil die zugehörige Nummer und den ETag (den die Befehlszeilenschnittstelle für Sie ausgibt) für jeden Teil in einer JSON-Datei speichern müssen. Weitere Informationen enthält der 'Leitfaden für mehrteilige Uploads' weiter unten.
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets, bei dem der mehrteilige Upload stattfindet.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* Die Upload-ID, die den mehrteiligen Upload angibt.
		* Flag: `--upload-id ID`
	* TeilNUMMER des Teils, der hochgeladen wird. Hierbei handelt es sich um eine positive Ganzzahl im Wertebereich von 1-10.000. (Standardwert: 1)
		* Flag: `--part-number NUMMER`
	* _Optional_: Speicherposition der Objektdaten (`DATEIPFAD`).
		* Flag: `--body DATEIPFAD`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Kopie eines Teils hochladen
{: #ic-upload-a-part-copy}
* **Aktion:** Hochladen eines Teils durch Kopieren von Daten aus einem vorhandenen Objekt.
* **Syntax:** `ibmcloud cos upload-part-copy --bucket BUCKETNAME --key SCHLÜSSEL --upload-id ID --part-number NUMMER --copy-source QUELLE [--copy-source-if-match ETAG] [--copy-source-if-modified-since ZEITMARKE] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since ZEITMARKE] [--copy-source-range wert] [--region REGION] [--json]`
	* Beachten Sie, dass Sie für jeden hochgeladenen Dateiteil die zugehörige Nummer und den ETag (den die Befehlszeilenschnittstelle für Sie ausgibt) für jeden Teil in einer JSON-Datei speichern müssen. Weitere Informationen enthält der 'Leitfaden für mehrteilige Uploads'.
* **Parameter, die angegeben werden müssen:**
	* Der Name des Buckets.
		* Flag: `--bucket BUCKETNAME`
	* Der SCHLÜSSEL des Objekts.
		* Flag: `--key SCHLÜSSEL`
	* Die Upload-ID, die den mehrteiligen Upload angibt.
		* Flag: `--upload-id ID`
	* TeilNUMMER des Teils, der hochgeladen wird. Hierbei handelt es sich um eine positive Ganzzahl zwischen 1 und 10.000.
		* Flag: `--part-number TEILNUMMER`
	* (QUELLE) Der Name des Quellenbuckets und der Schlüsselname des Quellenobjekts, welcher durch einen Schrägstrich (/) abgetrennt wird. Muss URL-codiert sein.
		* Flag: `--copy-source QUELLE`
	* _Optional_: Kopiert das Objekt, sofern dessen Entitätstag (Etag) mit dem angegebenen Tag (ETAG) übereinstimmt.
		* Flag: `--copy-source-if-match ETAG`
	* _Optional_: Kopiert das Objekt, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) geändert wurde.
		* Flag: `--copy-source-if-modified-since ZEITMARKE`
	* _2Optional_: Kopiert das Objekt, sofern dessen Entitätstag (ETag) vom angegebenen Tag (ETAG) abweicht.
		* Flag: `--copy-source-if-none-match ETAG`
	* _Optional_: Kopiert das Objekt, sofern es nach dem angegebenen Zeitpunkt (ZEITMARKE) nicht geändert wurde.
		* Flag: `--copy-source-if-unmodified-since ZEITMARKE`
	* _Optional_: Der Bereich von Byte, aus dem aus dem Quellenobjekt kopiert werden soll. Der Bereichswert im Format 'bytes=erster_wert-letzter_wert' verwenden, wobei 'erster_wert' und 'letzer_wert' die relativen Byteadressen mit der Basis null angeben, die kopiert werden sollen. Die Angabe 'bytes=0-9' gibt beispielweise an, dass die ersten  zehn Byte der Quelle kopiert werden sollen. Ein Bereich kann nur kopiert werden, wenn die Größe des Quellenobjekts 5 MB überschreitet.
		* Flag: `--copy-source-range wert`
	* _Optional_: Die REGION, in der sich das Bucket befindet. Ist dieses Flag nicht angegeben, verwendet das Programm die Standardoption, die in der Konfiguration angegeben ist.
		* Flag: `--region REGION`
	* _Optional_: Die Rückgabe der Ausgabe erfolgt in unaufbereitetem JSON-Format.
		* Flag: `--json`


### Warten
{: #ic-wait}
* **Aktion:** Warten, bis eine bestimmte Bedingung erfüllt ist. Jeder Unterbefehl fragt eine Anwendungsprogrammierschnittstelle (API) ab, bis die aufgelistete Anforderung erfüllt ist.
* **Syntax:** `ibmcloud cos wait befehl [argumente...] [befehlsoptionen]`
* **Befehle:**
    * `bucket-exists`
  		* Beim Polling mit 'head-bucket' warten, bis Antwort 200 erhalten wird. Das Polling wird alle 5 Sekunden so lange durchgeführt, bis der Status 'Erfolgreich' erzielt worden ist. Dieser Vorgang wird nach 20 fehlgeschlagenen Prüfungen mit dem Rückgabecode 255 beendet.
	* `bucket-not-exists`
		* Beim Polling mit 'head-bucket' warten, bis Antwort 404 erhalten wird. Das Polling wird alle 5 Sekunden so lange durchgeführt, bis der Status 'Erfolgreich' erzielt worden ist. Dieser Vorgang wird nach 20 fehlgeschlagenen Prüfungen mit dem Rückgabecode 255 beendet.
	* `object-exists`
		* Beim Polling mit 'head-object' warten, bis Antwort 200 erhalten wird. Das Polling wird alle 5 Sekunden so lange durchgeführt, bis der Status 'Erfolgreich' erzielt worden ist. Dieser Vorgang wird nach 20 fehlgeschlagenen Prüfungen mit dem Rückgabecode 255 beendet.
	* `object-not-exists`
		* Beim Polling mit 'head-object' warten, bis Antwort 404 erhalten wird. Das Polling wird alle 5 Sekunden so lange durchgeführt, bis der Status 'Erfolgreich' erzielt worden ist. Dieser Vorgang wird nach 20 fehlgeschlagenen Prüfungen mit dem Rückgabecode 255 beendet.

