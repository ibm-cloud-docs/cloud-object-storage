---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: worm, immutable, policy, retention, compliance

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

# 使用不可变对象存储器
{: #immutable}

不可变对象存储器允许客户保留电子记录，并在记录保留期结束和除去任何合法保留之前，以 WORM（写一次读多次）、不可擦除且不可重写的方式维护数据完整性。此功能可供需要在其环境中长期保留数据的任何客户使用，包括但不限于以下行业中的组织：

 * 金融
 * 医疗保健
 * 媒体内容归档
 * 希望阻止对对象或文档执行特权修改或删除的行业

底层特性功能还可供处理财务记录管理（例如，经纪商-交易商交易），并且可能需要以不可重写和不可擦除格式保留对象的组织使用。

不可变对象存储器仅在某些区域可用；有关详细信息，请参阅[集成服务](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)。此外，使用不可变对象存储器还需要标准价格套餐。有关详细信息，请参阅[定价](https://www.ibm.com/cloud/object-storage)。
{:note}

无法将 Aspera 高速传输用于使用保留时间策略的存储区。
{:important}

## 术语和用法
{: #immutable-terminology}

### 保留期
{: #immutable-terminology-period}

对象必须在 COS 存储区中保持存储的持续时间。

### 保留时间策略
{: #immutable-terminology-policy}

保留时间策略在 COS 存储区级别启用。最短保留期、最长保留期和缺省保留期由此策略定义，并应用于存储区中的所有对象。

最短保留期是对象必须在存储区中保留的最短持续时间。

最长保留期是对象可以在存储区中保留的最长持续时间。

如果对象存储在存储区中而未指定定制保留期，那么将使用缺省保留期。最短保留期必须短于或等于缺省保留期，而缺省保留期必须短于或等于最长保留期。

注：可以为对象指定的最长保留期为 1000 年。

注：要在存储区上创建保留时间策略，您将需要“管理者”角色。有关更多详细信息，请参阅[存储区许可权](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions)。

### 合法保留 
{: #immutable-terminology-hold}

对于某些记录（对象），即使在其保留期到期之后，也可能需要阻止将其删除，例如等待完成的法律审查可能需要在更长的持续时间内访问记录，此持续时间超过初始为对象设置的保留期。在此类场景中，可以在对象级别应用合法保留标志。在初始上传到 COS 存储区期间或添加了对象之后，可以将合法保留应用于对象。注：每个对象最多可以应用 100 个合法保留。

### 无限期保留
{: #immutable-terminology-indefinite}

允许用户将对象设置为无限期存储，直到应用新的保留期为止。这是按对象级别设置的。

### 基于事件的保留时间
{: #immutable-terminology-events}

使用不可变对象存储器时，如果用户不确定其用例的保留期的最终持续时间，或者希望使用基于事件的保留时间功能，那么用户可以在对象上设置无限期保留。设置为无限期保留后，用户应用程序仍可以在日后将对象保留时间更改为有限值。例如，一家公司采用的政策是，在员工离开公司后，将其记录保留 3 年。在员工进公司时，可以无限期保留与该员工相关的记录。在该员工离开公司时，根据公司政策的定义，无限期保留会转换为有限值 3 年（从当前时间起算）。然后，在保留期更改后，会对该对象保护 3 年。用户或第三方应用程序可以使用 SDK 或 REST API 将保留期从无限期保留更改为有限期保留。

### 永久保留
{: #immutable-terminology-permanent}

只能在启用保留时间策略的 COS 存储区级别启用永久保留，并且用户能够在对象上传期间选择永久保留期选项。启用永久保留后，无法撤销此过程，并且**无法删除**使用永久保留期上传的对象。用户应自行负责验证使用具有保留时间策略的 COS 存储区来**永久**存储对象是否为合理的需求。


使用不可变对象存储器时，您负责确保在数据受保留时间策略约束期间，IBM Cloud 帐户始终符合 IBM Cloud 策略和准则。有关更多信息，请参阅 IBM Cloud 服务条款。
{:important}

## 不可变对象存储器和各种法规注意事项
{: #immutable-regulation}

使用不可变对象存储器时，客户负责检查并确保是否可以利用所讨论的任何特性功能来满足和遵守有关电子记录存储和保留的重要规则，这些规则通常由以下各方进行监管：

  * [美国证券交易委员会 (SEC) 规则 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64)
  * [美国金融业监管局 (FINRA) 规则 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957) 和
  * [美国商品期货交易委员会 (CFTC) 规则 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

为了帮助客户做出明智决策，IBM 聘请 Cohasset Associates Inc. 对 IBM 的不可变对象存储器功能开展了独立评估。请查看 Cohasset Associates Inc. 的[报告](https://www.ibm.com/downloads/cas/JBDNP0KV)，此报告提供了有关 IBM Cloud Object Storage 的不可变对象存储器功能评估的详细信息。

### 审计访问和事务
{: #immutable-audit}
通过开具客户服务凭单，可以视情形访问不可变对象存储器的日志数据，以查看对保留时间参数、对象保留期和合法保留应用的更改。

## 使用控制台
{: #immutable-console}

保留时间策略可以添加到新存储区或现有空存储区，并且无法除去。对于新存储区，请确保是在[支持的区域](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)中创建存储区，然后选择**添加保留时间策略**选项。对于现有存储区，请确保该存储区没有对象，然后导航至配置设置，单击存储区保留时间策略部分下的**创建策略**按钮。无论哪种情况，都请设置最短保留期、最长保留期和缺省保留期。

## 使用 REST API、库和 SDK
{: #immutable-sdk}

IBM COS SDK 中引入了多个新的 API，用于支持应用程序使用保留时间策略。在此页面顶部选择语言（HTTP、Java、Javascript 或 Python），以查看使用相应 COS SDK 的示例。 

请注意，所有代码示例都假定存在可调用不同方法的名为 `cos` 的客户机对象。有关创建客户机的详细信息，请参阅特定的 SDK 指南。

用于设置保留期的所有日期值均为 GMT。需要 `Content-MD5` 头来确保数据完整性，并在使用 SDK 时自动发送此头。
{:note}

### 在现有存储区上添加保留时间策略
{: #immutable-sdk-add-policy}
此 `PUT` 操作实现使用 `protection` 查询参数来设置现有存储区的保留时间参数。此操作允许您设置或更改最短保留期、缺省保留期和最长保留期。此操作还允许您更改存储区的保护状态。 

对于写入受保护存储区的对象，在保护时间段到期并且除去了对象上的所有合法保留之前，无法删除这些对象。除非在创建对象时提供了特定于对象的值，否则将向对象提供存储区的缺省保留时间值。如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。 

保留期设置 `MinimumRetention`、`DefaultRetention` 和 `MaximumRetention` 的最小和最大支持值分别为 0 天和 365243 天（1000 年）。 

`Content-MD5` 头是必需的。此操作不会使用其他查询参数。

有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
{:tip}

{: http}

**语法**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**示例请求**
{: http}

```
PUT /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
x-amz-content-sha256: 2938f51643d63c864fdbea618fe71b13579570a86f39da2837c922bae68d72df
Content-MD5: GQmpTNpruOyK6YrxHnpj7g==
Content-Type: text/plain
Host: 67.228.254.193
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

**示例响应**
{: http}

```
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.14.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```
{: codeblock}
{: http}

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

```java
public static void addProtectionConfigurationToBucket(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    cos.setBucketProtection(bucketName, newConfig);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}

public static void addProtectionConfigurationToBucketWithRequest(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    SetBucketProtectionConfigurationRequest newRequest = new SetBucketProtectionConfigurationRequest()
        .withBucketName(bucketName)
        .withProtectionConfiguration(newConfig);

    cos.setBucketProtectionConfiguration(newRequest);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}
```
{: codeblock}
{: java}

### 检查存储区上的保留时间策略
{: #immutable-sdk-get}

此 GET 操作实现访存现有存储区的保留时间参数。
{: http}

**语法**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**示例请求**
{: http}

```xml
GET /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
Content-Type: text/plain
Host: 67.228.254.193
Example response
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.13.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

如果存储区上没有保护配置，那么服务器会改为使用已禁用状态进行响应。
{: http}

```xml
<ProtectionConfiguration>
  <Status>Disabled</Status>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

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

```java
public static void getProtectionConfigurationOnBucket(String bucketName) {
    System.out.printf("Retrieving protection configuration from bucket: %s\n", bucketName;

    BucketProtectionConfiguration config = cos.getBucketProtection(bucketName);

    String status = config.getStatus();

    System.out.printf("Status: %s\n", status);

    if (!status.toUpperCase().equals("DISABLED")) {
        System.out.printf("Minimum Retention (Days): %s\n", config.getMinimumRetentionInDays());
        System.out.printf("Default Retention (Days): %s\n", config.getDefaultRetentionInDays());
        System.out.printf("Maximum Retention (Days): %s\n", config.getMaximumRetentionInDays());
    }
}
```
{: codeblock}
{: java}

### 将对象上传到使用保留时间策略的存储区
{: #immutable-sdk-upload}

此 `PUT` 操作增强功能添加了三个新的请求头：两个用于以不同方式指定保留期，另一个用于向新对象添加单个合法保留。针对新头的非法值定义了新错误，并且如果对象处于保留期内，那么任何覆盖操作都会失败。
{: http}

如果覆盖使用保留时间策略的存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。

`Content-MD5` 头是必需的。
{: http}

这些头还适用于 POST 对象和分块上传请求。如果通过分块上传对象，那么每个分块都需要 `Content-MD5` 头。
{: http}

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

```java
public static void putObjectAddLegalHold(String bucketName, String objectName, String fileText, String legalHoldId) {
    System.out.printf("Add legal hold %s to %s in bucket %s with a putObject operation.\n", legalHoldId, objectName, bucketName);

    InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileText.length());

    PutObjectRequest req = new PutObjectRequest(
        bucketName,
        objectName,
        newStream,
        metadata
    );
    req.setRetentionLegalHoldId(legalHoldId);

    cos.putObject(req);

    System.out.printf("Legal hold %s added to object %s in bucket %s\n", legalHoldId, objectName, bucketName);
}

public static void copyProtectedObject(String sourceBucketName, String sourceObjectName, String destinationBucketName, String newObjectName) {
    System.out.printf("Copy protected object %s from bucket %s to %s/%s.\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);

    CopyObjectRequest req = new CopyObjectRequest(
        sourceBucketName, 
        sourceObjectName, 
        destinationBucketName, 
        newObjectName
    );
    req.setRetentionDirective(RetentionDirective.COPY);
    

    cos.copyObject(req);

    System.out.printf("Protected object copied from %s/%s to %s/%s\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);
}
```
{: codeblock}
{: java}

### 向对象添加合法保留或除去对象的合法保留
{: #immutable-sdk-legal-hold}

此 `POST` 操作实现使用 `legalHold` 查询参数以及 `add` 和 `remove` 查询参数来向受保护存储区中的受保护对象添加单个合法保留，或除去其合法保留。
{: http}

一个对象可以支持 100 个合法保留：

*  合法保留标识是一个字符串，最大长度为 64 个字符，最小长度为 1 个字符。有效字符为字母、数字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 和 `。
* 如果添加给定合法保留将导致对象上超过 100 个合法保留，那么不会添加新的合法保留，并且将返回 `400` 错误。
* 如果标识太长，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识包含无效字符，那么不会将其添加到对象，并且将返回 `400` 错误。
* 如果标识已在对象上使用，那么不会修改现有合法保留，响应会指示该标识已在使用，并返回 `409` 错误。
* 如果对象没有保留期元数据，那么将返回 `400` 错误，并且不允许添加或除去合法保留。

保留期头必须存在，否则会返回 `400` 错误。
{: http}

添加或除去合法保留的用户必须具有对此存储区的`管理者`许可权。

`Content-MD5` 头是必需的。此操作不会使用特定于操作的有效内容元素。
{: http}

**语法**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**示例请求**
{: http}

```
POST /BucketName/ObjectName?legalHold&add=legalHoldID HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
```
{: codeblock}
{: http}

**示例响应**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}

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

```java
public static void addLegalHoldToObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Adding legal hold %s to object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.addLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s added to object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}

public static void deleteLegalHoldFromObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Deleting legal hold %s from object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.deleteLegalHold(
        bucketName, 
        objectName, 
        legalHoldId
    );

    System.out.printf("Legal hold %s deleted from object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}
```
{: codeblock}
{: java}

### 延长对象的保留期
{: #immutable-sdk-extend}

此 `POST` 操作实现使用 `extendRetention` 查询参数来延长受保护存储区中受保护对象的保留期。
{: http}

对象的保留期只能延长。不能在当前配置值的基础上缩短。

保留时间延长值可通过以下三种方式之一进行设置：

* 在当前值的基础上增加时间（`Additional-Retention-Period` 或类似方法）
* 新的延长时间段（以秒为单位）（`Extend-Retention-From-Current-Time` 或类似方法）
* 对象的新保留到期日期（`New-Retention-Expiration-Date` 或类似方法）

根据 `extendRetention` 请求中设置的参数，对象元数据中存储的当前保留期可通过给定更多时间延长，也可替换为新值。在所有情况下，都会根据当前保留期来检查延长保留时间参数，并且仅当更新的保留期大于当前保留期时，才会接受延长参数。

如果覆盖受保护存储区中不再保留的对象（保留期已到期，并且对象没有任何合法保留），那么会再次保留这些对象。可以在对象覆盖请求中提供新的保留期，否则会为对象提供存储区的缺省保留时间。

**语法**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**示例请求**
{: http}

```yaml
POST /BucketName/ObjectName?extendRetention HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00GMT
Authorization: authorization string
Content-Type: text/plain
Additional-Retention-Period: 31470552
```
{: codeblock}
{: http}

**示例响应**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:50:00GMT
Connection: close
```
{: codeblock}
{: http}

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

```java
public static void extendRetentionPeriodOnObject(String bucketName, String objectName, Long additionalSeconds) {
    System.out.printf("Extend the retention period on %s in bucket %s by %s seconds.\n", objectName, bucketName, additionalSeconds);

    ExtendObjectRetentionRequest req = new ExtendObjectRetentionRequest(
        bucketName, 
        objectName)
        .withAdditionalRetentionPeriod(additionalSeconds);

    cos.extendObjectRetention(req);

    System.out.printf("New retention period on %s is %s\n", objectName, additionalSeconds);
}
```
{: codeblock}
{: java}

### 列出对象上的合法保留
{: #immutable-sdk-list-holds}

此 `GET` 操作实现使用 `legalHold` 查询参数来返回 XML 响应主体中对象上的合法保留及相关保留状态的列表。
{: http}

此操作会返回以下内容：

* 对象创建日期
* 对象保留期（秒）
* 根据时间段和创建日期计算的保留到期日期
* 合法保留的列表
* 合法保留标识
* 应用合法保留时的时间戳记

如果对象上没有合法保留，那么会返回空的 `LegalHoldSet`。如果在对象上未指定保留期，那么会返回 `404` 错误。

**语法**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**示例请求**
{: http}

```
GET /BucketName/ObjectName?legalHold HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: {authorization-string}
Content-Type: text/plain
```
{: codeblock}
{: http}

**示例响应**
{: http}

```xml
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
<?xml version="1.0" encoding="UTF-8"?>
<RetentionState>
  <CreateTime>Fri, 8 Sep 2018 21:33:08 GMT</CreateTime>
  <RetentionPeriod>220752000</RetentionPeriod>
  <RetentionPeriodExpirationDate>Fri, 1 Sep 2023 21:33:08
GMT</RetentionPeriodExpirationDate>
  <LegalHoldSet>
    <LegalHold>
      <ID>SomeLegalHoldID</ID>
      <Date>Fri, 8 Sep 2018 23:13:18 GMT</Date>
    </LegalHold>
    <LegalHold>
    ...
    </LegalHold>
  </LegalHoldSet>
</RetentionState>
```
{: codeblock}
{: http}

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

```java
public static void listLegalHoldsOnObject(String bucketName, String objectName) {
    System.out.printf("List all legal holds on object %s in bucket %s\n", objectName, bucketName);

    ListLegalHoldsResult result = cos.listLegalHolds(
        bucketName, 
        objectName
    );

    System.out.printf("Legal holds on bucket %s: \n", bucketName);

    List<LegalHold> holds = result.getLegalHolds();
    for (LegalHold hold : holds) {
        System.out.printf("Legal Hold: %s", hold);
    }
}
```
{: codeblock}
{: java}
