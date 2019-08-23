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

# 불변 오브젝트 스토리지 사용
{: #immutable}

불변 오브젝트 스토리지를 사용하면 보존 기간이 끝나고 법적 보존이 제거될 때까지 클라이언트가 전자 레코드를 보존하고 WORM(Write-Once-Read-Many), 삭제 불가능, 재작성 불가능 방식으로 데이터 무결성을 유지할 수 있습니다. 이 기능은 다음 산업의 조직 외에도 해당 환경에서 장기 데이터 보존에 필요한 클라이언트가 사용할 수 있습니다.

 * 금융
 * 헬스케어
 * 미디어 컨텐츠 아카이브
 * 오브젝트 또는 문서에 대한 권한 있는 수정 또는 삭제를 방지하려는 경우

기본 기능은 증권 중개인 트랜잭션과 같은 금융 레코드 관리를 처리하는 조직에서도 사용 가능하며, 재작성이 불가능하고 삭제 불가능한 형식으로 오브젝트를 보존해야 합니다.

불변 오브젝트 스토리지는 특정 지역에서만 사용 가능합니다. 세부사항은 [통합 서비스](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)를 참조하십시오. 또한 표준 가격 플랜도 필요합니다. 세부사항은 [가격](https://www.ibm.com/cloud/object-storage)을 참조하십시오.
{:note}

보존 정책이 있는 버킷에서 Aspera 고속 전송을 사용할 수 없습니다.
{:important}

## 용어 및 사용법
{: #immutable-terminology}

### 보존 기간
{: #immutable-terminology-period}

오브젝트가 COS 버킷에 저장되어 있어야 하는 지속 시간입니다.

### 보존 정책
{: #immutable-terminology-policy}

보존 정책은 COS 버킷 레벨에서 사용할 수 있습니다. 최소, 최대 및 기본 보존 기간은 이 정책으로 정의되며 버킷의 모든 오브젝트에 적용됩니다.

최소 보존 기간은 오브젝트가 버킷에 보존되어야 하는 최소 지속 시간입니다.

최대 보존 기간은 오브젝트가 버킷에 보존될 수 있는 최대 지속 시간입니다.

사용자 정의 보존 기간을 지정하지 않고 오브젝트를 버킷에 저장하는 경우 기본 보존 기간이 사용됩니다. 최소 보존 기간은 기본 보존 기간보다 짧거나 같아야 하며, 최대 보존 기간보다 짧거나 같아야 합니다.

참고: 오브젝트에 최대 1000년의 최대 보존 기간을 지정할 수 있습니다.

참고: 버킷에 보존 정책을 작성하려면 관리자 역할이 필요합니다. 세부사항은 [버킷 권한](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions)을 참조하십시오.

### 법적 보존 
{: #immutable-terminology-hold}

특정 레코드(오브젝트)는 보존 기간이 만료된 후에도 삭제되지 않도록 할 수 있습니다(예: 완료가 보류된 법적 검토에 원본 오브젝트에 설정된 보존 기간이 지나 연장된 지속 기간 동안 레코드에 대한 액세스가 필요할 수 있음). 이와 같은 시나리오에서 법적 보존 플래그를 오브젝트 레벨에 적용할 수 있습니다. 
 
법적 보존을 COS 버킷에 대한 초기 업로드 동안 또는 오브젝트가 추가된 후에 오브젝트에 적용할 수 있습니다.

참고: 오브젝트마다 최대 100개의 법적 보존을 적용할 수 있습니다.

### 무기한 보존
{: #immutable-terminology-indefinite}

사용자는 새 보존 기간이 적용될 때까지 오브젝트가 무기한 저장되도록 설정할 수 있습니다. 이는 오브젝트별 레벨에서 설정됩니다.

### 이벤트 기반 보존
{: #immutable-terminology-events}

불변 오브젝트 스토리지를 사용하면 사용자가 유스 케이스에 대한 보존 기간의 최종 지속 기간에 대해 확신이 없거나 이벤트 기반 보존 기능을 사용하려는 경우 오브젝트에 대한 무기한 보존을 설정할 수 있습니다. 무기한으로 설정되면 사용자 애플리케이션이 나중에 오브젝트 보존을 한정된 값으로 변경할 수 있습니다. 예를 들어, 회사에 직원이 퇴사한 후 3년 동안 직원 레코드를 보존하는 정책이 있습니다. 직원이 회사에 들어오면 이 직원과 연관된 레코드가 무기한 보존될 수 있습니다. 직원이 퇴사하면 무기한 보존이 회사 정책에 정의된 대로 현재 시간부터 3년이라는 한정된 값으로 변환됩니다. 그러면 보존 기간 변경 후 오브젝트는 3년 동안 보호됩니다. 사용자 또는 서드파티 애플리케이션이 SDK 또는 REST API를 사용하여 무기한에서 한정된 보존으로 보존 기간을 변경할 수 있습니다.

### 영구 보존
{: #immutable-terminology-permanent}

영구 보존은 보존 정책이 사용되는 COS 버킷 레벨에서만 사용 가능하며 사용자는 오브젝트 업로드 중에 영구 보존 기간 옵션을 선택할 수 있습니다. 사용으로 설정되면, 이 프로세스는 되돌릴 수 없으며 영구 보존 기간을 사용하여 업로드된 오브젝트는 **삭제할 수 없습니다**. 보존 정책이 있는 COS 버킷을 사용하여 오브젝트를 **영구적으로** 저장할 적법한 요구가 있는 경우 사용자 쪽에서 유효성을 검증해야 합니다.


불변 오브젝트 스토리지를 사용하는 경우, 데이터에 보존 정책이 적용되는 한 IBM Cloud 계정이 IBM Cloud 정책 및 가이드라인에 따라 우수한 상태로 유지되도록 해야 합니다. 자세한 정보는 IBM Cloud 서비스 이용 약관을 참조하십시오.
{:important}

## 다양한 규정에 대한 불변 오브젝트 스토리지 및 고려사항
{: #immutable-regulation}

불변 오브젝트 스토리지를 사용하는 경우, 클라이언트는 논의된 기능을 활용하여 일반적으로 다음을 통해 통제되는 전자 레코드 스토리지 및 보존에 대해 주요 규칙을 충족하고 준수할 수 있는지 여부를 확인해야 합니다.

  * [Securities and Exchange Commission (SEC) Rule 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64),
  * [Financial Industry Regulatory Authority (FINRA) Rule 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957),
  * [Commodity Futures Trading Commission (CFTC) Rule 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

클라이언트가 정보에 입각한 결정을 내리도록 하기 위해 IBM은 Cohasset Associates Inc.와 함께 IBM의 불변 오브젝트 스토리지 기능에 대한 독립적인 평가를 수행했습니다. IBM Cloud Object Storage의 불변 오브젝트 스토리지 기능 평가에 대한 세부사항을 제공하는 Cohasset Associates Inc.의 [보고서](https://www.ibm.com/downloads/cas/JBDNP0KV)를 검토하십시오.

### 액세스 및 트랜잭션의 감사
{: #immutable-audit}
보존 매개변수, 오브젝트 보존 기간 및 법적 보존의 애플리케이션에 대한 변경사항을 검토하기 위한 불변 오브젝트 저장소의 로그 데이터 액세스는 고객 서비스 티켓을 열어 케이스별로 사용 가능합니다.

## 콘솔 사용
{: #immutable-console}

보존 정책은 새로운 빈 버킷 또는 기존 빈 버킷에 추가할 수 있으며 제거할 수 없습니다. 새 버킷의 경우 [지원되는 지역](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability)에서 버킷을 작성하고 있는지 확인한 다음 **보존 정책 추가** 옵션을 선택하십시오. 기존 버킷의 경우 오브젝트가 없는지 확인한 다음 구성 설정으로 이동하고 버킷 보존 정책 섹션 아래에 있는 **정책 작성** 단추를 클릭하십시오. 어느 경우에나 최소, 최대 및 기본 보존 기간을 설정하십시오.

## REST API, 라이브러리 및 SDK 사용
{: #immutable-sdk}

여러 새 API가 IBM COS SDK에 도입되어 보존 정책에 대한 작업을 수행하는 애플리케이션에 대한 지원을 제공합니다. 해당 COS SDK를 사용하는 예를 보려면 이 페이지의 맨 위에서 언어(HTTP, Java, Javascript 또는 Python)를 선택하십시오. 

모든 코드 예에서는 서로 다른 메소드를 호출할 수 있는 `cos`라는 클라이언트 오브젝트의 존재를 가정합니다. 클라이언트 작성에 대한 세부사항은 구체적 SDK 안내서를 참조하십시오.

보존 기간 설정에 사용되는 모든 날짜 값은 GMT입니다. `Content-MD5` 헤더는 데이터 무결성을 보장하는 데 필요하며 SDK 사용 시 자동으로 전송됩니다.
{:note}

### 기존 버킷에 보존 정책 추가
{: #immutable-sdk-add-policy}
이 `PUT` 오퍼레이션 구현은 `protection` 조회 매개변수를 사용하여 기존 버킷의 보존 매개변수를 설정합니다. 이 오퍼레이션을 사용하면 최소, 기본 및 최대 보존 기간을 설정하거나 변경할 수 있습니다. 이 오퍼레이션을 사용하면 버킷의 보호 상태를 변경할 수도 있습니다.  

보호된 버킷에 작성된 오브젝트는 보호 기간이 만료되어 오브젝트에 대한 모든 법적 보존이 제거될 때까지 삭제할 수 없습니다. 오브젝트가 작성될 때 오브젝트 고유 값이 제공되지 않으면 버킷의 기본 보존 값이 오브젝트에 지정됩니다. 보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다. 

보존 기간 설정 `MinimumRetention`, `DefaultRetention` 및 `MaximumRetention`에 대해 지원되는 최소 및 최대값은 각각 0일과 365243일(1000년)입니다.  

`Content-MD5` 헤더가 필요합니다. 이 오퍼레이션은 추가 조회 매개변수를 사용하지 않습니다.

엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.
{:tip}

{: http}

**구문**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**요청 예**
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

**응답 예**
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

### 버킷의 보존 정책 확인
{: #immutable-sdk-get}

이 GET 오퍼레이션 구현은 기존 버킷에 대한 보존 매개변수를 페치합니다.
{: http}

**구문**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**요청 예**
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

버킷에 보호 구성이 없는 경우 대신 서버가 사용 안함 상태로 응답합니다.
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

### 보존 정책이 있는 버킷에 오브젝트 업로드
{: #immutable-sdk-upload}

이 `PUT` 오퍼레이션의 개선사항에서는 세 개의 새 요청 헤더를 추가합니다. 두 개는 서로 다른 방식의 보존 기간 지정을 위한 헤더이고 하나는 새 오브젝트에 단일 법적 보존을 추가하기 위한 헤더입니다. 새 오류는 새 헤더의 잘못된 값에 대해 정의되며 오브젝트가 보존되는 경우 겹쳐쓰기에 실패합니다.
{: http}

더 이상 보존되지 않는(보존 기간이 만료되었고 오브젝트에 법적 보존이 없음) 보존 정책을 사용하는 버킷의 오브젝트는 겹쳐쓸 때 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다.

`Content-MD5` 헤더가 필요합니다.
{: http}

이러한 헤더는 POST 오브젝트 및 다중 파트 업로드 요청에도 적용됩니다. 다중 파트로 오브젝트를 업로드하는 경우 각 파트에 `Content-MD5` 헤더가 필요합니다.
{: http}

|값  	| 유형|설명 |
| --- | --- | --- | 
|`Retention-Period` | 음수가 아닌 정수(초) | 오브젝트에 저장할 보존 기간(초)입니다. 오브젝트는 보존 기간에 지정된 시간이 경과하기 전까지 겹쳐쓰거나 삭제할 수 없습니다. 이 필드와 `Retention-Expiration-Date`가 모두 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 `DefaultRetention` 기간이 사용됩니다. 영(`0`)은 버킷의 최소 보존 기간도 `0`이라고 가정하는 적법한 값입니다. |
| `Retention-expiration-date` | 날짜(ISO 8601 형식) | 오브젝트를 적법하게 삭제하거나 수정할 수 있는 날짜입니다. 이 헤더 또는 Retention-Period 헤더만 지정할 수 있습니다. 둘 다 지정된 경우에는 `400` 오류가 리턴됩니다. 둘 다 지정되지 않은 경우에는 버킷의 DefaultRetention 기간이 사용됩니다. |
| `Retention-legal-hold-id` | 문자열 | 오브젝트에 적용할 단일 법적 보존입니다. 법적 보존은 Y자 길이의 문자열입니다. 오브젝트는 해당 오브젝트와 연관된 모든 법적 보존이 제거될 때까지 겹쳐쓰거나 삭제할 수 없습니다. |

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

### 오브젝트에서 법적 보존 추가 또는 제거
{: #immutable-sdk-legal-hold}

이 `POST` 오퍼레이션 구현은 `legalHold` 조회 매개변수 및 `add`와 `remove` 조회 매개변수를 사용하여 보호된 버킷의 보호된 오브젝트에서 단일 법적 보존을 추가하거나 제거합니다.
{: http}

오브젝트는 100개의 법적 보존을 지원할 수 있습니다.

*  법적 보존 ID는 최대 64자이고 최소 1자인 문자열입니다. 유효한 문자는 문자, 숫자, `!`, `_`, `.`, `*`, `(`, `)`, `-` 및 `입니다.
* 특정 법적 보존을 추가했을 때 오브젝트의 총 100개 법적 보존 한계를 초과하는 경우에는 해당 새 법적 보존이 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID가 너무 긴 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다. 
* ID에 올바르지 않은 문자가 포함되어 있는 경우에는 오브젝트에 추가되지 않으며 `400` 오류가 리턴됩니다.
* ID가 이미 오브젝트에서 사용 중인 경우에는 기존 법적 보존이 수정되지 않으며 해당 ID가 이미 사용 중임을 나타내는 응답이 `409` 오류와 함께 표시됩니다. 
* 오브젝트에 보존 기간 메타데이터가 없는 경우에는 `400` 오류가 리턴되며 법적 보존 추가 또는 제거가 허용되지 않습니다. 

보존 기간 헤더는 반드시 있어야 하며, 그렇지 않은 경우에는 `400` 오류가 리턴됩니다.
{: http}

법적 보존을 추가하거나 제거하는 사용자에게는 해당 버킷에 대한 `Manager` 권한이 있어야 합니다. 

`Content-MD5` 헤더가 필요합니다. 이 오퍼레이션은 오퍼레이션 특정 페이로드 요소를 사용하지 않습니다.
{: http}

**구문**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**요청 예**
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

**응답 예**
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

### 오브젝트의 보존 기간 연장
{: #immutable-sdk-extend}

이 `POST` 오퍼레이션 구현은 `extendRetention` 조회 매개변수를 사용하여 보호된 버킷에서 보호된 오브젝트의 보존 기간을 연장합니다.
{: http}

오브젝트의 보존 기간은 연장만 가능합니다. 현재 구성된 값에서 줄일 수는 없습니다. 

보존 연장 값은 다음 세 가지 방법 중 하나로 설정됩니다.

* 현재 값에서의 추가 시간(`Additional-Retention-Period` 또는 이와 유사한 메소드)
* 초 단위의 새 연장 기간(`Extend-Retention-From-Current-Time` 또는 이와 유사한 메소드)
* 오브젝트의 새 보존 만료 날짜(`New-Retention-Expiration-Date` 또는 이와 유사한 메소드)

오브젝트 메타데이터에 저장된 현재 보존 기간은 `extendRetention` 요청에 설정된 매개변수에 따라 지정된 추가 시간만큼 증가되거나, 새 값으로 대체될 수 있습니다. 모든 경우에 보존 연장 매개변수는 현재 보존 기간에 대해 확인되며, 연장된 매개변수는 업데이트된 보존 기간이 현재 보존 기간보다 긴 경우에만 허용됩니다. 

보호된 버킷에서 더 이상 보존되지 않는 오브젝트(보존 기간이 만료되어 오브젝트에 대한 법적 보존이 없음)를 겹쳐쓰면 다시 보존 상태가 됩니다. 새 보존 기간은 오브젝트 겹쳐쓰기 요청의 일부로서 제공될 수 있으며, 그렇지 않은 경우에는 버킷의 기본 보존 기간이 오브젝트에 지정됩니다.

**구문**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**요청 예**
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

**응답 예**
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

### 오브젝트의 법적 보존 나열
{: #immutable-sdk-list-holds}

이 `GET` 오퍼레이션 구현은 `legalHold` 조회 매개변수를 사용하여 XML 응답 본문에 오브젝트의 법적 보존 목록 및 관련 보존 상태를 리턴합니다.
{: http}

이 오퍼레이션은 다음 항목을 리턴합니다. 

* 오브젝트 작성 날짜
* 오브젝트 보존 기간(초)
* 보존 기간 및 작성 날짜를 기반으로 계산된 보존 만료 날짜
* 법적 보존의 목록
* 법적 보존 ID
* 법적 보존이 적용된 시점의 시간소인

오브젝트에 대한 법적 보존이 없는 경우에는 비어 있는 `LegalHoldSet`이 리턴됩니다.
오브젝트에 대해 지정된 보존 기간이 없는 경우에는 `404` 오류가 리턴됩니다. 

**구문**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**요청 예**
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

**응답 예**
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
