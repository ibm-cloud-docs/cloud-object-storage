---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# 從 OpenStack Swift 移轉資料
{: #migrate}

{{site.data.keyword.cos_full_notm}} 變成 {{site.data.keyword.cloud_notm}} Platform 服務可供使用之前，需要物件儲存庫的專案會使用 [OpenStack Swift](https://docs.openstack.org/swift/latest/) 或 [OpenStack Swift（基礎架構）](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift)。建議開發人員更新其應用程式，並將其資料移轉至 {{site.data.keyword.cloud_notm}}，以充分運用 IAM 及 Key Protect 所提供的新存取控制及加密好處，以及它們變成可用時的新特性。

Swift「容器 (container)」的概念與 COS「儲存區 (bucket)」相同。COS 將服務實例限制為 100 個儲存區，而部分 Swift 實例可能具有較大數目的容器。組織資料時，COS 儲存區可以保留數十億個物件，而且目錄型「字首」的物件名稱中支援正斜線 (`/`)。COS 支援儲存區及服務實例層次的 IAM 原則。
{:tip}

跨物件儲存空間服務移轉資料的一種方法是使用 'sync' 或 'clone' 工具，例如[開放程式碼 `rclone` 指令行公用程式](https://rclone.org/docs/)。此公用程式將同步兩個位置（包括雲端儲存空間）之間的檔案樹狀結構。當 `rclone` 將資料寫入 COS 時，它會使用 COS/S3 API 來分段大型物件，並根據設定為配置參數的大小及臨界值來平行上傳組件。

COS 與 Swift 之間有一些差異，必須視為資料移轉的一部分。

  - COS 還沒有支援到期原則或版本化。依賴這些 Swift 特性的工作流程必須改為在移轉至 COS 時，將它們當作應用程式邏輯的一部分來處理。
  - COS 支援物件層次 meta 資料，但使用 `rclone` 移轉資料時不會保留此資訊。可以使用 `x-amz-meta-{key}: {value}` 標頭對 COS 中的物件設定自訂 meta 資料，但建議先將物件層次 meta 資料備份至資料庫，再使用 `rclone`。藉由[將物件複製到自身](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object)，可以將自訂 meta 資料套用至現有物件 - 系統將會辨識物件資料相同，而且只會更新 meta 資料。請注意，`rclone` **可以**保留時間戳記。
  - COS 將 IAM 原則用於服務實例及儲存區層次存取控制。設定 `public-read` ACL，[可以將物件設為公開使用](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access)，因此不需要 authorization 標頭。
  - 相對於 Swift API，在 COS/S3 API 中，大型物件的[多部分上傳](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)的處理方式不同。
  - COS 容許熟悉的選用 HTTP 標頭，例如 `Cache-Control`、`Content-Encoding`、`Content-MD5` 及 `Content-Type`。

本手冊提供將資料從單一 Swift 容器移轉至單一 COS 儲存區的指示。這需要針對您要移轉的所有容器重複進行，然後必須更新應用程式邏輯，才能使用新的 API。移轉資料之後，您可以使用 `rclone check` 來驗證傳送的完整性，這會比較 MD5 總和檢查，並產生所有不相符物件的清單。


## 設定 {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. 如果您尚未建立，請從[型錄](https://cloud.ibm.com/catalog/services/cloud-object-storage)中佈建 {{site.data.keyword.cos_full_notm}} 的實例。
  2. 建立您將需要儲存已傳送資料的所有儲存區。請閱讀[入門手冊](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)，以充分瞭解[端點](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)及[儲存空間類別](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes)這類重要概念。
  3. 因為 Swift API 的語法明顯與 COS/S3 API 不同，所以可能需要重構應用程式，才能使用 COS SDK 中所提供的對等方法。程式庫是以（[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)）或 [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) 提供。

## 設定運算資源以執行移轉工具
{: #migrate-compute}
  1. 選擇最接近您資料的 Linux/macOS/BSD 機器或 IBM Cloud Infrastructure Bare Metal Server 或 Virtual Server。建議使用下列「伺服器」配置：32 GB RAM、2-4 核心處理器，以及專用網路速度 1000 Mbps。  
  2. 如果您在 IBM Cloud Infrastructure Bare Metal 或 Virtual Server 上執行移轉，請使用**專用** Swift 及 COS 端點。
  3. 否則，請使用**公用** Swift 及 COS 端點。
  4. 透過[套件管理程式或經過前置編譯的二進位檔](https://rclone.org/install/)安裝 `rclone`。

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## 配置 OpenStack Swift 的 `rclone`
{: #migrate-rclone}

  1. 在 `~/.rclone.conf` 中建立 `rclone` 配置檔。

        ```
        touch ~/.rclone.conf
        ```

  2. 複製下列內容，並將其貼入 `rclone.conf`，以建立 Swift 來源。

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. 取得 OpenStack Swift 認證
    <br>a. 按一下 [IBM Cloud 主控台儀表板](https://cloud.ibm.com/)中的 Swift 實例。
    <br>b. 按一下導覽畫面中的**服務認證**。
    <br>c. 按一下**新建認證**，以產生認證資訊。按一下**新增**。
    <br>d. 檢視您建立的認證，並複製 JSON 內容。

  4. 填寫下列欄位：

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. 跳到 COS 的「配置 `rclone`」區段


## 配置 OpenStack Swift（基礎架構）的 `rclone`
{: #migrate-config-swift}

  1. 在 `~/.rclone.conf` 中建立 `rclone` 配置檔。

        ```
        touch ~/.rclone.conf
        ```

  2. 複製下列內容，並將其貼入 `rclone.conf`，以建立 Swift 來源。

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. 取得 OpenStack Swift（基礎架構）認證
    <br>a. 按一下 IBM Cloud 基礎架構客戶入口網站中的 Swift 帳戶。
    <br>b. 按一下移轉來源容器的資料中心。
    <br>c. 按一下**檢視認證**。
    <br>d. 複製下列內容。
      <br>&nbsp;&nbsp;&nbsp;**使用者名稱**
      <br>&nbsp;&nbsp;&nbsp;**API 金鑰**
      <br>&nbsp;&nbsp;&nbsp;**鑑別端點**，根據您執行移轉工具的位置

  4. 使用 OpenStack Swift（基礎架構）認證，填寫下列欄位：

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## 配置 COS 的 `rclone`
{: #migrate-config-cos}

### 取得 COS 認證
{: #migrate-config-cos-credential}

  1. 按一下 IBM Cloud 主控台中的 COS 實例。
  2. 按一下導覽畫面中的**服務認證**。
  3. 按一下**新建認證**，以產生認證資訊。
  4. 在**線型配置參數**下，新增 `{"HMAC":true}`。按一下**新增**。
  5. 檢視您建立的認證，並複製 JSON 內容。

### 取得 COS 端點
{: #migrate-config-cos-endpoint}

  1. 按一下導覽畫面中的**儲存區**。
  2. 按一下移轉目標儲存區。
  3. 按一下導覽畫面中的**配置**。
  4. 向下捲動至**端點**區段，並根據您執行移轉工具的位置來選擇端點。

  5. 複製下列內容，並將其貼入 `rclone.conf`，以建立 COS 目標。

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. 使用 COS 認證及端點，填寫下列欄位：

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## 驗證已適當地配置移轉來源及目標
{: #migrate-verify}

1. 列出 Swift 容器，驗證已適當地配置 `rclone`。

    ```
    rclone lsd SWIFT:
    ```

2. 列出 COS 儲存區，驗證已適當地配置 `rclone`。

    ```
    rclone lsd COS:
    ```

## 執行 `rclone`
{: #migrate-run}

1. 測試（未複製任何資料）`rclone`，將來源 Swift 容器中的物件（例如 `swift-test`）同步至目標 COS 儲存區（例如 `cos-test`）。

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. 確認您要移轉的檔案出現在指令輸出中。如果一切看起來都不錯，請移除 `--dry-run` 旗標，並新增 `-v` 旗標來複製資料。使用選用 `--checksum` 旗標，將避免更新兩個位置中具有相同 MD5 雜湊及物件大小的所有檔案。

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   您應該嘗試將執行 rclone 的機器的 CPU、記憶體及網路最大化，以獲得最快速的傳送時間。調整 rclone 時需要考量一些其他參數：

   --checkers int  平行執行的檢查程式數目。（預設值為 8）
   這是執行中總和檢查比較執行緒數目。建議將此值增加至 64 個以上。

   --transfers int 平行執行的檔案傳送次數。（預設值為 4）
   這是要平行傳送的物件數。建議將此值增加至 64 或 128 或更多。

   --fast-list 使用遞迴清單（如果可用）。使用較多記憶體，但交易較少。使用此選項可改善效能 - 減少複製物件所需的要求數目。

使用 `rclone` 移轉資料時，會複製但不會刪除來源資料。
{:tip}


3. 針對需要移轉的任何其他容器，重複執行。
4. 複製所有資料並且驗證應用程式可以存取 COS 中的資料之後，請刪除 Swift 服務實例。
