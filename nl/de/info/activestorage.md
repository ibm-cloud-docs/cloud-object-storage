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

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} ist ein Open-Source-Framework für die Entwicklung von Webanwendungen, das die Programmiersprache Ruby mit HTML, CSS und JavaScript kombiniert. Ruby enthält alles, was zur Erstellung serverseitiger Webanwendungen mit dem MVC-Muster (MVC: Model View Controller) erforderlich ist. 

Bei Rails wird das Modell (Schicht der Geschäftsdaten und Logik) des MVC-Musters von [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window} verarbeitet. Active Record stellt das [ORM-](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window}-Framework (Object-Relational Mapping, objektrelationale Abbildung) zur Verfügung, das die Geschäftsobjekte mit permanentem Speicher im relationalen Datenbankmanagementsystem verbindet.

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} ist ein integriertes Framework zum Anhängen von Dateien aus Cloudspeicherservices wie {{site.data.keyword.cos_full}} an Active Record-Objekte. Amazon S3, Google Cloud Storage und Microsoft Azure werden ebenso unterstützt wie lokale plattenbasierte Services.

Führen Sie zum Starten mit Active Storage die folgenden Befehle vom Ausgangsverzeichnis Ihrer Anwendung aus: 

```
bin/rails active_storage:install
bin/rails db:migrate
```

Dadurch werden die beiden erforderlichen Tabellen (`active_storage_blobs` und `active_storage_attachments`) in der Datenbank der Anwendung zur Verwaltung von Active Record-Dateianhängen erstellt. 

## Speicherkonfiguration
{: #ror-activestorage-config}

Deklarieren Sie Ihren {{site.data.keyword.cos_short}}-Service in `config/storage.yml`:

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucketregion>
  bucket: <bucketname>
  endpoint: <regionaler_endpunkt>
```

Fügen Sie das Gem [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} zu Ihrer Gemfile hinzu:

```
gem "aws-sdk-s3", require: false
```

Sie können mehrere Serviceinstanzen mit unterschiedlichen Endpunkten und Buckets zur Verwendung in unterschiedlichen Umgebungen deklarieren.
{:tip}

*Schlüsselwerte*
* `<bucket-region>` - Region, die mit Ihrem Bucket übereinstimmt (d.h. `us-south-standard`). Die vollständige Liste ist [hier](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint) verfügbar.
* `<bucket-name>` - Name Ihres Buckets.
* `<regional-endpoint>` - Endpunkt für den Zugriff auf Ihr Bucket (d. h. `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`). Eine vollständige Liste der Endpunkte ist [hier](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) verfügbar.

Die Anweisung `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` gibt an, dass Rails den Zugriffsschlüssel und den geheimen Schlüssel mit einer Pull-Operation aus den in `~/.aws/credentials` gespeicherten Berechtigungsnachweisdaten in folgendem Format extrahieren soll:

```
[default]
aws_access_key_id = <zugriffsschlüssel-ID>
aws_secret_access_key = <geheimer_zugriffsschlüssel>
```

## Rails-Umgebung
{: #ror-activestorage-rails}

Konfigurieren Sie Ihre Umgebungen so, dass sie Ihren {{site.data.keyword.cos_short}}-Service verwenden, indem Sie die folgende Einstellung aktualisieren:

```
config.active_storage.service = :ibmcos
```

Aktualisieren Sie die entsprechende Konfigurationsdatei für jede der Umgebungen:

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## CORS-Konfiguration
{: #ror-activestorage-cors}

Um Rails Zugriff auf Ihren Bereich zu ermöglichen, müssen Sie eine CORS-Konfiguration (CORS: Cross-Origin Resource Sharing) ähnlich der nachfolgend gezeigten erstellen:

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

Diese Konfiguration ermöglicht Anforderungen von `www.ibm.com` die Ausführung von Anforderungen vom Typ `GET`, `PUT` und `POST` für Ihr Bucket. Passen Sie den Eintrag `<AllowedOrigin>` an die Bedürfnisse Ihrer Anwendung an. 

Damit Rails ordnungsgemäß mit Ihrem Bucket interagieren kann, müssen die Header `x-amz-*` und `content-*` zugelassen sein. Weitere Informationen zu CORS enthält die [API-Referenz](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket).
