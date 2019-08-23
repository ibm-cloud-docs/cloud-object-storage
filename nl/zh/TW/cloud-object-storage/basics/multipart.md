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

# 儲存大型物件
{: #large-objects}

使用多部分上傳時，{{site.data.keyword.cos_full}} 可以支援大小為 10 TB 的單一物件。您也可以[使用已啟用 Aspera 高速傳送的主控台](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera)來上傳大型物件。在大部分情況下，Aspera 高速傳送可大幅增加傳送資料的效能，尤其是長距離或在不穩定的網路條件下。

## 透過多個組件上傳物件
{: #large-objects-multipart}

建議使用多部分上傳作業，將較大的物件寫入至 {{site.data.keyword.cos_short}}。單一物件上傳是作為一組組件來執行，而且可以依任何順序平行獨立上傳這些組件。上傳完成時，{{site.data.keyword.cos_short}} 接著會將所有組件呈現為單一物件。這提供許多優點：網路岔斷不會導致大型上傳失敗、可以在一段時間後暫停並重新啟動上傳，而且物件可以在建立時上傳。

多部分上傳僅適用於大於 5 MB 的物件。對於小於 50 GB 的物件，建議使用 20 MB 到 100 MB 的組件大小，以達到最佳效能。對於較大的物件，可以增加組件大小，而不會造成重大效能影響。多部分上傳限制為不超過 10,000 個組件，且各組件 5 GB，而且物件大小上限為 10 TB。


由於管理及最佳化平行上傳所涉及的複雜性，許多開發人員都會使用提供多部分上傳支援的程式庫。

大部分工具（例如 CLI 或 IBM Cloud Console）以及大部分相容程式庫及 SDK，都會透過多部分上傳自動傳送物件。

## 使用 REST API 或 SDK
{: #large-objects-multipart-api} 

除非刪除物件，或中斷多部分上傳，否則會持續保留不完整的多部分上傳。如果未中斷不完整的多部分上傳，則局部上傳會繼續使用資源。設計介面時，應該記住這一點，並清除不完整的多部分上傳。
{:tip}

在多個組件中上傳物件分為三個階段：

1. 起始上傳，並建立 `UploadId`。
2. 上傳個別組件，指定物件的循序組件號碼及 `UploadId`。
3. 完成上傳所有組件時，會藉由傳送具有 `UploadId` 的要求以及列出每個組件號碼及其個別 `Etag` 值的 XML 區塊來完成上傳。

如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
{:tip}

### 起始多部分上傳
{: #large-objects-multipart-api-initiate} 
{: http}

針對具有查詢參數 `upload` 的物件發出 `POST`，會建立新的 `UploadId` 值，之後，所上傳物件的每個組件都會參照該值。
{: http}

**語法**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**要求範例**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**回應範例**
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

### 上傳組件
{: #large-objects-multipart-api-upload-part} 
{: http}

針對具有查詢參數 `partNumber` 及 `uploadId` 的物件發出 `PUT` 要求，將會上傳物件的某個組件。組件可以依序或平行上傳，但必須依序編號。
{: http}

**語法**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**要求範例**
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

**回應範例**
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

### 完成多部分上傳
{: #large-objects-multipart-api-complete} 
{: http}

針對具有查詢參數 `uploadId` 且內文中具有適當 XML 區塊的物件發出 `POST` 要求，將會完成多部分上傳。
{: http}

**語法**
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

**要求範例**
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

**回應範例**
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


### 中斷不完整的多部分上傳
{: #large-objects-multipart-api-abort} 
{: http}

針對具有查詢參數 `uploadId` 的物件發出 `DELETE` 要求，會刪除多部分上傳的所有未完成組件。
{: http}
**語法**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**要求範例**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**回應範例**
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

[S3cmd](https://s3tools.org/s3cmd){:new_window} 是免費的 Linux 及 Mac 指令行工具和用戶端，用於上傳、擷取及管理使用 S3 通訊協定的雲端儲存空間服務提供者中的資料。它專為熟悉指令行程式的專業使用者所設計，非常適合用於批次 Script 及自動化備份。S3cmd 是以 Python 撰寫。它是可在 GNU Public License 第 2 版 (GPLv2) 下取得的開放程式碼專案，並且免費提供商業和私人使用。
{: S3cmd}

S3cmd 需要 Python 2.6 或更新版本，並且與 Python 3 相容。安裝 S3cmd 的最簡單方式是使用 Python Package Index (PyPi)。
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

安裝套件之後，請在[這裡](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window}抓取 {{site.data.keyword.cos_full}} 範例配置檔，並使用 Cloud Object Storage (S3) 認證進行更新：
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
不論您是使用範例檔案還是使用執行 `s3cmd --configure` 所產生的檔案，這都相同。
{: S3cmd}

從「客戶入口網站」使用 COS 詳細資料更新那些行之後，您可以發出 `s3cmd ls` 指令來測試連線，這樣會列出帳戶上的所有儲存區。
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

[s3tools](https://s3tools.org/usage){:new_window} 網站上具有完整的選項及指令清單，以及基本用法資訊。
{: S3cmd}

### 使用 S3cmd 的多部分上傳
{: #large-objects-s3cmd-upload} 
{: S3cmd}

嘗試上傳大於指定臨界值的檔案時，`put` 指令會自動執行多部分上傳。
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

臨界值是由 `--multipart-chunk-size-mb` 選項所決定：
{: S3cmd}

```
--multipart-chunk-size-mb=SIZE
    Size of each chunk of a multipart upload. Files bigger
    than SIZE are automatically uploaded as multithreaded-
    multipart, smaller files are uploaded using the
    traditional method. SIZE is in megabytes, default
    chunk size is 15MB, minimum allowed chunk size is 5MB,
    maximum is 5GB.
```
{: codeblock}
{: S3cmd}

範例：
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

輸出：
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

Java SDK 提供兩種方式來執行大型物件上傳：
{: java}

* [多部分上傳](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### 使用 Python SDK
{: #large-objects-python} 
{: python}

Python SDK 提供兩種方式來執行大型物件上傳：
{: python}

* [多部分上傳](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### 使用 Node.js SDK
{: #large-objects-node} 
{: javascript}

Node.js SDK 提供一種方式來執行大型物件上傳：
{: javascript}

* [多部分上傳](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
