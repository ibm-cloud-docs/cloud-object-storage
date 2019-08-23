---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Aspera-Hochgeschwindigkeitsübertragung verwenden
{: #aspera}

Die Verwendung der Aspera-Hochgeschwindigkeitsübertragung beseitigt die Einschränkungen herkömmlicher FTP- und HTTP-Übertragungen und führt in den meisten Fällen zu einer Verbesserung der Leistung bei der Datenübertragung. Dies gilt insbesondere für Netze, bei denen es zu langen Latenzzeiten und zu Paketverlusten kommt. Anstelle einer standardmäßigen `PUT`-Anforderung via HTTP wird bei der Aspera-Hochgeschwindigkeitsübertragung für den Upload von Objekten das [FASP-Protokoll](https://asperasoft.com/technology/transport/fasp/) benutzt. Die Verwendung der Aspera-Hochgeschwindigkeitsübertragung für Uploads und Downloads bietet die folgenden Vorteile:

- Schnellere Übertragungsgeschwindigkeiten
- Übertragung großer Objekte bei Uploads mit mehr als 200 MB in der Konsole und Übertragung von Objekten mit 1 GB über ein SDK oder eine Bibliothek
- Upload kompletter Ordner mit beliebigem Datentyp einschließlich Multimediadateien, Plattenimages und anderer strukturierter oder unstrukturierter Daten
- Anpassung der Übertragungsgeschwindigkeiten und Standardbenutzervorgaben
- Übertragungen können unabhängig voneinander angezeigt, angehalten/fortgesetzt oder abgebrochen werden

Die Aspera-Hochgeschwindigkeitsübertragung steht in der {{site.data.keyword.cloud_notm}} [Konsole](#aspera-console) zur Verfügung und kann außerdem mithilfe eines [SDK](#aspera-sdk) programmgesteuert verwendet werden. 

Die Aspera-Hochgeschwindigkeitsübertragung steht nur in bestimmten Regionen zur Verfügung. Weitere Einzelheiten zu diesem Thema finden Sie im Abschnitt [Integrierte Services](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability).
{:tip}

## Konsole verwenden
{: #aspera-console}

Wenn Sie ein Bucket in einer [unterstützten Region](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability) erstellen, haben Sie die Option, die Aspera-Hochgeschwindigkeitsübertragung zum Hochladen von Dateien oder Ordnern auszuwählen. Nachdem Sie versucht haben, ein Objekt hochzuladen, werden Sie aufgefordert, den Aspera Connect-Client zu installieren.

### Aspera Connect installieren
{: #aspera-install}

1. Wählen Sie den Client **Aspera Connect installieren** aus.
2. Befolgen Sie abhängig vom verwendeten Betriebssystem und Browser die jeweils geltenden Installationsanweisungen.
3. Setzen Sie den Datei- oder Ordnerupload fort.

Das Aspera Connect-Plug-in kann auch direkt über die [Aspera-Website](https://downloads.asperasoft.com/connect2/) installiert werden. Hilfe zur Fehlerbehebung bei Problemen mit dem Aspera Connect-Plug-in finden Sie in der [Dokumentation](https://downloads.asperasoft.com/en/documentation/8).

Sobald das Plug-in installiert ist, haben Sie die Option, die Aspera-Hochgeschwindigkeitsübertragung als Standardwert für alle Uploads in das Zielbucket festzulegen, die denselben Browser verwenden. Wählen Sie **Meine Browservorgaben speichern** aus. Die möglichen Optionen sind auch auf der Bucketkonfigurationsseite unter den **Übertragungsoptionen** verfügbar. Mit diesen Optionen können Sie zwischen der Standard- und der Hochgeschwindigkeitsübertragung als Standardtransport für Uploads und Downloads wählen.

Normalerweise stellt die webbasierte Konsole von IBM Cloud Object Storage nicht die am häufigsten genutzte Methode zur Verwendung von {{site.data.keyword.cos_short}} dar. Die Standardübertragungsoption begrenzt die Größe der Objekte auf 200 MB, wobei der Dateiname und der Schlüssel identisch sind. Die Unterstützung für größere Objekte und die (abhängig von bestimmten Netzfaktoren) verbesserte Leistung wird durch die Aspera-Hochgeschwindigkeitsübertragung bereitgestellt.

### Übertragungsstatus
{: #aspera-console-transfer-status}

**Aktiv:** Nachdem Sie eine Übertragung gestartet haben, wird als Übertragungsstatus 'Aktiv' angezeigt. Während die Übertragung aktiv ist, können Sie eine aktive Übertragung anhalten, fortsetzen oder abbrechen. 

**Abgeschlossen:** Nach Abschluss Ihrer Übertragung werden auf der Registerkarte 'Abgeschlossen' Informationen zur aktuellen Übertragung und zu allen weiteren Übertragungen in dieser Sitzung angezeigt. Diese Informationen können gelöscht werden. Es werden ausschließlich Informationen zu Übertragungen angezeigt, die in der aktuellen Sitzung abgeschlossen wurden.

**Vorgaben:** Sie können den Standardwert für Uploads und/oder Downloads auf Hochgeschwindigkeit festlegen.

Für Downloads mit der Aspera-Hochgeschwindigkeitsübertragung fallen egress-Gebühren an. Weitere Informationen hierzu finden Sie auf der [Seite zur Preisstruktur](https://www.ibm.com/cloud/object-storage).
{:tip}

**Erweiterte Vorgaben:** Sie können für Uploads und Downloads die Bandbreite festlegen.

----

## Bibliotheken und SDKs verwenden
{: #aspera-sdk}

Das SDK für die Aspera-Hochgeschwindigkeitsübertragung bietet Ihnen die Möglichkeit, die Hochgeschwindigkeitsübertragung in Ihren angepassten Anwendungen zu starten, wenn Sie mit Java oder Python arbeiten.

### Hinweise zur Verwendung der Aspera-Hochgeschwindigkeitsübertragung
{: #aspera-guidance}

Das FASP-Protokoll, das für die Aspera-Hochgeschwindigkeitsübertragung verwendet wird, eignet sich nicht für alle Datenübertragungen von und an COS. Für Übertragungen, die mit der Aspera-Hochgeschwindigkeitsübertragung arbeiten, gelten folgende Voraussetzungen:

1. Verwenden Sie stets mehrere Sitzungen. Zur Nutzung der Funktionalität der Aspera-Hochgeschwindigkeitsübertragung sollten mindestens zwei parallele Sitzungen verwendet werden. Lesen Sie hierzu die speziellen Anleitungen zu [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) und [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
2. Die Aspera-Hochgeschwindigkeitsübertragung ist ideal für größere Dateien geeignet. Dateien oder Verzeichnisse, die ein Gesamtdatenvolumen kleiner als 1 GB umfassen, sollten das entsprechende Objekt stattdessen in mehreren Teilen übertragen und dazu die standardmäßigen Klassen des Übertragungsmanagers verwenden. Aspera-Hochgeschwindigkeitsübertragungen benötigen mehr Zeit bis zur Übertragung des ersten Byte, als dies bei normalen HTTP-Übertragungen der Fall ist. Die Instanziierung zahlreicher Objekte des Aspera-Übertragungsmanagers zum Verwalten der Übertragungen einzelner kleinerer Dateien kann im Vergleich zur Verwendung von HTTP-Basisanforderungen zu einer verringerten Leistung führen, sodass am besten ein einzelner Client instanziiert werden sollte, um stattdessen ein Verzeichnis mit kleineren Dateien hochzuladen.
3. Die Aspera-Hochgeschwindigkeitsübertragung wurde teils zur Verbesserung der Leistung in Netzumgebungen mit starken Paketverlusten konzipiert, sodass die Protokollperformanz über größere Distanzen und in öffentlichen WLANs gewährleistet ist. Die Aspera-Hochgeschwindigkeitsübertragung sollte nicht für Übertragungen in einer Region oder einem Rechenzentrum verwendet werden.

Das SDK für die Aspera-Hochgeschwindigkeitsübertragung ist ein Closed-Source-Produkt und stellt deshalb eine optionale Abhängigkeit für das COS-SDK dar (das mit einer Apache-Lizenz arbeitet).
{:tip}

#### Paketierung für COS/Aspera-Hochgeschwindigkeitsübertragung
{: #aspera-packaging}

In der folgenden Abbildung finden Sie eine allgemeine Übersicht zur Interaktion des COS-SDK mit der Bibliothek für die Aspera-Hochgeschwindigkeitsübertragung, um die Funktionalität bereitzustellen.

<img alt="SDK für COS/Aspera-Hochgeschwindigkeitsübertragung." src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="Abbildung 1: SDK für COS/Aspera-Hochgeschwindigkeitsübertragung." caption-side="bottom"} 

### Unterstützte Plattformen
{: #aspera-sdk-platforms}

| Betriebssystem         | Version   | Architektur  | Getestete Java-Version| Getestete Python-Version|
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64-Bit       | 6 und höher  | 2.7, 3.6       |
| Mac OS X               | 10.13     | 64-Bit       | 6 und höher  | 2.7, 3.6       |
| Microsoft&reg; Windows | 10        | 64-Bit       | 6 und höher  | 2.7, 3.6       |

Jede Sitzung für eine Aspera-Hochgeschwindigkeitsübertragung generiert einen individuellen `ascp`-Prozess, der auf dem Clientsystem zur Durchführung der Übertragung ausgeführt wird. Vergewissern Sie sich, dass Ihre Datenverarbeitungsumgebung die Ausführung dieses Prozesses unterstützt.
{:tip}

**Zusätzliche Einschränkungen**

* 32-Bit-Binärkomponenten werden nicht unterstützt.
* Zur Windows-Unterstützung wird Windows 10 benötigt.
* Die Linux-Unterstützung ist auf Ubuntu (getestet für 18.04 LTS) beschränkt.
* Clients des Aspera-Übertragungsmanagers müssen mit IAM-API-Schlüsseln und nicht mit HMAC-Berechtigungsnachweisen erstellt werden.

### SDK mithilfe von Java abrufen
{: #aspera-sdk-java} 
{: java}

Die beste Möglichkeit zur Verwendung von {{site.data.keyword.cos_full_notm}} und des Java-SDK für die Aspera-Hochgeschwindigkeitsübertragung besteht in der Verwendung von Maven zur Verwaltung der Abhängigkeiten. Wenn Sie noch nicht mit Maven gearbeitet haben, dann können Sie sich mithilfe des Leitfadens [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window} mit Maven vertraut machen.
{: java}

Maven verwendet eine Datei mit dem Namen `pom.xml`, um die Bibliotheken (und deren Versionen) anzugeben, die für ein Java-Projekt erforderlich sind. Im Folgenden finden Sie die Beispieldatei `pom.xml` für die Verwendung von {{site.data.keyword.cos_full_notm}} und des Java-SDK für die Aspera-Hochgeschwindigkeitsübertragung.
{: java}

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera</artifactId>
            <version>0.1.163682</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}
{: java}

Beispiele zum Starten der Aspera-Hochgeschwindigkeitsübertragung mit Java sind im Abschnitt [Aspera-Hochgeschwindigkeitsübertragung verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) verfügbar.
{: java}

### SDK mithilfe von Python abrufen
{: #aspera-sdk-python} 
{: python}

Die Python-SDKs für {{site.data.keyword.cos_full_notm}} und die Aspera-Hochgeschwindigkeitsübertragung stehen im PyPI-Software-Repository (PyPI = Python Package Index) zur Verfügung.
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

Beispiele zum Starten von Aspera-Übertragungen mit Python sind im Abschnitt [Aspera-Hochgeschwindigkeitsübertragung verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera) verfügbar.
{: python}
