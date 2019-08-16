---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: s3fs, open source, file system, gateway

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

# Bucket mit `s3fs` anhängen
{: #s3fs}

Anwendungen, die voraussichtlich zur Ausführung von Lese- und Schreiboperationen in einem NFS-Dateisystem verwendet werden, können `s3fs` benutzen, um ein Bucket als Verzeichnis anzuhängen, wobei gleichzeitig das native Objektformat für Dateien beibehalten werden kann. Dies ermöglicht Ihnen die Interaktion mit Ihrem Cloudspeicher mithilfe gängiger Shellbefehle wie z. B. `ls` zur Auflistung oder `cp` zum Kopieren von Dateien. Des Weiteren können Sie auf diese Weise traditionellen Anwendungen, mit denen Lese- und Schreiboperationen in lokalen Dateien durchgeführt werden, den Zugriff ermöglichen. Eine ausführlichere Übersicht hierzu finden Sie in der [offiziellen Readme-Datei des Projekts](https://github.com/s3fs-fuse/s3fs-fuse).

## Voraussetzungen
{: #s3fs-prereqs}

* IBM Cloud-Konto und eine Instanz von {{site.data.keyword.cos_full}}
* Linux- oder OSX-Umgebung
* Berechtigungsnachweise (entweder [IAM-API-Schlüssel](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) oder [HMAC-Berechtigungsnachweise](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac))

## Installation durchführen
{: #s3fs-install}

Verwenden Sie unter OSX [Homebrew](https://brew.sh/):

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

Verwenden Sie für Debian oder Ubuntu folgenden Befehl: 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

In der offiziellen `s3fs`-Dokumentation wird die Verwendung von `libcurl4-gnutls-dev` anstelle von `libcurl4-openssl-dev` empfohlen. Beide Optionen funktionieren, aber die Verwendung der OpenSSL-Version kann zu einer Verbesserung der Leistung führen.
{:tip}

Sie können `s3fs` auch auf Basis der Quelle erstellen. Klonen Sie dazu zuerst das GitHub-Repository:

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

Erstellen Sie anschließend `s3fs`:

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

Installieren Sie die Binärdatei:

```sh
sudo make install
```
{:codeblock}

## Konfiguration
{: #s3fs-config}

Speichern Sie Ihre Berechtigungsnachweise in einer Datei, die entweder `<access_key>:<secret_key>` oder `:<api_key>` enthält. Diese Datei muss über eingeschränkten Zugriff verfügen. Führen Sie folgenden Befehl aus: 

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

Nun können Sie ein Bucket mit dem folgenden Befehl anhängen:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

Wenn die Berechtigungsnachweisdatei nur über einen API-Schlüssel verfügt (keine HMAC-Berechtigungsnachweise), müssen Sie auch das Flag `ibm_iam_auth` hinzufügen:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

In `<bucket>` wird ein bereits vorhandenes Bucket angegeben und in `<mountpoint>` das lokale Verzeichnis, in dem das Bucket angehängt werden soll. Der Wert in `<endpoint>` muss mit dem [Standort des Buckets](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) übereinstimmen. Für `credentials_file` wird die Datei angegeben, die mit dem API-Schlüssel oder den HMAC-Berechtigungsnachweisen erstellt wurde.

Mit dem Befehl `ls <mountpoint>` können Sie nun die Objekte in diesem Bucket in derselben Weise wie bei lokalen Dateien (oder bei Verwendung von Objektpräfixen wie bei verschachtelten Verzeichnissen) auflisten.

## Leistungsoptimierung
{: #s3fs-performance}

Obwohl die Leistung niemals mit der eines tatsächlichen lokalen Dateisystems vergleichbar ist, kann der Durchsatz mit einigen erweiterten Optionen dennoch verbessert werden. 

```sh
s3fs <bucket_name> <mountpoint> -o url=http{s}://<COS_endpoint> –o passwd_file=<credentials_file> \
-o cipher_suites=AESGCM \
-o kernel_cache \
-o max_background=1000 \
-o max_stat_cache_size=100000 \
-o multipart_size=52 \
-o parallel_count=30 \
-o multireq_max=30 \
-o dbglevel=warn
```
{:codeblock}

1. `cipher_suites=AESGCM` ist nur bei Verwendung eines HTTPS-Endpunkts relevant. Standardmäßig verwenden sichere Verbindungen zu IBM COS die Cipher-Suite `AES256-SHA`. Die Verwendung einer `AESGCM`-Suite führt hingegen auf Ihrem Clientsystem zu einer erheblichen Reduzierung des CPU-Aufwands, der durch die TLS-Verschlüsselungsfunktionen verursacht wird, wobei derselbe Grad der Verschlüsselungssicherheit bereitgestellt wird.
2. `kernel_cache` aktiviert den Kernelpuffercache für Ihren `s3fs`-Mountpunkt. Dies bedeutet, dass Objekte von `s3fs` nur einmal gelesen werden, da wiederholte Lesevorgänge für dieselbe Datei über den Puffercache des Kernels ausgeführt werden können. Der Kernelpuffercache verwendet nur freien Speicher, der von anderen Prozessen nicht verwendet wird. Diese Option ist nicht zu empfehlen, wenn zu erwarten ist, dass die Bucketobjekte durch einen anderen Prozess bzw. ein anderes System überschrieben werden, während das Bucket angehängt wird, und wenn für Ihren Anwendungsfall Livezugriff auf die aktuellsten Inhalte benötigt wird. 
3. `max_background=1000` verbessert die Leistung von `s3fs` bei gleichzeitigen Dateileseoperationen. FUSE unterstützt standardmäßig Dateileseanforderungen von bis zu 128 KB. Bei Leseanforderungen für größere Datenvolumen wird die große Anforderung vom Kernel in kleinere Unteranforderungen aufgeteilt, die dann von s3fs asynchron verarbeitet werden können. Mit der Option `max_background` wird die globale maximale Anzahl solcher gleichzeitig verarbeiteten asynchronen Anforderungen festgelegt. Standardmäßig ist diese Anzahl auf den Wert '12' gesetzt. Wird sie jedoch auf einen beliebigen, hohen Wert (1000) gesetzt, kann die Blockierung von Leseanforderungen auch dann verhindert werden, wenn eine große Anzahl von Dateien gleichzeitig gelesen wird.
4. `max_stat_cache_size=100000` reduziert die Anzahl der redundanten HTTP-Anforderungen vom Typ `HEAD`, die von `s3fs` gesendet werden, und reduziert die Zeitdauer, die zum Auflisten eines Verzeichnisses oder zum Abrufen von Dateiattributen benötigt wird. Bei der typischen Dateisystemverwendung wird häufig mit einem Aufruf für `stat()` auf die Metadaten von Dateien zugegriffen. Dadurch wird eine Zuordnung zur entsprechenden `HEAD`-Anforderung im Objektspeichersystem hergestellt. Standardmäßig werden in `s3fs` die Attribute (Metadaten) von bis zu 1000 Objekten im Cache zwischengespeichert. Jeder zwischengespeicherte Eintrag benötigt bis zu 0,5 KB an Speicherplatz. Idealerweise sollten die Metadaten aller Objekte in Ihrem Bucket im Cache zwischengespeichert werden können. Allerdings sollten Sie die Auswirkungen dieser Cacheeinstellung auf die Speicherbelegung sorgfältig überprüfen. Wenn Sie `100000` angeben, dann werden lediglich 0,5 KB * 100000 = 50 MB belegt.
5. `multipart_size=52` legt die maximale Größe der Anforderungen und Antworten, die vom COS-Server gesendet bzw. empfangen werden, fest. Die Werte werden in Megabyte (MB) angegeben. `s3fs` legt für diesen Wert standardmäßig 10 MB fest. Durch die Erhöhung dieses Werts wird auch der Durchsatz (MB/s) für die jeweilige HTTP-Verbindung erhöht. Andererseits wird die Latenzzeit für das erste Byte, das aus der Datei verarbeitet wird, entsprechend erhöht. Wenn in Ihrem Anwendungsfall lediglich ein geringes Datenvolumen aus den einzelnen Dateien gelesen wird, dann ist eine Erhöhung dieses Werts voraussichtlich nicht sinnvoll. Darüber hinaus wird für große Objekte (z. B. über 50 MB) der Durchsatz erhöht, wenn dieser Wert klein genug ist, um die Datei gleichzeitig mit mehreren Anforderungen abrufen zu können. Erfahrungsgemäß liegt der optimale Wert für diese Option bei ca. 50 MB. In den bewährten Verfahren (Best Practices) für COS wird die Verwendung von Anforderungen empfohlen, deren Größe ein Vielfaches von 4 MB darstellt. Aus diesem Grund wird für diese Option ein Wert von 52 (MB) empfohlen.
6. `parallel_count=30` legt die maximale Anzahl an Anforderungen pro Lese-/Schreiboperation für eine Einzeldatei fest, die gleichzeitig an COS gesendet werden können. Standardmäßig ist dieser Wert auf '5' gesetzt. Bei sehr großen Objekten können Sie mehr Durchsatz erzielen, indem Sie diesen Wert erhöhen. Wie bei der vorherigen Option sollten Sie diesen Wert niedrig halten, wenn Sie nur ein kleines Datenvolumen aus jeder Datei lesen.
7. `multireq_max=30` Beim Auflisten eines Verzeichnisses wird pro Objekt in der Liste eine Objektmetadatenanforderung (`HEAD`) gesendet (sofern die Metadaten nicht im Cache gefunden werden). Mit dieser Option wird die Anzahl gleichzeitig an COS gesendeter Anforderungen dieses Typs in Bezug auf eine einzelne Verzeichnislistenoperation begrenzt. Standardmäßig ist dieser Wert auf '20' gesetzt. Beachten Sie, dass dieser Wert größer oder gleich dem Wert sein muss, der für die Option `parallel_count` angegeben wird (siehe oben).
8. `dbglevel=warn` setzt die Debugstufe für die Protokollierung von Nachrichten in '/var/log/syslog' auf `warn` und nicht auf den Standardwert (`crit`).

## Einschränkungen
{: #s3fs-limitations}

Beachten Sie unbedingt, dass 's3fs' möglicherweise nicht für alle Anwendungen geeignet ist, da es bei Objektspeicherservices zu hohen Latenzzeiten für die Zeitdauer bis zum ersten Byte kommen kann und Defizite beim wahlfreien Schreibzugriff bestehen können. Workloads, die nur große Dateien (z. B. Deep-Learning-Workloads) lesen, können mit `s3fs` jedoch gute Durchsatzraten erzielen. 
