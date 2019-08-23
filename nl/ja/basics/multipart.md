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

# 大きなオブジェクトの保管
{: #large-objects}

{{site.data.keyword.cos_full}} は、マルチパート・アップロードを使用する場合は最大 10 TB の単一オブジェクトをサポートします。[Aspera 高速転送を有効にしてコンソールを使用する](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera)ことによっても、大きなオブジェクトをアップロードできます。ほとんどのシナリオで、特に長距離を転送する場合やネットワーク状態が不安定な場合は、Aspera 高速転送によってデータ転送のパフォーマンスが向上します。

## 複数のパートでのオブジェクトのアップロード
{: #large-objects-multipart}

大きなオブジェクトを {{site.data.keyword.cos_short}} に書き込むにはマルチパート・アップロード操作が推奨されます。単一オブジェクトのアップロードがいくつかのパートからなる 1 つのセットとして実行され、これらのパートは、並行して、任意の順序で、それぞれ独立して、アップロードされることが可能です。アップロードが完了した時点で、{{site.data.keyword.cos_short}} はすべてのパーツを単一オブジェクトとして表します。これには、ネットワーク割り込みが原因で大規模なアップロードが失敗することがない、時間の経過とともにアップロードを一時停止および再開できる、オブジェクトの作成中にそのオブジェクトをアップロードできるなど、多くの利点があります。

複数パーツ・アップロードは、5 MB を超えるオブジェクトに対してのみ使用可能です。50 GB より小さいオブジェクトの場合、最適なパフォーマンスを得るため、パートのサイズとして 20 MB から 100 MB が推奨されます。それより大きいオブジェクトの場合、パーツのサイズを増やしてもパフォーマンスへの大きな影響はありません。マルチパート・アップロードは、それぞれが 5 GB の 10,000 個以下のパートに制限され、オブジェクトの最大サイズは 10 TB に制限されています。


並列化されたアップロードの管理と最適化は複雑であるため、多くの開発者はマルチパート・アップロードをサポートするライブラリーを使用します。

CLI や IBM Cloud コンソールなどのほとんどのツール、およびほとんどの互換性のあるライブラリーおよび SDK は、自動的にオブジェクトをマルチパート・アップロードで転送します。

## REST API または SDK の使用
{: #large-objects-multipart-api} 

不完全な複数パーツ・アップロードは、オブジェクトが削除されるか、複数パーツ・アップロードが中止されるまで保持されます。不完全な複数パーツ・アップロードが中止されない場合、部分アップロードによってリソースの使用が継続されます。インターフェースの設計でこの点を考慮し、不完全な複数パーツ・アップロードをクリーンアップする必要があります。{:tip}

オブジェクトを複数のパートでアップロードするには、以下の 3 つのフェーズがあります。

1. アップロードが開始され、`UploadId` が作成されます。
2. 個々のパートは、順次パート番号およびオブジェクトの `UploadId` を指定してアップロードされます。
3. すべてのパートのアップロードが完了すると、`UploadId` と、各パート番号およびそれぞれの `Etag` 値をリストする XML ブロックを指定した要求を送信することでアップロードが完了します。

エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
{:tip}

### マルチパート・アップロードの開始
{: #large-objects-multipart-api-initiate} 
{: http}

照会パラメーター `upload` が指定された `POST` がオブジェクトに対して発行されると、新しい `UploadId` 値が作成され、アップロードされるオブジェクトの各パートによってその値が参照されます。
{: http}

**構文**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**要求例**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**応答例**
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

### パートのアップロード
{: #large-objects-multipart-api-upload-part} 
{: http}

照会パラメーター `partNumber` および `uploadId` が指定された `PUT` 要求がオブジェクトに対して発行されると、オブジェクトの 1 つのパートがアップロードされます。パートは順次または並列にアップロードされることが可能ですが、順番に番号が付けられている必要があります。
{: http}

**構文**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**要求例**
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

**応答例**
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

### マルチパート・アップロードの完了
{: #large-objects-multipart-api-complete} 
{: http}

照会パラメーター `uploadId` が指定され、適切な XML ブロックが本体に指定された `POST` 要求がオブジェクトに対して発行されると、マルチパート・アップロードが実行されます。
{: http}

**構文**
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

**要求例**
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

**応答例**
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


### 未完了マルチパート・アップロードの打ち切り
{: #large-objects-multipart-api-abort} 
{: http}

照会パラメーター `uploadId` が指定された `DELETE` 要求がオブジェクトに対して発行されると、マルチパート・アップロードの終了していないパートがすべて削除されます。
{: http}
**構文**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**要求例**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**応答例**
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

### S3cmd (CLI) の使用
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){:new_window} は、S3 プロトコルを使用するクラウド・ストレージ・サービス・プロバイダーでデータのアップロード、取得、および管理に使用される、 Linux および Mac 用の無料コマンド・ライン・ツールおよびクライアントです。これは、コマンド・ライン・プログラムに精通しているパワー・ユーザー向けに設計されており、バッチ・スクリプトおよび自動バックアップに最適です。S3cmd は Python で作成されています。これは、GNU Public License v2 (GPLv2) の下で使用可能なオープン・ソース・プロジェクトであり、商用でもプライベートでも無料で使用できます。
{: S3cmd}

S3cmd には Python 2.6 以降が必要であり、Python 3 と互換です。S3cmd をインストールする最も簡単な方法は、Python Package Index (PyPi) を使用することです。
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

パッケージがインストールされたら、[ここ](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window}で {{site.data.keyword.cos_full}} サンプル構成ファイルを入手し、ご使用の Cloud Object Storage (S3) 資格情報で更新します。
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

以下の 4 行を更新する必要があります。
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
これは、サンプル・ファイルを使用する場合も、`s3cmd --configure` を実行することによって生成されるファイルを使用する場合も同じです。
{: S3cmd}

カスタマー・ポータルからの COS 詳細でこれらの行が更新されたら、`s3cmd ls` コマンドを実行して接続をテストできます。このコマンドによって、アカウントのすべてのバケットがリストされます。
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

オプションおよびコマンドの完全なリスト、および基本的な使用法についての情報は、[s3tools](https://s3tools.org/usage){:new_window} サイトにあります。
{: S3cmd}

### S3cmd を使用したマルチパート・アップロード
{: #large-objects-s3cmd-upload} 
{: S3cmd}

指定されたしきい値より大きいファイルをアップロードしようとすると、`put` コマンドは自動的にマルチパート・アップロードを実行します。
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

しきい値は `--multipart-chunk-size-mb` オプションによって決まります。
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

例:
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

出力:
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

### Java SDK の使用
{: #large-objects-java} 
{: java}

Java SDK で大きなオブジェクトのアップロードを実行するには、次の 2 とおりの方法があります。
{: java}

* [マルチパート・アップロード](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Python SDK の使用
{: #large-objects-python} 
{: python}

Python SDK で大きなオブジェクトのアップロードを実行するには、次の 2 とおりの方法があります。
{: python}

* [マルチパート・アップロード](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Node.js SDK の使用
{: #large-objects-node} 
{: javascript}

Node.js SDK で大きなオブジェクトのアップロードを実行する方法は次の 1 つです。
{: javascript}

* [マルチパート・アップロード](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
