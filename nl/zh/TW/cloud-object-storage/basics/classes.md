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

# 使用儲存空間類別
{: #classes}

並非所有資料都提供作用中工作負載。保存資料可能會長時間保持不變。若為較少的作用中工作負載，您可以使用不同的儲存空間類別來建立儲存區。這些儲存區中所儲存的物件會產生與標準儲存空間不同排程的費用。

## 何謂類別？
{: #classes-about}

有四個儲存空間類別：

*  **標準**：用於作用中工作負載 - 擷取資料是免費的（除了作業要求本身及公用出埠頻寬的成本之外）。
*  **儲存庫**：用於未頻繁存取資料的冷工作負載 - 讀取資料需要擷取費用。服務包括物件大小和儲存期限臨界值，其與此服務的預期使用一致：儲存較冷且活動量不大的資料。
*  **冷儲存庫**：用於主要保存資料（每 90 天或更少天數存取一次）的冷工作負載 - 讀取資料需要較大的擷取費用。服務包括物件大小和儲存期限臨界值，其與此服務的預期使用一致：儲存非作用中的冷資料。
*  **Flex**：用於更難預測存取型樣的動態工作負載。取決於使用情形，如果與擷取費用結合的較冷儲存空間的較低成本超出上限值，則儲存空間費用會增加，而且不會套用任何擷取費用。如果未頻繁存取資料，則 Flex 儲存空間會比「標準」儲存空間更具成本效益，而且，如果較冷的使用型樣變成更為活躍，則 Flex 儲存空間會比「儲存庫」或「冷儲存庫」儲存空間更具成本效益。無臨界值物件大小或儲存期限會套用至 Flex 儲存區。

如需定價詳細資料，請參閱 [ibm.com 上的定價表](https://www.ibm.com/cloud/object-storage#s3api)。

如需如何建立具有不同儲存空間類別的儲存區的相關資訊，請參閱 [API 參考資料](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)。

## 如何建立具有不同儲存空間類別的儲存區？
{: #classes-locationconstraint}

在主控台中建立儲存區時，會有一個下拉功能表容許選取儲存空間類別。 

以程式設計方式建立儲存區時，必須指定對應於所使用端點的 `LocationConstraint`。`LocationConstraint` 的有效佈建程式碼如下：<br>
&emsp;&emsp;  **美國地區：**`us-standard`/`us-vault`/`us-cold`/`us-flex` <br>
&emsp;&emsp;  **美國東部：**`us-east-standard`/`us-east-vault`/`us-east-cold`/`us-east-flex` <br>
&emsp;&emsp;  **美國南部：**`us-south-standard`/`us-south-vault`/`us-south-cold`/`us-south-flex` <br>
&emsp;&emsp;  **歐盟地區：**`eu-standard`/`eu-vault`/`eu-cold`/`eu-flex` <br>
&emsp;&emsp;  **歐盟大不列顛：**`eu-gb-standard`/`eu-gb-vault`/`eu-gb-cold`/`eu-gb-flex` <br>
&emsp;&emsp;  **歐盟德國：**`eu-de-standard`/`eu-de-vault`/`eu-de-cold`/`eu-de-flex` <br>
&emsp;&emsp;  **亞太地區：**`ap-standard`/`ap-vault`/`ap-cold`/`ap-flex` <br>
&emsp;&emsp;  **亞太日本：**`jp-tok-standard`/`jp-tok-vault`/`jp-tok-cold`/`jp-tok-flex` <br>
&emsp;&emsp;  **亞太澳洲：**`au-syd-standard`/`au-syd-vault`/`au-syd-cold`/`au-syd-flex` <br>
&emsp;&emsp;  **阿姆斯特丹：**`ams03-standard`/`ams03-vault`/`ams03-cold`/`ams03-flex` <br>
&emsp;&emsp;  **清奈：**`che01-standard`/`che01-vault`/`che01-cold`/`che01-flex` <br>
&emsp;&emsp;  **香港：**`hkg02-standard`/`hkg02-vault`/`hkg02-cold`/`hkg02-flex` <br>
&emsp;&emsp;  **墨爾本：**`mel01-standard`/`mel01-vault`/`mel01-cold`/`mel01-flex` <br>
&emsp;&emsp;  **墨西哥：**`mex01-standard`/`mex01-vault`/`mex01-cold`/`mex01-flex` <br>
&emsp;&emsp;  **米蘭：**`mil01-standard`/`mil01-vault`/`mil01-cold`/`mil01-flex` <br>
&emsp;&emsp;  **蒙特婁：**`mon01-standard`/`mon01-vault`/`mon01-cold`/`mon01-flex` <br>
&emsp;&emsp;  **奧斯陸：**`osl01-standard`/`osl01-vault`/`osl01-cold`/`osl01-flex` <br>
&emsp;&emsp;  **聖荷西：**`sjc04-standard`/`sjc04-vault`/`sjc04-cold`/`sjc04-flex` <br>
&emsp;&emsp;  **聖保羅：**`sao01-standard`/`sao01-vault`/`sao01-cold`/`sao01-flex` <br>
&emsp;&emsp;  **首爾：**`seo01-standard`/`seo01-vault`/`seo01-cold`/`seo01-flex` <br>
&emsp;&emsp;  **多倫多：**`tor01-standard`/`tor01-vault`/`tor01-cold`/`tor01-flex` <br>


如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

## 使用 REST API、程式庫及 SDK
{: #classes-sdk}

IBM COS SDK 中引進了數個新的 API，用來支援使用保留原則的應用程式。請在此頁面頂端選取語言（curl、Java、Javascript、Go 或 Python），以使用適當的 COS SDK 來檢視範例。 

請注意，所有程式碼範例都假設存在稱為 `cos` 且可以呼叫不同方法的用戶端物件。如需建立用戶端的詳細資料，請參閱特定 SDK 手冊。


### 建立具有儲存空間類別的儲存區

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

儲存區建立之後，就無法變更其儲存空間類別。如果需要重新分類物件，則需要將資料移至另一個具有所需儲存空間類別的儲存區。 
