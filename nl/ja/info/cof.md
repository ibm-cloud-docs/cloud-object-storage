---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# Cloud Foundry アプリでの Cloud Object Storage の使用
{: #cloud-foundry}

{{site.data.keyword.cos_full}} を {{site.data.keyword.cfee_full}} アプリケーションと組み合わせることで、地域およびエンドポイントを使用して高可用性コンテンツを提供できます。

## Cloud Foundry エンタープライズ環境
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} は、クラウドでアプリおよびサービスをホストするためのプラットフォームです。独自のアカウント内で実行され、共有ハードウェアまたは専用ハードウェアにデプロイできる、複数の隔離されたエンタープライズ・グレード・プラットフォームをオンデマンドでインスタンス化できます。このプラットフォームは、使用量の増加に応じたアプリのスケーリングを容易にして、ユーザーが開発に集中できるように、ランタイムおよびインフラストラクチャーを簡素化します。

Cloud Foundry プラットフォームの実装を成功させるためには、必要なリソースおよびエンタープライズの要件について[適切な計画と設計](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation)が必要です。Cloud Foundry エンタープライズ環境の[概要](/docs/cloud-foundry?topic=cloud-foundry-about#creating)ならびに、入門的な[チュートリアル](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started)を参照してください。

### 地域
{: #cloud-foundry-regions}
[地域エンドポイント](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)は、IBM Cloud 環境の重要な部分です。異なる地域でアプリケーションおよびサービス・インスタンスを作成でき、各地域でアプリケーション管理には同じ IBM Cloud インフラストラクチャーを、請求処理には同じ使用量詳細ビューを使用できます。お客様またはお客様の顧客に地理的に近い IBM Cloud 地域を選択することにより、アプリケーションのデータ待ち時間を削減できるほか、コストも最小限に抑えることができます。地域は、セキュリティー上の懸念または規制上の要件に応じて選択することもできます。 

{{site.data.keyword.cos_full}} では、アプリケーションからの API 要求の送信先となる[エンドポイントを選択](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)することによって、単一データ・センター、地域全体、または複数地域の組み合わせに対してもデータを分散させることができます。

### リソース接続と別名
{: #cloud-foundry-aliases}

別名は、リソース・グループ内の管理対象サービスと、組織またはスペース内のアプリケーションとを接続するものです。別名は、リモート・リソースへの参照を保持するシンボリック・リンクのようなものです。これにより、プラットフォーム全体でのインスタンスのインターオペラビリティーと再利用が可能になります。この接続 (別名) は、{{site.data.keyword.cloud_notm}} コンソールでサービス・インスタンスとして表されます。 リソース・グループ内にサービスのインスタンスを作成し、その後、地域の組織またはスペース内に別名を作成することによって、それらのいずれの地域からでもそのインスタンスを再使用できます。

## VCAP 変数としての資格情報の保管 
{: #cloud-foundry-vcap}

{{site.data.keyword.cos_short}} 資格情報は VCAP_SERVICES 環境変数に保管でき、{{site.data.keyword.cos_short}} サービスにアクセスするとき、それを使用するために解析できます。資格情報には、以下の例に示すような情報が含まれます。

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

その後、{{site.data.keyword.cos_short}} コンテンツにアクセスするためにアプリケーション内で VCAP_SERVICES 環境変数を解析できます。以下は、Node.js を使用して環境変数を COS SDK に統合する例です。

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

SDK を使用して {{site.data.keyword.cos_short}} にアクセスする方法とサンプル・コードについて詳しくは、以下を参照してください。

* [Java の使用](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [Python の使用](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [Node.js の使用](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## サービス・バインディングの作成 
{: #cloud-foundry-bindings}

### ダッシュボード
{: #cloud-foundry-bindings-console}

サービス・バインディングを作成する最も簡単な方法は、[{{site.data.keyword.cloud}} ダッシュボード](https://cloud.ibm.com/resources)を使用することです。 

1. [ダッシュボード](https://cloud.ibm.com/resources)にログインします。
2. 目的の Cloud Foundry アプリケーションをクリックします。
3. 左側のメニューで「接続」をクリックします。
4. 右側にある**「接続の作成」**をクリックします。
5. *「既存の互換性のあるサービスの接続」*ページで、目的の {{site.data.keyword.cos_short}} サービスの上にカーソルを移動し、**「接続」**をクリックします。
6. *「IAM 対応サービスの接続」*ポップアップ画面で、「アクセス役割」を選択し、サービス ID の「自動生成」は未選択のままで**「接続」**をクリックします。
7. 新しいサービス・バインディングを使用するためには、Cloud Foundry アプリケーションを再ステージングする必要があります。**「再ステージ」**をクリックしてプロセスを開始します。
8. 再ステージングが完了すると、アプリケーションで Cloud Object Storage サービスを使用できるようになります。

アプリケーションの VCAP_SERVICES 環境変数は、サービス情報で自動的に更新されます。新しい変数を表示するには、次のようにします。

1. 右側のメニューで*「ランタイム」* をクリックします。
2. *「環境変数」* をクリックします。
3. 該当の COS サービスがリストされるようになったことを確認します。

### IBM クライアント・ツール (CLI)
{: #cloud-foundry-bindings-cli}

1. IBM Cloud CLI にログインします。
```
 ibmcloud login --apikey <your api key>
```

2. Cloud Foundry 環境をターゲットとします。
```
 ibmcloud target --cf
```

3. {{site.data.keyword.cos_short}} のサービス別名を作成します。
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. {{site.data.keyword.cos_short}} 別名と Cloud Foundry アプリケーションの間のサービス・バインディングを作成し、バインディングに役割を指定します。有効な役割は以下のとおりです。<br/><ul><li>ライター</li><li>リーダー</li><li>管理者</li><li>管理者</li><li>オペレーター</li><li>ビューアー</li><li>エディター</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### IBM クライアント・ツール (CLI) と HMAC 資格情報
{: #cloud-foundry-hmac}

ハッシュ・ベースのメッセージ認証コード (HMAC) は、アクセス・キーと秘密鍵のペアを使用する、作成されるメッセージ認証コードを計算するためのメカニズムです。この手法を使用して、メッセージの保全性と認証性を検証できます。[HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)の使用について詳しくは、{{site.data.keyword.cos_short}} の資料を参照してください。

1. IBM Cloud CLI にログインします。
```
 ibmcloud login --apikey <your api key>
```

2. Cloud Foundry 環境をターゲットとします。
```
 ibmcloud target --cf
```

3. {{site.data.keyword.cos_short}} のサービス別名を作成します。
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. {{site.data.keyword.cos_short}} 別名と Cloud Foundry アプリケーションの間のサービス・バインディングを作成し、バインディングに役割を指定します。<br/><br/>* **注:** HMAC 対応のサービス資格情報を作成する場合、追加のパラメーター* (`{"HMAC":true}`) *が必要です。*<br/><br/>有効な役割は以下のとおりです。<br/><ul><li>ライター</li><li>リーダー</li><li>管理者</li><li>管理者</li><li>オペレーター</li><li>ビューアー</li><li>エディター</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### {{site.data.keyword.containershort_notm}}へのバインディング
{: #cloud-foundry-k8s}

{{site.data.keyword.containershort}}へのサービス・バインディングを作成する場合に必要な手順は、若干異なります。 

*このセクションでは、[jq - 軽量のコマンド・ライン JSON プロセッサー](https://stedolan.github.io/jq/){:new_window} もインストールする必要があります。*

以下の情報が必要になります。また、その下の各コマンドではキー値をこれらの情報で置換してください。

* `<service alias>` - COS サービスの新しい別名
* `<cos instance name>` - 既存の COS インスタンスの名前
* `<service credential name>` - サービス・キー/資格情報の新しい名前
* `<role>` - サービス・キーに関連付ける役割 (有効な役割については上記を参照してください。最もよく指定されるのは`ライター`です。)
* `<cluster name>` - 既存の Kubernetes クラスター・サービスの名前
* `<secret binding name>` - この値は、COS がクラスター・サービスにバインドされるときに生成されます。


1. COS インスタンスのサービス別名を作成します。<br/><br/>* **注:** COS インスタンスに設定できるサービス別名は 1 つのみです。*
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. COS サービス別名への許可を持つ新規サービス・キーを作成します。
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. クラスター・サービスを COS にバインドします。
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. COS サービス別名がクラスターにバインドされたことを確認します。
```
ibmcloud cs cluster-services --cluster <cluster name>
```
出力は次のようになります。
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. クラスター内のシークレットのリストを取得し、目的の COS サービスのシークレットを見つけます。通常は、`binding-` に、ステップ 1 で指定した `<service alias>` を加えたものになります (すなわち、`binding-sv-cos`)。この値をステップ 6 で `<secret binding name>` として使用します。
```
kubectl get secrets
```
出力は次のようになります。
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. COS HMAC 資格情報が、クラスターのシークレット内で有効なことを確認します。
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
出力は次のようになります。
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
