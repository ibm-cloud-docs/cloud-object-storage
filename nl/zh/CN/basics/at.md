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


# {{site.data.keyword.cloudaccesstrailshort}} 事件
{: #at-events}

使用 {{site.data.keyword.cloudaccesstrailfull}} 服务来跟踪用户和应用程序如何与 {{site.data.keyword.cos_full}} 交互。
{: shortdesc}

{{site.data.keyword.cloudaccesstrailfull_notm}} 服务会记录 {{site.data.keyword.Bluemix_notm}} 中用户发起的会更改服务状态的活动。有关更多信息，请参阅 [{{site.data.keyword.cloudaccesstrailshort}} 入门](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started)。



## 事件列表
{: #at-events-list}

下表列出了生成事件的操作：

<table>
  <caption>生成事件的操作</caption>
  <tr>
    <th>操作</th>
	  <th>描述</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>用户请求存储区元数据并确定存储区上是否启用了 IBM Key Protect 时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>用户创建存储区时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>用户请求存储区中对象的列表时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>用户更新存储区（例如，用户重命名存储区）时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>用户删除存储区时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>用户将存储区上的访问控制表设置为 `public-read` 或 `private` 时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>用户读取存储区上的访问控制表时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>用户为存储区创建跨源资源共享配置时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>用户请求确定是否在存储区上启用了跨源资源共享配置时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>用户修改存储区的跨源资源共享配置时，会生成事件。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>用户删除存储区的跨源资源共享配置时，会生成事件。</td>
  </tr>
</table>



## 查看事件的位置
{: #at-ui}

{{site.data.keyword.cloudaccesstrailshort}} 事件在 {{site.data.keyword.cloudaccesstrailshort}} **帐户域**中提供。

事件将发送到距离 {{site.data.keyword.cos_full_notm}} 存储区位置最近的 {{site.data.keyword.cloudaccesstrailshort}} 区域，如[“支持的服务”页面](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability)上所示。
