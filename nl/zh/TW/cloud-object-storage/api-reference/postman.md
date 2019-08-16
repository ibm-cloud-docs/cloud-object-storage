---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# 使用 `Postman`
{: #postman}

以下是 {{site.data.keyword.cos_full}} REST API 的基本 `Postman` 設定。您可以在[儲存區](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)或[物件](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)的 API 參考資料中找到其他詳細資料。

使用 `Postman`，會假設您對 Object Storage 有特定的熟悉度，並且採用[服務認證](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)或[主控台](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)的必要資訊。如果不熟悉任何術語或變數，可以在[名詞解釋](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)中找到它們。

個人識別資訊 (PII)：建立儲存區及（或）新增物件時，請確保不要使用任何可依下列方式來識別任何使用者（自然人）的資訊：依名稱、位置或任何其他方法。
{:tip}

## REST API 用戶端概觀
{: #postman-rest}

REST（具象狀態傳輸 (REST)）是一種架構樣式，提供讓電腦系統透過 Web 彼此互動的標準，一般是使用所有主要開發語言及平台都支援的標準 HTTP URL 及動詞（GET、PUT、POST 等等）。不過，與 REST API 互動並不像使用標準網際網路瀏覽器那麼簡單。簡單瀏覽器不容許對 URL 要求的任何操作。這是 REST API 用戶端所在的位置。

REST API 用戶端提供一個簡單的 GUI 型應用程式，以與現有 REST API 程式庫互動。良好的用戶端藉由容許使用者快速組合簡單和複雜的 HTTP 要求，來輕鬆地測試、開發及記載 API。Postman 是一個提供完整 API 開發環境的出色 REST API 用戶端，其包括適用於設計和模擬、除錯、測試、記載、監視以及發佈 API 的內建工具。它也提供其它有用特性，例如讓協同作業變得簡單的「集合」及「工作區」。 

## 必要條件
{: #postman-prereqs}
* IBM Cloud 帳戶
* [已建立 Cloud Storage 資源](https://cloud.ibm.com/catalog/)（精簡/免費方案正常運作）
* [已安裝與配置 IBM Cloud CLI](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [Cloud Storage 的服務實例 ID](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [IAM (Identity and Access Management) 記號](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [COS 儲存區的端點](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### 建立儲存區
{: #postman-create-bucket}
1.	啟動 Postman。
2.	在「新建標籤」中，選取 `PUT` 下拉清單。
3.	在位址列中輸入端點，並新增新儲存區的名稱。
a.	儲存區名稱在所有儲存區中必須是唯一的，因此請選擇特定內容。
4.	在「類型」下拉清單中，選取「持有人記號」。
5.	在「記號」方框中，新增「IAM 記號」。
6.	按一下「預覽要求」。
a.	您應該會看到一則指出已新增標頭的確認訊息。
7.	按一下您應該在其中看到現有 Authorization 項目的「標頭」標籤。
8.	新增索引鍵。
a.	索引鍵：`ibm-service-instance-id`。
b.	值：Cloud Storage 服務的「資源實例 ID」。
9.	按一下「傳送」。
10.	您將收到狀態 `200 OK` 訊息。

### 建立新的文字檔
{: #postman-create-text-file}

1.	按一下「加號 (+)」圖示，來建立新的標籤。
2.	從清單中，選取 `PUT`。
3.	在位址列中，輸入具有先前區段的儲存區名稱及檔名的端點位址。
4.	在「類型」清單中，選取「持有人記號」。
5.	在「記號」方框中，新增「IAM 記號」。
6.	選取「內文」標籤。
7.	選取原始選項，並確定已選取「文字」。
8.	在提供的空間中，輸入文字。
9.	按一下「傳送」。
10.	您將收到狀態 `200 OK` 訊息。

### 列出儲存區的內容
{: #postman-list-objects}

1.	選取「加號 (+)」圖示，來建立新的標籤。
2.	驗證在清單中已選取 `GET`。
3.	在位址列中，輸入具有先前區段的儲存區名稱的端點位址。
4.	在「類型」清單中，選取「持有人記號」。
5.	在「記號」方框中，新增「IAM 記號」。
6.	按一下「傳送」。
7.	您將收到狀態 `200 OK` 訊息。
8.	在「回應內文」區段中，有一個具有儲存區中檔案清單的 XML 訊息。

## 使用範例集合
{: #postman-collection}

具有可配置 {{site.data.keyword.cos_full}} API 要求範例的「Postman 集合」可供[下載 ![外部鏈結圖示](../icons/launch-glyph.svg "外部鏈結圖示")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window}。

### 將集合匯入至 Postman
{: #postman-import-collection}

1. 在 Postman 中，按一下右上角的「匯入」按鈕
2. 使用下列一種方法來匯入「集合」檔案：
    * 從「匯入」視窗中，將「集合」檔案拖放到標籤為**在這裡放置檔案**的視窗
    * 按一下「選擇檔案」按鈕，瀏覽至資料夾，然後選取「集合」檔案
3. *IBM COS* 現在應該會出現在「集合」視窗中
4. 展開「集合」，您應該會看到 20 個範例要求
5. 「集合」包含 6 個變數，需要設定這些變數，才能順利執行 API 要求
    * 按一下集合右邊的三個點以展開功能表，然後按一下「編輯」
6. 編輯變數，以符合 Cloud Storage 環境
    * **bucket** - 輸入您想要建立之新儲存區的名稱（儲存區名稱在 Cloud Storage 中必須是唯一的）。
    * **serviceid** - 輸入 Cloud Storage 服務的 CRN。[這裡](/docs/overview?topic=overview-crn)提供取得 CRN 的指示。
    * **iamtoken** - 輸入 Cloud Storage 服務的 OAUTH 記號。[這裡](/docs/services/key-protect?topic=key-protect-retrieve-access-token)提供取得 OAUTH 記號的指示。
    * **endpoint** - 輸入 Cloud Storage 服務的地區端點。您可以從 [IBM Cloud 儀表板](https://cloud.ibm.com/resources/){:new_window}中取得可用的端點
        * *確定選取的端點符合 Key Protect 服務，以確保範例正確執行*
    * **rootkeycrn** - 在主要 Key Protect 服務中建立的「根金鑰」CRN。
        * CRN 應該類似下列內容：<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *確定選取的 Key Protect 服務符合「端點」的地區*
    * **bucketlocationvault** - 輸入*建立新儲存區（不同的儲存空間類別）* API 要求之儲存區建立的位置限制值。
        * 可接受的值包括：
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. 按一下「更新」

### 執行範例
{: #postman-samples}
API 範例要求相當直接明確且容易使用。它們的設計是要依序執行，並示範如何與 Cloud Storage 互動。它們也可以用來對 Cloud Storage 服務執行功能測試，以確保適當的作業。

<table>
    <tr>
        <th>要求</th>
        <th>預期結果</th>
        <th>測試結果</th>
    </tr>
    <tr>
        <td>擷取儲存區清單</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
                <li>
                    在「內文」中，您應該設定 Cloud Storage 中儲存區的 XML 清單。
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期內容</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>建立新的儲存區</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>建立新的文字檔</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期標頭</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>建立新的二進位檔</td>
        <td>
            <ul>
                <li>
                    按一下「內文」，然後按一下「選擇檔案」以選取要上傳的映像檔
                </li>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期標頭</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>擷取儲存區中的檔案清單</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
                <li>
                    在回應的「內文」中，您應該會看到在先前要求中建立的兩個檔案
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期標頭</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>擷取儲存區中的檔案清單（依字首過濾）</td>
        <td>
            <ul>
                <li>將 querystring 值變更為 prefix=&lt;some text&gt;</li>
                <li>狀態碼 200 OK</li>
                <li>
                    在回應的「內文」中，您應該會看到名稱開頭為所指定字首的檔案
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期標頭</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>擷取文字檔</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
                <li>
                    在回應的「內文」中，您應該會看到在前一個要求中輸入的文字
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期內文內容</li>
                <li>回應包含預期標頭</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>擷取二進位檔</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
                <li>
                    在回應的「內文」中，您應該會看到在前一個要求中選擇的映像檔
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期標頭</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>擷取失敗的多部分上傳清單</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
                <li>
                    在回應的「內文」中，您應該會看到儲存區的任何失敗多部分上傳
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期內容</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>擷取失敗的多部分上傳清單（依名稱過濾）</td>
        <td>
            <ul>
                <li>將 querystring 值變更為 prefix=&lt;some text&gt;</li>
                <li>狀態碼 200 OK</li>
                <li>
                    在回應的「內文」中，您應該會看到名稱開頭為指定字首之儲存區的所有失敗多部分上傳
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期內容</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>設定已啟用 CORS 的儲存區</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>擷取儲存區 CORS 配置</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
                <li>
                    在回應的「內文」中，您應該會看到儲存區的 CORS 配置集
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
                <li>回應包含預期內容</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>刪除儲存區 CORS 配置</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>刪除文字檔</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>刪除二進位檔</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>刪除儲存區</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>建立新的儲存區（不同的儲存空間類別）</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>刪除儲存區（不同的儲存空間類別）</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>建立新的儲存區 (Key Protect)</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>刪除儲存區 (Key Protect)</td>
        <td>
            <ul>
                <li>狀態碼 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>要求成功</li>
            </ul>
        </td>                
    </tr>
</table>

## 使用 Postman Collection Runner
{: #postman-runner}

Postman Collection Runner 提供一個使用者介面用來測試集合，並容許您一次執行「集合」中的所有要求。 

1. 按一下主要 Postman 視窗右上角的 Runner 按鈕。
2. 在 Runner 視窗中，選取 IBM COS 集合，然後按一下畫面底端的大型藍色**執行 IBM COS** 按鈕。
3. Collection Runner 視窗在執行要求時將顯示反覆運算。您將會看到測試結果出現在每個要求下方。
    * **執行摘要**會顯示要求的網格視圖，並容許過濾結果。
    * 您也可以按一下 **匯出結果**，以將結果儲存至 JSON 檔案。
