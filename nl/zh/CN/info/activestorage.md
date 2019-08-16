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

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} 是一个开放式源代码 Web 应用程序开发框架，用于将 Ruby 编程语言与 HTML、CSS 和 JavaScript 组合使用。它包含使用模型-视图-控制器 (MVC) 模式创建服务器端 Web 应用程序所需的一切内容。 

在 Rails 中，MVC 模式的模型（业务数据和逻辑层）由 [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window} 进行处理。它提供了[对象关系映射 (ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} 框架，用于将业务对象与关系数据库管理系统中的持久性存储器相连接。

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} 是一个内置框架，用于将云存储服务（如 {{site.data.keyword.cos_full}}）中的文件连接到 Active Record 对象。此外，还支持 Amazon S3、Google Cloud Storage 和 Microsoft Azure 以及基于磁盘的本地服务。

要开始使用 Active Storage，请从应用程序的主目录运行以下命令： 

```
bin/rails active_storage:install
bin/rails db:migrate
```

这将在应用程序的数据库中创建两个必需的表（`active_storage_blobs` 和 `active_storage_attachments`），用于管理 Active Record 文件附件。 

## 存储器配置
{: #ror-activestorage-config}

在 `config/storage.yml` 中声明 {{site.data.keyword.cos_short}} 服务：

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

将 [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} gem 添加到 Gemfile：

```
gem "aws-sdk-s3", require: false
```

您可以使用不同的端点和存储区来声明多个服务实例，以用于不同的环境。
{:tip}

*键值*
* `<bucket-region>` - 与存储区匹配的区域（例如，`us-south-standard`）。[此处](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint)提供了完整的列表。
* `<bucket-name>` - 存储区的名称。
* `<regional-endpoint>` - 用于访问存储区的端点（例如，`https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`）。[此处](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)提供了端点的完整列表。

语句 `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` 指示 Rails 从存储在 `~/.aws/credentials` 中的凭证数据中拉取访问密钥和私钥，格式如下：

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Rails 环境
{: #ror-activestorage-rails}

通过更新以下设置，将环境配置为使用 {{site.data.keyword.cos_short}} 服务：

```
config.active_storage.service = :ibmcos
```

更新每个环境的相应配置文件：

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## CORS 配置
{: #ror-activestorage-cors}

要启用 Rails 对存储区的访问权，必须创建类似于以下内容的跨源资源共享 (CORS) 配置：

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

此配置允许来自 `www.ibm.com` 的请求对存储区执行 `GET`、`PUT` 和 `POST` 请求。调整 `<AllowedOrigin>` 条目以满足应用程序的需求。 

此外，还需要允许 `x-amz-*` 和 `content-*` 头，以便 Rails 可正确地与存储区进行交互。在 [API 参考](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket)中提供了有关 CORS 的更多信息。
