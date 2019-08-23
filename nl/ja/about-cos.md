---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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


# {{site.data.keyword.cos_full_notm}} について
{: #about-ibm-cloud-object-storage}

{{site.data.keyword.cos_full}} を使用して保管される情報は、暗号化され、複数の地理的位置に分散され、REST API を使用して HTTP を介してアクセスされます。このサービスは、{{site.data.keyword.cos_full_notm}} システム (旧称 Cleversafe) が提供する分散ストレージ・テクノロジーを利用しています。

{{site.data.keyword.cos_full_notm}} では、「クロス地域」、「地域」、および「単一データ・センター」という 3 種類の回復力 (レジリエンシー) を使用できます。「クロス地域」では、単一地域を使用する場合よりも耐久性と可用性が高くなりますが、その代わりに待ち時間が少し長くなります。これは現在のところ US、EU、および AP で使用可能です。「地域」サービスでは、これらのトレードオフが逆になり、単一の地域内の複数のアベイラビリティー・ゾーンにオブジェクトが分散されます。これは US、EU、および AP 地域で使用可能です。1 つの地域またはアベイラビリティー・ゾーンが使用不可になっても、オブジェクト・ストアは障害なく機能し続けます。「単一データ・センター」では、物理的に同じ場所にある複数のマシンにオブジェクトが分散されます。使用可能な地域については、[ここ](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)を参照してください。

開発者は、{{site.data.keyword.cos_full_notm}} API を使用してオブジェクト・ストレージと対話します。この資料は、アカウントのプロビジョニングの[開始](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)、バケットの作成、オブジェクトのアップロード、および一般的な API 対話を支援します。

## その他の IBM オブジェクト・ストレージ・サービス
{: #about-other-cos}
現在、{{site.data.keyword.cloud_notm}} は、{{site.data.keyword.cos_full_notm}} に加えて、ユーザーの多様なニーズに応じていくつかのオブジェクト・ストレージ・オファリングを提供しています。これらはすべて Web ベースのポータルおよび REST API を介してアクセス可能です。[詳細はこちら。](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
