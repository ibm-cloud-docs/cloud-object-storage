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

# SQL Query 사용
{: #sql-query}

IBM Cloud SQL Query는 SQL 조회(즉, `SELECT`문)를 실행하여 사각형 데이터를 분석하거나 변환하거나 정리할 수 있는 전체 관리 서비스입니다.
{:shortdesc}

SQL Query를 사용하여 `SELECT`문만 작성할 수 있습니다. `CREATE`, `DELETE`, `INSERT` 및 `UPDATE`와 같은 조치는 불가능합니다.
{:tip}

입력 데이터는 하나 이상의 IBM Cloud Object Storage 인스턴스에 있는 ORC, CSV, JSON 또는 Parquet 파일에서 읽어옵니다. 각 조회 결과는 선택한 Cloud Object Storage 인스턴스의 CSV 파일에 기록됩니다.

선택한 개별 오브젝트(Object SQL URL) 또는 활성 접두부 필터를 사용하여(SQL URL로 필터링됨) 현재 표시된 모든 오브젝트에 대해 오브젝트의 SQL 조회 가능 URL을 검색할 수 있습니다. SQL문 내에서 이 URL을 테이블 이름으로 사용할 수 있습니다.
{:tip}

SQL Query 사용에 대한 자세한 정보는 [SQL Query 문서](/docs/services/sql-query?topic=sql-query-overview) 및 [IBM Cloud SQL Query로 데이터 분석](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)을 참조하십시오.
