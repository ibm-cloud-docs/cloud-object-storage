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

# 使用存储类
{: #classes}

并非所有数据都会传送到活动工作负载。归档数据可能很长时间都不会使用。对于活动较少的工作负载，可以创建使用不同存储类的存储区。对于这些存储区中存储的对象，收费安排与标准存储器不同。

## 存储类有哪些？
{: #classes-about}

有四种存储类：

*  **标准**：用于活动工作负载 - 检索数据免费（除了操作请求本身和公共出站带宽的成本外）。
*  **保险库**：用于数据访问不频繁的凉工作负载 - 读取数据会产生检索费用。该服务包括符合服务预期用途的对象大小和存储时间段的阈值：存储不太活跃且活动性较低的数据。
*  **冷保险库**：用于主要归档数据的冷工作负载（访问频率为每 90 天一次或更低），读取数据会产生更多的检索费用。该服务包括符合服务预期用途的对象大小和存储时间段的阈值：存储不活跃的非活动数据。
*  **Flex**：用于访问模式更难以预测的动态工作负载。根据使用情况，如果不太活跃的存储器的较低成本加上检索费用超过上限值，那么存储器费用会增加，但不会产生任何检索费用。如果对数据的访问不频繁，那么 Flex 存储器的性价比可能高于“标准”存储器，并且如果不太活跃的使用模式变得更活跃，那么 Flex 存储器的性价比要高于“保险库”或“冷保险库”存储器。Flex 存储区不会应用任何阈值对象大小或存储时间段。

有关定价详细信息，请参阅 [ibm.com 上的定价表](https://www.ibm.com/cloud/object-storage#s3api)。

有关如何创建使用不同存储类的存储区的信息，请参阅 [API 参考](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)。

## 如何创建使用不同存储类的存储区？
{: #classes-locationconstraint}

在控制台中创建存储区时，有一个下拉菜单，支持选择存储类。 

以编程方式创建存储区时，必须指定与使用的端点相对应的 `LocationConstraint`。 `LocationConstraint` 的有效供应代码为：<br>
&emsp;&emsp;  **美国地理位置：**`us-standard` / `us-vault` / `us-cold` / `us-flex`<br>
&emsp;&emsp;  **美国东部：**`us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex`<br>
&emsp;&emsp;  **美国南部：**`us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  **欧盟地理位置：**`eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  **欧盟 - 英国：**`eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex`<br>
&emsp;&emsp;  **欧盟 - 德国：**`eu-de-standard` / `eu-de-vault` / `eu-de-cold` / `eu-de-flex`<br>
&emsp;&emsp;  **亚太地区地理位置：**`ap-standard` / `ap-vault` / `ap-cold` / `ap-flex`<br>
&emsp;&emsp;  **亚太地区 - 日本：**`jp-tok-standard` / `jp-tok-vault` / `jp-tok-cold` / `jp-tok-flex`<br>
&emsp;&emsp;  **亚太地区 - 澳大利亚：**`au-syd-standard` / `au-syd-vault` / `au-syd-cold` / `au-syd-flex`<br>
&emsp;&emsp;  **阿姆斯特丹：**`ams03-standard` / `ams03-vault` / `ams03-cold` / `ams03-flex`<br>
&emsp;&emsp;  **金奈：**`che01-standard` / `che01-vault` / `che01-cold` / `che01-flex`<br>
&emsp;&emsp;  **中国香港特别行政区：**`hkg02-standard` / `hkg02-vault` / `hkg02-cold` / `hkg02-flex`<br>
&emsp;&emsp;  **墨尔本：**`mel01-standard` / `mel01-vault` / `mel01-cold`/ `mel01-flex`<br>
&emsp;&emsp;  **墨西哥城：**`mex01-standard` / `mex01-vault` / `mex01-cold` / `mex01-flex`<br>
&emsp;&emsp;  **米兰：**`mil01-standard` / `mil01-vault` / `mil01-cold` / `mil01-flex`<br>
&emsp;&emsp;  **蒙特利尔：**`mon01-standard` / `mon01-vault` / `mon01-cold` / `mon01-flex`<br>
&emsp;&emsp;  **奥斯陆：**`osl01-standard` / `osl01-vault` / `osl01-cold` / `osl01-flex`<br>
&emsp;&emsp;  **圣何塞：**`sjc04-standard` / `sjc04-vault` / `sjc04-cold` / `sjc04-flex`<br>
&emsp;&emsp;  **圣保罗：**`sao01-standard` / `sao01-vault` / `sao01-cold`/ `sao01-flex`<br>
&emsp;&emsp;  **首尔：**`seo01-standard` / `seo01-vault` / `seo01-cold` / `seo01-flex`<br>
&emsp;&emsp;  **多伦多：**`tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex`<br>


有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

## 使用 REST API、库和 SDK
{: #classes-sdk}

IBM COS SDK 中引入了多个新的 API，用于支持应用程序使用保留时间策略。在此页面顶部选择语言（curl、Java、Javascript、Go 或 Python），以查看使用相应 COS SDK 的示例。 

请注意，所有代码示例都假定存在可调用不同方法的名为 `cos` 的客户机对象。有关创建客户机的详细信息，请参阅特定的 SDK 指南。


### 创建使用某个存储类的存储区

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

创建存储区后，即无法更改存储区的存储类。如果需要重新分类对象，必须将数据移至使用所需存储类的其他存储区。 
