---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: python, sdk

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

# Python の使用
{: #python}

Python サポートは、`boto3` ライブラリーの fork から提供されます。`pip install ibm-cos-sdk` を使用して、Python Package Index からインストールできます。

ソース・コードは、[GitHub](https://github.com/ibm/ibm-cos-sdk-python/) にあります。

`ibm_boto3` ライブラリーは、{{site.data.keyword.cos_full}} API のすべてにアクセスできます。以下の基本的な例に示すように、サービス・リソース (下位クライアント) の作成時は、エンドポイント、API キー、およびインスタンス ID を指定しなければなりません。

サービス・インスタンス ID は、_リソース・インスタンス ID_ とも呼ばれます。この値は、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)を作成するか、CLI を介して見つけることができます。
{:tip}

詳細な資料については、[ここ](https://ibm.github.io/ibm-cos-sdk-python/)を参照してください。

## 1.x.x からのアップグレード
{: #python-migrate}

2.0 バージョンの SDK では、名前空間の変更が導入されました。この変更により、アプリケーションは元の `boto3` ライブラリーを使用して、同じアプリケーションまたは環境内の AWS リソースに接続できます。1.x から 2.0 にマイグレーションするには、いくつかの変更が必要です。

    1. `requirements.txt` を更新するか、PyPI から `pip install -U ibm-cos-sdk` を使用します。`pip list | grep ibm-cos` を使用して、古いバージョンが存在しないことを確認します。
    2. すべてのインポート宣言を `boto3` から `ibm_boto3` に更新します。
    3. 必要に応じて、元の `boto3` を再インストールします。その際は、`requirements.txt` を更新するか、PyPI から `pip install boto3` を使用します。

## クライアントの作成と資格情報の入手
{: #python-credentials}

COS に接続するために、資格情報 (API キーとサービス・インスタンス ID) を指定することによりクライアントが作成および構成されます。これらの値は、資格情報ファイルまたは環境変数から自動的に入手することもできます。

[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)を生成した後は、結果の JSON 文書を `~/.bluemix/cos_credentials` に保存できます。クライアントの作成時に他の資格情報が明示的に設定されない限り、SDK は、このファイルから資格情報を自動的に入手します。`cos_credentials` ファイルに HMAC 鍵が含まれている場合、クライアントは署名を使用して認証を受けます。そうでない場合、クライアントはベアラー・トークンと提供される API キーを使用して認証を受けます。

AWS S3 からマイグレーションする場合は、`~/.aws/credentials` から資格情報データを以下の形式で入手することもできます。

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

`~/.bluemix/cos_credentials` と `~/.aws/credentials` の両方が存在する場合は、`cos_credentials` が優先されます。

### 必要な情報の収集
{: #python-prereqs}

この後の例には、以下の変数が現れます。

* `bucket_name` は、[固有かつ DNS セーフの](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)ストリングでなければなりません。バケット名はシステム全体で固有であるため、この例を複数回実行する場合は、これらの値を変更する必要があります。名前は、削除後も 10 分から 15 分の間は予約済みになることに注意してください。
* `ibm_api_key_id` は、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)内で `apikey` として検出される値です。
* `ibm_service_instance_id` は、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)内で `resource_instance_id` として検出される値です。 
* `endpoint_url` は、`https://` プロトコルを含むサービス・エンドポイント URL です。この値は、[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)内で検出される `endpoints` の値では**ありません**。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
* `LocationConstraint` は、`endpoint` 値に対応する[有効なプロビジョニング・コード](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)です。 


## コード例
{: #python-examples}

サンプル・コードは、**Python 2.7.15** を使用して書かれたものです。

### 構成の初期化
{: #python-examples-init}

  
```python
import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>" # eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Create resource
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)
```
*キー値*
* `<endpoint>` - スキーマの接頭部 ('https://') が付いた Cloud Object Storage のパブリック・エンドポイント ([IBM Cloud ダッシュボード](https://cloud.ibm.com/resources){:new_window} から入手可能)。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
* `<api-key>` - サービス資格情報の作成時に生成される API キー (作成および削除の例では、書き込みアクセス権限が必要です)
* `<resource-instance-id>` - Cloud Object Storage のリソース ID ([IBM Cloud CLI](/docs/cli?topic=cloud-cli-idt-cli) または [IBM Cloud ダッシュボード](https://cloud.ibm.com/resources){:new_window} から入手可能)
* `<location>` - Cloud Object Storage のデフォルト・ロケーション (`<endpoint>` に使用される地域と一致しなければなりません)

*SDK リファレンス*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### 新規バケットの作成
{: #python-examples-new-bucket}

`LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラスのガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)を参照してください。

```python
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

*SDK リファレンス*
* クラス
  * [`バケット`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* メソッド
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### 新規テキスト・ファイルの作成
{: #python-examples-new-file}

```python
def create_text_file(bucket_name, item_name, file_text):
    print("Creating new item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).put(
            Body=file_text
        )
        print("Item: {0} created!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create text file: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [`オブジェクト`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* メソッド
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### 使用可能なバケットのリスト
{: #python-examples-list-buckets}

```python
def get_buckets():
    print("Retrieving list of buckets")
    try:
        buckets = cos.buckets.all()
        for bucket in buckets:
            print("Bucket Name: {0}".format(bucket.name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve list buckets: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [バケット](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* コレクション
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### バケット内の項目のリスト
{: #python-examples-list-objects}

```python
def get_bucket_contents(bucket_name):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        files = cos.Bucket(bucket_name).objects.all()
        for file in files:
            print("Item: {0} ({1} bytes).".format(file.key, file.size))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [バケット](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* コレクション
    * [オブジェクト](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### 特定の項目のファイル内容の取得
{: #python-examples-get-file-contents}

```python
def get_item(bucket_name, item_name):
    print("Retrieving item from bucket: {0}, key: {1}".format(bucket_name, item_name))
    try:
        file = cos.Object(bucket_name, item_name).get()
        print("File Contents: {0}".format(file["Body"].read()))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve file contents: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [`オブジェクト`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* メソッド
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### バケットから項目を削除
{: #python-examples-delete-object}

```python
def delete_item(bucket_name, item_name):
    print("Deleting item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).delete()
        print("Item: {0} deleted!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete item: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [オブジェクト](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* メソッド
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### バケットから複数項目を削除
{: #python-examples-delete-multiple-objects}

削除要求には、削除するキーを最大 1000 個含めることができます。これは要求ごとのオーバーヘッドを削減するのに役立ちますが、多数のキーを削除する場合は注意が必要です。また、適切なパフォーマンスを確保するために、オブジェクトのサイズも考慮に入れてください。
{:tip}

```python
def delete_items(bucket_name):
    try:
        delete_request = {
            "Objects": [
                { "Key": "deletetest/testfile1.txt" },
            { "Key": "deletetest/testfile2.txt" },
            { "Key": "deletetest/testfile3.txt" },
            { "Key": "deletetest/testfile4.txt" },
            { "Key": "deletetest/testfile5.txt" }
            ]
        }

        response = cos_cli.delete_objects(
            Bucket=bucket_name,
            Delete=delete_request
        )

        print("Deleted items for {0}\n".format(bucket_name))
        print(json.dumps(response.get("Deleted"), indent=4))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to copy item: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* メソッド
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### バケットの削除
{: #python-examples-delete-bucket}

```python
def delete_bucket(bucket_name):
    print("Deleting bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).delete()
        print("Bucket: {0} deleted!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete bucket: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [バケット](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* メソッド
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### 複数パーツ・アップロードの実行
{: #python-examples-multipart}

#### バイナリー・ファイルのアップロード (推奨される方法)
{: #python-examples-multipart-binary}

[S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} クラスの [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} メソッドは、必要に応じて複数パーツ・アップロードを自動的に実行します。複数パーツ・アップロードを使用する場合のしきい値の決定には、[TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} クラスが使用されます。

```python
def multi_part_upload(bucket_name, item_name, file_path):
    try:
        print("Starting file transfer for {0} to bucket: {1}\n".format(item_name, bucket_name))
        # set 5 MB chunks
        part_size = 1024 * 1024 * 5

        # set threadhold to 15 MB
        file_threshold = 1024 * 1024 * 15

        # set the transfer threshold and chunk size
        transfer_config = ibm_boto3.s3.transfer.TransferConfig(
            multipart_threshold=file_threshold,
            multipart_chunksize=part_size
        )

        # the upload_fileobj method will automatically execute a multi-part upload
        # in 5 MB chunks for all files over 15 MB
        with open(file_path, "rb") as file_data:
            cos.Object(bucket_name, item_name).upload_fileobj(
                Fileobj=file_data,
                Config=transfer_config
            )

        print("Transfer for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [オブジェクト](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* メソッド
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### 手動での複数パーツ・アップロードの実行
{: #python-examples-multipart-manual}

必要な場合は、[S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} クラスを使用して、複数パーツ・アップロードを実行できます。これは、アップロード・プロセスをより正確に制御する必要がある場合に役立つ可能性があります。

```python
def multi_part_upload_manual(bucket_name, item_name, file_path):
    try:
        # create client object
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT
        )

        print("Starting multi-part upload for {0} to bucket: {1}\n".format(item_name, bucket_name))

        # initiate the multi-part upload
        mp = cos_cli.create_multipart_upload(
            Bucket=bucket_name,
            Key=item_name
        )

        upload_id = mp["UploadId"]

        # min 5MB part size
        part_size = 1024 * 1024 * 5
        file_size = os.stat(file_path).st_size
        part_count = int(math.ceil(file_size / float(part_size)))
        data_packs = []
        position = 0
        part_num = 0

        # begin uploading the parts
        with open(file_path, "rb") as file:
            for i in range(part_count):
                part_num = i + 1
                part_size = min(part_size, (file_size - position))

                print("Uploading to {0} (part {1} of {2})".format(item_name, part_num, part_count))

                file_data = file.read(part_size)

                mp_part = cos_cli.upload_part(
                    Bucket=bucket_name,
                    Key=item_name,
                    PartNumber=part_num,
                    Body=file_data,
                    ContentLength=part_size,
                    UploadId=upload_id
                )

                data_packs.append({
                    "ETag":mp_part["ETag"],
                    "PartNumber":part_num
                })

                position += part_size

        # complete upload
        cos_cli.complete_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id,
            MultipartUpload={
                "Parts": data_packs
            }
        )
        print("Upload for {0} Complete!\n".format(item_name))
    except ClientError as be:
        # abort the upload
        cos_cli.abort_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id
        )
        print("Multi-part upload aborted for {0}\n".format(item_name))
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* メソッド
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### TransferManager を使用したラージ・オブジェクトのアップロード
{: #python-examples-multipart-transfer}

`TransferManager` は、必要に応じて構成パラメーターを設定して自動的に複数パーツ・アップロードを取り込むことで、大規模ファイル転送を実行するための新しい方法を提供します。

```python
def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # set the chunk size to 5 MB
    part_size = 1024 * 1024 * 5

    # set threadhold to 5 MB
    file_threshold = 1024 * 1024 * 5

    # Create client connection
cos_cli = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_SERVICE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

    # set the transfer threshold and chunk size in config settings
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # create transfer manager
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_cli, config=transfer_config)

    try:
        # initiate file upload
        future = transfer_mgr.upload(file_path, bucket_name, item_name)

        # wait for upload to complete
        future.result()

        print ("Large file upload complete!")
    except Exception as e:
        print("Unable to complete large file upload: {0}".format(e))
    finally:
        transfer_mgr.shutdown()
```

### バケット内の項目のリスト (v2)
{: #python-examples-list-objects-v2}

[S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} オブジェクトには、コンテンツをリストするための、更新されたメソッドが含まれます ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window})。このメソッドでは、返されるレコードの数を制限できるほか、レコードをバッチで取得できます。これは、アプリケーション内で結果をページングする場合に役立ち、パフォーマンスも改善される可能性があります。

```python
def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        # create client object
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT)

        more_results = True
        next_token = ""

        while (more_results):
            response = cos_cli.list_objects_v2(Bucket=bucket_name, MaxKeys=max_keys, ContinuationToken=next_token)
            files = response["Contents"]
            for file in files:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))

            if (response["IsTruncated"]):
                next_token = response["NextContinuationToken"]
                print("...More results in next batch!\n")
            else:
                more_results = False
                next_token = ""

        log_done()
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*SDK リファレンス*
* クラス
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* メソッド
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Key Protect の使用
{: #python-examples-kp}
クラウド内の重要な Data at Rest (保存されたデータ) を暗号化するために、Key Protect をストレージ・バケットに追加できます。

### 始める前に
{: #python-examples-kp-prereqs}

Key-Protect が有効なバケットを作成するためには、以下の項目が必要です。

* [プロビジョン済み](/docs/services/key-protect?topic=key-protect-provision)の Key Protect サービス
* 使用可能なルート・キー ([生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)されたか、[インポート](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)されたもの)

### ルート・キー CRN の取得
{: #python-examples-kp-root}

1. Key Protect サービスの[インスタンス ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) を取得します。
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) を使用して、すべての[使用可能なキー](https://cloud.ibm.com/apidocs/key-protect)を取得します。
    * `curl` コマンドまたは API REST クライアント ([Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) など) のいずれかを使用して、[Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) にアクセスできます。
3. バケットで Key Protect を有効にするために使用するルート・キーの CRN を取得します。CRN は、下記のようになります。

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect が有効なバケットの作成
{: #python-examples-kp-new-bucket}
```python
COS_KP_ALGORITHM = "<algorithm>"
COS_KP_ROOTKEY_CRN = "<root-key-crn>"

# Create a new bucket with key protect (encryption)
def create_bucket_kp(bucket_name):
    print("Creating new encrypted bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            },
            IBMSSEKPEncryptionAlgorithm=COS_KP_ALGORITHM,
            IBMSSEKPCustomerRootKeyCrn=COS_KP_ROOTKEY_CRN
        )
        print("Encrypted Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create encrypted bucket: {0}".format(e))
```

*キー値*
* `<algorithm>` - バケットに追加される新規オブジェクトに使用される暗号化アルゴリズム (デフォルトは AES256)。
* `<root-key-crn>` - Key Protect サービスから取得したルート・キーの CRN。

*SDK リファレンス*
* クラス
    * [バケット](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* メソッド
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Aspera 高速転送の使用
{: #python-examples-aspera}

[Aspera 高速転送ライブラリー](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)をインストールすると、アプリケーション内で高速ファイル転送を活用できます。Aspera ライブラリーはクローズ・ソースであるため、(Apache ライセンスを使用する) COS SDK のオプションの依存関係です。

各 Aspera セッションは、転送を実行するためにクライアント・マシン上で実行される個別の `ascp` プロセスを作成します。ご使用のコンピューティング環境でこのプロセスの実行が許可されるようにしてください。
{:tip}


### AsperaTransferManager の初期化
{: #python-examples-aspera-init}

`AsperaTransferManager` を初期化する前に、正常に機能する [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} オブジェクト (`resource` でも `session` でもありません) が存在することを確認してください。

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_s3transfer.aspera.manager import AsperaTransferManager

COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location>"

# Create resource
cos = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

transfer_manager = AsperaTransferManager(cos)
```

Aspera 高速転送用の IAM API キーを指定する必要があります。[HMAC 資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} は、現在、サポートされて**いません**。IAM について詳しくは、[ここをクリック](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)してください。
{:tip}

スループットを最大にするには、**しきい値**で定義したサイズのデータをチャンクで送信する、指定した数の並列**セッション**に転送を分割します。

マルチセッションを使用する場合の標準的な構成は、以下のとおりです。
* 2500 MBps ターゲット・レート
* 100 MB のしきい値 (*これが、ほとんどのアプリケーションに対する推奨値です*)

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
上記の例の場合、SDK は 2500 MBps のターゲット・レートに到達しようと、それに見合った十分な数のセッションを作成します。

代わりの方法として、セッション管理を SDK で明示的に構成することもできます。これは、ネットワーク使用率をより正確に制御する必要がある場合に役立ちます。

明示的なマルチセッションを使用する場合の標準的な構成は、以下のとおりです。
* 2 個または 10 個のセッション
* 100 MB のしきい値 (*これが、ほとんどのアプリケーションに対する推奨値です*)

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
最高のパフォーマンスを得るには、ほとんどのシナリオで、Aspera 高速転送のインスタンス化に関連するオーバーヘッドを最小限に抑えるために、常に複数のセッションを使用してください。**ネットワーク容量が 1 Gbps 以上ある場合は、10 個のセッションを使用してください。**それより低い帯域幅のネットワークでは、使用するセッションを 2 つにする必要があります。
{:tip}

### ファイルのアップロード
{: #python-examples-aspera-upload}
```python
bucket_name = "<bucket-name>"
upload_filename = "<absolute-path-to-file>"
object_name = "<item-name>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload(upload_filename, bucket_name, object_name)

    # Wait for upload to complete
    future.result()
```

*キー値*
* `<bucket-name>` - ターゲット・バケットの名前
* `<absolute-path-to-file>` - アップロードするファイルのディレクトリー・パスとファイル名
* `<item-name>` - バケットに追加される新規ファイルの名前

### ファイルのダウンロード
{: #python-examples-aspera-download}

```python
bucket_name = "<bucket-name>"
download_filename = "<absolute-path-to-file>"
object_name = "<object-to-download>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download(bucket_name, object_name, download_filename)

    # Wait for download to complete
    future.result()
```

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前。
* `<absolute-path-to-file>` - ローカル・システムにファイルを保存する際のディレクトリーとファイル名。
* `<object-to-download>` - ダウンロードするバケット内のファイルの名前。

### ディレクトリーのアップロード
{: #python-examples-aspera-directory-upload}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY, and have objects in it.
local_upload_directory = "<absolute-path-to-directory>"
# THIS SHOULD NOT HAVE A LEADING "/"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory)

    # Wait for upload to complete
    future.result()
```

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前
* `<absolute-path-to-directory>` - アップロードするファイルを含んでいるローカル・ディレクトリー。前後に `/` を付ける必要があります (例: `/Users/testuser/Documents/Upload/`)
* `<object prefix>` - ファイルを保管するバケット内のディレクトリーの名前。前にスラッシュ `/` は付けないでください (例: `newuploads/`)

### ディレクトリーのダウンロード
{: #python-examples-aspera-directory-download}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory)

    # Wait for download to complete
    future.result()
```

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前
* `<absolute-path-to-directory>` - ダウンロードされたファイルを保存するローカル・ディレクトリー。前後にスラッシュ `/` を付ける必要があります (例: `/Users/testuser/Downloads/`)
* `<object prefix>` - ファイルを保管するバケット内のディレクトリーの名前。前にスラッシュ `/` は付けないでください (例: `todownload/`)

### サブスクライバーの使用
{: #python-examples-aspera-subscribers}

サブスクライバーは、カスタム・コールバック・メソッドを関連付けることにより、転送を監視できます。すべての転送は、以下のフェーズの間を遷移します。

`待機中 - 進行中 - 完了`

各フェーズに応じた 3 つの使用可能なサブスクライバーがあります。

* `CallbackOnQueued()` - 新しい転送が `AsperaTransferManager` に追加されたときに呼び出されます。
* `CallbackOnProgress()` - 転送がデータの送信を開始したときに呼び出されます (転送の進行中は繰り返し起動されます)。
* `CallbackOnDone()` - 転送が完了すると呼び出されます。

```python
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Subscriber callbacks
class CallbackOnQueued(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_queued(self, future, **kwargs):
        print("Directory download queued.")

class CallbackOnProgress(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_progress(self, future, bytes_transferred, **kwargs):
        print("Directory download in progress: %s bytes transferred" % bytes_transferred)

class CallbackOnDone(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_done(self, future, **kwargs):
        print("Downloads complete!")

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Attach subscribers
subscribers = [CallbackOnQueued(), CallbackOnProgress(), CallbackOnDone()]

# Get object with Aspera
future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, subscribers)

# Wait for download to complete
    future.result()
```

*キー値*
* `<bucket-name>` - Aspera が有効になっている Object Storage サービス・インスタンス内のバケットの名前
* `<absolute-path-to-directory>` - ダウンロードされたファイルを保存するローカル・ディレクトリー。前後にスラッシュ `/` を付ける必要があります (例: `/Users/testuser/Downloads/`)
* `<object prefix>` - ファイルを保管するバケット内のディレクトリーの名前。前にスラッシュ `/` は付けないでください (例: `todownload/`)

上記のサンプル・コードによって、以下のような出力が生成されます。

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### 一時停止/再開/キャンセル
{: #python-examples-aspera-pause}

SDK には、`AsperaTransferFuture` オブジェクトの以下のメソッドを使用して、ファイル/ディレクトリー転送の進行を管理できる機能があります。

* `pause()`
* `resume()`
* `cancel()`

上記のいずれのメソッドを呼び出しても副次作用はありません。クリーンアップとハウスキーピングは、SDK によって適切に処理されます。
{:tip}

```python
# Create Transfer manager
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

with AsperaTransferManager(client) as transfer_manager:

    # download a directory with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

    # pause the transfer
    future.pause()

    # resume the transfer
    future.resume()

    # cancel the transfer
    future.cancel()
```

### Aspera に関する問題のトラブルシューティング
{: #python-examples-aspera-ts}
**問題:** 開発者が Windows 10 で Python 2.7.15 を使用している場合、Aspera SDK のインストール時に障害が発生することがあります。

**原因:** ご使用の環境に異なるバージョンの Python がインストールされている場合、Aspera SDK をインストールしようとしたときにインストールが失敗することがあります。この原因は、パスに DLL ファイルが欠落しているか、パスに誤った DLL が含まれているためです。

**解決策:** この問題を解決するための最初のステップは、Aspera ライブラリーを再インストールすることです。インストール中に障害が発生した可能性があります。その結果、DLL ファイルが影響を受けた可能性があります。それでも問題が解決しない場合は、Python のバージョンを更新する必要があります。これを実行できない場合は、[Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window} をインストールして使用できます。これで、問題なく Aspeara SDK をインストールできるようになります。

## メタデータの更新
{: #python-examples-metadata}
既存のオブジェクトのメタデータを更新する方法は、2 とおりあります。
* 新しいメタデータと元のオブジェクト・コンテンツが指定された `PUT` 要求
* 元のオブジェクトをコピー・ソースとして指定し、新しいメタデータを使用する `COPY` 要求の実行

### PUT を使用したメタデータの更新
{: #python-examples-metadata-put}
**注:** `PUT` 要求は、オブジェクトの既存のコンテンツを上書きするものです。このため、まずオブジェクトをダウンロードし、新しいメタデータを指定して再度アップロードする必要があります。

```python
def update_metadata_put(bucket_name, item_name, key, value):
    try:
        # retrieve the existing item to reload the contents
        response = cos_cli.get_object(Bucket=bucket_name, Key=item_name)
        existing_body = response.get("Body").read()

        # set the new metadata
        new_metadata = {
            key: value
        }

        cos_cli.put_object(Bucket=bucket_name, Key=item_name, Body=existing_body, Metadata=new_metadata)

        print("Metadata update (PUT) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```

### COPY を使用したメタデータの更新
{: #python-examples-metadata-copy}

```python
def update_metadata_copy(bucket_name, item_name, key, value):
    try:
        # set the new metadata
        new_metadata = {
            key: value
        }

        # set the copy source to itself
        copy_source = {
            "Bucket": bucket_name,
            "Key": item_name
        }

        cos_cli.copy_object(Bucket=bucket_name, Key=item_name, CopySource=copy_source, Metadata=new_metadata, MetadataDirective="REPLACE")

        print("Metadata update (COPY) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```

## Immutable Object Storage の使用
{: #python-examples-immutable}

### 既存のバケットへの保護構成の追加
{: #python-examples-immutable-add}

保護対象のバケットに書き込まれたオブジェクトは、保護期間が満了し、オブジェクトに対するすべての法的保留が解除されるまで削除できません。オブジェクトの作成時にオブジェクト固有の値が指定されない限り、バケットのデフォルトの保存期間値がオブジェクトにも適用されます。保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。 

保存期間設定 `MinimumRetention`、`DefaultRetention`、および `MaximumRetention` でサポートされる最小値と最大値は、それぞれ 0 日と 365243 日 (1000 年) です。 

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

### バケットに対する保護のチェック
{: #python-examples-immutable-check}
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


### 保護オブジェクトのアップロード
{: #python-examples-immutable-upload}

保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。


|値	| タイプ	| 説明 |
| --- | --- | --- | 
|`Retention-Period` | 負でない整数 (秒数) | オブジェクトを保管する保存期間 (秒数)。オブジェクトは、保存期間に指定された時間が経過するまで上書きも削除もできません。このフィールドと `Retention-Expiration-Date` が指定された場合、`400` エラーが返されます。いずれも指定されない場合は、バケットの `DefaultRetention` 期間が使用されます。ゼロ (`0`) は有効な値であり、バケットの最小保存期間も `0` であると想定されます。 |
| `Retention-expiration-date` | 日付 (ISO 8601 フォーマット) | オブジェクトを合法的に削除または変更できるようになる日付。これか Retention-Period ヘッダーのいずれかのみを指定できます。両方が指定された場合は、`400` エラーが返されます。いずれも指定されない場合は、バケットの DefaultRetention 期間が使用されます。 |
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

### 保護オブジェクトに対する法的保留の追加または解除
{: #python-examples-immutable-legal-hold}

オブジェクトがサポートできる法的保留は 100 個です。

*  法的保留 ID は、最大長 64 文字かつ最小長 1 文字のストリングです。有効な文字は、英字、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-`、および ` です。
* 指定された法的保留を追加することで、オブジェクトに対する法的保留の合計数が 100 個を超える場合、新しい法的保留は追加されず、`400` エラーが返されます。
* ID が長すぎる場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID に無効文字が含まれている場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID がオブジェクトで既に使用中の場合、既存の法的保留は変更されず、ID が既に使用中であることを示す応答と `409` エラーが返されます。
* オブジェクトに保存期間メタデータがない場合は、`400` エラーが返され、法的保留の追加または削除は許可されません。


法的保留の追加または削除を行うユーザーには、このバケットに対する`マネージャー`許可が必要です。


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

### 保護オブジェクトの保存期間の延長
{: #python-examples-immutable-extend}

オブジェクトの保存期間は延長のみが可能です。現在構成されている値から減らすことはできません。

保存延長値は次の 3 つの方法のいずれかで設定されます。

* 現行値からの追加時間 (`Additional-Retention-Period` または同様のメソッド)
* 新しい延長期間 (秒数) (`Extend-Retention-From-Current-Time` または同様のメソッド)
* オブジェクトの新しい保存有効期限日付 (`New-Retention-Expiration-Date` または同様のメソッド)

オブジェクト・メタデータに保管されている現在の保存期間は、`extendRetention` 要求に設定されているパラメーターに応じて、指定された追加時間分増やされるか、新しい値に置き換えられます。いずれの場合も、保存延長パラメーターは現在の保存期間に対して検査され、更新後の保存期間が現在の保存期間より大きい場合にのみ、延長パラメーターが受け入れられます。

保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。



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

### 保護オブジェクトの法的保留のリスト
{: #python-examples-immutable-list-holds}

この操作で返される内容は以下のとおりです。

* オブジェクト作成日
* オブジェクト保存期間 (秒数)
* 期間および作成日に基づいて計算された保存有効期限
* 法的保留のリスト
* 法的保留 ID
* 法的保留が適用されたときのタイム・スタンプ

オブジェクトに法的保留がない場合は、空の `LegalHoldSet` が返されます。
オブジェクトに保存期間が指定されていない場合は、`404` エラーが返されます。


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

