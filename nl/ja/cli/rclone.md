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

# `rclone` の使用
{: #rclone}

## `rclone` のインストール
{: #rclone-install}

`rclone` ツールは、ディレクトリーの同期を維持し、ストレージ・プラットフォーム間でデータをマイグレーションするために役立ちます。これは Go プログラムで、単一のバイナリー・ファイルとして提供されます。

### クイック・スタート・インストール
{: #rclone-quick}

*  関連するバイナリーを[ダウンロード](https://rclone.org/downloads/)します。 
*  アーカイブから `rclone` バイナリーまたは `rclone.exe` バイナリーを解凍します。
*  `rclone config` を実行してセットアップします。

### スクリプトを使用したインストール
{: #rclone-script}

Linux /macOS/BSD システムに `rclone` をインストールするには、以下のようにします。

```
curl https://rclone.org/install.sh | sudo bash
```

以下のように、ベータ版も使用可能です。

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

インストール・スクリプトは最初にインストールされている `rclone` のバージョンを検査し、現行バージョンが既に最新の場合はダウンロードをスキップします。
{:note}

### プリコンパイル済みバイナリーからの Linux でのインストール
{: #rclone-linux-binary}

まず、以下のようにバイナリーをフェッチして解凍します。

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

次に、以下のようにバイナリー・ファイルを適切な場所にコピーします。

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

以下のように資料をインストールします。

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

以下のように `rclone config` を実行してセットアップします。

```
rclone config
```

### プリコンパイル済みバイナリーからの macOS でのインストール
{: #rclone-osx-binary}

まず、以下のように `rclone` パッケージをダウンロードします。

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

次に、以下のように、ダウンロードしたファイルを解凍し、解凍したフォルダーに `cd` で移動します。

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

以下のように、`rclone` を `$PATH` に移動し、プロンプトが出されたらパスワードを入力します。

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

ディレクトリーが存在する場合でも、`mkdir` コマンドは安全に実行できます。
{:tip}

残っているファイルを削除します。

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

以下のように `rclone config` を実行してセットアップします。

```
rclone config
```

## IBM COS へのアクセスの構成
{: #rclone-config}

1. `rclone config` を実行し、新規リモートを示す `n` を選択します。

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. 以下のように、構成の名前を入力します。
```
	name> <YOUR NAME>
```

3. 「s3」ストレージを選択します。

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

  4. S3 ストレージ・プロバイダーとして IBM COS を選択します。

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

  1. **False** を入力して資格情報を入力します。

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

  6. アクセス・キーおよびシークレットを入力します。

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. IBM COS のエンドポイントを指定します。パブリック IBM COS の場合、提供されているオプションから選択します。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

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

  8. IBM COS ロケーション制約を指定します。ロケーション制約は、エンドポイントと一致する必要があります。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

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

  9. ACL を指定します。`public-read` と `private` のみがサポートされます。 

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. 表示されている構成を確認し、「remote」を受け入れて保存してから、終了します。構成ファイルは次のようになります。

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

## コマンド・リファレンス
{: #rclone-reference}

### バケットの作成
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### 使用可能なバケットのリスト
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### バケットのコンテンツのリスト表示
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### ローカルからリモートへのファイルのコピー
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### リモートからローカルへのファイルのコピー
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### リモートでのファイルの削除
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### リスト・コマンド
{: #rclone-reference-listing}

関連するリスト・コマンドがいくつかあります。
* `ls` は、オブジェクトのサイズとパスのみをリストします。
* `lsl` は、オブジェクトの変更時刻、サイズ、およびパスのみをリストします。
* `lsd` は、ディレクトリーのみをリストします。
* `lsf` は、オブジェクトとディレクトリーを解析しやすい形式でリストします。
* `lsjson` は、オブジェクトとディレクトリーを JSON 形式でリストします。

## `rclone sync`
{: #rclone-sync}

`sync` 操作は、ソースと宛先を同一にし、宛先のみを変更します。同期では、サイズと変更時刻、または MD5SUM によるテストを行って、変更されていないファイルは転送されません。宛先は、必要に応じてファイルの削除を含め、ソースと一致するように更新されます。

これはデータ損失の原因となる可能性があるため、最初に `--dry-run` フラグを指定してテストし、コピーおよび削除される内容を正確に確認してください。
{:important}

いずれの時点でもエラーがあった場合、宛先内のファイルは削除されないことに注意してください。

ディレクトリー自体ではなく、ディレクトリーの_内容_ が同期されます。`source:path` がディレクトリーの場合、コピーされるのは、ディレクトリー名と内容ではなく、`source:path` の内容です。詳しくは、`copy` コマンドの詳細な説明を参照してください。

`dest:path` が存在しない場合は作成され、`source:path` の内容がそこに入れられます。

```sh
rclone sync source:path dest:path [flags]
```

### 複数の場所からの `rclone` の同時使用
{: #rclone-sync-multiple}

以下のように、出力に別のサブディレクトリーを選択すると、複数の場所から同時に `rclone` を使用できます。

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

同じディレクトリーに `sync` する場合は、以下のように、`rclone copy` を使用する必要があります。そうしないと、2 つのプロセスによってお互いのファイルが削除されることがあります。

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

`sync`、`copy`、または `move` を使用する場合、上書きまたは削除されたファイルは、元の階層のこのディレクトリーに移動されます。

`--suffix` を設定すると、移動されたファイルにサフィックスが追加されます。ディレクトリーに同じパス (サフィックスが追加された後) のファイルがある場合、そのファイルは上書きされます。

使用中のリモートはサーバー・サイドの移動またはコピーをサポートしている必要があり、同期の宛先と同じリモートを使用する必要があります。バックアップ・ディレクトリーは、宛先ディレクトリーとオーバーラップしてはなりません。

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

`/path/to/local` を `remote:current` に `sync` しますが、更新または削除されたファイルはすべて、`remote:old` に保管されます。

`rclone` をスクリプトから実行する場合、`--backup-dir` に渡すディレクトリー名として今日の日付を使用して古いファイルを保管することも、今日の日付を指定した `--suffix` を渡すこともできます。

## `rclone` での日次同期
{: #rclone-sync-daily}

バックアップのスケジュールは、バックアップの自動化にとって重要です。これを行う方法は、プラットフォームによって異なります。Windows ではタスク・スケジューラーを使用できます。一方、MacOS および Linux では crontab を使用できます。

### ディレクトリーの同期
{: #rclone-sync-directory}

`rclone`は、ローカル・ディレクトリーをリモート・コンテナーと同期し、ローカル・ディレクトリー内のすべてのファイルをコンテナーに保管します。`rclone` は構文 `rclone sync source destination` を使用します。ここで、`source` はローカル・フォルダーであり、`destination` は IBM COS 内のコンテナーです。

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

既に宛先が作成されている場合がありますが、そうでない場合は、上記の手順を使用して新しいバケットを作成できます。

### ジョブのスケジュール
{: #rclone-sync-schedule}

ジョブをスケジュールする前に、初期アップロードが実行され、完了していることを確認してください。

#### Windows
{: #rclone-sync-windows}

1. コンピューター上の任意の場所に `backup.bat` というテキスト・ファイルを作成し、[ディレクトリーの同期](#rclone-sync-directory)に関するセクションで使用したコマンドを貼り付けます。 rclone.exe の絶対パスを指定し、ファイルの保存を忘れないでください。

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. `schtasks` を使用して、ジョブをスケジュールします。このユーティリティーは、複数のパラメーターを取ります。
	* /RU – ジョブを実行するユーザー。これは、使用するユーザーがログアウトしている場合に必要です。
	* /RP – ユーザーのパスワード。
	* /SC – DAILY に設定します。
	* /TN – ジョブの名前。backup という名前にします。
	* /TR – 先ほど作成した backup.bat ファイルのパス。
	* /ST – タスクを開始する時刻。これは、24 時間の時刻形式です。01:05:00 は 1:05 AM です。13:05:00 は 1:05 PM です。

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac および Linux
{: #rclone-sync-nix}

1. コンピューター上の任意の場所に `backup.sh` というテキスト・ファイルを作成し、[ディレクトリーの同期](#rclone-sync-directory)のセクションで使用したコマンドを貼り付けます。以下のようになります。rclone 実行可能ファイルの絶対パスを指定し、ファイルの保存を忘れないでください。

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. `chmod` を使用して、スクリプトを実行可能にします。

```sh
chmod +x backup.sh
```

3. crontab を編集します。

```sh
sudo crontab -e
```

4. crontab ファイルの最後に項目を追加します。crontab は単純です。最初の 5 つのフィールドは、順に分、時間、日、月、週を表します。* の使用はすべてを示します。`backup.sh` を毎日 1:05 AM に実行する場合は、以下のようなものを使用します。

```sh
5 1 * * * /full/path/to/backup.sh
```

5. crontab を保存すると、準備完了です。
