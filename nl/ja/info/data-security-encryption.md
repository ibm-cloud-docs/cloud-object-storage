---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# データ・セキュリティーと暗号化
{: #security}

{{site.data.keyword.cos_full}} は、セキュリティー、可用性、および信頼性を確保しながら、大量の非構造化データをコスト効率よく保管するための革新的なアプローチを使用します。この実現には、Information Dispersal Algorithms (IDAs) が使用され、データをデータ・センターのネットワークに分散される認識不能の「スライス」に分割することで、データの伝送と保管を本質的にプライベートかつセキュアなものにします。いずれの単一ストレージ・ノード内にもデータの完全なコピーは存在せず、ノードのサブセットが使用可能であれば、ネットワーク上でデータを十分に取り出すことができます。

{{site.data.keyword.cos_full_notm}} 内の Data at Rest (保存されたデータ) はすべて暗号化されます。このテクノロジーは、オブジェクトごとに生成されたキーを使用して、各オブジェクトを個別に暗号化します。これらのキーは、All-or-Nothing Transform (AONT) を使用してオブジェクト・データを保護する同じ Information Dispersal Algorithms を使用して保護され、安全に保管されます。これにより、個々のノードまたはハード・ディスクで漏えいが発生した場合でもキー・データが公開されるのを防止します。

ユーザーが暗号鍵を管理する必要がある場合、ルート・キーは、[SSE-C を使用してオブジェクト単位で](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c)提供するか、[SSE-KP を使用してバケット単位で](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp)提供できます。

ストレージには HTTPS を介してアクセスでき、ストレージ・デバイス同士は TLS を使用して内部で相互に認証および通信します。


## データ削除
{: #security-deletion}

データが削除された後は、削除されたオブジェクトをリカバリーしたり再構成したりできないようにするさまざまなメカニズムが存在します。オブジェクトの削除はさまざまなステージを経ます。ステージには、オブジェクトが削除されたことを示すメタデータのマーク付けから、コンテンツ領域の削除、ドライブそのものでの消去のファイナライズ、そのスライス・データを表すブロックの最終的な上書きまでがあります。データ・センターで漏えいが発生したか、物理ディスクを所有しているかに応じて、オブジェクトがリカバリー不能になる時間は、削除操作のフェーズによって異なります。メタデータ・オブジェクトが更新されると、データ・センター・ネットワークの外部にあるクライアントはオブジェクトの読み取りを実行できなくなります。コンテンツ領域を表すスライスの大半がストレージ・デバイスによってファイナライズされると、そのオブジェクトにアクセスすることはできません。

## テナントの分離
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} は、共有インフラストラクチャーであり、マルチテナント・オブジェクト・ストレージ・ソリューションです。ワークロードが専用ストレージまたは分離ストレージを必要とする場合、詳細については、[{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) にアクセスしてください。
