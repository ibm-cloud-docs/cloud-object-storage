---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-14"

keywords: storage classes, tiers, cost, buckets, location constraint, provisioning code, locationconstraint

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
{:table: .aria-labeledby="caption"}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}
{:curl: .ph data-hd-programlang='curl'}

# 스토리지 클래스 사용
{: #classes}

일부 데이터가 활성 워크로드를 피드하지 않습니다. 아카이브 데이터가 오랫 동안 처리되지 않을 수 있습니다. 활성 워크로드가 적으면 서로 다른 스토리지 클래스로 버킷을 작성할 수 있습니다. 이러한 버킷에 저장된 오브젝트는 표준 스토리지와는 다른 스케줄에 따라 비용이 발생합니다.

## 클래스의 개념
{: #classes-about}

네 가지 스토리지 클래스가 있습니다.

*  **표준**: 활성 워크로드에 사용되며, 검색된 데이터에 대한 비용이 없습니다(오퍼레이션 요청 자체와 공용 아웃바운드 대역폭의 비용 제외).
*  **볼트**: 데이터가 자주 액세스되지 않는 쿨 워크로드에 사용되며, 데이터 읽기에 검색 비용이 적용됩니다. 서비스에는 이 서비스의 사용 목적, 즉, 덜 활성화되는 쿨러 데이터에 적합한 오브젝트 크기 및 스토리지 기간에 대한 임계값이 포함됩니다.
*  **콜드 볼트**: 데이터가 주로 아카이브되는 콜드 워크로드에 사용되며(90일 이하마다 액세스됨) 많은 검색 비용이 데이터 읽기에 적용됩니다. 서비스에는 이 서비스의 사용 목적, 즉, 비활성 콜드 데이터의 저장에 적합한 오브젝트 크기 및 스토리지 기간에 대한 임계값이 포함됩니다.
*  **Flex**: 액세스 패턴 예측이 어려운 동적 워크로드에 사용됩니다. 사용량에 따라 쿨러 스토리지의 적은 비용과 검색 비용이 결합된 값이 한계 값을 초과하는 경우, 스토리지 비용이 증가하고 검색 비용은 적용되지 않습니다. 데이터가 자주 액세스되지 않는 경우 Flex 스토리지가 표준 스토리지보다 비용면에서 효율적일 수 있으며, 쿨러 사용 패턴이 더 활성화되는 경우 Flex 스토리지가 볼트 또는 콜드 볼트 스토리지보다 비용면에서 효율적입니다. 임계값 오브젝트 크기 또는 스토리지 기간이 Flex 버킷에 적용되지 않습니다.

가격 세부사항은 [ibm.com의 가격 테이블](https://www.ibm.com/cloud/object-storage#s3api)을 참조하십시오.

여러 스토리지 클래스로 버킷을 작성하는 방법에 대한 정보는 [API 참조](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)를 참조하십시오.

## 서로 다른 스토리지 클래스로 버킷을 작성하는 방법
{: #classes-locationconstraint}

콘솔에서 버킷을 작성할 때 스토리지 클래스 선택을 허용하는 드롭 다운 메뉴가 있습니다. 

프로그래밍 방식으로 버킷을 작성할 때 사용되는 엔드포인트에 해당하는 `LocationConstraint`를 지정해야 합니다. `LocationConstraint`에 대한 올바른 프로비저닝 코드는 다음과 같습니다. <br>
&emsp;&emsp;  **미국:** `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  **미국 동부:** `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  **미국 남부:** `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **유럽 연합:** `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **유럽 연합 영국:** `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  **유럽 연합 독일:** `eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex` <br>
&emsp;&emsp;  **아시아 태평양 지역:** `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  **아시아 태평양 일본:** `jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex` <br>
&emsp;&emsp;  **아시아 태평양 오스트레일리아:** `au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex` <br>
&emsp;&emsp;  **암스테르담:** `ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex` <br>
&emsp;&emsp;  **첸나이:** `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  **홍콩:** `hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex` <br>
&emsp;&emsp;  **멜버른:** `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  **멕시코:** `mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex` <br>
&emsp;&emsp;  **밀라노:** `mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex` <br>
&emsp;&emsp;  **몬트리올:** `mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex` <br>
&emsp;&emsp;  **오슬로:** `osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex` <br>
&emsp;&emsp;  **산호세:** `sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex` <br>
&emsp;&emsp;  **상파울루:** `sao01-standard` / `sao01-vault` / `sao01-cold` / `sao01-flex` <br>
&emsp;&emsp;  **서울:** `seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex` <br>
&emsp;&emsp;  **토론토:** `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>


엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.

## REST API, 라이브러리 및 SDK 사용
{: #classes-sdk}

여러 새 API가 IBM COS SDK에 도입되어 보존 정책에 대한 작업을 수행하는 애플리케이션에 대한 지원을 제공합니다. 해당 COS SDK를 사용하는 예를 보려면 이 페이지의 맨 위에서 언어(curl, Java, Javascript, Go 또는 Python)를 선택하십시오. 

모든 코드 예에서는 서로 다른 메소드를 호출할 수 있는 `cos`라는 클라이언트 오브젝트의 존재를 가정합니다. 클라이언트 작성에 대한 세부사항은 구체적 SDK 안내서를 참조하십시오.


### 스토리지 클래스로 버킷 작성

```java
public static void createBucket(String bucketName) {
    System.out.printf("Creating new bucket: %s\n", bucketName);
    _cos.createBucket(bucketName, "us-vault");
    System.out.printf("Bucket: %s created!\n", bucketName);
}
```
{: codeblock}
{: java}


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
{: codeblock}
{: javascript}


```py
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create bucket: {0}".format(e))
```
{: codeblock}
{: python}

```go
func main() {

    // Create client
    sess := session.Must(session.NewSession())
    client := s3.New(sess, conf)

    // Bucket Names
    newBucket := "<NEW_BUCKET_NAME>"

    input := &s3.CreateBucketInput{
        Bucket: aws.String(newBucket),
        CreateBucketConfiguration: &s3.CreateBucketConfiguration{
            LocationConstraint: aws.String("us-cold"),
        },
    }
    client.CreateBucket(input)

    d, _ := client.ListBuckets(&s3.ListBucketsInput{})
    fmt.Println(d)
}
```
{: codeblock}
{: go}


```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}
{: curl}

버킷이 작성되면 버킷의 스토리지 클래스를 변경할 수 없습니다. 오브젝트를 재분류해야 하는 경우 원하는 스토리지 클래스의 다른 버킷으로 데이터를 이동해야 합니다. 
