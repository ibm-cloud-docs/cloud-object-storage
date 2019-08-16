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

# Python 사용
{: #python}

Python 지원은 `boto3` 라이브러리의 분기를 통해 지원됩니다. 이는 `pip install ibm-cos-sdk`를 통해 Python Package Index로부터 설치할 수 있습니다. 

소스 코드는 [GitHub](https://github.com/ibm/ibm-cos-sdk-python/)에서 찾을 수 있습니다. 

`ibm_boto3` 라이브러리는 {{site.data.keyword.cos_full}} API에 대한 완전한 액세스를 제공합니다. 다음 기본 예에 표시되어 있는 바와 같이 서비스 리소스 또는 하위 레벨 클라이언트의 작성 중에는 엔드포인트, API 키 및 인스턴스 ID를 반드시 지정해야 합니다. 

서비스 인스턴스 ID는 _리소스 인스턴스 ID_라고도 합니다. 해당 값은 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)를 작성하여 찾거나 CLI를 통해 찾을 수 있습니다.
{:tip}

자세한 문서는 [여기](https://ibm.github.io/ibm-cos-sdk-python/)에서 찾을 수 있습니다. 

## 1.x.x에서의 업그레이드
{: #python-migrate}

이 SDK의 2.0 버전에서는 애플리케이션이 기존 `boto3` 라이브러리를 사용하여 동일한 애플리케이션 또는 환경 내의 AWS 리소스에 연결할 수 있도록 하는 네임스페이스 변경사항이 도입되었습니다. 1.x에서 2.0으로 마이그레이션하려면 몇 가지 변경이 필요합니다. 

    1. `requirements.txt`를 업데이트하거나 `pip install -U ibm-cos-sdk`를 통해 PyPI로부터 설치하십시오. `pip list | grep ibm-cos`로 다른 이전 버전이 없는지 확인하십시오. 
    2. `boto3`에서 `ibm_boto3`로 가져오기 선언을 업데이트하십시오. 
    3. 필요한 경우에는 `requirements.txt`를 업데이트하거나 `pip install boto3`를 통해 PyPI로부터 기존 `boto3`를 다시 설치하십시오. 

## 클라이언트 작성 및 인증 정보 제공
{: #python-credentials}

COS에 연결하기 위해, 인증 정보(API 키 및 서비스 인스턴스 ID) 제공을 통해 클라이언트가 작성되고 구성됩니다. 이러한 값은 인증 정보 파일 또는 환경 변수로부터 자동으로 가져올 수도 있습니다. 

[서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)를 생성하고 나면 결과 JSON 문서를 `~/.bluemix/cos_credentials`에 저장할 수 있습니다. 이 SDK는 클라이언트 작성 중에 다른 인증 정보가 명시적으로 설정되지 않은 한 이 파일에서 자동으로 인증 정보를 가져옵니다. `cos_credentials` 파일이 HMAC 키를 포함하는 경우에는 클라이언트가 서명을 사용하여 인증하며, 그렇지 않은 경우에는 제공된 API 키를 사용하여 Bearer 토큰으로 인증합니다. 

AWS S3에서 마이그레이션하는 경우에는 다음 형식으로 `~/.aws/credentials`에서 인증 정보 데이터를 가져올 수도 있습니다. 

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

`~/.bluemix/cos_credentials`와 `~/.aws/credentials`가 모두 있는 경우에는 `cos_credentials`가 우선권을 갖습니다. 

### 필수 정보 수집
{: #python-prereqs}

이 예에는 다음 변수가 있습니다. 

* `bucket_name`은 [고유하며 DNS를 준수하는](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket) 문자열이어야 합니다. 버킷 이름은 전체 시스템에서 고유하므로 이 예가 여러 번 실행되는 경우에는 이러한 값을 변경해야 합니다. 이름은 삭제 후에도 10 - 15분 정도 예약되어 있다는 점을 참고하십시오. 
* `ibm_api_key_id`는 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)에서 `apikey`로 찾을 수 있는 값입니다. 
* `ibm_service_instance_id`는 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)에서 `resource_instance_id`로 찾을 수 있는 값입니다.  
* `endpoint_url`은 `https://` 프로토콜을 포함하는 서비스 엔드포인트 URL입니다. 이 값은 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)에서 찾을 수 있는 `endpoints` 값이 **아닙니다**. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 
* `LocationConstraint`는 `endpoint` 값에 대응하는 [유효한 프로비저닝 코드](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)입니다.  


## 코드 예
{: #python-examples}

코드 예는 **Python 2.7.15**를 사용하여 작성되었습니다. 

### 구성 초기화
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
*키 값*
* `<endpoint>` - 스키마가 접두부에 포함된('https://'), Cloud Object Storage의 공용 엔드포인트입니다([IBM Cloud 대시보드](https://cloud.ibm.com/resources){:new_window}에서 사용 가능). 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 
* `<api-key>` - 서비스 인증 정보를 작성할 때 생성된 API 키입니다(작성 및 삭제 예의 경우 쓰기 액세스 권한 필요). 
* `<resource-instance-id>` - Cloud Object Storage의 리소스 ID입니다([IBM Cloud CLI](/docs/cli?topic=cloud-cli-idt-cli) 또는 [IBM Cloud 대시보드](https://cloud.ibm.com/resources){:new_window}를 통해 사용 가능). 
* `<location>` - Cloud Object Storage의 기본 위치입니다(`<endpoint>`에 대해 사용된 지역과 일치해야 함). 

*SDK 참조*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### 새 버킷 작성
{: #python-examples-new-bucket}

`LocationConstraint`에 대한 유효한 프로비저닝 코드의 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)에서 참조할 수 있습니다. 

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

*SDK 참조*
* 클래스
  * [`Bucket`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 메소드
    * [`create`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### 새 텍스트 파일 작성
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

*SDK 참조*
* 클래스
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 메소드
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### 사용 가능한 버킷 나열
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

*SDK 참조*
* 클래스
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* 콜렉션
    * [buckets](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### 버킷에 있는 항목 나열
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

*SDK 참조*
* 클래스
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* 콜렉션
    * [objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### 특정 항목의 파일 컨텐츠 가져오기
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

*SDK 참조*
* 클래스
    * [`Object`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 메소드
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### 버킷에서 항목 삭제
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

*SDK 참조*
* 클래스
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* 메소드
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### 버킷에서 여러 항목 삭제
{: #python-examples-delete-multiple-objects}

삭제 요청은 최대 1000개의 삭제할 키를 포함할 수 있습니다. 이는 요청당 오버헤드를 줄이는 데 유용하지만, 많은 키를 삭제하는 경우에는 주의하십시오. 또한, 적절한 성능을 보장하기 위해 오브젝트의 크기도 고려하십시오.
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

*SDK 참조*
* 클래스
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 메소드
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### 버킷 삭제
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

*SDK 참조*
* 클래스
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 메소드
    * [delete](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### 다중 파트 업로드 실행
{: #python-examples-multipart}

#### 2진 파일 업로드(선호되는 방법)
{: #python-examples-multipart-binary}

[S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} 클래스의 [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} 메소드는 필요한 경우 자동으로 다중 파트 업로드를 실행합니다. [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} 클래스는 다중 파트 업로드 사용에 대한 임계값을 결정하는 데 사용됩니다. 

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

*SDK 참조*
* 클래스
    * [Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* 메소드
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### 수동으로 다중 파트 업로드 실행
{: #python-examples-multipart-manual}

원하는 경우에는 [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} 클래스를 사용하여 다중 파트 업로드를 수행할 수 있습니다. 이는 업로드 프로세스에 대해 더 큰 제어 능력이 필요한 경우 유용합니다. 

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

*SDK 참조*
* 클래스
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 메소드
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### TransferManager를 사용한 대형 오브젝트 업로드
{: #python-examples-multipart-transfer}

`TransferManager`는 필요할 때마다 구성 매개변수를 설정하여 자동으로 다중 파트 업로드를 포함시킴으로써 대형 파일 전송을 실행하는 다른 방법을 제공합니다. 

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

### 버킷에 있는 항목 나열(v2)
{: #python-examples-list-objects-v2}

[S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} 오브젝트에는 컨텐츠를 나열하는 데 사용할 수 있는 업데이트된 메소드([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window})가 포함되어 있습니다. 이 메소드는 리턴되는 레코드의 수를 제한하고 레코드를 배치로 검색할 수 있도록 합니다. 이는 애플리케이션에서 결과에 대한 페이징을 수행하고 성능을 향상시키는 데 유용합니다. 

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

*SDK 참조*
* 클래스
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* 메소드
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Key Protect 사용
{: #python-examples-kp}
클라우드의 민감한 저장 데이터를 암호화하기 위해 Key Protect를 스토리지 버킷에 추가할 수 있습니다. 

### 시작하기 전에
{: #python-examples-kp-prereqs}

Key Protect가 사용으로 설정된 버킷을 작성하려면 다음 항목이 필요합니다. 

* [프로비저닝](/docs/services/key-protect?topic=key-protect-provision)된 Key Protect 서비스
* 사용 가능한 루트 키([생성](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) 또는 [가져오기](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)를 통해 확보)

### 루트 키 CRN 검색
{: #python-examples-kp-root}

1. Key Protect 서비스의 [인스턴스 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)를 검색하십시오. 
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)를 사용하여 [사용 가능한 키](https://cloud.ibm.com/apidocs/key-protect)를 모두 검색하십시오. 
    * `curl` 명령을 사용하거나 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)과 같은 API REST 클라이언트를 사용하여 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)에 액세스할 수 있습니다. 
3. 버킷에서 Key Protect를 사용으로 설정하는 데 사용할 루트 키의 CRN을 검색하십시오. 이 CRN은 아래와 같습니다. 

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect가 사용으로 설정된 버킷 작성
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

*키 값*
* `<algorithm>` - 버킷에 추가된 새 오브젝트에 사용되는 암호화 알고리즘입니다(기본값은 AES256). 
* `<root-key-crn>` - Key Protect 서비스로부터 얻은 루트 키의 CRN입니다. 

*SDK 참조*
* 클래스
    * [Bucket](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* 메소드
    * [create](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Aspera 고속 전송 사용
{: #python-examples-aspera}

[Aspera 고속 전송 라이브러리](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging)를 설치하면 애플리케이션에서 고속 파일 전송을 이용할 수 있습니다. Aspera 라이브러리는 클로즈드 소스이므로 COS SDK(Apache 라이센스를 사용함)의 선택적 종속 항목입니다. 

각 Aspera 세션은 전송을 수행하기 위해 클라이언트 시스템에서 실행되는 개별 `ascp` 프로세스를 작성합니다. 자신의 컴퓨팅 환경이 이 프로세스의 실행을 허용하는지 확인하십시오.
{:tip}


### AsperaTransferManager 초기화
{: #python-examples-aspera-init}

`AsperaTransferManager`를 초기화하기 전에 작동하는 [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}(`resource` 또는 `session`이 아님) 오브젝트가 있는지 확인하십시오. 

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

Aspera 고속 전송을 위해서는 IAM API 키를 제공해야 합니다. [HMAC 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window}는 현재 지원되지 **않습니다**. IAM에 대한 자세한 정보를 보려면 [여기를 클릭](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)하십시오.
{:tip}

처리량을 최고로 높이려면 **임계값** 값으로 크기가 정의된 데이터 청크를 전송하는 병렬 **세션**의 지정된 수만큼 전송을 분할하십시오. 

다중 세션 사용에 대한 일반적인 구성은 다음과 같습니다. 
* 2500MBps의 목표 속도
* 100MB의 임계값(*이는 대부분의 애플리케이션에 대해 권장되는 값임*)

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
위 예에서 이 SDK는 목표 속도인 2500MBps에 도달하기 위해 충분한 세션을 생성합니다. 

또는 SDK에서 세션 관리를 명시적으로 구성할 수도 있습니다. 이는 네트워크 이용을 더 정확하게 제어해야 하는 경우 유용합니다. 

명시적 다중 세션 사용에 대한 일반적인 구성은 다음과 같습니다. 
* 2개 또는 10개의 세션
* 100MB의 임계값(*이는 대부분의 애플리케이션에 대해 권장되는 값임*)

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
대부분의 시나리오에서 최상의 성능을 얻으려면 항상 여러 세션을 사용하여 Aspera 고속 전송의 인스턴스화와 연관된 오버헤드를 최소화하십시오. **네트워크 용량이 1Gbps 이상인 경우에는 10개의 세션을 사용해야 합니다.** 이보다 느린 대역폭에서는 두 개의 세션을 사용해야 합니다.
{:tip}

### 파일 업로드
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

*키 값*
* `<bucket-name>` - 대상 버킷의 이름
* `<absolute-path-to-file>` - 업로드되는 파일의 파일 이름 및 디렉토리 경로
* `<item-name>` - 버킷에 추가되는 새 파일의 이름

### 파일 다운로드
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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-file>` - 로컬 시스템에 파일을 저장하는 데 사용할 디렉토리 및 파일 이름입니다. 
* `<object-to-download>` - 버킷에서 다운로드할 파일의 이름입니다. 

### 디렉토리 업로드
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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-directory>` - 업로드할 파일을 포함하는 로컬 디렉토리입니다. 선행 및 후행 `/`가 있어야 합니다(예: `/Users/testuser/Documents/Upload/`). 
* `<object prefix>` - 파일을 저장할 버킷의 디렉토리입니다. 선행 슬래시 `/`가 없어야 합니다(예: `newuploads/`). 

### 디렉토리 다운로드
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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-directory>` - 다운로드한 파일을 저장할 로컬 디렉토리입니다. 선행 및 후행 슬래시 `/`가 있어야 합니다(예: `/Users/testuser/Downloads/`). 
* `<object prefix>` - 파일을 저장할 버킷의 디렉토리입니다. 선행 슬래시 `/`가 없어야 합니다(예: `todownload/`). 

### 구독자 사용
{: #python-examples-aspera-subscribers}

구독자는 사용자 정의 콜백 메소드를 첨부함으로써 전송에 대한 관찰 기능을 제공합니다. 모든 전송은 다음 단계를 거쳐 상태 전이됩니다. 

`Queued - In Progress - Done`

각 단계에 대해 사용 가능한 구독자는 세 가지입니다. 

* `CallbackOnQueued()` - 새 전송이 `AsperaTransferManager`에 추가되면 호출됨
* `CallbackOnProgress()` - 전송이 데이터 전송을 시작하면 전송됨(전송 진행 중에 반복적으로 실행됨)
* `CallbackOnDone()` - 전송이 완료되면 호출됨

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

*키 값*
* `<bucket-name>` - Aspera가 사용으로 설정된 Object Storage 서비스 인스턴스에 있는 버킷의 이름입니다. 
* `<absolute-path-to-directory>` - 다운로드한 파일을 저장할 로컬 디렉토리입니다. 선행 및 후행 슬래시 `/`가 있어야 합니다(예: `/Users/testuser/Downloads/`). 
* `<object prefix>` - 파일을 저장할 버킷의 디렉토리입니다. 선행 슬래시 `/`가 없어야 합니다(예: `todownload/`). 

위 샘플 코드는 다음 출력을 생성합니다. 

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### 일시정지/재개/취소
{: #python-examples-aspera-pause}

이 SDK는 `AsperaTransferFuture` 오브젝트의 다음 메소드를 통해 파일/디렉토리 전송의 진행상태를 관리하는 기능을 제공합니다. 

* `pause()`
* `resume()`
* `cancel()`

위에 간략히 설명된 메소드를 호출하는 데는 부작용이 없습니다. 적절한 정리 및 하우스키핑은 SDK에 의해 처리됩니다.
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

### Aspera 문제점 해결
{: #python-examples-aspera-ts}
**문제:** Windows 10에서 Python 2.7.15를 사용하고 있는 개발자의 경우에는 Aspera SDK를 설치할 때 실패가 발생할 수 있습니다. 

**원인:** 환경에 설치된 다른 Python 버전이 없는 경우에는 Aspera SDK를 설치하려 할 때 설치 실패가 발생할 수 있습니다. 이는 경로의 누락된 DLL 파일 또는 올바르지 않은 DLL로 인해 발생할 수 있습니다. 

**해결책:** 이 문제를 해결하는 첫 번째 단계는 Aspera 라이브러리를 다시 설치하는 것입니다. 설치 중에 실패가 있었을 수 있습니다. 그 결과로 DLL 파일이 영향을 받았을 수 있습니다. 이렇게 해도 문제가 해결되지 않은 경우에는 자신의 Python 버전을 업데이트해야 합니다. 이를 수행할 수 없는 경우에는 [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window}을 설치할 수 있습니다. 이는 아무 문제 없이 Aspera SDK를 설치할 수 있게 해 줍니다. 

## 메타데이터 업데이트
{: #python-examples-metadata}
기존 오브젝트의 메타데이터를 업데이트하는 데는 두 가지 방법이 있습니다. 
* 새 메타데이터와 원본 오브젝트 컨텐츠를 사용한 `PUT` 요청
* 원본 오브젝트를 복사 소스로 지정하여, 새 메타데이터로 `COPY` 요청 실행

### PUT을 사용한 메타데이터 업데이트
{: #python-examples-metadata-put}
**참고:** `PUT` 요청은 오브젝트의 기존 컨텐츠를 겹쳐쓰므로 먼저 이를 다운로드한 후 새 메타데이터로 다시 업로드해야 합니다. 

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

### COPY를 사용한 메타데이터 업데이트
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

## 불변 오브젝트 스토리지 사용
{: #python-examples-immutable}

### 기존 버킷에 보호 구성 추가
{: #python-examples-immutable-add}

보호된 버킷에 작성된 오브젝트는 보호 기간이 만료되어 오브젝트에 대한 모든 법적 보존이 제거될 때까지 삭제할 수 없습니다. 오브젝트가 작성될 때 오브젝트 고유 값이 제공되지 않으면 버킷의 기본 보존 값이 오브젝트에 지정됩니다. 보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다.  

보존 기간 설정 `MinimumRetention`, `DefaultRetention` 및 `MaximumRetention`에 대해 지원되는 최소 및 최대값은 각각 0일과 365243일(1000년)입니다.  

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

### 버킷에 대한 보호 확인
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


### 보호된 오브젝트 업로드
{: #python-examples-immutable-upload}

보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다. 


|값 | 유형 |설명 |
| --- | --- | --- | 
|`Retention-Period` | 음수가 아닌 정수(초) | 오브젝트에 저장할 보존 기간(초)입니다. 오브젝트는 보존 기간에 지정된 시간이 경과하기 전까지 겹쳐쓰거나 삭제할 수 없습니다. 이 필드와 `Retention-Expiration-Date`가 모두 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 `DefaultRetention` 기간이 사용됩니다. 영(`0`)은 버킷의 최소 보존 기간도 `0`이라고 가정하는 적법한 값입니다. |
| `Retention-expiration-date` | 날짜(ISO 8601 형식) | 오브젝트를 적법하게 삭제하거나 수정할 수 있는 날짜입니다. 이 헤더 또는 Retention-Period 헤더만 지정할 수 있습니다. 둘 다 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 DefaultRetention 기간이 사용됩니다. |
| `Retention-legal-hold-id` | 문자열 | 오브젝트에 적용할 단일 법적 보존입니다. 법적 보존은 Y자 길이의 문자열입니다. 오브젝트는 해당 오브젝트와 연관된 모든 법적 보존이 제거될 때까지 겹쳐쓰거나 삭제할 수 없습니다. |

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

### 보호된 오브젝트에서 법적 보존을 추가하거나 제거
{: #python-examples-immutable-legal-hold}

오브젝트는 100개의 법적 보존을 지원할 수 있습니다. 

*  법적 보존 ID는 최대 64자이고 최소 1자인 문자열입니다. 유효한 문자는 문자, 숫자, `!`, `_`, `.`, `*`, `(`, `)`, `-` 및 `입니다. 
* 특정 법적 보존을 추가했을 때 오브젝트의 총 100개 법적 보존 한계를 초과하는 경우에는 해당 새 법적 보존이 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID가 너무 긴 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID에 올바르지 않은 문자가 포함되어 있는 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID가 이미 오브젝트에서 사용 중인 경우에는 기존 법적 보존이 수정되지 않으며 해당 ID가 이미 사용 중임을 나타내는 응답이 `409` 오류와 함께 표시됩니다. 
* 오브젝트에 보존 기간 메타데이터가 없는 경우에는 `400` 오류가 리턴되며 법적 보존 추가 또는 제거가 허용되지 않습니다. 


법적 보존을 추가하거나 제거하는 사용자에게는 해당 버킷에 대한 `Manager` 권한이 있어야 합니다. 


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

### 보호된 오브젝트의 보존 기간 연장
{: #python-examples-immutable-extend}

오브젝트의 보존 기간은 연장만 가능합니다. 현재 구성된 값에서 줄일 수는 없습니다. 

보존 연장 값은 다음 세 가지 방법 중 하나로 설정됩니다. 

* 현재 값에서의 추가 시간(`Additional-Retention-Period` 또는 이와 유사한 메소드)
* 초 단위의 새 연장 기간(`Extend-Retention-From-Current-Time` 또는 이와 유사한 메소드)
* 오브젝트의 새 보존 만료 날짜(`New-Retention-Expiration-Date` 또는 이와 유사한 메소드)

오브젝트 메타데이터에 저장된 현재 보존 기간은 `extendRetention` 요청에 설정된 매개변수에 따라 지정된 추가 시간만큼 증가되거나, 새 값으로 대체될 수 있습니다. 모든 경우에 보존 연장 매개변수는 현재 보존 기간에 대해 확인되며, 연장된 매개변수는 업데이트된 보존 기간이 현재 보존 기간보다 긴 경우에만 허용됩니다. 

보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다. 



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

### 보호된 오브젝트에 대한 법적 보존 나열
{: #python-examples-immutable-list-holds}

이 오퍼레이션은 다음 항목을 리턴합니다. 

* 오브젝트 작성 날짜
* 오브젝트 보존 기간(초)
* 보존 기간 및 작성 날짜를 기반으로 계산된 보존 만료 날짜
* 법적 보존의 목록
* 법적 보존 ID
* 법적 보존이 적용된 시점의 시간소인

오브젝트에 대한 법적 보존이 없는 경우에는 비어 있는 `LegalHoldSet`이 리턴됩니다.
오브젝트에 대해 지정된 보존 기간이 없는 경우에는 `404` 오류가 리턴됩니다. 


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

