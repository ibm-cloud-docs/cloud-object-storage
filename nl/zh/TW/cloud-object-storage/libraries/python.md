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

Python 支援是透過 `boto3` 程式庫的分支而提供。它可以透過 `pip install ibm-cos-sdk` 從 Python Package Index 進行安裝。

原始碼可以在 [GitHub](https://github.com/ibm/ibm-cos-sdk-python/) 找到。

`ibm_boto3` 程式庫提供對 {{site.data.keyword.cos_full}} API 的完整存取。建立服務資源或低階用戶端時必須指定端點、API 金鑰及實例 ID，如下列基本範例中所示。

服務實例 ID 也稱為_資源實例 ID_。可以藉由建立[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)，或透過 CLI 找到值。
{:tip}

詳細文件可以在[這裡](https://ibm.github.io/ibm-cos-sdk-python/)找到。

## 從 1.x.x 升級
{: #python-migrate}

2.0 版的 SDK 引進名稱空間作業的變更，可讓應用程式利用原始 `boto3` 程式庫來連接至相同應用程式或環境內的 AWS 資源。若要從 1.x 移轉至 2.0，需要進行一些變更。

    1. 更新 `requirements.txt`，或從 PyPI 透過 `pip install -U ibm-cos-sdk` 進行。使用 `pip list | grep ibm-cos` 驗證沒有舊版本存在。
    2. 將任何匯入宣告從 `boto3` 更新為 `ibm_boto3`。
    3. 必要的話，更新 `requirements.txt`，或從 PyPI 透過 `pip install boto3` 進行，重新安裝原始的 `boto3`。

## 建立用戶端及讀取認證
{: #python-credentials}

為了連接至 COS，會藉由提供認證資訊（API 金鑰及服務實例 ID），來建立及配置用戶端。這些值也可以自動從 credentials 檔案或環境變數中取得。

產生[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)後，會將產生的 JSON 文件儲存為 `~/.bluemix/cos_credentials`。除非在建立用戶端期間，明確地設定其他認證，否則 SDK 將自動從此檔案讀取認證。如果 `cos_credentials` 檔案包含 HMAC 金鑰，用戶端將以簽章進行鑑別，否則用戶端會使用提供的 API 金鑰以持有人記號進行鑑別。

如果從 AWS S3 移轉，您也可以使用下列格式從 `~/.aws/credentials` 讀取認證資料：

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

如果 `~/.bluemix/cos_credentials` 和 `~/.aws/credentials` 同時存在，則 `cos_credentials` 會優先。

### 收集必要資訊
{: #python-prereqs}

下列變數會出現在範例中：

* `bucket_name` 必須是[唯一且 DNS 安全](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)的字串。因為儲存區名稱在整個系統中是唯一的，因此如果此範例執行多次，將需要變更這些值。請注意，名稱在刪除之後會保留 10-15 分鐘。
* `ibm_api_key_id` 是[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中作為 `apikey` 找到的值。
* `ibm_service_instance_id` 是[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中作為 `resource_instance_id` 找到的值。 
* `endpoint_url` 是服務端點 URL，包含 `https://` 通訊協定。這個值**不是**在[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)中找到的 `endpoints` 值。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `LocationConstraint` 是與 `endpoint` 值對應的[有效佈建碼](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)。 


## 程式碼範例
{: #python-examples}

程式碼範例是使用 **Python 2.7.15** 撰寫

### 起始設定配置
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
*金鑰值*
* `<endpoint>` - 雲端物件儲存空間的公用端點，且具有綱目字首 ('https://')（可從 [IBM Cloud 儀表板](https://cloud.ibm.com/resources){:new_window}取得）。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<api-key>` - 建立服務認證時產生的 API 金鑰（建立及刪除範例需要寫入權）。
* `<resource-instance-id>` - 雲端物件儲存空間的資源 ID（可透過 [IBM Cloud CLI](/docs/cli?topic=cloud-cli-idt-cli) 或 [IBM Cloud 儀表板](https://cloud.ibm.com/resources){:new_window}取得）。
* `<location>` - 雲端物件儲存空間的預設位置（必須符合用於 `<endpoint>` 的地區）。

*SDK 參照*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### 建立新的儲存區
{: #python-examples-new-bucket}

`LocationConstraint` 的有效佈建碼清單可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中參閱。

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

*SDK 參照*
* 類別
  * [`Bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 方法
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### 建立新的文字檔
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

*SDK 參照*
* 類別
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 方法
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### 列出可用的儲存區
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

*SDK 參照*
* 類別
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* 集合
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### 列出儲存區中的項目
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

*SDK 參照*
* 類別
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* 集合
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### 取得特定項目的檔案內容
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

*SDK 參照*
* 類別
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 方法
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### 從儲存區刪除項目
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

*SDK 參照*
* 類別
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 方法
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### 從儲存區刪除多個項目
{: #python-examples-delete-multiple-objects}

刪除要求最多可以包含您要刪除的 1000 個金鑰。雖然這對於減少每個要求的額外負擔非常有用，但在刪除許多金鑰時，請注意。此外，也請考量物件的大小，以確保適當的效能。
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

*SDK 參照*
* 類別
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 方法
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### 刪除儲存區
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

*SDK 參照*
* 類別
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 方法
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### 執行多部分上傳
{: #python-examples-multipart}

#### 上傳二進位檔（偏好的方法）
{: #python-examples-multipart-binary}

[S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} 類別的 [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} 方法會視需要自動執行多部分上傳。[TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} 類別用來判斷使用多部分上傳的臨界值。

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

*SDK 參照*
* 類別
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* 方法
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### 手動執行多部分上傳
{: #python-examples-multipart-manual}

想要的話，[S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} 類別可以用來執行多部分上傳。這適用於需要對上傳處理程序有更多控制權的情況。

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

*SDK 參照*
* 類別
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 方法
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### 使用 TransferManager 的大型物件上傳
{: #python-examples-multipart-transfer}

`TransferManager` 提供另一種方式來執行大型檔案傳送，方法是在必須設定配置參數時自動併入多部分上傳。

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

### 列出儲存區中的項目（第 2 版）
{: #python-examples-list-objects-v2}

[S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} 物件包含更新的方法，以列出內容 ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window})。此方法可讓您限制傳回的記錄數，並分批次擷取記錄。這可能有助於在應用程式內將您的結果進行分頁，並改善效能。

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

*SDK 參照*
* 類別
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 方法
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## 使用 Key Protect
{: #python-examples-kp}
Key Protect 可新增至儲存空間儲存區，以加密雲端中靜止的機密資料。

### 開始之前
{: #python-examples-kp-prereqs}

為了建立儲存區並啟用 Key Protect，需要下列項目：

* [已佈建](/docs/services/key-protect?topic=key-protect-provision) Key Protect 服務
* 根金鑰可用（[已產生](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已匯入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 擷取根金鑰 CRN
{: #python-examples-kp-root}

1. 擷取 Key Protect 服務的[實例 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 來擷取所有[可用金鑰](https://cloud.ibm.com/apidocs/key-protect)
    * 您可以使用 `curl` 指令或 API REST 用戶端（例如 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）來存取 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 擷取您用來在儲存區上啟用 Key Protect 的根金鑰 CRN。CRN 看起來如下：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 建立儲存區並啟用 key-protect
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

*金鑰值*
* `<algorithm>` - 用於新增至儲存區的新物件加密演算法（預設為 AES256）。
* `<root-key-crn>` - 從 Key Protect 服務取得的根金鑰 CRN。

*SDK 參照*
* 類別
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 方法
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## 使用 Aspera 高速傳輸
{: #python-examples-aspera}

透過安裝 [Aspera 高速傳送程式庫](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)，您可以在應用程式中使用高速檔案傳送。Aspera 程式庫是封閉的來源，因此對於 COS SDK（其使用 Apache 授權）具有選用的相依關係。

每一個 Aspera 階段作業都會建立在用戶端機器上執行的個別 `ascp` 處理程序，以執行傳送。請確定您的運算環境可以容許執行此處理程序。
{:tip}


### 起始設定 AsperaTransferManager
{: #python-examples-aspera-init}

在起始設定 `AsperaTransferManager` 之前，請確定您有工作中的 [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}（不是 `resource` 或 `session`）物件。

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

您需要提供 IAM API 金鑰，以進行 Aspera 高速傳送。[HMAC 認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} 目前**不**支援。如需 IAM 的相關資訊，[請按一下這裡](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)。
{:tip}

為了得到最高的傳輸量，請將傳送分割成指定數目的平行**階段作業** ，以傳送大小由 **threshold** 值定義的資料區塊。

使用多個階段作業的一般配置應該是：
* 2500 MBps 目標率
* 100 MB 臨界值（*此為大部分應用程式的建議值*）

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
在上述範例中，SDK 會大量產生足夠的階段作業，以嘗試達到目標率 2500 MBps。

或者，可以在 SDK 中明確配置階段作業管理。在需要更精確控制網路使用率的情況下，這十分有用。

使用明確的多個階段作業的一般配置應該是：
* 2 或 10 個階段作業
* 100 MB 臨界值（*此為大部分應用程式的建議值*）

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
若要在大部分情境下具有最佳效能，請一律使用多個階段作業，讓與實例化 Aspera 高速傳送相關聯的任何額外負擔減到最少。**如果您的網路容量至少為 1 Gbps，您應該使用 10 個階段作業。**較低頻寬的網路應該使用兩個階段作業。
{:tip}

### 檔案上傳
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

*金鑰值*
* `<bucket-name>` - 目標儲存區名稱
* `<absolute-path-to-file>` - 要上傳之檔案的目錄路徑及檔名
* `<item-name>` - 新增至儲存區之新檔案的名稱

### 檔案下載
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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-file>` - 將檔案儲存到本端系統的目錄及檔名。
* `<object-to-download>` - 要下載之儲存區檔案的名稱。

### 目錄上傳
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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-directory>` - 包含要上傳之檔案的本端目錄。必須具有前導和尾端 `/`（例如 `/Users/testuser/Documents/Upload/`）
* `<object prefix>` - 要儲存檔案之儲存區裡的名稱目錄。不得具有前導斜線 `/`（例如 `newuploads/`）

### 目錄下載
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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-directory>` - 要儲存所下載檔案的本端目錄。必須具有前導和尾端斜線 `/`（例如 `/Users/testuser/Downloads/`）
* `<object prefix>` - 要儲存檔案之儲存區裡的名稱目錄。不得具有前導斜線 `/`（例如 `todownload/`）

### 使用訂閱者
{: #python-examples-aspera-subscribers}

訂閱者藉由連接自訂回呼方法而提供對於傳送的觀察。所有傳送會在下列階段之間轉移：

`Queued - In Progress - Done`

每個階段有三個可用的訂閱者：

* `CallbackOnQueued()` - 有新的傳送新增至 `AsperaTransferManager` 時呼叫
* `CallbackOnProgress()` - 傳送已開始傳輸資料時呼叫（傳送進行中時會反覆地發動）。
* `CallbackOnDone()` - 傳送完成之後呼叫

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

*金鑰值*
* `<bucket-name>` - 啟用 Aspera 的 Object Storage 服務實例的儲存區名稱。
* `<absolute-path-to-directory>` - 要儲存所下載檔案的本端目錄。必須具有前導和尾端斜線 `/`（例如 `/Users/testuser/Downloads/`）
* `<object prefix>` - 要儲存檔案之儲存區裡的名稱目錄。不得具有前導斜線 `/`（例如 `todownload/`）

上面的範例程式碼會產生下列輸出：

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### 暫停/繼續/取消
{: #python-examples-aspera-pause}

SDK 可讓您透過 `AsperaTransferFuture` 物件的下列方法來管理檔案/目錄傳送的進度：

* `pause()`
* `resume()`
* `cancel()`

呼叫上述任一種方法時，不會產生任何副作用。SDK 會處理適當的清除及整理。
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

### 疑難排解 Aspera 問題
{: #python-examples-aspera-ts}
**問題：**在 Windows 10 上使用 Python 2.7.15 的開發人員可能會在安裝 Aspera SDK 時遭遇失敗。

**原因：**如果在您的環境安裝了不同版本的 Python，則您可能會在嘗試安裝 Aspera SDK 時遭遇安裝失敗。這可能是遺漏 DLL 檔案或路徑中有錯誤的 DLL 所致。

**解決方案：**解決此問題的首要步驟是重新安裝 Aspera 程式庫。安裝期間可能有失敗。因此，這可能已影響 DLL 檔案。如果那未解決問題，則您必須更新 Python 的版本。如果您無法這麼做，可以安裝 [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window}。這將容許您安裝 Aspeara SDK 而不會有任何問題。

## 更新 meta 資料
{: #python-examples-metadata}
有兩種方式可更新現有物件上的 meta 資料：
* 具有新 meta 資料及原始物件內容的 `PUT` 要求
* 使用新 meta 資料來執行 `COPY` 要求，並指定原始物件作為副本來源

### 使用 PUT 更新 meta 資料
{: #python-examples-metadata-put}
**附註：**`PUT` 要求會改寫物件的現有內容，因此必須先下載並重新上傳具有新 meta 資料的內容。

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

### 使用 COPY 更新 meta 資料
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

## 使用 Immutable Object Storage
{: #python-examples-immutable}

### 將保護配置新增至現有儲存區
{: #python-examples-immutable-add}

在保護期間過期，並移除物件上的所有合法保留之前，無法刪除寫入受保護儲存區的物件。除非在物件建立時提供物件特定值，否則會將儲存區的預設保留值提供給物件。不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。 

保留期間設定值 `MinimumRetention`、`DefaultRetention` 及 `MaximumRetention` 的最小和最大支援值分別是 0 天到 365243 天（1000 年）。 

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

### 檢查儲存區的保護
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


### 上傳受保護物件
{: #python-examples-immutable-upload}

不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。


|值	|類型|說明|
| --- | --- | --- | 
|`Retention-Period` | 非負整數（秒）| 儲存在物件上的保留期間（以秒為單位）。除非已過保留期間中指定的時間，否則無法改寫或刪除物件。如果指定此欄位及 `Retention-Expiration-Date`，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 `DefaultRetention` 期間。零 (`0`) 是合法值，假設儲存區最小保留期間也為 `0`。|
| `Retention-expiration-date` | 日期（ISO 8601 格式）|在此日期將可以合法刪除或修改物件。您只能指定此項或 Retention-Period 標頭。如果兩者都指定，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 DefaultRetention 期間。|
| `Retention-legal-hold-id` |字串   | 要套用至物件的單一合法保留。合法保留是 Y 字元長字串。除非已移除與物件相關聯的所有合法保留，否則無法改寫或刪除物件。|

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

### 新增或移除受保護物件的合法保留
{: #python-examples-immutable-legal-hold}

物件可支援 100 個合法保留：

*  合法保留 ID 是長度上限為 64 個字元的字串，且長度下限為 1 個字元。有效字元為字母、數字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 及 `。
* 如果給定合法保留新增數超過物件上的 100 個合法保留總數，則不會新增合法保留，將會傳回 `400` 錯誤。
* 如果 ID 太長，將不會新增到物件中，且會傳回 `400` 錯誤。
* 如果 ID 包含無效的字元，則不會將它新增至物件，且會傳回 `400` 錯誤。
* 如果某個 ID 已在物件上使用，則不會修改現有合法保留，且回應會指出 ID 已在使用中，並且有 `409` 錯誤。
* 如果物件沒有保留期間 meta 資料，則會傳回 `400` 錯誤，且不容許新增或移除合法保留。


正在新增或移除合法保留的使用者必須具有此儲存區的 `Manager` 許可權。


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

### 延長受保護物件的保留期間
{: #python-examples-immutable-extend}

只能延長物件的保留期間。不能從目前配置的值縮短。

保留擴充值以三種方式之一來設定：

* 現行值再加上時間（`Additional-Retention-Period` 或類似方法）
* 新擴充期間（以秒為單位）（`Extend-Retention-From-Current-Time` 或類似方法）
* 物件的新保留到期日（`New-Retention-Expiration-Date` 或類似方法）

儲存在物件 meta 資料中的現行保留期間，會增加給定的額外時間，或以新值取代，視 `extendRetention` 要求中設定的參數而定。在所有情況下，會針對現行保留期間檢查延長保留參數，而且只有在更新的保留期間大於現行保留期間時，才會接受延長的參數。

不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。



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

### 列出受保護物件的合法保留
{: #python-examples-immutable-list-holds}

此作業傳回：

* 物件建立日期
* 物件保留期間（秒）
* 根據期間和建立日期計算的保留到期日
* 合法保留的清單
* 合法保留 ID
* 套用合法保留時的時間戳記

如果物件沒有任何合法保留，則會傳回空的 `LegalHoldSet`。如果在物件上未指定保留期間，則會傳回 `404` 錯誤。


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

