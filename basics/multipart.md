---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: big data, multipart, multiple parts, transfer

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Store large objects
{: #large-objects}

{{site.data.keyword.cos_full}} can support single objects as large as 10 TB when using multipart uploads. 
{: shortdesc}

Large objects can also be uploaded [by using the console with Aspera high-speed-transfer enabled](/docs/cloud-object-storage?topic=cloud-object-storage-aspera). Under most scenarios, Aspera high-speed transfer results in significantly increased performance for transferring data, especially across long distances or under unstable network conditions.

## Uploading objects in multiple parts
{: #large-objects-multipart}

Multipart upload operations are recommended to write larger objects into {{site.data.keyword.cos_short}}. An upload of a single object is performed as a set of parts and these parts can be uploaded independently in any order and in parallel. Upon upload completion, {{site.data.keyword.cos_short}} then presents all parts as a single object. This provides many benefits: network interruptions do not cause large uploads to fail, uploads can be paused and restarted over time, and objects can be uploaded as they are being created.

Multipart uploads are only available for objects larger than 5 MB. For objects smaller than 50 GB, a part size of 20 MB to 100 MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact. Multipart uploads are limited to no more than 10,000 parts of 5 GB each up to a maximum object size of 10 TB.


Due to the complexity involved in managing and optimizing parallelized uploads, many developers use libraries that provide multipart upload support.

Most tools, such as the CLIs or the IBM Cloud Console, as well as most compatible libraries and SDKs, will automatically transfer objects in multipart uploads.

## Using the REST API or SDKs
{: #large-objects-multipart-api} 

Incomplete multipart uploads do persist until the object is deleted or the multipart upload is aborted. If an incomplete multipart upload is not aborted, the partial upload continues to use resources. Interfaces should be designed with this point in mind, and clean up incomplete multipart uploads.
{:tip}

There are three phases to uploading an object in multiple parts:

1. The upload is initiated and an `UploadId` is created.
2. Individual parts are uploaded specifying their sequential part numbers and the `UploadId` for the object.
3. When all parts are finished uploading, the upload is completed by sending a request with the `UploadId` and an XML block that lists each part number and it's respective `Etag` value.

For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

#### Initiate a multipart upload
{: #large-objects-multipart-api-initiate} 
{: http}

A `POST` issued to an object with the query parameter `upload` creates a new `UploadId` value, which is then be referenced by each part of the object being uploaded.
{: http}

**Syntax**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**Example request**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Example response**
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

#### Upload a part
{: #large-objects-multipart-api-upload-part} 
{: http}

A `PUT` request that is issued to an object with query parameters `partNumber` and `uploadId` will upload one part of an object. The parts can be uploaded serially or in parallel, but must be numbered in order.
{: http}

**Syntax**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Example request**
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

**Example response**
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

#### Complete a multipart upload
{: #large-objects-multipart-api-complete} 
{: http}

A `POST` request that is issued to an object with query parameter `uploadId` and the appropriate XML block in the body will complete a multipart upload.
{: http}

**Syntax**
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

**Example request**
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

**Example response**
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


#### Abort incomplete multipart uploads
{: #large-objects-multipart-api-abort} 
{: http}

A `DELETE` request issued to an object with query parameter `uploadId` deletes all unfinished parts of a multipart upload.
{: http}

**Syntax**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Example request**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Example response**
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

#### Using S3cmd (CLI)
{: #large-objects-s3cmd} 
{: S3cmd}

[S3cmd](https://s3tools.org/s3cmd){: external} is a free Linux and Mac command-line tool and client for uploading, retrieving, and managing data in cloud storage service providers that use the S3 protocol. It is designed for power users who are familiar with command-line programs and is ideal for batch scripts and automated backup. S3cmd is written in Python. It's an open source project available under GNU Public License v2 (GPLv2) and is free for both commercial and private use.
{: S3cmd}

S3cmd requires Python 2.6 or newer and is compatible with Python 3. The easiest way to install S3cmd is with the Python Package Index (PyPi).
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

Once the package has been installed, grab the {{site.data.keyword.cos_full}} example configuration file [here](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){: external} and update it with your Cloud Object Storage (S3) credentials:
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

The four lines that need to be updated are
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
This is the same whether you use the example file or the one generated by running: `s3cmd --configure`.
{: S3cmd}

Once those lines have been updated with the COS details from the Customer portal, you can test the connection by issuing the command `s3cmd ls`, which will list all the buckets on the account.
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

The full list of options and commands along with basic usage information is available on the [s3tools](https://s3tools.org/usage){: external} site.
{: S3cmd}

#### Multipart uploads with S3cmd
{: #large-objects-s3cmd-upload} 
{: S3cmd}

A `put` command will automatically run a multi-part upload when attempting to upload a file larger than the specified threshold..
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

The threshold is determined by the `--multipart-chunk-size-mb` option:
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

Example:
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

Output:
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

### Using the Java SDK
{: #large-objects-java} 
{: java}

The Java SDK provides two ways to run large object uploads:
{: java}

* [Multipart Uploads](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Using the Python SDK
{: #large-objects-python} 
{: python}

The Python SDK provides two ways to run large object uploads:
{: python}

* [Multipart Uploads](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Using the Node.js SDK
{: #large-objects-node} 
{: javascript}

The Node.js SDK provides a single way to run large object uploads:
{: javascript}

* [Multipart Uploads](=/docs/cloud-object-storage/iam?topic=cloud-object-storage-node#node-examples-multipart)
{: codeblock}
{: javascript}
