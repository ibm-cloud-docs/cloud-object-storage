---

copyright:
  years: 2020, 2024
lastupdated: "2024-03-20"

keywords: developer, best practices, object storage

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Data IO and encryption
{: #performance-io}

Object size can have significant impacts on {{site.data.keyword.cos_full}} performance. Choose the right approach for your workload.
{: shortdesc}

## Multipart transfers
{: #performance-io-transfers}

Under typical conditions, multipart uploads and downloads are a very efficient method for breaking up transfers into many parallel transactions. Depending on the object size, a part size of 100MB is generally recommended. In any case, it is most efficient to set the part size to a multiple of 4MiB in order to optimize the data ingest into and egress out of COS.

As with AWS S3, using multipart transfers provides the following advantages: 
- Improved throughput — You can upload parts in parallel to improve throughput.   
- Quick recovery from any network issues — Smaller part size minimizes the impact of  restarting a failed upload due to a network error.   
- Pause and resume object uploads — Upload object parts over time. Once a multipart  upload is initiated a multipart upload there is no expiry; it must explicitly complete or the multipart upload has to be aborted. 
- Begin an upload before the final object size is known — An object can be uploaded as it is being created.  

Due to the additional complexity of multipart transfers, it is recommended to use appropriate S3 libraries, tools, or SDKs that offer support for managed multipart transfers:
- [IBM COS SDK for Java](https://github.com/IBM/ibm-cos-sdk-java)
- [IBM COS SDK for Python](https://github.com/IBM/ibm-cos-sdk-python)
- [IBM COS SDK for Javascript (Node.js)](https://github.com/IBM/ibm-cos-sdk-js)
- [IBM COS SDK for Go](https://github.com/IBM/ibm-cos-sdk-go)
- [IBM COS Plug-in for IBM Cloud CLI](https://github.com/IBM/ibmcloud-cos-cli)
- [S3FS-FUSE](https://github.com/s3fs-fuse/s3fs-fuse)

While there is no dedicated API for a multipart download, it is possible to use a `Range` header in a `GET` request to read only a specific part of an object, and many ranged reads can be issued in parallel, just like when uploading parts. After all parts have been downloaded, they can be concatenated and the complete object can be checked for integrity. As mentioned previously, use of SDKs or other tooling is recommended to avoid the complexities of manually managing these transfers.

Workflows that need to store large numbers of very small objects may be better served by aggregating the small files into a larger data structure (such as [Parquet].

For objects greater than 200mb in size, especially in less stable networks or over very long distances where packet loss is a concern, Aspera High-Speed Transfer can deliver excellent performance.  Aspera transfers can also upload nested directory structures efficiently within a single request.

## Throttling batch deletes
{: #performance-io-throttle}

The S3 API provides a mechanism for deleting up to 1,000 objects with a single batch delete request. It is recommended to throttle these requests client-side in order to minimize the chances of derogatory performance within the COS System.  When the number of deletes issued is too high for the system, the client will receive HTTP 503 errors with an error message indicating "slow down".  


## Consistency impacts
{: #performance-io-consistency}

IBM Cloud Object Storage System guarantees immediate consistency for all object operations, which includes object writes, overwrites, deletes, multipart operations, and ACL modifications. Bucket creation is also immediately consistent.  Bucket metadata and configuration is eventually consistent, as is the case with other object storage systems, meaning that changes across a highly distributed system may not be synchronized for a short period of time. This occurs as a result of metadata caching that provides significant performance benefits, and also safeguards against the possibility of denial-of-service attacks. 
Some applications will overwrite the same object, or delete and rewrite the same object repeatedly over a short amount of time. This can cause contention in the indices within the COS System and should be avoided.  In the rare case where overwriting data with the same object key (name) at a very high frequency and over extended periods of time is a critical aspect of an application design, a different storage platform (file, block, noSQL, etc) may be a better choice.

## Existence checks
{: #performance-io-existence}

Applications may want to check if an object exists or has been modified before writing to it.  Often this leads to inefficient application logic that will send a HEAD request followed by a PUT or GET request.  This anti-pattern results in wasted networking and server resources, and should be discouraged.  Instead of using a HEAD request as an existence check within some function, use a conditional request header.  These standard HTTP headers will compare MD5 hashes or timestamps to determine whether the data operation should proceed or not.  For more information, see Conditional Requests.

## Using conditional requests
{: #performance-io-conditional}

When making a request to read or write data, it is possible to set conditions on that request to avoid unnecessary operations. This is accomplished using the following pre-conditional HTTP headers: `If-Match`, `If-None-Match`, `If-Modified-Since`, and `If-Unmodified-Since`.

It is generally preferable to use `If-Match` because the granularity of the `Last-Modified` value is only in seconds, and may not be sufficient to avoid race conditions in some applications.

### Using If-Match
{: #performance-io-conditional-if-match}

On an object `PUT`, `HEAD`, or `GET` request, the `If-Match` header will check to see if a provided Etag (MD5 hash of the object content) matches the provided Etag value. If this value matches, the operation will proceed. If the match fails, the system will return a `412 Precondition Failed` error.

> If-Match is most often used with state-changing methods (e.g., POST, PUT, DELETE) to prevent accidental overwrites when multiple user agents might be acting in parallel on the same resource (i.e., to prevent the "lost update" problem).

### Using If-None-Match
{: #performance-io-conditional-if-none-match}

On an object `PUT`, `HEAD`, or `GET` request, the `If-None-Match` header will check to see if a provided Etag (MD5 hash of the object content) matches the provided Etag value. If this value does not match, the operation will proceed. If the match succeeds, the system will return a `412 Precondition Failed` error on a `PUT` and a `304 Not Modified` on `GET` or `HEAD`.

> If-None-Match is primarily used in conditional GET requests to enable efficient updates of cached information with a minimum amount of transaction overhead. When a client desires to update one or more stored responses that have entity-tags, the client SHOULD generate an If-None-Match header field containing a list of those entity-tags when making a GET request; this allows recipient servers to send a 304 (Not Modified) response to indicate when one of those stored responses matches the selected representation.

### Using If-Modified-Since
{: #performance-io-conditional-if-modified}

On an object `HEAD` or `GET` request, the `If-Modified-Since` header will check to see if the object's `Last-Modified` value (for example `Sat, 14 March 2020 19:43:31 GMT`) is newer than a provided value. If the object has been modified, the operation will proceed. If the object has not been modified, the system will return a `304 Not Modified`.

> If-Modified-Since is typically used for two distinct purposes: 1) to allow efficient updates of a cached representation that does not have an Etag and 2) to limit the scope of a web traversal to resources that have recently changed.

### Using If-Unmodified-Since
{: #performance-io-conditional-if-unmodified}

On an object `PUT`, `HEAD`, or `GET` request, the `If-Unmodified-Since` header will check to see if the object's `Last-Modified` value (for example `Sat, 14 March 2020 19:43:31 GMT`) is equal to or earlier than a provided value. If the object has not been modified, the operation will proceed. If the `Last-Modified` value is more recent, the system will return a `412 Precondition Failed` error on a `PUT` and a `304 Not Modified` on `GET` or `HEAD`.

> If-Unmodified-Since is most often used with state-changing methods (e.g., POST, PUT, DELETE) to prevent accidental overwrites when multiple user agents might be acting in parallel on a resource that does not supply entity-tags with its representations (i.e., to prevent the "lost update" problem). It can also be used with safe methods to abort a request if the selected representation does not match one already stored (or partially stored) from a prior request.

## Retry strategy
{: #performance-io-retry}

While most libraries and SDKs will automatically handle retry logic, care must be taken when writing software that uses the API directly to properly handle transient errors. Most importantly, it is critical to provide appropriate retry logic that implements exponential back-off when receiving 503 errors. 

## Cypher tuning
{: #performance-io-tuning}

IBM COS supports a variety of Cipher settings to encrypt data in transit. Not all cipher settings yield the same level performance and using TLS in general leads to small performance degradation. The following cipher settings are recommended (in descending order of priority): 
- `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384` 
- `TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256` 
- `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` 
- `TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA` 
- `TLS_RSA_WITH_AES_256_CBC_SHA256` 
- `TLS_RSA_WITH_AES_128_CBC_SHA256` 
- `TLS_RSA_WITH_AES_256_CBC_SHA` 
- `TLS_RSA_WITH_AES_128_CBC_SHA` 
