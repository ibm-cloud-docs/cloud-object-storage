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

# 使用转换规则归档冷数据
{: #archive}

{{site.data.keyword.cos_full}} 归档是一个[低成本](https://www.ibm.com/cloud/object-storage)选项，适用于很少访问的数据。您可以通过将任何存储层（标准、保险库、冷保险库和 Flex）转换为长期脱机归档来存储数据，或者使用联机“冷保险库”选项。
{: shortdesc}

您可以使用与 IBM Cloud Object Storage 集成的 Web 控制台、REST API 和第三方工具来归档对象。 

有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
{:tip}

## 添加或管理存储区上的归档策略
{: #archive-add}

创建或修改存储区的归档策略时，请考虑以下内容：

* 可以随时将归档策略添加到新存储区或现有存储区。 
* 可以修改或禁用现有归档策略。 
* 新添加或修改的归档策略将应用于上传的新对象，但不会影响现有对象。

要立即归档上传到存储区的新对象，请在归档策略上输入 0 天。
{:tip}

归档仅在特定区域中可用。有关更多详细信息，请参阅[集成服务](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)。
{:tip}

## 复原归档对象
{: #archive-restore}

要访问归档对象，必须将其复原到原始存储层。复原对象时，可以指定希望对象可用的天数。在指定时间段结束时，将删除复原的副本。 

复原过程可能最长需要 12 小时。
{:tip}

归档对象子状态包括：

* 已存档：处于“已存档”状态的对象是根据存储区上的归档策略，已从其联机存储层（标准、保险库、冷保险库和 Flex）移至脱机归档层的对象。
* 正在复原：处于“正在复原”状态的对象是正在将“已归档”状态的副本生成到其原始联机存储层的对象。
* 已复原：处于“已复原”状态的对象是已复原到其原始联机存储层的归档对象的副本，可使用指定的时间长度。在该时间段结束时，将删除该对象副本，但仍保留相应的归档对象。

## 限制
{: #archive-limitations}

归档策略是使用 `PUT Bucket Lifecycle Configuration` S3 API 操作的子集实现的。 

支持的功能包括：
* 指定对象转换为“已归档”状态的未来日期或天数。
* 为对象设置[到期规则](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry)。

不支持的功能包括：
* 每个存储区多个转换规则。
* 使用前缀或对象键来过滤要归档的对象。
* 对存储类分层。

## 使用 REST API 和 SDK
{: #archive-api} 

### 创建存储区生命周期配置
{: #archive-api-create} 
{: http}

此 `PUT` 操作实现使用 `lifecycle` 查询参数来设置存储区的生命周期设置。此操作支持对给定存储区定义单个生命周期策略。策略定义为包含以下参数的一个规则：`ID`、`Status` 和 `Transition`。
{: http}

转换操作支持未来写入存储区的对象在定义的时间段后转换为“已归档”状态。对存储区的生命周期策略的更改**仅应用于写入该存储区的新对象**。

Cloud IAM 用户必须至少具有`写入者`角色，才能向存储区添加生命周期策略。

经典基础架构用户必须具有“所有者”许可权，并且能够在存储帐户中创建存储区，才能向存储区添加生命周期策略。

此操作不会使用特定于操作的其他查询参数。
{: http}

头|类型|描述
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5`|字符串|**必需**：有效内容的 Base64 编码的 128 位 MD5 散列，用作完整性检查，可确保有效内容在传输中未变更。
{: http}

请求主体必须包含具有以下模式的 XML 块：
{: http}

|元素|类型|子代|祖代|约束|
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
|`LifecycleConfiguration`|容器|`Rule`|无|限制为 1。|
|`Rule`|容器|`ID`、`Status`、`Filter`、`Transition`|`LifecycleConfiguration`|限制为 1。|
|`ID`|字符串|无|`Rule`|必须由（`a-z、`A-Z 和 0-9`）以及以下符号组成：`!` `_` `.` `*` `'` `(` `)` `-` |
|`Filter`|字符串|`Prefix`|`Rule`|必须包含 `Prefix` 元素|
|`Prefix`|字符串|无|`Filter`|**必须**设置为 `<Prefix/>`。|
|`Transition`|`容器`|`Days`、`StorageClass`|`Rule`|限制为 1。|
|`Days`|非负整数|无|`Transition`|必须为大于 0 的值。|
|`Date`|日期|无|`Transistion`|必须为 ISO 8601 格式，并且日期必须是未来的日期。|
|`StorageClass`|字符串|无|`Transition`|**必须**设置为 `GLACIER`。|
{: http}

__语法__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="示例 1. 请注意，在此语法示例中使用了斜杠和点。" caption-side="bottom"}

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
{: caption="示例 2. 用于创建对象生命周期配置的 XML 样本。" caption-side="bottom"}

__示例__
{: http}

_样本请求_

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
{: caption="示例 3. 用于创建对象生命周期配置的请求头样本。" caption-side="bottom"}

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
{: caption="示例 4. PUT 请求主体的 XML 样本。" caption-side="bottom"}

_样本响应_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="示例 5. 响应头。" caption-side="bottom"}

---

### 检索存储区生命周期配置
{: #archive-api-retrieve} 
{: http}

此 `GET` 操作实现使用 `lifecycle` 查询参数来检索存储区的生命周期设置。 

Cloud IAM 用户必须至少具有`读取者`角色，才能检索存储区的生命周期。

经典基础架构用户必须至少具有对存储区的`读`许可权，才能检索存储区的生命周期策略。

此操作不会使用特定于操作的其他头、查询参数或有效内容。

__语法__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="示例 6. GET 请求的语法变体。" caption-side="bottom"}

__示例__
{: http}

_样本请求_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="示例 7. 用于检索配置的样本请求头。" caption-side="bottom"}

_样本响应_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="示例 8. 来自 GET 请求的样本响应头。" caption-side="bottom"}

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
{: caption="示例 9. 响应主体的 XML 样本。" caption-side="bottom"}

---

### 删除存储区生命周期配置
{: #archive-api-delete} {: http}

此 `DELETE` 操作实现使用 `lifecycle` 查询参数来除去存储区的任何生命周期设置。规则定义的转换不会再应用于新对象。 

**注：**对于在删除规则之前已经写入存储区的对象，将保留现有的转换规则。

Cloud IAM 用户必须至少具有`写入者`角色，才能从存储区中除去生命周期策略。

经典基础架构用户必须具有对存储区的`所有者`许可权，才能从存储区中除去生命周期策略。

此操作不会使用特定于操作的其他头、查询参数或有效内容。

__语法__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="示例 10. 请注意，在语法示例中使用了斜杠和点。" caption-side="bottom"}

__示例__
{: http}

_样本请求_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="示例 11. DELETE HTTP 动词的样本请求头。" caption-side="bottom"}

_样本响应_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="示例 12. 来自 DELETE 请求的样本响应。" caption-side="bottom"}

---

### 临时复原归档对象 
{: #archive-api-restore} {: http}

此 `POST` 操作实现使用 `restore` 查询参数来请求临时复原归档对象。用户必须先复原归档对象，然后再下载或修改该对象。复原对象时，用户必须指定在多长时间后将删除对象临时副本。对象会保留存储区的存储类。

可能会延迟最长 12 小时，复原的副本才可供访问。`HEAD` 请求可以检查复原的副本是否可用。 

要永久复原对象，用户必须将该复原的对象复制到没有活动生命周期配置的存储区。

Cloud IAM 用户必须至少具有`写入者`角色，才能复原对象。

经典基础架构用户必须至少具有对存储区的`写`许可权以及具有对对象的`读`许可权，才能复原该对象。

此操作不会使用特定于操作的其他查询参数。

头|类型|描述
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5`|字符串|**必需**：有效内容的 Base64 编码的 128 位 MD5 散列，用作完整性检查，可确保有效内容在传输中未变更。

请求主体必须包含具有以下模式的 XML 块：

元素|类型|子代|祖代|约束
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest`|容器|`Days`、`GlacierJobParameters`|无|无
`Days`|整数|无|`RestoreRequest`|指定临时复原对象的生命周期。对象的复原副本可以存在的最短天数为 1。复原时间段到期后，将除去对象的临时副本。
`GlacierJobParameters`|字符串|`Tier`|`RestoreRequest`|无
`Tier`|字符串|无|`GlacierJobParameters`|**必须**设置为 `Bulk`。

如果对象处于“已归档”状态，那么成功响应将返回 `202`，如果对象已经处于“已复原”状态，那么将返回 `200`。如果对象已经处于“已复原”状态，并且收到用于复原对象的新请求，那么 `Days` 元素将更新已复原对象的到期时间。

__语法__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="示例 13. 请注意，在语法示例中使用了斜杠和点。" caption-side="bottom"}

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
{: caption="示例 14. 请求主体的 XML 模型。" caption-side="bottom"}

__示例__
{: http}

_样本请求_

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
{: caption="示例 15. 用于对象复原的样本请求头。" caption-side="bottom"}

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
{: caption="示例 16. 用于对象复原的样本请求主体。" caption-side="bottom"}

_样本响应_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="示例 17. 对复原对象的响应 (`HTTP 202`)。" caption-side="bottom"}

---

### 获取对象的头
{: http}
{: #archive-api-head}

提供有对象路径的 `HEAD` 请求将检索对象的头。此操作不会使用特定于操作的查询参数或有效内容元素。

__语法__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="示例 18. 定义端点的变体。" caption-side="bottom"}


__归档对象的响应头__
{: http}

头|类型|描述
--- | ---- | ------------
`x-amz-restore`|字符串|对象已复原或正在进行复原时，会包含此头。如果对象已复原，那么还会返回临时副本的到期日期。
`x-amz-storage-class`|字符串|已归档或临时复原时，会返回 `GLACER`。
`x-ibm-archive-transition-time`|日期|返回安排对象以转换到归档层的日期和时间。
`x-ibm-transition`|字符串|对象具有转换元数据时，会包含此头，并返回转换的层和原始转换时间。
`x-ibm-restored-copy-storage-class`|字符串|对象处于 `RestoreInProgress` 或 `Restored` 状态时，会包含此头，并返回存储区的存储类。


_样本请求_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="示例 19. 显示请求头的示例。" caption-side="bottom"}

_样本响应_

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
{: caption="示例 20. 显示响应头的示例。" caption-side="bottom"}


### 创建存储区生命周期配置
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
{: caption="示例 21. 显示创建生命周期配置的示例。" caption-side="bottom"}

### 检索存储区生命周期配置
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
{: caption="示例 22. 显示检索生命周期元数据的示例。" caption-side="bottom"}

### 删除存储区生命周期配置
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
{: caption="示例 23. 显示如何删除存储区生命周期配置的示例。" caption-side="bottom"}

### 临时复原归档对象 
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
{: caption="示例 24. 用于复原归档对象的代码。" caption-side="bottom"}

### 获取对象的头
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
{: caption="示例 25. 显示检索对象头的示例。" caption-side="bottom"}


### 创建存储区生命周期配置
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
{: caption="示例 26. 用于创建对象配置的方法。" caption-side="bottom"}

### 检索存储区生命周期配置
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="示例 27. 用于检索对象配置的方法。" caption-side="bottom"}

### 删除存储区生命周期配置
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="示例 28. 用于删除对象配置的方法。" caption-side="bottom"}

### 临时复原归档对象 
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
{: caption="示例 29. 临时复原归档对象。" caption-side="bottom"}

### 获取对象的头
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
{: caption="示例 30. 处理对象头的响应。" caption-side="bottom"}


### 创建存储区生命周期配置
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="示例 31. 用于设置存储区生命周期的函数。" caption-side="bottom"}

**方法概要**
{: java}

方法|描述
--- | ---
`getBucketName()`|获取要设置其生命周期配置的存储区的名称。
`getLifecycleConfiguration()`|获取指定存储区的新生命周期配置。
`setBucketName(String bucketName)`|设置要设置其生命周期配置的存储区的名称。
`withBucketName(String bucketName)`|设置要设置其生命周期配置的存储区的名称，并返回此对象，以便可以将其他方法调用链接在一起。
{: java}

### 检索存储区生命周期配置
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="示例 32. 用于获取对象生命周期配置的函数特征符。" caption-side="bottom"}

### 删除存储区生命周期配置
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="示例 33. 用于删除对象配置的函数。" caption-side="bottom"}

### 临时复原归档对象 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="示例 34. 用于复原归档对象的函数特征符。" caption-side="bottom"}

**方法概要**
{: java}

方法|描述
--- | ---
`clone()`|为除了处理程序上下文以外的其他所有字段创建此对象的浅克隆。
`getBucketName()`|返回引用了待复原对象的存储区的名称。
`getExpirationInDays()`|返回从对象创建到其到期之间的时间（以天为单位）。
`setExpirationInDays(int expirationInDays)`|设置在将对象上传到存储区到该对象到期之间的时间（以天为单位）。
{: java}

### 获取对象的头
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="示例 35. 用于获取对象头的函数。" caption-side="bottom"}

**方法概要**
{: java}

方法|描述
--- | ---
`clone()`| 返回此 `ObjectMetadata` 的克隆。
`getRestoreExpirationTime()`|返回从归档临时复原的对象将到期的时间，到期后需要重新复原该对象才能进行访问。
`getStorageClass() `|返回存储区的原始存储类。
`getIBMTransition()`|返回转换存储类和转换时间。
{: java}

## 后续步骤
{: #archive-next-steps}

除了 {{site.data.keyword.cos_full_notm}} 之外，{{site.data.keyword.cloud_notm}} 目前还针对不同的用户需求额外提供了多个对象存储器产品，所有这些产品都可通过基于 Web 的门户网站和 REST API 进行访问。[了解更多](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)。
