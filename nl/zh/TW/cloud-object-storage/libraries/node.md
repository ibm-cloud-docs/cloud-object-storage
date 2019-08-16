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

# 使用 Node.js
{: #node}

## 安裝 SDK
{: #node-install}

安裝 {{site.data.keyword.cos_full}} SDK for Node.js 的偏好方式是使用 Node.js 的 [`npm`](https://www.npmjs.com){:new_window} 套件管理程式。在指令行鍵入下列指令：

```sh
npm install ibm-cos-sdk
```

原始碼管理於 [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window}。

個別方法及類別的詳細資料可以在 [SDK 的 API 文件](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}找到。

## 開始使用
{: #node-gs}

### 最低需求
{: #node-gs-prereqs}

若要執行 SDK，您需要 **Node 4.x+**。

### 建立用戶端及讀取認證
{: #node-gs-credentials}

為了連接至 COS，會藉由提供認證資訊（API 金鑰、服務實例 ID 及 IBM Authentication Endpoint），來建立及配置用戶端。這些值也可以自動從 credentials 檔案或環境變數中取得。

產生[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)後，會將產生的 JSON 文件儲存為 `~/.bluemix/cos_credentials`。除非在建立用戶端期間，明確地設定其他認證，否則 SDK 將自動從此檔案讀取認證。如果 `cos_credentials` 檔案包含 HMAC 金鑰，用戶端將以簽章進行鑑別，否則用戶端會使用提供的 API 金鑰以持有人記號進行鑑別。

`default` 區段標題指定認證的預設設定檔和相關聯值。您可以在相同的共用配置檔中建立更多設定檔，每個有自己的認證資訊。下列範例顯示具有預設設定檔的配置檔：
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

如果從 AWS S3 移轉，您也可以使用下列格式從 `~/.aws/credentials` 讀取認證資料：

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

如果 `~/.bluemix/cos_credentials` 和 `~/.aws/credentials` 同時存在，則 `cos_credentials` 會優先。

## 程式碼範例
{: #node-examples}

### 起始設定配置
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
*金鑰值*
* `<endpoint>` - 雲端物件儲存空間的公用端點（可從 [IBM Cloud 儀表格](https://cloud.ibm.com/resources){:new_window}取得）。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<api-key>` - 建立服務認證時產生的 API 金鑰（建立及刪除範例需要寫入權）。
* `<resource-instance-id>` - 雲端物件儲存空間的資源 ID（可透過 [IBM Cloud CLI](/docs/overview?topic=overview-crn) 或 [IBM Cloud 儀表板](https://cloud.ibm.com/resources){:new_window}取得）。

### 建立儲存區
{: #node-examples-new-bucket}

可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中參閱 `LocationConstraint` 的有效佈建碼清單。

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


*SDK 參照*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### 建立文字物件
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

*SDK 參照*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### 列出儲存區
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

*SDK 參照*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### 列出儲存區中的項目
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

*SDK 參照*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### 取得特定項目的檔案內容
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

*SDK 參照*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### 從儲存區刪除項目
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
*SDK 參照*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### 從儲存區刪除多個項目
{: #node-examples-multidelete}

刪除要求最多可以包含您要刪除的 1000 個金鑰。雖然分批次刪除物件對於減少每個要求的額外負擔非常有用，但請注意在刪除許多金鑰時可能要一段時間才能完成。此外，也請考量物件的大小，以確保適當的效能。
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

*SDK 參照*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### 刪除儲存區
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

*SDK 參照*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### 執行多部分上傳
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

*SDK 參照*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## 使用 Key Protect
{: #node-examples-kp}

Key Protect 可新增至儲存空間儲存區，以管理加密金鑰。在 IBM COS 中所有資料都已加密，但 Key Protect 提供了一項服務，來使用集中化服務產生、替換及控制對加密金鑰的存取權。

### 開始之前
{: #node-examples-kp-prereqs}
為了建立儲存區並啟用 Key Protect，需要下列項目：

* [已佈建](/docs/services/key-protect?topic=key-protect-provision#provision) Key Protect 服務
* 根金鑰可用（[已產生](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已匯入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 擷取根金鑰 CRN
{: #node-examples-kp-root}

1. 擷取 Key Protect 服務的[實例 ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 來擷取所有[可用金鑰](https://cloud.ibm.com/apidocs/key-protect)
    * 您可以使用 `curl` 指令或 API REST 用戶端（例如 [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）來存取 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 擷取您將使用來在儲存區上啟用 Key Protect 的根金鑰 CRN。CRN 看起來如下：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 建立儲存區並啟用 Key Protect
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
*金鑰值*
* `<bucket-location>` - 您儲存區的地區或位置（Key Protect 只適用於特定地區。請確定您的位置符合 Key Protect 服務。）可以在[儲存空間類別手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中參閱 `LocationConstraint` 的有效佈建碼清單。
* `<algorithm>` - 用於新增至儲存區的新物件加密演算法（預設為 AES256）。
* `<root-key-crn>` - 從 Key Protect 服務取得的「根金鑰」的 CRN。

*SDK 參照*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## 使用保存特性
{: #node-examples-archive}

Archive Tier 允許使用者保存舊資料，並減少它們的儲存空間成本。保存原則（也稱為*生命週期配置*）是針對儲存區而建立，適用於在原則建立之後新增至儲存區的任何物件。

### 檢視儲存區的生命週期配置
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

*SDK 參照*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 建立生命週期配置 
{: #node-examples-put-lifecycle}

關於建構生命週期配置的詳細資訊提供於 [API 參考資料](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)。

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

*金鑰值*
* `<policy-id>` - 生命週期原則的名稱（必須是唯一的）
* `<number-of-days>` - 要保留還原檔案的天數

*SDK 參照*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 刪除儲存區的生命週期配置
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

*SDK 參照*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 暫時還原物件
{: #node-examples-restore-object}

關於還原要求參數的詳細資訊提供於 [API 參考資料](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore)。

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

*金鑰值*
* `<number-of-days>` - 要保留還原檔案的天數

*SDK 參照*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 檢視物件的 HEAD 資訊
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

*SDK 參照*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## 更新 meta 資料
{: #node-examples-metadata}

有兩種方式可更新現有物件上的 meta 資料：
* 具有新 meta 資料及原始物件內容的 `PUT` 要求
* 使用新 meta 資料來執行 `COPY` 要求，並指定原始物件作為副本來源

### 使用 PUT 更新 meta 資料
{: #node-examples-metadata-put}

**附註：**`PUT` 要求會改寫物件的現有內容，因此必須先下載並重新上傳具有新 meta 資料的內容


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

### 使用 COPY 更新 meta 資料
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

## 使用 Immutable Object Storage
{: #node-examples-immutable}

### 將保護配置新增至現有儲存區
{: #node-examples-immutable-add}

在保護期間過期之前，無法刪除寫入受保護儲存區的物件，並移除物件上的所有合法保留。除非在物件建立時提供物件特定值，否則會將儲存區的預設保留值提供給物件。不再保留受保護儲存區中的物件（保留期間已過期，而物件沒有任何合法保留），改寫時，將會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。 

保留期間設定值 `MinimumRetention`、`DefaultRetention` 及 `MaximumRetention` 的最小和最大支援值分別是 0 天到 365243 天（1000 年）。 


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

### 檢查儲存區的保護
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

### 上傳受保護物件
{: #node-examples-immutable-upload}

不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。

|值	|類型|說明|
| --- | --- | --- | 
|`Retention-Period` | 非負整數（秒）| 儲存在物件上的保留期間（以秒為單位）。除非已過保留期間中指定的時間，否則無法改寫或刪除物件。如果指定此欄位及 `Retention-Expiration-Date`，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 `DefaultRetention` 期間。零 (`0`) 是合法值，假設儲存區最小保留期間也為 `0`。|
| `Retention-expiration-date` | 日期（ISO 8601 格式）|在此日期將可以合法刪除或修改物件。您只能指定此項或 Retention-Period 標頭。如果兩者都指定，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 DefaultRetention 期間。|
| `Retention-legal-hold-id` |字串   | 要套用至物件的單一合法保留。合法保留是 Y 字元長字串。除非已移除與物件相關聯的所有合法保留，否則無法改寫或刪除物件。|

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

### 新增或移除受保護物件的合法保留
{: #node-examples-immutable-legal-hold}

物件可支援 100 個合法保留：

*  合法保留 ID 是長度上限為 64 個字元的字串，且長度下限為 1 個字元。有效字元為字母、數字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 及 `。
* 如果給定合法保留新增數超過物件上的 100 個合法保留總數，則不會新增合法保留，將會傳回 `400` 錯誤。
* 如果 ID 太長，將不會新增到物件中，且會傳回 `400` 錯誤。
* 如果 ID 包含無效的字元，則不會將它新增至物件，且會傳回 `400` 錯誤。
* 如果某個 ID 已在物件上使用，則不會修改現有合法保留，且回應會指出 ID 已在使用中，並且有 `409` 錯誤。
* 如果物件沒有保留期間 meta 資料，則會傳回 `400` 錯誤，且不容許新增或移除合法保留。

正在新增或移除合法保留的使用者必須具有此儲存區的 `Manager` 許可權。

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

### 延長受保護物件的保留期間
{: #node-examples-immutable-extend}

只能延長物件的保留期間。不能從目前配置的值縮短。

保留擴充值以三種方式之一來設定：

* 現行值再加上時間（`Additional-Retention-Period` 或類似方法）
* 新擴充期間（以秒為單位）（`Extend-Retention-From-Current-Time` 或類似方法）
* 物件的新保留到期日（`New-Retention-Expiration-Date` 或類似方法）

儲存在物件 meta 資料中的現行保留期間，會增加給定的額外時間，或以新值取代，視 `extendRetention` 要求中設定的參數而定。在所有情況下，會針對現行保留期間檢查延長保留參數，而且只有在更新的保留期間大於現行保留期間時，才會接受延長的參數。

不再保留的受保護儲存區物件（保留期間已過期，而物件沒有任何合法保留），在被改寫時會再次保留。新的保留期間可以提供為物件改寫要求的一部分，否則會將儲存區的預設保留時間提供給物件。

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

### 列出受保護物件的合法保留
{: #node-examples-immutable-list-holds}

此作業傳回：

* 物件建立日期
* 物件保留期間（秒）
* 根據期間和建立日期計算的保留到期日
* 合法保留的清單
* 合法保留 ID
* 套用合法保留時的時間戳記

如果物件沒有任何合法保留，則會傳回空的 `LegalHoldSet`。如果在物件上未指定保留期間，則會傳回 `404` 錯誤。


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
