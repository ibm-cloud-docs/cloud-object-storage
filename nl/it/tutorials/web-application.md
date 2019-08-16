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

# Esercitazione: applicazione web di Image Gallery
{: #web-application}

Dall'inizio alla fine, la creazione di un'applicazione web copre molti concetti differenti ed è un ottimo modo di
fare esperienza con le funzioni di {{site.data.keyword.cos_full}}. Questa esercitazione ti mostrerà come creare
una galleria di immagini di esempio su {{site.data.keyword.cloud}} Platform e come riunire tra loro
molti concetti e procedure differenti. La tua applicazione utilizzerà {{site.data.keyword.cos_full_notm}} come server di
backend per un'applicazione Node.js che consente a un utente di caricare e visualizzare file immagine JPEG.

## Prima di cominciare 
{: #wa-prereqs}

Come prerequisiti per la creazione di un'applicazione web, iniziamo con i seguenti:

  - Account {{site.data.keyword.cloud_notm}} Platform
  - Docker, come parte di {{site.data.keyword.cloud_notm}} Developer Tools
  - Node.js 
  - Git (applicazione desktop e CLI (interfaccia riga di comando))

### Installazione di Docker
{: #tutorial-wa-install-docker}

La transizione dalla creazione di applicazioni web con istanze del server tradizionali o anche di server virtuali
all'utilizzo dei contenitori, come Docker, velocizza lo sviluppo e facilita la verifica
supportando anche lo sviluppo automatizzato. Un contenitore è una struttura leggera che non ha bisogno di un ulteriore sovraccarico,
come un sistema operativo, necessita solo di codice e configurazione per tutto dalle dipendenze alle impostazioni.

Inizia aprendo uno strumento familiare agli sviluppatori esperti e un nuovo migliore amico per quelli che
hanno appena iniziato: la riga di comando. Sin da quando è stata inventata la GUI (graphic user interface), l'interfaccia di riga di comando del
tuo computer è stata relegata a uno status di seconda classe. Ma ora è il momento di riutilizzarla (sebbene la GUI
continuerà ad essere utilizzata a lungo specialmente quando abbiamo bisogno di sfogliare il web per scaricare il nostro nuovo set di strumenti di riga di comando). 

Vai avanti e apri il terminale o un'altra interfaccia di riga di comando appropriata per il tuo sistema operativo e crea una
directory utilizzando i comandi appropriati per la shell specifica che stai utilizzando. Modifica la tua directory di riferimento con la nuova
che hai appena creato. Quando viene creata, la tua applicazione avrà la propria directory secondaria al suo interno,
la quale contiene la configurazione e il codice iniziali di cui hai bisogno per essere operativo.

Uscendo dalla riga di comando e tornando al browser, segui le istruzioni per installare [{{site.data.keyword.cloud_notm}} Platform developer tools](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) utilizzando il link.
Developer Tools offre un approccio ripetibile ed estensibile alla creazione e distribuzione delle applicazioni cloud.

[Docker](https://www.docker.com) viene installato come parte di Developer Tools, e ne avremo bisogno, anche se lavorerà per lo più
in background, all'interno delle routine che eseguono lo scaffolding della tua nuova applicazione. Docker deve essere in esecuzione perché
funzionino i comandi di creazione. Vai avanti e crea un account Docker online all'indirizzo [Dockerhub](https://hub.docker.com), esegui l'applicazione Docker ed accedi.

### Installazione di Node.js
{: #tutorial-wa-install-node}

L'applicazione che creerai utilizza [Node.js](https://nodejs.org/) come motore lato server per eseguire il codice
JavaScript per questa applicazione web. Per utilizzare l'npm (Node Package Manager) incluso in Node e per gestire le dipendenze
della tua applicazione, devi installare Node.js in locale. Inoltre, con Node.js installato in locale
semplifichi la verifica, velocizzando lo sviluppo. 

Prima di cominciare, puoi prendere in considerazione di utilizzare un gestore delle versioni,
come Node Version Manager o `nvm`, per installare Node, riducendo la complessità della gestione di più versioni di Node.js. Al momento della redazione del presente documento, per installare o aggiornare `nvm` su una macchina Mac o Linux, puoi utilizzare lo script di installazione tramite cURL
nell'interfaccia CLI che hai appena aperto copiando e incollando uno dei comandi nei primi due esempi nella tua riga di comando
e premendo Invio (nota, questo presuppone che la tua shell sia BASH e non un'alternativa):

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Esempio 1. Utilizzo di cURL per installare nvm (Node Version Manager)" caption-side="bottom"}
`Example 1. Using cURL to install Node Version Manager (nvm)`
   
...oppure Wget (ne è necessario soltanto uno, non entrambi; utilizza quello presente sul tuo sistema):

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Esempio 2. Utilizzo di Wget per installare nvm (Node Version Manager)" caption-side="bottom"}
`Example 2. Using Wget to install Node Version Manager (nvm)`

Oppure, per Windows, puoi utilizzare [nvm per Windows](https://github.com/coreybutler/nvm-windows) con i programmi di installazione
e i codici di origine nel link.

Se non desideri la complessità aggiunta del supporto di più release di Node.js, visita il sito web
[Node.js](https://nodejs.org/en/download/releases/)
e installa la versione Long Term Support (LTS) di Node.js
che corrisponde all'ultima versione supportata dal pacchetto di build SDK for Node.js ora utilizzato su
{{site.data.keyword.cloud_notm}} Platform. Al momento della redazione del presente documento,
il pacchetto di build più recente è v3.26 e supporta Node.js community edition v6.17.0+. 

Puoi trovare ulteriori informazioni sul pacchetto di build {{site.data.keyword.cloud_notm}}
SDK for Node.js più recente nella pagina [Aggiornamenti più recenti di SDK for Nodejs](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates). 

Tramite `nvm` puoi installare la versione di Node che corrisponde ai requisiti copiando e installando il comando dall'esempio 3
nella tua riga di comando.

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="Esempio 3. Utilizzo di `nvm` per installare una versione specifica di Node.js" caption-side="bottom"}
`Example 3. Using nvm to install a specific version of Node.js`

Indipendentemente da quale approccio utilizzi, dopo aver seguito le istruzioni per installare Node.js e npm (incluso con Node)
sul tuo computer, come appropriato per il sistema operativo e la strategia che stai utilizzando, congratulati con te stesso
perché hai iniziato con il piede giusto!

### Installazione di Git
{: #tutorial-wa-install-git}

Probabilmente già conosci with Git, poiché è il sistema di controllo della versione più largamente utilizzato
dagli sviluppatori che creano le applicazioni per il web.
Utilizzeremo Git in seguito quando creeremo una toolchain Continuous Deployment (CD) in {{site.data.keyword.cloud_notm}} Platform
per la fornitura continua e lo sviluppo. Se non hai un account GitHub, crea un account personale pubblico gratuito
nel sito web di [Github](https://github.com/join);
altrimenti, sentiti libero di accedere con un altro account a tua disposizione.

Tieni presente che esistono delle istruzioni dettagliate e importanti su come generare e caricare le chiavi SSH nel tuo
[profilo Github](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) per l'accesso sicuro a Github dalla riga di comando. Tuttavia, se
lo fai ora farai solo pratica perché dovrai ripetere la procedura
per l'istanza di Github utilizzata per {{site.data.keyword.cloud_notm}} Platform, a cui accederemo in seguito. Sebbene
la procedura per l'utilizzo delle chiavi SSH sia complicata, con la pratica, puoi prendere confidenza nell'utilizzo di SSH con la CLI.

Per ora, vai alla pagina [Github Desktop](https://desktop.github.com/) per scaricare
GitHub Desktop e poi esegui il programma di installazione. Quando il programma di installazione ha terminato l'installazione,
ti viene richiesto di accedere a GitHub con il tuo account.

Nella finestra di accesso (vedi la prima figura in questa esercitazione), immetti il nome e l'email che
vuoi vengano visualizzati pubblicamente (presupponendo che tu abbia un account pubblico) per tutti i commit
al tuo repository. Dopo aver collegato l'applicazione al tuo account, potrebbe venirti richiesto
di verificare la connessione dell'applicazione tramite il tuo account Github online.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

Non hai ancora creato alcun repository. Se noti un repository denominato Tutorial incluso con GitHub Desktop,
sentiti libero di utilizzarlo per prendere familiarità con le operazioni. Hai appena completato la parte dei prerequisiti
di questa esercitazione. Sei pronto a creare un'applicazione?

## Creazione dell'applicazione starter Node.js utilizzando Developer Tools
{: #tutorial-create-skeleton}

Per iniziare a sviluppare la tua applicazione in locale, inizia accedendo a {{site.data.keyword.cloud_notm}} Platform direttamente
dalla riga di comando, come mostrato nell'esempio 4. 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="Esempio 4. Comando per accedere a IBM Cloud Platform utilizzando la CLI Developer Tools" caption-side="bottom"}
`Example 4. Command to login to the IBM Cloud Platfirm using CLI Developer Tools`

Puoi specificare dei parametri facoltativi se lo desideri: la tua organizzazione con l'opzione -o e lo spazio con -s oppure
se stai utilizzando un account federato: --sso. Quando accedi potrebbe venirti richiesto
di scegliere una versione, per gli scopi di questa esercitazione seleziona `us-south` per la regione, poiché questa stessa opzione sarà utilizzata quando crei una toolchain CD,
più avanti in questa esercitazione.  

Successivamente, configura l'endpoint (se non lo è già) tramite il comando mostrato nell'esempio 5. Sono possibili altri endpoint
cosa preferibile per l'utilizzo in produzione, ma per ora, utilizza il codice come mostrato, se appropriato per il tuo account.

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="Esempio 5. Comando per configurare l'endpoint API per il tuo account." caption-side="bottom"}
`Example 5. Command to set the API endpoint for your account`

Seleziona l'aspetto Cloud Foundry (cf) di {{site.data.keyword.cloud_notm}} Platform utilizzando il codice mostrato
nell'esempio 6, tramite il comando target e l'opzione --cf. L'API `cf` è integrata nella CLI Developer Tools.

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="Esempio 6. Configurazione delle tue opzioni per l'utilizzo dell'API Cloud Foundry." caption-side="bottom"}
`Example 6. Setting your options for using the Cloud Foundry API`

Ora è arrivato il momento di iniziare a lavorare: la creazione di un'applicazione web inizia con il codice mostrato nell'esempio 7. Lo spazio `dev`
è un'opzione predefinita per la tua organizzazione, ma potresti volerne creare degli altri per isolare impegni diversi, ad esempio mantenendo la 'finanza'
separata dallo 'sviluppo'.

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="Esempio 7. Comando per creare un'applicazione utilizzando IBM Cloud Developer Tools" caption-side="bottom"}
`Example 7. Command to create an app using IBM Cloud Developer Tools`

Con tale comando, ti verranno fatte alcune domande. Puoi tornare indietro a qualsiasi punto del processo, ma se ti senti perso,
o hai saltato dei passi, sentiti libero di ricominciare eliminando la directory o creandone un'altra per
il test e l'esplorazione. Inoltre, quando completi il processo creando la tua applicazione in locale sulla riga di comando, potrai vedere i risultati online successivamente,
nel tuo portale online {{site.data.keyword.cloud_notm}} in cui hai creato il tuo account per gestire
le risorse create.

Nell'esempio 8, nota l'opzione per la creazione della 'applicazione web' che desideri. Immetti '2' e premi Invio.

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
{: caption="Esempio 8. Output dal comando `ibmcloud dev create` in cui hai selezionato l'opzione #2, per un'applicazione web" caption-side="bottom"}
`Example 8. Output from the command ibmcloud dev create where you select option #2, for a Web App`

Esistono molte opzioni nell'esempio 9 basate su quelli che vengono chiamati "pacchetti di build" e nota l'opzione per l'utilizzo di 'Node'. Immetti '4' e premi Invio.

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
{: caption="Esempio 9. Opzioni di linguaggio da `ibmcloud dev create` - Continua." caption-side="bottom"}
`Example 9. Language options from ibmcloud dev create continued`

Dopo aver effettuato la tua selezione per il framework e/o il linguaggio di programmazione, la selezione successiva mostrata nell'esempio 10
avrà così tante opzioni, che potresti scorrere oltre il tuo servizio desiderato. Come puoi vedere nell'esempio,
desideriamo utilizzare una semplice applicazione web Node.js con Express.js. Immetti '6' e premi Invio.

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
{: caption="Esempio 10. Opzioni dell'applicazione di base da `ibmcloud dev create`." caption-side="bottom"}
`Example 10. Skeleton application options from ibmcloud dev create`

Ora che hai scelto le opzioni più semplici, l'opzione più difficile per tutti gli sviluppatori è ancora necessaria: la denominazione della tua applicazione. Segui
l'esempio mostrato nell'esempio 11 e immetti 'webapplication' e premi quindi Invio.

```bash
? Enter a name for your application> webapplication
```
{: caption="Esempio 11. Assegna il nome 'webapplication' alla tua applicazione utilizzando `ibmcloud dev create`." caption-side="bottom"}
`Example 11. Name your application 'webapplication' using ibmcloud dev create`

Successivamente, puoi aggiungere tutti i servizi necessari o desiderati, ad esempio gli archivi dati o le funzioni di calcolo, tramite la console web. Tuttavia, come mostrato nell'esempio 12, immetti 'n' per no quando ti viene chiesto se vuoi aggiungere i servizi in questo momento.

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="Esempio 12. Opzione per aggiungere i servizi quando utilizzi il comando `ibmcloud dev create` - Continua." caption-side="bottom"}
`Example 12. Option to add services when using ibmcloud dev create continued`

Precedentemente, i vantaggi dello sviluppo con i contenitori, invece dei ServerIron tradizionali o anche dei server virtuali,
sono stati menzionati quando di parlava di Docker. Un modo di gestire i contenitori è con un software di orchestrazione, come Kubernetes,
che è diventato uno standard _di fatto_ nello sviluppo. Ma per questa esercitazione, possiamo lasciare che il servizio Cloud Foundry gestisca
un singolo contenitore Docker che conterrà il codice, le librerie e la configurazione necessari alla tua applicazione.

Come mostrato nell'esempio 13, immetti '1' e premi Invio per utilizzare 'IBM DevOps' per l'integrazione di CD nel
tuo ciclo di vita del progetto.
 
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
{: caption="Esempio 13. Opzioni di sviluppo da `ibmcloud dev create`." caption-side="bottom"}
`Example 13. Deployment options from ibmcloud dev create`

Come notato in precedenza, sceglieremo una regione per la nostra toolchain CD di distribuzione automatizzata, per cui seleziona la stessa opzione di prima,
'5' come mostrato nell'esempio 14.

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
{: caption="Esempio 14. Regioni disponibili come opzioni in `ibmcloud dev create`." caption-side="bottom"}
`Example 14. Regions available as options in ibmcloud dev create`

A questo punto, la generazione di una nuova applicazione ci ricorderà che la toolchain utilizzata
per distribuire la tua applicazione successivamente, avrà bisogno di ulteriore configurazione, come mostrato nell'esempio 15. Come menzionato precedentemente,
il caricamento della tua chiave pubblica in Github (nell'istanza toolchain CD su {{site.data.keyword.cloud_notm}}
Platform), richiederà la fornitura dell'applicazione distribuita tramite Github. Puoi trovare ulteriori istruzioni dopo aver distribuito la tua applicazione
ed eseguito l'accesso al tuo account IBM Cloud GitLab all'indirizzo [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair).

```

Note: For successful connection to the DevOps toolchain, this machine 
must be configured for SSH access to your IBM Cloud GitLab account at 
https://git.ng.bluemix.net/profile/keys in order to download the 
application code.


```
{: caption="Esempio 15. Tieni conto della nota fornita: chiavi SSH dal comando `ibmcloud dev create`." caption-side="bottom"}
`Example 15. Note given re: SSH keys by the ibmcloud dev create`

Ulteriori prompt confermeranno il nome dell'applicazione e della toolchain che hai definito in precedenza. L'esempio 16 mostra come puoi modificare i nomi
dell'host e della toolchain, se lo desideri. Il nome host deve essere univoco per il dominio utilizzato come endpoint di servizio della tua applicazione, ma se non c'è alcun conflitto,
puoi semplicemente premere Invio quando richiesto per la conferma.

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="Esempio 16. Conferma dei nomi delle proprietà in `ibmcloud dev create`." caption-side="bottom"}
`Example 16. Confirming names for properties in ibmcloud dev create`

Se copi e incolli il link fornito alla fine dell'output che hai ricevuto come risultato dell'utilizzo del comando `ibmcloud dev create`,
potrai accedere alla tua toolchain CD. Ma, puoi anche accedere dalla console in un secondo momento, nel caso hai dimenticato di acquisire il link.
Seguono delle ulteriori informazioni, mentre il processo continua, come mostrato nell'esempio 17, in cui le voci dell'applicazione sono state create online
ed è stata creata una directory con il codice di esempio. 

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
{: caption="Esempio 17. Conferma delle azioni generate da `ibmcloud dev create`." caption-side="bottom"}
`Example 17. Confirmation of actions generated by ibmcloud dev create`

L'ultima istruzione dell'esempio 17 significa che se visualizzi la tua directory corrente, una nuova directory secondaria `webapplication`
dovrebbe ora essere visibile. Nella directory `webapplication` troverai una struttura della tua nuova applicazione Node.js. Tuttavia, mentre la ricetta
può essere presente, gli ingredienti stessi, ancora impacchettati in un'immagine Docker, devono essere "bolliti"&mdash;scusami,
creati&mdash;utilizzando il comando nell'esempio 18. Docker dovrebbe essere eseguito sulla tua macchina locale come conseguenza dell'installazione,
ma se hai bisogno di riavviarlo, fallo pure. Tutti i tentativi di creare la tua nuova applicazione web senza Docker in esecuzione avranno esito negativo,
ma questo non è l'unico motivo possibile. Se si verifica un problema, controlla i messaggi di errore risultanti che possono avere il link appropriato
per visualizzare i log dei risultati nel tuo portale online per il tuo account {{site.data.keyword.cloud_notm}} Platform.

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Esempio 18. Comando build di {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Example 18. IBM Cloud Platform build command`

In aggiunta alla creazione dell'applicazione per la distribuzione, la creazione dell'applicazione ti consente di eseguire lo stesso codice in locale con il comando `run`
(dopo aver copiato e incollato o immesso il comando dall'esempio 19). Quando terminato, copia e incolla l'URL fornito nella tua barra di indirizzi
del browser, normalmente, <http://localhost:3000>.

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Esempio 19. Comando CLI di {{site.data.keyword.cloud_notm}} Platform per eseguire la tua applicazione" caption-side="bottom"}

Ora che la tua applicazione è stata creata e definita, visualizzala per confermare che funzioni. Se vedi un'immagine segnaposto come mostrato
nella figura 2, congratulazioni! Hai creato una nuova applicazione web Node.js e sei pronto per distribuirla al cloud.

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="Figura 2. Nuova applicazione Node.js: congratulazioni!" caption-side="top"}

Distribuisci l'applicazione a {{site.data.keyword.cloud_notm}} Platform con il comando deploy (come mostrato nell'esempio 20).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Esempio 20. Comando CLI di {{site.data.keyword.cloud_notm}} Platform per caricare e distribuire la tua applicazione" caption-side="bottom"}
`Example 20. IBM Cloud Platform CLI command to upload and deploy your app`

L'URL sarà di nuovo visualizzato come risultato dell'esecuzione del comando `ibmcloud dev deploy` in base all'endpoint regionale
e al nome host specificati in precedenza. Se si verifica un problema, puoi vedere i link ai log archiviati nel tuo portale
in {{site.data.keyword.cloud_notm}} Platform. Se non c'è alcun problema, dovresti vedere una visualizzazione identica nel tuo browser
dell'applicazione locale che hai appena visitato. Procedi e visita la tua nuova applicazione web nel cloud!

## Creazione dell'applicazione Web Gallery utilizzando un'applicazione di esempio
{: #tutorial-create-app}

Richiamiamo i prerequisiti necessari per lo sviluppo di un'applicazione Node.js su {{site.data.keyword.cloud_notm}} Platform. Hai già
creato il tuo account {{site.data.keyword.cloud_notm}} Platform ed installato Developer Tools,
che hanno installato Docker. In seguito hai installato Node.js. L'ultimo elemento elencato come prerequisito per questa esercitazione era Git, che approfondiremo ora.  

Cominceremo ad utilizzare le specifiche per lavorare con la galleria di immagini in Node.js. Per ora, utilizzeremo Github Desktop
per questo scenario, ma potresti anche utilizzare il client di riga di comando Git per completare le stesse attività. Per iniziare, clona una template starter
per la tua nuova applicazione web. 

Segui questa procedura:

1.  Clona il repository elencato nell'esempio 21. Scarica il template per la tua applicazione sul tuo ambiente
    di sviluppo locale utilizzando Git. Invece di clonare l'applicazione di esempio
    da {{site.data.keyword.cloud_notm}} Platform, utilizza il comando nell'esempio 21 per clonare il template
    starter per l'applicazione {{site.data.keyword.cos_full_notm}} Web Gallery. Dopo aver clonato il repository
    troverai l'applicazione starter nella directory
    COS-WebGalleryStart. Apri una finestra Git CMD e passa a una
    directory in cui vuoi clonare il repository Github. Utilizza il comando mostrato
    nel primo esempio di questa esercitazione.

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="Esempio 21. Dettagli del comando clone di Git" caption-side="bottom"}
`Example 21. Git clone command details`

2.  Esegui l'applicazione localmente. Apri un'applicazione terminale fornendo una CLI e modifica la tua directory di lavoro con
    la directory COS-WebGalleryStart. Tieni presente che le dipendenze Node.js
    sono elencate nel file package.json. Scaricale utilizzando il comando mostrato di seguito,
    nell'esempio 22.

```bash
npm install
```
{: codeblock}
{: caption="Esempio 22. Installa Node Package Manager (npm)" caption-side="bottom"}
`Example 22. Node Package Manager (npm) install`

3.  Esegui l'applicazione utilizzando il comando mostrato nell'esempio 23.

```bash
npm start
```
{: codeblock}
{: caption="Esempio 23. Dettagli dell'avvio della tua applicazione utilizzando npm" caption-side="bottom"}
`Example 23. Details on starting your app with npm`

Apri un browser e visualizza la tua applicazione sull'indirizzo e la porta prodotti
nella console, <http://localhost:3000>.

**Suggerimento**: per riavviare l'applicazione localmente, termina il processo Node (Ctrl+C)
per arrestarlo e utilizza nuovamente `npm start`. Tuttavia, mentre sviluppi nuove funzioni, utilizzare nodemon per riavviare la tua applicazione quando
rileva una modifica permette di risparmiare del tempo. Installa nodemon globalmente in questo modo:
`npm install -g nodemon`. Successivamente eseguilo dalla riga di comando nella tua directory dell'applicazione
utilizzando: `nodemon`, in modo che 'nodemon' avvii la tua applicazione.

4.  Prepara l'applicazione per lo sviluppo. Aggiorna il valore della proprietà del nome dell'applicazione
    nel file `manifest.yml` da COS-WebGallery, con il nome che hai immesso
    per la tua applicazione su {{site.data.keyword.cloud_notm}} Platform e le altre informazioni come mostrato nell'esempio 24,
    se necessario. L'applicazione `manifest.yml` è simile al seguente esempio. Inoltre, puoi personalizzare il file `package.json`
    ubicato nella directory root dell'applicazione con il nome
    della tua applicazione e il tuo nome come autore.

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
{: caption="Esempio 24. Contenuti di `manifest.yml`" caption-side="bottom"}
`Example 24. Contents of manifest.yml`

**Suggerimento**: questo è il punto in cui potresti dover configurare le chiavi SSH per trasmettere il codice in modo interattivo alla tua origine remota. Se imposti una
    passphrase per la tua chiave SSH, ti viene richiesto di immettere questo codice ogni volta che esegui il push delle tue modifiche all'origine remota per il tuo
    repository. 

5.  Rimuovi e sostituisci i contenuti della tua directory `webapplication` con quelli della directory che hai appena modificato, `COS-WebGalleryStart`.
    Utilizzando le tue competenze Git ottimizzate, aggiungi i file che sono stati eliminati e aggiunti al repository con la CLI o
    Github Desktop. Successivamente, esegui il push delle modifiche all'origine del repository. In futuro, potrai apportare delle modifiche alla tua applicazione web basata sul cloud
    semplicemente eseguendo il push delle modifiche a Git. La toolchain CD riavvierà automaticamente, come per magia, il processo del server
    dopo aver clonato le tue modifiche e averle messe sul server. 


In sostanza, stiamo ricodificando la nostra applicazione, per cui ripeteremo il processo di build. Ma questa volta utilizzeremo il nuovo codice di Image Gallery. 

###Distribuisci l'applicazione a {{site.data.keyword.cloud_notm}} Platform.### 

Per iniziare ad utilizzare l'applicazione con le tue modifiche
    con {{site.data.keyword.cloud_notm}} Platform, distribuiscila utilizzando Developer Tools ripetendo la stessa procedura eseguita
    precedentemente.

a.  Se non lo hai ancora fatto, se hai eseguito un riavvio o ti sei scollegato, accedi a {{site.data.keyword.cloud_notm}} Platform
utilizzando il comando login. Come promemoria ti viene mostrato nell'esempio 25 e tieni presente che puoi specificare dei parametri facoltativi se lo desideri: la tua organizzazione con l'opzione -o e lo spazio con -s oppure
se stai utilizzando un account federato: --sso. Ricorda
di scegliere la stessa regione che hai utilizzato fino a questo punto, se ti viene richiesto.

```bash
ibmcloud login
```
{: codeblock}
{: caption="Esempio 25. Comando CLI per l'accesso a {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Example 25. CLI command for logging into the IBM Cloud Platform`

b.  Configura l'endpoint API per la tua regione utilizzando il comando api
        (come mostrato con i segnaposto facoltativi nell'esempio 6). Se non conosci l'URL dell'endpoint API regionale,
        vedi la pagina introduttiva.

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="Esempio 26. Endpoint API {{site.data.keyword.cloud_notm}} Platform " caption-side="bottom"}
`Example 26. IBM Cloud Platform API endpoint`

c.  Seleziona l'aspetto Cloud Foundry di {{site.data.keyword.cloud_notm}} Platform utilizzando il codice mostrato
nell'esempio 27, tramite il comando target e l'opzione --cf. 


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="Esempio 27. CLI {{site.data.keyword.cloud_notm}} Platform che seleziona Cloud Foundry" caption-side="bottom"}
`Example 27. IBM Cloud Platform CLI targeting Cloud Foundry`

d.  Crea l'applicazione per la distribuzione di tale applicazione con il comando build (come nell'esempio 28).

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Esempio 28. Comando build di {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Example 28. IBM Cloud Platform build command`

g.  Continua e verifica l'applicazione localmente. In aggiunta alla creazione dell'applicazione per la distribuzione, la creazione dell'applicazione ti consente di eseguire lo stesso codice in locale con il comando run
(dopo aver immesso il comando dall'esempio 29). 


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Esempio 29. Comando CLI di {{site.data.keyword.cloud_notm}} Platform per eseguire la tua applicazione" caption-side="bottom"}
`Example 29. IBM Cloud Platform CLI command to run your app`

h.  Distribuisci l'applicazione a {{site.data.keyword.cloud_notm}} Platform con il comando deploy
(come mostrato nell'esempio 30).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Esempio 30. Comando CLI di {{site.data.keyword.cloud_notm}} Platform per caricare e distribuire" caption-side="bottom"}
`Example 30. IBM Cloud Platform CLI command to upload and deploys`

Il codice nell'esempio 31 mostra la sequenza di comandi utilizzati in questo esempio per creare, verificare e distribuire l'applicazione web iniziale.

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
{: caption="Esempio 31. Elenco di comandi CLI {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Example 31. IBM Cloud Platform CLI command list`

Se ha esito positivo, {{site.data.keyword.cloud_notm}} Platform segnala che l'applicazione è stata caricata,
correttamente distribuita e avviata. Se hai anche eseguito l'accesso alla console web di {{site.data.keyword.cloud_notm}} Platform,
viene avvisato anche dello stato della tua applicazione. Ma, cosa più importante, puoi verificare che l'applicazione
è stata distribuita visitando l'URL dell'applicazione segnalato da {{site.data.keyword.cloud_notm}} Platform con un browser o dalla
console web facendo clic sul pulsante View App.

5.  Verifica l'applicazione. La modifica visibile dal template dell'applicazione predefinito che era stato
    distribuito alla creazione dell'applicazione starter mostrata in seguito,
    dimostra che la distribuzione dell'applicazione a {{site.data.keyword.cloud_notm}} Platform ha avuto esito positivo.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### Crea un ramo Git
{: #tutorial-create-branch}

Ora, devi creare un ramo per l'ambiente di sviluppo locale
da utilizzare con la tua fase di build di {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline:

1.  Se utilizzi Github Desktop, fai clic sull'icona del ramo; ti viene richiesto di immettere un nome per il ramo
    (vedi la figura 14). Questo esempio utilizza Local-dev per il nome.

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  Dopo aver creato il ramo, GitHub confronta i file locali sul ramo
    Local-dev con quelli nel repository sul ramo master
    e segnala che non ci sono modifiche locali. Puoi ora fare clic su Publish per aggiungere il ramo
    creato sul tuo repository locale al tuo repository GitHub
    (come mostrato nella figura 5).

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

Ora che il ramo Local-dev è stato pubblicato nel repository GitHub nella tua
toolchain, la fase di build di {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline sarà attivata
e inizierà la fase di distribuzione ogni volta che esegui il push di un commit ad esso.
La distribuzione dell'applicazione dalla CLI non sarà più necessaria, poiché la distribuzione è stata integrata direttamente nel tuo flusso di lavoro.

### Configurazione delle tue credenziali di archiviazione {{site.data.keyword.cos_full_notm}} 
{: #tutorial-credentials}

Devi configurare le credenziali {{site.data.keyword.cos_short}} per la tua applicazione web, nonché un 'bucket'
dove saranno archiviate e richiamate le immagini. La chiave API che creerai ha bisogno delle credenziali {{site.data.keyword.cos_short}} HMAC, come definito dalle tue
[Credenziali del servizio](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials).
Puoi riconoscere i termini `access_key_id` e `secret_access_key` e puoi avere un account AWS e utilizzare un file
di credenziali che hanno già le voci `aws_access_key_id` e `aws_secret_access_key`. 

Dopo aver completato la creazione di una chiave API, scaricato e copiato le credenziali HMAC, completa la seguente procedura:

1.  Sull'ambiente di sviluppo locale, posiziona le credenziali nel percorso
    Windows `%USERPROFILE%\\.aws\\credentials` (per utenti Mac/Linux, le credenziali devono andare in
    `~/.aws/credentials)`. L'esempio 32 mostra i contenuti di un file
    di credenziali tipico.

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="Esempio 32. Come sono definite le credenziali nel tuo file `~/.aws/credentials` " caption-side="bottom"}
`Example 32. Credentials as they are defined in your ~/.aws/credentials file`

2.  Nella pagina web dell'applicazione che hai creato utilizzando il comando CLI su {{site.data.keyword.cloud_notm}} Platform,
    definisci le tue credenziali richieste come variabili di ambiente per le procedure consigliate di sviluppo
    eseguendo l'accesso a {{site.data.keyword.cloud_notm}} Platform e, nelle applicazioni Cloud Foundry, seleziona
    la tua applicazione, 'webapplication.' Dalle schede, fai clic su Runtime.

3.  Nella finestra Runtime, fai clic sulle variabili di ambiente all'inizio della pagina
    e scorri fino alla sezione definita dall'utente, che ti consente di aggiungere
    le variabili.

4.  Aggiungi due variabili: una con il valore del tuo access_key_id, utilizzando `AWS_ACCESS_KEY_ID` come nome della chiave
    e l'altra con il valore della tua chiave di accesso segreta, denominata `AWS_SECRET_ACCESS_KEY`.
    Queste variabili e i loro rispettivi valori sono utilizzati dall'applicazione per l'autenticazione con l'istanza di
    {{site.data.keyword.cos_short}} quando eseguita su {{site.data.keyword.cloud_notm}}
    Platform (vedi la figura 6). Quando hai terminato con le voci,
    fai clic su Save e {{site.data.keyword.cloud_notm}} Platform riavvierà automaticamente l'applicazione al tuo posto.

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

Successivamente, oltre al {{site.data.keyword.cos_short}} Portal per la tua istanza del servizio,
aggiungi un bucket per contenere le tue immagini. Questo scenario utilizza il bucket denominato `web-images`.


## Personalizzazione di un'applicazione web {{site.data.keyword.cos_full_notm}} Image Gallery Node.js
{: #tutorial-develop}

Poiché questo esempio utilizza un'architettura MVC, la modifica della struttura di directory
nel tuo progetto per rispecchiare questa architettura è conveniente nonché una procedura consigliata.
La struttura di directory ha una directory di viste per contenere i template di visualizzazione EJS, una directory di rotte
per contenere le rotte espresse e una directory di controller come posizione
in cui inserire la logica del controller. Inserisci questi elementi in una directory di origine principale
denominata src (vedi la figura 7).

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**Suggerimento**: il repository che hai clonato precedentemente contiene una directory denominata
COS-WebGalleryEnd. La visualizzazione del codice di origine dell'applicazione completata nel tuo editor preferito
potrebbe essere utile quando segui le prossime fasi. Questa sarà la versione della tua
'webapplication' di cui viene eseguito il commit e la distribuzione a {{site.data.keyword.cloud_notm}} Platform
quando completi questa esercitazione.

### Progettazione dell'applicazione
{: #tutorial-develop-design}

Esistono due attività principali che un utente dovrebbe poter effettuare con l'applicazione
web della galleria di immagini semplice:

  - Caricare le immagini da un browser web nel bucket {{site.data.keyword.cos_short}}.
  - Visualizzare le immagini nel bucket {{site.data.keyword.cos_short}} in un browser web.

La seguente procedura si focalizza su come soddisfare queste due funzioni dimostrative invece di creare un'applicazione
completamente sviluppata a livello di produzione. La distribuzione di questa esercitazione e lasciarla esposta e in esecuzione significa che chiunque trova l'applicazione
può eseguire le stesse azioni: caricare i file sul tuo bucket {{site.data.keyword.cos_full_notm}} e visualizzare tutte le immagini JPG già presenti nei loro browser.

### Sviluppo dell'applicazione
{: #tutorial-develop-app}

Nel file `package.json`, nell'oggetto scripts,
vedi come viene definito "start" (esempio 33). Questo file
viene utilizzato da {{site.data.keyword.cloud_notm}} Platform per far sapere a Node di eseguire app.js ogni volta che viene avviata
l'applicazione. Lo utilizza anche durante la verifica dell'applicazione in locale. Dai un'occhiata al file dell'applicazione principale, denominato app.js. Questo è il codice che abbiamo detto a Node.js di elaborare per primo quando avvii la tua applicazione con il comando `npm start` (o nodemon). 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="Esempio 33. Indicare alla tua applicazione come eseguire il bootstrap del tuo codice personalizzato" caption-side="bottom"}
`Example 33. Telling your app how to bootstrap your custom code`

Il nostro file app.js inizia con il codice mostrato nell'esempio 34.
In un primo momento, il codice utilizza il nodo per caricare i moduli necessari per iniziare.
Il framework Express crea l'applicazione come un singleton semplice denominato `app`.
L'esempio termina (tralasciando la maggior parte del codice per ora) indicando all'applicazione
di ascoltare sulla porta assegnata e una proprietà di ambiente o 3000 per impostazione predefinita.
Quando avviata correttamente all'inizio, visualizza a schermo un messaggio con l'URL del server sulla console.

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
{: caption="Esempio 34. La tua applicazione web ha un avvio semplice ma potente" caption-side="bottom"}
`Example 34. Your Web Application has a humble, but powerful, start`

Vediamo come l'esempio 35 mostra come definire un percorso e le viste. La prima riga di codice indica al framework
Express di utilizzare la directory pubblica per fornire i nostri file statici, che includono
tutte le immagini statiche e i fogli di stile che utilizziamo. Le seguenti righe indicano all'applicazione
dove trovare i template per le nostre viste nella directory
src/views e impostano il nostro motore delle viste in modo che sia EJS. Inoltre, il framework utilizzerà il
middleware body-parser per esporre i dati della richiesta in entrata
all'applicazione come JSON. Nelle righe di chiusura dell'esempio, l'applicazione express risponde a tutte le richieste
GET in entrata nel nostro URL dell'applicazione eseguendo il rendering del template di visualizzazione index.ejs.

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
{: caption="Esempio 35. Ubicazioni template e viste dell'applicazione web" caption-side="bottom"}
`Example 35. Web app views and template locations`

La seguente figura mostra di quale template di visualizzazione di indice viene eseguito il rendering
e inviato al browser. Se utilizzi `nodemon` puoi aver notato che
il tuo browser si è aggiornato quando hai salvato le tue modifiche e che la tua applicazione può essere simile alla figura 8.

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

Nell'esempio 36, i nostri template di visualizzazione condividono il codice HTML tra le tag
&lt;head&gt;...&lt;/head&gt;, per cui lo posizioniamo in un template di inclusione separato
(vedi l'esempio 16). Questo template (head-inc.ejs)
contiene uno scriptlet e un bind per una variabile JavaScript e per il titolo della pagina alla riga 1.
La variabile `title` è impostata in `app.js` e passata come i dati
del nostro template di visualizzazione nella riga seguente. Altrimenti, stiamo semplicemente utilizzando alcuni indirizzi CDN
per eseguire il pull in Bootstrap CSS, Bootstrap JavaScript e JQuery. Infine, aggiungiamo un file
styles.css statico personalizzato dalla nostra directory di fogli di stile/pubblici.

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
{: caption="Esempio 36. Elementi HTML da head-inc.ejs" caption-side="bottom"}
`Example 36. HTML elements from head-inc.ejs`

Il corpo della vista di indice contiene le nostre schede di navigazione in formato bootstrap
(vedi l'esempio 37) e il nostro formato di caricamento in un layout di base fornito dagli stili
CSS inclusi con il bootstrap.

Considera queste due specifiche per la nostra applicazione:

-   Abbiamo impostato il nostro metodo form su POST e il tipo di codifica form-data con
    multipart/form-data alla riga 24. Per l'azione form, inviamo i dati
    dal nostro form all'applicazione alla rotta dell'applicazione "/". Successivamente, eseguiamo un ulteriore lavoro
    nella nostra logica del router per gestire le richieste POST
    su tale rotta.

-   Vogliamo visualizzare il feedback sullo stato del caricamento di file tentato
    dall'utente. Questo feedback viene passato alla nostra vista in una variabile
    denominata "status" e viene visualizzato sotto il formato di caricamento.

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
{: caption="Esempio 37. Elementi HTML da index.ejs" caption-side="bottom"}
`Example 37. HTML elements from index.ejs`

Prendiamoci un momento per ritornare a `app.js` nell'esempio 38. L'esempio configura le rotte Express
per gestire le richieste aggiuntive che saranno effettuate alla nostra applicazione. Il codice
per questi metodi di instradamento sarà in due file nella directory `./src/routes`
nel tuo progetto:

-   imageUploadRoutes.js: questo file gestisce cosa succede quando l'utente
    seleziona un'immagine e fa clic su Upload.

-   galleryRoutes.js: questo file gestisce le richieste quando l'utente fa clic
    sulla scheda Gallery per richiedere la vista imageGallery.

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
{: caption="Esempio 38. Esempi di router Node Express" caption-side="bottom"}
`Example 38. Node Express router examples`

#### Caricamento di immagini
{: #tutorial-develop-image-upload}

Vedi il codice da 'imageUploadRoutes.js' nell'esempio 39. All'inizio dobbiamo creare un'istanza
di un nuovo router express e denominarla `imageUploadRoutes`.
Successivamente, creiamo una funzione che restituisce `imageUploadRoutes`,
e lo assegna a una variabile denominata `router`. Una volta terminato, la funzione deve essere
esportata come un modulo per renderla accessibile al framework e al nostro codice principale in app.js.
La separazione della nostra logica di instradamento dalla logica di caricamento richiede un file controller denominato
galleryController.js. Poiché tale logica è dedicata all'elaborazione della richiesta in entrata
e fornisce la riposta appropriata, la inseriamo in tale funzione e la salviamo nella
directory ./src/controllers.

L'istanza del router dal framework Express è dove il nostro file imageUploadRoutes
è progettato per instradare le richieste per la rotta dell'applicazione root ("/") quando viene utilizzato il metodo HTTP POST.
Nel metodo `post` del nostro imageUploadRoutes, utilizziamo il middleware dai moduli `multer` e
`multer-s3` esposti da galleryController come `upload`.
Il middleware prende i dati e i file dal nostro formato di caricamento POST,
li elabora ed esegue una funzione di callback. Nella funzione di callback
controlliamo di ottenere un codice di stato HTTP di 200 e di
aver almeno un file nel nostro oggetto di richiesta da caricare. In base a queste condizioni,
inviamo il feedback nella nostra variabile `status` ed eseguiamo il rendering del template
di visualizzazione dell'indice con il nuovo stato.

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
{: caption="Esempio 39. Dettagli del router Node express " caption-side="bottom"}
`Example 39. Node express router details`

In confronto, il codice per galleryRouter nell'esempio 40 è un modello di semplicità. Seguiamo lo stesso schema
utilizzato con imageUploadRouter e richiediamo galleryController sulla prima riga della funzione, quindi configuriamo la nostra rotta. La differenza principale è che instradiamo
richieste HTTP GET invece di POST ed inviamo tutto l'output
nella risposta da getGalleryImages, che viene esposto da
galleryController sull'ultima riga dell'esempio.

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
{: caption="Esempio 40. Dettagli del router Node express " caption-side="bottom"}
`Example 40. Node express router details`

Successivamente spostiamo la nostra attenzione sul controller per la galleria.

Tieni presente come abbiamo configurato il caricamento `multer` nell'esempio 41,
(che tronca una parte di codice che ignoreremo per ora). Abbiamo bisogno dei moduli
`ibm-cos-sdk`, `multer` e `multer-s3`. Il codice mostra come
configurare un oggetto S3 che punta a un endpoint server {{site.data.keyword.cos_short}}. Stiamo strategicamente configurando
dei valori come il bucket, la regione e l'indirizzo dell'endpoint per semplicità,
ma vi è possibile fare facilmente riferimento
da una variabile di ambiente o un file di configurazione JSON.

Definiamo `upload` come utilizzato nell'esempio 41 e definito in imageUploadRouter creando una nuova istanza
`multer` con `storage` come propria unica proprietà. Questa proprietà indica a
`multer` dove inviare il file dal nostro multipart/form-data. Poiché {{site.data.keyword.cloud_notm}}
Platform utilizza un'implementazione dell'API S3, configuriamo l'archiviazione in modo che sia un oggetto
`s3-multer`. Questo oggetto `s3-multer` contiene una proprietà `s3` che
abbiamo precedentemente assegnato al nostro oggetto `s3` e una proprietà bucket assegnata
alla variabile `myBucket`, a cui è assegnato un
valore di “web-images”. L'oggetto `s3-multer` ora ha tutti i dati necessari
per connettere e caricare i file nel nostro bucket {{site.data.keyword.cos_short}} quando li riceve
dal formato di caricamento. Il nome o la chiave dell'oggetto caricato
sarà il nome del file originale preso dall'oggetto file quando viene
archiviato nel nostro bucket {{site.data.keyword.cos_short}} “web-images”  

**Suggerimento**: utilizza una data/ora come parte del nome file per mantenere l'unicità del nome file. 

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
{: caption="Esempio 41. Dettagli del controller Node express " caption-side="bottom"}
`Example 41. Node express controller details`

Per un test locale, un'attività utile
è di visualizzare a schermo l'oggetto file nella console, `console.log(file)`.
Eseguiamo un test locale del formato di caricamento e mostriamo l'output dal log della console
del file nell'esempio 42.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="Esempio 42. Visualizzazione della console dell'oggetto di debug" caption-side="bottom"}
`Example 42. Console display of debug object`

Mentre vantarsi è sconveniente, la figura 9 mostra il feedback dal nostro callback
che dichiara che l'applicazione ha effettivamente: "caricato il file correttamente" durante la verifica.

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### Visualizzazione e richiamo dell'immagine
{: #tutorial-image-display}

Ricorda di nuovo app.js, la riga di codice `app.use('/gallery', galleryRouter);`
indica al framework express di utilizzare tale router quando viene richiesta la rotta “/gallery”.
Quel router, se ricordi, utilizza galleryController.js (vedi il codice nell'esempio 43), definiamo la funzione
getGalleryImages, la firma che abbiamo visto in precedenza. Utilizzando lo stesso oggetto `s3`
configurato per la nostra nuova funzione di caricamento immagini, richiamiamo la funzione denominata
`listObjectsV2`. Questa funzione restituisce i dati di indice che definiscono ogni oggetto
nel nostro bucket. Per visualizzare le immagini in HTML, abbiamo bisogno di un URL immagine per ogni immagine
JPEG nel nostro bucket `web-images` da visualizzare nel nuovo template di visualizzazione. La chiusura
con l'oggetto dati restituito da `listObjectsV2` contiene i metadati su
tutti gli oggetti nel nostro bucket. 

Il codice effettua una ricerca in `bucketContents` ricercando tutte le chiavi oggetto che terminano in ".jpg," e
crea un parametro da passare alla funzione S3 getSignedUrl. Questa funzione restituisce
un URL firmato per ogni oggetto quando forniamo la chiave e il nome del
bucket dell'oggetto. Nella funzione di callback salviamo ogni URL in un array
e lo passiamo al metodo della risposta server HTTP `res.render`
come valore della proprietà denominata `imageUrls`.

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
{: caption="Esempio 43. Contenuto parziale di galleryController.js" caption-side="bottom"}
`Example 43. Partial contents of galleryController.js`

L'ultimo esempio di codice, il numero 44 in questa esercitazione, mostra il corpo del template galleryView con il codice necessario
per visualizzare le immagini. Otteniamo l'array imageUrls dal metodo res.render()
e interagiamo con una coppia di tag &lt;div&gt;&lt;/div&gt; nidificate in cui
l'URL immagine effettuerà una richiesta GET per l'immagine quando viene richiesta
la rotta /gallery. 

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
{: caption="Esempio 44. Scriptlet di output e ricerca utilizzati nel template della galleria" caption-side="bottom"}
`Example 44. Loop and output scriptlets used in the gallery template`

Lo verifichiamo in locale da http://localhost:3000/gallery e visualizziamo la nostra immagine
nella figura 10.

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### Commit a Git
{: #tutorial-develop-commit}

Ora che la funzione di base dell'applicazione sta funzionando, eseguiremo il commit del nostro codice
al nostro repository locale e poi ne eseguiremo il push a GitHub. Utilizzando GitHub Desktop, facciamo clic su
Changes (vedi la figura 11), immettiamo un riepilogo delle modifiche nel campo
Summary e poi facciamo clic su Commit to Local-dev. 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

Quando facciamo clic su
Sync, il nostro commit viene inviato al ramo Local-dev remoto che abbiamo pubblicato su
GitHub e questa azione avvierà la fase di build seguita dalla fase di distribuzione
nella nostra Delivery Pipeline, come esemplificato nell'ultima figura in questa esercitazione, la numero 12. 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## Operazioni successive
{: #nextsteps}

Congratulazioni! Siamo andati insieme dall'inizio alla fine lungo questo percorso di creazione
di una galleria di immagini dell'applicazione web utilizzando {{site.data.keyword.cloud_notm}} Platform.
Ogni concetto che abbiamo trattato in questa introduzione di base può essere esplorato ulteriormente in
[{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/). 

Buona fortuna!
