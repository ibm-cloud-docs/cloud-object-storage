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

# `s3fs` を使用したバケットのマウント
{: #s3fs}

NFS スタイルのファイル・システムに対する読み取りおよび書き込みを予期するアプリケーションでは、`s3fs`を使用でき、ファイルのネイティブ・オブジェクト形式を保持しながら、ディレクトリーとしてバケットをマウントできます。これにより、リスト表示用の `ls` やファイルのコピー用の `cp` のような使い慣れたシェル・コマンドを使用したり、ローカル・ファイルに対する読み取りおよび書き込みに依存するレガシー・アプリケーションにアクセスしたりして、クラウド・ストレージと対話できます。詳細な概要については、[プロジェクトの公式の README を参照してください](https://github.com/s3fs-fuse/s3fs-fuse)。

## 前提条件
{: #s3fs-prereqs}

* IBM Cloud アカウントおよび {{site.data.keyword.cos_full}} のインスタンス
* Linux 環境または OSX 環境
* 資格情報 ([IAM API キー](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)または [HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac))

## インストール
{: #s3fs-install}

OSX では、以下のように [Homebrew](https://brew.sh/) を使用します。

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

Debian または Ubuntu では、以下のようにします。 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

公式の `s3fs` 資料では、`libcurl4-openssl-dev` ではなく `libcurl4-gnutls-dev` を使用することが推奨されています。どちらも機能しますが、OpenSSL バージョンの方がよりパフォーマンスが向上する可能性があります。
{:tip}

ソースから `s3fs` をビルドすることもできます。まず、以下のように Github リポジトリーを複製します。

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

次に、以下のように `s3fs` をビルドします。

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

その後、以下のようにバイナリーをインストールします。

```sh
sudo make install
```
{:codeblock}

## 構成
{: #s3fs-config}

`<access_key>:<secret_key>` または `:<api_key>` のいずれかを含めて、ファイルに資格情報を保管します。このファイルへのアクセスは制限する必要があるため、以下を実行します。

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

これで、以下を使用してバケットをマウントできます。

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

資格情報ファイルに API キーしかない (HMAC 資格情報がない) 場合は、以下のように `ibm_iam_auth` フラグも追加する必要があります。

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

`<bucket>` は既存のバケットであり、`<mountpoint>`は、バケットをマウントするローカル・ディレクトリーです。`<endpoint>` は、[バケットの場所](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)に対応している必要があります。`credentials_file` は、 API キーまたは HMAC 資格情報を使用して作成されたファイルです。

これで、`ls <mountpoint>` を実行すると、当該バケット内のオブジェクトはローカル・ファイル (または、オブジェクト接頭部の場合はネストされたディレクトリー) としてリストされます。

## パフォーマンス最適化
{: #s3fs-performance}

パフォーマンスは本物のローカル・ファイル・システムと等しくなることはありませんが、いくつかの拡張オプションを使用してスループットを向上させることができます。 

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

1. `cipher_suites=AESGCM` は、HTTPS エンドポイントを使用する場合にのみ関係します。デフォルトでは、IBM COS へのセキュア接続は `AES256-SHA` 暗号スイートを使用します。代わりに、`AESGCM` スイートを使用すると、同じレベルの暗号セキュリティーを維持しながら、TLS 暗号機能により、クライアント・マシンの CPU オーバーヘッドが大幅に削減されます。
2. `kernel_cache` は、`s3fs` マウント・ポイントでカーネル・バッファー・キャッシュを使用可能にします。つまり、同じファイルの反復読み取りをカーネルのバッファー・キャッシュから処理できるため、オブジェクトは `s3fs` によって一度だけ読み取られます。カーネル・バッファー・キャッシュは、他のプロセスで使用されていない空きメモリーのみを使用します。バケットのマウント中に別のプロセス/マシンからバケット・オブジェクトが上書きされることが予想され、お客様のユースケースで最新のコンテンツにライブ・アクセスする必要がある場合、このオプションはお勧めしません。 
3. `max_background=1000` は、`s3fs` 並行ファイル読み取りパフォーマンスを向上させます。デフォルトでは、FUSE は最大 128 KB のファイル読み取り要求をサポートします。これを超える読み取りを要求すると、カーネルは大きな要求を小さいサブ要求に分割し、s3fs がそれらを非同期的に処理できるようにします。`max_background` オプションは、このような並行非同期要求のグローバル最大数を設定します。デフォルトでは 12 に設定されていますが、これを任意の大きい値 (1000) に設定すると、多数のファイルを同時に読み取る場合でも読み取り要求がブロックされなくなります。
4. `max_stat_cache_size=100000` を設定すると、`s3fs` によって送信される冗長な HTTP `HEAD` 要求の数が減り、ディレクトリーのリストやファイル属性の取得にかかる時間が短縮します。標準的なファイル・システムの使用では、オブジェクト・ストレージ・システム上の `HEAD` 要求にマップされる `stat()` 呼び出しを介してファイルのメタデータに頻繁にアクセスします。デフォルトでは、`s3fs` は最大 1000 個のオブジェクトの属性 (メタデータ) をキャッシュに入れます。キャッシュされる各項目は、最大で 0.5 KB のメモリーを占有します。キャッシュがバケット内のすべてのオブジェクトのメタデータを保持できるようにするのが理想的です。ただし、このキャッシングによるメモリー使用の影響を考慮することをお勧めします。`100000` に設定すると、占有スペースは 0.5 KB * 100000= 50 MB 以下になります。
5. `multipart_size=52` は、COS サーバーとの間で送受信される要求および応答の最大サイズを MB 単位で設定します。`s3fs` は、デフォルトでこれを 10 MB に設定します。この値を大きくすると、HTTP 接続当たりのスループット (MB/秒) も向上します。一方、ファイルから提供される最初のバイトの待ち時間がそれぞれ増加します。したがって、お客様のユースケースで各ファイルから少量のデータのみを読み取る場合は、恐らく、この値を大きくしたくないと思われます。さらに、ラージ・オブジェクト (例えば 50 MB を超えるもの) では、この値が十分に小さく、複数の要求を使用してファイルを並行してフェッチできる場合、スループットが向上します。このオプションの最適値は約 50 MB と思われます。COS のベスト・プラクティスでは、4 MB の倍数の要求を使用することが推奨されるため、このオプションを 52 (MB) に設定することをお勧めします。
6. `parallel_count=30` は、単一のファイルの読み取り/書き込み操作ごとに、COS に並行して送信される要求の最大数を設定します。デフォルトでは、これは 5 に設定されています。非常に大きなオブジェクトの場合、この値を大きくすると、スループットを向上させることができます。前のオプションと同様に、各ファイルの少量のデータのみを読み取る場合は、この値を小さいままにしてください。
7. `multireq_max=30`: ディレクトリーをリストすると、リスト内のオブジェクトごとにオブジェクト・メタデータ要求 (`HEAD`) が送信されます (メタデータがキャッシュ内に見つからない場合)。このオプションは、単一のディレクトリー・リスト操作で COS に並行して送信されるそのような要求の数を制限します。デフォルトでは、20 に設定されています。なお、この値は、上記の `parallel_count` オプション以上でなければなりません。
8. `dbglevel=warn` は、メッセージを /var/log/syslog に記録するために、デバッグ・レベルをデフォルトのレベル (`crit`) ではなく、`warn` に設定します。

## 制限
{: #s3fs-limitations}

オブジェクト・ストレージ・サービスは最初のバイトまでの待ち時間が長く、ランダム書き込みアクセスがないため、s3fs はすべてのアプリケーションに適しているとは限らないことに注意してください。ディープ・ラーニングのワークロードなど、大規模ファイルのみを読み取るワークロードでは、`s3fs` を使用して良好なスループットを実現できます。 
