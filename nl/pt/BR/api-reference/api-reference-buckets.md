---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, buckets

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

# Operações de depósito
{: #compatibility-api-bucket-operations}


## Listar depósitos
{: #compatibility-api-list-buckets}

Uma solicitação de `GET` enviada para a raiz do terminal retorna uma lista de depósitos que estão associados à instância de serviço especificada. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Cabeçalho (Header)                    | Tipo   | Necessário? |  Descrição
--------------------------|--------|---| -----------------------------
`ibm-service-instance-id` | String | Sim | Lista os depósitos que foram criados nessa instância de serviço.

Parâmetro de consulta              | Valor   | Necessário? |  Descrição
--------------------------|--------|---| -----------------------------------------------------------
`extended` | Nenhum | Não | Fornece metadados `LocationConstraint` na listagem.

A listagem ampliada não é suportada nos SDKs nem na CLI.
{:note}

**Sintaxe**

```bash
GET https://{endpoint}/
```

**Exemplo de solicitação**

```http
GET / HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Resposta de exemplo**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

### Obtendo uma listagem ampliada
{: #compatibility-api-list-buckets-extended}

**Sintaxe**

```bash
GET https://{endpoint}/?extended
```

**Exemplo de solicitação**

```http
GET /?extended HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Resposta de exemplo**

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>{account-id}</ID>
        <DisplayName>{account-id}</DisplayName>
    </Owner>
    <IsTruncated>false</IsTruncated>
    <MaxKeys>1000</MaxKeys>
    <Prefix/>
    <Marker/>
    <Buckets>
        <Bucket>
            <Name>bucket-27200-lwx4cfvcue</Name>
            <CreationDate>2016-08-18T14:21:36.593Z</CreationDate>
            <LocationConstraint>us-south-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27590-drqmydpfdv</Name>
            <CreationDate>2016-08-18T14:22:32.366Z</CreationDate>
            <LocationConstraint>seo01-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-27852-290jtb0n2y</Name>
            <CreationDate>2016-08-18T14:23:03.141Z</CreationDate>
            <LocationConstraint>eu-standard</LocationConstraint>
        </Bucket>
        <Bucket>
            <Name>bucket-28731-k0o1gde2rm</Name>
            <CreationDate>2016-08-18T14:25:09.599Z</CreationDate>
            <LocationConstraint>us-cold</LocationConstraint>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

## Criar um depósito
{: #compatibility-api-new-bucket}

Uma solicitação de `PUT` enviada para a raiz do terminal seguida por uma sequência criará um depósito. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Os nomes dos depósitos devem ser globalmente exclusivos e compatíveis com o DNS; os nomes entre 3 e 63 caracteres de comprimento devem ser compostos de letras minúsculas, números e traços. Os nomes dos depósitos devem começar e terminar com uma letra minúscula ou um número. Os nomes dos depósitos que se assemelham a endereços IP não são permitidos. Essa operação não faz uso de parâmetros de consulta específicos da operação.

Os nomes dos depósitos devem ser exclusivos, pois todos os depósitos na nuvem pública compartilham um namespace global. Isso permite acesso a um depósito sem a necessidade de fornecer qualquer instância de serviço ou informações de conta. Também não é possível criar um depósito com um nome que se inicie com `cosv1-` ou `account-`, pois esses prefixos são reservados pelo sistema.
{:important}

Cabeçalho (Header)                                        | Tipo   | Descrição
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | String  |  Esse cabeçalho referencia a instância de serviço na qual o depósito será criado e para a qual o uso de dados será faturado.

**Nota**: informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio no nome do depósito ou do objeto.
{:tip}

**Sintaxe**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Exemplo de solicitação**

Este é um exemplo de criação de um novo depósito chamado 'images'.

```http
PUT /images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

----

## Criar um depósito com uma classe de armazenamento diferente
{: #compatibility-api-storage-class}

Para criar um depósito com uma classe de armazenamento diferente, envie um bloco XML especificando uma configuração de depósito com um `LocationConstraint` de `{provisioning code}` no corpo de uma solicitação de `PUT` para um terminal de depósito. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Observe que as [regras de nomenclatura](#compatibility-api-new-bucket) do depósito padrão se aplicam. Essa operação não faz uso de parâmetros de consulta específicos da operação.

Cabeçalho (Header)                                        | Tipo   | Descrição
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | String  |  Esse cabeçalho referencia a instância de serviço na qual o depósito será criado e para a qual o uso de dados será faturado.

**Sintaxe**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

**Exemplo de solicitação**

Este é um exemplo de criação de um novo depósito chamado 'vault-images'.

```http
PUT /vault-images HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
ibm-service-instance-id: {ibm-service-instance-id}
Content-Length: 110
```

```xml
<CreateBucketConfiguration>
  <LocationConstraint>us-vault</LocationConstraint>
</CreateBucketConfiguration>
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Fri, 17 Mar 2017 17:52:17 GMT
X-Clv-Request-Id: b6483b2c-24ae-488a-884c-db1a93b9a9a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
Content-Length: 0
```

----

## Criar um novo depósito com chaves de criptografia gerenciadas pelo Key Protect (SSE-KP)
{: #compatibility-api-key-protect}

Para criar um depósito no qual as chaves de criptografia são gerenciadas pelo Key Protect, é necessário ter acesso a uma instância de serviço do Key Protect ativa localizada no mesmo local que o novo depósito. Essa operação não faz uso de parâmetros de consulta específicos da operação.

Para obter mais informações sobre como usar o Key Protect para gerenciar suas chaves de criptografia, [consulte a documentação](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

Observe que o Key Protect **não** está disponível em uma configuração de Região cruzada e quaisquer depósitos do SSE-KP devem ser Regionais.
{:tip}

Cabeçalho (Header)                                        | Tipo   | Descrição
------------------------------------------------- | ------ | ----
`ibm-service-instance-id`  | String  |  Esse cabeçalho referencia a instância de serviço na qual o depósito será criado e para a qual o uso de dados será faturado.
`ibm-sse-kp-encryption-algorithm` | String | Esse cabeçalho é usado para especificar o algoritmo e o tamanho da chave a serem usados com a chave de criptografia armazenada usando o Key Protect. Esse valor deve ser configurado como a sequência `AES256`.
`ibm-sse-kp-customer-root-key-crn`  | String | Esse cabeçalho é usado para referenciar a chave raiz específica usada pelo Key Protect para criptografar esse depósito. Esse valor deve ser o CRN integral da chave raiz.

**Sintaxe**

```shell
PUT https://{endpoint}/{bucket-name} # path style
PUT https://{bucket-name}.{endpoint} # virtual host style
```

**Exemplo de solicitação**

Este é um exemplo de criação de um novo depósito chamado 'secure-files'.

```http
PUT /secure-files HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
ibm-service-instance-id: {ibm-service-instance-id}
ibm-sse-kp-encryption-algorithm: "AES256"
ibm-sse-kp-customer-root-key-crn: {customer-root-key-id}
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:45:25 GMT
X-Clv-Request-Id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: dca204eb-72b5-4e2a-a142-808d2a5c2a87
Content-Length: 0
```

---

## Recuperar os cabeçalhos de um depósito
{: #compatibility-api-head-bucket}

Um `HEAD` emitido para um depósito retornará os cabeçalhos para esse depósito.

As solicitações de `HEAD` não retornam um corpo e, portanto, não podem retornar mensagens de erro específicas, como `NoSuchBucket`, somente `NotFound`.
{:tip}

**Sintaxe**

```bash
HEAD https://{endpoint}/{bucket-name} # path style
HEAD https://{bucket-name}.{endpoint} # virtual host style
```

**Exemplo de solicitação**

Este é um exemplo de busca dos cabeçalhos para o depósito 'images'.

```http
HEAD /images HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
```

**Exemplo de solicitação**

As solicitações de `HEAD` em depósitos com a criptografia do Key Protect retornarão cabeçalhos extras.

```http
HEAD /secure-files HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization:Bearer {token}
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:46:35 GMT
X-Clv-Request-Id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 0c2832e3-3c51-4ea6-96a3-cd8482aca08a
Content-Length: 0
ibm-sse-kp-enabled: True
ibm-see-kp-crk-id: {customer-root-key-id}
```

----

## Listar objetos em um determinado depósito (Versão 2)
{: #compatibility-api-list-objects-v2}

Uma solicitação de `GET` endereçada a um depósito retorna uma lista de objetos, limitada a 1.000 por vez e retornada em ordem não lexicográfica. O valor `StorageClass` retornado na resposta é um valor padrão, pois as operações de classe de armazenamento não são implementadas no COS. Essa operação não faz uso de cabeçalhos ou de elementos de carga útil específicos da operação.

**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name}?list-type=2 # path style
GET https://{bucket-name}.{endpoint}?list-type=2 # virtual host style
```

### Parâmetros de consulta opcionais
{: #compatibility-api-list-objects-v2-params}
Nome | Tipo | Descrição
--- | ---- | ------------
`list-type` | String | Indica a versão 2 da API e o valor deve ser 2.
`prefix` | String | Restringe a resposta aos nomes de objetos que iniciam com `prefix`.
`delimiter` | String | Agrupa objetos entre o `prefix` e o `delimiter`.
`encoding-type` | String | Se caracteres Unicode que não são suportados por XML forem usados em um nome de objeto, esse parâmetro poderá ser configurado como `url` para codificar adequadamente a resposta.
`max-keys` | String | Restringe o número de objetos a serem exibidos na resposta. O padrão e o máximo são 1.000.
`fetch-owner` | String | A versão 2 da API não inclui as informações de `Owner` por padrão. Configure esse parâmetro como `true` se as informações de `Owner` forem desejadas na resposta.
`continuation-token` | String | Especifica o próximo conjunto de objetos a serem retornados quando sua resposta estiver truncada (o elemento `IsTruncated` retorna `true`).<br/><br/>Sua resposta inicial incluirá o elemento `NextContinuationToken`. Use esse token na próxima solicitação como o valor para `continuation-token`.
`start-after` | String | Retorna nomes de chave após um objeto chave específico.<br/><br/>*Esse parâmetro é válido somente em sua solicitação inicial.*  Se um parâmetro `continuation-token` estiver incluído em sua solicitação, esse parâmetro será ignorado.

**Solicitação de exemplo (simples com o IAM)**

Essa solicitação lista os objetos dentro do depósito "apiary".

```http
GET /apiary?list-type=2 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Resposta de exemplo (simples)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 814
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <KeyCount>3</KeyCount>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Solicitação de exemplo (parâmetro max-keys)**

Essa solicitação lista os objetos dentro do depósito "apiary" com uma chave máxima retornada configurada como 1.

```http
GET /apiary?list-type=2&max-keys=1 HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Resposta de exemplo (resposta truncada)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 598
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <NextContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

**Solicitação de exemplo (parâmetro continuation-token)**

Essa solicitação lista os objetos dentro do depósito "apiary" com um token de continuação especificado.

```http
GET /apiary?list-type=2&max-keys=1&continuation-token=1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Resposta de exemplo (resposta truncada, parâmetro continuation-token)**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 604
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <ContinuationToken>1dPe45g5uuxjyASPegLq80sQsZKL5OB2by4Iz_7YGR5NjiOENBPZXqvKJN6_PgKGVzZYTlws7qqdWaMklzb8HX2iDxxl72ane3rUFQrvNMeIih49MZ4APUjrAuYI83KxSMmfKHGZyKallFkD5N6PwKg</ContinuationToken>
  <NextContinuationToken>1a8j20CqowRrM4epIQ7fTBuyPZWZUeA8Epog16wYu9KhAPNoYkWQYhGURsIQbll1lP7c-OO-V5Vyzu6mogiakC4NSwlK4LyRDdHQgY-yPH4wMB76MfQR61VyxI4TJLxIWTPSZA0nmQQWcuV2mE4jiDA</NextContinuationToken>
  <KeyCount>1</KeyCount>
  <MaxKeys>1</MaxKeys>
  <Delimiter/>
  <IsTruncated>true</IsTruncated>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

### Listar objetos em um determinado depósito (descontinuado)
{: #compatibility-api-list-objects}

**Nota:** *essa API está incluída para compatibilidade com versões anteriores.* Consulte [Versão 2](#compatibility-api-list-objects-v2) para obter o método recomendado de recuperação de objetos em um depósito.

Uma solicitação de `GET` endereçada a um depósito retorna uma lista de objetos, limitada a 1.000 por vez e retornada em ordem não lexicográfica. O valor `StorageClass` retornado na resposta é um valor padrão, pois as operações de classe de armazenamento não são implementadas no COS. Essa operação não faz uso de cabeçalhos ou de elementos de carga útil específicos da operação.

**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name} # path style
GET https://{bucket-name}.{endpoint} # virtual host style
```

### Parâmetros de consulta opcionais
{: #compatibility-api-list-objects-params}

Nome | Tipo | Descrição
--- | ---- | ------------
`prefix` | String | Restringe a resposta aos nomes de objetos que iniciam com `prefix`.
`delimiter` | String | Agrupa objetos entre o `prefix` e o `delimiter`.
`encoding-type` | String | Se caracteres Unicode que não são suportados por XML forem usados em um nome de objeto, esse parâmetro poderá ser configurado como `url` para codificar adequadamente a resposta.
`max-keys` | String | Restringe o número de objetos a serem exibidos na resposta. O padrão e o máximo são 1.000.
`marker` | String | Especifica o objeto no qual a listagem deve ser iniciada, na ordem binária UTF-8.

**Exemplo de solicitação**

Essa solicitação lista os objetos dentro do depósito "apiary".

```http
GET /apiary HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 24 Aug 2016 17:36:24 GMT
X-Clv-Request-Id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.115
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f39ff2e-55d1-461b-a6f1-2d0b75138861
Content-Type: application/xml
Content-Length: 909
```

```xml
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>apiary</Name>
  <Prefix/>
  <Marker/>
  <MaxKeys>1000</MaxKeys>
  <Delimiter/>
  <IsTruncated>false</IsTruncated>
  <Contents>
    <Key>drone-bee</Key>
    <LastModified>2016-08-25T17:38:38.549Z</LastModified>
    <ETag>"0cbc6611f5540bd0809a388dc95a615b"</ETag>
    <Size>4</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>soldier-bee</Key>
    <LastModified>2016-08-25T17:49:06.006Z</LastModified>
    <ETag>"37d4c94839ee181a2224d6242176c4b5"</ETag>
    <Size>11</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
  <Contents>
    <Key>worker-bee</Key>
    <LastModified>2016-08-25T17:46:53.288Z</LastModified>
    <ETag>"d34d8aada2996fc42e6948b926513907"</ETag>
    <Size>467</Size>
    <Owner>
      <ID>{account-id}</ID>
      <DisplayName>{account-id}</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
  </Contents>
</ListBucketResult>
```

----

## Excluir um depósito

Um `DELETE` emitido para um depósito vazio exclui o depósito. Depois de excluir um depósito, o nome será mantido em reserva pelo sistema por 10 minutos, após o qual ele será liberado para reutilização. *Somente depósitos vazios podem ser excluídos.*

**Sintaxe**

```bash
DELETE https://{endpoint}/{bucket-name} # path style
DELETE https://{bucket-name}.{endpoint} # virtual host style
```

### Cabeçalhos opcionais

Nome | Tipo | Descrição
--- | ---- | ------------
`aspera-ak-max-tries` | Sequência | Especifica o número de vezes para tentar a operação de exclusão. O valor padrão é 2.


**Exemplo de solicitação**

```http
DELETE /apiary HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: Bearer {token}
```

O servidor responde com `204 No Content`.

Se um depósito não vazio for solicitado para exclusão, o servidor responderá com `409 Conflict`.

**Resposta de exemplo**

```xml
<Error>
  <Code>BucketNotEmpty</Code>
  <Message>The bucket you tried to delete is not empty.</Message>
  <Resource>/apiary/</Resource>
  <RequestId>9d2bbc00-2827-4210-b40a-8107863f4386</RequestId>
  <httpStatusCode>409</httpStatusCode>
</Error>
```

----

## Listar uploads de múltiplas partes cancelados/incompletos para um depósito

Um `GET` emitido para um depósito com os parâmetros adequados recupera informações sobre quaisquer uploads de múltiplas partes cancelados ou incompletos para um depósito.

**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name}?uploads= # path style
GET https://{bucket-name}.{endpoint}?uploads= # virtual host style
```

**Parâmetros**

Nome | Tipo | Descrição
--- | ---- | ------------
`prefix` | String | Restringe a resposta aos nomes de objetos que iniciam com `{prefix}`.
`delimiter` | String | Agrupa objetos entre o `prefix` e o `delimiter`.
`encoding-type` | String | Se caracteres Unicode que não são suportados por XML forem usados em um nome de objeto, esse parâmetro poderá ser configurado como `url` para codificar adequadamente a resposta.
`max-uploads` | inteiro | Restringe o número de objetos a serem exibidos na resposta. O padrão e o máximo são 1.000.
`key-marker` | String | Especifica o ponto no qual a listagem deve ser iniciada.
`upload-id-marker` | String | Ignorado caso `key-marker` não seja especificado, caso contrário, configura um ponto no qual iniciar a listagem de partes acima de `upload-id-marker`.

**Exemplo de solicitação**

Este é um exemplo de recuperação de todos os uploads de múltiplas partes cancelados e incompletos.

```http
GET /apiary?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo** (sem uploads de múltiplas partes em andamento)

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:22:27 GMT
X-Clv-Request-Id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 9fa96daa-9f37-42ee-ab79-0bcda049c671
Content-Type: application/xml
Content-Length: 374
```

```xml
<ListMultipartUploadsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>apiary</Bucket>
  <KeyMarker/>
  <UploadIdMarker/>
  <NextKeyMarker>multipart-object-123</NextKeyMarker>
  <NextUploadIdMarker>0000015a-df89-51d0-2790-dee1ac994053</NextUploadIdMarker>
  <MaxUploads>1000</MaxUploads>
  <IsTruncated>false</IsTruncated>
  <Upload>
    <Key>file</Key>
    <UploadId>0000015a-d92a-bc4a-c312-8c1c2a0e89db</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-16T22:09:01.002Z</Initiated>
  </Upload>
  <Upload>
    <Key>multipart-object-123</Key>
    <UploadId>0000015a-df89-51d0-2790-dee1ac994053</UploadId>
    <Initiator>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Initiator>
    <Owner>
      <ID>d4d11b981e6e489486a945d640d41c4d</ID>
      <DisplayName>d4d11b981e6e489486a945d640d41c4d</DisplayName>
    </Owner>
    <StorageClass>STANDARD</StorageClass>
    <Initiated>2017-03-18T03:50:02.960Z</Initiated>
  </Upload>
</ListMultipartUploadsResult>
```

----

## Listar qualquer configuração de compartilhamento de recurso de origem cruzada para um depósito

Um `GET` emitido para um depósito com os parâmetros adequados recupera informações sobre a configuração de compartilhamento de recurso de origem cruzada (CORS) para um depósito.

**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name}?cors= # path style
GET https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Exemplo de solicitação**

Este é um exemplo de listagem de uma configuração de CORS no depósito "apiary".

```http
GET /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo** 

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:20:30 GMT
X-Clv-Request-Id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 0b69bce1-8420-4f93-a04a-35d7542799e6
Content-Type: application/xml
Content-Length: 123
```

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <CORSRule>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
  </CORSRule>
</CORSConfiguration>
```

----

## Criar uma configuração de compartilhamento de recurso de origem cruzada para um depósito

Um `PUT` emitido para um depósito com os parâmetros adequados cria ou substitui uma configuração de compartilhamento de recurso de origem cruzada (CORS) para um depósito.

O cabeçalho `Content-MD5` necessário precisa ser a representação binária de um hash MD5 codificado em base64.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Sintaxe**

```bash
PUT https://{endpoint}/{bucket-name}?cors= # path style
PUT https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Exemplo de solicitação**

Este é um exemplo de inclusão de uma configuração de CORS que permite solicitações de `www.ibm.com` para emitir solicitações de `GET`, `PUT` e `POST` para o depósito.

```http
PUT /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 237
```

```xml
<CORSConfiguration>
  <CORSRule>
    <AllowedOrigin>http:www.ibm.com</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
  </CORSRule>
</CORSConfiguration>
```


**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2016 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.129
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```

----

## Exclua qualquer configuração de compartilhamento de recurso de origem cruzada para um depósito

Um `DELETE` emitido para um depósito com os parâmetros adequados cria ou substitui uma configuração de compartilhamento de recurso de origem cruzada (CORS) para um depósito.

**Sintaxe**

```bash
DELETE https://{endpoint}/{bucket-name}?cors= # path style
DELETE https://{bucket-name}.{endpoint}?cors= # virtual host style
```

**Exemplo de solicitação**

Este é um exemplo de exclusão de uma configuração de CORS para um depósito.

```http
DELETE /apiary?cors= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

O servidor responde com `204 No Content`.

----

## Listar a restrição de local para um depósito

Um `GET` emitido para um depósito com o parâmetro adequado recupera as informações de local para um depósito.

**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name}?location # path style
GET https://{bucket-name}.{endpoint}?location # virtual host style
```

**Exemplo de solicitação**

Este é um exemplo de recuperação do local do depósito "apiary".

```http
GET /apiary?location= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Tue, 12 Jun 2018 21:10:57 GMT
X-Clv-Request-Id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Accept-Ranges: bytes
Server: Cleversafe/3.13.3.57
X-Clv-S3-Version: 2.5
x-amz-request-id: 0e469546-3e43-4c6b-b814-5ad0db5b638f
Content-Type: application/xml
Content-Length: 161
```

```xml
<LocationConstraint xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  us-south-standard
</LocationConstraint>
```

----

## Criar uma configuração de ciclo de vida do depósito
{: #compatibility-api-create-bucket-lifecycle}

Uma operação `PUT` usa o parâmetro de consulta de ciclo de vida para definir as configurações de ciclo de vida para o depósito. Um cabeçalho `Content-MD5` é necessário como uma verificação de integridade para a carga útil.

**Sintaxe**

```bash
PUT https://{endpoint}/{bucket-name}?lifecycle # path style
PUT https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Elementos de carga útil**

O corpo da solicitação deve conter um bloco XML com o esquema a seguir:

|Elemento|Tipo|Filhos|Antecessor|Restrição|
|---|---|---|---|---|
|LifecycleConfiguration|Contêiner|Regra|Nenhum|Limite 1|
|Regra|Contêiner|ID, Status, Filter, Transition|LifecycleConfiguration|Limite 1|
|ID|String|Nenhum|Regra|**Deve** consistir em ``(a-z, A-Z, 0-9)`` e os símbolos a seguir:`` !`_ .*'()- ``|
|Filtro|String|Prefixo|Regra|**Deve** conter um elemento `Prefix`.|
|Prefixo|String|Nenhum|Filtro|**Deve** ser configurado como `<Prefix/>`.|
|Transition|Contêiner|Days, StorageClass|Regra|Limite 1.|
|Days|Número inteiro não negativo|Nenhum|Transition|**Deve** ser um valor maior que 0.|
|data|data|Nenhum|Transition|**Deve** estar no formato ISO 8601 e a data deve estar no futuro.|
|StorageClass|String|Nenhum|Transition|**Deve** ser configurado como GLACIER.|

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>{string}</ID>
        <Status>Enabled</Status>
        <Filter>
            <Prefix/>
        </Filter>
        <Transition>
            <Days>{integer}</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

**Exemplo de solicitação**

```http
PUT /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ== 
Content-Length: 305
```

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

O servidor responde com `200 OK`.

----

## Recuperar uma configuração de ciclo de vida do depósito

Uma operação `GET` usa o parâmetro de consulta de ciclo de vida para recuperar as configurações de ciclo de vida para o depósito.

**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name}?lifecycle # path style
GET https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Exemplo de solicitação**

```http
GET /apiary?lifecycle HTTP/1.1
Content-Type: text/plain
Host: s3.us.cloud-object-storage.appdomain.cloud
Authorization: {authorization-string}
```

**Resposta de exemplo**

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
            <Prefix/>
        </Filter>
        <Status>Enabled</Status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```

----

## Excluir a configuração de ciclo de vida de um depósito

Um `DELETE` emitido para um depósito com os parâmetros adequados remove quaisquer configurações de ciclo de vida de um depósito.

**Sintaxe**

```bash
DELETE https://{endpoint}/{bucket-name}?lifecycle # path style
DELETE https://{bucket-name}.{endpoint}?lifecycle # virtual host style
```

**Exemplo de solicitação**

```http
DELETE /apiary?lifecycle HTTP/1.1
Authorization: {authorization-string}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

O servidor responde com `204 No Content`.
