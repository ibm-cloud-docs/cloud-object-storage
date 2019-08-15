---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-05"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:external: target="blank" .external}
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
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 
{:http: .ph data-hd-programlang='http'} 

# 有効期限ルールを使用した失効データの削除
{: #expiry}

有効期限ルールは、定義された期間 (オブジェクト作成日からの期間) の経過後にオブジェクトを削除します。 

{{site.data.keyword.cos_full_notm}} に組み込まれている Web コンソール、REST API、およびサード・パーティー・ツールを使用して、オブジェクトのライフサイクルを設定できます。 

* 新規または既存のバケットに有効期限ルールを追加できます。
* 既存の有効期限ルールを変更したり、無効化したりすることができます。
* 新しく追加または変更された有効期限ルールは、バケット内のすべての新規オブジェクトおよび既存オブジェクトに適用されます。
* ライフサイクル・ポリシーを追加または変更するには、「`ライター`」役割が必要です。 
* バケット当たり最大 1000 個のライフサイクル・ルール (保存 + 有効期限) を定義できます。
* 有効期限ルールの変更が有効になるまでに最大 24 時間かかると想定しておいてください。
* オプションの接頭部フィルターを定義することによって、各有効期限ルールの適用範囲を、名前が接頭部に一致するオブジェクトのサブセットのみに限定できます。
* 接頭部フィルターのない有効期限ルールは、バケット内のすべてのオブジェクトに適用されます。
* 日数で指定されたオブジェクト有効期限は、オブジェクトが作成された時点から計算して翌日の午前 0 時 (UTC) に丸められます。例えば、作成日から 10 日後にオブジェクトのセットを期限切れにするという有効期限ルールがバケットに指定されている場合、2019 年 4 月 15 日 05:10 UTC に作成されたオブジェクトは 2019 年 4 月 26 日 2019 00:00 UTC に有効期限が切れます。 
* 各バケットの有効期限ルールは、24 時間ごとに 1 回評価されます。オブジェクトの期限日付に基づいて期限切れに適格であると判定されたオブジェクトは、削除のためにキューに入れられます。有効期限が切れたオブジェクトの削除は翌日に始まり、通常は 24 時間未満で完了します。オブジェクトが削除されると、そのオブジェクトに関連付けられたストレージについての請求はなくなります。

バケットの Immutable Object Storage 保存ポリシーの対象であるオブジェクトに対しては、その保存ポリシーが適用されなくなるまで、有効期限に関するアクションはすべて据え置かれます。
{: important}

## 有効期限ルールの属性
{: #expiry-rules-attributes}

各有効期限ルールには、以下の属性があります。

### ID
ルールの ID は、バケットのライフサイクル構成内で固有でなければなりません。

### Expiration
Expiration ブロックには、オブジェクトの自動削除を制御する詳細が含まれます。これは、将来の特定の日付、または、新規オブジェクトが書き込まれてからの期間にすることができます。

### Prefix
バケット内のオブジェクト名の接頭部と突き合わせるストリング (オプション)。接頭部を含んでいるルールは、一致するオブジェクトにのみ適用されます。複数のルールを使用して、同じバケット内の異なる接頭部に対して異なる有効期限アクションを適用できます。例えば、同じライフサイクル構成内で、1 つのルールは `logs/` で始まるすべてのオブジェクトを 30 日後に削除し、2 つ目のルールは `video/` で始まるオブジェクトを 365 日後に削除するといったことが可能です。  

### Status
ルールを有効または無効にすることができます。ルールは、有効にされた場合にのみアクティブです。

## ライフサイクル構成の例

次の構成は、すべての新規オブジェクトを 30 日後に期限切れにします。

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-30-days</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>30</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

次の構成は、接頭部が `foo/` のすべてのオブジェクトを 2020 年 6 月 1 日に削除します。

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-on-a-date</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Date>2020-06-01T00:00:00.000Z</Date>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

遷移ルールと有効期限ルールを組み合わせることもできます。次の構成では、すべてのオブジェクトは作成後 90 日間保存され、接頭部が `foo/` のすべてのオブジェクトは 180 日後に削除されます。

```xml
<LifecycleConfiguration>
  <Rule>
		<ID>archive-first</ID>
		<Filter />
		<Status>Enabled</Status>
    <Transition>
      <Days>90</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
	</Rule>
	<Rule>
		<ID>then-delete</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Days>180</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

## コンソールの使用
{: #expiry-using-console}

新規バケットを作成するときに、**「有効期限ルールの追加」**ボックスにチェック・マークを付けます。次に、新しい有効期限ルールを作成するために**「ルールの追加」**をクリックします。バケットの作成中に最大 5 つのルールを追加でき、それ以上のルールは後で追加することができます。

既存バケットの場合、ナビゲーション・メニューから**「構成」**を選択し、_「有効期限ルール」_セクションにある**「ルールの追加」**をクリックします。

## API および SDK の使用
{: #expiry-using-api-sdks}

REST API または IBM COS SDK を使用して、有効期限ルールをプログラマチックに管理できます。コンテキスト・スイッチャーでカテゴリーを選択することによって、例の形式を選択してください。

### バケットのライフサイクル構成への有効期限ルールの追加
{: #expiry-api-create}

**REST API リファレンス**
{: http}

`PUT` 操作のこの実装は、`lifecycle` 照会パラメーターを使用してバケットのライフサイクル設定を設定します。この操作では、バケットに対して単一のライフサイクル・ポリシー定義が可能です。ポリシーは、パラメーター `ID`、`Status`、`Filter`、および `Expiration` から構成されるルールの集合として定義されます。
{: http}
 
Cloud IAM ユーザーがバケットからライフサイクル・ポリシーを削除するには、「`ライター`」役割を持っている必要があります。

クラシック・インフラストラクチャーのユーザーがバケットからライフサイクル・ポリシーを削除するには、バケットに対する「`所有者`」許可を持っている必要があります。

ヘッダー                    | タイプ   | 説明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | ストリング | **必須**: base64 でエンコードされた、ペイロードの 128 ビット MD5 ハッシュ。ペイロードが転送中に変更されなかったことを確認するための保全性検査として使用されます。
{: http}

要求の本体には、以下のスキーマの XML ブロックが含まれている必要があります。
{: http}

| 要素                  | タイプ                 | 子                               | 上位                     | 制約                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | コンテナー            | `Rule`                                 | なし                     |限度 1。|
| `Rule`                   | コンテナー            | `ID`、`Status`、`Filter`、`Expiration` | `LifecycleConfiguration` | 限度 1000。                                                                                  |
| `ID`                     | ストリング               | なし                                   | `Rule`                   | (`a-z,`A-Z0-9`) と記号 `!` `_` `.` `*` `'` `(` `)` `-` で構成される必要があります|
| `Filter`                 | ストリング               | `Prefix`                               | `Rule`                   | `Prefix` 要素を含んでいる必要があります|
| `Prefix`                 | ストリング               | なし                                   | `Filter`                 | ルールは、この接頭部に一致するキーを持つすべてのオブジェクトに適用されます。|
| `Expiration`             | `Container`          | `Days` または `Date`                       | `Rule`                   |限度 1。|
| `Days`                   | 負でない整数 | なし                                   | `Expiration`             | 0 より大きい値である必要があります。|
| `Date`                   | 日付                 | なし                                   | `Expiration`             | ISO 8601 形式でなければなりません。|
{: http}

要求の本体には、表に示されたスキーマを持つ XML ブロックが含まれている必要があります (例 1 を参照)。
{: http}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="例 1. 要求本体の XML の例。" caption-side="bottom"}
{: http}

**構文**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="例 2. この構文例では、スラッシュとドットの使用に注意してください。" caption-side="bottom"}
{: codeblock}
{: http}

**要求例**
{: http}

```yaml
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305

<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="例 3. オブジェクト・ライフサイクル構成を作成するための要求ヘッダーの例。" caption-side="bottom"}
{: http}

**NodeJS COS SDK で使用するコードの例**
{: javascript}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);
var date = new Date('June 16, 2019 00:00:00');

var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* required */
        Expiration:
        {
          Date: date
        }
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

**Python COS SDK で使用するコードの例**
{: python}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。
{: python}

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'Status': 'Enabled',
                'Filter': {},
                'Expiration':
                {
                    'Days': 123
                },
            },
        ]
    }
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

**Java COS SDK で使用するコードの例**
{: java}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Define a rule for exiring items in a bucket
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);
            
            // Add the rule to a new BucketLifecycleConfiguration.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));
            
            // Use the client to set the LifecycleConfiguration on the bucket.
            _cosClient.setBucketLifecycleConfiguration(bucketName, configuration);   
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
    }
```
{: codeblock}
{: java}
{: caption="例 1. ライフサイクル構成の作成を示すコード・サンプル。" caption-side="bottom"}

### バケットのライフサイクル構成 (有効期限を含む) の調査
{: #expiry-api-view}

`GET` 操作のこの実装は、`lifecycle` 照会パラメーターを使用してバケットのライフサイクル設定を調べます。ライフサイクル構成が存在しない場合は、HTTP `404` 応答が返されます。
{: http}

Cloud IAM ユーザーがバケットからライフサイクル・ポリシーを削除するには、「`リーダー`」役割を持っている必要があります。

クラシック・インフラストラクチャーのユーザーがバケットからライフサイクル・ポリシーを削除するには、バケットに対する「`読み取り`」許可を持っている必要があります。

ヘッダー                    | タイプ   | 説明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | ストリング | **必須**: base64 でエンコードされた、ペイロードの 128 ビット MD5 ハッシュ。ペイロードが転送中に変更されなかったことを確認するための保全性検査として使用されます。
{: http}

**構文**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="例 5. この構文例では、スラッシュとドットの使用に注意してください。" caption-side="bottom"}
{: codeblock}
{: http}

**要求ヘッダー例**
{: http}

```yaml
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: caption="例 6. オブジェクト・ライフサイクル構成を作成するための要求ヘッダーの例。" caption-side="bottom"}
{: http}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

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

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').get_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。 

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Use the client to read the configuration
            BucketLifecycleConfiguration config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            
            System.out.println(config.toString());
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }
```
{: codeblock}
{: java}
{: caption="例 2. ライフサイクル構成の検査を示すコード・サンプル。" caption-side="bottom"}

### バケットのライフサイクル構成 (有効期限を含む) の削除
{: #expiry-api-delete}

`DELETE` 操作のこの実装は、`lifecycle` 照会パラメーターを使用してバケットのライフサイクル設定を調べます。バケットに関連付けられたすべてのライフサイクル・ルールが削除されます。ルールで定義された遷移は、新規オブジェクトに対しては行われなくなります。ただし、既存の遷移ルールは、ルールが削除される前に既にバケットに書き込まれたオブジェクトのために保持されます。有効期限ルールは存在しなくなります。ライフサイクル構成が存在しない場合は、HTTP `404` 応答が返されます。
{: http}

Cloud IAM ユーザーがバケットからライフサイクル・ポリシーを削除するには、「`ライター`」役割を持っている必要があります。

クラシック・インフラストラクチャーのユーザーがバケットからライフサイクル・ポリシーを削除するには、バケットに対する「`所有者`」許可を持っている必要があります。

**構文**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="例 7. この構文例では、スラッシュとドットの使用に注意してください。" caption-side="bottom"}
{: codeblock}
{: http}

**要求ヘッダー例**
{: http}

```yaml
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="例 8. オブジェクト・ライフサイクル構成を作成するための要求ヘッダーの例。" caption-side="bottom"}
{: http}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。 

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').delete_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

{{site.data.keyword.cos_full}} SDK を使用するために必要なのは、正しいパラメーターと適切な構成を指定して適切な関数を呼び出すことだけです。
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Delete the configuration.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);
            
            // Verify that the configuration has been deleted by attempting to retrieve it.
            config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            String s = (config == null) ? "Configuration has been deleted." : "Configuration still exists.";
            System.out.println(s);
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }

```
{: codeblock}
{: java}
{: caption="例 3. ライフサイクル構成の削除を示すコード・サンプル。" caption-side="bottom"}

## 次のステップ
{: #expiry-next-steps}

有効期限は、{{site.data.keyword.cos_full_notm}} で使用可能な多くのライフサイクル概念の 1 つに過ぎません。
この概要で取り上げた各概念について、[{{site.data.keyword.cloud_notm}} プラットフォーム](https://cloud.ibm.com/)でさらに詳しく探索できます。

