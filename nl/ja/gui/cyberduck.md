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

# Cyberduck を使用したファイルの転送
{: #cyberduck}

Cyberduck は、Mac および Windows 用の、普及している使いやすいオープン・ソースのクラウド・オブジェクト・ストレージ・ブラウザーです。Cyberduck は、IBM COS への接続に必要な正しい許可シグニチャーを計算できます。Cyberduck は、[cyberduck.io/](https://cyberduck.io/){: new_window} からダウンロードできます。

Cyberduck を使用して IBM COS への接続を作成し、ローカル・ファイルのフォルダーをバケットに同期するには、以下のステップを実行します。

 1. Cyberduck をダウンロードしてインストールし、開始します。
 2. アプリケーションのメイン・ウィンドウが開き、そこで IBM COS への接続を作成できます。**「Open Connection」**をクリックして、IBM COS への接続を構成します。
 3. ポップアップ・ウィンドウが開きます。上部のドロップダウン・メニューから、`「S3 (HTTPS)」`を選択します。以下のフィールドに情報を入力し、「Connect」をクリックします。

    * `Server`: IBM COS のエンドポイントを入力します
        * *エンドポイントの地域が意図したバケットと一致していることを確認します。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。*
    * `Access Key ID`
    * `Secret Access Key`
    * `Add to Keychain`: 他のアプリケーションで使用できるように、キーチェーンへの接続を保存します *(オプション)*

 4. Cyberduck で、バケットを作成できるアカウントのルートが表示されます。
    * メイン・ペイン内を右クリックし、**「New Folder」**を選択します (*アプリケーションは、フォルダーがより一般的なコンテナー構成である多数の転送プロトコルを処理します*)。
    * バケット名を入力してから、「Create」をクリックします。
 5. バケットが作成されたら、バケットをダブルクリックして表示します。バケット内で、以下のようなさまざまな機能を実行できます。
    * ファイルをバケットにアップロードします。
    * バケット・コンテンツをリストします。
    * バケットからオブジェクトをダウンロードします。
    * ローカル・ファイルをバケットに同期します。
    * オブジェクトを別のバケットに同期します。
    * バケットのアーカイブを作成します。
 6. バケット内を右クリックし、**「Synchronize」**を選択します。ポップアップ・ウィンドウが開き、そこで、バケットに同期するフォルダーを参照できます。フォルダーを選択し、「Choose」をクリックします。
 7. フォルダーを選択すると、新しいポップアップ・ウィンドウが開きます。ここでは、バケットとの同期操作を選択するドロップダウン・メニューを使用できます。メニューから、以下の 3 つの同期オプションを使用できます。

    * `Download`: これにより、変更されたオブジェクトと欠落しているオブジェクトがバケットからダウンロードされます。
    * `Upload`: これにより、変更されたファイルと欠落しているファイルがバケットにアップロードされます。
    * `Mirror`: これにより、ダウンロード操作とアップロード操作の両方が実行され、新規および更新されたすべてのファイルとオブジェクトがローカル・フォルダーとバケットの間で確実に同期されます。

 8. 別のウィンドウが開き、アクティブな転送要求と履歴転送要求が表示されます。同期要求が完了すると、メイン・ウィンドウでバケットに対するリスト操作が実行され、バケット内の更新されたコンテンツが反映されます。

## Mountain Duck
{: #mountain-duck}

Mountain Duck は Cyberduck に基づいて作成されており、Mac 上の Finder または Windows 上の Explorer でクラウド・オブジェクト・ストレージをディスクとしてマウントできます。試用版が使用可能ですが、継続的に使用するには登録キーが必要です。

Mountain Duck でのブックマークの作成は、Cyberduck での接続の作成と非常によく似ており、以下のようにします。

1. Mountain Duck をダウンロードしてインストールし、開始します。
2. 新規ブックマークを作成します。
3. ドロップダウン・メニューから`「S3 (HTTPS)」`を選択し、以下の情報を入力します。
    * `Server`: IBM COS のエンドポイントを入力します 
        * *エンドポイント地域が意図したバケットと一致していることを確認します。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。*
    * `Username`: アクセス・キーを入力します
    * **「Connect」**をクリックします
    * 秘密鍵の入力を求めるプロンプトが出されます。この秘密鍵は、キーチェーンに保存されます。

これで、バケットが Finder または Explorer で使用可能になります。他のマウントされたファイル・システムと同様に、{{site.data.keyword.cos_short}} と対話できます。

## CLI
{: #cyberduck-cli}

Cyberduck は、Linux、Mac OS X、および Windows 上のシェルで実行されるコマンド・ライン・インターフェース (CLI) である `duck` も提供しています。インストール手順については、`duck` [Wiki ページ](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window}を参照してください。

`duck` を {{site.data.keyword.cos_full}} で使用するには、カスタム・プロファイルを[アプリケーション・サポート・ディレクトリー](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}に追加する必要があります。サンプルや事前構成プロファイルなど、`duck` 接続プロファイルに関する詳細情報は、[CLI Help / Howto](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window} にあります。

以下は、地域 COS エンドポイントのプロファイルの例です。

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

エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

このプロファイルを `duck` に追加すると、以下のようなコマンドを使用して {{site.data.keyword.cos_short}} にアクセスできます。

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*キー値*
* `<bucket-name>` - COS バケットの名前 (*バケットとエンドポイントの地域が整合していることを確認してください*)
* `<access-key>` - HMAC アクセス・キー
* `<secret-access-key>` - HMAC 秘密鍵

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

コマンド・ライン・オプションの完全なリストを確認するには、シェルで `duck --help` と入力するか、[Wiki サイト](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}にアクセスしてください。
