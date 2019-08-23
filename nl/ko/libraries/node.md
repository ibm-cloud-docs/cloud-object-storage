---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: node, javascript, sdk

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

# Node.js 사용
{: #node}

## SDK 설치
{: #node-install}

{{site.data.keyword.cos_full}} SDK for Node.js 설치에 대해 선호되는 방법은 Node.js용
[`npm`](https://www.npmjs.com){:new_window} 패키지 관리자를 사용하는 것입니다. 명령행에 다음 명령을
입력하십시오. 

```sh
npm install ibm-cos-sdk
```

소스 코드는 [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window}에서 호스팅되고 있습니다. 

개별 메소드 및 클래스에 대한 세부사항은 [SDK에 대한 API 문서](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}에서 찾을 수 있습니다. 

## 시작하기
{: #node-gs}

### 최소 요구사항
{: #node-gs-prereqs}

이 SDK를 실행하려면 **Node 4.x+**가 필요합니다. 

### 클라이언트 작성 및 인증 정보 제공
{: #node-gs-credentials}

COS에 연결하기 위해, 인증 정보(API 키, 서비스 인스턴스 ID, IBM 인증 엔드포인트) 제공을 통해 클라이언트가 작성되고 구성됩니다. 이러한 값은 인증 정보 파일 또는 환경 변수로부터 자동으로 가져올 수도 있습니다. 

[서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)를 생성하고 나면 결과 JSON 문서를 `~/.bluemix/cos_credentials`에 저장할 수 있습니다. 이 SDK는 클라이언트 작성 중에 다른 인증 정보가 명시적으로 설정되지 않은 한 이 파일에서 자동으로 인증 정보를 가져옵니다. `cos_credentials` 파일이 HMAC 키를 포함하는 경우에는 클라이언트가 서명을 사용하여 인증하며, 그렇지 않은 경우에는 제공된 API 키를 사용하여 Bearer 토큰으로 인증합니다. 

`default` 섹션 표제는 인증 정보의 기본 프로파일 및 연관된 값을 지정합니다. 동일한 공유 구성 파일에 각자의 고유 인증 정보를 포함하는 여러 프로파일을 작성할 수 있습니다. 다음 예는 기본 프로파일을 사용하는 구성 파일을 보여줍니다. 
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

AWS S3에서 마이그레이션하는 경우에는 다음 형식으로 `~/.aws/credentials`에서 인증 정보 데이터를 가져올 수도 있습니다. 

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

`~/.bluemix/cos_credentials`와 `~/.aws/credentials`가 모두 있는 경우에는 `cos_credentials`가 우선권을 갖습니다. 

## 코드 예
{: #node-examples}

### 구성 초기화
{: #node-examples-init}

```javascript
const AWS = require('ibm-cos-sdk');

var config = {
    endpoint: '<endpoint>',
    apiKeyId: '<api-key>',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: '<resource-instance-id>',
};

var cos = new AWS.S3(config);
```
*키 값*
* `<endpoint>` - Cloud Object Storage의 공용 엔드포인트입니다([IBM Cloud 대시보드](https://cloud.ibm.com/resources){:new_window}에서 사용 가능). 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 
* `<api-key>` - 서비스 인증 정보를 작성할 때 생성된 API 키입니다(작성 및 삭제 예의 경우 쓰기 액세스 권한 필요). 
* `<resource-instance-id>` - Cloud Object Storage의 리소스 ID입니다([IBM Cloud CLI](/docs/overview?topic=overview-crn) 또는 [IBM Cloud 대시보드](https://cloud.ibm.com/resources){:new_window}를 통해 사용 가능). 

### 버킷 작성
{: #node-examples-new-bucket}

`LocationConstraint`에 대한 유효한 프로비저닝 코드의 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)에서 참조할 수 있습니다. 

```javascript
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: 'us-standard'
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```


*SDK 참조*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### 텍스트 오브젝트 작성
{: #node-examples-new-file}

```javascript
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName, 
        Key: itemName, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### 버킷 나열
{: #node-examples-list-buckets}

```javascript
function getBuckets() {
    console.log('Retrieving list of buckets');
    return cos.listBuckets()
    .promise()
    .then((data) => {
        if (data.Buckets != null) {
            for (var i = 0; i < data.Buckets.length; i++) {
                console.log(`Bucket Name: ${data.Buckets[i].Name}`);
            }
        }
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### 버킷에 있는 항목 나열
{: #node-examples-list-objects}

```javascript
function getBucketContents(bucketName) {
    console.log(`Retrieving bucket contents from: ${bucketName}`);
    return cos.listObjects(
        {Bucket: bucketName},
    ).promise()
    .then((data) => {
        if (data != null && data.Contents != null) {
            for (var i = 0; i < data.Contents.length; i++) {
                var itemKey = data.Contents[i].Key;
                var itemSize = data.Contents[i].Size;
                console.log(`Item: ${itemKey} (${itemSize} bytes).`)
            }
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### 특정 항목의 파일 컨텐츠 가져오기
{: #node-examples-get-contents}

```javascript
function getItem(bucketName, itemName) {
    console.log(`Retrieving item from bucket: ${bucketName}, key: ${itemName}`);
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log('File Contents: ' + Buffer.from(data.Body).toString());
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### 버킷에서 항목 삭제
{: #node-examples-delete-object}

```javascript
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() =>{
        console.log(`Item: ${itemName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*SDK 참조*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### 버킷에서 여러 항목 삭제
{: #node-examples-multidelete}

삭제 요청은 최대 1000개의 삭제할 키를 포함할 수 있습니다. 오브젝트를 일괄처리로 삭제하는 것은 요청당 오버헤드를 줄이는 데 매우 유용하지만, 많은 키를 삭제하는 경우에는 요청이 완료되는 데 어느 정도 시간이 소요될 수 있다는 점에 주의하십시오. 또한, 적절한 성능을 보장하기 위해 오브젝트의 크기도 고려하십시오.
{:tip}

```javascript
function deleteItems(bucketName) {
    var deleteRequest = {
        "Objects": [
            { "Key": "deletetest/testfile1.txt" },
            { "Key": "deletetest/testfile2.txt" },
            { "Key": "deletetest/testfile3.txt" },
            { "Key": "deletetest/testfile4.txt" },
            { "Key": "deletetest/testfile5.txt" }
        ]        
    }
    return cos.deleteObjects({
        Bucket: bucketName,
        Delete: deleteRequest
    }).promise()
    .then((data) => {
        console.log(`Deleted items for ${bucketName}`);
        console.log(data.Deleted);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });    
}
```

*SDK 참조*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### 버킷 삭제
{: #node-examples-delete-bucket}

```javascript
function deleteBucket(bucketName) {
    console.log(`Deleting bucket: ${bucketName}`);
    return cos.deleteBucket({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucketName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### 다중 파트 업로드 실행
{: #node-examples-multipart}

```javascript
function multiPartUpload(bucketName, itemName, filePath) {
    var uploadID = null;

    if (!fs.existsSync(filePath)) {
        log.error(new Error(`The file \'${filePath}\' does not exist or is not accessible.`));
        return;
    }

    console.log(`Starting multi-part upload for ${itemName} to bucket: ${bucketName}`);
    return cos.createMultipartUpload({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        uploadID = data.UploadId;

        //begin the file upload        
        fs.readFile(filePath, (e, fileData) => {
            //min 5MB part
            var partSize = 1024 * 1024 * 5;
            var partCount = Math.ceil(fileData.length / partSize);
    
            async.timesSeries(partCount, (partNum, next) => {
                var start = partNum * partSize;
                var end = Math.min(start + partSize, fileData.length);
    
                partNum++;

                console.log(`Uploading to ${itemName} (part ${partNum} of ${partCount})`);  

                cos.uploadPart({
                    Body: fileData.slice(start, end),
                    Bucket: bucketName,
                    Key: itemName,
                    PartNumber: partNum,
                    UploadId: uploadID
                }).promise()
                .then((data) => {
                    next(e, {ETag: data.ETag, PartNumber: partNum});
                })
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            }, (e, dataPacks) => {
                cos.completeMultipartUpload({
                    Bucket: bucketName,
                    Key: itemName,
                    MultipartUpload: {
                        Parts: dataPacks
                    },
                    UploadId: uploadID
                }).promise()
                .then(console.log(`Upload of all ${partCount} parts of ${itemName} successful.`))
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            });
        });
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function cancelMultiPartUpload(bucketName, itemName, uploadID) {
    return cos.abortMultipartUpload({
        Bucket: bucketName,
        Key: itemName,
        UploadId: uploadID
    }).promise()
    .then(() => {
        console.log(`Multi-part upload aborted for ${itemName}`);
    })
    .catch((e)=>{
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Key Protect 사용
{: #node-examples-kp}

암호화 키를 관리하기 위해 Key Protect를 스토리지 버킷에 추가할 수 있습니다. IBM COS에서는 모든 데이터가 암호화되지만, Key Protect는 중앙 집중식 서비스를 사용하여 암호화 키를 생성하고, 순환하고, 암호화 키에 대한 액세스를 제어하는 서비스를 제공합니다. 

### 시작하기 전에
{: #node-examples-kp-prereqs}
Key Protect가 사용으로 설정된 버킷을 작성하려면 다음 항목이 필요합니다. 

* [프로비저닝](/docs/services/key-protect?topic=key-protect-provision#provision)된 Key Protect 서비스
* 사용 가능한 루트 키([생성](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) 또는 [가져오기](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)를 통해 확보)

### 루트 키 CRN 검색
{: #node-examples-kp-root}

1. Key Protect 서비스의 [인스턴스 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)를 검색하십시오. 
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)를 사용하여 [사용 가능한 키](https://cloud.ibm.com/apidocs/key-protect)를 모두 검색하십시오. 
    * `curl` 명령을 사용하거나 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)과 같은 API REST 클라이언트를 사용하여 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)에 액세스할 수 있습니다. 
3. 버킷에서 Key Protect를 사용으로 설정하는 데 사용할 루트 키의 CRN을 검색하십시오. 이 CRN은 아래와 같습니다. 

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect가 사용으로 설정된 버킷 작성
{: #node-examples-kp-new-bucket}

```javascript
function createBucketKP(bucketName) {
    console.log(`Creating new encrypted bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: '<bucket-location>'
        },
        IBMSSEKPEncryptionAlgorithm: '<algorithm>',
        IBMSSEKPCustomerRootKeyCrn: '<root-key-crn>'
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
        logDone();
    }))
    .catch(logError);
}
```
*키 값*
* `<bucket-location>` - 버킷의 지역 또는 위치입니다. Key Protect는 특정 지역에서만 사용 가능합니다. 자신의 위치가 Key Protect 서비스 지역과 일치하는지 확인하십시오. `LocationConstraint`에 대한 유효한 프로비저닝 코드의 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)에서 참조할 수 있습니다. 
* `<algorithm>` - 버킷에 추가된 새 오브젝트에 사용되는 암호화 알고리즘입니다(기본값은 AES256). 
* `<root-key-crn>` - Key Protect 서비스로부터 얻은 루트 키의 CRN입니다. 

*SDK 참조*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## 아카이브 기능 사용
{: #node-examples-archive}

아카이브 계층은 사용자가 시간이 경과된(stale) 데이터를 아카이브하여 스토리지 비용을 줄일 수 있게 해 줍니다. 아카이브 정책(*라이프사이클 구성*이라고도 함)은 버킷에 대해 작성되며 정책이 작성된 후 버킷에 추가된 모든 오브젝트에 적용됩니다. 

### 버킷의 라이프사이클 구성 보기
{: #node-examples-get-lifecycle}

```javascript
function getLifecycleConfiguration(bucketName) {
    return cos.getBucketLifecycleConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log(`Retrieving bucket lifecycle config from: ${bucketName}`);
            console.log(JSON.stringify(data, null, 4));
        }
        else {
            console.log(`No lifecycle configuration for ${bucketName}`);
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 라이프사이클 구성 작성 
{: #node-examples-put-lifecycle}

라이프사이클 구성 규칙을 구성하는 데 대한 자세한 정보는 [API 참조](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)에 있습니다. 

```javascript
function createLifecycleConfiguration(bucketName) {
    //
    var config = {
        Rules: [{
            Status: 'Enabled', 
            ID: '<policy-id>',
            Filter: {
                Prefix: ''
            },
            Transitions: [{
                Days: <number-of-days>, 
                StorageClass: 'GLACIER'
            }]
        }]
    };
    
    return cos.putBucketLifecycleConfiguration({
        Bucket: bucketName,
        LifecycleConfiguration: config
    }).promise()
    .then(() => {
        console.log(`Created bucket lifecycle config for: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*키 값*
* `<policy-id>` - 라이프사이클 정책의 이름(고유해야 함)
* `<number-of-days>` - 복원된 파일을 보존할 일 수

*SDK 참조*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 버킷의 라이프사이클 구성 삭제
{: #node-examples-delete-lifecycle}

```javascript
function deleteLifecycleConfiguration(bucketName) {
    return cos.deleteBucketLifecycle({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Deleted bucket lifecycle config from: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 오브젝트의 임시 복원
{: #node-examples-restore-object}

복원 요청 매개변수에 대한 자세한 정보는 [API 참조](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore)에 있습니다. 

```javascript
function restoreItem(bucketName, itemName) {
    var params = {
        Bucket: bucketName, 
        Key: itemName, 
        RestoreRequest: {
            Days: <number-of-days>, 
            GlacierJobParameters: {
                Tier: 'Bulk' 
            },
        } 
    };
    
    return cos.restoreObject(params).promise()
    .then(() => {
        console.log(`Restoring item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*키 값*
* `<number-of-days>` - 복원된 파일을 보존할 일 수

*SDK 참조*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 오브젝트의 HEAD 정보 보기
{: #node-examples-lifecycle-head-object}
```javascript
function getHEADItem(bucketName, itemName) {
    return cos.headObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        console.log(`Retrieving HEAD for item: ${itemName} from bucket: ${bucketName}`);
        console.log(JSON.stringify(data, null, 4));
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK 참조*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## 메타데이터 업데이트
{: #node-examples-metadata}

기존 오브젝트의 메타데이터를 업데이트하는 데는 두 가지 방법이 있습니다. 
* 새 메타데이터와 원본 오브젝트 컨텐츠를 사용한 `PUT` 요청
* 원본 오브젝트를 복사 소스로 지정하여, 새 메타데이터로 `COPY` 요청 실행

### PUT을 사용한 메타데이터 업데이트
{: #node-examples-metadata-put}

**참고:** `PUT` 요청은 오브젝트의 기존 컨텐츠를 겹쳐쓰므로 먼저 이를 다운로드한 후 새 메타데이터로 다시 업로드해야 합니다. 


```javascript
function updateMetadataPut(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //retrieve the existing item to reload the contents
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        //set the new metadata
        var newMetadata = {
            newkey: metaValue
        };

        return cos.putObject({
            Bucket: bucketName,
            Key: itemName,
            Body: data.Body,
            Metadata: newMetadata
        }).promise()
        .then(() => {
            console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
        })
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

### COPY를 사용한 메타데이터 업데이트
{: #node-examples-metadata-copy}

```javascript
function updateMetadataCopy(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //set the copy source to itself
    var copySource = bucketName + '/' + itemName;

    //set the new metadata
    var newMetadata = {
        newkey: metaValue
    };

    return cos.copyObject({
        Bucket: bucketName, 
        Key: itemName,
        CopySource: copySource,
        Metadata: newMetadata,
        MetadataDirective: 'REPLACE'
    }).promise()
    .then((data) => {
        console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

## 불변 오브젝트 스토리지 사용
{: #node-examples-immutable}

### 기존 버킷에 보호 구성 추가
{: #node-examples-immutable-add}

보호된 버킷에 작성된 오브젝트는 보호 기간이 만료되어 오브젝트에 대한 모든 법적 보존이 제거될 때까지 삭제할 수 없습니다. 오브젝트가 작성될 때 오브젝트 고유 값이 제공되지 않으면 버킷의 기본 보존 값이 오브젝트에 지정됩니다. 보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다.  

보존 기간 설정 `MinimumRetention`, `DefaultRetention` 및 `MaximumRetention`에 대해 지원되는 최소 및 최대값은 각각 0일과 365243일(1000년)입니다.  


```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### 버킷에 대한 보호 확인
{: #node-examples-immutable-check}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### 보호된 오브젝트 업로드
{: #node-examples-immutable-upload}

보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다. 

|값 | 유형 |설명 |
| --- | --- | --- | 
|`Retention-Period` | 음수가 아닌 정수(초) | 오브젝트에 저장할 보존 기간(초)입니다. 오브젝트는 보존 기간에 지정된 시간이 경과하기 전까지 겹쳐쓰거나 삭제할 수 없습니다. 이 필드와 `Retention-Expiration-Date`가 모두 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 `DefaultRetention` 기간이 사용됩니다. 영(`0`)은 버킷의 최소 보존 기간도 `0`이라고 가정하는 적법한 값입니다. |
| `Retention-expiration-date` | 날짜(ISO 8601 형식) | 오브젝트를 적법하게 삭제하거나 수정할 수 있는 날짜입니다. 이 헤더 또는 Retention-Period 헤더만 지정할 수 있습니다. 둘 다 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 DefaultRetention 기간이 사용됩니다. |
| `Retention-legal-hold-id` | 문자열 | 오브젝트에 적용할 단일 법적 보존입니다. 법적 보존은 Y자 길이의 문자열입니다. 오브젝트는 해당 오브젝트와 연관된 모든 법적 보존이 제거될 때까지 겹쳐쓰거나 삭제할 수 없습니다. |

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### 보호된 오브젝트에서 법적 보존을 추가하거나 제거
{: #node-examples-immutable-legal-hold}

오브젝트는 100개의 법적 보존을 지원할 수 있습니다. 

*  법적 보존 ID는 최대 64자이고 최소 1자인 문자열입니다. 유효한 문자는 문자, 숫자, `!`, `_`, `.`, `*`, `(`, `)`, `-` 및 `입니다. 
* 특정 법적 보존을 추가했을 때 오브젝트의 총 100개 법적 보존 한계를 초과하는 경우에는 해당 새 법적 보존이 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID가 너무 긴 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID에 올바르지 않은 문자가 포함되어 있는 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID가 이미 오브젝트에서 사용 중인 경우에는 기존 법적 보존이 수정되지 않으며 해당 ID가 이미 사용 중임을 나타내는 응답이 `409` 오류와 함께 표시됩니다. 
* 오브젝트에 보존 기간 메타데이터가 없는 경우에는 `400` 오류가 리턴되며 법적 보존 추가 또는 제거가 허용되지 않습니다. 

법적 보존을 추가하거나 제거하는 사용자에게는 해당 버킷에 대한 `Manager` 권한이 있어야 합니다. 

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### 보호된 오브젝트의 보존 기간 연장
{: #node-examples-immutable-extend}

오브젝트의 보존 기간은 연장만 가능합니다. 현재 구성된 값에서 줄일 수는 없습니다. 

보존 연장 값은 다음 세 가지 방법 중 하나로 설정됩니다. 

* 현재 값에서의 추가 시간(`Additional-Retention-Period` 또는 이와 유사한 메소드)
* 초 단위의 새 연장 기간(`Extend-Retention-From-Current-Time` 또는 이와 유사한 메소드)
* 오브젝트의 새 보존 만료 날짜(`New-Retention-Expiration-Date` 또는 이와 유사한 메소드)

오브젝트 메타데이터에 저장된 현재 보존 기간은 `extendRetention` 요청에 설정된 매개변수에 따라 지정된 추가 시간만큼 증가되거나, 새 값으로 대체될 수 있습니다. 모든 경우에 보존 연장 매개변수는 현재 보존 기간에 대해 확인되며, 연장된 매개변수는 업데이트된 보존 기간이 현재 보존 기간보다 긴 경우에만 허용됩니다. 

보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다. 

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### 보호된 오브젝트에 대한 법적 보존 나열
{: #node-examples-immutable-list-holds}

이 오퍼레이션은 다음 항목을 리턴합니다. 

* 오브젝트 작성 날짜
* 오브젝트 보존 기간(초)
* 보존 기간 및 작성 날짜를 기반으로 계산된 보존 만료 날짜
* 법적 보존의 목록
* 법적 보존 ID
* 법적 보존이 적용된 시점의 시간소인

오브젝트에 대한 법적 보존이 없는 경우에는 비어 있는 `LegalHoldSet`이 리턴됩니다.
오브젝트에 대해 지정된 보존 기간이 없는 경우에는 `404` 오류가 리턴됩니다. 


```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}
