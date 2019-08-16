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

# 教程：图像库 Web 应用程序
{: #web-application}

从头至尾构建 Web 应用程序涵盖许多不同的概念，非常适合用于了解 {{site.data.keyword.cos_full}} 的功能。本教程将向您说明如何在 {{site.data.keyword.cloud}} Platform 上构建简单的图像库，以及如何综合利用许多不同的概念和实践。应用程序将 {{site.data.keyword.cos_full_notm}} 用作 Node.js 应用程序的后端服务器，允许用户上传和查看 JPEG 图像文件。

## 开始之前
{: #wa-prereqs}

作为构建 Web 应用程序的先决条件，首先须具备：

  - {{site.data.keyword.cloud_notm}} Platform 帐户
  - Docker（作为 {{site.data.keyword.cloud_notm}} Developer Tools 的一部分）
  - Node.js 
  - Git（Desktop 应用程序和命令行界面 &mdash; CLI）

### 安装 Docker
{: #tutorial-wa-install-docker}

从使用传统服务器实例或者甚至使用虚拟服务器来构建 Web 应用程序过渡到使用容器（如 Docker）进行构建，不仅能加快开发速度，还能在支持自动部署的同时，轻松执行测试。容器是一种轻量级结构，无需额外的开销（如操作系统），只用代码和配置就能满足从依赖项到设置的一切要求。

首先，打开有经验的开发者都熟悉，并且新手也能轻松上手的工具：命令行。自从图形用户界面 (GUI) 问世以来，计算机命令行界面的地位已降至次等。但是现在，是时候重新起用命令行界面了（不过 GUI 不会在短时间内消失 &mdash; 尤其是需要浏览 Web 来下载新的命令行工具集时）。 

请打开终端或与操作系统相应的其他命令行界面，然后使用与所用特定 shell 相应的命令来创建目录。将您自己的引用目录切换为刚才创建的新目录。应用程序创建后，会在该目录中拥有其自己的子目录，其中包含快速入门和熟悉运用所需的入门模板代码和配置。

退出命令行并返回到浏览器后，遵循链接中的指示信息安装 [{{site.data.keyword.cloud_notm}} Platform Developer Tools](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli)。Developer Tools 提供了可扩展且可重复的方法来构建和部署云应用程序。

[Docker](https://www.docker.com) 作为 Developer Tools 的一部分进行安装，我们需要 Docker，虽然 Docker 的工作主要是在后台为新应用程序搭建脚手架的例程中执行。Docker 必须在运行，构建命令才能正常工作。在 [Dockerhub](https://hub.docker.com) 上联机创建 Docker 帐户，运行 Docker 应用程序，然后登录。

### 安装 Node.js
{: #tutorial-wa-install-node}

您将构建的应用程序使用 [Node.js](https://nodejs.org/) 作为服务器端引擎来运行此 Web 应用程序的 JavaScript 代码。要使用 Node 随附的 Node Package Manager (npm) 来管理应用程序的依赖项，必须在本地安装 Node.js。此外，在本地安装 Node.js 简化了测试，因此加快了开发速度。 

开始之前，可以考虑使用版本管理器（如 Node Version Manager - `nvm`）来安装 Node，从而降低管理多个版本 Node.js 的复杂性。在本文撰写之时，要在 Mac 或 Linux 机器上安装或更新 `nvm`，可以在刚才打开的 CLI 界面中通过 cURL 来使用安装脚本，方法是将前两个示例中的其中一个命令复制并粘贴到命令行，然后按 Enter 键（注：这假定 shell 是 BASH，而不是备用项）：

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="示例 1. 使用 cURL 安装 Node Version Manager (nvm)" caption-side="bottom"}
`示例 1. 使用 cURL 安装 Node Version Manager (nvm)`
   
...或者使用 Wget（只能使用其中一个工具，而不能同时使用这两者；请使用您系统上可用的其中任一工具）：

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="示例 2. 使用 Wget 安装 Node Version Manager (nvm)" caption-side="bottom"}
`示例 2. 使用 Wget 安装 Node Version Manager (nvm)`

或者，对于 Windows，可以在链接中将 [nvm for Windows](https://github.com/coreybutler/nvm-windows) 与安装程序和源代码配合使用。

如果您不希望增加支持多个 Node.js 发行版的复杂性，请访问 [Node.js](https://nodejs.org/en/download/releases/) Web 站点，并安装 Node.js 的长期支持 (LTS) 版本，此版本与 {{site.data.keyword.cloud_notm}} Platform 上现在使用的 SDK for Node.js buildpack 支持的最新版本相匹配。在撰写本文时，最新版本的 buildpack 是 V3.26，此版本支持 Node.js Community Edition V6.17.0+。 

您可以在 [SDK for Node.js 最新更新](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates)页面上找到有关最新 {{site.data.keyword.cloud_notm}} SDK for Node.js buildpack 的其他信息。 

使用 `nvm` 可以安装相应的 Node 版本，以满足将示例 3 中的命令复制并粘贴到命令行的需求。

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="示例 3. 使用 `nvm` 安装特定版本的 Node.js" caption-side="bottom"}
`示例 3. 使用 nvm 安装特定版本的 Node.js`

无论使用哪种方法，只要遵循了与您所使用的操作系统和策略相应的指示信息在计算机上安装了 Node.js 和 npm（随附于 Node），那么祝贺您，您已顺利完成准备工作！

### 安装 Git
{: #tutorial-wa-install-git}

您可能已经很熟悉 Git，因为这是开发者为 Web 构建应用程序时最广泛使用的源代码版本控制系统。稍后在 {{site.data.keyword.cloud_notm}} Platform 中创建 Continuous Delivery (CD) 工具链以用于持续交付和部署时，将使用 Git。如果您没有 GitHub 帐户，请在 [Github](https://github.com/join) Web 站点上创建免费公共个人帐户；如果有 GitHub 帐户，那么可随意使用您可能拥有的其他任何帐户进行登录。

请注意，对于如何生成 SSH 密钥并将其上传到 [Github 概要文件](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)，以通过命令行安全访问 Github，提供了重要的逐步指示信息。但是，如果现在执行这些操作，您获得的只是良好实践，因为您必须对用于 {{site.data.keyword.cloud_notm}} Platform（稍后将访问）的 Github 实例重复执行相关步骤。虽然使用 SSH 密钥的步骤可能较复杂，但经过练习，您也可以在 CLI 上熟练使用 SSH。

现在，转至 [GitHub Desktop](https://desktop.github.com/) 页面以下载 GitHub Desktop，然后运行安装程序。安装程序完成后，系统将提示您使用您的帐户登录到 GitHub。

在“登录”窗口中（请参阅本教程中的第一个图），输入您要公开显示的名称和电子邮件（假定您拥有的是公共帐户），以便对存储库进行任何落实。将应用程序链接到您的帐户后，可能会要求您通过 Github 帐户联机验证应用程序连接。

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

您目前还不必创建任何存储库。如果您注意到 GitHub Desktop 随附有名为 Tutorial 的存储库，请随意试用，这可帮助您熟悉操作。您刚才已完成了本教程的先决条件部分。准备好构建应用程序了吗？

## 使用 Developer Tools 创建 Node.js 入门模板应用程序
{: #tutorial-create-skeleton}

要开始在本地开发应用程序，请首先通过命令行直接登录到 {{site.data.keyword.cloud_notm}} Platform，如示例 4 所示。 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="示例 4. 用于使用 CLI Developer Tools 登录到 IBM Cloud Platform 的命令" caption-side="bottom"}
`示例 4. 用于使用 CLI Developer Tools 登录到 IBM Cloud Platform 的命令`

如果需要，可以指定可选参数：使用 -o 选项指定组织，使用 -s 选项指定空间，或者如果您使用的是联合帐户，请使用 --sso。您登录时，系统可能会要求您选择区域，在本练习中，请选择 `us-south` 作为区域，因为在本教程中稍后构建 CD 工具链时将使用该相同的选项。  

接下来，使用示例 5 中显示的命令来设置端点（如果尚未设置）。其他端点也是可以的，而且可能更适合生产用途，但目前，请使用所示的代码（如果适合您的帐户）。

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="示例 5. 用于设置帐户的 API 端点的命令。" caption-side="bottom"}
`示例 5. 用于设置帐户的 API 端点的命令`

使用示例 6 中显示的代码以及 target 命令和 --cf 选项，将 {{site.data.keyword.cloud_notm}} Platform 的 Cloud Foundry (cf) 部分设定为目标。`cf` API 嵌入在 CLI Developer Tools 中。

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="示例 6. 设置选项以使用 Cloud Foundry API。" caption-side="bottom"}
`示例 6. 设置选项以使用 Cloud Foundry API`

接下来，开始执行期待已久的任务：使用示例 7 中显示的代码开始创建 Web 应用程序。`dev` 空间是组织的缺省选项，但是您可能希望创建其他空间来隔离不同的工作，例如使“财务”与“开发”保持分开。

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="示例 7. 用于使用 IBM Cloud Developer Tools 创建应用程序的命令" caption-side="bottom"}
`示例 7. 用于使用 IBM Cloud Developer Tools 创建应用程序的命令`

使用该命令时，系统会要求您回答一系列问题。在该过程中有许多点可以返回，但如果您发觉自己迷失了方向或遗漏了某些步骤，请随时删除该目录来重新开始，或者创建其他目录用于测试和探索。此外，在命令行上完成本地创建应用程序的过程时，可以稍后在 {{site.data.keyword.cloud_notm}} 联机门户网站（在其中创建了帐户来管理已创建的资源）中联机查看结果。

在示例 8 中，记下用于创建“Web 应用程序”的选项 &mdash; 这是您需要的选项。请输入“2”，然后按 Enter 键。

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
{: caption="示例 8. `ibmcloud dev create` 命令的输出，在其中选择选项 #2，这表示 Web 应用程序" caption-side="bottom"}
`示例 8. ibmcloud dev create 命令的输出，在其中选择选项 #2，这表示 Web 应用程序`

示例 9 中有多个基于称为“buildpack”的对象的选项，并请注意表示使用“Node”的选项。请输入“4”，然后按 Enter 键。

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
{: caption="示例 9. `ibmcloud dev create` 继续输出的语言选项。" caption-side="bottom"}
`示例 9. ibmcloud dev create 继续输出的语言选项`

在选择了编程语言和/或框架后，示例 10 中显示的下一个选择将有许多选项，可能会滚过您所需的服务。正如您在示例中看到的，我们希望使用简单的 Node.js Web App with Express.js。因此，请输入“6”，然后按 Enter 键。

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
{: caption="示例 10. `ibmcloud dev create` 输出的框架应用程序选项。" caption-side="bottom"}
`示例 10. ibmcloud dev create 输出的框架应用程序选项`

既然您已选择了更直接的选项，接下来仍然需要决定全球各地开发者都感到最难的选项：对应用程序命名。请遵循示例 11 中显示的示例并输入“webapplication”，然后按 Enter 键。

```bash
? Enter a name for your application> webapplication
```
{: caption="示例 11. 使用 `ibmcloud dev create` 将应用程序命名为“webapplication”。" caption-side="bottom"}
`示例 11. 使用 ibmcloud dev create 将应用程序命名为“webapplication”`

日后，可以根据需要通过 Web 控制台来添加任意数量的服务（如数据存储或计算函数）。但是，如示例 12 所示，如果系统询问您此时是否要添加服务，请输入“n”表示否。

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="示例 12. 使用 `ibmcloud dev create` 继续输出的用于添加服务的选项。" caption-side="bottom"}
`示例 12. 使用 ibmcloud dev create 继续输出的用于添加服务的选项`

先前提到过 Docker 的优点是使用容器进行开发，而不是使用传统的服务器，甚至虚拟服务器进行开发。一种管理容器的方法是使用编排软件，如 Kubernetes，该软件现已成为开发中的_实际_标准。但是对于本教程，我们可以允许 Cloud Foundry 服务管理单个 Docker 容器，该容器将包含应用程序所需的代码、库和配置。

如示例 13 所示，输入“1”并按 Enter 键以使用“IBM DevOps”，用于将 CD 集成到项目生命周期内。
 
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
{: caption="示例 13. `ibmcloud dev create` 输出的部署选项。" caption-side="bottom"}
`示例 13. ibmcloud dev create 输出的部署选项`

如前所述，将为自动部署 CD 工具链选择区域，因此请选择与先前相同的选项“5”，如示例 14 所示。

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
{: caption="示例 14. 在 `ibmcloud dev create` 中作为选项提供的区域。" caption-side="bottom"}
`示例 14. 在 ibmcloud dev create 中作为选项提供的区域`

此时，生成新应用程序将提醒我们，稍后用于部署应用程序的工具链将需要一些额外的配置，如示例 15 所示。如前所述，需要将公用密钥上传到 Github（在 {{site.data.keyword.cloud_notm}} Platform 的 CD 工具链实例上），以使用 Github 来交付已部署的应用程序。部署应用程序并登录到 IBM Cloud GitLab 帐户后，在 [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair) 上可以找到其他指示信息。

```

Note: For successful connection to the DevOps toolchain, this machine
must be configured for SSH access to your IBM Cloud GitLab account at
https://git.ng.bluemix.net/profile/keys in order to download the
application code.


```
{: caption="示例 15. 针对通过 `ibmcloud dev create` 命令使用的 SSH 密钥提供的注释。" caption-side="bottom"}
`示例 15. 针对通过 ibmcloud dev create 命令使用的 SSH 密钥提供的注释`

进一步的提示将确认您先前定义的应用程序和工具链名称。示例 16 显示了可以如何变更主机和工具链名称（如果需要）。对于用作应用程序服务端点的域，主机名必须唯一，但如果没有冲突，那么在要求确认时，您只需要按 Enter 键即可。

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="示例 16. 在 `ibmcloud dev create` 中确认属性的名称。" caption-side="bottom"}
`示例 16. 在 ibmcloud dev create 中确认属性的名称`

如果复制并粘贴因使用 `ibmcloud dev create` 命令而收到的输出末尾所提供的链接，您将能够访问 CD 工具链。但是，如果未捕获到该链接，也可以稍后通过控制台进行访问。随着该过程继续，后面将提供进一步信息，如示例 17 所示，其中已联机创建应用程序条目，并且已创建包含样本代码的目录。 

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
{: caption="示例 17. 确认 `ibmcloud dev create` 生成的操作。" caption-side="bottom"}
`示例 17. 确认 ibmcloud dev create 生成的操作`

示例 17 中的最后一句话表示，如果查看当前目录，现在应该会显示一个新的子目录 `webapplication`。在 `webapplication` 目录中，您将找到新的 Node.js 应用程序的脚手架。但是，虽然可能提供了食谱，但食材本身仍包装在 Docker 映像中，必须使用示例 18 中的命令进行“炖煮”&mdash; 抱歉，应该是构建。Docker 安装后，应该正在本地计算机上运行，但如果您需要重新启动 Docker，请执行重新启动。在 Docker 未运行的情况下构建新 Web 应用程序的任何尝试都会失败，但这并不是构建失败的唯一可能的原因。如果有任何问题，请检查生成的错误消息，这些消息可能具有相应的链接，可用于在 {{site.data.keyword.cloud_notm}} Platform 帐户的联机门户网站中查看结果日志。

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="示例 18. {{site.data.keyword.cloud_notm}} Platform build 命令" caption-side="bottom"}
`示例 18. IBM Cloud Platform build 命令`

除了构建应用程序用于交付外，通过构建应用程序，还可以使用 `run` 命令在本地运行相同的代码（复制并粘贴或输入示例 19 中的命令后）。完成后，请将提供的 URL 复制并粘贴到浏览器的地址栏中，通常为 <http://localhost:3000>。

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="示例 19. 用于运行应用程序的 {{site.data.keyword.cloud_notm}} Platform CLI 命令" caption-side="bottom"}

既然已创建并定义了应用程序，接下来请查看应用程序以确认它是否工作正常。如果看到图 2 中显示的占位符图像，那么祝贺您！您已创建新的 Node.js Web 应用程序，并已准备好将其部署到云。

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="图 2. 新的 Node.js 应用程序：祝贺您！" caption-side="top"}

使用 deploy 命令将应用程序部署到 {{site.data.keyword.cloud_notm}} Platform（如示例 20 所示）。

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="示例 20. 用于上传和部署应用程序的 {{site.data.keyword.cloud_notm}} Platform CLI 命令" caption-side="bottom"}
`示例 20. 用于上传和部署应用程序的 IBM Cloud Platform CLI 命令`

根据先前指定的区域端点和主机名来运行 `ibmcloud dev deploy` 命令，将再次显示该 URL。如果发生任何问题，可以在 {{site.data.keyword.cloud_notm}} Platform 上查看门户网站中所存储日志的链接。如果没有任何问题，那么在浏览器中看到的显示内容应该与您刚才访问过的本地应用程序完全相同。请接着访问云中的新 Web 应用程序！

## 使用样本应用程序创建 Web Gallery 应用程序
{: #tutorial-create-app}

让我们回想一下在 {{site.data.keyword.cloud_notm}} Platform 上开发 Node.js 应用程序所需的先决条件。您已创建了 {{site.data.keyword.cloud_notm}} Platform 帐户，并且安装了 Developer Tools（已用于安装 Docker）。然后，安装了 Node.js。作为本教程的先决条件列出的最后一项是 Git，这正是我们现在要深入研究的。  

下面将开始说明对 Node.js 中的图像库执行操作的具体信息。目前，我们对于此场景将使用 Github Desktop，但您也可以使用 Git 命令行客户机来完成相同的任务。首先，为新的 Web 应用程序克隆入门模板。 

请执行以下步骤：

1.  克隆示例 21 中列出的存储库。使用 Git 在本地开发环境上下载应用程序的模板。不要通过 {{site.data.keyword.cloud_notm}} Platform 克隆样本应用程序，请改为使用示例 21 中的命令来克隆 {{site.data.keyword.cos_full_notm}} Web Gallery 应用程序的入门模板。克隆存储库后，在 COS-WebGalleryStart 目录中可找到该入门模板应用程序。打开 Git CMD 窗口，并切换到要在其中克隆 Github 存储库的目录。使用本教程的第一个示例中显示的命令。

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="示例 21. Git clone 命令详细信息" caption-side="bottom"}
`示例 21. Git clone 命令详细信息`

2.  在本地运行应用程序。打开提供 CLI 的终端应用程序，并将工作目录切换到 COS-WebGalleryStart 目录。请注意 package.json 文件中列出的 Node.js 依赖项。使用示例 22 中显示的下一个命令将其下载到相应位置。

```bash
npm install
```
{: codeblock}
{: caption="示例 22. Node Package Manager (npm) 安装" caption-side="bottom"}
`示例 22. Node Package Manager (npm) 安装`

3.  使用示例 23 中显示的命令来运行应用程序。

```bash
npm start
```
{: codeblock}
{: caption="示例 23. 关于使用 npm 启动应用程序的详细信息" caption-side="bottom"}
`示例 23. 关于使用 npm 启动应用程序的详细信息`

打开浏览器，并使用输出到控制台的地址和端口 (<http://localhost:3000>) 来查看应用程序。

**提示**：要在本地重新启动应用程序，请终止节点进程 (Ctrl+C) 以将其停止，然后再次使用 `npm start`。但是，在开发新功能期间，使用 nodemon 以在检测到更改时重新启动应用程序可节省时间。以全局方式安装 nodemon，如下所示：`npm install -g nodemon`。然后，通过命令行在应用程序目录中使用 `nodemon` 运行此程序，以使其启动您的应用程序。

4.  开始准备应用程序以进行部署！将 `manifest.yml` 文件中的应用程序名称属性值从 COS-WebGallery 更新为您在 {{site.data.keyword.cloud_notm}} Platform 上为应用程序输入的名称，并可根据需要更新示例 24 中所示的其他信息。应用程序的 `manifest.yml` 类似于以下示例。此外，您还能以创建者身份使用应用程序名称和您的姓名来定制位于应用程序的应用程序根目录中的 `package.json` 文件。

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
{: caption="示例 24. `manifest.yml` 的内容" caption-side="bottom"}
`示例 24. manifest.yml 的内容`

**提示**：现在您可能需要设置 SSH 密钥以通过交互方式将代码推送到远程源。如果为 
SSH 密钥设置了口令，那么每次将更改推送到存储库的远程源时，都需要输入此代码。 

5.  除去 `webapplication` 目录的内容，并将其替换为刚才修改的 `COS-WebGalleryStart` 目录的内容。使用经过细致调整的 Git 技能，添加原先使用 CLI 或 Github Desktop 删除和添加到存储库的文件。然后，将更改推送到存储库源。未来，只要将更改推送到 Git 就能对基于云的 Web 应用程序进行更改。CD 工具链在克隆更改并将其存放在服务器上后，会自动神奇地重新启动服务器进程。 


实际上，我们已对应用程序重新编码，因此下面将重复构建过程。但这次将使用新的图像库代码。 

###将应用程序部署到 {{site.data.keyword.cloud_notm}} Platform。### 

要部署包含更改的入门模板应用程序
到 {{site.data.keyword.cloud_notm}} Platform，请使用 Developer Tools 通过重复先前执行的相同步骤对其进行部署。

a.  如果尚未登录到 {{site.data.keyword.cloud_notm}} Platform，或者已将其重新启动或从中注销，请使用 login 命令进行登录。请注意，在示例 25 中显示了此命令，可以根据需要指定可选参数：使用 -o 选项指定组织，使用 -s 选项指定空间，或者如果您使用的是联合帐户，请使用 --sso。如果系统要求选择区域，请务必选择您目前一直在使用的同一区域。

```bash
ibmcloud login
```
{: codeblock}
{: caption="示例 25. 用于登录到 {{site.data.keyword.cloud_notm}} Platform 的 CLI 命令" caption-side="bottom"}
`示例 25. 用于登录到 IBM Cloud Platform 的 CLI 命令`

b.  使用 api 命令为区域设置 API 端点（如示例 6 中的可选占位符所示）。如果您不知道区域 API 端点 URL，请参阅“入门”页面。

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="示例 26. {{site.data.keyword.cloud_notm}} Platform API 端点" caption-side="bottom"}
`示例 26. IBM Cloud Platform API 端点`

c.  使用示例 27 中显示的代码以及 target 命令和 --cf 选项，将 {{site.data.keyword.cloud_notm}} Platform 的 Cloud Foundry 部分设定为目标。


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="示例 27. 将 Cloud Foundry 设定为目标的 {{site.data.keyword.cloud_notm}} Platform CLI" caption-side="bottom"}
`示例 27. 将 Cloud Foundry 设定为目标的 IBM Cloud Platform CLI`

d.  使用 build 命令构建用于交付的该应用程序（如示例 28 所示）。

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="示例 28. {{site.data.keyword.cloud_notm}} Platform build 命令" caption-side="bottom"}
`示例 28. IBM Cloud Platform build 命令`

g.  下面将在本地测试应用程序。除了构建应用程序用于交付外，通过构建应用程序，还可以使用 run 命令在本地运行相同的代码（输入示例 29 中的命令后）。


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="示例 29. 用于运行应用程序的 {{site.data.keyword.cloud_notm}} Platform CLI 命令" caption-side="bottom"}
`示例 29. 用于运行应用程序的 IBM Cloud Platform CLI 命令`

h.  使用 deploy 命令将应用程序部署到 {{site.data.keyword.cloud_notm}} Platform（如示例 30 所示）。

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="示例 30. 用于上传和部署的 {{site.data.keyword.cloud_notm}} Platform CLI 命令" caption-side="bottom"}
`示例 30. 用于上传和部署的 IBM Cloud Platform CLI 命令`

示例 31 中的代码显示了此示例中用于构建、测试和部署初始 Web 应用程序的命令序列。

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
{: caption="示例 31. {{site.data.keyword.cloud_notm}} Platform CLI 命令列表" caption-side="bottom"}
`示例 31. IBM Cloud Platform CLI 命令列表`

如果成功，{{site.data.keyword.cloud_notm}} Platform 会报告应用程序已上传、已成功部署并已启动。如果您还登录到了 {{site.data.keyword.cloud_notm}} Platform Web 控制台，那么还会收到有关应用程序状态的通知。但是，最重要的是，您可以通过使用浏览器访问 {{site.data.keyword.cloud_notm}} Platform 报告的应用程序 URL，或者在 Web 控制台中通过单击“查看应用程序”按钮，验证应用程序是否已部署。

5.  测试应用程序。您可以看到创建时部署的缺省应用程序模板更改为如下所示的入门模板应用程序，这证明了已将应用程序成功部署到 {{site.data.keyword.cloud_notm}} Platform。

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### 创建 Git 分支
{: #tutorial-create-branch}

现在，需要为本地开发环境创建分支，以用于 {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline 的构建阶段：

1.  如果使用的是 GitHub Desktop，请单击“分支”图标；系统将提示您输入分支的名称（请参阅图 14）。此示例使用 Local-dev 作为分支名称。

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  创建 Local-dev 分支后，GitHub 将该分支上的本地文件与主分支上存储库中的文件进行了比较，并报告“无本地更改”。现在，可以单击“发布”以将在本地存储库上创建的该分支添加到 GitHub 存储库（如图 5 所示）。

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

既然 Local-dev 分支已发布到工具链中的 GitHub 存储库，现在只要向其推送落实，系统就会触发 {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline 的构建阶段，随后触发部署阶段。不再需要通过 CLI 来部署应用程序，因为部署已直接集成到工作流程中。

### 设置 {{site.data.keyword.cos_full_notm}} 存储器凭证
{: #tutorial-credentials}

您需要为 Web 应用程序配置 {{site.data.keyword.cos_short}} 凭证，以及将在其中存储和检索图像的“存储区”。要创建的 API 密钥将需要由[服务凭证](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials)定义的 {{site.data.keyword.cos_short}} HMAC 凭证。您可能会发现 `access_key_id` 和 `secret_access_key` 项，因为您可能有 AWS 帐户，请使用已具有 `aws_access_key_id` 和 `aws_secret_access_key` 条目的凭证文件。 

完成创建 API 密钥，下载该密钥，并复制 HMAC 凭证的操作后，请完成以下步骤：

1.  在本地开发环境中，将凭证放入 Windows 路径 `%USERPROFILE%\\.aws\\credentials`（对于 Mac/Linux 用户，凭证应该放入 `~/.aws/credentials`）。示例 32 显示了典型凭证文件的内容。

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="示例 32. 在 `~/.aws/credentials` 文件中定义的凭证" caption-side="bottom"}
`示例 32. 在 ~/.aws/credentials 文件中定义的凭证`

2.  在 {{site.data.keyword.cloud_notm}} Platform 上使用 CLI 命令创建的应用程序的 Web 页面中，通过登录到 {{site.data.keyword.cloud_notm}} Platform，然后在“Cloud Foundry 应用程序”下，选择应用程序“webapplication”，以根据开发最佳实践，将必需的凭证定义为环境变量。在各选项卡中，单击“运行时”。

3.  在“运行时”窗口中，单击页面顶部的“环境变量”并滚动到“用户定义”部分，这允许您添加变量。

4.  添加两个变量：一个变量带有 access_key_id 的值，使用 `AWS_ACCESS_KEY_ID` 作为键名，另一个变量带有私钥访问密钥的值，名称为 `AWS_SECRET_ACCESS_KEY`。应用程序在 {{site.data.keyword.cloud_notm}} Platform 上运行时，将使用这些变量及其各自的值向 {{site.data.keyword.cos_short}} 实例进行认证（请参阅图 6）。完成条目后，单击“保存”，{{site.data.keyword.cloud_notm}} Platform 将自动重新启动应用程序。

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

接下来，在服务实例的 {{site.data.keyword.cos_short}} 门户网站中，添加要包含图像的存储区。此场景使用名为 `web-images` 的存储区。


## 定制 Node.js {{site.data.keyword.cos_full_notm}} 图像库 Web 应用程序
{: #tutorial-develop}

由于此示例使用的是 MVC 体系结构，因此调整项目中的目录结构来反映此体系结构是一种方便的做法，也是最佳实践。目录结构中包含 views 目录（用于包含 EJS 视图模板）、routes 目录（用于包含 Express 路径）和 controllers 目录（用于放置控制器逻辑）。请将这些项放到名为 src 的父源目录下（请参阅图 7）。

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**提示**：先前克隆的存储库包含名为 COS-WebGalleryEnd 的目录。在执行后续步骤时，在首选编辑器中查看已完成应用程序的源代码可能会很有帮助。这将是完成本教程时落实并部署到 {{site.data.keyword.cloud_notm}} Platform 的“webapplication”版本。

### 设计应用程序
{: #tutorial-develop-design}

下面是用户应该能够使用简单图像库 Web 应用程序执行的两个主要任务：

  - 通过 Web 浏览器将图像上传到 {{site.data.keyword.cos_short}} 存储区。
  - 在 Web 浏览器中查看 {{site.data.keyword.cos_short}} 存储区中的图像。

后续步骤将重点关注如何完成这两个演示功能，而不是构建已完全开发的生产级应用程序。部署本教程，然后将其公开并运行意味着任何发现该应用程序的人都可以执行相同的操作：将文件上传到 {{site.data.keyword.cos_full_notm}} 存储区，并在其浏览器中查看已存在的任何 JPEG 图像。

### 开发应用程序
{: #tutorial-develop-app}

在 `package.json` 文件的 scripts 对象中，您会看到“start”是如何定义的（示例 33）。{{site.data.keyword.cloud_notm}} Platform 使用此文件来指示节点在每次应用程序启动时运行 app.js。此外，在本地测试应用程序时，也可以使用此文件。请查看名为 app.js 的主应用程序文件。这是我们指示 Node.js 在使用 `npm start` 命令（或 nodemon）启动应用程序时首先处理的代码。 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="示例 33. 指示应用程序如何引导定制代码" caption-side="bottom"}
`示例 33. 指示应用程序如何引导定制代码`

app.js 文件以示例 34 中显示的代码开头。首先，代码使用 node 来装入启动所需的模块。Express 框架将应用程序创建为单项，并将其简单地命名为 `app`。示例最后（目前省去了大部分代码）是指示应用程序侦听分配的端口和环境属性，缺省情况下为 3000。成功启动后，会将包含服务器 URL 的消息显示到控制台。

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
{: caption="示例 34. Web 应用程序虽然一开始很简单，但实际功能很强大" caption-side="bottom"}
`示例 34. Web 应用程序虽然一开始很简单，但实际功能很强大`

下面来看看显示的示例 35 如何定义路径和视图。第一行代码指示 Express 框架使用 public 目录来提供静态文件，这些文件包含我们使用的所有静态图像和样式表。后续行指示应用程序在 src/views 目录中查找视图的模板，并将视图引擎设置为 EJS。此外，框架还将使用 body-parser 中间件，将针对应用程序的入局请求数据作为 JSON 公开。在示例的结束行中，Express 应用程序通过呈现 index.ejs 视图模板来响应对应用程序 URL 发出的所有入局 GET 请求。

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
{: caption="示例 35. Web 应用程序视图和模板位置" caption-side="bottom"}
`示例 35. Web 应用程序视图和模板位置`

下图显示了呈现并发送到浏览器时的索引视图模板。如果使用的是 `nodemon`，那么您可能已注意到，在保存更改时浏览器已刷新，并且应用程序应该类似于图 8。

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

在示例 36 中，视图模板共享 &lt;head&gt;...&lt;/head&gt; 标记之间的 HTML 代码，因此我们在其中放入了单独的 include 模板（请参阅示例 16）。此模板 (head-inc.ejs) 包含用于第 1 行页面标题的 scriptlet（JavaScript 变量的绑定）。`title` 变量在 `app.js` 中进行设置，并在该变量下面的行中作为视图模板的数据传入。除此之外，我们简单地使用一些 CDN 地址来拉入引导程序 CSS、引导程序 JavaScript 和 JQuery。最后，将从 pubic/style sheets 目录添加定制静态 styles.css 文件。

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
{: caption="示例 36. head-inc.ejs 中的 HTML 元素" caption-side="bottom"}
`示例 36. head-inc.ejs 中的 HTML 元素`

索引视图的主体包含引导程序样式的导航选项卡（请参阅示例 37），以及引导程序随附的 CSS 样式所提供基本布局中的上传表单。

对于应用程序，请考虑以下两个规范：

-   在第 24 行上，将表单方法设置为 POST，并将表单数据编码类型设置为 multipart/form-data。对于表单操作，是将表单中的数据发送到应用程序路径“/”。稍后，我们会在路由器逻辑中执行其他操作，以处理对该路径的 POST 请求。

-   我们希望向用户显示已尝试文件上传的状态的相关反馈。此反馈会在名为“status”的变量中传递到视图，并且会显示在上传表单的下方。

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
{: caption="示例 37. index.ejs 中的 HTML 元素" caption-side="bottom"}
`示例 37. index.ejs 中的 HTML 元素`

下面将稍花点时间来返回到示例 38 中的 `app.js`。该示例设置了 Express 路径，用于处理将对应用程序发出的其他请求。这些路由方法的代码将位于项目的 `./src/routes` 目录下的两个文件中：

-   imageUploadRoutes.js：此文件处理用户选择图像并单击“上传”时执行的操作。

-   galleryRoutes.js：此文件处理用户单击“图库”选项卡以请求 imageGallery 视图时的请求。

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
{: caption="示例 38. Node Express 路由器示例" caption-side="bottom"}
`示例 38. Node Express 路由器示例`

#### 图像上传
{: #tutorial-develop-image-upload}

请参阅示例 39 中“imageUploadRoutes.js”中的代码。首先必须创建新 Express 路由器的实例，并将其命名为 `imageUploadRoutes`。稍后，我们将创建用于返回 `imageUploadRoutes` 的函数，并将其分配给名为 `router` 的变量。完成后，必须将该函数导出为模块，以使其可供 app.js 中的框架和主代码访问。要使路由逻辑与上传逻辑分开，需要名为 galleryController.js 的控制器文件。由于该逻辑专用于处理入局请求并提供相应的响应，因此我们将该逻辑放入该函数中，并将其保存在 ./src/controllers 目录中。

imageUploadRoutes 设计为在使用 HTTP POST 方法时，从 Express 框架中的路由器实例路由对根应用程序路径（“/”）的请求。在 imageUploadRoutes 的 `post` 方法中，我们将使用 `multer` 和 `multer-s3` 模块中由 galleryController 公开为 `upload` 的中间件。该中间件采用上传表单 POST 中的数据和文件，对其进行处理，然后运行回调函数。在回调函数中，我们将检查是否收到 HTTP 状态码 200，以及请求对象中是否至少有一个文件要上传。根据这些条件，我们在 `status` 变量中设置反馈，并呈现具有新状态的索引视图模板。

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
{: caption="示例 39. Node Express 路由器详细信息" caption-side="bottom"}
`示例 39. Node Express 路由器详细信息`

相比之下，示例 40 中的 galleryRouter 的代码是一个简单的模型。我们将遵循对 imageUploadRouter 使用的相同模式，并且在函数第一行上需要 galleryController，然后设置路径。主要区别在于，我们将路由的是 HTTP GET 请求（而不是 POST 请求），并且将发送来自 getGalleryImages 的响应中的所有输出，这些输出由该示例最后一行中的 galleryController 公开。

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
{: caption="示例 40. Node Express 路由器详细信息" caption-side="bottom"}
`示例 40. Node Express 路由器详细信息`

接下来，我们将关注图库的控制器。

请注意我们如何在示例 41 中设置 `multer` 上传（其中截断了一些目前将忽略的代码）。我们需要 `ibm-cos-sdk`、`multer` 和 `multer-s3` 模块。代码显示了如何配置指向 {{site.data.keyword.cos_short}} 服务器端点的 S3 对象。为了简单起见，我们将以静态方式设置端点地址、区域和存储区等值，但这些值可以通过环境变量或 JSON 配置文件轻松引用。

我们定义了如示例 41 中所使用并在 imageUploadRouter 中定义的 `upload`，方法是创建新的 `multer` 实例并将 `storage` 作为其唯一属性。此属性指示 `multer` 从 multipart/form-data 中发送文件的位置。由于 {{site.data.keyword.cloud_notm}} Platform 使用的是 S3 API 的实现，因此我们将 storage 设置为 `s3-multer` 对象。此 `s3-multer` 对象包含先前已分配给 `s3` 对象的 `s3` 属性，以及已分配给 `myBucket` 变量的存储区属性，此变量的值为“web-images”。现在，`s3-multer` 对象具有从上传表单接收数据时，连接文件并将其上传到 {{site.data.keyword.cos_short}} 存储区所需的所有数据。上传的对象的名称或键将是文件对象存储在 {{site.data.keyword.cos_short}} 的“web-images”存储区中时，从该文件对象获取的原始文件名。 

**提示**：使用时间戳记作为文件名的一部分，以保持文件名的唯一性。 

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
{: caption="示例 41. Node Express 控制器详细信息" caption-side="bottom"}
`示例 41. Node Express 控制器详细信息`

对于本地测试，一个非常有用的任务是将文件对象显示到控制台：`console.log(file)`。我们对上传表单执行了本地测试，这显示了示例 42 中文件的控制台日志中的输出。

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="示例 42. 调试对象的控制台显示" caption-side="bottom"}
`示例 42. 调试对象的控制台显示`

虽说没什么好显摆的，但图 9 显示了来自回调的反馈，其中声明在测试时应用程序确实“已成功上传文件”。

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### 图像检索和显示
{: #tutorial-image-display}

还记得前面在 app.js 中，代码行 `app.use('/gallery', galleryRouter);` 指示 Express 框架在请求“/gallery”路径时使用该路由器。如果您还记得，该路由器使用的是 galleryController.js（请参阅示例 43 中的代码），我们定义了 getGalleryImages 函数，我们先前已经看到该函数的特征符。我们使用为图像上传函数设置的同一 `s3` 对象，调用了名为 `listObjectsV2` 的函数。此函数返回定义存储区中每个对象的索引数据。要在 HTML 中显示图像，需要 `web-images` 存储区中的每个 JPEG 图像的图像 URL，才能在视图模板中显示。包含 `listObjectsV2` 返回的数据对象的闭包包含有关存储区中每个对象的元数据： 

代码遍历 `bucketContents` 来搜索以“.jpg”结尾的任何对象键，然后创建用于传递到 S3 getSignedUrl 函数的参数。提供对象的存储区名称和密钥时，此函数会返回任何对象的已签名 URL。在回调函数中，我们将每个 URL 保存在数组中，然后将其作为名为 `imageUrls` 的属性的值，传递到 HTTP 服务器响应方法 `res.render`。

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
{: caption="示例 43. galleryController.js 的部分内容" caption-side="bottom"}
`示例 43. galleryController.js 的部分内容`

最后一个代码示例（本教程中的示例 44）显示了 galleryView 模板的主体，其中包含显示图像所需的代码。我们通过 res.render() 方法获取了 imageUrls 数组，然后迭代一对嵌套的 &lt;div&gt;&lt;/div&gt; 标记，其中请求 /gallery 路径时，图像 URL 将对图像发出 GET 请求。

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
{: caption="示例 44. 图库模板中使用的循环和输出 scriptlet" caption-side="bottom"}
`示例 44. 图库模板中使用的循环和输出 scriptlet`

我们通过 http://localhost:3000/gallery 在本地对其进行了测试，并看到图 10 中的图像。

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### 落实到 Git
{: #tutorial-develop-commit}

既然应用程序的基本功能已在正常运行，接下来要将代码落实到本地存储库，然后将其推送到 GitHub。使用 GitHub Desktop，单击“更改”（请参阅图 11），在“摘要”字段中输入更改的摘要，然后单击“落实到 Local-dev”。 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

单击“同步”后，落实会发送到已发布到 GitHub 的远程 Local-dev 分支，此操作将启动 Delivery Pipeline 中的“构建阶段”，然后启动“部署阶段”，如本教程中的最后一个图（图 12）所示。 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## 后续步骤
{: #nextsteps}

祝贺您！我们已从头至尾完成了使用 {{site.data.keyword.cloud_notm}} Platform 来构建 Web 应用程序图像库的过程。在此基本简介中涵盖的每个概念都可在 [{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/) 上做进一步的探索。 

祝您好运！
