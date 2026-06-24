---

copyright:
  years: 2017, 2026
lastupdated: "2026-06-24"

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



See the API docs for a listing of [Error codes](/docs/apis/cos/cos-compatibility-f1995#compatibility-errors)
