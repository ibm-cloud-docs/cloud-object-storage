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

# Usar o SQL Query
{: #sql-query}

O IBM Cloud SQL Query é um serviço totalmente gerenciado que permite executar consultas SQL (ou seja, instruções `SELECT` ) para analisar, transformar ou limpar dados retangulares.
{:shortdesc}

É possível usar o SQL Query para criar somente instruções `SELECT`; ações como `CREATE`, `DELETE`, `INSERT` e `UPDATE` não são possíveis.
{:tip}

Os dados de entrada são lidos de arquivos ORC, CSV, JSON ou Parquet localizados em uma ou mais instâncias do IBM Cloud Object Storage. Cada resultado da consulta é gravado em um arquivo CSV em uma instância do Cloud Object Storage de sua escolha.

É possível recuperar uma URL apta à consulta SQL para objetos para um objeto individual selecionado (URL de SQL de objeto) ou para todos os objetos exibidos atualmente com um filtro de prefixo ativo (URL de SQL filtrada). É possível usar essa URL dentro da instrução SQL como o nome da tabela.
{:tip}

Para obter mais informações sobre como usar o SQL Query, consulte a [Documentação do SQL Query](/docs/services/sql-query?topic=sql-query-overview) e [Analisando dados com o IBM Cloud SQL Query](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).
