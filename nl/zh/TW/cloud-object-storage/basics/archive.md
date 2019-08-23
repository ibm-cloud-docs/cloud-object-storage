---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# 使用轉移規則保存原始資料
{: #archive}

「{{site.data.keyword.cos_full}} 保存」是一個[低成本](https://www.ibm.com/cloud/object-storage)選項，適用於很少存取的資料。您可以藉由將資料從所有儲存空間層級（Standard、Vault、Cold Vault 及 Flex）轉移至長期離線保存檔來儲存資料，或使用線上 Cold Vault 選項。
{: shortdesc}

您可以使用 Web 主控台、REST API 以及與 IBM Cloud Object Storage 整合的協力廠商工具來保存物件。 

如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
{:tip}

## 新增或管理儲存區的保存原則
{: #archive-add}

建立或修改儲存區的保存原則時，請考量下列事項：

* 保存原則可以隨時新增至新的或現有儲存區。 
* 可以修改或停用現有的保存原則。 
* 剛新增或修改的保存原則適用於已上傳的新物件，且不會影響現有物件。

若要立即保存已上傳至儲存區的新物件，請在保存原則上輸入 0 天。
{:tip}

保存檔僅適用於特定地區。如需詳細資料，請參閱[整合式服務](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)。
{:tip}

## 還原保存物件
{: #archive-restore}

為了能夠存取保存物件，您必須將它還原至原始儲存空間層級。還原物件時，您可以指定希望物件可供使用的天數。在指定的期間結束時，即會刪除還原的副本。 

還原處理程序最多可能需要 12 小時。
{:tip}

保存物件子狀態如下：

* 已保存：根據儲存區上的保存原則，已將處於已保存狀態的物件從其線上儲存空間層級（Standard、Vault、Cold Vault 及 Flex）移至離線保存層級。
* 還原中：處於還原中狀態的物件正在產生從已保存狀態到其原始線上儲存空間層級的副本。
* 已還原：處於已還原狀態的物件是已還原至其原始線上儲存空間層級一段指定時間的保存物件副本。在此期間結束時，即會刪除物件的副本，同時維護保存物件。

## 限制
{: #archive-limitations}

保存原則是使用 `PUT 儲存區生命週期配置` S3 API 作業的子集進行實作。 

支援的功能包括：
* 指定物件轉移至已保存狀態時的日期或未來天數。
* 設定物件的[到期規則](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry)。

不受支援的功能包括：
* 每個儲存區的多個轉移規則。
* 過濾物件以使用字首或物件索引鍵進行保存。
* 儲存空間類別之間的分層。

## 使用 REST API 及 SDK
{: #archive-api} 

### 建立儲存區生命週期配置
{: #archive-api-create} 
{: http}

`PUT` 作業的這項實作會使用 `lifecycle` 查詢參數來設定儲存區的生命週期設定。此作業容許給定儲存區的單一生命週期原則定義。該原則定義為包含下列參數的規則：`ID`、`Status` 及 `Transition`。
{: http}

轉移動作會在定義的一段時間之後，讓寫入儲存區的未來物件進入已保存狀態。對儲存區的生命週期原則所做的變更，**只會套用至寫入該儲存區的新物件**。

Cloud IAM 使用者必須至少具有 `Writer` 角色，才能將生命週期原則新增至儲存區。

「標準基礎架構使用者」必須具有「擁有者許可權」，並且能夠在儲存空間帳戶中建立儲存區，才能將生命週期原則新增至儲存區。

此作業不會使用其他作業特定查詢參數。
{: http}

標頭                    | 類型   | 說明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` |字串   | **必要**：有效負載的 base64 編碼 128 位元 MD5 雜湊，用來作為完整性檢查，以確保未在傳輸中變更有效負載。
{: http}

要求的內文必須包含具有下列綱目的 XML 區塊：
{: http}

| 元素                  | 類型                 | 子項                               | 上代                 | 限制                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | 無                     | 限制 1。                                                                                  |
| `Rule`                   | Container            | `ID`、`Status`、`Filter`、`Transition` | `LifecycleConfiguration` | 限制 1。                                                                                  |
| `ID`                     | 字串     | 無                                   | `Rule`                   | 必須包含（`a-z、`A-Z0-9`）及下列符號：`!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | 字串     | `Prefix`                               | `Rule`                   | 必須包含 `Prefix` 元素                                                            |
| `Prefix`                 | 字串     | 無                                   | `Filter`                 | **必須**設為 `<Prefix/>`。|
| `Transition`             | `Container`          | `Days`、`StorageClass`                 | `Rule`                   | 限制 1。                                                                                  |
| `Days`                   | 非負整數 | 無                                   | `Transition`             | 必須是大於 0 的值。|
| `Date`                   | Date                 | 無                                   | `Transistion`            | 必須為「ISO 8601 格式」，而且日期必須是未來日期。|
| `StorageClass`           | 字串     | 無                                   | `Transition`             | **必須**設為 `GLACIER`。                                                             |
{: http}

__語法__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="範例 1. 注意此語法範例中斜線及點的用法。" caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
		<Filter>
			<Prefix/>
		</Filter>
		<Transition>
			<Days>{integer}</Days>
			<StorageClass>GLACIER</StorageClass>
		</Transition>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="範例 2. 用於建立物件生命週期配置的 XML 範例。" caption-side="bottom"}

__範例__
{: http}

_要求範例_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="範例 3. 用於建立物件生命週期配置的要求標頭範例。" caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="範例 4. PUT 要求內文的 XML 範例。" caption-side="bottom"}

_回應範例_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="範例 5. 回應標頭。" caption-side="bottom"}

---

### 擷取儲存區生命週期配置
{: #archive-api-retrieve} 
{: http}

`GET` 作業的這項實作會使用 `lifecycle` 查詢參數來擷取儲存區的生命週期設定。 

Cloud IAM 使用者必須至少具有 `Reader` 角色，才能擷取儲存區的生命週期。

「標準基礎架構使用者」必須至少具有儲存區的 `Read` 許可權，才能擷取儲存區的生命週期原則。

此作業不會使用其他作業特定標頭、查詢參數或有效負載。

__語法__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="範例 6. GET 要求的語法變異。" caption-side="bottom"}

__範例__
{: http}

_要求範例_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="範例 7. 用於擷取配置的要求標頭範例。" caption-side="bottom"}

_回應範例_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="範例 8. GET 要求中的回應標頭範例。" caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="範例 9. 回應內文的 XML 範例。" caption-side="bottom"}

---

### 刪除儲存區生命週期配置
{: #archive-api-delete} {: http}

`DELETE` 作業的這項實作會使用 `lifecycle` 查詢參數來移除儲存區的任何生命週期設定。對於新物件，不會再發生規則所定義的轉移。 

**附註：**刪除規則之前，對於已寫入儲存區的物件，將會維護現有轉移規則。

Cloud IAM 使用者必須至少具有 `Writer` 角色，才能移除儲存區中的生命週期原則。

「標準基礎架構使用者」必須具有儲存區的 `Owner` 許可權，才能移除儲存區中的生命週期原則。

此作業不會使用其他作業特定標頭、查詢參數或有效負載。

__語法__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="範例 10. 注意語法範例中斜線及點的用法。" caption-side="bottom"}

__範例__
{: http}

_要求範例_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="範例 11. DELETE HTTP 動詞的要求標頭範例。" caption-side="bottom"}

_回應範例_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="範例 12. DELETE 要求中的回應範例。" caption-side="bottom"}

---

### 暫時還原保存物件 
{: #archive-api-restore} {: http}

`POST` 作業的這項實作會使用 `restore` 查詢參數來要求暫時還原保存物件。下載或修改物件之前，使用者必須先還原保存物件。還原物件時，使用者必須指定一個期間，在此之後，將會刪除物件的暫存副本。此物件會維護儲存區的儲存空間類別。

在還原的副本可供存取之前，可能會有長達 12 小時的延遲。`HEAD` 要求可查看還原的副本是否可供使用。 

若要永久地還原物件，使用者必須將已還原的物件複製到沒有作用中生命週期配置的儲存區。

Cloud IAM 使用者必須至少具有 `Writer` 角色，才能還原物件。

「標準基礎架構」使用者必須至少具有儲存區的 `Write` 許可權以及物件的 `Read` 許可權，才能將其還原。

此作業不會使用其他作業特定查詢參數。

標頭                    | 類型   | 說明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` |字串   | **必要**：有效負載的 base64 編碼 128 位元 MD5 雜湊，用來作為完整性檢查，以確保未在傳輸中變更有效負載。

要求的內文必須包含具有下列綱目的 XML 區塊：

元素                  | 類型      | 子項                               | 上代                 | 限制
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | Container | `Days`、`GlacierJobParameters`    | 無       | 無
`Days`                   | 整數 | 無 | `RestoreRequest` |指定暫時還原物件的生命期限。還原物件副本可能存在的天數下限是 1。在經歷還原期間之後，將會移除物件的暫存副本。
`GlacierJobParameters` | 字串   | `Tier` | `RestoreRequest` | 無
`Tier` | 字串   | 無 | `GlacierJobParameters` |**必須**設為 `Bulk`。

如果物件處於已保存狀態，則成功回應會傳回 `202`，如果物件已處於已還原狀態，則成功回應會傳回 `200`。如果物件已處於已還原狀態，且收到還原物件的新要求，則 `Days` 元素將會更新已還原物件的有效期限。

__語法__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="範例 13. 注意語法範例中斜線及點的用法。" caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="範例 14. 要求內文的 XML 模型。" caption-side="bottom"}

__範例__
{: http}

_要求範例_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="範例 15. 用於物件還原的要求標頭範例。" caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="範例 16. 用於物件還原的要求內文範例。" caption-side="bottom"}

_回應範例_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="範例 17. 用來還原物件的回應 (`HTTP 202`)。" caption-side="bottom"}

---

### 取得物件的標頭
{: http}
{: #archive-api-head}

給定物件路徑的 `HEAD` 會擷取該物件的標頭。此作業不會使用作業特定查詢參數或有效負載元素。

__語法__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="範例 18. 定義端點的變異。" caption-side="bottom"}


__用於保存物件的回應標頭__
{: http}

標頭 | 類型 | 說明
--- | ---- | ------------
`x-amz-restore` |字串   | 如果已還原物件，或正在進行還原，則會包含此項目。如果已還原物件，則同時會傳回暫存副本的到期日。
`x-amz-storage-class` |字串   | 如果已保存或暫時還原，則傳回 `GLACIER`。
`x-ibm-archive-transition-time` | date          | 傳回物件排定要轉移至保存層級的日期和時間。
`x-ibm-transition` |字串   | 如果物件具有轉移 meta 資料，並傳回轉移的層級及原始時間，則會包含此項目。
`x-ibm-restored-copy-storage-class` |字串   | 如果物件處於 `RestoreInProgress` 或 `Restored` 狀態，並傳回儲存區的儲存空間類別，則會包含此項目。


_要求範例_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="範例 19. 顯示要求標頭的範例。" caption-side="bottom"}

_回應範例_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="範例 20. 顯示回應標頭的範例。" caption-side="bottom"}


### 建立儲存區生命週期配置
{: #archive-node-create} 
{: javascript}

```js
var params = {
      Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'STRING_VALUE',
        Filter: '', /* required */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* required if Days not specified */
            Days: 0, /* required if Date not specified */
            StorageClass: 'GLACIER' /* required */
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="範例 21. 顯示如何建立生命週期配置的範例。" caption-side="bottom"}

### 擷取儲存區生命週期配置
{: #archive-node-retrieve} {: javascript}

```js
var params = {
      Bucket: 'STRING_VALUE' /* required */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="範例 22. 顯示如何擷取生命週期 meta 資料的範例。" caption-side="bottom"}

### 刪除儲存區生命週期配置
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="範例 23. 顯示如何刪除儲存區生命週期配置的範例。" caption-side="bottom"}

### 暫時還原保存物件 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
      Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
  ContentMD5: 'STRING_VALUE', /* required */
  RestoreRequest: {
   Days: 1, /* days until copy expires */
   GlacierJobParameters: {
     Tier: Bulk /* required */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="範例 24. 用於還原保存物件的程式碼。" caption-side="bottom"}

### 取得物件的標頭
{: #archive-node-head} 
{: javascript}

```js
var params = {
      Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // an error occurred
  else   
    console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="範例 25. 顯示如何擷取物件標頭的範例。" caption-side="bottom"}


### 建立儲存區生命週期配置
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="範例 26. 用於建立物件配置的方法。" caption-side="bottom"}

### 擷取儲存區生命週期配置
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="範例 27. 用於擷取物件配置的方法。" caption-side="bottom"}

### 刪除儲存區生命週期配置
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="範例 28. 用於刪除物件配置的方法。" caption-side="bottom"}

### 暫時還原保存物件 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="範例 29. 暫時還原保存物件。" caption-side="bottom"}

### 取得物件的標頭
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="範例 30. 處理物件標頭的回應。" caption-side="bottom"}


### 建立儲存區生命週期配置
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="範例 31. 用於設定儲存區生命週期的函數。" caption-side="bottom"}

**方法摘要**
{: java}

方法 |  說明
--- | ---
`getBucketName()` | 取得將設定其生命週期配置的儲存區的名稱。
`getLifecycleConfiguration()` | 取得指定儲存區的新生命週期配置。
`setBucketName(String bucketName)` | 設定將設定其生命週期配置的儲存區的名稱。
`withBucketName(String bucketName)` | 設定將設定其生命週期配置的儲存區的名稱，並傳回此物件，以將其他方法呼叫鏈結在一起。
{: java}

### 擷取儲存區生命週期配置
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="範例 32. 用於取得物件生命週期配置的函數簽章。" caption-side="bottom"}

### 刪除儲存區生命週期配置
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="範例 33. 用於刪除物件配置的函數。" caption-side="bottom"}

### 暫時還原保存物件 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="範例 34. 用於還原保存物件的函數簽章。" caption-side="bottom"}

**方法摘要**
{: java}

方法 |  說明
--- | ---
`clone()` | 針對處理程式環境定義以外的所有欄位，建立此物件的表層複製品。
`getBucketName()` | 傳回儲存區的名稱，其中包含要還原之物件的參照。
`getExpirationInDays()` | 傳回物件從建立至到期的時間（以天為單位）。
`setExpirationInDays(int expirationInDays)` | 設定將物件上傳至儲存區與物件到期之間的時間（以天為單位）。
{: java}

### 取得物件的標頭
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="範例 35. 用於取得物件標頭的函數。" caption-side="bottom"}

**方法摘要**
{: java}

方法 |  說明
--- | ---
`clone()` | 傳回此 `ObjectMetadata` 的複製品。
`getRestoreExpirationTime()` | 傳回已從 ARCHIVE 暫時還原的物件將到期，而且需要重新還原才能存取的時間。
`getStorageClass() ` | 傳回儲存區的原始儲存空間類別。
`getIBMTransition()` | 傳回轉移儲存空間類別及轉移時間。
{: java}

## 後續步驟
{: #archive-next-steps}

除了 {{site.data.keyword.cos_full_notm}} 之外，{{site.data.keyword.cloud_notm}} 目前還會針對不同的使用者需求提供數個其他物件儲存空間供應項目，而且所有這些項目都可以透過 Web 型入口網站及 REST API 進行存取。[進一步瞭解。](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
