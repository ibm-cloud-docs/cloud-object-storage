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

# Utilizza SQL Query
{: #sql-query}

IBM Cloud SQL Query è un servizio completamente gestito che ti consente di eseguire query SQL (ossia istruzioni `SELECT`) per analizzare, trasformare o ripulire i dati rettangolari.
{:shortdesc}

Puoi utilizzare SQL Query per creare solo istruzioni `SELECT`; azioni come `CREATE`, `DELETE`, `INSERT` e `UPDATE` non sono possibili.
{:tip}

I dati di input vengono letti dai file ORC, CSV, JSON o Parquet che si trovano in una o più istanze di IBM Cloud Object Storage. Ogni risultato della query viene scritto in un file CSV in un'istanza Cloud Object Storage di tua scelta. 

Puoi richiamare un URL queryable SQL per gli oggetti per un singolo oggetto selezionato (URL SQL oggetto) o per tutti gli oggetti attualmente visualizzati con un filtro di prefisso attivo (URL SQL filtrato). Puoi utilizzare questo URL all'interno di un'istruzione SQL come il nome tabella.
{:tip}

Per ulteriori informazioni sull'utilizzo di SQL Query, vedi la [documentazione di SQL Query](/docs/services/sql-query?topic=sql-query-overview) e [Analyzing Data with IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).
