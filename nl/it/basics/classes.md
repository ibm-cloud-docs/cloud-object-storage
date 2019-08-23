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

# Utilizza le classi di archiviazione
{: #classes}

Non tutti i dati forniscono carichi di lavoro attivi. I dati archiviati possono rimanere inalterati per lunghi periodi di tempo. Per carichi di lavoro meno attivi, puoi creare i bucket con diverse classi di archiviazione. Gli oggetti archiviati in questi bucket comporteranno degli addebiti in una pianificazione diversa dall'archiviazione standard. 

## Cosa sono le classi?
{: #classes-about}

Ci sono quattro classi di archiviazione:

*  **Standard**: utilizzata per i carichi di lavoro attivi - non ci sono addebiti per i dati richiamati (a parte il costo della richiesta operativa stessa e della larghezza di banda in uscita pubblica). 
*  **Vault**: utilizzata per i carichi di lavoro cool in cui non si esegue di frequente l'accesso ai dati - si applica un addebito per il richiamo per la lettura dei dati. Il servizio include una soglia per la dimensione dell'oggetto e il periodo di archiviazione coerenti con l'uso previsto di questo servizio per i dati meno attivi. 
*  **Cold Vault**: utilizzata per i carichi di lavoro cold in cui i dati vengono principalmente archiviati (accesso ogni 90 giorni o meno) - si applica un addebito per il richiamo maggiore per la lettura dei dati. Il servizio include una soglia per la dimensione dell'oggetto e il periodo di archiviazione coerenti con l'uso previsto di questo servizio: archiviazione dei dati cold, inattivi.
*  **Flex**: utilizzata per i carichi di lavoro dinamici in cui i modelli di accesso sono più difficili da prevedere. A seconda dell'utilizzo, se i costi inferiori di un'archiviazione meno utilizzata combinati con gli addebiti per il richiamo superano un valore limite, l'addebito di archiviazione aumenta e non vengono applicati altri addebiti per il richiamo. Se non si accede di frequente ai dati, l'archiviazione Flex può essere più conveniente di quella Standard e se i modelli di utilizzo meno utilizzati diventano più attivi, l'archiviazione Flex è più conveniente dell'archiviazione Vault o Cold Vault. Non vengono applicati periodi di archiviazione o dimensioni dell'oggetto della soglia ai bucket Flex. 

Per i dettagli dei prezzi, vedi [la tabella dei prezzi sul sito ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Per informazioni su come creare i bucket con diverse classi di archiviazione, vedi il [riferimento API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

## Come posso creare un bucket con una classe di archiviazione diversa?
{: #classes-locationconstraint}

Quando crei un bucket nella console, è presente un menu a discesa che ti consente di selezionare la classe di archiviazione.  

Quando crei i bucket in modo programmatico, devi specificare una `LocationConstraint` che corrisponde all'endpoint utilizzato. I codici di provisioning validi per `LocationConstraint` sono: <br>
&emsp;&emsp;  **Geografia Stati Uniti:** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **Stati Uniti Est:** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **Stati Uniti Sud:** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **Geografia UE:** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **Gran Bretagna UE:** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **Germania UE:** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **Geografia Asia Pacifico:** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **Giappone Asia Pacifico:** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **Australia Asia Pacifico:** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **Amsterdam:** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **Chennai:** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **Hong Kong:** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **Melbourne:** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **Messico:** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **Milano:** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **Montréal:** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **Oslo:** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **San Jose:** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **San Paolo:** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **Seul:** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **Toronto:** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). 

## Utilizzo dell'API REST, delle librerie e degli SDK
{: #classes-sdk}

Sono state introdotte diverse nuove API negli SDK IBM COS per fornire supporto per le applicazioni che utilizzano le politiche di conservazione. Seleziona un linguaggio (curl, Java, Javascript, Go o Python) nella parte superiore di questa pagina per visualizzare gli esempi che utilizzano l'SDK COS appropriato.  

Tieni presente che tutti gli esempi di codice presuppongono l'esistenza di un oggetto client denominato `cos` che può richiamare i diversi metodi. Per dettagli sulla creazione dei client, vedi le guide SDK specifiche. 


### Crea un bucket con una classe di archiviazione

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

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Names
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

Non è possibile modificare la classe di archiviazione di un bucket una volta creato il bucket. Se gli oggetti devono essere riclassificati, è necessario spostare i dati in un altro bucket con la classe di archiviazione desiderata.  
