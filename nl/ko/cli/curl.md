---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, curl, cli

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

# `curl` 사용
{: #curl}

여기에는 {{site.data.keyword.cos_full}} REST API에 대한 기본 `curl` 명령의 'cheatsheet'가 있습니다. 추가 세부사항은 [버킷](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) 또는 [오브젝트](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations)에 대한 API 참조에 있습니다.

`curl` 사용 시 명령행 및 오브젝트 스토리지에 익숙하다고 가정하고 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), [엔드포인트 참조](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) 또는 [콘솔](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)에서 필수 정보를 가져왔습니다. 용어나 변수가 익숙하지 않은 경우 이를 [용어집](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)에서 찾을 수 있습니다.

**참고**: 개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}

## IAM 토큰 요청
{: #curl-iam}

요청을 인증하기 위해 IAM oauth 토큰을 생성하는 두 가지 방법이 있습니다. 즉, API 키와 함께 `curl` 명령을 사용하거나(아래에 설명됨) [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli)를 사용하여 명령행에서 생성합니다. 

### API 키를 사용하여 IAM 토큰 요청
{: #curl-token}

먼저 API 키가 있는지 확인하십시오. [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys)에서 이 키를 가져오십시오.

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## 리소스 인스턴스 ID 확보
{: #curl-instance-id}

다음 명령 중 일부에는 `ibm-service-instance-id` 매개변수가 필요합니다. 이 값을 찾으려면 클라우드 콘솔에 있는 오브젝트 스토리지 인스턴스의 **서비스 인증 정보** 탭으로 이동하십시오. 필요한 경우 새 인증 정보를 작성한 다음 *인증 정보 보기* 드롭 다운을 사용하여 JSON 형식을 확인하십시오. `resource_instance_id` 값을 사용하십시오. 

curl API에서 사용하려면 마지막 단일 콜론 다음에 시작하여 마지막 이중 콜론 앞에서 끝나는 UUID만 필요합니다. 예를 들어, ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::`는 `39d8d161-22c4-4b77-a856-f11db5130d7d`로 축약될 수 있습니다.
{:tip}

## 버킷 나열
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## 버킷 추가
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## 버킷 추가(스토리지 클래스)
{: #curl-add-bucket-class}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}

`LocationConstraint`에 대한 올바른 프로비저닝 코드 목록은 [스토리지 클래스 안내서](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint)에서 참조할 수 있습니다.

## 버킷 CORS 작성
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

`Content-MD5` 헤더는 base64 인코딩 MD5 해시의 2진 표시여야 합니다.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## 버킷 CORS 가져오기
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 버킷 CORS 삭제
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 오브젝트 나열
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 버켓 헤더 가져오기
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 버킷 삭제
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 오브젝트 업로드
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## 오브젝트의 헤더 가져오기
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 오브젝트 복사
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## CORS 정보 확인
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## 오브젝트 다운로드
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 오브젝트의 ACL 확인
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 오브젝트에 대한 익명의 액세스 허용
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## 오브젝트 삭제
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 다중 오브젝트 삭제
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

`Content-MD5` 헤더는 base64 인코딩 MD5 해시의 2진 표시여야 합니다.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## 다중 파트 업로드 시작
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 파트 업로드
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## 다중 파트 업로드 완료
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## 불완전한 다중 파트 업로드 가져오기
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## 불완전한 다중 파트 업로드 중단
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
