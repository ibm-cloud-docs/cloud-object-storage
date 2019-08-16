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

# 使用 AWS CLI
{: #aws-cli}

AWS 的正式指令行介面與 IBM COS S3 API 相容。它以 Python 撰寫，可以透過 `pip install awscli` 從 Python Package Index 進行安裝。依預設，存取金鑰是從 `~/.aws/credentials` 讀取，但也可以設為環境變數。

這些範例已使用 1.14.2 版的 CLI 產生。若要檢查已安裝的版本，請執行 `aws --version`。

## 配置 CLI 以連接至 {{site.data.keyword.cos_short}}
{: #aws-cli-config}

若要配置 AWS CLI，請鍵入 `aws configure`，並提供 [HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)及預設地區名稱。 AWS S3 使用的「地區名稱」對應於 {{site.data.keyword.cos_short}} 用來定義新儲存區儲存空間類別的佈建碼 (`LocationConstraint`)。

`LocationConstraint` 的有效佈建碼清單可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)中參閱。

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

這會建立兩個檔案：

 `~/.aws/credentials`：

```
[default]
aws_access_key_id = {Access Key ID}
aws_secret_access_key = {Secret Access Key}
```
{:codeblock}

`~/.aws/config`：

```
[default]
region = {Provisioning Code}
output = json
```
{:codeblock}


您也可以使用環境變數來設定 HMAC 認證：

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


IBM COS 端點必須使用 `--endpoint-url` 選項來取得，而且無法在 credentials 檔中設定。


## 高階語法指令
{: #aws-cli-high-level}

簡單的使用案例可以使用 `aws --endpoint-url {endpoint} s3 <command>` 來達成。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。物件是使用熟悉的 Shell 指令進行管理，例如 `ls`、`mv`、`cp` 及 `rm`。儲存區可以使用 `mb` 來建立，並使用 `rb` 予以刪除。

### 列出服務實例內的所有儲存區
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### 列出儲存區內的物件
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### 建立新的儲存區
{: #aws-cli-high-level-new-bucket}

**附註**：個人識別資訊 (PII)：建立儲存區及/或新增物件時，請務必不要使用可以依姓名、位置或任何其他方法識別任何使用者（自然人）的任何資訊。
{:tip}

如果 `~/.aws/config` 檔案中的預設地區對應於與所選擇端點相同的位置，則儲存區建立作業是直接明確的。

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### 將物件新增至儲存區
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

或者，您也可以設定不同於檔案名稱的新物件金鑰：

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### 將物件從某個儲存區複製到相同地區內的另一個儲存區：
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### 從儲存區刪除物件
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### 移除儲存區
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### 建立預先簽署的 URL
{: #aws-cli-high-level-presign}

CLI 也可以建立預先簽署的 URL。這些容許暫時的物件公用存取權，而不會變更任何現有存取控制措施。所產生的 URL 包含使 URI 模糊的 HMAC 簽章，讓沒有完整 URL 的使用者較不可能存取可用其他方式公開存取的檔案。

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

您也可以在幾秒內設定 URL 的有效期限（預設值為 3600）：

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## 低階語法指令
{: #aws-cli-low-level}

AWS CLI 也容許透過使用 `s3api` 指令，提供與直接 HTTP 要求相同回應的直接 API 呼叫。

### 列出儲存區：
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

### 列出儲存區內的物件
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
