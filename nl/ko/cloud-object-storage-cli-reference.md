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

# IBM Cloud CLI 사용
{: #ic-use-the-ibm-cli}

Cloud Object Storage 플러그인은 오브젝트 스토리지 리소스에 대한 작업을 위해 IBM Cloud 명령행 인터페이스(CLI)를 API 랩퍼로 확장합니다.

## 전제조건
{: #ic-prerequisites}
* [IBM Cloud](https://cloud.ibm.com/) 계정
* [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision)의 인스턴스
* [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## 설치 및 구성
{: #ic-installation}

플러그인은 64비트 프로세서에서 실행되는 Windows, Linux 및 macOS 운영 체제와 호환 가능합니다.

`plugin install` 명령을 사용하여 플러그인을 설치하십시오.

```
ibmcloud plugin install cloud-object-storage
```

플러그인이 설치되면 [`ibmcloud cos config`](#configure-the-program) 명령을 사용하여 플러그인을 구성할 수 있습니다. 이는 인증 정보, 기본 다운로드 위치, 인증 선택 등으로 플러그인을 채우는 데 사용할 수 있습니다.

프로그램은 다운로드한 파일의 기본 로컬 디렉토리를 설정하고 기본 지역을 설정하는 기능도 제공합니다. 기본 다운로드 위치를 설정하려면 `ibmcloud cos config ddl`을 입력하고 프로그램에 올바른 파일 경로를 입력하십시오. 기본 지역을 설정하려면 `ibmcloud cos config region`을 입력하고 프로그램에 지역 코드(예: `us-south`)를 입력하십시오. 기본적으로 이 값을 `us-geo`로 설정합니다.


IAM 인증을 사용하는 경우 CRN을 제공하여 일부 명령을 사용해야 합니다. CRN을 설정하기 위해 `ibmcloud cos config crn`을 입력하고 CRN을 제공할 수 있습니다. `ibmcloud resource service-instance INSTANCE_NAME`으로 CRN을 찾을 수 있습니다. 또는 웹 기반 콘솔을 열고 사이드바에서 **서비스 인증 정보**를 선택하고 새 인증 정보 세트를 작성할 수 있습니다(또는 이미 작성한 기존 인증 정보 파일 보기).

`ibmcloud cos config list`를 프롬프트로 표시하여 현재 Cloud Object Storage 인증 정보를 볼 수 있습니다. 구성 파일이 플러그인에서 생성되므로 파일을 수동으로 편집하지 않는 것이 좋습니다.

### HMAC 인증 정보
{: #ic-hmac-credentials}

선호하는 경우 [서비스 ID의 HMAC 인증 정보](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac)를 API 키 대신 사용할 수 있습니다. `ibmcloud cos config hmac`를 실행하여 HMAC 인증 정보를 입력한 다음, `ibmcloud cos config auth`를 사용하여 인증 방법을 전환하십시오.

고유 API 키를 사용하여 토큰 인증을 사용하도록 선택한 경우 프로그램이 자동으로 사용자를 인증하므로 인증 정보를 제공하지 않아도 됩니다.
{: note}

언제든 HMAC와 IAM 인증 간에 전환하기 위해 `ibmcloud cos config auth`를 입력할 수 있습니다. IBM Cloud에서 인증 및 권한에 대한 자세한 정보는 [Identity and Access Management 문서](/docs/iam?topic=iam-iamoverview)를 참조하십시오.

## 명령 색인
{: #ic-command-index}

| 명령      |  |  |
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

아래에 나열된 각 오퍼레이션에는 수행 내용, 사용 방법 및 선택적 또는 필수 매개변수에 대한 설명이 있습니다. 선택사항으로 지정되지 않는 한 나열된 매개변수는 필수입니다.

CLI 플러그인은 Object Storage에서 사용 가능한 전체 기능 스위트(예: Aspera 고속 전송, 불변 오브젝트 스토리지, Key Protect 버킷 작성 또는 버킷 방화벽)를 지원하지 않습니다.
{: note}

### 다중 파트 업로드 정보
{: #ic-abort-multipart-upload}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에 대한 업로드를 종료하여 다중 파트 업로드 인스턴스를 중단합니다.
* **사용법:** `ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* 다중 파트 업로드를 식별하는 업로드 ID입니다.
		* 플래그: `--upload-id ID`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 다중 파트 업로드 완료
{: #ic-complete-multipart-upload}
* **조치:** 현재 업로드한 파트를 어셈블하고 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에 파일을 업로드하여 다중 파트 업로드 인스턴스를 완료합니다.
* **사용법:** `ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* 다중 파트 업로드를 식별하는 업로드 ID입니다.
		* 플래그: `--upload-id ID`
	* 설정할 다중 파트 업로드의 구조입니다.
		* 플래그: `--multipart-upload STRUCTURE`
		* 단축 구문:  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* JSON 구문:  
	`--multipart-upload file://<filename.json>`  
	`--multipart-upload` 명령은 전체 파일에 리어셈블해야 하는 다중 파트 업로드의 파트를 설명하는 JSON 구조를 사용합니다. 이 예에서 `file://` 접두부는 지정된 파일에서 JSON 구조를 로드하는 데 사용됩니다.
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
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


## 다중 파트 업로드 수동 제어
{: #ic-manual-multipart-uploads}

IBM Cloud Object Storage CLI는 사용자가 다중 파트 업로드 기능을 사용하여 대형 파일을 다중 파트로 업로드할 수 있는 기능을 제공합니다. 새 다중 파트 업로드를 시작하려면 새 업로드 인스턴스의 업로드 ID를 리턴하는 `create-multipart-upload` 명령을 실행하십시오. 업로드 프로세스를 계속하려면 각 후속 명령에 대한 업로드 ID를 저장해야 합니다.

`complete-multipart-upload` 명령을 실행하면 업로드할 각 파일 파트에 대해 `upload-part`를 실행하십시오. **다중 파트 업로드의 경우 모든 파일 파트(마지막 파트 제외)의 크기가 5MB 이상이어야 합니다.** 파일을 개별 파트로 분할하기 위해 터미널 창에서 `split`를 실행할 수 있습니다. 예를 들어, 데스크탑에 이름이 `TESTFILE`인 13MB 파일이 있고 이 파일을 각각 5MB의 파일 파트로 분할하려는 경우 `split -b 3m ~/Desktop/TESTFILE part-file-`을 실행할 수 있습니다. 이 명령은 세 개의 파일 파트를 두 개는 각각 5MB이고 하나는 3MB인 파일 파트로 생성하며, 파일의 이름은 `part-file-aa`, `part-file-ab` 및 `part-file-ac`입니다.

각 파일 파트가 업로드되면 CLI는 해당 ETag를 출력합니다. 이 ETag를 파트 번호와 함께 형식화된 JSON 파일로 저장해야 합니다. 이 템플리트를 사용하여 고유 ETag JSON 데이터 파일을 작성하십시오.

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

필요에 따라 이 JSON 템플리트에 항목을 추가하십시오.

다중 파트 업로드 인스턴스의 상태를 보려면 버킷 이름, 키 및 업로드 ID를 제공하여 항상 `upload-part` 명령을 실행할 수 있습니다. 이는 다중 파트 업로드 인스턴스에 대한 원시 정보를 출력합니다. 파일의 각 파트 업로드를 완료하면 필수 매개변수와 함께 `complete-multipart-upload` 명령을 실행하십시오. 모두 잘 진행되면 파일이 원하는 버킷에 업로드되었다는 확인 메시지를 수신합니다.

### 프로그램 구성
{: #ic-config}
* **조치:** 프로그램의 환경 설정을 구성합니다.
* **사용법:** `ibmcloud cos config [COMMAND]`
* **명령:**
	* HMAC와 IAM 인증 사이에서 전환합니다.
		* 명령: `auth`
	* 구성에 CRN을 저장합니다.
		* 명령: `crn`
	* 구성에 기본 다운로드 위치를 저장합니다.
		* 명령: `ddl`
	* 구성에 HMAC 인증 정보를 저장합니다.
		* 명령: `hmac`
	* 구성을 나열합니다.
		* 명령: `list`
	* 구성에 기본 지역을 저장합니다.
		* 명령: `region`
	* VHost와 경로 URL 스타일 사이에서 전환합니다.
		* 명령: `url-style`


### 버킷에서 오브젝트 복사
{: #ic-copy-object}
* **조치:** 소스 버킷에서 대상 버킷으로 오브젝트를 복사합니다.
* **사용법:** `ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* (소스) 소스 버킷 이름과 소스 오브젝트의 키 이름이며, 슬래시(/)로 구분됩니다. URL로 인코딩되어야 합니다.
		* 플래그: `--copy-source SOURCE`
	* _선택사항_: 요청 및 응답 체인에 `CACHING_DIRECTIVES`를 지정합니다.
		* 플래그: `--cache-control CACHING_DIRECTIVES`
	* _선택사항_: 표시 정보를 지정합니다(`DIRECTIVES`).
		* 플래그: `--content-disposition DIRECTIVES`
	* _선택사항_: 오브젝트에 적용되는 컨텐츠 인코딩(CONTENT_ENCODING)과 적용해야 하는 디코딩 메커니즘을 지정하여 Content-Type 헤더 필드에서 참조하는 미디어 유형을 얻습니다.
		* 플래그: `--content-encoding CONTENT_ENCODING`
	* _선택사항_: 컨텐츠의 언어입니다.
		* 플래그: `--content-language LANGUAGE`
	* _선택사항_: 오브젝트 데이터 형식을 설명하는 표준 MIME 유형입니다.
		* 플래그: `--content-type MIME`
	* _선택사항_: 해당 엔티티 태그(Etag)가 지정된 태그(ETAG)와 일치하는 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-match ETAG`
	* _선택사항_: 지정된 시간(TIMESTAMP) 이후에 수정된 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-modified-since TIMESTAMP`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 태그(ETAG)와 다른 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-none-match ETAG`
	* _선택사항_: 지정된 시간(TIMESTAMP) 이후에 수정되지 않은 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-unmodified-since TIMESTAMP`
	* _선택사항_: 저장할 메타데이터의 맵입니다. 구문: KeyName1=string,KeyName2=string
		* 플래그: `--metadata MAP`
	* _선택사항_: 소스 오브젝트에서 메타데이터를 복사했는지 아니면 요청에 제공된 메타데이터로 바꿨는지를 지정합니다. DIRECTIVE 값: COPY,REPLACE.
		* 플래그: ` --metadata-directive DIRECTIVE`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 새 버킷 작성
{: #ic-create-bucket}

* **조치:** IBM Cloud Object Storage 인스턴스에서 버킷을 작성합니다.
* **사용법:** `ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* IAM 인증을 사용하는 경우 CRN을 제공해야 합니다. 이는 [`ibmcloud cos config crn`](#configure-the-program) 명령을 사용하여 설정 가능합니다.
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 클래스의 이름입니다.
		* 플래그: `--class CLASS_NAME`
	* _선택사항_: 요청에서 IBM 서비스 인스턴스 ID를 설정합니다.
		* 플래그: `--ibm-service-instance-id ID`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`



### 다중 파트 업로드 새로 작성
{: #ic-create-multipart-upload}
* **조치:** 다중 파트 업로드 인스턴스를 새로 작성하여 다중 파트 파일 업로드 프로세스를 시작합니다.
* **사용법:** `ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* _선택사항_: 요청 및 응답 체인에 `CACHING_DIRECTIVES`를 지정합니다.
		* 플래그: `--cache-control CACHING_DIRECTIVES`
	* _선택사항_: 표시 정보를 지정합니다(`DIRECTIVES`).
		* 플래그: `--content-disposition DIRECTIVES`
	* _선택사항_: 오브젝트의 컨텐츠 인코딩(`CONTENT_ENCODING`)을 지정합니다.
		* 플래그: `--content-encoding CONTENT_ENCODING`
	* _선택사항_: 컨텐츠의 언어입니다.
		* 플래그: `--content-language LANGUAGE`
	* _선택사항_: 오브젝트 데이터 형식을 설명하는 표준 MIME 유형입니다.
		* 플래그: `--content-type MIME`
	* _선택사항_: 저장할 메타데이터의 맵입니다. 구문: KeyName1=string,KeyName2=string
		* 플래그: `--metadata MAP`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 기존 버켓 삭제
{: #ic-delete-bucket}

* **조치:** IBM Cloud Object Storage 인스턴스에서 기존 버킷을 삭제합니다.
* **사용법:** `ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
    * _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
       * 플래그: `--region REGION`
    * _선택사항_: 오퍼레이션이 확인을 요청하지 않습니다.
       * 플래그: `--force`
    * _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
       * 플래그: `--json`


### 버킷 CORS 삭제
{: #ic-delete-bucket-cors}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에서 CORS 구성을 삭제합니다.
* **사용법:** `ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 오브젝트 삭제
{: #ic-delete-object}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에서 오브젝트를 삭제합니다.
* **사용법:** `ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
  * _선택사항_: 오퍼레이션이 확인을 요청하지 않습니다.
  	* 플래그: `--force`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 다중 오브젝트 삭제
{: #ic-delete-objects}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에서 다중 오브젝트를 삭제합니다.
* **사용법:** `ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.  
		* 플래그: `--bucket BUCKET_NAME`  
	* 단축 또는 JSON 구문을 사용하는 구조입니다.  
		* 플래그: `--delete STRUCTURE`  
		* 단축 구문:  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* JSON 구문:  
	`--delete file://<filename.json>`  
	`--delete` 명령은 전체 파일에 리어셈블해야 하는 다중 파트 업로드의 파트를 설명하는 JSON 구조를 사용합니다. 이 예에서 `file://` 접두부는 지정된 파일에서 JSON 구조를 로드하는 데 사용됩니다.
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
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### S3Manager를 사용하여 오브젝트 다운로드
{: #ic-download-s3manager}
* **조치:** S3에서 동시에 오브젝트를 다운로드합니다.
* **사용법:** `ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **제공할 매개변수:**
	* 버킷의 이름(BUCKET_NAME)입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* _선택사항_: 파트 전송 시 업로드를 위해 호출마다 동시에 스핀업하는 goroutine의 수입니다. 기본값은 5입니다.
		* 플래그: `--concurrency value`
	* _선택사항_: 청크에 데이터를 버퍼링하고 S3에 대한 파트로서 종료할 때 사용할 버퍼 크기(바이트)입니다. 허용되는 최소 파트 크기는 5MB입니다.
		* 플래그: `--part-size SIZE`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 ETAG와 동일한 경우에만 오브젝트를 리턴하고 그렇지 않으면 412(전제조건 실패)를 리턴합니다.
		* 플래그: `--if-match ETAG`
	* _선택사항_: 지정된 TIMESTAMP 이후 수정된 경우에만 오브젝트를 리턴하고 그렇지 않으면 304(수정되지 않음)를 리턴합니다.
		* 플래그: `--if-modified-since TIMESTAMP`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 ETAG와 다른 경우에만 오브젝트를 리턴하고 그렇지 않으면 304(수정되지 않음)를 리턴합니다.
		* 플래그: `--if-none-match ETAG`
	* _선택사항_: 지정된 TIMESTAMP 이후 수정되지 않은 경우에만 오브젝트를 리턴하고 그렇지 않으면 412(전제조건 실패)를 리턴합니다.
		* 플래그: `--if-unmodified-since TIMESTAMP`
	* _선택사항_: 오브젝트의 지정된 RANGE 바이트를 다운로드합니다. HTTP 범위 헤더에 대한 자세한 정보를 보려면 [여기를 클릭](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35)하십시오.
		* 플래그: `--range RANGE`
	* _선택사항_: 응답의 Cache-Control 헤더를 설정합니다.
		* 플래그: `--response-cache-control HEADER`
	* _선택사항_: 응답의 Content-Disposition 헤더를 설정합니다.
		* 플래그: `--response-content-disposition HEADER`
	* _선택사항_: 응답의 Content-Encoding 헤더를 설정합니다.
		* 플래그: `--response-content-encoding HEADER`
	* _선택사항_: 응답의 Content-Language 헤더를 설정합니다.
		* 플래그: `--response-content-language HEADER`
	* _선택사항_: 응답의 Content-Type 헤더를 설정합니다.
		* 플래그: `--response-content-type HEADER`
	* _선택사항_: 응답의 Expires 헤더를 설정합니다.
		* 플래그: `--response-expires HEADER`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`
	* _선택사항_: 오브젝트의 컨텐츠를 저장할 위치입니다. 이 매개변수를 제공하지 않으면 프로그램에서 기본 위치를 사용합니다.
		* 매개변수: `OUTFILE`


### 버킷의 클래스 가져오기
{: #ic-bucket-class}
* **조치:** IBM Cloud Object Storage 인스턴스에서 버킷의 클래스를 판별합니다.
* **사용법:** `ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 버킷 CORS 가져오기
{: #ic-get-bucket-cors}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에 대한 CORS 구성을 리턴합니다.
* **사용법:** `ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **제공할 매개변수:**
  * 버킷 이름입니다.  
    * 플래그: `--bucket BUCKET_NAME`
  * _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
    * 플래그: `--region REGION`
  * _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
    * 플래그: `--json`


### 버킷 찾기
{: #ic-find-bucket}
* **조치:** IBM Cloud Object Storage 인스턴스에서 버킷의 지역과 클래스를 판별합니다. 
* **사용법:** `ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`
	


### 오브젝트 다운로드
{: #ic-download-object}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에서 오브젝트를 다운로드합니다.
* **사용법:** `ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 ETAG와 동일한 경우에만 오브젝트를 리턴하고 그렇지 않으면 412(전제조건 실패)를 리턴합니다.
		* 플래그: `--if-match ETAG`
	* _선택사항_: 지정된 TIMESTAMP 이후 수정된 경우에만 오브젝트를 리턴하고 그렇지 않으면 304(수정되지 않음)를 리턴합니다.
		* 플래그: `--if-modified-since TIMESTAMP`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 ETAG와 다른 경우에만 오브젝트를 리턴하고 그렇지 않으면 304(수정되지 않음)를 리턴합니다.
		* 플래그: `--if-none-match ETAG`
	* _선택사항_: 지정된 TIMESTAMP 이후 수정되지 않은 경우에만 오브젝트를 리턴하고 그렇지 않으면 412(전제조건 실패)를 리턴합니다.
		* 플래그: `--if-unmodified-since TIMESTAMP`
	* _선택사항_: 오브젝트의 지정된 RANGE 바이트를 다운로드합니다. 
		* 플래그: `--range RANGE`
	* _선택사항_: 응답의 Cache-Control 헤더를 설정합니다.
		* 플래그: `--response-cache-control HEADER`
	* _선택사항_: 응답의 Content-Disposition 헤더를 설정합니다.
		* 플래그: `--response-content-disposition HEADER`
	* _선택사항_: 응답의 Content-Encoding 헤더를 설정합니다.
		* 플래그: `--response-content-encoding HEADER`
	* _선택사항_: 응답의 Content-Language 헤더를 설정합니다.
		* 플래그: `--response-content-language HEADER`
	* _선택사항_: 응답의 Content-Type 헤더를 설정합니다.
		* 플래그: `--response-content-type HEADER`
	* _선택사항_: 응답의 Expires 헤더를 설정합니다.
		* 플래그: `--response-expires HEADER`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`
	* _선택사항_: 오브젝트의 컨텐츠를 저장할 위치입니다. 이 매개변수를 제공하지 않으면 프로그램에서 기본 위치를 사용합니다.
		* 매개변수: `OUTFILE`


### 버킷의 헤더 가져오기
{: #ic-bucket-header}
* **조치:** 버킷이 IBM Cloud Object Storage 인스턴스에 있는지 판별합니다.
* **사용법:** `ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 오브젝트의 헤더 가져오기
{: #ic-object-header}
* **조치:** 파일이 사용자의 IBM Cloud Object Storage 계정의 버킷에 있는지 판별합니다.
* **사용법:** `ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 ETAG와 동일한 경우에만 오브젝트를 리턴하고 그렇지 않으면 412(전제조건 실패)를 리턴합니다.
		* 플래그: `--if-match ETAG`
	* _선택사항_: 지정된 TIMESTAMP 이후 수정된 경우에만 오브젝트를 리턴하고 그렇지 않으면 304(수정되지 않음)를 리턴합니다.
		* 플래그: `--if-modified-since TIMESTAMP`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 ETAG와 다른 경우에만 오브젝트를 리턴하고 그렇지 않으면 304(수정되지 않음)를 리턴합니다.
		* 플래그: `--if-none-match ETAG`
	* _선택사항_: 지정된 TIMESTAMP 이후 수정되지 않은 경우에만 오브젝트를 리턴하고 그렇지 않으면 412(전제조건 실패)를 리턴합니다.
		* 플래그: `--if-unmodified-since TIMESTAMP`
	* 오브젝트의 지정된 RANGE 바이트를 다운로드합니다.
		* 플래그: `--range RANGE`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 모든 버킷 나열
{: #ic-list-buckets}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 모든 버킷의 목록을 인쇄합니다. 버킷은 여러 영역에 위치할 수 있습니다.
* **사용법:** `ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* IAM 인증을 사용하는 경우 CRN을 제공해야 합니다. 이는 [`ibmcloud cos config crn`](#configure-the-program) 명령을 사용하여 설정 가능합니다.
* **제공할 매개변수:**
  * 제공할 매개변수가 없습니다.
	* _선택사항_: 요청에서 IBM 서비스 인스턴스 ID를 설정합니다.
		* 플래그: `--ibm-service-instance-id`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 확장된 버킷 나열
{: #ic-extended-bucket-listing}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 모든 버킷의 목록을 인쇄합니다. 버킷은 여러 영역에 위치할 수 있습니다.
* **사용법:** `ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* IAM 인증을 사용하는 경우 CRN을 제공해야 합니다. 이는 [`ibmcloud cos config crn`](#configure-the-program) 명령을 사용하여 설정 가능합니다.
* **제공할 매개변수:**
  * 제공할 매개변수가 없습니다.
	* _선택사항_: 요청에서 IBM 서비스 인스턴스 ID를 설정합니다.
		* 플래그: `--ibm-service-instance-id`
	* _선택사항_: 버킷의 오브젝트를 나열할 때 시작할 키를 지정합니다.
		* 플래그: `--marker KEY`
	* _선택사항_: 지정된 접두부로 시작하는 키로 응답을 제한합니다.
		* 플래그: `--prefix PREFIX`
	* _선택사항_: 서비스 호출에서 가져올 각 페이지의 크기입니다. 이는 명령의 출력에 리턴된 항목의 수에 영향을 주지 않습니다. 페이지 크기를 더 작게 설정하면 서비스에 대한 호출이 많아져 각 호출에서 더 적은 수의 항목을 검색하게 됩니다. 이는 서비스 호출 시간이 초과하는 것을 방지할 수 있습니다.
		* 플래그: `--page-size SIZE`
	* _선택사항_: 명령의 출력에 리턴할 항목의 총 수입니다.
		* 플래그: `--max-items NUMBER`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 진행 중인 다중 파트 업로드 나열
{: #ic-list-multipart-uploads}
* **조치:** 진행 중인 다중 파트 업로드를 나열합니다.
* **사용법:** `ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 구분 기호는 키를 그룹화하는 데 사용할 문자입니다.
		* 플래그: `--delimiter DELIMITER`
	* _선택사항_: 응답에서 오브젝트 키를 인코딩하도록 요청하고 사용할 인코딩 방법을 지정합니다.
		* 플래그: `--encoding-type METHOD`
	* _선택사항_: 지정된 접두부로 시작하는 키로 응답을 제한합니다.
		* 플래그: `--prefix PREFIX`
	* _선택사항_: upload-id-marker와 함께 이 매개변수는 목록이 시작되는 다중 파트 업로드를 지정합니다.
		* 플래그: `--key-marker value`
	* _선택사항_: key-marker와 함께 목록이 시작되는 다중 파트 업로드를 지정합니다. key-marker를 지정하지 않으면 upload-id-marker 매개변수가 무시됩니다.
		* 플래그: `--upload-id-marker value`
	* _선택사항_: 서비스 호출에서 가져올 각 페이지의 크기입니다. 이는 명령의 출력에 리턴된 항목의 수에 영향을 주지 않습니다. 페이지 크기를 더 작게 설정하면 서비스에 대한 호출이 많아져 각 호출에서 더 적은 수의 항목을 검색하게 됩니다. 이는 서비스 호출 시간이 초과하는 것을 방지할 수 있습니다(기본값: 1000).
		* 플래그: `--page-size SIZE`
	* _선택사항_: 명령의 출력에 리턴할 항목의 총 수입니다. 사용 가능한 총 항목 수가 지정된 값을 초과하는 경우에는 NextToken이 명령의 출력에 제공됩니다. 페이지 매김을 재개하려면 후속 명령의 starting-token 인수에 NextToken 값을 제공하십시오(기본값: 0).
		* 플래그: `--max-items NUMBER`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 오브젝트 나열
{: #ic-list-objects}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷의 파일을 나열합니다. 이 오퍼레이션은 현재 1000개의 최근에 작성된 오브젝트로 제한되며 필터링되지 않습니다.
* **사용법:** `ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 구분 기호는 키를 그룹화하는 데 사용할 문자입니다.
		* 플래그: `--delimiter DELIMITER`
	* _선택사항_: 응답에서 오브젝트 키를 인코딩하도록 요청하고 사용할 인코딩 방법을 지정합니다.
		* 플래그: `--encoding-type METHOD`
	* _선택사항_: 지정된 접두부로 시작하는 키로 응답을 제한합니다.
		* 플래그: `--prefix PREFIX`
	* _선택사항_: 페이지 매김을 시작할 위치를 지정하는 토큰입니다. 이는 이전에 잘린 응답의 NextToken입니다.
		* 플래그: `--starting-token TOKEN`
	* _선택사항_: 서비스 호출에서 가져올 각 페이지의 크기입니다. 이는 명령의 출력에 리턴된 항목의 수에 영향을 주지 않습니다. 페이지 크기를 더 작게 설정하면 서비스에 대한 호출이 많아져 각 호출에서 더 적은 수의 항목을 검색하게 됩니다. 이는 서비스 호출 시간이 초과하는 것을 방지할 수 있습니다(기본값: 1000).
		* 플래그: `--page-size SIZE`
	* _선택사항_: 명령의 출력에 리턴할 항목의 총 수입니다. 사용 가능한 총 항목 수가 지정된 값을 초과하는 경우에는 NextToken이 명령의 출력에 제공됩니다. 페이지 매김을 재개하려면 후속 명령의 starting-token 인수에 NextToken 값을 제공하십시오(기본값: 0)
		* 플래그: `--max-items NUMBER`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 파트 나열
{: #ic-list-parts}
* **조치:** 진행 중인 다중 파트 업로드 인스턴스에 대한 정보를 출력합니다.
* **사용법:** `ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* 다중 파트 업로드를 식별하는 업로드 ID입니다.
		* 플래그: `--upload-id ID`
	* 목록이 시작되는 파트 번호 값입니다(기본값: 1).
		* 플래그: `--part-number-marker VALUE`
	* _선택사항_: 서비스 호출에서 가져올 각 페이지의 크기입니다. 이는 명령의 출력에 리턴된 항목의 수에 영향을 주지 않습니다. 페이지 크기를 더 작게 설정하면 서비스에 대한 호출이 많아져 각 호출에서 더 적은 수의 항목을 검색하게 됩니다. 이는 서비스 호출 시간이 초과하는 것을 방지할 수 있습니다(기본값: 1000).
		* 플래그: `--page-size SIZE`
	* _선택사항_: 명령의 출력에 리턴할 항목의 총 수입니다. 사용 가능한 총 항목 수가 지정된 값을 초과하는 경우에는 NextToken이 명령의 출력에 제공됩니다. 페이지 매김을 재개하려면 후속 명령의 starting-token 인수에 NextToken 값을 제공하십시오(기본값: 0)
		* 플래그: `--max-items NUMBER`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 버킷 CORS 설정
{: #ic-set-bucket-cors}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에 대한 CORS 구성을 설정합니다.
* **사용법:** `ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* _선택사항_: 파일에서 JSON 구문을 사용하는 구조입니다.
		* 플래그: `--cors-configuration STRUCTURE`
		* JSON 구문:  
	`--cors-configuration file://<filename.json>`  
	`--cors-configuration` 명령은 전체 파일에 리어셈블해야 하는 다중 파트 업로드의 파트를 설명하는 JSON 구조를 사용합니다. 이 예에서 `file://` 접두부는 지정된 파일에서 JSON 구조를 로드하는 데 사용됩니다.
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
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`



### 오브젝트 배치
{: #ic-upload-object}
* **조치:** 사용자의 IBM Cloud Object Storage 계정에 있는 버킷에 오브젝트를 업로드합니다.
* **사용법:** `ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **제공할 매개변수:**
    * 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* _선택사항_: 오브젝트 데이터 위치입니다(`FILE_PATH`).
		* 플래그: `--body FILE_PATH`
	* _선택사항_: 요청 및 응답 체인에 `CACHING_DIRECTIVES`를 지정합니다.
		* 플래그: `--cache-control CACHING_DIRECTIVES`
	* _선택사항_: 표시 정보를 지정합니다(`DIRECTIVES`).
		* 플래그: `--content-disposition DIRECTIVES`
	* _선택사항_: 오브젝트의 컨텐츠 인코딩(`CONTENT_ENCODING`)을 지정합니다.
		* 플래그: `--content-encoding CONTENT_ENCODING`
	* _선택사항_: 컨텐츠의 언어입니다.
		* 플래그: `--content-language LANGUAGE`
	* _선택사항_: 본문의 크기(바이트)입니다. 이 매개변수는 본문의 크기를 자동으로 판별할 수 없는 경우에 유용합니다(기본값: 0).
		* 플래그: `--content-length SIZE`
	* _선택사항_: 데이터의 base64 인코딩 128비트 MD5 요약입니다.
		* 플래그: `--content-md5 MD5`
	* _선택사항_: 오브젝트 데이터 형식을 설명하는 표준 MIME 유형입니다.
		* 플래그: `--content-type MIME`
	* _선택사항_: 저장할 메타데이터의 맵입니다. 구문: KeyName1=string,KeyName2=string
		* 플래그: `--metadata MAP`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### S3Manager를 사용하여 오브젝트 업로드
{: #ic-upload-s3manager}
* **조치:** S3에서 동시에 오브젝트를 업로드합니다.
* **사용법:** `ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **제공할 매개변수:**
	* 버킷의 이름(BUCKET_NAME)입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* 업로드할 파일의 경로입니다.
		* 플래그: `--file PATH`
	* _선택사항_: 파트 전송 시 업로드를 위해 호출마다 동시에 스핀업하는 goroutine의 수입니다. 기본값은 5입니다.
		* 플래그: `--concurrency value`
	* _선택사항_: 업로드할 오브젝트의 파트 크기를 계산하는 S3로 업로드할 최대 파트 수입니다. 한계는 10,000개의 파트입니다.
		* 플래그: `--max-upload-parts PARTS`
	* _선택사항_: 청크에 데이터를 버퍼링하고 S3에 대한 파트로서 종료할 때 사용할 버퍼 크기(바이트)입니다. 허용되는 최소 파트 크기는 5MB입니다.
		* 플래그: `--part-size SIZE`
	* _선택사항_: 이 값을 true로 설정하면 실패 시 SDK가 AbortMultipartUpload를 호출하지 않으며, 수동 복구를 위해 S3에 업로드에 성공한 모든 파트를 남겨둡니다.
		* 플래그: `--leave-parts-on-errors`
	* _선택사항_: 요청/응답 체인에 CACHING_DIRECTIVES를 지정합니다.
		* 플래그: `--cache-control CACHING_DIRECTIVES`
	* _선택사항_: 표시 정보를 지정합니다(DIRECTIVES).
		* 플래그: `--content-disposition DIRECTIVES`
	* _선택사항_: 오브젝트에 적용되는 컨텐츠 인코딩(CONTENT_ENCODING)과 적용해야 하는 디코딩 메커니즘을 지정하여 Content-Type 헤더 필드에서 참조하는 미디어 유형을 얻습니다.
		* 플래그: `--content-encoding CONTENT_ENCODING`
	* _선택사항_: 컨텐츠의 언어입니다.
		* 플래그: `--content-language LANGUAGE`
	* _선택사항_: 본문의 크기(바이트)입니다. 이 매개변수는 본문의 크기를 자동으로 판별할 수 없는 경우에 유용합니다
		* 플래그: `--content-length SIZE`
	* _선택사항_: 데이터의 base64 인코딩 128비트 MD5 요약입니다.
		* 플래그: `--content-md5 MD5`
	* _선택사항_: 오브젝트 데이터 형식을 설명하는 표준 MIME 유형입니다.
		* 플래그: `--content-type MIME`
	* _선택사항_: 저장할 메타데이터의 맵입니다. 구문: KeyName1=string,KeyName2=string
		* 플래그: `--metadata MAP`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 파트 업로드
{: #ic-upload-part}
* **조치:** 기존 다중 파트 업로드 인스턴스에 파일의 파트를 업로드합니다.
* **사용법:** `ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* 각 업로드한 파일의 파트 번호 및 각 파트에 대한 ETag(CLI가 인쇄함)를 JSON 파일에 저장해야 합니다. 자세한 정보는 "다중 파트 업로드 안내서"를 참조하십시오.
* **제공할 매개변수:**
	* 다중 파트 업로드가 수행되는 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* 다중 파트 업로드를 식별하는 업로드 ID입니다.
		* 플래그: `--upload-id ID`
	* 업로드하는 파트의 파트 번호입니다. 이는 1 - 10,000 범위의 양의 정수입니다(기본값: 1).
		* 플래그: `--part-number NUMBER`
	* _선택사항_: 오브젝트 데이터 위치입니다(`FILE_PATH`).
		* 플래그: `--body FILE_PATH`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 파트 사본 업로드
{: #ic-upload-a-part-copy}
* **조치:** 기존 오브젝트에서 데이터를 복사하여 파트를 업로드합니다.
* **사용법:** `ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* 각 업로드한 파일의 파트 번호 및 각 파트에 대한 ETag(CLI가 인쇄함)를 JSON 파일에 저장해야 합니다. 자세한 정보는 "다중 파트 업로드 안내서"를 참조하십시오.
* **제공할 매개변수:**
	* 버킷 이름입니다.
		* 플래그: `--bucket BUCKET_NAME`
	* 오브젝트의 키입니다.
		* 플래그: `--key KEY`
	* 다중 파트 업로드를 식별하는 업로드 ID입니다.
		* 플래그: `--upload-id ID`
	* 업로드하는 파트의 파트 번호입니다. 이는 1 - 10,000의 양의 정수입니다.
		* 플래그: `--part-number PART_NUMBER`
	* (소스) 소스 버킷 이름과 소스 오브젝트의 키 이름이며, 슬래시(/)로 구분됩니다. URL로 인코딩되어야 합니다.
		* 플래그: `--copy-source SOURCE`
	* _선택사항_: 해당 엔티티 태그(Etag)가 지정된 태그(ETAG)와 일치하는 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-match ETAG`
	* _선택사항_: 지정된 시간(TIMESTAMP) 이후에 수정된 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-modified-since TIMESTAMP`
	* _선택사항_: 해당 엔티티 태그(ETag)가 지정된 태그(ETAG)와 다른 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-none-match ETAG`
	* _선택사항_: 지정된 시간(TIMESTAMP) 이후에 수정되지 않은 경우 오브젝트를 복사합니다.
		* 플래그: `--copy-source-if-unmodified-since TIMESTAMP`
	* _선택사항_: 소스 오브젝트에서 복사할 범위(바이트)입니다. 범위 값은 bytes=first-last 양식을 사용해야 하며, 이 양식에서 처음과 마지막은 복사할 0 기반 바이트 오프셋입니다. 예를 들어, bytes=0-9는 소스의 처음 10바이트를 복사함을 표시합니다. 소스 오브젝트가 5MB를 초과하는 경우에만 범위를 복사할 수 있습니다.
		* 플래그: `--copy-source-range value`
	* _선택사항_: 버킷이 있는 지역입니다. 이 플래그를 제공하지 않으면 프로그램에서 구성에 지정된 기본 옵션을 사용합니다.
		* 플래그: `--region REGION`
	* _선택사항_: 원시 JSON 형식으로 리턴된 출력입니다.
		* 플래그: `--json`


### 대기
{: #ic-wait}
* **조치:** 특정 조건이 충족될 때까지 대기합니다. 각 하위 명령은 나열된 요구사항이 충족될 때까지 API를 폴링합니다.
* **사용법:** `ibmcloud cos wait command [arguments...] [command options]`
* **명령:**
    * `bucket-exists`
  		* head-bucket으로 폴링하는 경우 200 응답이 수신될 때까지 대기하십시오. 성공 상태가 될 때까지 5초마다 폴링합니다. 20번의 검사 실패 후에 255 리턴 코드가 나타나며 종료됩니다.
	* `bucket-not-exists`
		* head-bucket으로 폴링하는 경우 404 응답이 수신될 때까지 대기하십시오. 성공 상태가 될 때까지 5초마다 폴링합니다. 20번의 검사 실패 후에 255 리턴 코드가 나타나며 종료됩니다.
	* `object-exists`
		* head-object로 폴링하는 경우 200 응답이 수신될 때까지 대기하십시오. 성공 상태가 될 때까지 5초마다 폴링합니다. 20번의 검사 실패 후에 255 리턴 코드가 나타나며 종료됩니다.
	* `object-not-exists`
		* head-object로 폴링하는 경우 404 응답이 수신될 때까지 대기하십시오. 성공 상태가 될 때까지 5초마다 폴링합니다. 20번의 검사 실패 후에 255 리턴 코드가 나타나며 종료됩니다.

