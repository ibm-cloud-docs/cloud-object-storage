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

# Utilisation de classes de stockage
{: #classes}

Toutes les données n'alimentent pas les charges de travail actives. Les données archivées peuvent demeurer intactes durant de longues périodes. Pour les charges de travail moins actives, vous pouvez créer des compartiments avec différentes classes de stockage. Le stockage d'objets dans ces compartiments entraîne des frais selon une planification différente de celle du stockage standard. 

## Que sont les classes ?
{: #classes-about}

Il existe quatre classes de stockage :

*  **Standard** : classe utilisée pour les charges de travail actives, sans frais pour les données extraites (en dehors du coût de la demande opérationnelle proprement dite et de la bande passante sortante publique). 
*  **Coffre** : classe utilisée pour les charges de travail tièdes pour lesquelles les données ne sont pas fréquemment consultées. Des frais d'extraction s'appliquent lors de la lecture des données. Le service comprend un seuil pour la taille d'objet et la durée de stockage adaptés à l'usage prévu, le stockage de données moins actives tièdes. 
*  **Coffre froid** : classe utilisée pour les charges de travail froides pour lesquelles les données sont principalement archivées (consultées tous les 90 jours tout au plus). Des frais d'extraction supérieurs s'appliquent lors de la lecture des données. Le service comprend un seuil pour la taille d'objet et la durée de stockage adaptés à l'usage prévu, le stockage de données inactives froides. 
*  **Flex** : classe utilisée pour les charges de travail dynamiques pour lesquelles les modèles d'accès sont plus difficiles à prévoir. En fonction de l'utilisation, si les coûts plus bas du stockage tiède combinés aux frais d'extraction dépassent une valeur plafonnée, les frais de stockage augmentent et les frais d'extraction sont abandonnés. Si les données ne sont pas fréquemment consultées, le stockage Flex peut être plus économique que le stockage standard, et si les modèles d'utilisation tièdes deviennent plus actifs, le stockage Flex est plus économique que le stockage Coffre ou Coffre froid. Aucun seuil de taille d'objet ou de période de stockage ne s'applique aux compartiments Flex. 

Pour plus d'informations sur la tarification, voir le [tableau de tarification à l'adresse ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Pour plus d'informations sur la création de compartiments avec différentes classes de stockage, voir la rubrique [Référence d'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

## Création d'un compartiment avec une classe de stockage différente
{: #classes-locationconstraint}

Lorsque vous créez un compartiment dans la console, un menu déroulant vous permet de sélectionner des classes de stockage.  

Lors de la création de compartiments à l'aide d'un programme, il est nécessaire de spécifier une contrainte `LocationConstraint` correspondant au noeud final utilisé. Les codes de mise à disposition valides pour `LocationConstraint` sont les suivants : <br>
&emsp;&emsp;  **Etats-Unis : ** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **Est des Etats-Unis : ** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **Sud des Etats-Unis : ** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **Union européenne : ** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **Union européenne - Grande-Bretagne : ** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **Union européenne - Allemagne : ** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **Asie-Pacifique : ** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **Asie-Pacifique - Japon : ** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **Asie-Pacifique - Australie : ** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **Amsterdam : ** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **Chennai : ** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **Hong Kong : ** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **Melbourne : ** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **Mexico : ** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **Milan : ** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **Montréal : ** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **Oslo : ** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **San José : ** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **São Paulo : ** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **Séoul : ** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **Toronto : ** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Utilisation de l'API REST, de bibliothèques et de SDK
{: #classes-sdk}

Plusieurs nouvelles API ont été introduites dans les SDK IBM COS pour fournir une prise en charge des applications qui fonctionnent avec les règles de conservation. Sélectionnez un langage (curl, Java, Javascript, Go ou Python) en haut de cette page pour visualiser des exemples d'utilisation du SDK COS approprié.  

Notez que tous les exemples de code partent du principe qu'il existe un objet client appelé `cos` pouvant appeler les différentes méthodes. Pour plus de détails sur la création de clients, voir les guides SDK spécifiques. 


### Création d'un compartiment avec une classe de stockage

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

Une fois qu'un compartiment est créé, sa classe de stockage ne peut pas être modifiée. Si des objets doivent être reclassifiés, il est nécessaire de déplacer les données vers un autre compartiment doté de la classe de stockage souhaitée.  
