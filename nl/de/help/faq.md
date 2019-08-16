---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: faq, questions

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

# Häufig gestellte Fragen (FAQs)
{: #faq}

## Fragen zur API
{: #faq-api}

**Muss bei Namen von {{site.data.keyword.cos_full}}-Buckets die Groß-/Kleinschreibung beachtet werden?**

Bucketnamen müssen über den DNS adressierbar sein. Daher muss bei ihnen die Groß-/Kleinschreibung nicht beachtet werden.

**Was ist die maximale Anzahl von Zeichen, die in einem Objektnamen verwendet werden dürfen?**

1024 Zeichen.

**Wie finde ich die Gesamtgröße meines Buckets über die API heraus?**

Es ist nicht möglich, die Größe eines Buckets mit einer einzigen Anforderung zu ermitteln. Vielmehr müssen Sie den Inhalt eines Buckets auflisten und dann die Größenangaben der einzelnen Objekte addieren.

**Kann ich Daten von AWS S3 auf {{site.data.keyword.cos_full_notm}} migrieren?**

Ja, Sie können Ihre vorhandenen Tools verwenden, um Daten in {{site.data.keyword.cos_full_notm}} zu lesen und zu schreiben. Es ist jedoch erforderlich, die HMAC-Berechtigungsnachweise zu konfigurieren, damit sich Ihre Tools authentifizieren können. Gegenwärtig werden nicht alle S3-kompatiblen Tools unterstützt. Weitere Informationen enthält der Abschnitt [HMAC-Berechtigungsnachweise verwenden](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).


## Fragen zum Produktangebot
{: #faq-offering}

**Besteht für ein Konto ein Grenzwert von maximal 100 Buckets? Was geschieht, wenn mehr Buckets benötigt werden?**

Ja, 100 Buckets stellen derzeit die oberen Grenzwert dar. Im Allgemeinen stellen Präfixe die bessere Option für die Gruppierung von Objekten in einem Bucket dar, es sei denn, die Daten müssen sich in einer anderen Region oder in einer anderen Speicherklasse befinden. Um Patientenakten zu gruppieren, würden Sie beispielsweise ein Präfix pro Patient verwenden. Sollte dies keine praktikable Lösung darstellen, wenden Sie sich an die Kundenunterstützung.

**Muss ich ein anderes Konto erstellen, wenn ich meine Daten mit Vault oder Cold Vault von {{site.data.keyword.cos_full_notm}} speichern möchte?**

Nein, Speicherklassen (sowie Regionen) werden auf Bucketebene definiert. Erstellen Sie einfach ein neues Bucket, für das die gewünschte Speicherklasse festgelegt ist.

**Wie lege ich die Speicherklasse fest, wenn ich ein Bucket über die API erstelle?**

Die Speicherklasse (zum Beispiel `us-flex`) wird der Konfigurationsvariablen `LocationConstraint` für dieses Bucket zugewiesen. Dies ist durch einen wesentlichen Unterschied zwischen der Art und Weise bedingt, in der +AWS S3 und {{site.data.keyword.cos_full_notm}} Speicherklassen verarbeiten. {{site.data.keyword.cos_short}} legt Speicherklassen auf Bucketebene fest, während AWS S3 einem einzelnen Objekt eine Speicherklasse zuweist. Eine Liste der gültigen Bereitstellungscodes für `LocationConstraint` kann im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes) nachgeschlagen werden.

**Kann die Speicherklasse eines Buckets geändert werden? Wenn zum Beispiel Produktionsdaten in 'Standard' gespeichert sind, können wir diese aus Gründen der Abrechnung einfach zu 'Vault' wechseln, sofern wir sie nicht häufig verwenden?**

Heutzutage erfordert das Ändern der Speicherklasse das manuelle Verschieben oder Kopieren der Daten aus einem Bucket in ein anderes Bucket mit der gewünschten Lagerklasse.


## Fragen zum Leistungsverhalten
{: #faq-performance}

**Geht die Datenkonsistenz in {{site.data.keyword.cos_short}} mit einer Beeinträchtigung des Leistungsverhaltens einher?**

Konsistenz hat bei jedem verteilten System seinen Preis, aber die Effizienz des verteilten {{site.data.keyword.cos_full_notm}}-Speichersystems ist höher und der Systemaufwand ist niedriger als bei Systemen mit mehreren synchronen Kopien.

**Gibt es keine Auswirkungen auf die Leistung, wenn meine Anwendung umfangreiche Objekte bearbeiten muss?**

Zur Leistungsoptimierung können Objekte zeitgleich (parallel) in mehreren Teilen hoch- und heruntergeladen werden.


## Fragen zur Verschlüsselung
{: #faq-encryption}

**Stellt {{site.data.keyword.cos_short}} Verschlüsselung für ruhende und für bewegte Daten bereit?**

Ja. Ruhende Daten werden mit automatischer providerseitiger 256-Verschlüsselung des erweiterten Verschlüsselungsstandards (EAS, Advanced Encryption Standard) und der kryptologischen Hashfunktion SHA-256 (SHA: Secure Hash Algorithm) verschlüsselt. Bewegte Daten werden mit der integrierten Transport Layer Security/Secure Sockets Layer (TLS/SSL) in Betreiberqualität oder SNMPv3 mit AES-Verschlüsselung gesichert.

**Wie hoch ist der typische Verschlüsselungsaufwand, wenn ein Kunde seine Daten verschlüsseln möchte?**

Die serverseitige Verschlüsselung für Kundendaten ist stets aktiviert. Im Vergleich zum Hashing, das bei der S3-Authentifizierung und der Löschungskodierung erforderlich ist, spielt die Verschlüsselung keine große Rolle bei den Verarbeitungskosten von COS.

**Verschlüsselt {{site.data.keyword.cos_short}} alle Daten?**

Ja, {{site.data.keyword.cos_short}} verschlüsselt alle Daten.

**Besteht bei {{site.data.keyword.cos_short}} für die Verschlüsselungsalgorithmen Konformität mit FIPS 140-2?**

Ja, das IBM COS Federal-Angebot ist im Federal Risk and Authorization Management Program (FedRAMP) der US-Regierung für mittlere Sicherheitssteuerungen genehmigt, für die eine validierte FIPS-Konfiguration erforderlich ist. IBM COS Federal ist nach FIPS 140-2 Stufe 1 zertifiziert. Weitere Informationen zum COS Federal-Angebot erhalten Sie, indem Sie über unserer Bundesstelle [Kontakt zu uns](https://www.ibm.com/cloud/government) aufnehmen.

**Wird die Verschlüsselung von Clientschlüsseln unterstützt?**

Ja, die Verschlüsselung von Clientschlüsseln wird unter Verwendung von SSE-C oder Key Protect unterstützt.

## Allgemeine Fragen
{: #faq-general}

**Wie viele Objekte passen in ein einziges Bucket?**

Es gibt keine praktische Begrenzung der Anzahl von Objekten in einem einzigen Bucket.

**Können Buckets ineinander verschachtelt werden?**

Nein, Buckets können nicht ineinander verschachtelt werden. Wenn innerhalb eines Buckets ein höheres Maß an Organisation erforderlich ist, kann dies über Präfixe erfolgen, deren Verwendung unterstützt wird: `{endpunkt}/{bucketname}/{objektpräfix}/{objektname}`. Beachten Sie, dass der Objektschlüssel weiterhin aus der Kombination `{objektpräfix}/{objektname}` besteht.

**Worin besteht der Unterschied zwischen Anforderungen der 'Klasse A' und 'Klasse B'?**

Anforderungen der 'Klasse A' sind Operationen, die eine Änderung oder Auflistung einbeziehen. Dies umfasst das Erstellen von Buckets, das Hochladen oder Kopieren von Objekten, das Erstellen oder Ändern von Konfigurationen, das Auflisten von Buckets und das Auflisten des Inhalts von Buckets. Anforderungen der 'Klasse B' sind solche, die sich auf das Abrufen von Objekten oder ihrer zugehörigen Metadaten/Konfigurationen aus dem System beziehen. Für das Löschen von Buckets oder Objekten aus dem System fällt keine Gebühr an.

**Was ist der beste Weg, um Daten mithilfe von Object Storage so zu strukturieren, dass Sie sie 'ansehen' können und finden, wonach Sie suchen? Ohne eine Verzeichnisstruktur scheint es schwierig, Tausende von Dateien auf einer Ebene anzeigen zu können.**

Sie können die jedem Objekt zugeordneten Metadaten verwenden, um die gesuchten Objekte zu finden. Der größte Vorteil des Objektspeichers besteht aus den Metadaten, die jedem Objekt zugeordnet sind. Für jedes Objekt können bis zu 4 MB Metadaten in {{site.data.keyword.cos_short}} vorhanden sein. Wenn Metadaten in eine Datenbank ausgelagert werden, bieten sie hervorragende Suchfunktionen. In 4 MB kann eine große Anzahl von Schlüssel/Wert-Paaren gespeichert werden. Sie können auch die Suche mit Präfixen verwenden, um das Gesuchte zu finden. Wenn Sie beispielsweise Buckets verwenden, die Daten einzelner Kunden voneinander zu trennen, können Sie zur Organisation in den Buckets selbst Präfixe verwenden. Beispiel: /bucket1/folder/object, wobei 'folder/' das Präfix darstellt.

**Können Sie bestätigen, dass {{site.data.keyword.cos_short}} 'sofort konsistent' im Gegensatz zu 'schließlich konsistent' ist?**

{{site.data.keyword.cos_short}} ist für Daten 'sofort konsistent' und für die Nutzungsabrechnung 'letztendlich konsistent'.


**Kann {{site.data.keyword.cos_short}} die Daten wie HDFS automatisch so partitionieren, dass ich die Partitionen parallel lesen kann, zum Beispiel mit Spark?**

{{site.data.keyword.cos_short}} unterstützt eine Anforderung vom Typ GET mit Bereich für das Objekt, so dass eine Anwendung eine verteilte einheitenübergreifende Operation vom Typ 'Lesen' durchführen kann. Die Verwaltung des Striping wäre Aufgabe der Anwendung.
