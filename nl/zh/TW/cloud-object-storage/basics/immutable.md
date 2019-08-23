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

# 使用 Immutable Object Storage
{: #immutable}

Immutable Object Storage 容許用戶端保留電子記錄，並以 WORM（一寫多讀）、不可消除且不可重寫的方式來維護資料完整性，直到其保留期間結束以及移除所有合法保留為止。任何在其環境中有長期資料保留需求的用戶端都可以使用此特性，包括但不限於下列產業中的組織：

 * 金融
 * 醫療保健
 * 媒體內容存檔
 * 尋求防止特許修改或刪除物件或文件者

基礎特性功能也可以供處理金融記錄管理的組織（例如經紀商/自營商交易）使用，而且可能需要以不可重寫及不可消除的格式來保留物件。

只有特定地區才提供 Immutable Object Storage。如需詳細資料，請參閱[整合式服務](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)。它同時需要標準定價方案。如需詳細資料，請參閱[定價](https://www.ibm.com/cloud/object-storage)。
{:note}

不可以搭配使用 Aspera 高速傳送與具有保留原則的儲存區。
{:important}

## 術語與用法
{: #immutable-terminology}

### 保留期間 (Retention period)
{: #immutable-terminology-period}

物件必須持續儲存在 COS 儲存區中的持續時間。

### 保留原則 (Retention policy)
{: #immutable-terminology-policy}

在 COS 儲存區層次啟用保留原則。此原則定義保留期間下限、上限及預設保留期間，並將其套用至儲存區中的所有物件。

保留期間下限是物件必須保留在儲存區中的持續時間下限。

保留期間上限是物件可以保留在儲存區中的持續時間上限。

如果物件儲存在儲存區中，但未指定自訂保留期間，則會使用預設保留期間。保留期間下限必須小於或等於預設保留期間，而預設保留期間必須小於或等於保留期間上限。

附註：物件的保留期間上限可以指定為 1000 年。

附註：為了能夠在儲存區上建立保留原則，您將需要「管理員」角色。如需詳細資料，請參閱[儲存區許可權](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions)。

### 合法保留 (Legal hold) 
{: #immutable-terminology-hold}

即使在保留期間到期之後，還是可能需要防止特定記錄（物件）被刪除，例如，擱置完成的法律檢閱可能需要長時間存取記錄，而這段時間超過原本為物件設定的保留期間。在這類情境中，可以在物件層次套用合法保留旗標。
 
在起始上傳至 cos 儲存區期間或在新增物件之後，可以將合法保留套用至物件。
 
附註：每個物件最多可以套用 100 個合法保留。

### 無限期保留 (Indefinite retention)
{: #immutable-terminology-indefinite}

容許使用者將物件設定為無限期儲存，直到套用新的保留期間為止。這是根據物件層次所設定的。

### 事件型保留 (Event-based retention)
{: #immutable-terminology-events}

Immutable Object Storage 容許使用者在不確定其使用案例保留期間的最終持續時間時，或想要利用事件型保留功能時，設定無限期保留物件。設為無限期之後，使用者應用程式可以在之後將物件保留變更為有限值。例如，公司具有在員工離開公司之後保留員工記錄三年的原則。員工加入公司時，就可以無限期保留與該員工相關聯的記錄。員工離開公司時，即會將無限期保留轉換為從現行時間算起 3 年的有限值（由公司原則所定義）。接著，在保留期間變更之後，該物件會受到保護 3 年。使用者或協力廠商應用程式可以使用 SDK 或 REST API，將保留期間從無限期變更為有限保留。

### 永久保留 (Permanent retention)
{: #immutable-terminology-permanent}

只能在啟用保留原則的 COS 儲存區層次啟用永久保留，而且使用者可以在物件上傳期間選取永久保留期間選項。啟用之後，就無法回復此處理程序，而且**無法刪除**使用永久保留期間所上傳的物件。使用者有責任在其結束時驗證是否合法需要使用具有保留原則的 COS 儲存區**永久地**儲存物件。


使用 Immutable Object Storage 時，您負責確保只要資料受到保留原則的約束，「IBM Cloud 帳戶」就能根據 IBM Cloud 原則及準則保持良好狀態。如需相關資訊，請參閱「IBM Cloud 服務」條款。
{:important}

## Immutable Object Storage 及各種法規的考量
{: #immutable-regulation}

使用 Immutable Object Storage 時，用戶端必須負責檢查並確定是否可以運用所討論的所有特性功能，來滿足並遵守一般由下列項目所控管的電子記錄儲存和保留的關鍵規則：

  * [美國證券交易委員會 (SEC) 規則 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64)、
  * [美國金融業監管局 (FINRA) 規則 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957) 及
  * [商品期貨交易委員會 (CFTC) 規則 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

為了協助用戶端做出明智的決策，IBM 已與 Cohasset Associates Inc. 合作，對 IBM 的 Immutable Object Storage 功能進行獨立評量。請檢閱 Cohasset Associates Inc. 的[報告](https://www.ibm.com/downloads/cas/JBDNP0KV)，此報告提供評量 IBM Cloud Object Storage 的 Immutable Object Storage 功能的詳細資料。

### 存取及交易的審核
{: #immutable-audit}
藉由開立客戶服務問題單，即可逐案提供 Immutable Object Storage 檢閱保留參數、物件保留期間以及合法保留套用變更的存取日誌資料。

## 使用主控台
{: #immutable-console}

保留原則可以新增至新的或現有的空儲存區，且無法予以移除。若為新儲存區，請確定您要在[支援的地區](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)中建立儲存區，然後選擇**新增保留原則**選項。若為現有儲存區，請確定它沒有任何物件，並導覽至配置設定，然後按一下儲存區保留原則區段下方的**建立原則**按鈕。在任一情況下，都請設定保留期間下限、上限及預設保留期間。

## 使用 REST API、程式庫及 SDK
{: #immutable-sdk}

IBM COS SDK 中引進了數個新的 API，用來支援使用保留原則的應用程式。請在此頁面頂端選取語言（HTTP、Java、Javascript 或 Python），以使用適當的 COS SDK 來檢視範例。 

請注意，所有程式碼範例都假設存在稱為 `cos` 且可以呼叫不同方法的用戶端物件。如需建立用戶端的詳細資料，請參閱特定 SDK 手冊。

所有用來設定保留期間的日期值都為 GMT。需要有 `Content-MD5` 標頭，才能確保資料完整性，並且在使用 SDK 時自動傳送。
{:note}

### 在現有儲存區上新增保留原則
{: #immutable-sdk-add-policy}
`PUT` 作業的這項實作會使用 `protection` 查詢參數來設定現有儲存區的保留參數。此作業容許您設定或變更保留期間下限、上限及預設保留期間。此作業也容許您變更儲存區的保護狀態。 

除非保護期間過期，並移除物件上的所有合法保留，否則無法刪除已寫入受保護儲存區的物件。除非在建立物件時提供物件特定值，否則會將儲存區的預設保留值提供給物件。受保護儲存區中不再保留的物件（保留期間已過期，而且物件沒有任何合法保留），在改寫時，將再次被保留。在物件改寫要求中可以提供新的保留期間，否則會將儲存區的預設保留時間提供給物件。 

支援的保留期間設定 `MinimumRetention`、`DefaultRetention` 及 `MaximumRetention` 值下限及上限分別為 0 天及 365243 天（1000 年）。 

需要有 `Content-MD5` 標頭。此作業不會使用其他查詢參數。

如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
{:tip}

{: http}

**語法**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**要求範例**
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

**回應範例**
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

### 檢查儲存區的保留原則
{: #immutable-sdk-get}

GET 作業的這項實作會提取現有儲存區的保留參數。
{: http}

**語法**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**要求範例**
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

如果儲存區上沒有任何保護配置，則伺服器回應會改為已停用狀態。
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

### 將物件上傳至具有保留原則的儲存區
{: #immutable-sdk-upload}

`PUT` 作業的這項加強會新增三個新的要求標頭：兩個用於以不同的方式指定保留期間，而一個用於將單一合法保留新增至新物件。針對新標頭的無效值定義新的錯誤，而且，如果保留物件，則任何改寫都會失敗。
{: http}

具有保留原則的儲存區中不再保留的物件（保留期間已過期，而且物件沒有任何合法保留），在改寫時，將再次被保留。在物件改寫要求中可以提供新的保留期間，否則會將儲存區的預設保留時間提供給物件。

需要有 `Content-MD5` 標頭。
{: http}

這些標頭也適用於 POST 物件及多部分上傳要求。如果上傳多個組件中的物件，則每個組件都需要有 `Content-MD5` 標頭。
{: http}

| 值	| 類型	| 說明 |
| --- | --- | --- | 
| `Retention-Period` | 非負整數（秒）| 在物件上儲存的保留期間（以秒為單位）。除非已過保留期間中指定的時間量，否則無法改寫、也不能刪除物件。如果指定此欄位及 `Retention-Expiration-Date`，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 `DefaultRetention` 期間。假設儲存區的保留期間下限也是 `0`，則零 (`0`) 是合法值。|
| `Retention-expiration-date` | 日期（ISO 8601 格式）| 將合法刪除或修改物件的日期。您只能指定此標頭或 Retention-Period 標頭。如果同時指定兩者，則會傳回 `400` 錯誤。如果未指定任一項，則會使用儲存區的 DefaultRetention 期間。|
| `Retention-legal-hold-id` |字串   | 要套用至物件的單一合法保留。合法保留是 Y 字元長度的字串。除非移除與物件相關聯的所有合法保留，否則無法改寫或刪除物件。|

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

### 新增或移除物件的合法保留
{: #immutable-sdk-legal-hold}

`POST` 作業的這項實作會使用 `legalHold` 查詢參數以及 `add` 和 `remove` 查詢參數，以新增或移除受保護儲存區中受保護物件的單一合法保留。
{: http}

此物件可以支援 100 個合法保留：

*  合法保留 ID 是長度上限為 64 個字元且長度下限為 1 個字元的字串。有效字元為字母、數字、`!`、`_`、`.`、`*`、`(`、`)`、`-` 及 `。
* 如果新增的給定合法保留超過物件的 100 個合法保留總計，則不會新增合法保留，並會傳回 `400` 錯誤。
* 如果 ID 太長，則不會將它新增至物件，並會傳回 `400` 錯誤。
* 如果 ID 包含無效字元，則不會將它新增至物件，並會傳回 `400` 錯誤。
* 如果已在物件上使用 ID，則不會修改現有的合法保留，而且回應會指出已在使用 ID，且發生 `409` 錯誤。
* 如果物件沒有保留期間 meta 資料，則會傳回 `400` 錯誤，而且不容許新增或移除合法保留。

需要具有保留期間標頭，否則會傳回 `400` 錯誤。
{: http}

新增或移除合法保留的使用者，必須具有此儲存區的 `Manager` 許可權。

需要有 `Content-MD5` 標頭。此作業不會使用作業特定有效負載元素。
{: http}

**語法**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**要求範例**
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

**回應範例**
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

### 延長物件的保留期間
{: #immutable-sdk-extend}

`POST` 作業的這項實作會使用 `extendRetention` 查詢參數，來延長受保護儲存區中受保護物件的保留期間。
{: http}

只能延長物件的保留期間。它無法減少目前配置的值。

保留擴充值是使用下列三種方式之一來設定：

* 從現行值算起的額外時間（`Additional-Retention-Period` 或類似方法）
* 新的擴充期間（以秒為單位）（`Extend-Retention-From-Current-Time` 或類似方法）
* 物件的新保留到期日（`New-Retention-Expiration-Date` 或類似方法）

物件 meta 資料中所儲存的現行保留期間可透過給定的額外時間增加，或取代為新值，視 `extendRetention` 要求中所設定的參數而定。無論如何，會根據現行保留期間來檢查延長保留參數，而且只有在更新的保留期間大於現行保留期間時，才會接受延伸參數。

受保護儲存區中不再保留的物件（保留期間已過期，而且物件沒有任何合法保留），在改寫時，將再次被保留。在物件改寫要求中可以提供新的保留期間，否則會將儲存區的預設保留時間提供給物件。

**語法**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**要求範例**
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

**回應範例**
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

### 列出物件的合法保留
{: #immutable-sdk-list-holds}

`GET` 作業的這項實作會使用 `legalHold` 查詢參數，以傳回 XML 回應內文中物件及相關保留狀態的合法保留清單。
{: http}

此作業會傳回：

* 物件建立日期
* 物件保留期間（以秒為單位）
* 根據期間和建立日期計算的保留到期日
* 合法保留的清單
* 合法保留 ID
* 套用合法保留時的時間戳記

如果物件沒有任何合法保留，則會傳回空的 `LegalHoldSet`。如果物件未指定任何保留期間，則會傳回 `404` 錯誤。

**語法**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**要求範例**
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

**回應範例**
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
