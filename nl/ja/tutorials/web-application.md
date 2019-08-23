---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-08"

keywords: tutorial, web application, photo galleries

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

# チュートリアル: イメージ・ギャラリー Web アプリケーション
{: #web-application}

Web アプリケーションの作成は、最初から最後まで多くの異なる概念をカバーしており、{{site.data.keyword.cos_full}} の機能を理解するのに適切な方法です。 このチュートリアルでは、{{site.data.keyword.cloud}} プラットフォームでシンプルなイメージ・ギャラリーを作成する方法と、多くの異なる概念や手法をまとめる方法を示します。 アプリケーションは、ユーザーが JPEG イメージ・ファイルをアップロードしたり表示したりできる Node.js アプリケーションのバックエンド・サーバーとして {{site.data.keyword.cos_full_notm}} を使用します。

## 始める前に
{: #wa-prereqs}

Web アプリケーションを作成するための前提条件として、以下から始めます。

  - {{site.data.keyword.cloud_notm}} プラットフォーム・アカウント
  - {{site.data.keyword.cloud_notm}} 開発者ツールの一部としての Docker
  - Node.js 
  - Git (デスクトップ・アプリケーションとコマンド・ライン・インターフェース (CLI) の両方)

### Docker のインストール
{: #tutorial-wa-install-docker}

従来のサーバー・インスタンスや仮想サーバーを使用した Web アプリケーションの作成を、Docker のようなコンテナーを使用した作成に移行すると、開発スピードが上がり、デプロイメントの自動化がサポートされると同時に、テストが簡易化されます。 コンテナーは、オペレーティング・システムのように追加のオーバーヘッドを必要としない軽量の構造になっており、依存関係から設定までのすべてに関する構成とコードのみを保持します。

まずは、経験豊富な開発者にとっては使い慣れたツールであり、開発初心者にとっては新たな常用ツールとなるコマンド・ラインを開きましょう。 グラフィック・ユーザー・インターフェース (GUI) が開発されてからというもの、コンピューターのコマンド・ライン・インターフェースは 2 番目の地位に格下げされてきました。 しかし、ここではコマンド・ラインが主要ツールとなります (ただし、Web を参照して新しいコマンド・ライン・ツール・セットをダウンロードしなければならない場合があるなど、GUI をすぐに使用しなくなることはありません)。 

それでは、端末、またはご使用のオペレーティング・システム用のその他の適切なコマンド・ライン・インターフェースを開いて、使用している特定のシェルに適したコマンドでディレクトリーを作成しましょう。 ご自身の参照ディレクトリーを、作成したこの新しいディレクトリーに変更します。 作成されると、アプリケーションはその中に、稼働と実行に必要なスターター・コードと構成を含む専用のサブディレクトリーを入れます。

コマンド・ラインを終了してブラウザーに戻り、リンクにある手順に従って [{{site.data.keyword.cloud_notm}} プラットフォームの開発者ツール](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli)をインストールします。
開発者ツールでは、クラウド・アプリケーションの作成やデプロイを行うための拡張可能で反復可能な手法が提供されます。

[Docker](https://www.docker.com) は開発者ツールの一部としてインストールされ、その処理は主にバックグラウンドで行われますが、新しいアプリを構築するルーチン内で必要になります。 build コマンドが機能するためには、Docker が実行中でなければなりません。 [Dockerhub](https://hub.docker.com) で Docker アカウントをオンラインで作成して、Docker アプリを実行し、サインインしましょう。

### Node.js のインストール
{: #tutorial-wa-install-node}

ビルドするアプリは、[Node.js](https://nodejs.org/) をサーバー・サイド・エンジンとして使用して、この Web アプリケーションの JavaScript コードを実行します。 アプリの依存関係を管理するために、Node に含まれる Node Package Manager (npm) を使用するには、Node.js をローカルにインストールする必要があります。 また、Node.js をローカルにインストールするとテストが単純化されるため、開発スピードが上がります。 

始める前に、Node Version Manager (`nvm`) などのバージョン・マネージャーを使用して Node をインストールし、複数バージョンの Node.js を管理する複雑さを軽減することを検討してください。 本資料の執筆時点では、Mac または Linux マシンで `nvm` のインストールや更新を行うには、最初の 2 つの例のいずれかのコマンドをコピーし、先ほど開いた CLI インターフェースでコマンド・ラインに貼り付け、Enter キーを押すことで、cURL を使用するインストール・スクリプトを使用することができます (ここでは、ご使用のシェルは BASH であることを想定しており、それに代わるものではないことに注意してください)。

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="例 1. cURL を使用した Node Version Manager (nvm) のインストール" caption-side="bottom"}
`例 1. cURL を使用した Node Version Manager (nvm) のインストール`
   
...あるいは、Wget を使用できます (両方ではなく、いずれか 1 つのみ必要です。システムで使用可能な方を使用してください)。

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="例 2. Wget を使用した Node Version Manager (nvm) のインストール" caption-side="bottom"}
`例 2. Wget を使用した Node Version Manager (nvm) のインストール`

または、Windows の場合は、[nvm for Windows](https://github.com/coreybutler/nvm-windows) をリンクにあるインストーラーとソース・コードとともに使用できます。

Node.js の複数のリリースをサポートするという余計な複雑さを省くには、[Node.js](https://nodejs.org/en/download/releases/) Web サイトにアクセスし、{{site.data.keyword.cloud_notm}} プラットフォームで現在使用されている SDK for Node.js ビルドパックでサポートされる最新バージョンに一致する Node.js の長期サポート (LTS) バージョンをインストールしてください。 本資料の執筆時点では、最新のビルドパックは v3.26 であり、Node.js Community Edition v6.17.0 以上がサポートされています。 

最新の {{site.data.keyword.cloud_notm}} SDK for Node.js ビルドパックに関する追加情報については、[SDK for Nodejs の最新の更新](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates)ページをご確認ください。 

例 3 のコマンドをコピーしてコマンド・ラインに貼り付け、`nvm` を使用して要件に一致するバージョンの Node をインストールできます。

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="例 3. `nvm` を使用した特定のバージョンの Node.js のインストール" caption-side="bottom"}
`例 3. nvm を使用した特定のバージョンの Node.js のインストール`

いずれの手法でも、ご使用のオペレーティング・システムと戦略に応じて、説明に従い Node.js と (Node に含まれている) npm をコンピューターにインストールできたら、うまく開始できたことを喜びましょう。

### Git のインストール
{: #tutorial-wa-install-git}

Git は、Web 用のアプリケーションを作成する開発者の間で最も広く使用されているソース・コードのバージョン管理システムであるため、すでに Git に関する知識をお持ちかもしれません。 ここでは、後で継続的デリバリーと継続的デプロイメントのために {{site.data.keyword.cloud_notm}} プラットフォームで継続的デプロイメント (CD) ツールチェーンを作成する際に Git を使用します。 GitHub アカウントをお持ちでない場合は、[GitHub](https://github.com/join) Web サイトで無料のパブリックな個人アカウントを作成してください。お持ちの場合は、既にご使用のアカウントでログインしてください。

なお、コマンド・ラインからの GitHub へのセキュアなアクセスのために、SSH 鍵を生成して [GitHub プロファイル](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)にアップロードする方法に関する重要なステップバイステップの手順があることにご留意ください。 ただし、ここでそれを実行しても、良い練習をしただけになります。後でアクセスする {{site.data.keyword.cloud_notm}} プラットフォームで使用する GitHub のインスタンスに対してこのステップを繰り返す必要があるためです。SSH 鍵を使用するステップは複雑な場合がありますが、実践を積んでいくことで、SSH を CLI で簡単に使用できるようになります。

今は、[Github Desktop](https://desktop.github.com/) ページにアクセスして GitHub Desktop をダウンロードし、インストーラーを実行します。 インストーラーが終了すると、アカウントを使用して GitHub にログインするよう求められます。

「Log in」ウィンドウ (このチュートリアルの最初の図を参照) で、リポジトリーに対するコミットで公開する名前と E メールを入力します (パブリック・アカウントを持っていることが前提です)。 アプリケーションをアカウントにリンクすると、GitHub アカウントを介してオンラインでアプリケーション接続を検証するように求められる場合があります。

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

まだリポジトリーを作成する必要はありません。 GitHub Desktop に「Tutorial」という名前のリポジトリーがある場合は、操作に慣れるためにそれを試してみても構いません。 これで、このチュートリアルの前提条件部分が完了しました。 アプリを作成する準備はできましたか?

## 開発者ツールを使用した Node.js スターター・アプリの作成
{: #tutorial-create-skeleton}

アプリケーションの開発をローカルで開始するには、例 4 に示されているように、まずコマンド・ラインから直接 {{site.data.keyword.cloud_notm}} プラットフォームにログインします。 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="例 4. CLI 開発者ツールを使用して IBM Cloud プラットフォームにログインするためのコマンド" caption-side="bottom"}
`例 4. CLI 開発者ツールを使用して IBM Cloud プラットフォームにログインするためのコマンド`

オプション -o で組織、オプション -s でスペース、または統合アカウントを使用する場合は --sso といったオプション・パラメーターを指定することもできます。 ログインすると、地域の選択を求められることがあります。この演習の目的のため、 地域として `us-south` を選択してください。このチュートリアルでは、後から CD ツールチェーンを作成する際に、これと同じオプションを使用します。  

次に、例 5 に示されているコマンドを使用して、エンドポイントを設定します (まだ設定されていない場合)。 他のエンドポイントを指定することも可能であり、実動での使用ではその方がよい場合もありますが、ここでは、アカウントにとって適切であれば、示されている通りのコードを使用してください。

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="例 5. アカウントの API エンドポイントを設定するコマンド" caption-side="bottom"}
`例 5. アカウントの API エンドポイントを設定するコマンド`

例 6 に示されている、target コマンドと --cf オプションを使用するコードを使用して、{{site.data.keyword.cloud_notm}} プラットフォームの Cloud Foundry (cf) 部分をターゲットにします。 `cf` API は、CLI 開発者ツール内に組み込まれています。

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="例 6. Cloud Foundry API を使用するためのオプションの設定" caption-side="bottom"}
`例 6. Cloud Foundry API を使用するためのオプションの設定`

ようやく、このチュートリアルの目的である Web アプリケーションの作成が、例 7 に示されているコードで始まります。`dev` スペースは、組織のデフォルト・オプションですが、「財務」と「開発」を別々にするなど、さまざまな作業を分けるために他のスペースを作成することもできます。

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="例 7. IBM Cloud 開発者ツールを使用してアプリを作成するためのコマンド" caption-side="bottom"}
`例 7. IBM Cloud 開発者ツールを使用してアプリを作成するためのコマンド`

このコマンドを使用すると、一連の質問が表示されます。 このプロセスでは多くの時点で戻ることができますが、よくわからなくなったように感じる場合や、ステップを飛ばしたように思える場合は、ディレクトリーを削除するか、テストや調査のために別のディレクトリーを作成して、最初からやり直しても構いません。 また、コマンド・ラインでローカルにアプリケーションを作成するプロセスを完了すると、作成したリソースを管理するための、アカウントを作成した {{site.data.keyword.cloud_notm}} オンライン・ポータルで、後から結果をオンラインで確認できます。

例 8 では、対象とする「Web App」を作成するオプションを確認します。 「2」と入力し、Enter キーを押します。

```
                                        
--------------------------------------------------------------------------------
Select an application type:
--------------------------------------------------------------------------------
 1. Blank App
 2. Backend Service / Web App
 3. Mobile App
--------------------------------------------------------------------------------
 0. Exit
--------------------------------------------------------------------------------
? Enter selection number:> 2


```
{: caption="例 8. コマンド `ibmcloud dev create` の出力 (「Web App」のオプション「2」を選択)" caption-side="bottom"}
`例 8. コマンド「ibmcloud dev create」の出力 (「Web App」のオプション「2」を選択)`

例 9 では、いわゆる「ビルドパック」に基づくさまざまなオプションがあります。「Node」を使用するためのオプションを確認します。 「4」と入力し、Enter キーを押します。

```

--------------------------------------------------------------------------------
Select a language:
--------------------------------------------------------------------------------
 1. Go
 2. Java - MicroProfile / Java EE
 3. Java - Spring
 4. Node
 5. Python - Django
 6. Python - Flask
 7. Scala
 8. Swift
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 4


```
{: caption="例 9. `ibmcloud dev create` の言語オプション (続き)" caption-side="bottom"}
`例 9. 「ibmcloud dev create」の言語オプション (続き)`

プログラミング言語またはフレームワーク (あるいはその両方) を選択すると、例 10 に示されているように次の選択項目では多数のオプションが表示され、目的のサービスを超えてスクロールされてしまう場合があります。 例に示されているように、ここではシンプルな「Node.js Web App with Express.js」を使用します。 「6」と入力し、Enter キーを押します。

```
? Select a Starter Kit:

--------------------------------------------------------------------------------
APPSERVICE
--------------------------------------------------------------------------------
 1. MEAN Stack: MongoDb, Express.js, Angular, Node.js - A starter 
    project for setting up a mongodb, express, angular and node application
 2. MERN Stack: MongoDb, Express.js, React, Node.js - A starter 
    project for setting up a mongodb, express, react and node application
 3. Node.js BFF Example with Express.js - A starter for building 
    backend-for-frontend APIs in Node.js, using the Express.js framework.
 4. Node.js Example Serverless App - A starter providing a set of 
    Cloud Functions and API for a serverless backend that uses Cloudant NoSQL 
    database.
 5. Node.js Microservice with Express.js - A starter for building a 
    microservice backend in Node.js, using the Express.js framework.
 6. Node.js Web App with Express.js - A starter that provides a basic 
    web serving application in Node.js, using the Express.js framework.
 7. Node.js Web App with Express.js and React - A starter that 
    provides a rich React frontend delivered from a Node.js application, 
    including key web development tools Gulp, SaaS, and Webpack, using the 
    Express.js framework.

--------------------------------------------------------------------------------
FINANCE
--------------------------------------------------------------------------------
 8. Wealth Management Chatbot - A chatbot that allows the user to 
    query the status of their investments and evaluate the impact of different 
    market scenarios on their investment portfolio. It can easily be extended 
    in several ways.

--------------------------------------------------------------------------------
WATSON
--------------------------------------------------------------------------------
 9. Watson Assistant Basic - Simple application that demonstrates the 
    Watson Assistant service in a chat interface simulating banking tasks.
10. Watson Natural Language Understanding Basic - Collection of APIs 
    that can analyze text to help you understand its concepts, entities, 
    keywords, sentiment, and can create a custom model for some APIs to get 
    specific results that are tailored to your domain.
11. Watson News Intelligence - This starter kit demonstrates how to 
    query news content to understand what people are saying or feeling about 
    important topics.
12. Watson Speech to Text Basic - Basic sample of Speech to Text 
    service to convert speech in multiple languages into text.
13. Watson Text to Speech Basic - Basic sample of how to use Text to 
    Speech for streaming, low latency, synthesis of audio from text.
14. Watson Visual Recognition Basic - Use deep learning algorithms to 
    analyze images that can give you insights into your visual content.
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 6

```
{: caption="例 10. `ibmcloud dev create` のスケルトン・アプリケーション・オプション" caption-side="bottom"}
`例 10. 「ibmcloud dev create」のスケルトン・アプリケーション・オプション`

これで、簡単なオプションの選択が終わりました。引き続き、あらゆる場面で開発者にとって最も難しい、アプリに名前を付けるというオプションを実行する必要があります。 例 11 に示されている例に従って「webapplication」と入力し、Enter キーを押してください。

```bash
? Enter a name for your application> webapplication
```
{: caption="例 11. `ibmcloud dev create` を使用したアプリケーション名「webapplication」の設定" caption-side="bottom"}
`例 11. 「ibmcloud dev create」を使用したアプリケーション名「webapplication」の設定`

必要に応じて、データ・ストアや計算機能などのサービスを、Web コンソールで後からいくつでも追加できます。 しかし、例 12 に示されているように、サービスを追加するかどうかを尋ねられたら、現時点では「いいえ」の意味で「n」と入力してください。

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="例 12. `ibmcloud dev create` を使用した場合のサービス追加のオプション (続き)" caption-side="bottom"}
`例 12. 「ibmcloud dev create」を使用した場合のサービス追加のオプション (続き)`

初めの方で、Docker に関連して、従来のサーバー・インスタンスや仮想サーバーの代わりにコンテナーを使用した開発の利点を述べました。 コンテナーを管理する 1 つの方法として、開発において_デファクト _・スタンダードになっている Kubernetes などのオーケストレーション・ソフトウェアを使用するという方法があります。 しかし、このチュートリアルでは、アプリが必要とするコード、ライブラリー、および構成を含む単一の Docker コンテナーを Cloud Foundry サービスで管理します。

例 13 に示されているように、プロジェクト・ライフサイクル内で CD を統合するという目的で「IBM DevOps」を使用するために「1」と入力して、Enter キーを押します。
 
```

--------------------------------------------------------------------------------
Select from the following DevOps toolchain and target runtime environment 
options:
 1. IBM DevOps, deploy to Cloud Foundry buildpacks
 2. IBM DevOps, deploy to Kubernetes containers
 3. No DevOps, with manual deployment
--------------------------------------------------------------------------------
? Enter selection number:> 1

```
{: caption="例 13. `ibmcloud dev create` のデプロイメント・オプション" caption-side="bottom"}
`例 13. 「ibmcloud dev create」のデプロイメント・オプション`

前述したように、ここでデプロイメントの自動化 CD ツールチェーンの地域を選択します。例 14 に示されているように、前に選択したオプションと同じ「5」を選択します。

```

--------------------------------------------------------------------------------
Select a region for your toolchain from the following options:
--------------------------------------------------------------------------------
 1. eu-de (Frankfurt)
 2. eu-gb (London)
 3. jp-tok
 4. us-east (Washington DC)
 5. us-south (Dallas)
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 5

```
{: caption="例 14. `ibmcloud dev create` のオプションとして選択可能な地域" caption-side="bottom"}
`例 14. 「ibmcloud dev create」のオプションとして選択可能な地域`

この時点で、新しいアプリケーションを生成すると、例 15 に示されているように、後でアプリケーションをデプロイするために使用されるツールチェーンには、追加の構成が必要なことが通知されます。 前述したように、GitHub を使用してデプロイ済みアプリケーションを配信するには、公開鍵を GitHub ({{site.data.keyword.cloud_notm}} プラットフォーム上の CD ツールチェーン・インスタンスにある) にアップロードする必要があります。 アプリケーションをデプロイし、[README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair) で IBM Cloud GitLab アカウントにログインすると、追加の説明を確認することができます。

```

Note: For successful connection to the DevOps toolchain, this machine 
must be configured for SSH access to your IBM Cloud GitLab account at 
https://git.ng.bluemix.net/profile/keys in order to download the 
application code.


```
{: caption="例 15. `ibmcloud dev create` コマンド入力後の SSH 鍵に対して表示される注記" caption-side="bottom"}
`例 15. 「ibmcloud dev create」入力後の SSH 鍵に対して表示される注記`

さらにプロンプトが表示され、以前に定義したアプリケーションとツールチェーンの名前が確認されます。 例 16 には、必要に応じてホスト名およびツールチェーン名を変更する方法が示されています。 ホスト名は、アプリケーションのサービス・エンドポイントとして使用されるドメインで固有でなければなりませんが、競合がない場合は、確認を求められたときに Return キーを押すだけです。

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="例 16. `ibmcloud dev create` におけるプロパティーの名前の確認" caption-side="bottom"}
`例 16. 「ibmcloud dev create」におけるプロパティーの名前の確認`

`ibmcloud dev create` コマンドを使用した結果として表示された出力の最後にあるリンクをコピーして貼り付けると、CD ツールチェーンにアクセスできます。 ただし、リンクをキャプチャーしなかった場合でも、後でコンソールからアクセスすることもできます。
プロセスの続行に伴い、追加情報が続きます。例 17 に示されているように、アプリケーション・エントリーがオンラインで作成され、サンプル・コードを含むディレクトリーが作成されました。 

```
Cloning repository 
https://git.ng.bluemix.net/Organization.Name/webapplication...
Cloning into 'webapplication'...
remote: Counting objects: 60, done.
remote: Compressing objects: 100% (54/54), done.
remote: Total 60 (delta 4), reused 0 (delta 0)
Receiving objects: 100% (60/60), 50.04 KiB | 1.52 MiB/s, done.
Resolving deltas: 100% (4/4), done.
OK

The app, webapplication, has been successfully saved into the 
current directory.

```
{: caption="例 17. `ibmcloud dev create` によって生成されたアクションの確認" caption-side="bottom"}
`例 17. 「ibmcloud dev create」によって生成されたアクションの確認`

例 17 の最後のステートメントは、現行ディレクトリーを表示すると、新しいサブディレクトリー `webapplication` が表示されるようになったことを意味しています。 `webapplication` ディレクトリー内には、新しい Node.js アプリケーションのスキャフォールドが含まれています。 しかし、レシピは存在しているかもしれませんが、まだ Docker イメージ内にラップされている構成要素自体を、例 18 のコマンドを使用して「ビルド」する必要があります。 Docker は、インストールの結果としてローカル・マシンで実行されていなければなりませんが、必要であれば再始動してください。 Docker を実行せずに新しい Web アプリケーションをビルドしようとすると失敗しますが、それは考えられる唯一の理由ではありません。 問題がある場合は、結果として生成されたエラー・メッセージを確認してください。エラー・メッセージには、{{site.data.keyword.cloud_notm}} プラットフォーム・アカウントのオンライン・ポータルで結果ログを表示するための該当するリンクが含まれていることがあります。

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="例 18. {{site.data.keyword.cloud_notm}} プラットフォームの build コマンド" caption-side="bottom"}
`例 18. IBM Cloud プラットフォームの build コマンド`

アプリのビルドは、配信目的の他に、これによって、(例 19 のコマンドをコピーして貼り付けるか、入力して) `run` コマンドで同じコードをローカルで実行できるようになります。 完了したら、提供された URL をコピーしてブラウザーのアドレス・バーに貼り付けます。 通常は 、<http://localhost:3000> です。

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="例 19. アプリを実行するための {{site.data.keyword.cloud_notm}} プラットフォーム CLI コマンド" caption-side="bottom"}

これで、アプリが作成され、定義されました。アプリケーションを表示して、動作することを確認してください。 図 2 に示されているようなプレースホルダー・イメージが表示されていれば成功です。 新しい Node.js Web アプリケーションが作成され、クラウドにデプロイする準備ができました。

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="図 2. 新しい Node.js アプリケーション: 完了"}

アプリを (例 20 に示されているように) deploy コマンドで {{site.data.keyword.cloud_notm}} プラットフォームにデプロイします。

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="例 20. アプリのアップロードとデプロイを行うための {{site.data.keyword.cloud_notm}} プラットフォーム CLI コマンド" caption-side="bottom"}
`例 20. アプリのアップロードとデプロイを行うための IBM Cloud プラットフォーム CLI コマンド`

前に指定した地域エンドポイントとホスト名に基づき、コマンド `ibmcloud dev deploy` を実行した結果として、URL が再度表示されます。 問題がある場合は、{{site.data.keyword.cloud_notm}} プラットフォームのポータルに保管されているログへのリンクが表示されることがあります。 問題がなければ、先ほどアクセスしたローカル・アプリケーションと同一の画面がブラウザーに表示されます。 では、クラウド内の新しい Web アプリケーションにアクセスしてみましょう。

## サンプル・アプリケーションを使用した Web Gallery アプリの作成
{: #tutorial-create-app}

{{site.data.keyword.cloud_notm}} プラットフォームで Node.js アプリを開発するために必要な前提条件を思い出してみましょう。 {{site.data.keyword.cloud_notm}} プラットフォーム・アカウントを作成して、開発者ツールをインストールし、それにより Docker もインストールされました。 次に、Node.js をインストールしました。 このチュートリアルの前提条件としてリストされていた最後の項目は Git です。これから詳しく説明していきます。  

まずは、Node.js でのイメージ・ギャラリーの作業の詳細から始めます。 今回、このシナリオでは GitHub Desktop を使用しますが、Git コマンド・ライン・クライアントを使用して同じタスクを実行することもできます。 始めに、新しい Web アプリケーションのスターター・テンプレートを複製しましょう。 

以下の手順に従います。

1.  例 21 にリストされているリポジトリーを複製します。 Git を使用して、ローカル開発環境にアプリのテンプレートをダウンロードします。 {{site.data.keyword.cloud_notm}} プラットフォームからサンプル・アプリを複製するのではなく、例 21 のコマンドを使用して、{{site.data.keyword.cos_full_notm}} Web Gallery アプリのスターター・テンプレートを複製します。 リポジトリーが複製されると、スターター・アプリは COS-WebGalleryStart ディレクトリーに含まれています。 Git CMD ウィンドウを開き、GitHub リポジトリーを複製するディレクトリーに移動します。 このチュートリアルの最初の例に示されているコマンドを使用します。

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="例 21. Git clone コマンドの詳細" caption-side="bottom"}
`例 21. Git clone コマンドの詳細`

2.  アプリをローカルで実行します。CLI を提供する端末アプリケーションを開き、作業ディレクトリーを COS-WebGalleryStart ディレクトリーに変更します。 package.json ファイルにリストされている Node.js 依存関係を確認してください。 次の例 22 に示されているコマンドを使用して、それらを所定の場所にダウンロードします。

```bash
npm install
```
{: codeblock}
{: caption="例 22. Node Package Manager (npm) のインストール" caption-side="bottom"}
`例 22. Node Package Manager (npm) のインストール`

3.  例 23 に示されているコマンドを使用して、アプリを実行します。

```bash
npm start
```
{: codeblock}
{: caption="例 23. npm を使用したアプリの開始に関する詳細" caption-side="bottom"}
`例 23. npm を使用したアプリの開始に関する詳細`

ブラウザーを開き、コンソールに出力されるアドレスとポート (<http://localhost:3000>) でアプリを表示します。

**ヒント**: アプリをローカルで再始動するには、ノード・プロセスを強制終了 (Ctrl+C) して停止し、`npm start` を再度使用します。 ただし、新機能の開発時には、変更の検出時にアプリを再始動する nodemon を使用すると、時間の節約になります。 nodemon は `npm install -g nodemon` のように指定して、グローバルにインストールします。 この後、`nodemon` を使用してアプリのディレクトリーでコマンド・ラインから nodemon を実行し、「nodemon」がアプリを開始するようにします。

4.  アプリをデプロイメントする準備をします。 必要に応じて、例 24 に示されているように、COS-WebGallery にある `manifest.yml` ファイル内のアプリケーション名のプロパティー値を、{{site.data.keyword.cloud_notm}} プラットフォームで入力したアプリの名前およびその他の情報に更新します。 アプリケーションの `manifest.yml` は、以下の例のようになります。 さらに、アプリのルート・ディレクトリーにある `package.json` ファイルを、アプリの名前と作成者の名前でカスタマイズできます。

```yaml
applications:
- path: .
  memory: 256M
  instances: 1
  domain: us-south.cf.appdomain.cloud
  name: webapplication
  host: webapplication
  disk_quota: 1024M
  random-route: true
```
{: codeblock}
{: caption="例 24. `manifest.yml` の内容" caption-side="bottom"}
`例 24. manifest.yml の内容`

**ヒント**: ここで、リモート・オリジンにコードを対話式にプッシュするために SSH 鍵をセットアップしなければならない場合があります。  
SSH 鍵のパスフレーズを設定した場合、リポジトリーのリモート・オリジンに変更をプッシュするたびに、このコードを入力する必要があります。 

5.  `webapplication` ディレクトリーの内容を削除し、変更したディレクトリー `COS-WebGalleryStart` の内容で置き換えます。
    ご自身のきめ細やかな Git スキルを使用して、削除されてリポジトリーに追加されたファイルを CLI または GitHub Desktop のいずれかで追加します。 次に、変更をリポジトリー・オリジンにプッシュします。 今後は、変更を Git にプッシュするだけで、クラウド・ベースの Web アプリケーションに変更を加えることができます。 CD ツールチェーンは、変更を複製してそれらをサーバーに退避させた後、サーバー・プロセスを自動的に再始動します。 


基本的に、アプリケーションを再コード化したため、ビルド・プロセスを繰り返しましょう。 しかし今回は、新しいイメージ・ギャラリー・コードを使用します。 

###{{site.data.keyword.cloud_notm}} プラットフォームへのアプリのデプロイ### 

変更を含むスターター・アプリを
{{site.data.keyword.cloud_notm}}プラットフォームに入れるには、開発者ツールを使用して前に実行した同じステップを繰り返すことでデプロイします。

a.  まだログインしていない場合、または再始動やログアウトをした場合は、login コマンドを使用して {{site.data.keyword.cloud_notm}} プラットフォームにログインします。 リマインダーとして、例 25 にコマンドを示しています。なお、オプション -o で組織、オプション -s でスペース、または統合アカウントを使用する場合は --sso といったオプション・パラメーターを指定できます。 地域を尋ねられた場合は、これまでと同じ地域を必ず選択してください。

```bash
ibmcloud login
```
{: codeblock}
{: caption="例 25. {{site.data.keyword.cloud_notm}} プラットフォームにログインするための CLI コマンド" caption-side="bottom"}
`例 25. IBM Cloud プラットフォームにログインするための CLI コマンド`

b.  地域の API エンドポイントを (例 6 のオプションのプレースホルダーで示すように) api コマンドを使用して設定します。 地域の API エンドポイント URL が分からない場合は、「始めに」のページを参照してください。

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="例 26. {{site.data.keyword.cloud_notm}} プラットフォーム API エンドポイント" caption-side="bottom"}
`例 26. IBM Cloud プラットフォーム API エンドポイント`

c.  例 27 に示されている、target コマンドと --cf オプションを使用するコードを使用して、{{site.data.keyword.cloud_notm}} プラットフォームの Cloud Foundry 部分をターゲットにします。 


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="例 27. Cloud Foundry をターゲットとする {{site.data.keyword.cloud_notm}} プラットフォーム CLI" caption-side="bottom"}
`例 27. Cloud Foundry をターゲットとする IBM Cloud プラットフォーム CLI`

d.  アプリケーションを配信するために、(例 28 のように) build コマンドを使用してアプリをビルドします。

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="例 28. {{site.data.keyword.cloud_notm}} プラットフォームの build コマンド" caption-side="bottom"}
`例 28. IBM Cloud プラットフォームの build コマンド`

g.  アプリケーションをローカルでテストしてみましょう。 アプリのビルドは、配信目的の他に、これによって、(例 29 のコマンドを入力して) run コマンドで同じコードをローカルで実行できるようになります。


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="例 29. アプリを実行するための {{site.data.keyword.cloud_notm}} プラットフォーム CLI コマンド" caption-side="bottom"}
`例 29. アプリを実行するための IBM Cloud Platform CLI コマンド`

h.  アプリを (例 30 に示されているように) deploy コマンドで {{site.data.keyword.cloud_notm}} プラットフォームにデプロイします。

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="例 30. アップロードとデプロイを行うための {{site.data.keyword.cloud_notm}} プラットフォーム CLI コマンド" caption-side="bottom"}
`例 30. アップロードとデプロイを行うための IBM Cloud プラットフォーム CLI コマンド`

例 31 のコードは、初期 Web アプリケーションのビルド、テスト、およびデプロイを行うためにこの例で使用される一連のコマンドを示しています。

```bash
ibmcloud login --sso
ibmcloud api cloud.ibm.com
ibmcloud target --cf
ibmcloud dev enable
ibmcloud dev build
ibmcloud dev run
ibmcloud dev deploy
```
{: codeblock}
{: caption="例 31. {{site.data.keyword.cloud_notm}} プラットフォーム CLI コマンド・リスト" caption-side="bottom"}
`例 31. IBM Cloud プラットフォーム CLI コマンド・リスト`

成功すると、{{site.data.keyword.cloud_notm}} プラットフォームは、アプリがアップロードされ、正常にデプロイされ、開始されたことを報告します。 {{site.data.keyword.cloud_notm}} プラットフォーム Web コンソールにもログインしている場合は、そこでもアプリの状況が通知されます。 しかし、最も重要なことは、{{site.data.keyword.cloud_notm}} プラットフォームによって報告されたアプリの URL にブラウザーでアクセスするか、 Web コンソールから「アプリの表示」ボタンをクリックして、アプリがデプロイされたことを確認できるということです。

5.  アプリをテストします。 作成時にデプロイされたデフォルトのアプリ・テンプレートから、以下に示すスターター・アプリへ変化していることが見てわかれば、{{site.data.keyword.cloud_notm}} プラットフォームへのアプリのデプロイが成功したことを示しています。

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### Git ブランチの作成
{: #tutorial-create-branch}

ここで、{{site.data.keyword.cloud_notm}} プラットフォーム Delivery Pipeline のビルド・ステージに使用するローカル開発環境のブランチを作成する必要があります。

1.  GitHub Desktop を使用している場合は、ブランチ・アイコンをクリックします。ブランチの名前の入力を求めるプロンプトが出されます (図 14 を参照)。 この例では、Local-dev を名前として使用しています。

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  ブランチを作成すると、GitHub は Local-dev ブランチ上のローカル・ファイルとマスター・ブランチ上のリポジトリー内のファイルを比較し、ローカルでの変更がないことを報告します。 これで、(図 5 に示されているように) 「Publish」をクリックして、ローカル・リポジトリーに作成したブランチを GitHub リポジトリーに追加できます。

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

Local-dev ブランチがツールチェーン内の GitHub リポジトリーにパブリッシュされると、{{site.data.keyword.cloud_notm}} プラットフォーム Delivery Pipeline のビルド・ステージがトリガーされ、その後、GitHub リポジトリーにコミットをプッシュするとデプロイ・ステージに進みます。
デプロイメントはワークフローに直接統合されているため、CLI からアプリをデプロイする必要はなくなります。

### {{site.data.keyword.cos_full_notm}} のストレージ資格情報のセットアップ
{: #tutorial-credentials}

Web アプリケーションの {{site.data.keyword.cos_short}} 資格情報と、イメージの保管および取得を行う「バケット」を構成する必要があります。 作成する API キーには、[サービス資格情報](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials)で定義されている {{site.data.keyword.cos_short}} HMAC 資格情報が必要です。
AWS アカウントを持っていれば、`access_key_id` と `secret_access_key` という用語がわかるかもしれません。その場合は、既に `aws_access_key_id` 項目と `aws_secret_access_key` 項目を含む資格情報ファイルを使用することができます。 

API キーの作成が完了し、ダウンロードし、HMAC 資格情報をコピーしたら、以下のステップを実行します。

1.  ローカル開発環境で、資格情報を Windows パス `%USERPROFILE%\\.aws\\credentials` に入れます (Mac/Linux ユーザーの場合、資格情報は `~/.aws/credentials` に入れる必要があります)。 例 32 は、標準的な資格情報ファイルの内容を示しています。

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="例 32. `~/.aws/credentials` ファイルに定義されている資格情報" caption-side="bottom"}
`例 32. ~/.aws/credentials ファイルに定義されている資格情報`

2.  {{site.data.keyword.cloud_notm}} プラットフォームで CLI コマンドを使用して作成したアプリケーションの Web ページで、{{site.data.keyword.cloud_notm}} プラットフォームにログインし、開発のベスト・プラクティスによって、必要な資格情報を環境変数として定義して、Cloud Foundry アプリの下で「webapplication」アプリを選択します。 タブで「ランタイム」をクリックします。

3.  「ランタイム」ウィンドウで、ページ上部の「環境変数」をクリックし、「ユーザー定義」セクションまでスクロールします。ここで、変数を追加できます。

4.  2 つの変数を追加します。 1 つはキーの名前として `AWS_ACCESS_KEY_ID` を使用して access_key_id の値を追加します。もう 1 つは `AWS_SECRET_ACCESS_KEY` という名前の秘密アクセス・キーの値を追加します。
    これらの変数とそれぞれの値は、{{site.data.keyword.cloud_notm}} プラットフォームでの実行時に、アプリが {{site.data.keyword.cos_short}} インスタンスに対する認証に使用するものです (図 6 を参照)。 項目の追加が終わった後に「保存」をクリックすると、{{site.data.keyword.cloud_notm}} プラットフォームが自動的にアプリを再始動します。

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

次に、サービス・インスタンスの {{site.data.keyword.cos_short}} ポータルで、イメージを格納するバケットを追加します。 このシナリオでは、`web-images` という名前のバケットを使用します。


## Node.js {{site.data.keyword.cos_full_notm}} イメージ・ギャラリー Web アプリケーションのカスタマイズ
{: #tutorial-develop}

この例では MVC アーキテクチャーを使用しているため、このアーキテクチャーを反映するようにプロジェクト内のディレクトリー構造を調整することは、利便性があるだけでなく、最良の方法でもあります。
このディレクトリー構造には、EJS ビュー・テンプレートを入れる「views」ディレクトリー、express ルートを入れる「routes」ディレクトリー、controller ロジックを入れる場所としての「controllers」ディレクトリーがあります。 これらの項目を、「src」という名前の親ソース・ディレクトリーの下に配置します (図 7 を参照)。

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**ヒント**: 以前に複製したリポジトリーには COS-WebGalleryEnd という名前のディレクトリーが含まれています。 お好みのエディターで、完了したアプリケーションのソース・コードを表示すると、次のステップを実行する際に役立つことがあります。 これは、このチュートリアルを完了した際にコミットされ、{{site.data.keyword.cloud_notm}} プラットフォームにデプロイされる「webapplication」のバージョンになります。

### アプリの設計
{: #tutorial-develop-design}

以下は、シンプルなイメージ・ギャラリー Web アプリケーションでユーザーが実行できなければならない 2 つのメインタスクです。

  - Web ブラウザーから {{site.data.keyword.cos_short}} バケットにイメージをアップロードする。
  - {{site.data.keyword.cos_short}} バケット内のイメージを Web ブラウザーで表示する。

次のステップでは、完全に開発された実動グレードのアプリを構築するのではなく、これら 2 つのデモンストレーション機能を完成させる方法に焦点を当てます。 このチュートリアルをデプロイし、公開した状態で実行すると、このアプリを見つけたあらゆるユーザーが、{{site.data.keyword.cos_full_notm}} バケットにファイルをアップロードしたり、既にバケット内に存在する JPEG イメージをブラウザーで表示したりという、同じアクションを実行できることになります。

### アプリの開発
{: #tutorial-develop-app}

`package.json` ファイルの scripts オブジェクト内に、「start」の定義方法が示されています (例 33)。 このファイルは、アプリが開始されるたびに app.js を実行するようノードに指示するために {{site.data.keyword.cloud_notm}} プラットフォームが使用するものです。 また、ローカルでアプリをテストするときにも使用します。 app.js というメイン・アプリケーション・ファイルを見てみましょう。 これは、`npm start` コマンド (または nodemon) を使用してアプリを開始するときに、Node.js に最初に処理するよう指示したコードです。 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="例 33. アプリへのカスタム・コードをブートストラップする方法の指示" caption-side="bottom"}
`例 33. アプリへのカスタム・コードをブートストラップする方法の指示`

app.js ファイルは、例 34 に示されているコードで始まります。
最初に、コードはノードを使用して、開始に必要なモジュールをロードします。
Express フレームワークは、単に `app` という名前のシングルトンとしてアプリを作成します。
この例は、割り当てられた環境プロパティーであるポート (デフォルトの 3000) で listen するようにアプリに指示して終わっています (ここではコードの大部分を省略しています)。
開始時に正常に起動すると、サーバー URL を含むメッセージがコンソールに出力されます。

```javascript
var express = require('express');
var cfenv = require('cfenv');
var bodyParser = require('body-parser');
var app = express();
//...

// start server on the specified port and binding host
var port = process.env.PORT || 3000;
app.listen(port, function() {
    console.log("To view your app, open this link in your browser: http://localhost:" + port);
});
//...
```
{: codeblock}
{: javascript}
{: caption="例 34. Web アプリケーションの単純で強力な開始" caption-side="bottom"}
`例 34. Web アプリケーションの単純で強力な開始`

例 35 で、パスとビューの定義方法を見てみましょう。 コードの最初の行は、公開ディレクトリーを使用して静的ファイルを提供するように Express フレームワークに指示しています。静的ファイルには、使用する静的イメージとスタイル・シートが含まれます。 それに続く行では、ビューのテンプレートが src/views ディレクトリーにあることをアプリに指示し、ビュー・エンジンを EJS に設定しています。 さらに、フレームワークは body-parser ミドルウェアを使用して、着信要求データを JSON としてアプリに公開します。 この例の最終行で、express アプリは、index.ejs ビュー・テンプレートをレンダリングして、アプリ URL へのすべての着信 GET 要求に応答します。

```javascript
//...
// serve the files out of ./public as our main files
app.use(express.static('public'));
app.set('views', './src/views');
app.set('view engine', 'ejs');
app.use(bodyParser.json());

var title = 'COS Image Gallery Web Application';
// Serve index.ejs
app.get('/', function (req, res) {
  res.render('index', {status: '', title: title});
});

//...
```
{: codeblock}
{: javascript}
{: caption="例 35. Web アプリのビューおよびテンプレートの場所" caption-side="bottom"}
`例 35. Web アプリのビューおよびテンプレートの場所`

以下の図は、レンダリングされてブラウザーに送信されたときのインデックス・ビュー・テンプレートを示しています。 `nodemon` を使用している場合は、変更を保存したときにブラウザーが最新表示されたことに気付く場合があります。アプリは、図 8 のように表示されるはずです。

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

例 36 では、ビュー・テンプレートは &lt;head&gt;...&lt;/ head&gt; タグ間で HTML コードを共有するため、それを別のインクルード・テンプレートに配置しました (例 16 を参照)。 このテンプレート (head-inc.ejs) には、1 行目にページ・タイトル用のスクリプトレット (JavaScript 変数のバインディング) が含まれています。
`title` 変数は `app.js` で設定され、その下の行でビュー・テンプレートのデータとして渡されます。 そうでない場合は、単純にいくつかの CDN アドレスを使用して Bootstrap CSS、Bootstrap JavaScript、および JQuery をプルします。 最後に、pubic/style sheets ディレクトリーからカスタム静的 styles.css ファイルを追加します。

```html
<title><%=title%></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
      integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
      crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
        crossorigin="anonymous">
</script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous">
</script>

<link rel="stylesheet" href="stylesheets/style.css">

```
{: codeblock}
{: caption="例 36. head-inc.ejs の HTML 要素" caption-side="bottom"}
`例 36. head-inc.ejs の HTML 要素`

インデックス・ビューの本文には、ブートストラップ・スタイルのナビゲーション・タブ (例 37 を参照) と、ブートストラップに含まれている CSS スタイルによって提供される基本レイアウトのアップロード・フォームが含まれています。

アプリの以下の 2 つの仕様について考えてみます。

-   24 行目で、フォームのメソッドを POST に、form-data エンコード・タイプを multipart/form-data に設定しています。 フォームのアクションとして、フォームのデータをアプリのルート「/」に送信します。 そのルートに対する POST 要求を処理するために、後から router ロジックで追加の作業を行います。

-   試行されたファイル・アップロードの状況に関するフィードバックをユーザーに表示します。 このフィードバックは、「status」という名前の変数でビューに渡され、アップロード・フォームの下部に表示されます。

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="/">ホーム</a></li>
    <li role="presentation"><a href="/gallery">Gallery</a></li>
</ul>
<div class="container">
    <h2>Upload Image to IBM Cloud Object Storage</h2>
    <div class="row">
        <div class="col-md-12">
            <div class="container" style="margin-top: 20px;">
                <div class="row">

                    <div class="col-lg-8 col-md-8 well">

                        <p class="wellText">Upload your JPG image file here</p>

                        <form method="post" enctype="multipart/form-data" action="/">
                            <p><input class="wellText" type="file" size="100px" name="img-file" /></p>
                            <br/>
                            <p><input class="btn btn-danger" type="submit" value="Upload" /></p>
                        </form>

                        <br/>
                        <span class="notice"><%=status%></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>
```
{: codeblock}
{: caption="例 37. index.ejs の HTML 要素" caption-side="bottom"}
`例 37. index.ejs の HTML 要素`

例 38 で、`app.js` に戻ってみましょう。 この例では、アプリに対して行われる追加の要求を処理するように Express ルートをセットアップします。 これらのルーティング・メソッドのコードは、プロジェクトの `./src/routes` ディレクトリーの下の 2 つのファイルに含まれています。

-   imageUploadRoutes.js: このファイルは、ユーザーがイメージを選択して「アップロード」をクリックした場合の処理を行います。

-   galleryRoutes.js: このファイルは、ユーザーが「Gallery」タブをクリックして imageGallery ビューを要求した場合の処理を行います。

```javascript
//...
var imageUploadRoutes = require('./src/routes/imageUploadRoutes')(title);
var galleryRouter = require('./src/routes/galleryRoutes')(title);

app.use('/gallery', galleryRouter);
app.use('/', imageUploadRoutes);

//...
```
{: codeblock}
{: javascript}
{: caption="例 38. Node Express router の例" caption-side="bottom"}
`例 38. Node Express router の例`

#### イメージのアップロード
{: #tutorial-develop-image-upload}

例 39 にある「imageUploadRoutes.js」のコードを見てみましょう。 最初に、新しい Express router のインスタンスを作成し、`imageUploadRoutes` という名前を付けなければなりません。
その後で、`imageUploadRoutes` を返す関数を作成し、それを `router` という変数に割り当てています。 完了したら、この関数をモジュールとしてエクスポートし、フレームワークおよび app.js 内のメイン・コードにアクセスできるようにする必要があります。
ルーティング・ロジックをアップロード・ロジックから分離するには、galleryController.js という名前の controller ファイルが必要です。 このロジックは、着信要求の処理および適切な応答の提供専用となっているため、そのロジックをこの関数に入れて、./src/controllers ディレクトリーに保存します。

Express フレームワークの Router のインスタンスでは、HTTP POST メソッドが使用された場合に、ルートのアプリ・ルート (「/」) への要求をルーティングするように imageUploadRoutes が設計されています。
imageUploadRoutes の `post` メソッド内では、galleryController によって `upload` として公開されている、`multer` モジュールおよび `multer-s3` モジュールのミドルウェアを使用しています。
ミドルウェアは、Upload フォーム POST からデータとファイルを取得して処理し、コールバック関数を実行します。 コールバック関数では、HTTP 状況コード 200 を受け取っていること、およびアップロードする要求オブジェクトに少なくとも 1 つのファイルがあることを確認します。 これらの条件に基づいて、`status` 変数にフィードバックを設定し、新しい状況でインデックス・ビュー・テンプレートをレンダリングします。

```javascript
var express = require('express');
var imageUploadRoutes = express.Router();
var status = '';

var router = function(title) {

    var galleryController =
        require('../controllers/galleryController')(title);

    imageUploadRoutes.route('/')
    	.post(
    		galleryController.upload.array('img-file', 1), function (req, res, next) {
                if (res.statusCode === 200 && req.files.length > 0) {
                    status = 'uploaded file successfully';
                }
                else {
                    status = 'upload failed';
                }
                res.render('index', {status: status, title: title});
            });

    return imageUploadRoutes;
};

module.exports = router;
```
{: codeblock}
{: javascript}
{: caption="例 39. Node express router の詳細" caption-side="bottom"}
`例 39. Node express router の詳細`

これに比べると、例 40 の galleryRouter のコードは、簡略化されたモデルになっています。 imageUploadRoutes に対して行ったものと同じパターンに従い、関数の最初の行に galleryController が必要です。その後で、ルートを設定しています。 主な違いは、POST ではなく HTTP GET 要求をルーティングしていることと、例の最後の方の行にある galleryController によって公開されている getGalleryImages からの応答ですべての出力を送信していることです。

```javascript
var express = require('express');
var galleryRouter = express.Router();

var router = function(title) {

    var galleryController =
        require('../controllers/galleryController')(title);

    galleryRouter.route('/')
        .get(galleryController.getGalleryImages);

    return galleryRouter;
};
module.exports = router;

```
{: codeblock}
{: javascript}
{: caption="例 40. Node express router の詳細" caption-side="bottom"}
`例 40. Node express router の詳細`

次に、ギャラリーの controller に注目しましょう。

例 41 で `multer` の upload がどのようにセットアップされているかを確認してください (この例では、現時点で不要なコードは省略されています)。 `ibm-cos-sdk`、`multer`、および `multer-s3` の各モジュールが必要です。 このコードは、{{site.data.keyword.cos_short}} サーバー・エンドポイントを指す S3 オブジェクトを構成する方法を示しています。 簡略化のために、エンドポイント・アドレス、地域、およびバケットなどの値を静的に設定していますが、それらは環境変数や JSON 構成ファイルから容易に参照できます。

唯一のプロパティーとして `storage` を指定して新規の `multer` インスタンスを作成することにより、例 41 で使用されているように、さらに、imageUploadRoutes で定義されているように `upload` を定義します。 このプロパティーは、multipart/form-data からファイルを送信する場所を `multer` に指示します。 {{site.data.keyword.cloud_notm}} プラットフォームは S3 API の実装を使用するため、storage を `s3-multer` オブジェクトに設定します。 この `s3-multer` オブジェクトには、以前に `s3` オブジェクトに割り当てた `s3` プロパティーと、「web-images」の値が割り当てられている `myBucket` 変数に割り当てた bucket プロパティーが含まれています。 これで、`s3-multer` オブジェクトには、アップロード・フォームからデータを受信した場合に、{{site.data.keyword.cos_short}} バケットに接続してファイルをアップロードするために必要なすべてのデータが含まれるようになりました。 アップロードされるオブジェクトの名前またはキーは、{{site.data.keyword.cos_short}} 「web-images」バケットに保管されるときにファイル・オブジェクトから取得される元のファイル名になります。 

**ヒント**: ファイル名の一意性を維持するために、ファイル名の一部としてタイム・スタンプを使用します。 

```javascript
var galleryController = function(title) {

    var aws = require('ibm-cos-sdk');
    var multer = require('multer');
    var multerS3 = require('multer-s3');
    
    var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
    var s3 = new aws.S3({endpoint: ep, region: 'us-south-1'});
    var myBucket = 'web-images';

    var upload = multer({
        storage: multerS3({
            s3: s3,
            bucket: myBucket,
            acl: 'public-read',
            metadata: function (req, file, cb) {
                cb(null, {fieldName: file.fieldname});
            },
            key: function (req, file, cb) {
                console.log(file);
                cb(null, file.originalname);
            }
        })
    });
    
    var getGalleryImages = function (req, res) { ... };

    return {
        getGalleryImages: getGalleryImages,
        upload: upload
    };
};

module.exports = galleryController;
```
{: codeblock}
{: javascript}
{: caption="例 41. Node express controller の詳細" caption-side="bottom"}
`例 41. Node express controller の詳細`

ローカルでテストをする場合、`console.log(file)` は、ファイル・オブジェクトをコンソールに出力する便利なタスクです。
ローカルでアップロード・フォームのテストを実行した際の、ファイルに関するコンソール・ログの出力を例 42 に示します。

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="例 42. デバッグ・オブジェクトのコンソール表示" caption-side="bottom"}
`例 42. デバッグ・オブジェクトのコンソール表示`

図 9 は、テストをした際にアプリケーションが実際に「ファイルのアップロードに成功した」ことを報告する、コールバックからのフィードバックです。

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### イメージの取得と表示
{: #tutorial-image-display}

ここで、app.js に戻りましょう。コード `app.use('/gallery', galleryRouter);` の行は、「/gallery」ルートが要求された場合にこのルーターを使用するように express フレームワークに指示するということを思い出してください。
このルーターが galleryController.js (例 43 のコードを参照) を使用することを思い出したら、先に示された getGalleryImages 関数を定義します。 イメージのアップロード関数でセットアップしたものと同じ `s3` オブジェクトを使用して、`listObjectsV2` という名前の関数を呼び出します。 この関数は、バケット内の各オブジェクトを定義するインデックス・データを返します。 イメージを HTML 内に表示するには、ビュー・テンプレートに表示するための `web-images` バケット内の JPEG イメージごとにイメージ URL が必要です。 `listObjectsV2` によって返されるデータ・オブジェクトのクロージャーには、バケット内の各オブジェクトに関するメタデータが含まれます。 

このコードは、「.jpg」で終わるオブジェクト・キーを検索して `bucketContents` 内をループし、S3 getSignedUrl 関数に渡すパラメーターを作成します。 この関数は、オブジェクトのバケット名とキーが指定されると、任意のオブジェクトの署名付き URL を返します。 コールバック関数では、各 URL を配列に保存し、それを `imageUrls` という名前のプロパティーの値として HTTP サーバー応答メソッド `res.render` に渡します。

```javascript
//...
    
    var getGalleryImages = function (req, res) {
        var params = {Bucket: myBucket};
        var imageUrlList = [];
        
        s3.listObjectsV2(params, function (err, data) {    
            if (data) {
                var bucketContents = data.Contents;
                for (var i = 0; i < bucketContents.length; i++) {
                    if (bucketContents[i].Key.search(/.jpg/i) > -1) {
                        var urlParams = {Bucket: myBucket, Key: bucketContents[i].Key};
                        s3.getSignedUrl('getObject', urlParams, function (err, url) {
                            imageUrlList.push(url);
                        });
                    }
                }
            }
            res.render('galleryView', {
                title: title,
                imageUrls: imageUrlList
            });
        });
    };

//...
```
{: codeblock}
{: javascript}
{: caption="例 43. galleryController.js の内容の一部" caption-side="bottom"}
`例 43. galleryController.js の内容の一部`

このチュートリアルの最後のコード例 44 は、イメージを表示するために必要なコードを含む galleryView テンプレートの本文を示しています。 res.render() メソッドから imageUrls 配列を取得し、/gallery ルートが要求された場合に、イメージ URL がイメージの GET 要求を行う、ネストした &lt;div&gt;&lt;/div&gt; タグのペアを反復するようになっています。

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="/">ホーム</a></li>
        <li role="presentation" class="active"><a href="/gallery">Gallery</a></li>
    </ul>
    <div class="container">
        <h2>IBM COS Image Gallery</h2>

        <div class="row">
            <% for (var i=0; i < imageUrls.length; i++) { %>
                <div class="col-md-4">
                    <div class="thumbnail">
                            <img src="<%=imageUrls[i]%>" alt="Lights" style="width:100%">
                    </div>
                </div>
            <% } %>
        </div>
    </div>
</body>

</html>
```
{: codeblock}
{: caption="例 44. ギャラリー・テンプレートで使用されるループおよび出力のスクリプトレット" caption-side="bottom"}
`例 44. ギャラリー・テンプレートで使用されるループおよび出力のスクリプトレット`

これを http://localhost:3000/gallery からローカルでテストした場合、図 10 のイメージが表示されます。

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### Git へのコミット
{: #tutorial-develop-commit}

アプリの基本的な機能が動作するようになったので、コードをローカル・リポジトリーにコミットし、その後で GitHub にプッシュします。 GitHub Desktop を使用して「Changes」をクリックし (図 11 を参照)、「Summary」フィールドに変更の要約を入力して、「Commit to Local-dev」をクリックします。 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

「Sync」をクリックすると、GitHub にパブリッシュしたリモートの Local-dev ブランチにコミットが送信されます。このチュートリアルの最後の図 12 で例示されているように、このアクションによって、Delivery Pipeline でビルド・ステージが開始され、デプロイ・ステージがそれに続きます。 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## 次のステップ
{: #nextsteps}

{{site.data.keyword.cloud_notm}} プラットフォームを使用して Web アプリケーション・イメージ・ギャラリーを作成するために、コースに沿って最初から最後まで進んできました。
この基本的な概要で取り上げた各概念は、[{{site.data.keyword.cloud_notm}} プラットフォーム](https://cloud.ibm.com/)でさらに探索することができます。 

これで終了です。
