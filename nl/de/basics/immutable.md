---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: worm, immutable, policy, retention, compliance

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

# Immutable Object Storage verwenden
{: #immutable}

Immutable Object Storage (IOS) ermöglicht Kunden die Aufbewahrung elektronischer Aufzeichnungen und die Aufrechterhaltung der Datenintegrität nach dem WORM-Prinzip (WORM = Write-Once-Read-Many). Die Daten sind bis zum Ende Ihrer Aufbewahrungsdauer und der Entfernung ihres Beweissicherungsvermerks nicht löschbar und nicht mehrfach beschreibbar. Diese Funktion kann von allen Kunden verwendet werden, in deren Umgebung Bedarf für die Langzeitaufbewahrung von Daten besteht. Dies gilt u. a. für Organisationen in den folgenden Branchen:

 * Finanzwesen
 * Gesundheitswesen
 * Medieninhaltsarchive
 * Branchen, in denen die Verhinderung privilegierter Änderungen oder der Löschung von Objekten oder Dokumenten von Bedeutung ist

Die zugrunde liegenden Funktionen können auch von Organisationen eingesetzt werden, die für die Verwaltung von Finanzunterlagen verantwortlich sind (z. B. Broker-Dealer-Transaktionen) und die Bedarf für die Aufbewahrung der Objekte in nicht mehrfach beschreibbarer und nicht löschbarer Form haben.

Immutable Object Storage steht nur in bestimmten Regionen zur Verfügung. Detaillierte Informationen hierzu finden Sie im Abschnitt [Integrierte Services](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability). Des Weiteren ist zur Nutzung von IOS ein Standardpreisstrukturplan erforderlich. Detaillierte Informationen hierzu finden Sie im Abschnitt zur [Preisstruktur](https://www.ibm.com/cloud/object-storage).
{:note}

Für Buckets, für die eine Aufbewahrungsrichtlinie definiert wurde, ist die Nutzung der Aspera-Hochgeschwindigkeitsübertragung nicht möglich.
{:important}

## Terminologie und Verwendung
{: #immutable-terminology}

### Aufbewahrungszeitraum
{: #immutable-terminology-period}

Die Zeitdauer, die ein Objekt im COS-Bucket gespeichert bleiben muss.

### Aufbewahrungsrichtlinie
{: #immutable-terminology-policy}

Eine Aufbewahrungsrichtlinie wird auf COS-Bucketebene aktiviert. In dieser Richtlinie werden die mindestens erforderliche, maximal zulässige und standardmäßige Aufbewahrungszeitdauer definiert, die für alle Objekte im Bucket gelten.

Die mindestens erforderliche Aufbewahrungsdauer stellt den Zeitraum dar, für den ein Objekt mindestens im Bucket aufbewahrt werden muss.

Die maximal zulässige Aufbewahrungsdauer stellt den Zeitraum dar, für den ein Objekt maximal im Bucket aufbewahrt werden kann.

Wird ein Objekt im Bucket ohne Angabe einer angepassten Aufbewahrungsdauer gespeichert, dann verwendet das System den Standardaufbewahrungszeitraum. Die mindestens erforderliche Aufbewahrungsdauer muss kleiner oder gleich dem Standardaufbewahrungszeitraum sein, der seinerseits kleiner oder gleich der maximal zulässigen Aufbewahrungsdauer sein muss.

Hinweis: Für Objekte kann eine maximal zulässige Aufbewahrungsdauer von 1000 Jahren angegeben werden.

Hinweis: Zum Erstellen einer Aufbewahrungsrichtlinie für ein Bucket benötigen Sie die Rolle 'Manager'. Weitere Einzelheiten hierzu finden Sie im Abschnitt zu den [Bucketberechtigungen](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions).

### Beweissicherung 
{: #immutable-terminology-hold}

Bestimmte Datensätze (Objekte) müssen auch nach Ablauf der Aufbewahrungsdauer gegen eine Löschung gesichert werden. Dies ist z. B. für rechtliche Überprüfungen der Fall, die noch nicht abgeschlossen sind und die den Zugriff auf Datensätze über einen verlängerten Zeitraum erforderlich machen, der die ursprünglich für das Objekt festgelegte Aufbewahrungsdauer übersteigt. In einem derartigen Szenario kann das Flag 'Beweissicherung' auf Objektebene angewendet werden.
 
Solche Beweissicherungsvermerke können während des ersten Uploads in das COS-Bucket oder nach dem Hinzufügen eines Objekts auf Objekte angewendet werden.
 
Hinweis: Pro Objekt können maximal 100 Beweissicherungsvermerke angewendet werden.

### Unbegrenzte Aufbewahrung
{: #immutable-terminology-indefinite}

Mit dieser Option kann der Benutzer festlegen, dass das Objekt für unbegrenzte Zeit aufbewahrt werden soll, es sei denn, es wird zu einem späteren Zeitpunkt eine neue Aufbewahrungsdauer definiert. Diese Option wird auf Objektebene festgelegt.

### Ereignisgesteuerte Aufbewahrung
{: #immutable-terminology-events}

Immutable Object Storage (IOS) ermöglicht Benutzern das Festlegen einer unbegrenzten Aufbewahrungsdauer für Objekte, wenn die endgültige Zeitdauer der Aufbewahrung für den betreffenden Anwendungsfall noch nicht feststeht, oder wenn die ereignisgesteuerte Aufbewahrung verwendet werden soll. Nachdem eine unbegrenzte Aufbewahrungsdauer festgelegt wurde, kann in Benutzeranwendungen die Aufbewahrungsdauer eines Objekts zu einem späteren Zeitpunkt geändert und es eine begrenzte Aufbewahrungsdauer angegeben werden. Beispiel: Ein Unternehmen verfügt über eine Richtlinie zur Aufbewahrung von Mitarbeiterdaten, in der eine Aufbewahrungsdauer von drei Jahren nach Ausscheiden des Mitarbeiters aus dem Unternehmen vorgegeben ist. Wenn ein Mitarbeiter ins Unternehmen eintritt, dann wird angegeben, dass die Daten des Mitarbeiters unbeschränkt aufbewahrt werden sollen. Verlässt der betreffende Mitarbeiter das Unternehmen, dann wird die unbeschränkte Aufbewahrungsdauer in eine beschränkte Aufbewahrungsdauer von drei Jahren ab dem aktuellen Zeitpunkt umgewandelt, was den Angaben in der Unternehmensrichtlinie entspricht. Das Objekt wird dann nach Änderung der Aufbewahrungsdauer für drei Jahre geschützt. Eine Benutzer- oder Drittanbieteranwendung kann die Aufbewahrungsdauer mit einem SDK oder einer REST-API von unbegrenzt in begrenzt ändern.

### Dauerhafte Aufbewahrung
{: #immutable-terminology-permanent}

Die dauerhafte Aufbewahrung kann nur auf Ebene des COS-Buckets mit aktivierter Aufbewahrungsrichtlinie aktiviert werden. Die Benutzer können die Option für die dauerhafte Aufbewahrung während des Objektuploads auswählen. Nach seiner Aktivierung kann dieser Prozess nicht umgekehrt werden und Objekte, die unter Verwendung einer dauerhaften Aufbewahrung hochgeladen wurden, **können nicht gelöscht** werden. Es liegt im Verantwortungsbereich der Benutzer, ihrerseits zu prüfen, ob eine rechtliche Notwendigkeit zur **dauerhaften** Speicherung von Objekten in COS-Buckets mit einer Aufbewahrungsrichtlinie besteht.


Bei Verwendung von Immutable Object Storage sind Sie für die Einhaltung der IBM Cloud-Richtlinien und -Leitlinien für Ihr IBM Cloud-Konto verantwortlich, solange Ihre Daten einer Aufbewahrungsrichtlinie unterliegen. Weitere Informationen hierzu finden Sie im Abschnitt zu den IBM Cloud Service-Bedingungen.
{:important}

## Immutable Object Storage und Hinweise zu verschiedenen Regelungen
{: #immutable-regulation}

Bei Verwendung von Immutable Object Storage liegt es im Verantwortungsbereich des Kunden, zu prüfen und sicherzustellen, dass bei der Nutzung der hier behandelten Funktionalität die wichtigsten Regelungen in Bezug auf die Speicherung und Aufbewahrung elektronischer Aufzeichnungen eingehalten werden. Hierbei sind insbesondere folgende Regelungen zu beachten:

  * [Securities and Exchange Commission (SEC) - Regelung 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64),
  * [Financial Industry Regulatory Authority (FINRA) - Regelung 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957), and
  * [Commodity Futures Trading Commission (CFTC) - Regelung 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

Um Kunden bei der Entscheidungsfindung zu unterstützen, hat IBM Cohasset Associates Inc. beauftragt, eine unabhängige Bewertung der Funktionalität von IBM Immutable Object Storage durchzuführen. Lesen Sie den [Bericht](https://www.ibm.com/downloads/cas/JBDNP0KV) von Cohasset Associates Inc., in dem Sie detaillierte Informationen zur Bewertung der Funktionalität von Immutable Object Storage von IBM Cloud Object Storage finden. 

### Zugriff und Transaktionen protokollieren
{: #immutable-audit}
Die Immutable Object Storage-Protokolldaten können verwendet werden, um Änderungen der Aufbewahrungsparameter, der Aufbewahrungsdauer für Objekte sowie die Anwendung von Beweissicherungsvermerken auf Fallbasis zu überprüfen. Hierzu kann ein Kundenservice-Ticket geöffnet werden.

## Konsole verwenden
{: #immutable-console}

Aufbewahrungsrichtlinien können zu neuen oder bereits vorhandenen, leeren Buckets hinzugefügt werden. Nach dem Hinzufügen können sie nicht wieder entfernt werden. Für ein neues Bucket müssen Sie sicherstellen, dass das Bucket in einer [unterstützten Region](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability) erstellt wird. Anschließend müssen Sie die Option **Aufbewahrungsrichtlinie hinzufügen** auswählen. Für ein bereits vorhandenes Bucket müssen Sie sich vergewissern, dass es keine Objekte enthält, und dann zu den Konfigurationseinstellungen navigieren und auf die Schaltfläche **Richtlinie erstellen** unterhalb des Abschnitts für die Aufbewahrungsrichtlinie des Buckets klicken. In beiden Fällen müssen Sie Werte für die mindestens erforderliche, maximal zulässige und standardmäßige Aufbewahrungsdauer festlegen.

## REST-API, Bibliotheken und SDKs verwenden
{: #immutable-sdk}

Für die IBM COS-SDKs wurden mehrere neue APIs eingeführt, um Unterstützung für Anwendungen zu bieten, die mit Aufbewahrungsrichtlinien arbeiten. Wählen Sie oben auf dieser Seite eine Sprache (HTTP, Java, Javascript oder Python) aus, um Beispiele zur Verwendung des entsprechenden COS-SDK anzuzeigen. 

Beachten Sie hierbei, dass in allen Codebeispielen davon ausgegangen wird, dass ein Clientobjekt mit dem Namen `cos` vorhanden ist, mit dem die verschiedenen Methoden aufgerufen werden können. Detaillierte Informationen zum Erstellen von Clients finden Sie in den Leitfäden zum jeweiligen SDK.

Alle Datumswerte, die zum Festlegen der Aufbewahrungszeiträume verwendet werden, werden als GMT-Werte (GMT = Greenwich Mean Time) angegeben. Zur Sicherstellung der Datenintegrität ist ein Header `Content-MD5` erforderlich, der bei Verwendung eines SDK automatisch gesendet wird.
{:note}

### Aufbewahrungsrichtlinie für vorhandenes Bucket hinzufügen
{: #immutable-sdk-add-policy}
Bei dieser Implementierung der `PUT`-Operation wird der Abfrageparameter `protection` verwendet, um die Aufbewahrungsparameter für ein vorhandenes Bucket festzulegen. Diese Operation ermöglicht Ihnen das Festlegen der Werte für die mindestens erforderliche, maximal zulässige und standardmäßige Aufbewahrungsdauer. Mit dieser Operation können Sie auch den Schutzstatus des Buckets ändern. 

Objekte, die in ein geschütztes Bucket geschrieben wurden, können erst gelöscht werden, nachdem die Schutzfrist abgelaufen ist und alle Beweissicherungsvermerke für das Objekt entfernt wurden. Der Standardaufbewahrungswert des Buckets wird einem Objekt zugewiesen, es sei denn, bei der Erstellung des Objekts wird ein objektspezifischer Wert angegeben. Objekte in geschützten Buckets, die nicht mehr aufbewahrt werden müssen (Aufbewahrungszeitraum ist abgelaufen und für das Objekt wurden keine Beweissicherungsvermerke angegeben), werden im Falle einer Überschreibung erneut mit einer Aufbewahrungsdauer versehen. Die neue Aufbewahrungsdauer kann als Teil der Objektüberschreibungsanforderung angegeben werden oder dem Objekt wird der Standardaufbewahrungszeitraum des Buckets zugeordnet. 

Die unterstützten Mindest- und Maximalwerte für die Einstellungen `MinimumRetention`, `DefaultRetention` und `MaximumRetention` der Aufbewahrungsdauer lauten 0 Tage und 365243 Tage (1000 Jahre). 

Ein Header `Content-MD5` ist erforderlich. Diese Operation verwendet keine zusätzlichen Abfrageparameter.

Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
{:tip}

{: http}

**Syntax**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # Pfadstil
PUT https://{bucket-name}.{endpoint}?protection= # Stil des virtuellen Hosts
```
{: codeblock}
{: http}

**Beispielanforderung**
{: http}

```
PUT /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
x-amz-content-sha256: 2938f51643d63c864fdbea618fe71b13579570a86f39da2837c922bae68d72df
Content-MD5: GQmpTNpruOyK6YrxHnpj7g==
Content-Type: text/plain
Host: 67.228.254.193
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

**Beispielantwort**
{: http}

```
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.14.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```
{: codeblock}
{: http}

```py
def add_protection_configuration_to_bucket(bucket_name):
    try:
        new_protection_config = {
            "Status": "Retention",
            "MinimumRetention": {"Days": 10},
            "DefaultRetention": {"Days": 100},
            "MaximumRetention": {"Days": 1000}
        }

        cos.put_bucket_protection_configuration(Bucket=bucket_name, ProtectionConfiguration=new_protection_config)

        print("Protection added to bucket {0}\n".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to set bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addProtectionConfigurationToBucket(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    cos.setBucketProtection(bucketName, newConfig);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}

public static void addProtectionConfigurationToBucketWithRequest(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    SetBucketProtectionConfigurationRequest newRequest = new SetBucketProtectionConfigurationRequest()
        .withBucketName(bucketName)
        .withProtectionConfiguration(newConfig);

    cos.setBucketProtectionConfiguration(newRequest);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}
```
{: codeblock}
{: java}

### Aufbewahrungsrichtlinie eines Buckets prüfen
{: #immutable-sdk-get}

Bei dieser Implementierung der GET-Operation werden die Aufbewahrungsparameter eines vorhandenen Buckets abgerufen.
{: http}

**Syntax**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # Pfadstil
GET https://{bucket-name}.{endpoint}?protection= # Stil des virtuellen Hosts
```
{: codeblock}
{: http}

**Beispielanforderung**
{: http}

```xml
GET /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
Content-Type: text/plain
Host: 67.228.254.193
Example response
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.13.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

Wenn für ein Bucket keine Schutzkonfiguration festgelegt wurde, dann gibt der Server den Status 'Inaktiviert' aus.
{: http}

```xml
<ProtectionConfiguration>
  <Status>Disabled</Status>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

```py
def get_protection_configuration_on_bucket(bucket_name):
    try:
        response = cos.get_bucket_protection_configuration(Bucket=bucket_name)
        protection_config = response.get("ProtectionConfiguration")

        print("Bucket protection config for {0}\n".format(bucket_name))
        print(protection_config)
        print("\n")
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to get bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void getProtectionConfigurationOnBucket(String bucketName) {
    System.out.printf("Retrieving protection configuration from bucket: %s\n", bucketName;

    BucketProtectionConfiguration config = cos.getBucketProtection(bucketName);

    String status = config.getStatus();

    System.out.printf("Status: %s\n", status);

    if (!status.toUpperCase().equals("DISABLED")) {
        System.out.printf("Minimum Retention (Days): %s\n", config.getMinimumRetentionInDays());
        System.out.printf("Default Retention (Days): %s\n", config.getDefaultRetentionInDays());
        System.out.printf("Maximum Retention (Days): %s\n", config.getMaximumRetentionInDays());
    }
}
```
{: codeblock}
{: java}

### Objekt mit Aufbewahrungsrichtlinie in Bucket hochladen
{: #immutable-sdk-upload}

Diese Erweiterung der `PUT`-Operation fügt drei neue Anforderungsheader hinzu: Zwei zur Angabe der Aufbewahrungsdauer auf unterschiedliche Weise und einen zum Hinzufügen eines einzelnen Beweissicherungsvermerks zu dem neuen Objekt. Für unzulässige Werte bei neuen Headern werden neue Fehler definiert. Wenn für ein Objekt eine Aufbewahrungsdauer angegeben ist, dann schlagen Überschreibungen fehl.
{: http}

Objekte in Buckets mit Aufbewahrungsrichtlinie, die nicht mehr aufbewahrt werden müssen (Aufbewahrungszeitraum ist abgelaufen und für das Objekt wurden keine Beweissicherungsvermerke angegeben), werden im Falle einer Überschreibung erneut mit einer Aufbewahrungsdauer versehen. Die neue Aufbewahrungsdauer kann als Teil der Objektüberschreibungsanforderung angegeben werden oder dem Objekt wird der Standardaufbewahrungszeitraum des Buckets zugeordnet.

Ein Header `Content-MD5` ist erforderlich.
{: http}

Diese Header gelten auch für POST-Anforderungen für Objekte und für mehrteilige Uploads. Wird ein Objekt in mehreren Teilen hochgeladen, dann benötigt jedes Teil einen Header `Content-MD5`.
{: http}

|Wert 	| Typ 	| Beschreibung|
| --- | --- | --- | 
|`Retention-Period` | Nicht negative Ganzzahl (Sekunden) | Die Aufbewahrungsdauer des Objekts in Sekunden. Das Objekt kann vor Ablauf der Aufbewahrungsdauer weder überschrieben noch gelöscht werden. Werden dieses Feld und `Retention-Expiration-Date` angegeben, dann gibt das System einen Fehler `400` zurück. Wird keiner dieser Werte angegeben, dann wird der Wert für `DefaultRetention` des Buckets verwendet. Null (`0`) ist ein zulässiger Wert, wenn die mindestens erforderliche Aufbewahrungsdauer des Buckets ebenfalls `0` ist. |
| `Retention-expiration-date` | Datum (ISO 8601-Format) | Das Datum, an dem das Löschen oder Ändern des Objekts wieder zulässig sein wird. Sie können nur diesen Header oder den Header 'Retention-Period' angeben. Werden beide angegeben, dann gibt das System den Fehler `400` zurück. Wird keiner dieser Werte angegeben, dann wird der Wert für 'DefaultRetention' des Buckets verwendet. |
| `Retention-legal-hold-id` | Zeichenfolge | Ein einzelner Beweissicherungsvermerk, der auf das Objekt angewendet wird. Ein Beweissicherungsvermerk wird in einer langen Zeichenfolge (Y) angegeben. Das Objekt kann erst überschrieben oder gelöscht werden, nachdem alle dem Objekt zugeordneten Beweissicherungsvermerke entfernt wurden. |

```py
def put_object_add_legal_hold(bucket_name, object_name, file_text, legal_hold_id):
    print("Add legal hold {0} to {1} in bucket {2} with a putObject operation.\n".format(legal_hold_id, object_name, bucket_name))
    
    cos.put_object(
        Bucket=bucket_name,
        Key=object_name,
        Body=file_text, 
        RetentionLegalHoldId=legal_hold_id)

    print("Legal hold {0} added to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))
  
def copy_protected_object(source_bucket_name, source_object_name, destination_bucket_name, new_object_name):
    print("Copy protected object {0} from bucket {1} to {2}/{3}.\n".format(source_object_name, source_bucket_name, destination_bucket_name, new_object_name))

    copy_source = {
        "Bucket": source_bucket_name,
        "Key": source_object_name
    }

    cos.copy_object(
        Bucket=destination_bucket_name, 
        Key=new_object_name, 
        CopySource=copy_source, 
        RetentionDirective="Copy"
    )

    print("Protected object copied from {0}/{1} to {2}/{3}\n".format(source_bucket_name, source_object_name, destination_bucket_name, new_object_name));

def complete_multipart_upload_with_retention(bucket_name, object_name, upload_id, retention_period):
    print("Completing multi-part upload for object {0} in bucket {1}\n".format(object_name, bucket_name))

    cos.complete_multipart_upload(
        Bucket=bucket_name, 
        Key=object_name,
        MultipartUpload={
            "Parts":[{
                "ETag": part["ETag"],
                "PartNumber": 1
            }]
        },
        UploadId=upload_id,
        RetentionPeriod=retention_period
    )

    print("Multi-part upload completed for object {0} in bucket {1}\n".format(object_name, bucket_name))

def upload_file_with_retention(bucket_name, object_name, path_to_file, retention_period):
    print("Uploading file {0} to object {1} in bucket {2}\n".format(path_to_file, object_name, bucket_name))
    
    args = {
        "RetentionPeriod": retention_period
    }

    cos.upload_file(
        Filename=path_to_file,
        Bucket=bucket_name,
        Key=object_name,
        ExtraArgs=args
    )

    print("File upload complete to object {0} in bucket {1}\n".format(object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void putObjectAddLegalHold(String bucketName, String objectName, String fileText, String legalHoldId) {
    System.out.printf("Add legal hold %s to %s in bucket %s with a putObject operation.\n", legalHoldId, objectName, bucketName);

    InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileText.length());

    PutObjectRequest req = new PutObjectRequest(
        bucketName,
        objectName,
        newStream,
        metadata
    );
    req.setRetentionLegalHoldId(legalHoldId);

    cos.putObject(req);

    System.out.printf("Legal hold %s added to object %s in bucket %s\n", legalHoldId, objectName, bucketName);
}

public static void copyProtectedObject(String sourceBucketName, String sourceObjectName, String destinationBucketName, String newObjectName) {
    System.out.printf("Copy protected object %s from bucket %s to %s/%s.\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);

    CopyObjectRequest req = new CopyObjectRequest(
        sourceBucketName, 
        sourceObjectName, 
        destinationBucketName, 
        newObjectName
    );
    req.setRetentionDirective(RetentionDirective.COPY);
    

    cos.copyObject(req);

    System.out.printf("Protected object copied from %s/%s to %s/%s\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);
}
```
{: codeblock}
{: java}

### Beweissicherungsvermerk zu Objekt hinzufügen oder für Objekt entfernen
{: #immutable-sdk-legal-hold}

Bei dieser Implementierung der `POST`-Operation werden die Abfrageparameter `legalHold` sowie `add` und `remove` verwendet, um einen einzelnen Beweissicherungsvermerk zu einem geschützten Objekt in einem geschützten Bucket hinzuzufügen oder für ein solches Objekt zu entfernen.
{: http}

Das Objekt kann bis zu 100 Beweissicherungsvermerke unterstützen:

*  Die Kennung eines Beweissicherungsvermerks besteht aus einer Zeichenfolge mit maximal 64 Zeichen Länge und einer Mindestlänge von einem Zeichen. Folgende Zeichen sind zulässig: Buchstaben, Zahlen sowie die Zeichen `!`, `_`, `.`, `*`, `(`, `)`, `-` und `.
* Wenn durch das Hinzufügen des angegebenen Beweissicherungsvermerks der Grenzwert von insgesamt 100 Beweissicherungsvermerken für das Objekt überschritten wird, dann wird der neue Beweissicherungsvermerk nicht hinzugefügt und das System gibt den Fehler `400` aus.
* Wenn eine Kennung zu lang ist, dann wird sie nicht zum Objekt hinzugefügt und das System gibt den Fehler `400` zurück.
* Wenn eine Kennung ungültige Zeichen enthält, dann wird sie nicht zum Objekt hinzugefügt und das System gibt den Fehler `400` zurück.
* Wenn eine Kennung für ein Objekt bereits verwendet wird, dann wird der vorhandene Beweissicherungsvermerk nicht geändert und in der Antwort wird angegeben, dass die Kennung bereits belegt ist (Fehler `409`).
* Wenn ein Objekt nicht über Metadaten zur Aufbewahrungsdauer verfügt, wird der Fehler `400` zurückgegeben und es kann kein Beweissicherungsvermerk hinzugefügt oder entfernt werden.

Das Vorhandensein eines Headers für die Aufbewahrungsdauer ist erforderlich, da andernfalls der Fehler `400` zurückgegeben wird.
{: http}

Der Benutzer, der einen Beweissicherungsvermerk hinzufügt oder entfernt, muss über die Berechtigungen für `Manager` für dieses Bucket verfügen.

Ein Header `Content-MD5` ist erforderlich. Diese Operation verwendet keine operationsspezifischen Nutzdatenelemente.
{: http}

**Syntax**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # Pfadstil
POST https://{bucket-name}.{endpoint}?legalHold= # Stil des virtuellen Hosts
```
{: http}

**Beispielanforderung**
{: http}

```
POST /BucketName/ObjectName?legalHold&add=legalHoldID HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
```
{: codeblock}
{: http}

**Beispielantwort**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}

```py
def add_legal_hold_to_object(bucket_name, object_name, legal_hold_id):
    print("Adding legal hold {0} to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.add_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} added to object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))

def delete_legal_hold_from_object(bucket_name, object_name, legal_hold_id):
    print("Deleting legal hold {0} from object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.delete_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} deleted from object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addLegalHoldToObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Adding legal hold %s to object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.addLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s added to object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}

public static void deleteLegalHoldFromObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Deleting legal hold %s from object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.deleteLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s deleted from object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}
```
{: codeblock}
{: java}

### Aufbewahrungsdauer eines Objekts verlängern
{: #immutable-sdk-extend}

Bei dieser Implementierung der `POST`-Operation wird der Abfrageparameter `extendRetention` verwendet, um die Aufbewahrungsdauer eines geschützten Objekts in einem geschützten Bucket zu verlängern.
{: http}

Die Aufbewahrungsdauer eines Objekts kann nur verlängert werden. Sie kann gegenüber dem momentan konfigurierten Wert nicht verkürzt werden.

Der Wert für die Verlängerung der Aufbewahrungsdauer kann auf die folgenden drei Arten festgelegt werden:

* Hinzufügen zusätzlicher Zeit zum aktuellen Wert (`Additional-Retention-Period` oder ähnliche Methode)
* Definieren einer neuen Verlängerungszeitdauer in Sekunden (`Extend-Retention-From-Current-Time` oder ähnliche Methode)
* Definieren eines neuen Ablaufdatums für die Aufbewahrung des Objekts (`New-Retention-Expiration-Date` oder ähnliche Methode)

Die in den Objektmetadaten momentan gespeicherte Aufbewahrungsdauer wird entweder um die angegebene zusätzliche Zeitdauer verlängert oder durch den neuen Wert ersetzt. Welche Methode verwendet wird, hängt von dem Parameter ab, der in der Anforderung für `extendRetention` festgelegt wird. In allen Fällen wird der Parameter zur Verlängerung der Aufbewahrungsdauer mit der aktuellen Aufbewahrungsdauer verglichen. Der Parameter für die Verlängerung wird nur dann akzeptiert, wenn die aktualisierte Aufbewahrungsdauer länger als die aktuelle Aufbewahrungsdauer ist.

Objekte in geschützten Buckets, die nicht mehr aufbewahrt werden müssen (Aufbewahrungszeitraum ist abgelaufen und für das Objekt wurden keine Beweissicherungsvermerke angegeben), werden im Falle einer Überschreibung erneut mit einer Aufbewahrungsdauer versehen. Die neue Aufbewahrungsdauer kann als Teil der Objektüberschreibungsanforderung angegeben werden oder dem Objekt wird der Standardaufbewahrungszeitraum des Buckets zugeordnet.

**Syntax**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # Pfadstil
POST https://{bucket-name}.{endpoint}?extendRetention= # Stil des virtuellen Hosts
```
{: codeblock}
{: http}

**Beispielanforderung**
{: http}

```yaml
POST /BucketName/ObjectName?extendRetention HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00GMT
Authorization: authorization string
Content-Type: text/plain
Additional-Retention-Period: 31470552
```
{: codeblock}
{: http}

**Beispielantwort**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:50:00GMT
Connection: close
```
{: codeblock}
{: http}

```py
def extend_retention_period_on_object(bucket_name, object_name, additional_seconds):
    print("Extend the retention period on {0} in bucket {1} by {2} seconds.\n".format(object_name, bucket_name, additional_seconds))

    cos.extend_object_retention(
        Bucket=bucket_ame,
        Key=object_name,
        AdditionalRetentionPeriod=additional_seconds
    )

    print("New retention period on {0} is {1}\n".format(object_name, additional_seconds))
```
{: codeblock}
{: python}

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void extendRetentionPeriodOnObject(String bucketName, String objectName, Long additionalSeconds) {
    System.out.printf("Extend the retention period on %s in bucket %s by %s seconds.\n", objectName, bucketName, additionalSeconds);

    ExtendObjectRetentionRequest req = new ExtendObjectRetentionRequest(
        bucketName, 
        objectName)
        .withAdditionalRetentionPeriod(additionalSeconds);

    cos.extendObjectRetention(req);

    System.out.printf("New retention period on %s is %s\n", objectName, additionalSeconds);
}
```
{: codeblock}
{: java}

### Beweissicherungsvermerke eines Objekts auflisten
{: #immutable-sdk-list-holds}

Bei dieser Implementierung der `GET`-Operation wird der Abfrageparameter `legalHold` verwendet, um die Liste der Beweissicherungsvermerke für ein Objekt und den zugehörigen Aufbewahrungsstatus in einem XML-Antworthauptteil zurückzugeben.
{: http}

Diese Operation gibt Folgendes zurück:

* Objekterstellungsdatum
* Aufbewahrungsdauer des Objekts in Sekunden
* Berechnetes Ablaufdatum der Aufbewahrung auf Basis der Aufbewahrungsdauer und des Erstellungsdatums
* Liste der Beweissicherungsvermerke
* Kennung des Beweissicherungsvermerks
* Zeitmarke für Anwendung des Beweissicherungsvermerks

Wenn für das Objekt keine Beweissicherungsvermerke definiert wurden, dann gibt das System ein leeres Element `LegalHoldSet` zurück.
Wurde keine Aufbewahrungsdauer für das Objekt angegeben, dann wird der Fehler `404` zurückgegeben.

**Syntax**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # Pfadstil
GET https://{bucket-name}.{endpoint}?legalHold= # Stil des virtuellen Hosts
```
{: http}

**Beispielanforderung**
{: http}

```
GET /BucketName/ObjectName?legalHold HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: {authorization-string}
Content-Type: text/plain
```
{: codeblock}
{: http}

**Beispielantwort**
{: http}

```xml
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
<?xml version="1.0" encoding="UTF-8"?>
<RetentionState>
  <CreateTime>Fri, 8 Sep 2018 21:33:08 GMT</CreateTime>
  <RetentionPeriod>220752000</RetentionPeriod>
  <RetentionPeriodExpirationDate>Fri, 1 Sep 2023 21:33:08
GMT</RetentionPeriodExpirationDate>
  <LegalHoldSet>
    <LegalHold>
      <ID>SomeLegalHoldID</ID>
      <Date>Fri, 8 Sep 2018 23:13:18 GMT</Date>
    </LegalHold>
    <LegalHold>
    ...
    </LegalHold>
  </LegalHoldSet>
</RetentionState>
```
{: codeblock}
{: http}

```py 
def list_legal_holds_on_object(bucket_name, object_name):
    print("List all legal holds on object {0} in bucket {1}\n".format(object_name, bucket_name));

    response = cos.list_legal_holds(
        Bucket=bucket_name,
        Key=object_name
    )

    print("Legal holds on bucket {0}: {1}\n".format(bucket_name, response))
```
{: codeblock}
{: python}

```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void listLegalHoldsOnObject(String bucketName, String objectName) {
    System.out.printf("List all legal holds on object %s in bucket %s\n", objectName, bucketName);

    ListLegalHoldsResult result = cos.listLegalHolds(
        bucketName, 
        objectName
    );

    System.out.printf("Legal holds on bucket %s: \n", bucketName);

    List<LegalHold> holds = result.getLegalHolds();
    for (LegalHold hold : holds) {
        System.out.printf("Legal Hold: %s", hold);
    }
}
```
{: codeblock}
{: java}
