---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, basics

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

# Informationen zu Objektspeichern
{: #about-cos}

Objektspeicher sind ein modernes Speichertechnologiekonzept und eine logische Weiterentwicklung von Block- und Dateispeichern. Objektspeicher gibt es bereits seit Ende der 90er Jahre, haben aber in den letzten 10 Jahren zunehmend Marktakzeptanz gefunden und sich erfolgreich durchgesetzt.

Objektspeicher wurden erfunden, um eine Reihe von Problemen zu lösen:

*  Die Verwaltung von Daten in sehr großem Rahmen mit herkömmlichen Block- und Dateisystemen war schwierig, da diese Technologien aufgrund von Einschränkungen auf verschiedenen Ebenen der Datenverwaltungshardware und des Software-Stacks zu Dateninseln führen.

*  Die Verwaltung des Namensbereichs im richtigen Maß zog die zur Aufrechterhaltung und Pflege großer und komplexer Hierarchien nach sich, die für den Zugriff auf die Daten erforderlich sind. Einschränkungen bei verschachtelten Strukturen auf traditionellen Block- und Dateispeicher-Arrays trugen ebenfalls zur weiteren Bildung von Dateninseln bei.

*  Die Bereitstellung von Zugriffsschutz erforderte eine Kombination aus Technologien, komplexen Sicherheitssystemen und einer erheblichen Beteiligung von Menschen an der Verwaltung dieser Bereiche.

Objektspeicher, die auch als objektbasierte Speicher (OBS) bezeichnet werden, verwenden einen anderen Ansatz beim Speichern und Referenzieren von Daten. Objektdatenspeicherkonzepte umfassen die folgenden drei Konstrukte:

*  Daten: Dies sind die Benutzer- und Anwendungsdaten, für die ein permanenter Speicher erforderlich ist. Dabei kann es sich um Text, Binärformate, Multimediainhalte oder um andere von Menschen oder Maschinen generierte Inhalte handeln.

*  Metadaten: Dies sind die Daten über die Daten. Metadaten enthalten einige vordefinierte Attribute wie Zeitpunkt und Größe des Uploads. Objektspeicher ermöglichen Benutzern die Einbindung angepasster Metadaten, die beliebige Informationen in Schlüssel- und Wertepaaren enthalten. Diese Informationen enthalten typischerweise Informationen, die für den Benutzer oder die Anwendung, der bzw. die die Daten speichert, relevant sind und die jederzeit geändert werden können. Ein einzigartiger Aspekt in Hinsicht auf die Verarbeitung von Metadaten in Objektspeichersystemen besteht darin, dass Metadaten gemeinsam mit dem Objekt gespeichert werden.

*  Schlüssel: Jedem Objekt in einem OBS-System ist eine eindeutige Ressourcen-ID zugeordnet. Dieser Schlüssel ermöglicht es dem Objektspeichersystem, Objekte voneinander zu unterscheiden, und dient dazu, Daten zu suchen, ohne hierzu das physikalische Laufwerk, das Array oder die Position mit den Daten genau kennen zu müssen.

Durch diesen Ansatz können Objektspeicher Daten in einer einfachen, flachen Hierarchie speichern, was den Bedarf an umfangreichen, leistungshemmenden Repositorys für Metadaten verringert.

Der Datenzugriff erfolgt über eine REST-Schnittstelle über das HTTP-Protokoll, was jederzeit einen ortsunabhängigen Zugriff durch einfaches Referenzieren des Objektschlüssels ermöglicht.
