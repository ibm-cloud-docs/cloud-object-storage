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

IBM Cloud SQL Query 是一个完全受管服务，支持运行 SQL 查询（即 `SELECT` 语句）来分析、变换或清除矩形数据。
{:shortdesc}

只能使用 SQL Query 来创建 `SELECT` 语句；无法执行 `CREATE`、`DELETE`、`INSERT` 和 `UPDATE` 等操作。
{:tip}

输入数据是从位于一个或多个 IBM Cloud Object Storage 实例中的 ORC、CSV、JSON 或 Parquet 文件中读取的。每个查询结果都会写入您选择的 Cloud Object Storage 实例中的 CSV 文件。

您可以针对所选单个对象或者针对当前使用活动前缀过滤器显示的所有对象，检索对象的 SQL 可查询 URL，前者为对象 SQL URL，后者为过滤的 SQL URL。可以在 SQL 语句中使用此 URL 作为表名。
{:tip}

有关使用 SQL Query 的更多信息，请参阅 [SQL Query 文档](/docs/services/sql-query?topic=sql-query-overview)和 [Analyzing Data with IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)。
