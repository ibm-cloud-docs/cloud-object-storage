---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# 公共头和错误代码
{: #compatibility-common}

## 公共请求头
{: #compatibility-request-headers}

下表描述了支持的公共请求头。如果在请求中发送下表中未列出的任何公共头，{{site.data.keyword.cos_full}} 会忽略这些头，不过某些请求可能支持其他头，如本文档中所定义。

|头|注释|
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
|Authorization|**必需**，适用于所有请求（OAuth2 `bearer` 令牌）。|
|ibm-service-instance-id|**必需**，适用于创建或列出存储区的请求。|
|Content-MD5|有效内容的 Base64 编码的 128 位 MD5 散列，用作完整性检查，可确保有效内容在传输中未变更。|
|Expect|值 `100-continue` 表示在发送有效内容之前，将等待系统确认头是否正确。|
|host|端点或“虚拟主机”，语法为 `{bucket-name}.{endpoint}`。通常，系统会自动添加此头。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。| 
|Cache-Control|可用于指定请求/应答链中的高速缓存行为。有关更多信息，请转至 http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9|

### 定制元数据
{: #compatibility-headers-metadata}

使用对象存储器的优点是能够通过将键值对作为头发送来添加定制元数据。这些头的格式为 `x-amz-meta-{KEY}`。请注意，与 AWS S3 不同的是，IBM COS 会将具有相同元数据键的多个头组合成值的逗号分隔列表。

## 公共响应头
{: #compatibility-response-headers}

下表描述了公共响应头。

|头|注释|
|------------------|-----------------------------------------------------|
|Content-Length|请求主体的长度（以字节为单位）。|
|Connection|指示连接是打开还是关闭。|
|Date|请求的时间戳记。|
|ETag|请求的 MD5 散列值。|
|Server|响应服务器的名称。|
|X-Clv-Request-Id|为每个请求生成的唯一标识。|

### 生命周期响应头
{: #compatibility-lifecycle-headers}

下表描述了归档对象的响应头

|头|注释|
|------------------|-----------------------------------------------------|
|x-amz-restore|对象已复原或正在进行复原时，会包含此头。|
|x-amz-storage-class|已归档或临时复原时，会返回 `GLACER`。|
|x-ibm-archive-transition-time|返回安排对象以转换到归档层的日期和时间。|
|x-ibm-transition|对象具有转换元数据时，会包含此头，并返回转换的层和原始转换时间。|
|x-ibm-restored-copy-storage-class|对象处于 `RestoreInProgress` 或 `Restored` 状态时，会包含此头，并返回存储区的存储类。|

## 错误代码
{: #compatibility-errors}

|错误代码|描述|HTTP 状态码|
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
|AccessDenied|访问被拒绝|403 已禁止|
|BadDigest|指定的 Content-MD5 与收到的内容不匹配。|400 错误请求|
|BucketAlreadyExists|请求的存储区名称不可用。存储区名称空间由系统的所有用户共享。请选择其他名称，然后重试。|409 冲突|
|BucketAlreadyOwnedByYou|先前创建指定存储区的请求已成功，并且您已拥有该存储区。|409 冲突|
|BucketNotEmpty|您尝试删除的存储区不为空。|409 冲突|
|CredentialsNotSupported|此请求不支持凭证。|400 错误请求|
|EntityTooSmall|建议的上传小于允许的最小对象大小。|400 错误请求|
|EntityTooLarge|建议的上传超过允许的最大对象大小。|400 错误请求|
|IncompleteBody|未提供 Content-Length HTTP 头指定的字节数。|400 错误请求|
|IncorrectNumberOfFilesInPostRequest|POST 要求每个请求只能上传一个文件。|400 错误请求|
|InlineDataTooLarge|内联数据超过允许的最大大小。|400 错误请求|
|InternalError|遇到内部错误。请重试。|500 内部服务器错误|
|InvalidAccessKeyId|提供的 AWS 访问密钥标识在我们的记录中不存在。|403 已禁止|
|InvalidArgument|自变量无效|400 错误请求|
|InvalidBucketName|指定的存储区无效。|400 错误请求|
|InvalidBucketState|对于当前状态的存储区，请求无效。|409 冲突|
|InvalidDigest|指定的 Content-MD5 无效。|400 错误请求|
|InvalidLocationConstraint|指定的位置约束无效。有关区域的更多信息，请参阅“如何为存储区选择区域”。|400 错误请求|
|InvalidObjectState|对于当前状态的存储区，操作无效。|403 已禁止|
|InvalidPart|找不到一个或多个指定分块。该分块可能未上传，或者指定的实体标记可能与该分块的实体标记不匹配。|400 错误请求|
|InvalidPartOrder|分块列表未按升序排列。分块列表必须按分块号顺序指定。|400 错误请求|
|InvalidRange|无法满足请求的范围。|416 无法满足请求的范围|
|InvalidRequest|请使用 AWS4-HMAC-SHA256。|400 错误请求|
|InvalidSecurity|提供的安全凭证无效。|403 已禁止|
|InvalidURI|无法解析指定的 URI。|400 错误请求|
|KeyTooLong|密钥太长。|400 错误请求|
|MalformedPOSTRequest|POST 请求的主体不是格式正确的 multipart/form-data。|400 错误请求|
|MalformedXML|提供的 XML 格式不正确，或者未根据发布的模式进行验证。|400 错误请求|
|MaxMessageLengthExceeded|请求太大。|400 错误请求|
|MaxPostPreDataLengthExceededError|上传文件前面的 POST 请求字段太大。|400 错误请求|
|MetadataTooLarge|元数据头超过允许的最大元数据大小。|400 错误请求|
|MethodNotAllowed|不允许对此资源使用指定的方法。|405 不允许使用此方法|
|MissingContentLength|必须提供 Content-Length HTTP 头。|411 需要长度|
|MissingRequestBodyError|用户将空 XML 文档作为请求发送时，会发生此错误。错误消息为：“请求主体为空。”|400 错误请求|
|NoSuchBucket|指定的存储区不存在。|404 找不到|
|NoSuchKey|指定的密钥不存在。|404 找不到|
|NoSuchUpload|指定的分块上传不存在。上传标识可能无效，或者分块上传可能已中止或完成。|404 找不到|
|NotImplemented|提供的头暗含未实现的功能。|501 未实现|
|OperationAborted|当前正在针对此资源执行冲突的条件操作。请重试。|409 冲突|
|PreconditionFailed|在指定的前置条件中，至少不满足其中一个前置条件。|412 前置条件失败|
|Redirect|临时重定向。|307 已临时移动|
|RequestIsNotMultiPartContent|存储区 POST 必须是机柜类型 multipart/form-data。|400 错误请求|
|RequestTimeout|在超时时间段内，未对与服务器的套接字连接执行读取或写入操作。|400 错误请求|
|RequestTimeTooSkewed|请求时间与服务器时间的差异太大。|403 已禁止|
|ServiceUnavailable|请求速率下降。|503 服务不可用|
|SlowDown|请求速率下降。|503 速度慢|
|TemporaryRedirect|DNS 更新期间，将您重定向到存储区。|307 已临时移动|
|TooManyBuckets|尝试创建的存储区多于允许的存储区数。|400 错误请求|
|UnexpectedContent|此请求不支持内容。|400 错误请求|
|UserKeyMustBeSpecified|存储区 POST 必须包含指定的字段名。如果已指定，请检查字段的顺序。|400 错误请求|
