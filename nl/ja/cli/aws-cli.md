---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# AWS CLI の使用
{: #aws-cli}

AWS の公式コマンド・ライン・インターフェースは、IBM COS S3 API と互換性があります。Python で作成されており、`pip install awscli` を使用して Python Package Index からインストールできます。デフォルトでは、アクセス・キーのソースは `~/.aws/credentials` ですが、環境変数として設定することもできます。

以下の例は、バージョン 1.14.2 の CLI を使用して生成されています。インストールされているバージョンを確認するには、`aws --version` を実行します。

## {{site.data.keyword.cos_short}} に接続するための CLI の構成
{: #aws-cli-config}

AWS CLI を構成するには、`aws configure` と入力し、[HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)とデフォルトのリージョン名を指定します。AWS S3 で使用される「リージョン名」は、{{site.data.keyword.cos_short}} で新規バケットのストレージ・クラスを定義するために使用するプロビジョニング・コード (`LocationConstraint`) に対応しています。

`LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラス・ガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)を参照してください。

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

これにより、以下の 2 つのファイルが作成されます。

 `~/.aws/credentials`:

```
[default]
aws_access_key_id = {Access Key ID}
aws_secret_access_key = {Secret Access Key}
```
{:codeblock}

`~/.aws/config`:

```
[default]
region = {Provisioning Code}
output = json
```
{:codeblock}


また、以下のように環境変数を使用して HMAC 資格情報を設定することもできます。

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


IBM COS エンドポイントのソースは、`--endpoint-url` オプションを使用して指定する必要があり、資格情報ファイルで設定することはできません。


## 高水準の構文コマンド
{: #aws-cli-high-level}

シンプルなユース・ケースは、`aws --endpoint-url {endpoint} s3 <command>` を使用して実現できます。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。オブジェクトは、`ls`、`mv`、`cp`、`rm` などの使い慣れたシェル・コマンドを使用して管理します。バケットは `mb`を使用して作成でき、`rb` を使用して削除できます。

### サービス・インスタンス内のすべてのバケットのリスト表示
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### バケット内のオブジェクトのリスト表示
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### 新規バケットの作成
{: #aws-cli-high-level-new-bucket}

**注**: 個人情報 (PII): バケットの作成やオブジェクトの追加を行うときは、名前、場所、その他の方法でユーザー (個人) を特定可能な情報を使用しないようにしてください。
{:tip}

`~/.aws/config` ファイル内のデフォルトのリージョンが、選択したエンドポイントと同じロケーションに対応している場合、バケットの作成は簡単です。

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### バケットへのオブジェクトの追加
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

あるいは、以下のように、ファイル名とは異なる新しいオブジェクト・キーを設定することもできます。

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### 同じリージョン内のあるバケットから別のバケットへのオブジェクトのコピー:
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### バケットからオブジェクトを削除
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### バケットの削除
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### 事前署名 URL の作成
{: #aws-cli-high-level-presign}

CLI では、事前署名 URL を作成することもできます。これにより、既存のアクセス制御を変更することなく、オブジェクトへの一時的なパブリック・アクセスが可能になります。生成された URL には、URI を難読化する HMAC 署名が含まれているため、絶対 URL を知らないユーザーは、HMAC 署名が含まれていない場合は公開されてアクセス可能なファイルにアクセスできる可能性が低くなります。

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

以下のように、URL の有効期限を秒単位で設定することもできます (デフォルトは 3600)。

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## 低水準の構文コマンド
{: #aws-cli-low-level}

AWS CLI では、`s3api` コマンドを使用して、直接 HTTP 要求と同じ応答を提供する直接 API 呼び出しも可能です。

### バケットのリスト表示:
{: #aws-cli-low-level-list-buckets}

```bash
$ aws --endpoint-url {endpoint} s3api list-buckets
{
    "Owner": {
        "DisplayName": "{storage-account-uuid}",
        "ID": "{storage-account-uuid}"
    },
    "Buckets": [
        {
            "CreationDate": "2016-09-09T12:48:52.442Z",
            "Name": "bucket-1"
        },
        {
            "CreationDate": "2016-09-16T21:29:00.912Z",
            "Name": "bucket-2"
        }
    ]
}
```

### バケット内のオブジェクトのリスト表示
{: #aws-cli-low-level-list-objects}

```sh
$ aws --endpoint-url {endpoint} s3api list-objects --bucket bucket-1
```

```json
{
    "Contents": [
        {
            "LastModified": "2016-09-28T15:36:56.807Z",
            "ETag": "\"13d567d518c650414c50a81805fff7f2\"",
            "StorageClass": "STANDARD",
            "Key": "c1ca2-filename-00001",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 837
        },
        {
            "LastModified": "2016-09-09T12:49:58.018Z",
            "ETag": "\"3ca744fa96cb95e92081708887f63de5\"",
            "StorageClass": "STANDARD",
            "Key": "c9872-filename-00002",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 533
        },
        {
            "LastModified": "2016-09-28T15:36:17.573Z",
            "ETag": "\"a54ed08bcb07c28f89f4b14ff54ce5b7\"",
            "StorageClass": "STANDARD",
            "Key": "98837-filename-00003",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 14476
        },
        {
            "LastModified": "2016-10-06T14:46:26.923Z",
            "ETag": "\"2bcc8ee6bc1e4b8cd2f9a1d61d817ed2\"",
            "StorageClass": "STANDARD",
            "Key": "abfc4-filename-00004",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 20950
        }
    ]
}
```
