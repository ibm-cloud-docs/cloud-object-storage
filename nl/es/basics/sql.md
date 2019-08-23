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

# Utilización de SQL Query
{: #sql-query}

IBM Cloud SQL Query es un servicio completamente gestionado que le permite ejecutar consultas SQL (es decir, sentencias `SELECT`) para analizar, transformar o limpiar datos.
{:shortdesc}

Solo puede utilizar SQL Query para crear sentencias `SELECT`; no se pueden ejecutar acciones como `CREATE`, `DELETE`, `INSERT` y `UPDATE`.
{:tip}

Los datos de entrada se leen de archivos ORC, CSV, JSON o Parquet que se encuentran en una o varias instancias de IBM Cloud Object Storage. El resultado de cada consulta se escribe en un archivo CSV en la instancia de Cloud Object Storage que elija.

Puede recuperar un URL que se pueda consultar con SQL para los objetos correspondientes a un objeto individual seleccionado (URL de SQL de objeto) o para todos los objetos que se visualizan actualmente con un filtro de prefijo activo (URL de SQL filtrado). Puede utilizar este URL dentro de la sentencia SQL como nombre de tabla.
{:tip}

Para obtener más información sobre cómo utilizar SQL Query, consulte la [documentación de SQL Query](/docs/services/sql-query?topic=sql-query-overview) y el apartado sobre [Análisis de datos con IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).
