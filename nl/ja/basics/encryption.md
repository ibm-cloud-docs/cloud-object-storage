---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# 暗号化の管理
{: #encryption}

デフォルトでは、{{site.data.keyword.cos_full}} に保管されるすべてのオブジェクトは、[ランダムに生成される鍵と AONT (all-or-nothing-transform)](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security) を使用して暗号化されます。保存されたデータはこのデフォルトの暗号化モデルによって安全に保護されますが、ワークロードによっては、使用される暗号鍵を所有する必要があります。データの保管時に独自の暗号鍵を提供することによって鍵を手動で管理する (SSE-C) か、IBM Key Protect を使用するバケットを作成して暗号鍵を管理する (SSE-KP) ことができます。

## カスタマー提供の鍵によるサーバー・サイド暗号化 (SSE-C)
{: #encryption-sse-c}

SSE-C がオブジェクトに適用されます。カスタマーが管理する鍵を使用してオブジェクトまたはそのメタデータの読み取りまたは書き込みを行う要求は、必要な暗号化情報を HTTP 要求のヘッダーとして送信します。構文は S3 API と同じであり、SSE-C をサポートする S3 互換ライブラリーは {{site.data.keyword.cos_full}} に対して予期されるとおりに機能するはずです。

SSE-C ヘッダーを使用するすべての要求は SSL を使用して送信される必要があります。応答ヘッダー内の `ETag` 値はオブジェクトの MD5 ハッシュでは*なく*、ランダムに生成された 32 バイト 16 進数ストリングであることに注意してください。

ヘッダー | タイプ | 説明
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | ストリング | このヘッダーは、`x-amz-server-side-encryption-customer-key` ヘッダーに保管される暗号鍵で使用するアルゴリズムおよび鍵サイズを指定するために使用されます。この値は、ストリング `AES256` に設定する必要があります。
`x-amz-server-side-encryption-customer-key` | ストリング | このヘッダーは、サーバー・サイド暗号化プロセスで使用される AES 256 鍵の Base64 エンコードのバイト・ストリング表現をトランスポートするために使用されます。
`x-amz-server-side-encryption-customer-key-MD5` | ストリング | このヘッダーは、RFC 1321 に従う暗号鍵の Base64 エンコード 128 ビット MD5 ダイジェストをトランスポートするために使用されます。オブジェクト・ストアはこの値を使用して、`x-amz-server-side-encryption-customer-key` で渡された鍵がトランスポートおよびエンコード処理中に壊れなかったことを検証します。鍵が Base64 エンコードされる前に、鍵のダイジェストが計算される必要があります。


## {{site.data.keyword.keymanagementservicelong_notm}} によるサーバー・サイド暗号化 (SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}} は、{{site.data.keyword.cloud_notm}} サービスによって使用される暗号鍵を生成、管理、および破棄するための、一元化された鍵管理システム (KMS) です。{{site.data.keyword.cloud_notm}} カタログから {{site.data.keyword.keymanagementserviceshort}} のインスタンスを作成できます。

新規バケットを作成する地域に {{site.data.keyword.keymanagementserviceshort}} のインスタンスを作成したら、ルート・キーを作成し、そのキーの CRN をメモする必要があります。

バケットの暗号化を {{site.data.keyword.keymanagementserviceshort}} を使用して管理することを選択できるのは、作成時のみです。既存のバケットを {{site.data.keyword.keymanagementserviceshort}} を使用するように変更することはできません。
{:tip}

バケットの作成時に、追加のヘッダーを指定する必要があります。

{{site.data.keyword.keymanagementservicelong_notm}} について詳しくは、[資料を参照してください](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect)。

### SSE-KP の概要
{: #sse-kp-gs}

デフォルトでは、{{site.data.keyword.cos_full}} に保管されるすべてのオブジェクトは、ランダムに生成される複数の鍵と AONT (all-or-nothing-transform) を使用して暗号化されます。保存されたデータはこのデフォルトの暗号化モデルによって安全に保護されますが、ワークロードによっては、使用される暗号鍵を所有する必要があります。[{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) を使用して、鍵の作成、追加、および管理を行い、次に、バケットを暗号化するためにそれらの鍵を {{site.data.keyword.cos_full}} のインスタンスと関連付けることができます。

### 始める前に
{: #sse-kp-prereqs}

以下のものが必要です。
  * [{{site.data.keyword.cloud}} プラットフォームのアカウント](http://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}} のインスタンス](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * [{{site.data.keyword.keymanagementservicelong_notm}} のインスタンス](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * アップロードするファイルがローカル・コンピューター上にあること

### {{site.data.keyword.keymanagementserviceshort}} での鍵の作成または追加
{: #sse-kp-add-key}

{{site.data.keyword.keymanagementserviceshort}} のインスタンスに移動して、[鍵を生成または入力](/docs/services/key-protect?topic=key-protect-getting-started-tutorial)します。

### サービスへの許可の付与
{: #sse-kp}
IBM COS での使用を {{site.data.keyword.keymanagementserviceshort}} に許可します。

1. {{site.data.keyword.cloud_notm}} ダッシュボードを開きます。
2. メニュー・バーで、**「管理」** &gt; **「アカウント」** &gt; **「ユーザー」**をクリックします。
3. サイド・ナビゲーションで、**「ID およびアクセス」** &gt; **「許可」**をクリックします。
4. **「許可の作成」**をクリックします。
5. **「ソース・サービス」**メニューで**「Cloud Object Storage」**を選択します。
6. **「ソース・サービス・インスタンス」**メニューで、許可を付与するサービス・インスタンスを選択します。
7. **「ターゲット・サービス」**メニューで、**{{site.data.keyword.keymanagementservicelong_notm}}** を選択します。
8. **「ターゲット・サービス・インスタンス」**メニューで、許可を付与するサービス・インスタンスを選択します。
9. **リーダー**の役割を有効にします。
10. **「許可」**をクリックします。

### バケットの作成
{: #encryption-createbucket}

鍵が {{site.data.keyword.keymanagementserviceshort}} に存在し、IBM COS と共に使用するように Key Protect に許可を付与したら、鍵を新規バケットと関連付けます。

1. {{site.data.keyword.cos_short}} のインスタンスにナビゲートします。
2. **「バケットの作成」**をクリックします。
3. バケット名を入力し、**「地域」**回復力を選択し、ロケーションおよびストレージ・クラスを選択します。
4. 拡張構成で、**「Key Protect キーの追加」**を有効にします。
5. 関連付けられた Key Protect サービス・インスタンス、鍵、および鍵 ID を選択します。
6. **「作成」**をクリックします。

**「バケットおよびオブジェクト」**のリストの**「拡張」**の下でバケットに鍵のアイコンが表示されるようになります。これは、バケットで Key Protect 鍵が有効になっていることを示します。鍵の詳細を表示するには、バケットの右側にあるメニューをクリックし、**「Key Protect キーの表示」**をクリックします。

オブジェクトが SSE-KP を使用して暗号化されている場合に返される `Etag` 値は、元の暗号化されていないオブジェクトの実際の MD5 ハッシュで**ある**ことに注意してください。
{:tip}


## 鍵のローテーション
{: #encryption-rotate}

鍵のローテーションは、データ・ブリーチのリスク軽減において重要な役割を担います。鍵を定期的に変更することで、鍵が失われたり漏えいしたりした場合に起こり得るデータ損失を減らすことができます。鍵のローテーションの頻度は組織によって異なり、環境、暗号化データの量、データの種別、準拠法など、多くの変動要因に依存します。[米国連邦情報・技術局 (NIST)](https://www.nist.gov/topics/cryptography){:new_window} によって、鍵の適切な長さの定義および鍵を使用する期間に関する指針が提供されています。

### 手動での鍵のローテーション
{: #encryption-rotate-manual}

{{site.data.keyword.cos_short}} 用の鍵のローテーションを行うには、Key Protect を有効にした新規バケットを新しいルート・キーを使用して作成し、既存バケットからの内容をこの新規バケットにコピーする必要があります。

**注:** システムから鍵を削除すると、その鍵の内容と、その鍵を使用して暗号化されたすべてのデータが廃棄されます。いったん削除されると、元に戻すことはできず、データは永久に失われます。

1. [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial) サービスで新規ルート・キーを作成または追加します。
2. [新規バケットを作成](#encryption-createbucket)し、新規ルート・キーを追加します。
3. 元のバケットのすべてのオブジェクトを新規バケットにコピーします。
    1. このステップは、さまざまな方法で実行できます。
        1. [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) または [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli) を使用してコマンド・ラインから行う
        2.  (API) [/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object] を使用する
        3. [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)、または [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) で SDK を使用する
