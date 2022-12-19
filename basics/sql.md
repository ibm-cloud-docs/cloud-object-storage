---

copyright:
  years: 2017, 2020
lastupdated: "2020-12-15"

keywords: cloud services, integration, sql, query, analytics

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Using SQL Query
{: #sql-query}

{{site.data.keyword.sqlquery_full}} is a fully-managed service that lets you run SQL queries (that is, `SELECT` statements) to analyze, transform, or clean up rectangular data using the full ANSI SQL standard.
{: shortdesc}

When you use the console to interact with your instance of {{site.data.keyword.cos_full_notm}}, there are instances of IBM Cloud SQL Query automatically recognised in the new "Integrations" panel. You can also create new instances of {{site.data.keyword.sqlquery_notm}} directly from the "Integrations" panel in your browser. See Figure 1, showing the option to integrate services like {{site.data.keyword.sqlquery_short}} next to your credentials and buckets.

![Integrations in COS](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/integrate-from-cos.jpg){: caption="Figure 1. Integrate SQL Query from COS instance"}

Select the card detailing the options and offering for {{site.data.keyword.sqlquery_notm}}. This is shown in Figure 2.

![Integrate SQL Query](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/integrate-with-sql.jpg){: caption="Figure 2. Select the SQL Query card to integrate"}

Figure 3 shows the panel that opens when the card is selected. This panel give you control over the location and costs regarding your new instance of {{site.data.keyword.sqlquery_notm}}. Select the region appropriate for your buckets and the plan suitable for your projects. If you want more information you can use the documentation to [get started](/docs/sql-query?topic=sql-query-gettingstarted).

![Create SQL Instance](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/create-sql-instance-cos.jpg){: caption="Figure 3. Fill out form fields to configure your instance"}

## Querying {{site.data.keyword.cos_short}} with SQL Query
{: #sql-query-querying-objects}

You can use SQL Query to create `SELECT` statements only; actions such as `CREATE`, `DELETE`, `INSERT`, and `UPDATE` are not possible.
{: tip}

Input data for your queries are read from ORC, CSV, JSON, or Parquet files located in one or more {{site.data.keyword.cos_full_notm}} instances. Each query result is written by default to a CSV file in a Cloud Object Storage instance where you created the integration. But you can freely override and customise the format and {{site.data.keyword.cos_short}} location as part of the SQL statement that you run.

You can use a custom `INTO` clause of a `SELECT` statement to control where and how result data from a `SELECT` statement is written to {{site.data.keyword.cos_full_notm}}.
{: note}

Getting started using SQL Query `SELECT` statements from inside your instance is as easy as creating an integration. Objects of queryable data formats, as well as folders with multiple objects of a consistent queryable format (when shown in the "folders" view) are labeled as shown in Figure 4.

![Object with SQL label](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/accessible-using-sql.jpg){: caption="Figure 4. SQL label shows queryable objects"}

You can retrieve a SQL queryable URL for objects for a selected individual object (Object SQL URL) or for all objects currently displayed with an active prefix filter (Filtered SQL URL). You can use this URL inside the SQL statement as the table name.
{: tip}

Figure 5 shows how to access your data using {{site.data.keyword.sqlquery_short}}. When you click on the ellipses at the end of a row of an object that you can query, you will see a menu where you can "Access with SQL" by selecting that option.

![Access with SQL](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/access-with-sql.jpg){: caption="Figure 5. Access with SQL shows on queryable objects"}

The panel shown in Figure 6 shows how to access your data using {{site.data.keyword.sqlquery_short}}. The location of your object appears in the panel for reference outside of the console. The instances to which you have access appear in the dropdown list in the panel. After you specify the instance, click on "Open in SQL Query" to launch your instance already pre-populated with a sample query written in the appropriate SQL.

![Open with SQL](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/open-with-sql.jpg){: caption="Figure 6. Access with SQL launch panel"}

Access is based on permissions, and you may wish to study more about [authentication and access](/docs/sql-query?topic=sql-query-authentication#accessauthentication).
{: important}

## Getting Results
{: #sql-query-getting-results}

Figure 7 shows a sample SQL query you can modify. By pressing the "Run" button, the list below the query will populate with a new entry that links to your results. The results will be stored in the location shown beneath the query.

![SQL Query Window](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/select-with-sql.jpg){: caption="Figure 7. Access with SQL query window"}

The entry representing the job of the `SELECT` statement run previously is shown in Figure 8. There are two tabs, "Results" and "Details," at the top of the list that allow you to switch between seeing the results and more detailed information.

![SQL Query Results](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/results-from-sql.jpg){: caption="Figure 8. Access with SQL query jobs"}

The entry representing the details of running the `SELECT` statement run previously is shown in Figure 9. 

![SQL Query Details](https://docs-resources.s3.us.cloud-object-storage.appdomain.cloud/details-from-sql.jpg){: caption="Figure 9. Access with SQL query jobs"}

## Next Steps
{: #sql-query-next-steps}

For more information on using {{site.data.keyword.sqlquery_short}} see the [{{site.data.keyword.sqlquery_short}} documentation](/docs/sql-query?topic=sql-query-overview) and [Analyzing Data with IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).
