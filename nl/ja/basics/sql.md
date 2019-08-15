---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud services, integration, sql, query, analytics

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

# SQL Query の使用
{: #sql-query}

IBM Cloud SQL Query は、矩形データの分析、変換、またはクリーンアップを行うために SQL 照会 (つまり `SELECT` ステートメント) の実行を可能にする、完全に管理されたサービスです。
{:shortdesc}

SQL Query を使用して作成できるのは `SELECT` ステートメントのみであり、`CREATE`、`DELETE`、`INSERT`、および `UPDATE` などのアクションは作成できません。
{:tip}

入力データは、1 つ以上の IBM Cloud Object Storage インスタンスに置かれた、ORC ファイル、CSV ファイル、JSON ファイル、または Parquet ファイルから読み取られます。各照会結果は、選択した Cloud Object Storage インスタンス内の CSV ファイルに書き込まれます。

オブジェクトの SQL 照会可能 URL を、選択した 1 つの個別オブジェクトに対して取得する (オブジェクト SQL URL) か、アクティブな接頭部フィルターを使用して現在表示されているすべてのオブジェクトに対して取得する (フィルターされた SQL URL) ことができます。この URL を SQL ステートメントの内部で表名として使用できます。
{:tip}

SQL Query の使用について詳しくは、[SQL Query 資料](/docs/services/sql-query?topic=sql-query-overview)および [Analyzing Data with IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053) を参照してください。
