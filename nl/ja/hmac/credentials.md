---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# HMAC 資格情報の使用
{: #hmac}

{{site.data.keyword.cos_full}} API は、オブジェクトを読み書きするための REST ベースの API です。この API では、認証/許可に {{site.data.keyword.iamlong}} が使用されるほか、アプリケーションを {{site.data.keyword.cloud_notm}} に簡単にマイグレーションできるように、S3 API のサブセットがサポートされます。

IAM トークン・ベースの認証に加えて、アクセス・キーと秘密鍵とのペアから作成される[署名を使用した認証](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)も可能です。これは、AWS 署名バージョン 4 と機能的に同等であり、IBM COS によって提供される HMAC 鍵は、S3 対応のほとんどのライブラリーおよびツールで機能します。

ユーザーは、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)の作成時に HMAC 資格情報のセットを作成できます。そのためには、資格情報の作成時に構成パラメーター `{"HMAC":true}` を指定します。以下は、{{site.data.keyword.cos_full}} CLI を使用してサービス・キーを作成する方法を示した例です。このサービス・キーには、**ライター**の役割を使用し (ご使用のアカウントには他にも使用可能な役割があり、その役割がニーズに最適である場合もあります)、HMAC 資格情報を指定しています。 

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="例 1. cURL を使用した HMAC 資格情報の作成。単一引用符と二重引用符の両方を使用していることに注意してください。" caption-side="bottom"}

例 1 のコマンドで生成されるキーの結果を保管する場合は、例の最後に ` > file.skey` を付加します。この命令セットの場合、例 2 に示すように、`cos_hmac_keys` ヘッダーと子キー、すなわち必要な 2 つのフィールドである `access_key_id` および `secret_access_key` の存在が確認できれば十分です。

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="例 2. HMAC 資格情報の生成時に注目すべきキー。" caption-side="bottom"}

特に重要なことは、環境変数 (関連するオペレーティング・システムに固有の指示) を設定できることです。例えば、例 3 では、`.bash_profile` スクリプトに、シェルの開始時にエクスポートされ、開発で使用される `COS_HMAC_ACCESS_KEY_ID` と `COS_HMAC_SECRET_ACCESS_KEY` が含まれています。

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="例 3. 環境変数としての HMAC 資格情報の使用。" caption-side="bottom"}

サービス資格情報が作成されると、HMAC 鍵が `cos_hmac_keys` フィールドに組み込まれます。その後、これらの HMAC 鍵は[サービス ID](/docs/iam?topic=iam-serviceids#serviceids) と関連付けられ、サービス ID の役割に従って許可されるリソースまたは操作にアクセスするために使用できるようになります。 

直接の [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) 呼び出しで使用する署名を作成するために HMAC 資格情報を使用する場合は、以下のように追加のヘッダーが必要な点に注意してください。
1. すべての要求に、`%Y%m%dT%H%M%SZ` 形式の日付が設定された `x-amz-date` ヘッダーが必要です。
2. ペイロードが含まれる要求 (オブジェクトのアップロード、複数オブジェクトの削除など) は、ペイロード・コンテンツの SHA256 ハッシュが設定された `x-amz-content-sha256` ヘッダーを提供する必要があります。
3. ACL (`public-read` 以外) はサポートされません。

現在、S3 対応のすべてのツールがサポートされているわけではありません。一部のツールは、バケット作成時に `public-read` 以外の ACL を設定しようとします。これらのツールを使用したバケット作成は失敗します。サポートされない ACL エラーで `PUT bucket` 要求が失敗した場合、まず、[コンソール](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)を使用してバケットを作成してから、そのバケットに対してオブジェクトの読み書きを行うようにツールを構成してください。オブジェクト書き込みに対する ACL を設定するツールは、現在サポートされていません。
{:tip}
