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

# Usar o Immutable Object Storage
{: #immutable}

O Immutable Object Storage permite que os clientes preservem registros eletrônicos e mantenham a integridade de dados em uma maneira WORM (Write-Once-Read-Many), não apagável e não regravável até o término de seu período de retenção e a remoção de quaisquer retenções legais. Esse recurso pode ser usado por quaisquer clientes que tenham a necessidade de retenção de dados de longo prazo em seu ambiente, incluindo, entre outras, organizações nas indústrias a seguir:

 * Financeiro
 * Saúde
 * Archives de conteúdo de mídia
 * Aqueles que estão procurando evitar modificação ou exclusão privilegiada de objetos ou documentos

Os recursos subjacentes também podem ser usados por organizações que lidam com gerenciamento de registros financeiros, como transações de corretor de valores, e talvez seja necessário reter os objetos em um formato não regravável e não apagável. 

O Immutable Object Storage está disponível somente em determinadas regiões, consulte [Serviços integrados](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability) para obter detalhes. Ele também requer um plano de precificação Standard. Consulte [precificação](https://www.ibm.com/cloud/object-storage) para obter detalhes.
{:note}

Não é possível usar o Aspera high-speed transfer com depósitos com uma política de retenção.
{:important}

## Terminologia e uso
{: #immutable-terminology}

### Período de Retenção
{: #immutable-terminology-period}

A duração de tempo em que um objeto deve permanecer armazenado no depósito do COS.

### Política de retenção
{: #immutable-terminology-policy}

Uma política de retenção é ativada no nível do depósito do COS. Os períodos mínimo, máximo e padrão de retenção são definidos por essa política e se aplicam a todos os objetos no depósito.

O período mínimo de retenção é a duração mínima de tempo que um objeto deve ser retido no depósito.

O período máximo de retenção é a duração máxima de tempo que um objeto pode ser retido no depósito.

Se um objeto for armazenado no depósito sem especificar um período de retenção customizado, o período de retenção padrão será usado. O período mínimo de retenção deve ser menor ou igual ao período de retenção padrão, que, por sua vez, deve ser menor ou igual ao período máximo de retenção.

Nota: um período máximo de retenção de 1000 anos pode ser especificado para os objetos.

Nota: para criar uma política de retenção em um depósito, você precisará de função de Gerenciador. Consulte [Permissões de depósito](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions) para obter mais detalhes.

### Retenção legal 
{: #immutable-terminology-hold}

Determinados registros (objetos) podem precisar ser impedidos de exclusão mesmo após a expiração do período de retenção; por exemplo, uma revisão legal que está pendente de conclusão pode requerer acesso aos registros para uma duração ampliada que vai além do período de retenção que foi originalmente configurado para o objeto. Em tal cenário, uma sinalização de retenção legal pode ser aplicada no nível do objeto. 
 
As retenções legais podem ser aplicadas aos objetos durante os uploads iniciais para o depósito cos ou após um objeto ter sido incluído.  

Nota: um máximo de 100 retenções legais pode ser aplicado por objeto.

### Retenção indefinida
{: #immutable-terminology-indefinite}

Permite que o usuário configure o objeto para ser armazenado indefinidamente até que um novo período de retenção seja aplicado. Isso é configurado em um nível por objeto.

### Retenção baseada em evento
{: #immutable-terminology-events}

O Immutable Object Storage permite que os usuários configurem a retenção indefinida no objeto se eles não têm certeza sobre a duração final do período de retenção para seu caso de uso ou gostariam de usar o recurso de retenção baseada em evento. Quando configurado como indefinido, os aplicativos de usuário podem então mudar a retenção de objetos para um valor finito posteriormente. Por exemplo, uma empresa tem uma política de retenção de registros de funcionários por três anos depois que o funcionário sai da empresa. Quando um funcionário se associa à empresa, os registros associados a esse funcionário podem ser retidos indefinidamente. Quando o funcionário sai da empresa, a retenção indefinida é convertida em um valor finito de três anos a partir do momento atual, conforme definido pela política da empresa. O objeto é então protegido por três anos após a mudança do período de retenção. Um aplicativo de usuário ou terceiro pode mudar o período de retenção de retenção indefinida para finita usando o SDK ou a API de REST.

### Retenção permanente
{: #immutable-terminology-permanent}

A retenção permanente pode ser ativada somente em um nível do depósito do COS com a política de retenção ativada e os usuários são capazes de selecionar a opção de período de retenção permanente durante os uploads de objetos. Uma vez ativado, esse processo não pode ser revertido e os objetos transferidos por upload usando o período de retenção permanente **não podem ser excluídos**. É responsabilidade dos usuários validar da parte deles se há uma necessidade legítima de armazenar objetos **permanentemente** usando depósitos do COS com a política de retenção. 


Ao usar o Immutable Object Storage, você é responsável por assegurar que sua Conta do IBM Cloud seja mantida em boas condições por políticas e diretrizes do IBM Cloud, desde que os dados estejam sujeitos a uma política de retenção. Consulte os termos do IBM Cloud Service para obter mais informações.
{:important}

## Immutable Object Storage e considerações para várias regulamentações
{: #immutable-regulation}

Ao usar o Immutable Object Storage, é responsabilidade do cliente verificar e assegurar se algum dos recursos discutidos pode ser alavancado para satisfazer e obedecer às regras principais em torno do armazenamento e retenção de registros eletrônicos e retenção que são geralmente governados por:

  * [Regra do Securities and Exchange Commission (SEC) 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64),
  * [Regra do Financial Industry Regulatory Authority (FINRA) 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957) e
  * [Regra do Commodity Futures Trading Commission (CFTC) 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

Para auxiliar clientes a tomar decisões informadas, a IBM contratou a Cohasset Associates Inc. para conduzir uma avaliação independente do recurso Immutable Object Storage da IBM. Revise o [relatório](https://www.ibm.com/downloads/cas/JBDNP0KV) da Cohasset Associates Inc. que fornece detalhes sobre a avaliação do recurso Immutable Object Storage do IBM Cloud Object Storage. 

### Auditoria de acesso e transações
{: #immutable-audit}
Os dados do log de acesso para o Immutable Object Storage para revisar as mudanças nos parâmetros de retenção, no período de retenção do objeto e no aplicativo de retenções legais estão disponíveis em uma base caso a caso, abrindo um chamado de atendimento ao cliente.

## Utilizando o Console
{: #immutable-console}

As políticas de retenção podem ser incluídas em depósitos vazios novos ou existentes e não podem ser removidas. Para um novo depósito, assegure-se de que você esteja criando o depósito em uma [região suportada](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability) e, em seguida, escolha a opção **Incluir política de retenção**. Para um depósito existente, assegure-se de que ele não tenha objetos e, em seguida, navegue para as definições de configuração e clique no botão **Criar política** abaixo da seção de política de retenção do depósito. Em qualquer um dos casos, configure os períodos mínimo, máximo e padrão de retenção.

## Usando a API de REST, as bibliotecas e os SDKs
{: #immutable-sdk}

Diversas APIs novas foram introduzidas nos SDKs do IBM COS para fornecer suporte para aplicativos que trabalham com políticas de retenção. Selecione uma linguagem (HTTP, Java, Javascript ou Python) na parte superior desta página para visualizar exemplos usando o SDK do COS apropriado. 

Observe que todos os exemplos de código supõem a existência de um objeto de cliente chamado `cos` que pode chamar os diferentes métodos. Para obter detalhes sobre como criar clientes, consulte os guias de SDK específicos.

Todos os valores de data usados para configurar os períodos de retenção são GMT. Um cabeçalho `Content-MD5` é necessário para assegurar a integridade de dados e é enviado automaticamente ao usar um SDK.
{:note}

### Incluir uma política de retenção em um depósito existente
{: #immutable-sdk-add-policy}
Essa implementação da operação `PUT` usa o parâmetro de consulta `protection` para configurar os parâmetros de retenção para um depósito existente. Essa operação permite configurar ou mudar os períodos mínimo, padrão e máximo de retenção. Essa operação também permite mudar o estado de proteção do depósito. 

Os objetos gravados em um depósito protegido não podem ser excluídos até que o período de proteção tenha expirado e todas as retenções legais no objeto sejam removidas. O valor de retenção padrão do depósito é fornecido para um objeto, a menos que um valor específico do objeto seja fornecido quando o objeto for criado. Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto. 

Os valores mínimo e máximo suportados para as configurações de período de retenção `MinimumRetention`, `DefaultRetention` e `MaximumRetention` são 0 dias e 365243 dias (1000 anos) respectivamente. 

Um cabeçalho `Content-MD5` é necessário. Essa operação não faz uso de parâmetros de consulta adicionais.

Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

{: http}

**Sintaxe**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Solicitação de exemplo**
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

**Resposta de exemplo**
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

### Verificar a política de retenção em um depósito
{: #immutable-sdk-get}

Essa implementação de uma operação GET busca os parâmetros de retenção para um depósito existente.
{: http}

**Sintaxe**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Solicitação de exemplo**
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

Se não houver configuração de proteção no depósito, o servidor responderá com status desativado no lugar.
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

### Fazer upload de um objeto em um depósito com política de retenção
{: #immutable-sdk-upload}

Esse aprimoramento da operação `PUT` inclui três novos cabeçalhos de solicitação: dois para especificar o período de retenção de diferentes formas e um para incluir uma única retenção legal no novo objeto. Novos erros são definidos para valores ilegais para os novos cabeçalhos e, se um objeto estiver sob retenção, quaisquer sobrescrições falharão.
{: http}

Os objetos em depósitos com política de retenção que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.

Um cabeçalho `Content-MD5` é necessário.
{: http}

Esses cabeçalhos se aplicam a solicitações de objeto POST e de upload de múltiplas partes também. Se você fizer upload de um objeto em múltiplas partes, cada parte requererá um cabeçalho `Content-MD5`.
{: http}

|Valor	| Tipo	| Descrição |
| --- | --- | --- | 
|`Retention-Period` | Número inteiro não negativo (segundos) | O período de retenção para armazenar o objeto em segundos. O objeto não pode ser sobrescrito nem excluído até que a quantia de tempo especificada no período de retenção tenha decorrido. Se esse campo e `Retention-Expiration-Date` forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período `DefaultRetention` do depósito será usado. Zero (`0`) é um valor legal que supõe que o período mínimo de retenção do depósito também é `0`. |
| `Retention-expiration-date` | Data (formato ISO 8601) | A data na qual será legal excluir ou modificar o objeto. É possível especificar somente isso ou o cabeçalho Retention-Period. Se ambos forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período DefaultRetention do depósito será usado. |
| `Retention-legal-hold-id` | sequência | Uma única retenção legal para aplicar ao objeto. Uma retenção legal é uma sequência longa de caracteres Y. O objeto não pode ser sobrescrito nem excluído até que todas as retenções legais associadas ao objeto sejam removidas. |

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

### Incluir ou remover uma retenção legal para ou de um objeto
{: #immutable-sdk-legal-hold}

Essa implementação da operação `POST` usa o parâmetro de consulta `legalHold` e os parâmetros de consulta `add` e `remove` para incluir ou remover uma única retenção legal de um objeto protegido em um depósito protegido.
{: http}

O objeto pode suportar 100 retenções legais:

*  Um identificador de retenção legal é uma sequência de comprimento máximo de 64 caracteres e um comprimento mínimo de 1 caractere. Os caracteres válidos são letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` e `.
* Se a adição de uma determinada retenção legal exceder 100 retenções legais totais no objeto, a nova retenção legal não será incluída e um erro `400` será retornado.
* Se um identificador for muito longo, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador contiver caracteres inválidos, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador já estiver em uso em um objeto, a retenção legal existente não será modificada e a resposta indicará que o identificador já estava em uso com um erro `409`.
* Se um objeto não tiver metadados de período de retenção, um erro `400` será retornado e a inclusão ou remoção de uma retenção legal não será permitida.

A presença de um cabeçalho de período de retenção é necessária, caso contrário, um erro `400` é retornado.
{: http}

O usuário que faz a inclusão ou remoção de uma retenção legal deve ter as permissões `Manager` para esse depósito.

Um cabeçalho `Content-MD5` é necessário. Essa operação não faz uso de elementos de carga útil específicos da operação.
{: http}

**Sintaxe**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Solicitação de exemplo**
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

**Resposta de exemplo**
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

### Ampliar o período de retenção de um objeto
{: #immutable-sdk-extend}

Essa implementação da operação `POST` usa o parâmetro de consulta `extendRetention` para ampliar o período de retenção de um objeto protegido em um depósito protegido.
{: http}

O período de retenção de um objeto pode somente ser ampliado. Ele não pode ser diminuído do valor configurado atualmente.

O valor de expansão de retenção é configurado de uma de três maneiras:

* tempo adicional do valor atual (`Additional-Retention-Period` ou método semelhante)
* novo período de extensão em segundos (`Extend-Retention-From-Current-Time` ou método semelhante)
* nova data de validade de retenção do objeto (`New-Retention-Expiration-Date` ou método semelhante)

O período de retenção atual armazenado nos metadados do objeto é aumentado pelo tempo adicional fornecido ou substituído pelo novo valor, dependendo do parâmetro que está configurado na solicitação `extendRetention`. Em todos os casos, o parâmetro de ampliação de retenção é verificado com relação ao período de retenção atual e o parâmetro ampliado será aceito somente se o período de retenção atualizado for maior que o período de retenção atual.

Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.

**Sintaxe**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**Solicitação de exemplo**
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

**Resposta de exemplo**
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

### Listar retenções legais em um objeto
{: #immutable-sdk-list-holds}

Essa implementação da operação `GET` usa o parâmetro de consulta `legalHold` para retornar a lista de retenções legais em um objeto e estado de retenção relacionado em um corpo de resposta XML.
{: http}

Essa operação retorna:

* Data de criação do objeto
* Período de retenção do objeto em segundos
* Data de expiração de retenção calculada com base no período e data de criação
* Lista de retenções legais
* Identificador de retenção legal
* Registro de data e hora em que a retenção legal foi aplicada

Se não houver retenções legais no objeto, um `LegalHoldSet` vazio será retornado.
Se não houver nenhum período de retenção especificado no objeto, um erro `404` será retornado.

**Sintaxe**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Solicitação de exemplo**
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

**Resposta de exemplo**
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
