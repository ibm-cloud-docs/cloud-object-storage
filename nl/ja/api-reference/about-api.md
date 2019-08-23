---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# {{site.data.keyword.cos_full_notm}} S3 API について
{: #compatibility-api}

{{site.data.keyword.cos_full}} API は、オブジェクトを読み書きするための REST ベースの API です。認証と許可のために {{site.data.keyword.iamlong}} が使用され、アプリケーションを {{site.data.keyword.cloud_notm}} に簡単にマイグレーションできるように S3 API のサブセットがサポートされています。

このリファレンス文書は継続的に改善されています。この API をアプリケーションで使用することに関して技術的な質問がある場合は、[StackOverflow](https://stackoverflow.com/) に質問を投稿してください。`ibm-cloud-platform` タグと `object-storage` タグの両方を追加してください。また、資料の改善のためにフィードバックをお寄せください。

{{site.data.keyword.iamshort}} トークンの扱いは比較的簡単であるため、基本的なストレージとの対話およびテストには `curl` が適しています。詳しくは、[`curl` 解説書](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl)を参照してください。

以下の表では、{{site.data.keyword.cos_full_notm}} API のすべての操作について説明します。詳しくは、[バケット](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)または[オブジェクトに関する API リファレンスのページ](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)を参照してください。


## バケット操作
{: #compatibility-api-bucket}

これらの操作は、バケットの作成、削除、情報の取得、および動作の制御を行います。

| バケット操作            | 注記                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` バケット          | アカウントに属しているすべてのバケットのリストを取得するために使用されます。   |
| `DELETE` バケット       | 空のバケットを削除します。                                                     |
| `DELETE` バケット CORS  | バケットに設定された CORS (クロス・オリジン・リソース共有) 構成をすべて削除します。  |
| `GET` バケット          | バケット内のオブジェクトをリストします。一度にリストできるのは 1,000 オブジェクトに制限されています。|
| `GET` バケット CORS     | バケットに設定された CORS 構成をすべて取得します。                             |
| `HEAD` バケット         | バケットのヘッダーを取得します。                                               |
| `GET` マルチパート・アップロード | 未完了またはキャンセル済みのマルチパート・アップロードをリストします。|
| `PUT` バケット          | バケットの命名には制約があります。アカウントは 100 バケットに制限されます。    |
| `PUT` バケット CORS     | バケットの CORS 構成を作成します。                                             |


## オブジェクト操作
{: #compatibility-api-object}

これらの操作は、オブジェクトの作成、削除、情報の取得、および動作の制御を行います。

| オブジェクト操作          | 注記                                                                                |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` オブジェクト       | バケットからオブジェクトを削除します。                                             |
| `DELETE` バッチ             | 1 つの操作でバケットから多数のオブジェクトを削除します。                           |
| `GET` オブジェクト          | バケットからオブジェクトを取得します。                                             |
| `HEAD` オブジェクト         | オブジェクトのヘッダーを取得します。                                               |
| `OPTIONS` オブジェクト      | CORS 構成を調べて、特定の要求を送信できるかどうかを確認します。                    |
| `PUT` オブジェクト          | バケットにオブジェクトを追加します。                                               |
| `PUT` オブジェクト (コピー) | オブジェクトのコピーを作成します。                                                 |
| マルチパート・アップロードの開始             | アップロードするパートのセットのアップロード ID を作成します。                     |
| パートのアップロード                         | アップロード ID と関連付けられた、オブジェクトのパートをアップロードします。       |
| パートのアップロード (コピー)                | アップロード ID と関連付けられた、既存オブジェクトのパートをアップロードします。   |
| マルチパート・アップロードの実行             | 1 つのアップロード ID と関連付けられた一連のパートから 1 つのオブジェクトを組み立てます。|
| マルチパート・アップロードのキャンセル       | アップロードをキャンセルし、アップロード ID と関連付けられた未処理パートを削除します。|
| パートのリスト                               | アップロード ID と関連付けられたパートのリストを返します。                         |


上記以外にもいくつかの操作 (タグ付けやバージョン管理など) が {{site.data.keyword.cos_short}} のプライベート・クラウド実装環境でサポートされていますが、現在のところパブリックまたは専用クラウドではサポートされていません。カスタム Object Storage ソリューションについて詳しくは、[ibm.com](https://www.ibm.com/cloud/object-storage) を参照してください。
