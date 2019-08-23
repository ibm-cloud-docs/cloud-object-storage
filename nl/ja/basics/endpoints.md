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

# エンドポイントおよびストレージ・ロケーション
{: #endpoints}

REST API 要求の送信またはストレージ・クライアントの構成には、ターゲット・エンドポイントまたは URL の設定が必要です。各ストレージ・ロケーションには、独自の URL のセットがあります。

ほとんどのユーザーは、所定のストレージ・ロケーションに対して以下のいずれかのエンドポイントを使用する必要があります。プライベート・エンドポイントは IBM Cloud 内から使用される必要があり、データ転送料金は発生しません。パブリック・エンドポイントは、IBM Cloud の外部から使用される必要があり、転送料金が発生します。可能な場合はプライベート・エンドポイントの使用をお勧めします。

2018 年 12 月時点で、エンドポイントを更新しました。別に発表があるまで[レガシー・エンドポイント](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)は引き続き機能します。ここにリストされている新しいエンドポイントを使用するようにアプリケーションを更新してください。
{:note}

## 地域エンドポイント
{: #endpoints-region}

地域エンドポイントで作成されるバケットでは、1 つの大都市圏に散在する 3 つのデータ・センターにデータが分散されます。これらのデータ・センターのいずれかが停止したり破壊されたりしても、可用性に影響はありません。

<table>
  <thead>
    <tr>
      <th>地域</th>
      <th>タイプ</th>
      <th>エンドポイント</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>米国南部</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>米国東部</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>EU 英国</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>EU ドイツ</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>AP オーストラリア</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>AP 日本</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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

## クロス地域エンドポイント
{: #endpoints-geo}

クロス地域エンドポイントで作成されたバケットでは、3 つの地域にデータが分散されます。これらの地域のいずれかが停止したり破壊されたりしても、可用性に影響はありません。要求は Border Gateway Protocol (BGP) ルーティングを使用して、最も近い地域のデータ・センターに転送されます。停止が発生した場合、要求はアクティブな地域に自動的に再転送されます。独自のフェイルオーバー・ロジックを作成したい上級者は、[特定のアクセス・ポイント](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)に要求を送信し、BGP ルーティングをバイパスすることができます。

<table>
  <thead>
    <tr>
      <th>地域</th>
      <th>タイプ</th>
      <th>エンドポイント</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>米国クロス地域</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>EU クロス地域</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>AP クロス地域</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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



## 単一データ・センター・エンドポイント
{: #endpoints-zone}

単一データ・センターは、IAM や Key Protect などの IBM Cloud サービスと同じ場所にあるのではなく、サイトが停止したり破壊されたりした場合の回復力はありません。 

ネットワーク障害が原因で、データ・センターが IAM にアクセスするためにコア IBM Cloud 地域に到達できないパーティションが発生すると、無効になった可能性のあるキャッシュから認証情報と許可情報が読み取られます。その結果、新規または変更された IAM ポリシーが最大 24 時間適用されない可能性があります。
{:important}

<table>
  <thead>
    <tr>
      <th>地域</th>
      <th>タイプ</th>
      <th>エンドポイント</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>アムステルダム、オランダ</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>チェンナイ、インド</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>ホンコン</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>メルボルン、オーストラリア</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>メキシコ・シティー、メキシコ</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>ミラノ、イタリア</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>モントリオール、カナダ</td>
      <td>
        <p>パブリック</p>
        <p>プライベート</p>
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
      <td>オスロ、ノルウェー</td>
      <td>パブリック</td>
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
      <td>サンノゼ、米国</td>
      <td>パブリック</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>サンパウロ、ブラジル</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>ソウル、韓国</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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
      <td>トロント、カナダ</td>
      <td>
        <p>パブリック</p>
        <p>プライベート
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

