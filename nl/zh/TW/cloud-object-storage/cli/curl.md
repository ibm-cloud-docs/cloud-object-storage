---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, curl, cli

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

# 使用 `curl`
{: #curl}

以下是 {{site.data.keyword.cos_full}} REST API 基本 `curl` 指令的「備忘單」。您可以在[儲存區](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)或[物件](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations)的 API 參考資料中找到其他詳細資料。

使用 `curl` 假設您對指令行和物件儲存空間具有一定程度的熟悉程度，並且已從[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)、[端點參考資料](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)或[主控台](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)取得必要的資訊。如果有任何術語或變數不熟悉，可以在[名詞解釋](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)中找到它們。

**附註**：個人識別資訊 (PII)：建立儲存區及/或新增物件時，請務必不要使用可以依姓名、位置或任何其他方法識別任何使用者（自然人）的任何資訊。
{:tip}

## 要求 IAM 記號
{: #curl-iam}

有兩種方式可以產生 IAM oauth 記號來鑑別要求：使用 `curl` 指令搭配 API 金鑰（說明如下），或使用 [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) 從指令行中使用。 

### 使用 API 金鑰要求 IAM 記號
{: #curl-token}

請先確定您有 API 金鑰。從 [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys) 取得。

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## 取得資源實例 ID
{: #curl-instance-id}

下列部分指令需要 `ibm-service-instance-id` 參數。若要尋找此值，請在雲端主控台中移至 Object Storage 實例的**服務認證**標籤。視需要建立新的認證，然後使用*檢視認證* 下拉清單，以查看 JSON 格式。使用 `resource_instance_id` 的值。 

若要與 curl API 搭配使用，您只需要在最後一個單冒號後面開始，然後在最終雙冒號之前結束的 UUID。例如，ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` 可以縮寫為 `39d8d161-22c4-4b77-a856-f11db5130d7d`。
{:tip}

## 列出儲存區
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## 新增儲存區
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## 新增儲存區（儲存空間類別）
{: #curl-add-bucket-class}

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

`LocationConstraint` 的有效佈建碼清單可以在[儲存空間類別手冊](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint)中參閱。

## 建立儲存區 CORS
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

`Content-MD5` 標頭需要是 base64 編碼 MD5 雜湊的二進位表示法。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## 取得儲存區 CORS
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 刪除儲存區 CORS
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 列出物件
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 取得儲存區標頭
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 刪除儲存區
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 上傳物件
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## 取得物件的標頭
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 複製物件
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## 檢查 CORS 資訊
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## 下載物件
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 檢查物件的 ACL
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 容許對物件的匿名存取
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## 刪除物件
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 刪除多個物件
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

`Content-MD5` 標頭需要是 base64 編碼 MD5 雜湊的二進位表示法。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## 起始多部分上傳
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 上傳組件
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## 完成多部分上傳
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## 取得不完整的多部分上傳
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 中斷不完整的多部分上傳
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
