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

# 使用到期规则删除旧数据
{: #expiry}

到期规则用于在定义的时间段之后（从对象创建日期起算）删除对象。 

您可以使用与 {{site.data.keyword.cos_full_notm}} 集成的 Web 控制台、REST API 和第三方工具来设置对象的生命周期。 

* 可以将到期规则添加到新存储区或现有存储区。
* 可以修改或禁用现有到期规则。
* 新添加或修改的到期规则会应用于存储区中的所有新对象和现有对象。
* 添加或修改生命周期策略需要`写入者`角色。 
* 每个存储区最多可以定义 1000 个生命周期规则（归档 + 到期）。
* 容许最长 24 小时，以等待到期规则中的任何更改生效。
* 通过定义可选前缀过滤器可以限制每个到期规则的作用域，以仅应用于名称与前缀匹配的对象子集。
* 没有前缀过滤器的到期规则将应用于存储区中的所有对象。
* 对象的到期时间段（以天为单位）从对象创建时间开始计算，并四舍五入到下一天的午夜 UTC。例如，如果存储区的到期规则是一组对象在创建日期后 10 天到期，那么在 2019 年 4 月 15 日 05:10 UTC 创建的对象将于 2019 年 4 月 26 日 00:00 UTC 到期。 
* 每个存储区的到期规则会每 24 小时求值一次。符合到期条件的任何对象（根据对象的到期日期）都将排队等待删除。删除到期对象将在到期之日的第二天开始，并且通常需要的时间少于 24 小时。删除对象后，不会再针对这些对象的任何关联存储器向您收费。

受存储区的不可变对象存储器保留时间策略约束的对象将延迟任何到期操作，直到不再强制实施保留时间策略为止。
{: important}

## 到期规则的属性
{: #expiry-rules-attributes}

每个到期规则都具有以下属性：

### ID
规则的 ID 必须在存储区的生命周期配置中唯一。

### Expiration
Expiration 块包含用于管理对象自动删除的详细信息。这可能是未来的某个特定日期，或者是编写新对象之后的一段时间。

### Prefix
一个可选字符串，将与存储区中对象名称的前缀相匹配。具有前缀的规则将仅应用于与该前缀匹配的对象。对于同一存储区中不同前缀的不同到期操作，可以使用多个规则。例如，在同一生命周期配置中，一个规则可在 30 天后删除以 `logs/` 开头的所有对象，另一个规则可在 365 天后删除以 `video/` 开头的对象。  

### Status
可以启用或禁用规则。仅当启用时规则时，规则才处于活动状态。

## 样本生命周期配置

以下配置使任何新对象在 30 天后到期。

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

以下配置在 2020 年 6 月 1 日删除前缀为 `foo/` 的所有对象。

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

您还可以组合使用转换和到期规则。以下配置在任何对象创建 90 天后将其归档，并在 180 天后删除前缀为 `foo/` 的所有对象。

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

## 使用控制台
{: #expiry-using-console}

创建新存储区时，请选中**添加到期规则**框。接下来，单击**添加规则**以创建新的到期规则。在创建存储区期间，最多可以添加五个规则，日后可以添加额外的规则。

对于现有存储区，请从导航菜单中选择**配置**，然后单击_到期规则_部分下的**添加规则**。

## 使用 API 和 SDK
{: #expiry-using-api-sdks}

可以使用 REST API 或 IBM COS SDK 以编程方式管理到期规则。通过在上下文切换器中选择类别来选择格式用于示例。

### 向存储区的生命周期配置添加到期规则
{: #expiry-api-create}

**REST API 参考**
{: http}

此 `PUT` 操作实现使用 `lifecycle` 查询参数来设置存储区的生命周期设置。此操作支持对存储区定义单个生命周期策略。策略定义为包含以下参数的一组规则：`ID`、`Status`、`Filter` 和 `Expiration`。
{: http}
 
Cloud IAM 用户必须具有`写入者`角色，才能从存储区中除去生命周期策略。

经典基础架构用户必须具有对存储区的`所有者`许可权，才能从存储区中除去生命周期策略。

头|类型|描述
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5`|字符串|**必需**：有效内容的 Base64 编码的 128 位 MD5 散列，用作完整性检查，可确保有效内容在传输中未变更。
{: http}

请求主体必须包含具有以下模式的 XML 块：
{: http}

|元素|类型|子代|祖代|约束|
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
|`LifecycleConfiguration`|容器|`Rule`|无|限制为 1。|
|`Rule`|容器|`ID`、`Status`、`Filter`、`Expiration`|`LifecycleConfiguration`|限制为 1000。|
|`ID`|字符串|无|`Rule`|必须由（`a-z、`A-Z 和 0-9`）以及以下符号组成：`!` `_` `.` `*` `'` `(` `)` `-` |
|`Filter`|字符串|`Prefix`|`Rule`|必须包含 `Prefix` 元素|
|`Prefix`|字符串|无|`Filter`|规则会应用于其键与此前缀相匹配的任何对象。|
|`Expiration`|`容器`|`Days` 或 `Date`|`Rule`|限制为 1。|
|`Days`|非负整数|无|`Expiration`|必须为大于 0 的值。|
|`Date`|日期|无|`Expiration`|必须为 ISO 8601 格式。|
{: http}

请求主体必须包含具有表中所述模式的 XML 块（请参阅示例 1）：
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
{: caption="示例 1. 请求主体中的 XML 样本。" caption-side="bottom"}
{: http}

**语法**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="示例 2. 请注意，在此语法示例中使用了斜杠和点。" caption-side="bottom"}
{: codeblock}
{: http}

**示例请求**
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
{: caption="示例 3. 用于创建对象生命周期配置的请求头样本。" caption-side="bottom"}
{: http}

**用于 NodeJS COS SDK 的代码样本**
{: javascript}

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
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

**用于 Python COS SDK 的代码样本**
{: python}

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
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

**用于 Java COS SDK 的代码样本**
{: java}

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
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
{: caption="示例 1. 显示创建生命周期配置的代码样本。" caption-side="bottom"}

### 检查存储区的生命周期配置，包括到期时间
{: #expiry-api-view}

此 `GET` 操作实现使用 `lifecycle` 查询参数来检查存储区的生命周期设置。如果不存在生命周期配置，那么将返回 HTTP `404` 响应。
{: http}

Cloud IAM 用户必须具有`读取者`角色，才能从存储区中除去生命周期策略。

经典基础架构用户必须具有对存储区的`读`许可权，才能从存储区中除去生命周期策略。

头|类型|描述
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5`|字符串|**必需**：有效内容的 Base64 编码的 128 位 MD5 散列，用作完整性检查，可确保有效内容在传输中未变更。
{: http}

**语法**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="示例 5. 请注意，在此语法示例中使用了斜杠和点。" caption-side="bottom"}
{: codeblock}
{: http}

**示例头请求**
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
{: caption="示例 6. 用于创建对象生命周期配置的请求头样本。" caption-side="bottom"}
{: http}

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
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

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。


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

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
 

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
{: caption="示例 2. 显示检查生命周期配置的代码样本。" caption-side="bottom"}

### 删除存储区的生命周期配置，包括到期时间
{: #expiry-api-delete}

此 `DELETE` 操作实现使用 `lifecycle` 查询参数来删除存储区的生命周期设置。这将删除与存储区关联的所有生命周期规则。规则定义的转换不会再应用于新对象。但是，对于在删除规则之前已经写入存储区的对象，将保留现有的转换规则。到期规则将不再存在。如果不存在生命周期配置，那么将返回 HTTP `404` 响应。
{: http}

Cloud IAM 用户必须具有`写入者`角色，才能从存储区中除去生命周期策略。

经典基础架构用户必须具有对存储区的`所有者`许可权，才能从存储区中除去生命周期策略。

**语法**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="示例 7. 请注意，在此语法示例中使用了斜杠和点。" caption-side="bottom"}
{: codeblock}
{: http}

**示例头请求**
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
{: caption="示例 8. 用于创建对象生命周期配置的请求头样本。" caption-side="bottom"}
{: http}

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
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

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
 

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

使用 {{site.data.keyword.cos_full}} SDK 时，仅需要使用正确参数和正确配置调用相应函数即可。
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
{: caption="示例 3. 显示删除生命周期配置的代码样本。" caption-side="bottom"}

## 后续步骤
{: #expiry-next-steps}

到期时间只是可用于 {{site.data.keyword.cos_full_notm}} 的诸多生命周期概念中的一个概念。在此概述中涵盖的每个概念都可在 [{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/) 上做进一步的探索。

