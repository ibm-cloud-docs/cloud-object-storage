---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# 遷移ルールを使用したコールド・データのアーカイブ
{: #archive}

{{site.data.keyword.cos_full}} Archive は、めったにアクセスされないデータ向けの[低コスト](https://www.ibm.com/cloud/object-storage)のオプションです。ストレージ層 (Standard、Vault、Cold Vault、および Flex) のいずれかから長期間オフライン・アーカイブに遷移させることによってデータを保管するか、またはオンライン Cold Vault オプションを使用することができます。
{: shortdesc}

IBM Cloud Object Storage に組み込まれている Web コンソール、REST API、およびサード・パーティー・ツールを使用して、オブジェクトをアーカイブできます。 

エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
{:tip}

## バケットのアーカイブ・ポリシーの追加または管理
{: #archive-add}

バケットのアーカイブ・ポリシーを作成または変更するときには以下の点を考慮してください。

* 新規または既存のバケットにアーカイブ・ポリシーをいつでも追加できます。 
* 既存のアーカイブ・ポリシーは変更することも無効にすることもできます。 
* 新しく追加されたか、変更されたアーカイブ・ポリシーは、アップロードされた新規オブジェクトに適用され、既存のオブジェクトには影響しません。

バケットにアップロードされた新規オブジェクトを即時にアーカイブするには、アーカイブ・ポリシーに 0 日を入力します。
{:tip}

Archive は特定の地域でのみ使用可能です。詳しくは、[統合サービス](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)を参照してください。
{:tip}

## アーカイブ済みオブジェクトのリストア
{: #archive-restore}

アーカイブされたオブジェクトにアクセスするには、それを元のストレージ層にリストアする必要があります。オブジェクトをリストアするときに、そのオブジェクトを使用可能な状態にしておきたい日数を指定できます。指定した期間が終わると、リストアされたコピーは削除されます。 

リストア処理には最大 12 時間かかる可能性があります。
{:tip}

アーカイブ済みオブジェクトの副状態は以下のとおりです。

* アーカイブ済み: 「アーカイブ済み」状態のオブジェクトは、バケットのアーカイブ・ポリシーに基づいて、オンラインのストレージ層 (Standard、Vault、Cold Vault、および Flex) からオフラインのアーカイブ層に移動済みです。
* リストア中: 「リストア中」状態のオブジェクトは、「アーカイブ済み」状態から元のオンラインのストレージ層へのコピーの生成が進行中です。
* リストア済み: 「リストア済み」状態のオブジェクトは、アーカイブ済みオブジェクトのコピーであり、指定された期間だけ元のオンラインのストレージ層にリストアされたものです。期間が終了すると、アーカイブ済みオブジェクトの保守中に、オブジェクトのコピーは削除されます。

## 制限
{: #archive-limitations}

アーカイブ・ポリシーは、`PUT Bucket Lifecycle Configuration` S3 API 操作のサブセットを使用して実装されます。 

サポートされる機能は以下のとおりです。
* オブジェクトがアーカイブ済み状態に遷移する将来の日付または日数を指定する。
* オブジェクトの[有効期限ルール](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry)を設定する。

サポートされない機能には以下のものがあります。
* バケット当たり複数の遷移ルール。
* 接頭部またはオブジェクト・キーを使用して、アーカイブするオブジェクトをフィルタリングすること。
* ストレージ・クラス間の層。

## REST API および SDK の使用
{: #archive-api} 

### バケットのライフサイクル構成の作成
{: #archive-api-create} 
{: http}

`PUT` 操作のこの実装は、`lifecycle` 照会パラメーターを使用してバケットのライフサイクル設定を設定します。この操作では、特定のバケットに対して単一のライフサイクル・ポリシー定義が可能です。ポリシーは、パラメーター `ID`、`Status`、および `Transition` から構成されるルールとして定義されます。
{: http}

遷移アクションは、バケットに書き込まれる将来のオブジェクトを、定義された期間が過ぎた後にアーカイブ済み状態にすることができます。バケットのライフサイクル・ポリシーの変更は、バケットに書き込まれた**新規オブジェクトにのみ適用されます**。

Cloud IAM ユーザーがバケットにライフサイクル・ポリシーを追加するには、最小限 `Writer` 役割を持っている必要があります。

クラシック・インフラストラクチャーのユーザーがバケットにライフサイクル・ポリシーを追加するには、所有者許可を持っていて、ストレージ・アカウントにバケットを作成できなければなりません。

この操作では、追加の操作固有の照会パラメーターは使用されません。
{: http}

ヘッダー                    | タイプ   | 説明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | ストリング | **必須**: base64 でエンコードされた、ペイロードの 128 ビット MD5 ハッシュ。ペイロードが転送中に変更されなかったことを確認するための保全性検査として使用されます。
{: http}

要求の本体には、以下のスキーマの XML ブロックが含まれている必要があります。
{: http}

| 要素                  | タイプ                 | 子                               | 上位                     | 制約                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | コンテナー            | `Rule`                                 | なし                     |限度 1。|
| `Rule`                   | コンテナー            | `ID`、`Status`、`Filter`、`Transition` | `LifecycleConfiguration` |限度 1。|
| `ID`                     | ストリング               | なし                                   | `Rule`                   | (`a-z,`A-Z0-9`) と記号 `!` `_` `.` `*` `'` `(` `)` `-` で構成される必要があります|
| `Filter`                 | ストリング               | `Prefix`                               | `Rule`                   | `Prefix` 要素を含んでいる必要があります|
| `Prefix`                 | ストリング               | なし                                   | `Filter`                 | `<Prefix/>` に設定される**必要があります**。|
| `Transition`             | `Container`          | `Days`、`StorageClass`                 | `Rule`                   |限度 1。|
| `Days`                   | 負でない整数 | なし                                   | `Transition`             | 0 より大きい値である必要があります。|
| `Date`                   | 日付                 | なし                                   | `Transistion`            | ISO 8601 形式である必要があり、将来の日付でなければなりません。|
| `StorageClass`           | ストリング               | なし                                   | `Transition`             | `GLACIER` に設定される**必要があります**。|
{: http}

__構文__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="例 1. この構文例では、スラッシュとドットの使用に注意してください。" caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
		<Filter>
			<Prefix/>
		</Filter>
		<Transition>
			<Days>{integer}</Days>
			<StorageClass>GLACIER</StorageClass>
		</Transition>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="例 2. オブジェクト・ライフサイクル構成を作成するための XML の例。" caption-side="bottom"}

__例__
{: http}

_サンプル要求_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="例 3. オブジェクト・ライフサイクル構成を作成するための要求ヘッダーの例。" caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="例 4. PUT 要求本体の XML の例。" caption-side="bottom"}

_サンプル応答_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="例 5. 応答ヘッダー。" caption-side="bottom"}

---

### バケットのライフサイクル構成の取得
{: #archive-api-retrieve} 
{: http}

`GET` 操作のこの実装は、`lifecycle` 照会パラメーターを使用してバケットのライフサイクル設定を取得します。 

Cloud IAM ユーザーがバケットのライフサイクルを取得するには、最小限 `Reader` 役割を持っている必要があります。

クラシック・インフラストラクチャーのユーザーがバケットのライフサイクル・ポリシーを取得するには、最小限、バケットに対する `Read` 許可を持っている必要があります。

この操作では、追加の操作固有のヘッダー、照会パラメーター、およびペイロードは使用されません。

__構文__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="例 6. GET 要求の構文のバリエーション。" caption-side="bottom"}

__例__ 
{: http}

_サンプル要求_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="例 7. 構成を取得するための要求ヘッダーの例。" caption-side="bottom"}

_サンプル応答_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="例 8. GET 要求からの応答ヘッダーの例。" caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="例 9. 応答本体の XML の例。" caption-side="bottom"}

---

### バケットのライフサイクル構成の削除
{: #archive-api-delete} {: http}

`DELETE` 操作のこの実装は、`lifecycle` 照会パラメーターを使用してバケットのすべてのライフサイクル設定を削除します。ルールで定義された遷移は、新規オブジェクトに対しては行われなくなります。 

**注:** 既存の遷移ルールは、ルールが削除される前に既にバケットに書き込まれたオブジェクトのために保持されます。

Cloud IAM ユーザーがバケットからライフサイクル・ポリシーを削除するには、最小限 `Writer` 役割を持っている必要があります。

クラシック・インフラストラクチャーのユーザーがバケットからライフサイクル・ポリシーを削除するには、バケットに対する「`所有者`」許可を持っている必要があります。

この操作では、追加の操作固有のヘッダー、照会パラメーター、およびペイロードは使用されません。

__構文__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="例 10. この構文例では、スラッシュとドットの使用に注意してください。" caption-side="bottom"}

__例__
{: http}

_サンプル要求_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="例 11. DELETE HTTP 動詞の要求ヘッダーの例。" caption-side="bottom"}

_サンプル応答_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="例 12.  DELETE 要求からのサンプル応答。" caption-side="bottom"}

---

### アーカイブ済みオブジェクトの一時的なリストア 
{: #archive-api-restore} {: http}

`POST` 操作のこの実装は、`restore` 照会パラメーターを使用して、アーカイブ済みオブジェクトの一時的なリストアを要求します。ユーザーは、まずアーカイブ済みオブジェクトをリストアした後で、オブジェクトをダウンロードまたは変更する必要があります。ユーザーは、オブジェクトをリストアするときに、オブジェクトの一時コピーが削除されるまでの期間を指定する必要があります。オブジェクトは、バケットのストレージ・クラスを保持します。

リストアされたコピーがアクセス可能になるまでに、最大 12 時間の遅延が生じることがあります。リストアされたコピーが使用可能かどうかを `HEAD` 要求で検査できます。 

オブジェクトを永続的にリストアするには、ユーザーは、リストアされるオブジェクトを、アクティブなライフサイクル構成がないバケットにコピーする必要があります。

Cloud IAM ユーザーがオブジェクトをリストアするには、最小限 `Writer` 役割を持っている必要があります。

クラシック・インフラストラクチャーのユーザーがオブジェクトをリストアするには、最小限、バケットに対する `Write` 許可と、オブジェクトに対する `Read` 許可を持っている必要があります。

この操作では、追加の操作固有の照会パラメーターは使用されません。

ヘッダー                    | タイプ   | 説明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | ストリング | **必須**: base64 でエンコードされた、ペイロードの 128 ビット MD5 ハッシュ。ペイロードが転送中に変更されなかったことを確認するための保全性検査として使用されます。

要求の本体には、以下のスキーマの XML ブロックが含まれている必要があります。

要素                  | タイプ      | 子                               | 上位                     |制約
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | コンテナー | `Days`、`GlacierJobParameters`    | なし       | なし
`Days`                   | 整数 | なし | `RestoreRequest` |一時的にリストアされたオブジェクトの存続期間を指定します。オブジェクトのリストアされたコピーが存在できる最小日数は 1 です。リストア期間を過ぎると、オブジェクトの一時コピーは削除されます。
`GlacierJobParameters` | ストリング | `Tier` | `RestoreRequest` | なし
`Tier` | ストリング | なし | `GlacierJobParameters` | `Bulk` に設定される**必要があります**。

正常な応答では、オブジェクトがアーカイブ済み状態の場合は `202` が返され、オブジェクトが既にリストア済み状態の場合は `200` が返されます。既にリストア済み状態のオブジェクトをリストアする新しい要求が受信されると、リストアされたオブジェクトの有効期限は `Days` 要素によって更新されます。

__構文__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="例 13. この構文例では、スラッシュとドットの使用に注意してください。" caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="例 14.  要求本体の XML のモデル。" caption-side="bottom"}

__例__
{: http}

_サンプル要求_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="例 15. オブジェクトをリストアする要求ヘッダーの例。" caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="例 16. オブジェクトをリストアする要求本体の例。" caption-side="bottom"}

_サンプル応答_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="例 17. オブジェクトのリストアに対する応答 (`HTTP 202`)." caption-side="bottom"}

---

### オブジェクトのヘッダーの取得
{: http}
{: #archive-api-head}

オブジェクトへのパスが指定された `HEAD` は、そのオブジェクトのヘッダーを取得します。この操作では操作固有の照会パラメーターおよびペイロード要素は使用されません。

__構文__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="例 18. エンドポイント定義のバリエーション。" caption-side="bottom"}


__アーカイブ済みオブジェクトの応答ヘッダー__
{: http}

ヘッダー | タイプ | 説明
--- | ---- | ------------
`x-amz-restore` | ストリング |オブジェクトがリストア済みの場合またはリストアが進行中の場合に含まれます。オブジェクトがリストア済みの場合、一時コピーの有効期限日付も返されます。
`x-amz-storage-class` | ストリング |アーカイブ済みまたは一時的にリストア済みの場合に `GLACIER` を返します。
`x-ibm-archive-transition-time` | 日付 |アーカイブ層へのオブジェクトの遷移がスケジュールされている日時を返します。
`x-ibm-transition` | ストリング |オブジェクトに遷移メタデータがある場合に含まれ、遷移の層および元の時刻を返します。
`x-ibm-restored-copy-storage-class` | ストリング |オブジェクトが `RestoreInProgress` 状態または `Restored` 状態の場合に含まれ、バケットのストレージ・クラスを返します。


_サンプル要求_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="例 19. 要求ヘッダーを示す例。" caption-side="bottom"}

_サンプル応答_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="例 20. 応答ヘッダーを示す例。" caption-side="bottom"}


### バケットのライフサイクル構成の作成
{: #archive-node-create} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'STRING_VALUE',
        Filter: '', /* required */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* required if Days not specified */
            Days: 0, /* required if Date not specified */
            StorageClass: 'GLACIER' /* required */
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="例 21. ライフサイクル構成の作成を示す例。" caption-side="bottom"}

### バケットのライフサイクル構成の取得
{: #archive-node-retrieve} {: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="例 22. ライフサイクル・メタデータの取得を示す例。" caption-side="bottom"}

### バケットのライフサイクル構成の削除
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="例 23. バケットのライフサイクル構成を削除する方法を示す例。" caption-side="bottom"}

### アーカイブ済みオブジェクトの一時的なリストア 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
  ContentMD5: 'STRING_VALUE', /* required */
  RestoreRequest: {
   Days: 1, /* days until copy expires */
   GlacierJobParameters: {
     Tier: Bulk /* required */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="例 24. アーカイブ済みオブジェクトのリストアで使用されるコード。" caption-side="bottom"}

### オブジェクトのヘッダーの取得
{: #archive-node-head} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // an error occurred
  else   
    console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="例 25. オブジェクト・ヘッダーの取得を示す例。" caption-side="bottom"}


### バケットのライフサイクル構成の作成
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="例 26. オブジェクト構成の作成に使用されるメソッド。" caption-side="bottom"}

### バケットのライフサイクル構成の取得
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="例 27. オブジェクト構成の取得に使用されるメソッド。" caption-side="bottom"}

### バケットのライフサイクル構成の削除
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="例 28. オブジェクト構成の削除に使用されるメソッド。" caption-side="bottom"}

### アーカイブ済みオブジェクトの一時的なリストア 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="例 29. アーカイブ済みオブジェクトの一時的なリストア" caption-side="bottom"}

### オブジェクトのヘッダーの取得
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="例 30. オブジェクト・ヘッダーの応答の処理。" caption-side="bottom"}


### バケットのライフサイクル構成の作成
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="例 31. バケットのライフサイクルの設定に使用される関数。" caption-side="bottom"}

**メソッドの要約**
{: java}

方法 |  説明
--- | ---
`getBucketName()` | ライフサイクル構成が設定されるバケットの名前を取得します。
`getLifecycleConfiguration()` | 指定されたバケットの新しいライフサイクル構成を取得します。
`setBucketName(String bucketName)` | ライフサイクル構成が設定されるバケットの名前を設定します。
`withBucketName(String bucketName)` | ライフサイクル構成が設定されるバケットの名前を設定し、追加のメソッド呼び出しをチェーニングできるようにこのオブジェクトを返します。
{: java}

### バケットのライフサイクル構成の取得
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="例 32. オブジェクトのライフサイクル構成を取得するための関数シグニチャー。" caption-side="bottom"}

### バケットのライフサイクル構成の削除
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="例 33. オブジェクト構成の削除に使用される関数。" caption-side="bottom"}

### アーカイブ済みオブジェクトの一時的なリストア 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="例 34. アーカイブ済みオブジェクトをリストアするための関数シグニチャー。" caption-side="bottom"}

**メソッドの要約**
{: java}

方法 |  説明
--- | ---
`clone()` | ハンドラー・コンテキストを除くすべてのフィールドについて、このオブジェクトのシャロー・クローンを作成します。
`getBucketName()` | リストアするオブジェクトへの参照を含んでいるバケット名を返します。
`getExpirationInDays()` | オブジェクトの作成から有効期限切れまでの期間を日数で返します。
`setExpirationInDays(int expirationInDays)` | オブジェクトがバケットにアップロードされてから有効期限が切れるまでの期間を日数で設定します。
{: java}

### オブジェクトのヘッダーの取得
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="例 35. オブジェクトのヘッダーの取得に使用される関数。" caption-side="bottom"}

**メソッドの要約**
{: java}

方法 |  説明
--- | ---
`clone()` | この `ObjectMetadata` のクローンを返します。
`getRestoreExpirationTime()` | アーカイブから一時的にリストアされたオブジェクトの有効期限が切れ、アクセスするには再リストアが必要になる時刻を返します。
`getStorageClass() ` | バケットの元のストレージ・クラスを返します。
`getIBMTransition()` | 遷移ストレージ・クラスおよび遷移の時刻を返します。
{: java}

## 次のステップ
{: #archive-next-steps}

現在、{{site.data.keyword.cloud_notm}} は、{{site.data.keyword.cos_full_notm}} に加えて、ユーザーの多様なニーズに応じていくつかのオブジェクト・ストレージ・オファリングを提供しています。これらはすべて Web ベースのポータルおよび REST API を介してアクセス可能です。[詳細はこちら。](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
