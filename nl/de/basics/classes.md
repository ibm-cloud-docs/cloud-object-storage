---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-14"

keywords: storage classes, tiers, cost, buckets, location constraint, provisioning code, locationconstraint

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
{:table: .aria-labeledby="caption"}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}
{:curl: .ph data-hd-programlang='curl'}

# Speicherklassen verwenden
{: #classes}

Nicht alle Daten fließen in aktive Workloads ein. Auf Archivdaten wird möglicherweise über lange Zeiträume hinweg nicht zugegriffen. Für Workloads mit einem geringeren Aktivitätsgrad können Sie Buckets mit anderen Speicherklassen erstellen. Für die Objekte, die in diesen Buckets gespeichert sind, werden die Gebühren auf Basis eines anderen Zeitplans erhoben, als dies bei einem Standardspeicher der Fall ist.

## Was sind Klassen?
{: #classes-about}

Es stehen vier Speicherklassen zur Verfügung:

*  **Standard**: Wird für aktive Workloads verwendet. Für abgerufene Daten fallen (außer den Kosten für die während des Betriebs ausgeführten Anforderungen selbst und die öffentliche abgehende Bandbreite) keine Gebühren an. 
*  **Vault**: Wird für kalte Workloads verwendet, bei denen nur wenige Datenzugriffe stattfinden. Für das Lesen von Daten fällt eine Abfragegebühr an. Der Service umfasst einen Schwellenwert für die Objektgröße und den Speicherzeitraum, der von der gewünschten Nutzung dieses Service für weniger häufig verwendete Daten mit einem geringeren Aktivitätsgrad abhängt.
*  **Kalte Vault**: Wird für kalte Workloads verwendet, deren Daten überwiegend archiviert sind (Zugriff alle 90 Tage oder seltener). Eine höhere Abfragegebühr fällt für das Lesen der Daten an. Der Service umfasst einen Schwellenwert für die Objektgröße und den Speicherzeitraum, der von der gewünschten Nutzung dieses Service (Speicherung kalter, inaktiver Daten) abhängt.
*  **Flex**: Wird für dynamische Workloads verwendet, deren Zugriffsmuster schwieriger vorherzusagen sind. Abhängig von der Nutzung. Wenn die niedrigeren Kosten für kalten Speicher kombiniert mit den Abfragegebühren einen Kappungswert übersteigen, dann wird die Speichergebühr erhöht und es werden keine Abfragegebühren mehr erhoben. Wird auf die Daten selten zugegriffen, dann kann der Flexspeicher kostenwirksamer als der Standardspeicher sein. Treten bei kälteren Nutzungsmustern mehr Aktivitäten auf, dann ist der Flexspeicher kostenwirksamer als der Vaultspeicher oder der kalte Vaultspeicher. Für Flexbuckets gelten keine Schwellenwerte in Bezug auf die Objektgröße oder den Speicherzeitraum.

Detaillierte Preisinformationen finden Sie in der [Tabelle mit der Preisstruktur unter ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Informationen zur Vorgehensweise beim Erstellen von Buckets mit anderen Speicherklassen finden Sie in der [API-Referenz](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

## Wie kann ich ein Bucket mit einer anderen Speicherklasse erstellen?
{: #classes-locationconstraint}

Wenn Sie in der Konsole ein Bucket erstellen, dann können Sie dort über ein Dropdown-Menü die gewünschte Speicherklasse auswählen. 

Bei der programmgesteuerten Erstellung von Buckets müssen Sie einen Wert für `LocationConstraint` angeben, der mit dem verwendeten Endpunkt korrespondiert. Für `LocationConstraint` stehen die folgenden gültigen Bereitstellungscodes zur Verfügung:<br>
&emsp;&emsp;  **Vereinigte Staaten (Ländergruppe):** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **Vereinigte Staaten (Osten):** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **Vereinigte Staaten (Süden):** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **Europa (Ländergruppe):** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **Europa (Großbritannien):** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **Europa (Deutschland):** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **Asien-Pazifik (Ländergruppe):** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **Asien-Pazifik (Japan):** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **Asien-Pazifik (Australien):** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **Amsterdam:** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **Chennai:** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **Hongkong:** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **Melbourne:** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **Mexiko:** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **Mailand:** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **Montréal:** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **Oslo:** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **San Jose:** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **São Paulo:** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **Seoul:** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **Toronto:** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## REST-API, Bibliotheken und SDKs verwenden
{: #classes-sdk}

Für die IBM COS-SDKs wurden mehrere neue APIs eingeführt, um Unterstützung für Anwendungen zu bieten, die mit Aufbewahrungsrichtlinien arbeiten. Wählen Sie oben auf dieser Seite eine Sprache (curl, Java, Javascript, Go oder Python) aus, um Beispiele zur Verwendung des entsprechenden COS-SDK anzuzeigen. 

Beachten Sie hierbei, dass in allen Codebeispielen davon ausgegangen wird, dass ein Clientobjekt mit dem Namen `cos` vorhanden ist, mit dem die verschiedenen Methoden aufgerufen werden können. Detaillierte Informationen zum Erstellen von Clients finden Sie in den Leitfäden zum jeweiligen SDK.


### Bucket mit Speicherklasse erstellen

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName, "us-vault");
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```
{: codeblock}
{: java}


```javascript
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: 'us-standard'
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}


```py
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create bucket: {0}".format(e))
```
{: codeblock}
{: python}

```go
func main() {

    // Client erstellen
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucketnamen
    newBucket := "<NEW_BUCKET_NAME>"

    input := &s3.CreateBucketInput{
        Bucket: aws.String(newBucket),
        CreateBucketConfiguration: &s3.CreateBucketConfiguration{
            LocationConstraint: aws.String("us-cold"),
        },
    }
    client.CreateBucket(input)

    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```
{: codeblock}
{: go}


```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}
{: curl}

Die Speicherklasse eines Buckets kann nach der Erstellung des Buckets nicht mehr geändert werden. Wenn für Objekte eine andere Speicherklasse angegeben werden muss, dann müssen die Daten in ein anderes Bucket verschoben werden, für das die gewünschte Speicherklasse definiert wurde. 
