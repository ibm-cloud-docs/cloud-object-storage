---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# CrossFTP を使用したファイルの転送
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} は、{{site.data.keyword.cos_full}} を含む S3 互換のクラウド・ストレージ・ソリューションをサポートする、フル機能の FTP クライアントです。CrossFTP は、Mac OS X、Microsoft Windows、Linux をサポートし、以下のような機能を備えた Free バージョン、Pro バージョン、および Enterprise バージョンを提供します。

* タブ付きインターフェース
* パスワード暗号化
* 検索
* バッチ転送
* 暗号化 (*Pro/Enterprise バージョン*)
* 同期 (*Pro/Enterprise バージョン*)
* スケジューラー (*Pro/Enterprise バージョン*)
* コマンド・ライン・インターフェース (*Pro/Enterprise バージョン*)

## IBM Cloud Object Storage への接続
{: #crossftp-connect}

1. CrossFTP をダウンロードしてインストールし、開始します。
2. 右側のペインで、プラス (+) アイコンをクリックしてサイト・マネージャーを開くことにより、新規サイトを作成します。
3. 「*General*」タブで以下を入力します。
    * **「Protocol」**を`「S3/HTTPS」`に設定します。
    * **「Label」**を任意の記述名に設定します。
    * **「Host」**を {{site.data.keyword.cos_short}} エンドポイント (つまり、`s3.us.cloud-object-storage.appdomain.cloud`) に設定します。
        * *エンドポイント地域が意図したターゲット・バケットと一致していることを確認します。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。*
    * **「Port」**を`「443」`のままにします。
    * **「Access Key」**および**「Secret」**を、ターゲット・バケットへの適切なアクセス権限を備えている HMAC 資格情報に設定します。
4. 「*S3*」タブで以下のようにします。
    * `「Use DevPay」`のチェック・マークが外されていることを確認します。
    * **「API Set ...」**をクリックし、`「Dev Pay」`および`「CloudFront Distribution」`のチェック・マークが外されていることを確認します。
5. ***Mac OS X の場合のみ***
    * メニュー・バーで*「Security」>「TLS/SSL Protocols...」*をクリックします。
    * オプション`「Customize the enabled protocols」`を選択します。
    * `「TLSv1.2」`を**「Enabled」**ボックスに追加します。
    * **「OK」**をクリックします。
6. ***Linux の場合のみ***
    * メニュー・バーで*「Security」>「Cipher Settings...」*をクリックします。
    * オプション`「Customize the enabled cipher suites」`を選択します。
    * `「TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA」`を**「Enabled」**ボックスに追加します。
    * **「OK」**をクリックします。
7. **「Apply」**および**「Close」**を順にクリックします。
8. *「Sites」*の下の新規項目が、ステップ 3 で指定した*「Label」*で使用可能になるはずです。
9. 新規項目をダブルクリックしてエンドポイントに接続します。

ここから、使用可能なバケットのリストがウィンドウで表示され、使用可能なファイルを参照してローカル・ディスクとの間で転送できます。
