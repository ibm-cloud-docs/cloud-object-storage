---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud services, integration, aspera, key protect, archive, worm

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

# 통합 서비스 가용성
{: #service-availability}

아래 표에서는 다음 서비스가 지원되는 지역을 설명합니다.
* [Aspera 고속 전송](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)
* [Key Protect](/docs/services/cloud-object-storage/basics/cloud-object-storage/basics?topic=cloud-object-storage-encryption#sse-kp)
* [데이터 아카이브](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [불변 오브젝트 스토리지](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-at-events#at_events)


Aspera 고속을 사용한 다운로드에는 추가 Egress 비용이 발생합니다. 자세한 정보는 [가격 페이지](https://www.ibm.com/cloud/object-storage)를 참조하십시오.
{:tip}

## 교차 지역
{: #service-availability-geo}

<table>
  <thead>
    <tr>
      <th>지역</th>
      <th>Aspera</th>
      <th>Key Protect</th>
      <th>데이터 아카이브</th>
      <th>불변 오브젝트 스토리지</th>
      <th>Activity Tracker</th>
    </tr>
  </thead>
  <tr>
    <td>아시아 태평양 교차 지역</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>시드니</td>
  </tr>
  <tr>
    <td>유럽 연합 교차 지역</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>영국</td>
  </tr>
  <tr>
    <td>미국 교차 지역</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>미국 남부</td>
  </tr>
 </table>





## 지역
{: #service-availability-region}

<table>
  <thead>
    <tr>
      <th>지역</th>
      <th>Aspera</th>
      <th>Key Protect</th>
      <th>데이터 아카이브</th>
      <th>불변 오브젝트 스토리지</th>
      <th>Activity Tracker</th>
    </tr>
  </thead>
   <tr>
    <td>아시아 태평양 오스트레일리아</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>시드니</td>
   </tr>
   <tr>
    <td>아시아 태평양 일본</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>시드니</td>
   </tr>
   <tr>
    <td>유럽 연합 영국</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>영국</td>
   </tr>
   <tr>
    <td>유럽 연합 독일</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>영국</td>
   </tr>
   <tr>
    <td>미국 남부</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>미국 남부</td>
   </tr>
   <tr>
    <td>미국 동부</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>예</td>
    <td>미국 남부</td>
   </tr>
</table>



## 단일 데이터 센터
{: #service-availability-zone}

<table>
  <thead>
    <tr>
      <th>지역</th>
      <th>Aspera</th>
      <th>Key Protect</th>
      <th>데이터 아카이브</th>
      <th>불변 오브젝트 스토리지</th>
      <th>Activity Tracker</th>
    </tr>
  </thead>
  <tr>
    <td>암스테르담, 네덜란드</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>영국</td>
  </tr>
  <tr>
    <td>첸나이, 인도</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>시드니</td>
  </tr>
  <tr>
    <td>홍콩</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>시드니</td>
  </tr>
  <tr>
    <td>멜버른, 오스트레일리아</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>시드니</td>
  </tr>
  <tr>
    <td>멕시코 시티, 멕시코</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>미국 남부</td>
  </tr>
  <tr>
    <td>밀라노, 이탈리아</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>영국</td>
  </tr>
  <tr>
    <td>몬트리올, 캐나다</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>미국 남부</td>
  </tr>
  <tr>
    <td>오슬로, 노르웨이</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>영국</td>
  </tr>
  <tr>
    <td>산호세, 미국</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>미국 남부</td>
  </tr>
  <tr>
    <td>상파울루, 브라질</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>미국 남부</td>
  </tr>
  <tr>
    <td>서울, 대한민국</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>시드니</td>
  </tr>
  <tr>
    <td>토론토, 캐나다</td>
    <td>예</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>아니오</td>
    <td>미국 남부</td>
  </tr>
</table>

