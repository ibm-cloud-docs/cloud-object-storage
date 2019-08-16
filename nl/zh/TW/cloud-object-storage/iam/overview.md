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

# IAM 概觀
{: #iam-overview}

{{site.data.keyword.cloud}} Identity & Access Management 可讓您安全地鑑別使用者，並在 {{site.data.keyword.cloud_notm}} Platform 中一致地控制對所有雲端資源的存取。如需相關資訊，請參閱[入門指導教學](/docs/iam?topic=iam-getstarted#getstarted)。

## 身分管理
{: #iam-overview-identity}

身分管理包括使用者、服務與資源的互動。使用者依其 IBM ID 識別。服務依其服務 ID 識別。而且，會使用 CRN 來識別及處理資源。

「{{site.data.keyword.cloud_notm}} IAM 記號服務」可讓您建立、更新、刪除及使用適用於使用者和服務的 API 金鑰。使用 API 呼叫或「{{site.data.keyword.cloud}} Platform 主控台」的 Identity & Access 區段，即可建立這些 API 金鑰。在多個服務之間，可以使用相同的金鑰。每位使用者都可以有多個 API 金鑰來支援金鑰替換情境，以及針對不同用途使用不同金鑰的情境，以限制單一金鑰的曝光。

如需相關資訊，請參閱[何謂 Cloud IAM？](/docs/iam?topic=iam-iamoverview#iamoverview)。

### 使用者及 API 金鑰
{: #iam-overview-user-api-keys}

{{site.data.keyword.cloud_notm}} 使用者可以建立及使用 API 金鑰，以用於自動化及編寫 Script 用途，以及在使用 CLI 時聯合登入。可以在 Identity and Access Management 使用者介面或使用 `ibmcloud` CLI 來建立 API 金鑰。

### 服務 ID 及 API 金鑰
{: #iam-overview-service-id-api-key}

「IAM 記號服務」可讓您建立「服務 ID」以及「服務ID」的 API 金鑰。「服務 ID」類似於「功能 ID」或「應用程式 ID」，是用來鑑別服務，而不是代表使用者。

使用者可以建立「服務 ID」，並將它們連結至 {{site.data.keyword.cloud_notm}} Platform 帳戶、CloudFoundry 組織或 CloudFoundry 空間這類的範圍，雖然用於採用 IAM，但最好將「服務 ID」連結至 {{site.data.keyword.cloud_notm}} Platform 帳戶。完成此連結，以為「服務 ID」提供一個駐留在其中的容器。此容器還會定義誰可以更新及刪除「服務 ID」，以及誰可以建立、更新、讀取及刪除與該「服務 ID」相關聯的「API 金鑰」。請務必注意，「服務 ID」與使用者「不相關」。

### 金鑰替換
{: #iam-overview-key-rotation}

應該定期替換 API 金鑰，以防止洩漏的金鑰造成的所有安全侵害。

## 存取管理
{: #iam-overview-access-management}

「IAM 存取控制」提供一個為 {{site.data.keyword.cloud_notm}} 資源指派使用者角色的常見方式，並且控制使用者可以對這些資源執行的動作。您可以跨帳戶或組織來檢視及管理使用者（視所給定的存取選項而定）。例如，帳戶擁有者會自動獲指派 Identity and Access Managemement 的帳戶「管理者」角色，讓他們能夠指派及管理適用於所有其帳戶成員的服務原則。

### 使用者、角色、資源及原則
{: #iam-overview-access-policies}

「IAM 存取控制」可讓您指派每個服務或服務實例的原則，以容許在指派的環境定義內管理資源及使用者的存取層次。原則會使用屬性組合來定義適用的資源集，以將一組資源的一或數個角色授與給使用者。當您將原則指派給使用者時，請先指定服務，然後指定要指派的一或數個角色。取決於您所選取的服務，可能會有其他配置選項可供使用。

雖然角色是一組動作，但對映至這些角色的動作是服務所特有的。每個服務都會決定上線處理程序期間此角色與動作的對映，而且此對映適用於服務的所有使用者。「角色及存取原則」是透過「原則管理點 (PAP)」配置，並且透過「原則強制執行點 (PEP)」及「原則決策點 (PDP)」強制執行。

請參閱[組織使用者、團隊、應用程式的最佳作法](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications)，以進一步瞭解。
