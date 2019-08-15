---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: worm, immutable, policy, retention, compliance

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

# Immutable Object Storage の使用
{: #immutable}

Immutable Object Storage は、保存期間が終了し、訴訟ホールドがすべて解除されるまで、お客様が電子レコードを保持し、データ保全性を維持することを、消去も再書き込みもできない WORM (Write-Once-Read-Many) という方法で可能にします。この機能は、以下の業界の組織 (以下に限定されるわけではありません) など、環境で長期間のデータ保存を必要とするお客様であれば誰でも使用できます。

 * 金融
 * 医療
 * メディア・コンテンツ・アーカイブ
 * オブジェクトまたは文書に対する特権のある変更または削除を防止しようとしているユーザー

基礎となっている機能は、ブローカー・ディーラー取引などの金融レコード管理を扱っていて、再書き込みも消去もできない形式でオブジェクトを保持する必要がある組織で使用されることもあります。

Immutable Object Storage は、特定の地域でのみ使用可能です。詳しくは、[統合サービス](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)を参照してください。また、「標準」価格プランが必要です。詳しくは、[料金設定](https://www.ibm.com/cloud/object-storage)を参照してください。
{:note}

保存ポリシーが設定されたバケットで Aspera 高速転送を使用することはできません。
{:important}

## 用語および使用法
{: #immutable-terminology}

### 保存期間
{: #immutable-terminology-period}

オブジェクトが COS バケットに保管されたままでなければならない期間。

### 保存ポリシー
{: #immutable-terminology-policy}

保存ポリシーは、COS バケット・レベルで有効です。このポリシーは、最大、最小、およびデフォルトの保存期間を定義し、バケット内のすべてのオブジェクトに適用されます。

最小保存期間は、オブジェクトをバケット内で保持しなければならない最小期間です。

最大保存期間は、オブジェクトをバケット内で保持できる最大期間です。

カスタム保存期間を指定せずにオブジェクトをバケットに保管すると、デフォルトの保存期間が使用されます。最小保存期間はデフォルト保存期間以下でなければならず、デフォルト保存期間は最大保存期間以下でなければなりません

注: オブジェクトに指定できる最大保存期間は 1000 年です。

注: バケットに保存ポリシーを作成するには、「管理者」役割が必要です。詳しくは、[バケット許可](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions)を参照してください。

### 訴訟ホールド 
{: #immutable-terminology-hold}

ある種のレコード (オブジェクト) は、保存期間が満了した後でも削除されないようにする必要があります。例えば、法的レビューで完了が保留されている場合に、オブジェクトに対して設定された元の保存期間を過ぎても、一定の延長期間の間はレコードへのアクセスが必要になることがあります。このようなシナリオでは、オブジェクト・レベルで訴訟ホールド・フラグを適用できます。 
オブジェクトへの訴訟ホールドの適用は、COS バケットへの初期アップロード時に行うことも、オブジェクトが追加された後で行うこともできます。
 
注: オブジェクト当たり最大 100 個の訴訟ホールドを適用できます。

### 無期限保存
{: #immutable-terminology-indefinite}

ユーザーは、新しい保存期間が適用されるまでオブジェクトが無期限に保管されるように設定できます。これはオブジェクト・レベルで設定されます。

### イベント・ベースの保存
{: #immutable-terminology-events}

Immutable Object Storage を使用して、ユーザーは、ユース・ケースの保存期間の最終的な長さがはっきり分からない場合や、イベント・ベースの保存機能を利用したい場合に、オブジェクトに無期限の保存を設定できます。無期限に設定した後で、ユーザー・アプリケーションでオブジェクト保存期間を有限の値に変更できます。例えば、ある会社で従業員の退職後 3 年間は従業員レコードを保持するという方針があるとします。従業員が入社した時点では、その従業員に関連付けられたレコードを無期限に保持するようにできます。従業員が退職すると、この無期限の保存は、会社の方針に定義されているように、その時点から 3 年間の有限値に変換されます。そうすると、保存期間が変更された後の 3 年間、オブジェクトは保護されることになります。ユーザー・アプリケーションまたはサード・パーティー・アプリケーションは、SDK または REST API を使用して、保存期間を無限から有限に変更できます。

### 永久保存
{: #immutable-terminology-permanent}

永久保存は、保存ポリシーが有効にされた COS バケット・レベルでのみ有効にすることができ、ユーザーはオブジェクトのアップロード中に永久保存期間オプションを選択できます。一度有効にした後でこのプロセスを元に戻すことはできず、永久保存期間を使用してアップロードされたオブジェクトは**削除できません**。保存ポリシーが設定された COS バケットを使用してオブジェクトを**永久に**保管する正当な必要性があるかどうかを検証するのは、ユーザー側の責任です。


Immutable Object Storage を使用する場合、データが保存ポリシーの対象である間はずっと、ご使用の IBM Cloud アカウントが IBM Cloud の方針およびガイドラインに照らして良好な状態であり続けるようにするのはお客様の責任です。詳しくは、IBM Cloud サービスの用語を参照してください。
{:important}

## Immutable Object Storage と各種規則に関する考慮事項
{: #immutable-regulation}

Immutable Object Storage を使用する場合、ここで説明されている機能のいずれについても、一般的に以下によって管理されている電子記録保管および保存に関する主要な規則に準拠するように利用できるかどうかを確認するのは、お客様の責任です。

  * [Securities and Exchange Commission (SEC) Rule 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64)
  * [Financial Industry Regulatory Authority (FINRA) Rule 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957)
  * [Commodity Futures Trading Commission (CFTC) Rule 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

お客様が十分な情報に基づいて意思決定を行うのを支援するため、IBM は、Immutable Object Storage 機能の独立した査定を行うように Cohasset Associates Inc. に依頼しました。IBM Cloud Object Storage の Immutable Object Storage 機能の査定の詳細については、Cohasset Associates Inc. の [レポート](https://www.ibm.com/downloads/cas/JBDNP0KV)をご覧ください。

### アクセスおよびトランザクションの監査
{: #immutable-audit}
Immutable Object Storage のログ・データにアクセスして、保存パラメーター、オブジェクト保存期間、および訴訟ホールドの適用の変更を検討することができます。これは、カスタマー・サービス・チケットをオープンすることで事例ごとに確認できます。

## コンソールの使用
{: #immutable-console}

保存ポリシーは新規または既存の空のバケットに追加することができ、削除することはできません。新規バケットの場合、[サポートされる地域](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)のいずれかでバケットを作成し、次に、**「保存ポリシーの追加」**オプションを選択します。既存バケットの場合、オブジェクトがないことを確認してから、構成設定に移動し、「バケット保存ポリシー」セクションの**「ポリシーの作成」**ボタンをクリックします。いずれの場合も、最小、最大、およびデフォルトの保存期間を設定してください。

## REST API、ライブラリー、および SDK の使用
{: #immutable-sdk}

保存ポリシーを処理するアプリケーションをサポートするため、いくつかの新しい API が IBM COS SDK に導入されました。このページの上部で言語 (HTTP、Java、Javascript、または Python) を選択すると、適切な COS SDK を使用する例が表示されます。 

すべてのコード例で、さまざまなメソッドを呼び出すことができる `cos` という名前のクライアント・オブジェクトが存在することを前提としていることに注意してください。クライアントの作成について詳しくは、特定の SDK ガイドを参照してください。

保存期間の設定に使用されるすべての日付値は GMT です。`Content-MD5` ヘッダーは、データ保全性を確保するために必須であり、SDK を使用する場合は自動的に送信されます。
{:note}

### 既存バケットへの保存ポリシーの追加
{: #immutable-sdk-add-policy}
`PUT` 操作のこの実装では、`protection` 照会パラメーターを使用して、既存のバケットに保存パラメーターを設定します。この操作により、保存期間の最小、デフォルト、および最大を設定または変更できます。また、この操作により、バケットの保護状態を変更することもできます。 

保護対象のバケットに書き込まれたオブジェクトは、保護期間が満了し、オブジェクトに対するすべての法的保留が解除されるまで削除できません。オブジェクトの作成時にオブジェクト固有の値が指定されない限り、バケットのデフォルトの保存期間値がオブジェクトにも適用されます。保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。 

保存期間設定 `MinimumRetention`、`DefaultRetention`、および `MaximumRetention` でサポートされる最小値と最大値は、それぞれ 0 日と 365243 日 (1000 年) です。 

`Content-MD5` ヘッダーは必須です。この操作では、追加の照会パラメーターは使用されません。

エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
{:tip}

{: http}

**構文**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**要求例**
{: http}

```
PUT /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
x-amz-content-sha256: 2938f51643d63c864fdbea618fe71b13579570a86f39da2837c922bae68d72df
Content-MD5: GQmpTNpruOyK6YrxHnpj7g==
Content-Type: text/plain
Host: 67.228.254.193
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

**応答例**
{: http}

```
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.14.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```
{: codeblock}
{: http}

```py
def add_protection_configuration_to_bucket(bucket_name):
    try:
        new_protection_config = {
            "Status": "Retention",
            "MinimumRetention": {"Days": 10},
            "DefaultRetention": {"Days": 100},
            "MaximumRetention": {"Days": 1000}
        }

        cos.put_bucket_protection_configuration(Bucket=bucket_name, ProtectionConfiguration=new_protection_config)

        print("Protection added to bucket {0}\n".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to set bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addProtectionConfigurationToBucket(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    cos.setBucketProtection(bucketName, newConfig);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}

public static void addProtectionConfigurationToBucketWithRequest(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    SetBucketProtectionConfigurationRequest newRequest = new SetBucketProtectionConfigurationRequest()
        .withBucketName(bucketName)
        .withProtectionConfiguration(newConfig);

    cos.setBucketProtectionConfiguration(newRequest);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}
```
{: codeblock}
{: java}

### バケットの保存ポリシーの確認
{: #immutable-sdk-get}

GET 操作のこの実装は、既存バケットの保存パラメーターを取り出します。
{: http}

**構文**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**要求例**
{: http}

```xml
GET /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
Content-Type: text/plain
Host: 67.228.254.193
Example response
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.13.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

バケットに対する保存構成がない場合、サーバーは代わりに状況を Disabled として応答します。
{: http}

```xml
<ProtectionConfiguration>
  <Status>Disabled</Status>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

```py
def get_protection_configuration_on_bucket(bucket_name):
    try:
        response = cos.get_bucket_protection_configuration(Bucket=bucket_name)
        protection_config = response.get("ProtectionConfiguration")

        print("Bucket protection config for {0}\n".format(bucket_name))
        print(protection_config)
        print("\n")
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to get bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void getProtectionConfigurationOnBucket(String bucketName) {
    System.out.printf("Retrieving protection configuration from bucket: %s\n", bucketName;

    BucketProtectionConfiguration config = cos.getBucketProtection(bucketName);

    String status = config.getStatus();

    System.out.printf("Status: %s\n", status);

    if (!status.toUpperCase().equals("DISABLED")) {
        System.out.printf("Minimum Retention (Days): %s\n", config.getMinimumRetentionInDays());
        System.out.printf("Default Retention (Days): %s\n", config.getDefaultRetentionInDays());
        System.out.printf("Maximum Retention (Days): %s\n", config.getMaximumRetentionInDays());
    }
}
```
{: codeblock}
{: java}

### 保存ポリシーのあるバケットへのオブジェクトのアップロード
{: #immutable-sdk-upload}

`PUT` 操作のこの機能拡張では 3 つの新しい要求ヘッダーが追加されました。2 つは、異なる方法で保存期間を指定するためのものであり、1 つは新規オブジェクトに単一の訴訟ホールドを追加するためのものです。これらの新しいヘッダーの値が正しくない場合のために新しいエラーが定義され、オブジェクトが保存されている場合は上書きは失敗します。
{: http}

保存ポリシーのあるバケット内の保存されなくなったオブジェクト (保存期間が満了し、オブジェクトに訴訟ホールドがない) は、上書きされると再び保存されるようになります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。

`Content-MD5` ヘッダーは必須です。
{: http}

これらのヘッダーは、POST オブジェクトおよびマルチパート・アップロード要求にも適用されます。1 つのオブジェクトを複数のパートにしてアップロードする場合、各パートに `Content-MD5` ヘッダーが必要です。
{: http}

|値	| タイプ	| 説明 |
| --- | --- | --- | 
|`Retention-Period` | 負でない整数 (秒数) | オブジェクトを保管する保存期間 (秒数)。オブジェクトは、保存期間に指定された時間が経過するまで上書きも削除もできません。このフィールドと `Retention-Expiration-Date` が指定された場合、`400` エラーが返されます。いずれも指定されない場合は、バケットの `DefaultRetention` 期間が使用されます。ゼロ (`0`) は、有効な値であり、バケットの最小保存期間も `0` であると想定されます。|
| `Retention-expiration-date` | 日付 (ISO 8601 フォーマット) | オブジェクトを合法的に削除または変更できるようになる日付。これか、Retention-Period ヘッダーのいずれかのみを指定できます。両方が指定された場合、`400` エラーが返されます。いずれも指定されない場合は、バケットの DefaultRetention 期間が使用されます。 |
| `Retention-legal-hold-id` | ストリング | オブジェクトに適用される単一の法的保留。法的保留とは、Y 文字の長さのストリングです。オブジェクトに関連付けられたすべての法的保留が解除されるまで、オブジェクトは上書きも削除もできません。 |

```py
def put_object_add_legal_hold(bucket_name, object_name, file_text, legal_hold_id):
    print("Add legal hold {0} to {1} in bucket {2} with a putObject operation.\n".format(legal_hold_id, object_name, bucket_name))
    
    cos.put_object(
        Bucket=bucket_name,
        Key=object_name,
        Body=file_text, 
        RetentionLegalHoldId=legal_hold_id)

    print("Legal hold {0} added to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))
  
def copy_protected_object(source_bucket_name, source_object_name, destination_bucket_name, new_object_name):
    print("Copy protected object {0} from bucket {1} to {2}/{3}.\n".format(source_object_name, source_bucket_name, destination_bucket_name, new_object_name))

    copy_source = {
        "Bucket": source_bucket_name,
        "Key": source_object_name
    }

    cos.copy_object(
        Bucket=destination_bucket_name, 
        Key=new_object_name, 
        CopySource=copy_source, 
        RetentionDirective="Copy"
    )

    print("Protected object copied from {0}/{1} to {2}/{3}\n".format(source_bucket_name, source_object_name, destination_bucket_name, new_object_name));

def complete_multipart_upload_with_retention(bucket_name, object_name, upload_id, retention_period):
    print("Completing multi-part upload for object {0} in bucket {1}\n".format(object_name, bucket_name))

    cos.complete_multipart_upload(
        Bucket=bucket_name, 
        Key=object_name,
        MultipartUpload={
            "Parts": [
    			{
                "ETag": part["ETag"],
                "PartNumber": 1
            }]
        },
        UploadId=upload_id,
        RetentionPeriod=retention_period
    )

    print("Multi-part upload completed for object {0} in bucket {1}\n".format(object_name, bucket_name))

def upload_file_with_retention(bucket_name, object_name, path_to_file, retention_period):
    print("Uploading file {0} to object {1} in bucket {2}\n".format(path_to_file, object_name, bucket_name))
    
    args = {
        "RetentionPeriod": retention_period
    }

    cos.upload_file(
        Filename=path_to_file,
        Bucket=bucket_name,
        Key=object_name,
        ExtraArgs=args
    )

    print("File upload complete to object {0} in bucket {1}\n".format(object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void putObjectAddLegalHold(String bucketName, String objectName, String fileText, String legalHoldId) {
    System.out.printf("Add legal hold %s to %s in bucket %s with a putObject operation.\n", legalHoldId, objectName, bucketName);

    InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

    ObjectMetadata metadata = new ObjectMetadata();        
    metadata.setContentLength(fileText.length());

    PutObjectRequest req = new PutObjectRequest(
        bucketName,
        objectName,
        newStream,
        metadata
    );
    req.setRetentionLegalHoldId(legalHoldId);

    cos.putObject(req);

    System.out.printf("Legal hold %s added to object %s in bucket %s\n", legalHoldId, objectName, bucketName);
}

public static void copyProtectedObject(String sourceBucketName, String sourceObjectName, String destinationBucketName, String newObjectName) {
    System.out.printf("Copy protected object %s from bucket %s to %s/%s.\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);

    CopyObjectRequest req = new CopyObjectRequest(
        sourceBucketName, 
        sourceObjectName, 
        destinationBucketName, 
        newObjectName
    );
    req.setRetentionDirective(RetentionDirective.COPY);
    

    cos.copyObject(req);

    System.out.printf("Protected object copied from %s/%s to %s/%s\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);
}
```
{: codeblock}
{: java}

### オブジェクトに対する訴訟ホールドの追加または削除
{: #immutable-sdk-legal-hold}

`POST` 操作のこの実装は、`legalHold` 照会パラメーターと、`add` および `remove` 照会パラメーターを使用して、保護バケット内の保護オブジェクトに対して単一の訴訟ホールドを追加または削除します。
{: http}

オブジェクトがサポートできる法的保留は 100 個です。

*  法的保留 ID は、最大長 64 文字かつ最小長 1 文字のストリングです。有効な文字は、英字、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-`、および ` です。
* 指定された法的保留を追加することで、オブジェクトに対する法的保留の合計数が 100 個を超える場合、新しい法的保留は追加されず、`400` エラーが返されます。
* ID が長すぎる場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID に無効文字が含まれている場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID がオブジェクトで既に使用中の場合、既存の法的保留は変更されず、ID が既に使用中であることを示す応答と `409` エラーが返されます。
* オブジェクトに保存期間メタデータがない場合は、`400` エラーが返され、法的保留の追加または削除は許可されません。

保存期間ヘッダーが存在する必要があります。存在しない場合は、`400` エラーが返されます。
{: http}

法的保留の追加または削除を行うユーザーには、このバケットに対する`マネージャー`許可が必要です。

`Content-MD5` ヘッダーは必須です。この操作では操作固有のペイロード要素は使用されません。
{: http}

**構文**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**要求例**
{: http}

```
POST /BucketName/ObjectName?legalHold&add=legalHoldID HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
```
{: codeblock}
{: http}

**応答例**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}

```py
def add_legal_hold_to_object(bucket_name, object_name, legal_hold_id):
    print("Adding legal hold {0} to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.add_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} added to object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))

def delete_legal_hold_from_object(bucket_name, object_name, legal_hold_id):
    print("Deleting legal hold {0} from object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.delete_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} deleted from object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addLegalHoldToObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Adding legal hold %s to object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.addLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s added to object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}

public static void deleteLegalHoldFromObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Deleting legal hold %s from object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.deleteLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s deleted from object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}
```
{: codeblock}
{: java}

### オブジェクトの保存期間の延長
{: #immutable-sdk-extend}

`POST` 操作のこの実装は、`extendRetention` 照会パラメーターを使用して、保護バケット内の保護オブジェクトの保存期間を延長します。
{: http}

オブジェクトの保存期間は延長のみが可能です。現在構成されている値から減らすことはできません。

保存延長値は次の 3 つの方法のいずれかで設定されます。

* 現行値からの追加時間 (`Additional-Retention-Period` または同様のメソッド)
* 新しい延長期間 (秒数) (`Extend-Retention-From-Current-Time` または同様のメソッド)
* オブジェクトの新しい保存有効期限日付 (`New-Retention-Expiration-Date` または同様のメソッド)

オブジェクト・メタデータに保管されている現在の保存期間は、`extendRetention` 要求に設定されているパラメーターに応じて、指定された追加時間分増やされるか、新しい値に置き換えられます。いずれの場合も、保存延長パラメーターは現在の保存期間に対して検査され、更新後の保存期間が現在の保存期間より大きい場合にのみ、延長パラメーターが受け入れられます。

保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。

**構文**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**要求例**
{: http}

```yaml
POST /BucketName/ObjectName?extendRetention HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00GMT
Authorization: authorization string
Content-Type: text/plain
Additional-Retention-Period: 31470552
```
{: codeblock}
{: http}

**応答例**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:50:00GMT
Connection: close
```
{: codeblock}
{: http}

```py
def extend_retention_period_on_object(bucket_name, object_name, additional_seconds):
    print("Extend the retention period on {0} in bucket {1} by {2} seconds.\n".format(object_name, bucket_name, additional_seconds))

    cos.extend_object_retention(
        Bucket=bucket_ame,
        Key=object_name,
        AdditionalRetentionPeriod=additional_seconds
    )

    print("New retention period on {0} is {1}\n".format(object_name, additional_seconds))
```
{: codeblock}
{: python}

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void extendRetentionPeriodOnObject(String bucketName, String objectName, Long additionalSeconds) {
    System.out.printf("Extend the retention period on %s in bucket %s by %s seconds.\n", objectName, bucketName, additionalSeconds);

    ExtendObjectRetentionRequest req = new ExtendObjectRetentionRequest(
        bucketName, 
        objectName)
        .withAdditionalRetentionPeriod(additionalSeconds);

    cos.extendObjectRetention(req);

    System.out.printf("New retention period on %s is %s\n", objectName, additionalSeconds);
}
```
{: codeblock}
{: java}

### オブジェクトの訴訟ホールドのリスト
{: #immutable-sdk-list-holds}

`GET` 操作のこの実装は、`legalHold` 照会パラメーターを使用して、オブジェクトの訴訟ホールドのリストおよび関連する保存状況を XML 応答本体で返します。
{: http}

この操作で返される内容は以下のとおりです。

* オブジェクト作成日
* オブジェクト保存期間 (秒数)
* 期間および作成日に基づいて計算された保存有効期限
* 法的保留のリスト
* 法的保留 ID
* 法的保留が適用されたときのタイム・スタンプ

オブジェクトに法的保留がない場合は、空の `LegalHoldSet` が返されます。
オブジェクトに保存期間が指定されていない場合、`404` エラーが返されます。

**構文**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**要求例**
{: http}

```
GET /BucketName/ObjectName?legalHold HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: {authorization-string}
Content-Type: text/plain
```
{: codeblock}
{: http}

**応答例**
{: http}

```xml
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
<?xml version="1.0" encoding="UTF-8"?>
<RetentionState>
  <CreateTime>Fri, 8 Sep 2018 21:33:08 GMT</CreateTime>
  <RetentionPeriod>220752000</RetentionPeriod>
  <RetentionPeriodExpirationDate>Fri, 1 Sep 2023 21:33:08
GMT</RetentionPeriodExpirationDate>
  <LegalHoldSet>
    <LegalHold>
      <ID>SomeLegalHoldID</ID>
      <Date>Fri, 8 Sep 2018 23:13:18 GMT</Date>
    </LegalHold>
    <LegalHold>
    ...
    </LegalHold>
  </LegalHoldSet>
</RetentionState>
```
{: codeblock}
{: http}

```py 
def list_legal_holds_on_object(bucket_name, object_name):
    print("List all legal holds on object {0} in bucket {1}\n".format(object_name, bucket_name));

    response = cos.list_legal_holds(
        Bucket=bucket_name,
        Key=object_name
    )

    print("Legal holds on bucket {0}: {1}\n".format(bucket_name, response))
```
{: codeblock}
{: python}

```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void listLegalHoldsOnObject(String bucketName, String objectName) {
    System.out.printf("List all legal holds on object %s in bucket %s\n", objectName, bucketName);

    ListLegalHoldsResult result = cos.listLegalHolds(
        bucketName, 
        objectName
    );

    System.out.printf("Legal holds on bucket %s: \n", bucketName);

    List<LegalHold> holds = result.getLegalHolds();
    for (LegalHold hold : holds) {
        System.out.printf("Legal Hold: %s", hold);
    }
}
```
{: codeblock}
{: java}
