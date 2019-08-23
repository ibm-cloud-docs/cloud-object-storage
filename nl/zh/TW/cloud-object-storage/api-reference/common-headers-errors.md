---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# 一般標頭及錯誤碼
{: #compatibility-common}

## 一般要求標頭
{: #compatibility-request-headers}

下表說明支援的一般要求標頭。如果在要求中傳送，則 {{site.data.keyword.cos_full}} 會忽略下面未列出的所有一般標頭，但部分要求可能支援此文件中所定義的其他標頭。

| 標頭                  | 附註                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Authorization           | 對於所有要求而言，是**必要項目**（OAuth2 `bearer` 記號）。                                                                            |
| ibm-service-instance-id | 對於建立或列出儲存區的要求，是**必要項目**。                                                                              |
| Content-MD5             | 有效負載的 base64 編碼 128 位元 MD5 雜湊，用來作為完整性檢查，以確保未在傳輸中變更有效負載。|
| Expect                  | 值 `100-continue` 會等待在傳送有效負載之前，來自標頭適合的系統的確認通知。|
| host                    | `{bucket-name}.{endpoint}` 的端點或「虛擬主機」語法。一般而言，會自動新增此標頭。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。| 
| Cache-Control | 可用來指定要求/回覆鏈的快取行為。如需相關資訊，請移至 http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 |

### 自訂 meta 資料
{: #compatibility-headers-metadata}

使用 Object Storage 的優點是可以將鍵值組作為標頭傳送，以新增自訂 meta 資料。這些標頭的格式為 `x-amz-meta-{KEY}`。請注意，與 AWS S3 不同，IBM COS 會將具有相同 meta 資料索引鍵的多個標頭與結合至逗點區隔的值清單。

## 一般回應標頭
{: #compatibility-response-headers}

下表說明一般回應標頭。

| 標頭             | 附註                                               |
|------------------|-----------------------------------------------------|
| Content-Length   | 要求內文的長度（以位元組為單位）。                 |
| Connection       | 指出是開啟還是關閉連線。                             |
| Date             | 要求的時間戳記。                                   |
| ETag             | 要求的 MD5 雜湊值。                                |
| Server           | 回應伺服器的名稱。                                 |
| X-Clv-Request-Id | 每個要求產生的唯一 ID。                            |

### 生命週期回應標頭
{: #compatibility-lifecycle-headers}

下表說明保存物件的回應標頭

| 標頭           | 附註                                                |
|------------------|-----------------------------------------------------|
|x-amz-restore| 如果已還原物件，或正在進行還原，則會包含此項目。|
|x-amz-storage-class| 如果已保存或暫時還原，則傳回 `GLACIER`。|
|x-ibm-archive-transition-time| 傳回物件排定要轉移至保存層級的日期和時間。|
|x-ibm-transition| 如果物件具有轉移 meta 資料，並傳回轉移的層級及原始時間，則會包含此項目。|
|x-ibm-restored-copy-storage-class| 如果物件處於 `RestoreInProgress` 或 `Restored` 狀態，並傳回儲存區的儲存空間類別，則會包含此項目。|

## 錯誤碼
{: #compatibility-errors}

| 錯誤碼                              | 說明                                                                                                                                                                   | HTTP 狀態碼                         |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | 拒絕存取                                                                                                                                                               | 403 禁止                            |
| BadDigest                           | 您指定的 Content-MD5 不符合我們收到的 Content-MD5。                                                                                                                    | 400 不當的要求                      |
| BucketAlreadyExists                 | 所要求的儲存區名稱無法使用。儲存區名稱空間由系統的所有使用者共用。請選取不同的名稱，然後再試一次。                                                                     | 409 衝突                            |
| BucketAlreadyOwnedByYou             | 您先前用來建立具名儲存區的要求成功，而且您已擁有它。                                                                                                                     | 409 衝突                            |
| BucketNotEmpty                      | 您嘗試刪除的儲存區不是空的。                                                                                                                                           | 409 衝突                            |
| CredentialsNotSupported             | 此要求不支援認證。                                                                                                                                                     | 400 不當的要求                      |
| EntityTooSmall                      | 您提出的上傳小於容許的物件大小下限。                                                                                                                                   | 400 不當的要求                      |
| EntityTooLarge                      | 您提出的上傳超出容許的物件大小上限。                                                                                                                                   | 400 不當的要求                      |
| IncompleteBody                      | 您未提供 Content-Length HTTP 標頭所指定的位元組數。                                                                                                                    | 400 不當的要求                      |
| IncorrectNumberOfFilesInPostRequest | POST 需要每個要求都只上傳一個檔案。                                                                                                                                    | 400 不當的要求                      |
| InlineDataTooLarge                  | 行內資料超出容許的大小上限。                                                                                                                                           | 400 不當的要求                      |
| InternalError                       | 發生內部錯誤。請重試。                                                                                                                                                 | 500 內部伺服器錯誤                  |
| InvalidAccessKeyId                  | 您提供的 AWS 存取金鑰 ID 不存在於記錄中。                                                                                                                              | 403 禁止                            |
| InvalidArgument                     | 引數無效                                                                                                                                                               | 400 不當的要求                      |
| InvalidBucketName                   | 指定的儲存區無效。                                                                                                                                                     | 400 不當的要求                      |
| InvalidBucketState                  | 要求對儲存區的現行狀態無效。                                                                                                                                           | 409 衝突                            |
| InvalidDigest                       | 指定的 Content-MD5 無效。                                                                                                                                              | 400 不當的要求                      |
| InvalidLocationConstraint           | 指定的位置限制無效。如需地區的相關資訊，請參閱「如何選取儲存區的地區」。                                                                                               | 400 不當的要求                      |
| InvalidObjectState                  | 作業對物件的現行狀態無效。                                                                                                                                             | 403 禁止                            |
| InvalidPart                         | 找不到一個以上的指定組件。可能尚未上傳組件，或指定的實體標籤可能不符合組件的實體標籤。                                                                                 | 400 不當的要求                      |
| InvalidPartOrder                    | 組件清單未依遞增順序。必須依組件號碼順序指定組件清單。                                                                                                                 | 400 不當的要求                      |
| InvalidRange                        | 無法滿足所要求的範圍。                                                                                                                                                 | 416 無法滿足要求的範圍              |
| InvalidRequest                      | 請使用 AWS4-HMAC-SHA256。                                                                                                                                              | 400 不當的要求                      |
| InvalidSecurity                     | 提供的安全認證無效。                                                                                                                                                   | 403 禁止                            |
| InvalidURI                          | 無法剖析指定的 URI。                                                                                                                                                   | 400 不當的要求                      |
| KeyTooLong                          | 金鑰太長。                                                                                                                                                             | 400 不當的要求                      |
| MalformedPOSTRequest                | POST 要求的內文不是形式完整的 multipart/form-data。                                                                                                                    | 400 不當的要求                      |
| MalformedXML                        | 您提供的 XML 形式不完整，或未針對已發佈的綱目進行驗證。                                                                                                                  | 400 不當的要求                      |
| MaxMessageLengthExceeded            | 要求太大。                                                                                                                                                             | 400 不當的要求                      |
| MaxPostPreDataLengthExceededError   | 上傳檔案之前的 POST 要求欄位太大。                                                                                                                                     | 400 不當的要求                      |
| MetadataTooLarge                    | meta 資料標頭超出容許的 meta 資料大小上限。                                                                                                                            | 400 不當的要求                      |
| MethodNotAllowed                    | 不容許針對此資源進行指定的方法。                                                                                                                                       | 405 不容許的方法                      |
| MissingContentLength                | 您必須提供 Content-Length HTTP 標頭。                                                                                                                                  | 411 必要的長度                        |
| MissingRequestBodyError             | 這發生在使用者將空的 xml 文件作為要求傳送時。錯誤訊息為「要求內文是空的」。                                                                                              | 400 不當的要求                      |
| NoSuchBucket                        | 指定的儲存區不存在。                                                                                                                                                   | 404 找不到                          |
| NoSuchKey                           | 指定的金鑰不存在。                                                                                                                                                     | 404 找不到                          |
| NoSuchUpload                        | 指定的多部分上傳不存在。上傳 ID 可能無效，或者可能已中斷或完成多部分上傳。                                                                                             | 404 找不到                          |
| NotImplemented                      | 您提供的標頭暗示未實作功能。                                                                                                                                           | 501 未實作                          |
| OperationAborted                    | 目前針對此資源正在進行的衝突條件式作業。請重試。                                                                                                                       | 409 衝突                            |
| PreconditionFailed                  | 您指定的前置條件至少有一個未保留。                                                                                                                                     | 412 前置條件失敗                    |
| Redirect                            | 暫時重新導向。                                                                                                                                                         | 307 暫時移動                      |
| RequestIsNotMultiPartContent        | 儲存區 POST 必須是 enclosure-type multipart/form-data。                                                                                                                | 400 不當的要求                      |
| RequestTimeout                      | 在逾時期間內，未讀取或寫入與伺服器的 Socket 連線。                                                                                                                     | 400 不當的要求                      |
| RequestTimeTooSkewed                | 要求時間與伺服器時間之間的差異太大。                                                                                                                                   | 403 禁止                            |
| ServiceUnavailable                  | 減少要求比率。                                                                                                                                                         | 503 服務無法使用                    |
| SlowDown                            | 減少要求比率。                                                                                                                                                         | 503 變慢                            |
| TemporaryRedirect                   | DNS 更新時，會將您重新導向至儲存區。                                                                                                                                   | 307 暫時移動                      |
| TooManyBuckets                      | 您嘗試建立的儲存區超過容許的數目。                                                                                                                                     | 400 不當的要求                      |
| UnexpectedContent                   | 此要求不支援內容。                                                                                                                                                     | 400 不當的要求                      |
| UserKeyMustBeSpecified              | 儲存區 POST 必須包含指定的欄位名稱。如果已指定，請檢查欄位的順序。                                                                                                     | 400 不當的要求                      |
