---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Aspera 高速転送の使用
{: #aspera}

Aspera 高速転送は、従来の FTP 転送および HTTP 転送の制約を解決し、ほとんどの条件下で、特に、待ち時間とパケット・ロスが大きいネットワークにおいて、データ転送のパフォーマンスを改善します。標準の HTTP `PUT` の代わりに、Aspera 高速転送は [FASP プロトコル](https://asperasoft.com/technology/transport/fasp/)を使用してオブジェクトをアップロードします。アップロードおよびダウンロードに Aspera 高速転送を使用すると、次のような利点があります。

- 転送の高速化
- コンソールを使用する場合は 200 MB を、SDK またはライブラリーを使用する場合は 1 GB を超える、大容量オブジェクト・アップロードの転送
- マルチメディア・ファイル、ディスク・イメージ、その他の構造化データや非構造化データなど、あらゆるタイプのデータのフォルダー全体のアップロード
- 転送速度およびデフォルト設定のカスタマイズ
- 転送を個別に表示、一時停止/再開、またはキャンセルすることが可能

Aspera 高速転送は、{{site.data.keyword.cloud_notm}} [コンソール](#aspera-console)で使用可能であり、[SDK](#aspera-sdk) を使用してプログラマチックに使用することもできます。 

Aspera 高速転送は、特定の地域でのみ使用可能です。詳しくは、[統合サービス](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)を参照してください。
{:tip}

## コンソールの使用
{: #aspera-console}

[サポートされる地域](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)でバケットを作成するときに、ファイルまたはフォルダーのアップロードのために Aspera 高速転送を選択するというオプションがあります。オブジェクトをアップロードしようとすると、Aspera Connect クライアントをインストールするように促すプロンプトが出されます。

### Aspera Connect のインストール
{: #aspera-install}

1. **「Aspera Connect クライアントのインストール」**を選択します。
2. ご使用のオペレーティング・システムおよびブラウザーに合ったインストール手順に従います。
3. ファイルまたはフォルダーのアップロードを再開します。

[Aspera Web サイト](https://downloads.asperasoft.com/connect2/)から直接 Aspera Connect プラグインをインストールすることもできます。Aspera Connect プラグインに関する問題のトラブルシューティングで支援が必要な場合は、[資料を参照してください](https://downloads.asperasoft.com/en/documentation/8)。

プラグインがインストール済みの場合、同じブラウザーを使用するターゲット・バケットへのすべてのアップロードのデフォルトとして Aspera 高速転送を設定できるオプションがあります。**「ブラウザー設定を記憶する」**を選択します。バケット構成ページの**「転送オプション」**の下にもオプションがあります。これらのオプションを使用して、アップロードおよびダウンロードのデフォルト転送として標準と高速のいずれかを選択できます。

通常、IBM Cloud Object Storage の Web ベースのコンソールを使用することは、最も一般的な {{site.data.keyword.cos_short}} の使用法ではありません。標準転送オプションでは、オブジェクトのサイズは 200 MB に制限され、ファイル名とキーは同じになります。もっと大きいオブジェクト・サイズおよびもっと高いパフォーマンス (ネットワーク要因による) が Aspera 高速転送によってサポートされます。

### 転送状況
{: #aspera-console-transfer-status}

**アクティブ:** 転送を開始すると、転送状況は「アクティブ」と表示されます。転送がアクティブである間、アクティブな転送を一時停止、再開、またはキャンセルすることができます。 

**完了:** 転送が完了すると、それについての情報と、このセッションでのすべての転送が、「完了」タブに表示されます。この情報は消去することができます。現行セッションで完了した転送に関する情報のみが表示されます。

**設定:** アップロードまたはダウンロード (あるいは両方) のデフォルトを「高速」に設定できます。

Aspera 高速転送を使用したダウンロードでは、出口料金が発生します。詳しくは、[料金設定ページ](https://www.ibm.com/cloud/object-storage)を参照してください。
{:tip}

**拡張設定 (Advanced Preferences):** アップロードおよびダウンロードの帯域幅を設定できます。

----

## ライブラリーおよび SDK の使用
{: #aspera-sdk}

Aspera 高速転送 SDK は、Java または Python を使用している場合、カスタム・アプリケーション内で高速転送を開始する機能を提供します。

### Aspera 高速転送を使用する状況
{: #aspera-guidance}

Aspera 高速転送が使用する FASP プロトコルは、COS との間のデータ転送のすべてに適しているわけではありません。具体的には、Aspera 高速転送を利用する転送は以下を行う必要があります。

1. 常に複数のセッションを使用する - Aspera 高速転送の機能を最も有効に利用するには、少なくとも 2 つの並列セッションが必要です。[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) および [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera) の固有のガイドを参照してください。
2. Aspera 高速転送は大きなファイルに最適です。含まれているデータの総量が 1 GB 未満のファイルまたはディレクトリーの場合は、代わりに標準 Transfer Manager クラスを使用して、オブジェクトを複数のパートで転送するべきです。Aspera 高速転送では、time-to-first-byte (最初の 1 バイトが到着するまでの時間) は通常の HTTP 転送よりも長くかかります。個々の小さいファイルの転送を管理するために多くの Aspera Transfer Manager オブジェクトをインスタンス化すると、基本的な HTTP 要求に比べて標準以下のパフォーマンスになる可能性があるため、代わりに、単一のクライアントをインスタンス化して、小さいファイルを 1 つのディレクトリーにまとめてアップロードするのが最善です。
3. Aspera 高速転送は、大量のパケット・ロスがあるネットワーク環境でのパフォーマンスを向上させるために設計されたところがあり、長距離間ネットワークや公共広域ネットワークでプロトコルが機能するようになっています。Aspera 高速転送は、地域内またはデータ・センター内での転送には使用するべきではありません。

Aspera 高速転送 SDK はクローズ・ソースであるため、COS SDK (これは Apache ライセンスを使用します) のオプションの依存関係です。
{:tip}

#### COS と Aspera 高速転送のパッケージ化
{: #aspera-packaging}

以下は、COS SDK が Aspera 高速転送ライブラリーとどのように対話して機能するのかを示す概要図です。

<img alt="COS/Aspera 高速転送 SDK。" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="図 1: COS/Aspera 高速転送 SDK。" caption-side="bottom"} 

### サポートされるプラットフォーム
{: #aspera-sdk-platforms}

| OS                     | バージョン   | アーキテクチャー | テスト済み Java バージョン | テスト済み Python バージョン |
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64 ビット    | 6 以上       | 2.7、3.6       |
| Mac OS X               | 10.13     | 64 ビット    | 6 以上       | 2.7、3.6       |
| Microsoft&reg; Windows | 10        | 64 ビット    | 6 以上       | 2.7、3.6       |

各 Aspera 高速転送セッションは、転送を実行するためにクライアント・マシン上で実行される個別の `ascp` プロセスを作成します。ご使用のコンピューティング環境でこのプロセスの実行が許可されるようにしてください。
{:tip}

**追加制限**

* 32 ビット・バイナリーはサポートされていません
* Windows サポートには Windows 10 が必要です
* Linux サポートは Ubuntu に限定されています (18.04 LTS に対してテスト済みです)
* Aspera Transfer Manager クライアントは、HMAC 資格情報ではなく IAM API キーを使用して作成される必要があります。

### Java を使用した SDK の取得
{: #aspera-sdk-java} 
{: java}

{{site.data.keyword.cos_full_notm}} と Aspera 高速転送 Java SDK を使用する最良の方法は、Maven を使用して依存関係を管理することです。Maven に精通していなくても、[Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window} ガイドを使用すれば問題ありません。
{: java}

Maven は、`pom.xml` という名前のファイルを使用して、Java プロジェクトに必要なライブラリー (およびバージョン) を指定します。{{site.data.keyword.cos_full_notm}} および Aspera 高速転送 Java SDK を使用するための `pom.xml` ファイルの例を以下に示します。
{: java}

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera</artifactId>
            <version>0.1.163682</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}
{: java}

Java を使用した Aspera 高速転送の開始の例は、『[Aspera 高速転送の使用](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera)』セクションにあります。
{: java}

### Python を使用した SDK の取得
{: #aspera-sdk-python} 
{: python}

{{site.data.keyword.cos_full_notm}} および Aspera 高速転送 Python SDK は、Python Package Index (PyPI) ソフトウェア・リポジトリーから入手できます。
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

Python を使用した Aspera 転送の開始の例は、『[Aspera 高速転送の使用](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera)』セクションにあります。
{: python}
