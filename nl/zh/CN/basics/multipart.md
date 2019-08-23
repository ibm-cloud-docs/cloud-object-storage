---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: big data, multipart, multiple parts, transfer

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
{:S3cmd: .ph data-hd-programlang='S3cmd'}

# 存储大对象
{: #large-objects}

使用分块上传时，{{site.data.keyword.cos_full}} 可以支持的单个对象大小最大为 10 TB。此外，大对象还可以[使用启用 Aspera 高速传输的控制台](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera)来上传。在大多数情况下，Aspera 高速传输能显著提高数据传输性能，尤其是针对长距离传输或在网络条件不稳定的情况下传输。

## 分块上传对象
{: #large-objects-multipart}

要将较大的对象写入 {{site.data.keyword.cos_short}}，建议使用分块上传操作。单个对象的上传可作为一组分块来执行，这些分块可以按任意顺序独立地、并行上传。上传完成后，{{site.data.keyword.cos_short}} 会将所有分块显示为单个对象。此方法有诸多优点：网络中断不会导致大型上传操作失败，在一段时间内上传可以暂停并重新启动，并且可以在对象创建期间上传对象。

分块上传仅可用于大于 5 MB 的对象。对于小于 50 GB 的对象，建议的分块大小为 20 MB 到 100 MB，以实现最佳性能。对于更大的对象，可以增大分块大小，而不会对性能产生重大影响。分块上传限制为分块数不超过 10,000 个，每个分块 5 GB，最大对象大小为 10 TB。


由于管理和优化并行上传涉及的复杂性，许多开发者会使用提供分块上传支持的库。

大多数工具（例如，CLI 或 IBM Cloud 控制台）以及大多数兼容的库和 SDK 都将自动以分块上传方式传输对象。

## 使用 REST API 或 SDK
{: #large-objects-multipart-api} 

未完成的分块上传会一直持久存储，直到删除对象或中止分块上传。如果未中止未完成的分块上传，那么分块上传会继续使用资源。设计界面时应该谨记这一点，应清除未完成的分块上传。
{:tip}

分块上传对象分为三个阶段：

1. 启动上传并创建 `UploadId`。
2. 上传各个分块，并指定对象的分块序号和 `UploadId`。
3. 上传所有分块完成后，通过发送包含 `UploadId` 和 XML 块（列出每个分块编号及其各自的 `Etag` 值）的请求来完成上传。

有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
{:tip}

### 启动分块上传
{: #large-objects-multipart-api-initiate} 
{: http}

向对象发出使用查询参数 `upload` 的 `POST` 请求会创建新的 `UploadId` 值，然后要上传的对象的每个分块都会引用此值。
{: http}

**语法**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**示例请求**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**示例响应**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```
{: codeblock}
{: http}

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```
{: codeblock}
{: http}

----

### 上传分块
{: #large-objects-multipart-api-upload-part} 
{: http}

向对象发出使用查询参数 `partNumber` 和 `uploadId` 的 `PUT` 请求将上传对象的一个分块。这些分块可以按序列上传，也可以并行上传，但必须按顺序编号。
{: http}

**语法**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**示例请求**
{: http}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: codeblock}
{: http}

**示例响应**
{: http}

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```
{: codeblock}
{: http}

### 完成分块上传
{: #large-objects-multipart-api-complete} 
{: http}

向对象发出使用查询参数 `uploadId` 并且主体中包含相应 XML 块的 `PUT` 请求将完成分块上传。
{: http}

**语法**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**示例请求**
{: http}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**示例响应**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```
{: codeblock}
{: http}


### 中止未完成的分块上传
{: #large-objects-multipart-api-abort} 
{: http}

向对象发出使用查询参数 `uploadId` 的 `DELETE` 请求将删除分块上传中所有未完成的分块。
{: http}
**语法**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**示例请求**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**示例响应**
{: http}

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}
{: http}

### 使用 S3cmd (CLI)
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){:new_window} 是一个免费的 Linux 和 Mac 命令行工具和客户机，用于上传、检索和管理使用 S3 协议的云存储服务提供者中的数据。S3cmd 专为熟悉命令行程序的高级用户而设计，非常适合批处理脚本和自动备份。S3cmd 是用 Python 编写的。这是一个开放式源代码项目，依据 GNU Public License V2 (GPLv2) 提供，免费供商业和私人使用。
{: S3cmd}

S3cmd 需要 Python 2.6 或更高版本，并且与 Python 3 兼容。要安装 S3cmd，最简单的方法是使用 Python Package Index (PyPi)。
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

安装该包后，请在[此处](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window}抓取 {{site.data.keyword.cos_full}} 示例配置文件，并使用 Cloud Object Storage (S3) 凭证对其进行更新：
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

需要更新的四行如下：
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
无论是使用示例文件还是使用通过运行 `s3cmd --configure` 生成的文件，都需要更新这四行。
{: S3cmd}

在客户门户网站中使用 COS 详细信息更新了这些行后，就可以通过发出 `s3cmd ls` 命令来测试连接，这将列出帐户上的所有存储区。
{: S3cmd}

```
$ s3cmd ls 
2017-02-03 14:52  s3://backuptest
2017-02-06 15:04  s3://coldbackups
2017-02-03 21:23  s3://largebackup
2017-02-07 17:44  s3://winbackup
```
{: codeblock}
{: S3cmd}

[s3tools](https://s3tools.org/usage){:new_window} 站点上提供了选项和命令以及基本用法信息的完整列表。
{: S3cmd}

### 使用 S3cmd 分块上传
{: #large-objects-s3cmd-upload} 
{: S3cmd}

尝试上传大于指定阈值的文件时，`put` 命令会自动运行分块上传。
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

阈值由 `--multipart-chunk-size-mb` 选项确定：
{: S3cmd}

```
--multipart-chunk-size-mb=SIZE
    分块上传的每个块的大小。大于 SIZE 的文件会自动作为多线程
    分块上传，小于 SIZE 的文件会使用传统方法上传。SIZE
    以兆字节为单位，缺省块大小为 15 MB，允许的最小块大小为
    5 MB，最大为 5 GB。
```
{: codeblock}
{: S3cmd}

示例：
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

输出：
{: S3cmd}

```
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 1 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  1731.92 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 2 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2001.14 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 3 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2000.28 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 4 of 4, 4MB] [1 of 1]
 4973645 of 4973645   100% in    2s  1823.51 kB/s  done
 ```
{: codeblock}
{: S3cmd}

### 使用 Java SDK
{: #large-objects-java} 
{: java}

Java SDK 提供了两种运行大对象上传的方法：
{: java}

* [分块上传](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### 使用 Python SDK
{: #large-objects-python} 
{: python}

Python SDK 提供了两种运行大对象上传的方法：
{: python}

* [分块上传](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### 使用 Node.js SDK
{: #large-objects-node} 
{: javascript}

Node.js SDK 提供了一种运行大对象上传的方法：
{: javascript}

* [分块上传](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
