---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: activity tracker, event logging, observability

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
{:table: .aria-labeledby="caption"}


# {{site.data.keyword.cloudaccesstrailshort}} 이벤트
{: #at-events}

{{site.data.keyword.cloudaccesstrailfull}} 서비스를 사용하여 사용자 및 애플리케이션이 {{site.data.keyword.cos_full}}와 상호작용하는 방법을 추적합니다.
{: shortdesc}

{{site.data.keyword.cloudaccesstrailfull_notm}} 서비스는 {{site.data.keyword.Bluemix_notm}}에서 서비스 상태를 변경하는 사용자 시작 활동을 기록합니다. 자세한 정보는 [{{site.data.keyword.cloudaccesstrailshort}} 시작하기](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started)를 참조하십시오.



## 이벤트 목록
{: #at-events-list}

다음 표에는 이벤트를 생성하는 조치가 나열되어 있습니다.

<table>
  <caption>이벤트를 생성하는 조치</caption>
  <tr>
    <th>조치</th>
	  <th>설명</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>사용자가 버킷 메타데이터와 IBM Key Protect가 버킷에서 사용되는지 여부를 요청할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>사용자가 버킷을 작성할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>사용자가 버킷에서 오브젝트 목록을 요청할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>사용자가 버킷을 업데이트할 때(예: 사용자가 버킷의 이름을 바꿀 때) 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>사용자가 버킷을 삭제할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>사용자가 버킷의 액세스 제어 목록을 `public-read` 또는 `private`으로 설정할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>사용자가 버킷의 액세스 제어 목록을 읽을 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>사용자가 버킷에 대한 CORS(Cross-Origin Resource Sharing) 구성을 작성할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>CORS(Cross-Origin Resource Sharing) 구성이 버킷에서 사용 가능한 경우 사용자가 요청할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>사용자가 버킷에 대한 CORS(Cross-Origin Resource Sharing) 구성을 수정할 때 이벤트가 생성됩니다.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>사용자가 버킷에 대한 CORS(Cross-Origin Resource Sharing) 구성을 삭제할 때 이벤트가 생성됩니다.</td>
  </tr>
</table>



## 이벤트를 볼 수 있는 위치
{: #at-ui}

{{site.data.keyword.cloudaccesstrailshort}} 이벤트가 {{site.data.keyword.cloudaccesstrailshort}} **계정 도메인**에서 사용 가능합니다.

이벤트가 [서비스 지원 페이지](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability)에 표시된 {{site.data.keyword.cos_full_notm}} 버킷 위치에 가장 가까운 {{site.data.keyword.cloudaccesstrailshort}} 지역으로 전송됩니다.
