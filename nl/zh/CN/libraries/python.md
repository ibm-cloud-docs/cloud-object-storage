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

# 使用 Python
{: #python}

Python 支持是通过 `boto3` 库的派生提供的。可以在 Python Package Index 中通过 `pip install ibm-cos-sdk` 安装 Python。

在 [GitHub](https://github.com/ibm/ibm-cos-sdk-python/) 上可以找到源代码。

`ibm_boto3` 库提供对 {{site.data.keyword.cos_full}} API 的完全访问权。端点、API 密钥和实例标识必须在创建服务资源或低级别客户机期间指定，如以下基本示例所示。

服务实例标识也称为_资源实例标识_。通过创建[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)或通过 CLI 可以找到此值。
{:tip}

在[此处](https://ibm.github.io/ibm-cos-sdk-python/)可以找到详细的文档。

## 从 1.x.x 升级
{: #python-migrate}

SDK 2.0 版本引入了名称空间更改，允许应用程序使用原始 `boto3` 库来连接到同一应用程序或环境中的 AWS 资源。要从 1.x 迁移到 2.0，有必要进行一些更改。

    1. 更新 `requirements.txt`，或在 PyPI 中通过 `pip install -U ibm-cos-sdk` 进行更新。使用 `pip list | grep ibm-cos` 来验证是否不存在任何更低的版本。
    2. 将所有导入声明从 `boto3` 更新为 `ibm_boto3`。
    3. 如果需要，请通过更新 `requirements.txt`，或在 PyPI 中通过 `pip install boto3` 进行更新来重新安装原始 `boto3`。

## 创建客户机和获取凭证
{: #python-credentials}

为了连接到 COS，将通过提供凭证信息（API 密钥和服务实例标识）来创建和配置客户机。这些值还可以自动从凭证文件或环境变量中获取。

生成[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)后，生成的 JSON 文档可以保存到 `~/.bluemix/cos_credentials`。除非在客户机创建期间显式设置了其他凭证，否则 SDK 会自动从此文件中获取凭证。如果 `cos_credentials` 文件包含 HMAC 密钥，那么客户机会使用签名进行认证；如果不包含 HMAC 密钥，客户机将使用提供的 API 密钥通过不记名令牌进行认证。

如果是从 AWS S3 进行迁移，那么还可以从 `~/.aws/credentials` 中获取以下格式的凭证数据：

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

如果 `~/.bluemix/cos_credentials` 和 `~/.aws/credentials` 同时存在，那么 `cos_credentials` 优先。

### 收集必需的信息
{: #python-prereqs}

示例中显示了以下变量：

* `bucket_name` 必须是[唯一的 DNS 安全](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)字符串。因为存储区名称在整个系统中唯一，因此如果多次运行此示例，那么需要更改这些值。请注意，名称在删除后会保留 10 到 15 分钟。
* `ibm_api_key_id` 是在[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `apikey` 的值。
* `ibm_service_instance_id` 是在[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `resource_instance_id` 的值。 
* `endpoint_url` 是服务端点 URL，包含 `https://` 协议。此值**不是**在[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `endpoints` 值。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `LocationConstraint` 是对应于 `endpoint` 值的[有效供应代码](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)。 


## 代码示例
{: #python-examples}

代码示例是使用 **Python 2.7.15** 编写的

### 初始化配置
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
*键值*
* `<endpoint>` - Cloud Object Storage 的公共端点，前缀为模式（“https://”）（通过 [IBM Cloud 仪表板](https://cloud.ibm.com/resources){:new_window}提供）。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<api-key>` - 创建服务凭证时生成的 API 密钥（创建和删除示例需要写访问权）
* `<resource-instance-id>` - Cloud Object Storage 的资源标识（通过 [IBM Cloud CLI](/docs/cli?topic=cloud-cli-idt-cli) 或 [IBM Cloud 仪表板](https://cloud.ibm.com/resources){:new_window}提供）
* `<location>` - Cloud Object Storage 的缺省位置（必须与用于 `<endpoint>` 的区域相匹配）

*SDK 参考*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### 创建新存储区
{: #python-examples-new-bucket}

在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中可以参考 `LocationConstraint` 的有效供应代码的列表。

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

*SDK 参考*
* 类
  * [`Bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 方法
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### 创建新的文本文件
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

*SDK 参考*
* 类
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 方法
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### 列出可用存储区
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

*SDK 参考*
* 类
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* 集合
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### 列出存储区中的项
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

*SDK 参考*
* 类
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* 集合
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### 获取特定项的文件内容
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

*SDK 参考*
* 类
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 方法
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### 从存储区中删除一个项
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

*SDK 参考*
* 类
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 方法
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### 从存储区中删除多个项
{: #python-examples-delete-multiple-objects}

删除请求最多可包含 1000 个要删除的键。虽然这对于减少每个请求的开销非常有用，但在删除大量键时应谨慎。此外，请考虑对象的大小，以确保性能合适。
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

*SDK 参考*
* 类
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 方法
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### 删除存储区
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

*SDK 参考*
* 类
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 方法
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### 运行分块上传
{: #python-examples-multipart}

#### 上传二进制文件（首选方法）
{: #python-examples-multipart-binary}

必要时，[S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} 类的 [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} 方法会自动运行分块上传。[TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} 类用于确定使用分块上传的阈值。

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

*SDK 参考*
* 类
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* 方法
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### 手动运行分块上传
{: #python-examples-multipart-manual}

如果需要，可以使用 [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} 类来执行分块上传。如果需要拥有对上传过程的更多控制权，那么此方法可能非常有用。

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

*SDK 参考*
* 类
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 方法
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### 使用 TransferManager 上传大对象
{: #python-examples-multipart-transfer}

`TransferManager` 提供了另一种方法，用于在每次有必要设置配置参数时，自动合并分块上传来运行大型文件传输。

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

### 列出存储区中的项 (V2)
{: #python-examples-list-objects-v2}

[S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} 对象包含已更新的用于列出内容的方法 ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window})。此方法允许您限制返回的记录数，并批量检索记录。这对于对应用程序中的结果进行分页可能非常有用，并可能提高性能。

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

*SDK 参考*
* 类
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 方法
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## 使用 Key Protect
{: #python-examples-kp}
可以将 Key Protect 添加到存储区，以对云中的敏感数据进行静态加密。

### 开始之前
{: #python-examples-kp-prereqs}

要创建启用了 Key Protect 的存储区，需要以下各项：

* [已供应](/docs/services/key-protect?topic=key-protect-provision) Key Protect 服务
* 根密钥可用（[已生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已导入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 检索根密钥 CRN
{: #python-examples-kp-root}

1. 检索 Key Protect 服务的[实例标识](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)。
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 来检索所有[可用密钥](https://cloud.ibm.com/apidocs/key-protect)。
    * 可以使用 `curl` 命令或 API REST 客户机（例如，[Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）来访问 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 检索用于在存储区上启用 Key Protect 的根密钥的 CRN。CRN 类似于以下内容：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 创建启用了 Key Protect 的存储区
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

*键值*
* `<algorithm>` - 用于添加到存储区的新对象的加密算法（缺省值为 AES256）。
* `<root-key-crn>` - 从 Key Protect 服务获取的根密钥的 CRN。

*SDK 参考*
* 类
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 方法
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## 使用 Aspera 高速传输
{: #python-examples-aspera}

通过安装 [Aspera 高速传输库](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)，可以在应用程序中利用高速文件传输。Aspera 库是封闭式源代码库，因此具有 COS SDK（使用 Apache 许可证）的可选依赖项。

每个 Aspera 会话都会创建一个单独的 `ascp` 进程，此进程在客户机上运行以执行传输。请确保计算环境允许此进程运行。
{:tip}


### 初始化 AsperaTransferManager
{: #python-examples-aspera-init}

初始化 `AsperaTransferManager` 之前，请确保在使用 [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}（而不是 `resource` 或 `session`）对象。

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

您需要提供 IAM API 密钥以用于 Aspera 高速传输。目前**不**支持 [HMAC 凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window}。有关 IAM 的更多信息，请[单击此处](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)。
{:tip}

要实现最高吞吐量，请将传输拆分成指定数量的并行**会话**，这些会话发送的数据块的大小由**阈值**定义。

使用多会话的典型配置应该如下：
* 2500 MBps 目标速率
* 100 MB 阈值（*这是针对大多数应用程序的建议值*）

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
在以上示例中，SDK 将衍生足够的会话来尝试达到目标速率 2500 MBps。

或者，可以在 SDK 中显式配置会话管理。对于需要更精确地控制网络利用率的情况，此功能会非常有用。

使用显式多会话的典型配置应该如下：
* 2 个或 10 个会话
* 100 MB 阈值（*这是针对大多数应用程序的建议值*）

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
为了在大多数场景实现最佳性能，请始终使用多个会话，以最大限度地减少与实例化 Aspera 高速传输关联的任何开销。**如果网络容量至少为 1 Gbps，那么应该使用 10 个会话。**带宽低于 1 Gbps 的网络应该使用 2 个会话。
{:tip}

### 文件上传
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

*键值*
* `<bucket-name>` - 目标存储区的名称
* `<absolute-path-to-file>` - 要上传的文件的目录路径和文件名
* `<item-name>` - 已添加到存储区的新文件的名称

### 文件下载
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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称。
* `<absolute-path-to-file>` - 本地系统上要将文件保存到的目录和文件名。
* `<object-to-download>` - 存储区中要下载的对象的名称。

### 目录上传
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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称
* `<absolute-path-to-directory>` - 包含要上传的文件的本地目录。必须具有前导和尾部 `/`（即，`/Users/testuser/Documents/Upload/`）
* `<object prefix>` - 存储区中要存储文件的目录的名称。不能有前导斜杠 `/`（即，`newupload/`）

### 目录下载
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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称
* `<absolute-path-to-directory>` - 要保存所下载文件的本地目录。必须具有前导和尾部斜杠 `/`（即，`/Users/testuser/Downloads/`）
* `<object prefix>` - 存储区中要存储文件的目录的名称。不能有前导斜杠 `/`（即，`todownload/`）

### 使用订户
{: #python-examples-aspera-subscribers}

订户通过附加定制回调方法来提供对传输的可观察性。所有传输都会在以下各阶段之间进行转换：

`已排队 - 正在进行 - 完成`

每个阶段有三个可用订户：

* `CallbackOnQueued()` - 将新传输添加到 `AsperaTransferManager` 时调用
* `CallbackOnProgress()` - 传输开始传输数据时调用（正在传输期间会重复触发）。
* `CallbackOnDone()` - 传输完成后调用

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

*键值*
* `<bucket-name>` - Object Storage 服务实例中启用了 Aspera 的存储区的名称
* `<absolute-path-to-directory>` - 要保存所下载文件的本地目录。必须具有前导和尾部斜杠 `/`（即，`/Users/testuser/Downloads/`）
* `<object prefix>` - 存储区中要存储文件的目录的名称。不能有前导斜杠 `/`（即，`todownload/`）

上面的样本代码会生成以下输出：

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### 暂停/恢复/取消
{: #python-examples-aspera-pause}

SDK 提供了通过 `AsperaTransferFuture` 对象的以下方法来管理文件/目录传输进度的能力：

* `pause()`
* `resume()`
* `cancel()`

调用以上概述的任一方法都不会有任何副作用。合适的清除和整理工作由 SDK 负责处理。
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

### 对 Aspera 问题进行故障诊断
{: #python-examples-aspera-ts}
**问题：**在 Windows 10 上使用 Python 2.7.15 的开发者在安装 Aspera SDK 时，可能会遇到失败。

**原因：**如果在环境中安装了 Python 的不同版本，那么在尝试安装 Aspera SDK 时可能会遇到安装失败。原因可能是路径中缺少 DLL 文件或 DLL 不正确。

**解决方案：**解决此问题的第一步是重新安装 Aspera 库。安装期间可能发生了故障。结果，这可能影响了 DLL 文件。如果这样做无法解决这些问题，您需要更新 Python 版本。如果无法执行此操作，那么可以安装 [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window}。这将允许安装 Aspera SDK，而不发生任何问题。

## 更新元数据
{: #python-examples-metadata}
有两种方法可更新现有对象上的元数据：
* 对新的元数据和原始对象内容执行 `PUT` 请求
* 使用将原始对象指定为复制源的新元数据来执行 `COPY` 请求

### 使用 PUT 更新元数据
{: #python-examples-metadata-put}
**注：**`PUT` 请求会覆盖对象的现有内容，因此必须首先下载对象，然后使用新的元数据重新上传

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

### 使用 COPY 更新元数据
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

## 使用不可变对象存储器
{: #python-examples-immutable}

### 向现有存储区添加保护配置
{: #python-examples-immutable-add}

对于写入受保护存储区的对象，在保护时间段到期并且除去了对象上的所有合法保留之前，无法删除这些对象。除非在创建对象时提供了特定于对象的值，否则将向对象提供存储区的缺省保留时间值。如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。 

保留期设置 `MinimumRetention`、`DefaultRetention` 和 `MaximumRetention` 的最小和最大支持值分别为 0 天和 365243 天（1000 年）。 

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

### 检查存储区上的保护
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


### 上传受保护对象
{: #python-examples-immutable-upload}

如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。


|值|类型|描述|
| --- | --- | --- | 
|`Retention-Period`|非负整数（秒）|要在对象上存储的保留期（以秒为单位）。在保留期中指定的时间长度到期之前，无法覆盖也无法删除对象。如果同时指定了此字段和 `Retention-Expiration-Date`，将返回 `400` 错误。如果这两个字段均未指定，将使用存储区的 `DefaultRetention` 时间段。假定存储区的最短保留期为 `0`，那么零 (`0`) 是合法值。|
|`Retention-expiration-date`|日期（ISO 8601 格式）|能够合法删除或修改对象的日期。只能指定此项或指定 Retention-Period 头。如果同时指定这两项，将返回 `400` 错误。如果这两项均未指定，将使用存储区的 DefaultRetention 时间段。|
|`Retention-legal-hold-id`|字符串|要应用于对象的单个合法保留。合法保留是长度为 Y 个字符的字符串。在除去与对象关联的所有合法保留之前，无法覆盖或删除对象。|

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
            "Parts":[{
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

### 向受保护对象添加合法保留或除去受保护对象的合法保留
{: #python-examples-immutable-legal-hold}

一个对象可以支持 100 个合法保留：

*  合法保留标识是一个字符串，最大长度为 64 个字符，最小长度为 1 个字符。有效字符为字母、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 和 `。
* 如果添加给定合法保留将导致对象上超过 100 个合法保留，那么不会添加新的合法保留，并且将返回 `400` 错误。
* 如果标识太长，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识包含无效字符，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识已在对象上使用，那么不会修改现有合法保留，响应会指示该标识已在使用，并返回 `409` 错误。
* 如果对象没有保留期元数据，那么将返回 `400` 错误，并且不允许添加或除去合法保留。


添加或除去合法保留的用户必须具有对此存储区的`管理者`许可权。


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

### 延长受保护对象的保留期
{: #python-examples-immutable-extend}

对象的保留期只能延长。不能在当前配置值的基础上缩短。

保留时间延长值可通过以下三种方式之一进行设置：

* 在当前值的基础上增加时间（`Additional-Retention-Period` 或类似方法）
* 新的延长时间段（以秒为单位）（`Extend-Retention-From-Current-Time` 或类似方法）
* 对象的新保留到期日期（`New-Retention-Expiration-Date` 或类似方法）

根据 `extendRetention` 请求中设置的参数，对象元数据中存储的当前保留期可通过给定更多时间延长，也可替换为新值。在所有情况下，都会根据当前保留期来检查延长保留时间参数，并且仅当更新的保留期大于当前保留期时，才会接受延长参数。

如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。



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

### 列出受保护对象上的合法保留
{: #python-examples-immutable-list-holds}

此操作会返回以下内容：

* 对象创建日期
* 对象保留期（秒）
* 根据时间段和创建日期计算的保留到期日期
* 合法保留的列表
* 合法保留标识
* 应用合法保留时的时间戳记

如果对象上没有合法保留，那么会返回空的 `LegalHoldSet`。如果在对象上未指定保留期，那么会返回 `404` 错误。


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

