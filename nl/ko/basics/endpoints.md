---

copyright:
  years: 2017
lastupdated: "2018-05-25"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}

# 엔드포인트 및 스토리지 위치
{: #endpoints}

REST API 요청을 보내거나 스토리지 클라이언트를 구성하려면 대상 엔드포인트 또는 URL을 설정해야 합니다. 각 스토리지 위치에는 고유 URL 세트가 있습니다.

대부분의 사용자는 지정된 스토리지 위치에 다음 엔드포인트 중 하나를 사용해야 합니다. 개인용 엔드포인트는 IBM Cloud 내에서 사용되어야 하며 데이터 전송 비용을 발생시키지 않습니다. 공용 엔드포인트는 IBM Cloud 외부에서 사용되어야 하며 데이터 전송 비용을 발생시키지 않습니다. 가능하면 개인용 엔드포인트를 사용하는 것이 좋습니다.

2018년 12월부로 엔드포인트가 업데이트되었습니다. [레거시 엔드포인트](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)는 추가 통지가 있을 때까지 계속 작동합니다. 애플리케이션을 업데이트하여 여기에 나열된 새 엔드포인트를 사용하십시오.
{:note}

## 지역 엔드포인트
{: #endpoints-region}

지역 엔드포인트에서 작성된 버킷은 도시권에 분산된 세 개의 데이터 센터에 데이터를 분배합니다. 이러한 데이터 센터 중 하나에서 가동 중단 또는 심지어 파괴가 발생하는 경우에도 가용성에는 영향을 주지 않습니다.

<table>
  <thead>
    <tr>
      <th>지역</th>
      <th>유형</th>
      <th>엔드포인트</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>미국 남부</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us-south.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us-south.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>미국 동부</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us-east.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us-east.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>유럽 연합 영국</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-gb.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu-gb.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>유럽 연합 독일</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-de.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu-de.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>아시아 태평양 오스트레일리아</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.au-syd.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.au-syd.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>아시아 태평양 일본</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.jp-tok.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.jp-tok.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

## 교차 지역 엔드포인트
{: #endpoints-geo}

교차 지역 엔드포인트에서 작성된 버킷이 세 지역에 데이터를 분배합니다. 이러한 지역 중 하나에서 가동 중단 또는 심지어 파괴가 발생하는 경우에도 가용성에는 영향을 주지 않습니다. 요청은 BGP(Border Gateway Protocol) 라우팅을 사용하여 가장 가까운 지역의 데이터 센터로 라우팅됩니다. 가동 중단의 경우 요청이 자동으로 활성 지역으로 다시 라우팅됩니다. 고유 장애 복구 로직을 작성하려는 고급 사용자는 요청을 [특정 액세스 지점](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)으로 보내 BGP 라우팅을 우회하여 이를 수행할 수 있습니다.

<table>
  <thead>
    <tr>
      <th>지역</th>
      <th>유형</th>
      <th>엔드포인트</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>미국 교차 지역</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>유럽 연합 교차 지역</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>아시아 태평양 교차 지역</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}



## 단일 데이터 센터 엔트포인트
{: #endpoints-zone}

단일 데이터 센터는 IAM 또는 Key Protect와 같은 IBM Cloud 서비스와 함께 위치하지 않으며 사이트 가동 중단 또는 파괴가 발생하는 경우 복원성이 없습니다. 

네트워크 장애로 인해 데이터 센터가 IAM에 액세스하기 위한 코어 IBM Cloud 지역에 도달할 수 없는 파티션이 생기는 경우 인증 및 권한 정보를 시간이 경과되는(stale) 캐시에서 읽게 됩니다. 이로 인해 새롭거나 변경된 IAM 정책이 최대 24시간 동안 시행되지 않을 수 있습니다.
{:important}

<table>
  <thead>
    <tr>
      <th>지역</th>
      <th>유형</th>
      <th>엔드포인트</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>암스테르담, 네덜란드</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ams03.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.ams03.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>첸나이, 인도</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.che01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.che01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>홍콩</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.hkg02.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.hkg02.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>멜버른, 오스트레일리아</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mel01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mel01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>멕시코 시티, 멕시코</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mex01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mex01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>밀라노, 이탈리아</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mil01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mil01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>몬트리올, 캐나다</td>
      <td>
        <p>공용</p>
        <p>개인용</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mon01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mon01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>오슬로, 노르웨이</td>
      <td>공용</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.osl01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.osl01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>산호세, 미국</td>
      <td>공용</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>상파울루, 브라질</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sao01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sao01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>서울, 대한민국</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.seo01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.seo01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>토론토, 캐나다</td>
      <td>
        <p>공용</p>
        <p>개인용
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.tor01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.tor01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

