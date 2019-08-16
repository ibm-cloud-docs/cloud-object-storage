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

# AWS CLI 사용
{: #aws-cli}

AWS의 공식 명령행 인터페이스는 IBM COS S3 API와 호환됩니다. Python으로 작성된 경우 `pip install awscli`를 통해 PyPI(Python Package Index)에서 설치될 수 있습니다. 기본적으로 액세스 키는 `~/.aws/credentials`에서 가져오지만 환경 변수로 설정될 수도 있습니다.

이러한 예는 CLI의 버전 1.14.2를 사용하여 생성되었습니다. 설치된 버전을 확인하려면 `aws --version`을 실행하십시오.

## {{site.data.keyword.cos_short}}에 연결하도록 CLI 구성
{: #aws-cli-config}

AWS CLI를 구성하려면 `aws configure`를 입력하고 [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) 및 기본 지역 이름을 제공하십시오. AWS S3에서 사용된 "지역 이름"은 {{site.data.keyword.cos_short}}가 새 버킷의 스토리지 클래스를 정의하는 데 사용하는 프로비저닝 코드(`LocationConstraint`)에 해당합니다.

`LocationConstraint`에 대한 올바른 프로비저닝 코드 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)에서 참조할 수 있습니다.

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

이렇게 하면 두 개의 파일이 작성됩니다.

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


또한 환경 변수를 사용하여 HMAC 인증 정보를 설정할 수 있습니다.

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


IBM COS 엔드포인트는 `--endpoint-url` 옵션을 사용하여 가져와야 하며 인증 정보 파일에 설정될 수 없습니다.


## 상위 레벨 구문 명령
{: #aws-cli-high-level}

단순 유스 케이스는 `aws --endpoint-url {endpoint} s3<command>`를 사용하여 완성할 수 있습니다. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 오브젝트는 익숙한 쉘 명령(예: `ls`, `mv`, `cp` 및 `rm`)을 사용하여 관리됩니다. 버킷은 `mb`를 사용하여 작성될 수 있으며 `rb`를 사용하여 삭제될 수 있습니다.

### 서비스 인스턴스 내 모든 버킷 나열
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### 버킷 내 오브젝트 나열
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### 새 버킷 작성
{: #aws-cli-high-level-new-bucket}

**참고**: 개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}

`~/.aws/config` 파일의 기본 지역이 선택한 엔드포인트와 동일한 위치에 해당하는 경우 버킷 작성이 간단합니다.

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### 버킷에 오브젝트 추가
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

또는 파일 이름과 다른 새 오브젝트 키를 설정할 수 있습니다.

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### 동일한 지역 내 하나의 버킷에서 다른 버킷으로 오브젝트 복사
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### 버킷에서 오브젝트 삭제
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### 버킷 제거
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### 사전 서명된 URL 작성
{: #aws-cli-high-level-presign}

CLI는 사전 서명된 URL을 작성할 수 있습니다. 이러한 URL에서는 기존 액세스 제어를 변경하지 않고 오브젝트에 대한 임시 공용 액세스를 허용합니다. 생성된 URL에는 URI를 변조하는 HMAC 서명이 포함되며, 이로 인해 전체 URL 없이 사용자가 공개적으로 액세스 가능한 파일에 액세스할 가능성이 적어집니다.

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

URL의 만기 시간(초)을 설정할 수도 있습니다(기본값은 3600).

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## 하위 레벨 구문 명령
{: #aws-cli-low-level}

AWS CLI는 `s3api` 명령을 사용하여 직접 HTTP 요청과 동일한 응답을 제공하는 직접 API 호출을 허용합니다.

### 버킷 나열:
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

### 버킷 내 오브젝트 나열
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
