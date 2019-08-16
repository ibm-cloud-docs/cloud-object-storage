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

Cloud Object Storage 外掛程式會以 API 封套延伸 IBM Cloud 指令行介面 (CLI)，以使用 Object Storage 資源。

## 必要條件
{: #ic-prerequisites}
* [IBM Cloud](https://cloud.ibm.com/) 帳戶
* [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision) 實例
* [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## 安裝與配置
{: #ic-installation}

外掛程式與在 64 位元處理器上執行的 Windows、Linux 及 macOS 作業系統相容。

使用 `plugin install` 指令來安裝外掛程式。

```
ibmcloud plugin install cloud-object-storage
```

安裝外掛程式之後，您可以使用 [`ibmcloud cos config`](#configure-the-program) 指令來配置外掛程式。這可用來在外掛程式中移入您的認證、預設下載位置、選擇鑑別等。

此程式也讓您可以設定已下載檔案的預設本端目錄，以及設定預設地區。若要設定預設下載位置，請鍵入 `ibmcloud cos config ddl`，並在程式中輸入有效的檔案路徑。若要設定預設地區，請鍵入 `ibmcloud cos config region`，並在程式中輸入地區碼（例如 `us-south`）。依預設，此值設為 `us-geo`。


如果您使用 IAM 鑑別，則必須提供 CRN，才能使用部分指令。若要設定 CRN，您可以鍵入 `ibmcloud cos config crn`，並提供 CRN。您可以使用 `ibmcloud resource service-instance INSTANCE_NAME` 來尋找 CRN。或者，您可以開啟 Web 型主控台，並在資訊看板中選取**服務認證**，然後建立一組新的認證（或檢視您已建立的現有認證檔）。

您可以藉由提示 `ibmcloud cos config list` 來檢視現行 Cloud Object Storage 認證。因為配置檔是由外掛程式所產生，所以最好不要手動編輯檔案。

### HMAC 認證
{: #ic-hmac-credentials}

如果願意，可以使用[服務 ID 的 HMAC 認證](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac)，而非您的 API 金鑰。請執行 `ibmcloud cos config hmac` 來輸入 HMAC 認證，然後使用 `ibmcloud cos config auth` 來切換授權方法。

如果您選擇搭配使用記號鑑別與自己的 API 金鑰，則不需要提供任何認證，因為程式會自動對您進行鑑別。
{: note}

在任何時候，若要在 HMAC 與 IAM 鑑別之間進行切換，您可以鍵入 `ibmcloud cos config auth`。如需 IBM Cloud 中鑑別及授權的相關資訊，請參閱 [Identity and Access Management 文件](/docs/iam?topic=iam-iamoverview)。

## 指令索引
{: #ic-command-index}

| 指令                  |  |  |
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

下面列出的每個作業都有關於其用途、使用方式以及任何選用參數或必要參數的說明。除非指定為選用參數，否則所有列出的參數都是必要參數。

CLI 外掛程式不支援 Object Storage 中可用的完整特性套組，例如「Aspera 高速傳送」、Immutable Object Storage、建立 Key Protect 儲存區或「儲存區防火牆」。
{: note}

### 中斷多部分上傳
{: #ic-abort-multipart-upload}
* **動作：**結束上傳至使用者 IBM Cloud Object Storage 帳戶中的儲存區，以中斷多部分上傳實例。
* **用法：**`ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* 上傳識別多部分上傳的 ID。
		* 旗標：`--upload-id ID`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 完成多部分上傳
{: #ic-complete-multipart-upload}
* **動作：**組合目前上傳的組件，並將檔案上傳至使用者 IBM Cloud Object Storage 帳戶中的儲存區，以完成多部分上傳實例。
* **用法：**`ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* 上傳識別多部分上傳的 ID。
		* 旗標：`--upload-id ID`
	* 要設定的 MultipartUpload 的 STRUCTURE。
		* 旗標：`--multipart-upload STRUCTURE`
		* 速記語法：  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* JSON 語法：  
	`--multipart-upload file://<filename.json>`  
	`--multipart-upload` 指令採用 JSON 結構，用於說明應該重新組合為完整檔案的多部分上傳的組件。在此範例中，使用 `file://` 字首，從指定的檔案中載入 JSON 結構。
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
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


## 手動控制多部分上傳
{: #ic-manual-multipart-uploads}

IBM Cloud Object Storage CLI 可讓使用者使用多部分上傳功能，在多個組件中上傳大型檔案。若要起始新的多部分上傳，請執行 `create-multipart-upload` 指令，它會傳回新上傳實例的上傳 ID。若要繼續上傳處理程序，您必須儲存每個後續指令的上傳 ID。

在執行 `complete-multipart-upload` 指令之後，請針對您要上傳的每個檔案組件執行 `upload-part`。**對於多部分上傳，每個檔案組件（最後一個組件除外）的大小都必須至少為 5 MB。**若要將檔案分割成個別組件，您可以在終端機視窗中執行 `split`。例如，如果您在「桌面」上具有名為 `TESTFILE` 的 13 MB 檔案，而且您想要將它分割成各 5 MB 的檔案組件，則可以執行 `split -b 3m ~/Desktop/TESTFILE part-file-`。這個指令會將三個檔案組件產生為各 5 MB 的兩個檔案組件，以及一個 3 MB 的檔案組件，名稱為 `part-file-aa`、`part-file-ab` 及 `part-file-ac`。

上傳每個檔案組件時，CLI 會列印出其 ETag。您必須將此 ETag 與組件號碼一起儲存為格式化 JSON 檔案。您可以使用此範本來建立自己的 ETag JSON 資料檔案。

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

視需要將更多項目新增至此 JSON 範本。

若要查看多部分上傳實例的狀態，您一律可以執行 `upload-part` 指令，提供儲存區名稱、索引鍵及上傳 ID。這會列印多部分上傳實例的原始資訊。完成上傳檔案的每個組件之後，請搭配使用必要的參數來執行 `complete-multipart-upload` 指令。如果一切順利，您會收到一個檔案已順利上傳至想要的儲存區的確認。

### 配置程式
{: #ic-config}
* **動作：**配置程式的喜好設定。
* **用法：**`ibmcloud cos config [COMMAND]`
* **指令：**
	* 在 HMAC 與 IAM 鑑別之間進行切換。
		* 指令：`auth`
	* 將 CRN 儲存在配置中。
		* 指令：`crn`
	* 將「預設下載位置」儲存在配置中。
		* 指令：`ddl`
	* 將 HMAC 認證儲存在配置中。
		* 指令：`hmac`
	* 列出配置。
		* 指令：`list`
	* 將「預設地區」儲存在配置中。
		* 指令：`region`
	* 在 VHost 與「路徑 URL」樣式之間進行切換。
		* 指令：`url-style`


### 複製儲存區中的物件
{: #ic-copy-object}
* **動作：**將來源儲存區中的物件複製到目的地儲存區。
* **用法：**`ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* (SOURCE) 來源儲存區的名稱，以及來源物件的索引鍵名稱（以斜線 (/) 區隔）。必須是 URL 編碼。
		* 旗標：`--copy-source SOURCE`
	* _選用_：指定要求及回覆鏈的 `CACHING_DIRECTIVES`。
		* 旗標：`--cache-control CACHING_DIRECTIVES`
	* _選用_：指定呈現資訊 (`DIRECTIVES`)。
		* 旗標：`--content-disposition DIRECTIVES`
	* _選用_：指定套用至物件的內容編碼 (CONTENT_ENCODING)，因此而指定必須套用以取得 Content-Type 標頭欄位所參照之 media-type 的解碼機制。
		* 旗標：`--content-encoding CONTENT_ENCODING`
	* _選用_：內容所使用的 LANGUAGE。
		* 旗標：`--content-language LANGUAGE`
	* _選用_：說明物件資料格式的標準 MIME 類型。
		* 旗標：`--content-type MIME`
	* _選用_：如果物件的實體標籤 (Etag) 符合指定的標籤 (ETAG)，則複製此物件。
		* 旗標：`--copy-source-if-match ETAG`
	* _選用_：如果物件自指定時間 (TIMESTAMP) 後經過修改，則複製此物件。
		* 旗標：`--copy-source-if-modified-since TIMESTAMP`
	* _選用_：如果物件的實體標籤 (ETag) 與指定的標籤 (ETAG) 不同，則複製此物件。
		* 旗標：`--copy-source-if-none-match ETAG`
	* _選用_：如果物件自指定時間 (TIMESTAMP) 後未經過修改，則複製此物件。
		* 旗標：`--copy-source-if-unmodified-since TIMESTAMP`
	* _選用_：要儲存的 meta 資料的 MAP。語法：KeyName1=string,KeyName2=string
		* 旗標：`--metadata MAP`
	* _選用_：指定是要從來源物件中複製 meta 資料，還是將 meta 資料取代為要求中所提供的 meta 資料。DIRECTIVE 值：COPY、REPLACE。
		* 旗標：` --metadata-directive DIRECTIVE`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 建立新的儲存區
{: #ic-create-bucket}

* **動作：**在 IBM Cloud Object Storage 實例中建立儲存區。
* **用法：**`ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* 請注意，如果您使用 IAM 鑑別，則必須提供 CRN。這可以使用 [`ibmcloud cos config crn`](#configure-the-program) 指令進行設定。
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：類別的名稱。
		* 旗標：`--class CLASS_NAME`
	* _選用_：在要求中設定「IBM 服務實例 ID」。
		* 旗標：`--ibm-service-instance-id ID`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`



### 建立新的多部分上傳
{: #ic-create-multipart-upload}
* **動作：**藉由建立新的多部分上傳實例，以開始多部分檔案上傳處理程序。
* **用法：**`ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* _選用_：指定要求及回覆鏈的 `CACHING_DIRECTIVES`。
		* 旗標：`--cache-control CACHING_DIRECTIVES`
	* _選用_：指定呈現資訊 (`DIRECTIVES`)。
		* 旗標：`--content-disposition DIRECTIVES`
	* _選用_：指定物件的內容編碼 (`CONTENT_ENCODING`)。
		* 旗標：`--content-encoding CONTENT_ENCODING`
	* _選用_：內容所使用的 LANGUAGE。
		* 旗標：`--content-language LANGUAGE`
	* _選用_：說明物件資料格式的標準 MIME 類型。
		* 旗標：`--content-type MIME`
	* _選用_：要儲存的 meta 資料的 MAP。語法：KeyName1=string,KeyName2=string
		* 旗標：`--metadata MAP`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 刪除現有儲存區
{: #ic-delete-bucket}

* **動作：**刪除 IBM Cloud Object Storage 實例中的現有儲存區。
* **用法：**`ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
    * _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
       * 旗標：`--region REGION`
    * _選用_：此作業不會要求確認。
       * 旗標：`--force`
    * _選用_：以原始 JSON 格式傳回的輸出。
       * 旗標：`--json`


### 刪除儲存區 CORS
{: #ic-delete-bucket-cors}
* **動作：**在使用者的 IBM Cloud Object Storage 帳戶中，刪除儲存區上的 CORS 配置。
* **用法：**`ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 刪除物件
{: #ic-delete-object}
* **動作：**在使用者的 IBM Cloud Object Storage 帳戶中，從儲存區中刪除一個物件。
* **用法：**`ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
  * _選用_：此作業不會要求確認。
  	* 旗標：`--force`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 刪除多個物件
{: #ic-delete-objects}
* **動作：**在使用者的 IBM Cloud Object Storage 帳戶中，從儲存區中刪除多個物件。
* **用法：**`ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。  
		* 旗標：`--bucket BUCKET_NAME`  
	* 使用速記或 JSON 語法的 STRUCTURE。  
		* 旗標：`--delete STRUCTURE`  
		* 速記語法：  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* JSON 語法：  
	`--delete file://<filename.json>`  
	`--delete` 指令採用 JSON 結構，用於說明應該重新組合為完整檔案的多部分上傳的組件。在此範例中，使用 `file://` 字首，從指定的檔案中載入 JSON 結構。
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
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 使用 S3Manager 下載物件
{: #ic-download-s3manager}
* **動作：**同時從 S3 下載數個物件。
* **用法：**`ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **要提供的參數：**
	* 儲存區的名稱 (BUCKET_NAME)。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* _選用_：在傳送組件時，每次呼叫「上傳」以平行啟動的 goroutines 數目。預設值為 5。
		* 旗標：`--concurrency value`
	* _選用_：將資料緩衝為區塊並將其作為 S3 的組件結束時，要使用的緩衝區 SIZE（以位元組為單位）。容許的組件大小下限為 5MB。
		* 旗標：`--part-size SIZE`
	* _選用_：只有在物件的 entitytag (ETag) 與指定的 ETAG 相同時，才會傳回物件，否則會傳回 412（前置條件失敗）。
		* 旗標：`--if-match ETAG`
	* _選用_：只有在物件自指定 TIMESTAMP 後經過修改，才會傳回物件，否則會傳回 304（未經過修改）。
		* 旗標：`--if-modified-since TIMESTAMP`
	* _選用_：只有在物件的實體標記 (ETag) 與指定的 ETAG 不同時，才會傳回物件，否則會傳回 304（未經過修改）。
		* 旗標：`--if-none-match ETAG`
	* _選用_：只有在物件自指定 TIMESTAMP 後未經過修改，才會傳回物件，否則會傳回 412（前置條件失敗）。
		* 旗標：`--if-unmodified-since TIMESTAMP`
	* _選用_：下載物件的指定 RANGE 位元組。如需 HTTP Range 標頭的相關資訊，[請按一下這裡](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35)。
		* 旗標：`--range RANGE`
	* _選用_：設定回應的 Cache-Control HEADER。
		* 旗標：`--response-cache-control HEADER`
	* _選用_：設定回應的 Content-Disposition HEADER。
		* 旗標：`--response-content-disposition HEADER`
	* _選用_：設定回應的 Content-Encoding HEADER。
		* 旗標：`--response-content-encoding HEADER`
	* _選用_：設定回應的 Content-Language HEADER。
		* 旗標：`--response-content-language HEADER`
	* _選用_：設定回應的 Content-Type HEADER。
		* 旗標：`--response-content-type HEADER`
	* _選用_：設定回應的 Expires HEADER。
		* 旗標：`--response-expires HEADER`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式將使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`
	* _選用_：要儲存物件內容的位置。如果未提供此參數，則程式會使用預設位置。
		* 參數：`OUTFILE`


### 取得儲存區的類別
{: #ic-bucket-class}
* **動作：**決定 IBM Cloud Object Storage 實例中儲存區的類別。
* **用法：**`ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 取得儲存區 CORS
{: #ic-get-bucket-cors}
* **動作：**傳回使用者 IBM Cloud Object Storage 帳戶中儲存區的 CORS 配置。
* **用法：**`ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **要提供的參數：**
  * 儲存區的名稱。  
    * 旗標：`--bucket BUCKET_NAME`
  * _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
    * 旗標：`--region REGION`
  * _選用_：以原始 JSON 格式傳回的輸出。
    * 旗標：`--json`


### 尋找儲存區
{: #ic-find-bucket}
* **動作：**決定 IBM Cloud Object Storage 實例中儲存區的地區及類別。 
* **用法：**`ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`
	


### 下載物件
{: #ic-download-object}
* **動作：**在使用者的 IBM Cloud Object Storage 帳戶中，從儲存區中下載一個物件。
* **用法：**`ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* _選用_：只有在物件的實體標記 (ETag) 與指定的 ETAG 相同時，才會傳回物件，否則會傳回 412（前置條件失敗）。
		* 旗標：`--if-match ETAG`
	* _選用_：只有在物件自指定 TIMESTAMP 後經過修改，才會傳回物件，否則會傳回 304（未經過修改）。
		* 旗標：`--if-modified-since TIMESTAMP`
	* _選用_：只有在物件的實體標記 (ETag) 與指定的 ETAG 不同時，才會傳回物件，否則會傳回 304（未經過修改）。
		* 旗標：`--if-none-match ETAG`
	* _選用_：只有在物件自指定 TIMESTAMP 後未經過修改，才會傳回物件，否則會傳回 412（前置條件失敗）。
		* 旗標：`--if-unmodified-since TIMESTAMP`
	* _選用_：下載物件的指定 RANGE 位元組。
		* 旗標：`--range RANGE`
	* _選用_：設定回應的 Cache-Control HEADER。
		* 旗標：`--response-cache-control HEADER`
	* _選用_：設定回應的 Content-Disposition HEADER。
		* 旗標：`--response-content-disposition HEADER`
	* _選用_：設定回應的 Content-Encoding HEADER。
		* 旗標：`--response-content-encoding HEADER`
	* _選用_：設定回應的 Content-Language HEADER。
		* 旗標：`--response-content-language HEADER`
	* _選用_：設定回應的 Content-Type HEADER。
		* 旗標：`--response-content-type HEADER`
	* _選用_：設定回應的 Expires HEADER。
		* 旗標：`--response-expires HEADER`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`
	* _選用_：要儲存物件內容的位置。如果未提供此參數，則程式會使用預設位置。
		* 參數：`OUTFILE`


### 取得儲存區的標頭
{: #ic-bucket-header}
* **動作：**判斷儲存區是否存在於 IBM Cloud Object Storage 實例中。
* **用法：**`ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 取得物件的標頭
{: #ic-object-header}
* **動作：**判斷檔案是否存在於使用者 IBM Cloud Object Storage 帳戶的儲存區中。
* **用法：**`ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* _選用_：只有在物件的實體標記 (ETag) 與指定的 ETAG 相同時，才會傳回物件，否則會傳回 412（前置條件失敗）。
		* 旗標：`--if-match ETAG`
	* _選用_：只有在物件自指定 TIMESTAMP 後經過修改，才會傳回物件，否則會傳回 304（未經過修改）。
		* 旗標：`--if-modified-since TIMESTAMP`
	* _選用_：只有在物件的實體標記 (ETag) 與指定的 ETAG 不同時，才會傳回物件，否則會傳回 304（未經過修改）。
		* 旗標：`--if-none-match ETAG`
	* _選用_：只有在物件自指定 TIMESTAMP 後未經過修改，才會傳回物件，否則會傳回 412（前置條件失敗）。
		* 旗標：`--if-unmodified-since TIMESTAMP`
	* 下載物件的指定 RANGE 位元組。
		* 旗標：`--range RANGE`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 列出所有儲存區
{: #ic-list-buckets}
* **動作：**列印使用者 IBM Cloud Object Storage 帳戶中所有儲存區的清單。儲存區可能位於不同的地區。
* **用法：**`ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* 請注意，如果您使用 IAM 鑑別，則必須提供 CRN。這可以使用 [`ibmcloud cos config crn`](#configure-the-program) 指令進行設定。
* **要提供的參數：**
  * 沒有要提供的參數。
	* _選用_：在要求中設定「IBM 服務實例 ID」。
		* 旗標：`--ibm-service-instance-id`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 延伸儲存區清單
{: #ic-extended-bucket-listing}
* **動作：**列印使用者 IBM Cloud Object Storage 帳戶中所有儲存區的清單。儲存區可能位於不同的地區。
* **用法：**`ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* 請注意，如果您使用 IAM 鑑別，則必須提供 CRN。這可以使用 [`ibmcloud cos config crn`](#configure-the-program) 指令進行設定。
* **要提供的參數：**
  * 沒有要提供的參數。
	* _選用_：在要求中設定「IBM 服務實例 ID」。
		* 旗標：`--ibm-service-instance-id`
	* _選用_：指定要在列出儲存區中的物件時開始使用的 KEY。
		* 旗標：`--marker KEY`
	* _選用_：將回應限制為開頭為指定 PREFIX 的索引鍵。
		* 旗標：`--prefix PREFIX`
	* _選用_：要在服務呼叫中取得之每個頁面的 SIZE。這不會影響指令輸出中傳回的項目數。設定較小的頁面大小會導致對服務發出更多呼叫，但每次呼叫都會擷取較少的項目。這可協助防止服務呼叫逾時。
		* 旗標：`--page-size SIZE`
	* _選用_：要在指令輸出中傳回的項目總數 NUMBER。
		* 旗標：`--max-items NUMBER`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 列出進行中多部分上傳
{: #ic-list-multipart-uploads}
* **動作：**列出進行中多部分上傳。
* **用法：**`ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：DELIMITER 是您用來將索引鍵分組的字元。
		* 旗標：`--delimiter DELIMITER`
	* _選用_：編碼回應中物件索引鍵及指定要使用的編碼 METHOD 的要求。
		* 旗標：`--encoding-type METHOD`
	* _選用_：將回應限制為開頭為指定 PREFIX 的索引鍵。
		* 旗標：`--prefix PREFIX`
	* _選用_：將此參數與 upload-id-marker 一起使用，可指定應該在此之後開始列出的多部分上傳。
		* 旗標：`--key-marker value`
	* _選用_：與 key-marker 一起使用，可指定應該在此之後開始列出的多部分上傳。如果未指定 key-marker，則會忽略 upload-id-marker 參數。
		* 旗標：`--upload-id-marker value`
	* _選用_：要在服務呼叫中取得之每個頁面的 SIZE。這不會影響指令輸出中傳回的項目數。設定較小的頁面大小會導致對服務發出更多呼叫，但每次呼叫都會擷取較少的項目。這可協助防止服務呼叫逾時。（預設值：1000）。
		* 旗標：`--page-size SIZE`
	* _選用_：要在指令輸出中傳回的項目總數 NUMBER。如果可用的項目總數超過指定的值，則會在指令輸出中提供 NextToken。若要繼續分頁，請在後續指令的 starting-token 引數中提供 NextToken 值（預設值：0）。
		* 旗標：`--max-items NUMBER`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 列出物件
{: #ic-list-objects}
* **動作：**列出位於使用者 IBM Cloud Object Storage 帳戶之儲存區中的檔案。此作業目前限制為 1000 個最近建立的物件，而且無法進行過濾。
* **用法：**`ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：DELIMITER 是您用來將索引鍵分組的字元。
		* 旗標：`--delimiter DELIMITER`
	* _選用_：編碼回應中物件索引鍵及指定要使用的編碼 METHOD 的要求。
		* 旗標：`--encoding-type METHOD`
	* _選用_：將回應限制為開頭為指定 PREFIX 的索引鍵。
		* 旗標：`--prefix PREFIX`
	* _選用_：指定開始分頁位置的 TOKEN。這是先前截斷回應中的 NextToken。
		* 旗標：`--starting-token TOKEN`
	* _選用_：要在服務呼叫中取得之每個頁面的 SIZE。這不會影響指令輸出中傳回的項目數。設定較小的頁面大小會導致對服務發出更多呼叫，但每次呼叫都會擷取較少的項目。這可協助防止服務呼叫逾時。（預設值：1000）
		* 旗標：`--page-size SIZE`
	* _選用_：要在指令輸出中傳回的項目總數 NUMBER。如果可用的項目總數超過指定的值，則會在指令輸出中提供 NextToken。若要繼續分頁，請在後續指令的 starting-token 引數中提供 NextToken 值。（預設值：0）
		* 旗標：`--max-items NUMBER`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 列出組件
{: #ic-list-parts}
* **動作：**列印進行中多部分上傳實例的相關資訊。
* **用法：**`ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* 上傳識別多部分上傳的 ID。
		* 旗標：`--upload-id ID`
	* 在此之後開始列出的組件號碼 VALUE（預設值：1）
		* 旗標：`--part-number-marker VALUE`
	* _選用_：要在服務呼叫中取得之每個頁面的 SIZE。這不會影響指令輸出中傳回的項目數。設定較小的頁面大小會導致對服務發出更多呼叫，但每次呼叫都會擷取較少的項目。這可協助防止服務呼叫逾時。（預設值：1000）
		* 旗標：`--page-size SIZE`
	* _選用_：要在指令輸出中傳回的項目總數 NUMBER。如果可用的項目總數超過指定的值，則會在指令輸出中提供 NextToken。若要繼續分頁，請在後續指令的 starting-token 引數中提供 NextToken 值。（預設值：0）
		* 旗標：`--max-items NUMBER`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 設定儲存區 CORS
{: #ic-set-bucket-cors}
* **動作：**設定使用者 IBM Cloud Object Storage 帳戶中儲存區的 CORS 配置。
* **用法：**`ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* _選用_：在檔案中使用 JSON 語法的 STRUCTURE。
		* 旗標：`--cors-configuration STRUCTURE`
		* JSON 語法：  
	`--cors-configuration file://<filename.json>`  
	`--cors-configuration` 指令採用 JSON 結構，用於說明應該重新組合為完整檔案的多部分上傳的組件。在此範例中，使用 `file://` 字首，從指定的檔案中載入 JSON 結構。
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
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`



### 放置物件
{: #ic-upload-object}
* **動作：**將物件上傳至使用者 IBM Cloud Object Storage 帳戶中的儲存區。
* **用法：**`ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **要提供的參數：**
    * 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* _選用_：物件資料位置 (`FILE_PATH`)。
		* 旗標：`--body FILE_PATH`
	* _選用_：指定要求及回覆鏈的 `CACHING_DIRECTIVES`。
		* 旗標：`--cache-control CACHING_DIRECTIVES`
	* _選用_：指定呈現資訊 (`DIRECTIVES`)。
		* 旗標：`--content-disposition DIRECTIVES`
	* _選用_：指定物件的內容編碼 (`CONTENT_ENCODING`)。
		* 旗標：`--content-encoding CONTENT_ENCODING`
	* _選用_：內容所使用的 LANGUAGE。
		* 旗標：`--content-language LANGUAGE`
	* _選用_：內文的 SIZE（以位元組為單位）。無法自動判斷內文的大小時，此參數十分有用。（預設值：0）
		* 旗標：`--content-length SIZE`
	* _選用_：資料的 base64 編碼 128 位元 MD5 摘要。
		* 旗標：`--content-md5 MD5`
	* _選用_：說明物件資料格式的標準 MIME 類型。
		* 旗標：`--content-type MIME`
	* _選用_：要儲存的 meta 資料的 MAP。語法：KeyName1=string,KeyName2=string
		* 旗標：`--metadata MAP`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 使用 S3Manager 上傳物件
{: #ic-upload-s3manager}
* **動作：**同時從 S3 上傳數個物件。
* **用法：**`ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **要提供的參數：**
	* 儲存區的名稱 (BUCKET_NAME)。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* 要上傳之檔案的 PATH。
		* 旗標：`--file PATH`
	* _選用_：在傳送組件時，每次呼叫「上傳」以平行啟動的 goroutines 數目。預設值為 5。
		* 旗標：`--concurrency value`
	* _選用_：將上傳至 S3 的 PARTS 數目上限，用於計算要上傳之物件的組件大小。限制為 10,000 個組件。
		* 旗標：`--max-upload-parts PARTS`
	* _選用_：將資料緩衝為區塊並將其作為 S3 的組件結束時，要使用的緩衝區 SIZE（以位元組為單位）。容許的組件大小下限為 5MB。
		* 旗標：`--part-size SIZE`
	* _選用_：將此值設為 true 會讓 SDK 避免在失敗時呼叫 AbortMultipartUpload，並將所有順利上傳的組件保留在 S3 上，以進行手動回復。
		* 旗標：`--leave-parts-on-errors`
	* _選用_：指定要求/回覆鏈的 CACHING_DIRECTIVES。
		* 旗標：`--cache-control CACHING_DIRECTIVES`
	* _選用_：指定呈現資訊 (DIRECTIVES)。
		* 旗標：`--content-disposition DIRECTIVES`
	* _選用_：指定已套用至物件的內容編碼 (CONTENT_ENCODING)，因此而指定必須套用以取得 Content-Type 標頭欄位所參照之 media-type 的解碼機制。
		* 旗標：`--content-encoding CONTENT_ENCODING`
	* _選用_：內容所使用的 LANGUAGE。
		* 旗標：`--content-language LANGUAGE`
	* _選用_：內文的 SIZE（以位元組為單位）。無法自動判斷內文的大小時，此參數十分有用。
		* 旗標：`--content-length SIZE`
	* _選用_：資料的 base64 編碼 128 位元 MD5 摘要。
		* 旗標：`--content-md5 MD5`
	* _選用_：說明物件資料格式的標準 MIME 類型。
		* 旗標：`--content-type MIME`
	* _選用_：要儲存的 meta 資料的 MAP。語法：KeyName1=string,KeyName2=string
		* 旗標：`--metadata MAP`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式將使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 上傳組件
{: #ic-upload-part}
* **動作：**上傳現有多部分上傳實例中檔案的組件。
* **用法：**`ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* 請注意，您必須將每個上傳檔案組件的號碼及每個組件的 ETag（CLI 將為您列印）儲存至 JSON 檔案。如需相關資訊，請參閱下面的《多部分上傳手冊》。
* **要提供的參數：**
	* 將在其中進行多部分上傳的儲存區名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* 上傳識別多部分上傳的 ID。
		* 旗標：`--upload-id ID`
	* 所上傳之組件的組件號碼 NUMBER。這是在 1 - 10,000 範圍內的正整數。（預設值：1）
		* 旗標：`--part-number NUMBER`
	* _選用_：物件資料位置 (`FILE_PATH`)。
		* 旗標：`--body FILE_PATH`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 上傳組件副本
{: #ic-upload-a-part-copy}
* **動作：**藉由複製現有物件中的資料來上傳組件。
* **用法：**`ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* 請注意，您必須將每個上傳檔案組件的號碼及每個組件的 ETag（CLI 將為您列印）儲存至 JSON 檔案。如需相關資訊，請參閱《多部分上傳手冊》。
* **要提供的參數：**
	* 儲存區的名稱。
		* 旗標：`--bucket BUCKET_NAME`
	* 物件的 KEY。
		* 旗標：`--key KEY`
	* 上傳識別多部分上傳的 ID。
		* 旗標：`--upload-id ID`
	* 所上傳之組件的組件號碼 NUMBER。這是介於 1 與 10,000 之間的正整數。
		* 旗標：`--part-number PART_NUMBER`
	* (SOURCE) 來源儲存區的名稱，以及來源物件的索引鍵名稱（以斜線 (/) 區隔）。必須是 URL 編碼。
		* 旗標：`--copy-source SOURCE`
	* _選用_：如果物件的實體標籤 (Etag) 符合指定的標籤 (ETAG)，則複製此物件。
		* 旗標：`--copy-source-if-match ETAG`
	* _選用_：如果物件自指定時間 (TIMESTAMP) 後經過修改，則複製此物件。
		* 旗標：`--copy-source-if-modified-since TIMESTAMP`
	* _選用_：如果物件的實體標籤 (ETag) 與指定的標籤 (ETAG) 不同，則複製此物件。
		* 旗標：`--copy-source-if-none-match ETAG`
	* _選用_：如果物件自指定時間 (TIMESTAMP) 後未經過修改，則複製此物件。
		* 旗標：`--copy-source-if-unmodified-since TIMESTAMP`
	* _選用_：要從來源物件中複製的位元組範圍。範圍值必須使用 bytes=first-last 形式，其中 first 及 last 是要複製的以零起始位元組偏移。例如，bytes=0-9 指出您要複製來源的前 10 個位元組。只有在來源物件大於 5 MB 時，您才能複製一個範圍。
		* 旗標：`--copy-source-range value`
	* _選用_：儲存區所在的 REGION。如果未提供此旗標，則程式會使用配置中指定的預設選項。
		* 旗標：`--region REGION`
	* _選用_：以原始 JSON 格式傳回的輸出。
		* 旗標：`--json`


### 等待
{: #ic-wait}
* **動作：**等到滿足特定條件。除非符合列出的需求，否則每個次指令都會輪詢 API。
* **用法：**`ibmcloud cos wait command [arguments...] [command options]`
* **指令：**
    * `bucket-exists`
  		* 等到使用 head-bucket 輪詢時接收到 200 回應。除非達到成功狀態，否則它會每隔 5 秒輪詢一次。在 20 次檢查失敗之後，這會結束，而且回覆碼為 255。
	* `bucket-not-exists`
		* 等到使用 head-bucket 輪詢時接收到 404 回應。除非達到成功狀態，否則它會每隔 5 秒輪詢一次。在 20 次檢查失敗之後，這會結束，而且回覆碼為 255。
	* `object-exists`
		* 等到使用 head-object 輪詢時接收到 200 回應。除非達到成功狀態，否則它會每隔 5 秒輪詢一次。在 20 次檢查失敗之後，這會結束，而且回覆碼為 255。
	* `object-not-exists`
		* 等到使用 head-object 輪詢時接收到 404 回應。除非達到成功狀態，否則它會每隔 5 秒輪詢一次。在 20 次檢查失敗之後，這會結束，而且回覆碼為 255。

