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

# 端點及儲存位置
{: #endpoints}

傳送 REST API 要求或配置儲存空間用戶端時，需要設定目標端點或 URL。每個儲存空間位置都會有自己的一組 URL。

大部分使用者都應該針對給定的儲存空間位置使用下列其中一個端點。專用端點應該是在 IBM 雲端內使用，而且不會產生資料傳送費用。公用端點應該是在 IBM 雲端外使用，而且確實會產生傳送費用。如果可能的話，建議使用專用端點。

自 2018 年 12 月開始，我們已更新端點。[舊式端點](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)會繼續運作，直到另行通知為止。請更新應用程式，以使用這裡所列的新端點。
{:note}

## 地區端點
{: #endpoints-region}

在地區端點上建立的儲存區會將資料散佈至三個資料中心，並分散到都會區。其中任何一個資料中心都可能會遇到中斷甚至破壞，而不影響可用性。

<table>
  <thead>
    <tr>
      <th>地區</th>
      <th>類型</th>
      <th>端點</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>美國南部</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>美國東部</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>歐盟英國</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>歐盟德國</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>亞太澳洲</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>亞太日本</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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

## 跨地區端點
{: #endpoints-geo}

在跨地區端點上建立的儲存區會將資料散佈至三個地區。其中任何一個地區都可能會遇到中斷甚至破壞，而不影響可用性。使用「邊界閘道通訊協定 (BGP)」遞送，將要求遞送至最近地區的資料中心。在中斷的情況下，會將要求自動重新遞送至作用中地區。想要撰寫自己的失效接手邏輯的進階使用者，可以透過將要求傳送至[特定存取點](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)並略過 BGP 遞送來達成此作業。

<table>
  <thead>
    <tr>
      <th>地區</th>
      <th>類型</th>
      <th>端點</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>美國跨地區 </td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>歐盟跨地區</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>亞太跨地區</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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



## 單一資料中心端點
{: #endpoints-zone}

單一資料中心不會與 IBM Cloud 服務（例如 IAM 或 Key Protect）共置，而且在發生網站中斷或破壞時，無法提供任何備援。 

如果網路失敗導致資料中心無法到達核心 IBM Cloud 地區以存取 IAM 的分割區，則會從快取中讀取可能變成過時的鑑別及授權資訊。這可能會導致長達 24 小時無法強制執行新的或已變更的 IAM 原則。
{:important}

<table>
  <thead>
    <tr>
      <th>地區</th>
      <th>類型</th>
      <th>端點</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>荷蘭阿姆斯特丹</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>印度清奈</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>香港</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>澳洲墨爾本</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>墨西哥，墨西哥市</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>義大利米蘭</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>加拿大蒙特婁</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>挪威奧斯陸</td>
      <td>公用</td>
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
      <td>美國聖荷西</td>
      <td>公用</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>巴西聖保羅</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>南韓首爾</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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
      <td>加拿大多倫多</td>
      <td>
        <p>公用</p>
        <p>專用</p>
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

