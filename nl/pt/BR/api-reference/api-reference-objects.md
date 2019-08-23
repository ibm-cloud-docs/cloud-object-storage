---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: rest, s3, compatibility, api, objects

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

# Operações de objeto
{: #object-operations}

Essas operações leem, gravam e configuram os objetos contidos em um depósito.

Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Fazer upload de um objeto
{: #object-operations-put}

Um `PUT` fornecido em um caminho para um objeto faz upload do corpo da solicitação como um objeto. Todos os objetos transferidos por upload em um único encadeamento devem ser menores que 500 MB (os objetos [transferidos por upload em múltiplas partes](/docs/services/cloud-object-storage?topic=cloud-object-storage-large-objects) podem ser tão grandes quanto 10 TB).

**Nota**: informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
{:tip}

**Sintaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemplo de solicitação**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Solicitação de exemplo (HMAC)**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: {payload_hash}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
PUT /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

----

## Obter cabeçalhos de um objeto
{: #object-operations-head}

Um `HEAD` fornecido em um caminho para um objeto recupera os cabeçalhos desse objeto.

Observe que o valor `Etag` retornado para objetos criptografados usando SSE-KP **não** será o hash MD5 do objeto não criptografado original.
{:tip}

**Sintaxe**

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemplo de solicitação**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
HEAD /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
HEAD /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:32:44 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: da214d69-1999-4461-a130-81ba33c484a6
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:49:06 GMT
Content-Length: 11
```

----

## Fazer download de um objeto
{: #object-operations-get}

Um `GET` fornecido em um caminho para um objeto faz download do objeto.

Observe que o valor `Etag` retornado para objetos criptografados usando SSE-C/SSE-KP **não** será o hash MD5 do objeto não criptografado original.
{:tip}

**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name}/{object-name} # path style
GET https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### Cabeçalhos opcionais
{: #object-operations-get-headers}

Cabeçalho (Header) | Tipo | Descrição
--- | ---- | ------------
`range` | sequência | Retorna os bytes de um objeto dentro do intervalo especificado.

**Exemplo de solicitação**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
GET /apiary/worker-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
GET /apiary/worker-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:34:25 GMT
X-Clv-Request-Id: 116dcd6b-215d-4a81-bd30-30291fa38f93
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 116dcd6b-215d-4a81-bd30-30291fa38f93
ETag: "d34d8aada2996fc42e6948b926513907"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2016 17:46:53 GMT
Content-Length: 467

 Female bees that are not fortunate enough to be selected to be the 'queen'
 while they were still larvae become known as 'worker' bees. These bees lack
 the ability to reproduce and instead ensure that the hive functions smoothly,
 acting almost as a single organism in fulfilling their purpose.
```

----

## Excluir um objeto
{: #object-operations-delete}

Um `DELETE` fornecido em um caminho para um objeto exclui um objeto.

**Sintaxe**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name} # path style
DELETE https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemplo de solicitação**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
DELETE /apiary/soldier-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
DELETE /apiary/soldier-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 204 No Content
Date: Thu, 25 Aug 2016 17:44:57 GMT
X-Clv-Request-Id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 8ff4dc32-a6f0-447f-86cf-427b564d5855
```

----

## Excluindo múltiplos objetos
{: #object-operations-multidelete}

Um `POST` fornecido em um caminho para um depósito e parâmetros adequados excluirá um conjunto especificado de objetos. Um cabeçalho `Content-MD5` especificando o hash MD5 codificado em base64 do corpo da solicitação é necessário.

O cabeçalho `Content-MD5` necessário precisa ser a representação binária de um hash MD5 codificado em base64.

**Nota:** se um objeto especificado na solicitação não for localizado, o resultado será retornado como excluído. 

### Elementos opcionais
{: #object-operations-multidelete-options}

|Cabeçalho (Header)|Tipo|Descrição|
|---|---|---|
|`Quiet`|Booleano|Ative o modo silencioso para a solicitação.|

A solicitação pode conter um máximo de 1000 chaves que você deseja excluir. Embora isso seja muito útil na redução da sobrecarga por solicitação, fique atento ao excluir um grande número de chaves. Além disso, leve em conta os tamanhos dos objetos para assegurar o desempenho adequado.
{:tip}

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

**Sintaxe**

```bash
POST https://{endpoint}/{bucket-name}?delete= # path style
POST https://{bucket-name}.{endpoint}?delete= # virtual host style
```

**Exemplo de solicitação**

```http
POST /apiary?delete= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
POST /apiary?delete= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
POST /apiary?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&delete=&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Content-MD5: xj/vf7lD7vbIe/bqHTaLvg==
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Delete>
    <Object>
         <Key>surplus-bee</Key>
    </Object>
    <Object>
         <Key>unnecessary-bee</Key>
    </Object>
</Delete>
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 18:54:53 GMT
X-Clv-Request-Id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: a6232735-c3b7-4c13-a7b2-cd40c4728d51
Content-Type: application/xml
Content-Length: 207
```
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<DeleteResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Deleted>
         <Key>surplus-bee</Key>
    </Deleted>
    <Deleted>
         <Key>unnecessary-bee</Key>
    </Deleted>
</DeleteResult>
```

----

## Copiar um objeto
{: #object-operations-copy}

Um `PUT` fornecido em um caminho para um novo objeto cria uma nova cópia de outro objeto especificado pelo cabeçalho `x-amz-copy-source`. A menos que alterados de outra forma, os metadados permanecem os mesmos.

**Nota**: informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
{:tip}


**Nota**: a cópia de um item de um depósito ativado pelo *Key Protect* para um depósito de destino em outra região é restrita e resultará em um `500 - Internal Error`.
{:tip}

**Sintaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

### Cabeçalhos opcionais
{: #object-operations-copy-options}

Cabeçalho (Header) | Tipo | Descrição
--- | ---- | ------------
`x-amz-metadata-directive` | sequência (`COPY` ou `REPLACE`) | `REPLACE` sobrescreverá os metadados originais com novos metadados que são fornecidos.
`x-amz-copy-source-if-match` | sequência (`ETag`)| Cria uma cópia caso o `ETag` especificado corresponda ao objeto de origem.
`x-amz-copy-source-if-none-match` | sequência (`ETag`)| Cria uma cópia caso o `ETag` especificado seja diferente do objeto de origem.
`x-amz-copy-source-if-unmodified-since` | sequência (timestamp)| Cria uma cópia caso o objeto de origem não tenha sido modificado desde a data especificada. A data deve ser uma data de HTTP válida (por exemplo, `Wed, 30 Nov 2016 20:21:38 GMT`).
`x-amz-copy-source-if-modified-since` | sequência (timestamp)| Cria uma cópia caso o objeto de origem tenha sido modificado desde a data especificada. A data deve ser uma data de HTTP válida (por exemplo, `Wed, 30 Nov 2016 20:21:38 GMT`).

**Exemplo de solicitação**

Este exemplo básico usa o objeto `bee` do depósito `garden` e cria uma cópia no depósito `apiary` com a nova chave `wild-bee`.

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: Bearer {token}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
PUT /apiary/wild-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
PUT /apiary/wild-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
x-amz-copy-source: /garden/bee
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 30 Nov 2016 19:52:52 GMT
X-Clv-Request-Id: 72992a90-8f86-433f-b1a4-7b1b33714bed
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 72992a90-8f86-433f-b1a4-7b1b33714bed
ETag: "853aab195ce770b0dfb294a4e9467e62"
Content-Type: application/xml
Content-Length: 240
```

```xml
<CopyObjectResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <LastModified>2016-11-30T19:52:53.125Z</LastModified>
  <ETag>"853aab195ce770b0dfb294a4e9467e62"</ETag>
</CopyObjectResult>
```

----

## Verificar a configuração de CORS de um objeto
{: #object-operations-options}

Um `OPTIONS` fornecido em um caminho para um objeto juntamente com uma origem e um tipo de solicitação verifica se esse objeto está acessível nessa origem usando esse tipo de solicitação. Ao contrário de todas as outras solicitações, uma solicitação de OPTIONS não requer os cabeçalhos `authorization` ou `x-amx-date`.

**Sintaxe**

```bash
OPTIONS https://{endpoint}/{bucket-name}/{object-name} # path style
OPTIONS https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemplo de solicitação**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
OPTIONS /apiary/queen-bee HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
OPTIONS /apiary/queen-bee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&x-amz-signature={signature} HTTP/1.1
Access-Control-Request-Method: PUT
Origin: http://ibm.com
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Wed, 07 Dec 2016 16:23:14 GMT
X-Clv-Request-Id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.137
X-Clv-S3-Version: 2.5
x-amz-request-id: 9a2ae3e1-76dd-4eec-a8f2-1a7f60f63483
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: PUT
Access-Control-Allow-Credentials: true
Vary: Origin, Access-Control-Request-Headers, Access-Control-Allow-Methods
Content-Length: 0

```

----

## Fazendo upload de objetos em múltiplas partes
{: #object-operations-multipart}

Ao trabalhar com objetos maiores, as operações de upload de múltiplas partes são recomendadas para gravar objetos no {{site.data.keyword.cos_full}}. Um upload de um único objeto pode ser executado como um conjunto de partes e essas partes podem ser transferidas por upload independentemente em qualquer ordem e em paralelo. Após a conclusão do upload, o {{site.data.keyword.cos_short}} então apresenta todas as partes como um único objeto. Isso fornece muitos benefícios: as interrupções de rede não fazem com que grandes uploads falhem, os uploads podem ser pausados e reiniciados ao longo do tempo e os objetos podem ser transferidos por upload conforme eles estão sendo criados.

Os uploads de múltiplas partes estão disponíveis apenas para objetos maiores que 5 MB. Para objetos menores que 50 GB, um tamanho de parte de 20 MB a 100 MB é recomendado para desempenho ideal. Para objetos maiores, o tamanho da parte pode ser aumentado sem impacto significativo no desempenho. Os uploads de múltiplas partes são limitados a não mais que 10.000 partes de 5 GB cada.

O uso de mais de 500 partes leva a ineficiências no {{site.data.keyword.cos_short}} e deve ser evitado quando possível.
{:tip}

Devido à complexidade adicional envolvida, é recomendável que os desenvolvedores façam uso de uma biblioteca que forneça suporte ao upload de múltiplas partes.

Os uploads de múltiplas partes incompletos persistem até que o objeto seja excluído ou o upload de múltiplas partes seja interrompido com `AbortIncompleteMultipartUpload`. Se um upload de múltiplas partes incompleto não for interrompido, o upload parcial continuará a usar recursos. As interfaces devem ser projetadas tendo esse ponto em mente e limpar os uploads de múltiplas partes incompletos.
{:tip}

Há três fases para fazer upload de um objeto em múltiplas partes:

1. O upload é iniciado e um `UploadId` é criado.
2. As partes individuais são transferidas por upload especificando seus números de partes sequenciais e o `UploadId` para o objeto.
3. Quando todas as partes concluem o upload, o upload é concluído enviando uma solicitação com o `UploadId` e um bloco XML que lista cada número de parte e seu respectivo valor `Etag`.

## Iniciar um upload de múltiplas partes
{: #object-operations-multipart-initiate}

Um `POST` emitido para um objeto com o parâmetro de consulta `upload` cria um novo valor `UploadId`, que é, então, referenciado por cada parte do objeto que está sendo transferido por upload.

**Nota**: informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
{:tip}

**Sintaxe**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**Exemplo de solicitação**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploads=&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```

----

## Fazer upload de uma parte
{: #object-operations-multipart-put-part}

Uma solicitação `PUT` emitida para um objeto com parâmetros de consulta `partNumber` e `uploadId` fará upload de uma parte de um objeto. As partes podem ser transferidas por upload em série ou em paralelo, mas devem ser numeradas em ordem.

**Nota**: informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
{:tip}

**Sintaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**Exemplo de solicitação**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
x-amz-content-sha256: STREAMING-AWS4-HMAC-SHA256-PAYLOAD
Content-Encoding: aws-chunked
x-amz-decoded-content-length: 13374550
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
PUT /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```

----

## Listar partes
{: #object-operations-multipart-list}

Um `GET` fornecido em um caminho para um objeto de várias partes com um `UploadID` ativo especificado como um parâmetro de consulta retornará uma lista de todas as partes do objeto.


**Sintaxe**

```bash
GET https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId} # path style
GET https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId} # virtual host style
```

### Parâmetros de consulta
{: #object-operations-multipart-list-params}
Parâmetro | Necessário?| Tipo | Descrição
--- | ---- | ------------
`uploadId` | necessária | string | O ID de upload retornado ao inicializar um upload de múltiplas partes.
`max-parts` | Opcional | string | Padronizado como 1.000.
`part-number​-marker` | Opcional | string | Define onde a lista de partes será iniciada.

**Exemplo de solicitação**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
GET /farm/spaceship?uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
GET /farm/spaceship?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=01000162-3f46-6ab8-4b5f-f7060b310f37&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Mon, 19 Mar 2018 17:21:08 GMT
X-Clv-Request-Id: 6544044d-4f88-4bb6-9ee5-bfadf5023249
Server: Cleversafe/3.12.4.20
X-Clv-S3-Version: 2.5
Accept-Ranges: bytes
Content-Type: application/xml
Content-Length: 743
```

```xml
<ListPartsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>farm</Bucket>
  <Key>spaceship</Key>
  <UploadId>01000162-3f46-6ab8-4b5f-f7060b310f37</UploadId>
  <Initiator>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Initiator>
  <Owner>
    <ID>d6f04d83-6c4f-4a62-a165-696756d63903</ID>
    <DisplayName>d6f04d83-6c4f-4a62-a165-696756d63903</DisplayName>
  </Owner>
  <StorageClass>STANDARD</StorageClass>
  <MaxParts>1000</MaxParts>
  <IsTruncated>false</IsTruncated>
  <Part>
    <PartNumber>1</PartNumber>
    <LastModified>2018-03-19T17:20:35.482Z</LastModified>
    <ETag>"bb03cf4fa8603fe407a65ee1dba55265"</ETag>
    <Size>7128094</Size>
  </Part>
</ListPartsResult>
```

----

## Concluir um upload de múltiplas partes
{: #object-operations-multipart-complete}

Uma solicitação de `POST` emitida para um objeto com o parâmetro de consulta `uploadId` e o bloco XML apropriado no corpo concluirá um upload de múltiplas partes.

**Sintaxe**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Exemplo de solicitação**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
POST /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

## Interromper uploads de múltiplas partes incompletos
{: #object-operations-multipart-uploads}

Uma solicitação de `DELETE` emitida para um objeto com o parâmetro de consulta `uploadId` excluirá todas as partes não concluídas de um upload de múltiplas partes.

**Sintaxe**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**Exemplo de solicitação**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
DELETE /some-bucket/multipart-object-123?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&uploadId=0000015a-df89-51d0-2790-dee1ac994053&x-amz-signature={signature} HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Resposta de exemplo**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Restaurar temporariamente um objeto arquivado
{: #object-operations-archive-restore}

Uma solicitação de `POST` emitida para um objeto com o parâmetro de consulta `restore` para solicitar a restauração temporária de um objeto arquivado. Um cabeçalho `Content-MD5` é necessário como uma verificação de integridade para a carga útil.

Um objeto arquivado deve ser restaurado antes de fazer download ou modificar o objeto. O tempo de vida do objeto deve ser especificado, após o qual a cópia temporária do objeto será excluída.

Pode haver um atraso de até 15 horas antes que a cópia restaurada esteja disponível para acesso. Uma solicitação de HEAD poderá verificar se a cópia restaurada está disponível.

Para restaurar permanentemente o objeto, ele deve ser copiado para um depósito que não tenha uma configuração de ciclo de vida ativa.

**Sintaxe**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?restore # path style
POST https://{bucket-name}.{endpoint}/{object-name}?restore # virtual host style
```

**Elementos de carga útil**

O corpo da solicitação deve conter um bloco XML com o esquema a seguir:

|Elemento|Tipo|Filhos|Antecessor|Restrição|
|---|---|---|---|---|
|RestoreRequest|Contêiner|Days, GlacierJobParameters|Nenhum|Nenhuma|
|Days|Integer|Nenhum|RestoreRequest|Especificado o tempo de vida do objeto temporariamente restaurado. O número mínimo de dias em que uma cópia restaurada do objeto pode existir é 1. Depois que o período de restauração tiver decorrido, a cópia temporária do objeto será removida.|
|GlacierJobParameters|String|Camada|RestoreRequest|Nenhum|
|Camada|String|Nenhum|GlacierJobParameters|**Deve** ser configurado como `Bulk`.|

```xml
<RestoreRequest>
    <Days>{integer}</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Exemplo de solicitação**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: {authorization-string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (cabeçalhos do HMAC)**

```http
POST /apiary/queenbee?restore HTTP/1.1
Authorization: 'AWS4-HMAC-SHA256 Credential={access_key}/{datestamp}/{location}/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature={signature}'
x-amz-date: {timestamp}
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

**Solicitação de exemplo (URL pré-assinada do HMAC)**

```http
POST /apiary/queenbee?x-amz-algorithm=AWS4-HMAC-SHA256&x-amz-credential={access_key}%2F{datestamp}%2F{location}%2Fs3%2Faws4_request&x-amz-date={timestamp}&x-amz-expires=86400&x-zmz-signedheaders=host&restore&x-amz-signature={signature} HTTP/1.1
Content-MD5: rgRRGfd/OytcM7O5gIaQ== 
Content-Length: 305
Host: s3.us.cloud-object-storage.appdomain.cloud
```

```xml
<RestoreRequest>
    <Days>3</Days>
    <GlacierJobParameters>
        <Tier>Bulk</Tier>
    </GlacierJobParameters>
</RestoreRequest>
```

**Resposta de exemplo**

```http
HTTP/1.1 202 Accepted
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Atualizando os metadados
{: #object-operations-metadata}

Há duas maneiras de atualizar os metadados em um objeto existente:
* Uma solicitação `PUT` com os novos metadados e o conteúdo do objeto original
* Executando uma solicitação de `COPY` com os novos metadados especificando o objeto original como a origem de cópia

Todas as chaves de metadados devem ser prefixadas com `x-amz-meta-`
{: tip}

### Usando PUT para atualizar metadados
{: #object-operations-metadata-put}

A solicitação de `PUT` requer uma cópia do objeto existente, pois o conteúdo será sobrescrito. {: important}

**Sintaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemplo de solicitação**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-meta-key1: value1
x-amz-meta-key2: value2

Content-Length: 533

 The 'queen' bee is developed from larvae selected by worker bees and fed a
 substance referred to as 'royal jelly' to accelerate sexual maturity. After a
 short while the 'queen' is the mother of nearly every bee in the hive, and
 the colony will fight fiercely to protect her.

```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```

### Usando COPY para atualizar metadados
{: #object-operations-metadata-copy}

Para obter detalhes adicionais sobre a execução de uma solicitação de `COPY`, clique [aqui](#object-operations-copy)

**Sintaxe**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name} # path style
PUT https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```

**Exemplo de solicitação**

```http
PUT /apiary/queen-bee HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
x-amz-copy-source: /apiary/queen-bee
x-amz-metadata-directive: REPLACE
x-amz-meta-key1: value1
x-amz-meta-key2: value2
```

**Resposta de exemplo**

```http
HTTP/1.1 200 OK
Date: Thu, 25 Aug 2016 18:30:02 GMT
X-Clv-Request-Id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
Accept-Ranges: bytes
Server: Cleversafe/3.9.0.121
X-Clv-S3-Version: 2.5
x-amz-request-id: 9f0ca49a-ae13-4d2d-925b-117b157cf5c3
ETag: "3ca744fa96cb95e92081708887f63de5"
Content-Length: 0
```
