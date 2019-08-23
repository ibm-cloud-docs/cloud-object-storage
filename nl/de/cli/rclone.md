---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: data migration, object storage, cli, rclone

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

# `rclone` verwenden
{: #rclone}

## `rclone` installieren
{: #rclone-install}

Das Tool `rclone` wird zur Synchronisation von Verzeichnissen und zur Migration von Daten zwischen verschiedenen Speicherplattformen verwendet. Das Programm basiert auf Go und wird in Form einer einzelnen Binärdatei bereitgestellt.

### Schnellstartinstallation
{: #rclone-quick}

*  Führen Sie den [Download](https://rclone.org/downloads/) der relevanten Binärdatei durch. 
*  Extrahieren Sie die Binärdatei für `rclone` oder die Datei `rclone.exe` aus dem Archiv.
*  Führen Sie zur Installation den Befehl `rclone config` aus.

### Installation mit Script durchführen
{: #rclone-script}

Installieren Sie `rclone` auf Linux-, Mac OS- und BSD-Systemen:

```
curl https://rclone.org/install.sh | sudo bash
```

Des Weiteren stehen Betaversionen zur Verfügung:

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

Das Installationsscript überprüft zuerst die installierte Version von `rclone` und überspringt das Herunterladen, wenn die aktuelle Version bereits auf dem neuesten Stand ist.
{:note}

### Linux-Installation mit vorkompilierter Binärdatei durchführen
{: #rclone-linux-binary}

Rufen Sie zuerst die Binärdatei ab und entpacken Sie sie:

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

Kopieren Sie die Binärdatei als Nächstes an eine geeignete Position:

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

Installieren Sie die Dokumentation:

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

Führen Sie zur Installation den Befehl `rclone config` aus:

```
rclone config
```

### Mac OS-Installation mit vorkompilierter Binärdatei durchführen
{: #rclone-osx-binary}

Laden Sie zuerst das Paket für `rclone` herunter:

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

Extrahieren Sie anschließend die heruntergeladene Datei und wechseln Sie mit dem Befehl `cd` in den extrahierten Ordner:

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

Verschieben Sie `rclone` in `$PATH` und geben Sie Ihr Kennwort ein, wenn Sie dazu aufgefordert werden:

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

Der Befehl `mkdir` kann ohne Risiko ausgeführt werden, auch wenn das Verzeichnis vorhanden ist.
{:tip}

Entfernen Sie die verbliebenen Dateien.

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

Führen Sie zur Installation den Befehl `rclone config` aus:

```
rclone config
```

## Zugriff auf IBM COS konfigurieren
{: #rclone-config}

1. Führen Sie den Befehl `rclone config` aus und wählen Sie für eine neue ferne Instanz `n` aus.

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. Geben Sie den Namen für die Konfiguration ein:
```
	name> <YOUR NAME>
```

3. Wählen Sie den S3-Speicher ("s3") aus.

```
	Choose a number from below, or type in your own value
		1 / Alias for a existing remote
		\ "alias"
		2 / Amazon Drive
		\ "amazon cloud drive"
		3 / Amazon S3 Complaint Storage Providers (Dreamhost, Ceph, Minio, IBM COS)
		\ "s3"
		4 / Backblaze B2
		\ "b2"
	[snip]
		23 / http Connection
	  \ "http"
	Storage> 3
```

  4. Wählen Sie IBM COS als S3-Speicheranbieter aus.

```
Choose the S3 provider.
Enter a string value. Press Enter for the default ("")
Choose a number from below, or type in your own value
	 1 / Amazon Web Services (AWS) S3
	   \ "AWS"
	 2 / Ceph Object Storage
	 \ "Ceph"
   3 / Digital Ocean Spaces
   \ "Digital Ocean"
	 4 / Dreamhost DreamObjects
   \ "Dreamhost"
   5 / IBM COS S3
	 \ "IBMCOS"
	 [snip]
	 Provider>5
```

  1. Geben Sie **False** ein, um Ihre Berechtigungsnachweise einzugeben.

```
Get AWS credentials from the runtime (environment variables or EC2/ECS meta data if no env vars). 
Only applies if access_key_id and secret_access_key is blank.
Enter a boolean value (true or false). Please Enter for the default ("false").
Choose a number from below, or type in your own value
	 1 / Enter AWS credentials in the next step
   \ "false"
   2 / Get AWS credentials from the environment (env vars or IAM)
   \ "true"
   env_auth>false
```

  6. Geben Sie den Zugriffsschlüssel und den geheimen Schlüssel ein.

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. Geben Sie den Endpunkt für IBM COS an. Wählen Sie für Public IBM COS eine der verfügbaren Optionen aus. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```
Endpoint for IBM COS S3 API.
	Choose a number from below, or type in your own value
	 1 / US Cross Region Endpoint
	   \ "s3.us.cloud-object-storage.appdomain.cloud"
	 2 / US Cross Region Dallas Endpoint
	   \ "s3-api.dal.us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 3 / US Cross Region Washington DC Endpoint
	   \ "s3-api.wdc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 4 / US Cross Region San Jose Endpoint
	   \ "s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 5 / US Cross Region Private Endpoint
	   \ "s3-api.us-geo.objectstorage.service.networklayer.com"
[snip]
	34 / Toronto Single Site Private Endpoint
	   \ "s3.tor01.objectstorage.service.networklayer.com"
	endpoint>1
```

  8. Geben Sie eine IBM COS-Standortbedingung an. Die Standortbedingung muss mit dem Endpunkt übereinstimmen. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```
 1 / US Cross Region Standard
	   \ "us-standard"
	 2 / US Cross Region Vault
	   \ "us-vault"
	 3 / US Cross Region Cold
	   \ "us-cold"
	 4 / US Cross Region Flex
	   \ "us-flex"
	 5 / US East Region Standard
	   \ "us-east-standard"
[snip]
	32 / Toronto Flex
	   \ "tor01-flex"
location_constraint>1
```

  9. Geben Sie eine ACL an. Nur `public-read` und `private` werden unterstützt. 

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. Überprüfen Sie die angezeigte Konfiguration und akzeptieren Sie die Speicherung von "remote". Beenden Sie anschließend die Operation. Die Konfigurationsdatei hat folgendes Format:

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3.us.cloud-object-storage.appdomain.cloud
	location_constraint = us-standard
	acl = private
```

## Befehlsreferenz
{: #rclone-reference}

### Bucket erstellen
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### Verfügbare Buckets auflisten
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### Inhalte eines Buckets auflisten
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### Datei von lokaler auf ferne Einheit kopieren
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### Datei von ferner auf lokale Einheit kopieren
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### Datei auf ferner Einheit löschen
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### Listenbefehle
{: #rclone-reference-listing}

Die folgenden zugehörigen Listenbefehle stehen zur Verfügung
* `ls` - nur zum Auflisten der Größe und des Pfads von Objekten
* `lsl` - nur zum Auflisten der Änderungszeit, der Größe und des Pfads von Objekten
* `lsd` - nur zum Auflisten von Verzeichnissen
* `lsf` - zum Auflisten von Objekten und Verzeichnissen ein einfach zu parsender Form
* `lsjson` - zum Auflisten von Objekten und Verzeichnissen im JSON-Format

## `rclone sync`
{: #rclone-sync}

Die Operation `sync` bewirkt, dass Quelle und Ziel identisch sind. Dabei wird nur das Ziel geändert. Bei der Synchronisation werden unveränderte Dateien nicht übertragen. Zur Ermittlung von geänderten Dateien müssen Sie die Größe und die Änderungszeit oder den MD5SUM-Wert überprüfen. Das Ziel wird so aktualisiert, dass es mit der Quelle übereinstimmt. Dazu werden ggf. auch Dateien gelöscht.

Da es hierbei zu Datenverlusten kommen kann, sollten Sie zuerst mit dem Flag `--dry-run` einen Testlauf durchführen, um genau feststellen zu können, welche Daten kopiert und welche gelöscht werden.
{:important}

Beachten Sie, dass Dateien auf dem Ziel nicht gelöscht werden, wenn an einer Stelle des Prozesses Fehler aufgetreten sind.

Der _Inhalt_ des Verzeichnisses wird synchronisiert, jedoch nicht das Verzeichnis selbst. Wenn `source:path` ein Verzeichnis ist, dann wird der Inhalt von `source:path` kopiert, nicht jedoch der Verzeichnisname und dessen Inhalt. Weitere Informationen zu diesem Thema finden Sie in der ausführlichen Erläuterung im Befehl `copy`.

Wenn `dest:path` nicht vorhanden ist, wird der entsprechende Pfad erstellt. Anschließend wird der Inhalt von `source:path` dorthin kopiert.

```sh
rclone sync source:path dest:path [flags]
```

### `rclone` an mehreren Standorten gleichzeitig verwenden
{: #rclone-sync-multiple}

Sie können `rclone` an mehreren Standorten gleichzeitig verwenden, wenn Sie für die Ausgabe jeweils ein anderes Unterverzeichnis auswählen:

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

Wenn Sie die Synchronisation (`sync`) in dasselbe Verzeichnis ausführen, dann müssen Sie `rclone copy` verwenden, da andernfalls die beiden Prozesse möglicherweise die Dateien des jeweils anderen Prozesses löschen:

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

Bei Verwendung der Befehle `sync`, `copy` oder `move` werden alle Dateien, die überschrieben oder gelöscht worden wären, in ihrer ursprünglichen Hierarchie in dieses Verzeichnis verschoben.

Wenn `--suffix` festgelegt wird, dann erhalten die verschobenen Dateien das Suffix, das ihnen hinzugefügt wurde. Wenn (nach der Hinzufügung des Suffix) eine Datei mit demselben Pfad im Verzeichnis vorhanden ist, wird sie überschrieben.

Die verwendete ferne Einheit muss die serverseitige Verschiebung oder Kopieerstellung unterstützen und Sie müssen dieselbe ferne Einheit wie das Ziel der Synchronisation verwenden. Das Sicherungsverzeichnis darf keine Überschneidungen mit dem Zielverzeichnis aufweisen.

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

Mit diesem Befehl wird `/path/to/local` anhand von `sync` mit `remote:current` synchronisiert. Dateien, die aktualisiert oder gelöscht worden wären, werden jedoch in `remote:old` gespeichert.

Wenn Sie `rclone` über ein Script ausführen, können Sie das aktuelle Datum als Verzeichnisname verwenden, der zur Speicherung der alten Dateien an `--backup-dir` übergeben wird. Alternativ hierzu können Sie das aktuelle Datum in `--suffix` übergeben.

## Tägliche Synchronisation mit `rclone` durchführen
{: #rclone-sync-daily}

Zur Automatisierung von Sicherungen müssen Sie einen Sicherungszeitplan erstellen. Abhängig von Ihrer Plattform werden hierbei unterschiedliche Verfahren angewendet. Unter Windows kann hierzu der Taskplaner (Aufgabenplanung) benutzt werden, während unter Mac OS und Linux 'crontabs' verwendet wird.

### Verzeichnis synchronisieren
{: #rclone-sync-directory}

`rclone` synchronisiert ein lokales Verzeichnis mit dem fernen Container und speichert alle Dateien im lokalen Verzeichnis in dem Container. `rclone` verwendet die Syntax `rclone sync source destination`, wobei `source` der lokale Ordner und `destination` der Container in Ihrem IBM COS ist.

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

Möglicherweise haben Sie bereits ein Ziel, das erstellt wird. Andernfalls können Sie ein neues Bucket erstellen, indem Sie die oben beschriebenen Schritte ausführen.

### Job terminieren
{: #rclone-sync-schedule}

Bevor Sie einen Job terminieren, sollten Sie sicherstellen, dass Sie den ersten Upload ausgeführt haben und dass dieser abgeschlossen wurde.

#### Windows
{: #rclone-sync-windows}

1. Erstellen Sie an einer beliebigen Position auf Ihrem Computer eine Textdatei mit dem Namen `backup.bat` und fügen Sie den Befehl ein, den Sie im Abschnitt zum [Synchronisieren eines Verzeichnisses](#rclone-sync-directory) verwendet haben. Geben Sie den vollständigen Pfad zur Datei 'rclone.exe' an und vergessen Sie dabei nicht, die Datei zu speichern.

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. Verwenden Sie `schtasks`, um einen Job zu terminieren. Dieses Dienstprogramm verwendet eine Reihe von Parametern.
	* /RU – der Benutzer, unter dem der Job ausgeführt werden soll. Dieser Parameter ist erforderlich, wenn der gewünschte Benutzer abgemeldet ist.
	* /RP – das Kennwort des Benutzers.
	* /SC – legen Sie DAILY fest.
	* /TN – der Name des Jobs. Ordnen Sie ihm den Namen 'backup' zu.
	* /TR – der Pfad zur Datei 'backup.bat', die Sie soeben erstellt haben.
	* /ST – der Zeitpunkt, zu dem der Start der Task ausgeführt werden soll. Dieser Zeitpunkt wird im 24-Stunden-Format angegeben. 01:05:00 ist 1:05 AM. 13:05:00 ist 1:05 PM.

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac und Linux
{: #rclone-sync-nix}

1. Erstellen Sie an einer beliebigen Position auf Ihrem Computer eine Textdatei mit dem Namen `backup.sh` und fügen Sie den Befehl ein, den Sie im Abschnitt zum [Synchronisieren eines Verzeichnisses](#rclone-sync-directory) verwendet haben. Die Datei hat folgendes Format. Geben Sie den vollständigen Pfad zur ausführbaren rclone-Datei an und vergessen Sie dabei nicht, die Datei zu speichern.

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. Definieren Sie das Script mit `chmod` als ausführbar.

```sh
chmod +x backup.sh
```

3. Bearbeiten Sie 'crontabs'.

```sh
sudo crontab -e
```

4. Fügen Sie einen Eintrag am Ende der crontabs-Datei hinzu. Die crontabs-Dateien sind einfach strukturiert: Die ersten fünf Felder geben der Reihe nach die Werte für Minuten, Stunden, Tage, Monate und Wochentage an. Mit * werden alle bezeichnet. Wenn die Datei `backup.sh` täglich um 1 Uhr nachts (1:05 AM) ausgeführt werden soll, dann verwenden Sie einen Befehl mit dem folgenden Format:

```sh
5 1 * * * /full/path/to/backup.sh
```

5. Speichern Sie die crontabs-Datei. Sie sind nun bereit für weitere Schritte.
