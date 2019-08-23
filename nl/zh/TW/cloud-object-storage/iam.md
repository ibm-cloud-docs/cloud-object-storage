---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# 開始使用 IAM
{: #iam}

## Identity and Access Management 角色及動作
{: #iam-roles}

對於您帳戶中的使用者，對 {{site.data.keyword.cos_full}} 服務實例的存取權是由 {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM) 所控制。對於存取您帳戶中 {{site.data.keyword.cos_full}} 服務的每個使用者，必須向其指派定義了 IAM 使用者角色的存取原則。該原則確定使用者可在您所選服務或實例的環境定義中執行的動作。容許的作業由 {{site.data.keyword.Bluemix_notm}} 服務進行自訂，並定義為容許在服務上執行的動作。然之後，這些動作會對映至 IAM 使用者角色。

原則可讓您在不同層次授與存取權。部分選項包括： 

* 對帳戶中所有服務實例的存取權
* 對您帳戶中個別服務實例的存取權
* 對實例中特定資源的存取權
* 對您帳戶中所有已啟用 IAM 功能之服務的存取權

在定義存取原則的範圍後，可以指派角色。請檢閱下列各表，其概述每個角色在 {{site.data.keyword.cos_short}} 服務內所容許的動作。

下表詳述對映至平台管理角色的動作。平台管理角色可讓使用者對平台層次的服務資源執行作業，例如，指派服務的使用者存取、建立或刪除服務 ID、建立實例，以及將實例連結至應用程式。

| 平台管理角色 | 動作說明 | 範例動作 |
|:-----------------|:-----------------|:-----------------|
| 檢視者 | 檢視服務實例，但不進行修改 | <ul><li>列出可用的 COS 服務實例</li><li>檢視 COS 服務方案詳細資料</li><li>檢視使用情形詳細資料</li></ul>|
| 編輯者 | 執行所有平台動作，但管理帳戶以及指派存取原則除外 |<ul><li>建立及刪除 COS 服務實例</li></ul> |
| 操作員 | COS 未使用 | 無 |
|管理者| 根據獲指派此角色的資源來執行所有平台動作，包括將存取原則指派給其他使用者 |<ul><li>更新使用者原則</li>更新定價方案</ul>|
{: caption="表 1. IAM 使用者角色和動作"}


下表詳細描述了對映到服務存取角色的動作。服務存取角色可讓使用者存取 {{site.data.keyword.cos_short}} 並且能夠呼叫 {{site.data.keyword.cos_short}} API。

| 服務存取角色 | 動作說明 | 範例動作 |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| 內容讀者            | 下載物件，但不列出物件或儲存區 | <ul><li>下載物件</li></ul> |
| 讀者                | 除了「內容讀者」動作之外，「讀者」還可以列出儲存區及（或）物件，但不進行修改。| <ul><li>列出儲存區</li><li>列出及下載物件</li></ul>                    |
| 撰寫者              | 除了「讀者」動作之外，「撰寫者」還可以建立儲存區以及上傳物件。| <ul><li>建立新的儲存區及物件</li><li>移除儲存區及物件</li></ul> |
| 管理員              | 除了「撰寫者」動作之外，「管理員」還可以完成影響存取控制的特許動作。| <ul><li>新增保留原則</li><li>新增儲存區防火牆</li></ul>              |
{: caption="表 3. IAM 服務存取角色及動作"}


如需在使用者介面中指派使用者角色的相關資訊，請參閱[管理 IAM 存取](/docs/iam?topic=iam-iammanidaccser)。
 
