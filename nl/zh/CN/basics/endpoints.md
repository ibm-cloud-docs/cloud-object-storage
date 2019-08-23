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

# 端点和存储位置
{: #endpoints}

发送 REST API 请求或配置存储客户机需要设置目标端点或 URL。每个存储位置都有自己的一组 URL。

大多数用户应该使用给定存储位置的下列其中一个端点。专用端点应该在 IBM Cloud 内使用，这不会产生数据传输费用。公共端点应该在 IBM Cloud 外部使用，这会产生传输费用。建议尽可能使用专用端点。

从 2018 年 12 月开始，我们已更新了端点。[旧端点](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)将继续运行，直到发布进一步通知为止。请更新应用程序以使用此处列出的新端点。
{:note}

## 区域端点
{: #endpoints-region}

在区域端点上创建的存储区在一个大城市区域中分布的三个数据中心分布数据。其中任一数据中心遇到中断甚至破坏，都不会影响可用性。

<table>
  <thead>
    <tr>
      <th>区域</th>
      <th>类型</th>
      <th>端点</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>美国南部</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>美国东部</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>欧盟 - 英国</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>欧盟 - 德国</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>亚太地区 - 澳大利亚</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>亚太地区 - 日本</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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

## 跨区域端点
{: #endpoints-geo}

在跨区域端点上创建的存储区在三个区域中分布数据。其中任一区域遇到中断甚至破坏，都不会影响可用性。请求使用边界网关协议 (BGP) 路由来路由到距离最近的区域的数据中心。如果发生中断，请求会自动重新路由到活动区域。高级用户如果希望编写自己的故障转移逻辑，可以通过向[特定访问点](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints)发送请求并绕过 BGP 路由来实现。

<table>
  <thead>
    <tr>
      <th>区域</th>
      <th>类型</th>
      <th>端点</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>美国跨区域</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>欧盟跨区域</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>亚太地区跨区域</td>
      <td>
        <p>公共</p>
        <p>专用
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



## 单个数据中心端点
{: #endpoints-zone}

单个数据中心不与 IBM Cloud 服务（例如，IAM 或 Key Protect）位于相同位置，并且在发生站点中断或破坏的情况下不能提供任何弹性。 

如果联网故障导致分区中的数据中心无法通过访问核心 IBM Cloud 区域来访问 IAM，那么会从高速缓存中读取认证和授权信息，但这些信息可能已变得陈旧。这可能导致最长 24 小时内无法强制实施新的或变更的 IAM 策略。
{:important}

<table>
  <thead>
    <tr>
      <th>区域</th>
      <th>类型</th>
      <th>端点</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>荷兰阿姆斯特丹</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>印度金奈</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>中国香港特別行政区</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>澳大利亚墨尔本</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>墨西哥的墨西哥城</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>意大利米兰</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>加拿大蒙特利尔</td>
      <td>
        <p>公共</p>
        <p>专用</p>
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
      <td>挪威奥斯陆</td>
      <td>公共</td>
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
      <td>美国圣何塞</td>
      <td>公共</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>巴西圣保罗</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>韩国首尔</td>
      <td>
        <p>公共</p>
        <p>专用
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
      <td>加拿大多伦多</td>
      <td>
        <p>公共</p>
        <p>专用
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

