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

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window}는 Ruby 프로그래밍 언어를 HTML, CSS 및 JavaScript와 결합하는 오픈 소스 웹 애플리케이션 개발 프레임워크입니다. 여기에는 모델-뷰-컨트롤러(MVC) 패턴을 사용하여 서버 측 웹 애플리케이션을 작성하는 데 필요한 모든 항목이 포함되어 있습니다.  

Rails에서 MVC 패턴의 모델(비즈니스 데이터 및 로직 계층)은 [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window}에 의해 처리됩니다. 이는 관계형 데이터베이스 관리 시스템에서 비즈니스 오브젝트와 지속적 스토리지를 연결하는 [오브젝트 관계 맵핑(ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} 프레임워크를 제공합니다. 

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window}는 {{site.data.keyword.cos_full}}와 같은 클라우드 스토리지 서비스의 파일을 Active Record 오브젝트에 첨부하는 데 사용되는 기본 제공 프레임워크입니다. 로컬 디스크 기반 서비스 외에 Amazon S3, Google Cloud Storage 및 Microsoft Azure 또한 지원됩니다. 

Active Storage를 시작하려면 애플리케이션의 홈 디렉토리에서 다음 명령을 실행하십시오.  

```
bin/rails active_storage:install
bin/rails db:migrate
```

이 명령은 Active Record 파일 첨부를 관리하는 데 필요한 두 필수 테이블(`active_storage_blobs` 및 `active_storage_attachments`)을 애플리케이션의 데이터베이스에 작성합니다.  

## 스토리지 구성
{: #ror-activestorage-config}

`config/storage.yml`에서 {{site.data.keyword.cos_short}} 서비스를 선언하십시오. 

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

Gemfile에 [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} gem을 추가하십시오. 

```
gem "aws-sdk-s3", require: false
```

여러 엔드포인트 및 버킷을 사용하여 다양한 환경에서 사용할 여러 서비스 인스턴스를 선언할 수 있습니다.
{:tip}

*키 값*
* `<bucket-region>` - 버킷과 일치하는 지역입니다(예: `us-south-standard`). 전체 목록은 [여기](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint)에 있습니다. 
* `<bucket-name>` - 버킷의 이름입니다. 
* `<regional-endpoint>` - 버킷에 액세스하기 위한 엔드포인트입니다(예: `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`). 엔드포인트의 전체 목록은 [여기](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)에 있습니다. 

`<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>`문은 다음 형식으로 `~/.aws/credentials`에 저장된 인증 정보 데이터로부터 액세스 키 및 비밀 키를 가져오도록 Rails에 지시합니다. 

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Rails 환경
{: #ror-activestorage-rails}

다음 설정을 업데이트하여 {{site.data.keyword.cos_short}} 서비스를 사용하도록 환경을 구성하십시오. 

```
config.active_storage.service = :ibmcos
```

각 환경에 해당하는 구성 파일을 업데이트하십시오. 

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## CORS 구성
{: #ror-activestorage-cors}

Rails가 버킷에 액세스할 수 있도록 하려면 아래와 같은 CORS(Cross-Origin Resource Sharing) 구성을 작성해야 합니다. 

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

이 구성은 `www.ibm.com`으로부터의 요청이 버킷에 대해 `GET`, `PUT` 및 `POST` 요청을 실행할 수 있도록 합니다. 자신의 애플리케이션의 필요에 맞춰 `<AllowedOrigin>` 항목을 조정하십시오.  

Rails가 버킷과 올바르게 상호작용할 수 있도록 하려면 `x-amz-*` 및 `content-*` 헤더 허용 또한 필요합니다. CORS에 대한 자세한 정보는 [API 참조](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket)에 있습니다. 
