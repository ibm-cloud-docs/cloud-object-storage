---

copyright:
  years: 2017, 2018
lastupdated: "2018-08-02"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Store very large objects

{{site.data.keyword.cos_full}} can support single objects as large as 10TB when using multipart uploads.  Large objects can also be uploaded [using the console with Aspera high-speed-transfer enabled](/docs/services/cloud-object-storage/basics/upload.html#high-speed-transfer). Under most scenarios, Aspera high-speed transfer will result in significantly increased perfomance for transfering data, especially across long distances or under unstable network conditions.

## Uploading objects in multiple parts

When working with larger objects, multipart upload operations are recommended to write objects into {{site.data.keyword.cos_short}}. An upload of a single object is performed as a set of parts and these parts can be uploaded independently in any order and in parallel. Upon upload completion, {{site.data.keyword.cos_short}} then presents all parts as a single object. This provides many benefits: network interruptions do not cause large uploads to fail, uploads can be paused and restarted over time, and objects can be uploaded as they are being created.

Multipart uploads are only available for objects larger than 5MB. For objects smaller than 50GB, a part size of 20MB to 100MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact.  Multipart uploads are limited to no more than 10,000 parts of 5GB each up to a maximum object size of 10TB.

Avoid using more than 500 parts when possible, this leads to inefficiencies in {{site.data.keyword.cos_short}}.
{:tip}

Due to the complexity involved in managing and optimizing parallelized uploads, many developers make use of libraries that provide multipart upload support.

Most tools, such as the AWS CLI or the IBM Cloud Console, as well as most compatible libraries and SDKs, will automatically transfer objects in multipart uploads.

## Using the API
{: multipart}

Incomplete multipart uploads do persist until the object is deleted or the multipart upload is aborted with `AbortIncompleteMultipartUpload`. If an incomplete multipart upload is not aborted, the partial upload continues to use resources.  Interfaces should be designed with this point in mind, and clean up incomplete multipart uploads.
{:tip}

There are three phases to uploading an object in multiple parts:

1. The upload is initiated and an `UploadId` is created.
2. Individual parts are uploaded specifying their sequential part numbers and the `UploadId` for the object.
3. When all parts are finished uploading, the upload is completed by sending a request with the `UploadId` and an XML block that lists each part number and it's respective `Etag` value.

### Initiate a multipart upload

A `POST` issued to an object with the query parameter `upload` creates a new `UploadId` value, which is then be referenced by each part of the object being uploaded.

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```

**Sample request**

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

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

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```

----

### Upload a part

A `PUT` request issued to an object with query parameters `partNumber` and `uploadId` will upload one part of an object.  The parts may be uploaded serially or in parallel, but must be numbered in order.

**Syntax**

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```

**Sample request**

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 13374550
```

**Sample response**

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

----

### Complete a multipart upload

A `POST` request issued to an object with query parameter `uploadId` and the appropriate XML block in the body will complete a multipart upload.

**Syntax**

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```

**Sample request**

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3-api.us-geo.objectstorage.softlayer.net
Content-Length: 257
```

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

**Sample response**

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

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3-api.us-geo.objectstorage.softlayer.net/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```

----

### Abort incomplete multipart uploads

A `DELETE` request issued to an object with query parameter `uploadId` will delete all unfinished parts of a multipart upload.

**Syntax**

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```

**Sample request**

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3-api.us-geo.objectstorage.softlayer.net
```

**Sample response**

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```

## Using S3cmd (CLI)

[S3cmd](https://s3tools.org/s3cmd){:new_window} is a free Linux and Mac command line tool and client for uploading, retrieving and managing data in cloud storage service providers that use the S3 protocol. It is designed for power users who are familiar with command line programs and is ideal for batch scripts and automated backup.  S3cmd is written in Python. It's an open source project available under GNU Public License v2 (GPLv2) and is free for both commercial and private use.

### Installation and Configuration

S3cmd requires Python 2.6 or newer and is compatible with Python 3.  The easiest way to install S3cmd is with the Python Package Index (PyPi).

```
pip install s3cmd
```

Once the package has been installed, grab the {{site.data.keyword.cos_full}} example configuration file [here](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window} and update it with your Cloud Object Storage (S3) credentials:

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```

The 4 lines that need to be updated are

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`

This is the same whether you use the example file or the one generated by running: `s3cmd --configure`.

Once those lines have been updated with the COS details from the Customer portal you can test the connection by issuing the command `s3cmd ls` which will list all the buckets on the account.

```
$ s3cmd ls 
2017-02-03 14:52  s3://backuptest
2017-02-06 15:04  s3://coldbackups
2017-02-03 21:23  s3://largebackup
2017-02-07 17:44  s3://winbackup
```

The full list of options and commands along with basic usage information is available on the [s3tools](https://s3tools.org/usage){:new_window} site.

### Multi-Part Upload using S3cmd

A `put` command will automatically execute a multi-part upload when attempting to upload a file larger than the specified threshold..

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```

The threshold is determined by the `--multipart-chunk-size-mb` option:

```
--multipart-chunk-size-mb=SIZE
    Size of each chunk of a multipart upload. Files bigger
    than SIZE are automatically uploaded as multithreaded-
    multipart, smaller files are uploaded using the
    traditional method. SIZE is in megabytes, default
    chunk size is 15MB, minimum allowed chunk size is 5MB,
    maximum is 5GB.
```

Example:

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```

Output:

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

## Using the Java SDK

The Java SDK provides two ways to execute large object uploads:

* [Multi-part Uploads](/docs/services/cloud-object-storage/libraries/java.html#multipart-upload)
* [TransferManager](/docs/services/cloud-object-storage/libraries/java.html#transfer-manager)

## Using the Python SDK

The Python SDK provides two ways to execute large object uploads:

* [Multi-part Uploads](/docs/services/cloud-object-storage/libraries/python.html#multipart-upload)
* [TransferManager](/docs/services/cloud-object-storage/libraries/python.html#transfer-manager)

## Using the Node.js SDK

The Node.js SDK provides a single way to execute large object uploads:

* [Multi-part Uploads](/docs/services/cloud-object-storage/libraries/node.html#multipart-upload)