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

# 만기 규칙을 사용하여 시간이 경과된(stale) 데이터 삭제
{: #expiry}

만기 규칙은 정의된 기간(오브젝트 작성 날짜부터 시작하여)이 지나면 오브젝트를 삭제합니다. 

웹 콘솔, REST API 및 {{site.data.keyword.cos_full_notm}}와 통합된 서드파티 도구를 사용하여 오브젝트의 라이프사이클을 설정할 수 있습니다. 

* 새 버킷 또는 기존 버킷에 만기 규칙을 추가할 수 있습니다.
* 기존 만기 규칙을 수정하거나 사용 안함으로 설정할 수 있습니다.
* 새로 추가되거나 수정된 만기 규칙은 버킷의 모든 새 오브젝트 및 기존 오브젝트에 적용됩니다.
* 라이프사이클 정책을 추가하거나 수정하려면 `작성자` 역할이 필요합니다. 
* 버킷당 최대 1000개의 라이프사이클 규칙(아카이브+만기)을 정의할 수 있습니다.
* 만기 규칙의 변경사항이 적용될 때까지 최대 24시간이 허용됩니다.
* 각 만기 규칙의 범위는 이름이 접두부와 일치하는 오브젝트의 서브세트에만 적용하도록 선택적 접두부 필터를 정의하여 제한할 수 있습니다.
* 접두부 필터가 없는 만기 규칙은 버킷의 모든 오브젝트에 적용됩니다.
* 일 수로 지정된 오브젝트의 만기 기간은 오브젝트가 작성된 시간부터 계산되며 다음 날 자정(UTC)으로 반올림됩니다. 예를 들어, 작성 날짜로부터 10일 후에 오브젝트 세트가 만료되는 버킷의 만기 규칙이 있는 경우 2019년 4월 15일 5시 10분(UTC)에 작성된 오브젝트는 2019년 4월 26일 자정(UTC)에 만료됩니다. 
* 각 버킷의 만기 규칙은 24시간마다 한 번씩 평가됩니다. 만기가 되는 오브젝트(오브젝트의 만기 날짜 기준)는 삭제를 위해 큐에 대기합니다. 만료된 오브젝트 삭제는 다음 날 시작되며 대개 24시간 미만이 걸립니다. 오브젝트가 삭제되면 오브젝트에 연관된 스토리지에 대해 비용이 청구되지 않습니다.

버킷의 불변 오브젝트 스토리지 보존 정책이 적용되는 오브젝트에서는 보존 정책이 더 이상 시행되지 않을 때까지 만기 조치가 지연됩니다.
{: important}

## 만기 규칙의 속성
{: #expiry-rules-attributes}

각 만기 규칙에는 다음 속성이 있습니다.

### ID
규칙의 ID는 버킷의 라이프사이클 구성 내에서 고유해야 합니다.

### 만료
만기 블록에는 오브젝트의 자동 삭제를 통제하는 세부사항이 포함됩니다. 이는 미래의 특정 날짜이거나 새 오브젝트가 작성된 후의 기간일 수 있습니다.

### 접두부
버킷에 있는 오브젝트 이름의 접두부와 일치할 선택적 문자열입니다. 접두부가 있는 규칙은 일치하는 오브젝트에만 적용됩니다. 동일한 버킷 내 서로 다른 접두부에 대한 다양한 만기 조치에 여러 규칙을 사용할 수 있습니다. 예를 들어, 동일한 라이프사이클 구성 내에서 하나의 규칙은 30일 후에 `logs/`로 시작하는 모든 오브젝트를 삭제할 수 있으며 두 번째 규칙은 365일 후에 `video/`로 시작하는 오브젝트를 삭제할 수 있습니다.  

### 상태
규칙은 사용 또는 사용 안함으로 설정할 수 있습니다. 규칙은 사용으로 설정된 경우에만 활성입니다.

## 샘플 라이프사이클 구성

이 구성에서는 30일이 지난 후 새 오브젝트가 만료됩니다.

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

이 구성은 2020년 6월 1일에 접두부가 `foo/`인 모든 오브젝트를 삭제합니다.

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

또한 전이와 만기 규칙을 결합할 수 있습니다. 이 구성은 작성 후 90일 동안 오브젝트를 아카이브하고 180일 후에 접두부가 `foo/`인 모든 오브젝트를 삭제합니다.

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

## 콘솔 사용
{: #expiry-using-console}

새 버킷을 작성할 때 **만기 규칙 추가** 상자를 선택하십시오. 그런 다음 **규칙 추가** 클릭하여 새 만기 규칙을 작성하십시오. 버킷 작성 중에 최대 5개의 규칙을 추가할 수 있으며 나중에 규칙을 추가할 수 있습니다.

기존 버킷에 대해 탐색 메뉴에서 **구성**을 선택하고 _만기 규칙_ 섹션에서 **규칙 추가**를 클릭하십시오.

## API 및 SDK 사용
{: #expiry-using-api-sdks}

REST API 또는 IBM COS SDK를 사용하여 프로그래밍 방식으로 만기 규칙을 관리할 수 있습니다. 컨텍스트 교환기에서 카테고리를 선택하여 예제의 형식을 선택하십시오.

### 버킷의 라이프사이클 구성에 만기 규칙 추가
{: #expiry-api-create}

**REST API 참조**
{: http}

이 `PUT` 오퍼레이션 구현은 `lifecycle` 조회 매개변수를 사용하여 버킷의 라이프사이클 설정을 지정합니다. 이 오퍼레이션은 버킷에 대한 단일 라이프사이클 정책 정의를 허용합니다. 정책은 다음 매개변수로 구성된 규칙 세트로 정의됩니다. `ID`, `Status`, `Filter` 및 `Expiration`.
{: http}
 
Cloud IAM 사용자에게 버킷에서 라이프사이클 정책을 제거할 수 있는 `작성자` 역할이 있어야 합니다.

클래식 인프라 사용자에게 버킷에서 라이프사이클 정책을 제거할 수 있는 버킷에 대한 `소유자` 권한이 있어야 합니다.

헤더                      |유형   |설명
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | 문자열 | **필수**: 페이로드의 base64 인코딩 128비트 MD5 해시이며, 페이로드가 전송 중에 변경되지 않았는지 확인하기 위한 무결성 검사로 사용됩니다.
{: http}

요청 본문에 다음 스키마가 포함된 XML 블록이 있어야 합니다.
{: http}

| 요소                     |유형                 | 하위                                   | 상위                     | 제한조건                                                                                   |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | 컨테이너             | `Rule`                                 |없음                     | 한계 1.                                                                                  |
| `Rule`                   | 컨테이너             | `ID`, `Status`, `Filter`, `Expiration` | `LifecycleConfiguration` | 한계 1000.                                                                                  |
| `ID`                     | 문자열               |없음                                   | `Rule`                   | (`a-z,`A-Z0-9`) 및 다음 기호로 구성되어야 합니다. `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | 문자열               | `Prefix`                               | `Rule`                   | `Prefix` 요소가 포함되어야 합니다.                                                            |
| `Prefix`                 | 문자열               |없음                                   | `Filter`                 | 규칙은 이 접두부와 일치하는 키가 있는 모든 오브젝트에 적용됩니다.                                                           |
| `Expiration`             | `컨테이너`          | `Days` 또는 `Date`                       | `Rule`                   | 한계 1.                                                                                  |
| `Days`                   | 음수가 아닌 정수     |없음                                   | `Expiration`             | 0보다 큰 값이어야 합니다.                                                           |
| `Date`                   | 날짜                 |없음                                   | `Expiration`             | ISO 8601 형식이어야 합니다.                            |
{: http}

요청의 본문에는 테이블에서 다루는 스키마가 포함된 XML 블록이 있어야 합니다(예 1 참조).
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
{: caption="예 1. 요청 본문의 XML 샘플입니다." caption-side="bottom"}
{: http}

**구문**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="예 2. 이 구문 예의 슬래시와 점 사용을 참고하십시오." caption-side="bottom"}
{: codeblock}
{: http}

**요청 예**
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
{: caption="예 3. 오브젝트 라이프사이클 구성 작성을 위한 요청 헤더 샘플입니다." caption-side="bottom"}
{: http}

**NodeJS COS SDK에서 사용하기 위한 코드 샘플**
{: javascript}

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다.
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

**Python COS SDK에서 사용하기 위한 코드 샘플**
{: python}

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다.
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

**Java COS SDK에서 사용하기 위한 코드 샘플**
{: java}

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다.
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
{: caption="예 1. 라이프사이클 구성 작성을 보여주는 코드 샘플입니다." caption-side="bottom"}

### 만기를 포함해 버킷의 라이프사이클 구성 검토
{: #expiry-api-view}

이 `GET` 오퍼레이션 구현은 `lifecycle` 조회 매개변수를 사용하여 버킷의 라이프사이클 설정을 검토합니다. 라이프사이클 구성이 없는 경우 HTTP `404` 응답이 리턴됩니다.
{: http}

Cloud IAM 사용자에게 버킷에서 라이프사이클 정책을 제거할 수 있는 `독자` 역할이 있어야 합니다.

클래식 인프라 사용자에게 버킷에서 라이프사이클 정책을 제거할 수 있는 버킷에 대한 `독자` 권한이 있어야 합니다.

헤더                      |유형   |설명
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | 문자열 | **필수**: 페이로드의 base64 인코딩 128비트 MD5 해시이며, 페이로드가 전송 중에 변경되지 않았는지 확인하기 위한 무결성 검사로 사용됩니다.
{: http}

**구문**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="예 5. 이 구문 예의 슬래시와 점 사용을 참고하십시오." caption-side="bottom"}
{: codeblock}
{: http}

**헤더 요청 예**
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
{: caption="예 6. 오브젝트 라이프사이클 구성 작성을 위한 요청 헤더 샘플입니다." caption-side="bottom"}
{: http}

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다.
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

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다.

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

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다. 

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
{: caption="예 2. 라이프사이클 구성 검사를 보여주는 코드 샘플입니다." caption-side="bottom"}

### 만기를 포함해 버킷의 라이프사이클 구성 삭제
{: #expiry-api-delete}

이 `DELETE` 오퍼레이션 구현은 `lifecycle` 조회 매개변수를 사용하여 버킷의 라이프사이클 설정을 검토합니다. 버킷과 연관된 모든 라이프사이클 규칙이 삭제됩니다. 규칙으로 정의된 전이는 더 이상 새 오브젝트에 대해 발생하지 않습니다. 하지만 기존 전이 규칙은 규칙이 삭제되기 전에 버킷에 이미 작성된 오브젝트에 대해 유지됩니다. 만기 규칙이 더 이상 존재하지 않습니다. 라이프사이클 구성이 없는 경우 HTTP `404` 응답이 리턴됩니다.
{: http}

Cloud IAM 사용자에게 버킷에서 라이프사이클 정책을 제거할 수 있는 `작성자` 역할이 있어야 합니다.

클래식 인프라 사용자에게 버킷에서 라이프사이클 정책을 제거할 수 있는 버킷에 대한 `소유자` 권한이 있어야 합니다.

**구문**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="예 7. 이 구문 예의 슬래시와 점 사용을 참고하십시오." caption-side="bottom"}
{: codeblock}
{: http}

**헤더 요청 예**
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
{: caption="예 8. 오브젝트 라이프사이클 구성 작성을 위한 요청 헤더 샘플입니다." caption-side="bottom"}
{: http}

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다.
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

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다. 

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

{{site.data.keyword.cos_full}} SDK를 사용하기 위해서는 올바른 매개변수 및 적절한 구성과 함께 적합한 함수를 호출해야 합니다.
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
{: caption="예 3. 라이프사이클 구성 삭제를 보여주는 코드 샘플입니다." caption-side="bottom"}

## 다음 단계
{: #expiry-next-steps}

만기는 {{site.data.keyword.cos_full_notm}}에 사용 가능한 많은 라이프사이클 개념 중 하나입니다.
이 개요에서 다룬 각 개념은 [{{site.data.keyword.cloud_notm}} 플랫폼](https://cloud.ibm.com/)에서
자세히 볼 수 있습니다.

