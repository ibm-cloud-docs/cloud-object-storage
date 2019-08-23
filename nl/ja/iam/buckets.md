---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# バケット許可
{: #iam-bucket-permissions}

UI または CLI を使用してポリシーを作成することにより、バケットに対するアクセス役割をユーザーおよびサービス ID に割り当てます。

| アクセス役割 | 操作の例                                             |
|:------------|-------------------------------------------------------------|
| 管理者     | オブジェクトの公開、バケットおよびオブジェクトの作成と破棄 |
| ライター      | バケットおよびオブジェクトの作成と破棄                      |
| リーダー      | オブジェクトのリストおよびダウンロード                                   |
| コンテンツ・リーダー  | オブジェクトのダウンロード                        |

## ユーザーへのアクセス権限の付与
{: #iam-user-access}

ユーザーがコンソールを使用できる必要がある場合は、サービス・アクセス役割 (`リーダー`など) に加えて、最低でもインスタンス自体に対するプラットフォームでの`ビューアー`のアクセス役割**も**ユーザーに付与する必要があります。これにより、すべてのバケットを表示し、バケット内のオブジェクトをリストすることが可能になります。次に、左側のナビゲーション・メニューから**「バケット許可」**を選択し、ユーザーを選択してから、そのユーザーに必要なアクセス権限のレベル (`「マネージャー」`または`「ライター」`) を選択します。

ユーザーが API を使用してデータと対話し、コンソール・アクセスを必要としない場合で、_かつ_、そのユーザーがアカウントのメンバーである場合には、親インスタンスへのアクセス権限を付けずに単一バケットへのアクセス権限を付与できます。

## ポリシーの適用
{: #iam-policy-enforcement}

IAM ポリシーは、最高レベルのアクセス権限から、最も制限されるレベルまで階層的に適用されます。競合がある場合は、より権限が容認されるポリシーのほうに解決されます。例えば、あるユーザーにバケットに対する`ライター`と`リーダー`の両方のサービス・アクセス役割がある場合、`リーダー`の役割を付与しているポリシーは無視されます。

これは、サービス・インスタンス・レベルおよびバケット・レベルのポリシーにも適用されます。

- ユーザーが、サービス・インスタンスに対する`ライター`の役割と単一バケットに対する`リーダー`の役割を付与するポリシーを保持している場合、バケット・レベルのポリシーは無視されます。
- ユーザーが、サービス・インスタンスに対する`リーダー`の役割と単一バケットに対する`ライター`の役割を付与するポリシーを保持している場合、両方のポリシーが適用され、個々のバケットに対しては、より権限が容認される`ライター`の役割が優先されます。

アクセスの対象を単一バケット (またはバケットのセット) に制限する必要がある場合は、コンソールまたは CLI のいずれを使用する場合でもユーザーまたはサービス ID にインスタンス・レベルのポリシーが存在しないようにしてください。

### UI の使用
{: #iam-policy-enforcement-console}

新しいバケット・レベルのポリシーを作成するには、以下のようにします。 

  1. **「管理」**メニューから**「アクセス (IAM)」**コンソールにナビゲートします。
  2. 左側のナビゲーション・メニューから**「ユーザー」**を選択します。
  3. ユーザーを選択します。
  4. **「アクセス・ポリシー」**タブを選択して、ユーザーの既存のポリシーを表示するか、新規ポリシーを割り当てるか、既存のポリシーを編集します。
  5. **「アクセス権限の割り当て」**をクリックして、新規ポリシーを作成します。
  6. **「リソースへのアクセス権限の割り当て」**を選択します。
  7. 最初に、サービス・メニューから**「Cloud Object Storage」**を選択します。
  8. 次に、適切なサービス・インスタンスを選択します。**「リソース・タイプ」**フィールドに`「バケット」`と入力し、**「リソース ID」**フィールドにバケット名を入力します。
  9. 目的のサービス・アクセス役割を選択します。
  10.  **「割り当て」**をクリックします。

**「リソース・タイプ」**フィールドまたは**「リソース」**フィールドをブランクのままにすると、インスタンス・レベルのポリシーが作成されるので注意してください。
{:tip}

### CLI の使用
{: #iam-policy-enforcement-cli}

端末から以下のコマンドを実行します。

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

既存のポリシーをリストするには、以下のようにします。

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

既存のポリシーを編集するには、以下のようにします。

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## サービス ID へのアクセス権限の付与
{: #iam-service-id}
バケットに対するアクセス権限をアプリケーションまたはその他の人間以外のエンティティーに付与する必要がある場合は、サービス ID を使用します。サービス ID は、特にこの目的のために作成することも、既に使用されている既存のサービス ID にすることもできます。

### UI の使用
{: #iam-service-id-console}

  1. **「管理」**メニューから**「アクセス (IAM)」**コンソールにナビゲートします。
  2. 左側のナビゲーション・メニューから**「サービス ID」**を選択します。
  3. サービス ID を選択して既存のポリシーを表示し、新規ポリシーを割り当てるか、既存のポリシーを編集します。
  3. サービス・インスタンス、サービス ID、および目的の役割を選択します。
  4. **「リソース・タイプ」**フィールドに`「バケット」`と入力し、**「リソース」**フィールドにバケット名を入力します。
  5. **「送信」**をクリックします。

  **「リソース・タイプ」**フィールドまたは**「リソース」**フィールドをブランクのままにすると、インスタンス・レベルのポリシーが作成されるので注意してください。
  {:tip}

### CLI の使用
{: #iam-service-id-cli}

端末から以下のコマンドを実行します。

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

既存のポリシーをリストするには、以下のようにします。

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

既存のポリシーを編集するには、以下のようにします。

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
