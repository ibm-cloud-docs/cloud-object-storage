---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Lab
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup は、ユーザーがローカル・ファイル・システムの一部またはすべてを S3 API 互換のオブジェクト・ストレージ・システムにバックアップできるようにする柔軟なユーティリティーです。Windows 、MacOS、および Linux 用の Free バージョンと Professional バージョンがあり、{{site.data.keyword.cos_full}} を含む多数の一般的なクラウド・ストレージ・サービスがサポートされます。Cloudberry Backup は、[cloudberrylab.com](https://www.cloudberrylab.com/) からダウンロードできます。

Cloudberry Backup には、以下のような多くの便利な機能が含まれています。

* スケジューリング
* 増分およびブロック・レベルのバックアップ
* コマンド・ライン・インターフェース
* E メール通知
* 圧縮 (*Pro バージョンのみ*)

## Cloudberry Explorer
{: #cloudberry-explorer}

Cloudberry Lab の新製品は、{{site.data.keyword.cos_short}} の使い慣れたファイル管理ユーザー・インターフェースを提供します。[Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} も Free バージョンと Pro バージョンがあり、現在、Windows でのみ使用可能です。主な機能には、以下のものがあります。

* フォルダー/バケットの同期
* コマンド・ライン・インターフェース
* ACL 管理
* 容量レポート

Pro バージョンには、以下も含まれます。
* 検索 
* 暗号化/圧縮
* 再開可能なアップロード
* FTP/SFTP サポート

## Cloudberry と Object Storage の併用
{: #cloudberry-cos}

{{site.data.keyword.cos_short}} と連携するように Cloudberry 製品を構成する際に留意すべき重要な点は、以下のとおりです。

* オプションのリストから`「S3 Compatible」`を選択します
* 現在サポートされているのは [HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)のみです
* バケットごとに別個の接続が必要です
* 接続で指定されている`エンドポイント`が、選択したバケットのリージョンに一致していることを確認してください (*宛先にアクセスできないことで、バックアップが失敗してしまうため*)。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
