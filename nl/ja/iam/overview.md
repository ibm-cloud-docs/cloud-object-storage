---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# IAM の概要
{: #iam-overview}

{{site.data.keyword.cloud}} Identity & Access Management により、ユーザーを安全に認証して、{{site.data.keyword.cloud_notm}} プラットフォーム内ですべてのクラウド・リソースへのアクセスを一貫した方法で制御できます。詳しくは、[入門チュートリアル](/docs/iam?topic=iam-getstarted#getstarted)を参照してください。

## ID 管理
{: #iam-overview-identity}

ID 管理には、ユーザー、サービス、およびリソースの相互作用が含まれます。ユーザーは、IBMid によって識別されます。サービスは、サービス ID によって識別されます。さらに、リソースは、CRN を使用して識別およびアドレス指定されます。

{{site.data.keyword.cloud_notm}} IAM Token Service により、ユーザーおよびサービス用の API キーの作成、更新、削除、および使用が可能になります。これらの API キーは、API 呼び出しで作成することも、{{site.data.keyword.cloud}} プラットフォーム・コンソールの「ID およびアクセス」セクションで作成することもできます。同じキーを複数のサービスで使用できます。各ユーザーは、単一キーの公開を制限するために目的別に異なるキーを使用するシナリオや、キー・ローテーション・シナリオをサポートするために複数の API キーを使用できます。

詳しくは、[Cloud IAM とは?](/docs/iam?topic=iam-iamoverview#iamoverview) を参照してください。

### ユーザーと API キー
{: #iam-overview-user-api-keys}

API キーは、自動化およびスクリプティングの目的のため、また、CLI を使用する際の統合ログインのために、{{site.data.keyword.cloud_notm}} ユーザーが作成し、使用できます。API キーは、ID およびアクセス管理 UI で作成するか、`ibmcloud` CLI を使用して作成できます。

### サービス ID と API キー
{: #iam-overview-service-id-api-key}

IAM Token Service により、サービス ID の作成とサービス ID 用の API キーの作成が可能になります。サービス ID は、「機能 ID」または「アプリケーション ID」のようなものであり、ユーザーを表すためではなく、サービスの認証のために使用されます。

ユーザーは、サービス ID を作成し、それらを {{site.data.keyword.cloud_notm}} プラットフォーム・アカウント、CloudFoundry 組織、または CloudFoundry スペースなどのスコープにバインドできます。ただし、IAM を採用する場合は、サービス ID を {{site.data.keyword.cloud_notm}} プラットフォーム・アカウントにバインドするのが最善です。このバインドは、サービス ID に、それが入るコンテナーを示すために行われます。 また、このコンテナーでは、サービス ID の更新および削除を誰ができるか、そのサービス ID に関連付けられた API キーの作成、更新、読み取り、削除を誰ができるかも定義します。 サービス ID はユーザーには関連しないことに注意してください。

### 鍵のローテーション
{: #iam-overview-key-rotation}

漏えいしたキーに起因するセキュリティー・ブリーチ (抜け穴) を防止するために、API キーは定期的なローテーションが必要です。

## アクセス管理
{: #iam-overview-access-management}

IAM アクセス制御は、{{site.data.keyword.cloud_notm}} リソースにユーザー役割を割り当てる一般的な方法を提供し、それらのリソースに対してユーザーが実行できるアクションを制御します。付与されているアクセス・オプションに応じて、アカウント全体または組織全体のユーザーを表示および管理できます。例えば、アカウント所有者には、ID およびアクセス管理のためにアカウント管理者役割が自動的に割り当てられ、それによってアカウントのすべてのメンバーのサービス・ポリシーを割り当てたり管理したりできるようになります。

### ユーザー、役割、リソース、およびポリシー
{: #iam-overview-access-policies}

IAM アクセス制御では、割り当てられたコンテキスト内でリソースおよびユーザーを管理するためのアクセス・レベルを許可するために、サービスまたはサービス・インスタンスごとのポリシーの割り当てが可能です。ポリシーは、属性の組み合わせを使用して適用可能なリソース・セットを定義することにより、リソース・セットに対する 1 つ以上の役割をユーザーに付与します。ユーザーにポリシーを割り当てる場合、まずサービスを指定し、その次に割り当てる役割を指定します。選択するサービスに応じて、追加の構成オプションが使用可能な場合があります。

役割はアクションの集合ですが、これらの役割にマップされるアクションは、サービスに固有です。各サービスは、オンボーディング・プロセスの間にこの役割からアクションへのマッピングを決定します。このマッピングは、サービスのすべてのユーザーに適用されます。役割およびアクセス・ポリシーは、Policy Administration Point (PAP) を通じて構成され、Policy Enforcement Point (PEP) および Policy Decision Point (PDP) を介して適用されます。

詳しくは、[ユーザー、チーム、アプリケーションを編成するためのベスト・プラクティス](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications)を参照してください。
