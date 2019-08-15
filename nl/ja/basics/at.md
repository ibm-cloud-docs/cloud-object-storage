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


# {{site.data.keyword.cloudaccesstrailshort}} イベント
{: #at-events}

{{site.data.keyword.cloudaccesstrailfull}} サービスを使用して、ユーザーとアプリケーションが {{site.data.keyword.cos_full}} とどのように対話するのかを追跡します。
{: shortdesc}

{{site.data.keyword.cloudaccesstrailfull_notm}} サービスは、{{site.data.keyword.Bluemix_notm}} 内のサービスの状態を変更するユーザー開始アクティビティーを記録します。 詳しくは、[{{site.data.keyword.cloudaccesstrailshort}} の概要](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started)を参照してください。



## イベント・リスト
{: #at-events-list}

以下の表では、イベントを生成するアクションをリストします。

<table>
  <caption>イベントを生成するアクション</caption>
  <tr>
    <th>アクション</th>
	  <th>説明</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>ユーザーがバケット・メタデータと、IBM Key Protect がバケットで有効になっているかどうかを要求すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>ユーザーがバケットを作成すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>ユーザーがバケット内のオブジェクトのリストを要求すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>ユーザーがバケットを更新する (例: バケットの名前を変更する) と、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>ユーザーがバケットを削除すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>ユーザーがバケットのアクセス制御リストを `public-read` または `private` に設定すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>ユーザーがバケットのアクセス制御リストを読み取ると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>ユーザーがバケットのクロス・オリジン・リソース共有構成を作成すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>ユーザーがバケットでクロス・オリジン・リソース共有構成がバケットで有効になっているかどうかを要求すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>ユーザーがバケットのクロス・オリジン・リソース共有構成を変更すると、イベントが生成されます。</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>ユーザーがバケットのクロス・オリジン・リソース共有構成を削除すると、イベントが生成されます。</td>
  </tr>
</table>



## イベントの表示先
{: #at-ui}

{{site.data.keyword.cloudaccesstrailshort}} イベントは、{{site.data.keyword.cloudaccesstrailshort}} **アカウント・ドメイン**内で使用可能です。

イベントは、[サポートされているサービスに関するページ](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability)に表示される {{site.data.keyword.cos_full_notm}} バケットのロケーションに最も近い {{site.data.keyword.cloudaccesstrailshort}} 地域に送信されます。
