---

copyright:
  years: 2017, 2023
lastupdated: "2023-08-08"

keywords: object storage, ruby, activestorage, rails

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Ruby on Rails (Active Storage)
{: #ror-activestorage}

[Rails](https://guides.rubyonrails.org/getting_started.html){: external} is an open-source, web application development framework.
{: shortdesc}

Combining the Ruby programming language with HTML, CSS, and JavaScript, Rails includes everything needed to create server-side web applications using the Model-View-Controller (MVC) pattern.

In Rails, the Model (business data and logic layer) of the MVC pattern is handled by [Active Record](https://guides.rubyonrails.org/active_record_basics.html){: external}.

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){: external} is a built-in framework for attaching files from cloud storage services like {{site.data.keyword.cos_full}} to Active Record objects.

To get started with Active Storage, run the following commands from your application's home directory: 

```
bin/rails active_storage:install
bin/rails db:migrate
```

These commands create the two required tables (`active_storage_blobs` and `active_storage_attachments`) in the application's database to manage Active Record file attachments. 

## Storage Configuration
{: #ror-activestorage-config}

Declare your {{site.data.keyword.cos_short}} service in `config/storage.yml`:

```sh
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

Add the [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){: external} gem to your Gemfile:

```sh
gem "aws-sdk-s3", require: false
```

You can declare any number of service instances with different endpoints and buckets to be used in different environments.
{: tip}

*Key Values*
* `<bucket-region>` - Region that matches your bucket (that is, `us-south-standard`). Full list available [here](/docs/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint).
* `<bucket-name>` - Name of your bucket
* `<regional-endpoint>` - Endpoint to access your bucket (such as `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`). A full list of endpoints available [here](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

The statement `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` instructs Rails to pull the access key and secret key from the credentials data that is stored at `~/.aws/credentials` in the format:

```sh
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Rails Environment
{: #ror-activestorage-rails}

Configure your environments to use your {{site.data.keyword.cos_short}} service by updating the following setting:

```sh
config.active_storage.service = :ibmcos
```

Update the corresponding configuration file for each of the environments:

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## CORS Configuration
{: #ror-activestorage-cors}

To enable Rails access to your bucket, you must create a Cross-Origin Resource Sharing (CORS) configuration:

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

This configuration allows requests from `www.ibm.com` to run `GET`, `PUT`, and `POST` requests to your bucket. Adjust the `<AllowedOrigin>` entry to suit your application's needs.

Allowing `x-amz-*` and `content-*` headers is also required in order for Rails to properly interact with your bucket. More information about CORS is available in the [API Reference](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-add-cors).
