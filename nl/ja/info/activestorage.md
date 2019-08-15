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

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} は、Ruby プログラミング言語を HTML、CSS、および JavaScript と結合する、オープン・ソースの Web アプリケーション開発フレームワークです。そこには、モデル・ビュー・コントローラー (MVC) パターンを使用してサーバー・サイド Web アプリケーションを作成するために必要なものがすべて組み込まれています。 

Rails では、MVC パターンのモデル (ビジネス・データおよび論理層) が [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window} によって処理されます。これは、ビジネス・オブジェクトをリレーショナル・データベース管理システム内の永続ストレージに接続する[オブジェクト関連マッピング (ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} フレームワークを提供します。

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} は、{{site.data.keyword.cos_full}} などのクラウド・ストレージ・サービスから Active Record オブジェクトにファイルを添付するための組み込みフレームワークです。ローカル・ディスク・ベースのサービスに加え、Amazon S3、Google Cloud Storage、および Microsoft Azure もサポートされます。

Active Storage の使用を開始するには、アプリケーションのホーム・ディレクトリーから以下のコマンドを実行します。 

```
bin/rails active_storage:install
bin/rails db:migrate
```

これで、Active Record 添付ファイルを管理するために必要な 2 つの表 (`active_storage_blobs` と `active_storage_attachments`) がアプリケーションのデータベース内に作成されます。 

## ストレージ構成
{: #ror-activestorage-config}

`config/storage.yml` 内で {{site.data.keyword.cos_short}} サービスを宣言します。

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

[aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} gem を Gemfile に追加します。

```
gem "aws-sdk-s3", require: false
```

いろいろな環境で使用できるように、異なるエンドポイントとバケットを使用して複数のサービス・インスタンスを宣言できます。
{:tip}

*キー値*
* `<bucket-region>` - バケットに一致する地域 (例: `us-south-standard`)。[ここ](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint)で完全なリストを入手できます。
* `<bucket-name>` - バケットの名前
* `<regional-endpoint>` - バケットにアクセスするためのエンドポイント (例: `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`)。[ここ](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)でエンドポイントの完全なリストを入手できます。

ステートメント `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` は、Rails に対し、`~/.aws/credentials` に保管されている資格情報データからアクセス・キーと秘密鍵を以下のフォーマットでプルするように指示します。

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Rails 環境
{: #ror-activestorage-rails}

以下の設定を更新することで、{{site.data.keyword.cos_short}} サービスを使用するように環境を構成します。

```
config.active_storage.service = :ibmcos
```

環境ごとに対応する構成ファイルを更新します。

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## CORS 構成
{: #ror-activestorage-cors}

Rails がバケットにアクセスできるようにするには、以下のような Cross-Origin Resource Sharing (CORS) 構成を作成する必要があります。

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

この構成により、`www.ibm.com` からの要求がバケットに対する `GET`、`PUT`、および `POST` の各要求を実行できるようになります。`<AllowedOrigin>` エントリーは、ご使用のアプリケーションのニーズに合わせて調整してください。 

Rails がバケットと適切に対話できるようにするためには、`x-amz-*` ヘッダーと `content-*` ヘッダーも許可する必要があります。CORS について詳しくは、[API リファレンス](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket)を参照してください。
