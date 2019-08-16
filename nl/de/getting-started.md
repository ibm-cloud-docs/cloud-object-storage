---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Lernprogramm zur Einführung
{: #getting-started}

Dieses Lernprogramm zur Einführung führt Sie durch die Schritte, die erforderlich sind, um Buckets zu erstellen, Objekte hochzuladen und Zugriffsrichtlinien einzurichten, damit andere Benutzer mit Ihren Daten arbeiten können.
{: shortdesc}

## Vorbemerkungen
{: #gs-prereqs}

Sie benötigen Folgendes:
  * Ein Konto bei der [{{site.data.keyword.cloud}}-Plattform](https://cloud.ibm.com)
  * Eine [Instanz von {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * Einige Dateien auf Ihrem lokalen Computer zum Hochladen
{: #gs-prereqs}

 Dieses Lernprogramm hilft neuen Benutzern bei den ersten Schritten mit der Konsole der {{site.data.keyword.cloud_notm}}-Plattform. Entwickler, die eine Einführung in die API wünschen, finden entsprechende Informationen im [Entwicklerhandbuch](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) oder in der [API-Übersicht](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Buckets zum Speichern Ihrer Daten erstellen
{: #gs-create-buckets}

  1. Durch das [Bestellen von {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) wird eine _Serviceinstanz_ erstellt. {{site.data.keyword.cos_full_notm}} ist ein Multi-Tenant-System und alle Instanzen von {{site.data.keyword.cos_short}} nutzen die physische Infrastruktur gemeinsam. Sie werden automatisch an die Serviceinstanz weitergeleitet, in der Sie mit der Erstellung von Buckets beginnen können. Ihre {{site.data.keyword.cos_short}}-Instanzen werden in der [Ressourcenliste](https://cloud.ibm.com/resources) unter **Speicher** aufgeführt.

Die Begriffe 'Ressourceninstanz' und 'Serviceinstanz' beziehen sich auf dasselbe Konzept und können austauschbar verwendet werden.
{: tip}

  1. Folgen Sie **Bucket erstellen** und wählen Sie einen eindeutigen Namen aus. Alle Buckets in allen Regionen der Welt verwenden einen einzigen gemeinsamen Namensbereich. Stellen Sie sicher, dass Sie über die [korrekten Berechtigungen](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) zum Erstellen von Buckets verfügen.

  **Hinweis**: Achten Sie beim Erstellen von Bereichen oder Hinzufügen von Objekten darauf, dass Sie die Verwendung personenbezogener Daten (PII, Personally Identifiable Information) vermeiden. Personenbezogene Daten sind Angaben, die einen beliebigen Benutzer (natürliche Person) durch seinen Namen, seinen Standort oder anderweitig identifizieren können.{: tip}

  1. Wählen Sie zuerst die gewünschte [Ebene der _Ausfallsicherheit_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) aus und dann einen _Standort_, an dem Ihre Daten physisch gespeichert werden sollen. Ausfallsicherheit bezieht sich auf den Umfang und die Größe des geografischen Gebiets, über das Ihre Daten verteilt sind. Bei _regionsübergreifender_ Ausfallsicherheit werden Ihre Daten über mehrere Ballungsräume gestreut, während die Daten bei _regionaler_ Ausfallsicherheit auf einen einzigen Ballungsraum verteilt werden. Ein _einzelnes Rechenzentrum_ verteilt Daten auf Geräte innerhalb eines einzigen Standorts.
  2. Wählen Sie die [_Speicherklasse_ für das Bucket](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes) aus, die wiederspiegelt, wie häufig Sie die gespeicherten Daten voraussichtlich lesen werden und damit maßgeblich für die Abrechnungsdetails ist. Folgen Sie dem Link **Erstellen**, um ein neues Bucket zu erstellen und darauf zuzugreifen.

Buckets sind nur eine von mehreren Möglichkeiten, Ihre Daten zu organisieren. Objektnamen (oft auch als _Objektschlüssel_ bezeichnet) können einen oder mehrere normale Schrägstriche für ein verzeichnisähnliches Organisationssystem verwenden. Sie verwenden dann den Teil des Objektnamens vor einem Begrenzer, um ein _Objektpräfix_ zu bilden, das zum Auflisten zusammengehöriger Objekte in einem einzelnen Bucket über die API verwendet wird.
{: tip}


## Objekte zu Buckets hinzufügen
{: #gs-add-objects}

Wechseln Sie nun  zu einem Ihrer Buckets, indem Sie das entsprechende Bucket in der Liste auswählen. Klicken Sie auf **Objekte hinzufügen**. Neue Objekte überschreiben bereits vorhandene gleichnamige Objekte in demselben Bucket. Wenn Sie die Konsole zum Hochladen von Objekten verwenden, stimmt der Objektname stets mit dem Dateinamen überein. Wenn Sie die API zum Schreiben von Daten verwenden, muss keine Beziehung zwischen dem Dateinamen und dem Objektschlüssel bestehen. Fügen Sie zum Fortfahren eine Reihe von Dateien zu diesem Bucket hinzu.

Die Größe von Objekten ist beim Upload über die Konsole auf 200 MB beschränkt, sofern Sie nicht das Plug-in für [Aspera-Hochgeschwindigkeitsübertragung](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload) verwenden. Größere Objekte (von bis zu 10 TB) können auch [über die API in Teile zerlegt und parallel hochgeladen](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) werden. Objektschlüssel können eine Länge von bis zu 1024 Zeichen haben und sollten nach Möglichkeit keinerlei Zeichen enthalten, die bei einer Webadresse problematisch sein könnten. Zeichen wie `?`, `=`, `<` sowie andere Sonderzeichen könnten unter Umständen zu unerwünschtem Verhalten führen, sofern sie nicht URL-codiert sind.
{:tip}

## Benutzer zu Ihrem Konto einladen, um Ihre Buckets und Daten zu verwalten
{: #gs-invite-user}

Jetzt beziehen Sie einen weiteren Benutzer ein und ermöglichen diesem, für die Instanz und alle in ihr gespeicherten Daten als Administrator zu fungieren.

  1. Um den neuen Benutzer hinzufügen zu können, müssen Sie zuerst die aktuelle {{site.data.keyword.cos_short}}-Schnittstelle verlassen und zur IAM-Konsole wechseln. Öffnen Sie das Menü **Verwalten** und folgen Sie dem Link unter **Zugriff (IAM)** > **Benutzer**. Klicken Sie auf **Benutzer einladen**. 	<img alt="Benutzer einladen (IAM)" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`Abbildung 1: Benutzer einladen (IAM)`
  2. Geben Sie die E-Mail-Adresse eines Benutzers ein, den Sie zu Ihrer Organisation einladen möchten, erweitern Sie dann den Abschnitt **Services** und wählen Sie im Menü **Zugriff zuweisen für** das Element 'Ressource' aus. Wählen Sie dann im Menü **Services** den Eintrag 'Cloud Object Storage' aus. 	<img alt="Services (IAM)" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`Abbildung 2: Services (IAM)`
  3. Es werden jetzt drei weitere Felder angezeigt: _Serviceinstanz_, _Ressourcentyp_ und _Ressourcen-ID_. Das erste Feld definiert, auf welche Instanz von {{site.data.keyword.cos_short}} der Benutzer zugreifen kann. Mit diesem Feld kann auch festgelegt werden, dass allen Instanzen von {{site.data.keyword.cos_short}} dieselbe Zugriffsebene erteilt wird. Die anderen Felder können Sie vorerst leer lassen. 	<img alt="Benutzer einladen (IAM)" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`Abbildung 3: Benutzer einladen (IAM)`
  4. Mit dem Kontrollkästchen unter **Rollen auswählen** wird bestimmt, welche Gruppe von Aktionen dem Benutzer zur Verfügung stehen. Wählen Sie die Plattformzugriffsrolle 'Administrator' aus, damit der Benutzer seinerseits anderen [Benutzern und Servcice-IDs](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) Zugriff auf die Instanz erteilen kann. Wählen Sie die Servicezugriffsrolle 'Manager' aus, wenn der Benutzer die {{site.data.keyword.cos_short}}-Instanz verwalten können sowie Buckets und Objekte erstellen und löschen können soll. Diese Kombinationen aus _Subjekt_ (Benutzer), _Rolle_ (Manager) und _Ressource_ ({{site.data.keyword.cos_short}}-Serviceinstanz) bilden zusammen die [IAM-Richtlinien](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam). Ausführlichere Anleitungen für Rollen und Richtlinien finden Sie in der [Dokumentation zu IAM](/docs/iam?topic=iam-userroles).
	<img alt="Rollen (IAM)" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`Abbildung 4: Rollen auswählen (IAM)`
  5. {{site.data.keyword.cloud_notm}} verwendet Cloud Foundry als die zugrundeliegende Plattform für die Kontenverwaltung. Daher muss ein minimaler Zugriff auf Cloud Foundry gewährt werden, damit der Benutzer in der Lage ist, überhaupt auf Ihre Organisation zuzugreifen. Wählen Sie im Menü **Organisation** eine Organisation aus und wählen Sie dann sowohl im Menü **Organisationsrollen** als auch im Menü **Bereichsrollen** die Rolle 'Auditor' aus. Durch Festlegen von Cloud Foundry-Berechtigungen erhält der Benutzer die Möglichkeit, die für Ihre Organisation verfügbaren Services anzuzeigen, ohne diese jedoch ändern zu können.

## Entwicklern Zugriff auf ein Bucket erteilen
{: #gs-bucket-policy}

  1. Navigieren Sie zum Menü **Verwalten** und folgen Sie dem Link unter **Zugriff (IAM)** > **Service-ID**. Hier können Sie eine _Service-ID_ erstellen, die als abstrahierte, an das Konto gebundene Identität dient. Service-IDs können API-Schlüssel zugewiesen werden und sie werden in Situationen verwendet, in denen Sie keine bestimmte Entwickler-ID an einen Prozess oder eine Komponente einer Anwendung binden möchten. 	<img alt="Service-IDs (IAM)" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`Abbildung 5: Service-IDs (IAM)`
  2. Wiederholen Sie den oben beschriebenen Vorgang, wählen Sie jedoch in Schritt 3 eine bestimmte Serviceinstanz aus, geben Sie als _Ressourcentyp_ 'Bucket' an und geben Sie bei _Ressourcen-ID_ den vollständigen CRN eines vorhandenen Buckets ein.
  3. Jetzt kann die Service-ID auf dieses bestimmte Bucket zugreifen, nicht aber auf andere.

## Nächste Schritte
{: #gs-next-steps}

Nachdem Ihnen jetzt der Objektspeicher über die webbasierte Konsole vertraut ist, könnte es für Sie interessant sein, einen ähnlichen Workflow über die Befehlszeile mit dem Befehlszeilenprogramm `ibmcloud cos` durchzuführen, um die Serviceinstanz zu erstellen und mit IAM zu interagieren, und dann mit `curl` direkt auf COS zuzugreifen. Lesen Sie zum Einstieg die [API-Übersicht](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).
