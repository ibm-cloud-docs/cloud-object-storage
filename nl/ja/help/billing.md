---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# 請求
{: #billing}

価格設定については、[{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window} を参照してください。

## 請求書
{: #billing-invoices}

アカウントの請求書は、ナビゲーション・メニューの**「管理」** > **「請求および使用量」**で確認できます。

各アカウントは単一の請求を受け取ります。異なるコンテナー・セットごとに個別の請求が必要な場合は、複数のアカウントを作成する必要があります。

## {{site.data.keyword.cos_full_notm}} 価格設定
{: #billing-pricing}

{{site.data.keyword.cos_full}} のストレージ・コストは、保管されるデータの合計ボリューム、パブリック・アウトバウンド帯域幅の使用量、およびシステムによって処理される操作要求の総数によって決まります。

インフラストラクチャー・オファリングは、パブリック・トラフィック、プライベート・トラフィック、および管理トラフィックにセグメント化された 3 層のネットワークに接続されます。インフラストラクチャー・サービスは、プライベート・ネットワークを介して無料で相互にデータを転送できます。インフラストラクチャー・オファリング (ベアメタル・サーバー、仮想サーバー、クラウド・ストレージなど) は、パブリック・ネットワークを介して {{site.data.keyword.cloud_notm}} プラットフォーム・カタログ内のその他のアプリケーションやサービス (Watson サービス、Cloud Foundry ランタイムなど) に接続します。このため、これら 2 つのタイプのオファリング間で行われるデータ転送は、標準のパブリック・ネットワーク帯域幅レートで計測され、課金されます。
{: tip}

## 要求クラス
{: #billing-request-classes}

「クラス A」の要求には、変更またはリスト作成が関係します。このカテゴリーには、バケットの作成、オブジェクトのアップロードまたはコピー、構成の作成または変更、バケットのリスト、およびバケットの内容のリストが含まれます。

「クラス B」の要求は、オブジェクトまたはオブジェクトに関連したメタデータや構成をシステムから取得することに関連します。

バケットまたはオブジェクトをシステムから削除しても料金は発生しません。

| クラス | 要求 | 例 |
|--- |--- |--- |
| クラス A | PUT、COPY、および POST の各要求と、バケットおよびオブジェクトをリストするのに使用される GET 要求 | バケットの作成、オブジェクトのアップロードまたはコピー、バケットのリスト、バケットの内容のリスト、ACL の設定、および CORS 構成の設定 |
| クラス B | GET (リスト作成を除く)、HEAD、および OPTIONS の各要求 | オブジェクトおよびメタデータの取得 |

## Aspera 転送
{: #billing-aspera}

[Aspera 高速転送](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)では、下りの追加料金が発生します。詳しくは、[価格設定のページ](https://www.ibm.com/cloud/object-storage#s3api)を参照してください。

## ストレージ・クラス
{: #billing-storage-classes}

保管されるすべてのデータに必ずしも頻繁にアクセスする必要があるわけでなく、一部のアーカイブ・データはほぼアクセスされない可能性もあります。あまりアクティブでないワークロードを対象に、異なるストレージ・クラス内にバケットを作成できます。そのようなバケット内に保管されるオブジェクトは、標準ストレージとは異なるスケジュールで課金されます。

以下の 4 つのクラスがあります。

*  **Standard** - アクティブなワークロード用です。データの取得に料金はかかりません (ただし、操作要求そのものの費用は別です)。
*  **Vault** - データへのアクセスが 1 カ月に 1 回のペースに満たないような、あまり頻繁にアクセスされないワークロード用です。データが読み取られるたびに追加の取得料金 ($/GB) が適用されます。サービスには、使用頻度が低い、あまりアクティブでないデータ向けであるこのサービスの使用目的に合ったオブジェクト・サイズと保管期間に関する最小しきい値が含まれます。
*  **Cold Vault** - データへのアクセスが 90 日以上おきのような、めったにアクセスされないワークロード用です。データが読み取られるたびに、より高額の追加の取得料金 ($/GB) が適用されます。サービスには、使用頻度がかなり低い、非アクティブ・データ向けであるこのサービスの使用目的に合ったオブジェクト・サイズと保管期間に関する長めの最小しきい値が含まれます。
*  **Flex** - アクセス・パターンの予測が難しい動的ワークロード用です。使用量に応じて、取得料金のコストが上限値を超える場合は、取得料金がドロップされ、代わりに新しい容量料金が適用されます。このストレージは、データが頻繁にアクセスされない場合は Standard ストレージよりもコスト効率が良く、アクセス使用パターンが予期せずアクティブになる場合は、Vault ストレージまたは Cold Vault ストレージよりもコスト効率が良くなります。Flex には、最小のオブジェクト・サイズや保管期間に関する要件はありません。

価格設定について詳しくは、[ibm.com の価格設定表](https://www.ibm.com/cloud/object-storage#s3api)を参照してください。

異なるストレージ・クラスでのバケットの作成について詳しくは、[API リファレンス](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class)を参照してください。
