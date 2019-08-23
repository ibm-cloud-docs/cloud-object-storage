---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# 開発者向けガイド
{: #dev-guide}

## 暗号設定の調整
{: #dev-guide-cipher}

{{site.data.keyword.cos_full}} は、転送中のデータを暗号化するためのさまざまな暗号設定をサポートしています。すべての暗号設定で同じレベルのパフォーマンスが得られるわけではありません。`TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`、`TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`、`TLS_RSA_WITH_AES_256_CBC_SHA`、`TLS_RSA_WITH_AES_128_CBC_SHA` のいずれかをネゴシエーションすることで、クライアントと {{site.data.keyword.cos_full_notm}} システムの間に TLS がない場合と同じレベルのパフォーマンスを得られることが分かっています。

## 複数パーツ・アップロードの使用
{: #dev-guide-multipart}

ラージ・オブジェクトを処理する場合、{{site.data.keyword.cos_full_notm}} にオブジェクトを書き込むときには複数パーツ・アップロード操作が推奨されます。単一オブジェクトのアップロードは一連のパーツとして実行でき、これらのパーツは任意の順序で並行して個別にアップロードできます。アップロードが完了した時点で、{{site.data.keyword.cos_short}} はすべてのパーツを単一オブジェクトとして表します。これには、ネットワーク割り込みが原因で大規模なアップロードが失敗することがない、時間の経過とともにアップロードを一時停止および再開できる、オブジェクトの作成中にそのオブジェクトをアップロードできるなど、多くの利点があります。

複数パーツ・アップロードは、5 MB を超えるオブジェクトに対してのみ使用可能です。50 GB より小さいオブジェクトの場合、最適なパフォーマンスを得るには、パーツのサイズを 20 MB から 100 MB にすることをお勧めします。それより大きいオブジェクトの場合、パーツのサイズを増やしてもパフォーマンスへの大きな影響はありません。

500 個を超えるパーツを使用すると、{{site.data.keyword.cos_short}} で非効率が生じるため、できるだけ回避してください。

処理に付随する複雑さが増すため、開発者には、複数パーツ・アップロード・サポートを提供している S3 API ライブラリーの使用をお勧めします。

不完全な複数パーツ・アップロードは、オブジェクトが削除されるか、`AbortIncompleteMultipartUpload` を使用して複数パーツ・アップロードが中止されるまで保持されます。不完全な複数パーツ・アップロードが中止されない場合、部分アップロードによってリソースの使用が継続されます。インターフェースの設計でこの点を考慮し、不完全な複数パーツ・アップロードをクリーンアップする必要があります。

## Software Development Kit の使用
{: #dev-guide-sdks}

公開されている S3 API SDK を使用する必要はありません。カスタム・ソフトウェアでも、API を利用して直接 {{site.data.keyword.cos_short}} と統合できます。ただし、公開されている S3 API ライブラリーを使用すると、認証/署名の生成、`5xx` エラーに対する自動再試行ロジック、事前署名 URL 生成などの利点があります。API を直接使用して一時的エラーを処理するようなソフトウェアを作成する場合は、注意が必要です。例えば、`503` エラーを受け取ったときに指数バックオフを使用して再試行するようなソフトウェアです。

## ページ編集
{: #dev-guide-pagination}

バケット内で多数のオブジェクトを扱う場合、Web アプリケーションでパフォーマンス低下が始まる可能性があります。アプリケーションの多くは、**ページ編集** (*大きなレコード・セットを個別のページに分割するプロセス*) と呼ばれる手法を採用しています。ほとんどすべての開発プラットフォームが、組み込み機能またはサード・パーティー・ライブラリーを使用してページ編集を実現するためのオブジェクトまたはメソッドを提供しています。

{{site.data.keyword.cos_short}} SDK は、指定されたバケット内のオブジェクトをリストするメソッドを介してページ編集のサポートを提供します。このメソッドには、大きな結果セットを分割しようとする際に非常に役立つ多数のパラメーターがあります。

### 基本的な使用法
{: #dev-guide-pagination-basics}
オブジェクト・リスト・メソッドの背後にある基本概念には、応答で返されるキーの最大数 (`MaxKeys`) の設定が関係します。応答には、使用可能な結果がさらにあるかどうかを示す `boolean` 値 (`IsTruncated`) と、`NextContinuationToken` という `string` 値も含まれます。後続の要求で継続トークンを設定すると、使用可能な結果がなくなるまで、次のオブジェクトのバッチが返されます。

#### 一般的なパラメーター
{: #dev-guide-pagination-params}

|パラメーター|説明|
|---|---|
|`ContinuationToken`|次のレコード・バッチを指定するトークンを設定します。|
|`MaxKeys`|応答に含めるキーの最大数を設定します。|
|`Prefix`|指定した接頭部で始まるキーに応答を制限します。|
|`StartAfter`|キーに基づいてオブジェクト・リストを開始する位置を設定します。|

### Java の使用
{: #dev-guide-pagination-java}

{{site.data.keyword.cos_full}} SDK for Java には、目的のサイズでオブジェクト・リストを返すことができる [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} メソッドがあります。[ここ](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2)で、完全なサンプル・コードを入手できます。

### Python の使用
{: #dev-guide-pagination-python}

{{site.data.keyword.cos_full}} SDK for Python には、目的のサイズでオブジェクト・リストを返すことができる [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} メソッドがあります。[ここ](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2)で、完全なサンプル・コードを入手できます。
