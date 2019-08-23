---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# パブリック・アクセスの許可
{: #iam-public-access}

データは共有するためのものであることがあります。バケットには、学術研究や民間の研究用に公開されたデータ・セット、または Web アプリケーションやコンテンツ配信ネットワークによって使用されるイメージ・リポジトリーが保持されることがあります。**「パブリック・アクセス」**グループを使用して、このようなバケットにアクセスできるようにします。
{: shortdesc}

## コンソールを使用したパブリック・アクセスの設定
{: #iam-public-access-console}

最初に、バケットがあることを確認します。ない場合は、[入門チュートリアル](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)の手順に従って、コンソールについて理解してください。

### パブリック・アクセスの有効化
{: #public-access-console-enable}

1. {{site.data.keyword.cloud_notm}} [コンソール・ダッシュボード](https://cloud.ibm.com/)から、**「ストレージ」**を選択してリソース・リストを表示します。
2. 次に、**「ストレージ」**メニュー内から、バケットがあるサービス・インスタンスを選択します。これにより、{{site.data.keyword.cos_short}} コンソールが表示されます。
3. パブリックにアクセス可能にするバケットを選択します。このポリシーによって、適切な URL を持つすべてのユーザーが_バケット内のすべてのオブジェクト_ をダウンロードできるようになる点に留意してください。
4. ナビゲーション・メニューから**「アクセス・ポリシー」**を選択します。
5. **「パブリック・アクセス」**タブを選択します。
6. **「アクセス・ポリシーの作成」**をクリックします。警告を読んだ後、**「有効化」**を選択します。
7. これで、このバケット内のすべてのオブジェクトにパブリックでアクセス可能になりました。

### パブリック・アクセスの無効化
{: #public-access-console-disable}

1. {{site.data.keyword.cloud_notm}} [コンソール](https://cloud.ibm.com/)内の任意の場所から、**「管理」**メニュー、**「アクセス (IAM)」**を順番に選択します。
2. ナビゲーション・メニューから**「アクセス・グループ」**を選択します。
3. **「パブリック・アクセス」**を選択して、現在使用中のすべてのパブリック・アクセス・ポリシーのリストを表示します。
4. アクセス制御の適用対象に戻す必要があるバケットに対応するポリシーを見つけます。
5. ポリシー項目の右端にあるアクションのリストから、**「削除」**を選択します。
6. ダイアログ・ボックスを確認します。これで、ポリシーはバケットから削除されます。

## 個々のオブジェクトに対するパブリック・アクセスの許可
{: #public-access-object}

REST API を使用してオブジェクトをパブリック・アクセス可能にするには、`x-amz-acl: public-read` ヘッダーを要求に組み込むことができます。このヘッダーを設定すると、あらゆる [IAM ポリシー](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)の検査がバイパスされ、非認証の `HEAD` 要求および `GET` 要求が許可されます。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。

さらに、[HMAC 資格情報](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature)により、[事前署名 URL を使用する一時的なパブリック・アクセス](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url)を許可できます。

### パブリック・オブジェクトのアップロード
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### 既存のオブジェクトに対するパブリック・アクセスの許可
{: #public-access-object-existing}

ペイロードなしで照会パラメーター `?acl` を使用するとともに、`x-amz-acl: public-read` ヘッダーを設定すると、データを上書きすることなく、オブジェクトへのパブリック・アクセスが可能になります。

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### パブリック・オブジェクトを再度プライベートにする
{: #public-access-object-private}

ペイロードなしで照会パラメーター `?acl` を使用するとともに、空の `x-amz-acl:` ヘッダーを設定すると、データを上書きすることなく、オブジェクトへのパブリック・アクセスが取り消されます。

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## 静的 Web サイト
{: #public-access-static-website}

{{site.data.keyword.cos_full_notm}} は、静的 Web サイトの自動ホスティングをサポートしていませんが、Web サーバーを手動で構成し、それを使用して、バケット内でホストされるパブリック・アクセス可能なコンテンツのサービスを提供することができます。詳しくは、[このチュートリアル](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos)を参照してください。
