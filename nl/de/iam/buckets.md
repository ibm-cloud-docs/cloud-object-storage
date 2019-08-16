---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# Bucketberechtigungen
{: #iam-bucket-permissions}

Weisen Sie Benutzern und Service-IDs Zugriffsrollen für Buckets zu und verwenden Sie zum Erstellen von Richtlinien entweder die Benutzerschnittstelle oder die Befehlszeilenschnittstelle (CLI).

| Zugriffsrolle | Beispielaktionen                                          |
|:------------|-------------------------------------------------------------|
| Manager     | Objekte öffentlich machen, Buckets und Objekte erstellen und zerstören |
| Schreibberechtigter | Buckets und Objekte erstellen und zerstören |
| Leseberechtigter | Objekte auflisten und herunterladen                                   |
| Leseberechtiger für Inhalt (Content Reader)      |Objekte herunterladen                                   |

## Einem Benutzer Zugriff erteilen
{: #iam-user-access}

Wenn der Benutzer die Konsole nutzen können soll, ist es notwendig, ihm neben der Servicezugriffsrolle (wie `Leseberechtigter`) **zusätzlich** für die Instanz selbst die Rolle `Anzeigeberechtigter` für ein Mindestmaß an Zugriff auf die Plattform zu erteilen. Auf diese Weise können Benutzer alle Buckets anzeigen und die in ihnen enthaltenen Objekte auflisten. Wählen Sie dann im Navigationsmenü links das Element **Bucketberechtigungen** aus, wählen Sie den Benutzer aus und wählen Sie anschließend die benötigte Zugriffsebene aus (`Manager` oder `Schreibberechtigter`).

Wenn der Benutzer über die API mit Daten interagiert und keinen Konsolenzugriff benötigt _und_ wenn er Mitglied Ihres Kontos ist, können Sie einem einzelnen Bucket Zugriff erteilen, ohne dass sich hierdurch Zugriff auf die übergeordnete Instanz ergibt.

## Richtliniendurchsetzung
{: #iam-policy-enforcement}

IAM-Richtlinien werden hierarchisch von der Ebene mit dem wenigsten restriktiven Zugriff bis zur Ebenen mit dem am stärksten eingeschränkten Zugriff durchgesetzt. Konflikte werden durch die nachgiebigere (permissivere) Richtlinie gelöst. Wenn einem Benutzer beispielsweise sowohl die Servicezugriffsrolle `Schreibberechtigter` als auch die Servicezugriffsrolle `Leseberechtigter` für ein Bucket zugewiesen ist, wird die Richtlinie, mit der die Rolle `Leseberechtigter` erteilt wird, ignoriert.

Dies trifft auch für Richtlinien auf Serviceinstanz- und Bucketebene zu.

- Wenn einem Benutzer durch eine Richtlinie die Rolle `Schreibberechtigter` für eine Serviceinstanz und die Rolle `Leseberechtigter` für ein einzelnes Bucket erteilt wird, so wird die Richtlinie auf Bucketebene ignoriert.
- Wenn einem Benutzer durch eine Richtlinie die Rolle `Leseberechtigter` für eine Serviceinstanz und die Rolle `Schreibberechtigter` für ein einzelnes Bucket erteilt wird, werden beide Richtlinien umgesetzt und die permissivere Rolle `Schreibberechtigter` erhält für das einzelne Bucket Vorrang.

Wenn es notwendig ist, den Zugriff auf ein einzelnes Bucket (oder eine Gruppe von Buckets) zu beschränken, stellen Sie über die Konsole oder die Befehlszeilenschnittstelle (CLI) sicher, dass für den Benutzer oder die Service-ID keine Richtlinien auf Instanzebene vorliegen.

### Benutzerschnittstelle (UI) verwenden
{: #iam-policy-enforcement-console}

So erstellen Sie eine neue Richtlinie auf Bucketebene: 

  1. Navigieren Sie vom Menü **Verwalten** zur Konsole **Zugriff IAM**.
  2. Wählen Sie im Navigationsmenü links die Option **Benutzer** aus.
  3. Wählen Sie einen Benutzer aus.
  4. Wählen Sie die Registerkarte **Zugriffsrichtlinien** aus, um die für den Benutzer vorhandenen Zugriffsrichtlinien anzuzeigen, eine neue Richtlinie zuzuweisen oder eine vorhandene Richtlinie zu bearbeiten.
  5. Klicken Sie auf **Zugriff zuweisen**, um eine neue Richtlinie zu erstellen.
  6. Wählen Sie die Option **Zugriff auf Ressourcen zuweisen** aus.
  7. Wählen Sie zuerst im Menü für Services die Option **Cloud Object Storage** aus.
  8. Wählen Sie dann die entsprechende Serviceinstanz aus. Geben Sie im Feld **Ressourcentyp** den Wert `Bucket` ein und geben Sie im Feld **Ressourcen-ID** den Bucketnamen ein.
  9. Wählen Sie die gewünschte Servicezugriffsrolle aus.
  10.  Klicken Sie auf **Zuweisen**

Beachten Sie, dass durch das Leerlassen der Felder **Ressourcentyp** bzw. **Ressource** eine Richtlinie auf Instanzebene erstellt wird.
{:tip}

### Befehlszeilenschnittstelle (CLI) verwenden
{: #iam-policy-enforcement-cli}

Führen Sie den folgenden Befehl von einem Terminal aus:

```bash
ibmcloud iam user-policy-create <benutzername> \
      --roles <rolle> \
      --service-name cloud-object-storage \
      --service-instance <ressourceninstanz-ID>
      --region global \
      --resource-type bucket \
      --resource <bucketname>
```
{:codeblock}

So listen Sie vorhandene Richtlinien auf:

```bash
ibmcloud iam user-policies <benutzername>
```
{:codeblock}

So bearbeiten Sie eine vorhandene Richtlinie:

```bash
ibmcloud iam user-policy-update <benutzername> <richtlinien-ID> \
      --roles <rolle> \
      --service-name cloud-object-storage \
      --service-instance <ressourceninstanz-ID>
      --region global \
      --resource-type bucket \
      --resource <bucketname>
```
{:codeblock}

## Zugriff auf eine Service-ID gewähren
{: #iam-service-id}
Wenn Sie einer Anwendung oder einer anderen nicht menschlichen Entität Zugriff auf ein Bucket gewähren müssen, verwenden Sie eine Service-ID. Dabei kann die Service-ID eigens zu diesem Zweck erstellt werden oder es kann eine bereits vorhandene Service-ID verwendet werden.

### Benutzerschnittstelle (UI) verwenden
{: #iam-service-id-console}

  1. Navigieren Sie vom Menü **Verwalten** zur Konsole **Zugriff (IAM)**.
  2. Wählen Sie im Navigationsmenü links die Option **Service-IDs** aus.
  3. Wählen Sie eine Service-ID aus, um alle eventuell vorhandenen Richtlinien anzuzeigen, und weisen Sie eine neue Richtlinie zu oder bearbeiten Sie eine bereits bestehende.
  3. Wählen Sie die Serviceinstanz, die Service-ID und die gewünschte Rolle aus.
  4. Geben Sie im Feld **Ressourcentyp** den Wert `Bucket` ein und geben Sie im Feld **Ressource** den Bucketnamen ein.
  5. Klicken Sie auf **Senden**.

  Beachten Sie, dass durch das Leerlassen der Felder **Ressourcentyp** bzw. **Ressource** eine Richtlinie auf Instanzebene erstellt wird.
  {:tip}

### Befehlszeilenschnittstelle (CLI) verwenden
{: #iam-service-id-cli}

Führen Sie den folgenden Befehl von einem Terminal aus:

```bash
ibmcloud iam service-policy-create <service-ID-name> \
      --roles <rolle> \
      --service-name cloud-object-storage \
      --service-instance <ressourceninstanz-ID>
      --region global \
      --resource-type bucket \
      --resource <bucketname>
```
{:codeblock}

So listen Sie vorhandene Richtlinien auf:

```bash
ibmcloud iam service-policies <service-ID-name>
```
{:codeblock}

So bearbeiten Sie eine vorhandene Richtlinie:

```bash
ibmcloud iam service-policy-update <service-ID-name> <richtlinien-ID> \
      --roles <rolle> \
      --service-name cloud-object-storage \
      --service-instance <ressourceninstanz-ID>
      --region global \
      --resource-type bucket \
      --resource <bucketname>
```
{:codeblock}
