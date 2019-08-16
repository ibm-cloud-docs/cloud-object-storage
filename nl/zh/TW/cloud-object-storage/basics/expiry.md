---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-05"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:external: target="blank" .external}
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
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 
{:http: .ph data-hd-programlang='http'} 

# 使用有效期限規則刪除舊資料
{: #expiry}

有效期限規則會在定義的期間（從物件建立日期開始）之後刪除物件。 

您可以使用與 {{site.data.keyword.cos_full_notm}} 整合的 Web 主控台、REST API 及協力廠商工具，來設定物件的生命週期。 

* 有效期限規則可以新增至新的或現有儲存區。
* 可以修改或停用現有的有效期限規則。
* 新增或修改的「有效期限」規則會套用至儲存區中的所有新的及現有物件。
* 新增或修改生命週期原則需要 `Writer` 角色。 
* 每個儲存區最多可以定義 1000 個生命週期規則（保存 + 有效期限）。
* 容許最多 24 小時，讓「有效期限」規則中的所有變更生效。
* 每個有效期限規則的範圍的限制方式是定義選用的字首過濾器，只套用至名稱符合字首的物件子集。
* 沒有字首過濾器的有效期限規則將套用至儲存區中的所有物件。
* 物件的有效期限（以天數為單位指定）是從物件建立時間開始計算，四捨五入到隔天的午夜 UTC。例如，如果儲存區的有效期限規則讓一組物件在建立日期後的 10 天到期，則在 2019 年 4 月 15 日 5:10 UTC 建立的物件將在 2019 年 4 月 26 日 00:00 UTC 到期。 
* 每個儲存區的有效期限規則每 24 小時評估一次。會將符合有效期限的任何物件（以物件的到期日為基礎）置入佇列以進行刪除。刪除過期物件會從第二天開始，而且通常需要不到 24 小時。刪除物件之後，就不會針對該物件的任何關聯儲存空間向您收取任何費用。

除非不再強制執行保留原則，否則受限於儲存區 Immutable Object Storage 保留原則的物件將會延遲所有有效期限動作。
{: important}

## 有效期限規則的屬性
{: #expiry-rules-attributes}

每個有效期限規則都有下列屬性：

### ID
在儲存區的生命週期配置內，規則的 ID 必須是唯一的。

### Expiration
有效期限區塊包含控管自動刪除物件的詳細資料。這可能是未來的特定日期，或寫入新物件之後的一段時間。

### Prefix
此選用字串將與儲存區中物件名稱的字首相符。具有字首的規則只會套用至相符的物件。您可以針對相同儲存區內的不同字首，對不同的到期動作使用多個規則。例如，在相同的生命週期配置內，一個規則可以在 30 天之後刪除開頭為 `logs/` 的所有物件，而第二個規則可以在 365 天之後刪除開頭為 `video/` 的物件。  

### Status
可以啟用或停用規則。只有在啟用時，規則才會作用。

## 生命週期配置範例

此配置讓所有新物件在 30 天後到期。

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-30-days</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>30</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

此配置會在 2020 年 6 月 1 日刪除字首為 `foo/` 的所有物件。

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-on-a-date</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Date>2020-06-01T00:00:00.000Z</Date>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

您也可以結合轉移及有效期限規則。此配置會在建立之後的 90 天保存所有物件，並在 180 天後刪除字首為 `foo/` 的所有物件。

```xml
<LifecycleConfiguration>
  <Rule>
		<ID>archive-first</ID>
		<Filter />
		<Status>Enabled</Status>
    <Transition>
      <Days>90</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
	</Rule>
	<Rule>
		<ID>then-delete</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Days>180</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

## 使用主控台
{: #expiry-using-console}

建立新的儲存區時，請檢查**新增有效期限規則**方框。接下來，按一下**新增規則**，以建立新的有效期限規則。在儲存區建立期間，您可以新增最多五個規則，且之後可以新增額外的規則。

若為現有儲存區，請從導覽功能表中選取**配置**，然後按一下_有效期限規則_ 區段下的**新增規則**。

## 使用 API 及 SDK
{: #expiry-using-api-sdks}

您可以使用 REST API 或 IBM COS SDK，以程式設計方式來管理有效期限規則。在環境定義切換器中選取種類，以選取範例的格式。

### 將有效期限規則新增至儲存區的生命週期配置
{: #expiry-api-create}

**REST API 參考資料**
{: http}

`PUT` 作業的這項實作會使用 `lifecycle` 查詢參數來設定儲存區的生命週期設定。此作業容許儲存區的單一生命週期原則定義。該原則定義為包含下列參數的一組規則：`ID`、`Status`、`Filter` 及 `Expiration`。
{: http}
 
Cloud IAM 使用者必須具有 `Writer` 角色，才能移除儲存區中的生命週期原則。

「標準基礎架構使用者」必須具有儲存區的 `Owner` 許可權，才能移除儲存區中的生命週期原則。

標頭                    | 類型   | 說明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | 字串   | **必要**：有效負載的 base64 編碼 128 位元 MD5 雜湊，用來作為完整性檢查，以確保未在傳輸中變更有效負載。
{: http}

要求的內文必須包含具有下列綱目的 XML 區塊：
{: http}

| 元素                  | 類型                 | 子項                               | 上代                 | 限制                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | 無                     | 限制 1。                                                                                  |
| `Rule`                   | Container            | `ID`、`Status`、`Filter`、`Expiration` | `LifecycleConfiguration` | 限制 1000。                                                                                  |
| `ID`                     | 字串     | 無                                   | `Rule`                   | 必須包含（`a-z、`A-Z0-9`）及下列符號：`!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | 字串     | `Prefix`                               | `Rule`                   | 必須包含 `Prefix` 元素                                                            |
| `Prefix`                 | 字串     | 無                                   | `Filter`                 | 規則會套用至索引鍵符合此字首的所有物件。|
| `Expiration`             | `Container`          | `Days` 或 `Date`                       | `Rule`                   | 限制 1。                                                                                  |
| `Days`                   | 非負整數 | 無                                   | `Expiration`             | 必須是大於 0 的值。|
| `Date`                   | Date                 | 無                                   | `Expiration`             | 必須為「ISO 8601 格式」。                            |
{: http}

要求的內文必須包含具有表格中所處理綱目的 XML 區塊（請參閱範例 1）。
{: http}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="範例 1. 要求內文的 XML 範例。" caption-side="bottom"}
{: http}

**語法**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="範例 2. 注意此語法範例中斜線及點的用法。" caption-side="bottom"}
{: codeblock}
{: http}

**要求範例**
{: http}

```yaml
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305

<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="範例 3. 用於建立物件生命週期配置的要求標頭範例。" caption-side="bottom"}
{: http}

**與 NodeJS COS SDK 搭配使用的程式碼範例**
{: javascript}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);
var date = new Date('June 16, 2019 00:00:00');

var params = {
      Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* required */
        Expiration:
        {
          Date: date
        }
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

**與 Python COS SDK 搭配使用的程式碼範例**
{: python}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
{: python}

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'Status': 'Enabled',
                'Filter': {},
                'Expiration':
                {
                    'Days': 123
                },
            },
        ]
    }
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

**與 Java COS SDK 搭配使用的程式碼範例**
{: java}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Define a rule for exiring items in a bucket
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);
            
            // Add the rule to a new BucketLifecycleConfiguration.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));
            
            // Use the client to set the LifecycleConfiguration on the bucket.
            _cosClient.setBucketLifecycleConfiguration(bucketName, configuration);   
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
    }
```
{: codeblock}
{: java}
{: caption="範例 1. 顯示如何建立生命週期配置的程式碼範例。" caption-side="bottom"}

### 檢查儲存區的生命週期配置，包括有效期限
{: #expiry-api-view}

`GET` 作業的這項實作會使用 `lifecycle` 查詢參數來檢查儲存區的生命週期設定。如果沒有生命週期配置，則會傳回 HTTP `404` 回應。
{: http}

Cloud IAM 使用者必須具有 `Reader` 角色，才能移除儲存區中的生命週期原則。

「標準基礎架構使用者」必須具有儲存區的 `Read` 許可權，才能移除儲存區中的生命週期原則。

標頭                    | 類型   | 說明
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | 字串   | **必要**：有效負載的 base64 編碼 128 位元 MD5 雜湊，用來作為完整性檢查，以確保未在傳輸中變更有效負載。
{: http}

**語法**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="範例 5. 注意此語法範例中斜線及點的用法。" caption-side="bottom"}
{: codeblock}
{: http}

**標頭要求範例**
{: http}

```yaml
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: caption="範例 6. 用於建立物件生命週期配置的要求標頭範例。" caption-side="bottom"}
{: http}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

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

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。


```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').get_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
 

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Use the client to read the configuration
            BucketLifecycleConfiguration config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            
            System.out.println(config.toString());
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }
```
{: codeblock}
{: java}
{: caption="範例 2. 顯示如何檢驗生命週期配置的程式碼範例。" caption-side="bottom"}

### 刪除儲存區的生命週期配置，包括有效期限
{: #expiry-api-delete}

`DELETE` 作業的這項實作會使用 `lifecycle` 查詢參數來檢查儲存區的生命週期設定。將會刪除與儲存區相關聯的所有生命週期規則。對於新物件，不會再發生規則所定義的轉移。不過，刪除規則之前，對於已寫入儲存區的物件，將會維護現有轉移規則。「有效期限規則」將不再存在。如果沒有生命週期配置，則會傳回 HTTP `404` 回應。
{: http}

Cloud IAM 使用者必須具有 `Writer` 角色，才能移除儲存區中的生命週期原則。

「標準基礎架構使用者」必須具有儲存區的 `Owner` 許可權，才能移除儲存區中的生命週期原則。

**語法**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="範例 7. 注意此語法範例中斜線及點的用法。" caption-side="bottom"}
{: codeblock}
{: http}

**標頭要求範例**
{: http}

```yaml
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="範例 8. 用於建立物件生命週期配置的要求標頭範例。" caption-side="bottom"}
{: http}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
      Bucket: 'STRING_VALUE' /* required */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
 

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').delete_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

使用 {{site.data.keyword.cos_full}} SDK 只需要呼叫具有正確參數及適當配置的適當函數。
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
    
    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Delete the configuration.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);
            
            // Verify that the configuration has been deleted by attempting to retrieve it.
            config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            String s = (config == null) ? "Configuration has been deleted." : "Configuration still exists.";
            System.out.println(s);
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }

```
{: codeblock}
{: java}
{: caption="範例 3. 顯示如何刪除生命週期配置的程式碼範例。" caption-side="bottom"}

## 後續步驟
{: #expiry-next-steps}

有效期限只是 {{site.data.keyword.cos_full_notm}} 可用的數個生命週期概念之一。您可以在 [{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/) 進一步探索此概觀中涵蓋的每個概念。

