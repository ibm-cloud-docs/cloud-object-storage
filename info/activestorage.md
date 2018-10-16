---

copyright:
  years: 2018
lastupdated: "2018-10-15"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Ruby on Rails/Active Storage

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} is an open-source, web application development framework that combines the Ruby programming language with HTML, CSS, and JavaScript.  It includes everything needed to create server-side web applications using the Model-View-Controller (MVC) pattern.  

In Rails, the Model (business data and logic layer) of the MVC pattern is handled by [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window}.  It provides the [Object-Relational Mapping (ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} framework that connects the business objects with persistent storage in the relational database management system.

## Active Storage

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} is a built-in framework for attaching files from cloud storage services like {{site.data.keyword.cos_full}} to Active Record objects.  Amazon S3, Google Cloud Storage, and Microsoft Azure are also supported as well as local disk-based services.

### Setup

To get started with Active Storage, run the following commands from your application's home directory: 

```
bin/rails active_storage:install
bin/rails db:migrate
```

This will create the two required tables (`active_storage_blobs` and `active_storage_attachments`) in the application's database to manage Active Record file attachments.  


### COS Storage Configuration

#### Storage Configuration
Declare your {{site.data.keyword.cos_short}} service in `config/storage.yml`:

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

You can declare multiple service instances to be used in different environments by specifying different endpoints and buckets.
{:tip}

The statement `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` instructs Rails to pull the access key and secret key from the credentials data stored at `~/.aws/credentials` in the format:

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

*Key Values*
* `<bucket-region>` - Region that match your bucket (i.e. `us-south-standard`).  Full list available [here](/docs/services/cloud-object-storage/basics/classes.html#how-do-i-create-a-bucket-with-a-different-storage-class-).
* `<bucket-name>` - Name of your bucket
* `<regional-endpoint>` - Endpoint to access your bucket (i.e. `https://s3.us-south.objectstorage.softlayer.net`)  Full list of endpoints available [here](/docs/services/cloud-object-storage/basics/endpoints.html#select-regions-and-endpoints).

#### Rails Environment

Set your environments to use your {{site.data.keyword.cos_short}} service by setting the `config/environments/development.rb`, `config/environments/test.rb`, `config/environments/production.rb` respectively.

```
config.active_storage.service = :ibmcos
```

### CORS Configuration

To enable Rails access to your bucket you must create a Cross-Origin Resource Sharing (CORS) configuration similar to below:

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

This configuration will allow requests from `www.ibm.com` to execute `GET`, `PUT`, and `POST` requests to your bucket.  Adjust the `<AllowedOrigin>` entry to suit your application's needs.  Allowing `x-amz-*` and `content-*` headers is also required in order for Rails to properly interact with your bucket.  More information about CORS is available in the [API Reference](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html#create-a-cross-origin-resource-sharing-configuration-for-a-bucket).