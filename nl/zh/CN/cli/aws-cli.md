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

AWS 的官方命令行界面与 IBM COS S3 API 兼容。此界面用 Python 编写，可以通过 `pip install awscli` 从 Python Package Index 进行安装。缺省情况下，访问密钥可从 `~/.aws/credentials` 中获取，但也可以设置为环境变量。

这些示例是使用 CLI V1.14.2 生成的。要检查安装的版本，请运行 `aws --version`。

## 配置 CLI 以连接到 {{site.data.keyword.cos_short}}
{: #aws-cli-config}

要配置 AWS CLI，请输入 `aws configure`，然后提供 [HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)和缺省区域名称。AWS S3 使用的“区域名称”对应于 {{site.data.keyword.cos_short}} 用于定义新存储区的存储类的供应代码 (`LocationConstraint`)。

在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)中可以参考 `LocationConstraint` 的有效供应代码的列表。

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

这将创建两个文件：

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


您还可以使用环境变量来设置 HMAC 凭证：

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


IBM COS 端点必须使用 `--endpoint-url` 选项来获取，而不能在凭证文件中进行设置。


## 高级语法命令
{: #aws-cli-high-level}

简单用例可以使用 `aws --endpoint-url {endpoint} s3 <command>` 来完成。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。管理对象时，可使用您熟悉的 shell 命令，例如 `ls`、`mv`、`cp` 和 `rm`。可以使用 `mb` 创建存储区，使用 `rb` 删除存储区。

### 列出服务实例中的所有存储区
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### 列出存储区中的对象
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### 新建存储区
{: #aws-cli-high-level-new-bucket}

**注**：个人可标识信息 (PII)：创建存储区和/或添加对象时，请确保未使用可以通过名称、位置或其他任何方式识别到任何用户（自然人）的任何信息。
{:tip}

如果 `~/.aws/config` 文件中的缺省区域与所选端点的位置相同，那么存储区创建非常简单。

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### 向存储区添加对象
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

或者，可以设置与文件名不同的新对象键：

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### 将对象从一个存储区复制到同一区域中的另一个存储区：
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### 从存储区中删除一个对象
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### 除去存储区
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### 创建预签名 URL
{: #aws-cli-high-level-presign}

CLI 还能够创建预签名 URL。通过这些 URL，可以临时对对象进行公共访问，而不更改任何现有访问控制。生成的 URL 包含用于对 URI 进行加密的 HMAC 签名，这样一来，没有完整 URL 的用户不太可能能够访问原本可公开访问的文件。

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

还可以设置 URL 的到期时间（以秒为单位，缺省值为 3600）：

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## 低级语法命令
{: #aws-cli-low-level}

AWS CLI 还允许发出直接 API 调用，以提供与使用 `s3api` 命令发出直接 HTTP 请求相同的响应。

### 列出存储区：
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

### 列出存储区中的对象
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
