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


# {{site.data.keyword.cloudaccesstrailshort}}events
{: #at-events}

請使用 {{site.data.keyword.cloudaccesstrailfull}} 服務來追蹤使用者及應用程式與 {{site.data.keyword.cos_full}} 的互動情況。
{: shortdesc}

{{site.data.keyword.cloudaccesstrailfull_notm}} 服務會記錄由使用者起始並且會變更 {{site.data.keyword.Bluemix_notm}} 中服務狀態的活動。如需相關資訊，請參閱[開始使用 {{site.data.keyword.cloudaccesstrailshort}}](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started)。



## 事件清單
{: #at-events-list}

下表列出產生事件的動作：

<table>
  <caption>產生事件的動作</caption>
  <tr>
    <th>動作</th>
	  <th>說明</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>使用者要求儲存區 meta 資料以及是否已在儲存區上啟用 IBM Key Protect 時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>使用者建立儲存區時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>使用者要求儲存區中的物件清單時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>使用者更新儲存區時（例如，使用者重新命名儲存區時），即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>使用者刪除儲存區時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>使用者將儲存區上的存取控制清單設為 `public-read` 或 `private` 時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>使用者讀取儲存區上的存取控制清單時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>使用者建立儲存區的跨原點資源共用配置時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>使用者要求是否在儲存區啟用跨原點資源共用配置時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>使用者修改儲存區的跨原點資源共用配置時，即會產生事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>使用者刪除儲存區的跨原點資源共用配置時，即會產生事件。</td>
  </tr>
</table>



## 事件的檢視位置
{: #at-ui}

您可以在 {{site.data.keyword.cloudaccesstrailshort}} **帳戶網域**中取得 {{site.data.keyword.cloudaccesstrailshort}} 事件。

事件會傳送至 {{site.data.keyword.cloudaccesstrailshort}} 地區，而此地區最接近[服務支援頁面](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability)上所顯示的 {{site.data.keyword.cos_full_notm}} 儲存區位置。
