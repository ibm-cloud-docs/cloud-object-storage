---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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


# Informationen zu {{site.data.keyword.cos_full_notm}}
{: #about-ibm-cloud-object-storage}

Informationen, die mit {{site.data.keyword.cos_full}} gespeichert werden, werden verschlüsselt und über mehrere geografische Standorte verteilt. Der Zugriff auf diese Informationen erfolgt mithilfe von HTTP über eine REST-API. Dieser Service nutzt die verteilten Speichertechnologien, die von {{site.data.keyword.cos_full_notm}} System (früher Cleversafe) bereitgestellt werden.

{{site.data.keyword.cos_full_notm}} ist mit drei Typen von Ausfallsicherheit verfügbar: 'Regionsübergreifend', 'Regional' und 'Einzelnes Rechenzentrum'. Der Typ 'Regionsübergreifend' bietet im Vergleich zur Verwendung einer einzelnen Region eine höhere Permanenz und Verfügbarkeit, wodurch es allerdings zu leicht erhöhten Latenzzeiten kommt. Dieser Typ steht momentan in USA (US), Europa (EU) und im asiatisch-pazifischen Raum (AP) zur Verfügung. Beim Service 'Regional' sind diese Kompromisse umgekehrt. Objekte werden über mehrere Verfügbarkeitszonen in einer einzelnen Region verteilt. Dieser Service steht in den Regionen USA (US), Europa (EU) und im asiatisch-pazifischen Raum (AP) zur Verfügung. Wenn eine angegebene Region oder Verfügbarkeitszone nicht verfügbar ist, dann bleibt die Funktionsfähigkeit des Objektspeichers ohne Beeinträchtigungen erhalten. Beim Typ 'Einzelnes Rechenzentrum' werden Objekte über mehrere Systeme verteilt, die sich am selben physischen Standort befinden. Informationen zu den verfügbaren Regionen finden Sie [hier](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints).

Entwickler verwenden eine {{site.data.keyword.cos_full_notm}}-API, um mit ihrem Objektspeicher zu interagieren. In dieser Dokumentation finden Sie Unterstützung bei den [ersten Schritten](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started), die zur Bereitstellung von Konten, zum Erstellen von Buckets, zum Hochladen von Objekten und zur Verwendung einer Referenz für häufig verwendete API-Interaktionen ausgeführt werden müssen.

## Weitere IBM Object Storage-Services
{: #about-other-cos}
Zusätzlich zu {{site.data.keyword.cos_full_notm}} stellt {{site.data.keyword.cloud_notm}} momentan verschiedene zusätzliche Object Storage-Angebote für unterschiedliche Benutzeranforderungen zur Verfügung, auf die über webbasierte Portale und REST-APIs zugegriffen werden kann. [Weitere Informationen](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud).
