---

copyright:
  years: 2017, 2018, 2019
lastupdated: "26-06-2019"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:note: .note}

# 使用 IBM Cloud CLI
{: #ic-use-the-ibm-cli}

Cloud Object Storage 插件通过可使用 Object Storage 资源的 API 包装器来扩展 IBM Cloud 命令行界面 (CLI)。

## 先决条件
{: #ic-prerequisites}
* [IBM Cloud](https://cloud.ibm.com/) 帐户
* [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision) 的实例
* [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## 安装和配置
{: #ic-installation}

该插件与在 64 位处理器上运行的 Windows、Linux 和 macOS 操作系统相兼容。

使用 `plugin install` 命令来安装插件。

```
ibmcloud plugin install cloud-object-storage
```

安装插件后，可以使用 [`ibmcloud cos config`](#configure-the-program) 命令来配置该插件。此命令可用于使用凭证和缺省下载位置来填充插件，并可选择认证等。

通过该程序，还能够设置下载文件的缺省本地目录以及设置缺省区域。要设置缺省下载位置，请输入 `ibmcloud cos config ddl` 并在程序中输入有效的文件路径。要设置缺省区域，请输入 `ibmcloud cos config region`，并向程序输入区域代码，例如 `us-south`。缺省情况下，此值设置为 `us-geo`。


如果要使用 IAM 认证，那么必须提供 CRN 才可使用某些命令。要设置 CRN，可以输入 `ibmcloud cos config crn` 并提供 CRN。您可以通过 `ibmcloud resource service-instance INSTANCE_NAME` 找到 CRN。或者，可以打开基于 Web 的控制台，选择侧边栏中的**服务凭证**，然后创建一组新的凭证（或查看已创建的现有凭证文件）。

可以通过执行 `ibmcloud cos config list` 来查看当前 Cloud Object Storage 凭证。由于配置文件是由插件生成的，因此最好不要手动编辑该文件。

### HMAC 凭证
{: #ic-hmac-credentials}

如果需要，可以使用[服务标识的 HMAC 凭证](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac)来代替 API 密钥。运行 `ibmcloud cos config hmac` 来输入 HMAC 凭证，然后使用 `ibmcloud cos config auth` 切换授权方法。

如果选择使用您自己的 API 密钥进行令牌认证，那么无需提供任何凭证，因为程序会自动对您进行认证。
{: note}

要在 HMAC 和 IAM 认证之间进行切换，可以随时输入 `ibmcloud cos config auth`。有关 IBM Cloud 中的认证和授权的更多信息，请参阅 [Identity and Access Management 文档](/docs/iam?topic=iam-iamoverview)。

## 命令索引
{: #ic-command-index}

|命令|  |  |
| --- | --- | --- |
| [`abort-multipart-upload`](#abort-a-multipart-upload) | [`complete-multipart-upload`](#complete-a-multipart-upload) | [`config`](#configure-the-program) |
| [`copy-object`](#copy-object-from-bucket) | [`create-bucket`](#create-a-new-bucket) | [`create-multipart-upload`](#create-a-new-multipart-upload) |
| [`delete-bucket`](#delete-an-existing-bucket) | [`delete-bucket-cors`](#delete-bucket-cors) | [`delete-object`](#delete-an-object) |
| [`delete-objects`](#delete-multiple-objects) | [`download`](#download-objects-using-s3manager) | [`get-bucket-class`](#get-a-buckets-class) | 
| [`get-bucket-cors`](#get-bucket-cors) | [`get-bucket-location`](#find-a-bucket) | [`get-object`](#download-an-object) |
| [`head-bucket`](#get-a-buckets-headers) | [`head-object`](#get-an-objects-headers) | [`list-buckets`](#list-all-buckets) | 
| [`list-buckets-extended`](#extended-bucket-listing) | [`list-multipart-uploads`](#list-in-progress-multipart-uploads) | [`list-objects`](#list-objects) |
| [`list-parts`](#list-parts) | [`put-bucket-cors`](#set-bucket-cors) | [`put-object`](#upload-an-object) |
| [`upload`](#upload-objects-using-s3manager) | [`upload-part`](#upload-a-part) | [`upload-part-copy`](#upload-a-part-copy) |
| [`wait`](#wait) |  |  |

下面列出的每个操作都包含其用途、使用方法以及任何可选参数或必需参数的说明。除非指定为可选，否则列出的任何参数都是必需的。

对于 Object Storage 中提供的全套功能，CLI 插件并非全部支持，例如不支持 Aspera 高速传输、不可变对象存储器、创建 Key Protect 存储区或存储区防火墙。
{: note}

### 中止分块上传
{: #ic-abort-multipart-upload}
* **操作：**通过结束到用户 IBM Cloud Object Storage 帐户中存储区的上传操作，中止分块上传实例。
* **用法：**`ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* 标识分块上传的上传标识。
		* 标志：`--upload-id ID`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 完成分块上传
{: #ic-complete-multipart-upload}
* **操作：**通过组合当前上传的分块并将组合后的文件上传到用户 IBM Cloud Object Storage 帐户中的存储区，完成分块上传实例。
* **用法：**`ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* 标识分块上传的上传标识。
		* 标志：`--upload-id ID`
	* 要设置的分块上传的结构 (STRUCTURE)。
		* 标志：`--multipart-upload STRUCTURE`
		* 简写语法：  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* JSON 语法：  
	`--multipart-upload file://<filename.json>`  
	`--multipart-upload` 命令采用 JSON 结构来描述分块上传中应该组合成完整文件的分块。在此示例中，`file://` 前缀用于从指定的文件装入 JSON 结构。
		```
			{
  			"Parts": [
    			{
     			 "ETag": "string",
     			 "PartNumber": integer
    			}
    			...
  				]
			}
		```
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


## 手动控制分块上传
{: #ic-manual-multipart-uploads}

IBM Cloud Object Storage CLI 提供了用户使用分块上传功能通过多个分块上传大型文件的功能。要启动新的分块上传，请运行 `create-multipart-upload` 命令，这将返回新的上传实例的上传标识。要继续上传过程，必须保存上传标识以用于每个后续命令。

运行 `complete-multipart-upload` 命令后，对要上传的每个文件分块运行 `upload-part`。**对于分块上传，每个文件分块（最后一个分块除外）的大小必须至少为 5 MB。**要将文件拆分为单独的分块，可以在终端窗口中运行 `split`。例如，如果在桌面上有一个名为 `TESTFILE` 的 13 MB 文件，并且您希望将其拆分成多个文件分块，每个分块 5 MB，那么可以运行 `split -b 3m ~/Desktop/TESTFILE part-file-`。此命令会生成三个文件分块，其中两个文件分块各自为 5 MB，另一个文件分块为 3 MB，名称分别为 `part-file-aa`、`part-file-ab` 和 `part-file-ac`。

上传每个文件分块时，CLI 会显示其 ETag。必须将此 ETag 与分块号一起保存到已设置格式的 JSON 文件中。使用以下模板可创建您自己的 ETag JSON 数据文件。

```
{
  "Parts": [
    {
      "PartNumber": 1,
      "ETag": "The ETag of the first file part goes here."
    },
    {
      "PartNumber": 2,
      "ETag": "The ETag of the second file part goes here."
    }
  ]
}
```

根据需要，向此 JSON 模板添加更多条目。

要查看分块上传实例的状态，可以随时运行 `upload-part` 命令，并提供存储区名称、密钥和上传标识。这将显示有关分块上传实例的原始信息。上传文件的每个分块完成后，运行带有必需参数的 `complete-multipart-upload` 命令。如果一切顺利，您会收到确认消息，指示文件已成功上传到所需存储区。

### 配置程序
{: #ic-config}
* **操作：**配置程序的首选项。
* **用法：**`ibmcloud cos config [COMMAND]`
* **命令：**
	* 在 HMAC 和 IAM 认证之间进行切换。
		* 命令：`auth`
	* 将 CRN 存储在配置中。
		* 命令：`crn`
	* 将缺省下载位置存储在配置中。
		* 命令：`ddl`
	* 将 HMAC 凭证存储在配置中。
		* 命令：`hmac`
	* 列出配置。
		* 命令：`list`
	* 将缺省区域存储在配置中。
		* 命令：`region`
	* 在 VHost 和路径 URL 样式之间进行切换。
		* 命令：`url-style`


### 从存储区复制对象
{: #ic-copy-object}
* **操作：**将对象从源存储区复制到目标存储区。
* **用法：**`ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* (SOURCE) 源存储区的名称和源对象的键名，以斜杠 (/) 分隔。必须为 URL 编码形式。
		* 标志：`--copy-source SOURCE`
	* _可选_：为请求和回复链指定 `CACHING_DIRECTIVES`。
		* 标志：`--cache-control CACHING_DIRECTIVES`
	* _可选_：指定呈现信息 (`DIRECTIVES`)。
		* 标志：`--content-disposition DIRECTIVES`
	* _可选_：指定应用于对象的内容编码 (CONTENT_ENCODING)，以及为了获取 Content-Type 头字段引用的媒体类型而必须应用的解码机制。
		* 标志：`--content-encoding CONTENT_ENCODING`
	* _可选_：内容使用的语言 (LANGUAGE)。
		* 标志：`--content-language LANGUAGE`
	* _可选_：描述对象数据格式的标准 MIME 类型。
		* 标志：`--content-type MIME`
	* _可选_：对象的实体标记 (Etag) 与指定标记 (ETAG) 相匹配时，复制该对象。
		* 标志：`--copy-source-if-match ETAG`
	* _可选_：对象自指定时间 (TIMESTAMP) 以来已修改时，复制该对象。
		* 标志：`--copy-source-if-modified-since TIMESTAMP`
	* _可选_：对象的实体标记 (ETag) 与指定的标记 (ETAG) 不同时，复制该对象。
		* 标志：`--copy-source-if-none-match ETAG`
	* _可选_：对象自指定时间 (TIMESTAMP) 以来未修改时，复制该对象。
		* 标志：`--copy-source-if-unmodified-since TIMESTAMP`
	* _可选_：要存储的元数据的映射 (MAP)。语法：KeyName1=string,KeyName2=string
		* 标志：`--metadata MAP`
	* _可选_：指定元数据是从源对象进行复制，还是替换为请求中提供的元数据。DIRECTIVE 值：COPY 或 REPLACE。
		* 标志：`--metadata-directive DIRECTIVE`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 创建新存储区
{: #ic-create-bucket}

* **操作：**在 IBM Cloud Object Storage 实例中创建存储区。
* **用法：**`ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* 请注意，如果要使用 IAM 认证，那么必须提供 CRN。这可以使用 [`ibmcloud cos config crn`](#configure-the-program) 命令进行设置。
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：类的名称。
		* 标志：`--class CLASS_NAME`
	* _可选_：在请求中设置 IBM 服务实例标识。
		* 标志：`--ibm-service-instance-id ID`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`



### 创建新的分块上传
{: #ic-create-multipart-upload}
* **操作：**通过创建新的分块上传实例来启动分块文件上传过程。
* **用法：**`ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* _可选_：为请求和回复链指定 `CACHING_DIRECTIVES`。
		* 标志：`--cache-control CACHING_DIRECTIVES`
	* _可选_：指定呈现信息 (`DIRECTIVES`)。
		* 标志：`--content-disposition DIRECTIVES`
	* _可选_：指定对象的内容编码 (`CONTENT_ENCODING`)。
		* 标志：`--content-encoding CONTENT_ENCODING`
	* _可选_：内容使用的语言 (LANGUAGE)。
		* 标志：`--content-language LANGUAGE`
	* _可选_：描述对象数据格式的标准 MIME 类型。
		* 标志：`--content-type MIME`
	* _可选_：要存储的元数据的映射 (MAP)。语法：KeyName1=string,KeyName2=string
		* 标志：`--metadata MAP`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 删除现有存储区
{: #ic-delete-bucket}

* **操作：**删除 IBM Cloud Object Storage 实例中的现有存储区。
* **用法：**`ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
    * _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
       * 标志：`--region REGION`
    * _可选_：操作不会要求确认。
       * 标志：`--force`
    * _可选_：以原始 JSON 格式返回输出。
       * 标志：`--json`


### 删除存储区 CORS
{: #ic-delete-bucket-cors}
* **操作：**删除用户 IBM Cloud Object Storage 帐户中存储区上的 CORS 配置。
* **用法：**`ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 删除一个对象
{: #ic-delete-object}
* **操作：**从用户 IBM Cloud Object Storage 帐户的存储区中删除一个对象。
* **用法：**`ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
  * _可选_：操作不会要求确认。
  	* 标志：`--force`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 删除多个对象
{: #ic-delete-objects}
* **操作：**从用户 IBM Cloud Object Storage 帐户的存储区中删除多个对象。
* **用法：**`ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。  
		* 标志：`--bucket BUCKET_NAME`  
	* 使用简写语法或 JSON 语法的结构 (STRUCTURE)。  
		* 标志：`--delete STRUCTURE`  
		* 简写语法：  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* JSON 语法：  
	`--delete file://<filename.json>`  
	`--delete` 命令采用 JSON 结构来描述分块上传中应该组合成完整文件的分块。在此示例中，`file://` 前缀用于从指定的文件装入 JSON 结构。
		```
	{
  	"Objects": [
    	{
    	"Key": "string",
    	"VersionId": "string"
    	}
    ...
  	],
  	"Quiet": true|false
	}
	```
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 使用 S3Manager 下载对象
{: #ic-download-s3manager}
* **操作：**以并行方式从 S3 下载对象。
* **用法：**`ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **要提供的参数：**
	* 存储区的名称 (BUCKET_NAME)。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* _可选_：发送分块时，每次调用上传时并行启动的 goroutine 数。缺省值为 5。
		* 标志：`--concurrency value`
	* _可选_：将数据缓冲到区块中并将其作为 S3 的分块结束时要使用的缓冲区大小 (SIZE)（以字节为单位）。允许的最小分块大小为 5 MB。
		* 标志：`--part-size SIZE`
	* _可选_：仅当对象的实体标记 (ETag) 与指定的 ETTAG 相同时，才会返回该对象，否则会返回 412（前置条件失败）。
		* 标志：`--if-match ETAG`
	* _可选_：仅当对象自指定的时间戳记 (TIMESTAMP) 以来已修改时，才会返回该对象，否则会返回 304（未修改）。
		* 标志：`--if-modified-since TIMESTAMP`
	* _可选_：仅当对象的实体标记 (ETag) 与指定的 ETTAG 不同时，才会返回该对象，否则会返回 304（未修改）。
		* 标志：`--if-none-match ETAG`
	* _可选_：仅当对象自指定的时间戳记 (TIMESTAMP) 以来未修改时，才会返回该对象，否则会返回 412（前置条件失败）。
		* 标志：`--if-unmodified-since TIMESTAMP`
	* _可选_：下载对象的指定范围 (RANGE) 字节。有关 HTTP 范围头的更多信息，请[单击此处](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35)。
		* 标志：`--range RANGE`
	* _可选_：设置响应的 Cache-Control 头 (Cache-Control HEADER)。
		* 标志：`--response-cache-control HEADER`
	* _可选_：设置响应的 Content-Disposition 头 (Content-Disposition HEADER)。
		* 标志：`--response-content-disposition HEADER`
	* _可选_：设置响应的 Content-Encoding 头 (Content-Encoding HEADER)。
		* 标志：`--response-content-encoding HEADER`
	* _可选_：设置响应的 Content-Language 头 (Content-Language HEADER)。
		* 标志：`--response-content-language HEADER`
	* _可选_：设置响应的 Content-Type 头 (Content-Type HEADER)。
		* 标志：`--response-content-type HEADER`
	* _可选_：设置响应的 Expires 头 (Expires HEADER)。
		* 标志：`--response-expires HEADER`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，那么程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`
	* _可选_：用于保存对象内容的位置。如果未提供此参数，程序将使用缺省位置。
		* 参数：`OUTFILE`


### 获取存储区的类
{: #ic-bucket-class}
* **操作：**确定 IBM Cloud Object Storage 实例中存储区的类。
* **用法：**`ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 获取存储区 CORS
{: #ic-get-bucket-cors}
* **操作：**返回用户 IBM Cloud Object Storage 帐户中存储区的 CORS 配置。
* **用法：**`ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **要提供的参数：**
  * 存储区的名称。  
    * 标志：`--bucket BUCKET_NAME`
  * _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
    * 标志：`--region REGION`
  * _可选_：以原始 JSON 格式返回输出。
    * 标志：`--json`


### 查找存储区
{: #ic-find-bucket}
* **操作：**确定 IBM Cloud Object Storage 实例中存储区的区域和类。 
* **用法：**`ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`
	


### 下载对象
{: #ic-download-object}
* **操作：**从用户 IBM Cloud Object Storage 帐户的存储区下载对象。
* **用法：**`ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* _可选_：仅当对象的实体标记 (ETag) 与指定的 ETTAG 相同时，才会返回该对象，否则会返回 412（前置条件失败）。
		* 标志：`--if-match ETAG`
	* _可选_：仅当对象自指定的时间戳记 (TIMESTAMP) 以来已修改时，才会返回该对象，否则会返回 304（未修改）。
		* 标志：`--if-modified-since TIMESTAMP`
	* _可选_：仅当对象的实体标记 (ETag) 与指定的 ETTAG 不同时，才会返回该对象，否则会返回 304（未修改）。
		* 标志：`--if-none-match ETAG`
	* _可选_：仅当对象自指定的时间戳记 (TIMESTAMP) 以来未修改时，才会返回该对象，否则会返回 412（前置条件失败）。
		* 标志：`--if-unmodified-since TIMESTAMP`
	* _可选_：下载对象的指定范围 (RANGE) 字节。
		* 标志：`--range RANGE`
	* _可选_：设置响应的 Cache-Control 头 (Cache-Control HEADER)。
		* 标志：`--response-cache-control HEADER`
	* _可选_：设置响应的 Content-Disposition 头 (Content-Disposition HEADER)。
		* 标志：`--response-content-disposition HEADER`
	* _可选_：设置响应的 Content-Encoding 头 (Content-Encoding HEADER)。
		* 标志：`--response-content-encoding HEADER`
	* _可选_：设置响应的 Content-Language 头 (Content-Language HEADER)。
		* 标志：`--response-content-language HEADER`
	* _可选_：设置响应的 Content-Type 头 (Content-Type HEADER)。
		* 标志：`--response-content-type HEADER`
	* _可选_：设置响应的 Expires 头 (Expires HEADER)。
		* 标志：`--response-expires HEADER`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`
	* _可选_：用于保存对象内容的位置。如果未提供此参数，程序将使用缺省位置。
		* 参数：`OUTFILE`


### 获取存储区的头
{: #ic-bucket-header}
* **操作：**确定某个存储区在 IBM Cloud Object Storage 实例中是否存在。
* **用法：**`ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 获取对象的头
{: #ic-object-header}
* **操作：**确定某个文件在用户 IBM Cloud Object Storage 帐户的存储区中是否存在。
* **用法：**`ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* _可选_：仅当对象的实体标记 (ETag) 与指定的 ETTAG 相同时，才会返回该对象，否则会返回 412（前置条件失败）。
		* 标志：`--if-match ETAG`
	* _可选_：仅当对象自指定的时间戳记 (TIMESTAMP) 以来已修改时，才会返回该对象，否则会返回 304（未修改）。
		* 标志：`--if-modified-since TIMESTAMP`
	* _可选_：仅当对象的实体标记 (ETag) 与指定的 ETTAG 不同时，才会返回该对象，否则会返回 304（未修改）。
		* 标志：`--if-none-match ETAG`
	* _可选_：仅当对象自指定的时间戳记 (TIMESTAMP) 以来未修改时，才会返回该对象，否则会返回 412（前置条件失败）。
		* 标志：`--if-unmodified-since TIMESTAMP`
	* 下载对象的指定范围 (RANGE) 字节。
		* 标志：`--range RANGE`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 列出所有存储区
{: #ic-list-buckets}
* **操作：**显示用户 IBM Cloud Object Storage 帐户中所有存储区的列表。存储区可能位于不同的区域中。
* **用法：**`ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* 请注意，如果要使用 IAM 认证，那么必须提供 CRN。这可以使用 [`ibmcloud cos config crn`](#configure-the-program) 命令进行设置。
* **要提供的参数：**
  * 没有要提供的参数。
	* _可选_：在请求中设置 IBM 服务实例标识。
		* 标志：`--ibm-service-instance-id`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 扩展存储区列示
{: #ic-extended-bucket-listing}
* **操作：**显示用户 IBM Cloud Object Storage 帐户中所有存储区的列表。存储区可能位于不同的区域中。
* **用法：**`ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* 请注意，如果要使用 IAM 认证，那么必须提供 CRN。这可以使用 [`ibmcloud cos config crn`](#configure-the-program) 命令进行设置。
* **要提供的参数：**
  * 没有要提供的参数。
	* _可选_：在请求中设置 IBM 服务实例标识。
		* 标志：`--ibm-service-instance-id`
	* _可选_：指定列出存储区中对象时列表起始位置的键 (KEY)。
		* 标志：`--marker KEY`
	* _可选_：将响应限制为以指定前缀 (PREFIX) 开头的键。
		* 标志：`--prefix PREFIX`
	* _可选_：要在服务调用中获取的每个页面的大小 (SIZE)。这不会影响命令输出中返回的项数。设置较小的页面大小会导致对服务的调用增多，因而在每个调用中检索的项数减少。这可帮助防止服务调用超时。
		* 标志：`--page-size SIZE`
	* _可选_：命令输出中要返回的项总数 (NUMBER)。
		* 标志：`--max-items NUMBER`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 列出进行中的分块上传
{: #ic-list-multipart-uploads}
* **操作：**列出进行中的分块上传。
* **用法：**`ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：定界符 (DELIMITER) 是用于对键分组的字符。
		* 标志：`--delimiter DELIMITER`
	* _可选_：请求对响应中的对象键进行编码，并指定要使用的编码方法 (METHOD)。
		* 标志：`--encoding-type METHOD`
	* _可选_：将响应限制为以指定前缀 (PREFIX) 开头的键。
		* 标志：`--prefix PREFIX`
	* _可选_：此参数与 upload-id-marker 一起用于指定列表起始位置的分块上传。
		* 标志：`--key-marker value`
	* _可选_：此参数与 key-marker 一起用于指定列表起始位置的分块上传。如果未指定 key-marker，那么将忽略 upload-id-marker 参数。
		* 标志：`--upload-id-marker value`
	* _可选_：要在服务调用中获取的每个页面的大小 (SIZE)。这不会影响命令输出中返回的项数。设置较小的页面大小会导致对服务的调用增多，因而在每个调用中检索的项数减少。这可帮助防止服务调用超时。（缺省值：1000）。
		* 标志：`--page-size SIZE`
	* _可选_：命令输出中要返回的项总数 (NUMBER)。如果可用项总数大于指定的值，那么将在命令的输出中提供 NextToken。要恢复分页，请在后续命令的 starting-token 自变量中提供 NextToken 值。（缺省值：0）。
		* 标志：`--max-items NUMBER`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 列出对象
{: #ic-list-objects}
* **操作：**列出用户 IBM Cloud Object Storage 帐户的存储区中存在的文件。此操作当前限制为只能列出 1000 个最近创建的对象，并且无法对这些对象进行过滤。
* **用法：**`ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：定界符 (DELIMITER) 是用于对键分组的字符。
		* 标志：`--delimiter DELIMITER`
	* _可选_：请求对响应中的对象键进行编码，并指定要使用的编码方法 (METHOD)。
		* 标志：`--encoding-type METHOD`
	* _可选_：将响应限制为以指定前缀 (PREFIX) 开头的键。
		* 标志：`--prefix PREFIX`
	* _可选_：指定分页开始位置的记号 (TOKEN)。这是先前截断的响应中的 NextToken。
		* 标志：`--starting-token TOKEN`
	* _可选_：要在服务调用中获取的每个页面的大小 (SIZE)。这不会影响命令输出中返回的项数。设置较小的页面大小会导致对服务的调用增多，因而在每个调用中检索的项数减少。这可帮助防止服务调用超时。（缺省值：1000）
		* 标志：`--page-size SIZE`
	* _可选_：命令输出中要返回的项总数 (NUMBER)。如果可用项总数大于指定的值，那么将在命令的输出中提供 NextToken。要恢复分页，请在后续命令的 starting-token 自变量中提供 NextToken 值。（缺省值：0）
		* 标志：`--max-items NUMBER`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 列出分块
{: #ic-list-parts}
* **操作：**显示有关正在进行的分块上传实例的信息。
* **用法：**`ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* 标识分块上传的上传标识。
		* 标志：`--upload-id ID`
	* 列表起始位置的分块号值（缺省值：1）
		* 标志：`--part-number-marker VALUE`
	* _可选_：要在服务调用中获取的每个页面的大小 (SIZE)。这不会影响命令输出中返回的项数。设置较小的页面大小会导致对服务的调用增多，因而在每个调用中检索的项数减少。这可帮助防止服务调用超时。（缺省值：1000）
		* 标志：`--page-size SIZE`
	* _可选_：命令输出中要返回的项总数 (NUMBER)。如果可用项总数大于指定的值，那么将在命令的输出中提供 NextToken。要恢复分页，请在后续命令的 starting-token 自变量中提供 NextToken 值。（缺省值：0）
		* 标志：`--max-items NUMBER`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 设置存储区 CORS
{: #ic-set-bucket-cors}
* **操作：**设置用户 IBM Cloud Object Storage 帐户中存储区的 CORS 配置。
* **用法：**`ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* _可选_：文件中使用 JSON 语法的结构 (STRUCTURE)。
		* 标志：`--cors-configuration STRUCTURE`
		* JSON 语法：  
	`--cors-configuration file://<filename.json>`  
	`--cors-configuration` 命令采用 JSON 结构来描述分块上传中应该组合成完整文件的分块。在此示例中，`file://` 前缀用于从指定的文件装入 JSON 结构。
		```
	{
  	"CORSRules": [
    	{
      	"AllowedHeaders": ["string", ...],
      	"AllowedMethods": ["string", ...],
      	"AllowedOrigins": ["string", ...],
      	"ExposeHeaders": ["string", ...],
      	"MaxAgeSeconds": integer
    	}
    	...
  	]
	}
	```
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`



### 放置对象
{: #ic-upload-object}
* **操作：**将对象上传到用户 IBM Cloud Object Storage 帐户的存储区。
* **用法：**`ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **要提供的参数：**
    * 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* _可选_：对象数据位置 (`FILE_PATH`)。
		* 标志：`--body FILE_PATH`
	* _可选_：为请求和回复链指定 `CACHING_DIRECTIVES`。
		* 标志：`--cache-control CACHING_DIRECTIVES`
	* _可选_：指定呈现信息 (`DIRECTIVES`)。
		* 标志：`--content-disposition DIRECTIVES`
	* _可选_：指定对象的内容编码 (`CONTENT_ENCODING`)。
		* 标志：`--content-encoding CONTENT_ENCODING`
	* _可选_：内容使用的语言 (LANGUAGE)。
		* 标志：`--content-language LANGUAGE`
	* _可选_：主体的大小 (SIZE)（以字节为单位）。在无法自动确定主体的大小时，此参数非常有用。（缺省值：0）
		* 标志：`--content-length SIZE`
	* _可选_：数据的 Base64 编码的 128 位 MD5 摘要。
		* 标志：`--content-md5 MD5`
	* _可选_：描述对象数据格式的标准 MIME 类型。
		* 标志：`--content-type MIME`
	* _可选_：要存储的元数据的映射 (MAP)。语法：KeyName1=string,KeyName2=string
		* 标志：`--metadata MAP`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 使用 S3Manager 上传对象
{: #ic-upload-s3manager}
* **操作：**以并行方式从 S3 上传对象。
* **用法：**`ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **要提供的参数：**
	* 存储区的名称 (BUCKET_NAME)。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* 要上传的文件的路径 (PATH)。
		* 标志：`--file PATH`
	* _可选_：发送分块时，每次调用上传时并行启动的 goroutine 数。缺省值为 5。
		* 标志：`--concurrency value`
	* _可选_：将上传到 S3 的最大分块数 (PARTS)，此值用于计算要上传的对象的分块大小。限制为 10,000 个分块。
		* 标志：`--max-upload-parts PARTS`
	* _可选_：将数据缓冲到区块中并将其作为 S3 的分块结束时要使用的缓冲区大小 (SIZE)（以字节为单位）。允许的最小分块大小为 5 MB。
		* 标志：`--part-size SIZE`
	* _可选_：将此值设置为 true 将使得 SDK 避免在失败时调用 AbortMultipartUpload，从而在 S3 上保留所有已成功上传的分块以便手动恢复。
		* 标志：`--leave-parts-on-errors`
	* _可选_：指定请求/回复链的 CACHING_DIRECTIVES。
		* 标志：`--cache-control CACHING_DIRECTIVES`
	* _可选_：指定呈现信息 (DIRECTIVES)。
		* 标志：`--content-disposition DIRECTIVES`
	* _可选_：指定已应用于对象的内容编码 (CONTENT_ENCODING)，以及为了获取 Content-Type 头字段引用的媒体类型而必须应用的解码机制。
		* 标志：`--content-encoding CONTENT_ENCODING`
	* _可选_：内容使用的语言 (LANGUAGE)。
		* 标志：`--content-language LANGUAGE`
	* _可选_：主体的大小 (SIZE)（以字节为单位）。在无法自动确定主体的大小时，此参数非常有用。
		* 标志：`--content-length SIZE`
	* _可选_：数据的 Base64 编码的 128 位 MD5 摘要。
		* 标志：`--content-md5 MD5`
	* _可选_：描述对象数据格式的标准 MIME 类型。
		* 标志：`--content-type MIME`
	* _可选_：要存储的元数据的映射 (MAP)。语法：KeyName1=string,KeyName2=string
		* 标志：`--metadata MAP`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，那么程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 上传分块
{: #ic-upload-part}
* **操作：**在现有分块上传实例中上传文件的分块。
* **用法：**`ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* 请注意，必须将每个上传的文件分块的编号和 ETag（CLI 将显示）保存到 JSON 文件中。有关更多信息，请参阅下面的“分块上传指南”。
* **要提供的参数：**
	* 将执行分块上传的存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* 标识分块上传的上传标识。
		* 标志：`--upload-id ID`
	* 要上传的分块的分块号 (NUMBER)。这是范围在 1 到 10,000 之间的正整数。（缺省值：1）
		* 标志：`--part-number NUMBER`
	* _可选_：对象数据位置 (`FILE_PATH`)。
		* 标志：`--body FILE_PATH`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 上传分块副本
{: #ic-upload-a-part-copy}
* **操作：**通过从现有对象复制数据来上传分块。
* **用法：**`ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* 请注意，必须将每个上传的文件分块的编号和 ETag（CLI 将显示）保存到 JSON 文件中。有关更多信息，请参阅“分块上传指南”。
* **要提供的参数：**
	* 存储区的名称。
		* 标志：`--bucket BUCKET_NAME`
	* 对象的键 (KEY)。
		* 标志：`--key KEY`
	* 标识分块上传的上传标识。
		* 标志：`--upload-id ID`
	* 要上传的分块的分块号 (NUMBER)。这是 1 到 10,000 之间的正整数。
		* 标志：`--part-number PART_NUMBER`
	* (SOURCE) 源存储区的名称和源对象的键名，以斜杠 (/) 分隔。必须为 URL 编码形式。
		* 标志：`--copy-source SOURCE`
	* _可选_：对象的实体标记 (Etag) 与指定标记 (ETAG) 相匹配时，复制该对象。
		* 标志：`--copy-source-if-match ETAG`
	* _可选_：对象自指定时间 (TIMESTAMP) 以来已修改时，复制该对象。
		* 标志：`--copy-source-if-modified-since TIMESTAMP`
	* _可选_：对象的实体标记 (ETag) 与指定的标记 (ETAG) 不同时，复制该对象。
		* 标志：`--copy-source-if-none-match ETAG`
	* _可选_：对象自指定时间 (TIMESTAMP) 以来未修改时，复制该对象。
		* 标志：`--copy-source-if-unmodified-since TIMESTAMP`
	* _可选_：要从源对象复制的字节范围。范围值必须使用格式 bytes=first-last，其中 first 和 last 是要复制的基于零的字节偏移量。例如，bytes=0-9 指示要复制源的前 10 个字节。仅当源对象大于 5 MB 时，才能复制范围。
		* 标志：`--copy-source-range value`
	* _可选_：存储区所在的区域 (REGION)。如果未提供此标志，程序将使用配置中指定的缺省选项。
		* 标志：`--region REGION`
	* _可选_：以原始 JSON 格式返回输出。
		* 标志：`--json`


### 等待
{: #ic-wait}
* **操作：**等待直到满足特定条件。每个子命令都会轮询一个 API，直到满足列出的需求。
* **用法：**`ibmcloud cos wait command [arguments...] [command options]`
* **命令：**
    * `bucket-exists`
  		* 轮询 head-bucket 时，等待直到收到 200 响应。此命令每 5 秒轮询一次，直到达到成功状态。在检查失败 20 次后，此命令会退出并显示返回码 255。
	* `bucket-not-exists`
		* 轮询 head-bucket 时，等待直到收到 404 响应。此命令每 5 秒轮询一次，直到达到成功状态。在检查失败 20 次后，此命令会退出并显示返回码 255。
	* `object-exists`
		* 轮询 head-object 时，等待直到收到 200 响应。此命令每 5 秒轮询一次，直到达到成功状态。在检查失败 20 次后，此命令会退出并显示返回码 255。
	* `object-not-exists`
		* 轮询 head-object 时，等待直到收到 404 响应。此命令每 5 秒轮询一次，直到达到成功状态。在检查失败 20 次后，此命令会退出并显示返回码 255。

