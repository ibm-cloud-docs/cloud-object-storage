---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# Abrechnung
{: #billing}

Informationen zur Preisstruktur finden Sie auf der Seite für [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window}.

## Rechnungen
{: #billing-invoices}

Sie finden die Rechnungen für Ihr Konto im Navigationsmenü unter **Verwalten** > **Abrechnung und Nutzung**.

Jedes Konto erhält eine einzige Rechnung. Wenn Sie eine getrennte Abrechnung für unterschiedliche Gruppen von Containern wünschen, ist die Erstellung mehrerer Konten erforderlich.

## Preisstruktur bei {{site.data.keyword.cos_full_notm}}
{: #billing-pricing}

Die Speicherkosten für {{site.data.keyword.cos_full}} werden durch das Gesamtvolumen der gespeicherten Daten, die verwendete Bandbreite für öffentlichen ausgehenden Datenverkehr und die Gesamtzahl der vom System verarbeiteten Betriebsanforderungen bestimmt.

Infrastrukturangebote sind mit einem aus drei Tiers bestehenden Netz verbunden, das eine Segmentierung von öffentlichem Datenverkehr, privatem Datenverkehr und Managementdatenverkehr bewirkt. Infrastrukturservices können untereinander Daten über das private Netz kostenfrei übertragen. Infrastrukturangebote (wie Bare-Metal-Server, virtuelle Server und Cloudspeicher) stellen über das öffentliche Netz eine Verbindung zu anderen Anwendungen und Services im Katalog der {{site.data.keyword.cloud_notm}}-Plattform her (wie z. B. Watson-Services und Cloud Foundry-Laufzeiten), sodass die Datenübertragung zwischen diesen beiden Arten von Angeboten gemessen und zu Standardraten für Bandbreite im öffentlichen Netz abgerechnet werden.
{: tip}

## Anforderungsklassen
{: #billing-request-classes}

Anforderungen der 'Klasse A' beziehen eine Änderung oder Auflistung ein. Diese Kategorie umfasst das Erstellen von Buckets, das Hochladen oder Kopieren von Objekten, das Erstellen oder Ändern von Konfigurationen, das Auflisten von Buckets und das Auflisten des Inhalts von Buckets.

Anforderungen der 'Klasse B' beziehen sich auf das Abrufen von Objekten oder ihrer zugehörigen Metadaten oder Konfigurationen aus dem System.

Das Löschen von Buckets oder Objekten aus dem System ist nicht kostenpflichtig.

| Klasse | Anforderungen | Beispiele |
|--- |--- |--- |
| Klasse A | Anforderungen vom Typ PUT, COPY und POST sowie Anforderungen vom Typ GET, die zum Auflisten von Buckets und Objekten verwendet werden | Buckets erstellen, Objekte hochladen oder kopieren, Buckets auflisten, Inhalt von Buckets auflisten, ACSs festlegen, CORS-Konfigurationen festlegen |
| Klasse B | Anforderungen vom Typ GET (unter Ausschluss von Auflistungen), Anforderungen vom Typ HEAD und OPTIONS | Objekte und Metadaten abrufen |

## Aspera-Übertragungen
{: #billing-aspera}

Für [Aspera-Hochgeschwindigkeitsübertragung](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) werden zusätzliche Egress-Gebühren erhoben. Weitere Informationen finden Sie auf der [Seite für die Preisstruktur](https://www.ibm.com/cloud/object-storage#s3api).

## Speicherklassen
{: #billing-storage-classes}

Nicht auf alle gespeicherten Daten muss häufig zugegriffen werden und manche Archivdaten werden möglicherweise nur selten aufgerufen werden, wenn überhaupt. Für weniger aktive Arbeitslasten können Buckets in einer anderen Speicherklasse erstellt werden und für Objekte, die in diesen Buckets gespeichert sind, werden die Gebühren nach einem anderen Zeitplan als für den Standardspeicher berechnet.

Es gibt vier Klassen:

*  **Standard** wird für aktive Workloads verwendet, ohne dass für die abgerufenen Daten (mit Ausnahme der Kosten für die Betriebsanforderung selbst) Gebühren berechnet werden.
*  **Vault** wird für 'kalte' Workloads verwendet, bei denen weniger als einmal im Monat auf Daten zugegriffen wird, wobei bei jedem Lesen von Daten eine zusätzliche Gebühr für Abfragen ($/GB) berechnet wird. Der Service schließt einen Mindestschwellenwert für die Objektgröße und die Speicherdauer ein, der der beabsichtigten Nutzung dieses Service für 'kühlere', weniger aktive Daten entspricht.
*  **Cold Vault** wird für 'kalte' Workloads verwendet, bei denen alle 90 Tage oder seltener auf Daten zugegriffen wird, wobei bei jedem Lesen von Daten eine höhere zusätzliche Gebühr für Abfragen ($/GB) berechnet wird. Der Service schließt einen längeren Mindestschwellenwert für die Objektgröße und die Speicherdauer ein, der der beabsichtigten Nutzung dieses Service für 'kalte, inaktive Daten entspricht.
*  **Flex** wird für dynamische Workloads verwendet, bei denen die Zugriffsmuster schwieriger vorherzusagen sind. Wenn die nutzungsbedingten Kosten und Abrufgebühren einen Höchstwert überschreiten, entfallen die Abrufgebühren und stattdessen wird eine neue Kapazitätsgebühr berechnet. Wenn nicht häufig auf die Daten zugegriffen wird, ist diese Klasse kostengünstiger als der Standardspeicher, und wenn die Muster der Zugriffsverwendung unerwartet auf mehr Aktivität hinweisen, ist diese Speicherklasse kostengünstiger als 'Vault' oder 'Kalter Vault'. Bei Flex ist keine minimale Objektgröße oder Speicherdauer erforderlich.

Weitere Informationen zur Preisstruktur finden Sie in der [Preisstrukturtabelle unter ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Weitere Informationen zum Erstellen von Buckets mit unterschiedlichen Speicherklassen finden Sie in der [API-Referenz](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).
