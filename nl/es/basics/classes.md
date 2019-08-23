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

# Utilización de clases de almacenamiento
{: #classes}

No todos los datos alimentan cargas de trabajo activas. Los datos archivados pueden permanecer inalterados durante largos periodos de tiempo. Para cargas de trabajo menos activas, puede crear grupos con distintas clases de almacenamiento. Los objetos almacenados en estos grupos incurrirán en cargos según una planificación distinta a la del almacenamiento estándar.

## ¿Cuáles son las clases?
{: #classes-about}

Hay cuatro clases de almacenamiento:

*  **Estándar (Standard)**: se utiliza para cargas de trabajo activas; no hay ningún cargo para los datos recuperados (aparte del coste de la propia solicitud operativa y del ancho de banda de salida público).
*  **Caja fuerte (Vault)**: se utiliza para cargas de trabajo en frío, en las que no se accede a los datos con frecuencia; se aplica un cargo de recuperación por leer datos. El servicio incluye un umbral de tamaño de objeto y de periodo de almacenamiento coherente con el uso previsto de este servicio de datos en frío menos activos.
*  **Caja fuerte fría (Cold Vault)**: se utiliza para cargas de trabajo en frío donde los datos principalmente se archivan (se accede a los mismos cada 90 días o menos); se aplica un cargo de recuperación mayor por leer datos. El servicio incluye un umbral de tamaño de objeto y periodo de almacenamiento coherente con el uso previsto de este servicio: almacenamiento de datos en frío, inactivos.
*  **Flex**: se utiliza para cargas de trabajo dinámicas, para las que es más difícil predecir patrones de acceso. En función del uso, si los costes más bajos del almacenamiento de en frío combinados con cargos de recuperación superan un valor límite, el cargo por almacenamiento aumenta y no se aplica ningún cargo de recuperación. Si no se accede a los datos con frecuencia, el almacenamiento Flex puede ser más rentable que el almacenamiento Estándar, y si los patrones de uso en frío se vuelven más activos, el almacenamiento Flex resulta más rentable que el almacenamiento de tipo Caja fuerte o Caja fuerte fría. No se aplica ningún tamaño de objeto de umbral ni período de almacenamiento a los grupos Flex.

Para obtener información detallada sobre los precios, consulte [la tabla de precios en ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Para obtener información sobre cómo crear grupos con diferentes clases de almacenamiento, revise la [Consulta de API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

## ¿Cómo se crea un grupo con una clase de almacenamiento diferente?
{: #classes-locationconstraint}

Cuando se crea un grupo en la consola, hay un menú desplegable que permite seleccionar la clase de almacenamiento. 

Cuando se crean grupos mediante programación, es necesario especificar un valor de `LocationConstraint` que se corresponde con el punto final utilizado. Los códigos de suministro válidos para `LocationConstraint` son los siguientes: <br>
&emsp;&emsp;  **EE. UU. Geo:** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **EE. UU. este:** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **EE. UU. sur:** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **UE Geo:** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **UE Gran Bretaña:** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **UE Alemania:** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **AP Geo:** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **AP Japón:** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **AP Australia:** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **Amsterdam:** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **Chennai:** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **Hong Kong:** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **Melbourne:** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **México:** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **Milán:** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **Montreal:** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **Oslo:** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **San José:** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **São Paulo:** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **Seúl:** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **Toronto:** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Utilización de API REST, bibliotecas y SDK
{: #classes-sdk}

Se han incorporado varias API nuevas en los SDK de IBM COS para proporcionar soporte para las aplicaciones que trabajan con políticas de retención. Seleccione un lenguaje (curl, Java, Javascript, Go o Python) en la parte superior de esta página para ver ejemplos de utilización del SDK de COS adecuado. 

Tenga en cuenta que en todos los ejemplos de código se asume la existencia de un objeto de cliente llamado `cos` que puede llamar a los distintos métodos. Para ver información sobre cómo crear clientes, consulte las guías de SDK específicas.


### Creación de un grupo con una clase de almacenamiento

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

    // Crear cliente
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Nombres de grupo
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

No se puede cambiar la clase de almacenamiento de un grupo una vez que creado el grupo. Si se tienen que volver a clasificar objetos, hay que mover los datos a otro grupo con la clase de almacenamiento deseada. 
