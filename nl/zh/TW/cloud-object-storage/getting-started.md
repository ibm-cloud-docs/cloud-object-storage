---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# 入門指導教學
{: #getting-started}

本入門指導教學會逐步解說下列作業所需的步驟：建立儲存區、上傳物件，以及設定存取原則，以容許其他使用者使用您的資料。
{: shortdesc}

## 開始之前
{: #gs-prereqs}

您需要：
  * [{{site.data.keyword.cloud}} Platform 帳戶](https://cloud.ibm.com)
  * [{{site.data.keyword.cos_full_notm}} 實例](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * 以及本端電腦上一些要上傳的檔案。
{: #gs-prereqs}

 本指導教學會帶領新使用者完成 {{site.data.keyword.cloud_notm}} Platform 主控台的首要步驟。想要開始使用 API 的開發人員，請參閱[開發人員手冊](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev)或 [API 概觀](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)。

## 建立一些儲存區來儲存資料
{: #gs-create-buckets}

  1. [訂購 {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) 時會建立_服務實例_。{{site.data.keyword.cos_full_notm}} 是一種多方承租戶系統，所有 {{site.data.keyword.cos_short}} 實例都會共用實體基礎架構。您會自動被重新導向至您可以從中開始建立儲存區的服務實例。{{site.data.keyword.cos_short}} 實例列在[資源清單](https://cloud.ibm.com/resources)的**儲存空間**下。

「資源實例 (resource instance)」及「服務實例 (service instance)」術語指的是相同的概念，可以交換使用。
{: tip}

  1. 遵循**建立儲存區**，並選擇唯一名稱。全球所有地區中的所有儲存區都共用單一名稱空間。請確定您具有[正確的許可權](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions)可以建立儲存區。

  **附註**：當您建立儲存區或新增物件時，請務必避免使用「個人識別資訊 (PII)」。PII 是可依下列方式來識別任何使用者（自然人）的資訊：依名稱、位置或任何其他方法。
  {: tip}

  1. 先選擇想要的[_備援_ 層次](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)，然後選擇您希望實際儲存資料的_位置_。備援是指資料分散的地理區域範圍及規模。_跨地區_ 備援會將資料分散到數個都會區域，而_地區_ 備援會將資料分散到單一都會區域。_單一資料中心_ 只會將資料分散到單一站台內的裝置。
  2. 選擇[儲存區的_儲存空間類別_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes)，這反映您預期讀取所儲存資料的頻率，並決定計費詳細資料。請遵循**建立**鏈結，以建立及存取新的儲存區。

儲存區是一種組織資料的方式，但它們並不是唯一的方式。物件名稱（通常稱為_物件索引鍵_）可以針對目錄型組織系統使用一個以上的正斜線。然後，您可以在定界字元之前使用物件名稱的部分來形成_物件字首_，用來透過 API 列出單一儲存區中的相關物件。
{: tip}


## 將部分物件新增至儲存區
{: #gs-add-objects}

現在，請繼續並從清單中選取其中一個儲存區，然後移至該儲存區。按一下**新增物件**。新物件會改寫相同儲存區內同名的現有物件。當您使用主控台來上傳物件時，物件名稱一律會符合檔名。如果您使用 API 來寫入資料，則檔名與物件索引鍵之間不需要有任何關係。請繼續，並將一些檔案新增至此儲存區。

除非您使用 [Aspera 高速傳輸](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload)外掛程式，否則透過主控台上傳時，會將物件限制為 200 MB。較大型物件（最多 10 TB）也可以[分割為數個組件並使用 API 平行上傳](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)。物件索引鍵的長度最多可為 1024 個字元，而且最好避免在網址中會出現問題的所有字元。例如，如果不是 URL 編碼，則 `?`、`=`、`<` 及其他特殊字元可能會造成不想要的行為。
{:tip}

## 邀請使用者加入您的帳戶，以管理您的儲存區及資料
{: #gs-invite-user}

現在，您將帶入另一位使用者，並容許他們擔任實例及其中所儲存之任何資料的管理者。

  1. 首先，若要新增使用者，您需要離開現行 {{site.data.keyword.cos_short}} 介面並前往 IAM 主控台。移至**管理**功能表，並遵循**存取 (IAM)** > **使用者**上的鏈結。按一下**邀請使用者**。
	<img alt="IAM 邀請使用者" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`圖 1：IAM 邀請使用者`
  2. 輸入您要邀請加入組織的使用者的電子郵件位址，然後展開**服務**區段，並從**將存取權指派給**功能表中選取「資源」。現在，從**服務**功能表中，選擇 "Cloud Object Storage"。
	<img alt="IAM 服務" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`圖 2：IAM 服務`
  3. 現在，會出現三個其他欄位：_服務實例_、_資源類型_ 及_資源 ID_。第一個欄位定義使用者可以存取的 {{site.data.keyword.cos_short}} 實例。它也可以設為將相同的存取層次授與所有 {{site.data.keyword.cos_short}} 實例。現在，可以讓其他欄位保留為空白。
	<img alt="IAM 邀請使用者" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`圖 3：IAM 邀請使用者`
  4. **選取角色**下的勾選框決定可供使用者使用的動作集。選取「管理者」平台存取角色，以容許使用者將其他[使用者及服務 ID](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) 存取權授與實例。選取「管理員」服務存取角色，以容許使用者管理 {{site.data.keyword.cos_short}} 實例以及建立與刪除儲存區和物件。這些_主旨_（使用者）、_角色_（管理員）及_資源_（{{site.data.keyword.cos_short}} 服務實例）組合會一同形成 [IAM 原則](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam)。如需角色及原則的其他詳細指引，[請參閱 IAM 文件](/docs/iam?topic=iam-userroles)。
	<img alt="IAM 角色" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`圖 4：IAM 選取角色`
  5. {{site.data.keyword.cloud_notm}} 使用 Cloud Foundry 作為基礎帳戶管理平台，因此，您必須授與最低的 Cloud Foundry 存取層次，使用者才能在第一時間存取您的組織。請從**組織**功能表中選取組織，然後從**組織角色**及**空間角色**功能表中選取「審核員」。設定 Cloud Foundry 許可權容許使用者檢視您組織可用的服務，但不會變更它們。

## 讓開發人員存取儲存區。
{: #gs-bucket-policy}

  1. 導覽至**管理**功能表，並遵循**存取 (IAM)** > **服務 ID** 上的鏈結。您可以在這裡建立_服務 ID_，作為連結至帳戶的抽象身分。服務 ID 可以獲指派 API 金鑰，並在您不想將特定「開發人員」身分連結至應用程式程序或元件的狀況下使用。
	<img alt="IAM 服務 ID" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`圖 5：IAM 服務 ID`
  2. 重複上述處理程序，但在步驟 3 中，選擇特定服務實例，並將「儲存區」輸入為_資源類型_，並將現有儲存區的完整 CRN 輸入為_資源 ID_。
  3. 現在，服務 ID 可以存取該特定儲存區，但不能存取其他儲存區。

## 後續步驟
{: #gs-next-steps}

現在，您已透過 Web 型主控台熟悉 Object Storage，則可能會有興趣從指令行使用 `ibmcloud cos` 指令行公用程式來建立服務實例並與 IAM 互動，以及使用 `curl` 來直接存取 COS，以執行類似的工作流程。[請查看 API 概觀](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)，以開始使用。
