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

# IAM-Übersicht
{: #iam-overview}

{{site.data.keyword.cloud}} Identity & Access Management ermöglicht Ihnen, Benutzer sicher zu authentifizieren und den Zugriff auf alle Cloud-Ressourcen konsistent in der {{site.data.keyword.cloud_notm}}-Plattform zu steuern. Weitere Informationen enthält der Abschnitt [Lernprogramm zur Einführung](/docs/iam?topic=iam-getstarted#getstarted).

## Identitätsmanagement
{: #iam-overview-identity}

Das Identitätsmanagement umfasst die Interaktion von Benutzern, Services und Ressourcen. Benutzer werden anhand ihrer IBMid angegeben. Services werden mit ihrer Service-ID angegeben. Ressourcen schließlich werden unter Verwendung von Cloudressourcennamen bzw. CRNs angegeben und adressiert.

Der IAM-Token-Service von {{site.data.keyword.cloud_notm}} ermöglicht Ihnen, API-Schlüssel für Benutzer und Services zu erstellen, aktualisieren, löschen und verwenden. Diese API-Schlüssel können entweder mit API-Aufrufen oder im Abschnitt 'Identität und Zugriff' der Konsole der {{site.data.keyword.cloud}}-Plattform erstellt werden. Der gleiche Schlüssel kann für mehrere Services verwendet werden. Zur Unterstützung von Schlüsselrotationsszenarios sowie von Szenarios, bei denen unterschiedliche Schlüssel für unterschiedliche Zwecke verwendet werden, um das Sicherheitsrisiko durch die Verwendung eines einzigen Schlüssels einzugrenzen, kann jeder Benutzer über mehrere API-Schlüssel verfügen.

Weitere Informationen enthält [Was ist Cloud IAM?](/docs/iam?topic=iam-iamoverview#iamoverview)

### Benutzer und API-Schlüssel
{: #iam-overview-user-api-keys}

API-Schlüssel können von {{site.data.keyword.cloud_notm}}-Benutzern zur Automatisierung und die Scripterstellung sowie für die föderierte Anmeldung bei Verwendung der Befehlszeilenschnittstelle erstellt und verwendet werden. API-Schlüssel können in der Benutzerschnittstelle von Identity and Access Management (IAM) oder über die Befehlszeilenschnittstelle (CLI) `ibmcloud` erstellt werden.

### Service-IDs und API-Schlüssel
{: #iam-overview-service-id-api-key}

Der IAM-Token-Service ermöglicht die Erstellung von Service-IDs und API-Schlüsseln für Service-IDs. Eine Service-ID ist vergleichbar mit einer 'Funktions-ID' oder einer 'Anwendungs-ID' und dient der Authentifizierung von Services, nicht aber der Darstellung eines Benutzers.

Benutzer können Service-IDs erstellen und sie an Gültigkeitsbereiche wie etwa ein Konto der {{site.data.keyword.cloud_notm}}-Plattform, eine CloudFoundry-Organisation oder einen CloudFoundry-Bereich (Space) binden, doch bei der Übernahme von IAM ist es am besten, Service-IDs an ein Konto der {{site.data.keyword.cloud_notm}}-Plattform zu binden. Diese Bindung erfolgt, damit die Service-ID in einem Container Bestand haben kann. Dieser Container definiert auch, wer die Service-ID aktualisieren und löschen darf und wer API-Schlüssel, die dieser Service-ID zugeordnet sind, erstellen, aktualisieren, lesen und löschen kann. Beachten Sie unbedingt, dass eine Service-ID NICHT an einen Benutzer gekoppelt ist.

### Schlüsselrotation
{: #iam-overview-key-rotation}

API-Schlüssel sollten in regelmäßigen Abständen im Rotationsverfahren wechselt werden, um eventuellen Sicherheitsverletzungen durch offengelegte Schlüssel vorzubeugen.

## Zugriffsmanagement
{: #iam-overview-access-management}

Die IAM-Zugriffssteuerung bietet eine gängige Möglichkeit, Benutzerrollen für {{site.data.keyword.cloud_notm}}-Ressourcen zuzuordnen und steuert, welche Aktionen die Benutzer für diese Ressourcen durchführen können. Je nachdem, welche Zugriffsoptionen Ihnen erteilt wurden, können Sie Benutzer im ganzen Konto oder in der gesamten Organisation anzeigen und verwalten. So wird zum Beispiel Kontoeignern für Identity and Access Managemement automatisch die Rolle des Kontoadministrators zugewiesen, was es ihnen ermöglicht, Servicerichtlinien für alle Mitglieder ihres Kontos zuzuweisen und zu verwalten.

### Benutzer, Rollen, Ressourcen und Richtlinien
{: #iam-overview-access-policies}

Die IAM-Zugriffssteuerng lässt die Zuweisung von Richtlinien nach Service oder Serviceinstanz zu und ermöglicht so verschiedene Ebenen von Zugriff für die Verwaltung von Ressourcen und Benutzern innerhalb des zugewiesenen Kontexts. Eine Richtlinie erteilt einem Benutzer eine oder mehrere Rollen für eine Gruppe von Ressourcen, indem sie die maßgebliche Gruppe von Resssourcen mittels einer Kombination von Attributen definiert. Wenn Sie einem Benutzer eine Richtlinie zuweisen, geben Sie zuerst den Service an und dann eine oder oder mehrere Rollen, die zugewiesen werden sollen. Je nach dem ausgewähltem Service stehen möglicherweise zusätzliche Konfigurationsoptionen zur Verfügung.

Während es sich bei Rollen um eine Sammlung von Aktionen handelt, sind die Aktionen, die diesen Rollen zugeordnet sind, servicespezifisch. Jeder Service bestimmt diese Zuordnung von Rollen und Aktionen im Rahmen des Onboarding-Prozesses und diese Zuordnung gilt für alle Benutzer des Service. Rollen und Zugriffsrichtlinien werden über den Richtlinienverwaltungspunkt (PAP, Policy Administration Point) konfiguriert und über den Richtliniendurchsetzungspunkt (PEP, Policy Enforcement Point) sowie den Richtlinienentscheidungspunkt (PDP, Policy Decision Point) umgesetzt.

Weitere Informationen finden Sie in [Bewährte Verfahren zum Organisieren von Benutzern, Teams und Anwendungen](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications).
