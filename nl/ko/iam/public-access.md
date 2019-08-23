---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# 공용 액세스 허용
{: #iam-public-access}

데이터를 공유해야 하는 경우가 있습니다. 버킷은 학술 및 개인 연구를 위한 공개 데이터 세트 또는 웹 애플리케이션 및 컨텐츠 전달 네트워크에서 사용하는 이미지 저장소를 포함할 수 있습니다. **공용 액세스** 그룹을 사용하여 이러한 버킷을 액세스 가능하도록 설정하십시오.
{: shortdesc}

## 콘솔을 사용한 공용 액세스 설정
{: #iam-public-access-console}

먼저 버킷이 있는지 확인하십시오. 없는 경우에는 [시작하기 튜토리얼](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)을 수행하여 콘솔 사용법을 숙지하십시오. 

### 공용 액세스 사용
{: #public-access-console-enable}

1. {{site.data.keyword.cloud_notm}} [콘솔 대시보드](https://cloud.ibm.com/)에서 **스토리지**를 선택하여 리소스 목록을 보십시오. 
2. 그런 다음 **스토리지** 메뉴 내에서 버킷이 있는 서비스 인스턴스를 선택하십시오. 이렇게 하면 {site.data.keyword.cos_short} 콘솔로 이동됩니다. 
3. 공용으로 액세스할 수 있도록 할 버킷을 선택하십시오. 이 정책은 해당 URL이 있는 모든 사용자가 _버킷에 있는 모든 오브젝트_를 다운로드할 수 있도록 설정한다는 점을 기억하십시오. 
4. 탐색 메뉴에서 **액세스 정책**을 선택하십시오. 
5. **공용 액세스** 탭을 선택하십시오. 
6. **액세스 정책 작성**을 클릭하십시오. 경고를 읽은 후 **사용**을 선택하십시오. 
7. 이제 이 버킷에 있는 모든 오브젝트에 공용으로 액세스할 수 있습니다. 

### 공용 액세스 사용 안함
{: #public-access-console-disable}

1. {{site.data.keyword.cloud_notm}} [콘솔](https://cloud.ibm.com/)의 어느 위치에서든 **관리** 메뉴를 선택하고 **액세스(IAM)**를 선택하십시오. 
2. 탐색 메뉴에서 **액세스 그룹**을 선택하십시오. 
3. **공용 액세스**를 선택하여 현재 사용 중인 모든 공용 액세스 정책의 목록을 보십시오. 
4. 액세스 제어를 다시 적용할 버킷에 대응하는 정책을 찾으십시오. 
5. 정책 항목의 맨 오른쪽에 있는 조치 목록에서 **제거**를 선택하십시오. 
6. 대화 상자를 확인하고 나면 해당 정책이 버킷에서 제거됩니다. 

## 개별 오브젝트에 대한 공용 액세스 허용
{: #public-access-object}

REST API를 통해 오브젝트를 공용으로 액세스할 수 있도록 하려는 경우에는 요청에 `x-amz-acl: public-read` 헤더를 포함시킬 수 있습니다. 이 헤더를 설정하면 모든 [IAM 정책](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) 확인이 무시되며 인증되지 않은 `HEAD` 및 `GET` 요청이 가능해집니다. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 

또한 [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)는 [사전 서명된 URL을 사용하는 임시 공용 액세스](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url) 허용을 가능하게 합니다. 

### 공용 오브젝트 업로드
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### 기존 오브젝트에 대한 공용 액세스 허용
{: #public-access-object-existing}

페이로드가 없는 `?acl` 매개변수와 `x-amz-acl: public-read` 헤더를 사용하면 데이터를 겹쳐쓰지 않으면서 오브젝트에 대한 공용 액세스를 허용합니다. 

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### 공용 오브젝트를 다시 개인용으로 설정
{: #public-access-object-private}

페이로드가 없는 `?acl` 조회 매개변수와 비어 있는 `x-amz-acl:` 헤더를 사용하면 데이터를 겹쳐쓰지 않으면서 오브젝트에 대한 공용 액세스를 취소합니다. 

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## 정적 웹 사이트
{: #public-access-static-website}

{{site.data.keyword.cos_full_notm}}는 자동 정적 웹 사이트 호스팅을 지원하지 않지만, 웹 서버를 수동으로 구성하고 이를 사용하여 버킷에서 호스팅되는 공용 액세스 가능한 컨텐츠를 제공하는 것은 가능합니다. 자세한 정보는 [이 튜토리얼](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos)을 참조하십시오. 
