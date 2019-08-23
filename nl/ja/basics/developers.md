---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# 開発者向けの情報
{: #gs-dev}
最初に、[{{site.data.keyword.cloud}} プラットフォーム CLI](https://cloud.ibm.com/docs/cli/index.html) および [IBM 開発者用ツール](https://cloud.ibm.com/docs/cloudnative/idt/index.html)がインストール済みであることを確認してください。

## {{site.data.keyword.cos_full_notm}} のインスタンスのプロビジョン
{: #gs-dev-provision}

  1. 最初に、API キーがあることを確認します。これは [IBM Cloud ID およびアクセス管理](https://cloud.ibm.com/iam/apikeys)から取得できます。
  2. CLI を使用して {{site.data.keyword.cloud_notm}} プラットフォームにログインします。API キーをファイルに保管したり、環境変数として設定したりすることも可能です。

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. 次に、{{site.data.keyword.cos_full_notm}} のインスタンスを、インスタンス名、ID、および使用したいプラン (ライトまたは標準) を指定して、プロビジョンします。これによって CRN を取得できます。アップグレードされたアカウントがある場合は、「`標準`」プランを指定してください。その他の場合は「`ライト`」と指定します。

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

[入門ガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)に、バケットおよびオブジェクトの作成、ユーザーの招待、およびポリシーの作成の基本的な手順が説明されています。基本的な 'curl' コマンドのリストは、[ここ](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)にあります。

{{site.data.keyword.cloud_notm}} CLI を使用したアプリケーションの作成、Kubernetes クラスターの管理などについて詳しくは、[資料](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli)を参照してください。


## API の使用
{: #gs-dev-api}

{{site.data.keyword.cos_short}} に保管されているデータを管理するために、[AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli) などの S3 API 互換ツールを、互換性のための [HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)と共に使用することができます。IAM トークンの扱いは比較的簡単であるため、基本的なストレージとの対話およびテストには `curl` が適しています。詳しい情報は [`curl` リファレンス](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)および [API リファレンス資料](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)にあります。

## ライブラリーおよび SDK の使用
{: #gs-dev-sdk}
[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go)、および [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) で使用できる IBM COS SDK があります。これらは、[IAM トークン・ベースの認証](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)および [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption) をサポートするように変更された、AWS S3 SDK から分岐したバージョンです。 

## IBM Cloud でのアプリケーションの構築
{: #gs-dev-apps}
{{site.data.keyword.cloud}} は、開発者がアプリケーションにとって適切なアーキテクチャーおよびデプロイメントのオプションを選択する際の柔軟性を提供します。コードの実行は、[ベアメタル](https://cloud.ibm.com/catalog/infrastructure/bare-metal)で、[仮想マシン](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group)で、[サーバーレス・フレームワーク](https://cloud.ibm.com/openwhisk)を使用して、[コンテナー](https://cloud.ibm.com/kubernetes/catalog/cluster)内で、または、[Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs) を使用して行うことができます。 

[Cloud Native Computing Foundation](https://www.cncf.io) では [Kubernetes](https://kubernetes.io) コンテナー・オーケストレーション・フレームワークは最近「育成」から「卒業」になり、{{site.data.keyword.cloud}} Kubernetes サービスのための基盤を形成しています。Kubernetes アプリケーションで永続ストレージ用にオブジェクト・ストレージを使用したい開発者は、以下のリンクで詳細を参照できます。

 * [ストレージ・ソリューションの選択](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [永続ストレージのオプション比較表](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [メイン COS ページ](/docs/containers?topic=containers-object_storage)
 * [COS のインストール](/docs/containers?topic=containers-object_storage#install_cos)
 * [COS サービス・インスタンスの作成](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [COS 秘密の作成](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [構成の決定](/docs/containers?topic=containers-object_storage#configure_cos)
 * [COS のプロビジョン](/docs/containers?topic=containers-object_storage#add_cos)
 * [情報のバックアップとリストア](/docs/containers?topic=containers-object_storage#backup_restore)
 * [ストレージ・クラスのリファレンス](/docs/containers?topic=containers-object_storage#storageclass_reference)


