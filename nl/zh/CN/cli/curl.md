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

下面是 {{site.data.keyword.cos_full}} REST API 的基本 `curl` 命令的“备忘单”。在 API 参考中可以找到有关[存储区](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)或[对象](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations)的其他详细信息。

使用 `curl` 即假定您在一定程度上熟悉命令行和对象存储器，并且已从[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)、[端点引用](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)或[控制台](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)中获取必要的信息。如果遇到任何不熟悉的术语或变量，可以在[词汇表](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)中找到这些术语或变量。

**注**：个人可标识信息 (PII)：创建存储区和/或添加对象时，请确保未使用可以通过名称、位置或其他任何方式识别到任何用户（自然人）的任何信息。
{:tip}

## 请求 IAM 令牌
{: #curl-iam}

有两种方法可生成用于认证请求的 IAM OAuth 令牌：使用 `curl` 命令和 API 密钥（如下所述），或通过使用 [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) 的命令行。 

### 使用 API 密钥请求 IAM 令牌
{: #curl-token}

首先，确保您有 API 密钥。请从 [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys) 中获取 API 密钥。

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## 获取资源实例标识
{: #curl-instance-id}

以下某些命令需要 `ibm-service-instance-id` 参数。要查找此值，请在云控制台中，转至 Object Storage 实例的**服务凭证**选项卡。根据需要创建新的凭证，然后使用*查看凭证*下拉列表来查看 JSON 格式。请使用 `resource_instance_id` 的值。 

要用于 curl API，您只需要 UUID，UUID 在最后一个单冒号之后开始，并在最后一个双冒号之前结束。例如，id `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` 可以缩略为 `39d8d161-22c4-4b77-a856-f11db5130d7d`。
{:tip}

## 列出存储区
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## 添加存储区
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## 添加存储区（存储类）
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

在[存储类指南](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint)中可以参考 `LocationConstraint` 的有效供应代码的列表。

## 创建存储区 CORS
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

`Content-MD5` 头需要是 Base64 编码的 MD5 散列的二进制表示。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## 获取存储区 CORS
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 删除存储区 CORS
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 列出对象
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 获取存储区头
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 删除存储区
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 上传对象
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## 获取对象的头
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 复制对象
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## 检查 CORS 信息
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## 下载对象
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 检查对象的 ACL
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 允许对对象进行匿名访问
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## 删除一个对象
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 删除多个对象
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

`Content-MD5` 头需要是 Base64 编码的 MD5 散列的二进制表示。

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## 启动分块上传
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 上传分块
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## 完成分块上传
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

## 获取未完成的分块上传
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 中止未完成的分块上传
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
