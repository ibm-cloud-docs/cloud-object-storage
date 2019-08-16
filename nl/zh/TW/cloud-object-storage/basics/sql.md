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

# 使用 SQL Query
{: #sql-query}

IBM Cloud SQL Query 是一項受全面管理的服務，可讓您執行 SQL 查詢（即 `SELECT` 陳述式）以分析、轉換或清除矩形資料。
{:shortdesc}

您只能使用 SQL Query 來建立 `SELECT` 陳述式；無法執行 `CREATE`、`DELETE`、`INSERT` 及 `UPDATE` 這類動作。
{:tip}

輸入資料是從位於一個以上 IBM Cloud Object Storage 實例中的 ORC、CSV、JSON 或 Parquet 檔案進行讀取。每個查詢結果都會寫入至所選擇 Cloud Object Storage 實例中的 CSV 檔案。

您可以擷取下列物件的 SQL 可查詢 URL：所選取個別物件的物件（物件 SQL URL）或目前顯示作用中字首過濾器的所有物件（已過濾的 SQL URL）。您可以在 SQL 陳述式內使用此 URL 作為表格名稱。
{:tip}

如需使用 SQL Query 的相關資訊，請參閱 [SQL Query 文件](/docs/services/sql-query?topic=sql-query-overview)及[使用 IBM Cloud SQL Query 分析資料](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)。
