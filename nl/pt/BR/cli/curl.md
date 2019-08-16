---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, curl, cli

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

# Usando `curl`
{: #curl}

Aqui está uma 'folha de dicas' de comandos básicos `curl` para a API de REST do {{site.data.keyword.cos_full}}. Detalhes adicionais podem ser localizados na referência da API para [depósitos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) ou [objetos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations).

O uso de `curl` supõe uma certa quantia de familiaridade com a linha de comandos e o armazenamento de objeto e que as informações necessárias foram obtidas de uma [credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), da [referência de terminais](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) ou do [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Se quaisquer termos ou variáveis não forem familiares, eles poderão ser localizados no [glossário](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

**Nota**: informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
{:tip}

## Solicitar um token do IAM
{: #curl-iam}

Há duas maneiras de gerar um token oauth do IAM para solicitações de autenticação: usando um comando `curl` com uma chave de API (descrita a seguir) ou por meio da linha de comandos usando a [CLI do IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli). 

### Solicite um token do IAM usando uma chave de API
{: #curl-token}

Primeiro, assegure-se de que tenha uma chave de API. Obtenha isso do [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys).

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## Obter seu ID da instância de recurso
{: #curl-instance-id}

Alguns dos comandos a seguir requerem um parâmetro `ibm-service-instance-id`. Para localizar esse valor, acesse a guia **Credenciais de serviço** de sua instância do Object Storage no console em nuvem. Crie uma nova credencial, se necessário, em seguida, use a lista suspensa *Visualizar credenciais* para ver o formato JSON. Use o valor de `resource_instance_id`. 

Para uso com as APIs do curl, é necessário somente o UUID que inicia após o último e único sinal de dois-pontos e termina antes do sinal de dois-pontos duplo final. Por exemplo, o ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` pode ser abreviado para `39d8d161-22c4-4b77-a856-f11db5130d7d`.
{:tip}

## Listar depósitos
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Incluir um depósito
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Incluir um depósito (classe de armazenamento)
{: #curl-add-bucket-class}

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

Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint).

## Criar um CORS de depósito
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

O cabeçalho `Content-MD5` precisa ser a representação binária de um hash MD5 codificado em base64.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Obter um CORS de depósito
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Excluir um CORS de depósito
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Listar objetos
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Obter cabeçalhos do depósito
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Excluir um depósito
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Fazer upload de um objeto
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## Obter cabeçalhos de um objeto
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Copiar um objeto
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## Verificar informações de CORS
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## Fazer download de um objeto
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Verificar a ACL do objeto
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Permitir acesso anônimo a um objeto
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Excluir um objeto
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Excluir múltiplos objetos
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

O cabeçalho `Content-MD5` precisa ser a representação binária de um hash MD5 codificado em base64.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Iniciar um upload de múltiplas partes
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Fazer upload de uma parte
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## Concluir um upload de múltiplas partes
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## Obter uploads de múltiplas partes incompletos
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Interromper uploads de múltiplas partes incompletos
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
