---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# Verschlüsselung verwalten
{: #encryption}

Alle Objekte, die in {{site.data.keyword.cos_full}} gespeichert sind, werden standardmäßig mit [nach dem Zufallsprinzip generierten Schlüsseln und einer Alles-oder-nichts-Transformation](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security) verschlüsselt. Während dieses Standardverschlüsselungsmodell eine Ruhesicherheit bietet, müssen einige Workloads im Besitz der verwendeten Verschlüsselungsschlüssel sein. Sie können Ihre Schlüssel manuell verwalten, indem Sie eigene Verschlüsselungsschlüssel beim Speichern der Daten (SSE-C) bereitstellen, oder Sie können Buckets erstellen, die IBM Key Protect (SSE-KP) zum Verwalten von Verschlüsselungsschlüsseln verwenden.

## Serverseitige Verschlüsselung mit vom Kunden bereitgestellten Schlüsseln (SSE-C)
{: #encryption-sse-c}

SSE-C wird für Objekte umgesetzt. Anforderungen zum Ausführen von Lese- oder Schreiboperationen für Objekte oder deren Metadaten mithilfe von vom Kunden verwalteten Schlüsseln senden die erforderlichen Verschlüsselungsinformationen als Header in den HTTP-Anforderungen. Die Syntax ist identisch mit der S3-API und S3-kompatible Bibliotheken, die SSE-C unterstützen, können für {{site.data.keyword.cos_full}} wie erwartet ausgeführt werden.

Jede Anforderung, die SSE-C-Header verwendet, muss mit SSL gesendet werden. Beachten Sie hierbei, dass `ETag`-Werte in Antwortheadern *nicht* den MD5-Hashwert des Objekts darstellen, sondern eine nach dem Zufallsprinzip generierte 32-Byte-Hexadezimalzeichenfolge.

Header | Typ  | Beschreibung
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | Zeichenfolge | Dieser Header wird verwendet, um den Algorithmus und die Schlüsselgröße anzugeben, die mit dem Verschlüsselungsschlüssel verwendet werden sollen, der im Header `x-amz-server-side-encryption-customer-key` gespeichert wird. Dieser Wert muss auf die Zeichenfolge `AES256` gesetzt werden.
`x-amz-server-side-encryption-customer-key` | Zeichenfolge | Dieser Header wird verwendet, um die Base64-codierte Bytefolgedarstellung des AES-256-Schlüssels zu transportieren, die im serverseitigen Verschlüsselungsprozess verwendet wird.
`x-amz-server-side-encryption-customer-key-MD5` | Zeichenfolge | Dieser Header wird verwendet, um den Base64-codierten 128-Bit-MD5-Digest-Wert des Verschlüsselungsschlüssels gemäß RFC 1321 zu transportieren. Der Objektspeicher verwendet diesen Wert, um zu validieren, dass der in `x-amz-server-side-encryption-customer-key` übergebene Schlüssel während des Transports und des Codierungsprozesses nicht beschädigt wurde. Der Digest-Wert muss für den Schlüssel berechnet werden, BEVOR der Schlüssel im Base64-Format codiert wird.


## Serverseitige Verschlüsselung mit {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #encryption-kp}

Bei {{site.data.keyword.keymanagementservicefull}} handelt es sich um ein zentrales Schlüsselmanagementsystem (KMS = Key Management System) zum Generieren, Verwalten und Löschen von Verschlüsselungsschlüsseln, die von den {{site.data.keyword.cloud_notm}}-Services verwendet werden. Sie können eine Instanz von {{site.data.keyword.keymanagementserviceshort}} aus dem {{site.data.keyword.cloud_notm}}-Katalog erstellen.

Sobald Sie über eine Instanz von {{site.data.keyword.keymanagementserviceshort}} in einer Region verfügen, in der ein neues Bucket erstellt werden soll, müssen Sie einen Rootschlüssel erstellen und den CRN dieses Schlüssels notieren.

Sie können auswählen, dass {{site.data.keyword.keymanagementserviceshort}} zum Verwalten der Verschlüsselung für ein Bucket nur während der Erstellung verwendet werden soll. Es ist nicht möglich, ein vorhandenes Bucket nachträglich zu ändern, sodass {{site.data.keyword.keymanagementserviceshort}} verwendet wird.
{:tip}

Zur Erstellung des Buckets müssen Sie zusätzliche Header angeben.

Weitere Informationen zu {{site.data.keyword.keymanagementservicelong_notm}} finden Sie in der [Dokumentation](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect).

### Einführung in SSE-KP
{: #sse-kp-gs}

Alle Objekte, die in {{site.data.keyword.cos_full}} gespeichert sind, werden standardmäßig mit mehreren, nach dem Zufallsprinzip generierten Schlüsseln und einer Alles-oder-nichts-Transformation verschlüsselt. Während dieses Standardverschlüsselungsmodell eine Ruhesicherheit bietet, müssen bestimmte Workloads im Besitz der verwendeten Verschlüsselungsschlüssel sein. Sie können  [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) verwenden, um Schlüssel zu erstellen, hinzuzufügen und zu verwalten. Diese Schlüssel können anschließend Ihrer Instanz von {{site.data.keyword.cos_full}} zugeordnet werden, um Buckets zu verschlüsseln.

### Vorbereitungen
{: #sse-kp-prereqs}

Sie benötigen die folgenden Komponenten:
  * [{{site.data.keyword.cloud}} Platform-Konto](http://cloud.ibm.com)
  * [Instanz von {{site.data.keyword.cos_full_notm}}](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * [Instanz von {{site.data.keyword.keymanagementservicelong_notm}}](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * Bestimmte Dateien zum Hochladen auf Ihrem lokalen Computer

### Schlüssel in {{site.data.keyword.keymanagementserviceshort}} erstellen oder hinzufügen
{: #sse-kp-add-key}

Navigieren Sie zu Ihrer Instanz von {{site.data.keyword.keymanagementserviceshort}} und [generieren Sie einen Schlüssel oder geben Sie einen Schlüssel ein](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

### Serviceautorisierung erteilen
{: #sse-kp}
Gehen Sie wie folgt vor, um {{site.data.keyword.keymanagementserviceshort}} zur Verwendung mit IBM COS zu berechtigen:

1. Öffnen Sie das {{site.data.keyword.cloud_notm}}-Dashboard.
2. Klicken Sie in der Menüleiste auf **Verwalten** &gt; **Konto** &gt; **Benutzer**.
3. Klicken Sie in der seitlichen Navigation auf **Identität & Zugriff** &gt; **Autorisierungen**.
4. Klicken Sie auf **Autorisierung erstellen**.
5. Wählen Sie im Menü **Quellenservice** den Eintrag **Cloud Object Storage** aus.
6. Wählen Sie im Menü **Quellenserviceinstanz** die Serviceinstanz aus, die autorisiert werden soll.
7. Wählen Sie im Menü **Zielservice** den Eintrag **{{site.data.keyword.keymanagementservicelong_notm}}** aus.
8. Wählen Sie im Menü **Zielserviceinstanz** die Serviceinstanz aus, die autorisiert werden soll.
9. Aktivieren Sie die Rolle **Leseberechtigter**.
10. Klicken Sie auf **Autorisieren**.

### Bucket erstellen
{: #encryption-createbucket}

Wenn Ihr Schlüssel in {{site.data.keyword.keymanagementserviceshort}} vorhanden ist und Sie den Key Protect-Service für die Verwendung mit IBM COS autorisiert haben, ordnen Sie den Schlüssel einem neuen Bucket zu:

1. Navigieren Sie zu Ihrer Instanz von {{site.data.keyword.cos_short}}.
2. Klicken Sie auf **Bucket erstellen**.
3. Geben Sie einen Bucketnamen ein, wählen Sie den Ausfallsicherheitstyp **Regional** und dann einen Standort und eine Speicherklasse aus.
4. Aktivieren Sie in 'Erweiterte Konfiguration' die Option **Key Protect-Schlüssel hinzufügen**.
5. Wählen Sie die zugehörige Key Protect-Serviceinstanz, den Schlüssel und die Schlüssel-ID aus.
6. Klicken Sie auf **Erstellen**.

In der Liste **Buckets und Objekte** verfügt das Bucket nun unter **Erweitert** über ein Schlüsselsymbol, das angibt, dass das Bucket über einen aktivierten Key Protect-Schlüssel verfügt. Um die Schlüsseldetails anzuzeigen, klicken Sie auf das Menü rechts neben dem Bucket und dann auf **Key Protect-Schlüssel anzeigen**.

Beachten Sie hierbei, dass der Wert für `Etag`, der für Objekte zurückgegeben wird, die mit SSE-KP verschlüsselt wurden, mit dem MD5-Hashwert des ursprünglichen unverschlüsselten Objekts **übereinstimmt**.
{:tip}


## Schlüssel turnusmäßig wechseln
{: #encryption-rotate}

Das turnusmäßige Wechseln der verwendeten Schlüssel stellt einen wichtigen Faktor bei der Reduzierung des Risikos für Datenschutzverletzungen dar. Durch das Wechseln der Schlüssel in regelmäßigen Zeitabständen wird das Risiko eines Datenverlusts reduziert, wenn der Schlüssel z. B. verloren geht oder manipuliert wurde. Die Häufigkeit, mit der die Schlüssel gewechselt werden, kann von Organisation zu Organisation variieren und hängt von einer Reihe von Faktoren wie beispielsweise der Umgebung, des Volumens an verschlüsselten Daten, der Klassifizierung der Daten und der gesetzlichen Complianceregelungen ab. Das [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){:new_window} stellt Definitionen für geeignete Schlüssellängen und Richtlinien für die Verwendungsdauer von Schlüsseln zur Verfügung.

### Turnusmäßigen Wechsel von Schlüsseln manuell durchführen
{: #encryption-rotate-manual}

Um Ihre {{site.data.keyword.cos_short}}-Schlüssel turnusmäßig zu wechseln, müssen Sie ein neues Bucket erstellen, für das die Key Protect-Funktion mit einem neuen Rootschlüssel aktiviert wurde. Außerdem müssen Sie den Inhalt aus dem vorhandenen Bucket in das neue Bucket kopieren.

**HINWEIS**: Wenn Sie einen Schlüssel aus dem System löschen, werden die Inhalte und die Daten, die mit diesem Schlüssel verschlüsselt wurden, unbrauchbar gemacht. Einmal entfernt, kann die Löschung eines Schlüssels nicht rückgängig gemacht oder umgekehrt werden und führt zu einem dauerhaften Verlust der betroffenen Daten.

1. Erstellen Sie einen neuen Rootschlüssel in Ihrem [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)-Service oder fügen Sie einen neuen Rootschlüssel hinzu.
2. [Erstellen Sie ein neues Bucket](#encryption-createbucket) und fügen Sie den neuen Rootschlüssel hinzu.
3. Kopieren Sie alle Objekte aus dem ursprünglichen Bucket in das neue Bucket.
    1. Dieser Schritt kann mit verschiedenen Methoden durchgeführt werden:
        1. Über die Befehlszeile mit [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) oder der [AWS-CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli).
        2. Über die (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object].
        3. Über das SDK mit [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) oder [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go).
