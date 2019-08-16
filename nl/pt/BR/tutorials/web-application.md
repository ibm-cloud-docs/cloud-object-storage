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

# Tutorial: aplicativo da web da galeria de imagens
{: #web-application}

Do início ao término, a construção de um aplicativo da web abrange muitos conceitos diferentes e é uma ótima maneira para a introdução
aos recursos do {{site.data.keyword.cos_full}}. Este tutorial mostrará como construir
uma galeria de imagens simples no {{site.data.keyword.cloud}} Platform e como reunir
muitos conceitos e práticas diferentes. Seu aplicativo usará o {{site.data.keyword.cos_full_notm}} como o
servidor de back-end para um aplicativo Node.js que permite que um usuário faça upload e visualize os arquivos de imagem JPEG.

## Antes de iniciar
{: #wa-prereqs}

Como pré-requisitos para a construção de um aplicativo da web, vamos começar com o seguinte:

  - Conta do {{site.data.keyword.cloud_notm}} Platform
  - Docker, como parte do {{site.data.keyword.cloud_notm}} Developer Tools
  - Node.js 
  - Git (app de Desktop e Interface da Linha de Comandos &mdash; CLI)

### Instalando o Docker
{: #tutorial-wa-install-docker}

A transição da construção de aplicativos da web com instâncias do servidor tradicional ou até mesmo
servidores virtuais para o uso de contêineres, como o Docker, acelera o desenvolvimento e facilita o teste enquanto suporta
a implementação automatizada. Um contêiner é uma estrutura leve que não precisa de sobrecarga adicional, como um sistema
operacional, apenas seu código e configuração para tudo, de dependências a configurações.

Vamos começar abrindo uma ferramenta familiar aos desenvolvedores experientes e um novo melhor amigo para aqueles que estão
apenas iniciando: a linha de comandos. Desde que a interface gráfica com o usuário (GUI) foi inventada, a interface da
linha de comandos de seu computador foi relegada para o status de segunda classe. Mas agora, é hora de trazê-la de volta (embora a GUI
não vá desaparecer tão cedo &mdash; especialmente quando precisamos procurar na web para fazer download de nosso novo conjunto de ferramentas da linha de comandos). 

Vá em frente e abra o Terminal ou outra interface da linha de comandos apropriada para seu sistema operacional e crie um
diretório usando os comandos apropriados para o shell específico que você está usando. Mude seu próprio diretório de referência para
o novo que você acabou de criar. Quando criado, seu aplicativo terá seu próprio subdiretório dentro dele, contendo
o código de início e a configuração necessários para o funcionamento.

Saindo da linha de comandos e retornando para o navegador, siga as instruções para instalar as [ferramentas do desenvolvedor do {{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) no link.
O Developer Tools oferece uma abordagem extensível e repetida para construir e implementar aplicativos em nuvem.

O [Docker](https://www.docker.com) é instalado como parte do Developer Tools e vamos precisar dele, mesmo que seu trabalho
ocorra principalmente em segundo plano, dentro das rotinas que sustentam seu novo app. O Docker deve estar em execução para que os comandos build funcionem. Vá em frente e crie uma conta do Docker on-line em [Dockerhub](https://hub.docker.com), execute o app Docker e se conecte.

### Instalando o Node.js
{: #tutorial-wa-install-node}

O app que você construirá usa o [Node.js](https://nodejs.org/) como o mecanismo do lado do servidor para executar o
código JavaScript para esse aplicativo da web. Para usar o Node Package Manager (npm) incluído do Node, para gerenciar
as dependências de seu app, deve-se instalar o Node.js localmente. Além disso, ter o Node.js instalado localmente
simplifica o teste, acelerando o desenvolvimento. 

Antes de iniciar, você pode considerar o uso de um gerenciador
de versão, como o Node Version Manager ou `nvm`, para instalar o Node, reduzindo a complexidade do gerenciamento de múltiplas versões do Node.js. A partir dessa gravação, para instalar ou atualizar o `nvm` em uma máquina Mac ou Linux, é possível usar o script de instalação usando cURL na
interface CLI que você acabou de abrir, copiando e colando um dos comandos nos primeiros dois exemplos em sua linha de comandos
e pressionando Enter (observe que isso presume que seu shell é BASH e não um alternativo):

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Exemplo 1. Usando o cURL para instalar o Node Version Manager (nvm)" caption-side="bottom"}
`Exemplo 1. Usando o cURL para instalar o Node Version Manager (nvm)`
   
...ou Wget (apenas um é necessário, mas não ambos; use o que estiver disponível em seu sistema):

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Exemplo 2. Usando o Wget para instalar o Node Version Manager (nvm)" caption-side="bottom"}
`Exemplo 2. Usando o Wget para instalar o Node Version Manager (nvm)`

Ou, para Windows, é possível usar [nvm for Windows](https://github.com/coreybutler/nvm-windows) com instaladores
e código-fonte no link.

Se você não desejar a complexidade adicional de suportar múltiplas liberações do Node.js, visite o
website [Node.js](https://nodejs.org/en/download/releases/)
e instale a Versão Long Term Support (LTS) do Node.js que
corresponde à versão mais recente suportada pelo buildpack do SDK for Node.js agora usado no
{{site.data.keyword.cloud_notm}} Platform. No momento dessa gravação,
o buildpack mais recente é v3.26 e suporta o Node.js community edition v6.17.0+. 

É possível localizar informações adicionais sobre o buildpack mais recente do {{site.data.keyword.cloud_notm}}
SDK for Node.js na página [Atualizações mais recentes do SDK for Nodejs](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates). 

Usando `nvm`, é possível instalar a versão do Nó que corresponde aos requisitos, copiando e colando o comando do Exemplo 3
em sua linha de comandos.

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="Exemplo 3. Usando `nvm` para instalar uma versão específica do Node.js" caption-side="bottom"}
`Exemplo 3. Usando nvm para instalar uma versão específica do Node.js`

Qualquer abordagem que você use, uma vez que tenha seguido as instruções para instalar o Node.js e o npm (incluído com o Node)
em seu computador, conforme apropriado para o sistema operacional e a estratégia que estão sendo usados, parabenize-se por uma tarefa
bem iniciada!

### Instalando o Git
{: #tutorial-wa-install-git}

Você provavelmente já está familiarizado com o Git, já que ele é o sistema de versão de
código-fonte mais amplamente usado entre os desenvolvedores que estão construindo aplicativos para a web.
Usaremos o Git posteriormente quando criarmos uma Cadeia de ferramentas de Continuous Delivery (CD) no {{site.data.keyword.cloud_notm}} Platform para
entrega contínua e implementação. Se não tiver uma conta do GitHub, crie uma
conta pessoal pública gratuita no website [Github](https://github.com/join);
caso contrário, fique à vontade para efetuar login com qualquer outra conta que você possa ter.

Observe que há instruções importantes, passo a passo, instruções sobre como gerar e fazer upload de chaves SSH para o
[Perfil do Github](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) para obter acesso seguro ao Github por meio da linha de comandos. No entanto, se
fizer isso agora, você estará somente fazendo um bom exercício, pois terá que repetir as etapas
para a instância de Github usado para o {{site.data.keyword.cloud_notm}} Platform, que acessaremos posteriormente. Embora
as etapas para usar as chaves SSH possam ser complicadas, com prática, você também pode ser fluente com SSH na CLI.

Por enquanto, acesse a página [Github Desktop](https://desktop.github.com/) para fazer download
do GitHub Desktop e, em seguida, execute o instalador. Quando o instalador é concluído,
é solicitado que você efetue login no GitHub com sua conta.

Na janela Efetuar login (consulte a primeira figura neste tutorial), insira o nome e o e-mail
que você deseja exibir publicamente (supondo que tenha uma conta pública) para quaisquer
confirmações em seu repositório. Depois de vincular o aplicativo à sua conta, você pode ser solicitado
a verificar a conexão do aplicativo por meio de sua conta do Github on-line.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

Você não precisa criar nenhum repositório ainda. Se observar um repositório denominado Tutorial incluído com o GitHub Desktop,
sinta-se à vontade para experimentar com ele para ajudar você a familiarizar-se com as operações. Você acabou de concluir a parte
de pré-requisito deste tutorial. Você está pronto para construir um app?

## Criando o app iniciador Node.js usando o Developer Tools
{: #tutorial-create-skeleton}

Para iniciar o desenvolvimento de seu aplicativo localmente, inicie efetuando login no {{site.data.keyword.cloud_notm}} Platform diretamente
por meio da linha de comandos, conforme mostrado no Exemplo 4. 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="Exemplo 4. Comando para efetuar login no IBM Cloud Platform usando o CLI Developer Tools" caption-side="bottom"}
`Exemplo 4. Comando para efetuar login no IBM Cloud Platform usando o CLI Developer Tools`

Você poderá especificar parâmetros opcionais se desejar: sua organização com a opção -o e o espaço com a opção -s ou,
se estiver usando uma conta federada: --sso. Quando você efetuar login, poderá ser solicitado que
escolha uma região; para os propósitos deste exercício, selecione `us-south` como a região, pois essa mesma opção será usada ao construir uma Cadeia de ferramentas de CD, posteriormente
neste tutorial.  

Em seguida, configure o terminal (se ele ainda não estiver configurado) usando o comando mostrado no Exemplo 5. Outros terminais são possíveis e
podem ser preferíveis para uso de produção, mas, por enquanto, use o código conforme mostrado, se apropriado para sua conta.

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="Exemplo 5. Comando para configurar o terminal de API para sua conta." caption-side="bottom"}
`Exemplo 5. Comando para configurar o terminal de API para sua conta`

Tenha como destino o aspecto do Cloud Foundry (cf) do {{site.data.keyword.cloud_notm}} Platform usando o código mostrado no
Exemplo 6, usando o comando de destino e a opção --cf. A API `cf` é integrada ao CLI Developer Tools.

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="Exemplo 6. Configurando suas opções para usar a API do Cloud Foundry." caption-side="bottom"}
`Exemplo 6. Configurando suas opções para usar a API do Cloud Foundry`

E agora, o momento para o qual você tem trabalhado: criar um aplicativo da web inicia com o código mostrado no Exemplo 7. O espaço `dev`
é uma opção padrão para sua organização, mas você pode desejar criar outros para isolar os esforços diferentes, mantendo 'finanças'
separado de 'desenvolvimento', por exemplo.

```bash
Ibmcloud dev criar
```
{:codeblock: .codeblock}
{: caption="Exemplo 7. Comando para criar um app usando o IBM Cloud Developer Tools" caption-side="bottom"}
`Exemplo 7. Comando para criar um app usando o IBM Cloud Developer Tools`

Com esse comando, uma série de perguntas será feita a você. É possível voltar em muitos pontos no processo, mas, se você sentir
que ficou perdido ou perdeu etapas, fique à vontade para recomeçar, excluindo o diretório ou criando outro para
seu teste e exploração. Além disso, ao concluir o processo que está criando seu aplicativo localmente na linha de comandos, você será capaz de ver os resultados
on-line posteriormente em seu portal do {{site.data.keyword.cloud_notm}} on-line no qual criou sua conta para gerenciar os
recursos criados.

No Exemplo 8, observe a opção para criar um 'App da web' &mdash; que é o que você deseja. Digite '2' e pressione Enter.

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
{: caption="Exemplo 8. A saída do comando `ibmcloud dev create` em que você seleciona a opção #2 para um App da web" caption-side="bottom"}
`Exemplo 8. A saída do comando ibmcloud dev create em que você seleciona a opção #2 para um App da web`

Há uma série de opções no Exemplo 9 com base no que são chamados de "buildpacks" e anote a opção para usar o 'Node'. Digite '4' e pressione Enter.

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
{: caption="Exemplo 9. Opções de linguagem de `ibmcloud dev create` continuado." caption-side="bottom"}
`Exemplo 9. Opções de linguagem de ibmcloud dev create continuado`

Depois de ter feito sua seleção para a linguagem de programação e/ou estrutura, a próxima seleção mostrada no Exemplo 10
terá tantas opções, que pode rolar para além do seu serviço desejado. Como é possível ver no exemplo, desejamos
usar um App da web Node.js simples com Express.js. Digite '6' e pressione Enter.

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
{: caption="Exemplo 10. Opções do aplicativo de estrutura básica de `ibmcloud dev create`." caption-side="bottom"}
`Exemplo 10. Opções do aplicativo de estrutura básica de ibmcloud dev create`

Agora que você escolheu as opções mais simples, a opção mais difícil para desenvolvedores em todos os lugares ainda é necessária: nomear seu app. Siga
o exemplo mostrado no Exemplo 11 e digite 'webapplication', em seguida, pressione Enter.

```bash
? Enter a name for your application> webapplication
```
{: caption="Exemplo 11. Nomeie seu aplicativo 'webapplication' usando `ibmcloud dev create`." caption-side="bottom"}
`Exemplo 11. Nomeie seu aplicativo 'webapplication' usando ibmcloud dev create`

Posteriormente, é possível incluir tantos serviços, como armazenamentos de dados ou funções de cálculo, quantos forem necessários ou desejados por meio do console da web. No entanto, conforme mostrado no Exemplo 12, digite 'n' para não quando perguntado se deseja incluir serviços neste momento.

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="Exemplo 12. Opção para incluir serviços ao usar `ibmcloud dev create` continuado." caption-side="bottom"}
`Exemplo 12. Opção para incluir serviços ao usar ibmcloud dev create continuado`

Anteriormente, as vantagens de se desenvolver com contêineres, em vez de servidor tradicional iron, ou mesmo servidores virtuais,
eram mencionadas com relação ao Docker. Uma maneira de gerenciar contêineres é com o software de orquestração, como o Kubernetes, que se
tornou um padrão _de facto_ em desenvolvimento. Mas, para este tutorial, podemos permitir que o serviço Cloud Foundry gerencie um único
contêiner do Docker que conterá o código, as bibliotecas e a configuração necessárias para seu app.

Conforme mostrado no Exemplo 13, digite '1' e pressione Enter para usar 'IBM DevOps' para o propósito de integrar o CD dentro de seu ciclo de vida
do projeto.
 
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
{: caption="Exemplo 13. Opções de implementação de `ibmcloud dev create`." caption-side="bottom"}
`Exemplo 13. Opções de implementação de ibmcloud dev create`

Conforme observado anteriormente, vamos escolher uma região para nossa cadeia de ferramentas do CD de implementação automatizada, portanto, selecione a mesma opção que anteriormente, '5',
conforme mostrado no Exemplo 14.

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
{: caption="Exemplo 14. Regiões disponíveis como opções em `ibmcloud dev create`." caption-side="bottom"}
`Exemplo 14. Regiões disponíveis como opções em ibmcloud dev create`

Neste ponto, a geração de um novo aplicativo nos lembrará de que a cadeia de ferramentas usada
para implementar seu app posteriormente precisará de alguma configuração adicional, conforme mostrado no Exemplo 15. Conforme mencionado anteriormente,
ao fazer upload de sua chave pública para o Github (na instância da Cadeia de ferramentas de CD no {{site.data.keyword.cloud_notm}}
Platform), será necessário entregar o aplicativo implementado usando o Github. Instruções adicionais podem ser localizadas depois de implementar
seu aplicativo e efetuar login na sua conta do IBM Cloud GitLab em [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair).

```

Note: For successful connection to the DevOps toolchain, this machine
must be configured for SSH access to your IBM Cloud GitLab account at
https://git.ng.bluemix.net/profile/keys in order to download the
application code.


```
{: caption="Exemplo 15. Nota fornecida sobre: chaves SSH pelo comando `ibmcloud dev create`." caption-side="bottom"}
`Exemplo 15. Nota fornecida sobre: chaves SSH pelo ibmcloud dev create`

Os prompts adicionais confirmarão o nome do aplicativo e da cadeia de ferramentas que você definiu anteriormente. O Exemplo 16 mostra como é possível alterar os
nomes do host e da cadeia de ferramentas, se desejar. O nome do host deve ser exclusivo para o domínio usado como o terminal em serviço de seu aplicativo, mas, se não houver conflito,
você poderá simplesmente pressionar retornar quando a confirmação for solicitada.

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="Exemplo 16. Confirmando nomes para propriedades em `ibmcloud dev create`." caption-side="bottom"}
`Exemplo 16. Confirmando nomes para propriedades em ibmcloud dev create`

Se copiar e colar esse link fornecido no final da saída recebida como resultado do uso do comando `ibmcloud dev create`, você
será capaz de acessar sua Cadeia de ferramentas de CD. Mas, também será possível acessá-lo por meio do console posteriormente, caso você tenha perdido a captura do link.
Informações adicionais a seguir, conforme o processo continua, conforme mostrado no Exemplo 17, em que as entradas do aplicativo foram criadas
on-line e um diretório com o código de amostra foi criado. 

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
{: caption="Exemplo 17. Confirmação de ações geradas por `ibmcloud dev create`." caption-side="bottom"}
`Exemplo 17. Confirmação de ações geradas por ibmcloud dev create`

Essa última instrução do Exemplo 17 significa que, se você visualizar seu diretório atual, um novo subdiretório `webapplication` deverá
agora estar visível. Dentro do diretório `webapplication`, você localizará um andaime de seu novo aplicativo Node.js. No entanto, embora
a receita possa estar presente, os ingredientes em si, ainda agrupados em uma imagem do Docker, precisam ser "preparados" &mdash; ou melhor, construídos &mdash; usando
o comando no Exemplo 18. O Docker deve estar em execução em sua máquina local como uma consequência da instalação,
mas, se for necessário reiniciá-lo, faça isso. Qualquer tentativa de construir seu novo aplicativo da web sem o Docker em execução
falhará, mas essa não é a única razão possível. Se houver algum problema, verifique as mensagens de erro resultantes que podem ter o
link apropriado para visualizar os logs de resultados em seu portal on-line para a sua conta do {{site.data.keyword.cloud_notm}} Platform.

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Exemplo 18. Comando build do {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Exemplo 18. Comando build do IBM Cloud Platform`

Além de construir o app para entrega, a construção do app permite que você execute o mesmo código localmente com o comando `run`
(depois de copiar e colar ou digitar o comando do Exemplo 19). Quando concluído, copie e cole a URL fornecida na
barra de endereço de seu navegador, geralmente, <http://localhost:3000>.

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Exemplo 19. Comando da CLI do {{site.data.keyword.cloud_notm}} Platform para executar seu app" caption-side="bottom"}

Agora que o app está criado e definido, visualize seu aplicativo para confirmar se ele funciona. Se você vir a imagem de item temporário conforme
mostrado na Figura 2, parabéns! Você criou um novo aplicativo da web Node.js e está pronto para implementá-lo na nuvem.

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="Figura 2. Novo aplicativo Node.js: parabéns!" caption-side="top"}

Implemente o app no {{site.data.keyword.cloud_notm}} Platform com o comando deploy (conforme mostrado no Exemplo 20).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Exemplo 20. Comando da CLI do {{site.data.keyword.cloud_notm}} Platform para fazer upload e implementar seu app" caption-side="bottom"}
`Exemplo 20. Comando da CLI do IBM Cloud Platform para fazer upload e implementar seu app`

A URL novamente será exibida como um resultado da execução do comando `ibmcloud dev deploy` com base no terminal regional
e no nome do host que você especificou anteriormente. Se houver algum problema, você poderá ver links para os logs que estão armazenados em seu portal
no {{site.data.keyword.cloud_notm}} Platform. Se não houver nenhum problema, você deverá ver uma exibição idêntica em seu navegador
para o aplicativo local recém-visitado. Vá em frente e visite seu novo aplicativo da web na nuvem!

## Criando o app Web Gallery usando um aplicativo de amostra
{: #tutorial-create-app}

Vamos lembrar dos pré-requisitos necessários para desenvolver um app Node.js no {{site.data.keyword.cloud_notm}} Platform. Você
já criou a sua conta do {{site.data.keyword.cloud_notm}} Platform, bem como instalou o Developer Tools, que
instalou o Docker. Em seguida, você instalou o Node.js. O último item listado como um pré-requisito para este tutorial foi Git, no qual vamos nos aprofundar agora.  

Vamos iniciar as especificidades de trabalhar na galeria de imagens em Node.js. Por enquanto, vamos usar o Github Desktop para
esse cenário, mas também é possível usar o cliente da linha de comandos Git para concluir as mesmas tarefas. Para iniciar, vamos clonar um modelo
de iniciador para o seu novo aplicativo da web. 

Siga estas etapas:

1.  Clone o repositório listado no Exemplo 21. Faça download do modelo para seu app em seu ambiente
    de desenvolvimento local usando Git. Em vez de clonar o app de
    amostra do {{site.data.keyword.cloud_notm}} Platform, use o comando no Exemplo 21 para clonar o
    modelo de iniciador para o app {{site.data.keyword.cos_full_notm}} Web Gallery. Depois de clonar o
    repositório, você localizará o app iniciador no
    diretório COS-WebGalleryStart. Abra uma janela CMD do Git e mude para um
    diretório no qual você deseja clonar o repositório Github. Use o comando mostrado
    no primeiro exemplo deste tutorial.

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="Exemplo 21. Detalhes do comando clone do Git" caption-side="bottom"}
`Exemplo 21. Detalhes do comando clone do Git`

2.  Execute o app localmente. Abra um aplicativo do terminal fornecendo uma CLI e mude seu diretório de trabalho para
    o diretório COS-WebGalleryStart. Observe as dependências do Node.js
    listadas no arquivo package.json. Faça download delas no local usando o comando
    mostrado a seguir, no Exemplo 22.

```bash
npm install
```
{: codeblock}
{: caption="Exemplo 22. Instalação do Node Package Manager (npm)" caption-side="bottom"}
`Exemplo 22. Instalação do Node Package Manager (npm)`

3.  Execute o app usando o comando mostrado no Exemplo 23.

```bash
npm start
```
{: codeblock}
{: caption="Exemplo 23. Detalhes sobre como iniciar seu app com npm" caption-side="bottom"}
`Exemplo 23. Detalhes sobre como iniciar seu app com npm`

Abra um navegador e visualize seu app no endereço e na porta que são a saída
para o console, <http://localhost:3000>.

**Dica**: para reiniciar o app localmente, encerre o processo de nó (Ctrl + C) para
pará-lo e use `npm start` novamente. No entanto, enquanto você desenvolve novos recursos, o uso do nodemon para reiniciar seu app quando
ele detecta uma mudança economiza seu tempo. Instale o nodemon globalmente como este:
`npm install -g nodemon`. Em seguida, execute-o por meio da linha de comandos em seu diretório
de app usando: `nodemon`, para que 'nodemon' inicie seu app.

4.  Prepare-se para preparar o app para implementação! Atualize o valor da propriedade de
    nome do aplicativo no arquivo `manifest.yml` do COS-WebGallery para o nome
    inserido para seu app no {{site.data.keyword.cloud_notm}} Platform e as outras informações, conforme mostrado no Exemplo 24,
    se necessário. O aplicativo `manifest.yml` é semelhante ao exemplo a seguir. Além disso, é possível customizar o arquivo `package.json`
    localizado no diretório-raiz do app para seu app com o nome
    de seu app e seu nome como o autor.

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
{: caption="Exemplo 24. Conteúdo de `manifest.yml`" caption-side="bottom"}
`Exemplo 24. Conteúdo de manifest.yml`

**Dica**: agora é o ponto em que você pode precisar configurar chaves SSH para enviar por push o código de forma interativa para sua origem remota. Se você configurar uma 
    passphrase para sua chave SSH, será necessário inserir esse código toda vez que enviar por push suas mudanças para a origem remota de
    seu repositório. 

5.  Remova e substitua o conteúdo de seu diretório `webapplication` pelo conteúdo do diretório que você acabou de modificar, `COS-WebGalleryStart`.
    Usando suas qualificações de Git finamente ajustadas, inclua os arquivos que foram excluídos e incluídos no repositório com a CLI ou
    o Github Desktop. Em seguida, envie por push as mudanças para a origem do repositório. No futuro, você será capaz de fazer mudanças em seu
    aplicativo da web baseado em nuvem apenas enviando por push as mudanças para o Git. A cadeia de ferramentas do CD reiniciará automaticamente de forma mágica o processo do servidor
    depois de clonar suas mudanças e armazená-las em arquivo stash no servidor. 


Essencialmente, nós codificamos novamente o nosso aplicativo, então vamos repetir o processo de construção. Mas desta vez, usaremos o novo código da Galeria de imagens. 

###Implemente o app no {{site.data.keyword.cloud_notm}} Platform.### 

Para obter o app iniciador com suas mudanças
    para o {{site.data.keyword.cloud_notm}} Platform, implemente-o usando o Developer Tools, repetindo as mesmas etapas que executamos
    anteriormente.

a.  Caso ainda não tenha feito isso ou se tiver reiniciado ou tiver efetuado logout, efetue login no {{site.data.keyword.cloud_notm}} Platform
usando o comando de login. Como um lembrete, isso é mostrado no Exemplo 25 e observe que será possível especificar parâmetros opcionais se
você desejar: sua organização com a opção -o e o espaço com a opção -s ou, se estiver usando uma conta federada: --sso. Lembre-se
de escolher a mesma região em que esteve trabalhando até o momento, se for solicitada.

```bash
ibmcloud login
```
{: codeblock}
{: caption="Exemplo 25. Comando da CLI para efetuar login no {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Exemplo 25. Comando da CLI para efetuar login no IBM Cloud Platform`

b.  Configure o Terminal de API para sua região usando o comando api (conforme
        mostrado com os itens temporários opcionais no Exemplo 6). Se você não souber sua URL de terminal
        da API regional, consulte a página Introdução.

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="Exemplo 26. Terminal da API do {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Exemplo 26. Terminal da API do IBM Cloud Platform`

c.  Tenha como destino o aspecto do Cloud Foundry do {{site.data.keyword.cloud_notm}} Platform usando o código mostrado no
exemplo 27, usando o comando de destino e a opção --cf.


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="Exemplo 27. CLI do {{site.data.keyword.cloud_notm}} Platform destinando o Cloud Foundry" caption-side="bottom"}
`Exemplo 27. CLI do IBM Cloud Platform destinando o Cloud Foundry`

d.  Construa o app para entrega que o aplicativo com o comando build (como no Exemplo 28).

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Exemplo 28. Comando build do {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Exemplo 28. Comando build do IBM Cloud Platform`

g.  Vamos seguir em frente e testar o aplicativo localmente. Além de construir o app para entrega, a construção do app permite que você execute o mesmo código localmente com o comando run (depois de digitar o
    comando do Exemplo 29).


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Exemplo 29. Comando da CLI do {{site.data.keyword.cloud_notm}} Platform para executar seu app" caption-side="bottom"}
`Exemplo 29. Comando da CLI do IBM Cloud Platform para executar seu app`

h.  Implemente o app no {{site.data.keyword.cloud_notm}} Platform com o comando deploy (conforme mostrado no Exemplo 30).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Exemplo 30. Comando da CLI do {{site.data.keyword.cloud_notm}} Platform para fazer upload e implementar" caption-side="bottom"}
`Exemplo 30. Comando da CLI do IBM Cloud Platform para fazer upload e implementar`

O código no Exemplo 31 mostra a sequência de comandos usados neste exemplo para construir, testar e implementar o aplicativo da web inicial.

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
{: caption="Exemplo 31. Lista de comandos da CLI do {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Exemplo 31. Lista de comandos da CLI do IBM Cloud Platform`

Se bem-sucedido, o {{site.data.keyword.cloud_notm}} Platform relata que o app foi transferido por upload,
implementado com êxito e iniciado. Se também estiver com login efetuado no console da web
do {{site.data.keyword.cloud_notm}} Platform, você será notificado lá também sobre o status de seu app. Mas, mais importante, é possível verificar se o app
foi implementado visitando a URL do app relatada pelo {{site.data.keyword.cloud_notm}} Platform com um navegador ou por meio do
console da web clicando no botão Visualizar app.

5.  Teste o app. A mudança visível do modelo de app padrão que
    foi implementada na criação para o app iniciador mostrado a seguir
    demonstrou que a implementação do app no {{site.data.keyword.cloud_notm}} Platform foi bem-sucedido.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### Criar uma ramificação de Git
{: #tutorial-create-branch}

Agora, é necessário criar uma ramificação para o ambiente de desenvolvimento local
a ser usado para o Estágio de construção do {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline:

1.  Se estiver usando o Github Desktop, clique no ícone de ramificação; será solicitado que você insira um nome para a
    ramificação (consulte a Figura 14). Este exemplo usa Local-dev como o nome.

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  Depois de criar a ramificação, o GitHub compara os arquivos locais na
    ramificação Local-dev com os arquivos no repositório na ramificação
    principal e relata Nenhuma mudança local. Agora é possível clicar em Publicar para
    incluir a ramificação que você criou em seu repositório local para seu repositório GitHub
    (conforme mostrado na Figura 5).

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

Agora que a ramificação Local-dev está publicada no repositório GitHub em sua
cadeia de ferramentas, o estágio de construção de seu {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline será
acionado seguido pelo estágio de implementação sempre que você enviar por push uma confirmação para ele.
A implementação do app por meio da CLI não será mais necessária, pois a implementação foi integrada diretamente a seu fluxo de trabalho.

### Configurando o {{site.data.keyword.cos_full_notm}} para suas credenciais de armazenamento
{: #tutorial-credentials}

É necessário configurar as credenciais do {{site.data.keyword.cos_short}} para seu aplicativo da web, assim como um 'depósito'
no qual ele armazenará e recuperará imagens. A chave de API que você criará precisará de credenciais HMAC do {{site.data.keyword.cos_short}}, conforme definido por suas
[Credenciais de serviço](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials).
Você pode reconhecer os termos `access_key_id` e `secret_access_key` uma vez que pode ter uma conta do AWS e usar
um arquivo de credenciais que já tenha entradas `aws_access_key_id` e `aws_secret_access_key`. 

Depois de ter concluído a criação de uma chave de API, transferido por download e, em seguida, copiado as credenciais HMAC, conclua as etapas a seguir:

1.  No ambiente de desenvolvimento local, coloque as credenciais no
    caminho do Windows `%USERPROFILE%\\.aws\\credentials` (para usuários do Mac/Linux, as credenciais devem
    ir para `~/.aws/credentials)`. O Exemplo 32 mostra o conteúdo de um
    arquivo de credenciais típico.

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="Exemplo 32. Credenciais conforme elas são definidas no arquivo `~/.aws/credentials " caption-side="bottom"}
`Exemplo 32. Credenciais conforme elas são definidas no arquivo ~/.aws/credentials`

2.  Na página da web para o aplicativo criado usando o comando da CLI no {{site.data.keyword.cloud_notm}} Platform,
    defina suas credenciais necessárias como variáveis de ambiente conforme as melhores práticas de desenvolvimento
    efetuando login no {{site.data.keyword.cloud_notm}} Platform e, sob Cloud Foundry Apps, selecione seu
    app, 'webapplication'. Nas guias, clique em Tempo de execução.

3.  Na janela Tempo de execução, clique em Variáveis de ambiente na parte superior da
    página e role para a seção Definido pelo usuário, que permite incluir
    as variáveis.

4.  Inclua duas variáveis: uma com o valor de seu access_key_id usando `AWS_ACCESS_KEY_ID` como o nome
    da chave e outra com o valor de sua chave de acesso secreta denominada `AWS_SECRET_ACCESS_KEY`.
    Essas variáveis e seus respectivos valores são o que o app usa para autenticar para a instância
    do {{site.data.keyword.cos_short}} ao executar no {{site.data.keyword.cloud_notm}}
    Platform (consulte a Figura 6). Ao concluir com as
    entradas, clique em Salvar e o {{site.data.keyword.cloud_notm}} Platform reiniciará automaticamente o app para você.

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

Em seguida, no {{site.data.keyword.cos_short}} Portal para sua instância de serviço,
inclua um depósito para conter suas imagens. Este cenário usa o depósito denominado `web-images`.


## Customizando um aplicativo da web galeria de imagens do {{site.data.keyword.cos_full_notm}} do Node.js
{: #tutorial-develop}

Como este exemplo usa uma arquitetura MVC, ajustar a estrutura de
diretório dentro de seu projeto para refletir essa arquitetura é uma conveniência, bem como uma melhor prática.
A estrutura de diretório tem um diretório views para conter os modelos de visualização EJS, um
diretório routes para conter as rotas expressas e um diretório controllers como
o local para colocar a lógica do controlador. Coloque esses itens sob um diretório de origem
pai denominado src (consulte a Figura 7).

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**Dica**: o repositório que você clonou anteriormente contém um diretório denominado
COS-WebGalleryEnd. Visualizar o código-fonte do aplicativo concluído em seu editor preferencial
poderá ser útil conforme você seguir as próximas etapas. Essa será a versão
de seu 'webapplication' que é confirmada e implementada no {{site.data.keyword.cloud_notm}} Platform
quando você concluir este tutorial.

### Projetando o app
{: #tutorial-develop-design}

Estas são as duas tarefas principais que um usuário deve ser capaz de executar com o
aplicativo da web de galeria de imagens simples:

  - Faça upload de imagens de um navegador da web para o depósito do {{site.data.keyword.cos_short}}.
  - Visualize as imagens no depósito do {{site.data.keyword.cos_short}} em um navegador da web.

As próximas etapas se concentram em como realizar essas duas funções de demonstração em vez de construir um app de classificação de produção
totalmente desenvolvido. Implementar este tutorial e deixá-lo exposto e em execução significa que qualquer pessoa que localizar o app
poderá executar as mesmas ações: fazer upload de arquivos em seu depósito do {{site.data.keyword.cos_full_notm}} e visualizar as imagens JPEG que já existem em seu navegador.

### Desenvolvendo o app
{: #tutorial-develop-app}

No arquivo `package.json`, dentro do
objeto de scripts, é possível ver como "start" é definido (Exemplo 33). Esse arquivo
é o que o {{site.data.keyword.cloud_notm}} Platform usa para informar ao nó para executar app.js cada vez que o app
é iniciado. Além disso, use-o ao testar o app localmente. Dê uma olhada no arquivo de aplicativo principal, que é chamado app.js. Esse é o código que nós dissemos ao Node.js para processar primeiro quando você inicia seu app com o comando `npm start` (ou nodemon). 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="Exemplo 33. Informando ao seu app como autoinicializar seu código customizado" caption-side="bottom"}
`Exemplo 33. Informando ao seu app como autoinicializar seu código customizado`

Nosso arquivo app.js inicia com o código mostrado no Exemplo 34.
No início, o código usa o nó para carregar os módulos que são necessários para a introdução.
A estrutura Express cria o app como um singleton simplesmente chamado `app`.
O exemplo termina (omitindo a maioria do código por enquanto) informando ao app
para atender na porta que está designada e uma propriedade do ambiente, ou 3000, por padrão.
Ao ativar com êxito no início, ele imprimirá uma mensagem com a URL do servidor para o console.

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
{: caption="Exemplo 34. Seu Aplicativo da web tem um início modesto, mas poderoso" caption-side="bottom"}
`Exemplo 34. Seu Aplicativo da web tem um início modesto, mas poderoso`

Vamos ver como o Exemplo 35 mostra como definir um caminho e visualizações. A primeira linha de código informa à
estrutura Express para usar o diretório público para atender aos nossos arquivos estáticos, que
incluem quaisquer imagens estáticas e folhas de estilo que usamos. As linhas a seguir informam ao
app onde localizar os modelos para nossas visualizações no
diretório src/views e configurar nosso mecanismo de visualização como EJS. Além disso, a estrutura
usará o middleware do analisador sintático de corpo para expor os dados da solicitação recebida
para o app como JSON. Nas linhas de fechamento do exemplo, o app expresso responde a
todas as solicitações GET recebidas para nossa URL de app, renderizando o modelo de visualização
index.ejs.

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
{: caption="Exemplo 35. Visualizações do app da web e locais de modelo" caption-side="bottom"}
`Exemplo 35. Visualizações do app da web e locais de modelo`

A figura a seguir mostra o modelo de visualização de índice quando renderizado
e enviado para o navegador. Se você está usando o `nodemon`, pode ter notado
que seu navegador foi atualizado quando suas mudanças foram salvas e seu app deve ser semelhante à Figura 8.

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

No Exemplo 36, nossos modelos de visualização compartilham o código HTML entre as
tags &lt;head&gt;...&lt;/head&gt;, portanto, nós o colocamos em um modelo de inclusão
separado (consulte o Exemplo 16). Esse modelo (head-inc.ejs)
contém um scriptlet &mdash; uma ligação uma variável JavaScript &mdash; para o título da página na linha 1.
A variável `title` é configurada em `app.js` e passada como dados para nosso modelo
de visualização na linha abaixo dela. Caso contrário, estamos simplesmente usando alguns endereços CDN
para puxar no CSS de autoinicialização, Javascript de autoinicialização e JQuery. Por fim, nós incluímos um arquivo
styles.css estático customizado de nosso diretório style sheets/público.

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
{: caption="Exemplo 36. Elementos HTML de head-inc.ejs" caption-side="bottom"}
`Exemplo 36. Elementos HTML de head-inc.ejs`

O corpo da visualização de índice contém as nossas guias de navegação
de estilo de autoinicialização (consulte o Exemplo 37) e nosso formulário de upload em um layout básico fornecido pelos
estilos CSS incluídos com autoinicialização.

Considere estas duas especificações para nosso app:

-   Nós configuramos nosso método de formulário para POST e o tipo de codificação de dados de formulário como
    dados de formulário/partes múltiplas na linha 24. Para a ação de formulário, enviamos os
    dados de nosso formulário para o app para a rota de app "/". Posteriormente,
    faremos trabalho adicional em nossa lógica do roteador para manipular solicitações de POST para
    essa rota.

-   Desejamos exibir feedback sobre o status da tentativa de upload de
    arquivo para o usuário. Esse feedback é passado para nossa visualização em uma
    variável denominada "status" e é exibido abaixo do formulário de upload.

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="/">Página inicial</a></li>
    <li role="presentation"><a href="/gallery">Galeria</a></li>
</ul>
<div class="container">
    <h2>Fazer upload da imagem para o IBM Cloud Object Storage</h2>
    <div class="row">
        <div class="col-md-12">
            <div class="container" style="margin-top: 20px;">
                <div class="row">

                    <div class="col-lg-8 col-md-8 well">

                        <p class="wellText">Faça upload de seu arquivo de imagem JPG aqui</p>

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
{: caption="Exemplo 37. Elementos HTML de index.ejs" caption-side="bottom"}
`Exemplo 37. Elementos HTML de index.ejs`

Vamos reservar um tempo para retornar ao `app.js` no Exemplo 38. O exemplo configura as rotas
Express para manipular solicitações adicionais que serão feitas em nosso app. O
código para esses métodos de roteamento será em dois arquivos sob o diretório
`./src/routes` em seu projeto:

-   imageUploadRoutes.js: esse arquivo manipula o que acontece quando o usuário
    seleciona uma imagem e clica em Upload.

-   galleryRoutes.js: esse arquivo manipula solicitações quando o usuário clica
    na guia Galeria para solicitar a visualização imageGallery.

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
{: caption="Exemplo 38. Exemplos do roteador do Node Express" caption-side="bottom"}
`Exemplo 38. Exemplos do roteador do Node Express`

#### Upload de imagem
{: #tutorial-develop-image-upload}

Consulte o código de 'imageUploadRoutes.js' no Exemplo 39. Deve-se criar uma instância
de um novo roteador expresso e nomeá-lo `imageUploadRoutes` no início.
Posteriormente, criaremos uma função que retorna `imageUploadRoutes`
e designaremos a ela uma variável chamada `router`. Quando concluído, a função deve ser
exportada como um módulo para torná-la acessível à estrutura e ao nosso código principal em app.js.
A separação de nossa lógica de roteamento da lógica de upload requer um arquivo de controlador denominado
galleryController.js. Como essa lógica é dedicada ao processamento da solicitação recebida e
ao fornecimento da resposta apropriada, colocamos essa lógica nessa função e a salvamos no
diretório ./src/controllers.

A instância do Roteador da estrutura Express é o local em que o nosso imageUploadRoutes
é projetado para rotear solicitações para a rota do app raiz ("/") quando o método de HTTP POST é usado.
Dentro do método `post` de nosso imageUploadRoutes, usamos o middleware dos módulos `multer` e
`multer-s3` que é exposto pelo galleryController como `upload`.
O middleware toma os dados e o arquivo de nosso POST de formulário de Upload,
processa-os e executa uma função de retorno de chamada. Na função de retorno de chamada,
verificamos que obtemos um código de status de HTTP de 200 e que
tivemos pelo menos um arquivo em nosso objeto de solicitação para fazer upload. Com base nessas
condições, nós configuramos o feedback em nossa variável `status` e renderizamos o
modelo de visualização de índice com o novo status.

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
{: caption="Exemplo 39. Detalhes do roteador expresso do Node" caption-side="bottom"}
`Exemplo 39. Detalhes do roteador expresso do Node`

Em comparação, o código para o galleryRouter no Exemplo 40 é um modelo de simplicidade. Seguimos o mesmo padrão
que fizemos com imageUploadRouter e requeremos o galleryController na primeira linha da função e, em seguida, configuramos nossa rota. A principal diferença é que nós
estamos roteando solicitações de HTTP GET em vez de POST e enviando toda a saída na resposta de getGalleryImages, que é exposta pelo
galleryController na última linha do exemplo.

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
{: caption="Exemplo 40. Detalhes do roteador expresso do Node" caption-side="bottom"}
`Exemplo 40. Detalhes do roteador expresso do Node`

Em seguida, viramos nossa atenção para o controlador para a galeria.

Observe como configuramos o upload de `multer` no Exemplo 41
(que trunca um código que vamos ignorar por enquanto). Nós
requeremos os módulos `ibm-cos-sdk`, `multer` e `multer-s3`. O código mostra como
configurar um objeto S3 que aponta para um terminal do servidor {{site.data.keyword.cos_short}}. Estamos
configurando estaticamente valores como o endereço de terminal, a região e
o depósito para simplificar, mas eles podem facilmente ser referenciados por meio de uma
variável de ambiente ou arquivo de configuração JSON.

Definimos `upload` conforme usado no Exemplo 41 e definido no imageUploadRouter, criando uma nova
instância de `multer` com `storage` como sua única propriedade. Essa propriedade informa
ao `multer` onde enviar o arquivo de nossos dados de formulário/partes múltiplas. Como o {{site.data.keyword.cloud_notm}}
Platform usa uma implementação da API S3, configuramos o armazenamento para ser um
objeto `s3-multer`. Esse objeto `s3-multer` contém uma propriedade `s3` que nós
designamos ao nosso objeto `s3` anteriormente e uma propriedade do depósito que
designamos à variável `myBucket`, que é designada a um
valor de “web-images”. O objeto `s3-multer` tem agora todos os dados
necessários para conectar e fazer upload de arquivos em nosso depósito do {{site.data.keyword.cos_short}} quando ele
recebe dados do formulário de upload. O nome ou a chave do objeto
transferido por upload será o nome do arquivo original tomado do objeto de arquivo quando
ele estiver armazenado em nosso depósito “web-images” do {{site.data.keyword.cos_short}} 

**Dica**: use um registro de data e hora como parte do nome do arquivo para manter a exclusividade de nome do arquivo. 

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
{: caption="Exemplo 41. Detalhes do controlador expresso do Node" caption-side="bottom"}
`Exemplo 41. Detalhes do controlador expresso do Node`

Para teste local, uma
tarefa útil é imprimir o objeto de arquivo para o console, `console.log(file)`.
Nós executamos um teste local do formulário de Upload e mostramos a saída do
log do console do arquivo no Exemplo 42.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="Exemplo 42. Exibição de console do objeto de depuração" caption-side="bottom"}
`Exemplo 42. Exibição de console do objeto de depuração`

Embora seja inadequado se gabar, a Figura 9 mostra o feedback de nosso retorno de chamada
declarando que o aplicativo realmente: "fez upload do arquivo com êxito" quando testado.

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### Recuperação de imagem e exibição
{: #tutorial-image-display}

Lembre-se, de volta no app.js, a linha de código `app.use('/gallery', galleryRouter);`
informa à estrutura Express para usar esse roteador quando a rota “/gallery” é solicitada.
Esse roteador, se você se lembra, usa o galleryController.js (consulte o código no Exemplo 43), nós definimos a
função getGalleryImages, cuja assinatura vimos anteriormente. Usando o mesmo objeto `s3`
que configuramos para nossa função de upload de imagem, chamamos a função denominada
`listObjectsV2`. Essa função retorna os dados do índice que definem cada um dos
objetos em nosso depósito. Para exibir imagens em HTML, precisamos de uma URL de imagem para cada
imagem JPEG em nosso depósito `web-images` para exibir em nosso modelo de visualização. O
fechamento com o objeto de dados retornado por `listObjectsV2` contém metadados
sobre cada objeto em nosso depósito. 

O código efetua loop no `bucketContents` procurando qualquer chave do objeto que termina em ".jpg" e
cria um parâmetro para passar para a função S3 getSignedUrl. Essa
função retorna uma URL assinada para qualquer objeto quando fornecemos o
nome e a chave do depósito do objeto. Na função de retorno de chamada, salvamos cada URL
em uma matriz e a passamos para o método de resposta do servidor HTTP `res.render`
como o valor para uma propriedade denominada `imageUrls`.

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
{: caption="Exemplo 43. Conteúdo parcial de galleryController.js" caption-side="bottom"}
`Exemplo 43. Conteúdo parcial de galleryController.js`

O último exemplo de código, número 44 neste tutorial, mostra o corpo para o modelo galleryView com o código
necessário para exibir as imagens. Nós obtemos a matriz imageUrls do método
res.render() e iteramos sobre um par de tags &lt;div&gt;&lt;/div&gt; aninhadas em que
a URL de imagem fará uma solicitação de GET para a imagem quando a rota /gallery
for solicitada.

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="/">Página inicial</a></li>
        <li role="presentation" class="active"><a href="/gallery">Galeria</a></li>
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
{: caption="Exemplo 44. Loop e saída de scriptlets usados no modelo de galeria" caption-side="bottom"}
`Exemplo 44. Loop e saída de scriptlets usados no modelo de galeria`

Nós o testamos localmente por meio de http://localhost:3000/gallery e vemos nossa imagem
na Figura 10.

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### Confirmando para Git
{: #tutorial-develop-commit}

Agora que os recursos básicos do aplicativo estão funcionando, vamos confirmar o nosso código
para o nosso repositório local e, em seguida, enviá-lo por push para o GitHub. Usando o GitHub Desktop,
clicamos em Mudanças (consulte a Figura 11), digite um resumo das mudanças no
campo Resumo e, em seguida, clique em Confirmar para Local-dev. 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

Quando clicamos
em Sincronizar, nossa confirmação é enviada para a ramificação Local-dev remota que
publicamos no GitHub e essa ação inicia o Estágio de construção seguido pelo
Estágio de implementação em nosso Delivery Pipeline, conforme exemplificado na última figura, número 12, neste tutorial. 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## Próximas Etapas
{: #nextsteps}

Parabéns! Nós fomos do início ao fim nesse
caminho para construir uma galeria de imagens do aplicativo da web usando o {{site.data.keyword.cloud_notm}} Platform.
Cada um dos conceitos que cobrimos nessa introdução básica pode ser explorado ainda mais no
[{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/). 

Boa sorte!
