---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# Für Entwickler
{: #gs-dev}
Stellen Sie zunächst sicher, dass die [{{site.data.keyword.cloud}} Platform-CLI](https://cloud.ibm.com/docs/cli/index.html) und die [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) installiert sind.

## Instanz von {{site.data.keyword.cos_full_notm}} bereitstellen
{: #gs-dev-provision}

  1. Stellen Sie zunächst sicher, dass Sie über einen API-Schlüssel verfügen. Rufen Sie diesen von [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys) ab.
  2. Melden Sie sich bei {{site.data.keyword.cloud_notm}} Platform über die Befehlszeilenschnittstelle an. Es ist auch möglich, den API-Schlüssel in einer Datei zu speichern oder als Umgebungsvariable zu definieren.

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. Als Nächstes müssen Sie eine Instanz von {{site.data.keyword.cos_full_notm}} angeben, in der der Name der Instanz, die ID und der gewünschte Plan (Lite oder Standard) angegeben werden. Daraufhin wird der CRN ausgegeben. Wenn Sie über ein Konto verfügen, für das ein Upgrade durchgeführt wurde, dann geben Sie den `Standard`-Plan an. Geben Sie andernfalls `Lite` an.

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

Im [Handbuch zur Einführung](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) werden Sie durch die grundlegenden Schritte zum Erstellen von Buckets und Objekten geführt. Außerdem erfahren Sie dort, wie Benutzer eingeladen und Richtlinien erstellt werden können. Eine Liste der grundlegenden 'curl'-Befehle finden Sie [hier](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl).

Weiterführende Informationen zur Verwendung der {{site.data.keyword.cloud_notm}}-Befehlszeilenschnittstelle für die Erstellung von Anwendungen, die Verwaltung von Kubernetes-Clustern und für weitere Operationen finden Sie in der [Dokumentation](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli).


## API verwenden
{: #gs-dev-api}

Zur Verwaltung von Daten, die in {{site.data.keyword.cos_short}} gespeichert werden, können Sie Tools verwenden, die mit der S3-API kompatibel sind. Hierzu gehört beispielsweise die [AWS-CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli) mit [HMAC-Berechtigungsnachweisen](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) für die Kompatibilität. Da die Verwendung von IAM-Tokens relativ benutzerfreundlich ist, stellt `curl` eine gute Wahl für grundlegende Tests und die Interaktion mit Ihrem Speicher dar. Weitere Informationen hierzu finden Sie in der [`curl`-Referenz](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) und auch in der [API-Referenzdokumentation](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Bibliotheken und SDKs verwenden
{: #gs-dev-sdk}
IBM COS-SDKs stehen für [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) und  [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) zur Verfügung. Hierbei handelt es sich um abgeleitete Versionen der AWS S3-SDKs, die geändert wurden, um die [IAM-Token-basierte Authentifizierung](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) sowie [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption) zu unterstützen. 

## Anwendungen in IBM Cloud erstellen
{: #gs-dev-apps}
{{site.data.keyword.cloud}} bietet Entwicklern Flexibilität bei der Auswahl der richtigen Architektur- und Bereitstellungsoptionen für eine bestimmte Anwendung. Führen Sie Ihren Code auf [Bare-Metal-Systemen](https://cloud.ibm.com/catalog/infrastructure/bare-metal), in [virtuellen Maschinen](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), unter Verwendung eines [serverunabhängigen Frameworks](https://cloud.ibm.com/openwhisk), in [Containern](https://cloud.ibm.com/kubernetes/catalog/cluster) oder unter Verwendung von [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs) aus. 

Die [Cloud Native Computing Foundation](https://www.cncf.io) hat das [Kubernetes](https://kubernetes.io) Container Orchestration Framework entwickelt und abgeschlossen, das die Basis für den {{site.data.keyword.cloud}} Kubernetes Service bildet. Entwickler, die Objektspeicher für die persistente Speicherung in ihren Kubernetes-Anwendungen verwenden möchten, können über die folgenden Links weiterführende Informationen abrufen:

 * [Speicherlösung auswählen](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [Vergleichstabelle für Optionen zur persistenten Speicherung](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [COS-Hauptseite](/docs/containers?topic=containers-object_storage)
 * [COS installieren](/docs/containers?topic=containers-object_storage#install_cos)
 * [COS-Serviceinstanz erstellen](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [Geheimen COS-Schlüssel erstellen](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [Entscheidung zur Konfiguration treffen](/docs/containers?topic=containers-object_storage#configure_cos)
 * [COS bereitstellen](/docs/containers?topic=containers-object_storage#add_cos)
 * [Informationen zum Sichern und Wiederherstellen](/docs/containers?topic=containers-object_storage#backup_restore)
 * [Speicherklassenreferenz](/docs/containers?topic=containers-object_storage#storageclass_reference)


