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

## 安装 SDK
{: #node-install}

安装 {{site.data.keyword.cos_full}} SDK for Node.js 的首选方法是使用 Node.js 的 [`npm`](https://www.npmjs.com){:new_window} 软件包管理器。在命令行中输入以下命令：

```sh
npm install ibm-cos-sdk
```

源代码在 [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window} 上托管。

有关各个方法和类的更多详细信息，请参阅 [SDK 的 API 文档](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}。

## 开始使用
{: #node-gs}

### 最低需求
{: #node-gs-prereqs}

要运行 SDK，您需要 **Node 4.x+**。

### 创建客户机和获取凭证
{: #node-gs-credentials}

为了连接到 COS，将通过提供凭证信息（API 密钥、服务实例标识和 IBM 认证端点）来创建和配置客户机。这些值还可以自动从凭证文件或环境变量中获取。

生成[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)后，生成的 JSON 文档可以保存到 `~/.bluemix/cos_credentials`。除非在客户机创建期间显式设置了其他凭证，否则 SDK 会自动从此文件中获取凭证。如果 `cos_credentials` 文件包含 HMAC 密钥，那么客户机会使用签名进行认证；如果不包含 HMAC 密钥，客户机将使用提供的 API 密钥通过不记名令牌进行认证。

`default` 部分的标题指定用于凭证的缺省概要文件和关联的值。可以在同一共享配置文件中创建更多概要文件，每个概要文件包含自己的凭证信息。以下示例显示了具有缺省概要文件的配置文件：
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

如果是从 AWS S3 进行迁移，那么还可以从 `~/.aws/credentials` 中获取以下格式的凭证数据：

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

如果 `~/.bluemix/cos_credentials` 和 `~/.aws/credentials` 同时存在，那么 `cos_credentials` 优先。

## 代码示例
{: #node-examples}

### 初始化配置
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
*键值*
* `<endpoint>` - Cloud Object Storage 的公共端点（通过 [IBM Cloud 仪表板](https://cloud.ibm.com/resources){:new_window}提供）。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<api-key>` - 创建服务凭证时生成的 API 密钥（创建和删除示例需要写访问权）
* `<resource-instance-id>` - Cloud Object Storage 的资源标识（通过 [IBM Cloud CLI](/docs/overview?topic=overview-crn) 或 [IBM Cloud 仪表板](https://cloud.ibm.com/resources){:new_window}提供）

### 创建存储区
{: #node-examples-new-bucket}

在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中可以参考 `LocationConstraint` 的有效供应代码的列表。

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


*SDK 参考*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### 创建文本对象
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

*SDK 参考*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### 列出存储区
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

*SDK 参考*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### 列出存储区中的项
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

*SDK 参考*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### 获取特定项的文件内容
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

*SDK 参考*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### 从存储区中删除一个项
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
*SDK 参考*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### 从存储区中删除多个项
{: #node-examples-multidelete}

删除请求最多可包含 1000 个要删除的键。虽然批量删除对象对于减少每个请求的开销非常有用，但在删除大量键时应谨慎，请求可能需要一些时间才能完成。此外，请考虑对象的大小，以确保性能合适。
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

*SDK 参考*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### 删除存储区
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

*SDK 参考*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### 执行分块上传
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

*SDK 参考*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## 使用 Key Protect
{: #node-examples-kp}

可以将 Key Protect 添加到存储区，以管理加密密钥。所有数据都在 IBM COS 中进行加密，但 Key Protect 提供的是使用集中服务来生成和循环加密密钥以及控制加密密钥访问权的服务。

### 开始之前
{: #node-examples-kp-prereqs}
要创建启用了 Key Protect 的存储区，需要以下各项：

* [已供应](/docs/services/key-protect?topic=key-protect-provision#provision) Key Protect 服务
* 根密钥可用（[已生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)或[已导入](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)）

### 检索根密钥 CRN
{: #node-examples-kp-root}

1. 检索 Key Protect 服务的[实例标识](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID)。
2. 使用 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) 来检索所有[可用密钥](https://cloud.ibm.com/apidocs/key-protect)。
    * 可以使用 `curl` 命令或 API REST 客户机（例如，[Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman)）来访问 [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api)。
3. 检索将用于在存储区上启用 Key Protect 的根密钥的 CRN。CRN 类似于以下内容：

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### 创建启用了 Key Protect 的存储区
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
*键值*
* `<bucket-location>` - 存储区的区域或位置（Key Protect 仅在特定区域中可用。请确保您的位置与 Key Protect 服务相匹配）。在[存储类指南](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)中可以参考 `LocationConstraint` 的有效供应代码的列表。
* `<algorithm>` - 用于添加到存储区的新对象的加密算法（缺省值为 AES256）。
* `<root-key-crn>` - 从 Key Protect 服务获取的根密钥的 CRN。

*SDK 参考*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## 使用归档功能
{: #node-examples-archive}

通过归档层，用户可以归档旧数据并降低其存储成本。归档策略（也称为*生命周期配置*）是针对存储区创建的，并应用于创建策略后添加到存储区的任何对象。

### 查看存储区的生命周期配置
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

*SDK 参考*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 创建生命周期配置 
{: #node-examples-put-lifecycle}

在 [API 参考](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)中提供了有关构造生命周期配置规则的详细信息

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

*键值*
* `<policy-id>` - 生命周期策略的名称（必须唯一）
* `<number-of-days>` - 保留已复原文件的天数

*SDK 参考*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 删除存储区的生命周期配置
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

*SDK 参考*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 临时复原对象
{: #node-examples-restore-object}

在 [API 参考](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore)中提供了有关复原请求参数的详细信息

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

*键值*
* `<number-of-days>` - 保留已复原文件的天数

*SDK 参考*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### 查看对象的 HEAD 信息
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

*SDK 参考*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## 更新元数据
{: #node-examples-metadata}

有两种方法可更新现有对象上的元数据：
* 对新的元数据和原始对象内容执行 `PUT` 请求
* 使用将原始对象指定为复制源的新元数据来执行 `COPY` 请求

### 使用 PUT 更新元数据
{: #node-examples-metadata-put}

**注：**`PUT` 请求会覆盖对象的现有内容，因此必须首先下载对象，然后使用新的元数据重新上传


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

### 使用 COPY 更新元数据
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

## 使用不可变对象存储器
{: #node-examples-immutable}

### 向现有存储区添加保护配置
{: #node-examples-immutable-add}

对于写入受保护存储区的对象，在保护时间段到期并且除去了对象上的所有合法保留之前，无法删除这些对象。除非在创建对象时提供了特定于对象的值，否则将向对象提供存储区的缺省保留时间值。如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。 

保留期设置 `MinimumRetention`、`DefaultRetention` 和 `MaximumRetention` 的最小和最大支持值分别为 0 天和 365243 天（1000 年）。 


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

### 检查存储区上的保护
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

### 上传受保护对象
{: #node-examples-immutable-upload}

如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。

|值|类型|描述|
| --- | --- | --- | 
|`Retention-Period`|非负整数（秒）|要在对象上存储的保留期（以秒为单位）。在保留期中指定的时间长度到期之前，无法覆盖也无法删除对象。如果同时指定了此字段和 `Retention-Expiration-Date`，将返回 `400` 错误。如果这两个字段均未指定，将使用存储区的 `DefaultRetention` 时间段。假定存储区的最短保留期为 `0`，那么零 (`0`) 是合法值。|
|`Retention-expiration-date`|日期（ISO 8601 格式）|能够合法删除或修改对象的日期。只能指定此项或指定 Retention-Period 头。如果同时指定这两项，将返回 `400` 错误。如果这两项均未指定，将使用存储区的 DefaultRetention 时间段。|
|`Retention-legal-hold-id`|字符串|要应用于对象的单个合法保留。合法保留是长度为 Y 个字符的字符串。在除去与对象关联的所有合法保留之前，无法覆盖或删除对象。|

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

### 向受保护对象添加合法保留或除去受保护对象的合法保留
{: #node-examples-immutable-legal-hold}

一个对象可以支持 100 个合法保留：

*  合法保留标识是一个字符串，最大长度为 64 个字符，最小长度为 1 个字符。有效字符为字母、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 和 `。
* 如果添加给定合法保留将导致对象上超过 100 个合法保留，那么不会添加新的合法保留，并且将返回 `400` 错误。
* 如果标识太长，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识包含无效字符，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识已在对象上使用，那么不会修改现有合法保留，响应会指示该标识已在使用，并返回 `409` 错误。
* 如果对象没有保留期元数据，那么将返回 `400` 错误，并且不允许添加或除去合法保留。

添加或除去合法保留的用户必须具有对此存储区的`管理者`许可权。

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

### 延长受保护对象的保留期
{: #node-examples-immutable-extend}

对象的保留期只能延长。不能在当前配置值的基础上缩短。

保留时间延长值可通过以下三种方式之一进行设置：

* 在当前值的基础上增加时间（`Additional-Retention-Period` 或类似方法）
* 新的延长时间段（以秒为单位）（`Extend-Retention-From-Current-Time` 或类似方法）
* 对象的新保留到期日期（`New-Retention-Expiration-Date` 或类似方法）

根据 `extendRetention` 请求中设置的参数，对象元数据中存储的当前保留期可通过给定更多时间延长，也可替换为新值。在所有情况下，都会根据当前保留期来检查延长保留时间参数，并且仅当更新的保留期大于当前保留期时，才会接受延长参数。

如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。

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

### 列出受保护对象上的合法保留
{: #node-examples-immutable-list-holds}

此操作会返回以下内容：

* 对象创建日期
* 对象保留期（秒）
* 根据时间段和创建日期计算的保留到期日期
* 合法保留的列表
* 合法保留标识
* 应用合法保留时的时间戳记

如果对象上没有合法保留，那么会返回空的 `LegalHoldSet`。如果在对象上未指定保留期，那么会返回 `404` 错误。


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
