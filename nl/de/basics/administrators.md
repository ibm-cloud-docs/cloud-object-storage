---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: administrator, storage, iam, access

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

# Für Administratoren
{: #administrators}

Speicher- und Systemadministratoren, die Objektspeicher konfigurieren und den Zugriff auf Daten verwalten müssen, können IAM (IBM Cloud Identity and Access Management) nutzen, um Benutzer zu verwalten, API-Schlüssel zu erstellen und turnusmäßig zu wechseln und um Benutzern und Services bestimmte Rollen zuzuweisen. Wenn Sie diesen Schritt noch nicht ausgeführt haben, dann sollten Sie nun das [Lernprogramm 'Einführung'](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) durcharbeiten, um sich mit den zentralen Konzepten wie z. B. 'Buckets', 'Objekte' und 'Benutzer' vertraut zu machen.

## Speicher einrichten
{: #administrators-setup}

Als erstes müssen Sie mindestens eine Objektspeicherressourceninstanz und einige Buckets zur Speicherung der Daten anlegen. Berücksichtigen Sie bei der Erstellung der Buckets, wie Sie den Zugriff auf Ihre Daten weiter segmentieren wollen, wo Ihre Daten physisch gespeichert werden sollen und wie oft auf die Daten zugegriffen wird.

### Zugriff segmentieren
{: #administrators-access}

Der Zugriff kann auf zwei Ebenen segmentiert werden: Auf Ebene der Ressourceninstanz und auf Bucketebene. 

Dies kann nützlich sein, wenn Sie z. B. sicherstellen wollen, dass ein Entwicklungsteam nur auf die Instanzen des Objektspeichers zugreifen kann, mit denen dieses Team arbeitet, nicht jedoch auf die Instanzen eines anderen Teams. Möglicherweise möchten Sie auch sicherstellen, dass nur die von Ihrem Team erstellte Software zur Bearbeitung der gespeicherten Daten benutzt werden kann. Zu diesem Zweck können Sie festlegen, dass Ihre Entwickler mit Zugriff auf die Cloudplattform die Daten nur zur Fehlerbehebung lesen können. Dies sind Beispiele für Richtlinien auf Serviceebene.

Wenn Entwicklungsteams oder einzelne Benutzer, die über Anzeigeberechtigtung auf eine Speicherinstanz verfügen, Daten jedoch in mindestens einem Bucket direkt bearbeiten können sollen, dann können Sie Richtlinien auf Bucketebene verwenden, um die Berechtigungsebene, die den Benutzern in Ihrem Konto erteilt wird, hochzustufen. Beispiel: Ein Benutzer kann keine neuen Buckets erstellen, jedoch Objekte in vorhandenen Buckets erstellen und löschen.

## Zugriff verwalten
{: #administrators-manage-access}

IAM basiert auf einem grundlegenden Konzept: Einem _Subjekt_ wird eine _Rolle_ für eine _Ressource_ zugeteilt.

Es gibt zwei grundlegende Subjekttypen: _Benutzer_ und _Service-IDs_.

Ein weiteres Konzept sind die _Serviceberechtigungsnachweise_. Ein Serviceberechtigungsnachweis stellt eine Zusammenstellung wichtiger Informationen dar, die zur Herstellung einer Verbindung zu einer Instanz von {{site.data.keyword.cos_full}} benötigt werden. Diese Informationen umfassen mindestens eine Kennung für die Instanz von {{site.data.keyword.cos_full_notm}} (z. B. die Ressourceninstanz-ID), Service- und Authentifizierungsendpunkte und eine Möglichkeit, um dem Subjekt einen API-Schlüssel (z. B. eine Service-ID) zuzuordnen. Wenn Sie den Serviceberechtigungsnachweis erstellen, dann haben Sie die Möglichkeit, ihn einer vorhandenen Service-ID zuzuordnen, oder eine neue Service-ID zu erstellen.

Wenn also Ihr Entwicklungsteam die Konsole zum Anzeigen der Objektspeicherinstanzen und Kubernetes-Cluster verwenden können soll, dann benötigen die Mitglieder Ihres Teams die Rolle `Anzeigeberechtigter` für die Objektspeicherressourcen und die Rolle `Administrator` für den Container-Service. Beachten Sie hierbei, dass die Rolle `Anzeigeberechtigter` dem Benutzer lediglich die Überprüfung des Vorhandenseins der Instanz und das Anzeigen vorhandener Berechtigungsnachweise ermöglicht. Die Buckets und Objekte können hingegen mit dieser Rolle **nicht** angezeigt werden. Wenn die Serviceberechtigungsnachweise erstellt werden, dann wird ihnen eine Service-ID zugeordnet. Diese Service-ID muss über die Rolle `Manager` oder `Schreibberechtigter` für die Instanz verfügen, um Buckets und Objekte erstellen und löschen zu können.

Weitere Informationen zu den IAM-Rollen und -Berechtigungen finden Sie im Abschnitt mit der [IAM-Übersicht](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview).
