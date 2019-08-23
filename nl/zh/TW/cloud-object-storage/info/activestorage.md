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

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} 是一種使用開放程式碼的 Web 應用程式開發架構，結合 Ruby 程式設計語言與 HTML、CSS 及 JavaScript。它包括使用「模型視圖控制器 (MVC)」型樣來建立伺服器端 Web 應用程式所需的全部項目。 

在 Rails 中，MVC 型樣的模型（商業資料和邏輯層）是由[作用中記錄](https://guides.rubyonrails.org/active_record_basics.html){:new_window}所處理。它提供[物件相關對映 (ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} 架構，可連接關聯式資料庫管理系統中具有持續性儲存空間的商業物件。

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} 是一種內建架構，可將檔案從 {{site.data.keyword.cos_full}} 這類雲端儲存空間服務連接至「作用中記錄」物件。同時支援 Amazon S3、Google Cloud Storage 及 Microsoft Azure，以及本端磁碟型服務。

若要開始使用 Active Storage，請從應用程式的起始目錄中執行下列指令： 

```
bin/rails active_storage:install
bin/rails db:migrate
```

這會在應用程式的資料庫中建立兩個必要表格（`active_storage_blobs` 及 `active_storage_attachments`），以管理「作用中記錄」檔案連接。 

## 儲存空間配置
{: #ror-activestorage-config}

在 `config/storage.yml` 中宣告 {{site.data.keyword.cos_short}} 服務：

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

將 [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} Gem 新增至 Gemfile：

```
gem "aws-sdk-s3", require: false
```

您可以宣告多個服務實例，其具有要在不同環境中使用的不同端點及儲存區。
{:tip}

*鍵值*
* `<bucket-region>` - 符合您儲存區的地區（亦即 `us-south-standard`）。[這裡](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint)提供完整清單。
* `<bucket-name>` - 儲存區的名稱
* `<regional-endpoint>` - 要存取儲存區的端點（亦即 `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`）。[這裡](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)提供完整端點清單。

陳述式 `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` 會指示 Rails 從 `~/.aws/credentials` 中所儲存的認證資料中取回存取金鑰及密碼金鑰，格式為：

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Rails 環境
{: #ror-activestorage-rails}

更新下列設定，以配置環境來使用 {{site.data.keyword.cos_short}} 服務：

```
config.active_storage.service = :ibmcos
```

針對每個環境，更新對應的配置檔：

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## CORS 配置
{: #ror-activestorage-cors}

若要讓 Rails 存取您的儲存區，您必須建立與下面類似的「跨原點資源共用 (CORS)」配置：

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

此配置容許來自 `www.ibm.com` 的要求，將 `GET`、`PUT` 及 `POST` 要求執行至儲存區。調整 `<AllowedOrigin>` 項目，以符合應用程式的需求。 

同時需要容許 `x-amz-*` 及 `content-*` 標頭，Rails 才能適當地與您的儲存區互動。如需 CORS 的相關資訊，請參閱 [API 參考資料](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket)。
