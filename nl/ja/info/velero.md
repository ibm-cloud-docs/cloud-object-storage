---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: heptio, kubernetes, backup

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

# Velero 統合
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window} (旧称 Heptio Ark) は、S3 API を使用して Kubernetes クラスターをバックアップおよびリストアするためのツール・セットです。

Velero は、以下の 2 つの部分から構成されています。

* クラスター上で実行されるサーバー・コンポーネント
* ローカル・クライアントで実行されるコマンド・ライン・インターフェース

## 前提条件
{: #velero-prereqs}

開始する前に、以下のセットアップが必要です。

* [`IBM Cloud CLI`](/docs/cli?topic=cloud-cli-getting-started){:new_window} がインストールされていること
* [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} コマンド・ライン・ツールがインストール済みで、クラスターに接続するように構成されていること
* {{site.data.keyword.cos_short}} インスタンス
* {{site.data.keyword.cos_short}} バケット
* バケットへのライター権限を持つ HMAC 資格情報

## Velero クライアントのインストール
{: #velero-install}

1. ご使用の OS 用の最新[バージョン](https://github.com/heptio/velero/releases){:new_window}の Velero をダウンロードします。
2. .tar ファイルをローカル・システム上のフォルダーに解凍します。
3. `velero` バイナリーを実行できることを確認します。

```
velero --help
```

```
Velero は、特に Kubernetes クラスター・リソースを対象とした、災害復旧を管理
するためのツールです。これは、アプリケーションの状態および関連データを
バックアップするためのシンプルで構成可能かつ操作的に堅固な方法を提供します。

kubectl に慣れているユーザーの場合、Velero でも同様のモデルがサポートされているため、
「velero get backup」や「velero create schedule」などのコマンドを実行できます。同じ
操作を「velero backup get」や「velero schedule create」として実行することもできます。

使用法:
  velero [command]

使用可能コマンド:
  backup            バックアップの処理
  backup-location   バックアップ・ストレージ・ロケーションの処理
  bug               velero バグのレポート
  client            velero クライアント関連のコマンド
  completion        指定されたシェル (bash または zsh) のシェル完了コードの出力
  create            velero リソースの作成
  delete            velero リソースの削除
  describe          velero リソースの記述
  get               velero リソースの取得
  help              任意のコマンドに関するヘルプ
  plugin            プラグインの処理
  restic            restic の処理
  restore           リストアの処理
  schedule          スケジュールの処理
  server            velero サーバーの実行
  snapshot-location スナップショット・ロケーションの処理
  version           velero バージョンおよび関連イメージの表示
...
```

*(オプション)* Mac OS または Linux では、ark バイナリーを一時フォルダーから、より永続的な場所 (`/usr/local/bin` など) に移動します。
{: tip}

## Velero サーバーのインストールおよび構成
{: #velero-config}
### 資格情報ファイルの作成
{: #velero-config-credentials}

ローカル Velero フォルダー (*.tar ファイルを解凍したフォルダー*) 内に HMAC 鍵を使用して資格情報ファイル (`credentials-velero`) を作成します。

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### kubectl の構成
{: #velero-config-kubectl}

クラスターに接続するために [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} を構成します。

1. CLI を使用して IBM Cloud プラットフォームにログインします。<br/><br/>*セキュリティーを強化する場合、API キーをファイルに保管したり、環境変数として設定したりすることも可能です。*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. クラスター構成を取得します。
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. **export** コマンドをコピー・アンド・ペーストして、KUBECONFIG 環境変数を設定します。

4. 以下を実行して、`kubectl` が正しく構成されていることを確認します。
    ```bash
    kubectl cluster-config
    ```
    {: pre}
  
    ```bash
    Kubernetes master is running at https://c6.hou02.containers.cloud.ibm.com:29244
    Heapster is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/heapster/proxy
    KubeDNS is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
    kubernetes-dashboard is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy
    ```
    {: pre}

### Velero サーバーとクラウド・ストレージの構成
{: #velero-config-storage}

1. Velero フォルダー内で、以下を実行して名前空間、RBAC、およびその他のスキャフォールドをセットアップします。<br/><br/>*デフォルトの名前空間は、`velero` です。カスタム名前空間を作成する必要がある場合は、[Run in custom namespace](https://heptio.github.io/velero/master/namespace.html){:new_window} の説明を参照してください。*
    ```bash
    kubectl apply -f config/common/00-prereqs.yaml
    ```
    {: pre}

    ```bash
    customresourcedefinition.apiextensions.k8s.io/backups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/schedules.velero.io created
    customresourcedefinition.apiextensions.k8s.io/restores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/downloadrequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/deletebackuprequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumebackups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumerestores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/resticrepositories.velero.io created
    customresourcedefinition.apiextensions.k8s.io/backupstoragelocations.velero.io created
    customresourcedefinition.apiextensions.k8s.io/volumesnapshotlocations.velero.io created
    namespace/velero created
    serviceaccount/velero created
    clusterrolebinding.rbac.authorization.k8s.io/velero created
    ```
    {: pre}
2. 資格情報ファイルを使用してシークレットを作成します。
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. `config/ibm/05-ark-backupstoragelocation.yaml` 内に以下の値を指定します。
   * `<YOUR_BUCKET>` - バックアップ・ファイルを保管するバケットの名前
   * `<YOUR_REGION>` - バケットの[ロケーションの制約](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - 地域エンドポイント URL (`https://s3.us.cloud-object-storage.appdomain.cloud`)。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

    *追加情報については、[BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window} 定義を参照してください。*

### Velero サーバーの開始
{: #velero-config-server}

1. Velero フォルダー内で、以下のコマンドを実行して、クラスター内にオブジェクトを作成します。
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. 以下のコマンドを実行して、デプロイメントを作成します。
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. `velero` 名前空間で `kubectl get` を使用することで、デプロイメントが正常にスケジュールされていることを確認します。`「Available」`に `1` と表示されると、Ark は作動可能です。
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## バックアップとリストアのテスト
{: #velero-test}

### バックアップ
{: #velero-test-backup}

現時点で、以下のコマンドを実行して、Kubernetes クラスターの単純バックアップを作成できます。
```bash
velero backup create <backup-name>
```
{: pre}

このコマンドによって、永続ボリュームを含め、クラスター上のすべてのリソースのバックアップが作成されます。

バックアップの対象を特定の名前空間、リソース・タイプ、またはラベルに制限することもできます。

Velero では、名前による選択が許可されておらず、ラベルのみが使用可能です。
{: tip}

このコマンドでは、`app=<app-label>` のラベルが付いたコンポーネントのみがバックアップされます。 
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

以下を実行すると、オプションの完全なリストを表示できます。
```bash
velero backup --help
```
{: pre}

### リストア
{: #velero-test-restore}

バックアップをリストアするには、以下のコマンドを実行します。
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

以下を実行すると、オプションの完全なリストを表示できます。
```bash
velero restore --help
```
{: pre}

