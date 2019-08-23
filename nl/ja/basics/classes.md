---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-14"

keywords: storage classes, tiers, cost, buckets, location constraint, provisioning code, locationconstraint

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
{:table: .aria-labeledby="caption"}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}
{:curl: .ph data-hd-programlang='curl'}

# ストレージ・クラスの使用
{: #classes}

すべてのデータがアクティブなワークロードに送られるわけではありません。 保存データは長期間アクセスされないままになることがあります。あまりアクティブでないワークロード用に、異なるストレージ・クラスを使用するバケットを作成できます。それらのバケットに保管されるオブジェクトは、標準ストレージとは異なるスケジュールで課金されます。

## クラスとは
{: #classes-about}

以下の 4 つのストレージ・クラスがあります。

*  **Standard**: アクティブなワークロードに使用されます。データの取得に料金はかかりません (操作要求そのものとパブリック・アウトバウンド帯域幅のコストを除く)。
*  **Vault**: データへのアクセスが頻繁ではない、クールなワークロードに使用されます。データの読み取りに取得料金がかかります。このサービスには、その使用目的である、クールな (あまりアクティブでない) データと整合する、オブジェクト・サイズと保管期間のしきい値が含まれています。
*  **Cold Vault**: データが主としてアーカイブされた状態にある (アクセス頻度が 90 日ごと、またはそれ以下の) コールドなワークロードに使用されます。データの読み取りにはより高額の取得料金がかかります。このサービスには、その使用目的であるコールドな (まったくアクティブでない) データの保管と整合する、オブジェクト・サイズと保管期間のしきい値が含まれています。
*  **Flex**: アクセス・パターンの予測が難しい動的ワークロードに使用されます。使用量に応じて、あまりアクセスされないデータの比較的低額な保管コストと取得料金とを合計した値が上限値を超えると、保管料金が高くなり、取得料金はかからなくなります。データへのアクセスが頻繁でない場合は、Standard ストレージよりも Flex ストレージのほうがコスト効率が良くなる可能性があります。また、比較的クールな使用パターンがよりアクティブになる場合は、Vault または Cold Vault ストレージよりも Flex ストレージのほうがコスト効率が良くなります。Flex バケットには、オブジェクト・サイズおよび保管期間のしきい値は適用されません。

価格設定の詳細については、[ibm.com の価格表](https://www.ibm.com/cloud/object-storage#s3api)を参照してください。

異なるストレージ・クラスを使用するバケットの作成方法について詳しくは、[API リファレンス](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)を参照してください。

## 異なるストレージ・クラスを使用するバケットの作成方法
{: #classes-locationconstraint}

コンソールでバケットを作成する場合、ストレージ・クラスを選択できるドロップダウン・メニューがあります。 

バケットをプログラマチックに作成する場合、使用されるエンドポイントに対応する `LocationConstraint` を指定する必要があります。`LocationConstraint` の有効なプロビジョニング・コードは以下のとおりです。<br>
&emsp;&emsp;  **米国地域:** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **米国東部:** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **米国南部:** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **EU 地域:** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **EU グレートブリテン:** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **EU ドイツ:** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **AP 地域:** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **AP 日本:** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **AP オーストラリア:** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **アムステルダム:** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **チェンナイ:** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **ホンコン:** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **メルボルン:** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **メキシコ:** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **ミラノ:** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **モントリオール:** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **オスロ:** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **サンノゼ:** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **サンパウロ:** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **ソウル:** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **トロント:** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

## REST API、ライブラリー、および SDK の使用
{: #classes-sdk}

保存ポリシーを処理するアプリケーションをサポートするため、いくつかの新しい API が IBM COS SDK に導入されました。このページの上部で言語 (curl、Java、Javascript、Go、または Python) を選択すると、適切な COS SDK を使用する例が表示されます。 

すべてのコード例で、さまざまなメソッドを呼び出すことができる `cos` という名前のクライアント・オブジェクトが存在することを前提としていることに注意してください。クライアントの作成について詳しくは、特定の SDK ガイドを参照してください。


### ストレージ・クラスが設定されたバケットの作成

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName, "us-vault");
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```
{: codeblock}
{: java}


```javascript
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: 'us-standard'
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}


```py
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create bucket: {0}".format(e))
```
{: codeblock}
{: python}

```go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Names
    newBucket := "<NEW_BUCKET_NAME>"

    input := &s3.CreateBucketInput{
        Bucket: aws.String(newBucket),
        CreateBucketConfiguration: &s3.CreateBucketConfiguration{
            LocationConstraint: aws.String("us-cold"),
        },
    }
    client.CreateBucket(input)

    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```
{: codeblock}
{: go}


```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}
{: curl}

バケットがいったん作成されると、そのバケットのストレージ・クラスを変更することはできません。オブジェクトを再分類する必要がある場合は、ご希望のストレージ・クラスが設定された別のバケットにデータを移動する必要があります。 
