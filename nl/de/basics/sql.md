---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud services, integration, sql, query, analytics

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

# SQL Query verwenden
{: #sql-query}

IBM Cloud SQL Query ist ein vollständig verwalteter Service, mit dem Sie SQL-Abfragen (d. h. `SELECT`-Anweisungen) zum Analysieren, Transformieren oder Bereinigen von Rechteckdaten ausführen können.
{:shortdesc}

Sie können SQL Query nur verwenden, um `SELECT`-Anweisungen zu erstellen. Aktionen wie z. B. `CREATE`, `DELETE`, `INSERT` und `UPDATE` sind hingegen nicht möglich.
{:tip}

Die Eingabedaten werden aus ORC-, CSV-, JSON- oder Parquet-Dateien gelesen, die sich in einzelnen oder mehreren IBM Cloud Object Storage-Instanzen befinden. Jedes Abfrageergebnis wird in eine CSV-Datei in einer Cloud Object Storage-Instanz Ihrer Wahl geschrieben.

Sie können eine mit SQL abfragbare URL für Objekte für ein ausgewähltes einzelnes Objekt (Objekt-SQL-URL) oder für alle Objekte abrufen, die momentan mit einem aktiven Präfixfilter angezeigt werden (gefilterte SQL-URL). Diese URL können Sie in der SQL-Anweisung als Tabellenname verwenden.
{:tip}

Weitere Informationen zur Verwendung von SQL Query finden Sie in der [SQL Query-Dokumentation](/docs/services/sql-query?topic=sql-query-overview) und im Abschnitt zum [Analysieren von Daten mit IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).
