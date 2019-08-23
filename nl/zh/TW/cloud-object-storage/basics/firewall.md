---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-21"

keywords: ip address, firewall, configuration, api

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

# 設定防火牆
{: #setting-a-firewall}

IAM 原則提供一種方式，讓管理者限制個別儲存區的存取權。如果只能從信任的網路存取某些資料，會發生什麼事？除非要求來自容許的 IP 位址清單，否則儲存區防火牆會限制資料的所有存取權。
{: shortdesc}

設定防火牆時，有一些規則：

* 設定或檢視防火牆的使用者必須具有儲存區的`管理員`角色。 
* 具有儲存區`管理員`角色的使用者，可以從任何 IP 位址檢視及編輯容許的 IP 位址清單，以防止意外鎖定。
* 「{{site.data.keyword.cos_short}} 主控台」仍然可以存取儲存區，前提是已授權使用者的 IP 位址。
* 其他 {{site.data.keyword.cloud_notm}} 服務**未獲授權**，無法略過防火牆。此限制表示依賴 IAM 原則來存取儲存區的其他服務（例如 Aspera、SQL Query、Security Advisor、Watson Studio、Cloud Functions 等等）將無法這麼做。

設定防火牆時，儲存區會與 {{site.data.keyword.cloud_notm}} 的其餘部分隔離。在啟用防火牆之前，請考量這對相依於其他服務的應用程式及工作流程的影響程度，而這些服務直接存取儲存區。
{: important}

## 使用主控台來設定防火牆
{: #firewall-console}

首先，請確定您有一個儲存區。否則，請遵循[入門指導教學](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)來熟悉主控台。

### 設定授權 IP 位址清單
{: #firewall-console-enable}

1. 從 {{site.data.keyword.cloud_notm}} [主控台儀表板](https://cloud.ibm.com/)中，選取**儲存空間**以檢視資源清單。
2. 接下來，從**儲存空間**功能表中選取具有儲存區的服務實例。這會將您帶往「{{site.data.keyword.cos_short}} 主控台」。
3. 選擇您要限制授權 IP 位址存取權的儲存區。 
4. 從導覽功能表中，選取**存取原則**。
5. 選取**授權 IP** 標籤。
6. 按一下**新增 IP 位址**，然後選擇**新增**。
7. 新增 [CIDR 表示法](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing)中的 IP 位址清單，例如 `192.168.0.0/16, fe80:021b::0/64`。位址可以遵循 IPv4 或 IPv6 標準。按一下**新增**。
8. 除非在主控台中儲存位址，否則不會強制執行防火牆。按一下**全部儲存**，以強制執行防火牆。
9. 現在，只能從這些 IP 位址存取此儲存區中的所有物件！

### 移除任何 IP 位址限制
{: #firewalls-console-disable}

1. 從**授權 IP** 標籤中，勾選要從授權清單中移除的所有 IP 位址或範圍旁的方框。
2. 選取**刪除**，然後再按一次**刪除**來確認對話框。
3. 除非在主控台中儲存變更，否則不會強制執行已更新的清單。按一下**全部儲存**，以強制執行新的規則。
4. 現在，只能從這些 IP 位址存取此儲存區中的所有物件！

如果未列出授權 IP 位址，則表示一般 IAM 原則將套用至儲存區，而且使用者的 IP 位址沒有任何限制。
{: note}


## 透過 API 設定防火牆
{: #firewall-api}

防火牆是使用 [COS 資源配置 API](https://cloud.ibm.com/apidocs/cos/cos-configuration) 進行管理的。這個新的 REST API 用於配置儲存區。 

具有`管理員`角色的使用者，可以從任何網路檢視及編輯容許的 IP 位址清單，以防止意外鎖定。
{: tip}
