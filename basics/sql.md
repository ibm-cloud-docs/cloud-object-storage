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

# Use SQL Query
{: #sql-query}

IBM Cloud SQL Query is a fully-managed service that lets you run SQL queries (that is, `SELECT` statements) to analyze, transform, or clean up rectangular data.
{:shortdesc}

You can use SQL Query to create `SELECT` statements only; actions such as `CREATE`, `DELETE`, `INSERT`, and `UPDATE` are not possible.
{:tip}

Input data is read from ORC, CSV, JSON, or Parquet files located in one or more IBM Cloud Object Storage instances. Each query result is written to a CSV file in a Cloud Object Storage instance of your choice.

You can retrieve a SQL queryable URL for objects for a selected individual object (Object SQL URL) or for all objects currently displayed with an active prefix filter (Filtered SQL URL). You can use this URL inside the SQL statement as the table name.
{:tip}

For more information on using SQL Query see the [SQL Query documentation](/docs/services/sql-query?topic=sql-query-overview) and [Analyzing Data with IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).
