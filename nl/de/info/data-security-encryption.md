---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# Datensicherheit und -verschlüsselung
{: #security}

{{site.data.keyword.cos_full}} verwendet einen innovativen Ansatz, um große Mengen unstrukturierter Daten kostengünstig zu speichern und gleichzeitig Sicherheit, Verfügbarkeit und Zuverlässigkeit zu gewährleisten. Dies wird durch die Verwendung von Algorithmen zur Streuung von Informationen (Information Dispersal Algorithms, IDAs) erreicht, um Daten in nicht erkennbare 'Slices' zu zerlegen, die über ein Netz von Rechenzentren verteilt sind, wodurch die Übertragung und Speicherung von Daten grundsätzlich privat und sicher wird. Es befindet sich zu keinem Zeitpunkt eine vollständige Kopie der Daten in einem einzelnen Speicherknoten und es muss lediglich eine Teilmenge von Knoten verfügbar sein, um die Daten im Netz vollständig abrufen zu können.

Alle Daten in {{site.data.keyword.cos_full_notm}} werden im Ruhezustand verschlüsselt. Bei dieser Technologie wird jedes Objekt individuell mit Hilfe von pro Objekt generierten Schlüsseln verschlüsselt. Diese Schlüssel werden mit denselben gleichen Algorithmen zur Streuung von Informationen gesichert und zuverlässig gespeichert, die Objektdaten mit einer Alles-oder-nichts-Transformation (AONT, All-or-Nothing-Transformation) schützen, welche eine Offenlegung von Schlüsseldaten verhindert, falls einzelne Knoten oder Festplatten beeinträchtigt sind.

Wenn es für den Benutzer notwendig ist, Verschlüsselungsschlüssel steuern zu können, können Rootschlüssel auf einer [Objektbasise mit SSE-C](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c) oder auf einer [Bucketbasis mit SSE-KP](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp) bereitgestellt werden.

Auf den Speicher kann über HTTPS zugegriffen werden und interne Speichergeräte sind zertifiziert und kommunizieren über TLS miteinander.


## Löschung von Daten
{: #security-deletion}

Nach dem Löschen der Daten gibt es unterschiedliche Mechanismen, die eine Wiederherstellung oder Rekonstruktion der gelöschten Objekte verhindern. Zum Löschen eines Objekts werden unterschiedliche Stufen durchlaufen, vom Markieren der Metadaten, die das Objekt als gelöscht kennzeichnen, über das Entfernen der Inhaltsregionen und die Beendigung der Löschung auf den Laufwerken selbst bis hin zur letztendlichen Überschreibung der Blöcke, die diese Slicedaten darstellen. Je nachdem, ob das Rechenzentrum beeinträchtigt ist oder sich im Besitz der physischen Festplatten befindet, richtet sich die Zeit, nach deren Verstreichen ein Objekt nicht mehr wiederhergestellt werden kann, nach der Phase der Löschoperation. Wenn das Metadatenobjekt aktualisiert wird, können externe Clients aus dem Rechenzentrumsnetz  das Objekt nicht mehr lesen. Wenn eine Mehrheit der 'Slices', die die Inhaltsregionen darstellen, von den Speichermedien abgeschlossen worden ist, ist es nicht möglich, auf das Objekt zuzugreifen.

## Tenant-Isolation
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} ist eine Multi-Tenant-Objektspeicherlösung mit gemeinsam genutzter Infrastruktur. Wenn Ihre Workload dedizierten oder isolierten Speicher erfordert, besuchen Sie [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage), um weitere Informationen zu erhalten.
