---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# `Postman` の使用
{: #postman}

ここでは、{{site.data.keyword.cos_full}} REST API のための基本的な `Postman` のセットアップについて説明します。さらに詳しい説明については、[バケット](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)または[オブジェクト](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)の API リファレンスを参照してください。

`Postman` を使用するには、オブジェクト・ストレージに関するある程度の知識と、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)または[コンソール](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)からの必要情報があることが前提です。分からない用語や変数については、[用語集](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)を参照してください。

個人情報 (PII): バケットの作成またはオブジェクトの追加を行うときには、ユーザー (個人) を名前、場所、またはその他の方法で特定できる情報を使用しないように注意してください。
{:tip}

## REST API クライアントの概要
{: #postman-rest}

REST (REpresentational State Transfer) は、複数のコンピューター・システムが互いに Web を介して (通常は、すべての主要な開発言語およびプラットフォームでサポートされている HTTP URL および動詞 (GET、PUT、POST など) を使用して) 対話するための標準を提供する設計様式です。ただし、REST API との対話は、標準的なインターネット・ブラウザーの使用ほど単純ではありません。単純なブラウザーでは URL 要求の操作は許可されていません。そこで REST API クライアントが役立ちます。

REST API クライアントは、既存の REST API ライブラリーとのインターフェースとなる、単純な GUI ベースのアプリケーションを提供します。優れたクライアントを使用すると、ユーザーは単純な HTTP 要求と複雑な HTTP 要求の両方を素早くまとめることができるため、 API のテスト、開発、および文書化が容易になります。Postman は、とても優秀な REST API クライアントであり、 API の設計とモック、デバッグ、テスト、文書化、モニター、および公開のための組み込みツールを含んでいる完全な API 開発環境を提供します。また、コラボレーションを容易にするコレクションやワークスペースなどの便利な機能も備えています。 

## 前提条件
{: #postman-prereqs}
* IBM Cloud アカウント
* [作成済みの Cloud Storage リソース](https://cloud.ibm.com/catalog/) (ライト・プランまたは無料プランで十分です)
* [インストールおよび構成済みの IBM Cloud CLI](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [Cloud Storage のサービス・インスタンス ID](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [IAM (ID およびアクセス管理) トークン](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [COS バケットのエンドポイント](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### バケットの作成
{: #postman-create-bucket}
1.	Postman を起動します。
2.	新規タブでドロップダウン・リストから `PUT` を選択します。
3.	アドレス・バーにエンドポイントを入力し、新規バケットの名前を追加します。
a.	バケット名はすべてのバケットで固有でなければならないため、特有の名前を付けてください。
4.	「Type」ドロップダウン・リストで Bearer トークンを選択します。
5.	「Token」ボックスで IAM トークンを追加します。
6.	「Preview Request」をクリックします。
a.	ヘッダーが追加されたことを示す確認メッセージが表示されます。
7.	Authorization の既存項目が表示されている「Header」タブをクリックします。
8.	新しいキーを追加します。
a.	キー: `ibm-service-instance-id`
b.	値: クラウド・ストレージ・サービスのリソース・インスタンス ID。
9.	「Send」をクリックします。
10.	`200 OK` 状況を示すメッセージが表示されます。

### 新規テキスト・ファイルの作成
{: #postman-create-text-file}

1.	プラス (+) アイコンをクリックして、新規タブを作成します。
2.	リストから `PUT` を選択します。
3.	アドレス・バーに、前のセクションからのバケット名、およびファイル名を含むエンドポイント・アドレスを入力します。
4.	「Type」リストで Bearer トークンを選択します。
5.	「Token」ボックスで IAM トークンを追加します。
6.	「Body」タブを選択します。
7.	「raw」オプションを選択し、「Text」が選択されていることを確認します。
8.	用意されたスペースにテキストを入力します。
9.	「Send」をクリックします。
10.	`200 OK` 状況を示すメッセージが表示されます。

### バケットの内容のリスト表示
{: #postman-list-objects}

1.	プラス (+) アイコンを選択して、新規タブを作成します。
2.	リストで `GET` が選択されていることを確認します。
3.	アドレス・バーで、前のセクションからのバケット名を含むエンドポイント・アドレスを入力します。
4.	「Type」リストで、Bearer トークンを選択します。
5.	「Token」ボックスで IAM トークンを追加します。
6.	「Send」をクリックします。
7.	`200 OK` 状況を示すメッセージが表示されます。
8.	「Response」セクションの「Body」に示される XML メッセージに、バケット内のファイルがリストされます。

## サンプル・コレクションの使用
{: #postman-collection}

構成可能な {{site.data.keyword.cos_full}} API 要求サンプルと共に、Postman コレクションを [ダウンロード ![外部リンク・アイコン](../icons/launch-glyph.svg "外部リンク・アイコン")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window} できます。

### Postman へのコレクションのインポート
{: #postman-import-collection}

1. Postman で、右上隅にある「Import」ボタンをクリックします。
2. 以下のいずれかの方法でコレクション・ファイルをインポートします。
    * 「Import」ウィンドウから、コレクション・ファイルを **Drop files here** というラベルが付いたウィンドウにドラッグ・アンド・ドロップします。
    * 「Choose Files」ボタンをクリックし、フォルダーを参照してコレクション・ファイルを選択します。
3. これで、「Collections」ウィンドウに *IBM COS* が表示されます。
4. このコレクションを展開すると、20 個のサンプル要求が表示されます。
5. このコレクションには 6 個の変数が含まれています。API 要求を正常に実行するためにはこれらの変数を設定する必要があります。
    * コレクションの右側にある 3 つのドットをクリックしてメニューを展開し、「Edit」をクリックします。
6. ご使用の Cloud Storage 環境に合わせて変数を編集します。
    * **bucket** - 作成する新規バケットの名前を入力します (バケット名は Cloud Storage 全体で固有でなければなりません)。
    * **serviceid** - Cloud Storage サービスの CRN を入力します。CRN の入手方法についての説明は[ここ](/docs/overview?topic=overview-crn)にあります。
    * **iamtoken** - Cloud Storage サービスの OAUTH トークンを入力します。OAUTH トークンの入手方法についての説明は[ここ](/docs/services/key-protect?topic=key-protect-retrieve-access-token)にあります。
    * **endpoint** - Cloud Storage サービスの地域エンドポイントを入力します。使用可能なエンドポイントは [IBM Cloud ダッシュボード](https://cloud.ibm.com/resources/){:new_window}で取得できます。
        * *サンプルが正しく実行されるようにするため、選択したエンドポイントがご使用の  Key Protect サービスと一致することを確認してください*
    * **rootkeycrn** - ご使用の 1 次 Key Protect サービスで作成されたルート・キーの CRN。
        * この CRN は以下のようなものです。<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *選択された Key Protect サービスがエンドポイントの地域と一致していることを確認してください*
    * **bucketlocationvault** - *新規バケットの作成 (異なるストレージ・クラス)* API 要求に対するバケット作成のロケーション制約値を入力します。
        * 許容値は以下のとおりです。
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. 「Update」をクリックします。

### サンプルの実行
{: #postman-samples}
API サンプル要求は、かなり単純で使いやすいものです。これらは、順番に実行され、Cloud Storage とどのように対話するのかを示すように設計されています。Cloud Storage サービスに対して機能テストを実行して、適切な動作を確認するためにも使用できます。

<table>
    <tr>
        <th>要求</th>
        <th>予期される結果</th>
        <th>テスト結果</th>
    </tr>
    <tr>
        <td>バケットのリストを取得する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
                <li>
                    「Body」に、クラウド・ストレージ内のバケットの XML リストを設定する必要があります。
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期される内容が応答に含まれます</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>新規バケットを作成する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>新規テキスト・ファイルを作成する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期されるヘッダーが応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>新規バイナリー・ファイルを作成する</td>
        <td>
            <ul>
                <li>
                    「Body」をクリックし、「Choose File」をクリックして、アップロードするイメージを選択します
                </li>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期されるヘッダーが応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>バケットからファイルのリストを取得する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
                <li>
                    応答の「Body」に、前の要求で作成した 2 つのファイルが表示されるはずです
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期されるヘッダーが応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>バケットからファイルのリストを取得する (接頭部でフィルタリングする)</td>
        <td>
            <ul>
                <li>照会ストリング値を prefix=&lt;任意のテキスト&gt; に変更します</li>
                <li>状況コード 200 OK</li>
                <li>
                    応答の「Body」に、指定した接頭部で始まる名前を持つファイルが表示されるはずです。
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期されるヘッダーが応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>テキスト・ファイルを取得する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
                <li>
                    応答の「Body」に、前の要求で入力したテキストが表示されるはずです
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期される本体内容が応答に含まれます</li>
                <li>予期されるヘッダーが応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>バイナリー・ファイルを取得する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
                <li>
                    応答の「Body」に、前の要求で選択したイメージが表示されるはずです
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期されるヘッダーが応答に含まれます</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>失敗したマルチパート・アップロードのリストを取得する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
                <li>
                    応答の「Body」に、失敗したバケットのマルチパート・アップロードが表示されるはずです
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期される内容が応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>失敗したマルチパート・アップロードのリストを取得する (名前でフィルタリングする)</td>
        <td>
            <ul>
                <li>照会ストリング値を prefix=&lt;任意のテキスト&gt; に変更します</li>
                <li>状況コード 200 OK</li>
                <li>
                    応答の「Body」に、失敗したバケットのマルチパート・アップロードのうち、指定した接頭部で始まる名前のものが表示されるはずです
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期される内容が応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>CORS が有効なバケットを設定する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>バケット CORS 構成を取得する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
                <li>
                    応答の「Body」に、バケットに対して設定された CORS 構成が表示されるはずです
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
                <li>予期される内容が応答に含まれます</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>バケット CORS 構成を削除する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>テキスト・ファイルを削除する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>バイナリー・ファイルを削除する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>バケットを削除する</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>新規バケットを作成する (異なるストレージ・クラス)</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>バケットを削除する (異なるストレージ・クラス)</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>新規バケットを作成する (Key Protect)</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>バケットを削除する (Key Protect)</td>
        <td>
            <ul>
                <li>状況コード 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求は成功しました</li>
            </ul>
        </td>                
    </tr>
</table>

## Postman Collection Runner の使用
{: #postman-runner}

Postman Collection Runner は、コレクションをテストするためのユーザー・インターフェースを提供し、1 つのコレクション内のすべての要求を一度に実行することを可能にします。 

1. Postman のメイン・ウィンドウの右上隅にある Runner ボタンをクリックします。
2. Runner ウィンドウで、IBM COS コレクションを選択し、画面の下部の大きな青い**「Run IBM COS」**ボタンをクリックします。
3. Collection Runner ウィンドウに、一連の要求が実行されるのに応じて反復が表示されます。各要求の下にテスト結果が表示されます。
    * **「Run Summary」**に要求がグリッド表示され、結果をフィルター操作できます。
    * **「Export Results」**をクリックして、結果を JSON ファイルに保存することもできます。
