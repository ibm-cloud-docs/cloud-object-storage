---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# Cloud Object Storage mit Cloud Foundry-Apps verwenden
{: #cloud-foundry}

{{site.data.keyword.cos_full}} kann mit {{site.data.keyword.cfee_full}}-Apps gekoppelt werden, um hoch verfügbaren Inhalt unter Verwendung von Regionen und Endpunkten bereitzustellen.

## Cloud Foundry Enterprise Environment
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} ist eine Plattform für das Hosting von Apps und Services in der Cloud. Sie können bei Bedarf mehrere isolierte, auf Unternehmen abgestimmte Plattformen instanziieren, die in Ihrem eigenen Konto ausgeführt werden können und entweder auf gemeinsam genutzter oder auf dedizierter Hardware bereitgestellt werden. Die Plattform erleichtert die Skalierung von Apps mit zunehmender Verarbeitung und vereinfacht die Laufzeit und die Infrastruktur, sodass Sie sich auf die Entwicklung konzentrieren können.

Die erfolgreiche Implementierung einer Cloud Foundry-Plattform erfordert die [angemessene Planung und Gestaltung](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation) für die notwendigen Ressourcen und Unternehmensanforderungen. Mehr zum Cloud Foundry Enterprise Environment erfahren Sie in der [Einführung](/docs/cloud-foundry?topic=cloud-foundry-about#creating) sowie im [Lernprogramm zur Einführung](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started).

### Regionen
{: #cloud-foundry-regions}
[Regionale Endpunkte](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) sind ein wichtiger Bestandteil der IBM Cloud-Umgebung. Sie können Anwendungen und Serviceinstanzen in verschiedenen Regionen mit derselben IBM Cloud-Infrastruktur für das Anwendungsmanagement und derselben Ansicht von Verwendungsdetails für die Abrechnung erstellen. Durch Auswahl einer IBM Cloud-Region, die sich geografisch in Ihrer Nähe oder in der Nähe Ihrer Kunden befindet, können Sie die Datenlatenz in Ihren Anwendungen senken und die Kosten minimieren. Die Auswahl von Regionen kann auch erfolgen, um Sicherheitsprobleme anzusprechen oder gesetzliche Bestimmungen zu berücksichtigen. 

Mit {{site.data.keyword.cos_full}} können Sie auswählen, ob Sie Daten über ein einzelnes Rechenzentrum, eine gesamte Region oder sogar eine Kombination von Regionen verteilen möchten, indem Sie den [Endpunkt auswählen](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints), an den Ihre Anwendung API-Anfragen sendet.

### Ressourcenverbindungen und Aliasse
{: #cloud-foundry-aliases}

Ein Alias ist eine Verbindung zwischen Ihrem verwalteten Service innerhalb einer Ressourcengruppe und einer Anwendung innerhalb einer Organisation oder eines Bereichs (Space). Aliase sind wie symbolische Links, die Verweise auf entfernte Ressourcen beinhalten. Sie ermöglichen die Interoperabilität und Wiederverwendung einer Instanz über die gesamte Plattform hinweg. In der {{site.data.keyword.cloud_notm}}-Konsole wird die Verbindung (Aliasname) als Serviceinstanz dargestellt. Sie können beispielsweise eine Instanz eines Service in einer Ressourcengruppe erstellen und sie anschließend aus allen verfügbaren Regionen wiederverwenden, indem Sie einen Aliasnamen in einer Organisation oder einem Bereich in diesen Regionen erstellen.

## Berechtigungsnachweise als VCAP-Variablen speichern 
{: #cloud-foundry-vcap}

{{site.data.keyword.cos_short}}-Berechtigungsnachweise können in der Umgebungsvariablen VCAP_SERVICES gespeichert werden, die zur Verwendung geparst werden kann, wenn auf den {{site.data.keyword.cos_short}}-Service zugegriffen wird. Die Berechtigungsnachweise umfassen Informationen, wie im folgenden Beispiel dargestellt:

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

Die Umgebungsvariable VCAP_SERVICES kann dann innerhalb Ihrer Anwendung geparst werden, um auf Ihren {{site.data.keyword.cos_short}}-Inhalt zuzugreifen. Das nachfolgende Beispiel veranschaulicht die Integration der Umgebungsvariablen mit dem COS-SDK über Node.js.

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// COS-SDK initialisieren
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

Weitere Informationen darüber, wie Sie das SDK für den Zugriff auf {{site.data.keyword.cos_short}} mit Codebeispielen verwenden, finden Sie hier:

* [Java verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [Python verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [Node.js verwenden](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## Servicebindungen erstellen 
{: #cloud-foundry-bindings}

### Dashboard
{: #cloud-foundry-bindings-console}

Die einfachste Möglichkeit, eine Servicebindung zu erstellen, involviert die Verwendung des [{{site.data.keyword.cloud}}-Dashboards](https://cloud.ibm.com/resources). 

1. Melden Sie sich am [Dashboard](https://cloud.ibm.com/resources) an.
2. Klicken Sie auf Ihre Cloud Foundry-Anwendung.
3. Klicken Sie im Menü zur linken Seite auf 'Verbindungen'.
4. Klicken Sie rechts auf **Verbindung erstellen**.
5. Bewegen Sie auf der Seite *Verbindung zu vorhandenem kompatiblen Service herstellen* den Mauszeiger über Ihren {{site.data.keyword.cos_short}}-Service und klicken Sie auf **Verbinden**.
6. Wählen Sie im Popup-Fenster *Verbindung für IAM-fähigen Service* die Zugriffsrolle aus, belassen Sie 'Automatisch für Service-ID generieren' unverändert und klicken Sie auf **Verbinden**.
7. Die Cloud Foundry-Anwendung erfordert das erneute Staging, damit sie die neue Servicebindung nutzt. Klicken Sie auf **Erneutes Staging**, um den Prozess anzustoßen.
8. Nach Abschluss des Restagingvorgangs steht Ihr Cloud Object Storage-Service für Ihre Anwendung zur Verfügung.

Die Umgebungsvariable VCAP_SERVICES der Anwendung wird automatisch mit den Serviceinformationen aktualisiert. So zeigen Sie die neue Variable an:

1. Klicken Sie im Menü zur rechten Seite auf *Laufzeit*.
2. Klicken Sie auf *Umgebungsvariablen*.
3. Prüfen Sie, ob Ihr COS-Service jetzt aufgelistet wird.

### IBM Client-Tools (CLI)
{: #cloud-foundry-bindings-cli}

1. Melden Sie sich über die IBM Cloud-Befehlszeilenschnittstelle (CLI) an:
```
 ibmcloud login --apikey <API-schlüssel>
```

2. Legen Sie Ihre Cloud Foundry-Umgebung als Ziel fest:
```
 ibmcloud target --cf
```

3. Erstellen Sie einen Servicealias für Ihre Instanz von {{site.data.keyword.cos_short}}:
```
ibmcloud resource service-alias-create <servicealias> --instance-name <COS-instanzname>
```

4. Erstellen Sie eine Servicebindung zwischen Ihrem {{site.data.keyword.cos_short}}-Alias und Ihrer Cloud Foundry-Anwendung und geben Sie eine Rolle für Ihre Bindung an. Die folgenden Rollen sind gültig:<br/><ul><li>Schreibberechtigter</li><li>Leseberechtigter</li><li>Manager</li><li>Administrator</li><li>Operator</li><li>Anzeigeberechtigter</li><li>Bearbeiter</li></ul>
```
ibmcloud resource service-binding-create <servicealias> <CF-app-name> <rolle>
```

### IBM Client-Tools (CLI) mit HMAC-Berechtigungsnachweisen
{: #cloud-foundry-hmac}

Hash-Based Message Authentication Code oder HMAC ist ein Mechanismus zum Berechnen eines erstellten Nachrichtenauthentifizierungscodes, der ein Paar aus Zugriffs- und geheimem Schlüssel verwendet. Diese Technik kann verwendet werden, um die Integrität und Authentizität einer Nachricht zu überprüfen. Weitere Informationen zur Verwendung von [HMAC-Berechtigungsnachweisen](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) finden Sie in der Dokumentation zu {{site.data.keyword.cos_short}}.

1. Melden Sie sich über die IBM Cloud-Befehlszeilenschnittstelle (CLI) an:
```
 ibmcloud login --apikey <API-schlüssel>
```

2. Legen Sie Ihre Cloud Foundry-Umgebung als Ziel fest:
```
 ibmcloud target --cf
```

3. Erstellen Sie einen Servicealias für Ihre Instanz von {{site.data.keyword.cos_short}}:
```
ibmcloud resource service-alias-create <servicealias> --instance-name <COS-instanzname>
```

4. Erstellen Sie eine Servicebindung zwischen Ihrem {{site.data.keyword.cos_short}}-Alias und Ihrer Cloud Foundry-Anwendung und geben Sie eine Rolle für Ihre Bindung an.<br/><br/>* **Hinweis:** Für die Erstellung von Serviceberechtigungsnachweisen mit aktiviertem HMAC ist ein zusätzlicher Parameter * (`{"HMAC":true}`) *notwendig.*<br/><br/>Die folgenden Rollen sind gültig:<br/><ul><li>Schreibberechtigter</li><li>Leseberechtigter</li><li>Manager</li><li>Administrator</li><li>Operator</li><li>Anzeigeberechtigter</li><li>Bearbeiter</li></ul>
```
ibmcloud resource service-binding-create <servicealias> <CF-app-name> <rolle> -p '{"HMAC":true}'
```

### Bindung an {{site.data.keyword.containershort_notm}} durchführen
{: #cloud-foundry-k8s}

Zum Erstellen einer Servicebindung zu {{site.data.keyword.containershort}} ist eine geringfügig anderes Verfahren erforderlich. 

*Für diesen Abschnitt muss außerdem [jq, ein schlanker JSON-Befehlszeilenprozessor](https://stedolan.github.io/jq/){:new_window}, installiert werden.*

Sie benötigen die folgenden Informationen und müssen die Schlüsselwerte in den Befehlen unten ersetzen:

* `<service alias>` - Neuer Aliasname für den COS-Service
* `<cos instance name>` - Name Ihrer vorhandenen COS-Instanz
* `<service credential name>` - Neuer Name für Ihren Serviceschlüssel/Berechtigungsnachweis
* `<role>` - Rolle, die an den Serviceschlüssel angehängt werden soll (gültige Rollen siehe oben; meist wird `Schreibberechtigter` angegeben)
* `<cluster name>` - Name Ihres vorhandenen Kubernetes-Cluster-Service
* `<secret binding name>` - Wert, der generiert wird, wenn COS an den Cluster-Service gebunden wird


1. Erstellen Sie einen Servicealias für Ihre COS-Instanz.<br/><br/>* **Hinweis:** Eine COS-Instanz kann nur einen Servicealias besitzen.*
```
ibmcloud resource service-alias-create <servicealias> --instance-name <COS-instanzname>
```
 
1. Erstellen Sie einen neuen Serviceschlüssel mit Berechtigungen für den COS-Servicealias.
```
ibmcloud resource service-key-create <serviceberechtigungsnachweisname> <rolle> --alias-name <servicealias> --parameters '{"HMAC":true}’
```

3. Binden Sie den Cluster an COS.
```
ibmcloud cs cluster-service-bind --cluster <clustername> --namespace default --service <servicealias>
```

4. Prüfen Sie, ob der COS-Servicealias an den Cluster gebunden ist.
```
ibmcloud cs cluster-services --cluster <clustername>
```
Die Ausgabe sieht ungefähr wie folgt aus:
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. Rufen Sie die Liste der geheimen Schlüssel in Ihrem Cluster ab und suchen Sie den geheimen Schlüssel für Ihren COS-Service. In der Regel handelt es sich dabei um `binding-` und den in Schritt 1 für `<servicealias>` angegebenen Wert (z. B. `binding-sv-cos`). Verwenden Sie diesen Wert für `<name_der_bindung_für_geheimen_schlüssel>` in Schritt 6.
```
kubectl get secrets
```
Die Ausgabe sieht ungefähr wie folgt aus:
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. Überprüfen Sie, ob die COS-HMAC-Berechtigungsnachweise in den geheimen Schlüsseln für Ihr Cluster verfügbar sind.
```
kubectl get secret <name_der_bindung_für_geheimen_schlüssel> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
Die Ausgabe sieht ungefähr wie folgt aus:
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
