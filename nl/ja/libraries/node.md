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

# Node.js の使用
{: #node}

## SDK のインストール
{: #node-install}

{{site.data.keyword.cos_full}} SDK for Node.js をインストールする際の望ましい方法は、Node.js 用の [`npm`](https://www.npmjs.com){:new_window} パッケージ・マネージャーを使用することです。コマンド・ラインに以下のコマンドを入力します。

```sh
npm install ibm-cos-sdk
```

ソース・コードは、[GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window} でホストされます。

個別のメソッドおよびクラスについて詳しくは、[SDK の API 資料](https://ibm.github.io/ibm-cos-sdk-js/){:new_window} を参照してください。

## 開始
{: #node-gs}

### 最小要件
{: #node-gs-prereqs}

SDK を実行するには、**Node 4.x+** が必要です。

### クライアントの作成と資格情報の入手
{: #node-gs-credentials}

COS に接続するために、資格情報 (API キー、サービス・インスタンス ID、および IBM 認証エンドポイント) を指定することによりクライアントが作成および構成されます。これらの値は、資格情報ファイルまたは環境変数から自動的に入手することもできます。

[サービス資格情報](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)を生成した後は、結果の JSON 文書を `~/.bluemix/cos_credentials` に保存できます。クライアントの作成時に他の資格情報が明示的に設定されない限り、SDK は、このファイルから資格情報を自動的に入手します。`cos_credentials` ファイルに HMAC 鍵が含まれている場合、クライアントは署名を使用して認証を受けます。そうでない場合、クライアントは、提供される API キーを使用してベアラー・トークンで認証を受けます。

`default` セクション・ヘッディングには、デフォルト・プロファイルおよび資格情報に関連した値を指定します。同じ共有構成ファイル内に追加のプロファイルを作成して、それぞれのプロファイルに独自の資格情報を設定できます。以下は、デフォルト・プロファイルが設定された構成ファイルの例です。
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

AWS S3 からマイグレーションする場合は、`~/.aws/credentials` から資格情報データを以下の形式で入手することもできます。

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

`~/.bluemix/cos_credentials` と `~/.aws/credentials` の両方が存在する場合は、`cos_credentials` が優先されます。

## コード例
{: #node-examples}

### 構成の初期化
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
*キー値*
* `<endpoint>` - Cloud Object Storage のパブリック・エンドポイント ([IBM Cloud ダッシュボード](https://cloud.ibm.com/resources){:new_window} から入手可能)。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
* `<api-key>` - サービス資格情報の作成時に生成される API キー (作成および削除の例では、書き込みアクセス権限が必要です)
* `<resource-instance-id>` - Cloud Object Storage のリソース ID ([IBM Cloud CLI](/docs/overview?topic=overview-crn) または [IBM Cloud ダッシュボード](https://cloud.ibm.com/resources){:new_window} から入手可能)

### バケットの作成
{: #node-examples-new-bucket}

`LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラスのガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)を参照してください。

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


*SDK リファレンス*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### テキスト・オブジェクトの作成
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

*SDK リファレンス*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### バケットのリスト
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

*SDK リファレンス*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### バケット内の項目のリスト
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

*SDK リファレンス*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### 特定の項目のファイル内容の取得
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

*SDK リファレンス*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### バケットから項目を削除
{: #node-examples-delete-object}

```javascript
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*SDK リファレンス*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### バケットから複数項目を削除
{: #node-examples-multidelete}

削除要求には、削除するキーを最大 1000 個含めることができます。オブジェクトの削除をバッチで行うことは、要求ごとのオーバーヘッドを削減するのに非常に役立ちますが、多数のキーを削除すると要求が完了するまでにしばらく時間がかかる可能性があるので注意してください。また、適切なパフォーマンスを確保するために、オブジェクトのサイズも考慮に入れてください。
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

*SDK リファレンス*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### バケットの削除
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

*SDK リファレンス*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### 複数パーツ・アップロードの実行
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
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*SDK リファレンス*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Key Protect の使用
{: #node-examples-kp}

暗号鍵を管理するために、Key Protect をストレージ・バケットに追加できます。IBM COS ではすべてのデータが暗号化されますが、Key Protect が提供するサービスは、集中型サービスを使用して暗号鍵を生成したり、ローテーションしたり、暗号鍵へのアクセスを制御したりするためのものです。

### 始める前に
{: #node-examples-kp-prereqs}
Key-Protect が有効なバケットを作成するためには、以下の項目が必要です。

* [プロビジョン済み](/docs/services/key-protect?topic=key-protect-provision#provision)の Key Protect サービス
* 使用可能なルート・キー ([生成](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys)されたか、[インポート](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys)されたもの)

### ルート・キー CRN の取得
{: #node-examples-kp-root}

1. Key Protect サービスの[インスタンス ID](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) を取得します。
2. [Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) を使用して、すべての[使用可能なキー](https://cloud.ibm.com/apidocs/key-protect)を取得します。
    * `curl` コマンドまたは API REST クライアント ([Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman) など) のいずれかを使用して、[Key Protect API](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) にアクセスできます。
3. バケットで Key Protect を有効にするために使用するルート・キーの CRN を取得します。CRN は、下記のようになります。

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Key Protect が有効なバケットの作成
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
*キー値*
* `<bucket-location>` - バケットの地域またはロケーション (Key Protect は、特定の地域でのみ使用可能です。ご使用のロケーションが Key Protect サービスと一致していることを確認してください。) `LocationConstraint` の有効なプロビジョニング・コードのリストについては、[ストレージ・クラスのガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)を参照してください。
* `<algorithm>` - バケットに追加される新規オブジェクトに使用される暗号化アルゴリズム (デフォルトは AES256)。
* `<root-key-crn>` - Key Protect サービスから取得したルート・キーの CRN。

*SDK リファレンス*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## アーカイブ機能の使用
{: #node-examples-archive}

アーカイブ層を使用することで、ユーザーは、失効したデータをアーカイブし、ストレージ・コストを削減できます。アーカイブ・ポリシー (*ライフサイクル構成* とも呼ばれます) は、バケットに対して作成され、ポリシーが作成された後にバケットに追加されるすべてのオブジェクトに適用されます。

### バケットのライフサイクル構成の表示
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

*SDK リファレンス*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### ライフサイクル構成の作成 
{: #node-examples-put-lifecycle}

ライフサイクル構成ルールの構造について詳しくは、[API リファレンス](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)を参照してください。

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

*キー値*
* `<policy-id>` - ライフサイクル・ポリシーの名前 (固有でなければなりません)
* `<number-of-days>` - リストアされるファイルを保持する日数

*SDK リファレンス*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### バケットのライフサイクル構成の削除
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

*SDK リファレンス*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### オブジェクトの一時的リストア
{: #node-examples-restore-object}

リストア要求のパラメーターについて詳しくは、[API リファレンス](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore)を参照してください。

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

*キー値*
* `<number-of-days>` - リストアされるファイルを保持する日数

*SDK リファレンス*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### オブジェクトの HEAD 情報の表示
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

*SDK リファレンス*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## メタデータの更新
{: #node-examples-metadata}

既存のオブジェクトのメタデータを更新する方法は、2 とおりあります。
* 新しいメタデータと元のオブジェクト・コンテンツが指定された `PUT` 要求
* 元のオブジェクトをコピー・ソースとして指定し、新しいメタデータを使用する `COPY` 要求の実行

### PUT を使用したメタデータの更新
{: #node-examples-metadata-put}

**注:** `PUT` 要求は、オブジェクトの既存のコンテンツを上書きするものです。このため、まずオブジェクトをダウンロードし、新しいメタデータを指定して再度アップロードする必要があります。


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

### COPY を使用したメタデータの更新
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

## Immutable Object Storage の使用
{: #node-examples-immutable}

### 既存のバケットへの保護構成の追加
{: #node-examples-immutable-add}

保護対象のバケットに書き込まれたオブジェクトは、保護期間が満了し、オブジェクトに対するすべての法的保留が解除されるまで削除できません。オブジェクトの作成時にオブジェクト固有の値が指定されない限り、バケットのデフォルトの保存期間値がオブジェクトにも適用されます。保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。 

保存期間設定 `MinimumRetention`、`DefaultRetention`、および `MaximumRetention` でサポートされる最小値と最大値は、それぞれ 0 日と 365243 日 (1000 年) です。 


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

### バケットに対する保護のチェック
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

### 保護オブジェクトのアップロード
{: #node-examples-immutable-upload}

保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。

|値	| タイプ	| 説明 |
| --- | --- | --- | 
|`Retention-Period` | 負でない整数 (秒数) | オブジェクトを保管する保存期間 (秒数)。オブジェクトは、保存期間に指定された時間が経過するまで上書きも削除もできません。このフィールドと `Retention-Expiration-Date` が指定された場合、`400` エラーが返されます。いずれも指定されない場合は、バケットの `DefaultRetention` 期間が使用されます。ゼロ (`0`) は有効な値であり、バケットの最小保存期間も `0` であると想定されます。 |
| `Retention-expiration-date` | 日付 (ISO 8601 フォーマット) | オブジェクトを合法的に削除または変更できるようになる日付。これか Retention-Period ヘッダーのいずれかのみを指定できます。両方が指定された場合は、`400` エラーが返されます。いずれも指定されない場合は、バケットの DefaultRetention 期間が使用されます。 |
| `Retention-legal-hold-id` | ストリング | オブジェクトに適用される単一の法的保留。法的保留とは、Y 文字の長さのストリングです。オブジェクトに関連付けられたすべての法的保留が解除されるまで、オブジェクトは上書きも削除もできません。 |

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

### 保護オブジェクトに対する法的保留の追加または解除
{: #node-examples-immutable-legal-hold}

オブジェクトがサポートできる法的保留は 100 個です。

*  法的保留 ID は、最大長 64 文字かつ最小長 1 文字のストリングです。有効な文字は、英字、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-`、および ` です。
* 指定された法的保留を追加することで、オブジェクトに対する法的保留の合計数が 100 個を超える場合、新しい法的保留は追加されず、`400` エラーが返されます。
* ID が長すぎる場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID に無効文字が含まれている場合、ID はオブジェクトに追加されず、`400` エラーが返されます。
* ID がオブジェクトで既に使用中の場合、既存の法的保留は変更されず、ID が既に使用中であることを示す応答と `409` エラーが返されます。
* オブジェクトに保存期間メタデータがない場合は、`400` エラーが返され、法的保留の追加または削除は許可されません。

法的保留の追加または削除を行うユーザーには、このバケットに対する`マネージャー`許可が必要です。

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

### 保護オブジェクトの保存期間の延長
{: #node-examples-immutable-extend}

オブジェクトの保存期間は延長のみが可能です。現在構成されている値から減らすことはできません。

保存延長値は次の 3 つの方法のいずれかで設定されます。

* 現行値からの追加時間 (`Additional-Retention-Period` または同様のメソッド)
* 新しい延長期間 (秒数) (`Extend-Retention-From-Current-Time` または同様のメソッド)
* オブジェクトの新しい保存有効期限日付 (`New-Retention-Expiration-Date` または同様のメソッド)

オブジェクト・メタデータに保管されている現在の保存期間は、`extendRetention` 要求に設定されているパラメーターに応じて、指定された追加時間分増やされるか、新しい値に置き換えられます。いずれの場合も、保存延長パラメーターは現在の保存期間に対して検査され、更新後の保存期間が現在の保存期間より大きい場合にのみ、延長パラメーターが受け入れられます。

保全の対象でなくなった保護対象バケット内のオブジェクト (保存期間が満了し、オブジェクトに法的保留が課せられていない) が上書きされた場合、そのオブジェクトは再度保全の対象になります。新しい保存期間は、オブジェクト上書き要求の一部として指定できます。そうしない場合は、バケットのデフォルトの保存期間がオブジェクトに適用されます。

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

### 保護オブジェクトの法的保留のリスト
{: #node-examples-immutable-list-holds}

この操作で返される内容は以下のとおりです。

* オブジェクト作成日
* オブジェクト保存期間 (秒数)
* 期間および作成日に基づいて計算された保存有効期限
* 法的保留のリスト
* 法的保留 ID
* 法的保留が適用されたときのタイム・スタンプ

オブジェクトに法的保留がない場合は、空の `LegalHoldSet` が返されます。
オブジェクトに保存期間が指定されていない場合は、`404` エラーが返されます。


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
