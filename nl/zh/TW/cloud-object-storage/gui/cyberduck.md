---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# 使用 Cyberduck 傳送檔案
{: #cyberduck}

Cyberduck 是一個熱門的開放程式碼，且易於使用 Mac 及 Windows 的 Cloud Object Storage 瀏覽器。Cyberduck 能夠計算連接至 IBM COS 所需的正確授權簽章。您可以從 [cyberduck.io/](https://cyberduck.io/){: new_window} 下載 Cyberduck。

若要使用 Cyberduck 來建立與 IBM COS 的連線，並將本端檔案的資料夾同步化至儲存區，請遵循下列步驟：

 1. 下載、安裝及啟動 Cyberduck。
 2. 即會開啟應用程式的主視窗，您可以在其中建立與 IBM COS 的連線。按一下**開啟連線**，以配置與 IBM COS 的連線。
 3. 即會開啟蹦現視窗。從頂端的下拉功能表中，選取 `S3 (HTTPS)`。在下列欄位中輸入資訊，然後按一下「連接」：

    * `伺服器`：輸入 IBM COS 的端點
        * *確定端點的地區符合想要的儲存區。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。*
    * `存取金鑰 ID`
    * `密碼存取金鑰`
    * `新增至金鑰鏈`：儲存與金鑰鏈的連線，以容許跨其他應用程式使用*（選用）*

 4. Cyberduck 會將您帶往可建立儲存區的帳戶的根目錄。
    * 在主窗格內按一下滑鼠右鍵，然後選取**新資料夾**（*應用程式會處理「資料夾」是較常見容器建構的許多傳送通訊協定*）。
    * 輸入儲存區名稱，然後按一下「建立」。
 5. 建立儲存區之後，請按兩下儲存區以進行檢視。在儲存區內，您可以執行各種功能，例如：
    * 將檔案上傳至儲存區
    * 列出儲存區內容
    * 下載儲存區中的物件
    * 將本端檔案同步化至儲存區
    * 將物件同步化至另一個儲存區
    * 建立儲存區的保存檔
 6. 在儲存區內按一下滑鼠右鍵，然後選取**同步化**。即會開啟蹦現視窗，您可以在其中瀏覽至要同步化至儲存區的資料夾。選取資料夾，然後按一下「選擇」。
 7. 在選取資料夾之後，即會開啟新的蹦現視窗。在這裡，您可以使用下拉功能表，以選取與儲存區的同步化作業。功能表中有三個可能的同步化選項可供使用：

    * `下載`：這會下載儲存區中的已變更及遺漏物件。
    * `上傳`：這會將已變更及遺漏檔案上傳至儲存區。
    * `鏡映`：這會執行下載及上傳作業，以確保本端資料夾與儲存區之間已同步所有新的及已更新的檔案和物件。

 8. 即會開啟另一個視窗，以顯示作用中及歷程傳送要求。同步化要求完成之後，主視窗會對儲存區執行列出作業，以反映儲存區中的已更新內容。

## Mountain Duck
{: #mountain-duck}

Mountain Duck 以 Cyberduck 為建置基礎，容許您將 Cloud Object Storage 裝載為「搜尋器」（在 Mac 上）或「檔案總管」（在 Windows 上）中的磁碟。試用版可供使用，但需要有登錄金鑰才能繼續使用。

在 Mountain Duck 中建立書籤與在 Cyberduck 中建立連線十分類似：

1. 下載、安裝及啟動 Mountain Duck。
2. 建立新的書籤
3. 從下拉功能表中，選取 `S3 (HTTPS)`，然後輸入下列資訊：
    * `伺服器`：輸入 IBM COS 的端點 
        * *確定端點地區符合想要的儲存區。如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。*
    * `使用者名稱`：輸入「存取金鑰」
    * 按一下**連接**
    * 系統會提示您輸入「密碼金鑰」，然後會將它儲存在金鑰鏈中。

現在，可以在「搜尋器」或「檔案總管」中使用儲存區。您可以像任何其他裝載的檔案系統一樣地與 {{site.data.keyword.cos_short}} 互動。

## CLI
{: #cyberduck-cli}

Cyberduck 也提供 `duck`，這是在 Linux、Mac OS X 及 Windows 上以 Shell 執行的指令行介面 (CLI)。`duck` [Wiki 頁面](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window}中會提供安裝指示。

為了能夠搭配使用 `duck` 與 {{site.data.keyword.cos_full}}，需要將自訂設定檔新增至[應用程式支援目錄](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}。[CLI help/how-to](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window} 提供有關 `duck` 連線設定檔的詳細資訊（包括範例及預先配置的設定檔）。

以下是地區 COS 端點的範例設定檔：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

將此設定檔新增至 `duck`，容許您使用與下面類似的指令來存取 {{site.data.keyword.cos_short}}：

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*鍵值*
* `<bucket-name>` - COS 儲存區的名稱（*確保儲存區與端點地區一致*）
* `<access-key>` - HMAC 存取金鑰
* `<secret-access-key>` - HMAC 密碼金鑰

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

在 Shell 中輸入 `duck --help` 或造訪 [Wiki 網站](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}，以取得完整的指令行選項清單
