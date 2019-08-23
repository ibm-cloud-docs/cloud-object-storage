---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, curl, cli

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

# `curl` の使用
{: #curl}

以下に、{{site.data.keyword.cos_full}} REST API の基本的な `curl` コマンドの「虎の巻」を示します。追加の詳細については、[バケット](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)または[オブジェクト](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations)の API リファレンスを参照してください。

`curl` の使用では、コマンド・ラインおよびオブジェクト・ストレージにある程度精通しており、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)、[エンドポイント参照](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)、または[コンソール](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)から必要な情報を取得していることが前提となっています。分からない用語や変数については、[用語集](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)を参照してください。

**注**: 個人情報 (PII): バケットの作成やオブジェクトの追加を行うときは、名前、場所、その他の方法でユーザー (個人) を特定可能な情報を使用しないようにしてください。
{:tip}

## IAM トークンの要求
{: #curl-iam}

要求を認証するための IAM oauth トークンを生成する方法は 2 つあります。 API キーを指定して `curl` コマンド (以下で説明) を使用するか、[IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) を使用してコマンド・ラインから行います。 

### API キーを使用した IAM トークンの要求
{: #curl-token}

最初に、API キーがあることを確認します。これは、[{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys)(https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli)] から入手できます。

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## リソース・インスタンス ID の取得
{: #curl-instance-id}

以下のコマンドの一部には、`ibm-service-instance-id` パラメーターが必要です。この値を見つけるには、クラウド・コンソールでオブジェクト・ストレージ・インスタンスの**「サービス資格情報」**タブに移動します。必要に応じて新規資格情報を作成し、*「資格情報の表示」*ドロップダウンを使用して JSON 形式を表示します。`resource_instance_id` の値を使用します。 

curl API で使用する場合は、最後の単一コロンの後から始まり、最後の二重コロンの前に終わる UUID のみが必要です。例えば、ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` は、`39d8d161-22c4-4b77-a856-f11db5130d7d` に短縮できます。
{:tip}

## バケットのリスト
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## バケットの追加
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## バケットの追加 (ストレージ・クラス)
{: #curl-add-bucket-class}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}

`LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラス・ガイド](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint)を参照してください。

## バケット CORS の作成
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

`Content-MD5` ヘッダーは、base64 エンコードの MD5 ハッシュのバイナリー表記である必要があります。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## バケット CORS の取得
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## バケット CORS の削除
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## オブジェクトのリスト表示
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## バケット・ヘッダーの取得
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## バケットの削除
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## オブジェクトのアップロード
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## オブジェクトのヘッダーの取得
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## オブジェクトのコピー
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## CORS 情報の確認
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## オブジェクトのダウンロード
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## オブジェクトの ACL の確認
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## オブジェクトへの匿名アクセスの許可
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## オブジェクトの削除
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 複数のオブジェクトの削除
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

`Content-MD5` ヘッダーは、base64 エンコードの MD5 ハッシュのバイナリー表記である必要があります。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## マルチパート・アップロードの開始
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## パートのアップロード
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## マルチパート・アップロードの完了
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## 不完全なマルチパート・アップロードの取得
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 不完全なマルチパート・アップロードの中止
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
