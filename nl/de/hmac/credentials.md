---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# HMAC-Berechtigungsnachweise verwenden
{: #hmac}

Die {{site.data.keyword.cos_full}}-API ist eine REST-basierte API zum Lesen und Schreiben von Objekten. Sie verwendet {{site.data.keyword.iamlong}} für die Authentifizierung/Autorisierung und unterstützt zur einfachen Migration von Anwendungen auf {{site.data.keyword.cloud_notm}} eine Teilmenge der S3-API.

Zusätzlich zur IAM-Token-basierten Authentifizierung ist es auch möglich, sich mit einer [Signatur zu authentifizieren](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature), die aus einem Paar aus Zugriffsschlüssel und geheimem Schlüssel erstellt wurde. Dies ist in funktioneller Hinsicht identisch mit der AWS-Signatur Version 4 und die von IBM COS bereitgestellten HMAC-Schlüssel müssten mit den meisten S3-kompatiblen Bibliotheken und Tools funktionieren.

Benutzer können beim Erstellen eines [Serviceberechtigungsnachweises](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) eine Gruppe von HMAC-Berechtigungsnachweisen verwenden, indem sie bei der Erstellung von Berechtigungsnachweisen den Konfigurationsparameter `{"HMAC":true}` angeben. Das nachfolgende Beispiel veranschaulicht, wie Sie die Befehlszeilenschnittstelle (CLI) von {{site.data.keyword.cos_full}} verwenden, um einen Serviceschlüssel mit HMAC-Berechtigungsnachweisen mit der Rolle **Schreibberechtigter** zu erstellen. (Für Ihr Konto stehen möglicherweise andere Rollen zur Verfügung, die gegebenenfalls am besten für Ihre Bedürfnisse geeignet sind.) 

```
ibmcloud resource service-key-create <schlüsselname_ohne_leerzeiche> Writer --instance-name "<instanzname--anführungszeichen_bei_vorhandenen_leerzeichen_verwenden>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="Beispiel 1. cURL zum Erstellen von HMAC-Berechtigungsnachweisen verwenden. Beachten Sie die Verwendung einfacher wie auch doppelter hochgestellter Anführungszeichen." caption-side="bottom"}

Falls Sie die Ergebnisse des mit dem Befehl in Beispiel 1 soeben generierten Schlüssels speichern möchten, können Sie die ` > file.skey` an das Ende des Beispiels anhängen. Für die Zwecke dieses Instruktionssatzes müssen Sie lediglich die Überschrift `cos_hmac_keys` mit den untergeordneten Schlüsseln `access_key_id` und `secret_access_key` finden &mdash; zwei Felder, die Sie benötigen, wie Beispiel 2 zeigt.

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="Beispiel 2. Erwähnenswerte Schlüssel beim Generieren von HMAC-Berechtigungsnachweisen." caption-side="bottom"}

Von besonderem Interesse ist die Möglichkeit, Umgebungsvariablen zu festzulegen. (Die Anweisungen dafür sind für das betreffende Betriebssystem jeweils spezifisch.) In Beispiel 3 enthält ein Script `.bash_profile` die Schlüssel `COS_HMAC_ACCESS_KEY_ID` und `COS_HMAC_SECRET_ACCESS_KEY`, die beim Starten einer Shell exportiert und in der Entwicklung verwendet werden.

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="Beispiel 3. Verwendung von HMAC-Berechtigungsnachweisen als Umgebungsvariablen" caption-side="bottom"}

Nachdem der Berechtigungsnachweis erstellt worden ist, wird der HMAC-Schlüssel in das Feld `cos_hmac_keys` eingebunden. Diese HMAC-Schlüssel werden dann einer [Service-ID](/docs/iam?topic=iam-serviceids#serviceids) zugeordnet und können zum Zugreifen auf alle Ressourcen oder Operationen verwendet werden, auf die durch die Rolle der Service-ID der Zugriff zugelassen ist. 

Beachten Sie, dass bei der Verwendung von HMAC-Berechtigungsnachweisen für die Erstellung von Signaturen, die mit direkten Aufrufen der [REST-API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) verwendet werden sollen, zusätzliche Header erforderlich sind:
1. Alle Anforderungen müssen einen Header `x-amz-date` mit der Datumsangabe im Format `%Y%m%dT%H%M%SZ` besitzen.
2. Alle Anforderungen mit Nutzdaten (Uploads von Objekten, Löschen mehrerer Objekte usw.) müssen einen Header vom Typ `x-amz-content-sha256` mit einem SHA256-Hashwert des Nutzdateninhalts bereitstellen.
3. ACLs (mit Ausnahme von `public-read`) werden nicht unterstützt.

Es werden gegenwärtig nicht alle S3-kompatiblen Tools unterstützt. Manche Tools versuchen bei der Erstellung von Buckets andere ACLs als `public-read` festzulegen. Die Erstellung von Buckets mit diesen Tools schlägt fehl. Wenn eine Anforderung vom `PUT bucket` mit einem Fehler durch eine nicht unterstützte ACL fehlschlägt, versuchen Sie zuerst, das Bucket über die [Konsole](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) zu erstellen und konfigurieren Sie dann das Tool, um Objekte aus diesem Bucket zu lesen und in dieses Bucket zu schreiben. Tools, die ACLs für Schreibvorgänge für Objekte festlegen, werden derzeit nicht unterstützt.
{:tip}
