---

copyright:
  years: 2017, 2018, 2019
lastupdated: "26-06-2019"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:note: .note}

# IBM Cloud CLI の使用
{: #ic-use-the-ibm-cli}

Cloud Object Storage プラグインは、Object Storage リソースを処理するための API ラッパーによって、 IBM Cloud コマンド・ライン・インターフェース (CLI) を拡張します。

## 前提条件
{: #ic-prerequisites}
* [IBM Cloud](https://cloud.ibm.com/) アカウント
* [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision) のインスタンス
* [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## インストールおよび構成
{: #ic-installation}

このプラグインは、64 ビット・プロセッサー上で稼働する Windows、Linux、および macOS オペレーティング・システムと互換性があります。

`plugin install` コマンドを使用してプラグインをインストールします。

```
ibmcloud plugin install cloud-object-storage
```

プラグインがインストールされたら、[`ibmcloud cos config`](#configure-the-program) コマンドを使用してプラグインを構成できます。これを使用して、認証などを選択し、資格情報、デフォルトのダウンロード場所をプラグインに設定できます。

このプログラムは、ダウンロードしたファイルのデフォルト・ローカル・ディレクトリーを設定したり、デフォルト地域を設定したりする機能も備えています。デフォルトのダウンロード場所を設定するには、`ibmcloud cos config ddl` と入力し、プログラムに有効なファイル・パスを入力します。デフォルト地域を設定するには、`ibmcloud cos config region` と入力し、`us-south` などの地域コードをプログラムに入力します。デフォルトでは、この値は `us-geo` に設定されます。


IAM 認証を使用している場合、一部のコマンドを使用するには CRN を指定する必要があります。CRN を設定するには、`ibmcloud cos config crn` と入力し、CRN を指定します。CRN は、`ibmcloud resource service-instance INSTANCE_NAME` で確認できます。あるいは、Web ベースのコンソールを開き、サイドバーで**「サービス資格情報」**を選択し、新しい資格情報セットを作成する (または既に作成した既存の資格情報ファイルを表示する) こともできます。

`ibmcloud cos config list` と要求して、現在の Cloud Object Storage の資格情報を表示できます。構成ファイルはプラグインによって生成されるため、ファイルを手動で編集しないことをお勧めします。

### HMAC 資格情報
{: #ic-hmac-credentials}

希望する場合は、 API キーの代わりに[サービス ID の HMAC 資格情報](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac)を使用できます。`ibmcloud cos config hmac` を実行して HMAC 資格情報を入力してから、`ibmcloud cos config auth` を使用して許可方式を切り替えます。

独自の API キーを指定してトークン認証を使用する場合は、プログラムが自動的に認証するため、資格情報を指定する必要はありません。
{: note}

いつでも、HMAC 認証と IAM 認証を切り替えるために、`ibmcloud cos config auth` と入力できます。IBM Cloud での認証と許可について詳しくは、[Identity and Access Management の資料](/docs/iam?topic=iam-iamoverview)を参照してください。

## コマンド索引
{: #ic-command-index}

| コマンド |  |  |
| --- | --- | --- |
| [`abort-multipart-upload`](#abort-a-multipart-upload) | [`complete-multipart-upload`](#complete-a-multipart-upload) | [`config`](#configure-the-program) |
| [`copy-object`](#copy-object-from-bucket) | [`create-bucket`](#create-a-new-bucket) | [`create-multipart-upload`](#create-a-new-multipart-upload) |
| [`delete-bucket`](#delete-an-existing-bucket) | [`delete-bucket-cors`](#delete-bucket-cors) | [`delete-object`](#delete-an-object) |
| [`delete-objects`](#delete-multiple-objects) | [`download`](#download-objects-using-s3manager) | [`get-bucket-class`](#get-a-buckets-class) | 
| [`get-bucket-cors`](#get-bucket-cors) | [`get-bucket-location`](#find-a-bucket) | [`get-object`](#download-an-object) |
| [`head-bucket`](#get-a-buckets-headers) | [`head-object`](#get-an-objects-headers) | [`list-buckets`](#list-all-buckets) | 
| [`list-buckets-extended`](#extended-bucket-listing) | [`list-multipart-uploads`](#list-in-progress-multipart-uploads) | [`list-objects`](#list-objects) |
| [`list-parts`](#list-parts) | [`put-bucket-cors`](#set-bucket-cors) | [`put-object`](#upload-an-object) |
| [`upload`](#upload-objects-using-s3manager) | [`upload-part`](#upload-a-part) | [`upload-part-copy`](#upload-a-part-copy) |
| [`wait`](#wait) |  |  |

以下にリストされている各操作では、処理内容、使用方法、およびオプション・パラメーターや必須パラメーターが説明されています。リストされているすべてのパラメーターは、オプションと示されていない限り、必須です。

CLI プラグインでは、Aspera High-Speed Transfer、Immutable Object Storage、Key Protect バケットの作成、Bucket Firewalls など、Object Storage で使用可能な完全な機能スイートはサポートされません。
{: note}

### マルチパート・アップロードの中止
{: #ic-abort-multipart-upload}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットへのアップロードを終了して、マルチパート・アップロード・インスタンスを中止します。
* **使用法:** `ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* マルチパート・アップロードを識別するアップロード ID。
		* フラグ: `--upload-id ID`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### マルチパート・アップロードの完了
{: #ic-complete-multipart-upload}
* **アクション:** 現在アップロードされているパートをアセンブルし、そのファイルをユーザーの IBM Cloud Object Storage アカウント内のバケットにアップロードして、マルチパート・アップロード・インスタンスを完了します。
* **使用法:** `ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* マルチパート・アップロードを識別するアップロード ID。
		* フラグ: `--upload-id ID`
	* 設定する MultipartUpload の構造 (STRUCTURE)。
		* フラグ: `--multipart-upload STRUCTURE`
		* 省略構文:  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* JSON 構文:  
	`--multipart-upload file://<filename.json>`  
	`--multipart-upload` コマンドは、完全なファイルに再アセンブルする必要があるマルチパート・アップロードのパートを記述する JSON 構造を取ります。この例では、`file ://` 接頭部を使用して、指定したファイルから JSON 構造をロードします。
		```
			{
  			"Parts": [
    			{
     			 "ETag": "string",
     			 "PartNumber": integer
    			}
    			...
  				]
			}
		```
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


## マルチパート・アップロードの手動制御
{: #ic-manual-multipart-uploads}

IBM Cloud Object Storage CLI は、ユーザーがマルチパート・アップロード機能を使用して大容量ファイルを複数のパートに分けてアップロードする機能を提供します。新規マルチパート・アップロードを開始するには、`create-multipart-upload` コマンドを実行します。これにより、新規アップロード・インスタンスのアップロード ID が返されます。アップロード・プロセスを続行するには、後続の各コマンドのためにアップロード ID を保存する必要があります。

`complete-multipart-upload` コマンドを実行したら、アップロードするファイル・パートごとに `upload-part` を実行します。**マルチパート・アップロードでは、各ファイル・パート (最後のパートを除く) のサイズは 5 MB 以上でなければなりません。**ファイルを別々のパートに分割するには、端末ウィンドウで `split` を実行します。例えば、デスクトップ上に `TESTFILE` という名前の 13 MB のファイルがあり、それぞれ 5 MB のファイル・パートに分割する場合は、`split -b 3m ~/Desktop/TESTFILE part-file-` を実行できます。このコマンドは、3 つのファイル・パート (それぞれ 5 MB の 2 つのファイル・パートと、1 つの 3 MB のファイル・パート) を `part-file-aa`、`part-file-ab`、および `part-file-ac` という名前で生成します。

各ファイル・パートがアップロードされると、CLI はその ETag を出力します。この ETag をパート番号とともに、フォーマット設定した JSON ファイルに保存する必要があります。以下のテンプレートを使用して、独自の ETag JSON データ・ファイルを作成してください。

```
{
  "Parts": [
    			{
      "PartNumber": 1,
      "ETag": "The ETag of the first file part goes here."
    },
    {
      "PartNumber": 2,
      "ETag": "The ETag of the second file part goes here."
    }
  ]
}
```

必要に応じて、この JSON テンプレートにさらに項目を追加します。

マルチパート・アップロード・インスタンスの状況を確認するために、バケット名、キー、およびアップロード ID を指定していつでも `upload-part` コマンドを実行できます。これにより、マルチパート・アップロード・インスタンスに関する生情報が出力されます。ファイルの各パートのアップロードが完了したら、必要なパラメーターを指定して `complete-multipart-upload` コマンドを実行します。すべてが正常に行われた場合、ファイルが目的のバケットに正常にアップロードされたことを示す確認が返されます。

### プログラムの構成
{: #ic-config}
* **アクション:** プログラムの設定を構成します。
* **使用法:** `ibmcloud cos config [COMMAND]`
* **コマンド:**
	* HMAC と IAM の認証の間で切り替えます。
		* コマンド: `auth`
	* CRN を構成に保管します。
		* コマンド: `crn`
	* デフォルトのダウンロード場所を構成に保管します。
		* コマンド: `ddl`
	* HMAC 資格情報を構成に保管します。
		* コマンド: `hmac`
	* 構成をリストします。
		* コマンド: `list`
	* デフォルト地域を構成に保管します。
		* コマンド: `region`
	* VHost とパス URL スタイルの間で切り替えます。
		* コマンド: `url-style`


### バケットからのオブジェクトのコピー
{: #ic-copy-object}
* **アクション:** オブジェクトをソース・バケットから宛先バケットにコピーします。
* **使用法:** `ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* (SOURCE) スラッシュ (/) で区切られた、ソース・バケットの名前とソース・オブジェクトのキー名。URL エンコードされていなければなりません。
		* フラグ: `--copy-source SOURCE`
	* _オプション_: 要求および応答チェーンの `CACHING_DIRECTIVES` を指定します。
		* フラグ: `--cache-control CACHING_DIRECTIVES`
	* _オプション_: 表示情報 (`DIRECTIVES`) を指定します。
		* フラグ: `--content-disposition DIRECTIVES`
	* _オプション_: オブジェクトに適用されているコンテンツ・エンコード (CONTENT_ENCODING) を指定し、それによって Content-Type ヘッダー・フィールドで参照されている media-type を取得するために適用する必要があるデコード・メカニズムを指定します。
		* フラグ: `--content-encoding CONTENT_ENCODING`
	* _オプション_: コンテンツの言語 (LANGUAGE)。
		* フラグ: `--content-language LANGUAGE`
	* _オプション_: オブジェクト・データの形式を記述する標準 MIME タイプ。
		* フラグ: `--content-type MIME`
	* _オプション_: オブジェクトのエンティティー・タグ (Etag) が指定したタグ (ETAG) と一致している場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-match ETAG`
	* _オプション_: 指定した時刻 (TIMESTAMP) 以降にオブジェクトが変更されている場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-modified-since TIMESTAMP`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定したタグ (ETAG) と異なる場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-none-match ETAG`
	* _オプション_: 指定した時刻 (TIMESTAMP) 以降にオブジェクトが変更されていない場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-unmodified-since TIMESTAMP`
	* _オプション_: 保管するメタデータのマップ (MAP)。構文: KeyName1=string,KeyName2=string
		* フラグ: `--metadata MAP`
	* _オプション_: メタデータをソース・オブジェクトからコピーするのか、それとも要求で指定されたメタデータで置き換えるかのかを指定します。DIRECTIVE の値: COPY、REPLACE。
		* フラグ: ` --metadata-directive DIRECTIVE`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### 新規バケットの作成
{: #ic-create-bucket}

* **アクション:** IBM Cloud Object Storage インスタンスにバケットを作成します。
* **使用法:** `ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* なお、IAM 認証を使用する場合は CRN を指定する必要があります。これは、[`ibmcloud cos config crn`](#configure-the-program) コマンドを使用して設定できます。
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: クラスの名前。
		* フラグ: `--class CLASS_NAME`
	* _オプション_: IBM サービス・インスタンス ID を要求に設定します。
		* フラグ: `--ibm-service-instance-id ID`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`



### 新規マルチパート・アップロードの作成
{: #ic-create-multipart-upload}
* **アクション:** 新規マルチパート・アップロード・インスタンスを作成して、マルチパート・ファイル・アップロード・プロセスを開始します。
* **使用法:** `ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* _オプション_: 要求および応答チェーンの `CACHING_DIRECTIVES` を指定します。
		* フラグ: `--cache-control CACHING_DIRECTIVES`
	* _オプション_: 表示情報 (`DIRECTIVES`) を指定します。
		* フラグ: `--content-disposition DIRECTIVES`
	* _オプション_: オブジェクトのコンテンツ・エンコード (`CONTENT_ENCODING`) を指定します。
		* フラグ: `--content-encoding CONTENT_ENCODING`
	* _オプション_: コンテンツの言語 (LANGUAGE)。
		* フラグ: `--content-language LANGUAGE`
	* _オプション_: オブジェクト・データの形式を記述する標準 MIME タイプ。
		* フラグ: `--content-type MIME`
	* _オプション_: 保管するメタデータのマップ (MAP)。構文: KeyName1=string,KeyName2=string
		* フラグ: `--metadata MAP`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### 既存のバケットの削除
{: #ic-delete-bucket}

* **アクション:** IBM Cloud Object Storage インスタンス内の既存のバケットを削除します。
* **使用法:** `ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
    * _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
       * フラグ: `--region REGION`
    * _オプション_: この操作では確認は求められません。
       * フラグ: `--force`
    * _オプション_: 未加工 JSON 形式で返される出力。
       * フラグ: `--json`


### バケット CORS の削除
{: #ic-delete-bucket-cors}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットの CORS 構成を削除します。
* **使用法:** `ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### オブジェクトの削除
{: #ic-delete-object}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットから 1 つのオブジェクトを削除します。
* **使用法:** `ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
  * _オプション_: この操作では確認は求められません。
  	* フラグ: `--force`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### 複数のオブジェクトの削除
{: #ic-delete-objects}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットから複数のオブジェクトを削除します。
* **使用法:** `ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。  
		* フラグ: `--bucket BUCKET_NAME`  
	* 省略構文または JSON 構文のいずれかを使用する構造 (STRUCTURE)。  
		* フラグ: `--delete STRUCTURE`  
		* 省略構文:  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* JSON 構文:  
	`--delete file://<filename.json>`  
	`--delete` コマンドは、完全なファイルに再アセンブルする必要があるマルチパート・アップロードのパートを記述する JSON 構造を取ります。この例では、`file ://` 接頭部を使用して、指定したファイルから JSON 構造をロードします。
		```
	{
  	"Objects": [
    	{
    	"Key": "string",
    	"VersionId": "string"
    	}
    ...
  	],
  	"Quiet": true|false
	}
	```
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### S3Manager を使用したオブジェクトのダウンロード
{: #ic-download-s3manager}
* **アクション:** 並行して S3 からオブジェクトをダウンロードします。
* **使用法:** `ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **指定するパラメーター:**
	* バケットの名前 (BUCKET_NAME)。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* _オプション_: パートの送信時にアップロードの呼び出しごとに並行して開始する goroutine の数。デフォルト値は 5 です。
		* フラグ: `--concurrency value`
	* _オプション_: データをチャンクにバッファリングして S3 へのパートにするときに使用するバイト単位のバッファー・サイズ (SIZE)。許可される最小パート・サイズは 5MB です。
		* フラグ: `--part-size SIZE`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定した ETAG と同じ場合にのみオブジェクトを返します。条件に合わない場合は、412 (前提条件不適合) を返します。
		* フラグ: `--if-match ETAG`
	* _オプション_: 指定した TIMESTAMP 以降にオブジェクトが変更された場合にのみオブジェクトを返します。条件に合わない場合は、304 (変更なし) を返します。
		* フラグ: `--if-modified-since TIMESTAMP`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定した ETAG と異なる場合にのみオブジェクトを返します。条件に合わない場合は、304 (変更なし) を返します。
		* フラグ: `--if-none-match ETAG`
	* _オプション_: 指定した TIMESTAMP 以降にオブジェクトが変更されていない場合にのみオブジェクトを返します。条件に合わない場合は、412 (前提条件不適合) を返します。
		* フラグ: `--if-unmodified-since TIMESTAMP`
	* _オプション_: オブジェクトの指定した RANGE バイトをダウンロードします。HTTP Range ヘッダーについて詳しくは、[ここをクリック](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35)してください。
		* フラグ: `--range RANGE`
	* _オプション_: 応答の Cache-Control ヘッダー (HEADER) を設定します。
		* フラグ: `--response-cache-control HEADER`
	* _オプション_: 応答の Content-Disposition ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-disposition HEADER`
	* _オプション_: 応答の Content-Encoding ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-encoding HEADER`
	* _オプション_: 応答の Content-Language ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-language HEADER`
	* _オプション_: 応答の Content-Type ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-type HEADER`
	* _オプション_: 応答の Expires ヘッダー (HEADER) を設定します。
		* フラグ: `--response-expires HEADER`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`
	* _オプション_: オブジェクトのコンテンツを保存する場所。このパラメーターが指定されていない場合、プログラムはデフォルトの場所を使用します。
		* パラメーター: `OUTFILE`


### バケットのクラスの取得
{: #ic-bucket-class}
* **アクション:** IBM Cloud Object Storage インスタンス内のバケットのクラスを判別します。
* **使用法:** `ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### バケット CORS の取得
{: #ic-get-bucket-cors}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットの CORS 構成を返します。
* **使用法:** `ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **指定するパラメーター:**
  * バケットの名前。  
    * フラグ: `--bucket BUCKET_NAME`
  * _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
    * フラグ: `--region REGION`
  * _オプション_: 未加工 JSON 形式で返される出力。
    * フラグ: `--json`


### バケットの検出
{: #ic-find-bucket}
* **アクション:** IBM Cloud Object Storage インスタンス内のバケットの地域およびクラスを判別します。 
* **使用法:** `ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`
	


### オブジェクトのダウンロード
{: #ic-download-object}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットから 1 つのオブジェクトをダウンロードします。
* **使用法:** `ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定した ETAG と同じ場合にのみオブジェクトを返します。条件に合わない場合は、412 (前提条件不適合) を返します。
		* フラグ: `--if-match ETAG`
	* _オプション_: 指定した TIMESTAMP 以降にオブジェクトが変更された場合にのみオブジェクトを返します。条件に合わない場合は、304 (変更なし) を返します。
		* フラグ: `--if-modified-since TIMESTAMP`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定した ETAG と異なる場合にのみオブジェクトを返します。条件に合わない場合は、304 (変更なし) を返します。
		* フラグ: `--if-none-match ETAG`
	* _オプション_: 指定した TIMESTAMP 以降にオブジェクトが変更されていない場合にのみオブジェクトを返します。条件に合わない場合は、412 (前提条件不適合) を返します。
		* フラグ: `--if-unmodified-since TIMESTAMP`
	* _オプション_: オブジェクトの指定した RANGE バイトをダウンロードします。
		* フラグ: `--range RANGE`
	* _オプション_: 応答の Cache-Control ヘッダー (HEADER) を設定します。
		* フラグ: `--response-cache-control HEADER`
	* _オプション_: 応答の Content-Disposition ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-disposition HEADER`
	* _オプション_: 応答の Content-Encoding ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-encoding HEADER`
	* _オプション_: 応答の Content-Language ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-language HEADER`
	* _オプション_: 応答の Content-Type ヘッダー (HEADER) を設定します。
		* フラグ: `--response-content-type HEADER`
	* _オプション_: 応答の Expires ヘッダー (HEADER) を設定します。
		* フラグ: `--response-expires HEADER`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`
	* _オプション_: オブジェクトのコンテンツを保存する場所。このパラメーターが指定されていない場合、プログラムはデフォルトの場所を使用します。
		* パラメーター: `OUTFILE`


### バケットのヘッダーの取得
{: #ic-bucket-header}
* **アクション:** IBM Cloud Object Storage インスタンス内にバケットが存在しているかどうかを判別します。
* **使用法:** `ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### オブジェクトのヘッダーの取得
{: #ic-object-header}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットにファイルが存在しているかどうかを判別します。
* **使用法:** `ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定した ETAG と同じ場合にのみオブジェクトを返します。条件に合わない場合は、412 (前提条件不適合) を返します。
		* フラグ: `--if-match ETAG`
	* _オプション_: 指定した TIMESTAMP 以降にオブジェクトが変更された場合にのみオブジェクトを返します。条件に合わない場合は、304 (変更なし) を返します。
		* フラグ: `--if-modified-since TIMESTAMP`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定した ETAG と異なる場合にのみオブジェクトを返します。条件に合わない場合は、304 (変更なし) を返します。
		* フラグ: `--if-none-match ETAG`
	* _オプション_: 指定した TIMESTAMP 以降にオブジェクトが変更されていない場合にのみオブジェクトを返します。条件に合わない場合は、412 (前提条件不適合) を返します。
		* フラグ: `--if-unmodified-since TIMESTAMP`
	* オブジェクトの指定した RANGE バイトをダウンロードします。
		* フラグ: `--range RANGE`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### すべてのバケットのリスト表示
{: #ic-list-buckets}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のすべてのバケットのリストを出力します。バケットは、異なる地域に存在している可能性があります。
* **使用法:** `ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* なお、IAM 認証を使用する場合は CRN を指定する必要があります。これは、[`ibmcloud cos config crn`](#configure-the-program) コマンドを使用して設定できます。
* **指定するパラメーター:**
  * 指定するパラメーターはありません。
	* _オプション_: IBM サービス・インスタンス ID を要求に設定します。
		* フラグ: `--ibm-service-instance-id`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### 拡張バケット・リスト表示
{: #ic-extended-bucket-listing}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のすべてのバケットのリストを出力します。バケットは、異なる地域に存在している可能性があります。
* **使用法:** `ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* なお、IAM 認証を使用する場合は CRN を指定する必要があります。これは、[`ibmcloud cos config crn`](#configure-the-program) コマンドを使用して設定できます。
* **指定するパラメーター:**
  * 指定するパラメーターはありません。
	* _オプション_: IBM サービス・インスタンス ID を要求に設定します。
		* フラグ: `--ibm-service-instance-id`
	* _オプション_: バケット内のオブジェクトをリストする際の開始キー (KEY) を指定します。
		* フラグ: `--marker KEY`
	* _オプション_: 指定した接頭部 (PREFIX) で始まるキーに応答を制限します。
		* フラグ: `--prefix PREFIX`
	* _オプション_: サービス呼び出しで取得する各ページのサイズ (SIZE)。これは、コマンドの出力として返される項目の数には影響しません。ページ・サイズをより小さく設定することで、各呼び出しで取得する項目が少なくなり、サービスに送られる呼び出し数が増えることになります。これにより、サービス呼び出しのタイムアウトを防ぐことができます。
		* フラグ: `--page-size SIZE`
	* _オプション_: コマンドの出力として返す項目の総数 (NUMBER)。
		* フラグ: `--max-items NUMBER`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### 進行中のマルチパート・アップロードのリスト表示
{: #ic-list-multipart-uploads}
* **アクション:** 進行中のマルチパート・アップロードをリストします。
* **使用法:** `ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: 区切り文字 (DELIMITER) は、キーをグループ化するために使用する文字です。
		* フラグ: `--delimiter DELIMITER`
	* _オプション_: 応答内のオブジェクト・キーをエンコードするように要求し、使用するエンコード方式 (METHOD) を指定します。
		* フラグ: `--encoding-type METHOD`
	* _オプション_: 指定した接頭部 (PREFIX) で始まるキーに応答を制限します。
		* フラグ: `--prefix PREFIX`
	* _オプション_: upload-id-marker と共に、このパラメーターはリスト表示が開始する前のマルチパート・アップロードを指定します。
		* フラグ: `--key-marker value`
	* _オプション_: key-marker と共に、リスト表示が開始する前のマルチパート・アップロードを指定します。key-marker が指定されていない場合、upload-id-marker パラメーターは無視されます。
		* フラグ: `--upload-id-marker value`
	* _オプション_: サービス呼び出しで取得する各ページのサイズ (SIZE)。これは、コマンドの出力として返される項目の数には影響しません。ページ・サイズをより小さく設定することで、各呼び出しで取得する項目が少なくなり、サービスに送られる呼び出し数が増えることになります。これにより、サービス呼び出しのタイムアウトを防ぐことができます。(デフォルト: 1000)。
		* フラグ: `--page-size SIZE`
	* _オプション_: コマンドの出力として返す項目の総数 (NUMBER)。対象項目の合計数が指定した値を超える場合、コマンドの出力に NextToken が提供されます。ページ編集を再開するには、以後のコマンドの starting-token 引数内に NextToken 値を指定します。(デフォルト: 0)。
		* フラグ: `--max-items NUMBER`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### オブジェクトのリスト表示
{: #ic-list-objects}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットに存在するファイルをリストします。この操作は現在、最近作成された 1000 個のオブジェクトに制限されており、フィルタリングできません。
* **使用法:** `ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: 区切り文字 (DELIMITER) は、キーをグループ化するために使用する文字です。
		* フラグ: `--delimiter DELIMITER`
	* _オプション_: 応答内のオブジェクト・キーをエンコードするように要求し、使用するエンコード方式 (METHOD) を指定します。
		* フラグ: `--encoding-type METHOD`
	* _オプション_: 指定した接頭部 (PREFIX) で始まるキーに応答を制限します。
		* フラグ: `--prefix PREFIX`
	* _オプション_: ページ編集を開始する場所を指定するトークン (TOKEN)。これは以前に切り捨てられた応答からの NextToken です。
		* フラグ: `--starting-token TOKEN`
	* _オプション_: サービス呼び出しで取得する各ページのサイズ (SIZE)。これは、コマンドの出力として返される項目の数には影響しません。ページ・サイズをより小さく設定することで、各呼び出しで取得する項目が少なくなり、サービスに送られる呼び出し数が増えることになります。これにより、サービス呼び出しのタイムアウトを防ぐことができます。(デフォルト: 1000)
		* フラグ: `--page-size SIZE`
	* _オプション_: コマンドの出力として返す項目の総数 (NUMBER)。対象項目の合計数が指定した値を超える場合、コマンドの出力に NextToken が提供されます。ページ編集を再開するには、以後のコマンドの starting-token 引数内に NextToken 値を指定します。(デフォルト: 0)
		* フラグ: `--max-items NUMBER`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### パートのリスト
{: #ic-list-parts}
* **アクション:** 進行中のマルチパート・アップロード・インスタンスに関する情報を出力します。
* **使用法:** `ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* マルチパート・アップロードを識別するアップロード ID。
		* フラグ: `--upload-id ID`
	* リスト表示を開始する前のパート番号値 (VALUE) (デフォルト: 1)
		* フラグ: `--part-number-marker VALUE`
	* _オプション_: サービス呼び出しで取得する各ページのサイズ (SIZE)。これは、コマンドの出力として返される項目の数には影響しません。ページ・サイズをより小さく設定することで、各呼び出しで取得する項目が少なくなり、サービスに送られる呼び出し数が増えることになります。これにより、サービス呼び出しのタイムアウトを防ぐことができます。(デフォルト: 1000)
		* フラグ: `--page-size SIZE`
	* _オプション_: コマンドの出力として返す項目の総数 (NUMBER)。対象項目の合計数が指定した値を超える場合、コマンドの出力に NextToken が提供されます。ページ編集を再開するには、以後のコマンドの starting-token 引数内に NextToken 値を指定します。(デフォルト: 0)
		* フラグ: `--max-items NUMBER`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### バケット CORS の設定
{: #ic-set-bucket-cors}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットの CORS 構成を設定します。
* **使用法:** `ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* _オプション_: ファイル内の JSON 構文を使用する構造 (STRUCTURE)。
		* フラグ: `--cors-configuration STRUCTURE`
		* JSON 構文:  
	`--cors-configuration file://<filename.json>`  
	`--cors-configuration` コマンドは、完全なファイルに再アセンブルする必要があるマルチパート・アップロードのパートを記述する JSON 構造を取ります。この例では、`file ://` 接頭部を使用して、指定したファイルから JSON 構造をロードします。
	```
	{
  	"CORSRules": [
    	{
      	"AllowedHeaders": ["string", ...],
      	"AllowedMethods": ["string", ...],
      	"AllowedOrigins": ["string", ...],
      	"ExposeHeaders": ["string", ...],
      	"MaxAgeSeconds": integer
    	}
    	...
  	]
	}
	```
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`



### オブジェクトの配置
{: #ic-upload-object}
* **アクション:** ユーザーの IBM Cloud Object Storage アカウント内のバケットにオブジェクトをアップロードします。
* **使用法:** `ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **指定するパラメーター:**
    * バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* _オプション_: オブジェクト・データの場所 (`FILE_PATH`)。
		* フラグ: `--body FILE_PATH`
	* _オプション_: 要求および応答チェーンの `CACHING_DIRECTIVES` を指定します。
		* フラグ: `--cache-control CACHING_DIRECTIVES`
	* _オプション_: 表示情報 (`DIRECTIVES`) を指定します。
		* フラグ: `--content-disposition DIRECTIVES`
	* _オプション_: オブジェクトのコンテンツ・エンコード (`CONTENT_ENCODING`) を指定します。
		* フラグ: `--content-encoding CONTENT_ENCODING`
	* _オプション_: コンテンツの言語 (LANGUAGE)。
		* フラグ: `--content-language LANGUAGE`
	* _オプション_: 本文のバイト単位のサイズ (SIZE)。このパラメーターは、本文のサイズを自動的に判定できない場合に役立ちます。(デフォルト: 0)
		* フラグ: `--content-length SIZE`
	* _オプション_: データの Base64 エンコードの 128 ビット MD5 ダイジェスト。
		* フラグ: `--content-md5 MD5`
	* _オプション_: オブジェクト・データの形式を記述する標準 MIME タイプ。
		* フラグ: `--content-type MIME`
	* _オプション_: 保管するメタデータのマップ (MAP)。構文: KeyName1=string,KeyName2=string
		* フラグ: `--metadata MAP`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### S3Manager を使用したオブジェクトのアップロード
{: #ic-upload-s3manager}
* **アクション:** 並行して S3 からオブジェクトをアップロードします。
* **使用法:** `ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **指定するパラメーター:**
	* バケットの名前 (BUCKET_NAME)。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* アップロードするファイルのパス (PATH)。
		* フラグ: `--file PATH`
	* _オプション_: パートの送信時にアップロードの呼び出しごとに並行して開始する goroutine の数。デフォルト値は 5 です。
		* フラグ: `--concurrency value`
	* _オプション_: アップロード対象のオブジェクトのパート・サイズを計算する、S3 にアップロードされるパートの最大数 (PARTS)。制限は 10,000 パートです。
		* フラグ: `--max-upload-parts PARTS`
	* _オプション_: データをチャンクにバッファリングして S3 へのパートにするときに使用するバイト単位のバッファー・サイズ (SIZE)。許可される最小パート・サイズは 5MB です。
		* フラグ: `--part-size SIZE`
	* _オプション_: この値を true に設定すると、失敗時に SDK が AbortMultipartUpload を呼び出さなくなり、正常にアップロードされたすべてのパートは手動での復旧用に S3 に置かれたままになります。
		* フラグ: `--leave-parts-on-errors`
	* _オプション_: 要求/応答チェーンの CACHING_DIRECTIVES を指定します。
		* フラグ: `--cache-control CACHING_DIRECTIVES`
	* _オプション_: 表示情報 (DIRECTIVES) を指定します。
		* フラグ: `--content-disposition DIRECTIVES`
	* _オプション_: オブジェクトに適用されているコンテンツ・エンコード (CONTENT_ENCODING) を指定し、それによって Content-Type ヘッダー・フィールドで参照されている media-type を取得するために適用する必要があるデコード・メカニズムを指定します。
		* フラグ: `--content-encoding CONTENT_ENCODING`
	* _オプション_: コンテンツの言語 (LANGUAGE)。
		* フラグ: `--content-language LANGUAGE`
	* _オプション_: 本文のバイト単位のサイズ (SIZE)。このパラメーターは、本文のサイズを自動的に判定できない場合に役立ちます。
		* フラグ: `--content-length SIZE`
	* _オプション_: データの Base64 エンコードの 128 ビット MD5 ダイジェスト。
		* フラグ: `--content-md5 MD5`
	* _オプション_: オブジェクト・データの形式を記述する標準 MIME タイプ。
		* フラグ: `--content-type MIME`
	* _オプション_: 保管するメタデータのマップ (MAP)。構文: KeyName1=string,KeyName2=string
		* フラグ: `--metadata MAP`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### パートのアップロード
{: #ic-upload-part}
* **アクション:** 既存のマルチパート・アップロード・インスタンスにファイルのパートをアップロードします。
* **使用法:** `ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* なお、それぞれのアップロードしたファイル・パートの番号と各パートの ETag (CLI で自動的に出力される) を JSON ファイルに保存する必要があります。詳しくは、下記の『マルチパート・アップロード・ガイド』を参照してください。
* **指定するパラメーター:**
	* マルチパート・アップロードが実行されているバケット名。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* マルチパート・アップロードを識別するアップロード ID。
		* フラグ: `--upload-id ID`
	* アップロードしているパートのパート番号 (NUMBER)。これは、1 から 10,000 の範囲の正整数です。(デフォルト: 1)
		* フラグ: `--part-number NUMBER`
	* _オプション_: オブジェクト・データの場所 (`FILE_PATH`)。
		* フラグ: `--body FILE_PATH`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### パート・コピーのアップロード
{: #ic-upload-a-part-copy}
* **アクション:** 既存のオブジェクトからデータをコピーすることで、パートをアップロードします。
* **使用法:** `ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* なお、それぞれのアップロードしたファイル・パートの番号と各パートの ETag (CLI で自動的に出力される) を JSON ファイルに保存する必要があります。詳しくは、『マルチパート・アップロード・ガイド』を参照してください。
* **指定するパラメーター:**
	* バケットの名前。
		* フラグ: `--bucket BUCKET_NAME`
	* オブジェクトのキー。
		* フラグ: `--key KEY`
	* マルチパート・アップロードを識別するアップロード ID。
		* フラグ: `--upload-id ID`
	* アップロードしているパートのパート番号 (NUMBER)。これは 1 から 10,000 までの正整数です。
		* フラグ: `--part-number PART_NUMBER`
	* (SOURCE) スラッシュ (/) で区切られた、ソース・バケットの名前とソース・オブジェクトのキー名。URL エンコードされていなければなりません。
		* フラグ: `--copy-source SOURCE`
	* _オプション_: オブジェクトのエンティティー・タグ (Etag) が指定したタグ (ETAG) と一致している場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-match ETAG`
	* _オプション_: 指定した時刻 (TIMESTAMP) 以降にオブジェクトが変更されている場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-modified-since TIMESTAMP`
	* _オプション_: オブジェクトのエンティティー・タグ (ETag) が指定したタグ (ETAG) と異なる場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-none-match ETAG`
	* _オプション_: 指定した時刻 (TIMESTAMP) 以降にオブジェクトが変更されていない場合にオブジェクトをコピーします。
		* フラグ: `--copy-source-if-unmodified-since TIMESTAMP`
	* _オプション_: ソース・オブジェクトからコピーするバイトの範囲。範囲値は bytes=first-last の形式にする必要があります。ここで、first と last はコピー対象を示すゼロで開始するバイト・オフセットです。例えば、bytes=0-9 は、ソースの最初の 10 バイトをコピーすることを意味します。ソース・オブジェクトが 5 MB を超える場合のみ、範囲コピーを行うことができます。
		* フラグ: `--copy-source-range value`
	* _オプション_: バケットが存在する地域 (REGION)。このフラグを設定しなかった場合、プログラムは構成に指定されたデフォルト・オプションを使用します。
		* フラグ: `--region REGION`
	* _オプション_: 未加工 JSON 形式で返される出力。
		* フラグ: `--json`


### 待機
{: #ic-wait}
* **アクション:** 特定の条件が満たされるまで待機します。各サブコマンドは、リストされた要件が満たされるまで API をポーリングします。
* **使用法:** `ibmcloud cos wait command [arguments...] [command options]`
* **コマンド:**
    * `bucket-exists`
  		* head-bucket でのポーリング中に 200 件の応答を受信するまで待機します。これは、成功状態に到達するまで 5 秒ごとにポーリングします。20 回チェックに失敗すると、戻りコード 255 で終了します。
	* `bucket-not-exists`
		* head-bucket でのポーリング中に 404 応答を受信するまで待機します。これは、成功状態に到達するまで 5 秒ごとにポーリングします。20 回チェックに失敗すると、戻りコード 255 で終了します。
	* `object-exists`
		* head-object でのポーリング中に 200 応答を受信するまで待機します。これは、成功状態に到達するまで 5 秒ごとにポーリングします。20 回チェックに失敗すると、戻りコード 255 で終了します。
	* `object-not-exists`
		* head-object でのポーリング中に 404 応答を受信するまで待機します。これは、成功状態に到達するまで 5 秒ごとにポーリングします。20 回チェックに失敗すると、戻りコード 255 で終了します。

