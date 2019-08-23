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

# Utilisation de SQL Query
{: #sql-query}

IBM Cloud SQL Query est un service entièrement géré qui vous permet d'exécuter des requêtes SQL (c'est-à-dire des instructions `SELECT`) pour analyser, transformer ou nettoyer des données rectangulaires. {:shortdesc}

Vous pouvez utiliser SQL Query pour créer des instructions `SELECT` uniquement ; des actions telles que `CREATE`, `DELETE`, `INSERT`et `UPDATE` ne sont pas possibles. {:tip}

Les données d'entrée sont lues à partir des fichiers ORC, CSV, JSON ou Parquet situés dans une ou plusieurs instances IBM Cloud Object Storage. Chaque résultat de requête est écrit dans un fichier CSV dans l'instance Cloud Object Storage de votre choix. 

Vous pouvez extraire une URL utilisable dans une requête SQL pour des objets, pour un objet individuel sélectionné (URL SQL Object) ou pour tous les objets actuellement affichés avec un filtre de préfixe actif (URL SQL filtrée). Vous pouvez utiliser cette URL dans l'instruction SQL comme nom de table. {:tip}

Pour plus d'informations sur l'utilisation de SQL Query, voir la [documentation SQL Query](/docs/services/sql-query?topic=sql-query-overview) et l'article [Analyzing Data with IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053). 
