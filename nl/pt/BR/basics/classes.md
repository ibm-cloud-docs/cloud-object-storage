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

# Usar classes de armazenamento
{: #classes}

Nem todos os dados alimentam cargas de trabalho ativas. Os dados de arquivamento podem ficar intocados por longos períodos. Para cargas de trabalho menos ativas, é possível criar depósitos com diferentes classes de armazenamento. Os objetos armazenados nesses depósitos incorrerão em encargos em um planejamento diferente do armazenamento Standard.

## Quais são as classes?
{: #classes-about}

Há quatro classes de armazenamento:

*  **Standard**: usado para cargas de trabalho ativas - não há encargos para dados recuperados (além do custo da solicitação operacional em si e da largura da banda de saída pública).
*  **Vault**: usado para cargas de trabalho frias nas quais os dados não são acessados frequentemente - um encargo de recuperação se aplica à leitura de dados. O serviço inclui um limite para o tamanho do objeto e o período de armazenamento consistentes com o uso desejado desse serviço para dados mais frios e menos ativos.
*  **Cold Vault**: usado para cargas de trabalho inativas nas quais os dados são primordialmente arquivados (acessados a cada 90 dias ou menos) - um encargo de recuperação maior se aplica à leitura de dados. O serviço inclui um limite para o tamanho do objeto e o período de armazenamento consistentes com o uso desejado desse serviço: armazenando dados frios e inativos.
*  **Flex**: usado para cargas de trabalho dinâmicas nas quais os padrões de acesso são mais difíceis de prever. Dependendo do uso, se os custos inferiores de armazenamento mais frio combinados com os encargos de recuperação excederem um valor máximo, o encargo de armazenamento aumentará e nenhum encargo de recuperação se aplicará. Se os dados não são acessados frequentemente, o armazenamento Flex pode ser mais eficiente em custo do que o armazenamento Standard e, se os padrões de uso mais frio se tornam mais ativos, o armazenamento Flex é mais eficiente em custo do que o armazenamento Vault ou Cold Vault. Nenhum tamanho de objeto de limite ou período de armazenamento se aplica aos depósitos do Flex.

Para obter detalhes de precificação, consulte [a tabela de precificação em ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Para obter informações sobre como criar depósitos com diferentes classes de armazenamento, consulte a [Referência da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

## Como criar um depósito com uma classe de armazenamento diferente?
{: #classes-locationconstraint}

Ao criar um depósito no console, há um menu suspenso que permite a seleção de classe de armazenamento. 

Ao criar depósitos programaticamente, é necessário especificar um `LocationConstraint` que corresponde ao terminal usado. Os códigos de fornecimento válidos para `LocationConstraint` são: <br>
&emsp;&emsp;  **Geografia dos EUA:** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **Leste dos EUA:** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **Sul dos EUA:** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **Geografia da UE:** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **Grã-Bretanha UE:** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **Alemanha UE:** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **Geografia da AP:** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **Japão AP:** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **Austrália AP:** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **Amsterdã:** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **Chennai:** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **Hong Kong:** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **Melbourne:** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **México:** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **Milão:** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **Montreal:** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **Oslo:** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **São José:** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **São Paulo:** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **Seul:** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **Toronto:** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

## Usando a API de REST, as bibliotecas e os SDKs
{: #classes-sdk}

Diversas APIs novas foram introduzidas nos SDKs do IBM COS para fornecer suporte para aplicativos que trabalham com políticas de retenção. Selecione uma linguagem (curl, Java, Javascript, Go ou Python) na parte superior desta página para visualizar exemplos usando o SDK do COS apropriado. 

Observe que todos os exemplos de código supõem a existência de um objeto de cliente chamado `cos` que pode chamar os diferentes métodos. Para obter detalhes sobre como criar clientes, consulte os guias de SDK específicos.


### Criar um depósito com uma classe de armazenamento

```java
public static void createBucket (String bucketName) {
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

Não é possível mudar a classe de armazenamento de um depósito depois que ele é criado. Se os objetos precisarem ser reclassificados, será necessário mover os dados para outro depósito com a classe de armazenamento desejada. 
