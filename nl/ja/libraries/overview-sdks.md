---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-19"

keywords: sdks, overview

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
{:go: .ph data-hd-programlang='go'}

# IBM COS SDK について
{: #sdk-about}

IBM COS は、Java、Python、NodeJS、および Go 用の SDK を提供しています。これらの SDK は公式の AWS S3 API SDK に基づいていますが、IAM、Key Protect、Immutable Object Storage などの IBM Cloud の機能を使用するように変更されています。

| 機能                     | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| IAM API キー・サポート      | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |
| 管理された複数パーツ・アップロード   | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |
| 管理された複数パーツ・ダウンロード   | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| バケット・リストの拡張      |                                                   |                                                   |                                                   |                                                   |                                                   |
| バージョン 2 オブジェクト・リスト    | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Key Protect                 | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |
| SSE-C                       | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| アーカイブ・ルール          | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| 保存ポリシー                | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Aspera 高速転送             | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) | ![チェック・マーク・アイコン](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## IAM API キー・サポート
{: #sdk-about-iam}
アクセス・キー/秘密鍵のペアの代わりに API キーを使用してクライアントを作成できます。トークン管理は自動的に処理され、長時間実行される操作では実行中にトークンが自動的にリフレッシュされます。
## 管理された複数パーツ・アップロード
`TransferManager` クラスを使用して、SDK が、オブジェクトを複数パーツでアップロードするために必要なすべてのロジックを処理します。
## 管理された複数パーツ・ダウンロード
`TransferManager` クラスを使用して、SDK が、オブジェクトを複数パーツでダウンロードするために必要なすべてのロジックを処理します。
## バケット・リストの拡張
これは、バケットのリストを返す S3 API の拡張であり、リストするときにバケットのプロビジョニング・コード (`LocationConstraint` として返される、バケットのロケーションとストレージ・クラスの組み合わせ) を使用できます。サービス・インスタンス内のバケットは、使用されるエンドポイントに関係なくすべてリストされるため、この機能はバケットを見つける場合に便利です。
## バージョン 2 オブジェクト・リスト
バージョン 2 リストでは、オブジェクト・リストのより強力なスコープ設定が可能になります。
## Key Protect
Key Protect は、暗号鍵を管理する IBM Cloud サービスであり、バケット作成時のオプション・パラメーターの 1 つです。
## SSE-C                      
## アーカイブ・ルール              
## 保存ポリシー         
## Aspera 高速転送 
