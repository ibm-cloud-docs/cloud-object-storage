---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# 入門チュートリアル
{: #getting-started}

この入門チュートリアルでは、バケットの作成、オブジェクトのアップロード、および他のユーザーがデータを処理できるようにアクセス・ポリシーをセットアップするために必要な手順について説明します。
{: shortdesc}

## 始める前に
{: #gs-prereqs}

以下が必要です。
  * [{{site.data.keyword.cloud}} プラットフォーム・アカウント](https://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}} のインスタンス](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * アップロードするローカル・コンピューター上のいくつかのファイル
{: #gs-prereqs}

 このチュートリアルでは、{{site.data.keyword.cloud_notm}} プラットフォーム・コンソールでの最初のステップについて、新規ユーザーに説明します。API を使用して開始する開発者の場合は、[開発者のガイド](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev)または [API の概要](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)を参照してください。

## データを保管するバケットの作成
{: #gs-create-buckets}

  1. [{{site.data.keyword.cos_full_notm}} をオーダー](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)すると、_サービス・インスタンス_ が作成されます。{{site.data.keyword.cos_full_notm}} はマルチテナント・システムであり、{{site.data.keyword.cos_short}} のすべてのインスタンスが物理インフラストラクチャーを共有します。バケットの作成を開始できるサービス・インスタンスに自動的にリダイレクトされます。{{site.data.keyword.cos_short}} インスタンスは、[リソース・リスト](https://cloud.ibm.com/resources)で**「ストレージ」**の下にリストされます。

「リソース・インスタンス」と「サービス・インスタンス」という用語は同じ概念を指しており、同じ意味で使用できます。
{: tip}

  1. **「バケットの作成」**に従って、固有の名前を選択します。全世界のすべての地域のすべてのバケットが、単一の名前空間を共有します。バケットを作成するための[適切な許可](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions)を備えていることを確認します。

  **注**: バケットを作成する場合やオブジェクトを追加する場合は、個人情報 (PII) を使用しないようにしてください。PII とは、名前、場所、またはその他の方法でユーザー (個人) を特定できる情報のことです。{: tip}

  1. まず、必要な[レベルの_回復力_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) を選択してから、データを物理的に保管する_場所_ を選択します。回復力とは、データが分散される地域の範囲や規模を指します。_地域間_ の回復力は、データを複数の大都市地域に分散します。一方、_地域的_ 回復力はデータを単一の大都市地域に分散します。_単一データ・センター_ は、単一サイト内のデバイス間でのみデータを分散します。
  2. [バケットの_ストレージ・クラス_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes) を選択します。これは、保管されているデータを読み取る予想頻度を反映したものであり、請求の詳細を決定します。**「作成」**リンクをたどって、新規バケットを作成してそのバケットにアクセスします。

バケットはデータを編成する手段ですが、唯一の方法ではありません。オブジェクト名 (_オブジェクト・キー_ とよく呼ばれる) では、ディレクトリーに類似した編成システムに 1 つ以上のスラッシュを使用できます。この場合、区切り文字の前にあるオブジェクト名の部分を使用して、_オブジェクト接頭部_ を形成します。この接頭部は、API を介して単一バケット内の関連オブジェクトをリストするために使用されます。
{: tip}


## バケットへのオブジェクトの追加
{: #gs-add-objects}

次に、リストからバケットの 1 つを選択して、そのバケットに移動します。**「オブジェクトの追加 (Add Objects)」**をクリックします。新規オブジェクトは、同じバケット内の同じ名前を持つ既存のオブジェクトを上書きします。コンソールを使用してオブジェクトをアップロードした場合、オブジェクト名は常にファイル名と一致します。API を使用してデータを書き込む場合、ファイル名とオブジェクト・キーの間に関係は必要ありません。先に進み、このバケットに少数のファイルを追加します。

[Aspera High-Speed Transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload) プラグインを使用しない限り、コンソールを介したアップロードでは、オブジェクトは 200 MB に制限されます。大規模オブジェクト (最大 10 TB) は、[API を使用して複数のパートに分割し、並行してアップロードする](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)こともできます。オブジェクト・キーの長さは最大で 1024 文字にすることができます。また、Web アドレスで問題が生じる可能性がある文字は使用しないことをお勧めします。例えば、`?`、`=`、`<`、および他の特殊文字は、URL エンコードしていない場合、望ましくない動作を引き起こす可能性があります。
{:tip}

## バケットおよびデータを管理するためのユーザー・アカウントへのユーザーの招待
{: #gs-invite-user}

次に、別のユーザーを参加させ、インスタンスおよびそのインスタンスに保管されているデータの管理者としての役割を果たせるようにします。

  1. まず、新規ユーザーを追加するために、現在の {{site.data.keyword.cos_short}} インターフェースおよびヘッドから、IAM コンソールに移動する必要があります。**「管理」**メニューに移動し、**「アクセス (IAM)」**>**「ユーザー」**のリンクをたどります。**「ユーザーの招待」**をクリックします。
	<img alt="IAM のユーザーの招待" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`図 1: IAM のユーザーの招待`
  2. 組織に招待するユーザーの E メール・アドレスを入力してから、**「サービス」**セクションを展開し、**「アクセス権限の割り当て」**メニューから「リソース」を選択します。次に、**「サービス」**メニューから「Cloud Object Storage」を選択します。
	<img alt="IAM のサービス" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`図 2: IAM のサービス`
  3. これで、さらに 3 つのフィールド (_「サービス・インスタンス」_、_「リソース・タイプ」_、および _「リソース ID」_) が表示されます。最初のフィールドは、ユーザーがアクセスできる {{site.data.keyword.cos_short}} のインスタンスを定義します。{{site.data.keyword.cos_short}} のすべてのインスタンスに同じレベルのアクセス権限を付与するように設定することもできます。現在のところ、他のフィールドはブランクのままにすることができます。
	<img alt="IAM のユーザーの招待" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`図 3: IAM のユーザーの招待`
  4. **「役割の選択」**の下のチェック・ボックスにより、ユーザーが使用できるアクションのセットが決まります。ユーザーが他の[ユーザーおよびサービス ID](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) にインスタンスへのアクセス権限を付与できるようにする場合は、「管理者」プラットフォーム・アクセス役割を選択します。ユーザーが {{site.data.keyword.cos_short}} インスタンスの管理、およびバケットやオブジェクトの作成と削除を行えるようにする場合は、「管理者」サービス・アクセス役割を選択します。_サブジェクト_ (ユーザー)、_役割_ (管理者)、および_リソース_ ({{site.data.keyword.cos_short}} サービス・インスタンス) を組み合わせることで、[IAM ポリシー](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)を構成します。役割およびポリシーの詳細な説明については、[IAM 資料を参照](/docs/iam?topic=iam-userroles)してください。
<img alt="IAM の役割" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`図 4: IAM の役割の選択`
  5. {{site.data.keyword.cloud_notm}} は、基礎となるアカウント管理プラットフォームとして Cloud Foundry を使用するため、ユーザーが最初に組織にアクセスできるようにするには、最小限のレベルの Cloud Foundry アクセス権限を付与する必要があります。**「組織」**メニューから組織を選択してから、**「組織の役割」**メニューと **「スペースの役割」**メニューの両方の「監査員」を選択します。Cloud Foundry 許可を設定すると、ユーザーは組織で使用可能なサービスを表示できるようになりますが、変更はできません。

## バケットに対するアクセス権限の開発者への付与
{: #gs-bucket-policy}

  1. **「管理」**メニューにナビゲートし、**「アクセス (IAM)」**>**「サービス ID」**のリンクをたどります。ここでは、アカウントにバインドされた抽象化 ID として機能する_サービス ID_ を作成できます。サービス ID には、API キーを割り当てることができます。サービス ID は、特定の開発者の ID をアプリケーションのプロセスやコンポーネントに結び付けたくない場合に使用されます。
	<img alt="IAM のサービス ID" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`図 5: IAM のサービス ID`
  2. 上記のプロセスを繰り返します。ただし、ステップ 3 では、特定のサービス・インスタンスを選択し、_「リソース・タイプ」_として「バケット」を入力し、_「リソース ID」_として既存のバケットの完全 CRN を入力します。
  3. これで、サービス ID はその特定のバケットにアクセスできます。他のバケットにはアクセスできません。

## 次のステップ
{: #gs-next-steps}

Web ベースのコンソールを介したオブジェクト・ストレージに詳しくなったので、サービス・インスタンスを作成して IAM と対話するための `ibmcloud cos` コマンド・ライン・ユーティリティーや、COS に直接アクセスするための `curl` を使用して、コマンド・ラインから類似したワークフローを実行できます。まずは、[API の概要を確認してください](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。
