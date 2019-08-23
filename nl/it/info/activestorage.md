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

# Ruby on Rails/archiviazione attiva
{: #ror-activestorage}

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} è un framework open-source di sviluppo di applicazioni web che combina il linguaggio di programmazione Ruby con HTML, CSS e JavaScript. Include tutto quanto occorre per creare applicazioni web lato server utilizzando lo schema Modello-Vista-Controller (MVC, Model-View-Controller). 

In Rails, la parte Modello (livello di dati e di logica di business) dello schema MVC è gestito da [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window}. Fornisce il framework [ORM (Object-Relational Mapping)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} che connette gli oggetti di business all'archiviazione persistente nel sistema di gestione del database relazionale.

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} è un framework integrato per il collegamento di file dai servizi di archiviazione cloud come {{site.data.keyword.cos_full}} ad oggetti Active Record. Sono supportati anche Amazon S3, Google Cloud Storage e Microsoft Azure, così come i servizi basati su disco locali.

Per un'introduzione a Active Storage, esegui questi comandi dalla directory home della tua applicazione: 

```
bin/rails active_storage:install
bin/rails db:migrate
```

Ciò creerà le due tabelle richieste (`active_storage_blobs` e `active_storage_attachments`) nel database dell'applicazione per gestire gli allegati di file Active Record. 

## Configurazione dell'archiviazione
{: #ror-activestorage-config}

Dichiara il tuo servizio {{site.data.keyword.cos_short}} in `config/storage.yml`:

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

Aggiungi la gem [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} al tuo Gemfile:

```
gem "aws-sdk-s3", require: false
```

Puoi dichiarare più istanze del servizio con endpoint e bucket differenti da utilizzare in ambienti differenti.
{:tip}

*Valori chiave*
* `<bucket-region>` - la regione che corrisponde al tuo bucket (ossia `us-south-standard`). Un elenco completo è disponibile [qui](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint).
* `<bucket-name>` - il nome del tuo bucket
* `<regional-endpoint>` - l'endpoint per accedere al tuo bucket (ossia `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`)  L'elenco completo di endpoint è disponibile [qui](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

L'istruzione `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` indica a Rails di estrarre la chiave di accesso e la chiave segreta dai dati di credenziali memorizzati in `~/.aws/credentials` nel formato:

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Ambiente Rails
{: #ror-activestorage-rails}

Configura i tuoi ambienti per utilizzare il servizio {{site.data.keyword.cos_short}} aggiornando la seguente impostazione:

```
config.active_storage.service = :ibmcos
```

Aggiorna il file di configurazione corrispondente per ciascuno degli ambienti:

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## Configurazione CORS
{: #ror-activestorage-cors}

Per abilitare l'accesso Rails al tuo bucket, devi creare una configurazione CORS (Cross-Origin Resource Sharing) simile alla seguente:

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

Questa configurazione consentirà alle richieste da `www.ibm.com` di eseguire richieste `GET`, `PUT` e `POST` al tuo bucket.Regola la voce `<AllowedOrigin>` in modo adatto alle esigenze della tua applicazione. 

È anche necessario consentire intestazioni `x-amz-*` e `content-*` perché Rails interagisca correttamente con il tuo bucket. Ulteriori informazioni su CORS sono disponibili nella [Guida di riferimento API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket).
