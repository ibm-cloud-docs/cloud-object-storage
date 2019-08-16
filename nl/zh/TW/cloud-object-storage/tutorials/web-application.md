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

# 指導教學：Image Gallery Web 應用程式
{: #web-application}

從開始到結束，建置 Web 應用程式涵蓋了許多不同的概念，是向您介紹 {{site.data.keyword.cos_full}} 特性的好方法。本指導教學將顯示如何在 {{site.data.keyword.cloud}} Platform 上建置簡單的 Image Gallery，以及如何結合許多不同的概念與作法。您的應用程式將使用 {{site.data.keyword.cos_full_notm}} 作為 Node.js 應用程式的後端伺服器，容許使用者上傳及檢視 JPEG 影像檔。

## 開始之前
{: #wa-prereqs}

作為建置 Web 應用程式的必要條件，我們將從下列項目開始：

  - {{site.data.keyword.cloud_notm}} Platform 帳戶
  - Docker，作為 {{site.data.keyword.cloud_notm}} Developer Tools 的一部分
  - Node.js 
  - Git（Desktop 應用程式及指令行介面&mdash;CLI）

### 安裝 Docker
{: #tutorial-wa-install-docker}

從使用傳統伺服器實例或甚至虛擬伺服器來建置 Web 應用程式轉移為使用容器（例如 Docker），可加速開發、輕鬆測試，同時支援自動化部署。容器是一種不需要其他額外負擔的輕量型結構（例如作業系統），只需要您的程式碼及配置，就能透過相依關係來設定所有內容。

讓我們從開啟有經驗的開發人員所熟悉的工具，以及剛入門人員的最佳新朋友開始：指令行。自從發明圖形使用者介面 (GUI) 後，電腦的指令行介面已降級至次級狀態。但現在，是時候讓它回歸了（雖然 GUI 不會馬上消失；尤其是當我們需要瀏覽 Web 來下載新的指令行工具集時）。 

請繼續並開啟「終端機」，或針對您的作業系統開啟其他適當的「指令行介面」，並使用您所使用的特定 Shell 適用的指令來建立目錄。將您自己的參照目錄變更為剛剛建立的新目錄。建立時，您的應用程式將會在該目錄內具有其專屬的子目錄，內含開始進行所需的入門範本程式碼及配置。

離開指令行並回到瀏覽器，遵循鏈結上安裝 [{{site.data.keyword.cloud_notm}} Platform Developer Tools](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) 的指示。Developer Tools 提供可延伸且可重複的方法，以建置及部署雲端應用程式。

[Docker](https://www.docker.com) 會安裝為 Developer Tools 的一部分，而我們在支撐您新應用程式的常式內會需要它，即使它的工作大部分都在背景中進行。Docker 必須為執行中，建置指令才能運作。請繼續並在 [Dockerhub](https://hub.docker.com) 線上建立一個 Docker 帳戶，執行 Docker 應用程式，然後再登入。

### 安裝 Node.js
{: #tutorial-wa-install-node}

您將建置的應用程式使用 [Node.js](https://nodejs.org/) 作為伺服器端引擎，以執行此 Web 應用程式的 JavaScript 程式碼。為了能夠使用 Node 的內含 Node Package Manager (npm) 來管理應用程式的相依關係，您必須在本端安裝 Node.js。此外，在本端安裝 Node.js 可簡化測試，進而加速開發。 

開始之前，您可以考慮使用版本管理程式（例如 Node Version Manager 或 `nvm`）來安裝 Node，以減少管理多個版本的 Node.js 的複雜性。截至本文撰寫之時，若要在 Mac 或 Linux 機器上安裝或更新 `nvm`，您可以在剛剛開啟的 CLI 介面中利用 cURL 來使用安裝 Script，方法是將前兩個範例中的其中一個指令複製並貼入指令行，然後按 Enter 鍵（請注意，這假設您的 Shell 是 BASH，而不是替代方案）：

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="範例 1. 使用 cURL 安裝 Node Version Manager (nvm)" caption-side="bottom"}
`範例 1. 使用 cURL 安裝 Node Version Manager (nvm)`
   
...或使用 Wget（只需要一個，不同時使用兩者；請使用系統上的任何可用項目）：

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="範例 2. 使用 Wget 安裝 Node Version Manager (nvm)" caption-side="bottom"}
`範例 2. 使用 Wget 安裝 Node Version Manager (nvm)`

或者，若為 Windows，您可以搭配使用 [nvm for Windows](https://github.com/coreybutler/nvm-windows) 與安裝程式，以及鏈結上的原始碼。

如果您不想要增加支援多版次 Node.js 的複雜性，請造訪 [Node.js](https://nodejs.org/en/download/releases/) 網站，並安裝 Long Term Support (LTS) 版的 Node.js，其符合目前在 {{site.data.keyword.cloud_notm}} Platform 上使用的 SDK for Node.js 建置套件所支援的最新版本。在撰寫本文時，最新建置套件為 3.26 版，並且支援 Node.js 社群版本 6.17.0+ 版。 

您可以在 [SDK for Nodejs 最新更新](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates)頁面上找到最新 {{site.data.keyword.cloud_notm}} SDK for Node.js 建置套件的其他資訊。 

使用 `nvm`，您可以安裝的 Node 版本必須符合將範例 3 中的指令複製並貼入指令行的需求。

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="範例 3. 使用 `nvm` 安裝特定版本的 Node.js" caption-side="bottom"}
`範例 3. 使用 nvm 安裝特定版本的 Node.js`

不論您使用哪種方式，只要遵循指示，在您的電腦上安裝 Node.js 及 npm（Node 所隨附），就像您所使用的作業系統和策略一樣，那麼恭喜您，您的工作已有很好的開始！

### 安裝 Git
{: #tutorial-wa-install-git}

您可能已經熟悉 Git，因為它是建置 Web 應用程式的開發人員之間最廣泛使用的原始碼版本化系統。稍後，我們在 {{site.data.keyword.cloud_notm}} Platform 中建立 Continuous Deployment (CD) Toolchain 以進行持續交付及部署時，會使用 Git。如果您沒有 GitHub 帳戶，請在 [Github](https://github.com/join) 網站建立免費的公用個人帳戶；否則，請使用您所擁有的任何其他帳戶來登入。

請注意，對於從指令行安全存取 Github，有幾個重要的逐步指示，說明如何產生 SSH 金鑰並將其上傳至 [Github 設定檔](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)。不過，如果您現在這麼做，則只是一個很好的練習，因為您必須重複用於 {{site.data.keyword.cloud_notm}} Platform 之 Github 實例的步驟，而我們稍後會存取該 Platform。雖然使用 SSH 金鑰的步驟可能十分複雜，但是透過練習，您也可以在 CLI 上流暢的使用 SSH。

現在，請移至 [Github Desktop](https://desktop.github.com/) 頁面來下載 Github Desktop，然後執行安裝程式。安裝程式完成時，系統會提示您使用帳戶登入 GitHub。

在「登入」視窗中（請參閱本指導教學中的第一張圖），輸入您要公開顯示的名稱和電子郵件（假設您具有公用帳戶），以確定對儲存庫的所有提交。在將應用程式鏈結至您的帳戶後，系統可能會要求您在線上透過 Github 帳戶驗證應用程式連線。

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

您還不需要建立任何儲存庫。如果您注意到 GitHub Desktop 隨附的名為 Tutorial 的儲存庫，請隨意試用，以協助您熟悉作業。您剛剛已完成本指導教學的必要條件。準備好要建置應用程式了嗎？

## 使用 Developer Tools 建立 Node.js 入門範本應用程式 
{: #tutorial-create-skeleton}

若要在本端開始開發應用程式，請先從指令行直接登入 {{site.data.keyword.cloud_notm}} Platform，如範例 4 所示。 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="範例 4. 使用 CLI Developer Tools 登入 IBM Cloud Platfirm 的指令" caption-side="bottom"}
`範例 4. 使用 CLI Developer Tools 登入 IBM Cloud Platfirm 的指令`

如果您想要下列項目，可以指定選用參數：指定 -o 選項可取得組織，指定 -s 選項可取得空間，或者，如果您使用聯合帳戶，則可以指定：--sso。登入時，系統可能會要求您選擇地區，基於此練習的目的，請選取 `us-south` 作為地區，因為之後在本指導教學中建置 CD Toolchain 時，將會使用該相同選項。  

接下來，使用範例 5 所示的指令來設定端點（如果尚未設定的話）。您可以使用其他端點，而且它們可能適合正式作業使用，但現在，如果您的帳戶適用，請使用所示的程式碼。

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="範例 5. 設定帳戶 API 端點的指令。" caption-side="bottom"}
`範例 5. 設定帳戶 API 端點的指令`

使用 target 指令及 --cf 選項，利用範例 6 所示的程式碼將 {{site.data.keyword.cloud_notm}} Platform 的 Cloud Foundry (cf) 層面設為目標。`cf` API 內嵌在 CLI Developer Tools 中。

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="範例 6. 設定使用 Cloud Foundry API 的選項。" caption-side="bottom"}
`範例 6. 設定使用 Cloud Foundry API 的選項`

現在，您一直努力在：使用範例 7 所示的程式碼開始建立 Web 應用程式。`dev` 空間是您組織的預設選項，但您可能想要建立其他空間來隔離不同的工作，例如，隔離 'finance' 與 'development'。

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="範例 7. 使用 IBM Cloud Developer Tools 建立應用程式的指令" caption-side="bottom"}
`範例 7. 使用 IBM Cloud Developer Tools 建立應用程式的指令`

使用該指令時，系統會詢問您一連串的問題。在過程中您可以多次返回，但如果您感覺自己已迷失方向或遺漏了某些步驟，請隨意刪除目錄或建立另一個目錄來進行測試及探索，以重新開始。此外，當您完成在指令行本端建立應用程式的處理程序時，稍後就可以在您建立帳戶以管理所建立資源的 {{site.data.keyword.cloud_notm}} 線上入口網站中查看結果。

在範例 8 中，請記住此選項是建立 'Web App'&mdash;即您想要的選項。鍵入 '2'，然後按 Enter 鍵。

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
{: caption="範例 8. `ibmcloud dev create` 指令的輸出，其中選取選項 #2，以建立 Web 應用程式" caption-side="bottom"}
`範例 8. ibmcloud dev create 指令的輸出，其中選取選項 #2，以建立 Web 應用程式`

根據稱為「建置套件」的內容，範例 9 中有許多選項，請記住此選項是使用 'Node'。鍵入 '4'，然後按 Enter 鍵。

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
{: caption="範例 9. `ibmcloud dev create` 的語言選項（續）。" caption-side="bottom"}
`範例 9. ibmcloud dev create 的語言選項（續）`

在您選擇程式設計語言及（或）架構之後，範例 10 所示的下一個選擇將會有許多選項，因此可能會捲動到超過您想要的服務。如您在範例中所見，我們想要使用簡單的 Node.js Web App with Express.js。鍵入 '6'，然後按 Enter 鍵。

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
{: caption="範例 10. `ibmcloud dev create` 的架構應用程式選項。" caption-side="bottom"}
`範例 10. ibmcloud dev create 的架構應用程式選項`

現在，您已選擇更直接的選項，對世界各地開發人員而言最困難的選項仍是：命名應用程式。請遵循範例 11 所示的範例，鍵入 'webapplication'，然後按 Enter 鍵。

```bash
? Enter a name for your application> webapplication
```
{: caption="範例 11. 使用 `ibmcloud dev create` 來命名應用程式 'webapplication'。" caption-side="bottom"}
`範例 11. 使用 ibmcloud dev create 來命名應用程式 'webapplication'`

稍後，您可以透過 Web 主控台視需要新增任意數量的服務，例如資料儲存庫或運算功能。不過，如範例 12 所示，當系統詢問您是否要現在新增服務時，請鍵入 'n' 來拒絕。

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="範例 12. 使用 `ibmcloud dev create` 時的新增服務選項（續）。" caption-side="bottom"}
`範例 12. 使用 ibmcloud dev create 時的新增服務選項（續）`

稍早已提及關於 Docker 與容器一起開發的優點，而非傳統伺服器 Iron，甚至是虛擬伺服器。其中一種管理容器的方式是使用編排軟體（例如 Kubernetes），它已成為開發時的_現存_ 標準。但是，對於本指導教學，我們可以讓 Cloud Foundry 服務管理單一 Docker 容器，其中包含應用程式所需的程式碼、程式庫及配置。

如範例 13 所示，鍵入 '1'，然後按 Enter 鍵，以使用 'IBM DevOps' 在專案生命週期內整合 CD。
 
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
{: caption="範例 13. `ibmcloud dev create` 的部署選項。" caption-side="bottom"}
`範例 13. ibmcloud dev create 的部署選項`

如前所述，我們將選擇自動化部署 CD 工具鏈的地區，因此請選取與之前相同的選項，即範例 14 所示的 '5'。

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
{: caption="範例 14. 可作為 `ibmcloud dev create` 中選項的地區。" caption-side="bottom"}
`範例 14. 可作為 ibmcloud dev create 中選項的地區`

此時，產生新的應用程式時會提醒我們，稍後用來部署應用程式的工具鏈需要一些額外配置，如範例 15 所示。如之前所述，需要將公開金鑰上傳至 Github（在 {{site.data.keyword.cloud_notm}} Platform 的 CD Toolchain 實例上），才能使用 Github 遞送已部署的應用程式。部署應用程式並登入 IBM Cloud GitLab 帳戶（位於 [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair)）之後，即可找到其他指示。

```

Note: For successful connection to the DevOps toolchain, this machine 
must be configured for SSH access to your IBM Cloud GitLab account at 
https://git.ng.bluemix.net/profile/keys in order to download the 
application code.


```
{: caption="範例 15. Note given re：`ibmcloud dev create` 指令的 SSH 金鑰" caption-side="bottom"}
`範例 15. Note given re：ibmcloud dev create 指令的 SSH 金鑰`

進一步提示將確認您稍早定義的應用程式及工具鏈名稱。範例 16 顯示您可以變更主機及工具鏈名稱的方式（如果想要的話）。在作為應用程式服務端點的網域中，主機名稱必須是唯一的，但如果未發生衝突，您只會在要求確認時才按 Return。

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="範例 16. 確認 `ibmcloud dev create` 中內容的名稱。" caption-side="bottom"}
`範例 16. 確認 ibmcloud dev create 中內容的名稱`

如果您複製並貼上在使用 `ibmcloud dev create` 指令後所收到的輸出尾端的給定鏈結，則能夠存取您的 CD Toolchain。但是，如果您錯過擷取該鏈結，也可以之後從主控台進行存取。隨著處理程序繼續執行，後面會有進一步資訊，如範例 17 所示，其中，已在線上建立應用程式項目，並且已建立具有範例程式碼的目錄。 

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
{: caption="範例 17. 確認 `ibmcloud dev create` 所產生的動作。" caption-side="bottom"}
`範例 17. 確認 ibmcloud dev create 所產生的動作`

範例 17 的最後一個陳述式表示，如果您檢視現行目錄，現在應該可以看到新的子目錄 `webapplication`。在 `webapplication` 目錄內，您會找到新 Node.js 應用程式的支撐。不過，雖然可能已有食譜，但是必須使用範例 18 的指令來「烹煮」（抱歉，建置）仍包裝在 Docker 映像檔中的配料本身。安裝之後，Docker 應在本端機器上執行，但如果您需要將它重新啟動，請這麼做。在 Docker 未執行的情況下，任何嘗試建置新的 Web 應用程式都會失敗，但這不是唯一可能的原因。如果有任何問題，請檢查產生的錯誤訊息，這些錯誤訊息可能有適當的鏈結，可讓您在 {{site.data.keyword.cloud_notm}} Platform 帳戶的線上入口網站中檢視結果日誌。

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="範例 18. {{site.data.keyword.cloud_notm}} Platform build 指令" caption-side="bottom"}
`範例 18. IBM Cloud Platform build 指令`

除了建置應用程式進行交付之外，建置應用程式也容許您使用 `run` 指令在本端執行相同的程式碼（在您複製並貼上之後，或從範例 19 鍵入指令之後）。完成時，請複製提供的 URL，並將其貼入瀏覽器的位址列，通常是 <http://localhost:3000>。

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="範例 19. 執行應用程式的 {{site.data.keyword.cloud_notm}} Platform CLI 指令" caption-side="bottom"}

現在，已建立並定義應用程式，請檢視應用程式以確認它可以運作。如果您看到圖 2 所示的位置保留元影像，恭喜！您已建立新的 Node.js Web 應用程式，並已準備好可將它部署至雲端。

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="圖 2. 新的 Node.js 應用程式：恭喜！" caption-side="top"}

使用 deploy 指令，將應用程式部署至 {{site.data.keyword.cloud_notm}} Platform（如範例 20 所示）。

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="範例 20. 用來上傳並部署應用程式的 {{site.data.keyword.cloud_notm}} Platform CLI 指令" caption-side="bottom"}
`範例 20. 用來上傳並部署應用程式的 IBM Cloud Platform CLI 指令`

根據之前指定的地區端點及主機名稱執行 `ibmcloud dev deploy` 指令之後，會再次顯示 URL。如果有任何問題，您可能會在 {{site.data.keyword.cloud_notm}} Platform 看到儲存在入口網站中的日誌鏈結。如果沒有任何問題，您應該會在瀏覽器中看到與剛才造訪的本端應用程式相同的顯示畫面。請繼續並造訪雲端中的新 Web 應用程式！

## 使用範例應用程式建立 Web Gallery 應用程式
{: #tutorial-create-app}

請回想一下在 {{site.data.keyword.cloud_notm}} Platform 上開發 Node.js 應用程式所需的必要條件。您已建立 {{site.data.keyword.cloud_notm}} Platform 帳戶，並安裝已安裝 Docker 的 Developer Tools。然後，安裝 Node.js。作為本指導教學必要條件列出的最後一個項目是我們現在將深入研究的 Git。  

我們將開始在 Node.js 中處理 Image Gallery 的細節。現在，我們將在此情境中使用 Github Desktop，但您也可以使用 Git 指令行用戶端來完成相同的作業。若要開始使用，請針對新的 Web 應用程式複製一個入門範本。 

請遵循下列步驟：

1.  複製範例 21 所列的儲存庫。使用 Git，在本端開發環境下載應用程式的範本。使用範例 21 的指令來複製 {{site.data.keyword.cos_full_notm}} Web Gallery 應用程式的入門範本，而不是從 {{site.data.keyword.cloud_notm}} Platform 複製範例應用程式。複製儲存庫之後，您會在 COS-WebGalleryStart 目錄中找到入門範本應用程式。開啟 Git CMD 視窗，然後切換至您要複製 Github 儲存庫的目錄。使用本指導教學的第一個範例中所顯示的指令。

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="範例 21. Git clone 指令詳細資料" caption-side="bottom"}
`範例 21. Git clone 指令詳細資料`

2.  在本端執行應用程式。開啟終端機應用程式並提供 CLI，然後將工作目錄切換至 COS-WebGalleryStart 目錄。請記住 package.json 檔案中所列的 Node.js 相依關係。使用範例 22 中下一個顯示的指令，將它們下載至適當位置。

```bash
npm install
```
{: codeblock}
{: caption="範例 22. Node Package Manager (npm) 安裝" caption-side="bottom"}
`範例 22. Node Package Manager (npm) 安裝`

3.  使用範例 23 所示的指令來執行應用程式。

```bash
npm start
```
{: codeblock}
{: caption="範例 23. 使用 npm 啟動應用程式的詳細資料" caption-side="bottom"}
`範例 23. 使用 npm 啟動應用程式的詳細資料`

開啟瀏覽器，並在輸出至主控台的位址及埠上檢視應用程式：<http://localhost:3000>。

**提示**：若要在本端重新啟動應用程式，請結束節點處理程序 (Ctrl+C) 以將它停止，並再次使用 `npm start`。不過，當您開發新特性，在應用程式偵測到變更時，使用 nodemon 重新啟動應用程式，可節省您的時間。廣域地安裝 nodemon，例如：`npm install -g nodemon`。然後，從指令行使用 `nodemon` 在應用程式目錄中執行它，讓 'nodemon' 啟動應用程式。

4.  準備好準備應用程式進行部署！必要的話，將 `manifest.yml` 檔案中的應用程式名稱內容值從 COS-WebGallery 更新為您在 {{site.data.keyword.cloud_notm}} Platform 上為應用程式輸入的名稱，以及範例 24 所示的其他資訊。應用程式 `manifest.yml` 看起來類似下列範例。此外，您也可以使用應用程式的名稱和作者的名稱，來自訂位於應用程式的應用程式根目錄中的 `package.json` 檔案。

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
{: caption="範例 24. `manifest.yml` 的內容" caption-side="bottom"}
`範例 24. manifest.yml 的內容`

**提示**：現在，您可能需要設定 SSH 金鑰，以互動方式將程式碼推送至遠端原點。如果您設定 
    SSH 金鑰的通行詞組，則每次將變更推送至儲存庫的遠端原點時都需要輸入此程式碼。 

5.  移除 `webapplication` 目錄的內容，並將其取代為您剛剛修改的目錄內容 `COS-WebGalleryStart`。使用精細調整的 Git 技能，新增已刪除並使用 CLI 或 Github Desktop 新增至儲存庫的檔案。然後，將變更推送至儲存庫原點。日後，您只要將變更推送至 Git，就能對雲端型 Web 應用程式進行變更。在複製變更並將其隱藏在伺服器之後，CD 工具鏈即會自動重新啟動伺服器處理程序。 


在本質上，我們已重新編碼應用程式，因此請重複建置處理程序。但這次，我們將使用新的 Image Gallery 程式碼。 

###將應用程式部署至 {{site.data.keyword.cloud_notm}} Platform。### 

若要取得入門範本應用程式與您對
    {{site.data.keyword.cloud_notm}} Platform 的變更，請重複我們之前執行的相同步驟，使用 Developer Tools 來部署它。

a.  如果您尚未完成，或已重新啟動或登出，請使用 login 指令登入 {{site.data.keyword.cloud_notm}} Platform。提醒您，它顯示在範例 25 中，並請注意，如果您想要下列項目，可以指定選用參數：指定 -o 選項可取得組織，指定 -s 選項可取得空間，或者，如果您使用聯合帳戶，則可以指定：--sso。請記得選擇您在此時間點所使用的相同地區（如果系統詢問您的話）。

```bash
ibmcloud login
```
{: codeblock}
{: caption="範例 25. 登入 {{site.data.keyword.cloud_notm}} Platform 的 CLI 指令" caption-side="bottom"}
`範例 25. 登入 IBM Cloud Platform 的 CLI 指令`

b.  使用 api 指令來設定地區的「API 端點」（如範例 6 的選用位置保留元所示）。如果您不知道地區 API 端點 URL，請參閱「開始使用」頁面。

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="範例 26. {{site.data.keyword.cloud_notm}} Platform API 端點" caption-side="bottom"}
`範例 26. IBM Cloud Platform API 端點`

c.  使用 target 指令及 --cf 選項，利用範例 27 所示的程式碼將 {{site.data.keyword.cloud_notm}} Platform 的 Cloud Foundry 層面設為目標。


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="範例 27. 將 {{site.data.keyword.cloud_notm}} Platform CLI 的 Cloud Foundry 設為目標 " caption-side="bottom"}
`範例 27. 將 IBM Cloud Platform CLI 的 Cloud Foundry 設為目標`

d.  使用 build 指令，建置應用程式來交付該應用程式（如同範例 28）。

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="範例 28. {{site.data.keyword.cloud_notm}} Platform build 指令" caption-side="bottom"}
`範例 28. IBM Cloud Platform build 指令`

g.  繼續並在本端測試應用程式。除了建置應用程式進行交付之外，建置應用程式也容許您使用 run 指令在本端執行相同的程式碼（在範例 29 鍵入指令之後）。


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="範例 29. 執行應用程式的 {{site.data.keyword.cloud_notm}} Platform CLI 指令" caption-side="bottom"}
`範例 29. 執行應用程式的 IBM Cloud Platform CLI 指令`

h.  使用 deploy 指令，將應用程式部署至 {{site.data.keyword.cloud_notm}} Platform（如範例 30 所示）。

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="範例 30. 用來上傳並部署的 {{site.data.keyword.cloud_notm}} Platform CLI 指令" caption-side="bottom"}
`範例 30. 用來上傳並部署的 IBM Cloud Platform CLI 指令`

範例 31 中的程式碼顯示此範例中用來建置、測試及部署起始 Web 應用程式的指令序列。

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
{: caption="範例 31. {{site.data.keyword.cloud_notm}} Platform CLI 指令清單" caption-side="bottom"}
`範例 31. IBM Cloud Platform CLI 指令清單`

如果成功，{{site.data.keyword.cloud_notm}} Platform 會報告已上傳、順利部署並啟動應用程式。如果您也已登入 {{site.data.keyword.cloud_notm}} Platform Web 主控台，則也會在該處通知您應用程式的狀態。但是，最重要的是，您可以使用瀏覽器來造訪 {{site.data.keyword.cloud_notm}} Platform 所報告的應用程式 URL，或從 Web 主控台按一下「檢視應用程式」按鈕，來驗證已部署該應用程式。

5.  測試應用程式。建立時部署至下面所示的入門範本應用程式的預設應用程式範本的可見變更，證明已成功將應用程式部署至 {{site.data.keyword.cloud_notm}} Platform。

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### 建立 Git 分支
{: #tutorial-create-branch}

現在，您需要為本端開發環境建立分支，以用於「{{site.data.keyword.cloud_notm}} Platform Delivery Pipeline 建置階段」：

1.  如果使用 Github Desktop，請按一下分支圖示；系統會提示您輸入分支的名稱（請參閱圖 14）。此範例使用 Local-dev 作為名稱。

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  建立分支之後，GitHub 會比較 Local-dev 分支上的本端檔案與主要分支上儲存庫中的檔案，並報告「無本端變更」。您現在可以按一下「發佈」，將您在本端儲存庫上建立的分支新增至 GitHub 儲存庫（如圖 5 所示）。

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

現在，Local-dev 分支已發佈至工具鏈中的 GitHub 儲存庫，因此只要您將確定推送至 {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline，就會先觸發它的建置階段，再觸發部署階段。
不再需要從 CLI 部署應用程式，因為部署已直接整合至工作流程。

### 設定 {{site.data.keyword.cos_full_notm}} 儲存空間認證
{: #tutorial-credentials}

您需要配置 Web 應用程式的 {{site.data.keyword.cos_short}} 認證，以及它將儲存及擷取影像的「儲存區」。您將建立的 API 金鑰需要 {{site.data.keyword.cos_short}} HMAC 認證，如[服務認證](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials)所定義。您可以辨識 `access_key_id` 及 `secret_access_key` 項目，因為您可能具有 AWS 帳戶，並使用已具有 `aws_access_key_id` 及 `aws_secret_access_key` 項目的認證檔。 

在您已完成建立 API 金鑰、下載並複製 HMAC 認證之後，請完成下列步驟：

1.  在本端開發環境上，將認證放在 Windows 路徑 `%USERPROFILE%\\.aws\\credentials`（適用於 Mac/Linux 使用者，認證應該進入 `~/.aws/credentials`）。範例 32 顯示一般認證檔的內容。

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="範例 32. 定義於 `~/.aws/credentials` 檔案的認證" caption-side="bottom"}
`範例 32. 定義於 ~/.aws/credentials 檔案的認證`

2.  在 {{site.data.keyword.cloud_notm}} Platform 上使用 CLI 指令所建立的應用程式網頁中，登入 {{site.data.keyword.cloud_notm}} Platform，根據開發最佳作法將您的必要認證定義為環境變數，並在「Cloud Foundry 應用程式」下方選取應用程式 'webapplication'。從標籤中，按一下「運行環境」。

3.  在「運行環境」視窗中，按一下頁面頂端的「環境變數」，並捲動至「使用者定義」區段，讓您可以新增變數。

4.  新增兩個變數：一個具有 access_key_id 值，且使用 `AWS_ACCESS_KEY_ID` 作為金鑰名稱，另一個則具有密碼存取金鑰值，且名為 `AWS_SECRET_ACCESS_KEY`。在 {{site.data.keyword.cloud_notm}} Platform 上執行時，這些變數及其個別值是應用程式用來鑑別 {{site.data.keyword.cos_short}} 實例的內容（請參閱圖 6）。當您完成項目時，請按一下「儲存」，{{site.data.keyword.cloud_notm}} Platform 將會自動重新啟動應用程式。

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

接下來，在服務實例的「{{site.data.keyword.cos_short}} 入口網站」上，新增儲存區以包含影像。此情境使用名為 `web-images` 的儲存區。


## 自訂 Node.js {{site.data.keyword.cos_full_notm}} Image Gallery Web 應用程式
{: #tutorial-develop}

因為此範例使用 MVC 架構，所以調整專案內的目錄結構來反映此架構是一種便利方式，也是最佳作法。 目錄結構具有包含 EJS 視圖範本的 views 目錄、包含 Express 路徑的 routes 目錄，以及用來放置控制器邏輯的 controllers 目錄。請將這些項目放在名為 src 的母項來源目錄下（請參閱圖 7）。

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**提示**：您之前複製的儲存庫包含名為 COS-WebGalleryEnd 的目錄。在遵循後續步驟時，使用偏好的編輯器來檢視已完成應用程式的原始碼可能十分有用。這將是在您完成本指導教學時，確定並部署至 {{site.data.keyword.cloud_notm}} Platform 的 'webapplication' 版本。

### 設計應用程式
{: #tutorial-develop-design}

這是使用者應該能夠使用簡單 Image Gallery Web 應用程式執行的兩個主要作業：

  - 將影像從 Web 瀏覽器上傳至 {{site.data.keyword.cos_short}} 儲存區。
  - 在 Web 瀏覽器中檢視 {{site.data.keyword.cos_short}} 儲存區中的影像。

後續步驟著重在如何完成這兩個示範功能，而不是建置完整開發的正式作業等級應用程式。部署本指導教學並讓它公開並執行，表示任何找到此應用程式的人都可以執行相同的動作：將檔案上傳至 {{site.data.keyword.cos_full_notm}} 儲存區，並檢視其瀏覽器中已存在的所有 JPEG 影像。

### 開發應用程式
{: #tutorial-develop-app}

在 `package.json` 檔案的 scripts 物件內，您會看到如何定義 "start"（範例 33）。{{site.data.keyword.cloud_notm}} Platform 使用此檔案來告知節點在每次應用程式啟動時執行 app.js。也可以在本端測試應用程式時使用它。請查看主要應用程式檔案（稱為 app.js）。這是在您使用 `npm start` 指令（或 nodemon）啟動應用程式時，我們告知 Node.js 先處理的程式碼。 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="範例 33. 告知應用程式如何引導自訂程式碼" caption-side="bottom"}
`範例 33. 告知應用程式如何引導自訂程式碼`

app.js 檔案的開頭是範例 34 所示的程式碼。一開始，程式碼會使用節點來載入開始使用時所需的模組。Express 架構會將應用程式建立為單態，簡稱為 `app`。範例結束（暫時省略大部分程式碼）告知應用程式接聽指派的埠及環境內容（依預設為 3000）。若在開始時順利啟動，則會將具有伺服器 URL 的訊息列印至主控台。

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
{: caption="範例 34. Web 應用程式具有簡略但強大的開始" caption-side="bottom"}
`範例 34. Web 應用程式具有簡略但強大的開始`

請查看範例 35，顯示路徑及視圖的定義方式。第一行程式碼會告知 Express 架構使用公用目錄來提供靜態檔案，其中包括使用的所有靜態影像及樣式表。後面的各行告知應用程式在 src/views 目錄中尋找視圖範本的位置，並將視圖引擎設為 EJS。此外，架構也會使用主體剖析器中介軟體，將送入的要求資料以 JSON 形式公開給應用程式。在範例的結尾各行，Express 應用程式會呈現 index.ejs 視圖範本，以回應進入應用程式 URL 的所有送入 GET 要求。

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
{: caption="範例 35. Web 應用程式視圖及範本位置" caption-side="bottom"}
`範例 35. Web 應用程式視圖及範本位置`

下圖顯示呈現並傳送至瀏覽器的索引視圖範本。如果您使用 `nodemon`，則可能已注意到您在儲存變更時已重新整理瀏覽器，而且您的應用程式應該看起來像圖 8。

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

在範例 36 中，視圖範本會在 &lt;head&gt;...&lt;/head&gt; 標籤之間分享 HTML 程式碼，因此我們會將它放在個別的併入範本中（請參閱範例 16）。此範本 (head-inc.ejs) 在第 1 行包含頁面標題的 Scriptlet（JavaScript 變數的連結）。`title` 變數設定在 `app.js` 中，並在其下的指令行中作為我們視圖範本的資料傳入。否則，只需要使用一些 CDN 位址來取回 Bootstrap CSS、Bootstrap JavaScript 及 JQuery。最後，從 pubic/style sheets 目錄中新增自訂靜態 styles.css 檔案。

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
{: caption="範例 36. head-inc.ejs 中的 HTML 元素" caption-side="bottom"}
`範例 36. head-inc.ejs 中的 HTML 元素`

索引視圖的主體包含引導樣式導覽標籤（請參閱範例 37），以及引導隨附的 CSS 樣式所提供的基本佈置中的上傳表單。

請考量應用程式的這兩個規格：

-   將表單方法設為 POST，並在第 24 行將表單資料編碼類型設為 multipart/form-data。針對表單動作，將表單中的資料傳送至應用程式路徑 "/" 的應用程式。稍後，在路由器邏輯中執行其他工作，以處理對該路徑的 POST 要求。

-   我們想要向使用者顯示有關嘗試檔案上傳狀態的意見。此意見會以名為 "status" 的變數傳遞至視圖中，並顯示在上傳表單下面。

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="/">Home</a></li>
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
{: caption="範例 37. index.ejs 中的 HTML 元素" caption-side="bottom"}
`範例 37. index.ejs 中的 HTML 元素`

在範例 38 中，需要一些時間才能回到 `app.js`。此範例會設定 Express 路徑，以處理要對應用程式提出的其他要求。這些遞送方法的程式碼將位於專案的 `./src/routes` 目錄下的兩個檔案中：

-   imageUploadRoutes.js：此檔案會處理在使用者選取影像並按一下「上傳」時所發生的情況。

-   galleryRoutes.js：在使用者按一下「展示區」標籤以要求 imageGallery 視圖時，此檔案會處理要求。

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
{: caption="範例 38. Node Express 路由器範例" caption-side="bottom"}
`範例 38. Node Express 路由器範例`

#### 影像上傳
{: #tutorial-develop-image-upload}

請參閱範例 39 中 'imageUploadRoutes.js' 內的程式碼。我們必須在開始時建立新 Express 路由器的實例，並將它命名為 `imageUploadRoutes`。稍後，建立的函數會傳回 `imageUploadRoutes`，並將它指派給稱為 `router` 的變數。完成時，必須將函數匯出為模組，讓架構及 app.js 中的主要程式碼能夠存取它。區隔遞送邏輯與上傳邏輯時，需要名為 galleryController.js 的控制器檔案。因為該邏輯專用於處理送入的要求，並提供適當的回應，所以將該邏輯放入該函數中，並將它儲存在 ./src/controllers 目錄中。

Express 架構中的「路由器」實例是設計 imageUploadRoutes 的地方，以在使用 HTTP POST 方法時遞送根應用程式路徑 ("/") 的要求。
在 imageUploadRoutes 的 `post` 方法內，使用 `multer` 及 `multer-s3` 模組中由 galleryController 公開為 `upload` 的中介軟體。
中介軟體會從「上傳」表單 POST 中取得資料及檔案，並進行處理，然後執行回呼函數。在回呼函數中，確認取得 HTTP 狀態碼 200，而且要求物件中至少有一個要上傳的檔案。根據這些條件，在 `status` 變數中設定意見，並呈現具有新狀態的索引視圖範本。

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
{: caption="範例 39. Node Express 路由器詳細資料" caption-side="bottom"}
`範例 39. Node Express 路由器詳細資料`

相較之下，範例 40 中 galleryRouter 的程式碼是簡易性模型。請遵循使用 imageUploadRouter 所做的相同型樣，並在函數第一行要求 galleryController，然後設定路徑。主要差異在於遞送 HTTP GET 要求，而不是 POST，以及在 getGalleryImages 的回應中傳送所有輸出，這會由範例最後一行上的 galleryController 公開。

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
{: caption="範例 40. Node Express 路由器詳細資料" caption-side="bottom"}
`範例 40. Node Express 路由器詳細資料`

接下來，會將注意力集中在展示區的控制器。

請注意，我們在範例 41 中如何設定 `multer` 上傳（這現在會截斷暫時將忽略的部分程式碼）。我們需要模組 `ibm-cos-sdk`、`multer` 及 `multer-s3`。此程式碼顯示如何配置指向 {{site.data.keyword.cos_short}} 伺服器端點的 S3 物件。我們會靜態地設定值（例如端點位址、地區及儲存區）以進行簡化，但可以透過環境變數或 JSON 配置檔輕鬆地參照它們。

我們會藉由建立以 `storage` 作為其唯一內容的新 `multer` 實例，來定義範例 41 中所使用且在 imageUploadRouter 中定義的 `upload`。此內容會告知 `multer` 從 multipart/form-data 傳送檔案的位置。自從 {{site.data.keyword.cloud_notm}} Platform 使用 S3 API 實作後，我們將儲存空間設為 `s3-multer` 物件。此 `s3-multer` 物件包含之前指派給 `s3` 物件的 `s3` 內容，以及指派已獲指派 "web-images" 值之 `myBucket` 變數的儲存區內容。`s3-multer` 物件現在具有從上傳表單接收資料時，將檔案連接及上傳至 {{site.data.keyword.cos_short}} 儲存區所需的所有資料。所上傳物件的名稱或金鑰將是其儲存在 {{site.data.keyword.cos_short}} "web-images" 儲存區時，從檔案物件取得的原始檔名 

**提示**：請使用時間戳記作為檔名的一部分，以維護檔名的唯一性。 

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
{: caption="範例 41. Node Express 控制器詳細資料" caption-side="bottom"}
`範例 41. Node Express 控制器詳細資料`

若為本端測試，有用的作業是將檔案物件列印至主控台 `console.log(file)`。我們執行「上傳」表單的本端測試，並顯示範例 42 中檔案的主控台日誌輸出。

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="範例 42. 除錯物件的主控台顯示" caption-side="bottom"}
`範例 42. 除錯物件的主控台顯示`

雖然自誇並不適合，但是圖 9 顯示回呼的意見，而回呼宣告應用程式在測試時確實「順利上傳檔案」。

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### 影像擷取及顯示
{: #tutorial-image-display}

請記住，回到 app.js，程式碼行 `app.use('/gallery', galleryRouter);` 會告知 Express 架構，在要求 "/gallery" 路徑時使用該路由器。該路由器（如果您記得）使用 galleryController.js（請參閱範例 43 中的程式碼），而我們定義 getGalleryImages 函數，這是我們先前看到的簽章。使用針對影像上傳函數所設定的相同 `s3` 物件，我們會呼叫名為 `listObjectsV2` 的函數。此函數會傳回定義儲存區中每個物件的索引資料。若要在 HTML 內顯示影像，`web-images` 儲存區中每個 JPEG 影像都需要一個影像 URL，才能在視圖範本中顯示。具有 `listObjectsV2` 所傳回資料物件的關閉，會包含儲存區中每個物件的 meta 資料。 

程式碼會循環執行 `bucketContents` 以搜尋結尾為 ".jpg" 的任何物件索引鍵，並建立要傳遞給 S3 getSignedUrl 函數的參數。當我們提供物件的儲存區名稱及索引鍵時，此函數會傳回任何物件的已簽署 URL。在回呼函數中，我們將每個 URL 儲存至陣列中，並將它傳遞至 HTTP 伺服器回應方法 `res.render`，作為名為 `imageUrls` 的內容值。

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
{: caption="範例 43. galleryController.js 的局部內容" caption-side="bottom"}
`範例 43. galleryController.js 的局部內容`

本指導教學中編號 44 的最後一個程式碼範例，顯示具有顯示影像所需程式碼之 galleryView 範本的主體。我們會從 res.render() 方法取得 imageUrls 陣列，並反覆運算一對巢狀 &lt;div&gt;&lt;/div&gt; 標籤，其中，要求 /gallery 路徑時，影像 URL 會提出影像的 GET 要求。

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="/">Home</a></li>
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
{: caption="範例 44. 展示區範本中使用的迴圈及輸出 Scriptlet" caption-side="bottom"}
`範例 44. 展示區範本中使用的迴圈及輸出 Scriptlet`

我們在本端從 http://localhost:3000/gallery 進行測試，並在圖 10 中看到影像。

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### 確定至 Git
{: #tutorial-develop-commit}

現在，應用程式的基本特性正在運作，我們會將程式碼確定至本端儲存庫，然後將它推送至 GitHub。使用 GitHub Desktop 時，我們按一下「變更」（請參閱圖 11），在「摘要」欄位中鍵入變更的摘要，然後按一下「確定至 Local-dev」。 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

當我們按一下「同步」時，會將確定傳送至已發佈至 GitHub 的遠端 Local-dev 分支，而此動作會在 Delivery Pipeline 中啟動「建置階段」，後面接著「部署階段」（如本指導教學編號 12 的最後一張圖所示）。 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## 後續步驟
{: #nextsteps}

恭喜！我們已從頭到尾走完此路徑，使用 {{site.data.keyword.cloud_notm}} Platform 建置了 Web 應用程式 Image Gallery。
您可以在 [{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/) 進一步探索此基本簡介中涵蓋的每個概念。 

祝您一切順利！
