---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, simpana

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


# CommVault Simpana を使用したデータのアーカイブ
{: #commvault}

CommVault Simpana は、{{site.data.keyword.cos_full_notm}} のアーカイブ層と統合されます。Simpana について詳しくは、[CommVault Simpana 資料](https://documentation.commvault.com/commvault/)を参照してください。

IBM COS Infrastructure Archive について詳しくは、[『方法』のデータのアーカイブ](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive)を参照してください。

## 統合ステップ
{: #commvault-integration}

1.	Simpana コンソールから、Amazon S3 クラウド・ストレージ・ライブラリーを作成します。 

2. サービス・ホストがエンドポイントを指していることを確認してください。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。Simpana は、このステップでバケットをプロビジョンします。あるいは、プロビジョンされたバケットを使用することもできます。 

3.	バケットでポリシーを作成します。AWS CLI、SDK、または Web コンソールを使用して、ポリシーを作成できます。以下に、ポリシーの例を示します。

```shell
{
  "Rules": [
    {
      "ID": "CommVault",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transitions": [
        {
        "Days": 0,
        "StorageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

### ポリシーとバケットの関連付け
{: #commvault-assign-policy}

1. 以下の CLI コマンドを実行します。

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	Simpana を使用してストレージ・ポリシーを作成し、最初のステップで作成したクラウド・ストレージ・ライブラリーにストレージ・ポリシーを関連付けます。ストレージ・ポリシーは、Simpana がバックアップ転送のために COS と対話する方法を制御します。ポリシーの概要については、[こちら](https://documentation.commvault.com/commvault/v11/article?p=13804.htm)を参照してください。

3.	バックアップ・セットを作成し、前のステップで作成したストレージ・ポリシーにバックアップ・セットを関連付けます。バックアップ・セットの概要については、[こちら](https://documentation.commvault.com/commvault/v11/article?p=11666.htm)を参照してください。

## バックアップの実行
{: #commvault-backup}

ポリシーを使用して、バケットへのバックアップを開始し、{{site.data.keyword.cos_full_notm}} へのバックアップを実行できます。Simpana バックアップについて詳しくは、[こちら](https://documentation.commvault.com/commvault/v11/article?p=11677.htm)を参照してください。バックアップ・コンテンツは、バケットに構成されたポリシーに基づいてアーカイブ層に移行されます。

## リストアの実行
{: #commvault-restore}

{{site.data.keyword.cos_full_notm}} からバックアップ・コンテンツをリストアできます。Simpana リストアについて詳しくは、[こちら](https://documentation.commvault.com/commvault/v11/article?p=12867.htm)を参照してください。

### アーカイブ層からオブジェクトを自動的にリストアするための Simpana の構成
{: #commvault-auto-restore}

1. COS からバックアップをリストアするときに {{site.data.keyword.cos_full_notm}} リストアをトリガーするタスクを作成します。構成するには、[CommVault Simpana 資料](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)を参照してください。

2. クラウド・ストレージ再呼び出しタスクを使用して、バックアップしたコンテンツをアーカイブ層から元の層にリストアします。このタスクは、Simpana が {{site.data.keyword.cos_full_notm}} から戻りコードを受け取ると実行されます。アーカイブ再呼び出しについて詳しくは、[こちら](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)を参照してください。

3. (アーカイブ層から元の層への) リストアが完了すると、Simpana はコンテンツを読み取り、元の場所または構成された場所に書き込みます。
