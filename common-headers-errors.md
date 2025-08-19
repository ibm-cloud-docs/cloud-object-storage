---

copyright:
  years: 2017, 2025
lastupdated: "2025-08-19"

keywords: metadata, reference, api

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}


# Common headers and error codes
{: #compatibility-common}

Data transfers use many standard protocols and have unique requirements. Keep up-to-date with the reference to common headers and some error codes.
{: shortdesc}

## Common Request Headers
{: #compatibility-request-headers}

The following table describes supported common request headers. {{site.data.keyword.cos_full}} ignores any common headers that are not listed below if sent as part of a request, although some requests might support extra headers as defined in this document.

| Header                  | Note                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Authorization           | **Required** for all requests (OAuth2 `bearer` token).                                                                            |
| `ibm-service-instance-id` | **Required** for requests to create or list buckets.                                                                              |
| `Content-MD5`             | The base64 encoded 128-bit binary MD5 hash of the payload, which is used as an integrity check to ensure that the payload was not altered in transit. The base64 encoding must be performed on the binary output of the MD5 hash, not the hexadecimal representation. |
| `x-amz-checksum-crc32` | This header is the Base64 encoded, 32-bit CRC32 checksum of the object. |
| `x-amz-checksum-crc32c` | This header is the Base64 encoded, 32-bit CRC32C checksum of the object.|
| `x-amz-checksum-crc64nvme` | This header is the Base64 encoded, 64-bit CRC64NVME checksum of the object. The CRC64NVME checksum is always a full object checksum. |
| `x-amz-checksum-sha1` | This header is the Base64 encoded, 160-bit SHA1 digest of the object. |
| `x-amz-checksum-sha256` | This header is the Base64 encoded, 256-bit SHA256 digest of the object. |
| `x-amz-sdk-checksum-algorithm` | Indicates the algorithm used to create the checksum for the object when using the SDK. |
| `Expect`                  | The value `100-continue` waits for acknowledgment from the system that the headers are appropriate before sending the payload. |
| `host`                    | Either the endpoint or the 'virtual host' syntax of `{bucket-name}.{endpoint}`. Typically, this header is automatically added. For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)    |
| `Cache-Control` | Can be used to specify caching behavior along the request/reply chain. For more information, go to http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 |

### Custom metadata
{: #compatibility-headers-metadata}

A benefit of using Object Storage is the ability to add custom metadata by sending key-value pairs as headers. These headers take the form of `x-amz-meta-{KEY}`. Note that unlike AWS S3, {{site.data.keyword.cos_full_notm}} combines multiple headers with the same metadata key into a comma-separated list of values.

## Common Response Headers
{: #compatibility-response-headers}

The following table describes common response headers.

| Header           | Note                                                |
|------------------|-----------------------------------------------------|
| `Content-Length`   | The length of the request body in bytes.           |
| `Connection`       | Indicates whether the connection is open or closed. |
| `Date`             | Timestamp of the request.                          |
| `ETag`             | MD5 hash value of the request.                     |
| `Server`           | Name of the responding server.                     |
| `X-Clv-Request-Id` | Unique identifier generated per request.           |

### Lifecycle Response Headers
{: #compatibility-lifecycle-headers}

The following table describes response headers for archived objects

| Header           | Note                                                |
|------------------|-----------------------------------------------------|
|`x-amz-restore`|Included if the object has been restored or if a restoration is in progress.|
|`x-amz-storage-class`|Returns `GLACIER` or `ACCELERATED` if archived or temporarily restored.|
|`x-ibm-archive-transition-time`|Returns the date and time when the object is scheduled to transition to the archive tier.|
|`x-ibm-transition`|Included if the object has transition metadata and returns the tier and original time of transition.|
|`x-ibm-restored-copy-storage-class`|Included if an object is in the `RestoreInProgress` or `Restored` states and returns the storage class of the bucket.|

## Error Codes
{: #compatibility-errors}

| Error Code                          | Description                                                                                                                                                             | HTTP Status Code                    |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | Access Denied                                                                                                                                                           | 403 Forbidden                       |
| BadDigest                           | The Content-MD5 that you specified did not match what we received.                                                                                                          | 400 Bad Request                     |
| BucketAlreadyExists                 | The requested bucket name isn't available. The bucket namespace is shared by all users of the system. Please select a different name and try again.                   | 409 Conflict                        |
| BucketAlreadyOwnedByYou             | Your previous request to create the named bucket that is succeeded and you already own it.                                                                                     | 409 Conflict                        |
| BucketNotEmpty                      | The bucket that you tried to delete isn't empty.                                                                                                                           | 409 Conflict                        |
| CredentialsNotSupported             | This request does not support credentials.                                                                                                                             | 400 Bad Request                     |
| EntityTooSmall                      | Your proposed upload is smaller than the minimum allowed object size.                                                                                                  | 400 Bad Request                     |
| EntityTooLarge                      | Your proposed upload exceeds the maximum allowed object size.                                                                                                          | 400 Bad Request                     |
| IncompleteBody                      | You did not provide the number of bytes specified by the Content-Length HTTP header.                                                                                   | 400 Bad Request                     |
| IncorrectNumberOfFilesInPostRequest | POST requires exactly one file upload per request.                                                                                                                     | 400 Bad Request                     |
| InlineDataTooLarge                  | Inline data exceeds the maximum allowed size.                                                                                                                          | 400 Bad Request                     |
| InternalError                       | We encountered an internal error. Please try again.                                                                                                                    | 500 Internal Server Error           |
| InvalidAccessKeyId                  | The AWS access key Id that you provided does not exist in our records.                                                                                                      | 403 Forbidden                       |
| InvalidArgument                     | Invalid Argument                                                                                                                                                        | 400 Bad Request                     |
| InvalidBucketName                   | The specified bucket is not valid.                                                                                                                                     | 400 Bad Request                     |
| InvalidBucketState                  | The request is not valid with the current state of the bucket.                                                                                                         | 409 Conflict                        |
| InvalidDigest                       | The Content-MD5 that you specified is not valid.                                                                                                                            | 400 Bad Request                     |
| InvalidLocationConstraint           | The specified location constraint is not valid. For more information about regions, see How to Select a Region for Your Buckets.                                       | 400 Bad Request                     |
| InvalidObjectState                  | The operation is not valid for the current state of the object.                                                                                                        | 403 Forbidden                       |
| InvalidPart                         | One or more of the specified parts might not be found. The part might not have been uploaded, or the specified entity tag might not have matched the part's entity tag. | 400 Bad Request                     |
| InvalidPartOrder                    | The list of parts was not in ascending order. Parts list must specified in order by part number.                                                                       | 400 Bad Request                     |
| InvalidRange                        | The requested range cannot be satisfied.                                                                                                                               | 416 Requested Range Not Satisfiable |
| InvalidRequest                      | Please use AWS4-HMAC-SHA256.                                                                                                                                           | 400 Bad Request                     |
| InvalidSecurity                     | The provided security credentials are not valid.                                                                                                                       | 403 Forbidden                       |
| InvalidURI                          | Mightn't parse the specified URI.                                                                                                                                      | 400 Bad Request                     |
| KeyTooLong                          | Your key is too long.                                                                                                                                                  | 400 Bad Request                     |
| MalformedPOSTRequest                | The body of your POST request is not well-formed multipart/form-data.                                                                                                  | 400 Bad Request                     |
| MalformedXML                        | The XML you provided was not well-formed or did not validate against our published schema.                                                                             | 400 Bad Request                     |
| MaxMessageLengthExceeded            | Your request was too large.                                                                                                                                              | 400 Bad Request                     |
| MaxPostPreDataLengthExceededError   | Your POST request fields preceding the upload file were too large.                                                                                                     | 400 Bad Request                     |
| MetadataTooLarge                    | Your metadata headers exceed the maximum allowed metadata size.                                                                                                        | 400 Bad Request                     |
| MethodNotAllowed                    | The specified method is not allowed against this resource.                                                                                                             | 405 Method Not Allowed              |
| MissingContentLength                | You must provide the Content-Length HTTP header.                                                                                                                       | 411 Length Required                 |
| MissingRequestBodyError             | This happens when the user sends an empty xml document as a request. The error message is, "Request body is empty."                                                     | 400 Bad Request                     |
| NoSuchBucket                        | The specified bucket does not exist.                                                                                                                                   | 404 Not Found                       |
| NoSuchKey                           | The specified key does not exist.                                                                                                                                      | 404 Not Found                       |
| NoSuchUpload                        | The specified multipart upload does not exist. The upload ID might be invalid, or the multipart upload might have been aborted or completed.                           | 404 Not Found                       |
| NotImplemented                      | A header that you provided implies functionality that is not implemented.                                                                                                   | 501 Not Implemented                 |
| OperationAborted                    | A conflicting conditional operation is currently in progress against this resource. Try again.                                                                         | 409 Conflict                        |
| PreconditionFailed                  | At least one of the preconditions you specified did not hold.                                                                                                          | 412 Precondition Failed             |
| Redirect                            | Temporary redirect.                                                                                                                                                    | 307 Moved Temporarily               |
| RequestIsNotMultiPartContent        | Bucket POST must be of the enclosure-type multipart/form-data.                                                                                                         | 400 Bad Request                     |
| RequestTimeout                      | Your socket connection to the server was not read from or written to within the timeout period.                                                                        | 400 Bad Request                     |
| RequestTimeTooSkewed                | The difference between the request time and the server's time is too large.                                                                                            | 403 Forbidden                       |
| ServiceUnavailable                  | Reduce your request rate.                                                                                                                                              | 503 Service Unavailable             |
| SlowDown                            | Reduce your request rate.                                                                                                                                              | 503 Slow Down                       |
| TemporaryRedirect                   | You are being redirected to the bucket while DNS updates.                                                                                                              | 307 Moved Temporarily               |
| TooManyBuckets                      | You have attempted to create more buckets than allowed.                                                                                                                | 400 Bad Request                     |
| UnexpectedContent                   | This request does not support content.                                                                                                                                 | 400 Bad Request                     |
| UserKeyMustBeSpecified              | The bucket POST must contain the specified field name. If it is specified, check the order of the fields.                                                              | 400 Bad Request                     |
