---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: s3fs, open source, file system, gateway

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

# 使用 `s3fs` 裝載儲存區
{: #s3fs}

預期在 NFS 樣式檔案系統中進行讀取及寫入的應用程式，可以使用 `s3fs`，這樣可以將儲存區裝載為目錄，同時保留檔案的原生物件格式。這可讓您使用熟悉的 Shell 指令（例如 `ls` 列出檔案，或 `cp` 複製檔案）與您的雲端儲存空間互動，同時也能存取依賴從本端檔案進行讀取及寫入的舊式應用程式。如需更詳細的概觀，[請參閱專案的正式 README](https://github.com/s3fs-fuse/s3fs-fuse)。

## 必要條件
{: #s3fs-prereqs}

* IBM Cloud 帳戶及 {{site.data.keyword.cos_full}} 的實例
* Linux 或 OSX 環境
* 認證（[IAM API 金鑰](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)或 [HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)）

## 安裝
{: #s3fs-install}

在 OSX 上，使用 [Homebrew](https://brew.sh/)：

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

在 Debian 或 Ubuntu 上： 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

官方的 `s3fs` 文件建議使用 `libcurl4-gnutls-dev`，而非 `libcurl4-openssl-dev`。兩者都可行，但 OpenSSL 版本的效能可能更好。
{:tip}

您也可以從來源建置 `s3fs`。首先，請複製 Github 儲存庫：

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

然後建置 `s3fs`：

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

安裝二進位檔：

```sh
sudo make install
```
{:codeblock}

## 配置
{: #s3fs-config}

將認證儲存在包含 `<access_key>:<secret_key>` 或 `:<api_key>` 的檔案中。此檔案需要受限制的存取權，因此請執行：

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

現在，您可以使用下列指令裝載儲存區：

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

如果 credentials 檔案只有 API 金鑰（沒有 HMAC 認證），則您也需要新增 `ibm_iam_auth` 旗標：

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

`<bucket>` 是現有的儲存區，而 `<mountpoint>` 是您要裝載儲存區的本端目錄。`<endpoint>` 必須對應至[儲存區的位置](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)。`credentials_file` 是使用 API 金鑰或 HMAC 認證所建立的檔案。

現在，`ls <mountpoint>` 會列出該儲存區中的物件，就如同它們是本端檔案一樣（或在有物件字首的情況下，如同它們是巢狀目錄一樣）。

## 效能最佳化
{: #s3fs-performance}

雖然效能永遠不會等於真正的本端檔案系統，仍可能使用某些進階選項來提高傳輸量。 

```sh
s3fs <bucket_name> <mountpoint> -o url=http{s}://<COS_endpoint> –o passwd_file=<credentials_file> \
-o cipher_suites=AESGCM \
-o kernel_cache \
-o max_background=1000 \
-o max_stat_cache_size=100000 \
-o multipart_size=52 \
-o parallel_count=30 \
-o multireq_max=30 \
-o dbglevel=warn
```
{:codeblock}

1. `cipher_suites=AESGCM` 僅在使用 HTTPS 端點時相關。依預設，對 IBM COS 的安全連線會使用 `AES256-SHA` 密碼組合。改為使用 `AESGCM` 套組，能大幅減少用戶端機器上的 CPU 額外負擔（由 TLS 加密功能所造成），同時提供相同層次的加密安全。
2. `kernel_cache` 會啟用 `s3fs` 裝載點上的核心緩衝區快取。這表示物件只會由 `s3fs` 讀取一次，因為可以從核心的緩衝區快取處理重複讀取相同檔案的作業。核心緩衝區快取只會使用未被其他處理程序使用的可用記憶體。如果您預期在裝載儲存區時，會從另一個處理程序/機器改寫儲存區物件，且您的使用案例需要即時存取最新的內容，則不建議使用這個選項。 
3. `max_background=1000` 能改善 `s3fs` 並行檔案讀取效能。依預設，FUSE 支援最多 128 KB 的檔案讀取要求。當要求讀取更多內容時，核心會將大型要求分割成較小的子要求，並讓 s3fs 以非同步方式處理它們。`max_background` 選項會設定這類並行非同步要求的廣域數目上限。依預設，它設為 12，但將它設定為任意高值 (1000)，可防止讀取要求遭到封鎖，即使同時讀取大量檔案也一樣。
4. `max_stat_cache_size=100000` 會減少 `s3fs` 所傳送的冗餘 HTTP `HEAD` 要求數，並縮短列出目錄或擷取檔案屬性所花費的時間。一般檔案系統使用會透過 `stat()` 呼叫（這對應到物件儲存空間系統的 `HEAD` 要求），來頻繁存取檔案的 meta 資料。依預設，`s3fs` 會快取最多 1000 個物件的屬性（meta 資料）。每個快取的項目最多需要 0.5 KB 的記憶體。理想情況下，您會希望快取能夠保留儲存區中所有物件的 meta 資料。不過，建議您考量此快取的記憶體用量可能影響。將它設為 `100000` 將需要不超過 0.5 KB * 100000 = 50 MB。
5. `multipart_size=52` 會設定從 COS 伺服器傳送及接收的要求與回應大小上限（以 MB 單位）。`s3fs` 依預設會將這設為 10 MB。增加此值也會增加每個 HTTP 連線的傳輸量 (MB/s)。另一方面，從檔案提供的第一個位元組的延遲將分別增加。因此，如果您的使用案例只會讀取每一個檔案中的少量資料，您可能不想要增加此值。此外，如果此值足夠小，容許同時使用多個要求來提取檔案，則對於大型物件（例如超過 50 MB）傳輸量會增加。這個選項的最佳值大約為 50 MB。COS 最佳作法建議使用的要求數為 4 MB 的倍數，因此建議將這個選項設為 52 (MB)。
6. `parallel_count=30` 會設定每一次檔案讀取/寫入作業時，同時傳送給 COS 的要求數目上限。依預設，這會設為 5。對於非常大的物件，您可以增加此值來得到更多的傳輸量。與前一個選項一樣，如果您只讀取每個檔案的少量資料，請將此值保持為低值。
7. `multireq_max=30` 在列出目錄時，會針對清單中的每個物件傳送物件 meta 資料要求 (`HEAD`)（除非在快取中找到 meta 資料）。這個選項會限制單一目錄清單作業中，傳送至 COS 的此類同時要求數。依預設，它設為 20。請注意，此值必須大於或等於上面的 `parallel_count` 選項。
8. `dbglevel=warn` 會將除錯層次設為 `warn`，而不是預設值 `crit`，以便記載訊息至 /var/log/syslog。

## 限制
{: #s3fs-limitations}

請務必記住，s3fs 可能不適合所有應用程式，因為物件儲存空間服務對於第一個位元組具有高延遲時間，而且缺少隨機寫入權。只有讀取大型檔案的工作負載（例如深度學習工作負載），可以使用 `s3fs` 達到良好的傳輸量。 
