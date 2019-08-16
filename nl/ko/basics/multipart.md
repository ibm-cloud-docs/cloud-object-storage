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

# 대형 오브젝트 저장
{: #large-objects}

{{site.data.keyword.cos_full}}는 다중 파트 업로드 사용 시 10TB까지 단일 오브젝트를 지원할 수 있습니다. 대형 오브젝트는 [Aspera 고속 전송이 사용 가능한 콘솔을 사용하여](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera) 업로드할 수도 있습니다. 대부분의 시나리오에서 Aspera 고속 전송을 사용하면 특히 장거리 또는 불안정한 네트워크 조건에서 데이터 전송의 성능이 크게 향상됩니다.

## 다중 파트의 오브젝트 업로드
{: #large-objects-multipart}

다중 파트 업로드 오퍼레이션은 대형 오브젝트를 {{site.data.keyword.cos_short}}에 작성하기 위해 권장됩니다. 단일 오브젝트의 업로드는 파트 세트로 수행되며 이러한 파트는 개별적으로 임의 순서로 그리고 동시에 업로드될 수 있습니다. 업로드가 완료되면 {{site.data.keyword.cos_short}}가 모든 파트를 모아 하나의 오브젝트로 제시합니다. 이는 네트워크 장애가 발생해도 대용량 업로드가 실패하지 않는다는 점, 업로드를 일시정지하고 나중에 다시 시작할 수 있다는 점, 오브젝트가 작성되는 즉시 업로드 가능한 점 등 많은 이점을 제공합니다. 

다중 파트 업로드는 5MB가 넘는 오브젝트에서만 사용 가능합니다. 50GB보다 작은 오브젝트의 경우 최적의 성능을 위해 20MB에서 100MB의 파트 크기가 권장됩니다. 더 큰 오브젝트의 경우에는 성능에 큰 영향을 주지 않고 파트 크기를 늘릴 수 있습니다. 다중 파트 업로드는 최대 오브젝트 크기가 10TB이며 각각 5GB인 10,000개의 파트로 제한됩니다.


병렬화된 업로드 관리 및 최적화와 관련된 복잡도로 인해 많은 개발자가 다중 파트 업로드 지원을 제공하는 라이브러리를 사용합니다.

호환 가능한 라이브러리와 SDK뿐만 아니라 CLI 또는 IBM Cloud Console과 같은 대부분의 도구는 다중 파트 업로드로 오브젝트를 자동 전송합니다.

## REST API 및 SDK 사용
{: #large-objects-multipart-api} 

오브젝트가 삭제되거나 다중 파트 업로드가 중단될 때까지 불완전한 다중 파트 업로드가 지속됩니다. 불완전한 다중 파트 업로드가 중단되지 않으면 파트 업로드가 계속 리소스를 사용합니다. 이 점을 염두에 두고 인터페이스를 설계하고 불완전한 다중 파트 업로드를 정리해야 합니다.
{:tip}

다중 파트로 오브젝트를 업로드하는 데는 세 가지 단계가 있습니다.

1. 업로드가 시작되고 `UploadId`가 작성됩니다.
2. 개별 파트는 순차 파트 번호와 오브젝트의 `UploadId`를 지정하여 업로드됩니다.
3. 모든 파트 업로드가 완료되면 `UploadId`인 요청 및 각 파트 번호와 해당 `Etag` 값을 나열하는 XML 블록을 보내 업로드가 완료됩니다.

엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.
{:tip}

### 다중 파트 업로드 시작
{: #large-objects-multipart-api-initiate} 
{: http}

조회 매개변수 `upload`를 사용하여 오브젝트에 발행된 `POST`는 새 `UploadId` 값을 작성하며, 이 값은 업로드되는 오브젝트의 각 파트에서 참조됩니다.
{: http}

**구문**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**요청 예**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**응답 예**
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

### 파트 업로드
{: #large-objects-multipart-api-upload-part} 
{: http}

조회 매개변수가 `partNumber` 및 `uploadId`인 오브젝트에 발행된 `PUT` 요청이 오브젝트의 한 파트를 업로드합니다. 파트는 연속으로 또는 동시에 업로드될 수 있지만 순서대로 번호를 매겨야 합니다.
{: http}

**구문**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**요청 예**
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

**응답 예**
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

### 다중 파트 업로드 완료
{: #large-objects-multipart-api-complete} 
{: http}

조회 매개변수가 `uploadId`인 오브젝트에 발행된 `POST` 요청 및 본문의 해당 XML 블록이 다중 파트 업로드를 완료합니다.
{: http}

**구문**
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

**요청 예**
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

**응답 예**
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


### 불완전한 다중 파트 업로드 중단
{: #large-objects-multipart-api-abort} 
{: http}

조회 매개변수가 `uploadId`인 오브젝트에 발행된 `DELETE` 요청이 다중 파트 업로드의 완료되지 않은 모든 파트를 삭제합니다.
{: http}
**구문**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**요청 예**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**응답 예**
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

### S3cmd(CLI) 사용
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){:new_window}는 S3 프로토콜을 사용하는 클라우드 스토리지 서비스 제공자에서 데이터를 업로드하고 검색하고 관리하는 데 필요한 무료 Linux 및 Mac 명령행 도구 및 클라이언트입니다. 이는 명령행 프로그램에 익숙한 파워유저를 위해 디자인되었으며 일괄처리 스크립트 및 자동화된 백업에 이상적입니다. S3cmd는 Python으로 작성됩니다. 이는 GNU Public License v2(GPLv2)에서 사용 가능한 오픈 소스 프로젝트이며 상용 및 개인용의 경우 무료입니다.
{: S3cmd}

S3cmd는 Python 2.6 이상을 필요로 하고 Python 3와 호환됩니다. S3cmd를 설치하는 가장 쉬운 방법은 PyPi(Python Package Index)를 사용하는 것입니다.
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

패키지가 설치되면 [여기](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window}에서 {{site.data.keyword.cos_full}} 예제 구성 파일을 확보하고 Cloud Object Storage(S3) 인증 정보로 이 파일을 업데이트하십시오.
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

업데이트해야 하는 네 개의 행은 다음과 같습니다.
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
이는 다음을 실행하여 생성된 파일을 사용하는 경우 또는 예제 파일을 사용하는 경우 모두 동일합니다. `s3cmd --configure`.
{: S3cmd}

고객 포털에서 COS 세부사항으로 이러한 행이 업데이트되면 `s3cmd ls` 명령을 실행하여 연결을 테스트할 수 있으며, 이 명령은 계정의 모든 버킷을 나열합니다.
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

기본 사용 정보와 함께 옵션 및 명령의 전체 목록은 [s3tools](https://s3tools.org/usage){:new_window} 사이트에 있습니다.
{: S3cmd}

### S3cmd를 사용한 다중 파트 업로드
{: #large-objects-s3cmd-upload} 
{: S3cmd}

`put` 명령은 지정된 임계값보다 큰 파일을 업로드하려고 할 때 다중 파트 업로드를 자동으로 실행합니다.
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

`--multipart-chunk-size-mb` 옵션으로 임계값이 결정됩니다.
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

예제:
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

출력:
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

### Java SDK 사용
{: #large-objects-java} 
{: java}

Java SDK는 대형 오브젝트 업로드를 실행하는 두 가지 방법을 제공합니다.
{: java}

* [다중 파트 업로드](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [전송 관리자](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Python SDK 사용
{: #large-objects-python} 
{: python}

Python SDK는 대형 오브젝트 업로드를 실행하는 두 가지 방법을 제공합니다.
{: python}

* [다중 파트 업로드](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [전송 관리자](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Node.js SDK 사용
{: #large-objects-node} 
{: javascript}

Node.js SDK는 대형 오브젝트 업로드를 실행하는 한 가지 방법을 제공합니다.
{: javascript}

* [다중 파트 업로드](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
