---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-05"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:external: target="blank" .external}
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 
{:http: .ph data-hd-programlang='http'} 

# Exclua os dados antigos com regras de expiração
{: #expiry}

Uma regra de expiração exclui objetos após um período definido (a partir da data de criação do objeto). 

É possível configurar o ciclo de vida para objetos usando o console da web, a API de REST e as ferramentas de terceiros que são integradas ao {{site.data.keyword.cos_full_notm}}. 

* Uma regra de expiração pode ser incluída em um depósito novo ou existente.
* Uma regra de expiração existente pode ser modificada ou desativada.
* Uma regra de Expiração recém-incluída ou modificada aplica-se a todos os objetos novos e existentes no depósito.
* A inclusão ou modificação de políticas de ciclo de vida requer a função `Writer`. 
* Até 1000 regras de ciclo de vida (archive + expiração) podem ser definidas por depósito.
* Permita até 24 horas para que quaisquer mudanças nas regras de Expiração entrem em vigor.
* O escopo de cada regra de expiração pode ser limitado, definindo um filtro de prefixo opcional para ser aplicado a somente um subconjunto de objetos com nomes que correspondem ao prefixo.
* Uma regra de expiração sem um filtro de prefixo será aplicada a todos os objetos no depósito.
* O período de expiração para um objeto, especificado em número(s) de dias, é calculado a partir do momento em que o objeto foi criado e é encerrado à meia-noite UTC do dia seguinte. Por exemplo, se você tiver uma regra de expiração para um depósito para expirar um conjunto de objetos dez dias após a data de criação, um objeto que foi criado em 15 de abril de 2019 5h10 UTC expirará em 26 de abril de 2019 0h UTC. 
* As regras de expiração de cada depósito são avaliadas uma vez a cada 24 horas. Qualquer objeto que se qualifique para expiração (com base na data de expiração dos objetos) será enfileirado para exclusão. A exclusão de objetos expirados inicia no dia seguinte e geralmente leva menos de 24 horas. Não será faturado de você nenhum armazenamento associado de objetos depois que eles forem excluídos.

Os objetos que estão sujeitos a uma política de retenção do Immutable Object Storage de um depósito terão quaisquer ações de expiração adiadas até que a política de retenção não seja mais cumprida.
{: important}

## Atributos de regras de expiração
{: #expiry-rules-attributes}

Cada regra de expiração tem os atributos a seguir:

### ID
O ID de uma regra deve ser exclusivo dentro da configuração de ciclo de vida do depósito.

### Expiração
O bloco de expiração contém os detalhes que governam a exclusão automática de objetos. Isso pode ser uma data específica no futuro ou um período de tempo após os novos objetos serem gravados.

### Prefixo
Uma sequência opcional que será correspondida com o prefixo do nome do objeto no depósito. Uma regra com um prefixo será aplicada somente aos objetos que corresponderem. É possível usar múltiplas regras para diferentes ações de expiração para prefixos diferentes dentro do mesmo depósito. Por exemplo, dentro da mesma configuração de ciclo de vida, uma regra poderia excluir todos os objetos que iniciam com `logs/` após 30 dias e uma segunda regra poderia excluir objetos que iniciam com `video/` após 365 dias.  

### Status
Uma regra pode ser ativada ou desativada. Uma regra está ativa somente quando ativada.

## Configurações de ciclo de vida de amostra

Essa configuração expira quaisquer novos objetos após 30 dias.

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-30-days</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>30</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

Essa configuração exclui quaisquer objetos com o prefixo `foo/` em 1º de junho de 2020.

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-on-a-date</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Date>2020-06-01T00:00:00.000Z</Date>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

Também é possível combinar regras de transição e expiração. Essa configuração arquiva quaisquer objetos 90 dias após a criação e exclui quaisquer objetos com o prefixo `foo/` após 180 dias.

```xml
<LifecycleConfiguration>
  <Rule>
		<ID>archive-first</ID>
		<Filter />
		<Status>Enabled</Status>
    <Transition>
      <Days>90</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
	</Rule>
	<Rule>
		<ID>then-delete</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Days>180</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

## Utilizando o Console
{: #expiry-using-console}

Ao criar um novo depósito, marque a caixa **Incluir regra de expiração**. Em seguida, clique em **Incluir regra** para criar a nova regra de expiração. É possível incluir até cinco regras durante a criação do depósito e regras extras podem ser incluídas posteriormente.

Para um depósito existente, selecione **Configuração** no menu de navegação e clique em **Incluir regra** sob a seção _Regra de expiração_.

## Usando a API e SDKs
{: #expiry-using-api-sdks}

É possível gerenciar programaticamente as regras de expiração usando a API de REST ou os SDKs do IBM COS. Selecione o formato para os exemplos, selecionando uma categoria no alternador de contexto.

### Incluir uma regra de expiração em uma configuração de ciclo de vida de um depósito
{: #expiry-api-create}

**Referência da API de REST**
{: http}

Essa implementação da operação `PUT` usa o parâmetro de consulta `lifecycle` para definir as configurações de ciclo de vida para o depósito. Essa operação permite uma definição de política de ciclo de vida única para um depósito. A política é definida como um conjunto de regras que consistem nos parâmetros a seguir: `ID`, `Status`, `Filter` e `Expiration`.
{: http}
 
Os usuários do Cloud IAM devem ter a função `Writer` para remover uma política de ciclo de vida de um depósito.

Os usuários da infraestrutura clássica devem ter permissões `Owner` no depósito para remover uma política de ciclo de vida de um depósito.

Cabeçalho (Header)                    | Tipo   | Descrição
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | String | **Necessário**: o hash MD5 de 128 bits codificado em base64 da carga útil, que é usado como uma verificação de integridade para assegurar que a carga útil não tenha sido alterada em trânsito.
{: http}

O corpo da solicitação deve conter um bloco XML com o esquema a seguir:
{: http}

| Elemento                  | Tipo                 | Filhos                               | Antecessor               | Restrição                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Contêiner            | `Rule`                                 | Nenhum                   | Limite 1.                                                                                  |
| `Rule`                   | Contêiner            | `ID`, `Status`, `Filter`, `Expiration` | `LifecycleConfiguration` | Limite 1000.                                                                                  |
| `ID`                     | String               | Nenhum                                 | `Rule`                   | Deve consistir em (`a-z, `A-Z0-9`) e nos símbolos a seguir: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | String               | `Prefix`                               | `Rule`                   | Deve conter um elemento `Prefix`                                                            |
| `Prefix`                 | String               | Nenhum                                 | `Filter`                 | A regra se aplica a quaisquer objetos com chaves que correspondam a esse prefixo.                                                           |
| `Expiration`             | `Container`          | `Days` ou `Date`                       | `Rule`                   | Limite 1.                                                                                  |
| `Days`                   | Número inteiro não negativo | Nenhum                                 | `Expiration`             | Deve ser um valor maior que 0.                                                           |
| `Date`                   | data                 | Nenhum                                 | `Expiration`             | Deve estar no formato ISO 8601.                            |
{: http}

O corpo da solicitação deve conter um bloco XML com o esquema que é tratado na tabela (consulte Exemplo 1).
{: http}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Exemplo 1. Amostra XML do corpo da solicitação." caption-side="bottom"}
{: http}

**Sintaxe**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Exemplo 2. Observe o uso de barras e pontos neste exemplo de sintaxe." caption-side="bottom"}
{: codeblock}
{: http}

**Solicitação de exemplo**
{: http}

```yaml
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305

<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Exemplo 3. Amostras de cabeçalho da solicitação para criar uma configuração de ciclo de vida do objeto." caption-side="bottom"}
{: http}

**Amostra de código para uso com o SDK do COS NodeJS**
{: javascript}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);
var date = new Date('June 16, 2019 00:00:00');

var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* required */
        Expiration:
        {
          Date: date
        }
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

**Amostra de código para uso com o SDK do COS Python**
{: python}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada.
{: python}

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'Status': 'Enabled',
                'Filter': {},
                'Expiration':
                {
                    'Days': 123
                },
            },
        ]
    }
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

**Amostra de código para uso com o SDK do COS Java**
{: java}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada.
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Define a rule for exiring items in a bucket
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);
            
            // Add the rule to a new BucketLifecycleConfiguration.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));
            
            // Use the client to set the LifecycleConfiguration on the bucket.
            _cosClient.setBucketLifecycleConfiguration(bucketName, configuration);   
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration (new EndpointConfiguration (endpoint_url, location)) .withPathStyleAccessEnabled (true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
    }
```
{: codeblock}
{: java}
{: caption="Exemplo 1. Amostras de código mostrando a criação da configuração de ciclo de vida." caption-side="bottom"}

### Examinar a configuração de ciclo de vida de um depósito, incluindo a expiração
{: #expiry-api-view}

Essa implementação da operação `GET` usa o parâmetro de consulta `lifecycle` para examinar as configurações de ciclo de vida para o depósito. Uma resposta HTTP `404` será retornada se nenhuma configuração de ciclo de vida estiver presente.
{: http}

Os usuários do Cloud IAM devem ter a função `Reader` para remover uma política de ciclo de vida de um depósito.

Os usuários da infraestrutura clássica devem ter permissões `Read` no depósito para remover uma política de ciclo de vida de um depósito.

Cabeçalho (Header)                    | Tipo   | Descrição
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | String | **Necessário**: o hash MD5 de 128 bits codificado em base64 da carga útil, que é usado como uma verificação de integridade para assegurar que a carga útil não tenha sido alterada em trânsito.
{: http}

**Sintaxe**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Exemplo 5. Observe o uso de barras e pontos neste exemplo de sintaxe." caption-side="bottom"}
{: codeblock}
{: http}

**Solicitação de cabeçalho de exemplo**
{: http}

```yaml
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: caption="Exemplo 6. Amostras de cabeçalho da solicitação para criar uma configuração de ciclo de vida do objeto." caption-side="bottom"}
{: http}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada.

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').get_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada. 

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Use the client to read the configuration
            BucketLifecycleConfiguration config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            
            System.out.println(config.toString());
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration (new EndpointConfiguration (endpoint_url, location)) .withPathStyleAccessEnabled (true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }
```
{: codeblock}
{: java}
{: caption="Exemplo 2. Amostras de código mostrando a inspeção de configuração de ciclo de vida." caption-side="bottom"}

### Excluir uma configuração de ciclo de vida de um depósito, incluindo a expiração
{: #expiry-api-delete}

Essa implementação da operação `DELETE` usa o parâmetro de consulta `lifecycle` para examinar as configurações de ciclo de vida para o depósito. Todas as regras de ciclo de vida associadas ao depósito serão excluídas. As transições definidas pelas regras não ocorrerão mais para novos objetos. No entanto, as regras de transição existentes serão mantidas para objetos que já foram gravados no depósito antes que as regras fossem excluídas. As Regras de expiração não existirão mais. Uma resposta HTTP `404` será retornada se nenhuma configuração de ciclo de vida estiver presente.
{: http}

Os usuários do Cloud IAM devem ter a função `Writer` para remover uma política de ciclo de vida de um depósito.

Os usuários da infraestrutura clássica devem ter permissões `Owner` no depósito para remover uma política de ciclo de vida de um depósito.

**Sintaxe**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Exemplo 7. Observe o uso de barras e pontos neste exemplo de sintaxe." caption-side="bottom"}
{: codeblock}
{: http}

**Solicitação de cabeçalho de exemplo**
{: http}

```yaml
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="Exemplo 8. Amostras de cabeçalho da solicitação para criar uma configuração de ciclo de vida do objeto." caption-side="bottom"}
{: http}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada. 

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').delete_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

O uso dos SDKs do {{site.data.keyword.cos_full}} requer somente chamar as funções apropriadas com os parâmetros corretos e a configuração adequada.
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Delete the configuration.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);
            
            // Verify that the configuration has been deleted by attempting to retrieve it.
            config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            String s = (config == null) ? "Configuration has been deleted." : "Configuration still exists.";
            System.out.println(s);
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration (new EndpointConfiguration (endpoint_url, location)) .withPathStyleAccessEnabled (true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }

```
{: codeblock}
{: java}
{: caption="Exemplo 3. Amostras de código mostrando a exclusão da configuração de ciclo de vida." caption-side="bottom"}

## Próximas Etapas
{: #expiry-next-steps}

A expiração é apenas um de muitos conceitos de ciclo de vida disponíveis para o {{site.data.keyword.cos_full_notm}}.
Cada um dos conceitos que temos coberto nesta visão geral pode ser explorado ainda mais no
[{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/).

