---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# データのアップロード
{: #upload}

バケットが編成されたら、いくつかのオブジェクトを追加します。ストレージの使用方法に応じて、異なる方法でデータをシステムに取り込むことができます。データ・サイエンティストが分析用に大容量ファイルをいくつか使用したり、システム管理者がデータベース・バックアップをローカル・ファイルと同期させる必要があったり、開発者が数百万のファイルを読み書きする必要のあるソフトウェアを作成するといったことが考えられます。これらの各シナリオには、データを取り込むためのさまざまな方法があることが役立ちます。

## コンソールの使用
{: #upload-console}

通常、Web ベースのコンソールを使用することは、最も一般的な {{site.data.keyword.cos_short}} の使用法ではありません。オブジェクトは 200 MB に制限され、ファイル名とキーは同一です。複数のオブジェクトを同時にアップロードできます。ブラウザーでマルチスレッドが許可されている場合、各オブジェクトは複数のパートを並行して使用することによってアップロードされます。もっと大きいオブジェクト・サイズおよびもっと高いパフォーマンス (ネットワーク要因による) が [Aspera 高速転送](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)によってサポートされます。

## 互換ツールの使用
{: #upload-tool}

ユーザーによっては、ストレージと対話するためにスタンドアロン・ユーティリティーを使用することが必要な場合があります。Cloud Object Storage API でサポートされるのは最も一般的な S3 API 操作のセットであるため、多くの S3 互換ツールも [HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)を使用して {{site.data.keyword.cos_short}} に接続できます。

例として、ファイル・エクスプローラー ([Cyberduck](https://cyberduck.io/)、[Transmit](https://panic.com/transmit/) など)、バックアップ・ユーティリティー ([Cloudberry](https://www.cloudberrylab.com/)、[Duplicati](https://www.duplicati.com/) など)、コマンド・ライン・ユーティリティー ([s3cmd](https://github.com/s3tools/s3cmd)、[Minio Client](https://github.com/minio/mc) など) があり、その他にも多くあります。

## API の使用
{: #upload-api}

Object Storage のほとんどのプログラマチック・アプリケーションは、SDK (例えば [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)、または [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)) または [Cloud Object Storage API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) を使用します。通常、オブジェクトは[複数のパート](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)でアップロードされます。その際、Transfer Manager クラスによって構成されたパート・サイズとパート番号が使用されます。
