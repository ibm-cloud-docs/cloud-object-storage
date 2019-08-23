---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# Einführung in IAM
{: #iam}

## Rollen und Aktionen bei Identity and Access Management
{: #iam-roles}

Der Zugriff auf {{site.data.keyword.cos_full}}-Serviceinstanzen für Benutzer in Ihrem Konto wird über {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM) gesteuert. Jedem Benutzer, der auf den {{site.data.keyword.cos_full}}-Service in Ihrem Konto zugreift, muss eine Zugriffsrichtlinie mit einer definierten IAM-Benutzerrolle zugewiesen sein. Diese Richtlinie legt fest, welche Aktionen der Benutzer im Kontext des von Ihnen ausgewählten Service oder der von Ihnen ausgewählten Instanz ausführen kann. Die zulässigen Aktionen werden angepasst und vom {{site.data.keyword.Bluemix_notm}}-Service als Operationen definiert, deren Ausführung für den Service zugelassen ist. Die Aktionen werden dann IAM-Benutzerrollen zugeordnet.

Richtlinien ermöglichen die Gewährung des Zugriffs auf verschiedenen Ebenen. Zu den Optionen gehören unter anderem die folgenden: 

* Zugriff über alle Instanzen des Service in Ihrem Konto hinweg
* Zugriff auf eine einzelne Serviceinstanz in Ihrem Konto
* Zugriff auf eine bestimmte Ressource innerhalb einer Instanz
* Zugriff auf alle IAM-fähigen Services in Ihrem Konto

Nachdem Sie den Gültigkeitsbereich (Umfang) der Zugriffsrichtlinie definiert haben, weisen Sie eine Rolle zu. Prüfen Sie den Inhalt der folgenden Tabellen, in denen beschrieben wird, welche Aktionen jede Rolle innerhalb des {{site.data.keyword.cos_short}}-Service zulässt.

Die folgende Tabelle zeigt im Detail, welche Aktionen den Rollen für das Plattformmanagement zugeordnet sind. Plattformmanagement-Rollen ermöglichen Benutzern das Ausführen von Tasks für Serviceressourcen auf Plattformebene, wie zum Beispiel das Zuweisen von Benutzerzugriff für den Service, das Erstellen oder Löschen von Service-IDs, das Erstellen von Instanzen oder das Binden von Instanzen an Anwendungen.


| Plattformmanagement-Rolle | Beschreibung der Aktionen |Beispielaktionen|
|:-----------------|:-----------------|:-----------------|
| Anzeigeberechtigter | Serviceinstanzen anzeigen, nicht aber ändern | <ul><li>Verfügbare COS-Serviceinstanzen auflisten</li><li>Details des COS-Serviceplans anzeigen</li><li>Verwendungsdetails anzeigen</li></ul>|
| Bearbeiter | Alle Plattformaktionen ausführen, außer Konten verwalten und Zugriffsrichtlinien zuweisen |<ul><li>COS-Serviceinstanzen erstellen und löschen</li></ul> |
| Operator | Nicht von COS verwendet | Keine |
| Administrator | Alle Plattformaktionen auf der Grundlage der Ressource durchführen, der diese Rolle zugewiesen wird, einschließlich Zuweisen von Zugriffsrichtlinien zu anderen Benutzern |<ul><li>Benutzerrichtlinien aktualisieren</li>Preistarife aktualisieren</ul>|
{: caption="Tabelle 1. IAM-Benutzerrollen und -aktionen"}


Die folgende Tabelle zeigt im Detail, welche Aktionen den Rollen für den Servicezugriff zugeordnet sind. Servicezugriffsrollen ermöglichen Benutzern den Zugriff auf {{site.data.keyword.cos_short}} und versetzen sie außerdem in die Lage, die {{site.data.keyword.cos_short}}-API aufzurufen.

| Servicezugriffsrolle | Beschreibung der Aktionen | Beispielaktionen                                          |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| Leseberechtigter für Inhalt (Content Reader)      | Objekte herunterladen, aber keine Objekte oder Buckets auflisten | <ul><li>Objekte herunterladen                                   </li></ul> |
| Leseberechtigter | Zusätzlich zu den Aktionen für Leseberechtigte des Inhalts (Content Reader) können Leseberechtigte Buckets und/oder Objekte auflisten, nicht aber ändern. | <ul><li>Buckets auflisten</li><li>Objekte auflisten und herunterladen</li></ul>                    |
| Schreibberechtigter | Zusätzlich zu den Aktionen für Leseberechtigte können Schreibberechtigte Buckets erstellen und Objekte hochladen. | <ul><li>Neue Buckets und Objekte erstellen</li><li>Buckets und Objekte entfernen</li></ul> |
| Manager             | Zusätzlich zu den Aktionen für Schreibberechtigte können Manager Aktionen ausführen, für die eine Berechtigung erforderlich ist und die sich auf die Zugriffssteuerung auswirken. | <ul><li>Aufbewahrungsrichtlinie hinzufügen </li><li>Bucket-Firewall hinzufügen</li></ul>              |
{: caption="Tabelle 3. IAM-Servicebenutzerrollen und -aktionen"}


Informationen zum Zuweisen von Benutzerrollen in der Benutzerschnittstelle (UI) finden Sie in [Zugriff auf IAM verwalten](/docs/iam?topic=iam-iammanidaccser).
 
