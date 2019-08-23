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

# 튜토리얼: 이미지 갤러리 웹 애플리케이션
{: #web-application}

웹 애플리케이션 빌드는 처음부터 끝까지 다양한 개념을 다루며, {{site.data.keyword.cos_full}}의
기능을 파악하는 매우 좋은 방법입니다. 이 튜토리얼에서는 {{site.data.keyword.cloud}} 플랫폼에서
간단한 이미지 갤러리를 빌드하는 방법과 다양한 개념 및 기법을 조합하는 방법을
보여줍니다. 이 애플리케이션은 {{site.data.keyword.cos_full_notm}}를, 사용자가
JPEG 이미지 파일을 업로드하고 볼 수 있도록 하는 Node.js 애플리케이션의 백엔드 서버로 사용합니다. 

## 시작하기 전에
{: #wa-prereqs}

웹 애플리케이션 빌드를 위한 전제조건으로서, 다음 항목을 확보하는 것으로 작업을 시작합니다. 

  - {{site.data.keyword.cloud_notm}} 플랫폼 계정
  - Docker({{site.data.keyword.cloud_notm}} Developer Tools의 일부)
  - Node.js 
  - Git(Desktop 앱 및 명령행 인터페이스(CLI) 모두)

### Docker 설치
{: #tutorial-wa-install-docker}

기존 서버 인스턴스 또는 가상 서버를 사용한 웹 애플리케이션 빌드에서
Docker와 같은 컨테이너를 사용한 빌드로 전환하면 자동화된 배치를 지원하는 동시에 개발 속도가
빨라지고 테스트가 용이해집니다. 컨테이너는 운영 체제와 같은 추가 오버헤드가 필요하지 않은
경량 구조로 코드, 그리고 종속 항목 및 설정과 같은 모든 항목에 대한 구성만을 포함합니다. 

먼저 숙련된 개발자에게는 익숙하며, 막 입문하는 개발자는 숙지해야 하는 도구인
명령행을 여십시오. 그래픽 사용자 인터페이스(GUI)가 개발된 후로 컴퓨터의 명령행
인터페이스는 2등의 위치로 격하된 상태가 이어져 왔습니다. 그러나 지금은 이를 다시 사용할 때입니다(그렇다고 해서
GUI를 사용하지 않는 것은 아니며, 새 명령행 도구 세트를 다운로드하기 위해 웹을 탐색하려면 여전히 이를 사용해야 함).  

터미널, 또는 자신의 운영 체제에서 사용하는 기타 명령행 인터페이스를 열고,
사용 중인 특정 쉘의 명령을 사용하여 디렉토리를 작성하십시오. 참조 디렉토리를 방금
작성한 새 디렉토리로 변경하십시오. 애플리케이션이 작성되면 해당 디렉토리에 이를 시작하고
실행하는 데 필요한 스타터 코드 및 구성을 포함하는 서브디렉토리가 작성됩니다. 

명령행을 종료하고 브라우저로 돌아와서, 링크에 있는 [{{site.data.keyword.cloud_notm}} 플랫폼 Developer Tools](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) 설치 지시사항을 따르십시오.
Developer Tools는 클라우드 애플리케이션을 빌드하고 배치하는 데 대한, 확장 및 반복 가능한 접근법을 제공합니다. 

[Docker](https://www.docker.com)는 Developer Tools의 일부로서 설치되는 필수 항목으로,
새 앱을 스캐폴딩하는 루틴에서 대부분의 작업을 백그라운드로 수행합니다. build 명령이 작동하려면 Docker가 실행 중이어야
합니다. [Dockerhub](https://hub.docker.com)에서 온라인으로 Docker 계정을 작성하고, Docker 앱을 실행하고 로그인하십시오. 

### Node.js 설치
{: #tutorial-wa-install-node}

빌드할 앱에서는 이 웹 애플리케이션의 JavaScript 코드를 실행하기 위한 서버 측 엔진으로 [Node.js](https://nodejs.org/)를
사용합니다. Node.js에 포함된 Node Package Manager(npm)를 사용하여 앱의 종속 항목을 관리하려면
Node.js를 로컬에 설치해야 합니다. 또한 Node.js를 로컬에 설치하면 테스트 작업이 간소화되므로
개발 속도가 빨라집니다.  

시작하기 전에, 여러 Node.js 버전을 관리하는 작업을 용이하게 하기 위해 Node Version Manager(`nvm`)와
같은 버전 관리자를 사용하여 Node.js를 설치하는 것이 좋습니다. 이 튜토리얼의 작성 시점에서, Mac 또는 Linux 시스템에서 `nvm`을 설치하거나
업데이트하려는 경우에는 처음 두 예에 있는 명령 중 하나를 복사하고 명령행에 붙여넣은 후 Enter를 눌러, 방금 연 CLI에서 cURL을 사용하여
설치 스크립트를 사용할 수 있습니다(여기서는 사용자의 쉘이 다른 쉘이 아니라 BASH라고 가정함을 참고). 

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="예 1. cURL을 사용한 Node Version Manager(nvm) 설치" caption-side="bottom"}
`예 1. cURL을 사용한 Node Version Manager(nvm) 설치`
   
...또는 Wget을 사용할 수도 있습니다(두 방법 중 하나만 필요하며, 자신의 시스템에서 사용 가능한 쪽을 사용할 것). 

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="예 2. Wget을 사용한 Node Version Manager(nvm) 설치" caption-side="bottom"}
`예 2. Wget을 사용한 Node Version Manager(nvm) 설치`

또는, Windows의 경우 링크에 있는 설치 프로그램 및 소스 코드를 사용하여
[nvm for Windows](https://github.com/coreybutler/nvm-windows)를 사용할 수도 있습니다. 

여러 Node.js 릴리스를 지원함으로 인해 작업이 복잡해지지 않도록 하려는 경우에는
[Node.js](https://nodejs.org/en/download/releases/) 웹 사이트에
방문하여 현재 {{site.data.keyword.cloud_notm}} 플랫폼에서 사용되고 있는
SDK for Node.js 빌드팩에 대응하는 Node.js의 장기 지원(LTS) 버전을
설치하십시오. 이 튜토리얼의 작성 시점에서 최신 빌드팩은 v3.26이며,
이는 Node.js Community Edition v6.17.0+를 지원합니다.  

최신 {{site.data.keyword.cloud_notm}} SDK for Node.js 빌드팩에 대한 추가 정보는
[SDK for Node.js 최신 업데이트](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates) 페이지에서 찾을 수 있습니다.  

예 3에 있는 명령을 복사하고 명령행에 붙여넣음으로써, `nvm`을 사용하여 요구사항에 해당하는 Node.js 버전을
설치할 수 있습니다. 

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="예 3: 'nvm'을 사용한 특정 Node.js 버전 설치" caption-side="bottom"}
`예 3: nvm을 사용한 특정 Node.js 버전 설치`

어느 접근법을 사용하든, 사용 중인 운영 체제 및 전략에 맞는
Node.js 설치 지시사항에 따라 npm(Node.js에 포함되어 있음)을 컴퓨터에 설치한 후에는
다음 단계로 진행하십시오. 

### Git 설치
{: #tutorial-wa-install-git}

Git은 웹용 애플리케이션을 빌드하는 개발자들 사이에서 가장 널리
사용되는 소스 코드 버전화 시스템으로, 이미 이에 대해 잘 알고 있는 분도 계실 것입니다.
여기서는 나중에 지속적 딜리버리 및 배치를 위해 {{site.data.keyword.cloud_notm}} 플랫폼에서
지속적 배치(CD) 도구 체인을 작성할 때 Git을 사용합니다. GitHub 계정이 없는 경우에는
[GitHub](https://github.com/join) 웹 사이트에서 무료 공용/개인용 계정을
작성하고, 그렇지 않은 경우에는 보유하고 있는 계정으로 로그인하십시오. 

명령행에서 GitHub에 안전하게 액세스하기 위해 SSH 키를 생성하고 이를 자신의
[GitHub 프로파일](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)에 업로드하는 방법에 대한, 중요한 단계별 지시사항이 있다는 점을 참고하십시오. 그러나
이를 지금 수행하는 경우 이는 연습일 뿐이며, 나중에 액세스할 {{site.data.keyword.cloud_notm}} 플랫폼에 대해 사용되는
GitHub 인스턴스에 대해 해당 단계를 반복해야 합니다. SSH 키 사용 단계는 복잡하지만,
연습을 반복하면 CLI에서 SSH를 사용하는 방법을 숙지할 수 있습니다. 

지금은 [GitHub Desktop](https://desktop.github.com/) 페이지로 이동하여
GitHub Desktop을 다운로드한 후 설치 프로그램을 실행하십시오. 설치 프로그램이 완료되면
자신의 계정으로 GitHub에 로그인하도록 프롬프트가 표시됩니다. 

로그인 창(이 튜토리얼의 첫 번째 그림 참조)에서, 저장소로의 모든 커미트에
대해 공개적으로 표시할 이름 및 이메일(공용 계정을 보유하고 있다고 가정함)을
입력하십시오. 애플리케이션을 자신의 계정에 링크하고 나면 온라인으로 자신의 GitHub 계정을 통해
애플리케이션 연결을 확인해달라는 요청을 받습니다. 

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

아직은 저장소를 작성하지 않아도 됩니다. GitHub Desktop에 포함되어 있는 Tutorial이라는 저장소에서
여러 기능을 실험해 보면 오퍼레이션을 숙지하는 데 도움이 됩니다. 이제 이 튜토리얼의 전제조건 부분을
완료했습니다. 앱을 빌드할 준비가 되셨습니까? 

## Developer Tools를 사용한 Node.js 스타터 앱 작성
{: #tutorial-create-skeleton}

로컬로 애플리케이션 개발을 시작하려면, 예 4에 표시되어 있는 바와 같이 명령행에서 직접 {{site.data.keyword.cloud_notm}} 플랫폼에 로그인하여
작업을 시작하십시오.  

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="예 4. CLI Developer Tools를 사용하여 IBM Cloud 플랫폼에 로그인하는 명령" caption-side="bottom"}
`예 4. CLI Developer Tools를 사용하여 IBM Cloud 플랫폼에 로그인하는 명령`

원하는 경우에는 선택적 매개변수(조직은 옵션 -o, 영역은 옵션 -s, 연합 계정을 사용하고 있는 경우에는
--sso)를 지정할 수 있습니다. 로그인하면 지역을 선택해달라는 요청을 받을 수 있으며, 이 튜토리얼의 목적을 위해서는
지역으로 `us-south`를 선택하십시오. 나중에 이 튜토리얼에서 CD 도구 체인을 빌드할 때도 동일한 옵션을
사용합니다.   

그 다음에는 예 5에 표시되어 있는 명령을 사용하여 엔드포인트를 설정하십시오(아직 설정되지 않은 경우). 다른 엔드포인트도 사용할 수 있으며
프로덕션용으로는 다른 엔드포인트가 바람직할 수도 있으나, 지금은 자신의 계정에 대해 적절한 경우 표시되어 있는 코드를 사용하십시오. 

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="예 5. 계정의 API 엔드포인트를 설정하는 명령" caption-side="bottom"}
`예 5. 계정의 API 엔드포인트를 설정하는 명령`

예 6에 표시되어 있는 코드(target 명령과 --cf 옵션 사용)를 사용하여 {{site.data.keyword.cloud_notm}} 플랫폼의
Cloud Foundry(cf) 부분을 대상으로 지정하십시오. `cf` API는 CLI Developer Tools에 임베드되어 있습니다. 

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="예 6. Cloud Foundry API 사용을 위한 옵션 설정" caption-side="bottom"}
`예 6. Cloud Foundry API 사용을 위한 옵션 설정`

이제 드디어 예 7에 표시되어 있는 코드로 시작하는 웹 애플리케이션을 작성합니다. `dev` 영역은
사용자의 조직에 대한 기본 옵션이지만, '재무'를 '개발'과 구분하는 것과 같이 서로 다른 작업을 격리하기 위해
여러 항목을 작성할 수 있습니다. 

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="예 7. IBM Cloud Developer Tools를 사용하여 앱을 작성하는 명령" caption-side="bottom"}
`예 7. IBM Cloud Developer Tools를 사용하여 앱을 작성하는 명령`

이 명령을 사용하면 일련의 질문을 받게 됩니다. 원하는 경우에는 이 프로세스의
여러 지점으로 되돌아갈 수 있지만, 작업이 어느 과정인지 잊어버렸거나 단계를 건너뛰었다고
생각되는 경우에는 디렉토리를 삭제하거나 테스트 및 탐색을 위한 다른 디렉토리를 작성하여
작업을 처음부터 다시 시작할 수 있습니다. 또한, 명령행에서 로컬로 애플리케이션 작성 프로세스를
완료하고 나면 나중에 작성한 리소스를 관리하기 위해 계정을 작성한 {{site.data.keyword.cloud_notm}} 온라인 포털에서
해당 결과를 볼 수 있습니다. 

예 8에서는 'Web App' 작성 옵션을 주목하십시오. 이 옵션이 선택해야 하는 항목입니다. '2'를 입력하고 Enter를 누르십시오. 

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
{: caption="예 8. 명령 'ibmcloud dev create'의 출력(여기서 Web App을 위한 옵션 2번을 선택)" caption-side="bottom"}
`예 8. 명령 ibmcloud dev create의 출력(여기서 Web App을 위한 옵션 2번을 선택)`

예 9에는 "빌드팩"이라고 하는 여러 옵션이 있으며, 여기서는 'Node' 사용 옵션을 주목하십시오. '4'를 입력하고 Enter를 누르십시오. 

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
{: caption="예 9. 위 내용에서 이어지는, 'ibmcloud dev create'의 언어 옵션" caption-side="bottom"}
`예 9. 위 내용에서 이어지는, ibmcloud dev create의 언어 옵션`

프로그래밍 언어 및 프레임워크 선택을 완료한 후에는 예 10과 같이 많은 옵션이 있는 선택사항이 표시되며,
주의하지 않으면 스크롤하다 원하는 서비스를 지나칠 수도 있습니다. 예에서 볼 수 있듯, 여기서는
Express.js를 사용하여 간단한 Node.js 웹 앱을 빌드할 것입니다. '6'을 입력하고 Enter를 누르십시오. 

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
{: caption="예 10. 'ibmcloud dev create'의 스켈레톤 애플리케이션 옵션" caption-side="bottom"}
`예 10. ibmcloud dev create의 스켈레톤 애플리케이션 옵션`

분명한 옵션들은 선택되었으며, 이제 모든 개발자에게 있어서 가장 어려운 작업인 앱 이름 지정이 남아 있습니다. 예 11에
표시된 예에 따라 'webapplication'을 입력한 후 Enter를 누르십시오. 

```bash
? Enter a name for your application> webapplication
```
{: caption="예 11. 'ibmcloud dev create'를 사용하여 애플리케이션의 이름을 'webapplication'으로 지정" caption-side="bottom"}
`예 11. ibmcloud dev create를 사용하여 애플리케이션의 이름을 'webapplication'으로 지정`

나중에 웹 콘솔을 통해 필요하거나 원하는 만큼 여러 서비스(데이터 저장소 또는 컴퓨팅 기능 등)를 추가할 수 있습니다. 그러나 지금은 서비스를 추가할 것인지에 대해 질문을 받으면 예 12에 표시되어 있는 바와 같이 'n'을 입력하여 거절하십시오. 

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="예 12. 위 내용에서 이어지는, 'ibmcloud dev create' 사용 시의 서비스 추가 옵션" caption-side="bottom"}
`예 12. 위 내용에서 이어지는, ibmcloud dev create 사용 시의 서비스 추가 옵션`

앞서 기존 서버 인스턴스 또는 가상 서버 대신 컨테이너를 사용한 개발의 장점과 관련하여 Docker가
언급되었습니다. 컨테이너를 관리하는 한 가지 방법은 Kubernetes와 같은 오케스트레이션 소프트웨어를 사용하는 것이며,
이는 개발에 있어서 _사실상_ 표준이 되었습니다. 그러나 이 튜토리얼에서는 Cloud Foundry 서비스가 앱에서 필요로 하는
코드, 라이브러리 및 구성을 포함하는 단일 Docker 컨테이너를 관리하도록 할 수 있습니다. 

예 13에 표시되어 있는 바와 같이 '1'을 입력하고 Enter를 눌러, 프로젝트 라이프사이클에 CD를 통합하기 위해 'IBM DevOps'를
사용하십시오. 
 
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
{: caption="예 13. 'ibmcloud dev create'의 배치 옵션" caption-side="bottom"}
`예 13. ibmcloud dev create의 배치 옵션`

여기서는 앞서 언급한 바와 같이 자동화된 배치 CD 도구 체인의 지역을 선택할 것이므로 이전과 동일한 옵션을
선택하십시오(예 14에 표시된 '5'). 

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
{: caption="예 14. 'ibmcloud dev create'에서 사용 가능한 지역 옵션" caption-side="bottom"}
`예 14. ibmcloud dev create에서 사용 가능한 지역 옵션`

이 시점에서 새 애플리케이션을 생성하려면 예 15에 표시되어 있는 바와 같이 나중에 앱을 배치하는 데
사용되는 도구 체인에 몇 가지 추가 구성이 필요합니다. 앞서 언급한 바와 같이,
GitHub를 사용하여 배치된 애플리케이션을 전달하려면 공용 키를 GitHub에 업로드해야
합니다({{site.data.keyword.cloud_notm}} 플랫폼의 CD 도구 체인 인스턴스에서). 애플리케이션을 배치하고 IBM Cloud GitLab 계정에 로그인하고 나면
[Generating a new SSH key pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair)에 추가 지시사항이 있습니다. 

```

Note: For successful connection to the DevOps toolchain, this machine 
must be configured for SSH access to your IBM Cloud GitLab account at 
https://git.ng.bluemix.net/profile/keys in order to download the 
application code.


```
{: caption="예 15. ibmcloud dev create 명령 실행 시 SSH 키에 대해 제공되는 참고" caption-side="bottom"}
`예 15. ibmcloud dev create 명령 실행 시 SSH 키에 대해 제공되는 참고`

앞서 정의한 애플리케이션 및 도구 체인 이름을 확인하는 추가 프롬프트가 표시됩니다. 예 16은 원하는 경우 호스트 및 도구 체인 이름을
변경하는 방법을 보여줍니다. 호스트 이름은 애플리케이션의 서비스 엔드포인트로 사용되는 도메인에서 고유해야 하지만, 충돌이 없는 경우에는
확인 요청을 받았을 때 Return 키만 누르면 됩니다. 

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="예 16. 'ibmcloud dev create'에서의 특성 이름 확인" caption-side="bottom"}
`예 16. ibmcloud dev create에서의 특성 이름 확인`

`ibmcloud dev create` 명령을 사용한 결과로 수신된 출력의 끝에 제공된 링크를 복사하여 붙여넣으면
CD 도구 체인에 액세스할 수 있습니다. 그러나 이 링크를 캡처하지 않은 경우에는 나중에 콘솔에서 여기에 액세스할 수도 있습니다.
프로세스가 진행됨에 따라, 예 17에 표시되어 있는 바와 같이 애플리케이션 항목이 온라인으로 작성된 위치 및 샘플 코드가 작성된
디렉토리와 같은 추가 정보가 이어서 표시됩니다.  

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
{: caption="예 17. 'ibmcloud dev create'에 의해 생성된 조치의 확인" caption-side="bottom"}
`예 17. ibmcloud dev create에 의해 생성된 조치의 확인`

예 17의 마지막 문장은 현재 디렉토리를 보면 새 서브디렉토리인 `webapplication`을 볼 수 있음을
의미합니다. `webapplication` 디렉토리에는 사용자의 새 Node.js 애플리케이션의 스캐폴드가 있습니다. 그러나
레시피가 있다고 해도 구성요소 자체는 여전히 Docker 이미지에 저장되어 있으며, 사용자는 예 18에 있는 명령을 사용하여
이를 빌드해야 합니다. Docker는 설치 과정을 통해 로컬 시스템에서 이미 실행 중일 것이지만, 이를 다시 시작해야 하는
경우에는 다시 시작하십시오. Docker가 실행 중이 아닌 상태에서 새 웹 애플리케이션을 빌드하려는 모든 시도는 실패하지만,
실패의 원인은 이뿐만이 아닙니다. 문제가 있는 경우에는 결과 오류 메시지를 확인하십시오. 여기에는 자신의 {{site.data.keyword.cloud_notm}} 플랫폼
계정의 온라인 포털에서 결과 로그를 볼 수 있도록 하는 링크가 포함되어 있을 수 있습니다. 

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="예 18. {{site.data.keyword.cloud_notm}} 플랫폼 build 명령" caption-side="bottom"}
`예 18. IBM Cloud 플랫폼 build 명령`

전달을 위해 앱을 빌드하는 것 외에, 앱 빌드를 통해 `run` 명령으로 동일한 코드를 로컬에서
실행할 수도 있습니다(예 19의 명령을 복사하여 붙여넣거나 입력한 후). 완료되면 제공된 URL을 복사하여
브라우저의 주소 표시줄에 붙여넣으십시오(일반적으로 <http://localhost:3000>). 

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="예 19. 앱을 실행하는 {{site.data.keyword.cloud_notm}} 플랫폼 CLI 명령" caption-side="bottom"}

앱이 작성되고 정의된 후에는 애플리케이션을 보고 작동하는지 확인하십시오. 그림 2에 있는 플레이스홀더 이미지가
표시되는 경우에는 앱이 작동하는 것입니다. 이제 새 Node.js 웹 애플리케이션이 작성되어 클라우드에 배치할 준비가 되었습니다. 

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="그림 2. 새 Node.js 애플리케이션 작성 완료"}

deploy 명령으로 앱을 {{site.data.keyword.cloud_notm}} 플랫폼에 배치하십시오(예 20에 표시되어 있는 바와 같이). 

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="예 20. 앱을 업로드하고 배치하는 {{site.data.keyword.cloud_notm}} 플랫폼 CLI 명령" caption-side="bottom"}
`예 20. 앱을 업로드하고 배치하는 IBM Cloud 플랫폼 CLI 명령`

`ibmcloud dev deploy` 명령을 실행하면 그 결과로 앞서 지정한 지역 엔드포인트 및 호스트 이름에 따라
URL이 다시 표시됩니다. 문제가 있는 경우에는 {{site.data.keyword.cloud_notm}} 플랫폼 포털에 저장된 로그에 대한
링크가 표시됩니다. 문제가 없는 경우에는 방금 방문한 로컬 애플리케이션과 동일한 화면이 브라우저에
표시됩니다. 클라우드에 있는 새 웹 애플리케이션에 방문하십시오. 

## 샘플 애플리케이션을 사용한 웹 갤러리 앱 작성
{: #tutorial-create-app}

{{site.data.keyword.cloud_notm}} 플랫폼에서 Node.js 앱을 개발하는 데 필요한 전제조건을 다시 떠올려 보십시오. 이제까지의
작업으로 {{site.data.keyword.cloud_notm}} 플랫폼 계정을 작성하고, Docker를 설치하는 Developer Tools를
설치했습니다. 그 후에는 Node.js를 설치했습니다. 이 튜토리얼의 전제조건으로서 나열된 마지막 항목은 Git이며, 이제부터 이에 대해 자세히 살펴볼 것입니다.   

먼저 Node.js을 사용한 이미지 갤러리의 작동 세부사항을 결정합니다. 지금은 이 시나리오에 GitHub Desktop을
사용하지만, Git 명령행 클라이언트를 사용하여 동일한 태스크를 완료할 수도 있습니다. 작업을 시작하기 위해 새 웹 애플리케이션의
템플리트를 복제하십시오.  

다음 단계를 따르십시오. 

1.  예 21에 나열된 저장소를 복제하십시오. Git을 사용하여 앱의 템플리트를
    로컬 개발 환경에 다운로드하십시오. {{site.data.keyword.cloud_notm}} 플랫폼으로부터
    샘플 앱을 복제하지 말고, 예 21의 명령을 사용하여 {{site.data.keyword.cos_full_notm}}
    웹 갤러리 앱의 스타터 템플리트를 복제하십시오. 저장소를 복제하고 나면
    COS-WebGalleryStart 디렉토리에서 이 스타터 앱을 찾을 수
    있습니다. Git CMD 창을 열어 GitHub 저장소를 복제할
    디렉토리로 변경하십시오. 이 튜토리얼의 첫 번째 예에
    표시되어 있는 명령을 사용하십시오. 

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="예 21. Git clone 명령 세부사항" caption-side="bottom"}
`예 21. Git clone 명령 세부사항`

2.  앱을 로컬에서 실행하십시오. CLI를 제공하는 터미널 애플리케이션을 열고 작업 디렉토리를
    COS-WebGalleryStart 디렉토리로 변경하십시오. package.json 파일에 나열되어 있는 Node.js
    종속 항목에 유의하십시오. 다음 예 22에 표시되어 있는 명령을 사용하여 이들을 제자리에
    다운로드하십시오. 

```bash
npm install
```
{: codeblock}
{: caption="예 22. Node Package Manager(npm) 설치" caption-side="bottom"}
`예 22. Node Package Manager(npm) 설치`

3.  예 23에 표시되어 있는 명령을 사용하여 앱을 실행하십시오. 

```bash
npm start
```
{: codeblock}
{: caption="예 23. npm을 사용한 앱 시작에 대한 세부사항" caption-side="bottom"}
`예 23. npm을 사용한 앱 시작에 대한 세부사항`

브라우저를 열고 콘솔에 출력된 주소 및 포트(<http://localhost:3000>)에서
자신의 앱을 보십시오. 

**팁**: 앱을 로컬에서 다시 시작하려면 Node.js 프로세스를 강제 종료(Ctrl+C)하여
중지하고 `npm start`를 다시 사용하십시오. 그러나 새 기능을 개발하는 중에는 앱이 변경사항을
발견하는 경우 nodemon을 사용하여 앱을 다시 시작하면 시간이 절약됩니다. `npm install -g nodemon`
명령을 실행하여 nodemon을 글로벌 설치하십시오. 그 후에는 명령행의 앱 디렉토리에서 `nodemon`을
사용하여 'nodemon'이 앱을 시작하도록 하십시오. 

4.  앱의 배치를 준비하십시오. 필요한 경우에는 예 24에 표시되어 있는 바와 같이
    COS-WebGallery에 있는 `manifest.yml` 파일에서 애플리케이션 이름 특성 값을
    {{site.data.keyword.cloud_notm}} 플랫폼에서 앱에 대해 입력한 값으로 업데이트하고,
    기타 값 또한 업데이트하십시오. 애플리케이션 `manifest.yml`은 다음 예와 같습니다. 또한 자신의 앱 이름과
    자신의 이름(작성자로)을 사용하여 앱 루트 디렉토리에 있는 `package.json` 파일을
    사용자 정의할 수 있습니다. 

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
{: caption="예 24. 'manifest.yml'의 컨텐츠" caption-side="bottom"}
`예 24. manifest.yml의 컨텐츠`

**팁**: 이 시점이 원격 오리진에 대화식으로 코드를 푸시하기 위해 SSH 키를 설정해야 하는 시점입니다. SSH키에 대한 
    비밀번호 문구를 설정하는 경우에는 저장소에 대한 변경사항을 원격 오리진에 푸시할 때마다 이 코드를
    입력해야 합니다.  

5.  `webapplication` 디렉토리의 컨텐츠를 제거하고 방금 수정한 디렉토리인 `COS-WebGalleryStart`의 컨텐츠로 대체하십시오.
    습득한 Git 관련 지식을 사용하여, CLI 또는 GitHub Desktop으로 삭제되고 저장소에 추가된 파일을
    추가하십시오. 그 후 변경사항을 저장소 오리진에 푸시하십시오. 나중에는 변경사항을 Git에 푸시하는 것만으로
    클라우드 기반 웹 애플리케이션을 변경할 수 있게 됩니다. CD 도구 체인은 변경사항을 복제하여 서버에 저장한 후
    자동으로 서버 프로세스를 다시 시작합니다.  


본질적으로 애플리케이션을 다시 코딩했으므로 빌드 프로세스를 반복하십시오. 그러나 이번에는 새 이미지 갤러리 코드를 사용합니다.  

###앱을 {{site.data.keyword.cloud_notm}} 플랫폼에 배치하십시오. ### 

변경사항을 포함하는 스타터 앱을
    {{site.data.keyword.cloud_notm}} 플랫폼으로 가져오려면 이전에 수행한 것과 동일한 단계를 반복해 Developer Tools를 사용하여
    이를 배치하십시오. 

a.  아직 로그인하지 않은 경우, 또는 다시 시작하거나 로그아웃한 경우에는 login 명령을 사용하여 {{site.data.keyword.cloud_notm}} 플랫폼에
로그인하십시오. 이는 사용자가 잊지 않도록 예 25에 표시되어 있으며, 원하는 경우에는 선택적 매개변수(조직은 옵션 -o, 영역은 옵션 -s, 연합 계정을
사용하고 있는 경우에는 --sso)를 지정할 수 있다는 점을 참고하십시오. 지역을 묻는 경우에는 이제까지 작업하면서 사용한 것과
동일한 지역을 선택해야 한다는 점을 기억하십시오. 

```bash
ibmcloud login
```
{: codeblock}
{: caption="예 25. {{site.data.keyword.cloud_notm}} 플랫폼에 로그인하는 CLI 명령" caption-side="bottom"}
`예 25. IBM Cloud 플랫폼에 로그인하는 CLI 명령`

b.  api 명령을 사용(예 26에 선택적 플레이스홀더와 함께 표시되어 있는 바와 같이)하여
        지역의 API 엔드포인트를 설정하십시오. 지역 API 엔드포인트 URL을 모르는 경우에는
        시작하기 페이지를 참조하십시오. 

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="예 26. {{site.data.keyword.cloud_notm}} 플랫폼 API 엔드포인트" caption-side="bottom"}
`예 26. IBM Cloud 플랫폼 API 엔드포인트`

c.  예 27에 표시되어 있는 코드(target 명령과 --cf 옵션 사용)를 사용하여 {{site.data.keyword.cloud_notm}} 플랫폼의
Cloud Foundry 부분을 대상으로 지정하십시오. 


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="예 27. Cloud Foundry를 대상으로 지정하는 {{site.data.keyword.cloud_notm}} 플랫폼 CLI" caption-side="bottom"}
`예 27. Cloud Foundry를 대상으로 지정하는 IBM Cloud 플랫폼 CLI`

d.  build 명령을 사용하여 애플리케이션 전달을 위해 앱을 빌드하십시오(예 28과 같이). 

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="예 28. {{site.data.keyword.cloud_notm}} 플랫폼 build 명령" caption-side="bottom"}
`예 28. IBM Cloud 플랫폼 build 명령`

g.  앱을 로컬에서 테스트하십시오. 전달을 위해 앱을 빌드하고 나면, run 명령을 사용하여 로컬에서 동일한 코드를 실행할 수도 있습니다(예 29의
    명령을 입력한 후). 


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="예 29. 앱을 실행하는 {{site.data.keyword.cloud_notm}} 플랫폼 CLI 명령" caption-side="bottom"}
`예 29. 앱을 실행하는 IBM Cloud 플랫폼 CLI 명령`

h.  deploy 명령을 사용하여 앱을 {{site.data.keyword.cloud_notm}} 플랫폼에
배치하십시오(예 30에 표시되어 있는 바와 같이). 

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="예 30. 업로드하고 배치하는 {{site.data.keyword.cloud_notm}} 플랫폼 CLI 명령" caption-side="bottom"}
`예 30. 업로드하고 배치하는 IBM Cloud 플랫폼 CLI 명령`

예 31의 코드는 초기 웹 애플리케이션을 빌드하고, 테스트하고, 배치하기 위해 이 예에서 사용된 명령 순서를 보여줍니다. 

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
{: caption="예 31. {{site.data.keyword.cloud_notm}} 플랫폼 CLI 명령 목록" caption-side="bottom"}
`예 31. IBM Cloud 플랫폼 CLI 명령 목록`

명령 실행이 성공하는 경우 {{site.data.keyword.cloud_notm}} 플랫폼은 앱이 업로드되고,
성공적으로 배치되어 시작되었음을 보고합니다. {{site.data.keyword.cloud_notm}} 플랫폼 웹 콘솔에도 로그인한 경우에는
여기서도 앱의 상태에 대한 알림을 받습니다. 그러나 가장 중요한 것은 {{site.data.keyword.cloud_notm}} 플랫폼에 의해
보고된 앱 URL에 브라우저를 사용하여 방문하거나, 웹 콘솔에서 앱 보기 단추를 클릭하여 앱이 배치되었음을 확인할 수 있다는
것입니다. 

5.  앱을 테스트하십시오. 다음 그림에는 눈으로 확인 가능한, 스타터 앱
    작성 당시 배치된 기본 앱 템플리트로부터의 변경사항이 표시되어 있으며, 이는
    {{site.data.keyword.cloud_notm}} 플랫폼에 앱을 배치하는 작업이 성공했음을 보여줍니다. 

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### Git 분기 작성
{: #tutorial-create-branch}

이제 {{site.data.keyword.cloud_notm}} Delivery Pipeline 빌드 단계에 사용할
로컬 개발 환경의 분기를 작성해야 합니다. 

1.  GitHub Desktop을 사용하고 있는 경우에는 분기 아이콘을 클릭하십시오. 분기의
    이름을 입력하라는 프롬프트가 표시됩니다(그림 14 참조). 이 예는 Local-dev를 이름으로 사용합니다. 

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  분기를 작성하고 나면 GitHub가 Local-dev 분기의
    로컬 파일을 마스터 분기의 저장소에 있는 파일과 비교하고
    로컬 변경사항 없음을 보고합니다. 이제 공개를 클릭하여
    로컬 저장소에서 작성한 분기를 GitHub 저장소에 추가할 수
    있습니다(그림 5에 표시되어 있는 바와 같이). 

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

이제 Local-dev 분기가 도구 체인의 GitHub 저장소에 공개되었으므로,
여기에 커미트를 푸시할 때마다 {{site.data.keyword.cloud_notm}} 플랫폼
Delivery Pipeline의 빌드 단계와 배치 단계가 차례대로 트리거됩니다.
배치가 워크플로우에 직접 통합되었으므로 CLI에서의 앱 배치는 더 이상 필요하지 않습니다. 

### {{site.data.keyword.cos_full_notm}}의 스토리지 인증 정보 설정
{: #tutorial-credentials}

이제 웹 애플리케이션의 {{site.data.keyword.cos_short}} 인증 정보, 그리고 애플리케이션이 이미지를 저장하고 검색할
'버킷'을 구성해야 합니다. [서비스 인증 정보](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials)에
정의되어 있는 바와 같이, 작성되는 API 키는 {{site.data.keyword.cos_short}} HMAC 인증 정보를 필요로 합니다.
AWS 계정이 있는 사용자에게는 `access_key_id` 및 `secret_access_key`와 같은 용어가 익숙할 수 있으며,
이들은 `aws_access_key_id` 및 `aws_secret_access_key` 항목이 이미 있는 인증 정보 파일을 사용할 수 있습니다.  

API 키 작성을 완료하고, 이를 다운로드한 뒤 HMAC 인증 정보를 복사한 후에는 다음 단계를 완료하십시오. 

1.  로컬 개발 환경에서 인증 정보를 Windows 경로
    `%USERPROFILE%\\.aws\\credentials`에 배치하십시오(Mac/Linux 사용자의 경우에는 이 인증 정보를
    `~/.aws/credentials`에 배치해야 함). 예 32는 일반적인 인증 파일의
    컨텐츠를 보여줍니다. 

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="예 32. '~/.aws/credentials' 파일에 정의된 인증 정보" caption-side="bottom"}
`예 32. ~/.aws/credentials 파일에 정의된 인증 정보`

2.  {{site.data.keyword.cloud_notm}} 플랫폼에 로그인한 후
    Cloud Foundry 앱에서 자신의 앱인 'webapplication'을 선택하여, {{site.data.keyword.cloud_notm}} 플랫폼에
    CLI 명령을 사용하여 작성한 애플리케이션의 웹 페이지에서 개발 우수 사례에 따라 필수 인증 정보를
    환경 변수로 정의하십시오. 탭에서 런타임을 클릭하십시오. 

3.  런타임 창에서 페이지 맨 위에 있는 환경 변수를
    클릭하고, 변수를 추가할 수 있게 해 주는 사용자 정의 섹션으로
    스크롤하십시오. 

4.  값이 access_key_id의 값이며 이름이 `AWS_ACCESS_KEY_ID`인 변수와
    값이 secret_access_key의 값이며 이름이 `AWS_SECRET_ACCESS_KEY`인 변수를 추가하십시오.
    이러한 변수 및 해당 값은 앱이 {{site.data.keyword.cloud_notm}} 플랫폼에서
    실행될 때 {{site.data.keyword.cos_short}} 인스턴스에 인증하는 데 사용하는
    항목입니다(그림 6 참조). 항목 구성을 완료했으면 저장을 클릭하십시오.
    {{site.data.keyword.cloud_notm}} 플랫폼이 사용자 대신 앱을 자동으로 다시 시작합니다. 

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

다음으로, 서비스 인스턴스의 {{site.data.keyword.cos_short}} 포털에서
이미지를 포함할 버킷을 추가하십시오. 이 시나리오에서는 `web-images`라는 버킷을 사용합니다. 


## Node.js {{site.data.keyword.cos_full_notm}} 이미지 갤러리 웹 애플리케이션 사용자 정의
{: #tutorial-develop}

이 예에서는 MVC 아키텍처를 사용하므로, 이 아키텍처를 반영하기 위해
프로젝트의 디렉토리 구조를 조정하는 것이 작업의 우수 사례인 동시에 가장 편리합니다.
디렉토리 구조에는 EJS 뷰 템플리트를 포함하는 views 디렉토리,
Express 라우트를 포함하는 routes 디렉토리, 제어기 로직을 포함하는
controllers 디렉토리가 있습니다. 이들 항목을 src라고 하는 상위 소스
디렉토리 아래에 배치하십시오(그림 7 참조). 

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**팁**: 앞서 복제한 저장소는 COS-WebGalleryEnd라는 디렉토리를
포함하고 있습니다. 다음 단계를 따를 때는 완성된 애플리케이션의 소스 코드를 원하는
편집기에서 보는 것이 유용할 수 있습니다. 이 코드가 이 튜토리얼을 완료한 후
커미트하여 {{site.data.keyword.cloud_notm}} 플랫폼에 배치하는
'webapplication'의 버전입니다. 

### 앱 디자인
{: #tutorial-develop-design}

다음은 사용자가 간단한 이미지 갤러리 웹 애플리케이션을 사용하여 수행할 수 있어야 하는
두 가지 기본 태스크입니다. 

  - 웹 브라우저에서 {{site.data.keyword.cos_short}} 버킷으로 이미지를 업로드합니다. 
  - 웹 브라우저에서 {{site.data.keyword.cos_short}} 버킷에 있는 이미지를 봅니다. 

다음 단계에서는 완전히 개발된 프로덕션급 앱을 빌드하기보다 이러한 두 데모 기능을 개발하는 방법에
초점을 맞춥니다. 이 튜토리얼 앱을 배치하고 이를 실행 중인 상태로 노출시켜 둔다는 것은 이 앱을 찾는 모든 사용자가
동일한 조치(파일을 {{site.data.keyword.cos_full_notm}} 버킷에 업로드하고 해당 버킷에 이미 있는 JPEG 이미지를 브라우저에서 보기)를 수행할 수 있음을 의미합니다. 

### 앱 개발
{: #tutorial-develop-app}

`package.json` 파일의 scripts 오브젝트에서
"start"가 어떻게 정의되어 있는지 볼 수 있습니다(예 33). 이 파일이
앱이 시작될 때마다 {{site.data.keyword.cloud_notm}} 플랫폼이 Node.js에 app.js를 실행하도록 지시하는 데
사용하는 파일입니다. 앱을 로컬에서 테스트하는 경우에도 이 파일이 사용됩니다. app.js라는 이름의 기본 애플리케이션 파일을 살펴보십시오. 이 코드가 `npm start` 명령(또는 nodemon)으로 앱을 시작할 때 Node.js가 가장 먼저 처리하도록 지시된 코드입니다.  


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="예 33. 앱에 사용자 정의 코드 부트스트래핑 방법을 지시" caption-side="bottom"}
`예 33. 앱에 사용자 정의 코드 부트스트래핑 방법을 지시`

이 튜토리얼의 app.js 파일은 예 34에 표시되어 있는 코드로 시작합니다.
이 코드는 먼저 Node.js를 사용하여 시작하는 데 필요한 모듈을 로드합니다.
Express 프레임워크는 앱을 `app`이라는 이름의 싱글톤으로 작성합니다.
이 예는 기본적으로 3000으로 지정되며 환경 특성인 포트를 청취하도록
앱에 지시하는 부분에서 끝납니다(지금은 코드 대부분이 생략된 상태임).
성공적으로 시작되면 앱이 콘솔에 서버 URL을 포함하는 메시지를 출력합니다. 

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
{: caption="예 34. 웹 애플리케이션의 미약하지만 탄탄한 시작" caption-side="bottom"}
`예 34. 웹 애플리케이션의 미약하지만 탄탄한 시작`

예 35에서 경로 및 뷰를 정의하는 방법을 살펴보십시오. 코드의 첫 번째 행은
Express 프레임워크에 public 디렉토리를 사용하여 정적 파일을 제공하도록 지시하며,
이는 사용되는 정적 이미지 및 스타일시트를 포함합니다. 그 뒤의 행은
src/views 디렉토리에서 뷰의 템플리트를 찾을 수 있는 위치를 앱에 알려주며
뷰 엔진을 EJS로 설정합니다. 또한, 프레임워크는 body-parser 미들웨어를
사용하여 수신 요청 데이터를 JSON으로서 앱에
노출합니다. 예의 마지막 행에서, Express 앱은
index.ejs 템플리트를 렌더링하여 앱 URL에 대한 모든
수신 GET 요청에 응답합니다. 

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
{: caption="예 35. 웹 앱 뷰 및 템플리트 위치" caption-side="bottom"}
`예 35. 웹 앱 뷰 및 템플리트 위치`

다음 그림은 렌더링되어 브라우저에 전송된 인덱스 뷰 템플리트를
보여줍니다. `nodemon`을 사용하고 있는 경우에는
변경사항을 저장하면 브라우저가 새로 고쳐지는 것을 볼 수 있으며, 앱은 그림 8과 같은 모습이어야 합니다. 

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

예 36에서, 뷰 템플리트는 &lt;head&gt;...&lt;/head&gt; 태그
사이의 HTML 코드를 공유하므로 여기서는 별도의 Include 템플리트에
배치되었습니다(예 16 참조). 이 템플리트(head-inc.ejs)는 행 1에
페이지 제목에 대한 스크립틀릿(JavaScript 변수에 대한 바인딩)을 포함하고 있습니다.
`title` 변수는 `app.js`에 설정되며 그 아래에 있는
행에서 뷰 템플리트에 대한 데이터로서 전달됩니다. 그 외에는 간단히 몇 가지 CDN 주소를 사용하여
부트스트랩 CSS, 부트스트랩 JavaScript 및 JQuery를 가져옵니다. 그 후 마지막으로
public/stylesheets 디렉토리의 사용자 정의 정적 styles.css 파일을 추가합니다. 

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
{: caption="예 36. head-inc.ejs의 HTML 요소" caption-side="bottom"}
`예 36. head-inc.ejs의 HTML 요소`

인덱스 뷰의 본문은 부트스트랩 스타일의
탐색 탭(예 37 참조), 그리고 부트스트랩에 함께 포함된 CSS 스타일에서
제공하는 기본 레이아웃의 업로드 양식을 포함합니다. 

앱에 대해 다음 두 스펙을 고려하십시오. 

-   행 24에서 양식 메소드는 POST로, 양식 데이터 인코딩 유형은
    multipart/form-data로 설정합니다. 양식 조치의 경우에는
    양식에서 앱의 앱 라우트 "/"에 데이터를 전송합니다. 그 후에는
    라우터 로직에서 추가 작업을 수행하여 해당 라우트에 대한 POST 요청을
    처리합니다. 

-   여기서는 사용자가 시도한 파일 업로드의 상태에 대한 피드백을
    표시할 것입니다. 이 피드백은 "status"라는 변수로 뷰에 전달되며
    업로드 양식 아래에 표시됩니다. 

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
{: caption="예 37. index.ejs의 HTML 요소" caption-side="bottom"}
`예 37. index.ejs의 HTML 요소`

이제 예 38에서 다시 한번 `app.js`를 살펴보십시오. 이 예는 앱에 전송되는
추가 요청을 처리하기 위한 Express 라우트를 설정합니다. 이러한 라우팅 메소드의
코드는 프로젝트의 `./src/routes` 디렉토리에 있는
두 파일에 있습니다. 

-   imageUploadRoutes.js: 이 파일은 사용자가 이미지를 선택하고 업로드를 클릭하는 경우
    발생하는 상황을 처리합니다. 

-   galleryRoutes.js: 이 파일은 사용자가 갤러리 탭을 클릭하여
    imageGallery 뷰를 요청하는 경우를 처리합니다. 

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
{: caption="예 38. Node.js Express 라우터 예" caption-side="bottom"}
`예 38. Node.js Express 라우터 예`

#### 이미지 업로드
{: #tutorial-develop-image-upload}

예 39에서 'imageUploadRoutes.js'의 코드를 살펴보십시오. 여기서는 먼저
새 Express 라우터의 인스턴스를 작성하고 이름을 `imageUploadRoutes`로 지정해야 합니다.
그 후에는 `imageUploadRoutes`를 리턴하고 이를
`router`라는 변수에 지정하는 함수를 작성합니다. 작성이 완료되면
함수를 모듈로 내보내어 프레임워크 및 app.js의 주 코드에서 액세스할 수 있도록 해야 합니다.
업로드 로직과 라우팅 로직을 분리하려면 galleryController.js라는 제어기 파일이
필요합니다. 이 로직은 수신 요청을 처리하고 적절한 응답을 제공하는 작업만
전적으로 수행하므로, 여기서는 이 로직을 해당 함수에 삽입하고 이를
./src/controllers 디렉토리에 저장합니다. 

imageUploadRoutes는 HTTP POST 메소드가 사용되는 경우 루트 앱 라우트("/")에 대한 요청을
Express 프레임워크의 라우터 인스턴스로 라우팅하도록 디자인되었습니다.
imageUploadRoutes의 `post` 메소드에서는 `multer` 및 `multer-s3`
모듈의 미들웨어를 사용하며 이는 galleryController에 의해 `upload`로 노출됩니다.
이 미들웨어는 업로드 양식 POST에서 데이터 및 파일을 가져와 처리한 후
콜백 함수를 실행합니다. 이 콜백 함수에서는
HTTP 상태 코드 200이 수신되는지, 그리고
요청 오브젝트에 업로드할 파일이 하나 이상 있는지 확인합니다. 이러한 조건에
따라 `status` 변수에 피드백이 설정되고 새 상태를 사용하여
인덱스 뷰 템플리트가 렌더링됩니다. 

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
{: caption="예 39. Node.js Express 라우터 세부사항" caption-side="bottom"}
`예 39. Node.js Express 라우터 세부사항`

이에 비해, 예 40의 galleryRouter는 단순함의 전형입니다. 여기서는
imageUploadRouter의 경우와 동일한 패턴을 따르며 함수의 첫 번째 행에 galleryController가 오도록 한 후 라우트를 설정합니다. 주된 차이점은
POST가 아니라 HTTP GET 요청을 라우팅하며 getGalleryImages의
응답에 있는 모든 출력을 전송하는 것으로, 이는 이 예의
마지막 행에 있는 galleryController에 의해 노출됩니다. 

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
{: caption="예 40. Node.js Express 라우터 세부사항" caption-side="bottom"}
`예 40. Node.js Express 라우터 세부사항`

그 다음에는 갤러리의 제어기에 대해 작업을 수행합니다. 

예 41에서 어떻게 `multer` 업로드를 설정하는지
살펴보십시오(여기에는 몇몇 코드가 잘려 있으며, 지금은 편의를 위해 이를 무시할 것임). 여기서는
모듈 `ibm-cos-sdk`, `multer` 및 `multer-s3`이 필요합니다. 이 코드는
{{site.data.keyword.cos_short}} 서버 엔드포인트를 가리키는 S3 오브젝트를 구성하는 방법을 보여줍니다. 여기서는
코드를 단순하게 하기 위해 엔드포인트 주소, 지역 및 버킷의 값을 정적으로
설정하고 있지만, 이러한 값은 손쉽게 환경 변수 또는 JSON 구성 파일로부터
참조할 수도 있습니다. 

여기서는 새 `multer` 인스턴스를 작성하고 유일한 특성으로 `storage`를 사용하여,
예 41에서 사용되고 imageUploadRouter에 정의된 바와 같이 `upload`를 정의합니다. 이 특성은
`multer`에 multipart/form-data의 파일을 전송할 위치를 알려줍니다. {{site.data.keyword.cloud_notm}}
플랫폼은 S3 API의 구현을 사용하므로, 스토리지는 `s3-multer` 오브젝트로
설정합니다. 이 `s3-multer` 오브젝트는 앞서 `s3`
오브젝트에 지정된 `s3` 특성, 그리고 값 "web-images"가 지정된
`myBucket` 변수가 지정된 bucket 특성을
포함합니다. `s3-multer` 오브젝트에는 이제 업로드 양식으로부터
데이터를 수신하는 경우 {{site.data.keyword.cos_short}} 버킷에 연결하여 파일을 업로드하는 데
필요한 모든 데이터가 있습니다. 업로드되는 오브젝트의 이름 또는 키는
파일 오브젝트가 {{site.data.keyword.cos_short}} "web-images" 버킷에
저장될 때 가져온 원본 파일 이름이 됩니다.  

**팁**: 파일 이름 고유성을 유지하려면 파일 이름의 일부로 시간소인을 사용하십시오.  

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
{: caption="예 41. Node.js Express 제어기 세부사항" caption-side="bottom"}
`예 41. Node.js Express 제어기 세부사항`

로컬 테스트에 유용한 태스크는
파일 오브젝트를 콘솔에 출력하는 것입니다(`console.log(file)`).
예 42에서는 업로드 양식의 로컬 테스트를 수행하고 파일의
콘솔 로그에 있는 출력을 표시합니다. 

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="예 42. 디버그 오브젝트의 콘솔 표시" caption-side="bottom"}
`예 42. 디버그 오브젝트의 콘솔 표시`

자랑이 어울리지 않을지도 모르지만, 그림 9는 애플리케이션이 테스트에서
"파일 업로드에 성공"했음을 선언하는 콜백의 피드백을 보여줍니다. 

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### 이미지 검색 및 표시
{: #tutorial-image-display}

app.js에서 코드 행 `app.use('/gallery', galleryRouter);`는
Express 프레임워크에 "/gallery" 라우트가 요청되는 경우 해당 라우터를 사용하도록 지시한다는 점을 기억하십시오.
이 라우터는 galleryController.js를 사용하며(예 43의 코드 참조), 여기서는
앞서 시그니처를 살펴보았던 getGalleryImages 함수를 정의합니다. 이미지 업로드 함수에 대해
설정한 것과 동일한 `s3` 오브젝트가 사용되며, 이 함수의 이름은
`listObjectsV2`로 지정합니다. 이 함수는 버킷에 있는 각 오브젝트를 정의하는
인덱스 데이터를 리턴합니다. HTML 내에서 이미지를 표시하려면, 뷰 템플리트에 표시할
`web-images` 버킷의 각 JPEG 이미지에 대한 이미지 URL이 필요합니다. `listObjectsV2`에
의해 리턴된 데이터 오브젝트를 포함하는 마지막 부분에는 버킷에 있는 각 오브젝트에 대한
메타데이터가 포함되어 있습니다.  

이 코드는 `bucketContents`를 루프하면서 ".jpg"로 끝나는
오브젝트 키를 검색하고, S3 getSignedUrl 함수에 전달할 매개변수를 작성합니다. 이 함수는
오브젝트의 버킷 이름 및 키를 제공하면 오브젝트에 대한 서명된 URL을
리턴합니다. 콜백 함수에서는 각 URL을 배열에 저장하고 이를 `imageUrls`라는
특성의 값으로 HTTP 서버 응답 메소드 `res.render`에
전달합니다. 

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
{: caption="예 43. galleryController.js의 부분 컨텐츠" caption-side="bottom"}
`예 43. galleryController.js의 부분 컨텐츠`

이 튜토리얼의 마지막 코드 예인 예 44는 이미지를 표시하는 데 필요한 코드를 포함하는
galleryView 템플리트의 본문을 보여줍니다. 여기서는 res.render() 메소드에서
imageUrls 배열을 가져와 중첩된 &lt;div&gt;&lt;/div&gt; 태그 쌍을 반복하며,
여기서 /gallery 라우트가 요청된 경우에는 이미지 URL이 이미지에 대한 GET 요청을
전송합니다. 

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
{: caption="예 44. 갤러리 템플리트에서 사용되는 루프 및 출력 스크립틀릿" caption-side="bottom"}
`예 44. 갤러리 템플리트에서 사용되는 루프 및 출력 스크립틀릿`

http://localhost:3000/gallery에서 이를 로컬로 테스트하면 그림 10의
이미지를 볼 수 있습니다. 

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### Git에 커미트
{: #tutorial-develop-commit}

이제 앱의 기본 기능이 작동하므로, 코드를 로컬 저장소에 커미트한 후
GitHub으로 푸시합니다. GitHub Desktop을 사용하는 경우에는
Changes를 클릭하고(그림 11 참조), 요약 필드에 변경사항의
요약을 입력한 후 Commit to Local-dev를 클릭하십시오.  

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

Sync를 클릭하면
GitHub에 공개된 원격 Local-dev 분기에
커미트가 전송되며, 이 조치는 이 튜토리얼의 마지막 그림인
그림 12에 표시되어 있는 바와 같이 빌드 단계와 배치 단계를 차례로 시작합니다.  

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## 다음 단계
{: #nextsteps}

축하합니다. 이제까지 {{site.data.keyword.cloud_notm}} 플랫폼을
사용하여 웹 애플리케이션을 빌드하는 과정을 처음부터 끝까지 완료했습니다.
이 기본적인 소개에서 다룬 각 개념은
[{{site.data.keyword.cloud_notm}} 플랫폼](https://cloud.ibm.com/)에서 더욱 자세히 탐색할 수 있습니다.  

행운을 빕니다!
