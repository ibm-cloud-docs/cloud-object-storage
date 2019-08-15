---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# OpenStack Swift からのデータのマイグレーション
{: #migrate}

{{site.data.keyword.cos_full_notm}} が {{site.data.keyword.cloud_notm}} プラットフォーム・サービスとして使用可能になる前は、オブジェクト・ストアを必要とするプロジェクトは [OpenStack Swift](https://docs.openstack.org/swift/latest/) または [OpenStack Swift (infrastructure)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift) を使用していました。 開発者はアプリケーションを更新し、そのデータを {{site.data.keyword.cloud_notm}} にマイグレーションして、IAM および Key Protect によって提供される新しいアクセス制御と暗号化の利点や、使用可能になった新機能を利用することをお勧めします。

Swift の「コンテナー」の概念は、COS の「バケット」と同じです。 COS はサービス・インスタンスを 100 バケットに制限しますが、一部の Swift インスタンスはより多くのコンテナーを保持することができます。 COS バケットは数十億ものオブジェクトを保持することができ、データを編成する際には、オブジェクト名でディレクトリーのような「接頭部」としてのスラッシュ (`/`) がサポートされています。 COS は、バケット・レベルおよびサービス・インスタンス・レベルで IAM ポリシーをサポートしています。
{:tip}

オブジェクト・ストレージ・サービス間でデータをマイグレーションする 1 つの方法は、[オープン・ソースの `rclone` コマンド・ライン・ユーティリティー](https://rclone.org/docs/)などの「sync」ツールまたは「clone」ツールを使用することです。 このユーティリティーは、クラウド・ストレージを含む 2 つのロケーション間でファイル・ツリーを同期します。 `rclone` は COS にデータを書き込むときに、COS/S3 API を使用してラージ・オブジェクトをセグメント化し、構成パラメーターとして設定されたサイズとしきい値に従って各部分を並行してアップロードします。

COS と Swift には、データ・マイグレーションの一環として考慮する必要があるいくつかの相違点があります。

  - COS はまだ有効期限ポリシーおよびバージョン管理をサポートしていません。 Swift のこれらの機能に依存しているワークフローは、COS へのマイグレーション時に、アプリケーション・ロジックの一部としてそれらを処理する必要があります。
  - COS はオブジェクト・レベルのメタデータをサポートしますが、`rclone` を使用してデータをマイグレーションする場合、この情報は保持されません。 カスタム・メタデータは、`x-amz-meta- {2}: {0}` ヘッダーを使用して COS 内のオブジェクトに設定できますが、`rclone` を使用する前に、オブジェクト・レベルのメタデータをデータベースにバックアップすることをお勧めします。 カスタム・メタデータは、[オブジェクトをそれ自体にコピー](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object)することによって既存のオブジェクトに適用できます。システムは、オブジェクト・データが同一であることを認識し、メタデータのみを更新します。 なお、`rclone` は、タイム・スタンプを保持**できます**。
  - COS は、サービス・インスタンスおよびバケット・レベルのアクセス制御に IAM ポリシーを使用します。 `public-read` ACL を設定することで、[オブジェクトをパブリックに使用可能にすることができます](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access)。これにより、許可ヘッダーが不要になります。
  - COS/S3 API でのラージ・オブジェクトの[マルチパート・アップロード](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)は、Swift API とは異なる方法で処理されます。
  - COS では、`Cache-Control`、`Content-Encoding`、`Content-MD5`、および `Content-Type`など、よく知られているオプションの HTTP ヘッダーを使用できます。

このガイドでは、単一の Swift コンテナーから単一の COS バケットにデータをマイグレーションする手順を説明します。 この手順は、マイグレーションするすべてのコンテナーに対して繰り返す必要があります。その後で、新しい API を使用するようにアプリケーション・ロジックを更新する必要があります。 データをマイグレーションした後は、`rclone check` を使用して転送の整合性を検証できます。これにより、MD5 チェックサムが比較され、一致しないオブジェクトのリストが生成されます。


## {{site.data.keyword.cos_full_notm}} のセットアップ
{: #migrate-setup}

  1. {{site.data.keyword.cos_full_notm}} インスタンスをまだ作成していない場合は、[カタログ](https://cloud.ibm.com/catalog/services/cloud-object-storage)からプロビジョンします。
  2. 転送したデータの保管に必要となるバケットを作成します。 [入門ガイド](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)に目を通して、[エンドポイント](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)や[ストレージ・クラス](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes)などの主要な概念についての理解を深めてください。
  3. Swift API の構文は COS/S3 API と大きく異なるため、COS SDK で提供される同等のメソッドを使用するには、アプリケーションをリファクタリングしなければならない場合があります。 ライブラリーは、[Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java)、[Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)、[Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)、または [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) で使用可能です。

## マイグレーション・ツールを実行するためのコンピュート・リソースのセットアップ
{: #migrate-compute}
  1. データとの近接性が最も高い Linux/macOS/BSD マシン、または IBM Cloud インフラストラクチャーのベアメタル・サーバーまたは仮想サーバーを選択してください。
     推奨されるサーバー構成は、32 GB RAM、2 から 4 コア・プロセッサー、および 1000 Mbps のプライベート・ネットワーク速度です。  
  2. IBM Cloud インフラストラクチャーのベアメタル・サーバーまたは仮想サーバーでマイグレーションを実行する場合は、**プライベート**の Swift および COS のエンドポイントを使用します。
  3. それ以外の場合は、**パブリック**の Swift および COS のエンドポイントを使用します。
  4. [パッケージ・マネージャーまたはプリコンパイル済みバイナリーのいずれか](https://rclone.org/install/)から `rclone` をインストールします。

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## OpenStack Swift 用の `rclone` の構成
{: #migrate-rclone}

  1. `rclone` 構成ファイルを `~/.rclone.conf` に作成します。

        ```
        touch ~/.rclone.conf
        ```

  2. 以下をコピーし、`rclone.conf` に貼り付けて、Swift ソースを作成します。

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. OpenStack Swift 資格情報を取得します。
    <br>a. [IBM Cloud コンソールのダッシュボード](https://cloud.ibm.com/)で Swift インスタンスをクリックします。
    <br>b. ナビゲーション・パネルで**「サービス資格情報」**をクリックします。
    <br>c. **「新規資格情報」**をクリックして、資格情報を生成します。 **「追加」**をクリックします。
    <br>d. 作成した資格情報を表示し、JSON コンテンツをコピーします。

  4. 以下のフィールドに入力します。

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. 『COS 用の `rclone` の構成』セクションにスキップします。


## OpenStack Swift (infrastructure) 用の `rclone` の構成
{: #migrate-config-swift}

  1. `rclone` 構成ファイルを `~/.rclone.conf` に作成します。

        ```
        touch ~/.rclone.conf
        ```

  2. 以下をコピーし、`rclone.conf` に貼り付けて、Swift ソースを作成します。

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. OpenStack Swift (infrastructure) 資格情報を取得します。
    <br>a. IBM Cloud インフラストラクチャーのカスタマー・ポータルで、Swift アカウントをクリックします。
    <br>b. マイグレーション・ソース・コンテナーのデータ・センターをクリックします。
    <br>c. **「資格情報の表示」**をクリックします。
    <br>d. 以下をコピーします。
      <br>&nbsp;&nbsp;&nbsp;**ユーザー名**
      <br>&nbsp;&nbsp;&nbsp;**API キー**
      <br>&nbsp;&nbsp;&nbsp;マイグレーション・ツールの実行場所に基づく**認証エンドポイント**

  4. OpenStack Swift (infrastructure) 資格情報を使用して、以下のフィールドに入力します。

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## COS 用の `rclone` の構成
{: #migrate-config-cos}

### COS 資格情報の取得
{: #migrate-config-cos-credential}

  1. IBM Cloud コンソールで COS インスタンスをクリックします。
  2. ナビゲーション・パネルで**「サービス資格情報」**をクリックします。
  3. **「新規資格情報」**をクリックして、資格情報を生成します。
  4. **「インラインの構成パラメーター」**の下で、`{"HMAC":true}` を追加します。 **「追加」**をクリックします。
  5. 作成した資格情報を表示し、JSON コンテンツをコピーします。

### COS エンドポイントの取得
{: #migrate-config-cos-endpoint}

  1. ナビゲーション・パネルで**「バケット」**をクリックします。
  2. マイグレーション・ターゲットのバケットをクリックします。
  3. ナビゲーション・パネルで**「構成」**をクリックします。
  4. **「エンドポイント」**セクションまでスクロールダウンし、マイグレーション・ツールの実行場所に基づくエンドポイントを選択します。

  5. 以下をコピーし、`rclone.conf` に貼り付けて、COS ターゲットを作成します。

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. COS の資格情報とエンドポイントを使用して、以下のフィールドに入力します。

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## マイグレーションのソースとターゲットが正しく構成されているかどうかの確認
{: #migrate-verify}

1. Swift コンテナーをリストして、`rclone` が正しく構成されていることを確認します。

    ```
    rclone lsd SWIFT:
    ```

2. COS バケットをリストして、`rclone` が正しく構成されていることを確認します。

    ```
    rclone lsd COS:
    ```

## `rclone` を実行します。
{: #migrate-run}

1. `rclone` のリハーサル (データのコピーなし) を実行して、ソース Swift コンテナー (例: `swift-test`) 内のオブジェクトをターゲット COS バケット (例: `cos-test`) に同期します。

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. マイグレーションするファイルが、コマンド出力に表示されていることを確認します。 すべて問題なければ、`--dry-run` フラグを削除し、`-v` フラグを追加してデータをコピーします。 オプションの `--checksum` フラグを使用すると、両方のロケーションで MD5 ハッシュとオブジェクト・サイズが同じファイルは更新されなくなります。

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   rclone を実行しているマシン上の CPU、メモリー、およびネットワークを最大限に活用して、転送時間を最短にする必要があります。
   rclone の調整に関して考慮すべきその他のいくつかのパラメーターを以下に示します。

   --checkers int  並行して実行されるチェッカーの数。 (デフォルト 8)
   これは実行されるチェックサム比較スレッドの数です。 この値は、64 以上にすることをお勧めします。


   --transfers int 並行して実行されるファイル転送の数。 (デフォルト 4)
   これは並行して転送されるオブジェクトの数です。 この値は、64、128、またはそれ以上にすることをお勧めします。


   --fast-list 再帰リストの使用 (使用可能な場合)。 より多くのメモリーを使用しますが、トランザクションは少なくなります。
   このオプションは、パフォーマンスを向上させるために使用します。 このオプションによって、オブジェクトのコピーに必要な要求の数が減少します。

`rclone` を使用したデータのマイグレーションでは、ソース・データをコピーしますが削除はしません。
{:tip}


3. マイグレーションが必要な他のすべてのコンテナーに対して、この手順を繰り返します。
4. すべてのデータがコピーされ、アプリケーションが COS 内のそのデータにアクセスできることを確認したら、Swift サービス・インスタンスを削除します。
