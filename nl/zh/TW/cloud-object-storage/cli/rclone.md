---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: data migration, object storage, cli, rclone

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

# 使用 `rclone`
{: #rclone}

## 安裝 `rclone`
{: #rclone-install}

`rclone` 工具有助於保持目錄同步化，以及在儲存空間平台之間移轉資料。它是 Go 程式，並以一個二進位檔提供。

### 快速入門安裝
{: #rclone-quick}

*  [下載](https://rclone.org/downloads/)相關二進位檔。 
*  從保存檔解壓縮 `rclone` 或 `rclone.exe` 二進位檔。
*  執行 `rclone config` 來設定。

### 使用 Script 安裝
{: #rclone-script}

在 Linux/macOS/BSD 系統上安裝 `rclone`：

```
curl https://rclone.org/install.sh | sudo bash
```

也提供測試版：

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

安裝 Script 會先檢查已安裝的 `rclone` 版本，並且在現行版本已是最新版時跳過下載。
{:note}

### 從預先編譯的二進位檔進行 Linux 安裝
{: #rclone-linux-binary}

首先，提取並解壓縮二進位檔：

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

接下來，將二進位檔複製到可感應的位置：

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

安裝文件：

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

執行 `rclone config` 來設定：

```
rclone config
```

### 從預先編譯的二進位檔進行 macOS 安裝
{: #rclone-osx-binary}

首先，下載 `rclone` 套件：

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

然後，將已下載的檔案解壓縮，並 `cd` 至解壓縮的資料夾：

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

將 `rclone` 移動至您的 `$PATH`，並在出現提示時輸入密碼：

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

`mkdir` 指令執行很安全，即使目錄已存在也一樣。
{:tip}

移除殘留的檔案。

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

執行 `rclone config` 來設定：

```
rclone config
```

## 配置對 IBM COS 的存取
{: #rclone-config}

1. 執行 `rclone config`，然後選取 `n` 代表新的遠端。

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. 輸入配置的名稱：
```
	name> <YOUR NAME>
```

3. 選取 "s3" 儲存空間。

```
	Choose a number from below, or type in your own value
		1 / Alias for a existing remote
		\ "alias"
		2 / Amazon Drive
		\ "amazon cloud drive"
		3 / Amazon S3 Complaint Storage Providers (Dreamhost, Ceph, Minio, IBM COS)
		\ "s3"
		4 / Backblaze B2
		\ "b2"
	[snip]
		23 / http Connection
	  \ "http"
	Storage> 3
```

  4. 選取 IBM COS 作為 S3 儲存空間提供者。

```
Choose the S3 provider.
Enter a string value. Press Enter for the default ("")
Choose a number from below, or type in your own value
	 1 / Amazon Web Services (AWS) S3
	   \ "AWS"
	 2 / Ceph Object Storage
	 \ "Ceph"
   3 / Digital Ocean Spaces
   \ "Digital Ocean"
	 4 / Dreamhost DreamObjects
   \ "Dreamhost"
   5 / IBM COS S3
	 \ "IBMCOS"
	 [snip]
	 Provider>5
```

  1. 輸入 **False**，以輸入您的認證。

```
Get AWS credentials from the runtime (environment variables or EC2/ECS meta data if no env vars). 
Only applies if access_key_id and secret_access_key is blank.
Enter a boolean value (true or false). Please Enter for the default ("false").
Choose a number from below, or type in your own value
	 1 / Enter AWS credentials in the next step
   \ "false"
   2 / Get AWS credentials from the environment (env vars or IAM)
   \ "true"
   env_auth>false
```

  6. 輸入存取金鑰及密碼。

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. 指定 IBM COS 的端點。如需公用 IBM COS，請從提供的選項選擇。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

```
Endpoint for IBM COS S3 API.
	Choose a number from below, or type in your own value
	 1 / US Cross Region Endpoint
	   \ "s3.us.cloud-object-storage.appdomain.cloud"
	 2 / US Cross Region Dallas Endpoint
	   \ "s3-api.dal.us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 3 / US Cross Region Washington DC Endpoint
	   \ "s3-api.wdc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 4 / US Cross Region San Jose Endpoint
	   \ "s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 5 / US Cross Region Private Endpoint
	   \ "s3-api.us-geo.objectstorage.service.networklayer.com"
[snip]
	34 / Toronto Single Site Private Endpoint
	   \ "s3.tor01.objectstorage.service.networklayer.com"
	endpoint>1
```

  8. 指定 IBM COS 位置限制。位置限制必須符合端點。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

```
 1 / US Cross Region Standard
	   \ "us-standard"
	 2 / US Cross Region Vault
	   \ "us-vault"
	 3 / US Cross Region Cold
	   \ "us-cold"
	 4 / US Cross Region Flex
	   \ "us-flex"
	 5 / US East Region Standard
	   \ "us-east-standard"
[snip]
	32 / Toronto Flex
	   \ "tor01-flex"
location_constraint>1
```

  9. 指定 ACL。僅支援 `public-read` 及 `private`。 

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. 檢閱顯示的配置，並接受以儲存「遠端」，然後退出。配置檔案應該看起來類似：

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3.us.cloud-object-storage.appdomain.cloud
	location_constraint = us-standard
	acl = private
```

## 指令參考資料
{: #rclone-reference}

### 建立儲存區
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### 列出可用的儲存區
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### 列出儲存區的內容
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### 將檔案從本端複製到遠端
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### 將檔案從遠端複製到本端
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### 刪除遠端的檔案
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### 列出指令
{: #rclone-reference-listing}

有數個相關的清單指令
* `ls` 僅列出物件的大小及路徑
* `lsl` 僅列出物件的修改時間、大小及路徑
* `lsd` 僅列出目錄
* `lsf` 以可輕鬆剖析的格式列出物件和目錄
* `lsjson` 以 JSON 格式列出物件和目錄

## `rclone sync`
{: #rclone-sync}

`sync` 作業會讓來源與目的地相同，而且只會修改目的地。同步不會傳送未變更的檔案，依大小及修改時間或 MD5SUM 進行測試。目的地會更新以符合來源，包括視需要刪除檔案。

因為這可能會導致資料流失，所以請先使用 `--dry-run` 旗標進行測試，看看確切會複製及刪除哪些內容。
{:important}

請注意如果在任何時間點有任何錯誤，便不會刪除目的地中的檔案。

目錄的_內容_ 會同步，而不是目錄本身。當 `source:path` 是目錄時，會複製 `source:path` 的內容，而不是目錄名稱和內容。如需相關資訊，請參閱 `copy` 指令中的延伸說明。

如果 `dest:path` 不存在，則會建立它，並將 `source:path` 內容放至該處。

```sh
rclone sync source:path dest:path [flags]
```

### 從多個位置同時使用 `rclone`
{: #rclone-sync-multiple}

如果您為輸出選擇不同的子目錄，則可以同時從多個位置使用 `rclone`：

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

如果您 `sync` 到相同的目錄，則您應該使用 `rclone copy`，否則兩個處理程序可能會刪除彼此的其他檔案：

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

使用 `sync`、 `copy` 或 `move` 時，會將任何已改寫或刪除的檔案，在其原始階層中移至此目錄中。

如果設定 `--suffix`，則會將字尾新增至移動的檔案。如果目錄中有相同路徑的檔案（在新增字尾之後），則會改寫該檔案。

使用中的遠端必須支援伺服器端的移動或複製，而且您必須使用與同步目的地相同的遠端。備份目錄不得與目的地目錄重疊。

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

會將 `/path/to/local` `sync` 至 `remote:current`，但對於已更新或刪除的任何檔案，將會儲存在 `remote:old`。

如果從 Script 執行 `rclone`，建議您使用今天的日期作為傳遞給 `--backup-dir` 儲存舊檔案的目錄名稱，或是以今天的日期傳遞 `--suffix`。

## `rclone` 每日同步
{: #rclone-sync-daily}

排定備份對於自動進行備份而言很重要。您執行此作業的方式，取決於您的平台。Windows 可以使用「作業排程器」，而 MacOS 及 Linux 可以使用 crontab。

### 同步目錄
{: #rclone-sync-directory}

`Rclone` 會將本端目錄與遠端容器同步，並將本端目錄中的所有檔案儲存在容器中。`Rclone` 使用語法 `rclone sync source destination`，其中 `source` 是本端資料夾，`destination` 是您 IBM COS 內的容器。

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

您可能已經有建立好的目的地，但如果沒有，可以使用上述步驟建立新的儲存區。

### 排定工作
{: #rclone-sync-schedule}

排定工作之前，請確定您已進行起始上傳，且它已完成。

#### Windows
{: #rclone-sync-windows}

1. 在您的電腦上建立一個稱為 `backup.bat` 的文字檔，並貼上您在[同步目錄](#rclone-sync-directory)小節中使用的指令。請指定 rclone.exe 的完整路徑，且不要忘記儲存檔案。

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. 使用 `schtasks` 來排定工作。這個公用程式接受一些參數。
	* /RU – 要以其身分執行工作的使用者。如果您要使用的使用者已登出，則這是必要項目。
	* /RP – 使用者的密碼。
	* /SC – 設為 DAILY。
	* /TN – 工作的名稱。稱它為 backup。
	* /TR – 您剛剛建立的 backup.bat 檔路徑。
	* /ST – 啟動作業的時間。這是 24 小時制的時間格式。01:05:00 是上午 1:05。13:05:00 則為下午 1:05。

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac 及 Linux
{: #rclone-sync-nix}

1. 在您的電腦上建立一個稱為 `backup.sh` 的文字檔，並貼上您在[同步目錄](#rclone-sync-directory)小節中使用的指令。它看起來類似以下內容。 請指定 rclone 執行檔的完整路徑，且不要忘記儲存檔案。

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. 用 `chmod` 讓 Script 成為可執行。

```sh
chmod +x backup.sh
```

3. 編輯 crontab。

```sh
sudo crontab -e
```

4. 將項目新增至 crontab 檔案的底端。Crontab 很容易理解：前五個欄位依序代表分鐘、小時、日、月及平日。使用 * 表示全部。如果要讓 `backup.sh` 在每日上午 1:05 執行，請使用類似以下內容：

```sh
5 1 * * * /full/path/to/backup.sh
```

5. 儲存 crontab，您就可以開始進行了。
