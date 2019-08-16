---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# Usando credenciais HMAC
{: #hmac}

A API do {{site.data.keyword.cos_full}} é uma API baseada em REST para objetos de leitura e composição. Ela usa o {{site.data.keyword.iamlong}} para autenticação/autorização e suporta um subconjunto da API S3 para facilitar a migração de aplicativos para o {{site.data.keyword.cloud_notm}}.

Além da autenticação baseada em token do IAM, também é possível [autenticar usando uma assinatura](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) criada por meio de um par de chaves secretas e de acesso. Isso é funcionalmente idêntico ao AWS Signature Versão 4 e as chaves HMAC fornecidas pelo IBM COS devem funcionar com a maioria das bibliotecas e ferramentas compatíveis com S3.

Os usuários podem criar um conjunto de credenciais HMAC ao criar uma [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) fornecendo o parâmetro de configuração `{"HMAC":true}` durante a criação da credencial. Aqui está um exemplo mostrando como usar a CLI do {{site.data.keyword.cos_full}} para criar uma chave de serviço com credenciais HMAC usando a função **Gravador** (outras funções podem estar disponíveis para sua conta e podem ser mais adequadas para suas necessidades). 

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="Exemplo 1. Usando o cURL para criar credenciais HMAC. Observe o uso das aspas simples e duplas." caption-side="bottom"}

Se você deseja armazenar os resultados da chave recém-gerada pelo comando no Exemplo 1, é possível anexar ` > file.skey` ao final do exemplo. Para os propósitos deste conjunto de instruções, é necessário somente localizar o título `cos_hmac_keys` com as chaves-filhas, `access_key_id` e `secret_access_key`&mdash; os dois campos que você precisa, conforme mostrado no Exemplo 2.

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="Exemplo 2. Chaves de nota ao gerar credenciais HMAC." caption-side="bottom"}

De interesse particular é a capacidade de configurar variáveis de ambiente (as instruções para as quais são específicas para o sistema operacional envolvido). Por exemplo, no Exemplo 3, um script `.bash_profile` contém `COS_HMAC_ACCESS_KEY_ID` e `COS_HMAC_SECRET_ACCESS_KEY` que são exportados no início de um shell e usados no desenvolvimento.

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="Exemplo 3. Uso de credenciais HMAC como variáveis de ambiente." caption-side="bottom"}

Após a criação da Credencial de serviço, a chave HMAC é incluída no campo `cos_hmac_keys`. Essas chaves HMAC são, então, associadas a um [ID de serviço](/docs/iam?topic=iam-serviceids#serviceids) e podem ser usadas para acessar quaisquer recursos ou operações permitidos pela função do ID de serviço. 

Observe que ao usar as credenciais HMAC para criar assinaturas para uso com as chamadas diretas de [API de REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api), os cabeçalhos adicionais são necessários:
1. Todas as solicitações devem ter um cabeçalho `x-amz-date` com a data no formato `%Y%m%dT%H%M%SZ`.
2. Qualquer solicitação que tenha uma carga útil (uploads de objetos, exclusão de múltiplos objetos, etc.) deve fornecer um cabeçalho `x-amz-content-sha256` com um hash SHA256 do conteúdo de carga útil.
3. As ACLs (diferentes de `public-read`) não são suportadas.

Nem todas as ferramentas compatíveis com S3 são suportadas atualmente. Algumas ferramentas tentam configurar ACLs diferentes de `public-read` na criação do depósito. A criação do depósito por meio dessas ferramentas falhará. Se uma solicitação de `PUT bucket` falhar com um erro de ACL não suportada, primeiro use o [Console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para criar o depósito e, em seguida, configure a ferramenta para ler e gravar objetos nesse depósito. As ferramentas que configuram ACLs em gravações de objetos não são suportadas atualmente.
{:tip}
