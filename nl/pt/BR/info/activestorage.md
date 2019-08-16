---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: ruby, activestorage, rails, aws

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

# Ruby on Rails/Active Storage
{: #ror-activestorage}

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} é uma estrutura de desenvolvimento de aplicativo da web de software livre que combina a linguagem de programação Ruby com HTML, CSS e JavaScript. Ele inclui tudo o que é necessário para criar aplicativos da web do lado do servidor usando o padrão Model-View-Controller (MVC). 

No Rails, o Modelo (dados de negócios e camada lógica) do padrão MVC é manipulado pelo [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window}. Ele fornece a estrutura [Object-Relational Mapping (ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} que conecta os objetos de negócios ao armazenamento persistente no sistema de gerenciamento de banco de dados relacional.

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} é uma estrutura integrada para anexar arquivos de serviços de armazenamento em nuvem como o {{site.data.keyword.cos_full}} a objetos do Active Record. Amazon S3, Google Cloud Storage e Microsoft Azure também são suportados, bem como serviços locais baseados em disco.

Para começar com o Active Storage, execute os comandos a seguir por meio do diretório inicial do seu aplicativo: 

```
bin/rails active_storage:install
bin/rails db:migrate
```

Isso criará as duas tabelas necessárias (`active_storage_blobs` e `active_storage_attachments`) no banco de dados do aplicativo para gerenciar anexos de arquivo do Active Record. 

## Configuração de armazenamento
{: #ror-activestorage-config}

Declare seu serviço {{site.data.keyword.cos_short}} em `config/storage.yml`:

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

Inclua o gem [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} em seu Gemfile:

```
gem "aws-sdk-s3", require: false
```

É possível declarar múltiplas instâncias de serviço com diferentes terminais e depósitos para serem usados em ambientes diferentes.
{:tip}

*Valores da chave*
* `<bucket-region>` - A região que corresponde ao seu depósito (ou seja, `us-south-standard`). Lista integral disponível [aqui](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint).
* `<bucket-name>` - O nome de seu depósito
* `<regional-endpoint>` - O terminal para acessar seu depósito (ou seja, `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`). Lista integral de terminais disponível [aqui](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

A instrução `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` instrui o Rails a puxar a chave de acesso e a chave secreta dos dados de credenciais armazenados em `~/.aws/credentials` no formato:

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Ambiente do Rails
{: #ror-activestorage-rails}

Configure seus ambientes para usar o serviço {{site.data.keyword.cos_short}} atualizando a configuração a seguir:

```
config.active_storage.service = :ibmcos
```

Atualize o arquivo de configuração correspondente para cada um dos ambientes:

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## Configuração do CORS
{: #ror-activestorage-cors}

Para ativar o acesso do Rails ao seu depósito, deve-se criar uma configuração de Compartilhamento de Recurso de Origem Cruzada (CORS) semelhante à abaixo:

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <CORSRule>
        <AllowedMethod>GET</AllowedMethod>
        <AllowedMethod>PUT</AllowedMethod>
        <AllowedMethod>POST</AllowedMethod>
        <AllowedOrigin>www.ibm.com</AllowedOrigin>
        <AllowedHeader>x-amz-*</AllowedHeader>
        <AllowedHeader>content-*</AllowedHeader>
    </CORSRule>
</CORSConfiguration>
```

Essa configuração permitirá solicitações de `www.ibm.com` para executar solicitações de `GET`, `PUT` e `POST` em seu depósito. Ajuste a entrada `<AllowedOrigin>` para adequar as necessidades de seu aplicativo. 

Permitir cabeçalhos `x-amz-*` e `content-*` também é necessário para que o Rails interaja adequadamente com seu depósito. Mais informações sobre o CORS estão disponíveis na [Referência da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket).
